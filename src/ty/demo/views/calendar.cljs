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

(defn handle-change
  "Handle calendar changes (both navigation and selection)"
  [^js event]
  (let [detail (.-detail event)]
    (js/console.log "Calendar changed:" detail)
    (swap! state/state assoc :calendar-change
           {:year (.-year detail)
            :month (.-month detail)
            :day (.-day detail)
            :source (.-source detail)
            :is-today (.-today detail)
            :is-weekend (.-weekend detail)
            :timestamp (js/Date.now)})))

(defn orchestrated-calendar-demo []
  [:div.demo-section
   [:h2.demo-title "🎉 Orchestrated Calendar (ty-calendar)"]
   [:p.text-sm.ty-text-.mb-6 "Year/Month/Day attribute-based API with property-based composition - intuitive and clean."]

   ;; First row: Basic examples
   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8.mb-8

    ;; Basic orchestrated calendar
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "Basic Calendar with Navigation"]
      [:p.text-sm.ty-text-.mb-4 "Simple year/month/day attributes, internal property coordination."]
      [:div.flex.justify-center
       [:ty-calendar {:width "350px"
                      :on {:change handle-change}}]]]
     [:pre.text-xs.ty-content.p-3.rounded.overflow-x-auto
      [:code
       "<!-- Simple year/month/day attribute API -->\n"
       "<ty-calendar width=\"350px\"></ty-calendar>\n\n"
       ";; ClojureScript\n"
       "[:ty-calendar\n"
       " {:width \"350px\"\n"
       "  :on {:change handle-change}}]"]]]

    ;; Pre-selected date with navigation
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "📅 With Pre-Selected Date"]
      [:p.text-sm.ty-text-.mb-4 "Automatic navigation to selected month, year/month/day control."]
      [:div.flex.justify-center
       [:ty-calendar {:width "350px"
                      :year "2024"
                      :month "12"
                      :day "25"
                      :on {:change handle-change}}]]]
     [:pre.text-xs.ty-content.p-3.rounded.overflow-x-auto
      [:code
       "<!-- Pre-selected with auto-navigation -->\n"
       "<ty-calendar\n"
       "  width=\"350px\"\n"
       "  year=\"2024\"\n"
       "  month=\"12\"\n"
       "  day=\"25\">\n"
       "</ty-calendar>"]]]]

   ;; Second row: Advanced examples
   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8.mb-8

    ;; Property-based render functions (NEW APPROACH!)
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "⚡ Property-Based Hotel Pricing"]
      [:p.text-sm.ty-text-.mb-4 "CSS injected via customCSS property - loaded with defstyles!"]
      [:div.flex.justify-center
       [:ty-calendar {:id "property-hotel-calendar"
                      :width "380px"
                      :year "2024"
                      :month "12"
                      :day "22"
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
                      :on {:change handle-change}}]]]
     [:pre.text-xs.ty-content.p-3.rounded.overflow-x-auto.max-h-40
      [:code
       "// Direct property assignment\n"
       "const cal = document.querySelector('ty-calendar');\n"
       "cal.dayContentFn = function(context) {\n"
       "  const {dayInMonth, weekend} = context;\n"
       "  const price = 150 + (weekend ? 60 : 0);\n"
       "  return createPriceElement(dayInMonth, price);\n"
       "};\n\n"
       ";; ClojureScript with mount hook\n"
       "[:ty-calendar\n"
       " {:year \"2024\" :month \"12\" :day \"22\"\n"
       "  :replicant/on-mount\n"
       "  (fn [{^js el :replicant/node}]\n"
       "    (set! (.-dayContentFn el) hotel-fn))}]"]]]

    ;; Property-based event calendar  
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "📅 Property-Based Event Calendar"]
      [:p.text-sm.ty-text-.mb-4 "Event styling via customCSS property - no global CSS needed!"]
      [:div.flex.justify-center
       [:ty-calendar {:id "property-event-calendar"
                      :width "350px"
                      :year "2024"
                      :month "12"
                      :day "18"
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
                      :on {:change handle-change}}]]]
     [:pre.text-xs.ty-content.p-3.rounded.overflow-x-auto.max-h-40
      [:code
       "// Property-based event system\n"
       "cal.dayContentFn = (context) => {\n"
       "  const hasEvents = myEventChecker(context.dayInMonth);\n"
       "  return hasEvents ? createEventDay() : createNormalDay();\n"
       "};\n"
       "cal.dayClassesFn = (context) => {\n"
       "  return ['calendar-day', context.today ? 'today' : ''];\n"
       "};"]]]]])

