(ns ty.demo.views.multiselect
  (:require [ty.demo.state :as state]))

(defn multiselect-event-handler [event]
  (let [detail (.-detail event)
        values (js->clj (.-values detail) :keywordize-keys false)
        action (.-action detail)
        item (.-item detail)]
    (js/console.log "Multiselect changed:"
                    (clj->js {:values values :action action :item item}))
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
                               [:option {:value "red"} "Red"]
                               [:option {:value "blue"} "Blue"]
                               [:option {:value "green"} "Green"]
                               [:option {:value "yellow"} "Yellow"]
                               [:option {:value "purple"} "Purple"]
                               [:option {:value "orange"} "Orange"]]]]})

   (code-snippet "<ty-multiselect value=\"red,blue\" placeholder=\"Select colors...\">
  <option value=\"red\">Red</option>
  <option value=\"blue\">Blue</option>
  <option value=\"green\">Green</option>
</ty-multiselect>")

   (demo-section {:title "With Label"
                  :description "Multiselect with built-in label support"
                  :children [[:div.max-w-md
                              [:ty-multiselect {:label "Programming Languages"
                                                :placeholder "Select languages..."
                                                :style {:min-width "300px"}
                                                :on {:change multiselect-event-handler}}
                               [:option {:value "javascript"} "JavaScript"]
                               [:option {:value "python"} "Python"]
                               [:option {:value "java"} "Java"]
                               [:option {:value "clojure"} "Clojure"]
                               [:option {:value "rust"} "Rust"]
                               [:option {:value "go"} "Go"]]]]})

   (code-snippet "<ty-multiselect label=\"Programming Languages\" placeholder=\"Select languages...\">
  <option value=\"javascript\">JavaScript</option>
  <option value=\"python\">Python</option>
</ty-multiselect>")

   (demo-section {:title "Required Field"
                  :description "Required multiselect with indicator"
                  :children [[:div.max-w-md
                              [:ty-multiselect {:label "Skills"
                                                :required true
                                                :placeholder "Select your skills..."
                                                :style {:min-width "300px"}
                                                :on {:change multiselect-event-handler}}
                               [:option {:value "react"} "React"]
                               [:option {:value "vue"} "Vue.js"]
                               [:option {:value "angular"} "Angular"]
                               [:option {:value "node"} "Node.js"]
                               [:option {:value "python"} "Python"]
                               [:option {:value "java"} "Java"]]]]})

   (code-snippet "<ty-multiselect label=\"Skills\" required placeholder=\"Select your skills...\">
  <option value=\"react\">React</option>
  <option value=\"vue\">Vue.js</option>
</ty-multiselect>")])

(defn flavor-examples []
  [:div.demo-section
   [:h2.demo-title "Flavor Variants"]
   [:p.text-gray-600.dark:text-gray-400.mb-6
    "Semantic flavors affect both the dropdown border and chip colors"]

   [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
    [:div
     [:ty-multiselect {:flavor "positive"
                       :label "Positive (Green)"
                       :value "approved,verified"
                       :placeholder "Positive items..."
                       :style {:min-width "250px"}
                       :on {:change multiselect-event-handler}}
      [:option {:value "approved"} "Approved"]
      [:option {:value "verified"} "Verified"]
      [:option {:value "confirmed"} "Confirmed"]
      [:option {:value "validated"} "Validated"]]]

    [:div
     [:ty-multiselect {:flavor "negative"
                       :label "Negative (Red)"
                       :value "error,failed"
                       :placeholder "Negative items..."
                       :style {:min-width "250px"}
                       :on {:change multiselect-event-handler}}
      [:option {:value "error"} "Error"]
      [:option {:value "failed"} "Failed"]
      [:option {:value "rejected"} "Rejected"]
      [:option {:value "invalid"} "Invalid"]]]

    [:div
     [:ty-multiselect {:flavor "important"
                       :label "Important (Blue)"
                       :value "urgent,priority"
                       :placeholder "Important items..."
                       :style {:min-width "250px"}
                       :on {:change multiselect-event-handler}}
      [:option {:value "urgent"} "Urgent"]
      [:option {:value "priority"} "Priority"]
      [:option {:value "critical"} "Critical"]
      [:option {:value "essential"} "Essential"]]]

    [:div
     [:ty-multiselect {:label "Neutral (Default)"
                       :value "draft,pending"
                       :placeholder "Neutral items..."
                       :style {:min-width "250px"}
                       :on {:change multiselect-event-handler}}
      [:option {:value "draft"} "Draft"]
      [:option {:value "pending"} "Pending"]
      [:option {:value "review"} "Review"]
      [:option {:value "waiting"} "Waiting"]]]]

   (code-snippet "<ty-multiselect flavor=\"positive\" placeholder=\"Positive items...\">
  <option value=\"approved\">Approved</option>
  <option value=\"verified\">Verified</option>
</ty-multiselect>

<ty-multiselect flavor=\"negative\" placeholder=\"Negative items...\">
  <option value=\"error\">Error</option>
  <option value=\"failed\">Failed</option>
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
                               [:option {:value "option1"} "Option 1"]
                               [:option {:value "option2"} "Option 2"]
                               [:option {:value "option3"} "Option 3"]]]]})

   (code-snippet "<ty-multiselect value=\"option1,option2\" disabled>
  <option value=\"option1\">Option 1</option>
  <option value=\"option2\">Option 2</option>
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
     [:option {:value "event1"} "Event Test 1"]
     [:option {:value "event2"} "Event Test 2"]
     [:option {:value "event3"} "Event Test 3"]
     [:option {:value "event4"} "Event Test 4"]]]

   (code-snippet "const handleChange = (event) => {
  const { values, action, item } = event.detail;
  console.log('Multiselect changed:', { values, action, item });
};

<ty-multiselect onChange={handleChange}>
  <option value=\"event1\">Event Test 1</option>
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
