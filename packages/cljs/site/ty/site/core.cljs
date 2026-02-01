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
  (when-let [scroll-container (.getElementById js/document "main-scroll-container")]
    (.scrollTo scroll-container #js {:top 0
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

(defn calculate-collapsible-height
  "Calculate available height for collapsible nav sections.
   Uses window height and state values from resize observers for reactivity.
   When a section expands, it takes ALL available sidebar space."
  []
  (let [;; Window height
        window-h (:height @layout/window-size)

        ;; Get heights from state (stored by resize observer events)
        sizes (:sidebar-sizes @state)
        header-h (or (:header sizes) 64)
        sidebar-title-h (or (:title sizes) (:height (layout/get-observer-size "ty.sidebar.title")))
        fixed-nav-h (or (:fixed-content-height sizes) 200)
        quickstart-h (or (:quickstart sizes) 40)
        components-h (or (:components sizes) 40)

        ;; Which section is open?
        open-section (get @state :navigation.section/open)

        ;; The OTHER section's height (which is collapsed, so just header)
        other-section-h (case open-section
                          :quickstart components-h
                          :components quickstart-h
                          ;; Both closed - show both headers
                          (+ quickstart-h components-h))

        ;; Calculate available height for expanded section
        available (- window-h
                     header-h
                     sidebar-title-h
                     fixed-nav-h
                     other-section-h
                     80)]
    (max available 150)))

(defn scroll-shadow-hooks
  "Create on-mount/on-unmount hooks for scroll shadow tracking"
  [section-key]
  (let [update-shadows (fn [^js event]
                         (when-let [el (.-target event)]
                           (let [scroll-top (.-scrollTop el)
                                 scroll-height (.-scrollHeight el)
                                 client-height (.-clientHeight el)
                                 can-scroll-up (> scroll-top 0)
                                 can-scroll-down (< (+ scroll-top client-height)
                                                    (- scroll-height 1))
                                 final {:up can-scroll-up
                                        :down can-scroll-down}]
                             (when-not (= final (:scroll-shadows @state))
                               (swap! state assoc-in [:scroll-shadows section-key] final)))))]
    {:replicant/on-mount
     (fn [{^js el :replicant/node}]
       (update-shadows el)
       (.addEventListener el "scroll" update-shadows)
       (set! (.-_scrollHandler el) update-shadows))

     :replicant/on-unmount
     (fn [{^js el :replicant/node}]
       (when-let [handler (.-_scrollHandler el)]
         (.removeEventListener el "scroll" handler)
         (set! (.-_scrollHandler el) nil))
       (swap! state update :scroll-shadows dissoc section-key))}))

(defn nav-section
  "Render a navigation section with optional children and collapsible behavior"
  [{:keys [title items collapsible? section-key]}]
  (let [is-open? (= (get @state :navigation.section/open) section-key)
        icon (if is-open? "chevron-down" "arrow-right")
        observer-id (when section-key (str "ty.sidebar." (name section-key)))]
    [:div.mb-4
     (when title
       (if collapsible?
         ;; Collapsible section header (clickable) - wrapped in resize observer
         [:div.px-4.py-2.cursor-pointer.rounded.transition-colors
          {:on {:click #(toggle-nav-section! section-key items)}}
          [:div.flex.items-center.gap-2
           [:ty-icon {:name icon
                      :size "sm"
                      :class (if is-open? "ty-text-primary" "ty-text-")}]
           [:h3.text-xs.font-medium.uppercase.tracking-wider
            {:class (if is-open? ["ty-text-primary" "font-semibold"] "ty-text-")}
            title]]]
         ;; Non-collapsible section header (static)
         [:div.px-4.py-2
          [:h3.text-xs.font-medium.ty-text-.uppercase.tracking-wider.mb-2 title]]))

     ;; Children container with collapse animation
     (if collapsible?
       ;; Collapsible children with calculated height
       (let [available-height (calculate-collapsible-height)
             shadows (get-in @state [:scroll-shadows section-key])
             show-top (:up shadows)
             show-bottom (:down shadows)]
         [:div.overflow-hidden.transition-all.duration-300
          {:class (if is-open? "opacity-100" "opacity-0")
           :style {:max-height (if is-open? (str available-height "px") "0")
                   :width (if is-open? "100%" "0")}}
          ;; Wrapper for shadows
          [:div.relative.mt-2
           ;; Top shadow (ellipsis glow effect)
           [:div.absolute.left-0.right-0.pointer-events-none.z-10.transition-opacity.duration-300
            {:style {:top "-40px"
                     :height "80px"
                     :background "radial-gradient(ellipse 100% 30% at center, rgba(128, 128, 128, 0.15), rgba(0, 0, 0, 0), transparent)"
                     :clip-path "inset(50% 0 0 0)"
                     :opacity (if show-top 1 0)}}]
           ;; Scrollable content
           [:div.space-y-0.5.overflow-y-auto
            (merge {:style {:max-height (str (- available-height 8) "px")
                            :scrollbar-width "none"}}
                   (scroll-shadow-hooks section-key))
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
                      (nav-item (assoc child :indented? true :section-key section-key)))])]))]
           ;; Bottom shadow (ellipsis glow effect)
           [:div.absolute.left-0.right-0.pointer-events-none.z-10.transition-opacity.duration-300
            {:style {:bottom "-30px"
                     :height "60px"
                     :background "radial-gradient(ellipse 100% 20% at center, rgba(128, 128, 128, 0.2), rgba(0, 0, 0, 0), transparent)"
                     :clip-path "inset(0 0 50% 0)"
                     :opacity (if show-bottom 1 0)}}]]])
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

(defn resize-observer-hooks
  "Create on-mount/on-unmount hooks for a resize observer that updates state"
  [observer-id state-path]
  {:replicant/on-mount
   (fn [{^js el :replicant/node}]
     (when (and js/window.tyResizeObserver el)
       (let [unsubscribe (js/window.tyResizeObserver.onResize
                           observer-id
                           (fn [^js size]
                             (swap! state assoc-in state-path (.-height size))))]
         (set! (.-_resizeUnsub el) unsubscribe))))

   :replicant/on-unmount
   (fn [{^js el :replicant/node}]
     (when-let [unsubscribe (.-_resizeUnsub el)]
       (unsubscribe)
       (set! (.-_resizeUnsub el) nil)))})

(defn nav-items []
  [:div.space-y-6
   ;; Fixed content (always visible) - track height
   [:ty-resize-observer
    (merge {:id "ty.sidebar.nav-items"}
           (resize-observer-hooks "ty.sidebar.nav-items" [:sidebar-sizes :fixed-content-height]))
    [:div.space-y-6
     ;; Main Navigation
     (nav-section
       {:items [(let [route (first (filter #(= (:id %) ::landing) site-routes))]
                  {:route-id (:id route)
                   :label (:name route)
                   :icon (:icon route)})]})

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
                 :icon "mail"}]})]]

   ;; Quickstart (route navigation) - Collapsible
   [:ty-resize-observer
    (merge {:id "ty.sidebar.quickstart"}
           (resize-observer-hooks "ty.sidebar.quickstart" [:sidebar-sizes :quickstart]))
    (nav-section
      {:title "Quickstart"
       :collapsible? true
       :section-key :quickstart
       :items (for [route guide-routes]
                {:route-id (:id route)
                 :label (:name route)
                 :icon (:icon route)})})]

   ;; Components Section (route navigation to component docs) - Collapsible
   [:ty-resize-observer
    (merge {:id "ty.sidebar.components"}
           (resize-observer-hooks "ty.sidebar.components" [:sidebar-sizes :components]))
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
                component-routes)})]])

