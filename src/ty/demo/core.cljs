(ns ty.demo.core
  (:require [replicant.dom :as rdom]
            [ty.demo.state :refer [state]]
            [ty.demo.views.buttons :as buttons]
            [ty.demo.views.home :as home]
            [ty.demo.views.icons :as icons]
            [ty.demo.views.popups :as popups]))

(defn toggle-theme! []
  (let [new-theme (if (= (:theme @state) "light") "dark" "light")]
    (swap! state assoc :theme new-theme)
    ;; Persist to localStorage
    (.setItem js/localStorage "ty-theme" new-theme)))

(defn set-page! [page]
  (swap! state assoc :current-page page))

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
  (let [{:keys [current-page]} @state]
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
       (nav-item {:page :popups
                  :label "Popups"
                  :icon "message-square"
                  :active? (= current-page :popups)
                  :on-click #(set-page! :popups)})
       (nav-item {:page :theming
                  :label "Theming"
                  :icon "palette"
                  :active? (= current-page :theming)
                  :on-click #(set-page! :theming)})]]]))

(defn header []
  [:header.bg-white.dark:bg-gray-800.border-b.border-gray-200.dark:border-gray-700.px-6.py-4
   [:div.flex.justify-between.items-center
    [:h2.text-xl.font-semibold.text-gray-900.dark:text-white
     (case (:current-page @state)
       :home "Welcome to Ty"
       :buttons "Button Components"
       :icons "Icon Library"
       :popups "Popup Components"
       :theming "Theming & Customization"
       "Ty Components")]
    [:button.p-2.rounded-md.bg-gray-100.dark:bg-gray-700.hover:bg-gray-200.dark:hover:bg-gray-600.transition-colors
     {:on {:click toggle-theme!}}
     [:ty-icon {:name (if (= (:theme @state) "light") "moon" "sun")
                :size "md"}]]]])

(defn app []
  [:div.h-screen.flex.bg-gray-50.dark:bg-gray-900
   (sidebar)
   [:div.flex-1.flex.flex-col
    (header)
    [:main.flex-1.overflow-auto.p-6
     (case (:current-page @state)
       :home (home/home-view)
       :buttons (buttons/buttons-view)
       :icons (icons/icons-view)
       :popups (popups/popups-view)
       :theming [:div "Theming page - coming soon"]
       [:div "404"])]]])

(defn render-app! []
  (rdom/render (js/document.getElementById "app") (app)))

(defn ^:dev/after-load init []
  ;; Apply initial theme class and attribute
  (let [html (.-documentElement js/document)
        theme (:theme @state)]
    (set! (.-className html) (if (= theme "dark") "dark" ""))
    (set! (.-dataset.theme html) theme))

  ;; Set theme on document root and re-render on changes
  (add-watch state ::render
             (fn [_ _ _ {:keys [theme]}]
               (let [html (.-documentElement js/document)]
                 (set! (.-className html) (if (= theme "dark") "dark" ""))
                 (set! (.-dataset.theme html) theme))
               (render-app!)))

  ;; Initial render
  (render-app!))
