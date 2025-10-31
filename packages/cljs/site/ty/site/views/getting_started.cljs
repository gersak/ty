(ns ty.site.views.getting-started
  (:require [ty.router :as router]
            [ty.site.docs.common :as common]))

;; =============================================================================
;; Header Section
;; =============================================================================

(defn header []
  [:div.text-center.mb-12
   [:h1.text-4xl.font-bold.ty-text++.mb-4 "Getting Started"]
   [:p.text-xl.ty-text "TypeScript web components + ClojureScript infrastructure"]
   [:p.text-lg.ty-text- "18+ components • 3000+ icons • Semantic design • Framework-agnostic"]])

;; =============================================================================
;; Quick Setup Section
;; =============================================================================

(defn quick-setup []
  [:div
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Quick Setup"]
   [:p.ty-text-.mb-6
    "Before you begin, include the Ty CSS and JavaScript files in your HTML " [:code.ty-bg-neutral-.px-1.rounded "<head>"] ":"]

   [:div.space-y-6
    ;; CSS
    [:div
     [:h3.text-lg.font-semibold.ty-text++.mb-3 "1. Include Ty CSS"]
     (common/code-block
       "<!-- In your HTML <head> -->
<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@gersak/ty/css/ty.css\">"
       "html")
     [:p.ty-text-.text-sm.mt-3.italic
      "Provides the complete Ty design system with semantic colors, surfaces, and theming."]]

    ;; JavaScript
    [:div
     [:h3.text-lg.font-semibold.ty-text++.mb-3 "2. Include Ty Components"]
     (common/code-block
       "<!-- In your HTML <head> -->
<script src=\"https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.js\"></script>"
       "html")
     [:p.ty-text-.text-sm.mt-3.italic
      "Registers all 18+ Ty web components and makes them available as custom HTML elements."]]

    ;; Complete example
    [:div.ty-bg-success-.border.ty-border-success.rounded.p-4
     [:h3.text-lg.font-semibold.ty-text-success.mb-3 "Complete Example"]
     [:p.ty-text-success.text-sm.mb-3 "Here's a minimal HTML page with Ty:"]
     (common/code-block
       "<!DOCTYPE html>
<html lang=\"en\">
<head>
  <meta charset=\"UTF-8\">
  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
  <title>My Ty App</title>
  
  <!-- Ty CSS and JS -->
  <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@gersak/ty/css/ty.css\">
  <script src=\"https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.js\"></script>
</head>
<body class=\"ty-canvas\">
  
  <div class=\"ty-elevated p-6 rounded-lg max-w-md mx-auto mt-8\">
    <h1 class=\"ty-text++ text-2xl mb-4\">Hello Ty!</h1>
    <ty-button variant=\"primary\">Click Me</ty-button>
  </div>

</body>
</html>"
       "html")
     [:p.ty-text-success.text-sm.mt-3
      "💡 " [:strong "Note: "] "This is the fastest way to get started. For production apps with build tools, check the integration guides below."]]]])

;; =============================================================================
;; Path Selection Cards
;; =============================================================================

