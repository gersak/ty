(ns hello.views
  (:require ;; React wrappers for proper event handling
    ["@gersak/ty-react" :as ty]
    [hello.state :as state]
    [reagent.core :as r]))

(defn home-view []
  [:div.max-w-4xl.mx-auto
   [:header.ty-elevated.p-6.rounded-lg.mb-8
    [:h1.text-3xl.font-bold.ty-text++.mb-2
     "Welcome to Reagent + Ty Demo 🎯"]
    [:p.ty-text.text-lg
     "This demo showcases Reagent with Ty React wrappers for proper event handling."]]

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
      "React Wrappers"]
     [:ul.space-y-2.ty-text
      [:li "• Full event handling support"]
      [:li "• TypeScript type definitions"]
      [:li "• Reagent-friendly API"]
      [:li "• All 18 components available"]]]]])

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
        [:p.ty-text-.text-sm.mt-1 "Complete form with validation using React wrappers"]]
       [:div.flex.items-center.gap-2
        [:> ty/TyIcon {:name "user"
                       :size "lg"
                       :className "ty-text-primary"}]]]

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
        [:> ty/TyIcon {:name "user"
                       :size "sm"}]
        "Personal Information"]

       [:div.space-y-5
        ;; Name field
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Full Name *"]
         [:> ty/TyInput {:type "text"
                         :placeholder "Enter your full name"
                         :className (str "w-full " (when (:name errors) "error"))
                         :value (:name form-data)
                         :onInput #(do
                                     (swap! state/app-state assoc-in [:form :name] (.. % -detail -value))
                                     (let [error (validate-field :name (.. % -detail -value))]
                                       (swap! state/app-state assoc-in [:form-errors :name] error)))}]
         (when (:name errors)
           [:p.text-xs.ty-text-danger.mt-1.flex.items-center.gap-1
            [:> ty/TyIcon {:name "alert-circle"
                           :size "xs"}]
            (:name errors)])]

        ;; Email field  
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Email Address *"]
         [:> ty/TyInput {:type "email"
                         :placeholder "you@company.com"
                         :className (str "w-full " (when (:email errors) "error"))
                         :value (:email form-data)
                         :onInput #(do
                                     (swap! state/app-state assoc-in [:form :email] (.. % -detail -value))
                                     (let [error (validate-field :email (.. % -detail -value))]
                                       (swap! state/app-state assoc-in [:form-errors :email] error)))}]
         (when (:email errors)
           [:p.text-xs.ty-text-danger.mt-1.flex.items-center.gap-1
            [:> ty/TyIcon {:name "alert-circle"
                           :size "xs"}]
            (:email errors)])]

        ;; Phone field
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Phone Number"]
         [:> ty/TyInput {:type "tel"
                         :placeholder "+1 (555) 123-4567"
                         :className (str "w-full " (when (:phone errors) "error"))
                         :value (:phone form-data)
                         :onInput #(do
                                     (swap! state/app-state assoc-in [:form :phone] (.. % -detail -value))
                                     (let [error (validate-field :phone (.. % -detail -value))]
                                       (swap! state/app-state assoc-in [:form-errors :phone] error)))}]
         (when (:phone errors)
           [:p.text-xs.ty-text-danger.mt-1.flex.items-center.gap-1
            [:> ty/TyIcon {:name "alert-circle"
                           :size "xs"}]
            (:phone errors)])]

        ;; Birth Date
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Birth Date *"]
         [:> ty/TyDatePicker {:className (str "w-full " (when (:birthdate errors) "error"))
                              :placeholder "Select your birth date"
                              :value (:birthdate form-data)
                              :onChange #(let [value (.. % -detail -value)
                                               error (validate-field :birthdate value)]
                                           (swap! state/app-state
                                                  (fn [state]
                                                    (-> state
                                                        (assoc-in [:form-errors :birthdate] error)
                                                        (assoc-in [:form :birthdate] value)))))}]
         (when (:birthdate errors)
           [:p.text-xs.ty-text-danger.mt-1.flex.items-center.gap-1
            [:> ty/TyIcon {:name "alert-circle"
                           :size "xs"}]
            (:birthdate errors)])]]]

      ;; Preferences & Bio
      [:section.ty-elevated.p-6.rounded-lg
       [:h2.text-lg.font-semibold.mb-6.ty-text++.flex.items-center.gap-2
        [:> ty/TyIcon {:name "settings"
                       :size "sm"}]
        "Preferences & Bio"]

       [:div.space-y-5
        ;; Role dropdown
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Professional Role"]
         [:> ty/TyDropdown {:className "w-full"
                            :placeholder "Select your role"
                            :onChange #(swap! state/app-state assoc-in [:form :role] (.. ^js % -detail -option -value))}
          [:> ty/TyOption {:value "developer"}
           [:div.flex.items-center.gap-2
            [:> ty/TyIcon {:name "code"
                           :size "xs"}]
            "Software Developer"]]
          [:> ty/TyOption {:value "designer"}
           [:div.flex.items-center.gap-2
            [:> ty/TyIcon {:name "palette"
                           :size "xs"}]
            "UI/UX Designer"]]
          [:> ty/TyOption {:value "manager"}
           [:div.flex.items-center.gap-2
            [:> ty/TyIcon {:name "users"
                           :size "xs"}]
            "Project Manager"]]
          [:> ty/TyOption {:value "analyst"}
           [:div.flex.items-center.gap-2
            [:> ty/TyIcon {:name "bar-chart"
                           :size "xs"}]
            "Data Analyst"]]
          [:> ty/TyOption {:value "other"}
           [:div.flex.items-center.gap-2
            [:> ty/TyIcon {:name "briefcase"
                           :size "xs"}]
            "Other"]]]]

        ;; Skills multiselect
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Technical Skills"]
         [:> ty/TyMultiselect {:className "w-full"
                               :placeholder "Select your skills"
                               :value (when (seq (:skills form-data))
                                        (clojure.string/join "," (:skills form-data)))
                               :onChange #(let [values (.. ^js % -detail -values)
                                                values-vec (if (array? values)
                                                             (vec values)
                                                             [])]
                                            (swap! state/app-state assoc-in [:form :skills] values-vec))}
          [:> ty/TyTag {:value "javascript"
                        :flavor "primary"} "JavaScript"]
          [:> ty/TyTag {:value "typescript"
                        :flavor "primary"} "TypeScript"]
          [:> ty/TyTag {:value "react"
                        :flavor "neutral"} "React"]
          [:> ty/TyTag {:value "vue"
                        :flavor "neutral"} "Vue.js"]
          [:> ty/TyTag {:value "angular"
                        :flavor "neutral"} "Angular"]
          [:> ty/TyTag {:value "clojure"
                        :flavor "success"} "Clojure"]
          [:> ty/TyTag {:value "python"
                        :flavor "warning"} "Python"]
          [:> ty/TyTag {:value "java"
                        :flavor "warning"} "Java"]
          [:> ty/TyTag {:value "css"
                        :flavor "secondary"} "CSS"]
          [:> ty/TyTag {:value "sass"
                        :flavor "secondary"} "Sass"]
          [:> ty/TyTag {:value "tailwind"
                        :flavor "secondary"} "Tailwind CSS"]
          [:> ty/TyTag {:value "node"
                        :flavor "neutral"} "Node.js"]
          [:> ty/TyTag {:value "docker"
                        :flavor "neutral"} "Docker"]
          [:> ty/TyTag {:value "aws"
                        :flavor "danger"} "AWS"]]
         (when (seq (:skills form-data))
           [:p.text-xs.ty-text+.mt-2.flex.items-center.gap-1
            [:> ty/TyIcon {:name "check"
                           :size "xs"}]
            (str (count (:skills form-data)) " skill" (when (> (count (:skills form-data)) 1) "s") " selected")])]

        ;; Bio textarea - NOW USING TyTextarea!
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Biography"]
         [:> ty/TyTextarea {:rows 4
                            :placeholder "Tell us about yourself, your experience, and interests..."
                            :className (str "w-full resize-none " (when (:bio errors) "error"))
                            :value (:bio form-data)
                            :onInput #(do
                                        (swap! state/app-state assoc-in [:form :bio] (.. % -detail -value))
                                        (let [error (validate-field :bio (.. % -detail -value))]
                                          (swap! state/app-state assoc-in [:form-errors :bio] error)))}]
         [:div.flex.justify-between.mt-1
          (when (:bio errors)
            [:p.text-xs.ty-text-danger.flex.items-center.gap-1
             [:> ty/TyIcon {:name "alert-circle"
                            :size "xs"}]
             (:bio errors)])
          [:p.text-xs.ty-text-.ml-auto
           (str (count (str (:bio form-data))) "/500 characters")]]]

        ;; Newsletter checkbox - NOW USING TyCheckbox!
        [:div.flex.items-center.gap-3
         [:> ty/TyCheckbox {:checked (:newsletter form-data)
                            :onChange #(swap! state/app-state assoc-in [:form :newsletter] (.. % -detail -checked))}
          "Subscribe to newsletter and product updates"]]]]]

     ;; Form Actions
     [:div.ty-elevated.p-6.rounded-lg
      [:div.flex.flex-col.sm:flex-row.gap-4.justify-between.items-start.sm:items-center
       [:div
        [:p.text-sm.ty-text+ "Review your information before submitting"]
        [:p.text-xs.ty-text-.mt-1 "All fields marked with * are required"]]

       [:div.flex.gap-3.w-full.sm:w-auto
        [:> ty/TyButton {:flavor "secondary"
                         :className "flex-1 sm:flex-none"
                         :onClick #(do
                                     (swap! state/app-state assoc :form {:name ""
                                                                         :email ""
                                                                         :phone ""
                                                                         :role ""
                                                                         :skills []
                                                                         :bio ""
                                                                         :birthdate ""
                                                                         :newsletter false})
                                     (swap! state/app-state assoc :form-errors {}))}
         [:> ty/TyIcon {:name "refresh-cw"
                        :size "xs"}]
         "Reset Form"]

        [:> ty/TyButton {:flavor "primary"
                         :className "flex-1 sm:flex-none"
                         :type "submit"
                         :disabled is-submitting
                         :onClick #(do
                                     (swap! state/app-state assoc :form-submitting true)
                                     (js/setTimeout
                                       (fn []
                                         (swap! state/app-state assoc :form-submitting false)
                                         (js/alert (str "Form submitted! Data: " (pr-str form-data))))
                                       2000))}
         (if is-submitting
           [:<>
            [:> ty/TyIcon {:name "loader"
                           :size "xs"
                           :className "animate-spin"}]
            "Submitting..."]
           [:<>
            [:> ty/TyIcon {:name "send"
                           :size "xs"}]
            "Submit Form"])]]]]

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
           (pr-str errors)]]]])]))

