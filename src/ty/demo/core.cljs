(ns ty.demo.core
  (:require [replicant.dom :as rdom]
            [ty.context :as context]
            [ty.core]
            [ty.demo.icons :as demo-icons]
            [ty.demo.state :refer [state]]
            [ty.demo.views.buttons :as buttons]
            [ty.demo.views.calendar :as calendar]
            [ty.demo.views.dropdowns :as dropdowns]
            [ty.demo.views.formatting :as formatting]
            [ty.demo.views.home :as home]
            [ty.demo.views.i18n :as i18n-views]
            [ty.demo.views.icons :as icons]
            [ty.demo.views.inputs :as inputs]
            [ty.demo.views.layout :as layout-views]
            [ty.demo.views.modal :as modal]
            [ty.demo.views.multiselect :as multiselect]
            [ty.demo.views.popups :as popups]
            [ty.demo.views.tags :as tags]
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
              {:id ::dropdowns
               :segment "dropdowns"
               :name "Dropdowns"}
              {:id ::multiselect
               :segment "multiselect"
               :name "Multiselect"}
              {:id ::calendar
               :segment "calendar"
               :name "Calendar"}
              {:id ::icons
               :segment "icons"
               :name "Icons"}
              {:id ::popups
               :segment "popups"
               :name "Popups"}
              {:id ::tags
               :segment "tags"
               :name "Tags"}
              {:id ::modal
               :segment "modal"
               :name "Modal"}
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

(defn toggle-mobile-menu! []
  (swap! state update :mobile-menu-open not))

(defn nav-item [{:keys [route-id label icon on-click]}]
  (let [active? (router/rendered? route-id true)]
    [:button.flex.items-center.gap-2.lg:gap-3.w-full.px-3.lg:px-4.py-2.text-left.rounded-md.transition-colors.text-sm.lg:text-base
     {:class (if active?
               [:bg-blue-600 :text-white]
               [:text-gray-700 "dark:text-gray-300" "hover:bg-gray-100" "dark:hover:bg-gray-700"])
      :on {:click (fn []
                    (when on-click (on-click))
                    (when (and (not on-click) route-id)
                      (router/navigate! route-id)
                      ;; Close mobile menu after navigation
                      (swap! state assoc :mobile-menu-open false)))}}
     (when icon
       [:ty-icon {:name icon
                  :size "sm"}])
     [:span.truncate label]]))

(defn nav-items []
  [:div.space-y-0.5.lg:space-y-1
   (nav-item {:route-id ::home
              :label "Overview"
              :icon "home"})
   (nav-item {:route-id ::buttons
              :label "Buttons"
              :icon "click"})
   (nav-item {:route-id ::inputs
              :label "Inputs"
              :icon "type"})
   (nav-item {:route-id ::dropdowns
              :label "Dropdowns"
              :icon "chevron-down"})
   (nav-item {:route-id ::multiselect
              :label "Multiselect"
              :icon "check-circle"})
   (nav-item {:route-id ::calendar
              :label "Calendar"
              :icon "lucide-calendar"})
   (nav-item {:route-id ::icons
              :label "Icons"
              :icon "image"})
   (nav-item {:route-id ::popups
              :label "Popups"
              :icon "message-square"})
   (nav-item {:route-id ::tags
              :label "Tags"
              :icon "tag"})
   (nav-item {:route-id ::modal
              :label "Modal"
              :icon "square"})
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
                :icon "palette"}))])

(defn sidebar []
  [:aside.w-64.bg-white.dark:bg-gray-800.border-r.border-gray-200.dark:border-gray-700.h-full
   [:div.p-4.lg:p-6
    [:h1.text-lg.lg:text-2xl.font-bold.text-gray-900.dark:text-white.mb-1.lg:mb-2 "Ty Components"]
    [:p.text-xs.lg:text-sm.text-gray-600.dark:text-gray-400 "Web Components Library"]]

   [:nav.px-2.lg:px-4.pb-4.lg:pb-6
    (nav-items)]])

(defn mobile-menu []
  (when (:mobile-menu-open @state)
    [:div.fixed.inset-0.z-50.lg:hidden
     ;; Backdrop
     [:div.fixed.inset-0.bg-black.bg-opacity-50
      {:on {:click toggle-mobile-menu!}}]
     ;; Menu
     [:div.fixed.inset-y-0.left-0.w-72.max-w-xs.bg-white.dark:bg-gray-800.shadow-xl.overflow-y-auto
      [:div.p-4
       [:div.flex.items-center.justify-between.mb-4
        [:h1.text-lg.font-bold.text-gray-900.dark:text-white "Ty Components"]
        [:button.p-2.rounded-md.hover:bg-gray-100.dark:hover:bg-gray-700
         {:on {:click toggle-mobile-menu!}}
         [:ty-icon {:name "x" :size "sm"}]]]
       [:p.text-sm.text-gray-600.dark:text-gray-400.mb-6 "Web Components Library"]]
      [:nav.px-4.pb-6
       (nav-items)]]]))

