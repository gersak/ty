(ns ty.demo.views.calendar
  "Simplified calendar component demos - focused and educational"
  (:require
    [cljs-bean.core :refer [->clj ->js]]
    [ty.date.core :as date]
    [ty.demo.state :as state]))

;; Simplified render functions - only essential examples
(defonce demo-functions-setup
  (do
    (js/console.log "Setting up simplified calendar demo functions...")

    ;; Hotel pricing example - demonstrates business use case
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

    ;; Event dots example - demonstrates simple content enhancement
    (set! (.-eventDayContent js/window)
          (fn [^js context]
            (let [{:keys [day-in-month]} (->clj context)
                  ;; Mock some events for demo (15th, 18th, 22nd, 25th have events)
                  has-events (#{15 18 22 25} day-in-month)]
              (if has-events
                ;; Create DOM elements for event display
                (let [container (.createElement js/document "div")
                      day-span (.createElement js/document "span")
                      dot (.createElement js/document "span")]
                  (set! (.-className container) "day-content")
                  (set! (.-textContent day-span) (str day-in-month))
                  (set! (.-className dot) "event-dot")
                  (.appendChild container day-span)
                  (.appendChild container dot)
                  container)
                ;; Simple day number for no events
                (let [span (.createElement js/document "span")]
                  (set! (.-textContent span) (str day-in-month))
                  span)))))

    (js/console.log "Simplified demo functions registered")
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
            :value (.-value detail)
            :formatted (.toLocaleDateString (js/Date. (.-value detail)) "en-US")})))

(defn stateful-calendar-demo []
  [:div.demo-section
   [:h2.demo-title "🎉 Complete Calendar with Navigation (ty-calendar)"]
   [:p.text-sm.text-gray-600.mb-6 "Full-featured calendar component with navigation controls, selection state, and custom content."]

   ;; Basic examples
   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8.mb-8

    ;; Basic calendar
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "Basic Calendar"]
      [:p.text-sm.text-gray-600.mb-4 "Default calendar with navigation and today button."]
      [:ty-calendar {:width "350px"
                     :on {:day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       "[:ty-calendar\n"
       " {:width \"350px\"\n"
       "  :on {:day-click handle-day-click}}]"]]]

    ;; Calendar with initial selection
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "📅 With Initial Selected Date"]
      [:p.text-sm.text-gray-600.mb-4 "Pre-selected date (December 25, 2024) with automatic navigation to that month."]
      [:ty-calendar {:width "350px"
                     :value "2024-12-25" ; Initial selected date
                     :on {:day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       "[:ty-calendar\n"
       " {:width \"350px\"\n"
       "  :value \"2024-12-25\"  ; Pre-select Christmas\n"
       "  :on {:day-click handle-day-click}}]"]]]]

   ;; Advanced examples with custom content
   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8.mb-8

    ;; Hotel booking calendar with selection
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "🏨 Hotel Booking with Selection"]
      [:p.text-sm.text-gray-600.mb-4 "Business calendar with pricing and pre-selected date (Dec 22)."]
      [:ty-calendar {:width "420px"
                     :value "2024-12-22" ; Pre-select a weekend date
                     :day-content-fn "hotelDayContent"
                     :on {:day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       "[:ty-calendar\n"
       " {:width \"420px\"\n"
       "  :value \"2024-12-22\"  ; Pre-select weekend\n"
       "  :day-content-fn \"hotelDayContent\"}]"]]]

    ;; Event calendar with selection
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "📅 Event Calendar with Selection"]
      [:p.text-sm.text-gray-600.mb-4 "Calendar with event indicators and pre-selected event date (Dec 18)."]
      [:ty-calendar {:width "350px"
                     :value "2024-12-18" ; Pre-select an event day
                     :day-content-fn "eventDayContent"
                     :on {:day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       "[:ty-calendar\n"
       " {:width \"350px\"\n"
       "  :value \"2024-12-18\"  ; Pre-select event day\n"
       "  :day-content-fn \"eventDayContent\"}]"]]]]

   ;; Selection behavior explanation
   [:div.p-4.bg-blue-50.rounded-lg.mb-6
    [:h4.font-semibold.mb-3 "🎯 Selection Behavior:"]
    [:div.text-sm.space-y-2
     [:div [:strong "Initial Value:"] " Use `:value` attribute to pre-select a date"]
     [:div [:strong "Date Formats:"] " ISO strings (\"2024-12-25\"), timestamps, or Date objects"]
     [:div [:strong "Navigation:"] " Calendar automatically navigates to the month of the selected date"]
     [:div [:strong "Visual Feedback:"] " Selected date gets 'selected' CSS class for styling"]
     [:div [:strong "State Management:"] " Component manages selection internally - listen to day-click events"]
     [:div [:strong "Event Details:"] " Click events include full date context and selection state"]]]

   ;; Value format examples
   [:div.p-4.bg-green-50.rounded-lg.mb-6
    [:h4.font-semibold.mb-3 "📝 Supported Value Formats:"]
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-4.text-sm
     [:div
      [:p.font-medium.mb-2 "String Formats:"]
      [:pre.text-xs.bg-white.p-3.rounded
       [:code
        ":value \"2024-12-25\"           ; ISO date\n"
        ":value \"2024-12-25T10:30:00Z\" ; ISO datetime\n"
        ":value \"Dec 25, 2024\"         ; Parsed string"]]]
     [:div
      [:p.font-medium.mb-2 "Other Formats:"]
      [:pre.text-xs.bg-white.p-3.rounded
       [:code
        ":value 1735084800000           ; Timestamp (ms)\n"
        ":value (js/Date. 2024 11 25)   ; Date object\n"
        ":value nil                     ; No selection"]]]]]])

