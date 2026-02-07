(ns ty.site.docs.htmx
  "Documentation for using Ty with HTMX and Flask"
  (:require
    [ty.site.docs.common :refer [code-block doc-section example-section docs-page]]))

;; =============================================================================
;; INSTALLATION
;; =============================================================================

(defn installation-section
  "Flask + HTMX installation section"
  []
  (doc-section "Installation"
               [:div
                [:p.ty-text-.mb-4
                 "Set up a Flask application with HTMX and Ty components:"]

                [:h3.text-lg.font-semibold.ty-text.mb-3 "Python Dependencies"]

                (code-block
                  "# requirements.txt
flask>=3.0.0
python-dotenv>=1.0.0"
                  "text")

                [:p.ty-text-.mt-4.mb-4
                 "Install Python dependencies:"]

                (code-block
                  "pip install -r requirements.txt"
                  "bash")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-6 "HTMX"]

                [:p.ty-text-.mb-4
                 "HTMX loads via CDN - no installation needed. Include it in your HTML template."]

                [:div.ty-bg-neutral-.border.ty-border.rounded.p-3.mt-4
                 [:p.ty-text.text-sm.mb-2
                  "📦 " [:strong "What you get:"]]
                 [:ul.ty-text.text-sm.space-y-1.ml-4
                  [:li "• Server-side rendering with dynamic updates"]
                  [:li "• No complex JavaScript framework needed"]
                  [:li "• Progressive enhancement"]
                  [:li "• Direct integration with Ty components"]]]]))

;; =============================================================================
;; SETUP
;; =============================================================================

(defn setup-section
  "HTML template setup with CDN"
  []
  (doc-section "HTML Setup"
               [:div
                [:p.ty-text-.mb-4
                 "Create a base Flask template with Ty CSS, HTMX, and optional Tailwind CSS:"]

                (code-block
                  "<!DOCTYPE html>
<html lang=\"en\">
<head>
  <meta charset=\"UTF-8\">
  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
  <title>{% block title %}My HTMX App{% endblock %}</title>
  
  <!-- Ty CSS (semantic design system) -->
  <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@gersak/ty/css/ty.css\">
  
  <!-- Optional: Tailwind CSS for utilities -->
  <script src=\"https://cdn.tailwindcss.com\"></script>
  
  <!-- HTMX -->
  <script src=\"https://unpkg.com/htmx.org@2.0.4\"></script>
  
  <!-- Ty Components (loads all web components) -->
  <script src=\"https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.js\"></script>
</head>
<body class=\"ty-canvas\">
  {% block content %}{% endblock %}
  
  <!-- Your icon registration (after ty.js loads) -->
  <script defer src=\"{{ url_for('static', filename='js/icons.js') }}\"></script>
</body>
</html>"
                  "html")

                [:div.ty-bg-warning-.border.ty-border-warning.rounded.p-4.mt-4
                 [:p.ty-text-warning.text-sm.font-semibold.mb-2
                  "⚠️ Load order matters"]
                 [:ol.ty-text-warning.text-sm.space-y-1.ml-4.list-decimal
                  [:li "Ty CSS (styling)"]
                  [:li "Tailwind CSS (optional utilities)"]
                  [:li "HTMX (for server interactions)"]
                  [:li "Ty components (" [:code.ty-bg-warning.px-1.rounded "ty.js"] ")"]
                  [:li "Icon registration with " [:code.ty-bg-warning.px-1.rounded "defer"] " (ensures " [:code.ty-bg-warning.px-1.rounded "window.tyIcons"] " exists)"]]

                 [:p.ty-text-warning.text-sm.mt-3
                  "Use " [:code.ty-bg-warning.px-1.rounded "defer"] " on your icon script to ensure "
                  [:code.ty-bg-warning.px-1.rounded "window.tyIcons"] " is available from " [:code.ty-bg-warning.px-1.rounded "ty.js"] "."]]]))

;; =============================================================================
;; ICON REGISTRATION
;; =============================================================================

(defn icon-registration-section
  "Icon registration with tree-shaking"
  []
  (doc-section "Icon Registration"
               [:div
                [:p.ty-text-.mb-4
                 "Import only the icons you need (tree-shakeable). Create a "
                 [:code.ty-bg-neutral-.px-2.py-1.rounded "static/js/icons.js"] " file:"]

                (code-block
                  "// static/js/icons.js
// Import only what you need from NPM package
import { check, x, plus, edit, trash, search, save, settings } 
  from '@gersak/ty/icons/lucide'

// Register using the global API (provided by ty.js)
window.tyIcons.register({ 
  check, x, plus, edit, trash,
  search, save, settings
})

console.log('✓ Icons registered:', Object.keys(window.tyIcons.registry))"
                  "javascript")

                [:div.ty-bg-success-.border.ty-border-success.rounded.p-4.mt-4
                 [:p.ty-text-success.text-sm.mb-2
                  "✅ " [:strong "Tree-shaking saves bandwidth"]]
                 [:ul.ty-text-success.text-sm.space-y-1.ml-4
                  [:li "• 8 icons ≈ 1KB (bundled)"]
                  [:li "• Full Lucide library (1,636 icons) ≈ 897KB"]
                  [:li "• You only bundle what you import"]]]

                [:div.ty-bg-warning-.border.ty-border-warning.rounded.p-4.mt-4
                 [:p.ty-text-warning.text-sm.font-semibold.mb-2
                  "⚠️ Don't import entire icon sets"]
                 (code-block
                   "// ❌ BAD - imports all 1,636 icons (~897KB)
import * as lucide from '@gersak/ty/icons/lucide'
window.tyIcons.register(lucide)

// ✅ GOOD - only what you need
import { check, heart } from '@gersak/ty/icons/lucide'
window.tyIcons.register({ check, heart })"
                   "javascript")]]))


