(ns ty.site.docs.wizard
  "Documentation for ty-wizard component"
  (:require [clojure.string :as str]
            [ty.site.docs.common :refer [code-block attribute-table event-table]]
            [ty.site.state :refer [state]]))

;; =====================================================
;; Interactive Demo
;; =====================================================

(defn wizard-demo []
  "Interactive wizard demo using real ty-wizard component"
  (let [wizard-state (get @state :wizard-prototype {:active-step "welcome"
                                                    :completed-steps #{}})
        active-step (:active-step wizard-state)
        completed-steps (:completed-steps wizard-state)
        ;; Convert set to comma-separated string for component attribute
        completed-str (str/join "," completed-steps)

        ;; Helper to go to a specific step
        go-to-step (fn [step-id]
                     (swap! state assoc-in [:wizard-prototype :active-step] step-id))

        ;; Helper to go to next step and mark current as complete
        next-step (fn [current-id next-id]
                    (swap! state update-in [:wizard-prototype :completed-steps] conj current-id)
                    (swap! state assoc-in [:wizard-prototype :active-step] next-id))]

    [:ty-wizard
     {:width "900px"
      :height "700px"
      :active active-step
      :completed completed-str
      :on {:ty-wizard-step-change
           (fn [^js e]
             (let [detail (.-detail e)
                   new-active (.-activeId detail)]
               ;; User clicked a step indicator - update active step
               (swap! state assoc-in [:wizard-prototype :active-step] new-active)))}}

     ;; Custom step indicators via slots
     [:div {:slot "indicator-welcome"}
      [:ty-icon {:name "hand"
                 :size "sm"}]]

     [:div {:slot "indicator-account"}
      [:span.text-md.font-bold "1"]]

     [:div {:slot "indicator-profile"}
      [:span.text-md.font-bold "2"]]

     [:div {:slot "indicator-preferences"}
      [:span.text-md.font-bold "3"]]

     ;; Step content panels with navigation buttons INSIDE
     [:ty-step {:id "welcome"
                :label "Welcome"
                :description "Get started"}
      [:div.flex.flex-col.h-full.ty-elevated
       [:div.flex-1.flex.flex-col.items-center.justify-center.text-center.p-6
        [:h1.ty-text++.text-3xl.font-bold.mb-3 "Welcome to Ty Components!"]
        [:p.ty-text-.mb-6.max-w-md
         "Let's get you set up in just a few steps. This will only take a couple of minutes."]]
       ;; Navigation footer
       [:div.flex.justify-end.gap-2.p-4.border-t.border-ty-border-.bg-ty-surface
        [:ty-button
         {:flavor "primary"
          :on {:click #(next-step "welcome" "account")}}
         "Get Started"
         [:ty-icon {:slot "end" :name "arrow-right" :size "sm"}]]]]]

     [:ty-step {:id "account"
                :label "Account"
                :description "Email & password"}
      [:div.flex.flex-col.h-full.ty-elevated
       [:div.flex-1.p-6.space-y-4
        [:div
         [:h2.ty-text++.text-2xl.font-bold.mb-2 "Create Your Account"]
         [:p.ty-text-.mb-4 "Enter your email and choose a secure password."]]
        [:div.space-y-3.max-w-xl
         [:ty-input
          {:label "Email Address"
           :type "email"
           :placeholder "you@example.com"
           :required true}]
         [:ty-input
          {:label "Password"
           :type "password"
           :placeholder "Min. 8 characters"
           :required true}]
         [:ty-input
          {:label "Confirm Password"
           :type "password"
           :placeholder "Re-enter your password"
           :required true}]]]
       ;; Navigation footer
       [:div.flex.justify-end.gap-2.p-4.border-t.border-ty-border-.bg-ty-surface
        [:ty-button
         {:flavor "neutral"
          :on {:click #(go-to-step "welcome")}}
         [:ty-icon {:slot "start" :name "arrow-left" :size "sm"}]
         "Previous"]
        [:ty-button
         {:flavor "primary"
          :on {:click #(next-step "account" "profile")}}
         "Next"
         [:ty-icon {:slot "end" :name "arrow-right" :size "sm"}]]]]]

     [:ty-step {:id "profile"
                :label "Profile"
                :description "Personal info"}
      [:div.flex.flex-col.h-full.ty-elevated
       [:div.flex-1.p-6.space-y-4
        [:div
         [:h2.ty-text++.text-2xl.font-bold.mb-2 "Tell Us About Yourself"]
         [:p.ty-text-.mb-4 "Help us personalize your experience."]]
        [:div.space-y-3.max-w-xl
         [:ty-input
          {:label "Full Name"
           :placeholder "John Doe"
           :required true}]
         [:ty-input
          {:label "Company"
           :placeholder "Acme Inc. (optional)"}]
         [:ty-dropdown
          {:label "Role"
           :placeholder "Select your role"}
          [:ty-dropdown-option {:value "developer"} "Developer"]
          [:ty-dropdown-option {:value "designer"} "Designer"]
          [:ty-dropdown-option {:value "manager"} "Manager"]
          [:ty-dropdown-option {:value "other"} "Other"]]]]
       ;; Navigation footer
       [:div.flex.justify-end.gap-2.p-4.border-t.border-ty-border-.bg-ty-surface
        [:ty-button
         {:flavor "neutral"
          :on {:click #(go-to-step "account")}}
         [:ty-icon {:slot "start" :name "arrow-left" :size "sm"}]
         "Previous"]
        [:ty-button
         {:flavor "primary"
          :on {:click #(next-step "profile" "preferences")}}
         "Next"
         [:ty-icon {:slot "end" :name "arrow-right" :size "sm"}]]]]]

     [:ty-step {:id "preferences"
                :label "Preferences"
                :description "Customize settings"}
      [:div.flex.flex-col.h-full.ty-elevated
       [:div.flex-1.p-6.space-y-4
        [:div
         [:h2.ty-text++.text-2xl.font-bold.mb-2 "Customize Your Experience"]
         [:p.ty-text-.mb-4 "Configure your preferences."]]
        [:div.space-y-3.max-w-xl
         [:div.ty-elevated.p-4.rounded-lg.space-y-3
          [:h3.ty-text+.font-semibold "Notifications"]
          [:ty-checkbox "Email notifications"]
          [:ty-checkbox "Push notifications"]
          [:ty-checkbox "Weekly summary email"]]
         [:div.ty-elevated.p-4.rounded-lg.space-y-3
          [:h3.ty-text+.font-semibold "Privacy"]
          [:ty-checkbox {:checked true} "Make profile public"]
          [:ty-checkbox "Allow others to message me"]]]]
       ;; Navigation footer
       [:div.flex.justify-end.gap-2.p-4.border-t.border-ty-border-.bg-ty-surface
        [:ty-button
         {:flavor "neutral"
          :on {:click #(go-to-step "profile")}}
         [:ty-icon {:slot "start" :name "arrow-left" :size "sm"}]
         "Previous"]
        [:ty-button
         {:flavor "success"
          :on {:click #(do
                         (next-step "preferences" "welcome")
                         (js/alert "Setup complete! 🎉"))}}
         [:ty-icon {:slot "start" :name "check" :size "sm"}]
         "Complete Setup"]]]]]))

(defn wizard-prototype []
  "Complete wizard demo"
  [:div.flex.justify-center
   (wizard-demo)])

;; =====================================================
;; Documentation Sections
;; =====================================================

(defn intro []
  [:div.mb-8
   [:h1.text-3xl.font-bold.ty-text.mb-4 "Wizard"]
   [:p.ty-text-.text-lg.mb-4
    "A multi-step wizard component for guiding users through sequential processes like onboarding, checkouts, and complex forms. "
    "Behaves like tabs - a \"dumb\" component that only renders and fires events. All navigation, validation, and state management "
    "is controlled by you."]

   [:div.ty-bg-success-.ty-border-success.border.rounded-lg.p-4
    [:p.ty-text-success++.font-semibold.mb-2 "✅ Component Implemented"]
    [:p.ty-text-success.text-sm
     "The ty-wizard component is now available! Built following the ty-tabs pattern with container + child architecture, "
     "carousel animation, and progress tracking. Put your navigation buttons inside step content and control everything via the "
     "active and completed attributes."]]])

(defn prototype-section []
  [:div.mb-12
   [:h2.text-2xl.font-semibold.ty-text.mb-4 "Interactive Prototype"]
   [:p.ty-text-.mb-6
    "Try the interactive prototype below. Notice the carousel sliding animation and progress line."]
   [:div.flex.justify-center
    (wizard-prototype)]])

(defn api-spec []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-semibold.ty-text.mb-6 "Planned API"]

   [:div.mb-6
    [:h3.text-xl.font-medium.ty-text+.mb-4 "Basic Usage"]
    (code-block
     "(defn onboarding []
  (let [go-to-step (fn [id] (reset! active-step id))
        next-step (fn [current next]
                    (swap! completed conj current)
                    (reset! active-step next))]
    [:ty-wizard
     {:active @active-step
      :completed (str/join \",\" @completed)
      :width \"1000px\"
      :height \"700px\"
      :on {:ty-wizard-step-change
           (fn [e]
             (let [detail (.-detail e)]
               (reset! active-step (.-activeId detail))))}}

     ;; Custom step indicators via slots
     [:div {:slot \"indicator-welcome\"}
      [:ty-icon {:name \"hand\" :size \"sm\"}]]

     [:div {:slot \"indicator-account\"}
      [:span.text-lg.font-bold \"1\"]]

     ;; Step content with navigation inside
     [:ty-step {:id \"welcome\" :label \"Welcome\"}
      [:div.p-8
       [:h1 \"Welcome!\"]
       [:button {:on-click #(next-step \"welcome\" \"account\")}
        \"Next\"]]]

     [:ty-step {:id \"account\" :label \"Account\"}
      [:div.p-8
       [:ty-input {:label \"Email\"}]
       [:button {:on-click #(go-to-step \"welcome\")} \"Previous\"]
       [:button {:on-click #(next-step \"account\" \"complete\")} \"Next\"]]]]))")]

   [:div.mb-6
    [:h3.text-xl.font-medium.ty-text+.mb-4 "ty-wizard (Container)"]
    (attribute-table
     [{:name "active"
       :type "string"
       :default "first step"
       :description "ID of currently active step (controlled by user)"}
      {:name "completed"
       :type "string"
       :default "\"\""
       :description "Comma-separated IDs of completed steps (user controls)"}
      {:name "width"
       :type "string"
       :default "\"100%\""
       :description "Container width (px or %)"}
      {:name "height"
       :type "string"
       :default "\"700px\""
       :description "Container height"}
      {:name "orientation"
       :type "\"horizontal\" | \"vertical\""
       :default "\"horizontal\""
       :description "Step indicator layout (vertical not yet implemented)"}])]

   [:div.mb-6
    [:h3.text-xl.font-medium.ty-text+.mb-4 "ty-step (Child)"]
    (attribute-table
     [{:name "id"
       :type "string"
       :default "-"
       :description "Unique step identifier (required)"}
      {:name "label"
       :type "string"
       :default "-"
       :description "Text label shown below step circle"}
      {:name "disabled"
       :type "boolean"
       :default "false"
       :description "Step cannot be activated"}])]

   [:div
    [:h3.text-xl.font-medium.ty-text+.mb-4 "Events"]
    (event-table
     [{:name "ty-wizard-step-change"
       :detail "{activeId, activeIndex, previousId, previousIndex, direction}"
       :description "Fired when active step changes"}])]])

(defn features []
  [:div.mb-12
   [:h2.text-2xl.font-semibold.ty-text.mb-6 "Key Features"]
   [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
    [:div.ty-elevated.p-5.rounded-lg
     [:h3.ty-text+.font-semibold.mb-2.flex.items-center.gap-2
      [:ty-icon.ty-text-primary {:name "check-circle"
                                 :size "sm"}]
      "Carousel Sliding"]
     [:p.ty-text-.text-sm "Smooth sliding transitions between steps, just like ty-tabs"]]

    [:div.ty-elevated.p-5.rounded-lg
     [:h3.ty-text+.font-semibold.mb-2.flex.items-center.gap-2
      [:ty-icon.ty-text-success {:name "trending-up"
                                 :size "sm"}]
      "Progress Indicator"]
     [:p.ty-text-.text-sm "Visual progress line showing completion status"]]

    [:div.ty-elevated.p-5.rounded-lg
     [:h3.ty-text+.font-semibold.mb-2.flex.items-center.gap-2
      [:ty-icon.ty-text-warning {:name "mouse-pointer-2"
                                 :size "sm"}]
      "User Controlled"]
     [:p.ty-text-.text-sm "Dumb component that only renders - you control all navigation and validation"]]

    [:div.ty-elevated.p-5.rounded-lg
     [:h3.ty-text+.font-semibold.mb-2.flex.items-center.gap-2
      [:ty-icon.ty-text-info {:name "layers"
                              :size "sm"}]
      "Rich Customization"]
     [:p.ty-text-.text-sm "Custom step indicators via slots, full styling control"]]]])

(defn use-cases []
  [:div.mb-12
   [:h2.text-2xl.font-semibold.ty-text.mb-6 "Use Cases"]
   [:div.grid.gap-4
    [:div.ty-elevated.p-4.rounded-lg
     [:h3.ty-text+.font-semibold.mb-2.flex.items-center.gap-2
      [:ty-icon {:name "user"
                 :size "sm"}]
      "User Onboarding"]
     [:p.ty-text-.text-sm "Multi-step account creation with profile setup and preferences"]]

    [:div.ty-elevated.p-4.rounded-lg
     [:h3.ty-text+.font-semibold.mb-2.flex.items-center.gap-2
      [:ty-icon {:name "briefcase"
                 :size "sm"}]
      "Checkout Flow"]
     [:p.ty-text-.text-sm "Cart → Shipping → Payment → Review → Confirmation"]]

    [:div.ty-elevated.p-4.rounded-lg
     [:h3.ty-text+.font-semibold.mb-2.flex.items-center.gap-2
      [:ty-icon {:name "users"
                 :size "sm"}]
      "Complex Entity Creation"]
     [:p.ty-text-.text-sm "Creating users with roles, groups, permissions, and settings"]]

    [:div.ty-elevated.p-4.rounded-lg
     [:h3.ty-text+.font-semibold.mb-2.flex.items-center.gap-2
      [:ty-icon {:name "settings"
                 :size "sm"}]
      "Configuration Wizards"]
     [:p.ty-text-.text-sm "Guided setup for complex systems or integrations"]]]])

;; =====================================================
;; Main View
;; =====================================================

(defn view []
  [:div.max-w-7xl.mx-auto.p-6
   (intro)
   (prototype-section)
   (features)
   (api-spec)
   (use-cases)])
