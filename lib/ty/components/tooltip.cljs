(ns ty.components.tooltip
  "Tooltip component - shows helpful content on hover.
   Follows the same shadow DOM pattern as popup but triggers on hover/focus."
  (:require [ty.css :refer [ensure-styles!]]
            [ty.positioning :as pos]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load tooltip styles
(defstyles tooltip-styles)

;; =====================================================
;; Semantic Flavor Normalization
;; =====================================================

(defn validate-flavor
  "Validate that flavor uses new industry-standard semantic naming or standard tooltip flavors.
   For tooltips, flavor indicates semantic meaning and visual styling."
  [flavor]
  (let [valid-flavors #{"dark" "light" "primary" "secondary" "success" "danger" "warning" "info" "neutral"}
        normalized (or flavor "dark")]
    (when (and goog.DEBUG (not (contains? valid-flavors normalized)))
      (js/console.warn (str "[ty-tooltip] Invalid flavor '" flavor "'. Using 'dark'. "
                            "Valid flavors: dark, light, primary, secondary, success, danger, warning, info, neutral.")))
    (if (contains? valid-flavors normalized)
      normalized
      "dark")))

;; Store cleanup functions for auto-update
(defonce auto-update-cleanup-fns (js/WeakMap.))

;; Store event cleanup functions
(defonce event-cleanup-fns (js/WeakMap.))

;; Store timeouts for show/hide delays
(defonce timeout-state (js/WeakMap.))

(defn get-timeout-state [^js el]
  (or (.get timeout-state el)
      (let [state #js {:showTimeout nil
                       :hideTimeout nil}]
        (.set timeout-state el state)
        state)))

(defn tooltip-attributes
  "Read all tooltip attributes directly from element.
   Only accepts new industry-standard semantic flavors."
  [^js el]
  (let [raw-flavor (wcs/attr el "flavor")]
    {:placement (or (wcs/attr el "placement") "top")
     :offset (or (wcs/parse-int-attr el "offset") 8)
     :delay (or (wcs/parse-int-attr el "delay") 600)
     :disabled (wcs/parse-bool-attr el "disabled")
     :flavor (validate-flavor raw-flavor)
     ;; Internal state - not a real attribute
     :open (.-_open el)}))

(defn get-anchor-element
  "Get the parent element as anchor"
  [^js el]
  (.-parentElement el))

(defn get-tooltip-container
  "Get the tooltip container element"
  [^js shadow-root]
  (.querySelector shadow-root "#tooltip-container"))

(defn update-position!
  "Calculate and update tooltip position based on anchor"
  [^js el ^js shadow-root]
  (let [{:keys [placement offset]} (tooltip-attributes el)
        anchor (get-anchor-element el)
        container (get-tooltip-container shadow-root)]
    (when (and anchor container)
      ;; Calculate preferred placements based on placement attribute
      (let [preferences (case placement
                          "top" [:top :bottom :left :right]
                          "bottom" [:bottom :top :left :right]
                          "left" [:left :right :top :bottom]
                          "right" [:right :left :top :bottom]
                          ;; Default tooltip placement
                          [:top :bottom :right :left])
            ;; Use positioning engine to find best position
            position-data (pos/find-best-position
                            {:target-el anchor
                             :floating-el container
                             :preferences preferences
                             :offset offset
                             :padding 8})
            {:keys [x y]} position-data]
        ;; Update CSS variables
        (.setProperty (.-style el) "--x" (str x "px"))
        (.setProperty (.-style el) "--y" (str y "px"))))))

(defn cleanup-auto-update!
  "Clean up all observers and listeners for auto-update"
  [^js el]
  (when-let [cleanup-fn (.get auto-update-cleanup-fns el)]
    (cleanup-fn)
    (.delete auto-update-cleanup-fns el)))

(defn setup-auto-update!
  "Setup observers and listeners for auto-updating position"
  [^js el ^js shadow-root]
  (let [anchor (get-anchor-element el)
        container (get-tooltip-container shadow-root)
        ;; Debounced update function
        update-fn (let [timeout-id (atom nil)]
                    (fn []
                      (when @timeout-id
                        (js/clearTimeout @timeout-id))
                      (reset! timeout-id
                              (js/setTimeout
                                #(do
                                   (reset! timeout-id nil)
                                   (update-position! el shadow-root))
                                10))))
        ;; ResizeObserver for anchor and container
        resize-observer (js/ResizeObserver. update-fn)
        ;; Scroll listener with requestAnimationFrame
        scroll-raf-id (atom nil)
        scroll-handler (fn []
                         (when-not @scroll-raf-id
                           (reset! scroll-raf-id
                                   (js/requestAnimationFrame
                                     #(do
                                        (reset! scroll-raf-id nil)
                                        (update-position! el shadow-root))))))
        ;; Cleanup function
        cleanup (fn []
                  (.disconnect resize-observer)
                  (js/removeEventListener "scroll" scroll-handler true)
                  (js/removeEventListener "resize" update-fn)
                  (when @scroll-raf-id
                    (js/cancelAnimationFrame @scroll-raf-id)))]

    ;; Observe anchor and container for size changes
    (when anchor
      (.observe resize-observer anchor))
    (when container
      (.observe resize-observer container))

    ;; Listen for scroll events (capture phase for better performance)
    (js/addEventListener "scroll" scroll-handler true)

    ;; Listen for window resize
    (js/addEventListener "resize" update-fn)

    ;; Store cleanup function
    (.set auto-update-cleanup-fns el cleanup)))

