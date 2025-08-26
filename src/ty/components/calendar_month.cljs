(ns ty.components.calendar-month
  "Minimal stateless calendar month renderer"
  (:require [ty.css :refer [ensure-styles!]]
            [ty.date.core :as date]
            [ty.shim :as wcs]
            [ty.value :as value])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load basic calendar styles
(defstyles calendar-month-styles)

(defn render-day-cell
  "Render a single day cell with proper context styling"
  [day-context]
  (let [day-element (.createElement js/document "div")
        classes (cond-> ["calendar-day"]
                  (:today? day-context) (conj "today")
                  (:weekend day-context) (conj "weekend")
                  (:other-month day-context) (conj "other-month"))]
    (set! (.-className day-element) (clojure.string/join " " classes))
    (set! (.-textContent day-element) (str (:day-in-month day-context)))
    day-element))

(defn render!
  "Pure render function - no state"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        today (js/Date.)
        display-year (or (value/parse-integer (value/get-attribute el "display-year"))
                         (.getFullYear today))
        display-month (or (value/parse-integer (value/get-attribute el "display-month"))
                          (inc (.getMonth today)))

        ;; Generate days using date utils
        days (date/calendar-month-days display-year display-month)]

    ;; Load styles
    (ensure-styles! root calendar-month-styles "ty-calendar-month")

    ;; Clear and rebuild
    (set! (.-innerHTML root) "")

    ;; Create container
    (let [container (.createElement js/document "div")]
      (set! (.-className container) "calendar-month-container")

      ;; Add weekday headers (simple version)
      (let [header-row (.createElement js/document "div")]
        (set! (.-className header-row) "calendar-weekdays")
        (doseq [weekday ["Mon" "Tue" "Wed" "Thu" "Fri" "Sat" "Sun"]]
          (let [header-cell (.createElement js/document "div")]
            (set! (.-className header-cell) "calendar-weekday")
            (set! (.-textContent header-cell) weekday)
            (.appendChild header-row header-cell)))
        (.appendChild container header-row))

      ;; Add day cells
      (let [days-grid (.createElement js/document "div")]
        (set! (.-className days-grid) "calendar-days-grid")
        (doseq [day-context days]
          (let [day-cell (render-day-cell day-context)]
            (.appendChild days-grid day-cell)))
        (.appendChild container days-grid))

      (.appendChild root container))))

;; Simple web component registration
(wcs/define! "ty-calendar-month"
  {:observed [:display-year :display-month]
   :connected (fn [^js el] (render! el))
   :attr (fn [^js el attr-name old-value new-value]
           (render! el))})
