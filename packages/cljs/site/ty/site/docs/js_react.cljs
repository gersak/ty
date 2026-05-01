(ns ty.site.docs.js-react
  "Documentation for using Ty with JavaScript/TypeScript React"
  (:require
    [ty.site.docs.common :refer [code-block doc-section example-section docs-page]]))

;; =============================================================================
;; INSTALLATION
;; =============================================================================

(defn installation-section
  "NPM installation section"
  []
  (doc-section "Installation"
               [:div
                [:p.ty-text-.mb-4
                 "Install both packages via NPM:"]

                (code-block
                  "npm install @gersak/ty @gersak/ty-react"
                  "bash")

                [:div.grid.md:grid-cols-2.gap-4.mt-4
                 [:div.ty-bg-neutral-.border.ty-border.rounded.p-4
                  [:p.ty-text.text-sm.font-semibold.mb-2.flex.items-center.gap-2
                   [:ty-icon {:name "package" :size "sm"}] [:code "@gersak/ty"]]
                  [:ul.ty-text-.text-sm.space-y-1
                   [:li "• Web components (CSS + JS bundle)"]
                   [:li "• 1,600+ Lucide icons"]
                   [:li "• Heroicons, Material, FontAwesome"]
                   [:li "• Tree-shakeable icon imports"]]]
                 [:div.ty-bg-neutral-.border.ty-border.rounded.p-4
                  [:p.ty-text.text-sm.font-semibold.mb-2.flex.items-center.gap-2
                   [:ty-icon {:name "package" :size "sm"}] [:code "@gersak/ty-react"]]
                  [:ul.ty-text-.text-sm.space-y-1
                   [:li "• Typed React wrappers for every component"]
                   [:li "• Full TypeScript definitions"]
                   [:li "• React-idiomatic event handlers"]
                   [:li "• Ref forwarding for imperative APIs"]]]]]))

;; =============================================================================
;; SETUP
;; =============================================================================

(defn setup-section
  "Setup with CSS and components — bundler vs CDN"
  []
  (doc-section "Setup"
               [:div
                [:p.ty-text-.mb-4
                 "Two options for loading Ty's web components. Pick one — they're mutually exclusive."]

                ;; Option A: Bundler imports
                [:h3.text-lg.font-semibold.ty-text.mb-3.flex.items-center.gap-2
                 [:span.ty-bg-success.ty-text-success++.px-2.py-0.5.rounded.text-sm "Recommended"]
                 "Option A — Bundler imports"]
                [:p.ty-text-.mb-4
                 "Import " [:code.ty-bg-neutral-.px-1.rounded "@gersak/ty"]
                 " once at your app's entry point. Each component registers itself via "
                 [:code.ty-bg-neutral-.px-1.rounded "customElements.define()"]
                 " when its module evaluates."]

                (code-block
                  "// app/layout.tsx (Next.js) or src/main.tsx (Vite)
import '@gersak/ty/css/ty.css'   // CSS via your bundler
import '@gersak/ty'               // registers all <ty-*> components"
                  "typescript")

                [:p.ty-text-.mt-4.mb-2 "For smaller bundles, register only the components you use:"]

                (code-block
                  "import '@gersak/ty/css/ty.css'
import '@gersak/ty/button'
import '@gersak/ty/input'
import '@gersak/ty/dropdown'
import '@gersak/ty/modal'"
                  "typescript")

                [:p.ty-text-.mt-4.text-sm
                 "Available subpaths match component names: "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "button"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "input"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "dropdown"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "multiselect"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "modal"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "popup"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "tooltip"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "tag"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "option"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "icon"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "checkbox"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "textarea"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "copy"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "tabs"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "tab"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "calendar"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "calendar-month"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "calendar-navigation"] ", "
                 [:code.ty-bg-neutral-.px-1.rounded.text-xs "date-picker"] "."]

                ;; Option B: CDN
                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-8 "Option B — CDN script tag"]
                [:p.ty-text-.mb-4
                 "Skip the bundler — load Ty from a "
                 [:code.ty-bg-neutral-.px-1.rounded "<script>"]
                 " tag in your HTML head:"]

                (code-block
                  "<!DOCTYPE html>
<html lang=\"en\">
<head>
  <meta charset=\"UTF-8\">
  <title>My React App</title>

  <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@gersak/ty/css/ty.css\">
  <script type=\"module\" src=\"https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.js\"></script>
</head>
<body>
  <div id=\"root\"></div>
</body>
</html>"
                  "html")

                [:p.ty-text-.mt-4
                 "Or self-host by copying "
                 [:code.ty-bg-neutral-.px-1.rounded "node_modules/@gersak/ty/dist/ty.js"]
                 " and " [:code.ty-bg-neutral-.px-1.rounded "node_modules/@gersak/ty/css/ty.css"]
                 " into your " [:code.ty-bg-neutral-.px-1.rounded "public/"] " directory."]

                ;; Comparison
                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-8 "Which should I pick?"]
                [:div.overflow-x-auto.mb-4
                 [:table.w-full.text-sm
                  [:thead
                   [:tr.ty-border.border-b
                    [:th.text-left.py-2.px-3.ty-text+ ""]
                    [:th.text-left.py-2.px-3.ty-text+ "Bundler imports"]
                    [:th.text-left.py-2.px-3.ty-text+ "CDN script tag"]]]
                  [:tbody
                   [:tr.ty-border.border-b
                    [:td.py-2.px-3.ty-text "Component tree-shaking"]
                    [:td.py-2.px-3.ty-text-success "✓ Selective subpath imports"]
                    [:td.py-2.px-3.ty-text-danger "✗ Whole bundle"]]
                   [:tr.ty-border.border-b
                    [:td.py-2.px-3.ty-text "Icon registration"]
                    [:td.py-2.px-3.ty-text-success
                     "✓ Direct " [:code.ty-bg-success-.px-1.rounded.text-xs "registerIcons"] " import"]
                    [:td.py-2.px-3.ty-text-warning
                     "⚠ " [:code.ty-bg-warning-.px-1.rounded.text-xs "window.tyIcons.register"] " (timing-sensitive)"]]
                   [:tr.ty-border.border-b
                    [:td.py-2.px-3.ty-text "Build complexity"]
                    [:td.py-2.px-3.ty-text- "Standard import"]
                    [:td.py-2.px-3.ty-text-
                     "One-time " [:code.ty-bg-neutral-.px-1.rounded.text-xs "<script>"] " in HTML"]]
                   [:tr
                    [:td.py-2.px-3.ty-text "First paint"]
                    [:td.py-2.px-3.ty-text- "Same bundle as your app"]
                    [:td.py-2.px-3.ty-text- "Separate request, may FOUC"]]]]]

                [:p.ty-text-.text-sm
                 "Bundler imports are the better default for SPAs and SSR apps (Next.js, Remix, Vite). CDN is fine for prototyping or when you don't control the bundler."]]))

