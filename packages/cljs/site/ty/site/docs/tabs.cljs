(ns ty.site.docs.tabs
  "Documentation for ty-tabs and ty-tab components"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table]]))

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
        ;; Animation
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "--transition-duration"]
         [:td.px-4.py-2.ty-text-.text-sm.font-mono "300ms"]
         [:td.px-4.py-2.ty-text-.text-sm "Duration of panel and marker animations"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "--transition-easing"]
         [:td.px-4.py-2.ty-text-.text-sm.font-mono "ease-in-out"]
         [:td.px-4.py-2.ty-text-.text-sm "Timing function for animations"]]
        ;; Tray styling
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "--ty-tabs-bg"]
         [:td.px-4.py-2.ty-text-.text-sm.font-mono "transparent"]
         [:td.px-4.py-2.ty-text-.text-sm "Tab tray background color"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "--ty-tabs-border-width"]
         [:td.px-4.py-2.ty-text-.text-sm.font-mono "1px"]
         [:td.px-4.py-2.ty-text-.text-sm "Tab tray border thickness (0 = no border)"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "--ty-tabs-border-color"]
         [:td.px-4.py-2.ty-text-.text-sm.font-mono "var(--ty-border)"]
         [:td.px-4.py-2.ty-text-.text-sm "Tab tray border color"]]
        ;; Button styling
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "--ty-tabs-button-padding"]
         [:td.px-4.py-2.ty-text-.text-sm.font-mono "6px 12px"]
         [:td.px-4.py-2.ty-text-.text-sm "Tab button padding"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "--ty-tabs-button-gap"]
         [:td.px-4.py-2.ty-text-.text-sm.font-mono "8px"]
         [:td.px-4.py-2.ty-text-.text-sm "Gap between icon and text in buttons"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "--ty-tabs-button-color"]
         [:td.px-4.py-2.ty-text-.text-sm.font-mono "var(--ty-text-)"]
         [:td.px-4.py-2.ty-text-.text-sm "Default button text color"]]
        ;; Hover state
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "--ty-tabs-button-hover-bg"]
         [:td.px-4.py-2.ty-text-.text-sm.font-mono "var(--ty-surface-elevated)"]
         [:td.px-4.py-2.ty-text-.text-sm "Button hover background"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "--ty-tabs-button-hover-color"]
         [:td.px-4.py-2.ty-text-.text-sm.font-mono "var(--ty-text)"]
         [:td.px-4.py-2.ty-text-.text-sm "Button hover text color"]]
        ;; Disabled state
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "--ty-tabs-button-disabled-opacity"]
         [:td.px-4.py-2.ty-text-.text-sm.font-mono "0.5"]
         [:td.px-4.py-2.ty-text-.text-sm "Disabled button opacity"]]
        ;; Focus state
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "--ty-tabs-button-focus-color"]
         [:td.px-4.py-2.ty-text-.text-sm.font-mono "var(--ty-color-primary)"]
         [:td.px-4.py-2.ty-text-.text-sm "Focus outline color"]]]]]]

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
         [:td.px-4.py-2.ty-text.font-mono.text-sm "marker"]
         [:td.px-4.py-2.ty-text-.text-sm "Optional animated indicator that follows the active tab. Gives complete control over active tab appearance (background, underline, etc)."]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "label-{id}"]
         [:td.px-4.py-2.ty-text-.text-sm "Rich content for tab button with given id (overrides label attribute). Can include icons, badges, etc."]]
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
</ty-tabs>")
   [:p.ty-text-.text-sm.mt-4.italic "Note: Live examples coming soon"]])

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
      [:p.ty-text-.text-sm "ARIA roles, prefers-reduced-motion support, proper focus management"]]]]])

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
     [:p.ty-text- "Use icons in labels to improve scannability"]]
    [:div.flex.items-start.gap-2
     [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                       :size "sm"}]
     [:p.ty-text- "Use badges in labels to show notification counts"]]
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
               :label "General Settings"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "General Settings"]
       [:p.ty-text-.mb-4 "This is the general settings panel."]
       [:div.space-y-4
        [:div
         [:label.block.text-sm.font-medium.ty-text.mb-2 "Application Name"]
         [:ty-input {:value "My Application"
                     :class "w-full"}]]
        [:div
         [:label.block.text-sm.font-medium.ty-text.mb-2 "Description"]
         [:ty-textarea {:value "A sample application"
                        :class "w-full"}]]]]]

     [:ty-tab {:id "advanced"
               :label "Advanced Settings"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "Advanced Settings"]
       [:p.ty-text-.mb-4 "Advanced configuration options."]
       [:div.space-y-4
        [:div
         [:label.block.text-sm.font-medium.ty-text.mb-2 "API Endpoint"]
         [:ty-input {:value "https://api.example.com"
                     :class "w-full"}]]
        [:div
         [:label.block.text-sm.font-medium.ty-text.mb-2 "Timeout (ms)"]
         [:ty-input {:type "number"
                     :value "5000"
                     :class "w-full"}]]]]]

     [:ty-tab {:id "about"
               :label "About"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "About"]
       [:p.ty-text-.mb-4 "Information about this application."]
       [:div.space-y-3
        [:p.ty-text- [:strong "Version:"] " 1.0.0"]
        [:p.ty-text- [:strong "Build:"] " 2025-01-05"]
        [:p.ty-text- [:strong "Author:"] " Ty Components Team"]]]]]]
   (code-block "<ty-tabs width=\"100%\" height=\"400px\">
  <ty-tab id=\"general\" label=\"General Settings\">
    <div class=\"p-6\">
      <h3>General Settings</h3>
      <p>Content here...</p>
    </div>
  </ty-tab>
  
  <ty-tab id=\"advanced\" label=\"Advanced Settings\">
    <div class=\"p-6\">
      <h3>Advanced Settings</h3>
      <p>Content here...</p>
    </div>
  </ty-tab>
  
  <ty-tab id=\"about\" label=\"About\">
    <div class=\"p-6\">
      <h3>About</h3>
      <p>Content here...</p>
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
               :height "500px"
               :class "ty-elevated"}

     ;; Rich labels as direct children
     [:span {:slot "label-profile"
             :class "flex items-center gap-2"}
      [:ty-icon {:name "user"
                 :size "sm"}]
      "Profile"]

     [:span.flex.items-center.gap-2 {:slot "label-notifications"}
      [:ty-icon {:name "bell"
                 :size "sm"}]
      "Notifications"
      [:span.ty-bg-danger.ty-text-danger++.px-2.py-0.5.rounded-full.text-xs.font-bold "5"]]

     [:span {:slot "label-settings-tab"
             :class "flex items-center gap-2"}
      [:ty-icon {:name "settings"
                 :size "sm"}]
      "Settings"]

     ;; Tab panels
     [:ty-tab {:id "profile"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "User Profile"]
       [:div.space-y-4
        [:div.flex.items-center.gap-4
         [:div.w-20.h-20.rounded-full.ty-bg-primary.flex.items-center.justify-center
          [:ty-icon {:name "user"
                     :size "lg"
                     :class "ty-text-primary++"}]]
         [:div
          [:h4.text-lg.font-semibold.ty-text "John Doe"]
          [:p.ty-text-.text-sm "john.doe@example.com"]]]]]]

     [:ty-tab {:id "notifications"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "Notifications"]
       [:div.space-y-3
        [:div.ty-floating.p-4.rounded.flex.items-start.gap-3
         [:ty-icon {:name "mail"
                    :size "sm"
                    :class "ty-text-primary mt-1"}]
         [:div
          [:p.font-medium.ty-text "New message from Alice"]
          [:p.ty-text-.text-sm "2 minutes ago"]]]
        [:div.ty-floating.p-4.rounded.flex.items-start.gap-3
         [:ty-icon {:name "star"
                    :size "sm"
                    :class "ty-text-warning mt-1"}]
         [:div
          [:p.font-medium.ty-text "You received a star"]
          [:p.ty-text-.text-sm "1 hour ago"]]]]]]

     [:ty-tab {:id "settings-tab"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "Settings"]
       [:div.space-y-4
        [:div.flex.items-center.justify-between
         [:div
          [:p.font-medium.ty-text "Email Notifications"]
          [:p.ty-text-.text-sm "Receive email updates"]]
         [:ty-button {:flavor "primary"
                      :size "sm"} "Enable"]]]]]]]
   (code-block "<ty-tabs width=\"800px\" height=\"500px\" class=\"ty-elevated\">
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
   [:h3.text-xl.font-medium.ty-text.mb-3 "Marker Slot (Active Tab Styling)"]
   [:p.ty-text-.text-sm.mb-4
    "Tabs include a " [:strong "default 2px underline"] " that follows the active tab. You can customize this by providing a " [:code.ty-bg-neutral-.px-2.py-1.rounded.text-xs "slot=\"marker\""] " element, which gives you complete control over active tab appearance. When you provide a custom marker, the default underline is automatically hidden."]

   ;; Example 1: Underline marker
   [:div.mb-6
    [:h4.text-base.font-semibold.ty-text.mb-2 "Underline Marker"]
    [:div.ty-elevated.rounded-lg.p-6.mb-4
     [:ty-tabs {:width "100%"
                :height "300px"}
      ;; Underline marker
      [:div {:slot "marker"
             :style {:height "2px"
                     :background "var(--ty-color-primary)"
                     :position "absolute"
                     :bottom "0"}}]

      [:ty-tab {:id "tab1"
                :label "Dashboard"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-2 "Dashboard"]
        [:p.ty-text- "Simple underline indicator follows the active tab."]]]

      [:ty-tab {:id "tab2"
                :label "Analytics"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-2 "Analytics"]
        [:p.ty-text- "Click between tabs to see the smooth animation."]]]

      [:ty-tab {:id "tab3"
                :label "Settings"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-2 "Settings"]
        [:p.ty-text- "The marker position and width adjust automatically."]]]]]
    (code-block "<ty-tabs width=\"100%\" height=\"300px\">
  <!-- Simple underline marker -->
  <div slot=\"marker\" style=\"
    height: 2px;
    background: var(--ty-color-primary);
    position: absolute;
    bottom: 0;
  \"></div>
  
  <ty-tab id=\"tab1\" label=\"Dashboard\">...</ty-tab>
  <ty-tab id=\"tab2\" label=\"Analytics\">...</ty-tab>
  <ty-tab id=\"tab3\" label=\"Settings\">...</ty-tab>
</ty-tabs>")]

   ;; Example 2: Pill marker
   [:div.mb-6
    [:h4.text-base.font-semibold.ty-text.mb-2 "Pill Marker"]
    [:div.ty-elevated.rounded-lg.p-6.mb-4
     [:ty-tabs {:width "100%"
                :height "300px"
                :style {:--ty-tabs-border-width "0"
                        :--ty-tabs-bg "var(--ty-surface-elevated)"}}
      ;; Pill marker
      [:div.ty-bg-primary.rounded-full.shadow-sm {:slot "marker"}]

      [:ty-tab {:id "pill1"
                :label "Home"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-2 "Home"]
        [:p.ty-text- "Rounded pill background with shadow effect."]]]

      [:ty-tab {:id "pill2"
                :label "Projects"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-2 "Projects"]
        [:p.ty-text- "Material Design inspired pill tabs."]]]

      [:ty-tab {:id "pill3"
                :label "Team"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-2 "Team"]
        [:p.ty-text- "No border, subtle tray background."]]]]]
    (code-block "<ty-tabs width=\"100%\" height=\"300px\" style=\"
  --ty-tabs-border-width: 0;
  --ty-tabs-bg: var(--ty-surface-elevated);
\">
  <!-- Rounded pill marker -->
  <div slot=\"marker\" class=\"ty-bg-primary rounded-lg shadow-sm\"></div>
  
  <ty-tab id=\"home\" label=\"Home\">...</ty-tab>
  <ty-tab id=\"projects\" label=\"Projects\">...</ty-tab>
  <ty-tab id=\"team\" label=\"Team\">...</ty-tab>
</ty-tabs>")]

   ;; Example 3: Gradient marker
   [:div.mb-6
    [:h4.text-base.font-semibold.ty-text.mb-2 "Gradient Marker"]
    [:div.ty-elevated.rounded-lg.p-6.mb-4
     [:ty-tabs {:width "100%"
                :height "300px"
                :style {:--ty-tabs-border-width "0"}}
      ;; Gradient marker
      [:div {:slot "marker"
             :style {:background "linear-gradient(135deg, #667eea 0%, #764ba2 100%)"
                     :border-radius "0.5rem"
                     :box-shadow "0 2px 8px rgba(0,0,0,0.15)"}}]

      [:ty-tab {:id "grad1"
                :label "Overview"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-2 "Overview"]
        [:p.ty-text- "Beautiful gradient background with shadow."]]]

      [:ty-tab {:id "grad2"
                :label "Details"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-2 "Details"]
        [:p.ty-text- "Custom CSS gives you complete creative control."]]]

      [:ty-tab {:id "grad3"
                :label "Reports"}
       [:div.p-6
        [:h3.text-lg.font-bold.ty-text.mb-2 "Reports"]
        [:p.ty-text- "Mix gradients, shadows, and any CSS effects."]]]]]
    (code-block "<ty-tabs width=\"100%\" height=\"300px\" style=\"--ty-tabs-border-width: 0;\">
  <!-- Gradient marker -->
  <div slot=\"marker\" style=\"
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 0.5rem;
    box-shadow: 0 2px 8px rgba(0,0,0,0.15);
  \"></div>
  
  <ty-tab id=\"overview\" label=\"Overview\">...</ty-tab>
  <ty-tab id=\"details\" label=\"Details\">...</ty-tab>
  <ty-tab id=\"reports\" label=\"Reports\">...</ty-tab>
</ty-tabs>")]

   ;; Styling tips
   [:div.ty-bg-primary-.ty-border-primary.border.rounded-lg.p-4.mt-6
    [:div.flex.items-start.gap-3
     [:ty-icon.ty-text-primary++.mt-0.5.flex-shrink-0 {:name "lightbulb"
                                                       :size "md"}]
     [:div
      [:h4.text-base.font-semibold.ty-text-primary++.mb-2 "Marker Styling Tips"]
      [:ul.list-disc.ml-4.space-y-1.ty-text-primary.text-sm
       [:li "The marker automatically positions and resizes to match the active tab"]
       [:li "Use Ty semantic classes like " [:code.ty-bg-neutral-.px-1.rounded "ty-bg-primary"] " for theme-aware colors"]
       [:li "Combine with CSS variables for hover and border customization"]
       [:li "No marker = clean tabs with just hover effects"]
       [:li "The marker animates smoothly (300ms) - respects prefers-reduced-motion"]]]]]])

