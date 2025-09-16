(ns ty.site.views.contact-form)

(defn view []
  [:div.ty-elevated.p-8.rounded-lg
   [:h1.text-2xl.font-bold.ty-text.mb-6 "Contact Form Scenario"]
   [:p.ty-text-.mb-8 "This will showcase professional form validation, real-time feedback, and elegant error states."]

   [:div.ty-bg-neutral-.p-6.rounded-lg.text-center
    [:ty-icon {:name "mail"
               :size "3xl"
               :class "ty-text-warning mb-4"}]
    [:h3.text-lg.font-semibold.ty-text.mb-2 "Coming Soon"]
    [:p.ty-text-.text-sm "Professional contact form with real-time validation and submission flow."]]])
