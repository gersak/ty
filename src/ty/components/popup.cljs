(ns ty.components.popup
  "Popup positioning primitive.
   Handles positioning of floating elements relative to slotted anchors."
  (:require [ty.css :refer [ensure-styles!]]
            [ty.positioning :as pos]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load popup styles
(defstyles popup-styles)

;; Store cleanup functions for each popup instance
(defonce cleanup-fns (js/WeakMap.))

(defn popup-attributes
  "Read all popup attributes directly from element"
  [^js el]
  {:open (wcs/parse-bool-attr el "open")
   :placement (or (wcs/attr el "placement") "bottom")
   :offset (or (wcs/parse-int-attr el "offset") 8)
   :flip (wcs/parse-bool-attr el "flip")})

(defn get-anchor-element
  "Get the slotted anchor element"
  [^js shadow-root]
  (when-let [anchor-slot (.querySelector shadow-root "[name=anchor]")]
    (first (.assignedElements anchor-slot))))

(defn get-popup-content
  "Get the popup content container"
  [^js shadow-root]
  (.querySelector shadow-root "#popup-container"))

(defn update-position!
  "Calculate and update popup position based on anchor"
  [^js el ^js shadow-root]
  (let [{:keys [placement offset flip]} (popup-attributes el)
        anchor (get-anchor-element shadow-root)
        popup (get-popup-content shadow-root)]
    (when (and anchor popup)
      ;; Calculate preferred placements based on placement attribute
      (let [preferences (case placement
                          "top" [:top :bottom :left :right]
                          "bottom" [:bottom :top :left :right]
                          "left" [:left :right :top :bottom]
                          "right" [:right :left :top :bottom]
                          ;; Default auto placement
                          [:bottom :top :right :left])
            ;; Use positioning engine to find best position
            position-data (pos/find-best-position
                           {:target-el anchor
                            :floating-el popup
                            :preferences preferences
                            :offset offset
                            :padding 8})
            {:keys [x y]} position-data]
        ;; Update CSS variables
        (.setProperty (.-style el) "--x" (str x "px"))
        (.setProperty (.-style el) "--y" (str y "px"))))))

(defn cleanup-auto-update!
  "Clean up all observers and listeners"
  [^js el]
  (when-let [cleanup-fn (.get cleanup-fns el)]
    (cleanup-fn)
    (.delete cleanup-fns el)))

(defn setup-auto-update!
  "Setup observers and listeners for auto-updating position"
  [^js el ^js shadow-root]
  (let [anchor (get-anchor-element shadow-root)
        popup (get-popup-content shadow-root)
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
        ;; ResizeObserver for anchor and popup
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

    ;; Observe anchor and popup for size changes
    (when anchor
      (.observe resize-observer anchor))
    (when popup
      (.observe resize-observer popup))

    ;; Listen for scroll events (capture phase for better performance)
    (js/addEventListener "scroll" scroll-handler true)

    ;; Listen for window resize
    (js/addEventListener "resize" update-fn)

    ;; Store cleanup function
    (.set cleanup-fns el cleanup)))

(defn render! [^js el]
  (let [root (wcs/ensure-shadow el)
        existing-content (.querySelector root "#popup-container")
        {:keys [open]} (popup-attributes el)]
    ;; Ensure styles are loaded
    (ensure-styles! root popup-styles "ty-popup")

    ;; Create structure if it doesn't exist
    (when-not existing-content
      (let [container (js/document.createElement "div")
            content (js/document.createElement "slot")
            anchor (js/document.createElement "slot")]
        (set! (.-id content) "popup-content")
        (set! (.-id container) "popup-container")
        (set! (.-name anchor) "anchor")
        (.appendChild root anchor)
        (.appendChild root container)
        (.appendChild container content)
        ;; Initialize position
        (.setProperty (.-style el) "--x" "0px")
        (.setProperty (.-style el) "--y" "0px")))

    ;; Update visibility
    (when-let [content (.querySelector root "#popup-container")]
      (set! (.-className content) (if open "open" "")))

    ;; Handle open/close state
    (if open
      (do
        ;; Update position
        (update-position! el root)
        ;; Setup auto-update
        (setup-auto-update! el root))
      ;; Cleanup when closed
      (cleanup-auto-update! el))))

(wcs/define! "ty-popup"
  {:observed [:open :placement :offset :flip]
   :connected render!
   :disconnected cleanup-auto-update!
   :attr (fn [^js el attr-name _old new]
           ;; Re-render on attribute changes
           (render! el)
           ;; Update position immediately if open and placement-related attributes change
           (when (and (wcs/parse-bool-attr el "open")
                      (contains? #{"placement" "offset" "flip"} attr-name))
             (update-position! el (.-shadowRoot el))))})
