(ns ty.components.calendar-month
  "Calendar month grid component"
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
                           :highlighted-day nil}]
        (set! (.-tyCalendarState el) initial-state)
        initial-state)))

(defn set-calendar-state!
  "Update calendar component state"
  [^js el updates]
  (let [new-state (merge (get-calendar-state el) updates)]
    (set! (.-tyCalendarState el) new-state)
    new-state))

(defn get-calendar-value
  "Get value from either property or attribute (handles Replicant prop passing)"
  [^js el]
  (or (.-value el)
      (wcs/attr el "value")))

(defn dispatch-date-select!
  "Dispatch date selection event"
  [^js el value day-context]
  (let [detail #js {:value value
                    :date (date/value->time value)
                    :context (clj->js day-context)}
        event (js/CustomEvent. "date-select"
                               #js {:detail detail
                                    :bubbles true
                                    :cancelable true})]
    (.dispatchEvent el event)))

(defn handle-day-click!
  "Handle click on a calendar day"
  [^js el ^js shadow-root day-context ^js event]
  (.preventDefault event)
  (when-not (:other-month day-context)
    (let [value (:value day-context)]
      (set-calendar-state! el {:selected-value value})
      (dispatch-date-select! el value day-context)
      (render! el))))

(defn render-day-cell
  "Render a single day cell"
  [^js el ^js shadow-root day-context selected-value today-value]
  (let [value (:value day-context)
        is-selected (and selected-value (date/same-day? value selected-value))
        is-today (date/same-day? value today-value)
        is-weekend (:weekend? day-context)
        is-other-month (:other-month day-context)

        classes (cond-> ["calendar-day"]
                  is-selected (conj "selected")
                  is-today (conj "today")
                  is-weekend (conj "weekend")
                  is-other-month (conj "other-month"))

        day-element (doto (.createElement js/document "div")
                      (-> .-className (set! (clojure.string/join " " classes)))
                      (-> .-textContent (set! (str (:day-in-month day-context)))))]

    ;; Add click handler
    (.addEventListener day-element "click"
                       (partial handle-day-click! el shadow-root day-context))

    day-element))

(defn render!
  "Main render function for calendar month"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        {:keys [view-year view-month selected-value]} (get-calendar-state el)

        ;; Get value from property or attribute
        value-str (get-calendar-value el)
        value (when value-str
                (date/parse-date-string value-str))

        ;; Update selected value if provided
        _ (when (and value (not= value selected-value))
            (set-calendar-state! el {:selected-value value}))

        ;; Generate calendar days
        days (date/calendar-month-days view-year view-month)
        today-value (date/today)

        ;; Get weekday names
        weekday-names (date/get-weekday-names "en-US" "short")]

    ;; Ensure styles are loaded
    (ensure-styles! root calendar-month-styles "ty-calendar-month")

    ;; Clear and rebuild content
    (set! (.-innerHTML root) "")

    ;; Create container
    (let [container (.createElement js/document "div")]
      (set! (.-className container) "calendar-month-container")

      ;; Add weekday headers
      (let [header-row (.createElement js/document "div")]
        (set! (.-className header-row) "calendar-weekdays")
        (doseq [weekday weekday-names]
          (let [header-cell (.createElement js/document "div")]
            (set! (.-className header-cell) "calendar-weekday")
            (set! (.-textContent header-cell) weekday)
            (.appendChild header-row header-cell)))
        (.appendChild container header-row))

      ;; Add day cells
      (let [days-grid (.createElement js/document "div")]
        (set! (.-className days-grid) "calendar-days-grid")
        (doseq [day-context days]
          (let [day-cell (render-day-cell el root day-context
                                          (or value selected-value) today-value)]
            (.appendChild days-grid day-cell)))
        (.appendChild container days-grid))

      (.appendChild root container))))

(defn cleanup!
  "Cleanup function for calendar month"
  [^js el]
  (set! (.-tyCalendarState el) nil))

;; Register the web component
(wcs/define! "ty-calendar-month"
  {:observed [:value :year :month]
   :connected render!
   :disconnected cleanup!
   :attr (fn [^js el attr-name old-value new-value]
           (case attr-name
             "value" (do
                       (set-calendar-state! el {:selected-value (date/parse-date-string new-value)})
                       (render! el))
             "year" (do
                      (set-calendar-state! el {:view-year (js/parseInt new-value)})
                      (render! el))
             "month" (do
                       (set-calendar-state! el {:view-month (js/parseInt new-value)})
                       (render! el))
             (render! el)))})
