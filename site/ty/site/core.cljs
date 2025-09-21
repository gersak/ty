(ns ty.site.core
  (:require [clojure.string :as str]
            [replicant.dom :as rdom]
            [ty.components]
            [ty.context :as context]
            [ty.layout :as layout]
            [ty.router :as router]
            [ty.site.docs :as docs]
            [ty.site.docs.index :as docs.index]
            [ty.site.icons :as site-icons]
            [ty.site.state :refer [state]]
            [ty.site.views.contact-form :as contact-form]
            [ty.site.views.event-booking :as event-booking]
            [ty.site.views.getting-started :as getting-started]
            [ty.site.views.landing :as landing]
            [ty.site.views.ty-styles :as ty-styles]
            [ty.site.views.user-profile :as user-profile]))

;; Define site routes
(router/link ::router/root
             (concat
               [{:id ::landing
                 :segment ""
                 :name "Welcome"}
                {:id ::user-profile
                 :segment "user-profile"
                 :name "User Profile"}
                {:id ::event-booking
                 :segment "event-booking"
                 :name "Event Booking"}
                {:id ::contact-form
                 :segment "contact-form"
                 :name "Contact Form"}
                {:id ::ty-styles
                 :segment "ty-styles"
                 :name "Ty Styles"}
                {:id ::getting-started
                 :segment "getting-started"
                 :name "Getting Started"}
                {:id :ty.site/docs
                 :segment "docs"
                 :view docs.index/view
                 :name "Documentation"}]))

(defn toggle-theme! []
  (swap! state update :theme #(if (= % "light") "dark" "light"))
  (let [theme (:theme @state)]
    (if (= theme "dark")
      (.add (.-classList js/document.documentElement) "dark")
      (.remove (.-classList js/document.documentElement) "dark"))
    (.setItem js/localStorage "theme" theme)))

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
   (nav-item {:route-id ::landing
              :label "Welcome"
              :icon "home"})
   (nav-item {:route-id ::user-profile
              :label "User Profile"
              :icon "user"})
   (nav-item {:route-id ::event-booking
              :label "Event Booking"
              :icon "calendar"})
   (nav-item {:route-id ::contact-form
              :label "Contact Form"
              :icon "mail"})
   (nav-item {:route-id ::ty-styles
              :label "Ty Styles"
              :icon "palette"})
   (nav-item {:route-id ::getting-started
              :label "Getting Started"
              :icon "rocket"})])

