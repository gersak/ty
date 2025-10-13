(ns ty.site.docs.button
  "Documentation for ty-button component"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table]]))

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Title and Description
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-button"]
    [:p.text-lg.ty-text- "A flexible button component with multiple styles and sizes."]]

   ;; API Reference Card
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text.mb-6 "API Reference"]

    ;; Attributes
    [:div.mb-8
     [:h3.text-lg.font-medium.ty-text+.mb-4 "Attributes"]
     (attribute-table
      [{:name "flavor"
        :type "string"
        :default "\"default\""
        :description "Visual style of the button (default, primary, secondary, danger, success, warning, neutral)"}
       {:name "size"
        :type "string"
        :default "\"md\""
        :description "Size of the button (xs, sm, md, lg, xl)"}
       {:name "disabled"
        :type "boolean"
        :default "false"
        :description "Whether the button is disabled"}
       {:name "action"
        :type "boolean"
        :default "false"
        :description "Makes button circular/icon-only, typically for floating action buttons"}
       {:name "wide"
        :type "boolean"
        :default "false"
        :description "Makes button take more horizontal space"}
       {:name "type"
        :type "string"
        :default "\"button\""
        :description "HTML button type (button, submit, reset)"}
       {:name "name"
        :type "string"
        :default "-"
        :description "Form field name when used in forms"}
       {:name "value"
        :type "string"
        :default "-"
        :description "Form field value when used in forms"}])]

    ;; Events
    [:div.mb-8
     [:h3.text-lg.font-medium.ty-text+.mb-4 "Events"]
     (event-table
      [{:name "click"
        :payload "MouseEvent"
        :when-fired "When button is clicked"}
       {:name "focus"
        :payload "FocusEvent"
        :when-fired "When button receives focus"}
       {:name "blur"
        :payload "FocusEvent"
        :when-fired "When button loses focus"}])]

    ;; Slots
    [:div
     [:h3.text-lg.font-medium.ty-text+.mb-4 "Slots"]
     [:div.overflow-x-auto
      [:table.w-full
       [:thead
        [:tr.border-b.ty-border
         [:th.text-left.px-4.py-2.ty-text+ "Slot"]
         [:th.text-left.px-4.py-2.ty-text+ "Description"]]]
       [:tbody
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "start"]
         [:td.px-4.py-2.ty-text-.text-sm "Content placed before the button text (typically an icon)"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "(default)"]
         [:td.px-4.py-2.ty-text-.text-sm "Main button content"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "end"]
         [:td.px-4.py-2.ty-text-.text-sm "Content placed after the button text (typically an icon)"]]]]]]]

