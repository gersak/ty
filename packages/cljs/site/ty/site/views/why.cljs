(ns ty.site.views.why
  "The story behind ty - why it was created and what it stands for"
  (:require [ty.site.docs.common :refer [docs-page]]))

(defn view []
  (docs-page
   [:div.space-y-16

   ;; Header - More dramatic
   [:div.text-center.space-y-6.py-12
    [:div.inline-block.ty-bg-primary-.px-4.py-2.rounded-full.mb-4
     [:span.text-sm.ty-text-primary++.font-semibold.uppercase.tracking-wide "The Story"]]
    [:h1.text-5xl.lg:text-6xl.font-bold.ty-text.leading-tight
     "Why ty?"]
    [:p.text-2xl.ty-text-.max-w-2xl.mx-auto.leading-relaxed
     "Choosing standards over revolutions, and clarity over chaos."]]

   ;; The Framework Wars
   [:section.ty-elevated.p-12.rounded-xl.space-y-4.mb-6
    [:div.flex.items-center.gap-2.mb-4
     [:div.ty-bg-danger-.p-3.rounded-lg
      [:ty-icon.ty-text-danger++ {:name "swords"
                                  :size "lg"}]]
     [:h2.text-xl.font-bold.ty-text
      "Ecosystem Battles"]]
    [:p.text.ty-text.leading-relaxed
     "Flutter vs React vs Vue vs Svelte vs HTMX vs the next thing."]
    [:p.text.ty-text.leading-relaxed
     "The marketing is overwhelming, and the flood of content makes you feel like you " [:strong "must"] " choose a side. "]

    [:p.text.ty-text.leading-relaxed
     "Each framework is pitched as a " [:strong "revolution "] ", but what’s really happening is a battle for the same ground: your browser, your time, your commitment."]]

   ;; Pull Quote
   [:div.text-center.py-8
    [:blockquote.text-3xl.lg:text-4xl.font-bold.ty-text.italic.leading-tight
     "Maybe we don't need revolution. Maybe we need standards."]]

   ;; The Discovery - EMPHASIZED with more drama
   [:section.ty-elevated.p-12.rounded-2xl.space-y-8.ty-border-primary.border-2.ty-bg-primary-.relative.overflow-hidden
    ;; Decorative element
    [:div.absolute.top-0.right-0.w-64.h-64.ty-bg-primary-.opacity-50.rounded-full.blur-3xl {:style {:transform "translate(50%, -50%)"}}]

    [:div.text-center.mb-8.relative
     [:div.inline-block.ty-bg-primary.p-4.rounded-full.mb-6
      [:ty-icon.ty-text++ {:name "lightbulb"
                           :size "xl"}]]
     [:h2.text-xl.lg:text-5xl.font-bold.ty-text-primary.mb-4
      "Web Components"]]

    [:div.space-y-6.relative
     [:p.ty-text.leading-relaxed.font-medium
      "It turns out that " [:strong.ty-text-primary "Web Components"] " are a standardized approach built into browsers, based on vanilla JavaScript. They've been there all along."]
     [:p.ty-text.leading-relaxed.font-medium
      [:strong.ty-text-primary "No framework required. No technical debt. No need to commit to a platform."] " They just work—usable in any browser, with any technology or framework."]

     [:div.text-center.py-6
      [:p.ty-text-primary.font-bold.leading-relaxed
       "If you work with HTML, you can use Web Components."]]

     [:p.ty-text-.text-center
      [:i "“I just needed to build them first.”"]
      " 😅"]

     [:div.mt-8.p-8.ty-content.rounded-xl.border.ty-border.ty-text-
      [:p.leading-relaxed
       "Perspective shift. A way forward that is not about choosing sides in someone else's war. "
       "It is about building on something stable, something that would last."]]]]

   ;; The Perspective Shift
   [:section.ty-elevated.p-12.rounded-xl.space-y-6
    [:div.flex.items-center.gap-4.mb-6
     [:div.ty-bg-success-.p-3.rounded-lg
      [:ty-icon.ty-text-success++ {:name "code"
                                   :size "lg"}]]
     [:h2.text-xl.font-bold.ty-text
      "Building the Solution"]]
    [:p.text.ty-text.leading-relaxed
     "So ty Components is my attempt to create something different. To use ClojureScript—a language I " [:strong.ty-text-primary "love"] " — for solutions I believe in."]
    [:p.text.ty-text.leading-relaxed
     "It's working well so far. From what I can tell, it's genuinely usable outside of Clojure too—with HTMX, React, server-side rendering, whatever you need."]
    [:div.ty-bg-success-.p-6.rounded-lg.border-l-4.ty-border-success
     [:p.text-sm.ty-text.leading-relaxed
      "This feels right. Not because it's revolutionary, but because it's " [:em "stable"] ". Built on standards that browsers already support. Components that will work the same way years from now."]]]

   ;; The Bottom Line - More dramatic
   [:section.text-center.space-y-8.py-16.ty-bg-neutral-.rounded-2xl.p-12
    [:div.inline-block.ty-bg-primary-.px-4.py-2.rounded-full.mb-4
     [:span.text-sm.ty-text-primary.font-semibold.uppercase.tracking-wide "The Bottom Line"]]
    [:h2.text-4xl.lg:text-5xl.font-bold.ty-text.mb-6.leading-tight
     "ty isn't trying to win a war."]
    [:p.text-2xl.ty-text.leading-relaxed
     "It's choosing not to fight one."]
    [:p.text-xl.ty-text-.max-w-2xl.mx-auto.leading-relaxed
     "It's a bet on web standards, built by someone who found a different path, for others who are looking for the same."]

    ;; CTA buttons
    [:div.flex.flex-col.sm:flex-row.gap-4.justify-center.items-center.pt-12
     [:ty-button.px-8.py-4.text-lg
      {:primary true
       :on {:click #(js/window.location.href "#/welcome")}}
      [:ty-icon.mr-2 {:name "rocket"
                      :size "sm"}]
      "Start Using ty"]
     [:ty-button.px-8.py-4.text-lg
      {:on {:click #(js/window.location.href "https://github.com/gersak/ty")}}
      [:ty-icon.mr-2 {:name "github"
                      :size "sm"}]
      "View on GitHub"]]]]))