(defn stateless-calendar-demo []
  [:div.demo-section
   [:h2.demo-title "🔧 Stateless Calendar Month (ty-calendar-month)"]
   [:p.text-sm.text-gray-600.mb-6 "Pure rendering component for advanced use cases - you control the display state."]

   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8.mb-8

    ;; Basic stateless
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "Fixed Month Display"]
      [:p.text-sm.text-gray-600.mb-4 "Shows December 2024 - no navigation, pure display."]
      [:ty-calendar-month {:display-year "2024"
                           :display-month "12"
                           :width "320px"
                           :on {:day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       "[:ty-calendar-month\n"
       " {:display-year \"2024\"  ; You control month\n"
       "  :display-month \"12\"\n"
       "  :width \"320px\"}]"]]]

    ;; Stateless with custom content and value
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "📊 With Custom Content & Value"]
      [:p.text-sm.text-gray-600.mb-4 "Hotel pricing with pre-highlighted date - you manage everything."]
      [:ty-calendar-month {:display-year "2024"
                           :display-month "12"
                           :width "420px"
                           :value "2024-12-28" ; Highlight specific date
                           :day-content-fn "hotelDayContent"
                           :on {:day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       "[:ty-calendar-month\n"
       " {:display-year \"2024\"\n"
       "  :display-month \"12\"\n"
       "  :value \"2024-12-28\"  ; Highlight date\n"
       "  :day-content-fn \"hotelDayContent\"}]"]]]]

   ;; When to use stateless
   [:div.p-4.bg-purple-50.rounded-lg.mb-6
    [:h4.font-semibold.mb-3 "🤔 When to Use Stateless Calendar:"]
    [:div.text-sm.space-y-2
     [:div [:strong "✅ Custom Navigation:"] " Building your own navigation controls"]
     [:div [:strong "✅ Complex State:"] " Multiple calendars with synchronized state"]
     [:div [:strong "✅ External Control:"] " Calendar state managed by parent component"]
     [:div [:strong "✅ Performance:"] " Need minimal overhead for specialized use cases"]
     [:div [:strong "✅ Framework Integration:"] " Integrating with React/Vue state management"]]]

   ;; Architecture comparison
   [:div.p-4.bg-yellow-50.rounded-lg
    [:h4.font-semibold.mb-3 "⚖️ Stateful vs Stateless:"]
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-4.text-sm
     [:div
      [:p.font-medium.mb-2.text-green-700 "ty-calendar (Stateful):"]
      [:ul.space-y-1.ml-4.list-disc
       [:li "Complete solution with navigation"]
       [:li "Internal state management"]
       [:li "Ready-to-use out of the box"]
       [:li "Perfect for simple use cases"]]]
     [:div
      [:p.font-medium.mb-2.text-purple-700 "ty-calendar-month (Stateless):"]
      [:ul.space-y-1.ml-4.list-disc
       [:li "Pure rendering engine"]
       [:li "You control all state"]
       [:li "Maximum flexibility"]
       [:li "Perfect for complex applications"]]]]]])

