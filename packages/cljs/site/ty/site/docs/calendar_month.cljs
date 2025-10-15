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

(defn ensure-display-values!
  "IMPORTANT: Initialize displayMonth and displayYear before setting custom functions.
   Setting dayContentFn or dayClassesFn triggers render, which needs valid integers."
  [^js el]
  (when-not (.-displayMonth el)
    (set! (.-displayMonth el) (int (inc (.getMonth (js/Date.))))))
  (when-not (.-displayYear el)
    (set! (.-displayYear el) (int (.getFullYear (js/Date.))))))

(defn header-section
  "Renders the component title and description."
  []
  [:div.mb-8
   [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-calendar-month"]
   [:p.text-lg.ty-text- "The foundation component for calendar interfaces. Renders a single month with complete customization control."]
   [:p.text-sm.ty-text-.mt-2 "Powers ty-calendar and ty-datepicker components."]])

(defn api-reference-section
  "Renders the API reference documentation."
  []
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
      {:name "size"
       :type "string"
       :default "\"md\""
       :description "Calendar size variant: 'sm', 'md', or 'lg'"}
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
       :description "Custom day content renderer function (returns HTMLElement or string)"}
      {:name "customCSS"
       :type "CSSStyleSheet"
       :default "null"
       :description "Custom stylesheet injection for shadow DOM"}])]

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
        [:td.px-4.py-2.ty-text.font-mono.text-sm "value"]
        [:td.px-4.py-2.ty-text-.text-sm "number"]
        [:td.px-4.py-2.ty-text-.text-sm "UTC timestamp at midnight UTC"]]
       [:tr.border-b.ty-border-
        [:td.px-4.py-2.ty-text.font-mono.text-sm "localValue"]
        [:td.px-4.py-2.ty-text-.text-sm "number"]
        [:td.px-4.py-2.ty-text-.text-sm "Local timestamp at midnight local time"]]
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
        [:td.px-4.py-2.ty-text-.text-sm "Is this from adjacent month?"]]
       [:tr.border-b.ty-border-
        [:td.px-4.py-2.ty-text.font-mono.text-sm "prevMonth"]
        [:td.px-4.py-2.ty-text-.text-sm "boolean"]
        [:td.px-4.py-2.ty-text-.text-sm "Is this from previous month?"]]
       [:tr.border-b.ty-border-
        [:td.px-4.py-2.ty-text.font-mono.text-sm "nextMonth"]
        [:td.px-4.py-2.ty-text-.text-sm "boolean"]
        [:td.px-4.py-2.ty-text-.text-sm "Is this from next month?"]]
       [:tr.border-b.ty-border-
        [:td.px-4.py-2.ty-text.font-mono.text-sm "isSelected"]
        [:td.px-4.py-2.ty-text-.text-sm "boolean"]
        [:td.px-4.py-2.ty-text-.text-sm "Is this day selected?"]]
       [:tr.border-b.ty-border-
        [:td.px-4.py-2.ty-text.font-mono.text-sm "holiday"]
        [:td.px-4.py-2.ty-text-.text-sm "boolean"]
        [:td.px-4.py-2.ty-text-.text-sm "Is this a holiday? (optional)"]]]]]]])

(defn basic-examples-section
  "Renders basic usage examples: basic display, controlled months, and event handling."
  []
  [:<>
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

   ;; Different Months
   [:div.ty-content.rounded-lg.p-6
    [:h3.text-lg.font-medium.ty-text.mb-4 "Different Months"]
    [:p.text-sm.ty-text-.mb-4 "Display any month and year with simple property settings."]
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
    (code-block "<ty-calendar-month id=\"calendar\"></ty-calendar-month>\n\n<script>\ndocument.getElementById('calendar').addEventListener('day-click', function(event) {\n  const { date, dayInMonth, month, year, today, weekend } = event.detail;\n  console.log(`Clicked: ${year}-${month}-${dayInMonth}`);\n  \n  // Update selection\n  this.value = event.detail.timestamp;\n});\n</script>")]])

