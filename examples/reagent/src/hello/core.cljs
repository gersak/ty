(ns hello.core
  (:require
   ;; React wrappers from @gersak/ty-react
   ["@gersak/ty-react" :as ty]
   [hello.navigation :as nav]
   [hello.state :as state]
   [hello.views :as views]
   [reagent.core :as r]
   [reagent.dom :as rdom]
    ;; Icon imports from ClojureScript ty-icons (dead code eliminated)
   [ty.lucide :as lucide]
   [ty.material.filled :as mat-filled]))

;; Web components are already registered via ty.js loaded in index.html
;; No need for wcs/define! anymore

;; Register icons using window.tyIcons.register approach
;; with ClojureScript icons (dead code eliminated)
(defn register-icons! []
  (if-some [icons js/window.tyIcons]
    (.register icons
               #js {;; Navigation icons
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
                    "loader" lucide/loader-2})
    (js/setTimeout #(register-icons!) 10)))

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
         :buttons [views/buttons-view]
         :components [views/components-view]
         [views/home-view])]]]))

(defn init []
  ;; Register icons using window.tyIcons approach with ClojureScript icons
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