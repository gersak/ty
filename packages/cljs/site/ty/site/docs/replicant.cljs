(ns ty.site.docs.replicant
  "Documentation for using Ty with Replicant"
  (:require
    [ty.site.docs.common :refer [code-block doc-section example-section]]))

(defn basic-usage-section
  "Basic usage example section"
  []
  (doc-section "Basic Usage"
               [:div
                [:p.ty-text-.mb-4
                 "Here's a simple example to verify everything is working:"]

                (code-block
                  "(defonce app-state (r/atom {:message \"Hello from Ty!\"}))

(defn hello-view [state]
  [:div.ty-elevated.p-6.rounded-lg.max-w-md.mx-auto
   [:h2.ty-text++.text-xl.mb-4 (:message @state)]
   [:ty-button {:variant \"primary\"
                :on {:click #(swap! state assoc :message \"Button clicked!\")}}
    \"Click me\"]])

(defn mount! []
  (d/render
    [hello-view app-state]
    (js/document.getElementById \"app\")))"
                  "clojure")

                [:p.ty-text-.mt-4
                 "This creates a simple app with Ty's styling and a working button component."]]))

;; =============================================================================
;; ROUTING
;; =============================================================================

(defn routing-section
  "Complete routing section with ty.router"
  []
  (doc-section "Routing with ty.router"
               [:div.p-4.ty-content.rounded-lg
                [:p.ty-text-.mb-4
                 "Ty includes a powerful routing system that works great with Replicant. Routes can be defined in any namespace and linked together hierarchically."]

                [:h3.text-lg.font-semibold.ty-text.mb-3 "Multi-namespace Route Example"]
                [:p.ty-text-.mb-4
                 "Here's how to organize routes across multiple namespaces:"]

     ;; Main namespace
                (example-section
                  "Main Namespace (my-app.main)"
                  [:div.ty-bg-info-.border.ty-border-info.rounded.p-3.mb-4
                   [:p.ty-text.text-sm "Main namespace that links to site-specific routes"]]
                  "(ns my-app.main
  (:require [replicant.dom :as d]
            [replicant.core :as r]
            [ty.router :as router]
            [ty.components]
            [my-app.site-a :as site-a]
            [my-app.site-b :as site-b]))

;; Define main routes and link to sub-routes
(router/link :app/main
  [{:id :app/home
    :segment \"home\"
    :view home-view}
   {:id :app/site-a
    :segment \"site-a\"}  ; <- Link to siteA routes
   {:id :app/site-b
    :segment \"site-b\"}]) ; <- Link to siteB routes

(defn navigation []
  [:nav.ty-elevated.p-4.mb-6
   [:div.flex.gap-4
    [:ty-button {:variant (if (router/rendered? :app/home) \"primary\" \"secondary\")
                 :on {:click #(router/navigate! :app/home)}}
     \"Home\"]
    [:ty-button {:variant (if (router/rendered? :app/site-a) \"primary\" \"secondary\")
                 :on {:click #(router/navigate! :app/site-a)}}
     \"Site A\"]
    [:ty-button {:variant (if (router/rendered? :app/site-b) \"primary\" \"secondary\")
                 :on {:click #(router/navigate! :app/site-b)}}
     \"Site B\"]]])

(defn main-view []
  [:div
   [navigation]
   (cond
     (router/rendered? :app/home) [home-view]
     (router/rendered? :app/site-a) [site-a/view]
     (router/rendered? :app/site-b) [site-b/view]
     :else [:div \"Not found\"])])"
                  "clojure")

     ;; Site A namespace
                (example-section
                  "Site A Namespace (my-app.site-a)"
                  [:div.ty-bg-success-.border.ty-border-success.rounded.p-3.mb-4
                   [:p.ty-text-success.text-sm "Site A defines its own routes and sub-navigation"]]
                  "(ns my-app.site-a
  (:require [ty.router :as router]))

;; Define routes for this namespace
(def routes
  [{:id :app.site-a/dashboard
    :segment \"dashboard\"
    :view dashboard-view}
   {:id :app.site-a/users
    :segment \"users\" 
    :view users-view}
   {:id :app.site-a/settings
    :segment \"settings\"
    :view settings-view}])

;; Link the routes to make them active
;; make sure that first argument is already existing route
(router/link :app/site-a routes)

(defn site-a-nav []
  [:div.ty-elevated.p-4.mb-4
   [:h3.ty-text++.mb-2 \"Site A Navigation\"]
   [:div.flex.gap-2
    [:ty-button {:variant (if (router/rendered? :app.site-a/dashboard) \"primary\" \"outline\")
                 :size \"sm\"
                 :on {:click #(router/navigate! :app.site-a/dashboard)}}
     \"Dashboard\"]
    [:ty-button {:variant (if (router/rendered? :app.site-a/users) \"primary\" \"outline\")
                 :size \"sm\" 
                 :on {:click #(router/navigate! :app.site-a/users)}}
     \"Users\"]
    [:ty-button {:variant (if (router/rendered? :app.site-a/settings) \"primary\" \"outline\")
                 :size \"sm\"
                 :on {:click #(router/navigate! :app.site-a/settings)}}
     \"Settings\"]]])

(defn view []
  [:div
   [site-a-nav]
   (map (fn [{:keys [id view]}] 
          (when (router/rendered? id true) 
            (view))) 
        routes)])"
                  "clojure")

     ;; Site B namespace  
                (example-section
                  "Site B Namespace (my-app.site-b)"
                  [:div.ty-bg-warning-.border.ty-border-warning.rounded.p-3.mb-4
                   [:p.ty-text-warning.text-sm "Site B has its own independent route structure"]]
                  "(ns my-app.site-b 
  (:require [ty.router :as router]))

;; Different route structure for Site B
(def routes
  [{:id :app.site-b/products
    :segment \"products\"
    :view products-view}
   {:id :app.site-b/orders
    :segment \"orders\"
    :view orders-view}])

;; Link the routes to make them active
;; make sure that first argument is already existing route
(router/link :app/site-b routes)

(defn site-b-nav []
  [:div.ty-elevated.p-4.mb-4
   [:h3.ty-text++.mb-2 \"Site B Navigation\"]  
   [:div.flex.gap-2
    [:ty-button {:variant (if (router/rendered? :app.site-b/products) \"primary\" \"outline\")
                 :size \"sm\"
                 :on {:click #(router/navigate! :app.site-b/products)}}
     \"Products\"]
    [:ty-button {:variant (if (router/rendered? :app.site-b/orders) \"primary\" \"outline\")
                 :size \"sm\"
                 :on {:click #(router/navigate! :app.site-b/orders)}}
     \"Orders\"]]])

(defn view []
  [:div
   [site-b-nav]
   (map (fn [{:keys [id view]}]
          (when (router/rendered? id true)
            (view)))
        routes)])"
                  "clojure")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-8 "Key Router Functions"]
                [:div.space-y-4
                 [:div
                  [:h4.font-medium.ty-text.mb-2 [:code.ty-bg-neutral-.px-2.py-1.rounded "router/navigate!"]]
                  [:p.ty-text-.text-sm "Programmatically navigate to a route:"]]

                 (code-block
                   ";; Navigate to specific routes
(router/navigate! :app/home)
(router/navigate! :app.site-a/users)
(router/navigate! :app.site-b/products)

;; Navigate with URL query parameters
(router/navigate! :app.site-a/user {:id 123})"
                   "clojure")

                 [:div
                  [:h4.font-medium.ty-text.mb-2 [:code.ty-bg-neutral-.px-2.py-1.rounded "router/rendered?"]]
                  [:p.ty-text-.text-sm "Check if a route is currently active for conditional rendering:"]]

                 (code-block
                   ";; Check exact route
(router/rendered? :app/site-a true)

;; Check if :app/site-a is on URL path
(router/rendered? :app/site-a)

;; Check with inheritance (default behavior)
(router/rendered? :app.site-a/users)

;; Use in conditional rendering
[:ty-button {:variant (if (router/rendered? :app/home) \"primary\" \"secondary\")
             :on {:click #(router/navigate! :app/home)}}
 \"Home\"]"
                   "clojure")]]))

;; =============================================================================
;; INTERNATIONALIZATION
;; =============================================================================

(defn i18n-section
  "Complete i18n section for translations"
  []
  (doc-section "Internationalization (i18n)"
               [:div.p-4.ty-content.rounded-lg
                [:p.ty-text-.mb-4
                 "Ty provides a built-in i18n system using qualified keywords, similar to toddler. Translations are stored in memory and dynamically bound to the current locale."]

                [:h3.text-lg.font-semibold.ty-text.mb-3 "Basic Translation Setup"]
                [:p.ty-text-.mb-4
                 "Add translations using qualified keywords where the namespace is the locale:"]

                (example-section
                  "Adding Translations"
                  [:div.ty-bg-info-.border.ty-border-info.rounded.p-3.mb-4
                   [:p.ty-text.text-sm "Use qualified keywords for locale-specific translations"]]
                  "(ns my-app.core
  (:require [replicant.dom :as d]
            [replicant.core :as r]
            [ty.components]
            [ty.i18n :as i18n]
            [ty.i18n.keyword :as i18n-kw]))

;; Add translations using qualified keywords
(i18n-kw/add-translations
  {:save/default \"Save\"
   :save/hr \"Spremi\"
   :save/de \"Speichern\"
   :cancel/default \"Cancel\"
   :cancel/hr \"Odustani\"
   :cancel/de \"Abbrechen\"
   :hello/default \"Hello\"
   :hello/hr \"Bok\"
   :hello/de \"Hallo\"})

;; Or use Clojure's namespaced map syntax
(i18n-kw/add-translations
  (merge
    #:save {:default \"Save\"
            :hr \"Spremi\" 
            :de \"Speichern\"}
    #:cancel {:default \"Cancel\"
              :hr \"Odustani\"
              :de \"Abbrechen\"}))"
                  "clojure")

                (example-section
                  "Using Translations in Replicant Views"
                  [:div.ty-bg-success-.border.ty-border-success.rounded.p-3.mb-4
                   [:p.ty-text-success.text-sm "Bind locale dynamically and translate keywords"]]
                  "(defonce app-state (r/atom {:current-locale :hr}))

(defn welcome-view [state]
  (let [locale (:current-locale @state)]
    (binding [i18n/*locale* locale]  ; Bind current locale
      [:div.ty-elevated.p-6.rounded-lg
       ;; Simple translation - uses current locale
       [:h1.ty-text++.text-2xl.mb-4 
        (i18n/t :hello)]  ; Looks up :hello/hr -> \"Bok\"
       
       ;; Direct qualified keyword translation
       [:p.ty-text.mb-4
        (i18n/t :save/de)]  ; Always returns \"Speichern\"
       
       ;; Buttons with translations
       [:div.flex.gap-2
        [:ty-button {:variant \"primary\"
                     :on {:click #(save-data!)}}
         (i18n/t :save)]  ; Uses current locale
        [:ty-button {:variant \"secondary\" 
                     :on {:click #(cancel-action!)}}
         (i18n/t :cancel)]]])))  ; Uses current locale

(defn language-switcher [state]
  [:div.flex.items-center.gap-4
   [:span.font-medium \"Language:\"]
   [:div.flex.gap-2
    [:ty-button {:variant (if (= (:current-locale @state) :default) \"primary\" \"outline\")
                 :size \"sm\"
                 :on {:click #(swap! state assoc :current-locale :default)}}
     \"English\"]
    [:ty-button {:variant (if (= (:current-locale @state) :hr) \"primary\" \"outline\")
                 :size \"sm\"
                 :on {:click #(swap! state assoc :current-locale :hr)}}
     \"Hrvatski\"]
    [:ty-button {:variant (if (= (:current-locale @state) :de) \"primary\" \"outline\")
                 :size \"sm\"
                 :on {:click #(swap! state assoc :current-locale :de)}}
     \"Deutsch\"]]])"
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

;; Falls back to :default if locale not found
(binding [i18n/*locale* :fr]  ; French not loaded
  (i18n/t :save))  ; Returns :save/default value

;; Falls back to keyword name if no translation found
(i18n/t :missing-key)  ; Returns \"missing-key\""
                   "clojure")

                 [:div
                  [:h4.font-medium.ty-text.mb-2 [:code.ty-bg-neutral-.px-2.py-1.rounded "i18n-kw/add-translations"]]
                  [:p.ty-text-.text-sm "Add translations to in-memory cache:"]]

                 (code-block
                   ";; Using qualified keywords directly
(i18n-kw/add-translations
  {:button/default \"Button\"
   :button/hr \"Gumb\"
   :form/default \"Form\"
   :form/hr \"Obrazac\"})

;; Using namespaced map syntax (cleaner)
(i18n-kw/add-translations
  #:error {:default \"Error\"
           :hr \"Greška\"
           :de \"Fehler\"})"
                   "clojure")]]))

;; =============================================================================
;; FORMATTING
;; =============================================================================

(defn formatting-section
  "Complete formatting section for numbers, currencies, and dates in Replicant"
  []
  (doc-section "Number/Currency/Date Formatting"
               [:div.p-4.ty-content.rounded-lg
                [:p.ty-text-.mb-4
                 "Ty's i18n system provides automatic formatting for numbers, currencies, and dates using the native browser "
                 [:code.ty-bg-neutral-.px-1.rounded "Intl"]
                 " API. The magic happens through the "
                 [:code.ty-bg-neutral-.px-1.rounded "i18n/t"]
                 " function which uses the Translator protocol to automatically format different data types."]

                [:h3.text-lg.font-semibold.ty-text.mb-3 "The Magic of i18n/t"]
                [:p.ty-text-.mb-4
                 "Simply pass numbers, dates, or strings to "
                 [:code.ty-bg-neutral-.px-1.rounded "i18n/t"]
                 " and get locale-appropriate formatting automatically:"]

                (example-section
                  "Basic Number Formatting"
                  [:div.ty-bg-info-.border.ty-border-info.rounded.p-3.mb-4
                   [:p.ty-text.text-sm "Numbers are formatted using the current locale automatically"]]
                  "(defonce app-state (r/atom {:locale :hr}))

(defn price-display [state]
  (let [price 1234.56
        locale (:locale @state)]
    (binding [i18n/*locale* locale]
      [:div.space-y-2
       ;; Basic number - uses current locale number format
       [:div \"Price: \" (i18n/t price)]
       
       ;; Same number in different locales
       [:div \"English: \" (i18n/t price :en)]    ; 1,234.56
       [:div \"German: \" (i18n/t price :de)]     ; 1.234,56  
       [:div \"Croatian: \" (i18n/t price :hr)]   ; 1.234,56
       ])))

;; Results:
;; :en locale -> 1,234.56
;; :de locale -> 1.234,56 
;; :hr locale -> 1.234,56"
                  "clojure")

                (example-section
                  "Currency Formatting"
                  [:div.ty-bg-success-.border.ty-border-success.rounded.p-3.mb-4
                   [:p.ty-text-success.text-sm "Pass currency code as second argument for automatic currency formatting"]]
                  "(defn currency-examples [state]
  (let [amount 1999.99
        locale (:locale @state)]
    (binding [i18n/*locale* locale]
      [:div.space-y-2
       ;; Currency formatting - string as second argument = currency code
       [:div \"USD: \" (i18n/t amount \"USD\")]    ; $1,999.99
       [:div \"EUR: \" (i18n/t amount \"EUR\")]    ; €1,999.99
       [:div \"HRK: \" (i18n/t amount \"HRK\")]    ; 1.999,99 kn
       [:div \"JPY: \" (i18n/t amount \"JPY\")]    ; ¥2,000
       
       ;; Same amount, different locales
       [:div \"USD in German: \" (i18n/t amount \"USD\" :de)]  ; 1.999,99 $
       ])))

;; The magic: i18n/t detects that second argument is a currency code
;; and automatically formats using Intl.NumberFormat currency style"
                  "clojure")

                (example-section
                  "Date & Time Formatting"
                  [:div.ty-bg-warning-.border.ty-border-warning.rounded.p-3.mb-4
                   [:p.ty-text-warning.text-sm "Pass Date objects and style strings for automatic date formatting"]]
                  "(defn event-calendar [state]
  (let [event-date (js/Date. 2024 2 15 14 30)  ; March 15, 2:30 PM
        locale (:locale @state)]
    (binding [i18n/*locale* locale]
      [:div.space-y-2
       ;; Default date formatting (medium style)
       [:div \"Event: \" (i18n/t event-date)]
       
       ;; Built-in date styles - pass style as second argument
       [:div \"Short: \" (i18n/t event-date \"short\")]     ; 3/15/24
       [:div \"Medium: \" (i18n/t event-date \"medium\")]   ; Mar 15, 2024
       [:div \"Long: \" (i18n/t event-date \"long\")]       ; March 15, 2024
       [:div \"Full: \" (i18n/t event-date \"full\")]       ; Friday, March 15, 2024
       [:div \"Time: \" (i18n/t event-date \"time\")]       ; 2:30 PM
       [:div \"DateTime: \" (i18n/t event-date \"datetime\")] ; Mar 15, 2024 2:30 PM
       ])))

;; Results vary by locale:
;; :en -> Mar 15, 2024 | 3/15/24 | Friday, March 15, 2024  
;; :de -> 15.03.2024 | 15.03.24 | Freitag, 15. März 2024
;; :hr -> 15. 03. 2024. | 15. 03. 2024. | petak, 15. ožujka 2024."
                  "clojure")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-8 "Advanced Formatting Options"]
                [:p.ty-text-.mb-4
                 "For complete control, pass a locale and an options map as the third argument using native Intl API options:"]

                (example-section
                  "Custom Number Formatting"
                  [:div.ty-bg-neutral-.border.ty-border.rounded.p-3.mb-4
                   [:p.ty-text-.text-sm "Use Intl.NumberFormat options for complete control"]]
                  "(defn advanced-numbers [state]
  (let [locale (:locale @state)]
    [:div.space-y-2
     ;; Percentage formatting
     [:div \"Tax Rate: \" (i18n/t 0.195 locale {:style \"percent\"})]         ; 19.5%
     
     ;; Compact notation (K, M, B)
     [:div \"Revenue: \" (i18n/t 1234567 locale {:notation \"compact\"})]     ; 1.2M
     
     ;; Fixed decimal places
     [:div \"Price: \" (i18n/t 99.5 locale {:minimumFractionDigits 2})]      ; 99.50
     
     ;; Scientific notation  
     [:div \"Large: \" (i18n/t 123456789 locale {:notation \"scientific\"})] ; 1.235E8
     
     ;; No grouping separators
     [:div \"Serial: \" (i18n/t 1234567 locale {:useGrouping false})]        ; 1234567
     ]))

;; Using the number formatting functions directly:
(require '[ty.i18n.number :as num])

(defn direct-number-formatting []
  [:div.space-y-2
   [:div \"Formatted: \" (num/format-number 1234.56 :hr)]
   [:div \"Currency: \" (num/format-currency 99.99 \"EUR\" :de)] 
   [:div \"Percent: \" (num/format-percent 0.42 :en)]
   [:div \"Compact: \" (num/format-compact 1500000 :fr)]
   ])"
                  "clojure")

                (example-section
                  "Custom Date Formatting"
                  [:div.ty-bg-neutral-.border.ty-border.rounded.p-3.mb-4
                   [:p.ty-text-.text-sm "Use Intl.DateTimeFormat options for precise date control"]]
                  "(defn custom-dates [state]
  (let [date (js/Date. 2024 2 15 14 30 45)  ; March 15, 2:30:45 PM
        locale (:locale @state)]
    [:div.space-y-2
     ;; Custom date parts
     [:div (i18n/t date locale {:year \"numeric\" 
                                :month \"long\" 
                                :day \"numeric\"})]       ; March 15, 2024
     
     ;; Weekday with time
     [:div (i18n/t date locale {:weekday \"long\" 
                                :hour \"2-digit\" 
                                :minute \"2-digit\"})]    ; Friday 14:30
                                
     ;; Month and year only
     [:div (i18n/t date locale {:year \"numeric\" 
                                :month \"short\"})]       ; Mar 2024
                                
     ;; With timezone
     [:div (i18n/t date locale {:dateStyle \"short\" 
                                :timeStyle \"short\" 
                                :timeZone \"Europe/Zagreb\"})]
     ]))

;; Using time formatting functions directly:
(require '[ty.i18n.time :as time])

(defn direct-time-formatting []
  (let [date (js/Date.)]
    [:div.space-y-2
     [:div \"Short: \" (time/format-date-short date :en)]
     [:div \"Medium: \" (time/format-date-medium date :hr)]  
     [:div \"Long: \" (time/format-date-long date :de)]
     [:div \"Full: \" (time/format-date-full date :fr)]
     [:div \"Time: \" (time/format-time date :ja)]
     [:div \"DateTime: \" (time/format-datetime date :es)]
     
     ;; Relative time
     [:div \"Yesterday: \" (time/format-relative -1 \"day\" :en)]      ; yesterday
     [:div \"In 2 hours: \" (time/format-relative 2 \"hour\" :hr)]    ; za 2 sata
     ]))"
                  "clojure")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-8 "Locale Information"]
                [:p.ty-text-.mb-4 "Get localized month and weekday names for building calendars and date pickers:"]

                (example-section
                  "Locale-Specific Names"
                  [:div.ty-bg-info-.border.ty-border-info.rounded.p-3.mb-4
                   [:p.ty-text.text-sm "Get month and weekday names in any supported locale"]]
                  "(require '[ty.i18n :as i18n])

(defn locale-names [state]
  (let [locale (:locale @state)]
    [:div.space-y-4
     ;; Month names
     [:div
      [:h4.font-medium.ty-text.mb-2 \"Months in \" (name locale)]
      [:div.flex.flex-wrap.gap-2
       (for [month (i18n/locale locale :months)]
         [:span.px-2.py-1.ty-elevated.rounded.text-sm {:key month} month])]]
     
     ;; Short month names  
     [:div
      [:h4.font-medium.ty-text.mb-2 \"Short months\"]
      [:div.flex.flex-wrap.gap-2
       (for [month (i18n/locale locale :months/short)]
         [:span.px-2.py-1.ty-content.rounded.text-sm {:key month} month])]]
         
     ;; Weekday names
     [:div
      [:h4.font-medium.ty-text.mb-2 \"Weekdays\"] 
      [:div.flex.flex-wrap.gap-2
       (for [day (i18n/locale locale :weekdays)]
         [:span.px-2.py-1.ty-elevated.rounded.text-sm {:key day} day])]]
     
     ;; Short weekdays
     [:div
      [:h4.font-medium.ty-text.mb-2 \"Short weekdays\"]
      [:div.flex.flex-wrap.gap-2
       (for [day (i18n/locale locale :weekdays/short)]
         [:span.px-2.py-1.ty-content.rounded.text-sm {:key day} day])]]
     ]))

;; Results by locale:
;; :en -> January, February... | Sunday, Monday...  
;; :hr -> siječanj, veljača... | nedjelja, ponedjeljak...
;; :de -> Januar, Februar... | Sonntag, Montag...
;; :fr -> janvier, février... | dimanche, lundi..."
                  "clojure")

                [:h3.text-lg.font-semibold.ty-text.mb-3.mt-8 "Key Takeaways"]
                [:div.ty-bg-neutral-.rounded.p-4
                 [:ul.space-y-2.ty-text-.text-sm
                  [:li "• " [:code.ty-bg-neutral.px-1.rounded "i18n/t"] " automatically formats numbers, currencies, and dates"]
                  [:li "• Pass currency codes as strings: " [:code.ty-bg-neutral.px-1.rounded "(i18n/t 99.99 \"USD\")"]]
                  [:li "• Pass date styles as strings: " [:code.ty-bg-neutral.px-1.rounded "(i18n/t date \"full\")"]]
                  [:li "• Use options maps for custom formatting: " [:code.ty-bg-neutral.px-1.rounded "(i18n/t num locale {:style \"percent\"})"]]
                  [:li "• Get locale-specific names: " [:code.ty-bg-neutral.px-1.rounded "(i18n/locale :hr :months)"]]
                  [:li "• All formatting uses native " [:code.ty-bg-neutral.px-1.rounded "Intl"] " API for accuracy"]
                  [:li "• Bind " [:code.ty-bg-neutral.px-1.rounded "i18n/*locale*"] " to change formatting context dynamically"]]]]))

;; =============================================================================
;; MAIN VIEW
;; =============================================================================

(defn view
  "Main view for Replicant getting started documentation"
  []
  [:div.max-w-4xl.mx-auto.p-6
   [:h1.text-4xl.font-bold.ty-text.mb-4 "Getting Started with Replicant"]
   [:p.text-xl.ty-text-.mb-6
    "Learn how to use Ty web components with Replicant for reactive ClojureScript applications."]

   ;; All sections in logical order
   (doc-section "Dependencies"
                [:div
                 [:p.ty-text-.mb-4
                  "Add the required dependencies to your " [:code.ty-bg-neutral-.px-2.py-1.rounded "deps.edn"] " file:"]

                 (code-block
                   "{:deps {dev.gersak/ty {:mvn/version \"LATEST\"}
        dev.gersak/ty-icons {:mvn/version \"LATEST\"}
        no.cjohansen/replicant {:mvn/version \"LATEST\"}}}"
                   "clojure")

                 [:p.ty-text-.mt-4.text-sm
                  "Replace " [:code.ty-bg-neutral-.px-1.rounded "\"LATEST\""]
                  " with the current version numbers from Clojars."]])
   (doc-section "Component Registration"
                [:div
                 [:p.ty-text-.mb-4
                  "In your ClojureScript namespace, require " [:code.ty-bg-neutral-.px-2.py-1.rounded "ty.components"]
                  " to register all Ty web components:"]

                 (code-block
                   "(ns my-app.core
  (:require [replicant.dom :as d]
            [replicant.core :as r]
            [ty.components]))  ; <- This registers all Ty components"
                   "clojure")

                 [:p.ty-text-.mt-4
                  "Once registered, you can use Ty components directly in your Replicant hiccup markup."]])
   (basic-usage-section)
   (routing-section)
   (i18n-section)
   (formatting-section)])
