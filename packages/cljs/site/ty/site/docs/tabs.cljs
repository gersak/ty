(ns ty.site.docs.tabs
  "Documentation for ty-tabs and ty-tab components"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table docs-page]]))

;; =====================================================
;; Section Components - Split for Maintainability
;; =====================================================

(defn warning-banner []
  [:div.ty-bg-warning-.ty-border-warning.border-2.rounded-lg.p-6.mb-8
   [:div.flex.items-start.gap-3
    [:ty-icon.ty-text-warning++.mt-0.5.flex-shrink-0 {:name "alert-triangle"
                                                      :size "lg"}]
    [:div.flex-1
     [:h3.text-lg.font-bold.ty-text-warning++.mb-2 "⚠️ Tab Count Limitation"]
     [:p.ty-text-warning.mb-3
      "Tabs are designed for " [:strong "3-7 options maximum"] ". More than this creates poor user experience and causes visual overflow issues."]
     [:div.ty-bg-warning.rounded.p-4.mb-3
      [:p.text-sm.ty-text.font-semibold.mb-2 "If you need more than 7 options, use:"]
      [:ul.list-disc.ml-6.space-y-1.text-sm.ty-text-
       [:li [:strong "Sidebar Navigation"] " - For 10+ hierarchical items"]
       [:li [:strong "Accordion Menu"] " - For collapsible grouped sections"]
       [:li [:strong "Dropdown Select"] " - For choosing from many options"]
       [:li [:strong "Wizard/Stepper"] " - For sequential multi-step flows"]]]
     [:p.text-sm.ty-text-warning.italic.mt-2
      "The overflow examples below are educational - they demonstrate what NOT to do."]]]])

(defn api-reference []
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
        :description "Position of tab buttons: top (default) or bottom."}])]

    ;; CSS Parts
    [:div.mb-6
     [:h4.text-base.font-medium.ty-text.mb-3 "CSS Parts (::part)"]
     [:p.ty-text-.text-sm.mb-3
      "Customize tabs appearance using CSS Shadow Parts. These provide styling hooks without breaking encapsulation."]
     [:div.overflow-x-auto
      [:table.w-full
       [:thead
        [:tr.border-b.ty-border
         [:th.text-left.px-4.py-2.ty-text+ "Part"]
         [:th.text-left.px-4.py-2.ty-text+ "Description"]]]
       [:tbody
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "buttons-container"]
         [:td.px-4.py-2.ty-text-.text-sm "Tab buttons tray - customize background, border, padding"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "marker-wrapper"]
         [:td.px-4.py-2.ty-text-.text-sm "Animated marker wrapper - customize positioning animation"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "panels-container"]
         [:td.px-4.py-2.ty-text-.text-sm "Panels viewport - customize panels area appearance"]]]]]]

    ;; Slots
    [:div.mb-6
     [:h4.text-base.font-medium.ty-text.mb-3 "Slots"]
     [:div.overflow-x-auto
      [:table.w-full
       [:thead
        [:tr.border-b.ty-border
         [:th.text-left.px-4.py-2.ty-text+ "Slot"]
         [:th.text-left.px-4.py-2.ty-text+ "Description"]]]
       [:tbody
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "marker"]
         [:td.px-4.py-2.ty-text-.text-sm "Optional animated indicator that follows the active tab. Complete control over active tab styling (underline, pill, gradient, etc)."]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "label-{id}"]
         [:td.px-4.py-2.ty-text-.text-sm "Rich content for tab button with given id (overrides label attribute). Use for icons, badges, custom formatting."]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "(default)"]
         [:td.px-4.py-2.ty-text-.text-sm "Container for ty-tab children"]]]]]]

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
         [:td.px-4.py-2.ty-text.font-mono.text-sm "(default)"]
         [:td.px-4.py-2.ty-text-.text-sm "Panel content that displays when tab is active"]]]]]]]])

