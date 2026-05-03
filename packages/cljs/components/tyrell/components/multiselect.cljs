(ns ty.components.multiselect
  "Multiselect component with ty-tag integration for multiple selections"
  (:require
    [cljs-bean.core :as bean]
    [clojure.set]
    [clojure.string :as str]
    [ty.components.dropdown.common :as common]
    [ty.components.dropdown.desktop :as desktop]
    [ty.components.tag] ; Import ty-tag component
    [ty.css :refer [ensure-styles!]]
    [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load multiselect styles
(defstyles multiselect-styles "ty/components/multiselect.css")

;; =====================================================
;; VALUE HELPERS
;; =====================================================

(defn get-multiselect-value
  "Get value from either property or attribute (handles Replicant prop passing)"
  [^js el]
  (or (.-value el) ; First check property (for Replicant)
      (wcs/attr el "value"))) ; Then check attribute

;; =====================================================
;; TAG MANAGEMENT
;; =====================================================

(defn get-tag-value
  "Get value from either property or attribute"
  [^js tag]
  (or (.-value tag) ; First check property
      (.getAttribute tag "value"))) ; Then check attribute

(defn get-all-tags
  "Get all ty-tag elements from the component"
  [^js el]
  (->> (.querySelectorAll el "ty-tag")
       array-seq))

(defn get-selected-values
  "Get array of values from tags that have selected attribute"
  [^js el]
  (->> (get-all-tags el)
       (filter #(.hasAttribute % "selected"))
       (map get-tag-value)
       (filter some?) ; Remove nil values
       vec))

(defn all-options-selected?
  "Check if all available options are selected"
  [^js el]
  (let [all-tags (get-all-tags el)
        selected-count (->> all-tags
                            (filter #(.hasAttribute % "selected"))
                            count)
        total-count (count all-tags)]
    (and (> total-count 0)
         (= selected-count total-count))))

(defn update-tag-state!
  "Update a tag's selected state and attributes"
  [^js tag selected?]
  (let [parent (.-parentNode tag)]
    (if selected?
      (do
        (.setAttribute tag "selected" "")
        (.setAttribute tag "slot" "selected")
        (.setAttribute tag "dismissible" "true")
        ;; Force re-slotting by removing and re-adding the element
        (when parent
          (.removeChild parent tag)
          (.appendChild parent tag)))
      (do
        (.removeAttribute tag "selected")
        (.removeAttribute tag "slot")
        (.removeAttribute tag "dismissible")
        ;; Force re-slotting by removing and re-adding the element
        (when parent
          (.removeChild parent tag)
          (.appendChild parent tag))))))

(defn update-component-value!
  "Update the component's value attribute and property based on selected tags"
  [^js el]
  (let [selected-values (get-selected-values el)
        value-str (if (seq selected-values)
                    (str/join "," (map str selected-values))
                    "")]
    ;; Update both attribute and property for consistency
    (.setAttribute el "value" value-str)
    (set! (.-value el) value-str)
    value-str))

(defn update-placeholder-and-stub!
  "Show/hide placeholder and update stub classes based on selection"
  [^js el]
  (let [^js shadow-root (wcs/ensure-shadow el)
        selected-values (get-selected-values el)
        placeholder (.querySelector shadow-root ".dropdown-placeholder")
        stub (.querySelector shadow-root ".multiselect-stub")]
    ;; Update placeholder visibility
    (when placeholder
      (if (seq selected-values)
        (.add (.-classList placeholder) "hidden")
        (.remove (.-classList placeholder) "hidden")))
    ;; Update stub class for styling
    (when stub
      (if (seq selected-values)
        (.add (.-classList stub) "has-tags")
        (.remove (.-classList stub) "has-tags")))))

(defn initialize-selections!
  "Initialize selections from either pre-selected tags or value attribute/property
   Priority: 1. Tags with selected attribute, 2. Component value attribute/property"
  [^js el]
  (let [tags (get-all-tags el)
        ;; Check if any tags already have selected attribute
        has-preselected? (some #(.hasAttribute % "selected") tags)]

    (if has-preselected?
      ;; Use existing selected attributes and sync component value
      (do
        (doseq [tag tags]
          (when (.hasAttribute tag "selected")
            (update-tag-state! tag true)))
        (update-component-value! el))

      ;; Otherwise use value attribute/property to set selections
      (let [value-str (get-multiselect-value el) ; Use new helper function
            initial-values (if (and value-str (not-empty value-str))
                             (set (str/split value-str #","))
                             #{})]
        (doseq [tag tags]
          (let [tag-value (get-tag-value tag)
                should-be-selected (and tag-value
                                        (contains? initial-values tag-value))]
            (when should-be-selected
              (update-tag-state! tag true))))))))

;; =====================================================
;; REACTIVE STATE MANAGEMENT (Following input.cljs pattern)
;; =====================================================

(declare update-form-value! sync-selections-with-tags! set-form-value!)

(defn get-component-state
  "Get multiselect component state"
  [^js el]
  (or (.-tyMultiselectState el)
      ;; Initialize if not exists
      (let [initial-state {:open false
                           :search ""
                           :highlighted-index -1
                           :filtered-options []
                           :selected-values (get-selected-values el)}]
        (set! (.-tyMultiselectState el) initial-state)
        initial-state)))

;; =====================================================
;; EVENT DISPATCHING
;; =====================================================

(defn dispatch-change-event!
  "Dispatch custom change event for multiselect selection changes"
  [^js el selected-values action item]
  (let [detail (bean/->js {:values selected-values ; Clojure vector -> JS array
                           :action action ; "add" | "remove" | "clear" | "set"
                           :item item}) ; Added/removed item (or nil)
        event (js/CustomEvent. "change" #js {:detail detail
                                             :bubbles true
                                             :cancelable true})]
    (.dispatchEvent el event)))

(defn determine-change-action
  "Determine what type of change occurred by comparing old and new selected values"
  [old-values new-values]
  (let [old-set (set old-values)
        new-set (set new-values)
        added (clojure.set/difference new-set old-set)
        removed (clojure.set/difference old-set new-set)]
    (cond
      ;; Single addition
      (and (= (count added) 1) (empty? removed))
      {:action "add"
       :item (first added)}

      ;; Single removal  
      (and (= (count removed) 1) (empty? added))
      {:action "remove"
       :item (first removed)}

      ;; Clear all selections
      (and (seq old-values) (empty? new-values))
      {:action "clear"
       :item nil}

      ;; Complete replacement or multiple changes
      :else
      {:action "set"
       :item nil})))

(defn update-component-state!
  "Update component state and sync all dependent systems"
  [^js el updates]
  (let [state (get-component-state el)
        new-state (merge state updates)]
    (set! (.-tyMultiselectState el) new-state)

    ;; If selected-values changed, sync everything
    (when (contains? updates :selected-values)
      (let [old-selected-values (:selected-values state)
            new-selected-values (:selected-values updates)]
        ;; Update component value attribute
        (.setAttribute el "value" (str/join "," new-selected-values))
        ;; Update form value for HTMX compatibility
        (set-form-value! el new-selected-values)
        (sync-selections-with-tags! el new-selected-values)
        (update-placeholder-and-stub! el)

        ;; Dispatch change event if values actually changed
        (when (not= (set old-selected-values) (set new-selected-values))
          (let [{:keys [action item]} (determine-change-action old-selected-values new-selected-values)]
            (dispatch-change-event! el new-selected-values action item)))))

    new-state))

(defn parse-multiselect-value
  "Parse value attribute into set of selected values"
  [value-str]
  (cond
    ;;
    (sequential? value-str)
    value-str
    ;;
    (and value-str (not-empty value-str))
    (str/split value-str #",")
    :else
    nil))

(defn enrich-delta
  "Enrich attribute delta with derived state changes (following input.cljs pattern)"
  [delta el]
  (let [{:keys [selected-values]} (get-component-state el)]
    (if-not (contains? delta "value")
      delta
      ;; Value attribute changed - parse and compare with current state
      (let [new-selected (parse-multiselect-value (get delta "value"))
            new-selected-set (set new-selected)
            current-selected-set (set selected-values)]
        (if (not= current-selected-set new-selected-set)
          (assoc delta :selected-values new-selected)
          delta)))))

(defn sync-selections-with-tags!
  "Sync tag selected states with current selected-values state"
  [^js el selected-values]
  (let [selected-set (set selected-values)]
    (doseq [tag (get-all-tags el)]
      (let [tag-value (get-tag-value tag)
            should-be-selected (contains? selected-set tag-value)]
        (update-tag-state! tag should-be-selected)))))

(defn get-form-internals
  "Get ElementInternals for form participation"
  [^js el]
  (.-_internals el))

(defn set-form-value!
  "Set form value using ElementInternals with multiple values for HTMX compatibility.
   Creates FormData with multiple entries using same name (standard HTML pattern)."
  [^js el selected-values]
  (when-let [internals (get-form-internals el)]
    (let [element-name (.getAttribute el "name")]
      (if (and element-name (seq selected-values))
        ;; Multiple values with same name using FormData (HTMX standard)
        (let [form-data (js/FormData.)]
          (doseq [value selected-values]
            (.append form-data element-name (str value)))
          (.setFormValue internals form-data))
        ;; No selection or no name - empty form value
        (.setFormValue internals "")))))

(defn update-form-value!
  "Update form value after selection changes"
  [^js el]
  (let [selected-values (get-selected-values el)]
    (set-form-value! el selected-values)))

;; =====================================================
;; EVENT HANDLERS
;; =====================================================

(defn handle-tag-click!
  "Handle click on a tag in the options dropdown"
  [^js el ^js _ ^js event]
  (let [target (.-target event)]
    ;; Check if we clicked on a ty-tag or something inside it
    (when-let [tag (if (= (.toLowerCase (.-tagName target)) "ty-tag")
                     target
                     (.closest target "ty-tag"))]
      ;; Handle all tags except disabled ones
      (when-not (.hasAttribute tag "disabled")
        (.preventDefault event)
        (.stopPropagation event)

        (let [tag-value (get-tag-value tag)
              currently-selected? (.hasAttribute tag "selected")]
          (let [selected-values (get-selected-values el)]
            (when-not (contains? (set selected-values) tag-value)
              (update-component-state! el {:selected-values (conj selected-values tag-value)})))
          (when (all-options-selected? el)
            (desktop/close-dropdown! el))
          nil)))))

(defn handle-tag-dismiss!
  "Handle dismiss event from a selected tag"
  [^js el ^js _ ^js event]
  (.preventDefault event)
  (.stopPropagation event)

  (let [tag (.-target (.-detail event))
        tag-value (get-tag-value tag)
        selected-values (get-selected-values el)]
    (update-component-state! el {:selected-values (remove #{tag-value} selected-values)})))

(defn handle-stub-click!
  "Handle click on the stub area"
  [^js el ^js shadow-root ^js event]
  (let [target (.-target event)]
    ;; Don't open dropdown if clicking on a selected tag or dismiss button
    (when-not (or (= (.toLowerCase (.-tagName target)) "ty-tag")
                  (.closest target "ty-tag")
                  (.closest target ".tag-dismiss"))
      ;; Don't open dropdown if all options are already selected
      (when-not (all-options-selected? el)
        ;; Use desktop dropdown's stub click handler
        (desktop/handle-stub-click! el shadow-root event)))))

;; =====================================================
;; SETUP AND CLEANUP
;; =====================================================

(defn setup-tag-listeners!
  "Setup event listeners for all ty-tag elements"
  [^js el ^js shadow-root]
  ;; Remove any existing listeners first
  (when-let [listeners (.-tyTagListeners el)]
    (doseq [[tag handler] listeners]
      (.removeEventListener tag "dismiss" handler))
    (set! (.-tyTagListeners el) nil))

  ;; Add new listeners
  (let [tags (get-all-tags el)
        listeners (atom [])]
    (doseq [tag tags]
      (let [handler (partial handle-tag-dismiss! el shadow-root)]
        (.addEventListener tag "dismiss" handler)
        (swap! listeners conj [tag handler])))
    (set! (.-tyTagListeners el) @listeners)))

;; =====================================================
;; RENDERING
;; =====================================================

(defn render!
  "Main render function for multiselect"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        {:keys [placeholder disabled label required]} (common/dropdown-attributes el)]

    ;; Ensure styles are loaded
    (common/ensure-dropdown-styles! root)
    (ensure-styles! root multiselect-styles "ty-multiselect")

    ;; Create structure if it doesn't exist
    (when-not (.querySelector root ".multiselect-container")
      ;; Initialize state if needed
      (get-component-state el) ; This will initialize if not exists
      (set! (.-innerHTML root)
            (str
              ;; Container with label
              "<div class=\"multiselect-container\">"
              ;; Label (optional)
              "  <label class=\"dropdown-label\" style=\"" (if label "display: flex; align-items: center;" "display: none;") "\">"
              (or label "")
              (when (and label required) (str " <span class=\"required-icon\">" common/required-icon "</span>"))
              "  </label>"
              ;; Multiselect wrapper
              "  <div class=\"dropdown-wrapper\">"
              "    <div class=\"dropdown-stub multiselect-stub\"" (when disabled " disabled") ">"
              "      <div class=\"multiselect-chips\">"
              "        <slot name=\"selected\"></slot>"
              "      </div>"
              "      <span class=\"dropdown-placeholder\">" placeholder "</span>"
              "    </div>"
              "    <div class=\"dropdown-chevron\">"
              "      <svg viewBox=\"0 0 20 20\" fill=\"currentColor\">"
              "        <path fill-rule=\"evenodd\" d=\"M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z\" clip-rule=\"evenodd\" />"
              "      </svg>"
              "    </div>"
              "    <dialog class=\"dropdown-dialog\">"
              "      <div class=\"dropdown-header\">"
              "        <input class=\"dropdown-search-input\" type=\"text\""
              "               placeholder=\"Search options...\""
              (when disabled " disabled") " />"
              "        <div class=\"dropdown-search-chevron\">"
              "          <svg viewBox=\"0 0 20 20\" fill=\"currentColor\">"
              "            <path fill-rule=\"evenodd\" d=\"M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z\" clip-rule=\"evenodd\" />"
              "          </svg>"
              "        </div>"
              "      </div>"
              "      <div class=\"dropdown-options\">"
              "        <slot id=\"options-slot\"></slot>"
              "      </div>"
              "    </dialog>"
              "  </div>"
              "</div>"))

      ;; Setup event listeners
      (let [stub (.querySelector root ".dropdown-stub")
            search-input (.querySelector root ".dropdown-search-input")
            options-slot (.querySelector root "#options-slot")]

        ;; Stub click handler
        (when stub
          (.addEventListener stub "click" (partial handle-stub-click! el root)))

        ;; Search input handlers
        (when search-input
          (.addEventListener search-input "input" (partial desktop/handle-input-change! el root))
          (.addEventListener search-input "keydown" (partial desktop/handle-keyboard! el root))
          (.addEventListener search-input "blur" (partial desktop/handle-search-blur! el root)))

        ;; Option clicks handler - listen for clicks anywhere in the dropdown
        (when options-slot
          (.addEventListener options-slot "click" (partial handle-tag-click! el root)))

        ;; Outside click handler
        (let [outside-click-handler (partial desktop/handle-outside-click! el root)]
          (.addEventListener js/document "click" outside-click-handler)
          (set! (.-tyOutsideClickHandler el) outside-click-handler))

        ;; Store cleanup function
        (set! (.-tyDropdownCleanup el)
              (fn []
                (when stub
                  (.removeEventListener stub "click" (partial handle-stub-click! el root)))
                (when search-input
                  (.removeEventListener search-input "input" (partial desktop/handle-input-change! el root))
                  (.removeEventListener search-input "keydown" (partial desktop/handle-keyboard! el root))
                  (.removeEventListener search-input "blur" (partial desktop/handle-search-blur! el root)))
                (when options-slot
                  (.removeEventListener options-slot "click" (partial handle-tag-click! el root)))
                ;; Clean up outside click handler
                (when-let [handler (.-tyOutsideClickHandler el)]
                  (.removeEventListener js/document "click" handler)
                  (set! (.-tyOutsideClickHandler el) nil)))))

      ;; Initialize selections (supports both selected attrs and value attr/prop)
      (initialize-selections! el)

      ;; Initialize form value for HTMX compatibility
      (update-form-value! el)

      ;; Setup tag dismiss listeners
      (setup-tag-listeners! el root)

      ;; Update placeholder and stub class
      (update-placeholder-and-stub! el))))

;; =====================================================
;; CLEANUP
;; =====================================================

(defn cleanup!
  "Cleanup function for multiselect"
  [^js el]
  ;; Clean up tag listeners
  (when-let [listeners (.-tyTagListeners el)]
    (doseq [[tag handler] listeners]
      (.removeEventListener tag "dismiss" handler))
    (set! (.-tyTagListeners el) nil))

  ;; Clean up dropdown listeners
  (when-let [cleanup-fn (.-tyDropdownCleanup el)]
    (cleanup-fn)
    (set! (.-tyDropdownCleanup el) nil))

  ;; Clear multiselect state
  (set! (.-tyMultiselectState el) nil))

;; =====================================================
;; WEB COMPONENT REGISTRATION
;; =====================================================

(def configuration
  {:observed [:value :placeholder :disabled :readonly :flavor :label :required :name]
   :form-associated true ; Enable form participation for HTMX compatibility
   :props {:value nil} ; Enable property watching for framework integration
   :connected render!
   :disconnected cleanup!
   :attr (fn [^js el delta]
           (let [delta' (enrich-delta delta el)]
             (update-component-state! el delta')
             ;; Always re-render for non-value changes, or when value parsing changed state
             (when (or
                     (seq (dissoc delta "value")) ; Non-value changes always render
                     (not= delta delta') ; Value parsing changed something
                     (contains? delta' :selected-values)) ; Selected values changed
               ;; Sync tag states with new selected values
               (when (contains? delta' :selected-values)
                 (let [root (wcs/ensure-shadow el)]
                   (setup-tag-listeners! el root)))
               (render! el))))
   ;; Same logic for property changes  
   :prop (fn [^js el delta]
           (let [delta' (enrich-delta delta el)]
             (update-component-state! el delta')
             (when (or
                     (seq (dissoc delta "value"))
                     (not= delta delta')
                     (contains? delta' :selected-values))
               (when (contains? delta' :selected-values)
                 (let [root (wcs/ensure-shadow el)]
                   (setup-tag-listeners! el root)))
               (render! el))))})

