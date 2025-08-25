(ns ty.demo.views.calendar
  "Calendar component demos with enhanced features"
  (:require [ty.date.core :as date]
            [ty.demo.state :as state]))

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
    (js/console.log "Month changed:" #js {:year year :month month})))

(defn basic-calendar-month []
  [:div.demo-section
   [:h2.demo-title "Calendar Month Component"]

   [:div.grid.grid-cols-1.md:grid-cols-2.gap-8

    ;; Basic calendar
    [:div
     [:h3.demo-subtitle "Basic Calendar Month"]
     [:p.text-gray-600.dark:text-gray-400.mb-4
      "A simple calendar month grid with date selection"]
     [:div.max-w-sm
      [:ty-calendar-month {:on {:date-select handle-date-select
                                :month-change handle-month-change}}]]
     [:div.mt-4.text-sm.text-gray-600.dark:text-gray-400
      (when-let [selected (:selected-date @state/state)]
        [:div "Selected: " (date/format-date selected "en-US" 
                                            {:weekday "long" :year "numeric" 
                                             :month "long" :day "numeric"})])]]

    ;; Pre-selected date with ISO string
    [:div
     [:h3.demo-subtitle "ISO String Value"]
     [:p.text-gray-600.dark:text-gray-400.mb-4
      "Calendar accepts ISO date strings"]
     [:div.max-w-sm
      [:ty-calendar-month {:value "2024-12-25"
                           :on {:date-select handle-date-select}}]]
     [:p.mt-2.text-sm.text-gray-500 "Christmas 2024 is pre-selected using ISO string"]]

    ;; With constraints
    [:div
     [:h3.demo-subtitle "Min/Max Date Constraints"]
     [:p.text-gray-600.dark:text-gray-400.mb-4
      "Restrict selectable date range"]
     [:div.max-w-sm
      [:ty-calendar-month {:min-date "2024-12-10"
                           :max-date "2024-12-20"
                           :year "2024"
                           :month "12"
                           :on {:date-select handle-date-select}}]]
     [:p.mt-2.text-sm.text-gray-500 "Only Dec 10-20, 2024 are selectable"]]

    ;; With disabled dates
    [:div
     [:h3.demo-subtitle "Disabled Dates"]
     [:p.text-gray-600.dark:text-gray-400.mb-4
      "Specific dates can be disabled"]
     [:div.max-w-sm
      (let [today (date/today)
            disabled-dates (clojure.string/join ","
                                               [(+ today (* 2 date/day))
                                                (+ today (* 5 date/day))
                                                (+ today (* 7 date/day))])]
        [:ty-calendar-month {:disabled-dates disabled-dates
                             :on {:date-select handle-date-select}}])]
     [:p.mt-2.text-sm.text-gray-500 "Some future dates are disabled"]]

    ;; Monday first
    [:div
     [:h3.demo-subtitle "Monday as First Day"]
     [:p.text-gray-600.dark:text-gray-400.mb-4
      "Week starts on Monday (European style)"]
     [:div.max-w-sm
      [:ty-calendar-month {:first-day-of-week "1"
                           :on {:date-select handle-date-select}}]]
     [:p.mt-2.text-sm.text-gray-500 "first-day-of-week=\"1\" for Monday"]]

    ;; Different locale
    [:div
     [:h3.demo-subtitle "Localized (German)"]
     [:p.text-gray-600.dark:text-gray-400.mb-4
      "Weekday names in German"]
     [:div.max-w-sm
      [:ty-calendar-month {:locale "de-DE"
                           :first-day-of-week "1"
                           :on {:date-select handle-date-select}}]]
     [:p.mt-2.text-sm.text-gray-500 "Using locale=\"de-DE\""]]]])

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
       [:span [:strong "First Day of Week:"] " Configure Sunday (0) or Monday (1) as first day"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Enhanced Events:"] " date-select includes formatted value, month-change for navigation"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Visual States:"] " Disabled, focused, selected, today, weekend, other-month"]]]]

    ;; Code example
    [:div.bg-gray-50.dark:bg-gray-800.rounded-lg.p-6
     [:h3.font-semibold.mb-4 "Usage Examples"]
     [:pre.code-block.text-xs.overflow-x-auto
      [:code
       ";; Basic calendar with ISO string value\n"
       "<ty-calendar-month value=\"2024-12-25\"></ty-calendar-month>\n\n"
       ";; With constraints\n"
       "<ty-calendar-month \n"
       "  min-date=\"2024-12-01\"\n"
       "  max-date=\"2024-12-31\"\n"
       "  disabled-dates=\"2024-12-24,2024-12-25,2024-12-31\">\n"
       "</ty-calendar-month>\n\n"
       ";; European style (Monday first) in German\n"
       "<ty-calendar-month \n"
       "  locale=\"de-DE\"\n"
       "  first-day-of-week=\"1\">\n"
       "</ty-calendar-month>\n\n"
       ";; Handle events\n"
       "[:ty-calendar-month\n"
       " {:value selected-date\n"
       "  :min-date (date/format-value min)\n"
       "  :max-date (date/format-value max)\n"
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