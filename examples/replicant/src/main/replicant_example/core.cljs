(ns replicant-example.core
  "Main entry point for the Replicant + Ty components example"
  (:require [replicant.core :as r]
            [replicant.dom :as dom]
            [ty.components]
            [ty.icons :as icons]
            [ty.lucide :as lucide]))

(defonce app-state
  (atom {:selected-option nil
         :selected-date nil
         :selected-tags #{}
         :form-data {}
         :show-modal false}))

(defn icon-example []
  [:div.example-card
   [:h2.example-title "Icons"]
   [:p.example-description
    "Using Ty icons from the registry system."]
   [:div.flex.items-center.gap-4.flex-wrap
    [:ty-icon {:name "heart"
               :class "ty-text-danger text-2xl"}]
    [:ty-icon {:name "star"
               :class "ty-text-warning text-2xl"}]
    [:ty-icon {:name "check-circle"
               :class "ty-text-success text-2xl"}]
    [:ty-icon {:name "settings"
               :class "ty-text- text-2xl"}]
    [:ty-icon {:name "user"
               :class "ty-text-primary text-2xl"}]]])

(defn dropdown-example []
  (let [options [{:value "usa"
                  :label "United States"
                  :icon "flag"}
                 {:value "uk"
                  :label "United Kingdom"
                  :icon "flag"}
                 {:value "canada"
                  :label "Canada"
                  :icon "flag"}
                 {:value "germany"
                  :label "Germany"
                  :icon "flag"}]]
    [:div.example-card
     [:h2.example-title "Dropdown"]
     [:p.example-description
      "Country selection with icons and rich content."]
     [:div.space-y-4
      [:label.ty-text+.block.text-sm.font-medium "Select Country"]
      [:ty-dropdown
       {:placeholder "Choose a country..."
        :class "w-full"}
       (for [option options]
         [:ty-option {:key (:value option)
                      :value (:value option)}
          [:div.flex.items-center.gap-2
           [:ty-icon {:name (:icon option)
                      :class "ty-text-"}]
           [:span (:label option)]]])]]]))

(defn multiselect-example []
  (let [skills [{:value "clojure"
                 :label "Clojure"
                 :color "primary"}
                {:value "javascript"
                 :label "JavaScript"
                 :color "warning"}
                {:value "react"
                 :label "React"
                 :color "info"}
                {:value "css"
                 :label "CSS"
                 :color "success"}
                {:value "design"
                 :label "Design"
                 :color "secondary"}]]
    [:div.example-card
     [:h2.example-title "Multiselect"]
     [:p.example-description
      "Skills selection with colorful tags."]
     [:div.space-y-4
      [:label.ty-text+.block.text-sm.font-medium "Select Skills"]
      [:ty-multiselect
       {:placeholder "Choose your skills..."
        :class "w-full"}
       (for [skill skills]
         [:ty-tag {:flavor (:color skill)
                   :value (:value skill)}
          (:label skill)])]]]))

(defn calendar-example []
  [:div.example-card
   [:h2.example-title "Calendar"]
   [:p.example-description
    "Date selection with calendar component."]
   [:div.space-y-4
    [:label.ty-text+.block.text-sm.font-medium "Select Date"]
    [:ty-date-picker
     {:class "w-full"
      :placeholder "Choose a date..."}]]])

(defn form-example []
  [:div.example-card
   [:h2.example-title "Complete Form"]
   [:p.example-description
    "A form combining all components with Tailwind styling."]
   [:form.space-y-6
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
     [:div
      [:label.ty-text+.block.text-sm.font-medium.mb-2 "First Name"]
      [:ty-input {:placeholder "Enter your first name"
                  :class "w-full"}]]
     [:div
      [:label.ty-text+.block.text-sm.font-medium.mb-2 "Last Name"]
      [:ty-input {:placeholder "Enter your last name"
                  :class "w-full"}]]]

    [:div
     [:label.ty-text+.block.text-sm.font-medium.mb-2 "Country"]
     [:ty-dropdown
      {:placeholder "Select your country..."
       :class "w-full"}
      [:ty-option {:value "usa"} "🇺🇸 United States"]
      [:ty-option {:value "uk"} "🇬🇧 United Kingdom"]
      [:ty-option {:value "canada"} "🇨🇦 Canada"]]]

    [:div
     [:label.ty-text+.block.text-sm.font-medium.mb-2 "Skills"]
     [:ty-multiselect
      {:placeholder "Select your skills..."
       :class "w-full"}
      [:ty-tag {:flavor "primary"} "Clojure"]
      [:ty-tag {:flavor "warning"} "JavaScript"]
      [:ty-tag {:flavor "success"} "CSS"]]]

    [:div
     [:label.ty-text+.block.text-sm.font-medium.mb-2 "Birth Date"]
     [:ty-date-picker
      {:class "w-full"
       :placeholder "Select your birth date"}]]

    [:div.flex.gap-4
     [:ty-button {:type "submit"
                  :flavor "primary"}
      [:ty-icon {:name "check"}]
      "Submit"]
     [:ty-button {:type "button"
                  :flavor "secondary"}
      [:ty-icon {:name "x"}]
      "Cancel"]]]])

(defn app []
  [:div.space-y-8
   (icon-example)
   (dropdown-example)
   (multiselect-example)
   (calendar-example)
   (form-example)])

(defn render! []
  (dom/render (.getElementById js/document "app") (app)))

(defn init []
  (println "🚀 Initializing Replicant + Ty Components Example")

  ;; Register Lucide icons we need
  (icons/register-icons!
    {"heart" lucide/heart
     "star" lucide/star
     "check-circle" lucide/check-circle
     "settings" lucide/settings
     "user" lucide/user
     "flag" lucide/flag
     "check" lucide/check
     "x" lucide/x})

  ;; Initial render
  (render!)

  ;; Set up state watching for re-renders
  (add-watch app-state :render
             (fn [_ _ _ _]
               (render!))))
