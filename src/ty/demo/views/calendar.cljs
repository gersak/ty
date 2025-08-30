(ns ty.demo.views.calendar
  "Modern calendar component demos showcasing hybrid architecture."
  (:require [cljs-bean.core :refer [->clj ->js]]
            [ty.demo.state :as state])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load demo CSS styles for customCSS property injection
(defstyles hotel-pricing-styles "ty/demo/views/hotel-pricing.css")
(defstyles event-calendar-styles "ty/demo/views/event-calendar.css")
(defstyles luxury-suite-styles "ty/demo/views/luxury-suite.css")

;; Demo render functions setup - now showcasing both approaches
(defonce demo-functions-setup
  (do
    (js/console.log "Setting up modern calendar demo functions...")

    ;; Legacy global functions (for fallback demonstration only)
    (set! (.-legacyHotelContent js/window)
          (fn [^js context]
            (let [{:keys [day-in-month other-month weekend]} (->clj context)
                  base-price 120
                  weekend-markup (if weekend 50 0)
                  price (+ base-price weekend-markup)]
              (if other-month
                (let [span (.createElement js/document "span")]
                  (set! (.-textContent span) (str day-in-month))
                  span)
                (let [container (.createElement js/document "div")
                      day-span (.createElement js/document "span")
                      price-span (.createElement js/document "span")]
                  (set! (.-className container) "legacy-hotel-day")
                  (set! (.-className day-span) "day-num")
                  (set! (.-textContent day-span) (str day-in-month))
                  (set! (.-className price-span) "price")
                  (set! (.-textContent price-span) (str "$" price))
                  (.appendChild container day-span)
                  (.appendChild container price-span)
                  container)))))

    (js/console.log "Demo functions registered - showcasing both property and attribute approaches")
    :setup-complete))

(defn handle-value-change
  "Handle calendar value changes (selection)"
  [^js event]
  (let [detail (.-detail event)
        value (.-value detail)
        source (.-source detail)]
    (js/console.log "Calendar value changed:" detail)
    (swap! state/state assoc :calendar-selection
           {:value value
            :source source
            :formatted (when value (.toLocaleDateString value "en-US"))
            :timestamp (js/Date.now)})))

(defn handle-navigation-change
  "Handle calendar navigation changes (month/year)"
  [^js event]
  (let [detail (.-detail event)
        month (.-month detail)
        year (.-year detail)
        source (.-source detail)]
    (js/console.log "Calendar navigation changed:" detail)
    (swap! state/state assoc :calendar-navigation
           {:month month
            :year year
            :source source
            :timestamp (js/Date.now)})))

(defn handle-day-click
  "Handle individual day clicks"
  [^js event]
  (let [detail (.-detail event)]
    (js/console.log "Day clicked:" detail)
    (swap! state/state assoc :last-day-click
           {:value (.-value detail)
            :date-parts (->clj (.-dateParts detail))
            :is-today (.-isToday detail)
            :is-weekend (.-isWeekend detail)
            :timestamp (js/Date.now)})))

(defn orchestrated-calendar-demo []
  [:div.demo-section
   [:h2.demo-title "🎉 Orchestrated Calendar (ty-calendar)"]
   [:p.text-sm.text-gray-600.mb-6 "Attribute-based API with property-based composition - best of both worlds."]

   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8.mb-8

    ;; Basic orchestrated calendar
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "Basic Calendar with Navigation"]
      [:p.text-sm.text-gray-600.mb-4 "Simple HTML attributes, internal property coordination."]
      [:ty-calendar {:width "350px"
                     :on {:value-change handle-value-change
                          :navigation-change handle-navigation-change
                          :day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       "<!-- Simple attribute-based API -->\n"
       "<ty-calendar width=\"350px\"></ty-calendar>\n\n"
       ";; ClojureScript\n"
       "[:ty-calendar\n"
       " {:width \"350px\"\n"
       "  :on {:value-change handle-value-change}}]"]]]

    ;; Pre-selected date with navigation
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "📅 With Pre-Selected Date"]
      [:p.text-sm.text-gray-600.mb-4 "Automatic navigation to selected month, attribute control."]
      [:ty-calendar {:width "350px"
                     :value "2024-12-25"
                     :on {:value-change handle-value-change
                          :navigation-change handle-navigation-change
                          :day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       "<!-- Pre-selected with auto-navigation -->\n"
       "<ty-calendar\n"
       "  width=\"350px\"\n"
       "  value=\"2024-12-25\">\n"
       "</ty-calendar>"]]]

    ;; Property-based render functions (NEW APPROACH!)
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "⚡ Property-Based Hotel Pricing (REAL customCSS!)"]
      [:p.text-sm.text-gray-600.mb-4 "CSS injected via customCSS property - loaded with defstyles!"]
      [:ty-calendar {:id "property-hotel-calendar"
                     :width "420px"
                     :value "2024-12-22"
                     :replicant/on-mount (fn [{^js el :replicant/node}]
                                           ;; Direct function assignment - much cleaner!
                                           (set! (.-dayContentFn el)
                                                 (fn [^js context]
                                                   (let [{:keys [day-in-month weekend other-month]} (->clj context)
                                                         base-price 150
                                                         weekend-markup (if weekend 60 0)
                                                         price (+ base-price weekend-markup)]
                                                     (if other-month
                                                       (let [span (.createElement js/document "span")]
                                                         (set! (.-className span) "other-month")
                                                         (set! (.-textContent span) (str day-in-month))
                                                         span)
                                                       (let [container (.createElement js/document "div")
                                                             day-span (.createElement js/document "span")
                                                             price-span (.createElement js/document "span")]
                                                         (set! (.-className container) "modern-hotel-day")
                                                         (set! (.-className day-span) "day-num")
                                                         (set! (.-textContent day-span) (str day-in-month))
                                                         (set! (.-className price-span) "price modern")
                                                         (set! (.-textContent price-span) (str "€" price))
                                                         (.appendChild container day-span)
                                                         (.appendChild container price-span)
                                                         container)))))

                                           ;; REAL customCSS injection using defstyles!
                                           (set! (.-customCSS el) hotel-pricing-styles))
                     :on {:value-change handle-value-change
                          :navigation-change handle-navigation-change
                          :day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       "// NEW: Direct property assignment (preferred)\n"
       "const cal = document.querySelector('ty-calendar');\n"
       "cal.dayContentFn = function(context) {\n"
       "  const {dayInMonth, weekend} = context;\n"
       "  const price = 150 + (weekend ? 60 : 0);\n"
       "  // Return DOM element\n"
       "  return createPriceElement(dayInMonth, price);\n"
       "};\n\n"
       ";; ClojureScript with mount hook\n"
       "[:ty-calendar\n"
       " {:replicant/on-mount\n"
       "  (fn [{^js el :replicant/node}]\n"
       "    (set! (.-dayContentFn el) hotel-pricing-fn))}]"]]]

    ;; Property-based event calendar  
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "📅 Property-Based Event Calendar (REAL customCSS!)"]
      [:p.text-sm.text-gray-600.mb-4 "Event styling via customCSS property - no global CSS needed!"]
      [:ty-calendar {:id "property-event-calendar"
                     :width "350px"
                     :value "2024-12-18"
                     :replicant/on-mount (fn [{^js el :replicant/node}]
                                           (set! (.-dayContentFn el)
                                                 (fn [^js context]
                                                   (let [{:keys [day-in-month]} (->clj context)
                                                         ;; Mock events for demo (15th, 18th, 22nd, 25th, 28th have events)
                                                         has-events (#{15 18 22 25 28} day-in-month)
                                                         event-types (get {15 "meeting"
                                                                           18 "conference"
                                                                           22 "deadline"
                                                                           25 "holiday"
                                                                           28 "workshop"} day-in-month)]
                                                     (if has-events
                                                       (let [container (.createElement js/document "div")
                                                             day-span (.createElement js/document "span")
                                                             dot (.createElement js/document "span")]
                                                         (set! (.-className container) "modern-event-day")
                                                         (set! (.-className day-span) "day-num")
                                                         (set! (.-textContent day-span) (str day-in-month))
                                                         (set! (.-className dot) (str "event-dot " event-types))
                                                         (set! (.-title dot) (str "Event: " event-types))
                                                         (.appendChild container day-span)
                                                         (.appendChild container dot)
                                                         container)
                                                       (let [span (.createElement js/document "span")]
                                                         (set! (.-textContent span) (str day-in-month))
                                                         span)))))
                                           (set! (.-dayClassesFn el)
                                                 (fn [^js context]
                                                   (let [{:keys [day-in-month weekend today]} (->clj context)
                                                         has-events (#{15 18 22 25 28} day-in-month)]
                                                     (cond-> ["calendar-day"]
                                                       today (conj "today")
                                                       weekend (conj "weekend")
                                                       has-events (conj "has-events")))))

                                           ;; REAL customCSS injection using defstyles!
                                           (set! (.-customCSS el) event-calendar-styles))
                     :on {:value-change handle-value-change
                          :navigation-change handle-navigation-change
                          :day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       "// Property-based event system\n"
       "cal.dayContentFn = (context) => {\n"
       "  const hasEvents = myEventChecker(context.dayInMonth);\n"
       "  return hasEvents ? createEventDay() : createNormalDay();\n"
       "};\n"
       "cal.dayClassesFn = (context) => {\n"
       "  return ['calendar-day', context.today ? 'today' : ''];\n"
       "};"]]]]

   ;; NEW: CSS injection benefits section  
   [:div.p-4.bg-blue-50.rounded-lg.mb-6
    [:h4.font-semibold.mb-3 "✨ NEW: Custom CSS Property"]
    [:div.text-sm.space-y-3
     [:div
      [:strong "Complete Styling Control:"] " Inject CSS directly alongside render functions - no more Shadow DOM styling headaches!"]
     [:div
      [:strong "REAL EXAMPLES ABOVE:"] " The hotel pricing and event calendars above use REAL customCSS property injection!"]
     [:div.space-y-2
      [:strong "Usage:"]
      [:pre.text-xs.bg-white.p-3.rounded.mt-2
       [:code
        "// JavaScript - Perfect combination\n"
        "const cal = document.querySelector('ty-calendar');\n"
        "cal.dayContentFn = myRenderFunction;  // Content\n"
        "cal.dayClassesFn = myClassFunction;   // Classes\n"
        "cal.customCSS = myStyles;             // Styling!\n\n"
        "// ACTUAL demo implementation (see above):\n"
        "cal.customCSS = hotelPricingStyles;  // Loaded via defstyles\n\n"
        "// Or with raw CSS string:\n"
        "cal.customCSS = `\n"
        "  .my-custom-day {\n"
        "    background: linear-gradient(135deg, #fbbf24, #f59e0b);\n"
        "    border-radius: 8px;\n"
        "    padding: 4px;\n"
        "  }\n"
        "  .my-price {\n"
        "    background: #dc2626;\n"
        "    color: white;\n"
        "    padding: 2px 6px;\n"
        "    border-radius: 4px;\n"
        "  }\n"
        "`;\n\n"
        ";; ClojureScript with defstyles (demo approach)\n"
        "(defstyles hotel-styles \"hotel-pricing.css\")\n"
        "[:ty-calendar\n"
        " {:replicant/on-mount\n"
        "  (fn [{^js el :replicant/node}]\n"
        "    (set! (.-dayContentFn el) render-fn)\n"
        "    (set! (.-customCSS el) hotel-styles))}]"]]]
     [:div.text-xs.space-y-2
      [:div "✅ CSS automatically injected into Shadow DOM"]
      [:div "✅ Works perfectly with custom classes from dayClassesFn"]
      [:div "✅ No global <style> tags needed"]
      [:div "✅ Scoped to component instance"]
      [:div "✅ Re-renders when customCSS property changes"]
      [:div "✅ Demo uses REAL customCSS property injection with defstyles!"]]]]

   ;; Approach comparison
   [:div.p-4.bg-yellow-50.rounded-lg.mb-6
    [:h4.font-semibold.mb-3 "🆚 Property vs Attribute Approaches:"]
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-4.text-sm
     [:div
      [:p.font-medium.mb-2.text-red-700 "OLD: Attribute-Based (Global Functions)"]
      [:pre.text-xs.bg-white.p-3.rounded.mb-2
       [:code
        "<!-- HTML attribute approach -->\n"
        "<ty-calendar day-content-fn=\"myGlobalFunction\">\n"
        "</ty-calendar>\n\n"
        "// Required global function\n"
        "window.myGlobalFunction = function(context) {\n"
        "  // Global namespace pollution\n"
        "};"]]
      [:ul.space-y-1.ml-4.list-disc.text-xs
       [:li "❌ Global namespace pollution"]
       [:li "❌ Hard to manage in modules"]
       [:li "❌ Testing complications"]
       [:li "❌ No IDE autocomplete"]]]
     [:div
      [:p.font-medium.mb-2.text-green-700 "NEW: Property-Based (Direct Functions)"]
      [:pre.text-xs.bg-white.p-3.rounded.mb-2
       [:code
        "// Direct property assignment\n"
        "const cal = document.querySelector('ty-calendar');\n"
        "cal.dayContentFn = myRenderFunction;\n\n"
        "// No global pollution needed\n"
        "function myRenderFunction(context) {\n"
        "  // Clean, scoped function\n"
        "}"]]
      [:ul.space-y-1.ml-4.list-disc.text-xs
       [:li "✅ No global namespace pollution"]
       [:li "✅ Perfect for modules/frameworks"]
       [:li "✅ Easy to test and mock"]
       [:li "✅ IDE support and autocomplete"]
       [:li "✅ Runtime function updates"]]]]]

   ;; Legacy support section
   [:div.p-4.bg-purple-50.rounded-lg.mb-6
    [:h4.font-semibold.mb-3 "🔄 Legacy Support & Migration:"]
    [:div.text-sm.space-y-3
     [:div
      [:strong "Backward Compatibility:"] " The component still supports the old attribute approach for existing code."]
     [:div.space-y-2
      [:strong "Migration Path:"]
      [:div.ml-4
       [:div "1. Keep existing attribute-based code working"]
       [:div "2. Gradually move to property-based approach"]
       [:div "3. Remove global functions when ready"]
       [:div "4. Enjoy cleaner, more maintainable code"]]
      [:pre.text-xs.bg-white.p-3.rounded.mt-2
       [:code
        "// Example: Legacy fallback demo\n"
        "// Still works but not recommended\n"
        "<ty-calendar day-content-fn=\"legacyHotelContent\"></ty-calendar>"]]]
     ;; Legacy fallback demo
     [:div.space-y-2
      [:div
       [:strong "Legacy Fallback Demo:"]
       [:ty-calendar {:width "300px"
                      :value "2024-12-15"
                      :day-content-fn "legacyHotelContent"
                      :on {:value-change handle-value-change}}]]
      [:div.text-xs.text-gray-600 "↑ This still works (using global window.legacyHotelContent) but property approach is preferred."]]]]

   ;; Live event monitoring
   [:div.space-y-4.mb-8
    [:h3.demo-subtitle.mb-2 "🎯 Live Event Monitoring"]
    [:p.text-sm.text-gray-600.mb-4 "See the event coordination in action."]

    [:div.grid.grid-cols-1.md:grid-cols-3.gap-4
     ;; Selection events
     [:div.p-3.bg-green-50.rounded.text-xs
      [:div.font-semibold.mb-2.text-green-700 "Selection Events"]
      (if-let [selection (:calendar-selection @state/state)]
        [:div.space-y-1
         [:div "📅 Date: " (:formatted selection)]
         [:div "🔗 Source: " (:source selection)]
         [:div "⏰ Time: " (.toLocaleTimeString (js/Date. (:timestamp selection)))]]
        [:div.text-gray-500 "Select a date..."])]

     ;; Navigation events
     [:div.p-3.bg-purple-50.rounded.text-xs
      [:div.font-semibold.mb-2.text-purple-700 "Navigation Events"]
      (if-let [nav (:calendar-navigation @state/state)]
        [:div.space-y-1
         [:div "📅 Month: " (:month nav)]
         [:div "📅 Year: " (:year nav)]
         [:div "🔗 Source: " (:source nav)]
         [:div "⏰ Time: " (.toLocaleTimeString (js/Date. (:timestamp nav)))]]
        [:div.text-gray-500 "Navigate to see events..."])]

     ;; Day click events
     [:div.p-3.bg-blue-50.rounded.text-xs
      [:div.font-semibold.mb-2.text-blue-700 "Day Click Events"]
      (if-let [click (:last-day-click @state/state)]
        [:div.space-y-1
         [:div "📅 Day: " (get-in click [:date-parts :day])]
         [:div "🌅 Weekend: " (if (:is-weekend click) "Yes" "No")]
         [:div "✨ Today: " (if (:is-today click) "Yes" "No")]
         [:div "⏰ Time: " (.toLocaleTimeString (js/Date. (:timestamp click)))]]
        [:div.text-gray-500 "Click a day..."])]]]])

