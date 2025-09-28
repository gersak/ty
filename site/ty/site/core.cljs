(ns ty.site.core
  (:require [clojure.string :as str]
            [replicant.dom :as rdom]
            [ty.components]
            [ty.context :as context]
            [ty.layout :as layout]
            [ty.router :as router]
            [ty.site.docs :as docs]
            [ty.site.icons :as site-icons]
            [ty.site.state :refer [state]]
            [ty.site.views.contact-form :as contact-form]
            [ty.site.views.event-booking :as event-booking]
            [ty.site.views.getting-started :as getting-started]
            [ty.site.views.landing :as landing]
            [ty.site.views.ty-styles :as ty-styles]
            [ty.site.views.user-profile :as user-profile]))

;; Configuration for GitHub Pages deployment
;; These are replaced at build time via closure-defines
(goog-define ROUTER_BASE "")
(goog-define PRODUCTION false)

;; Define site routes

;; Define all routes with their view handlers (like docs system)
(def site-routes
  [{:id ::landing
    :segment ""
    :name "Welcome"
    :icon "home"
    :landing 10
    :view landing/view}
   {:id ::user-profile
    :segment "user-profile"
    :name "User Profile"
    :icon "user"
    :view user-profile/view}
   {:id ::event-booking
    :segment "event-booking"
    :name "Event Booking"
    :icon "calendar"
    :view event-booking/view}
   {:id ::contact-form
    :segment "contact-form"
    :name "Contact Form"
    :icon "mail"
    :view contact-form/view}
   {:id ::ty-styles
    :segment "ty-styles"
    :name "Ty Styles"
    :icon "palette"
    :view ty-styles/view}
   {:id ::getting-started
    :segment "getting-started"
    :name "Getting Started"
    :icon "rocket"
    :view getting-started/view}
   {:id :ty.site/docs
    :segment "docs"
    :view getting-started/view ; Default docs view
    :name "Documentation"}])

;; Import docs components that are already configured
(def component-routes docs/docs-components)

;; Import guide components (integration guides)
(def guide-routes docs/guide-components)

