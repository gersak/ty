(ns ty.demo.views.tags
  (:require [ty.demo.state :as state]))

(defn tag-event-handler [event-type]
  (fn [event]
    (let [detail (.-detail event)
          target (.-target detail)]
      (js/console.log (str "Tag " event-type ":") target)
      (when (= event-type "dismiss")
        ;; For demo purposes, add to removed tags list
        (swap! state/state update :removed-tags (fnil conj #{}) (.-textContent target))))))

(defn tags-view []
  [:div.max-w-6xl.mx-auto
   [:div.mb-8
    [:h1.text-3xl.font-bold.text-gray-900.dark:text-white.mb-2
     "Tag Component"]
    [:p.text-lg.text-gray-600.dark:text-gray-400
     "Flexible tag component with three slots and semantic flavors."]]

   ;; Basic Examples
   [:div.demo-section
    [:h2.demo-title "Basic Tags"]
    [:div.flex.flex-wrap.gap-2.mb-4
     [:ty-tag "JavaScript"]
     [:ty-tag "TypeScript"] 
     [:ty-tag "React"]
     [:ty-tag "Vue.js"]
     [:ty-tag "Svelte"]
     [:ty-tag {:not-pill true} "Rectangular"]
     [:ty-tag {:not-pill true :flavor "positive"} "Rectangle"]]]

   ;; Size Variants
   [:div.demo-section
    [:h2.demo-title "Size Variants"]
    [:div.space-y-4
     [:div
      [:h3.text-sm.font-medium.mb-2 "Extra Small (xs)"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:size "xs"} "xs"]
       [:ty-tag {:size "xs" :flavor "positive"} "Success"]
       [:ty-tag {:size "xs" :flavor "negative"} "Error"]]]
     
     [:div
      [:h3.text-sm.font-medium.mb-2 "Small (sm)"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:size "sm"} "sm"]
       [:ty-tag {:size "sm" :flavor "important"} "Important"]
       [:ty-tag {:size "sm" :flavor "exception"} "Warning"]]]
     
     [:div
      [:h3.text-sm.font-medium.mb-2 "Medium (md) - Default"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag "md default"]
       [:ty-tag {:flavor "unique"} "Unique"]
       [:ty-tag {:flavor "neutral"} "Neutral"]]]
     
     [:div
      [:h3.text-sm.font-medium.mb-2 "Large (lg)"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:size "lg"} "lg"]
       [:ty-tag {:size "lg" :flavor "positive"} "Large Success"]
       [:ty-tag {:size "lg" :flavor "important"} "Large Important"]]]
     
     [:div
      [:h3.text-sm.font-medium.mb-2 "Extra Large (xl)"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:size "xl"} "xl"]
       [:ty-tag {:size "xl" :flavor "exception"} "Extra Large"]]]]]

   ;; Shape Variants
   [:div.demo-section
    [:h2.demo-title "Shape Variants"]
    [:div.space-y-4
     [:div
      [:h3.text-sm.font-medium.mb-2 "Pill Shape (default)"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag "Default Pill"]
       [:ty-tag {:flavor "positive"} "Success"]
       [:ty-tag {:flavor "important"} "Important"]]]
     
     [:div
      [:h3.text-sm.font-medium.mb-2 "Rectangular Shape"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:not-pill true} "Rectangular"]
       [:ty-tag {:not-pill true :flavor "positive"} "Success"]
       [:ty-tag {:not-pill true :flavor "important"} "Important"]]]]]

   ;; Rich Tags with Icons
   [:div.demo-section
    [:h2.demo-title "Rich Tags with Icons"]
    [:div.space-y-6
     [:div
      [:h3.text-sm.font-medium.mb-3 "Status Tags with Icons"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:flavor "positive"}
        [:ty-icon {:slot "start" :name "check-circle"}]
        "Completed"]
       
       [:ty-tag {:flavor "exception"}
        [:ty-icon {:slot "start" :name "clock"}]
        "In Progress"]
       
       [:ty-tag {:flavor "negative"}
        [:ty-icon {:slot "start" :name "x-circle"}]
        "Failed"]
       
       [:ty-tag {:flavor "neutral"}
        [:ty-icon {:slot "start" :name "pause"}]
        "Paused"]]]
     
     [:div
      [:h3.text-sm.font-medium.mb-3 "Technology Tags"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:flavor "important"}
        [:ty-icon {:slot "start" :name "code"}]
        "JavaScript"]
       
       [:ty-tag {:flavor "positive"}
        [:ty-icon {:slot "start" :name "check"}]
        "React"]
       
       [:ty-tag {:flavor "exception"}
        [:ty-icon {:slot "start" :name "lightning-bolt"}]
        "Node.js"]
       
       [:ty-tag {:flavor "unique"}
        [:ty-icon {:slot "start" :name "star"}]
        "TypeScript"]]]
     
     [:div
      [:h3.text-sm.font-medium.mb-3 "Tags with Counts/Badges"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:flavor "important"}
        "Issues"
        [:span {:slot "end" :class "count"} "5"]]
       
       [:ty-tag {:flavor "positive"}
        [:ty-icon {:slot "start" :name "check"}]
        "Completed"
        [:span {:slot "end" :class "count"} "23"]]
       
       [:ty-tag {:flavor "exception"}
        "Pending Review"
        [:span {:slot "end" :class "count"} "8"]]
       
       [:ty-tag {:flavor "unique"}
        [:ty-icon {:slot "start" :name "star"}]
        "Featured"
        [:span {:slot "end" :class "count"} "12"]]]]
     
     [:div
      [:h3.text-sm.font-medium.mb-3 "User/Team Tags"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:flavor "neutral"}
        [:div {:slot "start" :class "w-4 h-4 bg-green-500 rounded-full"}]
        "john@example.com"]
       
       [:ty-tag {:flavor "neutral"}
        [:div {:slot "start" :class "w-4 h-4 bg-blue-500 rounded-full"}]
        "jane.designer"]
       
       [:ty-tag {:flavor "neutral"}
        [:div {:slot "start" :class "w-4 h-4 bg-purple-500 rounded-full"}]
        "mike.dev"
        [:span {:slot "end" :class "count"} "Admin"]]
       
       [:ty-tag {:flavor "important"}
        [:ty-icon {:slot "start" :name "user"}]
        "Development Team"
        [:span {:slot "end" :class "count"} "7"]]]]]]

   ;; Dismissible Tags
   [:div.demo-section
    [:h2.demo-title "Dismissible Tags"]
    [:div.space-y-6
     [:div
      [:h3.text-sm.font-medium.mb-3 "Simple Dismissible Tags"]
      [:p.text-sm.text-gray-600.mb-3 "Click the × to remove these tags"]
      [:div.flex.flex-wrap.gap-2
       (when-not (contains? (:removed-tags @state/state #{}) "React")
         [:ty-tag {:dismissible true :flavor "positive" :on {:ty-tag-dismiss (tag-event-handler "dismiss")}}
          "React"])
       
       (when-not (contains? (:removed-tags @state/state #{}) "Vue.js")
         [:ty-tag {:dismissible true :flavor "important" :on {:ty-tag-dismiss (tag-event-handler "dismiss")}}
          "Vue.js"])
       
       (when-not (contains? (:removed-tags @state/state #{}) "Angular")
         [:ty-tag {:dismissible true :flavor "exception" :on {:ty-tag-dismiss (tag-event-handler "dismiss")}}
          "Angular"])
       
       (when-not (contains? (:removed-tags @state/state #{}) "Svelte")
         [:ty-tag {:dismissible true :flavor "unique" :on {:ty-tag-dismiss (tag-event-handler "dismiss")}}
          "Svelte"])]]
     
     [:div
      [:h3.text-sm.font-medium.mb-3 "Dismissible Tags with Icons"]
      [:p.text-sm.text-gray-600.mb-3 "Rich dismissible tags with icons and counts"]
      [:div.flex.flex-wrap.gap-2
       (when-not (contains? (:removed-tags @state/state #{}) "Frontend Team")
         [:ty-tag {:dismissible true :flavor "positive" :on {:ty-tag-dismiss (tag-event-handler "dismiss")}}
          [:ty-icon {:slot "start" :name "code"}]
          "Frontend Team"
          [:span {:slot "end" :class "count"} "4"]])
       
       (when-not (contains? (:removed-tags @state/state #{}) "Backend Team")
         [:ty-tag {:dismissible true :flavor "important" :on {:ty-tag-dismiss (tag-event-handler "dismiss")}}
          [:ty-icon {:slot "start" :name "server"}]
          "Backend Team"
          [:span {:slot "end" :class "count"} "6"]])
       
       (when-not (contains? (:removed-tags @state/state #{}) "Design Team")
         [:ty-tag {:dismissible true :flavor "unique" :on {:ty-tag-dismiss (tag-event-handler "dismiss")}}
          [:ty-icon {:slot "start" :name "palette"}]
          "Design Team"
          [:span {:slot "end" :class "count"} "3"]])]]
     
     [:div
      [:h3.text-sm.font-medium.mb-3 "Rectangular Dismissible Tags"]
      [:div.flex.flex-wrap.gap-2
       (when-not (contains? (:removed-tags @state/state #{}) "Bug Report")
         [:ty-tag {:not-pill true :dismissible true :flavor "negative" :on {:ty-tag-dismiss (tag-event-handler "dismiss")}}
          [:ty-icon {:slot "start" :name "x-circle"}]
          "Bug Report"])
       
       (when-not (contains? (:removed-tags @state/state #{}) "Feature Request")
         [:ty-tag {:not-pill true :dismissible true :flavor "exception" :on {:ty-tag-dismiss (tag-event-handler "dismiss")}}
          [:ty-icon {:slot "start" :name "lightning-bolt"}]
          "Feature Request"])
       
       (when-not (contains? (:removed-tags @state/state #{}) "Enhancement")
         [:ty-tag {:not-pill true :dismissible true :flavor "positive" :on {:ty-tag-dismiss (tag-event-handler "dismiss")}}
          [:ty-icon {:slot "start" :name "check-circle"}]
          "Enhancement"])]]]

   ;; Interactive Examples
   [:div.demo-section
    [:h2.demo-title "Interactive Examples"]
    [:div.space-y-6
     [:div
      [:h3.text-sm.font-medium.mb-3 "Clickable Filter Tags"]
      [:p.text-sm.text-gray-600.mb-3 "Click these tags to filter content"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:clickable true :flavor "neutral" :on {:ty-tag-click (tag-event-handler "click")}}
        [:ty-icon {:slot "start" :name "filter"}]
        "All Items"]
       
       [:ty-tag {:clickable true :flavor "positive" :on {:ty-tag-click (tag-event-handler "click")}}
        [:ty-icon {:slot "start" :name "check"}]
        "Completed"
        [:span {:slot "end" :class "count"} "12"]]
       
       [:ty-tag {:clickable true :flavor "exception" :on {:ty-tag-click (tag-event-handler "click")}}
        [:ty-icon {:slot "start" :name "clock"}]
        "Pending"
        [:span {:slot "end" :class "count"} "8"]]
       
       [:ty-tag {:clickable true :flavor "important" :on {:ty-tag-click (tag-event-handler "click")}}
        [:ty-icon {:slot "start" :name "exclamation"}]
        "High Priority"
        [:span {:slot "end" :class "count"} "3"]]]]
     
     [:div
      [:h3.text-sm.font-medium.mb-3 "Combined: Clickable + Dismissible"]
      [:p.text-sm.text-gray-600.mb-3 "These tags are both clickable and dismissible"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:clickable true :dismissible true :flavor "positive" 
                 :on {:ty-tag-click (tag-event-handler "click")
                      :ty-tag-dismiss (tag-event-handler "dismiss")}}
        [:ty-icon {:slot "start" :name "tag"}]
        "Marketing"]
       
       [:ty-tag {:clickable true :dismissible true :flavor "important"
                 :on {:ty-tag-click (tag-event-handler "click")
                      :ty-tag-dismiss (tag-event-handler "dismiss")}}
        [:ty-icon {:slot "start" :name "code"}]
        "Development"
        [:span {:slot "end" :class "count"} "15"]]
       
       [:ty-tag {:not-pill true :clickable true :dismissible true :flavor "unique"
                 :on {:ty-tag-click (tag-event-handler "click")
                      :ty-tag-dismiss (tag-event-handler "dismiss")}}
        [:ty-icon {:slot "start" :name "star"}]
        "Premium Feature"]]]]]

   ;; Advanced Usage Examples
   [:div.demo-section
    [:h2.demo-title "Advanced Usage Examples"]
    [:div.space-y-6
     [:div
      [:h3.text-sm.font-medium.mb-3 "Skill Tags with Experience Levels"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:size "sm" :flavor "important"}
        "JavaScript"
        [:span {:slot "end" :class "count"} "5y"]]
       
       [:ty-tag {:size "sm" :flavor "positive"}
        "React"
        [:span {:slot "end" :class "count"} "3y"]]
       
       [:ty-tag {:size "sm" :flavor "exception"}
        "Node.js"
        [:span {:slot "end" :class "count"} "4y"]]
       
       [:ty-tag {:size "sm" :flavor "unique"}
        "GraphQL"
        [:span {:slot "end" :class "count"} "2y"]]]]
     
     [:div
      [:h3.text-sm.font-medium.mb-3 "Project Status Dashboard"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:not-pill true :flavor "positive"}
        [:ty-icon {:slot "start" :name "check-circle"}]
        "Deployed to Production"]
       
       [:ty-tag {:not-pill true :flavor "exception"}
        [:ty-icon {:slot "start" :name "clock"}]
        "Awaiting Code Review"
        [:span {:slot "end" :class "count"} "2"]]
       
       [:ty-tag {:not-pill true :flavor "important"}
        [:ty-icon {:slot "start" :name "exclamation"}]
        "Needs Testing"]
       
       [:ty-tag {:not-pill true :flavor "negative"}
        [:ty-icon {:slot "start" :name "x-circle"}]
        "Build Failed"]]]]]]

   ;; Code Examples
   [:div.demo-section
    [:h2.demo-title "Code Examples"]
    [:div.space-y-4
     [:div.p-4.bg-gray-50.dark:bg-gray-800.rounded-lg
      [:h4.text-sm.font-medium.mb-2 "Basic Usage"]
      [:pre.text-xs.text-gray-700.dark:text-gray-300
       "[:ty-tag \"JavaScript\"]\n"
       "[:ty-tag {:flavor \"positive\"} \"Success\"]\n"
       "[:ty-tag {:not-pill true :flavor \"important\"} \"Rectangle\"]"]]
     
     [:div.p-4.bg-gray-50.dark:bg-gray-800.rounded-lg
      [:h4.text-sm.font-medium.mb-2 "Rich Tags with Icons"]
      [:pre.text-xs.text-gray-700.dark:text-gray-300
       "[:ty-tag {:flavor \"positive\"}\n"
       " [:ty-icon {:slot \"start\" :name \"check\"}]\n"
       " \"Completed\"\n"
       " [:span {:slot \"end\" :class \"count\"} \"5\"]]"]]
     
     [:div.p-4.bg-gray-50.dark:bg-gray-800.rounded-lg
      [:h4.text-sm.font-medium.mb-2 "Dismissible Tags"]
      [:pre.text-xs.text-gray-700.dark:text-gray-300
       "[:ty-tag {:dismissible true :on {:ty-tag-dismiss handler}}\n"
       " \"Dismiss Me\"]\n\n"
       ";; With icons\n"
       "[:ty-tag {:dismissible true :flavor \"negative\"}\n"
       " [:ty-icon {:slot \"start\" :name \"x-circle\"}]\n"
       " \"Error Tag\"]"]]
     
     [:div.p-4.bg-gray-50.dark:bg-gray-800.rounded-lg
      [:h4.text-sm.font-medium.mb-2 "Interactive Tags"]
      [:pre.text-xs.text-gray-700.dark:text-gray-300
       "[:ty-tag {:clickable true :on {:ty-tag-click handler}}\n"
       " \"Click Me\"]\n\n"
       ";; Combined: clickable + dismissible\n"
       "[:ty-tag {:clickable true :dismissible true\n"
       "          :on {:ty-tag-click click-handler\n"
       "               :ty-tag-dismiss dismiss-handler}}\n"
       " \"Interactive Tag\"]"]]]]

   ;; Reset button for demo
   [:div.demo-section
    [:div.flex.items-center.gap-4
     [:ty-button {:flavor "neutral" :size "sm"
                  :on {:click #(swap! state/state dissoc :removed-tags)}}
      "Reset Dismissed Tags"]
     
     (when (seq (:removed-tags @state/state))
       [:div.text-sm.text-gray-600
        "Removed tags: " (clojure.string/join ", " (:removed-tags @state/state))])]]])
