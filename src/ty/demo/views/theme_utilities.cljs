(ns ty.demo.views.theme-utilities
  "Demo page for ty theme utility classes")

(defn view []
  [:div.space-y-8

   ;; Header
   [:div.text-center.mb-8
    [:h1.text-4xl.font-bold.ty-text-primary.mb-4 "Theme Utilities Demo"]
    [:p.text-lg.ty-text "Test semantic surfaces, background utilities, and theme-aware classes"]]

   ;; NEW: Semantic Surface Classes Demo
   [:div.demo-section
    [:h2.demo-title "🆕 Semantic Surface Classes"]
    [:p.demo-subtitle "Developer-friendly surface classes that match mental models"]
    [:div.ty-surface-canvas.p-6.rounded.border
     [:div.text-sm.font-mono.mb-4 ".ty-surface-canvas (App canvas background)"]
     [:div.ty-surface-content.p-4.rounded.mb-4
      [:div.text-sm.font-mono.mb-4 ".ty-surface-content (Main content background)"]
      [:div.ty-surface-elevated.ty-elevated.p-4.rounded.mb-4
       [:div.text-sm.font-mono.mb-2 ".ty-surface-elevated + .ty-elevated"]
       [:div.text-xs.ty-text "Perfect for cards and panels"]]
      [:div.ty-surface-floating.ty-floating.p-4.rounded
       [:div.text-sm.font-mono.mb-2 ".ty-surface-floating + .ty-floating"]
       [:div.text-xs.ty-text "Perfect for modals and overlays"]]]]]

   ;; Background Utilities Demo (Legacy but still supported)
   [:div.demo-section
    [:h2.demo-title "Background Utilities (ty-bg+)"]
    [:p.demo-subtitle "Base background levels with + and - emphasis"]
    [:div.grid.grid-cols-2.md:grid-cols-4.gap-4.mb-6
     [:div.ty-bg.p-4.rounded.border.text-center
      [:div.font-mono.text-sm ".ty-bg"]
      [:div.text-xs.ty-text "Base background"]]
     [:div.ty-bg+.p-4.rounded.border.text-center
      [:div.font-mono.text-sm ".ty-bg+"]
      [:div.text-xs.ty-text "Elevated"]]
     [:div.ty-bg++.p-4.rounded.border.text-center
      [:div.font-mono.text-sm ".ty-bg++"]
      [:div.text-xs.ty-text "More elevated"]]
     [:div.ty-bg+++.p-4.rounded.border.text-center
      [:div.font-mono.text-sm ".ty-bg+++"]
      [:div.text-xs.ty-text "Most elevated"]]]

    [:h3.demo-subtitle "Semantic Backgrounds"]
    [:div.grid.grid-cols-2.md:grid-cols-4.gap-4
     [:div.ty-bg-primary.p-4.rounded.text-center
      [:div.font-mono.text-sm.ty-text-primary ".ty-bg-primary"]
      [:div.text-xs.ty-text-primary- "Primary theme"]]
     [:div.ty-bg-success.p-4.rounded.text-center
      [:div.font-mono.text-sm.ty-text-success ".ty-bg-success"]
      [:div.text-xs.ty-text-success- "Success theme"]]
     [:div.ty-bg-warning.p-4.rounded.text-center
      [:div.font-mono.text-sm.ty-text-warning ".ty-bg-warning"]
      [:div.text-xs.ty-text-warning- "Warning theme"]]
     [:div.ty-bg-danger.p-4.rounded.text-center
      [:div.font-mono.text-sm.ty-text-danger ".ty-bg-danger"]
      [:div.text-xs.ty-text-danger- "Danger theme"]]]]

   ;; Text Color Demo
   [:div.demo-section
    [:h2.demo-title "Text Color Utilities"]
    [:p.demo-subtitle "Semantic colors with emphasis levels"]
    [:div.space-y-4
     [:div
      [:h4.font-semibold.mb-2 "Primary Text Colors"]
      [:div.flex.flex-wrap.gap-4
       [:span.ty-text-primary "ty-text-primary"]
       [:span.ty-text-primary+ "ty-text-primary+"]
       [:span.ty-text-primary++ "ty-text-primary++"]
       [:span.ty-text-primary- "ty-text-primary-"]
       [:span.ty-text-primary-- "ty-text-primary--"]]]
     [:div
      [:h4.font-semibold.mb-2 "Success Text Colors"]
      [:div.flex.flex-wrap.gap-4
       [:span.ty-text-success "ty-text-success"]
       [:span.ty-text-success+ "ty-text-success+"]
       [:span.ty-text-success++ "ty-text-success++"]
       [:span.ty-text-success- "ty-text-success-"]
       [:span.ty-text-success-- "ty-text-success--"]]]
     [:div
      [:h4.font-semibold.mb-2 "Danger Text Colors"]
      [:div.flex.flex-wrap.gap-4
       [:span.ty-text-danger "ty-text-danger"]
       [:span.ty-text-danger+ "ty-text-danger+"]
       [:span.ty-text-danger++ "ty-text-danger++"]
       [:span.ty-text-danger- "ty-text-danger-"]
       [:span.ty-text-danger-- "ty-text-danger--"]]]]]

   ;; Border Utilities Demo
   [:div.demo-section
    [:h2.demo-title "Border Utilities"]
    [:p.demo-subtitle "Theme-aware border colors with emphasis"]
    [:div.grid.grid-cols-2.md:grid-cols-3.gap-4
     [:div.p-4.border-2.ty-border.rounded
      [:div.font-mono.text-sm "ty-border"]
      [:div.text-xs.ty-text "Base border"]]
     [:div.p-4.border-2.ty-border+.rounded
      [:div.font-mono.text-sm "ty-border+"]
      [:div.text-xs.ty-text "Stronger"]]
     [:div.p-4.border-2.ty-border++.rounded
      [:div.font-mono.text-sm "ty-border++"]
      [:div.text-xs.ty-text "Even stronger"]]
     [:div.p-4.border-2.ty-border-primary.rounded
      [:div.font-mono.text-sm.ty-text-primary "ty-border-primary"]
      [:div.text-xs.ty-text-primary- "Primary border"]]
     [:div.p-4.border-2.ty-border-success.rounded
      [:div.font-mono.text-sm.ty-text-success "ty-border-success"]
      [:div.text-xs.ty-text-success- "Success border"]]
     [:div.p-4.border-2.ty-border-danger.rounded
      [:div.font-mono.text-sm.ty-text-danger "ty-border-danger"]
      [:div.text-xs.ty-text-danger- "Danger border"]]]]

   ;; Component-like examples
   [:div.demo-section
    [:h2.demo-title "Real-world Examples"]
    [:p.demo-subtitle "Using ty utilities for actual components"]
    [:div.space-y-4

     ;; Card example
     [:div.ty-card.rounded-lg.p-6
      [:h3.text-lg.font-semibold.ty-text-primary.mb-2 "Default Card"]
      [:p.ty-text "This card uses .ty-card class which automatically adapts to light/dark themes."]]

     ;; Elevated card
     [:div.ty-card-elevated.rounded-lg.p-6
      [:h3.text-lg.font-semibold.ty-text-primary.mb-2 "Elevated Card"]
      [:p.ty-text "This card uses .ty-card-elevated with automatic shadow and theme support."]]

     ;; Alert-style components
     [:div.ty-bg-success-.border.ty-border-success.rounded.p-4
      [:div.flex.items-center
       [:div.w-5.h-5.ty-text-success.mr-3 "✓"]
       [:div
        [:div.font-semibold.ty-text-success "Success Message"]
        [:div.text-sm.ty-text-success- "This combines ty-bg-success- with ty-border-success"]]]]

     [:div.ty-bg-danger-.border.ty-border-danger.rounded.p-4
      [:div.flex.items-center
       [:div.w-5.h-5.ty-text-danger.mr-3 "⚠"]
       [:div
        [:div.font-semibold.ty-text-danger "Error Message"]
        [:div.text-sm.ty-text-danger- "This combines ty-bg-danger- with ty-border-danger"]]]]]]

   ;; Theme Toggle Demo
   [:div.demo-section
    [:h2.demo-title "Theme Toggle Test"]
    [:p.demo-subtitle "Toggle between light and dark themes to see the utilities adapt"]
    [:div.flex.justify-center.gap-4.mb-6
     [:button.px-4.py-2.bg-white.border.border-gray-300.rounded.hover:bg-gray-50
      {:on {:click #(.. js/document -documentElement -classList (remove "dark"))}}
      "Light Theme"]
     [:button.px-4.py-2.bg-gray-800.text-white.border.border-gray-600.rounded.hover:bg-gray-700
      {:on {:click #(.. js/document -documentElement -classList (add "dark"))}}
      "Dark Theme"]]
    [:div.ty-bg-primary.ty-border-primary.border.rounded.p-4.text-center
     [:div.ty-text-primary.font-semibold "This box uses ty utilities"]
     [:div.text-sm.ty-text-primary- "Colors automatically adapt to theme!"]]]

   ;; Code examples
   [:div.demo-section
    [:h2.demo-title "Usage Examples"]
    [:div.space-y-4
     [:div
      [:h4.font-semibold.mb-2 "HTML Usage:"]
      [:pre.code-block
       [:code
        "&lt;div class=\"ty-bg+ p-4 rounded\"&gt;\n"
        "  &lt;h3 class=\"ty-text-primary+ font-semibold\"&gt;Title&lt;/h3&gt;\n"
        "  &lt;p class=\"ty-text\"&gt;Content&lt;/p&gt;\n"
        "&lt;/div&gt;"]]]
     [:div
      [:h4.font-semibold.mb-2 "Replicant Usage (Clean Syntax):"]
      [:pre.code-block
       [:code
        "[:div.ty-bg+.p-4.rounded\n"
        "  [:h3.ty-text-primary+.font-semibold \"Title\"]\n"
        "  [:p.ty-text \"Content\"]]"]]]
     [:div
      [:h4.font-semibold.mb-2 "Complex Example:"]
      [:pre.code-block
       [:code
        "[:div.ty-bg-success-.border.ty-border-success.rounded.p-4\n"
        "  [:div.flex.items-center\n"
        "    [:div.ty-text-success \"✓\"]\n"
        "    [:div.ty-text-success- \"Success message\"]]]"]]]]]])
