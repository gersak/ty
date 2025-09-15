(ns hello.views-react
  "Views using @gersak/ty-react wrappers instead of raw web components"
  (:require ["@gersak/ty-react" :as ty]
            [hello.state :as state]
            [reagent.core :as r]))

(defn home-view []
  [:div.max-w-4xl.mx-auto
   [:header.ty-elevated.p-6.rounded-lg.mb-8
    [:h1.text-3xl.font-bold.ty-text++.mb-2
     "Welcome to Reagent + Ty React Wrappers Demo 🎯"]
    [:p.ty-text.text-lg
     "This demo showcases Reagent integration with Ty React Wrappers - no more manual event listeners!"]]

   [:div.grid.md:grid-cols-2.gap-6
    [:section.ty-elevated.p-6.rounded-lg
     [:h2.text-xl.font-semibold.mb-4.ty-text++
      "React Wrapper Benefits"]
     [:ul.space-y-2.ty-text
      [:li "✅ Clean TypeScript integration"]
      [:li "✅ Automatic custom event handling"]
      [:li "✅ Proper React patterns (useEffect cleanup)"]
      [:li "✅ No memory leaks from manual listeners"]
      [:li "✅ Familiar React component API"]]]

    [:section.ty-elevated.p-6.rounded-lg
     [:h2.text-xl.font-semibold.mb-4.ty-text++
      "Implementation"]
     [:ul.space-y-2.ty-text
      [:li "• Uses @gersak/ty-react package"]
      [:li "• React components wrapped in Reagent"]
      [:li "• Event handlers work seamlessly"]
      [:li "• No :ref callback complexity"]]]]])

