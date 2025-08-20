(ns ty.components.input
  "Basic input component with layout integration and semantic styling"
  (:require [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load input styles from input.css
#_{:clj-kondo/ignore [:uninitialized-var]}
(defstyles input-styles)

(defn input-attributes
  "Read all input attributes directly from element"
  [^js el]
  {:type (wcs/attr el "type")
   :value (wcs/attr el "value")
   :placeholder (wcs/attr el "placeholder")
   :label (wcs/attr el "label")
   :disabled (wcs/parse-bool-attr el "disabled")
   :required (wcs/parse-bool-attr el "required")
   :size (wcs/attr el "size")
   :flavor (wcs/attr el "flavor")
   :class (wcs/attr el "class")})

(defn build-class-list
  "Build class list from attributes"
  [{:keys [size flavor disabled required class]}]
  (str (or size "md")
       " "
       (or flavor "neutral")
       (when disabled " disabled")
       (when required " required")
       (when class (str " " class))))

(defn render! [^js el]
  (let [{:keys [type value placeholder label disabled required]
         :as attrs} (input-attributes el)
        root (wcs/ensure-shadow el)
        ;; Check if we already have the structure
        existing-label (.querySelector root "label")
        existing-input (.querySelector root "input")]

    ;; Ensure styles are loaded
    (ensure-styles! root input-styles "ty-input")

    ;; Create or update structure
    (if (and existing-label existing-input)
      ;; Update existing elements
      (do
        (when label
          (set! (.-textContent existing-label) label)
          (set! (.. existing-label -style -display) "block")
          ;; Handle required class
          (if required
            (.add (.-classList existing-label) "required")
            (.remove (.-classList existing-label) "required")))
        (when-not label
          (set! (.. existing-label -style -display) "none"))

        (set! (.-type existing-input) (or type "text"))
        (set! (.-value existing-input) (or value ""))
        (set! (.-placeholder existing-input) (or placeholder ""))
        (set! (.-disabled existing-input) disabled)
        (set! (.-required existing-input) required)
        (set! (.-className existing-input) (build-class-list attrs)))

      ;; Create new structure
      (let [container (js/document.createElement "div")
            label-el (js/document.createElement "label")
            input-el (js/document.createElement "input")]

        ;; Set up container
        (set! (.-className container) "input-container")

        ;; Set up label
        (set! (.-className label-el) "input-label")
        (when label
          (set! (.-textContent label-el) label)
          (set! (.. label-el -style -display) "block")
          ;; Add required class if needed
          (when required
            (.add (.-classList label-el) "required")))
        (when-not label
          (set! (.. label-el -style -display) "none"))

        ;; Set up input
        (set! (.-type input-el) (or type "text"))
        (set! (.-value input-el) (or value ""))
        (set! (.-placeholder input-el) (or placeholder ""))
        (set! (.-disabled input-el) disabled)
        (set! (.-required input-el) required)
        (set! (.-className input-el) (build-class-list attrs))

        ;; Add event listeners for custom events
        (.addEventListener input-el "input"
                           (fn [e]
                             (.dispatchEvent el (js/CustomEvent. "input"
                                                                 #js {:bubbles true
                                                                      :composed true
                                                                      :detail {:value (.-value (.-target e))
                                                                               :originalEvent e}}))))

        (.addEventListener input-el "change"
                           (fn [e]
                             (.dispatchEvent el (js/CustomEvent. "change"
                                                                 #js {:bubbles true
                                                                      :composed true
                                                                      :detail {:value (.-value (.-target e))
                                                                               :originalEvent e}}))))

        ;; Build structure
        (.appendChild container label-el)
        (.appendChild container input-el)
        (.appendChild root container)))

    el))

(wcs/define! "ty-input"
  {:observed [:type :value :placeholder :label :disabled :required :size :flavor :class]
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
    (js/console.log "[ty-input] Preparing for hot reload...")))

(defn ^:dev/after-load start []
  ;; Called after code is reloaded
  ;; The shim will automatically refresh all input instances
  (when goog.DEBUG
    (js/console.log "[ty-input] Hot reload complete!")))