(defn render-function-examples []
  [:div.demo-section
   [:h2.demo-title "🎨 Custom Render Functions"]
   [:p.text-sm.text-gray-600.mb-6 "Powerful render functions let you create any day content using DOM elements."]

   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8.mb-8

    ;; Hotel pricing
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "🏨 Hotel Pricing"]
      [:p.text-sm.text-gray-600.mb-4 "Shows room pricing with weekend markup."]
      [:ty-calendar {:width "420px"
                     :value "2024-12-21" ; Pre-select weekend
                     :day-content-fn "hotelDayContent"
                     :on {:day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       ";; Hotel pricing render function\n"
       "(defn hotel-day-content [context]\n"
       "  (let [{:keys [day-in-month weekend]} (->clj context)\n"
       "        base-price 120\n"
       "        weekend-markup (if weekend 50 0)\n"
       "        price (+ base-price weekend-markup)]\n"
       "    ;; Create DOM elements...\n"
       "    container))\n\n"
       "[:ty-calendar {:day-content-fn \"hotelDayContent\"}]"]]]

    ;; Event dots
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "📅 Event Indicators"]
      [:p.text-sm.text-gray-600.mb-4 "Shows events with dot indicators on specific dates."]
      [:ty-calendar {:width "350px"
                     :value "2024-12-25" ; Pre-select Christmas
                     :day-content-fn "eventDayContent"
                     :on {:day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       ";; Event dots render function\n"
       "(defn event-day-content [context]\n"
       "  (let [{:keys [day-in-month]} (->clj context)\n"
       "        has-events (#{15 18 22 25} day-in-month)]\n"
       "    (if has-events\n"
       "      ;; Create DOM with event dot...\n"
       "      simple-day)))\n\n"
       "[:ty-calendar {:day-content-fn \"eventDayContent\"}]"]]]]

   ;; Render function benefits
   [:div.p-4.bg-green-50.rounded-lg.mb-6
    [:h4.font-semibold.mb-3 "💪 Render Function Power:"]
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-4.text-sm
     [:div.space-y-2
      [:div [:strong "🎨 Unlimited Design:"] " Create any visual content"]
      [:div [:strong "📊 Data Integration:"] " Show prices, events, availability"]
      [:div [:strong "🔧 DOM Control:"] " Full JavaScript DOM API access"]]
     [:div.space-y-2
      [:div [:strong "⚡ Performance:"] " Direct DOM manipulation"]
      [:div [:strong "🔒 Type Safety:"] " Component handles all return types"]
      [:div [:strong "🌐 Framework Agnostic:"] " Works with any JS framework"]]]]

   ;; Implementation tips
   [:div.p-4.bg-blue-50.rounded-lg
    [:h4.font-semibold.mb-3 "💡 Implementation Tips:"]
    [:div.text-sm.space-y-2
     [:div [:strong "Return DOM Elements:"] " Use `document.createElement()` for best performance"]
     [:div [:strong "Use cljs-bean:"] " Convert context with `(->clj context)` for easy access"]
     [:div [:strong "Handle Edge Cases:"] " Check `:other-month` for different styling"]
     [:div [:strong "Keep It Simple:"] " Start with basic content, enhance gradually"]
     [:pre.text-xs.bg-white.p-3.rounded.mt-3.overflow-x-auto
      [:code
       ";; Basic pattern:\n"
       "(defn my-render-fn [context]\n"
       "  (let [{:keys [day-in-month other-month]} (->clj context)\n"
       "        element (.createElement js/document \"div\")]\n"
       "    (set! (.-textContent element) (str day-in-month))\n"
       "    element))"]]]]])