(defn basic-usage []
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
</ty-tabs>")])

(defn key-features []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-semibold.ty-text.mb-4 "Key Features"]
   [:div.space-y-3
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-primary.mt-0.5 {:name "zap"
                                       :size "sm"}]
     [:div
      [:p.ty-text.font-medium "Carousel Animation"]
      [:p.ty-text-.text-sm "Smooth sliding transitions with automatic direction handling"]]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-primary.mt-0.5 {:name "maximize"
                                       :size "sm"}]
     [:div
      [:p.ty-text.font-medium "Fixed Dimensions"]
      [:p.ty-text-.text-sm "Prevents layout shift when switching between tabs with different content heights"]]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-primary.mt-0.5 {:name "layout"
                                       :size "sm"}]
     [:div
      [:p.ty-text.font-medium "Independent Panel Scrolling"]
      [:p.ty-text-.text-sm "Each panel scrolls within fixed viewport, scroll position resets on tab change"]]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-primary.mt-0.5 {:name "accessibility"
                                       :size "sm"}]
     [:div
      [:p.ty-text.font-medium "Accessibility Built-in"]
      [:p.ty-text-.text-sm "ARIA roles, prefers-reduced-motion support, proper focus management"]]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-primary.mt-0.5 {:name "palette"
                                       :size "sm"}]
     [:div
      [:p.ty-text.font-medium "CSS Parts & Slots"]
      [:p.ty-text-.text-sm "Complete styling control via ::part() and slot-based customization"]]]]])

(defn best-practices []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-semibold.ty-text.mb-4 "Best Practices"]
   [:div.space-y-3
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                       :size "sm"}]
     [:p.ty-text- "Always specify both width and height attributes"]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                       :size "sm"}]
     [:p.ty-text- "Use semantic IDs for tabs (e.g., 'settings', 'profile')"]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                       :size "sm"}]
     [:p.ty-text- "Keep tab labels concise (1-3 words maximum)"]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                       :size "sm"}]
     [:p.ty-text- "Limit tabs to 3-7 options - use alternative patterns for more"]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                       :size "sm"}]
     [:p.ty-text- "Use CSS Parts for tray styling instead of wrapping divs"]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                       :size "sm"}]
     [:p.ty-text- "Use marker slot for custom active tab indicators"]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-danger.mt-0.5 {:name "x"
                                      :size "sm"}]
     [:p.ty-text- "Don't use more than 7 tabs (causes overflow and poor UX)"]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-danger.mt-0.5 {:name "x"
                                      :size "sm"}]
     [:p.ty-text- "Don't use long, multi-word labels (keep it to 1-3 words)"]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-danger.mt-0.5 {:name "x"
                                      :size "sm"}]
     [:p.ty-text- "Avoid tabs with dramatically different content heights"]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-danger.mt-0.5 {:name "x"
                                      :size "sm"}]
     [:p.ty-text- "Don't nest tabs within tabs (use different navigation pattern)"]]]])

(defn limitations-section []
  [:div.ty-bg-neutral-.ty-border.border.rounded-lg.p-6
   [:h2.text-2xl.font-semibold.ty-text.mb-4 "Current Limitations & Roadmap"]
   [:div.space-y-4
    [:div
     [:h3.text-base.font-medium.ty-text.mb-2 "V1 Limitations:"]
     [:ul.list-disc.list-inside.space-y-1.ty-text-.text-sm
      [:li "No keyboard navigation (mouse/touch only)"]
      [:li "All panels rendered on mount (no lazy loading)"]
      [:li "Horizontal tabs only (vertical deferred to v2)"]
      [:li "No closable tabs feature"]
      [:li "Tab buttons overflow container if >7 tabs (by design - use alternative UI patterns)"]
      [:li "Long tab labels may compress or overflow (keep labels concise)"]]]

    [:div
     [:h3.text-base.font-medium.ty-text.mb-2 "Planned for V2:"]
     [:ul.list-disc.list-inside.space-y-1.ty-text-.text-sm
      [:li "Keyboard navigation (Arrow keys, Home/End)"]
      [:li "Lazy rendering with state preservation"]
      [:li "Vertical tabs (left/right placement)"]
      [:li "Closable tabs with close buttons"]
      [:li "Drag-and-drop reordering"]
      [:li "Nested tabs support"]]]]])

