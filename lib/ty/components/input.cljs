(ns ty.components.input
  "Enhanced input component with numeric formatting via shadow values"
  (:require [clojure.string :as str]
            [ty.css :refer [ensure-styles!]]
            [ty.i18n :as i18n]
            [ty.i18n.number :as num]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load input styles from input.css
(defstyles input-styles)

(defn should-format?
  "Check if input type should have numeric formatting"
  [type]
  (contains? #{"number" "currency" "percent" "compact"} type))

(defn parse-shadow-value
  "Convert string value to appropriate shadow value based on input type"
  [value el]
  (case (wcs/attr el "type")
    ("number" "float" "integer" "percent" "currency")
    (let [parsed (js/parseFloat value)]
      (if (js/isNaN parsed) nil parsed))

    "checkbox"
    ;; For checkboxes, parse boolean values from various inputs
    (cond
      (boolean? value) value
      (string? value) (contains? #{"true" "1" "on" "checked"} (str/lower-case value))
      (number? value) (not (zero? value))
      :else (boolean value))

    value))

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

(defn get-internals
  "Get ElementInternals - they're already attached by shim.js as _internals"
  [^js el]
  (.-_internals el))

(defn init-component-state!
  "Initialize component state from current value - property sync only"
  [^js el]
  (let [input-type (wcs/attr el "type")
        initial-value (cond
                        ;; For checkboxes, prefer 'checked' attribute/property
                        (= input-type "checkbox")
                        (or (.-checked el)
                            (wcs/attr el "checked")
                            (.-value el)
                            (wcs/attr el "value")
                            false)

                        ;; For regular inputs, use value
                        :else
                        (or (.-value el) (wcs/attr el "value")))
        shadow-value (parse-shadow-value initial-value el)]
    (set! (.-tyInputState el)
          {:shadow-value shadow-value
           :last-external-value (or initial-value "")
           :is-focused false})
    (set! (.-value el) shadow-value)
    ;; For checkboxes, also set the checked property
    (when (= input-type "checkbox")
      (set! (.-checked el) (boolean shadow-value)))
    (when shadow-value
      (when-let [internals (get-internals el)]
        (.setFormValue internals shadow-value)))
    ;; NOTE: Don't sync with internals here - they may not be attached yet
    (.-tyInputState el)))

(declare get-component-state)

(defn get-component-state
  "Get component state (must be initialized first)"
  [^js el]
  (.-tyInputState el))

(defn update-component-state!
  "Update component state and sync with form internals only (no attribute manipulation)"
  [^js el updates]
  (let [state (get-component-state el)
        new-state (merge state updates)
        input-type (wcs/attr el "type")]
    (set! (.-tyInputState el) new-state)

    (when (contains? updates :shadow-value)
      (let [shadow-value (:shadow-value updates)]
        (when (not= (:shadow-value state) shadow-value)
          (set! (.-value el) shadow-value)
          ;; For checkboxes, also update checked property
          (when (= input-type "checkbox")
            (set! (.-checked el) (boolean shadow-value)))
          (if (some? shadow-value)
            (.setAttribute el "value" shadow-value)
            (.removeAttribute el "value"))
          ;; For checkboxes, also set checked attribute
          (when (= input-type "checkbox")
            (if shadow-value
              (.setAttribute el "checked" "")
              (.removeAttribute el "checked")))
          (when-let [internals (get-internals el)]
            (.setFormValue internals shadow-value)))))

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
        input-type (:type format-config)]

    (case input-type
      "checkbox"
      ;; For checkboxes, return boolean state (used internally)
      shadow-value

      ;; Regular input formatting logic
      (let [should-format (and (should-format? input-type)
                               (not is-focused)
                               shadow-value)]
        (if should-format
          (format-shadow-value shadow-value format-config)
          (if shadow-value (str shadow-value) ""))))))

;; =====================================================
;; Event Handling
;; =====================================================

(defn emit-value-events!
  "Emit custom input events with shadow and formatted values"
  [^js el ^js original-event]
  (.preventDefault original-event)
  (.stopPropagation original-event)
  (let [{:keys [shadow-value]
         :as state} (get-component-state el)
        format-config (get-format-config el)
        input-type (:type format-config)]

    (case input-type
      "checkbox"
      ;; Checkbox events - emit checked state
      (let [data #js {:bubbles true
                      :composed true
                      :detail #js {:value shadow-value
                                   :checked shadow-value ; For compatibility  
                                   :originalEvent original-event}}]
        (doseq [e ["input" "change"]]
          (.dispatchEvent el (js/CustomEvent. e data))))

      ;; Regular input events with formatting
      (let [formatted-value (when shadow-value
                              (format-shadow-value shadow-value format-config))
            data #js {:bubbles true
                      :composed true
                      :detail #js {:value shadow-value
                                   :formattedValue formatted-value
                                   :rawValue (.-value (.-target original-event))
                                   :originalEvent original-event}}]
        (doseq [e ["input" "change"]]
          (.dispatchEvent el (js/CustomEvent. e data)))))))

