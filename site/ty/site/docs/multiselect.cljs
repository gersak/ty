(ns ty.site.docs.multiselect
  "Documentation for ty-multiselect component"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table doc-section example-section]]
            [ty.site.state :as state]))

(defn multiselect-event-handler [event]
  (let [detail (.-detail event)
        values (js->clj (.-values detail) :keywordize-keys false)
        action (.-action detail)
        item (.-item detail)]
    (js/console.log "Multiselect changed:"
                    (clj->js {:values values
                              :action action
                              :item item}))))

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Title and Description
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text++.mb-2 "ty-multiselect"]
    [:p.text-lg.ty-text-
     "A powerful multiselect component that allows selecting multiple options with tag-based display. "
     "Built on the dropdown foundation with automatic tag management, search filtering, and semantic styling. "
     "Uses ty-tag elements to display selected items as dismissible chips."]]

   ;; API Reference Card
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2#attributes.text-2xl.font-bold.ty-text++.mb-4 "API Reference"]

    ;; Attributes Table
    (attribute-table
      [{:name "value"
        :type "string"
        :default "null"
        :description "Comma-separated selected values (e.g., \"red,blue,green\")"}
       {:name "placeholder"
        :type "string"
        :default "null"
        :description "Placeholder text when no options are selected"}
       {:name "label"
        :type "string"
        :default "null"
        :description "Label displayed above the multiselect"}
       {:name "name"
        :type "string"
        :default "null"
        :description "Name for form submission (creates multiple entries in FormData)"}
       {:name "disabled"
        :type "boolean"
        :default "false"
        :description "Whether the multiselect is disabled"}
       {:name "required"
        :type "boolean"
        :default "false"
        :description "Whether the multiselect is required (shows asterisk)"}
       {:name "flavor"
        :type "string"
        :default "'neutral'"
        :description "Semantic flavor: primary, secondary, success, danger, warning, neutral"}
       {:name "clearable"
        :type "boolean"
        :default "false"
        :description "Show clear all button when items are selected"}
       {:name "class"
        :type "string"
        :default "null"
        :description "Additional CSS classes"}])

    [:div.mt-6
     [:h3.text-lg.font-semibold.ty-text++.mb-2 "Events"]
     (event-table
       [{:name "change"
         :when-fired "Fired when selections change"
         :payload "{values: string[], action: 'add'|'remove'|'clear'|'set', item: string|null}"}
        {:name "focus"
         :when-fired "Fired when multiselect gains focus"
         :payload "Standard FocusEvent"}
        {:name "blur"
         :when-fired "Fired when multiselect loses focus"
         :payload "Standard FocusEvent"}])

     [:div.mt-4
      [:h3.text-lg.font-semibold.ty-text++.mb-2 "Slots"]
      [:div.ty-bg-neutral-.rounded.p-4
       [:p.text-sm.ty-text- "Content slot accepts " [:code.ty-bg-neutral.px-1.rounded "ty-tag"] " elements:"]
       [:pre.text-xs.ty-text-.mt-2
        [:code.hljs.language-html "<!-- Use ty-tag elements as options -->\n<ty-tag value=\"red\" pill size=\"sm\">Red</ty-tag>\n<ty-tag value=\"blue\" pill size=\"sm\">Blue</ty-tag>"]]]
      [:div.ty-bg-neutral-.rounded.p-4.mt-2
       [:p.text-sm.ty-text- "Selected slot (automatic): Selected tags are moved here with dismiss functionality:"]
       [:pre.text-xs.ty-text-.mt-2
        [:code.hljs.language-html "<!-- Tags automatically get these attributes when selected -->\n<ty-tag slot=\"selected\" selected dismissible>Selected Tag</ty-tag>"]]]]]]

   ;; Basic Usage
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-bold.ty-text++.mb-4 "Basic Usage"]

    [:div.mb-6
     [:h3.text-lg.font-semibold.ty-text+.mb-2 "Simple Multiselect"]
     [:div.mb-4
      [:ty-multiselect {:placeholder "Select colors..."
                        :style {:min-width "280px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "red"
                 :size "sm"} "Red"]
       [:ty-tag {:value "blue"
                 :size "sm"} "Blue"]
       [:ty-tag {:value "green"
                 :size "sm"} "Green"]
       [:ty-tag {:value "yellow"
                 :size "sm"} "Yellow"]
       [:ty-tag {:value "purple"
                 :size "sm"} "Purple"]
       [:ty-tag {:value "orange"
                 :size "sm"} "Orange"]]]
     (code-block "<ty-multiselect placeholder=\"Select colors...\" style=\"min-width: 280px;\">
  <ty-tag value=\"red\" pill size=\"sm\">Red</ty-tag>
  <ty-tag value=\"blue\" pill size=\"sm\">Blue</ty-tag>
  <ty-tag value=\"green\" pill size=\"sm\">Green</ty-tag>
  <ty-tag value=\"yellow\" pill size=\"sm\">Yellow</ty-tag>
  <ty-tag value=\"purple\" pill size=\"sm\">Purple</ty-tag>
  <ty-tag value=\"orange\" pill size=\"sm\">Orange</ty-tag>
</ty-multiselect>")]

    [:div.mb-6
     [:h3.text-lg.font-semibold.ty-text+.mb-2 "With Initial Values"]
     [:div.mb-4
      [:ty-multiselect {:value ["javascript" "python"]
                        :label "Programming Languages"
                        :placeholder "Select languages..."
                        :style {:min-width "320px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "javascript"
                 :pill true
                 :size "sm"
                 :flavor "warning"}
        [:div.flex.items-center.gap-1.5
         [:div.w-5.h-5.rounded.bg-yellow-400.flex.items-center.justify-center.rounded-full
          [:span.text-xs.font-bold.text-black "JS"]]
         "JavaScript"]]
       [:ty-tag {:value "python"
                 :pill true
                 :size "sm"
                 :flavor "primary"}
        [:div.flex.items-center.gap-1.5
         [:div.w-5.h-5.rounded.bg-blue-500.flex.items-center.justify-center.rounded-full
          [:span.text-xs.font-bold.text-white "🐍"]]
         "Python"]]
       [:ty-tag {:value "java"
                 :pill true
                 :size "sm"
                 :flavor "danger"}
        [:div.flex.items-center.gap-1.5
         [:div.w-5.h-5.rounded.bg-red-600.flex.items-center.justify-center.rounded-full
          [:span.text-xs.font-bold.text-white "☕"]]
         "Java"]]
       [:ty-tag {:value "clojure"
                 :pill true
                 :size "sm"
                 :flavor "success"}
        [:div.flex.items-center.gap-1.5
         [:div.w-5.h-5.rounded.bg-green-600.flex.items-center.justify-center.rounded-full
          [:span.text-xs.font-bold.text-white "λ"]]
         "Clojure"]]
       [:ty-tag {:value "rust"
                 :pill true
                 :size "sm"
                 :flavor "warning"}
        [:div.flex.items-center.gap-1.5
         [:div.w-5.h-5.rounded.bg-orange-500.flex.items-center.justify-center.rounded-full
          [:span.text-xs.font-bold.text-white "🦀"]]
         "Rust"]]
       [:ty-tag {:value "go"
                 :pill true
                 :size "sm"
                 :flavor "info"}
        [:div.flex.items-center.gap-1.5
         [:div.w-5.h-5.rounded.bg-cyan-500.flex.items-center.justify-center.rounded-full
          [:span.text-xs.font-bold.text-white "Go"]]
         "Go"]]]]
     (code-block "<ty-multiselect value=\"javascript,python\" label=\"Programming Languages\" placeholder=\"Select languages...\">
  <ty-tag value=\"javascript\" pill size=\"sm\">JavaScript</ty-tag>
  <ty-tag value=\"python\" pill size=\"sm\">Python</ty-tag>
  <ty-tag value=\"java\" pill size=\"sm\">Java</ty-tag>
  <ty-tag value=\"clojure\" pill size=\"sm\">Clojure</ty-tag>
  <ty-tag value=\"rust\" pill size=\"sm\">Rust</ty-tag>
  <ty-tag value=\"go\" pill size=\"sm\">Go</ty-tag>
</ty-multiselect>")]

    [:div.mb-6
     [:h3.text-lg.font-semibold.ty-text+.mb-2 "Pre-selected Tags (Alternative)"]
     [:p.ty-text-.mb-2 "You can also use the " [:code.ty-bg-neutral-.px-2.py-1.rounded.text-sm "selected"] " attribute on tags directly:"]
     [:div.mb-4
      [:ty-multiselect {:label "Skills"
                        :placeholder "Select your skills..."
                        :style {:min-width "300px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "react"
                 :size "sm"
                 :selected true} "React"]
       [:ty-tag {:value "vue"
                 :size "sm"
                 :selected true} "Vue.js"]
       [:ty-tag {:value "angular"
                 :size "sm"} "Angular"]
       [:ty-tag {:value "node"
                 :size "sm"} "Node.js"]
       [:ty-tag {:value "django"
                 :size "sm"} "Django"]
       [:ty-tag {:value "rails"
                 :size "sm"} "Rails"]]]
     (code-block "<ty-multiselect label=\"Skills\" placeholder=\"Select your skills...\">
  <ty-tag value=\"react\" pill size=\"sm\" selected>React</ty-tag>
  <ty-tag value=\"vue\" pill size=\"sm\" selected>Vue.js</ty-tag>
  <ty-tag value=\"angular\" pill size=\"sm\">Angular</ty-tag>
  <ty-tag value=\"node\" pill size=\"sm\">Node.js</ty-tag>
  <ty-tag value=\"django\" pill size=\"sm\">Django</ty-tag>
  <ty-tag value=\"rails\" pill size=\"sm\">Rails</ty-tag>
</ty-multiselect>")]]

   ;; Semantic Flavors
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Semantic Flavors"]
   [:p.ty-text-.mb-4 "Use semantic flavors to convey meaning. Both the dropdown border and selected tags inherit the flavor styling."]

   [:div.space-y-8

    [:div.ty-content.rounded-lg.p-6
     [:h3.text-xl.font-semibold.ty-text++.mb-4 "Success (Approved Items)"]
     [:div.mb-4
      [:ty-multiselect {:flavor "success"
                        :value "approved,verified"
                        :label "Status"
                        :placeholder "Select approved items..."
                        :style {:min-width "280px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "approved"
                 :pill true
                 :size "sm"
                 :flavor "success"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "check-circle"
                    :size "12"
                    :class "ty-text-success++"}]
         "Approved"]]
       [:ty-tag {:value "verified"
                 :pill true
                 :size "sm"
                 :flavor "success"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "shield"
                    :size "12"
                    :class "ty-text-success++"}]
         "Verified"]]
       [:ty-tag {:value "confirmed"
                 :pill true
                 :size "sm"
                 :flavor "success"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "check"
                    :size "12"
                    :class "ty-text-success++"}]
         "Confirmed"]]
       [:ty-tag {:value "validated"
                 :pill true
                 :size "sm"
                 :flavor "success"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "star"
                    :size "12"
                    :class "ty-text-success++"}]
         "Validated"]]]]
     (code-block "<ty-multiselect flavor=\"success\" value=\"approved,verified\" placeholder=\"Select approved items...\">
  <ty-tag value=\"approved\" pill size=\"sm\" flavor=\"success\">✓ Approved</ty-tag>
  <ty-tag value=\"verified\" pill size=\"sm\" flavor=\"success\">✓ Verified</ty-tag>
  <ty-tag value=\"confirmed\" pill size=\"sm\" flavor=\"success\">✓ Confirmed</ty-tag>
  <ty-tag value=\"validated\" pill size=\"sm\" flavor=\"success\">✓ Validated</ty-tag>
</ty-multiselect>")]

    [:div.ty-content.rounded-lg.p-6
     [:h3.text-xl.font-semibold.ty-text++.mb-4 "Danger (Error States)"]
     [:div.mb-4
      [:ty-multiselect {:flavor "danger"
                        :value "error,timeout"
                        :label "System Issues"
                        :placeholder "Select error types..."
                        :style {:min-width "320px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "error"
                 :pill true
                 :size "sm"
                 :flavor "danger"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "x-circle"
                    :size "12"
                    :class "ty-text-danger++"}]
         "Critical Error"]]
       [:ty-tag {:value "failed"
                 :pill true
                 :size "sm"
                 :flavor "danger"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "alert-triangle"
                    :size "12"
                    :class "ty-text-danger++"}]
         "Process Failed"]]
       [:ty-tag {:value "rejected"
                 :pill true
                 :size "sm"
                 :flavor "danger"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "shield"
                    :size "12"
                    :class "ty-text-danger++"}]
         "Access Rejected"]]
       [:ty-tag {:value "invalid"
                 :pill true
                 :size "sm"
                 :flavor "danger"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "alert-circle"
                    :size "12"
                    :class "ty-text-danger++"}]
         "Invalid Data"]]
       [:ty-tag {:value "timeout"
                 :pill true
                 :size "sm"
                 :flavor "danger"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "clock"
                    :size "12"
                    :class "ty-text-danger++"}]
         "Timeout"]]
       [:ty-tag {:value "offline"
                 :pill true
                 :size "sm"
                 :flavor "danger"}
        [:div.flex.items-center.gap-1.5
         [:ty-icon {:name "wifi-off"
                    :size "12"
                    :class "ty-text-danger++"}]
         "Connection Lost"]]]]
     (code-block "<ty-multiselect flavor=\"danger\" value=\"error,failed\" placeholder=\"Select problem areas...\">
  <ty-tag value=\"error\" pill size=\"sm\" flavor=\"danger\">✗ Error</ty-tag>
  <ty-tag value=\"failed\" pill size=\"sm\" flavor=\"danger\">✗ Failed</ty-tag>
  <ty-tag value=\"rejected\" pill size=\"sm\" flavor=\"danger\">✗ Rejected</ty-tag>
  <ty-tag value=\"invalid\" pill size=\"sm\" flavor=\"danger\">✗ Invalid</ty-tag>
</ty-multiselect>")]]

   ;; Component States
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Component States"]

   [:div.ty-content.rounded-lg.p-6.mb-8
    [:div.mb-6
     [:h3.text-xl.font-semibold.ty-text++.mb-2 "Required Field"]
     [:div.mb-4
      [:ty-multiselect {:label "Required Skills"
                        :required true
                        :placeholder "You must select at least one skill..."
                        :style {:min-width "320px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "frontend"
                 :size "sm"} "Frontend"]
       [:ty-tag {:value "backend"
                 :size "sm"} "Backend"]
       [:ty-tag {:value "fullstack"
                 :size "sm"} "Full Stack"]
       [:ty-tag {:value "devops"
                 :size "sm"} "DevOps"]
       [:ty-tag {:value "design"
                 :size "sm"} "Design"]]]
     (code-block "<ty-multiselect label=\"Required Skills\" required placeholder=\"You must select at least one skill...\">
  <ty-tag value=\"frontend\" pill size=\"sm\">Frontend</ty-tag>
  <ty-tag value=\"backend\" pill size=\"sm\">Backend</ty-tag>
  <ty-tag value=\"fullstack\" pill size=\"sm\">Full Stack</ty-tag>
  <ty-tag value=\"devops\" pill size=\"sm\">DevOps</ty-tag>
  <ty-tag value=\"design\" pill size=\"sm\">Design</ty-tag>
</ty-multiselect>")]

    [:div.mb-6
     [:h3.text-xl.font-semibold.ty-text++.mb-2 "Disabled State"]
     [:div.mb-4
      [:ty-multiselect {:value "option1,option2"
                        :disabled true
                        :label "Disabled Multiselect"
                        :placeholder "Cannot be changed"
                        :style {:min-width "280px"}}
       [:ty-tag {:value "option1"
                 :size "sm"} "Option 1"]
       [:ty-tag {:value "option2"
                 :size "sm"} "Option 2"]
       [:ty-tag {:value "option3"
                 :size "sm"} "Option 3"]]]
     (code-block "<ty-multiselect value=\"option1,option2\" disabled label=\"Disabled Multiselect\">
  <ty-tag value=\"option1\" pill size=\"sm\">Option 1</ty-tag>
  <ty-tag value=\"option2\" pill size=\"sm\">Option 2</ty-tag>
  <ty-tag value=\"option3\" pill size=\"sm\">Option 3</ty-tag>
</ty-multiselect>")]

    [:div
     [:h3.text-xl.font-semibold.ty-text++.mb-2 "With Clear All Button"]
     [:p.ty-text-.mb-2 "Enable " [:code.ty-bg-neutral-.px-2.py-1.rounded.text-sm "clearable"] " to show a clear button when items are selected:"]
     [:div.mb-4
      [:ty-multiselect {:value "feature1,feature2,feature3"
                        :clearable true
                        :label "Features (Clearable)"
                        :placeholder "Select features..."
                        :style {:min-width "320px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "feature1"
                 :size "sm"} "Feature 1"]
       [:ty-tag {:value "feature2"
                 :size "sm"} "Feature 2"]
       [:ty-tag {:value "feature3"
                 :size "sm"} "Feature 3"]
       [:ty-tag {:value "feature4"
                 :size "sm"} "Feature 4"]
       [:ty-tag {:value "feature5"
                 :size "sm"} "Feature 5"]]]
     (code-block "<ty-multiselect value=\"feature1,feature2,feature3\" clearable label=\"Features (Clearable)\">
  <ty-tag value=\"feature1\" pill size=\"sm\">Feature 1</ty-tag>
  <ty-tag value=\"feature2\" pill size=\"sm\">Feature 2</ty-tag>
  <ty-tag value=\"feature3\" pill size=\"sm\">Feature 3</ty-tag>
  <ty-tag value=\"feature4\" pill size=\"sm\">Feature 4</ty-tag>
  <ty-tag value=\"feature5\" pill size=\"sm\">Feature 5</ty-tag>
</ty-multiselect>")]]

   ;; Form Integration
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-bold.ty-text++.mb-4 "Form Integration"]
    [:p.ty-text-.mb-4
     "ty-multiselect participates in form data collection using ElementInternals. "
     "Selected values are added to FormData as multiple entries with the same name (HTML standard)."]

    [:div.mb-6
     [:h3.text-xl.font-semibold.ty-text++.mb-2 "HTMX & FormData Example"]
     [:form#multiselect-form-demo.space-y-4.p-4.border.ty-border.rounded
      [:ty-multiselect {:name "categories"
                        :label "Categories"
                        :placeholder "Select categories..."
                        :value "tech,design"
                        :style {:min-width "280px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "tech"
                 :size "sm"} "Technology"]
       [:ty-tag {:value "design"
                 :size "sm"} "Design"]
       [:ty-tag {:value "business"
                 :size "sm"} "Business"]
       [:ty-tag {:value "marketing"
                 :size "sm"} "Marketing"]
       [:ty-tag {:value "finance"
                 :size "sm"} "Finance"]]

      [:ty-multiselect {:name "skills"
                        :label "Skills"
                        :placeholder "Select skills..."
                        :style {:min-width "280px"}
                        :on {:change multiselect-event-handler}}
       [:ty-tag {:value "javascript"
                 :size "sm"} "JavaScript"]
       [:ty-tag {:value "python"
                 :size "sm"} "Python"]
       [:ty-tag {:value "clojure"
                 :size "sm"} "Clojure"]
       [:ty-tag {:value "react"
                 :size "sm"} "React"]
       [:ty-tag {:value "css"
                 :size "sm"} "CSS"]]

      [:ty-button {:flavor "primary"
                   :on {:click #(let [form (.getElementById js/document "multiselect-form-demo")
                                      form-data (js/FormData. form)
                                      results (.getElementById js/document "multiselect-form-results")
                                      entries (js/Array.from (.entries form-data))]
                                  (set! (.-innerHTML results)
                                        (str "<strong>FormData contents:</strong><br>"
                                             (if (> (.-length entries) 0)
                                               (.join (.map entries
                                                            (fn [[name value]]
                                                              (str name " = " value)))
                                                      "<br>")
                                               "No form data found"))))}}
       "Extract FormData"]

      [:div#multiselect-form-results.mt-4.p-3.rounded.font-mono.text-xs.ty-bg-neutral-.ty-text-
       "Click 'Extract FormData' to see form values..."]]

     (code-block "<!-- HTMX Integration -->
<form hx-post=\"/submit\" hx-target=\"#results\">
  <ty-multiselect name=\"categories\" placeholder=\"Select categories...\">
    <ty-tag value=\"tech\" pill size=\"sm\">Technology</ty-tag>
    <ty-tag value=\"design\" pill size=\"sm\">Design</ty-tag>
  </ty-multiselect>
  
  <ty-multiselect name=\"skills\" placeholder=\"Select skills...\">
    <ty-tag value=\"javascript\" pill size=\"sm\">JavaScript</ty-tag>
    <ty-tag value=\"python\" pill size=\"sm\">Python</ty-tag>
  </ty-multiselect>
  
  <button type=\"submit\">Submit</button>
</form>

<!-- Resulting FormData (URL-encoded) -->
<!-- categories=tech&categories=design&skills=javascript&skills=python -->"
                 :lang "html")]

    [:div.text-xs.ty-text--
     [:strong "Expected server payload:"] [:br]
     [:code "categories: ['tech', 'design'], skills: ['javascript', 'python']"]]]

   ;; Tag Behavior
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-bold.ty-text++.mb-4 "Tag Behavior & Automatic Management"]

    [:div.mb-6
     [:h3.text-xl.font-semibold.ty-text++.mb-2 "Automatic Tag State Management"]
     [:p.ty-text-.mb-3 "ty-multiselect automatically manages tag states:"]
     [:ul.space-y-2.ty-text-.ml-4
      [:li "• " [:strong "Unselected tags"] " remain in the dropdown options"]
      [:li "• " [:strong "Selected tags"] " move to the " [:code.ty-bg-neutral-.px-1.rounded "selected"] " slot"]
      [:li "• " [:strong "Selected tags"] " automatically get " [:code.ty-bg-neutral-.px-1.rounded "dismissible=\"true\""] " attribute"]
      [:li "• " [:strong "Clicking a selected tag's dismiss button"] " moves it back to options"]
      [:li "• " [:strong "Tag flavors"] " should match the multiselect flavor for visual consistency"]]]

    [:div.mb-6
     [:h3.text-xl.font-semibold.ty-text++.mb-2 "Tag Size Recommendations"]
     [:div.flex.gap-4.items-start.mb-3
      [:div.flex.flex-col.gap-2
       [:ty-multiselect {:placeholder "Small tags (recommended)"
                         :value "small1,small2"
                         :style {:min-width "220px"}
                         :on {:change multiselect-event-handler}}
        [:ty-tag {:value "small1"
                  :size "sm"} "Small Tag"]
        [:ty-tag {:value "small2"
                  :size "sm"} "Small Tag"]
        [:ty-tag {:value "small3"
                  :size "sm"} "Small Tag"]]
       [:div.text-xs.ty-text-- "size=\"sm\" (recommended)"]]

      [:div.flex.flex-col.gap-2
       [:ty-multiselect {:placeholder "Regular tags"
                         :value "regular1,regular2"
                         :style {:min-width "220px"}
                         :on {:change multiselect-event-handler}}
        [:ty-tag {:value "regular1"} "Regular Tag"]
        [:ty-tag {:value "regular2"} "Regular Tag"]
        [:ty-tag {:value "regular3"} "Regular Tag"]]
       [:div.text-xs.ty-text-- "default size"]]]
     [:p.ty-text-.text-sm "Small tags (" [:code.ty-bg-neutral-.px-1.rounded "size=\"sm\""] ") are recommended for better space efficiency."]]

    [:div
     [:h3.text-xl.font-semibold.ty-text++.mb-2 "Pill vs. Rectangular Tags"]
     [:div.flex.gap-4.items-start.mb-3
      [:div.flex.flex-col.gap-2
       [:ty-multiselect {:placeholder "Pill tags (recommended)"
                         :value "pill1,pill2"
                         :style {:min-width "220px"}
                         :on {:change multiselect-event-handler}}
        [:ty-tag {:value "pill1"
                  :size "sm"} "Pill Tag"]
        [:ty-tag {:value "pill2"
                  :size "sm"} "Pill Tag"]
        [:ty-tag {:value "pill3"
                  :size "sm"} "Pill Tag"]]
       [:div.text-xs.ty-text-- "pill=\"true\" (recommended)"]]

      [:div.flex.flex-col.gap-2
       [:ty-multiselect {:placeholder "Rectangular tags"
                         :value "rect1,rect2"
                         :style {:min-width "220px"}
                         :on {:change multiselect-event-handler}}
        [:ty-tag {:value "rect1"
                  :size "sm"
                  :pill "false"} "Rect Tag"]
        [:ty-tag {:value "rect2"
                  :size "sm"
                  :pill "false"} "Rect Tag"]
        [:ty-tag {:value "rect3"
                  :size "sm"
                  :pill "false"} "Rect Tag"]]
       [:div.text-xs.ty-text-- "default shape"]]]
     [:p.ty-text-.text-sm "Pill tags (" [:code.ty-bg-neutral-.px-1.rounded "pill=\"true\""] ") are recommended for multiselect use cases."]]]

   ;; Event Handling
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-bold.ty-text++.mb-4 "Event Handling"]
    [:p.ty-text-.mb-4
     "ty-multiselect dispatches detailed change events with information about what changed:"]

    [:div.mb-4
     [:ty-multiselect {:id "event-demo"
                       :placeholder "Select to see events in console..."
                       :style {:min-width "280px"}
                       :on {:change multiselect-event-handler}}
      [:ty-tag {:value "event1"
                :size "sm"} "Event Test 1"]
      [:ty-tag {:value "event2"
                :size "sm"} "Event Test 2"]
      [:ty-tag {:value "event3"
                :size "sm"} "Event Test 3"]
      [:ty-tag {:value "event4"
                :size "sm"} "Event Test 4"]]]

    (code-block "// Event payload structure
{
  values: string[],        // Current selected values array
  action: string,          // \"add\", \"remove\", \"clear\", \"set\"
  item: string | null      // The specific item that was added/removed (null for clear/set)
}

// JavaScript event handling
const handleMultiselectChange = (event) => {
  const { values, action, item } = event.detail;
  
  console.log('Selection changed:', {
    currentValues: values,     // [\"event1\", \"event2\"]
    action: action,            // \"add\" | \"remove\" | \"clear\" | \"set\"
    changedItem: item          // \"event2\" (what was added/removed)
  });
  
  // React to specific actions
  if (action === 'add') {
    console.log(`Added: ${item}`);
  } else if (action === 'remove') {
    console.log(`Removed: ${item}`);
  } else if (action === 'clear') {
    console.log('All selections cleared');
  }
};

document.getElementById('my-multiselect')
  .addEventListener('change', handleMultiselectChange);

// React/framework example
<TyMultiselect 
  onChange={handleMultiselectChange}
  placeholder=\"Select options...\">
  <ty-tag value=\"option1\" pill size=\"sm\">Option 1</ty-tag>
</TyMultiselect>" :lang "javascript")

    [:div.mt-4.p-3.ty-bg-primary-.rounded.text-sm
     [:p.ty-text-primary++ [:strong "💡 Tip:"] " Open browser console to see detailed change events from the demo above."]]]

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
       [:li "• Use small pill tags (" [:code.ty-bg-neutral-.px-1.rounded "size=\"sm\" pill"] ") for better layout"]
       [:li "• Match tag flavors with multiselect flavor for consistency"]
       [:li "• Set appropriate min-width to prevent layout shifts"]
       [:li "• Use semantic flavors to convey meaning (success, danger, etc.)"]
       [:li "• Provide clear, descriptive labels for accessibility"]
       [:li "• Use " [:code.ty-bg-neutral-.px-1.rounded "required"] " attribute for mandatory fields"]
       [:li "• Listen to change events to react to selection changes"]]]

     ;; Don'ts
     [:div
      [:h3.text-lg.font-semibold.ty-text-danger++.mb-3.flex.items-center.gap-2
       [:ty-icon {:name "x-circle"
                  :size "20"}]
       "Don'ts"]
      [:ul.space-y-2.ty-text-
       [:li "• Don't mix tag sizes within the same multiselect"]
       [:li "• Don't use placeholder text as a label replacement"]
       [:li "• Don't forget to set " [:code.ty-bg-neutral-.px-1.rounded "value"] " attributes on tags"]
       [:li "• Don't make the component too narrow for selected tags"]
       [:li "• Don't manually manage tag " [:code.ty-bg-neutral-.px-1.rounded "selected"] " attributes after initialization"]
       [:li "• Don't use conflicting tag and multiselect flavors"]
       [:li "• Don't forget the " [:code.ty-bg-neutral-.px-1.rounded "name"] " attribute for forms"]]]]

    [:div.mt-6.p-4.ty-bg-primary-.rounded
     [:h3.font-semibold.ty-text-primary++.mb-2.flex.items-center.gap-2
      [:ty-icon {:name "lightbulb"
                 :size "18"}]
      "Key Tips"]
     [:ul.space-y-1.text-sm.ty-text-primary
      [:li "• Tags automatically move between slots based on selection state"]
      [:li "• Search functionality is built-in - users can type to filter options"]
      [:li "• Selected tags become dismissible automatically (X button appears)"]
      [:li "• Component automatically closes dropdown when all options are selected"]
      [:li "• FormData integration works seamlessly with HTMX and standard forms"]
      [:li "• Use " [:code.ty-bg-neutral-.px-1.rounded "value"] " attribute for initial state or " [:code.ty-bg-neutral-.px-1.rounded "selected"] " on tags"]
      [:li "• The component handles keyboard navigation and accessibility automatically"]]]]

   ;; Related Components
   [:div.mt-8.p-4.ty-border.border.rounded-lg
    [:h3.font-semibold.ty-text+.mb-2 "Related Components"]
    [:div.flex.gap-4.text-sm
     [:a.ty-text-primary.hover:underline {:href "/docs/dropdown"} "ty-dropdown →"]
     [:a.ty-text-primary.hover:underline {:href "/docs/tag"} "ty-tag →"]
     [:a.ty-text-primary.hover:underline {:href "/docs/input"} "ty-input →"]]]])
