(ns ty.components.tab
  "Individual tab component - contains both button label and panel content"
  (:require [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load tab styles
(defstyles tab-styles)

;; =====================================================
;; Tab Attributes
;; =====================================================

(defn tab-attributes
  "Extract tab configuration from element attributes"
  [^js el]
  {:id (wcs/attr el "id") ; Required unique identifier
   :label (wcs/attr el "label") ; Simple text label
   :disabled (wcs/parse-bool-attr el "disabled")})

;; =====================================================
;; Rendering
;; =====================================================

(defn render!
  "Render the tab - just acts as a container for panel content.
   The label rendering is handled by the parent ty-tabs component."
  [^js el]
  (let [root (wcs/ensure-shadow el)
        {:keys [id disabled]} (tab-attributes el)]

    ;; Ensure styles are loaded
    (ensure-styles! root tab-styles "ty-tab")

    ;; Simple structure - just a scrollable container for panel content
    ;; The parent ty-tabs will handle positioning and sizing via CSS variables
    (set! (.-innerHTML root)
          (str "<div class=\"tab-panel\""
               " role=\"tabpanel\""
               (when id (str " id=\"panel-" id "\""))
               (when id (str " aria-labelledby=\"tab-" id "\""))
               ">"
               "  <slot></slot>" ; Default slot for panel content
               "</div>"))))

(defn cleanup!
  "Cleanup when tab is disconnected"
  [^js el]
  ;; Nothing to cleanup for now
  nil)

(def configuration
  {:observed [:id :label :disabled :slot]
   :props {} ; No special properties for now
   :connected render!
   :disconnected cleanup!
   :attr (fn [^js el delta]
           (render! el))
   :prop (fn [^js el delta]
           (render! el))})
