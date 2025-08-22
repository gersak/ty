(ns ty.components.dropdown.mobile
  "Mobile implementation of dropdown using ty-modal for full-screen experience"
  (:require [ty.components.dropdown.common :as common]))

;; =====================================================
;; MOBILE EVENT HANDLERS
;; =====================================================

(defn handle-modal-close!
  "Handle mobile modal close events"
  [^js el ^js event]
  ;; Update component state to closed
  (common/set-component-state! el {:open false
                                   :highlighted-index -1})

  ;; Update visual state
  (when-let [root (.-shadowRoot el)]
    (when-let [chevron (.querySelector root ".dropdown-chevron")]
      (.remove (.-classList chevron) "open"))

    ;; Close the modal
    (when-let [modal (.querySelector root ".mobile-dropdown-modal")]
      (.removeAttribute modal "open"))))

(defn handle-option-click!
  "Handle option clicks in mobile mode"
  [^js el ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (let [option-element (.-target event)
        value (or (.-value option-element) (.-textContent option-element))
        text (.-textContent option-element)]
    ;; Update value attribute
    (.setAttribute el "value" value)
    ;; Update selection
    (when-let [root (.-shadowRoot el)]
      (let [options (map common/get-option-data (common/get-options root))]
        (common/select-option! options value))
      ;; Update stub display
      (common/update-stub-display! el root))
    ;; Dispatch change event
    (common/dispatch-change-event! el value text)
    ;; Close modal
    (handle-modal-close! el event)))

(defn handle-search!
  "Handle search input in mobile mode"
  [^js el ^js event]
  (let [{:keys [searchable]} (common/dropdown-attributes el)]
    (when searchable
      (let [search (.-value (.-target event))]
        (when-let [root (.-shadowRoot el)]
          (let [options (map common/get-option-data (common/get-options root))
                filtered (common/filter-options options search)]
            (common/set-component-state! el {:search search
                                             :filtered-options filtered})
            (common/update-option-visibility! filtered options)))))))

(defn handle-stub-click!
  "Handle click on stub to open mobile modal"
  [^js el ^js shadow-root ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (let [{:keys [disabled]} (common/dropdown-attributes el)]
    (when-not disabled
      ;; Update state
      (common/set-component-state! el {:open true})
      ;; Open modal
      (when-let [modal (.querySelector shadow-root ".mobile-dropdown-modal")]
        (.setAttribute modal "open" "true"))
      ;; Update chevron
      (when-let [chevron (.querySelector shadow-root ".dropdown-chevron")]
        (.add (.-classList chevron) "open")))))

;; =====================================================
;; MOBILE EVENT SETUP
;; =====================================================

(defn setup-event-listeners!
  "Setup event listeners for mobile mode"
  [^js el ^js root]
  (let [stub-input (.querySelector root ".dropdown-input.dropdown-stub")
        modal (.querySelector root ".mobile-dropdown-modal")
        search-input (.querySelector root ".mobile-search-input")
        slot (.querySelector root "slot")]

    ;; Stub input click - opens modal
    (when stub-input
      (.addEventListener stub-input "click" (partial handle-stub-click! el root)))

    ;; Modal close events
    (when modal
      (.addEventListener modal "ty-modal-close" (partial handle-modal-close! el)))

    ;; Search input
    (when search-input
      (.addEventListener search-input "input" (partial handle-search! el)))

    ;; Option clicks
    (when slot
      (.addEventListener slot "click" (partial handle-option-click! el)))

    ;; Store cleanup function
    (set! (.-tyDropdownCleanup el)
          (fn []
            ;; Cleanup mobile event listeners
            (when stub-input
              (.removeEventListener stub-input "click" (partial handle-stub-click! el root)))
            (when modal
              (.removeEventListener modal "ty-modal-close" (partial handle-modal-close! el)))
            (when search-input
              (.removeEventListener search-input "input" (partial handle-search! el)))
            (when slot
              (.removeEventListener slot "click" (partial handle-option-click! el)))))))

;; =====================================================
;; MOBILE RENDERING
;; =====================================================

(defn render!
  "Mobile implementation using ty-modal for full-screen experience"
  [^js el ^js root]
  (let [{:keys [placeholder searchable disabled]} (common/dropdown-attributes el)]

    ;; Create wrapper + mobile modal structure (matching desktop structure)
    (when-not (.querySelector root ".dropdown-wrapper")
      (set! (.-innerHTML root)
            (str
             ;; Wrapper - provides positioning context, no styling
              "<div class=\"dropdown-wrapper\">"

             ;; Read-only input - handles all visual styling (same as desktop)
              "  <input class=\"dropdown-input dropdown-stub\" "
              "         type=\"text\" "
              "         readonly "
              "         placeholder=\"" placeholder "\" "
              (when disabled "disabled ")
              "         value=\"" placeholder "\" />"

             ;; Chevron - positioned over the input
              "  <div class=\"dropdown-chevron\">"
              "    <svg viewBox=\"0 0 20 20\" fill=\"currentColor\">"
              "      <path fill-rule=\"evenodd\" d=\"M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z\" clip-rule=\"evenodd\" />"
              "    </svg>"
              "  </div>"

             ;; ty-modal for full-screen mobile experience
              "  <ty-modal class=\"mobile-dropdown-modal\""
              "  backdrop=\"true\" close-on-outside-click=\"true\" close-on-escape=\"true\">"
              "    <div class=\"mobile-dropdown-content\">"
              "      <div class=\"mobile-search-header\">"
              "        <input class=\"mobile-search-input\" type=\"text\" "
              "               placeholder=\"" (if searchable "Search..." placeholder) "\" "
              (when disabled "disabled ")
              "        />"
              "      </div>"
              "      <div class=\"mobile-options-list\">"
              "        <slot></slot>"
              "      </div>"
              "    </div>"
              "  </ty-modal>"
              "</div>")))

      ;; Setup mobile-specific event listeners
    (setup-event-listeners! el root)

    ;; Update stub display
    (common/update-stub-display! el root)

    ;; For non-searchable dropdowns, ensure all options are visible
    (when-not searchable
      (let [options (map common/get-option-data (common/get-options root))]
        (when (seq options)
          (common/update-option-visibility! options options))))))
