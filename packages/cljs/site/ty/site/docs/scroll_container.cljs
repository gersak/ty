(ns ty.site.docs.scroll-container
  "Documentation for the ty-scroll-container component"
  (:require [ty.site.docs.common :refer [code-block attribute-table docs-page]]))

(defn header-section []
  [:div.mb-8
   [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-scroll-container"]
   [:p.text-lg.ty-text-
    "A scroll container with shadow indicators and an optional custom-rendered scrollbar "
    "that looks consistent across all operating systems."]])

(defn api-reference-section []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-semibold.ty-text++.mb-4 "API Reference"]

   [:div.mb-6
    [:h3.text-lg.font-semibold.ty-text+.mb-3 "Attributes"]
    (attribute-table
      [{:name "max-height"
        :type "string"
        :default "none"
        :description "Maximum height of the scrollable area (e.g. \"300px\", \"50vh\")"}
       {:name "hide-scrollbar"
        :type "boolean"
        :default "false"
        :description "Hides the native scrollbar (no replacement rendered)"}
       {:name "custom-scrollbar"
        :type "boolean"
        :default "false"
        :description "Hides native scrollbar and renders a custom overlay scrollbar"}
       {:name "overflow-x"
        :type "boolean"
        :default "false"
        :description "Enable horizontal scrolling (adds horizontal scrollbar when custom-scrollbar is enabled)"}
       {:name "shadow"
        :type "string"
        :default "true"
        :description "Set to \"false\" to disable scroll shadow indicators"}])]

   [:div.mb-6
    [:h3.text-lg.font-semibold.ty-text+.mb-3 "CSS Custom Properties (custom-scrollbar mode)"]
    (attribute-table
      [{:name "--ty-scrollbar-width"
        :type "length"
        :default "8px"
        :description "Track width"}
       {:name "--ty-scrollbar-radius"
        :type "length"
        :default "4px"
        :description "Thumb and track border radius"}
       {:name "--ty-scrollbar-thumb"
        :type "color"
        :default "var(--ty-border)"
        :description "Thumb color"}
       {:name "--ty-scrollbar-thumb-hover"
        :type "color"
        :default "var(--ty-border-strong)"
        :description "Thumb color on hover"}
       {:name "--ty-scrollbar-thumb-active"
        :type "color"
        :default "var(--ty-border-strong)"
        :description "Thumb color while dragging"}
       {:name "--ty-scrollbar-track"
        :type "color"
        :default "transparent"
        :description "Track background"}
       {:name "--ty-scrollbar-track-hover"
        :type "color"
        :default "var(--ty-surface-elevated)"
        :description "Track background on hover"}
       {:name "--ty-scrollbar-thumb-min-height"
        :type "length"
        :default "30px"
        :description "Minimum thumb height"}])]

   [:div
    [:h3.text-lg.font-semibold.ty-text+.mb-3 "Methods"]
    [:div.ty-bg-neutral-.rounded.p-4.overflow-x-auto
     [:table.w-full.text-sm
      [:thead
       [:tr.border-b.ty-border
        [:th.text-left.p-2.ty-text+ "Method"]
        [:th.text-left.p-2.ty-text+ "Description"]]]
      [:tbody
       [:tr.border-b.ty-border-
        [:td.p-2.font-mono.text-xs "scrollToTop(smooth?)"]
        [:td.p-2.ty-text "Scroll to top, optionally with smooth animation"]]
       [:tr.border-b.ty-border-
        [:td.p-2.font-mono.text-xs "scrollToBottom(smooth?)"]
        [:td.p-2.ty-text "Scroll to bottom, optionally with smooth animation"]]
       [:tr.border-b.ty-border-
        [:td.p-2.font-mono.text-xs "scrollToLeft(smooth?)"]
        [:td.p-2.ty-text "Scroll to left edge"]]
       [:tr.border-b.ty-border-
        [:td.p-2.font-mono.text-xs "scrollToRight(smooth?)"]
        [:td.p-2.ty-text "Scroll to right edge"]]
       [:tr
        [:td.p-2.font-mono.text-xs "updateShadows()"]
        [:td.p-2.ty-text "Force-update shadow state and scrollbar after dynamic content changes"]]]]]]])

(defn- sample-items
  "Generate sample list items for demos"
  [n]
  (for [i (range 1 (inc n))]
    [:div.p-3.ty-border-.border-b
     {:key i}
     [:div.flex.items-center.justify-between
      [:span.ty-text (str "Item " i)]
      [:span.ty-text-.text-sm (str "Description for item " i)]]]))

