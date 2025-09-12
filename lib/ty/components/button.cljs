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
   Only accepts: primary, secondary, success, danger, warning, neutral"
  [flavor]
  (let [valid-flavors #{"primary" "secondary" "success" "danger" "warning" "neutral"}
        normalized (or flavor "neutral")]
    (when (and goog.DEBUG (not (contains? valid-flavors normalized)))
      (js/console.warn (str "[ty-button] Invalid flavor '" flavor "'. Using 'neutral'. "
                            "Valid flavors: primary, secondary, success, danger, warning, neutral.")))
    (if (contains? valid-flavors normalized)
      normalized
      "neutral")))

(defn button-attributes
  "Read all button attributes directly from element.
   Only accepts new industry-standard semantic flavors.
   Now includes form-related attributes: type, name, value."
  [^js el]
  (let [raw-flavor (wcs/attr el "flavor")
        raw-type (wcs/attr el "type")]
    {:flavor (validate-flavor raw-flavor)
     :disabled (wcs/parse-bool-attr el "disabled")
     :label (wcs/attr el "label")
     :class (wcs/attr el "class")
     :size (wcs/attr el "size")
     :filled (wcs/parse-bool-attr el "filled")
     :outlined (wcs/parse-bool-attr el "outlined")
     :accent (wcs/parse-bool-attr el "accent")
     :pill (wcs/parse-bool-attr el "pill")
     :action (wcs/parse-bool-attr el "action")
     ;; Form-related attributes
     :type (or raw-type "submit") ; Default to submit like native buttons
     :name (wcs/attr el "name")
     :value (wcs/attr el "value")}))

(defn get-form-internals
  "Get ElementInternals for form participation"
  [^js el]
  (.-_internals el))

(defn handle-form-action!
  "Handle form submission or reset based on button type"
  [^js el button-type]
  (let [internals (get-form-internals el)
        form (when internals (.-form internals))]
    (when form
      (case button-type
        "submit" (do
                   ;; For submit buttons, set the button's name/value as form data
                   (when-let [button-name (wcs/attr el "name")]
                     (when-let [button-value (wcs/attr el "value")]
                       (.setFormValue internals button-value)))
                   ;; Submit the form (without submitter parameter)
                   (.requestSubmit form))
        "reset" (.reset form)
        "button" nil ; Do nothing for type="button"
        ;; Default to submit behavior
        (when-let [button-name (wcs/attr el "name")]
          (when-let [button-value (wcs/attr el "value")]
            (.setFormValue internals button-value)))))))

(defn build-class-list
  "Build class list from attributes.
   Flavor is normalized to industry-standard semantics (primary, secondary, success, danger, warning, neutral)."
  [{:keys [flavor size filled outlined accent pill action class plain]}]
  (str/trim
    (str (or flavor "neutral")
         " "
         (or size "md")
         " "
        ;; Appearance logic
         (cond
           plain "plain"
           accent "accent"
           (and filled outlined) "filled-outlined"
           filled "filled"
           outlined "outlined"
           :else "accent")
         (when pill " pill")
         (when action " action")
         (when class (str " " class)))))

(defn render! [^js el]
  (let [{:keys [disabled label type]
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

        ;; Add click event listener with form action support
        (.addEventListener new-button "click"
                           (fn [e]
                             (when-not disabled
                               (.stopPropagation e)

                               ;; Handle form action (submit/reset) first
                               (handle-form-action! el type)

                               ;; Then dispatch the custom click event for other listeners
                               (.dispatchEvent el (js/CustomEvent. "click"
                                                                   #js {:bubbles true
                                                                        :composed true
                                                                        :detail {:originalEvent e}})))))
        (.appendChild root new-button)))

    el))

(def configuration
  {:observed [:flavor :disabled :label :class :size :filled :outlined :accent :pill :action :plain
              :type :name :value] ; Added form-related attributes
   :form-associated true ; Enable form participation
   ;; No need for props - we read directly from attributes
   :connected render!
   :attr (fn [^js el delta]
           ;; Always re-render for attribute changes
           (render! el))
   :prop (fn [^js el prop-delta]
           (render! el))})

; (wcs/define! "ty-button" configuration)

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
