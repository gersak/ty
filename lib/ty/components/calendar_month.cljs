(ns ty.components.calendar-month
  "Property-based stateless calendar month renderer with context integration.
  
  Uses the new property-controlled architecture:
  - Direct property access (no attribute fallback)
  - Automatic re-rendering via :prop hook
  - Context integration for locale fallbacks
  - Compatible with all modern frameworks"
  (:require [cljs-bean.core :refer [->js ->clj]]
            [ty.context :as context]
            [ty.css :refer [ensure-styles!]]
            [ty.date.core :as date]
            [ty.i18n.time :as time]
            [ty.shim :as wcs])
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

(defn get-locale-with-fallback
  "Get locale with context fallback"
  [^js el]
  (or (.-locale el) context/*locale* "en-US"))

(defn get-localized-weekdays
  "Get localized weekday names in Monday-first order (as used by calendar grid)"
  [locale style]
  (let [weekdays (time/get-weekday-names locale style)
        ;; get-weekday-names returns Sunday-first: [Sun Mon Tue Wed Thu Fri Sat]
        ;; We want Monday-first: [Mon Tue Wed Thu Fri Sat Sun]
        ;; So we take indices [1 2 3 4 5 6 0]
        monday-first-indices [1 2 3 4 5 6 0]]
    (mapv #(nth weekdays %) monday-first-indices)))

(defn normalize-size-value
  "Normalize size value - add 'px' if just a number, otherwise use as-is"
  [value]
  (when (and value (not= (str value) ""))
    (if (re-matches #"^\d+(\.\d+)?$" (str value))
      (str value "px") ; Add px if just a number
      (str value)))) ; Use as-is for other units (%, rem, vw, etc.)

(defn apply-width-properties!
  "Apply width-related properties as CSS custom properties"
  [^js el]
  (let [width (.-width el)
        min-width (.-minWidth el)
        max-width (.-maxWidth el)]
    ;; Only set CSS custom properties if values are present
    ;; This allows CSS and responsive rules to take precedence when not set
    (when-let [normalized-width (normalize-size-value width)]
      (.setProperty (.-style el) "--calendar-width" normalized-width))

    (when-let [normalized-min-width (normalize-size-value min-width)]
      (.setProperty (.-style el) "--calendar-min-width" normalized-min-width))

    (when-let [normalized-max-width (normalize-size-value max-width)]
      (.setProperty (.-style el) "--calendar-max-width" normalized-max-width)))) ; Use as-is for other units (%, rem, vw, etc.)

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
        content (if (ifn? day-content-fn)
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
  "Pure render function - property-based approach with context integration"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        today (js/Date.)

        ;; Direct property access with intelligent defaults
        display-year (or (.-displayYear el) (.getFullYear today))
        display-month (or (.-displayMonth el) (inc (.getMonth today)))
        value (.-value el)
        locale (get-locale-with-fallback el)

        ;; Generate days using date utils
        days (date/calendar-month-days display-year display-month)

        ;; Direct function property access
        day-content-fn (.-dayContentFn el)
        day-classes-fn (.-dayClassesFn el)
        custom-css (.-customCSS el)]

    ;; Apply width properties as CSS custom properties
    (apply-width-properties! el)

    ;; Load default styles
    (ensure-styles! root calendar-month-styles "ty-calendar-month")

    ;; Load custom CSS if provided
    (when custom-css
      (println "ENSURING CUSTOMM " custom-css)
      (let [sheet (js/CSSStyleSheet.)
            adopted (.-adoptedStyleSheets root)]
        (.replaceSync sheet custom-css)
        (when-not (some #(identical? % sheet) adopted)
          (set! (.-adoptedStyleSheets root) (.concat adopted #js [sheet])))))

    ;; Clear and rebuild
    (set! (.-innerHTML root) "")

    ;; Create unified flex container
    (let [calendar-container (.createElement js/document "div")]
      (set! (.-className calendar-container) "calendar-flex-container")

      ;; Create header row (Row 1) with localized weekday names
      (let [header-row (.createElement js/document "div")
            weekdays (get-localized-weekdays locale "short")]
        (set! (.-className header-row) "calendar-row calendar-header-row")
        (doseq [weekday weekdays]
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

;; Property-based web component definition
(wcs/define! "ty-calendar-month"
  {:observed [] ; Minimal attribute watching
   :props {:displayMonth nil ; Property declarations install getters/setters
           :displayYear nil ; Properties automatically trigger re-render via :prop hook
           :value nil
           :dayContentFn nil ; Direct function assignment
           :dayClassesFn nil
           :customCSS nil ; Custom CSS injection for render functions
           :width nil
           :minWidth nil
           :maxWidth nil
           :locale nil} ; Context integration
   :connected (fn [^js el]
                ;; Set defaults with context fallbacks
                (when-not (.-displayMonth el)
                  (set! (.-displayMonth el) (inc (.getMonth (js/Date.)))))
                (when-not (.-displayYear el)
                  (set! (.-displayYear el) (.getFullYear (js/Date.))))
                (when-not (.-locale el)
                  (set! (.-locale el) (or context/*locale* "en-US")))
                (render! el))
   :prop (fn [^js el delta]
           (render! el))})