(defn scrollbar-modes-section []
  [:div.ty-elevated.rounded-lg.p-6
   [:h3.text-xl.font-semibold.ty-text+.mb-4 "Scrollbar Modes"]
   [:p.ty-text-.mb-4 "Three modes: native (default), hidden, or custom rendered:"]

   [:div.grid.grid-cols-1.md:grid-cols-3.gap-6
    [:div
     [:h4.font-semibold.ty-text.mb-2 "Native (default)"]
     [:ty-scroll-container {:max-height "200px"}
      (sample-items 15)]]

    [:div
     [:h4.font-semibold.ty-text.mb-2 "Hidden"]
     [:ty-scroll-container {:max-height "200px"
                            :hide-scrollbar true}
      (sample-items 15)]]

    [:div
     [:h4.font-semibold.ty-text.mb-2 "Custom"]
     [:ty-scroll-container {:max-height "200px"
                            :custom-scrollbar true}
      (sample-items 15)]]]

   [:div.mt-4
    (code-block
      "<!-- Native scrollbar (default) -->
<ty-scroll-container max-height=\"200px\">...</ty-scroll-container>

<!-- Hidden scrollbar -->
<ty-scroll-container max-height=\"200px\" hide-scrollbar>...</ty-scroll-container>

<!-- Custom rendered scrollbar -->
<ty-scroll-container max-height=\"200px\" custom-scrollbar>...</ty-scroll-container>")]])

(defn themed-demo-section []
  [:div.ty-elevated.rounded-lg.p-6
   [:h3.text-xl.font-semibold.ty-text+.mb-4 "Themed Custom Scrollbar"]
   [:p.ty-text-.mb-4 "Customize scrollbar colors and size via CSS custom properties:"]

   [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
    [:div
     [:h4.font-semibold.ty-text.mb-2 "Default theme"]
     [:ty-scroll-container {:max-height "200px"
                            :custom-scrollbar true}
      (sample-items 20)]]

    [:div
     [:h4.font-semibold.ty-text.mb-2 "Wide + colored"]
     [:ty-scroll-container {:max-height "200px"
                            :custom-scrollbar true
                            :style {:--ty-scrollbar-width "12px"
                                    :--ty-scrollbar-thumb "var(--ty-color-primary)"
                                    :--ty-scrollbar-thumb-hover "var(--ty-color-primary-strong)"
                                    :--ty-scrollbar-radius "6px"}}
      (sample-items 20)]]]

   [:div.mt-4
    (code-block
      "<ty-scroll-container max-height=\"200px\" custom-scrollbar
  style=\"--ty-scrollbar-width: 12px;
         --ty-scrollbar-thumb: var(--ty-color-primary);
         --ty-scrollbar-radius: 6px;\">
  <!-- content -->
</ty-scroll-container>")]])

(defn horizontal-demo-section []
  [:div.ty-elevated.rounded-lg.p-6
   [:h3.text-xl.font-semibold.ty-text+.mb-4 "Horizontal Scrolling"]
   [:p.ty-text-.mb-4 "Enable horizontal scrolling with the overflow-x attribute. Both axes work together:"]

   [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
    [:div
     [:h4.font-semibold.ty-text.mb-2 "Horizontal only"]
     [:ty-scroll-container {:max-height "150px"
                            :overflow-x true
                            :custom-scrollbar true}
      [:div {:style {:width "1500px" :padding "1rem"}}
       [:div.flex.gap-4
        (for [i (range 1 21)]
          [:div.ty-elevated.rounded-lg.p-4.flex-shrink-0
           {:key i :style {:width "200px"}}
           [:div.ty-text+.font-semibold (str "Card " i)]
           [:div.ty-text-.text-sm "Horizontal scroll content"]])]]]]

    [:div
     [:h4.font-semibold.ty-text.mb-2 "Both axes"]
     [:ty-scroll-container {:max-height "200px"
                            :overflow-x true
                            :custom-scrollbar true}
      [:div {:style {:width "1500px"}}
       (for [i (range 1 16)]
         [:div.p-3.ty-border-.border-b
          {:key i}
          [:div.flex.items-center.justify-between
           [:span.ty-text (str "Row " i " — this content is wide enough to require horizontal scrolling")]
           [:span.ty-text-.text-sm {:style {:white-space "nowrap"}} (str "Extra detail for item " i)]]])]]]]

   [:div.mt-4
    (code-block
      "<ty-scroll-container max-height=\"200px\" overflow-x custom-scrollbar>
  <div style=\"width: 1500px\">
    <!-- wide content that overflows horizontally -->
  </div>
</ty-scroll-container>")]])

(defn no-shadow-section []
  [:div.ty-elevated.rounded-lg.p-6
   [:h3.text-xl.font-semibold.ty-text+.mb-4 "Without Shadows"]
   [:p.ty-text-.mb-4 "Disable the scroll shadow indicators:"]

   [:ty-scroll-container {:max-height "200px"
                          :shadow "false"
                          :custom-scrollbar true}
    (sample-items 15)]

   [:div.mt-4
    (code-block
      "<ty-scroll-container max-height=\"200px\" shadow=\"false\" custom-scrollbar>
  <!-- custom scrollbar, no shadow indicators -->
</ty-scroll-container>")]])

(defn view []
  (docs-page
    (header-section)
    (api-reference-section)

    [:h2.text-2xl.font-semibold.ty-text++.mb-6 "Examples"]

    [:div.space-y-8
     (scrollbar-modes-section)
     (horizontal-demo-section)
     (themed-demo-section)
     (no-shadow-section)]))
