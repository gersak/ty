(ns hello.views.forms
  (:require
    ["@gersak/ty-react" :as ty]
    [uix.core :as uix :refer [defui $ use-state use-effect]]))

(defui field-wrapper [{:keys [label required error class children]}]
  "Wrapper component for form fields with label and error handling"
  ($ :div {:class (str "space-y-2 " (or class ""))}
       ;; Label
     (when label
       ($ :label {:class "ty-text+ block text-sm font-medium"}
          label
          (when required
            ($ :span {:class "ty-text-danger ml-1"} "*"))))

       ;; Children (form controls)
     children

       ;; Error message
     (when error
       ($ :div {:class "ty-text-danger text-sm flex items-center gap-1"}
          ($ ty/Icon {:name "alert-circle"
                      :class "w-4 h-4"})
          error))))

(defui contact-form []
  "Main contact form demonstrating form patterns"
  (let [[form-data set-form-data] (use-state {:name ""
                                              :email ""
                                              :phone ""
                                              :subject ""
                                              :message ""
                                              :priority "medium"
                                              :newsletter false})
        [loading set-loading] (use-state false)
        [errors set-errors] (use-state {})
        [submitted set-submitted] (use-state false)]

    ;; Validation function
    (letfn [(validate-form [data]
              (let [errors (cond-> {}
                             (empty? (:name data))
                             (assoc :name "Name is required")

                             (empty? (:email data))
                             (assoc :email "Email is required")

                             (and (not (empty? (:email data)))
                                  (not (re-matches #".+@.+\..+" (:email data))))
                             (assoc :email "Please enter a valid email")

                             (empty? (:message data))
                             (assoc :message "Message is required")

                             (< (count (:message data)) 10)
                             (assoc :message "Message must be at least 10 characters"))]
                errors))

            (handle-submit [e]
              (.preventDefault e)
              (let [validation-errors (validate-form form-data)]
                (set-errors validation-errors)
                (if (empty? validation-errors)
                  (do
                    (set-loading true)
                               ;; Simulate API call
                    (js/setTimeout
                      #(do
                         (set-loading false)
                         (set-submitted true)
                         (js/setTimeout
                           (fn []
                             (set-submitted false)
                             (set-form-data {:name ""
                                             :email ""
                                             :phone ""
                                             :subject ""
                                             :message ""
                                             :priority "medium"
                                             :newsletter false}))
                           3000))
                      2000)))))]

      ;; Success message
      (when submitted
        ($ :div {:class "ty-bg-success- ty-border-success border rounded-lg p-4 mb-6"}
           ($ :div {:class "flex items-center gap-2"}
              ($ ty/Icon {:name "check"
                          :class "ty-text-success++"})
              ($ :h3 {:class "ty-text-success++ font-semibold"}
                 "Message Sent Successfully!"))
           ($ :p {:class "ty-text-success text-sm mt-1"}
              "Thank you for your message. We'll get back to you soon.")))

      ;; Form
      ($ :form {:class "ty-elevated p-6 rounded-lg max-w-2xl mx-auto space-y-6"
                :on-submit handle-submit}

         ;; Form header
         ($ :div {:class "text-center mb-6"}
            ($ :h2 {:class "ty-text++ text-2xl font-bold mb-2"}
               "Contact Us")
            ($ :p {:class "ty-text- text-sm"}
               "Fill out the form below and we'll get back to you as soon as possible."))

         ;; Personal information section
         ($ :div {:class "space-y-4"}
            ($ :h3 {:class "ty-text+ text-lg font-semibold border-b ty-border pb-2"}
               "Personal Information")

            ;; Name and Email row
            ($ :div {:class "grid grid-cols-1 md:grid-cols-2 gap-4"}

               ;; Name field
               ($ field-wrapper {:label "Full Name"
                                 :required true
                                 :error (:name errors)}
                  ($ ty/Input
                     {:type "text"
                      :value (:name form-data)
                      :on-change #(set-form-data assoc :name (.. % -target -value))
                      :placeholder "Enter your full name"
                      :class (str "w-full " (when (:name errors) "ty-border-danger"))}))

               ;; Email field
               ($ field-wrapper {:label "Email Address"
                                 :required true
                                 :error (:email errors)}
                  ($ ty/Input
                     {:type "email"
                      :value (:email form-data)
                      :on-change #(set-form-data assoc :email (.. % -target -value))
                      :placeholder "your@email.com"
                      :class (str "w-full " (when (:email errors) "ty-border-danger"))})))

            ;; Phone field
            ($ field-wrapper {:label "Phone Number (Optional)"}
               ($ ty/Input
                  {:type "tel"
                   :value (:phone form-data)
                   :on-change #(set-form-data assoc :phone (.. % -target -value))
                   :placeholder "+1 (555) 123-4567"
                   :class "w-full"})))

         ;; Message section
         ($ :div {:class "space-y-4"}
            ($ :h3 {:class "ty-text+ text-lg font-semibold border-b ty-border pb-2"}
               "Your Message")

            ;; Subject and Priority row
            ($ :div {:class "grid grid-cols-1 md:grid-cols-3 gap-4"}

               ;; Subject field
               ($ field-wrapper {:label "Subject"
                                 :class "md:col-span-2"}
                  ($ ty/Input
                     {:type "text"
                      :value (:subject form-data)
                      :on-change #(set-form-data assoc :subject (.. ^js % -target -value))
                      :placeholder "Brief subject of your message"
                      :class "w-full"}))

               ;; Priority dropdown
               ($ field-wrapper {:label "Priority"}
                  ($ ty/Dropdown
                     {:value (:priority form-data)
                      :on-change #(set-form-data assoc :priority (.. ^js % -detail -option -value))
                      :class "w-full"}
                     ($ ty/Option {:value "low"} "Low")
                     ($ ty/Option {:value "medium"} "Medium")
                     ($ ty/Option {:value "high"} "High")
                     ($ ty/Option {:value "urgent"} "Urgent"))))

            ;; Message textarea
            ($ field-wrapper {:label "Message"
                              :required true
                              :error (:message errors)}
               ($ ty/Input
                  {:type "textarea"
                   :rows "5"
                   :value (:message form-data)
                   :on-change #(set-form-data assoc :message (.. % -target -value))
                   :placeholder "Please describe your inquiry in detail..."
                   :class (str "w-full resize-none " (when (:message errors) "ty-border-danger"))}))

            ;; Character count
            ($ :div {:class "flex justify-between text-xs ty-text-"}
               ($ :span (str (count (:message form-data)) " characters"))
               ($ :span "Minimum 10 characters required")))

         ;; Newsletter subscription
         ($ :div {:class "space-y-4"}
            ($ :h3 {:class "ty-text+ text-lg font-semibold border-b ty-border pb-2"}
               "Preferences")

            ($ :label {:class "flex items-center gap-3 cursor-pointer"}
               ($ ty/Input
                  {:type "checkbox"
                   :checked (:newsletter form-data)
                   :on-change #(set-form-data assoc :newsletter (.. % -target -checked))
                   :class "rounded"})
               ($ :span {:class "ty-text text-sm"}
                  "Subscribe to our newsletter for updates and special offers")))

         ;; Form actions
         ($ :div {:class "flex flex-col sm:flex-row gap-3 pt-4 border-t ty-border"}
            ($ ty/Button
               {:type "button"
                :variant "outline"
                :on-click #(do
                             (set-form-data {:name ""
                                             :email ""
                                             :phone ""
                                             :subject ""
                                             :message ""
                                             :priority "medium"
                                             :newsletter false})
                             (set-errors {}))
                :class "flex items-center gap-2 justify-center"}
               ($ ty/Icon {:name "x"})
               "Clear Form")

            ($ ty/Button
               {:type "submit"
                :flavor "primary"
                :loading loading
                :disabled (or loading submitted)
                :class "flex items-center gap-2 justify-center sm:ml-auto"}
               ($ ty/Icon {:name "send"})
               (if loading "Sending..." "Send Message")))))))

(defui form-validation-demo []
  "Demonstration of various form validation patterns"
  (let [[demo-data set-demo-data] (use-state {:username ""
                                              :password ""
                                              :confirm-password ""
                                              :age ""
                                              :website ""})
        [demo-errors set-demo-errors] (use-state {})]

    ;; Real-time validation
    (use-effect
      (fn []
        (let [errors (cond-> {}
                      ;; Username validation
                       (and (not (empty? (:username demo-data)))
                            (< (count (:username demo-data)) 3))
                       (assoc :username "Username must be at least 3 characters")

                      ;; Password validation  
                       (and (not (empty? (:password demo-data)))
                            (< (count (:password demo-data)) 8))
                       (assoc :password "Password must be at least 8 characters")

                      ;; Confirm password validation
                       (and (not (empty? (:confirm-password demo-data)))
                            (not= (:password demo-data) (:confirm-password demo-data)))
                       (assoc :confirm-password "Passwords do not match")

                      ;; Age validation
                       (and (not (empty? (:age demo-data)))
                            (or (js/isNaN (js/parseInt (:age demo-data)))
                                (< (js/parseInt (:age demo-data)) 13)))
                       (assoc :age "Age must be 13 or older")

                      ;; Website validation
                       (and (not (empty? (:website demo-data)))
                            (not (re-matches #"https?://.+" (:website demo-data))))
                       (assoc :website "Please enter a valid URL starting with http:// or https://"))]
          (set-demo-errors errors)))
      [demo-data])

    ($ :div {:class "ty-elevated p-6 rounded-lg max-w-xl mx-auto mt-8"}
       ($ :h3 {:class "ty-text++ text-lg font-bold mb-4"}
          "Live Validation Demo")
       ($ :p {:class "ty-text- text-sm mb-6"}
          "This form demonstrates real-time validation. Errors appear as you type.")

       ($ :div {:class "space-y-4"}
          ;; Username field
          ($ field-wrapper {:label "Username"
                            :error (:username demo-errors)}
             ($ ty/Input
                {:type "text"
                 :value (:username demo-data)
                 :on-change #(set-demo-data assoc :username (.. % -target -value))
                 :placeholder "Enter username (min 3 chars)"
                 :class (str "w-full " (when (:username demo-errors) "ty-border-danger"))}))

          ;; Password fields
          ($ :div {:class "grid grid-cols-1 md:grid-cols-2 gap-4"}
             ($ field-wrapper {:label "Password"
                               :error (:password demo-errors)}
                ($ ty/Input
                   {:type "password"
                    :value (:password demo-data)
                    :on-change #(set-demo-data assoc :password (.. % -target -value))
                    :placeholder "Min 8 characters"
                    :class (str "w-full " (when (:password demo-errors) "ty-border-danger"))}))

             ($ field-wrapper {:label "Confirm Password"
                               :error (:confirm-password demo-errors)}
                ($ ty/Input
                   {:type "password"
                    :value (:confirm-password demo-data)
                    :on-change #(set-demo-data assoc :confirm-password (.. % -target -value))
                    :placeholder "Repeat password"
                    :class (str "w-full " (when (:confirm-password demo-errors) "ty-border-danger"))})))

          ;; Age field
          ($ field-wrapper {:label "Age"
                            :error (:age demo-errors)}
             ($ ty/Input
                {:type "number"
                 :value (:age demo-data)
                 :on-change #(set-demo-data assoc :age (.. % -target -value))
                 :placeholder "Must be 13 or older"
                 :class (str "w-full " (when (:age demo-errors) "ty-border-danger"))}))

          ;; Website field
          ($ field-wrapper {:label "Website (Optional)"
                            :error (:website demo-errors)}
             ($ ty/Input
                {:type "url"
                 :value (:website demo-data)
                 :on-change #(set-demo-data assoc :website (.. % -target -value))
                 :placeholder "https://example.com"
                 :class (str "w-full " (when (:website demo-errors) "ty-border-danger"))}))

          ;; Validation status
          ($ :div {:class "p-3 rounded ty-bg-info- border ty-border-info"}
             ($ :div {:class "flex items-center gap-2 mb-2"}
                ($ ty/Icon {:name (if (empty? demo-errors) "check" "alert-circle")
                            :class (if (empty? demo-errors) "ty-text-success++" "ty-text-warning++")})
                ($ :span {:class (str "text-sm font-medium "
                                      (if (empty? demo-errors) "ty-text-success++" "ty-text-warning++"))}
                   (if (empty? demo-errors) "All fields valid!" "Please fix the errors above")))
             ($ :p {:class "ty-text-info text-xs"}
                (str "Fields filled: " (count (filter #(not (empty? %)) (vals demo-data))) "/5")))))))

(defui view []
  "Main forms view with multiple form examples"
  ($ :div {:class "space-y-8"}

     ;; Page header
     ($ :div {:class "text-center"}
        ($ :h1 {:class "ty-text++ text-3xl font-bold mb-2"}
           "Form Examples")
        ($ :p {:class "ty-text- text-lg max-w-2xl mx-auto"}
           "Comprehensive form examples showcasing UIx + ty-react integration with proper TY color classes and Tailwind utilities."))

     ;; Main contact form
     ($ contact-form)

     ;; Validation demo
     ($ form-validation-demo)))