(defn example-long-content []
  [:section.mb-8
   [:h3.text-xl.font-medium.ty-text.mb-3 "✅ Expected: Long Panel Content"]
   [:p.ty-text-.text-sm.mb-4
    "Panel content scrolling is normal and expected. Each panel scrolls independently within fixed viewport."]
   [:div.ty-elevated.rounded-lg.p-6
    [:ty-tabs {:width "100%"
               :height "500px"}
     [:ty-tab {:id "short-content"
               :label "Short Content"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "Short Content"]
       [:p.ty-text- "This tab has minimal content - no scrolling needed."]]]

     [:ty-tab {:id "long-content"
               :label "Long Content"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "Scrollable Content"]
       [:p.ty-text-.mb-4 "✅ This is normal! Content exceeds viewport height and scrolls."]
       (for [i (range 1 31)]
         [:div.mb-4 {:key i}
          [:h4.text-base.font-semibold.ty-text.mb-2 (str "Section " i)]
          [:p.ty-text-.text-sm
           "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
           "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."]])]]

     [:ty-tab {:id "medium-content"
               :label "Medium"}
      [:div.p-6
       [:h3.text-xl.font-bold.ty-text.mb-4 "Medium Content"]
       (for [i (range 1 11)]
         [:p.ty-text-.mb-4 {:key i}
          (str "Paragraph " i ". Some medium-length content.")])]]]]
   (code-block "<!-- ✅ THIS IS GOOD - Panel scrolling is expected -->
<ty-tabs width=\"100%\" height=\"500px\">
  <ty-tab id=\"long\" label=\"Long Content\">
    <div class=\"p-6\">
      <!-- 30+ sections - scrolls normally -->
    </div>
  </ty-tab>
</ty-tabs>")])

(defn examples-section []
  [:div.mb-8
   [:h2.text-2xl.font-semibold.ty-text.mb-4 "Examples"]])

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Title and Description
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-tabs"]
    [:p.text-lg.ty-text- "A carousel-based tabs component with smooth animations and fixed container dimensions."]]

   ;; Sections - Clean composition
   (api-reference)
   (basic-usage)
   (example-basic-text-labels)
   (example-rich-labels)
   (example-marker-styling)
   (example-long-content)
   (key-features)
   (best-practices)
   (warning-banner)
   (limitations-section)])
