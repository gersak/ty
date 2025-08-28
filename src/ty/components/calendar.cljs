(ns ty.components.calendar
  "Full calendar component with navigation controls - wraps ty-calendar-month"
  (:require [cljs-bean.core :refer [->clj ->js]]
            [ty.css :refer [ensure-styles!]]
            [ty.date.core :as date]
            [ty.shim :as wcs]
            [ty.value :as value])
  (:require-macros [ty.css :refer [defstyles]]))

;; Default compact width for better out-of-box experience
(def default-calendar-width "304px")

;; Embedded SVG icons for calendar navigation - guaranteed to work with advanced compilation
(def chevron-left-svg
  "<svg stroke='currentColor' fill='none' stroke-linejoin='round' width='16' xmlns='http://www.w3.org/2000/svg' stroke-linecap='round' stroke-width='2' viewBox='0 0 24 24' height='16'><path d='m15 18-6-6 6-6'/></svg>")

(def chevron-right-svg
  "<svg stroke='currentColor' fill='none' stroke-linejoin='round' width='16' xmlns='http://www.w3.org/2000/svg' stroke-linecap='round' stroke-width='2' viewBox='0 0 24 24' height='16'><path d='m9 18 6-6-6-6'/></svg>")

(def chevrons-left-svg
  "<svg stroke='currentColor' fill='none' stroke-linejoin='round' width='16' xmlns='http://www.w3.org/2000/svg' stroke-linecap='round' stroke-width='2' viewBox='0 0 24 24' height='16'><path d='m11 17-5-5 5-5'/><path d='m18 17-5-5 5-5'/></svg>")

(def chevrons-right-svg
  "<svg stroke='currentColor' fill='none' stroke-linejoin='round' width='16' xmlns='http://www.w3.org/2000/svg' stroke-linecap='round' stroke-width='2' viewBox='0 0 24 24' height='16'><path d='m6 17 5-5-5-5'/><path d='m13 17 5-5-5-5'/></svg>")

;; Load calendar navigation styles
(defstyles calendar-styles)

(declare render!)

(defn init-calendar-state!
  "Initialize calendar state from initial value (uncontrolled pattern)"
  [^js el]
  (let [initial-value (or (value/get-attribute el "value")
                          (.-value el))
        parsed-value (when (and initial-value (not= initial-value ""))
                       (date/parse-value initial-value))
        today (js/Date.)
        ;; Determine display month from initial value OR current month
        display-context (if parsed-value
                          (date/day-time-context parsed-value)
                          {:year (.getFullYear today)
                           :month (inc (.getMonth today))})
        initial-state {:display-year (:year display-context)
                       :display-month (:month display-context)
                       :value parsed-value}]

    ;; Set internal state
    (set! (.-tyCalendarState el) initial-state)

    ;; Set .value property for JS compatibility 
    (set! (.-value el) parsed-value)

    initial-state))

(defn get-calendar-state
  "Get existing calendar state (should be initialized by init-calendar-state!)"
  [^js el]
  (or (.-tyCalendarState el)
      ;; Fallback if somehow not initialized
      (init-calendar-state! el)))

(defn emit-value-change!
  "Emit consistent value change event for uncontrolled component"
  [^js el new-value source]
  (let [event-detail #js {:value new-value
                          :source source ; "day-click" | "navigation" | "external"
                          :calendar-type "ty-calendar"
                          :has-navigation true}
        event (js/CustomEvent. "change"
                               #js {:detail event-detail
                                    :bubbles true
                                    :cancelable true})]
    ;; Update .value property for JS compatibility
    (set! (.-value el) new-value)

    ;; Dispatch change event
    (.dispatchEvent el event)))

(defn set-calendar-state!
  "Update calendar state and emit events if value changed"
  [^js el updates]
  (let [current-state (get-calendar-state el)
        new-state (merge current-state updates)
        value-changed? (not= (:value current-state) (:value new-state))]

    ;; Update internal state
    (set! (.-tyCalendarState el) new-state)

    ;; Emit change event if value changed
    (when value-changed?
      (emit-value-change! el (:value new-state) "internal"))

    new-state))

