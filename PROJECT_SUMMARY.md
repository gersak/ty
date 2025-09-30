# Ty Web Components - ClojureScript-First UI Components (Work in Progress)

## 🎯 The Core Value Proposition

**Ty is a work-in-progress ClojureScript component library** built on web standards, designed primarily for the Clojure/Script community with unique advantages:

- **ClojureScript-First**: Optimized for Shadow-cljs with lazy loading and code splitting
- **Minimal Footprint**: Zero JS dependencies, minimal ClojureScript deps - plays perfectly with Google Closure compiler
- **Adaptive UX Vision**: Components that intelligently adapt behavior for desktop vs mobile environments
- **Framework Agnostic**: Works with React, Vue, HTMX, vanilla JS, but ClojureScript gets the best experience
- **Semantic Design System**: Colors that mean something - `ty-text-danger` not `text-red-600`

## 🚧 Current Status & Roadmap

**Phase: Experimental → Stable Foundation**

### ✅ **What Works Today**
- **Core Components**: Button, Input, Tag, Icon, Textarea, Tooltip
- **Form System**: Dropdown, Multiselect with rich HTML content
- **Calendar System**: Full calendar orchestration with locale support
- **Layout**: Modal, Popup with focus management
- **Design System**: 5-variant semantic CSS with automatic dark mode
- **Integration**: HTMX-Flask example, React wrappers, ClojureScript routing

### 🔨 **Current Development**
- **Desktop/Mobile Adaptive UX**: Dropdowns currently experimental, expanding to all components
- **Performance Optimization**: Code splitting refinements

### 🎯 **Near-term Milestones (2025)**
1. **Tabs Component** - Missing piece for common UI patterns
2. **Mobile Adaptations** - Each component optimized for touch vs desktop interaction
3. **Resize Observers & Translation** - Infrastructure components for advanced layouts

### 🔮 **Vision**
Components that automatically provide the best UX for each environment:
- **Desktop**: Hover states, keyboard navigation, precise interactions
- **Mobile**: Touch-friendly sizing, swipe gestures, haptic feedback
- **Context-aware**: Same component, different behavior based on container/device

## 💡 Why ClojureScript First?

### **Bundle Size Advantage**
When you're already using ClojureScript in your project:
- **Ty adds ~50KB** (not the full 80KB) due to shared runtime
- **Full bundle**: ~240KB includes your app logic + Ty + ClojureScript runtime
- **Incremental cost**: Much lower than any other UI solution

### **Zero JS Dependencies + Google Closure**
- No npm dependency hell
- Works seamlessly with Google Closure compiler advanced optimizations
- Tree shaking that actually eliminates unused code
- No version conflicts with your existing ClojureScript stack

### **Shadow-cljs Optimized**
- Lazy loading system that loads components on demand
- Code splitting that respects component dependencies
- Development experience optimized for REPL-driven development
- Build system that just works without webpack configuration

### **ClojureScript Exclusive Features**
```clojure
;; Built-in router with authorization
(router/navigate! ::profile {:user-id 123})

;; Protocol-based i18n
(i18n/format-currency 1234.56 :locale "en-US") ;; → "$1,234.56"

;; Context-aware responsive design
(layout/container-width) ;; → Dynamic based on parent container
```

## 🌍 Distribution Strategy

### **Primary: Clojars** (ClojureScript developers)
```clojure
;; deps.edn
{:deps {dev.gersak/ty {:mvn/version "0.1.0"}}}
```
**Best experience**: Optimal bundle sizes, lazy loading, advanced compilation

### **Secondary: npm** (React/framework integration)
```bash
npm install @gersak/ty-react
```
**React wrappers**: TypeScript definitions, event handling, ref forwarding

### **Tertiary: jsdelivr** (CDN/prototyping)
```html
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.js"></script>
```
**Quick start**: No build required, works in any HTML page

## 🚀 Real-World Examples

### **1. ClojureScript + Reagent** - Optimal Experience
```clojure
[:ty-calendar {:value @selected-date
               :on-date-select #(reset! selected-date %)}]
```
**Result**: ~50KB total increment, lazy loading, REPL development

### **2. React + TypeScript** - Wrapped Components
```typescript
import { TyCalendar } from '@gersak/ty-react'

<TyCalendar onChange={(e) => setDate(e.detail.date)} />
```
**Result**: TypeScript safety, React integration, standard bundle size

### **3. Flask + HTMX** - Server-Side Renaissance
```html
<ty-input hx-get="/api/search" 
          hx-trigger="input changed delay:300ms">
```
**Result**: Rich client UX with server-side simplicity

### **4. Production Scenarios**

**Adaptive Dropdown Behavior**:
- **Desktop**: Hover to preview, click to select, keyboard navigation
- **Mobile**: Touch-friendly sizing, scroll momentum, haptic feedback

**Calendar Interactions**:
- **Desktop**: Month navigation with keyboard, date hover previews
- **Mobile**: Swipe gestures, larger touch targets, momentum scrolling

## 📊 Technical Details

### **Bundle Analysis**
- **Core system**: ~80KB gzipped (includes runtime for non-ClojureScript)
- **All components**: ~240KB total (complete system)
- **ClojureScript projects**: ~50KB increment (shared runtime)
- **Individual components**: 5-30KB each with dependencies

