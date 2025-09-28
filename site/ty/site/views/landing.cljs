(ns ty.site.views.landing
  (:require
   [ty.router :as router]
   [ty.site.views.user-profile :as user-profile]
   [ty.site.views.event-booking :as event-booking]
   [ty.site.views.contact-form :as contact-form]
   [ty.site.views.ty-styles :as ty-styles]))

(defn view []
  [:div.max-w-7xl.mx-auto {:id "top"}
   ;; ⚡ HERO - Problem-First, Solution-Focused
   [:div.text-center.mb-16.py-16
    [:h1.text-4xl.lg:text-6xl.font-bold.ty-text.mb-4.leading-tight
     "Stop Rewriting Your UI Every 2 Years"]

    [:p.text-lg.lg:text-xl.ty-text-.max-w-3xl.mx-auto.leading-relaxed.mb-8
     "Ty components work in React today, Vue tomorrow, and whatever comes next. "
     "Built on web standards, not framework opinions. One component system for all your projects."]

    ;; Pain points → Solution
    [:div.grid.md:grid-cols-3.gap-4.max-w-4xl.mx-auto.mb-8
     [:div.text-left.p-4
      [:div.ty-text-danger.font-semibold.mb-2 "❌ The Problem"]
      [:ul.text-sm.ty-text-.space-y-1
       [:li "• React 16→17→18 migrations"]
       [:li "• Maintaining 3 component libraries"]
       [:li "• Vue 2 components dead in Vue 3"]
       [:li "• Next breaking change anxiety"]]]
     [:div.text-left.p-4
      [:div.ty-text-warning.font-semibold.mb-2 "🤔 The Usual \"Solution\""]
      [:ul.text-sm.ty-text-.space-y-1
       [:li "• Wrapper components everywhere"]
       [:li "• Abstract everything (poorly)"]
       [:li "• \"Just use our CSS framework\""]
       [:li "• Hope this time is different"]]]
     [:div.text-left.p-4
      [:div.ty-text-success.font-semibold.mb-2 "✓ The Actual Solution"]
      [:ul.text-sm.ty-text-.space-y-1
       [:li "• Web Components (native browser)"]
       [:li "• Zero framework dependencies"]
       [:li "• Same API in every project"]
       [:li "• Will work in 2030+"]]]]

    [:div.flex.flex-wrap.gap-3.justify-center
     [:ty-tag {:flavor "primary"
               :size "md"} "Web Standards"]
     [:ty-tag {:flavor "success"
               :size "md"} "Production Ready"]
     [:ty-tag {:flavor "warning"
               :size "md"} "80KB gzipped"]
     [:ty-tag {:flavor "neutral"
               :size "md"} "TypeScript Support"]]]

   ;; 📚 LIVE EXAMPLES - Interactive demonstrations
   [:div.mb-20
    [:h2.text-4xl.font-bold.ty-text.text-center.mb-4
     "Try It Live"]
    [:p.text-lg.ty-text-.text-center.mb-12.max-w-3xl.mx-auto
     "Don't just read about it - interact with production-ready components. Each example showcases real-world complexity."]

    ;; User Profile Example
    [:section.mb-16 {:id "user-profile"}
     [:div.mb-8
      [:h3.text-3xl.font-bold.ty-text.mb-2 "User Profile Forms"]
      [:p.text-lg.ty-text-.mb-4
       "Rich dropdowns, multiselect with semantic tags, form validation that actually works."]]

     ;; Embedded preview
     [:div.ty-elevated.p-8.rounded-xl
      (user-profile/view)]]

    ;; Event Booking Example  
    [:section.mb-16 {:id "event-booking"}
     [:div.mb-8
      [:h3.text-3xl.font-bold.ty-text.mb-2 "Event Booking System"]
      [:p.text-lg.ty-text-.mb-4
       "Calendar with custom day rendering, time slots, booking workflow orchestration."]]

     ;; Embedded preview
     [:div.ty-elevated.p-8.rounded-xl
      (event-booking/view)]]

    ;; Contact Form Example
    [:section.mb-16 {:id "contact-form"}
     [:div.mb-8
      [:h3.text-3xl.font-bold.ty-text.mb-2 "Contact Form Validation"]
      [:p.text-lg.ty-text-.mb-4
       "Real-time validation, semantic error states, smooth user experience."]]

     ;; Embedded preview
     [:div.ty-elevated.p-8.rounded-xl
      (contact-form/view)]]]

   ;; 💎 THE BOTTOM LINE
   [:div.ty-bg-neutral-.rounded-3xl.p-10.lg:p-16
    [:h2.text-4xl.font-bold.ty-text.text-center.mb-6
     "Why This Exists"]
    [:p.text-lg.ty-text-.mb-8.max-w-3xl.mx-auto.leading-relaxed.text-center
     "Born from the frustration of rewriting the same components for every new framework version. "
     "Of maintaining multiple component libraries. "
     "Of hearing \"next year it will stabilize\" promises."]

    [:p.text-lg.ty-text.mb-8.max-w-3xl.mx-auto.leading-relaxed.text-center
     "But more importantly, built with hope. "
     "Hope that components can be stable for years, not months. Useful across any framework. "
     "Simple enough that both humans and AI can work with them effortlessly."]

    [:div.text-center.mb-12
     [:p.text-xl.ty-text.font-semibold.mb-2
      "Built on the only thing that's guaranteed:"]
     [:p.text-2xl.ty-text++.font-bold
      "Web Standards."]]

    [:div.text-center.mt-12
     [:a.inline-block.ty-bg-primary.ty-text++.px-8.py-4.rounded-lg.text-lg.font-semibold.hover:shadow-lg.transition-all
      {:href "https://github.com/gersak/ty"}
      "Get Started in 2 Minutes →"]
     [:p.text-sm.ty-text-.mt-4
      "npm install @gersak/ty  |  CDN ready  |  MIT licensed"]]]

   ;; Final proof
   [:div.text-center.py-12
    [:p.text-sm.ty-text-
     "Built for teams who are tired of the framework carousel."]
    [:p.text-xs.ty-text-.mt-2
     "Born from frustration, built with hope, maintained with care."]]])
