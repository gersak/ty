(ns ty.components.navigation
  (:require [ty.css :as css]))

;; Load navigation CSS at compile time
(css/defstyles navigation-styles "navigation.css")

;; Export styles for use in components
(defn load-navigation-styles!
  "Loads navigation CSS styles into the document.
   Call this once during app initialization."
  []
  (if (instance? js/CSSStyleSheet navigation-styles)
    ;; Use adoptedStyleSheets for better performance when supported
    (when-let [adoptedSheets (.-adoptedStyleSheets js/document)]
      (.push adoptedSheets navigation-styles))
    ;; Fallback: inject CSS into document head
    (let [style-element (.createElement js/document "style")]
      (set! (.-textContent style-element) navigation-styles)
      (.appendChild (.-head js/document) style-element))))

;; Export for manual injection if needed
(defn get-navigation-styles []
  navigation-styles)
