(ns ty.components.dropdown
  "Smart dropdown component with search filtering and intelligent positioning"
  (:require [ty.css :refer [ensure-styles!]]
            [ty.positioning :as pos]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load dropdown styles
(defstyles dropdown-styles)

;; Global registry to track all open dropdowns
(defonce open-dropdowns (atom #{}))

;; Store positioning cleanup functions
(defonce position-cleanup-fns (js/WeakMap.))

(declare close-dropdown!)

(defn close-all-dropdowns!
  "Close all currently open dropdowns"
  []
  (doseq [^js dropdown @open-dropdowns]
    (when (and dropdown (.-tyDropdownState dropdown))
      (let [shadow-root (.-shadowRoot dropdown)]
        (when shadow-root
          (close-dropdown! dropdown shadow-root))))))

(defn close-other-dropdowns!
  "Close all dropdowns except the specified one"
  [except-dropdown]
  (doseq [^js dropdown @open-dropdowns]
    (when (and dropdown
               (not= dropdown except-dropdown)
               (.-tyDropdownState dropdown))
      (let [shadow-root (.-shadowRoot dropdown)]
        (when shadow-root
          (close-dropdown! dropdown shadow-root))))))

(defn register-dropdown!
  "Register a dropdown as open"
  [dropdown]
  (swap! open-dropdowns conj dropdown))

(defn unregister-dropdown!
  "Unregister a dropdown"
  [dropdown]
  (swap! open-dropdowns disj dropdown))

(defn dropdown-attributes
  "Read all dropdown attributes directly from element"
  [^js el]
  {:value (or (wcs/attr el "value") "")
   :placeholder (or (wcs/attr el "placeholder") "Select an option...")
   :searchable (let [searchable-attr (wcs/attr el "searchable")]
                 (if (nil? searchable-attr)
                   true ; default to true if not specified
                   (wcs/parse-bool-attr el "searchable")))
   :disabled (wcs/parse-bool-attr el "disabled")
   :readonly (wcs/parse-bool-attr el "readonly")
   :size (or (wcs/attr el "size") "md")
   :flavor (or (wcs/attr el "flavor") "neutral")
   ;; New positioning attributes
   :placement (or (wcs/attr el "placement") "bottom-start")
   :auto-placement (if (wcs/attr el "auto-placement")
                     (wcs/parse-bool-attr el "auto-placement")
                     true) ; default to true
   :offset (or (wcs/parse-int-attr el "offset") 4)})

(defn get-component-state
  "Get or initialize component state on element"
  [^js el]
  (or (.-tyDropdownState el)
      (let [initial-state {:open false
                           :search ""
                           :highlighted-index -1
                           :filtered-options []
                           :current-placement nil
                           :position-cleanup nil}]
        (set! (.-tyDropdownState el) initial-state)
        initial-state)))

(defn set-component-state!
  "Update component state"
  [^js el updates]
  (let [new-state (merge (get-component-state el) updates)]
    (set! (.-tyDropdownState el) new-state)
    new-state))

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

(defn scroll-option-into-view!
  "Scroll an option element into view within the dropdown container"
  [^js option-element ^js options-container]
  (when (and option-element options-container)
    ;; Use scrollIntoView if available for smoother animation
    (if (.-scrollIntoView option-element)
      (.scrollIntoView option-element #js {:block "nearest"
                                           :behavior "smooth"})
      ;; Fallback to manual calculation
      (let [container-rect (.getBoundingClientRect options-container)
            option-rect (.getBoundingClientRect option-element)
            container-top (.-top container-rect)
            container-bottom (.-bottom container-rect)
            option-top (.-top option-rect)
            option-bottom (.-bottom option-rect)]

        ;; Check if option is above visible area
        (when (< option-top container-top)
          (set! (.-scrollTop options-container)
                (- (.-scrollTop options-container)
                   (- container-top option-top))))

        ;; Check if option is below visible area
        (when (> option-bottom container-bottom)
          (set! (.-scrollTop options-container)
                (+ (.-scrollTop options-container)
                   (- option-bottom container-bottom))))))))

(defn scroll-to-selected-option!
  "Scroll to the currently selected option when dropdown opens"
  [^js el ^js shadow-root]
  (let [{:keys [value]} (dropdown-attributes el)
        options-container (.querySelector shadow-root ".dropdown-options")]
    (when (and value options-container)
      ;; Find the selected option element
      (when-let [selected-option (first (filter #(= (or (.-value (:element %)) (.-textContent (:element %))) value)
                                                (map get-option-data (get-options shadow-root))))]
        (js/setTimeout
          #(scroll-option-into-view! (:element selected-option) options-container)
          100)))))

(defn highlight-option!
  "Highlight option at index and scroll into view"
  [options index ^js shadow-root]
  (clear-highlights! options)
  (when (and (>= index 0) (< index (count options)))
    (let [{:keys [element]} (nth options index)
          options-container (.querySelector shadow-root ".dropdown-options")]
      (.setAttribute element "highlighted" "")
      ;; Scroll the highlighted option into view
      (scroll-option-into-view! element options-container))))

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

(defn update-dropdown-position!
  "Update dropdown position using positioning system"
  [^js el ^js shadow-root]
  (let [input (.querySelector shadow-root ".dropdown-input")
        options-container (.querySelector shadow-root ".dropdown-options")
        {:keys [placement auto-placement offset]} (dropdown-attributes el)
        {:keys [open]} (get-component-state el)]

    (when (and open input options-container)
      ;; Use smart positioning if auto-placement is enabled
      (if auto-placement
        ;; Smart positioning with collision detection
        (let [preferences (get pos/placement-preferences :dropdown)
              position (pos/find-best-position
                         {:target-el input
                          :floating-el options-container
                          :preferences preferences
                          :offset offset
                          :padding 8})]
          ;; Apply calculated position
          (set! (.. options-container -style -position) "fixed")
          (set! (.. options-container -style -top) (str (:y position) "px"))
          (set! (.. options-container -style -left) (str (:x position) "px"))
          (set! (.. options-container -style -width) (str (.-offsetWidth input) "px"))

          ;; Store current placement for styling
          (set-component-state! el {:current-placement (:placement position)})
          (.remove (.-classList options-container) "placement-top" "placement-bottom")
          (let [placement-name (name (:placement position))]
            (.add (.-classList options-container)
                  (if (or (= placement-name "top")
                          (= placement-name "top-start")
                          (= placement-name "top-end"))
                    "placement-top"
                    "placement-bottom"))))

        ;; Manual positioning (original behavior)
        (do
          (set! (.. options-container -style -position) "absolute")
          (set! (.. options-container -style -top)
                (if (clojure.string/includes? (name placement) "top")
                  (str (- 0 (.-offsetHeight options-container) offset) "px")
                  "100%"))
          (set! (.. options-container -style -left) "0")
          (set! (.. options-container -style -width) "100%"))))))

(defn setup-position-tracking!
  "Setup automatic position updates when dropdown is open"
  [^js el ^js shadow-root]
  (let [input (.querySelector shadow-root ".dropdown-input")
        options-container (.querySelector shadow-root ".dropdown-options")
        {:keys [auto-placement]} (dropdown-attributes el)]

    (when (and auto-placement input options-container)
      ;; Cleanup any existing position tracking
      (when-let [cleanup (.get position-cleanup-fns el)]
        (cleanup))

      ;; Setup new auto-update positioning
      (let [cleanup (pos/auto-update
                      input
                      options-container
                      (fn [position]
                        ;; Update position on scroll/resize
                        (set! (.. options-container -style -top) (str (:y position) "px"))
                        (set! (.. options-container -style -left) (str (:x position) "px")))
                      {:preferences (get pos/placement-preferences :dropdown)
                       :offset (:offset (dropdown-attributes el))
                       :padding 8})]

        ;; Store cleanup function
        (.set position-cleanup-fns el cleanup)
        (set-component-state! el {:position-cleanup cleanup})))))

(defn cleanup-position-tracking!
  "Cleanup position tracking"
  [^js el]
  (when-let [cleanup (.get position-cleanup-fns el)]
    (cleanup)
    (.delete position-cleanup-fns el))
  (set-component-state! el {:position-cleanup nil}))

(defn close-dropdown!
  "Close dropdown and reset state"
  [^js el ^js shadow-root]
  (let [state (set-component-state! el {:open false
                                        :highlighted-index -1
                                        :current-placement nil})
        options-container (.querySelector shadow-root ".dropdown-options")
        chevron (.querySelector shadow-root ".dropdown-chevron")]

    ;; Unregister from global registry
    (unregister-dropdown! el)

    ;; Cleanup position tracking
    (cleanup-position-tracking! el)

    (when options-container
      (.remove (.-classList options-container) "open")
      ;; Reset positioning styles
      (set! (.. options-container -style -position) "")
      (set! (.. options-container -style -top) "")
      (set! (.. options-container -style -left) "")
      (set! (.. options-container -style -width) ""))

    (when chevron
      (.remove (.-classList chevron) "open"))

    ;; Reset search if not searchable
    (let [{:keys [searchable]} (dropdown-attributes el)]
      (when-not searchable
        (set-component-state! el {:search ""})))))

(defn open-dropdown!
  "Open dropdown with smart positioning and global management"
  [^js el ^js shadow-root]
  (let [{:keys [disabled readonly searchable value]} (dropdown-attributes el)]
    (when-not (or disabled readonly)
      ;; Close all other dropdowns first
      (close-other-dropdowns! el)

      ;; Register this dropdown as open
      (register-dropdown! el)

      ;; Initialize filtered options for keyboard navigation
      (let [all-options (map get-option-data (get-options shadow-root))
            current-search (if searchable (:search (get-component-state el)) "")
            filtered (if searchable
                       (filter-options all-options current-search)
                       all-options)
            ;; Find the index of the currently selected option for smart keyboard navigation
            selected-index (if (and value (seq filtered))
                             (first (keep-indexed
                                      (fn [idx option]
                                        (when (= (:value option) value) idx))
                                      filtered))
                             -1)]

        (set-component-state! el {:open true
                                  :filtered-options filtered
                                  :highlighted-index selected-index}))

      (let [options-container (.querySelector shadow-root ".dropdown-options")
            chevron (.querySelector shadow-root ".dropdown-chevron")]

        ;; For non-searchable dropdowns, ensure all options are visible
        (when-not searchable
          (let [options (map get-option-data (get-options shadow-root))]
            (update-option-visibility! options options)))

        (when options-container
          ;; Hide the dropdown initially to calculate position without visible movement
          (set! (.. options-container -style -visibility) "hidden")
          (set! (.. options-container -style -opacity) "0")
          (.add (.-classList options-container) "open"))

        (when chevron
          (.add (.-classList chevron) "open"))

        ;; Calculate and apply position while hidden, then make visible
        (js/setTimeout
          (fn []
            (when options-container
              ;; Calculate position while hidden
              (update-dropdown-position! el shadow-root)
              (setup-position-tracking! el shadow-root)

              ;; Now make visible with smooth transition
              (set! (.. options-container -style -visibility) "visible")
              (set! (.. options-container -style -opacity) "")

              ;; Highlight the selected option if any, and scroll to it
              (let [{:keys [highlighted-index filtered-options]} (get-component-state el)]
                (when (>= highlighted-index 0)
                  (highlight-option! filtered-options highlighted-index shadow-root))
                (scroll-to-selected-option! el shadow-root))))
          16))))) ; Wait for CSS transition to start

(defn handle-input-click!
  "Handle click on input to toggle dropdown"
  [^js el ^js shadow-root ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (let [{:keys [open]} (get-component-state el)]
    (if open
      (close-dropdown! el shadow-root)
      (open-dropdown! el shadow-root))))

(defn handle-input-change!
  "Handle input value change for search"
  [^js el ^js shadow-root ^js event]
  (let [{:keys [searchable]} (dropdown-attributes el)]
    ;; Only process search if dropdown is searchable
    (when searchable
      (let [search (.-value (.-target event))
            options (map get-option-data (get-options shadow-root))
            filtered (filter-options options search)
            ;; Keep the selected option highlighted if it's still in filtered results
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

        ;; Highlight the option if there's a valid index
        (when (>= new-highlighted-index 0)
          (highlight-option! filtered new-highlighted-index shadow-root))

        ;; Update position after filtering (height might change)
        (js/setTimeout #(update-dropdown-position! el shadow-root) 16)))))

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
    ;; Update input display
    (let [input (.querySelector shadow-root ".dropdown-input")]
      (set! (.-value input) text))
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
      ;; ENTER - select highlighted option or toggle
      13 (do
           (.preventDefault event)
           (if open
             (when (and (>= highlighted-index 0) (< highlighted-index (count filtered-options)))
               (let [{:keys [value text]} (nth filtered-options highlighted-index)]
                 (.setAttribute el "value" value)
                 (let [options (map get-option-data (get-options shadow-root))]
                   (select-option! options value))
                 (let [input (.querySelector shadow-root ".dropdown-input")]
                   (set! (.-value input) text))
                 (dispatch-change-event! el value text)
                 (close-dropdown! el shadow-root)))
             (open-dropdown! el shadow-root)))
      ;; UP ARROW
      38 (when open
           (.preventDefault event)
           (let [new-index (if (= highlighted-index -1)
                             ;; If no selection, start from last option when going up
                             (dec (count filtered-options))
                             ;; Otherwise, move up (with -1 as minimum for no selection)
                             (max -1 (dec highlighted-index)))]
             (set-component-state! el {:highlighted-index new-index})
             (highlight-option! filtered-options new-index shadow-root)))
      ;; DOWN ARROW  
      40 (when open
           (.preventDefault event)
           (let [new-index (if (= highlighted-index -1)
                             ;; If no selection, start from first option when going down
                             0
                             ;; Otherwise, move down (with max as last option)
                             (min (dec (count filtered-options)) (inc highlighted-index)))]
             (set-component-state! el {:highlighted-index new-index})
             (highlight-option! filtered-options new-index shadow-root)))
      ;; Default - open dropdown for alphanumeric
      (when-not open
        (open-dropdown! el shadow-root)))))

(defn handle-outside-click!
  "Handle clicks outside dropdown to close it"
  [^js el ^js shadow-root ^js event]
  (let [{:keys [open]} (get-component-state el)]
    (when open
      (let [path (or (.composedPath event) [])]
        (when-not (some #(= % el) path)
          (close-dropdown! el shadow-root))))))

(defn setup-event-listeners!
  "Setup all event listeners"
  [^js el ^js shadow-root]
  (let [input (.querySelector shadow-root ".dropdown-input")
        slot (.querySelector shadow-root "slot")
        outside-handler (partial handle-outside-click! el shadow-root)]

    ;; Input events
    (when input
      (.addEventListener input "click" (partial handle-input-click! el shadow-root))
      (.addEventListener input "input" (partial handle-input-change! el shadow-root))
      (.addEventListener input "keydown" (partial handle-keyboard! el shadow-root)))

    ;; Option clicks via event delegation on slot
    (when slot
      (.addEventListener slot "click" (partial handle-option-click! el shadow-root)))

    ;; Outside clicks
    (.addEventListener js/document "click" outside-handler)

    ;; Store cleanup function
    (set! (.-tyDropdownCleanup el)
          (fn []
            (when input
              (.removeEventListener input "click" (partial handle-input-click! el shadow-root))
              (.removeEventListener input "input" (partial handle-input-change! el shadow-root))
              (.removeEventListener input "keydown" (partial handle-keyboard! el shadow-root)))
            (when slot
              (.removeEventListener slot "click" (partial handle-option-click! el shadow-root)))
            (.removeEventListener js/document "click" outside-handler)
            ;; Cleanup positioning
            (cleanup-position-tracking! el)
            ;; Unregister from global registry
            (unregister-dropdown! el)))))

(defn update-input-display!
  "Update input field to show selected value"
  [^js el ^js shadow-root]
  (let [{:keys [value]} (dropdown-attributes el)
        input (.querySelector shadow-root ".dropdown-input")
        options (map get-option-data (get-options shadow-root))]
    (when input
      (if-let [selected-option (first (filter #(= (:value %) value) options))]
        (do
          (set! (.-value input) (:text selected-option))
          (select-option! options value))
        (set! (.-value input) "")))))

(defn render! [^js el]
  (let [root (wcs/ensure-shadow el)
        {:keys [placeholder searchable disabled readonly]} (dropdown-attributes el)]

    ;; Ensure styles are loaded
    (ensure-styles! root dropdown-styles "ty-dropdown")

    ;; Create structure if it doesn't exist
    (when-not (.querySelector root ".dropdown-input")
      (set! (.-innerHTML root)
            (str "<div class=\"dropdown-container\">"
                 "  <input class=\"dropdown-input\" type=\"text\" "
                 "         placeholder=\"" placeholder "\" "
                 (when disabled "disabled ")
                 (when readonly "readonly ")
                 (when-not searchable "readonly ")
                 ">"
                 "  <div class=\"dropdown-chevron\">"
                 "    <svg viewBox=\"0 0 20 20\" fill=\"currentColor\">"
                 "      <path fill-rule=\"evenodd\" d=\"M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z\" clip-rule=\"evenodd\" />"
                 "    </svg>"
                 "  </div>"
                 "  <div class=\"dropdown-options\">"
                 "    <slot></slot>"
                 "  </div>"
                 "</div>"))

      ;; Setup event listeners
      (setup-event-listeners! el root))

    ;; Update input display to match current value
    (update-input-display! el root)

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
  {:observed [:value :placeholder :searchable :disabled :readonly :size :flavor :placement :auto-placement :offset]
   :connected render!
   :disconnected cleanup!
   :attr (fn [^js el attr-name _old new]
           ;; Re-render on attribute changes
           (render! el))})
