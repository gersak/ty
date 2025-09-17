(ns ty.site.views.user-profile
  (:require [ty.site.state :as state]))

(defn view []
  [:div.max-w-4xl.mx-auto.space-y-8
   ;; Hero Section
   [:div.text-center.mb-12
    [:h1.text-4xl.font-bold.ty-text.mb-4 "User Profile Scenario"]
    [:p.text-lg.ty-text-.max-w-3xl.mx-auto.leading-relaxed
     "Experience the power of rich, interactive web components through a professional user profile interface. See how dropdowns can contain beautiful HTML content with flags, icons, and contextual information, while multiselects showcase colorful skill tags with semantic meanings."]

    [:div.flex.flex-wrap.gap-3.justify-center.mt-6
     [:span.px-3.py-1.ty-bg-primary-.ty-text-primary.rounded-full.text-sm.font-medium "Rich Dropdowns"]
     [:span.px-3.py-1.ty-bg-success-.ty-text-success.rounded-full.text-sm.font-medium "Visual Multiselects"]
     [:span.px-3.py-1.ty-bg-warning-.ty-text-warning.rounded-full.text-sm.font-medium "Modal Integration"]
     [:span.px-3.py-1.ty-bg-neutral-.ty-text-info.rounded-full.text-sm.font-medium "Form Validation"]]]

   ;; Interactive Demo
   [:div.ty-elevated.p-8.rounded-xl
    [:div.flex.items-center.gap-4.mb-8
     [:div.relative
      ;; Profile Avatar - Click to change
      [:div.w-20.h-20.ty-surface-content.rounded-full.flex.items-center.justify-center.cursor-pointer.hover:ty-surface-elevated.transition-colors.border-2.border-dashed.ty-border.hover:ty-border-primary
       {:on {:click #(swap! state/state assoc-in [:user-profile :avatar-modal-open] true)}}
       [:ty-icon {:name "user"
                  :size "2xl"
                  :class "ty-text-"}]]

      ;; Upload badge
      [:div.absolute.-bottom-1.-right-1.w-8.h-8.ty-bg-primary.rounded-full.flex.items-center.justify-center.cursor-pointer.hover:opacity-80.transition-opacity.shadow-lg
       {:on {:click #(swap! state/state assoc-in [:user-profile :avatar-modal-open] true)}}
       [:ty-icon.ty-text++ {:name "plus"
                            :size "sm"}]]]

     [:div.flex-1
      [:h2.text-2xl.font-semibold.ty-text "John Doe"]
      [:p.ty-text- "Software Developer"]
      [:p.text-sm.ty-text-- "Click avatar to upload new photo"]]]

    [:form.space-y-8
     ;; Personal Information Section
     [:div
      [:h3.text-xl.font-semibold.ty-text.mb-6.pb-2.border-b.ty-border "Personal Information"]

      [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
       ;; First Name - Basic input
       [:div
        [:ty-input {:type "text"
                    :label "First Name"
                    :value "John"
                    :placeholder "Enter your first name"
                    :required true}]]

       ;; Last Name - Basic input
       [:div
        [:ty-input {:type "text"
                    :label "Last Name"
                    :value "Doe"
                    :placeholder "Enter your last name"
                    :required true}]]

       ;; Email - Input with validation
       [:div
        [:ty-input {:type "email"
                    :label "Email Address"
                    :value "john.doe@example.com"
                    :placeholder "your@email.com"
                    :required true}]]

       ;; Phone - Formatted input
       [:div
        [:ty-input {:type "tel"
                    :label "Phone Number"
                    :value "+1 (555) 123-4567"
                    :placeholder "+1 (555) 123-4567"
                    :required true}]]]]

     ;; Professional Information Section
     [:div
      [:h3.text-xl.font-semibold.ty-text.mb-6.pb-2.border-b.ty-border "Professional Information"]

      [:div.space-y-6
       ;; Job Title and Company Row
       [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
        [:div
         [:ty-input {:type "text"
                     :label "Job Title"
                     :value "Senior Software Developer"
                     :placeholder "Your job title"}]]

        [:div
         [:ty-input {:type "text"
                     :label "Company"
                     :value "TechCorp Inc."
                     :placeholder "Company name"}]]]

       ;; Bio - Textarea
       [:div
        [:ty-input {:type "textarea"
                    :label "Professional Bio"
                    :value "Passionate software developer with 8+ years of experience building web applications. Specializes in ClojureScript, React, and modern web technologies."
                    :placeholder "Tell us about your professional background..."
                    :rows "4"}]]

       ;; Skills - Multiselect with rich tags
       [:div
        [:label.block.text-sm.font-medium.ty-text.mb-2 "Skills & Technologies"]
        [:ty-multiselect {:placeholder "Select your skills and technologies..."}
         ;; Pre-selected skills
         [:ty-tag {:value "clojurescript"
                   :flavor "primary"}
          [:div.flex.items-center.gap-2
           [:div.w-4.h-4.ty-bg-primary.rounded.flex.items-center.justify-center
            [:span.ty-text++.text-xs.font-bold "λ"]]
           [:span "ClojureScript"]]]
         [:ty-tag {:value "react"
                   :flavor "neutral"}
          [:div.flex.items-center.gap-2
           [:div.w-4.h-4.bg-blue-500.rounded.flex.items-center.justify-center
            [:span.ty-text++.text-xs.font-bold "⚛"]]
           [:span "React"]]]
         [:ty-tag {:value "typescript"
                   :flavor "secondary"}
          [:div.flex.items-center.gap-2
           [:div.w-4.h-4.bg-blue-600.rounded.flex.items-center.justify-center
            [:span.ty-text++.text-xs.font-bold "TS"]]
           [:span "TypeScript"]]]
         [:ty-tag {:value "nodejs"
                   :flavor "success"}
          [:div.flex.items-center.gap-2
           [:div.w-4.h-4.bg-green-600.rounded.flex.items-center.justify-center
            [:span.ty-text++.text-xs.font-bold "JS"]]
           [:span "Node.js"]]]
         [:ty-tag {:value "postgresql"
                   :flavor "neutral"}
          [:div.flex.items-center.gap-2
           [:div.w-4.h-4.bg-blue-800.rounded.flex.items-center.justify-center
            [:span.ty-text++.text-xs.font-bold "🐘"]]
           [:span "PostgreSQL"]]]

         ;; Available options to select
         [:ty-tag {:value "python"}
          [:div.flex.items-center.gap-3
           [:div.w-6.h-6.bg-yellow-500.rounded.flex.items-center.justify-center
            [:span.ty-text++.text-xs.font-bold "🐍"]]
           [:div.flex-1
            [:div.font-medium "Python"]
            [:div.text-xs.ty-text- "Programming language"]]]]
         [:ty-tag {:value "rust"}
          [:div.flex.items-center.gap-3
           [:div.w-6.h-6.bg-orange-600.rounded.flex.items-center.justify-center
            [:span.ty-text++.text-xs.font-bold "🦀"]]
           [:div.flex-1
            [:div.font-medium "Rust"]
            [:div.text-xs.ty-text- "Systems programming"]]]]
         [:ty-tag {:value "go"}
          [:div.flex.items-center.gap-3
           [:div.w-6.h-6.bg-cyan-500.rounded.flex.items-center.justify-center
            [:span.ty-text++.text-xs.font-bold "Go"]]
           [:div.flex-1
            [:div.font-medium "Go"]
            [:div.text-xs.ty-text- "Backend development"]]]]
         [:ty-tag {:value "docker"}
          [:div.flex.items-center.gap-3
           [:div.w-6.h-6.bg-blue-600.rounded.flex.items-center.justify-center
            [:span.ty-text++.text-xs.font-bold "🐳"]]
           [:div.flex-1
            [:div.font-medium "Docker"]
            [:div.text-xs.ty-text- "Containerization"]]]]
         [:ty-tag {:value "kubernetes"}
          [:div.flex.items-center.gap-3
           [:div.w-6.h-6.bg-blue-700.rounded.flex.items-center.justify-center
            [:span.ty-text++.text-xs.font-bold "☸"]]
           [:div.flex-1
            [:div.font-medium "Kubernetes"]
            [:div.text-xs.ty-text- "Container orchestration"]]]]
         [:ty-tag {:value "aws"}
          [:div.flex.items-center.gap-3
           [:div.w-6.h-6.bg-orange-500.rounded.flex.items-center.justify-center
            [:span.ty-text++.text-xs.font-bold "☁"]]
           [:div.flex-1
            [:div.font-medium "AWS"]
            [:div.text-xs.ty-text- "Cloud platform"]]]]
         [:ty-tag {:value "graphql"}
          [:div.flex.items-center.gap-3
           [:div.w-6.h-6.bg-pink-500.rounded.flex.items-center.justify-center
            [:span.ty-text++.text-xs.font-bold "QL"]]
           [:div.flex-1
            [:div.font-medium "GraphQL"]
            [:div.text-xs.ty-text- "API query language"]]]]
         [:ty-tag {:value "mongodb"}
          [:div.flex.items-center.gap-3
           [:div.w-6.h-6.bg-green-500.rounded.flex.items-center.justify-center
            [:span.ty-text++.text-xs.font-bold "🍃"]]
           [:div.flex-1
            [:div.font-medium "MongoDB"]
            [:div.text-xs.ty-text- "NoSQL database"]]]]]]]]

     ;; Location & Preferences Section  
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
            [:ty-icon {:name "sun"
                       :size "sm"
                       :class "ty-text-warning"}]
            [:span.font-medium "Pacific Standard"]]
           [:span.text-xs.ty-text-.font-mono "UTC-8"]]]
         [:ty-option {:value "mst"}
          [:div.flex.items-center.justify-between
           [:div.flex.items-center.gap-2
            [:ty-icon {:name "sun"
                       :size "sm"
                       :class "ty-text-warning"}]
            [:span.font-medium "Mountain Standard"]]
           [:span.text-xs.ty-text-.font-mono "UTC-7"]]]
         [:ty-option {:value "cst"}
          [:div.flex.items-center.justify-between
           [:div.flex.items-center.gap-2
            [:ty-icon {:name "sun"
                       :size "sm"
                       :class "ty-text-warning"}]
            [:span.font-medium "Central Standard"]]
           [:span.text-xs.ty-text-.font-mono "UTC-6"]]]
         [:ty-option {:value "est"}
          [:div.flex.items-center.justify-between
           [:div.flex.items-center.gap-2
            [:ty-icon {:name "sun"
                       :size "sm"
                       :class "ty-text-warning"}]
            [:span.font-medium "Eastern Standard"]]
           [:span.text-xs.ty-text-.font-mono "UTC-5"]]]
         [:ty-option {:value "utc"}
          [:div.flex.items-center.justify-between
           [:div.flex.items-center.gap-2
            [:ty-icon {:name "globe"
                       :size "sm"
                       :class "ty-text-info"}]
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
            [:ty-icon {:name "sun"
                       :size "sm"
                       :class "text-yellow-500"}]]
           [:div.flex-1
            [:div.font-medium "Light Mode"]
            [:div.text-xs.ty-text- "Bright and clean"]]]]
         [:ty-option {:value "dark"}
          [:div.flex.items-center.gap-3
           [:div.w-8.h-8.bg-gray-900.border.border-gray-700.rounded-full.flex.items-center.justify-center.shadow-sm
            [:ty-icon {:name "moon"
                       :size "sm"
                       :class "text-blue-400"}]]
           [:div.flex-1
            [:div.font-medium "Dark Mode"]
            [:div.text-xs.ty-text- "Easy on the eyes"]]]]
         [:ty-option {:value "auto"}
          [:div.flex.items-center.gap-3
           [:div.w-8.h-8.ty-surface-elevated.border.ty-border.rounded-full.flex.items-center.justify-center.shadow-sm
            [:ty-icon {:name "settings"
                       :size "sm"
                       :class "ty-text-"}]]
           [:div.flex-1
            [:div.font-medium "Auto (System)"]
            [:div.text-xs.ty-text- "Follow device setting"]]]]]]]]

     ;; Account Security Section
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
       [:div
        [:h4.text-sm.font-medium.ty-text.mb-4 "Email Notifications"]
        [:div.space-y-4
         [:ty-input {:type "checkbox"
                     :label "Product updates and announcements"
                     :name "notifications-updates"
                     :checked true
                     :flavor "primary"}]
         [:ty-input {:type "checkbox"
                     :label "Weekly newsletter"
                     :name "notifications-newsletter"
                     :checked true
                     :flavor "success"}]
         [:ty-input {:type "checkbox"
                     :label "Marketing promotions"
                     :name "notifications-marketing"
                     :checked false
                     :flavor "warning"}]]]

       ;; Privacy & Security Preferences
       [:div
        [:h4.text-sm.font-medium.ty-text.mb-4 "Privacy & Security"]
        [:div.space-y-4
         [:ty-input {:type "checkbox"
                     :label "Enable two-factor authentication"
                     :name "privacy-2fa"
                     :checked false
                     :required true
                     :flavor "danger"
                     :size "md"}]
         [:ty-input {:type "checkbox"
                     :label "Share profile with other users"
                     :name "privacy-profile-sharing"
                     :checked true
                     :flavor "neutral"
                     :size "sm"}]
         [:ty-input {:type "checkbox"
                     :label "Allow search engines to index profile"
                     :name "privacy-search-indexing"
                     :checked false
                     :disabled true
                     :flavor "secondary"
                     :size "lg"}]]]

       ;; Account Preferences
       [:div
        [:h4.text-sm.font-medium.ty-text.mb-4 "Account Preferences"]
        [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
         [:ty-input {:type "checkbox"
                     :label "Auto-save drafts"
                     :name "account-autosave"
                     :checked true
                     :flavor "primary"
                     :size "xs"}]
         [:ty-input {:type "checkbox"
                     :label "Show online status"
                     :name "account-online-status"
                     :checked false
                     :flavor "success"
                     :size "xl"}]
         [:ty-input {:type "checkbox"
                     :label "Send mobile push notifications"
                     :name "account-push-notifications"
                     :checked true
                     :error "Feature currently unavailable"
                     :flavor "danger"}]
         [:ty-input {:type "checkbox"
                     :label "Beta features access"
                     :name "account-beta-features"
                     :checked false
                     :flavor "warning"}]]]]]

     ;; Form Actions
     [:div.flex.flex-col.sm:flex-row.gap-4.justify-between.items-center.pt-8.border-t.ty-border
      ;; Status indicator (would be dynamic in real app)
      [:div.flex.items-center.gap-2
       [:ty-icon {:name "check-circle"
                  :class "ty-text-success"}]
       [:span.text-sm.ty-text-success "All changes saved automatically"]]

      ;; Action buttons
      [:div.flex.gap-3
       [:ty-button {:type "button"
                    :flavor "neutral"}
        "Cancel Changes"]
       [:ty-button {:type "button"
                    :flavor "secondary"}
        [:ty-icon {:name "download"
                   :class "mr-2"}]
        "Export Profile"]
       [:ty-button {:type "submit"
                    :flavor "primary"}
        [:ty-icon {:name "save"
                   :class "mr-2"}]
        "Save Profile"]]]]]