(router/link ::router/root
             (concat
              ;; Extract route configs from site-routes  
               (map #(select-keys % [:id :segment :name :landing]) site-routes)
              ;; Add component routes - docs-components already have correct structure
               (map (fn [route]
                      (-> route
                          (select-keys [:id :segment :name])
                          (update :segment (fn [segment] (str "docs/" segment))))) component-routes)
              ;; Add guide routes - docs/guide-components  
               (map (fn [route]
                      (-> route
                          (select-keys [:id :segment :name])
                          (update :segment (fn [segment] (str "docs/" segment))))) guide-routes)))

(defn toggle-theme! []
  (swap! state update :theme #(if (= % "light") "dark" "light"))
  (let [theme (:theme @state)]
    (if (= theme "dark")
      (.add (.-classList js/document.documentElement) "dark")
      (.remove (.-classList js/document.documentElement) "dark"))
    (.setItem js/localStorage "theme" theme)))

(defn toggle-mobile-menu! []
  (swap! state update :mobile-menu-open not))

(defn scroll-main-to-top!
  "Smoothly scrolls the main content area to the top"
  []
  (when-let [main-element (.querySelector js/document "main.overflow-auto")]
    (.scrollTo main-element #js {:top 0
                                 :behavior "smooth"})))

(defn nav-item [{:keys [route-id label icon on-click]}]
  (let [active? (router/rendered? route-id true)]
    [:button.w-full.text-left.px-4.py-2.rounded.transition-colors.cursor-pointer.flex.items-center
     {:class (if active?
               ["ty-bg-primary-" "ty-text-primary++"]
               ["hover:ty-bg-neutral" "ty-text"])
      :on {:click (fn []
                    (when on-click (on-click))
                    (when (and (not on-click) route-id)
                      (router/navigate! route-id)
                      ;; Scroll to top after navigation
                      (js/setTimeout scroll-main-to-top! 100)
                      ;; Close mobile menu after navigation
                      (swap! state assoc :mobile-menu-open false)))}}
     (when icon
       [:ty-icon.mr-2 {:name icon
                       :size "sm"}])
     [:div.flex.items-center.gap-2
      [:span.text-sm label]]]))

(defn nav-section [{:keys [title items]}]
  [:div.mb-4
   (when title
     [:div.px-4.py-2
      [:h3.text-xs.font-medium.ty-text-.uppercase.tracking-wider.mb-2 title]])
   [:div.space-y-0.5
    (for [item items]
      ^{:key (:label item)} (nav-item item))]])

(defn nav-items []
  [:div.space-y-6
   ;; Main Navigation
   (nav-section
     {:items [(let [route (first (filter #(= (:id %) ::landing) site-routes))]
                {:route-id (:id route)
                 :label (:name route)
                 :icon (:icon route)})]})

   ;; Examples Section (anchor navigation)
   (nav-section
     {:title "Live Examples"
      :items [{:route-id "#user-profile"
               :label "User Profile"
               :icon "user"
               :on-click #(.scrollIntoView (.getElementById js/document "user-profile") #js {:behavior "smooth"})}
              {:route-id "#event-booking"
               :label "Event Booking"
               :icon "calendar"
               :on-click #(.scrollIntoView (.getElementById js/document "event-booking") #js {:behavior "smooth"})}
              {:route-id "#contact-form"
               :label "Contact Form"
               :icon "mail"
               :on-click #(.scrollIntoView (.getElementById js/document "contact-form") #js {:behavior "smooth"})}]})

   ;; Quickstart (route navigation)
   (nav-section
     {:title "Quickstart"
      :items (concat
             ;; Getting Started from site-routes
               [{:route-id ::getting-started
                 :label "Getting Started"
                 :icon "rocket"}]
             ;; Guide routes from docs/guide-components
               (for [route guide-routes]
                 {:route-id (:id route)
                  :label (:name route)
                  :icon (:icon route)}))})

   ;; Components Section (route navigation to component docs)
   (nav-section
     {:title "Components"
      :items (for [route component-routes]
               {:route-id (:id route)
                :label (:name route)
                :icon (:icon route)})})])

(defn render
  "Render the appropriate view based on current route (like docs/render)"
  []
  (let [all-routes (concat site-routes component-routes guide-routes)
        current-route (some #(when (router/rendered? (:id %) true) %) all-routes)]
    (if current-route
      ((:view current-route))
      ;; Handle docs system separately for now
      (if (docs/in-docs?)
        (docs/render)
        ;; Fallback for unknown routes
        [:div.ty-elevated.p-8.rounded-lg.text-center
         [:h1.text-2xl.font-bold.ty-text.mb-4 "Page Not Found"]
         [:p.ty-text- "The requested page could not be found."]]))))

(defn sidebar []
  [:aside.w-64.ty-elevated.border-r.ty-border+.h-full
   [:div.p-4.lg:p-6
    [:h1.text-lg.lg:text-2xl.font-bold.ty-text.mb-1.lg:mb-2 "Ty Components"]
    [:p.text-xs.lg:text-sm.ty-text- "Professional Web Components"]]
   [:nav.px-2.lg:px-4.pb-4.lg:pb-6
    (nav-items)]])

(defn mobile-menu []
  [:div.lg:hidden
   [:ty-modal {:open (:mobile-menu-open @state)
               :on {:ty-modal-close toggle-mobile-menu!}}
    [:div.p-6.mx-auto.space-y-6.rounded-lg.ty-floating.box-border
     {:style {:width "240px"}}
     ;; Header section
     [:div.text-center.space-y-2
      [:h2.text-xl.font-bold.ty-text
       (if (docs/in-docs?) "Documentation" "Navigation")]
      [:p.text-sm.ty-text-
       (if (docs/in-docs?) "Component Reference" "Choose your destination")]]

     ;; Navigation content
     [:div.space-y-4
      [:div (nav-items)]]]]])

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
           ;; Use unified render system
           (render))]]])))

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

  ;; Initialize router with base path for GitHub Pages
  (router/init! (when-not (str/blank? ROUTER_BASE) ROUTER_BASE))

  ;; Watch router changes and re-render
  (add-watch router/*router* ::render
             (fn [_ _ _ _]
               (render-app!)
               ;; No global highlighting needed - individual code blocks handle it via :replicant/on-mount
               ))

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