(defn get-month-names
  "Get localized month names for dropdowns"
  [locale]
  (let [formatter (js/Intl.DateTimeFormat. locale #js {:month "long"})]
    (mapv (fn [month]
            (let [date (js/Date. 2024 (dec month) 1)]
              (.format formatter date)))
          (range 1 13))))

(defn get-year-range
  "Get reasonable year range for dropdown"
  [current-year]
  (let [start-year (- current-year 10)
        end-year (+ current-year 10)]
    (range start-year (inc end-year))))

(defn navigate-month!
  "Navigate to relative month (+1 for next, -1 for previous)"
  [^js el direction]
  (let [state (get-calendar-state el)
        current-month (:display-month state)
        current-year (:display-year state)
        new-date (date/add-months (date/time->value (date/date current-year current-month 1))
                                  direction)
        new-context (date/day-time-context new-date)]
    (set-calendar-state! el {:display-year (:year new-context)
                             :display-month (:month new-context)})
    (render! el)))

(defn navigate-year!
  "Navigate to relative year (+1 for next, -1 for previous)"
  [^js el direction]
  (let [state (get-calendar-state el)
        new-year (+ (:display-year state) direction)]
    (set-calendar-state! el {:display-year new-year})
    (render! el)))

(defn set-month!
  "Set specific month (1-12)"
  [^js el month]
  (set-calendar-state! el {:display-month month})
  (render! el))

(defn set-year!
  "Set specific year"
  [^js el year]
  (set-calendar-state! el {:display-year year})
  (render! el))

(defn create-day-classes-with-selection
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

(defn dispatch-calendar-event!
  "Forward events from embedded calendar with additional context"
  [^js el event-type detail]
  (let [calendar-detail (js/Object.assign
                          #js {}
                          detail
                          #js {:calendar-type "ty-calendar"
                               :has-navigation true})
        event (js/CustomEvent. event-type
                               #js {:detail calendar-detail
                                    :bubbles true
                                    :cancelable true})]
    (.dispatchEvent el event)))

(defn render-navigation-header
  "Render clean calendar navigation controls using 3-group flex layout"
  [{:keys [^js el state locale width]}]
  (let [month-names (get-month-names locale)
        current-month (:display-month state)
        current-year (:display-year state)
        current-month-name (nth month-names (dec current-month))]

    (let [header (.createElement js/document "div")
          left-group (.createElement js/document "div")
          center-group (.createElement js/document "div")
          right-group (.createElement js/document "div")]

      ;; Set up container classes
      (set! (.-className header) "calendar-navigation-header")
      (set! (.-className left-group) "nav-group nav-group-left")
      (set! (.-className center-group) "nav-group nav-group-center")
      (set! (.-className right-group) "nav-group nav-group-right")

      ;; Set width if provided to match the calendar
      (when width
        (set! (.-style header) (str "width: " width ";")))

      ;; Create buttons
      ;; Previous year button (double chevron left)
      (let [prev-year-btn (.createElement js/document "button")]
        (set! (.-className prev-year-btn) "nav-btn nav-year-prev")
        (set! (.-title prev-year-btn) "Previous year")
        (set! (.-innerHTML prev-year-btn) chevrons-left-svg)
        (.addEventListener prev-year-btn "click" #(navigate-year! el -1))
        (.appendChild left-group prev-year-btn))

      ;; Previous month button (single chevron left)
      (let [prev-month-btn (.createElement js/document "button")]
        (set! (.-className prev-month-btn) "nav-btn nav-month-prev")
        (set! (.-title prev-month-btn) "Previous month")
        (set! (.-innerHTML prev-month-btn) chevron-left-svg)
        (.addEventListener prev-month-btn "click" #(navigate-month! el -1))
        (.appendChild left-group prev-month-btn))

      ;; Month and year display (center)
      (let [month-year-display (.createElement js/document "div")]
        (set! (.-className month-year-display) "month-year-display")
        (set! (.-textContent month-year-display)
              (str current-month-name " " current-year))
        (.appendChild center-group month-year-display))

      ;; Next month button (single chevron right)
      (let [next-month-btn (.createElement js/document "button")]
        (set! (.-className next-month-btn) "nav-btn nav-month-next")
        (set! (.-title next-month-btn) "Next month")
        (set! (.-innerHTML next-month-btn) chevron-right-svg)
        (.addEventListener next-month-btn "click" #(navigate-month! el 1))
        (.appendChild right-group next-month-btn))

      ;; Next year button (double chevron right)
      (let [next-year-btn (.createElement js/document "button")]
        (set! (.-className next-year-btn) "nav-btn nav-year-next")
        (set! (.-title next-year-btn) "Next year")
        (set! (.-innerHTML next-year-btn) chevrons-right-svg)
        (.addEventListener next-year-btn "click" #(navigate-year! el 1))
        (.appendChild right-group next-year-btn))

      ;; Assemble the header with 3 groups
      (.appendChild header left-group)
      (.appendChild header center-group)
      (.appendChild header right-group)

      header)))

(defn render-embedded-calendar
  "Render the embedded ty-calendar-month with current state"
  [{:keys [^js el state width]}]
  (let [calendar-month (.createElement js/document "ty-calendar-month")]

    ;; Set display attributes from state
    (.setAttribute calendar-month "display-year" (:display-year state))
    (.setAttribute calendar-month "display-month" (:display-month state))

    ;; Pass through value if set
    (when-let [calendar-value (:value state)]
      (.setAttribute calendar-month "value" calendar-value))

    ;; Set width (either user provided or default)
    (when width
      (.setAttribute calendar-month "width" width))

    ;; Set custom day-classes function for selection styling (unless user provided one)
    (let [selected-value (:value state)
          user-day-classes-fn (value/get-attribute el "day-classes-fn")]
      (when (not user-day-classes-fn)
        ;; Only set our selection function if user didn't provide their own
        (set! (.-dayClassesFn calendar-month) (create-day-classes-with-selection selected-value))))

    ;; Pass through other attributes from parent (excluding width since we handled it above)
    (doseq [attr ["min-width" "max-width"
                  "day-content-fn" "day-classes-fn"
                  "min-date" "max-date" "disabled-dates"
                  "locale" "first-day-of-week"]]
      (when-let [attr-value (value/get-attribute el attr)]
        (.setAttribute calendar-month attr attr-value)))

    ;; Set up event forwarding
    (.addEventListener calendar-month "day-click"
                       (fn [^js event]
                         (.stopPropagation event) ; Prevent double bubbling
                         (let [detail (.-detail event)
                               selected-value (.-value detail)]
                           ;; Update internal state with selected date (this will emit change event)
                           (set-calendar-state! el {:value selected-value})
                           ;; Forward the original day-click event too
                           (dispatch-calendar-event! el "day-click" detail)
                           ;; Re-render to update selection styling
                           (render! el))))

    calendar-month))

(defn render!
  "Render the full calendar with navigation"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        state (get-calendar-state el)
        locale (or (value/get-attribute el "locale") "en-US")
        show-navigation? (not= (value/get-attribute el "show-navigation") "false") ; Default true
        ;; Determine width - user provided or default compact width
        width (or (value/get-attribute el "width") default-calendar-width)]

    ;; Load styles
    (ensure-styles! root calendar-styles "ty-calendar")

    ;; Clear and rebuild
    (set! (.-innerHTML root) "")

    ;; Create main container
    (let [container (.createElement js/document "div")]
      (set! (.-className container) "calendar-container")

      ;; Add navigation header (if enabled) with width for consistent sizing
      (when show-navigation?
        (.appendChild container
                      (render-navigation-header {:el el
                                                 :state state
                                                 :locale locale
                                                 :width width})))

      ;; Add embedded calendar month with width
      (.appendChild container
                    (render-embedded-calendar {:el el
                                               :state state
                                               :width width}))

      (.appendChild root container))))

(defn cleanup!
  "Clean up component state"
  [^js el]
  (set! (.-tyCalendarState el) nil))

;; Component registration
(wcs/define! "ty-calendar"
  {:observed [:show-navigation :locale :value
              :min-width :max-width :width :day-content-fn :day-classes-fn
              :min-date :max-date :disabled-dates :first-day-of-week]
   :connected (fn [^js el]
                ;; Initialize state from initial attributes first
                (init-calendar-state! el)
                ;; Then render
                (render! el))
   :disconnected (fn [^js el] (cleanup! el))
   :attr (fn [^js el attr-name old-value new-value]
           ;; For uncontrolled components, ignore external value changes after initialization
           ;; The component manages its own state after initial setup
           (when-not (= attr-name "value")
             ;; Re-render for other attribute changes
             (render! el)))})