(defn clear-timeouts! [^js el]
  (let [state (get-timeout-state el)]
    (when-let [timeout (.-showTimeout state)]
      (js/clearTimeout timeout)
      (set! (.-showTimeout state) nil))
    (when-let [timeout (.-hideTimeout state)]
      (js/clearTimeout timeout)
      (set! (.-hideTimeout state) nil))))

(declare render!)

(defn show-tooltip! [^js el]
  (let [{:keys [disabled]} (tooltip-attributes el)]
    (when-not disabled
      (set! (.-_open el) true)
      (render! el))))

(defn hide-tooltip! [^js el]
  (set! (.-_open el) false)
  (render! el))

(defn schedule-show! [^js el]
  (let [state (get-timeout-state el)
        {:keys [delay]} (tooltip-attributes el)]
    (clear-timeouts! el)
    (set! (.-showTimeout state)
          (js/setTimeout #(show-tooltip! el) delay))))

(defn schedule-hide! [^js el]
  (let [state (get-timeout-state el)]
    (clear-timeouts! el)
    (set! (.-hideTimeout state)
          (js/setTimeout #(hide-tooltip! el) 200))))

(defn render! [^js el]
  (let [root (wcs/ensure-shadow el)
        existing-container (.querySelector root "#tooltip-container")
        {:keys [open flavor]} (tooltip-attributes el)]
    ;; Ensure styles are loaded
    (ensure-styles! root tooltip-styles "ty-tooltip")

    ;; Create structure if it doesn't exist
    (when-not existing-container
      (let [container (js/document.createElement "div")
            content (js/document.createElement "slot")]
        (set! (.-id container) "tooltip-container")
        (.setAttribute container "data-flavor" flavor)
        (.appendChild root container)
        (.appendChild container content)
        ;; Initialize position
        (.setProperty (.-style el) "--x" "0px")
        (.setProperty (.-style el) "--y" "0px")))

    ;; Update variant if it changed
    (when-let [container (.querySelector root "#tooltip-container")]
      (.setAttribute container "data-flavor" flavor)
      ;; Update visibility
      (if open
        (.add (.-classList container) "open")
        (.remove (.-classList container) "open")))

    ;; Handle open/close state
    (if open
      (do
        ;; Update position
        (update-position! el root)
        ;; Setup auto-update
        (setup-auto-update! el root))
      ;; Cleanup when closed
      (cleanup-auto-update! el))))

(defn setup-events! [^js el]
  (let [anchor (get-anchor-element el)]
    (letfn [(handle-enter [e] (schedule-show! el))
            (handle-leave [e] (schedule-hide! el))
            (handle-focus [e] (schedule-show! el))
            (handle-blur [e] (schedule-hide! el))]

      ;; Add listeners
      (.addEventListener anchor "mouseenter" handle-enter)
      (.addEventListener anchor "mouseleave" handle-leave)
      (.addEventListener anchor "focusin" handle-focus)
      (.addEventListener anchor "focusout" handle-blur)

      ;; Store event cleanup function separately
      (.set event-cleanup-fns el
            (fn []
              ;; Remove all listeners
              (.removeEventListener anchor "mouseenter" handle-enter)
              (.removeEventListener anchor "mouseleave" handle-leave)
              (.removeEventListener anchor "focusin" handle-focus)
              (.removeEventListener anchor "focusout" handle-blur))))
    (when anchor)))

(defn cleanup!
  "Clean up when tooltip is disconnected"
  [^js el]
  ;; Clear timeouts
  (clear-timeouts! el)
  ;; Clean up auto-update
  (cleanup-auto-update! el)
  ;; Clean up event listeners
  (when-let [cleanup-fn (.get event-cleanup-fns el)]
    (cleanup-fn)
    (.delete event-cleanup-fns el))
  ;; Clean up timeout state
  (.delete timeout-state el))

(wcs/define! "ty-tooltip"
  {:observed [:placement :offset :delay :disabled :flavor]
   :connected (fn [^js el]
                ;; Initialize open state
                (set! (.-_open el) false)
                ;; Set up structure
                (render! el)
                ;; Set up events
                (setup-events! el))
   :disconnected cleanup!
   :attr (fn [^js el attr-name _old new]
           ;; Update variant in real-time if tooltip is visible
           (when (and (= attr-name "flavor") (.-_open el))
             (when-let [container (.querySelector (.-shadowRoot el) "#tooltip-container")]
               (.setAttribute container "data-flavor" new)))
           ;; Close tooltip if disabled
           (when (and (= attr-name "disabled") new)
             (hide-tooltip! el)))})
