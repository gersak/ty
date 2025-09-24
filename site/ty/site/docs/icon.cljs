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
     "Dynamic icon rendering with size variants and animation effects. Framework-agnostic icon system that works across ClojureScript, React, HTMX, and vanilla JavaScript."]]

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
      [:ty-icon {:name "check"
                 :size "sm"}]
      [:ty-icon {:name "plus"
                 :size "md"}]
      [:ty-icon {:name "trash"
                 :size "lg"}]
      [:ty-icon {:name "settings"
                 :size "xl"}]]

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
       [:ty-icon.ty-text {:name "star"
                          :size "xs"}]
       [:p.ty-text--.text-xs.mt-1 "xs"]]
      [:div.text-center
       [:ty-icon.ty-text {:name "star"
                          :size "sm"}]
       [:p.ty-text--.text-xs.mt-1 "sm"]]
      [:div.text-center
       [:ty-icon.ty-text {:name "star"
                          :size "md"}]
       [:p.ty-text--.text-xs.mt-1 "md"]]
      [:div.text-center
       [:ty-icon.ty-text {:name "star"
                          :size "lg"}]
       [:p.ty-text--.text-xs.mt-1 "lg"]]
      [:div.text-center
       [:ty-icon.ty-text {:name "star"
                          :size "xl"}]
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
      [:ty-icon.ty-text-primary {:name "info"
                                 :size "md"}]
      [:ty-icon.ty-text-success {:name "check-circle"
                                 :size "md"}]
      [:ty-icon.ty-text-warning {:name "alert-triangle"
                                 :size "md"}]
      [:ty-icon.ty-text-danger {:name "x-circle"
                                :size "md"}]
      [:ty-icon.ty-text-secondary {:name "settings"
                                   :size "md"}]]

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
       [:ty-icon.ty-text-primary {:name "loader"
                                  :size "md"
                                  :spin true}]
       [:p.ty-text--.text-xs.mt-2 "Spin"]]
      [:div.text-center
       [:ty-icon.ty-text-success {:name "heart"
                                  :size "md"
                                  :pulse true}]
       [:p.ty-text--.text-xs.mt-2 "Pulse"]]
      [:div.text-center
       [:ty-icon.ty-text-warning {:name "refresh-cw"
                                  :size "md"
                                  :spin true
                                  :tempo "slow"}]
       [:p.ty-text--.text-xs.mt-2 "Slow Spin"]]
      [:div.text-center
       [:ty-icon.ty-text-danger {:name "zap"
                                 :size "md"
                                 :pulse true
                                 :tempo "fast"}]
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
      [:ty-button {:flavor "primary"
                   :size "sm"}
       [:ty-icon {:slot "start"
                  :name "save"
                  :size "sm"}]
       "Save"]
      [:ty-button {:flavor "secondary"
                   :size "sm"}
       [:ty-icon {:slot "start"
                  :name "download"
                  :size "sm"}]
       "Export"]
      [:ty-button {:flavor "danger"
                   :size "sm"}
       [:ty-icon {:slot "start"
                  :name "trash"
                  :size "sm"}]
       "Delete"]
      [:ty-button {:flavor "neutral"
                   :size "sm"}
       "Next"
       [:ty-icon {:slot "end"
                  :name "arrow-right"
                  :size "sm"}]]]

     (code-block
       "<ty-button flavor=\"primary\">
  <ty-icon slot=\"start\" name=\"save\" size=\"sm\"></ty-icon>
  Save
</ty-button>

<ty-button flavor=\"neutral\">
  Next
  <ty-icon slot=\"end\" name=\"arrow-right\" size=\"sm\"></ty-icon>
</ty-button>")]]

   ;; Icon Registration Section
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text++.mb-6 "Icon Registration"]
    [:p.ty-text-.mb-6
     "Icons must be registered before use. The system supports multiple registration methods optimized for different technology stacks."]

    [:div.space-y-8
     ;; ClojureScript/Reagent
     [:div
      [:h3.text-lg.font-medium.ty-text+.mb-3.flex.items-center.gap-2
       [:ty-icon {:name "code"
                  :size "sm"}]
       "ClojureScript / Reagent / Replicant / UIx"]
      [:p.ty-text-.text-sm.mb-4
       "Preferred method: Import icon libraries and selectively register needed icons."]

      (code-block
        "(ns my.app.icons
  (:require [ty.lucide :as lucide]
            [ty.heroicons.outline :as heroicons]
            [ty.fav6.brands :as brands]
            [ty.icons :as icons]))

;; Register only the icons you need
(icons/add! {\"home\" lucide/home
             \"user\" lucide/user
             \"check\" lucide/check
             \"plus\" lucide/plus
             \"settings\" lucide/settings
             \"react\" brands/react
             \"python\" brands/python})

;; Usage in components
[:ty-icon {:name \"home\"}]
[:ty-icon {:name \"check\" :class \"ty-text-success\"}]"
        "clojure")]

     ;; React/Next.js
     [:div
      [:h3.text-lg.font-medium.ty-text+.mb-3.flex.items-center.gap-2
       [:ty-icon {:name "code"
                  :size "sm"}]
       "React / Next.js / TypeScript"]
      [:p.ty-text-.text-sm.mb-4
       "Preferred method: Create a separate icons.js file for centralized registration."]

      (code-block
        "// public/icons.js - Icon registration script
(function() {
  'use strict';
  
  const ICON_DEFINITIONS = {
    'home': '<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\">...</svg>',
    'user': '<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\">...</svg>',
    'check': '<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\">...</svg>',
    'plus': '<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\">...</svg>',
    // ... more icons
  };
  
  function initializeIcons() {
    if (window.ty && window.ty.icons) {
      window.ty.icons.add(ICON_DEFINITIONS);
      console.log('✅ Icons registered successfully');
    } else {
      setTimeout(initializeIcons, 50); // Retry
    }
  }
  
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeIcons);
  } else {
    initializeIcons();
  }
})();

// In your HTML
<script src=\"https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.js\"></script>
<script src=\"/icons.js\"></script>

// Usage in JSX
<ty-icon name=\"home\"></ty-icon>
<ty-icon name=\"user\" size=\"md\"></ty-icon>"
        "javascript")]

     ;; HTMX/Flask/Django  
     [:div
      [:h3.text-lg.font-medium.ty-text+.mb-3.flex.items-center.gap-2
       [:ty-icon {:name "code"
                  :size "sm"}]
       "HTMX / Flask / Django / Server-Side"]
      [:p.ty-text-.text-sm.mb-4
       "Preferred method: Use sprite sheets for optimal performance with server-rendered content."]

      (code-block
        "<!-- Method 1: SVG Sprite Sheet (Recommended) -->
<!DOCTYPE html>
<html>
<head>
  <script src=\"https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.js\"></script>
</head>
<body>
  <!-- Hidden sprite sheet -->
  <svg style=\"display:none\">
    <symbol id=\"icon-check\" viewBox=\"0 0 24 24\">
      <path d=\"M20 6 9 17l-5-5\"/>
    </symbol>
    <symbol id=\"icon-home\" viewBox=\"0 0 24 24\">
      <path d=\"m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z\"/>
    </symbol>
    <symbol id=\"icon-user\" viewBox=\"0 0 24 24\">
      <path d=\"M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2\"/>
      <circle cx=\"12\" cy=\"7\" r=\"4\"/>
    </symbol>
  </svg>

  <script>
    // Auto-register all sprites with 'icon-' prefix
    ty.icons.autoRegisterSprites('symbol[id^=\"icon-\"]');
  </script>

  <!-- Clean usage -->
  <ty-icon name=\"check\"></ty-icon>
  <ty-icon name=\"home\" size=\"lg\"></ty-icon>
  
  <!-- In HTMX responses -->
  <div hx-get=\"/status\" hx-target=\"#status\">
    <ty-icon name=\"user\"></ty-icon> Check Status
  </div>
</body>
</html>

<!-- Method 2: Dynamic Registration -->
<script>
ty.icons.register('*', async (name) => {
  const response = await fetch(`/api/icons/${name}.svg`);
  if (response.ok) return response.text();
  return null;
}, 'loader');
</script>")]

     ;; Vanilla JavaScript
     [:div
      [:h3.text-lg.font-medium.ty-text+.mb-3.flex.items-center.gap-2
       [:ty-icon {:name "code"
                  :size "sm"}]
       "Vanilla JavaScript / Static Sites"]
      [:p.ty-text-.text-sm.mb-4
       "Preferred method: Direct registration or URL-based loading for maximum flexibility."]

      (code-block
        "// Method 1: Direct Registration
ty.icons.registerIcons({
  'home': '<svg viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\">...</svg>',
  'user': '<svg viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\">...</svg>',
  'settings': '<svg viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\">...</svg>'
});

// Method 2: URL-based Loading
ty.icons.registerSources({
  'logo': '/assets/logo.svg',
  'banner': '/assets/banner.svg',
  'custom-icon': '/assets/custom.svg'
});

// Method 3: Pattern-based Dynamic Loading
ty.icons.register('lucide-*', async (name) => {
  const iconName = name.replace('lucide-', '');
  const response = await fetch(`https://cdn.jsdelivr.net/npm/lucide@latest/icons/${iconName}.svg`);
  if (response.ok) return response.text();
  return null;
}, 'loader');

// Method 4: CDN Integration
ty.icons.configure({
  basePath: 'https://cdn.example.com/icons/v2/',
  fallback: 'not-found'
});

ty.icons.register('material-*', (name) => {
  const iconName = name.replace('material-', '');
  return `${ty.icons.config.basePath}/material/${iconName}.svg`;
}, 'source');

// Usage
document.body.innerHTML = `
  <ty-icon name=\"home\"></ty-icon>
  <ty-icon name=\"logo\"></ty-icon>
  <ty-icon name=\"lucide-star\"></ty-icon>
  <ty-icon name=\"material-favorite\"></ty-icon>
`;"
        "javascript")]]]

;; JavaScript API Reference
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text++.mb-6 "JavaScript API"]
    [:div.space-y-4
     [:div
      [:h3.text-lg.font-medium.ty-text+.mb-3 "Registration Methods"]
      (code-block
        "// Icon registry (in-memory, fastest)
ty.icons.registerIcons({name: 'svg-string', ...})

// Sprite registry (DOM references, fast)  
ty.icons.registerSprites({name: '#symbol-id', ...})

// Source registry (URLs, cached after first load)
ty.icons.registerSources({name: '/path/to/icon.svg', ...})

// Loader registry (dynamic/async resolution)
ty.icons.registerLoaders({
  'pattern-*': (name) => fetch(`/api/icons/${name}`).then(r => r.text())
})

// Auto-register sprites from DOM
ty.icons.autoRegisterSprites('symbol[id^=\"icon-\"]')"
        "javascript")]

     [:div
      [:h3.text-lg.font-medium.ty-text+.mb-3 "Legacy Compatibility"]
      (code-block
        "// Legacy methods (still supported)
ty.icons.add({name: 'svg-string', ...})  // Same as registerIcons
ty.icons.set({name: 'svg-string', ...})  // Replace all icons"
        "javascript")]]]

   ;; Best Practices
   [:div.ty-elevated.rounded-lg.p-6
    [:h2.text-2xl.font-semibold.ty-text++.mb-4 "Best Practices"]
    [:div.space-y-4
     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                        :size "sm"}]
      [:div
       [:p.ty-text+.font-medium "Choose the right registration method"]
       [:p.ty-text-.text-sm "Use selective registration for ClojureScript, sprite sheets for HTMX, icon files for React"]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                        :size "sm"}]
      [:div
       [:p.ty-text+.font-medium "Register icons early"]
       [:p.ty-text-.text-sm "Load icons before your components render to avoid flickering"]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                        :size "sm"}]
      [:div
       [:p.ty-text+.font-medium "Use semantic colors for meaning"]
       [:p.ty-text-.text-sm "Apply ty-text-success, ty-text-danger, etc. to convey status"]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                        :size "sm"}]
      [:div
       [:p.ty-text+.font-medium "Match icon size to context"]
       [:p.ty-text-.text-sm "Use sm/xs for inline text, md/lg for headers, xl for hero sections"]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check"
                                        :size "sm"}]
      [:div
       [:p.ty-text+.font-medium "Use animations purposefully"]
       [:p.ty-text-.text-sm "Spin for loading states, pulse for attention, avoid overuse"]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-danger.mt-0.5 {:name "x"
                                       :size "sm"}]
      [:div
       [:p.ty-text+.font-medium "Don't add margins to icons in buttons"]
       [:p.ty-text-.text-sm "Buttons handle icon spacing automatically with slots"]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-danger.mt-0.5 {:name "x"
                                       :size "sm"}]
      [:div
       [:p.ty-text+.font-medium "Avoid mixing animation types"]
       [:p.ty-text-.text-sm "Don't use both spin and pulse on the same icon"]]]]]])
