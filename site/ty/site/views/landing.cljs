(ns ty.site.views.landing
  (:require
   [ty.router :as router]))

(defn view []
  [:div.max-w-7xl.mx-auto
   ;; ⚡ HERO - Clean & Professional
   [:div.text-center.mb-16.py-16
    [:h1.text-4xl.lg:text-6xl.font-bold.ty-text.mb-4.leading-tight
     "Beautiful Web Components That Actually Work"]

    [:p.text-lg.lg:text-xl.ty-text-.max-w-3xl.mx-auto.leading-relaxed.mb-8
     "Experience three stunning scenarios that showcase semantic design, automatic theming, and professional UX patterns working in harmony."]

    [:div.flex.flex-wrap.gap-3.justify-center
     [:ty-tag {:flavor "primary"
               :size "md"} "Semantic Design"]
     [:ty-tag {:flavor "success"
               :size "md"} "Calendar Workflows"]
     [:ty-tag {:flavor "warning"
               :size "md"} "Form Validation"]
     [:ty-tag {:flavor "neutral"
               :size "md"} "Zero Dependencies"]]]

   ;; 🎯 MAIN SHOWCASE - Three Stunning Scenarios
   [:div.mb-20
    [:h2.text-4xl.font-bold.ty-text.text-center.mb-4 "Four Stunning Scenarios"]
    [:p.text-lg.ty-text-.text-center.mb-12.max-w-3xl.mx-auto
     "Each scenario demonstrates how Ty components orchestrate to create seamless, professional user experiences."]

    [:div.grid.gap-6.md:grid-cols-2.lg:grid-cols-4
     ;; User Profile Scenario
     [:div.ty-elevated.overflow-hidden.rounded-xl.hover:shadow-xl.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(router/navigate! :ty.site.core/user-profile)}}
      [:div.h-48.ty-bg-primary-.flex.items-center.justify-center.relative.overflow-hidden
       [:ty-icon {:name "user"
                  :size "96"
                  :class "ty-text-primary group-hover:scale-110 transition-transform duration-300"}]
       [:div.absolute.inset-0.bg-gradient-to-br.from-transparent.to-black.opacity-10]
       [:div.absolute.top-4.right-4.ty-surface-floating.bg-opacity-20.backdrop-blur-sm.rounded-full.p-2
        [:ty-icon.ty-text++.group-hover:translate-x-1.transition-transform
         {:name "arrow-right"}]]]
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-3 "Rich User Profile Forms"]
       [:p.ty-text-.mb-4.leading-relaxed "Witness dropdowns filled with beautiful HTML content - country flags, role badges, and contextual information. See multiselect components showcase colorful skill tags with semantic meanings. Experience modal integration and professional form validation patterns."]
       [:div.flex.flex-wrap.gap-2.mb-4
        [:ty-tag {:flavor "primary"
                  :size "sm"
                  :pill "false"} "Rich Dropdowns"]
        [:ty-tag {:flavor "success"
                  :size "sm"
                  :pill "false"} "Visual Multiselects"]
        [:ty-tag {:flavor "warning"
                  :size "sm"
                  :pill "false"} "Modal Integration"]]
       [:div.flex.justify-between.items-center.group-hover:translate-x-1.transition-transform
        [:span.text-sm.ty-text-primary.font-medium "Explore Profile Demo →"]]]]

     ;; Event Booking Scenario
     [:div.ty-elevated.overflow-hidden.rounded-xl.hover:shadow-xl.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(router/navigate! :ty.site.core/event-booking)}}
      [:div.h-48.ty-bg-success-.flex.items-center.justify-center.relative.overflow-hidden
       [:ty-icon {:name "calendar"
                  :size "96"
                  :class "ty-text-success group-hover:scale-110 transition-transform duration-300"}]
       [:div.absolute.inset-0.bg-gradient-to-br.from-transparent.to-black.opacity-10]
       [:div.absolute.top-4.right-4.ty-surface-floating.bg-opacity-30.backdrop-blur-sm.rounded-full.p-2
        [:ty-icon.ty-text++.group-hover:translate-x-1.transition-transform
         {:name "external-link"
          :size "sm"}]]]
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-3 "Event Booking Experience"]
       [:p.ty-text-.mb-4.leading-relaxed "Interactive calendar orchestration with date selection, time slot management, and service customization. See how components work together to create seamless booking workflows."]
       [:div.flex.flex-wrap.gap-2.mb-4
        [:ty-tag {:flavor "success"
                  :size "sm"
                  :pill "false"} "Calendar Integration"]
        [:ty-tag {:flavor "neutral"
                  :size "sm"
                  :pill "false"} "Service Selection"]
        [:ty-tag {:flavor "warning"
                  :size "sm"
                  :pill "false"} "Booking Workflows"]]
       [:div.flex.justify-between.items-center.group-hover:translate-x-1.transition-transform
        [:span.text-sm.ty-text-success.font-medium "Launch Experience →"]]]]

     ;; Contact Form Scenario
     [:div.ty-elevated.overflow-hidden.rounded-xl.hover:shadow-xl.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(router/navigate! :ty.site.core/contact-form)}}
      [:div.h-48.ty-bg-warning-.flex.items-center.justify-center.relative.overflow-hidden
       [:ty-icon {:name "mail"
                  :size "96"
                  :class "ty-text-warning group-hover:scale-110 transition-transform duration-300"}]
       [:div.absolute.inset-0.bg-gradient-to-br.from-transparent.to-black.opacity-10]
       [:div.absolute.top-4.right-4.ty-surface-floating.bg-opacity-30.backdrop-blur-sm.rounded-full.p-2
        [:ty-icon.ty-text++.group-hover:translate-x-1.transition-transform
         {:name "zap"
          :size "sm"}]]]
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-3 "Professional Contact Forms"]
       [:p.ty-text-.mb-4.leading-relaxed "Real-time validation with elegant error states and instant visual feedback. Watch semantic colors guide users through form completion with professional UX patterns."]
       [:div.flex.flex-wrap.gap-2.mb-4
        [:ty-tag {:flavor "warning"
                  :size "sm"
                  :pill "false"} "Live Validation"]
        [:ty-tag {:flavor "danger"
                  :size "sm"
                  :pill "false"} "Error States"]
        [:ty-tag {:flavor "success"
                  :size "sm"
                  :pill "false"} "Success Flow"]]
       [:div.flex.justify-between.items-center.group-hover:translate-x-1.transition-transform
        [:span.text-sm.ty-text-warning.font-medium "Try Validation →"]]]]

     ;; Ty Styles Scenario
     [:div.ty-elevated.overflow-hidden.rounded-xl.hover:shadow-xl.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(router/navigate! :ty.site.core/getting-started)}}
      [:div.h-48.ty-bg-secondary-.flex.items-center.justify-center.relative.overflow-hidden
       [:ty-icon.ty-text-secondary.group-hover:scale-110.transition-transform.duration-300
        {:name "palette"
         :size "96"}]
       [:div.absolute.inset-0.bg-gradient-to-br.from-transparent.to-black.opacity-10]
       [:div.absolute.top-4.right-4.ty-surface-floating.bg-opacity-30.backdrop-blur-sm.rounded-full.p-2
        [:ty-icon.ty-text++.group-hover:translate-x-1.transition-transform
         {:name "brush"
          :size "sm"}]]]
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-3 "Semantic Design System"]
       [:p.ty-text-.mb-4.leading-relaxed "Explore the complete ty design system with 5-variant color hierarchies, semantic tokens, and automatic dark/light theme adaptation. See how consistent typography, spacing, and surfaces create cohesive user experiences."]
       [:div.flex.flex-wrap.gap-2.mb-4
        [:ty-tag {:flavor "secondary"
                  :size "sm"
                  :pill "false"} "Color Variants"]
        [:ty-tag {:flavor "neutral"
                  :size "sm"
                  :pill "false"} "Typography Scale"]
        [:ty-tag {:flavor "primary"
                  :size "sm"
                  :pill "false"} "Theme Switching"]]
       [:div.flex.justify-between.items-center.group-hover:translate-x-1.transition-transform
        [:span.text-sm.ty-text-secondary.font-medium "Explore Design System →"]]]]]]

   ;; 💎 WHAT MAKES TY SPECIAL - For Credibility
   [:div.ty-bg-neutral-.rounded-3xl.p-10.lg:p-16.text-center
    [:h2.text-4xl.font-bold.ty-text.mb-6 "What Makes Ty Different?"]
    [:p.text-lg.ty-text-.mb-12.max-w-3xl.mx-auto.leading-relaxed
     "Built from the ground up with semantic design principles, automatic theming, and production-grade UX patterns that just work."]
    [:div.grid.gap-8.md:grid-cols-2.lg:grid-cols-4.max-w-6xl.mx-auto
     [:div.space-y-4
      [:div.w-16.h-16.ty-bg-primary-.rounded-2xl.flex.items-center.justify-center.mx-auto
       [:ty-icon {:name "palette"
                  :size "xl"
                  :class "ty-text-primary"}]]
      [:h4.text-lg.font-bold.ty-text "Semantic Colors"]
      [:p.ty-text-.text-sm.leading-relaxed "5-variant system that adapts automatically to light and dark themes. No more color guessing."]]
     [:div.space-y-4
      [:div.w-16.h-16.ty-bg-success-.rounded-2xl.flex.items-center.justify-center.mx-auto
       [:ty-icon {:name "zap"
                  :size "xl"
                  :class "ty-text-success"}]]
      [:h4.text-lg.font-bold.ty-text "Zero Dependencies"]
      [:p.ty-text-.text-sm.leading-relaxed "Pure web standards that work with React, Vue, vanilla JS, or any framework. Performance built-in."]]
     [:div.space-y-4
      [:div.w-16.h-16.ty-bg-warning-.rounded-2xl.flex.items-center.justify-center.mx-auto
       [:ty-icon {:name "calendar"
                  :size "xl"
                  :class "ty-text-warning"}]]
      [:h4.text-lg.font-bold.ty-text "Advanced Components"]
      [:p.ty-text-.text-sm.leading-relaxed "Calendar orchestration, form validation, and modal management that handles edge cases professionally."]]
     [:div.space-y-4
      [:div.w-16.h-16.ty-bg-neutral-.rounded-2xl.flex.items-center.justify-center.mx-auto
       [:ty-icon {:name "moon"
                  :size "xl"
                  :class "ty-text-neutral"}]]
      [:h4.text-lg.font-bold.ty-text "Auto Theming"]
      [:p.ty-text-.text-sm.leading-relaxed "Dark and light modes that adapt seamlessly without configuration. Your users will thank you."]]]]])
