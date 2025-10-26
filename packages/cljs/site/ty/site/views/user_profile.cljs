(ns ty.site.views.user-profile
  (:require [clojure.string :as str]
            [ty.site.state :as state]))

;; ============================================================================
;; Validation Functions
;; ============================================================================

(defn validate-email [email]
  (let [email-regex #"^[^\s@]+@[^\s@]+\.[^\s@]+$"]
    (and (not-empty email) (re-matches email-regex email))))

(defn validate-phone [phone]
  (let [phone-regex #"^\+?[\d\s\(\)\-]{10,}$"]
    (and (not-empty phone) (re-matches phone-regex phone))))

(defn validate-field [field-key value]
  (case field-key
    :first-name (if (or (empty? value) (< (count (str/trim value)) 2))
                  "First name must be at least 2 characters"
                  nil)
    :last-name (if (or (empty? value) (< (count (str/trim value)) 2))
                 "Last name must be at least 2 characters"
                 nil)
    :email (cond
             (empty? value) "Email address is required"
             (not (validate-email value)) "Please enter a valid email address"
             :else nil)
    :phone (cond
             (empty? value) "Phone number is required"
             (not (validate-phone value)) "Please enter a valid phone number"
             :else nil)
    :job-title (if (empty? value)
                 "Job title is required"
                 nil)
    :company (if (empty? value)
               "Company name is required"
               nil)
    :bio (cond
           (empty? value) "Professional bio is required"
           (< (count (str/trim value)) 20) "Bio must be at least 20 characters"
           (> (count value) 500) "Bio must not exceed 500 characters"
           :else nil)
    nil))

(defn validate-form [form-data]
  (->> form-data
       (map (fn [[k v]] [k (validate-field k v)]))
       (filter (fn [[_ v]] v))
       (into {})))

;; ============================================================================
;; Event Handlers
;; ============================================================================

(defn handle-field-change [field-key]
  (fn [event]
    (let [value (-> event .-detail .-value)]
      (swap! state/state update-in [:user-profile :form-data] assoc field-key value)
      (swap! state/state update-in [:user-profile :touched-fields] conj field-key)
      ;; Real-time validation
      (let [error (validate-field field-key value)]
        (if error
          (swap! state/state assoc-in [:user-profile :validation-errors field-key] error)
          (swap! state/state update-in [:user-profile :validation-errors] dissoc field-key))))))

(defn handle-form-submit [event]
  (.preventDefault event)
  (let [form-data (get-in @state/state [:user-profile :form-data])
        errors (validate-form form-data)]

    ;; Mark all fields as touched
    (swap! state/state assoc-in [:user-profile :touched-fields]
           #{:first-name :last-name :email :phone :job-title :company :bio})

    (if (empty? errors)
      (do
        ;; Start submission
        (swap! state/state assoc-in [:user-profile :is-submitting] true)

        ;; Simulate API call
        (js/setTimeout
         (fn []
           (swap! state/state assoc-in [:user-profile :is-submitting] false)
           (swap! state/state assoc-in [:user-profile :success-modal-open] true)
           (swap! state/state assoc-in [:user-profile :saved-data] form-data))
         1000))
      ;; Set validation errors
      (swap! state/state assoc-in [:user-profile :validation-errors] errors))))

(defn handle-export-click []
  (let [form-data (get-in @state/state [:user-profile :form-data])]
    (swap! state/state assoc-in [:user-profile :export-modal-open] true)
    (swap! state/state assoc-in [:user-profile :exported-data] form-data)))

(defn handle-cancel []
  ;; Reset form to initial values
  (swap! state/state assoc-in [:user-profile :form-data]
         {:first-name "John"
          :last-name "Doe"
          :email "john.doe@example.com"
          :phone "+1 (555) 123-4567"
          :job-title "Senior Software Developer"
          :company "TechCorp Inc."
          :bio "Passionate software developer with 8+ years of experience building web applications. Specializes in ClojureScript, React, and modern web technologies."})
  (swap! state/state assoc-in [:user-profile :touched-fields] #{})
  (swap! state/state assoc-in [:user-profile :validation-errors] {}))

;; ============================================================================
;; Hero Section
;; ============================================================================

(defn hero-section []
  [:div.text-center.mb-12
   [:h1.text-4xl.font-bold.ty-text.mb-4 "User Profile Scenario"]
   [:p.text-lg.ty-text-.max-w-3xl.mx-auto.leading-relaxed
    "Experience the power of rich, interactive web components through a professional user profile interface. See how dropdowns can contain beautiful HTML content with flags, icons, and contextual information, while multiselects showcase colorful skill tags with semantic meanings."]

   [:div.flex.flex-wrap.gap-3.justify-center.mt-6
    [:span.px-3.py-1.ty-bg-primary-.ty-text-primary.rounded-full.text-sm.font-medium "Rich Dropdowns"]
    [:span.px-3.py-1.ty-bg-success-.ty-text-success.rounded-full.text-sm.font-medium "Visual Multiselects"]
    [:span.px-3.py-1.ty-bg-warning-.ty-text-warning.rounded-full.text-sm.font-medium "Modal Integration"]
    [:span.px-3.py-1.ty-bg-neutral-.ty-text.rounded-full.text-sm.font-medium "Form Validation"]]])

;; ============================================================================
;; Avatar Section
;; ============================================================================

(defn avatar-section [{:keys [on-avatar-click]}]
  [:div.flex.items-center.gap-4.mb-8
   [:div.relative
    ;; Profile Avatar - Click to change
    [:div.w-20.h-20.ty-surface-content.rounded-full.flex.items-center.justify-center.cursor-pointer.hover:ty-surface-elevated.transition-colors.border-2.border-dashed.ty-border.hover:ty-border-primary
     {:on {:click on-avatar-click}}
     [:ty-icon.ty-text- {:name "user"
                         :size "2xl"}]]

    ;; Upload badge
    [:div.absolute.-bottom-1.-right-1.w-8.h-8.ty-bg-primary.rounded-full.flex.items-center.justify-center.cursor-pointer.hover:opacity-80.transition-opacity.shadow-lg
     {:on {:click on-avatar-click}}
     [:ty-icon.ty-text++ {:name "plus"
                          :size "sm"}]]]

   [:div.flex-1
    [:h2.text-2xl.font-semibold.ty-text "John Doe"]
    [:p.ty-text- "Software Developer"]
    [:p.text-sm.ty-text-- "Click avatar to upload new photo"]]])

;; ============================================================================
;; Personal Information Section
;; ============================================================================

(defn personal-info-section [{:keys [form-data errors touched on-change]}]
  [:div.ty-floating.p-6.rounded-lg
   [:h3.text-xl.font-semibold.ty-text.mb-6.pb-2.border-b.ty-border "Personal Information"]

   [:div.space-y-6
    ;; First Name - Basic input
    [:div
     [:ty-input {:type "text"
                 :label "First Name"
                 :value (:first-name form-data)
                 :placeholder "Enter your first name"
                 :required true
                 :error (when (contains? touched :first-name)
                          (:first-name errors))
                 :on {:input (on-change :first-name)}}]]

    ;; Last Name - Basic input
    [:div
     [:ty-input {:type "text"
                 :label "Last Name"
                 :value (:last-name form-data)
                 :placeholder "Enter your last name"
                 :required true
                 :error (when (contains? touched :last-name)
                          (:last-name errors))
                 :on {:input (on-change :last-name)}}]]

    ;; Email - Input with validation
    [:div
     [:ty-input {:type "email"
                 :label "Email Address"
                 :value (:email form-data)
                 :placeholder "your@email.com"
                 :required true
                 :error (when (contains? touched :email)
                          (:email errors))
                 :on {:input (on-change :email)}}]]

    ;; Phone - Formatted input
    [:div
     [:ty-input {:type "tel"
                 :label "Phone Number"
                 :value (:phone form-data)
                 :placeholder "+1 (555) 123-4567"
                 :required true
                 :error (when (contains? touched :phone)
                          (:phone errors))
                 :on {:input (on-change :phone)}}]]]])

;; ============================================================================
;; Professional Information Section
;; ============================================================================

(defn professional-info-section [{:keys [form-data errors touched on-change]}]
  [:div.ty-floating.p-6.rounded-lg
   [:h3.text-xl.font-semibold.ty-text.mb-6.pb-2.border-b.ty-border "Professional Information"]

   [:div.space-y-6
    ;; Job Title
    [:div
     [:ty-input {:type "text"
                 :label "Job Title"
                 :value (:job-title form-data)
                 :placeholder "Your job title"
                 :error (when (contains? touched :job-title)
                          (:job-title errors))
                 :on {:input (on-change :job-title)}}]]

    ;; Company
    [:div
     [:ty-input {:type "text"
                 :label "Company"
                 :value (:company form-data)
                 :placeholder "Company name"
                 :error (when (contains? touched :company)
                          (:company errors))
                 :on {:input (on-change :company)}}]]

    ;; Bio - Textarea
    [:div
     [:ty-textarea {:type "textarea"
                    :label "Professional Bio"
                    :max-height "200px"
                    :value (:bio form-data)
                    :placeholder "Tell us about your professional background..."
                    :rows "4"
                    :error (when (contains? touched :bio)
                             (:bio errors))
                    :on {:change (on-change :bio)}}]
     ;; Character counter
     [:div.flex.justify-between.items-center.mt-2
      (when (and (contains? touched :bio) (:bio errors))
        [:p.text-sm.ty-text-danger.flex.items-center.gap-2
         [:ty-icon {:name "alert-circle"
                    :size "xs"}]
         (:bio errors)])
      [:span.text-xs.ty-text-
       {:class (cond
                 (> (count (:bio form-data "")) 450) "ty-text-warning"
                 (> (count (:bio form-data "")) 250) "ty-text-success"
                 :else "ty-text-")}
       (str (count (:bio form-data "")) "/500")]]]

    ;; Skills - Multiselect with unified simple tags
    [:div
     [:label.block.text-sm.font-medium.ty-text.mb-2 "Skills & Technologies"]
     [:ty-multiselect {:placeholder "Select your skills and technologies..."}
      ;; Pre-selected skills
      [:ty-tag {:value "clojurescript"
                :flavor "primary"}
       [:div.flex.items-center.gap-2
        [:span.ty-text++.text-xs.font-bold "λ"]
        [:span "ClojureScript"]]]
      [:ty-tag {:value "react"
                :flavor "neutral"}
       [:div.flex.items-center.gap-2
        [:span.ty-text++.text-xs.font-bold "⚛"]
        [:span "React"]]]
      [:ty-tag {:value "typescript"
                :flavor "secondary"}
       [:div.flex.items-center.gap-2
        [:span.ty-text++.text-xs.font-bold "TS"]
        [:span "TypeScript"]]]
      [:ty-tag {:value "nodejs"
                :flavor "success"}
       [:div.flex.items-center.gap-2
        [:span.ty-text++.text-xs.font-bold "JS"]
        [:span "Node.js"]]]
      [:ty-tag {:value "postgresql"
                :flavor "neutral"}
       [:div.flex.items-center.gap-2
        [:span.ty-text++.text-xs.font-bold "🐘"]
        [:span "PostgreSQL"]]]

      ;; Available options to select - simplified to match selected style
      [:ty-tag {:value "python"}
       [:div.flex.items-center.gap-2
        [:span.ty-text++.text-xs.font-bold "🐍"]
        [:span "Python"]]]
      [:ty-tag {:value "rust"}
       [:div.flex.items-center.gap-2
        [:span.ty-text++.text-xs.font-bold "🦀"]
        [:span "Rust"]]]
      [:ty-tag {:value "go"}
       [:div.flex.items-center.gap-2
        [:span.ty-text++.text-xs.font-bold "Go"]
        [:span "Go"]]]
      [:ty-tag {:value "docker"}
       [:div.flex.items-center.gap-2
        [:span.ty-text++.text-xs.font-bold "🐳"]
        [:span "Docker"]]]
      [:ty-tag {:value "kubernetes"}
       [:div.flex.items-center.gap-2
        [:span.ty-text++.text-xs.font-bold "☸"]
        [:span "Kubernetes"]]]
      [:ty-tag {:value "aws"}
       [:div.flex.items-center.gap-2
        [:span.ty-text++.text-xs.font-bold "☁"]
        [:span "AWS"]]]
      [:ty-tag {:value "graphql"}
       [:div.flex.items-center.gap-2
        [:span.ty-text++.text-xs.font-bold "QL"]
        [:span "GraphQL"]]]
      [:ty-tag {:value "mongodb"}
       [:div.flex.items-center.gap-2
        [:span.ty-text++.text-xs.font-bold "🍃"]
        [:span "MongoDB"]]]]]]])

;; ============================================================================
;; Location & Preferences Section
;; ============================================================================

(defn location-preferences-section []
  [:div
   [:h3.text-xl.font-semibold.ty-text.mb-6.pb-2.border-b.ty-border "Location & Preferences"]

   [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-6
    ;; Country Dropdown with flags and details
    [:div
     [:label.block.text-sm.font-medium.ty-text.mb-2 "Country"]
     [:ty-dropdown {:value "us"
                    :placeholder "Select your country"
                    :required true}
      [:ty-option {:value "us"}
       [:div.flex.items-center.gap-3
        [:span.text-lg "🇺🇸"]
        [:div.flex-1
         [:div.font-medium "United States"]
         [:div.text-xs.ty-text- "North America • USD"]]]]
      [:ty-option {:value "ca"}
       [:div.flex.items-center.gap-3
        [:span.text-lg "🇨🇦"]
        [:div.flex-1
         [:div.font-medium "Canada"]
         [:div.text-xs.ty-text- "North America • CAD"]]]]
      [:ty-option {:value "uk"}
       [:div.flex.items-center.gap-3
        [:span.text-lg "🇬🇧"]
        [:div.flex-1
         [:div.font-medium "United Kingdom"]
         [:div.text-xs.ty-text- "Europe • GBP"]]]]
      [:ty-option {:value "de"}
       [:div.flex.items-center.gap-3
        [:span.text-lg "🇩🇪"]
        [:div.flex-1
         [:div.font-medium "Germany"]
         [:div.text-xs.ty-text- "Europe • EUR"]]]]
      [:ty-option {:value "fr"}
       [:div.flex.items-center.gap-3
        [:span.text-lg "🇫🇷"]
        [:div.flex-1
         [:div.font-medium "France"]
         [:div.text-xs.ty-text- "Europe • EUR"]]]]
      [:ty-option {:value "jp"}
       [:div.flex.items-center.gap-3
        [:span.text-lg "🇯🇵"]
        [:div.flex-1
         [:div.font-medium "Japan"]
         [:div.text-xs.ty-text- "Asia • JPY"]]]]
      [:ty-option {:value "au"}
       [:div.flex.items-center.gap-3
        [:span.text-lg "🇦🇺"]
        [:div.flex-1
         [:div.font-medium "Australia"]
         [:div.text-xs.ty-text- "Oceania • AUD"]]]]]]

    ;; Timezone Dropdown with UTC offsets
    [:div
     [:label.block.text-sm.font-medium.ty-text.mb-2 "Timezone"]
     [:ty-dropdown {:value "pst"
                    :placeholder "Select timezone"}
      [:ty-option {:value "pst"}
       [:div.flex.items-center.justify-between
        [:div.flex.items-center.gap-2
         [:ty-icon.ty-text-warning {:name "sun"
                                    :size "sm"}]
         [:span.font-medium "Pacific Standard"]]
        [:span.text-xs.ty-text-.font-mono "UTC-8"]]]
      [:ty-option {:value "mst"}
       [:div.flex.items-center.justify-between
        [:div.flex.items-center.gap-2
         [:ty-icon.ty-text-warning {:name "sun"
                                    :size "sm"}]
         [:span.font-medium "Mountain Standard"]]
        [:span.text-xs.ty-text-.font-mono "UTC-7"]]]
      [:ty-option {:value "cst"}
       [:div.flex.items-center.justify-between
        [:div.flex.items-center.gap-2
         [:ty-icon.ty-text-warning {:name "sun"
                                    :size "sm"}]
         [:span.font-medium "Central Standard"]]
        [:span.text-xs.ty-text-.font-mono "UTC-6"]]]
      [:ty-option {:value "est"}
       [:div.flex.items-center.justify-between
        [:div.flex.items-center.gap-2
         [:ty-icon.ty-text-warning {:name "sun"
                                    :size "sm"}]
         [:span.font-medium "Eastern Standard"]]
        [:span.text-xs.ty-text-.font-mono "UTC-5"]]]
      [:ty-option {:value "utc"}
       [:div.flex.items-center.justify-between
        [:div.flex.items-center.gap-2
         [:ty-icon.ty-text {:name "globe"
                                 :size "sm"}]
         [:span.font-medium "Coordinated Universal"]]
        [:span.text-xs.ty-text-.font-mono "UTC+0"]]]]]

    ;; Language Preference with native names
    [:div
     [:label.block.text-sm.font-medium.ty-text.mb-2 "Preferred Language"]
     [:ty-dropdown {:value "en"
                    :placeholder "Select language"}
      [:ty-option {:value "en"}
       [:div.flex.items-center.gap-3
        [:div.w-8.h-6.ty-surface-content.rounded.flex.items-center.justify-center.text-xs.font-bold "EN"]
        [:div.flex-1
         [:div.font-medium "English"]
         [:div.text-xs.ty-text- "English"]]]]
      [:ty-option {:value "es"}
       [:div.flex.items-center.gap-3
        [:div.w-8.h-6.ty-surface-content.rounded.flex.items-center.justify-center.text-xs.font-bold "ES"]
        [:div.flex-1
         [:div.font-medium "Español"]
         [:div.text-xs.ty-text- "Spanish"]]]]
      [:ty-option {:value "fr"}
       [:div.flex.items-center.gap-3
        [:div.w-8.h-6.ty-surface-content.rounded.flex.items-center.justify-center.text-xs.font-bold "FR"]
        [:div.flex-1
         [:div.font-medium "Français"]
         [:div.text-xs.ty-text- "French"]]]]
      [:ty-option {:value "de"}
       [:div.flex.items-center.gap-3
        [:div.w-8.h-6.ty-surface-content.rounded.flex.items-center.justify-center.text-xs.font-bold "DE"]
        [:div.flex-1
         [:div.font-medium "Deutsch"]
         [:div.text-xs.ty-text- "German"]]]]
      [:ty-option {:value "ja"}
       [:div.flex.items-center.gap-3
        [:div.w-8.h-6.ty-surface-content.rounded.flex.items-center.justify-center.text-xs.font-bold "JA"]
        [:div.flex-1
         [:div.font-medium "日本語"]
         [:div.text-xs.ty-text- "Japanese"]]]]]]

    ;; Theme Preference with visual indicators
    [:div
     [:label.block.text-sm.font-medium.ty-text.mb-2 "Theme Preference"]
     [:ty-dropdown {:value "auto"
                    :placeholder "Select theme"}
      [:ty-option {:value "light"}
       [:div.flex.items-center.gap-3
        [:div.w-8.h-8.bg-white.border.ty-border.rounded-full.flex.items-center.justify-center.shadow-sm
         [:ty-icon.text-yellow-500 {:name "sun"
                                    :size "sm"}]]
        [:div.flex-1
         [:div.font-medium "Light Mode"]
         [:div.text-xs.ty-text- "Bright and clean"]]]]
      [:ty-option {:value "dark"}
       [:div.flex.items-center.gap-3
        [:div.w-8.h-8.bg-gray-900.border.border-gray-700.rounded-full.flex.items-center.justify-center.shadow-sm
         [:ty-icon.text-blue-400 {:name "moon"
                                  :size "sm"}]]
        [:div.flex-1
         [:div.font-medium "Dark Mode"]
         [:div.text-xs.ty-text- "Easy on the eyes"]]]]
      [:ty-option {:value "auto"}
       [:div.flex.items-center.gap-3
        [:div.w-8.h-8.ty-surface-elevated.border.ty-border.rounded-full.flex.items-center.justify-center.shadow-sm
         [:ty-icon.ty-text- {:name "settings"
                             :size "sm"}]]
        [:div.flex-1
         [:div.font-medium "Auto (System)"]
         [:div.text-xs.ty-text- "Follow device setting"]]]]]]]])

;; ============================================================================
;; Account Security Section
;; ============================================================================

(defn security-section []
  [:div
   [:h3.text-xl.font-semibold.ty-text.mb-6.pb-2.border-b.ty-border "Account & Security"]

   [:div.space-y-6
    ;; Password Fields Row
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
     [:div
      [:ty-input {:type "password"
                  :label "Current Password"
                  :placeholder "Enter current password"
                  :autocomplete "current-password"}]]
     [:div
      [:ty-input {:type "password"
                  :label "New Password"
                  :placeholder "Enter new password"
                  :autocomplete "new-password"}]]]

    ;; Email Notifications - Using ty-input checkboxes
    [:div.flex.wrap.gap-4
     [:div
      [:h4.text-sm.font-medium.ty-text.mb-4 "Email Notifications"]
      [:div.space-y-4
       [:ty-checkbox {:name "notifications-updates"
                      :value "enabled"
                      :checked ""}
        "Product updates and announcements"]
       [:ty-checkbox {:name "notifications-newsletter"
                      :value "enabled"
                      :checked ""}
        "Weekly newsletter"]
       [:ty-checkbox {:name "notifications-marketing"
                      :value "enabled"}
        "Marketing promotions and offers"]]]

     ;; Privacy & Security Preferences
     [:div
      [:h4.text-sm.font-medium.ty-text.mb-4 "Privacy & Security"]
      [:div.space-y-4
       [:ty-checkbox {:name "privacy-2fa"
                      :value "enabled"}
        "Enable two-factor authentication"]
       [:ty-checkbox {:name "privacy-profile-public"
                      :value "enabled"
                      :checked ""}
        "Make profile public"]
       [:ty-checkbox {:name "privacy-allow-contact"
                      :value "enabled"
                      :checked ""}
        "Allow contact from other users"]]]

     ;; Account Preferences
     [:div
      [:h4.text-sm.font-medium.ty-text.mb-4 "Account Preferences"]
      [:div.space-y-3
       [:ty-checkbox {:name "account-autosave"
                      :value "enabled"
                      :checked ""}
        "Auto-save drafts"]
       [:ty-checkbox {:name "account-online-status"
                      :value "enabled"
                      :checked ""}
        "Show online status"]
       [:ty-checkbox {:name "account-remember-login"
                      :value "enabled"}
        "Remember login for 30 days"]]]]]])

;; ============================================================================
;; Form Actions
;; ============================================================================

(defn form-actions [{:keys [is-submitting has-errors on-export on-cancel]}]
  [:div.flex.flex-col.sm:flex-row.gap-4.justify-between.items-center.pt-8.border-t.ty-border
   ;; Status indicator
   [:div.flex.items-center.gap-2
    (if has-errors
      [:div
       [:ty-icon.ty-text-danger {:name "alert-circle"}]
       [:span.text-sm.ty-text-danger "Please fix validation errors before saving"]]
      [:div
       [:ty-icon.ty-text-success {:name "check-circle"}]
       [:span.text-sm.ty-text-success "Form is valid and ready to save"]])]

   ;; Action buttons
   [:div.flex.gap-3
    [:ty-button {:type "button"
                 :flavor "neutral"
                 :on {:click on-cancel}}
     "Cancel Changes"]
    [:ty-button {:type "button"
                 :flavor "secondary"
                 :on {:click on-export}}
     [:ty-icon.mr-2 {:name "download"}]
     "Export Profile"]
    [:ty-button {:type "submit"
                 :flavor "primary"
                 :disabled (or is-submitting has-errors)}
     (if is-submitting
       [:div.flex.items-center.gap-2
        [:ty-icon {:name "loader-2"
                   :spin true
                   :size "sm"}]
        "Saving..."]
       [:div.flex.items-center.gap-2
        [:ty-icon {:name "save"
                   :size "sm"}]
        "Save Profile"])]]])

;; ============================================================================
;; Avatar Upload Modal
;; ============================================================================

(defn avatar-upload-modal [{:keys [open on-close]}]
  [:ty-modal {:open open
              :on {:close on-close}}
   [:div.p-8.max-w-lg.ty-elevated.rounded-lg.shadow-xl
    [:h3.text-xl.font-semibold.ty-text.mb-6 "Upload Profile Photo"]
    [:p.ty-text-.text-sm.mb-6 "Choose a new profile photo. For best results, upload an image that's at least 400x400 pixels."]

    ;; Upload Area
    [:div.border-2.border-dashed.ty-border.rounded-lg.p-8.text-center.hover:ty-border-primary.transition-colors.cursor-pointer.mb-6
     [:ty-icon.ty-text-.mb-4 {:name "upload"
                              :size "3xl"}]
     [:p.ty-text.font-medium.mb-2 "Drag and drop your photo here"]
     [:p.ty-text-.text-sm.mb-4 "or click to browse files"]

     ;; Supported formats showcase
     [:div.flex.flex-wrap.justify-center.gap-2.mt-4
      [:span.px-2.py-1.ty-bg-success-.ty-text-success.rounded.text-xs.font-medium "JPG"]
      [:span.px-2.py-1.ty-bg-success-.ty-text-success.rounded.text-xs.font-medium "PNG"]
      [:span.px-2.py-1.ty-bg-success-.ty-text-success.rounded.text-xs.font-medium "WebP"]
      [:span.px-2.py-1.ty-bg-info-.ty-text.rounded.text-xs.font-medium "Max 5MB"]]

     [:input.hidden {:type "file"
                     :accept "image/*"
                     :id "avatar-upload"}]]

    ;; Current Avatar Preview
    [:div.flex.items-center.gap-4.mb-6
     [:div.w-16.h-16.ty-surface-content.rounded-full.flex.items-center.justify-center
      [:ty-icon.ty-text- {:name "user"
                          :size "xl"}]]
     [:div.flex-1
      [:p.text-sm.font-medium.ty-text "Current Photo"]
      [:p.text-xs.ty-text- "No photo uploaded"]]]

    ;; Modal Actions
    [:div.flex.gap-3.justify-end
     [:ty-button {:flavor "neutral"
                  :on {:click on-close}}
      "Cancel"]
     [:ty-button {:flavor "primary"}
      [:ty-icon.mr-2 {:name "save"}]
      "Save Photo"]]]])

;; ============================================================================
;; Success Modal
;; ============================================================================

(defn success-modal [{:keys [open on-close saved-data]}]
  [:ty-modal {:open open
              :on {:close on-close}}
   [:div.p-8.max-w-2xl.ty-elevated.rounded-lg.shadow-xl
    [:div.flex.items-center.gap-4.mb-6
     [:div.w-16.h-16.ty-bg-success.rounded-full.flex.items-center.justify-center
      [:ty-icon.ty-text++ {:name "check"
                           :size "2xl"}]]
     [:div
      [:h3.text-2xl.font-semibold.ty-text "Profile Saved Successfully!"]
      [:p.ty-text- "Your profile has been updated with the following information:"]]]

    ;; Display saved data
    [:div.ty-floating.p-6.rounded-lg.space-y-4.mb-6
     [:div.grid.grid-cols-2.gap-4
      [:div
       [:p.text-sm.font-medium.ty-text+ "Name"]
       [:p.ty-text (str (:first-name saved-data) " " (:last-name saved-data))]]
      [:div
       [:p.text-sm.font-medium.ty-text+ "Email"]
       [:p.ty-text (:email saved-data)]]
      [:div
       [:p.text-sm.font-medium.ty-text+ "Phone"]
       [:p.ty-text (:phone saved-data)]]
      [:div
       [:p.text-sm.font-medium.ty-text+ "Company"]
       [:p.ty-text (:company saved-data)]]]
     [:div
      [:p.text-sm.font-medium.ty-text+.mb-2 "Job Title"]
      [:p.ty-text (:job-title saved-data)]]]

    [:div.flex.gap-3.justify-end
     [:ty-button {:flavor "primary"
                  :on {:click on-close}}
      "Close"]]]])

;; ============================================================================
;; Export Modal
;; ============================================================================

(defn export-modal [{:keys [open on-close exported-data]}]
  [:ty-modal {:open open
              :on {:close on-close}}
   [:div.p-8.max-w-2xl.ty-elevated.rounded-lg.shadow-xl
    [:div.flex.items-center.gap-4.mb-6
     [:div.w-16.h-16.ty-bg-info.rounded-full.flex.items-center.justify-center
      [:ty-icon.ty-text++ {:name "download"
                           :size "2xl"}]]
     [:div
      [:h3.text-2xl.font-semibold.ty-text "Export Profile Data"]
      [:p.ty-text- "Your profile data is ready for export."]]]

    ;; Export format options
    [:div.ty-floating.p-6.rounded-lg.mb-6
     [:h4.font-medium.ty-text.mb-4 "Select Export Format"]
     [:div.space-y-3
      [:div.p-4.border.ty-border.rounded-lg.cursor-pointer.hover:ty-border-primary.transition-colors
       [:div.flex.items-center.justify-between
        [:div
         [:p.font-medium.ty-text "JSON"]
         [:p.text-sm.ty-text- "Machine-readable format"]]
        [:ty-icon.ty-text-primary {:name "file-json"}]]]
      [:div.p-4.border.ty-border.rounded-lg.cursor-pointer.hover:ty-border-primary.transition-colors
       [:div.flex.items-center.justify-between
        [:div
         [:p.font-medium.ty-text "CSV"]
         [:p.text-sm.ty-text- "Spreadsheet format"]]
        [:ty-icon.ty-text-success {:name "table"}]]]
      [:div.p-4.border.ty-border.rounded-lg.cursor-pointer.hover:ty-border-primary.transition-colors
       [:div.flex.items-center.justify-between
        [:div
         [:p.font-medium.ty-text "PDF"]
         [:p.text-sm.ty-text- "Printable document"]]
        [:ty-icon.ty-text-danger {:name "file-text"}]]]]]

    [:div.flex.gap-3.justify-end
     [:ty-button {:flavor "neutral"
                  :on {:click on-close}}
      "Cancel"]
     [:ty-button {:flavor "primary"}
      [:ty-icon.mr-2 {:name "download"}]
      "Download"]]]])

;; ============================================================================
;; Main View
;; ============================================================================

(defn view []
  (let [{:keys [avatar-modal-open success-modal-open export-modal-open
                form-data validation-errors touched-fields is-submitting saved-data exported-data]}
        (get @state/state :user-profile
             ;; Default initial state
             {:avatar-modal-open false
              :success-modal-open false
              :export-modal-open false
              :form-data {:first-name "John"
                          :last-name "Doe"
                          :email "john.doe@example.com"
                          :phone "+1 (555) 123-4567"
                          :job-title "Senior Software Developer"
                          :company "TechCorp Inc."
                          :bio "Passionate software developer with 8+ years of experience building web applications. Specializes in ClojureScript, React, and modern web technologies."}
              :validation-errors {}
              :touched-fields #{}
              :is-submitting false
              :saved-data nil
              :exported-data nil})
        ;; Check if there are any validation errors
        has-errors (not (empty? validation-errors))]
    [:div.max-w-4xl.mx-auto.space-y-8
     ;; Hero Section
     (hero-section)

     ;; Interactive Demo
     [:div.ty-elevated.p-8.rounded-xl
      (avatar-section {:on-avatar-click #(swap! state/state assoc-in [:user-profile :avatar-modal-open] true)})

      [:form.space-y-8
       {:on {:submit handle-form-submit}}

       ;; Personal & Professional Information - Two Column Layout
       [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8
        ;; Left Column - Personal Information
        (personal-info-section {:form-data form-data
                                :errors validation-errors
                                :touched touched-fields
                                :on-change handle-field-change})

        ;; Right Column - Professional Information
        (professional-info-section {:form-data form-data
                                    :errors validation-errors
                                    :touched touched-fields
                                    :on-change handle-field-change})]

       ;; Location & Preferences Section
       (location-preferences-section)

       ;; Account Security Section
       (security-section)

       ;; Form Actions
       (form-actions {:is-submitting is-submitting
                      :has-errors has-errors
                      :on-export handle-export-click
                      :on-cancel handle-cancel})]]

     ;; Avatar Upload Modal
     (avatar-upload-modal {:open avatar-modal-open
                           :on-close #(swap! state/state assoc-in [:user-profile :avatar-modal-open] false)})

     ;; Success Modal
     (success-modal {:open success-modal-open
                     :on-close #(swap! state/state assoc-in [:user-profile :success-modal-open] false)
                     :saved-data saved-data})

     ;; Export Modal
     (export-modal {:open export-modal-open
                    :on-close #(swap! state/state assoc-in [:user-profile :export-modal-open] false)
                    :exported-data exported-data})]))
