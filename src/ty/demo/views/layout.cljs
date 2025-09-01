(ns ty.demo.views.layout
  "Demonstrates the layout context system"
  (:require
    [ty.context :refer [*element-sizes*]]
    [ty.demo.state :refer [state]]
    [ty.layout :as layout]))

(defn container-info-display []
  (let [{:keys [width height breakpoint orientation density scrollbar-width]}
        (layout/current-container)]
    [:div.ty-content.p-4.rounded-lg.space-y-2
     [:h3.text-lg.font-semibold "Current Container Context"]
     [:div.grid.grid-cols-2.gap-4.text-sm
      [:div
       [:span.font-medium "Width: "]
       [:span (or width "not set") "px"]]
      [:div
       [:span.font-medium "Height: "]
       [:span (or height "not set") "px"]]
      [:div
       [:span.font-medium "Breakpoint: "]
       [:span.px-2.py-1.ty-bg-primary-.rounded.text-xs
        (when breakpoint (name breakpoint))]]
      [:div
       [:span.font-medium "Orientation: "]
       [:span.px-2.py-1.ty-bg-success-.rounded.text-xs
        (when orientation (name orientation))]]
      [:div
       [:span.font-medium "Density: "]
       [:span.px-2.py-1.ty-bg-secondary-.rounded.text-xs
        (when density (name density))]]
      [:div
       [:span.font-medium "Aspect Ratio: "]
       [:span (when-let [ratio (layout/aspect-ratio)]
                (.toFixed ratio 2))]]]]))

(defn breakpoint-helpers-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Breakpoint Helpers"]
   [:div.space-y-2
    [:div.flex.items-center.gap-2
     [:span.font-medium "breakpoint>= :md"]
     [:span.px-2.py-1.rounded.text-xs
      {:class (if (layout/breakpoint>= :md)
                [:ty-bg-success :text-white]
                [:ty-bg-neutral :ty-text- :ty-bg-neutral :ty-text-])}
      (str (layout/breakpoint>= :md))]]
    [:div.flex.items-center.gap-2
     [:span.font-medium "breakpoint<= :lg"]
     [:span.px-2.py-1.rounded.text-xs
      {:class (if (layout/breakpoint<= :lg)
                [:ty-bg-success :text-white]
                [:ty-bg-neutral :ty-text- :ty-bg-neutral :ty-text-])}
      (str (layout/breakpoint<= :lg))]]
    [:div.flex.items-center.gap-2
     [:span.font-medium "portrait?"]
     [:span.px-2.py-1.rounded.text-xs
      {:class (if (layout/portrait?)
                [:ty-bg-success :text-white]
                [:ty-bg-neutral :ty-text- :ty-bg-neutral :ty-text-])}
      (str (layout/portrait?))]]
    [:div.flex.items-center.gap-2
     [:span.font-medium "landscape?"]
     [:span.px-2.py-1.rounded.text-xs
      {:class (if (layout/landscape?)
                [:ty-bg-success :text-white]
                [:ty-bg-neutral :ty-text- :ty-bg-neutral :ty-text-])}
      (str (layout/landscape?))]]]])

(defn responsive-grid-demo []
  (let [columns (layout/grid-columns)
        gap (layout/grid-gap)]
    [:div.space-y-4
     [:h3.text-lg.font-semibold "Responsive Grid"]
     [:div.text-sm.ty-text-
      (str "Columns: " columns ", Gap: " gap "px", "Container Width: " (layout/container-width))]
     [:div.grid
      {;:class [(str "grid-cols-" columns)]
       :style {:gap (str gap "px")
               :grid-template-columns (str "repeat(" columns ", minmax(0, 1fr))")}}
      (for [i (range columns)]
        [:div.ty-bg-primary-.p-4.rounded.text-center
         {:key i}
         (inc i)])]]))

(defn nested-container-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Nested Containers"]
   [:div.border-2.border-dashed.ty-border-primary.p-4.rounded
    [:div.mb-4
     [:span.font-medium "Outer Container: "]
     [:span (layout/container-info)]]

    ;; Nested container with 80% dimensions
    (layout/with-container {:width (layout/width% 80)
                            :height (layout/height% 80)}
      [:div.border-2.border-dashed.ty-border-success.p-4.rounded
       [:div.mb-4
        [:span.font-medium "Inner Container (80%): "]
        [:span (layout/container-info)]]

       ;; Double nested with 50% dimensions
       (layout/with-container {:width (layout/width% 50)
                               :height (layout/height% 50)}
         [:div.border-2.border-dashed.ty-border-secondary.p-4.rounded
          [:div
           [:span.font-medium "Nested Container (50%): "]
           [:span (layout/container-info)]]])])]])

