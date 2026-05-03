(ns ty.site.views.landing
  (:require
   [ty.router :as router]
   [ty.site.views.contact-form :as contact-form]
   [ty.site.views.event-booking :as event-booking]
   [ty.site.views.user-profile :as user-profile]))

(defn view []
  [:div.max-w-7xl.mx-auto {:id "top"}
   ;; HERO
   [:div.text-center.mb-16.py-16

    ;; Logo and title
    [:div.flex.items-center.justify-center.gap-2.mb-4
     [:ty-icon {:name "ty-logo"
                :class "ty-text-accent"
                :style {:height "5rem"
                        :width "8rem"
                        :margin-top "4px"}}]
     [:h1.text-3xl.lg:text-5xl.font-bold.ty-text
      "Web Components"]]

    [:p.text-xl.ty-text-.max-w-2xl.mx-auto.mb-4
     "Native web components that work with any framework. "
     "Or no framework at all."]

    ;; Hard numbers
    [:div.flex.flex-wrap.justify-center.gap-6.mb-8
     [:div.text-center
      [:p.text-2xl.font-bold.ty-text "0"]
      [:p.text-xs.ty-text-- "dependencies"]]
     [:div.text-center
      [:p.text-2xl.font-bold.ty-text "~60KB"]
      [:p.text-xs.ty-text-- "total size"]]
     [:div.text-center
      [:p.text-2xl.font-bold.ty-text "1"]
      [:p.text-xs.ty-text-- "script tag"]]]

    ;; Component overview
    [:div.max-w-3xl.mx-auto.mb-8
     [:p.text-sm.ty-text-.mb-3 "21 production-ready components:"]
     [:div.flex.flex-wrap.gap-2.justify-center
      [:ty-tag {:flavor "primary"
                :size "sm"} "Calendar"]
      [:ty-tag {:flavor "primary"
                :size "sm"} "Dropdown"]
      [:ty-tag {:flavor "primary"
                :size "sm"} "Multiselect"]
      [:ty-tag {:flavor "primary"
                :size "sm"} "Date Picker"]
      [:ty-tag {:flavor "primary"
                :size "sm"} "Modal"]
      [:ty-tag {:flavor "primary"
                :size "sm"} "Input"]
      [:ty-tag {:flavor "primary"
                :size "sm"} "Textarea"]
      [:ty-tag {:flavor "primary"
                :size "sm"} "Button"]
      [:ty-tag {:flavor "primary"
                :size "sm"} "Checkbox"]
      [:ty-tag {:flavor "primary"
                :size "sm"} "Tabs"]
      [:ty-tag {:flavor "primary"
                :size "sm"} "Wizard"]
      [:ty-tag {:flavor "primary"
                :size "sm"} "Tooltip"]
      [:ty-tag {:flavor "primary"
                :size "sm"} "Popup"]
      [:ty-tag {:flavor "primary"
                :size "sm"} "Icon"]
      [:ty-tag {:flavor "primary"
                :size "sm"} "Tag"]
      [:ty-tag {:flavor "primary"
                :size "sm"} "Copy"]]]

    ;; Why ty?
    [:div.ty-elevated.p-4.sm:p-8.rounded-xl.max-w-4xl.mx-auto.mb-12
     [:h3.text-lg.font-semibold.ty-text.mb-4
      "Built on standards. Works everywhere."]
     [:div.grid.md:grid-cols-2.gap-6
      [:div.space-y-3
       [:div.flex.items-center.gap-3
        [:div.ty-bg-success.ty-border-success.border.p-1.rounded-full.flex-shrink-0
         [:ty-icon.ty-text++ {:name "check"
                              :size "xs"}]]
        [:div
         [:p.text-left.ty-text.text-sm.font-medium "Web standards, not abstractions"]
         [:p.ty-text-.text-xs "Custom elements and Shadow DOM — no runtime to outgrow"]]]
       [:div.flex.items-center.gap-3
        [:div.ty-bg-success.ty-border-success.border.p-1.rounded-full.flex-shrink-0
         [:ty-icon.ty-text++ {:name "check"
                              :size "xs"}]]
        [:div
         [:p.text-left.ty-text.text-sm.font-medium "Framework agnostic"]
         [:p.ty-text-.text-xs "React, Vue, HTMX, vanilla HTML — your choice"]]]
       [:div.flex.items-center.gap-3
        [:div.ty-bg-success.ty-border-success.border.p-1.rounded-full.flex-shrink-0
         [:ty-icon.ty-text++ {:name "check"
                              :size "xs"}]]
        [:div
         [:p.text-left.ty-text.text-sm.font-medium "No build step required"]
         [:p.ty-text-.text-xs "CDN ready — one script tag and you're running"]]]]
      [:div.space-y-3
       [:div.flex.items-center.gap-3
        [:div.ty-bg-success.ty-border-success.border.p-1.rounded-full.flex-shrink-0
         [:ty-icon.ty-text++ {:name "check"
                              :size "xs"}]]
        [:div
         [:p.text-left.ty-text.text-sm.font-medium "Semantic design system"]
         [:p.ty-text-.text-xs "Surfaces, text hierarchy, and semantic colors built in"]]]
       [:div.flex.items-center.gap-3
        [:div.ty-bg-success.ty-border-success.border.p-1.rounded-full.flex-shrink-0
         [:ty-icon.ty-text++ {:name "check"
                              :size "xs"}]]
        [:div
         [:p.text-left.ty-text.text-sm.font-medium "Mobile-ready"]
         [:p.ty-text-.text-xs "Responsive components with touch-optimized interactions"]]]
       [:div.flex.items-center.gap-3
        [:div.ty-bg-success.ty-border-success.border.p-1.rounded-full.flex-shrink-0
         [:ty-icon.ty-text++ {:name "check"
                              :size "xs"}]]
        [:div
         [:p.text-left.ty-text.text-sm.font-medium "ClojureScript infrastructure"]
         [:p.ty-text-.text-xs "Router, i18n, layout utilities — when you need them"]]]]]]

    ;; The pragmatic pitch
    [:div.text-center.mb-12.max-w-3xl.mx-auto
     [:h3.text-2xl.font-bold.ty-text.mb-4
      "Frameworks change. Components don't."]
     [:p.ty-text-.mb-2
      "React 19, Vue 4, the next big thing — your ty components keep working."]
     [:p.ty-text--
      "Framework-optional, not anti-framework. React wrappers included."]]

    ;; Call to action
    [:div.flex.flex-col.gap-4.items-center
     [:div.flex.flex-col.sm:flex-row.gap-3
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
         [:ty-tag {:flavor "primary"
                   :size "xs"} "Live"]
         [:a.text-sm.ty-text-primary.underline.hover:no-underline.flex.items-center.gap-1
          {:href "https://github.com/gersak/ty/blob/master/packages/cljs/site/ty/site/views/user_profile.cljs"
           :target "_blank"
           :rel "noopener noreferrer"}
          [:ty-icon {:name "external-link"
                     :size "xs"}]
          "View Source"]]]]
      [:div.p-2.sm:p-8
       (user-profile/view)]]]

    ;; Event Booking Example
    [:section.mb-16 {:id "event-booking"}
     [:div.ty-content.rounded-xl.overflow-hidden
      [:div.ty-bg-primary-.px-6.py-4.border-b.ty-border
       [:div.flex.items-center.justify-between
        [:h3.text-lg.font-semibold.ty-text "Event Booking System"]
        [:div.flex.items-center.gap-2
         [:ty-tag {:flavor "primary"
                   :size "xs"} "Live"]
         [:a.text-sm.ty-text-primary.underline.hover:no-underline.flex.items-center.gap-1
          {:href "https://github.com/gersak/ty/blob/master/packages/cljs/site/ty/site/views/event_booking.cljs"
           :target "_blank"
           :rel "noopener noreferrer"}
          [:ty-icon {:name "external-link"
                     :size "xs"}]
          "View Source"]]]]
      [:div.p-2.sm:p-8
       (event-booking/view)]]]

    ;; Contact Form Example
    [:section.mb-16 {:id "contact-form"}
     [:div.ty-content.rounded-xl.overflow-hidden
      [:div.ty-bg-primary-.px-6.py-4.border-b.ty-border
       [:div.flex.items-center.justify-between
        [:h3.text-lg.font-semibold.ty-text "Contact Form"]
        [:div.flex.items-center.gap-2
         [:ty-tag {:flavor "primary"
                   :size "xs"} "Live"]
         [:a.text-sm.ty-text-primary.underline.hover:no-underline.flex.items-center.gap-1
          {:href "https://github.com/gersak/ty/blob/master/packages/cljs/site/ty/site/views/contact_form.cljs"
           :target "_blank"
           :rel "noopener noreferrer"}
          [:ty-icon {:name "external-link"
                     :size "xs"}]
          "View Source"]]]]
      [:div.p-2.sm:p-8
       (contact-form/view)]]]]

   ;; COMMUNITY & NEXT STEPS
   [:section.mb-20
    [:div.rounded-2xl.p-4.sm:p-8.lg:p-12
     [:div.text-center.mb-8
      [:h2.text-2xl.font-bold.ty-text.mb-3
       "Join the Community"]
      [:p.ty-text-.max-w-2xl.mx-auto
       "Ty grows with community input. Every issue, PR, and discussion helps."]]

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
     "MIT licensed. Production ready."]]])
