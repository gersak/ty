(ns ty.components.dropdown.desktop
  "Desktop implementation of dropdown using showModal() for top layer rendering"
  (:require
    [ty.components.dropdown.common :as common]
    [ty.components.dropdown.global :as global]
    [ty.scroll-lock :as scroll-lock]
    [ty.shim :as wcs]))

;; =====================================================
;; DESKTOP POSITIONING
;; =====================================================

(defn calculate-dropdown-position!
  "Calculate bottom-left anchored position with smart direction detection"
  [^js el ^js shadow-root ^js dialog]
  (let [stub (.querySelector shadow-root ".dropdown-stub")]
    (when (and stub dialog)
      (let [stub-rect (.getBoundingClientRect stub)
            viewport-height (.-innerHeight js/window)
            viewport-width (.-innerWidth js/window)

            ;; Smart height estimation - could be enhanced later
            estimated-height (+ 10 (.-height (.getBoundingClientRect dialog)))
            padding 8 ; Spacing from viewport edges

            ;; Available space calculations
            space-below (- viewport-height (.-bottom stub-rect))
            space-above (.-top stub-rect)
            space-right (- viewport-width (.-left stub-rect))

            ;; Smart direction logic
            position-below? (>= space-below (+ estimated-height padding))
            fits-horizontally? (>= space-right (.-width stub-rect))

            wrap-padding 20

            ;; Calculate position coordinates
            x (if fits-horizontally?
                (- (.-left stub-rect) 20) ; Normal left alignment
                (max padding (- viewport-width (.-width stub-rect) padding))) ; Right edge fallback

            y (cond
                ;; Prefer below if space available
                position-below?
                (- (.-top stub-rect) wrap-padding)

                ;; Position above if enough space
                (>= space-above (+ estimated-height padding))
                (- viewport-height (.-bottom stub-rect) wrap-padding)

                ;; Constrained space - fit within viewport
                :else
                (- (.-top stub-rect) wrap-padding))

            width (+ (.-width stub-rect) wrap-padding wrap-padding)]

        ;; Set CSS variables for positioning
        (.setProperty (.-style el) "--dropdown-x" (str x "px"))
        (.setProperty (.-style el) "--dropdown-y" (str y "px"))
        (.setProperty (.-style el) "--dropdown-width" (str width "px"))
        (.setProperty (.-style el) "--dropdown-offset-x" "0px")
        (.setProperty (.-style el) "--dropdown-offset-y" "0px")
        (.setProperty (.-style el) "--dropdown-padding" (str wrap-padding "px"))

        ;; Set direction classes for CSS styling
        (if position-below?
          (do
            (.add (.-classList dialog) "position-below")
            (.remove (.-classList dialog) "position-above"))
          (do
            (.add (.-classList dialog) "position-above")
            (.remove (.-classList dialog) "position-below")))

        ;; Optional: Store direction variable for debugging
        (.setProperty (.-style el) "--dropdown-direction"
                      (if position-below? "below" "above"))))))

;; =====================================================
;; DESKTOP DROPDOWN CONTROL
;; =====================================================

(defn close-dropdown!
  "Close dropdown dialog and cleanup"
  [^js el]
  (let [dropdown-id (str "dropdown-" (.-id el) "-" (hash el))]
    ;; Unlock scroll using new unified system
    (scroll-lock/unlock-scroll! dropdown-id))
  (let [shadow-root (wcs/ensure-shadow el)]
    (when-let [dialog (.querySelector shadow-root ".dropdown-dialog")]
      (.remove (.-classList dialog) "open")
      (.remove (.-classList dialog) "position-above")
      (.remove (.-classList dialog) "position-below")
      (.close dialog))
    (common/set-component-state! el {:open false
                                     :highlighted-index -1})
    (when-let [chevron (.querySelector shadow-root ".dropdown-chevron")]
      (.remove (.-classList chevron) "open"))
    (when-let [search-chevron (.querySelector shadow-root ".dropdown-search-chevron")]
      (.remove (.-classList search-chevron) "open"))
    (let [{:keys [searchable]} (common/dropdown-attributes el)]
      (when-not searchable
        (common/set-component-state! el {:search ""}))))
  ;; Clear from global state if this is the current dropdown
  (global/clear-current-dropdown! el))

      ;; Show modal first so browser can calculate proper dimensions
