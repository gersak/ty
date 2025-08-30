(ns ty.components.calendar
  "Year/Month/Day attribute-based calendar component with property-based composition.
  
  Architecture:
  - year/month/day HTML attributes for intuitive API
  - Internal ClojureScript state management (immutable maps)
  - Distributes properties to child components (navigation + month)
  - Child components use property-based APIs for performance
  - Single 'change' event with complete day context
  
  Internal State Format (ClojureScript):
  {:display-year 2024
   :display-month 12
   :selected-year 2024    ; nil if no selection
   :selected-month 12     ; nil if no selection  
   :selected-day 25}      ; nil if no selection
   
  Render Functions (Property-first approach):
  - Properties: el.dayContentFn = function(context) { ... } (preferred)
  - Attributes: day-content-fn=\"globalFunctionName\" (fallback)"
  (:require [cljs-bean.core :refer [->clj ->js]]
            [ty.context :as context]
            [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs]
            [ty.value :as value])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load calendar orchestration styles
(defstyles calendar-styles)

(declare render!)

(defn get-locale-with-fallback
  "Get locale with context fallback"
  [^js el]
  (or (value/get-attribute el "locale") context/*locale* "en-US"))

(defn get-current-date
  "Get current date for defaults"
  []
  (let [now (js/Date.)]
    {:year (.getFullYear now)
     :month (inc (.getMonth now))
     :day (.getDate now)}))

(defn parse-year-month-day
  "Parse and validate year/month/day from attributes"
  [^js el]
  (let [year-str (value/get-attribute el "year")
        month-str (value/get-attribute el "month")
        day-str (value/get-attribute el "day")
        current-date (get-current-date)]

    ;; Parse year with validation
    (let [year (if (and year-str (re-matches #"^\d{4}$" year-str))
                 (js/parseInt year-str 10)
                 (:year current-date))

          ;; Parse month with validation (1-12)
          month (if (and month-str (re-matches #"^\d{1,2}$" month-str))
                  (let [m (js/parseInt month-str 10)]
                    (if (and (>= m 1) (<= m 12)) m (:month current-date)))
                  (:month current-date))

          ;; Calculate days in month for validation
          days-in-month (.getDate (js/Date. year month 0))

          ;; Parse day with validation (1-days-in-month)
          day (when (and day-str (re-matches #"^\d{1,2}$" day-str))
                (let [d (js/parseInt day-str 10)]
                  (when (and (>= d 1) (<= d days-in-month)) d)))]

      {:display-year year
       :display-month month
       :selected-year (when day year)
       :selected-month (when day month)
       :selected-day day})))

(defn normalize-size-value
  "Normalize size value - add 'px' if just a number, otherwise use as-is"
  [value]
  (when (and value (not= (str value) ""))
    (if (re-matches #"^\d+(\.\d+)?$" (str value))
      (str value "px")
      (str value))))

(defn apply-width-attributes!
  "Apply width-related attributes as CSS custom properties"
  [^js el]
  (let [width (value/get-attribute el "width")
        min-width (value/get-attribute el "min-width")
        max-width (value/get-attribute el "max-width")]
    (when-let [normalized-width (normalize-size-value width)]
      (.setProperty (.-style el) "--calendar-width" normalized-width))
    (when-let [normalized-min-width (normalize-size-value min-width)]
      (.setProperty (.-style el) "--calendar-min-width" normalized-min-width))
    (when-let [normalized-max-width (normalize-size-value max-width)]
      (.setProperty (.-style el) "--calendar-max-width" normalized-max-width))))

(defn get-render-function
  "Get render function - properties first, then attribute fallback"
  [^js el prop-name attr-name]
  (or
    ;; First try direct property (preferred method)
    (aget el prop-name)
    ;; Fallback to attribute (function name lookup)
    (when-let [fn-name (value/get-attribute el attr-name)]
      (when (and (exists? js/window) (aget js/window fn-name))
        (aget js/window fn-name)))))

(defn init-calendar-state!
  "Initialize internal calendar state from year/month/day attributes"
  [^js el]
  (let [parsed-state (parse-year-month-day el)]
    ;; Store internal state as ClojureScript map (not JS object)
    (set! (.-tyCalendarState el) parsed-state)))

(defn get-calendar-state
  "Get current calendar internal state"
  [^js el]
  (or (.-tyCalendarState el)
      (do (init-calendar-state! el)
          (.-tyCalendarState el))))

(defn update-calendar-state!
  "Update internal calendar state with ClojureScript immutability"
  [^js el updates]
  (let [current-state (get-calendar-state el)
        new-state (merge current-state updates)]
    (set! (.-tyCalendarState el) new-state)
    (render! el)))

(defn update-attributes-from-state!
  "Update HTML attributes to reflect current internal state"
  [^js el]
  (let [state (get-calendar-state el)]
    (when-let [year (:selected-year state)]
      (.setAttribute el "year" (str year)))
    (when-let [month (:selected-month state)]
      (.setAttribute el "month" (str month)))
    (when-let [day (:selected-day state)]
      (.setAttribute el "day" (str day)))))


(defn create-navigation-element
  "Create and configure navigation element with property-based API"
  [^js el state]
  (let [nav (.createElement js/document "ty-calendar-navigation")]
    ;; Set properties (not attributes) for performance
    (set! (.-displayMonth nav) (:display-month state))
    (set! (.-displayYear nav) (:display-year state))
    (set! (.-locale nav) (get-locale-with-fallback el))
    (set! (.-width nav) "100%")

    ;; Store reference for updates
    (set! (.-tyNavigation el) nav)

    ;; Navigation change handler - coordinate with month display
    (.addEventListener nav "change"
                       (fn [^js event]
                         (let [detail (.-detail event)
                               month (.-month detail)
                               year (.-year detail)]
                           ;; Update internal state - clear selection when navigating
                           (update-calendar-state! el {:display-month month
                                                       :display-year year
                                                       :selected-year nil
                                                       :selected-month nil
                                                       :selected-day nil})
                           ;; Update month display
                           (when-let [month-el (.-tyCalendarMonth el)]
                             (set! (.-displayMonth month-el) month)
                             (set! (.-displayYear month-el) year)
                             (set! (.-value month-el) nil)) ; Clear selection
                           ;; Update attributes to reflect cleared selection
                           (.removeAttribute el "year")
                           (.removeAttribute el "month")
                           (.removeAttribute el "day"))))
    nav))

(defn create-month-element
  "Create and configure calendar month element with property-based API"
  [^js el state]
  (let [cal (.createElement js/document "ty-calendar-month")]
    ;; Set properties (not attributes) for performance
    (set! (.-displayMonth cal) (:display-month state))
    (set! (.-displayYear cal) (:display-year state))
    ;; Convert year/month/day to Date for ty-calendar-month
    (when (and (:selected-year state) (:selected-month state) (:selected-day state))
      (set! (.-value cal) (.getTime (js/Date. (:selected-year state)
                                              (dec (:selected-month state))
                                              (:selected-day state)))))
    (set! (.-locale cal) (get-locale-with-fallback el))
    (set! (.-width cal) "100%")

    ;; Pass render functions as properties - check properties first, then attributes
    (when-let [day-content-fn (.-dayContentFn el)]
      (set! (.-dayContentFn cal) day-content-fn))

    ;; Set default day classes function if none provided
    (if-let [day-classes-fn (.-dayClassesFn el)]
      (set! (.-dayClassesFn cal) day-classes-fn)
      (set! (.-dayClassesFn cal)
            (fn [^js context]
              (let [day-context (->clj context)
                    {sel-year :selected-year
                     sel-month :selected-month
                     sel-day :selected-day} (get-calendar-state el)
                    is-selected (and sel-year sel-month sel-day
                                     (= (:year day-context) sel-year)
                                     (= (:month day-context) sel-month)
                                     (= (:day-in-month day-context) sel-day))]
                (cond-> ["calendar-day"]
                  (:today day-context) (conj "today")
                  (:weekend day-context) (conj "weekend")
                  (:other-month day-context) (conj "other-month")
                  is-selected (conj "selected"))))))

    ;; Pass custom CSS as property - direct property passthrough
    (when-let [custom-css (.-customCSS el)]
      (set! (.-customCSS cal) custom-css))

    ;; Store reference for updates
    (set! (.-tyCalendarMonth el) cal)

    ;; Day click handler - update selection and emit event with complete context
    (.addEventListener cal "day-click"
                       (fn [^js event]
                         (.stopPropagation event)
                         (.preventDefault event)
                         (let [detail (.-detail event)
                               day-context (.-dayContext detail)
                               clj-context (->clj day-context)
                               new-year (:year clj-context)
                               new-month (:month clj-context)
                               new-day (:day-in-month clj-context)]
                           ;; Update internal state with new selection
                           (update-calendar-state! el {:selected-year new-year
                                                       :selected-month new-month
                                                       :selected-day new-day
                                                       :display-year new-year
                                                       :display-month new-month})
                           ;; Update HTML attributes to reflect state
                           (update-attributes-from-state! el)
                           ;; Emit change event with complete day context
                           (.dispatchEvent
                             el (js/CustomEvent. "change"
                                                 #js {:detail (.-detail event)
                                                      :bubbles true
                                                      :cancelable true})))))
    cal))

(defn should-show-navigation?
  "Check if navigation should be shown"
  [^js el]
  (let [show-nav (value/get-attribute el "show-navigation")]
    (not= show-nav "false"))) ; Default to true unless explicitly false

(defn render!
  "Render orchestrated calendar with navigation + month display"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        state (get-calendar-state el)]

    ;; Apply width attributes as CSS custom properties
    (apply-width-attributes! el)

    ;; Load styles
    (ensure-styles! root calendar-styles "ty-calendar")

    ;; Clear and rebuild
    (set! (.-innerHTML root) "")

    ;; Create main container
    (let [container (.createElement js/document "div")]
      (set! (.-className container) "calendar-container")

      ;; Add navigation if requested
      (when (should-show-navigation? el)
        (let [nav (create-navigation-element el state)]
          (.appendChild container nav)))

      ;; Add calendar month display
      (let [month-cal (create-month-element el state)]
        (.appendChild container month-cal))

      (.appendChild root container))))

(defn sync-with-attributes!
  "Sync internal state with changed year/month/day attributes"
  [^js el]
  (let [current-state (get-calendar-state el)
        new-state (parse-year-month-day el)]

    ;; Check if state changed externally
    (when (not= current-state new-state)
      ;; Update internal state
      (update-calendar-state! el new-state)

      ;; Update navigation if it exists
      (when-let [nav (.-tyNavigation el)]
        (set! (.-displayMonth nav) (:display-month new-state))
        (set! (.-displayYear nav) (:display-year new-state)))

      ;; Update month display if it exists  
      (when-let [month-el (.-tyCalendarMonth el)]
        (set! (.-displayMonth month-el) (:display-month new-state))
        (set! (.-displayYear month-el) (:display-year new-state))
        ;; Update selection value
        (if (and (:selected-year new-state) (:selected-month new-state) (:selected-day new-state))
          (set! (.-value month-el) (.getTime (js/Date. (:selected-year new-state)
                                                       (dec (:selected-month new-state))
                                                       (:selected-day new-state))))
          (set! (.-value month-el) nil))))))

(defn cleanup!
  "Clean up component references"
  [^js el]
  (set! (.-tyCalendarState el) nil)
  (set! (.-tyNavigation el) nil)
  (set! (.-tyCalendarMonth el) nil))

;; Year/Month/Day attribute-based web component registration with property support
(wcs/define! "ty-calendar"
  {:observed [:year :month :day :show-navigation :locale :width :min-width :max-width
              :day-content-fn :day-classes-fn]
   :props {:dayContentFn nil ; Direct function properties (preferred)
           :dayClassesFn nil ; Functions passed as properties, not attribute names
           :customCSS nil} ; Custom CSS injection for render functions
   :connected (fn [^js el]
                (init-calendar-state! el)
                (render! el))
   :disconnected (fn [^js el]
                   (cleanup! el))
   :attr (fn [^js el attr-name old-value new-value]
           ;; Sync state with attribute changes and re-render
           (sync-with-attributes! el)
           (render! el))
   :prop (fn [^js el prop-name old-value new-value]
           ;; Property changes also trigger re-render
           (render! el))})

;; Attribute-based web component registration with property support