(defn sidebar []
  (if (docs/in-docs?)
    ;; Docs mode sidebar
    [:aside.w-64.ty-elevated.border-r.ty-border+.h-full
     [:div.p-4.lg:p-6
      [:h1.text-lg.lg:text-2xl.font-bold.ty-text.mb-1.lg:mb-2 "Documentation"]
      [:p.text-xs.lg:text-sm.ty-text- "Component Reference"]]
     [:nav.px-2.lg:px-4.pb-4.lg:pb-6
      (docs/docs-sidebar)]]
    ;; Regular sidebar
    [:aside.w-64.ty-elevated.border-r.ty-border+.h-full
     [:div.p-4.lg:p-6
      [:h1.text-lg.lg:text-2xl.font-bold.ty-text.mb-1.lg:mb-2 "Ty Components"]
      [:p.text-xs.lg:text-sm.ty-text- "Professional Web Components"]]
     [:nav.px-2.lg:px-4.pb-4.lg:pb-6
      (nav-items)]]))

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
        [:h1.text-lg.font-bold.ty-text
         (if (docs/in-docs?) "Documentation" "Ty Components")]
        [:button.p-2.rounded-md.hover:ty-content
         {:on {:click toggle-mobile-menu!}}
         [:ty-icon {:name "x"
                    :size "sm"}]]]
       [:p.text-sm.ty-text-.mb-6
        (if (docs/in-docs?) "Component Reference" "Professional Web Components")]]
      [:nav.px-4.pb-6
       (if (docs/in-docs?)
         (docs/docs-sidebar)
         (nav-items))]]]))

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
        (docs/in-docs?) "Ty Documentation"
        (router/rendered? ::landing true) "Welcome to Ty Components"
        (router/rendered? ::user-profile true) "User Profile Scenario"
        (router/rendered? ::event-booking true) "Event Booking Scenario"
        (router/rendered? ::contact-form true) "Contact Form Scenario"
        (router/rendered? ::ty-styles true) "Ty Design System"
        (router/rendered? ::getting-started true) "Getting Started Guide"
        :else "Ty Components")]]

    ;; Actions section with Docs/Examples toggle and Theme toggle
    [:div.flex.items-center.gap-2.lg:gap-3.flex-shrink-0
     ;; Docs/Examples toggle button with icon in start slot
     (if (docs/in-docs?)
       ;; When in docs, show "Examples" button to go back
       [:div.w-30
        [:ty-button {:flavor "neutral"
                     :size "md"
                     :wide true
                     :on {:click #(router/navigate! ::landing)}}
         [:ty-icon {:slot "start"
                    :name "layers"
                    :size "sm"}]
         "Examples"]]
       ;; When not in docs, show "Docs" button
       [:div.w-30
        [:ty-button {:flavor "neutral"
                     :wide true
                     :on {:click #(router/navigate! :ty.site/docs)}}
         [:ty-icon {:slot "start"
                    :name "book-open"
                    :size "sm"}]
         "Docs"]])

     ;; Theme toggle button
     [:ty-button
      {:on {:click toggle-theme!}
       :action true}
      [:ty-icon {:name (if (= (:theme @state) "light") "moon" "sun")
                 :size "sm"}]]]]])

(defn app []
  (layout/with-window
    (let [show-sidebar? (layout/breakpoint>= :lg) ; Show sidebar on large screens and up
          sidebar-width (if show-sidebar? 256 0)
          header-height (if (layout/breakpoint>= :lg) 64 56) ; Smaller header on mobile
          content-padding (if (layout/breakpoint>= :lg) 48 32)] ; Less padding on mobile
      [:div.h-screen.flex.ty-canvas
       (mobile-menu)
       (when show-sidebar? (sidebar))
       [:div.flex-1.flex.flex-col.min-w-0
        (header)
        [:main.flex-1.overflow-auto.p-3.lg:p-6.ty-canvas
         ;; Provide accurate container dimensions for the main content area
         (layout/with-container
           {:width (- (layout/container-width) sidebar-width content-padding)
            :height (- (layout/container-height) header-height content-padding)}
           (cond
             ;; Regular views
             (router/rendered? ::landing true) (landing/view)
             (router/rendered? ::user-profile true) (user-profile/view)
             (router/rendered? ::event-booking true) (event-booking/view)
             (router/rendered? ::contact-form true) (contact-form/view)
             (router/rendered? ::ty-styles true) (ty-styles/view)
             (router/rendered? ::getting-started true) (getting-started/view)

             (docs/in-docs?) (docs/render)

             ;; Check for other component docs
             :else [:div.ty-elevated.p-8.rounded-lg.text-center
                    [:h1.text-2xl.font-bold.ty-text.mb-4 "Page Not Found"]
                    [:p.ty-text- "The requested page could not be found."]]))]]])))

(defn render-app! []
  (binding [context/*roles* (:user/roles @state)]
    (rdom/render (.getElementById js/document "app") (app))))

(defn ^:dev/after-load init []
  ;; Initialize theme from localStorage or system preference
  (let [stored-theme (.getItem js/localStorage "theme")
        system-theme (if (and (.-matchMedia js/window)
                              (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)")))
                       "dark" "light")
        theme (or stored-theme system-theme "light")]
    (swap! state assoc :theme theme)
    (if (= theme "dark")
      (.add (.-classList js/document.documentElement) "dark")
      (.remove (.-classList js/document.documentElement) "dark")))

  ;; Initialize router
  (router/init!)

  ;; Watch router changes and re-render
  (add-watch router/*router* ::render
             (fn [_ _ _ _]
               (render-app!)
               ;; Highlight code blocks after navigation in docs
               (when (docs/in-docs?)
                 (js/setTimeout
                   #(when (and js/window.hljs (.-highlightAll js/window.hljs))
                      (js/window.hljs.highlightAll))
                   100))))

  ;; Watch state changes and re-render
  (add-watch state ::render
             (fn [_ _ _ _] (render-app!)))

  ;; Watch window size changes for responsive layout (fixes sidebar toggle)
  (add-watch layout/window-size ::window-resize
             (fn [_ _ _ _] (render-app!)))

  ;; Watch element size changes for layout system
  (add-watch context/*element-sizes* ::resizing
             (fn [_ _ _ _] (render-app!)))

  ;; Initial render
  (render-app!))
