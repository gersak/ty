(ns ty.demo.core
  (:require [replicant.dom :as rdom]
            [ty.context :as context]
            [ty.demo.icons :as demo-icons]
            [ty.demo.state :refer [state]]
            [ty.demo.views.buttons :as buttons]
            [ty.demo.views.formatting :as formatting]
            [ty.demo.views.home :as home]
            [ty.demo.views.i18n :as i18n-views]
            [ty.demo.views.icons :as icons]
            [ty.demo.views.inputs :as inputs]
            [ty.demo.views.layout :as layout-views]
            [ty.demo.views.popups :as popups]
            [ty.layout :as layout]
            [ty.router :as router]))

(router/link ::router/root
             [{:id ::home
               :segment ""
               :name "Overview"
               :landing 10} ; Lowest priority landing - public
              {:id ::buttons
               :segment "buttons"
               :name "Buttons"}
              {:id ::inputs
               :segment "inputs"
               :name "Inputs"}
              {:id ::icons
               :segment "icons"
               :name "Icons"}
              {:id ::popups
               :segment "popups"
               :name "Popups"}
              {:id ::i18n
               :segment "i18n"
               :name "i18n"}
              {:id ::formatting
               :segment "formatting"
               :name "Formatting"}
              {:id ::layout
               :segment "layout"
               :name "Layout"}
              {:id ::admin-dashboard
               :segment "admin"
               :name "Admin Dashboard"
               :roles #{:admin}
               :landing 100} ; Highest priority landing for admins
              {:id ::theming
               :segment "theming"
               :name "Theming"
               :roles #{:admin}}])

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
     (nav-item {:route-id ::inputs
                :label "Inputs"
                :icon "type"})
     (nav-item {:route-id ::icons
                :label "Icons"
                :icon "image"})
     (nav-item {:route-id ::popups
                :label "Popups"
                :icon "message-square"})
     (nav-item {:route-id ::i18n
                :label "i18n"
                :icon "globe"})
     (nav-item {:route-id ::formatting
                :label "Formatting"
                :icon "hash"})
     (nav-item {:route-id ::layout
                :label "Layout"
                :icon "layout"})
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
       (router/rendered? ::inputs true) "Input Components"
       (router/rendered? ::icons true) "Icon Library"
       (router/rendered? ::popups true) "Popup Components"
       (router/rendered? ::i18n true) "Internationalization"
       (router/rendered? ::formatting true) "Number & Date Formatting"
       (router/rendered? ::layout true) "Layout Context System"
       (router/rendered? ::admin-dashboard true) "Admin Dashboard"
       (router/rendered? ::theming true) "Theming & Customization"
       :else "Ty Components")]
    [:div.flex.items-center.gap-4
     ;; User info / auth toggle for testing
     [:button.text-sm.px-3.py-1.rounded.bg-gray-200.dark:bg-gray-700.dark:text-gray-200
      {:on {:click #(if (:user/roles @state)
                      (swap! state dissoc :user/roles)
                      (swap! state assoc :user/roles #{:admin}))}}
      (if (:user/roles @state)
        "Logout"
        "Login as Admin")]
     [:button.flex.items-center.justify-center.w-8.h-8.rounded-md.bg-gray-100.dark:bg-gray-700.hover:bg-gray-200.dark:hover:bg-gray-600.transition-colors.dark:text-gray-200
      {:on {:click toggle-theme!}}
      [:ty-icon {:name (if (= (:theme @state) "light") "moon" "sun")
                 :size "md"}]]]]])

(defn app []
  (layout/with-window
    (let [show-sidebar? (layout/breakpoint>= :sm)
          sidebar-width (if show-sidebar? 256 0)
          header-height 64
          content-padding 48] ; 24px padding on each side (p-6 = 1.5rem = 24px)
      [:div.h-screen.flex.bg-gray-50.dark:bg-gray-900
       (when show-sidebar? (sidebar))
       [:div.flex-1.flex.flex-col
        (header)
        [:main.flex-1.overflow-auto.p-6
         ;; Provide accurate container dimensions for the main content area
         (layout/with-container
           {:width (- (layout/container-width) sidebar-width content-padding)
            :height (- (layout/container-height) header-height content-padding)}
           (cond
             (router/rendered? ::home true) (home/home-view)
             (router/rendered? ::buttons true) (buttons/buttons-view)
             (router/rendered? ::inputs true) (inputs/inputs-view)
             (router/rendered? ::icons true) (icons/icons-view)
             (router/rendered? ::popups true) (popups/popups-view)
             (router/rendered? ::i18n true) (i18n-views/i18n-view)
             (router/rendered? ::formatting true) (formatting/formatting-view)
             (router/rendered? ::layout true) (layout-views/layout-view)
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
             :else [:div "404"]))]]])))

(defn render-app! []
  (binding [context/*roles* (:user/roles @state)]
    (rdom/render (js/document.getElementById "app") (app))))

(defn ^:dev/after-load init []
  ;; Register demo icons
  (demo-icons/register-demo-icons!)

  ;; Setup routes if not already done
  ; (when (empty? (:children (:tree @router/*router*)))
  ;   (setup-routes!))

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

  (add-watch layout/window-size ::render
             (fn [_ _ _ _]
               (render-app!)))

  (add-watch context/*element-sizes* ::render
             (fn [_ _ _ _]
               (render-app!)))

  ;; Initial render
  (render-app!))
