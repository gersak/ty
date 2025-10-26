(ns ty.components.dropdown.mobile
  "Mobile implementation of dropdown using ty-modal for full-screen experience"
  (:require [ty.components.dropdown.common :as common]
            [ty.components.dropdown.global :as global]
            [ty.shim :as wcs]))

;; =====================================================
;; MOBILE EVENT HANDLERS
;; =====================================================

(defn handle-modal-close!
  "Handle mobile modal close events"
  [^js el ^js _]
  ;; Clear from global state 
  (global/clear-current-dropdown! el)

  ;; Update component state to closed AND clear search
  (common/set-component-state! el {:open false
                                   :search ""
                                   :highlighted-index -1
                                   :filtered-options []})

  ;; Update visual state
  (when-let [root (.-shadowRoot el)]
    ;; Clear selection - this will remove clones and clear selected attributes
    ;; DON'T clear selection when modal closes - keep the selected option!
    ;; (common/clear-selection! root)

    ;; Update selection display (show placeholder again)
    ;; Selection display will be managed by the selection itself

    ;; Clear the search input visually
    (when-let [search-input (.querySelector root ".mobile-search-input")]
      (set! (.-value search-input) ""))

    ;; Reset chevron
    (when-let [chevron (.querySelector root ".dropdown-chevron")]
      (.remove (.-classList chevron) "open"))

    ;; Close the modal
    (when-let [modal (.querySelector root ".mobile-dropdown-modal")]
      (.removeAttribute modal "open"))

    ;; Reset option visibility (show all options)
    (let [options (map common/get-option-data (common/get-options root))]
      (when (seq options)
        (common/update-option-visibility! options options)))))

