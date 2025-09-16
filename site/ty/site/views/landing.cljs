(ns ty.site.views.landing
  (:require
    [ty.router :as router]))

(defn view []
  [:div.ty-elevated.p-8.rounded-lg
   [:h1.text-3xl.font-bold.ty-text.mb-6 "Welcome to Ty Components"]
   [:p.ty-text-.text-lg.mb-8 "Professional Web Components with Semantic Design System"]

   [:div.grid.gap-6.md:grid-cols-2.lg:grid-cols-3
    ;; User Profile Card
    [:div.ty-elevated.p-6.rounded-lg.hover:shadow-lg.transition-shadow
     [:div.flex.items-center.mb-4
      [:ty-icon {:name "user"
                 :size "lg"
                 :class "ty-text-primary mr-3"}]
      [:h3.text-xl.font-semibold.ty-text "User Profile"]]
     [:p.ty-text-.mb-4 "Form fundamentals with input validation, dropdowns, and modal integration."]
     [:ty-button
      {:flavor "primary"
       :on {:click #(router/navigate! :ty.site.core/user-profile)}}
      "View Demo"]]

    ;; Event Booking Card
    [:div.ty-elevated.p-6.rounded-lg.hover:shadow-lg.transition-shadow
     [:div.flex.items-center.mb-4
      [:ty-icon {:name "calendar"
                 :size "lg"
                 :class "ty-text-success mr-3"}]
      [:h3.text-xl.font-semibold.ty-text "Event Booking"]]
     [:p.ty-text-.mb-4 "Interactive calendar with date selection and complex component orchestration."]
     [:ty-button
      {:flavor "success"
       :on {:click #(router/navigate! :ty.site.core/event-booking)}}
      "View Demo"]]

    ;; Contact Form Card
    [:div.ty-elevated.p-6.rounded-lg.hover:shadow-lg.transition-shadow
     [:div.flex.items-center.mb-4
      [:ty-icon {:name "mail"
                 :size "lg"
                 :class "ty-text-warning mr-3"}]
      [:h3.text-xl.font-semibold.ty-text "Contact Form"]]
     [:p.ty-text-.mb-4 "Professional form validation with real-time feedback and elegant error states."]
     [:ty-button
      {:flavor "warning"
       :on {:click #(router/navigate! :ty.site.core/contact-form)}}
      "View Demo"]]]

   [:div.mt-12.ty-bg-neutral-.p-6.rounded-lg
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Key Features"]
    [:div.grid.gap-4.md:grid-cols-2
     [:div.flex.items-start
      [:ty-icon {:name "check-circle"
                 :size "sm"
                 :class "ty-text-success mt-1 mr-3 flex-shrink-0"}]
      [:div
       [:h4.font-medium.ty-text "Framework Agnostic"]
       [:p.ty-text-.text-sm "Works with React, Vue, vanilla JavaScript, or any framework"]]]
     [:div.flex.items-start
      [:ty-icon {:name "star"
                 :size "sm"
                 :class "ty-text-primary mt-1 mr-3 flex-shrink-0"}]
      [:div
       [:h4.font-medium.ty-text "Semantic Design System"]
       [:p.ty-text-.text-sm "5-variant color system with automatic dark mode support"]]]
     [:div.flex.items-start
      [:ty-icon {:name "settings"
                 :size "sm"
                 :class "ty-text-secondary mt-1 mr-3 flex-shrink-0"}]
      [:div
       [:h4.font-medium.ty-text "Production Ready"]
       [:p.ty-text-.text-sm "Calendar, forms, modals with professional UX patterns"]]]
     [:div.flex.items-start
      [:ty-icon {:name "globe"
                 :size "sm"
                 :class "mt-1 mr-3 flex-shrink-0"}]
      [:div
       [:h4.font-medium.ty-text "Zero Dependencies"]
       [:p.ty-text-.text-sm "Pure web standards with no runtime overhead"]]]]]])