(defn example-basic-text-labels []
  [:section.mb-8
   [:h3.text-xl.font-medium.ty-text.mb-3 "Basic Tabs (Text Labels)"]
   [:p.ty-text-.text-sm.mb-4
    "Simple tabs using the label attribute for text-only buttons."]
   [:div.ty-elevated.rounded-lg.p-6
    [:ty-tabs {:width "100%"
               :height "400px"}
     [:ty-tab {:id "general"
               :label "General"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "General Settings"]
       [:p.ty-text-.mb-4 "Configure your application's basic settings."]
       [:div.space-y-4
        [:div
         [:label.block.text-sm.font-medium.ty-text.mb-2 "Application Name"]
         [:ty-input {:value "My Application"
                     :class "w-full"}]]
        [:div
         [:label.block.text-sm.font-medium.ty-text.mb-2 "Description"]
         [:ty-textarea {:value "A powerful web application"
                        :class "w-full"}]]]]]

     [:ty-tab {:id "advanced"
               :label "Advanced"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "Advanced Settings"]
       [:p.ty-text-.mb-4 "Fine-tune advanced configuration options."]
       [:div.space-y-4
        [:div
         [:label.block.text-sm.font-medium.ty-text.mb-2 "API Endpoint"]
         [:ty-input {:value "https://api.example.com"
                     :class "w-full"}]]
        [:div
         [:label.block.text-sm.font-medium.ty-text.mb-2 "Request Timeout (ms)"]
         [:ty-input {:type "number"
                     :value "5000"
                     :class "w-full"}]]]]]

     [:ty-tab {:id "about"
               :label "About"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "About This Application"]
       [:div.space-y-4
        [:div.flex.items-center.gap-4
         [:div.w-16.h-16.rounded-lg.ty-bg-primary.flex.items-center.justify-center
          [:ty-icon {:name "info"
                     :size "xl"
                     :class "ty-text-primary++"}]]
         [:div
          [:p.ty-text.font-semibold "Version 1.0.0"]
          [:p.ty-text-.text-sm "Built with Ty Components"]]]
        [:div.ty-content.rounded.p-4
         [:p.ty-text-.text-sm.leading-relaxed
          "This application demonstrates the power of Ty's web component system. "
          "Built on modern web standards for maximum compatibility."]]]]]]]
   (code-block "<ty-tabs width=\"100%\" height=\"400px\">
  <ty-tab id=\"general\" label=\"General\">
    <div class=\"p-6\">
      <h3>General Settings</h3>
      <p>Configure basic options...</p>
    </div>
  </ty-tab>
  
  <ty-tab id=\"advanced\" label=\"Advanced\">
    <div class=\"p-6\">
      <h3>Advanced Settings</h3>
      <p>Fine-tune configuration...</p>
    </div>
  </ty-tab>
  
  <ty-tab id=\"about\" label=\"About\">
    <div class=\"p-6\">
      <h3>About</h3>
      <p>Application information...</p>
    </div>
  </ty-tab>
</ty-tabs>")])

(defn example-rich-labels []
  [:section.mb-8
   [:h3.text-xl.font-medium.ty-text.mb-3 "Rich Labels with Icons & Badges"]
   [:p.ty-text-.text-sm.mb-4
    "Use slot-based labels for rich content including icons, badges, and custom styling. Labels must be direct children of ty-tabs."]
   [:div.ty-elevated.rounded-lg.p-6
    [:ty-tabs {:width "800px"
               :height "500px"}

     ;; Rich labels as direct children
     [:span.flex.items-center.gap-2 {:slot "label-profile"}
      [:ty-icon {:name "user"
                 :size "sm"}]
      "Profile"]

     [:span.flex.items-center.gap-2 {:slot "label-notifications"}
      [:ty-icon {:name "bell"
                 :size "sm"}]
      "Notifications"
      [:span.ty-bg-danger.ty-text-danger++.px-2.py-0.5.rounded-full.text-xs.font-bold "5"]]

     [:span.flex.items-center.gap-2 {:slot "label-settings"}
      [:ty-icon {:name "settings"
                 :size "sm"}]
      "Settings"]

     ;; Tab panels
     [:ty-tab {:id "profile"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "User Profile"]
       [:div.space-y-6
        [:div.flex.items-center.gap-4
         [:div.w-20.h-20.rounded-full.ty-bg-primary.flex.items-center.justify-center
          [:ty-icon {:name "user"
                     :size "xl"
                     :class "ty-text-primary++"}]]
         [:div
          [:h4.text-lg.font-semibold.ty-text "John Doe"]
          [:p.ty-text-.text-sm "john.doe@example.com"]
          [:p.ty-text--.text-xs.mt-1 "Premium Member"]]]
        [:div.grid.grid-cols-2.gap-4
         [:div.ty-content.rounded.p-4
          [:p.ty-text--.text-xs.mb-1 "Member Since"]
          [:p.ty-text.font-semibold "January 2024"]]
         [:div.ty-content.rounded.p-4
          [:p.ty-text--.text-xs.mb-1 "Last Active"]
          [:p.ty-text.font-semibold "2 minutes ago"]]]]]]

     [:ty-tab {:id "notifications"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "Notifications"]
       [:div.space-y-3
        [:div.ty-floating.p-4.rounded.flex.items-start.gap-3.border.ty-border
         [:div.w-10.h-10.rounded-full.ty-bg-primary-.flex.items-center.justify-center.flex-shrink-0
          [:ty-icon {:name "mail"
                     :size "sm"
                     :class "ty-text-primary"}]]
         [:div.flex-1
          [:p.font-medium.ty-text "New message from Alice"]
          [:p.ty-text-.text-sm.mb-2 "Hey! Want to grab coffee tomorrow?"]
          [:p.ty-text--.text-xs "2 minutes ago"]]]
        [:div.ty-floating.p-4.rounded.flex.items-start.gap-3.border.ty-border
         [:div.w-10.h-10.rounded-full.ty-bg-warning-.flex.items-center.justify-center.flex-shrink-0
          [:ty-icon {:name "star"
                     :size "sm"
                     :class "ty-text-warning"}]]
         [:div.flex-1
          [:p.font-medium.ty-text "You received a star"]
          [:p.ty-text-.text-sm.mb-2 "Someone starred your repository!"]
          [:p.ty-text--.text-xs "1 hour ago"]]]
        [:div.ty-floating.p-4.rounded.flex.items-start.gap-3.border.ty-border
         [:div.w-10.h-10.rounded-full.ty-bg-success-.flex.items-center.justify-center.flex-shrink-0
          [:ty-icon {:name "check-circle"
                     :size "sm"
                     :class "ty-text-success"}]]
         [:div.flex-1
          [:p.font-medium.ty-text "Deployment successful"]
          [:p.ty-text-.text-sm.mb-2 "Your app is now live in production."]
          [:p.ty-text--.text-xs "3 hours ago"]]]]]]

     [:ty-tab {:id "settings"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "Settings"]
       [:div.space-y-6
        [:div.flex.items-center.justify-between.py-3.border-b.ty-border
         [:div
          [:p.font-medium.ty-text "Email Notifications"]
          [:p.ty-text-.text-sm "Receive updates via email"]]
         [:ty-button {:flavor "primary"
                      :size "sm"} "Enable"]]
        [:div.flex.items-center.justify-between.py-3.border-b.ty-border
         [:div
          [:p.font-medium.ty-text "Dark Mode"]
          [:p.ty-text-.text-sm "Toggle dark theme"]]
         [:ty-button {:size "sm"} "Auto"]]
        [:div.flex.items-center.justify-between.py-3
         [:div
          [:p.font-medium.ty-text "Language"]
          [:p.ty-text-.text-sm "Choose interface language"]]
         [:ty-button {:size "sm"} "English"]]]]]]]
   (code-block "<ty-tabs width=\"800px\" height=\"500px\">
  <!-- Rich labels as direct children of ty-tabs -->
  <span slot=\"label-profile\" class=\"flex items-center gap-2\">
    <ty-icon name=\"user\" size=\"sm\"></ty-icon>
    Profile
  </span>
  
  <span slot=\"label-notifications\" class=\"flex items-center gap-2\">
    <ty-icon name=\"bell\" size=\"sm\"></ty-icon>
    Notifications
    <span class=\"ty-bg-danger ty-text-danger++ px-2 py-0.5 rounded-full text-xs font-bold\">5</span>
  </span>
  
  <span slot=\"label-settings\" class=\"flex items-center gap-2\">
    <ty-icon name=\"settings\" size=\"sm\"></ty-icon>
    Settings
  </span>
  
  <!-- Tab panels contain the content -->
  <ty-tab id=\"profile\">
    <div class=\"p-6\">
      <h3>User Profile</h3>
      <!-- Profile content -->
    </div>
  </ty-tab>
  
  <ty-tab id=\"notifications\">
    <div class=\"p-6\">
      <h3>Notifications</h3>
      <!-- Notifications content -->
    </div>
  </ty-tab>
  
  <ty-tab id=\"settings\">
    <div class=\"p-6\">
      <h3>Settings</h3>
      <!-- Settings content -->
    </div>
  </ty-tab>
</ty-tabs>")])

(defn example-marker-styling []
  [:section.mb-8
   [:h3.text-xl.font-medium.ty-text.mb-3 "Custom Marker Slot (Active Tab Indicator)"]
   [:p.ty-text-.text-sm.mb-4
    "Tabs include a " [:strong "default 2px underline"] " that follows the active tab. Customize by providing a "
    [:code.ty-bg-neutral-.px-2.py-1.rounded.text-xs "slot=\"marker\""] " element for complete control over the active tab indicator."]

   ;; Example 1: Colored underline
   [:div.mb-8
    [:h4.text-base.font-semibold.ty-text.mb-3 "Colored Underline"]
    [:div.ty-elevated.rounded-lg.p-6.mb-4
     [:ty-tabs {:width "100%"
                :height "300px"}
      ;; Primary color underline
      [:div.absolute.bottom-0.left-0.h-0.5.ty-bg-primary {:slot "marker"}]

      [:ty-tab {:id "dashboard"
                :label "Dashboard"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-3 "Dashboard Overview"]
        [:p.ty-text-.mb-4 "Clean underline indicator using Ty semantic colors."]
        [:div.grid.grid-cols-3.gap-4
         [:div.ty-content.rounded.p-4.text-center
          [:p.ty-text++.text-2xl.font-bold "152"]
          [:p.ty-text-.text-sm "Active Users"]]
         [:div.ty-content.rounded.p-4.text-center
          [:p.ty-text++.text-2xl.font-bold "89%"]
          [:p.ty-text-.text-sm "Completion"]]
         [:div.ty-content.rounded.p-4.text-center
          [:p.ty-text++.text-2xl.font-bold "24"]
          [:p.ty-text-.text-sm "New Today"]]]]]

      [:ty-tab {:id "analytics"
                :label "Analytics"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-3 "Analytics"]
        [:p.ty-text- "The marker smoothly animates between tabs."]]]

      [:ty-tab {:id "reports"
                :label "Reports"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-3 "Reports"]
        [:p.ty-text- "Width adjusts automatically to match button size."]]]]]
    (code-block "<ty-tabs width=\"100%\" height=\"300px\">
  <!-- Simple colored underline -->
  <div slot=\"marker\" class=\"absolute bottom-0 left-0 h-0.5 ty-bg-primary\"></div>
  
  <ty-tab id=\"dashboard\" label=\"Dashboard\">...</ty-tab>
  <ty-tab id=\"analytics\" label=\"Analytics\">...</ty-tab>
  <ty-tab id=\"reports\" label=\"Reports\">...</ty-tab>
</ty-tabs>")]

   ;; Example 2: Pill marker (no tray border)
   [:div.mb-8
    [:h4.text-base.font-semibold.ty-text.mb-3 "Pill Background (No Border)"]
    [:div.ty-elevated.rounded-lg.p-6.mb-4
     [:style "
       .tabs-pill::part(buttons-container) {
         background: var(--ty-surface-elevated);
         padding: 0.25rem;
         border-radius: 0.5rem;
         border: none;
       }
     "]
     [:ty-tabs {:width "100%"
                :height "300px"
                :class "tabs-pill"}
      ;; Rounded pill marker - REPLACES the default underline
      [:div.ty-bg-primary.rounded-full.shadow-sm {:slot "marker"}]

      [:ty-tab {:id "home"
                :label "Home"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-3 "Home"]
        [:p.ty-text-.mb-4 "Pill marker with elevated tray - no underline, clean Material Design style."]
        [:div.space-y-3
         [:div.flex.items-center.gap-3
          [:ty-icon {:name "home"
                     :size "sm"
                     :class "ty-text-primary"}]
          [:p.ty-text "Welcome to your dashboard"]]
         [:div.flex.items-center.gap-3
          [:ty-icon {:name "zap"
                     :size "sm"
                     :class "ty-text-primary"}]
          [:p.ty-text "Quick actions available"]]]]]

      [:ty-tab {:id "projects"
                :label "Projects"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-3 "Projects"]
        [:p.ty-text- "Custom marker slot replaces default underline entirely."]]]

      [:ty-tab {:id "team"
                :label "Team"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-3 "Team"]
        [:p.ty-text- "Combine marker slot with ::part for complete control."]]]]]
    (code-block "<style>
  .tabs-pill::part(buttons-container) {
    background: var(--ty-surface-elevated);
    padding: 0.25rem;
    border-radius: 0.5rem;
    border: none;
  }
</style>

<ty-tabs width=\"100%\" height=\"300px\" class=\"tabs-pill\">
  <!-- Pill marker replaces default underline -->
  <div slot=\"marker\" class=\"ty-bg-primary rounded-full shadow-sm\"></div>
  
  <ty-tab id=\"home\" label=\"Home\">...</ty-tab>
  <ty-tab id=\"projects\" label=\"Projects\">...</ty-tab>
  <ty-tab id=\"team\" label=\"Team\">...</ty-tab>
</ty-tabs>")]

   ;; Example 3: Gradient marker (no tray border)
   [:div.mb-8
    [:h4.text-base.font-semibold.ty-text.mb-3 "Gradient Background (No Border)"]
    [:div.ty-elevated.rounded-lg.p-6.mb-4
     [:style "
       .tabs-gradient::part(buttons-container) {
         padding: 0.5rem 0;
         border: none;
         --ty-text-strong: white;
       }
     "]
     [:ty-tabs {:width "100%"
                :height "300px"
                :class "tabs-gradient"}
      ;; Gradient marker with shadow - REPLACES the default underline
      [:div {:slot "marker"
             :style {:background "linear-gradient(135deg, #667eea 0%, #764ba2 100%)"
                     :border-radius "0.5rem"
                     :box-shadow "0 4px 12px rgba(102, 126, 234, 0.3)"}}]

      [:ty-tab {:id "overview"
                :label "Overview"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-3 "Overview"]
        [:p.ty-text-.mb-4 "Beautiful gradient with shadow - no border, premium feel."]
        [:div.ty-content.rounded.p-4
         [:p.ty-text.leading-relaxed
          "Use CSS gradients to create stunning visual effects. "
          "The marker slot completely replaces the default underline indicator."]]]]

      [:ty-tab {:id "details"
                :label "Details"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-3 "Details"]
        [:p.ty-text- "Custom CSS gives unlimited creative control."]]]

      [:ty-tab {:id "summary"
                :label "Summary"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-3 "Summary"]
        [:p.ty-text- "Mix gradients, shadows, rounded corners - anything!"]]]]]
    (code-block "<style>
  .tabs-gradient::part(buttons-container) {
    padding: 0.5rem 0;
    border: none;
    --ty-text-strong: white;
  }
</style>

<ty-tabs width=\"100%\" height=\"300px\" class=\"tabs-gradient\">
  <!-- Gradient marker replaces default underline -->
  <div slot=\"marker\" style=\"
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 0.5rem;
    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
  \"></div>
  
  <ty-tab id=\"overview\" label=\"Overview\">...</ty-tab>
  <ty-tab id=\"details\" label=\"Details\">...</ty-tab>
  <ty-tab id=\"summary\" label=\"Summary\">...</ty-tab>
</ty-tabs>")]

   ;; Styling tips
   [:div.ty-bg-primary-.ty-border-primary.border.rounded-lg.p-4.mt-6
    [:div.flex.items-start.gap-3
     [:ty-icon.ty-text-primary++.mt-0.5.flex-shrink-0 {:name "lightbulb"
                                                       :size "md"}]
     [:div
      [:h4.text-base.font-semibold.ty-text-primary++.mb-2 "Marker Slot Behavior"]
      [:ul.list-disc.ml-4.space-y-1.ty-text-primary.text-sm
       [:li [:strong "Replaces default underline:"] " When you provide a marker slot, the default 2px underline is automatically hidden"]
       [:li [:strong "Auto positioning:"] " The marker automatically positions and sizes to match the active tab button"]
       [:li [:strong "Use semantic colors:"] " Classes like " [:code.ty-bg-neutral-.px-1.rounded "ty-bg-primary"] " for theme-aware styling"]
       [:li [:strong "Smooth animation:"] " The marker animates smoothly (300ms) and respects prefers-reduced-motion"]
       [:li [:strong "Combine with ::part:"] " Use " [:code.ty-bg-neutral-.px-1.rounded "::part(buttons-container)"] " to remove tray border for cleaner look"]]

      [:h4.text-base.font-semibold.ty-text-primary++.mb-2.mt-4 "Button Styling Limitation"]
      [:p.ty-text-primary.text-sm.mb-2
       "Individual tab buttons are currently " [:strong "not exposed as a CSS part"] ". To style the active tab appearance:"]
      [:ul.list-disc.ml-4.space-y-1.ty-text-primary.text-sm
       [:li [:strong "Use the marker slot:"] " The recommended way to indicate the active tab"]
       [:li [:strong "Rich labels:"] " Use " [:code.ty-bg-neutral-.px-1.rounded "slot=\"label-{id}\""] " for custom button content with icons, badges"]
       [:li [:strong "Future enhancement:"] " Button styling via ::part may be added in a future version"]]]]]])

