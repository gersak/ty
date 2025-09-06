(ns ty.demo.views.dropdowns
  (:require [ty.context :as context]
            [ty.demo.state :as state]
            [ty.i18n :as i18n]
            [ty.layout :as layout]))

(defn dropdown-event-handler [^js event]
  (let [detail (.-detail event)
        value (.. event -detail -option -value)
        text (.-text detail)]
    (swap! state/state assoc ::dropdown-value value)))

(defn date-picker-event-handler [^js event]
  (let [detail (.-detail event)
        value (.-value detail)
        formatted (.-formatted detail)]
    (swap! state/state assoc :date-picker-value value)))

(defn demo-row [{:keys [title description children]}]
  [:div.mb-8
   (when title
     [:h3.demo-subtitle title])
   (when description
     [:p.ty-text-.mb-4 description])
   (into [:div.flex.flex-wrap.gap-4.items-start] children)])

(defn code-snippet [code]
  [:div.mt-4
   [:pre.code-block.text-xs
    [:code code]]])

(defn basic-examples []
  [:div.demo-section
   [:h2.demo-title "Basic Usage"]

   (demo-row {:title "Basic Dropdown"
              :description "A simple dropdown with searchable options"
              :children [[:div.max-w-xs
                          [:ty-dropdown.w-full {:value "red"
                                                :placeholder "Select a color..."
                                                :style {:min-width "200px"}
                                                :on {:change dropdown-event-handler}}
                           [:option {:value "red"} "Red"]
                           [:option {:value "blue"} "Blue"]
                           [:option {:value "green"} "Green"]
                           [:option {:value "yellow"} "Yellow"]
                           [:option {:value "purple"} "Purple"]]]]})
   (code-snippet "<ty-dropdown value=\"red\" placeholder=\"Select a color...\" style=\"min-width: 200px;\">
  <option value=\"red\">Red</option>
  <option value=\"blue\">Blue</option>
  <option value=\"green\">Green</option>
</ty-dropdown>")

   (demo-row {:title "With Label"
              :description "Dropdown with built-in label support"
              :children [[:div.max-w-xs
                          [:ty-dropdown {:value "medium"
                                         :label "Size"
                                         :placeholder "Select size..."
                                         :style {:min-width "180px"}
                                         :on {:change dropdown-event-handler}}
                           [:option {:value "small"} "Small"]
                           [:option {:value "medium"} "Medium"]
                           [:option {:value "large"} "Large"]
                           [:option {:value "xl"} "Extra Large"]]]]})
   (code-snippet "<ty-dropdown label=\"Size\" placeholder=\"Select size...\" style=\"min-width: 180px;\">
  <option value=\"small\">Small</option>
  <option value=\"medium\">Medium</option>
</ty-dropdown>")

   (demo-row {:title "Required Dropdown"
              :description "Dropdown with required indicator"
              :children [[:div.max-w-xs
                          [:ty-dropdown {:label "Country"
                                         :required true
                                         :placeholder "Select your country"
                                         :style {:min-width "200px"}
                                         :on {:change dropdown-event-handler}}
                           [:option {:value "us"} "United States"]
                           [:option {:value "ca"} "Canada"]
                           [:option {:value "uk"} "United Kingdom"]
                           [:option {:value "de"} "Germany"]
                           [:option {:value "fr"} "France"]]]]})
   (code-snippet "<ty-dropdown label=\"Country\" required placeholder=\"Select your country\">
  <option value=\"us\">United States</option>
  <option value=\"ca\">Canada</option>
</ty-dropdown>")

   (demo-row {:title "Non-searchable"
              :description "Disable search functionality for simple selection"
              :children [[:div.max-w-xs
                          [:ty-dropdown {:value "medium"
                                         :searchable "false"
                                         :placeholder "Select size..."
                                         :style {:min-width "180px"}
                                         :on {:change dropdown-event-handler}}
                           [:option {:value "small"} "Small"]
                           [:option {:value "medium"} "Medium"]
                           [:option {:value "large"} "Large"]
                           [:option {:value "xl"} "Extra Large"]]]]})
   (code-snippet "<ty-dropdown searchable=\"false\" placeholder=\"Select size...\" style=\"min-width: 180px;\">
  <option value=\"small\">Small</option>
  <option value=\"medium\">Medium</option>
</ty-dropdown>")

   (demo-row {:title "Disabled State"
              :description "Disabled dropdown for read-only contexts"
              :children [[:div.max-w-xs
                          [:ty-dropdown {:value "option2"
                                         :disabled true
                                         :placeholder "Disabled dropdown"
                                         :style {:min-width "200px"}}
                           [:option {:value "option1"} "Option 1"]
                           [:option {:value "option2"} "Option 2"]
                           [:option {:value "option3"} "Option 3"]]]]})

   (demo-row {:title "Read-only State"
              :description "Read-only dropdown shows value but prevents interaction (note: no chevron)"
              :children [[:div.max-w-xs
                          [:ty-dropdown {:value "readonly-value"
                                         :label "Read-only Field"
                                         :readonly true
                                         :style {:min-width "200px"}}
                           [:option {:value "readonly-value"} "Read-only Value"]
                           [:option {:value "other"} "Other Option"]]]]})])

(defn date-picker-examples []
  [:div.demo-section
   [:h2.demo-title "🎉 NEW: Date Picker Component"]
   [:p.ty-text-.mb-6
    "Date picker combines input field with calendar dropdown - now uses ty-calendar internally with clean architecture separation."]

   (demo-row {:title "Basic Date Pickers"
              :description "Compare regular dropdown vs date picker side by side"
              :children [[:div.max-w-xs
                          [:ty-dropdown {:label "Country"
                                         :placeholder "Select country..."
                                         :style {:min-width "200px"}
                                         :on {:change dropdown-event-handler}}
                           [:option {:value "us"} "🇺🇸 United States"]
                           [:option {:value "ja"} "🇯🇵 Japan"]
                           [:option {:value "de"} "🇩🇪 Germany"]
                           [:option {:value "au"} "🇦🇺 Australia"]
                           [:option {:value "pt_BR"} "🇧🇷 Brazil"]
                           [:option {:value "en_ZA"} "🇿🇦 South Africa"]]]
                         [:div.mt-2.text-sm.ty-text-
                          "Selected: "
                          [:code.ty-content.px-2.py-1.rounded.text-xs
                           (str (or (::dropdown-value @state/state) "none"))]]
                         [:div.max-w-xs
                          [:ty-date-picker {:label "Birthday"
                                            :placeholder "Select date..."
                                            :locale (or (::dropdown-value @state/state) "en")
                                            :on {:change date-picker-event-handler}}]
                          [:div.mt-2.text-sm.ty-text-
                           "Selected: "
                           [:code.ty-content.px-2.py-1.rounded.text-xs
                            (if-let [date-val (:date-picker-value @state/state)]
                              (i18n/translate
                               (js/Date. date-val)
                               (::dropdown-value @state/state "en")
                               {:dateStyle "full"})
                              "none")]]]]})

   (code-snippet "<!-- Regular dropdown -->
<ty-dropdown label=\"Country\" placeholder=\"Select country...\">
  <option value=\"us\">🇺🇸 United States</option>
</ty-dropdown>

<!-- Date picker -->
<ty-date-picker label=\"Birthday\" placeholder=\"Select date...\"></ty-date-picker>")

   (demo-row {:title "Clearable Date Picker - Clear Button Test"
              :description "Date picker with clear button functionality - test clearing values"
              :children [[:div.max-w-xs
                          [:ty-date-picker {:label "Check-in Date"
                                            :value "2024-12-22"
                                            :clearable true
                                            :placeholder "Select check-in..."
                                            :on {:change date-picker-event-handler}}]
                          [:div.mt-2.text-sm.ty-text-
                           "Value: "
                           [:code.ty-content.px-2.py-1.rounded.text-xs
                            (if-let [date-val (:date-picker-value @state/state)]
                              (.toLocaleDateString (js/Date. date-val))
                              "null")]]
                          [:div.mt-1.text-xs.ty-text-primary
                           "💡 Look for X button when date is selected"]]]})

   (code-snippet "<ty-date-picker 
  label=\"Check-in Date\" 
  value=\"2024-12-22\"
  clearable=\"true\" 
  placeholder=\"Select check-in...\">
</ty-date-picker>")

   (demo-row {:title "Required Fields"
              :description "Both components support required field indicators"
              :children [[:div.max-w-xs
                          [:ty-dropdown {:label "Priority"
                                         :required true
                                         :placeholder "Select priority..."
                                         :style {:min-width "180px"}
                                         :on {:change dropdown-event-handler}}
                           [:option {:value "low"} "Low"]
                           [:option {:value "medium"} "Medium"]
                           [:option {:value "high"} "High"]]]
                         [:div.max-w-xs
                          [:ty-date-picker {:label "Due Date"
                                            :required true
                                            :placeholder "When is this due?"
                                            :on {:change date-picker-event-handler}}]]]})

   ;; NEW: Architecture explanation
   [:div.mt-8.p-4.ty-bg-primary-.rounded-lg
    [:h3.text-lg.font-semibold.mb-3.ty-text-primary "🏗️ New Architecture"]
    [:p.text-sm.ty-text-primary.mb-3
     "ty-date-picker now uses ty-calendar internally, creating a clean component hierarchy:"]
    [:div.text-sm.space-y-2.ty-text-primary
     [:div "📅 ty-date-picker: Manages selected value + input styling + dropdown behavior"]
     [:div "🧭 ty-calendar: Handles navigation controls + change events + day selection styling"]
     [:div "🎨 ty-calendar-month: Pure rendering engine"]
     [:p.mt-3.text-xs "This means consistent calendar behavior across date pickers and standalone calendars!"]]]

   ;; NEW: Size variations showing the architecture works
   (demo-row {:title "Size Variations"
              :description "All input sizes work with the internal ty-calendar integration"
              :children [[:div.space-y-4.w-full
                          [:div.flex.flex-wrap.gap-4.items-end
                           [:div.min-w-0.flex-1
                            [:ty-date-picker {:size "xs"
                                              :label "Extra Small"
                                              :value "2024-01-15"
                                              :on {:change date-picker-event-handler}}]]
                           [:div.min-w-0.flex-1
                            [:ty-date-picker {:size "sm"
                                              :label "Small"
                                              :value "2024-02-20"
                                              :on {:change date-picker-event-handler}}]]
                           [:div.min-w-0.flex-1
                            [:ty-date-picker {:size "md"
                                              :label "Medium (Default)"
                                              :value "2024-03-25"
                                              :on {:change date-picker-event-handler}}]]]
                          [:div.flex.flex-wrap.gap-4.items-end
                           [:div.min-w-0.flex-1
                            [:ty-date-picker {:size "lg"
                                              :label "Large"
                                              :value "2024-04-10"
                                              :on {:change date-picker-event-handler}}]]
                           [:div.min-w-0.flex-1
                            [:ty-date-picker {:size "xl"
                                              :label "Extra Large"
                                              :value "2024-05-30"
                                              :on {:change date-picker-event-handler}}]]]]]})

   (code-snippet "<ty-date-picker size=\"xs\" label=\"Extra Small\" value=\"2024-01-15\"></ty-date-picker>
<ty-date-picker size=\"sm\" label=\"Small\" value=\"2024-02-20\"></ty-date-picker>
<ty-date-picker size=\"md\" label=\"Medium\" value=\"2024-03-25\"></ty-date-picker>
<ty-date-picker size=\"lg\" label=\"Large\" value=\"2024-04-10\"></ty-date-picker>
<ty-date-picker size=\"xl\" label=\"Extra Large\" value=\"2024-05-30\"></ty-date-picker>")

   ;; NEW: Flavor variations
   (demo-row {:title "Semantic Flavors"
              :description "Date pickers support all semantic flavors like other components"
              :children [[:div.flex.flex-wrap.gap-4
                          [:ty-date-picker {:label "Positive"
                                            :flavor "positive"
                                            :value "2024-06-15"
                                            :style {:min-width "180px"}
                                            :on {:change date-picker-event-handler}}]
                          [:ty-date-picker {:label "Negative"
                                            :flavor "negative"
                                            :value "2024-07-20"
                                            :style {:min-width "180px"}
                                            :on {:change date-picker-event-handler}}]
                          [:ty-date-picker {:label "Important"
                                            :flavor "important"
                                            :value "2024-08-25"
                                            :style {:min-width "180px"}
                                            :on {:change date-picker-event-handler}}]]]})

   (code-snippet "<ty-date-picker flavor=\"positive\" label=\"Success Date\" value=\"2024-06-15\"></ty-date-picker>
<ty-date-picker flavor=\"negative\" label=\"Error Date\" value=\"2024-07-20\"></ty-date-picker>
<ty-date-picker flavor=\"important\" label=\"Important Date\" value=\"2024-08-25\"></ty-date-picker>")

   ;; NEW: Date + Time picker examples
   [:div.mt-8.p-4.ty-bg-success-.rounded-lg
    [:h3.text-lg.font-semibold.mb-3.ty-text-success "🆕 NEW: Date + Time Support"]
    [:p.text-sm.ty-text-success.mb-3
     "Add with-time=\"true\" to get time input with masked validation:"]
    [:div.text-sm.space-y-2.ty-text-success
     [:div "⏰ Time input with HH:mm masking"]
     [:div "📅 Combined date+time selection"]
     [:div "✨ ISO datetime string output (2024-12-25T14:30)"]
     [:div "🎯 Validates hours (00-23) and minutes (00-59)"]]]

   (demo-row {:title "Date + Time Picker Examples"
              :description "Date picker with integrated time input using masked field and datetime value output"
              :children [[:div.space-y-4.w-full
                          [:div.flex.flex-wrap.gap-4.items-end
                           [:div.min-w-0.flex-1
                            [:ty-date-picker {:label "Event DateTime"
                                              :with-time true
                                              :value "2024-12-25T14:30"
                                              :on {:change date-picker-event-handler}}]
                            [:div.mt-2.text-xs.ty-text-
                             "ISO datetime: "
                             [:code.ty-content.px-1.py-0.5.rounded.text-xs
                              (str (:date-picker-value @state/state))]]]
                           [:div.min-w-0.flex-1
                            [:ty-date-picker {:label "Meeting Time"
                                              :with-time true
                                              :size "sm"
                                              :clearable true
                                              :placeholder "Select meeting time"
                                              :on {:change date-picker-event-handler}}]]]
                          [:div.flex.flex-wrap.gap-4.items-end
                           [:div.min-w-0.flex-1
                            [:ty-date-picker {:label "Large Event"
                                              :with-time true
                                              :size "lg"
                                              :flavor "important"
                                              :value "2024-12-31T23:59"
                                              :on {:change date-picker-event-handler}}]]
                           [:div.min-w-0.flex-1
                            [:ty-date-picker {:label "Deadline"
                                              :with-time true
                                              :size "xl"
                                              :flavor "negative"
                                              :required true
                                              :on {:change date-picker-event-handler}}]]]]]})

   (code-snippet "<!-- Basic date + time picker -->
<ty-date-picker label=\"Event DateTime\" 
                with-time=\"true\" 
                value=\"2024-12-25T14:30\">
</ty-date-picker>

<!-- Clearable meeting time -->
<ty-date-picker label=\"Meeting Time\" 
                with-time=\"true\" 
                size=\"sm\" 
                clearable=\"true\" 
                placeholder=\"Select meeting time\">
</ty-date-picker>

<!-- Large important event -->
<ty-date-picker label=\"Large Event\" 
                with-time=\"true\" 
                size=\"lg\" 
                flavor=\"important\" 
                value=\"2024-12-31T23:59\">
</ty-date-picker>")

   (demo-row {:title "Time Validation Demo"
              :description "Try entering invalid times - the mask prevents values like 25:70"
              :children [[:div.max-w-xs
                          [:ty-date-picker {:label "Time Validation Test"
                                            :with-time true
                                            :value "2024-01-01T12:00"
                                            :on {:change date-picker-event-handler}}]
                          [:div.mt-2.text-sm.ty-text-
                           "Try typing: 25:70, 99:99, etc. - they'll be rejected!"]
                          [:div.mt-1.text-xs.ty-text-primary
                           "✅ Valid: 00-23 hours, 00-59 minutes"]
                          [:div.mt-1.text-xs.ty-text-danger
                           "❌ Invalid: >23 hours, >59 minutes"]]]})])

(defn date-picker-form-integration-demo []
  "Demo showcasing new form integration with HTMX compatibility for date picker"
  [:div.demo-section
   [:h2.demo-title "📝 NEW: Date Picker Form Integration & HTMX Compatibility"]
   [:div.text-sm.ty-text-.mb-6
    "Date picker now supports form-associated custom elements with ElementInternals, making it fully compatible with forms and HTMX! Works for both date-only and date+time modes."]

   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-6

    ;; Form integration example
    [:div.p-4.ty-elevated.rounded-lg
     [:h3.font-semibold.mb-3 "Form Integration"]
     [:div.text-sm.space-y-3
      [:div "✅ Automatic form value setting as ISO date/datetime string"]
      [:div "✅ Value attribute synchronization"]
      [:div "✅ Works with any server framework"]
      [:div "✅ HTMX-ready out of the box"]
      [:div "✅ Supports both date-only and date+time modes"]]

     ;; Live form demo
     [:form.mt-4.space-y-4 {:id "date-picker-form"}
      [:div
       [:ty-date-picker {:name "event-date"
                         :label "Event Date"
                         :value "2024-12-25"
                         :clearable true
                         :style {:min-width "200px"}}]]
      [:div
       [:ty-date-picker {:name "meeting-datetime"
                         :label "Meeting Date & Time"
                         :with-time true
                         :value "2024-12-25T14:30"
                         :clearable true
                         :style {:min-width "200px"}}]]
      [:div
       [:label.block.text-sm.font-medium.mb-2 "Event Description:"]
       [:textarea.w-full.p-2.border.rounded {:name "description"
                                             :placeholder "Enter event description..."
                                             :rows 3}]]
      [:button.px-4.py-2.bg-blue-500.text-white.rounded.hover:bg-blue-600
       {:type "button"
        :on {:click (fn [_]
                      (let [form (.getElementById js/document "date-picker-form")
                            form-data (js/FormData. form)]
                        (js/alert (str "Form Data:\n"
                                       "Event Date: " (.get form-data "event-date") "\n"
                                       "Meeting DateTime: " (.get form-data "meeting-datetime") "\n"
                                       "Description: " (.get form-data "description")))))}}
       "Show Form Data"]]]

    ;; HTMX example code
    [:div.p-4.ty-elevated.rounded-lg
     [:h3.font-semibold.mb-3 "HTMX Integration"]
     [:div.text-sm.space-y-3
      [:div "Date picker automatically includes selected date/datetime in form submissions"]
      [:div "ISO format: Date (YYYY-MM-DD) or DateTime (YYYY-MM-DDTHH:mm)"]
      [:div "Standard HTML form behavior - no special handling needed"]]

     [:pre.text-xs.ty-content.p-3.rounded.mt-3.overflow-x-auto
      [:code
       "<!-- HTMX Form Example -->\n"
       "<form hx-post=\"/api/events\" hx-target=\"#result\">\n"
       "  <!-- Date only -->\n"
       "  <ty-date-picker name=\"event-date\" \n"
       "                  label=\"Event Date\" \n"
       "                  value=\"2024-12-25\">\n"
       "  </ty-date-picker>\n"
       "\n"
       "  <!-- Date + Time -->\n"
       "  <ty-date-picker name=\"meeting-time\" \n"
       "                  label=\"Meeting Time\" \n"
       "                  with-time=\"true\"\n"
       "                  value=\"2024-12-25T14:30\">\n"
       "  </ty-date-picker>\n"
       "\n"
       "  <button type=\"submit\">Create Event</button>\n"
       "</form>\n\n"
       "<!-- Server receives: -->\n"
       "event-date=2024-12-25&meeting-time=2024-12-25T14:30\n\n"
       "<!-- Python/Django -->\n"
       "event_date = request.POST['event-date']  # \"2024-12-25\"\n"
       "meeting_time = request.POST['meeting-time']  # \"2024-12-25T14:30\"\n"
       "datetime.fromisoformat(event_date)  # Parse date\n"
       "datetime.fromisoformat(meeting_time)  # Parse datetime\n\n"
       "<!-- Node.js/Express -->\n"
       "const eventDate = req.body['event-date'];  // \"2024-12-25\"\n"
       "const meetingTime = req.body['meeting-time'];  // \"2024-12-25T14:30\"\n"
       "new Date(eventDate);  // Parse easily\n"
       "new Date(meetingTime);  // Parse with time"]]]

    ;; Value format comparison
    [:div.p-4.ty-elevated.rounded-lg
     [:h3.font-semibold.mb-3 "Value Format Examples"]
     [:div.text-sm.space-y-3
      [:div "Compare output formats for different modes:"]
      [:div.space-y-2
       [:div.flex.justify-between.items-center.py-1.px-2.ty-bg-neutral+.rounded
        [:span.font-mono.text-xs "date-only"]
        [:code.text-xs "2024-12-25"]]
       [:div.flex.justify-between.items-center.py-1.px-2.ty-bg-neutral+.rounded
        [:span.font-mono.text-xs "with-time"]
        [:code.text-xs "2024-12-25T14:30"]]
       [:div.flex.justify-between.items-center.py-1.px-2.ty-bg-neutral+.rounded
        [:span.font-mono.text-xs "cleared"]
        [:code.text-xs "\"\" (empty string)"]]]]
     [:div.text-xs.mt-3.ty-text-
      "💡 Server-side frameworks automatically parse these standard ISO formats"]]]])

(defn localized-date-picker-demo []
  [:div.demo-section
   [:h2.demo-title "🌍 Localized Date Picker"]
   [:p.ty-text-.mb-6
    "Date picker components automatically adapt to different locales - placeholder text, time labels, weekday names, and navigation tooltips all translate seamlessly."]

   (demo-row {:title "Multi-Language Date Pickers"
              :description "Each picker shows localized interface elements"
              :children [;; English
                         [:div.demo-item
                          [:h4.font-semibold.mb-2 "English (en-US)"]
                          [:ty-date-picker {:label "Meeting Date"
                                            :locale "en-US"
                                            :with-time true
                                            :value "2024-12-25T14:30"
                                            :on {:change date-picker-event-handler}}]]

                ;; Croatian
                         [:div.demo-item
                          [:h4.font-semibold.mb-2 "Croatian (hr)"]
                          [:ty-date-picker {:label "Datum sastanka"
                                            :locale "hr"
                                            :with-time true
                                            :value "2024-12-25T14:30"
                                            :on {:change date-picker-event-handler}}]]

                ;; German
                         [:div.demo-item
                          [:h4.font-semibold.mb-2 "German (de-DE)"]
                          [:ty-date-picker {:label "Besprechungstermin"
                                            :locale "de-DE"
                                            :with-time true
                                            :value "2024-12-25T14:30"
                                            :on {:change date-picker-event-handler}}]]

                ;; French
                         [:div.demo-item
                          [:h4.font-semibold.mb-2 "French (fr-FR)"]
                          [:ty-date-picker {:label "Date de réunion"
                                            :locale "fr-FR"
                                            :with-time true
                                            :value "2024-12-25T14:30"
                                            :on {:change date-picker-event-handler}}]]]})

   (code-snippet "<!-- English date picker -->
<ty-date-picker label=\"Meeting Date\" locale=\"en-US\" with-time=\"true\"></ty-date-picker>

<!-- Croatian date picker -->
<ty-date-picker label=\"Datum sastanka\" locale=\"hr\" with-time=\"true\"></ty-date-picker>

<!-- German date picker -->
<ty-date-picker label=\"Besprechungstermin\" locale=\"de-DE\" with-time=\"true\"></ty-date-picker>")

   ;; Show what gets localized
   [:div {:class "mt-6 p-4 ty-bg-success- rounded-lg"}
    [:h4.font-semibold.mb-2.ty-text-success "🎯 What Gets Localized"]
    [:ul {:class "text-sm space-y-1 ty-text-success"}
     [:li "✅ Placeholder text: \"Select date...\" → \"Odaberite datum...\""]
     [:li "✅ Time label: \"Time:\" → \"Vrijeme:\""]
     [:li "✅ Weekday headers: \"Mon Tue Wed\" → \"pon uto sri\""]
     [:li "✅ Navigation tooltips: \"Previous month\" → \"Prethodnji mjesec\""]
     [:li "✅ Date formatting: Uses locale-specific formats"]
     [:li "✅ Calendar layout: Respects local conventions"]]]

   ;; Technical details
   [:div {:class "mt-4 p-4 ty-bg-primary- rounded-lg"}
    [:h4.font-semibold.mb-2.ty-text-primary "⚙️ Implementation Details"]
    [:div {:class "text-sm space-y-2 ty-text-primary"}
     [:div [:strong "String translations:"] " Custom ty.i18n.string system with fallback to original text"]
     [:div [:strong "Date/time formatting:"] " Native Intl.DateTimeFormat for locale-aware display"]
     [:div [:strong "Weekday names:"] " Intl API provides localized short names in Monday-first order"]
     [:div [:strong "No external deps:"] " Built-in browser internationalization APIs only"]]]

   (demo-row {:title "Supported Languages"
              :description "Date picker currently supports 10+ languages with more coming"
              :children [[:div {:class "grid grid-cols-2 md:grid-cols-5 gap-2 text-sm"}
                          [:div.p-2.ty-bg-neutral+.rounded "🇺🇸 English"]
                          [:div.p-2.ty-bg-neutral+.rounded "🇭🇷 Croatian"]
                          [:div.p-2.ty-bg-neutral+.rounded "🇩🇪 German"]
                          [:div.p-2.ty-bg-neutral+.rounded "🇫🇷 French"]
                          [:div.p-2.ty-bg-neutral+.rounded "🇪🇸 Spanish"]
                          [:div.p-2.ty-bg-neutral+.rounded "🇮🇹 Italian"]
                          [:div.p-2.ty-bg-neutral+.rounded "🇵🇹 Portuguese"]
                          [:div.p-2.ty-bg-neutral+.rounded "🇷🇺 Russian"]
                          [:div.p-2.ty-bg-neutral+.rounded "🇯🇵 Japanese"]
                          [:div.p-2.ty-bg-neutral+.rounded "🇰🇷 Korean"]
                          [:div.p-2.ty-bg-neutral+.rounded "🇨🇳 Chinese"]
                          [:div.p-2.ty-bg-neutral+.rounded "...more"]]]})

   [:div {:class "mt-6 p-3 ty-bg-warning- rounded-lg"}
    [:div {:class "text-sm ty-text-warning"}
     [:strong "💡 Pro Tip:"] " Set locale at the component level or globally via ty.i18n/*locale*. Components automatically inherit locale from parent context if not explicitly set."]]])

(defn multiple-dropdowns-test []
  [:div.demo-section
   [:h2.demo-title "Global Dropdown Management"]
   [:p.ty-text-.mb-6
    "Test global dropdown behavior - opening one dropdown automatically closes others. This ensures only one dropdown can be open at a time across the entire page."]

   [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-6
    ;; First row
    [:div
     [:ty-dropdown {:value "a1"
                    :label "First Dropdown"
                    :placeholder "Select option A..."
                    :style {:min-width "180px"}
                    :on {:change dropdown-event-handler}}
      [:option {:value "a1"} "Option A1"]
      [:option {:value "a2"} "Option A2"]
      [:option {:value "a3"} "Option A3"]
      [:option {:value "a4"} "Option A4"]]]

    [:div
     [:ty-dropdown {:value "b1"
                    :label "Second Dropdown"
                    :placeholder "Select option B..."
                    :style {:min-width "180px"}
                    :on {:change dropdown-event-handler}}
      [:option {:value "b1"} "Option B1"]
      [:option {:value "b2"} "Option B2"]
      [:option {:value "b3"} "Option B3"]
      [:option {:value "b4"} "Option B4"]]]

    [:div
     [:ty-dropdown {:value "c1"
                    :label "Third Dropdown"
                    :placeholder "Select option C..."
                    :style {:min-width "180px"}
                    :on {:change dropdown-event-handler}}
      [:option {:value "c1"} "Option C1"]
      [:option {:value "c2"} "Option C2"]
      [:option {:value "c3"} "Option C3"]
      [:option {:value "c4"} "Option C4"]]]

    ;; Second row
    [:div
     [:ty-dropdown {:value "d1"
                    :label "Fourth Dropdown"
                    :flavor "success"
                    :placeholder "Positive dropdown..."
                    :style {:min-width "180px"}
                    :on {:change dropdown-event-handler}}
      [:option {:value "d1"} "Positive D1"]
      [:option {:value "d2"} "Positive D2"]
      [:option {:value "d3"} "Positive D3"]]]

    [:div
     [:ty-dropdown {:value "e1"
                    :label "Fifth Dropdown"
                    :flavor "danger"
                    :placeholder "Negative dropdown..."
                    :style {:min-width "180px"}
                    :on {:change dropdown-event-handler}}
      [:option {:value "e1"} "Negative E1"]
      [:option {:value "e2"} "Negative E2"]
      [:option {:value "e3"} "Negative E3"]]]

    [:div
     [:ty-dropdown {:value "f1"
                    :label "Sixth Dropdown"
                    :flavor "primary"
                    :placeholder "Important dropdown..."
                    :style {:min-width "180px"}
                    :on {:change dropdown-event-handler}}
      [:option {:value "f1"} "Important F1"]
      [:option {:value "f2"} "Important F2"]
      [:option {:value "f3"} "Important F3"]]]]

   [:div.mt-6.p-4.bg-blue-50.dark:bg-blue-900.border.border-blue-200.dark:border-blue-700.rounded
    [:h4.text-sm.font-medium.text-blue-800.dark:text-blue-200.mb-2 "Expected Behavior:"]
    [:ul.text-sm.text-blue-700.dark:text-blue-300.space-y-1
     [:li "✅ Only one dropdown can be open at a time across the entire page"]
     [:li "✅ Opening a new dropdown automatically closes any currently open dropdown"]
     [:li "✅ Works with both mobile modal and desktop dialog implementations"]
     [:li "✅ Clicking outside closes the open dropdown"]
     [:li "✅ ESC key closes the open dropdown"]
     [:li "✅ Smooth transitions without visible position jumps"]]]

   (code-snippet "<!-- Multiple dropdowns with automatic global management -->
<ty-dropdown placeholder=\"First dropdown...\" style=\"min-width: 180px;\">
  <option value=\"a1\">Option A1</option>
</ty-dropdown>

<ty-dropdown placeholder=\"Second dropdown...\" style=\"min-width: 180px;\">
  <option value=\"b1\">Option B1</option>
</ty-dropdown>

<!-- Only one will be open at a time automatically -->")])

(defn flavor-variants []
  [:div.demo-section
   [:h2.demo-title "Flavor Variants"]
   [:p.ty-text-.mb-6
    "Semantic flavors convey meaning and context through border colors"]

   [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-4
    [:div
     [:ty-dropdown {:value "neutral1"
                    :label "Neutral (default)"
                    :placeholder "Neutral flavor"
                    :style {:min-width "180px"}}
      [:option {:value "neutral1"} "Neutral Option 1"]
      [:option {:value "neutral2"} "Neutral Option 2"]]]

    [:div
     [:ty-dropdown {:flavor "success"
                    :label "Positive (Green)"
                    :value "pos1"
                    :placeholder "Positive flavor"
                    :style {:min-width "180px"}}
      [:option {:value "pos1"} "Positive Option 1"]
      [:option {:value "pos2"} "Positive Option 2"]]]

    [:div
     [:ty-dropdown {:flavor "danger"
                    :label "Negative (Red)"
                    :value "neg1"
                    :placeholder "Negative flavor"
                    :style {:min-width "180px"}}
      [:option {:value "neg1"} "Negative Option 1"]
      [:option {:value "neg2"} "Negative Option 2"]]]

    [:div
     [:ty-dropdown {:flavor "primary"
                    :label "Important (Blue)"
                    :value "imp1"
                    :placeholder "Important flavor"
                    :style {:min-width "180px"}}
      [:option {:value "imp1"} "Important Option 1"]
      [:option {:value "imp2"} "Important Option 2"]]]

    [:div
     [:ty-dropdown {:flavor "warning"
                    :label "Exception (Yellow)"
                    :value "exc1"
                    :placeholder "Exception flavor"
                    :style {:min-width "180px"}}
      [:option {:value "exc1"} "Exception Option 1"]
      [:option {:value "exc2"} "Exception Option 2"]]]

    [:div
     [:ty-dropdown {:flavor "secondary"
                    :label "Unique (Purple)"
                    :value "unq1"
                    :placeholder "Unique flavor"
                    :style {:min-width "180px"}}
      [:option {:value "unq1"} "Unique Option 1"]
      [:option {:value "unq2"} "Unique Option 2"]]]]

   (code-snippet "<ty-dropdown flavor=\"success\" placeholder=\"Positive flavor\">
  <option value=\"pos1\">Positive Option 1</option>
  <option value=\"pos2\">Positive Option 2</option>
</ty-dropdown>

<ty-dropdown flavor=\"danger\" placeholder=\"Negative flavor\">
  <option value=\"neg1\">Negative Option 1</option>
</ty-dropdown>")])

(defn search-examples []
  [:div.demo-section
   [:h2.demo-title "Search & Filtering"]
   [:p.ty-text-.mb-6
    "Powerful search functionality helps users find options quickly"]

   (demo-row {:title "Programming Languages"
              :description "Search through programming languages"
              :children [[:div.max-w-sm
                          [:ty-dropdown {:value "clojure"
                                         :placeholder "Search languages..."
                                         :style {:min-width "220px"}
                                         :on {:change dropdown-event-handler}}
                           [:option {:value "javascript"} "JavaScript"]
                           [:option {:value "typescript"} "TypeScript"]
                           [:option {:value "python"} "Python"]
                           [:option {:value "java"} "Java"]
                           [:option {:value "clojure"} "Clojure"]
                           [:option {:value "clojurescript"} "ClojureScript"]
                           [:option {:value "rust"} "Rust"]
                           [:option {:value "go"} "Go"]
                           [:option {:value "kotlin"} "Kotlin"]
                           [:option {:value "swift"} "Swift"]
                           [:option {:value "csharp"} "C#"]
                           [:option {:value "cpp"} "C++"]
                           [:option {:value "c"} "C"]
                           [:option {:value "ruby"} "Ruby"]
                           [:option {:value "php"} "PHP"]
                           [:option {:value "scala"} "Scala"]
                           [:option {:value "haskell"} "Haskell"]
                           [:option {:value "elixir"} "Elixir"]
                           [:option {:value "erlang"} "Erlang"]
                           [:option {:value "dart"} "Dart"]]]]})

   (demo-row {:title "Web Frameworks"
              :description "Type to filter frameworks"
              :children [[:div.max-w-sm
                          [:ty-dropdown {:value "react"
                                         :placeholder "Search frameworks..."
                                         :style {:min-width "200px"}
                                         :on {:change dropdown-event-handler}}
                           [:option {:value "react"} "React"]
                           [:option {:value "vue"} "Vue.js"]
                           [:option {:value "angular"} "Angular"]
                           [:option {:value "svelte"} "Svelte"]
                           [:option {:value "solid"} "SolidJS"]
                           [:option {:value "lit"} "Lit"]
                           [:option {:value "stencil"} "Stencil"]
                           [:option {:value "alpine"} "Alpine.js"]
                           [:option {:value "htmx"} "HTMX"]
                           [:option {:value "stimulus"} "Stimulus"]]]]})

   (code-snippet "<ty-dropdown placeholder=\"Search languages...\" style=\"min-width: 220px;\">
  <option value=\"javascript\">JavaScript</option>
  <option value=\"python\">Python</option>
  <option value=\"clojure\">Clojure</option>
</ty-dropdown>")])

(defn blur-clear-demo []
  [:div.demo-section
   [:h2.demo-title "Blur Clear Search (Desktop Only)"]
   [:p.ty-text-.mb-6
    "On desktop, clicking outside the search input clears the search and shows all options. This feature helps users reset their search without having to manually clear the input."]

   [:div.space-y-6
    [:div
     [:h3.demo-subtitle "Try it out:"]
     [:ol.list-decimal.list-inside.text-sm.text-gray-600.dark:text-gray-400.mb-4.space-y-1
      [:li "Open the dropdown below"]
      [:li "Type something to filter the options (e.g., \"java\")"]
      [:li "Click outside the search input (but keep dropdown open)"]
      [:li "Notice the search clears and all options are visible again"]]

     [:div.max-w-sm
      [:ty-dropdown {:value "clojure"
                     :placeholder "Search to test blur clear..."
                     :on {:change dropdown-event-handler}}
       [:option {:value "javascript"} "JavaScript"]
       [:option {:value "typescript"} "TypeScript"]
       [:option {:value "python"} "Python"]
       [:option {:value "java"} "Java"]
       [:option {:value "clojure"} "Clojure"]
       [:option {:value "clojurescript"} "ClojureScript"]
       [:option {:value "rust"} "Rust"]
       [:option {:value "go"} "Go"]
       [:option {:value "kotlin"} "Kotlin"]
       [:option {:value "swift"} "Swift"]
       [:option {:value "csharp"} "C#"]
       [:option {:value "cpp"} "C++"]
       [:option {:value "ruby"} "Ruby"]
       [:option {:value "php"} "PHP"]
       [:option {:value "scala"} "Scala"]
       [:option {:value "haskell"} "Haskell"]
       [:option {:value "elixir"} "Elixir"]]]]

    [:div {:class "bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 rounded-lg p-4"}
     [:h4.text-sm.font-medium.text-blue-900.dark:text-blue-100.mb-2 "Platform Behavior"]
     [:ul.text-sm.text-blue-700.dark:text-blue-200.space-y-1
      [:li "📱 " [:strong "Mobile:"] " Search is cleared when modal closes"]
      [:li "🖥️ " [:strong "Desktop:"] " Search is cleared on input blur (this demo)"]]]]

   (code-snippet "<!-- Desktop blur clear demo -->
<ty-dropdown placeholder=\"Search to test blur clear...\">
  <option value=\"javascript\">JavaScript</option>
  <option value=\"python\">Python</option>
  <!-- Type to filter, then click outside input to clear -->
</ty-dropdown>")])

(defn rich-html-demo []
  [:div.demo-section
   [:h2.demo-title "Rich HTML Content in Options"]
   [:p.text-gray-600.dark:text-gray-400.mb-6
    "Options can contain rich HTML with icons, badges, formatting, and styling. The selected option displays with full styling preserved in the dropdown stub."]

   [:div.space-y-8
    ;; Programming Languages with Icons
    [:div
     [:h3.demo-subtitle "Programming Languages with Icons"]
     [:p.text-sm.text-gray-600.dark:text-gray-400.mb-3
      "Each option includes an icon and formatted description. Notice how the full styling appears in the stub when selected."]

     [:div.max-w-xl
      [:ty-dropdown {:value "clojure"
                     :placeholder "Choose a programming language..."
                     :on {:change dropdown-event-handler}}
       [:ty-option {:value "javascript"}
        [:div.flex.items-center.gap-3
         [:div.w-6.h-6.rounded.bg-yellow-400.flex.items-center.justify-center.ty-text++.text-xs.font-bold "JS"]
         [:div
          [:div.font-medium.ty-text "JavaScript"]
          [:div.text-sm.ty-text-- "Dynamic scripting language"]]]]

       [:ty-option {:value "typescript"}
        [:div.flex.items-center.gap-3
         [:div.w-6.h-6.rounded.bg-blue-600.flex.items-center.justify-center.ty-text++ "TS"]
         [:div
          [:div.font-medium.ty-text "TypeScript"]
          [:div.text-sm.ty-text-- "JavaScript with static typing"]]]]

       [:ty-option {:value "clojure"}
        [:div.flex.items-center.gap-3
         [:div.w-6.h-6.rounded.bg-green-600.flex.items-center.justify-center.ty-text++.text-xs.font-bold "λ"]
         [:div
          [:div.font-medium.ty-text "Clojure"]
          [:div.text-sm.ty-text-- "Functional Lisp for the JVM"]]]]

       [:ty-option {:value "rust"}
        [:div.flex.items-center.gap-3
         [:div.w-6.h-6.rounded.bg-orange-600.flex.items-center.justify-center.ty-text++.text-xs.font-bold "R"]
         [:div
          [:div.font-medium.ty-text "Rust"]
          [:div.text-sm.ty-text-- "Memory-safe systems programming"]]]]

       [:ty-option {:value "python"}
        [:div.flex.items-center.gap-3
         [:div.w-6.h-6.rounded.bg-blue-500.flex.items-center.justify-center.ty-text++.text-xs.font-bold "Py"]
         [:div
          [:div.font-medium.ty-text "Python"]
          [:div.text-sm.ty-text-- "Versatile high-level language"]]]]]]]

    ;; Team Members with Avatars and Status
    [:div
     [:h3.demo-subtitle "Team Members with Status"]
     [:p.text-sm.text-gray-600.dark:text-gray-400.mb-3
      "Options with profile pictures, names, roles, and online status indicators."]

     [:div.max-w-xl
      [:ty-dropdown {:value "alice"
                     :placeholder "Assign to team member..."
                     :on {:change dropdown-event-handler}}
       [:ty-option {:value "alice"}
        [:div.flex.items-center.gap-3
         [:div.relative.flex-shrink-0
          [:div.w-8.h-8.rounded-full.bg-blue-500.flex.items-center.justify-center.text-white.font-medium "A"]
          [:div.absolute.-top-1.-right-1.w-3.h-3.bg-green-500.rounded-full.border-2.border-white]]
         [:div.flex-1.min-w-0
          [:div.font-medium "Alice Johnson"]
          [:div.text-sm.ty-text "Senior Developer"]]
         [:span.px-2.py-1.text-xs.bg-green-100.text-green-800.rounded-full.flex-shrink-0 "Online"]]]

       [:ty-option {:value "bob"}
        [:div.flex.items-center.gap-3
         [:div.relative.flex-shrink-0
          [:div.w-8.h-8.rounded-full.bg-purple-500.flex.items-center.justify-center.text-white.font-medium "B"]
          [:div.absolute.-top-1.-right-1.w-3.h-3.bg-yellow-500.rounded-full.border-2.border-white]]
         [:div.flex-1.min-w-0
          [:div.font-medium "Bob Smith"]
          [:div.text-sm.ty-text "Product Manager"]]
         [:span.px-2.py-1.text-xs.bg-yellow-100.text-yellow-800.rounded-full.flex-shrink-0 "Away"]]]

       [:ty-option {:value "carol"}
        [:div.flex.items-center.gap-3
         [:div.relative.flex-shrink-0
          [:div.w-8.h-8.rounded-full.bg-pink-500.flex.items-center.justify-center.text-white.font-medium "C"]
          [:div.absolute.-top-1.-right-1.w-3.h-3.bg-gray-400.rounded-full.border-2.border-white]]
         [:div.flex-1.min-w-0
          [:div.font-medium "Carol Davis"]
          [:div.text-sm.ty-text "UX Designer"]]
         [:span.px-2.py-1.text-xs.bg-gray-100.text-gray-800.rounded-full.flex-shrink-0 "Offline"]]]]]]

    ;; System Status with Indicators
    [:div
     [:h3.demo-subtitle "System Status with Indicators"]
     [:p.text-sm.text-gray-600.dark:text-gray-400.mb-3
      "Options showing system status with colored indicators and metrics."]

     [:div.max-w-xl
      [:ty-dropdown {:value "prod"
                     :placeholder "Select environment..."
                     :on {:change dropdown-event-handler}}
       [:ty-option {:value "prod"}
        [:div.flex.items-center.gap-3
         [:div.w-3.h-3.bg-green-500.rounded-full]
         [:div.flex-1
          [:div.font-medium "Production"]
          [:div.text-sm.ty-text "99.9% uptime • 1.2k users"]]
         [:span.px-2.py-1.text-xs.bg-green-100.text-green-800.rounded "Healthy"]]]

       [:ty-option {:value "staging"}
        [:div.flex.items-center.gap-3
         [:div.w-3.h-3.bg-yellow-500.rounded-full]
         [:div.flex-1
          [:div.font-medium "Staging"]
          [:div.text-sm.ty-text "95.2% uptime • 12 users"]]
         [:span.px-2.py-1.text-xs.bg-yellow-100.text-yellow-800.rounded "Warning"]]]

       [:ty-option {:value "dev"}
        [:div.flex.items-center.gap-3
         [:div.w-3.h-3.bg-red-500.rounded-full]
         [:div.flex-1
          [:div.font-medium "Development"]
          [:div.text-sm.ty-text "78.1% uptime • 3 users"]]
         [:span.px-2.py-1.text-xs.bg-red-100.text-red-800.rounded "Error"]]]]]]

    ;; Files with Icons and Metadata
    [:div
     [:h3.demo-subtitle "Files with Metadata"]
     [:p.text-sm.text-gray-600.dark:text-gray-400.mb-3
      "File picker with file type icons, sizes, and modification dates."]

     [:div.max-w-xl
      [:ty-dropdown {:value "report.pdf"
                     :placeholder "Choose a file..."
                     :on {:change dropdown-event-handler}}
       [:ty-option {:value "report.pdf"}
        [:div.flex.items-center.gap-3
         [:div.w-8.h-8.bg-red-100.rounded.flex.items-center.justify-center
          [:span.text-red-600.text-xs.font-bold "PDF"]]
         [:div.flex-1
          [:div.font-medium "Annual Report 2024.pdf"]
          [:div.text-sm.ty-text "2.4 MB • Modified 2 hours ago"]]]]

       [:ty-option {:value "spreadsheet.xlsx"}
        [:div.flex.items-center.gap-3
         [:div.w-8.h-8.bg-green-100.rounded.flex.items-center.justify-center
          [:span.text-green-600.text-xs.font-bold "XLS"]]
         [:div.flex-1
          [:div.font-medium "Budget_Q4.xlsx"]
          [:div.text-sm.ty-text "856 KB • Modified yesterday"]]]]

       [:ty-option {:value "presentation.pptx"}
        [:div.flex.items-center.gap-3
         [:div.w-8.h-8.bg-orange-100.rounded.flex.items-center.justify-center
          [:span.text-orange-600.text-xs.font-bold "PPT"]]
         [:div.flex-1
          [:div.font-medium "Project_Kickoff.pptx"]
          [:div.text-sm.ty-text "12.3 MB • Modified last week"]]]]]]]

    ;; Benefits callout
    [:div {:class "bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 rounded-lg p-6"}
     [:h4.text-lg.font-medium.text-blue-900.dark:text-blue-100.mb-3 "Rich Content Benefits"]
     [:ul.text-sm.text-blue-700.dark:text-blue-200.space-y-2
      [:li "🎨 " [:strong "Full styling preserved:"] " Icons, colors, layouts display perfectly"]
      [:li "🔍 " [:strong "Search still works:"] " Filters on text content while showing rich UI"]
      [:li "📱 " [:strong "Mobile & desktop:"] " Rich content works on all devices"]
      [:li "⚡ " [:strong "Performance optimized:"] " Uses efficient DOM cloning"]
      [:li "♿ " [:strong "Accessible:"] " Screen readers see meaningful text content"]]]]

   (code-snippet "<!-- Rich HTML options example -->
<ty-dropdown value=\"clojure\" placeholder=\"Choose language...\">
  <ty-option value=\"javascript\">
    <div class=\"flex items-center gap-3\">
      <div class=\"w-6 h-6 rounded bg-yellow-400 flex items-center justify-center text-black text-xs font-bold\">JS</div>
      <div>
        <div class=\"font-medium\">JavaScript</div>
        <div class=\"text-sm ty-text\">Dynamic scripting language</div>
      </div>
    </div>
  </ty-option>
  <!-- More rich options... -->
</ty-dropdown>")])

(defn sizing-flavors-demo []
  [:div.demo-section
   [:h2.demo-title "Custom Sizing Examples"]
   [:p.text-gray-600.dark:text-gray-400.mb-6
    "Use inline styles or CSS classes to customize dropdown sizing and appearance."]

   [:div.space-y-8
    ;; Size Examples
    [:div
     [:h3.demo-subtitle "Different Widths"]
     [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-4
      ;; Compact
      [:div
       [:ty-dropdown {:placeholder "Compact..."
                      :label "Compact (160px)"
                      :style {:min-width "160px"
                              :font-size "0.875rem"}}
        [:ty-option {:value "react"}
         [:div.flex.items-center.gap-2
          [:div.w-4.h-4.bg-blue-500.rounded.flex.items-center.justify-center.text-white.text-xs "R"]
          [:span.text-sm "React"]]]
        [:ty-option {:value "vue"}
         [:div.flex.items-center.gap-2
          [:div.w-4.h-4.bg-green-500.rounded.flex.items-center.justify-center.text-white.text-xs "V"]
          [:span.text-sm "Vue"]]]]]

      ;; Standard
      [:div
       [:ty-dropdown {:placeholder "Standard..."
                      :label "Standard (200px)"
                      :style {:min-width "200px"}}
        [:ty-option {:value "react"}
         [:div.flex.items-center.gap-2
          [:div.w-5.h-5.bg-blue-500.rounded.flex.items-center.justify-center.text-white.text-xs "R"]
          [:span "React"]]]
        [:ty-option {:value "vue"}
         [:div.flex.items-center.gap-2
          [:div.w-5.h-5.bg-green-500.rounded.flex.items-center.justify-center.text-white.text-xs "V"]
          [:span "Vue"]]]]]

      ;; Wide
      [:div
       [:ty-dropdown {:placeholder "Wide dropdown..."
                      :label "Wide (300px)"
                      :style {:min-width "300px"
                              :font-size "1rem"}}
        [:ty-option {:value "react"}
         [:div.flex.items-center.gap-3
          [:div.w-6.h-6.bg-blue-500.rounded.flex.items-center.justify-center.text-white.text-sm "R"]
          [:span.text-lg "React"]]]
        [:ty-option {:value "vue"}
         [:div.flex.items-center.gap-3
          [:div.w-6.h-6.bg-green-500.rounded.flex.items-center.justify-center.text-white.text-sm "V"]
          [:span.text-lg "Vue"]]]]]]]

    ;; Combined Example
    [:div
     [:h3.demo-subtitle "Large Form Example"]
     [:div.max-w-2xl
      [:ty-dropdown {:placeholder "Select a web framework..."
                     :label "Choose your preferred framework"
                     :style {:min-width "400px"
                             :font-size "1.1rem"
                             :min-height "3rem"}}
       [:ty-option {:value "react"}
        [:div.flex.items-center.gap-4
         [:div.w-10.h-10.bg-blue-100.rounded-lg.flex.items-center.justify-center
          [:span.text-blue-600.text-xl "⚛️"]]
         [:div
          [:div.font-medium.text-lg "React"]
          [:div.text-sm.ty-text "A JavaScript library for building user interfaces"]]]]
       [:ty-option {:value "vue"}
        [:div.flex.items-center.gap-4
         [:div.w-10.h-10.bg-green-100.rounded-lg.flex.items-center.justify-center
          [:span.text-green-600.text-xl "🟢"]]
         [:div
          [:div.font-medium.text-lg "Vue.js"]
          [:div.text-sm.ty-text "The progressive JavaScript framework"]]]]
       [:ty-option {:value "svelte"}
        [:div.flex.items-center.gap-4
         [:div.w-10.h-10.bg-orange-100.rounded-lg.flex.items-center.justify-center
          [:span.text-orange-600.text-xl "🔥"]]
         [:div
          [:div.font-medium.text-lg "Svelte"]
          [:div.text-sm.ty-text "Cybernetically enhanced web apps"]]]]]]]]

   (code-snippet "<!-- Custom sizing with inline styles -->
<ty-dropdown placeholder=\"Compact...\" style=\"min-width: 160px; font-size: 0.875rem;\">
  <ty-option value=\"react\">React</ty-option>
</ty-dropdown>

<ty-dropdown placeholder=\"Large...\" style=\"min-width: 400px; font-size: 1.1rem; min-height: 3rem;\">
  <ty-option value=\"special\">
    <div class=\"flex items-center gap-4\">
      <div class=\"w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center\">
        <span class=\"text-blue-600 text-xl\">⚛️</span>
      </div>
      <div>
        <div class=\"font-medium text-lg\">React</div>
        <div class=\"text-sm ty-text\">A JavaScript library for building user interfaces</div>
      </div>
    </div>
  </ty-option>
</ty-dropdown>")])

(defn htmx-reactive-demo []
  [:div.demo-section
   [:h2.demo-title "🚀 HTMX & Reactive Integration"]
   [:p.text-gray-600.dark:text-gray-400.mb-6
    "Demonstrates the new reactive dropdown capabilities with HTMX form integration, property/attribute reactivity, and framework-ready behavior."]

   ;; Form Integration Demo
   (demo-row {:title "Form Integration & Serialization"
              :description "Dropdowns with name attributes participate in form data serialization"
              :children [[:div.max-w-2xl.w-full
                          [:form.space-y-4.p-6.ty-elevated.rounded-lg
                           [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
                            [:ty-dropdown {:name "category"
                                           :label "Category"
                                           :value "tech"
                                           :required true
                                           :style {:min-width "180px"}
                                           :on {:change dropdown-event-handler}}
                             [:option {:value "tech"} "Technology"]
                             [:option {:value "design"} "Design"]
                             [:option {:value "business"} "Business"]
                             [:option {:value "marketing"} "Marketing"]]

                            [:ty-dropdown {:name "priority"
                                           :label "Priority"
                                           :value "high"
                                           :style {:min-width "150px"}
                                           :on {:change dropdown-event-handler}}
                             [:option {:value "low"} "Low"]
                             [:option {:value "medium"} "Medium"]
                             [:option {:value "high"} "High"]
                             [:option {:value "critical"} "Critical"]]]

                           [:button.ty-button.ty-button-primary {:type "button"
                                                                 :on {:click #(let [form (.-target %)
                                                                                    form (-> % .-target (.closest "form"))
                                                                                    form-data (js/FormData. form)
                                                                                    data (js/Object.fromEntries form-data)]
                                                                                (js/console.log "Form Data:" data)
                                                                                (js/alert (str "Form Data: " (js/JSON.stringify data nil 2))))}}
                            "Test Form Serialization"]

                           [:div.text-sm.ty-text-
                            "Click the button to see form data in console and alert. The dropdowns automatically participate in form serialization with their " [:code.ty-content.px-1.py-0.5.rounded "name"] " attributes."]]]]})

   (code-snippet "<!-- Form integration with name attributes -->
<form>
  <ty-dropdown name=\"category\" label=\"Category\" value=\"tech\" required>
    <option value=\"tech\">Technology</option>
    <option value=\"design\">Design</option>
  </ty-dropdown>
  
  <ty-dropdown name=\"priority\" label=\"Priority\" value=\"high\">
    <option value=\"low\">Low</option>
    <option value=\"high\">High</option>
  </ty-dropdown>
  
  <button type=\"submit\">Submit</button>
</form>

<script>
// Form data automatically includes dropdown values:
// { category: 'tech', priority: 'high' }
</script>")

   ;; Property/Attribute Reactivity Demo
   (demo-row {:title "Property & Attribute Reactivity"
              :description "External JavaScript can now set values and trigger reactive updates"
              :children [[:div.max-w-2xl.w-full
                          [:div.space-y-4
                           [:div.p-6.ty-elevated.rounded-lg
                            [:ty-dropdown {:id "reactive-dropdown"
                                           :name "reactive-test"
                                           :label "Reactive Test Dropdown"
                                           :value "option1"
                                           :style {:min-width "220px"}
                                           :on {:change dropdown-event-handler}}
                             [:option {:value "option1"} "Option 1"]
                             [:option {:value "option2"} "Option 2"]
                             [:option {:value "option3"} "Option 3"]
                             [:option {:value "option4"} "Option 4"]
                             [:option {:value "option5"} "Option 5"]]

                            [:div.mt-4.text-sm.ty-text-
                             "Current value: "
                             [:code.ty-content.px-2.py-1.rounded.text-xs {:id "reactive-value-display"}
                              "option1"]]]

                           [:div.grid.grid-cols-2.md:grid-cols-4.gap-2
                            [:button.ty-button.ty-button-sm {:on {:click #(let [dropdown (js/document.getElementById "reactive-dropdown")
                                                                                display (js/document.getElementById "reactive-value-display")]
                                                                            (set! (.-value dropdown) "option2")
                                                                            (set! (.-textContent display) "option2")
                                                                            (js/console.log "Set property: dropdown.value = 'option2'"))}}
                             "Set Property"]
                            [:button.ty-button.ty-button-sm {:on {:click #(let [dropdown (js/document.getElementById "reactive-dropdown")
                                                                                display (js/document.getElementById "reactive-value-display")]
                                                                            (.setAttribute dropdown "value" "option3")
                                                                            (set! (.-textContent display) "option3")
                                                                            (js/console.log "Set attribute: dropdown.setAttribute('value', 'option3')"))}}
                             "Set Attribute"]
                            [:button.ty-button.ty-button-sm {:on {:click #(let [dropdown (js/document.getElementById "reactive-dropdown")
                                                                                display (js/document.getElementById "reactive-value-display")]
                                                                            (set! (.-value dropdown) "option4")
                                                                            (set! (.-textContent display) "option4")
                                                                            (js/console.log "Property update: dropdown.value = 'option4'"))}}
                             "Property Update"]
                            [:button.ty-button.ty-button-sm {:on {:click #(let [dropdown (js/document.getElementById "reactive-dropdown")
                                                                                display (js/document.getElementById "reactive-value-display")]
                                                                            (.setAttribute dropdown "value" "option5")
                                                                            (set! (.-textContent display) "option5")
                                                                            (js/console.log "Attribute update: dropdown.setAttribute('value', 'option5')"))}}
                             "Attr Update"]]

                           [:div.p-4.ty-bg-info-.rounded-lg
                            [:h4.font-semibold.ty-text-info.mb-2 "✨ Reactive Features"]
                            [:ul.text-sm.ty-text-info.space-y-1
                             [:li "🔄 " [:strong "Property Reactivity:"] " " [:code.ty-content.px-1.py-0.5.rounded.text-xs "dropdown.value = 'newValue'"]]
                             [:li "📝 " [:strong "Attribute Reactivity:"] " " [:code.ty-content.px-1.py-0.5.rounded.text-xs "dropdown.setAttribute('value', 'newValue')"]]
                             [:li "⚡ " [:strong "Smart Updates:"] " Only re-renders when value actually changes"]
                             [:li "🎯 " [:strong "Framework Ready:"] " Works with React, Vue, Angular property binding"]
                             [:li "📊 " [:strong "State Sync:"] " Internal state stays synchronized with DOM"]]]]]]})

   (code-snippet "// Property reactivity (NEW!)
const dropdown = document.querySelector('ty-dropdown');
dropdown.value = 'newValue'; // ✅ Triggers state update & re-render

// Attribute reactivity (NEW!)
dropdown.setAttribute('value', 'anotherValue'); // ✅ Also works

// Framework integration (NEW!)
// React: <ty-dropdown value={dynamicValue} onChange={handler} />
// Vue: <ty-dropdown :value=\"dynamicValue\" @change=\"handler\" />
// Angular: <ty-dropdown [value]=\"dynamicValue\" (change)=\"handler($event)\" />")

   ;; HTMX Integration Demo
   (demo-row
    {:title "HTMX Integration Simulation"
     :description "Simulates HTMX form submission patterns (would work with real HTMX server)"
     :children [[:div.max-w-2xl.w-full
                 [:div.space-y-4
                  [:form.p-6.ty-elevated.rounded-lg
                   {:on {:submit #(do
                                    (.preventDefault %)
                                    (let [form (.-target %)
                                          form-data (js/FormData. form)
                                          data (js/Object.fromEntries form-data)
                                          result-div (js/document.getElementById "htmx-result")]
                                      (set! (.-innerHTML result-div)
                                            (str "<div class=\"p-4 ty-bg-success- rounded border-l-4 border-green-500\">"
                                                 "<h4 class=\"font-semibold ty-text-success mb-2\">✅ Form Submitted Successfully</h4>"
                                                 "<pre class=\"text-sm ty-text-success overflow-x-auto\">"
                                                 (js/JSON.stringify data nil 2)
                                                 "</pre>"
                                                 "<p class=\"text-xs ty-text-success mt-2\">In real HTMX: hx-post=\"/api/endpoint\" hx-target=\"#result\"</p>"
                                                 "</div>"))
                                      (js/console.log "HTMX simulation - would POST to server:" data)))}}
                   [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
                    [:ty-dropdown {:name "department"
                                   :label "Department"
                                   :value "engineering"
                                   :required true
                                   :flavor "primary"
                                   :style {:min-width "180px"}}
                     [:option {:value "engineering"} "🔧 Engineering"]
                     [:option {:value "design"} "🎨 Design"]
                     [:option {:value "marketing"} "📢 Marketing"]
                     [:option {:value "sales"} "💼 Sales"]
                     [:option {:value "hr"} "👥 Human Resources"]]

                    [:ty-dropdown {:name "role"
                                   :label "Role"
                                   :value "developer"
                                   :required true
                                   :flavor "secondary"
                                   :style {:min-width "180px"}}
                     [:option {:value "intern"} "👨‍💻 Intern"]
                     [:option {:value "developer"} "⚡ Developer"]
                     [:option {:value "senior"} "🚀 Senior Developer"]
                     [:option {:value "lead"} "👑 Team Lead"]
                     [:option {:value "manager"} "📊 Manager"]]]

                   [:div.mt-6.flex.justify-center.grow
                    [:ty-button {:type "submit"}
                     "Submit Form (HTMX Simulation)"]]]

                  [:div#htmx-result.mt-4]

                  [:div.mt-4.p-4.bg-amber-50.dark:bg-amber-900.border.border-amber-200.dark:border-amber-700.rounded-lg
                   [:h4.font-semibold.text-amber-800.dark:text-amber-200.mb-2 "🔗 Real HTMX Usage"]
                   [:pre.text-xs.text-amber-700.dark:text-amber-300.overflow-x-auto
                    "<!-- Real HTMX attributes -->\n<form hx-post=\"/api/form/submit\" \n      hx-target=\"#result\" \n      hx-indicator=\"#loading\">\n  \n  <ty-dropdown name=\"department\" required>\n    <option value=\"engineering\">Engineering</option>\n  </ty-dropdown>\n  \n  <button type=\"submit\">Submit</button>\n</form>\n\n<!-- Server receives: { department: 'engineering', role: 'developer' } -->"]]]]]})

   (code-snippet "<!-- HTMX form integration -->
<form hx-post=\"/api/form/submit\" hx-target=\"#result\">
  <ty-dropdown name=\"category\" label=\"Category\" required>
    <option value=\"tech\">Technology</option>
    <option value=\"design\">Design</option>
  </ty-dropdown>
  
  <ty-dropdown name=\"priority\" label=\"Priority\">
    <option value=\"high\">High</option>
    <option value=\"low\">Low</option>
  </ty-dropdown>
  
  <button type=\"submit\">Submit</button>
</form>

<!-- Server automatically receives form data -->
<!-- POST /api/form/submit -->
<!-- Content-Type: application/x-www-form-urlencoded -->
<!-- category=tech&priority=high -->")

   ;; Framework Integration Demo
   (demo-row {:title "Framework Integration Examples"
              :description "Code examples showing how the reactive dropdown works with popular frameworks"
              :children [[:div.w-full.space-y-6
                          ;; React Example
                          [:div.p-6.ty-elevated.rounded-lg
                           [:h4.font-semibold.mb-3.flex.items-center.gap-2
                            [:span.text-blue-500 "⚛️"] "React Integration"]
                           [:pre.code-block.text-sm.overflow-x-auto
                            "// React component with reactive ty-dropdown\nfunction MyComponent() {\n  const [category, setCategory] = useState('tech');\n  const [priority, setPriority] = useState('high');\n\n  return (\n    <form>\n      <ty-dropdown \n        value={category}\n        onChange={e => setCategory(e.detail.value)}\n        name=\"category\"\n        label=\"Category\"\n        required>\n        <option value=\"tech\">Technology</option>\n        <option value=\"design\">Design</option>\n      </ty-dropdown>\n      \n      <ty-dropdown \n        value={priority}\n        onChange={e => setPriority(e.detail.value)}\n        name=\"priority\"\n        label=\"Priority\">\n        <option value=\"high\">High</option>\n        <option value=\"low\">Low</option>\n      </ty-dropdown>\n    </form>\n  );\n}"]]

                          ;; Vue Example  
                          [:div.p-6.ty-elevated.rounded-lg
                           [:h4.font-semibold.mb-3.flex.items-center.gap-2
                            [:span.text-green-500 "🟢"] "Vue Integration"]
                           [:pre.code-block.text-sm.overflow-x-auto
                            "<!-- Vue component with reactive ty-dropdown -->\n<template>\n  <form>\n    <ty-dropdown \n      :value=\"category\"\n      @change=\"category = $event.detail.value\"\n      name=\"category\"\n      label=\"Category\"\n      required>\n      <option value=\"tech\">Technology</option>\n      <option value=\"design\">Design</option>\n    </ty-dropdown>\n    \n    <ty-dropdown \n      :value=\"priority\"\n      @change=\"priority = $event.detail.value\"\n      name=\"priority\"\n      label=\"Priority\">\n      <option value=\"high\">High</option>\n      <option value=\"low\">Low</option>\n    </ty-dropdown>\n  </form>\n</template>\n\n<script>\nexport default {\n  data() {\n    return {\n      category: 'tech',\n      priority: 'high'\n    }\n  }\n}\n</script>"]]

                          ;; Angular Example
                          [:div.p-6.ty-elevated.rounded-lg
                           [:h4.font-semibold.mb-3.flex.items-center.gap-2
                            [:span.text-red-500 "🅰️"] "Angular Integration"]
                           [:pre.code-block.text-sm.overflow-x-auto
                            "// Angular component with reactive ty-dropdown\n@Component({\n  selector: 'app-form',\n  template: `\n    <form>\n      <ty-dropdown \n        [value]=\"category\"\n        (change)=\"onCategoryChange($event)\"\n        name=\"category\"\n        label=\"Category\"\n        required>\n        <option value=\"tech\">Technology</option>\n        <option value=\"design\">Design</option>\n      </ty-dropdown>\n      \n      <ty-dropdown \n        [value]=\"priority\"\n        (change)=\"onPriorityChange($event)\"\n        name=\"priority\"\n        label=\"Priority\">\n        <option value=\"high\">High</option>\n        <option value=\"low\">Low</option>\n      </ty-dropdown>\n    </form>\n  `\n})\nexport class FormComponent {\n  category = 'tech';\n  priority = 'high';\n  \n  onCategoryChange(event: CustomEvent) {\n    this.category = event.detail.value;\n  }\n  \n  onPriorityChange(event: CustomEvent) {\n    this.priority = event.detail.value;\n  }\n}"]]]]})

   ;; Benefits Summary
   [:div.mt-8.p-6.ty-bg-success-.rounded-lg
    [:h3.text-xl.font-semibold.mb-4.ty-text-success "🎯 Migration Benefits Summary"]
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
     [:div
      [:h4.font-semibold.mb-3.ty-text-success "✨ New Reactive Features"]
      [:ul.text-sm.space-y-2.ty-text-success
       [:li "🔄 Property & attribute reactivity"]
       [:li "⚡ Smart re-rendering optimization"]
       [:li "🎯 Framework integration ready"]
       [:li "📝 Form data serialization"]
       [:li "🌐 HTMX compatibility"]
       [:li "🔄 External state management"]]]
     [:div
      [:h4.font-semibold.mb-3.ty-text-success "🏗️ Technical Improvements"]
      [:ul.text-sm.space-y-2.ty-text-success
       [:li "📦 Self-contained functions"]
       [:li "🧠 Reactive state management"]
       [:li "🎨 Consistent with other components"]
       [:li "⚙️ Batched delta processing"]
       [:li "🔧 Property/attribute sync"]
       [:li "📊 Better developer experience"]]]]]])

(defn event-debugging []
  [:div.demo-section
   [:h2.demo-title "Event Debugging"]
   [:p.text-gray-600.dark:text-gray-400.mb-4
    "Open the browser console to see change events. Current value: "]
   [:code.bg-gray-100.dark:bg-gray-800.px-2.py-1.rounded.text-sm
    (str (::dropdown-value @state/state))]

   [:div.mt-6.max-w-sm
    [:ty-dropdown {:value (::dropdown-value @state/state "")
                   :placeholder "Select to see events..."
                   :on {:change dropdown-event-handler}}
     [:option {:value "event1"} "Event Test 1"]
     [:option {:value "event2"} "Event Test 2"]
     [:option {:value "event3"} "Event Test 3"]
     [:option {:value "event4"} "Event Test 4"]]]

   (code-snippet "const handleChange = (event) => {
  const { value, text } = event.detail;
  console.log('Dropdown changed:', { value, text });
};

<ty-dropdown on:change={handleChange}>
  <option value=\"event1\">Event Test 1</option>
</ty-dropdown>")])

(defn showmodal-z-index-tests
  "Test dropdowns in challenging CSS environments that would break fixed positioning"
  []
  [:div.demo-section.mb-12
   [:h2.demo-title.text-xl.font-semibold.mb-4.text-blue-600 "🚀 showModal() Z-Index Resistance Tests"]
   [:p.text-gray-600.mb-6
    "These tests verify the dropdown works in CSS environments that create stacking contexts and would break traditional fixed positioning."]

   ;; Transform context test
   [:div.test-scenario.mb-8
    [:h3.font-semibold.mb-3 "1. Transform Context (scale + rotate)"]
    [:p.text-sm.text-gray-500.mb-3 "Dropdown inside element with CSS transforms - would break fixed positioning"]
    [:div {:style {:transform "scale(0.9) rotate(1deg)"
                   :background "linear-gradient(45deg, #f0f9ff, #ecfeff)"
                   :padding "1.5rem"
                   :border-radius "12px"
                   :border "2px solid #0ea5e9"}}
     [:ty-dropdown
      {:placeholder "Select inside transform context"
       :style {:width "250px"}}
      [:option {:value "transform-1"} "Transform Test Option 1"]
      [:option {:value "transform-2"} "Transform Test Option 2"]
      [:option {:value "transform-3"} "Transform Test Option 3"]
      [:option {:value "transform-4"} "Should appear above everything ✨"]]]]

   ;; Overflow hidden test
   [:div.test-scenario.mb-8
    [:h3.font-semibold.mb-3 "2. Overflow Hidden Container"]
    [:p.text-sm.text-gray-500.mb-3 "Dropdown inside container with overflow: hidden - would be clipped"]
    [:div {:style {:overflow "hidden"
                   :height "120px"
                   :background "#fef3c7"
                   :padding "1rem"
                   :border-radius "8px"
                   :border "2px solid #f59e0b"}}
     [:ty-dropdown
      {:placeholder "Select in overflow:hidden"
       :style {:width "220px"}}
      [:option {:value "overflow-1"} "Overflow Test 1"]
      [:option {:value "overflow-2"} "Overflow Test 2"]
      [:option {:value "overflow-3"} "Should not be clipped! 🎯"]]]]

   ;; High z-index competition
   [:div.test-scenario.mb-8.relative
    [:h3.font-semibold.mb-3 "3. High Z-Index Competition"]
    [:p.text-sm.text-gray-500.mb-3 "Multiple elements with high z-index values competing"]
    [:div.relative
     ;; High z-index competitor
     [:div {:style {:position "absolute"
                    :top "40px"
                    :left "200px"
                    :width "100px"
                    :height "50px"
                    :background "#dc2626"
                    :color "white"
                    :z-index "9999"
                    :padding "0.5rem"
                    :border-radius "4px"
                    :font-size "12px"
                    :line-height "1.2"}}
      "z-index: 9999"]
     [:ty-dropdown
      {:placeholder "Dropdown vs z-index 9999"
       :style {:width "200px"}}
      [:option {:value "zindex-1"} "Z-Index Test 1"]
      [:option {:value "zindex-2"} "Z-Index Test 2"]
      [:option {:value "zindex-3"} "Top layer wins! 🏆"]]]]

   ;; Multiple stacking contexts
   [:div.test-scenario.mb-8
    [:h3.font-semibold.mb-3 "4. Complex Stacking Context"]
    [:p.text-sm.text-gray-500.mb-3 "Nested elements with filter, opacity, and perspective"]
    [:div {:style {:filter "drop-shadow(4px 4px 8px rgba(0,0,0,0.1))"
                   :opacity "0.95"
                   :perspective "1000px"}}
     [:div {:style {:background "linear-gradient(135deg, #667eea 0%, #764ba2 100%)"
                    :padding "1.5rem"
                    :border-radius "12px"
                    :transform "rotateX(5deg)"}}
      [:ty-dropdown
       {:placeholder "Complex context dropdown"
        :style {:width "240px"}}
       [:option {:value "complex-1"} "Complex Context 1"]
       [:option {:value "complex-2"} "Complex Context 2"]
       [:option {:value "complex-3"} "Pierces all contexts! ⚡"]]]]]])

(defn scroll-to-close-tests
  "Test the scroll-to-close functionality"
  []
  [:div.demo-section.mb-12
   [:h2.demo-title.text-xl.font-semibold.mb-4.text-green-600 "📜 Scroll-to-Close Behavior Tests"]
   [:p.text-gray-600.mb-6
    "Open the dropdowns below, then scroll the page or containers. Dropdowns should close automatically when scrolling outside them."]

   ;; Page scroll test
   [:div.test-scenario.mb-8
    [:h3.font-semibold.mb-3 "1. Page Scroll Detection"]
    [:p.text-sm.text-gray-500.mb-3 "Open dropdown, then scroll the page. Should close automatically."]
    [:ty-dropdown
     {:placeholder "Open me, then scroll page"
      :style {:width "250px"}}
     [:option {:value "scroll-1"} "Page Scroll Test 1"]
     [:option {:value "scroll-2"} "Page Scroll Test 2"]
     [:option {:value "scroll-3"} "Closes on scroll 🔄"]]]

   ;; Container scroll test
   [:div.test-scenario.mb-8
    [:h3.font-semibold.mb-3 "2. Container Scroll Detection"]
    [:p.text-sm.text-gray-500.mb-3 "Open dropdown inside scrollable container"]
    [:div {:style {:height "200px"
                   :overflow-y "auto"
                   :background "#f8fafc"
                   :padding "1rem"
                   :border "1px solid #e2e8f0"
                   :border-radius "8px"}}
     [:div {:style {:height "400px"}}
      [:ty-dropdown
       {:placeholder "Scroll container to test"
        :style {:width "220px"}}
       [:option {:value "container-1"} "Container Scroll 1"]
       [:option {:value "container-2"} "Container Scroll 2"]
       [:option {:value "container-3"} "Detects container scroll 📦"]]
      [:div.mt-8.p-4.bg-blue-50.rounded
       "Scroll this container after opening the dropdown above"]
      [:div.mt-4.p-4.bg-green-50.rounded
       "The dropdown should close when you scroll this container"]]]]])

(defn backdrop-escape-tests
  "Test native modal backdrop clicks and ESC key behavior"
  []
  [:div.demo-section.mb-12
   [:h2.demo-title.text-xl.font-semibold.mb-4.text-purple-600 "🎭 Backdrop & ESC Key Tests"]
   [:p.text-gray-600.mb-6
    "Test the native modal behavior: click outside the dropdown or press ESC to close."]

   [:div.test-scenario.mb-8
    [:h3.font-semibold.mb-3 "1. Click Backdrop to Close"]
    [:p.text-sm.text-gray-500.mb-3 "Open dropdown, then click anywhere outside. Should close immediately."]
    [:div.flex.gap-4.items-center
     [:ty-dropdown
      {:placeholder "Click outside to close"
       :style {:width "200px"}}
      [:option {:value "backdrop-1"} "Backdrop Test 1"]
      [:option {:value "backdrop-2"} "Backdrop Test 2"]
      [:option {:value "backdrop-3"} "Click anywhere! 👆"]]
     [:div.p-4.bg-yellow-50.rounded.border-2.border-dashed.border-yellow-300
      "Click this area"]]]

   [:div.test-scenario.mb-8
    [:h3.font-semibold.mb-3 "2. ESC Key to Close"]
    [:p.text-sm.text-gray-500.mb-3 "Open dropdown, then press ESC key. Should close immediately."]
    [:ty-dropdown
     {:placeholder "Press ESC to close"
      :style {:width "200px"}}
     [:option {:value "esc-1"} "ESC Test 1"]
     [:option {:value "esc-2"} "ESC Test 2"]
     [:option {:value "esc-3"} "Press ESC! ⌨️"]]]])

(defn positioning-accuracy-tests
  "Test that dropdowns appear over the stub with exact positioning"
  []
  [:div.demo-section.mb-12
   [:h2.demo-title.text-xl.font-semibold.mb-4.text-red-600 "📐 Positioning Accuracy Tests"]
   [:p.text-gray-600.mb-6
    "Verify dropdowns appear exactly over their input field (stub) with pixel-perfect alignment."]

   ;; Different sizes test
   [:div.test-scenario.mb-8
    [:h3.font-semibold.mb-3 "1. Different Width Dropdowns"]
    [:p.text-sm.text-gray-500.mb-3 "Dropdown should match the width of each input exactly"]
    [:div.flex.flex-col.gap-4
     [:ty-dropdown
      {:placeholder "Narrow dropdown"
       :style {:width "150px"}}
      [:option {:value "narrow-1"} "Fits exactly"]]
     [:ty-dropdown
      {:placeholder "Medium width dropdown"
       :style {:width "250px"}}
      [:option {:value "medium-1"} "Should align perfectly"]]
     [:ty-dropdown
      {:placeholder "Wide dropdown for testing alignment precision"
       :style {:width "400px"}}
      [:option {:value "wide-1"} "Perfect width matching"]]]]

   ;; Edge positioning test
   [:div.test-scenario.mb-8
    [:h3.font-semibold.mb-3 "2. Viewport Edge Behavior"]
    [:p.text-sm.text-gray-500.mb-3 "Dropdown near viewport edges should position intelligently"]
    [:div.flex.justify-between.items-center
     [:ty-dropdown
      {:placeholder "Left edge"
       :style {:width "180px"}}
      [:option {:value "left-1"} "Left edge test"]]
     [:ty-dropdown
      {:placeholder "Right edge"
       :style {:width "180px"}}
      [:option {:value "right-1"} "Right edge test"]]]]])

(defn showmodal-comprehensive-tests
  "All showModal() implementation tests in one section"
  []
  [:div.container.mx-auto.px-4.py-8
   [:div.text-center.mb-8
    [:h1.text-3xl.font-bold.text-gray-800.mb-2 "🚀 showModal() Implementation Tests"]
    [:p.text-lg.text-gray-600.mb-4
     "Comprehensive tests for the new dropdown showModal() positioning system"]
    [:div.inline-flex.items-center.gap-2.px-4.py-2.bg-green-100.text-green-800.rounded-lg.text-sm
     [:span "✅"] "Phase 3: Event Integration - Complete"]]

   (showmodal-z-index-tests)
   (scroll-to-close-tests)
   (backdrop-escape-tests)
   (positioning-accuracy-tests)

   [:div.mt-12.p-6.bg-blue-50.rounded-lg.border.border-blue-200
    [:h3.font-semibold.text-blue-800.mb-2 "🎯 Testing Instructions:"]
    [:ul.text-sm.text-blue-700.space-y-1
     [:li "1. Try opening dropdowns in challenging CSS contexts"]
     [:li "2. Test scroll-to-close by scrolling after opening dropdowns"]
     [:li "3. Test backdrop clicks and ESC key functionality"]
     [:li "4. Verify positioning accuracy and width matching"]
     [:li "5. Check that all dropdowns appear above other content"]]]

   [:div.mt-6.p-6.bg-gray-50.rounded-lg.border.border-gray-200
    [:h3.font-semibold.text-gray-800.mb-2 "📊 Expected Results:"]
    [:ul.text-sm.text-gray-700.space-y-1
     [:li "• All dropdowns should appear in browser's top layer"]
     [:li "• No z-index issues regardless of parent CSS"]
     [:li "• Dropdowns close automatically when scrolling outside"]
     [:li "• Native modal behavior (ESC key, backdrop clicks)"]
     [:li "• Perfect positioning alignment over input fields"]]]])

(defn view []
  [:div.max-w-6xl.mx-auto
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2
     "Dropdown Component"]
    [:p.text-lg.ty-text-
     "A powerful dropdown component with smart positioning, search filtering, keyboard navigation, global management, and rich content support. Use inline styles for custom sizing and flavor attributes for semantic styling."]]

   ;; NEW: showModal() comprehensive tests section
   (showmodal-comprehensive-tests)

   [:div.space-y-12
    (htmx-reactive-demo)
    (date-picker-examples)
    (date-picker-form-integration-demo)
    (localized-date-picker-demo)
    (basic-examples)
    (multiple-dropdowns-test)
    (flavor-variants)
    (search-examples)
    (blur-clear-demo)
    (rich-html-demo)
    (sizing-flavors-demo)
    (event-debugging)]])
