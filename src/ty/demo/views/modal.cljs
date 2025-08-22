(ns ty.demo.views.modal
  (:require [ty.demo.state :as state]))

(defn dropdown-event-handler [event]
  (let [detail (.-detail event)
        value (.-value detail)
        text (.-text detail)]
    (js/console.log "Modal dropdown changed:" (js-obj "value" value "text" text))
    (swap! state/state assoc :modal-dropdown-value value)))

(defn modal-view []
  [:div.max-w-6xl.mx-auto
   [:div.mb-8
    [:h1.text-3xl.font-bold.text-gray-900.dark:text-white.mb-2
     "Modal Component"]
    [:p.text-lg.text-gray-600.dark:text-gray-400.mb-4
     "A pure modal wrapper component that provides backdrop, focus management, and keyboard interaction without imposing any styling on your content."]
    [:div.p-4.bg-blue-50.dark:bg-blue-900.border.border-blue-200.dark:border-blue-700.rounded
     [:h4.text-sm.font-medium.text-blue-800.dark:text-blue-200.mb-2 "Key Features:"]
     [:ul.text-sm.text-blue-700.dark:text-blue-300.space-y-1
      [:li "• No built-in sizing - content determines its own dimensions"]
      [:li "• No background or styling imposed - pure content wrapper"]
      [:li "• Focus management and keyboard navigation"]
      [:li "• Backdrop click and escape key handling"]
      [:li "• CSS variable inheritance for design tokens"]]]]

   ;; Basic test
   [:div.demo-section
    [:h2.demo-title "Basic Modal Test"]
    [:p.text-gray-600.dark:text-gray-400.mb-4
     "The modal component is a pure wrapper - all styling (size, background, borders, etc.) is applied by user content."]
    [:ty-button {:on {:click #(swap! state/state assoc :modal-basic-open true)}}
     "Open Modal"]

    ;; Simple modal
    [:ty-modal {:open (get @state/state :modal-basic-open false)
                :on {:ty-modal-close #(swap! state/state assoc :modal-basic-open false)}}
     [:div.p-6.max-w-md.bg-white.dark:bg-gray-800.rounded-lg.shadow-xl
      [:h3.text-lg.font-semibold.mb-4 "Test Modal"]
      [:p.text-gray-600.dark:text-gray-400.mb-4 "This is a test modal with user-defined styling."]
      [:ty-button {:on {:click #(swap! state/state assoc :modal-basic-open false)}}
       "Close"]]]]

   ;; Dropdown in Modal Test
   [:div.demo-section
    [:h2.demo-title "Dropdown in Modal Test"]
    [:p.text-gray-600.dark:text-gray-400.mb-4
     "Test dropdown positioning, z-index stacking, and behavior inside modal dialogs."]

    [:div.grid.grid-cols-1.md:grid-cols-2.gap-4.mb-6
     [:ty-button {:on {:click #(swap! state/state assoc :modal-dropdown-simple-open true)}}
      "Open Simple Modal with Dropdown"]

     [:ty-button {:on {:click #(swap! state/state assoc :modal-dropdown-complex-open true)}}
      "Open Complex Modal with Multiple Dropdowns"]]

    ;; Simple modal with dropdown
    [:ty-modal {:open (get @state/state :modal-dropdown-simple-open false)
                :on {:ty-modal-close #(swap! state/state assoc :modal-dropdown-simple-open false)}}
     [:div.p-6.max-w-lg.bg-white.dark:bg-gray-800.rounded-lg.shadow-xl
      [:h3.text-xl.font-semibold.mb-4 "Simple Dropdown Test"]
      [:p.text-gray-600.mb-4
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
        [:ty-button {:flavor "positive"
                     :on {:click #(swap! state/state assoc :modal-dropdown-simple-open false)}}
         "Save"]
        [:ty-button {:flavor "neutral"
                     :on {:click #(swap! state/state assoc :modal-dropdown-simple-open false)}}
         "Cancel"]]]]]

    ;; Complex modal with multiple dropdowns and edge cases
    [:ty-modal {:open (get @state/state :modal-dropdown-complex-open false)
                :on {:ty-modal-close #(swap! state/state assoc :modal-dropdown-complex-open false)}}
     [:div.p-6.max-w-4xl.bg-white.dark:bg-gray-800.rounded-lg.shadow-xl
      [:h3.text-xl.font-semibold.mb-4 "Complex Dropdown Test"]
      [:p.text-gray-600.mb-6
       "Test multiple dropdowns, positioning edge cases, and interactions:"]

      [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
       ;; Left column
       [:div.space-y-4
        [:h4.font-medium.text-gray-900 "Top Section (should position down)"]

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
                        :flavor "positive"
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
        [:h4.font-medium.text-gray-900 "Settings"]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "Theme"]
         [:ty-dropdown {:value "dark"
                        :placeholder "Select theme..."
                        :searchable false
                        :flavor "important"
                        :on {:change dropdown-event-handler}}
          [:option {:value "light"} "Light Mode"]
          [:option {:value "dark"} "Dark Mode"]
          [:option {:value "auto"} "Auto (System)"]]]

        [:div
         [:label.block.text-sm.font-medium.mb-1 "Language"]
         [:ty-dropdown {:value "en"
                        :placeholder "Select language..."
                        :flavor "unique"
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
         [:h4.font-medium.text-gray-900.mb-2 "Bottom Section (should position up)"]
         [:p.text-sm.text-gray-600.mb-4
          "These dropdowns should flip upward when near the bottom of the modal."]

         [:div.space-y-3
          [:div
           [:label.block.text-sm.font-medium.mb-1 "Time Zone"]
           [:ty-dropdown {:placeholder "Select timezone..."
                          :flavor "exception"
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
                          :flavor "negative"
                          :on {:change dropdown-event-handler}}
            [:option {:value "usd"} "US Dollar (USD)"]
            [:option {:value "eur"} "Euro (EUR)"]
            [:option {:value "gbp"} "British Pound (GBP)"]
            [:option {:value "jpy"} "Japanese Yen (JPY)"]
            [:option {:value "cad"} "Canadian Dollar (CAD)"]
            [:option {:value "aud"} "Australian Dollar (AUD)"]]]]]]]

      [:div.flex.justify-between.items-center.pt-6.border-t
       [:div.text-sm.text-gray-600
        "Current selection: " [:code.bg-gray-100.px-2.py-1.rounded (:modal-dropdown-value @state/state "none")]]

       [:div.flex.gap-2
        [:ty-button {:flavor "positive"
                     :on {:click #(swap! state/state assoc :modal-dropdown-complex-open false)}}
         "Apply Settings"]
        [:ty-button {:flavor "neutral"
                     :on {:click #(swap! state/state assoc :modal-dropdown-complex-open false)}}
         "Cancel"]]]]]]

   ;; Test Results Section
   [:div.demo-section
    [:h2.demo-title "Expected Behavior in Modals"]
    [:div.space-y-4
     [:div.p-4.bg-blue-50.dark:bg-blue-900.border.border-blue-200.dark:border-blue-700.rounded
      [:h4.text-sm.font-medium.text-blue-800.dark:text-blue-200.mb-2 "✅ Should Work Correctly:"]
      [:ul.text-sm.text-blue-700.dark:text-blue-300.space-y-1
       [:li "• Dropdown options appear above modal backdrop (higher z-index)"]
       [:li "• Smart positioning works (flips up/down based on available space)"]
       [:li "• Global dropdown management (only one dropdown open at a time)"]
       [:li "• Keyboard navigation works inside modal"]
       [:li "• Clicking outside dropdown closes it (but not the modal)"]
       [:li "• ESC key closes dropdown first, then modal on second press"]
       [:li "• Smooth scrolling and positioning transitions"]]]

     [:div.p-4.bg-yellow-50.dark:bg-yellow-900.border.border-yellow-200.dark:border-yellow-700.rounded
      [:h4.text-sm.font-medium.text-yellow-800.dark:text-yellow-200.mb-2 "⚠️ Potential Issues to Test:"]
      [:ul.text-sm.text-yellow-700.dark:text-yellow-300.space-y-1
       [:li "• Z-index conflicts between modal and dropdown"]
       [:li "• Positioning calculations with modal scroll"]
       [:li "• Focus management between modal and dropdown"]
       [:li "• Event bubbling and modal close behavior"]
       [:li "• Backdrop click interactions"]]]

     [:div.p-4.bg-gray-50.dark:bg-gray-800.border.border-gray-200.dark:border-gray-700.rounded
      [:h4.text-sm.font-medium.text-gray-800.dark:text-gray-200.mb-2 "🧪 Test Cases:"]
      [:ul.text-sm.text-gray-700.dark:text-gray-300.space-y-1
       [:li "1. Open modal → Open dropdown → Check z-index stacking"]
       [:li "2. Open dropdown near top → Should position downward"]
       [:li "3. Open dropdown near bottom → Should position upward"]
       [:li "4. Use keyboard navigation → Should work smoothly"]
       [:li "5. Open second dropdown → First should close automatically"]
       [:li "6. Click outside dropdown → Should close dropdown only"]
       [:li "7. Click modal backdrop → Should close modal"]
       [:li "8. ESC key behavior → Dropdown first, then modal"]
       [:li "9. Search in dropdown → Should filter options"]
       [:li "10. Scroll modal content → Dropdown should reposition"]]]]]])
