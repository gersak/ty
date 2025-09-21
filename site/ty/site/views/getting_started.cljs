(ns ty.site.views.getting-started)

(defn view []
  [:div.max-w-4xl.mx-auto.space-y-8
   [:div.text-center.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-4 "Getting Started"]
    [:p.text-lg.ty-text-.max-w-2xl.mx-auto "Choose your setup based on your tech stack. Copy the code snippets and you're ready to go."]]

   ;; HTML/Backend Setup
   [:div.ty-elevated.p-6.rounded-xl
    [:div.flex.items-center.gap-3.mb-6
     [:ty-icon.ty-text-primary {:name "code"
                                :size "lg"}]
     [:h2.text-xl.font-semibold.ty-text "HTML / Backend"]]

    [:p.ty-text-.mb-4 "For vanilla HTML, PHP, Python/Flask, Ruby/Rails, Java/Spring, etc."]

    [:div.space-y-4
     [:div
      [:h4.font-medium.ty-text.mb-2 "1. Add to your <head> section:"]
      [:pre.rounded-lg.overflow-x-auto
       [:code.language-html.hljs
        "<!-- Ty Components CSS -->\n<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/css/ty.css\">\n\n<!-- Ty Components JS -->\n<script src=\"https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/ty.js\"></script>"]]]

     [:div
      [:h4.font-medium.ty-text.mb-2 "2. Use components anywhere in your HTML:"]
      [:pre.rounded-lg.overflow-x-auto
       [:code.language-html.hljs
        "<ty-button variant=\"primary\">Click me</ty-button>\n<ty-input label=\"Email\" type=\"email\"></ty-input>\n<ty-dropdown label=\"Choose option\">\n  <ty-option value=\"1\">Option 1</ty-option>\n  <ty-option value=\"2\">Option 2</ty-option>\n</ty-dropdown>"]]]]]

   ;; ClojureScript Vanilla (Replicant, etc.)
   [:div.ty-elevated.p-6.rounded-xl
    [:div.flex.items-center.gap-3.mb-6
     [:ty-icon.ty-text-warning {:name "lambda"
                                :size "lg"}]
     [:h2.text-xl.font-semibold.ty-text "ClojureScript (Vanilla / Replicant)"]]

    [:p.ty-text-.mb-4 "For vanilla ClojureScript, Replicant, or non-React libraries - use native web components."]

    [:div.space-y-4
     [:div
      [:h4.font-medium.ty-text.mb-2 "1. Add to deps.edn:"]
      [:pre.rounded-lg.overflow-x-auto
       [:code.language-clojure.hljs
        "{:deps {dev.gersak/ty {:mvn/version \"0.1.5\"}}}"]]]

     [:div
      [:h4.font-medium.ty-text.mb-2 "2. Require and use web components:"]
      [:pre.rounded-lg.overflow-x-auto
       [:code.language-clojure.hljs
        "(ns my-app.core\n  (:require [ty.components]))  ; Auto-registers all web components\n\n(defn app []\n  [:div\n    [:ty-button {:variant \"primary\"} \"Click me\"]\n    [:ty-input {:label \"Email\" :type \"email\"}]])"]]]]]

   ;; ClojureScript + React (Reagent, UIx, etc.)
   [:div.ty-elevated.p-6.rounded-xl
    [:div.flex.items-center.gap-3.mb-6
     [:ty-icon.ty-text-info {:name "lambda"
                             :size "lg"}]
     [:h2.text-xl.font-semibold.ty-text "ClojureScript + React (Reagent / UIx)"]]

    [:p.ty-text-.mb-4 "For Reagent, UIx, and other React-based ClojureScript libraries - use the React wrappers."]

    [:div.space-y-4
     [:div
      [:h4.font-medium.ty-text.mb-2 "1. Add dependencies:"]
      [:pre.rounded-lg.overflow-x-auto
       [:code.language-clojure.hljs
        ";; deps.edn - Add React-based ClojureScript lib\n{:deps {com.pitch/uix.core {:mvn/version \"1.0.1\"}\n        thheller/shadow-cljs {:mvn/version \"2.28.10\"}}}\n\n;; package.json - Add Ty React wrappers\n\"@gersak/ty-react\": \"latest\""]]]

     [:div
      [:h4.font-medium.ty-text.mb-2 "2. Import and use React wrappers:"]
      [:pre.rounded-lg.overflow-x-auto
       [:code.language-clojure.hljs
        "(ns my-app.core\n  (:require [uix.core :as uix :refer [$ defui]]\n            [\"@gersak/ty-react\" :refer [Button Input Dropdown]]))\n\n(defui app []\n  ($ :div\n    ($ Button {:variant \"primary\"} \"Click me\")\n    ($ Input {:label \"Email\" :type \"email\"}))"]]]]]

;; React Setup
   [:div.ty-elevated.p-6.rounded-xl
    [:div.flex.items-center.gap-3.mb-6
     [:ty-icon.ty-text-success {:name "atom"
                                :size "lg"}]
     [:h2.text-xl.font-semibold.ty-text "React / Next.js / Remix"]]

    [:p.ty-text-.mb-4 "TypeScript-friendly React wrappers with full type support."]

    [:div.space-y-4
     [:div
      [:h4.font-medium.ty-text.mb-2 "1. Install packages:"]
      [:pre.rounded-lg.overflow-x-auto
       [:code.language-bash.hljs
        "npm install @gersak/ty-react"]]]

     [:div
      [:h4.font-medium.ty-text.mb-2 "2. Import and use components:"]
      [:pre.rounded-lg.overflow-x-auto
       [:code.language-javascript.hljs
        "import { Button, Input, Dropdown } from '@gersak/ty-react'\nimport '@gersak/ty/dist/css/ty.css'\n\nfunction App() {\n  return (\n    <div>\n      <Button variant=\"primary\">Click me</Button>\n      <Input label=\"Email\" type=\"email\" />\n    </div>\n  )\n}"]]]]]

   ;; Next Steps
   [:div.ty-bg-primary-.border.border-ty-border-primary.p-6.rounded-xl
    [:div.flex.items-start.gap-4
     [:ty-icon.ty-text-primary.flex-shrink-0.mt-1 {:name "rocket"
                                                   :size "lg"}]
     [:div
      [:h3.text-lg.font-semibold.ty-text-primary.mb-2 "Next Steps"]
      [:div.space-y-2.text-sm.ty-text-primary
       [:div.flex.items-center.gap-2
        [:ty-icon {:name "check"
                   :size "sm"}]
        "Explore the live examples above (User Profile, Event Booking, Contact Form)"]
       [:div.flex.items-center.gap-2
        [:ty-icon {:name "check"
                   :size "sm"}]
        "Check out the Ty Styles page to understand the semantic color system"]
       [:div.flex.items-center.gap-2
        [:ty-icon {:name "check"
                   :size "sm"}]
        "View source code of examples to see real-world usage patterns"]]]]]])
