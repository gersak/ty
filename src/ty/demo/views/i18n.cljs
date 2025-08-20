(ns ty.demo.views.i18n
  "Demonstrates the i18n functionality"
  (:require
    [ty.demo.state :refer [state]]
    [ty.i18n :as i18n]
    [ty.i18n.keyword :as i18n-kw]))

(defn translation-example []
  [:div.space-y-2
   [:h3.text-lg.font-semibold "Basic Translation"]
   [:div.grid.grid-cols-2.gap-4
    [:div
     [:code.text-sm.text-gray-600.dark:text-gray-400 "(i18n/t :save)"]
     [:span.ml-2 "→"]
     [:span.ml-2.font-medium (i18n/t :save)]]
    [:div
     [:code.text-sm.text-gray-600.dark:text-gray-400 "(i18n/t :cancel)"]
     [:span.ml-2 "→"]
     [:span.ml-2.font-medium (i18n/t :cancel)]]
    [:div
     [:code.text-sm.text-gray-600.dark:text-gray-400 "(i18n/t :delete)"]
     [:span.ml-2 "→"]
     [:span.ml-2.font-medium (i18n/t :delete)]]]])

(defn locale-switcher []
  [:div.flex.items-center.gap-4
   [:span.font-medium "Current locale:"]
   [:select.px-3.py-2.border.rounded-md
    {:value (name @(atom i18n/*locale*))
     :on {:change (fn [e]
                    (let [locale (keyword (.. e -target -value))]
                      (swap! state assoc :locale locale)))}}
    [:option {:value "en"} "English"]
    [:option {:value "hr"} "Hrvatski"]
    [:option {:value "de"} "Deutsch"]
    [:option {:value "fr"} "Français"]
    [:option {:value "es"} "Español"]]])

(defn load-translations-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Load Translations"]
   [:div.flex.gap-4
    [:button.px-4.py-2.rounded-md.transition-colors.bg-blue-500.hover:bg-blue-600.text-white.dark:bg-blue-600.dark:hover:bg-blue-700
     {:on {:click (fn []
                    (-> (i18n-kw/load-translations!
                          {:format :edn
                           :path "/i18n/common.edn"})
                        (.then #(js/console.log "Loaded EDN translations:" %))
                        (.catch #(js/console.error "Failed to load:" %))))}}
     "Load EDN Translations"]
    [:button.px-4.py-2.rounded-md.transition-colors.bg-green-500.hover:bg-green-600.text-white.dark:bg-green-600.dark:hover:bg-green-700
     {:on {:click (fn []
                    (-> (i18n-kw/load-translations!
                          {:format :json
                           :path "/i18n/en.json"
                           :locale :en})
                        (.then #(js/console.log "Loaded JSON translations:" %))
                        (.catch #(js/console.error "Failed to load:" %))))}}
     "Load JSON (English)"]]
   [:div.text-sm.text-gray-600
    "Check console for loading results"]])

(defn manual-translations-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Manual Translation Management"]
   [:button.px-4.py-2.bg-purple-500.text-white.rounded-md.hover:bg-purple-600
    {:on {:click (fn []
                   ;; Using qualified keywords directly
                   (i18n-kw/add-translations
                     {:hello/default "Hello"
                      :hello/hr "Bok"
                      :hello/de "Hallo"
                      :hello/fr "Bonjour"
                      :hello/es "Hola"
                      :goodbye/default "Goodbye"
                      :goodbye/hr "Doviđenja"
                      :goodbye/de "Auf Wiedersehen"
                      :goodbye/fr "Au revoir"
                      :goodbye/es "Adiós"})
                   (js/console.log "Added manual translations"))}}
    "Add Manual Translations"]
   [:div.grid.grid-cols-2.gap-4.mt-4
    [:div
     [:code.text-sm.text-gray-600.dark:text-gray-400 "(i18n/t :hello)"]
     [:span.ml-2 "→"]
     [:span.ml-2.font-medium (i18n/t :hello)]]
    [:div
     [:code.text-sm.text-gray-600.dark:text-gray-400 "(i18n/t :goodbye)"]
     [:span.ml-2 "→"]
     [:span.ml-2.font-medium (i18n/t :goodbye)]]]])

(defn namespaced-map-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Namespaced Map Syntax"]
   [:button.px-4.py-2.bg-indigo-500.text-white.rounded-md.hover:bg-indigo-600
    {:on {:click (fn []
                   ;; Using Clojure's namespaced map syntax
                   (i18n-kw/add-translations
                     (merge
                       #:button {:default "Button"
                                 :hr "Gumb"
                                 :de "Schaltfläche"}
                       #:form {:default "Form"
                               :hr "Obrazac"
                               :de "Formular"}))
                   (js/console.log "Added namespaced translations"))}}
    "Add with Namespaced Maps"]
   [:div.grid.grid-cols-2.gap-4.mt-4
    [:div
     [:code.text-sm.text-gray-600.dark:text-gray-400 "(i18n/t :button)"]
     [:span.ml-2 "→"]
     [:span.ml-2.font-medium (i18n/t :button)]]
    [:div
     [:code.text-sm.text-gray-600.dark:text-gray-400 "(i18n/t :form)"]
     [:span.ml-2 "→"]
     [:span.ml-2.font-medium (i18n/t :form)]]]])

(defn qualified-keyword-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Direct Qualified Keywords"]
   [:div.grid.grid-cols-2.gap-4
    [:div
     [:code.text-sm.text-gray-600.dark:text-gray-400 "(i18n/t :save/hr)"]
     [:span.ml-2 "→"]
     [:span.ml-2.font-medium (i18n/t :save/hr)]]
    [:div
     [:code.text-sm.text-gray-600.dark:text-gray-400 "(i18n/t :cancel/de)"]
     [:span.ml-2 "→"]
     [:span.ml-2.font-medium (i18n/t :cancel/de)]]
    [:div
     [:code.text-sm.text-gray-600.dark:text-gray-400 "(i18n/t :welcome/fr)"]
     [:span.ml-2 "→"]
     [:span.ml-2.font-medium (i18n/t :welcome/fr)]]
    [:div
     [:code.text-sm.text-gray-600.dark:text-gray-400 "(i18n/t :yes/es)"]
     [:span.ml-2 "→"]
     [:span.ml-2.font-medium (i18n/t :yes/es)]]]])

(defn current-translations-view []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Current Translations in Memory"]
   [:button.px-4.py-2.bg-gray-500.text-white.rounded-md.hover:bg-gray-600
    {:on {:click #(js/console.log "Current translations:" @i18n-kw/translations)}}
    "Log Translations to Console"]
   [:div.text-sm.text-gray-600
    "Open browser console to see the translations atom"]])

(defn i18n-view []
  (let [locale (:locale @state i18n/*locale*)]
    (binding [i18n/*locale* locale]
      [:div.p-8.max-w-6xl.mx-auto.space-y-8.text-gray-600.dark:text-gray-400
       [:div
        [:h1.text-3xl.font-bold.mb-4 "i18n - Internationalization"]
        [:p
         "Translation utilities using qualified keywords, matching toddler's approach."]]

       [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
        (locale-switcher)]

       [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
        (translation-example)]

       [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
        (load-translations-demo)]

       [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
        (manual-translations-demo)]

       [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
        (namespaced-map-demo)]

       [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
        (qualified-keyword-demo)]

       [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
        (current-translations-view)]])))
