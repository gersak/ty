(ns ty.components.option
  "Ty Option Web Component - Rich HTML alternative to native <option>
   
   Enhanced with robust property-attribute synchronization for framework compatibility."
  (:require [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load option styles
(defstyles option-styles "ty/components/option.css")

;; =====================================================
;; PROPERTY-ATTRIBUTE SYNCHRONIZATION
;; =====================================================

(defn get-option-value
  "Get value from either property or attribute, with textContent fallback.
   Property takes precedence (for framework compatibility)."
  [^js el]
  (let [prop-value (.-value el)
        attr-value (wcs/attr el "value")
        text-value (.-textContent el)
        resolved (or prop-value attr-value text-value)]

    ;; Debug logging in development
    (when (and goog.DEBUG prop-value attr-value (not= prop-value attr-value))
      (js/console.warn "[ty-option] Property and attribute value mismatch:"
                       "property=" prop-value "attribute=" attr-value
                       "Using property value."))

    resolved)) ; Text content fallback

(defn sync-properties-to-attributes!
  "Sync JavaScript properties to HTML attributes for CSS/DevTools consistency"
  [^js el delta]
  (when (contains? delta "value")
    (let [value (get delta "value")]
      (if (some? value)
        (.setAttribute el "value" (str value))
        (.removeAttribute el "value"))))

  (when (contains? delta "selected")
    (let [selected (get delta "selected")]
      (if selected
        (.setAttribute el "selected" "")
        (.removeAttribute el "selected"))))

  (when (contains? delta "disabled")
    (let [disabled (get delta "disabled")]
      (if disabled
        (.setAttribute el "disabled" "")
        (.removeAttribute el "disabled"))))

  (when (contains? delta "highlighted")
    (let [highlighted (get delta "highlighted")]
      (if highlighted
        (.setAttribute el "highlighted" "")
        (.removeAttribute el "highlighted"))))

  (when (contains? delta "hidden")
    (let [hidden (get delta "hidden")]
      (if hidden
        (.setAttribute el "hidden" "")
        (.removeAttribute el "hidden")))))

(defn sync-attributes-to-properties!
  "Ensure properties reflect attribute changes"
  [^js el delta]
  (when (contains? delta "value")
    (set! (.-value el) (get delta "value")))

  (when (contains? delta "selected")
    (set! (.-selected el) (boolean (get delta "selected"))))

  (when (contains? delta "disabled")
    (set! (.-disabled el) (boolean (get delta "disabled"))))

  (when (contains? delta "highlighted")
    (set! (.-highlighted el) (boolean (get delta "highlighted"))))

  (when (contains? delta "hidden")
    (set! (.-hidden el) (boolean (get delta "hidden")))))

;; =====================================================
;; TY-OPTION WEB COMPONENT
;; =====================================================

(defn option-attributes
  "Read option attributes from element using robust value resolution"
  [^js el]
  {:value (get-option-value el) ; Use robust value resolution
   :disabled (wcs/parse-bool-attr el "disabled")
   :selected (wcs/parse-bool-attr el "selected")
   :highlighted (wcs/parse-bool-attr el "highlighted")
   :hidden (wcs/parse-bool-attr el "hidden")})

(defn render!
  "Render ty-option component with rich HTML content and property sync"
  [^js el]
  (let [{:keys [value disabled selected highlighted hidden]} (option-attributes el)
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

    ;; CRITICAL: Ensure value property is set using robust resolution
    ;; This handles cases where frameworks set property before connection
    (when value
      (set! (.-value el) value)
      ;; Also ensure attribute is set for CSS selectors and debugging
      (when-not (.hasAttribute el "value")
        (.setAttribute el "value" (str value))))

    el))

;; =====================================================
;; WEB COMPONENT REGISTRATION
;; =====================================================

(def configuration
  {:observed [:value :disabled :selected :highlighted :hidden]
   :props {:value nil ; Enable property watching for framework compatibility
           :selected nil ; React, Vue, Angular can set these properties
           :disabled nil ; before element is connected to DOM
           :highlighted nil
           :hidden nil}
   :connected render!
   :disconnected (fn [^js el]) ; No cleanup needed currently
   :attr (fn [^js el delta]
           ;; Sync attribute changes to properties, then render
           (sync-attributes-to-properties! el delta)
           (render! el))
   :prop (fn [^js el delta]
           ;; Sync property changes to attributes, then render
           (sync-properties-to-attributes! el delta)
           (render! el))})
