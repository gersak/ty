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
                                    :theme-preference "auto"}}}))
