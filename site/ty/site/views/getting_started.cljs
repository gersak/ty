(ns ty.site.views.getting-started
  (:require [ty.site.docs.common :as common]))

(defn view []
  [:div.max-w-4xl.mx-auto.space-y-8
   [:div.text-center.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-4 "Getting Started"]
    [:p.text-lg.ty-text-.max-w-2xl.mx-auto "Choose your setup based on your tech stack. All approaches require both CSS styles and web components runtime."]]

   ;; CRITICAL CSS WARNING
   [:div.ty-bg-primary-.border.ty-border-primary.p-6.rounded-xl.mb-8
    [:div.flex.items-start.gap-4
     [:ty-icon.ty-text-primary++.flex-shrink-0.mt-1 {:name "alert-triangle"
                                                     :size "lg"}]
     [:div
      [:h3.text-xl.font-bold.ty-text-primary++.mb-3 "ty.css is REQUIRED"]
      [:p.ty-text-primary.mb-3
       "Every setup below requires " [:code.ty-bg-primary.px-2.py-1.rounded.font-mono "ty.css"]
       ". Components depend on CSS variables defined in this stylesheet. Without it, components "
       [:strong "render but have no styling"] "."]
      [:div.p-4.rounded-lg
       [:p.text-sm.ty-text-primary++.font-medium.mb-2 "What breaks without ty.css:"]
       [:ul.space-y-1.text-sm.ty-text-primary
        [:li "❌ No colors (CSS variables undefined)"]
        [:li "❌ No layout (surface hierarchy missing)"]
        [:li "❌ No theme switching"]
        [:li "❌ Utility classes (" [:code.font-mono "ty-bg-primary"] ") don't work"]]]]]]

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

    [:p.ty-text-.mb-4 "Use TypeScript-friendly React wrappers with CDN for the easiest setup. Perfect for Next.js, Remix, and any React app."]

    [:div.space-y-4
     [:div
      [:h4.font-medium.ty-text.mb-2 "1. Add CDN to your HTML head:"]
      (common/code-block
        "<!-- In your HTML head (index.html, _app.js, etc.) -->
<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css\">
<script src=\"https://cdn.jsdelivr.net/npm/@gersak/ty@latest/ty.js\"></script>"
        "html")]
     [:div
      [:h4.font-medium.ty-text.mb-2 "2. Install React wrappers:"]
      (common/code-block
        "npm install @gersak/ty-react"
        "bash")]
     [:div
      [:h4.font-medium.ty-text.mb-2 "3. Use React components:"]
      (common/code-block
        "import { Button, Input, Dropdown, Calendar } from '@gersak/ty-react'

function App() {
  const [date, setDate] = useState(null)
  
  return (
    <div>
      <Button variant=\"primary\" onClick={() => alert('Clicked!')}>
        Click me
      </Button>
      <Input 
        label=\"Email\" 
        type=\"email\" 
        onChange={(e) => console.log(e.target.value)}
      />
      <Calendar 
        value={date}
        onChange={(e) => setDate(e.detail.date)}
      />
    </div>
  )
}"
        "javascript")]
     [:div.ty-bg-success-.border.ty-border-success.p-4.rounded-lg
      [:div.flex.items-start.gap-2
       [:ty-icon.ty-text-success++.flex-shrink-0.mt-0.5 {:name "check-circle"
                                                         :size "sm"}]
       [:div.text-sm.ty-text-success++
        [:strong "Why this approach?"] " CDN handles web components runtime automatically. React wrappers provide TypeScript support and React-friendly event handling. No version conflicts, minimal bundle size."]]]

     [:div.ty-bg-info-.border.ty-border-info.p-4.rounded-lg
      [:div.text-sm.ty-text-info++
       [:h5.font-medium.mb-2 "💡 Available React wrappers:"]
       [:div.grid.grid-cols-2.gap-2.text-xs
        [:div
         [:strong "Form Components:"]
         [:ul.space-y-1.mt-1
          [:li "• Button, Input, Textarea"]
          [:li "• Dropdown, Multiselect"]
          [:li "• Tag"]]]
        [:div
         [:strong "Complex Components:"]
         [:ul.space-y-1.mt-1
          [:li "• Calendar, DatePicker"]
          [:li "• Modal, Popup"]
          [:li "• Tooltip"]]]]
       [:p.text-xs.mt-2 "Import as " [:code.ty-bg-info-.px-1.rounded "import { Button, Calendar } from '@gersak/ty-react'"] " or with destructuring."]]]]]

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
    [:ty-button {:flavor \"primary\"} \"Click me\"]
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

    [:p.ty-text-.mb-4 "For Reagent, UIx, and other React-based ClojureScript libraries - use web components directly for the cleanest approach."]

    [:div.space-y-4
     [:div
      [:h4.font-medium.ty-text.mb-2 "1. Add dependency to deps.edn:"]
      (common/code-block
        "{:deps {dev.gersak/ty {:mvn/version \"0.1.5\"}
         com.pitch/uix.core {:mvn/version \"1.0.1\"}}}"
        "clojure")]
     [:div
      [:h4.font-medium.ty-text.mb-2 "2. Require ty.components to register web components:"]
      (common/code-block
        "(ns my-app.core
  (:require [uix.core :as uix :refer [$ defui]]
            [ty.components]))  ; Auto-registers ALL web components

(defui app []
  (let [[value set-value!] (uix/use-state \"\")]
    ($ :div
      ;; Use web components directly (recommended for ClojureScript)
      ($ :ty-button {:flavor \"primary\" 
                     :onClick #(js/alert \"Clicked!\")} \"Click me\")
      ($ :ty-input {:label \"Email\" 
                    :type \"email\"
                    :value value
                    :onChange #(set-value! (.. % -target -value))}))))"
        "clojure")]
     [:div.ty-bg-success-.border.ty-border-success.p-4.rounded-lg
      [:div.flex.items-start.gap-2
       [:ty-icon.ty-text-success++.flex-shrink-0.mt-0.5 {:name "check-circle"
                                                         :size "sm"}]
       [:div.text-sm.ty-text-success++
        [:strong "Why this approach?"] " Everything is included in the ClojureScript dependency - no CDN needed! The ty.components namespace registers all web components and includes CSS automatically."]]]

     [:div.ty-bg-info-.border.ty-border-info.p-4.rounded-lg
      [:div.text-sm.ty-text-info++
       [:h5.font-medium.mb-2 "💡 Available web components:"]
       [:div.grid.grid-cols-2.gap-2.text-xs
        [:div
         [:strong "Form Components:"]
         [:ul.space-y-1.mt-1
          [:li "• ty-button, ty-input, ty-textarea"]
          [:li "• ty-dropdown, ty-multiselect"]
          [:li "• ty-tag"]]]
        [:div
         [:strong "Complex Components:"]
         [:ul.space-y-1.mt-1
          [:li "• ty-calendar, ty-date-picker"]
          [:li "• ty-modal, ty-popup"]
          [:li "• ty-tooltip"]]]]
       [:p.text-xs.mt-2 "All components work as native HTML elements with standard events and properties."]]]]]

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
