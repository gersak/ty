(ns ty.site.docs.calendar
  "Documentation for ty-calendar component"
  (:require
   [cljs-bean.core :refer [->clj]]
   [ty.site.docs.common
    :refer [code-block
            attribute-table
            event-table
            docs-page]])
  (:require-macros [ty.css :refer [defstyles]]))

;; Custom styles for hotel pricing example
(defstyles hotel-pricing-styles "ty/site/docs/calendar-hotel-pricing.css")

;; =============================================================================
;; Helper Data
;; =============================================================================

(def pricing-data
  "Hotel pricing data for December 2024"
  {1 210,
   2 150,
   3 150,
   4 150,
   5 150,
   6 150,
   7 210,
   8 210,
   9 150,
   10 150,
   11 150,
   12 150,
   13 150,
   14 210,
   15 210,
   16 150,
   17 150,
   18 150,
   19 150,
   20 150,
   21 210,
   22 210,
   23 150,
   24 150,
   25 150,
   26 150,
   27 150,
   28 210,
   29 210,
   30 150,
   31 150})

;; =============================================================================
;; Section Components
;; =============================================================================

(defn header-section []
  [:div.mb-8
   [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-calendar"]
   [:p.text-lg.ty-text- "A complete, stateful calendar component with built-in navigation and date selection."]
   [:p.text-sm.ty-text-.mt-2 "Orchestrates ty-calendar-navigation and ty-calendar-month components with full customization pass-through."]])

(defn api-reference-section []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-semibold.ty-text.mb-6 "API Reference"]

   ;; Properties
   [:div.mb-8
    [:h3.text-lg.font-medium.ty-text+.mb-4 "Properties / Attributes"]
    [:p.text-sm.ty-text-.mb-4 "Set via attributes or JavaScript properties. The calendar maintains internal state for navigation."]
    (attribute-table
     [{:name "year"
       :type "number"
       :default "Current year"
       :description "Initial selected year (via attribute)"}
      {:name "month"
       :type "number"
       :default "Current month"
       :description "Initial selected month 1-12 (via attribute)"}
      {:name "day"
       :type "number"
       :default "null"
       :description "Initial selected day 1-31 (via attribute)"}
      {:name "value"
       :type "string"
       :default "empty string"
       :description "Get/set selected date as ISO string (YYYY-MM-DD). Alternative to year/month/day."}
      {:name "name"
       :type "string"
       :default "null"
       :description "Form field name for submission"}
      {:name "locale"
       :type "string"
       :default "Context locale or \"en-US\""
       :description "Locale for internationalization"}
      {:name "show-navigation"
       :type "boolean"
       :default "true"
       :description "Show/hide navigation controls (attribute: show-navigation=\"false\")"}
      {:name "stateless"
       :type "boolean"
       :default "false"
       :description "Stateless mode - parent controls all state. Doesn't update attributes or form value."}
      {:name "size"
       :type "string"
       :default "md"
       :description "Predefined size: 'sm', 'md', or 'lg'"}
      {:name "width"
       :type "string"
       :default "null"
       :description "Custom width (mutually exclusive with size)"}
      {:name "dayContentFn"
       :type "function"
       :default "Shows day number"
       :description "Custom day content renderer (passed to ty-calendar-month)"}
      {:name "customCSS"
       :type "CSSStyleSheet"
       :default "null"
       :description "Custom stylesheet (passed to ty-calendar-month)"}])]

   ;; Events
   [:div.mb-8
    [:h3.text-lg.font-medium.ty-text+.mb-4 "Events"]
    (event-table
     [{:name "change"
       :payload "{year, month, day, action: 'select', source: 'day-click', dayContext}"
       :when-fired "When a date is selected by clicking a day"}
      {:name "navigate"
       :payload "{month, year, action: 'navigate', source: 'navigation'}"
       :when-fired "When navigating between months/years using navigation controls"}])]

   ;; Pass-through Behavior
   [:div
    [:h3.text-lg.font-medium.ty-text+.mb-4 "🔄 Customization Pass-through"]
    [:p.text-sm.ty-text-.mb-4 "ty-calendar acts as an orchestrator, passing these customization properties directly to its internal ty-calendar-month component:"]
    [:div.ty-bg-info-.rounded-lg.p-4
     [:ul.space-y-2.text-sm.ty-text
      [:li.flex.items-start.gap-2
       [:ty-icon.ty-text.mt-0.5 {:name "arrow-right"
                                      :size "xs"}]
       [:span [:code.ty-elevated.px-1 "dayContentFn"] " - Your custom day rendering function"]]
      [:li.flex.items-start.gap-2
       [:ty-icon.ty-text.mt-0.5 {:name "arrow-right"
                                      :size "xs"}]
       [:span [:code.ty-elevated.px-1 "customCSS"] " - Your custom stylesheet for shadow DOM"]]]]]])

