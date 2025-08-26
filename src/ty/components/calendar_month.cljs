(ns ty.components.calendar-month
  "Calendar month grid component with transparent value handling"
  (:require [ty.css :refer [ensure-styles!]]
            [ty.date.core :as date]
            [ty.shim :as wcs]
            [ty.value :as val])
  (:require-macros [ty.css :refer [defstyles]]))

(declare render!)

;; Load calendar-month styles
(defstyles calendar-month-styles)

;; =====================================================
;; Register Value Handlers with Multimethod
;; =====================================================

(defmethod val/parse-value "TY-CALENDAR-MONTH" [el value]
  (date/parse-value value))

(defmethod val/normalize-value "TY-CALENDAR-MONTH" [el value]
  (date/format-value value))

;; =====================================================
;; State Management
;; =====================================================

(defn get-initial-state
  "Create complete initial calendar state based on element's value and initial attributes"
  [^js el]
  ;; Add this temporarily in get-initial-state
  (js/console.log "Element properties debug:"
                  #js {:all-props (.getOwnPropertyNames js/Object el)
                       :view-year-prop (.-viewYear el)
                       :view-month-prop (.-viewMonth el)
                       :view-month-attr (.getAttribute el "view-month")
                       :has-view-year-attr (.hasAttribute el "view-year")
                       :has-view-month-attr (.hasAttribute el "view-month")})
  (let [initial-value (val/parse-value el (val/get-value el))

        ;; Replicant-compatible: check both properties and attributes
        view-year-str (wcs/attr el "view-year")
        view-month-str (wcs/attr el "view-month")
        min-date-str (or (.-minDate el) (wcs/attr el "min-date"))
        max-date-str (or (.-maxDate el) (wcs/attr el "max-date"))

        ;; Parse the attributes if they exist
        initial-view-year (val/parse-integer view-year-str)
        initial-view-month (val/parse-integer view-month-str)
        initial-min-date (when min-date-str (date/parse-value min-date-str))
        initial-max-date (when max-date-str (date/parse-value max-date-str))]

    ;; Debug logging
    (js/console.log "Calendar initial state debug:"
                    #js {:element-tag (.-tagName el)
                         :view-year-prop (.-viewYear el)
                         :view-year-attr (wcs/attr el "view-year")
                         :view-year-final view-year-str
                         :view-month-prop (.-viewMonth el)
                         :view-month-attr (wcs/attr el "view-month")
                         :view-month-final view-month-str
                         :parsed-year initial-view-year
                         :parsed-month initial-view-month})

    (let [;; Determine view year/month
          {:keys [view-year view-month]}
          (cond
            ;; Use initial attributes if at least one is provided
            (or initial-view-year initial-view-month)
            (do
              (js/console.log "Using initial navigation (partial):"
                              #js {:year (or initial-view-year (.getFullYear (js/Date.)))
                                   :month (or initial-view-month (inc (.getMonth (js/Date.))))
                                   :had-year (boolean initial-view-year)
                                   :had-month (boolean initial-view-month)})
              {:view-year (or initial-view-year (.getFullYear (js/Date.)))
               :view-month (or initial-view-month (inc (.getMonth (js/Date.))))})

            ;; If value exists, show that month/year
            initial-value
            (let [ctx (date/day-time-context initial-value)]
              (js/console.log "Using value date:" #js {:year (:year ctx)
                                                       :month (:month ctx)})
              {:view-year (:year ctx)
               :view-month (:month ctx)})

            ;; Otherwise show current month/year
            :else
            (do
              (js/console.log "Using current date")
              {:view-year (.getFullYear (js/Date.))
               :view-month (inc (.getMonth (js/Date.)))}))]
      ;; Return complete initial state
      {:selected-value initial-value
       :view-year view-year
       :view-month view-month
       :focused-day nil
       ;; Store initial constraints for reference
       :initial-min-date initial-min-date
       :initial-max-date initial-max-date})))

