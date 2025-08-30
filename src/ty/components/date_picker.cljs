(ns ty.components.date-picker
  "Date picker component - read-only input with calendar dropdown"
  (:require [cljs-bean.core :refer [->clj ->js]]
            [ty.css :refer [ensure-styles!]]
            [ty.date.core :as date]
            [ty.i18n.time :as time]
            [ty.shim :as wcs]
            [ty.value :as value])
  (:require-macros [ty.css :refer [defstyles]]))

;; Calendar icon SVG for the input
(def calendar-icon-svg
  "<svg stroke='currentColor' fill='none' stroke-width='2' viewBox='0 0 24 24' width='16' height='16' xmlns='http://www.w3.org/2000/svg'><rect x='3' y='4' width='18' height='18' rx='2' ry='2'></rect><line x1='16' y1='2' x2='16' y2='6'></line><line x1='8' y1='2' x2='8' y2='6'></line><line x1='3' y1='10' x2='21' y2='10'></line></svg>")

;; Clear icon SVG
(def clear-icon-svg
  "<svg stroke='currentColor' fill='none' stroke-width='2' viewBox='0 0 24 24' width='14' height='14' xmlns='http://www.w3.org/2000/svg'><line x1='18' y1='6' x2='6' y2='18'></line><line x1='6' y1='6' x2='18' y2='18'></line></svg>")

;; Required icon (same as input component)
(def required-icon
  "<span class=\"required-icon\">*</span>")

;; Load date picker styles
(defstyles date-picker-styles)

(declare render!)

;; Mobile detection
 ;; Create day-classes function for selection styling (for ty-date-picker)
;; Create day-classes function for selection styling (for ty-date-picker)
(defn create-date-picker-day-classes
  "Create a day-classes function that adds 'selected' class for the selected date"
  [selected-value]
  (fn [^js day-context]
    (let [context (->clj day-context)
          base-classes (cond-> ["calendar-day"]
                         (:today? context) (conj "today")
                         (:weekend context) (conj "weekend")
                         (:other-month context) (conj "other-month")
                         ;; Add selected class if this day matches the selected value
                         (and selected-value (= (:value context) selected-value)) (conj "selected"))]
      (->js base-classes))))

;; Set up global function for date picker selection styling
(defn setup-date-picker-day-classes!
  "Set up global day-classes function for a specific date picker instance"
  [^js el selected-value]
  (let [instance-id (str "datePicker_" (.-tyDatePickerId el))
        fn-name (str "datePickerDayClasses_" (.-tyDatePickerId el))]
    ;; Create unique function for this instance
    (aset js/window fn-name (create-date-picker-day-classes selected-value))
    fn-name))

(defn is-mobile? []
  (or (< (.-innerWidth js/window) 768)
      (.-maxTouchPoints js/navigator)))

;; Component state management
(defn init-component-state!
  "Initialize date picker state"
  [^js el]
  (let [initial-value (or (value/get-attribute el "value")
                          (.-value el))
        parsed-value (when (and initial-value (not= initial-value ""))
                       (date/parse-value initial-value))
        initial-state {:value parsed-value
                       :open? false
                       :formatted-value nil}]

    ;; Create unique ID for this instance (for day-classes function)
    (when-not (.-tyDatePickerId el)
      (set! (.-tyDatePickerId el) (str (random-uuid))))

    ;; Set internal state
    (set! (.-tyDatePickerState el) initial-state)

    ;; Set .value property for JS compatibility
    (set! (.-value el) parsed-value)

    initial-state))

(defn get-component-state
  "Get existing component state"
  [^js el]
  (or (.-tyDatePickerState el)
      (init-component-state! el)))

(defn update-component-state!
  "Update component state"
  [^js el updates]
  (let [current-state (get-component-state el)
        new-state (merge current-state updates)]
    (set! (.-tyDatePickerState el) new-state)
    new-state))