(defn header []
  [:header.bg-white.dark:bg-gray-800.border-b.border-gray-200.dark:border-gray-700.px-3.py-3.lg:px-6.lg:py-4
   [:div.flex.justify-between.items-center
    [:div.flex.items-center.gap-3.lg:gap-4.min-w-0.flex-1
     ;; Mobile menu button
     [:button.lg:hidden.p-1.5.rounded-md.hover:bg-gray-100.dark:hover:bg-gray-700.flex-shrink-0
      {:on {:click toggle-mobile-menu!}}
      [:ty-icon {:name "menu" :size "sm"}]]
     [:h2.text-base.lg:text-xl.font-semibold.text-gray-900.dark:text-white.truncate
      (cond
        (router/rendered? ::home true) "Welcome to Ty"
        (router/rendered? ::buttons true) "Button Components"
        (router/rendered? ::inputs true) "Input Components"
        (router/rendered? ::dropdowns true) "Dropdown Components"
        (router/rendered? ::multiselect true) "Multiselect Components"
        (router/rendered? ::calendar true) "Calendar Components"
        (router/rendered? ::icons true) "Icon Library"
        (router/rendered? ::popups true) "Popup Components"
        (router/rendered? ::tags true) "Tag Components"
        (router/rendered? ::modal true) "Modal Dialogs"
        (router/rendered? ::admin-dashboard true) "Admin Dashboard"
        (router/rendered? ::theming true) "Theming & Customization"
        :else "Ty Components")]]
    [:div.flex.items-center.gap-2.lg:gap-4.flex-shrink-0
     ;; User info / auth toggle for testing
     [:button.text-xs.lg:text-sm.px-2.lg:px-3.py-1.rounded.bg-gray-200.dark:bg-gray-700.dark:text-gray-200.hidden.sm:block
      {:on {:click #(if (:user/roles @state)
                      (swap! state dissoc :user/roles)
                      (swap! state assoc :user/roles #{:admin}))}}
      (if (:user/roles @state)
        "Logout"
        "Login as Admin")]
     [:button.flex.items-center.justify-center.w-7.h-7.lg:w-8.lg:h-8.rounded-md.bg-gray-100.dark:bg-gray-700.hover:bg-gray-200.dark:hover:bg-gray-600.transition-colors.dark:text-gray-200
      {:on {:click toggle-theme!}}
      [:ty-icon {:name (if (= (:theme @state) "light") "moon" "sun")
                 :size "sm"}]]]]])

(defn app []
  (layout/with-window
    (let [show-sidebar? (layout/breakpoint>= :lg) ; Changed from :sm to :lg for better mobile experience
          sidebar-width (if show-sidebar? 256 0)
          header-height (if (layout/breakpoint>= :lg) 64 56) ; Smaller header on mobile
          content-padding (if (layout/breakpoint>= :lg) 48 32)] ; Less padding on mobile
      [:div.h-screen.flex.bg-gray-50.dark:bg-gray-900
       (mobile-menu) ; Add mobile menu
       (when show-sidebar? (sidebar))
       [:div.flex-1.flex.flex-col.min-w-0 ; Prevent overflow
        (header)
        [:main.flex-1.overflow-auto.p-3.lg:p-6 ; Much smaller padding on mobile
         ;; Provide accurate container dimensions for the main content area
         (layout/with-container
           {:width (- (layout/container-width) sidebar-width content-padding)
            :height (- (layout/container-height) header-height content-padding)}
           (cond
             (router/rendered? ::home true) (home/view)
             (router/rendered? ::buttons true) (buttons/view)
             (router/rendered? ::inputs true) (inputs/view)
             (router/rendered? ::dropdowns true) (dropdowns/view)
             (router/rendered? ::multiselect true) (multiselect/view)
             (router/rendered? ::calendar true) (calendar/view)
             (router/rendered? ::icons true) (icons/view)
             (router/rendered? ::popups true) (popups/view)
             (router/rendered? ::tags true) (tags/view)
             (router/rendered? ::modal true) (modal/view)
             (router/rendered? ::i18n true) (i18n-views/view)
             (router/rendered? ::formatting true) (formatting/view)
             (router/rendered? ::layout true) (layout-views/view)
             (router/rendered? ::admin-dashboard true)
             [:div.max-w-4xl.mx-auto
              [:h1.text-2xl.lg:text-3xl.font-bold.text-gray-900.dark:text-white.mb-4
               "Admin Dashboard"]
              [:p.text-sm.lg:text-base.text-gray-600.dark:text-gray-400.mb-6
               "This page is only visible to admin users. It has the highest landing priority (100)."]
              [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-4.lg:p-6
               [:h2.text-lg.lg:text-xl.font-semibold.mb-4 "Landing System Demo"]
               [:p.text-sm.lg:text-base.mb-4 "Navigate to the root URL (/) to see the landing system in action:"]
               [:ul.list-disc.list-inside.space-y-2.text-sm.lg:text-base.text-gray-600.dark:text-gray-400
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
