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
- **TypeScript Components** - Modern, type-safe UI components (~40KB core)
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
- ✅ **Icon** - Icon registry with 3000+ icons available (tree-shakeable)
- ✅ **Copy** - Copy-to-clipboard for API keys, tokens, URLs
- ✅ **Tabs** - Tab navigation with content panels
- ✅ **Tab** - Individual tab component

### 🎨 Semantic Design System

- **5-Variant Color System** - From `ty-text--` (faint) to `ty-text++` (strong)
- **Automatic Dark Mode** - Intelligent emphasis flipping
- **130+ CSS Variables** - Complete customization
- **7 Semantic Colors** - primary, secondary, success, danger, warning, info, neutral
- **5 Surface Levels** - canvas, content, elevated, floating, input

### 🎯 Icon System (3000+ Icons Available)

**Core package includes the registry utility only (~2.6MB)**

```typescript
// Tree-shakeable imports (Recommended)
import { check, heart, star } from '@gersak/ty/icons/lucide'
import { registerIcons } from '@gersak/ty/icons/registry'

registerIcons({ check, heart, star })  // Only ~0.5-1KB per icon
```

**Or use the global API:**
```javascript
// After loading ty.js from CDN
window.tyIcons.register({
  'check': '<svg>...</svg>',
  'heart': '<svg>...</svg>'
})
```

**Available Icon Libraries:**
- **Lucide**: 1,636 icons (tree-shakeable)
- **Heroicons**: 4 variants
- **Material Design**: 5 variants
- **FontAwesome**: 3 variants
- **Custom SVG**: Bring your own icons

**[See it in action →](https://gersak.github.io/ty)**

---

## 🚀 Quick Start

### CDN (Fastest)

```html
<!-- Ty CSS (Required) -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css">

<!-- Ty Components -->
<script type="module" src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/ty.js"></script>

<!-- Optional: Add icons -->
<script type="module">
  import { check, heart } from 'https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/icons/lucide.js'
  window.tyIcons.register({ check, heart })
</script>
```

Then use anywhere:

```html
<ty-button flavor="primary">Hello World</ty-button>
<ty-calendar value="2024-12-25"></ty-calendar>
<ty-icon name="check"></ty-icon>
```

### NPM

```bash
npm install @gersak/ty
```

```typescript
import '@gersak/ty/css/ty.css'
import { TyButton, TyCalendar } from '@gersak/ty'
import { check, heart } from '@gersak/ty/icons/lucide'
import { registerIcons } from '@gersak/ty/icons/registry'

registerIcons({ check, heart })
```

**[See full documentation →](https://gersak.github.io/ty)**

---

## 📊 Bundle Sizes

**TypeScript Implementation (Current):**
- **Core Bundle**: ~40KB minified (all 18 components)
- **Icon Registry**: ~5KB + icons as needed (0.5-1KB each)
- **Full Suite**: ~150KB for everything
- **Tree-Shakeable**: Import only what you use

**With ClojureScript Infrastructure (Optional):**
- **Additional**: ~50KB for router, i18n, context management
- **Shared Runtime**: If already using ClojureScript

*10x smaller than the original ClojureScript-only implementation!*

---

## 🌐 Framework Integration

### React
```tsx
import { TyButton, TyCalendar } from '@gersak/ty'
import '@gersak/ty/css/ty.css'

function App() {
  return (
    <div>
      <ty-button flavor="primary" onClick={() => alert('Clicked!')}>
        Click Me
      </ty-button>
      <ty-calendar onChange={(e) => console.log(e.detail.date)} />
    </div>
  )
}
```

**Note**: React wrapper package `@gersak/ty-react` coming soon for better TypeScript integration!

### HTMX + Flask/Django
```html
<ty-input 
  hx-post="/api/search" 
  hx-trigger="input changed delay:300ms"
  hx-target="#results"
/>
```

Just works. Server-side rendering with dynamic interactions.

### Vue/Angular
```vue
<template>
  <ty-button flavor="primary" @click="handleClick">
    Click Me
  </ty-button>
</template>

<script>
import '@gersak/ty'
export default { /* ... */ }
</script>
```

Import the components, use them. Web Components are web standards.

### ClojureScript (Reagent/UIx)
```clojure
[:ty-button {:flavor "primary"
             :on-click #(js/alert "Clicked!")}
 "Click Me"]
```

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
- ~40KB core, zero runtime dependencies
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
- **ClojureScript devs**: Get ~50KB bundle increase + powerful infrastructure
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
- 📦 **Clojars (ClojureScript)**: [dev.gersak/ty](https://clojars.org/dev.gersak/ty)
- 🌐 **CDN**: [jsdelivr.net/npm/@gersak/ty](https://www.jsdelivr.net/npm/@gersak/ty)

---

## 🛠️ Development

### TypeScript Components (Port 3000)
```bash
npm run dev:ts
# Opens: http://localhost:3000/
# Features: HMR, source maps, console.log preserved
```

### ClojureScript Site (Port 8000)
```bash
npm run dev
# Opens: http://localhost:8000/
# Features: Live reload, documentation site
```

### Build Everything
```bash
npm run build:ts      # TypeScript components
npm run build         # ClojureScript site
```

**See [TYPESCRIPT_DEV_GUIDE.md](./TYPESCRIPT_DEV_GUIDE.md) for detailed development instructions.**

---

## 🎯 Coming Soon

- 🚧 Better mobile adaptations
- 🚧 More layout components
- 🚧 Enhanced accessibility features
- 🚧 `@gersak/ty-react` - React wrapper package with full TypeScript support
- 🚧 Storybook integration
- 🚧 Comprehensive test suite

---

**Built with TypeScript for universal compatibility. Powered by ClojureScript for advanced features. Framework-agnostic by design.**

**MIT Licensed. Work in progress. Getting better every day.**