(defn buttons-view []
  [:div.max-w-4xl.mx-auto
   [:section.ty-elevated.p-6.rounded-lg.mb-6
    [:h2.text-xl.font-semibold.mb-4.ty-text++
     "Button Examples"]

    [:div.space-y-6
     [:div
      [:h3.font-medium.mb-3.ty-text+ "Basic Buttons"]
      [:div.flex.gap-4.flex-wrap
       [:> ty/TyButton {:onClick #(js/alert "Default clicked!")}
        "Default"]
       [:> ty/TyButton {:flavor "primary"
                        :onClick #(js/alert "Primary clicked!")}
        "Primary"]
       [:> ty/TyButton {:flavor "secondary"
                        :onClick #(js/alert "Secondary clicked!")}
        "Secondary"]]]

     [:div
      [:h3.font-medium.mb-3.ty-text+ "Semantic Buttons"]
      [:div.flex.gap-4.flex-wrap
       [:> ty/TyButton {:flavor "success"
                        :onClick #(js/alert "Success clicked!")}
        "Success"]
       [:> ty/TyButton {:flavor "warning"
                        :onClick #(js/alert "Warning clicked!")}
        "Warning"]
       [:> ty/TyButton {:flavor "danger"
                        :onClick #(js/alert "Danger clicked!")}
        "Danger"]]]

     [:div
      [:h3.font-medium.mb-3.ty-text+ "Buttons with Icons"]
      [:div.flex.gap-4.flex-wrap
       [:> ty/TyButton {:flavor "primary"}
        [:> ty/TyIcon {:name "save"
                       :size "sm"}]
        "Save"]
       [:> ty/TyButton {:flavor "danger"}
        [:> ty/TyIcon {:name "trash"
                       :size "sm"}]
        "Delete"]
       [:> ty/TyButton {:flavor "secondary"}
        [:> ty/TyIcon {:name "download"
                       :size "sm"}]
        "Download"]]]]]])

(defn components-view []
  [:div.max-w-4xl.mx-auto
   [:section.ty-elevated.p-6.rounded-lg.mb-6
    [:h2.text-xl.font-semibold.mb-4.ty-text++
     "Available React Wrapper Components"]

    [:div.grid.md:grid-cols-2.lg:grid-cols-3.gap-4
     (for [component ["TyButton" "TyInput" "TyTextarea" "TyCheckbox"
                      "TyDropdown" "TyOption" "TyMultiselect" "TyTag"
                      "TyModal" "TyIcon" "TyTooltip" "TyPopup"
                      "TyCalendar" "TyCalendarMonth" "TyCalendarNavigation"
                      "TyDatePicker" "TyTabs" "TyTab"]]
       ^{:key component}
       [:div.ty-elevated.p-4.rounded.text-center
        [:code.text-sm.ty-text component]])]]

   [:section.ty-elevated.p-6.rounded-lg
    [:h2.text-xl.font-semibold.mb-4.ty-text++
     "Implementation Notes"]
    [:div.space-y-4.ty-text
     [:p "All components use React wrappers from " [:code "@gersak/ty-react"] " for proper event handling."]
     [:pre.bg-gray-50.p-4.rounded.text-sm.overflow-auto
      "[:> ty/TyButton {:onClick #(js/alert \"Clicked!\")} \"Click Me\"]"]
     [:p "Web components are automatically registered via " [:code "ty.js"] " loaded in index.html."]]]])