(ns ty.components.button
  (:require [clojure.string :as str]
            [ty.shim :as wcs]
            [ty.styles.tokens :as tokens]))

(defn variant->semantic-state
  "Map button variant to semantic state"
  [variant]
  (case variant
    "positive" :positive
    "negative" :negative
    "warning" :exception
    "neutral" :neutral
    :important)) ; default

(defn create-styles
  "Generate component styles"
  [{:keys [variant disabled]}]
  (let [state (variant->semantic-state variant)]
    (str "<style>"
         ":host {"
         "  display: inline-block;"
         "  font-family: " (tokens/css-var :font-sans) ";"
         "}"
         ""
         "button {"
         "  display: flex;"
         "  align-items: center;"
         "  justify-content: center;"
         ;; Colors based on semantic state
         "  background: " (tokens/semantic-var :background state) ";"
         "  color: " (tokens/semantic-var :color state) ";"
         "  border: 1px solid " (tokens/semantic-var :border state) ";"
         ;; Spacing
         "  padding: " (tokens/spacing 2) " " (tokens/spacing 4) ";"
         "  min-height: " (tokens/spacing 10) ";"
         "  min-width: " (tokens/spacing 20) ";"
         ;; Typography
         "  font-size: " (tokens/font-size :sm) ";"
         "  font-weight: " (tokens/font-weight :semibold) ";"
         "  line-height: " (tokens/line-height :normal) ";"
         ;; Shape
         "  border-radius: " (tokens/radius :md) ";"
         ;; Interaction
         "  cursor: " (if disabled "not-allowed" "pointer") ";"
         "  opacity: " (if disabled "0.6" "1") ";"
         "  user-select: none;"
         "  transition: " (tokens/transition :all) ";"
         "}"
         ""
         "button:hover:not(:disabled) {"
         "  background: " (tokens/semantic-var :background state :p1) ";"
         "  transform: translateY(-1px);"
         "  box-shadow: " (tokens/shadow :sm) ";"
         "}"
         ""
         "button:active:not(:disabled) {"
         "  transform: translateY(0);"
         "}"
         ""
         "button:focus-visible {"
         "  outline: none;"
         "  box-shadow: 0 0 0 2px " (tokens/css-var :background) ","
         "              0 0 0 4px " (tokens/semantic-var :color state) ";"
         "}"
         "</style>")))

(defn render! [el]
  (let [{:keys [variant disabled label]
         class-name :class
         :or {variant "important"
              disabled false
              label ""}} (wcs/get-props el)
        root (wcs/ensure-shadow el)
        ;; Check if we already have our elements
        style-el (.querySelector root "style")
        button-el (.querySelector root "button")]

    (println "RENDERING!!!")
    ;; Create or update style element
    (if style-el
      ;; Update existing styles
      (set! (.-textContent style-el)
            (create-styles {:variant variant
                            :disabled disabled}))
      ;; Create new style element
      (let [new-style (js/document.createElement "style")]
        (set! (.-textContent new-style) (create-styles {:variant variant
                                                        :disabled disabled}))
        (.appendChild root new-style)))

    ;; Create or update button element
    (if button-el
      ;; Update existing button
      (do
        (set! (.-disabled button-el) disabled)
        (when class-name
          (set! (.-className button-el) class-name))
        (set! (.-textContent button-el) (or label (.-textContent el))))
      ;; Create new button
      (let [new-button (js/document.createElement "button")]
        (set! (.-disabled new-button) disabled)
        (when class-name
          (set! (.-className new-button) class-name))
        (set! (.-textContent new-button) (or label (.-textContent el)))

        ;; Add click event listener
        (.addEventListener new-button "click"
                           (fn [e]
                             (when-not disabled
                               (.stopPropagation e)
                               (.dispatchEvent el (js/CustomEvent. "click"
                                                                   #js {:bubbles true
                                                                        :composed true
                                                                        :detail {:originalEvent e}})))))
        (.appendChild root new-button)))

    el))

(wcs/define! "ty-button"
  {:observed [:variant :disabled :label :class]
   :props {:variant nil
           :disabled nil
           :label nil
           :class nil}
   :construct (fn [el]
                ;; Hydrate props from attributes at construction
                (let [p {:variant (or (wcs/attr el :variant) "important")
                         :disabled (wcs/parse-bool-attr el :disabled)
                         :label (wcs/attr el :label)
                         :class (wcs/attr el :class)}]
                  (wcs/set-props! el p)))
   :connected render!
   :attr (fn [el name _old new]
           ;; Reflect attribute changes into props
           (case name
             :variant (wcs/set-props! el {:variant new})
             :disabled (wcs/set-props! el {:disabled (wcs/parse-bool-attr el :disabled)})
             :label (wcs/set-props! el {:label new})
             :class (wcs/set-props! el {:class new})
             nil)
           (render! el))
   :prop (fn [el _k _old _new]
           ;; Any prop change re-renders
           (render! el))})
