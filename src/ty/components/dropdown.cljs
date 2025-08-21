(ns ty.components.dropdown
  "Clean dropdown component with stub + dialog overlay pattern"
  (:require [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load dropdown styles
(defstyles dropdown-styles)

;; Simple global management - just track current open dropdown
(defonce current-open-dropdown (atom nil))

(declare close-dropdown!)

(defn close-current-dropdown!
  "Close currently open dropdown if any"
  []
  (when-let [dropdown @current-open-dropdown]
    (when-let [shadow-root (.-shadowRoot dropdown)]
      (close-dropdown! dropdown shadow-root))
    (reset! current-open-dropdown nil)))

(defn set-current-dropdown!
  "Set the currently open dropdown, closing any previous one"
  [dropdown]
  (close-current-dropdown!)
  (reset! current-open-dropdown dropdown))

(defn dropdown-attributes
  "Read dropdown attributes directly from element"
  [^js el]
  {:value (or (wcs/attr el "value") "")
   :placeholder (or (wcs/attr el "placeholder") "Select an option...")
   :searchable (let [searchable? (wcs/parse-bool-attr el "searchable")
                     not-searchable? (wcs/parse-bool-attr el "not-searchable")]
                 (cond
                   searchable? true
                   not-searchable? false
                   :else true))
   :disabled (wcs/parse-bool-attr el "disabled")
   :readonly (wcs/parse-bool-attr el "readonly")
   :size (or (wcs/attr el "size") "md")
   :flavor (or (wcs/attr el "flavor") "neutral")})

(defn get-component-state
  "Get or initialize simplified component state"
  [^js el]
  (or (.-tyDropdownState el)
      (let [initial-state {:open false
                           :search ""
                           :highlighted-index -1
                           :filtered-options []}]
        (set! (.-tyDropdownState el) initial-state)
        initial-state)))

(defn set-component-state!
  "Update component state"
  [^js el updates]
  (let [new-state (merge (get-component-state el) updates)]
    (set! (.-tyDropdownState el) new-state)
    new-state))

;; Option management functions (copied from old implementation)
(defn get-options
  "Get all option elements from slot"
  [^js shadow-root]
  (when-let [slot (.querySelector shadow-root "slot")]
    (array-seq (.assignedElements slot))))

(defn get-option-data
  "Extract value and text from option element"
  [^js option]
  {:value (or (.-value option) (.-textContent option))
   :text (.-textContent option)
   :element option})

(defn filter-options
  "Filter options based on search string"
  [options search]
  (if (empty? search)
    options
    (let [search-lower (.toLowerCase search)]
      (filter (fn [{:keys [text]}]
                (.includes (.toLowerCase text) search-lower))
              options))))

(defn update-option-visibility!
  "Update visibility of options based on filtered list"
  [filtered-options all-options]
  (let [visible-values (set (map :value filtered-options))]
    (doseq [{:keys [value element]} all-options]
      (if (contains? visible-values value)
        (.removeAttribute element "hidden")
        (.setAttribute element "hidden" "")))))

(defn clear-highlights!
  "Remove highlighting from all options"
  [options]
  (doseq [{:keys [element]} options]
    (.removeAttribute element "highlighted")))

(defn highlight-option!
  "Highlight option at index"
  [options index]
  (clear-highlights! options)
  (when (and index (>= index 0) (< index (count options)))
    (let [{:keys [element]} (nth options index)]
      (.setAttribute element "highlighted" ""))))

(defn clear-selection!
  "Remove selection from all options"
  [options]
  (doseq [{:keys [element]} options]
    (.removeAttribute element "selected")))

(defn select-option!
  "Mark option as selected"
  [options value]
  (clear-selection! options)
  (when-let [option (first (filter #(= (:value %) value) options))]
    (.setAttribute (:element option) "selected" "")))

(defn dispatch-change-event!
  "Dispatch custom change event"
  [^js el value text]
  (let [detail (js-obj "value" value "text" text)
        event (js/CustomEvent. "change"
                               #js {:detail detail
                                    :bubbles true
                                    :cancelable true})]
    (.dispatchEvent el event)))

(defn update-stub-display!
  "Update stub to show selected value or placeholder"
  [^js el ^js shadow-root]
  (let [{:keys [value placeholder]} (dropdown-attributes el)
        stub-value (.querySelector shadow-root ".dropdown-value")
        options (map get-option-data (get-options shadow-root))]
    (when stub-value
      (if-let [selected-option (first (filter #(= (:value %) value) options))]
        (do
          (set! (.-textContent stub-value) (:text selected-option))
          (select-option! options value))
        (set! (.-textContent stub-value) placeholder)))))

;; Simple positioning - just above/below decision
(defn update-dropdown-position!
  "Position dialog so input appears exactly where stub was"
  [^js el ^js shadow-root]
  (let [stub (.querySelector shadow-root ".dropdown-stub")
        dialog (.querySelector shadow-root ".dropdown-dialog")
        {:keys [open]} (get-component-state el)]

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

(defn close-dropdown!
  "Close dropdown dialog and reset state"
  [^js el ^js shadow-root]
  (let [_ (set-component-state! el {:open false
                                    :highlighted-index -1})
        dialog (.querySelector shadow-root ".dropdown-dialog")
        chevron (.querySelector shadow-root ".dropdown-chevron")]

    ;; Clear from global state
    (when (= @current-open-dropdown el)
      (reset! current-open-dropdown nil))

    ;; Close dialog
    (when (and dialog (.-open dialog))
      (.close dialog))

    (when chevron
      (.remove (.-classList chevron) "open"))

    ;; Reset search if not searchable
    (let [{:keys [searchable]} (dropdown-attributes el)]
      (when-not searchable
        (set-component-state! el {:search ""})))))

(defn open-dropdown!
  "Open dropdown dialog with simplified management"
  [^js el ^js shadow-root]
  (let [{:keys [disabled readonly searchable value]} (dropdown-attributes el)]
    (when-not (or disabled readonly)
      ;; Set as current open dropdown (closes any existing)
      (set-current-dropdown! el)

      ;; Initialize filtered options for keyboard navigation
      (let [all-options (map get-option-data (get-options shadow-root))
            current-search (if searchable (:search (get-component-state el)) "")
            filtered (if searchable
                       (filter-options all-options current-search)
                       all-options)
            ;; Find the index of the currently selected option
            selected-index (if (and value (seq filtered))
                             (first (keep-indexed
                                      (fn [idx option]
                                        (when (= (:value option) value) idx))
                                      filtered))
                             -1)]

        (set-component-state! el {:open true
                                  :filtered-options filtered
                                  :highlighted-index selected-index}))

      (let [dialog (.querySelector shadow-root ".dropdown-dialog")
            chevron (.querySelector shadow-root ".dropdown-chevron")
            input (.querySelector shadow-root ".dropdown-input")]

        ;; For non-searchable dropdowns, ensure all options are visible
        (when-not searchable
          (let [options (map get-option-data (get-options shadow-root))]
            (update-option-visibility! options options)))

        (when chevron
          (.add (.-classList chevron) "open"))

        ;; Open dialog - input inside dialog gets focus naturally!
        (when dialog
          (.showModal dialog)

          ;; Set initial input value for searchable dropdowns
          (when (and input searchable)
            (set! (.-value input) ""))

          ;; Position dialog once - no tracking needed
          (update-dropdown-position! el shadow-root)

          ;; Highlight the selected option if any
          (js/setTimeout
            (fn []
              (let [{:keys [highlighted-index filtered-options]} (get-component-state el)]
                (when (>= highlighted-index 0)
                  (highlight-option! filtered-options highlighted-index))))
            50))))))

;; Event handlers
(defn handle-stub-click!
  "Handle click on stub to open dropdown dialog"
  [^js el ^js shadow-root ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (let [{:keys [disabled]} (dropdown-attributes el)]
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
  (let [{:keys [searchable]} (dropdown-attributes el)]
    (when searchable
      (let [search (.-value (.-target event))
            options (map get-option-data (get-options shadow-root))
            filtered (filter-options options search)
            {:keys [value]} (dropdown-attributes el)
            new-highlighted-index (if (and value (seq filtered))
                                    (first (keep-indexed
                                             (fn [idx option]
                                               (when (= (:value option) value) idx))
                                             filtered))
                                    -1)]
        (set-component-state! el {:search search
                                  :filtered-options filtered
                                  :highlighted-index new-highlighted-index})
        (update-option-visibility! filtered options)
        (clear-highlights! options)
        (when (>= new-highlighted-index 0)
          (highlight-option! filtered new-highlighted-index))))))

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
    (let [options (map get-option-data (get-options shadow-root))]
      (select-option! options value))
    ;; Update stub display
    (update-stub-display! el shadow-root)
    ;; Dispatch change event
    (dispatch-change-event! el value text)
    ;; Close dropdown
    (close-dropdown! el shadow-root)))

(defn handle-keyboard!
  "Handle keyboard navigation"
  [^js el ^js shadow-root ^js event]
  (let [{:keys [open highlighted-index filtered-options]} (get-component-state el)
        key-code (.-keyCode event)]
    (case key-code
      ;; ESC - close dropdown
      27 (when open
           (.preventDefault event)
           (close-dropdown! el shadow-root))
      ;; ENTER - select highlighted option
      13 (do
           (.preventDefault event)
           (when open
             (when (and (>= highlighted-index 0) (< highlighted-index (count filtered-options)))
               (let [{:keys [value text]} (nth filtered-options highlighted-index)]
                 (.setAttribute el "value" value)
                 (let [options (map get-option-data (get-options shadow-root))]
                   (select-option! options value))
                 (update-stub-display! el shadow-root)
                 (dispatch-change-event! el value text)
                 (close-dropdown! el shadow-root)))))
      ;; UP ARROW
      38 (when open
           (.preventDefault event)
           (let [new-index (if (= highlighted-index -1)
                             (dec (count filtered-options))
                             (max -1 (dec highlighted-index)))]
             (set-component-state! el {:highlighted-index new-index})
             (highlight-option! filtered-options new-index)))
      ;; DOWN ARROW  
      40 (when open
           (.preventDefault event)
           (let [new-index (if (= highlighted-index -1)
                             0
                             (min (dec (count filtered-options)) (inc highlighted-index)))]
             (set-component-state! el {:highlighted-index new-index})
             (highlight-option! filtered-options new-index)))
      nil)))

(defn handle-outside-click!
  "Handle clicks outside dropdown to close it"
  [^js el ^js shadow-root ^js event]
  (let [{:keys [open]} (get-component-state el)]
    (when open
      (let [path (or (.composedPath event) [])]
        (when-not (some #(= % el) path)
          (close-dropdown! el shadow-root))))))

(defn setup-event-listeners!
  "Setup event listeners for stub + dialog structure"
  [^js el ^js shadow-root]
  (let [stub (.querySelector shadow-root ".dropdown-stub")
        input (.querySelector shadow-root ".dropdown-input")
        close-btn (.querySelector shadow-root ".dropdown-close")
        slot (.querySelector shadow-root "slot")
        outside-handler (partial handle-outside-click! el shadow-root)]

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

    ;; Outside clicks
    (.addEventListener js/document "click" outside-handler)

    ;; Store cleanup function
    (set! (.-tyDropdownCleanup el)
          (fn []
            (when stub
              (.removeEventListener stub "click" (partial handle-stub-click! el shadow-root)))
            (when input
              (.removeEventListener input "input" (partial handle-input-change! el shadow-root))
              (.removeEventListener input "keydown" (partial handle-keyboard! el shadow-root)))
            (when close-btn
              (.removeEventListener close-btn "click" (partial handle-close-click! el shadow-root)))
            (when slot
              (.removeEventListener slot "click" (partial handle-option-click! el shadow-root)))
            (.removeEventListener js/document "click" outside-handler)
            ;; Clear from global state if this was the open one
            (when (= @current-open-dropdown el)
              (reset! current-open-dropdown nil))))))

(defn render! [^js el]
  (let [root (wcs/ensure-shadow el)
        {:keys [placeholder searchable disabled]} (dropdown-attributes el)]

    ;; Ensure styles are loaded
    (ensure-styles! root dropdown-styles "ty-dropdown")

    ;; Create stub + dialog structure
    (when-not (.querySelector root ".dropdown-stub")
      (set! (.-innerHTML root)
            (str
             ;; Stub - Styled to look identical to input for seamless transition
              "<div class=\"dropdown-stub dropdown-input-style\" "
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
    (update-stub-display! el root)

    ;; For non-searchable dropdowns, ensure all options are visible
    (when-not searchable
      (let [options (map get-option-data (get-options root))]
        (when (seq options)
          (update-option-visibility! options options))))))

(defn cleanup! [^js el]
  (when-let [cleanup-fn (.-tyDropdownCleanup el)]
    (cleanup-fn)
    (set! (.-tyDropdownCleanup el) nil)))

(wcs/define! "ty-dropdown"
  {:observed [:value :placeholder :searchable :not-searchable :disabled :readonly :size :flavor]
   :connected render!
   :disconnected cleanup!
   :attr (fn [^js el _ _old _]
           (render! el))})
