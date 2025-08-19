(ns ty.demo.views.layout
  "Demonstrates the layout context system"
  (:require
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
  (layout/with-window
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

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (responsive-grid-demo)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (nested-container-demo)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (responsive-value-demo)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (aspect-ratio-demo)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (container-percentage-demo)]]))
