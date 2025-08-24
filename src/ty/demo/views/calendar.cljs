(ns ty.demo.views.calendar
  "Calendar component demos"
  (:require [ty.date.core :as date]
            [ty.demo.state :as state]))

(defn handle-date-select [^js event]
  (let [detail (.-detail event)
        value (.-value detail)
        date-obj (.-date detail)]
    (js/console.log "Date selected:"
                    #js {:value value
                         :date (.toLocaleDateString date-obj)
                         :context (.-context detail)})
    (swap! state/state assoc :selected-date value)))

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
      [:ty-calendar-month {:on {:date-select handle-date-select}}]]
     [:div.mt-4.text-sm.text-gray-600.dark:text-gray-400
      (when-let [selected (:selected-date @state/state)]
        [:div "Selected: " (.toLocaleDateString (date/value->time selected))])]]

    ;; Pre-selected date
    [:div
     [:h3.demo-subtitle "With Pre-selected Date"]
     [:p.text-gray-600.dark:text-gray-400.mb-4
      "Calendar with an initial selected date"]
     [:div.max-w-sm
      [:ty-calendar-month {:value (str (date/time->value (date/date 2024 12 25)))
                           :on {:date-select handle-date-select}}]]
     [:p.mt-2.text-sm.text-gray-500 "Christmas 2024 is pre-selected"]]

    ;; Today's date
    [:div
     [:h3.demo-subtitle "Today Highlighted"]
     [:p.text-gray-600.dark:text-gray-400.mb-4
      "Today's date is highlighted with a red color"]
     [:div.max-w-sm
      [:ty-calendar-month {:value (str (date/today))
                           :on {:date-select handle-date-select}}]]
     [:p.mt-2.text-sm.text-gray-500 "Today is selected and highlighted"]]

    ;; Custom month/year
    [:div
     [:h3.demo-subtitle "Custom Month View"]
     [:p.text-gray-600.dark:text-gray-400.mb-4
      "Display a specific month and year"]
     [:div.max-w-sm
      [:ty-calendar-month {:year "2025"
                           :month "2"
                           :on {:date-select handle-date-select}}]]
     [:p.mt-2.text-sm.text-gray-500 "Showing February 2025"]]]])

(defn calendar-features []
  [:div.demo-section.mt-8
   [:h2.demo-title "Calendar Features"]

   [:div.grid.grid-cols-1.gap-6

    ;; Feature list
    [:div.bg-gray-50.dark:bg-gray-800.rounded-lg.p-6
     [:h3.font-semibold.mb-4 "Current Features"]
     [:ul.space-y-2.text-sm
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span "6-week calendar grid (42 days) for consistent layout"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span "Previous and next month days shown (grayed out)"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span "Today's date highlighted with red color and dot indicator"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span "Selected date highlighted with blue background"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span "Weekend days styled differently"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span "Hover effects for interactive feedback"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span "Custom events for date selection"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span "Dark theme support"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span "Responsive design for mobile"]]]]

    ;; Code example
    [:div.bg-gray-50.dark:bg-gray-800.rounded-lg.p-6
     [:h3.font-semibold.mb-4 "Usage Example"]
     [:pre.code-block.text-xs
      [:code
       "<!-- Basic calendar -->\n"
       "<ty-calendar-month></ty-calendar-month>\n\n"
       "<!-- With selected date -->\n"
       "<ty-calendar-month value=\"1735171200000\"></ty-calendar-month>\n\n"
       "<!-- Specific month/year -->\n"
       "<ty-calendar-month year=\"2025\" month=\"6\"></ty-calendar-month>\n\n"
       "<!-- With event handler -->\n"
       "<ty-calendar-month ondate-select=\"handleDate\"></ty-calendar-month>"]]]

    ;; Technical details
    [:div.bg-blue-50.dark:bg-blue-900.rounded-lg.p-6
     [:h3.font-semibold.mb-4 "Technical Implementation"]
     [:div.text-sm.space-y-2
      [:p "• Uses " [:strong "gersak/timing"] " library for date calculations"]
      [:p "• Numeric domain operations (milliseconds since epoch) for performance"]
      [:p "• Component-local state management"]
      [:p "• Property/attribute dual support for framework compatibility"]
      [:p "• Shadow DOM encapsulation with CSS variables for theming"]
      [:p "• Zero external dependencies (only timing library)"]]]]])

(defn view []
  [:div.p-6
   (basic-calendar-month)
   (calendar-features)])
