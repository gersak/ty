(ns ty.components.input
  "Enhanced input component with numeric formatting via shadow values"
  (:require [ty.css :refer [ensure-styles!]]
            [ty.i18n :as i18n]
            [ty.i18n.number :as num]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load input styles from input.css
(defstyles input-styles)

;; =====================================================
;; Semantic Flavor Normalization
;; =====================================================

(defn validate-flavor
  "Validate that flavor uses new industry-standard semantic naming.
   For inputs, flavor typically indicates validation state or visual emphasis."
  [flavor]
  (let [valid-flavors #{"primary" "secondary" "success" "danger" "warning" "info" "neutral"}
        normalized (or flavor "neutral")]
    (when (and goog.DEBUG (not (contains? valid-flavors normalized)))
      (js/console.warn (str "[ty-input] Invalid flavor '" flavor "'. Using 'neutral'. "
                            "Valid flavors: primary, secondary, success, danger, warning, info, neutral.")))
    (if (contains? valid-flavors normalized)
      normalized
      "neutral")))

(defn get-validation-state
  "Determine validation state based on error attribute and flavor.
   Returns the appropriate semantic state for styling."
  [error flavor]
  (cond
    ;; Error attribute takes precedence
    (not-empty error) "danger"
    ;; Use validated flavor for validation states
    (#{"success" "danger" "warning" "info"} flavor) flavor
    ;; For other flavors, return neutral (no special validation styling)
    :else "neutral"))

;; =====================================================
;; Shadow Value State Management
;; =====================================================

;; =====================================================
;; Shadow Value State Management
;; =====================================================

(defn should-format?
  "Check if input type should have numeric formatting"
  [type]
  (contains? #{"number" "currency" "percent" "compact"} type))

;; =====================================================
;; PHASE 2: Smart Change Detection
;; =====================================================

(defn parse-shadow-value
  "Convert string value to shadow value (nil or number)"
  [value]
  (cond
    (or (nil? value) (= value "") (= value "")) nil
    (number? value) value
    (string? value) (let [parsed (js/parseFloat value)]
                      (if (js/isNaN parsed) nil parsed))
    :else nil))

(defn delta-value-matches-shadow?
  "Check if delta value matches current shadow value using simple string comparison.
   Much simpler approach - just stringify shadow and compare directly."
  [delta-value shadow-value]
  (let [shadow-str (if shadow-value (str shadow-value) "")]
    (= (str delta-value) shadow-str)))

(defn get-format-config
  "Extract formatting configuration from element attributes as JS object"
  [^js el]
  (let [type (wcs/attr el "type")
        currency (wcs/attr el "currency")
        locale (or (wcs/attr el "locale") (name i18n/*locale*))
        precision (when-let [p (wcs/attr el "precision")]
                    (js/parseInt p))]
    {:type (or type "text")
     :currency (or currency "USD")
     :locale locale
     :precision precision}))

(defn init-component-state!
  "Initialize component state from current value"
  [^js el]
  (let [initial-value (or (.-value el) (wcs/attr el "value"))
        shadow-value (parse-shadow-value initial-value)]
    (set! (.-tyInputState el)
          {:shadow-value shadow-value
           :last-external-value (or initial-value "")
           :is-focused false})
    ;; Mirror initial value upstream
    (if shadow-value
      (do
        (.setAttribute el "value" (str shadow-value))
        (set! (.-value el) shadow-value)
        ;; Sync initial form value
        (when (.-internals el)
          (.setFormValue (.-internals el) (str shadow-value))))
      (do
        (.removeAttribute el "value")
        (set! (.-value el) nil)
        ;; Clear initial form value
        (when (.-internals el)
          (.setFormValue (.-internals el) ""))))
    (.-tyInputState el)))

(defn get-component-state
  "Get component state (must be initialized first)"
  [^js el]
  (.-tyInputState el))

(defn update-component-state!
  "Update component state and mirror to value attribute"
  [^js el updates]
  (let [state (get-component-state el)
        new-state (merge state updates)]
    (set! (.-tyInputState el) new-state)

    ;; Mirror shadow value upstream to maintain transparency
    (when (contains? updates :shadow-value)
      (let [shadow-value (:shadow-value updates)]
        (if shadow-value
          (do
            (.setAttribute el "value" (str shadow-value))
            (set! (.-value el) shadow-value)
            ;; Sync with form internals (using string for form submission)
            (when (.-internals el)
              (.setFormValue (.-internals el) (str shadow-value))))
          (do
            (.removeAttribute el "value")
            (set! (.-value el) nil)
            ;; Clear form value when shadow value is nil
            (when (.-internals el)
              (.setFormValue (.-internals el) ""))))))

    new-state))

;; =====================================================
;; Formatting Functions
;; =====================================================

(defn format-shadow-value
  "Format shadow value according to type and config"
  [shadow-value {:keys [type currency locale precision]
                 :as config}]
  (when shadow-value
    (let [options (cond-> {}
                    precision (assoc :minimumFractionDigits precision
                                     :maximumFractionDigits precision))]
      (case type
        "currency" (num/format-currency shadow-value currency locale)
        "percent" (num/format-percent (/ shadow-value 100) locale) ; Divide by 100 for user-friendly percentage
        "compact" (num/format-compact shadow-value locale)
        "number" (num/format-number shadow-value locale options)
        (str shadow-value)))))

(defn get-display-value
  "Get the value that should be displayed in input"
  [^js el]
  (let [{:keys [shadow-value is-focused]
         :as state} (get-component-state el)
        format-config (get-format-config el)
        should-format (and (should-format? (:type format-config))
                           (not is-focused)
                           shadow-value)]
    (if should-format
      (format-shadow-value shadow-value format-config)
      (if shadow-value (str shadow-value) ""))))

;; =====================================================
;; Event Handling
;; =====================================================

(defn emit-value-events!
  "Emit custom input events with shadow and formatted values"
  [^js el ^js original-event]
  (let [{:keys [shadow-value]
         :as state} (get-component-state el)
        format-config (get-format-config el)
        formatted-value (when shadow-value
                          (format-shadow-value shadow-value format-config))
        data #js {:bubbles true
                  :composed true
                  :detail #js {:value shadow-value
                               :formattedValue formatted-value
                               :rawValue (.-value (.-target original-event))
                               :originalEvent original-event}}]

    (doseq [e ["input" "change"]]
      (.dispatchEvent el (js/CustomEvent. e data)))))

(defn handle-input-event
  "Handle input event - update shadow value"
  [^js el ^js e]
  (let [input-value (.-value (.-target e))
        shadow-value (parse-shadow-value input-value)]
    (update-component-state! el {:shadow-value shadow-value})
    (emit-value-events! el e)))

(defn handle-focus-event
  "Handle focus event - show raw shadow value"
  [^js el ^js e]
  (update-component-state! el {:is-focused true})
  (let [display-value (get-display-value el)
        input-el (.-target e)]
    (set! (.-value input-el) display-value)))

(defn handle-blur-event
  "Handle blur event - show formatted value"
  [^js el ^js e]
  (update-component-state! el {:is-focused false})
  (let [display-value (get-display-value el)
        input-el (.-target e)]
    (set! (.-value input-el) display-value))
  ;; WHY IS THIS HERE?
  #_(emit-value-events! el e))

;; =====================================================
;; Upstream Value Sync
;; =====================================================

(defn sync-external-value!
  "Sync external value changes with shadow value using simple string comparison"
  [^js el]
  (let [current-value (wcs/attr el "value")
        state (get-component-state el)
        current-shadow (:shadow-value state)]

    ;; ✅ Simple string comparison - only sync if different
    (when-not (delta-value-matches-shadow? current-value current-shadow)
      (let [new-shadow-value (parse-shadow-value current-value)]
        (update-component-state! el {:shadow-value new-shadow-value
                                     :last-external-value (or current-value "")})

        ;; Update input display
        (when-let [input-el (.querySelector (wcs/ensure-shadow el) "input")]
          (set! (.-value input-el) (get-display-value el)))
        (doseq [e ["input" "change"]]
          (.dispatchEvent
            el
            (js/CustomEvent. e
                             #js {:bubbles true
                                  :composed true
                                  :detail #js {:value new-shadow-value
                                               :formattedValue (format-shadow-value
                                                                 new-shadow-value
                                                                 (get-format-config el))
                                               :originalEvent nil}})))))))

;; =====================================================
;; Enhanced Attribute Reading
;; =====================================================

(defn input-attributes
  "Read all input attributes directly from element.
   Only accepts new industry-standard semantic flavors."
  [^js el]
  (let [raw-flavor (wcs/attr el "flavor")
        validated-flavor (validate-flavor raw-flavor)
        error (wcs/attr el "error")
        validation-state (get-validation-state error validated-flavor)]
    {:type (wcs/attr el "type")
     :value (get-display-value el) ; Use computed display value
     :placeholder (wcs/attr el "placeholder")
     :label (wcs/attr el "label")
     :disabled (wcs/parse-bool-attr el "disabled")
     :required (wcs/parse-bool-attr el "required")
     :error error
     :size (wcs/attr el "size")
     :flavor validated-flavor
     :validation-state validation-state ; Computed semantic validation state
     :class (wcs/attr el "class")
     ;; Numeric formatting attributes
     :currency (wcs/attr el "currency")
     :locale (wcs/attr el "locale")
     :precision (wcs/attr el "precision")}))

(defn build-class-list
  "Build class list from attributes with semantic validation state support.
   Uses normalized flavor and validation state for consistent styling."
  [{:keys [size flavor validation-state disabled required error class]}]
  (str (or size "md")
       " "
       (or flavor "neutral")
       " "
       ;; Add validation state class if it's a validation semantic
       (when (#{"success" "danger" "warning" "info"} validation-state)
         (str validation-state " "))
       (when disabled " disabled")
       (when required " required")
       (when error " error")
       (when class (str " " class))))

;; =====================================================
;; Component Rendering
;; =====================================================

(defn setup-input-events!
  "Setup enhanced event listeners for numeric inputs"
  [^js el ^js input-el]
  (let [format-config (get-format-config el)]
    (if (should-format? (:type format-config))
      ;; Enhanced events for numeric inputs
      (do
        (.addEventListener input-el "input" #(handle-input-event el %))
        (.addEventListener input-el "focus" #(handle-focus-event el %))
        (.addEventListener input-el "blur" #(handle-blur-event el %)))

      ;; Standard events for text inputs
      (do
        (.addEventListener input-el "input"
                           (fn [e]
                             (.dispatchEvent el (js/CustomEvent. "input"
                                                                 #js {:bubbles true
                                                                      :composed true
                                                                      :detail #js {:value (.-value (.-target e))
                                                                                   :originalEvent e}}))))
        (.addEventListener input-el "change"
                           (fn [e]
                             (.dispatchEvent el (js/CustomEvent. "change"
                                                                 #js {:bubbles true
                                                                      :composed true
                                                                      :detail #js {:value (.-value (.-target e))
                                                                                   :originalEvent e}}))))))))

(def required-icon
  "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\" class=\"lucide lucide-asterisk-icon lucide-asterisk\"><path d=\"M12 6v12\"/><path d=\"M17.196 9 6.804 15\"/><path d=\"m6.804 9 10.392 6\"/></svg>")

(defn render! [^js el]
  ;; Don't sync external value here - state is already initialized

  (let [{:keys [type value placeholder label disabled required error]
         :as attrs} (input-attributes el)
        root (wcs/ensure-shadow el)
        existing-label (.querySelector root "label")
        existing-input (.querySelector root "input")
        existing-error (.querySelector root ".error-message")]

    ;; Ensure styles are loaded
    (ensure-styles! root input-styles "ty-input")

    ;; Create or update structure
    (if (and existing-label existing-input)
      ;; Update existing elements
      (do
        ;; Update label with required icon
        (when label
          (set! (.-innerHTML existing-label)
                (str label (when required (str " <span class=\"required-icon\">" required-icon "</span>"))))
          (set! (.. existing-label -style -display) "flex")
          (set! (.. existing-label -style -alignItems) "center"))
        (when-not label
          (set! (.. existing-label -style -display) "none"))

        ;; Update input
        (set! (.-type existing-input) (if (#{"password" "currency" "date"} type)
                                        type
                                        "text"))
        (set! (.-value existing-input) (or value ""))
        (set! (.-placeholder existing-input) (or placeholder ""))
        (set! (.-disabled existing-input) disabled)
        (set! (.-required existing-input) required)
        (set! (.-className existing-input) (build-class-list attrs))

        ;; Update error message
        (if error
          (do
            (when-not existing-error
              (let [error-el (.createElement js/document "div")]
                (set! (.-className error-el) "error-message")
                (.appendChild root error-el)))
            (set! (.-textContent (.querySelector root ".error-message")) error))
          (when existing-error
            (.remove existing-error))))

      ;; Create new structure
      (let [container (.createElement js/document "div")
            label-el (.createElement js/document "label")
            input-el (.createElement js/document "input")]

        ;; Set up container
        (set! (.-className container) "input-container")

        ;; Set up label with required icon
        (set! (.-className label-el) "input-label")
        (when label
          (set! (.-innerHTML label-el)
                (str label (when required (str " <span class=\"required-icon\">" required-icon "</span>"))))
          (set! (.. label-el -style -display) "block"))
        (when-not label
          (set! (.. label-el -style -display) "none"))

        ;; Set up input
        (set! (.-type input-el) (or type "text"))
        (set! (.-value input-el) (or value ""))
        (set! (.-placeholder input-el) (or placeholder ""))
        (set! (.-disabled input-el) disabled)
        (set! (.-required input-el) required)
        (set! (.-className input-el) (build-class-list attrs))

        ;; Set up enhanced event listeners
        (setup-input-events! el input-el)

        ;; Add error message if present
        (when error
          (let [error-el (.createElement js/document "div")]
            (set! (.-className error-el) "error-message")
            (set! (.-textContent error-el) error)
            (.appendChild container error-el)))

        ;; Build structure
        (.appendChild container label-el)
        (.appendChild container input-el)
        (.appendChild root container)))

    el))

;; =====================================================
;; Web Component Definition
;; =====================================================

(wcs/define! "ty-input"
  {:observed [:type :name :value :placeholder :label :disabled :required :error
              :size :flavor :class :currency :locale :precision]
   :form-associated true ; ← Enable form participation  
   :props {:value nil} ; ← PHASE 1: Enable property watching
   :connected (fn [^js el]
               ;; Setup ElementInternals for form participation (before state init)
               ;; Only attach once - attachInternals() throws if called multiple times
               ;; More robust check for hot reloading scenarios
                (when (.-attachInternals el)
                  (try
                    ;; Only attach if we don't have internals yet
                    (when-not (.-internals el)
                      (set! (.-internals el) (.attachInternals el))
                      (.log js/console "[ty-input] ElementInternals attached successfully"))
                    (catch js/Error e
                      ;; If internals were already attached (hot reload scenario)
                      ;; Just log and continue - the element should still work
                      (.warn js/console "[ty-input] ElementInternals already attached (hot reload):" (.-message e)))))
               ;; Initialize state first
                (init-component-state! el)
               ;; Then render
                (render! el))
   :attr (fn [^js el delta]
           ;; delta = {"value" "123.45", "type" "currency", "disabled" "true", ...}
           (when (contains? delta "value")
             (let [new-value (get delta "value")
                   state (get-component-state el)
                   current-shadow (:shadow-value state)]
               ;; ✅ Simple string comparison - only update if different
               (when (and state
                          (not (delta-value-matches-shadow? new-value current-shadow)))
                 ;; It's a different external change - sync it
                 (let [shadow-value (parse-shadow-value new-value)]
                   ;; Update state directly (don't trigger attribute batch)
                   (set! (.-tyInputState el)
                         (merge state {:shadow-value shadow-value
                                       :last-external-value (or new-value "")}))

                   ;; ✅ Sync both attribute and property
                   (if shadow-value
                     (do
                       ;; Set both attribute and property to shadow value string
                       (.setAttribute el "value" (str shadow-value))
                       (set! (.-value el) shadow-value))
                     (do
                       ;; Remove both attribute and property when nil
                       (.removeAttribute el "value")
                       (set! (.-value el) nil)))

                   ;; Sync form internals
                   (when (.-internals el)
                     (.setFormValue (.-internals el) (if shadow-value (str shadow-value) "")))

                   ;; Update input display
                   (when-let [input-el (.querySelector (wcs/ensure-shadow el) "input")]
                     (set! (.-value input-el) (get-display-value el)))

                   ;; Emit events for the external change
                   (let [format-config (get-format-config el)
                         formatted-value (when shadow-value
                                           (format-shadow-value shadow-value format-config))]
                     (doseq [e ["input" "change"]]
                       (.dispatchEvent el
                                       (js/CustomEvent. e
                                                        #js {:bubbles true
                                                             :composed true
                                                             :detail #js {:value shadow-value
                                                                          :formattedValue formatted-value
                                                                          :originalEvent nil}}))))))))
           ;; For any other attributes in delta, re-render
           (when (seq (dissoc delta "value"))
             (render! el)))
   :prop (fn [^js el delta]
           ;; delta = {"value" 123.45, "disabled" true, ...}
           (when (contains? delta "value")
             (let [new-value (get delta "value")
                   state (get-component-state el)
                   current-shadow (:shadow-value state)]
               ;; ✅ Simple string comparison - only update if different
               (when (and state
                          (not (delta-value-matches-shadow? new-value current-shadow)))
                 (let [shadow-value (parse-shadow-value new-value)]
                   ;; Update state directly (avoid loops)
                   (set! (.-tyInputState el)
                         (merge state {:shadow-value shadow-value
                                       :last-external-value (str new-value)}))

                   ;; ✅ Sync both attribute and property  
                   (if shadow-value
                     (do
                       ;; Set both attribute and property to shadow value string
                       (.setAttribute el "value" (str shadow-value))
                       (set! (.-value el) shadow-value))
                     (do
                       ;; Remove both attribute and property when nil
                       (.removeAttribute el "value")
                       (set! (.-value el) nil)))

                   ;; Sync form internals
                   (when (.-internals el)
                     (.setFormValue (.-internals el) (if shadow-value (str shadow-value) "")))

                   ;; Update display if not focused
                   (when-not (:is-focused state)
                     (when-let [input-el (.querySelector (wcs/ensure-shadow el) "input")]
                       (set! (.-value input-el) (get-display-value el))))

                   ;; Emit change events
                   (let [format-config (get-format-config el)
                         formatted-value (when shadow-value
                                           (format-shadow-value shadow-value format-config))]
                     (doseq [e ["input" "change"]]
                       (.dispatchEvent el
                                       (js/CustomEvent. e
                                                        #js {:bubbles true
                                                             :composed true
                                                             :detail #js {:value shadow-value
                                                                          :formattedValue formatted-value
                                                                          :originalEvent nil}})))))))))})
