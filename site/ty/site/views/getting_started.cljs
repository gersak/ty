(ns ty.site.views.getting-started
  (:require [ty.site.docs.common :as common]))

(defn view []
  [:div.max-w-4xl.mx-auto.space-y-8
   [:div.text-center.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-4 "Getting Started"]
    [:p.text-lg.ty-text-.max-w-2xl.mx-auto "Choose your setup based on your tech stack. All approaches require both CSS styles and web components runtime."]]

   ;; HTML/Backend Setup
   [:div.ty-elevated.p-6.rounded-xl
    [:div.flex.items-center.gap-3.mb-6
     [:ty-icon.ty-text-primary {:name "code"
                                :size "lg"}]
     [:h2.text-xl.font-semibold.ty-text "HTML / Backend Frameworks"]]

    [:p.ty-text-.mb-4 "For vanilla HTML, PHP, Python/Flask, Ruby/Rails, Java/Spring, etc. - use CDN for simplicity."]

    [:div.space-y-4
     [:div
      [:h4.font-medium.ty-text.mb-2 "Add to your <head> section:"]
      (common/code-block
        "<!-- Ty Components CSS -->
<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css\">

<!-- Ty Components JS (registers all web components) -->
<script src=\"https://cdn.jsdelivr.net/npm/@gersak/ty@latest/ty.js\"></script>"
        "html")]

     [:div
      [:h4.font-medium.ty-text.mb-2 "Use components anywhere in your HTML:"]
      (common/code-block
        "<ty-button variant=\"primary\">Click me</ty-button>
<ty-input label=\"Email\" type=\"email\"></ty-input>
<ty-dropdown label=\"Choose option\">
  <ty-option value=\"1\">Option 1</ty-option>
  <ty-option value=\"2\">Option 2</ty-option>
</ty-dropdown>"
        "html")]]]

   ;; React Setup
   [:div.ty-elevated.p-6.rounded-xl
    [:div.flex.items-center.gap-3.mb-6
     [:ty-icon.ty-text-success {:name "atom"
                                :size "lg"}]
     [:h2.text-xl.font-semibold.ty-text "React / Next.js / Remix"]]

    [:p.ty-text-.mb-4 "TypeScript-friendly React wrappers with full type support. You need both React wrappers AND web components runtime."]

    [:div.space-y-6
     [:div.ty-bg-warning-.border.ty-border-warning.p-4.rounded-lg
      [:div.flex.items-start.gap-2
       [:ty-icon.ty-text-warning++.flex-shrink-0.mt-0.5 {:name "info"
                                                         :size "sm"}]
       [:div.text-sm.ty-text-warning++
        [:strong "Important:"] " React wrappers (@gersak/ty-react) are just thin wrappers around web components. You still need the web components runtime (ty.js) loaded."]]]

     [:div
      [:h4.font-medium.ty-text.mb-2 "Option 1: NPM Packages (Recommended)"]
      [:div.space-y-3
       [:div
        [:h5.text-sm.font-medium.ty-text-.mb-2 "1a. Install packages:"]
        (common/code-block
          "# Install React wrappers + base components
npm install @gersak/ty-react @gersak/ty"
          "bash")]
       [:div
        [:h5.text-sm.font-medium.ty-text-.mb-2 "1b. Import everything in your app:"]
        (common/code-block
          "// Import CSS styles
import '@gersak/ty/css/ty.css'
// Import web components runtime (registers all <ty-*> components)
import '@gersak/ty/ty.js'
// Import React wrappers
import { Button, Input, Dropdown } from '@gersak/ty-react'

function App() {
  return (
    <div>
      {/* Use React wrappers (recommended) */}
      <Button variant=\"primary\">Click me</Button>
      <Input label=\"Email\" type=\"email\" />
      
      {/* Or mix with web components directly */}
      <ty-button variant=\"secondary\">Web Component</ty-button>
    </div>
  )
}"
          "javascript")]]]

     [:div
      [:h4.font-medium.ty-text.mb-2 "Option 2: CDN + NPM Hybrid"]
      [:div.space-y-3
       [:div
        [:h5.text-sm.font-medium.ty-text-.mb-2 "2a. Add CDN to your HTML head:"]
        (common/code-block
          "<!-- In your HTML head (index.html, _app.js, etc.) -->
<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css\">
<script src=\"https://cdn.jsdelivr.net/npm/@gersak/ty@latest/ty.js\"></script>"
          "html")]
       [:div
        [:h5.text-sm.font-medium.ty-text-.mb-2 "2b. Install only React wrappers:"]
        (common/code-block
          "npm install @gersak/ty-react"
          "bash")]
       [:div
        [:h5.text-sm.font-medium.ty-text-.mb-2 "2c. Use React components (no imports needed):"]
        (common/code-block
          "import { Button, Input, Dropdown } from '@gersak/ty-react'

function App() {
  return (
    <div>
      <Button variant=\"primary\">Click me</Button>
      <Input label=\"Email\" type=\"email\" />
    </div>
  )
}"
          "javascript")]]]]]

   ;; ClojureScript Vanilla (Replicant, etc.)
   [:div.ty-elevated.p-6.rounded-xl
    [:div.flex.items-center.gap-3.mb-6
     [:ty-icon.ty-text-warning {:name "lambda"
                                :size "lg"}]
     [:h2.text-xl.font-semibold.ty-text "ClojureScript (Vanilla / Replicant)"]]

    [:p.ty-text-.mb-4 "For vanilla ClojureScript, Replicant, or non-React libraries - use native web components directly."]

    [:div.space-y-4
     [:div
      [:h4.font-medium.ty-text.mb-2 "1. Add to deps.edn:"]
      (common/code-block
        "{:deps {dev.gersak/ty {:mvn/version \"0.1.5\"}}}"
        "clojure")]

     [:div
      [:h4.font-medium.ty-text.mb-2 "2. Require components namespace:"]
      (common/code-block
        "(ns my-app.core
  (:require [ty.components]))  ; Auto-registers ALL web components

(defn app []
  [:div
    [:ty-button {:variant \"primary\"} \"Click me\"]
    [:ty-input {:label \"Email\" :type \"email\"}]])"
        "clojure")]

     [:div.ty-bg-success-.border.ty-border-success.p-4.rounded-lg
      [:div.flex.items-start.gap-2
       [:ty-icon.ty-text-success++.flex-shrink-0.mt-0.5 {:name "check-circle"
                                                         :size "sm"}]
       [:div.text-sm.ty-text-success++
        [:strong "Advantage:"] " No separate CDN or NPM imports needed! The dev.gersak/ty package includes everything - CSS, JS runtime, and components all in one."]]]]]

   ;; ClojureScript + React (Reagent, UIx, etc.)
   [:div.ty-elevated.p-6.rounded-xl
    [:div.flex.items-center.gap-3.mb-6
     [:ty-icon.ty-text-info {:name "lambda"
                             :size "lg"}]
     [:h2.text-xl.font-semibold.ty-text "ClojureScript + React (Reagent / UIx)"]]

    [:p.ty-text-.mb-4 "For Reagent, UIx, and other React-based ClojureScript libraries - you have two great options."]

    [:div.space-y-6
     [:div
      [:h4.font-medium.ty-text.mb-2 "Option 1: Pure ClojureScript (Recommended)"]
      [:div.space-y-3
       [:div
        [:h5.text-sm.font-medium.ty-text-.mb-2 "1a. Add ClojureScript dependency:"]
        (common/code-block
          ";; deps.edn
{:deps {dev.gersak/ty {:mvn/version \"0.1.5\"}
        com.pitch/uix.core {:mvn/version \"1.0.1\"}}}"
          "clojure")]
       [:div
        [:h5.text-sm.font-medium.ty-text-.mb-2 "1b. Use web components directly:"]
        (common/code-block
          "(ns my-app.core
  (:require [uix.core :as uix :refer [$ defui]]
            [ty.components]))  ; Registers all web components

(defui app []
  ($ :div
    ;; Use web components with React/UIx
    ($ :ty-button {:variant \"primary\"} \"Click me\")
    ($ :ty-input {:label \"Email\" :type \"email\"})))"
          "clojure")]
       [:div.ty-bg-success-.border.ty-border-success.p-4.rounded-lg
        [:div.flex.items-start.gap-2
         [:ty-icon.ty-text-success++.flex-shrink-0.mt-0.5 {:name "check-circle"
                                                           :size "sm"}]
         [:div.text-sm.ty-text-success++
          [:strong "Benefits:"] " No NPM dependencies, no version conflicts, everything managed through ClojureScript ecosystem."]]]]]

     [:div
      [:h4.font-medium.ty-text.mb-2 "Option 2: NPM React Wrappers"]
      [:div.space-y-3
       [:div
        [:h5.text-sm.font-medium.ty-text-.mb-2 "2a. Add dependencies:"]
        (common/code-block
          ";; deps.edn - ClojureScript React lib
{:deps {com.pitch/uix.core {:mvn/version \"1.0.1\"}}}

// package.json - NPM packages
{
  \"@gersak/ty-react\": \"latest\",
  \"@gersak/ty\": \"latest\"  // Needed for web components runtime
}"
          "clojure")]
       [:div
        [:h5.text-sm.font-medium.ty-text-.mb-2 "2b. Import and use React wrappers:"]
        (common/code-block
          "(ns my-app.core
  (:require [uix.core :as uix :refer [$ defui]]
            ;; Import CSS and JS runtime
            [\"@gersak/ty/css/ty.css\"]
            [\"@gersak/ty/ty.js\"]
            ;; Import React wrappers
            [\"@gersak/ty-react\" :refer [Button Input Dropdown]]))

(defui app []
  ($ :div
    ($ Button {:variant \"primary\"} \"Click me\")
    ($ Input {:label \"Email\" :type \"email\"})))"
          "clojure")]]]]]

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
