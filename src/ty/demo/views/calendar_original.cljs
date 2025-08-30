(ns ty.demo.views.calendar
  "Calendar component demos with DOM-only render functions"
  (:require
    [cljs-bean.core :refer [->clj ->js]]
    [ty.date.core :as date]
    [ty.demo.state :as state]))

;; Demo render functions for testing - ALL DOM-ONLY  
;; Set up demo render functions ONCE when namespace loads
(defonce demo-functions-setup
  (do
    (js/console.log "Setting up DOM-only demo render functions...")

    ;; Holiday day content - DOM elements only
    (set! (.-holidayDayContent js/window)
          (fn [^js context]
            (let [{:keys [day-in-month holiday?]} (->clj context)]
              (if holiday?
                ;; Create DOM elements for holiday display
                (let [container (.createElement js/document "div")
                      day-span (.createElement js/document "span")
                      star-span (.createElement js/document "span")]
                  (set! (.-className container) "day-content")
                  (set! (.-textContent day-span) (str day-in-month))
                  (set! (.-className star-span) "holiday-star")
                  (set! (.-textContent star-span) "★")
                  (.appendChild container day-span)
                  (.appendChild container star-span)
                  container)
                ;; Simple day number for non-holidays
                (let [span (.createElement js/document "span")]
                  (set! (.-textContent span) (str day-in-month))
                  span)))))

    ;; Event dots day content - DOM elements only
    (set! (.-eventDayContent js/window)
          (fn [^js context]
            (let [{:keys [day-in-month]} (->clj context)
                  ;; Mock some events for demo (15th, 18th, 22nd, 25th have events)
                  has-events (#{15 18 22 25} day-in-month)]
              (if has-events
                ;; Create DOM elements for event display
                (let [container (.createElement js/document "div")
                      day-span (.createElement js/document "span")
                      dots-container (.createElement js/document "div")
                      dot (.createElement js/document "span")]
                  (set! (.-className container) "day-content")
                  (set! (.-textContent day-span) (str day-in-month))
                  (set! (.-className dots-container) "event-dots")
                  (set! (.-className dot) "event-dot")
                  (.appendChild dots-container dot)
                  (.appendChild container day-span)
                  (.appendChild container dots-container)
                  container)
                ;; Simple day number for no events
                (let [span (.createElement js/document "span")]
                  (set! (.-textContent span) (str day-in-month))
                  span)))))

    ;; Hotel pricing day content - DOM elements only
    (set! (.-hotelDayContent js/window)
          (fn [^js context]
            (let [{:keys [day-in-month other-month weekend]} (->clj context)
                  base-price 120
                  weekend-markup (if weekend 50 0)
                  price (+ base-price weekend-markup)]
              (if other-month
                ;; Simple day number for other months
                (let [span (.createElement js/document "span")]
                  (set! (.-textContent span) (str day-in-month))
                  span)
                ;; Create DOM elements for hotel pricing
                (let [container (.createElement js/document "div")
                      day-span (.createElement js/document "span")
                      price-span (.createElement js/document "span")]
                  (set! (.-className container) "hotel-day")
                  (set! (.-className day-span) "day-num")
                  (set! (.-textContent day-span) (str day-in-month))
                  (set! (.-className price-span) "price")
                  (set! (.-textContent price-span) (str "$" price))
                  (.appendChild container day-span)
                  (.appendChild container price-span)
                  container)))))

    ;; Project task count day content - DOM elements only
    (set! (.-projectDayContent js/window)
          (fn [^js context]
            (let [{:keys [day-in-month]} (->clj context)
                  ;; Mock task counts for demo
                  task-count (case day-in-month
                               5 3, 12 1, 18 5, 22 2, 28 4
                               0)]
              (if (> task-count 0)
                ;; Create DOM elements for task display
                (let [container (.createElement js/document "div")
                      day-span (.createElement js/document "span")
                      task-span (.createElement js/document "span")]
                  (set! (.-className container) "project-day")
                  (set! (.-className day-span) "day-num")
                  (set! (.-textContent day-span) (str day-in-month))
                  (set! (.-className task-span) "task-count")
                  (set! (.-textContent task-span) (str task-count " tasks"))
                  (.appendChild container day-span)
                  (.appendChild container task-span)
                  container)
                ;; Simple day number for no tasks
                (let [span (.createElement js/document "span")]
                  (set! (.-textContent span) (str day-in-month))
                  span)))))

    ;; Complex multi-element content
    (set! (.-complexContent js/window)
          (fn [^js context]
            (let [{:keys [day-in-month weekend]} (->clj context)]
              (let [container (.createElement js/document "div")
                    header (.createElement js/document "div")
                    day-span (.createElement js/document "span")
                    status-badge (.createElement js/document "span")
                    content-area (.createElement js/document "div")
                    event-1 (.createElement js/document "div")
                    event-2 (.createElement js/document "div")
                    footer (.createElement js/document "div")
                    availability (.createElement js/document "span")]
                ;; Setup container
                (set! (.-className container) "complex-day")
                ;; Header with day and status
                (set! (.-className header) "day-header")
                (set! (.-className day-span) "day-number")
                (set! (.-textContent day-span) (str day-in-month))
                (set! (.-className status-badge) (if weekend "status weekend" "status"))
                (set! (.-textContent status-badge) (if weekend "W" "D"))
                (.appendChild header day-span)
                (.appendChild header status-badge)
                ;; Content area with events
                (set! (.-className content-area) "events")
                (set! (.-className event-1) "event")
                (set! (.-textContent event-1) "Meet")
                (set! (.-className event-2) "event")
                (set! (.-textContent event-2) "Call")
                (.appendChild content-area event-1)
                (.appendChild content-area event-2)
                ;; Footer with availability
                (set! (.-className footer) "day-footer")
                (set! (.-className availability) "availability")
                (set! (.-textContent availability) "3 free")
                (.appendChild footer availability)
                ;; Assemble container
                (.appendChild container header)
                (.appendChild container content-area)
                (.appendChild container footer)
                container))))

    ;; Custom classes for different contexts
    (set! (.-holidayDayClasses js/window)
          (fn [^js context]
            (let [{:keys [holiday? today? weekend other-month]} (->clj context)]
              (->js (cond-> ["calendar-day"]
                      holiday? (conj "holiday")
                      today? (conj "today")
                      weekend (conj "weekend")
                      other-month (conj "other-month"))))))

    (set! (.-hotelDayClasses js/window)
          (fn [^js context]
            (let [{:keys [today? weekend other-month]} (->clj context)]
              (->js (cond-> ["calendar-day" "hotel-day"]
                      today? (conj "today")
                      weekend (conj "weekend-pricing")
                      other-month (conj "other-month"))))))

    (set! (.-projectDayClasses js/window)
          (fn [^js context]
            (let [{:keys [day-in-month today? weekend other-month]} (->clj context)
                  has-tasks (#{5 12 18 22 28} day-in-month)]
              (->js (cond-> ["calendar-day" "project-day"]
                      today? (conj "today")
                      weekend (conj "weekend")
                      other-month (conj "other-month")
                      has-tasks (conj "has-tasks"))))))

    ;; Log that setup is complete
    (js/console.log "DOM-only demo render functions registered:"
                    #js {:holidayDayContent (exists? (.-holidayDayContent js/window))
                         :eventDayContent (exists? (.-eventDayContent js/window))
                         :hotelDayContent (exists? (.-hotelDayContent js/window))
                         :projectDayContent (exists? (.-projectDayContent js/window))
                         :complexContent (exists? (.-complexContent js/window))})
    :setup-complete))

(defn handle-day-click [^js event]
  (let [detail (.-detail event)
        date-parts (.-dateParts detail)]
    (js/console.log "Day clicked:" detail)
    (swap! state/state assoc :last-clicked
           {:year (.-year date-parts)
            :month (.-month date-parts)
            :day (.-day date-parts)
            :is-today (.-isToday detail)
            :is-weekend (.-isWeekend detail)
            :is-other-month (.-isOtherMonth detail)
            :value (.-value detail)})))

(defn calendar-selection-handler [calendar-id]
  "Creates a day-click handler that updates state for a specific calendar"
  (fn [^js event]
    (let [detail (.-detail event)
          date-parts (.-dateParts detail)
          value (.-value detail)]
      (js/console.log (str "Calendar " calendar-id " selected:") detail)
      (swap! state/state assoc-in [:calendar-selections calendar-id]
             {:year (.-year date-parts)
              :month (.-month date-parts)
              :day (.-day date-parts)
              :value value
              :formatted-us (str (.-month date-parts) "/" (.-day date-parts) "/" (.-year date-parts))
              :formatted-de (str (.-day date-parts) "." (.-month date-parts) "." (.-year date-parts))
              :formatted-long (.toLocaleDateString (js/Date. value) "en-US"
                                                   #js {:weekday "long"
                                                        :year "numeric"
                                                        :month "long"
                                                        :day "numeric"})}))))

(defn stateful-calendar-demo []
  [:div.demo-section
   [:h2.demo-title "🎉 New: Stateful Calendar with Navigation"]
   [:p.text-sm.text-gray-600.mb-6 "Complete calendar component with navigation controls - wraps the stateless calendar-month core with full state management."]

   ;; Basic calendar with navigation
   [:div.mb-8
    [:h3.demo-subtitle.mb-4 "Full Calendar with Navigation Controls"]
    [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8

     ;; Basic stateful calendar
     [:div.space-y-4
      [:div
       [:h4.font-semibold.mb-2 "Default Calendar"]
       [:p.text-sm.text-gray-600.mb-4 "Navigation controls with month/year dropdowns and today button."]
       [:ty-calendar {:show-today-button true
                      :on {:day-click handle-day-click}}]]

      [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
       [:code
        "[:ty-calendar\n"
        " {:show-today-button true\n"
        "  :on {:day-click handle-day-click}}]"]]]

     ;; Calendar with custom width
     [:div.space-y-4
      [:div
       [:h4.font-semibold.mb-2 "Custom Width & Content"]
       [:p.text-sm.text-gray-600.mb-4 "With width control and custom render functions."]
       [:ty-calendar {:show-today-button true
                      :width "450px"
                      :day-content-fn "hotelDayContent"
                      :on {:day-click handle-day-click}}]]

      [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
       [:code
        "[:ty-calendar\n"
        " {:width \"450px\"\n"
        "  :day-content-fn \"hotelDayContent\"\n"
        "  :show-today-button true}]"]]]]]

   ;; Navigation features showcase
   [:div.mb-8
    [:h3.demo-subtitle.mb-4 "Navigation Features"]
    [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-6

     ;; Year navigation
     [:div.space-y-3
      [:div
       [:h4.font-medium.mb-1 "Year Navigation"]
       [:p.text-xs.text-gray-600.mb-3 "<<  >> buttons for year jumping"]
       [:ty-calendar {:show-today-button false
                      :width "320px"
                      :on {:day-click handle-day-click}}]]
      [:div.text-xs.text-gray-600
       [:p "• Click << >> for year navigation"]
       [:p "• Click < > for month navigation"]
       [:p "• Use dropdowns for direct selection"]]]

     ;; With today button
     [:div.space-y-3
      [:div
       [:h4.font-medium.mb-1 "Today Button"]
       [:p.text-xs.text-gray-600.mb-3 "Quick navigation to current month"]
       [:ty-calendar {:show-today-button true
                      :width "320px"
                      :day-content-fn "eventDayContent"
                      :on {:day-click handle-day-click}}]]
      [:div.text-xs.text-gray-600
       [:p "• Today button navigates to current month"]
       [:p "• Works with any custom content"]
       [:p "• Preserves all render functions"]]]

     ;; Localized calendar
     [:div.space-y-3
      [:div
       [:h4.font-medium.mb-1 "Localized (German)"]
       [:p.text-xs.text-gray-600.mb-3 "Localized month names in dropdown"]
       [:ty-calendar {:show-today-button true
                      :width "320px"
                      :locale "de-DE"
                      :on {:day-click handle-day-click}}]]
      [:div.text-xs.text-gray-600
       [:p "• Month names in German"]
       [:p "• Locale affects dropdown text"]
       [:p "• Year numbers remain universal"]]]]]

   ;; State management demonstration
   [:div.mb-8
    [:h3.demo-subtitle.mb-4 "State Management & Event Forwarding"]
    [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8

     ;; Event forwarding
     [:div.space-y-4
      [:div
       [:h4.font-semibold.mb-2 "Event Forwarding"]
       [:p.text-sm.text-gray-600.mb-4 "Events from embedded calendar-month are forwarded with additional context."]
       [:ty-calendar {:show-today-button true
                      :width "380px"
                      :day-content-fn "projectDayContent"
                      :on {:day-click handle-day-click}}]]]

     ;; Complex content
     [:div.space-y-4
      [:div
       [:h4.font-semibold.mb-2 "Complex Content"]
       [:p.text-sm.text-gray-600.mb-4 "Navigation works with any render functions from the stateless core."]
       [:ty-calendar {:show-today-button true
                      :width "420px"
                      :day-content-fn "complexContent"
                      :on {:day-click handle-day-click}}]]
      [:div.text-xs.text-gray-600.bg-blue-50.p-3.rounded
       [:p.font-medium.mb-1 "💡 Architecture Benefit:"]
       [:p "All render function power from calendar-month is preserved - navigation is just a wrapper!"]]]]]

   ;; API comparison
   [:div.mb-8
    [:h3.demo-subtitle.mb-4 "API Comparison: Stateless vs Stateful"]
    [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8

     ;; Stateless core
     [:div.space-y-4
      [:div
       [:h4.font-semibold.mb-2 "ty-calendar-month (Stateless Core)"]
       [:p.text-sm.text-gray-600.mb-4 "Pure renderer - you control the state externally."]
       [:ty-calendar-month {:display-year "2024"
                            :display-month "12"
                            :width "350px"
                            :day-content-fn "holidayDayContent"
                            :on {:day-click handle-day-click}}]]

      [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
       [:code
        "[:ty-calendar-month\n"
        " {:display-year \"2024\"  ; You set explicit month\n"
        "  :display-month \"12\"\n"
        "  :day-content-fn \"holidayDayContent\"}]"]]]

     ;; Stateful wrapper
     [:div.space-y-4
      [:div
       [:h4.font-semibold.mb-2 "ty-calendar (Stateful Wrapper)"]
       [:p.text-sm.text-gray-600.mb-4 "Manages navigation state internally - just provide content."]
       [:ty-calendar {:show-today-button true
                      :width "350px"
                      :day-content-fn "holidayDayContent"
                      :on {:day-click handle-day-click}}]]

      [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
       [:code
        "[:ty-calendar\n"
        " {:show-today-button true    ; Navigation managed internally\n"
        "  :day-content-fn \"holidayDayContent\"}]"]]]]]

   ;; Benefits section
   [:div.p-4.bg-green-50.rounded-lg
    [:h4.font-semibold.mb-3 "🎯 Stateful Calendar Benefits:"]
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-4.text-sm
     [:div.space-y-2
      [:div [:strong "🧩 Complete Solution:"] " Navigation + calendar in one component"]
      [:div [:strong "🎮 Interactive Navigation:"] " << < > >> buttons plus dropdowns"]
      [:div [:strong "🏠 Today Button:"] " Quick navigation to current month"]
      [:div [:strong "🌍 Localization:"] " Month names adapt to locale"]]
     [:div.space-y-2
      [:div [:strong "🔧 State Management:"] " Internal navigation state handling"]
      [:div [:strong "📡 Event Forwarding:"] " All calendar-month events preserved"]
      [:div [:strong "🎨 Full Customization:"] " All render functions still work"]
      [:div [:strong "⚡ Performance:"] " Wraps stateless core efficiently"]]]]

   ;; Architecture explanation
   [:div.mt-6.p-4.bg-blue-50.rounded-lg
    [:h4.font-semibold.mb-3 "🏗️ Architecture: Stateless Core + Stateful Wrapper"]
    [:div.text-sm.space-y-2
     [:p [:strong "Pattern:"] " ty-calendar (stateful) embeds and controls ty-calendar-month (stateless)"]
     [:div.bg-white.p-3.rounded.font-mono.text-xs.overflow-x-auto
      [:div "ty-calendar"]
      [:div "├── Navigation Header (<<  <  December 2024  >  >> Today)"]
      [:div "└── ty-calendar-month (stateless core with all render functions)"]]
     [:p [:strong "Benefits:"] " Best of both worlds - complete navigation UX with unlimited customization power"]]]

   ;; Usage examples
   [:div.mt-6.p-4.bg-purple-50.rounded-lg
    [:h4.font-semibold.mb-3 "💻 Usage Examples"]
    [:div.grid.grid-cols-1.lg:grid-cols-2.gap-4
     [:div
      [:p.font-medium.mb-2.text-sm "HTML/ClojureScript:"]
      [:pre.text-xs.bg-white.p-3.rounded.overflow-x-auto
       [:code
        "<!-- Complete calendar solution -->\n"
        "<ty-calendar\n"
        "  show-today-button=\"true\"\n"
        "  width=\"400px\"\n"
        "  day-content-fn=\"myRenderer\">\n"
        "</ty-calendar>\n\n"
        ";; ClojureScript/Replicant\n"
        "[:ty-calendar\n"
        " {:show-today-button true\n"
        "  :width \"400px\"\n"
        "  :day-content-fn \"myRenderer\"}]"]]]

     [:div
      [:p.font-medium.mb-2.text-sm "React JSX:"]
      [:pre.text-xs.bg-white.p-3.rounded.overflow-x-auto
       [:code
        "<ty-calendar\n"
        "  showTodayButton={true}\n"
        "  width=\"400px\"\n"
        "  dayContentFn=\"myRenderer\"\n"
        "  onDayClick={handleDayClick}\n"
        "/>\n\n"
        "// Perfect for dashboards\n"
        "<ty-calendar\n"
        "  width=\"100%\"\n"
        "  minWidth=\"350px\"\n"
        "  dayContentFn=\"businessCalendar\"\n"
        "/>"]]]]]])

(defn individual-selection-demo []
  [:div.demo-section
   [:h2.demo-title "🎯 Individual Calendar Selection State"]
   [:p.text-sm.text-gray-600.mb-6 "Each calendar tracks its own selected date internally. Click any date to see the selection - no shared state between calendars."]

   [:div.grid.grid-cols-1.lg:grid-cols-3.gap-6

    ;; Calendar A
    [:div.space-y-4
     [:div
      [:h4.font-semibold.mb-2 "Calendar A"]
      [:p.text-sm.text-gray-600.mb-3 "Each calendar manages its own selection"]
      [:ty-calendar {:id "demo-calendar-1"
                     :show-today-button true
                     :width "300px"
                     :on {:day-click (fn [^js e] (let [detail (.-detail e)
                                                       date-parts (.-dateParts detail)
                                                       formatted-date (str (:month date-parts) "/" (:day date-parts) "/" (:year date-parts))
                                                       log-el (.getElementById js/document "selection-log-1")]
                                                   (set! (.-textContent log-el) (str "Selected: " formatted-date))))}}]]
     [:div.text-xs.p-2.bg-blue-50.rounded.font-mono
      {:id "selection-log-1"}
      "Click a date to see selection..."]]

    ;; Calendar B
    [:div.space-y-4
     [:div
      [:h4.font-semibold.mb-2 "Calendar B"]
      [:p.text-sm.text-gray-600.mb-3 "German locale - independent selection"]
      [:ty-calendar {:id "demo-calendar-2"
                     :show-today-button true
                     :width "300px"
                     :locale "de-DE"
                     :on {:day-click #(let [detail (.-detail ^js %)
                                            date-parts (.-dateParts detail)
                                            formatted-date (str (:day date-parts) "." (:month date-parts) "." (:year date-parts))
                                            log-el (.getElementById js/document "selection-log-2")]
                                        (set! (.-textContent log-el) (str "Ausgewählt: " formatted-date)))}}]]
     [:div.text-xs.p-2.bg-green-50.rounded.font-mono
      {:id "selection-log-2"}
      "Klicken Sie auf ein Datum..."]]

    ;; Calendar C
    [:div.space-y-4
     [:div
      [:h4.font-semibold.mb-2 "Calendar C"]
      [:p.text-sm.text-gray-600.mb-3 "Custom width - independent selection"]
      [:ty-calendar {:id "demo-calendar-3"
                     :show-today-button true
                     :width "320px"
                     :on {:day-click #(let [detail (.-detail ^js %)
                                            value (.-value detail)
                                            date-obj (js/Date. value)
                                            formatted-date (.toLocaleDateString date-obj "en-US" #js {:weekday "long"
                                                                                                      :year "numeric"
                                                                                                      :month "long"
                                                                                                      :day "numeric"})
                                            log-el (.getElementById js/document "selection-log-3")]
                                        (set! (.-textContent log-el) formatted-date))}}]]
     [:div.text-xs.p-2.bg-purple-50.rounded.font-mono
      {:id "selection-log-3"}
      "No selection made yet..."]]]

   ;; How it works
   [:div.mt-6.p-4.bg-yellow-50.rounded-lg
    [:h4.font-semibold.mb-3 "🔍 How Selection Works:"]
    [:div.text-sm.space-y-2
     [:p [:strong "Internal State:"] " Each ty-calendar maintains its own :selected-date in component state"]
     [:p [:strong "Visual Feedback:"] " Custom day-classes function adds 'selected' class to matching days"]
     [:p [:strong "Event Flow:"] " day-click → update internal state → re-render with selection → forward event"]
     [:p [:strong "Independence:"] " Selecting in one calendar doesn't affect others"]]
    [:pre.text-xs.bg-white.p-3.rounded.font-mono.mt-3.overflow-auto
     [:code
      ";; Each calendar is self-contained\n"
      "[:ty-calendar {:on {:day-click my-handler}}]\n\n"
      ";; Internal state per component:\n"
      "component-state: {:display-year 2024\n"
      "                 :display-month 12\n"
      "                 :selected-date 1735084800000}"]]]

   ;; Testing
   [:div.mt-4.p-4.bg-gray-50.rounded-lg
    [:h4.font-semibold.mb-3 "🧪 Test Selection:"]
    [:div.text-sm.space-y-2
     [:p "1. Click different dates in each calendar"]
     [:p "2. Notice each calendar highlights its own selection independently"]
     [:p "3. Navigate months - selections persist within each calendar"]
     [:p "4. Each calendar's event display shows different formatting"]]]

   ;; Architecture benefits
   [:div.mt-4.p-4.bg-green-50.rounded-lg
    [:h4.font-semibold.mb-3 "🏗️ Architecture Benefits:"]
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-4.text-sm
     [:div.space-y-2
      [:div [:strong "✅ Self-Contained:"] " Each calendar manages own state"]
      [:div [:strong "✅ Clean API:"] " Just listen to day-click events"]
      [:div [:strong "✅ No Props Drilling:"] " Internal state, not global"]]
     [:div.space-y-2
      [:div [:strong "✅ Visual Feedback:"] " Automatic selection highlighting"]
      [:div [:strong "✅ Event Forwarding:"] " External code can still listen"]
      [:div [:strong "✅ Independence:"] " Multiple calendars don't interfere"]]]]

   ;; Code example
   [:div.mt-4.p-4.bg-blue-50.rounded-lg
    [:h4.font-semibold.mb-3 "💻 Usage Pattern:"]
    [:pre.text-xs.bg-white.p-3.rounded.overflow-x-auto
     [:code
      "<!-- Each calendar is independent -->\n"
      "<ty-calendar show-today-button=\"true\" width=\"300px\"></ty-calendar>\n"
      "<ty-calendar locale=\"de-DE\" width=\"350px\"></ty-calendar>\n\n"
      ";; ClojureScript\n"
      "[:ty-calendar {:on {:day-click handle-selection}}]\n"
      "[:ty-calendar {:locale \"de-DE\" :on {:day-click other-handler}}]\n\n"
      "// React JSX\n"
      "<ty-calendar onDayClick={handleSelection} />\n"
      "<ty-calendar locale=\"de-DE\" onDayClick={otherHandler} />"]]]])

