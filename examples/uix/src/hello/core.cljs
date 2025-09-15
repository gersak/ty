(ns hello.core
  (:require
   ;; Clean React wrappers
    ["@gersak/ty-react" :as ty]
   ;; Form view
    [hello.views.forms :as forms]

    [ty.components.button :as button]
    [ty.components.calendar :as calendar]

    [ty.components.calendar-month :as calendar-month]
    [ty.components.calendar-navigation :as calendar-navigation]
   ;; All component configurations
    [ty.components.core]
    [ty.components.date-picker :as date-picker]
    [ty.components.dropdown :as dropdown]
    [ty.components.icon :as icon]
    [ty.components.input :as input]
    [ty.components.modal :as modal]
    [ty.components.multiselect :as multiselect]
    [ty.components.option :as option]
    [ty.components.popup :as popup]
    [ty.components.tag :as tag]
    [ty.components.tooltip :as tooltip]
   ;; ClojureScript ty utilities
    [ty.icons :as icons]
    [ty.lucide :as lucide]
   ;; TY Router
    [ty.router :as router]
    [ty.shim :as wcs]
   ;; UIx for React-like development
    [uix.core :as uix :refer [defui $]]

    [uix.dom]))

;; Define routes
(router/link ::router/root
             [{:id ::home
               :segment ""
               :name "Getting Started"
               :landing 10}
              {:id ::buttons
               :segment "buttons"
               :name "Buttons & Icons"}
              {:id ::forms
               :segment "forms"
               :name "Forms & Inputs"}
              {:id ::layout
               :segment "layout"
               :name "Layout & Containers"}
              {:id ::modals
               :segment "modals"
               :name "Modals & Overlays"}])

(wcs/define! "ty-button" button/configuration)
(wcs/define! "ty-option" option/configuration)
(wcs/define! "ty-tag" tag/configuration)
(wcs/define! "ty-tooltip" tooltip/configuration)
(wcs/define! "ty-popup" popup/configuration)
(wcs/define! "ty-icon" icon/configuration)
(wcs/define! "ty-modal" modal/configuration)
(wcs/define! "ty-input" input/configuration)
(wcs/define! "ty-calendar" calendar/configuration)
(wcs/define! "ty-calendar-month" calendar-month/configuration)
(wcs/define! "ty-calendar-navigation" calendar-navigation/configuration)
(wcs/define! "ty-date-picker" date-picker/configuration)
(wcs/define! "ty-dropdown" dropdown/configuration)
(wcs/define! "ty-multiselect" multiselect/configuration)

(defn register-icons! []
  "Register icons used in the application"
  (icons/add! {;; Form icons
               "user" lucide/user
               "mail" lucide/mail
               "phone" lucide/phone
               "message-circle" lucide/message-circle
               "send" lucide/send
               "check" lucide/check
               "x" lucide/x
               "alert-circle" lucide/alert-circle

               ;; Navigation icons  
               "home" lucide/home
               "forms" lucide/file-text
               "settings" lucide/settings

               ;; Layout and UI icons
               "menu" lucide/menu
               "sun" lucide/sun
               "moon" lucide/moon
               "edit" lucide/edit
               "square" lucide/square
               "grid" lucide/grid-3x3
               "layers" lucide/layers
               "play" lucide/play
               "code" lucide/code-2
               "zap" lucide/zap
               "palette" lucide/palette}))