(defn handle-option-click!
  "Handle option clicks in mobile mode"
  [^js el ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (let [option-element (.-target event)]

    ;; Select the option (this will clone it to selected slot)
    (when-let [root (.-shadowRoot el)]
      (common/select-option! root option-element)

      ;; Update selection display (hide placeholder)
      (common/update-selection-display! el))

    ;; Dispatch change event with option element
    (common/dispatch-change-event! el option-element)

    ;; Close modal
    (handle-modal-close! el event)))

(defn handle-search!
  "Handle search input in mobile mode"
  [^js el ^js event]
  (let [{:keys [searchable]} (common/dropdown-attributes el)
        search (.-value (.-target event))]
    (common/set-component-state! el {:search search})

    (if searchable
      ;; Internal search: filter options locally (default behavior when searchable is true)
      (when-let [root (.-shadowRoot el)]
        (let [options (map common/get-option-data (common/get-options root))
              filtered (common/filter-options options search)]
          (common/set-component-state! el {:filtered-options filtered})
          (common/update-option-visibility! filtered options)))

      ;; External search: dispatch event for external handling (when searchable is false)
      (common/dispatch-search-event! el search))))

(defn handle-stub-click!
  "Handle click on stub to open mobile modal and highlight selected value"
  [^js el ^js shadow-root ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (let [{:keys [disabled searchable]} (common/dropdown-attributes el)]
    (when-not disabled
      ;; Close any currently open dropdown before opening this one
      (global/set-current-dropdown! el)

      ;; Update state and clear any previous search
      (common/set-component-state! el {:open true
                                       :search ""
                                       :filtered-options []})

      ;; Clear the search input visually
      (when-let [search-input (.querySelector shadow-root ".mobile-search-input")]
        (set! (.-value search-input) ""))

      ;; Show all options initially and find selected option
      (let [options (map common/get-option-data (common/get-options shadow-root))
            {:keys [current-value]} (common/get-dropdown-state el)
            ;; Find the index of the currently selected option
            selected-index (when current-value
                             (first (keep-indexed
                                     (fn [idx option]
                                       (when (= (:value option) current-value) idx))
                                     options)))
            initial-highlighted-index (or selected-index -1)]

        (when (seq options)
          (common/update-option-visibility! options options)
          ;; Set the highlighted index for mobile consistency
          (common/set-component-state! el {:highlighted-index initial-highlighted-index})
          ;; Highlight and scroll to selected option if it exists
          (when (and selected-index (>= selected-index 0))
            (common/highlight-option! options selected-index))))

      ;; Open modal
      (when-let [modal (.querySelector shadow-root ".mobile-dropdown-modal")]
        (.setAttribute modal "open" "true"))

      ;; Update chevron
      (when-let [chevron (.querySelector shadow-root ".dropdown-chevron")]
        (.add (.-classList chevron) "open"))

      ;; Auto-focus the search input after modal opens (slight delay for modal animation)
      (when searchable
        (js/setTimeout
         (fn []
           (when-let [search-input (.querySelector shadow-root ".mobile-search-input")]
             (.focus search-input)))
         150)))))

;; =====================================================
;; MOBILE EVENT SETUP
;; =====================================================

(defn setup-event-listeners!
  "Setup event listeners for mobile mode"
  [^js el]
  (let [^js root (wcs/ensure-shadow el)
        stub (.querySelector root ".dropdown-stub")
        modal (.querySelector root ".mobile-dropdown-modal")
        search-input (.querySelector root ".mobile-search-input")
        options-container (.querySelector root ".mobile-options-list")]

    ;; Stub click - opens modal
    (when stub
      (.addEventListener stub "click" (partial handle-stub-click! el root)))

    ;; Modal close events
    (when modal
      (.addEventListener modal "close" (partial handle-modal-close! el)))

    ;; Search input
    (when search-input
      (.addEventListener search-input "input" (partial handle-search! el)))

    ;; Option clicks via event delegation on options container
    (when options-container
      (.addEventListener options-container "click" (partial handle-option-click! el)))

    ;; Store cleanup function
    (set! (.-tyDropdownCleanup el)
          (fn []
            ;; Cleanup mobile event listeners
            (when stub
              (.removeEventListener stub "click" (partial handle-stub-click! el root)))
            (when modal
              (.removeEventListener modal "close" (partial handle-modal-close! el)))
            (when search-input
              (.removeEventListener search-input "input" (partial handle-search! el)))
            (when options-container
              (.removeEventListener options-container "click" (partial handle-option-click! el)))))))

;; =====================================================
;; MOBILE RENDERING
;; =====================================================

(defn render!
  "Mobile implementation using ty-modal for full-screen experience (self-contained)"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        {:keys [placeholder searchable disabled label required size]} (common/dropdown-attributes el)]

    ;; Create wrapper + mobile modal structure (matching desktop structure)
    (when-not (.querySelector root ".dropdown-container")
      (set! (.-innerHTML root)
            (str
             ;; Container with label
             "<div class=\"dropdown-container\">"
             ;; Label (optional)
             "  <label class=\"dropdown-label\" style=\"" (if label "display: flex; align-items: center;" "display: none;") "\">"
             (or label "")
             (when (and label required) (str " <span class=\"required-icon\">" common/required-icon "</span>"))
             "  </label>"
             ;; Dropdown wrapper - provides positioning context, no styling
             "  <div class=\"dropdown-wrapper\">"
             ;; Dropdown stub - shows selected option or placeholder (same as desktop)
             "    <div class=\"dropdown-stub " size "\" "
             (when disabled "disabled ")
             ">"
             "      <slot name=\"selected\"></slot>"
             "      <span class=\"dropdown-placeholder\">" placeholder "</span>"
             "    </div>"
             ;; Chevron - positioned over the stub
             "    <div class=\"dropdown-chevron\">"
             "      <svg viewBox=\"0 0 20 20\" fill=\"currentColor\">"
             "        <path fill-rule=\"evenodd\" d=\"M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z\" clip-rule=\"evenodd\" />"
             "      </svg>"
             "    </div>"
             ;; ty-modal for full-screen mobile experience
             "    <ty-modal class=\"mobile-dropdown-modal\""
             "    backdrop=\"true\" close-on-outside-click=\"true\" close-on-escape=\"true\">"
             "      <div class=\"mobile-dropdown-content\">"
             "        <div class=\"mobile-search-header\">"
             "          <input class=\"mobile-search-input " size "\" type=\"text\" "
             "                 placeholder=\"" (if searchable "Search..." placeholder) "\" "
             (when disabled "disabled ")
             "          />"
             "        </div>"
             "        <div class=\"mobile-options-list\">"
             "          <slot></slot>"
             "        </div>"
             "      </div>"
             "    </ty-modal>"
             "  </div>"
             "</div>"))

      ;; Setup mobile-specific event listeners
      (setup-event-listeners! el))

    ;; For non-searchable dropdowns, ensure all options are visible
    (when-not searchable
      (let [options (map common/get-option-data (common/get-options root))]
        (when (seq options)
          (common/update-option-visibility! options options))))))
