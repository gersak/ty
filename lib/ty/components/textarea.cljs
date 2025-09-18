(ns ty.components.textarea
  "Enhanced textarea component with auto-resize functionality"
  (:require [clojure.string :as str]
            [goog.object :as gobj]
            [ty.components.input :refer [required-icon]]
            [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load textarea styles from textarea.css
(defstyles textarea-styles)

(defn init-component-state!
  "Initialize component state for textarea"
  [^js el]
  (let [initial-value (or (.-value el) (wcs/attr el "value") "")]
    (set! (.-tyTextareaState el)
          {:value initial-value
           :is-focused false
           :dummy-style nil})
    (set! (.-value el) initial-value)
    (.-tyTextareaState el)))

(defn get-component-state
  "Get component state (must be initialized first)"
  [^js el]
  (.-tyTextareaState el))

(defn update-component-state!
  "Update component state"
  [^js el updates]
  (let [state (get-component-state el)
        new-state (merge state updates)]
    (set! (.-tyTextareaState el) new-state)
    new-state))

(defn get-font-style
  "Extract font-related CSS properties from element"
  [^js el]
  (let [computed-style (.getComputedStyle js/window el)]
    {:font-family (.-fontFamily computed-style)
     :font-size (.-fontSize computed-style)
     :font-weight (.-fontWeight computed-style)
     :line-height (.-lineHeight computed-style)
     :letter-spacing (.-letterSpacing computed-style)
     :font-style (.-fontStyle computed-style)}))

(defn get-spacing-style
  "Extract spacing-related CSS properties from element"
  [^js el]
  (let [computed-style (.getComputedStyle js/window el)]
    {:padding (.-padding computed-style)
     :padding-top (.-paddingTop computed-style)
     :padding-right (.-paddingRight computed-style)
     :padding-bottom (.-paddingBottom computed-style)
     :padding-left (.-paddingLeft computed-style)
     :margin (.-margin computed-style)
     :border-width (.-borderWidth computed-style)}))

(defn setup-dummy-element!
  "Setup the hidden dummy element for height measurement"
  [^js el ^js textarea-el]
  (let [root (wcs/ensure-shadow el)
        existing-dummy (.querySelector root ".textarea-dummy")]

    ;; Remove existing dummy if present
    (when existing-dummy
      (.remove existing-dummy))

    ;; Create new dummy element
    (let [dummy-el (.createElement js/document "pre")
          font-style (get-font-style textarea-el)
          spacing-style (get-spacing-style textarea-el)]

      ;; Set dummy element attributes
      (set! (.-className dummy-el) "textarea-dummy")

      ;; Apply styles to match textarea
      (let [style-obj (.-style dummy-el)]
        (set! (.-position style-obj) "absolute")
        (set! (.-top style-obj) "0")
        (set! (.-left style-obj) "0")
        (set! (.-visibility style-obj) "hidden")
        (set! (.-whiteSpace style-obj) "pre-wrap")
        (set! (.-wordBreak style-obj) "break-word")
        (set! (.-boxSizing style-obj) "border-box")
        (set! (.-overflow style-obj) "hidden")
        (set! (.-pointerEvents style-obj) "none")

        ;; Apply font styles
        (doseq [[prop value] font-style]
          (gobj/set style-obj (name prop) value))

        ;; Apply spacing styles  
        (doseq [[prop value] spacing-style]
          (gobj/set style-obj (name prop) value)))

      ;; Append to shadow root
      (.appendChild root dummy-el)

      ;; Store dummy style in state
      (update-component-state! el {:dummy-style (merge font-style spacing-style)})

      dummy-el)))

(defn resize-textarea!
  "Resize textarea based on dummy element content with min/max height constraints"
  [^js el ^js textarea-el ^js dummy-el]
  (when (and textarea-el dummy-el)
    (let [{:keys [value is-focused]} (get-component-state el)
          placeholder (wcs/attr el "placeholder")
          min-height-attr (wcs/attr el "min-height")
          max-height-attr (wcs/attr el "max-height")
          content (if (and (str/blank? value) placeholder)
                    placeholder
                    (str value " ")) ; Add space to prevent layout shift
          current-height (.-clientHeight textarea-el)]

      ;; Set dummy content
      (set! (.-textContent dummy-el) content)

      ;; Set dummy width to match textarea
      (set! (.. dummy-el -style -width) (str (.-clientWidth textarea-el) "px"))

      ;; Get measured height
      (let [measured-height (.-scrollHeight dummy-el)
            ;; Parse min/max heights (remove 'px' suffix and convert to number)
            min-height (when min-height-attr
                         (js/parseInt (str/replace min-height-attr "px" "") 10))
            max-height (when max-height-attr
                         (js/parseInt (str/replace max-height-attr "px" "") 10))
            ;; Apply constraints
            constrained-height (cond-> measured-height
                                 min-height (max min-height)
                                 max-height (min max-height))]

        ;; Only update if height changed significantly (avoid micro-adjustments)
        (when (> (js/Math.abs (- constrained-height current-height)) 2)
          (set! (.. textarea-el -style -height) (str constrained-height "px"))

          ;; Set overflow based on whether we hit max-height
          (if (and max-height (>= measured-height max-height))
            (set! (.. textarea-el -style -overflowY) "auto")
            (set! (.. textarea-el -style -overflowY) "hidden")))))))

;; Enhanced performance version with debouncing
(defn resize-textarea-optimized!
  "Optimized resize with debouncing and RAF"
  [^js el ^js textarea-el ^js dummy-el]
  (when (and textarea-el dummy-el)
    (let [{:keys [value resize-timeout]} (get-component-state el)]

      ;; Clear existing timeout
      (when resize-timeout
        (js/clearTimeout resize-timeout))

      ;; Debounce resize calculations (100ms)
      (let [timeout-id
            (js/setTimeout
              (fn []
                ;; Use RAF for smooth height updates
                (js/requestAnimationFrame
                  (fn []
                    (let [content (str value " ")
                          current-height (.-clientHeight textarea-el)]

                      ;; Only proceed if textarea is visible
                      (when (> (.-offsetHeight textarea-el) 0)
                        ;; Set dummy content and width
                        (set! (.-textContent dummy-el) content)
                        (set! (.. dummy-el -style -width)
                              (str (.-clientWidth textarea-el) "px"))

                        ;; Measure and update if significant change
                        (let [measured-height (.-scrollHeight dummy-el)]
                          (when (> (js/Math.abs (- measured-height current-height)) 2)
                            (set! (.. textarea-el -style -height)
                                  (str measured-height "px")))))))))
              100)] ; 100ms debounce

        ;; Store timeout ID for cleanup
        (update-component-state! el {:resize-timeout timeout-id})))))

(defn emit-value-events!
  "Emit custom input and change events while allowing standard events to bubble"
  [^js el ^js original-event]
  (let [{:keys [value]} (get-component-state el)]

    ;; Update form internals for proper form submission
    (when-let [internals (.-_internals el)]
      (.setFormValue internals (or value "")))

    ;; Emit custom events as enhancements (don't prevent default)
    (let [data #js {:bubbles true
                    :composed true
                    :detail #js {:value value
                                 :originalEvent original-event}}]
      ;; Use setTimeout to ensure these fire after standard events
      (js/setTimeout
        (fn []
          (doseq [event-type ["input" "change"]]
            (.dispatchEvent el (js/CustomEvent. event-type data))))
        0))))

(defn handle-input-event
  "Handle textarea input event"
  [^js el ^js e]
  (let [new-value (.-value (.-target e))]
    (update-component-state! el {:value new-value})

    ;; Trigger resize after state update
    (let [root (wcs/ensure-shadow el)
          textarea-el (.querySelector root "textarea")
          dummy-el (.querySelector root ".textarea-dummy")]
      (resize-textarea! el textarea-el dummy-el))

    ;; Emit custom events
    (emit-value-events! el e)))

(defn handle-focus-event
  "Handle textarea focus event"
  [^js el ^js e]
  (update-component-state! el {:is-focused true}))

(defn handle-blur-event
  "Handle textarea blur event"
  [^js el ^js e]
  (update-component-state! el {:is-focused false}))

(defn textarea-attributes
  "Read all textarea attributes directly from element"
  [^js el]
  (let [{:keys [value]} (get-component-state el)]
    {:value (or value "")
     :name (wcs/attr el "name")
     :placeholder (wcs/attr el "placeholder")
     :label (wcs/attr el "label")
     :disabled (wcs/parse-bool-attr el "disabled")
     :required (wcs/parse-bool-attr el "required")
     :error (wcs/attr el "error")
     :size (wcs/attr el "size")
     :class (wcs/attr el "class")
     :rows (wcs/attr el "rows")
     :cols (wcs/attr el "cols")
     :resize (wcs/attr el "resize")
     :min-height (wcs/attr el "min-height")
     :max-height (wcs/attr el "max-height")}))

(defn build-class-list
  "Build class list from attributes"
  [{:keys [size disabled required error class resize]}]
  (str (or size "md")
       (when disabled " disabled")
       (when required " required")
       (when error " error")
       (when resize (str " resize-" resize))
       (when class (str " " class))))

(defn setup-textarea-events!
  "Setup event listeners for textarea"
  [^js el ^js textarea-el]
  (.addEventListener textarea-el "input" #(handle-input-event el %))
  (.addEventListener textarea-el "change" #(handle-input-event el %))
  (.addEventListener textarea-el "focus" #(handle-focus-event el %))
  (.addEventListener textarea-el "blur" #(handle-blur-event el %)))

(defn apply-height-constraints!
  "Apply min/max height constraints to textarea element"
  [^js el ^js textarea-el]
  (let [min-height-attr (wcs/attr el "min-height")
        max-height-attr (wcs/attr el "max-height")]
    (when min-height-attr
      (set! (.. textarea-el -style -minHeight) min-height-attr))
    (when max-height-attr
      (set! (.. textarea-el -style -maxHeight) max-height-attr))))

(defn render! [^js el]
  (let [{:keys [value placeholder label disabled required error size rows cols resize name]
         :as attrs} (textarea-attributes el)
        root (wcs/ensure-shadow el)
        existing-container (.querySelector root ".textarea-container")
        existing-label (.querySelector root "label")
        existing-textarea (.querySelector root "textarea")
        existing-error (.querySelector root ".error-message")]

    (if (and existing-container existing-textarea)
      ;; Update existing elements
      (do
        ;; Update label
        (when label
          (set! (.-innerHTML existing-label)
                (str label (when required (str " <span class=\"required-icon\">" required-icon "</span>"))))
          (set! (.. existing-label -style -display) "block"))
        (when-not label
          (set! (.. existing-label -style -display) "none"))

        ;; Update textarea
        (set! (.-value existing-textarea) (or value ""))
        (set! (.-placeholder existing-textarea) (or placeholder ""))
        (set! (.-disabled existing-textarea) disabled)
        (set! (.-required existing-textarea) required)
        (set! (.-className existing-textarea) (build-class-list attrs))

        ;; Set name for form association and HTMX compatibility
        (if name
          (.setAttribute existing-textarea "name" name)
          (.removeAttribute existing-textarea "name"))

        ;; Set rows/cols if specified
        (when rows (.setAttribute existing-textarea "rows" rows))
        (when cols (.setAttribute existing-textarea "cols" cols))

        ;; Apply height constraints
        (apply-height-constraints! el existing-textarea)

        ;; Update error message

        ;; Update error message

        ;; Update error message

        ;; Update error message
        (if error
          (do
            (when-not existing-error
              (let [error-el (.createElement js/document "div")]
                (set! (.-className error-el) "error-message")
                (.appendChild existing-container error-el)))
            (set! (.-textContent (.querySelector root ".error-message")) error))
          (when existing-error
            (.remove existing-error)))

        ;; Setup dummy element and trigger resize
        (let [dummy-el (setup-dummy-element! el existing-textarea)]
          (js/setTimeout #(resize-textarea! el existing-textarea dummy-el) 0)))

      ;; Create new structure
      (let [container (.createElement js/document "div")
            label-el (.createElement js/document "label")
            textarea-el (.createElement js/document "textarea")]

        ;; Set up container
        (set! (.-className container) "textarea-container")

        ;; Set up label with required icon
        (set! (.-className label-el) "textarea-label")
        (when label
          (set! (.-innerHTML label-el)
                (str label (when required (str " <span class=\"required-icon\">" required-icon "</span>"))))
          (set! (.. label-el -style -display) "block"))
        (when-not label
          (set! (.. label-el -style -display) "none"))

        ;; Set up textarea
        (set! (.-value textarea-el) (or value ""))
        (set! (.-placeholder textarea-el) (or placeholder ""))
        (set! (.-disabled textarea-el) disabled)
        (set! (.-required textarea-el) required)
        (set! (.-className textarea-el) (build-class-list attrs))

        ;; Set name for form association and HTMX compatibility
        (when name
          (.setAttribute textarea-el "name" name))

        ;; Set default or specified rows/cols
        (.setAttribute textarea-el "rows" (or rows "3"))
        (when cols (.setAttribute textarea-el "cols" cols))

        ;; Set up event listeners
        (setup-textarea-events! el textarea-el)

        ;; Add error message if present
        (when error
          (let [error-el (.createElement js/document "div")]
            (set! (.-className error-el) "error-message")
            (set! (.-textContent error-el) error)
            (.appendChild container error-el)))

        ;; Build structure
        (.appendChild container label-el)
        (.appendChild container textarea-el)
        (.appendChild root container)

        ;; Setup dummy element and trigger initial resize
        (let [dummy-el (setup-dummy-element! el textarea-el)]
          (js/setTimeout #(resize-textarea! el textarea-el dummy-el) 0)))))

  el)

(defn- enrich-delta
  "Enrich attribute/property delta with parsed values"
  [{:strs [value]
    :as delta} el]
  (cond
    ;; Handle value changes
    (contains? delta "value")
    (assoc delta :value value)

    :else delta))

;; =====================================================
;; Web Component Definition
;; =====================================================

(def configuration
  {:observed [:name :value :placeholder :label :disabled :required :error
              :size :class :rows :cols :resize :min-height :max-height]
   :form-associated true
   :props {:value nil}
   :connected (fn [^js el]
                (ensure-styles! (wcs/ensure-shadow el) textarea-styles "ty-textarea")
                ;; Initialize state first
                (init-component-state! el)

                ;; Set up form value synchronization for HTMX compatibility
                (let [{:keys [value]} (get-component-state el)]
                  (set! (.-value el) (or value ""))
                  ;; Sync with form internals if available
                  (when-let [internals (.-_internals el)]
                    (.setFormValue internals (or value ""))))

                ;; Render the component
                (render! el))
   :attr (fn [^js el delta]
           (let [delta' (enrich-delta delta el)]
             (update-component-state! el delta')
             ;; Sync form value when value changes
             (when (contains? delta' :value)
               (let [{:keys [value]} (get-component-state el)]
                 (set! (.-value el) (or value ""))
                 (when-let [internals (.-_internals el)]
                   (.setFormValue internals (or value "")))))
             (render! el)))
   :prop (fn [^js el delta]
           (let [delta' (enrich-delta delta el)]
             (update-component-state! el delta')
             ;; Sync form value when value changes  
             (when (contains? delta' :value)
               (let [{:keys [value]} (get-component-state el)]
                 (set! (.-value el) (or value ""))
                 (when-let [internals (.-_internals el)]
                   (.setFormValue internals (or value "")))))
             (render! el)))})

(wcs/define! "ty-textarea" configuration)
