(ns ty.components.button
  (:require [clojure.string :as str]
            [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load button styles from button.css
#_{:clj-kondo/ignore [:uninitialized-var]}
(defstyles button-styles)

(defn validate-flavor
  "Validate that flavor uses new industry-standard semantic naming.
   Only accepts: primary, secondary, success, danger, warning, info, neutral"
  [flavor]
  (let [valid-flavors #{"primary" "secondary" "success" "danger" "warning" "info" "neutral"}
        normalized (or flavor "neutral")]
    (when (and goog.DEBUG (not (contains? valid-flavors normalized)))
      (js/console.warn (str "[ty-button] Invalid flavor '" flavor "'. Using 'neutral'. "
                            "Valid flavors: primary, secondary, success, danger, warning, info, neutral.")))
    (if (contains? valid-flavors normalized)
      normalized
      "neutral")))

(defn button-attributes
  "Read all button attributes directly from element.
   Only accepts new industry-standard semantic flavors."
  [^js el]
  (let [raw-flavor (wcs/attr el "flavor")]
    {:flavor (validate-flavor raw-flavor)
     :disabled (wcs/parse-bool-attr el "disabled")
     :label (wcs/attr el "label")
     :class (wcs/attr el "class")
     :size (wcs/attr el "size")
     :filled (wcs/parse-bool-attr el "filled")
     :outlined (wcs/parse-bool-attr el "outlined")
     :accent (wcs/parse-bool-attr el "accent")
     :pill (wcs/parse-bool-attr el "pill")
     :action (wcs/parse-bool-attr el "action")}))

(defn build-class-list
  "Build class list from attributes.
   Flavor is normalized to industry-standard semantics (primary, secondary, success, danger, warning, info, neutral)."
  [{:keys [flavor size filled outlined accent pill action class]}]
  (str/trim
   (str (or flavor "neutral")
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
        (when pill " pill")
        (when action " action")
        (when class (str " " class)))))

(defn render! [^js el]
  (let [{:keys [disabled label]
         :as attrs} (button-attributes el)
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
        (set! (.-className button-el) (build-class-list attrs)))
      ;; Create new button structure
      (let [new-button (js/document.createElement "button")
            start-slot (js/document.createElement "slot")
            text-slot (js/document.createElement "slot")
            end-slot (js/document.createElement "slot")]

        ;; Set button properties
        (set! (.-disabled new-button) disabled)
        (set! (.-className new-button) (build-class-list attrs))

        ;; Configure slots and text
        (set! (.-name start-slot) "start")
        (set! (.-className start-slot) "start")
        (set! (.-name end-slot) "end")
        (set! (.-className end-slot) "end")

        ;; Build button structure
        (.appendChild new-button start-slot)
        (.appendChild new-button text-slot)
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
  {:observed [:flavor :disabled :label :class :size :filled :outlined :accent :pill :action]
   ;; No need for props - we read directly from attributes
   :connected render!
   :attr (fn [^js el _attr-name _old _new]
           ;; Any attribute change triggers re-render
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
