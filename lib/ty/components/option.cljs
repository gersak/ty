(ns ty.components.option
  "Ty Option Web Component - Rich HTML alternative to native <option>"
  (:require [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load option styles
(defstyles option-styles "ty/components/option.css")

;; =====================================================
;; TY-OPTION WEB COMPONENT
;; =====================================================

(defn option-attributes
  "Read option attributes directly from element"
  [^js el]
  {:value (or (wcs/attr el "value") (.-textContent el))
   :disabled (wcs/parse-bool-attr el "disabled")
   :selected (wcs/parse-bool-attr el "selected")
   :highlighted (wcs/parse-bool-attr el "highlighted")
   :hidden (wcs/parse-bool-attr el "hidden")})

(defn render!
  "Render ty-option component with rich HTML content"
  [^js el]
  (let [{:keys [disabled selected highlighted hidden]} (option-attributes el)
        root (wcs/ensure-shadow el)]

    ;; Ensure styles are loaded
    (ensure-styles! root option-styles "ty-option")

    ;; Create simple wrapper that preserves content
    (when-not (.querySelector root ".option-content")
      (set! (.-innerHTML root)
            "<div class=\"option-content\"><slot></slot></div>"))

    ;; Update content wrapper attributes
    (when-let [content (.querySelector root ".option-content")]
      (if disabled
        (.setAttribute content "disabled" "")
        (.removeAttribute content "disabled"))
      (if selected
        (.setAttribute content "selected" "")
        (.removeAttribute content "selected"))
      (if highlighted
        (.setAttribute content "highlighted" "")
        (.removeAttribute content "highlighted"))
      (if hidden
        (.setAttribute content "hidden" "")
        (.removeAttribute content "hidden")))

    ;; Set value property for easy access
    (let [value (or (wcs/attr el "value") (.-textContent el))]
      (set! (.-value el) value))

    el))

;; =====================================================
;; WEB COMPONENT REGISTRATION
;; =====================================================

(def configuration
  {:observed [:value :disabled :selected :highlighted :hidden]
   :connected render!
   :attr (fn [^js el _attr-name _old _new]
           ;; Any attribute change triggers re-render
           (render! el))})