(defn stateful-vs-stateless-section []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2.text-xl.font-semibold.ty-text.mb-4 "🎯 Component Comparison"]
   [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
    [:div
     [:h3.font-medium.ty-text+.mb-2.flex.items-center.gap-2
      [:ty-icon.ty-text-primary {:name "package"
                                 :size "sm"}]
      "ty-calendar (This Component)"]
     [:ul.space-y-2.text-sm.ty-text-
      [:li.flex.items-start.gap-2
       [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                         :size "xs"}]
       [:span [:strong "Two Modes:"] " Stateful (default) or Stateless (via attribute)"]]
      [:li.flex.items-start.gap-2
       [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                         :size "xs"}]
       [:span [:strong "Navigation:"] " Built-in month/year controls"]]
      [:li.flex.items-start.gap-2
       [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                         :size "xs"}]
       [:span [:strong "Complete:"] " Full calendar experience out of the box"]]
      [:li.flex.items-start.gap-2
       [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                         :size "xs"}]
       [:span [:strong "Pass-through:"] " Forwards customization to ty-calendar-month"]]
      [:li.flex.items-start.gap-2
       [:ty-icon.ty-text.mt-0.5 {:name "info"
                                      :size "xs"}]
       [:span [:strong "Stateful mode:"] " Manages selection internally"]]
      [:li.flex.items-start.gap-2
       [:ty-icon.ty-text.mt-0.5 {:name "info"
                                      :size "xs"}]
       [:span [:strong "Stateless mode:"] " Parent controls all state"]]]]
    [:div
     [:h3.font-medium.ty-text+.mb-2.flex.items-center.gap-2
      [:ty-icon.ty-text-secondary {:name "square"
                                   :size "sm"}]
      "ty-calendar-month (Foundation)"]
     [:ul.space-y-2.text-sm.ty-text-
      [:li.flex.items-start.gap-2
       [:ty-icon.ty-text.mt-0.5 {:name "minus"
                                      :size "xs"}]
       [:span [:strong "Always Stateless:"] " Parent controls all state"]]
      [:li.flex.items-start.gap-2
       [:ty-icon.ty-text.mt-0.5 {:name "minus"
                                      :size "xs"}]
       [:span [:strong "No Navigation:"] " Just renders a single month"]]
      [:li.flex.items-start.gap-2
       [:ty-icon.ty-text.mt-0.5 {:name "minus"
                                      :size "xs"}]
       [:span [:strong "Foundation:"] " Building block for calendar UIs"]]
      [:li.flex.items-start.gap-2
       [:ty-icon.ty-text.mt-0.5 {:name "minus"
                                      :size "xs"}]
       [:span [:strong "Direct Control:"] " You manage everything"]]]]]])