(defn natural-content-sizing []
  [:div.demo-section
   [:h2.demo-title "Natural Content Sizing - No Grid Constraints"]
   [:p.text-sm.text-gray-600.mb-6 "Each calendar sizes itself optimally for its content - showing realistic proportions for different use cases"]

   ;; First row: Compact calendars
   [:div.flex.flex-wrap.gap-8.justify-start.items-start.mb-8

    ;; Minimal compact calendar
    [:div.flex.flex-col
     [:div.mb-4
      [:h3.demo-subtitle.mb-2 "Minimal & Compact"]
      [:p.text-sm.text-gray-600.max-w-xs "Clean design, optimal for simple date selection"]]
     [:ty-calendar-month {:display-year "2024"
                          :display-month "12"
                          :style {:--calendar-min-width "280px"}
                          :on {:day-click handle-day-click}}]]

    ;; Holiday calendar - slightly wider
    [:div.flex.flex-col
     [:div.mb-4
      [:h3.demo-subtitle.mb-2 "Holiday Calendar"]
      [:p.text-sm.text-gray-600.max-w-xs "Sized for holiday indicators and stars"]]
     [:ty-calendar-month {:display-year "2024"
                          :display-month "12"
                          :style {:--calendar-min-width "320px"}
                          :day-content-fn "holidayDayContent"
                          :on {:day-click handle-day-click}}]]

    ;; Event dots - medium size
    [:div.flex.flex-col
     [:div.mb-4
      [:h3.demo-subtitle.mb-2 "Event Calendar"]
      [:p.text-sm.text-gray-600.max-w-xs "Optimized width for event dot indicators"]]
     [:ty-calendar-month {:display-year "2024"
                          :display-month "12"
                          :style {:--calendar-min-width "340px"}
                          :day-content-fn "eventDayContent"
                          :on {:day-click handle-day-click}}]]]

   ;; Second row: Business calendars  
   [:div.flex.flex-wrap.gap-8.justify-start.items-start.mb-8

    ;; Hotel booking - needs width for pricing
    [:div.flex.flex-col
     [:div.mb-4
      [:h3.demo-subtitle.mb-2 "Hotel Booking"]
      [:p.text-sm.text-gray-600.max-w-sm "Wider layout accommodates pricing information and weekend markup"]]
     [:ty-calendar-month {:display-year "2024"
                          :display-month "12"
                          :style {:--calendar-min-width "420px"}
                          :day-content-fn "hotelDayContent"
                          :on {:day-click handle-day-click}}]]

    ;; Project timeline
    [:div.flex.flex-col
     [:div.mb-4
      [:h3.demo-subtitle.mb-2 "Project Timeline"]
      [:p.text-sm.text-gray-600.max-w-sm "Balanced size for task counts and project information"]]
     [:ty-calendar-month {:display-year "2024"
                          :display-month "12"
                          :style {:--calendar-min-width "380px"}
                          :day-content-fn "projectDayContent"
                          :on {:day-click handle-day-click}}]]]

   ;; Third row: Complex content (full width)
   [:div.mb-8
    [:div.flex.flex-col.max-w-4xl
     [:div.mb-4
      [:h3.demo-subtitle.mb-2 "Complex Multi-Element Calendar"]
      [:p.text-sm.text-gray-600.max-w-3xl "Wide layout accommodates complex content with headers, events, and footers - demonstrates how unified flex layout maintains proportions"]]
     [:ty-calendar-month {:display-year "2024"
                          :display-month "12"
                          :style {:--calendar-min-width "600px"}
                          :day-content-fn "complexContent"
                          :on {:day-click handle-day-click}}]]]

   ;; Benefits section
   [:div.mt-8.p-4.bg-green-50.rounded-lg
    [:h4.font-semibold.mb-2 "🎯 Natural Sizing Benefits:"]
    [:div.text-sm.space-y-2
     [:div [:strong "Content-Driven:"] " Each calendar shows its optimal size for the content type"]
     [:div [:strong "Realistic Demos:"] " Developers see actual proportions, not forced constraints"]
     [:div [:strong "Unified Layout:"] " Headers automatically match day cell widths via flex"]
     [:div [:strong "Professional Look:"] " Content determines the layout, creating natural visual hierarchy"]]]

   ;; CSS custom properties example
   [:div.mt-6.p-4.bg-blue-50.rounded-lg
    [:h4.font-semibold.mb-2 "💡 Width Control Examples:"]
    [:pre.text-xs.bg-white.p-3.rounded.overflow-x-auto
     [:code
      "<!-- Compact calendar -->\n"
      "<ty-calendar-month style=\"--calendar-min-width: 280px;\"></ty-calendar-month>\n\n"
      "<!-- Hotel booking calendar -->\n"
      "<ty-calendar-month style=\"--calendar-min-width: 420px;\"\n"
      "                   day-content-fn=\"hotelDayContent\"></ty-calendar-month>\n\n"
      "<!-- Complex content calendar -->\n"
      "<ty-calendar-month style=\"--calendar-min-width: 600px;\"\n"
      "                   day-content-fn=\"complexContent\"></ty-calendar-month>\n\n"
      "<!-- Responsive calendar -->\n"
      "<ty-calendar-month style=\"--calendar-width: min(100%, 500px);\"></ty-calendar-month>"]]]

   ;; Event log
   [:div.mt-6.p-4.bg-gray-50.rounded-lg
    [:h4.font-semibold.mb-2 "Last Clicked Day:"]
    (if-let [clicked (:last-clicked @state/state)]
      [:div.text-sm
       [:div "Date: " (:year clicked) "-" (when (< (:month clicked) 10) "0") (:month clicked) "-" (when (< (:day clicked) 10) "0") (:day clicked)]
       [:div "Today?: " (if (:is-today clicked) "Yes" "No")]
       [:div "Weekend?: " (if (:is-weekend clicked) "Yes" "No")]
       [:div "Other Month?: " (if (:is-other-month clicked) "Yes" "No")]]
      [:div.text-gray-500.italic "Click a day to see details"])]])