(defn example-css-parts []
  [:section.mb-8
   [:h3.text-xl.font-medium.ty-text.mb-3 "CSS Parts - Tray Customization"]
   [:p.ty-text-.text-sm.mb-4
    "Use CSS Shadow Parts (::part) to customize the tab buttons tray without breaking encapsulation. "
    "This is the modern, recommended approach for styling web components."]

   ;; Example 1: Elevated tray
   [:div.mb-8
    [:h4.text-base.font-semibold.ty-text.mb-3 "Elevated Tray with Background"]
    [:div.ty-elevated.rounded-lg.p-6.mb-4
     [:style "
       .tabs-elevated::part(buttons-container) {
         background: var(--ty-surface-elevated);
         border: none;
         padding: 0.5rem;
         border-radius: 0.5rem;
         gap: 0.25rem;
       }
     "]
     [:ty-tabs {:width "100%"
                :height "300px"
                :class "tabs-elevated"}
      [:div.ty-bg-primary.rounded-lg {:slot "marker"}]

      [:ty-tab {:id "files"
                :label "Files"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-3 "Files"]
        [:p.ty-text-.mb-4 "Elevated tray with rounded background - perfect for card-based layouts."]
        [:div.space-y-2
         [:div.flex.items-center.gap-3.ty-content.rounded.p-3
          [:ty-icon {:name "file-text"
                     :size "sm"
                     :class "ty-text-primary"}]
          [:div.flex-1
           [:p.ty-text.font-medium "document.pdf"]
           [:p.ty-text--.text-xs "2.4 MB"]]]]]]

      [:ty-tab {:id "images"
                :label "Images"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-3 "Images"]
        [:p.ty-text- "Styled with CSS Parts - no wrapper divs needed."]]]

      [:ty-tab {:id "videos"
                :label "Videos"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-3 "Videos"]
        [:p.ty-text- "Clean, semantic styling approach."]]]]]
    (code-block "<style>
  .tabs-elevated::part(buttons-container) {
    background: var(--ty-surface-elevated);
    border: none;
    padding: 0.5rem;
    border-radius: 0.5rem;
    gap: 0.25rem;
  }
</style>

<ty-tabs width=\"100%\" height=\"300px\" class=\"tabs-elevated\">
  <div slot=\"marker\" class=\"ty-bg-primary rounded-lg\"></div>
  
  <ty-tab id=\"files\" label=\"Files\">...</ty-tab>
  <ty-tab id=\"images\" label=\"Images\">...</ty-tab>
  <ty-tab id=\"videos\" label=\"Videos\">...</ty-tab>
</ty-tabs>")]

   ;; Example 2: Bordered tray
   [:div.mb-8
    [:h4.text-base.font-semibold.ty-text.mb-3 "Custom Border & Spacing"]
    [:div.ty-elevated.rounded-lg.p-6.mb-4
     [:style "
       .tabs-bordered::part(buttons-container) {
         border: 2px solid var(--ty-color-primary);
         border-radius: 0.5rem;
         padding: 0.25rem;
         background: var(--ty-surface-content);
       }
     "]
     [:ty-tabs {:width "100%"
                :height "300px"
                :class "tabs-bordered"}
      [:div.ty-bg-primary.rounded {:slot "marker"}]

      [:ty-tab {:id "inbox"
                :label "Inbox"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-3 "Inbox"]
        [:p.ty-text-.mb-4 "Custom border styling via CSS Parts."]
        [:div.space-y-3
         [:div.ty-floating.border.ty-border.rounded.p-3
          [:p.ty-text.font-medium "New message from Alice"]
          [:p.ty-text-.text-sm "Would you like to review the proposal?"]]]]]

      [:ty-tab {:id "sent"
                :label "Sent"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-3 "Sent"]
        [:p.ty-text- "2px primary border with rounded corners."]]]

      [:ty-tab {:id "drafts"
                :label "Drafts"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-3 "Drafts"]
        [:p.ty-text- "Easy to customize, maintains encapsulation."]]]]]
    (code-block "<style>
  .tabs-bordered::part(buttons-container) {
    border: 2px solid var(--ty-color-primary);
    border-radius: 0.5rem;
    padding: 0.25rem;
    background: var(--ty-surface-content);
  }
</style>

<ty-tabs width=\"100%\" height=\"300px\" class=\"tabs-bordered\">
  <div slot=\"marker\" class=\"ty-bg-primary rounded\"></div>
  
  <ty-tab id=\"inbox\" label=\"Inbox\">...</ty-tab>
  <ty-tab id=\"sent\" label=\"Sent\">...</ty-tab>
  <ty-tab id=\"drafts\" label=\"Drafts\">...</ty-tab>
</ty-tabs>")]

   ;; CSS Parts benefits
   [:div.ty-bg-success-.ty-border-success.border.rounded-lg.p-4.mt-6
    [:div.flex.items-start.gap-3
     [:ty-icon.ty-text-success++.mt-0.5.flex-shrink-0 {:name "check-circle"
                                                       :size "md"}]
     [:div
      [:h4.text-base.font-semibold.ty-text-success++.mb-2 "Why CSS Parts?"]
      [:ul.list-disc.ml-4.space-y-1.ty-text-success.text-sm
       [:li "✅ " [:strong "Maintains encapsulation"] " - Styling hooks without breaking shadow DOM"]
       [:li "✅ " [:strong "No wrapper divs"] " - Clean semantic HTML structure"]
       [:li "✅ " [:strong "Framework agnostic"] " - Works with any CSS approach"]
       [:li "✅ " [:strong "Type-safe"] " - IDE autocomplete for part names"]
       [:li "✅ " [:strong "Scoped styling"] " - Use classes to target specific tabs instances"]]]]]])