(defn get-input-type
  [^js event]
  (or
    (.. event -target -type)
    "text"))

(defn handle-input-event
  "Handle input event - update shadow value"
  [^js el ^js e]
  (let [input-value (.-value (.-target e))
        shadow-value (parse-shadow-value input-value el)]
    (update-component-state! el {:shadow-value shadow-value})
    (emit-value-events! el e)))

(defn handle-focus-event
  "Handle focus event - show raw shadow value"
  [^js el ^js e]
  (update-component-state! el {:is-focused true})
  (let [display-value (get-display-value el)
        input-el (.-target e)]
    (set! (.-value input-el) display-value)))

(defn handle-checkbox-click
  "Handle checkbox click event - toggle boolean state"
  [^js el ^js e]
  (.preventDefault e)
  (.stopPropagation e)
  (let [{:keys [shadow-value]} (get-component-state el)
        new-value (not shadow-value)]
    (update-component-state! el {:shadow-value new-value})
    (emit-value-events! el e)))

(defn handle-checkbox-keydown
  "Handle checkbox keyboard events - space/enter to toggle"
  [^js el ^js e]
  (when (contains? #{"Space" " " "Enter"} (.-key e))
    (.preventDefault e)
    (.stopPropagation e)
    (handle-checkbox-click el e)))

(defn handle-blur-event
  "Handle blur event - show formatted value"
  [^js el ^js e]
  (update-component-state! el {:is-focused false})
  (let [display-value (get-display-value el)
        input-el (.-target e)]
    (set! (.-value input-el) display-value)))

;; =====================================================
;; Enhanced Attribute Reading
;; =====================================================

(defn input-attributes
  "Read all input attributes directly from element.
   Only accepts new industry-standard semantic flavors."
  [^js el]
  (let [raw-flavor (wcs/attr el "flavor")
        error (wcs/attr el "error")
        validated-flavor (cond
                           (some? error) "danger"
                           (contains? #{"success" "warning" "danger" "primary" "secondary"} raw-flavor)
                           raw-flavor
                           :else "neutral")]
    {:type (wcs/attr el "type")
     :value (get-display-value el) ; Use computed display value
     :placeholder (wcs/attr el "placeholder")
     :label (wcs/attr el "label")
     :disabled (wcs/parse-bool-attr el "disabled")
     :required (wcs/parse-bool-attr el "required")
     :error error
     :size (wcs/attr el "size")
     :flavor validated-flavor
     :class (wcs/attr el "class")
     ;; Numeric formatting attributes
     :currency (wcs/attr el "currency")
     :locale (wcs/attr el "locale")
     :precision (wcs/attr el "precision")}))

(defn build-class-list
  "Build class list from attributes with semantic validation state support.
   Uses normalized flavor and validation state for consistent styling."
  [{:keys [size flavor disabled required error class]}]
  (str (or size "md")
       " "
       (or flavor "neutral")
       " "
       (when disabled " disabled")
       (when required " required")
       (when error " error")
       (when class (str " " class))))

;; =====================================================
;; Component Rendering
;; =====================================================

(defn setup-input-events!
  "Setup enhanced event listeners for all input types"
  [^js el ^js input-el]
  ;; Use unified event handling for all input types
  (.addEventListener input-el "change" #(handle-input-event el %))
  (.addEventListener input-el "input" #(handle-input-event el %))
  (.addEventListener input-el "focus" #(handle-focus-event el %))
  (.addEventListener input-el "blur" #(handle-blur-event el %)))

(defn setup-checkbox-events!
  "Setup event listeners for checkbox functionality"
  [^js el ^js checkbox-el]
  (.addEventListener checkbox-el "click" #(handle-checkbox-click el %))
  (.addEventListener checkbox-el "keydown" #(handle-checkbox-keydown el %))
  ;; Add focus/blur for accessibility
  (.addEventListener checkbox-el "focus" #(update-component-state! el {:is-focused true}))
  (.addEventListener checkbox-el "blur" #(update-component-state! el {:is-focused false})))

(def required-icon
  "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\" class=\"lucide lucide-asterisk-icon lucide-asterisk\"><path d=\"M12 6v12\"/><path d=\"M17.196 9 6.804 15\"/><path d=\"m6.804 9 10.392 6\"/></svg>")

;; Checkbox icons from Lucide
(def checkbox-unchecked-icon
  "<?xml version='1.0' encoding='UTF-8'?>
<svg stroke='currentColor' fill='none' stroke-linejoin='round' width='24' xmlns='http://www.w3.org/2000/svg' stroke-linecap='round' stroke-width='2' viewBox='0 0 24 24' height='24'>
<rect rx='2' y='3' x='3' height='18' width='18'/>
</svg>")

(def checkbox-checked-icon
  "<?xml version='1.0' encoding='UTF-8'?>
<svg stroke='currentColor' fill='none' stroke-linejoin='round' width='24' xmlns='http://www.w3.org/2000/svg' stroke-linecap='round' stroke-width='2' viewBox='0 0 24 24' height='24'>
<path d='M21 10.656V19a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h12.344'/>
<path d='m9 11 3 3L22 4'/>
</svg>")

(defn render! [^js el]
  (let [{:keys [type value placeholder label disabled required error]
         :as attrs} (input-attributes el)
        root (wcs/ensure-shadow el)
        existing-label (.querySelector root "label")
        existing-input (.querySelector root "input")
        existing-checkbox (.querySelector root ".checkbox-container")
        existing-error (.querySelector root ".error-message")
        is-checkbox (= type "checkbox")]

    ;; Ensure styles are loaded

    ;; Handle checkbox vs regular input rendering
    (if is-checkbox
      ;; === CHECKBOX RENDERING ===
      (if (and existing-label existing-checkbox)
        ;; Update existing checkbox elements
        (do
          ;; Update label
          (when label
            (set! (.-innerHTML existing-label)
                  (str label (when required (str " <span class=\"required-icon\">" required-icon "</span>"))))
            (set! (.. existing-label -style -display) "flex")
            (set! (.. existing-label -style -alignItems) "center"))
          (when-not label
            (set! (.. existing-label -style -display) "none"))

          ;; Update checkbox icon
          (let [checkbox-icon (.querySelector existing-checkbox ".checkbox-icon")]
            (set! (.-innerHTML checkbox-icon)
                  (if value checkbox-checked-icon checkbox-unchecked-icon))
            (set! (.-className existing-checkbox)
                  (str "checkbox-container " (build-class-list attrs)))
            ;; Set tabindex for keyboard accessibility
            (set! (.-tabIndex existing-checkbox) (if disabled -1 0))
            (.setAttribute existing-checkbox "role" "checkbox")
            (.setAttribute existing-checkbox "aria-checked" (str (boolean value)))
            (.setAttribute existing-checkbox "aria-disabled" (str disabled)))

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

        ;; Create new checkbox structure
        (let [container (.createElement js/document "div")
              label-el (.createElement js/document "label")
              checkbox-el (.createElement js/document "div")
              checkbox-icon (.createElement js/document "div")]

          ;; Set up container
          (set! (.-className container) "input-container")

          ;; Set up label
          (set! (.-className label-el) "checkbox-label")
          ;; Set up checkbox
          (set! (.-className checkbox-el) (str "checkbox-container " (build-class-list attrs)))
          (set! (.-className checkbox-icon) "checkbox-icon")
          (set! (.-innerHTML checkbox-icon)
                (if value checkbox-checked-icon checkbox-unchecked-icon))

          ;; Accessibility attributes
          (set! (.-tabIndex checkbox-el) (if disabled -1 0))
          (.setAttribute checkbox-el "role" "checkbox")
          (.setAttribute checkbox-el "aria-checked" (str (boolean value)))
          (.setAttribute checkbox-el "aria-disabled" (str disabled))

          ;; Setup checkbox events
          (setup-checkbox-events! el checkbox-el)

          ;; Append checkbox icon to container
          (.appendChild checkbox-el checkbox-icon)

          ;; Add error message if present
          (when error
            (let [error-el (.createElement js/document "div")]
              (set! (.-className error-el) "error-message")
              (set! (.-textContent error-el) error)
              (.appendChild container error-el)))

          ;; Build structure
          (when (some? label) (.appendChild checkbox-el label-el))
          (.appendChild container checkbox-el)
          (.appendChild root container)))

      ;; === REGULAR INPUT RENDERING ===
      (if (and existing-label existing-input)
        ;; Update existing regular input elements
        (do
          ;; Remove checkbox elements if switching from checkbox
          (when existing-checkbox
            (.remove existing-checkbox))

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

        ;; Create new regular input structure
        (let [container (.createElement js/document "div")
              label-el (.createElement js/document "label")
              input-el (.createElement js/document "input")]

          ;; Remove checkbox elements if switching from checkbox
          (when existing-checkbox
            (.remove existing-checkbox))

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
          (.appendChild root container))))

    el))

(defn- should-accept-value-change?
  "Determine if we should accept an external value change.
   Protects against spurious empty value changes while allowing legitimate updates."
  [new-value current-shadow-value el]
  (let [current-input-el (.querySelector (wcs/ensure-shadow el) "input")
        current-dom-value (when current-input-el (.-value current-input-el))
        is-focused (and current-input-el (= current-input-el (.-activeElement js/document)))
        is-empty-override (and (some? current-shadow-value)
                               (or (nil? new-value) (= "" new-value)))]

    ;; Accept value changes unless they're suspicious empty overrides during user interaction
    (not (and is-empty-override is-focused))))

(defn- enrich-delta
  [{:strs [value checked]
    :as delta} el]
  (let [{:keys [shadow-value]} (get-component-state el)
        input-type (wcs/attr el "type")]

    ;; Handle checkbox 'checked' attribute
    (cond
      (and (= input-type "checkbox") (contains? delta "checked"))
      (let [parsed-checked (parse-shadow-value checked el)]
        (cond
          ;; Value hasn't changed - no update needed
          (= shadow-value parsed-checked)
          delta

          ;; Accept legitimate value changes
          :else
          (assoc delta :shadow-value parsed-checked)))

      ;; Handle regular 'value' attribute
      (contains? delta "value")
      (let [parsed-value (parse-shadow-value value el)]
        (cond
          ;; Value hasn't changed - no update needed
          (= shadow-value parsed-value)
          delta

          ;; Protect against suspicious empty value changes (for regular inputs)
          (and (not= input-type "checkbox")
               (not (should-accept-value-change? parsed-value shadow-value el)))
          (do
            (js/console.warn "🛡️ Ignoring suspicious empty value change. Current:" shadow-value "Attempted:" parsed-value)
            (dissoc delta "value")) ; Remove value from delta to prevent update

          ;; Accept legitimate value changes
          :else
          (assoc delta :shadow-value parsed-value)))

      :else delta)))

;; =====================================================
;; Web Component Definition
;; =====================================================

(def configuration
  {:observed [:type :name :value :placeholder :label :disabled :required :error
              :size :flavor :class :currency :locale :precision :checked]
   :form-associated true
   :props {:value nil
           :checked nil}
   :connected (fn [^js el]

                (ensure-styles! (wcs/ensure-shadow el) input-styles "ty-input")
                ;; Initialize state first
                (init-component-state! el)

                ;; Render the component
                (render! el))
   :attr (fn [^js el delta]
           (let [delta' (enrich-delta delta el)]
             (update-component-state! el delta')
             (when (or
                     (seq (dissoc delta "value"))
                     (not= delta delta'))
               (render! el))))
   :prop (fn [^js el delta]
           (let [delta' (enrich-delta delta el)]
             (update-component-state! el delta')
             (when (or
                     (seq (dissoc delta "value"))
                     (not= delta delta'))
               (render! el))))})

(wcs/define! "ty-input" configuration)
