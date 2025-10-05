(ns ty.site.views.tabs-test
  "Test page for tabs components"
  (:require [ty.router :as router]))

(defn view []
  [:div.max-w-6xl.mx-auto.p-6
   ;; Page Header
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2 "Tabs Component Tests"]
    [:p.text-lg.ty-text- "Interactive testing for ty-tabs and ty-tab components"]]

   ;; Test 1: Basic Tabs with Text Labels
   [:section.mb-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "1. Basic Tabs (Text Labels)"]
    [:div.ty-elevated.rounded-lg.p-6
     [:ty-tabs {:width "100%"
                :height "400px"}
      [:ty-tab {:id "general"
                :label "General Settings"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "General Settings"]
        [:p.ty-text-.mb-4 "This is the general settings panel."]
        [:div.space-y-4
         [:div
          [:label.block.text-sm.font-medium.ty-text.mb-2 "Application Name"]
          [:ty-input {:value "My Application"
                      :class "w-full"}]]
         [:div
          [:label.block.text-sm.font-medium.ty-text.mb-2 "Description"]
          [:ty-textarea {:value "A sample application"
                         :class "w-full"}]]]]]

      [:ty-tab {:id "advanced"
                :label "Advanced Settings"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Advanced Settings"]
        [:p.ty-text-.mb-4 "Advanced configuration options."]
        [:div.space-y-4
         [:div
          [:label.block.text-sm.font-medium.ty-text.mb-2 "API Endpoint"]
          [:ty-input {:value "https://api.example.com"
                      :class "w-full"}]]
         [:div
          [:label.block.text-sm.font-medium.ty-text.mb-2 "Timeout (ms)"]
          [:ty-input {:type "number"
                      :value "5000"
                      :class "w-full"}]]]]]

      [:ty-tab {:id "about"
                :label "About"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "About"]
        [:p.ty-text-.mb-4 "Information about this application."]
        [:div.space-y-3
         [:p.ty-text- [:strong "Version:"] " 1.0.0"]
         [:p.ty-text- [:strong "Build:"] " 2025-01-05"]
         [:p.ty-text- [:strong "Author:"] " Ty Components Team"]]]]]]]

   ;; Test 2: Tabs with Icons (Rich Labels) - NEW API
   [:section.mb-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "2. Tabs with Icons (Rich Labels)"]
    [:div.ty-elevated.rounded-lg.p-6
     [:ty-tabs {:width "800px"
                :height "500px"
                :class "ty-elevated"}

      ;; Rich labels as direct children of ty-tabs
      [:span {:slot "label-profile"
              :class "flex items-center gap-2"}
       [:ty-icon {:name "user"
                  :size "sm"}]
       "Profile"]

      [:span {:slot "label-notifications"
              :class "flex items-center gap-2"}
       [:ty-icon {:name "bell"
                  :size "sm"}]
       "Notifications"
       [:span.ty-bg-danger.ty-text-danger++.px-2.py-0.5.rounded-full.text-xs.font-bold "5"]]

      [:span {:slot "label-settings-tab"
              :class "flex items-center gap-2"}
       [:ty-icon {:name "settings"
                  :size "sm"}]
       "Settings"]

      ;; Tab content
      [:ty-tab {:id "profile"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "User Profile"]
        [:div.space-y-4
         [:div.flex.items-center.gap-4
          [:div.w-20.h-20.rounded-full.ty-bg-primary.flex.items-center.justify-center
           [:ty-icon {:name "user"
                      :size "lg"
                      :class "ty-text-primary++"}]]
          [:div
           [:h4.text-lg.font-semibold.ty-text "John Doe"]
           [:p.ty-text-.text-sm "john.doe@example.com"]]]
         [:div
          [:label.block.text-sm.font-medium.ty-text.mb-2 "Full Name"]
          [:ty-input {:value "John Doe"
                      :class "w-full"}]]
         [:div
          [:label.block.text-sm.font-medium.ty-text.mb-2 "Email"]
          [:ty-input {:type "email"
                      :value "john.doe@example.com"
                      :class "w-full"}]]]]]

      [:ty-tab {:id "notifications"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Notifications"]
        [:div.space-y-3
         [:div.ty-elevated.p-4.rounded.flex.items-start.gap-3
          [:ty-icon {:name "mail"
                     :size "sm"
                     :class "ty-text-primary mt-1"}]
          [:div
           [:p.font-medium.ty-text "New message from Alice"]
           [:p.ty-text-.text-sm "2 minutes ago"]]]
         [:div.ty-elevated.p-4.rounded.flex.items-start.gap-3
          [:ty-icon {:name "star"
                     :size "sm"
                     :class "ty-text-warning mt-1"}]
          [:div
           [:p.font-medium.ty-text "You received a star"]
           [:p.ty-text-.text-sm "1 hour ago"]]]
         [:div.ty-elevated.p-4.rounded.flex.items-start.gap-3
          [:ty-icon {:name "message-circle"
                     :size "sm"
                     :class "ty-text-success mt-1"}]
          [:div
           [:p.font-medium.ty-text "New comment on your post"]
           [:p.ty-text-.text-sm "3 hours ago"]]]]]]

      [:ty-tab {:id "settings-tab"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Settings"]
        [:div.space-y-4
         [:div.flex.items-center.justify-between
          [:div
           [:p.font-medium.ty-text "Email Notifications"]
           [:p.ty-text-.text-sm "Receive email updates"]]
          [:ty-button {:flavor "primary"
                       :size "sm"} "Enable"]]
         [:div.flex.items-center.justify-between
          [:div
           [:p.font-medium.ty-text "Dark Mode"]
           [:p.ty-text-.text-sm "Use dark color scheme"]]
          [:ty-button {:flavor "secondary"
                       :size "sm"} "Toggle"]]
         [:div.flex.items-center.justify-between
          [:div
           [:p.font-medium.ty-text "Two-Factor Auth"]
           [:p.ty-text-.text-sm "Extra security layer"]]
          [:ty-button {:flavor "success"
                       :size "sm"} "Configure"]]]]]]]]

   ;; Test 3: Bottom Placement
   [:section.mb-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "3. Bottom Placement"]
    [:div.ty-elevated.rounded-lg.p-6
     [:ty-tabs {:width "600px"
                :height "400px"
                :placement "bottom"}
      [:ty-tab {:id "tab1"
                :label "Tab 1"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Tab 1 Content"]
        [:p.ty-text- "This tabs component has buttons at the bottom."]]]
      [:ty-tab {:id "tab2"
                :label "Tab 2"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Tab 2 Content"]
        [:p.ty-text- "Notice the tab buttons are positioned below the content."]]]
      [:ty-tab {:id "tab3"
                :label "Tab 3"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Tab 3 Content"]
        [:p.ty-text- "This can be useful for certain UI patterns."]]]]]]

   ;; Test 4: Disabled Tab
   [:section.mb-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "4. Disabled Tab"]
    [:div.ty-elevated.rounded-lg.p-6
     [:ty-tabs {:width "100%"
                :height "300px"}
      [:ty-tab {:id "enabled1"
                :label "Available"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Available Content"]
        [:p.ty-text- "This tab is available and clickable."]]]
      [:ty-tab {:id "disabled-tab"
                :label "Coming Soon"
                :disabled true}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Coming Soon"]
        [:p.ty-text- "This feature is not yet available."]]]
      [:ty-tab {:id "enabled2"
                :label "Also Available"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "More Available Content"]
        [:p.ty-text- "Another available tab."]]]]]]

   ;; Test 5: Long Content (Scrolling)
   [:section.mb-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "5. Long Content (Scrolling Test)"]
    [:div.ty-elevated.rounded-lg.p-6
     [:ty-tabs {:width "100%"
                :height "400px"}
      [:ty-tab {:id "short"
                :label "Short Content"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Short Content"]
        [:p.ty-text- "This tab has minimal content."]]]
      [:ty-tab {:id "long"
                :label "Long Content"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Long Content"]
        (for [i (range 1 21)]
          [:p.ty-text-.mb-4 {:key i}
           "Paragraph " i ". Lorem ipsum dolor sit amet, consectetur adipiscing elit. 
            Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. 
            Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris."])]]
      [:ty-tab {:id "medium"
                :label "Medium Content"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Medium Content"]
        (for [i (range 1 8)]
          [:p.ty-text-.mb-4 {:key i}
           "Paragraph " i ". Some medium-length content here."])]]]]]

   ;; Test 6: Programmatic Control
   [:section.mb-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "6. Programmatic Control"]
    [:div.ty-elevated.rounded-lg.p-6
     [:div.mb-4.flex.gap-2
      [:ty-button {:on {:click #(when-let [tabs (.querySelector js/document "#programmatic-tabs")]
                                  (set! (.-active tabs) "prog-1"))}}
       "Go to Tab 1"]
      [:ty-button {:on {:click #(when-let [tabs (.querySelector js/document "#programmatic-tabs")]
                                  (set! (.-active tabs) "prog-2"))}}
       "Go to Tab 2"]
      [:ty-button {:on {:click #(when-let [tabs (.querySelector js/document "#programmatic-tabs")]
                                  (set! (.-active tabs) "prog-3"))}}
       "Go to Tab 3"]]
     [:ty-tabs {:id "programmatic-tabs"
                :width "100%"
                :height "300px"
                :on {:ty-tab-change (fn [^js e]
                                      (let [detail (.-detail e)]
                                        (js/console.log "Tab changed!"
                                                        "Active:" (.-activeId detail)
                                                        "Previous:" (.-previousId detail))))}}
      [:ty-tab {:id "prog-1"
                :label "Tab 1"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Tab 1"]
        [:p.ty-text- "Use the buttons above to switch tabs programmatically."]]]
      [:ty-tab {:id "prog-2"
                :label "Tab 2"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Tab 2"]
        [:p.ty-text- "Check the console for tab change events."]]]
      [:ty-tab {:id "prog-3"
                :label "Tab 3"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Tab 3"]
        [:p.ty-text- "Events include active and previous tab information."]]]]]]

   ;; Test 7: Custom Animation Timing
   [:section.mb-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "7. Custom Animation Timing"]
    [:div.ty-elevated.rounded-lg.p-6
     [:ty-tabs {:width "100%"
                :height "300px"
                :style {:--transition-duration "600ms"
                        :--transition-easing "cubic-bezier(0.4, 0, 0.2, 1)"}}
      [:ty-tab {:id "slow1"
                :label "Slow Tab 1"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Slow Animation"]
        [:p.ty-text- "This tabs component has a slower animation (600ms)."]]]
      [:ty-tab {:id "slow2"
                :label "Slow Tab 2"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Custom Easing"]
        [:p.ty-text- "Uses cubic-bezier easing for smooth motion."]]]
      [:ty-tab {:id "slow3"
                :label "Slow Tab 3"}
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-4 "Notice the Difference"]
        [:p.ty-text- "Compare this to the default 300ms animation above."]]]]]]

   ;; Instructions
   [:section.ty-bg-primary-.ty-border-primary.border.rounded-lg.p-6
    [:h2.text-xl.font-semibold.ty-text-primary++.mb-4 "Test Instructions"]
    [:ul.list-disc.list-inside.space-y-2.ty-text-primary
     [:li "Click different tabs to test the carousel animation"]
     [:li "Observe the sliding direction (left/right based on tab position)"]
     [:li "Scroll within the long content tab to test panel scrolling"]
     [:li "Try the programmatic control buttons"]
     [:li "Check browser console for ty-tab-change events"]
     [:li "Resize the browser window to test responsive width"]
     [:li "Test with browser DevTools device emulation"]
     [:li "Try keyboard navigation (Tab key to focus buttons)"]
     [:li "Test with reduced motion enabled in OS preferences"]
     [:li "Test 2 shows the NEW API: labels as direct children of ty-tabs"]
     [:li "Notice how Tailwind classes and ty-icon work in Test 2 labels"]]]])
