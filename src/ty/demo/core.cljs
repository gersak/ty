(ns ty.demo.core
  (:require [replicant.dom :as rdom]
            [ty.context :as context]
            [ty.demo.icons :as demo-icons]
            [ty.demo.state :refer [state]]
            [ty.demo.views.buttons :as buttons]
            [ty.demo.views.home :as home]
            [ty.demo.views.icons :as icons]
            [ty.demo.views.popups :as popups]
            [ty.router :as router]))

(defn setup-routes! []
  "Setup demo routes"
  (router/link ::router/root
               [{:id ::home
                 :segment ""
                 :name "Overview"
                 :landing 10} ; Lowest priority landing - public
                {:id ::buttons
                 :segment "buttons"
                 :name "Buttons"}
                {:id ::icons
                 :segment "icons"
                 :name "Icons"}
                {:id ::popups
                 :segment "popups"
                 :name "Popups"}
                {:id ::admin-dashboard
                 :segment "admin"
                 :name "Admin Dashboard"
                 :roles #{:admin}
                 :landing 100} ; Highest priority landing for admins
                {:id ::theming
                 :segment "theming"
                 :name "Theming"
                 :roles #{:admin}}]))

(defn toggle-theme! []
  (let [new-theme (if (= (:theme @state) "light") "dark" "light")]
    (swap! state assoc :theme new-theme)
    ;; Persist to localStorage
    (.setItem js/localStorage "ty-theme" new-theme)))

(defn nav-item [{:keys [route-id label icon on-click]}]
  (let [active? (router/rendered? route-id true)]
    [:button.flex.items-center.gap-3.w-full.px-4.py-2.text-left.rounded-md.transition-colors
     {:class (if active?
               [:bg-blue-600 :text-white]
               [:text-gray-700 "dark:text-gray-300" "hover:bg-gray-100" "dark:hover:bg-gray-700"])
      :on {:click (or on-click #(router/navigate! route-id))}}
     (when icon
       [:ty-icon {:name icon
                  :size "sm"}])
     [:span label]]))

(defn sidebar []
  [:aside.w-64.bg-white.dark:bg-gray-800.border-r.border-gray-200.dark:border-gray-700.h-full
   [:div.p-6
    [:h1.text-2xl.font-bold.text-gray-900.dark:text-white.mb-2 "Ty Components"]
    [:p.text-sm.text-gray-600.dark:text-gray-400 "Web Components Library"]]

   [:nav.px-4.pb-6
    [:div.space-y-1
     (nav-item {:route-id ::home
                :label "Overview"
                :icon "home"})
     (nav-item {:route-id ::buttons
                :label "Buttons"
                :icon "click"})
     (nav-item {:route-id ::icons
                :label "Icons"
                :icon "image"})
     (nav-item {:route-id ::popups
                :label "Popups"
                :icon "message-square"})
     (when (router/authorized? ::admin-dashboard)
       (nav-item {:route-id ::admin-dashboard
                  :label "Admin Dashboard"
                  :icon "shield"}))
     (when (router/authorized? ::theming)
       (nav-item {:route-id ::theming
                  :label "Theming"
                  :icon "palette"}))]]])

(defn header []
  [:header.bg-white.dark:bg-gray-800.border-b.border-gray-200.dark:border-gray-700.px-6.py-4
   [:div.flex.justify-between.items-center
    [:h2.text-xl.font-semibold.text-gray-900.dark:text-white
     (cond
       (router/rendered? ::home true) "Welcome to Ty"
       (router/rendered? ::buttons true) "Button Components"
       (router/rendered? ::icons true) "Icon Library"
       (router/rendered? ::popups true) "Popup Components"
       (router/rendered? ::admin-dashboard true) "Admin Dashboard"
       (router/rendered? ::theming true) "Theming & Customization"
       :else "Ty Components")]
    [:div.flex.items-center.gap-4
     ;; User info / auth toggle for testing
     [:button.text-sm.px-3.py-1.rounded.bg-gray-200.dark:bg-gray-700
      {:on {:click #(if (:user/roles @state)
                      (swap! state dissoc :user/roles)
                      (swap! state assoc :user/roles #{:admin}))}}
      (if (:user/roles @state)
        "Logout"
        "Login as Admin")]
     [:button.p-2.rounded-md.bg-gray-100.dark:bg-gray-700.hover:bg-gray-200.dark:hover:bg-gray-600.transition-colors
      {:on {:click toggle-theme!}}
      [:ty-icon {:name (if (= (:theme @state) "light") "moon" "sun")
                 :size "md"}]]]]])

(defn app []
  [:div.h-screen.flex.bg-gray-50.dark:bg-gray-900
   (sidebar)
   [:div.flex-1.flex.flex-col
    (header)
    [:main.flex-1.overflow-auto.p-6
     (cond
       (router/rendered? ::home true) (home/home-view)
       (router/rendered? ::buttons true) (buttons/buttons-view)
       (router/rendered? ::icons true) (icons/icons-view)
       (router/rendered? ::popups true) (popups/popups-view)
       (router/rendered? ::admin-dashboard true)
       [:div.max-w-4xl.mx-auto
        [:h1.text-3xl.font-bold.text-gray-900.dark:text-white.mb-4
         "Admin Dashboard"]
        [:p.text-gray-600.dark:text-gray-400.mb-6
         "This page is only visible to admin users. It has the highest landing priority (100)."]
        [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
         [:h2.text-xl.font-semibold.mb-4 "Landing System Demo"]
         [:p.mb-4 "Navigate to the root URL (/) to see the landing system in action:"]
         [:ul.list-disc.list-inside.space-y-2.text-gray-600.dark:text-gray-400
          [:li "Admin users will be redirected here (priority: 100)"]
          [:li "Non-admin users will be redirected to Overview (priority: 10)"]
          [:li "Try logging out and navigating to / to see the difference"]]]]
       (router/rendered? ::theming true) [:div "Theming page - coming soon"]
       :else [:div "404"])]]])

(defn render-app! []
  (binding [context/*roles* (:user/roles @state)]
    (rdom/render (js/document.getElementById "app") (app))))

(defn ^:dev/after-load init []
  ;; Register demo icons
  (demo-icons/register-demo-icons!)

  ;; Setup routes if not already done
  (when (empty? (:children (:tree @router/*router*)))
    (setup-routes!))

  ;; Initialize router with landing support
  (binding [context/*roles* (:user/roles @state)]
    (router/init! "" "/")) ; base="" landing-url="/"

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

  ;; Re-render when router changes
  (add-watch router/*router* ::render
             (fn [_ _ _ _]
               (render-app!)))

  ;; Re-render when user changes
  (add-watch context/*user* ::render
             (fn [_ _ _ _]
               (render-app!)))

  ;; Initial render
  (render-app!))
