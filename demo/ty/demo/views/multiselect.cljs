(ns ty.demo.views.multiselect
  (:require [ty.demo.state :as state]))

(defn multiselect-event-handler [event]
  (let [detail (.-detail event)
        values (js->clj (.-values detail) :keywordize-keys false)
        action (.-action detail)
        item (.-item detail)]
    (js/console.log "Multiselect changed:"
                    (clj->js {:values values
                              :action action
                              :item item}))
    (swap! state/state assoc :multiselect-values values)))

(defn demo-section [{:keys [title description children]}]
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
   [:h2.demo-title "Basic Multiselect"]

   (demo-section {:title "Simple Multiselect (value attribute)"
                  :description "Initialize with value=\"red,blue\". Click tags to toggle selection."
                  :children [[:div.max-w-md
                              [:ty-multiselect {:value "red,blue"
                                                :placeholder "Select colors..."
                                                :style {:min-width "280px"}
                                                :on {:change multiselect-event-handler}}
                               [:ty-tag {:value "red"
                                         :pill true
                                         :size "sm"} "Red"]
                               [:ty-tag {:value "blue"
                                         :pill true
                                         :size "sm"} "Blue"]
                               [:ty-tag {:value "green"
                                         :pill true
                                         :size "sm"} "Green"]
                               [:ty-tag {:value "yellow"
                                         :pill true
                                         :size "sm"} "Yellow"]
                               [:ty-tag {:value "purple"
                                         :pill true
                                         :size "sm"} "Purple"]
                               [:ty-tag {:value "orange"
                                         :pill true
                                         :size "sm"} "Orange"]]]]})

   (code-snippet "<ty-multiselect value=\"red,blue\" placeholder=\"Select colors...\">
  <ty-tag value=\"red\" pill size=\"sm\">Red</ty-tag>
  <ty-tag value=\"blue\" pill size=\"sm\">Blue</ty-tag>
  <ty-tag value=\"green\" pill size=\"sm\">Green</ty-tag>
</ty-multiselect>")

   (demo-section {:title "With Pre-selected Tags"
                  :description "Initialize with selected attribute on tags directly. More declarative approach."
                  :children [[:div.max-w-md
                              [:ty-multiselect {:label "Programming Languages"
                                                :placeholder "Select languages..."
                                                :style {:min-width "300px"}
                                                :on {:change multiselect-event-handler}}
                               [:ty-tag {:value "javascript"
                                         :pill true
                                         :size "sm"
                                         :selected true} "JavaScript"]
                               [:ty-tag {:value "python"
                                         :pill true
                                         :size "sm"
                                         :selected true} "Python"]
                               [:ty-tag {:value "java"
                                         :pill true
                                         :size "sm"} "Java"]
                               [:ty-tag {:value "clojure"
                                         :pill true
                                         :size "sm"} "Clojure"]
                               [:ty-tag {:value "rust"
                                         :pill true
                                         :size "sm"} "Rust"]
                               [:ty-tag {:value "go"
                                         :pill true
                                         :size "sm"} "Go"]]]]})

   (code-snippet "<ty-multiselect label=\"Programming Languages\" placeholder=\"Select languages...\">
  <ty-tag value=\"javascript\" pill size=\"sm\" selected>JavaScript</ty-tag>
  <ty-tag value=\"python\" pill size=\"sm\" selected>Python</ty-tag>
  <ty-tag value=\"java\" pill size=\"sm\">Java</ty-tag>
</ty-multiselect>")

   (demo-section {:title "Required Field (No Initial Selection)"
                  :description "Required multiselect with indicator, starting with no selections"
                  :children [[:div.max-w-md
                              [:ty-multiselect {:label "Skills"
                                                :required true
                                                :placeholder "Select your skills..."
                                                :style {:min-width "300px"}
                                                :on {:change multiselect-event-handler}}
                               [:ty-tag {:value "react"
                                         :pill true
                                         :size "sm"} "React"]
                               [:ty-tag {:value "vue"
                                         :pill true
                                         :size "sm"} "Vue.js"]
                               [:ty-tag {:value "angular"
                                         :pill true
                                         :size "sm"} "Angular"]
                               [:ty-tag {:value "node"
                                         :pill true
                                         :size "sm"} "Node.js"]
                               [:ty-tag {:value "django"
                                         :pill true
                                         :size "sm"} "Django"]
                               [:ty-tag {:value "rails"
                                         :pill true
                                         :size "sm"} "Rails"]]]]})

   (code-snippet "<ty-multiselect label=\"Skills\" required placeholder=\"Select your skills...\">
  <ty-tag value=\"react\" pill size=\"sm\">React</ty-tag>
  <ty-tag value=\"vue\" pill size=\"sm\">Vue.js</ty-tag>
</ty-multiselect>")])

(defn flavor-examples []
  [:div.demo-section
   [:h2.demo-title "Flavor Variants"]
   [:p.ty-text-.mb-6
    "Semantic flavors affect both the dropdown border and tag colors"]

   [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
    [:div
     [:ty-multiselect {:flavor "success"
                       :label "Positive (Green)"
                       :value "approved,verified"
                       :placeholder "Positive items..."
                       :style {:min-width "250px"}
                       :on {:change multiselect-event-handler}}
      [:ty-tag {:value "approved"
                :pill true
                :size "sm"
                :flavor "success"} "Approved"]
      [:ty-tag {:value "verified"
                :pill true
                :size "sm"
                :flavor "success"} "Verified"]
      [:ty-tag {:value "confirmed"
                :pill true
                :size "sm"
                :flavor "success"} "Confirmed"]
      [:ty-tag {:value "validated"
                :pill true
                :size "sm"
                :flavor "success"} "Validated"]]]

    [:div
     [:ty-multiselect {:flavor "danger"
                       :label "Negative (Red)"
                       :value "error,failed"
                       :placeholder "Negative items..."
                       :style {:min-width "250px"}
                       :on {:change multiselect-event-handler}}
      [:ty-tag {:value "error"
                :pill true
                :size "sm"
                :flavor "danger"} "Error"]
      [:ty-tag {:value "failed"
                :pill true
                :size "sm"
                :flavor "danger"} "Failed"]
      [:ty-tag {:value "rejected"
                :pill true
                :size "sm"
                :flavor "danger"} "Rejected"]
      [:ty-tag {:value "invalid"
                :pill true
                :size "sm"
                :flavor "danger"} "Invalid"]]]

    [:div
     [:ty-multiselect {:flavor "primary"
                       :label "Important (Blue)"
                       :value "urgent,priority"
                       :placeholder "Important items..."
                       :style {:min-width "250px"}
                       :on {:change multiselect-event-handler}}
      [:ty-tag {:value "urgent"
                :pill true
                :size "sm"
                :flavor "primary"} "Urgent"]
      [:ty-tag {:value "priority"
                :pill true
                :size "sm"
                :flavor "primary"} "Priority"]
      [:ty-tag {:value "critical"
                :pill true
                :size "sm"
                :flavor "primary"} "Critical"]
      [:ty-tag {:value "essential"
                :pill true
                :size "sm"
                :flavor "primary"} "Essential"]]]

    [:div
     [:ty-multiselect {:label "Neutral (Default)"
                       :value "draft,pending"
                       :placeholder "Neutral items..."
                       :style {:min-width "250px"}
                       :on {:change multiselect-event-handler}}
      [:ty-tag {:value "draft"
                :pill true
                :size "sm"} "Draft"]
      [:ty-tag {:value "pending"
                :pill true
                :size "sm"} "Pending"]
      [:ty-tag {:value "review"
                :pill true
                :size "sm"} "Review"]
      [:ty-tag {:value "waiting"
                :pill true
                :size "sm"} "Waiting"]]]]

   (code-snippet "<ty-multiselect flavor=\"success\" placeholder=\"Positive items...\">
  <ty-tag value=\"approved\" pill size=\"sm\" flavor=\"success\">Approved</ty-tag>
  <ty-tag value=\"verified\" pill size=\"sm\" flavor=\"success\">Verified</ty-tag>
</ty-multiselect>

<ty-multiselect flavor=\"danger\" placeholder=\"Negative items...\">
  <ty-tag value=\"error\" pill size=\"sm\" flavor=\"danger\">Error</ty-tag>
  <ty-tag value=\"failed\" pill size=\"sm\" flavor=\"danger\">Failed</ty-tag>
</ty-multiselect>")])

(defn state-examples []
  [:div.demo-section
   [:h2.demo-title "Component States"]

   (demo-section {:title "Disabled State"
                  :description "Disabled multiselect shows values but prevents interaction"
                  :children [[:div.max-w-md
                              [:ty-multiselect {:value "option1,option2"
                                                :disabled true
                                                :placeholder "Disabled multiselect"
                                                :style {:min-width "280px"}}
                               [:ty-tag {:value "option1"
                                         :pill true
                                         :size "sm"} "Option 1"]
                               [:ty-tag {:value "option2"
                                         :pill true
                                         :size "sm"} "Option 2"]
                               [:ty-tag {:value "option3"
                                         :pill true
                                         :size "sm"} "Option 3"]]]]})

   (demo-section {:title "With Clear All Button"
                  :description "Enable the clearable attribute to show a 'Clear all' button when items are selected"
                  :children [[:div.max-w-md
                              [:ty-multiselect {:value "feature1,feature2,feature3"
                                                :clearable true
                                                :label "Features"
                                                :placeholder "Select features..."
                                                :style {:min-width "320px"}
                                                :on {:change multiselect-event-handler}}
                               [:ty-tag {:value "feature1"
                                         :pill true
                                         :size "sm"} "Feature 1"]
                               [:ty-tag {:value "feature2"
                                         :pill true
                                         :size "sm"} "Feature 2"]
                               [:ty-tag {:value "feature3"
                                         :pill true
                                         :size "sm"} "Feature 3"]
                               [:ty-tag {:value "feature4"
                                         :pill true
                                         :size "sm"} "Feature 4"]
                               [:ty-tag {:value "feature5"
                                         :pill true
                                         :size "sm"} "Feature 5"]]]]})

   (code-snippet "<ty-multiselect value=\"option1,option2\" disabled>
  <ty-tag value=\"option1\" pill size=\"sm\">Option 1</ty-tag>
  <ty-tag value=\"option2\" pill size=\"sm\">Option 2</ty-tag>
</ty-multiselect>

<ty-multiselect clearable value=\"feature1,feature2\" placeholder=\"With clear all...\">
  <ty-tag value=\"feature1\" pill size=\"sm\">Feature 1</ty-tag>
  <ty-tag value=\"feature2\" pill size=\"sm\">Feature 2</ty-tag>
</ty-multiselect>")])

(defn event-debugging []
  [:div.demo-section
   [:h2.demo-title "Event Debugging"]
   [:p.ty-text-.mb-4
    "Open browser console to see change events. Current values: "]
   [:code.ty-content.px-2.py-1.rounded.text-sm
    (str (vec (:multiselect-values @state/state)))]

   [:div.mt-6.max-w-md
    [:ty-multiselect {:placeholder "Select to see events..."
                      :style {:min-width "280px"}
                      :on {:change multiselect-event-handler}}
     [:ty-tag {:value "event1"
               :pill true
               :size "sm"} "Event Test 1"]
     [:ty-tag {:value "event2"
               :pill true
               :size "sm"} "Event Test 2"]
     [:ty-tag {:value "event3"
               :pill true
               :size "sm"} "Event Test 3"]
     [:ty-tag {:value "event4"
               :pill true
               :size "sm"} "Event Test 4"]]]

   (code-snippet "const handleChange = (event) => {
  const { values, action, item } = event.detail;
  console.log('Multiselect changed:', { values, action, item });
};

<ty-multiselect onChange={handleChange}>
  <ty-tag value=\"event1\" pill size=\"sm\">Event Test 1</ty-tag>
</ty-multiselect>")])

(defn htmx-integration-demo []
  (demo-section
    {:title "HTMX & Form Integration"
     :description "Test form data serialization for HTMX compatibility. The multiselect component uses ElementInternals to participate in form data collection."
     :children
     [:div.grid.grid-cols-1.lg:grid-cols-2.gap-6

      ;; Form data extraction test
      [:div.space-y-4
       [:h4.font-medium "FormData Extraction Test"]
       [:p.text-sm.ty-text-- "Tests that multiselect values appear in FormData as multiple entries with the same name (HTMX standard)."]

       [:form#multiselect-form-test.space-y-4.p-4.border.ty-border.rounded
        [:ty-multiselect {:name "categories"
                          :label "Categories"
                          :placeholder "Select categories..."
                          :value "tech,design"}
         [:ty-tag {:value "tech"} "Technology"]
         [:ty-tag {:value "design"} "Design"]
         [:ty-tag {:value "business"} "Business"]
         [:ty-tag {:value "marketing"} "Marketing"]
         [:ty-tag {:value "finance"} "Finance"]]

        [:ty-multiselect {:name "skills"
                          :label "Skills"
                          :placeholder "Select skills..."}
         [:ty-tag {:value "javascript"} "JavaScript"]
         [:ty-tag {:value "python"} "Python"]
         [:ty-tag {:value "clojure"} "Clojure"]
         [:ty-tag {:value "react"} "React"]
         [:ty-tag {:value "css"} "CSS"]]

        [:ty-button {:flavor "primary"
                     :on {:click #(let [form (.getElementById js/document "multiselect-form-test")
                                        form-data (js/FormData. form)
                                        results (.getElementById js/document "multiselect-formdata-results")
                                        entries (js/Array.from (.entries form-data))]
                                    (.log js/console "=== Multiselect FormData Debug ===")
                                    (.log js/console "Form found:" form)
                                    (.log js/console "FormData entries:" entries)
                                    (set! (.-innerHTML results)
                                          (str "<strong>FormData contents:</strong><br>"
                                               (if (> (.-length entries) 0)
                                                 (.join (.map entries
                                                              (fn [[name value]]
                                                                (str name " = " value)))
                                                        "<br>")
                                                 "❌ NO FORMDATA ENTRIES FOUND"))))}}
         "Extract FormData"]

        [:div#multiselect-formdata-results.mt-4.p-3.rounded.font-mono.text-xs
         {:class [:ty-bg-neutral- :ty-text-]}
         "Click 'Extract FormData' to see form values..."]]

       [:div.text-xs.ty-text--
        [:strong "Expected HTMX format:"] [:br]
        [:code "categories=tech&categories=design&skills=javascript&skills=python"]]]

      ;; Property access test  
      [:div.space-y-4
       [:h4.font-medium "Property Access Test"]
       [:p.text-sm.ty-text-- "Tests JavaScript property access and programmatic control."]

       [:div.space-y-3.p-4.border.ty-border.rounded
        [:ty-multiselect {:id "prop-test-multiselect"
                          :name "test-values"
                          :label "Test Multiselect"
                          :placeholder "Select options..."}
         [:ty-tag {:value "option1"} "Option 1"]
         [:ty-tag {:value "option2"} "Option 2"]
         [:ty-tag {:value "option3"} "Option 3"]
         [:ty-tag {:value "option4"} "Option 4"]]

        [:div.flex.gap-2.flex-wrap
         [:ty-button {:flavor "success"
                      :size "sm"
                      :on {:click #(let [element (.getElementById js/document "prop-test-multiselect")]
                                     (.setAttribute element "value" "option1,option3")
                                     (.dispatchEvent element (js/Event. "change")))}}
          "Set option1,option3"]
         [:ty-button {:flavor "warning"
                      :size "sm"
                      :on {:click #(let [element (.getElementById js/document "prop-test-multiselect")]
                                     (.setAttribute element "value" "option2,option4"))}}
          "Set option2,option4"]
         [:ty-button {:flavor "neutral"
                      :size "sm"
                      :on {:click #(let [element (.getElementById js/document "prop-test-multiselect")]
                                     (.setAttribute element "value" ""))}}
          "Clear All"]]

        [:ty-button {:flavor "info"
                     :on {:click #(let [element (.getElementById js/document "prop-test-multiselect")
                                        form-data (js/FormData.)
                                        selected-values (js/Array.from (.querySelectorAll element "ty-tag[selected]"))
                                        results (.getElementById js/document "multiselect-property-results")]
                                    ;; Simulate FormData extraction
                                    (doseq [tag selected-values]
                                      (.append form-data "test-values" (.getAttribute tag "value")))
                                    (let [entries (js/Array.from (.entries form-data))]
                                      (set! (.-innerHTML results)
                                            (str "<strong>Current State:</strong><br>"
                                                 "value attribute: " (or (.getAttribute element "value") "(none)") "<br>"
                                                 "selected tags: " (.-length selected-values) "<br>"
                                                 "<strong>FormData simulation:</strong><br>"
                                                 (if (> (.-length entries) 0)
                                                   (.join (.map entries (fn [[name value]] (str name "=" value))) "&")
                                                   "(no values)")))))}}
         "Check Current State"]

        [:div#multiselect-property-results.mt-4.p-3.rounded.font-mono.text-xs
         {:class [:ty-bg-neutral- :ty-text-]}
         "Click 'Check Current State' to see current values..."]]]]}))

(defn view []
  [:div.max-w-6xl.mx-auto
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2
     "Multiselect Component"]
    [:p.text-lg.ty-text-
     "A multiselect component that allows selecting multiple options. Built on top of the dropdown component with ty-tag-based selection display. Tags automatically move between slots and update their dismissible state based on selection."]]

   [:div.space-y-12
    (basic-examples)
    (flavor-examples)
    (state-examples)
    (htmx-integration-demo)
    (event-debugging)]])
