(ns ty.components.popup
  "Popup component with self-positioning capabilities.
   Positions itself relative to an anchor element."
  (:require [ty.css :refer [ensure-styles!]]
            [ty.positioning :as pos]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load popup styles
(defstyles popup-styles)

(defn popup-attributes
  "Read all popup attributes directly from element"
  [^js el]
  {:open (wcs/parse-bool-attr el "open")
   :placement (or (wcs/attr el "placement") "bottom")
   :offset (or (wcs/parse-int-attr el "offset") 8)
   :flip (wcs/parse-bool-attr el "flip")})

(defn get-anchor-element
  "Get the slotted anchor element"
  [^js shadow-root]
  (when-let [anchor-slot (.querySelector shadow-root "[name=anchor]")]
    (first (.assignedElements anchor-slot))))

(defn get-popup-content
  "Get the popup content container"
  [^js shadow-root]
  (.querySelector shadow-root "#popup-container"))

(defn update-position!
  "Calculate and update popup position based on anchor"
  [^js el ^js shadow-root]
  (let [{:keys [placement offset flip]} (popup-attributes el)
        anchor (get-anchor-element shadow-root)
        popup (get-popup-content shadow-root)]
    (when (and anchor popup)
      ;; Calculate preferred placements based on placement attribute
      (let [preferences (case placement
                          "top" [:top :bottom :left :right]
                          "bottom" [:bottom :top :left :right]
                          "left" [:left :right :top :bottom]
                          "right" [:right :left :top :bottom]
                          ;; Default auto placement
                          [:bottom :top :right :left])
            ;; Use positioning engine to find best position
            position-data (pos/find-best-position
                            {:target-el anchor
                             :floating-el popup
                             :preferences preferences
                             :offset offset
                             :padding 8})
            {:keys [x y]} position-data]
        ;; Update CSS variables
        (.setProperty (.-style el) "--x" (str x "px"))
        (.setProperty (.-style el) "--y" (str y "px"))))))

(defn render! [^js el]
  (let [root (wcs/ensure-shadow el)
        existing-content (.querySelector root "#popup-container")
        {:keys [open]} (popup-attributes el)]
    ;; Ensure styles are loaded
    (ensure-styles! root popup-styles "ty-popup")

    ;; Create structure if it doesn't exist
    (when-not existing-content
      (let [container (js/document.createElement "div")
            content (js/document.createElement "slot")
            anchor (js/document.createElement "slot")]
        (set! (.-id content) "popup-content")
        (set! (.-id container) "popup-container")
        (set! (.-name anchor) "anchor")
        (.appendChild root anchor)
        (.appendChild root container)
        (.appendChild container content)
        ;; Initialize position
        (.setProperty (.-style el) "--x" "0px")
        (.setProperty (.-style el) "--y" "0px")))

    ;; Update visibility
    (when-let [content (.querySelector root "#popup-container")]
      (set! (.-className content) (if open "open" "")))

    ;; Update position when open
    (when open
      (update-position! el root))))

(wcs/define! "ty-popup"
  {:observed [:open :placement :offset :flip]
   :connected render!
   :attr (fn [^js el attr-name _old new]
           ;; Re-render on attribute changes
           (render! el)
           ;; Update position immediately if open and placement-related attributes change
           (when (and (wcs/parse-bool-attr el "open")
                      (contains? #{"placement" "offset" "flip"} attr-name))
             (update-position! el (.-shadowRoot el))))})
