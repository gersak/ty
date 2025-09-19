(ns ty.site.docs.button
  "Documentation for ty-button component"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table]]))

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Title
   [:h1.text-3xl.font-bold.ty-text.mb-4 "ty-button"]
   [:p.text-lg.ty-text-.mb-8 "A flexible button component with multiple styles and sizes."]

   ;; Basic Usage
   [:section.mb-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Basic Usage"]
    (code-block "<ty-button>Click me</ty-button>")]

   ;; Live Demo
   [:section.mb-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Live Examples"]
    [:div.space-y-4
     [:div
      [:h3.text-lg.font-medium.ty-text.mb-2 "Button Flavors"]
      [:div.flex.flex-wrap.gap-3.mb-4
       [:ty-button "Default"]
       [:ty-button {:flavor "primary"} "Primary"]
       [:ty-button {:flavor "secondary"} "Secondary"]
       [:ty-button {:flavor "danger"} "Danger"]
       [:ty-button {:flavor "success"} "Success"]]
      (code-block "<ty-button>Default</ty-button>
<ty-button flavor=\"primary\">Primary</ty-button>
<ty-button flavor=\"secondary\">Secondary</ty-button>
<ty-button flavor=\"danger\">Danger</ty-button>
<ty-button flavor=\"success\">Success</ty-button>")]

     [:div
      [:h3.text-lg.font-medium.ty-text.mb-2 "Button Sizes"]
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

     [:div
      [:h3.text-lg.font-medium.ty-text.mb-2 "Button with Icons"]
      [:div.flex.flex-wrap.gap-3.mb-4
       [:ty-button {:flavor "primary"}
        [:ty-icon {:slot "start"
                   :name "save"
                   :size "sm"}]
        "Save"]
       [:ty-button {:flavor "secondary"}
        [:ty-icon {:slot "start"
                   :name "download"
                   :size "sm"}]
        "Download"]
       [:ty-button {:flavor "danger"}
        [:ty-icon {:slot "start"
                   :name "trash"
                   :size "sm"}]
        "Delete"]
       [:ty-button
        "Next"
        [:ty-icon {:slot "end"
                   :name "arrow-right"
                   :size "sm"}]]]
      (code-block "<ty-button flavor=\"primary\">
  <ty-icon slot=\"start\" name=\"save\" size=\"sm\"></ty-icon>
  Save
</ty-button>

<ty-button>
  Next
  <ty-icon slot=\"end\" name=\"arrow-right\" size=\"sm\"></ty-icon>
</ty-button>")]

     [:div
      [:h3.text-lg.font-medium.ty-text.mb-2 "Button States"]
      [:div.flex.flex-wrap.gap-3.mb-4
       [:ty-button {:disabled true} "Disabled"]
       [:ty-button {:flavor "primary"}
        [:ty-icon {:slot "start"
                   :name "loader-2"
                   :size "sm"
                   :spin true}]
        "Loading..."]
       [:ty-button {:flavor "secondary"}
        [:ty-icon {:slot "start"
                   :name "refresh"
                   :size "sm"
                   :spin true}]
        "Processing"]
       [:ty-button {:flavor "success"
                    :disabled true} "Success Disabled"]]
      (code-block "<ty-button disabled>Disabled</ty-button>

<!-- Loading state with spinning icon -->
<ty-button flavor=\"primary\">
  <ty-icon slot=\"start\" name=\"loader-2\" size=\"sm\" spin=\"true\"></ty-icon>
  Loading...
</ty-button>

<ty-button flavor=\"secondary\">
  <ty-icon slot=\"start\" name=\"refresh-cw\" size=\"sm\" spin=\"true\"></ty-icon>
  Processing
</ty-button>

<ty-button flavor=\"success\" disabled>Success Disabled</ty-button>")]]]

   ;; Attributes
   [:section#attributes.mb-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Attributes"]
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
   [:section#events.mb-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Events"]
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
   [:section#slots.mb-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Slots"]
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
        [:td.px-4.py-2.ty-text-.text-sm "Content placed after the button text (typically an icon)"]]]]]]

   ;; Loading State Pattern
   [:section.mb-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Loading State Pattern"]
    [:p.ty-text-.mb-4
     "To create a loading state, use a spinning icon in the start slot. Common patterns include:"]
    [:div.space-y-4
     [:div
      [:h3.text-md.font-medium.ty-text.mb-2 "Simple Loading"]
      [:div.flex.gap-3.mb-4
       [:ty-button {:flavor "primary"}
        [:ty-icon {:slot "start"
                   :name "loader-2"
                   :size "sm"
                   :spin true}]
        "Saving..."]]
      (code-block "<ty-button flavor=\"primary\">
  <ty-icon slot=\"start\" name=\"loader-2\" size=\"sm\" spin=\"true\"></ty-icon>
  Saving...
</ty-button>")]

     [:div
      [:h3.text-md.font-medium.ty-text.mb-2 "Icon-only Loading"]
      [:div.flex.gap-3.mb-4
       [:ty-button {:flavor "primary"
                    :size "sm"}
        [:ty-icon {:name "loader-2"
                   :size "sm"
                   :spin true}]]]
      (code-block "<ty-button flavor=\"primary\" size=\"sm\">
  <ty-icon name=\"loader-2\" size=\"sm\" spin=\"true\"></ty-icon>
</ty-button>")]

     [:div
      [:h3.text-md.font-medium.ty-text.mb-2 "Loading with Disabled State"]
      [:div.flex.gap-3.mb-4
       [:ty-button {:flavor "primary"
                    :disabled true}
        [:ty-icon {:slot "start"
                   :name "refresh-cw"
                   :size "sm"
                   :spin true}]
        "Processing..."]]
      (code-block "<ty-button flavor=\"primary\" disabled>
  <ty-icon slot=\"start\" name=\"refresh-cw\" size=\"sm\" spin=\"true\"></ty-icon>
  Processing...
</ty-button>")]

     [:div
      [:h3.text-md.font-medium.ty-text.mb-2 "Different Loading Icons"]
      [:div.flex.flex-wrap.gap-3.mb-4
       [:ty-button {:flavor "neutral"}
        [:ty-icon {:slot "start"
                   :name "loader"
                   :size "sm"
                   :spin true}]
        "loader"]
       [:ty-button {:flavor "neutral"}
        [:ty-icon {:slot "start"
                   :name "loader-2"
                   :size "sm"
                   :spin true}]
        "loader-2"]
       [:ty-button {:flavor "neutral"}
        [:ty-icon {:slot "start"
                   :name "refresh-cw"
                   :size "sm"
                   :spin true}]
        "refresh-cw"]]
      (code-block "<!-- Available spinning icons for loading states -->
<ty-icon name=\"loader\" spin=\"true\"></ty-icon>
<ty-icon name=\"loader-2\" spin=\"true\"></ty-icon>  
<ty-icon name=\"refresh-cw\" spin=\"true\"></ty-icon>")]]]

   ;; Best Practices
   [:section.mb-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Best Practices"]
    [:div.space-y-3
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5
       {:name "check"
        :size "sm"}]
      [:p.ty-text- "Use semantic flavors (primary, danger) to convey meaning"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5
       {:name "check"
        :size "sm"}]
      [:p.ty-text- "Show loading states with spinning icons for async operations"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5
       {:name "check"
        :size "sm"}]
      [:p.ty-text- "Use slots for icons to maintain proper spacing"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5
       {:name "check"
        :size "sm"}]
      [:p.ty-text- "Disable buttons during loading to prevent multiple submissions"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-danger.mt-0.5
       {:name "x"
        :size "sm"}]
      [:p.ty-text- "Don't use multiple buttons with the same primary action"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-danger.mt-0.5
       {:name "x"
        :size "sm"}]
      [:p.ty-text- "Avoid using more than one primary button per section"]]]]])