(defn render
  "Render the appropriate view based on current route (like docs/render)"
  []
  (let [all-routes (flatten-routes (concat site-routes component-routes guide-routes))
        current-route (some #(when (router/rendered? (:id %) true) %) all-routes)
        view (:view current-route)]
    (when (ifn? view) (view))))

(defn sidebar-content
  "Sidebar navigation content (used in both desktop sidebar and mobile menu)"
  []
  [:aside.h-full.flex.flex-col
   [:ty-resize-observer
    (merge {:id "ty.sidebar.title"}
           (resize-observer-hooks "ty.sidebar.title" [:sidebar-sizes :title]))
    [:div.p-4.lg:p-6.flex-shrink-0
     [:h1.text-lg.lg:text-2xl.font-bold.ty-text.mb-1.lg:mb-2 "ty components"]]]
   ;; Scrollable nav with hidden scrollbar (overflow wrapper technique)
   [:div.flex-1.overflow-hidden
    [:nav.px-2.lg:px-4.pb-4.lg:pb-6.h-full.overflow-y-scroll
     {:style {:scrollbar-width "none"      ;; Firefox
              :margin-right "-20px"        ;; Push scrollbar outside
              :padding-right "20px"}}      ;; Compensate for margin
     (nav-items)]]])

(defn slugify
  "Convert text to URL-friendly slug"
  [text]
  (-> text
      str/lower-case
      (str/replace #"[^\w\s-]" "")
      (str/replace #"\s+" "-")
      (str/replace #"-+" "-")
      (str/trim)))

(defn toc-sidebar
  "Table of Contents sidebar with scroll-spy"
  []
  (let [headings (get-in @state [:toc :headings])
        active-id (get-in @state [:toc :active-id])]
    [:aside.sticky.top-6.self-start
     {:style {:width "200px"}}
     (when (seq headings)
       [:nav.text-sm
        [:h4.text-xs.font-medium.ty-text-.uppercase.tracking-wider.mb-3
         "On this page"]
        [:ul.space-y-0.5.border-l.ty-border-
         (for [{:keys [id text level]} headings]
           ^{:key id}
           [:li
            [:a.block.py-1.5.pl-3.-ml-px.border-l-2.transition-all.duration-150
             {:href (str "#" id)
              :class [(if (= active-id id)
                        "ty-border-primary ty-text-primary font-medium"
                        "border-transparent ty-text- hover:ty-text hover:ty-border-neutral")
                      (when (= level 3) "pl-5 text-xs")]
              :on {:click (fn [e]
                            (.preventDefault e)
                            (when-let [el (.getElementById js/document id)]
                              (.scrollIntoView el #js {:behavior "smooth"
                                                       :block "start"})))}}
             text]])]])]))

;; ============================================================================
;; Command Palette Search
;; ============================================================================

(defn fuzzy-match?
  "Check if query fuzzy-matches text (case-insensitive, matches if all chars appear in order)"
  [query text]
  (let [q (str/lower-case query)
        t (str/lower-case text)]
    (loop [qi 0
           ti 0]
      (cond
        (>= qi (count q)) true              ; All query chars matched
        (>= ti (count t)) false             ; Ran out of text
        (= (nth q qi) (nth t ti))           ; Match found
        (recur (inc qi) (inc ti))
        :else                               ; Keep searching
        (recur qi (inc ti))))))

(defn build-search-index
  "Build search index from docs and guide components"
  []
  (concat
   ;; Guide components
    (for [route guide-routes]
      {:id (:id route)
       :name (:name route)
       :type :guide
       :icon (:icon route)
       :segment (:segment route)})
   ;; Docs components (flatten children)
    (mapcat
      (fn [route]
        (let [parent {:id (:id route)
                      :name (:name route)
                      :type :component
                      :icon (:icon route)
                      :segment (:segment route)}
              children (when-let [ch (:children route)]
                         (for [child ch]
                           {:id (:id child)
                            :name (:name child)
                            :type :component
                            :icon (:icon child)
                            :segment (:segment child)
                            :parent-name (:name route)}))]
          (if children
            (cons parent children)
            [parent])))
      component-routes)))

(defn search-items
  "Filter search index by query"
  [query]
  (let [index (build-search-index)]
    (if (str/blank? query)
      (take 8 index) ; Show first 8 items when no query
      (->> index
           (filter #(fuzzy-match? query (:name %)))
           (take 10)))))

(defn open-search! []
  (swap! state assoc-in [:search :open] true)
  (swap! state assoc-in [:search :query] "")
  (swap! state assoc-in [:search :selected-index] 0)
  ;; Focus the input after render
  (js/setTimeout
    #(when-let [input (.getElementById js/document "search-input")]
       (.focus input))
    50))

(defn close-search! []
  (swap! state assoc-in [:search :open] false))

(defn select-search-result!
  "Navigate to selected search result"
  [result]
  (close-search!)
  (router/navigate! (:id result))
  (js/setTimeout scroll-main-to-top! 100))

(defn search-modal
  "Command palette search modal"
  []
  (let [{:keys [open query selected-index]} (:search @state)
        results (search-items query)
        result-count (count results)]
    (when open
      [:ty-modal {:open true
                  :on {:close close-search!}}
       [:div.ty-floating.rounded-lg.shadow-xl.overflow-hidden
        {:style {:width "min(560px, 90vw)"
                 :max-height "80vh"}}
        ;; Search input
        [:div.p-4.border-b.ty-border
         [:div.flex.items-center.gap-3
          [:ty-icon {:name "search"
                     :size "sm"
                     :class "ty-text-"}]
          [:input#search-input.flex-1.bg-transparent.outline-none.text-lg.ty-text
           {:type "text"
            :placeholder "Search components and guides..."
            :value query
            :on {:input (fn [e]
                          (swap! state assoc-in [:search :query] (.. e -target -value))
                          (swap! state assoc-in [:search :selected-index] 0))
                 :keydown (fn [e]
                            (let [key (.-key e)]
                              (cond
                                (= key "ArrowDown")
                                (do (.preventDefault e)
                                    (swap! state update-in [:search :selected-index]
                                           #(min (inc %) (dec result-count))))

                                (= key "ArrowUp")
                                (do (.preventDefault e)
                                    (swap! state update-in [:search :selected-index]
                                           #(max (dec %) 0)))

                                (= key "Enter")
                                (when-let [result (nth results selected-index nil)]
                                  (select-search-result! result))

                                (= key "Escape")
                                (close-search!))))}}]
          [:kbd.text-xs.ty-text-.ty-bg-neutral-.px-2.py-1.rounded "esc"]]]

        ;; Results list (fixed height to prevent twitching)
        [:div.overflow-y-auto {:style {:height "400px"}}
         (if (seq results)
           [:ul.py-2
            (for [[idx result] (map-indexed vector results)]
              ^{:key (:id result)}
              [:li
               [:button.w-full.text-left.px-4.py-3.flex.items-center.gap-3.transition-colors
                {:class (if (= idx selected-index)
                          "ty-bg-primary- ty-text-primary++"
                          "hover:ty-bg-neutral-")
                 :on {:click #(select-search-result! result)
                      :mouseenter #(swap! state assoc-in [:search :selected-index] idx)}}
                ;; Icon
                [:div.w-8.h-8.rounded.flex.items-center.justify-center
                 {:class (case (:type result)
                           :guide "ty-bg-success- ty-text-success"
                           :component "ty-bg-primary- ty-text-primary"
                           "ty-bg-neutral-")}
                 [:ty-icon {:name (:icon result)
                            :size "sm"}]]
                ;; Name and type
                [:div.flex-1.min-w-0
                 [:div.font-medium.ty-text.truncate (:name result)]
                 [:div.text-xs.ty-text-
                  (str (case (:type result)
                         :guide "Guide"
                         :component "Component"
                         "Page")
                       (when-let [parent (:parent-name result)]
                         (str " · " parent)))]]
                ;; Enter hint for selected
                (when (= idx selected-index)
                  [:kbd.text-xs.ty-text-.ty-bg-neutral.px-2.py-1.rounded "↵"])]])]
           [:div.py-8.text-center.ty-text-
            [:ty-icon.mb-2.opacity-50 {:name "search"
                                       :size "lg"}]
            [:p "No results found"]])]

        ;; Footer with keyboard hints
        [:div.px-4.py-3.border-t.ty-border.flex.items-center.gap-4.text-xs.ty-text-
         [:span.flex.items-center.gap-1
          [:kbd.ty-bg-neutral-.px-1.5.py-0.5.rounded "↑"]
          [:kbd.ty-bg-neutral-.px-1.5.py-0.5.rounded "↓"]
          " Navigate"]
         [:span.flex.items-center.gap-1
          [:kbd.ty-bg-neutral-.px-1.5.py-0.5.rounded "↵"]
          " Select"]
         [:span.flex.items-center.gap-1
          [:kbd.ty-bg-neutral-.px-1.5.py-0.5.rounded "esc"]
          " Close"]]]])))

(defonce keyboard-shortcuts-initialized (atom false))

(defn setup-keyboard-shortcuts!
  "Setup global keyboard shortcuts (only once)"
  []
  (when-not @keyboard-shortcuts-initialized
    (reset! keyboard-shortcuts-initialized true)
    (.addEventListener js/document "keydown"
                       (fn [e]
                         (let [key (.-key e)
                               cmd-or-ctrl? (or (.-metaKey e) (.-ctrlKey e))
                               ;; Don't trigger shortcuts when typing in inputs
                               in-input? (when-let [active (.-activeElement js/document)]
                                           (or (= (.-tagName active) "INPUT")
                                               (= (.-tagName active) "TEXTAREA")
                                               (.-isContentEditable active)))]
                           ;; Cmd+K or Ctrl+K to open search (always works)
                           (when (and cmd-or-ctrl? (= key "k"))
                             (.preventDefault e)
                             (if (get-in @state [:search :open])
                               (close-search!)
                               (open-search!))))))))

;; ============================================================================
;; Table of Contents Scroll Spy
;; ============================================================================

(defonce toc-observer (atom nil))

(defn extract-headings!
  "Extract h2 and h3 headings from main content and update state"
  []
  (js/setTimeout
    (fn []
      (when-let [main-el (.querySelector js/document "main")]
        (let [heading-els (.querySelectorAll main-el "h2[id], h3[id]")
              headings (vec (for [el (array-seq heading-els)]
                              {:id (.-id el)
                               :text (.-textContent el)
                               :level (if (= (.-tagName el) "H2") 2 3)}))]
          (swap! state assoc-in [:toc :headings] headings))))
    100))

(defn setup-toc-observer!
  "Setup IntersectionObserver for scroll-spy on headings"
  []
  ;; Disconnect existing observer
  (when @toc-observer
    (.disconnect @toc-observer))

  (js/setTimeout
    (fn []
      (when-let [scroll-container (.getElementById js/document "main-scroll-container")]
        (let [observer (js/IntersectionObserver.
                         (fn [entries]
                         ;; Find the first intersecting entry (topmost visible heading)
                           (let [intersecting (->> entries
                                                   (filter #(.-isIntersecting %))
                                                   (sort-by #(-> ^js % .-boundingClientRect .-top))
                                                   first)]
                             (when intersecting
                               (let [id (.. intersecting -target -id)]
                                 (swap! state assoc-in [:toc :active-id] id)))))
                         #js {:root scroll-container
                              :rootMargin "-10% 0px -80% 0px"
                              :threshold 0})]
         ;; Observe all headings with IDs
          (when-let [main-el (.querySelector js/document "main")]
            (doseq [el (array-seq (.querySelectorAll main-el "h2[id], h3[id]"))]
              (.observe observer el)))
          (reset! toc-observer observer))))
    200))

(defn update-toc!
  "Extract headings and setup scroll observer"
  []
  (extract-headings!)
  (setup-toc-observer!))

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
  [:ty-resize-observer
   (merge {:id "ty.header"}
          (resize-observer-hooks "ty.header" [:sidebar-sizes :header]))
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

    ;; Actions section with Search, Docs/Examples toggle and Theme toggle
     [:div.flex.items-center.gap-2.lg:gap-3.flex-shrink-0
     ;; Search button
      [:button.hidden.sm:flex.items-center.gap-2.px-3.py-1.5.rounded-md.ty-bg-neutral-.hover:ty-bg-neutral.transition-colors
       {:on {:click open-search!}}
       [:ty-icon {:name "search"
                  :size "sm"
                  :class "ty-text-"}]
       [:span.text-sm.ty-text- "Search..."]
       [:kbd.text-xs.ty-text-.ty-bg-neutral.px-1.5.py-0.5.rounded.ml-2
        (if (.-userAgent js/navigator)
          (if (str/includes? (.-userAgent js/navigator) "Mac") "⌘K" "Ctrl+K")
          "⌘K")]]
     ;; Mobile search button
      [:button.sm:hidden.p-1.5.rounded-md.hover:ty-bg-neutral.transition-colors
       {:on {:click open-search!}}
       [:ty-icon {:name "search"
                  :size "sm"}]]
     ;; Theme toggle button
      [:ty-button
       {:on {:click toggle-theme!}
        :action true}
       [:ty-icon {:name (if (= (:theme @state) "light") "moon" "sun")
                  :size "sm"}]]]]]])

(defn app []
  (layout/with-window
    (let [show-sidebar? (layout/breakpoint>= :lg) ; Show sidebar on large screens and up
          show-toc? (layout/breakpoint>= :xl)     ; Show TOC on extra large screens
          header-height (if (layout/breakpoint>= :lg) 64 56)
          content-padding (if (layout/breakpoint>= :lg) 48 32)]
      [:div.h-screen.flex.flex-col.ty-canvas.ty-text
       (mobile-menu)
       (search-modal)
       (header)
       ;; Main scrollable area
       [:div.flex-1.overflow-auto {:id "main-scroll-container"}
        ;; Centered container with CSS Grid
        [:div.mx-auto.px-4.lg:px-6.py-6
         {:style {:display "grid"
                  :grid-template-columns (cond
                                           show-toc? "240px minmax(0, 768px) 200px"
                                           show-sidebar? "240px minmax(0, 1fr)"
                                           :else "1fr")
                  :max-width "1300px"
                  :gap "24px"}}
         ;; Left sidebar (navigation)
         (when show-sidebar?
           [:div.sticky.top-0.self-start.h-fit
            {:style {:max-height "calc(100vh - 120px)"}}
            (sidebar-content)])
         ;; Main content
         [:main.min-w-0
          (layout/with-container
            {:width (cond
                      show-toc? 768
                      show-sidebar? (- (layout/container-width) 240 200 content-padding)
                      :else (- (layout/container-width) content-padding))
             :height (- (layout/container-height) header-height content-padding)}
            (render))]
         ;; Right TOC sidebar
         (when show-toc?
           (toc-sidebar))]]])))

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

  ;; Setup global keyboard shortcuts (Cmd+K for search)
  (setup-keyboard-shortcuts!)

  ;; Watch router changes for auto-expand and re-render
  (add-watch router/*router* ::render
             (fn [_ _ _ _]
               (auto-expand-section!) ; Auto-expand on route change
               (render-app!)
               ;; Update TOC headings for new page
               (update-toc!)))

  ;; Watch state changes and re-render
  (add-watch state ::render
             (fn [_ _ _ _] (render-app!)))

  ;; Watch window size changes for responsive layout (fixes sidebar toggle)
  (add-watch layout/window-size ::window-resize
             (fn [_ _ _ _] (render-app!)))

  ;; Initial render
  (render-app!)

  ;; Initial TOC extraction
  (update-toc!))
