(ns ty.components.button
  (:require [clojure.string :as str]
            [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load button styles from button.css
(defstyles button-styles)

(defn render! [el]
  (let [{:keys [variant disabled label]
         class-name :class
         :or {disabled false
              label ""}} (wcs/get-props el)
        root (wcs/ensure-shadow el)
        ;; Check if we already have button element
        button-el (.querySelector root "button")]

    ;; Ensure styles are loaded (handles both CSSStyleSheet and string)
    (ensure-styles! root button-styles "ty-button")

    ;; Create or update button element
    (if button-el
      ;; Update existing button
      (do
        (set! (.-disabled button-el) disabled)
        ;; Set variant class and any additional classes
        (set! (.-className button-el)
              (str/trim (str (or variant "neutral")
                             (when class-name (str " " class-name)))))
        (set! (.-textContent button-el) (or label (.-textContent el))))
      ;; Create new button
      (let [new-button (js/document.createElement "button")]
        (set! (.-disabled new-button) disabled)
        (set! (.-className new-button)
              (str/trim (str (or variant "neutral")
                             (when class-name (str " " class-name)))))
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
                (let [p {:variant (wcs/attr el :variant)
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
