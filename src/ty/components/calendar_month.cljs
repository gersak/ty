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

(defn normalize-size-value
  "Normalize size value - add 'px' if just a number, otherwise use as-is"
  [value]
  (when (and value (not= (str value) ""))
    (if (re-matches #"^\d+(\.\d+)?$" (str value))
      (str value "px") ; Add px if just a number
      (str value)))) ; Use as-is for other units (%, rem, vw, etc.)

(defn apply-width-attributes!
  "Apply width-related attributes as CSS custom properties"
  [^js el]
  (let [width (value/get-attribute el "width")
        min-width (value/get-attribute el "min-width")
        max-width (value/get-attribute el "max-width")]

    ;; Only set CSS custom properties if attributes are present
    ;; This allows CSS and responsive rules to take precedence when attributes are not set
    (when-let [normalized-width (normalize-size-value width)]
      (.setProperty (.-style el) "--calendar-width" normalized-width))

    (when-let [normalized-min-width (normalize-size-value min-width)]
      (.setProperty (.-style el) "--calendar-min-width" normalized-min-width))

    (when-let [normalized-max-width (normalize-size-value max-width)]
      (.setProperty (.-style el) "--calendar-max-width" normalized-max-width))))

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

    ;; STRICT DOM-ONLY CHECK: Throw error immediately if custom function returns non-DOM content
    (when (and day-content-fn content (not (.-nodeType content)))
      (throw (js/Error. (str "Custom render function must return a DOM element, got: " (type content) ". "
                             "Use (.createElement js/document \"div\") to create DOM elements."))))

    ;; Apply classes
    (set! (.-className day-element) (clojure.string/join " " classes))

    ;; Set content (kept for future flexibility - current strict check prevents this path for custom functions)
    (cond
      ;; String content (from default renderer) - use textContent
      (string? content)
      (set! (.-textContent day-element) content)

      ;; DOM element - append it (ONLY valid custom content after strict check above)
      (and content (.-nodeType content))
      (.appendChild day-element content)

      ;; Custom render function returned invalid content - throw error (redundant after strict check but kept for completeness)
      (and day-content-fn content)
      (throw (js/Error. (str "Custom render function must return a DOM element, got: " (type content) ". "
                             "Use (.createElement js/document \"div\") to create DOM elements.")))

      ;; No content or nil - do nothing
      :else
      nil)

    ;; Add click handler
    (.addEventListener day-element "click"
                       (fn [^js event]
                         (.preventDefault event)
                         (dispatch-day-click! el day-context event)))

    day-element))

(defn render!
  "Pure render function - unified flex layout"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        today (js/Date.)
        display-year (or (value/parse-integer (value/get-attribute el "display-year"))
                         (.getFullYear today))
        display-month (or (value/parse-integer (value/get-attribute el "display-month"))
                          (inc (.getMonth today)))

        ;; Generate days using date utils
        days (date/calendar-month-days display-year display-month)

        ;; Get render functions from attributes
        day-content-fn (get-render-function el "day-content-fn")
        day-classes-fn (get-render-function el "day-classes-fn")]

    ;; Apply width attributes as CSS custom properties
    (apply-width-attributes! el)

    ;; Load styles
    (ensure-styles! root calendar-month-styles "ty-calendar-month")

    ;; Clear and rebuild
    (set! (.-innerHTML root) "")

    ;; Create unified flex container
    (let [calendar-container (.createElement js/document "div")]
      (set! (.-className calendar-container) "calendar-flex-container")

      ;; Create header row (Row 1)
      (let [header-row (.createElement js/document "div")]
        (set! (.-className header-row) "calendar-row calendar-header-row")
        (doseq [weekday ["Mon" "Tue" "Wed" "Thu" "Fri" "Sat" "Sun"]]
          (let [header-cell (.createElement js/document "div")]
            (set! (.-className header-cell) "calendar-cell calendar-header-cell")
            (set! (.-textContent header-cell) weekday)
            (.appendChild header-row header-cell)))
        (.appendChild calendar-container header-row))

      ;; Create day rows (Rows 2-7) - 6 weeks of 7 days each
      (let [day-weeks (partition 7 days)]
        (doseq [week day-weeks]
          (let [day-row (.createElement js/document "div")]
            (set! (.-className day-row) "calendar-row calendar-day-row")
            (doseq [day-context week]
              (let [day-cell (render-day-cell day-context el day-content-fn day-classes-fn)]
                ;; Add unified cell classes for flex layout
                (set! (.-className day-cell)
                      (str (.-className day-cell) " calendar-cell calendar-day-cell"))
                (.appendChild day-row day-cell)))
            (.appendChild calendar-container day-row))))

      (.appendChild root calendar-container))))

;; Simple web component registration
(wcs/define! "ty-calendar-month"
  {:observed [:display-year :display-month :day-content-fn :day-classes-fn
              :width :min-width :max-width]
   :connected (fn [^js el] (render! el))
   :attr (fn [^js el attr-name old-value new-value]
           (render! el))})