(defn architecture-diagram-section []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2.text-xl.font-semibold.ty-text.mb-4 "🏭 Component Architecture"]
   [:div.ty-bg-neutral-.rounded-lg.p-4.text-center
    [:div.inline-block.text-left
     [:div.flex.items-center.gap-2.mb-3
      [:div.ty-bg-primary-.px-3.py-2.rounded.font-mono.text-sm.ty-text-primary "ty-calendar"]
      [:ty-icon.ty-text- {:name "arrow-right"
                          :size "sm"}]
      [:span.ty-text-.text-sm "(stateful wrapper)"]]
     [:div.ml-8.pl-4.border-l-2.ty-border-
      [:div.flex.items-center.gap-2.mb-2
       [:div.ty-bg-secondary-.px-3.py-1.rounded.font-mono.text-sm.ty-text-secondary "ty-calendar-navigation"]
       [:span.ty-text--.text-xs "(month/year controls)"]]
      [:div.flex.items-center.gap-2
       [:div.ty-bg-secondary-.px-3.py-1.rounded.font-mono.text-sm.ty-text-secondary "ty-calendar-month"]
       [:span.ty-text--.text-xs "(renders days with your custom functions)"]]]]]
   [:p.text-sm.ty-text-.mt-4.italic "Your dayContentFn and customCSS are passed through to ty-calendar-month"]])

(defn key-features-section []
  [:div.mb-8
   [:h2.text-xl.font-semibold.ty-text.mb-4 "✨ Key Features"]
   [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-primary.mt-0.5 {:name "navigation"
                                       :size "sm"}]
     [:div
      [:p.font-medium.ty-text "Built-in Navigation"]
      [:p.text-sm.ty-text- "Month and year controls with keyboard support."]]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-primary.mt-0.5 {:name "calendar-check"
                                       :size "sm"}]
     [:div
      [:p.font-medium.ty-text "State Management"]
      [:p.text-sm.ty-text- "Manages selected date internally, emits change events."]]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-primary.mt-0.5 {:name "layers"
                                       :size "sm"}]
     [:div
      [:p.font-medium.ty-text "Customization Pass-through"]
      [:p.text-sm.ty-text- "dayContentFn and customCSS forwarded to month view."]]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-primary.mt-0.5 {:name "form-input"
                                       :size "sm"}]
     [:div
      [:p.font-medium.ty-text "Form Integration"]
      [:p.text-sm.ty-text- "Works with native forms via name attribute."]]]]])

(defn basic-usage-section []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-semibold.ty-text.mb-4 "Basic Usage"]
   [:p.text-sm.ty-text-.mb-4 "Drop in a complete calendar with navigation - no configuration needed."]
   (code-block "<ty-calendar></ty-calendar>")
   [:div.mt-4
    [:ty-calendar]]])

(defn initial-selection-example []
  [:div.ty-content.rounded-lg.p-6
   [:h3.text-lg.font-medium.ty-text.mb-4 "With Initial Selection"]
   [:p.text-sm.ty-text-.mb-4 "Set initial date using year, month, and day attributes."]
   [:div.flex.flex-col.lg:flex-row.gap-6.mb-4
    [:div.flex-1
     [:ty-calendar {:year "2024"
                    :month "12"
                    :day "25"}]]
    [:div.lg:w-80
     [:h4.font-medium.ty-text.mb-2 "Features"]
     [:ul.space-y-1.text-sm.ty-text-
      [:li "• Starts with Dec 25, 2024 selected"]
      [:li "• Navigation controls change view"]
      [:li "• Selection persists during navigation"]
      [:li "• Emits 'change' event on selection"]]]]
   (code-block "<ty-calendar year=\"2024\" month=\"12\" day=\"25\"></ty-calendar>")])

(defn value-property-example []
  [:div.ty-content.rounded-lg.p-6
   [:h3.text-lg.font-medium.ty-text.mb-4 "Using the value Property"]
   [:p.text-sm.ty-text-.mb-4 "Alternative way to set date using ISO format (YYYY-MM-DD)."]
   [:div.flex.flex-col.lg:flex-row.gap-6.mb-4
    [:div.flex-1
     [:ty-calendar {:id "value-calendar"
                    :replicant/on-mount
                    (fn [{^js el :replicant/node}]
                      (set! (.-value el) "2024-12-25"))}]]
    [:div.lg:w-80
     [:h4.font-medium.ty-text.mb-2 "Value Property"]
     [:p.text-sm.ty-text- "The value property accepts and returns ISO date strings, making it easy to integrate with APIs and databases."]]]
   (code-block "// Set date using ISO string\nconst calendar = document.getElementById('calendar');\ncalendar.value = '2024-12-25';\n\n// Read selected date\nconsole.log(calendar.value); // '2024-12-25'\n\n// Clear selection\ncalendar.value = '';")])

