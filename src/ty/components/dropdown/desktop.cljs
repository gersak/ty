(ns ty.components.dropdown.desktop
  "Desktop implementation of dropdown using dialog element with smart positioning"
  (:require [ty.components.dropdown.common :as common]
            [ty.util.outside-click :as outside-click]))

;; =====================================================
;; DESKTOP POSITIONING
;; =====================================================

(defn update-position!
  "Position dialog so input appears exactly where stub was"
  [^js el ^js shadow-root]
  (let [stub (.querySelector shadow-root ".dropdown-stub")
        dialog (.querySelector shadow-root ".dropdown-dialog")
        {:keys [open]} (common/get-component-state el)]

    (when (and open stub dialog)
      (let [stub-rect (.getBoundingClientRect stub)
            stub-top (.-top stub-rect)
            stub-left (.-left stub-rect)
            stub-width (.-width stub-rect)
            stub-height (.-height stub-rect)
            viewport-width (.-innerWidth js/window)
            viewport-height (.-innerHeight js/window)
            is-mobile (<= viewport-width 768)]

        (if is-mobile
          ;; Mobile: Full screen dialog
          (do
            (set! (.. dialog -style -position) "fixed")
            (set! (.. dialog -style -top) "0")
            (set! (.. dialog -style -left) "0")
            (set! (.. dialog -style -width) "100vw")
            (set! (.. dialog -style -height) "100vh")
            (set! (.. dialog -style -margin) "0")
            (.add (.-classList dialog) "mobile-fullscreen"))

          ;; Desktop: Position input exactly where stub was
          (do
            (.remove (.-classList dialog) "mobile-fullscreen")

            ;; Simple decision: above or below?
            (let [space-below (- viewport-height stub-top stub-height)
                  position-below (> space-below 300)] ; Need ~300px for dropdown

              ;; Position dialog so input appears exactly where stub was
              (set! (.. dialog -style -position) "fixed")
              (set! (.. dialog -style -left) (str stub-left "px"))
              (set! (.. dialog -style -width) (str stub-width "px"))
              (set! (.. dialog -style -margin) "0")

              (if position-below
                ;; Position below: input at stub position, options below
                (set! (.. dialog -style -top) (str stub-top "px"))
                ;; Position above: calculate so input aligns with stub
                (set! (.. dialog -style -bottom) (str (- viewport-height stub-top stub-height) "px")))

              ;; Add class for CSS to handle layout direction
              (.remove (.-classList dialog) "position-above" "position-below")
              (.add (.-classList dialog) (if position-below "position-below" "position-above")))))))))

;; =====================================================
;; DESKTOP DROPDOWN CONTROL
;; =====================================================

(defn close-dropdown!
  "Programmatically close dropdown dialog"
  [^js el ^js shadow-root]
  (let [dialog (.querySelector shadow-root ".dropdown-dialog")]
    ;; Close dialog - this will trigger the dialog close event
    (when (and dialog (.-open dialog))
      (.close dialog))))

(declare handle-option-click! handle-outside-action!)

