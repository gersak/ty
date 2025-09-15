(ns ty.demo.views.modal
  (:require [ty.demo.state :as state]))

(defn dropdown-event-handler [event]
  (let [detail (.-detail event)
        value (.-value detail)
        text (.-text detail)]
    (js/console.log "Modal dropdown changed:" (js-obj "value" value "text" text))
    (swap! state/state assoc :modal-dropdown-value value)))

(defn view []
  [:div.max-w-6xl.mx-auto
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2
     "Modal Component"]
    [:p.text-lg.ty-text-.mb-4
     "A pure modal wrapper component that provides backdrop, focus management, and keyboard interaction without imposing any styling on your content."]
    [:div.p-4.ty-bg-primary-.border.ty-border-primary.rounded
     [:h4.text-sm.font-medium.ty-text-primary.mb-2 "Key Features:"]
     [:ul.text-sm.ty-text-primary.space-y-1
      [:li "• No built-in sizing - content determines its own dimensions"]
      [:li "• No background or styling imposed - pure content wrapper"]
      [:li "• Focus management and keyboard navigation"]
      [:li "• Backdrop click and escape key handling"]
      [:li "• CSS variable inheritance for design tokens"]]]]

   ;; Basic test
   [:div.demo-section
    [:h2.demo-title "Basic Modal Test"]
    [:p.ty-text-.mb-4
     "The modal component is a pure wrapper - all styling (size, background, borders, etc.) is applied by user content."]
    [:ty-button {:on {:click #(swap! state/state assoc :modal-basic-open true)}}
     "Open Modal"]

    ;; Simple modal
    [:ty-modal {:open (get @state/state :modal-basic-open false)
                :on {:ty-modal-close #(swap! state/state assoc :modal-basic-open false)}}
     [:div.p-6.max-w-md.ty-elevated.rounded-lg.shadow-xl
      [:h3.text-lg.font-semibold.mb-4 "Test Modal"]
      [:p.ty-text-.mb-4 "This is a test modal with user-defined styling."]
      [:ty-button {:on {:click #(swap! state/state assoc :modal-basic-open false)}}
       "Close"]]]]

   ;; NEW: Date Picker in Modal Test Section
   [:div.demo-section
    [:h2.demo-title "🚨 Date Picker in Modal Test - Issue Debugging"]
    [:p.ty-text-.mb-4
     "Testing date picker components inside modals to debug the issue where clicking the date picker field might close the modal."]

    ;; Issue description
    [:div.p-4.ty-bg-danger-.border.ty-border-danger.rounded.mb-6
     [:h4.text-sm.font-medium.ty-text-danger.mb-2 "🐛 Known Issue:"]
     [:ul.text-sm.ty-text-danger.space-y-1
      [:li "• Clicking on date picker input field may close the modal"]
      [:li "• Calendar dropdown may not appear or position incorrectly"]
      [:li "• Event propagation conflicts between modal and date picker dialogs"]
      [:li "• Z-index stacking issues with multiple dialog elements"]]]

    [:div.grid.grid-cols-1.md:grid-cols-2.gap-4.mb-6
     [:ty-button {:on {:click #(swap! state/state assoc :modal-datepicker-simple-open true)}}
      "🗓️ Simple Date Picker Modal"]

     [:ty-button {:on {:click #(swap! state/state assoc :modal-datepicker-complex-open true)}}
      "🗓️ Complex Date Picker Modal"]

     [:ty-button {:on {:click #(swap! state/state assoc :modal-datepicker-form-open true)}}
      "📋 Date Picker Form Modal"]

     [:ty-button {:on {:click #(swap! state/state assoc :modal-datepicker-time-open true)}}
      "⏰ Date Picker with Time Modal"]]

    ;; Simple Date Picker Modal
    [:ty-modal {:open (get @state/state :modal-datepicker-simple-open false)
                :on {:ty-modal-close #(swap! state/state assoc :modal-datepicker-simple-open false)}}
     [:div.p-6.max-w-lg.ty-elevated.rounded-lg.shadow-xl
      [:h3.text-xl.font-semibold.mb-4 "Simple Date Picker Test"]
      [:p.ty-text-.mb-4
       "Click on the date picker input below. It should open the calendar dropdown WITHOUT closing this modal."]

      [:div.space-y-4
       [:div
        [:label.block.text-sm.font-medium.mb-2 "Select a Date"]
        [:ty-date-picker {:placeholder "Choose a date..."
                          :clearable true
                          :format "medium"}]]

       [:div
        [:label.block.text-sm.font-medium.mb-2 "Another Date Picker"]
        [:ty-date-picker {:value "2024-03-15"
                          :clearable true
                          :format "short"}]]

       [:div.flex.gap-2.pt-4
        [:ty-button {:flavor "success"
                     :on {:click #(do
                                    (js/console.log "Date picker form submitted")
                                    (swap! state/state assoc :modal-datepicker-simple-open false))}}
         "Save Date"]
        [:ty-button {:flavor "neutral"
                     :on {:click #(swap! state/state assoc :modal-datepicker-simple-open false)}}
         "Cancel"]]]]]

    ;; Complex Date Picker Modal
    [:ty-modal {:open (get @state/state :modal-datepicker-complex-open false)
                :on {:ty-modal-close #(swap! state/state assoc :modal-datepicker-complex-open false)}}
     [:div.p-6.max-w-4xl.ty-elevated.rounded-lg.shadow-xl
      [:h3.text-xl.font-semibold.mb-4 "Complex Date Picker Test"]
      [:p.ty-text-.mb-6
       "Multiple date pickers with different configurations to test various edge cases:"]

      [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
       ;; Left column
       [:div.space-y-4
        [:h4.font-medium.ty-text "Event Planning"]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "Event Start Date"]
         [:ty-date-picker {:placeholder "Select start date..."
                           :format "long"
                           :clearable true}]]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "Event End Date"]
         [:ty-date-picker {:placeholder "Select end date..."
                           :format "long"
                           :flavor "success"
                           :clearable true}]]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "Registration Deadline"]
         [:ty-date-picker {:placeholder "Select deadline..."
                           :format "medium"
                           :flavor "warning"
                           :clearable true}]]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "Publication Date"]
         [:ty-date-picker {:value "2024-06-01"
                           :format "full"
                           :flavor "primary"
                           :clearable true}]]]

       ;; Right column
       [:div.space-y-4
        [:h4.font-medium.ty-text "Project Milestones"]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "Project Start"]
         [:ty-date-picker {:value "2024-01-15"
                           :format "short"
                           :size "sm"
                           :clearable true}]]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "Alpha Release"]
         [:ty-date-picker {:placeholder "Select alpha date..."
                           :format "medium"
                           :size "md"
                           :flavor "secondary"
                           :clearable true}]]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "Beta Release"]
         [:ty-date-picker {:placeholder "Select beta date..."
                           :format "medium"
                           :flavor "info"
                           :clearable true}]]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "Final Release (Critical!)"]
         [:ty-date-picker {:placeholder "Select final date..."
                           :format "long"
                           :flavor "danger"
                           :size "lg"
                           :clearable true}]]]]

      [:div.flex.justify-between.items-center.pt-6.border-t
       [:div.text-sm.ty-text-
        "Test: All date pickers should work without closing the modal"]

       [:div.flex.gap-2
        [:ty-button {:flavor "success"
                     :on {:click #(do
                                    (js/console.log "Complex date picker form submitted")
                                    (swap! state/state assoc :modal-datepicker-complex-open false))}}
         "Save All Dates"]
        [:ty-button {:flavor "neutral"
                     :on {:click #(swap! state/state assoc :modal-datepicker-complex-open false)}}
         "Cancel"]]]]]

    ;; Form Integration Modal
    [:ty-modal {:open (get @state/state :modal-datepicker-form-open false)
                :on {:ty-modal-close #(swap! state/state assoc :modal-datepicker-form-open false)}}
     [:div.p-6.max-w-2xl.ty-elevated.rounded-lg.shadow-xl
      [:h3.text-xl.font-semibold.mb-4 "Date Picker Form Integration"]
      [:p.ty-text-.mb-6
       "Testing date pickers in a complete form with validation and mixed input types:"]

      [:form.space-y-6
       [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
        [:div
         [:label.block.text-sm.font-medium.mb-1 "Full Name"]
         [:ty-input {:type "text"
                     :placeholder "Enter your name"
                     :required true}]]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "Email"]
         [:ty-input {:type "email"
                     :placeholder "your@email.com"
                     :required true}]]]

       [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
        [:div
         [:label.block.text-sm.font-medium.mb-1 "Birth Date"]
         [:ty-date-picker {:placeholder "Select your birth date..."
                           :format "long"
                           :clearable true
                           :required true}]]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "Preferred Contact Method"]
         [:ty-dropdown {:placeholder "Choose method..."
                        :on {:change dropdown-event-handler}}
          [:option {:value "email"} "Email"]
          [:option {:value "phone"} "Phone"]
          [:option {:value "sms"} "SMS"]]]]

       [:div
        [:label.block.text-sm.font-medium.mb-1 "Available Dates (Multi-select simulation)"]
        [:div.space-y-2
         [:ty-date-picker {:placeholder "First available date..."
                           :format "medium"
                           :clearable true}]
         [:ty-date-picker {:placeholder "Second available date..."
                           :format "medium"
                           :clearable true}]
         [:ty-date-picker {:placeholder "Third available date..."
                           :format "medium"
                           :clearable true}]]]

       [:div
        [:label.block.text-sm.font-medium.mb-1 "Additional Notes"]
        [:ty-input {:type "textarea"
                    :placeholder "Any additional information..."
                    :rows "3"}]]

       [:div.flex.justify-end.gap-2
        [:ty-button {:type "button"
                     :flavor "neutral"
                     :on {:click #(swap! state/state assoc :modal-datepicker-form-open false)}}
         "Cancel"]
        [:ty-button {:type "submit"
                     :flavor "primary"
                     :on {:click #(do
                                    (.preventDefault %)
                                    (js/console.log "Form submitted with date pickers")
                                    (js/alert "Form submitted! Check console for details.")
                                    (swap! state/state assoc :modal-datepicker-form-open false))}}
         "Submit Form"]]]]]

    ;; Date Picker with Time Modal
    [:ty-modal {:open (get @state/state :modal-datepicker-time-open false)
                :on {:ty-modal-close #(swap! state/state assoc :modal-datepicker-time-open false)}}
     [:div.p-6.max-w-lg.ty-elevated.rounded-lg.shadow-xl
      [:h3.text-xl.font-semibold.mb-4 "Date Picker with Time Test"]
      [:p.ty-text-.mb-4
       "Testing date pickers with time input components:"]

      [:div.space-y-4
       [:div
        [:label.block.text-sm.font-medium.mb-2 "Meeting Date & Time"]
        [:ty-date-picker {:placeholder "Select date and time..."
                          :with-time true
                          :format "long"
                          :clearable true}]]

       [:div
        [:label.block.text-sm.font-medium.mb-2 "Deadline Date & Time"]
        [:ty-date-picker {:value "2024-12-31T23:59"
                          :with-time true
                          :format "medium"
                          :flavor "danger"
                          :clearable true}]]

       [:div
        [:label.block.text-sm.font-medium.mb-2 "Event Start (Short Format)"]
        [:ty-date-picker {:placeholder "Select start time..."
                          :with-time true
                          :format "short"
                          :flavor "success"
                          :clearable true}]]

       [:div.flex.gap-2.pt-4
        [:ty-button {:flavor "success"
                     :on {:click #(do
                                    (js/console.log "Date+time form submitted")
                                    (swap! state/state assoc :modal-datepicker-time-open false))}}
         "Schedule Event"]
        [:ty-button {:flavor "neutral"
                     :on {:click #(swap! state/state assoc :modal-datepicker-time-open false)}}
         "Cancel"]]]]]

    ;; Test Instructions
    [:div.p-4.ty-content.border.ty-border.rounded.mb-6
     [:h4.text-sm.font-medium.ty-text.mb-2 "🧪 Debugging Instructions:"]
     [:ul.text-sm.ty-text-.space-y-1
      [:li "1. Open each modal using the buttons above"]
      [:li "2. Click on any date picker input field"]
      [:li "3. ✅ Expected: Calendar should open WITHOUT closing the modal"]
      [:li "4. ❌ Actual (bug): Modal might close when clicking date picker"]
      [:li "5. Check browser console for any JavaScript errors"]
      [:li "6. Test with browser dev tools to inspect DOM structure"]
      [:li "7. Check z-index values and dialog element positioning"]
      [:li "8. Test event propagation by adding event.stopPropagation() where needed"]]]]

   ;; Dropdown in Modal Test (existing)
   [:div.demo-section
    [:h2.demo-title "Dropdown in Modal Test - CSS Variable Inheritance"]
    [:p.ty-text-.mb-4
     "Test dropdown positioning, z-index stacking, and behavior inside modal dialogs. This specifically tests the automatic CSS variable inheritance system that ensures nested web components have access to all design tokens."]

    [:div.grid.grid-cols-1.md:grid-cols-2.gap-4.mb-6
     [:ty-button {:on {:click #(swap! state/state assoc :modal-dropdown-simple-open true)}}
      "Open Simple Modal with Dropdown"]

     [:ty-button {:on {:click #(swap! state/state assoc :modal-dropdown-complex-open true)}}
      "Open Complex Modal with Multiple Dropdowns"]]

    ;; Simple modal with dropdown
    [:ty-modal {:open (get @state/state :modal-dropdown-simple-open false)
                :on {:ty-modal-close #(swap! state/state assoc :modal-dropdown-simple-open false)}}
     [:div.p-6.max-w-lg.ty-elevated.rounded-lg.shadow-xl
      [:h3.text-xl.font-semibold.mb-4 "Simple Dropdown Test"]
      [:p.ty-text-.mb-4
       "Test basic dropdown functionality inside a modal:"]

      [:div.space-y-4
       [:div
        [:label.block.text-sm.font-medium.mb-2 "Select a Programming Language"]
        [:ty-dropdown {:value "javascript"
                       :placeholder "Choose a language..."
                       :on {:change dropdown-event-handler}}
         [:option {:value "javascript"} "JavaScript"]
         [:option {:value "typescript"} "TypeScript"]
         [:option {:value "python"} "Python"]
         [:option {:value "java"} "Java"]
         [:option {:value "clojure"} "Clojure"]
         [:option {:value "rust"} "Rust"]
         [:option {:value "go"} "Go"]]]

       [:div
        [:label.block.text-sm.font-medium.mb-2 "Select Framework"]
        [:ty-dropdown {:placeholder "Choose a framework..."
                       :searchable false
                       :on {:change dropdown-event-handler}}
         [:option {:value "react"} "React"]
         [:option {:value "vue"} "Vue.js"]
         [:option {:value "angular"} "Angular"]
         [:option {:value "svelte"} "Svelte"]]]

       [:div.flex.gap-2.pt-4
        [:ty-button {:flavor "success"
                     :on {:click #(swap! state/state assoc :modal-dropdown-simple-open false)}}
         "Save"]
        [:ty-button {:flavor "neutral"
                     :on {:click #(swap! state/state assoc :modal-dropdown-simple-open false)}}
         "Cancel"]]]]]

    ;; Complex modal with multiple dropdowns and edge cases
    [:ty-modal {:open (get @state/state :modal-dropdown-complex-open false)
                :on {:ty-modal-close #(swap! state/state assoc :modal-dropdown-complex-open false)}}
     [:div.p-6.max-w-4xl.ty-elevated.rounded-lg.shadow-xl
      [:h3.text-xl.font-semibold.mb-4 "Complex Dropdown Test"]
      [:p.ty-text-.mb-6
       "Test multiple dropdowns, positioning edge cases, and interactions:"]

      [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
       ;; Left column
       [:div.space-y-4
        [:h4.font-medium.ty-text "Top Section (should position down)"]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "Country"]
         [:ty-dropdown {:value "usa"
                        :placeholder "Select country..."
                        :on {:change dropdown-event-handler}}
          [:option {:value "usa"} "United States"]
          [:option {:value "canada"} "Canada"]
          [:option {:value "uk"} "United Kingdom"]
          [:option {:value "germany"} "Germany"]
          [:option {:value "france"} "France"]
          [:option {:value "japan"} "Japan"]
          [:option {:value "australia"} "Australia"]
          [:option {:value "brazil"} "Brazil"]
          [:option {:value "india"} "India"]
          [:option {:value "china"} "China"]]]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "State/Province"]
         [:ty-dropdown {:placeholder "Select state..."
                        :flavor "success"
                        :on {:change dropdown-event-handler}}
          [:option {:value "ca"} "California"]
          [:option {:value "ny"} "New York"]
          [:option {:value "tx"} "Texas"]
          [:option {:value "fl"} "Florida"]
          [:option {:value "wa"} "Washington"]
          [:option {:value "or"} "Oregon"]]]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "City (Large list, searchable)"]
         [:ty-dropdown {:placeholder "Search cities..."
                        :on {:change dropdown-event-handler}}
          [:option {:value "sf"} "San Francisco"]
          [:option {:value "la"} "Los Angeles"]
          [:option {:value "nyc"} "New York City"]
          [:option {:value "chicago"} "Chicago"]
          [:option {:value "houston"} "Houston"]
          [:option {:value "phoenix"} "Phoenix"]
          [:option {:value "philadelphia"} "Philadelphia"]
          [:option {:value "san-antonio"} "San Antonio"]
          [:option {:value "san-diego"} "San Diego"]
          [:option {:value "dallas"} "Dallas"]
          [:option {:value "san-jose"} "San Jose"]
          [:option {:value "austin"} "Austin"]
          [:option {:value "jacksonville"} "Jacksonville"]
          [:option {:value "fort-worth"} "Fort Worth"]
          [:option {:value "columbus"} "Columbus"]
          [:option {:value "charlotte"} "Charlotte"]
          [:option {:value "seattle"} "Seattle"]
          [:option {:value "denver"} "Denver"]
          [:option {:value "el-paso"} "El Paso"]
          [:option {:value "detroit"} "Detroit"]]]]

       ;; Right column
       [:div.space-y-4
        [:h4.font-medium.ty-text "Settings"]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "Theme"]
         [:ty-dropdown {:value "dark"
                        :placeholder "Select theme..."
                        :searchable false
                        :flavor "primary"
                        :on {:change dropdown-event-handler}}
          [:option {:value "light"} "Light Mode"]
          [:option {:value "dark"} "Dark Mode"]
          [:option {:value "auto"} "Auto (System)"]]]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "Language"]
         [:ty-dropdown {:value "en"
                        :placeholder "Select language..."
                        :flavor "secondary"
                        :on {:change dropdown-event-handler}}
          [:option {:value "en"} "English"]
          [:option {:value "es"} "Español"]
          [:option {:value "fr"} "Français"]
          [:option {:value "de"} "Deutsch"]
          [:option {:value "it"} "Italiano"]
          [:option {:value "pt"} "Português"]
          [:option {:value "ru"} "Русский"]
          [:option {:value "zh"} "中文"]
          [:option {:value "ja"} "日本語"]
          [:option {:value "ko"} "한국어"]]]

        [:div.mt-8
         [:h4.font-medium.ty-text.mb-2 "Bottom Section (should position up)"]
         [:p.text-sm.ty-text-.mb-4
          "These dropdowns should flip upward when near the bottom of the modal."]

         [:div.space-y-3
          [:div
           [:label.block.text-sm.font-medium.mb-1 "Time Zone"]
           [:ty-dropdown {:placeholder "Select timezone..."
                          :flavor "warning"
                          :on {:change dropdown-event-handler}}
            [:option {:value "pst"} "Pacific Standard Time (PST)"]
            [:option {:value "mst"} "Mountain Standard Time (MST)"]
            [:option {:value "cst"} "Central Standard Time (CST)"]
            [:option {:value "est"} "Eastern Standard Time (EST)"]
            [:option {:value "utc"} "Coordinated Universal Time (UTC)"]
            [:option {:value "gmt"} "Greenwich Mean Time (GMT)"]
            [:option {:value "cet"} "Central European Time (CET)"]
            [:option {:value "jst"} "Japan Standard Time (JST)"]]]

          [:div
           [:label.block.text-sm.font-medium.mb-1 "Currency"]
           [:ty-dropdown {:value "usd"
                          :placeholder "Select currency..."
                          :searchable false
                          :flavor "danger"
                          :on {:change dropdown-event-handler}}
            [:option {:value "usd"} "US Dollar (USD)"]
            [:option {:value "eur"} "Euro (EUR)"]
            [:option {:value "gbp"} "British Pound (GBP)"]
            [:option {:value "jpy"} "Japanese Yen (JPY)"]
            [:option {:value "cad"} "Canadian Dollar (CAD)"]
            [:option {:value "aud"} "Australian Dollar (AUD)"]]]]]]]

      [:div.flex.justify-between.items-center.pt-6.border-t
       [:div.text-sm.ty-text-
        "Current selection: " [:code.ty-content.px-2.py-1.rounded (:modal-dropdown-value @state/state "none")]]

       [:div.flex.gap-2
        [:ty-button {:flavor "success"
                     :on {:click #(swap! state/state assoc :modal-dropdown-complex-open false)}}
         "Apply Settings"]
        [:ty-button {:flavor "neutral"
                     :on {:click #(swap! state/state assoc :modal-dropdown-complex-open false)}}
         "Cancel"]]]]]]

   ;; Test Results Section
   [:div.demo-section
    [:h2.demo-title "Expected Behavior in Modals"]
    [:div.space-y-4
     [:div.p-4.ty-bg-primary-.border.ty-border-primary.rounded
      [:h4.text-sm.font-medium.ty-text-primary.mb-2 "✅ Should Work Correctly:"]
      [:ul.text-sm.ty-text-primary.space-y-1
       [:li "• Date picker input should open calendar without closing modal"]
       [:li "• Dropdown options appear above modal backdrop (higher z-index)"]
       [:li "• Smart positioning works (flips up/down based on available space)"]
       [:li "• Global dropdown management (only one dropdown open at a time)"]
       [:li "• Keyboard navigation works inside modal"]
       [:li "• Clicking outside dropdown/calendar closes it (but not the modal)"]
       [:li "• ESC key closes dropdown/calendar first, then modal on second press"]
       [:li "• Smooth scrolling and positioning transitions"]]]

     [:div.p-4.ty-bg-warning-.border.ty-border-warning.rounded
      [:h4.text-sm.font-medium.ty-text-warning.mb-2 "⚠️ Potential Issues to Test:"]
      [:ul.text-sm.ty-text-warning.space-y-1
       [:li "• Z-index conflicts between modal and date picker dialogs"]
       [:li "• Event propagation causing modal to close when clicking date picker"]
       [:li "• Positioning calculations with modal scroll"]
       [:li "• Focus management between modal and date picker components"]
       [:li "• Backdrop click interactions between nested dialogs"]
       [:li "• Multiple dialog elements competing for focus and events"]]]

     [:div.p-4.ty-content.border.ty-border.rounded
      [:h4.text-sm.font-medium.ty-text.mb-2 "🧪 Test Cases:"]
      [:ul.text-sm.ty-text-.space-y-1
       [:li "1. Open modal → Click date picker → Check if modal stays open"]
       [:li "2. Open date picker calendar → Check z-index stacking above modal"]
       [:li "3. Click outside date picker calendar → Should close calendar only"]
       [:li "4. Click modal backdrop → Should close modal"]
       [:li "5. ESC key behavior → Date picker first, then modal"]
       [:li "6. Use keyboard navigation → Should work smoothly in both"]
       [:li "7. Test with time picker → Additional time input should work"]
       [:li "8. Test multiple date pickers in same modal → All should work independently"]
       [:li "9. Test form submission → Values should be preserved"]
       [:li "10. Test mixed components (date picker + dropdown) → No conflicts"]]]]]])