### **Performance Characteristics**
- **First Paint**: <1s with proper setup
- **Component Registration**: Lazy, on-demand
- **Runtime Overhead**: Minimal - native Web Components
- **Memory Usage**: Efficient - no virtual DOM

### **Browser Support**
- **Modern browsers**: Native Web Components support
- **Legacy support**: Polyfills available
- **Mobile browsers**: iOS Safari 14+, Chrome Android

## 🎨 Design System Deep Dive

### **5-Variant Semantic System**
```css
/* Text emphasis levels */
--ty-color-primary-strong   /* Headers, critical actions */
--ty-color-primary-mild     /* Subheadings, emphasis */
--ty-color-primary          /* Standard text */
--ty-color-primary-soft     /* Secondary content */
--ty-color-primary-faint    /* Disabled, hints */
```

### **Adaptive Color Logic**
- **Light mode**: Strong = darker, faint = lighter
- **Dark mode**: Emphasis logic flips automatically
- **Context aware**: Colors adjust based on background surface

### **CSS Variables Approach**
- **130+ CSS variables**: Complete customization without rebuild
- **Semantic tokens**: Colors convey meaning, not just appearance
- **Surface system**: Canvas → Content → Elevated → Floating → Input

## 🛠️ Developer Experience

### **ClojureScript Development**
```clojure
;; Hot reload with component state preservation
(defn component []
  [:div
   [:ty-calendar {:value @state}]])

;; REPL integration
(ty.core/load-component! :calendar)  ; Load on demand
```

### **Framework Integration**
```html
<!-- Works everywhere -->
<ty-button variant="primary">Universal</ty-button>

<!-- React wrapper (TypeScript) -->
<TyButton variant="primary" onClick={handler}>Type Safe</TyButton>

<!-- Vanilla JS -->
document.querySelector('ty-button').addEventListener('click', handler)
```

### **Testing Strategy**
```javascript
// Standard DOM testing
const button = document.querySelector('ty-button')
button.click()
expect(button.getAttribute('aria-pressed')).toBe('true')
```

## 🚦 When to Choose Ty

### **Perfect Fit When:**
- ✅ **ClojureScript project** (optimal experience, minimal cost)
- ✅ **Multiple frameworks** in your organization
- ✅ **Long-term stability** concerns about framework churn
- ✅ **Semantic design system** requirements
- ✅ **Mobile + desktop** adaptive UX needs
- ✅ **Bundle size optimization** with Google Closure

### **Consider Alternatives When:**
- ❌ **React-only shop** with no ClojureScript (may prefer React-specific solutions)
- ❌ **Highly specific design system** (Material Design, etc.)
- ❌ **CSS-in-JS preference** over CSS variables
- ❌ **Immediate production needs** (remember: work in progress!)

## 🔮 The ClojureScript Advantage

### **Why This Matters for Clojure Developers**

**Current State**: Clojure web developers often choose between:
- **Reagent/Re-frame**: Great for SPAs, but limited component ecosystem
- **React wrappers**: Bundle size overhead, JavaScript dependency management
- **Vanilla solutions**: Lots of DIY work

**Ty's Position**: Native ClojureScript components that:
- Work seamlessly with Google Closure compiler
- Provide rich UX without JavaScript ecosystem baggage
- Offer professional components (calendar, forms) that "just work"
- Scale from simple widgets to complete design systems

### **Future Vision**
As ClojureScript adoption grows and Web Components mature, Ty provides:
- **Stable foundation**: Won't break with ClojureScript updates
- **Framework insurance**: Components work regardless of future framework trends
- **Community resource**: Shared components across ClojureScript projects

## 🎯 Getting Started

### **ClojureScript Projects** (Recommended)
```clojure
;; deps.edn
{:deps {dev.gersak/ty {:mvn/version "LATEST"}}}

;; Load and use
(require '[ty.components :as ty])
[:ty-button {:variant "primary"} "Hello Ty!"]
```

### **React Projects**
```bash
npm install @gersak/ty-react
```

### **Quick Prototyping**
```html
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.js"></script>
```

## 📈 Why Work in Progress?

**Honest Communication**: Rather than overpromise, Ty sets realistic expectations:
- **Core is solid**: Basic components work well today
- **Vision is clear**: Desktop/mobile adaptive UX is the goal
- **Progress is steady**: Regular improvements, clear milestones
- **Community-driven**: Built for ClojureScript developers, by ClojureScript developers

**The Bet**: Web Components + ClojureScript + semantic design will outlast the current framework churn. Ty is building for the long term.

---

## Quick Links

- **GitHub**: [github.com/gersak/ty](https://github.com/gersak/ty)
- **Clojars**: [clojars.org/dev.gersak/ty](https://clojars.org/dev.gersak/ty)
- **npm (React)**: [@gersak/ty-react](https://www.npmjs.com/package/@gersak/ty-react)
- **CDN**: [jsdelivr.net/npm/@gersak/ty](https://www.jsdelivr.net/npm/@gersak/ty)
- **Examples**: See `/examples` folder for integrations
- **License**: MIT

## Contributing to the ClojureScript Ecosystem

Ty is open source and community-driven. Whether you're contributing components, testing mobile adaptations, or providing feedback on ClojureScript integration - help shape the future of ClojureScript UI development.

**Built by Clojure developers, for Clojure developers. Framework-agnostic by design, ClojureScript-optimized by choice.**