(defn property-based-components-demo []
  [:div.demo-section
   [:h2.demo-title "🔧 Property-Based Components (Advanced)"]
   [:p.text-sm.text-gray-600.mb-6 "Direct property control for maximum performance and flexibility."]

   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8.mb-8

    ;; Standalone navigation
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "🎛️ Standalone Navigation"]
      [:p.text-sm.text-gray-600.mb-4 "Property-controlled navigation component."]
      [:ty-calendar-navigation {:id "demo-nav"
                                :replicant/on-mount (fn [{^js el :replicant/node}]
                                                      (set! (.-displayMonth el) 12)
                                                      (set! (.-displayYear el) 2024)
                                                      (set! (.-width el) "350px"))
                                :on {:change #(let [detail (.-detail %)]
                                                (js/console.log "Navigation change:" detail))}}]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       "// Direct property control\n"
       "const nav = document.querySelector('ty-calendar-navigation');\n"
       "nav.displayMonth = 12;\n"
       "nav.displayYear = 2024;\n"
       "nav.width = '350px';  // Auto re-render!"]]]

    ;; Standalone month with property-based render functions
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "📅 Property-Based Month Display"]
      [:p.text-sm.text-gray-600.mb-4 "Direct property control with scoped render functions."]
      [:ty-calendar-month {:id "demo-month"
                           :replicant/on-mount (fn [{^js el :replicant/node}]
                                                 (set! (.-displayMonth el) 12)
                                                 (set! (.-displayYear el) 2024)
                                                 (set! (.-value el) "2024-12-15")
                                                 (set! (.-width el) "420px")
                                                 ;; Property-based render function - no globals!
                                                 (set! (.-dayContentFn el)
                                                       (fn [^js context]
                                                         (let [{:keys [day-in-month other-month today]} (->clj context)
                                                               availability (if (odd? day-in-month) "available" "booked")]
                                                           (if other-month
                                                             (let [span (.createElement js/document "span")]
                                                               (set! (.-className span) "other-month")
                                                               (set! (.-textContent span) (str day-in-month))
                                                               span)
                                                             (let [container (.createElement js/document "div")
                                                                   day-span (.createElement js/document "span")
                                                                   status-span (.createElement js/document "span")]
                                                               (set! (.-className container) (str "availability-day " availability))
                                                               (set! (.-className day-span) "day-num")
                                                               (set! (.-textContent day-span) (str day-in-month))
                                                               (set! (.-className status-span) "status")
                                                               (set! (.-textContent status-span) (if (= availability "available") "✓" "✗"))
                                                               (.appendChild container day-span)
                                                               (.appendChild container status-span)
                                                               container))))))
                           :on {:day-click handle-day-click}}]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       "// Property-based month component\n"
       "const month = document.querySelector('ty-calendar-month');\n"
       "month.displayMonth = 12;\n"
       "month.value = '2024-12-15';\n"
       "month.dayContentFn = myAvailabilityFunction; // Direct assignment\n\n"
       "// No global namespace pollution!\n"
       "function myAvailabilityFunction(context) {\n"
       "  return context.available ? checkmark : xmark;\n"
       "}"]]]]])

