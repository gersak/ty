(ns ty.components.dropdown.desktop
  "Desktop implementation of dropdown using dialog element with smart positioning"
  (:require [ty.components.dropdown.common :as common]))

;; =====================================================
;; DESKTOP POSITIONING
;; =====================================================

(defn update-position!
  "Smart positioning using CSS classes - works with wrapper structure"
  [^js el ^js shadow-root]
  (let [wrapper (.querySelector shadow-root ".dropdown-wrapper")
        dialog (.querySelector shadow-root ".dropdown-dialog")
        {:keys [open]} (common/get-component-state el)]

    (when (and open wrapper dialog)
      (let [wrapper-rect (.getBoundingClientRect wrapper)
            wrapper-bottom (.-bottom wrapper-rect)
            viewport-height (.-innerHeight js/window)
            space-below (- viewport-height wrapper-bottom)
            position-below (> space-below 300)] ; Need ~300px for dropdown

        ;; Just toggle CSS classes - let CSS handle the positioning
        (.remove (.-classList dialog) "position-above" "position-below")
        (.add (.-classList dialog) (if position-below "position-below" "position-above"))))))

;; =====================================================
;; DESKTOP DROPDOWN CONTROL
;; =====================================================

(defn close-dropdown!
  "Programmatically close dropdown dialog"
  [^js el ^js shadow-root]
  (let [dialog (.querySelector shadow-root ".dropdown-dialog")]
    ;; Close dialog - this will trigger the dialog close event
    (.remove (.-classList dialog) "position-above" "position-below")
    (.close dialog)))

(declare handle-option-click!)

(defn open-dropdown!
  "Open dropdown dialog"
  [^js el ^js shadow-root]
  (let [{:keys [disabled readonly searchable value]} (common/dropdown-attributes el)]
    (when-not (or disabled readonly)
      ;; Initialize filtered options for keyboard navigation
      (let [all-options (map common/get-option-data (common/get-options shadow-root))
            current-search (if searchable (:search (common/get-component-state el)) "")
            filtered (if searchable
                       (common/filter-options all-options current-search)
                       all-options)
            ;; Find the index of the currently selected option
            selected-index (if (and value (seq filtered))
                             (first (keep-indexed
                                      (fn [idx option]
                                        (when (= (:value option) value) idx))
                                      filtered))
                             -1)]

        (common/set-component-state! el {:open true
                                         :filtered-options filtered
                                         :highlighted-index selected-index}))

      (let [dialog (.querySelector shadow-root ".dropdown-dialog")
            chevron (.querySelector shadow-root ".dropdown-chevron")
            input (.querySelector shadow-root ".dropdown-input")]

        ;; For non-searchable dropdowns, ensure all options are visible
        (when-not searchable
          (let [options (map common/get-option-data (common/get-options shadow-root))]
            (common/update-option-visibility! options options)))

        (when chevron
          (.add (.-classList chevron) "open"))

        ;; Open dialog - input inside dialog gets focus naturally!
        ;; Browser handles preventing multiple modals automatically
        (when dialog
          (.show dialog)

          ;; Set initial input value for searchable dropdowns
          (when (and input searchable)
            (set! (.-value input) ""))

          ;; Position dialog once
          (update-position! el shadow-root)

          ;; Highlight the selected option if any
          (js/setTimeout
            (fn []
              (let [{:keys [highlighted-index filtered-options]} (common/get-component-state el)]
                (when (>= highlighted-index 0)
                  (common/highlight-option! filtered-options highlighted-index))))
            50))))))

;; =====================================================
;; DESKTOP EVENT HANDLERS
;; =====================================================

(defn handle-backdrop-click!
  "Handle dialog backdrop clicks (native approach like modal)"
  [^js el ^js shadow-root ^js event]
  (let [dialog (.querySelector shadow-root ".dropdown-dialog")]
    (when (= (.-target event) dialog) ; Click on backdrop, not content
      (.preventDefault event)
      (.stopPropagation event)
      (close-dropdown! el shadow-root))))

