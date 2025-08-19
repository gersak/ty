(ns ty.layout.components
  "Layout-aware components that demonstrate practical usage"
  (:require
   [ty.layout :as layout]))

(defn responsive-card-grid
  "A card grid that adapts columns based on container size"
  [{:keys [items render-card gap]
    :or {gap 16}}]
  (let [columns (layout/grid-columns)
        actual-gap (or gap (layout/grid-gap))]
    [:div.grid
     {:class [(str "grid-cols-" columns)]
      :style {:gap (str actual-gap "px")}}
     (for [item items]
       ^{:key (:id item)}
       (render-card item))]))

(defn adaptive-sidebar-layout
  "Layout with sidebar that changes based on breakpoint"
  [{:keys [sidebar content]}]
  (let [show-sidebar? (layout/breakpoint>= :lg)
        sidebar-width (layout/responsive-value 
                       {:lg 256 :xl 320 :2xl 384})]
    [:div.flex.h-full
     (when show-sidebar?
       [:aside.border-r.border-gray-200
        {:style {:width (str sidebar-width "px")}}
        sidebar])
     [:main.flex-1
      content]]))

(defn container-aware-modal
  "Modal that sizes based on container dimensions"
  [{:keys [open? title content on-close max-width-percent]
    :or {max-width-percent 90}}]
  (when open?
    (let [max-width (layout/width% max-width-percent)
          max-height (layout/height% 80)]
      [:div.fixed.inset-0.bg-black.bg-opacity-50.flex.items-center.justify-center
       [:div.bg-white.rounded-lg.shadow-xl.flex.flex-col
        {:style {:maxWidth (str max-width "px")
                 :maxHeight (str max-height "px")
                 :width "100%"}}
        [:div.px-6.py-4.border-b.flex.justify-between.items-center
         [:h2.text-lg.font-semibold title]
         [:button.text-gray-500.hover:text-gray-700
          {:on {:click on-close}}
          "×"]]
        [:div.flex-1.overflow-auto.p-6
         content]]])))

(defn responsive-text
  "Text that changes size based on container"
  [{:keys [children element]
    :or {element :div}}]
  (let [font-size (layout/responsive-value 
                   {:xs "0.875rem"    ; 14px
                    :sm "1rem"        ; 16px
                    :md "1.125rem"    ; 18px
                    :lg "1.25rem"     ; 20px
                    :xl "1.5rem"      ; 24px
                    :2xl "1.875rem"}) ; 30px
        line-height (layout/responsive-value
                     {:xs 1.5
                      :sm 1.5
                      :md 1.6
                      :lg 1.6
                      :xl 1.7
                      :2xl 1.7})]
    [element
     {:style {:fontSize font-size
              :lineHeight line-height}}
     children]))

(defn aspect-ratio-box
  "Maintains aspect ratio within container"
  [{:keys [ratio children class]
    :or {ratio 16/9}}]
  (let [{:keys [width height]} (layout/maintain-aspect-ratio ratio)]
    [:div
     {:class class
      :style {:width (str width "px")
              :height (str height "px")}}
     children]))

(defn density-aware-list
  "List with spacing that adapts to density"
  [{:keys [items render-item]}]
  (let [spacing (case (layout/container-density)
                  :compact 8
                  :normal 12
                  :comfortable 16)]
    [:div.space-y-0
     {:style {:gap (str spacing "px")}
      :class ["flex" "flex-col"]}
     (for [item items]
       ^{:key (:id item)}
       (render-item item))]))

(defn orientation-layout
  "Different layouts for portrait vs landscape"
  [{:keys [portrait-content landscape-content]}]
  (if (layout/portrait?)
    [:div.flex.flex-col portrait-content]
    [:div.flex.flex-row landscape-content]))

(defn breakpoint-debug
  "Visual indicator of current breakpoint - useful for development"
  []
  (let [bp (layout/container-breakpoint)]
    [:div.fixed.bottom-4.right-4.bg-black.text-white.px-3.py-1.rounded.text-sm.z-50
     [:span.font-mono (name bp)]]))
