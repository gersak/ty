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
     [:p.text-gray-600.dark:text-gray-400.mb-4 description])
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
   [:p.text-gray-600.dark:text-gray-400.mb-6
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
                         [:div.mt-2.text-sm.text-gray-600.dark:text-gray-400
                          "Selected: "
                          [:code.bg-gray-100.dark:bg-gray-800.px-2.py-1.rounded.text-xs
                           (str (or (::dropdown-value @state/state) "none"))]]
                         [:div.max-w-xs
                          [:ty-date-picker {:label "Birthday"
                                            :placeholder "Select date..."
                                            :on {:change date-picker-event-handler}}]
                          [:div.mt-2.text-sm.text-gray-600.dark:text-gray-400
                           "Selected: "
                           [:code.bg-gray-100.dark:bg-gray-800.px-2.py-1.rounded.text-xs
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
                          [:div.mt-2.text-sm.text-gray-600.dark:text-gray-400
                           "Value: "
                           [:code.bg-gray-100.dark:bg-gray-800.px-2.py-1.rounded.text-xs
                            (if-let [date-val (:date-picker-value @state/state)]
                              (.toLocaleDateString (js/Date. date-val))
                              "null")]]
                          [:div.mt-1.text-xs.text-blue-600.dark:text-blue-400
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
   [:div.mt-8.p-4.bg-blue-50.dark:bg-blue-900.rounded-lg
    [:h3.text-lg.font-semibold.mb-3.text-blue-800.dark:text-blue-200 "🏗️ New Architecture"]
    [:p.text-sm.text-blue-700.dark:text-blue-300.mb-3
     "ty-date-picker now uses ty-calendar internally, creating a clean component hierarchy:"]
    [:div.text-sm.space-y-2.text-blue-700.dark:text-blue-300
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
   [:div.mt-8.p-4.bg-green-50.dark:bg-green-900.rounded-lg
    [:h3.text-lg.font-semibold.mb-3.text-green-800.dark:text-green-200 "🆕 NEW: Date + Time Support"]
    [:p.text-sm.text-green-700.dark:text-green-300.mb-3
     "Add with-time=\"true\" to get time input with masked validation:"]
    [:div.text-sm.space-y-2.text-green-700.dark:text-green-300
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
                            [:div.mt-2.text-xs.text-gray-600.dark:text-gray-400
                             "ISO datetime: "
                             [:code.bg-gray-100.dark:bg-gray-800.px-1.py-0.5.rounded.text-xs
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
                          [:div.mt-2.text-sm.text-gray-600.dark:text-gray-400
                           "Try typing: 25:70, 99:99, etc. - they'll be rejected!"]
                          [:div.mt-1.text-xs.text-blue-600.dark:text-blue-400
                           "✅ Valid: 00-23 hours, 00-59 minutes"]
                          [:div.mt-1.text-xs.text-red-600.dark:text-red-400
                           "❌ Invalid: >23 hours, >59 minutes"]]]})])

(defn multiple-dropdowns-test []
  [:div.demo-section
   [:h2.demo-title "Global Dropdown Management"]
   [:p.text-gray-600.dark:text-gray-400.mb-6
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
   [:p.text-gray-600.dark:text-gray-400.mb-6
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
   [:p.text-gray-600.dark:text-gray-400.mb-6
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
   [:p.text-gray-600.dark:text-gray-400.mb-6
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
         [:div.w-6.h-6.rounded.bg-yellow-400.flex.items-center.justify-center.text-black.text-xs.font-bold "JS"]
         [:div
          [:div.font-medium "JavaScript"]
          [:div.text-sm.text-gray-500 "Dynamic scripting language"]]]]

       [:ty-option {:value "typescript"}
        [:div.flex.items-center.gap-3
         [:div.w-6.h-6.rounded.bg-blue-600.flex.items-center.justify-center.text-white.text-xs.font-bold "TS"]
         [:div
          [:div.font-medium "TypeScript"]
          [:div.text-sm.text-gray-500 "JavaScript with static typing"]]]]

       [:ty-option {:value "clojure"}
        [:div.flex.items-center.gap-3
         [:div.w-6.h-6.rounded.bg-green-600.flex.items-center.justify-center.text-white.text-xs.font-bold "λ"]
         [:div
          [:div.font-medium "Clojure"]
          [:div.text-sm.text-gray-500 "Functional Lisp for the JVM"]]]]

       [:ty-option {:value "rust"}
        [:div.flex.items-center.gap-3
         [:div.w-6.h-6.rounded.bg-orange-600.flex.items-center.justify-center.text-white.text-xs.font-bold "R"]
         [:div
          [:div.font-medium "Rust"]
          [:div.text-sm.text-gray-500 "Memory-safe systems programming"]]]]

       [:ty-option {:value "python"}
        [:div.flex.items-center.gap-3
         [:div.w-6.h-6.rounded.bg-blue-500.flex.items-center.justify-center.text-white.text-xs.font-bold "Py"]
         [:div
          [:div.font-medium "Python"]
          [:div.text-sm.text-gray-500 "Versatile high-level language"]]]]]]]

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
          [:div.text-sm.text-gray-500 "Senior Developer"]]
         [:span.px-2.py-1.text-xs.bg-green-100.text-green-800.rounded-full.flex-shrink-0 "Online"]]]

       [:ty-option {:value "bob"}
        [:div.flex.items-center.gap-3
         [:div.relative.flex-shrink-0
          [:div.w-8.h-8.rounded-full.bg-purple-500.flex.items-center.justify-center.text-white.font-medium "B"]
          [:div.absolute.-top-1.-right-1.w-3.h-3.bg-yellow-500.rounded-full.border-2.border-white]]
         [:div.flex-1.min-w-0
          [:div.font-medium "Bob Smith"]
          [:div.text-sm.text-gray-500 "Product Manager"]]
         [:span.px-2.py-1.text-xs.bg-yellow-100.text-yellow-800.rounded-full.flex-shrink-0 "Away"]]]

       [:ty-option {:value "carol"}
        [:div.flex.items-center.gap-3
         [:div.relative.flex-shrink-0
          [:div.w-8.h-8.rounded-full.bg-pink-500.flex.items-center.justify-center.text-white.font-medium "C"]
          [:div.absolute.-top-1.-right-1.w-3.h-3.bg-gray-400.rounded-full.border-2.border-white]]
         [:div.flex-1.min-w-0
          [:div.font-medium "Carol Davis"]
          [:div.text-sm.text-gray-500 "UX Designer"]]
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
          [:div.text-sm.text-gray-500 "99.9% uptime • 1.2k users"]]
         [:span.px-2.py-1.text-xs.bg-green-100.text-green-800.rounded "Healthy"]]]

       [:ty-option {:value "staging"}
        [:div.flex.items-center.gap-3
         [:div.w-3.h-3.bg-yellow-500.rounded-full]
         [:div.flex-1
          [:div.font-medium "Staging"]
          [:div.text-sm.text-gray-500 "95.2% uptime • 12 users"]]
         [:span.px-2.py-1.text-xs.bg-yellow-100.text-yellow-800.rounded "Warning"]]]

       [:ty-option {:value "dev"}
        [:div.flex.items-center.gap-3
         [:div.w-3.h-3.bg-red-500.rounded-full]
         [:div.flex-1
          [:div.font-medium "Development"]
          [:div.text-sm.text-gray-500 "78.1% uptime • 3 users"]]
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
          [:div.text-sm.text-gray-500 "2.4 MB • Modified 2 hours ago"]]]]

       [:ty-option {:value "spreadsheet.xlsx"}
        [:div.flex.items-center.gap-3
         [:div.w-8.h-8.bg-green-100.rounded.flex.items-center.justify-center
          [:span.text-green-600.text-xs.font-bold "XLS"]]
         [:div.flex-1
          [:div.font-medium "Budget_Q4.xlsx"]
          [:div.text-sm.text-gray-500 "856 KB • Modified yesterday"]]]]

       [:ty-option {:value "presentation.pptx"}
        [:div.flex.items-center.gap-3
         [:div.w-8.h-8.bg-orange-100.rounded.flex.items-center.justify-center
          [:span.text-orange-600.text-xs.font-bold "PPT"]]
         [:div.flex-1
          [:div.font-medium "Project_Kickoff.pptx"]
          [:div.text-sm.text-gray-500 "12.3 MB • Modified last week"]]]]]]]

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
        <div class=\"text-sm text-gray-500\">Dynamic scripting language</div>
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
          [:div.text-sm.text-gray-500 "A JavaScript library for building user interfaces"]]]]
       [:ty-option {:value "vue"}
        [:div.flex.items-center.gap-4
         [:div.w-10.h-10.bg-green-100.rounded-lg.flex.items-center.justify-center
          [:span.text-green-600.text-xl "🟢"]]
         [:div
          [:div.font-medium.text-lg "Vue.js"]
          [:div.text-sm.text-gray-500 "The progressive JavaScript framework"]]]]
       [:ty-option {:value "svelte"}
        [:div.flex.items-center.gap-4
         [:div.w-10.h-10.bg-orange-100.rounded-lg.flex.items-center.justify-center
          [:span.text-orange-600.text-xl "🔥"]]
         [:div
          [:div.font-medium.text-lg "Svelte"]
          [:div.text-sm.text-gray-500 "Cybernetically enhanced web apps"]]]]]]]]

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
        <div class=\"text-sm text-gray-500\">A JavaScript library for building user interfaces</div>
      </div>
    </div>
  </ty-option>
</ty-dropdown>")])

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

(defn view []
  [:div.max-w-6xl.mx-auto
   [:div.mb-8
    [:h1.text-3xl.font-bold.text-gray-900.dark:text-white.mb-2
     "Dropdown Component"]
    [:p.text-lg.text-gray-600.dark:text-gray-400
     "A powerful dropdown component with smart positioning, search filtering, keyboard navigation, global management, and rich content support. Use inline styles for custom sizing and flavor attributes for semantic styling."]]

   [:div.space-y-12
    (date-picker-examples)
    (basic-examples)
    (multiple-dropdowns-test)
    (flavor-variants)
    (search-examples)
    (blur-clear-demo)
    (rich-html-demo)
    (sizing-flavors-demo)
    (event-debugging)]])
