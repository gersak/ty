(ns hello.core
  (:require
    [hello.navigation :as nav]
    [hello.state :as state]
    [hello.views :as views]
    [hello.views-react :as views-react]
    [reagent.core :as r]
    [reagent.dom :as rdom]
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

   ;; Include i18n namespaces
    [ty.i18n :as i18n]
    [ty.i18n.keyword]
    [ty.i18n.number]
    [ty.i18n.string]
    [ty.i18n.time]
   ;; Icon libraries - the proper way!
    [ty.icons :as icons]
    [ty.lucide :as lucide]
    [ty.material.filled :as mat-filled]
    [ty.material.outlined :as mat-outlined]
    ;; Web component shim
    [ty.shim :as wcs]))

;; Register all components directly using their configurations
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

;; Register icons using the proper Ty icon libraries
(defn register-icons! []
  (icons/add! {;; Navigation icons
               "home" mat-filled/home
               "edit" lucide/edit
               "menu" mat-filled/menu
               "x" lucide/x
               "grid" lucide/grid-3x3

               ;; Theme icons
               "sun" lucide/sun
               "moon" lucide/moon

               ;; Button icons
               "save" lucide/save
               "trash" lucide/trash-2
               "download" lucide/download
               "click" lucide/mouse-pointer-click

               ;; Form icons
               "user" lucide/user
               "settings" lucide/settings
               "code" lucide/code-2
               "palette" lucide/palette
               "users" lucide/users
               "bar-chart" lucide/bar-chart-3
               "briefcase" lucide/briefcase
               "alert-circle" lucide/alert-circle
               "refresh-cw" lucide/refresh-cw
               "send" lucide/send
               "loader" lucide/loader-2}))

;; Main layout with responsive sidebar
(defn main-layout []
  (let [show-sidebar? (>= js/window.innerWidth 1024)] ; lg breakpoint
    [:div.h-screen.flex.ty-canvas
     [nav/mobile-menu]
     (when show-sidebar? [nav/sidebar])
     [:div.flex-1.flex.flex-col.min-w-0
      [nav/header]
      [:main.flex-1.overflow-auto.p-6.ty-content
       ;; Route-based content
       (case (:current-route @state/app-state)
         :home [views/home-view]
         :forms [views/forms-view]
         :forms-react [views-react/forms-view]
         :buttons [views/buttons-view]
         :components [views/components-view]
         [views/home-view])]]]))

(defn init []
  ;; Register icons using proper icon libraries
  (register-icons!)

  ;; Apply initial theme
  (let [theme (:theme @state/app-state)
        html (.-documentElement js/document)]
    (set! (.-className html) (if (= theme "dark") "dark" "")))

  ;; Watch for theme changes
  (add-watch state/app-state ::theme
             (fn [_ _ _ new-state]
               (let [html (.-documentElement js/document)]
                 (set! (.-className html) (if (= (:theme new-state) "dark") "dark" "")))))

  ;; Simple hash-based routing
  (defn handle-route-change []
    (let [hash (.-hash js/location)
          route (if (empty? hash)
                  :home
                  (keyword (.substring hash 1)))]
      (when (contains? state/routes route)
        (swap! state/app-state assoc :current-route route))))

  ;; Listen for hash changes
  (.addEventListener js/window "hashchange" handle-route-change)

  ;; Handle initial route
  (handle-route-change)

  ;; Render app
  (rdom/render [main-layout] (.getElementById js/document "app")))

(defn ^:after-load after-load []
  (rdom/render [main-layout] (.getElementById js/document "app")))
