(ns ty.site.views.getting-started)

(defn view []
  [:div.max-w-4xl.mx-auto.space-y-8
   [:div.text-center.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-4 "Getting Started"]
    [:p.text-lg.ty-text-.max-w-2xl.mx-auto "Choose your setup based on your tech stack. Copy the code snippets and you're ready to go."]]

   ;; HTML/Backend Setup
   [:div.ty-elevated.p-6.rounded-xl
    [:div.flex.items-center.gap-3.mb-6
     [:ty-icon {:name "code"
                :size "lg"
                :class "ty-text-primary"}]
     [:h2.text-xl.font-semibold.ty-text "HTML / Backend"]]

    [:p.ty-text-.mb-4 "For vanilla HTML, PHP, Python/Flask, Ruby/Rails, Java/Spring, etc."]

    [:div.space-y-4
     [:div
      [:h4.font-medium.ty-text.mb-2 "1. Add to your <head> section:"]
      [:pre.rounded-lg.overflow-x-auto
       [:code.language-html
        "&lt;!-- Ty Components CSS --&gt;\n&lt;link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/css/ty.css\"&gt;\n\n&lt;!-- Ty Components JS --&gt;\n&lt;script src=\"https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/ty.js\"&gt;&lt;/script&gt;"]]]

     [:div
      [:h4.font-medium.ty-text.mb-2 "2. Use components anywhere in your HTML:"]
      [:pre.rounded-lg.overflow-x-auto
       [:code.language-html
        "&lt;ty-button variant=\"primary\"&gt;Click me&lt;/ty-button&gt;\n&lt;ty-input label=\"Email\" type=\"email\"&gt;&lt;/ty-input&gt;\n&lt;ty-dropdown label=\"Choose option\"&gt;\n  &lt;ty-option value=\"1\"&gt;Option 1&lt;/ty-option&gt;\n  &lt;ty-option value=\"2\"&gt;Option 2&lt;/ty-option&gt;\n&lt;/ty-dropdown&gt;"]]]]]

   ;; React Setup
   [:div.ty-elevated.p-6.rounded-xl
    [:div.flex.items-center.gap-3.mb-6
     [:ty-icon {:name "atom"
                :size "lg"
                :class "ty-text-success"}]
     [:h2.text-xl.font-semibold.ty-text "React / Next.js / Remix"]]

    [:p.ty-text-.mb-4 "TypeScript-friendly React wrappers with full type support."]

    [:div.space-y-4
     [:div
      [:h4.font-medium.ty-text.mb-2 "1. Install packages:"]
      [:pre.rounded-lg.overflow-x-auto
       [:code.language-bash
        "npm install @gersak/ty @gersak/ty-react"]]]

     [:div
      [:h4.font-medium.ty-text.mb-2 "2. Import and use components:"]
      [:pre.rounded-lg.overflow-x-auto
       [:code.language-javascript
        "import { TyButton, TyInput, TyDropdown } from '@gersak/ty-react'\nimport '@gersak/ty/dist/css/ty.css'\n\nfunction App() {\n  return (\n    <div>\n      <TyButton variant=\"primary\">Click me</TyButton>\n      <TyInput label=\"Email\" type=\"email\" />\n    </div>\n  )\n}"]]]]]

   ;; ClojureScript Setup  
   [:div.ty-elevated.p-6.rounded-xl
    [:div.flex.items-center.gap-3.mb-6
     [:ty-icon {:name "lambda"
                :size "lg"
                :class "ty-text-warning"}]
     [:h2.text-xl.font-semibold.ty-text "ClojureScript / Reagent / UIx"]]

    [:p.ty-text-.mb-4 "Native ClojureScript components with semantic design system."]

    [:div.space-y-4
     [:div
      [:h4.font-medium.ty-text.mb-2 "1. Add to deps.edn:"]
      [:pre.rounded-lg.overflow-x-auto
       [:code.language-clojure
        "{:deps {dev.gersak/ty {:mvn/version \"0.1.5\"}}}"]]]

     [:div
      [:h4.font-medium.ty-text.mb-2 "2. Require and use components:"]
      [:pre.rounded-lg.overflow-x-auto
       [:code.language-clojure
        "(ns my-app.core\n  (:require [ty.components]))  ; Auto-registers all components\n\n(defn app []\n  [:div\n    [:ty-button {:variant \"primary\"} \"Click me\"]\n    [:ty-input {:label \"Email\" :type \"email\"}]])"]]]]]

   ;; Build Tools & Advanced
   [:div.ty-elevated.p-6.rounded-xl
    [:div.flex.items-center.gap-3.mb-6
     [:ty-icon {:name "settings"
                :size "lg"
                :class "ty-text-info"}]
     [:h2.text-xl.font-semibold.ty-text "Build Tools & Advanced"]]

    [:div.grid.gap-6.md:grid-cols-2
     [:div
      [:h4.font-medium.ty-text.mb-3 "Webpack / Vite / Rollup"]
      [:pre.rounded-lg.overflow-x-auto
       [:code.language-javascript
        "// Import specific modules\nimport '@gersak/ty/calendar'\nimport '@gersak/ty/dropdown'\n\n// Or full bundle\nimport '@gersak/ty/bundle'"]]]

     [:div
      [:h4.font-medium.ty-text.mb-3 "Icons (Optional)"]
      [:pre.rounded-lg.overflow-x-auto
       [:code.language-clojure
        "// ClojureScript\ndev.gersak/ty-icons\n{:mvn/version \"0.1.0\"}\n\n// Register icons\n(:require [ty.icons.lucide])"]]]]]

   ;; Next Steps
   [:div.ty-bg-primary-.border.border-ty-border-primary.p-6.rounded-xl
    [:div.flex.items-start.gap-4
     [:ty-icon {:name "rocket"
                :size "lg"
                :class "ty-text-primary flex-shrink-0 mt-1"}]
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