(defn navigation-events-example []
  [:div.ty-content.rounded-lg.p-6
   [:h3.text-lg.font-medium.ty-text.mb-4 "Navigation Events"]
   [:p.text-sm.ty-text-.mb-4 "Listen to both 'change' (day selection) and 'navigate' (month/year changes) events."]
   [:div.flex.flex-col.lg:flex-row.gap-6.mb-4
    [:div.flex-1
     [:ty-calendar {:id "navigate-calendar"
                    :on {:change (fn [^js e]
                                   (let [{:keys [year month day]} (->clj (.-detail e))]
                                     (js/console.log "Date selected:" year month day)))
                         :navigate (fn [^js e]
                                     (let [{:keys [year month]} (->clj (.-detail e))]
                                       (js/console.log "Navigated to:" year month)))}}]]
    [:div.lg:w-80
     [:h4.font-medium.ty-text.mb-2 "Two Event Types"]
     [:p.text-sm.ty-text-.mb-3 [:strong "change:"] " Fires when selecting a day"]
     [:p.text-sm.ty-text- [:strong "navigate:"] " Fires when changing month/year with controls"]]]
   (code-block "const calendar = document.getElementById('calendar');\n\n// Listen for day selection\ncalendar.addEventListener('change', (e) => {\n  const { year, month, day } = e.detail;\n  console.log(`Selected: ${year}-${month}-${day}`);\n});\n\n// Listen for month/year navigation\ncalendar.addEventListener('navigate', (e) => {\n  const { year, month } = e.detail;\n  console.log(`Navigated to: ${year}-${month}`);\n});")])

(defn stateless-mode-example []
  [:div.ty-content.rounded-lg.p-6.border-2.ty-border-warning
   [:h3.text-lg.font-medium.ty-text.mb-4 "Stateless Mode"]
   [:p.text-sm.ty-text-.mb-4 "For complete control, use stateless mode where the parent manages all state."]
   [:div.flex.flex-col.lg:flex-row.gap-6.mb-4
    [:div.flex-1
     [:p.text-sm.ty-text-.mb-2 "In stateless mode:"]
     [:ul.space-y-1.text-sm.ty-text-.mb-4
      [:li "• Calendar doesn't update attributes"]
      [:li "• Calendar doesn't update form values"]
      [:li "• Parent controls year/month/day props"]
      [:li "• Listen to events and update props manually"]]
     [:div.ty-elevated.rounded.p-3.text-xs.font-mono
      "<ty-calendar stateless year=\"2024\" month=\"12\"></ty-calendar>"]]
    [:div.lg:w-80
     [:h4.font-medium.ty-text.mb-2 "When to Use"]
     [:p.text-sm.ty-text- "Use stateless mode when building complex calendar UIs where you need full control over state management, like booking ranges or multi-date selection."]]]
   (code-block "// Stateless calendar - parent controls everything\nconst calendar = document.querySelector('ty-calendar[stateless]');\n\n// Set displayed month (doesn't persist)\ncalendar.year = 2024;\ncalendar.month = 12;\n\n// Listen for selections but calendar won't update itself\ncalendar.addEventListener('change', (e) => {\n  const { year, month, day } = e.detail;\n  // You decide what to do with the selection\n  console.log('User clicked:', year, month, day);\n  \n  // Manually update props if needed\n  calendar.year = year;\n  calendar.month = month;\n  calendar.day = day;\n});")])