(defn open-dropdown!
  "Open dropdown dialog"
  [^js el ^js shadow-root]
  (let [{:keys [disabled readonly searchable value]} (common/dropdown-attributes el)]
    (when-not (or disabled readonly)
      ;; Close any other open dropdown first
      (outside-click/close-current-component!)

      ;; Register this dropdown as current
      (outside-click/set-current-component! el #(close-dropdown! % shadow-root))

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
        (when dialog
          (.showModal dialog)

          ;; Set initial input value for searchable dropdowns
          (when (and input searchable)
            (set! (.-value input) ""))

          ;; Position dialog once
          (update-position! el shadow-root)

          ;; Setup outside click detection
          (when-not (.-tyOutsideClickCleanup el)
            (set! (.-tyOutsideClickCleanup el)
                  (outside-click/setup-outside-handlers!
                    dialog
                    (partial handle-outside-action! el shadow-root)
                    {:mobile-optimized? true
                     :prevent-default? false
                     :touch-enabled? true
                     :escape-key? true})))

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

(defn handle-outside-action!
  "Handle outside click or escape key to close dropdown"
  [^js el ^js shadow-root ^js event]
  ;; Only close if this dropdown is the current one
  (when (outside-click/is-current-component? el)
    (close-dropdown! el shadow-root)))

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
      (common/set-component-state! el {:search ""})))

  ;; Clear from global registry
  (when (outside-click/is-current-component? el)
    (reset! outside-click/current-open-component nil)))

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
  "Handle keyboard navigation (arrow keys and enter only - ESC handled by outside-click util)"
  [^js el ^js shadow-root ^js event]
  (let [{:keys [open highlighted-index filtered-options]} (common/get-component-state el)
        key-code (.-keyCode event)]
    (case key-code
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
  "Setup event listeners for stub + dialog structure"
  [^js el ^js shadow-root]
  (let [stub (.querySelector shadow-root ".dropdown-stub")
        input (.querySelector shadow-root ".dropdown-input")
        close-btn (.querySelector shadow-root ".dropdown-close")
        slot (.querySelector shadow-root "slot")
        dialog (.querySelector shadow-root ".dropdown-dialog")]

    ;; Stub click - opens dialog
    (when stub
      (.addEventListener stub "click" (partial handle-stub-click! el shadow-root)))

    ;; Input events (inside dialog)
    (when input
      (.addEventListener input "input" (partial handle-input-change! el shadow-root))
      (.addEventListener input "keydown" (partial handle-keyboard! el shadow-root)))

    ;; Close button
    (when close-btn
      (.addEventListener close-btn "click" (partial handle-close-click! el shadow-root)))

    ;; Option clicks via event delegation on slot
    (when slot
      (.addEventListener slot "click" (partial handle-option-click! el shadow-root)))

    ;; Dialog close event - now just for cleanup
    (when dialog
      (.addEventListener dialog "close" (partial handle-dialog-close! el shadow-root)))

    ;; Store cleanup function
    (set! (.-tyDropdownCleanup el)
          (fn []
            ;; Cleanup outside click handlers
            (when-let [outside-cleanup (.-tyOutsideClickCleanup el)]
              (outside-cleanup)
              (set! (.-tyOutsideClickCleanup el) nil))

            ;; Cleanup regular event listeners
            (when stub
              (.removeEventListener stub "click" (partial handle-stub-click! el shadow-root)))
            (when input
              (.removeEventListener input "input" (partial handle-input-change! el shadow-root))
              (.removeEventListener input "keydown" (partial handle-keyboard! el shadow-root)))
            (when close-btn
              (.removeEventListener close-btn "click" (partial handle-close-click! el shadow-root)))
            (when slot
              (.removeEventListener slot "click" (partial handle-option-click! el shadow-root)))
            (when dialog
              (.removeEventListener dialog "close" (partial handle-dialog-close! el shadow-root)))))))

;; =====================================================
;; DESKTOP RENDERING
;; =====================================================

(defn render!
  "Desktop implementation using dialog element with smart positioning"
  [^js el ^js root]
  (let [{:keys [placeholder searchable disabled]} (common/dropdown-attributes el)]

    ;; Create stub + dialog structure
    (when-not (.querySelector root ".dropdown-stub")
      (set! (.-innerHTML root)
            (str
             ;; Stub - Styled to look identical to input for seamless transition
              "<div class=\"dropdown-stub\" "
              (when disabled "disabled ")
              ">"
              "  <span class=\"dropdown-value\">" placeholder "</span>"
              "  <div class=\"dropdown-chevron\">"
              "    <svg viewBox=\"0 0 20 20\" fill=\"currentColor\">"
              "      <path fill-rule=\"evenodd\" d=\"M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z\" clip-rule=\"evenodd\" />"
              "    </svg>"
              "  </div>"
              "</div>"

             ;; Dialog - Contains input + options (opens as overlay)
              "<dialog class=\"dropdown-dialog\">"
              "  <div class=\"dropdown-header\">"
              "    <input class=\"dropdown-input\" type=\"text\" "
              "           placeholder=\"" placeholder "\" "
              (when disabled "disabled ")
              "    />"
              (when-not disabled
                "    <button class=\"dropdown-close\" type=\"button\" aria-label=\"Close\">"
                "      <svg viewBox=\"0 0 20 20\" fill=\"currentColor\">"
                "        <path fill-rule=\"evenodd\" d=\"M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z\" clip-rule=\"evenodd\" />"
                "      </svg>"
                "    </button>")
              "  </div>"
              "  <div class=\"dropdown-options\">"
              "    <slot></slot>"
              "  </div>"
              "</dialog>"))

      ;; Setup event listeners
      (setup-event-listeners! el root))

    ;; Update stub display to match current value
    (common/update-stub-display! el root)

    ;; For non-searchable dropdowns, ensure all options are visible
    (when-not searchable
      (let [options (map common/get-option-data (common/get-options root))]
        (when (seq options)
          (common/update-option-visibility! options options))))))
