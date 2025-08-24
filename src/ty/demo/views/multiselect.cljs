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
     [:p.text-gray-600.dark:text-gray-400.mb-4 description])
   (into [:div.flex.flex-wrap.gap-4.items-start] children)])

(defn code-snippet [code]
  [:div.mt-4
   [:pre.code-block.text-xs
    [:code code]]])

(defn basic-examples []
  [:div.demo-section
   [:h2.demo-title "Basic Multiselect"]

   (demo-section {:title "Simple Multiselect"
                  :description "Select multiple options. Click ty-tag dismiss buttons (×) to remove them."
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
  <ty-tag value=\"red\" dismissible pill size=\"sm\">Red</ty-tag>
  <ty-tag value=\"blue\" dismissible pill size=\"sm\">Blue</ty-tag>
  <ty-tag value=\"green\" dismissible pill size=\"sm\">Green</ty-tag>
</ty-multiselect>")

   (demo-section {:title "With Label"
                  :description "Multiselect with built-in label support"
                  :children [[:div.max-w-md
                              [:ty-multiselect {:label "Programming Languages"
                                                :placeholder "Select languages..."
                                                :style {:min-width "300px"}
                                                :on {:change multiselect-event-handler}}
                               [:ty-tag {:value "javascript"
                                         :pill true
                                         :size "sm"} "JavaScript"]
                               [:ty-tag {:value "python"
                                         :pill true
                                         :size "sm"} "Python"]
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
  <ty-tag value=\"javascript\" dismissible pill size=\"sm\">JavaScript</ty-tag>
  <ty-tag value=\"python\" dismissible pill size=\"sm\">Python</ty-tag>
</ty-multiselect>")

   (demo-section {:title "Required Field"
                  :description "Required multiselect with indicator"
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
                               [:ty-tag {:value "python"
                                         :pill true
                                         :size "sm"} "Python"]
                               [:ty-tag {:value "java"
                                         :pill true
                                         :size "sm"} "Java"]]]]})

   (code-snippet "<ty-multiselect label=\"Skills\" required placeholder=\"Select your skills...\">
  <ty-tag value=\"react\" dismissible pill size=\"sm\">React</ty-tag>
  <ty-tag value=\"vue\" dismissible pill size=\"sm\">Vue.js</ty-tag>
</ty-multiselect>")])

(defn flavor-examples []
  [:div.demo-section
   [:h2.demo-title "Flavor Variants"]
   [:p.text-gray-600.dark:text-gray-400.mb-6
    "Semantic flavors affect both the dropdown border and tag colors"]

   [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
    [:div
     [:ty-multiselect {:flavor "positive"
                       :label "Positive (Green)"
                       :value "approved,verified"
                       :placeholder "Positive items..."
                       :style {:min-width "250px"}
                       :on {:change multiselect-event-handler}}
      [:ty-tag {:value "approved"
                :pill true
                :size "sm"
                :flavor "positive"} "Approved"]
      [:ty-tag {:value "verified"
                :pill true
                :size "sm"
                :flavor "positive"} "Verified"]
      [:ty-tag {:value "confirmed"
                :pill true
                :size "sm"
                :flavor "positive"} "Confirmed"]
      [:ty-tag {:value "validated"
                :pill true
                :size "sm"
                :flavor "positive"} "Validated"]]]

    [:div
     [:ty-multiselect {:flavor "negative"
                       :label "Negative (Red)"
                       :value "error,failed"
                       :placeholder "Negative items..."
                       :style {:min-width "250px"}
                       :on {:change multiselect-event-handler}}
      [:ty-tag {:value "error"
                :pill true
                :size "sm"
                :flavor "negative"} "Error"]
      [:ty-tag {:value "failed"
                :pill true
                :size "sm"
                :flavor "negative"} "Failed"]
      [:ty-tag {:value "rejected"
                :pill true
                :size "sm"
                :flavor "negative"} "Rejected"]
      [:ty-tag {:value "invalid"
                :pill true
                :size "sm"
                :flavor "negative"} "Invalid"]]]

    [:div
     [:ty-multiselect {:flavor "important"
                       :label "Important (Blue)"
                       :value "urgent,priority"
                       :placeholder "Important items..."
                       :style {:min-width "250px"}
                       :on {:change multiselect-event-handler}}
      [:ty-tag {:value "urgent"
                :pill true
                :size "sm"
                :flavor "important"} "Urgent"]
      [:ty-tag {:value "priority"
                :pill true
                :size "sm"
                :flavor "important"} "Priority"]
      [:ty-tag {:value "critical"
                :pill true
                :size "sm"
                :flavor "important"} "Critical"]
      [:ty-tag {:value "essential"
                :pill true
                :size "sm"
                :flavor "important"} "Essential"]]]

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

   (code-snippet "<ty-multiselect flavor=\"positive\" placeholder=\"Positive items...\">
  <ty-tag value=\"approved\" dismissible pill size=\"sm\" flavor=\"positive\">Approved</ty-tag>
  <ty-tag value=\"verified\" dismissible pill size=\"sm\" flavor=\"positive\">Verified</ty-tag>
</ty-multiselect>

<ty-multiselect flavor=\"negative\" placeholder=\"Negative items...\">
  <ty-tag value=\"error\" dismissible pill size=\"sm\" flavor=\"negative\">Error</ty-tag>
  <ty-tag value=\"failed\" dismissible pill size=\"sm\" flavor=\"negative\">Failed</ty-tag>
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
                  :description "Enable the clearable attribute to show a 'Clear all' button"
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
  <ty-tag value=\"option1\" dismissible pill size=\"sm\">Option 1</ty-tag>
  <ty-tag value=\"option2\" dismissible pill size=\"sm\">Option 2</ty-tag>
</ty-multiselect>

<ty-multiselect clearable value=\"feature1,feature2\" placeholder=\"With clear all...\">
  <ty-tag value=\"feature1\" dismissible pill size=\"sm\">Feature 1</ty-tag>
  <ty-tag value=\"feature2\" dismissible pill size=\"sm\">Feature 2</ty-tag>
</ty-multiselect>")])

(defn event-debugging []
  [:div.demo-section
   [:h2.demo-title "Event Debugging"]
   [:p.text-gray-600.dark:text-gray-400.mb-4
    "Open browser console to see change events. Current values: "]
   [:code.bg-gray-100.dark:bg-gray-800.px-2.py-1.rounded.text-sm
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
  <ty-tag value=\"event1\" dismissible pill size=\"sm\">Event Test 1</ty-tag>
</ty-multiselect>")])

(defn render []
  [:div.max-w-6xl.mx-auto
   [:div.mb-8
    [:h1.text-3xl.font-bold.text-gray-900.dark:text-white.mb-2
     "Multiselect Component"]
    [:p.text-lg.text-gray-600.dark:text-gray-400
     "A multiselect component that allows selecting multiple options. Built on top of the dropdown component with ty-tag-based selection display. Features semantic flavors, proper outside click handling, and full keyboard accessibility."]]

   [:div.space-y-12
    (basic-examples)
    (flavor-examples)
    (state-examples)
    (event-debugging)]])