(defn without-navigation-example []
  [:div.ty-content.rounded-lg.p-6
   [:h3.text-lg.font-medium.ty-text.mb-4 "Without Navigation"]
   [:p.text-sm.ty-text-.mb-4 "Hide the navigation controls for simpler displays."]
   [:div.flex.flex-col.lg:flex-row.gap-6.mb-4
    [:div.flex-1
     [:ty-calendar {:show-navigation "false"
                    :year "2024"
                    :month "12"}]]
    [:div.lg:w-80
     [:h4.font-medium.ty-text.mb-2 "Use Cases"]
     [:p.text-sm.ty-text- "Useful when you want to show a specific month without allowing navigation, or when you're building custom navigation controls."]]]
   (code-block "<ty-calendar show-navigation=\"false\" year=\"2024\" month=\"12\"></ty-calendar>")])

(defn form-integration-example []
  [:div.ty-content.rounded-lg.p-6
   [:h3.text-lg.font-medium.ty-text.mb-4 "Form Integration"]
   [:p.text-sm.ty-text-.mb-4 "Use the name attribute for form submission."]
   [:div.flex.flex-col.lg:flex-row.gap-6.mb-4
    [:div.flex-1
     [:form {:on-submit (fn [e]
                          (.preventDefault e)
                          (js/alert "Form would submit with selected date"))}
      [:label.block.text-sm.font-medium.ty-text.mb-2 "Select Event Date"]
      [:ty-calendar {:name "event-date"
                     :id "form-calendar"}]
      [:ty-button.mt-4 {:type "submit"} "Submit"]]]
    [:div.lg:w-80
     [:h4.font-medium.ty-text.mb-2 "Form Behavior"]
     [:p.text-sm.ty-text- "The calendar submits the selected date as ISO format (YYYY-MM-DD) when included in a form."]
     [:div.mt-3.text-xs.ty-elevated.p-3.rounded
      [:strong.ty-text "Submitted value:"]
      [:pre.mt-1.ty-text- "event-date=2024-12-25"]]]]
   (code-block "<form>\n  <label>Select Event Date</label>\n  <ty-calendar name=\"event-date\"></ty-calendar>\n  <button type=\"submit\">Submit</button>\n</form>")])