(defn example-long-content []
  [:section.mb-8
   [:h3.text-xl.font-medium.ty-text.mb-3 "✅ Panel Scrolling (Expected Behavior)"]
   [:p.ty-text-.text-sm.mb-4
    "Panel content scrolling is normal and expected. Each panel scrolls independently within fixed viewport."]
   [:div.ty-elevated.rounded-lg.p-6
    [:ty-tabs {:width "100%"
               :height "500px"}
     [:ty-tab {:id "short-content"
               :label "Short"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "Short Content"]
       [:p.ty-text- "This tab has minimal content - no scrolling needed."]
       [:div.ty-content.rounded.p-4.mt-4
        [:p.ty-text "Everything fits within the viewport."]]]]

     [:ty-tab {:id "long-content"
               :label "Long"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "Scrollable Content"]
       [:p.ty-text-.mb-4 "✅ This is normal! Content exceeds viewport height and scrolls independently."]
       (for [i (range 1 31)]
         [:div.mb-4 {:key i}
          [:div.ty-content.rounded.p-4
           [:h4.text-base.font-semibold.ty-text.mb-2 (str "Section " i)]
           [:p.ty-text-.text-sm
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
            "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."]]])]]

     [:ty-tab {:id "medium-content"
               :label "Medium"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "Medium Content"]
       [:p.ty-text-.mb-4 "Some scrolling required, but not excessive."]
       (for [i (range 1 11)]
         [:div.mb-4 {:key i}
          [:div.ty-content.rounded.p-3
           [:p.ty-text (str "Paragraph " i ". Some medium-length content that demonstrates scrolling behavior.")]]])]]]]
   (code-block "<!-- ✅ THIS IS GOOD - Panel scrolling is expected behavior -->
<ty-tabs width=\"100%\" height=\"500px\">
  <ty-tab id=\"long\" label=\"Long Content\">
    <div class=\"p-6\">
      <!-- 30+ sections - scrolls normally within fixed viewport -->
      <h3>Section 1</h3>
      <p>Content...</p>
      <!-- ... more sections ... -->
    </div>
  </ty-tab>
</ty-tabs>")])

(defn examples-section []
  [:div.mb-8
   [:h2.text-2xl.font-semibold.ty-text.mb-6 "Examples"]])

(defn view []
  (docs-page
   ;; Title and Description
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-tabs"]
    [:p.text-lg.ty-text-
     "A carousel-based tabs component with smooth animations, fixed dimensions, and complete customization via slots and CSS Parts."]]

   ;; Sections - Clean composition
   (api-reference)
   (basic-usage)
   (examples-section)
   (example-basic-text-labels)
   (example-rich-labels)
   (example-marker-styling)
   (example-css-parts)
   (example-long-content)
   (key-features)
   (best-practices)
   (warning-banner)
   (limitations-section)))