(defn get-calendar-state
  "Get or initialize calendar component state"
  [^js el]
  (or (.-tyCalendarState el)
      (let [initial-state (get-initial-state el)]
        (set! (.-tyCalendarState el) initial-state)
        initial-state)))

(defn set-calendar-state!
  "Update calendar component state"
  [^js el updates]
  (let [new-state (merge (get-calendar-state el) updates)]
    (set! (.-tyCalendarState el) new-state)
    ;; Also update via the value system if selected-value changed
    (when (contains? updates :selected-value)
      (set! (.-tyUpdateState el) (partial set-calendar-state! el)))
    new-state))

(defn sync-calendar-value!
  "Sync value and optionally update view to show that month."
  [^js el raw-value & {:keys [update-view?]
                       :or {update-view? true}}]
  (let [parsed (val/sync-value! el raw-value)]
    ;; Update our internal state
    (set-calendar-state! el {:selected-value parsed})
    ;; If we have a valid value and should update view, navigate to that month/year
    (when (and parsed update-view?)
      (let [ctx (date/day-time-context parsed)]
        (set-calendar-state! el {:view-year (:year ctx)
                                 :view-month (:month ctx)})))
    parsed))

;; =====================================================
;; Other Attribute Getters
;; =====================================================

(defn get-min-date
  "Get minimum date constraint - prefers initial value over current attribute"
  [^js el]
  (let [{:keys [initial-min-date]} (get-calendar-state el)]
    (or initial-min-date
        (date/parse-value (or (.-minDate el)
                              (wcs/attr el "min-date"))))))

(defn get-max-date
  "Get maximum date constraint - prefers initial value over current attribute"
  [^js el]
  (let [{:keys [initial-max-date]} (get-calendar-state el)]
    (or initial-max-date
        (date/parse-value (or (.-maxDate el)
                              (wcs/attr el "max-date"))))))

(defn get-disabled-dates
  "Get disabled dates as a set of values"
  [^js el]
  (let [disabled (or (.-disabledDates el)
                     (wcs/attr el "disabled-dates"))]
    (cond
      (nil? disabled) #{}
      (set? disabled) disabled
      (sequential? disabled) (set (map date/parse-value disabled))
      (string? disabled)
      (if (= disabled "")
        #{}
        (set (map date/parse-value (.split disabled ","))))
      :else #{})))

(defn is-selectable?
  "Check if a day can be selected based on constraints"
  [day-value min-date max-date disabled-dates]
  (and (date/in-range? day-value min-date max-date)
       (not (contains? disabled-dates day-value))))

;; =====================================================
;; Event Dispatching
;; =====================================================

(defn dispatch-date-select!
  "Dispatch date selection event with detailed information"
  [^js el value day-context]
  (let [detail #js {:value value
                    :date (date/value->time value)
                    :formatted (date/format-value value)
                    :context (clj->js day-context)}
        event (js/CustomEvent. "date-select"
                               #js {:detail detail
                                    :bubbles true
                                    :cancelable true})]
    (.dispatchEvent el event)))

(defn dispatch-month-change!
  "Dispatch month navigation event"
  [^js el year month]
  (let [detail #js {:year year
                    :month month}
        event (js/CustomEvent. "month-change"
                               #js {:detail detail
                                    :bubbles true
                                    :cancelable false})]
    (.dispatchEvent el event)))

;; =====================================================
;; Event Handlers
;; =====================================================

