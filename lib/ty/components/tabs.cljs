(ns ty.components.tabs
  "Tabs container component - orchestrates tab buttons and panels with carousel animation"
  (:require [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load tabs styles
(defstyles tabs-styles)

;; =====================================================
;; Tabs Attributes & State
;; =====================================================

(defn tabs-attributes
  "Extract tabs configuration from element attributes"
  [^js el]
  {:width (or (wcs/attr el "width") "100%")
   :height (or (wcs/attr el "height") "400px")
   :active (wcs/attr el "active") ; ID of active tab
   :placement (or (wcs/attr el "placement") "top")})

(defn get-child-tabs
  "Get all ty-tab child elements"
  [^js el]
  (vec (array-seq (.querySelectorAll el "ty-tab"))))

(defn get-tab-id
  "Get ID from a ty-tab element"
  [^js tab-el]
  (.getAttribute tab-el "id"))

(defn has-slot-label?
  "Check if ty-tabs has a direct child label slot for this tab-id.
   Looks in ty-tabs' light DOM for slot='label-{tab-id}' elements."
  [^js tabs-el tab-id]
  (some? (.querySelector tabs-el (str "[slot='label-" tab-id "']"))))

(defn get-tab-label-type
  "Determine label type: :slot (rich content) or :text (simple attribute).
   Checks ty-tabs element for slot='label-{tab-id}' direct children."
  [^js tabs-el ^js tab-el]
  (let [tab-id (get-tab-id tab-el)]
    (if (has-slot-label? tabs-el tab-id)
      :slot
      :text)))

(defn is-tab-disabled?
  "Check if tab is disabled"
  [^js tab-el]
  (.hasAttribute tab-el "disabled"))

;; =====================================================
;; Active Tab Management  
;; =====================================================

(defn find-tab-index
  "Find index of tab with given ID"
  [tabs tab-id]
  (first (keep-indexed (fn [idx tab]
                         (when (= (get-tab-id tab) tab-id) idx))
                       tabs)))

(defn get-active-tab-id
  "Get the active tab ID, defaulting to first tab if not specified"
  [^js el tabs]
  (let [active-attr (wcs/attr el "active")]
    (if (and active-attr (find-tab-index tabs active-attr))
      active-attr
      ;; Default to first tab
      (when (seq tabs)
        (get-tab-id (first tabs))))))

(defn set-active-tab!
  "Set the active tab by ID"
  [^js el tab-id]
  (.setAttribute el "active" tab-id))

;; =====================================================
;; Event Dispatching
;; =====================================================

(defn dispatch-tab-change-event!
  "Dispatch ty-tab-change event"
  [^js el active-id active-index previous-id previous-index]
  (let [event (js/CustomEvent. "ty-tab-change"
                               #js {:detail #js {:activeId active-id
                                                 :activeIndex active-index
                                                 :previousId previous-id
                                                 :previousIndex previous-index}
                                    :bubbles true
                                    :cancelable false})]
    (.dispatchEvent el event)))

;; =====================================================
;; Tab Button Click Handler
;; =====================================================

(defn handle-tab-click!
  "Handle tab button click"
  [^js el tab-id ^js event]
  (.preventDefault event)
  (.stopPropagation event)

;; Set the active attribute (which will trigger state update)
  (set-active-tab! el tab-id))

;; =====================================================
;; Event Listeners Cleanup
;; =====================================================

(defn cleanup-event-listeners!
  "Clean up existing event listeners"
  [^js el]
  (when-let [cleanup-fn (.-tyTabsCleanup el)]
    (cleanup-fn)
    (set! (.-tyTabsCleanup el) nil)))

(defn setup-event-listeners!
  "Setup event listeners for tab button clicks"
  [^js el ^js shadow-root tabs]
  ;; Clean up any existing listeners first
  (cleanup-event-listeners! el)

  (let [listeners (atom [])]
    ;; Add click listener for each tab button
    (doseq [[idx tab] (map-indexed vector tabs)]
      (let [tab-id (get-tab-id tab)
            button (.querySelector shadow-root (str "[data-tab-id='" tab-id "']"))]
        (when button
          (let [handler (partial handle-tab-click! el tab-id)]
            (.addEventListener button "pointerdown" handler)
            (swap! listeners conj {:element button
                                   :type "pointerdown"
                                   :handler handler})))))

    ;; Store cleanup function
    (set! (.-tyTabsCleanup el)
          (fn []
            (doseq [{:keys [element type handler]} @listeners]
              (.removeEventListener element type handler))))))

;; =====================================================
;; ResizeObserver for Responsive Width
;; =====================================================

;; =====================================================
;; Transform Update
;; =====================================================

(defn update-transform!
  "Update the transform on panels-wrapper based on active index and measured width"
  [^js el active-index]
  (let [shadow-root (.-shadowRoot el)
        panels-wrapper (when shadow-root (.querySelector shadow-root ".panels-wrapper"))]
    (when panels-wrapper
      ;; Measure the actual width of the container
      (let [container-width (.-offsetWidth el)
            offset-px (* active-index container-width)]
        ;; Apply transform directly in pixels
        (.. panels-wrapper -style (setProperty "transform" (str "translateX(-" offset-px "px)")))))))

(defn update-aria-attributes!
  "Update ARIA attributes on tab buttons without re-rendering"
  [^js el ^js shadow-root active-id]
  (when shadow-root
    (let [tabs (get-child-tabs el)]
      (doseq [[idx tab] (map-indexed vector tabs)]
        (let [tab-id (get-tab-id tab)
              button (.querySelector shadow-root (str "[data-tab-id='" tab-id "']"))
              is-active? (= tab-id active-id)]
          (when button
            (.setAttribute button "aria-selected" (str is-active?))
            (.setAttribute button "tabindex" (if is-active? "0" "-1"))))))))

(defn update-panel-interaction!
  "Update pointer-events and opacity on tab panels without re-rendering"
  [^js el active-id]
  (let [tabs (get-child-tabs el)]
    (doseq [tab tabs]
      (let [tab-id (get-tab-id tab)
            is-active? (= tab-id active-id)]
        (if is-active?
          (do
            (.. tab -style (setProperty "pointer-events" "auto"))
            (.. tab -style (setProperty "opacity" "1")))
          (do
            (.. tab -style (setProperty "pointer-events" "none"))
            (.. tab -style (setProperty "opacity" "0"))))))))

(defn update-active-tab-state!
  "Update only the active tab state without re-rendering DOM.
   This is called when only the active attribute changes."
  [^js el tab-id]
  (let [tabs (get-child-tabs el)
        shadow-root (.-shadowRoot el)
        current-active (get-active-tab-id el tabs)
        current-index (find-tab-index tabs current-active)
        new-index (find-tab-index tabs tab-id)]

    ;; Only update if different tab and valid
    (when (and (not= current-active tab-id)
               (some? new-index))

      ;; Update CSS variable for transform
      (.. el -style (setProperty "--active-index" (str new-index)))

      ;; Update transform directly
      (update-transform! el new-index)

      ;; Update ARIA attributes on buttons
      (update-aria-attributes! el shadow-root tab-id)

      ;; Update pointer-events on panels
      (update-panel-interaction! el tab-id)

      ;; Reset scroll position of new active panel
      (when-let [new-panel (nth tabs new-index nil)]
        (let [panel-shadow (.-shadowRoot new-panel)
              panel-div (when panel-shadow (.querySelector panel-shadow ".tab-panel"))]
          (when panel-div
            (set! (.-scrollTop panel-div) 0))))

      ;; Dispatch change event
      (dispatch-tab-change-event! el tab-id new-index current-active current-index))))

;; =====================================================
;; ResizeObserver for Responsive Width
;; =====================================================

(defn setup-resize-observer!
  "Setup ResizeObserver for percentage widths"
  [^js el]
  (when-let [old-observer (.-tyTabsResizeObserver el)]
    (.disconnect old-observer))

  (let [{:keys [width]} (tabs-attributes el)]
    (when (and width (.includes width "%"))
      (let [observer (js/ResizeObserver.
                       (fn [entries]
                         (let [entry (aget entries 0)
                               content-rect (.-contentRect entry)
                               measured-width (.-width content-rect)
                               tabs (get-child-tabs el)
                               active-id (get-active-tab-id el tabs)
                               active-index (or (find-tab-index tabs active-id) 0)]
                          ;; Update CSS variable with measured width
                           (.. el -style (setProperty "--tabs-width" (str measured-width "px")))
                          ;; Update transform with new width
                           (update-transform! el active-index))))]
        (.observe observer el)
        (set! (.-tyTabsResizeObserver el) observer)))))

(defn cleanup-resize-observer!
  "Cleanup ResizeObserver"
  [^js el]
  (when-let [observer (.-tyTabsResizeObserver el)]
    (.disconnect observer)
    (set! (.-tyTabsResizeObserver el) nil)))

;; =====================================================
;; Rendering
;; =====================================================

(defn render-tab-buttons
  "Generate HTML for tab buttons using slots for rich labels.
   Slots are looked up as direct children of tabs-el with slot='label-{tab-id}'."
  [tabs-el tabs active-id]
  (str "<div class=\"tab-buttons\" role=\"tablist\">"
       (apply str
              (for [[idx tab] (map-indexed vector tabs)]
                (let [tab-id (get-tab-id tab)
                      label-type (get-tab-label-type tabs-el tab)
                      text-label (when (= label-type :text)
                                   (or (.getAttribute tab "label") "Tab"))
                      disabled? (is-tab-disabled? tab)
                      active? (= tab-id active-id)]
                  (str "<button "
                       "  class=\"tab-button\""
                       "  role=\"tab\""
                       "  data-tab-id=\"" tab-id "\""
                       "  id=\"tab-" tab-id "\""
                       "  aria-controls=\"panel-" tab-id "\""
                       "  aria-selected=\"" (if active? "true" "false") "\""
                       "  tabindex=\"" (if active? "0" "-1") "\""
                       (when disabled? " disabled aria-disabled=\"true\"")
                       ">"
                       ;; Use slot for rich labels, text for simple labels
                       (if (= label-type :slot)
                         (str "<slot name=\"label-" tab-id "\"></slot>")
                         text-label)
                       "</button>"))))
       "</div>"))

(defn render!
  "Render the tabs container with buttons and panel viewport.
   Smart rendering: checks if structure exists and only updates when needed."
  [^js el]
  (let [root (wcs/ensure-shadow el)
        {:keys [width height placement]} (tabs-attributes el)
        tabs (get-child-tabs el)
        active-id (get-active-tab-id el tabs)
        active-index (or (find-tab-index tabs active-id) 0)
        ;; Check if structure already exists
        existing-container (.querySelector root ".tabs-container")
        existing-buttons (.querySelector root ".tab-buttons")
        existing-viewport (.querySelector root ".panels-viewport")]

    ;; Assign unique slot names to label elements (must happen before rendering buttons)

    ;; Ensure styles are loaded
    (ensure-styles! root tabs-styles "ty-tabs")

    ;; Dev warning for too many tabs
    (when (> (count tabs) 7)
      (js/console.warn
        (str "[ty-tabs] More than 7 tabs detected (" (count tabs) " tabs). "
             "This may cause overflow and poor UX. "
             "Consider using sidebar navigation, accordion menu, or other patterns. "
             "See: https://docs.claude.com/components/tabs#alternatives")))

    ;; Set CSS variables for dimensions
    (.. el -style (setProperty "--tabs-width" (if (.includes width "%") "100%" width)))
    (.. el -style (setProperty "--tabs-height" height))
    (.. el -style (setProperty "--active-index" (str active-index)))

    (if (and existing-container existing-buttons existing-viewport)
      ;; === SMART UPDATE: Structure exists, only update what changed ===
      (do
        ;; Update placement if changed
        (.setAttribute existing-container "data-placement" placement)

        ;; Update tab buttons (regenerate with new slot names)
        (set! (.-innerHTML existing-buttons)
              (apply str
                     (for [[idx tab] (map-indexed vector tabs)]
                       (let [tab-id (get-tab-id tab)
                             label-type (get-tab-label-type el tab)
                             text-label (when (= label-type :text)
                                          (or (.getAttribute tab "label") "Tab"))
                             disabled? (is-tab-disabled? tab)
                             active? (= tab-id active-id)]
                         (str "<button "
                              "  class=\"tab-button\""
                              "  role=\"tab\""
                              "  data-tab-id=\"" tab-id "\""
                              "  id=\"tab-" tab-id "\""
                              "  aria-controls=\"panel-" tab-id "\""
                              "  aria-selected=\"" (if active? "true" "false") "\""
                              "  tabindex=\"" (if active? "0" "-1") "\""
                              (when disabled? " disabled aria-disabled=\"true\"")
                              ">"
                              ;; Use slot for rich labels, text for simple labels
                              (if (= label-type :slot)
                                (str "<slot name=\"label-" tab-id "\"></slot>")
                                text-label)
                              "</button>")))))

        ;; Re-setup event listeners (buttons were recreated)
        (setup-event-listeners! el root tabs)

        ;; Measure button height after update
        (js/requestAnimationFrame
          (fn []
            (when-let [buttons (.querySelector root ".tab-buttons")]
              (let [buttons-height (.-offsetHeight buttons)]
                (.. el -style (setProperty "--buttons-height" (str buttons-height "px")))))
           ;; Update transform with current active index
            (update-transform! el active-index)))

        ;; Update panel interaction states
        (update-panel-interaction! el active-id))

      ;; === FULL RENDER: First time or structure missing ===
      (do
        ;; Render full structure
        (set! (.-innerHTML root)
              (str "<div class=\"tabs-container\" data-placement=\"" placement "\">"
                   (render-tab-buttons el tabs active-id)
                   "  <div class=\"panels-viewport\">"
                   "    <div class=\"panels-wrapper\">"
                   "      <slot></slot>" ; Light DOM ty-tab elements go here
                   "    </div>"
                   "  </div>"
                   "</div>"))

        ;; Measure button height and update transform after render
        (js/requestAnimationFrame
          (fn []
            (when-let [buttons (.querySelector root ".tab-buttons")]
              (let [buttons-height (.-offsetHeight buttons)]
                (.. el -style (setProperty "--buttons-height" (str buttons-height "px")))))
           ;; Update transform with measured width
            (update-transform! el active-index)))

        ;; Setup event listeners
        (setup-event-listeners! el root tabs)

        ;; Setup ResizeObserver for responsive width
        (setup-resize-observer! el)

        ;; Update tab panel states - use pointer-events instead of aria-hidden for visibility
        (update-panel-interaction! el active-id)))))

(defn cleanup!
  "Cleanup when tabs component is disconnected"
  [^js el]
  (cleanup-event-listeners! el)
  (cleanup-resize-observer! el))

(def configuration
  {:observed [:width :height :active :placement :slot]
   :props {:active nil} ; Enable property watching for programmatic control
   :connected render!
   :disconnected cleanup!
   :attr (fn [^js el delta]
           ;; Smart rendering: only full render when structural attributes change
           (if (contains? delta "active")
             ;; Active tab changed - just update state, no re-render
             (when-let [tab-id (get delta "active")]
               (update-active-tab-state! el tab-id)
               (render! el))
             ;; Other attributes changed (width, height, placement) - full render
             (render! el)))
   :prop (fn [^js el delta]
           ;; Sync property changes to attributes
           (when (contains? delta "active")
             (when-let [active (get delta "active")]
               (.setAttribute el "active" active)))
           ;; Smart rendering: only full render when structural properties change
           (if (contains? delta "active")
             ;; Active tab changed - state update handled by :attr callback
             nil
             ;; Other properties changed - full render
             (render! el)))})
