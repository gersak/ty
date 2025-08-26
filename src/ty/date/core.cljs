(ns ty.date.core
  "Core date utilities using gersak/timing library"
  (:require [timing.adjusters :as adj]
            [timing.core :as t]))

;; Re-export commonly used timing functions for convenience
(def date t/date)
(def time->value t/time->value)
(def value->time t/value->time)
(def day-time-context t/day-time-context)
(def calendar-frame t/calendar-frame)

;; Time units
(def day t/day)
(def week t/week)
(def days t/days)
(def weeks t/weeks)
(def hours t/hours)
(def minutes t/minutes)

;; Variable length periods - these are in adjusters!
(def add-months adj/add-months)
(def add-years adj/add-years)

(defn today
  "Get today's date as numeric value"
  []
  (time->value (date)))

(defn parse-value
  "Parse various date input formats to numeric value (milliseconds).
   Supports:
   - Numeric timestamps (milliseconds)
   - ISO date strings (2024-12-25, 2024-12-25T10:30:00Z)
   - Date objects
   - nil (returns nil)
   
   Returns nil if parsing fails."
  [input]
  (cond
    ;; Already nil
    (nil? input) nil

    ;; Already a number - assume it's milliseconds
    (number? input) input

    ;; Date object
    (instance? js/Date input)
    (let [ms (.getTime input)]
      (when-not (js/isNaN ms) ms))

    ;; String - try to parse
    (string? input)
    (when (not= input "")
      (let [parsed (js/Date. input)
            ms (.getTime parsed)]
        (when-not (js/isNaN ms) ms)))

    ;; Unknown type
    :else nil))

(defn format-value
  "Format a numeric value to ISO string (YYYY-MM-DD).
   Returns nil if value is invalid."
  [value]
  (when-let [v (parse-value value)]
    (let [date-obj (value->time v)
          year (.getFullYear date-obj)
          month (inc (.getMonth date-obj))
          day (.getDate date-obj)]
      (str year "-"
           (when (< month 10) "0") month "-"
           (when (< day 10) "0") day))))

(defn format-date
  "Format date using Intl.DateTimeFormat"
  ([value] (format-date value "en-US" {}))
  ([value locale] (format-date value locale {}))
  ([value locale options]
   (when-let [v (parse-value value)]
     (let [date-obj (value->time v)
           formatter (js/Intl.DateTimeFormat. locale (clj->js options))]
       (.format formatter date-obj)))))

(defn parse-date-string
  "Parse ISO date string to numeric value.
   DEPRECATED: Use parse-value instead."
  [date-str]
  (parse-value date-str))

(defn same-day?
  "Check if two date values are on the same day"
  [value1 value2]
  (when-let [v1 (parse-value value1)]
    (when-let [v2 (parse-value value2)]
      (let [ctx1 (day-time-context v1)
            ctx2 (day-time-context v2)]
        (and (= (:year ctx1) (:year ctx2))
             (= (:month ctx1) (:month ctx2))
             (= (:day-in-month ctx1) (:day-in-month ctx2)))))))

(defn in-range?
  "Check if a date value is within the given range (inclusive).
   Returns true if value is between min-date and max-date."
  [value min-date max-date]
  (when-let [v (parse-value value)]
    (and (or (nil? min-date)
             (>= v (parse-value min-date)))
         (or (nil? max-date)
             (<= v (parse-value max-date))))))

(defn is-disabled?
  "Check if a date value is in the disabled dates set.
   disabled-dates can be a set, array, or comma-separated string."
  [value disabled-dates]
  (when (and value disabled-dates)
    (let [v (parse-value value)
          disabled-set (cond
                         (set? disabled-dates) disabled-dates
                         (sequential? disabled-dates) (set disabled-dates)
                         (string? disabled-dates)
                         (set (map parse-value (.split disabled-dates ",")))
                         :else #{})]
      (contains? disabled-set v))))

(defn calendar-month-days
  "Generate calendar month days for 6-week display.
   Returns a sequence of day contexts with additional UI metadata."
  [year month]
  (let [;; Get first day of the month
        month-value (time->value (date year month 1))
        month-context (day-time-context month-value)

        today-js (js/Date.)
        today-year (.getFullYear today-js)
        today-month (inc (.getMonth today-js)) ; js/Date months are 0-based
        today-day (.getDate today-js)

        ;; Get all days in the month
        month-days (calendar-frame month-value :month)

        ;; Get first and last days
        first-day (first month-days)
        last-day (last month-days)

        ;; Get the week containing the first day
        first-week (calendar-frame (:value first-day) :week)

        ;; Get the week containing the last day
        last-week (calendar-frame (:value last-day) :week)

        ;; Combine all days
        all-days (concat
                  ;; Previous month days
                   (map #(assoc % :other-month true :prev-month true)
                        (take-while #(< (:value %) month-value) first-week))
                  ;; Current month days
                   month-days
                  ;; Next month days
                   (map #(assoc % :other-month true :next-month true)
                        (drop-while #(<= (:value %) (:value last-day)) last-week)))]

    ;; Ensure we have exactly 42 days (6 weeks) and add UI metadata
    (let [days-count (count all-days)
          final-days (if (< days-count 42)
                      ;; Add more days from next month
                       (let [last-value (:value (last all-days))
                             additional-days (take (- 42 days-count)
                                                   (map #(assoc % :other-month true :next-month true)
                                                        (calendar-frame (+ last-value day) :week)))]
                         (concat all-days additional-days))
                      ;; Take only 42 days
                       (take 42 all-days))]

      ;; Add UI-specific metadata to each day
      (map (fn [day-ctx]
             (let [is-today? (and (= (:year day-ctx) today-year)
                                  (= (:month day-ctx) today-month)
                                  (= (:day-in-month day-ctx) today-day))]
               (assoc day-ctx :today? is-today?)))
           final-days))))

(defn get-weekday-names
  "Get localized weekday names"
  ([locale] (get-weekday-names locale "short"))
  ([locale format-type]
   (let [base-date (time->value (date 2024 1 7)) ; Sunday
         formatter-options (case format-type
                             "narrow" #js {:weekday "narrow"}
                             "short" #js {:weekday "short"}
                             "long" #js {:weekday "long"}
                             #js {:weekday "short"})]
     (mapv (fn [i]
             (let [date-val (+ base-date (* i day))
                   date-obj (value->time date-val)
                   formatter (js/Intl.DateTimeFormat. locale formatter-options)]
               (.format formatter date-obj)))
           (range 7)))))

(defn get-month-names
  "Get localized month names"
  ([locale] (get-month-names locale "long"))
  ([locale format-type]
   (let [formatter-options (case format-type
                             "numeric" #js {:month "numeric"}
                             "2-digit" #js {:month "2-digit"}
                             "narrow" #js {:month "narrow"}
                             "short" #js {:month "short"}
                             "long" #js {:month "long"}
                             #js {:month "long"})]
     (mapv (fn [month]
             (let [date-obj (value->time (time->value (date 2024 month 1)))
                   formatter (js/Intl.DateTimeFormat. locale formatter-options)]
               (.format formatter date-obj)))
           (range 1 13)))))
