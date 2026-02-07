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
                 "Install the React-specific package via NPM:"]

                (code-block
                  "npm install @gersak/ty-react"
                  "bash")

                [:div.ty-bg-neutral-.border.ty-border.rounded.p-3.mt-4
                 [:p.ty-text.text-sm.mb-2
                  "📦 " [:strong "What's included:"]]
                 [:ul.ty-text.text-sm.space-y-1.ml-4
                  [:li "• React wrappers for all Ty components"]
                  [:li "• Full TypeScript definitions"]
                  [:li "• Proper React event handling"]
                  [:li "• Tree-shakeable imports"]]]]))

;; =============================================================================
;; SETUP
;; =============================================================================

(defn setup-section
  "Setup with CSS and components"
  []
  (doc-section "Setup"
               [:div
                [:p.ty-text-.mb-4
                 "Include Ty CSS and JavaScript in your HTML:"]

                (code-block
                  "<!DOCTYPE html>
<html lang=\"en\">
<head>
  <meta charset=\"UTF-8\">
  <title>My React App</title>
  
  <!-- Ty CSS and JS from CDN -->
  <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.css\">
  <script src=\"https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.js\"></script>
</head>
<body>
  <div id=\"root\"></div>
</body>
</html>"
                  "html")]))

;; =============================================================================
;; ICON REGISTRATION
;; =============================================================================

(defn icon-registration-section
  "Icon registration"
  []
  (doc-section "Icon Registration"
               [:div
                [:p.ty-text-.mb-4
                 "Import only the icons you need (tree-shakeable):"]

                (code-block
                  "// Import specific icons
import { check, heart, save } from '@gersak/ty/icons/lucide'

// Register with global API
window.tyIcons.register({ check, heart, save })"
                  "javascript")

                [:div.ty-bg-warning-.border.ty-border-warning.rounded.p-4.mt-4
                 [:p.ty-text-warning.text-sm.font-semibold.mb-2
                  "⚠️ Import only what you need"]
                 (code-block
                   "// ❌ BAD - imports all 1,636 icons
import * as lucide from '@gersak/ty/icons/lucide'

// ✅ GOOD - only what you need
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
