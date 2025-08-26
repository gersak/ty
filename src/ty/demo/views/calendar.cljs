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
                         :projectDayContent (exists? (.-projectDayContent js/window))})
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
   [:h2.demo-title "Stateless Calendar - DOM-Only Render Functions"]
   [:p.text-sm.text-gray-600.mb-6 "All custom render functions return DOM elements for optimal performance and security"]

   [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-4.gap-4
    ;; Default rendering
    [:div
     [:h3.demo-subtitle "Default Rendering"]
     [:p.text-sm.text-gray-600.mb-4 "Plain day numbers"]
     [:ty-calendar-month {:display-year "2024"
                          :display-month "12"
                          :on {:day-click handle-day-click}}]]

    ;; Holiday indicators
    [:div
     [:h3.demo-subtitle "Holiday Indicators"]
     [:p.text-sm.text-gray-600.mb-4 "Days with holiday stars"]
     [:ty-calendar-month {:display-year "2024"
                          :display-month "12"
                          :day-content-fn "holidayDayContent"
                          :on {:day-click handle-day-click}}]]

    ;; Event indicators
    [:div
     [:h3.demo-subtitle "Event Calendar"]
     [:p.text-sm.text-gray-600.mb-4 "Days with event dots"]
     [:ty-calendar-month {:display-year "2024"
                          :display-month "12"
                          :day-content-fn "eventDayContent"
                          :on {:day-click handle-day-click}}]]

    ;; Hotel pricing
    [:div
     [:h3.demo-subtitle "Hotel Booking"]
     [:p.text-sm.text-gray-600.mb-4 "Pricing calendar"]
     [:ty-calendar-month {:display-year "2024"
                          :display-month "12"
                          :day-content-fn "hotelDayContent"
                          :on {:day-click handle-day-click}}]]

    ;; Project tasks
    [:div
     [:h3.demo-subtitle "Project Timeline"]
     [:p.text-sm.text-gray-600.mb-4 "Task count display"]
     [:ty-calendar-month {:display-year "2024"
                          :display-month "12"
                          :day-content-fn "projectDayContent"
                          :on {:day-click handle-day-click}}]]]

   [:div.mt-6.p-4.bg-green-50.rounded-lg
    [:h4.font-semibold.mb-2 "✅ DOM-Only Architecture Benefits:"]
    [:div.text-sm.space-y-2
     [:div [:strong "Performance:"] " Direct DOM manipulation, no innerHTML parsing"]
     [:div [:strong "Security:"] " No XSS risks from user-generated content"]
     [:div [:strong "Type Safety:"] " Compile-time guarantees for render functions"]
     [:div [:strong "Flexibility:"] " Full DOM API access for complex layouts"]]]

   [:div.mt-6.p-4.bg-blue-50.rounded-lg
    [:h4.font-semibold.mb-2 "💡 Render Function Pattern:"]
    [:pre.text-xs.bg-white.p-3.rounded.overflow-x-auto
     [:code
      "(defn my-render-fn [day-context]\n"
      "  (let [container (.createElement js/document \"div\")\n"
      "        span (.createElement js/document \"span\")]\n"
      "    (set! (.-className container) \"custom-day\")\n"
      "    (set! (.-textContent span) \"Content\")\n"
      "    (.appendChild container span)\n"
      "    container)) ; Return DOM element"]]]

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
   [:h2.demo-title "Architecture & Features"]

   [:div.grid.grid-cols-1.gap-6

    ;; Architecture benefits
    [:div.bg-gray-50.dark:bg-gray-800.rounded-lg.p-6
     [:h3.font-semibold.mb-4 "DOM-Only Render Architecture"]
     [:ul.space-y-2.text-sm
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Strict Type Safety:"] " Custom render functions must return DOM elements"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Zero XSS Risk:"] " No innerHTML usage, only safe DOM manipulation"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Optimal Performance:"] " Direct element creation and attachment"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Full DOM API:"] " Access to complete browser DOM functionality"]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "Framework Agnostic:"] " Works with vanilla JS, React, Vue, etc."]]
      [:li.flex.items-start
       [:span.text-green-500.mr-2 "✓"]
       [:span [:strong "ClojureScript Native:"] " Seamless data conversion with cljs-bean"]]]]

    ;; Technical implementation
    [:div.bg-blue-50.dark:bg-blue-900.rounded-lg.p-6
     [:h3.font-semibold.mb-4 "Technical Implementation"]
     [:div.text-sm.space-y-2
      [:p "Render functions receive day context and must return DOM elements:"]
      [:pre.bg-white.p-3.rounded.text-xs.overflow-x-auto
       [:code
        ";; ✅ Correct - returns DOM element\n"
        "(fn [day-context]\n"
        "  (let [el (.createElement js/document \"div\")]\n"
        "    (set! (.-textContent el) \"Content\")\n"
        "    el))\n\n"
        ";; ❌ Error - returns string\n"
        "(fn [day-context]\n"
        "  \"<div>Content</div>\") ; Throws error!"]]]]

    ;; Usage examples
    [:div.bg-yellow-50.dark:bg-yellow-900.rounded-lg.p-6
     [:h3.font-semibold.mb-4 "Usage Examples"]
     [:pre.code-block.text-xs.overflow-x-auto
      [:code
       ";; Basic calendar month\n"
       "[:ty-calendar-month]\n\n"
       ";; With custom render function\n"
       "[:ty-calendar-month\n"
       " {:day-content-fn \"myRenderFunction\"}]\n\n"
       ";; With display date\n"
       "[:ty-calendar-month\n"
       " {:display-year \"2024\"\n"
       "  :display-month \"12\"\n"
       "  :day-content-fn \"hotelPricing\"}]\n\n"
       ";; All attributes\n"
       "[:ty-calendar-month\n"
       " {:display-year \"2024\"\n"
       "  :display-month \"12\"\n"
       "  :day-content-fn \"customContent\"\n"
       "  :day-classes-fn \"customClasses\"}]"]]]

    ;; Real-world patterns
    [:div.bg-green-50.dark:bg-green-900.rounded-lg.p-6
     [:h3.font-semibold.mb-4 "Real-World Patterns"]
     [:div.text-sm.space-y-2
      [:p "Common use cases implemented with DOM-only functions:"]
      [:ul.ml-4.space-y-1
       [:li "• " [:strong "Hotel Booking:"] " Price display with weekend markup"]
       [:li "• " [:strong "Event Calendar:"] " Visual indicators for scheduled events"]
       [:li "• " [:strong "Project Timeline:"] " Task counts and progress indicators"]
       [:li "• " [:strong "Holiday Calendar:"] " Special styling for holidays and weekends"]
       [:li "• " [:strong "Availability Calendar:"] " Booking status and constraints"]
       [:li "• " [:strong "Multi-Calendar Views:"] " Different render functions per calendar"]]]]]])

(defn view []
  [:div.p-6
   (basic-calendar-month)
   (calendar-features)])
