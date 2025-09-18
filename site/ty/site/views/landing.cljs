(ns ty.site.views.landing
  (:require
   [ty.router :as router]))

(defn view []
  [:div.max-w-7xl.mx-auto
   ;; ⚡ HERO - Pure Messaging, No CTAs
   [:div.text-center.mb-20.py-12
    [:div.mb-10
     [:h1.text-6xl.lg:text-7xl.font-black.ty-text.mb-6.leading-tight
      "Web Components"]
     [:h2.text-6xl.lg:text-7xl.font-black.ty-text-primary.mb-8.leading-tight
      "Done Right"]
     [:h3.text-2xl.lg:text-3xl.font-light.ty-text-.mb-8.leading-relaxed
      "See what's possible with thoughtful design"]]

    [:p.text-xl.ty-text-.max-w-4xl.mx-auto.leading-relaxed.mb-12
     "Experience production-ready components through three stunning scenarios that showcase the power of semantic design, automatic theming, and professional UX patterns."]

    [:div.flex.flex-wrap.gap-3.justify-center
     [:span.px-4.py-2.ty-bg-primary-.ty-text-primary.rounded-full.text-sm.font-medium.shadow-sm "✨ Semantic Design System"]
     [:span.px-4.py-2.ty-bg-success-.ty-text-success.rounded-full.text-sm.font-medium.shadow-sm "📅 Advanced Calendar Workflows"]
     [:span.px-4.py-2.ty-bg-warning-.ty-text-warning.rounded-full.text-sm.font-medium.shadow-sm "⚡ Real-time Form Validation"]
     [:span.px-4.py-2.ty-bg-info-.ty-text-info.rounded-full.text-sm.font-medium.shadow-sm "🌍 Zero Framework Dependencies"]]]

   ;; 🎯 MAIN SHOWCASE - Three Stunning Scenarios
   [:div.mb-20
    [:h2.text-4xl.font-bold.ty-text.text-center.mb-4 "Three Stunning Scenarios"]
    [:p.text-lg.ty-text-.text-center.mb-12.max-w-3xl.mx-auto
     "Each scenario demonstrates how Ty components orchestrate to create seamless, professional user experiences."]

    [:div.grid.gap-10.lg:gap-12
     ;; User Profile Scenario - Full Width Feature
     [:div.ty-elevated.overflow-hidden.rounded-2xl.hover:shadow-2xl.transition-all.duration-500.cursor-pointer.group
      {:on {:click #(router/navigate! :ty.site.core/user-profile)}}
      [:div {:class "lg:flex items-center"}
       [:div {:class "lg:w-1/2 h-64 lg:h-80 ty-bg-primary- flex items-center justify-center relative overflow-hidden"}
        [:ty-icon {:name "user"
                   :size "4xl"
                   :class "ty-text-primary group-hover:scale-110 transition-transform duration-500"}]
        [:div.absolute.inset-0.bg-gradient-to-br.from-transparent.via-transparent.to-black.opacity-20]
        [:div.absolute.top-4.right-4.bg-white.bg-opacity-20.backdrop-blur-sm.rounded-full.p-2
         [:ty-icon {:name "arrow-right" :class "text-white group-hover:translate-x-1 transition-transform"}]]]
       [:div {:class "p-8 lg:w-1/2"}
        [:h3.text-2xl.lg:text-3xl.font-bold.ty-text.mb-4 "Rich User Profile Forms"]
        [:p.ty-text-.text-lg.mb-6.leading-relaxed
         "Witness dropdowns filled with beautiful HTML content - country flags, role badges, and contextual information. See multiselect components showcase colorful skill tags with semantic meanings. Experience modal integration and professional form validation patterns."]
        [:div.flex.flex-wrap.gap-2.mb-6
         [:span.px-3.py-1.ty-bg-primary-.ty-text-primary.rounded-full.text-sm.font-medium "Rich Dropdowns"]
         [:span.px-3.py-1.ty-bg-success-.ty-text-success.rounded-full.text-sm.font-medium "Visual Multiselects"]
         [:span.px-3.py-1.ty-bg-warning-.ty-text-warning.rounded-full.text-sm.font-medium "Modal Integration"]]
        [:div.flex.items-center.ty-text-primary.font-semibold.text-lg
         [:span "Explore Profile Demo"]
         [:ty-icon {:name "arrow-right" :class "ml-2 group-hover:translate-x-2 transition-transform"}]]]]]

     ;; Two Column Layout for Remaining Scenarios
     [:div.grid.gap-8.lg:grid-cols-2
      ;; Event Booking Scenario
      [:div.ty-elevated.overflow-hidden.rounded-xl.hover:shadow-xl.transition-all.duration-300.cursor-pointer.group
       {:on {:click #(router/navigate! :ty.site.core/event-booking)}}
       [:div.h-48.ty-bg-success-.flex.items-center.justify-center.relative.overflow-hidden
        [:ty-icon {:name "calendar"
                   :size "3xl"
                   :class "ty-text-success group-hover:scale-110 transition-transform duration-300"}]
        [:div.absolute.inset-0.bg-gradient-to-br.from-transparent.to-black.opacity-10]
        [:div.absolute.top-4.right-4.bg-white.bg-opacity-30.backdrop-blur-sm.rounded-full.p-2
         [:ty-icon {:name "external-link" :size "sm" :class "text-white"}]]]
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-3 "Event Booking Experience"]
        [:p.ty-text-.mb-4.leading-relaxed "Interactive calendar orchestration with date selection, time slot management, and service customization. See how components work together to create seamless booking workflows."]
        [:div.flex.flex-wrap.gap-2.mb-4
         [:span.px-2.py-1.ty-bg-success-.ty-text-success.rounded.text-xs.font-medium "Calendar Integration"]
         [:span.px-2.py-1.ty-bg-info-.ty-text-info.rounded.text-xs.font-medium "Service Selection"]
         [:span.px-2.py-1.ty-bg-warning-.ty-text-warning.rounded.text-xs.font-medium "Booking Workflows"]]
        [:div.flex.justify-between.items-center
         [:span.text-sm.ty-text-success.font-medium "Launch Experience →"]
         [:ty-icon {:name "arrow-right"
                    :class "ty-text-success group-hover:translate-x-1 transition-transform"}]]]]

      ;; Contact Form Scenario
      [:div.ty-elevated.overflow-hidden.rounded-xl.hover:shadow-xl.transition-all.duration-300.cursor-pointer.group
       {:on {:click #(router/navigate! :ty.site.core/contact-form)}}
       [:div.h-48.ty-bg-warning-.flex.items-center.justify-center.relative.overflow-hidden
        [:ty-icon {:name "mail"
                   :size "3xl"
                   :class "ty-text-warning group-hover:scale-110 transition-transform duration-300"}]
        [:div.absolute.inset-0.bg-gradient-to-br.from-transparent.to-black.opacity-10]
        [:div.absolute.top-4.right-4.bg-white.bg-opacity-30.backdrop-blur-sm.rounded-full.p-2
         [:ty-icon {:name "zap" :size "sm" :class "text-white"}]]]
       [:div.p-6
        [:h3.text-xl.font-bold.ty-text.mb-3 "Professional Contact Forms"]
        [:p.ty-text-.mb-4.leading-relaxed "Real-time validation with elegant error states and instant visual feedback. Watch semantic colors guide users through form completion with professional UX patterns."]
        [:div.flex.flex-wrap.gap-2.mb-4
         [:span.px-2.py-1.ty-bg-warning-.ty-text-warning.rounded.text-xs.font-medium "Live Validation"]
         [:span.px-2.py-1.ty-bg-danger-.ty-text-danger.rounded.text-xs.font-medium "Error States"]
         [:span.px-2.py-1.ty-bg-success-.ty-text-success.rounded.text-xs.font-medium "Success Flow"]]
        [:div.flex.justify-between.items-center
         [:span.text-sm.ty-text-warning.font-medium "Try Validation →"]
         [:ty-icon {:name "arrow-right"
                    :class "ty-text-warning group-hover:translate-x-1 transition-transform"}]]]]]]]

   ;; 💎 WHAT MAKES TY SPECIAL - For Credibility
   [:div.ty-bg-neutral-.rounded-3xl.p-10.lg:p-16.text-center
    [:h2.text-4xl.font-bold.ty-text.mb-6 "What Makes Ty Different?"]
    [:p.text-lg.ty-text-.mb-12.max-w-3xl.mx-auto.leading-relaxed
     "Built from the ground up with semantic design principles, automatic theming, and production-grade UX patterns that just work."]
    [:div.grid.gap-8.md:grid-cols-2.lg:grid-cols-4.max-w-6xl.mx-auto
     [:div.space-y-4
      [:div.w-16.h-16.ty-bg-primary-.rounded-2xl.flex.items-center.justify-center.mx-auto
       [:ty-icon {:name "palette" :size "xl" :class "ty-text-primary"}]]
      [:h4.text-lg.font-bold.ty-text "Semantic Colors"]
      [:p.ty-text-.text-sm.leading-relaxed "5-variant system that adapts automatically to light and dark themes. No more color guessing."]]
     [:div.space-y-4
      [:div.w-16.h-16.ty-bg-success-.rounded-2xl.flex.items-center.justify-center.mx-auto
       [:ty-icon {:name "zap" :size "xl" :class "ty-text-success"}]]
      [:h4.text-lg.font-bold.ty-text "Zero Dependencies"]
      [:p.ty-text-.text-sm.leading-relaxed "Pure web standards that work with React, Vue, vanilla JS, or any framework. Performance built-in."]]
     [:div.space-y-4
      [:div.w-16.h-16.ty-bg-warning-.rounded-2xl.flex.items-center.justify-center.mx-auto
       [:ty-icon {:name "calendar" :size "xl" :class "ty-text-warning"}]]
      [:h4.text-lg.font-bold.ty-text "Advanced Components"]
      [:p.ty-text-.text-sm.leading-relaxed "Calendar orchestration, form validation, and modal management that handles edge cases professionally."]]
     [:div.space-y-4
      [:div.w-16.h-16.ty-bg-info-.rounded-2xl.flex.items-center.justify-center.mx-auto
       [:ty-icon {:name "moon" :size "xl" :class "ty-text-info"}]]
      [:h4.text-lg.font-bold.ty-text "Auto Theming"]
      [:p.ty-text-.text-sm.leading-relaxed "Dark and light modes that adapt seamlessly without configuration. Your users will thank you."]]]]])
