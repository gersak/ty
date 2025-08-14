(ns ty.components.tooltip
  "Tooltip component - shows helpful text on hover.
   Simple implementation without portal complexity.
   
   Usage:
   <button>
     Save Changes
     <ty-tooltip content=\"Save your work\"></ty-tooltip>
   </button>"
  (:require [ty.css :refer [ensure-styles!]]
            [ty.positioning :as pos]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load tooltip styles from tooltip.css
#_{:clj-kondo/ignore [:uninitialized-var]}
(defstyles tooltip-styles)

;; Component state
(defn init-state! [^js el]
  (set! (.-_tyTooltipState el)
        #js {:open false
             :showTimeout nil
             :hideTimeout nil
             :cleanup nil
             :targetEl nil
             :isHovering false}))

(defn ^js get-state [^js el]
  (or (.-_tyTooltipState el)
      (init-state! el)))

(defn tooltip-attributes [^js el]
  {:content (wcs/attr el "content")
   :position (or (wcs/attr el "position") "top")
   :delay (or (wcs/parse-int-attr el "delay") 500)
   :offset (or (wcs/parse-int-attr el "offset") 8)
   :disabled (wcs/parse-bool-attr el "disabled")})

(defn show-tooltip! [^js el]
  (let [state (get-state el)
        target (.-targetEl state)]
    (when (and target (not (wcs/parse-bool-attr el "disabled")))
      (set! (.-open state) true)

      ;; Show the popup that's in the body
      (when-let [popup (.-_tooltipPopup el)]
        (.setAttribute popup "data-visible" "true")
        (set! (.-style.display popup) "block")

        ;; Get the inner content div for positioning
        (when-let [content-div (.-firstChild popup)]
          ;; Start position tracking
          (let [{:keys [position offset]} (tooltip-attributes el)
                ;; Get preferences based on position
                preferences (case position
                              "top" [:top :bottom :left :right]
                              "bottom" [:bottom :top :left :right]
                              "left" [:left :right :top :bottom]
                              "right" [:right :left :top :bottom]
                              ;; Support all position variants
                              "top-start" [:top-start :top-end :bottom-start :bottom-end]
                              "top-end" [:top-end :top-start :bottom-end :bottom-start]
                              "bottom-start" [:bottom-start :bottom-end :top-start :top-end]
                              "bottom-end" [:bottom-end :bottom-start :top-end :top-start]
                              "left-start" [:left-start :left-end :right-start :right-end]
                              "left-end" [:left-end :left-start :right-end :right-start]
                              "right-start" [:right-start :right-end :left-start :left-end]
                              "right-end" [:right-end :right-start :left-end :left-start]
                              ;; Default fallback
                              [:top :bottom :left :right])

                ;; Track last position to avoid unnecessary updates
                last-position (volatile! nil)

                ;; Create update function
                update-fn (fn [{:keys [x y placement]
                                :as position-data}]
                            ;; Only update if position has actually changed
                            (when (not= @last-position position-data)
                              (vreset! last-position position-data)
                              ;; Remove the transform and set position directly
                              (set! (.-style.transform content-div) "none")
                              (set! (.-style.left content-div) (str x "px"))
                              (set! (.-style.top content-div) (str y "px"))
                              (.setAttribute popup "data-placement" (name placement))))]

            ;; Setup auto-update with position tracking
            ;; Use the content div as the floating element
            (set! (.-cleanup state)
                  (pos/auto-update target content-div update-fn))

            ;; Do initial positioning
            (let [position-data (pos/find-best-position
                                  {:target-el target
                                   :floating-el content-div
                                   :preferences preferences
                                   :offset offset})]
              (update-fn position-data))))))))

(defn hide-tooltip! [^js el]
  (let [state (get-state el)]
    (set! (.-open state) false)

    ;; Hide the popup that's in the body
    (when-let [popup (.-_tooltipPopup el)]
      (.removeAttribute popup "data-visible")
      (set! (.-style.display popup) "none"))

    ;; Stop position tracking
    (when-let [cleanup (.-cleanup state)]
      (cleanup)
      (set! (.-cleanup state) nil))))

(defn clear-timeouts! [^js state]
  (when-let [timeout (.-showTimeout state)]
    (js/clearTimeout timeout)
    (set! (.-showTimeout state) nil))
  (when-let [timeout (.-hideTimeout state)]
    (js/clearTimeout timeout)
    (set! (.-hideTimeout state) nil)))

(defn schedule-show! [^js el]
  (let [state (get-state el)
        {:keys [delay]} (tooltip-attributes el)]
    (clear-timeouts! state)
    (set! (.-showTimeout state)
          (js/setTimeout #(show-tooltip! el) delay))))

(defn schedule-hide! [^js el]
  (let [state (get-state el)]
    (clear-timeouts! state)
    (set! (.-hideTimeout state)
          (js/setTimeout #(hide-tooltip! el) 100))))

(defn setup-target! [^js el ^js target]
  (let [state (get-state el)]
    ;; Store target reference
    (set! (.-targetEl state) target)

    ;; Event handlers
    (let [handle-enter (fn [e]
                         (.stopPropagation e)
                         (set! (.-isHovering state) true)
                         (schedule-show! el))
          handle-leave (fn [e]
                         (.stopPropagation e)
                         (set! (.-isHovering state) false)
                         (schedule-hide! el))
          handle-focus (fn []
                         (schedule-show! el))
          handle-blur (fn []
                        (when-not (.-isHovering state)
                          (schedule-hide! el)))]

      ;; Add listeners to target
      (.addEventListener target "mouseenter" handle-enter)
      (.addEventListener target "mouseleave" handle-leave)
      (.addEventListener target "focus" handle-focus)
      (.addEventListener target "blur" handle-blur)

      ;; Store cleanup function
      (set! (.-cleanupTarget state)
            (fn []
              (.removeEventListener target "mouseenter" handle-enter)
              (.removeEventListener target "mouseleave" handle-leave)
              (.removeEventListener target "focus" handle-focus)
              (.removeEventListener target "blur" handle-blur))))))

(defn render! [^js el]
  (let [{:keys [content]} (tooltip-attributes el)]
    ;; Store a reference to the tooltip content on the element
    (set! (.-_tooltipContent el) (or content "NO CONTENT"))

    ;; Create a unique ID for this tooltip
    (let [tooltip-id (str "ty-tooltip-" (str (random-uuid)))]
      (set! (.-_tooltipId el) tooltip-id)

      ;; Remove any existing popup
      (when-let [existing (.getElementById js/document tooltip-id)]
        (.removeChild (.-parentNode existing) existing))

      ;; Create and append the popup to body
      (let [popup (.createElement js/document "div")]
        (set! (.-id popup) tooltip-id)
        (set! (.-className popup) "ty-tooltip-popup")
        (.setAttribute popup "data-tooltip-for" "")
        ;; Initially hide the popup
        (set! (.-style.display popup) "none")
        (set! (.-innerHTML popup)
              (str "<div style='
                      position: fixed;
                      left: 0;
                      top: 0;
                      background: #333;
                      color: white;
                      padding: 10px 15px;
                      border-radius: 4px;
                      font-size: 14px;
                      z-index: 999999;
                      box-shadow: 0 2px 10px rgba(0,0,0,0.2);
                      white-space: nowrap;
                      max-width: 250px;
                    '>"
                   (.-_tooltipContent el)
                   "</div>"))

        ;; Append to body
        (.appendChild js/document.body popup)

        ;; Store reference
        (set! (.-_tooltipPopup el) popup)))))

(defn update-content! [^js el]
  (let [{:keys [content]} (tooltip-attributes el)]
    (when-let [text-el (.querySelector el ".tooltip-text")]
      (set! (.-innerHTML text-el) (or content "")))))

(defn cleanup! [^js el]
  (let [state (get-state el)]
    ;; Clear timeouts
    (clear-timeouts! state)

    ;; Stop position tracking
    (when-let [cleanup (.-cleanup state)]
      (cleanup))

    ;; Remove target listeners
    (when-let [cleanup-target (.-cleanupTarget state)]
      (cleanup-target))

    ;; Remove popup from body
    (when-let [popup (.-_tooltipPopup el)]
      (when (.-parentNode popup)
        (.removeChild (.-parentNode popup) popup)))))

(wcs/define! "ty-tooltip"
  {:observed [:content :position :delay :offset :disabled]
   :connected (fn [^js el]
                (render! el)
                ;; Find parent as target
                (when-let [parent (.-parentElement el)]
                  (setup-target! el parent)))
   :disconnected cleanup!
   :attr (fn [^js el attr-name _old _new]
           (case attr-name
             "content" (update-content! el)
             "disabled" (when _new (hide-tooltip! el))
             nil))})
