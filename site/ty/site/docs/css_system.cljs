(ns ty.site.docs.css-system
  "Documentation for ty CSS system"
  (:require [ty.site.docs.common :refer [code-block doc-section]]))

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   [:h1.text-3xl.font-bold.ty-text.mb-4 "Ty CSS System"]
   [:p.text-lg.ty-text-.mb-8 "Understanding the semantic color system and design tokens."]

   (doc-section "Core Principle"
                [:div.ty-elevated.rounded-lg.p-6
                 [:p.ty-text.mb-4
                  "Use TY classes for colors and semantic meaning, Tailwind for layout and spacing."]
                 [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
                  [:div
                   [:h3.font-medium.ty-text+.mb-2 "TY Classes For:"]
                   [:ul.space-y-1.text-sm
                    [:li.ty-text- "• Background colors (ty-bg-primary)"]
                    [:li.ty-text- "• Text colors (ty-text-success)"]
                    [:li.ty-text- "• Border colors (ty-border-danger)"]
                    [:li.ty-text- "• Surface levels (ty-elevated)"]]]
                  [:div
                   [:h3.font-medium.ty-text+.mb-2 "Tailwind For:"]
                   [:ul.space-y-1.text-sm
                    [:li.ty-text- "• Spacing (p-4, m-2)"]
                    [:li.ty-text- "• Layout (flex, grid)"]
                    [:li.ty-text- "• Typography (text-xl, font-bold)"]
                    [:li.ty-text- "• Sizing (w-full, h-64)"]]]]])

   (doc-section "5-Variant Text System"
                [:div.space-y-3
                 [:div.flex.items-center.gap-4
                  [:span.ty-text++.text-lg "ty-text++"]
                  [:span.ty-text- "Maximum emphasis - Headers, critical text"]]
                 [:div.flex.items-center.gap-4
                  [:span.ty-text+.text-lg "ty-text+"]
                  [:span.ty-text- "High emphasis - Subheadings, labels"]]
                 [:div.flex.items-center.gap-4
                  [:span.ty-text.text-lg "ty-text"]
                  [:span.ty-text- "Base emphasis - Body text"]]
                 [:div.flex.items-center.gap-4
                  [:span.ty-text-.text-lg "ty-text-"]
                  [:span.ty-text- "Reduced emphasis - Secondary content"]]
                 [:div.flex.items-center.gap-4
                  [:span.ty-text--.text-lg "ty-text--"]
                  [:span.ty-text- "Minimal emphasis - Disabled, hints"]]])

   (doc-section "3-Variant Background System"
                [:div.space-y-4
                 [:div.grid.grid-cols-1.md:grid-cols-3.gap-4
                  [:div.ty-bg-primary+.p-4.rounded
                   [:p.ty-text++ "ty-bg-primary+"]
                   [:p.ty-text "Stronger background"]]
                  [:div.ty-bg-primary.p-4.rounded
                   [:p.ty-text++ "ty-bg-primary"]
                   [:p.ty-text "Base background"]]
                  [:div.ty-bg-primary-.p-4.rounded
                   [:p.ty-text++ "ty-bg-primary-"]
                   [:p.ty-text "Softer background"]]]
                 [:p.ty-text-.text-sm.mt-4
                  "Background variants available for: primary, secondary, success, danger, warning, info, neutral"]])

   (doc-section "Surface Levels"
                [:div.space-y-4
                 [:div.ty-canvas.p-4.rounded.border.ty-border
                  [:p.ty-text+ "ty-canvas"]
                  [:p.ty-text- "App background - lowest level"]]
                 [:div.ty-content.p-4.rounded.border.ty-border
                  [:p.ty-text+ "ty-content"]
                  [:p.ty-text- "Main content areas"]]
                 [:div.ty-elevated.p-4.rounded
                  [:p.ty-text+ "ty-elevated"]
                  [:p.ty-text- "Cards, panels - includes shadow"]]
                 [:div.ty-floating.p-4.rounded
                  [:p.ty-text+ "ty-floating"]
                  [:p.ty-text- "Modals, dropdowns - highest elevation"]]
                 [:div.ty-input.p-4.rounded.border.ty-border
                  [:p.ty-text+ "ty-input"]
                  [:p.ty-text- "Form control backgrounds"]]])

   (doc-section "Border System"
                [:div.space-y-4
                 [:div.border.ty-border++.p-4.rounded
                  [:p.ty-text+ "ty-border++"]
                  [:p.ty-text- "Maximum border emphasis"]]
                 [:div.border.ty-border+.p-4.rounded
                  [:p.ty-text+ "ty-border+"]
                  [:p.ty-text- "Strong border"]]
                 [:div.border.ty-border.p-4.rounded
                  [:p.ty-text+ "ty-border"]
                  [:p.ty-text- "Normal border"]]
                 [:div.border.ty-border-.p-4.rounded
                  [:p.ty-text+ "ty-border-"]
                  [:p.ty-text- "Soft border"]]
                 [:div.border.ty-border--.p-4.rounded
                  [:p.ty-text+ "ty-border--"]
                  [:p.ty-text- "Minimal border"]]])

   (doc-section "Semantic Colors"
                [:div.grid.grid-cols-2.md:grid-cols-4.gap-4
                 [:div.ty-bg-primary.p-4.rounded.text-center
                  [:p.ty-text++ "Primary"]
                  [:p.ty-text- "Main actions"]]
                 [:div.ty-bg-secondary.p-4.rounded.text-center
                  [:p.ty-text++ "Secondary"]
                  [:p.ty-text- "Alternative actions"]]
                 [:div.ty-bg-success.p-4.rounded.text-center
                  [:p.ty-text++ "Success"]
                  [:p.ty-text- "Positive feedback"]]
                 [:div.ty-bg-danger.p-4.rounded.text-center
                  [:p.ty-text++ "Danger"]
                  [:p.ty-text- "Errors, warnings"]]
                 [:div.ty-bg-warning.p-4.rounded.text-center
                  [:p.ty-text++ "Warning"]
                  [:p.ty-text- "Caution states"]]
                 [:div.ty-bg-info.p-4.rounded.text-center
                  [:p.ty-text++ "Info"]
                  [:p.ty-text- "Informational"]]
                 [:div.ty-bg-neutral.p-4.rounded.text-center
                  [:p.ty-text++ "Neutral"]
                  [:p.ty-text- "Default, muted"]]])

   (doc-section "Usage Examples"
                [:div.space-y-6
                 [:div
                  [:h3.text-lg.font-medium.ty-text.mb-2 "Card Component"]
                  (code-block "<!-- TY for colors, Tailwind for layout -->
<div class=\"ty-elevated p-6 rounded-lg\">
  <h2 class=\"ty-text++ text-xl font-bold mb-4\">Card Title</h2>
  <p class=\"ty-text- text-sm\">Card description</p>
  <button class=\"ty-bg-primary ty-text++ px-4 py-2 rounded\">
    Action
  </button>
</div>")]

                 [:div
                  [:h3.text-lg.font-medium.ty-text.mb-2 "Alert Components"]
                  [:div.space-y-3.mb-4
                   [:div.ty-bg-success-.ty-border-success.border.rounded-lg.p-4
                    [:h4.ty-text-success++.font-semibold "Success!"]
                    [:p.ty-text-success.text-sm "Operation completed successfully."]]
                   [:div.ty-bg-danger-.ty-border-danger.border.rounded-lg.p-4
                    [:h4.ty-text-danger++.font-semibold "Error!"]
                    [:p.ty-text-danger.text-sm "Something went wrong."]]
                   [:div.ty-bg-warning-.ty-border-warning.border.rounded-lg.p-4
                    [:h4.ty-text-warning++.font-semibold "Warning!"]
                    [:p.ty-text-warning.text-sm "Please review before proceeding."]]]
                  (code-block "<div class=\"ty-bg-success- ty-border-success border rounded-lg p-4\">
  <h4 class=\"ty-text-success++ font-semibold\">Success!</h4>
  <p class=\"ty-text-success text-sm\">Operation completed successfully.</p>
</div>")]

                 [:div
                  [:h3.text-lg.font-medium.ty-text.mb-2 "Form Fields"]
                  [:div.space-y-3.mb-4
                   [:div
                    [:label.ty-text+.block.text-sm.font-medium.mb-1 "Default Input"]
                    [:input.ty-input.ty-border.border.rounded-md.px-3.py-2.w-full
                     {:type "text" :placeholder "Enter text..."}]]
                   [:div
                    [:label.ty-text+.block.text-sm.font-medium.mb-1 "Success State"]
                    [:input.ty-input.ty-border-success.border.rounded-md.px-3.py-2.w-full
                     {:type "text" :value "Valid input"}]]
                   [:div
                    [:label.ty-text+.block.text-sm.font-medium.mb-1 "Error State"]
                    [:input.ty-input.ty-border-danger.border.rounded-md.px-3.py-2.w-full
                     {:type "text" :value "Invalid input"}]
                    [:p.ty-text-danger.text-sm.mt-1 "This field is required"]]]
                  (code-block "<label class=\"ty-text+ block text-sm font-medium\">
  Email Address
</label>
<input class=\"ty-input ty-border border rounded-md px-3 py-2 w-full\">

<!-- Error state -->
<input class=\"ty-input ty-border-danger border rounded-md px-3 py-2 w-full\">
<p class=\"ty-text-danger text-sm mt-1\">This field is required</p>")]])

   (doc-section "Best Practices"
                [:div.ty-elevated.rounded-lg.p-6
                 [:ul.space-y-3
                  [:li.flex.items-start.gap-2
                   [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
                   [:p.ty-text- "Use semantic colors (primary, danger) instead of visual colors (blue, red)"]]
                  [:li.flex.items-start.gap-2
                   [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
                   [:p.ty-text- "Combine TY classes for theming with Tailwind utilities for layout"]]
                  [:li.flex.items-start.gap-2
                   [:ty-icon.ty-text-success.mt-0.5 {:name "check" :size "sm"}]
                   [:p.ty-text- "Use text variants (++, +, base, -, --) to establish visual hierarchy"]]
                  [:li.flex.items-start.gap-2
                   [:ty-icon.ty-text-danger.mt-0.5 {:name "x" :size "sm"}]
                   [:p.ty-text- "Don't use Tailwind color classes (bg-blue-500) - use TY semantic colors"]]
                  [:li.flex.items-start.gap-2
                   [:ty-icon.ty-text-danger.mt-0.5 {:name "x" :size "sm"}]
                   [:p.ty-text- "Don't mix different semantic contexts (e.g., danger background with success text)"]]]])])
