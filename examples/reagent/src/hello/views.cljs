(ns hello.views
  (:require [hello.state :as state]
            [reagent.core :as r]))

(defn home-view []
  [:div.max-w-4xl.mx-auto
   [:header.ty-elevated.p-6.rounded-lg.mb-8
    [:h1.text-3xl.font-bold.ty-text++.mb-2
     "Welcome to Reagent + Ty Demo 🎯"]
    [:p.ty-text.text-lg
     "This demo showcases Reagent integration with Ty Web Components."]]

   [:div.grid.md:grid-cols-2.gap-6
    [:section.ty-elevated.p-6.rounded-lg
     [:h2.text-xl.font-semibold.mb-4.ty-text++
      "Navigation Features"]
     [:ul.space-y-2.ty-text
      [:li "• Responsive sidebar navigation"]
      [:li "• Mobile overlay menu"]
      [:li "• Theme toggle (light/dark)"]
      [:li "• URL routing with browser history"]]]

    [:section.ty-elevated.p-6.rounded-lg
     [:h2.text-xl.font-semibold.mb-4.ty-text++
      "Styling System"]
     [:ul.space-y-2.ty-text
      [:li "• Ty classes for colors and surfaces"]
      [:li "• Tailwind for layout and spacing"]
      [:li "• Responsive design patterns"]
      [:li "• Semantic color variants"]]]]])

