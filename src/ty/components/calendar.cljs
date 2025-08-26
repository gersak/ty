(ns ty.components.calendar
  "Full calendar component with month/year navigation"
  (:require [ty.css :refer [ensure-styles!]]
            [ty.date.core :as date]
            [ty.shim :as wcs]
            [ty.value :as val])
  (:require-macros [ty.css :refer [defstyles]]))

(declare render!)

;; Load calendar styles
(defstyles calendar-styles)

;; =====================================================
;; Register Value Handlers with Multimethod  
;; =====================================================

(defmethod val/parse-value "TY-CALENDAR" [el value]
  (date/parse-value value))

(defmethod val/normalize-value "TY-CALENDAR" [el value]
  (date/format-value value))

;; =====================================================
;; State Management
;; =====================================================

(defn get-initial-state
  "Create initial calendar state with navigation"
  [^js el]
  (js/console.log "TY-CALENDAR DEBUG - ELEMENT ATTRS:"
                  #js {:view-year (wcs/attr el "view-year")
                       :view-month (wcs/attr el "view-month")})
  (let [initial-value (val/parse-value el (val/get-value el))

        ;; Get initial navigation attributes using initial-only pattern
        initial-attrs (wcs/get-initial-attrs el
                                             {:view-year js/parseInt
                                              :view-month js/parseInt
                                              :min-date date/parse-value
                                              :max-date date/parse-value})
        _ (js/console.log "TY-CALENDAR INITIAL ATTRS:" initial-attrs)

        ;; Determine view year/month
        {:keys [view-year view-month]}
        (cond
          ;; Use initial attributes if provided
          (or (:view-year initial-attrs) (:view-month initial-attrs))
          {:view-year (or (:view-year initial-attrs) (.getFullYear (js/Date.)))
           :view-month (or (:view-month initial-attrs) (inc (.getMonth (js/Date.))))}

          ;; If value exists, show that month/year
          initial-value
          (let [ctx (date/day-time-context initial-value)]
            {:view-year (:year ctx) :view-month (:month ctx)})

          ;; Otherwise show current month/year
          :else
          {:view-year (.getFullYear (js/Date.))
           :view-month (inc (.getMonth (js/Date.)))})]

    ;; Return complete initial state
    {:selected-value initial-value
     :view-year view-year
     :view-month view-month
     :initial-min-date (:min-date initial-attrs)
     :initial-max-date (:max-date initial-attrs)}))

(defn get-calendar-state
  "Get or initialize calendar state"
  [^js el]
  (or (.-tyCalendarState el)
      (let [initial-state (get-initial-state el)]
        (set! (.-tyCalendarState el) initial-state)
        initial-state)))

(defn set-calendar-state!
  "Update calendar state"
  [^js el updates]
  (let [new-state (merge (get-calendar-state el) updates)]
    (set! (.-tyCalendarState el) new-state)
    ;; Also update via the value system if selected-value changed
    (when (contains? updates :selected-value)
      (val/sync-value! el (:selected-value new-state)))
    new-state))

;; =====================================================
;; Navigation Logic  
;; =====================================================

(defn navigate-month!
  "Navigate to a specific month/year and re-render"
  [^js el year month]
  (set-calendar-state! el {:view-year year :view-month month})
  (render! el)
  ;; Dispatch navigation event
  (let [detail #js {:year year :month month}
        event (js/CustomEvent. "month-change"
                               #js {:detail detail
                                    :bubbles true
                                    :cancelable false})]
    (.dispatchEvent el event)))

(defn navigate-previous-month!
  "Navigate to previous month"
  [^js el]
  (let [{:keys [view-year view-month]} (get-calendar-state el)]
    (if (= view-month 1)
      (navigate-month! el (dec view-year) 12)
      (navigate-month! el view-year (dec view-month)))))

(defn navigate-next-month!
  "Navigate to next month"
  [^js el]
  (let [{:keys [view-year view-month]} (get-calendar-state el)]
    (if (= view-month 12)
      (navigate-month! el (inc view-year) 1)
      (navigate-month! el view-year (inc view-month)))))

(defn navigate-to-today!
  "Navigate to current month/year"
  [^js el]
  (let [now (js/Date.)
        current-year (.getFullYear now)
        current-month (inc (.getMonth now))]
    (navigate-month! el current-year current-month)))