(defn width-and-styling-demo []
  [:div.demo-section
   [:h2.demo-title "📏 Width Control & Styling"]
   [:p.text-sm.text-gray-600.mb-6 "Flexible sizing with CSS custom properties."]

   [:div.grid.grid-cols-1.lg:grid-cols-3.gap-6.mb-8

    ;; Fixed width
    [:div.space-y-3
     [:div
      [:h4.font-medium.mb-1 "Fixed Width"]
      [:p.text-xs.text-gray-600.mb-3 "Exact calendar dimensions"]
      [:ty-calendar {:width "320px"
                     :value "2024-12-15"
                     :on {:value-change handle-value-change}}]]
     [:pre.text-xs.bg-gray-100.p-2.rounded
      [:code "width=\"320px\""]]]

    ;; Responsive width
    [:div.space-y-3
     [:div
      [:h4.font-medium.mb-1 "Responsive Width"]
      [:p.text-xs.text-gray-600.mb-3 "Adapts to container with property-based content"]
      [:ty-calendar {:id "responsive-width-cal"
                     :width "clamp(300px, 50vw, 450px)"
                     :value "2024-12-20"
                     :replicant/on-mount (fn [{^js el :replicant/node}]
                                           ;; Property-based pricing function
                                           (set! (.-dayContentFn el)
                                                 (fn [^js context]
                                                   (let [{:keys [day-in-month weekend other-month]} (->clj context)]
                                                     (if other-month
                                                       (let [span (.createElement js/document "span")]
                                                         (set! (.-textContent span) (str day-in-month))
                                                         span)
                                                       (let [container (.createElement js/document "div")
                                                             day-span (.createElement js/document "span")]
                                                         (set! (.-className container) "simple-day")
                                                         (set! (.-textContent day-span) (str day-in-month))
                                                         (when weekend (set! (.-className container) "weekend-day"))
                                                         (.appendChild container day-span)
                                                         container))))))
                     :on {:value-change handle-value-change}}]]
     [:pre.text-xs.bg-gray-100.p-2.rounded
      [:code "width=\"clamp(300px, 50vw, 450px)\""]]]

    ;; Constrained width
    [:div.space-y-3
     [:div
      [:h4.font-medium.mb-1 "Min/Max Width"]
      [:p.text-xs.text-gray-600.mb-3 "With constraints and property-based events"]
      [:ty-calendar {:id "constrained-width-cal"
                     :min-width "280px"
                     :max-width "400px"
                     :width "100%"
                     :value "2024-12-25"
                     :replicant/on-mount (fn [{^js el :replicant/node}]
                                           ;; Property-based event function
                                           (set! (.-dayContentFn el)
                                                 (fn [^js context]
                                                   (let [{:keys [day-in-month]} (->clj context)
                                                         has-events (#{25 31} day-in-month)]
                                                     (if has-events
                                                       (let [container (.createElement js/document "div")
                                                             day-span (.createElement js/document "span")
                                                             star (.createElement js/document "span")]
                                                         (set! (.-className container) "holiday-day")
                                                         (set! (.-textContent day-span) (str day-in-month))
                                                         (set! (.-textContent star) "★")
                                                         (set! (.-className star) "star")
                                                         (.appendChild container day-span)
                                                         (.appendChild container star)
                                                         container)
                                                       (let [span (.createElement js/document "span")]
                                                         (set! (.-textContent span) (str day-in-month))
                                                         span))))))
                     :on {:value-change handle-value-change}}]]
     [:pre.text-xs.bg-gray-100.p-2.rounded
      [:code "min-width=\"280px\"\nmax-width=\"400px\""]]]]])

(defn integration-patterns-demo []
  [:div.demo-section
   [:h2.demo-title "🔗 Integration Patterns"]
   [:p.text-sm.text-gray-600.mb-6 "Real-world integration examples for different use cases."]

   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8.mb-8

    ;; Form integration
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "📝 Form Integration"]
      [:p.text-sm.text-gray-600.mb-4 "Calendar as form input with validation."]
      [:form.space-y-4 {:on {:submit (fn [e] (.preventDefault e))}}
       [:div
        [:label.block.text-sm.font-medium.mb-2 "Select Date"]
        [:ty-calendar {:width "350px"
                       :value ""
                       :on {:value-change #(js/console.log "Form value:" (.-value (.-detail %)))}}]]
       [:button.btn.btn-primary {:type "submit"} "Submit"]]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       "<!-- Form integration -->\n"
       "<form>\n"
       "  <label>Select Date</label>\n"
       "  <ty-calendar\n"
       "    value=\"\"\n"
       "    onvalue-change=\"handleDateChange\">\n"
       "  </ty-calendar>\n"
       "</form>"]]]

    ;; Dynamic update
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "🔄 Dynamic Updates"]
      [:p.text-sm.text-gray-600.mb-4 "Programmatic calendar control."]
      [:div.space-y-4
       [:ty-calendar {:id "dynamic-calendar"
                      :width "350px"
                      :value "2024-12-01"
                      :on {:value-change handle-value-change}}]
       [:div.space-x-2
        [:button.btn.btn-sm {:on {:click #(.setAttribute (.getElementById js/document "dynamic-calendar") "value" "2024-12-25")}} "Christmas"]
        [:button.btn.btn-sm {:on {:click #(.setAttribute (.getElementById js/document "dynamic-calendar") "value" "2024-12-31")}} "New Year"]
        [:button.btn.btn-sm {:on {:click #(.setAttribute (.getElementById js/document "dynamic-calendar") "value" "")}} "Clear"]]]]
     [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
      [:code
       "// Programmatic updates\n"
       "const cal = document.querySelector('#my-calendar');\n"
       "cal.setAttribute('value', '2024-12-25');\n\n"
       "// Automatically triggers re-render and navigation"]]]]])

(defn view []
  [:div.p-6
   [:div.mb-8
    [:h1.text-3xl.font-bold.text-gray-900.dark:text-white "Modern Calendar System"]
    [:p.text-lg.text-gray-600.dark:text-gray-400.mt-2
     "Hybrid architecture: attribute-based API with property-based composition for optimal performance."]]

   ;; Main demos
   (orchestrated-calendar-demo)
   (property-based-components-demo)
   (width-and-styling-demo)
   (integration-patterns-demo)

   ;; Architecture summary
   [:div.demo-section
    [:h2.demo-title "🏗️ Architecture Summary"]
    [:div.grid.grid-cols-1.lg:grid-cols-3.gap-6

     [:div.p-4.bg-blue-50.rounded-lg
      [:h3.font-semibold.mb-3.text-blue-700 "ty-calendar"]
      [:div.text-sm.space-y-2
       [:div "📝 HTML attribute API"]
       [:div "🎯 Internal state management"]
       [:div "🔄 Auto property distribution"]
       [:div "📊 Event coordination"]
       [:div "✅ User-friendly"]]
      [:pre.text-xs.bg-white.p-2.rounded.mt-3
       [:code "<ty-calendar value=\"2024-12-25\">"]]]

     [:div.p-4.bg-purple-50.rounded-lg
      [:h3.font-semibold.mb-3.text-purple-700 "ty-calendar-navigation"]
      [:div.text-sm.space-y-2
       [:div "⚡ Property-based API"]
       [:div "🚀 High performance"]
       [:div "🎯 Direct control"]
       [:div "🔄 Auto re-rendering"]
       [:div "🧩 Composable"]]
      [:pre.text-xs.bg-white.p-2.rounded.mt-3
       [:code "nav.displayMonth = 12;"]]]

     [:div.p-4.bg-green-50.rounded-lg
      [:h3.font-semibold.mb-3.text-green-700 "ty-calendar-month"]
      [:div.text-sm.space-y-2
       [:div "⚡ Property-based API"]
       [:div "🎨 Custom render functions"]
       [:div "📊 Rich event system"]
       [:div "🔄 Stateless rendering"]
       [:div "🏗️ Maximum flexibility"]]
      [:pre.text-xs.bg-white.p-2.rounded.mt-3
       [:code "cal.dayContentFn = fn;"]]]]]

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
      [:h3.demo-subtitle "For Advanced Use Cases: Property-Based Functions (NEW!)"]
      [:div.text-sm.space-y-3
       [:div "⚡ Direct function property assignment"]
       [:div "🔒 No global namespace pollution"]
       [:div "🧪 Easy testing and mocking"]
       [:div "🔧 Perfect for modern frameworks"]]
      [:pre.text-xs.bg-gray-100.p-3.rounded.overflow-x-auto
       [:code
        "// JavaScript - Direct property assignment (RECOMMENDED)\n"
        "const cal = document.querySelector('ty-calendar');\n"
        "cal.dayContentFn = function(context) {\n"
        "  return createCustomDay(context.dayInMonth);\n"
        "};\n\n"
        ";; ClojureScript - Property-based approach\n"
        "[:ty-calendar\n"
        " {:replicant/on-mount\n"
        "  (fn [{^js el :replicant/node}]\n"
        "    (set! (.-dayContentFn el) my-render-fn))}]\n\n"
        "// React - Direct property integration\n"
        "<ty-calendar ref={el => el && (el.dayContentFn = myFn)} />"]]]]]])
