(ns ty.site.docs.scroll-container
  "Documentation for the ty-scroll-container component"
  (:require [ty.site.docs.common :refer [code-block attribute-table docs-page]]))

(defn header-section []
  [:div.mb-8
   [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-scroll-container"]
   [:p.text-lg.ty-text-
    "A scroll container with visual shadow indicators at top and bottom edges when content is scrollable."]])

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
        :description "Fully hides the native scrollbar while keeping scroll functionality"}
       {:name "shadow"
        :type "string"
        :default "true"
        :description "Set to \"false\" to disable scroll shadow indicators"}])]

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
       [:tr
        [:td.p-2.font-mono.text-xs "updateShadows()"]
        [:td.p-2.ty-text "Force-update shadow state after dynamic content changes"]]]]]]])

(defn- sample-items
  "Generate sample list items for demos"
  [n]
  (for [i (range 1 (inc n))]
    [:div.p-3.ty-border-.border-b
     {:key i}
     [:div.flex.items-center.justify-between
      [:span.ty-text (str "Item " i)]
      [:span.ty-text-.text-sm (str "Description for item " i)]]]))

(defn basic-demo-section []
  [:div.ty-elevated.rounded-lg.p-6
   [:h3.text-xl.font-semibold.ty-text+.mb-4 "Basic Usage"]
   [:p.ty-text-.mb-4 "A scroll container with shadow indicators that appear based on scroll position:"]

   [:ty-scroll-container {:max-height "200px"}
    (sample-items 15)]

   [:div.mt-4
    (code-block
      "<ty-scroll-container max-height=\"200px\">
  <div>Item 1</div>
  <div>Item 2</div>
  <!-- ... more items ... -->
</ty-scroll-container>")]])

(defn scrollbar-comparison-section []
  [:div.ty-elevated.rounded-lg.p-6
   [:h3.text-xl.font-semibold.ty-text+.mb-4 "Scrollbar Visibility"]
   [:p.ty-text-.mb-4 "Compare the default scrollbar with a hidden scrollbar side by side:"]

   [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
    [:div
     [:h4.font-semibold.ty-text.mb-2 "Default (with scrollbar)"]
     [:ty-scroll-container {:max-height "200px"}
      (sample-items 12)]]

    [:div
     [:h4.font-semibold.ty-text.mb-2 "Hidden scrollbar"]
     [:ty-scroll-container {:max-height "200px"
                            :hide-scrollbar true}
      (sample-items 12)]]]

   [:div.mt-4
    (code-block
      "<!-- Default scrollbar -->
<ty-scroll-container max-height=\"200px\">
  <!-- content -->
</ty-scroll-container>

<!-- Hidden scrollbar -->
<ty-scroll-container max-height=\"200px\" hide-scrollbar>
  <!-- content is still scrollable, no scrollbar visible -->
</ty-scroll-container>")]])

(defn no-shadow-section []
  [:div.ty-elevated.rounded-lg.p-6
   [:h3.text-xl.font-semibold.ty-text+.mb-4 "Without Shadows"]
   [:p.ty-text-.mb-4 "Disable the scroll shadow indicators:"]

   [:ty-scroll-container {:max-height "200px"
                          :shadow "false"}
    (sample-items 15)]

   [:div.mt-4
    (code-block
      "<ty-scroll-container max-height=\"200px\" shadow=\"false\">
  <!-- no shadow indicators at top/bottom -->
</ty-scroll-container>")]])

(defn view []
  (docs-page
    (header-section)
    (api-reference-section)

    [:h2.text-2xl.font-semibold.ty-text++.mb-6 "Examples"]

    [:div.space-y-8
     (basic-demo-section)
     (scrollbar-comparison-section)
     (no-shadow-section)]))