(defn handle-dialog-close!
  "Handle dialog close event (now just for cleanup)"
  [^js el ^js shadow-root ^js event]
  ;; Update component state to reflect closed status
  (common/set-component-state! el {:open false
                                   :highlighted-index -1})

  ;; Update visual state  
  (when-let [chevron (.querySelector shadow-root ".dropdown-chevron")]
    (.remove (.-classList chevron) "open"))

  ;; Reset search if not searchable
  (let [{:keys [searchable]} (common/dropdown-attributes el)]
    (when-not searchable
      (common/set-component-state! el {:search ""}))))

(defn handle-stub-click!
  "Handle click on stub to open dropdown dialog"
  [^js el ^js shadow-root ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (let [{:keys [disabled]} (common/dropdown-attributes el)]
    (when-not disabled
      (open-dropdown! el shadow-root))))

(defn handle-close-click!
  "Handle click on close button to close dropdown"
  [^js el ^js shadow-root ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (close-dropdown! el shadow-root))

(defn handle-input-change!
  "Handle input value change for search"
  [^js el ^js shadow-root ^js event]
  (let [{:keys [searchable]} (common/dropdown-attributes el)]
    (when searchable
      (let [search (.-value (.-target event))
            options (map common/get-option-data (common/get-options shadow-root))
            filtered (common/filter-options options search)
            {:keys [value]} (common/dropdown-attributes el)
            new-highlighted-index (if (and value (seq filtered))
                                    (first (keep-indexed
                                             (fn [idx option]
                                               (when (= (:value option) value) idx))
                                             filtered))
                                    -1)]
        (common/set-component-state! el {:search search
                                         :filtered-options filtered
                                         :highlighted-index new-highlighted-index})
        (common/update-option-visibility! filtered options)
        (common/clear-highlights! options)
        (when (>= new-highlighted-index 0)
          (common/highlight-option! filtered new-highlighted-index))))))

(defn handle-option-click!
  "Handle click on option"
  [^js el ^js shadow-root ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (let [option-element (.-target event)
        value (or (.-value option-element) (.-textContent option-element))
        text (.-textContent option-element)]
    ;; Update value attribute
    (.setAttribute el "value" value)
    ;; Update selection
    (let [options (map common/get-option-data (common/get-options shadow-root))]
      (common/select-option! options value))
    ;; Update stub display
    (common/update-stub-display! el shadow-root)
    ;; Dispatch change event
    (common/dispatch-change-event! el value text)
    ;; Close dropdown
    (close-dropdown! el shadow-root)))

(defn handle-keyboard!
  "Handle keyboard navigation (arrow keys, enter, and escape)"
  [^js el ^js shadow-root ^js event]
  (let [{:keys [open highlighted-index filtered-options]} (common/get-component-state el)
        key-code (.-keyCode event)]
    (case key-code
      ;; ESCAPE - close dropdown
      27 (do
           (.preventDefault event)
           (.stopPropagation event)
           (close-dropdown! el shadow-root))
      ;; ENTER - select highlighted option
      13 (do
           (.preventDefault event)
           (when open
             (when (and (>= highlighted-index 0) (< highlighted-index (count filtered-options)))
               (let [{:keys [value text]} (nth filtered-options highlighted-index)]
                 (.setAttribute el "value" value)
                 (let [options (map common/get-option-data (common/get-options shadow-root))]
                   (common/select-option! options value))
                 (common/update-stub-display! el shadow-root)
                 (common/dispatch-change-event! el value text)
                 (close-dropdown! el shadow-root)))))
      ;; UP ARROW
      38 (when open
           (.preventDefault event)
           (let [new-index (if (= highlighted-index -1)
                             (dec (count filtered-options))
                             (max -1 (dec highlighted-index)))]
             (common/set-component-state! el {:highlighted-index new-index})
             (common/highlight-option! filtered-options new-index)))
      ;; DOWN ARROW  
      40 (when open
           (.preventDefault event)
           (let [new-index (if (= highlighted-index -1)
                             0
                             (min (dec (count filtered-options)) (inc highlighted-index)))]
             (common/set-component-state! el {:highlighted-index new-index})
             (common/highlight-option! filtered-options new-index)))
      nil)))

;; =====================================================
;; DESKTOP EVENT SETUP
;; =====================================================

(defn setup-event-listeners!
  "Setup event listeners for wrapper + input + dialog structure"
  [^js el ^js shadow-root]
  (let [wrapper (.querySelector shadow-root ".dropdown-wrapper")
        stub-input (.querySelector shadow-root ".dropdown-input.dropdown-stub")
        search-input (.querySelector shadow-root ".dropdown-search-input")
        close-btn (.querySelector shadow-root ".dropdown-close")
        slot (.querySelector shadow-root "slot")
        dialog (.querySelector shadow-root ".dropdown-dialog")]

    ;; Stub input click - opens dialog
    (when stub-input
      (.addEventListener stub-input "click" (partial handle-stub-click! el shadow-root)))

    ;; Search input events (inside dialog) - includes escape key handling
    (when search-input
      (.addEventListener search-input "input" (partial handle-input-change! el shadow-root))
      (.addEventListener search-input "keydown" (partial handle-keyboard! el shadow-root)))

    ;; Close button
    (when close-btn
      (.addEventListener close-btn "click" (partial handle-close-click! el shadow-root)))

    ;; Option clicks via event delegation on slot
    (when slot
      (.addEventListener slot "click" (partial handle-option-click! el shadow-root)))

    ;; Dialog close event - for cleanup
    (when dialog
      (.addEventListener dialog "close" (partial handle-dialog-close! el shadow-root)))

    ;; Native dialog backdrop clicks (like modal)
    (when dialog
      (.addEventListener dialog "click" (partial handle-backdrop-click! el shadow-root)))

    ;; Store cleanup function
    (set! (.-tyDropdownCleanup el)
          (fn []
            ;; Cleanup regular event listeners
            (when stub-input
              (.removeEventListener stub-input "click" (partial handle-stub-click! el shadow-root)))
            (when search-input
              (.removeEventListener search-input "input" (partial handle-input-change! el shadow-root))
              (.removeEventListener search-input "keydown" (partial handle-keyboard! el shadow-root)))
            (when close-btn
              (.removeEventListener close-btn "click" (partial handle-close-click! el shadow-root)))
            (when slot
              (.removeEventListener slot "click" (partial handle-option-click! el shadow-root)))
            (when dialog
              (.removeEventListener dialog "close" (partial handle-dialog-close! el shadow-root))
              (.removeEventListener dialog "click" (partial handle-backdrop-click! el shadow-root)))))))

;; =====================================================
;; DESKTOP RENDERING
;; =====================================================

(defn render!
  "Desktop implementation using wrapper + input + dialog structure for perfect positioning"
  [^js el ^js root]
  (let [{:keys [placeholder searchable disabled]} (common/dropdown-attributes el)]

    ;; Create wrapper + input + dialog structure
    (when-not (.querySelector root ".dropdown-wrapper")
      (set! (.-innerHTML root)
            (str
             ;; Wrapper - provides positioning context, no styling
              "<div class=\"dropdown-wrapper\">"

             ;; Read-only input - handles all visual styling
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

             ;; Dialog - positioned relative to wrapper (no border offset)
              "  <dialog class=\"dropdown-dialog\">"
              "    <div class=\"dropdown-header\">"
              "      <input class=\"dropdown-search-input\" type=\"text\" "
              "             placeholder=\"" (if searchable "Search..." placeholder) "\" "
              (when disabled "disabled ")
              "      />"
              (when-not disabled
                "      <button class=\"dropdown-close\" type=\"button\" aria-label=\"Close\">"
                "        <svg viewBox=\"0 0 20 20\" fill=\"currentColor\">"
                "          <path fill-rule=\"evenodd\" d=\"M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z\" clip-rule=\"evenodd\" />"
                "        </svg>"
                "      </button>")
              "    </div>"
              "    <div class=\"dropdown-options\">"
              "      <slot></slot>"
              "    </div>"
              "  </dialog>"
              "</div>"))

      ;; Setup event listeners
      (setup-event-listeners! el root))

    ;; Update stub display to match current value
    (common/update-stub-display! el root)

    ;; For non-searchable dropdowns, ensure all options are visible
    (when-not searchable
      (let [options (map common/get-option-data (common/get-options root))]
        (when (seq options)
          (common/update-option-visibility! options options))))))
