<blockquote style="color: #414863">
<p><em>"After playing around with Replicant, I realized I could build Web Components without React — actually, even without Replicant.</em></p>

<p><em>I'm wondering if it's called 'Replicant' because of <strong>Blade Runner</strong> (I love Blade Runner).</em></p>

<p><em>Maybe I could name my own library something similar... Tyrell? No, that feels pretentious.</em></p>

<p><em>Anyway, I don't really want to type something long like <code>tyrell-button</code>. It should be shorter — maybe <code>ty-button</code>.</em></p>

<p><strong><em>Yes! Let's call it ty."</em></strong></p>
</blockquote>

# ty - Modern Web Components Built on Standards

[![jsDelivr](https://data.jsdelivr.com/v1/package/npm/@gersak/ty/badge)](https://www.jsdelivr.com/package/npm/@gersak/ty)
[![NPM Version](https://img.shields.io/npm/v/@gersak/ty.svg)](https://www.npmjs.com/package/@gersak/ty)
[![Bundle Size](https://img.shields.io/bundlephobia/minzip/@gersak/ty)](https://bundlephobia.com/package/@gersak/ty)
[![Clojars Project](https://img.shields.io/clojars/v/dev.gersak/ty-icons.svg)](https://clojars.org/dev.gersak/ty-icons)
[![Clojars Project](https://img.shields.io/clojars/v/dev.gersak/ty.svg)](https://clojars.org/dev.gersak/ty)

**Framework-agnostic web components with a unique dual architecture:**
- **TypeScript Components** - Modern, type-safe UI components
- **ClojureScript Infrastructure** - Advanced routing, i18n, and documentation site
- **Zero Dependencies** - Built on web standards that won't break

Works with React, Vue, HTMX—or no framework at all.

## ⚠️ Work in Progress - But Ready to Use

**ty is actively being developed.** Components work in production, examples run smoothly, but expect rough edges. This is a real project with a real vision - web components that work everywhere, built on standards that won't break next year.

## ✨ What's Included

### 18 Production-Ready Components

- ✅ **Button** - Semantic buttons with flavors and sizes
- ✅ **Input** - Enhanced inputs with validation and formatting
- ✅ **Textarea** - Auto-resizing textarea
- ✅ **Checkbox** - Styled checkbox with form integration
- ✅ **Calendar** - Full calendar system with navigation and custom rendering
- ✅ **Calendar Month** - Standalone month view
- ✅ **Calendar Navigation** - Calendar controls
- ✅ **Date Picker** - Date selection with calendar popup
- ✅ **Dropdown** - Rich dropdown with HTML content support
- ✅ **Multiselect** - Multi-select with tags and search
- ✅ **Modal** - Accessible modals with focus trapping
- ✅ **Popup** - Positioned popovers
- ✅ **Tooltip** - Smart tooltips with positioning
- ✅ **Tag** - Chip/tag component with removable option
- ✅ **Icon** - Icon component with registry system
- ✅ **Copy** - Copy-to-clipboard for API keys, tokens, URLs
- ✅ **Tabs** - Tab navigation with content panels
- ✅ **Tab** - Individual tab component

### 🎨 Semantic Design System

- **5-Variant Color System** - From `ty-text--` (faint) to `ty-text++` (strong)
- **Automatic Dark Mode** - Intelligent emphasis flipping
- **130+ CSS Variables** - Complete customization
- **7 Semantic Colors** - primary, secondary, success, danger, warning, info, neutral
- **5 Surface Levels** - canvas, content, elevated, floating, input

**[See it in action →](https://gersak.github.io/ty)**

---

## 🚀 Quick Start

### CDN (Fastest)

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>My App</title>
  
  <!-- Ty CSS and JS from CDN -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.css">
  <script src="https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.js"></script>
</head>
<body>
  <ty-button flavor="primary">Hello World</ty-button>
  <ty-calendar value="2024-12-25"></ty-calendar>
</body>
</html>
```

### NPM

```bash
npm install @gersak/ty
```

```typescript
import '@gersak/ty/css/ty.css'
import { TyButton, TyCalendar } from '@gersak/ty'
```

**[See full documentation →](https://gersak.github.io/ty)**

---

## 🎯 Icon System (Optional)

Icons are **optional** and loaded separately:

```javascript
// Import specific icons (tree-shakeable)
import { check, heart, save } from '@gersak/ty/icons/lucide'

// Register with global API
window.tyIcons.register({ check, heart, save })
```

Then use:

```html
<ty-icon name="check"></ty-icon>
```

**Available Icon Libraries:**
- **Lucide**: 1,636 icons (tree-shakeable)
- **Heroicons**: 4 variants
- **Material Design**: 5 variants
- **FontAwesome**: 3 variants

⚠️ **Import only what you need** - importing all icons would load ~900KB!

---

## 🌐 Framework Integration

### React

**For React projects, use the React wrapper package:**

```bash
npm install @gersak/ty-react
```

```jsx
import React, { useState } from 'react'
import { TyButton, TyInput, TyIcon } from '@gersak/ty-react'
import { check, heart, save } from '@gersak/ty/icons/lucide'

// Register icons
window.tyIcons.register({ check, heart, save })

function App() {
  const [name, setName] = useState('')

  return (
    <div className="ty-elevated p-6 rounded-lg">
      <h2 className="ty-text++ text-xl mb-4">Hello Ty!</h2>
      
      <TyInput
        value={name}
        placeholder="Enter name"
        onChange={(e) => setName(e.target.value)}
      />
      
      <TyButton 
        flavor="primary"
        onClick={() => alert('Hello ' + name)}
      >
        <TyIcon name="check" />
        Submit
      </TyButton>
    </div>
  )
}
```

**Setup HTML (include Ty CSS and JS):**

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>My React App</title>
  
  <!-- Ty CSS and JS from CDN -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.css">
  <script src="https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.js"></script>
</head>
<body>
  <div id="root"></div>
</body>
</html>
```

### HTMX + Flask/Django

```html
<ty-input 
  hx-post="/api/search" 
  hx-trigger="input changed delay:300ms"
  hx-target="#results"
  placeholder="Search..."
/>
```

Just works. Server-side rendering with dynamic interactions.

Import the components, use them. Web Components are web standards.

### ClojureScript (Reagent/UIx)

For ClojureScript projects, use the React wrapper with tree-shakeable icon imports:

**Add dependencies to `deps.edn`:**

```clojure
{:deps {com.pitch/uix.core {:mvn/version "1.1.0"}
        com.pitch/uix.dom {:mvn/version "1.1.0"}
        dev.gersak/ty-icons {:mvn/version "LATEST"}  ; Tree-shakeable icons
        dev.gersak/ty {:mvn/version "LATEST"}}}     ; Optional: Router, i18n, layout
```

**Import icons from ClojureScript (not JavaScript):**

```clojure
(ns my-app.core
  (:require ["@gersak/ty-react" :as ty]
            [ty.lucide :as lucide]))  ; ← ClojureScript import, not JavaScript!

;; Register only the icons you need (Google Closure Compiler tree-shakes unused icons)
(js/window.tyIcons.register
  #js {"check" lucide/check
       "heart" lucide/heart
       "save" lucide/save})

(defn app []
  [:div.ty-elevated.p-6.rounded-lg
   [:h2.ty-text++.text-xl.mb-4 "Hello Ty!"]
   
   [:> ty/Button {:flavor "primary"
                  :on-click #(js/alert "Clicked!")}
    [:> ty/Icon {:name "check"}]
    "Submit"]])
```

**⚠️ Important: Use ClojureScript imports for tree-shaking!**

- ✅ **Correct:** `[ty.lucide :as lucide]` - Google Closure Compiler eliminates unused icons
- ❌ **Wrong:** `["@gersak/ty/icons/lucide" :refer [check]]` - Bundles ALL 1,636 icons (~897KB)

By importing from the ClojureScript artifact (`dev.gersak/ty-icons`), only the icons you reference are included in your production bundle.

**Bonus**: Built-in router, i18n, and responsive layout system via `dev.gersak/ty` Clojars package.

---

## ⚠️ Important: CSS is Required

**Ty components require the `ty.css` stylesheet to display correctly.** The CSS file contains:

- **CSS Variables** - Design tokens for all colors, surfaces, spacing, and typography
- **Utility Classes** - Semantic classes like `.ty-bg-primary`, `.ty-text++`, `.ty-elevated`
- **Theme System** - Light/dark mode definitions that swap automatically
- **Component Styles** - Base styling that components depend on

---

## 🏗️ Architecture: The Best of Both Worlds

**TypeScript Components** (`packages/core/`)
- All 18 UI components written in modern TypeScript
- Published to NPM as `@gersak/ty`
- Zero runtime dependencies
- Full `.d.ts` type definitions
- Vite build with Terser minification

**ClojureScript Infrastructure** (`packages/cljs/`)
- Tree-based routing with authorization
- Protocol-based i18n with Intl API
- Context management for responsive design
- Documentation site and examples
- Published to Clojars as `dev.gersak/ty`

**Why This Matters:**
- **TypeScript devs**: Get modern components with type safety, no ClojureScript needed
- **ClojureScript devs**: Get powerful infrastructure + TypeScript components
- **Everyone**: Components work everywhere, regardless of tech stack

---

## 🤝 Join the Effort

This project grows with community input. Every issue, PR, and discussion helps shape the direction.

**Ways to contribute:**
- 🐛 [**Report Issues**](https://github.com/gersak/ty/issues) - Found a bug? Let us know
- 🌟 [**Star on GitHub**](https://github.com/gersak/ty) - Show support for the project
- 🔧 [**Pull Requests**](https://github.com/gersak/ty/pulls) - Documentation, components, examples - all contributions matter

**Especially interested in:**
- Mobile interaction improvements
- New components
- Real-world usage feedback
- Documentation improvements
- Icon library expansions

---

## 📚 Links

- 📖 **Docs & Examples**: [gersak.github.io/ty](https://gersak.github.io/ty)
- 💻 **GitHub**: [github.com/gersak/ty](https://github.com/gersak/ty)
- 📦 **NPM (TypeScript)**: [@gersak/ty](https://www.npmjs.com/package/@gersak/ty)
- ⚛️ **NPM (React)**: [@gersak/ty-react](https://www.npmjs.com/package/@gersak/ty-react)
- 📦 **Clojars (ClojureScript)**: [dev.gersak/ty](https://clojars.org/dev.gersak/ty)
- 🌐 **CDN**: [jsdelivr.net/npm/@gersak/ty](https://www.jsdelivr.net/npm/@gersak/ty)

---

## 🎯 Coming Soon

- 🚧 Better mobile adaptations
- 🚧 Enhanced accessibility features

---

**Built with TypeScript for universal compatibility. Powered by ClojureScript for advanced features. Framework-agnostic by design.**

**MIT Licensed. Work in progress. Getting better every day.**
