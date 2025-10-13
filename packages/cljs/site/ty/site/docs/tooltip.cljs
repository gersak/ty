(ns ty.site.docs.tooltip
  "ty-tooltip component documentation"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table doc-section example-section]]))

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Title and Description
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-tooltip"]
    [:p.text-lg.ty-text-
     "A lightweight, accessible tooltip component that displays helpful text on hover or focus. "
     "Automatically positions itself to stay within viewport boundaries."]]

   ;; API Reference Card
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
     [:h3.ty-text-info++.font-semibold.mb-1 "Content"]
     [:p.ty-text-info.text-sm
      "Tooltip content is provided as children of the ty-tooltip element. "
      "Can be plain text or HTML elements."]]]

   ;; Basic Usage Section
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
</a>")]

   ;; Examples Section
   [:h2.text-xl.font-semibold.ty-text++.mb-4 "Examples"]

   [:div.space-y-8
    ;; Placement Example
    [:div.ty-content.rounded-lg.p-6
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
</ty-button>")]

    ;; Flavors Example
    [:div.ty-content.rounded-lg.p-6
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
       [:ty-tooltip {:flavor "warning"} "Proceed with caution"]]]

     (code-block
       "<!-- Default dark -->
<ty-button>
  Dark
  <ty-tooltip>Default dark tooltip</ty-tooltip>
</ty-button>

<!-- Semantic flavors -->
<ty-button flavor=\"success\">
  Success
  <ty-tooltip flavor=\"success\">Operation successful</ty-tooltip>
</ty-button>

<ty-button flavor=\"danger\">
  Danger
  <ty-tooltip flavor=\"danger\">Destructive action</ty-tooltip>
</ty-button>")]

    ;; Rich Content Example
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text++.mb-3 "Rich Content"]
     [:p.ty-text-.mb-4 "Tooltips can contain HTML for more complex content."]

     [:div.flex.gap-4.mb-4
      [:ty-button
       "Text Formatting"
       [:ty-tooltip
        [:div
         [:strong "Bold text"]
         " and "
         [:em "italic text"]]]]

      [:ty-button {:flavor "primary"}
       "Multi-line"
       [:ty-tooltip
        [:div
         [:div "Line 1: Important info"]
         [:div "Line 2: More details"]
         [:div.text-xs.opacity-75 "Line 3: Additional note"]]]]

      [:ty-button
       [:ty-icon {:name "info"
                  :slot "start"}]
       "With Icon"
       [:ty-tooltip
        [:div.flex.items-center.gap-2
         [:ty-icon {:name "alert-circle"
                    :size "sm"}]
         [:span "Important information"]]]]]

     (code-block
       "<!-- Rich formatting -->
<ty-button>
  Text Formatting
  <ty-tooltip>
    <div>
      <strong>Bold text</strong> and <em>italic text</em>
    </div>
  </ty-tooltip>
</ty-button>

<!-- Multi-line content -->
<ty-button>
  Multi-line
  <ty-tooltip>
    <div>
      <div>Line 1: Important info</div>
      <div>Line 2: More details</div>
      <div class=\"text-xs opacity-75\">Line 3: Additional note</div>
    </div>
  </ty-tooltip>
</ty-button>

<!-- With icon -->
<ty-button>
  With Icon
  <ty-tooltip>
    <div class=\"flex items-center gap-2\">
      <ty-icon name=\"alert-circle\" size=\"sm\"></ty-icon>
      <span>Important information</span>
    </div>
  </ty-tooltip>
</ty-button>" "html")]

    ;; Delay Example
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text++.mb-3 "Hover Delays"]
     [:p.ty-text-.mb-4 "Control how quickly tooltips appear with the delay attribute."]

     [:div.flex.gap-4.mb-4
      [:ty-button
       "Instant (0ms)"
       [:ty-tooltip {:delay "0"} "Shows immediately"]]
      [:ty-button
       "Quick (200ms)"
       [:ty-tooltip {:delay "200"} "Shows quickly"]]
      [:ty-button
       "Default (600ms)"
       [:ty-tooltip "Standard delay"]]
      [:ty-button
       "Slow (1000ms)"
       [:ty-tooltip {:delay "1000"} "Shows after 1 second"]]]

     (code-block
       "<!-- Instant tooltip -->
<ty-button>
  Instant
  <ty-tooltip delay=\"0\">Shows immediately</ty-tooltip>
</ty-button>

<!-- Custom delay -->
<ty-button>
  Slow
  <ty-tooltip delay=\"1000\">Shows after 1 second</ty-tooltip>
</ty-button>")]

    ;; Icon Buttons Example
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text++.mb-3 "Icon Buttons with Tooltips"]
     [:p.ty-text-.mb-4 "Tooltips are especially useful for icon-only buttons to provide context."]

     [:div.flex.gap-3.mb-4
      [:ty-button {:action "true"}
       [:ty-icon {:name "edit"}]
       [:ty-tooltip "Edit document"]]

      [:ty-button {:action "true"
                   :flavor "success"}
       [:ty-icon {:name "save"}]
       [:ty-tooltip {:flavor "success"} "Save changes"]]

      [:ty-button {:action "true"
                   :flavor "danger"}
       [:ty-icon {:name "trash-2"}]
       [:ty-tooltip {:flavor "danger"} "Delete item"]]

      [:ty-button {:action "true"
                   :flavor "primary"}
       [:ty-icon {:name "share-2"}]
       [:ty-tooltip {:flavor "primary"} "Share document"]]]

     (code-block
       "<!-- Icon-only action buttons -->
<ty-button action=\"true\">
  <ty-icon name=\"edit\"></ty-icon>
  <ty-tooltip>Edit document</ty-tooltip>
</ty-button>

<ty-button action=\"true\" flavor=\"success\">
  <ty-icon name=\"save\"></ty-icon>
  <ty-tooltip flavor=\"success\">Save changes</ty-tooltip>
</ty-button>

<ty-button action=\"true\" flavor=\"danger\">
  <ty-icon name=\"trash-2\"></ty-icon>
  <ty-tooltip flavor=\"danger\">Delete item</ty-tooltip>
</ty-button>")]]

   ;; Common Use Cases Section
   [:h2.text-xl.font-semibold.ty-text++.mb-4.mt-8 "Common Use Cases"]

   [:div.space-y-6.mb-8
    ;; Form Field Help
    [:div
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "Form Field Help"]
     [:div.space-y-3
      [:div
       [:label.ty-text+.block.text-sm.font-medium.mb-1
        "Email Address"
        [:span.inline-block.ml-1.ty-text-.cursor-help
         [:ty-icon {:name "help-circle"
                    :size "xs"}]
         [:ty-tooltip "Must be a valid email format"]]]
       [:ty-input {:type "email"
                   :placeholder "user@example.com"}]]

      [:div
       [:label.ty-text+.block.text-sm.font-medium.mb-1
        "API Key"
        [:span.inline-block.ml-1.ty-text-.cursor-help
         [:ty-icon {:name "info"
                    :size "xs"}]
         [:ty-tooltip
          [:div
           [:div.font-semibold "Where to find your API key:"]
           [:div.text-xs "Settings → API → Generate Key"]]]]]
       [:ty-input {:type "password"
                   :placeholder "sk_live_..."}]]]]

    ;; Navigation Icons
    [:div
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "Navigation & Toolbars"]
     [:div.ty-elevated.rounded-lg.p-3.flex.items-center.justify-between
      [:div.flex.gap-2
       [:ty-button {:action "true"
                    :size "sm"}
        [:ty-icon {:name "home"
                   :size "sm"}]
        [:ty-tooltip "Go to dashboard"]]
       [:ty-button {:action "true"
                    :size "sm"}
        [:ty-icon {:name "settings"
                   :size "sm"}]
        [:ty-tooltip "Settings"]]
       [:ty-button {:action "true"
                    :size "sm"}
        [:ty-icon {:name "bell"
                   :size "sm"}]
        [:ty-tooltip "Notifications"]]]

      [:div.flex.gap-2
       [:ty-button {:action "true"
                    :size "sm"
                    :flavor "primary"}
        [:ty-icon {:name "plus"
                   :size "sm"}]
        [:ty-tooltip "Create new"]]
       [:ty-button {:action "true"
                    :size "sm"}
        [:ty-icon {:name "search"
                   :size "sm"}]
        [:ty-tooltip "Search"]]]]]

    ;; Keyboard Shortcuts
    [:div
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "Keyboard Shortcuts"]
     [:div.flex.gap-3
      [:ty-button
       [:ty-icon {:name "save"
                  :slot "start"}]
       "Save"
       [:ty-tooltip
        [:div.text-center
         [:div "Save Document"]
         [:kbd.text-xs.px-1.py-0.5.bg-black.bg-opacity-20.rounded.text-white "⌘+S"]]]]

      [:ty-button
       [:ty-icon {:name "copy"
                  :slot "start"}]
       "Copy"
       [:ty-tooltip
        [:div.text-center
         [:div "Copy to Clipboard"]
         [:kbd.text-xs.px-1.py-0.5.bg-black.bg-opacity-20.rounded.text-white "⌘+C"]]]]

      [:ty-button
       [:ty-icon {:name "rotate-ccw"
                  :slot "start"}]
       "Undo"
       [:ty-tooltip
        [:div.text-center
         [:div "Undo Last Action"]
         [:kbd.text-xs.px-1.py-0.5.bg-black.bg-opacity-20.rounded.text-white "⌘+Z"]]]]]]]

   ;; Styling Section
   [:h2.text-xl.font-semibold.ty-text++.mb-4.mt-8 "Styling Tooltips"]
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:p.ty-text-.mb-4
     "Since tooltip content is rendered as regular DOM children (via slots), you can style it directly with CSS classes. "
     "The tooltip container itself provides the background and positioning, while you control the content styling."]

    [:div.space-y-4.mb-4
     [:ty-button
      "Custom Styled"
      [:ty-tooltip
       [:div.text-center.py-1
        [:div.text-lg.font-bold.text-yellow-300 "⭐ Premium Feature"]
        [:div.text-xs.text-gray-300 "Available in Pro plan"]]]]

     [:ty-button {:flavor "danger"}
      "Styled Warning"
      [:ty-tooltip {:flavor "danger"}
       [:div
        [:div.flex.items-center.gap-2.mb-1
         [:ty-icon {:name "alert-triangle"
                    :size "sm"}]
         [:span.font-bold.text-sm "Destructive Action"]]
        [:div.text-xs.opacity-90 "This will permanently delete your data"]]]]]

    (code-block
      "<!-- Style tooltip content with regular CSS classes -->
<ty-button>
  Custom Styled
  <ty-tooltip>
    <div class=\"text-center py-1\">
      <div class=\"text-lg font-bold text-yellow-300\">⭐ Premium Feature</div>
      <div class=\"text-xs text-gray-300\">Available in Pro plan</div>
    </div>
  </ty-tooltip>
</ty-button>

<!-- Combine tooltip flavor with custom content styling -->
<ty-button flavor=\"danger\">
  Styled Warning
  <ty-tooltip flavor=\"danger\">
    <div>
      <div class=\"flex items-center gap-2 mb-1\">
        <ty-icon name=\"alert-triangle\" size=\"sm\"></ty-icon>
        <span class=\"font-bold text-sm\">Destructive Action</span>
      </div>
      <div class=\"text-xs opacity-90\">This will permanently delete your data</div>
    </div>
  </ty-tooltip>
</ty-button>" "html")]

   ;; Best Practices Section
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-xl.font-semibold.ty-text++.mb-4 "Best Practices"]
    [:div.space-y-4
     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check-circle"}]
      [:div
       [:div.ty-text+.font-medium "Keep tooltips concise"]
       [:div.ty-text-.text-sm "Use 1-2 short sentences. For longer content, consider a modal or popover."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check-circle"}]
      [:div
       [:div.ty-text+.font-medium "Use semantic flavors"]
       [:div.ty-text-.text-sm "Match tooltip flavor to the action context (danger for destructive, success for positive)."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check-circle"}]
      [:div
       [:div.ty-text+.font-medium "Provide tooltips for icon-only buttons"]
       [:div.ty-text-.text-sm "Always add tooltips to icon buttons to explain their purpose."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check-circle"}]
      [:div
       [:div.ty-text+.font-medium "Consider delay timing"]
       [:div.ty-text-.text-sm "Use shorter delays for frequently used controls, longer for decorative elements."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-danger.mt-0.5 {:name "x-circle"}]
      [:div
       [:div.ty-text+.font-medium "Don't use for critical information"]
       [:div.ty-text-.text-sm "Tooltips are hidden by default. Don't put essential info only in tooltips."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-danger.mt-0.5 {:name "x-circle"}]
      [:div
       [:div.ty-text+.font-medium "Avoid tooltips on mobile-primary interfaces"]
       [:div.ty-text-.text-sm "Touch devices don't have hover. Consider alternatives for mobile users."]]]]]

   ;; Accessibility Note
   [:div.ty-bg-info-.ty-border-info.border.rounded-lg.p-4
    [:h3.ty-text-info++.font-semibold.mb-2 "Accessibility"]
    [:p.ty-text-info.text-sm
     "Tooltips appear on both hover and focus, making them keyboard accessible. "
     "They have appropriate ARIA attributes and don't interfere with screen readers. "
     "The tooltip content is part of the DOM and accessible to assistive technology."]]])
