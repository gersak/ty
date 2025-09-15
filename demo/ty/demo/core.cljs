(ns ty.demo.core
  (:require [replicant.dom :as rdom]
            [ty.components]
            [ty.context :as context]
            [ty.demo.icons :as demo-icons]
            [ty.demo.state :refer [state]]
            [ty.demo.views.buttons :as buttons]
            [ty.demo.views.calendar :as calendar]
            [ty.demo.views.color-combinations :as color-combinations]
            [ty.demo.views.dropdowns :as dropdowns]
            [ty.demo.views.dropdown-value-test :as dropdown-value-test]
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
            [ty.demo.views.theme-utilities :as theme-utilities]
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
              {:id ::dropdown-value-test
               :segment "dropdown-value-test"
               :name "Dropdown Value Test"}
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
              {:id ::theme-utilities
               :segment "theme-utilities"
               :name "Theme Utilities"}
              {:id ::color-combinations
               :segment "color-combinations"
               :name "Color Combinations"}
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
    [:button.menu-item
     {:class (when active? "active")
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
   (nav-item {:route-id ::dropdown-value-test
              :label "Value Test"
              :icon "test-tube"})
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
   (nav-item {:route-id ::theme-utilities
              :label "Theme Utilities"
              :icon "palette"})
   (nav-item {:route-id ::color-combinations
              :label "Color Combinations"
              :icon "brush"})
   (when (router/authorized? ::admin-dashboard)
     (nav-item {:route-id ::admin-dashboard
                :label "Admin Dashboard"
                :icon "shield"}))
   (when (router/authorized? ::theming)
     (nav-item {:route-id ::theming
                :label "Theming"
                :icon "palette"}))])

(defn sidebar []
  [:aside.w-64.ty-elevated.border-r.ty-border+.h-full ; NEW: Short .ty-elevated includes both background + shadow
   [:div.p-4.lg:p-6
    [:h1.text-lg.lg:text-2xl.font-bold.ty-text.mb-1.lg:mb-2 "Ty Components"] ; NEW: ty-text
    [:p.text-xs.lg:text-sm.ty-text- "Web Components Library"]] ; NEW: ty-text-

   [:nav.px-2.lg:px-4.pb-4.lg:pb-6
    (nav-items)]])

(defn mobile-menu []
  (when (:mobile-menu-open @state)
    [:div.fixed.inset-0.z-50.lg:hidden
     ;; Backdrop
     [:div.fixed.inset-0.bg-black.bg-opacity-50
      {:on {:click toggle-mobile-menu!}}]
     ;; Menu
     [:div.fixed.inset-y-0.left-0.w-72.max-w-xs.ty-elevated.shadow-xl.overflow-y-auto
      [:div.p-4
       [:div.flex.items-center.justify-between.mb-4
        [:h1.text-lg.font-bold.ty-text "Ty Components"]
        [:button.p-2.rounded-md.hover:ty-content
         {:on {:click toggle-mobile-menu!}}
         [:ty-icon {:name "x"
                    :size "sm"}]]]
       [:p.text-sm.ty-text-.mb-6 "Web Components Library"]]
      [:nav.px-4.pb-6
       (nav-items)]]]))

(defn header []
  [:header.ty-elevated.border-b.ty-border+.px-3.py-3.lg:px-6.lg:py-4
   [:div.flex.justify-between.items-center
    [:div.flex.items-center.gap-3.lg:gap-4.min-w-0.flex-1
     ;; Mobile menu button
     [:button.lg:hidden.p-1.5.rounded-md.hover:ty-content.flex-shrink-0
      {:on {:click toggle-mobile-menu!}}
      [:ty-icon {:name "menu"
                 :size "sm"}]]
     [:h2.text-base.lg:text-xl.font-semibold.ty-text.truncate
      (cond
        (router/rendered? ::home true) "Welcome to Ty"
        (router/rendered? ::buttons true) "Button Components"
        (router/rendered? ::inputs true) "Input Components"
        (router/rendered? ::dropdowns true) "Dropdown Components"
        (router/rendered? ::dropdown-value-test true) "Dropdown Value Clearing Test"
        (router/rendered? ::multiselect true) "Multiselect Components"

        (router/rendered? ::calendar true) "Calendar Components"
        (router/rendered? ::icons true) "Icon Library"
        (router/rendered? ::popups true) "Popup Components"
        (router/rendered? ::tags true) "Tag Components"
        (router/rendered? ::modal true) "Modal Dialogs"
        (router/rendered? ::i18n true) "Internationalization"
        (router/rendered? ::formatting true) "Number & Date Formatting"
        (router/rendered? ::layout true) "Layout System"
        (router/rendered? ::theme-utilities true) "Theme Utilities"
        (router/rendered? ::color-combinations true) "Color Combinations"
        (router/rendered? ::admin-dashboard true) "Admin Dashboard"
        (router/rendered? ::theming true) "Theming & Customization"
        :else "Ty Components")]]
    [:div.flex.items-center.gap-2.lg:gap-4.flex-shrink-0
     ;; User info / auth toggle for testing
     [:button.text-xs.lg:text-sm.px-2.lg:px-3.py-1.rounded.ty-bg-neutral.ty-text.hidden.sm:block
      {:on {:click #(if (:user/roles @state)
                      (swap! state dissoc :user/roles)
                      (swap! state assoc :user/roles #{:admin}))}}
      (if (:user/roles @state)
        "Logout"
        "Login as Admin")]
     [:button.flex.items-center.justify-center.w-7.h-7.lg:w-8.lg:h-8.rounded-md.ty-content.hover:ty-content+.transition-colors.ty-text
      {:on {:click toggle-theme!}}
      [:ty-icon {:name (if (= (:theme @state) "light") "moon" "sun")
                 :size "sm"}]]]]])

(defn app []
  (layout/with-window
    (let [show-sidebar? (layout/breakpoint>= :lg) ; Changed from :sm to :lg for better mobile experience
          sidebar-width (if show-sidebar? 256 0)
          header-height (if (layout/breakpoint>= :lg) 64 56) ; Smaller header on mobile
          content-padding (if (layout/breakpoint>= :lg) 48 32)] ; Less padding on mobile
      [:div.h-screen.flex.ty-canvas ; NEW: Short .ty-canvas for app background
       (mobile-menu)
       (when show-sidebar? (sidebar))
       [:div.flex-1.flex.flex-col.min-w-0
        (header)
        [:main.flex-1.overflow-auto.p-3.lg:p-6.ty-content ; NEW: Short .ty-content for main content
         ;; Provide accurate container dimensions for the main content area
         (layout/with-container
           {:width (- (layout/container-width) sidebar-width content-padding)
            :height (- (layout/container-height) header-height content-padding)}
           (cond
             (router/rendered? ::home true) (home/view)
             (router/rendered? ::buttons true) (buttons/view)
             (router/rendered? ::inputs true) (inputs/view)
             (router/rendered? ::dropdowns true) (dropdowns/view)
             (router/rendered? ::dropdown-value-test true) (dropdown-value-test/view)
             (router/rendered? ::multiselect true) (multiselect/view)

             (router/rendered? ::calendar true) (calendar/view)
             (router/rendered? ::icons true) (icons/view)
             (router/rendered? ::popups true) (popups/view)
             (router/rendered? ::tags true) (tags/view)
             (router/rendered? ::modal true) (modal/view)
             (router/rendered? ::i18n true) (i18n-views/view)
             (router/rendered? ::formatting true) (formatting/view)
             (router/rendered? ::layout true) (layout-views/view)
             (router/rendered? ::theme-utilities true) (theme-utilities/view)
             (router/rendered? ::color-combinations true) (color-combinations/view)
             (router/rendered? ::admin-dashboard true)
             [:div.max-w-4xl.mx-auto
              [:h1.text-2xl.lg:text-3xl.font-bold.ty-text.mb-4 ; NEW: ty-text
               "Admin Dashboard"]
              [:p.text-sm.lg:text-base.ty-text-.mb-6 ; NEW: ty-text-
               "This page is only visible to admin users. It has the highest landing priority (100)."]
              [:div.ty-elevated.rounded-lg.p-4.lg:p-6 ; NEW: Short .ty-elevated includes background + shadow
               [:h2.text-lg.lg:text-xl.font-semibold.mb-4 "Landing System Demo"]
               [:p.text-sm.lg:text-base.mb-4 "Navigate to the root URL (/) to see the landing system in action:"]
               [:ul.list-disc.list-inside.space-y-2.text-sm.lg:text-base.ty-text- ; NEW: ty-text-
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
  ; (demo-icons/register-demo-icons!)

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