(defn localized-calendar-demo []
  [:div.demo-section
   [:h2.demo-title "🌍 Localized Calendar Headers"]
   [:p.text-sm.ty-text-.mb-6 "Weekday names automatically adapt to locale settings using native Intl API."]

   ;; Localized calendar examples in grid layout
   [:div.grid.grid-cols-1.lg:grid-cols-2.xl:grid-cols-3.gap-6.mb-8

    ;; English (US)
    [:div.space-y-3
     [:div
      [:h4.font-semibold.mb-1 "English (en-US)"]
      [:p.text-xs.ty-text-.mb-3 "Standard Monday-first layout"]
      [:ty-calendar {:width "280px"
                     :locale "en-US"
                     :on {:change handle-change}}]]
     [:pre.text-xs.ty-content.p-2.rounded
      [:code "locale=\"en-US\""]]]

    ;; Croatian
    [:div.space-y-3
     [:div
      [:h4.font-semibold.mb-1 "Croatian (hr)"]
      [:p.text-xs.ty-text-.mb-3 "\"pon uto sri\" weekdays"]
      [:ty-calendar {:width "280px"
                     :locale "hr"
                     :on {:change handle-change}}]]
     [:pre.text-xs.ty-content.p-2.rounded
      [:code "locale=\"hr\""]]]

    ;; German
    [:div.space-y-3
     [:div
      [:h4.font-semibold.mb-1 "German (de-DE)"]
      [:p.text-xs.ty-text-.mb-3 "\"Mo Di Mi\" format"]
      [:ty-calendar {:width "280px"
                     :locale "de-DE"
                     :on {:change handle-change}}]]
     [:pre.text-xs.ty-content.p-2.rounded
      [:code "locale=\"de-DE\""]]]

    ;; French
    [:div.space-y-3
     [:div
      [:h4.font-semibold.mb-1 "French (fr-FR)"]
      [:p.text-xs.ty-text-.mb-3 "\"lun mar mer\" weekdays"]
      [:ty-calendar {:width "280px"
                     :locale "fr-FR"
                     :on {:change handle-change}}]]
     [:pre.text-xs.ty-content.p-2.rounded
      [:code "locale=\"fr-FR\""]]]

    ;; Japanese
    [:div.space-y-3
     [:div
      [:h4.font-semibold.mb-1 "Japanese (ja-JP)"]
      [:p.text-xs.ty-text-.mb-3 "Native kanji weekdays"]
      [:ty-calendar {:width "280px"
                     :locale "ja-JP"
                     :on {:change handle-change}}]]
     [:pre.text-xs.ty-content.p-2.rounded
      [:code "locale=\"ja-JP\""]]]

    ;; Arabic
    [:div.space-y-3
     [:div
      [:h4.font-semibold.mb-1 "Arabic (ar-SA)"]
      [:p.text-xs.ty-text-.mb-3 "Right-to-left support"]
      [:ty-calendar {:width "280px"
                     :locale "ar-SA"
                     :on {:change handle-change}}]]
     [:pre.text-xs.ty-content.p-2.rounded
      [:code "locale=\"ar-SA\""]]]]

   ;; Usage example
   [:div.mb-6
    [:h3.demo-subtitle.mb-2 "Usage Examples"]
    [:pre.text-xs.ty-content.p-3.rounded.overflow-x-auto
     [:code
      "<!-- HTML -->
<ty-calendar locale=\"en-US\" width=\"300px\"></ty-calendar>
<ty-calendar locale=\"hr\" width=\"300px\"></ty-calendar>
<ty-calendar locale=\"de-DE\" width=\"300px\"></ty-calendar>

<!-- ClojureScript -->
[:ty-calendar {:locale \"en-US\" :width \"300px\"}]
[:ty-calendar {:locale \"hr\" :width \"300px\"}]
[:ty-calendar {:locale \"de-DE\" :width \"300px\"}]"]]]

   ;; Features explanation
   [:div {:class "mt-6 p-4 ty-bg-primary- rounded-lg"}
    [:h4.font-semibold.mb-2 "🎯 Localization Features"]
    [:ul {:class "text-sm space-y-1"}
     [:li "✅ Weekday headers: Automatically localized using native Intl.DateTimeFormat"]
     [:li "✅ Month names: Handled by navigation component"]
     [:li "✅ Date formats: Configurable via format attribute"]
     [:li "✅ Monday-first layout: European calendar standard"]
     [:li "✅ RTL support: Works with Arabic and Hebrew locales"]
     [:li "✅ Fallback: Defaults to browser locale if none specified"]]]

   ;; Implementation details
   [:div {:class "mt-4 p-4 ty-bg-success- rounded-lg"}
    [:h4.font-semibold.mb-2 "⚙️ Implementation Details"]
    [:div {:class "text-sm space-y-2"}
     [:div [:strong "Weekday Generation:"] " Uses Intl.DateTimeFormat with Monday-first reordering"]
     [:div [:strong "Locale Inheritance:"] " Components respect locale attributes or inherit from context"]
     [:div [:strong "Performance:"] " Weekday names cached per locale, minimal overhead"]
     [:div [:strong "Browser Support:"] " Works with all modern browsers supporting Intl API"]]]

   ;; Supported languages
   [:div {:class "mt-4 p-4 ty-bg-warning- rounded-lg"}
    [:h4.font-semibold.mb-2 "🌍 Supported Languages"]
    [:div {:class "grid grid-cols-2 md:grid-cols-4 gap-2 text-sm"}
     [:div.p-2.ty-elevated.rounded "🇺🇸 English"]
     [:div.p-2.ty-elevated.rounded "🇭🇷 Croatian"]
     [:div.p-2.ty-elevated.rounded "🇩🇪 German"]
     [:div.p-2.ty-elevated.rounded "🇫🇷 French"]
     [:div.p-2.ty-elevated.rounded "🇪🇸 Spanish"]
     [:div.p-2.ty-elevated.rounded "🇮🇹 Italian"]
     [:div.p-2.ty-elevated.rounded "🇵🇹 Portuguese"]
     [:div.p-2.ty-elevated.rounded "🇷🇺 Russian"]
     [:div.p-2.ty-elevated.rounded "🇯🇵 Japanese"]
     [:div.p-2.ty-elevated.rounded "🇰🇷 Korean"]
     [:div.p-2.ty-elevated.rounded "🇨🇳 Chinese"]
     [:div.p-2.ty-elevated.rounded "... many more"]]
    [:p.text-xs.ty-text-.mt-2
     "💡 Pro Tip: Set locale at component level or globally via ty.i18n/*locale*. Components automatically inherit locale from parent context."]]])

(defn property-based-components-demo []
  [:div.demo-section
   [:h2.demo-title "🔧 Property-Based Components (Advanced)"]
   [:p.text-sm.ty-text-.mb-6 "Direct property control for maximum performance and flexibility."]

   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8.mb-8

    ;; Standalone navigation
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "🎛️ Standalone Navigation"]
      [:p.text-sm.ty-text-.mb-4 "Property-controlled navigation component."]
      [:ty-calendar-navigation {:id "demo-nav"
                                :replicant/on-mount (fn [{^js el :replicant/node}]
                                                      (set! (.-displayMonth el) 12)
                                                      (set! (.-displayYear el) 2024)
                                                      (set! (.-width el) "350px"))
                                :on {:change #(let [detail (.-detail %)]
                                                (js/console.log "Navigation change:" detail))}}]]
     [:pre.text-xs.ty-content.p-3.rounded.overflow-x-auto
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
      [:p.text-sm.ty-text-.mb-4 "Direct property control with scoped render functions."]
      [:ty-calendar-month {:id "demo-month"
                           :replicant/on-mount (fn [{^js el :replicant/node}]
                                                 (set! (.-displayMonth el) 12)
                                                 (set! (.-displayYear el) 2024)
                                                 (set! (.-value el) (.getTime (js/Date. 2024 11 15))) ; Dec 15, 2024
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
                           :on {:day-click #(let [detail (.-detail %)]
                                              (js/console.log "Day click:" detail))}}]]
     [:pre.text-xs.ty-content.p-3.rounded.overflow-x-auto
      [:code
       "// Property-based month component\n"
       "const month = document.querySelector('ty-calendar-month');\n"
       "month.displayMonth = 12;\n"
       "month.value = new Date(2024, 11, 15).getTime();\n"
       "month.dayContentFn = myAvailabilityFunction; // Direct assignment\n\n"
       "// No global namespace pollution!\n"
       "function myAvailabilityFunction(context) {\n"
       "  return context.available ? checkmark : xmark;\n"
       "}"]]]]])

(defn width-and-styling-demo []
  [:div.demo-section
   [:h2.demo-title "📏 Width Control & Styling"]
   [:p.text-sm.ty-text-.mb-6 "Flexible sizing with CSS custom properties."]

   [:div.grid.grid-cols-1.lg:grid-cols-3.gap-6.mb-8

    ;; Fixed width
    [:div.space-y-3
     [:div
      [:h4.font-medium.mb-1 "Fixed Width"]
      [:p.text-xs.ty-text-.mb-3 "Exact calendar dimensions"]
      [:ty-calendar {:width "320px"
                     :year "2024"
                     :month "12"
                     :day "15"
                     :on {:change handle-change}}]]
     [:pre.text-xs.ty-content.p-2.rounded
      [:code "year=\"2024\" month=\"12\" day=\"15\"\nwidth=\"320px\""]]]

    ;; Responsive width
    [:div.space-y-3
     [:div
      [:h4.font-medium.mb-1 "Responsive Width"]
      [:p.text-xs.ty-text-.mb-3 "Adapts to container with property-based content"]
      [:ty-calendar {:id "responsive-width-cal"
                     :width "clamp(300px, 50vw, 450px)"
                     :year "2024"
                     :month "12"
                     :day "20"
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
                     :on {:change handle-change}}]]
     [:pre.text-xs.ty-content.p-2.rounded
      [:code "year=\"2024\" month=\"12\" day=\"20\"\nwidth=\"clamp(300px, 50vw, 450px)\""]]]

    ;; Constrained width
    [:div.space-y-3
     [:div
      [:h4.font-medium.mb-1 "Min/Max Width"]
      [:p.text-xs.ty-text-.mb-3 "With constraints and property-based events"]
      [:ty-calendar {:id "constrained-width-cal"
                     :min-width "280px"
                     :max-width "400px"
                     :width "100%"
                     :year "2024"
                     :month "12"
                     :day "25"
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
                     :on {:change handle-change}}]]
     [:pre.text-xs.ty-content.p-2.rounded
      [:code "year=\"2024\" month=\"12\" day=\"25\"\nmin-width=\"280px\"\nmax-width=\"400px\""]]]]])

(defn integration-patterns-demo []
  [:div.demo-section
   [:h2.demo-title "🔗 Integration Patterns"]
   [:p.text-sm.ty-text-.mb-6 "Real-world integration examples for different use cases."]

   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8.mb-8

    ;; Form integration
    [:div.space-y-4
     [:div
      [:h3.demo-subtitle.mb-2 "📝 Form Integration"]
      [:p.text-sm.ty-text-.mb-4 "Calendar as form input with validation."]
      [:form.space-y-4 {:on {:submit (fn [e] (.preventDefault e))}}
       [:div
        [:label.block.text-sm.font-medium.mb-2 "Select Date"]
        [:ty-calendar {:width "350px"
                       :value ""
                       :on {:value-change #(js/console.log "Form value:" (.-value (.-detail %)))}}]]
       [:button.btn.btn-primary {:type "submit"} "Submit"]]]
     [:pre.text-xs.ty-content.p-3.rounded.overflow-x-auto
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
      [:p.text-sm.ty-text-.mb-4 "Programmatic calendar control."]
      [:div.space-y-4
       [:ty-calendar {:id "dynamic-calendar"
                      :width "350px"
                      :value "2024-12-01"
                      :on {:change (fn [evnt] (.log js/console evnt))}}]
       [:div.space-x-2
        [:button.btn.btn-sm {:on {:click #(.setAttribute (.getElementById js/document "dynamic-calendar") "value" "2024-12-25")}} "Christmas"]
        [:button.btn.btn-sm {:on {:click #(.setAttribute (.getElementById js/document "dynamic-calendar") "value" "2024-12-31")}} "New Year"]
        [:button.btn.btn-sm {:on {:click #(.setAttribute (.getElementById js/document "dynamic-calendar") "value" "")}} "Clear"]]]]
     [:pre.text-xs.ty-content.p-3.rounded.overflow-x-auto
      [:code
       "// Programmatic updates\n"
       "const cal = document.querySelector('#my-calendar');\n"
       "cal.setAttribute('value', '2024-12-25');\n\n"
       "// Automatically triggers re-render and navigation"]]]]])

(defn view []
  [:div.p-6
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text "Modern Calendar System"]
    [:p.text-lg.ty-text-.mt-2
     "Year/Month/Day architecture with property-based composition for optimal performance and intuitive API."]]

   ;; Main demos
   (orchestrated-calendar-demo)
   (localized-calendar-demo)

   ;; NEW: Year/Month/Day API benefits section  
   [:div.p-4.ty-bg-primary-.rounded-lg.mb-6
    [:h4.font-semibold.mb-3 "✨ NEW: Year/Month/Day Attribute API"]
    [:div.text-sm.space-y-3
     [:div
      [:strong "Intuitive Interface:"] " Work directly with familiar year/month/day concepts instead of timestamps."]
     [:div
      [:strong "No Date Parsing:"] " All values are simple numbers - no more Date object complexity!"]
     [:div.space-y-2
      [:strong "Usage:"]
      [:pre.text-xs.ty-elevated.p-3.rounded.mt-2
       [:code
        "<!-- OLD: Timestamp/ISO string approach -->\n"
        "<ty-calendar value=\"2024-12-25T00:00:00Z\"></ty-calendar>\n"
        "<ty-calendar value=\"1735084800000\"></ty-calendar>\n\n"
        "<!-- NEW: Clean year/month/day approach -->\n"
        "<ty-calendar year=\"2024\" month=\"12\" day=\"25\"></ty-calendar>\n\n"
        "// JavaScript - Simple number attributes\n"
        "cal.setAttribute('year', '2024');\n"
        "cal.setAttribute('month', '12');\n"
        "cal.setAttribute('day', '25');\n\n"
        ";; ClojureScript - Direct attributes\n"
        "[:ty-calendar {:year \"2024\" :month \"12\" :day \"25\"}]"]]]
     [:div.text-xs.space-y-2
      [:div "✅ No timestamp conversion needed"]
      [:div "✅ Intuitive year/month/day semantics"]
      [:div "✅ Automatic validation (1-12 months, correct day ranges)"]
      [:div "✅ Defaults to current month when no attributes provided"]
      [:div "✅ Single 'change' event with complete day context"]
      [:div "✅ Clear separation between display and selection state"]]]]

   ;; Live event monitoring
   [:div.space-y-4.mb-8
    [:h3.demo-subtitle.mb-2 "🎯 Live Event Monitoring"]
    [:p.text-sm.ty-text-.mb-4 "See the unified change event in action."]

    [:div.grid.grid-cols-1.gap-4
     ;; Single change event
     [:div.p-3.ty-bg-positive.rounded.text-xs
      [:div.font-semibold.mb-2.ty-text-positive "Single Change Event (Unified)"]
      (if-let [change (:calendar-change @state/state)]
        [:div.space-y-1
         [:div "📅 Date: " (:year change) "/" (:month change) "/" (:day change)]
         [:div "🔗 Source: " (:source change)]
         [:div "🌅 Weekend: " (if (:is-weekend change) "Yes" "No")]
         [:div "✨ Today: " (if (:is-today change) "Yes" "No")]
         [:div "⏰ Time: " (.toLocaleTimeString (js/Date. (:timestamp change)))]]
        [:div.text-gray-500 "Select a date or navigate to see events..."])]]]

   (property-based-components-demo)
   (width-and-styling-demo)
   (integration-patterns-demo)

   ;; Architecture summary
   [:div.demo-section
    [:h2.demo-title "🏗️ Architecture Summary"]
    [:div.grid.grid-cols-1.lg:grid-cols-3.gap-6

     [:div.p-4.ty-bg-primary-.rounded-lg
      [:h3.font-semibold.mb-3.ty-text-primary "ty-calendar"]
      [:div.text-sm.space-y-2
       [:div "📝 Year/Month/Day attribute API"]
       [:div "🎯 Internal state management"]
       [:div "🔄 Auto property distribution"]
       [:div "📊 Event coordination"]
       [:div "✅ User-friendly"]]
      [:pre.text-xs.ty-elevated.p-2.rounded.mt-3
       [:code "<ty-calendar year=\"2024\" month=\"12\" day=\"25\">"]]]

     [:div.p-4.ty-bg-secondary-.rounded-lg
      [:h3.font-semibold.mb-3.ty-text-secondary "ty-calendar-navigation"]
      [:div.text-sm.space-y-2
       [:div "⚡ Property-based API"]
       [:div "🚀 High performance"]
       [:div "🎯 Direct control"]
       [:div "🔄 Auto re-rendering"]
       [:div "🧩 Composable"]]
      [:pre.text-xs.ty-elevated.p-2.rounded.mt-3
       [:code "nav.displayMonth = 12;"]]]

     [:div.p-4.ty-bg-success-.rounded-lg
      [:h3.font-semibold.mb-3.ty-text-success "ty-calendar-month"]
      [:div.text-sm.space-y-2
       [:div "⚡ Property-based API"]
       [:div "🎨 Custom render functions"]
       [:div "📊 Rich event system"]
       [:div "🔄 Stateless rendering"]
       [:div "🏗️ Maximum flexibility"]]
      [:pre.text-xs.ty-elevated.p-2.rounded.mt-3
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
      [:pre.text-xs.ty-content.p-3.rounded.overflow-x-auto
       [:code
        "<!-- HTML -->\n"
        "<ty-calendar year=\"2024\" month=\"12\" day=\"25\" width=\"350px\"></ty-calendar>\n\n"
        ";; ClojureScript\n"
        "[:ty-calendar {:year \"2024\" :month \"12\" :day \"25\" :width \"350px\"}]\n\n"
        "// React JSX\n"
        "<ty-calendar year=\"2024\" month=\"12\" day=\"25\" width=\"350px\" />"]]]

     [:div.space-y-4
      [:h3.demo-subtitle "For Advanced Use Cases: Property-Based Functions (NEW!)"]
      [:div.text-sm.space-y-3
       [:div "⚡ Direct function property assignment"]
       [:div "🔒 No global namespace pollution"]
       [:div "🧪 Easy testing and mocking"]
       [:div "🔧 Perfect for modern frameworks"]]
      [:pre.text-xs.ty-content.p-3.rounded.overflow-x-auto
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
