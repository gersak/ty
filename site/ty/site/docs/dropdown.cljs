(ns ty.site.docs.dropdown
  "Documentation for ty-dropdown component"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table doc-section example-section]]))

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Title and Description
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text++.mb-2 "ty-dropdown"]
    [:p.text-lg.ty-text-
     "A powerful, searchable dropdown component with rich content support. "
     "Features automatic search filtering, keyboard navigation, and semantic styling. "
     "Use ty-option elements to create beautiful, interactive dropdown menus."]]

   ;; API Reference Card
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2#attributes.text-2xl.font-bold.ty-text++.mb-4 "API Reference"]

    ;; Attributes Table
    (attribute-table
     [{:name "value"
       :type "string"
       :default "null"
       :description "Selected option value"}
      {:name "placeholder"
       :type "string"
       :default "null"
       :description "Placeholder text when no option is selected"}
      {:name "label"
       :type "string"
       :default "null"
       :description "Label displayed above the dropdown"}
      {:name "name"
       :type "string"
       :default "null"
       :description "Name for form submission"}
      {:name "searchable"
       :type "boolean"
       :default "true"
       :description "Enable/disable search functionality"}
      {:name "disabled"
       :type "boolean"
       :default "false"
       :description "Whether the dropdown is disabled"}
      {:name "readonly"
       :type "boolean"
       :default "false"
       :description "Read-only mode (shows value, prevents interaction)"}
      {:name "required"
       :type "boolean"
       :default "false"
       :description "Whether the dropdown is required (shows asterisk)"}
      {:name "size"
       :type "string"
       :default "'md'"
       :description "Size variant: xs, sm, md, lg, xl"}
      {:name "flavor"
       :type "string"
       :default "'neutral'"
       :description "Semantic flavor: primary, secondary, success, danger, warning, neutral"}
      {:name "class"
       :type "string"
       :default "null"
       :description "Additional CSS classes"}])

    [:div.mt-6
     [:h3.text-lg.font-semibold.ty-text++.mb-2 "Events"]
     (event-table
      [{:name "change"
        :when-fired "Fired when selection changes"
        :payload "{option: selectedOption, value, text, originalEvent}"}
       {:name "search"
        :when-fired "Fired when user types in search field"
        :payload "{query: searchString, originalEvent}"}
       {:name "focus"
        :when-fired "Fired when dropdown gains focus"
        :payload "Standard FocusEvent"}
       {:name "blur"
        :when-fired "Fired when dropdown loses focus"
        :payload "Standard FocusEvent"}])

     [:div.mt-4
      [:h3.text-lg.font-semibold.ty-text++.mb-2 "Slots"]
      [:div.ty-bg-neutral-.rounded.p-4
       [:p.text-sm.ty-text- "Content slot accepts " [:code.ty-bg-neutral.px-1.rounded "ty-option"] " elements:"]
       [:pre.text-xs.ty-text-.mt-2
        [:code "<!-- Use ty-option, not HTML option -->\n<ty-option value=\"red\">Red Option</ty-option>\n<ty-option value=\"blue\">🔵 Blue Option</ty-option>"]]]]]]

   ;; Basic Usage
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-bold.ty-text++.mb-4 "Basic Usage"]

    [:div.mb-6
     [:h3.text-lg.font-semibold.ty-text+.mb-2 "Simple Dropdown"]
     [:div.mb-4
      [:ty-dropdown {:placeholder "Select a color..."
                     :style {:min-width "200px"}}
       [:ty-option {:value "red"} "Red"]
       [:ty-option {:value "blue"} "Blue"]
       [:ty-option {:value "green"} "Green"]
       [:ty-option {:value "yellow"} "Yellow"]]]
     (code-block "<ty-dropdown placeholder=\"Select a color...\" style=\"min-width: 200px;\">
  <ty-option value=\"red\">Red</ty-option>
  <ty-option value=\"blue\">Blue</ty-option>
  <ty-option value=\"green\">Green</ty-option>
  <ty-option value=\"yellow\">Yellow</ty-option>
</ty-dropdown>")]

    [:div.mb-6
     [:h3.text-lg.font-semibold.ty-text+.mb-2 "With Label and Value"]
     [:div.mb-4
      [:ty-dropdown {:value "medium"
                     :label "Size"
                     :placeholder "Select size..."
                     :style {:min-width "180px"}}
       [:ty-option {:value "small"} "Small"]
       [:ty-option {:value "medium"} "Medium"]
       [:ty-option {:value "large"} "Large"]
       [:ty-option {:value "xl"} "Extra Large"]]]
     (code-block "<ty-dropdown value=\"medium\" label=\"Size\" placeholder=\"Select size...\">
  <ty-option value=\"small\">Small</ty-option>
  <ty-option value=\"medium\">Medium</ty-option>
  <ty-option value=\"large\">Large</ty-option>
  <ty-option value=\"xl\">Extra Large</ty-option>
</ty-dropdown>")]

    [:div
     [:h3.text-lg.font-semibold.ty-text+.mb-2 "Non-searchable"]
     [:div.mb-4
      [:ty-dropdown {:searchable "false"
                     :placeholder "Select priority..."
                     :style {:min-width "160px"}}
       [:ty-option {:value "low"} "Low"]
       [:ty-option {:value "medium"} "Medium"]
       [:ty-option {:value "high"} "High"]
       [:ty-option {:value "urgent"} "Urgent"]]]
     (code-block "<ty-dropdown searchable=\"false\" placeholder=\"Select priority...\">
  <ty-option value=\"low\">Low</ty-option>
  <ty-option value=\"medium\">Medium</ty-option>
  <ty-option value=\"high\">High</ty-option>
  <ty-option value=\"urgent\">Urgent</ty-option>
</ty-dropdown>")]]

   ;; Rich Content Examples
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Rich Content Examples"]
   [:p.ty-text-.mb-4 "ty-option supports rich HTML content with full styling preserved. Search still works by filtering on text content while showing rich UI."]

   [:div.space-y-8

    ;; Programming Languages 
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-xl.font-semibold.ty-text++.mb-4 "Programming Languages with Descriptions"]
     [:p.ty-text-.mb-4 "Each option includes an icon badge and formatted two-line description. Notice how the full styling appears when selected."]

     [:div.mb-4
      [:ty-dropdown {:value "clojure"
                     :placeholder "Choose a programming language..."
                     :style {:min-width "300px"}}
       [:ty-option {:value "javascript"}
        [:div.flex.items-center.gap-3
         [:div.w-6.h-6.rounded.bg-yellow-400.flex.items-center.justify-center.text-black.text-xs.font-bold "JS"]
         [:div
          [:div.font-medium.ty-text "JavaScript"]
          [:div.text-sm.ty-text-- "Dynamic scripting language"]]]]
       [:ty-option {:value "typescript"}
        [:div.flex.items-center.gap-3
         [:div.w-6.h-6.rounded.bg-blue-600.flex.items-center.justify-center.text-white.text-xs.font-bold "TS"]
         [:div
          [:div.font-medium.ty-text "TypeScript"]
          [:div.text-sm.ty-text-- "JavaScript with static typing"]]]]
       [:ty-option {:value "clojure"}
        [:div.flex.items-center.gap-3
         [:div.w-6.h-6.rounded.bg-green-600.flex.items-center.justify-center.text-white.text-xs.font-bold "λ"]
         [:div
          [:div.font-medium.ty-text "Clojure"]
          [:div.text-sm.ty-text-- "Functional Lisp for the JVM"]]]]
       [:ty-option {:value "rust"}
        [:div.flex.items-center.gap-3
         [:div.w-6.h-6.rounded.bg-orange-600.flex.items-center.justify-center.text-white.text-xs.font-bold "R"]
         [:div
          [:div.font-medium.ty-text "Rust"]
          [:div.text-sm.ty-text-- "Memory-safe systems programming"]]]]]]

     (code-block "<ty-dropdown value=\"clojure\" placeholder=\"Choose language...\">
  <ty-option value=\"javascript\">
    <div class=\"flex items-center gap-3\">
      <div class=\"w-6 h-6 rounded bg-yellow-400 flex items-center justify-center text-black text-xs font-bold\">JS</div>
      <div>
        <div class=\"font-medium ty-text\">JavaScript</div>
        <div class=\"text-sm ty-text--\">Dynamic scripting language</div>
      </div>
    </div>
  </ty-option>
</ty-dropdown>")]

    ;; Team Members with Status
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-xl.font-semibold.ty-text++.mb-4 "Team Members with Status"]
     [:p.ty-text-.mb-4 "Options with avatars, names, roles, and online status indicators using flexible layouts."]

     [:div.mb-4
      [:ty-dropdown {:value "alice"
                     :placeholder "Assign to team member..."
                     :style {:min-width "280px"}}
       [:ty-option {:value "alice"}
        [:div.flex.items-center.gap-3
         [:div.relative.flex-shrink-0
          [:div.w-8.h-8.rounded-full.bg-blue-500.flex.items-center.justify-center.text-white.font-medium "A"]
          [:div.absolute.-top-1.-right-1.w-3.h-3.bg-green-500.rounded-full.border-2.border-white]]
         [:div.flex-1.min-w-0
          [:div.font-medium "Alice Johnson"]
          [:div.text-sm.ty-text-- "Senior Developer"]]
         [:span.px-2.py-1.text-xs.bg-green-100.text-green-800.rounded-full.flex-shrink-0 "Online"]]]
       [:ty-option {:value "bob"}
        [:div.flex.items-center.gap-3
         [:div.relative.flex-shrink-0
          [:div.w-8.h-8.rounded-full.bg-purple-500.flex.items-center.justify-center.text-white.font-medium "B"]
          [:div.absolute.-top-1.-right-1.w-3.h-3.bg-yellow-500.rounded-full.border-2.border-white]]
         [:div.flex-1.min-w-0
          [:div.font-medium "Bob Smith"]
          [:div.text-sm.ty-text-- "Product Manager"]]
         [:span.px-2.py-1.text-xs.bg-yellow-100.text-yellow-800.rounded-full.flex-shrink-0 "Away"]]]
       [:ty-option {:value "carol"}
        [:div.flex.items-center.gap-3
         [:div.relative.flex-shrink-0
          [:div.w-8.h-8.rounded-full.bg-pink-500.flex.items-center.justify-center.text-white.font-medium "C"]
          [:div.absolute.-top-1.-right-1.w-3.h-3.bg-gray-400.rounded-full.border-2.border-white]]
         [:div.flex-1.min-w-0
          [:div.font-medium "Carol Davis"]
          [:div.text-sm.ty-text-- "UX Designer"]]
         [:span.px-2.py-1.text-xs.bg-gray-100.text-gray-800.rounded-full.flex-shrink-0 "Offline"]]]]]

     (code-block "<ty-dropdown value=\"alice\" placeholder=\"Assign to team member...\">
  <ty-option value=\"alice\">
    <div class=\"flex items-center gap-3\">
      <div class=\"relative flex-shrink-0\">
        <div class=\"w-8 h-8 rounded-full bg-blue-500 flex items-center justify-center text-white font-medium\">A</div>
        <div class=\"absolute -top-1 -right-1 w-3 h-3 bg-green-500 rounded-full border-2 border-white\"></div>
      </div>
      <div class=\"flex-1 min-w-0\">
        <div class=\"font-medium\">Alice Johnson</div>
        <div class=\"text-sm ty-text--\">Senior Developer</div>
      </div>
      <span class=\"px-2 py-1 text-xs bg-green-100 text-green-800 rounded-full flex-shrink-0\">Online</span>
    </div>
  </ty-option>
</ty-dropdown>")]

    ;; Files with Metadata
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-xl.font-semibold.ty-text++.mb-4 "Files with Metadata"]
     [:p.ty-text-.mb-4 "File picker with file type badges, sizes, and modification dates - perfect for document selection."]

     [:div.mb-4
      [:ty-dropdown {:value "report.pdf"
                     :placeholder "Choose a file..."
                     :style {:min-width "320px"}}
       [:ty-option {:value "report.pdf"}
        [:div.flex.items-center.gap-3
         [:div.w-8.h-8.bg-red-100.rounded.flex.items-center.justify-center
          [:span.text-red-600.text-xs.font-bold "PDF"]]
         [:div.flex-1
          [:div.font-medium "Annual Report 2024.pdf"]
          [:div.text-sm.ty-text-- "2.4 MB • Modified 2 hours ago"]]]]
       [:ty-option {:value "spreadsheet.xlsx"}
        [:div.flex.items-center.gap-3
         [:div.w-8.h-8.bg-green-100.rounded.flex.items-center.justify-center
          [:span.text-green-600.text-xs.font-bold "XLS"]]
         [:div.flex-1
          [:div.font-medium "Budget_Q4.xlsx"]
          [:div.text-sm.ty-text-- "856 KB • Modified yesterday"]]]]
       [:ty-option {:value "presentation.pptx"}
        [:div.flex.items-center.gap-3
         [:div.w-8.h-8.bg-orange-100.rounded.flex.items-center.justify-center
          [:span.text-orange-600.text-xs.font-bold "PPT"]]
         [:div.flex-1
          [:div.font-medium "Project_Kickoff.pptx"]
          [:div.text-sm.ty-text-- "12.3 MB • Modified last week"]]]]]]

     (code-block "<ty-dropdown value=\"report.pdf\" placeholder=\"Choose a file...\">
  <ty-option value=\"report.pdf\">
    <div class=\"flex items-center gap-3\">
      <div class=\"w-8 h-8 bg-red-100 rounded flex items-center justify-center\">
        <span class=\"text-red-600 text-xs font-bold\">PDF</span>
      </div>
      <div class=\"flex-1\">
        <div class=\"font-medium\">Annual Report 2024.pdf</div>
        <div class=\"text-sm ty-text--\">2.4 MB • Modified 2 hours ago</div>
      </div>
    </div>
  </ty-option>
</ty-dropdown>")]]

   ;; Size and Flavor Examples
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Sizes and Flavors"]

   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h3.text-xl.font-semibold.ty-text++.mb-4 "Size Variants"]
    [:p.ty-text-.mb-4 "Different sizes for various contexts and layouts."]
    [:div.flex.flex-wrap.gap-4.items-start

     [:div
      [:ty-dropdown {:size "sm"
                     :placeholder "Small"
                     :style {:min-width "120px"}}
       [:ty-option {:value "sm1"} "Small 1"]
       [:ty-option {:value "sm2"} "Small 2"]]]

     [:div
      [:ty-dropdown {:size "md"
                     :placeholder "Medium (default)"
                     :style {:min-width "150px"}}
       [:ty-option {:value "md1"} "Medium 1"]
       [:ty-option {:value "md2"} "Medium 2"]]]

     [:div
      [:ty-dropdown {:size "lg"
                     :placeholder "Large"
                     :style {:min-width "180px"}}
       [:ty-option {:value "lg1"} "Large 1"]
       [:ty-option {:value "lg2"} "Large 2"]]]]

    (code-block "<ty-dropdown size=\"sm\" placeholder=\"Small\">...</ty-dropdown>
<ty-dropdown size=\"md\" placeholder=\"Medium (default)\">...</ty-dropdown>
<ty-dropdown size=\"lg\" placeholder=\"Large\">...</ty-dropdown>")]

   [:div.ty-content.rounded-lg.p-6
    [:h3.text-xl.font-semibold.ty-text++.mb-4 "Semantic Flavors"]
    [:p.ty-text-.mb-4 "Use semantic flavors to convey meaning and importance."]
    [:div.flex.flex-wrap.gap-4.items-start

     [:div
      [:ty-dropdown {:flavor "primary"
                     :placeholder "Primary"
                     :style {:min-width "140px"}}
       [:ty-option {:value "p1"} "Primary 1"]
       [:ty-option {:value "p2"} "Primary 2"]]]

     [:div
      [:ty-dropdown {:flavor "success"
                     :value "success"
                     :style {:min-width "140px"}}
       [:ty-option {:value "success"} "✓ Success"]
       [:ty-option {:value "valid"} "✓ Valid"]]]

     [:div
      [:ty-dropdown {:flavor "warning"
                     :placeholder "Warning"
                     :style {:min-width "140px"}}
       [:ty-option {:value "w1"} "⚠ Warning 1"]
       [:ty-option {:value "w2"} "⚠ Warning 2"]]]

     [:div
      [:ty-dropdown {:flavor "danger"
                     :placeholder "Danger"
                     :style {:min-width "140px"}}
       [:ty-option {:value "d1"} "✗ Error 1"]
       [:ty-option {:value "d2"} "✗ Error 2"]]]]

    (code-block "<ty-dropdown flavor=\"primary\" placeholder=\"Primary\">...</ty-dropdown>
<ty-dropdown flavor=\"success\" value=\"success\">...</ty-dropdown>
<ty-dropdown flavor=\"warning\" placeholder=\"Warning\">...</ty-dropdown>
<ty-dropdown flavor=\"danger\" placeholder=\"Danger\">...</ty-dropdown>")]

   ;; States Section
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "States"]
   [:div.ty-content.rounded-lg.p-6
    [:p.ty-text-.mb-4 "Different states for various use cases and contexts."]
    [:div.flex.flex-wrap.gap-4.items-start

     [:div
      [:ty-dropdown {:label "Required Field"
                     :required "true"
                     :placeholder "Required dropdown"
                     :style {:min-width "160px"}}
       [:ty-option {:value "opt1"} "Option 1"]
       [:ty-option {:value "opt2"} "Option 2"]]]

     [:div
      [:ty-dropdown {:label "Disabled"
                     :disabled "true"
                     :value "disabled-val"
                     :style {:min-width "160px"}}
       [:ty-option {:value "disabled-val"} "Disabled Value"]
       [:ty-option {:value "other"} "Other Option"]]]

     [:div
      [:ty-dropdown {:label "Read-only"
                     :readonly "true"
                     :value "readonly-val"
                     :style {:min-width "160px"}}
       [:ty-option {:value "readonly-val"} "Read-only Value"]
       [:ty-option {:value "other"} "Other Option"]]]]

    (code-block "<ty-dropdown label=\"Required Field\" required=\"true\">...</ty-dropdown>
<ty-dropdown label=\"Disabled\" disabled=\"true\" value=\"disabled-val\">...</ty-dropdown>
<ty-dropdown label=\"Read-only\" readonly=\"true\" value=\"readonly-val\">...</ty-dropdown>")]

   ;; Best Practices Section
   [:div.ty-elevated.rounded-lg.p-6
    [:h2.text-2xl.font-bold.ty-text++.mb-4 "Best Practices"]

    [:div.grid.md:grid-cols-2.gap-6
     ;; Do's
     [:div
      [:h3.text-lg.font-semibold.ty-text-success++.mb-3.flex.items-center.gap-2
       [:ty-icon {:name "check-circle"
                  :size "20"}]
       "Do's"]
      [:ul.space-y-2.ty-text-
       [:li "• Use ty-option elements (not HTML option)"]
       [:li "• Provide clear, descriptive labels"]
       [:li "• Use rich content for better UX (badges, avatars, descriptions)"]
       [:li "• Set appropriate min-width for content"]
       [:li "• Use semantic flavors for meaning"]
       [:li "• Enable search for long lists (default behavior)"]]]

     ;; Don'ts
     [:div
      [:h3.text-lg.font-semibold.ty-text-danger++.mb-3.flex.items-center.gap-2
       [:ty-icon {:name "x-circle"
                  :size "20"}]
       "Don'ts"]
      [:ul.space-y-2.ty-text-
       [:li "• Don't use HTML option elements"]
       [:li "• Don't make dropdowns too narrow for content"]
       [:li "• Don't disable search for long lists"]
       [:li "• Don't use placeholder as label replacement"]
       [:li "• Don't overcomplicate option layouts"]
       [:li "• Don't forget to set value attributes"]
       [:li "• Don't mix different visual styles in same dropdown"]]]]

    [:div.mt-6.p-4.ty-bg-primary-.rounded
     [:h3.font-semibold.ty-text-primary++.mb-2.flex.items-center.gap-2
      [:ty-icon {:name "lightbulb" :size "18"}]
      "Key Tips"]
     [:ul.space-y-1.text-sm.ty-text-primary
      [:li "• Search is enabled by default - users can type to filter options"]
      [:li "• Use two-line layouts (title + description) for rich information"]
      [:li "• The dropdown automatically handles keyboard navigation"]
      [:li "• Set explicit min-width styles to prevent layout shifts"]
      [:li "• Rich content works great for teams, files, languages, and complex data"]]]]

   ;; Related Components
   [:div.mt-8.p-4.ty-border.border.rounded-lg
    [:h3.font-semibold.ty-text+.mb-2 "Related Components"]
    [:div.flex.gap-4.text-sm
     [:a.ty-text-primary.hover:underline {:href "/docs/input"} "ty-input →"]
     [:a.ty-text-primary.hover:underline {:href "/docs/multiselect"} "ty-multiselect →"]
     [:a.ty-text-primary.hover:underline {:href "/docs/button"} "ty-button →"]]]])