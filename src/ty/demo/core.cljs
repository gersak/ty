(ns ty.demo.core
  (:require [replicant.dom :as rdom]
            [ty.demo.views.buttons :as buttons]
            [ty.demo.views.home :as home]
            [ty.demo.views.icons :as icons]))

(defonce app-state (atom {:theme "light"
                          :current-page :home}))

(defn toggle-theme! []
  (swap! app-state update :theme #(if (= % "light") "dark" "light")))

(defn set-page! [page]
  (swap! app-state assoc :current-page page))

(defn nav-item [{:keys [page label icon active? on-click]}]
  [:button.flex.items-center.gap-3.w-full.px-4.py-2.text-left.rounded-md.transition-colors
   {:class (if active?
             [:bg-blue-600 :text-white]
             [:text-gray-700 "dark:text-gray-300" "hover:bg-gray-100" "dark:hover:bg-gray-700"])
    :on {:click on-click}}
   (when icon
     [:ty-icon {:name icon
                :size "sm"}])
   [:span label]])

(defn sidebar []
  (let [{:keys [current-page]} @app-state]
    [:aside.w-64.bg-white.dark:bg-gray-800.border-r.border-gray-200.dark:border-gray-700.h-full
     [:div.p-6
      [:h1.text-2xl.font-bold.text-gray-900.dark:text-white.mb-2 "Ty Components"]
      [:p.text-sm.text-gray-600.dark:text-gray-400 "Web Components Library"]]

     [:nav.px-4.pb-6
      [:div.space-y-1
       (nav-item {:page :home
                  :label "Overview"
                  :icon "home"
                  :active? (= current-page :home)
                  :on-click #(set-page! :home)})
       (nav-item {:page :buttons
                  :label "Buttons"
                  :icon "click"
                  :active? (= current-page :buttons)
                  :on-click #(set-page! :buttons)})
       (nav-item {:page :icons
                  :label "Icons"
                  :icon "image"
                  :active? (= current-page :icons)
                  :on-click #(set-page! :icons)})
       (nav-item {:page :theming
                  :label "Theming"
                  :icon "palette"
                  :active? (= current-page :theming)
                  :on-click #(set-page! :theming)})]]]))

(defn header []
  [:header.bg-white.dark:bg-gray-800.border-b.border-gray-200.dark:border-gray-700.px-6.py-4
   [:div.flex.justify-between.items-center
    [:h2.text-xl.font-semibold.text-gray-900.dark:text-white
     (case (:current-page @app-state)
       :home "Welcome to Ty"
       :buttons "Button Components"
       :icons "Icon Library"
       :theming "Theming & Customization"
       "Ty Components")]
    [:button.p-2.rounded-md.bg-gray-100.dark:bg-gray-700.hover:bg-gray-200.dark:hover:bg-gray-600.transition-colors
     {:on {:click toggle-theme!}}
     [:ty-icon {:name (if (= (:theme @app-state) "light") "moon" "sun")
                :size "md"}]]]])

(defn app []
  [:div.h-screen.flex.bg-gray-50.dark:bg-gray-900
   (sidebar)
   [:div.flex-1.flex.flex-col
    (header)
    [:main.flex-1.overflow-auto.p-6
     (case (:current-page @app-state)
       :home (home/home-view)
       :buttons (buttons/buttons-view)
       :icons (icons/icons-view)
       :theming [:div "Theming page - coming soon"]
       [:div "404"])]]])

(defn render-app! []
  (rdom/render (js/document.getElementById "app") (app)))

(defn init []
  ;; Set theme on document root and re-render on changes
  (add-watch app-state :theme-watcher
             (fn [_ _ _ {:keys [theme]}]
               (set! (.-className (.-documentElement js/document))
                     (if (= theme "dark") "dark" ""))
               (render-app!)))

  ;; Initial render
  (render-app!))
