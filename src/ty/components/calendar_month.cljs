(ns ty.components.calendar-month
  "Calendar month grid component with enhanced features"
  (:require [ty.css :refer [ensure-styles!]]
            [ty.date.core :as date]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

(declare render!)

;; Load calendar-month styles
(defstyles calendar-month-styles)

(defn get-calendar-state
  "Get or initialize calendar component state"
  [^js el]
  (or (.-tyCalendarState el)
      (let [initial-state {:selected-value nil
                           :view-year (.getFullYear (js/Date.))
                           :view-month (inc (.getMonth (js/Date.)))
                           :highlighted-day nil
                           :focused-day nil}]
        (set! (.-tyCalendarState el) initial-state)
        initial-state)))

(defn set-calendar-state!
  "Update calendar component state"
  [^js el updates]
  (let [new-state (merge (get-calendar-state el) updates)]
    (set! (.-tyCalendarState el) new-state)
    new-state))

(defn get-calendar-value
  "Get value from either property or attribute (handles Replicant prop passing).
   Supports ISO strings, timestamps, and Date objects."
  [^js el]
  (date/parse-value (or (.-value el)
                        (wcs/attr el "value"))))

(defn get-min-date
  "Get minimum date constraint"
  [^js el]
  (date/parse-value (or (.-minDate el)
                        (wcs/attr el "min-date"))))

(defn get-max-date
  "Get maximum date constraint"
  [^js el]
  (date/parse-value (or (.-maxDate el)
                        (wcs/attr el "max-date"))))

(defn get-disabled-dates
  "Get disabled dates as a set of values"
  [^js el]
  (let [disabled (or (.-disabledDates el)
                     (wcs/attr el "disabled-dates"))]
    (cond
      (nil? disabled) #{}
      (set? disabled) disabled
      (sequential? disabled) (set (map date/parse-value disabled))
      (string? disabled)
      (if (= disabled "")
        #{}
        (set (map date/parse-value (.split disabled ","))))
      :else #{})))

(defn is-selectable?
  "Check if a day can be selected based on constraints"
  [day-value min-date max-date disabled-dates]
  (and (date/in-range? day-value min-date max-date)
       (not (contains? disabled-dates day-value))))

(defn dispatch-date-select!
  "Dispatch date selection event with detailed information"
  [^js el value day-context]
  (let [detail #js {:value value
                    :date (date/value->time value)
                    :formatted (date/format-value value)
                    :context (clj->js day-context)}
        event (js/CustomEvent. "date-select"
                               #js {:detail detail
                                    :bubbles true
                                    :cancelable true})]
    (.dispatchEvent el event)))

(defn dispatch-month-change!
  "Dispatch month navigation event"
  [^js el year month]
  (let [detail #js {:year year
                    :month month}
        event (js/CustomEvent. "month-change"
                               #js {:detail detail
                                    :bubbles true
                                    :cancelable false})]
    (.dispatchEvent el event)))

(defn handle-day-click!
  "Handle click on a calendar day"
  [^js el ^js shadow-root day-context min-date max-date disabled-dates ^js event]
  (.preventDefault event)
  (let [value (:value day-context)]
    (cond
      ;; Don't select other month days unless explicitly allowed
      (:other-month day-context)
      (when (wcs/parse-bool-attr el "allow-other-month")
        (let [{:keys [year month]} day-context]
          (set-calendar-state! el {:view-year year
                                   :view-month month})
          (dispatch-month-change! el year month)
          (render! el)))

      ;; Check if selectable
      (is-selectable? value min-date max-date disabled-dates)
      (do
        (set-calendar-state! el {:selected-value value})
        (dispatch-date-select! el value day-context)
        (render! el))

      ;; Otherwise do nothing (disabled day)
      :else nil)))

(defn handle-key-down!
  "Handle keyboard navigation in calendar"
  [^js el ^js event]
  (let [key (.-key event)
        {:keys [focused-day view-year view-month]} (get-calendar-state el)
        days (date/calendar-month-days view-year view-month)
        current-index (when focused-day
                        (.findIndex (to-array days)
                                    #(= (:value %) focused-day)))]
    (case key
      "ArrowLeft"
      (when (and current-index (> current-index 0))
        (.preventDefault event)
        (let [new-day (nth days (dec current-index))]
          (set-calendar-state! el {:focused-day (:value new-day)})
          (render! el)))

      "ArrowRight"
      (when (and current-index (< current-index 41))
        (.preventDefault event)
        (let [new-day (nth days (inc current-index))]
          (set-calendar-state! el {:focused-day (:value new-day)})
          (render! el)))

      "ArrowUp"
      (when (and current-index (>= current-index 7))
        (.preventDefault event)
        (let [new-day (nth days (- current-index 7))]
          (set-calendar-state! el {:focused-day (:value new-day)})
          (render! el)))

      "ArrowDown"
      (when (and current-index (<= current-index 34))
        (.preventDefault event)
        (let [new-day (nth days (+ current-index 7))]
          (set-calendar-state! el {:focused-day (:value new-day)})
          (render! el)))

      ("Enter" " ")
      (when focused-day
        (.preventDefault event)
        (let [day-context (first (filter #(= (:value %) focused-day) days))
              min-date (get-min-date el)
              max-date (get-max-date el)
              disabled-dates (get-disabled-dates el)]
          (when (and day-context
                     (not (:other-month day-context))
                     (is-selectable? focused-day min-date max-date disabled-dates))
            (set-calendar-state! el {:selected-value focused-day})
            (dispatch-date-select! el focused-day day-context)
            (render! el))))

      ;; Default - do nothing
      nil)))

(defn render-day-cell
  "Render a single day cell with all states"
  [^js el ^js shadow-root day-context selected-value today-value
   min-date max-date disabled-dates focused-day]
  (let [value (:value day-context)
        is-selected (and selected-value (date/same-day? value selected-value))
        is-today (date/same-day? value today-value)
        is-weekend (:weekend? day-context)
        is-other-month (:other-month day-context)
        is-disabled (or (not (date/in-range? value min-date max-date))
                        (contains? disabled-dates value))
        is-focused (and focused-day (= value focused-day))

        classes (cond-> ["calendar-day"]
                  is-selected (conj "selected")
                  is-today (conj "today")
                  is-weekend (conj "weekend")
                  is-other-month (conj "other-month")
                  is-disabled (conj "disabled")
                  is-focused (conj "focused"))

        day-element (doto (.createElement js/document "div")
                      (-> .-className (set! (clojure.string/join " " classes)))
                      (-> .-textContent (set! (str (:day-in-month day-context)))))]

    ;; Set ARIA attributes for accessibility
    (.setAttribute day-element "role" "button")
    (.setAttribute day-element "aria-label"
                   (date/format-date value "en-US"
                                     {:weekday "long"
                                      :year "numeric"
                                      :month "long"
                                      :day "numeric"}))
    (when is-selected
      (.setAttribute day-element "aria-selected" "true"))
    (when is-disabled
      (.setAttribute day-element "aria-disabled" "true"))
    (when is-focused
      (.setAttribute day-element "tabindex" "0"))

    ;; Add click handler if not disabled
    (when-not (or is-disabled is-other-month)
      (.addEventListener day-element "click"
                         (partial handle-day-click! el shadow-root day-context
                                  min-date max-date disabled-dates)))

    day-element))

(defn render!
  "Main render function for calendar month"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        {:keys [view-year view-month selected-value focused-day]} (get-calendar-state el)

        ;; Get value from property or attribute
        value (get-calendar-value el)

        ;; Get constraints
        min-date (get-min-date el)
        max-date (get-max-date el)
        disabled-dates (get-disabled-dates el)

        ;; Update selected value if provided
        _ (when (and value (not= value selected-value))
            (set-calendar-state! el {:selected-value value}))

        ;; Generate calendar days
        days (date/calendar-month-days view-year view-month)
        today-value (date/today)

        ;; Get weekday names
        locale (or (wcs/attr el "locale") "en-US")
        weekday-names (date/get-weekday-names locale "short")

        ;; Get first day of week (0 = Sunday, 1 = Monday)
        first-day-of-week (or (js/parseInt (wcs/attr el "first-day-of-week")) 0)

        ;; Rotate weekday names based on first day of week
        ordered-weekdays (if (= first-day-of-week 0)
                           weekday-names
                           (vec (concat (drop first-day-of-week weekday-names)
                                        (take first-day-of-week weekday-names))))]

    ;; Ensure styles are loaded
    (ensure-styles! root calendar-month-styles "ty-calendar-month")

    ;; Clear and rebuild content
    (set! (.-innerHTML root) "")

    ;; Create container
    (let [container (.createElement js/document "div")]
      (set! (.-className container) "calendar-month-container")

      ;; Set tabindex for keyboard navigation
      (.setAttribute container "tabindex" "0")
      (.addEventListener container "keydown" (partial handle-key-down! el))

      ;; Add weekday headers
      (let [header-row (.createElement js/document "div")]
        (set! (.-className header-row) "calendar-weekdays")
        (doseq [weekday ordered-weekdays]
          (let [header-cell (.createElement js/document "div")]
            (set! (.-className header-cell) "calendar-weekday")
            (set! (.-textContent header-cell) weekday)
            (.appendChild header-row header-cell)))
        (.appendChild container header-row))

      ;; Add day cells
      (let [days-grid (.createElement js/document "div")]
        (set! (.-className days-grid) "calendar-days-grid")

        ;; Reorder days based on first day of week if needed
        (let [ordered-days (if (= first-day-of-week 0)
                             days
                             ;; Reorganize days to start with Monday
                             (let [weeks (partition 7 days)]
                               (mapcat (fn [week]
                                         (vec (concat (drop first-day-of-week week)
                                                      (take first-day-of-week week))))
                                       weeks)))]
          (doseq [day-context ordered-days]
            (let [day-cell (render-day-cell el root day-context
                                            (or value selected-value) today-value
                                            min-date max-date disabled-dates focused-day)]
              (.appendChild days-grid day-cell))))
        (.appendChild container days-grid))

      (.appendChild root container))))

(defn cleanup!
  "Cleanup function for calendar month"
  [^js el]
  (set! (.-tyCalendarState el) nil))

;; Register the web component
(wcs/define! "ty-calendar-month"
  {:observed [:value :year :month :min-date :max-date :disabled-dates
              :locale :first-day-of-week :allow-other-month]
   :connected render!
   :disconnected cleanup!
   :attr (fn [^js el attr-name old-value new-value]
           (case attr-name
             "value" (do
                       (when-let [parsed (date/parse-value new-value)]
                         (set-calendar-state! el {:selected-value parsed}))
                       (render! el))
             "year" (do
                      (set-calendar-state! el {:view-year (js/parseInt new-value)})
                      (render! el))
             "month" (do
                       (set-calendar-state! el {:view-month (js/parseInt new-value)})
                       (render! el))
             ("min-date" "max-date" "disabled-dates" "locale"
                         "first-day-of-week" "allow-other-month") (render! el)
             (render! el)))})
