(ns ty.site.docs.calendar
  "Documentation for ty-calendar component"
  (:require
    [cljs-bean.core :refer [->clj]]
    [ty.site.docs.common
     :refer [code-block
             attribute-table
             event-table]])
  (:require-macros [ty.css :refer [defstyles]]))

;; Custom styles for hotel pricing example
(defstyles hotel-pricing-styles "ty/site/docs/calendar-hotel-pricing.css")

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Title and Description
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-calendar"]
    [:p.text-lg.ty-text- "A complete, stateful calendar component with built-in navigation and date selection."]
    [:p.text-sm.ty-text-.mt-2 "Orchestrates ty-calendar-navigation and ty-calendar-month components with full customization pass-through."]]

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
        {:name "name"
         :type "string"
         :default "null"
         :description "Form field name for submission"}
        {:name "locale"
         :type "string"
         :default "Context locale or \"en-US\""
         :description "Locale for internationalization"}
        {:name "dayContentFn"
         :type "function"
         :default "Shows day number"
         :description "Custom day content renderer (passed to ty-calendar-month)"}
        {:name "dayClassesFn"
         :type "function"
         :default "Basic classes"
         :description "Custom CSS classes function (passed to ty-calendar-month)"}
        {:name "customCSS"
         :type "CSSStyleSheet"
         :default "null"
         :description "Custom stylesheet (passed to ty-calendar-month)"}])]

    ;; Events
    [:div.mb-8
     [:h3.text-lg.font-medium.ty-text+.mb-4 "Events"]
     (event-table
       [{:name "change"
         :payload "DayContext object with year, month, day"
         :when-fired "When a date is selected"}])]

    ;; Pass-through Behavior
    [:div
     [:h3.text-lg.font-medium.ty-text+.mb-4 "🔄 Customization Pass-through"]
     [:p.text-sm.ty-text-.mb-4 "ty-calendar acts as an orchestrator, passing these customization properties directly to its internal ty-calendar-month component:"]
     [:div.ty-bg-info-.rounded-lg.p-4
      [:ul.space-y-2.text-sm.ty-text
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-info.mt-0.5 {:name "arrow-right"
                                       :size "xs"}]
        [:span [:code.ty-elevated.px-1 "dayContentFn"] " - Your custom day rendering function"]]
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-info.mt-0.5 {:name "arrow-right"
                                       :size "xs"}]
        [:span [:code.ty-elevated.px-1 "dayClassesFn"] " - Your custom CSS class logic"]]
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-info.mt-0.5 {:name "arrow-right"
                                       :size "xs"}]
        [:span [:code.ty-elevated.px-1 "customCSS"] " - Your custom stylesheet for shadow DOM"]]]]]]

   ;; Key Differences from ty-calendar-month
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-xl.font-semibold.ty-text.mb-4 "🎯 Stateful vs. Stateless"]
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
        [:span [:strong "Stateful:"] " Manages its own selected date and navigation"]]
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
        [:span [:strong "Pass-through:"] " Forwards customization to ty-calendar-month"]]]]
     [:div
      [:h3.font-medium.ty-text+.mb-2.flex.items-center.gap-2
       [:ty-icon.ty-text-secondary {:name "square"
                                    :size "sm"}]
       "ty-calendar-month (Foundation)"]
      [:ul.space-y-2.text-sm.ty-text-
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-info.mt-0.5 {:name "minus"
                                       :size "xs"}]
        [:span [:strong "Stateless:"] " Parent controls all state"]]
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-info.mt-0.5 {:name "minus"
                                       :size "xs"}]
        [:span [:strong "No Navigation:"] " Just renders a single month"]]
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-info.mt-0.5 {:name "minus"
                                       :size "xs"}]
        [:span [:strong "Foundation:"] " Building block for calendar UIs"]]
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-info.mt-0.5 {:name "minus"
                                       :size "xs"}]
        [:span [:strong "Direct Control:"] " You manage everything"]]]]]]

   ;; Architecture Diagram
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
    [:p.text-sm.ty-text-.mt-4.italic "Your dayContentFn, dayClassesFn, and customCSS are passed through to ty-calendar-month"]]

   ;; Key Features
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
      [:p.text-sm.ty-text- "dayContentFn, dayClassesFn, and customCSS forwarded to month view."]]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-primary.mt-0.5 {:name "form-input"
                                       :size "sm"}]
     [:div
      [:p.font-medium.ty-text "Form Integration"]
      [:p.text-sm.ty-text- "Works with native forms via name attribute."]]]]

   ;; Container Width Fitting Feature
   [:div.ty-elevated.rounded-lg.p-6.mb-8.mt-8
    [:h2.text-xl.font-semibold.ty-text.mb-4 "📏 Responsive Container Fitting"]
    [:div.mb-6
     [:p.ty-text.mb-3 "ty-calendar automatically adapts to its container's width, making it incredibly flexible for different layouts."]
     [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
      [:div
       [:h3.font-medium.ty-text+.mb-2 "How It Works"]
       [:ul.space-y-2.text-sm.ty-text-
        [:li.flex.items-start.gap-2
         [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                           :size "xs"}]
         "Calendar fills available container width"]
        [:li.flex.items-start.gap-2
         [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                           :size "xs"}]
         "Days resize proportionally"]
        [:li.flex.items-start.gap-2
         [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                           :size "xs"}]
         "Navigation adjusts to container"]
        [:li.flex.items-start.gap-2
         [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                           :size "xs"}]
         "Custom content scales beautifully"]]]
      [:div
       [:h3.font-medium.ty-text+.mb-2 "Styling Approach"]
       [:p.text-sm.ty-text-.mb-2 "Simply wrap ty-calendar in a container with your desired width:"]
       [:ul.space-y-2.text-sm.ty-text-
        [:li "• Fixed width: " [:code.ty-elevated.px-1 "width: 300px"]]
        [:li "• Responsive: " [:code.ty-elevated.px-1 "max-width: 400px"]]
        [:li "• Grid/Flexbox: Fits perfectly"]
        [:li "• Combined with " [:code.ty-elevated.px-1 "dayContentFn"] " for full control"]]]]]

    ;; Live Examples
    [:div.mt-6
     [:h3.font-medium.ty-text+.mb-4 "Live Examples"]
     [:div.grid.grid-cols-1.md:grid-cols-3.gap-4
      ;; Small Calendar
      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Small (250px)"]
       [:div.ty-content.rounded.p-2 {:style {:width "250px"}}
        [:ty-calendar]]]
      ;; Medium Calendar
      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Medium (350px)"]
       [:div.ty-content.rounded.p-2 {:style {:width "350px"}}
        [:ty-calendar]]]
      ;; Large Calendar
      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Large (450px)"]
       [:div.ty-content.rounded.p-2 {:style {:width "450px"}}
        [:ty-calendar]]]]]

    [:div.ty-bg-info-.rounded-lg.p-4.mt-6
     [:p.text-sm.ty-text.flex.items-start.gap-2
      [:ty-icon.ty-text-info.mt-0.5 {:name "lightbulb"
                                     :size "sm"}]
      [:span
       [:strong "Pro Tip:"] " Combine container width control with "
       [:code.ty-elevated.px-1 "dayContentFn"]
       " to create perfectly sized custom calendars for any use case - from tiny date pickers to large booking interfaces."]]]]

   ;; Basic Usage
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Basic Usage"]
    [:p.text-sm.ty-text-.mb-4 "Drop in a complete calendar with navigation - no configuration needed."]
    (code-block "<ty-calendar></ty-calendar>")
    [:div.mt-4
     [:ty-calendar]]]

   ;; Examples Section
   [:h2.text-2xl.font-semibold.ty-text.mb-6 "Examples"]

   [:div.space-y-8
    ;; With Initial Selection
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
     (code-block "<ty-calendar year=\"2024\" month=\"12\" day=\"25\"></ty-calendar>")]

    ;; Form Integration
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
       [:p.text-sm.ty-text- "The calendar submits the selected date as a timestamp when included in a form."]
       [:div.mt-3.text-xs.ty-elevated.p-3.rounded
        [:strong.ty-text "Submitted value:"]
        [:pre.mt-1.ty-text- "event-date=2025-09-17"]]]]
     (code-block "<form>\n  <label>Select Event Date</label>\n  <ty-calendar name=\"event-date\"></ty-calendar>\n  <button type=\"submit\">Submit</button>\n</form>")]

    ;; Event Handling
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Event Handling"]
     [:p.text-sm.ty-text-.mb-4 "Listen for the 'change' event to react to date selections."]
     [:div.flex.flex-col.lg:flex-row.gap-6.mb-4
      [:div.flex-1
       [:ty-calendar {:id "event-handling-cal"
                      :on {:change (fn [^js e]
                                     (let [{:keys [year month day]} (->clj (.-detail e))]
                                       (js/alert (str "Selected: " year "-" month "-" day))))}}]]
      [:div.lg:w-80
       [:h4.font-medium.ty-text.mb-2 "Try it"]
       [:p.text-sm.ty-text- "Click any date to see the change event with complete date information."]]]
     (code-block "<ty-calendar id=\"calendar\"></ty-calendar>\n\n<script>\ndocument.getElementById('calendar').addEventListener('change', function(e) {\n  const { year, month, day } = e.detail;\n  console.log(`Selected: ${year}-${month}-${day}`);\n});\n</script>")]

    ;; Advanced Customization Section Break
    [:div.border-t.ty-border.pt-8.mt-12.mb-8
     [:h2.text-2xl.font-semibold.ty-text.mb-4 "Advanced Customization"]
     [:p.text-sm.ty-text-.mb-6
      "Pass custom rendering functions through to the month display. These work exactly like ty-calendar-month customization."]]

    ;; Localization
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

                       ;; Hotel pricing data (in real app, this would come from API)
                        (let [pricing-data {1 210
                                            2 150
                                            3 150
                                            4 150
                                            5 150
                                            6 150
                                            7 210
                                            8 210
                                            9 150
                                            10 150
                                            11 150
                                            12 150
                                            13 150
                                            14 210
                                            15 210
                                            16 150
                                            17 150
                                            18 150
                                            19 150
                                            20 150
                                            21 210
                                            22 210
                                            23 150
                                            24 150
                                            25 150
                                            26 150
                                            27 150
                                            28 210
                                            29 210
                                            30 150
                                            31 150}]

                         ;; Custom day content function for hotel pricing
                          (set! (.-dayContentFn el)
                                (fn [^js context]
                                  (let [{:keys [day-in-month weekend other-month]} (->clj context)
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
                                    container)))))}]]]

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

     (code-block "// 1. Create custom styles for Shadow DOM
const hotelPricingStyles = new CSSStyleSheet();
hotelPricingStyles.replaceSync(`
  .hotel-calendar-day {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 0.25rem;
    padding: 0.5rem 0;
  }
  
  .hotel-calendar-day .day-number {
    font-size: 1rem;
    font-weight: 500;
  }
  
  .hotel-calendar-day .price-tag {
    background: #10b981;
    color: white;
    padding: 0.125rem 0.5rem;
    border-radius: 0.375rem;
    font-size: 0.75rem;
    font-weight: 600;
    min-width: 3.5rem;
    text-align: center;
  }
  
  .hotel-calendar-day .price-tag.high-demand {
    background: #f59e0b;
  }
  
  .hotel-calendar-day.other-month {
    opacity: 0.3;
  }
`);

// 2. Configure the calendar
const calendar = document.getElementById('hotel-calendar');

// Apply custom styles
calendar.customCSS = hotelPricingStyles;

// Hotel pricing data (from your API/database)
const pricingData = {
  1: 210, 2: 150, 3: 150, 4: 150, 5: 150, 6: 150, 7: 210, 8: 210,
  9: 150, 10: 150, 11: 150, 12: 150, 13: 150, 14: 210, 15: 210,
  // ... more pricing data
};

// Custom day content function
calendar.dayContentFn = function(dayContext) {
  const container = document.createElement('div');
  const dayNum = document.createElement('span');
  const priceTag = document.createElement('span');
  
  container.className = dayContext.otherMonth ? 
    'hotel-calendar-day other-month' : 'hotel-calendar-day';
  
  dayNum.className = 'day-number';
  dayNum.textContent = dayContext.dayInMonth;
  
  if (!dayContext.otherMonth) {
    const price = pricingData[dayContext.dayInMonth] || 150;
    priceTag.className = price > 200 ? 'price-tag high-demand' : 
                         dayContext.weekend ? 'price-tag weekend-price' : 
                         'price-tag';
    priceTag.textContent = `€${price}`;
  }
  
  container.appendChild(dayNum);
  if (!dayContext.otherMonth) {
    container.appendChild(priceTag);
  }
  
  return container;
};

// 3. Wrap in container for width control
<div style=\"max-width: 600px\">
  <ty-calendar id=\"hotel-calendar\" month=\"12\" year=\"2024\"></ty-calendar>
</div>" "javascript")]

    ;; Localization
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
     (code-block "<ty-calendar locale=\"fr-FR\"></ty-calendar>\n<ty-calendar locale=\"ja-JP\"></ty-calendar>")]]])
