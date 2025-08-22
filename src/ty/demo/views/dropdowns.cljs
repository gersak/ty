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

(defn smart-positioning []
  [:div.demo-section
   [:h2.demo-title "Smart Positioning"]
   [:p.text-gray-600.dark:text-gray-400.mb-6
    "Intelligent positioning that adapts to viewport constraints and prevents overflow"]

   (demo-row {:title "Auto-Placement (Default)"
              :description "Automatically chooses the best position to avoid viewport overflow"
              :children [[:div.max-w-xs
                          [:ty-dropdown {:value "auto1"
                                         :placeholder "Smart positioning"
                                         :auto-placement true}
                           [:option {:value "auto1"} "Automatically positioned"]
                           [:option {:value "auto2"} "Collision detection"]
                           [:option {:value "auto3"} "Viewport aware"]
                           [:option {:value "auto4"} "Intelligent fallbacks"]
                           [:option {:value "auto5"} "Smooth transitions"]]]]})
   (code-snippet "<ty-dropdown auto-placement=\"true\">
  <option value=\"auto1\">Automatically positioned</option>
</ty-dropdown>")

   (demo-row {:title "Manual Placement"
              :description "Explicitly control dropdown placement direction"
              :children [[:div.grid.grid-cols-2.gap-4.max-w-md
                          [:div
                           [:label.block.text-sm.font-medium.mb-1 "Bottom Start"]
                           [:ty-dropdown {:value "bs1"
                                          :placement "bottom-start"
                                          :auto-placement false
                                          :placeholder "Bottom start"}
                            [:option {:value "bs1"} "Bottom Start 1"]
                            [:option {:value "bs2"} "Bottom Start 2"]]]
                          [:div
                           [:label.block.text-sm.font-medium.mb-1 "Top Start"]
                           [:ty-dropdown {:value "ts1"
                                          :placement "top-start"
                                          :auto-placement false
                                          :placeholder "Top start"}
                            [:option {:value "ts1"} "Top Start 1"]
                            [:option {:value "ts2"} "Top Start 2"]]]]]})
   (code-snippet "<ty-dropdown placement=\"top-start\" auto-placement=\"false\">
  <option value=\"ts1\">Top Start 1</option>
</ty-dropdown>")

   (demo-row {:title "Offset Control"
              :description "Adjust the distance between input and dropdown"
              :children [[:div.grid.grid-cols-3.gap-4.max-w-lg
                          [:div
                           [:label.block.text-sm.font-medium.mb-1 "Small Offset (2px)"]
                           [:ty-dropdown {:value "off1"
                                          :offset 2
                                          :placeholder "2px offset"}
                            [:option {:value "off1"} "Close to input"]
                            [:option {:value "off2"} "Minimal gap"]]]
                          [:div
                           [:label.block.text-sm.font-medium.mb-1 "Default (4px)"]
                           [:ty-dropdown {:value "off3"
                                          :placeholder "Default offset"}
                            [:option {:value "off3"} "Standard gap"]
                            [:option {:value "off4"} "Default spacing"]]]
                          [:div
                           [:label.block.text-sm.font-medium.mb-1 "Large Offset (12px)"]
                           [:ty-dropdown {:value "off5"
                                          :offset 12
                                          :placeholder "12px offset"}
                            [:option {:value "off5"} "Far from input"]
                            [:option {:value "off6"} "Large gap"]]]]]})
   (code-snippet "<ty-dropdown offset=\"12\">
  <option value=\"off5\">Far from input</option>
</ty-dropdown>")

   ;; Viewport edge testing
   [:div.mt-8
    [:h3.demo-subtitle "Viewport Edge Testing"]
    [:p.text-gray-600.dark:text-gray-400.mb-4
     "Test smart positioning by scrolling these dropdowns near viewport edges. Notice how transitions are smooth without visible jumps."]

    [:div.h-96.overflow-auto.border.border-gray-200.dark:border-gray-700.rounded.p-4
     [:div.h-20] ; Top spacer

     ;; Top edge test
     [:div.mb-4
      [:label.block.text-sm.font-medium.mb-1 "Near Top Edge"]
      [:ty-dropdown {:value "edge1"
                     :placeholder "Should flip down when near top"}
       [:option {:value "edge1"} "Option 1"]
       [:option {:value "edge2"} "Option 2"]
       [:option {:value "edge3"} "Option 3"]
       [:option {:value "edge4"} "Option 4"]
       [:option {:value "edge5"} "Option 5"]]]

     [:div.h-64] ; Middle spacer

     ;; Bottom edge test
     [:div.mb-4
      [:label.block.text-sm.font-medium.mb-1 "Near Bottom Edge"]
      [:ty-dropdown {:value "edge6"
                     :placeholder "Should flip up when near bottom"}
       [:option {:value "edge6"} "Option 6"]
       [:option {:value "edge7"} "Option 7"]
       [:option {:value "edge8"} "Option 8"]
       [:option {:value "edge9"} "Option 9"]
       [:option {:value "edge10"} "Option 10"]]]

     [:div.h-20]]] ; Bottom spacer

   (code-snippet "<!-- Dropdown automatically flips direction near viewport edges -->
<ty-dropdown auto-placement=\"true\">
  <option>Viewport-aware positioning</option>
</ty-dropdown>")])

(defn responsive-example []
  (layout/with-window
    [:div.demo-section
     [:h2.demo-title "Responsive Example"]
     [:p.text-gray-600.dark:text-gray-400.mb-6
      "This dropdown adapts its size based on the container width using layout context."]

     [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-4
      [:div.border.border-gray-200.dark:border-gray-700.p-4.rounded
       [:label.block.text-sm.font-medium.mb-2 "Narrow Container"]
       [:ty-dropdown {:size (if (layout/breakpoint>= :md) "lg" "sm")
                      :value "responsive1"
                      :placeholder "Responsive dropdown"}
        [:option {:value "responsive1"} "Responsive Option 1"]
        [:option {:value "responsive2"} "Responsive Option 2"]
        [:option {:value "responsive3"} "Responsive Option 3"]]]

      [:div.border.border-gray-200.dark:border-gray-700.p-4.rounded.md:col-span-2
       [:label.block.text-sm.font-medium.mb-2 "Wide Container"]
       [:ty-dropdown {:size (if (layout/breakpoint>= :lg) "xl" "md")
                      :value "responsive4"
                      :placeholder "Another responsive dropdown"}
        [:option {:value "responsive4"} "Wide Option 1"]
        [:option {:value "responsive5"} "Wide Option 2"]
        [:option {:value "responsive6"} "Wide Option 3"]]]]

     (code-snippet "<ty-dropdown size={breakpointMd ? \"lg\" : \"sm\"}>
  <option value=\"responsive1\">Responsive Option 1</option>
</ty-dropdown>")]))

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
    (smart-positioning)
    (responsive-example)
    (search-examples)
    (event-debugging)]])
