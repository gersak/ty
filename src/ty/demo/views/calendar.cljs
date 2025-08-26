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
    (js/console.log "Month changed:" #js {:year year
                                          :month month})))

(defn basic-calendar-month []
  [:div.demo-section
   [:h2.demo-title "Stateless Calendar - Fallback Demo"]

   [:div.grid.grid-cols-1.md:grid-cols-2.gap-8
    ;; Explicit month
    [:div
     [:h3.demo-subtitle "December 2024 (Explicit)"]
     [:p.text-sm.text-gray-600.mb-4 "display-year=\"2024\" display-month=\"12\""]
     [:ty-calendar-month {:display-year "2024"
                          :display-month "12"}]]

    ;; Fallback to current month
    [:div
     [:h3.demo-subtitle "Current Month (Fallback)"]
     [:p.text-sm.text-gray-600.mb-4 "No attributes - falls back to today's month"]
     [:ty-calendar-month]]]])

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
