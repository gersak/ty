# Ty - ClojureScript-First Web Components (Work in Progress)

[![jsDelivr](https://data.jsdelivr.com/v1/package/npm/@gersak/ty/badge)](https://www.jsdelivr.com/package/npm/@gersak/ty)
[![NPM Version](https://img.shields.io/npm/v/@gersak/ty.svg)](https://www.npmjs.com/package/@gersak/ty)
[![Bundle Size](https://img.shields.io/bundlephobia/minzip/@gersak/ty)](https://bundlephobia.com/package/@gersak/ty)

**Ty** is a work-in-progress ClojureScript component library built on Web Components, designed primarily for the Clojure/Script community with unique advantages: zero JS dependencies, minimal ClojureScript deps, and seamless Google Closure compiler integration.

<div align="center">

**🎯 ClojureScript-First** • **📱 Desktop/Mobile Adaptive** • **⚡ Zero JS Dependencies** • **🌐 Framework Agnostic**

</div>

## 🚧 Current Status

**Phase: Experimental → Stable Foundation**

### ✅ **Working Today**
- Core components (Button, Input, Calendar, Dropdown, Modal)
- 5-variant semantic design system with automatic dark mode
- React wrappers via `@gersak/ty-react`
- HTMX integration example

### 🎯 **2025 Roadmap**
1. **Tabs component** - Missing piece for common UI patterns
2. **Mobile adaptations** - Touch-optimized interactions for all components  
3. **Resize observers & translation** - Infrastructure for advanced layouts

### 🔮 **Vision: Adaptive UX**
Components that automatically provide optimal UX:
- **Desktop**: Hover states, keyboard navigation, precise interactions
- **Mobile**: Touch-friendly sizing, swipe gestures, haptic feedback

## 💡 Why Ty for ClojureScript?

### **Bundle Size Advantage**
When you're already using ClojureScript:
- **Ty adds ~50KB** (not the full 80KB) due to shared runtime
- **Full system**: ~240KB includes app logic + Ty + ClojureScript
- **Much lower incremental cost** than any JavaScript UI library

### **Zero Dependencies + Google Closure**
- No npm dependency conflicts
- Advanced optimizations work perfectly
- Tree shaking that actually eliminates unused code
- Lazy loading with Shadow-cljs integration

```clojure
;; ClojureScript exclusive features
(router/navigate! ::profile {:user-id 123})           ; Built-in router
(i18n/format-currency 1234.56 :locale "en-US")       ; Protocol-based i18n
(layout/container-width)                              ; Context-aware responsive
```

## 🌍 Distribution Strategy

### **🥇 Clojars** (ClojureScript - Recommended)
```clojure
;; deps.edn
{:deps {dev.gersak/ty {:mvn/version "0.1.0"}}}
```
**Best experience**: Optimal bundle sizes, lazy loading, advanced compilation

### **🥈 npm** (React/Framework Integration)
```bash
npm install @gersak/ty-react
```
**React wrappers**: TypeScript definitions, event handling, ref forwarding

### **🥉 CDN** (Quick Prototyping)
```html
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.js"></script>
```
**No build required**: Works in any HTML page

## 🚀 Quick Start

### ClojureScript (Optimal Experience)
```clojure
(require '[ty.components :as ty])

[:div
 [:ty-button {:variant "primary"} "Hello Ty!"]
 [:ty-calendar {:value @selected-date
                :on-date-select #(reset! selected-date %)}]]
```

### React Integration
```jsx
import { TyButton, TyCalendar } from '@gersak/ty-react'

<TyCalendar onChange={(e) => setDate(e.detail.date)} />
```

### Vanilla HTML
```html
<ty-button variant="primary">Universal Component</ty-button>
<ty-calendar value="2024-12-25"></ty-calendar>

<script src="https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty-calendar.js"></script>
<script>ty.core.init()</script>
```

## 🎨 Semantic Design System

**5-variant color system** that automatically adapts to light/dark themes:

```css
/* Text emphasis levels */
--ty-color-primary-strong   /* Headers, critical actions */
--ty-color-primary-mild     /* Subheadings, emphasis */  
--ty-color-primary          /* Standard text */
--ty-color-primary-soft     /* Secondary content */
--ty-color-primary-faint    /* Disabled, hints */

/* Available for: primary, secondary, success, danger, warning, info, neutral */
```

**Semantic usage:**
```html
<div class="ty-elevated">                    <!-- Card with shadow -->
<button class="ty-bg-primary ty-text++">     <!-- Primary action -->
<span class="ty-text-danger ty-border-danger">  <!-- Error state -->
```

## 🧩 Available Components

```html
<!-- Form components -->
<ty-button variant="primary" size="lg">Primary Action</ty-button>
<ty-input type="email" placeholder="Enter email..." required></ty-input>
<ty-dropdown placeholder="Choose option...">
  <ty-option value="cljs">ClojureScript</ty-option>
  <ty-option value="js">JavaScript</ty-option>
</ty-dropdown>
<ty-multiselect values='["clojure", "web-components"]'></ty-multiselect>

<!-- Calendar system -->
<ty-calendar value="2024-12-25"></ty-calendar>
<ty-date-picker name="birthday" required></ty-date-picker>

<!-- Layout -->
<ty-modal backdrop="true">
  <h2>Modal Content</h2>
</ty-modal>
<ty-tooltip message="Helpful tip" placement="top">
  <span>Hover me</span>
</ty-tooltip>
```

## 📊 Bundle Analysis

| Component | Gzipped | Description |
|-----------|---------|-------------|
| **Core system** | ~80KB | Runtime + base components (50KB for ClojureScript projects) |
| **Calendar** | +12KB | Full calendar with navigation |
| **Dropdown** | +15KB | Rich dropdown with HTML content |
| **All components** | ~240KB | Complete system |

**ClojureScript advantage**: Shared runtime means much lower incremental cost.

## 🛠️ Framework Integration

### React + TypeScript
```typescript
import { TyButton, TyModal } from '@gersak/ty-react'

interface Props {
  onSave: (data: FormData) => void
}

export function MyForm({ onSave }: Props) {
  const modalRef = useRef<TyModal>(null)
  
  return (
    <TyModal ref={modalRef} backdrop>
      <TyButton onClick={() => modalRef.current?.show()}>
        Open Modal
      </TyButton>
    </TyModal>
  )
}
```

### HTMX + Flask
```html
<ty-input hx-get="/api/search" 
          hx-trigger="input changed delay:300ms"
          hx-target="#results">
```

### Vue.js
```vue
<template>
  <ty-calendar @date-select="handleDate" />
</template>

<script>
import '@gersak/ty/ty-calendar.js'
</script>
```

## 📱 Adaptive UX Examples

**Dropdown Behavior**:
- **Desktop**: Hover preview, keyboard navigation, precise clicking
- **Mobile**: Touch-friendly sizing, momentum scrolling, haptic feedback

**Calendar Interactions**:
- **Desktop**: Month navigation via keyboard, hover date previews
- **Mobile**: Swipe gestures, larger touch targets, momentum scrolling

## 🚦 When to Choose Ty

### **Perfect Fit:**
- ✅ **ClojureScript projects** (optimal experience, minimal bundle cost)
- ✅ **Multi-framework organizations** (same components everywhere)
- ✅ **Long-term stability** concerns about framework churn
- ✅ **Semantic design systems** with meaningful color tokens
- ✅ **Desktop + mobile** adaptive UX requirements

### **Consider Alternatives:**
- ❌ **React-only** with no ClojureScript (React-specific libraries may be better)
- ❌ **Immediate production needs** (remember: work in progress!)
- ❌ **Highly specific design systems** (Material Design, etc.)

## 📈 Why Work in Progress?

**Honest expectations**: Rather than overpromise, Ty communicates realistic status:
- **Core is solid**: Basic components work well today
- **Vision is clear**: Desktop/mobile adaptive UX 
- **Progress is steady**: Regular improvements with clear milestones
- **Community-driven**: Built for ClojureScript developers, by ClojureScript developers

**The bet**: Web Components + ClojureScript + semantic design will outlast framework churn.

## 🎬 Why "ty"?

<blockquote>
<p><em>"After playing around with Replicant, I realized I could build Web Components without React — actually, even without Replicant.</em></p>

<p><em>I'm wondering if it's called 'Replicant' because of <strong>Blade Runner</strong> (I love Blade Runner).</em></p>

<p><em>Maybe I could name my own library something similar... Tyrell? No, that feels too ambitious.</em></p>

<p><em>I don't really want to do something long like <code>tyrell-button</code>. It should be shorter — maybe <code>ty-button</code>.</em></p>

<p><strong><em>Yes! Let's call it ty."</em></strong></p>
</blockquote>

## 🛠️ Development

### Prerequisites
- **ClojureScript**: Java 8+, Node.js 16+
- **React/npm**: Node.js 16+

### Get Started
```bash
# Clone and develop
git clone https://github.com/gersak/ty.git
cd ty
npm install
npm run dev  # http://localhost:8000

# Or try examples
cd examples/htmx-flask && ./setup.sh
cd examples/react-nextjs && npm install && npm run dev
```

## 🎪 Examples

- **[HTMX + Flask](./examples/htmx-flask/)** - Server-side rendering with dynamic interactions
- **[React + Next.js](./examples/react-nextjs/)** - TypeScript React integration
- **[ClojureScript Reagent](./examples/reagent/)** - Native ClojureScript usage

## 🔗 Links

- **GitHub**: [github.com/gersak/ty](https://github.com/gersak/ty)
- **Clojars**: [clojars.org/dev.gersak/ty](https://clojars.org/dev.gersak/ty)
- **npm (React)**: [@gersak/ty-react](https://www.npmjs.com/package/@gersak/ty-react)
- **CDN**: [jsdelivr.net/npm/@gersak/ty](https://www.jsdelivr.net/npm/@gersak/ty)
- **Live Demo**: [gersak.github.io/ty](https://gersak.github.io/ty)

## 🤝 Contributing

Help shape the future of ClojureScript UI development:
- Component development and mobile adaptations
- Testing and feedback on ClojureScript integration
- Documentation and examples

**Built by Clojure developers, for Clojure developers. Framework-agnostic by design, ClojureScript-optimized by choice.**

---

<div align="center">

**MIT Licensed** • **Made with ❤️ using ClojureScript and Web Standards**

</div>
