(ns ty.site.core
  (:require [clojure.string :as str]
            [replicant.dom :as rdom]
            [ty.context :as context]
            [ty.layout :as layout]
            [ty.router :as router]
            [ty.site.docs :as docs]
            [ty.site.icons]
            [ty.site.state :refer [state]]
            [ty.site.views.landing :as landing]
            [ty.site.views.tabs-test :as tabs-test]
            [ty.site.views.why :as why]))

;; Configuration for GitHub Pages deployment
;; These are replaced at build time via closure-defines
(goog-define ROUTER_BASE "")
(goog-define PRODUCTION false)

;; Define site routes

;; Define all routes with their view handlers (like docs system)
(def site-routes
  [;; Main landing page
   {:id ::landing
    :segment "welcome"
    :hash "top"
    :name "Welcome"
    :icon "home"
    :landing 20
    :view landing/view}

   ;; Why ty Exists - The story page
   {:id ::why
    :segment "why"
    :name "Why ty exists"
    :icon "lightbulb"
    :landing 10
    :view why/view}

   ;; Tabs Test Page
   {:id ::tabs-test
    :segment "tabs-test"
    :name "Tabs Test"
    :icon "layout"
    :view tabs-test/view}

   ;; Landing page fragments for examples
   {:id ::landing-user-profile
    :segment "welcome"
    :hash "user-profile"
    :name "User Profile"
    :icon "user"
    :view landing/view}
   {:id ::landing-event-booking
    :segment "welcome"
    :hash "event-booking"
    :name "Event Booking"
    :icon "calendar"
    :view landing/view}
   {:id ::landing-contact-form
    :segment "welcome"
    :hash "contact-form"
    :name "Contact Form"
    :icon "mail"
    :view landing/view}])

;; Import docs components that are already configured
(def component-routes docs/docs-components)

;; Import guide components (integration guides)
(def guide-routes docs/guide-components)

