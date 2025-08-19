(ns ty.demo.views.layout
  "Demonstrates the layout context system"
  (:require
    [ty.context :refer [*element-sizes*]]
    [ty.demo.state :refer [state]]
    [ty.layout :as layout]))

(defn container-info-display []
  (let [{:keys [width height breakpoint orientation density scrollbar-width]}
        (layout/current-container)]
    [:div.bg-gray-100.dark:bg-gray-800.p-4.rounded-lg.space-y-2
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
       [:span.px-2.py-1.bg-blue-100.dark:bg-blue-900.rounded.text-xs
        (when breakpoint (name breakpoint))]]
      [:div
       [:span.font-medium "Orientation: "]
       [:span.px-2.py-1.bg-green-100.dark:bg-green-900.rounded.text-xs
        (when orientation (name orientation))]]
      [:div
       [:span.font-medium "Density: "]
       [:span.px-2.py-1.bg-purple-100.dark:bg-purple-900.rounded.text-xs
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
                [:bg-green-500 :text-white]
                [:bg-gray-300 :text-gray-700])}
      (str (layout/breakpoint>= :md))]]
    [:div.flex.items-center.gap-2
     [:span.font-medium "breakpoint<= :lg"]
     [:span.px-2.py-1.rounded.text-xs
      {:class (if (layout/breakpoint<= :lg)
                [:bg-green-500 :text-white]
                [:bg-gray-300 :text-gray-700])}
      (str (layout/breakpoint<= :lg))]]
    [:div.flex.items-center.gap-2
     [:span.font-medium "portrait?"]
     [:span.px-2.py-1.rounded.text-xs
      {:class (if (layout/portrait?)
                [:bg-green-500 :text-white]
                [:bg-gray-300 :text-gray-700])}
      (str (layout/portrait?))]]
    [:div.flex.items-center.gap-2
     [:span.font-medium "landscape?"]
     [:span.px-2.py-1.rounded.text-xs
      {:class (if (layout/landscape?)
                [:bg-green-500 :text-white]
                [:bg-gray-300 :text-gray-700])}
      (str (layout/landscape?))]]]])

(defn responsive-grid-demo []
  (let [columns (layout/grid-columns)
        gap (layout/grid-gap)]
    [:div.space-y-4
     [:h3.text-lg.font-semibold "Responsive Grid"]
     [:div.text-sm.text-gray-600
      (str "Columns: " columns ", Gap: " gap "px", "Container Width: " (layout/container-width))]
     [:div.grid
      {;:class [(str "grid-cols-" columns)]
       :style {:gap (str gap "px")
               :grid-template-columns (str "repeat(" columns ", minmax(0, 1fr))")}}
      (for [i (range columns)]
        [:div.bg-blue-100.dark:bg-blue-900.p-4.rounded.text-center
         {:key i}
         (inc i)])]]))