(defn responsive-value-demo []
  (let [font-size (layout/responsive-value {:xs "12px"
                                            :sm "14px"
                                            :md "16px"
                                            :lg "18px"
                                            :xl "20px"})
        padding (layout/responsive-value {:xs 8
                                          :sm 12
                                          :md 16
                                          :lg 20
                                          :xl 24})]
    [:div.space-y-4
     [:h3.text-lg.font-semibold "Responsive Values"]
     [:div.border.rounded
      {:style {:font-size font-size
               :padding (str padding "px")}}
      [:p "This text and padding adapt to the container size."]
      [:p.text-sm.ty-text-.mt-2
       (str "Font: " font-size ", Padding: " padding "px")]]]))

(defn aspect-ratio-demo []
  (let [{:keys [width height]} (layout/maintain-aspect-ratio (/ 16 9))]
    [:div.space-y-4
     [:h3.text-lg.font-semibold "Aspect Ratio Helper"]
     [:div.ty-content.rounded.flex.items-center.justify-center
      {:style {:width (str width "px")
               :height (str height "px")}}
      [:div.text-center
       [:div "16:9 Aspect Ratio"]
       [:div.text-sm.ty-text-
        (str (.toFixed width 0) " x " (.toFixed height 0))]]]]))

(defn container-percentage-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Container Percentage Helpers"]
   [:div.space-y-2
    [:div.ty-bg-primary-.rounded.p-2
     {:style {:width (str (layout/width% 75) "px")}}
     "75% of container width"]
    [:div.ty-bg-success-.rounded.p-2
     {:style {:width (str (layout/width% 50) "px")}}
     "50% of container width"]
    [:div.ty-bg-secondary-.rounded.p-2
     {:style {:width (str (layout/width% 25) "px")}}
     "25% of container width"]]])

;; ===== RESIZE OBSERVER DEMOS =====

(defn resize-observer-basic-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Basic Resize Observer"]
   [:p.text-sm.ty-text- "Drag the bottom-right corner to resize this panel."]
   [:ty-resize-observer#basic-demo.ty-bg-primary-.border-2.ty-border-primary.rounded-lg.p-4.resize.overflow-auto
    {:style {:min-width "200px"
             :min-height "150px"
             :width "300px"
             :height "200px"}}
    (layout/with-resize-observer "basic-demo"
      [:div.space-y-3
       [:div.text-center
        [:h4.font-semibold "Resizable Panel"]
        [:p.text-sm.ty-text- "This content is aware of its container size"]]
       [:div.ty-elevated.rounded.p-3
        [:div.text-sm
         [:div [:span.font-medium "Width: "] [:span.font-mono (layout/container-width) "px"]]
         [:div [:span.font-medium "Height: "] [:span.font-mono (layout/container-height) "px"]]
         [:div [:span.font-medium "Breakpoint: "] [:span.font-mono (name (layout/container-breakpoint))]]
         [:div [:span.font-medium "Orientation: "] [:span.font-mono (name (layout/container-orientation))]]]]
       [:div.text-center
        (if (layout/breakpoint>= :md)
          [:div.ty-text-success "✓ Wide enough for desktop layout"]
          [:div.text-orange-600 "⚠ Using compact mobile layout"])]])]])

(defn resize-observer-responsive-grid []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Container-Aware Responsive Grid"]
   [:p.text-sm.ty-text- "This grid adapts based on the container size, not window size."]
   [:ty-resize-observer#grid-demo.ty-content.border-2.ty-border.rounded-lg.p-4.resize.overflow-auto
    {:style {:min-width "200px"
             :min-height "200px"
             :width "400px"
             :height "250px"}}
    (layout/with-resize-observer "grid-demo"
      (let [columns (cond
                      (layout/breakpoint>= :lg) 4
                      (layout/breakpoint>= :md) 3
                      (layout/breakpoint>= :sm) 2
                      :else 1)]
        [:div.space-y-3
         [:div.text-center
          [:div.text-sm [:span.font-medium "Grid columns: "] [:span.font-mono columns]]
          [:div.text-xs.ty-text-- "Based on container breakpoint: " (name (layout/container-breakpoint))]]
         [:div.grid.gap-2
          {:style {:grid-template-columns (str "repeat(" columns ", minmax(0, 1fr))")}}
          (for [i (range 8)]
            [:div.ty-elevated.rounded.p-2.text-center.text-sm
             {:key i}
             (str "Item " (inc i))])]]))]])

