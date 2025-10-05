(ns ty.site.docs.tabs
  "Documentation for ty-tabs and ty-tab components"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table]]))

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Title and Description
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-tabs"]
    [:p.text-lg.ty-text- "A carousel-based tabs component with smooth animations and fixed container dimensions."]]

   ;; Component Status Banner
   [:div.ty-bg-warning-.ty-border-warning.border.rounded-lg.p-4.mb-8
    [:div.flex.items-start.gap-3
     [:ty-icon.ty-text-warning++.mt-0.5 {:name "construction" :size "md"}]
     [:div
      [:h3.text-base.font-semibold.ty-text-warning++ "Under Development"]
      [:p.text-sm.ty-text-warning.mt-1
       "This component is currently in development. The API and examples shown here represent the planned implementation."]]]]

   ;; API Reference Card
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text.mb-6 "API Reference"]

    ;; ty-tabs Component
    [:div.mb-8
     [:h3.text-xl.font-medium.ty-text+.mb-4 "ty-tabs (Container)"]

     ;; Attributes
     [:div.mb-6
      [:h4.text-base.font-medium.ty-text.mb-3 "Attributes"]
      (attribute-table
       [{:name "width"
         :type "string"
         :default "-"
         :description "Content area width (required). Accepts px or % values."}
        {:name "height"
         :type "string"
         :default "-"
         :description "Total container height including tab buttons (required). Accepts px values."}
        {:name "active"
         :type "string"
         :default "first tab id"
         :description "ID of the currently active tab. Defaults to first tab if not specified."}
        {:name "placement"
         :type "string"
         :default "\"top\""
         :description "Position of tab buttons (top, bottom). Left/right deferred to v2."}])]

     ;; CSS Custom Properties
     [:div.mb-6
      [:h4.text-base.font-medium.ty-text.mb-3 "CSS Custom Properties"]
      [:div.overflow-x-auto
       [:table.w-full
        [:thead
         [:tr.border-b.ty-border
          [:th.text-left.px-4.py-2.ty-text+ "Property"]
          [:th.text-left.px-4.py-2.ty-text+ "Default"]
          [:th.text-left.px-4.py-2.ty-text+ "Description"]]]
        [:tbody
         [:tr.border-b.ty-border-
          [:td.px-4.py-2.ty-text.font-mono.text-sm "--transition-duration"]
          [:td.px-4.py-2.ty-text-.text-sm.font-mono "300ms"]
          [:td.px-4.py-2.ty-text-.text-sm "Duration of panel slide animation"]]
         [:tr.border-b.ty-border-
          [:td.px-4.py-2.ty-text.font-mono.text-sm "--transition-easing"]
          [:td.px-4.py-2.ty-text-.text-sm.font-mono "ease-in-out"]
          [:td.px-4.py-2.ty-text-.text-sm "Timing function for animations"]]]]]]

     ;; Events
     [:div
      [:h4.text-base.font-medium.ty-text.mb-3 "Events"]
      (event-table
       [{:name "ty-tab-change"
         :payload "{activeId, activeIndex, previousId, previousIndex}"
         :when-fired "When active tab changes (fires when animation starts)"}])]]

    ;; ty-tab Component
    [:div
     [:h3.text-xl.font-medium.ty-text+.mb-4 "ty-tab (Individual Tab)"]

     ;; Attributes
     [:div.mb-6
      [:h4.text-base.font-medium.ty-text.mb-3 "Attributes"]
      (attribute-table
       [{:name "id"
         :type "string"
         :default "-"
         :description "Unique identifier for this tab (required)"}
        {:name "label"
         :type "string"
         :default "-"
         :description "Simple text label for the tab button"}
        {:name "disabled"
         :type "boolean"
         :default "false"
         :description "Whether the tab is disabled and cannot be activated"}])]

     ;; Slots
     [:div
      [:h4.text-base.font-medium.ty-text.mb-3 "Slots"]
      [:div.overflow-x-auto
       [:table.w-full
        [:thead
         [:tr.border-b.ty-border
          [:th.text-left.px-4.py-2.ty-text+ "Slot"]
          [:th.text-left.px-4.py-2.ty-text+ "Description"]]]
        [:tbody
         [:tr.border-b.ty-border-
          [:td.px-4.py-2.ty-text.font-mono.text-sm "label"]
          [:td.px-4.py-2.ty-text-.text-sm "Rich content for tab button (overrides label attribute). Can include icons, badges, etc."]]
         [:tr.border-b.ty-border-
          [:td.px-4.py-2.ty-text.font-mono.text-sm "(default)"]
          [:td.px-4.py-2.ty-text-.text-sm "Panel content that displays when tab is active"]]]]]]]]

   ;; Basic Usage
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Basic Usage"]
    (code-block "<ty-tabs width=\"800px\" height=\"600px\" active=\"general\">
  <ty-tab id=\"general\" label=\"General\">
    <h2>General Settings</h2>
    <p>Configure general options here.</p>
  </ty-tab>
  
  <ty-tab id=\"advanced\" label=\"Advanced\">
    <h2>Advanced Settings</h2>
    <p>Advanced configuration options.</p>
  </ty-tab>
</ty-tabs>")
    [:p.ty-text-.text-sm.mt-4.italic "Note: Live examples coming soon"]]

   ;; Key Features Section
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Key Features"]
    [:div.space-y-3
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-primary.mt-0.5 {:name "zap" :size "sm"}]
      [:div
       [:p.ty-text.font-medium "Carousel Animation"]
       [:p.ty-text-.text-sm "Smooth sliding transitions with automatic direction handling"]]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-primary.mt-0.5 {:name "maximize" :size "sm"}]
      [:div
       [:p.ty-text.font-medium "Fixed Dimensions"]
       [:p.ty-text-.text-sm "Prevents layout shift when switching between tabs with different content heights"]]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-primary.mt-0.5 {:name "layout" :size "sm"}]
      [:div
       [:p.ty-text.font-medium "Independent Panel Scrolling"]
       [:p.ty-text-.text-sm "Each panel scrolls within fixed viewport, scroll position resets on tab change"]]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-primary.mt-0.5 {:name "accessibility" :size "sm"}]
      [:div
       [:p.ty-text.font-medium "Accessibility Built-in"]
       [:p.ty-text-.text-sm "ARIA roles, prefers-reduced-motion support, proper focus management"]]]]]

   ;; Examples Section
   [:h2.text-2xl.font-semibold.ty-text.mb-6 "Examples"]

   [:div.space-y-8
    ;; Simple Text Labels
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Simple Text Labels"]
     [:p.ty-text-.text-sm.mb-4
      "Use the label attribute for simple text-only tab buttons."]
     (code-block "<ty-tabs width=\"100%\" height=\"400px\">
  <ty-tab id=\"tab1\" label=\"Settings\">
    <p>Settings content goes here...</p>
  </ty-tab>
  
  <ty-tab id=\"tab2\" label=\"Profile\">
    <p>Profile content goes here...</p>
  </ty-tab>
  
  <ty-tab id=\"tab3\" label=\"Security\">
    <p>Security content goes here...</p>
  </ty-tab>
</ty-tabs>")]

    ;; Rich Labels with Icons
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Rich Labels with Icons"]
     [:p.ty-text-.text-sm.mb-4
      "Use the label slot for rich content including icons, badges, and custom formatting."]
     (code-block "<ty-tabs width=\"800px\" height=\"500px\" class=\"ty-elevated\">
  <ty-tab id=\"profile\">
    <span slot=\"label\">
      <ty-icon name=\"user\"></ty-icon>
      Profile
    </span>
    <div>Profile content...</div>
  </ty-tab>
  
  <ty-tab id=\"notifications\">
    <span slot=\"label\">
      <ty-icon name=\"bell\"></ty-icon>
      Notifications
      <ty-badge>5</ty-badge>
    </span>
    <div>Notifications content...</div>
  </ty-tab>
  
  <ty-tab id=\"settings\">
    <span slot=\"label\">
      <ty-icon name=\"settings\"></ty-icon>
      Settings
    </span>
    <div>Settings content...</div>
  </ty-tab>
</ty-tabs>")]

    ;; Bottom Placement
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Bottom Placement"]
     [:p.ty-text-.text-sm.mb-4
      "Position tab buttons at the bottom instead of the top."]
     (code-block "<ty-tabs width=\"600px\" height=\"400px\" placement=\"bottom\">
  <ty-tab id=\"tab1\" label=\"Tab 1\">
    Content for tab 1
  </ty-tab>
  <ty-tab id=\"tab2\" label=\"Tab 2\">
    Content for tab 2
  </ty-tab>
  <ty-tab id=\"tab3\" label=\"Tab 3\">
    Content for tab 3
  </ty-tab>
</ty-tabs>")]

    ;; Custom Animation Timing
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Custom Animation Timing"]
     [:p.ty-text-.text-sm.mb-4
      "Customize slide animation duration and easing using CSS custom properties."]
     (code-block "<ty-tabs 
  width=\"800px\" 
  height=\"600px\"
  style=\"--transition-duration: 500ms; --transition-easing: cubic-bezier(0.4, 0, 0.2, 1)\">
  <ty-tab id=\"slow\" label=\"Slow Animation\">
    This tab slides in with a slower, custom animation.
  </ty-tab>
  <ty-tab id=\"tab2\" label=\"Tab 2\">
    Content here...
  </ty-tab>
</ty-tabs>")]

    ;; Disabled Tabs
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Disabled Tabs"]
     [:p.ty-text-.text-sm.mb-4
      "Disable tabs that shouldn't be accessible yet."]
     (code-block "<ty-tabs width=\"800px\" height=\"600px\">
  <ty-tab id=\"enabled\" label=\"Available\">
    This content is available.
  </ty-tab>
  
  <ty-tab id=\"disabled\" label=\"Coming Soon\" disabled>
    This feature is not yet available.
  </ty-tab>
  
  <ty-tab id=\"also-enabled\" label=\"Also Available\">
    More available content.
  </ty-tab>
</ty-tabs>")]

    ;; Programmatic Control
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Programmatic Control"]
     [:p.ty-text-.text-sm.mb-4
      "Control active tab via JavaScript and listen for tab changes."]
     (code-block "// Get reference to tabs element
const tabs = document.querySelector('ty-tabs');

// Change active tab programmatically
tabs.active = 'settings';

// Listen for tab changes
tabs.addEventListener('ty-tab-change', (e) => {
  console.log('Switched to:', e.detail.activeId);
  console.log('From:', e.detail.previousId);
  console.log('New index:', e.detail.activeIndex);
});")]]

   ;; How It Works Section
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "How It Works"]
    [:p.ty-text-.mb-4
     "Unlike traditional tab implementations, ty-tabs uses a carousel approach where all panels are positioned horizontally in a row:"]

    [:div.ty-content.rounded.p-4.mb-4.font-mono.text-sm
     [:div.ty-text- "Container (800px wide, overflow: hidden)"]
     [:div.ty-text-.ml-4 "└─ Panels Wrapper (transforms horizontally)"]
     [:div.ty-text-.ml-8 "├─ Panel 0 (at 0px)"]
     [:div.ty-text-.ml-8 "├─ Panel 1 (at 800px)"]
     [:div.ty-text-.ml-8 "└─ Panel 2 (at 1600px)"]]

    [:p.ty-text-.mb-3
     "When you switch tabs, the wrapper transforms to show the selected panel:"]

    [:ul.list-disc.list-inside.space-y-2.ty-text-.text-sm
     [:li "Tab 0 active: " [:code.font-mono.text-xs "transform: translateX(0)"]]
     [:li "Tab 1 active: " [:code.font-mono.text-xs "transform: translateX(-800px)"]]
     [:li "Tab 2 active: " [:code.font-mono.text-xs "transform: translateX(-1600px)"]]]]

   ;; Architecture Benefits
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Architecture Benefits"]
    [:div.space-y-3
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:div
       [:p.ty-text "GPU-Accelerated Animation"]
       [:p.ty-text-.text-sm "CSS transform provides smooth 60fps animations"]]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:div
       [:p.ty-text "Automatic Direction"]
       [:p.ty-text-.text-sm "Sliding direction (left/right) determined automatically based on tab position"]]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:div
       [:p.ty-text "No Layout Shift"]
       [:p.ty-text-.text-sm "Fixed dimensions prevent content jumping when switching tabs"]]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:div
       [:p.ty-text "Simple CSS Transition"]
       [:p.ty-text-.text-sm "Just update a CSS variable - no complex animation logic needed"]]]]]

   ;; Best Practices
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Best Practices"]
    [:div.space-y-3
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:p.ty-text- "Always specify both width and height attributes"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:p.ty-text- "Use semantic IDs for tabs (e.g., 'settings', 'profile')"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:p.ty-text- "Keep tab labels concise and descriptive"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:p.ty-text- "Use icons in labels to improve scannability"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
      [:p.ty-text- "Use badges in labels to show notification counts"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-danger.mt-0.5 {:name "x" :size "sm"}]
      [:p.ty-text- "Don't use too many tabs (5-7 max recommended)"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-danger.mt-0.5 {:name "x" :size "sm"}]
      [:p.ty-text- "Avoid tabs with dramatically different content heights"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-danger.mt-0.5 {:name "x" :size "sm"}]
      [:p.ty-text- "Don't nest tabs within tabs (use different navigation pattern)"]]]]

   ;; Limitations & Future
   [:div.ty-bg-neutral-.ty-border.border.rounded-lg.p-6
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Current Limitations & Roadmap"]
    [:div.space-y-4
     [:div
      [:h3.text-base.font-medium.ty-text.mb-2 "V1 Limitations:"]
      [:ul.list-disc.list-inside.space-y-1.ty-text-.text-sm
       [:li "No keyboard navigation (mouse/touch only)"]
       [:li "All panels rendered on mount (no lazy loading)"]
       [:li "Horizontal tabs only (vertical deferred to v2)"]
       [:li "No closable tabs feature"]]]

     [:div
      [:h3.text-base.font-medium.ty-text.mb-2 "Planned for V2:"]
      [:ul.list-disc.list-inside.space-y-1.ty-text-.text-sm
       [:li "Keyboard navigation (Arrow keys, Home/End)"]
       [:li "Lazy rendering with state preservation"]
       [:li "Vertical tabs (left/right placement)"]
       [:li "Closable tabs with close buttons"]
       [:li "Drag-and-drop reordering"]
       [:li "Nested tabs support"]]]]]])
