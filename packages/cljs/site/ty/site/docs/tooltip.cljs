(ns ty.site.docs.tooltip
  "ty-tooltip component documentation"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table doc-section example-section]]))

(defn api-reference-section []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2.text-xl.font-semibold.ty-text++.mb-4 "API Reference"]

   ;; Attributes
   (attribute-table
    [{:name "placement"
      :type "string"
      :default "\"top\""
      :description "Preferred placement position: \"top\", \"bottom\", \"left\", \"right\""}
     {:name "flavor"
      :type "string"
      :default "\"dark\""
      :description "Visual style: \"dark\", \"light\", \"primary\", \"secondary\", \"success\", \"danger\", \"warning\", \"info\", \"neutral\""}
     {:name "delay"
      :type "number"
      :default "600"
      :description "Delay in milliseconds before showing tooltip on hover"}
     {:name "offset"
      :type "number"
      :default "8"
      :description "Distance in pixels between tooltip and anchor element"}
     {:name "disabled"
      :type "boolean"
      :default "false"
      :description "Disables tooltip from showing"}])

   ;; Content Note
   [:div.ty-bg-info-.ty-border-info.border.rounded-lg.p-4.mt-4
    [:h3.ty-text++.font-semibold.mb-1 "Content"]
    [:p.ty-text.text-sm
     "Tooltip content is provided as children of the ty-tooltip element. "
     "Can be plain text or HTML elements."]]])