(defn nested-container-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Nested Containers"]
   [:div.border-2.border-dashed.border-blue-500.p-4.rounded
    [:div.mb-4
     [:span.font-medium "Outer Container: "]
     [:span (layout/container-info)]]

    ;; Nested container with 80% dimensions
    (layout/with-container {:width (layout/width% 80)
                            :height (layout/height% 80)}
      [:div.border-2.border-dashed.border-green-500.p-4.rounded
       [:div.mb-4
        [:span.font-medium "Inner Container (80%): "]
        [:span (layout/container-info)]]

       ;; Double nested with 50% dimensions
       (layout/with-container {:width (layout/width% 50)
                               :height (layout/height% 50)}
         [:div.border-2.border-dashed.border-purple-500.p-4.rounded
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
      [:p.text-sm.text-gray-600.mt-2
       (str "Font: " font-size ", Padding: " padding "px")]]]))

(defn aspect-ratio-demo []
  (let [{:keys [width height]} (layout/maintain-aspect-ratio (/ 16 9))]
    [:div.space-y-4
     [:h3.text-lg.font-semibold "Aspect Ratio Helper"]
     [:div.bg-gray-200.dark:bg-gray-700.rounded.flex.items-center.justify-center
      {:style {:width (str width "px")
               :height (str height "px")}}
      [:div.text-center
       [:div "16:9 Aspect Ratio"]
       [:div.text-sm.text-gray-600
        (str (.toFixed width 0) " x " (.toFixed height 0))]]]]))

(defn container-percentage-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Container Percentage Helpers"]
   [:div.space-y-2
    [:div.bg-blue-100.dark:bg-blue-900.rounded.p-2
     {:style {:width (str (layout/width% 75) "px")}}
     "75% of container width"]
    [:div.bg-green-100.dark:bg-green-900.rounded.p-2
     {:style {:width (str (layout/width% 50) "px")}}
     "50% of container width"]
    [:div.bg-purple-100.dark:bg-purple-900.rounded.p-2
     {:style {:width (str (layout/width% 25) "px")}}
     "25% of container width"]]])

;; ===== RESIZE OBSERVER DEMOS =====

(defn resize-observer-basic-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Basic Resize Observer"]
   [:p.text-sm.text-gray-600 "Drag the bottom-right corner to resize this panel."]
   [:ty-resize-observer#basic-demo.bg-blue-50.dark:bg-blue-900.border-2.border-blue-200.dark:border-blue-700.rounded-lg.p-4.resize.overflow-auto
    {:style {:min-width "200px"
             :min-height "150px"
             :width "300px"
             :height "200px"}}
    (layout/with-resize-observer "basic-demo"
      [:div.space-y-3
       [:div.text-center
        [:h4.font-semibold "Resizable Panel"]
        [:p.text-sm.text-gray-600 "This content is aware of its container size"]]
       [:div.bg-white.dark:bg-gray-800.rounded.p-3
        [:div.text-sm
         [:div [:span.font-medium "Width: "] [:span.font-mono (layout/container-width) "px"]]
         [:div [:span.font-medium "Height: "] [:span.font-mono (layout/container-height) "px"]]
         [:div [:span.font-medium "Breakpoint: "] [:span.font-mono (name (layout/container-breakpoint))]]
         [:div [:span.font-medium "Orientation: "] [:span.font-mono (name (layout/container-orientation))]]]]
       [:div.text-center
        (if (layout/breakpoint>= :md)
          [:div.text-green-600 "✓ Wide enough for desktop layout"]
          [:div.text-orange-600 "⚠ Using compact mobile layout"])]])]])

(defn resize-observer-responsive-grid []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Container-Aware Responsive Grid"]
   [:p.text-sm.text-gray-600 "This grid adapts based on the container size, not window size."]
   [:ty-resize-observer#grid-demo.bg-gray-50.dark:bg-gray-800.border-2.border-gray-200.dark:border-gray-600.rounded-lg.p-4.resize.overflow-auto
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
          [:div.text-xs.text-gray-500 "Based on container breakpoint: " (name (layout/container-breakpoint))]]
         [:div.grid.gap-2
          {:style {:grid-template-columns (str "repeat(" columns ", minmax(0, 1fr))")}}
          (for [i (range 8)]
            [:div.bg-white.dark:bg-gray-700.rounded.p-2.text-center.text-sm
             {:key i}
             (str "Item " (inc i))])]]))]])

(defn resize-observer-flexbox-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Flexbox Layout with Resize Observer"]
   [:p.text-sm.text-gray-600 "Both panels use resize observers for layout context."]
   [:div.flex.gap-4.h-80
    [:ty-resize-observer#flex-sidebar.bg-purple-50.dark:bg-purple-900.border-2.border-purple-200.dark:border-purple-700.rounded-lg.p-4.resize.overflow-auto
     {:style {:min-width "150px"
              :width "200px"}}
     (layout/with-resize-observer "flex-sidebar"
       [:div.space-y-3
        [:h4.font-semibold.text-center "Sidebar"]
        [:div.bg-white.dark:bg-gray-800.rounded.p-2.text-xs
         [:div "Width: " [:span.font-mono (layout/container-width) "px"]]
         [:div "Height: " [:span.font-mono (layout/container-height) "px"]]]
        [:div.text-center.text-sm
         (if (> (layout/container-width) 180)
           [:div "🎯 Full content"]
           [:div "📱 Compact"])]])]
    [:ty-resize-observer#flex-main.bg-green-50.dark:bg-green-900.border-2.border-green-200.dark:border-green-700.rounded-lg.p-4.flex-1
     (layout/with-resize-observer "flex-main"
       [:div.space-y-3
        [:h4.font-semibold.text-center "Main Content"]
        [:div.bg-white.dark:bg-gray-800.rounded.p-3
         [:div.text-sm.space-y-1
          [:div "Width: " [:span.font-mono (layout/container-width) "px"]]
          [:div "Height: " [:span.font-mono (layout/container-height) "px"]]
          [:div "Breakpoint: " [:span.font-mono (name (layout/container-breakpoint))]]]]
        [:div.text-center
         (cond
           (layout/breakpoint>= :lg) [:div.text-green-600 "🖥️ Desktop Layout"]
           (layout/breakpoint>= :md) [:div.text-blue-600 "💻 Tablet Layout"]
           :else [:div.text-purple-600 "📱 Mobile Layout"])]])]]])