;; Form validation functions
(defn validate-field [field value]
  (case field
    :name (cond
            (empty? (str value)) "Name is required"
            (< (count (str value)) 2) "Name must be at least 2 characters"
            :else nil)
    :email (cond
             (empty? (str value)) "Email is required"
             (not (re-matches #"^[^\s@]+@[^\s@]+\.[^\s@]+$" (str value))) "Please enter a valid email address"
             :else nil)
    :phone (cond
             (and (not (empty? (str value)))
                  (not (re-matches #"^[\d\s\-\+\(\)]{10,}$" (str value)))) "Please enter a valid phone number"
             :else nil)
    :bio (cond
           (> (count (str value)) 500) "Bio must be less than 500 characters"
           :else nil)
    :birthdate (cond
                 (empty? (str value)) "Birth date is required"
                 :else nil)
    nil))

(defn forms-view []
  (let [form-data (:form @state/app-state)
        errors (:form-errors @state/app-state)
        is-submitting (:form-submitting @state/app-state)]

    [:div.max-w-4xl.mx-auto.space-y-8
     ;; Header
     [:div.ty-elevated.p-6.rounded-lg
      [:div.flex.items-center.justify-between.mb-4
       [:div
        [:h1.text-2xl.font-bold.ty-text++ "User Profile Form"]
        [:p.ty-text-.text-sm.mt-1 "Complete form with validation, date picker, and multi-selection"]]
       [:div.flex.items-center.gap-2
        [:ty-icon {:name "user"
                   :size "lg"
                   :class "ty-text-primary"}]]]

      ;; Progress indicator
      (let [completed-fields (->> form-data
                                  (filter (fn [[k v]] (and v (not (empty? (str v))))))
                                  count)
            total-fields (count form-data)
            progress (* (/ completed-fields total-fields) 100)]
        [:div
         [:div.flex.justify-between.text-sm.mb-2
          [:span.ty-text+ "Form Progress"]
          [:span.ty-text+ (str completed-fields "/" total-fields " fields")]]
         [:div.w-full.bg-gray-200.rounded-full.h-2
          [:div.ty-bg-primary.h-2.rounded-full.transition-all.duration-300
           {:style {:width (str progress "%")}}]]])]

     ;; Form sections
     [:div.grid.lg:grid-cols-2.gap-8
      ;; Personal Information
      [:section.ty-elevated.p-6.rounded-lg
       [:h2.text-lg.font-semibold.mb-6.ty-text++.flex.items-center.gap-2
        [:ty-icon {:name "user"
                   :size "sm"}]
        "Personal Information"]

       [:div.space-y-5
        ;; Name field
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Full Name *"]
         [:ty-input {:type "text"
                     :placeholder "Enter your full name"
                     :class (str "w-full " (when (:name errors) "error"))
                     :value (:name form-data)
                     :on-input #(do
                                  (swap! state/app-state assoc-in [:form :name] (.. % -target -value))
                                  (let [error (validate-field :name (.. % -target -value))]
                                    (swap! state/app-state assoc-in [:form-errors :name] error)))}]
         (when (:name errors)
           [:p.text-xs.ty-text-danger.mt-1.flex.items-center.gap-1
            [:ty-icon {:name "alert-circle"
                       :size "xs"}]
            (:name errors)])]

        ;; Email field  
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Email Address *"]
         [:ty-input {:type "email"
                     :placeholder "you@company.com"
                     :class (str "w-full " (when (:email errors) "error"))
                     :value (:email form-data)
                     :on-input #(do
                                  (swap! state/app-state assoc-in [:form :email] (.. % -target -value))
                                  (let [error (validate-field :email (.. % -target -value))]
                                    (swap! state/app-state assoc-in [:form-errors :email] error)))}]
         (when (:email errors)
           [:p.text-xs.ty-text-danger.mt-1.flex.items-center.gap-1
            [:ty-icon {:name "alert-circle"
                       :size "xs"}]
            (:email errors)])]

        ;; Phone field
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Phone Number"]
         [:ty-input {:type "tel"
                     :placeholder "+1 (555) 123-4567"
                     :class (str "w-full " (when (:phone errors) "error"))
                     :value (:phone form-data)
                     :on-input #(do
                                  (swap! state/app-state assoc-in [:form :phone] (.. ^js % -target -value))
                                  (let [error (validate-field :phone (.. ^js % -target -value))]
                                    (swap! state/app-state assoc-in [:form-errors :phone] error)))}]
         (when (:phone errors)
           [:p.text-xs.ty-text-danger.mt-1.flex.items-center.gap-1
            [:ty-icon {:name "alert-circle"
                       :size "xs"}]
            (:phone errors)])]

        ;; Birth Date
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Birth Date *"]
         [:ty-date-picker
          {:ref (fn [el]
                  (when el
                    (.addEventListener
                      el "change"
                      (fn [^js event]
                        (let [value (.. event -detail -value)
                              error (validate-field :birthdate value)]
                          (swap! state/app-state
                                 (fn [state]
                                   (-> state
                                       (assoc-in [:form-errors :birthdate] error)
                                       (assoc-in [:form :birthdate] value)))))))))
           :class (str "w-full " (when (:birthdate errors) "error"))
           :placeholder "Select your birth date"
           :value (:birthdate form-data)}]
         (when (:birthdate errors)
           [:p.text-xs.ty-text-danger.mt-1.flex.items-center.gap-1
            [:ty-icon {:name "alert-circle"
                       :size "xs"}]
            (:birthdate errors)])]]]

      ;; Preferences & Bio
      [:section.ty-elevated.p-6.rounded-lg
       [:h2.text-lg.font-semibold.mb-6.ty-text++.flex.items-center.gap-2
        [:ty-icon {:name "settings"
                   :size "sm"}]
        "Preferences & Bio"]

       [:div.space-y-5
        ;; Role dropdown
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Professional Role"]
         [:ty-dropdown {:class "w-full"
                        :placeholder "Select your role"
                        :on-change #(swap! state/app-state assoc-in [:form :role] (.. ^js % -detail -option -value))}
          [:ty-option {:value "developer"}
           [:div.flex.items-center.gap-2
            [:ty-icon {:name "code"
                       :size "xs"}]
            "Software Developer"]]
          [:ty-option {:value "designer"}
           [:div.flex.items-center.gap-2
            [:ty-icon {:name "palette"
                       :size "xs"}]
            "UI/UX Designer"]]
          [:ty-option {:value "manager"}
           [:div.flex.items-center.gap-2
            [:ty-icon {:name "users"
                       :size "xs"}]
            "Project Manager"]]
          [:ty-option {:value "analyst"}
           [:div.flex.items-center.gap-2
            [:ty-icon {:name "bar-chart"
                       :size "xs"}]
            "Data Analyst"]]
          [:ty-option {:value "other"}
           [:div.flex.items-center.gap-2
            [:ty-icon {:name "briefcase"
                       :size "xs"}]
            "Other"]]]]

        ;; Skills multiselect
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Technical Skills"]
         [:ty-multiselect {:class "w-full"
                           :placeholder "Select your skills"
                           :on-change #(swap! state/app-state assoc-in [:form :skills] (.. % -detail -values))}
          [:ty-tag {:value "javascript"
                    :flavor "primary"} "JavaScript"]
          [:ty-tag {:value "typescript"
                    :flavor "primary"} "TypeScript"]
          [:ty-tag {:value "react"
                    :flavor "neutral"} "React"]
          [:ty-tag {:value "vue"
                    :flavor "neutral"} "Vue.js"]
          [:ty-tag {:value "angular"
                    :flavor "neutral"} "Angular"]
          [:ty-tag {:value "clojure"
                    :flavor "success"} "Clojure"]
          [:ty-tag {:value "python"
                    :flavor "warning"} "Python"]
          [:ty-tag {:value "java"
                    :flavor "warning"} "Java"]
          [:ty-tag {:value "css"
                    :flavor "secondary"} "CSS"]
          [:ty-tag {:value "sass"
                    :flavor "secondary"} "Sass"]
          [:ty-tag {:value "tailwind"
                    :flavor "secondary"} "Tailwind CSS"]
          [:ty-tag {:value "node"
                    :flavor "neutral"} "Node.js"]
          [:ty-tag {:value "docker"
                    :flavor "neutral"} "Docker"]
          [:ty-tag {:value "aws"
                    :flavor "danger"} "AWS"]]]

        ;; Bio textarea
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Biography"]
         [:ty-input {:type "textarea"
                     :rows "4"
                     :placeholder "Tell us about yourself, your experience, and interests..."
                     :class (str "w-full resize-none " (when (:bio errors) "error"))
                     :value (:bio form-data)
                     :on-input #(do
                                  (swap! state/app-state assoc-in [:form :bio] (.. % -target -value))
                                  (let [error (validate-field :bio (.. % -target -value))]
                                    (swap! state/app-state assoc-in [:form-errors :bio] error)))}]
         [:div.flex.justify-between.mt-1
          (when (:bio errors)
            [:p.text-xs.ty-text-danger.flex.items-center.gap-1
             [:ty-icon {:name "alert-circle"
                        :size "xs"}]
             (:bio errors)])
          [:p.text-xs.ty-text-.ml-auto
           (str (count (str (:bio form-data))) "/500 characters")]]]

        ;; Newsletter checkbox
        [:div.flex.items-center.gap-3
         [:input {:type "checkbox"
                  :id "newsletter"
                  :class "w-4 h-4 ty-text-primary focus:ring-primary-500 border-gray-300 rounded"
                  :checked (:newsletter form-data)
                  :on-change #(swap! state/app-state assoc-in [:form :newsletter] (.. % -target -checked))}]
         [:label {:for "newsletter"
                  :class "text-sm ty-text+"}
          "Subscribe to newsletter and product updates"]]]]]

     ;; Form Actions
     [:div.ty-elevated.p-6.rounded-lg
      [:div.flex.flex-col.sm:flex-row.gap-4.justify-between.items-start.sm:items-center
       [:div
        [:p.text-sm.ty-text+ "Review your information before submitting"]
        [:p.text-xs.ty-text-.mt-1 "All fields marked with * are required"]]

       [:div.flex.gap-3.w-full.sm:w-auto
        [:ty-button {:flavor "secondary"
                     :class "flex-1 sm:flex-none"
                     :on-click #(do
                                  (swap! state/app-state assoc :form {:name ""
                                                                      :email ""
                                                                      :phone ""
                                                                      :role ""
                                                                      :skills []
                                                                      :bio ""
                                                                      :birthdate ""
                                                                      :newsletter false})
                                  (swap! state/app-state assoc :form-errors {}))}
         [:ty-icon {:name "refresh-cw"
                    :size "xs"}]
         "Reset Form"]

        [:ty-button
         (cond->
           {:flavor "primary"
            :class "flex-1 sm:flex-none"
            :type "submit"
            :on-click #(do
                         (swap! state/app-state assoc :form-submitting true)
                         (js/setTimeout
                           (fn []
                             (swap! state/app-state assoc :form-submitting false)
                             (js/alert (str "Form submitted! Data: " (pr-str form-data))))
                           2000))}
           is-submitting (assoc :disabled true))
         (if is-submitting
           [:<>
            [:ty-icon {:name "loader"
                       :size "xs"
                       :class "animate-spin"}]
            "Submitting..."]
           [:<>
            [:ty-icon {:name "send"
                       :size "xs"}]
            "Submit Form"])]]]

     ;; Form Debug Info
      (when js/goog.DEBUG
        [:div.ty-elevated.p-4.rounded-lg
         [:h3.font-medium.mb-2.ty-text+ "Debug Info (Development Only)"]
         [:div.grid.md:grid-cols-2.gap-4.text-xs
          [:div
           [:h4.font-medium.mb-1.ty-text+ "Form Data:"]
           [:pre.ty-text-.overflow-auto.bg-gray-50.p-2.rounded.whitespace-pre-wrap.break-words
            (pr-str form-data)]]
          [:div
           [:h4.font-medium.mb-1.ty-text+ "Validation Errors:"]
           [:pre.ty-text-.overflow-auto.bg-gray-50.p-2.rounded.whitespace-pre-wrap.break-words
            (pr-str errors)]]]])]]))