(router/link ::router/root
             (concat
              ;; Extract route configs from site-routes  
               site-routes
              ;; Add component routes - docs-components already have correct structure
               (map (fn [route]
                      (-> route
                          (update :segment (fn [segment] (str "docs/" segment)))))
                    component-routes)
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

(defn toggle-mobile-menu!
  ([] (swap! state update :mobile-menu-open not)))

(defn close-mobile-menu!
  ([] (swap! state assoc :mobile-menu-open false)))

(defn scroll-main-to-top!
  "Smoothly scrolls the main content area to the top"
  []
  (when-let [main-element (.querySelector js/document "main.overflow-auto")]
    (.scrollTo main-element #js {:top 0
                                 :behavior "smooth"})))

(defn should-scroll-for-route?
  "Determine if navigation to target route should trigger scroll to top"
  [target-route-id]
  (let [target-url (router/component-path (:tree @router/*router*) target-route-id)
        target-components (router/url->components (:tree @router/*router*) target-url)
        target-component (last target-components)]
    ;; Only scroll to top if target component doesn't have hash
    (nil? (:hash target-component))))

(defn nav-item
  "Render a single navigation item (can be parent or child)"
  [{:keys [route-id label icon indented?]}]
  (let [active? (router/rendered? route-id true)]
    [:button.w-full.text-left.px-4.py-2.rounded.transition-colors.cursor-pointer.flex.items-center
     {:class (concat
               (if active?
                 ["ty-bg-primary-" "ty-text-primary++"]
                 ["hover:ty-bg-neutral" "ty-text"])
               (when indented? ["pl-8"])) ; Indent child items
      :on {:click (fn []
                    ;; Check if target route has hash before navigation
                    (let [should-scroll-top? (should-scroll-for-route? route-id)]
                      ;; Navigate
                      (router/navigate! route-id)
                      ;; Scroll to top only for non-fragment routes
                      (when should-scroll-top?
                        (js/setTimeout scroll-main-to-top! 100))
                      ;; Close mobile menu
                      (swap! state assoc :mobile-menu-open false)))}}
     (when icon
       [:ty-icon.mr-2 {:name icon
                       :size "sm"}])
     [:div.flex.items-center.gap-2
      [:span.text-sm label]]]))

(defn nav-section
  "Render a navigation section with optional children"
  [{:keys [title items]}]
  [:div.mb-4
   (when title
     [:div.px-4.py-2
      [:h3.text-xs.font-medium.ty-text-.uppercase.tracking-wider.mb-2 title]])
   [:div.space-y-0.5
    (for [item items]
      (let [has-children? (seq (:children item))]
        ^{:key (:label item)}
        [:div
         ;; Parent item
         (nav-item (assoc item :indented? false))
         ;; Children items (indented)
         (when has-children?
           [:div.space-y-0.5
            (for [child (:children item)]
              ^{:key (:label child)}
              (nav-item (assoc child :indented? true)))])]))]])

(defn nav-items []
  [:div.space-y-6
   ;; Main Navigation
   (nav-section
     {:items [(let [route (first (filter #(= (:id %) ::landing) site-routes))]
                {:route-id (:id route)
                 :label (:name route)
                 :icon (:icon route)})]})

   ;; Why ty Section
   (nav-section
     {:items [{:route-id ::why
               :label "Why ty exists"
               :icon "lightbulb"}]})

   ;; Tabs Test Section
   #_(nav-section
       {:title "Development"
        :items [{:route-id ::tabs-test
                 :label "Tabs Test"
                 :icon "layout"}]})

   ;; Examples Section (unified router navigation)
   (nav-section
     {:title "Live Examples"
      :items [{:route-id ::landing-user-profile
               :label "User Profile"
               :icon "user"}
              {:route-id ::landing-event-booking
               :label "Event Booking"
               :icon "calendar"}
              {:route-id ::landing-contact-form
               :label "Contact Form"
               :icon "mail"}]})

   ;; Quickstart (route navigation)
   (nav-section
     {:title "Quickstart"
      :items (for [route guide-routes]
               {:route-id (:id route)
                :label (:name route)
                :icon (:icon route)})})

   ;; Components Section (route navigation to component docs)
   (nav-section
     {:title "Components"
      :items (for [route component-routes]
               {:route-id (:id route)
                :label (:name route)
                :icon (:icon route)
                :children (when-let [children (:children route)]
                            (map (fn [child]
                                   {:route-id (:id child)
                                    :label (:name child)
                                    :icon (:icon child)})
                                 children))})})])

(defn flatten-routes
  "Recursively flatten routes including children"
  [routes]
  (mapcat (fn [route]
            (if-let [children (:children route)]
              (cons (dissoc route :children) (flatten-routes children))
              [route]))
          routes))

(defn render
  "Render the appropriate view based on current route (like docs/render)"
  []
  (let [all-routes (flatten-routes (concat site-routes component-routes guide-routes))
        current-route (some #(when (router/rendered? (:id %) true) %) all-routes)
        view (:view current-route)]
    (when (ifn? view) (view))))

(defn sidebar []
  [:aside.w-64.ty-elevated.border-r.ty-border+.h-full.flex.flex-col
   [:div.p-4.lg:p-6.flex-shrink-0
    [:h1.text-lg.lg:text-2xl.font-bold.ty-text.mb-1.lg:mb-2 "ty components"]]
   [:nav.px-2.lg:px-4.pb-4.lg:pb-6.flex-1.overflow-y-auto
    (nav-items)]])

(defn mobile-menu []
  (when (:mobile-menu-open @state)
    [:div.lg:hidden
     [:ty-modal {:open true
                 :on {:ty-modal-close close-mobile-menu!}}
      [:div.p-6.mx-auto.rounded-lg.ty-floating.box-border.flex.flex-col
       {:style {:width "320px"
                :max-height "90vh"}}
     ;; Header section (fixed)
       [:div.text-center.space-y-2.flex-shrink-0
        [:h2.text-xl.font-bold.ty-text
         (if (docs/in-docs?) "Documentation" "Navigation")]
        [:p.text-sm.ty-text-
         (if (docs/in-docs?) "Component Reference" "Choose your destination")]]

     ;; Navigation content (scrollable)
       [:div.flex-1.overflow-y-auto.mt-6.min-h-0
        [:div.space-y-4
         (nav-items)]]]]]))

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
        (router/rendered? ::user-profile true) "User Profile Scenario"
        (router/rendered? ::event-booking true) "Event Booking Scenario"
        (router/rendered? ::contact-form true) "Contact Form Scenario"
        (router/rendered? ::landing) "Welcome"
        (router/rendered? ::why true) "Why ty exists"
        (router/rendered? ::ty-styles true) "ty Design System"
        (router/rendered? ::getting-started true) "Getting Started Guide"
        :else "Documentation")]]

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
      [:div.h-screen.flex.ty-canvas.ty-text
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

  (ty.site.icons/register-icons!)

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
