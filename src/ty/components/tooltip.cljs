(ns ty.components.tooltip
  "Tooltip component - shows helpful text on hover.
   Uses ty-popup for display with Light DOM content approach."
  (:require [ty.css :refer [ensure-styles!]]
            [ty.positioning :as pos]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load tooltip styles from tooltip.css
#_{:clj-kondo/ignore [:uninitialized-var]}
(defstyles tooltip-styles)

;; Global registry of active tooltips
(defonce active-tooltips (atom {}))

;; Component state
(defn init-state! [^js el]
  (set! (.-_tyTooltipState el)
        #js {:open false
             :showTimeout nil
             :hideTimeout nil
             :cleanup nil
             :targetEl nil
             :popupEl nil
             :uniqueId (str "ty-tooltip-" (random-uuid))}))

(defn ^js get-state [^js el]
  (or (.-_tyTooltipState el)
      (init-state! el)))

(defn tooltip-attributes [^js el]
  {:content (wcs/attr el "content")
   :position (or (wcs/attr el "position") "top")
   :delay (or (wcs/parse-int-attr el "delay") 500)
   :offset (or (wcs/parse-int-attr el "offset") 8)
   :disabled (wcs/parse-bool-attr el "disabled")
   :variant (or (wcs/attr el "variant") "dark")})

(defn create-popup! [^js tooltip-el]
  (let [state (get-state tooltip-el)
        popup-id (.-uniqueId state)
        {:keys [position variant]} (tooltip-attributes tooltip-el)
        existing (.getElementById js/document popup-id)]
    
    ;; Remove existing if found
    (when existing
      (.remove existing))
    
    ;; Create new popup element
    (let [popup (.createElement js/document "ty-popup")]
      (set! (.-id popup) popup-id)
      (.setAttribute popup "placement" position)
      (.setAttribute popup "variant" variant)
      (.setAttribute popup "has-arrow" "true")
      
      ;; Store reference
      (set! (.-popupEl state) popup)
      
      ;; Append to body but keep hidden
      (.appendChild js/document.body popup)
      
      popup)))

(defn update-popup-content! [^js tooltip-el ^js popup-el]
  (let [{:keys [content]} (tooltip-attributes tooltip-el)
        ;; Get the actual content - could be from attribute or inner HTML
        actual-content (or content (.-innerHTML tooltip-el))]
    
    ;; Set content in Light DOM with wrapper div
    (set! (.-innerHTML popup-el)
          (str "<div class='popup-content'>" actual-content "</div>"))))

(defn show-tooltip! [^js el]
  (let [state (get-state el)
        target (.-targetEl state)]
    (when (and target (not (wcs/parse-bool-attr el "disabled")))
      (set! (.-open state) true)
      
      ;; Get or create popup
      (let [popup (or (.-popupEl state) (create-popup! el))]
        
        ;; Update content
        (update-popup-content! el popup)
        
        ;; Show popup
        (.setAttribute popup "open" "true")
        
        ;; Position it
        (let [{:keys [position offset]} (tooltip-attributes el)
              preferences (case position
                            "top" [:top :bottom :left :right]
                            "bottom" [:bottom :top :left :right]
                            "left" [:left :right :top :bottom]
                            "right" [:right :left :top :bottom]
                            [:top :bottom :left :right])
              
              config {:preferences preferences
                      :offset offset
                      :padding 8}
              
              ;; Create positioning function
              update-fn (fn [{:keys [x y placement]}]
                          (set! (.-style.left popup) (str x "px"))
                          (set! (.-style.top popup) (str y "px"))
                          (.setAttribute popup "placement" (name placement)))]
          
          ;; Initial position
          (let [position-data (pos/find-best-position
                                (merge {:target-el target
                                        :floating-el popup}
                                       config))]
            (update-fn position-data))
          
          ;; Setup auto-update
          (set! (.-cleanup state)
                (pos/auto-update target popup update-fn config)))))))

(defn hide-tooltip! [^js el]
  (let [state (get-state el)]
    (set! (.-open state) false)
    
    (when-let [popup (.-popupEl state)]
      (.removeAttribute popup "open"))
    
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
                         (.preventDefault e)
                         (schedule-show! el))
          handle-leave (fn [e]
                         (.preventDefault e)
                         (schedule-hide! el))
          handle-focus (fn []
                         (schedule-show! el))
          handle-blur (fn []
                        (schedule-hide! el))]
      
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
    
    ;; Remove popup from DOM
    (when-let [popup (.-popupEl state)]
      (when (.-parentNode popup)
        (.remove popup)))))

(wcs/define! "ty-tooltip"
  {:observed [:content :position :delay :offset :disabled :variant]
   :connected (fn [^js el]
                ;; Hide the tooltip element itself
                (set! (.-style.display el) "none")
                
                ;; Find parent as target
                (when-let [parent (.-parentElement el)]
                  (setup-target! el parent)))
   :disconnected cleanup!
   :attr (fn [^js el attr-name _old _new]
           (case attr-name
             "content" (when-let [popup (.-popupEl (get-state el))]
                         (when (.-open (get-state el))
                           (update-popup-content! el popup)))
             "disabled" (when _new (hide-tooltip! el))
             nil))})