;; =============================================================================
;; ICON REGISTRATION
;; =============================================================================

(defn icon-registration-section
  "Icon registration"
  []
  (doc-section "Icon Registration"
               [:div
                [:p.ty-text-.mb-4
                 [:code.ty-bg-neutral-.px-1.rounded "@gersak/ty"]
                 " ships icon libraries as tree-shakeable individual exports — only the icons you import end up in your bundle."]

                [:h3.text-lg.font-semibold.ty-text.mb-3 "Available Icon Libraries"]
                [:div.overflow-x-auto.mb-6
                 [:table.w-full.text-sm
                  [:thead
                   [:tr.ty-border.border-b
                    [:th.text-left.py-2.px-3.ty-text+ "Library"]
                    [:th.text-left.py-2.px-3.ty-text+ "Import path"]
                    [:th.text-left.py-2.px-3.ty-text+ "Variants"]]]
                  [:tbody
                   [:tr.ty-border.border-b
                    [:td.py-2.px-3.ty-text "Lucide (1,600+)"]
                    [:td.py-2.px-3 [:code.ty-bg-neutral-.px-1.rounded.text-xs "@gersak/ty/icons/lucide"]]
                    [:td.py-2.px-3.ty-text- "single file"]]
                   [:tr.ty-border.border-b
                    [:td.py-2.px-3.ty-text "Heroicons"]
                    [:td.py-2.px-3 [:code.ty-bg-neutral-.px-1.rounded.text-xs "@gersak/ty/icons/heroicons/{outline,solid,mini,micro}"]]
                    [:td.py-2.px-3.ty-text- "4 styles"]]
                   [:tr.ty-border.border-b
                    [:td.py-2.px-3.ty-text "Material"]
                    [:td.py-2.px-3 [:code.ty-bg-neutral-.px-1.rounded.text-xs "@gersak/ty/icons/material/{filled,outlined,round,sharp,two-tone}"]]
                    [:td.py-2.px-3.ty-text- "5 styles"]]
                   [:tr
                    [:td.py-2.px-3.ty-text "FontAwesome"]
                    [:td.py-2.px-3 [:code.ty-bg-neutral-.px-1.rounded.text-xs "@gersak/ty/icons/fontawesome/{brands,regular,solid}"]]
                    [:td.py-2.px-3.ty-text- "3 styles"]]]]]

                [:h3.text-lg.font-semibold.ty-text.mb-3 "Register at App Startup"]
                [:p.ty-text-.mb-4 "How you register depends on which setup option you picked above."]

                ;; --- Bundler path ---
                [:h4.text-base.font-semibold.ty-text.mb-2.mt-4.flex.items-center.gap-2
                 [:span.ty-bg-success.ty-text-success++.px-2.py-0.5.rounded.text-xs "Recommended"]
                 "With bundler imports"]
                [:p.ty-text-.text-sm.mb-3
                 [:code.ty-bg-neutral-.px-1.rounded "registerIcons"]
                 " and the components share the same module graph, so registration is synchronous — no timing dance:"]

                (code-block
                  "// lib/icons.ts
import { registerIcons } from '@gersak/ty/icons/registry'
import {
  check, heart, star,
  chevronRight, chevronLeft, alertCircle,
} from '@gersak/ty/icons/lucide'

registerIcons({
  check,
  heart,
  star,
  'chevron-right': chevronRight,
  'chevron-left': chevronLeft,
  'alert-circle': alertCircle,
})"
                  "typescript")

                [:p.ty-text-.text-sm.mt-3.mb-2
                 "Import this file once from your app entry to execute the registration:"]

                (code-block
                  "// app/layout.tsx (Next.js)
import '@gersak/ty/css/ty.css'
import '@gersak/ty'
import '@/lib/icons'   // runs registerIcons at module-eval time"
                  "typescript")

                [:p.ty-text-.text-sm.mt-3
                 "Import names follow camelCase from Lucide; rename to kebab-case in the registration map if that's how you'll reference them in markup ("
                 [:code.ty-bg-neutral-.px-1.rounded "<TyIcon name=\"chevron-right\" />"] ")."]

                ;; --- CDN path ---
                [:h4.text-base.font-semibold.ty-text.mb-2.mt-6 "With CDN script tag"]
                [:p.ty-text-.text-sm.mb-3
                 "The CDN bundle owns the registry the components read from. Use "
                 [:code.ty-bg-neutral-.px-1.rounded "window.tyIcons.register"]
                 " so writes land in that registry — "
                 [:em "not"] " "
                 [:code.ty-bg-neutral-.px-1.rounded "registerIcons"]
                 " imported from " [:code.ty-bg-neutral-.px-1.rounded "@gersak/ty/icons/registry"]
                 ", which would create a separate module instance with its own registry that "
                 [:code.ty-bg-neutral-.px-1.rounded "<ty-icon>"]
                 " can't see."]

                (code-block
                  "// lib/icons.ts
import { check, heart, star, chevronRight } from '@gersak/ty/icons/lucide'

export function registerAppIcons() {
  window.tyIcons.register({
    check,
    heart,
    star,
    'chevron-right': chevronRight,
  })
}"
                  "typescript")

                [:p.ty-text-.text-sm.mt-3.mb-2
                 "Mount this from a client component in your root layout — "
                 [:code.ty-bg-neutral-.px-1.rounded "useEffect"]
                 " waits until the CDN script has loaded "
                 [:code.ty-bg-neutral-.px-1.rounded "window.tyIcons"] ":"]

                (code-block
                  "// components/IconRegistry.tsx
'use client'
import { useEffect } from 'react'
import { registerAppIcons } from '@/lib/icons'

export function IconRegistry() {
  useEffect(() => {
    if (window.tyIcons) return registerAppIcons()
    const id = setInterval(() => {
      if (window.tyIcons) {
        clearInterval(id)
        registerAppIcons()
      }
    }, 50)
    return () => clearInterval(id)
  }, [])
  return null
}"
                  "typescript")

                [:p.ty-text-.text-sm.mt-3.mb-2
                 "Add this once in a " [:code.ty-bg-neutral-.px-1.rounded ".d.ts"]
                 " file so " [:code.ty-bg-neutral-.px-1.rounded "window.tyIcons"]
                 " is typed:"]

                (code-block
                  "declare global {
  interface Window {
    tyIcons: {
      register: (icons: Record<string, string>) => void
      get: (name: string) => string | undefined
      has: (name: string) => boolean
      list: () => string[]
    }
  }
}"
                  "typescript")

                ;; --- Use icons ---
                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-6 "Use Icons"]
                (code-block
                  "<TyIcon name=\"check\" size=\"sm\" />
<TyIcon name=\"heart\" size=\"lg\" pulse />
<TyIcon name=\"chevron-right\" />"
                  "tsx")

                [:div.ty-bg-warning-.border.ty-border-warning.rounded.p-4.mt-6
                 [:p.ty-text-warning.text-sm.font-semibold.mb-2.flex.items-center.gap-2
                  [:ty-icon.ty-text-warning {:name "alert-triangle" :size "sm"}]
                  "Always use named imports"]
                 (code-block
                   "// BAD — imports all 1,600+ icons
import * as lucide from '@gersak/ty/icons/lucide'

// GOOD — tree-shakes to just what you use
import { check, heart } from '@gersak/ty/icons/lucide'"
                   "javascript")]]))

