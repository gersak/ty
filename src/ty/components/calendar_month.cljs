(ns ty.components.calendar-month
  "Minimal stateless calendar month renderer"
  (:require [cljs-bean.core :refer [->js ->clj]]
            [ty.css :refer [ensure-styles!]]
            [ty.date.core :as date]
            [ty.shim :as wcs]
            [ty.value :as value])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load basic calendar styles
(defstyles calendar-month-styles)

(defn default-day-content
  "Default day content renderer - just the day number"
  [day-context]
  (str (:day-in-month (->clj day-context))))

(defn default-day-classes
  "Default day classes based on day context"
  [day-context]
  (let [day-context (->clj day-context)]
    (cond-> ["calendar-day"]
      (:today? day-context) (conj "today")
      (:weekend day-context) (conj "weekend")
      (:other-month day-context) (conj "other-month"))))

(defn set-render-functions!
  "Helper to set render functions as properties on element"
  [^js el {:keys [day-content-fn day-classes-fn]}]
  (when day-content-fn
    (set! (.-dayContentFn el) day-content-fn))
  (when day-classes-fn
    (set! (.-dayClassesFn el) day-classes-fn)))

(defn get-render-function
  "Get render function from element property (direct) or attribute (name lookup)"
  [^js el attr-name]
  (let [prop-name (case attr-name
                    "day-content-fn" "dayContentFn"
                    "day-classes-fn" "dayClassesFn"
                    attr-name)
        ;; First try to get as property (direct function)
        prop-fn (aget el prop-name)]
    (if (fn? prop-fn)
      prop-fn
      ;; Fallback to attribute (function name lookup)
      (when-let [fn-name (value/get-attribute el attr-name)]
        (when (and (exists? js/window) (aget js/window fn-name))
          (aget js/window fn-name))))))

(defn dispatch-day-click!
  "Dispatch day-click custom event with day context"
  [^js el day-context ^js dom-event]
  (let [detail #js {:dayContext (->js day-context)
                    :value (:value day-context)
                    :dateParts #js {:year (:year day-context)
                                    :month (:month day-context)
                                    :day (:day-in-month day-context)}
                    :isHoliday (:holiday? day-context)
                    :isToday (:today? day-context)
                    :isWeekend (:weekend day-context)
                    :isOtherMonth (:other-month day-context)}
        event (js/CustomEvent. "day-click"
                               #js {:detail detail
                                    :bubbles true
                                    :cancelable true})]
    (.dispatchEvent el event)))

(defn render-day-cell
  "Render a single day cell with customizable content and styling"
  [day-context ^js el day-content-fn day-classes-fn]
  (let [day-element (.createElement js/document "div")

        ;; Get content using render function or default
        content (if day-content-fn
                  (day-content-fn (->js day-context))
                  (default-day-content (->js day-context)))

        ;; Get classes using render function or default  
        classes (if day-classes-fn
                  (day-classes-fn (->js day-context))
                  (default-day-classes (->js day-context)))]

    ;; Apply classes
    (set! (.-className day-element) (clojure.string/join " " classes))

    ;; Set content (string or HTML)
    (cond
      ;; String content - use textContent for safety
      (string? content)
      (set! (.-textContent day-element) content)

      ;; DOM element - append it
      (and content (.-nodeType content))
      (.appendChild day-element content)

      ;; Fallback - treat as HTML string
      :else
      (set! (.-innerHTML day-element) (str content)))

    ;; Add click handler
    (.addEventListener day-element "click"
                       (fn [^js event]
                         (.preventDefault event)
                         (dispatch-day-click! el day-context event)))

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
      (let [days-grid (.createElement js/document "div")
            ;; Get render functions from attributes
            day-content-fn (get-render-function el "day-content-fn")
            day-classes-fn (get-render-function el "day-classes-fn")]
        (set! (.-className days-grid) "calendar-days-grid")
        (doseq [day-context days]
          (let [day-cell (render-day-cell day-context el day-content-fn day-classes-fn)]
            (.appendChild days-grid day-cell)))
        (.appendChild container days-grid))

      (.appendChild root container))))

;; Simple web component registration
(wcs/define! "ty-calendar-month"
  {:observed [:display-year :display-month :day-content-fn :day-classes-fn]
   :connected (fn [^js el] (render! el))
   :attr (fn [^js el attr-name old-value new-value]
           (render! el))})
