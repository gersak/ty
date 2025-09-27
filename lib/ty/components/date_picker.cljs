(ns ty.components.date-picker
  "Date picker component - read-only input with calendar dropdown"
  (:require [cljs-bean.core :refer [->clj ->js]]
            [clojure.string :as str]
            [goog.string :as gstr]
            [ty.css :refer [ensure-styles!]]
            [ty.date.core :as date]
            [ty.i18n :refer [translate]]
            [ty.i18n.time :as time]
            [ty.scroll-lock :as scroll-lock]
            [ty.shim :as wcs]
            [ty.value :as value])
  (:require-macros [ty.css :refer [defstyles]]))

;; Icons
(def calendar-icon-svg
  "<svg stroke='currentColor' fill='none' stroke-width='2' viewBox='0 0 24 24' width='16' height='16' xmlns='http://www.w3.org/2000/svg'><rect x='3' y='4' width='18' height='18' rx='2' ry='2'></rect><line x1='16' y1='2' x2='16' y2='6'></line><line x1='8' y1='2' x2='8' y2='6'></line><line x1='3' y1='10' x2='21' y2='10'></line></svg>")

(def clear-icon-svg
  "<svg stroke='currentColor' fill='none' stroke-width='2' viewBox='0 0 24 24' width='14' height='14' xmlns='http://www.w3.org/2000/svg'><line x1='18' y1='6' x2='6' y2='18'></line><line x1='6' y1='6' x2='18' y2='18'></line></svg>")

(def schedule-icon-svg
  "<svg stroke='currentColor' fill='none' stroke-width='2' viewBox='0 0 24 24' width='16' height='16' xmlns='http://www.w3.org/2000/svg'><circle cx='12' cy='12' r='10'></circle><polyline points='12,6 12,12 16,14'></polyline></svg>")

(def required-icon
  "<span class=\"required-icon\">*</span>")

(defstyles date-picker-styles)

(declare render!)

(defn get-internals
  "Get ElementInternals for form participation"
  [^js el]
  (.-_internals el))

;; === SIMPLIFIED CORE: PARSE ONCE, USE TIMING LIBRARY ===