;; =====================================================
;; Dropdown Options Generation
;; =====================================================

(defn generate-year-options
  "Generate year options around current year"
  [current-year]
  (let [start-year (- current-year 10)
        end-year (+ current-year 10)]
    (map (fn [year] {:value year :label (str year)})
         (range start-year (inc end-year)))))

(defn generate-month-options
  "Generate month options with localized names"
  [locale]
  (let [month-names (date/get-month-names locale "long")]
    (map-indexed (fn [idx name]
                   {:value (inc idx) :label name})
                 month-names)))

;; =====================================================
;; Event Handlers
;; =====================================================

(defn handle-month-select!
  "Handle month dropdown selection"
  [^js el month]
  (let [{:keys [view-year]} (get-calendar-state el)]
    (navigate-month! el view-year month)))

(defn handle-year-select!
  "Handle year dropdown selection"
  [^js el year]
  (let [{:keys [view-month]} (get-calendar-state el)]
    (navigate-month! el year view-month)))

(defn handle-calendar-month-events!
  "Forward events from embedded calendar month"
  [^js el ^js event]
  (case (.-type event)
    "date-select"
    (do
      ;; Update our state to match
      (let [detail (.-detail event)
            value (.-value detail)]
        (set-calendar-state! el {:selected-value value}))
      ;; Forward the event
      (let [forwarded-event (js/CustomEvent. "date-select"
                                             #js {:detail (.-detail event)
                                                  :bubbles true
                                                  :cancelable true})]
        (.dispatchEvent el forwarded-event)))

    "month-change"
    (do
      ;; Update our navigation state 
      (let [detail (.-detail event)
            year (.-year detail)
            month (.-month detail)]
        (set-calendar-state! el {:view-year year :view-month month}))
      ;; Re-render to update dropdowns
      (render! el))

    ;; Default - do nothing
    nil))

;; =====================================================
;; Rendering
;; =====================================================

(defn render-navigation-header
  "Render the navigation header with dropdowns and buttons"
  [^js el ^js root]
  (let [{:keys [view-year view-month]} (get-calendar-state el)
        locale (or (wcs/attr el "locale") "en-US")
        show-today (wcs/parse-bool-attr el "show-today-button")

        ;; Create header container
        header (.createElement js/document "div")]

    (set! (.-className header) "calendar-header")

    ;; Previous month button
    (let [prev-btn (.createElement js/document "button")]
      (set! (.-className prev-btn) "nav-button nav-prev")
      (set! (.-innerHTML prev-btn) "‹")
      (set! (.-title prev-btn) "Previous month")
      (.addEventListener prev-btn "click"
                         (fn [e]
                           (.preventDefault e)
                           (navigate-previous-month! el)))
      (.appendChild header prev-btn))

    ;; Month dropdown
    (let [month-select (.createElement js/document "select")
          month-options (generate-month-options locale)]
      (set! (.-className month-select) "month-select")
      (doseq [{:keys [value label]} month-options]
        (let [option (.createElement js/document "option")]
          (set! (.-value option) (str value))
          (set! (.-textContent option) label)
          (when (= value view-month)
            (set! (.-selected option) true))
          (.appendChild month-select option)))
      (.addEventListener month-select "change"
                         (fn [e]
                           (handle-month-select! el (js/parseInt (.-value (.-target e))))))
      (.appendChild header month-select))

    ;; Year dropdown  
    (let [year-select (.createElement js/document "select")
          year-options (generate-year-options view-year)]
      (set! (.-className year-select) "year-select")
      (doseq [{:keys [value label]} year-options]
        (let [option (.createElement js/document "option")]
          (set! (.-value option) (str value))
          (set! (.-textContent option) label)
          (when (= value view-year)
            (set! (.-selected option) true))
          (.appendChild year-select option)))
      (.addEventListener year-select "change"
                         (fn [e]
                           (handle-year-select! el (js/parseInt (.-value (.-target e))))))
      (.appendChild header year-select))

    ;; Next month button
    (let [next-btn (.createElement js/document "button")]
      (set! (.-className next-btn) "nav-button nav-next")
      (set! (.-innerHTML next-btn) "›")
      (set! (.-title next-btn) "Next month")
      (.addEventListener next-btn "click"
                         (fn [e]
                           (.preventDefault e)
                           (navigate-next-month! el)))
      (.appendChild header next-btn))

    ;; Today button (optional)
    (when show-today
      (let [today-btn (.createElement js/document "button")]
        (set! (.-className today-btn) "today-button")
        (set! (.-textContent today-btn) "Today")
        (set! (.-title today-btn) "Go to current month")
        (.addEventListener today-btn "click"
                           (fn [e]
                             (.preventDefault e)
                             (navigate-to-today! el)))
        (.appendChild header today-btn)))

    header))