;; Feature Showcase
   [:div.grid.grid-cols-1.lg:grid-cols-3.gap-8.mt-12
    ;; Validation States Showcase
    [:div.ty-elevated.p-6.rounded-lg
     [:h3.text-lg.font-semibold.ty-text.mb-4 "🎯 Validation States"]
     [:p.ty-text-.text-sm.mb-6 "See how form components handle different validation states using the semantic design system."]

     [:div.space-y-4
      ;; Success state
      [:div
       [:ty-input {:type "email"
                   :label "Email (Valid)"
                   :value "user@example.com"
                   :flavor "success"}]]

      ;; Error state simulation
      [:div
       [:ty-input {:type "password"
                   :label "Password (Invalid)"
                   :value "123"
                   :flavor "danger"}]
       [:p.text-sm.ty-text-danger.mt-1 "Password must be at least 8 characters"]]

      ;; Loading state simulation
      [:div
       [:ty-input {:type "text"
                   :label "Username (Checking availability)"
                   :value "johnsmith"
                   :flavor "warning"}]
       [:p.text-sm.ty-text-warning.mt-1 "Checking availability..."]]]]

    ;; Rich Content Features
    [:div.ty-elevated.p-6.rounded-lg
     [:h3.text-lg.font-semibold.ty-text.mb-4 "✨ Rich Content Features"]
     [:div.space-y-4.text-sm
      [:div.flex.items-start.gap-3
       [:ty-icon {:name "check-circle"
                  :class "ty-text-success mt-0.5"
                  :size "sm"}]
       [:div
        [:strong.ty-text "Rich Dropdown Options"]
        [:p.ty-text-- "Add icons, flags, currency symbols, and detailed descriptions to dropdown options using any HTML content."]]]

      [:div.flex.items-start.gap-3
       [:ty-icon {:name "check-circle"
                  :class "ty-text-success mt-0.5"
                  :size "sm"}]
       [:div
        [:strong.ty-text "Visual Multiselect Tags"]
        [:p.ty-text-- "Create beautiful skill tags with icons, colors, and semantic flavors for intuitive categorization."]]]

      [:div.flex.items-start.gap-3
       [:ty-icon {:name "check-circle"
                  :class "ty-text-success mt-0.5"
                  :size "sm"}]
       [:div
        [:strong.ty-text "Contextual Information"]
        [:p.ty-text-- "Show additional context like UTC offsets, currency codes, or language names without cluttering the interface."]]]

      [:div.flex.items-start.gap-3
       [:ty-icon {:name "check-circle"
                  :class "ty-text-success mt-0.5"
                  :size "sm"}]
       [:div
        [:strong.ty-text "Professional Polish"]
        [:p.ty-text-- "Every interaction feels premium with consistent spacing, hover states, and visual feedback throughout."]]]]

    ;; Technical Benefits
     [:div.ty-elevated.p-6.rounded-lg
      [:h3.text-lg.font-semibold.ty-text.mb-4 "⚡ Technical Benefits"]
      [:div.space-y-4.text-sm
       [:div.flex.items-start.gap-3
        [:ty-icon {:name "check-circle"
                   :class "ty-text-success mt-0.5"
                   :size "sm"}]
        [:div
         [:strong.ty-text "Framework Agnostic"]
         [:p.ty-text-- "Pure web components work with any frontend framework or vanilla JavaScript."]]]

       [:div.flex.items-start.gap-3
        [:ty-icon {:name "check-circle"
                   :class "ty-text-success mt-0.5"
                   :size "sm"}]
        [:div
         [:strong.ty-text "Semantic Design System"]
         [:p.ty-text-- "Colors convey meaning - success, danger, warning states are consistent across all components."]]]

       [:div.flex.items-start.gap-3
        [:ty-icon {:name "check-circle"
                   :class "ty-text-success mt-0.5"
                   :size "sm"}]
        [:div
         [:strong.ty-text "Flexible Content Model"]
         [:p.ty-text-- "HTML content inside ty-option and ty-tag elements enables unlimited customization possibilities."]]]

       [:div.flex.items-start.gap-3
        [:ty-icon {:name "check-circle"
                   :class "ty-text-success mt-0.5"
                   :size "sm"}]
        [:div
         [:strong.ty-text "Responsive & Accessible"]
         [:p.ty-text-- "Mobile-optimized layouts with proper keyboard navigation and screen reader support."]]]]]]]

   ;; Avatar Upload Modal
   [:ty-modal {:open (get-in @state/state [:user-profile :avatar-modal-open] false)
               :on {:ty-modal-close #(swap! state/state assoc-in [:user-profile :avatar-modal-open] false)}}
    [:div.p-8.max-w-lg.ty-elevated.rounded-lg.shadow-xl
     [:h3.text-xl.font-semibold.ty-text.mb-6 "Upload Profile Photo"]
     [:p.ty-text-.text-sm.mb-6 "Choose a new profile photo. For best results, upload an image that's at least 400x400 pixels."]

     ;; Upload Area
     [:div.border-2.border-dashed.ty-border.rounded-lg.p-8.text-center.hover:ty-border-primary.transition-colors.cursor-pointer.mb-6
      [:ty-icon {:name "upload"
                 :size "3xl"
                 :class "ty-text- mb-4"}]
      [:p.ty-text.font-medium.mb-2 "Drag and drop your photo here"]
      [:p.ty-text-.text-sm.mb-4 "or click to browse files"]

      ;; Supported formats showcase
      [:div.flex.flex-wrap.justify-center.gap-2.mt-4
       [:span.px-2.py-1.ty-bg-success-.ty-text-success.rounded.text-xs.font-medium "JPG"]
       [:span.px-2.py-1.ty-bg-success-.ty-text-success.rounded.text-xs.font-medium "PNG"]
       [:span.px-2.py-1.ty-bg-success-.ty-text-success.rounded.text-xs.font-medium "WebP"]
       [:span.px-2.py-1.ty-bg-info-.ty-text-info.rounded.text-xs.font-medium "Max 5MB"]]

      [:input.hidden {:type "file"
                      :accept "image/*"
                      :id "avatar-upload"}]]

     ;; Current Avatar Preview
     [:div.flex.items-center.gap-4.mb-6
      [:div.w-16.h-16.ty-surface-content.rounded-full.flex.items-center.justify-center
       [:ty-icon {:name "user"
                  :size "xl"
                  :class "ty-text-"}]]
      [:div.flex-1
       [:p.text-sm.font-medium.ty-text "Current Photo"]
       [:p.text-xs.ty-text- "No photo uploaded"]]]

     ;; Modal Actions
     [:div.flex.gap-3.justify-end
      [:ty-button {:flavor "neutral"
                   :on {:click #(swap! state/state assoc-in [:user-profile :avatar-modal-open] false)}}
       "Cancel"]
      [:ty-button {:flavor "primary"}
       [:ty-icon {:name "save"
                  :class "mr-2"}]
       "Save Photo"]]]]])