(defn open-dropdown!
  "Open dropdown dialog with scroll prevention and highlight selected value"
  [^js el ^js shadow-root]
  (let [dropdown-id (str "dropdown-" (.-id el) "-" (hash el))
        dialog (.querySelector shadow-root ".dropdown-dialog")]
    (when dialog
      ;; Lock scroll using new unified system
      (scroll-lock/lock-scroll! dropdown-id)

      ;; Show modal first so browser can calculate proper dimensions
      (.showModal dialog)
      (.add (.-classList dialog) "open")

      ;; Position dropdown AFTER showing modal
      (calculate-dropdown-position! el shadow-root dialog)

      ;; Update component state
      (common/set-component-state! el {:open true})

      ;; Update visual states
      (when-let [chevron (.querySelector shadow-root ".dropdown-chevron")]
        (.add (.-classList chevron) "open"))
      (when-let [search-chevron (.querySelector shadow-root ".dropdown-search-chevron")]
        (.add (.-classList search-chevron) "open"))

      ;; Initialize options state and find selected option
      (let [options (map common/get-option-data (common/get-options shadow-root))
            {:keys [current-value]} (common/get-dropdown-state el)
            ;; Find the index of the currently selected option
            selected-index (when current-value
                             (first (keep-indexed
                                      (fn [idx option]
                                        (when (= (:value option) current-value) idx))
                                      options)))
            initial-highlighted-index (or selected-index -1)]

        (common/set-component-state! el {:filtered-options options
                                         :highlighted-index initial-highlighted-index})

        ;; Highlight and scroll to selected option if it exists
        (when (and selected-index (>= selected-index 0))
          (common/highlight-option! options selected-index)))

      ;; Focus search input if searchable
      (let [{:keys [searchable]} (common/dropdown-attributes el)]
        (when searchable
          (when-let [search-input (.querySelector shadow-root ".dropdown-search-input")]
            (.focus search-input))))

      ;; Register as current dropdown globally
      (global/set-current-dropdown! el))))

;; =====================================================
;; EVENT HANDLERS
;; =====================================================

(defn handle-outside-click!
  "Close dropdown when clicking outside"
  [^js el ^js shadow-root ^js event]
  (let [{:keys [open]} (common/get-component-state el)]
    (when open
      (let [wrapper (.querySelector shadow-root ".dropdown-wrapper")
            target (.-target event)]
        (when (and wrapper (not (.contains wrapper target)))
          (.preventDefault event)
          (.stopPropagation event)
          (close-dropdown! el))))))

