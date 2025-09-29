(ns ty.site.views.landing
  (:require
    [ty.router :as router]
    [ty.site.views.contact-form :as contact-form]
    [ty.site.views.event-booking :as event-booking]
    [ty.site.views.user-profile :as user-profile]))

(defn view []
  [:div.max-w-7xl.mx-auto {:id "top"}
   ;; HERO - Work in Progress First, Honest and Direct
   [:div.text-center.mb-16.py-16

    ;; WIP as the MAIN message

;; Simple, direct title
    [:h1.text-3xl.lg:text-5xl.font-bold.ty-text.mb-4
     "ty Web Components"]

    [:p.text-lg.ty-text-.max-w-2xl.mx-auto.mb-8
     "Native web components that work with any framework. "
     "Or no framework at all."]

    [:div.ty-content.border.ty-border--.rounded-xl.p-6.max-w-3xl.mx-auto.mb-8
     [:div.flex.items-start.gap-4
      [:div.ty-bg-warning.ty-border-warning.border.p-2.rounded-lg
       [:ty-icon.ty-text-warning++.flex-shrink-0
        {:name "hammer"
         :size "lg"}]]
      [:div.text-left.space-y-3
       [:h2.text-xl.font-bold.ty-text
        "Work in Progress"]
       [:p.ty-text--
        "ty is actively being developed. Components work, examples run, but expect rough edges. "
        "This is a real project with a real vision - web components that work everywhere, "
        "built on standards that won't break next year."]
       [:p.text-sm.ty-text--
        "If you're tired of framework churn and want to help build something different, "
        "you're in the right place."]]]]

    ;; What works now
    [:div.max-w-3xl.mx-auto.mb-8
     [:p.text-sm.ty-text-.mb-3 "What's working today:"]
     [:div.flex.flex-wrap.gap-2.justify-center
      [:ty-tag {:flavor "success"
                :size "sm"} "Calendar"]
      [:ty-tag {:flavor "success"
                :size "sm"} "Dropdown"]
      [:ty-tag {:flavor "success"
                :size "sm"} "Modal"]
      [:ty-tag {:flavor "success"
                :size "sm"} "Input"]
      [:ty-tag {:flavor "success"
                :size "sm"} "Button"]
      [:ty-tag {:flavor "success"
                :size "sm"} "Multiselect"]
      [:ty-tag {:flavor "success"
                :size "sm"} "Date Picker"]
      [:ty-tag {:flavor "neutral"
                :size "sm"} "+ more"]]]

    ;; Why bother with WIP?
    [:div.ty-elevated.p-8.rounded-xl.max-w-4xl.mx-auto.mb-12
     [:h3.text-lg.font-semibold.ty-text.mb-4
      "Why use something that's still in progress?"]
     [:div.grid.md:grid-cols-2.gap-6
      [:div.space-y-3
       [:div.flex.items-center.gap-3
        [:div.ty-bg-success.ty-border-success.border.p-1.rounded-full.flex-shrink-0
         [:ty-icon.ty-text++ {:name "check"
                              :size "xs"}]]
        [:div
         [:p.text-left.ty-text.text-sm.font-medium "It already works"]
         [:p.ty-text-.text-xs "Components are functional and used in production"]]]
       [:div.flex.items-center.gap-3
        [:div.ty-bg-success.ty-border-success.border.p-1.rounded-full.flex-shrink-0
         [:ty-icon.ty-text++ {:name "check"
                              :size "xs"}]]
        [:div
         [:p.text-left.ty-text.text-sm.font-medium "Built on web standards"]
         [:p.ty-text-.text-xs "Not another abstraction layer that will break"]]]
       [:div.flex.items-center.gap-3
        [:div.ty-bg-success.ty-border-success.border.p-1.rounded-full.flex-shrink-0
         [:ty-icon.ty-text++ {:name "check"
                              :size "xs"}]]
        [:div
         [:p.text-left.ty-text.text-sm.font-medium "Framework agnostic"]
         [:p.ty-text-.text-xs "Use with React, Vue, HTMX, or vanilla HTML"]]]]
      [:div.space-y-3
       [:div.flex.items-center.gap-3
        [:div.ty-bg-success.ty-border-success.border.p-1.rounded-full.flex-shrink-0
         [:ty-icon.ty-text++ {:name "check"
                              :size "xs"}]]
        [:div
         [:p.text-left.ty-text.text-sm.font-medium "No build step required"]
         [:p.ty-text-.text-xs "CDN ready, just add script tag"]]]
       [:div.flex.items-center.gap-3
        [:div.ty-bg-success.ty-border-success.border.p-1.rounded-full.flex-shrink-0
         [:ty-icon.ty-text++ {:name "check"
                              :size "xs"}]]
        [:div
         [:p.text-left.ty-text.text-sm.font-medium "Your input matters"]
         [:p.ty-text-.text-xs "Early adopters shape the direction"]]]
       [:div.flex.items-center.gap-3
        [:div.ty-bg-success.ty-border-success.border.p-1.rounded-full.flex-shrink-0
         [:ty-icon.ty-text++ {:name "check"
                              :size "xs"}]]
        [:div
         [:p.text-left.ty-text.text-sm.font-medium "ClojureScript powered"]
         [:p.ty-text-.text-xs "Built with a language designed to last"]]]]]]

    ;; The vision (brief)
    [:div.text-center.mb-12
     [:h3.text-lg.font-semibold.ty-text.mb-3
      "The Goal"]
     [:p.ty-text-.max-w-2xl.mx-auto
      "Components you write once and use everywhere. "
      "Not tied to React's lifecycle, Vue's reactivity, or Angular's modules. "
      "Just HTML elements that happen to be powerful."]
     [:button.text-sm.ty-text-primary.underline.hover:no-underline.mt-2.cursor-pointer.font-bold
      {:on {:click #(do
                      (router/navigate! :ty.site.core/why)
                      (when-let [main-element (.querySelector js/document "main.overflow-auto")]
                        (.scrollTo main-element #js {:top 0
                                                     :behavior "smooth"})))}}
      "Read why this matters →"]]

    ;; Call to action
    [:div.flex.flex-col.gap-4.items-center
     [:div.flex.gap-3
      [:button.ty-bg-primary.ty-text++.px-6.py-3.rounded-lg.font-semibold.border.ty-border-primary.cursor-pointer
       {:on {:click #(.scrollIntoView
                       (.getElementById js/document "user-profile")
                       #js {:behavior "smooth"})}}
       "See Examples ↓"]
      [:button.ty-bg-success.ty-text++.px-6.py-3.rounded-lg.font-semibold.border.ty-border-success.cursor-pointer.flex.items-center
       {:on {:click #(do
                       (router/navigate! :ty.site.docs/getting-started)
                       (when-let [main-element (.querySelector js/document "main.overflow-auto")]
                         (.scrollTo main-element #js {:top 0
                                                      :behavior "smooth"})))}}
       [:ty-icon.mr-2 {:name "rocket"
                       :size "sm"}]
       "Getting Started"]
      [:a.ty-bg-neutral.ty-text++.px-6.py-3.rounded-lg.font-semibold.flex.items-center.gap-1.hover:ty-bg-secondary+.cursor-pointer.border.ty-border++
       {:href "https://github.com/gersak/ty"}
       [:ty-icon.mr-2 {:name "github"
                       :size "sm"}]
       "Star on GitHub"]]
     [:p.text-xs.ty-text-
      "CDN available • MIT licensed"]]]

   ;; LIVE EXAMPLES - Interactive demonstrations
   [:div.mb-20
    [:div.text-center.mb-12
     [:h2.text-2xl.lg:text-3xl.font-bold.ty-text.mb-3
      "Live Examples"]
     [:p.ty-text-.max-w-2xl.mx-auto
      "Real components solving real problems. Everything you see here is built with ty components. "
      "View source to see how simple it is."]]

    ;; User Profile Example
    [:section.mb-16 {:id "user-profile"}
     [:div.ty-content.rounded-xl.overflow-hidden
      [:div.ty-bg-primary-.px-6.py-4.border-b.ty-border
       [:div.flex.items-center.justify-between
        [:h3.text-lg.font-semibold.ty-text "User Profile Form"]
        [:div.flex.items-center.gap-2
         [:ty-tag {:flavor "success"
                   :size "xs"} "Working"]
         [:button.text-sm.ty-text-primary.underline.hover:no-underline
          {:on {:click #(js/console.log "View source")}}
          "View Source"]]]]
      [:div.p-8
       (user-profile/view)]]]

    ;; Event Booking Example  
    [:section.mb-16 {:id "event-booking"}
     [:div.ty-content.rounded-xl.overflow-hidden
      [:div.ty-bg-primary-.px-6.py-4.border-b.ty-border
       [:div.flex.items-center.justify-between
        [:h3.text-lg.font-semibold.ty-text "Event Booking System"]
        [:div.flex.items-center.gap-2
         [:ty-tag {:flavor "success"
                   :size "xs"} "Working"]
         [:button.text-sm.ty-text-primary.underline.hover:no-underline
          {:on {:click #(js/console.log "View source")}}
          "View Source"]]]]
      [:div.p-8
       (event-booking/view)]]]

    ;; Contact Form Example
    [:section.mb-16 {:id "contact-form"}
     [:div.ty-content.rounded-xl.overflow-hidden
      [:div.ty-bg-primary-.px-6.py-4.border-b.ty-border
       [:div.flex.items-center.justify-between
        [:h3.text-lg.font-semibold.ty-text "Contact Form"]
        [:div.flex.items-center.gap-2
         [:ty-tag {:flavor "success"
                   :size "xs"} "Working"]
         [:button.text-sm.ty-text-primary.underline.hover:no-underline
          {:on {:click #(js/console.log "View source")}}
          "View Source"]]]]
      [:div.p-8
       (contact-form/view)]]]]

   ;; COMMUNITY & NEXT STEPS
   [:section.mb-20
    [:div.ty-bg-neutral-.rounded-2xl.p-8.lg:p-12
     [:div.text-center.mb-8
      [:h2.text-2xl.font-bold.ty-text.mb-3
       "Join the Effort"]
      [:p.ty-text-.max-w-2xl.mx-auto
       "This project grows with community input. Every issue, PR, and discussion helps."]]

     [:div.grid.md:grid-cols-3.gap-6.max-w-4xl.mx-auto
      [:a.ty-elevated.p-6.rounded-xl.hover:shadow-lg.transition-all.block
       {:href "https://github.com/gersak/ty/issues"}
       [:div.flex.items-center.gap-3.mb-3
        [:ty-icon.ty-text-primary {:name "bug"
                                   :size "md"}]
        [:h3.font-semibold.ty-text "Report Issues"]]
       [:p.text-sm.ty-text- "Found a bug? Let us know. Every report helps improve the components."]]

      [:a.ty-elevated.p-6.rounded-xl.hover:shadow-lg.transition-all.block
       {:href "https://github.com/gersak/ty/discussions"}
       [:div.flex.items-center.gap-3.mb-3
        [:ty-icon.ty-text-primary {:name "message-square"
                                   :size "md"}]
        [:h3.font-semibold.ty-text "Discussions"]]
       [:p.text-sm.ty-text- "Share ideas, ask questions, show what you've built."]]

      [:a.ty-elevated.p-6.rounded-xl.hover:shadow-lg.transition-all.block
       {:href "https://github.com/gersak/ty"}
       [:div.flex.items-center.gap-3.mb-3
        [:ty-icon.ty-text-primary {:name "git-branch"
                                   :size "md"}]
        [:h3.font-semibold.ty-text "Contribute"]]
       [:p.text-sm.ty-text- "PRs welcome. Documentation, components, examples - all contributions matter."]]]]]

   ;; Final note
   [:div.text-center.py-12
    [:p.text-sm.ty-text-
     "Built for developers who believe in web standards."]
    [:p.text-xs.ty-text-.mt-2
     "Work in progress. Getting better every day."]]])