(defui nav-link [{:keys [route-id icon label]}]
  (let [is-active (router/rendered? route-id true)]
    ($ :button.flex.items-center.gap-3.px-4.py-3.rounded-lg.text-left.transition-all.duration-150.mx-4.grow
       {:class (if is-active
                 "ty-bg-primary- ty-text-primary++ font-medium"
                 "ty-text hover:ty-bg-neutral- hover:ty-text+")
        :on-click #(router/navigate! route-id)}
       ($ ty/Icon {:name icon
                   :class "w-5 h-5 flex-shrink-0"})
       ($ :span label))))

(defui feature-card [{:keys [icon title description]}]
  ($ :div {:class "ty-elevated rounded-lg p-6 hover:shadow-lg transition-shadow"}
     ($ :div {:class "ty-bg-primary- rounded-lg p-3 w-12 h-12 flex items-center justify-center mb-4"}
        ($ ty/Icon {:name icon
                    :class "w-6 h-6 ty-text-primary++"}))
     ($ :h3 {:class "ty-text++ font-semibold mb-2"} title)
     ($ :p {:class "ty-text- text-sm"} description)))

(defui stat-item [{:keys [label value]}]
  ($ :div {:class "text-center"}
     ($ :div {:class "ty-text++ text-2xl font-bold"} value)
     ($ :div {:class "ty-text- text-sm"} label)))

(defui home-view []
  ($ :div {:class "space-y-8"}
     ;; Hero section
     ($ :div {:class "text-center"}
        ($ :h1 {:class "ty-text++ text-4xl font-bold mb-4"}
           "UIx + ty-react Hybrid")
        ($ :p {:class "ty-text text-lg max-w-2xl mx-auto mb-8"}
           "A modern ClojureScript application showcasing the perfect integration of UIx hooks, ty-react components, and Tailwind CSS following the TY design system.")
        ($ :div {:class "flex flex-wrap justify-center gap-4"}
           ($ ty/Button {:variant "primary"
                         :class "flex items-center gap-2"}
              ($ ty/Icon {:name "play"})
              "Explore Components")
           ($ ty/Button {:flavor "outline"
                         :class "flex items-center gap-2"}
              ($ ty/Icon {:name "code"})
              "View Source")))

     ;; Feature grid
     ($ :div {:class "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"}
        ($ feature-card {:icon "zap"
                         :title "UIx Hooks"
                         :description "Modern React-like development with use-state, use-effect, and functional components."})
        ($ feature-card {:icon "layers"
                         :title "ty-react Components"
                         :description "Clean component wrappers with proper TypeScript integration and event handling."})
        ($ feature-card {:icon "palette"
                         :title "Semantic Design"
                         :description "TY color system with automatic dark/light theme support and semantic classes."}))

     ;; Quick stats
     ($ :div {:class "ty-elevated rounded-lg p-6"}
        ($ :h2 {:class "ty-text++ text-xl font-bold mb-4"}
           "Project Stats")
        ($ :div {:class "grid grid-cols-2 md:grid-cols-4 gap-4"}
           ($ stat-item {:label "Components"
                         :value "12+"})
           ($ stat-item {:label "Icons"
                         :value "25+"})
           ($ stat-item {:label "Views"
                         :value "5"})
           ($ stat-item {:label "Themes"
                         :value "2"})))))

(defui buttons-view []
  ($ :div {:class "space-y-8"}
     ($ :div
        ($ :h1 {:class "ty-text++ text-3xl font-bold mb-2"}
           "Buttons & Icons")
        ($ :p {:class "ty-text- max-w-2xl mb-8"}
           "Interactive examples of button variants, sizes, and icon integration patterns."))

     ;; Button variants
     ($ :div {:class "ty-elevated rounded-lg p-6 mb-8"}
        ($ :h2 {:class "ty-text++ text-xl font-semibold mb-4"}
           "Button Variants")
        ($ :div {:class "flex flex-wrap gap-4"}
           ($ ty/Button {:flavor "primary"} "Primary")
           ($ ty/Button {:flavor "secondary"} "Secondary")
           ($ ty/Button {:flavor "success"
                         :outlined true} "Outline")
           ($ ty/Button {:flavor "ghost"} "Ghost")))

     ;; Icon gallery
     ($ :div {:class "ty-elevated rounded-lg p-6"}
        ($ :h2 {:class "ty-text++ text-xl font-semibold mb-4"}
           "Available Icons")
        ($ :div {:class "grid grid-cols-4 md:grid-cols-8 gap-4"}
           (for [icon ["home" "user" "mail" "phone" "message-circle" "send" "check" "x"
                       "alert-circle" "forms" "settings" "edit" "play" "code" "zap" "layers"]]
             ($ :div {:key icon
                      :class "flex flex-col items-center gap-2 p-3 rounded-lg hover:ty-bg-neutral- transition-colors"}
                ($ ty/Icon {:name icon
                            :class "w-6 h-6"})
                ($ :span {:class "text-xs ty-text-"} icon)))))))

(defui layout-view []
  ($ :div {:class "space-y-8"}
     ($ :div
        ($ :h1 {:class "ty-text++ text-3xl font-bold mb-2"}
           "Layout & Containers")
        ($ :p {:class "ty-text- max-w-2xl mb-8"}
           "Responsive layout patterns using TY surface classes and Tailwind utilities."))

     ;; Surface examples
     ($ :div {:class "space-y-6"}
        ($ :h2 {:class "ty-text++ text-xl font-semibold mb-4"}
           "Surface Classes")

        ($ :div {:class "ty-canvas p-4 rounded-lg border"}
           ($ :p {:class "ty-text+ mb-2"} "ty-canvas - App background")
           ($ :div {:class "ty-content p-4 rounded-lg border"}
              ($ :p {:class "ty-text+ mb-2"} "ty-content - Main content area")
              ($ :div {:class "ty-elevated p-4 rounded-lg"}
                 ($ :p {:class "ty-text+ mb-2"} "ty-elevated - Cards and panels")
                 ($ :div {:class "ty-floating p-4 rounded-lg"}
                    ($ :p {:class "ty-text+"} "ty-floating - Modals and dropdowns"))))))))

(defui modals-view []
  ($ :div {:class "space-y-8"}
     ($ :div
        ($ :h1 {:class "ty-text++ text-3xl font-bold mb-2"}
           "Modals & Overlays")
        ($ :p {:class "ty-text- max-w-2xl mb-8"}
           "Modal dialogs, tooltips, and overlay components."))

     ($ :div {:class "ty-elevated rounded-lg p-6"}
        ($ :h2 {:class "ty-text++ text-xl font-semibold mb-4"}
           "Coming Soon")
        ($ :p {:class "ty-text"}
           "Modal and overlay examples will be added in the next phase."))))

;; Router integration hook
(defn use-router []
  "Hook to subscribe to router state changes and trigger re-renders"
  (let [[router-state set-router-state] (uix/use-state @router/*router*)]

    ;; Subscribe to router changes
    (uix/use-effect
      (fn []
        (let [watch-key (gensym "uix-router-watch")]
         ;; Add watch to router atom
          (add-watch router/*router* watch-key
                     (fn [_key _ref _old-state new-state]
                       (set-router-state new-state)))

         ;; Cleanup function
          (fn []
            (remove-watch router/*router* watch-key))))
      []) ; Empty dependency array - only run once

    router-state))

(defui app []
  (let [;; Remove state-based routing - use ty router instead
        [sidebar-open set-sidebar-open] (uix/use-state false)
        [theme set-theme] (uix/use-state "light")

        ;; Subscribe to router state changes for re-rendering
        router-state (use-router)]

    ;; Apply theme to html element
    (uix/use-effect
      (fn []
        (let [html-el (.-documentElement js/document)]
          (if (= theme "dark")
            (.add (.-classList html-el) "dark")
            (.remove (.-classList html-el) "dark"))))
      [theme])

    ;; Fixed layout structure - using flexbox instead of CSS Grid
    ($ :div {:class "min-h-screen flex flex-col ty-canvas"}

       ;; Header - fixed at top
       ($ :header {:class "flex items-center justify-between px-6 h-16 ty-elevated border-b ty-border z-50 flex-shrink-0"}
          ($ :div {:class "flex items-center gap-4"}
             ;; Mobile menu button
             ($ ty/Button
                {:variant "ghost"
                 :size "sm"
                 :on-click #(set-sidebar-open true)
                 :class "lg:hidden"}
                ($ ty/Icon {:name "menu"}))

             ;; Logo/Title with router-based header
             ($ :h1 {:class "ty-text++ text-xl font-bold"}
                (cond
                  (router/rendered? ::home true) "Getting Started"
                  (router/rendered? ::buttons true) "Buttons & Icons"
                  (router/rendered? ::forms true) "Forms & Inputs"
                  (router/rendered? ::layout true) "Layout & Containers"
                  (router/rendered? ::modals true) "Modals & Overlays"
                  :else "UIx + ty-react")))

          ;; Header actions
          ($ :div {:class "flex items-center gap-3"}
             ;; Theme toggle
             ($ ty/Button
                {:variant "ghost"
                 :size "sm"
                 :on-click #(set-theme (if (= theme "light") "dark" "light"))
                 :class "flex items-center gap-2"}
                ($ ty/Icon {:name (if (= theme "light") "sun" "moon")})
                (when (>= (.-innerWidth js/window) 768)
                  (if (= theme "light") "Light" "Dark")))

             ;; Status indicator
             ($ :div {:class "ty-bg-success- ty-text-success++ px-2 py-1 rounded text-xs font-medium flex items-center gap-1"}
                ($ ty/Icon {:name "check"
                            :class "w-3 h-3"})
                "Ready")))

       ;; Content area with sidebar and main
       ($ :div {:class "flex flex-1 min-h-0"}
          ;; Mobile overlay
          (when sidebar-open
            ($ :div {:class "lg:hidden fixed inset-0 bg-black bg-opacity-50 z-40"
                     :on-click #(set-sidebar-open false)}))

          ;; Sidebar
          ($ :aside {:class (str "w-80 lg:w-64 ty-content border-r ty-border z-30 flex-shrink-0 "
                                 "lg:relative lg:translate-x-0 "
                                 "fixed bottom-0 left-0 "
                                 "transition-transform duration-300 overflow-y-auto "
                                 (if sidebar-open "translate-x-0" "-translate-x-full lg:translate-x-0"))}
             ($ :nav {:class "py-6"}
                ;; Navigation sections
                ($ :div {:class "mb-6"}
                   ($ :div {:class "px-4 py-2 text-xs font-semibold uppercase tracking-wider ty-text- mb-3"}
                      "Overview")
                   ($ :div.space-y-1.flex.flex-col
                      ($ nav-link {:route-id ::home
                                   :icon "home"
                                   :label "Getting Started"})
                      ($ nav-link {:route-id ::buttons
                                   :icon "square"
                                   :label "Buttons & Icons"})
                      ($ nav-link {:route-id ::forms
                                   :icon "edit"
                                   :label "Forms & Inputs"})))

                ($ :div {:class "mb-6"}
                   ($ :div {:class "px-4 py-2 text-xs font-semibold uppercase tracking-wider ty-text- mb-3"}
                      "Components")
                   ($ :div {:class "space-y-1"}
                      ($ nav-link {:route-id ::layout
                                   :icon "grid"
                                   :label "Layout & Containers"})
                      ($ nav-link {:route-id ::modals
                                   :icon "layers"
                                   :label "Modals & Overlays"})))))

          ;; Main content area
          ($ :main {:class "flex-1 overflow-auto p-6 min-w-0"}
             ($ :div {:class "max-w-4xl mx-auto"}
                (cond
                  (router/rendered? ::home true) ($ home-view)
                  (router/rendered? ::buttons true) ($ buttons-view)
                  (router/rendered? ::forms true) ($ forms/view)
                  (router/rendered? ::layout true) ($ layout-view)
                  (router/rendered? ::modals true) ($ modals-view)
                  :else ($ :div {:class "text-center py-12"}
                           ($ :h1 {:class "ty-text++ text-2xl font-bold mb-2"}
                              "404 - Page Not Found")
                           ($ :p {:class "ty-text- mb-4"}
                              "The page you're looking for doesn't exist.")
                           ($ ty/Button {:on-click #(router/navigate! ::home)}
                              "Go Home"))))))

       ;; Close sidebar on navigation (mobile)
       (uix/use-effect
         (fn []
           (when sidebar-open
             (set-sidebar-open false)))
         [sidebar-open (:current router-state)]))))

(defn ^:after-load init []
  "Initialize the application"
  (register-icons!)

  ;; Initialize ty router with base path and landing URL
  (router/init! "" "/")

  ;; Watch for router changes to trigger re-renders  
  ;; This ensures the app re-renders when the route changes via browser back/forward
  (add-watch router/*router* ::uix-render
             (fn [_ _ _ _]
               (uix.dom/render ($ app) (.getElementById js/document "app"))))

  ;; Initial render
  (uix.dom/render ($ app) (.getElementById js/document "app")))