(defn parse-value
  "Parse ANY input format into year/month/day/hour/minute components.
   Accepts:
   - UTC strings: '2024-09-21T08:30:00Z' or '2024-09-21T08:30:00.000Z'
   - Datetime-local: '2024-09-21T10:30'
   - Date only: '2024-09-21'
   - Timestamps: milliseconds since epoch
   - With timezone: '2024-09-21T10:30:00+02:00'
   
   Always extracts components in LOCAL timezone for display/editing."
  [value with-time?]
  (when value
    (let [date-obj (cond
                     ;; String inputs
                     (string? value)
                     (cond
                       ;; Date-only format: YYYY-MM-DD
                       (re-matches #"^\d{4}-\d{2}-\d{2}$" value)
                       (let [parts (str/split value #"-")
                             year (js/parseInt (nth parts 0) 10)
                             month (dec (js/parseInt (nth parts 1) 10)) ; 0-based for JS Date
                             day (js/parseInt (nth parts 2) 10)]
                         ;; Create at midnight local time
                         (js/Date. year month day 0 0 0 0))

                       ;; Otherwise let JS Date handle it (datetime-local, UTC, with timezone)
                       :else
                       (js/Date. value))

                     ;; Numeric inputs (milliseconds)
                     (number? value)
                     (js/Date. value)

                     ;; Date object inputs
                     (instance? js/Date value)
                     value

                     :else nil)]

      (when (and date-obj (not (js/isNaN (.getTime date-obj))))
        ;; Extract components in LOCAL timezone for display
        (let [components {:year (.getFullYear date-obj)
                          :month (inc (.getMonth date-obj)) ; Convert to 1-based
                          :day (.getDate date-obj)
                          :hour (if with-time?
                                  (.getHours date-obj)
                                  0) ; Default to midnight for date-only
                          :minute (if with-time?
                                    (.getMinutes date-obj)
                                    0)}] ; Default to 00 minutes for date-only

          ;; For date-only mode, ensure we're using midnight
          (if with-time?
            components
            (assoc components :hour 0 :minute 0)))))))

(defn components->date-object
  "Convert internal components to Date object using timing library"
  [{:keys [year month day hour minute]}]
  (when (and year month day)
    (if (and hour minute)
      ;; Use timing library for date+time
      (date/date year month day hour minute)
      ;; Use timing library for date only
      (date/date year month day))))

(defn components->output-value
  "Convert internal components to UTC ISO 8601 format.
   
   For date+time mode: Outputs full UTC timestamp with milliseconds
   Example: '2024-09-21T08:30:00.000Z'
   
   For date-only mode: Outputs UTC timestamp at midnight local time
   Example: '2024-09-20T22:00:00.000Z' (midnight Sept 21 CEST = 10pm Sept 20 UTC)
   
   Always returns UTC to ensure unambiguous server communication."
  [components with-time?]
  (when (and components
             (:year components)
             (:month components)
             (:day components))
    ;; For both date-only and date+time, we need hour/minute
    ;; Date-only uses 00:00 (midnight)
    (let [hour (or (:hour components) 0)
          minute (or (:minute components) 0)
          ;; Use timing library to create proper local date
          date-obj (date/date (:year components)
                              (:month components)
                              (:day components)
                              hour
                              minute)]
      ;; Always output UTC ISO string
      (when date-obj
        (.toISOString date-obj)))))

;; === SIMPLIFIED STATE MANAGEMENT ===

(defn parse-date-picker-attributes
  "Parse date picker state from attributes - following calendar pattern"
  [^js el]
  (let [value-attr (or (value/get-attribute el "value") (.-value el))
        with-time? (= (value/get-attribute el "with-time") "true")
        components (parse-value value-attr with-time?)]
    (merge components {:with-time with-time?
                       :open? false})))

(defn init-component-state!
  "Initialize component state with UTC value conversion.
   Parses input value, stores local components for editing,
   and immediately updates value attribute to UTC format."
  [^js el]
  (let [initial-state (parse-date-picker-attributes el)
        ;; Get the components for UTC conversion
        components (select-keys initial-state [:year :month :day :hour :minute])
        with-time? (:with-time initial-state)]

    ;; Store state on element
    (set! (.-tyDatePickerState el) initial-state)

    ;; Convert to UTC and update both attribute and property
    (when (and (:year components) (:month components) (:day components))
      (let [utc-value (components->output-value components with-time?)]
        (when utc-value
          ;; Update value attribute to UTC
          (.setAttribute el "value" utc-value)
          ;; Update value property for JS compatibility
          (set! (.-value el) utc-value)
          ;; Set form value for form participation
          (when-let [internals (get-internals el)]
            (.setFormValue internals utc-value)))))

    initial-state))

(defn get-component-state [^js el]
  (or (.-tyDatePickerState el)
      (init-component-state! el)))

(defn update-component-state!
  "Update internal date picker state and sync UTC value.
   Maintains local components for editing while always outputting UTC.
   
   When force-sync is true, immediately syncs to attributes (used for user actions).
   Otherwise follows staging behavior for with-time mode."
  [^js el updates & {:keys [force-sync]
                     :or {force-sync false}}]
  (let [current-state (get-component-state el)
        new-state (merge current-state updates)
        with-time? (:with-time new-state)]
    (set! (.-tyDatePickerState el) new-state)

    ;; STAGING: Only sync attributes if dialog is closed OR force-sync is true
    ;; This prevents re-renders during time input editing
    (when (or force-sync
              (not (:open? new-state))
              (not with-time?))
      ;; Auto-sync when components change
      (when (some #(contains? updates %) [:year :month :day :hour :minute])
        (let [components (select-keys new-state [:year :month :day :hour :minute])
              ;; Always output UTC
              utc-value (components->output-value components with-time?)]

          ;; Sync UTC value to both attribute and property
          (if utc-value
            (do
              (.setAttribute el "value" utc-value)
              (set! (.-value el) utc-value)
              ;; Sync form value for HTMX compatibility
              (when-let [internals (get-internals el)]
                (.setFormValue internals utc-value)))
            (do
              (.removeAttribute el "value")
              (set! (.-value el) "")
              ;; Clear form value
              (when-let [internals (get-internals el)]
                (.setFormValue internals "")))))))

    new-state))

;; === DISPLAY FORMATTING ===

(defn format-display-value
  "Format components for display in input using i18n with time support"
  [components format-type locale with-time?]
  (when components
    (let [date-obj (components->date-object components)
          locale-str (or locale "en-US")]
      (when date-obj
        (if with-time?
          ;; When with-time is enabled, show date + time
          (case format-type
            "short" (time/format-date date-obj locale-str {:dateStyle "short"
                                                           :timeStyle "short"})
            "medium" (time/format-date date-obj locale-str {:dateStyle "medium"
                                                            :timeStyle "short"})
            "long" (time/format-date date-obj locale-str {:dateStyle "long"
                                                          :timeStyle "short"})
            "full" (time/format-date date-obj locale-str {:dateStyle "full"
                                                          :timeStyle "short"})
            (time/format-date date-obj locale-str {:dateStyle "long"
                                                   :timeStyle "short"}))
          ;; When with-time is false, show date only
          (case format-type
            "short" (time/format-date-short date-obj locale-str)
            "medium" (time/format-date-medium date-obj locale-str)
            "long" (time/format-date-long date-obj locale-str)
            "full" (time/format-date-full date-obj locale-str)
            (time/format-date-long date-obj locale-str)))))))

;; ==== TIME INPUT STATE MANAGEMENT ====

(defn init-time-input-state!
  "Initialize time input with internal state management"
  [^js time-input hour minute]
  (let [h (or hour 0)
        m (or minute 0)
        display (gstr/format "%02d:%02d" h m)
        raw-digits (gstr/format "%02d%02d" h m)
        state {:hour h
               :minute m
               :caret-position 0
               :display-value display
               :raw-digits raw-digits
               :editing-segment :hour}]
    (set! (.-tyTimeState time-input) (->clj state))
    (set! (.-value time-input) display)
    state))

(defn get-time-input-state
  "Get current time input state"
  [^js time-input]
  (when-let [state (.-tyTimeState time-input)]
    (->clj state)))

(defn update-time-input-state!
  "Update time input state and refresh display"
  [^js time-input updates]
  (let [current-state (get-time-input-state time-input)
        new-state (merge current-state updates)
        display (:display-value new-state)
        caret-pos (:caret-position new-state)]

    (set! (.-tyTimeState time-input) (->clj new-state))
    (set! (.-value time-input) display)
    ;; Set cursor position, mapping internal positions to DOM positions
    (let [actual-pos (case caret-pos
                       0 0 ; Position 0 -> DOM position 0
                       1 1 ; Position 1 -> DOM position 1  
                       2 3 ; Position 2 -> DOM position 3 (skip delimiter)
                       3 3 ; Position 3 -> DOM position 3
                       4 4 ; Position 4 -> DOM position 4
                       5 5 ; Position 5 -> DOM position 5 (after last char)
                       caret-pos)] ; Fallback
      (.setSelectionRange time-input actual-pos actual-pos))
    new-state))

(defn parse-time-components
  "Parse hour and minute from raw digits, ensuring valid ranges"
  [raw-digits]
  (when (and raw-digits (= (count raw-digits) 4))
    (let [hour-str (subs raw-digits 0 2)
          minute-str (subs raw-digits 2 4)
          hour (js/parseInt hour-str 10)
          minute (js/parseInt minute-str 10)]
      (when (and (<= 0 hour 23) (<= 0 minute 59))
        {:hour hour
         :minute minute}))))

(defn validate-time-digit
  "Check if digit is valid for given position (0-4, position 2 is delimiter)"
  [digit position current-digits]
  (case position
    0 (<= digit 2) ; First hour digit: 0-2
    1 (let [first-hour (js/parseInt (str (nth current-digits 0)) 10)]
        (if (= first-hour 2)
          (<= digit 3) ; If hour starts with 2, max 23
          true)) ; Otherwise 00-19 are all valid
    3 (<= digit 5) ; First minute digit: 0-5  
    4 true ; Second minute digit: 0-9
    false)) ; Position 2 is delimiter

(defn format-time-display
  "Format hour and minute into HH:mm display"
  [hour minute]
  (gstr/format "%02d:%02d" hour minute))

;; === ENHANCED TIME INPUT NAVIGATION ===

(defn find-next-editable-position
  "Find next editable position, skipping delimiter at position 2"
  [current-pos]
  (case current-pos
    0 1 ; 0 -> 1 (within hour)
    1 3 ; 1 -> 3 (skip delimiter, go to minute)
    3 4 ; 3 -> 4 (within minute)
    4 5 ; 4 -> 5 (after last digit, for cursor positioning)
    5 5)) ; 5 -> 5 (stay at end) ; 4 -> 4 (stay at end)

(defn find-prev-editable-position
  "Find previous editable position, skipping delimiter at position 2"
  [current-pos]
  (case current-pos
    5 4 ; 5 -> 4 (from after last digit to last digit)
    4 3 ; 4 -> 3 (within minute)
    3 1 ; 3 -> 1 (skip delimiter, go to hour)
    1 0 ; 1 -> 0 (within hour) 
    0 0)) ; 0 -> 0 (stay at start) ; 0 -> 0 (stay at start)

(defn position->segment
  "Get segment (:hour or :minute) for given position"
  [pos]
  (if (<= pos 1) :hour :minute))

(defn get-editable-positions
  "Get all valid editable positions [0 1 3 4]"
  []
  [0 1 3 4])

;; Removed find-nearest-editable-position - no longer needed 
;; since we always position cursor at position 0 on focus/click ; Position 5+ -> position 5 (after last digit) ; Positions 3,4 are editable (minute)

(defn position->raw-digit-index
  "Convert internal position (0,1,3,4) to raw digits index (0,1,2,3)"
  [internal-pos]
  (case internal-pos
    0 0 ; Position 0 → raw digit index 0 (first hour)
    1 1 ; Position 1 → raw digit index 1 (second hour)
    3 2 ; Position 3 → raw digit index 2 (first minute)
    4 3)) ; Position 4 → raw digit index 3 (second minute)

(defn replace-digit-at-position
  "Replace digit at specific position in raw digits, return new state"
  [time-state position new-digit]
  (let [{:keys [raw-digits]} time-state
        raw-index (position->raw-digit-index position) ; Convert to raw digit index
        digits-vec (vec raw-digits)
        new-digits (apply str (assoc digits-vec raw-index (str new-digit)))
        {:keys [hour minute]} (parse-time-components new-digits)]
    (when (and hour minute) ; Only if valid time
      (assoc time-state
        :raw-digits new-digits
        :hour hour
        :minute minute
        :display-value (format-time-display hour minute)))))

(defn zero-digit-at-position
  "Set digit to 0 at specific position, return new state"
  [time-state position]
  (replace-digit-at-position time-state position 0))

;; === ENHANCED KEY HANDLERS ===

(defn handle-time-arrow-right!
  "Handle arrow right with smart positioning"
  [^js time-input ^js event]
  (.preventDefault event)
  (let [time-state (get-time-input-state time-input)
        current-pos (:caret-position time-state) ; Use internal state for consistency
        next-pos (find-next-editable-position current-pos)]
    (update-time-input-state! time-input {:caret-position next-pos})))

(defn handle-time-arrow-left!
  "Handle arrow left with smart positioning"
  [^js time-input ^js event]
  (.preventDefault event)
  (let [time-state (get-time-input-state time-input)
        current-pos (:caret-position time-state) ; Use internal state for consistency
        prev-pos (find-prev-editable-position current-pos)]
    (update-time-input-state! time-input {:caret-position prev-pos})))

(declare handle-time-change!)

(defn handle-time-digit-input!
  "Handle digit input with character replacement at cursor position"
  [^js time-input ^js event digit ^js date-picker-el]
  (.preventDefault event)
  (let [time-state (get-time-input-state time-input)
        current-pos (:caret-position time-state) ; Use internal state, not DOM
        digit-num (js/parseInt digit 10)]

    ;; Only process if at editable position and digit is valid for that position
    (when (and (contains? #{0 1 3 4} current-pos)
               (validate-time-digit digit-num current-pos (:raw-digits time-state)))

      ;; Replace digit at current position
      (when-let [new-state (replace-digit-at-position time-state current-pos digit-num)]
        ;; Update state with new time
        (let [next-pos (find-next-editable-position current-pos)
              updated-state (assoc new-state :caret-position next-pos)]
          (update-time-input-state! time-input updated-state)
          (handle-time-change! date-picker-el time-input))))))

(defn handle-time-backspace!
  "Handle backspace with digit zeroing and smart cursor movement"
  [^js time-input ^js event ^js date-picker-el]
  (.preventDefault event)
  (let [time-state (get-time-input-state time-input)
        current-pos (:caret-position time-state)] ; Use internal state, not DOM

    (cond
      ;; If at position 0, can't go back further
      (= current-pos 0)
      nil

      ;; For all other positions, find the previous digit position to zero
      :else
      (let [target-pos (case current-pos
                         1 0 ; From pos 1, go back to pos 0
                         3 1 ; From pos 3 (first minute), go back to pos 1 (second hour)
                         4 3 ; From pos 4 (second minute), go back to pos 3 (first minute)
                         5 4 ; From pos 5 (after last digit), go back to pos 4 (second minute)
                         0)] ; Fallback
        (when-let [new-state (zero-digit-at-position time-state target-pos)]
          (let [updated-state (assoc new-state :caret-position target-pos)]
            (update-time-input-state! time-input updated-state)
            (handle-time-change! date-picker-el time-input)))))))

(defn handle-time-delete!
  "Handle delete with digit zeroing at current position"
  [^js time-input ^js event ^js date-picker-el]
  (.preventDefault event)
  (let [time-state (get-time-input-state time-input)
        current-pos (:caret-position time-state)] ; Use internal state, not DOM

    (when (contains? #{0 1 3 4} current-pos) ; Only at editable positions
      (when-let [new-state (zero-digit-at-position time-state current-pos)]
        (let [updated-state (assoc new-state :caret-position current-pos)]
          (update-time-input-state! time-input updated-state)
          (handle-time-change! date-picker-el time-input))))))

(defn handle-time-click!
  "Handle click - always position cursor at first digit for predictable UX"
  [^js time-input ^js event]
  ;; Always position cursor at the first digit (position 0) on click/focus
  ;; This prevents twitchy behavior and provides consistent UX
  (update-time-input-state! time-input {:caret-position 0}))

;; === EVENT HANDLING ===

(defn emit-change-event!
  "Emit change event with UTC value and local formatted display.
   Event detail includes:
   - value: UTC ISO string for server communication
   - formatted: Local time string for display
   - milliseconds: Epoch timestamp
   - source: Event source (selection, time-change, clear, external)"
  [^js el new-components source]
  (let [state (get-component-state el)
        with-time? (:with-time state)
        ;; Always output UTC for value
        utc-value (when new-components
                    (components->output-value new-components with-time?))
        milliseconds (when new-components
                       (let [date-obj (components->date-object new-components)]
                         (when date-obj (.getTime date-obj))))
        ;; Format for local display
        formatted (when new-components
                    (format-display-value new-components
                                          (or (value/get-attribute el "format") "long")
                                          (or (value/get-attribute el "locale") "en-US")
                                          with-time?))
        event-detail #js {:value utc-value ; UTC for server
                          :milliseconds milliseconds
                          :source source
                          :formatted formatted} ; Local for display
        event (js/CustomEvent. "change"
                               #js {:detail event-detail
                                    :bubbles true
                                    :cancelable true})]

    ;; Update .value property with UTC value
    (if utc-value
      (set! (.-value el) utc-value)
      (set! (.-value el) ""))

    (.dispatchEvent el event)))

(defn emit-open-event! [^js el]
  (let [event (js/CustomEvent. "open" #js {:bubbles true})]
    (.dispatchEvent el event)))

;; === DROPDOWN MANAGEMENT ===

(defn is-mobile? []
  (or (< (.-innerWidth js/window) 768)
      (.-maxTouchPoints js/navigator)))

(defn close-dropdown!
  [^js el]
  (let [state (get-component-state el)
        picker-id (str "date-picker-" (.-id el) "-" (hash el))]
    (when (:open? state)
      ;; IMPORTANT: Force sync any staged updates when closing
      (update-component-state! el {:open? false} :force-sync true)

      ;; Unlock scroll using new unified system
      (scroll-lock/unlock-scroll! picker-id)
      (let [root (wcs/ensure-shadow el)
            dialog (.querySelector root ".calendar-dialog")]
        (when dialog
          (.remove (.-classList dialog) "position-above" "position-below" "open")
          (.close dialog)))

      (render! el))))

(defn calculate-calendar-position!
  "Calculate calendar position using CSS variables like dropdown"
  [^js el ^js shadow-root]
  (let [stub (.querySelector shadow-root ".date-picker-stub")
        dialog (.querySelector shadow-root ".calendar-dialog")]
    (when (and stub dialog)
      (let [stub-rect (.getBoundingClientRect stub)
            viewport-height (.-innerHeight js/window)
            viewport-width (.-innerWidth js/window)

            ty-calendar (.querySelector shadow-root "ty-calendar")
            ty-calendar-shadow (wcs/ensure-shadow ty-calendar)
            ty-calendar-el (.querySelector ty-calendar-shadow ".calendar-container")

            ;; Calendar-specific height estimation (larger than dropdown)
            estimated-height 400
            padding 0 ; Spacing from viewport edges

            ;; Available space calculations
            space-below (- viewport-height (.-bottom stub-rect))
            space-above (.-top stub-rect)
            space-right (- viewport-width (.-left stub-rect))

            ;; Smart direction logic (calendar needs more space)
            position-below? (>= space-below (+ estimated-height padding))
            fits-horizontally? (>= space-right (.-width stub-rect))

            wrap-padding 8

            ;; Calculate position coordinates
            x (if fits-horizontally?
                (- (.-left stub-rect) wrap-padding) ; Normal left alignment
                (max padding (- viewport-width (.-width stub-rect) padding))) ; Right edge fallback

            y (cond
                ;; Prefer below if space available
                position-below?
                (.-bottom stub-rect)

                ;; Position above if enough space
                (>= space-above (+ estimated-height padding))
                (- viewport-height (.-top stub-rect))

                ;; Constrained space - fit within viewport
                :else
                (max padding (min (.-bottom stub-rect)
                                  (- viewport-height estimated-height padding))))]

        ;; Set CSS variables for positioning
        (.setProperty (.-style el) "--calendar-x" (str x "px"))
        (.setProperty (.-style el) "--calendar-y" (str y "px"))
        (.setProperty (.-style el) "--calendar-offset-x" "0px")
        (.setProperty (.-style el) "--calendar-offset-y" "0px")

        ;; Set direction classes for CSS styling
        (if position-below?
          (do
            (.add (.-classList dialog) "position-below")
            (.remove (.-classList dialog) "position-above"))
          (do
            (.add (.-classList dialog) "position-above")
            (.remove (.-classList dialog) "position-below")))))))

(defn open-dropdown!
  [^js el]
  (let [state (get-component-state el)
        picker-id (str "date-picker-" (.-id el) "-" (hash el))]
    (when-not (:open? state)
      (update-component-state! el {:open? true})
      (emit-open-event! el)
      (render! el)
      (.requestAnimationFrame js/window
                              (fn []
                                (let [root (wcs/ensure-shadow el)
                                      dialog (.querySelector root ".calendar-dialog")]
                                  (when dialog
                                    ;; Lock scroll using new unified system
                                    (scroll-lock/lock-scroll! picker-id)
                                    (.showModal dialog)
                                    (calculate-calendar-position! el root)
                                    (.add (.-classList dialog) "open"))))))))

;; === ATTRIBUTES PARSING ===

(defn get-input-attributes [^js el]
  (let [size (or (value/get-attribute el "size") "md")
        flavor (or (value/get-attribute el "flavor") nil)
        label (value/get-attribute el "label")
        placeholder (value/get-attribute el "placeholder")
        required (= (value/get-attribute el "required") "true")
        disabled (= (value/get-attribute el "disabled") "true")
        clearable (= (value/get-attribute el "clearable") "true")
        with-time (= (value/get-attribute el "with-time") "true")
        format-type (or (value/get-attribute el "format") "long") ; Changed default to "long"
        locale (or (value/get-attribute el "locale") "en-US")
        state (get-component-state el)
        components (select-keys state [:year :month :day :hour :minute])
        formatted-value (when (and (:year state) (:month state) (:day state))
                          (format-display-value components format-type locale with-time))]

    {:size size
     :flavor flavor
     :label label
     :placeholder placeholder
     :required required
     :disabled disabled
     :clearable clearable
     :with-time with-time
     :format-type format-type
     :locale locale
     :components components
     :formatted-value formatted-value
     :open? (:open? state)}))

;; === EVENT HANDLERS ===

(defn handle-stub-click! [^js el ^js event]
  (.preventDefault event)
  (when-not (= (value/get-attribute el "disabled") "true")
    (open-dropdown! el)))

(defn handle-clear-click! [^js el ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  ;; Clear all components
  (update-component-state! el {:year nil
                               :month nil
                               :day nil
                               :hour nil
                               :minute nil})
  ;; Remove value attribute
  (.removeAttribute el "value")
  ;; Clear value property
  (set! (.-value el) "")
  ;; Clear form value
  (when-let [internals (get-internals el)]
    (.setFormValue internals ""))
  ;; Emit change event with nil value
  (emit-change-event! el nil "clear")
  (render! el))

(defn handle-time-change!
  "Handle time changes from internal time input state"
  [^js el ^js time-input]
  (let [time-state (get-time-input-state time-input)
        date-state (get-component-state el)
        components (select-keys date-state [:year :month :day])
        {:keys [hour minute]} time-state]

    (when (and (:year date-state) (:month date-state) (:day date-state)
               (some? hour) (some? minute))
      (let [updated-components (assoc components :hour hour :minute minute)]
        ;; Force sync when time changes - user is actively editing time
        ;; and expects to see the value update immediately
        (update-component-state! el updated-components :force-sync true)
        (emit-change-event! el updated-components "time-change")))))

(defn handle-calendar-change! [^js el ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (let [detail (.-detail event)
        day-context (.-dayContext detail)
        clj-context (->clj day-context)
        state (get-component-state el)
        components (select-keys state [:year :month :day :hour :minute])
        new-components (assoc components
                         :year (:year clj-context)
                         :month (:month clj-context)
                         :day (:day-in-month clj-context))]

    ;; Force sync when calendar date changes - this is a deliberate user action
    ;; that should immediately update the value, even in with-time mode
    (update-component-state! el new-components :force-sync true)
    (emit-change-event! el new-components "selection")

    ;; Only close calendar if time is not required
    ;; When with-time is enabled, keep calendar open for time input
    (let [with-time? (:with-time state)]
      (if with-time?
        ;; Auto-focus the time input after date selection
        (.requestAnimationFrame js/window
                                (fn []
                                  (let [root (wcs/ensure-shadow el)
                                        time-input (.querySelector root ".time-input")]
                                    (when time-input
                                      (.focus time-input)))))
        ;; Close immediately for date-only pickers
        (close-dropdown! el)))))

(defn handle-dialog-click! [^js el ^js event]
  "Handle clicks on the dialog element (backdrop clicks)"
  (let [root (wcs/ensure-shadow el)
        dialog (.querySelector root ".calendar-dialog")
        content (.querySelector root ".calendar-content")
        state (get-component-state el)]
    ;; Close if clicking on dialog backdrop (not calendar content)
    ;; Works for both with-time and without-time modes
    (when (and (= (.-target event) dialog)
               (:open? state)
               content
               (not (.contains content (.-target event))))
      (.preventDefault event)
      (.stopPropagation event)
      (close-dropdown! el))))

(defn handle-outside-click! [^js el ^js event]
  "Handle clicks outside the date picker"
  (let [target (.-target event)
        state (get-component-state el)
        root (wcs/ensure-shadow el)
        dialog (.querySelector root ".calendar-dialog")]
    ;; Check if click is truly outside everything
    (when (and (:open? state)
               dialog
               (not (.contains el target))
               (not (.contains dialog target)))
      ;; Force sync any staged updates when closing via outside click
      (close-dropdown! el))))

(defn handle-escape-key! [^js el ^js event]
  (when (= (.-key event) "Escape")
    (.preventDefault event)
    (close-dropdown! el)))

;; === TIME INPUT ===

(defn create-time-input [^js el attrs]
  "Create enhanced time input with internal state management and toddler-style navigation"
  (when (:with-time attrs)
    (let [time-input (.createElement js/document "input")
          components (:components attrs)
          hour (:hour components)
          minute (:minute components)]

      ;; Basic input setup
      (set! (.-type time-input) "text")
      (set! (.-className time-input) "time-input")
      (set! (.-placeholder time-input) "HH:mm")
      (set! (.-autocomplete time-input) "off")
      (set! (.-maxLength time-input) 5) ; "HH:mm"

      ;; Initialize internal state
      (init-time-input-state! time-input hour minute)

      ;; Enhanced event handlers (basic for now, will enhance in next phases)
      (.addEventListener time-input "keydown"
                         (fn [^js event]
                           (let [key (.-key event)]
                             (case key
                               "ArrowRight" (handle-time-arrow-right! time-input event)
                               "ArrowLeft" (handle-time-arrow-left! time-input event)
                               "Backspace" (handle-time-backspace! time-input event el)
                               "Delete" (handle-time-delete! time-input event el)
                               "Home" (update-time-input-state! time-input {:caret-position 0})
                               "End" (update-time-input-state! time-input {:caret-position 5})
                               "Tab" nil ; Allow default tab behavior
                               ;; Handle digit input
                               (when (re-matches #"\d" key)
                                 (handle-time-digit-input! time-input event key el))))))

      (.addEventListener time-input "input"
                         (fn [^js event]
                           ;; Input event is now handled by keydown for precise control
                           ;; This prevents default browser input handling
                           (.preventDefault event)))

      (.addEventListener time-input "click"
                         (fn [^js event]
                           ;; Always position cursor at first digit on click
                           (handle-time-click! time-input event)))

      (.addEventListener time-input "focus"
                         (fn [^js event]
                           ;; Always position cursor at first digit on focus (tab, programmatic focus, etc.)
                           (handle-time-click! time-input event))) ; Move to next digit

      time-input)))

;; === RENDERING ===

(defn build-stub-classes [{:keys [size flavor disabled required open?]}]
  (let [base-classes ["date-picker-stub"]]
    (cond-> base-classes
      size (conj size)
      flavor (conj flavor)
      disabled (conj "disabled")
      required (conj "required")
      open? (conj "open"))))

(defn render-date-picker-stub [^js el attrs]
  (let [{:keys [placeholder formatted-value disabled clearable open?]} attrs
        stub (.createElement js/document "div")
        display-text (.createElement js/document "span")
        icon-container (.createElement js/document "div")
        calendar-icon (.createElement js/document "span")
        clear-button (.createElement js/document "button")]

    (set! (.-className stub)
          (->> (build-stub-classes attrs)
               (clojure.string/join " ")))
    (when disabled
      (.setAttribute stub "disabled" "true"))

    (set! (.-className display-text) "stub-text")
    (set! (.-textContent display-text)
          (or formatted-value placeholder (translate "Select date...")))
    (when-not formatted-value
      (.add (.-classList display-text) "placeholder"))

    (set! (.-className icon-container) "stub-icons")

    (when (and clearable formatted-value (not disabled))
      (set! (.-className clear-button) "stub-clear")
      (set! (.-innerHTML clear-button) clear-icon-svg)
      (set! (.-type clear-button) "button")
      (.addEventListener clear-button "click" #(handle-clear-click! el %))
      (.appendChild icon-container clear-button))

    (set! (.-className calendar-icon) "stub-arrow")
    (set! (.-innerHTML calendar-icon) calendar-icon-svg)
    (.appendChild icon-container calendar-icon)

    (.addEventListener stub "click" #(handle-stub-click! el %))

    (.appendChild stub display-text)
    (.appendChild stub icon-container)
    stub))

(defn render-container-structure [^js el attrs]
  (let [{:keys [label required]} attrs
        container (.createElement js/document "div")
        label-el (.createElement js/document "label")]

    (set! (.-className container) "dropdown-container")
    (set! (.-className label-el) "dropdown-label")

    (when label
      (set! (.-innerHTML label-el)
            (str label (when required required-icon)))
      (.appendChild container label-el))

    container))

(defn render-calendar-dropdown [^js el attrs]
  (let [{:keys [components locale with-time]} attrs
        dialog (.createElement js/document "dialog")
        content-wrapper (.createElement js/document "div")
        calendar (.createElement js/document "ty-calendar")]

    (set! (.-className dialog) "calendar-dialog")
    (set! (.-className content-wrapper) "calendar-content")

    ;; Set up ty-calendar with current date components
    (when (and (:year components) (:month components) (:day components))
      (.setAttribute calendar "year" (str (:year components)))
      (.setAttribute calendar "month" (str (:month components)))
      (.setAttribute calendar "day" (str (:day components))))

    (when locale
      (.setAttribute calendar "locale" locale))

    (.addEventListener calendar "change" #(handle-calendar-change! el %))
    (.appendChild content-wrapper calendar)

    ;; Add time section if with-time enabled
    (when with-time
      (let [time-section (.createElement js/document "div")
            time-label (.createElement js/document "label")
            time-input (create-time-input el attrs)
            time-icon (.createElement js/document "span")]

        (set! (.-className time-section) "time-section")
        (set! (.-className time-label) "time-label")
        (set! (.-textContent time-label) (translate "Time:"))

        (set! (.-className time-icon) "time-icon")
        (set! (.-innerHTML time-icon) schedule-icon-svg)

        (.appendChild time-section time-label)
        (when time-input
          (.appendChild time-section time-input))
        (.appendChild time-section time-icon)
        (.appendChild content-wrapper time-section)))

    ;; Wrap content in calendar-content container
    (.appendChild dialog content-wrapper)

    ;; Add dialog close event handler
    (.addEventListener dialog "close"
                       (fn [^js event]
                         (update-component-state! el {:open? false})))

    dialog))

(defn update-display!
  "Update just the display text without destroying DOM - used when dialog is open"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        attrs (get-input-attributes el)
        stub-text (.querySelector root ".stub-text")
        clear-button (.querySelector root ".stub-clear")]

    ;; Update stub text display
    (when stub-text
      (let [new-text (or (:formatted-value attrs)
                         (:placeholder attrs)
                         (translate "Select date..."))]
        (set! (.-textContent stub-text) new-text)
        (if (:formatted-value attrs)
          (.remove (.-classList stub-text) "placeholder")
          (.add (.-classList stub-text) "placeholder"))))

    ;; Update clear button visibility
    (when clear-button
      (if (and (:clearable attrs)
               (:formatted-value attrs)
               (not (:disabled attrs)))
        (.setProperty (.-style clear-button) "display" "")
        (.setProperty (.-style clear-button) "display" "none")))

    ;; Update calendar attributes if it exists
    (when-let [calendar (.querySelector root "ty-calendar")]
      (let [{:keys [year month day]} (:components attrs)]
        (when year
          (.setAttribute calendar "year" (str year)))
        (when month
          (.setAttribute calendar "month" (str month)))
        (when day
          (.setAttribute calendar "day" (str day)))))

    ;; DON'T re-initialize time input - it maintains its own state!
    ;; The time input handles its own internal state management
    ))

(defn render! [^js el]
  (let [root (wcs/ensure-shadow el)
        attrs (get-input-attributes el)
        state (get-component-state el)
        existing-dialog (.querySelector root ".calendar-dialog")]

    ;; SMART RENDERING: Check if dialog is currently open
    (if (and (:open? state)
             existing-dialog
             (.-open existing-dialog))
      ;; === PARTIAL UPDATE PATH ===
      ;; Dialog is open - just update display without destroying DOM
      (do
        (update-display! el)
        ;; Ensure dialog position is still correct
        (calculate-calendar-position! el root)
        el)

      ;; === FULL REBUILD PATH ===
      ;; Dialog is closed or doesn't exist - safe to rebuild
      (do
        (ensure-styles! root date-picker-styles "ty-date-picker")
        (set! (.-innerHTML root) "") ; Only destroy when dialog is closed

        (let [container (render-container-structure el attrs)
              wrapper (.createElement js/document "div")]

          (set! (.-className wrapper) "dropdown-wrapper")
          (.appendChild wrapper (render-date-picker-stub el attrs))
          (.appendChild wrapper (render-calendar-dropdown el attrs))
          (.appendChild container wrapper)
          (.appendChild root container))

        ;; Store function references for proper cleanup
        (let [click-fn #(handle-outside-click! el %)
              keydown-fn #(handle-escape-key! el %)
              dialog (.querySelector root ".calendar-dialog")
              dialog-click-fn #(handle-dialog-click! el %)]

          ;; Store references on element for cleanup
          (set! (.-tyClickListener el) click-fn)
          (set! (.-tyKeydownListener el) keydown-fn)
          (set! (.-tyDialogClickListener el) dialog-click-fn)

          ;; Add document listeners (for when calendar is closed)
          (.addEventListener js/document "click" click-fn)
          (.addEventListener js/document "keydown" keydown-fn)

          ;; Add dialog click listener (for backdrop clicks when calendar is open)
          (when dialog
            (.addEventListener dialog "click" dialog-click-fn)))

        el))))

(defn cleanup! [^js el]
  ;; Remove listeners using stored references
  (when (.-tyClickListener el)
    (.removeEventListener js/document "click" (.-tyClickListener el)))
  (when (.-tyKeydownListener el)
    (.removeEventListener js/document "keydown" (.-tyKeydownListener el)))

  ;; Remove dialog click listener
  (when (.-tyDialogClickListener el)
    (let [root (wcs/ensure-shadow el)
          dialog (.querySelector root ".calendar-dialog")]
      (when dialog
        (.removeEventListener dialog "click" (.-tyDialogClickListener el)))))

  ;; Clear all stored references
  (set! (.-tyDatePickerState el) nil)
  (set! (.-tyClickListener el) nil)
  (set! (.-tyKeydownListener el) nil)
  (set! (.-tyDialogClickListener el) nil))

(let [state-keys [:year :month :day :hour :minute]
      pred (apply juxt state-keys)]
  (defn state-changed?
    [old-data new-data]
    (not=
      (pred old-data)
      (pred new-data))))

(defn enrich-delta
  "Process attribute changes and parse date values for comparison.
   When value attribute changes:
   1. Parse the new value to components
   2. Compare with current components
   3. Return components if changed, otherwise skip update
   
   This ensures we only re-render when actual date/time values change."
  [^js el delta]
  (if-not (contains? delta "value")
    delta ; No value change, pass through
    (let [current-state (get-component-state el)
          current-components (select-keys current-state [:year :month :day :hour :minute])
          with-time? (:with-time current-state)
          new-components (parse-value (get delta "value") with-time?)]

      (cond
        ;; Value was cleared
        (every? nil? (vals new-components))
        (assoc delta :year nil :month nil :day nil :hour nil :minute nil)

        ;; Components changed
        (state-changed? current-components new-components)
        (merge (dissoc delta "value") new-components)

        ;; No actual change in components
        :else
        (dissoc delta "value")))))

(def configuration
  {:observed [:value :size :flavor :label :placeholder :required :disabled :name
              :clearable :format :locale :with-time]
   :props {:value nil} ; Property watching for framework integration
   :form-associated true ; Enable form participation for HTMX compatibility
   :connected (fn [^js el]
                (init-component-state! el)
                (render! el))
   :disconnected (fn [^js el]
                   (cleanup! el))
   :attr (fn [^js el delta]
           (let [delta' (enrich-delta el delta)]
             (when (or
                     (seq (dissoc delta "value")) ; Non-value changes always render
                     (not= delta delta') ; Value parsing changed something
                     (some #(contains? delta' %) [:year :month :day :hour :minute])) ; Components changed
               ;; Sync date picker with new state
               (update-component-state! el delta')
               (render! el))))
   :prop (fn [^js el delta]
           (let [delta' (enrich-delta el delta)]
             (when (or
                     (seq (dissoc delta "value")) ; Non-value changes always render
                     (not= delta delta') ; Value parsing changed something
                     (some #(contains? delta' %) [:year :month :day :hour :minute])) ; Components changed
               ;; Sync date picker with new state
               (update-component-state! el delta')
               (render! el))))})
