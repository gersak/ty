(ns ty.site.docs.react
  "Documentation for using Ty with ClojureScript React libraries (UIx, Reagent, etc.)"
  (:require
    [ty.site.docs.common :refer [code-block doc-section example-section docs-page]]))

;; =============================================================================
;; DEPENDENCIES & SETUP
;; =============================================================================

(defn dependencies-section
  "Dependencies section - shows React wrapper with optional icons"
  []
  (doc-section "Dependencies"
               [:div
                [:p.ty-text-.mb-4
                 "Add the React wrapper to your " [:code.ty-bg-neutral-.px-2.py-1.rounded "package.json"] " file:"]

                (code-block
                  "{
  \"dependencies\": {
    \"@gersak/ty-react\": \"^0.0.5\",
    \"react\": \"^18.2.0\",
    \"react-dom\": \"^18.2.0\"
  }
}"
                  "json")

                [:div.ty-bg-info-.border.ty-border-info.rounded.p-3.mt-4.mb-4
                 [:p.ty-text-info.text-sm.mb-2
                  "💡 " [:strong "Optional: Tree-shakeable Icons"]]
                 [:p.ty-text-info.text-sm
                  "Add the " [:code.ty-bg-info.px-1.rounded "ty-icons"] " artifact to your "
                  [:code.ty-bg-info.px-1.rounded "deps.edn"] " file if you want to minimize icon footprint using Google Closure Compiler:"]]

                (code-block
                  ";; deps.edn - Optional for tree-shakeable icons
{:deps {com.pitch/uix.core {:mvn/version \"1.1.0\"}
        com.pitch/uix.dom {:mvn/version \"1.1.0\"}
        dev.gersak/ty-icons {:mvn/version \"LATEST\"}}}  ;; ← Optional"
                  "clojure")

                [:p.ty-text-.mt-4.text-sm
                 "The " [:code.ty-bg-neutral-.px-1.rounded "@gersak/ty-react"] " package provides React components with proper TypeScript definitions and React-optimized behavior. "
                 "The " [:code.ty-bg-neutral-.px-1.rounded "ty-icons"] " ClojureScript artifact is only needed if you want Google Closure Compiler to tree-shake unused icons from your bundle."]

                [:p.ty-text-.mt-4.text-sm
                 "💡 " [:strong "Note:"] " The ClojureScript artifact "
                 [:code.ty-bg-neutral-.px-1.rounded "dev.gersak/ty"] " is only needed for advanced features like Router, i18n, or Layout (see Advanced Features below)."]]))

(defn html-setup-section
  "HTML setup section - CDN or bundler approach"
  []
  (doc-section "HTML Setup"
               [:div
                [:p.ty-text-.mb-4
                 "Include the Ty CSS and JavaScript in your HTML " [:code.ty-bg-neutral-.px-2.py-1.rounded "<head>"] ". "
                 "You can use the CDN or include the files from your build process:"]

                (code-block
                  "<!DOCTYPE html>
<html lang=\"en\">
<head>
  <meta charset=\"UTF-8\">
  <title>My Ty + React App</title>
  
  <!-- Ty CSS and Components (CDN) -->
  <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@gersak/ty/css/ty.css\">
  <script src=\"https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.js\"></script>
  
  <!-- OR use your bundler to include ty.js from node_modules -->
  <!-- <script src=\"/js/vendor/ty.js\"></script> -->
</head>
<body class=\"ty-canvas\">
  <div id=\"app\"></div>
  
  <!-- Your compiled ClojureScript with DEFER -->
  <script defer src=\"/js/main.js\"></script>
</body>
</html>"
                  "html")

                [:p.ty-text-.mt-4
                 "The script automatically registers all 18+ Ty web components. They're immediately available via the React wrappers."]

                [:div.ty-bg-danger-.border.ty-border-danger.rounded.p-4.mt-4
                 [:p.ty-text-danger.font-semibold.mb-2
                  "⚠️ IMPORTANT: Use " [:code.ty-bg-danger.px-1.rounded "defer"] " for your compiled ClojureScript!"]
                 [:p.ty-text-danger.text-sm
                  "Always add the " [:code.ty-bg-danger.px-1.rounded "defer"] " attribute to your compiled ClojureScript script tag. "
                  "This ensures that " [:code.ty-bg-danger.px-1.rounded "ty.js"] " loads first and properly registers the "
                  [:code.ty-bg-danger.px-1.rounded "window.tyIcons"] " object before your icon registration code runs. "
                  "Without " [:code.ty-bg-danger.px-1.rounded "defer"] ", icon registration may fail silently."]]

                [:div.ty-bg-info-.border.ty-border-info.rounded.p-3.mt-4
                 [:p.ty-text-info.text-sm
                  "💡 " [:strong "Note:"] " Any published " [:code.ty-bg-info.px-1.rounded "ty.js"] " file downloaded or distributed in any way will work out of the box. "
                  "You can use CDN, copy files to your assets folder, or bundle with your build tool."]]]))

(defn icon-registration-section
  "Icon registration section with tree-shakeable ClojureScript imports"
  []
  (doc-section "Icon Registration"
               [:div
                [:p.ty-text-.mb-4
                 "Import icons from the " [:code.ty-bg-neutral-.px-1.rounded "ty-icons"] " artifact and register them using the global API:"]

                (code-block
                  "(ns my-app.core
  (:require [uix.core :as uix :refer [defui $]]
            [uix.dom]
            [\"@gersak/ty-react\" :as ty]
            [ty.lucide :as lucide]))  ;; Tree-shakeable icon imports

;; Register only the icons you need
(js/window.tyIcons.register 
  #js {\"check\" lucide/check
       \"heart\" lucide/heart
       \"save\" lucide/save
       \"trash\" lucide/trash})

;; Now use icons in your components
(defui my-button []
  ($ ty/Button {:flavor \"primary\"}
     ($ ty/Icon {:name \"check\"})
     \"Save\"))"
                  "clojure")

                [:div.ty-bg-info-.border.ty-border-info.rounded.p-4.mt-4
                 [:p.ty-text-info.text-sm.mb-2
                  "👍 " [:strong "Tree-shaking benefits:"]]
                 [:ul.ty-text-info.text-sm.space-y-1.ml-4
                  [:li "• Only imported icons are included in your bundle"]
                  [:li "• Google Closure Compiler removes unused code"]
                  [:li "• Works with any ClojureScript project"]
                  [:li "• Uses the same " [:code.ty-bg-info.px-1.rounded "window.tyIcons.register"] " API as JavaScript"]]]]))

(defn react-wrapper-section
  "Section explaining React wrapper benefits"
  []
  (doc-section "Why Use @gersak/ty-react?"
               [:div
                [:p.ty-text-.mb-4
                 "The " [:code.ty-bg-neutral-.px-1.rounded "@gersak/ty-react"] " package provides React-optimized wrappers around Ty web components:"]

                [:div.ty-bg-neutral-.border.ty-border.rounded.p-4.mb-4
                 [:h3.text-lg.font-semibold.ty-text+.mb-2 "🎯 Recommended Approach"]
                 [:p.ty-text.text-sm.mb-3
                  "Use the React wrapper for the best developer experience with full TypeScript support, proper React integration, and optimized performance."]

                 [:div.ty-bg-warning-.border.ty-border-warning.rounded.p-3.mb-3
                  [:p.ty-text-warning.text-xs.font-medium.mb-2 "⚠️ Critical: React & CustomEvents"]
                  [:p.ty-text-warning.text-xs
                   "React cannot pick up CustomEvents from web components. The @gersak/ty-react wrapper listens for these CustomEvents and converts them to React SyntheticEvents, ensuring proper event handling and state updates."]]

                 [:ul.ty-text.text-xs.space-y-1
                  [:li "• Converts web component CustomEvents to React SyntheticEvents"]
                  [:li "• Better React integration (proper props, refs, events)"]
                  [:li "• Full TypeScript support with autocomplete"]
                  [:li "• Optimized for React's reconciliation"]
                  [:li "• Better development experience"]
                  [:li "• Still works with all Ty features (i18n, routing, etc.)"]]]

                [:div.ty-bg-neutral-.border.ty-border.rounded.p-4
                 [:h3.text-lg.font-semibold.ty-text.mb-2 "⚡ Alternative: Direct Web Components"]
                 [:p.ty-text-.text-sm.mb-3
                  "You can also use web components directly if you prefer minimal dependencies or need custom integration patterns:"]

                 (code-block
                   "(ns my-app.core
  (:require [uix.core :as uix :refer [defui]]
            [uix.dom]))

;; Web components are available as :ty-button, :ty-input, etc.
(defui my-component []
  ($ :div
     ($ :ty-button {:flavor \"primary\"} \"Click me\")))"
                   "clojure")

                 [:p.ty-text-.text-sm.mt-3
                  "However, you'll need to manually handle CustomEvent conversion for proper React integration."]]]))

;; =============================================================================
;; BASIC USAGE
;; =============================================================================

(defn basic-usage-section
  "Basic usage examples with UIx and icons"
  []
  (doc-section "Basic Usage"
               [:div
                [:h3.text-lg.font-semibold.ty-text.mb-3 "Using React Wrapper Components"]
                [:p.ty-text-.mb-4
                 "Here's a simple example using the React wrapper with icons:"]

                (code-block
                  "(ns my-app.core
  (:require [uix.core :as uix :refer [defui $]]
            [uix.dom]
            [\"@gersak/ty-react\" :as ty]
            [ty.lucide :as lucide]))

;; Register icons
(js/window.tyIcons.register
  #js {\"check\" lucide/check
       \"heart\" lucide/heart})

;; Create state
(defonce app-state (uix/use-state {:message \"Hello from Ty!\"
                                    :liked false}))

;; Component with icons
(defui hello-view []
  (let [[state set-state!] app-state]
    ($ :div.ty-elevated.p-6.rounded-lg.max-w-md.mx-auto
       ($ :h2.ty-text++.text-xl.mb-4 (:message state))
       
       ($ :div.flex.gap-2
          ($ ty/Button {:flavor \"primary\"
                        :onClick #(set-state! assoc :message \"Button clicked!\")}
             ($ ty/Icon {:name \"check\"})
             \"Click me\")
          
          ($ ty/Button {:flavor (if (:liked state) \"danger\" \"outline\")
                        :onClick #(set-state! update :liked not)}
             ($ ty/Icon {:name \"heart\"})
             (if (:liked state) \"Liked!\" \"Like\"))))))

(defn mount! []
  (uix.dom/render ($ hello-view) (js/document.getElementById \"app\")))"
                  "clojure")

                [:p.ty-text-.mt-4
                 "This creates a reactive app with Ty's styling, components, and icons working together."]

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-6 "Working with State"]
                [:p.ty-text-.mb-4 "Complex state management with multiple Ty components:"]

                (code-block
                  "(defui form-example []
  (let [[form-data set-form-data!] (uix/use-state {:name \"\" :email \"\" :locale :en})
        update-field! (fn [field value]
                        (set-form-data! #(assoc % field value)))]
    
    ($ :div.ty-elevated.p-6.rounded-lg
       ($ :h2.ty-text++.text-xl.mb-4 \"User Form\")
       
       ;; Name input
       ($ :div.mb-4
          ($ :label.ty-text+.block.text-sm.font-medium.mb-2 \"Name\")
          ($ ty/Input {:value (:name form-data)
                       :placeholder \"Enter your name\"
                       :onChange #(update-field! :name (.. % -target -value))}))
       
       ;; Email input  
       ($ :div.mb-4
          ($ :label.ty-text+.block.text-sm.font-medium.mb-2 \"Email\")
          ($ ty/Input {:type \"email\"
                       :value (:email form-data)
                       :placeholder \"Enter your email\"
                       :onChange #(update-field! :email (.. % -target -value))}))
       
       ;; Submit button
       ($ ty/Button {:flavor \"primary\" 
                     :disabled (or (empty? (:name form-data))
                                   (empty? (:email form-data)))}
          \"Submit\"))))"
                  "clojure")

                [:p.ty-text-.mt-4
                 "This creates a fully functional form with Ty's styling, validation states, and React-optimized updates."]

                ;; Technical explanation section  
                [:div.ty-bg-neutral-.border.ty-border.rounded.p-4.mt-6
                 [:h3.text-lg.font-semibold.ty-text.mb-3 "🔧 Technical: Why the React Wrapper Matters"]
                 [:p.ty-text-.text-sm.mb-3
                  "Web components communicate through " [:code.ty-bg-neutral-.px-1.rounded "CustomEvents"] ", but React's event system only recognizes " [:code.ty-bg-neutral-.px-1.rounded "SyntheticEvents"] ". This creates a compatibility gap:"]
                 [:div.grid.grid-cols-1.md:grid-cols-2.gap-4.text-xs
                  [:div
                   [:h4.font-medium.ty-text-.mb-2 "❌ Direct Web Components"]
                   [:ul.space-y-1.ty-text--.text-xs
                    [:li "• CustomEvents don't trigger React re-renders"]
                    [:li "• Event handlers may not fire properly"]
                    [:li "• State updates can be missed"]
                    [:li "• Requires manual event listener setup"]]]
                  [:div
                   [:h4.font-medium.ty-text-.mb-2 "✅ @gersak/ty-react Wrapper"]
                   [:ul.space-y-1.ty-text--.text-xs
                    [:li "• Automatically converts CustomEvents → SyntheticEvents"]
                    [:li "• Perfect React integration and re-renders"]
                    [:li "• All state updates work as expected"]
                    [:li "• Clean, declarative event handling"]]]]]]))

;; =============================================================================
;; ROUTING WITH UIX
;; =============================================================================

(defn routing-section
  "Complete routing section with ty.router and UIx"
  []
  (doc-section "🎯 Router - Tree-based Routing with Authorization"
               [:div.p-4.ty-content.rounded-lg
                [:div.ty-bg-warning-.border.ty-border-warning.rounded.p-3.mb-4
                 [:p.ty-text-warning.text-sm
                  "💡 " [:strong "Note:"] " The router requires the " [:code.ty-bg-warning.px-1.rounded "dev.gersak/ty"] " artifact:"]]

                (code-block
                  ";; deps.edn - Add ty artifact for router
{:deps {com.pitch/uix.core {:mvn/version \"1.1.0\"}
        com.pitch/uix.dom {:mvn/version \"1.1.0\"}
        dev.gersak/ty-icons {:mvn/version \"LATEST\"}
        dev.gersak/ty {:mvn/version \"LATEST\"}}}  ;; ← For router"
                  "clojure")

                [:p.ty-text-.mb-4.mt-6
                 "Ty's routing system works seamlessly with UIx and other React-based ClojureScript libraries. The same hierarchical routing approach applies."]

                [:h3.text-lg.font-semibold.ty-text.mb-3 "UIx Router Integration"]
                [:p.ty-text-.mb-4
                 "Here's how to set up routing with UIx components:"]

                (example-section
                  "Main App Component"
                  [:div.ty-bg-info-.border.ty-border-info.rounded.p-3.mb-4
                   [:p.ty-text.text-sm "Root component that handles routing and navigation"]]
                  "(ns my-app.main
  (:require [uix.core :as uix :refer [defui $]]
            [uix.dom]
            [ty.router :as router]
            [\"@gersak/ty-react\" :as ty]
            [my-app.dashboard :as dashboard]
            [my-app.users :as users]))

;; Define main routes
(router/link :app/main
  [{:id :app/home
    :segment \"home\"
    :view home-view}
   {:id :app/dashboard
    :segment \"dashboard\"
    :view dashboard/view}
   {:id :app/users  
    :segment \"users\"
    :view users/view}])

(defui navigation []
  ($ :nav.ty-elevated.p-4.mb-6
     ($ :div.flex.gap-4
        ($ ty/Button {:flavor (if (router/rendered? :app/home) \"primary\" \"secondary\")
                     :onClick #(router/navigate! :app/home)}
           \"Home\")
        ($ ty/Button {:flavor (if (router/rendered? :app/dashboard) \"primary\" \"secondary\")
                     :onClick #(router/navigate! :app/dashboard)}
           \"Dashboard\")
        ($ ty/Button {:flavor (if (router/rendered? :app/users) \"primary\" \"secondary\")
                     :onClick #(router/navigate! :app/users)}
           \"Users\"))))

(defui home-view []
  ($ :div.ty-elevated.p-8.rounded-lg
     ($ :h1.ty-text++.text-3xl.mb-4 \"Welcome to My App\")
     ($ :p.ty-text \"Select a page from the navigation above.\")))

(defui main-app []
  ;; Force re-render when route changes
  (let [[_ set-tick!] (uix/use-state 0)]
    (uix/use-effect 
     (fn []
       (let [handler #(set-tick! inc)]
         (js/window.addEventListener \"popstate\" handler)
         #(js/window.removeEventListener \"popstate\" handler)))
     [])
    
    ($ :div
       ($ navigation)
       (cond
         (router/rendered? :app/home) ($ home-view)
         (router/rendered? :app/dashboard) ($ dashboard/view)
         (router/rendered? :app/users) ($ users/view)
         :else ($ :div \"Not found\")))))"
                  "clojure")

                (example-section
                  "Dashboard Module"
                  [:div.ty-bg-success-.border.ty-border-success.rounded.p-3.mb-4
                   [:p.ty-text-success.text-sm "Separate module with its own sub-routes and components"]]
                  "(ns my-app.dashboard
  (:require [uix.core :as uix :refer [defui $]]
            [ty.router :as router]
            [\"@gersak/ty-react\" :as ty]
            [ty.i18n :as i18n]))

;; Define dashboard sub-routes
(def routes
  [{:id :app.dashboard/overview
    :segment \"overview\" 
    :view overview-view}
   {:id :app.dashboard/analytics
    :segment \"analytics\"
    :view analytics-view}
   {:id :app.dashboard/settings
    :segment \"settings\"
    :view settings-view}])

;; Link sub-routes to parent
(router/link :app/dashboard routes)

(defui dashboard-nav []
  ($ :div.ty-elevated.p-4.mb-4
     ($ :h3.ty-text++.mb-2 \"Dashboard\")
     ($ :div.flex.gap-2
        ($ ty/Button {:flavor (if (router/rendered? :app.dashboard/overview) \"primary\" \"outline\")
                     :size \"sm\"
                     :onClick #(router/navigate! :app.dashboard/overview)}
           \"Overview\")
        ($ ty/Button {:flavor (if (router/rendered? :app.dashboard/analytics) \"primary\" \"outline\")
                     :size \"sm\"
                     :onClick #(router/navigate! :app.dashboard/analytics)}
           \"Analytics\")
        ($ ty/Button {:flavor (if (router/rendered? :app.dashboard/settings) \"primary\" \"outline\")
                     :size \"sm\"
                     :onClick #(router/navigate! :app.dashboard/settings)}
           \"Settings\"))))

(defui overview-view []
  ($ :div.ty-elevated.p-6.rounded-lg
     ($ :h2.ty-text++.text-xl.mb-4 \"Dashboard Overview\")
     ($ :div.grid.grid-cols-1.md:grid-cols-3.gap-4
        ($ :div.ty-content.p-4.rounded
           ($ :h3.ty-text+.font-medium \"Total Users\")
           ($ :p.ty-text++.text-2xl \"1,234\"))
        ($ :div.ty-content.p-4.rounded
           ($ :h3.ty-text+.font-medium \"Revenue\")
           ($ :p.ty-text++.text-2xl \"$52,340\"))
        ($ :div.ty-content.p-4.rounded
           ($ :h3.ty-text+.font-medium \"Growth\")
           ($ :p.ty-text++.text-2xl \"+12%\")))))

(defui analytics-view []
  ($ :div.ty-elevated.p-6.rounded-lg
     ($ :h2.ty-text++.text-xl.mb-4 \"Analytics\")
     ($ :p.ty-text \"Analytics dashboard coming soon...\")))

(defui settings-view []
  ($ :div.ty-elevated.p-6.rounded-lg
     ($ :h2.ty-text++.text-xl.mb-4 \"Settings\")
     ($ :p.ty-text \"Settings panel coming soon...\")))

(defui view []
  ($ :div
     ($ dashboard-nav)
     ;; Render active sub-route
     (some (fn [{:keys [id view-fn]}]
             (when (router/rendered? id true)
               ($ view-fn)))
           routes)))"
                  "clojure")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-8 "Key Router Functions"]
                [:div.space-y-4
                 [:div
                  [:h4.font-medium.ty-text.mb-2 [:code.ty-bg-neutral-.px-2.py-1.rounded "router/navigate!"]]
                  [:p.ty-text-.text-sm "Programmatically navigate to a route:"]]

                 (code-block
                   ";; Navigate to specific routes
(router/navigate! :app/home)
(router/navigate! :app.dashboard/overview)

;; Navigate with URL query parameters
(router/navigate! :app.dashboard/user {:id 123})"
                   "clojure")

                 [:div
                  [:h4.font-medium.ty-text.mb-2 [:code.ty-bg-neutral-.px-2.py-1.rounded "router/rendered?"]]
                  [:p.ty-text-.text-sm "Check if a route is currently active for conditional rendering:"]]

                 (code-block
                   ";; Check exact route
(router/rendered? :app/dashboard true)

;; Check if route is on URL path
(router/rendered? :app/dashboard)

;; Use in conditional rendering
($ ty/Button {:flavor (if (router/rendered? :app/home) \"primary\" \"secondary\")
             :onClick #(router/navigate! :app/home)}
   \"Home\")"
                   "clojure")]]))

;; =============================================================================
;; INTERNATIONALIZATION WITH REACT
;; =============================================================================

(defn i18n-section
  "Complete i18n section for React components"
  []
  (doc-section "🌍 i18n - Internationalization System"
               [:div.p-4.ty-content.rounded-lg
                [:div.ty-bg-warning-.border.ty-border-warning.rounded.p-3.mb-4
                 [:p.ty-text-warning.text-sm
                  "💡 " [:strong "Note:"] " The i18n system requires the " [:code.ty-bg-warning.px-1.rounded "dev.gersak/ty"] " artifact:"]]

                (code-block
                  ";; deps.edn - Add ty artifact for i18n
{:deps {com.pitch/uix.core {:mvn/version \"1.1.0\"}
        com.pitch/uix.dom {:mvn/version \"1.1.0\"}
        dev.gersak/ty-icons {:mvn/version \"LATEST\"}
        dev.gersak/ty {:mvn/version \"LATEST\"}}}  ;; ← For i18n"
                  "clojure")

                [:p.ty-text-.mb-4.mt-6
                 "Ty's i18n system works seamlessly with React components. The same translation and formatting APIs apply, with some React-specific patterns for state management."]

                [:h3.text-lg.font-semibold.ty-text.mb-3 "Setting Up Translations"]
                [:p.ty-text-.mb-4
                 "Translation setup is identical to other frameworks:"]

                (example-section
                  "Adding Translations"
                  [:div.ty-bg-info-.border.ty-border-info.rounded.p-3.mb-4
                   [:p.ty-text.text-sm "Set up translations using qualified keywords"]]
                  "(ns my-app.core
  (:require [uix.core :as uix :refer [defui $]]
            [ty.react :refer [TyButton]]
            [ty.i18n :as i18n]
            [ty.i18n.keyword :as i18n-kw]))

;; Add translations
(i18n-kw/add-translations
  #:app {:title/default \"My Application\"
         :title/hr \"Moja Aplikacija\"
         :title/de \"Meine Anwendung\"}
  #:nav {:home/default \"Home\"
         :home/hr \"Početna\"
         :home/de \"Startseite\"
         :about/default \"About\"
         :about/hr \"O nama\"
         :about/de \"Über uns\"}
  #:form {:save/default \"Save\"
          :save/hr \"Spremi\"
          :save/de \"Speichern\"
          :cancel/default \"Cancel\"
          :cancel/hr \"Odustani\"
          :cancel/de \"Abbrechen\"})"
                  "clojure")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-6 "Locale State Management"]
                [:p.ty-text-.mb-4 "Manage current locale with React state:"]

                (example-section
                  "Locale Context Provider"
                  [:div.ty-bg-success-.border.ty-border-success.rounded.p-3.mb-4
                   [:p.ty-text-success.text-sm "Create a context provider for locale management"]]
                  ";; Create locale context
(def locale-context (uix/create-context))

(defui locale-provider [{:keys [children]}]
  (let [[locale set-locale!] (uix/use-state :en)]
    ($ (.-Provider locale-context) 
       {:value {:locale locale :set-locale! set-locale!}}
       children)))

;; Custom hook for locale
(defn use-locale []
  (uix/use-context locale-context))

;; Usage in components
(defui welcome-component []
  (let [{:keys [locale]} (use-locale)]
    (binding [i18n/*locale* locale]
      ($ :div.ty-elevated.p-6.rounded-lg
         ($ :h1.ty-text++.text-2xl.mb-4 (i18n/t :app/title))
         ($ :p.ty-text (i18n/t :welcome/message))))))"
                  "clojure")

                (example-section
                  "Language Switcher Component"
                  [:div.ty-bg-warning-.border.ty-border-warning.rounded.p-3.mb-4
                   [:p.ty-text-warning.text-sm "Interactive language switcher using React state"]]
                  "(defui language-switcher []
  (let [{:keys [locale set-locale!]} (use-locale)
        languages [{:code :en :name \"English\"}
                   {:code :hr :name \"Hrvatski\"}
                   {:code :de :name \"Deutsch\"}]]
    
    ($ :div.flex.items-center.gap-4
       ($ :span.font-medium.ty-text \"Language:\")
       ($ :div.flex.gap-2
          (for [lang languages]
            ($ TyButton {:key (name (:code lang))
                         :flavor (if (= locale (:code lang)) \"primary\" \"outline\")
                         :size \"sm\"
                         :onClick #(set-locale! (:code lang))}
               (:name lang)))))))"
                  "clojure")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-8 "Complete App Example"]
                [:p.ty-text-.mb-4 "Putting it all together in a real React app:"]

                (example-section
                  "Multi-language React App"
                  [:div.ty-bg-neutral-.border.ty-border.rounded.p-3.mb-4
                   [:p.ty-text-.text-sm "Complete example with locale management and translated components"]]
                  "(defui user-profile []
  (let [{:keys [locale]} (use-locale)
        [user-data set-user-data!] (uix/use-state {:name \"\" :email \"\" :bio \"\"})]
    
    (binding [i18n/*locale* locale]
      ($ :div.ty-elevated.p-6.rounded-lg
         ($ :h2.ty-text++.text-xl.mb-4 (i18n/t :profile/title))
         
         ;; Name field
         ($ :div.mb-4
            ($ :label.ty-text+.block.text-sm.font-medium.mb-2 
               (i18n/t :profile/name))
            ($ TyInput {:value (:name user-data)
                        :placeholder (i18n/t :profile/name-placeholder)
                        :onChange #(set-user-data! assoc :name (.. % -target -value))}))
         
         ;; Email field
         ($ :div.mb-4
            ($ :label.ty-text+.block.text-sm.font-medium.mb-2
               (i18n/t :profile/email))
            ($ TyInput {:type \"email\"
                        :value (:email user-data)
                        :placeholder (i18n/t :profile/email-placeholder)
                        :onChange #(set-user-data! assoc :email (.. % -target -value))}))
         
         ;; Bio field
         ($ :div.mb-6
            ($ :label.ty-text+.block.text-sm.font-medium.mb-2
               (i18n/t :profile/bio))
            ($ TyTextarea {:value (:bio user-data)
                           :placeholder (i18n/t :profile/bio-placeholder)
                           :rows 3
                           :onChange #(set-user-data! assoc :bio (.. % -target -value))}))
         
         ;; Action buttons
         ($ :div.flex.gap-2
            ($ TyButton {:flavor \"primary\"} (i18n/t :form/save))
            ($ TyButton {:flavor \"secondary\"} (i18n/t :form/cancel))))))

(defui main-app []
  ($ locale-provider
     ($ :div.max-w-4xl.mx-auto.p-6
        ($ :header.mb-6
           ($ :div.flex.justify-between.items-center
              ($ :h1.ty-text++.text-3xl \"My App\")
              ($ language-switcher)))
        
        ($ user-profile))))

;; Add profile translations
(i18n-kw/add-translations
  #:profile {:title/default \"User Profile\"
             :title/hr \"Korisnički Profil\"
             :title/de \"Benutzerprofil\"
             :name/default \"Full Name\"
             :name/hr \"Puno Ime\"
             :name/de \"Vollständiger Name\"
             :name-placeholder/default \"Enter your full name\"
             :name-placeholder/hr \"Unesite vaše puno ime\"
             :name-placeholder/de \"Geben Sie Ihren vollständigen Namen ein\"
             :email/default \"Email Address\"
             :email/hr \"Email Adresa\"
             :email/de \"E-Mail-Adresse\"
             :email-placeholder/default \"Enter your email\"
             :email-placeholder/hr \"Unesite vaš email\"
             :email-placeholder/de \"Geben Sie Ihre E-Mail ein\"
             :bio/default \"Biography\"
             :bio/hr \"Biografija\"
             :bio/de \"Biografie\"
             :bio-placeholder/default \"Tell us about yourself\"
             :bio-placeholder/hr \"Recite nam nešto o sebi\"
             :bio-placeholder/de \"Erzählen Sie uns von sich\"})"
                  "clojure")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-8 "Key i18n Functions"]
                [:div.space-y-4
                 [:div
                  [:h4.font-medium.ty-text.mb-2 [:code.ty-bg-neutral-.px-2.py-1.rounded "i18n/t"]]
                  [:p.ty-text-.text-sm "Translate keywords using current locale or direct qualified keywords:"]]

                 (code-block
                   ";; Unqualified keyword - uses current locale
(binding [i18n/*locale* :hr]
  (i18n/t :save))  ; Looks up :save/hr

;; Qualified keyword - ignores current locale
(i18n/t :save/de)  ; Always returns German translation

;; Use in React components
(defui my-button []
  (let [{:keys [locale]} (use-locale)]
    (binding [i18n/*locale* locale]
      ($ TyButton (i18n/t :form/save)))))"
                   "clojure")]]))

;; =============================================================================
;; FORMATTING WITH REACT
;; =============================================================================

(defn formatting-section
  "Complete formatting section for React components"
  []
  (doc-section "Number/Currency/Date Formatting"
               [:div.p-4.ty-content.rounded-lg
                [:div.ty-bg-warning-.border.ty-border-warning.rounded.p-3.mb-4
                 [:p.ty-text-warning.text-sm
                  "💡 " [:strong "Note:"] " Formatting requires the " [:code.ty-bg-warning.px-1.rounded "dev.gersak/ty"] " artifact (same as i18n above)."]]

                [:p.ty-text-.mb-4.mt-6
                 "Ty's formatting system works identically in React components. The same "
                 [:code.ty-bg-neutral-.px-1.rounded "i18n/t"]
                 " magic applies for automatic number, currency, and date formatting."]

                [:h3.text-lg.font-semibold.ty-text.mb-3 "React State Integration"]
                [:p.ty-text-.mb-4 "Combine formatting with React state for dynamic, locale-aware displays:"]

                (example-section
                  "Dynamic Price Display"
                  [:div.ty-bg-info-.border.ty-border-info.rounded.p-3.mb-4
                   [:p.ty-text.text-sm "Price component that updates with locale changes"]]
                  "(defui price-display [{:keys [amount currency]}]
  (let [{:keys [locale]} (use-locale)]
    (binding [i18n/*locale* locale]
      ($ :div.text-center
         ($ :div.ty-text-.text-sm.mb-1 \"Price\")
         ($ :div.ty-text++.text-2xl.font-bold 
            (i18n/t amount currency)))))

;; Usage
(defui product-card [{:keys [product]}]
  ($ :div.ty-elevated.p-4.rounded-lg
     ($ :h3.ty-text+.font-medium.mb-2 (:name product))
     ($ price-display {:amount (:price product) :currency \"USD\"})))"
                  "clojure")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-8 "Real-time Formatting Updates"]
                [:p.ty-text-.mb-4 "Watch formatting change in real-time as users switch locales:"]

                (code-block
                  "(defui formatting-showcase []
  (let [{:keys [locale]} (use-locale)
        sample-data {:number 1234567.89
                     :currency 99.99
                     :percentage 0.1234
                     :date (js/Date.)
                     :large-number 5432100}]
    
    (binding [i18n/*locale* locale]
      ($ :div.ty-elevated.p-6.rounded-lg
         ($ :h2.ty-text++.text-xl.mb-4 \"Formatting Showcase\")
         ($ :div.grid.grid-cols-1.md:grid-cols-2.gap-4.text-sm
            ;; Numbers
            ($ :div.space-y-2
               ($ :div ($ :strong \"Number: \") (i18n/t (:number sample-data)))
               ($ :div ($ :strong \"Currency: \") (i18n/t (:currency sample-data) \"USD\"))
               ($ :div ($ :strong \"Percentage: \") (i18n/t (:percentage sample-data) locale {:style \"percent\"}))
               ($ :div ($ :strong \"Compact: \") (i18n/t (:large-number sample-data) locale {:notation \"compact\"})))
            
            ;; Dates
            ($ :div.space-y-2
               ($ :div ($ :strong \"Date: \") (i18n/t (:date sample-data) \"medium\"))
               ($ :div ($ :strong \"Full: \") (i18n/t (:date sample-data) \"full\"))
               ($ :div ($ :strong \"Time: \") (i18n/t (:date sample-data) \"time\"))
               ($ :div ($ :strong \"Custom: \") (i18n/t (:date sample-data) locale {:weekday \"long\" :year \"numeric\"}))))))))

;; Use in app
($ :div
   ($ language-switcher)  ; Changes locale
   ($ formatting-showcase))  ; Automatically updates formatting"
                  "clojure")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-8 "Key Takeaways"]
                [:div.ty-bg-neutral-.rounded.p-4
                 [:ul.space-y-2.ty-text-.text-sm
                  [:li "• " [:code.ty-bg-neutral.px-1.rounded "i18n/t"] " automatically formats numbers, currencies, and dates"]
                  [:li "• Pass currency codes as strings: " [:code.ty-bg-neutral.px-1.rounded "(i18n/t 99.99 \"USD\")"]]
                  [:li "• Pass date styles as strings: " [:code.ty-bg-neutral.px-1.rounded "(i18n/t date \"full\")"]]
                  [:li "• Use options maps for custom formatting: " [:code.ty-bg-neutral.px-1.rounded "(i18n/t num locale {:style \"percent\"})"]]
                  [:li "• Bind " [:code.ty-bg-neutral.px-1.rounded "i18n/*locale*"] " to change formatting context dynamically"]
                  [:li "• Works seamlessly with React state and hooks"]]]]))

(defn layout-section
  "Layout system section for React"
  []
  (doc-section "📐 Layout - Container-Aware Responsive Utilities"
               [:div.p-4.ty-content.rounded-lg
                [:div.ty-bg-warning-.border.ty-border-warning.rounded.p-3.mb-4
                 [:p.ty-text-warning.text-sm
                  "💡 " [:strong "Note:"] " The layout system requires the " [:code.ty-bg-warning.px-1.rounded "dev.gersak/ty"] " artifact:"]]

                (code-block
                  ";; deps.edn - Add ty artifact for layout
{:deps {com.pitch/uix.core {:mvn/version \"1.1.0\"}
        com.pitch/uix.dom {:mvn/version \"1.1.0\"}
        dev.gersak/ty-icons {:mvn/version \"LATEST\"}
        dev.gersak/ty {:mvn/version \"LATEST\"}}}  ;; ← For layout system"
                  "clojure")

                [:p.ty-text-.mb-4.mt-6
                 "Ty provides container-aware responsive utilities that work alongside Tailwind CSS. Unlike viewport-based breakpoints, Ty's layout system adapts to the actual container size. "
                 "This works with UIx, Reagent, re-frame, and other React-based ClojureScript libraries."]

                [:h3.text-lg.font-semibold.ty-text.mb-3 "Container-Aware Rendering"]
                [:p.ty-text-.mb-4
                 "Bind a container context and use breakpoint utilities to adapt your UI:"]

                (code-block
                  "(ns my-app.components
  (:require [uix.core :as uix :refer [defui $]]
            [ty.layout :as layout]))

;; Container-aware component
(defui responsive-card []
  (layout/with-container {:width 800 :height 600}
    (if (layout/breakpoint>= :lg)
      ($ :div.desktop-layout
         ($ :h2 \"Large Screen View\")
         ($ :div.grid.grid-cols-3 \"...\"))  ;; 3-column grid
      ($ :div.mobile-layout
         ($ :h2 \"Small Screen View\")
         ($ :div.flex.flex-col \"...\")))))" ;; Stacked layout"
                  "clojure")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-6 "Breakpoint Utilities"]
                (code-block
                  ";; Breakpoint checks
(layout/breakpoint>= :md)  ;=> true/false
(layout/breakpoint<= :lg)  ;=> true/false
(layout/breakpoint= :sm)   ;=> true/false

;; Breakpoints: :xs :sm :md :lg :xl :2xl

;; Orientation detection
(layout/container-orientation)  ;=> :portrait | :landscape | :square"
                  "clojure")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-6 "Window Tracking with React"]
                [:p.ty-text-.mb-4
                 "Track window size changes reactively in React components:"]

                (code-block
                  ";; Subscribe to window size
@layout/window-size  ;=> {:width 1920 :height 1080}

;; Use in React components
(defui window-info []
  (let [size @layout/window-size]  ;; Reactive atom
    ($ :div
       ($ :p \"Window: \" (:width size) \" x \" (:height size))
       ($ :p \"Orientation: \" (name (layout/container-orientation))))))"
                  "clojure")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-6 "Practical React Example"]
                (code-block
                  "(defui adaptive-dashboard []
  ($ :div.ty-elevated.p-6
     ;; Adapt padding based on container size
     {:class (if (layout/breakpoint>= :lg) \"p-8\" \"p-4\")}
     
     ($ :h1.ty-text++.mb-4
        {:class (if (layout/breakpoint>= :md) \"text-3xl\" \"text-xl\")}
        \"Dashboard\")
     
     ;; Switch layout based on space
     (if (layout/breakpoint>= :lg)
       ;; Large: side-by-side
       ($ :div.grid.grid-cols-2.gap-6
          ($ stats-panel)
          ($ chart-panel))
       ;; Small: stacked
       ($ :div.space-y-4
          ($ stats-panel)
          ($ chart-panel)))))"
                  "clojure")

                [:p.ty-text-.mt-4.text-sm
                 "The layout system works identically across UIx, Reagent, re-form, and other React-based ClojureScript libraries."]]))

;; =============================================================================
;; MAIN VIEW
;; =============================================================================

(defn view
  "Main view for React/UIx getting started documentation"
  []
  (docs-page
   [:h1.text-4xl.font-bold.ty-text.mb-4 "Getting Started with ClojureScript React"]
   [:p.text-xl.ty-text-.mb-6
    "Learn how to use Ty web components with UIx, Reagent, and other React-based ClojureScript libraries."]

   ;; Basic setup sections
   (dependencies-section)
   (html-setup-section)
   (icon-registration-section)
   (react-wrapper-section)
   (basic-usage-section)

   ;; Advanced features section header
   [:div.mt-12.mb-6
    [:h2.text-3xl.font-bold.ty-text.mb-4 "Advanced ClojureScript Features"]
    [:p.ty-text-.mb-4
     "Beyond web components, Ty provides powerful ClojureScript infrastructure for routing, internationalization, formatting, and responsive layouts. "
     "These features require the " [:code.ty-bg-neutral-.px-1.rounded "dev.gersak/ty"] " artifact and work with UIx, Reagent, re-frame, and other React-based libraries:"]]

   ;; Advanced features
   (routing-section)
   (i18n-section)
   (formatting-section)
   (layout-section)))
