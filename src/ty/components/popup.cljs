(ns ty.components.popup
  "Minimal popup component - starting fresh.
   Just a fixed positioned element that's always visible."
  (:require [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load popup styles
(defstyles popup-styles)

(defn popup-attributes
  "Read all button attributes directly from element"
  [^js el]
  {:flavor (wcs/attr el "flavor")
   :position (wcs/attr el "position")
   :open (wcs/parse-bool-attr el "open")
   :disabled (wcs/parse-bool-attr el "disabled")
   :class (wcs/attr el "class")})

(defn render! [^js el]
  (let [root (wcs/ensure-shadow el)
        content (.querySelector root "#popup-content")
        {:keys [open]
         :as attrs} (popup-attributes el)]
    ;; For now, just ensure we have basic structure
    ;; No shadow DOM - everything in Light DOM
    (ensure-styles! root popup-styles "ty-popup")
    (if content
      (when content
        (set! (.-className content)
              (when open "open")))
      (let [container (js/document.createElement "div")
            content (js/document.createElement "slot")
            anchor (js/document.createElement "slot")]
        (set! (.-id content) "popup-content")
        (set! (.-id container) "popup-container")
        (set! (.-name anchor) "anchor")
        ;; Set content initial attributes
        (when open
          (set! (.-className content) "open"))
        (.appendChild root anchor)
        (.appendChild root container)
        (.appendChild container content)
        (.setProperty (.-style el) "--x" "0px")
        (.setProperty (.-style el) "--y" "0px")))))

(wcs/define! "ty-popup"
  {:observed [:flavor :disabled :class :open :position]
   :connected render!
   :attr (fn [^js el _attr-name _old _new]
           ;; Any attribute change triggers re-render
           (render! el))})