(defn handle-stub-click!
  "Open dropdown when clicking stub"
  [^js el ^js shadow-root ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (let [{:keys [disabled readonly]} (common/dropdown-attributes el)]
    (when-not (or disabled readonly)
      (open-dropdown! el shadow-root))))

(defn handle-input-change!
  "Handle search input changes"
  [^js el ^js shadow-root ^js event]
  (let [{:keys [searchable]} (common/dropdown-attributes el)
        search (.-value (.-target event))]
    (common/set-component-state! el {:search search})
    (if searchable
      ;; Internal search: filter options locally (default behavior when searchable is true)
      (let [options (map common/get-option-data (common/get-options shadow-root))
            filtered (common/filter-options options search)
            {:keys [value]} (common/dropdown-attributes el)
            new-highlighted-index (when (and value (seq filtered))
                                    (first (keep-indexed
                                             (fn [idx option]
                                               (when (= (:value option) value) idx))
                                             filtered)))]
        (common/set-component-state! el {:filtered-options filtered
                                         :highlighted-index (or new-highlighted-index -1)})
        (common/update-option-visibility! filtered options)
        (common/clear-highlights! options)
        (when (>= (or new-highlighted-index -1) 0)
          (common/highlight-option! filtered new-highlighted-index)))

      ;; External search: dispatch event for external handling (when searchable is false)
      (common/dispatch-search-event! el search))))

(defn handle-search-blur!
  "Reset search when search input loses focus"
  [^js el ^js shadow-root ^js _]
  (let [{:keys [searchable]} (common/dropdown-attributes el)]
    (when searchable
      (common/set-component-state! el {:search ""})
      (let [options (map common/get-option-data (common/get-options shadow-root))]
        (when (seq options)
          (common/set-component-state! el {:filtered-options options
                                           :highlighted-index -1})
          (common/update-option-visibility! options options)
          (common/clear-highlights! options)))
      (when-let [search-input (.querySelector shadow-root ".dropdown-search-input")]
        (set! (.-value search-input) "")))))

(defn handle-option-click!
  "Handle option selection"
  [^js el _ ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (let [option-element (.-target event)]
    (common/select-option! el option-element)
    (common/update-selection-display! el)
    (common/dispatch-change-event! el option-element)
    (close-dropdown! el)))

(defn handle-keyboard!
  "Handle keyboard navigation with circular scrolling and proper Enter selection"
  [^js el ^js shadow-root ^js event]
  (let [{:keys [open highlighted-index filtered-options]} (common/get-component-state el)
        key-code (.-keyCode event)
        target (.-target event)
        search-input (.querySelector shadow-root ".dropdown-search-input")]

    ;; Only handle navigation keys when dropdown is open and either:
    ;; 1. Event comes from search input, OR
    ;; 2. Event comes from document but search input is not focused
    (when (and open
               (or (= target search-input)
                   (not= (.-activeElement js/document) search-input)))
      (case key-code
        27 (do ; ESCAPE
             (.preventDefault event)
             (.stopPropagation event)
             (close-dropdown! el))
        13 (do ; ENTER
             (.preventDefault event)
             (.stopPropagation event)
             (when (and (>= highlighted-index 0) (< highlighted-index (count filtered-options)))
               (let [{:keys [element]} (nth filtered-options highlighted-index)]
                 (common/select-option! el element)
                 (common/update-selection-display! el)
                 (common/dispatch-change-event! el element)
                 (close-dropdown! el))))
        38 (do ; UP ARROW
             (.preventDefault event)
             (.stopPropagation event)
             (let [options-count (count filtered-options)
                   new-index (cond
                               ;; No options available
                               (zero? options-count) -1

                               ;; Nothing highlighted, go to last option
                               (= highlighted-index -1) (dec options-count)

                               ;; At first option, wrap to last
                               (= highlighted-index 0) (dec options-count)

                               ;; Move up one
                               :else (dec highlighted-index))]
               (common/set-component-state! el {:highlighted-index new-index})
               (common/highlight-option! filtered-options new-index)))
        40 (do ; DOWN ARROW
             (.preventDefault event)
             (.stopPropagation event)
             (let [options-count (count filtered-options)
                   new-index (cond
                               ;; No options available
                               (zero? options-count) -1

                               ;; Nothing highlighted, go to first option
                               (= highlighted-index -1) 0

                               ;; At last option, wrap to first
                               (= highlighted-index (dec options-count)) 0

                               ;; Move down one
                               :else (inc highlighted-index))]
               (common/set-component-state! el {:highlighted-index new-index})
               (common/highlight-option! filtered-options new-index)))
        nil))))

;; =====================================================
;; EVENT SETUP
;; =====================================================

(defn setup-event-listeners!
  "Setup all event listeners for desktop mode"
  [^js el]
  (let [^js shadow-root (wcs/ensure-shadow el)
        stub (.querySelector shadow-root ".dropdown-stub")
        search-input (.querySelector shadow-root ".dropdown-search-input")
        slot (.querySelector shadow-root "#options-slot")
        outside-click-handler (partial handle-outside-click! el shadow-root)
        keyboard-handler (partial handle-keyboard! el shadow-root)]

    ;; Setup event listeners
    (when stub
      (.addEventListener stub "click" (partial handle-stub-click! el shadow-root)))
    (when search-input
      (.addEventListener search-input "input" (partial handle-input-change! el shadow-root))
      (.addEventListener search-input "keydown" keyboard-handler)
      (.addEventListener search-input "blur" (partial handle-search-blur! el shadow-root)))
    (when slot
      (.addEventListener slot "pointerdown" (partial handle-option-click! el shadow-root)))

    ;; CRITICAL: Listen for keyboard events at document level for dropdown navigation
    ;; This ensures arrow keys work even when focus shifts away from search input
    (.addEventListener js/document "keydown" keyboard-handler)
    (.addEventListener js/document "click" outside-click-handler)
    (set! (.-tyOutsideClickHandler el) outside-click-handler)
    (set! (.-tyKeyboardHandler el) keyboard-handler)

    ;; Store cleanup function
    (set! (.-tyDropdownCleanup el)
          (fn []
            ;; Cleanup event listeners
            (when stub
              (.removeEventListener stub "click" (partial handle-stub-click! el shadow-root)))
            (when search-input
              (.removeEventListener search-input "input" (partial handle-input-change! el shadow-root))
              (.removeEventListener search-input "keydown" keyboard-handler)
              (.removeEventListener search-input "blur" (partial handle-search-blur! el shadow-root)))
            (when slot
              (.removeEventListener slot "click" (partial handle-option-click! el shadow-root)))
            (when-let [handler (.-tyOutsideClickHandler el)]
              (.removeEventListener js/document "click" handler)
              (set! (.-tyOutsideClickHandler el) nil))
            (when-let [handler (.-tyKeyboardHandler el)]
              (.removeEventListener js/document "keydown" handler)
              (set! (.-tyKeyboardHandler el) nil))))))

;; =====================================================
;; DESKTOP RENDERING
;; =====================================================

(defn render!
  "Desktop implementation using dialog structure (self-contained)"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        {:keys [placeholder searchable disabled label required size]} (common/dropdown-attributes el)]
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
             ;; Dropdown wrapper
              "  <div class=\"dropdown-wrapper\">"
              "    <div class=\"dropdown-stub " size "\"" (when disabled " disabled") ">"
              "      <slot name=\"selected\"></slot>"
              "      <span class=\"dropdown-placeholder\">" placeholder "</span>"
              "    <div class=\"dropdown-chevron\">"
              "      <svg viewBox=\"0 0 20 20\" fill=\"currentColor\">"
              "        <path fill-rule=\"evenodd\" d=\"M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z\" clip-rule=\"evenodd\" />"
              "      </svg>"
              "    </div>"
              "    </div>"
              "    <dialog class=\"dropdown-dialog\">"
              "      <div class=\"dropdown-header\">"
              "        <input class=\"dropdown-search-input " size "\" type=\"text\""
              "               placeholder=\"" (if searchable "Search..." placeholder) "\""
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
      (setup-event-listeners! el))

    ;; Ensure all options visible for non-searchable dropdowns
    (when-not searchable
      (let [options (map common/get-option-data (common/get-options root))]
        (when (seq options)
          (common/update-option-visibility! options options))))))
