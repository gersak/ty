(ns ty.site.views.getting-started)

(defn view []
  [:div.ty-elevated.p-8.rounded-lg
   [:h1.text-2xl.font-bold.ty-text.mb-6 "Getting Started"]
   [:p.ty-text-.mb-8 "Quick start guide for using Ty Components in your project."]

   [:div.space-y-8
    ;; Installation
    [:div
     [:h2.text-xl.font-semibold.ty-text.mb-4 "Installation"]
     [:div.ty-bg-neutral-.p-4.rounded-lg.font-mono.text-sm.ty-text-
      "npm install @gersak/ty"]]

    ;; Basic Usage
    [:div
     [:h2.text-xl.font-semibold.ty-text.mb-4 "Basic Usage"]
     [:div.ty-bg-neutral-.p-4.rounded-lg.font-mono.text-sm.ty-text-
      [:div "<!-- Include CSS and JS -->"]
      [:div "&lt;link rel=\"stylesheet\" href=\"path/to/ty.css\"&gt;"]
      [:div "&lt;script src=\"path/to/ty.js\"&gt;&lt;/script&gt;"]
      [:div.mt-2 ""]
      [:div "<!-- Use components -->"]
      [:div "&lt;ty-button variant=\"primary\"&gt;Click me&lt;/ty-button&gt;"]]]

    ;; Framework Integration
    [:div
     [:h2.text-xl.font-semibold.ty-text.mb-4 "Framework Integration"]
     [:div.grid.gap-4.md:grid-cols-2
      [:div.ty-bg-neutral-.p-4.rounded-lg
       [:h4.font-medium.ty-text.mb-2 "React"]
       [:pre.text-sm.ty-text-.font-mono "import { TyButton } from '@gersak/ty-react'\n\n&lt;TyButton variant=\"primary\"&gt;\n  Click me\n&lt;/TyButton&gt;"]]
      [:div.ty-bg-neutral-.p-4.rounded-lg
       [:h4.font-medium.ty-text.mb-2 "Vue"]
       [:pre.text-sm.ty-text-.font-mono "&lt;template&gt;\n  &lt;ty-button variant=\"primary\"&gt;\n    Click me\n  &lt;/ty-button&gt;\n&lt;/template&gt;"]]]]

    [:div.ty-bg-info-.border.border-ty-border-info.p-4.rounded-lg
     [:div.flex.items-start
      [:ty-icon {:name "info"
                 :size "sm"
                 :class "ty-text-info mt-0.5 mr-3 flex-shrink-0"}]
      [:div
       [:h4.font-medium.ty-text-info "Documentation"]
       [:p.ty-text-info-.text-sm "For complete documentation and examples, visit the demo application and explore the component showcase scenarios above."]]]]]])