;; =============================================================================
;; BASIC USAGE
;; =============================================================================

(defn basic-usage-section
  "Simple HTMX + Ty examples"
  []
  (doc-section "Basic Usage"
               [:div
                [:p.ty-text-.mb-4
                 "Use Ty components with HTMX attributes for server-driven interactivity:"]

                [:h3.text-lg.font-semibold.ty-text.mb-3 "Simple Button"]

                (code-block
                  "<!-- Flask template -->
<ty-button 
  class=\"ty-bg-primary ty-text++ px-4 py-2 rounded flex items-center gap-2\"
  hx-get=\"/api/hello\" 
  hx-target=\"#response\">
  <ty-icon name=\"check\"></ty-icon>
  Click Me
</ty-button>

<div id=\"response\" class=\"ty-text mt-4\"></div>"
                  "html")

                [:p.ty-text-.mt-4.mb-4
                 "Flask route:"]

                (code-block
                  "@app.route('/api/hello')
def hello():
    return '<p class=\"ty-text-success\">Hello from the server!</p>'"
                  "python")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-6 "Form with Ty Input"]

                (code-block
                  "<!-- Search with live results -->
<ty-input 
  type=\"text\" 
  placeholder=\"Search users...\"
  class=\"ty-input ty-border border rounded-md px-3 py-2 w-full\"
  hx-get=\"/api/users/search\" 
  hx-trigger=\"keyup changed delay:300ms\" 
  hx-target=\"#results\">
</ty-input>

<div id=\"results\" class=\"mt-4\"></div>"
                  "html")

                [:p.ty-text-.mt-4.mb-4
                 "Flask endpoint returns HTML:"]

                (code-block
                  "@app.route('/api/users/search')
def search_users():
    query = request.args.get('q', '').lower()
    users = [u for u in USERS if query in u['name'].lower()]
    return render_template('partials/user_results.html', users=users)"
                  "python")]))

;; =============================================================================
;; ICON BUNDLING (PRODUCTION)
;; =============================================================================

(defn icon-bundling-section
  "Production icon bundling with esbuild"
  []
  (doc-section "Icon Bundling (Production)"
               [:div
                [:p.ty-text-.mb-4
                 "For production Flask/HTMX apps, bundle icons at build time instead of loading from CDN:"]

                (code-block
                  "# 1. Install build dependencies
npm init -y
npm install --save-dev esbuild @gersak/ty

# 2. Add to package.json scripts:
{
  \"scripts\": {
    \"build:icons\": \"esbuild static/js/icons.js --bundle --minify --format=iife --tree-shaking=true --outfile=static/dist/icons.js\"
  }
}

# 3. Build:
npm run build:icons

# 4. Load in template:
<script defer src=\"{{ url_for('static', filename='dist/icons.js') }}\"></script>

# Result: ~1-2KB bundle instead of 897KB full library"
                  "bash")

                [:div.ty-bg-success-.border.ty-border-success.rounded.p-4.mt-4
                 [:p.ty-text-success.text-sm.mb-2
                  "✅ " [:strong "Benefits of build-time bundling"]]
                 [:ul.ty-text-success.text-sm.space-y-1.ml-4
                  [:li "• Much smaller bundle (~1-2KB vs loading from CDN)"]
                  [:li "• Single HTTP request instead of separate icon imports"]
                  [:li "• Better for production caching and performance"]
                  [:li "• Tree-shaking removes unused icons automatically"]]]]))

;; =============================================================================
;; MAIN VIEW
;; =============================================================================

(defn view
  "Main view for HTMX documentation"
  []
  (docs-page
   [:h1.text-4xl.font-bold.ty-text.mb-4 "HTMX Integration"]
   [:p.text-xl.ty-text-.mb-6
    "Discover how to use Ty components with HTMX for dynamic server-side applications."]

   (installation-section)
   (setup-section)
   (icon-registration-section)
   (basic-usage-section)

   ;; Examples Reference
   [:div.ty-bg-neutral-.border.ty-border.rounded.p-6.mt-12
    [:h2.text-2xl.font-bold.ty-text.mb-4 "Complete Example"]
    [:p.ty-text-.mb-4
     "A full Flask + HTMX + Ty example application is available in the repository:"]
    [:div.ty-elevated.rounded.p-4.mb-4
     [:p.ty-text.text-sm.mb-2.font-semibold "Features:"]
     [:ul.ty-text.text-sm.space-y-1.ml-4
      [:li "• Live user search with beautiful result cards"]
      [:li "• Interactive calendar with date selection"]
      [:li "• Real-time form validation"]
      [:li "• Dynamic modals with server content"]
      [:li "• Dark/light theme switching"]
      [:li "• Responsive mobile design"]]]
    [:button.ty-bg-primary.ty-text++.px-4.py-2.rounded.hover:opacity-90
     {:on {:click #(js/window.open "https://github.com/gersak/ty/tree/master/examples" "_blank")}}
     "View Flask Example"]]

   ;; Next steps
   [:div.ty-bg-neutral-.border.ty-border.rounded.p-6.mt-8
    [:h2.text-2xl.font-bold.ty-text.mb-4 "Next Steps"]
    [:ul.space-y-2.ty-text-.text-sm
     [:li "• Review the " [:strong "CSS System Guide"] " for styling best practices"]
     [:li "• Browse " [:strong "component documentation"] " for complete APIs"]
     [:li "• Explore the " [:strong "htmx-flask example"] " for working code"]
     [:li "• Check " [:a.ty-text-primary.hover:underline {:href "https://htmx.org/docs/"} "HTMX documentation"] " for advanced patterns"]]]))