;; Basic Usage
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Basic Usage"]
    (code-block "<ty-button>Click me</ty-button>")
    [:div.mt-4
     [:ty-button "Try it out"]]]

   ;; Examples Section
   [:h2.text-2xl.font-semibold.ty-text.mb-6 "Examples"]

   [:div.space-y-8
    ;; Button Flavors
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Button Flavors"]
     [:div.flex.flex-wrap.gap-3.mb-4
      [:ty-button "Default"]
      [:ty-button {:flavor "primary"} "Primary"]
      [:ty-button {:flavor "secondary"} "Secondary"]
      [:ty-button {:flavor "danger"} "Danger"]
      [:ty-button {:flavor "success"} "Success"]
      [:ty-button {:flavor "warning"} "Warning"]
      [:ty-button {:flavor "neutral"} "Neutral"]]
     (code-block "<ty-button>Default</ty-button>
<ty-button flavor=\"primary\">Primary</ty-button>
<ty-button flavor=\"secondary\">Secondary</ty-button>
<ty-button flavor=\"danger\">Danger</ty-button>
<ty-button flavor=\"success\">Success</ty-button>
<ty-button flavor=\"warning\">Warning</ty-button>
<ty-button flavor=\"neutral\">Neutral</ty-button>")]

    ;; Button Sizes
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Button Sizes"]
     [:div.flex.flex-wrap.items-center.gap-3.mb-4
      [:ty-button {:size "xs"} "Extra Small"]
      [:ty-button {:size "sm"} "Small"]
      [:ty-button "Default"]
      [:ty-button {:size "lg"} "Large"]
      [:ty-button {:size "xl"} "Extra Large"]]
     (code-block "<ty-button size=\"xs\">Extra Small</ty-button>
<ty-button size=\"sm\">Small</ty-button>
<ty-button>Default</ty-button>
<ty-button size=\"lg\">Large</ty-button>
<ty-button size=\"xl\">Extra Large</ty-button>")]

    ;; Action Buttons (Icon-only)
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Action Buttons (Icon-only)"]
     [:p.ty-text-.text-sm.mb-4
      "Action buttons are circular icon-only buttons, perfect for floating action buttons or toolbar actions."]
     [:div.flex.flex-wrap.items-center.gap-3.mb-4
      [:ty-button {:action true :flavor "primary" :size "lg"}
       [:ty-icon {:name "plus" :size "md"}]]
      [:ty-button {:action true :flavor "secondary"}
       [:ty-icon {:name "edit" :size "sm"}]]
      [:ty-button {:action true :flavor "danger"}
       [:ty-icon {:name "trash" :size "sm"}]]
      [:ty-button {:action true :flavor "success"}
       [:ty-icon {:name "check" :size "sm"}]]
      [:ty-button {:action true :size "xs"}
       [:ty-icon {:name "x" :size "xs"}]]]
     (code-block "<!-- Large Primary FAB -->
<ty-button action=\"true\" flavor=\"primary\" size=\"lg\">
  <ty-icon name=\"plus\" size=\"md\"></ty-icon>
</ty-button>

<!-- Regular Action Buttons -->
<ty-button action=\"true\" flavor=\"secondary\">
  <ty-icon name=\"edit\" size=\"sm\"></ty-icon>
</ty-button>

<ty-button action=\"true\" flavor=\"danger\">
  <ty-icon name=\"trash\" size=\"sm\"></ty-icon>
</ty-button>

<!-- Small Close Button -->
<ty-button action=\"true\" size=\"xs\">
  <ty-icon name=\"x\" size=\"xs\"></ty-icon>
</ty-button>")]

    ;; Button with Icons
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Buttons with Icons"]
     [:div.flex.flex-wrap.gap-3.mb-4
      [:ty-button {:flavor "primary"}
       [:ty-icon {:slot "start" :name "save" :size "sm"}]
       "Save"]
      [:ty-button {:flavor "secondary"}
       [:ty-icon {:slot "start" :name "download" :size "sm"}]
       "Download"]
      [:ty-button {:flavor "danger"}
       [:ty-icon {:slot "start" :name "trash" :size "sm"}]
       "Delete"]
      [:ty-button
       "Next"
       [:ty-icon {:slot "end" :name "arrow-right" :size "sm"}]]]
     (code-block "<ty-button flavor=\"primary\">
  <ty-icon slot=\"start\" name=\"save\" size=\"sm\"></ty-icon>
  Save
</ty-button>

<ty-button>
  Next
  <ty-icon slot=\"end\" name=\"arrow-right\" size=\"sm\"></ty-icon>
</ty-button>")]

    ;; Button States
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Button States"]
     [:div.flex.flex-wrap.gap-3.mb-4
      [:ty-button {:disabled true} "Disabled"]
      [:ty-button {:flavor "primary"}
       [:ty-icon {:slot "start" :name "loader-2" :size "sm" :spin true}]
       "Loading..."]
      [:ty-button {:flavor "secondary"}
       [:ty-icon {:slot "start" :name "refresh-cw" :size "sm" :spin true}]
       "Processing"]
      [:ty-button {:flavor "success" :disabled true} "Success Disabled"]]
     (code-block "<ty-button disabled>Disabled</ty-button>

<!-- Loading state with spinning icon -->
<ty-button flavor=\"primary\">
  <ty-icon slot=\"start\" name=\"loader-2\" size=\"sm\" spin=\"true\"></ty-icon>
  Loading...
</ty-button>

<ty-button flavor=\"success\" disabled>Success Disabled</ty-button>")]

    ;; Loading State Patterns
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Loading State Patterns"]
     [:p.ty-text-.text-sm.mb-4
      "Create loading states using spinning icons. Perfect for async operations."]

     [:div.space-y-4
      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Standard Loading"]
       [:div.flex.gap-3.mb-3
        [:ty-button {:flavor "primary"}
         [:ty-icon {:slot "start" :name "loader-2" :size "sm" :spin true}]
         "Saving..."]
        [:ty-button {:flavor "secondary" :disabled true}
         [:ty-icon {:slot "start" :name "refresh-cw" :size "sm" :spin true}]
         "Syncing..."]]
       (code-block "<ty-button flavor=\"primary\">
  <ty-icon slot=\"start\" name=\"loader-2\" size=\"sm\" spin=\"true\"></ty-icon>
  Saving...
</ty-button>")]

      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Action Button Loading"]
       [:div.flex.gap-3.mb-3
        [:ty-button {:action true :flavor "primary"}
         [:ty-icon {:name "loader-2" :size "sm" :spin true}]]
        [:ty-button {:action true :flavor "secondary" :disabled true}
         [:ty-icon {:name "refresh-cw" :size "sm" :spin true}]]]
       (code-block "<ty-button action=\"true\" flavor=\"primary\">
  <ty-icon name=\"loader-2\" size=\"sm\" spin=\"true\"></ty-icon>
</ty-button>")]

      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Available Spinning Icons"]
       [:div.flex.flex-wrap.gap-3.mb-3
        [:ty-button {:flavor "neutral"}
         [:ty-icon {:slot "start" :name "loader" :size "sm" :spin true}]
         "loader"]
        [:ty-button {:flavor "neutral"}
         [:ty-icon {:slot "start" :name "loader-2" :size "sm" :spin true}]
         "loader-2"]
        [:ty-button {:flavor "neutral"}
         [:ty-icon {:slot "start" :name "refresh-cw" :size "sm" :spin true}]
         "refresh-cw"]]]]]]

   ;; Use Cases Section
   [:h2.text-2xl.font-semibold.ty-text.mb-6.mt-12 "Common Use Cases"]

   [:div.space-y-8
    ;; Floating Action Button
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Floating Action Button (FAB)"]
     [:p.ty-text-.text-sm.mb-4
      "Use action buttons with primary flavor and large size for main floating actions."]
     [:div.relative.h-32.ty-elevated.rounded-lg.flex.items-end.justify-end.p-4.mb-4
      [:ty-button {:action true :flavor "primary" :size "lg"
                   :style "position: absolute; bottom: 1rem; right: 1rem;"}
       [:ty-icon {:name "plus" :size "md"}]]]
     (code-block "<!-- Positioned FAB -->
<div class=\"relative\">
  <!-- Your content -->
  <ty-button action=\"true\" flavor=\"primary\" size=\"lg\" 
             class=\"absolute bottom-4 right-4\">
    <ty-icon name=\"plus\" size=\"md\"></ty-icon>
  </ty-button>
</div>")]

    ;; Toolbar Actions
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Toolbar Actions"]
     [:p.ty-text-.text-sm.mb-4
      "Combine text buttons with action buttons in toolbars."]
     [:div.ty-elevated.rounded.p-3.flex.items-center.justify-between.mb-4
      [:div.flex.gap-2
       [:ty-button {:size "sm"} "Edit"]
       [:ty-button {:size "sm"} "Share"]]
      [:div.flex.gap-1
       [:ty-button {:action true :size "sm"}
        [:ty-icon {:name "search" :size "xs"}]]
       [:ty-button {:action true :size "sm"}
        [:ty-icon {:name "filter" :size "xs"}]]
       [:ty-button {:action true :size "sm"}
        [:ty-icon {:name "more-vertical" :size "xs"}]]]]
     (code-block "<div class=\"flex items-center justify-between\">
  <div class=\"flex gap-2\">
    <ty-button size=\"sm\">Edit</ty-button>
    <ty-button size=\"sm\">Share</ty-button>
  </div>
  <div class=\"flex gap-1\">
    <ty-button action=\"true\" size=\"sm\">
      <ty-icon name=\"search\" size=\"xs\"></ty-icon>
    </ty-button>
    <ty-button action=\"true\" size=\"sm\">
      <ty-icon name=\"more-vertical\" size=\"xs\"></ty-icon>
    </ty-button>
  </div>
</div>")]]

   ;; Best Practices
   [:div.ty-elevated.rounded-lg.p-6.mt-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Best Practices"]
    [:div.space-y-3
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:p.ty-text- "Use semantic flavors (primary, danger) to convey meaning"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:p.ty-text- "Show loading states with spinning icons for async operations"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:p.ty-text- "Use action buttons for icon-only actions to save space"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:p.ty-text- "Disable buttons during loading to prevent multiple submissions"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:p.ty-text- "Use slots for icons to maintain proper spacing"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-danger.mt-0.5 {:name "x" :size "sm"}]
      [:p.ty-text- "Don't use multiple buttons with the same primary action"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-danger.mt-0.5 {:name "x" :size "sm"}]
      [:p.ty-text- "Avoid using more than one primary button per section"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-danger.mt-0.5 {:name "x" :size "sm"}]
      [:p.ty-text- "Don't use action buttons for text-heavy actions"]]]]])