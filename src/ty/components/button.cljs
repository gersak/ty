(ns ty.components.button
  (:require [clojure.string :as str]
            [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load button styles from button.css
#_{:clj-kondo/ignore [:uninitialized-var]}
(defstyles button-styles)

(defn render! [el]
  (let [{:keys [flavor disabled label size appearance]
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
        ;; Set appearance, flavor, size classes, and any additional classes
        (set! (.-className button-el)
              (str/trim (str (or appearance "filled-outlined")
                             " "
                             (or flavor "neutral")
                             " "
                             (or size "md")
                             (when class-name (str " " class-name)))))
        (set! (.-textContent button-el) (or label (.-textContent el))))
      ;; Create new button
      (let [new-button (js/document.createElement "button")]
        (set! (.-disabled new-button) disabled)
        (set! (.-className new-button)
              (str/trim (str (or appearance "filled-outlined")
                             " "
                             (or flavor "neutral")
                             " "
                             (or size "md")
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
  {:observed [:flavor :disabled :label :class :size :appearance]
   :props {:flavor nil
           :disabled nil
           :label nil
           :class nil
           :size nil
           :appearance nil}
   :construct (fn [el]
                ;; Hydrate props from attributes at construction
                (let [p {:flavor (wcs/attr el :flavor)
                         :disabled (wcs/parse-bool-attr el :disabled)
                         :label (wcs/attr el :label)
                         :class (wcs/attr el :class)
                         :size (wcs/attr el :size)
                         :appearance (wcs/attr el :appearance)}]
                  (wcs/set-props! el p)))
   :connected render!
   :attr (fn [el name _old new]
           ;; Reflect attribute changes into props
           (case name
             :flavor (wcs/set-props! el {:flavor new})
             :disabled (wcs/set-props! el {:disabled (wcs/parse-bool-attr el :disabled)})
             :label (wcs/set-props! el {:label new})
             :class (wcs/set-props! el {:class new})
             :size (wcs/set-props! el {:size new})
             :appearance (wcs/set-props! el {:appearance new})
             nil)
           (render! el))
   :prop (fn [el _k _old _new]
           ;; Any prop change re-renders
           (render! el))})

;; -----------------------------
;; Hot Reload Hooks
;; -----------------------------

(defn ^:dev/before-load stop []
  ;; Called before code is reloaded
  (when goog.DEBUG
    (js/console.log "[ty-button] Preparing for hot reload...")))

(defn ^:dev/after-load start []
  ;; Called after code is reloaded
  ;; The shim will automatically refresh all button instances
  (when goog.DEBUG
    (js/console.log "[ty-button] Hot reload complete!")))