(defn calendar-features []
  [:div.demo-section.mt-8
   [:h2.demo-title "Unified Flex Architecture"]

   [:div.grid.grid-cols-1.gap-6

    ;; Architecture benefits
    [:div.bg-gray-50.dark:bg-gray-800.rounded-lg.p-6
     [:h3.font-semibold.mb-4 "Unified Flex Layout Benefits"]
     [:ul.space-y-2.text-sm
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Automatic Synchronization:"] " Headers and day cells maintain equal widths automatically"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Content-Responsive:"] " Calendar adapts its size based on actual content needs"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "No Manual CSS Coordination:"] " Single flex container handles all layout concerns"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Natural Proportions:"] " Each use case displays at its optimal width"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "DOM-Only Content:"] " Strict type safety with full DOM API access"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "CSS Custom Properties:"] " Fine-grained width control when needed"]]]]

    ;; Technical implementation
    [:div.bg-blue-50.dark:bg-blue-900.rounded-lg.p-6
     [:h3.font-semibold.mb-4 "Technical Implementation"]
     [:div.text-sm.space-y-2
      [:p "Single flex container with rows - headers automatically match day cell widths:"]
      [:pre.bg-white.p-3.rounded.text-xs.overflow-x-auto
       [:code
        ".calendar-flex-container {\n"
        "  display: flex;\n"
        "  flex-direction: column;\n"
        "}\n\n"
        ".calendar-row {\n"
        "  display: flex;\n"
        "  flex: 1; /* Equal height distribution */\n"
        "}\n\n"
        ".calendar-cell {\n"
        "  flex: 1; /* Equal width distribution */\n"
        "}"]]]]

    ;; Real-world patterns
    [:div.bg-green-50.dark:bg-green-900.rounded-lg.p-6
     [:h3.font-semibold.mb-4 "Real-World Usage Patterns"]
     [:div.text-sm.space-y-2
      [:p "Common calendar types with their optimal sizing:"]
      [:ul.ml-4.space-y-1
       [:li "• " [:strong "Minimal (280px):"] " Simple date selection, compact interfaces"]
       [:li "• " [:strong "Standard (320-340px):"] " Holiday indicators, event dots, basic content"]
       [:li "• " [:strong "Business (380-420px):"] " Hotel booking, project timelines, pricing info"]
       [:li "• " [:strong "Complex (600px+):"] " Multi-element content, detailed information"]
       [:li "• " [:strong "Responsive:"] " Use CSS custom properties for adaptive sizing"]
       [:li "• " [:strong "Full Width:"] " Dashboard integration, complex data visualization"]]]]]])

