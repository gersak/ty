(ns ty.site.docs.calendar-month
  "Documentation for ty-calendar-month component"
  (:require
    [cljs-bean.core :refer [->clj]]
    [clojure.string :as str]
    [ty.i18n :as i18n]
    [ty.site.docs.common
     :refer [code-block
             attribute-table
             event-table]])
  (:require-macros [ty.css :refer [defstyles]]))

;; Custom styles for documentation examples  
(defstyles availability-styles "ty/site/docs/calendar-month-availability.css")
(defstyles custom-classes-styles "ty/site/docs/calendar-month-custom-classes.css")

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Title and Description
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-calendar-month"]
    [:p.text-lg.ty-text- "A stateless calendar month renderer for building custom calendar interfaces with full control over day rendering and behavior."]]

   ;; Key Features Highlight
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-xl.font-semibold.ty-text.mb-4 "✨ Key Features"]
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-primary.mt-0.5 {:name "settings"
                                        :size "sm"}]
      [:div
       [:p.font-medium.ty-text "Property-Controlled"]
       [:p.text-sm.ty-text- "Direct property access with automatic re-rendering"]]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-primary.mt-0.5 {:name "layers"
                                        :size "sm"}]
      [:div
       [:p.font-medium.ty-text "Stateless Design"]
       [:p.text-sm.ty-text- "No internal state, purely controlled by parent"]]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-primary.mt-0.5 {:name "globe"
                                        :size "sm"}]
      [:div
       [:p.font-medium.ty-text "Framework Agnostic"]
       [:p.text-sm.ty-text- "Works with React, Vue, HTMX, vanilla JS"]]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-primary.mt-0.5 {:name "palette"
                                        :size "sm"}]
      [:div
       [:p.font-medium.ty-text "Fully Customizable"]
       [:p.text-sm.ty-text- "Custom day content and CSS classes"]]]]]

   ;; API Reference Card
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text.mb-6 "API Reference"]

    ;; Properties
    [:div.mb-8
     [:h3.text-lg.font-medium.ty-text+.mb-4 "Properties"]
     [:p.text-sm.ty-text-.mb-4 "All properties are directly accessible as JavaScript properties. Changes trigger automatic re-rendering."]
     (attribute-table
       [{:name "displayMonth"
         :type "number"
         :default "Current month"
         :description "Month to display (1-12)"}
        {:name "displayYear"
         :type "number"
         :default "Current year"
         :description "Year to display"}
        {:name "value"
         :type "number"
         :default "null"
         :description "Selected date as timestamp (milliseconds)"}
        {:name "locale"
         :type "string"
         :default "Context locale or \"en-US\""
         :description "Locale for internationalization"}
        {:name "width"
         :type "string"
         :default "\"fit-content\""
         :description "Component width (any CSS value)"}
        {:name "minWidth"
         :type "string"
         :default "\"280px\""
         :description "Minimum width constraint"}
        {:name "maxWidth"
         :type "string"
         :default "\"none\""
         :description "Maximum width constraint"}
        {:name "dayContentFn"
         :type "function"
         :default "Shows day number"
         :description "Custom day content renderer function"}
        {:name "dayClassesFn"
         :type "function"
         :default "Basic classes"
         :description "Custom CSS classes function"}
        {:name "customCSS"
         :type "CSSStyleSheet"
         :default "null"
         :description "Custom stylesheet injection"}])]

    ;; Events
    [:div.mb-8
     [:h3.text-lg.font-medium.ty-text+.mb-4 "Events"]
     (event-table
       [{:name "day-click"
         :payload "DayContext object"
         :when-fired "When a day cell is clicked"}])]

    ;; Day Context Structure
    [:div
     [:h3.text-lg.font-medium.ty-text+.mb-4 "Day Context Object"]
     [:p.text-sm.ty-text-.mb-4 "The day-click event detail contains a comprehensive day context object:"]
     [:div.overflow-x-auto
      [:table.w-full
       [:thead
        [:tr.border-b.ty-border
         [:th.text-left.px-4.py-2.ty-text+ "Property"]
         [:th.text-left.px-4.py-2.ty-text+ "Type"]
         [:th.text-left.px-4.py-2.ty-text+ "Description"]]]
       [:tbody
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "date"]
         [:td.px-4.py-2.ty-text-.text-sm "Date"]
         [:td.px-4.py-2.ty-text-.text-sm "JavaScript Date object"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "year"]
         [:td.px-4.py-2.ty-text-.text-sm "number"]
         [:td.px-4.py-2.ty-text-.text-sm "Full year (e.g., 2024)"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "month"]
         [:td.px-4.py-2.ty-text-.text-sm "number"]
         [:td.px-4.py-2.ty-text-.text-sm "Month 1-12"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "dayInMonth"]
         [:td.px-4.py-2.ty-text-.text-sm "number"]
         [:td.px-4.py-2.ty-text-.text-sm "Day of month 1-31"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "timestamp"]
         [:td.px-4.py-2.ty-text-.text-sm "number"]
         [:td.px-4.py-2.ty-text-.text-sm "Date as timestamp"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "today"]
         [:td.px-4.py-2.ty-text-.text-sm "boolean"]
         [:td.px-4.py-2.ty-text-.text-sm "Is this today?"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "weekend"]
         [:td.px-4.py-2.ty-text-.text-sm "boolean"]
         [:td.px-4.py-2.ty-text-.text-sm "Is this a weekend day?"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "otherMonth"]
         [:td.px-4.py-2.ty-text-.text-sm "boolean"]
         [:td.px-4.py-2.ty-text-.text-sm "Is this from adjacent month?"]]]]]]]

   ;; Basic Usage
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Basic Usage"]
    (code-block "<ty-calendar-month></ty-calendar-month>")
    [:div.mt-4
     [:ty-calendar-month]]]

   ;; Examples Section
   [:h2.text-2xl.font-semibold.ty-text.mb-6 "Examples"]

   [:div.space-y-8
    ;; Basic Month Display
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Basic Month Display"]
     [:p.text-sm.ty-text-.mb-4 "A simple calendar month with default settings."]
     [:div.flex.flex-col.lg:flex-row.gap-6.mb-4
      [:div.flex-1
       [:ty-calendar-month {:id "basic-calendar"}]]
      [:div.lg:w-80
       [:h4.font-medium.ty-text.mb-2 "Features"]
       [:ul.space-y-1.text-sm.ty-text-
        [:li "• Shows current month by default"]
        [:li "• Localized weekday headers"]
        [:li "• Today highlighting"]
        [:li "• Weekend styling"]
        [:li "• Adjacent month days shown muted"]]]]
     (code-block "<ty-calendar-month></ty-calendar-month>")]

    ;; Property Control
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Property Control"]
     [:p.text-sm.ty-text-.mb-4 "Direct property access for specific month/year display and date selection."]
     [:div.flex.flex-col.lg:flex-row.gap-6.mb-4
      [:div.flex-1
       [:ty-calendar-month {:id "controlled-calendar"
                            :replicant/on-mount (fn [{^js el :replicant/node}]
                                                  (set! (.-displayMonth el) 12)
                                                  (set! (.-displayYear el) 2024)
                                                  (set! (.-value el) (.getTime (js/Date. 2024 11 25))))}]]
      [:div.lg:w-80
       [:h4.font-medium.ty-text.mb-2 "JavaScript Control"]
       [:pre.text-xs.ty-elevated.p-3.rounded.overflow-x-auto
        [:code
         "const calendar = document.getElementById('calendar');\n"
         "calendar.displayMonth = 12;  // December\n"
         "calendar.displayYear = 2024;\n"
         "calendar.value = new Date(2024, 11, 25).getTime();"]]]]
     (code-block "<!-- HTML -->\n<ty-calendar-month id=\"calendar\"></ty-calendar-month>\n\n<!-- JavaScript -->\n<script>\nconst calendar = document.getElementById('calendar');\ncalendar.displayMonth = 12;  // December\ncalendar.displayYear = 2024;\ncalendar.value = new Date(2024, 11, 25).getTime();\n</script>")]

    ;; Event Handling
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Event Handling"]
     [:p.text-sm.ty-text-.mb-4 "Handle day clicks to build interactive calendar interfaces."]
     [:div.flex.flex-col.lg:flex-row.gap-6.mb-4
      [:div.flex-1
       [:ty-calendar-month {:id "event-calendar"
                            :on {:day-click (fn [^js e]
                                              (let [{:keys [year month day value]} (->clj (.-detail e))]
                                                (js/alert (str "Clicked: " (i18n/translate (js/Date. value) "full")))))}}]]
      [:div.lg:w-80
       [:h4.font-medium.ty-text.mb-2 "Click any day"]
       [:p.text-sm.ty-text- "The day-click event provides complete day information including date, day number, and context flags."]
       [:div.mt-3.text-xs.ty-elevated.p-3.rounded
        [:strong.ty-text "Event Detail:"]
        [:pre.mt-1.ty-text-
         "{\n"
         "  date: Date object\n"
         "  year: 2024\n"
         "  month: 12\n"
         "  dayInMonth: 15\n"
         "  value: 1734220800000\n"
         "  today: false\n"
         "  weekend: true\n"
         "  otherMonth: false\n"
         "}"]]]]
     (code-block "<ty-calendar-month id=\"calendar\"></ty-calendar-month>\n\n<script>\ndocument.getElementById('calendar').addEventListener('day-click', function(event) {\n  const { date, dayInMonth, month, year, today, weekend } = event.detail;\n  console.log(`Clicked: ${year}-${month}-${dayInMonth}`);\n  \n  // Update selection\n  this.value = event.detail.timestamp;\n});\n</script>")]

    ;; Localization
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Localization"]
     [:p.text-sm.ty-text-.mb-4 "Automatic localized weekday headers for international applications."]
     [:div.grid.grid-cols-1.lg:grid-cols-2.xl:grid-cols-4.gap-4.mb-4
      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "French (fr-FR)"]
       [:ty-calendar-month {:id "french-calendar"
                            :locale "fr-FR"}]]
      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "German (de-DE)"]
       [:ty-calendar-month {:id "german-calendar"
                            :locale "de-DE"}]]
      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Japanese (ja-JP)"]
       [:ty-calendar-month {:id "japanese-calendar"
                            :locale "ja-JP"}]]
      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Spanish (es-ES)"]
       [:ty-calendar-month {:id "spanish-calendar"
                            :locale "es-ES"}]]]
     (code-block "<!-- HTML -->
<ty-calendar-month id=\"french-calendar\" locale=\"fr-FR\"></ty-calendar-month>
<ty-calendar-month id=\"german-calendar\" locale=\"de-DE\"></ty-calendar-month>
<ty-calendar-month id=\"japanese-calendar\" locale=\"ja-JP\"></ty-calendar-month>
<ty-calendar-month id=\"spanish-calendar\" locale=\"es-ES\"></ty-calendar-month>
")]

    ;; Custom Day Content
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Custom Day Content"]
     [:p.text-sm.ty-text-.mb-4 "Use dayContentFn to render custom content in day cells - perfect for availability calendars, event indicators, or pricing displays."]
     [:div.flex.flex-col.lg:flex-row.gap-6.mb-4
      [:div.flex-1
       [:ty-calendar-month {:id "custom-content-calendar"
                            :replicant/on-mount (fn [{^js el :replicant/node}]
                                                  ;; Apply custom CSS for availability styling
                                                  (set! (.-customCSS el) availability-styles)
                                                  ;; Custom day content function
                                                  (set! (.-dayContentFn el)
                                                        (fn [^js context]
                                                          (let [{:keys [day-in-month other-month]} (->clj context)
                                                                available? (even? day-in-month)]
                                                            (if other-month
                                                              (let [container (.createElement js/document "div")]
                                                                (set! (.-className container) "availability-day other-month")
                                                                (let [day-span (.createElement js/document "span")]
                                                                  (set! (.-className day-span) "day-number")
                                                                  (set! (.-textContent day-span) (str day-in-month))
                                                                  (.appendChild container day-span))
                                                                container)
                                                              (let [container (.createElement js/document "div")
                                                                    day-span (.createElement js/document "span")
                                                                    indicator (.createElement js/document "div")]
                                                                (set! (.-className container) (str "availability-day " (if available? "available" "booked")))
                                                                (set! (.-className day-span) "day-number")
                                                                (set! (.-textContent day-span) (str day-in-month))
                                                                (set! (.-className indicator) "status-indicator")
                                                                (.appendChild container day-span)
                                                                (.appendChild container indicator)
                                                                container))))))}]]
      [:div.lg:w-80
       [:h4.font-medium.ty-text.mb-2 "Availability Display"]
       [:p.text-sm.ty-text- "Green dot = Available, Red dot = Booked"]
       [:div.mt-3.text-xs.ty-elevated.p-3.rounded
        [:strong.ty-text "dayContentFn Example:"]
        [:pre.mt-1.ty-text-.text-xs
         "function(dayContext) {\n"
         "  const available = isAvailable(dayContext.date);\n"
         "  return createDayWithIndicator(\n"
         "    dayContext.dayInMonth,\n"
         "    available\n"
         "  );\n"
         "}"]]]]
     (code-block "<!-- Create custom styles for shadow DOM -->\n<script>\n// Create custom CSS stylesheet\nconst availabilityStyles = new CSSStyleSheet();\navailabilityStyles.replaceSync(`\n  .availability-day {\n    display: flex;\n    flex-direction: column;\n    align-items: center;\n    justify-content: center;\n    width: 100%;\n    height: 100%;\n    gap: 0.125rem;\n  }\n  \n  .availability-day .day-number {\n    font-size: 0.75rem;\n    font-weight: 500;\n  }\n  \n  .availability-day .status-indicator {\n    width: 0.375rem;\n    height: 0.375rem;\n    border-radius: 50%;\n  }\n  \n  .availability-day.available .status-indicator {\n    background-color: var(--ty-color-success);\n  }\n  \n  .availability-day.booked .status-indicator {\n    background-color: var(--ty-color-danger);\n  }\n  \n  .availability-day.other-month {\n    opacity: 0.4;\n  }\n`);\n\nconst calendar = document.getElementById('calendar');\n\n// Apply custom CSS to shadow DOM\ncalendar.customCSS = availabilityStyles;\n\n// Custom day content function\ncalendar.dayContentFn = function(dayContext) {\n  const container = document.createElement('div');\n  const dayNum = document.createElement('span');\n  \n  dayNum.className = 'day-number';\n  dayNum.textContent = dayContext.dayInMonth;\n  \n  if (dayContext.otherMonth) {\n    container.className = 'availability-day other-month';\n    container.appendChild(dayNum);\n  } else {\n    const available = isAvailable(dayContext.date);\n    const indicator = document.createElement('div');\n    \n    container.className = `availability-day ${available ? 'available' : 'booked'}`;\n    indicator.className = 'status-indicator';\n    \n    container.appendChild(dayNum);\n    container.appendChild(indicator);\n  }\n  \n  return container;\n};\n\nfunction isAvailable(date) {\n  // Your availability logic here\n  return date.getDate() % 2 === 0;\n}\n</script>")]

    ;; Custom CSS Classes
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Custom CSS Classes"]
     [:p.text-sm.ty-text-.mb-4 "Use dayClassesFn to add conditional CSS classes to day cells based on your business logic."]
     [:div.flex.flex-col.lg:flex-row.gap-6.mb-4
      [:div.flex-1
       [:ty-calendar-month {:id "custom-classes-calendar"
                            :replicant/on-mount (fn [{^js el :replicant/node}]
                                                  ;; Apply custom CSS for special day styling
                                                  (set! (.-customCSS el) custom-classes-styles)
                                                  ;; Set custom class function
                                                  (set! (.-dayClassesFn el)
                                                        (fn [^js context]
                                                          (let [{:keys [day-in-month today weekend other-month]} (->clj context)
                                                                classes (cond-> []
                                                                          today (conj "today")
                                                                          weekend (conj "weekend")
                                                                          other-month (conj "other-month")
                                                                          (= 0 (mod day-in-month 7)) (conj "special-day"))]
                                                            classes))))}]]
      [:div.lg:w-80
       [:h4.font-medium.ty-text.mb-2 "Business Logic Styling"]
       [:p.text-sm.ty-text- "Days divisible by 7 get special highlighting"]
       [:div.mt-3.text-xs.ty-elevated.p-3.rounded
        [:strong.ty-text "dayClassesFn Example:"]
        [:pre.mt-1.ty-text-.text-xs
         "function(dayContext) {\n"
         "  const classes = [];\n"
         "  \n"
         "  if (dayContext.today) classes.push('today');\n"
         "  if (dayContext.weekend) classes.push('weekend');\n"
         "  if (isSpecialDay(dayContext.date)) \n"
         "    classes.push('special-day');\n"
         "  \n"
         "  return classes;\n"
         "}"]]]]
     (code-block "<!-- Custom CSS Classes with Shadow DOM -->\n<script>\n// Create custom styles for Shadow DOM\nconst customClassesStyles = new CSSStyleSheet();\ncustomClassesStyles.replaceSync(`\n  .special-day {\n    background-color: var(--ty-bg-primary-soft) !important;\n    color: var(--ty-color-primary-strong) !important;\n    border-radius: 0.25rem;\n    font-weight: 600;\n  }\n  \n  .special-day:hover {\n    background-color: var(--ty-bg-primary-mild) !important;\n    transform: scale(1.02);\n    transition: all 0.1s ease;\n  }\n  \n  .special-day.today {\n    border: 2px solid var(--ty-color-primary);\n    background-color: var(--ty-bg-primary) !important;\n    color: white !important;\n  }\n`);\n\nconst calendar = document.getElementById('calendar');\n\n// Apply custom CSS to shadow DOM\ncalendar.customCSS = customClassesStyles;\n\n// Custom day classes function  \ncalendar.dayClassesFn = function(dayContext) {\n  const classes = [];\n  \n  if (dayContext.today) classes.push('today');\n  if (dayContext.weekend) classes.push('weekend');\n  if (dayContext.otherMonth) classes.push('other-month');\n  \n  // Custom business logic - days divisible by 7\n  if (dayContext.dayInMonth % 7 === 0) {\n    classes.push('special-day');\n  }\n  \n  return classes;\n};\n</script>")]]])
