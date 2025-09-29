(ns ty.site.views.why
  "The story behind Ty - why it was created and what it stands for")

(defn view []
  [:div.max-w-4xl.mx-auto.space-y-12

   ;; Header
   [:div.text-center.space-y-4
    [:h1.text-4xl.lg:text-5xl.font-bold.ty-text
     "Why Ty Exists"]
    [:p.text-xl.ty-text-.max-w-2xl.mx-auto
     "The story of choosing standards over revolutions, and clarity over chaos."]]

   ;; The Framework Wars
   [:section.ty-elevated.p-8.rounded-xl.space-y-4
    [:h2.text-3xl.font-bold.ty-text.mb-4
     "The Framework Wars"]
    [:p.text-lg.ty-text
     "Flutter vs React. Google vs Meta/Vercel. Angular vs Vue. The marketing is overwhelming."]
    [:p.text-lg.ty-text
     "The volume of content and marketing materials is staggering. It creates the feeling that you " [:em "must choose"] ". But you only have the choices that " [:em "they"] " force on you. Pick your poison!"]
    [:p.text-lg.ty-text
     "Every single one of them presents this as a " [:strong "(tech) revolution"] ". In fact, it's a " [:strong "war"] "! A war for profit, for market share, for your commitment and loyalty."]]

   ;; Revolutions vs Wars
   [:section.space-y-4
    [:h2.text-3xl.font-bold.ty-text.mb-4
     "Revolutions vs Wars"]
    [:p.text-lg.ty-text
     "Revolutions are positive in some sense because they don't go back to the old ways that made a critical mass of people angry and willing to participate in change with hope for a better future."]
    [:p.text-lg.ty-text
     "But do we need a revolution? Or is it just marketing that encourages this feeling?"]
    [:p.text-lg.ty-text
     "Polarizing. Militarizing. Making us feel like we must pick a side."]
    [:p.text-lg.ty-text.font-semibold.ty-text-primary
     "Maybe we don't need revolution. Maybe we need standards."]]

   ;; The Exhaustion
   #_[:section.ty-elevated.p-8.rounded-xl.space-y-4
      [:h2.text-3xl.font-bold.ty-text.mb-4
       "The Mental Burden"]
      [:p.text-lg.ty-text
       "Lately, I've been reflecting on the mental burden this creates—and it's exhausting."]
      [:p.text-lg.ty-text
       "Which programming language to use? Clojure, Dart, JavaScript? The decision gets more complex from there. If I pick one, what are my options? How well can I position myself, and what are my blind spots?"]
      [:p.text-lg.ty-text
       "Not the marketing propaganda—where something looks amazing in a few cherry-picked demos but falls apart after 6 months of serious work."]
      [:p.text-lg.ty-text.font-semibold
       "I need something I can rely on. Something worth investing time into."]
      [:p.text-lg.ty-text-
       "It felt discouraging—watching the complexity multiply when scaling to multiple people, multiple teams, multiple projects. Everything turns into just another job."]]

   ;; The Discovery - EMPHASIZED
   [:section.ty-elevated.p-10.rounded-xl.space-y-6.ty-border-primary.border-2.ty-bg-primary-
    [:div.text-center.mb-6
     [:ty-icon.ty-text-primary.mx-auto {:name "lightbulb" :size "xl"}]]
    [:h2.text-3xl.font-bold.ty-text-primary.mb-6.text-center
     "Then I Found Web Components"]
    [:p.text-xl.ty-text.leading-relaxed
     "It turns out that " [:strong "Web Components"] " are a standardized approach built into browsers, based on vanilla JavaScript. They've been there all along."]
    [:p.text-xl.ty-text.leading-relaxed
     [:strong "No framework required. No technical debt. No need to commit to a platform."] " They just work—usable in any browser, with any technology or framework."]
    [:p.text-xl.ty-text.leading-relaxed.font-semibold.ty-text-primary
     "If you work with HTML, you can use Web Components."]
    [:p.text-lg.ty-text-.italic.text-center.mt-4
     "I just needed to build them first."]
    [:div.mt-6.p-6.ty-bg-neutral-.rounded-lg
     [:p.text-lg.ty-text.leading-relaxed
      "This was the perspective shift I needed. A way forward that wasn't about choosing sides in someone else's war. "
      "It was about building on something stable, something that would last."]]]

   ;; The Perspective Shift
   [:section.space-y-4
    [:h2.text-3xl.font-bold.ty-text.mb-4
     "Building the Solution"]
    [:p.text-lg.ty-text
     "So Ty Components is my attempt to create something different. To use ClojureScript—a language I " [:em "love"] "—for solutions I believe in."]
    [:p.text-lg.ty-text
     "It's working well so far. From what I can tell, it's genuinely usable outside of Clojure too—with HTMX, React, server-side rendering, whatever you need."]
    [:p.text-lg.ty-text
     "This feels right. Not because it's revolutionary, but because it's " [:em "stable"] ". Built on standards that browsers already support. Components that will work the same way years from now."]
    [:p.text-lg.ty-text.font-semibold.ty-text-primary
     "I'm investing another 3 months into this project to make it stable, production-ready, and feature-complete."]]

   ;; What You Get - Narrative Style (not bullet points)
   [:section.ty-elevated.p-8.rounded-xl.space-y-4
    [:h2.text-3xl.font-bold.ty-text.mb-6
     "What This Means"]
    [:p.text-lg.ty-text.leading-relaxed
     "Ty is built on " [:strong "Web Components"] "—the platform's native component system. This means your components work everywhere: React, HTMX, vanilla JS, ClojureScript, or any future framework that respects web standards."]
    [:p.text-lg.ty-text.leading-relaxed
     "There's " [:strong "no lock-in"] ". Your components will still work 5 years from now. No migration pain. No framework version anxiety. No rewriting everything when the next big thing arrives."]
    [:p.text-lg.ty-text.leading-relaxed
     "It's " [:strong "production-ready"] "—real validation, calendar orchestration, modal management that handles edge cases. Not toy examples, but components you can actually ship."]
    [:p.text-lg.ty-text.leading-relaxed.font-semibold.ty-text-primary
     "Most importantly: peace of mind. No more framework wars. No more marketing noise. Just reliable, working components built on standards that aren't going anywhere."]]

   ;; The Bottom Line
   [:section.text-center.space-y-6.py-8
    [:h2.text-3xl.font-bold.ty-text.mb-4
     "The Bottom Line"]
    [:p.text-xl.ty-text.max-w-2xl.mx-auto.leading-relaxed
     "Ty isn't trying to win a war. It's choosing not to fight one."]
    [:p.text-lg.ty-text.max-w-2xl.mx-auto.leading-relaxed
     "It's a bet on web standards, built by someone who found a better path, for others who are looking for the same."]

    ;; CTA buttons
    [:div.flex.flex-col.sm:flex-row.gap-4.justify-center.items-center.pt-8
     [:ty-button.px-6.py-3
      {:primary true
       :on {:click #(js/window.location.href "#/welcome")}}
      "Start Using Ty"]
     [:ty-button.px-6.py-3
      {:on {:click #(js/window.location.href "https://github.com/gersak/ty")}}
      [:ty-icon.mr-2 {:name "github" :size "sm"}]
      "View on GitHub"]]]])
