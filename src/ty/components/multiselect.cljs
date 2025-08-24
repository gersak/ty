(ns ty.components.multiselect
  "Basic multiselect component extending dropdown functionality with ty-tag integration"
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
                           :filtered-options []
                           :selected-values []}] ; Array of selected values
        (set! (.-tyMultiselectState el) initial-state)
        initial-state)))

(defn set-multiselect-state!
  "Update multiselect component state"
  [^js el updates]
  (let [new-state (merge (get-multiselect-state el) updates)]
    (set! (.-tyMultiselectState el) new-state)
    new-state))

;; =====================================================
;; VALUE PARSING AND MANAGEMENT
;; =====================================================

(defn parse-initial-values
  "Parse initial values from value attribute (comma-separated)"
  [^js el]
  (let [value-attr (wcs/attr el "value")]
    (if (and value-attr (not-empty value-attr))
      (vec (.split value-attr ","))
      [])))

(defn is-selected?
  "Check if value is in selected values array"
  [value selected-values]
  (some #(= % value) selected-values))

(defn toggle-selection
  "Add or remove value from selected values array"
  [value selected-values]
  (if (is-selected? value selected-values)
    (vec (remove #(= % value) selected-values))
    (conj selected-values value)))

;; =====================================================
;; EVENT DISPATCHING
;; =====================================================

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
;; SELECTION DISPLAY MANAGEMENT
;; =====================================================

;; =====================================================
;; CLEAR ALL FUNCTIONALITY
;; =====================================================

(declare update-selection-tags! update-selection-display!)

(defn add-clear-all-button!
  "Add a clear all button when there are selected values"
  [^js el ^js shadow-root]
  (let [{:keys [selected-values]} (get-multiselect-state el)
        tags-container (.querySelector shadow-root ".multiselect-chips")
        {:keys [disabled]} (common/dropdown-attributes el)]

    (when (and tags-container (seq selected-values) (not disabled))
      ;; Check if clear button already exists
      (when-not (.querySelector tags-container ".clear-all-btn")
        (let [clear-btn (.createElement js/document "button")]
          (.add (.-classList clear-btn) "clear-all-btn")
          (set! (.-innerHTML clear-btn) "Clear all")
          (set! (.-title clear-btn) "Clear all selected items")

          ;; Add click handler
          (.addEventListener clear-btn "click"
                             (fn [e]
                               (.preventDefault e)
                               (.stopPropagation e)
                               ;; Clear all selections
                               (set-multiselect-state! el {:selected-values []})

                               ;; Update the value attribute to reflect cleared state
                               (.setAttribute el "value" "")

                               ;; Clear selected state from all options
                               (let [options (map common/get-option-data (common/get-options shadow-root))]
                                 (doseq [{:keys [element]} options]
                                   (.removeAttribute element "selected")))

                               ;; Update display
                               (update-selection-display! el shadow-root)

                               ;; Dispatch change event
                               (dispatch-multiselect-change! el [] "clear" nil)))

          ;; Add to container
          (.appendChild tags-container clear-btn))))))

(defn update-selection-display!
  "Update which ty-tags are shown in the selected area vs available for selection"
  [^js el ^js shadow-root]
  (let [{:keys [selected-values]} (get-multiselect-state el)
        selected-slot (.querySelector shadow-root "slot[name='selected']")
        options-slot (.querySelector shadow-root "slot:not([name])")
        placeholder (.querySelector shadow-root ".dropdown-placeholder")
        all-tags (when options-slot
                   (->> (.assignedElements options-slot)
                        array-seq
                        (filter #(= (.toLowerCase (.-tagName %)) "ty-tag"))))]

    ;; Show/hide placeholder
    (if (seq selected-values)
      (.add (.-classList placeholder) "hidden")
      (.remove (.-classList placeholder) "hidden"))

    ;; Update tag slots and visibility
    (doseq [tag all-tags]
      (let [tag-value (.getAttribute tag "value")]
        (if (some #(= % tag-value) selected-values)
          (do
            ;; Move to selected slot
            (.setAttribute tag "slot" "selected")
            (.removeAttribute tag "hidden"))
          (do
            ;; Move back to default slot (options)
            (.removeAttribute tag "slot")
            (.removeAttribute tag "hidden")))))))

;; =====================================================
;; OPTION INTERACTION
;; =====================================================

(defn handle-multiselect-option-click!
  "Handle option click for multiselect (toggle selection, don't close)"
  [^js el ^js shadow-root ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (let [option-element (.-target event)
        option-data (common/get-option-data option-element)
        value (:value option-data)
        {:keys [selected-values]} (get-multiselect-state el)
        new-values (toggle-selection value selected-values)
        action (if (is-selected? value selected-values) "remove" "add")]

    ;; Update state
    (set-multiselect-state! el {:selected-values new-values})

    ;; Update visual selection display
    (update-selection-display! el shadow-root)

    ;; Update option highlighting
    (let [options (map common/get-option-data (common/get-options shadow-root))]
      (doseq [{:keys [element value]} options]
        (if (is-selected? value new-values)
          (.setAttribute element "selected" "")
          (.removeAttribute element "selected"))))

    ;; Dispatch change event
    (dispatch-multiselect-change! el new-values action value)

    ;; DON'T close dropdown - keep it open for multiple selections
    ))

;; =====================================================
;; STUB CLICK HANDLING
;; =====================================================

(defn handle-multiselect-stub-click!
  "Handle stub click - prevent clicks on tags from opening dropdown"
  [^js el ^js shadow-root ^js event]
  (let [target (.-target event)]
    ;; If clicking on a ty-tag element, don't open dropdown
    (when-not (or (= (.toLowerCase (.-tagName target)) "ty-tag")
                  (.closest target "ty-tag"))
      ;; Use desktop dropdown's stub click handler
      (desktop/handle-stub-click! el shadow-root event))))

;; =====================================================
;; MULTISELECT RENDERING
;; =====================================================

(defn render!
  "Main render function for multiselect"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        is-mobile? (common/is-mobile-device?)
        {:keys [placeholder disabled label required]} (common/dropdown-attributes el)]

    ;; Ensure styles are loaded
    (common/ensure-dropdown-styles! root)
    (ensure-styles! root multiselect-styles "ty-multiselect")

    ;; Initialize selected values from attribute
    (when-not (.-tyMultiselectState el)
      (let [initial-values (parse-initial-values el)]
        (set-multiselect-state! el {:selected-values initial-values})))

    ;; Render multiselect-specific HTML structure
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
              "      </div>" ; Container for selected chips
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

      ;; Setup event listeners with multiselect-specific handlers
      (let [stub (.querySelector root ".dropdown-stub")
            search-input (.querySelector root ".dropdown-search-input")
            slot (.querySelector root "#options-slot")
            dialog (.querySelector root ".dropdown-dialog")]

        ;; Override stub click to handle chips
        (when stub
          (.addEventListener stub "click" (partial handle-multiselect-stub-click! el root)))

        ;; Use desktop search input handler (already handles search)
        (when search-input
          (.addEventListener search-input "input" (partial desktop/handle-input-change! el root)))

        ;; Override option clicks for multiselect behavior  
        (when slot
          (.addEventListener slot "click" (partial handle-multiselect-option-click! el root)))

        ;; Use desktop dialog close handler
        (when dialog
          (.addEventListener dialog "close" (partial desktop/handle-dialog-close! el root)))

        ;; Add outside click handler for proper dialog closing
        (let [outside-click-handler (partial desktop/handle-outside-click! el root)]
          (.addEventListener js/document "click" outside-click-handler)
          (set! (.-tyOutsideClickHandler el) outside-click-handler))

        ;; Store cleanup function
        (set! (.-tyDropdownCleanup el)
              (fn []
                (when stub
                  (.removeEventListener stub "click" (partial handle-multiselect-stub-click! el root)))
                (when search-input
                  (.removeEventListener search-input "input" (partial desktop/handle-input-change! el root)))
                (when slot
                  (.removeEventListener slot "click" (partial handle-multiselect-option-click! el root)))
                (when dialog
                  (.removeEventListener dialog "close" (partial desktop/handle-dialog-close! el root)))
                ;; Clean up outside click handler
                (when-let [handler (.-tyOutsideClickHandler el)]
                  (.removeEventListener js/document "click" handler)
                  (set! (.-tyOutsideClickHandler el) nil))))))

    ;; Update chips display
    ;; Setup ty-tag dismiss event listeners
    (let [all-tags (->> (.querySelectorAll el "ty-tag")
                        array-seq)]
      (doseq [tag all-tags]
        (.addEventListener tag "ty-tag-dismiss"
                           (fn [e]
                             (.preventDefault e)
                             (.stopPropagation e)
                             (let [tag-value (.getAttribute tag "value")
                                   {:keys [selected-values]} (get-multiselect-state el)
                                   new-values (vec (remove #(= % tag-value) selected-values))]
                               (set-multiselect-state! el {:selected-values new-values})
                               (.setAttribute el "value" (.join (clj->js new-values) ","))
                               (update-selection-display! el root)
                               (dispatch-multiselect-change! el new-values "remove" tag-value))))))

    ;; Update selection display
    (update-selection-display! el root)

    ;; Update option selected states
    (let [{:keys [selected-values]} (get-multiselect-state el)
          options (map common/get-option-data (common/get-options root))]
      (doseq [{:keys [element value]} options]
        (if (is-selected? value selected-values)
          (.setAttribute element "selected" "")
          (.removeAttribute element "selected"))))))

;; =====================================================
;; CLEANUP FUNCTION
;; =====================================================

(defn cleanup!
  "Cleanup function for multiselect"
  [^js el]
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
   :attr (fn [^js el _ _ _]
           (render! el))})