(defn render!
  "Main render function for full calendar"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        {:keys [view-year view-month selected-value]} (get-calendar-state el)]

    ;; Ensure styles are loaded
    (ensure-styles! root calendar-styles "ty-calendar")

    ;; Clear and rebuild content
    (set! (.-innerHTML root) "")

    ;; Create main container
    (let [container (.createElement js/document "div")]
      (set! (.-className container) "calendar-container")

      ;; Add navigation header
      (.appendChild container (render-navigation-header el root))

      ;; Add embedded calendar month
      (let [calendar-month (.createElement js/document "ty-calendar-month")]
        ;; Set attributes on the embedded calendar
        (do
          (js/console.log "CALENDAR RENDER STATE:" #js {:view-year view-year :view-month view-month})
          (.setAttribute calendar-month "view-year" (str view-year)))
        (.setAttribute calendar-month "view-month" (str view-month))

        ;; Pass through value
        (when selected-value
          (.setAttribute calendar-month "value" (date/format-value selected-value)))

        ;; Pass through constraint attributes
        (when-let [min-date (or (wcs/attr el "min-date")
                                (when-let [initial (:initial-min-date (get-calendar-state el))]
                                  (date/format-value initial)))]
          (.setAttribute calendar-month "min-date" min-date))

        (when-let [max-date (or (wcs/attr el "max-date")
                                (when-let [initial (:initial-max-date (get-calendar-state el))]
                                  (date/format-value initial)))]
          (.setAttribute calendar-month "max-date" max-date))

        ;; Pass through other attributes  
        (when-let [disabled-dates (wcs/attr el "disabled-dates")]
          (.setAttribute calendar-month "disabled-dates" disabled-dates))

        (when-let [locale (wcs/attr el "locale")]
          (.setAttribute calendar-month "locale" locale))

        (when-let [first-day (wcs/attr el "first-day-of-week")]
          (.setAttribute calendar-month "first-day-of-week" first-day))

        (when (wcs/parse-bool-attr el "allow-other-month")
          (.setAttribute calendar-month "allow-other-month" "true"))

        ;; Add event listeners to forward events
        (.addEventListener calendar-month "date-select"
                           (partial handle-calendar-month-events! el))
        (.addEventListener calendar-month "month-change"
                           (partial handle-calendar-month-events! el))

        (.appendChild container calendar-month))

      (.appendChild root container))))

(defn cleanup!
  "Cleanup function for calendar"
  [^js el]
  (set! (.-tyCalendarState el) nil)
  (set! (.-tyRender el) nil)
  (set! (.-tyInitialAttrsRead el) nil))

;; =====================================================
;; Web Component Registration
;; =====================================================

(wcs/define! "ty-calendar"
  {:observed [:value :view-year :view-month :min-date :max-date :disabled-dates
              :locale :first-day-of-week :allow-other-month :show-today-button]
   :connected (fn [^js el]
                ;; Store render function for use by value system
                (set! (.-tyRender el) render!)
                ;; Setup value handling
                (val/setup-component! el (partial set-calendar-state! el))
                ;; Initialize state
                (get-calendar-state el)
                (render! el))
   :disconnected cleanup!
   :attr (fn [^js el attr-name old-value new-value]
           (case attr-name
             "value"
             ;; Use the simplified handler
             (val/handle-attr-change el attr-name old-value new-value render!)

             ;; Initial-only attributes - ignore after connection
             ("view-year" "view-month" "min-date" "max-date")
             nil

             ;; Reactive attributes that trigger re-render
             ("disabled-dates" "locale" "first-day-of-week" "allow-other-month" "show-today-button")
             (render! el)

             ;; Default
             (render! el)))})