(defn buttons-view []
  [:div.max-w-4xl.mx-auto
   [:section.ty-elevated.p-6.rounded-lg.mb-6
    [:h2.text-xl.font-semibold.mb-4.ty-text++
     "Button Examples"]

    [:div.space-y-6
     [:div
      [:h3.font-medium.mb-3.ty-text+ "Basic Buttons"]
      [:div.flex.gap-4.flex-wrap
       [:ty-button {:on-click #(js/alert "Default clicked!")}
        "Default"]
       [:ty-button {:flavor "primary"
                    :on-click #(js/alert "Primary clicked!")}
        "Primary"]
       [:ty-button {:flavor "secondary"
                    :on-click #(js/alert "Secondary clicked!")}
        "Secondary"]]]

     [:div
      [:h3.font-medium.mb-3.ty-text+ "Semantic Buttons"]
      [:div.flex.gap-4.flex-wrap
       [:ty-button {:flavor "success"
                    :on-click #(js/alert "Success clicked!")}
        "Success"]
       [:ty-button {:flavor "warning"
                    :on-click #(js/alert "Warning clicked!")}
        "Warning"]
       [:ty-button {:flavor "danger"
                    :on-click #(js/alert "Danger clicked!")}
        "Danger"]]]

     [:div
      [:h3.font-medium.mb-3.ty-text+ "Buttons with Icons"]
      [:div.flex.gap-4.flex-wrap
       [:ty-button {:flavor "primary"}
        [:ty-icon {:name "save"
                   :size "sm"}]
        "Save"]
       [:ty-button {:flavor "danger"}
        [:ty-icon {:name "trash"
                   :size "sm"}]
        "Delete"]
       [:ty-button {:flavor "secondary"}
        [:ty-icon {:name "download"
                   :size "sm"}]
        "Download"]]]]]])

(defn components-view []
  [:div.max-w-4xl.mx-auto
   [:section.ty-elevated.p-6.rounded-lg.mb-6
    [:h2.text-xl.font-semibold.mb-4.ty-text++
     "Available Components"]

    [:div.grid.md:grid-cols-2.lg:grid-cols-3.gap-4
     (for [component ["ty-button" "ty-input" "ty-dropdown" "ty-option"
                      "ty-multiselect" "ty-tag" "ty-modal" "ty-icon"
                      "ty-tooltip" "ty-popup" "ty-calendar" "ty-date-picker"]]
       ^{:key component}
       [:div.ty-elevated.p-4.rounded.text-center
        [:code.text-sm.ty-text component]])]]

   [:section.ty-elevated.p-6.rounded-lg
    [:h2.text-xl.font-semibold.mb-4.ty-text++
     "Implementation Notes"]
    [:div.space-y-4.ty-text
     [:p "Components are manually registered using:"]
     [:pre.bg-gray-50.p-4.rounded.text-sm.overflow-auto
      "(wcs/define! \"ty-button\" button/configuration)"]
     [:p "This approach gives full control over which components are loaded and when."]]]])