(defn hotel-pricing-example []
  [:div.ty-content.rounded-lg.p-6
   [:h3.text-lg.font-medium.ty-text.mb-2.flex.items-center.gap-2
    [:ty-icon.ty-text-warning {:name "zap"
                               :size "sm"}]
    "⚡ Property-Based Hotel Pricing Calendar"]
   [:p.text-sm.ty-text-.mb-4
    "Complete example showing hotel room pricing with weekend rates and dynamic pricing - demonstrates the power of customCSS with dayContentFn."]

   [:div.mb-4
    [:p.text-xs.ty-text-.italic.mb-2 "CSS injected via customCSS property - loaded with defstyles!"]
    [:div.ty-elevated.rounded.p-4 {:style {:width "100%"
                                           :max-width "600px"}}
     [:ty-calendar {:id "hotel-pricing-calendar"
                    :month "12"
                    :year "2024"
                    :replicant/on-mount
                    (fn [{^js el :replicant/node}]
                      ;; Apply custom CSS for hotel pricing styling
                      (set! (.-customCSS el) hotel-pricing-styles)

                      ;; Custom day content function for hotel pricing
                      (set! (.-dayContentFn el)
                            (fn [^js context]
                              (let [{day-in-month :dayInMonth
                                     weekend :weekend
                                     other-month :otherMonth} (->clj context)
                                    container (.createElement js/document "div")
                                    day-span (.createElement js/document "span")
                                    price-span (.createElement js/document "span")
                                    price (get pricing-data day-in-month 150)]

                                (set! (.-className container) (if other-month
                                                                "hotel-calendar-day other-month"
                                                                "hotel-calendar-day"))
                                (set! (.-className day-span) "day-number")
                                (set! (.-textContent day-span) (str day-in-month))

                                (when-not other-month
                                  (set! (.-className price-span)
                                        (cond
                                          (> price 200) "price-tag high-demand"
                                          weekend "price-tag weekend-price"
                                          :else "price-tag"))
                                  (set! (.-textContent price-span) (str "€" price)))

                                (.appendChild container day-span)
                                (when-not other-month
                                  (.appendChild container price-span))
                                container))))}]]]

   [:div.my-4
    [:h4.font-medium.ty-text.mb-2 "Key Implementation Points:"]
    [:ul.space-y-2.text-sm.ty-text-
     [:li.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                        :size "xs"}]
      [:span [:strong "Container Width:"] " Calendar fills the container (max-width: 600px)"]]
     [:li.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                        :size "xs"}]
      [:span [:strong "Custom CSS:"] " Injected via customCSS property using defstyles macro"]]
     [:li.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                        :size "xs"}]
      [:span [:strong "Dynamic Pricing:"] " Weekend rates (€210) vs. weekday rates (€150)"]]
     [:li.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                        :size "xs"}]
      [:span [:strong "Visual Hierarchy:"] " Clear day numbers with prominent price tags"]]]]

   (code-block "// Hotel Pricing Calendar - Price Only
const calendar = document.getElementById('hotel-calendar');

// Custom CSS for pricing display
const hotelStyles = new CSSStyleSheet();
hotelStyles.replaceSync(`
  .hotel-calendar-day {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 0.25rem;
  }
  .hotel-calendar-day .price-tag {
    background: #10b981;
    color: white;
    padding: 0.125rem 0.5rem;
    border-radius: 0.375rem;
    font-size: 0.75rem;
    font-weight: 600;
  }
`);

calendar.customCSS = hotelStyles;

// Pricing data
const pricingData = { 1: 210, 2: 150, /* ... */ };

// Custom day rendering
calendar.dayContentFn = (ctx) => {
  const container = document.createElement('div');
  const dayNum = document.createElement('span');
  const priceTag = document.createElement('span');
  
  container.className = 'hotel-calendar-day';
  dayNum.className = 'day-number';
  dayNum.textContent = ctx.dayInMonth;
  
  if (!ctx.otherMonth) {
    const price = pricingData[ctx.dayInMonth] || 150;
    priceTag.className = 'price-tag';
    priceTag.textContent = `€${price}`;
  }
  
  container.appendChild(dayNum);
  if (!ctx.otherMonth) container.appendChild(priceTag);
  
  return container;
};" "javascript")])

(defn localization-example []
  [:div.ty-content.rounded-lg.p-6
   [:h3.text-lg.font-medium.ty-text.mb-4 "Localization"]
   [:p.text-sm.ty-text-.mb-4 "Both navigation and month display respect the locale setting."]
   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-4.mb-4
    [:div
     [:h4.text-sm.font-medium.ty-text.mb-2 "French (fr-FR)"]
     [:ty-calendar {:locale "fr-FR"}]]
    [:div
     [:h4.text-sm.font-medium.ty-text.mb-2 "Japanese (ja-JP)"]
     [:ty-calendar {:locale "ja-JP"}]]]
   (code-block "<ty-calendar locale=\"fr-FR\"></ty-calendar>\n<ty-calendar locale=\"ja-JP\"></ty-calendar>")])

(defn advanced-customization-divider []
  [:div.border-t.ty-border.pt-8.mt-12.mb-8
   [:h2.text-2xl.font-semibold.ty-text.mb-4 "Advanced Customization"]
   [:p.text-sm.ty-text-.mb-6
    "Pass custom rendering functions through to the month display. These work exactly like ty-calendar-month customization."]])

;; =============================================================================
;; Main View Function
;; =============================================================================

(defn view []
  (docs-page
   (header-section)
   (api-reference-section)
   (stateful-vs-stateless-section)
   (architecture-diagram-section)
   (key-features-section)
   (basic-usage-section)

   ;; Examples Section
   [:h2.text-2xl.font-semibold.ty-text.mb-6 "Examples"]
   [:div.space-y-8
    (initial-selection-example)
    (value-property-example)
    (navigation-events-example)
    (stateless-mode-example)
    (without-navigation-example)
    (form-integration-example)]

   ;; Advanced Customization
   (advanced-customization-divider)
   [:div.space-y-8
    (hotel-pricing-example)
    (localization-example)]))