(defn localization-section
  "Renders localization examples with multiple locales."
  []
  [:div.ty-content.rounded-lg.p-6
   [:h3.text-lg.font-medium.ty-text.mb-4 "Localization"]
   [:p.text-sm.ty-text-.mb-4 "Automatic localized weekday headers for international applications."]
   [:div.grid.grid-cols-1.lg:grid-cols-2.xl:grid-cols-2.gap-4.mb-4
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
   (code-block "<!-- Simple attribute approach -->
<ty-calendar-month locale=\"fr-FR\"></ty-calendar-month>
<ty-calendar-month locale=\"de-DE\"></ty-calendar-month>
<ty-calendar-month locale=\"ja-JP\"></ty-calendar-month>
<ty-calendar-month locale=\"es-ES\"></ty-calendar-month>")])

(defn advanced-customization-section
  "Renders advanced customization examples with custom day content."
  []
  [:<>
   ;; Section Break
   [:div.border-t.ty-border.pt-8.mt-12.mb-8
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Advanced Customization"]
    [:p.text-sm.ty-text-.mb-6
     "For complex use cases requiring custom day content, styling, or business logic."]]

   ;; Custom Day Content
   [:div.ty-content.rounded-lg.p-6
    [:h3.text-lg.font-medium.ty-text.mb-4 "Custom Day Content"]
    [:p.text-sm.ty-text-.mb-4 "Use dayContentFn to render custom content in day cells - perfect for availability calendars, event indicators, or pricing displays."]
    [:div.flex.flex-col.lg:flex-row.gap-6.mb-4
     [:div.flex-1
      [:ty-calendar-month
       {:id "custom-content-calendar"
        :replicant/on-mount (fn [{^js el :replicant/node}]
                              ;; Apply custom CSS for availability styling
                              (set! (.-customCSS el) availability-styles)
                              ;; Custom day content function
                              (set! (.-dayContentFn el)
                                    (fn [^js context]
                                      (let [{day-in-month :dayInMonth
                                             other-month :otherMonth} (->clj context)
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
    (code-block "<!-- Create custom styles for shadow DOM -->\n<script>\n// Create custom CSS stylesheet\nconst availabilityStyles = new CSSStyleSheet();\navailabilityStyles.replaceSync(`\n  .availability-day {\n    display: flex;\n    flex-direction: column;\n    align-items: center;\n    justify-content: center;\n    width: 100%;\n    height: 100%;\n    gap: 0.125rem;\n  }\n  \n  .availability-day .day-number {\n    font-size: 0.75rem;\n    font-weight: 500;\n  }\n  \n  .availability-day .status-indicator {\n    width: 0.375rem;\n    height: 0.375rem;\n    border-radius: 50%;\n  }\n  \n  .availability-day.available .status-indicator {\n    background-color: var(--ty-color-success);\n  }\n  \n  .availability-day.booked .status-indicator {\n    background-color: var(--ty-color-danger);\n  }\n  \n  .availability-day.other-month {\n    opacity: 0.4;\n  }\n`);\n\nconst calendar = document.getElementById('calendar');\n\n// Apply custom CSS to shadow DOM\ncalendar.customCSS = availabilityStyles;\n\n// Custom day content function\ncalendar.dayContentFn = function(dayContext) {\n  const container = document.createElement('div');\n  const dayNum = document.createElement('span');\n  \n  dayNum.className = 'day-number';\n  dayNum.textContent = dayContext.dayInMonth;\n  \n  if (dayContext.otherMonth) {\n    container.className = 'availability-day other-month';\n    container.appendChild(dayNum);\n  } else {\n    const available = isAvailable(dayContext.date);\n    const indicator = document.createElement('div');\n    \n    container.className = `availability-day ${available ? 'available' : 'booked'}`;\n    indicator.className = 'status-indicator';\n    \n    container.appendChild(dayNum);\n    container.appendChild(indicator);\n  }\n  \n  return container;\n};\n\nfunction isAvailable(date) {\n  // Your availability logic here\n  return date.getDate() % 2 === 0;\n}\n</script>")]])

(defn view
  "Main documentation view for ty-calendar-month component."
  []
  [:div.max-w-4xl.mx-auto.p-6
   (header-section)
   (api-reference-section)
   [:div.space-y-8
    (basic-examples-section)
    (localization-section)
    (advanced-customization-section)]])