(defn basic-usage-section []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h2.text-xl.font-semibold.ty-text++.mb-4 "Basic Usage"]
   [:p.ty-text-.mb-4
    "Add a tooltip to any element by nesting a ty-tooltip element inside it. "
    "The tooltip will appear on hover or focus."]

   [:div.space-y-4.mb-4
    [:ty-button.mr-4
     "Hover me"
     [:ty-tooltip "This is a helpful tooltip"]]

    [:ty-button.mr-4 {:flavor "primary"}
     "Save Document"
     [:ty-tooltip "Click to save your changes"]]

    [:a.ty-text-primary.underline {:href "#"}
     "Learn more"
     [:ty-tooltip "Opens documentation in new tab"]]]

   (code-block
    "<!-- Simple tooltip -->
<ty-button>
  Hover me
  <ty-tooltip>This is a helpful tooltip</ty-tooltip>
</ty-button>

<!-- On a link -->
<a href=\"#\">
  Learn more
  <ty-tooltip>Opens documentation in new tab</ty-tooltip>
</a>")])

(defn placement-section []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h3.text-lg.font-semibold.ty-text++.mb-3 "Placement Options"]
   [:p.ty-text-.mb-4 "Tooltips can be positioned around the element in four directions."]

   [:div.flex.gap-4.mb-4
    [:ty-button
     "Top"
     [:ty-tooltip {:placement "top"} "Tooltip on top"]]
    [:ty-button
     "Bottom"
     [:ty-tooltip {:placement "bottom"} "Tooltip on bottom"]]
    [:ty-button
     "Left"
     [:ty-tooltip {:placement "left"} "Tooltip on left"]]
    [:ty-button
     "Right"
     [:ty-tooltip {:placement "right"} "Tooltip on right"]]]

   (code-block
    "<ty-button>
  Top
  <ty-tooltip placement=\"top\">Tooltip on top</ty-tooltip>
</ty-button>

<ty-button>
  Bottom
  <ty-tooltip placement=\"bottom\">Tooltip on bottom</ty-tooltip>
</ty-button>")])

(defn flavors-section []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h3.text-lg.font-semibold.ty-text++.mb-3 "Semantic Flavors"]
   [:p.ty-text-.mb-4 "Use different flavors to convey meaning and match your design system."]

   [:div.flex.flex-wrap.gap-3.mb-4
    [:ty-button
     "Dark"
     [:ty-tooltip "Default dark tooltip"]]
    [:ty-button
     "Light"
     [:ty-tooltip {:flavor "light"} "Light tooltip"]]
    [:ty-button {:flavor "primary"}
     "Primary"
     [:ty-tooltip {:flavor "primary"} "Primary action"]]
    [:ty-button {:flavor "success"}
     "Success"
     [:ty-tooltip {:flavor "success"} "Operation successful"]]
    [:ty-button {:flavor "danger"}
     "Danger"
     [:ty-tooltip {:flavor "danger"} "Destructive action"]]
    [:ty-button {:flavor "warning"}
     "Warning"
     [:ty-tooltip {:flavor "warning"} "Proceed with caution"]]
    [:ty-button {:flavor "info"}
     "Info"
     [:ty-tooltip {:flavor "info"} "Additional information"]]
    [:ty-button
     "Neutral"
     [:ty-tooltip {:flavor "neutral"} "Neutral tooltip"]]]

   (code-block
    "<ty-button flavor=\"primary\">
  Primary
  <ty-tooltip flavor=\"primary\">Primary action</ty-tooltip>
</ty-button>

<ty-button flavor=\"danger\">
  Danger
  <ty-tooltip flavor=\"danger\">Destructive action</ty-tooltip>
</ty-button>")])

(defn icon-buttons-section []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h3.text-lg.font-semibold.ty-text++.mb-3 "Icon Buttons"]
   [:p.ty-text-.mb-4 "Tooltips are essential for icon-only buttons to provide context."]

   [:div.flex.gap-3.mb-4
    [:ty-button {:flavor "primary"}
     [:ty-icon {:name "save"}]
     [:ty-tooltip "Save document"]]
    [:ty-button {:flavor "danger"}
     [:ty-icon {:name "trash"}]
     [:ty-tooltip {:flavor "danger"} "Delete item"]]
    [:ty-button
     [:ty-icon {:name "settings"}]
     [:ty-tooltip "Open settings"]]
    [:ty-button
     [:ty-icon {:name "share"}]
     [:ty-tooltip "Share this page"]]]

   (code-block
    "<ty-button flavor=\"primary\">
  <ty-icon name=\"save\"></ty-icon>
  <ty-tooltip>Save document</ty-tooltip>
</ty-button>

<ty-button flavor=\"danger\">
  <ty-icon name=\"trash\"></ty-icon>
  <ty-tooltip flavor=\"danger\">Delete item</ty-tooltip>
</ty-button>")])

(defn rich-content-section []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h3.text-lg.font-semibold.ty-text++.mb-3 "Rich Content"]
   [:p.ty-text-.mb-4 "Tooltips can contain any HTML content, not just plain text."]

   [:div.flex.gap-4.mb-4
    [:ty-button
     "Formatted"
     [:ty-tooltip
      [:div
       [:div.font-bold.mb-1 "Save Document"]
       [:div.text-sm "Cmd+S or Ctrl+S"]]]]

    [:ty-button {:flavor "danger"}
     "Delete"
     [:ty-tooltip {:flavor "danger"}
      [:div
       [:div.font-bold.mb-1 "⚠️ Permanent Action"]
       [:div.text-sm "This cannot be undone"]]]]]

   (code-block
    "<ty-button>
  Formatted
  <ty-tooltip>
    <div>
      <div class=\"font-bold mb-1\">Save Document</div>
      <div class=\"text-sm\">Cmd+S or Ctrl+S</div>
    </div>
  </ty-tooltip>
</ty-button>")])

(defn delay-section []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h3.text-lg.font-semibold.ty-text++.mb-3 "Show Delay"]
   [:p.ty-text-.mb-4 "Customize the delay before tooltip appears (default is 600ms)."]

   [:div.flex.gap-4.mb-4
    [:ty-button
     "Instant"
     [:ty-tooltip {:delay 0} "No delay"]]
    [:ty-button
     "Normal"
     [:ty-tooltip {:delay 600} "Default 600ms delay"]]
    [:ty-button
     "Slow"
     [:ty-tooltip {:delay 1000} "1 second delay"]]]

   (code-block
    "<ty-button>
  Instant
  <ty-tooltip delay=\"0\">No delay</ty-tooltip>
</ty-button>

<ty-button>
  Slow
  <ty-tooltip delay=\"1000\">1 second delay</ty-tooltip>
</ty-button>")])

(defn disabled-section []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h3.text-lg.font-semibold.ty-text++.mb-3 "Disabled State"]
   [:p.ty-text-.mb-4 "Temporarily disable tooltips without removing them."]

   [:div.flex.gap-4.mb-4
    [:ty-button
     "Enabled"
     [:ty-tooltip "Tooltip is active"]]
    [:ty-button
     "Disabled"
     [:ty-tooltip {:disabled true} "This won't show"]]]

   (code-block
    "<ty-button>
  Disabled
  <ty-tooltip disabled>This won't show</ty-tooltip>
</ty-button>")])

(defn best-practices-section []
  [:div.ty-bg-info-.ty-border-info.border.rounded-lg.p-6.mb-8
   [:h3.ty-text++.text-lg.font-semibold.mb-3 "Best Practices"]
   [:ul.list-disc.ml-6.space-y-2.ty-text
    [:li "Keep tooltip text concise and helpful"]
    [:li "Use tooltips for icon-only buttons to provide context"]
    [:li "Match tooltip flavor to button flavor for consistency"]
    [:li "Don't put critical information only in tooltips"]
    [:li "Tooltips should supplement, not replace, visible labels"]
    [:li "Avoid tooltips on mobile - they don't work well with touch"]
    [:li "Use rich content sparingly - keep it simple"]]])

(defn accessibility-section []
  [:div.ty-bg-warning-.ty-border-warning.border.rounded-lg.p-6.mb-8
   [:h3.ty-text-warning++.text-lg.font-semibold.mb-3 "Accessibility"]
   [:ul.list-disc.ml-6.space-y-2.ty-text-warning
    [:li "Tooltips show on both hover and keyboard focus"]
    [:li "They hide when focus leaves the element"]
    [:li "Consider adding aria-label for screen readers"]
    [:li "Don't rely solely on tooltips for critical information"]
    [:li "Ensure sufficient color contrast for all flavors"]]])

(defn view []
  [:div
   [:h1.text-3xl.font-bold.ty-text++.mb-6 "Tooltip"]
   [:p.ty-text.text-lg.mb-8
    "Contextual information that appears on hover or focus. "
    "Perfect for explaining icon buttons or providing helpful hints."]

   (api-reference-section)
   (basic-usage-section)
   (placement-section)
   (flavors-section)
   (icon-buttons-section)
   (rich-content-section)
   (delay-section)
   (disabled-section)
   (best-practices-section)
   (accessibility-section)])
