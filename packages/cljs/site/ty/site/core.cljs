(ns ty.site.core
  (:require [clojure.string :as str]
            [replicant.dom :as rdom]
            [ty.layout :as layout]
            [ty.router :as router]
            [ty.site.docs :as docs]
            [ty.site.icons]
            [ty.site.state :refer [state]]
            [ty.site.views.landing :as landing]
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
              ;; Add component routes - change to /components/ prefix
              (map (fn [route]
                     (-> route
                         (update :segment (fn [segment] (str "components/" segment)))))
                   component-routes)
              ;; Add guide routes - change to /guides/ prefix
              (map (fn [route]
                     (-> route
                         (select-keys [:id :segment :name])
                         (update :segment (fn [segment] (str "guides/" segment))))) guide-routes)))

(defn toggle-theme! []
  (swap! state update :theme #(if (= % "light") "dark" "light"))
  (let [theme (:theme @state)]
    (if (= theme "dark")
      (.add (.-classList js/document.documentElement) "dark")
      (.remove (.-classList js/document.documentElement) "dark"))
    (.setItem js/localStorage "theme" theme)))

;; ============================================================================
;; Navigation Section Management
;; ============================================================================

(defn get-open-section-from-storage
  "Read the open navigation section from localStorage"
  []
  (when-let [stored (.getItem js/localStorage "ty-nav-open-section")]
    (keyword stored)))

(defn set-open-section-in-storage!
  "Write the open navigation section to localStorage"
  [section]
  (if section
    (.setItem js/localStorage "ty-nav-open-section" (name section))
    (.removeItem js/localStorage "ty-nav-open-section")))

(defn get-last-visited-route
  "Get the last visited route for a section from localStorage"
  [section-key]
  (when-let [stored (.getItem js/localStorage (str "ty-nav-last-" (name section-key)))]
    (keyword stored)))

(defn set-last-visited-route!
  "Save the last visited route for a section to localStorage"
  [section-key route-id]
  (when (and section-key route-id)
    (.setItem js/localStorage (str "ty-nav-last-" (name section-key)) (name route-id))))

(defn scroll-main-to-top!
  "Smoothly scrolls the main content area to the top"
  []
  (when-let [main-element (.querySelector js/document "main.overflow-auto")]
    (.scrollTo main-element #js {:top 0
                                 :behavior "smooth"})))