(defn handle-day-click!
  "Handle click on a calendar day"
  [^js el ^js shadow-root day-context min-date max-date disabled-dates ^js event]
  (.preventDefault event)
  (let [value (:value day-context)]
    (cond
      ;; Don't select other month days unless explicitly allowed
      (:other-month day-context)
      (when (wcs/parse-bool-attr el "allow-other-month")
        (let [{:keys [year month]} day-context]
          (set-calendar-state! el {:view-year year
                                   :view-month month})
          (dispatch-month-change! el year month)
          (render! el)))

      ;; Check if selectable
      (is-selectable? value min-date max-date disabled-dates)
      (do
        ;; Sync value (don't update view since user is already looking at this month)
        (sync-calendar-value! el value :update-view? false)
        (dispatch-date-select! el value day-context)
        (render! el))

      ;; Otherwise do nothing (disabled day)
      :else nil)))

(defn handle-key-down!
  "Handle keyboard navigation in calendar"
  [^js el ^js event]
  (let [key (.-key event)
        {:keys [focused-day view-year view-month]} (get-calendar-state el)
        days (date/calendar-month-days view-year view-month)
        current-index (when focused-day
                        (.findIndex (to-array days)
                                    #(= (:value %) focused-day)))]
    (case key
      "ArrowLeft"
      (when (and current-index (> current-index 0))
        (.preventDefault event)
        (let [new-day (nth days (dec current-index))]
          (set-calendar-state! el {:focused-day (:value new-day)})
          (render! el)))

      "ArrowRight"
      (when (and current-index (< current-index 41))
        (.preventDefault event)
        (let [new-day (nth days (inc current-index))]
          (set-calendar-state! el {:focused-day (:value new-day)})
          (render! el)))

      "ArrowUp"
      (when (and current-index (>= current-index 7))
        (.preventDefault event)
        (let [new-day (nth days (- current-index 7))]
          (set-calendar-state! el {:focused-day (:value new-day)})
          (render! el)))

      "ArrowDown"
      (when (and current-index (<= current-index 34))
        (.preventDefault event)
        (let [new-day (nth days (+ current-index 7))]
          (set-calendar-state! el {:focused-day (:value new-day)})
          (render! el)))

      ("Enter" " ")
      (when focused-day
        (.preventDefault event)
        (let [day-context (first (filter #(= (:value %) focused-day) days))
              min-date (get-min-date el)
              max-date (get-max-date el)
              disabled-dates (get-disabled-dates el)]
          (when (and day-context
                     (not (:other-month day-context))
                     (is-selectable? focused-day min-date max-date disabled-dates))
            ;; Sync value (don't update view)
            (sync-calendar-value! el focused-day :update-view? false)
            (dispatch-date-select! el focused-day day-context)
            (render! el))))

      ;; Default - do nothing
      nil)))

;; =====================================================
;; Rendering
;; =====================================================

(defn render-day-cell
  "Render a single day cell with all states"
  [^js el ^js shadow-root day-context selected-value today-value
   min-date max-date disabled-dates focused-day]
  (let [value (:value day-context)
        is-selected (and selected-value (date/same-day? value selected-value))
        is-today (date/same-day? value today-value)
        is-weekend (:weekend? day-context)
        is-other-month (:other-month day-context)
        is-disabled (or (not (date/in-range? value min-date max-date))
                        (contains? disabled-dates value))
        is-focused (and focused-day (= value focused-day))

        classes (cond-> ["calendar-day"]
                  is-selected (conj "selected")
                  is-today (conj "today")
                  is-weekend (conj "weekend")
                  is-other-month (conj "other-month")
                  is-disabled (conj "disabled")
                  is-focused (conj "focused"))

        day-element (doto (.createElement js/document "div")
                      (-> .-className (set! (clojure.string/join " " classes)))
                      (-> .-textContent (set! (str (:day-in-month day-context)))))]

    ;; Set ARIA attributes for accessibility
    (.setAttribute day-element "role" "button")
    (.setAttribute day-element "aria-label"
                   (date/format-date value "en-US"
                                     {:weekday "long"
                                      :year "numeric"
                                      :month "long"
                                      :day "numeric"}))
    (when is-selected
      (.setAttribute day-element "aria-selected" "true"))
    (when is-disabled
      (.setAttribute day-element "aria-disabled" "true"))
    (when is-focused
      (.setAttribute day-element "tabindex" "0"))

    ;; Add click handler if not disabled
    (when-not (or is-disabled is-other-month)
      (.addEventListener day-element "click"
                         (partial handle-day-click! el shadow-root day-context
                                  min-date max-date disabled-dates)))

    day-element))

(defn render!
  "Main render function for calendar month"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        {:keys [view-year view-month selected-value focused-day]} (get-calendar-state el)

        ;; Get constraints
        min-date (get-min-date el)
        max-date (get-max-date el)
        disabled-dates (get-disabled-dates el)

        ;; Generate calendar days
        days (date/calendar-month-days view-year view-month)
        today-value (date/today)

        ;; Get weekday names
        locale (or (wcs/attr el "locale") "en-US")
        weekday-names (date/get-weekday-names locale "short")

        ;; Get first day of week (0 = Sunday, 1 = Monday)
        first-day-of-week (val/parse-attr-int el "first-day-of-week" 1)

        ;; Rotate weekday names based on first day of week
        ordered-weekdays (if (= first-day-of-week 0)
                           weekday-names
                           (vec (concat (drop first-day-of-week weekday-names)
                                        (take first-day-of-week weekday-names))))]

    ;; Ensure styles are loaded
    (ensure-styles! root calendar-month-styles "ty-calendar-month")

    ;; Clear and rebuild content
    (set! (.-innerHTML root) "")

    ;; Create container
    (let [container (.createElement js/document "div")]
      (set! (.-className container) "calendar-month-container")

      ;; Set tabindex for keyboard navigation
      (.setAttribute container "tabindex" "0")
      (.addEventListener container "keydown" (partial handle-key-down! el))

      ;; Add weekday headers
      (let [header-row (.createElement js/document "div")]
        (set! (.-className header-row) "calendar-weekdays")
        (doseq [weekday ordered-weekdays]
          (let [header-cell (.createElement js/document "div")]
            (set! (.-className header-cell) "calendar-weekday")
            (set! (.-textContent header-cell) weekday)
            (.appendChild header-row header-cell)))
        (.appendChild container header-row))

      ;; Add day cells
      (let [days-grid (.createElement js/document "div")]
        (set! (.-className days-grid) "calendar-days-grid")

        ;; Reorder days based on first day of week if needed
        (let [ordered-days (if (= first-day-of-week 0)
                             days
                             ;; Reorganize days to start with Monday
                             (let [weeks (partition 7 days)]
                               (mapcat (fn [week]
                                         (vec (concat (drop first-day-of-week week)
                                                      (take first-day-of-week week))))
                                       weeks)))]
          (doseq [day-context ordered-days]
            (let [day-cell (render-day-cell el root day-context
                                            selected-value today-value
                                            min-date max-date disabled-dates focused-day)]
              (.appendChild days-grid day-cell))))
        (.appendChild container days-grid))

      (.appendChild root container))))

(defn cleanup!
  "Cleanup function for calendar month"
  [^js el]
  (set! (.-tyCalendarState el) nil)
  (set! (.-tyUpdateState el) nil)
  (set! (.-tyRender el) nil)
  (set! (.-tyInitialAttrsRead el) nil))

;; =====================================================
;; Web Component Registration
;; =====================================================

(wcs/define-batched! "ty-calendar-month"
  {:observed [:value :view-year :view-month :min-date :max-date :disabled-dates
              :locale :first-day-of-week :allow-other-month]
   :connected (fn [^js el]
               ;; Store render function for use by value system
                (set! (.-tyRender el) render!)
               ;; Setup value handling
                (val/setup-component! el (partial set-calendar-state! el))
               ;; Initialize state (which considers initial value and initial attrs for view)
                (get-calendar-state el)
                (render! el))
   :disconnected cleanup!
   :batch-attr (fn [^js el changes]
           ;; Handle view navigation changes together
                 (let [view-year (get changes "view-year")
                       view-month (get changes "view-month")
                       updates (cond-> {}
                                 view-year (assoc :view-year (val/parse-integer view-year))
                                 view-month (assoc :view-month (val/parse-integer view-month)))]
                   (when (seq updates)
                     (set-calendar-state! el updates)))

           ;; Re-render once at the end  
                 (render! el))})
