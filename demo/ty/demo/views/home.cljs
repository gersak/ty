(ns ty.demo.views.home
  (:require [ty.router :as router]))

(defn feature-card [{:keys [title description icon]}]
  [:div.ty-surface-elevated.ty-elevated.p-6.rounded-lg ; SEMANTIC: Elevated card surface with shadow
   [:div.flex.items-center.gap-3.mb-3
    [:ty-icon {:name icon
               :size "lg"
               :class "text-ty-primary"}]
    [:h3.text-lg.font-semibold.ty-text title]] ; NEW: ty-text
   [:p.ty-text- description]]) ; NEW: ty-text- = muted text ; NEW: ty-text- = muted text ; NEW: ty-text- = muted text ; NEW: ty-text- = muted text

(defn code-example [code]
  [:pre.code-block
   [:code code]])

(defn view []
  [:div.max-w-7xl.mx-auto
   ;; ⚡ HERO - Bold & Confident
   [:div.text-center.mb-16.py-8
    [:div.mb-8
     [:h1.text-5xl.lg:text-6xl.font-black.ty-text.mb-6.leading-tight
      "Beautiful Web Components"]
     [:h2.text-2xl.lg:text-3xl.font-light.ty-text-.mb-8.leading-relaxed
      "That Actually Work Everywhere"]]

    [:p.text-xl.ty-text-.max-w-4xl.mx-auto.leading-relaxed.mb-12
     "Production-ready components with semantic design system, automatic theming, and zero framework dependencies. See what's possible when web standards meet thoughtful design."]

    [:div.flex.flex-wrap.gap-3.justify-center.mb-8
     [:span.px-4.py-2.ty-bg-primary-.ty-text-primary.rounded-full.text-sm.font-medium.shadow-sm "✨ 5-Variant Color System"]
     [:span.px-4.py-2.ty-bg-success-.ty-text-success.rounded-full.text-sm.font-medium.shadow-sm "📅 Advanced Calendar"]
     [:span.px-4.py-2.ty-bg-warning-.ty-text-warning.rounded-full.text-sm.font-medium.shadow-sm "🎨 Dark Mode Built-in"]
     [:span.px-4.py-2.ty-bg-info-.ty-text-info.rounded-full.text-sm.font-medium.shadow-sm "🌍 Works Everywhere"]]

    [:div.flex.flex-col.sm:flex-row.gap-4.justify-center
     [:ty-button {:flavor "primary"
                  :filled true
                  :size "lg"
                  :class "text-lg px-8 py-4"
                  :on {:click #(router/navigate! :ty.demo.core/calendar)}}
      [:ty-icon {:name "calendar" :slot "start"}]
      "See Calendar Magic"]
     [:ty-button {:flavor "success"
                  :filled true
                  :size "lg"
                  :class "text-lg px-8 py-4"
                  :on {:click #(router/navigate! :ty.demo.core/color-combinations)}}
      [:ty-icon {:name "droplet" :slot "start"}]
      "Explore Colors"]]]

   ;; 🎯 CLICK-BAIT DEMO TEASERS
   [:div.mb-16
    [:h2.text-3xl.font-bold.ty-text.text-center.mb-12 "See What's Possible"]

    [:div.grid.gap-8.md:grid-cols-2.lg:grid-cols-3
     ;; Calendar Teaser
     [:div.ty-elevated.overflow-hidden.rounded-xl.hover:shadow-xl.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(router/navigate! :ty.demo.core/calendar)}}
      [:div.h-40.ty-bg-primary-.flex.items-center.justify-center.relative.overflow-hidden
       [:ty-icon {:name "calendar"
                  :size "3xl"
                  :class "ty-text-primary group-hover:scale-110 transition-transform duration-300"}]
       [:div.absolute.inset-0.bg-gradient-to-br.from-transparent.to-black.opacity-10]]
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-3 "Smart Calendar System"]
       [:p.ty-text-.mb-4.leading-relaxed "Navigate months and years with smooth animations. Select dates, highlight today, and see how timing libraries create seamless experiences."]
       [:div.flex.justify-between.items-center
        [:span.text-sm.ty-text-primary.font-medium "Interactive Demo →"]
        [:ty-icon {:name "arrow-right"
                   :class "ty-text-primary group-hover:translate-x-1 transition-transform"}]]]]

     ;; Inputs Teaser  
     [:div.ty-elevated.overflow-hidden.rounded-xl.hover:shadow-xl.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(router/navigate! :ty.demo.core/inputs)}}
      [:div.h-40.ty-bg-success-.flex.items-center.justify-center.relative.overflow-hidden
       [:ty-icon {:name "edit-3"
                  :size "3xl"
                  :class "ty-text-success group-hover:scale-110 transition-transform duration-300"}]
       [:div.absolute.inset-0.bg-gradient-to-br.from-transparent.to-black.opacity-10]]
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-3 "Professional Inputs"]
       [:p.ty-text-.mb-4.leading-relaxed "Validation states, formatting, and elegant focus interactions. Watch how semantic colors guide users through form completion."]
       [:div.flex.justify-between.items-center
        [:span.text-sm.ty-text-success.font-medium "Try Inputs →"]
        [:ty-icon {:name "arrow-right"
                   :class "ty-text-success group-hover:translate-x-1 transition-transform"}]]]]

     ;; Dropdowns Teaser
     [:div.ty-elevated.overflow-hidden.rounded-xl.hover:shadow-xl.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(router/navigate! :ty.demo.core/dropdowns)}}
      [:div.h-40.ty-bg-warning-.flex.items-center.justify-center.relative.overflow-hidden
       [:ty-icon {:name "chevron-down"
                  :size "3xl"
                  :class "ty-text-warning group-hover:scale-110 transition-transform duration-300"}]
       [:div.absolute.inset-0.bg-gradient-to-br.from-transparent.to-black.opacity-10]]
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-3 "Rich Dropdowns"]
       [:p.ty-text-.mb-4.leading-relaxed "Options with icons, descriptions, and custom content. Mobile-friendly interactions with smooth animations and keyboard navigation."]
       [:div.flex.justify-between.items-center
        [:span.text-sm.ty-text-warning.font-medium "Open Menu →"]
        [:ty-icon {:name "arrow-right"
                   :class "ty-text-warning group-hover:translate-x-1 transition-transform"}]]]]

     ;; Modal Teaser
     [:div.ty-elevated.overflow-hidden.rounded-xl.hover:shadow-xl.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(router/navigate! :ty.demo.core/modal)}}
      [:div.h-40.ty-bg-danger-.flex.items-center.justify-center.relative.overflow-hidden
       [:ty-icon {:name "square"
                  :size "3xl"
                  :class "ty-text-danger group-hover:scale-110 transition-transform duration-300"}]
       [:div.absolute.inset-0.bg-gradient-to-br.from-transparent.to-black.opacity-10]]
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-3 "Perfect Modals"]
       [:p.ty-text-.mb-4.leading-relaxed "Backdrop blur, escape handling, and focus management. See how modals should behave in modern applications."]
       [:div.flex.justify-between.items-center
        [:span.text-sm.ty-text-danger.font-medium "Launch Modal →"]
        [:ty-icon {:name "arrow-right"
                   :class "ty-text-danger group-hover:translate-x-1 transition-transform"}]]]]

     ;; Icons Teaser
     [:div.ty-elevated.overflow-hidden.rounded-xl.hover:shadow-xl.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(router/navigate! :ty.demo.core/icons)}}
      [:div.h-40.ty-bg-info-.flex.items-center.justify-center.relative.overflow-hidden
       [:ty-icon {:name "image"
                  :size "3xl"
                  :class "ty-text-info group-hover:scale-110 transition-transform duration-300"}]
       [:div.absolute.inset-0.bg-gradient-to-br.from-transparent.to-black.opacity-10]]
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-3 "Icon Library"]
       [:p.ty-text-.mb-4.leading-relaxed "60+ Lucide icons with size variants, animations, and perfect integration. Generated from multiple icon libraries."]
       [:div.flex.justify-between.items-center
        [:span.text-sm.ty-text-info.font-medium "Browse Icons →"]
        [:ty-icon {:name "arrow-right"
                   :class "ty-text-info group-hover:translate-x-1 transition-transform"}]]]]

     ;; Color System Teaser  
     [:div.ty-elevated.overflow-hidden.rounded-xl.hover:shadow-xl.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(router/navigate! :ty.demo.core/color-combinations)}}
      [:div.h-40.ty-bg-secondary-.flex.items-center.justify-center.relative.overflow-hidden
       [:ty-icon {:name "palette"
                  :size "3xl"
                  :class "ty-text-secondary group-hover:scale-110 transition-transform duration-300"}]
       [:div.absolute.inset-0.bg-gradient-to-br.from-transparent.to-black.opacity-10]]
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-3 "Color Magic"]
       [:p.ty-text-.mb-4.leading-relaxed "5-variant semantic system that adapts to light and dark themes automatically. Test all combinations and see the power."]
       [:div.flex.justify-between.items-center
        [:span.text-sm.ty-text-secondary.font-medium "See Colors →"]
        [:ty-icon {:name "arrow-right"
                   :class "ty-text-secondary group-hover:translate-x-1 transition-transform"}]]]]]]

   ;; 💎 WHY TY ROCKS
   [:div.ty-bg-neutral-.rounded-2xl.p-8.lg:p-12.text-center.mb-16
    [:h2.text-3xl.font-bold.ty-text.mb-8 "Why Choose Ty?"]
    [:div.grid.gap-8.md:grid-cols-2.lg:grid-cols-4.max-w-6xl.mx-auto
     [:div.space-y-3
      [:ty-icon {:name "zap" :size "xl" :class "ty-text-primary mx-auto"}]
      [:h4.font-bold.ty-text "Lightning Fast"]
      [:p.ty-text-.text-sm.leading-relaxed "Static CSS, minimal runtime, shared stylesheets. Performance built-in."]]
     [:div.space-y-3
      [:ty-icon {:name "globe" :size "xl" :class "ty-text-success mx-auto"}]
      [:h4.font-bold.ty-text "Universal"]
      [:p.ty-text-.text-sm.leading-relaxed "Works with React, Vue, vanilla JS, or any framework. Web standards win."]]
     [:div.space-y-3
      [:ty-icon {:name "moon" :size "xl" :class "ty-text-warning mx-auto"}]
      [:h4.font-bold.ty-text "Auto Theming"]
      [:p.ty-text-.text-sm.leading-relaxed "Dark mode adaptation without effort. Semantic colors that just work."]]
     [:div.space-y-3
      [:ty-icon {:name "shield" :size "xl" :class "ty-text-danger mx-auto"}]
      [:h4.font-bold.ty-text "Production Ready"]
      [:p.ty-text-.text-sm.leading-relaxed "Form validation, accessibility, keyboard navigation. Built for real apps."]]]]

   ;; 🚀 FINAL CTA
   [:div.text-center.py-12
    [:h2.text-3xl.font-bold.ty-text.mb-6 "Ready to Explore?"]
    [:p.text-lg.ty-text-.mb-8.max-w-2xl.mx-auto "Start with any component and see how Ty makes complex UI simple and beautiful."]
    [:div.grid.gap-3.sm:flex.sm:flex-wrap.justify-center.max-w-4xl.mx-auto
     [:ty-button {:flavor "primary" :outlined true :class "text-sm"
                  :on {:click #(router/navigate! :ty.demo.core/calendar)}}
      "📅 Calendar Demo"]
     [:ty-button {:flavor "success" :outlined true :class "text-sm"
                  :on {:click #(router/navigate! :ty.demo.core/inputs)}}
      "📝 Input Demo"]
     [:ty-button {:flavor "warning" :outlined true :class "text-sm"
                  :on {:click #(router/navigate! :ty.demo.core/dropdowns)}}
      "📋 Dropdown Demo"]
     [:ty-button {:flavor "danger" :outlined true :class "text-sm"
                  :on {:click #(router/navigate! :ty.demo.core/modal)}}
      "⚡ Modal Demo"]
     [:ty-button {:flavor "info" :outlined true :class "text-sm"
                  :on {:click #(router/navigate! :ty.demo.core/icons)}}
      "🎨 Icon Demo"]
     [:ty-button {:flavor "secondary" :outlined true :class "text-sm"
                  :on {:click #(router/navigate! :ty.demo.core/color-combinations)}}
      "🌈 Color Demo"]]]])