(defn attribute-width-control []
  [:div.demo-section.mt-8
   [:h2.demo-title "New: Attribute-Based Width Control"]
   [:p.text-sm.text-gray-600.mb-6 "Control calendar width using simple attributes - same powerful CSS custom properties, better developer experience!"]

   ;; Comparison: Old vs New approach
   [:div.mb-8
    [:h3.demo-subtitle.mb-4 "Attribute vs CSS Custom Property Approaches"]
    [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8

     ;; Old approach (CSS custom properties)
     [:div.space-y-4
      [:div
       [:h4.font-semibold.mb-2 "CSS Custom Properties (Original)"]
       [:p.text-sm.text-gray-600.mb-4 "Still works! More verbose but maximum flexibility for responsive design."]
       [:ty-calendar-month {:display-year "2024"
                            :display-month "12"
                            :style {:--calendar-min-width "380px"
                                    :--calendar-max-width "500px"}
                            :day-content-fn "projectDayContent"
                            :on {:day-click handle-day-click}}]]

      [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
       [:code
        "[:ty-calendar-month\n"
        " {:style {:--calendar-min-width \"380px\"\n"
        "          :--calendar-max-width \"500px\"}\n"
        "  :day-content-fn \"projectDayContent\"}]"]]]

     ;; New approach (attributes)
     [:div.space-y-4
      [:div
       [:h4.font-semibold.mb-2 "Attributes (New!) ✨"]
       [:p.text-sm.text-gray-600.mb-4 "Clean, discoverable, and framework-friendly. Same power, better UX."]
       [:ty-calendar-month {:display-year "2024"
                            :display-month "12"
                            :min-width "380"
                            :max-width "500"
                            :day-content-fn "projectDayContent"
                            :on {:day-click handle-day-click}}]]

      [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
       [:code
        "[:ty-calendar-month\n"
        " {:min-width \"380\"\n"
        "  :max-width \"500\"\n"
        "  :day-content-fn \"projectDayContent\"}]"]]]]]

   ;; Attribute examples showcase
   [:div.mb-8
    [:h3.demo-subtitle.mb-4 "Attribute Width Control Examples"]
    [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-6

     ;; Fixed width
     [:div.space-y-3
      [:div
       [:h4.font-medium.mb-1 "Fixed Width"]
       [:p.text-xs.text-gray-600.mb-3 "width=\"400px\""]
       [:ty-calendar-month {:display-year "2024"
                            :display-month "12"
                            :width "400px"
                            :on {:day-click handle-day-click}}]]
      [:pre.text-xs.bg-gray-100.p-2.rounded
       [:code "width=\"400px\""]]]

     ;; Min width only
     [:div.space-y-3
      [:div
       [:h4.font-medium.mb-1 "Min Width"]
       [:p.text-xs.text-gray-600.mb-3 "min-width=\"320\" (grows with content)"]
       [:ty-calendar-month {:display-year "2024"
                            :display-month "12"
                            :min-width "320"
                            :day-content-fn "holidayDayContent"
                            :on {:day-click handle-day-click}}]]
      [:pre.text-xs.bg-gray-100.p-2.rounded
       [:code "min-width=\"320\""]]]

     ;; Width range
     [:div.space-y-3
      [:div
       [:h4.font-medium.mb-1 "Width Range"]
       [:p.text-xs.text-gray-600.mb-3 "min-width=\"350\" max-width=\"450\""]
       [:ty-calendar-month {:display-year "2024"
                            :display-month "12"
                            :min-width "350"
                            :max-width "450"
                            :day-content-fn "hotelDayContent"
                            :on {:day-click handle-day-click}}]]
      [:pre.text-xs.bg-gray-100.p-2.rounded
       [:code "min-width=\"350\"\nmax-width=\"450\""]]]]]

   ;; Unit support showcase
   [:div.mb-8
    [:h3.demo-subtitle.mb-4 "Unit Support & Responsive Values"]
    [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8

     ;; Different units
     [:div.space-y-4
      [:h4.font-medium.mb-2 "Multiple Units Supported"]
      [:div.grid.grid-cols-2.gap-4

       ;; Pixels (auto-added)
       [:div.space-y-2
        [:div
         [:p.text-xs.font-medium.mb-1 "Number → px"]
         [:p.text-xs.text-gray-600.mb-2 "min-width=\"280\""]
         [:ty-calendar-month {:display-year "2024"
                              :display-month "12"
                              :min-width "280"
                              :on {:day-click handle-day-click}}]]
        [:pre.text-xs.bg-gray-100.p-2.rounded
         [:code "min-width=\"280\"\n<!-- becomes 280px -->"]]]

       ;; Rem units
       [:div.space-y-2
        [:div
         [:p.text-xs.font-medium.mb-1 "REM Units"]
         [:p.text-xs.text-gray-600.mb-2 "min-width=\"20rem\""]
         [:ty-calendar-month {:display-year "2024"
                              :display-month "12"
                              :min-width "20rem"
                              :on {:day-click handle-day-click}}]]
        [:pre.text-xs.bg-gray-100.p-2.rounded
         [:code "min-width=\"20rem\""]]]]

      ;; CSS functions
      [:div.space-y-2
       [:h4.font-medium.mb-1 "CSS Functions"]
       [:p.text-xs.text-gray-600.mb-2 "width=\"clamp(300px, 50vw, 500px)\""]
       [:ty-calendar-month {:display-year "2024"
                            :display-month "12"
                            :width "clamp(300px, 50vw, 500px)"
                            :day-content-fn "eventDayContent"
                            :on {:day-click handle-day-click}}]
       [:pre.text-xs.bg-gray-100.p-2.rounded.overflow-x-auto
        [:code "width=\"clamp(300px, 50vw, 500px)\""]]]]

     ;; Responsive behavior
     [:div.space-y-4
      [:h4.font-medium.mb-2 "Responsive Behavior"]
      [:div.space-y-3
       [:p.text-xs.text-gray-600 "Attributes work perfectly with responsive CSS. Try resizing your browser:"]
       [:ty-calendar-month {:display-year "2024"
                            :display-month "12"
                            :width "min(100%, 450px)"
                            :min-width "280px"
                            :day-content-fn "complexContent"
                            :on {:day-click handle-day-click}}]
       [:pre.text-xs.bg-gray-100.p-2.rounded.overflow-x-auto
        [:code "width=\"min(100%, 450px)\"\nmin-width=\"280px\""]]

       [:div.mt-4.p-3.bg-blue-50.rounded.text-xs
        [:p.font-medium.mb-1 "💡 Pro Tip:"]
        [:p "Combine attributes with media queries for complete responsive control. Attributes set the base values, CSS can override with !important."]]]]]]

   ;; Benefits section
   [:div.p-4.bg-green-50.rounded-lg
    [:h4.font-semibold.mb-3 "🎯 Attribute-Based Width Benefits:"]
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-4.text-sm
     [:div.space-y-2
      [:div [:strong "✨ Better DX:"] " Clean, discoverable attributes vs verbose CSS"]
      [:div [:strong "🔧 Framework Friendly:"] " Works perfectly in React, Vue, ClojureScript"]
      [:div [:strong "📚 Self-Documenting:"] " Visible in HTML and dev tools"]]
     [:div.space-y-2
      [:div [:strong "🔄 Backward Compatible:"] " CSS custom properties still work"]
      [:div [:strong "🎨 Full Power:"] " Supports all CSS units and functions"]
      [:div [:strong "📱 Responsive Ready:"] " Plays nice with media queries"]]]]

   ;; Code examples
   [:div.mt-6.p-4.bg-blue-50.rounded-lg
    [:h4.font-semibold.mb-3 "💻 Usage Examples:"]
    [:div.grid.grid-cols-1.lg:grid-cols-2.gap-4
     [:div
      [:p.font-medium.mb-2.text-sm "HTML/ClojureScript:"]
      [:pre.text-xs.bg-white.p-3.rounded.overflow-x-auto
       [:code
        "<!-- Simple and clean -->\n"
        "<ty-calendar-month\n"
        "  width=\"400px\"\n"
        "  min-width=\"300px\"\n"
        "  day-content-fn=\"myRenderer\">\n"
        "</ty-calendar-month>\n\n"
        ";; ClojureScript/Replicant\n"
        "[:ty-calendar-month\n"
        " {:width \"400px\"\n"
        "  :min-width \"300px\"\n"
        "  :day-content-fn \"myRenderer\"}]"]]]

     [:div
      [:p.font-medium.mb-2.text-sm "React JSX:"]
      [:pre.text-xs.bg-white.p-3.rounded.overflow-x-auto
       [:code
        "<ty-calendar-month\n"
        "  width=\"400px\"\n"
        "  minWidth=\"300px\"\n"
        "  dayContentFn=\"myRenderer\"\n"
        "/>\n\n"
        "// Or with CSS for responsive\n"
        "<ty-calendar-month\n"
        "  width=\"clamp(300px, 50vw, 600px)\"\n"
        "  dayContentFn=\"myRenderer\"\n"
        "/>"]]]]]])

(defn view []
  [:div.p-6
   (stateful-calendar-demo)
   (individual-selection-demo)
   (natural-content-sizing)
   (attribute-width-control)
   (calendar-features)])
