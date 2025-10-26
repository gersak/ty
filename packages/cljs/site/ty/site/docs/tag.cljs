(ns ty.site.docs.tag
  "Documentation for the ty-tag component"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table doc-section example-section]]))

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Title and Description
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-tag"]
    [:p.text-lg.ty-text-
     "A versatile tag component for displaying labels, badges, and removable chips. "
     "Tags support semantic flavors, multiple sizes, and can be interactive with click and dismiss events."]]

   ;; API Reference Card
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text++.mb-4 "API Reference"]

    ;; Attributes Table
    [:div.mb-6
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "Attributes"]
     (attribute-table
       [{:name "flavor"
         :type "string"
         :default "\"neutral\""
         :description "Semantic color theme: primary, secondary, success, danger, warning, neutral"}
        {:name "size"
         :type "string"
         :default "\"md\""
         :description "Tag size: sm, md, lg"}
        {:name "pill"
         :type "boolean"
         :default "true"
         :description "Use pill shape (fully rounded) instead of rectangular with rounded corners"}
        {:name "not-pill"
         :type "boolean"
         :default "false"
         :description "Alternative way to disable pill shape (equivalent to pill=\"false\")"}
        {:name "clickable"
         :type "boolean"
         :default "false"
         :description "Makes the tag clickable with hover/active states"}
        {:name "dismissible"
         :type "boolean"
         :default "false"
         :description "Shows a dismiss button (X) that triggers dismiss event"}
        {:name "disabled"
         :type "boolean"
         :default "false"
         :description "Disables all interactions"}
        {:name "value"
         :type "string"
         :default "null"
         :description "Value for the tag (useful in multiselect contexts)"}
        {:name "selected"
         :type "boolean"
         :default "false"
         :description "Selected state (primarily for multiselect integration)"}])]

    ;; Events Table
    [:div.mb-6
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "Events"]
     (event-table
       [{:name "pointerdown"
         :payload "{target: HTMLElement}"
         :when-fired "Fired when a clickable tag is clicked"}
        {:name "click"
         :payload "{target: HTMLElement}"
         :when-fired "Fired when a clickable tag is clicked"}
        {:name "dismiss"
         :payload "{target: HTMLElement}"
         :when-fired "Fired when the dismiss button is clicked"}])]

    ;; Slots Table
    [:div
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "Slots"]
     [:div.ty-bg-neutral-.rounded.p-4.overflow-x-auto
      [:table.w-full.text-sm
       [:thead
        [:tr.border-b.ty-border
         [:th.text-left.p-2.ty-text+ "Slot"]
         [:th.text-left.p-2.ty-text+ "Description"]]]
       [:tbody
        [:tr.border-b.ty-border-
         [:td.p-2.font-mono.text-xs "default"]
         [:td.p-2.ty-text "Main tag content"]]
        [:tr.border-b.ty-border-
         [:td.p-2.font-mono.text-xs "start"]
         [:td.p-2.ty-text "Content before the main text (icons, emojis)"]]
        [:tr
         [:td.p-2.font-mono.text-xs "end"]
         [:td.p-2.ty-text "Content after the main text (badges, counts)"]]]]]]]


   ;; Examples Section
   [:h2.text-2xl.font-semibold.ty-text++.mb-6 "Examples"]

   [:div.space-y-8
    ;; Semantic Flavors
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-xl.font-semibold.ty-text+.mb-4 "Semantic Flavors"]
     [:p.ty-text-.mb-4 "All available semantic color themes:"]

     [:div.mb-4.p-4.ty-bg-neutral-.rounded
      [:div.flex.flex-wrap.gap-3
       [:ty-tag {:flavor "primary"} "Primary"]
       [:ty-tag {:flavor "secondary"} "Secondary"]
       [:ty-tag {:flavor "success"} "Success"]
       [:ty-tag {:flavor "danger"} "Danger"]
       [:ty-tag {:flavor "warning"} "Warning"]
       [:ty-tag {:flavor "neutral"} "Neutral"]]]

     (code-block
       "<ty-tag flavor=\"primary\">Primary</ty-tag>
<ty-tag flavor=\"secondary\">Secondary</ty-tag>
<ty-tag flavor=\"success\">Success</ty-tag>
<ty-tag flavor=\"danger\">Danger</ty-tag>
<ty-tag flavor=\"warning\">Warning</ty-tag>
<ty-tag flavor=\"neutral\">Neutral</ty-tag>")]

    ;; Sizes
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-xl.font-semibold.ty-text+.mb-4 "Tag Sizes"]
     [:p.ty-text-.mb-4 "Three sizes available for different contexts:"]

     [:div.mb-4.p-4.ty-bg-neutral-.rounded
      [:div.flex.flex-wrap.items-center.gap-3
       [:ty-tag {:size "sm"
                 :flavor "primary"} "Small"]
       [:ty-tag {:size "md"
                 :flavor "primary"} "Medium (default)"]
       [:ty-tag {:size "lg"
                 :flavor "primary"} "Large"]]]

     (code-block
       "<ty-tag size=\"sm\" flavor=\"primary\">Small</ty-tag>
<ty-tag size=\"md\" flavor=\"primary\">Medium (default)</ty-tag>
<ty-tag size=\"lg\" flavor=\"primary\">Large</ty-tag>")]

    ;; Pill vs Rectangular
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-xl.font-semibold.ty-text+.mb-4 "Shape Variants"]
     [:p.ty-text-.mb-4 "Tags default to pill shape but can be rectangular:"]

     [:div.mb-4.p-4.ty-bg-neutral-.rounded
      [:div.flex.flex-wrap.gap-3
       [:ty-tag {:flavor "primary"} "Pill (default)"]
       [:ty-tag {:flavor "primary"
                 :pill "false"} "Rectangular"]
       [:ty-tag {:flavor "success"
                 :not-pill "true"} "Also Rectangular"]]]

     (code-block
       "<!-- Pill shape (default) -->
<ty-tag flavor=\"primary\">Pill (default)</ty-tag>

<!-- Rectangular shape -->
<ty-tag flavor=\"primary\" pill=\"false\">Rectangular</ty-tag>
<ty-tag flavor=\"success\" not-pill=\"true\">Also Rectangular</ty-tag>")]

    ;; Interactive Tags
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-xl.font-semibold.ty-text+.mb-4 "Interactive Tags"]
     [:p.ty-text-.mb-4 "Tags can be clickable, dismissible, or both:"]

     [:div.mb-4.p-4.ty-bg-neutral-.rounded
      [:div.flex.flex-wrap.gap-3
       [:ty-tag
        {:flavor "primary"
         :clickable "true"
         :on {:pointerdown
              #(js/alert "Tag clicked")}}
        "Click me"]
       [:ty-tag#dismiss-demo
        {:flavor "danger"
         :dismissible "true"
         :on {:dismiss (fn [^js e]
                                (.remove (.-target e)))}}
        "Dismiss me"]
       [:ty-tag {:flavor "success"
                 :clickable "true"
                 :dismissible "true"}
        "Both"]
       [:ty-tag {:flavor "warning"
                 :clickable "true"
                 :disabled "true"} "Disabled"]]]

     (code-block
       "<!-- Clickable tag -->
<ty-tag flavor=\"primary\" clickable onclick=\"alert('Tag clicked!')\">
  Click me
</ty-tag>

<!-- Dismissible tag with event listener -->
<ty-tag id=\"dismiss-tag\" flavor=\"danger\" dismissible>
  Dismiss me
</ty-tag>
<script>
  document.getElementById('dismiss-tag')
    .addEventListener('dismiss', function(e) {
      e.target.remove();
    });
</script>

<!-- Both clickable and dismissible -->
<ty-tag flavor=\"success\" clickable dismissible>
  Both
</ty-tag>

<!-- Disabled state -->
<ty-tag flavor=\"warning\" clickable disabled>
  Disabled
</ty-tag>")]

    ;; With Icons and Slots
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-xl.font-semibold.ty-text+.mb-4 "With Icons and Slots"]
     [:p.ty-text-.mb-4 "Use slots to add icons, emojis, or badges:"]

     [:div.mb-4.p-4.ty-bg-neutral-.rounded
      [:div.flex.flex-wrap.gap-3
       [:ty-tag {:flavor "primary"
                 :size "sm"}
        [:span {:slot "start"} "🚀"]
        "Launch"]
       [:ty-tag {:flavor "success"}
        [:ty-icon {:slot "start"
                   :name "check"
                   :size "16"}]
        "Verified"]
       [:ty-tag {:flavor "warning"}
        "In Progress"
        [:span.ty-text--.ml-1 {:slot "end"} "(3)"]]
       [:ty-tag {:flavor "danger"
                 :dismissible "true"}
        [:ty-icon {:slot "start"
                   :name "alert-circle"
                   :size "16"}]
        "Error"]]]

     (code-block
       "<!-- With emoji -->
<ty-tag flavor=\"primary\" size=\"sm\">
  <span slot=\"start\">🚀</span>
  Launch
</ty-tag>

<!-- With icon -->
<ty-tag flavor=\"success\">
  <ty-icon slot=\"start\" name=\"check\" size=\"16\"></ty-icon>
  Verified
</ty-tag>

<!-- With end slot badge -->
<ty-tag flavor=\"warning\">
  In Progress
  <span slot=\"end\" class=\"ty-text-- ml-1\">(3)</span>
</ty-tag>

<!-- Icon with dismissible -->
<ty-tag flavor=\"danger\" dismissible>
  <ty-icon slot=\"start\" name=\"alert-circle\" size=\"16\"></ty-icon>
  Error
</ty-tag>")]

    ;; Multiselect Integration
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-xl.font-semibold.ty-text+.mb-4 "Multiselect Integration"]
     [:p.ty-text-.mb-4 "Tags work seamlessly with ty-multiselect for rich selections:"]

     [:div.mb-4.p-4.ty-bg-neutral-.rounded
      [:ty-multiselect {:placeholder "Select skills..."
                        :value "javascript,react"
                        :style {:min-width "300px"}}
       [:ty-tag {:value "javascript"
                 :flavor "warning"
                 :size "sm"}
        [:span {:slot "start"} "📜"] "JavaScript"]
       [:ty-tag {:value "typescript"
                 :flavor "primary"
                 :size "sm"}
        [:span {:slot "start"} "🔷"] "TypeScript"]
       [:ty-tag {:value "react"
                 :flavor "success"
                 :size "sm"}
        [:span {:slot "start"} "⚛️"] "React"]
       [:ty-tag {:value "vue"
                 :flavor "success"
                 :size "sm"}
        [:span {:slot "start"} "🟢"] "Vue.js"]
       [:ty-tag {:value "python"
                 :flavor "neutral"
                 :size "sm"}
        [:span {:slot "start"} "🐍"] "Python"]]]

     (code-block
       "<ty-multiselect placeholder=\"Select skills...\" value=\"javascript,react\">
  <ty-tag value=\"javascript\" flavor=\"warning\" size=\"sm\">
    <span slot=\"start\">📜</span>
    JavaScript
  </ty-tag>
  <ty-tag value=\"typescript\" flavor=\"primary\" size=\"sm\">
    <span slot=\"start\">🔷</span>
    TypeScript
  </ty-tag>
  <ty-tag value=\"react\" flavor=\"success\" size=\"sm\">
    <span slot=\"start\">⚛️</span>
    React
  </ty-tag>
  <ty-tag value=\"vue\" flavor=\"success\" size=\"sm\">
    <span slot=\"start\">🟢</span>
    Vue.js
  </ty-tag>
  <ty-tag value=\"python\" flavor=\"neutral\" size=\"sm\">
    <span slot=\"start\">🐍</span>
    Python
  </ty-tag>
</ty-multiselect>")]]

   ;; Common Use Cases
   [:h2.text-2xl.font-semibold.ty-text++.my-6 "Common Use Cases"]

   [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
    ;; Status Indicators
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "Status Indicators"]
     [:div.space-y-3
      [:div.flex.items-center.gap-2
       [:span.ty-text- "User Status:"]
       [:ty-tag {:flavor "success"
                 :size "sm"} "Active"]]
      [:div.flex.items-center.gap-2
       [:span.ty-text- "Deployment:"]
       [:ty-tag {:flavor "warning"
                 :size "sm"} "Pending"]]
      [:div.flex.items-center.gap-2
       [:span.ty-text- "Service:"]
       [:ty-tag {:flavor "danger"
                 :size "sm"} "Offline"]]]]

    ;; Category Labels
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "Category Labels"]
     [:div.flex.flex-wrap.gap-2
      [:ty-tag {:flavor "primary"
                :size "sm"
                :pill "false"} "Technology"]
      [:ty-tag {:flavor "secondary"
                :size "sm"
                :pill "false"} "Design"]
      [:ty-tag {:flavor "success"
                :size "sm"
                :pill "false"} "Marketing"]
      [:ty-tag {:flavor "warning"
                :size "sm"
                :pill "false"} "Sales"]]]

    ;; Skill Badges
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "Skill Badges"]
     [:div.flex.flex-wrap.gap-2
      [:ty-tag {:flavor "neutral"
                :size "sm"} "HTML/CSS"]
      [:ty-tag {:flavor "neutral"
                :size "sm"} "JavaScript"]
      [:ty-tag {:flavor "neutral"
                :size "sm"} "React"]
      [:ty-tag {:flavor "neutral"
                :size "sm"} "Node.js"]]]

    ;; Filter Tags
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "Active Filters"]
     [:div.flex.flex-wrap.gap-2
      [:ty-tag {:flavor "primary"
                :dismissible "true"
                :size "sm"} "Price < $100"]
      [:ty-tag {:flavor "primary"
                :dismissible "true"
                :size "sm"} "In Stock"]
      [:ty-tag {:flavor "primary"
                :dismissible "true"
                :size "sm"} "Free Shipping"]]]]

   ;; JavaScript API
   [:div.ty-elevated.rounded-lg.p-6.my-8
    [:h2.text-2xl.font-semibold.ty-text++.mb-4 "JavaScript API"]
    [:p.ty-text-.mb-4 "Programmatic interaction with ty-tag:"]

    (code-block
      "// Get tag element
const tag = document.querySelector('ty-tag');

// Properties
tag.value = 'my-value';           // Get/set value
tag.selected = true;              // Get/set selected state

// Listen for events (Custom Events, not attributes!)
tag.addEventListener('click', (e) => {
  console.log('Tag clicked:', e.detail.target);
});

tag.addEventListener('dismiss', (e) => {
  console.log('Tag dismissed:', e.detail.target);
  e.target.remove(); // Remove the tag
});

// Create tags dynamically
const newTag = document.createElement('ty-tag');
newTag.flavor = 'success';
newTag.dismissible = true;
newTag.textContent = 'New Tag';
document.body.appendChild(newTag);

// Add dismiss handler to dynamically created tag
newTag.addEventListener('dismiss', (e) => {
  e.target.remove();
});

// Keyboard interactions (built-in)
// - Enter/Space: Trigger click on clickable tags
// - Delete/Backspace: Trigger dismiss on dismissible tags"
      "javascript")]

   ;; Best Practices
   [:div.ty-elevated.rounded-lg.p-6
    [:h2.text-2xl.font-semibold.ty-text++.mb-4 "Best Practices"]

    [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
     ;; Do's
     [:div
      [:h3.text-lg.font-semibold.ty-text-success++.mb-3.flex.items-center
       [:ty-icon.mr-2.ty-text-success {:name "check-circle"
                                       :size "20"}]
       "Do's"]
      [:ul.space-y-2.ty-text
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-success {:name "check"
                                               :size "16"}]
        [:span "Use semantic flavors to convey meaning (success for positive, danger for errors)"]]
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-success {:name "check"
                                               :size "16"}]
        [:span "Keep tag text concise and scannable"]]
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-success {:name "check"
                                               :size "16"}]
        [:span "Use dismissible tags for removable filters or selections"]]
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-success {:name "check"
                                               :size "16"}]
        [:span "Provide visual feedback with icons or emojis in slots"]]
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-success {:name "check"
                                               :size "16"}]
        [:span "Use consistent sizing within tag groups"]]]]

     ;; Don'ts
     [:div
      [:h3.text-lg.font-semibold.ty-text-danger++.mb-3.flex.items-center
       [:ty-icon.mr-2.ty-text-danger {:name "x-circle"
                                      :size "20"}]
       "Don'ts"]
      [:ul.space-y-2.ty-text
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-danger {:name "x"
                                              :size "16"}]
        [:span "Don't use tags for long text - they're meant for labels"]]
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-danger {:name "x"
                                              :size "16"}]
        [:span "Don't mix pill and rectangular shapes in the same context"]]
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-danger {:name "x"
                                              :size "16"}]
        [:span "Don't make all tags dismissible if they represent fixed categories"]]
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-danger {:name "x"
                                              :size "16"}]
        [:span "Don't use clickable without providing clear visual feedback"]]
       [:li.flex.items-start
        [:ty-icon.mr-2.mt-0.5.ty-text-danger {:name "x"
                                              :size "16"}]
        [:span "Don't overuse different flavors - maintain visual hierarchy"]]]]]]

   ;; Accessibility
   [:div.ty-content.rounded-lg.p-6.my-8
    [:h2.text-2xl.font-semibold.ty-text++.mb-4 "Accessibility"]
    [:p.ty-text.mb-4
     "ty-tag includes built-in accessibility features:"]
    [:ul.list-disc.list-inside.space-y-2.ty-text-
     [:li "Keyboard navigation support (Enter/Space for click, Delete/Backspace for dismiss)"]
     [:li "ARIA attributes for disabled state"]
     [:li "Proper focus management for interactive tags"]
     [:li "Screen reader friendly dismiss button labels"]
     [:li "High contrast mode support via semantic colors"]]]])
