(ns ty.components.multiselect
  "Multiselect component with ty-tag integration for multiple selections"
  (:require [ty.components.dropdown.common :as common]
            [ty.components.dropdown.desktop :as desktop]
            [ty.components.dropdown.mobile :as mobile]
            [ty.components.tag] ; Import ty-tag component
            [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load multiselect styles
(defstyles multiselect-styles "ty/components/multiselect.css")

;; =====================================================
;; MULTISELECT STATE MANAGEMENT
;; =====================================================

(defn get-multiselect-state
  "Get or initialize multiselect component state"
  [^js el]
  (or (.-tyMultiselectState el)
      (let [initial-state {:open false
                           :search ""
                           :highlighted-index -1
                           :filtered-options []}]
        (set! (.-tyMultiselectState el) initial-state)
        initial-state)))

(defn set-multiselect-state!
  "Update multiselect component state"
  [^js el updates]
  (let [new-state (merge (get-multiselect-state el) updates)]
    (set! (.-tyMultiselectState el) new-state)
    new-state))

;; =====================================================
;; TAG MANAGEMENT
;; =====================================================

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
       (map #(.getAttribute % "value"))
       vec))

(defn find-tag-by-value
  "Find a tag element by its value attribute"
  [^js el value]
  (->> (get-all-tags el)
       (filter #(= (.getAttribute % "value") value))
       first))

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
  "Update the component's value attribute based on selected tags"
  [^js el]
  (let [selected-values (get-selected-values el)
        value-str (if (seq selected-values)
                    (.join (clj->js selected-values) ",")
                    "")]
    (.setAttribute el "value" value-str)
    value-str))

(defn update-placeholder-and-stub!
  "Show/hide placeholder and update stub classes based on selection"
  [^js el ^js shadow-root]
  (let [selected-values (get-selected-values el)
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
  "Initialize selections from either pre-selected tags or value attribute
   Priority: 1. Tags with selected attribute, 2. Component value attribute"
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

      ;; Otherwise use value attribute to set selections
      (let [value-attr (wcs/attr el "value")
            initial-values (if (and value-attr (not-empty value-attr))
                             (set (.split value-attr ","))
                             #{})]
        (doseq [tag tags]
          (let [tag-value (.getAttribute tag "value")
                should-be-selected (contains? initial-values tag-value)]
            (update-tag-state! tag should-be-selected)))))))

(defn dispatch-multiselect-change!
  "Dispatch multiselect change event"
  [^js el values action item]
  (let [detail #js {:values (clj->js values)
                    :action action ; "add" | "remove" | "clear"
                    :item item} ; The item that was added/removed
        event (js/CustomEvent. "change"
                               #js {:detail detail
                                    :bubbles true
                                    :cancelable true})]
    (.dispatchEvent el event)))

;; =====================================================
;; EVENT HANDLERS
;; =====================================================

(defn handle-tag-click!
  "Handle click on a tag in the options dropdown"
  [^js el ^js shadow-root ^js event]
  (let [target (.-target event)]
    ;; Check if we clicked on a ty-tag or something inside it
    (when-let [tag (if (= (.toLowerCase (.-tagName target)) "ty-tag")
                     target
                     (.closest target "ty-tag"))]
      ;; Handle all tags except disabled ones
      (when-not (.hasAttribute tag "disabled")
        (.preventDefault event)
        (.stopPropagation event)

        (let [tag-value (.getAttribute tag "value")
              currently-selected (.hasAttribute tag "selected")
              new-selected (not currently-selected)]

          ;; Toggle the tag's selected state
          (update-tag-state! tag new-selected)

          ;; Update component value
          (update-component-value! el)

          ;; Update placeholder and stub class
          (update-placeholder-and-stub! el shadow-root)

          ;; Dispatch change event
          (let [selected-values (get-selected-values el)
                action (if new-selected "add" "remove")]
            (dispatch-multiselect-change! el selected-values action tag-value))


          (when (all-options-selected? el)
            (desktop/close-dropdown! el shadow-root))

          ;; Keep dropdown open for multiple selections
          nil)))))

(defn handle-tag-dismiss!
  "Handle dismiss event from a selected tag"
  [^js el ^js shadow-root ^js event]
  (.preventDefault event)
  (.stopPropagation event)

  (let [tag (.-target (.-detail event))
        tag-value (.getAttribute tag "value")]

    ;; Update tag state
    (update-tag-state! tag false)

    ;; Update component value
    (update-component-value! el)

    ;; Update placeholder and stub class
    (update-placeholder-and-stub! el shadow-root)

    ;; Dispatch change event
    (let [selected-values (get-selected-values el)]
      (dispatch-multiselect-change! el selected-values "remove" tag-value))))

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

(defn handle-clear-all!
  "Handle clear all button click"
  [^js el ^js shadow-root ^js event]
  (.preventDefault event)
  (.stopPropagation event)

  ;; Clear all selections
  (doseq [tag (get-all-tags el)]
    (update-tag-state! tag false))

  ;; Update component value
  (update-component-value! el)

  ;; Update placeholder and stub class
  (update-placeholder-and-stub! el shadow-root)

  ;; Dispatch change event
  (dispatch-multiselect-change! el [] "clear" nil))

;; =====================================================
;; SETUP AND CLEANUP
;; =====================================================

(defn setup-tag-listeners!
  "Setup event listeners for all ty-tag elements"
  [^js el ^js shadow-root]
  ;; Remove any existing listeners first
  (when-let [listeners (.-tyTagListeners el)]
    (doseq [[tag handler] listeners]
      (.removeEventListener tag "ty-tag-dismiss" handler))
    (set! (.-tyTagListeners el) nil))

  ;; Add new listeners
  (let [tags (get-all-tags el)
        listeners (atom [])]
    (doseq [tag tags]
      (let [handler (partial handle-tag-dismiss! el shadow-root)]
        (.addEventListener tag "ty-tag-dismiss" handler)
        (swap! listeners conj [tag handler])))
    (set! (.-tyTagListeners el) @listeners)))

;; =====================================================
;; RENDERING
;; =====================================================

(defn render!
  "Main render function for multiselect"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        is-mobile? (common/is-mobile-device?)
        {:keys [placeholder disabled label required]} (common/dropdown-attributes el)
        clearable? (wcs/parse-bool-attr el "clearable")]

    ;; Ensure styles are loaded
    (common/ensure-dropdown-styles! root)
    (ensure-styles! root multiselect-styles "ty-multiselect")

    ;; Initialize state if needed
    (when-not (.-tyMultiselectState el)
      (set-multiselect-state! el {:open false
                                  :search ""
                                  :highlighted-index -1
                                  :filtered-options []}))

    ;; Create structure if it doesn't exist
    (when-not (.querySelector root ".multiselect-container")
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
            options-slot (.querySelector root "#options-slot")
            dialog (.querySelector root ".dropdown-dialog")]

        ;; Stub click handler
        (when stub
          (.addEventListener stub "click" (partial handle-stub-click! el root)))

        ;; Search input handler
        (when search-input
          (.addEventListener search-input "input" (partial desktop/handle-input-change! el root)))

        ;; Option clicks handler - listen for clicks anywhere in the dropdown
        (when options-slot
          (.addEventListener options-slot "click" (partial handle-tag-click! el root)))

        ;; Dialog close handler
        (when dialog
          (.addEventListener dialog "close" (partial desktop/handle-dialog-close! el root)))

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
                  (.removeEventListener search-input "input" (partial desktop/handle-input-change! el root)))
                (when options-slot
                  (.removeEventListener options-slot "click" (partial handle-tag-click! el root)))
                (when dialog
                  (.removeEventListener dialog "close" (partial desktop/handle-dialog-close! el root)))
                ;; Clean up outside click handler
                (when-let [handler (.-tyOutsideClickHandler el)]
                  (.removeEventListener js/document "click" handler)
                  (set! (.-tyOutsideClickHandler el) nil))))))

    ;; Initialize selections (supports both selected attrs and value attr)
    (initialize-selections! el)

    ;; Setup tag dismiss listeners
    (setup-tag-listeners! el root)

    ;; Update placeholder and stub class
    (update-placeholder-and-stub! el root)

    ;; Handle clearable button
    (when clearable?
      (let [chips-container (.querySelector root ".multiselect-chips")
            existing-clear-btn (.querySelector root ".clear-all-btn")
            has-selections? (seq (get-selected-values el))]

        ;; Add or remove clear button based on selections
        (if has-selections?
          (when-not existing-clear-btn
            (let [clear-btn (.createElement js/document "button")]
              (set! (.-className clear-btn) "clear-all-btn")
              (set! (.-type clear-btn) "button")
              (set! (.-innerHTML clear-btn) "Clear all")
              (.setAttribute clear-btn "aria-label" "Clear all selections")
              (.addEventListener clear-btn "click" (partial handle-clear-all! el root))
              (.appendChild chips-container clear-btn)))
          (when existing-clear-btn
            (.remove existing-clear-btn)))))))

;; =====================================================
;; CLEANUP
;; =====================================================

(defn cleanup!
  "Cleanup function for multiselect"
  [^js el]
  ;; Clean up tag listeners
  (when-let [listeners (.-tyTagListeners el)]
    (doseq [[tag handler] listeners]
      (.removeEventListener tag "ty-tag-dismiss" handler))
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

(wcs/define! "ty-multiselect"
  {:observed [:value :placeholder :disabled :readonly :flavor :label :required :clearable]
   :connected render!
   :disconnected cleanup!
   :attr (fn [^js el attr-name old-value new-value]
           ;; Re-render on attribute changes
           (when (= attr-name "value")
             ;; When value changes externally, update tags
             (initialize-selections! el)
             (let [root (wcs/ensure-shadow el)]
               (update-placeholder-and-stub! el root)
               (setup-tag-listeners! el root)))
           (render! el))})