;; Date formatting
(defn format-display-value
  "Format date value for display in input"
  [value format-type locale]
  (when value
    (let [date (js/Date. value)
          locale-str (or locale "en-US")]
      (case format-type
        "short" (time/format-date-short date locale-str)
        "medium" (time/format-date-medium date locale-str)
        "long" (time/format-date-long date locale-str)
        "full" (time/format-date-full date locale-str)
        ;; Default to short
        (time/format-date-short date locale-str)))))

;; Event handling
(defn emit-change-event!
  "Emit change event when value changes"
  [^js el new-value source]
  (let [event-detail #js {:value new-value
                          :source source ; "selection" | "clear" | "external"
                          :formatted (when new-value
                                       (format-display-value new-value
                                                             (or (value/get-attribute el "format") "short")
                                                             (or (value/get-attribute el "locale") "en-US")))}
        event (js/CustomEvent. "change"
                               #js {:detail event-detail
                                    :bubbles true
                                    :cancelable true})]
    ;; Update .value property for JS compatibility
    (set! (.-value el) new-value)

    ;; Dispatch event
    (.dispatchEvent el event)))

(defn emit-open-event!
  "Emit dropdown open event"
  [^js el]
  (let [event (js/CustomEvent. "open" #js {:bubbles true})]
    (.dispatchEvent el event)))

(defn emit-close-event!
  "Emit dropdown close event"
  [^js el]
  (let [event (js/CustomEvent. "close" #js {:bubbles true})]
    (.dispatchEvent el event)))

;; Dropdown management
(defn close-dropdown!
  "Close the calendar dropdown"
  [^js el]
  (let [state (get-component-state el)]
    (when (:open? state)
      (update-component-state! el {:open? false})
      (emit-close-event! el)
      (render! el))))

(defn position-dropdown!
  "Position the dropdown relative to input stub"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        stub (.querySelector root ".date-picker-stub")
        dropdown (.querySelector root ".calendar-dropdown")]
    (when (and stub dropdown)
      (let [stub-rect (.getBoundingClientRect stub)
            stub-bottom (.-bottom stub-rect)
            stub-left (.-left stub-rect)
            stub-width (.-width stub-rect)
            viewport-height (.-innerHeight js/window)
            space-below (- viewport-height stub-bottom)
            position-below (> space-below 400)
            dropdown-width (max stub-width 320)]

        (set! (.-style dropdown)
              (str "position: fixed;"
                   "left: " stub-left "px;"
                   (if position-below
                     (str "top: " (+ stub-bottom 4) "px;")
                     (str "bottom: " (+ (- viewport-height (.-top stub-rect)) 4) "px;"))
                   "width: " dropdown-width "px;"
                   "z-index: 1000;"))

        (.remove (.-classList dropdown) "position-above" "position-below")
        (.add (.-classList dropdown) (if position-below "position-below" "position-above"))))))

(defn open-dropdown!
  "Open the calendar dropdown"
  [^js el]
  (let [state (get-component-state el)]
    (when-not (:open? state)
      (update-component-state! el {:open? true})
      (emit-open-event! el)
      (render! el)
      ;; Position dropdown after render
      (.requestAnimationFrame js/window (fn [] (position-dropdown! el))))))

;; Input attributes parsing
(defn get-input-attributes
  "Parse input-related attributes"
  [^js el]
  (let [size (or (value/get-attribute el "size") "md")
        flavor (or (value/get-attribute el "flavor") nil)
        label (value/get-attribute el "label")
        placeholder (value/get-attribute el "placeholder")
        required (= (value/get-attribute el "required") "true")
        disabled (= (value/get-attribute el "disabled") "true")
        clearable (= (value/get-attribute el "clearable") "true")
        format-type (or (value/get-attribute el "format") "short")
        locale (or (value/get-attribute el "locale") "en-US")
        state (get-component-state el)
        formatted-value (when (:value state)
                          (format-display-value (:value state) format-type locale))]

    {:size size
     :flavor flavor
     :label label
     :placeholder placeholder
     :required required
     :disabled disabled
     :clearable clearable
     :format-type format-type
     :locale locale
     :value (:value state)
     :formatted-value formatted-value
     :open? (:open? state)}))

;; Event handlers
(defn handle-stub-click!
  "Handle click on date picker stub"
  [^js el ^js event]
  (.preventDefault event)
  (when-not (= (value/get-attribute el "disabled") "true")
    (open-dropdown! el)))

(defn handle-clear-click!
  "Handle clear button click"
  [^js el ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (update-component-state! el {:value nil})
  (emit-change-event! el nil "clear")
  (render! el))

(defn handle-calendar-change!
  "Handle date selection from ty-calendar (now listens to day-click events)"
  [^js el ^js event]
  (let [detail (.-detail event)
        selected-value (.-value detail)]
    ;; Update state with selected date
    (update-component-state! el {:value selected-value})
    ;; Emit change event
    (emit-change-event! el selected-value "selection")
    ;; Close dropdown
    (close-dropdown! el)))

(defn handle-outside-click!
  "Handle clicks outside the component"
  [^js el ^js event]
  (let [root (wcs/ensure-shadow el)
        target (.-target event)]
    ;; Check if click is outside our component
    (when-not (.contains el target)
      (close-dropdown! el))))

(defn handle-escape-key!
  "Handle escape key to close dropdown"
  [^js el ^js event]
  (when (= (.-key event) "Escape")
    (.preventDefault event)
    (close-dropdown! el)))

;; Build CSS classes for stub (following dropdown pattern)
(defn build-stub-classes
  "Build CSS class list for date picker stub"
  [{:keys [size flavor disabled required open?]}]
  (let [base-classes ["date-picker-stub"]]
    (cond-> base-classes
      size (conj size)
      flavor (conj flavor)
      disabled (conj "disabled")
      required (conj "required")
      open? (conj "open"))))

;; Render date picker stub (input-like element)
(defn render-date-picker-stub
  "Render the date picker stub (following dropdown pattern)"
  [^js el attrs]
  (let [{:keys [placeholder formatted-value disabled clearable open?]} attrs
        stub (.createElement js/document "div")
        display-text (.createElement js/document "span")
        icon-container (.createElement js/document "div")
        calendar-icon (.createElement js/document "span")
        clear-button (.createElement js/document "button")]

    ;; Set up stub (like dropdown-stub)
    (set! (.-className stub)
          (->> (build-stub-classes attrs)
               (clojure.string/join " ")))
    (when disabled
      (.setAttribute stub "disabled" "true"))

    ;; Set up display text
    (set! (.-className display-text) "stub-text")
    (set! (.-textContent display-text)
          (or formatted-value placeholder "Select date..."))
    (when-not formatted-value
      (.add (.-classList display-text) "placeholder"))

    ;; Set up icon container
    (set! (.-className icon-container) "stub-icons")

    ;; Set up clear button (only show if clearable and has value)
    (when (and clearable formatted-value (not disabled))
      (set! (.-className clear-button) "stub-clear")
      (set! (.-innerHTML clear-button) clear-icon-svg)
      (set! (.-type clear-button) "button")
      (.addEventListener clear-button "click" #(handle-clear-click! el %))
      (.appendChild icon-container clear-button))

    ;; Set up calendar icon
    (set! (.-className calendar-icon) "stub-arrow")
    (set! (.-innerHTML calendar-icon) calendar-icon-svg)
    (.appendChild icon-container calendar-icon)

    ;; Set up click event
    (.addEventListener stub "click" #(handle-stub-click! el %))

    ;; Build structure
    (.appendChild stub display-text)
    (.appendChild stub icon-container)

    stub))

;; Render container structure (following dropdown pattern)
(defn render-container-structure
  "Render the complete container structure"
  [^js el attrs]
  (let [{:keys [label required]} attrs
        container (.createElement js/document "div")
        label-el (.createElement js/document "label")]

    ;; Set up container (like dropdown-container)
    (set! (.-className container) "dropdown-container")

    ;; Set up label (like dropdown-label)
    (set! (.-className label-el) "dropdown-label")
    (if label
      (do
        (set! (.-innerHTML label-el)
              (str label (when required required-icon)))
        (.appendChild container label-el))
      ;; No label - skip it
      nil)

    container))

;; Render calendar dropdown
(defn render-calendar-dropdown
  "Render the calendar dropdown using ty-calendar with date picker styling"
  [^js el attrs]
  (let [{:keys [open? value locale]} attrs]
    (if open?
      (let [dropdown (.createElement js/document "div")
            calendar (.createElement js/document "ty-calendar")]

        ;; Set up dropdown container
        (set! (.-className dropdown) "calendar-dropdown")

        ;; Set up ty-calendar with date picker's value and styling
        (when value
          (.setAttribute calendar "value" value))
        (when locale
          (.setAttribute calendar "locale" locale))

        ;; Set up day-classes function for selection styling
        (let [day-classes-fn-name (setup-date-picker-day-classes! el value)]
          (.setAttribute calendar "day-classes-fn" day-classes-fn-name))

        ;; Event listener for day-click from ty-calendar
        (.addEventListener calendar "day-click" #(handle-calendar-change! el %))

        ;; Build structure
        (.appendChild dropdown calendar)
        dropdown)

      ;; Return empty div when closed
      (let [dropdown (.createElement js/document "div")]
        (set! (.-className dropdown) "calendar-dropdown hidden")
        dropdown))))

;; Main render function
(defn render!
  "Render the date picker component"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        attrs (get-input-attributes el)]

    ;; Ensure styles are loaded
    (ensure-styles! root date-picker-styles "ty-date-picker")

    ;; Update day-classes function when value changes (for calendar styling)
    (when (:open? attrs)
      (setup-date-picker-day-classes! el (:value attrs)))

    ;; Clear and rebuild
    (set! (.-innerHTML root) "")

    ;; Create container structure
    (let [container (render-container-structure el attrs)
          wrapper (.createElement js/document "div")]

      ;; Set up wrapper (like dropdown-wrapper)
      (set! (.-className wrapper) "dropdown-wrapper")

      ;; Add date picker stub
      (.appendChild wrapper (render-date-picker-stub el attrs))

      ;; Add calendar dropdown
      (.appendChild wrapper (render-calendar-dropdown el attrs))

      ;; Build final structure
      (.appendChild container wrapper)
      (.appendChild root container))

    ;; Set up global event listeners for open dropdown
    (when (:open? attrs)
      (.addEventListener js/document "click" #(handle-outside-click! el %))
      (.addEventListener js/document "keydown" #(handle-escape-key! el %)))

    el))

;; Cleanup function
(defn cleanup!
  "Clean up component state and event listeners"
  [^js el]
  ;; Clean up global day-classes function for this instance
  (when-let [picker-id (.-tyDatePickerId el)]
    (let [fn-name (str "datePickerDayClasses_" picker-id)]
      (js-delete js/window fn-name)))

  (set! (.-tyDatePickerState el) nil)
  (set! (.-tyDatePickerId el) nil)
  ;; Remove global event listeners
  (.removeEventListener js/document "click" #(handle-outside-click! el %))
  (.removeEventListener js/document "keydown" #(handle-escape-key! el %)))

;; Component registration
(wcs/define! "ty-date-picker"
  {:observed [:value :size :flavor :label :placeholder :required :disabled
              :clearable :format :locale :min-date :max-date :first-day-of-week]
   :connected (fn [^js el]
                ;; Initialize state from attributes
                (init-component-state! el)
                ;; Initial render
                (render! el))
   :disconnected (fn [^js el]
                   (cleanup! el))
   :attr (fn [^js el attr-name old-value new-value]
           ;; Handle external value changes
           (when (= attr-name "value")
             (let [parsed-value (when (and new-value (not= new-value ""))
                                  (date/parse-value new-value))]
               (update-component-state! el {:value parsed-value})))
           ;; Re-render for any attribute changes
           (render! el))})
