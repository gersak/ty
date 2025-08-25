(ns ty.value
  "Simplified value handling using multimethods.
   Each component registers its parse/normalize functions once.
   
   The Pattern:
   1. Parse any input format to internal value
   2. Compare parsed values to detect changes  
   3. Sync to property, attribute, and state
   4. Normalize for consistent attribute display"
  (:refer-clojure :exclude [parse-boolean]))

;; =====================================================
;; Multimethod Registry
;; =====================================================

(defmulti parse-value
  "Parse raw input to internal format based on element tag name.
   Components register their parsers via defmethod."
  (fn [el value] (.-tagName el)))

(defmulti normalize-value
  "Normalize internal value to string for attribute display.
   Components register their normalizers via defmethod."
  (fn [el value] (.-tagName el)))

;; Default implementations
(defmethod parse-value :default [el value]
  ;; Default: just return string value or nil
  (when (and value (not= value ""))
    (str value)))

(defmethod normalize-value :default [el value]
  ;; Default: convert to string
  (when value (str value)))

;; =====================================================
;; Core Functions
;; =====================================================

(defn get-value
  "Get current parsed value from element property."
  [^js el]
  (.-value el))

(defn get-attribute
  "Get current attribute value (the string in HTML)."
  [^js el]
  (when (.hasAttribute el "value")
    (.getAttribute el "value")))

(defn external-value-changed?
  "Check if value changed externally by comparing PARSED values.
   This avoids issues with different string formats that represent
   the same internal value."
  [^js el new-raw-value]
  (let [current-parsed (get-value el)
        new-parsed (parse-value el new-raw-value)]
    (not= current-parsed new-parsed)))

(defn sync-value!
  "Parse input once and sync to all three places:
   - Property (parsed value for programmatic access)
   - Attribute (normalized string for HTML visibility)
   - State (internal component state if applicable)
   
   Returns the parsed value."
  [^js el raw-value]
  (let [parsed (parse-value el raw-value)
        normalized (when parsed (normalize-value el parsed))]

    ;; 1. Property stores parsed internal value
    (set! (.-value el) parsed)

    ;; 2. Attribute shows normalized string
    (if normalized
      (.setAttribute el "value" normalized)
      (.removeAttribute el "value"))

    ;; 3. Update component state if it has state management
    (when-let [update-fn (.-tyUpdateState el)]
      (update-fn {:value parsed}))

    parsed))

;; =====================================================
;; Component Integration Helpers
;; =====================================================

(defn setup-component!
  "Initialize a component with its state update function.
   Call this in component's connected callback."
  [^js el update-state-fn]
  (set! (.-tyUpdateState el) update-state-fn)
  ;; Sync initial value if present
  (when-let [initial (get-attribute el)]
    (sync-value! el initial)))

(defn handle-attr-change
  "Standard handler for value attribute changes.
   Use this in component's :attr callback."
  [^js el attr-name _old-value new-value render-fn]
  (when (= attr-name "value")
    (when (external-value-changed? el new-value)
      (sync-value! el new-value)
      (when render-fn
        (render-fn el)))))

;; =====================================================
;; Common Parsers (for reuse via delegate)
;; =====================================================

(defn parse-string
  "Parse string values, nil for empty."
  [value]
  (when (and value (not= value ""))
    (str value)))

(defn parse-number
  "Parse numeric values."
  [value]
  (cond
    (nil? value) nil
    (number? value) value
    (string? value) (when (not= value "")
                      (let [parsed (js/parseFloat value)]
                        (when-not (js/isNaN parsed) parsed)))
    :else nil))

(defn parse-boolean
  "Parse boolean values."
  [value]
  (cond
    (boolean? value) value
    (string? value) (case value
                      ("true" "TRUE" "1" "yes" "YES") true
                      ("false" "FALSE" "0" "no" "NO" "") false
                      nil)
    :else nil))

(defn parse-array
  "Parse comma-separated or array values."
  [value]
  (cond
    (nil? value) []
    (sequential? value) (vec value)
    (string? value) (if (= value "")
                      []
                      (vec (.split value ",")))
    :else []))

(defn normalize-array
  "Format array as comma-separated string."
  [parsed-array]
  (when (seq parsed-array)
    (.join parsed-array ",")))

;; =====================================================
;; Property Setter Helper
;; =====================================================

(defn define-value-property!
  "Define a custom value property setter that triggers sync.
   This ensures programmatic updates also maintain transparency.
   
   Call this in component initialization."
  [^js el]
  (let [descriptor #js {:get (fn [] (.-__tyValue el))
                        :set (fn [v]
                               (when (not= v (.-__tyValue el))
                                 (sync-value! el v)
                                ;; Trigger render if component has render fn
                                 (when-let [render-fn (.-tyRender el)]
                                   (render-fn el))))
                        :enumerable true
                        :configurable true}]
    (js/Object.defineProperty el "value" descriptor)))
