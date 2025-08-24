(ns ty.date.core
  "Core date utilities using gersak/timing library"
  (:require [timing.core :as t]
            [timing.adjusters :as adj]))

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

(defn format-date
  "Format date using Intl.DateTimeFormat"
  ([value] (format-date value "en-US" {}))
  ([value locale] (format-date value locale {}))
  ([value locale options]
   (let [date-obj (value->time value)
         formatter (js/Intl.DateTimeFormat. locale (clj->js options))]
     (.format formatter date-obj))))

(defn parse-date-string
  "Parse ISO date string to numeric value"
  [date-str]
  (when (and date-str (not= date-str ""))
    (if (string? date-str)
      (time->value (js/Date. date-str))
      ;; If it's already a number, return it
      date-str)))

(defn same-day?
  "Check if two date values are on the same day"
  [value1 value2]
  (when (and value1 value2)
    (let [ctx1 (day-time-context value1)
          ctx2 (day-time-context value2)]
      (and (= (:year ctx1) (:year ctx2))
           (= (:month ctx1) (:month ctx2))
           (= (:day-in-month ctx1) (:day-in-month ctx2))))))

(defn calendar-month-days
  "Generate calendar month days for 6-week display.
   Returns a sequence of day contexts with additional UI metadata."
  [year month]
  (let [;; Get first day of the month
        month-value (time->value (date year month 1))
        month-context (day-time-context month-value)
        
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
    
    ;; Ensure we have exactly 42 days (6 weeks)
    (let [days-count (count all-days)]
      (if (< days-count 42)
        ;; Add more days from next month
        (let [last-value (:value (last all-days))
              additional-days (take (- 42 days-count)
                                   (map #(assoc % :other-month true :next-month true)
                                        (calendar-frame (+ last-value day) :week)))]
          (concat all-days additional-days))
        ;; Take only 42 days
        (take 42 all-days)))))

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
