(ns ty.demo.views.calendar
  "Calendar component demos with enhanced features"
  (:require
    [cljs-bean.core :refer [->clj ->js]]
    [ty.date.core :as date]
    [ty.demo.state :as state]))

;; Demo render functions for testing  
;; Set up demo render functions ONCE when namespace loads
(defonce demo-functions-setup
  (do
    (js/console.log "Setting up demo render functions...")

    ;; Holiday day content - shows day with holiday indicator
    (set! (.-holidayDayContent js/window)
          (fn [^js context]
            (let [{:keys [day-in-month holiday?]} (->clj context)]
              (if true
                (str "<div class=\"day-content\"><span>" day-in-month "</span><span class=\"holiday-star\">★</span></div>")
                (str day-in-month)))))

    ;; Event dots day content - shows day with event indicators  
    (set! (.-eventDayContent js/window)
          (fn [^js context]
            (let [{:keys [day-in-month]} (->clj context)
                  ;; Mock some events for demo (15th, 18th, 22nd, 25th have events)
                  has-events (#{15 18 22 25} day-in-month)]
              (if has-events
                (str "<div class=\"day-content\"><span>" day-in-month "</span><div class=\"event-dots\"><span class=\"event-dot\"></span></div></div>")
                (str day-in-month)))))

    ;; Price display for booking calendar
    (set! (.-hotelDayContent js/window)
          (fn [^js context]
            (let [{:keys [day-in-month other-month weekend]} (->clj context)
                  base-price 120
                  weekend-markup (if weekend 50 0)
                  price (+ base-price weekend-markup)]
              (if other-month
                (str day-in-month)
                (str "<div class=\"hotel-day\"><span class=\"day-num\">" day-in-month "</span><span class=\"price\">$" price "</span></div>")))))

    ;; Task count for project timeline  
    (set! (.-projectDayContent js/window)
          (fn [^js context]
            (let [{:keys [day-in-month]} (->clj context)
                  ;; Mock task counts for demo
                  task-count (case day-in-month
                               5 3, 12 1, 18 5, 22 2, 28 4
                               0)]
              (if (> task-count 0)
                (str "<div class=\"project-day\"><span class=\"day-num\">" day-in-month "</span><span class=\"task-count\">" task-count " tasks</span></div>")
                (str day-in-month)))))

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
    (js/console.log "Demo render functions registered:"
                    #js {:holidayDayContent (exists? (.-holidayDayContent js/window))
                         :eventDayContent (exists? (.-eventDayContent js/window))
                         :hotelDayContent (exists? (.-hotelDayContent js/window))
                         :projectDayContent (exists? (.-projectDayContent js/window))})
    :setup-complete))

 ;; APPROACH 1: Return HTML Strings (Current - should work now)
(set! (.-holidayDayContent js/window)
      (fn [^js context]
        (let [{:keys [day-in-month holiday?]} (->clj context)]
          (if holiday?
                ;; Return HTML string - component uses innerHTML
            "<div class=\"day-content\"><span>25</span><span class=\"holiday-star\">★</span></div>"
            (str day-in-month)))))

    ;; APPROACH 2: Return DOM Elements (Better)
(set! (.-hotelDayContentDOM js/window)
      (fn [^js context]
        (let [{:keys [day-in-month other-month weekend]} (->clj context)]
          (if other-month
            (str day-in-month)
                ;; Create actual DOM elements
            (let [container (.createElement js/document "div")
                  day-span (.createElement js/document "span")
                  price-span (.createElement js/document "span")
                  base-price 120
                  weekend-markup (if weekend 50 0)
                  price (+ base-price weekend-markup)]
              (set! (.-className container) "hotel-day")
              (set! (.-className day-span) "day-num")
              (set! (.-textContent day-span) (str day-in-month))
              (set! (.-className price-span) "price")
              (set! (.-textContent price-span) (str "$" price))
              (.appendChild container day-span)
              (.appendChild container price-span)
              container)))))

    ;; APPROACH 3: Mixed Content (Most Flexible)
(set! (.-eventDayContentMixed js/window)
      (fn [^js context]
        (let [{:keys [day-in-month]} (->clj context)
              has-events (#{15 18 22 25} day-in-month)]
          (if has-events
                ;; Create container with mixed content
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
            (str day-in-month)))))

    ;; Keep your existing string-based functions working
    ;; Holiday day content - shows day with holiday indicator

(defn handle-day-click [^js event]
  (let [detail (.-detail event)
        date-parts (.-dateParts detail)]
    (js/console.log "Day clicked:" detail #_(aget detail "date-parts"))
    (swap! state/state assoc :last-clicked
           {:year (.-year date-parts)
            :month (.-month date-parts)
            :day (.-day date-parts)
            :is-today (.-isToday detail)
            :is-weekend (.-isWeekend detail)
            :is-other-month (.-isOtherMonth detail)
            :value (.-value detail)})))

(defn handle-date-select [^js event]
  (let [detail (.-detail event)
        value (.-value detail)
        formatted (.-formatted detail)
        date-obj (.-date detail)]
    (js/console.log "Date selected:"
                    #js {:value value
                         :formatted formatted
                         :date (.toLocaleDateString date-obj)
                         :context (.-context detail)})
    (swap! state/state assoc :selected-date value)))

(defn handle-month-change [^js event]
  (let [detail (.-detail event)
        year (.-year detail)
        month (.-month detail)]
    (js/console.log "Month changed:" #js {:year year
                                          :month month})))

(defn basic-calendar-month []
  [:div.demo-section
   [:h2.demo-title "Stateless Calendar - Render Functions"]
   [:p.text-sm.text-gray-600.mb-6 "Demonstrating different approaches to custom day rendering"]

   [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-4.gap-4
    ;; Default rendering
    [:div
     [:h3.demo-subtitle "Default Rendering"]
     [:p.text-sm.text-gray-600.mb-4 "Plain day numbers"]
     [:ty-calendar-month {:display-year "2024"
                          :display-month "12"
                          :on {:day-click handle-day-click}}]]

    ;; HTML String approach  
    [:div
     [:h3.demo-subtitle "HTML Strings"]
     [:p.text-sm.text-gray-600.mb-4 "Returns HTML as strings"]
     [:ty-calendar-month {:display-year "2024"
                          :display-month "12"
                          :day-content-fn "holidayDayContent"
                          :on {:day-click handle-day-click}}]]

    ;; DOM Elements approach
    [:div
     [:h3.demo-subtitle "DOM Elements"]
     [:p.text-sm.text-gray-600.mb-4 "Hotel pricing with DOM"]
     [:ty-calendar-month {:display-year "2024"
                          :display-month "12"
                          :day-content-fn "hotelDayContentDOM"
                          :on {:day-click handle-day-click}}]]

    ;; Mixed content approach
    [:div
     [:h3.demo-subtitle "Mixed Content"]
     [:p.text-sm.text-gray-600.mb-4 "Events with DOM elements"]
     [:ty-calendar-month {:display-year "2024"
                          :display-month "12"
                          :day-content-fn "eventDayContentMixed"
                          :on {:day-click handle-day-click}}]]]

   [:div.mt-6.p-4.bg-blue-50.rounded-lg
    [:h4.font-semibold.mb-2 "Render Function Approaches:"]
    [:div.text-sm.space-y-2
     [:div [:strong "HTML Strings:"] " Return HTML as string - component uses innerHTML"]
     [:div [:strong "DOM Elements:"] " Return actual DOM nodes - component appends them"]
     [:div [:strong "Mixed:"] " Combine both approaches as needed"]]]

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
   [:h2.demo-title "Enhanced Features"]

   [:div.grid.grid-cols-1.gap-6

    ;; Feature list
    [:div.bg-gray-50.dark:bg-gray-800.rounded-lg.p-6
     [:h3.font-semibold.mb-4 "New Features in v2"]
     [:ul.space-y-2.text-sm
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Flexible Value Parsing:"] " Supports ISO strings, timestamps, and Date objects"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Date Constraints:"] " Min/max date limits and specific disabled dates"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Keyboard Navigation:"] " Arrow keys to move, Enter/Space to select"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Accessibility:"] " ARIA attributes, focus management, screen reader support"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Localization:"] " Weekday names in any locale via Intl API"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "First Day of Week:"] " Monday (1) by default, or Sunday (0) for US style"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Enhanced Events:"] " date-select includes formatted value, month-change for navigation"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Full Calendar Component:"] " Complete calendar with month/year navigation dropdowns and today button"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Clear Navigation API:"] " view-year/view-month initial-only attributes (React-compatible)"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Visual States:"] " Disabled, focused, selected, today, weekend, other-month"]]]]

    ;; Code example
    [:div.bg-gray-50.dark:bg-gray-800.rounded-lg.p-6
     [:h3.font-semibold.mb-4 "Usage Examples"]
     [:pre.code-block.text-xs.overflow-x-auto
      [:code
       ";; Full calendar with navigation\n"
       "<ty-calendar show-today-button=\"true\"></ty-calendar>\n\n"
       ";; Calendar with initial view\n"
       "<ty-calendar view-year=\"2024\" view-month=\"12\"></ty-calendar>\n\n"
       ";; Calendar with constraints\n"
       "<ty-calendar \n"
       "  view-year=\"2024\"\n"
       "  view-month=\"12\"\n"
       "  min-date=\"2024-12-01\"\n"
       "  max-date=\"2024-12-31\"\n"
       "  disabled-dates=\"2024-12-24,2024-12-25\"\n"
       "  show-today-button=\"true\">\n"
       "</ty-calendar>\n\n"
       ";; Localized calendar (German)\n"
       "<ty-calendar \n"
       "  locale=\"de-DE\"\n"
       "  first-day-of-week=\"1\"\n"
       "  show-today-button=\"true\">\n"
       "</ty-calendar>\n\n"
       ";; Calendar month grid only\n"
       "<ty-calendar-month view-year=\"2024\" view-month=\"12\"></ty-calendar-month>\n\n"
       ";; Replicant usage with event handlers\n"
       "[:ty-calendar\n"
       " {:value selected-date\n"
       "  :view-year \"2024\"\n"
       "  :view-month \"12\"\n"
       "  :show-today-button true\n"
       "  :on {:date-select handle-selection\n"
       "       :month-change handle-navigation}}]"]]]

    ;; Keyboard navigation
    [:div.bg-blue-50.dark:bg-blue-900.rounded-lg.p-6
     [:h3.font-semibold.mb-4 "Keyboard Navigation"]
     [:div.text-sm.space-y-2
      [:p "Focus the calendar and use keyboard to navigate:"]
      [:ul.ml-4.space-y-1
       [:li "• " [:kbd.px-2.py-1.bg-gray-200.dark:bg-gray-700.rounded "←"] " / "
        [:kbd.px-2.py-1.bg-gray-200.dark:bg-gray-700.rounded "→"] " - Navigate days"]
       [:li "• " [:kbd.px-2.py-1.bg-gray-200.dark:bg-gray-700.rounded "↑"] " / "
        [:kbd.px-2.py-1.bg-gray-200.dark:bg-gray-700.rounded "↓"] " - Navigate weeks"]
       [:li "• " [:kbd.px-2.py-1.bg-gray-200.dark:bg-gray-700.rounded "Enter"] " / "
        [:kbd.px-2.py-1.bg-gray-200.dark:bg-gray-700.rounded "Space"] " - Select focused date"]
       [:li "• " [:kbd.px-2.py-1.bg-gray-200.dark:bg-gray-700.rounded "Tab"] " - Move focus in/out"]]]]

    ;; Technical details
    [:div.bg-yellow-50.dark:bg-yellow-900.rounded-lg.p-6
     [:h3.font-semibold.mb-4 "Value Format Support"]
     [:div.text-sm.space-y-2
      [:p "The component accepts multiple value formats:"]
      [:ul.ml-4.space-y-1.font-mono.text-xs
       [:li "• ISO strings: \"2024-12-25\", \"2024-12-25T10:30:00Z\""]
       [:li "• Timestamps: 1735084800000 (milliseconds)"]
       [:li "• Date objects: new Date(2024, 11, 25)"]
       [:li "• nil/empty: No selection"]]
      [:p.mt-3 "Events return:"]
      [:ul.ml-4.space-y-1.font-mono.text-xs
       [:li "• value: Numeric timestamp (milliseconds)"]
       [:li "• date: JavaScript Date object"]
       [:li "• formatted: ISO date string (YYYY-MM-DD)"]
       [:li "• context: Full day context from timing library"]]]]]])

(defn view []
  [:div.p-6
   (basic-calendar-month)
   (calendar-features)])
