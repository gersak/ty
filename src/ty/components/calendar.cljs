(ns ty.components.calendar
  "Full calendar component with navigation controls - wraps ty-calendar-month"
  (:require [cljs-bean.core :refer [->clj ->js]]
            [ty.css :refer [ensure-styles!]]
            [ty.date.core :as date]
            [ty.shim :as wcs]
            [ty.value :as value])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load calendar navigation styles
(defstyles calendar-styles)

(declare render!)

(defn get-calendar-state
  "Get or initialize calendar state as ClojureScript data"
  [^js el]
  (or (.-tyCalendarState el)
      (let [today (js/Date.)
            initial-state {:display-year (.getFullYear today)
                           :display-month (inc (.getMonth today))
                           :value nil}]
        (set! (.-tyCalendarState el) initial-state)
        initial-state)))

(defn set-calendar-state!
  "Update calendar state using ClojureScript data structures"
  [^js el updates]
  (let [current-state (get-calendar-state el)
        new-state (merge current-state updates)]
    (set! (.-tyCalendarState el) new-state)
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

(defn go-to-today!
  "Navigate to current month and optionally select today"
  [^js el]
  (let [today (js/Date.)
        today-year (.getFullYear today)
        today-month (inc (.getMonth today))
        should-select (wcs/parse-bool-attr el "select-today-on-navigate")]
    (set-calendar-state! el (cond-> {:display-year today-year
                                     :display-month today-month}
                              should-select (assoc :value (date/today))))
    (render! el)))

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
  "Render calendar navigation controls"
  [^js el state locale show-today?]
  (let [month-names (get-month-names locale)
        year-range (get-year-range (:display-year state))
        current-month (:display-month state)
        current-year (:display-year state)]

    (let [header (.createElement js/document "div")]
      (set! (.-className header) "calendar-navigation-header")

      ;; Year navigation buttons
      (let [prev-year-btn (.createElement js/document "button")]
        (set! (.-className prev-year-btn) "nav-btn nav-year-prev")
        (set! (.-textContent prev-year-btn) "<<")
        (set! (.-title prev-year-btn) "Previous year")
        (.addEventListener prev-year-btn "click" #(navigate-year! el -1))
        (.appendChild header prev-year-btn))

      ;; Month navigation buttons  
      (let [prev-month-btn (.createElement js/document "button")]
        (set! (.-className prev-month-btn) "nav-btn nav-month-prev")
        (set! (.-textContent prev-month-btn) "<")
        (set! (.-title prev-month-btn) "Previous month")
        (.addEventListener prev-month-btn "click" #(navigate-month! el -1))
        (.appendChild header prev-month-btn))

      ;; Month dropdown
      (let [month-select (.createElement js/document "select")]
        (set! (.-className month-select) "nav-select nav-month-select")
        (set! (.-value month-select) current-month)
        (doseq [[idx month-name] (map-indexed vector month-names)]
          (let [option (.createElement js/document "option")]
            (set! (.-value option) (inc idx))
            (set! (.-textContent option) month-name)
            (when (= (inc idx) current-month)
              (set! (.-selected option) true))
            (.appendChild month-select option)))
        (.addEventListener month-select "change"
                           #(set-month! el (js/parseInt (.-value (.-target %)))))
        (.appendChild header month-select))

      ;; Year dropdown
      (let [year-select (.createElement js/document "select")]
        (set! (.-className year-select) "nav-select nav-year-select")
        (set! (.-value year-select) current-year)
        (doseq [year year-range]
          (let [option (.createElement js/document "option")]
            (set! (.-value option) year)
            (set! (.-textContent option) year)
            (when (= year current-year)
              (set! (.-selected option) true))
            (.appendChild year-select option)))
        (.addEventListener year-select "change"
                           #(set-year! el (js/parseInt (.-value (.-target %)))))
        (.appendChild header year-select))

      ;; Month navigation buttons
      (let [next-month-btn (.createElement js/document "button")]
        (set! (.-className next-month-btn) "nav-btn nav-month-next")
        (set! (.-textContent next-month-btn) ">")
        (set! (.-title next-month-btn) "Next month")
        (.addEventListener next-month-btn "click" #(navigate-month! el 1))
        (.appendChild header next-month-btn))

      ;; Year navigation buttons
      (let [next-year-btn (.createElement js/document "button")]
        (set! (.-className next-year-btn) "nav-btn nav-year-next")
        (set! (.-textContent next-year-btn) ">>")
        (set! (.-title next-year-btn) "Next year")
        (.addEventListener next-year-btn "click" #(navigate-year! el 1))
        (.appendChild header next-year-btn))

      ;; Today button (optional)
      (when show-today?
        (let [today-btn (.createElement js/document "button")]
          (set! (.-className today-btn) "nav-btn today-btn")
          (set! (.-textContent today-btn) "Today")
          (set! (.-title today-btn) "Go to current month")
          (.addEventListener today-btn "click" #(go-to-today! el))
          (.appendChild header today-btn)))

      header)))

(declare render!)

(defn render-embedded-calendar
  "Render the embedded ty-calendar-month with current state"
  [^js el state]
  (let [calendar-month (.createElement js/document "ty-calendar-month")]

    ;; Set display attributes from state
    (.setAttribute calendar-month "display-year" (:display-year state))
    (.setAttribute calendar-month "display-month" (:display-month state))

    ;; Pass through value if set
    (let [calendar-value (or (:selected-date state) (:value state))]
      (when calendar-value
        (.setAttribute calendar-month "value" calendar-value)))

    ;; Pass through other attributes from parent
    ;; Set custom day-classes function for selection styling (unless user provided one)
    (let [selected-value (or (:selected-date state) (:value state))
          user-day-classes-fn (value/get-attribute el "day-classes-fn")]
      (when (not user-day-classes-fn)
        ;; Only set our selection function if user didn't provide their own
        (set! (.-dayClassesFn calendar-month) (create-day-classes-with-selection selected-value))))

    ;; Pass through other attributes from parent
    (doseq [attr ["min-width" "max-width" "width"
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
                           ;; Update internal state with selected date
                           (set-calendar-state! el {:selected-date selected-value})
                           ;; Forward the event
                           (dispatch-calendar-event! el "day-click" detail)
                           (render! el))))

    calendar-month))

(defn render!
  "Render the full calendar with navigation"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        state (get-calendar-state el)
        locale (or (value/get-attribute el "locale") "en-US")
        show-today? (wcs/parse-bool-attr el "show-today-button")
        show-navigation? (not= (value/get-attribute el "show-navigation") "false")] ; Default true

    ;; Load styles
    (ensure-styles! root calendar-styles "ty-calendar")

    ;; Clear and rebuild
    (set! (.-innerHTML root) "")

    ;; Create main container
    (let [container (.createElement js/document "div")]
      (set! (.-className container) "calendar-container")

      ;; Add navigation header (if enabled)
      (when show-navigation?
        (.appendChild container (render-navigation-header el state locale show-today?)))

      ;; Add embedded calendar month
      (.appendChild container (render-embedded-calendar el state))

      (.appendChild root container))))

(defn cleanup!
  "Clean up component state"
  [^js el]
  (set! (.-tyCalendarState el) nil))

;; Component registration
(wcs/define! "ty-calendar"
  {:observed [:show-today-button :show-navigation :locale :value
              :min-width :max-width :width :day-content-fn :day-classes-fn
              :min-date :max-date :disabled-dates :first-day-of-week
              :select-today-on-navigate]
   :connected (fn [^js el] (render! el))
   :disconnected (fn [^js el] (cleanup! el))
   :attr (fn [^js el attr-name old-value new-value]
           ;; Handle value changes by updating state
           (when (= attr-name "value")
             (let [parsed-value (when (and new-value (not= new-value ""))
                                  (date/parse-value new-value))]
               (set-calendar-state! el {:value parsed-value})))
           ;; Always re-render on attribute changes
           (render! el))})
