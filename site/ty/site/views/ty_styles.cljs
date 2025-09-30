(ns ty.site.views.ty-styles
  "Site view showcasing Ty CSS classes and design system"
  (:require
    [ty.site.docs.common :as common]))

(defn theme-toggle []
  "Simple theme toggle section"
  [:div.flex.justify-center.gap-4.mb-8
   [:button.px-6.py-3.ty-elevated.border.ty-border.rounded-lg.hover:ty-content.ty-text.transition-colors.cursor-pointer
    {:on {:click #(.. js/document -documentElement -classList (remove "dark"))}}
    "☀️ Light Theme"]
   [:button.px-6.py-3.ty-bg-neutral.ty-text++.border.ty-border.rounded-lg.hover:ty-bg-neutral+.transition-colors.cursor-pointer
    {:on {:click #(.. js/document -documentElement -classList (add "dark"))}}
    "🌙 Dark Theme"]])

(defn text-variants-demo []
  "Shows the 5-variant text system"
  [:div.ty-elevated.p-6.rounded-lg
   [:h3.text-lg.font-semibold.ty-text.mb-4 "5-Variant Text System"]
   [:p.ty-text-.mb-6 "Each semantic color provides 5 levels of emphasis for precise text hierarchy."]

   [:div.grid.gap-6.md:grid-cols-2.lg:grid-cols-3
    ;; Primary text variants
    [:div.space-y-3
     [:h4.text-sm.font-medium.ty-text "Primary Text Variants"]
     [:div.ty-bg-primary-.p-4.rounded-lg.space-y-2
      [:div.ty-text-primary++ "ty-text-primary++"]
      [:div.ty-text-primary+ "ty-text-primary+"]
      [:div.ty-text-primary "ty-text-primary"]
      [:div.ty-text-primary- "ty-text-primary-"]
      [:div.ty-text-primary-- "ty-text-primary--"]]]

    ;; Secondary text variants
    [:div.space-y-3
     [:h4.text-sm.font-medium.ty-text "Secondary Text Variants"]
     [:div.ty-bg-secondary-.p-4.rounded-lg.space-y-2
      [:div.ty-text-secondary++ "ty-text-secondary++"]
      [:div.ty-text-secondary+ "ty-text-secondary+"]
      [:div.ty-text-secondary "ty-text-secondary"]
      [:div.ty-text-secondary- "ty-text-secondary-"]
      [:div.ty-text-secondary-- "ty-text-secondary--"]]]

    ;; Success text variants
    [:div.space-y-3
     [:h4.text-sm.font-medium.ty-text "Success Text Variants"]
     [:div.ty-bg-success-.p-4.rounded-lg.space-y-2
      [:div.ty-text-success++ "ty-text-success++"]
      [:div.ty-text-success+ "ty-text-success+"]
      [:div.ty-text-success "ty-text-success"]
      [:div.ty-text-success- "ty-text-success-"]
      [:div.ty-text-success-- "ty-text-success--"]]]

    ;; Danger text variants
    [:div.space-y-3
     [:h4.text-sm.font-medium.ty-text "Danger Text Variants"]
     [:div.ty-bg-danger-.p-4.rounded-lg.space-y-2
      [:div.ty-text-danger++ "ty-text-danger++"]
      [:div.ty-text-danger+ "ty-text-danger+"]
      [:div.ty-text-danger "ty-text-danger"]
      [:div.ty-text-danger- "ty-text-danger-"]
      [:div.ty-text-danger-- "ty-text-danger--"]]]

    ;; Warning text variants
    [:div.space-y-3
     [:h4.text-sm.font-medium.ty-text "Warning Text Variants"]
     [:div.ty-bg-warning-.p-4.rounded-lg.space-y-2
      [:div.ty-text-warning++ "ty-text-warning++"]
      [:div.ty-text-warning+ "ty-text-warning+"]
      [:div.ty-text-warning "ty-text-warning"]
      [:div.ty-text-warning- "ty-text-warning-"]
      [:div.ty-text-warning-- "ty-text-warning--"]]]

    ;; Neutral text variants
    [:div.space-y-3
     [:h4.text-sm.font-medium.ty-text "Neutral Text Variants"]
     [:div.ty-bg-neutral-.p-4.rounded-lg.space-y-2
      [:div.ty-text-neutral++ "ty-text-neutral++"]
      [:div.ty-text-neutral+ "ty-text-neutral+"]
      [:div.ty-text-neutral "ty-text-neutral"]
      [:div.ty-text-neutral- "ty-text-neutral-"]
      [:div.ty-text-neutral-- "ty-text-neutral--"]]]]])

(defn background-variants-demo []
  "Shows the 3-variant background system"
  [:div.ty-elevated.p-6.rounded-lg
   [:h3.text-lg.font-semibold.ty-text.mb-4 "Background Variants"]
   [:p.ty-text-.mb-6 "Each semantic color provides 3 background intensities: +, base, and -."]

   [:div.grid.gap-6.md:grid-cols-2.lg:grid-cols-3
    ;; Primary backgrounds
    [:div.space-y-3
     [:h4.text-sm.font-medium.ty-text "Primary Backgrounds"]
     [:div.ty-bg-primary+.p-4.rounded-lg.text-center
      [:div.ty-text-primary++.font-medium "ty-bg-primary+"]
      [:div.ty-text-primary.text-sm "Stronger background"]]
     [:div.ty-bg-primary.p-4.rounded-lg.text-center
      [:div.ty-text-primary++.font-medium "ty-bg-primary"]
      [:div.ty-text-primary.text-sm "Base background"]]
     [:div.ty-bg-primary-.p-4.rounded-lg.text-center
      [:div.ty-text-primary++.font-medium "ty-bg-primary-"]
      [:div.ty-text-primary.text-sm "Softer background"]]]

    ;; Secondary backgrounds
    [:div.space-y-3
     [:h4.text-sm.font-medium.ty-text "Secondary Backgrounds"]
     [:div.ty-bg-secondary+.p-4.rounded-lg.text-center
      [:div.ty-text-secondary++.font-medium "ty-bg-secondary+"]
      [:div.ty-text-secondary.text-sm "Stronger background"]]
     [:div.ty-bg-secondary.p-4.rounded-lg.text-center
      [:div.ty-text-secondary++.font-medium "ty-bg-secondary"]
      [:div.ty-text-secondary.text-sm "Base background"]]
     [:div.ty-bg-secondary-.p-4.rounded-lg.text-center
      [:div.ty-text-secondary++.font-medium "ty-bg-secondary-"]
      [:div.ty-text-secondary.text-sm "Softer background"]]]

    ;; Success backgrounds
    [:div.space-y-3
     [:h4.text-sm.font-medium.ty-text "Success Backgrounds"]
     [:div.ty-bg-success+.p-4.rounded-lg.text-center
      [:div.ty-text-success++.font-medium "ty-bg-success+"]
      [:div.ty-text-success.text-sm "Stronger background"]]
     [:div.ty-bg-success.p-4.rounded-lg.text-center
      [:div.ty-text-success++.font-medium "ty-bg-success"]
      [:div.ty-text-success.text-sm "Base background"]]
     [:div.ty-bg-success-.p-4.rounded-lg.text-center
      [:div.ty-text-success++.font-medium "ty-bg-success-"]
      [:div.ty-text-success.text-sm "Softer background"]]]

    ;; Danger backgrounds
    [:div.space-y-3
     [:h4.text-sm.font-medium.ty-text "Danger Backgrounds"]
     [:div.ty-bg-danger+.p-4.rounded-lg.text-center
      [:div.ty-text-danger++.font-medium "ty-bg-danger+"]
      [:div.ty-text-danger.text-sm "Stronger background"]]
     [:div.ty-bg-danger.p-4.rounded-lg.text-center
      [:div.ty-text-danger++.font-medium "ty-bg-danger"]
      [:div.ty-text-danger.text-sm "Base background"]]
     [:div.ty-bg-danger-.p-4.rounded-lg.text-center
      [:div.ty-text-danger++.font-medium "ty-bg-danger-"]
      [:div.ty-text-danger.text-sm "Softer background"]]]

    ;; Warning backgrounds
    [:div.space-y-3
     [:h4.text-sm.font-medium.ty-text "Warning Backgrounds"]
     [:div.ty-bg-warning+.p-4.rounded-lg.text-center
      [:div.ty-text-warning++.font-medium "ty-bg-warning+"]
      [:div.ty-text-warning.text-sm "Stronger background"]]
     [:div.ty-bg-warning.p-4.rounded-lg.text-center
      [:div.ty-text-warning++.font-medium "ty-bg-warning"]
      [:div.ty-text-warning.text-sm "Base background"]]
     [:div.ty-bg-warning-.p-4.rounded-lg.text-center
      [:div.ty-text-warning++.font-medium "ty-bg-warning-"]
      [:div.ty-text-warning.text-sm "Softer background"]]]

    ;; Neutral backgrounds
    [:div.space-y-3
     [:h4.text-sm.font-medium.ty-text "Neutral Backgrounds"]
     [:div.ty-bg-neutral+.p-4.rounded-lg.text-center
      [:div.ty-text-neutral++.font-medium "ty-bg-neutral+"]
      [:div.ty-text-neutral.text-sm "Stronger background"]]
     [:div.ty-bg-neutral.p-4.rounded-lg.text-center
      [:div.ty-text-neutral++.font-medium "ty-bg-neutral"]
      [:div.ty-text-neutral.text-sm "Base background"]]
     [:div.ty-bg-neutral-.p-4.rounded-lg.text-center
      [:div.ty-text-neutral++.font-medium "ty-bg-neutral-"]
      [:div.ty-text-neutral.text-sm "Softer background"]]]]])

(defn surface-classes-demo []
  "Shows surface classes nested to demonstrate layering hierarchy"
  [:div.ty-elevated.p-6.rounded-lg
   [:h3.text-lg.font-semibold.ty-text.mb-4 "Surface Hierarchy"]
   [:p.ty-text-.mb-6 "Semantic surface classes nested to show layering and elevation hierarchy."]

   [:div.space-y-6
    ;; Nested surfaces demonstration - like Russian dolls
    [:div.ty-canvas.p-6.rounded-lg.border.ty-border.relative
     [:div.absolute.top-2.left-3.text-xs.ty-text-.font-mono.opacity-75 "ty-canvas"]
     [:div.ty-content.p-6.rounded-lg.border.ty-border.relative.mt-6
      [:div.absolute.top-2.left-3.text-xs.ty-text-.font-mono.opacity-75 "ty-content"]
      [:div.ty-elevated.p-6.rounded-lg.relative.mt-6
       [:div.absolute.top-2.left-3.text-xs.ty-text-.font-mono.opacity-75 "ty-elevated"]
       [:div.ty-floating.p-6.rounded-lg.relative.mt-6.text-center
        [:div.absolute.top-2.left-3.text-xs.ty-text-.font-mono.opacity-75 "ty-floating"]
        [:div.mt-4
         [:h4.ty-text++.font-medium.mb-2 "🪆 Surface Layers"]
         [:p.ty-text-.text-sm "Each surface sits inside the previous one, creating depth and visual hierarchy like Russian nesting dolls."]]]]]]

    ;; Individual surface descriptions
    [:div.grid.gap-4.md:grid-cols-2.lg:grid-cols-3
     [:div.ty-canvas.p-4.rounded-lg.border.ty-border.text-center
      [:div.ty-text++.font-medium.mb-2 "ty-canvas"]
      [:div.ty-text-.text-sm "App background"]
      [:div.ty-text--.text-xs.mt-1 "Base layer"]]

     [:div.ty-content.p-4.rounded-lg.border.ty-border.text-center
      [:div.ty-text++.font-medium.mb-2 "ty-content"]
      [:div.ty-text-.text-sm "Main areas"]
      [:div.ty-text--.text-xs.mt-1 "Content layer"]]

     [:div.ty-elevated.p-4.rounded-lg.text-center
      [:div.ty-text++.font-medium.mb-2 "ty-elevated"]
      [:div.ty-text-.text-sm "Cards, panels"]
      [:div.ty-text--.text-xs.mt-1 "Elevated layer"]]

     [:div.ty-floating.p-4.rounded-lg.text-center
      [:div.ty-text++.font-medium.mb-2 "ty-floating"]
      [:div.ty-text-.text-sm "Modals, tooltips"]
      [:div.ty-text--.text-xs.mt-1 "Floating layer"]]]

    ;; Visual hierarchy explanation
    [:div.ty-elevated.p-4.rounded-lg
     [:h4.text-sm.font-medium.ty-text.mb-3 "Layer Hierarchy Explanation"]
     [:div.space-y-2.text-sm.ty-text-
      [:div "• " [:strong.ty-text++ "ty-canvas"] " - Base application background, lowest elevation"]
      [:div "• " [:strong.ty-text++ "ty-content"] " - Main content areas, sits on canvas"]
      [:div "• " [:strong.ty-text++ "ty-elevated"] " - Cards, panels, forms - elevated with shadow"]
      [:div "• " [:strong.ty-text++ "ty-floating"] " - Modals, dropdowns, tooltips - highest elevation"]]]]])

(defn practical-examples []
  "Shows real-world component patterns using Ty classes"
  [:div.ty-elevated.p-6.rounded-lg
   [:h3.text-lg.font-semibold.ty-text.mb-4 "Practical Component Examples"]
   [:p.ty-text-.mb-6 "Real-world patterns demonstrating proper Ty class usage."]

   [:div.space-y-6
    ;; Alert examples
    [:div
     [:h4.text-sm.font-medium.ty-text.mb-3 "Alert Components"]
     [:div.grid.gap-4.md:grid-cols-2.lg:grid-cols-3
      [:div.ty-bg-success-.ty-border-success.border.rounded-lg.p-4
       [:div.flex.items-center.gap-3
        [:ty-icon {:name "check-circle"
                   :size "sm"}]
        [:div
         [:div.ty-text-success++.font-medium "Success"]
         [:div.ty-text-success.text-sm "Operation completed successfully"]]]]

      [:div.ty-bg-danger-.ty-border-danger.border.rounded-lg.p-4
       [:div.flex.items-center.gap-3
        [:ty-icon {:name "alert-circle"
                   :size "sm"}]
        [:div
         [:div.ty-text-danger++.font-medium "Error"]
         [:div.ty-text-danger.text-sm "Something went wrong, please try again"]]]]

      [:div.ty-bg-warning-.ty-border-warning.border.rounded-lg.p-4
       [:div.flex.items-center.gap-3
        [:ty-icon {:name "alert-triangle"
                   :size "sm"}]
        [:div
         [:div.ty-text-warning++.font-medium "Warning"]
         [:div.ty-text-warning.text-sm "Please review before proceeding"]]]]

      [:div.ty-bg-neutral-.ty-border-neutral.border.rounded-lg.p-4
       [:div.flex.items-center.gap-3
        [:ty-icon {:name "info"
                   :size "sm"}]
        [:div
         [:div.ty-text-neutral++.font-medium "Information"]
         [:div.ty-text-neutral.text-sm "This is a helpful informational note"]]]]]]

    ;; Card example
    [:div
     [:h4.text-sm.font-medium.ty-text.mb-3 "Card Component"]
     [:div.ty-elevated.p-6.rounded-lg
      [:h5.ty-text-primary++.text-lg.font-semibold.mb-2 "Card Title"]
      [:p.ty-text.mb-4 "This is a card component using ty-elevated for the surface and ty-text-primary++ for the title to create proper hierarchy."]
      [:div.flex.gap-3
       [:button.px-4.py-2.ty-bg-primary.ty-text-primary++.rounded.hover:ty-bg-primary+.transition-colors "Primary Action"]
       [:button.px-4.py-2.ty-bg-secondary.ty-text-secondary++.rounded.hover:ty-bg-secondary+.transition-colors "Secondary"]]]]

    ;; Status badges
    [:div
     [:h4.text-sm.font-medium.ty-text.mb-3 "Status Badges"]
     [:div.flex.flex-wrap.gap-3
      [:span.px-3.py-1.ty-bg-success.ty-text-success++.rounded-full.text-sm.font-medium "Active"]
      [:span.px-3.py-1.ty-bg-warning.ty-text-warning++.rounded-full.text-sm.font-medium "Pending"]
      [:span.px-3.py-1.ty-bg-danger.ty-text-danger++.rounded-full.text-sm.font-medium "Failed"]
      [:span.px-3.py-1.ty-bg-neutral.ty-text-neutral++.rounded-full.text-sm.font-medium "Draft"]
      [:span.px-3.py-1.ty-bg-secondary.ty-text-secondary++.rounded-full.text-sm.font-medium "Archived"]
      [:span.px-3.py-1.ty-bg-primary.ty-text-primary++.rounded-full.text-sm.font-medium "Featured"]]]]])

(defn code-examples []
  "Shows code patterns for using Ty classes"
  [:div.ty-elevated.p-6.rounded-lg
   [:h3.text-lg.font-semibold.ty-text.mb-4 "Code Examples"]
   [:p.ty-text-.mb-6 "Copy these patterns to use Ty classes effectively in your components."]

   [:div.space-y-6
    ;; Alert pattern
    [:div
     [:h4.text-sm.font-medium.ty-text.mb-2 "Alert Component Pattern"]
     (common/code-block "[:div.ty-bg-success-.ty-border-success.border.rounded-lg.p-4
  [:div.flex.items-center.gap-3
    [:ty-icon {:name \"check-circle\" :size \"sm\"}]
    [:div
      [:div.ty-text-success++.font-medium \"Success\"]
      [:div.ty-text-success.text-sm \"Message text\"]]]]"
                        "clojure")]

    ;; Card pattern
    [:div
     [:h4.text-sm.font-medium.ty-text.mb-2 "Card Component Pattern"]
     (common/code-block "[:div.ty-elevated.p-6.rounded-lg
  [:h3.ty-text-primary++.text-lg.font-semibold \"Title\"]
  [:p.ty-text \"Body content with good contrast\"]
  [:div.ty-text-neutral-.text-sm \"Helper text\"]]"
                        "clojure")]

    ;; Form validation pattern
    [:div
     [:h4.text-sm.font-medium.ty-text.mb-2 "Form Validation Pattern"]
     (common/code-block "[:div.space-y-2
  [:input.ty-input.border.ty-border-danger]
  [:div.ty-text-danger.text-sm \"Error message\"]
  [:div.ty-text-danger-.text-xs \"Validation hint\"]]"
                        "clojure")]]])

(defn usage-guidelines []
  "Shows best practices for using Ty classes"
  [:div.ty-elevated.p-6.rounded-lg
   [:h3.text-lg.font-semibold.ty-text.mb-4 "Usage Guidelines"]
   [:p.ty-text-.mb-6 "Best practices for effective use of the Ty design system."]

   [:div.grid.gap-6.md:grid-cols-2
    [:div
     [:h4.text-sm.font-medium.ty-text-success.mb-3.flex.items-center.gap-2
      [:ty-icon {:name "check"
                 :size "sm"
                 :class "ty-text-success"}]
      "Do"]
     [:div.space-y-2.text-sm
      [:div.ty-text- "• Use semantic colors that match meaning (success/success)"]
      [:div.ty-text- "• Use strong text on soft backgrounds for good contrast"]
      [:div.ty-text- "• Test in both light and dark themes"]
      [:div.ty-text- "• Use ty-elevated for cards and panels"]
      [:div.ty-text- "• Follow text hierarchy (++ > + > base > - > --)"]]]

    [:div
     [:h4.text-sm.font-medium.ty-text-danger.mb-3.flex.items-center.gap-2
      [:ty-icon {:name "x"
                 :size "sm"
                 :class "ty-text-danger"}]
      "Don't"]
     [:div.space-y-2.text-sm
      [:div.ty-text- "• Mix competing semantic colors"]
      [:div.ty-text- "• Use faint text on saturated backgrounds"]
      [:div.ty-text- "• Rely on color alone for meaning"]
      [:div.ty-text- "• Ignore accessibility contrast requirements"]
      [:div.ty-text- "• Use too many emphasis levels in one component"]]]]])

(defn view []
  [:div.mx-auto.px-6.space-y-8
   {:style {:max-width "1000px"}}
   ;; Header
   [:div.text-center.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-4 "Ty Design System"]
    [:p.text-lg.ty-text-.max-w-3xl.mx-auto.leading-relaxed
     "Explore the complete Ty CSS class system with semantic colors, backgrounds, and surfaces. "
     "Built for consistency, accessibility, and automatic theme adaptation."]

    [:div.flex.flex-wrap.gap-3.justify-center.mt-6
     [:span.px-3.py-1.ty-bg-primary-.ty-text-primary.rounded-full.text-sm.font-medium "5-Variant System"]
     [:span.px-3.py-1.ty-bg-success-.ty-text-success.rounded-full.text-sm.font-medium "Semantic Colors"]
     [:span.px-3.py-1.ty-bg-warning-.ty-text-warning.rounded-full.text-sm.font-medium "Theme Adaptive"]
     [:span.px-3.py-1.ty-bg-neutral-.ty-text-neutral.rounded-full.text-sm.font-medium "Accessible"]]]

   ;; Theme toggle
   (theme-toggle)

   ;; Main content sections
   (text-variants-demo)
   (background-variants-demo)
   (surface-classes-demo)
   (practical-examples)
   (code-examples)
   (usage-guidelines)

   ;; Footer summary
   [:div.ty-elevated.p-6.rounded-lg.text-center
    [:h4.text-lg.font-semibold.ty-text.mb-2 "Ready to Use ✨"]
    [:p.ty-text-
     "The Ty design system provides a complete set of semantic CSS classes for building consistent, "
     "accessible, and beautiful user interfaces. All classes automatically adapt between light and dark themes "
     "while maintaining proper contrast ratios."]]])

[:span.px-3.py-1.ty-bg-neutral-.ty-text-neutral.rounded-full.text-sm.font-medium "Accessible"]
