(ns ty.site.docs.react
  "Documentation for using Ty with ClojureScript React libraries (UIx, Reagent, etc.)"
  (:require
   [ty.site.docs.common :refer [code-block doc-section example-section]]))

;; =============================================================================
;; DEPENDENCIES & SETUP
;; =============================================================================

(defn dependencies-section
  "Dependencies and setup for React-based ClojureScript"
  []
  (list
    ;; Dependencies
   (doc-section "Dependencies"
                [:div
                 [:p.ty-text-.mb-4
                  "Add the required dependencies to your " [:code.ty-bg-neutral-.px-2.py-1.rounded "deps.edn"] " file:"]

                 (code-block
                  ";; deps.edn - ClojureScript dependencies
{:deps {dev.gersak/ty {:mvn/version \"LATEST\"}
        dev.gersak/ty-icons {:mvn/version \"LATEST\"}
        com.pitch/uix.core {:mvn/version \"1.1.0\"}
        com.pitch/uix.dom {:mvn/version \"1.1.0\"}}}"
                  "clojure")

                 [:p.ty-text-.my-4
                  "And add the React wrapper as an NPM dependency in your " [:code.ty-bg-neutral-.px-2.py-1.rounded "package.json"] " file:"]

                 (code-block
                  "{
  \"dependencies\": {
    \"@gersak/ty-react\": \"^0.0.5\",
    \"react\": \"^18.2.0\",
    \"react-dom\": \"^18.2.0\"
  }
}"
                  "json")

                 [:p.ty-text-.mt-4.text-sm
                  "Replace " [:code.ty-bg-neutral-.px-1.rounded "\"LATEST\""]
                  " with the current version numbers from Clojars."]])

    ;; Setup Options
   (doc-section "Setup Options"
                [:div
                 [:p.ty-text-.mb-4 "There are two ways to use Ty components with React-based ClojureScript libraries:"]

                 [:div.ty-bg-success-.border.ty-border-success.rounded.p-4.mb-4
                  [:h3.text-lg.font-semibold.ty-text-success.mb-2 "🎯 Recommended: @gersak/ty-react"]
                  [:p.ty-text-success.text-sm
                   "Use the React wrapper for the best developer experience with full TypeScript support, proper React integration, and optimized performance."]]

                 [:div.ty-bg-neutral-.border.ty-border.rounded.p-4
                  [:h3.text-lg.font-semibold.ty-text.mb-2 "⚡ Alternative: Direct Web Components"]
                  [:p.ty-text-.text-sm
                   "Use web components directly if you prefer minimal dependencies or need custom integration patterns."]]

                 [:h3.text-lg.font-semibold.ty-text.mb-3.mt-6 "Option 1: React Wrapper Setup"]
                 [:p.ty-text-.mb-4 "The " [:code.ty-bg-neutral-.px-1.rounded "@gersak/ty-react"] " package provides React components with proper TypeScript definitions and React-optimized behavior:"]

                 (code-block
                  "(ns my-app.core
  (:require [uix.core :as uix :refer [defui $]]
            [uix.dom]
            ;; Clean React wrappers
            [\"@gersak/ty-react\" :as ty]
            ;; Still need ty components for web component registration  
            [ty.components.core]
            [ty.i18n :as i18n]))"
                  "clojure")

                 [:h3.text-lg.font-semibold.ty-text.mb-3.mt-6 "Option 2: Direct Web Components"]
                 [:p.ty-text-.mb-4 "Register web components and use them directly in your JSX/hiccup:"]

                 (code-block
                  "(ns my-app.core
  (:require [uix.core :as uix :refer [defui]]
            [uix.dom]
            [ty.components]     ; <- Registers all web components
            [ty.i18n :as i18n]))

;; Web components are now available as :ty-button, :ty-input, etc."
                  "clojure")

                 [:div.ty-bg-info-.border.ty-border-info.rounded.p-3.mt-4
                  [:p.ty-text-info.text-sm.font-medium "💡 Why prefer @gersak/ty-react?"]
                  [:div.ty-bg-warning-.border.ty-border-warning.rounded.p-3.mb-3.mt-3
                   [:p.ty-text-warning.text-xs.font-medium.mb-2 "⚠️ Critical: React & CustomEvents"]
                   [:p.ty-text-warning.text-xs
                    "React cannot pick up CustomEvents from web components. The @gersak/ty-react wrapper listens for these CustomEvents and converts them to React SyntheticEvents, ensuring proper event handling and state updates."]]
                  [:ul.ty-text-info.text-xs.mt-2.space-y-1
                   [:li "• Converts web component CustomEvents to React SyntheticEvents"]
                   [:li "• Better React integration (proper props, refs, events)"]
                   [:li "• Full TypeScript support with autocomplete"]
                   [:li "• Optimized for React's reconciliation"]
                   [:li "• Better development experience"]
                   [:li "• Still works with all Ty features (i18n, routing, etc.)"]]]])))

;; =============================================================================
;; BASIC USAGE
;; =============================================================================

(defn basic-usage-section
  "Basic usage examples with UIx"
  []
  (doc-section "Basic Usage"
               [:div
                [:h3.text-lg.font-semibold.ty-text.mb-3 "Using React Wrapper Components"]
                [:p.ty-text-.mb-4
                 "Here's a simple example using the recommended React wrapper approach:"]

                (code-block
                 "(ns my-app.core
  (:require [uix.core :as uix :refer [defui $]]
            [uix.dom]
            [\"@gersak/ty-react\" :as ty]
            [ty.components.core]))

(defui hello-view []
  (let [[message set-message!] (uix/use-state \"Hello from Ty!\")]
    ($ :div.ty-elevated.p-6.rounded-lg.max-w-md.mx-auto
       ($ :h2.ty-text++.text-xl.mb-4 message)
       ($ ty/Button {:variant \"primary\"
                     :onClick #(set-message! \"Button clicked!\")}
          \"Click me\"))))

(defn mount! []
  (uix.dom/render ($ hello-view) (js/document.getElementById \"app\")))"
                 "clojure")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-6 "Using Direct Web Components"]
                [:p.ty-text-.mb-4 "Alternative approach using web components directly:"]

                (code-block
                 "(defui hello-view-alt []
  (let [[message set-message!] (uix/use-state \"Hello from Ty!\")]
    ($ :div.ty-elevated.p-6.rounded-lg.max-w-md.mx-auto
       ($ :h2.ty-text++.text-xl.mb-4 message)
       ($ :ty-button {:variant \"primary\"
                      :onClick #(set-message! \"Button clicked!\")}
          \"Click me\"))))"
                 "clojure")

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
       ($ ty/Button {:variant \"primary\" 
                     :disabled (or (empty? (:name form-data))
                                   (empty? (:email form-data)))}
          \"Submit\"))))"
                 "clojure")

                [:p.ty-text-.mt-4
                 "This creates a fully functional form with Ty's styling, validation states, and React-optimized updates."

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
                     [:li "• Clean, declarative event handling"]]]]]]]))

;; =============================================================================
;; ROUTING WITH UIX
;; =============================================================================

(defn routing-section
  "Complete routing section with ty.router and UIx"
  []
  (doc-section "Routing with ty.router"
               [:div.p-4.ty-content.rounded-lg
                [:p.ty-text-.mb-4
                 "Ty's routing system works seamlessly with UIx and other React-based ClojureScript libraries. The same hierarchical routing approach applies."]

                [:h3.text-lg.font-semibold.ty-text.mb-3 "UIx Router Integration"]
                [:p.ty-text-.mb-4
                 "Here's how to set up routing with UIx components:"]

                (example-section
                 "Main App Component"
                 [:div.ty-bg-info-.border.ty-border-info.rounded.p-3.mb-4
                  [:p.ty-text-info.text-sm "Root component that handles routing and navigation"]]
                 "(ns my-app.main
  (:require [uix.core :as uix :refer [defui $]]
            [uix.dom]
            [ty.router :as router]
            [ty.react :refer [TyButton]]
            [ty.components]
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
        ($ TyButton {:variant (if (router/rendered? :app/home) \"primary\" \"secondary\")
                     :onClick #(router/navigate! :app/home)}
           \"Home\")
        ($ TyButton {:variant (if (router/rendered? :app/dashboard) \"primary\" \"secondary\")
                     :onClick #(router/navigate! :app/dashboard)}
           \"Dashboard\")
        ($ TyButton {:variant (if (router/rendered? :app/users) \"primary\" \"secondary\")
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
            [ty.react :refer [TyButton TyTag]]
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
        ($ TyButton {:variant (if (router/rendered? :app.dashboard/overview) \"primary\" \"outline\")
                     :size \"sm\"
                     :onClick #(router/navigate! :app.dashboard/overview)}
           \"Overview\")
        ($ TyButton {:variant (if (router/rendered? :app.dashboard/analytics) \"primary\" \"outline\")
                     :size \"sm\"
                     :onClick #(router/navigate! :app.dashboard/analytics)}
           \"Analytics\")
        ($ TyButton {:variant (if (router/rendered? :app.dashboard/settings) \"primary\" \"outline\")
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
                 "clojure")]))

;; =============================================================================
;; INTERNATIONALIZATION WITH REACT
;; =============================================================================

(defn i18n-section
  "Complete i18n section for React components"
  []
  (doc-section "Internationalization (i18n)"
               [:div.p-4.ty-content.rounded-lg
                [:p.ty-text-.mb-4
                 "Ty's i18n system works seamlessly with React components. The same translation and formatting APIs apply, with some React-specific patterns for state management."]

                [:h3.text-lg.font-semibold.ty-text.mb-3 "Setting Up Translations"]
                [:p.ty-text-.mb-4
                 "Translation setup is identical to other frameworks:"]

                (example-section
                 "Adding Translations"
                 [:div.ty-bg-info-.border.ty-border-info.rounded.p-3.mb-4
                  [:p.ty-text-info.text-sm "Set up translations using qualified keywords"]]
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
                         :variant (if (= locale (:code lang)) \"primary\" \"outline\")
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
            ($ TyButton {:variant \"primary\"} (i18n/t :form/save))
            ($ TyButton {:variant \"secondary\"} (i18n/t :form/cancel))))))

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
                 "clojure")]))

;; =============================================================================
;; FORMATTING WITH REACT
;; =============================================================================

(defn formatting-section
  "Complete formatting section for React components"
  []
  (doc-section "Number/Currency/Date Formatting"
               [:div.p-4.ty-content.rounded-lg
                [:p.ty-text-.mb-4
                 "Ty's formatting system works identically in React components. The same "
                 [:code.ty-bg-neutral-.px-1.rounded "i18n/t"]
                 " magic applies for automatic number, currency, and date formatting."]

                [:h3.text-lg.font-semibold.ty-text.mb-3 "React State Integration"]
                [:p.ty-text-.mb-4 "Combine formatting with React state for dynamic, locale-aware displays:"]

                (example-section
                 "Dynamic Price Display"
                 [:div.ty-bg-info-.border.ty-border-info.rounded.p-3.mb-4
                  [:p.ty-text-info.text-sm "Price component that updates with locale changes"]]
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

                (example-section
                 "Multi-Currency Dashboard"
                 [:div.ty-bg-success-.border.ty-border-success.rounded.p-3.mb-4
                  [:p.ty-text-success.text-sm "Complete dashboard with revenue metrics in multiple currencies"]]
                 "(defui revenue-dashboard []
  (let [{:keys [locale]} (use-locale)
        [metrics set-metrics!] (uix/use-state {:revenue 1234567.89
                                               :growth 0.123
                                               :transactions 5847
                                               :last-updated (js/Date.)})
        [currency set-currency!] (uix/use-state \"USD\")
        currencies [\"USD\" \"EUR\" \"HRK\" \"JPY\"]]
    
    (binding [i18n/*locale* locale]
      ($ :div.ty-elevated.p-6.rounded-lg
         ;; Header with currency selector
         ($ :div.flex.justify-between.items-center.mb-6
            ($ :h2.ty-text++.text-xl (i18n/t :dashboard/title))
            ($ :div.flex.items-center.gap-2
               ($ :span.ty-text.text-sm \"Currency:\")
               (for [curr currencies]
                 ($ TyButton {:key curr
                              :variant (if (= currency curr) \"primary\" \"outline\")
                              :size \"sm\"
                              :onClick #(set-currency! curr)}
                    curr))))
         
         ;; Metrics grid
         ($ :div.grid.grid-cols-1.md:grid-cols-4.gap-4
            ;; Total revenue
            ($ :div.ty-content.p-4.rounded.text-center
               ($ :div.ty-text-.text-sm.mb-1 (i18n/t :metrics/revenue))
               ($ :div.ty-text++.text-2xl.font-bold 
                  (i18n/t (:revenue metrics) currency)))
            
            ;; Growth percentage
            ($ :div.ty-content.p-4.rounded.text-center
               ($ :div.ty-text-.text-sm.mb-1 (i18n/t :metrics/growth))
               ($ :div.ty-text++.text-2xl.font-bold 
                  (i18n/t (:growth metrics) locale {:style \"percent\"})))
            
            ;; Transaction count
            ($ :div.ty-content.p-4.rounded.text-center
               ($ :div.ty-text-.text-sm.mb-1 (i18n/t :metrics/transactions))
               ($ :div.ty-text++.text-2xl.font-bold 
                  (i18n/t (:transactions metrics) locale {:notation \"compact\"})))
            
            ;; Last updated
            ($ :div.ty-content.p-4.rounded.text-center
               ($ :div.ty-text-.text-sm.mb-1 (i18n/t :metrics/updated))
               ($ :div.ty-text+.text-sm
                  (i18n/t (:last-updated metrics) \"datetime\"))))))))

;; Add dashboard translations
(i18n-kw/add-translations
  #:dashboard {:title/default \"Revenue Dashboard\"
               :title/hr \"Nadzorna Ploča Prihoda\"
               :title/de \"Umsatz-Dashboard\"}
  #:metrics {:revenue/default \"Total Revenue\"
             :revenue/hr \"Ukupni Prihod\"
             :growth/default \"Growth Rate\"
             :growth/hr \"Stopa Rasta\"
             :transactions/default \"Transactions\"
             :transactions/hr \"Transakcije\"
             :updated/default \"Last Updated\"
             :updated/hr \"Zadnji Put Ažurirano\"})"
                 "clojure")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-8 "Date Range Picker"]
                [:p.ty-text-.mb-4 "Build locale-aware date components using Ty's date formatting:"]

                (example-section
                 "Locale-Aware Date Picker"
                 [:div.ty-bg-warning-.border.ty-border-warning.rounded.p-3.mb-4
                  [:p.ty-text-warning.text-sm "Date picker that displays in the user's locale"]]
                 "(defui date-range-picker []
  (let [{:keys [locale]} (use-locale)
        [start-date set-start-date!] (uix/use-state (js/Date.))
        [end-date set-end-date!] (uix/use-state (js/Date. (.setDate (js/Date.) (+ (.getDate (js/Date.)) 7))))
        [show-calendar set-show-calendar!] (uix/use-state false)]
    
    (binding [i18n/*locale* locale]
      ($ :div.space-y-4
         ;; Date display
         ($ :div.ty-elevated.p-4.rounded-lg
            ($ :h3.ty-text+.font-medium.mb-2 (i18n/t :date/range-title))
            ($ :div.flex.items-center.gap-2
               ($ :span.ty-text (i18n/t start-date \"medium\"))
               ($ :span.ty-text- \"to\")
               ($ :span.ty-text (i18n/t end-date \"medium\"))))
         
         ;; Relative time display
         ($ :div.ty-content.p-3.rounded
            ($ :div.text-xs.ty-text-.space-y-1
               ($ :div \"Start: \" (i18n/t start-date \"full\"))
               ($ :div \"End: \" (i18n/t end-date \"full\"))
               ($ :div \"Duration: \" (.ceil js/Math (/ (- (.getTime end-date) (.getTime start-date)) 
                                                        (* 1000 60 60 24))) \" days\")))
         
         ;; Month names in current locale
         ($ :div.ty-content.p-3.rounded
            ($ :h4.ty-text+.text-sm.font-medium.mb-2 (i18n/t :date/months-title))
            ($ :div.flex.flex-wrap.gap-1.text-xs
               (for [month (i18n/locale locale :months/short)]
                 ($ :span.px-2.py-1.ty-elevated.rounded {:key month} month))))))))

;; Add date translations
(i18n-kw/add-translations
  #:date {:range-title/default \"Selected Date Range\"
          :range-title/hr \"Odabrani Raspon Datuma\"
          :months-title/default \"Months\"
          :months-title/hr \"Mjeseci\"})"
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
                 "clojure")]))

;; =============================================================================
;; MAIN VIEW
;; =============================================================================

(defn view
  "Main view for React/UIx getting started documentation"
  []
  [:div.max-w-4xl.mx-auto.p-6
   [:h1.text-4xl.font-bold.ty-text.mb-4 "Getting Started with ClojureScript React"]
   [:p.text-xl.ty-text-.mb-6
    "Learn how to use Ty web components with UIx and other React-based ClojureScript libraries."]

   ;; All sections in logical order
   (dependencies-section)
   (basic-usage-section)
   (routing-section)
   (i18n-section)
   (formatting-section)

   ;; What's next
   [:div.ty-elevated.rounded-lg.p-6.mt-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "What's Next?"]
    [:p.ty-text-.mb-4 "This guide covers the essential patterns for using Ty with React-based ClojureScript libraries. Key takeaways:"]
    [:ul.list-disc.list-inside.space-y-1.ty-text-.text-sm
     [:li [:strong "@gersak/ty-react"] " - Preferred wrapper with full React integration and TypeScript support"]
     [:li [:strong "Direct Web Components"] " - Alternative approach using web components directly in JSX"]
     [:li [:strong "State Management"] " - Use React hooks for locale and routing state"]
     [:li [:strong "Context Patterns"] " - Provide locale context for consistent i18n throughout the app"]
     [:li [:strong "Formatting"] " - Same powerful i18n/t API works seamlessly with React state"]]
    [:div.ty-bg-success-.border.ty-border-success.rounded.p-4.mt-4
     [:p.ty-text-success.text-sm.font-medium "💡 Remember"]
     [:p.ty-text-success.text-xs.mt-1
      "The @gersak/ty-react wrapper provides the best developer experience with proper React integration, TypeScript support, and optimized performance. Use direct web components only when you need custom integration patterns."]]]])