;; Form validation functions (same as before)
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
     ;; Header with React wrapper example
     [:div.ty-elevated.p-6.rounded-lg
      [:div.flex.items-center.justify-between.mb-4
       [:div
        [:h1.text-2xl.font-bold.ty-text++ "User Profile Form"]
        [:p.ty-text-.text-sm.mt-1 "Now using @gersak/ty-react wrappers - much cleaner!"]]
       [:div.flex.items-center.gap-2
        ;; Using React wrapper for icon
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
        ;; Name field with React wrapper
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Full Name"]
         [:> ty/TyInput {:type "text"
                         :required true
                         :placeholder "Enter your full name"
                         :className (str "w-full " (when (:name errors) "error"))
                         :value (:name form-data)
                         :onInput (fn [^js event]
                                    (let [value (.. event -detail -value)
                                          error (validate-field :name value)]
                                      (swap! state/app-state
                                             (fn [state]
                                               (-> state
                                                   (assoc-in [:form :name] value)
                                                   (assoc-in [:form-errors :name] error))))))}]
         (when (:name errors)
           [:p.text-xs.ty-text-danger.mt-1.flex.items-center.gap-1
            [:> ty/TyIcon {:name "alert-circle"
                           :size "xs"}]
            (:name errors)])]

        ;; Email field with React wrapper
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Email Address *"]
         [:> ty/TyInput {:type "email"
                         :placeholder "you@company.com"
                         :className (str "w-full " (when (:email errors) "error"))
                         :value (:email form-data)
                         :onInput (fn [^js event]
                                    (let [value (.. event -detail -value)
                                          error (validate-field :email value)]
                                      (swap! state/app-state assoc-in [:form :email] value)
                                      (swap! state/app-state assoc-in [:form-errors :email] error)))}]
         (when (:email errors)
           [:p.text-xs.ty-text-danger.mt-1.flex.items-center.gap-1
            [:> ty/TyIcon {:name "alert-circle"
                           :size "xs"}]
            (:email errors)])]

        ;; Phone field with React wrapper
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Phone Number"]
         [:> ty/TyInput {:type "tel"
                         :placeholder "+1 (555) 123-4567"
                         :className (str "w-full " (when (:phone errors) "error"))
                         :value (:phone form-data)
                         :onInput (fn [^js event]
                                    (let [value (.. event -detail -value)
                                          error (validate-field :phone value)]
                                      (swap! state/app-state assoc-in [:form :phone] value)
                                      (swap! state/app-state assoc-in [:form-errors :phone] error)))}]
         (when (:phone errors)
           [:p.text-xs.ty-text-danger.mt-1.flex.items-center.gap-1
            [:> ty/TyIcon {:name "alert-circle"
                           :size "xs"}]
            (:phone errors)])]

        ;; Birth Date with React wrapper - MUCH CLEANER!
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Birth Date *"]
         [:> ty/TyDatePicker {:className (str "w-full " (when (:birthdate errors) "error"))
                              :placeholder "Select your birth date"
                              :value (:birthdate form-data)
                              :onChange (fn [^js event]
                                          (let [value (.. event -detail -value)
                                                error (validate-field :birthdate value)]
                                            (swap! state/app-state assoc-in [:form :birthdate] value)
                                            (swap! state/app-state assoc-in [:form-errors :birthdate] error)))}]
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
        ;; Role dropdown with React wrapper
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Professional Role"]
         [:> ty/TyDropdown {:value (:role form-data)
                            :className "w-full"
                            :placeholder "Select your role"
                            :onChange (fn [^js event]
                                        (let [value (.. event -detail -option -value)]
                                          (swap! state/app-state assoc-in [:form :role] value)))}
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

        ;; Skills multiselect with React wrapper
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Technical Skills"]
         [:> ty/TyMultiselect {:value (:skills form-data)
                               :className "w-full"
                               :placeholder "Select your skills"
                               :onChange (fn [^js event]
                                           (let [values (js->clj (.. event -detail -values))]
                                             (swap! state/app-state assoc-in [:form :skills] values)))}
          (let [options [{:value "javascript"
                          :flavor "primary"
                          :text "JavaScript"}
                         {:value "typescript"
                          :flavor "primary"
                          :text "TypeScript"}
                         {:value "react"
                          :flavor "neutral"
                          :text "React"}
                         {:value "vue"
                          :flavor "neutral"
                          :text "Vue"}
                         {:value "angular"
                          :flavor "neutral"
                          :text "Angular"}
                         {:value "clojure"
                          :flavor "success"
                          :text "Clojure"}
                         {:value "python"
                          :flavor "warning"
                          :text "Python"}
                         {:value "java"
                          :flavor "warning"
                          :text "Java"}
                         {:value "css"
                          :flavor "secondary"
                          :text "CSS"}
                         {:value "sass"
                          :flavor "secondary"
                          :text "SASS"}
                         {:value "tailwind"
                          :flavor "secondary"
                          :text "Tailwind CSS"}
                         {:value "node"
                          :flavor "neutral"
                          :text "Node.js"}
                         {:value "docker"
                          :flavor "neutral"
                          :text "Docker"}
                         {:value "aws"
                          :flavor "danger"
                          :text "AWS"}]]
            (for [{:keys [text flavor value]} options]
              [:> ty/TyTag
               {:key value
                :value value
                :flavor flavor}
               text]))]]

        ;; Bio textarea with React wrapper
        [:div
         [:label.block.text-sm.font-medium.ty-text+.mb-2
          "Biography"]
         [:> ty/TyInput {:type "textarea"
                         :rows "4"
                         :placeholder "Tell us about yourself, your experience, and interests..."
                         :className (str "w-full resize-none " (when (:bio errors) "error"))
                         :value (:bio form-data)
                         :onInput (fn [^js event]
                                    (let [value (.. event -detail -value)
                                          error (validate-field :bio value)]
                                      (swap! state/app-state assoc-in [:form :bio] value)
                                      (swap! state/app-state assoc-in [:form-errors :bio] error)))}]
         [:div.flex.justify-between.mt-1
          (when (:bio errors)
            [:p.text-xs.ty-text-danger.flex.items-center.gap-1
             [:> ty/TyIcon {:name "alert-circle"
                            :size "xs"}]
             (:bio errors)])
          [:p.text-xs.ty-text-.ml-auto
           (str (count (str (:bio form-data))) "/500 characters")]]]

        ;; Newsletter checkbox (regular HTML)
        [:div.flex.items-center.gap-3
         [:input {:type "checkbox"
                  :id "newsletter"
                  :class "w-4 h-4 ty-text-primary focus:ring-primary-500 border-gray-300 rounded"
                  :checked (:newsletter form-data)
                  :on-change #(swap! state/app-state assoc-in [:form :newsletter] (.. % -target -checked))}]
         [:label {:for "newsletter"
                  :class "text-sm ty-text+"}
          "Subscribe to newsletter and product updates"]]]]

     ;; Form Actions
      [:div.ty-elevated.p-6.rounded-lg
       [:div.flex.flex-col.sm:flex-row.gap-4.justify-between.items-start.sm:items-center
        [:div
         [:p.text-sm.ty-text+ "Review your information before submitting"]
         [:p.text-xs.ty-text-.mt-1 "All fields marked with * are required"]]

        [:div.flex.gap-3.w-full.sm:w-auto
        ;; Reset button with React wrapper
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

        ;; Submit button with React wrapper
         [:> ty/TyButton (cond->
                           {:flavor "primary"
                            :className "flex-1 sm:flex-none"
                            :type "submit"
                            :onClick #(do
                                        (swap! state/app-state assoc :form-submitting true)
                                        (js/setTimeout
                                          (fn []
                                            (swap! state/app-state assoc :form-submitting false)
                                            (js/alert (str "Form submitted! Data: " (pr-str form-data))))
                                          2000))}
                           is-submitting (assoc :disabled true))
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
            (pr-str errors)]]]])]]))

(defn buttons-view []
  [:div.max-w-4xl.mx-auto
   [:section.ty-elevated.p-6.rounded-lg.mb-6
    [:h2.text-xl.font-semibold.mb-4.ty-text++
     "Button Examples (React Wrappers)"]

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
     (for [component ["TyButton" "TyInput" "TyDropdown" "TyOption"
                      "TyMultiselect" "TyTag" "TyModal" "TyIcon"
                      "TyTooltip" "TyPopup" "TyCalendar" "TyDatePicker"]]
       ^{:key component}
       [:div.ty-elevated.p-4.rounded.text-center
        [:code.text-sm.ty-text component]])]]

   [:section.ty-elevated.p-6.rounded-lg
    [:h2.text-xl.font-semibold.mb-4.ty-text++
     "React Wrapper Benefits"]
    [:div.space-y-4.ty-text
     [:p "✅ No manual event listeners or memory leaks"]
     [:p "✅ Proper React patterns with automatic cleanup"]
     [:p "✅ TypeScript integration and IntelliSense"]
     [:p "✅ Event handlers work seamlessly (onChange, onInput, etc.)"]
     [:p "✅ No complex :ref callbacks needed"]
     [:p "💡 Usage: " [:code "[:> ty/TyDatePicker {:onChange handler}]"]]]]])
