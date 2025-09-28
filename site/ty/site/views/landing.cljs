(ns ty.site.views.landing
  (:require
   [ty.router :as router]
   [ty.site.views.user-profile :as user-profile]
   [ty.site.views.event-booking :as event-booking]
   [ty.site.views.contact-form :as contact-form]
   [ty.site.views.ty-styles :as ty-styles]))

(defn view []
  [:div.max-w-7xl.mx-auto
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

   [:div.mb-20
    [:h2.text-4xl.font-bold.ty-text.text-center.mb-4
     "Components That Handle Real Complexity"]
    [:p.text-lg.ty-text-.text-center.mb-12.max-w-3xl.mx-auto
     "Not just another button library. These are production components that handle the messy reality of web apps."]

    [:div.grid.gap-6.md:grid-cols-2.max-w-3xl.mx-auto
     ;; User Profile - Complex Forms
     [:div.ty-elevated.overflow-hidden.rounded-xl.hover:shadow-xl.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(router/navigate! :ty.site.core/user-profile)}}
      [:div.h-48.ty-bg-primary-.flex.items-center.justify-center.relative.overflow-hidden
       [:ty-icon.ty-text-primary.group-hover:scale-110.transition-transform.duration-300
        {:name "user"
         :size "96"}]
       [:div.absolute.inset-0.bg-gradient-to-br.from-transparent.to-black.opacity-10]
       [:div.absolute.top-4.right-4.ty-surface-floating.bg-opacity-20.backdrop-blur-sm.rounded-full.p-2
        [:ty-icon.ty-text++.group-hover:translate-x-1.transition-transform
         {:name "arrow-right"}]]]
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-3 "Forms That Actually Work"]
       [:p.ty-text-.mb-4.leading-relaxed
        "Dropdowns with HTML content (flags, avatars). Multiselect with rich tags. "
        "Native form submission. Validation that makes sense. Try breaking it."]
       [:div.flex.flex-wrap.gap-2.mb-4
        [:ty-tag {:flavor "success"} "form-associated"]
        [:ty-tag {:flavor "neutral"} "native validation"]
        [:ty-tag {:flavor "warning"} "a11y compliant"]]
       [:div.flex.justify-between.items-center.group-hover:translate-x-1.transition-transform
        [:span.text-sm.ty-text-primary.font-medium "See Real Form →"]]]]

     ;; Calendar That Works
     [:div.ty-elevated.overflow-hidden.rounded-xl.hover:shadow-xl.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(router/navigate! :ty.site.core/event-booking)}}
      [:div.h-48.ty-bg-success-.flex.items-center.justify-center.relative.overflow-hidden
       [:ty-icon.ty-text-success.group-hover:scale-110.transition-transform.duration-300
        {:name "calendar"
         :size "96"}]
       [:div.absolute.inset-0.bg-gradient-to-br.from-transparent.to-black.opacity-10]
       [:div.absolute.top-4.right-4.ty-surface-floating.bg-opacity-30.backdrop-blur-sm.rounded-full.p-2
        [:ty-icon.ty-text++.group-hover:translate-x-1.transition-transform
         {:name "external-link"
          :size "sm"}]]]
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-3 "Calendar That Handles Edge Cases"]
       [:p.ty-text-.mb-4.leading-relaxed
        "Leap years, timezones, locale formats, keyboard navigation. "
        "Custom day rendering. Booking workflows. It all just works."]
       [:div.flex.flex-wrap.gap-2.mb-4
        [:ty-tag {:flavor "primary"} "130+ locales"]
        [:ty-tag {:flavor "neutral"} "keyboard nav"]
        [:ty-tag {:flavor "success"} "custom rendering"]]
       [:div.flex.justify-between.items-center.group-hover:translate-x-1.transition-transform
        [:span.text-sm.ty-text-success.font-medium "Try Calendar →"]]]]

     ;; Validation That Makes Sense
     [:div.ty-elevated.overflow-hidden.rounded-xl.hover:shadow-xl.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(router/navigate! :ty.site.core/contact-form)}}
      [:div.h-48.ty-bg-warning-.flex.items-center.justify-center.relative.overflow-hidden
       [:ty-icon.ty-text-warning.group-hover:scale-110.transition-transform.duration-300
        {:name "mail"
         :size "96"}]
       [:div.absolute.inset-0.bg-gradient-to-br.from-transparent.to-black.opacity-10]
       [:div.absolute.top-4.right-4.ty-surface-floating.bg-opacity-30.backdrop-blur-sm.rounded-full.p-2
        [:ty-icon.ty-text++.group-hover:translate-x-1.transition-transform
         {:name "zap"
          :size "sm"}]]]
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-3 "Validation Without the Pain"]
       [:p.ty-text-.mb-4.leading-relaxed
        "Real-time feedback that doesn't annoy. Semantic error states. "
        "Works with native HTML validation or your custom logic."]
       [:div.flex.flex-wrap.gap-2.mb-4
        [:ty-tag {:flavor "primary"} "instant feedback"]
        [:ty-tag {:flavor "secondary"} "semantic colors"]
        [:ty-tag {:flavor "success"} "custom + native"]]
       [:div.flex.justify-between.items-center.group-hover:translate-x-1.transition-transform
        [:span.text-sm.ty-text-warning.font-medium "Test Validation →"]]]]

     ;; Design System
     [:div.ty-elevated.overflow-hidden.rounded-xl.hover:shadow-xl.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(router/navigate! :ty.site.core/ty-styles)}}
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
       [:h3.text-xl.font-bold.ty-text.mb-3 "Design System That Scales"]
       [:p.ty-text-.mb-4.leading-relaxed
        "Semantic colors that mean something. 5 emphasis levels. "
        "Dark mode that actually works. One CSS file, infinite customization."]
       [:div.flex.flex-wrap.gap-2.mb-4
        [:ty-tag {:flavor "neutral"} "CSS variables"]
        [:ty-tag {:flavor "neutral"} "auto dark mode"]
        [:ty-tag {:flavor "neutral"} "semantic tokens"]]
       [:div.flex.justify-between.items-center.group-hover:translate-x-1.transition-transform
        [:span.text-sm.ty-text-secondary.font-medium "Explore System →"]]]]]]
   [:div.mb-20
    [:h2.text-4xl.font-bold.ty-text.text-center.mb-4
     "Components That Handle Real Complexity"]
    [:p.text-lg.ty-text-.text-center.mb-12.max-w-3xl.mx-auto
     "Not just another button library. These are production components that handle the messy reality of web apps."]

    [:div.grid.gap-6.md:grid-cols-2.max-w-3xl.mx-auto
     ;; Flask + HTMX Example
     [:div.ty-elevated.p-6.rounded-xl.hover:shadow-lg.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(.open js/window "https://github.com/gersak/ty/tree/main/examples/htmx-flask" "_blank")}}
      [:div.flex.items-center.justify-center.mb-4
       [:div.w-12.h-12.ty-bg-danger-.rounded-xl.flex.items-center.justify-center.mr-3
        [:ty-icon.ty-text-danger {:name "python"
                                  :size "lg"}]]
       [:div.w-12.h-12.ty-bg-info-.rounded-xl.flex.items-center.justify-center
        [:ty-icon.ty-text-info {:name "zap"
                                :size "lg"}]]]
      [:h4.font-bold.ty-text.text-center.mb-2 "Server-Side Python"]
      [:p.text-sm.ty-text-.text-center.mb-4.leading-relaxed
       "Native HTML. Zero JavaScript build. Web Components work directly with HTMX."]
      [:div.flex.justify-center.gap-2
       [:code.text-xs.ty-bg-neutral-.px-2.py-1.rounded
        "<ty-calendar name=\"date\">"]]]

     ;; React + Next.js Example  
     [:div.ty-elevated.p-6.rounded-xl.hover:shadow-lg.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(.open js/window "https://github.com/gersak/ty/tree/main/examples/react-nextjs" "_blank")}}
      [:div.flex.items-center.justify-center.mb-4
       [:div.w-12.h-12.ty-bg-primary-.rounded-xl.flex.items-center.justify-center.mr-3
        [:ty-icon.ty-text-primary {:name "react"
                                   :size "lg"}]]
       [:div.w-12.h-12.ty-bg-neutral-.rounded-xl.flex.items-center.justify-center
        [:ty-icon.ty-text-neutral {:name "node-js"
                                   :size "lg"}]]]
      [:h4.font-bold.ty-text.text-center.mb-2 "React + TypeScript"]
      [:p.text-sm.ty-text-.text-center.mb-4.leading-relaxed
       "TypeScript wrappers handle events. Works in React 16-19. Same underlying components."]
      [:div.flex.justify-center.gap-2
       [:code.text-xs.ty-bg-neutral-.px-2.py-1.rounded
        "<TyCalendar onChange={handleDate} />"]]]

     ;; ClojureScript Reagent Example
     [:div.ty-elevated.p-6.rounded-xl.hover:shadow-lg.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(.open js/window "https://github.com/gersak/ty/tree/main/examples/reagent" "_blank")}}
      [:div.flex.items-center.justify-center.mb-4
       [:div.w-12.h-12.ty-bg-success-.rounded-xl.flex.items-center.justify-center.mr-3
        [:ty-icon.ty-text-success {:name "code"
                                   :size "lg"}]]
       [:div.w-12.h-12.ty-bg-success-.rounded-xl.flex.items-center.justify-center
        [:ty-icon.ty-text-success {:name "atom"
                                   :size "lg"}]]]
      [:h4.font-bold.ty-text.text-center.mb-2 "ClojureScript"]
      [:p.text-sm.ty-text-.text-center.mb-4.leading-relaxed
       "Direct HTML elements. Native custom events. Plus routing and i18n if you want."]
      [:div.flex.justify-center.gap-2
       [:code.text-xs.ty-bg-neutral-.px-2.py-1.rounded
        "[:ty-calendar {:on-change handler}]"]]]

     ;; Vanilla JS
     [:div.ty-elevated.p-6.rounded-xl.hover:shadow-lg.transition-all.duration-300.cursor-pointer.group
      {:on {:click #(.open js/window "https://github.com/gersak/ty/tree/main/examples/vanilla" "_blank")}}
      [:div.flex.items-center.justify-center.mb-4
       [:div.w-12.h-12.ty-bg-warning-.rounded-xl.flex.items-center.justify-center.mr-3
        [:ty-icon.ty-text-warning {:name "file-code"
                                   :size "lg"}]]
       [:div.w-12.h-12.ty-bg-warning-.rounded-xl.flex.items-center.justify-center
        [:ty-icon.ty-text-warning {:name "globe"
                                   :size "lg"}]]]
      [:h4.font-bold.ty-text.text-center.mb-2 "Vanilla JavaScript"]
      [:p.text-sm.ty-text-.text-center.mb-4.leading-relaxed
       "Pure Web Components. Native DOM events. No framework, no wrappers, just standards."]
      [:div.flex.justify-center.gap-2
       [:code.text-xs.ty-bg-neutral-.px-2.py-1.rounded
        "el.addEventListener('date-select', ...)"]]]]

    ;; Why React needs wrappers
    [:div.text-center.mt-8
     [:details.inline-block.text-left
      [:summary.cursor-pointer.ty-text-primary.text-sm.hover:underline
       "Why does React need wrappers?"]
      [:div.ty-bg-neutral-.p-4.rounded-lg.mt-2.max-w-2xl.text-sm.ty-text-
       [:p.mb-2
        "React doesn't handle Web Component custom events natively (onChange vs addEventListener). "
        "Thin TypeScript wrappers (@gersak/ty-react) handle:"]
       [:ul.list-disc.list-inside.space-y-1.ml-2
        [:li "Custom event binding (onChange → addEventListener)"]
        [:li "Property vs attribute mapping"]
        [:li "TypeScript definitions"]
        [:li "Imperative methods (ref.current.show())"]
        [:li "React 16-19 compatibility"]]
       [:p.mt-2.text-xs
        "These wrappers are provided - no need to write them. The Web Components remain unchanged."]]]]]

   ;; 🧩 FOR CLOJURESCRIPT DEVELOPERS - First-class, not an afterthought
   [:div.mb-20
    [:h2.text-3xl.font-bold.ty-text.text-center.mb-4
     "First-Class ClojureScript Support"]
    [:p.text-lg.ty-text-.text-center.mb-12.max-w-3xl.mx-auto
     "If you're using ClojureScript, you get even more. Not required, but nice to have."]

    [:div.grid.gap-8.md:grid-cols-2.lg:grid-cols-3.max-w-5xl.mx-auto
     ;; Built-in Router
     [:div.ty-elevated.p-8.rounded-xl
      [:div.flex.items-start.gap-4
       [:div.w-12.h-12.ty-bg-primary-.rounded-xl.flex.items-center.justify-center.flex-shrink-0
        [:ty-icon.ty-text-primary {:name "map-pin"
                                   :size "lg"}]]
       [:div.flex-1
        [:h4.text-lg.font-bold.ty-text.mb-2 "Tree-Based Router"]
        [:p.ty-text-.text-sm.mb-3.leading-relaxed
         "Authorization, query params, landing pages. One less npm package."]
        [:code.text-xs.ty-bg-neutral-.px-2.py-1.rounded.block.mb-1
         "(router/navigate! ::profile {:tab :settings})"]
        [:p.text-xs.ty-text-.mt-2
         "Zero dependencies. 4KB gzipped."]]]]

     ;; Native i18n
     [:div.ty-elevated.p-8.rounded-xl
      [:div.flex.items-start.gap-4
       [:div.w-12.h-12.ty-bg-success-.rounded-xl.flex.items-center.justify-center.flex-shrink-0
        [:ty-icon.ty-text-success {:name "globe"
                                   :size "lg"}]]
       [:div.flex-1
        [:h4.text-lg.font-bold.ty-text.mb-2 "Protocol-Based i18n"]
        [:p.ty-text-.text-sm.mb-3.leading-relaxed
         "Extend any type. 130+ locales. Browser Intl API."]
        [:code.text-xs.ty-bg-neutral-.px-2.py-1.rounded.block.mb-1
         "(t 1234.56 :currency \"EUR\") ; → \"€1,234.56\""]
        [:p.text-xs.ty-text-.mt-2
         "No JSON files. Just protocols."]]]]

     ;; Lazy Loading
     [:div.ty-elevated.p-8.rounded-xl
      [:div.flex.items-start.gap-4
       [:div.w-12.h-12.ty-bg-warning-.rounded-xl.flex.items-center.justify-center.flex-shrink-0
        [:ty-icon.ty-text-warning {:name "zap"
                                   :size "lg"}]]
       [:div.flex-1
        [:h4.text-lg.font-bold.ty-text.mb-2 "Shadow-cljs Optimized"]
        [:p.ty-text-.text-sm.mb-3.leading-relaxed
         "Code splitting, lazy loading, dead code elimination."]
        [:code.text-xs.ty-bg-neutral-.px-2.py-1.rounded.block.mb-1
         "(lazy/loadable ty.components.calendar)"]
        [:p.text-xs.ty-text-.mt-2
         "Only load what you use."]]]]

     ;; Google Closure
     [:div.ty-elevated.p-8.rounded-xl
      [:div.flex.items-start.gap-4
       [:div.w-12.h-12.ty-bg-danger-.rounded-xl.flex.items-center.justify-center.flex-shrink-0
        [:ty-icon.ty-text-danger {:name "package"
                                  :size "lg"}]]
       [:div.flex-1
        [:h4.text-lg.font-bold.ty-text.mb-2 "Closure Compiled"]
        [:p.ty-text-.text-sm.mb-3.leading-relaxed
         "Advanced optimizations. Smallest possible bundles."]
        [:code.text-xs.ty-bg-neutral-.px-2.py-1.rounded.block.mb-1
         ";; Button only: 12KB total"]
        [:p.text-xs.ty-text-.mt-2
         "Tree shaking that actually works."]]]]

     ;; AI-Ready Components
     [:div.ty-elevated.p-8.rounded-xl
      [:div.flex.items-start.gap-4
       [:div.w-12.h-12.ty-bg-info-.rounded-xl.flex.items-center.justify-center.flex-shrink-0
        [:ty-icon.ty-text-info {:name "sparkles"
                                :size "lg"}]]
       [:div.flex-1
        [:h4.text-lg.font-bold.ty-text.mb-2 "AI-Friendly Design"]
        [:p.ty-text-.text-sm.mb-3.leading-relaxed
         "Simple HTML elements. Standard events. No magic. "
         "AI assistants understand ty components correctly."]
        [:code.text-xs.ty-bg-neutral-.px-2.py-1.rounded.block.mb-1
         "<ty-button>Click me</ty-button>"]
        [:p.text-xs.ty-text-.mt-2
         "Web standards are universal. Even for AI."]]]]

     ;; 2000+ Icons Built-in
     [:div.ty-elevated.p-8.rounded-xl
      [:div.flex.items-start.gap-4
       [:div.w-12.h-12.ty-bg-secondary-.rounded-xl.flex.items-center.justify-center.flex-shrink-0
        [:ty-icon.ty-text-secondary {:name "package"
                                     :size "lg"}]]
       [:div.flex-1
        [:h4.text-lg.font-bold.ty-text.mb-2 "2000+ Icons Built-in"]
        [:p.ty-text-.text-sm.mb-3.leading-relaxed
         "No npm installs. No file downloads. Icons ported to ClojureScript, Closure compiler optimized."]
        [:div.text-xs.ty-text-.space-y-1.mb-2
         [:div "• 1850+ Lucide icons"]
         [:div "• Font Awesome 6 (Solid, Regular, Brands)"]
         [:div "• Material Icons (Filled, Outlined)"]
         [:div "• Heroicons"]
         [:div "• Custom UI icons"]]
        [:code.text-xs.ty-bg-neutral-.px-2.py-1.rounded.block
         "[:ty-icon {:name \"lucide-rocket\"}]"]
        [:p.text-xs.ty-text-.mt-2
         "Tree-shaken. Only bundle what you use."]]]]]]

   ;; 📚 LIVE EXAMPLES - Interactive demonstrations
   [:div.mb-20
    [:h2.text-4xl.font-bold.ty-text.text-center.mb-4
     "Try It Live"]
    [:p.text-lg.ty-text-.text-center.mb-12.max-w-3xl.mx-auto
     "Don't just read about it - interact with production-ready components. Each example showcases real-world complexity."]

    ;; User Profile Example
    [:section.mb-16 {:id "user-profile"}
     [:div.flex.items-center.justify-between.mb-8
      [:div
       [:h3.text-3xl.font-bold.ty-text.mb-2 "User Profile Forms"]
       [:p.text-lg.ty-text-.mb-4
        "Rich dropdowns, multiselect with semantic tags, form validation that actually works."]]
      [:a.ty-bg-primary.ty-text++.px-6.py-3.rounded-lg.font-semibold.hover:shadow-lg.transition-all
       {:href "#user-profile-full"
        :on {:click #(router/navigate! :ty.site.core/user-profile)}}
       "View Full Example →"]]

     ;; Embedded preview
     [:div.ty-elevated.p-8.rounded-xl
      (user-profile/view)]]

    ;; Event Booking Example  
    [:section.mb-16 {:id "event-booking"}
     [:div.flex.items-center.justify-between.mb-8
      [:div
       [:h3.text-3xl.font-bold.ty-text.mb-2 "Event Booking System"]
       [:p.text-lg.ty-text-.mb-4
        "Calendar with custom day rendering, time slots, booking workflow orchestration."]]
      [:a.ty-bg-success.ty-text++.px-6.py-3.rounded-lg.font-semibold.hover:shadow-lg.transition-all
       {:href "#event-booking-full"
        :on {:click #(router/navigate! :ty.site.core/event-booking)}}
       "View Full Example →"]]

     ;; Embedded preview
     [:div.ty-elevated.p-8.rounded-xl
      (event-booking/view)]]

    ;; Contact Form Example
    [:section.mb-16 {:id "contact-form"}
     [:div.flex.items-center.justify-between.mb-8
      [:div
       [:h3.text-3xl.font-bold.ty-text.mb-2 "Contact Form Validation"]
       [:p.text-lg.ty-text-.mb-4
        "Real-time validation, semantic error states, smooth user experience."]]
      [:a.ty-bg-warning.ty-text++.px-6.py-3.rounded-lg.font-semibold.hover:shadow-lg.transition-all
       {:href "#contact-form-full"
        :on {:click #(router/navigate! :ty.site.core/contact-form)}}
       "View Full Example →"]]

     ;; Embedded preview
     [:div.ty-elevated.p-8.rounded-xl
      (contact-form/view)]]

    ;; Style System Example
    [:section.mb-16 {:id "ty-styles"}
     [:div.flex.items-center.justify-between.mb-8
      [:div
       [:h3.text-3xl.font-bold.ty-text.mb-2 "Design System"]
       [:p.text-lg.ty-text-.mb-4
        "Semantic colors, 5-variant system, automatic dark mode, CSS variables."]]
      [:a.ty-bg-secondary.ty-text++.px-6.py-3.rounded-lg.font-semibold.hover:shadow-lg.transition-all
       {:href "#ty-styles-full"
        :on {:click #(router/navigate! :ty.site.core/ty-styles)}}
       "Explore System →"]]

     ;; Embedded preview
     [:div.ty-elevated.p-8.rounded-xl
      (ty-styles/view)]]]

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

    [:div.grid.gap-6.md:grid-cols-3.max-w-4xl.mx-auto
     ;; What You Get
     [:div.ty-floating.p-6.rounded-xl.text-center
      [:h3.text-lg.font-bold.ty-text.mb-3 "What You Get"]
      [:ul.text-sm.ty-text-.text-left.space-y-2
       [:li "✓ Components that work everywhere"]
       [:li "✓ No framework migrations"]
       [:li "✓ TypeScript definitions"]
       [:li "✓ Semantic design system"]
       [:li "✓ Dark mode built-in"]
       [:li "✓ 80KB gzipped"]
       [:li "✓ AI-friendly simplicity"]]]

     ;; What You Don't Get
     [:div.ty-floating.p-6.rounded-xl.text-center
      [:h3.text-lg.font-bold.ty-text.mb-3 "What You Don't Get"]
      [:ul.text-sm.ty-text-.text-left.space-y-2
       [:li "✗ React 19 breaking changes"]
       [:li "✗ Vue 2→3 rewrites"]
       [:li "✗ Angular version hell"]
       [:li "✗ Next.js 15 surprises"]
       [:li "✗ Vendor lock-in"]
       [:li "✗ Migration anxiety"]]]

     ;; Who It's For
     [:div.ty-floating.p-6.rounded-xl.text-center
      [:h3.text-lg.font-bold.ty-text.mb-3 "Who It's For"]
      [:ul.text-sm.ty-text-.text-left.space-y-2
       [:li "• Teams with multiple projects"]
       [:li "• Developers tired of churn"]
       [:li "• Companies wanting stability"]
       [:li "• Anyone building for the long term"]
       [:li "• You, if you're still reading"]
       [:li "• (Seriously, try it)"]]]]

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