(defn toggle-nav-section!
  "Toggle a navigation section open/closed and navigate to first/last item"
  [section-key items]
  (let [current (get @state :navigation.section/open)
        is-opening? (not= current section-key)]
    ;; Toggle section state
    (swap! state assoc :navigation.section/open (if is-opening? section-key nil))
    (set-open-section-in-storage! (if is-opening? section-key nil))

    ;; Navigate when opening
    (when (and is-opening? (seq items))
      (let [last-visited (get-last-visited-route section-key)
            target-route (if last-visited
                           ;; Navigate to last visited if exists
                           (some #(when (= (:route-id %) last-visited) (:route-id %)) items)
                           ;; Otherwise navigate to first item
                           (:route-id (first items)))]
        (when target-route
          (router/navigate! target-route)
          (js/setTimeout scroll-main-to-top! 100))))))

(defn route-in-list?
  "Check if a route-id exists in a flat list of routes"
  [route-id routes]
  (some #(= (:id %) route-id) routes))

(defn route-in-tree?
  "Check if a route-id exists in a tree of routes (including children)"
  [route-id routes]
  (some (fn [route]
          (or (= (:id route) route-id)
              (when-let [children (:children route)]
                (route-in-list? route-id children))))
        routes))

(defn route->section
  "Determine which section owns a route"
  [route-id]
  (cond
    ;; Check if route is in guide-routes (QUICKSTART section)
    (route-in-list? route-id guide-routes) :quickstart

    ;; Check if route is in component-routes (COMPONENTS section)
    (route-in-tree? route-id component-routes) :components

    ;; Otherwise nil (Welcome, Landing, Live Examples = collapse all)
    :else nil))

(defn flatten-routes
  "Recursively flatten routes including children"
  [routes]
  (mapcat (fn [route]
            (if-let [children (:children route)]
              (cons (dissoc route :children) (flatten-routes children))
              [route]))
          routes))

(defn auto-expand-section!
  "Automatically expand section based on current route"
  []
  (let [all-routes (flatten-routes (concat site-routes component-routes guide-routes))
        current-route (some #(when (router/rendered? (:id %) true) %) all-routes)
        section (when current-route (route->section (:id current-route)))]
    ;; Set section state (nil will collapse all)
    (swap! state assoc :navigation.section/open section)
    (set-open-section-in-storage! section)))

(defn toggle-mobile-menu!
  ([] (swap! state update :mobile-menu-open not)))

(defn close-mobile-menu!
  ([] (swap! state assoc :mobile-menu-open false)))

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
  [{:keys [route-id label icon indented? section-key]}]
  (let [active? (router/rendered? route-id true)]
    [:button.w-full.text-left.px-4.py-2.rounded.transition-colors.cursor-pointer.flex.items-center.gap-2
     {:class (concat
              (if active?
                ["ty-bg-primary-" "ty-text-primary++"]
                ["hover:ty-bg-neutral" "ty-text"])
              (when indented? ["pl-8"])) ; Indent child items
      :on {:click (fn []
                    ;; Save last visited route for section
                    (when section-key
                      (set-last-visited-route! section-key route-id))
                    ;; Check if target route has hash before navigation
                    (let [should-scroll-top? (should-scroll-for-route? route-id)]
                      ;; Navigate
                      (router/navigate! route-id)
                      ;; Scroll to top only for non-fragment routes
                      (when should-scroll-top?
                        (js/setTimeout scroll-main-to-top! 100))
                      ;; Close mobile menu
                      (swap! state assoc :mobile-menu-open false)))}}
     [:ty-icon {:name icon
                :size "sm"}]
     [:span.text-sm label]]))

(defn nav-section
  "Render a navigation section with optional children and collapsible behavior"
  [{:keys [title items collapsible? section-key]}]
  (let [is-open? (= (get @state :navigation.section/open) section-key)
        icon (if is-open? "chevron-down" "arrow-right")]
    [:div.mb-4
     (when title
       (if collapsible?
         ;; Collapsible section header (clickable)
         [:div.px-4.py-2.cursor-pointer.rounded.transition-colors
          {:on {:click #(toggle-nav-section! section-key items)}}
          [:div.flex.items-center.gap-2
           [:ty-icon {:name icon
                      :size "sm"
                      :class (if is-open? "ty-text-primary" "ty-text-")}]
           [:h3.text-xs.font-medium.uppercase.tracking-wider
            {:class (if is-open? "ty-text-primary font-semibold" "ty-text-")}
            title]]]
         ;; Non-collapsible section header (static)
         [:div.px-4.py-2
          [:h3.text-xs.font-medium.ty-text-.uppercase.tracking-wider.mb-2 title]]))

     ;; Children container with collapse animation
     (if collapsible?
       ;; Collapsible children
       [:div.overflow-hidden.transition-all.duration-300
        {:class (if is-open? "opacity-100" "opacity-0")
         :style {:max-height (if is-open? "1000px" "0")
                 :width (if is-open? "100%" "0")}}
        [:div.space-y-0.5.mt-2
         (for [item items]
           (let [has-children? (seq (:children item))]
             ^{:key (:label item)}
             [:div
              ;; Parent item
              (nav-item (assoc item :indented? false :section-key section-key))
              ;; Children items (indented)
              (when has-children?
                [:div.space-y-0.5
                 (for [child (:children item)]
                   ^{:key (:label child)}
                   (nav-item (assoc child :indented? true :section-key section-key)))])]))]]
       ;; Non-collapsible children (always visible)
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
                  (nav-item (assoc child :indented? true)))])]))])]))

(defn nav-items []
  [:div.space-y-6
   ;; Main Navigation
   (nav-section
    {:items [(let [route (first (filter #(= (:id %) ::landing) site-routes))]
               {:route-id (:id route)
                :label (:name route)
                :icon (:icon route)})]})

   ;; Tabs Test Section
   #_(nav-section
      {:title "Development"
       :items [{:route-id ::tabs-test
                :label "Tabs Test"
                :icon "layout"}]})

   ;; Examples Section (unified router navigation) - Always visible
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

   ;; Quickstart (route navigation) - Collapsible
   (nav-section
    {:title "Quickstart"
     :collapsible? true
     :section-key :quickstart
     :items (for [route guide-routes]
              {:route-id (:id route)
               :label (:name route)
               :icon (:icon route)})})

   ;; Components Section (route navigation to component docs) - Collapsible
   (nav-section
    {:title "Components"
     :collapsible? true
     :section-key :components
     :items (keep
             (fn [route]
               (when-not (= (:id route) :ty.site/docs)
                 {:route-id (:id route)
                  :label (:name route)
                  :icon (:icon route)
                  :children (when-let [children (:children route)]
                              (map (fn [child]
                                     {:route-id (:id child)
                                      :label (:name child)
                                      :icon (:icon child)})
                                   children))}))
             component-routes)})])

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
                 :on {:close close-mobile-menu!}}
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
  (binding [router/*roles* (:user/roles @state)]
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

  ;; Auto-expand navigation section based on current route (initial load)
  (auto-expand-section!)

  ;; Watch router changes for auto-expand and re-render
  (add-watch router/*router* ::render
             (fn [_ _ _ _]
               (auto-expand-section!) ; Auto-expand on route change
               (render-app!)
               ;; No global highlighting needed - individual code blocks handle it via :replicant/on-mount
               ))

  ;; Watch state changes and re-render
  (add-watch state ::render
             (fn [_ _ _ _] (render-app!)))

  ;; Watch window size changes for responsive layout (fixes sidebar toggle)
  (add-watch layout/window-size ::window-resize
             (fn [_ _ _ _] (render-app!)))

  ;; Initial render
  (render-app!))