(defn path-card
  "Clickable card for navigation to integration guide"
  [{:keys [route-id icon color title description link-text]}]
  [:div.ty-elevated.p-6.rounded-xl.hover:ty-elevated.cursor-pointer.transition-all
   {:on {:click #(router/navigate! route-id)}}
   [:div.flex.items-center.gap-3.mb-4
    [:ty-icon {:name icon
               :size "lg"
               :class (str "ty-text-" color)}]
    [:h3.text-xl.font-semibold.ty-text title]]
   [:p.ty-text-.mb-4 description]
   [:div.flex.items-center.gap-2.text-sm.font-medium
    {:class (str "ty-text-" color)}
    [:span link-text]
    [:ty-icon {:name "arrow-right"
               :size "sm"}]]])

(defn choose-your-path []
  [:div
   [:h2.text-2xl.font-bold.ty-text++.mb-6 "Choose Your Path"]
   [:div.grid.grid-cols-1.md:grid-cols-2.gap-6

    (path-card
      {:route-id :ty.site.docs/htmx
       :icon "server"
       :color "primary"
       :title "HTML / Backend / HTMX"
       :description "No build tools required. Perfect for PHP, Python, Ruby, Java, and server-side frameworks."
       :link-text "View HTMX Guide"})

    (path-card
      {:route-id :ty.site.docs/react
       :icon "atom"
       :color "success"
       :title "JavaScript / TypeScript"
       :description "NPM with tree-shakeable imports. Works with React, Vue, Svelte, and more."
       :link-text "View JS React Guide"})

    (path-card
      {:route-id :ty.site.docs/replicant
       :icon "lambda"
       :color "warning"
       :title "ClojureScript (Vanilla)"
       :description "Replicant, vanilla hiccup. Includes Router, i18n, and Layout utilities."
       :link-text "View Replicant Guide"})

    (path-card
      {:route-id :ty.site.docs/clojurescript
       :icon "lambda"
       :color "neutral"
       :title "ClojureScript + React"
       :description "Reagent, UIx. Full ClojureScript infrastructure with React integration."
       :link-text "View CLJS React Guide"})]])

;; =============================================================================
;; ClojureScript Advantages
;; =============================================================================

(defn advantage-item
  "Single advantage item with icon and description"
  [{:keys [icon title description]}]
  [:div.flex.items-start.gap-3
   [:ty-icon.ty-text-primary++.flex-shrink-0.mt-0.5 {:name icon
                                                     :size "sm"}]
   [:div
    [:h4.font-semibold.ty-text-primary++ title]
    [:p.text-sm.ty-text-primary description]]])

(defn clojurescript-advantages []
  [:div.ty-bg-primary-.border.ty-border-primary.p-6.rounded-xl
   [:div.flex.items-start.gap-4
    [:ty-icon.ty-text-primary++.flex-shrink-0.mt-1 {:name "sparkles"
                                                    :size "lg"}]
    [:div
     [:h2.text-2xl.font-bold.ty-text-primary++.mb-4 "ClojureScript Advantages"]
     [:p.ty-text-primary.mb-4
      "Beyond web components, "
      [:code.ty-bg-primary.px-2.py-1.rounded.font-mono "dev.gersak/ty"]
      " includes:"]
     [:div.space-y-3
      (advantage-item
        {:icon "navigation"
         :title "Router"
         :description "Tree-based routing with role-based access control"})

      (advantage-item
        {:icon "globe"
         :title "i18n"
         :description "Protocol-based translations with 130+ locales and Intl API integration"})

      (advantage-item
        {:icon "layout"
         :title "Layout"
         :description "Container-aware responsive utilities with breakpoint detection"})

      (advantage-item
        {:icon "package"
         :title "Optimal Bundles"
         :description "~50KB total increment with shared ClojureScript runtime"})]
     [:p.text-sm.ty-text-primary.mt-4.italic
      "Learn more in the Replicant or CLJS React guides."]]]])

;; =============================================================================
;; Icon System
;; =============================================================================

(defn icon-example
  "Single icon registration example"
  [{:keys [title code language]}]
  [:div
   [:h4.font-medium.ty-text.mb-2 title]
   (common/code-block code language)])

(defn icon-system []
  [:div
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Icon System"]
   [:p.ty-text-.mb-6
    "Ty includes 3000+ icons from Lucide, Heroicons, Material Design, and FontAwesome."]

   [:h3.text-lg.font-semibold.ty-text++.mb-3
    "Unified Pattern: "
    [:code.ty-bg-neutral.px-2.py-1.rounded.font-mono "window.tyIcons.register()"]]
   [:p.ty-text-.mb-6 "Icons come from different sources depending on your platform:"]

   [:div.space-y-6

    (icon-example
      {:title "NPM / JavaScript (Tree-shakeable)"
       :code "// Import only what you need
import { check, heart, star } from '@gersak/ty/icons/lucide'

// Register with window API
window.tyIcons.register({ check, heart, star })"
       :language "javascript"})

    (icon-example
      {:title "Build-Time Bundling (Recommended for Server-Side Apps)"
       :code "// 1. Install dependencies
// npm init -y
// npm install --save-dev esbuild @gersak/ty

// 2. Create icons.js - pick only what you need
import { check, heart, save, x, menu } from '@gersak/ty/icons/lucide'
window.tyIcons.register({ check, heart, save, x, menu })

// 3. Add to package.json scripts:
// \"build:icons\": \"esbuild icons.js --bundle --minify --format=iife --tree-shaking=true --outfile=static/icons.js\"

// 4. Build: npm run build:icons

// 5. Load in templates with defer:
// <script defer src=\"/static/icons.js\"></script>

// Result: ~6KB bundle instead of 897KB
// Perfect for HTMX, JSP, PHP, Rails, Django"
       :language "javascript"})

    (icon-example
      {:title "ClojureScript (Generated namespaces)"
       :code ";; Add dependencies
{:deps {dev.gersak/ty-icons {:mvn/version \"LATEST\"}}}

;; Import and register
(ns my-app.icons
  (:require [ty.lucide :as lucide]))

(window.tyIcons.register 
  #js {\"check\" lucide/check
       \"heart\" lucide/heart
       \"star\" lucide/star})"
       :language "clojure"})]])

;; =============================================================================
;; Next Steps
;; =============================================================================

(defn next-step-item
  "Single next step item"
  [text]
  [:div.flex.items-center.gap-2
   [:ty-icon {:name "check"
              :size "sm"}]
   [:span text]])

(defn next-steps []
  [:div.ty-bg-primary-.border.ty-border-primary.p-6.rounded-xl
   [:div.flex.items-start.gap-4
    [:ty-icon.ty-text-primary.flex-shrink-0.mt-1 {:name "rocket"
                                                  :size "lg"}]
    [:div
     [:h2.text-xl.font-semibold.ty-text-primary.mb-3 "Next Steps"]
     [:div.space-y-2.text-sm.ty-text-primary
      (next-step-item
        [:span "Read your " [:strong "integration guide"] " (Replicant, CLJS React, JS React, or HTMX)"])

      (next-step-item
        [:span "Learn the " [:strong "CSS system"] " for semantic design"])

      (next-step-item
        [:span "Browse " [:strong "component documentation"] " for detailed APIs"])

      (next-step-item
        [:span "Explore " [:strong "live examples"] " (User Profile, Event Booking, Contact Form)"])]]]])

;; =============================================================================
;; Main View
;; =============================================================================

(defn view []
  [:div.max-w-4xl.mx-auto.space-y-8
   (header)
   (quick-setup)
   (choose-your-path)
   ;; (clojurescript-advantages)
   (icon-system)
   (next-steps)])
