(ns ty.site.state)

(defonce state
  (atom {:theme "light"
         :mobile-menu-open false

         ;; User profile state
         :user-profile {:avatar-modal-open false
                        :form-data {:first-name "John"
                                    :last-name "Doe"
                                    :email "john.doe@example.com"
                                    :phone "+1 (555) 123-4567"
                                    :job-title "Senior Software Developer"
                                    :company "TechCorp Inc."
                                    :bio "Passionate software developer with 8+ years of experience building web applications. Specializes in ClojureScript, React, and modern web technologies."
                                    :skills "ClojureScript, React, TypeScript, Node.js, PostgreSQL"
                                    :country "us"
                                    :timezone "pst"
                                    :language "en"
                                    :theme-preference "auto"}}

         ;; Event booking state
         :event-booking {:confirmation-modal-open false
                         :selected-date nil
                         :selected-time nil
                         :selected-services #{}
                         :service-quantities {} ; Map of service-id -> quantity
                         :attendee-count 1
                         :booking-data {:event-type "meeting"
                                        :duration "60"
                                        :contact-name ""
                                        :contact-email ""
                                        :special-requests ""}}

         ;; Contact form state
         :contact-form {:form-data {:full-name "Sarah Chen"
                                    :email "sarah.chen@designstudio.com"
                                    :company "Creative Design Studio"
                                    :subject "Partnership Inquiry for Web Component Library"
                                    :message "Hi there! I'm reaching out on behalf of Creative Design Studio. We've been exploring modern web component libraries for our upcoming client projects, and Ty Components caught our attention with its elegant design system and ClojureScript foundation.\n\nWe're particularly interested in:\n• The semantic color system and automatic dark/light theme support\n• Professional form components with real-time validation\n• Integration possibilities with our existing React projects\n\nWould you be open to discussing a potential partnership? We work with several Fortune 500 companies who might benefit from Ty's component library. We'd love to schedule a call to explore collaboration opportunities.\n\nLooking forward to hearing from you!\n\nBest regards,\nSarah"
                                    :priority "high"
                                    :department #{"partnership" "sales"}
                                    :newsletter-consent true}
                        :validation-errors {}
                        :touched-fields #{}
                        :is-submitting false
                        :submission-status nil ; :success, :error, or nil
                        :submission-message ""}}))
