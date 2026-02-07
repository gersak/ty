(ns ty.site.docs.resize-observer
  "Documentation for the ty-resize-observer component"
  (:require [replicant.dom :as d]
            [ty.site.docs.common :refer [code-block attribute-table event-table doc-section example-section docs-page]]
            [ty.site.state :as state]))

(defn header-section
  "Title and description"
  []
  [:div.mb-8
   [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-resize-observer"]
   [:p.text-lg.ty-text-
    "A utility component that tracks its own dimensions and makes them available to child components. "
    "Enables responsive layouts where children need to know their container's size."]])

(defn api-reference-section
  "API Reference card with attributes and methods"
  []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-semibold.ty-text++.mb-4 "API Reference"]

   ;; Attributes Table
   [:div.mb-6
    [:h3.text-lg.font-semibold.ty-text+.mb-3 "Attributes"]
    (attribute-table
      [{:name "id"
        :type "string"
        :default "null"
        :description "Required. Unique identifier used to query the element's size"}
       {:name "debounce"
        :type "number"
        :default "0"
        :description "Debounce delay in milliseconds. When > 0, only updates after the specified time of no resize activity"}])]

   ;; JavaScript API
   [:div
    [:h3.text-lg.font-semibold.ty-text+.mb-3 "JavaScript API"]
    [:div.ty-bg-neutral-.rounded.p-4.overflow-x-auto
     [:table.w-full.text-sm
      [:thead
       [:tr.border-b.ty-border
        [:th.text-left.p-2.ty-text+ "Method"]
        [:th.text-left.p-2.ty-text+ "Returns"]
        [:th.text-left.p-2.ty-text+ "Description"]]]
      [:tbody
       [:tr.border-b.ty-border-
        [:td.p-2.font-mono.text-xs "getSize(id)"]
        [:td.p-2.font-mono.text-xs "{width, height}"]
        [:td.p-2.ty-text "Sync query for current size by ID"]]
       [:tr.border-b.ty-border-
        [:td.p-2.font-mono.text-xs "onResize(id, callback)"]
        [:td.p-2.font-mono.text-xs "function"]
        [:td.p-2.ty-text "Subscribe to size changes, returns unsubscribe function"]]
       [:tr
        [:td.p-2.font-mono.text-xs "getAllSizes()"]
        [:td.p-2.font-mono.text-xs "object"]
        [:td.p-2.ty-text "Get all registered sizes (debugging)"]]]]]]])