(defn resize-observer-flexbox-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Flexbox Layout with Resize Observer"]
   [:p.text-sm.ty-text- "Both panels use resize observers for layout context."]
   [:div.flex.gap-4.h-80
    [:ty-resize-observer#flex-sidebar.ty-bg-secondary-.border-2.ty-border-secondary.rounded-lg.p-4.resize.overflow-auto
     {:style {:min-width "150px"
              :width "200px"}}
     (layout/with-resize-observer "flex-sidebar"
       [:div.space-y-3
        [:h4.font-semibold.text-center "Sidebar"]
        [:div.ty-elevated.rounded.p-2.text-xs
         [:div "Width: " [:span.font-mono (layout/container-width) "px"]]
         [:div "Height: " [:span.font-mono (layout/container-height) "px"]]]
        [:div.text-center.text-sm
         (if (> (layout/container-width) 180)
           [:div "🎯 Full content"]
           [:div "📱 Compact"])]])]
    [:ty-resize-observer#flex-main.ty-bg-success-.border-2.ty-border-success.rounded-lg.p-4.flex-1
     (layout/with-resize-observer "flex-main"
       [:div.space-y-3
        [:h4.font-semibold.text-center "Main Content"]
        [:div.ty-elevated.rounded.p-3
         [:div.text-sm.space-y-1
          [:div "Width: " [:span.font-mono (layout/container-width) "px"]]
          [:div "Height: " [:span.font-mono (layout/container-height) "px"]]
          [:div "Breakpoint: " [:span.font-mono (name (layout/container-breakpoint))]]]]
        [:div.text-center
         (cond
           (layout/breakpoint>= :lg) [:div.ty-text-success "🖥️ Desktop Layout"]
           (layout/breakpoint>= :md) [:div.ty-text-primary "💻 Tablet Layout"]
           :else [:div.ty-text-secondary "📱 Mobile Layout"])]])]]])

(defn resize-observer-nested-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Nested Resize Observers"]
   [:p.text-sm.ty-text- "Nested containers, each with their own resize observer."]
   [:ty-resize-observer#outer-container.ty-bg-primary-.border-2.ty-border-primary.rounded-lg.p-4.resize.overflow-auto
    {:style {:min-width "300px"
             :min-height "200px"
             :width "500px"
             :height "300px"}}
    (layout/with-resize-observer "outer-container"
      [:div.space-y-3
       [:div.text-center
        [:h4.font-semibold "Outer Container"]
        [:div.text-sm "Dimensions: " [:span.font-mono (layout/container-width) "×" (layout/container-height)]]]
       [:ty-resize-observer#inner-container.ty-elevated.border-2.ty-border.rounded.p-3.resize.overflow-auto
        {:style {:min-width "150px"
                 :min-height "100px"
                 :width "60%"
                 :height "60%"}}
        (layout/with-resize-observer "inner-container"
          [:div.space-y-2
           [:div.text-center
            [:h5.font-medium "Inner Container"]
            [:div.text-sm "Dimensions: " [:span.font-mono (layout/container-width) "×" (layout/container-height)]]]
           [:div.text-xs.text-center
            (if (and (> (layout/container-width) 200) (> (layout/container-height) 120))
              [:div.ty-text-success "✓ Comfortable size"]
              [:div.ty-text-danger "⚠ Too small"])]])]])]])