(defn resize-observer-nested-demo []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Nested Resize Observers"]
   [:p.text-sm.text-gray-600 "Nested containers, each with their own resize observer."]
   [:ty-resize-observer#outer-container.bg-blue-50.dark:bg-blue-900.border-2.border-blue-300.dark:border-blue-600.rounded-lg.p-4.resize.overflow-auto
    {:style {:min-width "300px"
             :min-height "200px"
             :width "500px"
             :height "300px"}}
    (layout/with-resize-observer "outer-container"
      [:div.space-y-3
       [:div.text-center
        [:h4.font-semibold "Outer Container"]
        [:div.text-sm "Dimensions: " [:span.font-mono (layout/container-width) "×" (layout/container-height)]]]
       [:ty-resize-observer#inner-container.bg-white.dark:bg-gray-800.border-2.border-gray-300.dark:border-gray-600.rounded.p-3.resize.overflow-auto
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
              [:div.text-green-600 "✓ Comfortable size"]
              [:div.text-red-600 "⚠ Too small"])]])]])]])

(defn resize-observer-api-demo []
  (let [basic-size (layout/get-element-size "basic-demo")
        grid-size (layout/get-element-size "grid-demo")
        sidebar-size (layout/get-element-size "flex-sidebar")
        main-size (layout/get-element-size "flex-main")]
    [:div.space-y-4
     [:h3.text-lg.font-semibold "Resize Observer API"]
     [:p.text-sm.text-gray-600 "Direct access to element sizes from anywhere in the application."]
     [:div.bg-gray-50.dark:bg-gray-800.rounded-lg.p-4
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
        [:div.text-xs.font-mono.text-gray-600
         (str (count @*element-sizes*) " elements being tracked")]]
       [:div
        [:h4.font-medium.mb-2 "Usage Examples"]
        [:div.text-xs.space-y-1.font-mono.bg-white.dark:bg-gray-700.p-2.rounded
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
       [:div.text-xs.text-gray-500 "Resize the window to see updates"]])]])

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
    [:div.text-xs.text-gray-500
     "The container context is bound from window dimensions"]]])

(defn layout-view []
  ;; Use the with-window macro instead of manual tracking
  [:ty-resize-observer#layout-core-features
   (layout/with-resize-observer "layout-core-features"
     [:div.p-8.max-w-6xl.mx-auto.space-y-8
      [:div
       [:h1.text-3xl.font-bold.mb-4 "Layout Context System"]
       [:p.text-gray-600
        "Dynamic layout context using Clojure's dynamic vars for responsive components."]
       [:p.text-sm.text-gray-500.mt-2
        "Using the " [:code "with-window"] " macro for automatic dimension tracking."]]

      [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
       (container-info-display)]

      [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
       (window-tracking-demo)]

      [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
       (window-vs-container-demo)]

      [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
       (breakpoint-helpers-demo)]

      ;; ===== RESIZE OBSERVER SECTION =====
      [:div
       [:h2.text-2xl.font-bold.mb-4.text-blue-600.dark:text-blue-400 "Resize Observer Components"]
       [:p.text-gray-600.mb-6
        "Self-observing components that integrate seamlessly with the layout context system."]]

      [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
       (resize-observer-basic-demo)]

      [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
       (resize-observer-responsive-grid)]

      [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
       (resize-observer-flexbox-demo)]

      [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
       (resize-observer-nested-demo)]

      [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
       (resize-observer-api-demo)]

      ;; ===== ORIGINAL LAYOUT DEMOS =====
      [:div
       [:h2.text-2xl.font-bold.mb-4.text-green-600.dark:text-green-400 "Core Layout Features"]
       [:p.text-gray-600.mb-6
        "Built-in layout context features and helpers."]]

      [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
       (responsive-grid-demo)]

      [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
       (nested-container-demo)]

      [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
       (responsive-value-demo)]

      [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
       (aspect-ratio-demo)]

      [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
       (container-percentage-demo)]])])
