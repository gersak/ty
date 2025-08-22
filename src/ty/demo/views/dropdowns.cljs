(ns ty.demo.views.dropdowns
  (:require [ty.demo.state :as state]
            [ty.layout :as layout]))

(defn dropdown-event-handler [event]
  (let [detail (.-detail event)
        value (.-value detail)
        text (.-text detail)]
    (swap! state/state assoc :dropdown-value value)))

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
                                                :on {:change dropdown-event-handler}}
                           [:option {:value "red"} "Red"]
                           [:option {:value "blue"} "Blue"]
                           [:option {:value "green"} "Green"]
                           [:option {:value "yellow"} "Yellow"]
                           [:option {:value "purple"} "Purple"]]]]})
   (code-snippet "<ty-dropdown value=\"red\" placeholder=\"Select a color...\">
  <option value=\"red\">Red</option>
  <option value=\"blue\">Blue</option>
  <option value=\"green\">Green</option>
</ty-dropdown>")

   (demo-row {:title "Non-searchable"
              :description "Disable search functionality for simple selection"
              :children [[:div.max-w-xs
                          [:ty-dropdown {:value "medium"
                                         :searchable "false"
                                         :placeholder "Select size..."
                                         :on {:change dropdown-event-handler}}
                           [:option {:value "small"} "Small"]
                           [:option {:value "medium"} "Medium"]
                           [:option {:value "large"} "Large"]
                           [:option {:value "xl"} "Extra Large"]]]]})
   (code-snippet "<ty-dropdown searchable=\"false\" placeholder=\"Select size...\">
  <option value=\"small\">Small</option>
  <option value=\"medium\">Medium</option>
</ty-dropdown>")

   (demo-row {:title "Disabled State"
              :description "Disabled dropdown for read-only contexts"
              :children [[:div.max-w-xs
                          [:ty-dropdown {:value "option2"
                                         :disabled true
                                         :placeholder "Disabled dropdown"}
                           [:option {:value "option1"} "Option 1"]
                           [:option {:value "option2"} "Option 2"]
                           [:option {:value "option3"} "Option 3"]]]]})

   (demo-row {:title "Read-only State"
              :description "Read-only dropdown shows value but prevents interaction"
              :children [[:div.max-w-xs
                          [:ty-dropdown {:value "readonly-value"
                                         :readonly true}
                           [:option {:value "readonly-value"} "Read-only Value"]
                           [:option {:value "other"} "Other Option"]]]]})])

