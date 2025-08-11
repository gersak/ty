(ns ty.components.button
  (:require [clojure.string :as str]
            [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load button styles from button.css
#_{:clj-kondo/ignore [:uninitialized-var]}
(defstyles button-styles)

(defn render! [el]
  (let [{:keys [flavor disabled label size filled outlined accent]
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
        ;; Set classes based on attributes
        (set! (.-className button-el)
              (str/trim (str (or flavor "neutral")
                             " "
                             (or size "md")
                             " "
                             ;; Appearance logic
                             (cond
                               accent "accent"
                               (and filled outlined) "filled-outlined"
                               filled "filled"
                               outlined "outlined"
                               :else "plain")
                             (when class-name (str " " class-name)))))
        ;; Update label in the text span if it exists
        (when-let [text-span (.querySelector button-el ".button-text")]
          (set! (.-textContent text-span) (or label (.-textContent el)))))
      ;; Create new button structure
      (let [new-button (js/document.createElement "button")
            start-slot (js/document.createElement "slot")
            text-span (js/document.createElement "span")
            end-slot (js/document.createElement "slot")]

        ;; Set button properties
        (set! (.-disabled new-button) disabled)
        (set! (.-className new-button)
              (str/trim (str (or flavor "neutral")
                             " "
                             (or size "md")
                             " "
                             ;; Appearance logic
                             (cond
                               accent "accent"
                               (and filled outlined) "filled-outlined"
                               filled "filled"
                               outlined "outlined"
                               :else "plain")
                             (when class-name (str " " class-name)))))

        ;; Configure slots and text
        (set! (.-name start-slot) "start")
        (set! (.-className text-span) "button-text")
        (set! (.-textContent text-span) (or label (.-textContent el)))
        (set! (.-name end-slot) "end")

        ;; Build button structure
        (.appendChild new-button start-slot)
        (.appendChild new-button text-span)
        (.appendChild new-button end-slot)

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
  {:observed [:flavor :disabled :label :class :size :filled :outlined :accent]
   :props {:flavor nil
           :disabled nil
           :label nil
           :class nil
           :size nil
           :filled nil
           :outlined nil
           :accent nil}
   :construct (fn [el]
                ;; Hydrate props from attributes at construction
                (let [p {:flavor (wcs/attr el :flavor)
                         :disabled (wcs/parse-bool-attr el :disabled)
                         :label (wcs/attr el :label)
                         :class (wcs/attr el :class)
                         :size (wcs/attr el :size)
                         :filled (wcs/parse-bool-attr el :filled)
                         :outlined (wcs/parse-bool-attr el :outlined)
                         :accent (wcs/parse-bool-attr el :accent)}]
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
             :filled (wcs/set-props! el {:filled (wcs/parse-bool-attr el :filled)})
             :outlined (wcs/set-props! el {:outlined (wcs/parse-bool-attr el :outlined)})
             :accent (wcs/set-props! el {:accent (wcs/parse-bool-attr el :accent)})
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