;; =============================================================================
;; USAGE
;; =============================================================================

(defn usage-section
  "Basic usage examples"
  []
  (doc-section "Basic Usage"
               [:div
                [:p.ty-text-.mb-4
                 "Use Ty React components in your JSX:"]

                (example-section
                  "Simple Example"
                  [:div.ty-bg-neutral-.border.ty-border.rounded.p-3.mb-4
                   [:p.ty-text.text-sm "Button with icon and state"]]
                  "import React, { useState } from 'react'
import { TyButton, TyIcon } from '@gersak/ty-react'
import { check, heart } from '@gersak/ty/icons/lucide'

// Register icons
window.tyIcons.register({ check, heart })

function App() {
  const [liked, setLiked] = useState(false)

  return (
    <div className=\"ty-elevated p-6 rounded-lg\">
      <h2 className=\"ty-text++ text-xl mb-4\">Hello Ty!</h2>
      
      <TyButton 
        flavor=\"primary\"
        onClick={() => alert('Clicked!')}
      >
        <TyIcon name=\"check\" />
        Click me
      </TyButton>
      
      <TyButton
        flavor={liked ? 'danger' : 'outline'}
        onClick={() => setLiked(!liked)}
      >
        <TyIcon name=\"heart\" />
        {liked ? 'Liked!' : 'Like'}
      </TyButton>
    </div>
  )
}"
                  "javascript")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-6 "Form Example"]

                (example-section
                  "Form with Controlled Components"
                  [:div.ty-bg-success-.border.ty-border-success.rounded.p-3.mb-4
                   [:p.ty-text-success.text-sm "Controlled inputs with validation"]]
                  "import React, { useState } from 'react'
import { TyButton, TyInput, TyIcon } from '@gersak/ty-react'
import { save } from '@gersak/ty/icons/lucide'

window.tyIcons.register({ save })

function UserForm() {
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')

  const handleSubmit = (e) => {
    e.preventDefault()
    console.log({ name, email })
  }

  return (
    <form onSubmit={handleSubmit} className=\"ty-elevated p-6 rounded-lg\">
      <h2 className=\"ty-text++ text-xl mb-4\">User Form</h2>
      
      <div className=\"mb-4\">
        <label className=\"ty-text+ block text-sm mb-2\">Name</label>
        <TyInput
          value={name}
          placeholder=\"Enter name\"
          onChange={(e) => setName(e.target.value)}
        />
      </div>
      
      <div className=\"mb-4\">
        <label className=\"ty-text+ block text-sm mb-2\">Email</label>
        <TyInput
          type=\"email\"
          value={email}
          placeholder=\"Enter email\"
          onChange={(e) => setEmail(e.target.value)}
        />
      </div>
      
      <TyButton type=\"submit\" flavor=\"primary\">
        <TyIcon name=\"save\" />
        Submit
      </TyButton>
    </form>
  )
}"
                  "javascript")]))

;; =============================================================================
;; MAIN VIEW
;; =============================================================================

(defn view
  "Main view for JavaScript React documentation"
  []
  (docs-page
   [:h1.text-4xl.font-bold.ty-text.mb-4 "JavaScript / TypeScript with React"]
   [:p.text-xl.ty-text-.mb-6
    "Use Ty components with React, Next.js, Vite, or any React framework."]

   (installation-section)
   (setup-section)
   (icon-registration-section)
   (usage-section)

   ;; Next steps
   [:div.ty-bg-neutral-.border.ty-border.rounded.p-6.mt-12
    [:h2.text-2xl.font-bold.ty-text.mb-4 "Next Steps"]
    [:ul.space-y-2.ty-text-.text-sm
     [:li "• Review the " [:strong "CSS Guide"] " for styling"]
     [:li "• Browse " [:strong "component docs"] " for APIs"]
     [:li "• Check " [:strong "/examples"] " for complete projects"]]]))
