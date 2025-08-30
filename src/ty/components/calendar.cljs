(ns ty.components.calendar
  "Attribute-based orchestrated calendar component with property-based composition.
  
  Architecture:
  - HTML attributes for user-friendly API
  - Internal ClojureScript state management (immutable maps)
  - Distributes properties to child components (navigation + month)
  - Child components use property-based APIs for performance
  - Event coordination between navigation and month display
  
  Internal State Format (ClojureScript):
  {:display-month 12
   :display-year 2024
   :selected-value #inst \"2024-12-25T00:00:00Z\"}
   
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
  (js/Date.))

(defn parse-date-value
  "Parse date value from various formats"
  [value]
  (when (and value (not= value ""))
    (cond
      (string? value) (js/Date. value)
      (number? value) (js/Date. value)
      :else value)))

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
  "Initialize internal calendar state from attributes"
  [^js el]
  (let [today (get-current-date)
        initial-value (parse-date-value (value/get-attribute el "value"))
        display-month (if initial-value
                        (inc (.getMonth initial-value))
                        (inc (.getMonth today)))
        display-year (if initial-value
                       (.getFullYear initial-value)
                       (.getFullYear today))]

    ;; Store internal state as ClojureScript map (not JS object)
    (set! (.-tyCalendarState el)
          {:display-month display-month
           :display-year display-year
           :selected-value initial-value})))

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

(defn emit-value-change!
  "Emit value-change event when selection changes"
  [^js el new-value source]
  (let [event (js/CustomEvent. "value-change"
                               #js {:detail #js {:value new-value
                                                 :source source}
                                    :bubbles true
                                    :cancelable true})]
    (.dispatchEvent el event)))

(defn emit-navigation-change!
  "Emit navigation-change event when display month/year changes"
  [^js el month year source]
  (let [event (js/CustomEvent. "navigation-change"
                               #js {:detail #js {:month month
                                                 :year year
                                                 :source source}
                                    :bubbles true
                                    :cancelable true})]
    (.dispatchEvent el event)))

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
                           ;; Update internal state
                           (update-calendar-state! el {:display-month month
                                                       :display-year year})
                           ;; Update month display
                           (when-let [month-el (.-tyCalendarMonth el)]
                             (set! (.-displayMonth month-el) month)
                             (set! (.-displayYear month-el) year))
                           ;; Emit navigation change event
                           (emit-navigation-change! el month year "navigation"))))
    nav))

(defn create-month-element
  "Create and configure calendar month element with property-based API"
  [^js el state]
  (let [cal (.createElement js/document "ty-calendar-month")]
    ;; Set properties (not attributes) for performance
    (set! (.-displayMonth cal) (:display-month state))
    (set! (.-displayYear cal) (:display-year state))
    (set! (.-value cal) (:selected-value state))
    (set! (.-locale cal) (get-locale-with-fallback el))
    (set! (.-width cal) "100%")

    ;; Pass render functions as properties - check properties first, then attributes
    (when-let [day-content-fn (get-render-function el "dayContentFn" "day-content-fn")]
      (set! (.-dayContentFn cal) day-content-fn))
    (when-let [day-classes-fn (get-render-function el "dayClassesFn" "day-classes-fn")]
      (set! (.-dayClassesFn cal) day-classes-fn))

    ;; Pass custom CSS as property - direct property passthrough
    (when-let [custom-css (.-customCSS el)]
      (set! (.-customCSS cal) custom-css))

    ;; Store reference for updates
    (set! (.-tyCalendarMonth el) cal)

    ;; Day click handler - update selection and emit event
    (.addEventListener cal "day-click"
                       (fn [^js event]
                         (let [detail (.-detail event)
                               new-value (.-value detail)]
                           ;; Update internal state
                           (update-calendar-state! el {:selected-value new-value})
                           ;; Update HTML attribute to reflect state
                           (.setAttribute el "value" (if new-value (.toISOString new-value) ""))
                           ;; Emit value change event
                           (emit-value-change! el new-value "day-click"))))
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
  "Sync internal state with changed attributes"
  [^js el]
  (let [state (get-calendar-state el)
        new-value (parse-date-value (value/get-attribute el "value"))
        current-value (:selected-value state)]

    ;; Check if value changed externally
    (when (not= new-value current-value)
      ;; Update internal state
      (update-calendar-state! el {:selected-value new-value})

      ;; Navigate to month of new value if provided
      (when new-value
        (let [new-month (inc (.getMonth new-value))
              new-year (.getFullYear new-value)]
          (update-calendar-state! el {:display-month new-month
                                      :display-year new-year})

          ;; Update navigation if it exists
          (when-let [nav (.-tyNavigation el)]
            (set! (.-displayMonth nav) new-month)
            (set! (.-displayYear nav) new-year))

          ;; Update month display if it exists  
          (when-let [month-el (.-tyCalendarMonth el)]
            (set! (.-displayMonth month-el) new-month)
            (set! (.-displayYear month-el) new-year))))

      ;; Update month display value
      (when-let [month-el (.-tyCalendarMonth el)]
        (set! (.-value month-el) new-value)))))

(defn cleanup!
  "Clean up component references"
  [^js el]
  (set! (.-tyCalendarState el) nil)
  (set! (.-tyNavigation el) nil)
  (set! (.-tyCalendarMonth el) nil))

;; Attribute-based web component registration with property support
(wcs/define! "ty-calendar"
  {:observed [:value :show-navigation :locale :width :min-width :max-width
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