(defn resize-observer-api-demo []
  (let [basic-size (layout/get-element-size "basic-demo")
        grid-size (layout/get-element-size "grid-demo")
        sidebar-size (layout/get-element-size "flex-sidebar")
        main-size (layout/get-element-size "flex-main")]
    [:div.space-y-4
     [:h3.text-lg.font-semibold "Resize Observer API"]
     [:p.text-sm.ty-text- "Direct access to element sizes from anywhere in the application."]
     [:div.ty-content.rounded-lg.p-4
      [:div.space-y-3
       [:div
        [:h4.font-medium.mb-2 "Current Element Sizes"]
        [:div.text-sm.space-y-1.font-mono
         [:div "basic-demo: " (if basic-size (str (:width basic-size) "×" (:height basic-size)) "not available")]
         [:div "grid-demo: " (if grid-size (str (:width grid-size) "×" (:height grid-size)) "not available")]
         [:div "flex-sidebar: " (if sidebar-size (str (:width sidebar-size) "×" (:height sidebar-size)) "not available")]
         [:div "flex-main: " (if main-size (str (:width main-size) "×" (:height main-size)) "not available")]]]
       [:div
        [:h4.font-medium.mb-2 "Registry Contents"]
        [:div.text-xs.font-mono.ty-text-
         (str (count @*element-sizes*) " elements being tracked")]]
       [:div
        [:h4.font-medium.mb-2 "Usage Examples"]
        [:div.text-xs.space-y-1.font-mono.ty-elevated.p-2.rounded
         [:div ";; Direct size access"]
         [:div "(layout/get-element-size \"basic-demo\")"]
         [:div]
         [:div ";; Layout context binding"]
         [:div "(layout/with-resize-observer \"grid-demo\""]
         [:div "  (my-component))"]
         [:div]
         [:div ";; Watch for changes"]
         [:div "(add-watch resize-observer/element-sizes ::my-key ...)"]]]]]]))

(defn window-tracking-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Window Dimensions (Live)"]
   [:div.text-sm
    (let [{:keys [width height]} @layout/window-size]
      [:div.space-y-1
       [:div "Window: " [:span.font-mono width " x " height] "px"]
       [:div.text-xs.ty-text-- "Resize the window to see updates"]])]])

(defn window-vs-container-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Window vs Container"]
   [:div.text-sm.space-y-2
    [:div
     [:span.font-medium "Window: "]
     (let [{:keys [width height]} @layout/window-size]
       [:span.font-mono width " x " height])]
    [:div
     [:span.font-medium "Container: "]
     [:span.font-mono (layout/container-info)]]
    [:div.text-xs.ty-text--
     "The container context is bound from window dimensions"]]])

(defn view []
  ;; Use the with-window macro instead of manual tracking
  [:ty-resize-observer#layout-core-features
   (layout/with-resize-observer "layout-core-features"
     [:div.p-8.max-w-6xl.mx-auto.space-y-8.ty-text
      [:div
       [:h1.text-3xl.font-bold.mb-4 "Layout Context System"]
       [:p.ty-text-
        "Dynamic layout context using Clojure's dynamic vars for responsive components."]
       [:p.text-sm.ty-text--.mt-2
        "Using the " [:code "with-window"] " macro for automatic dimension tracking."]]

      [:div.ty-elevated.rounded-lg.shadow-md.p-6
       (container-info-display)]

      [:div.ty-elevated.rounded-lg.shadow-md.p-6
       (window-tracking-demo)]

      [:div.ty-elevated.rounded-lg.shadow-md.p-6
       (window-vs-container-demo)]

      [:div.ty-elevated.rounded-lg.shadow-md.p-6
       (breakpoint-helpers-demo)]

      ;; ===== RESIZE OBSERVER SECTION =====
      [:div
       [:h2.text-2xl.font-bold.mb-4.ty-text-primary "Resize Observer Components"]
       [:p.ty-text-.mb-6
        "Self-observing components that integrate seamlessly with the layout context system."]]

      [:div.ty-elevated.rounded-lg.shadow-md.p-6
       (resize-observer-basic-demo)]

      [:div.ty-elevated.rounded-lg.shadow-md.p-6
       (resize-observer-responsive-grid)]

      [:div.ty-elevated.rounded-lg.shadow-md.p-6
       (resize-observer-flexbox-demo)]

      [:div.ty-elevated.rounded-lg.shadow-md.p-6
       (resize-observer-nested-demo)]

      [:div.ty-elevated.rounded-lg.shadow-md.p-6
       (resize-observer-api-demo)]

      ;; ===== ORIGINAL LAYOUT DEMOS =====
      [:div
       [:h2.text-2xl.font-bold.mb-4.ty-text-success.dark:text-green-400 "Core Layout Features"]
       [:p.ty-text-.mb-6
        "Built-in layout context features and helpers."]]

      [:div.ty-elevated.rounded-lg.shadow-md.p-6
       (responsive-grid-demo)]

      [:div.ty-elevated.rounded-lg.shadow-md.p-6
       (nested-container-demo)]

      [:div.ty-elevated.rounded-lg.shadow-md.p-6
       (responsive-value-demo)]

      [:div.ty-elevated.rounded-lg.shadow-md.p-6
       (aspect-ratio-demo)]

      [:div.ty-elevated.rounded-lg.shadow-md.p-6
       (container-percentage-demo)]])])
