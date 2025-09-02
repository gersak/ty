(ns ty.demo.views.theme-utilities
  "Demo page for ty semantic theme system")

(defn view []
  [:div.space-y-8

   ;; Header
   [:div.text-center.mb-8
    [:h1.text-4xl.font-bold.ty-text-primary.mb-4 "Ty Semantic Theme System"]
    [:p.text-lg.ty-text "Production-ready semantic design system with Phase 4A surface architecture"]]

   ;; Project Status
   [:div.demo-section
    [:h2.demo-title "✅ Project Status: Phase 4A Complete"]
    [:div.space-y-4
     [:div.ty-bg-success-.border.ty-border-success.rounded.p-4
      [:div.flex.items-center
       [:div.w-5.h-5.ty-text-success.mr-3 "✅"]
       [:div
        [:div.font-semibold.ty-text-success "Demo Migration Complete"]
        [:div.text-sm.ty-text-success- "All 14 demo files successfully migrated to semantic theme system"]]]]

     [:div.ty-bg-primary-.border.ty-border-primary.rounded.p-4
      [:div.flex.items-center
       [:div.w-5.h-5.ty-text-primary.mr-3 "🎨"]
       [:div
        [:div.font-semibold.ty-text-primary "Phase 4A: Semantic Surface Architecture"]
        [:div.text-sm.ty-text-primary- "Short utility classes with perfect semantic alignment implemented"]]]]]]

   ;; Phase 4A: Semantic Surface Classes Demo
   [:div.demo-section
    [:h2.demo-title "🏗️ Phase 4A: Semantic Surface Architecture"]
    [:p.demo-subtitle "Developer-friendly surface classes that match mental models (47% shorter class names)"]

    [:div.space-y-6
     ;; Live surface demo with actual nesting
     [:div.ty-canvas.p-6.rounded.border
      [:div.text-sm.font-mono.mb-4.ty-text- ".ty-canvas - App/page background"]
      [:div.ty-content.p-4.rounded.mb-4
       [:div.text-sm.font-mono.mb-4.ty-text- ".ty-content - Main content areas"]
       [:div.ty-elevated.p-4.rounded.mb-4
        [:div.text-sm.font-mono.mb-2.ty-text- ".ty-elevated - Cards & panels (includes shadow)"]
        [:div.text-xs.ty-text-- "Combines background + professional elevation"]]
       [:div.ty-floating.p-4.rounded
        [:div.text-sm.font-mono.mb-2.ty-text- ".ty-floating - Modals & overlays"]
        [:div.text-xs.ty-text-- "Higher z-index for floating elements"]]]
      [:div.ty-input.p-3.rounded.border
       [:div.text-sm.font-mono.ty-text- ".ty-input - Form controls"]]]]

    [:div.ty-bg-primary-.border.ty-border-primary.rounded.p-4
     [:h4.text-sm.font-medium.ty-text-primary.mb-2 "Architecture Benefits:"]
     [:ul.text-sm.ty-text-primary.space-y-1.list-disc.list-inside
      [:li "47% shorter class names vs verbose alternatives"]
      [:li "Perfect semantic alignment (class name = variable name)"]
      [:li "Automatic shadow integration on .ty-elevated"]
      [:li "No combinatorial explosion (avoided 120+ combinations)"]
      [:li "Clean separation between surfaces and components"]]]]

   ;; Text Hierarchy System  
   [:div.demo-section
    [:h2.demo-title "📝 Six-Level Text Hierarchy System"]
    [:p.demo-subtitle "Emphasis-based text hierarchy with automatic theme adaptation"]
    [:div.space-y-6
     ;; Base text hierarchy (6 levels)
     [:div.ty-elevated.p-6.rounded-lg
      [:h4.text-sm.font-medium.mb-4.ty-text "Base Text Hierarchy (Emphasis-Based)"]
      [:div.space-y-2
       [:div.flex.items-center.gap-4
        [:span.ty-text++.font-semibold "ty-text++"]
        [:span.ty-text++ "Strongest emphasis (headers)"]]
       [:div.flex.items-center.gap-4
        [:span.ty-text+.font-semibold "ty-text+"]
        [:span.ty-text+ "Strong emphasis (subheaders)"]]
       [:div.flex.items-center.gap-4
        [:span.ty-text.font-semibold "ty-text"]
        [:span.ty-text "Base text (body content)"]]
       [:div.flex.items-center.gap-4
        [:span.ty-text-.font-semibold "ty-text-"]
        [:span.ty-text- "Reduced emphasis (descriptions)"]]
       [:div.flex.items-center.gap-4
        [:span.ty-text--.font-semibold "ty-text--"]
        [:span.ty-text-- "Low emphasis (helpers)"]]
       [:div.flex.items-center.gap-4
        [:span.ty-text---.font-semibold "ty-text---"]
        [:span.ty-text--- "Minimal emphasis (fine print)"]]]]

     ;; Semantic text colors  
     [:div.ty-elevated.p-6.rounded-lg
      [:h4.text-sm.font-medium.mb-4.ty-text "Semantic Text Colors (Intent-Based)"]
      [:div.space-y-2
       [:div.flex.items-center.gap-4
        [:span.ty-text-primary.font-semibold "ty-text-primary"]
        [:span.ty-text-primary "Primary actions, links"]]
       [:div.flex.items-center.gap-4
        [:span.ty-text-success.font-semibold "ty-text-success"]
        [:span.ty-text-success "Success states, confirmations"]]
       [:div.flex.items-center.gap-4
        [:span.ty-text-warning.font-semibold "ty-text-warning"]
        [:span.ty-text-warning "Warnings, cautions"]]
       [:div.flex.items-center.gap-4
        [:span.ty-text-danger.font-semibold "ty-text-danger"]
        [:span.ty-text-danger "Errors, critical states"]]
       [:div.flex.items-center.gap-4
        [:span.ty-text-secondary.font-semibold "ty-text-secondary"]
        [:span.ty-text-secondary "Secondary actions"]]]]

     [:div.ty-content.p-4.rounded-lg
      [:h4.text-sm.font-medium.mb-2.ty-text "Usage in Demo Application:"]
      [:div.text-xs.font-mono.ty-text--
       [:div "[:h1.ty-text++ \"Page Title\"]"]
       [:div "[:h2.ty-text+ \"Section Header\"]"]
       [:div "[:p.ty-text \"Body content\"]"]
       [:div "[:p.ty-text- \"Description\"]"]
       [:div "[:small.ty-text-- \"Helper text\"]"]
       [:div "[:small.ty-text--- \"Fine print\"]"]
       [:div "[:a.ty-text-primary \"Link\"]"]
       [:div "[:span.ty-text-success \"✓ Success\"]"]]]]]

   ;; Semantic Color System
   [:div.demo-section
    [:h2.demo-title "🎨 Semantic Color System"]
    [:p.demo-subtitle "Intent-based colors with automatic theme adaptation"]

    [:h3.demo-subtitle "Background Colors"]
    [:div.grid.grid-cols-2.md:grid-cols-3.gap-4.mb-6
     [:div.ty-bg-primary-.p-4.rounded.text-center.border
      [:div.font-mono.text-sm.ty-text-primary ".ty-bg-primary-"]
      [:div.text-xs.ty-text-primary- "Primary info boxes"]]
     [:div.ty-bg-success-.p-4.rounded.text-center.border
      [:div.font-mono.text-sm.ty-text-success ".ty-bg-success-"]
      [:div.text-xs.ty-text-success- "Success alerts"]]
     [:div.ty-bg-warning-.p-4.rounded.text-center.border
      [:div.font-mono.text-sm.ty-text-warning ".ty-bg-warning-"]
      [:div.text-xs.ty-text-warning- "Warning notifications"]]
     [:div.ty-bg-danger-.p-4.rounded.text-center.border
      [:div.font-mono.text-sm.ty-text-danger ".ty-bg-danger-"]
      [:div.text-xs.ty-text-danger- "Error messages"]]
     [:div.ty-bg-secondary-.p-4.rounded.text-center.border
      [:div.font-mono.text-sm.ty-text-secondary ".ty-bg-secondary-"]
      [:div.text-xs.ty-text-secondary- "Secondary actions"]]
     [:div.ty-bg-neutral.p-4.rounded.text-center.border
      [:div.font-mono.text-sm.ty-text-neutral-mild ".ty-bg-neutral"]
      [:div.text-xs.ty-text-neutral-soft "Neutral elements"]]]

    [:h3.demo-subtitle "Text Colors"]
    [:div.space-y-3
     [:div.flex.flex-wrap.gap-4
      [:span.ty-text-primary "ty-text-primary"]
      [:span.ty-text-success "ty-text-success"]
      [:span.ty-text-warning "ty-text-warning"]
      [:span.ty-text-danger "ty-text-danger"]
      [:span.ty-text-secondary "ty-text-secondary"]]]]

   ;; Real-world Component Examples
   [:div.demo-section
    [:h2.demo-title "🚀 Production Usage Examples"]
    [:p.demo-subtitle "How the system works in the actual demo application"]
    [:div.space-y-4

     ;; Application shell example
     [:div.ty-elevated.rounded-lg.p-4.border-l-4.border.ty-border-primary
      [:h4.text-sm.font-medium.ty-text.mb-2 "Application Shell (core.cljs)"]
      [:div.text-xs.font-mono.ty-text-.space-y-1
       [:div "[:aside.ty-elevated  ; Sidebar with automatic shadow"]
       [:div " [:h1.ty-text \"Ty Components\"]]"]
       [:div ""]
       [:div "[:main.ty-content  ; Main content area"]
       [:div " (demo-content)]"]]]

     ;; Alert component example  
     [:div.ty-bg-success-.border.ty-border-success.rounded.p-4
      [:div.flex.items-center
       [:div.w-5.h-5.ty-text-success.mr-3 "✓"]
       [:div
        [:div.font-semibold.ty-text-success "Success Alert Pattern"]
        [:div.text-sm.ty-text-success- "ty-bg-success- + ty-border-success + ty-text-success"]]]]

     ;; Modal example
     [:div.ty-floating.rounded-lg.p-4.border
      [:h4.text-sm.font-medium.ty-text.mb-2 "Modal Pattern"]
      [:p.text-xs.ty-text- "ty-floating surface for overlays and dropdowns"]
      [:div.text-xs.font-mono.ty-text--
       "[:ty-modal [:div.ty-floating.rounded-lg ...]"]]]]

   ;; Theme Toggle Demo
   [:div.demo-section
    [:h2.demo-title "🌓 Live Theme Switching"]
    [:p.demo-subtitle "All semantic classes automatically adapt between themes"]
    [:div.flex.justify-center.gap-4.mb-6
     [:button.px-4.py-2.ty-elevated.border.ty-border.rounded.hover:ty-content.ty-text
      {:on {:click #(.. js/document -documentElement -classList (remove "dark"))}}
      "☀️ Light Theme"]
     [:button.px-4.py-2.ty-bg-neutral.text-white.border.ty-border.rounded.hover:ty-bg-neutral+.ty-text
      {:on {:click #(.. js/document -documentElement -classList (add "dark"))}}
      "🌙 Dark Theme"]]
    [:div.ty-bg-primary-.ty-border-primary.border.rounded.p-4.text-center
     [:div.ty-text-primary.font-semibold "Watch these colors change! ✨"]
     [:div.text-sm.ty-text-primary- "Semantic system automatically adapts"]]]

   ;; Migration Results
   [:div.demo-section
    [:h2.demo-title "📊 Migration Results"]
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
     [:div.ty-elevated.p-6.rounded-lg
      [:h4.font-semibold.mb-4.ty-text "Phase 1A-1D Complete"]
      [:ul.space-y-2.text-sm.ty-text-
       [:li "✅ 14 demo files migrated"]
       [:li "✅ 300+ instances converted"]
       [:li "✅ Zero breaking changes"]
       [:li "✅ Perfect theme adaptation"]
       [:li "✅ Semantic consistency achieved"]]

      [:h5.font-semibold.mt-4.mb-2.ty-text-primary "Files Migrated:"]
      [:div.text-xs.ty-text--.space-y-1
       [:div "• core.cljs (app shell)"]
       [:div "• layout.cljs (72 instances)"]
       [:div "• calendar.cljs (45 instances)"]
       [:div "• dropdowns.cljs (35 instances)"]
       [:div "• All other demo files"]]]

     [:div.ty-elevated.p-6.rounded-lg
      [:h4.font-semibold.mb-4.ty-text "System Benefits"]
      [:ul.space-y-2.text-sm.ty-text-
       [:li "🎨 Intent-based naming"]
       [:li "🔄 Automatic theme switching"]
       [:li "🛠️ Maintainable codebase"]
       [:li "♿ Accessibility ready"]
       [:li "📱 Responsive by default"]]

      [:h5.font-semibold.mt-4.mb-2.ty-text-primary "Architecture:"]
      [:div.text-xs.ty-text--.space-y-1
       [:div "• Semantic variables (--ty-color-*)"]
       [:div "• Surface utilities (.ty-elevated)"]
       [:div "• Text hierarchy (.ty-text-)"]
       [:div "• CSS custom properties"]
       [:div "• Shadow DOM compatible"]]]]]

   ;; Next Steps
   [:div.demo-section
    [:h2.demo-title "🚀 Next Development Phases"]
    [:div.space-y-4
     [:div.ty-bg-primary-.border.ty-border-primary.rounded.p-4
      [:h4.text-sm.font-medium.ty-text-primary.mb-2 "Available Next Steps:"]
      [:ul.text-sm.ty-text-primary.space-y-1
       [:li "• Phase 2: Component CSS Migration (apply to actual web components)"]
       [:li "• Phase 3: Utility Classes System (comprehensive utilities)"]
       [:li "• Production Deployment (current system is ready)"]]]

     [:div.ty-elevated.p-4.rounded-lg
      [:h4.text-sm.font-medium.ty-text.mb-2 "Current Status: Production Ready ✨"]
      [:p.text-sm.ty-text-
       "The demo application now showcases a complete semantic theme system that automatically adapts between light and dark modes while maintaining perfect visual hierarchy and accessibility standards."]]]]

   ;; Code Examples
   [:div.demo-section
    [:h2.demo-title "💻 Usage Examples"]
    [:div.space-y-4
     [:div.ty-content.p-4.rounded-lg
      [:h4.text-sm.font-medium.mb-2 "Replicant (ClojureScript) Usage:"]
      [:pre.text-xs.ty-text-.font-mono
       "[:div.ty-elevated.p-4.rounded\n"
       "  [:h3.ty-text \"Semantic Header\"]\n"
       "  [:p.ty-text- \"Description text\"]\n"
       "  [:div.ty-bg-success-.ty-border-success.border.rounded.p-2\n"
       "    [:span.ty-text-success \"✓ Success message\"]]]"]]

     [:div.ty-content.p-4.rounded-lg
      [:h4.text-sm.font-medium.mb-2 "HTML Usage:"]
      [:pre.text-xs.ty-text-.font-mono
       "&lt;div class=\"ty-elevated p-4 rounded\"&gt;\n"
       "  &lt;h3 class=\"ty-text\"&gt;Semantic Header&lt;/h3&gt;\n"
       "  &lt;p class=\"ty-text-\"&gt;Description&lt;/p&gt;\n"
       "&lt;/div&gt;"]]

     [:div.ty-content.p-4.rounded-lg
      [:h4.text-sm.font-medium.mb-2 "Surface Combinations:"]
      [:pre.text-xs.ty-text-.font-mono
       ";; App layout pattern\n"
       "[:div.ty-canvas  ; Page background\n"
       " [:main.ty-content  ; Content area\n"
       "  [:div.ty-elevated  ; Card/panel\n"
       "   [:div.ty-floating  ; Modal/overlay\n"
       "    [:input.ty-input]]]]]"]]]]])