(defn quick-start-section
  "Quick start with getSize() - no subscription needed"
  []
  [:div.ty-content.rounded-lg.p-6
   [:h3.text-xl.font-semibold.ty-text+.mb-4 "Quick Start"]
   [:p.ty-text-.mb-4 "The simplest way to query container size:"]

   (code-block
     "<ty-resize-observer id=\"container\">
  <div class=\"child\">
    Content here
  </div>
</ty-resize-observer>

<script type=\"module\">
  import { getSize } from '@gersak/ty';
  
  // One-time size query
  const size = getSize('container');
  console.log(size); // { width: 400, height: 300 }
</script>")])

(defn interactive-demo-section
  "Interactive demo with visual size display"
  []
  [:div.ty-elevated.rounded-lg.p-6
   [:h3.text-xl.font-semibold.ty-text+.mb-4 "🎯 Interactive Demo"]
   [:p.ty-text-.mb-4 "Resize the container below and watch the dimensions update in real-time:"]

   [:ty-resize-observer#interactive-demo.ty-border-primary.rounded-lg.border-2
    {:style {:resize "both"
             :overflow "auto"
             :padding "1.5rem"
             :min-width "300px"
             :min-height "200px"}
     :replicant/on-mount
     (fn [{^js el :replicant/node}]
       ;; Subscribe to resize changes and update global state
       (when (and js/window.tyResizeObserver el)
         (let [unsubscribe (js/window.tyResizeObserver.onResize
                             "interactive-demo"
                             (fn [size]
                              ;; Update state with new size (js->clj conversion)
                               (swap! state/state assoc-in [:element-sizes "interactive-demo"]
                                      {:width (.-width size)
                                       :height (.-height size)})))]
           ;; Store unsubscribe function on element for cleanup
           (set! (.-_resizeUnsub el) unsubscribe))))

     :replicant/on-unmount
     (fn [{^js el :replicant/node}]
       ;; Clean up subscription
       (when-let [unsubscribe (.-_resizeUnsub el)]
         (unsubscribe)
         (set! (.-_resizeUnsub el) nil))
       ;; Remove from state
       (swap! state/state update :element-sizes dissoc "interactive-demo"))}

    (let [size (get-in @state/state [:element-sizes "interactive-demo"])]
      [:div
       [:div.flex.items-center.gap-2.mb-4
        [:span.text-lg.font-semibold.ty-text "👆 Drag corner to resize"]]

       (if size
         [:div.space-y-3
          [:div.grid.grid-cols-2.gap-4
           [:div.ty-content.p-4.rounded-lg.text-center
            [:div.text-sm.ty-text-.mb-1 "Width"]
            [:div.text-2xl.font-bold.ty-text-primary (str (int (:width size)) "px")]]
           [:div.ty-content.p-4.rounded-lg.text-center
            [:div.text-sm.ty-text-.mb-1 "Height"]
            [:div.text-2xl.font-bold.ty-text-success (str (int (:height size)) "px")]]]]
         [:div.ty-text- "Initializing..."])])]])

(defn responsive-layout-section
  "Example showing layout changes based on container width"
  []
  [:div.ty-elevated.rounded-lg.p-6
   [:h3.text-xl.font-semibold.ty-text+.mb-4 "📱 Responsive Layout"]
   [:p.ty-text-.mb-4 "Automatically switch between mobile/desktop layouts based on container width (not window width):"]

   [:ty-resize-observer#responsive-layout.border-2.ty-border-primary.rounded-lg
    {:style {:resize "horizontal"
             :overflow "auto"
             :padding "1rem"
             :min-width "200px"
             :max-width "100%"}
     :replicant/on-mount
     (fn [{^js el :replicant/node}]
       (when js/window.tyResizeObserver
         (let [unsubscribe (js/window.tyResizeObserver.onResize
                             "responsive-layout"
                             (fn [size]
                               (swap! state/state assoc-in [:element-sizes "responsive-layout"]
                                      {:width (.-width size)
                                       :height (.-height size)})))]
           (set! (.-_resizeUnsub el) unsubscribe))))

     :replicant/on-unmount
     (fn [{^js el :replicant/node}]
       (when-let [unsubscribe (.-_resizeUnsub el)]
         (unsubscribe)
         (set! (.-_resizeUnsub el) nil))
       (swap! state/state update :element-sizes dissoc "responsive-layout"))}

    (let [size (get-in @state/state [:element-sizes "responsive-layout"])
          width (:width size 400)
          is-mobile (< width 600)]
      [:div
       [:div.mb-4.text-center
        [:span.ty-text.font-semibold
         "Current: "
         [:span {:class (if is-mobile "ty-text-warning++" "ty-text-success++")}
          (if is-mobile "Mobile" "Desktop")
          " Layout"]
         " (" (int width) "px)"]]

       (if is-mobile
         ;; Mobile layout: vertical stack
         [:div.space-y-3
          [:div.ty-content.p-4.rounded-lg
           [:div.font-semibold.ty-text+ "Card 1"]
           [:p.ty-text-.text-sm "Full width mobile card"]]
          [:div.ty-content.p-4.rounded-lg
           [:div.font-semibold.ty-text+ "Card 2"]
           [:p.ty-text-.text-sm "Full width mobile card"]]
          [:div.ty-content.p-4.rounded-lg
           [:div.font-semibold.ty-text+ "Card 3"]
           [:p.ty-text-.text-sm "Full width mobile card"]]]

         ;; Desktop layout: horizontal grid
         [:div.grid.grid-cols-3.gap-4
          [:div.ty-content.p-4.rounded-lg
           [:div.font-semibold.ty-text+ "Card 1"]
           [:p.ty-text-.text-sm "Desktop grid card"]]
          [:div.ty-content.p-4.rounded-lg
           [:div.font-semibold.ty-text+ "Card 2"]
           [:p.ty-text-.text-sm "Desktop grid card"]]
          [:div.ty-content.p-4.rounded-lg
           [:div.font-semibold.ty-text+ "Card 3"]
           [:p.ty-text-.text-sm "Desktop grid card"]]])])]

   [:div.mt-4.ty-bg-neutral-.rounded.p-3
    [:p.ty-text-.text-sm
     [:strong "Try it:"] " Drag the right edge to resize. Layout switches at 600px breakpoint."]]])

(defn debounced-performance-section
  "Example showing debounce benefit for expensive operations"
  []
  [:div.ty-content.rounded-lg.p-6
   [:h3.text-xl.font-semibold.ty-text+.mb-4 "⚡ Debounced Performance"]
   [:p.ty-text-.mb-4 "Reduce update frequency for expensive operations like API calls or complex calculations:"]

   [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
    ;; No debounce
    [:div
     [:h4.font-semibold.ty-text.mb-2 "Without Debounce"]
     [:ty-resize-observer#no-debounce.ty-border-danger.border-2.rounded-lg
      {:style {:resize "both"
               :overflow "auto"
               :padding "1rem"
               :min-width "150px"
               :min-height "100px"}
       :replicant/on-mount
       (fn [{^js el :replicant/node}]
         (when js/window.tyResizeObserver
           (let [unsubscribe (js/window.tyResizeObserver.onResize
                               "no-debounce"
                               (fn [size]
                                 (swap! state/state
                                        (fn [s]
                                          (-> s
                                              (assoc-in [:element-sizes "no-debounce"]
                                                        {:width (.-width size)
                                                         :height (.-height size)})
                                              (update-in [:update-counts "no-debounce"] (fnil inc 0)))))))]
             (set! (.-_resizeUnsub el) unsubscribe))))

       :replicant/on-unmount
       (fn [{^js el :replicant/node}]
         (when-let [unsubscribe (.-_resizeUnsub el)]
           (unsubscribe)
           (set! (.-_resizeUnsub el) nil))
         (swap! state/state update :element-sizes dissoc "no-debounce")
         (swap! state/state update :update-counts dissoc "no-debounce"))}

      (let [count (get-in @state/state [:update-counts "no-debounce"] 0)]
        [:div
         [:div.ty-text.text-sm.mb-2 "Updates: " [:span.font-bold.ty-text-danger++ count]]
         [:div.ty-text-.text-xs "Every pixel fires!"]])]]

    ;; With debounce
    [:div
     [:h4.font-semibold.ty-text.mb-2 "With 300ms Debounce"]
     [:ty-resize-observer#with-debounce.ty-border-success.rounded-lg.border-2
      {:debounce 300
       :style {:resize "both"
               :overflow "auto"
               :padding "1rem"
               :min-width "150px"
               :min-height "100px"}
       :replicant/on-mount
       (fn [{^js el :replicant/node}]
         (when js/window.tyResizeObserver
           (let [unsubscribe (js/window.tyResizeObserver.onResize
                               "with-debounce"
                               (fn [size]
                                 (swap! state/state
                                        (fn [s]
                                          (-> s
                                              (assoc-in [:element-sizes "with-debounce"]
                                                        {:width (.-width size)
                                                         :height (.-height size)})
                                              (update-in [:update-counts "with-debounce"] (fnil inc 0)))))))]
             (set! (.-_resizeUnsub el) unsubscribe))))

       :replicant/on-unmount
       (fn [{^js el :replicant/node}]
         (when-let [unsubscribe (.-_resizeUnsub el)]
           (unsubscribe)
           (set! (.-_resizeUnsub el) nil))
         (swap! state/state update :element-sizes dissoc "with-debounce")
         (swap! state/state update :update-counts dissoc "with-debounce"))}

      (let [count (get-in @state/state [:update-counts "with-debounce"] 0)]
        [:div
         [:div.ty-text.text-sm.mb-2 "Updates: " [:span.font-bold.ty-text-success++ count]]
         [:div.ty-text-.text-xs "Only after 300ms pause"]])]]]

   [:div.mt-4.ty-bg-neutral-.rounded.p-3
    [:p.ty-text-.text-sm
     [:strong "Try it:"] " Resize both containers and watch the update counts. Debouncing dramatically reduces unnecessary updates!"]]

   [:div.mt-4
    (code-block
      "<!-- Perfect for expensive operations -->
<ty-resize-observer id=\"chart\" debounce=\"300\">
  <canvas id=\"my-chart\"></canvas>
</ty-resize-observer>

<script>
  onResize('chart', ({ width, height }) => {
    // Expensive: Re-render chart, make API call, etc.
    rechartCanvas(width, height);
  });
</script>")]])

(defn nested-observers-section
  "Example showing multiple nested resize observers"
  []
  [:div.ty-elevated.rounded-lg.p-6
   [:h3.text-xl.font-semibold.ty-text+.mb-4 "🔗 Nested Observers"]
   [:p.ty-text-.mb-4 "Track multiple container sizes independently - useful for complex layouts:"]

   [:ty-resize-observer#outer-container.ty-border-primary.border-2.rounded-lg
    {:style {:resize "both"
             :overflow "auto"
             :padding "1rem"
             :min-width "300px"
             :min-height "200px"}
     :replicant/on-mount
     (fn [{^js el :replicant/node}]
       (when js/window.tyResizeObserver
         (let [unsubscribe (js/window.tyResizeObserver.onResize
                             "outer-container"
                             (fn [size]
                               (swap! state/state assoc-in [:element-sizes "outer-container"]
                                      {:width (.-width size)
                                       :height (.-height size)})))]
           (set! (.-_resizeUnsub el) unsubscribe))))

     :replicant/on-unmount
     (fn [{^js el :replicant/node}]
       (when-let [unsubscribe (.-_resizeUnsub el)]
         (unsubscribe)
         (set! (.-_resizeUnsub el) nil))
       (swap! state/state update :element-sizes dissoc "outer-container"))}

    (let [outer-size (get-in @state/state [:element-sizes "outer-container"])]
      [:div
       [:div.font-semibold.ty-text.mb-3
        "Outer Container: "
        (when outer-size
          [:span.ty-text-primary (str (int (:width outer-size)) " × " (int (:height outer-size)) "px")])]

       [:ty-resize-observer#inner-container.border-2.ty-border-success.rounded.border-dashed
        {:style {:resize "both"
                 :overflow "auto"
                 :padding "0.75rem"
                 :min-width "150px"
                 :min-height "100px"
                 :margin-top "0.5rem"}
         :replicant/on-mount
         (fn [{^js el :replicant/node}]
           (when js/window.tyResizeObserver
             (let [unsubscribe (js/window.tyResizeObserver.onResize
                                 "inner-container"
                                 (fn [size]
                                   (swap! state/state assoc-in [:element-sizes "inner-container"]
                                          {:width (.-width size)
                                           :height (.-height size)})))]
               (set! (.-_resizeUnsub el) unsubscribe))))

         :replicant/on-unmount
         (fn [{^js el :replicant/node}]
           (when-let [unsubscribe (.-_resizeUnsub el)]
             (unsubscribe)
             (set! (.-_resizeUnsub el) nil))
           (swap! state/state update :element-sizes dissoc "inner-container"))}

        (let [inner-size (get-in @state/state [:element-sizes "inner-container"])
              outer-size (get-in @state/state [:element-sizes "outer-container"])
              ratio (when (and inner-size outer-size (:width outer-size) (> (:width outer-size) 0))
                      (* 100 (/ (:width inner-size) (:width outer-size))))]
          [:div
           [:div.font-semibold.ty-text.text-sm.mb-2
            "Inner Container: "
            (when inner-size
              [:span.ty-text-success (str (int (:width inner-size)) " × " (int (:height inner-size)) "px")])]

           (when ratio
             [:div.ty-text-.text-xs
              "Takes up " [:span.font-bold (int ratio) "%"] " of outer width"])])]])]

   [:div.mt-4.ty-bg-neutral-.rounded.p-3
    [:p.ty-text-.text-sm
     [:strong "Use case:"] " Dashboard with resizable panels, split-pane editors, or nested modals."]]

   [:div.mt-4
    (code-block
      "<ty-resize-observer id=\"sidebar\">
  <aside>
    <ty-resize-observer id=\"sidebar-section\">
      <!-- Each section tracks its own size -->
    </ty-resize-observer>
  </aside>
</ty-resize-observer>

<script>
  // Independent tracking
  onResize('sidebar', (size) => updateSidebarLayout(size));
  onResize('sidebar-section', (size) => updateSectionLayout(size));
</script>")]])

(defn flexbox-dual-observer-section
  "Flexbox layout with two independent resize observers"
  []
  [:div.ty-elevated.rounded-lg.p-6
   [:h3.text-xl.font-semibold.ty-text+.mb-4 "📐 Flexbox Layout with Dual Observers"]
   [:p.ty-text-.mb-4 "Both panels use independent resize observers for their own layout context. Perfect for split-pane layouts:"]

   [:div.flex.gap-4 {:style {:height "320px"}}
    ;; Sidebar with resize observer
    [:ty-resize-observer#flex-sidebar.border-2.ty-bg-secondary-.ty-border-secondary.rounded-lg
     {:style {:resize "horizontal"
              :overflow "auto"
              :padding "1rem"
              :min-width "150px"
              :width "200px"}
      :replicant/on-mount
      (fn [{^js el :replicant/node}]
        (when js/window.tyResizeObserver
          (let [unsubscribe (js/window.tyResizeObserver.onResize
                              "flex-sidebar"
                              (fn [size]
                                (swap! state/state assoc-in [:element-sizes "flex-sidebar"]
                                       {:width (.-width size)
                                        :height (.-height size)})))]
            (set! (.-_resizeUnsub el) unsubscribe))))

      :replicant/on-unmount
      (fn [{^js el :replicant/node}]
        (when-let [unsubscribe (.-_resizeUnsub el)]
          (unsubscribe)
          (set! (.-_resizeUnsub el) nil))
        (swap! state/state update :element-sizes dissoc "flex-sidebar"))}

     (let [size (get-in @state/state [:element-sizes "flex-sidebar"])
           width (:width size 0)
           is-compact (< width 180)]
       [:div.space-y-3
        [:h4.font-semibold.text-center.ty-text+
         "Sidebar"]

        [:div.ty-elevated.rounded.p-2.text-xs.space-y-1
         [:div
          [:span.ty-text+ "Width: "]
          [:span.font-mono.ty-text (if size (str (int width) "px") "nil")]]
         [:div
          [:span.ty-text+ "Height: "]
          [:span.font-mono.ty-text (if size (str (int (:height size)) "px") "nil")]]]

        [:div.text-center.text-sm
         (if is-compact
           [:div.ty-text-warning "📱 Compact"]
           [:div.ty-text-success "🎯 Full content"])]])]

    ;; Main content with resize observer
    [:ty-resize-observer#flex-main.ty-bg-success-.ty-border-success.rounded-lg.border-2.p-4.flex-1
     {:replicant/on-mount
      (fn [{^js el :replicant/node}]
        (when js/window.tyResizeObserver
          (let [unsubscribe (js/window.tyResizeObserver.onResize
                              "flex-main"
                              (fn [size]
                                (swap! state/state assoc-in [:element-sizes "flex-main"]
                                       {:width (.-width size)
                                        :height (.-height size)})))]
            (set! (.-_resizeUnsub el) unsubscribe))))

      :replicant/on-unmount
      (fn [{^js el :replicant/node}]
        (when-let [unsubscribe (.-_resizeUnsub el)]
          (unsubscribe)
          (set! (.-_resizeUnsub el) nil))
        (swap! state/state update :element-sizes dissoc "flex-main"))}

     (let [size (get-in @state/state [:element-sizes "flex-main"])
           width (:width size 0)]
       [:div.space-y-3
        [:h4.font-semibold.text-center.ty-text+
         "Main Content"]

        [:div.ty-elevated.rounded.p-3.space-y-1.text-sm
         [:div
          [:span.ty-text+ "Width: "]
          [:span.font-mono.ty-text (if size (str (int width) "px") "nil")]]
         [:div
          [:span.ty-text+ "Height: "]
          [:span.font-mono.ty-text (if size (str (int (:height size)) "px") "nil")]]
         [:div
          [:span.ty-text+ "Layout: "]
          [:span.font-mono.ty-text
           (cond
             (>= width 768) "desktop"
             (>= width 480) "tablet"
             :else "mobile")]]]

        [:div.text-center
         (cond
           (>= width 768) [:div.ty-text-success "🖥️ Desktop Layout"]
           (>= width 480) [:div.ty-text-primary "💻 Tablet Layout"]
           :else [:div.ty-text-secondary "📱 Mobile Layout"])]])]]

   [:div.mt-4.ty-bg-neutral-.rounded.p-3
    [:p.ty-text-.text-sm
     [:strong "Try it:"] " Drag the sidebar's right edge to resize. Each panel independently tracks its own dimensions and adapts its layout accordingly."]]

   [:div.mt-4
    (code-block
      "<div class=\"flex gap-4\">
  <!-- Sidebar with its own observer -->
  <ty-resize-observer id=\"sidebar\">
    <aside>Sidebar content</aside>
  </ty-resize-observer>
  
  <!-- Main content with its own observer -->
  <ty-resize-observer id=\"main\">
    <main>Main content</main>
  </ty-resize-observer>
</div>

<script type=\"module\">
  import { onResize } from '@gersak/ty';
  
  // Independent subscriptions
  onResize('sidebar', ({ width }) => {
    // Sidebar adapts to its width
    document.querySelector('aside').classList.toggle('compact', width < 180);
  });
  
  onResize('main', ({ width }) => {
    // Main content uses different breakpoints
    const layout = width >= 768 ? 'desktop' : width >= 480 ? 'tablet' : 'mobile';
    document.querySelector('main').dataset.layout = layout;
  });
</script>")]])

(defn view []
  (docs-page
   ;; Title and Description
   (header-section)

   ;; API Reference Card
   (api-reference-section)

   ;; Examples Section
   [:h2.text-2xl.font-semibold.ty-text++.mb-6 "Examples"]

   [:div.space-y-8
    ;; Quick Start
    (quick-start-section)

    ;; Interactive Demo
    (interactive-demo-section)

    ;; Responsive Layout
    (responsive-layout-section)

    ;; Debounced Performance
    (debounced-performance-section)

    ;; Nested Observers
    (nested-observers-section)

    ;; Flexbox Dual Observers
    (flexbox-dual-observer-section)

    ;; Callback Subscription (keep existing)
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-xl.font-semibold.ty-text+.mb-4 "Reactive Updates"]
     [:p.ty-text-.mb-4 "Subscribe to size changes with callbacks:"]

     (code-block
       "<ty-resize-observer id=\"reactive-container\">
  <div id=\"display\"></div>
</ty-resize-observer>

<script type=\"module\">
  import { onResize } from '@gersak/ty';
  
  const unsubscribe = onResize('reactive-container', ({ width, height }) => {
    document.getElementById('display').textContent = 
      `Size: ${width}px × ${height}px`;
  });
  
  // Later: unsubscribe();
</script>")]

    ;; Debounced (simple code example)
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-xl.font-semibold.ty-text+.mb-4 "Window API (CDN)"]
     [:p.ty-text-.mb-4 "Use the global window API for script tag integration:"]

     (code-block
       "<ty-resize-observer id=\"container\">
  <div>Content</div>
</ty-resize-observer>

<script>
  // Query size
  const size = window.tyResizeObserver.getSize('container');
  
  // Subscribe to changes
  const unsubscribe = window.tyResizeObserver.onResize('container', (size) => {
    console.log('Resized:', size);
  });
  
  // Get all sizes (debugging)
  console.log(window.tyResizeObserver.sizes);
</script>")]]

   ;; Framework Integration
   [:h2.text-2xl.font-semibold.ty-text++.my-6 "Framework Integration"]

   [:div.grid.grid-cols-1.gap-6
    ;; React
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "React"]
     (code-block
       "import { useEffect, useState } from 'react';
import { getSize, onResize } from '@gersak/ty';

function ChildComponent() {
  const [parentSize, setParentSize] = useState(() => getSize('parent'));
  
  useEffect(() => {
    const unsubscribe = onResize('parent', (size) => {
      setParentSize(size);
    });
    
    return unsubscribe;
  }, []);
  
  return <div>Parent: {parentSize?.width}px wide</div>;
}"
       "javascript")]

    ;; ClojureScript
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "ClojureScript / Replicant"]
     [:p.ty-text-.mb-3 "Using Replicant lifecycle hooks with global state:"]
     (code-block
       "(ns my-app.docs
  (:require [ty.site.state :as state]))

(defn interactive-demo []
  [:ty-resize-observer#my-container
   {:style {:resize \"both\" :overflow \"auto\" :min-width \"200px\"}
    
    ;; Subscribe on mount
    :replicant/on-mount
    (fn [{^js el :replicant/node}]
      (when js/window.tyResizeObserver
        (let [unsubscribe (js/window.tyResizeObserver.onResize
                           \"my-container\"
                           (fn [size]
                             ;; Update global state on resize
                             (swap! state/state assoc-in [:element-sizes \"my-container\"]
                                    {:width (.-width size)
                                     :height (.-height size)})))]
          ;; Store unsubscribe for cleanup
          (set! (.-_resizeUnsub el) unsubscribe))))
    
    ;; Cleanup on unmount
    :replicant/on-unmount
    (fn [{^js el :replicant/node}]
      (when-let [unsubscribe (.-_resizeUnsub el)]
        (unsubscribe)
        (set! (.-_resizeUnsub el) nil))
      (swap! state/state update :element-sizes dissoc \"my-container\"))}
   
   ;; Read from state - re-renders automatically when size changes
   (let [size (get-in @state/state [:element-sizes \"my-container\"])]
     [:div 
      (str \"Size: \" (:width size) \"px × \" (:height size) \"px\")])])"
       "clojure")]]

   ;; Best Practices
   [:div.ty-elevated.rounded-lg.p-6.my-8
    [:h2.text-2xl.font-semibold.ty-text++.mb-4 "Best Practices"]

    [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
     ;; Do's
     [:div
      [:h3.text-lg.font-semibold.ty-text-success++.mb-3.flex.items-center
       [:ty-icon.mr-2.ty-text-success {:name "check-circle"
                                       :size "20"}]
       "Do's"]
      [:ul.space-y-2.ty-text
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-success {:name "check"
                                               :size "16"}]
        [:span "Always provide a unique ID attribute"]]
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-success {:name "check"
                                               :size "16"}]
        [:span "Use debounce for expensive operations (layout recalculations, API calls)"]]
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-success {:name "check"
                                               :size "16"}]
        [:span "Unsubscribe from callbacks when component unmounts"]]
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-success {:name "check"
                                               :size "16"}]
        [:span "Use CSS Container Queries for pure layout changes when possible"]]]]

     ;; Don'ts
     [:div
      [:h3.text-lg.font-semibold.ty-text-danger++.mb-3.flex.items-center
       [:ty-icon.mr-2.ty-text-danger {:name "x-circle"
                                      :size "20"}]
       "Don'ts"]
      [:ul.space-y-2.ty-text
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-danger {:name "x"
                                              :size "16"}]
        [:span "Don't forget to unsubscribe - memory leaks!"]]
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-danger {:name "x"
                                              :size "16"}]
        [:span "Don't use without an ID - it won't register"]]
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-danger {:name "x"
                                              :size "16"}]
        [:span "Don't poll getSize() in a tight loop - use callbacks"]]
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-danger {:name "x"
                                              :size "16"}]
        [:span "Don't use for simple responsive design - prefer CSS media queries"]]]]]]))

