(ns ty.site.views.contact-form
  (:require
    [clojure.string :as str]
    [ty.site.state :as state]))

;; Validation functions
(defn validate-email [email]
  (let [email-regex #"^[^\s@]+@[^\s@]+\.[^\s@]+$"]
    (and (not-empty email) (re-matches email-regex email))))

(defn validate-field [field-key value]
  (case field-key
    :full-name (if (or (empty? value) (< (count (clojure.string/trim value)) 2))
                 "Full name must be at least 2 characters long"
                 nil)
    :email (cond
             (empty? value) "Email address is required"
             (not (validate-email value)) "Please enter a valid email address"
             :else nil)
    :company (if (empty? value)
               "Company name is required"
               nil)
    :subject (if (or (empty? value) (< (count (clojure.string/trim value)) 5))
               "Subject must be at least 5 characters long"
               nil)
    :message (cond
               (empty? value) "Message is required"
               (< (count (clojure.string/trim value)) 20) "Message must be at least 20 characters long"
               (> (count value) 2000) "Message must not exceed 2000 characters"
               :else nil)
    nil))

(defn validate-form [form-data]
  (->> form-data
       (map (fn [[k v]] [k (validate-field k v)]))
       (filter (fn [[_ v]] v))
       (into {})))

;; Event handlers
(defn handle-field-change [field-key]
  (fn [event]
    (let [value (-> event .-detail .-value)]
      (swap! state/state update-in [:contact-form :form-data] assoc field-key value)
      (swap! state/state update-in [:contact-form :touched-fields] conj field-key)
      ;; Real-time validation
      (let [error (validate-field field-key value)]
        (if error
          (swap! state/state assoc-in [:contact-form :validation-errors field-key] error)
          (swap! state/state update-in [:contact-form :validation-errors] dissoc field-key))))))

(defn handle-checkbox-change [field-key]
  (fn [event]
    (let [checked (-> event .-detail .-value)]
      (swap! state/state assoc-in [:contact-form :form-data field-key] checked))))

(defn handle-priority-change [^js event]
  (let [value (-> event .-detail .-option .-value)]
    (swap! state/state assoc-in [:contact-form :form-data :priority] value)))

(defn handle-department-change [departments]
  (swap! state/state assoc-in [:contact-form :form-data :department] departments))

;; Form submission
(defn simulate-form-submission [_]
  (js/Promise.
    (fn [resolve reject]
      (js/setTimeout
        (fn []
        ;; Simulate 90% success rate
          (if (> (js/Math.random) 0.1)
            (resolve {:success true
                      :message "Thank you! Your message has been sent successfully. We'll get back to you within 24 hours."})
            (reject {:success false
                     :message "Sorry, there was an error sending your message. Please try again or contact us directly at support@example.com."})))
        2000))))

(defn handle-form-submit [event]
  (.preventDefault event)
  (let [form-data (get-in @state/state [:contact-form :form-data])
        errors (validate-form form-data)]

    ;; Mark all fields as touched
    (swap! state/state assoc-in [:contact-form :touched-fields]
           #{:full-name :email :company :subject :message})

    (if (empty? errors)
      (do
        ;; Start submission
        (swap! state/state assoc-in [:contact-form :is-submitting] true)
        (swap! state/state assoc-in [:contact-form :submission-status] nil)

        ;; Simulate API call
        (-> (simulate-form-submission form-data)
            (.then (fn [result]
                     (swap! state/state assoc-in [:contact-form :is-submitting] false)
                     (swap! state/state assoc-in [:contact-form :submission-status] :success)
                     (swap! state/state assoc-in [:contact-form :submission-message] (:message result))
                     ;; Clear form on success
                     (swap! state/state assoc-in [:contact-form :form-data]
                            {:full-name ""
                             :email ""
                             :company ""
                             :subject ""
                             :message ""
                             :priority "medium"
                             :department #{}
                             :newsletter-consent false})
                     (swap! state/state assoc-in [:contact-form :touched-fields] #{})
                     (swap! state/state assoc-in [:contact-form :validation-errors] {})))
            (.catch (fn [error]
                      (swap! state/state assoc-in [:contact-form :is-submitting] false)
                      (swap! state/state assoc-in [:contact-form :submission-status] :error)
                      (swap! state/state assoc-in [:contact-form :submission-message] (:message error))))))
      ;; Set validation errors
      (swap! state/state assoc-in [:contact-form :validation-errors] errors))))

(defn reset-form []
  (swap! state/state assoc :contact-form
         {:form-data {:full-name ""
                      :email ""
                      :company ""
                      :subject ""
                      :message ""
                      :priority ""
                      :department #{}
                      :newsletter-consent false}
          :validation-errors {}
          :touched-fields #{}
          :is-submitting false
          :submission-status nil
          :submission-message ""}))

(defn view []
  (let [{:keys [form-data validation-errors touched-fields is-submitting submission-status submission-message]}
        (:contact-form @state/state)]
    [:div.max-w-4xl.mx-auto.space-y-8
     ;; Header
     [:div.text-center.mb-12
      [:h1.text-4xl.font-bold.ty-text.mb-4 "Contact Form Scenario"]
      [:p.text-lg.ty-text-.max-w-3xl.mx-auto.leading-relaxed
       "Experience professional form validation with real-time feedback, elegant error states, and comprehensive user experience patterns. This contact form demonstrates advanced form handling with loading states, success feedback, and graceful error recovery."]

      [:div.flex.flex-wrap.gap-3.justify-center.mt-6
       [:span.px-3.py-1.ty-bg-success-.ty-text-success.rounded-full.text-sm.font-medium "Real-time Validation"]
       [:span.px-3.py-1.ty-bg-primary-.ty-text-primary.rounded-full.text-sm.font-medium "Professional Styling"]
       [:span.px-3.py-1.ty-bg-warning-.ty-text-warning.rounded-full.text-sm.font-medium "Loading States"]
       [:span.px-3.py-1.ty-bg-info-.ty-text-info.rounded-full.text-sm.font-medium "Success Feedback"]]]

     ;; Submission Status Messages
     (when submission-status
       [:div.ty-elevated.p-6.rounded-lg
        (if (= submission-status :success)
          [:div.flex.items-center.gap-4.ty-bg-success-.p-4.rounded-lg
           [:ty-icon {:name "check-circle"
                      :size "lg"}]
           [:div
            [:h3.font-semibold.ty-text-success "Message Sent Successfully!"]
            [:p.text-sm.ty-text-success submission-message]]]
          [:div.flex.items-center.gap-4.ty-bg-danger-.p-4.rounded-lg
           [:ty-icon {:name "alert-circle"
                      :size "lg"}]
           [:div
            [:h3.font-semibold.ty-text-danger "Submission Failed"]
            [:p.text-sm.ty-text-danger submission-message]
            [:button.mt-2.text-sm.underline.ty-text-danger.hover:no-underline
             {:on {:click (fn [] (swap! state/state assoc-in [:contact-form :submission-status] nil))}}
             "Try Again"]]])])

     ;; Main Form
     [:div.ty-elevated.p-8.rounded-xl
      [:div.flex.items-center.justify-between.mb-8
       [:div.flex.items-center.gap-4
        [:ty-icon {:name "mail"
                   :size "xl"}]
        [:div
         [:h2.text-2xl.font-semibold.ty-text "Get in Touch"]
         [:p.ty-text- "We'd love to hear from you. Send us a message and we'll respond as soon as possible."]]]]

      [:form.space-y-6
       {:on {:submit handle-form-submit}}

       ;; Two-column layout: Form fields on left, Message on right
       [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8
        ;; Left Column - Form Fields
        [:div.space-y-6
         ;; Name and Email Row
         [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
          ;; Full Name
          [:div
           [:ty-input {:type "text"
                       :label "Full Name"
                       :value (:full-name form-data)
                       :placeholder "Enter your full name"
                       :required true
                       :icon "user"
                       :on {:input (handle-field-change :full-name)}}]
           (when (and (contains? touched-fields :full-name)
                      (contains? validation-errors :full-name))
             [:p.text-sm.ty-text-danger.mt-1.flex.items-center.gap-2
              [:ty-icon {:name "alert-circle"
                         :size "xs"}]
              (get validation-errors :full-name)])]

          ;; Email
          [:div
           [:ty-input {:type "email"
                       :label "Email Address"
                       :value (:email form-data)
                       :placeholder "your@email.com"
                       :required true
                       :icon "mail"
                       :on {:input (handle-field-change :email)}}]
           (when (and (contains? touched-fields :email)
                      (contains? validation-errors :email))
             [:p.text-sm.ty-text-danger.mt-1.flex.items-center.gap-2
              [:ty-icon {:name "alert-circle"
                         :size "xs"}]
              (get validation-errors :email)])]]

         ;; Company
         [:div
          [:ty-input {:type "text"
                      :label "Company"
                      :value (:company form-data)
                      :placeholder "Your company name"
                      :required true
                      :icon "building"
                      :on {:input (handle-field-change :company)}}]
          (when (and (contains? touched-fields :company)
                     (contains? validation-errors :company))
            [:p.text-sm.ty-text-danger.mt-1.flex.items-center.gap-2
             [:ty-icon {:name "alert-circle"
                        :size "xs"}]
             (get validation-errors :company)])]

         ;; Subject
         [:div
          [:ty-input {:type "text"
                      :label "Subject"
                      :value (:subject form-data)
                      :placeholder "Brief description of your inquiry"
                      :required true
                      :icon "file-text"
                      :on {:input (handle-field-change :subject)}}]
          (when (and (contains? touched-fields :subject)
                     (contains? validation-errors :subject))
            [:p.text-sm.ty-text-danger.mt-1.flex.items-center.gap-2
             [:ty-icon {:name "alert-circle"
                        :size "xs"}]
             (get validation-errors :subject)])]

         ;; Priority and Department Row
         [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
          ;; Priority Selection
          [:div
           [:ty-dropdown
            {:label "Priority Level"
             :value (:priority form-data)
             :on {:change handle-priority-change}}
            [:ty-option {:value "low"}
             [:div.flex.gap-2.items-center
              [:ty-icon {:name "circle"
                         :size "xs"
                         :slot "start"}]
              "Low - General inquiry"]]
            [:ty-option {:value "medium"}
             [:div.flex.gap-2.items-center
              [:ty-icon {:name "minus-circle"
                         :size "xs"
                         :slot "start"}]
              "Medium - Standard request"]]
            [:ty-option {:value "high"}
             [:div.flex.gap-2.items-center
              [:ty-icon {:name "alert-triangle"
                         :size "xs"
                         :slot "start"}]
              "High - Urgent issue"]]
            [:ty-option {:value "critical"}
             [:div.flex.gap-2.items-center
              [:ty-icon {:name "alert-circle"
                         :size "xs"
                         :slot "start"}]
              "Critical - Immediate attention needed"]]]]

          ;; Department Routing (using multiselect)
          [:div
           [:label.block.text-sm.font-medium.ty-text.mb-2 "Department(s)"]
           [:ty-multiselect
            {:placeholder "Select relevant departments..."
             :value (str/join "," (:department form-data))
             :on {:change (fn [event]
                            (let [values (-> event .-detail .-values)]
                              (handle-department-change (set values))))}}
            [:ty-tag {:value "sales"
                      :flavor "primary"}
             [:ty-icon {:name "briefcase"
                        :size "xs"
                        :slot "start"}]
             "Sales"]
            [:ty-tag {:value "support"
                      :flavor "success"}
             [:ty-icon {:name "life-buoy"
                        :size "xs"
                        :slot "start"}]
             "Support"]
            [:ty-tag {:value "technical"
                      :flavor "secondary"}
             [:ty-icon {:name "settings"
                        :size "xs"
                        :slot "start"}]
             "Technical"]
            [:ty-tag {:value "billing"
                      :flavor "warning"}
             [:ty-icon {:name "credit-card"
                        :size "xs"
                        :slot "start"}]
             "Billing"]
            [:ty-tag {:value "partnership"
                      :flavor "danger"}
             [:ty-icon {:name "handshake"
                        :size "xs"
                        :slot "start"}]
             "Partnership"]]]]]

        ;; Right Column - Message
        [:div
         [:ty-textarea {:label "Message"
                        :value (:message form-data)
                        :placeholder "Please describe your inquiry in detail..."
                        :min-height "200px"
                        :max-height "400px"
                        :required true
                        :on {:change (handle-field-change :message)}}]
         [:div.flex.justify-between.items-center.mt-1
          (when (and (contains? touched-fields :message)
                     (contains? validation-errors :message))
            [:p.text-sm.ty-text-danger.flex.items-center.gap-2
             [:ty-icon {:name "alert-circle"
                        :size "xs"}]
             (get validation-errors :message)])
          [:span.text-xs.ty-text-
           {:class (when (> (count (:message form-data)) 1800) "ty-text-warning")}
           (str (count (:message form-data)) "/2000 characters")]]]]

;; Newsletter consent
       [:div.p-4.ty-bg-neutral-.rounded-lg
        [:ty-input {:type "checkbox"
                    :label "I would like to receive updates and marketing communications about your products and services."
                    :checked (:newsletter-consent form-data)
                    :on {:change (handle-checkbox-change :newsletter-consent)}}]]

       ;; Submit Section
       [:div.border-t.ty-border.pt-6.flex.flex-col.sm:flex-row.gap-4.justify-between.items-center
        [:div.text-sm.ty-text-.text-center.sm:text-left
         [:p "By submitting this form, you agree to our"]
         [:a.ty-text-primary.underline.hover:no-underline.ml-1
          {:href "#"
           :on {:click (fn [e] (.preventDefault e))}} "Terms of Service"]
         [:span " and "]
         [:a.ty-text-primary.underline.hover:no-underline
          {:href "#"
           :on {:click (fn [e] (.preventDefault e))}} "Privacy Policy"]]

        [:div.flex.gap-3
         [:ty-button
          {:type "button"
           :flavor "secondary"
           :on {:click reset-form}}
          [:ty-icon {:name "refresh-ccw"
                     :size "sm"}]
          "Reset"]
         [:ty-button
          {:type "submit"
           :flavor "primary"
           :disabled is-submitting
           :class (when is-submitting "opacity-75 cursor-not-allowed")}
          (if is-submitting
            [:div.flex.items-center.gap-2
             [:ty-icon {:name "loader-2"
                        :size "sm"}]
             "Sending..."]
            [:div.flex.items-center.gap-2
             [:ty-icon {:name "send"
                        :size "sm"}]
             "Send Message"])]]]]

     ;; Additional Information
      [:div.ty-bg-neutral-.p-6.rounded-lg
       [:h3.text-lg.font-semibold.ty-text.mb-4 "Other Ways to Reach Us"]
       [:div.grid.grid-cols-1.md:grid-cols-3.gap-6
        [:div.flex.items-center.gap-3
         [:ty-icon {:name "mail"
                    :size "sm"}]
         [:div
          [:p.font-medium.ty-text "Email"]
          [:p.text-sm.ty-text- "support@example.com"]]]
        [:div.flex.items-center.gap-3
         [:ty-icon {:name "phone"
                    :size "sm"}]
         [:div
          [:p.font-medium.ty-text "Phone"]
          [:p.text-sm.ty-text- "+1 (555) 123-4567"]]]
        [:div.flex.items-center.gap-3
         [:ty-icon {:name "map-pin"
                    :size "sm"}]
         [:div
          [:p.font-medium.ty-text "Address"]
          [:p.text-sm.ty-text- "123 Business Ave, Suite 100"]]]]

       [:div.mt-6.p-4.ty-bg-info-.rounded-lg
        [:div.flex.items-start.gap-3
         [:ty-icon {:name "info"
                    :size "sm"}]
         [:div.text-sm
          [:p.ty-text-info.font-medium "Response Time"]
          [:p.ty-text-info "We typically respond to all inquiries within 24 hours during business days. For urgent technical issues, please call us directly."]]]]]]]))