(defn multiple-dropdowns-test []
  [:div.demo-section
   [:h2.demo-title "Global Dropdown Management"]
   [:p.text-gray-600.dark:text-gray-400.mb-6
    "Test global dropdown behavior - opening one dropdown automatically closes others. Try opening multiple dropdowns to see this in action."]

   [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-6
    ;; First row
    [:div
     [:label.block.text-sm.font-medium.mb-2 "First Dropdown"]
     [:ty-dropdown {:value "a1"
                    :placeholder "Select option A..."
                    :on {:change dropdown-event-handler}}
      [:option {:value "a1"} "Option A1"]
      [:option {:value "a2"} "Option A2"]
      [:option {:value "a3"} "Option A3"]
      [:option {:value "a4"} "Option A4"]]]

    [:div
     [:label.block.text-sm.font-medium.mb-2 "Second Dropdown"]
     [:ty-dropdown {:value "b1"
                    :placeholder "Select option B..."
                    :on {:change dropdown-event-handler}}
      [:option {:value "b1"} "Option B1"]
      [:option {:value "b2"} "Option B2"]
      [:option {:value "b3"} "Option B3"]
      [:option {:value "b4"} "Option B4"]]]

    [:div
     [:label.block.text-sm.font-medium.mb-2 "Third Dropdown"]
     [:ty-dropdown {:value "c1"
                    :placeholder "Select option C..."
                    :on {:change dropdown-event-handler}}
      [:option {:value "c1"} "Option C1"]
      [:option {:value "c2"} "Option C2"]
      [:option {:value "c3"} "Option C3"]
      [:option {:value "c4"} "Option C4"]]]

    ;; Second row
    [:div
     [:label.block.text-sm.font-medium.mb-2 "Fourth Dropdown"]
     [:ty-dropdown {:value "d1"
                    :flavor "positive"
                    :placeholder "Positive dropdown..."
                    :on {:change dropdown-event-handler}}
      [:option {:value "d1"} "Positive D1"]
      [:option {:value "d2"} "Positive D2"]
      [:option {:value "d3"} "Positive D3"]]]

    [:div
     [:label.block.text-sm.font-medium.mb-2 "Fifth Dropdown"]
     [:ty-dropdown {:value "e1"
                    :flavor "negative"
                    :placeholder "Negative dropdown..."
                    :on {:change dropdown-event-handler}}
      [:option {:value "e1"} "Negative E1"]
      [:option {:value "e2"} "Negative E2"]
      [:option {:value "e3"} "Negative E3"]]]

    [:div
     [:label.block.text-sm.font-medium.mb-2 "Sixth Dropdown"]
     [:ty-dropdown {:value "f1"
                    :flavor "important"
                    :placeholder "Important dropdown..."
                    :on {:change dropdown-event-handler}}
      [:option {:value "f1"} "Important F1"]
      [:option {:value "f2"} "Important F2"]
      [:option {:value "f3"} "Important F3"]]]]

   [:div.mt-6.p-4.bg-blue-50.dark:bg-blue-900.border.border-blue-200.dark:border-blue-700.rounded
    [:h4.text-sm.font-medium.text-blue-800.dark:text-blue-200.mb-2 "Expected Behavior:"]
    [:ul.text-sm.text-blue-700.dark:text-blue-300.space-y-1
     [:li "✅ Only one dropdown can be open at a time"]
     [:li "✅ Opening a new dropdown closes any currently open dropdown"]
     [:li "✅ Clicking outside closes the open dropdown"]
     [:li "✅ ESC key closes the open dropdown"]
     [:li "✅ Smooth transitions without visible position jumps"]]]

   (code-snippet "<!-- Multiple dropdowns with automatic global management -->
<ty-dropdown placeholder=\"First dropdown...\">
  <option value=\"a1\">Option A1</option>
</ty-dropdown>

<ty-dropdown placeholder=\"Second dropdown...\">
  <option value=\"b1\">Option B1</option>
</ty-dropdown>

<!-- Only one will be open at a time automatically -->")])

(defn size-variants []
  [:div.demo-section
   [:h2.demo-title "Size Variants"]
   [:p.text-gray-600.dark:text-gray-400.mb-6
    "Five size variants to fit different contexts and layouts, plus mini width for compact spaces"]

   [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-5.gap-4
    [:div
     [:label.block.text-sm.font-medium.mb-1 "Extra Small (xs)"]
     [:ty-dropdown {:size "xs"
                    :value "xs1"
                    :placeholder "XS dropdown"}
      [:option {:value "xs1"} "XS Option 1"]
      [:option {:value "xs2"} "XS Option 2"]]]

    [:div
     [:label.block.text-sm.font-medium.mb-1 "Small (sm)"]
     [:ty-dropdown {:size "sm"
                    :value "sm1"
                    :placeholder "SM dropdown"}
      [:option {:value "sm1"} "SM Option 1"]
      [:option {:value "sm2"} "SM Option 2"]]]

    [:div
     [:label.block.text-sm.font-medium.mb-1 "Medium (md)"]
     [:ty-dropdown {:size "md"
                    :value "md1"
                    :placeholder "MD dropdown"}
      [:option {:value "md1"} "MD Option 1"]
      [:option {:value "md2"} "MD Option 2"]]]

    [:div
     [:label.block.text-sm.font-medium.mb-1 "Large (lg)"]
     [:ty-dropdown {:size "lg"
                    :value "lg1"
                    :placeholder "LG dropdown"}
      [:option {:value "lg1"} "LG Option 1"]
      [:option {:value "lg2"} "LG Option 2"]]]

    [:div
     [:label.block.text-sm.font-medium.mb-1 "Extra Large (xl)"]
     [:ty-dropdown {:size "xl"
                    :value "xl1"
                    :placeholder "XL dropdown"}
      [:option {:value "xl1"} "XL Option 1"]
      [:option {:value "xl2"} "XL Option 2"]]]]

   ;; Mini width examples
   [:div.mt-8
    [:h3.demo-subtitle "Mini Width Variant"]
    [:p.text-gray-600.dark:text-gray-400.mb-4
     "Add class=\"mini\" for compact 140px width - perfect for quantity selectors, status indicators, and tight layouts"]

    [:div.grid.grid-cols-2.md:grid-cols-4.lg:grid-cols-6.gap-4
     [:div
      [:label.block.text-sm.font-medium.mb-1 "Quantity"]
      [:ty-dropdown {:class "mini"
                     :size "sm"
                     :value "1"
                     :placeholder "Qty"}
       [:option {:value "1"} "1"]
       [:option {:value "2"} "2"]
       [:option {:value "3"} "3"]
       [:option {:value "4"} "4"]
       [:option {:value "5"} "5"]]]

     [:div
      [:label.block.text-sm.font-medium.mb-1 "Size"]
      [:ty-dropdown {:class "mini"
                     :size "sm"
                     :value "M"
                     :placeholder "Size"}
       [:option {:value "XS"} "XS"]
       [:option {:value "S"} "S"]
       [:option {:value "M"} "M"]
       [:option {:value "L"} "L"]
       [:option {:value "XL"} "XL"]]]

     [:div
      [:label.block.text-sm.font-medium.mb-1 "Status"]
      [:ty-dropdown {:class "mini"
                     :size "xs"
                     :value "OK"
                     :flavor "positive"
                     :placeholder "Status"}
       [:option {:value "OK"} "OK"]
       [:option {:value "ERR"} "ERR"]
       [:option {:value "WARN"} "WARN"]]]

     [:div
      [:label.block.text-sm.font-medium.mb-1 "Priority"]
      [:ty-dropdown {:class "mini"
                     :size "xs"
                     :value "H"
                     :flavor "important"
                     :placeholder "Priority"}
       [:option {:value "L"} "Low"]
       [:option {:value "M"} "Med"]
       [:option {:value "H"} "High"]]]

     [:div
      [:label.block.text-sm.font-medium.mb-1 "Type"]
      [:ty-dropdown {:class "mini"
                     :size "sm"
                     :value "A"
                     :flavor "unique"
                     :placeholder "Type"}
       [:option {:value "A"} "Type A"]
       [:option {:value "B"} "Type B"]
       [:option {:value "C"} "Type C"]]]

     [:div
      [:label.block.text-sm.font-medium.mb-1 "Grade"]
      [:ty-dropdown {:class "mini"
                     :size "sm"
                     :value "A+"
                     :flavor "exception"
                     :placeholder "Grade"}
       [:option {:value "A+"} "A+"]
       [:option {:value "A"} "A"]
       [:option {:value "B+"} "B+"]
       [:option {:value "B"} "B"]
       [:option {:value "C"} "C"]]]]]

   (code-snippet "<ty-dropdown size=\"lg\" placeholder=\"Large dropdown\">
  <option value=\"lg1\">LG Option 1</option>
  <option value=\"lg2\">LG Option 2</option>
</ty-dropdown>

<!-- Mini width variant - 140px wide -->
<ty-dropdown class=\"mini\" size=\"sm\" placeholder=\"Qty\">
  <option value=\"1\">1</option>
  <option value=\"2\">2</option>
  <option value=\"3\">3</option>
</ty-dropdown>")])

(defn flavor-variants []
  [:div.demo-section
   [:h2.demo-title "Flavor Variants"]
   [:p.text-gray-600.dark:text-gray-400.mb-6
    "Semantic flavors convey meaning and context"]

   [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-4
    [:div
     [:label.block.text-sm.font-medium.mb-1 "Neutral (default)"]
     [:ty-dropdown {:flavor "neutral"
                    :value "neutral1"
                    :placeholder "Neutral flavor"}
      [:option {:value "neutral1"} "Neutral Option 1"]
      [:option {:value "neutral2"} "Neutral Option 2"]]]

    [:div
     [:label.block.text-sm.font-medium.mb-1 "Positive"]
     [:ty-dropdown {:flavor "positive"
                    :value "pos1"
                    :placeholder "Positive flavor"}
      [:option {:value "pos1"} "Positive Option 1"]
      [:option {:value "pos2"} "Positive Option 2"]]]

    [:div
     [:label.block.text-sm.font-medium.mb-1 "Negative"]
     [:ty-dropdown {:flavor "negative"
                    :value "neg1"
                    :placeholder "Negative flavor"}
      [:option {:value "neg1"} "Negative Option 1"]
      [:option {:value "neg2"} "Negative Option 2"]]]

    [:div
     [:label.block.text-sm.font-medium.mb-1 "Important"]
     [:ty-dropdown {:flavor "important"
                    :value "imp1"
                    :placeholder "Important flavor"}
      [:option {:value "imp1"} "Important Option 1"]
      [:option {:value "imp2"} "Important Option 2"]]]

    [:div
     [:label.block.text-sm.font-medium.mb-1 "Exception"]
     [:ty-dropdown {:flavor "exception"
                    :value "exc1"
                    :placeholder "Exception flavor"}
      [:option {:value "exc1"} "Exception Option 1"]
      [:option {:value "exc2"} "Exception Option 2"]]]

    [:div
     [:label.block.text-sm.font-medium.mb-1 "Unique"]
     [:ty-dropdown {:flavor "unique"
                    :value "unq1"
                    :placeholder "Unique flavor"}
      [:option {:value "unq1"} "Unique Option 1"]
      [:option {:value "unq2"} "Unique Option 2"]]]]

   (code-snippet "<ty-dropdown flavor=\"positive\" placeholder=\"Positive flavor\">
  <option value=\"pos1\">Positive Option 1</option>
  <option value=\"pos2\">Positive Option 2</option>
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

   (code-snippet "<ty-dropdown placeholder=\"Search languages...\">
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
   [:h2.demo-title "Sizing & Flavors with Rich Content"]
   [:p.text-gray-600.dark:text-gray-400.mb-6
    "Dropdowns support different sizes (xs, sm, md, lg, xl) and semantic flavors (neutral, important, positive, negative, exception, unique) while preserving rich HTML content."]

   [:div.space-y-8
    ;; Size Examples
    [:div
     [:h3.demo-subtitle "Different Sizes"]
     [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-4
      ;; Extra Small
      [:div
       [:label.block.text-sm.font-medium.mb-2 "Extra Small (xs)"]
       [:ty-dropdown {:size "xs"
                      :placeholder "Choose..."}
        [:ty-option {:value "react"}
         [:div.flex.items-center.gap-2
          [:div.w-4.h-4.bg-blue-500.rounded.flex.items-center.justify-center.text-white.text-xs "R"]
          [:span.text-sm "React"]]]
        [:ty-option {:value "vue"}
         [:div.flex.items-center.gap-2
          [:div.w-4.h-4.bg-green-500.rounded.flex.items-center.justify-center.text-white.text-xs "V"]
          [:span.text-sm "Vue"]]]]]

      ;; Small
      [:div
       [:label.block.text-sm.font-medium.mb-2 "Small (sm)"]
       [:ty-dropdown {:size "sm"
                      :placeholder "Choose..."}
        [:ty-option {:value "react"}
         [:div.flex.items-center.gap-2
          [:div.w-5.h-5.bg-blue-500.rounded.flex.items-center.justify-center.text-white.text-xs "R"]
          [:span "React"]]]
        [:ty-option {:value "vue"}
         [:div.flex.items-center.gap-2
          [:div.w-5.h-5.bg-green-500.rounded.flex.items-center.justify-center.text-white.text-xs "V"]
          [:span "Vue"]]]]]

      ;; Large
      [:div
       [:label.block.text-sm.font-medium.mb-2 "Large (lg)"]
       [:ty-dropdown {:size "lg"
                      :placeholder "Choose..."}
        [:ty-option {:value "react"}
         [:div.flex.items-center.items-center.gap-3
          [:div.w-6.h-6.bg-blue-500.rounded.flex.items-center.justify-center.text-white.text-sm "R"]
          [:span.text-lg "React"]]]
        [:ty-option {:value "vue"}
         [:div.flex.items-center.items-center.gap-3
          [:div.w-6.h-6.bg-green-500.rounded.flex.items-center.justify-center.text-white.text-sm "V"]
          [:span.text-lg "Vue"]]]]]]]

    ;; Flavor Examples
    [:div
     [:h3.demo-subtitle "Semantic Flavors"]
     [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
      ;; Important (Blue)
      [:div
       [:label.block.text-sm.font-medium.mb-2 "Important (Blue)"]
       [:ty-dropdown {:flavor "important"
                      :placeholder "Select priority..."}
        [:ty-option {:value "high"}
         [:div.flex.items-center.items-center.gap-3
          [:div.w-3.h-3.bg-blue-500.rounded-full]
          [:span "High Priority"]]]
        [:ty-option {:value "medium"}
         [:div.flex.items-center.items-center.gap-3
          [:div.w-3.h-3.bg-blue-300.rounded-full]
          [:span "Medium Priority"]]]]]

      ;; Positive (Green)
      [:div
       [:label.block.text-sm.font-medium.mb-2 "Positive (Green)"]
       [:ty-dropdown {:flavor "positive"
                      :placeholder "Select status..."}
        [:ty-option {:value "approved"}
         [:div.flex.items-center.items-center.gap-3
          [:div.w-3.h-3.bg-green-500.rounded-full]
          [:span "Approved"]]]
        [:ty-option {:value "completed"}
         [:div.flex.items-center.items-center.gap-3
          [:div.w-3.h-3.bg-green-400.rounded-full]
          [:span "Completed"]]]]]

      ;; Negative (Red)
      [:div
       [:label.block.text-sm.font-medium.mb-2 "Negative (Red)"]
       [:ty-dropdown {:flavor "negative"
                      :placeholder "Select error..."}
        [:ty-option {:value "failed"}
         [:div.flex.items-center.items-center.gap-3
          [:div.w-3.h-3.bg-red-500.rounded-full]
          [:span "Failed"]]]
        [:ty-option {:value "rejected"}
         [:div.flex.items-center.items-center.gap-3
          [:div.w-3.h-3.bg-red-400.rounded-full]
          [:span "Rejected"]]]]]

      ;; Exception (Yellow)
      [:div
       [:label.block.text-sm.font-medium.mb-2 "Exception (Yellow)"]
       [:ty-dropdown {:flavor "exception"
                      :placeholder "Select warning..."}
        [:ty-option {:value "warning"}
         [:div.flex.items-center.items-center.gap-3
          [:div.w-3.h-3.bg-yellow-500.rounded-full]
          [:span "Warning"]]]
        [:ty-option {:value "caution"}
         [:div.flex.items-center.items-center.gap-3
          [:div.w-3.h-3.bg-yellow-400.rounded-full]
          [:span "Caution"]]]]]]]

    ;; Combined Example
    [:div
     [:h3.demo-subtitle "Combined Size & Flavor"]
     [:div.max-w-xl
      [:label.block.text-sm.font-medium.mb-2 "Large + Unique (Purple)"]
      [:ty-dropdown {:size "lg"
                     :flavor "unique"
                     :placeholder "Choose your superpower..."}
       [:ty-option {:value "flight"}
        [:div.flex.items-center.items-center.gap-3
         [:div.w-8.h-8.bg-purple-100.rounded-full.flex.items-center.justify-center
          [:span.text-purple-600.text-lg "✈️"]]
         [:div
          [:div.font-medium.text-lg "Flight"]
          [:div.text-sm.text-gray-500 "Soar through the skies"]]]]
       [:ty-option {:value "invisibility"}
        [:div.flex.items-center.items-center.gap-3
         [:div.w-8.h-8.bg-purple-100.rounded-full.flex.items-center.justify-center
          [:span.text-purple-600.text-lg "👻"]]
         [:div
          [:div.font-medium.text-lg "Invisibility"]
          [:div.text-sm.text-gray-500 "Become unseen at will"]]]]
       [:ty-option {:value "telepathy"}
        [:div.flex.items-center.items-center.gap-3
         [:div.w-8.h-8.bg-purple-100.rounded-full.flex.items-center.justify-center
          [:span.text-purple-600.text-lg "🧠"]]
         [:div
          [:div.font-medium.text-lg "Telepathy"]
          [:div.text-sm.text-gray-500 "Read minds and communicate"]]]]]]]]

   (code-snippet "<!-- Size and flavor examples -->
<ty-dropdown size=\"lg\" flavor=\"unique\" placeholder=\"Choose...\">
  <ty-option value=\"special\">
    <div class=\"flex items-center gap-3\">
      <div class=\"w-8 h-8 bg-purple-100 rounded-full flex items-center justify-center\">
        <span class=\"text-purple-600 text-lg\">✨</span>
      </div>
      <div>
        <div class=\"font-medium text-lg\">Special Option</div>
        <div class=\"text-sm text-gray-500\">With rich content</div>
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
    (str (:dropdown-value @state/state))]

   [:div.mt-6.max-w-sm
    [:ty-dropdown {:value (:dropdown-value @state/state "")
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

(defn render []
  [:div.max-w-6xl.mx-auto
   [:div.mb-8
    [:h1.text-3xl.font-bold.text-gray-900.dark:text-white.mb-2
     "Dropdown Component"]
    [:p.text-lg.text-gray-600.dark:text-gray-400
     "A powerful dropdown component with smart positioning, search filtering, keyboard navigation, global management, and semantic styling."]]

   [:div.space-y-12
    (basic-examples)
    (multiple-dropdowns-test)
    (size-variants)
    (flavor-variants)
    (search-examples)
    (blur-clear-demo)
    (rich-html-demo)
    (sizing-flavors-demo)
    (event-debugging)]])
