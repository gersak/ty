(ns ty.site.docs.icon
  "Documentation for ty-icon component - dynamic icon rendering with animations"
  (:require
   [ty.site.docs.common :refer [code-block attribute-table event-table doc-section example-section]]))

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Header
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text++.mb-2 "ty-icon"]
    [:p.text-lg.ty-text-
     "Dynamic icon rendering with size variants and animation effects. Loads icons from the registry and supports hot reloading during development."]]

   ;; API Reference
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text++.mb-6 "API Reference"]

    [:div.space-y-6
     ;; Attributes
     [:div
      [:h3.text-lg.font-medium.ty-text+.mb-3 "Attributes"]
      (attribute-table
       [{:name "name"
         :type "string"
         :default "-"
         :description "Icon name from the registry (e.g., 'check', 'plus', 'trash')"}
        {:name "size"
         :type "string"
         :default "-"
         :description "Icon size: xs, sm, md, lg, xl"}
        {:name "spin"
         :type "boolean"
         :default "false"
         :description "Apply continuous rotation animation"}
        {:name "pulse"
         :type "boolean"
         :default "false"
         :description "Apply pulsing animation"}
        {:name "tempo"
         :type "string"
         :default "-"
         :description "Animation speed: slow, normal, fast"}
        {:name "class"
         :type "string"
         :default "-"
         :description "Additional CSS classes (e.g., 'ty-text-success')"}])]]]

   ;; Basic Usage
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-xl.font-semibold.ty-text++.mb-4 "Basic Usage"]
    [:p.ty-text-.mb-4 "Icons automatically inherit text color from their container or can be styled directly."]

    [:div.mb-6
     [:div.flex.items-center.gap-4.mb-4
      [:ty-icon {:name "check" :size "sm"}]
      [:ty-icon {:name "plus" :size "md"}]
      [:ty-icon {:name "trash" :size "lg"}]
      [:ty-icon {:name "settings" :size "xl"}]]

     (code-block
      "<ty-icon name=\"check\" size=\"sm\"></ty-icon>
<ty-icon name=\"plus\" size=\"md\"></ty-icon>
<ty-icon name=\"trash\" size=\"lg\"></ty-icon>
<ty-icon name=\"settings\" size=\"xl\"></ty-icon>")]]

   ;; Examples Section
   [:h2.text-2xl.font-bold.ty-text++.mb-6 "Examples"]
   [:div.space-y-8

    ;; Sizes
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text+.mb-4 "Icon Sizes"]
     [:div.flex.items-center.gap-6.mb-4
      [:div.text-center
       [:ty-icon.ty-text {:name "star" :size "xs"}]
       [:p.ty-text--.text-xs.mt-1 "xs"]]
      [:div.text-center
       [:ty-icon.ty-text {:name "star" :size "sm"}]
       [:p.ty-text--.text-xs.mt-1 "sm"]]
      [:div.text-center
       [:ty-icon.ty-text {:name "star" :size "md"}]
       [:p.ty-text--.text-xs.mt-1 "md"]]
      [:div.text-center
       [:ty-icon.ty-text {:name "star" :size "lg"}]
       [:p.ty-text--.text-xs.mt-1 "lg"]]
      [:div.text-center
       [:ty-icon.ty-text {:name "star" :size "xl"}]
       [:p.ty-text--.text-xs.mt-1 "xl"]]]

     (code-block
      "<ty-icon name=\"star\" size=\"xs\"></ty-icon>
<ty-icon name=\"star\" size=\"sm\"></ty-icon>
<ty-icon name=\"star\" size=\"md\"></ty-icon>
<ty-icon name=\"star\" size=\"lg\"></ty-icon>
<ty-icon name=\"star\" size=\"xl\"></ty-icon>")]

    ;; Semantic Colors
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text+.mb-4 "Semantic Colors"]
     [:div.flex.items-center.gap-4.mb-4
      [:ty-icon.ty-text-primary {:name "info" :size "md"}]
      [:ty-icon.ty-text-success {:name "check-circle" :size "md"}]
      [:ty-icon.ty-text-warning {:name "alert-triangle" :size "md"}]
      [:ty-icon.ty-text-danger {:name "x-circle" :size "md"}]
      [:ty-icon.ty-text-secondary {:name "settings" :size "md"}]]

     (code-block
      "<ty-icon class=\"ty-text-primary\" name=\"info\" size=\"md\"></ty-icon>
<ty-icon class=\"ty-text-success\" name=\"check-circle\" size=\"md\"></ty-icon>
<ty-icon class=\"ty-text-warning\" name=\"alert-triangle\" size=\"md\"></ty-icon>
<ty-icon class=\"ty-text-danger\" name=\"x-circle\" size=\"md\"></ty-icon>
<ty-icon class=\"ty-text-secondary\" name=\"settings\" size=\"md\"></ty-icon>")]

    ;; Animations
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text+.mb-4 "Animations"]
     [:div.flex.items-center.gap-6.mb-4
      [:div.text-center
       [:ty-icon.ty-text-primary {:name "loader" :size "md" :spin true}]
       [:p.ty-text--.text-xs.mt-2 "Spin"]]
      [:div.text-center
       [:ty-icon.ty-text-success {:name "heart" :size "md" :pulse true}]
       [:p.ty-text--.text-xs.mt-2 "Pulse"]]
      [:div.text-center
       [:ty-icon.ty-text-warning {:name "refresh-cw" :size "md" :spin true :tempo "slow"}]
       [:p.ty-text--.text-xs.mt-2 "Slow Spin"]]
      [:div.text-center
       [:ty-icon.ty-text-danger {:name "zap" :size "md" :pulse true :tempo "fast"}]
       [:p.ty-text--.text-xs.mt-2 "Fast Pulse"]]]

     (code-block
      "<ty-icon name=\"loader\" spin=\"true\"></ty-icon>
<ty-icon name=\"heart\" pulse=\"true\"></ty-icon>
<ty-icon name=\"refresh-cw\" spin=\"true\" tempo=\"slow\"></ty-icon>
<ty-icon name=\"zap\" pulse=\"true\" tempo=\"fast\"></ty-icon>")]

    ;; In Buttons
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text+.mb-4 "Icons in Buttons"]
     [:div.flex.items-center.gap-3.mb-4
      [:ty-button {:flavor "primary" :size "sm"}
       [:ty-icon {:slot "start" :name "save" :size "sm"}]
       "Save"]
      [:ty-button {:flavor "secondary" :size "sm"}
       [:ty-icon {:slot "start" :name "download" :size "sm"}]
       "Export"]
      [:ty-button {:flavor "danger" :size "sm"}
       [:ty-icon {:slot "start" :name "trash" :size "sm"}]
       "Delete"]
      [:ty-button {:flavor "neutral" :size "sm"}
       "Next"
       [:ty-icon {:slot "end" :name "arrow-right" :size "sm"}]]]

     (code-block
      "<ty-button flavor=\"primary\">
  <ty-icon slot=\"start\" name=\"save\" size=\"sm\"></ty-icon>
  Save
</ty-button>

<ty-button flavor=\"neutral\">
  Next
  <ty-icon slot=\"end\" name=\"arrow-right\" size=\"sm\"></ty-icon>
</ty-button>")]]

   ;; JavaScript Integration
   [:h2.text-2xl.font-bold.ty-text++.mb-6 "JavaScript Integration"]
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h3.text-lg.font-semibold.ty-text+.mb-4 "Working with Icons in JavaScript"]
    [:p.ty-text-.mb-4 "Icons can be created and manipulated dynamically using JavaScript."]

    (code-block
     "// Create an icon element
const icon = document.createElement('ty-icon');
icon.setAttribute('name', 'star');
icon.setAttribute('size', 'md');
icon.className = 'ty-text-warning';

// Add to DOM
document.body.appendChild(icon);

// Change icon dynamically
icon.setAttribute('name', 'heart');
icon.setAttribute('spin', 'true');

// Remove animation
icon.removeAttribute('spin');

// Common patterns
function createLoadingButton() {
  const button = document.createElement('ty-button');
  const icon = document.createElement('ty-icon');
  
  icon.setAttribute('name', 'loader');
  icon.setAttribute('spin', 'true');
  icon.setAttribute('slot', 'start');
  icon.setAttribute('size', 'sm');
  
  button.appendChild(icon);
  button.appendChild(document.createTextNode('Loading...'));
  button.setAttribute('disabled', 'true');
  
  return button;
}

// Status indicators
function createStatusIcon(status) {
  const icon = document.createElement('ty-icon');
  
  switch(status) {
    case 'success':
      icon.setAttribute('name', 'check-circle');
      icon.className = 'ty-text-success';
      break;
    case 'error':
      icon.setAttribute('name', 'x-circle');
      icon.className = 'ty-text-danger';
      break;
    case 'warning':
      icon.setAttribute('name', 'alert-triangle');
      icon.className = 'ty-text-warning';
      break;
    case 'loading':
      icon.setAttribute('name', 'loader');
      icon.setAttribute('spin', 'true');
      icon.className = 'ty-text-primary';
      break;
  }
  
  icon.setAttribute('size', 'md');
  return icon;
}"
     :lang "javascript")]

   ;; Common Use Cases
   [:h2.text-2xl.font-bold.ty-text++.mb-6 "Common Use Cases"]
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h3.text-lg.font-semibold.ty-text+.mb-4 "Status Indicators"]
    [:div.space-y-3.mb-4
     [:div.flex.items-center.gap-2
      [:ty-icon.ty-text-success {:name "check-circle" :size "sm"}]
      [:span.ty-text "Operation completed successfully"]]
     [:div.flex.items-center.gap-2
      [:ty-icon.ty-text-danger {:name "x-circle" :size "sm"}]
      [:span.ty-text "Error: Invalid input"]]
     [:div.flex.items-center.gap-2
      [:ty-icon.ty-text-warning {:name "alert-triangle" :size "sm"}]
      [:span.ty-text "Warning: Limited availability"]]
     [:div.flex.items-center.gap-2
      [:ty-icon.ty-text-primary {:name "info" :size "sm"}]
      [:span.ty-text "Note: Updates available"]]]

    [:h3.text-lg.font-semibold.ty-text+.mb-4.mt-6 "Loading States"]
    [:div.flex.items-center.gap-4.mb-4
     [:ty-button {:flavor "primary" :disabled true}
      [:ty-icon {:slot "start" :name "loader" :size "sm" :spin true}]
      "Processing..."]
     [:ty-button {:flavor "secondary" :disabled true}
      [:ty-icon {:slot "start" :name "refresh-cw" :size "sm" :spin true}]
      "Syncing..."]
     [:div.flex.items-center.gap-2
      [:ty-icon.ty-text-primary {:name "loader" :size "md" :spin true}]
      [:span.ty-text "Loading content..."]]]

    [:h3.text-lg.font-semibold.ty-text+.mb-4.mt-6 "Navigation & Actions"]
    [:div.flex.items-center.gap-3
     [:ty-button {:flavor "neutral" :size "xs" :action true}
      [:ty-icon {:name "menu" :size "xs"}]]
     [:ty-button {:flavor "neutral" :size "xs" :action true}
      [:ty-icon {:name "search" :size "xs"}]]
     [:ty-button {:flavor "neutral" :size "xs" :action true}
      [:ty-icon {:name "filter" :size "xs"}]]
     [:ty-button {:flavor "neutral" :size "xs" :action true}
      [:ty-icon {:name "more-vertical" :size "xs"}]]]]

   ;; Best Practices
   [:div.ty-elevated.rounded-lg.p-6
    [:h2.text-2xl.font-semibold.ty-text++.mb-4 "Best Practices"]
    [:div.space-y-4
     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:div
       [:p.ty-text+.font-medium "Use semantic colors for meaning"]
       [:p.ty-text-.text-sm "Apply ty-text-success, ty-text-danger, etc. to convey status"]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:div
       [:p.ty-text+.font-medium "Match icon size to context"]
       [:p.ty-text-.text-sm "Use sm/xs for inline text, md/lg for headers, xl for hero sections"]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:div
       [:p.ty-text+.font-medium "Use animations purposefully"]
       [:p.ty-text-.text-sm "Spin for loading states, pulse for attention, avoid overuse"]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-danger.mt-0.5 {:name "x" :size "sm"}]
      [:div
       [:p.ty-text+.font-medium "Don't add margins to icons in buttons"]
       [:p.ty-text-.text-sm "Buttons handle icon spacing automatically with slots"]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-danger.mt-0.5 {:name "x" :size "sm"}]
      [:div
       [:p.ty-text+.font-medium "Avoid mixing animation types"]
       [:p.ty-text-.text-sm "Don't use both spin and pulse on the same icon"]]]]]])