(defn width-control-demo []
  [:div.demo-section
   [:h2.demo-title "📏 Width Control with Attributes"]
   [:p.text-sm.text-gray-600.mb-6 "Simple, clean width control using HTML attributes - works perfectly across frameworks."]

   [:div.grid.grid-cols-1.lg:grid-cols-3.gap-6.mb-8

    ;; Fixed width
    [:div.space-y-3
     [:div
      [:h4.font-medium.mb-1 "Fixed Width"]
      [:p.text-xs.text-gray-600.mb-3 "Set exact calendar width"]
      [:ty-calendar {:width "320px"
                     :value "2024-12-20"
                     :on {:day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-2.rounded
      [:code "{:width \"320px\"}"]]]

    ;; Min width with responsive
    [:div.space-y-3
     [:div
      [:h4.font-medium.mb-1 "Min Width"]
      [:p.text-xs.text-gray-600.mb-3 "Minimum width, grows with content"]
      [:ty-calendar {:min-width "300px"
                     :width "clamp(300px, 50vw, 450px)"
                     :value "2024-12-21"
                     :day-content-fn "hotelDayContent"
                     :on {:day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-2.rounded
      [:code "{:min-width \"300px\"\n :width \"clamp(300px, 50vw, 450px)\"}"]]]

    ;; Max width constraint
    [:div.space-y-3
     [:div
      [:h4.font-medium.mb-1 "Width Range"]
      [:p.text-xs.text-gray-600.mb-3 "Constrain between min and max"]
      [:ty-calendar {:min-width "280px"
                     :max-width "400px"
                     :width "100%"
                     :value "2024-12-22"
                     :day-content-fn "eventDayContent"
                     :on {:day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-2.rounded
      [:code "{:min-width \"280px\"\n :max-width \"400px\"\n :width \"100%\"}"]]]]

   ;; Benefits
   [:div.p-4.bg-green-50.rounded-lg.mb-6
    [:h4.font-semibold.mb-3 "✨ Attribute Benefits:"]
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-4.text-sm
     [:div.space-y-2
      [:div [:strong "🎯 Simple API:"] " Clean, discoverable attributes"]
      [:div [:strong "🔧 Framework Friendly:"] " Works in React, Vue, vanilla JS"]]
     [:div.space-y-2
      [:div [:strong "📚 Self-Documenting:"] " Visible in dev tools"]
      [:div [:strong "🎨 Full CSS Power:"] " Supports all CSS units and functions"]]]]

   ;; Supported formats
   [:div.p-4.bg-blue-50.rounded-lg
    [:h4.font-semibold.mb-3 "📖 Supported Formats:"]
    [:div.grid.grid-cols-1.lg:grid-cols-2.gap-4
     [:div
      [:p.font-medium.mb-2.text-sm "Simple Values:"]
      [:pre.text-xs.bg-white.p-3.rounded
       [:code
        "width=\"400\"        ; → 400px (auto-convert)\n"
        "width=\"400px\"      ; → 400px (preserve)\n"
        "width=\"20rem\"      ; → 20rem (preserve)\n"
        "width=\"25em\"       ; → 25em (preserve)"]]]
     [:div
      [:p.font-medium.mb-2.text-sm "CSS Functions:"]
      [:pre.text-xs.bg-white.p-3.rounded
       [:code
        "width=\"100%\"                    ; → 100% (responsive)\n"
        "width=\"min(100%, 500px)\"       ; → CSS min()\n"
        "width=\"clamp(300px, 50vw, 600px)\"; → CSS clamp()\n"
        "width=\"calc(100vw - 300px)\"    ; → CSS calc()"]]]]]])

(defn event-handling-demo []
  [:div.demo-section
   [:h2.demo-title "🎯 Event Handling & Selection"]
   [:p.text-sm.text-gray-600.mb-6 "Rich event system with complete day context and selection state."]

   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8.mb-8

    ;; Event details
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "Click for Event Details"]
      [:p.text-sm.text-gray-600.mb-4 "Click any day to see complete event context."]
      [:ty-calendar {:width "350px"
                     :value "2024-12-15" ; Pre-select a date
                     :on {:day-click handle-day-click}}]]
     [:div.text-xs.p-3.bg-gray-50.rounded.font-mono
      (if-let [clicked (:last-clicked @state/state)]
        [:div
         [:div "📅 Date: " (:formatted clicked)]
         [:div "🆔 Value: " (:value clicked)]
         [:div "📊 Year: " (:year clicked) ", Month: " (:month clicked) ", Day: " (:day clicked)]
         [:div "✨ Today?: " (if (:is-today clicked) "Yes" "No")]
         [:div "🌅 Weekend?: " (if (:is-weekend clicked) "Yes" "No")]]
        "Click a day to see event details...")]]

    ;; With custom content
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "Events + Custom Content"]
      [:p.text-sm.text-gray-600.mb-4 "Rich events work with any custom render functions."]
      [:ty-calendar {:width "420px"
                     :value "2024-12-22" ; Pre-select weekend 
                     :day-content-fn "hotelDayContent"
                     :on {:day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       ";; Event handler receives full context\n"
       "(defn handle-day-click [event]\n"
       "  (let [detail (.-detail event)\n"
       "        date-parts (.-dateParts detail)]\n"
       "    {:year (.-year date-parts)\n"
       "     :value (.-value detail)\n"
       "     :isToday (.-isToday detail)\n"
       "     :isWeekend (.-isWeekend detail)}))"]]]]

   ;; Event structure
   [:div.p-4.bg-blue-50.rounded-lg.mb-6
    [:h4.font-semibold.mb-3 "📋 Event Detail Structure:"]
    [:pre.text-xs.bg-white.p-3.rounded.overflow-x-auto.font-mono
     [:code
      "{\n"
      "  dayContext: {...},           // Full timing library context\n"
      "  value: 1735084800000,        // Timestamp (ms)\n"
      "  dateParts: {\n"
      "    year: 2024,\n"
      "    month: 12,\n"
      "    day: 25\n"
      "  },\n"
      "  isHoliday: false,           // Holiday detection\n"
      "  isToday: false,             // Convenience flags\n"
      "  isWeekend: false,\n"
      "  isOtherMonth: false\n"
      "}"]]]

   ;; Usage patterns
   [:div.p-4.bg-green-50.rounded-lg
    [:h4.font-semibold.mb-3 "🔥 Common Event Patterns:"]
    [:div.grid.grid-cols-1.lg:grid-cols-2.gap-4.text-sm
     [:div
      [:p.font-medium.mb-2 "React/ClojureScript:"]
      [:pre.text-xs.bg-white.p-3.rounded
       [:code
        ";; Update state on selection\n"
        "[:ty-calendar\n"
        " {:value @selected-date\n"
        "  :on {:day-click\n"
        "       #(reset! selected-date\n"
        "               (.-value (.-detail %)))}}]"]]]
     [:div
      [:p.font-medium.mb-2 "Validation & Business Logic:"]
      [:pre.text-xs.bg-white.p-3.rounded
       [:code
        ";; Validate selection\n"
        "(defn handle-selection [event]\n"
        "  (let [detail (.-detail event)]\n"
        "    (when-not (.-isWeekend detail)\n"
        "      ;; Only allow weekday selection\n"
        "      (update-booking! (.-value detail)))))"]]]]]])

(defn view []
  [:div.p-6
   [:div.mb-8
    [:h1.text-3xl.font-bold.text-gray-900.dark:text-white "Calendar Components"]
    [:p.text-lg.text-gray-600.dark:text-gray-400.mt-2
     "Complete calendar system with stateful navigation and stateless rendering core."]]

   ;; Main demos
   (stateful-calendar-demo)
   (stateless-calendar-demo)
   (render-function-examples)
   (width-control-demo)
   (event-handling-demo)

   ;; Quick start guide
   [:div.demo-section
    [:h2.demo-title "🚀 Quick Start Guide"]
    [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8

     [:div.space-y-4
      [:h3.demo-subtitle "For Most Use Cases: ty-calendar"]
      [:div.text-sm.space-y-3
       [:div "✅ Complete solution with navigation"]
       [:div "✅ Internal selection state management"]
       [:div "✅ Works great out of the box"]
       [:div "✅ Perfect for forms, date pickers, simple UIs"]]
      [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
       [:code
        "<!-- HTML -->\n"
        "<ty-calendar value=\"2024-12-25\" width=\"350px\"></ty-calendar>\n\n"
        ";; ClojureScript\n"
        "[:ty-calendar {:value \"2024-12-25\" :width \"350px\"}]\n\n"
        "// React JSX\n"
        "<ty-calendar value=\"2024-12-25\" width=\"350px\" />"]]]

     [:div.space-y-4
      [:h3.demo-subtitle "For Advanced Use Cases: ty-calendar-month"]
      [:div.text-sm.space-y-3
       [:div "🔧 You control all state and navigation"]
       [:div "🎨 Maximum customization flexibility"]
       [:div "⚡ Performance optimized rendering"]
       [:div "🏗️ Perfect for complex applications"]]
      [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
       [:code
        "<!-- HTML -->\n"
        "<ty-calendar-month\n"
        "  display-year=\"2024\"\n"
        "  display-month=\"12\"\n"
        "  value=\"2024-12-25\"></ty-calendar-month>\n\n"
        ";; ClojureScript\n"
        "[:ty-calendar-month\n"
        " {:display-year \"2024\"\n"
        "  :display-month \"12\" \n"
        "  :value \"2024-12-25\"}]"]]]]]])
