# ty - Web Components for ClojureScript

[![jsDelivr](https://data.jsdelivr.com/v1/package/npm/@gersak/ty/badge)](https://www.jsdelivr.com/package/npm/@gersak/ty)
[![NPM Version](https://img.shields.io/npm/v/@gersak/ty.svg)](https://www.npmjs.com/package/@gersak/ty)
[![Bundle Size](https://img.shields.io/bundlephobia/minzip/@gersak/ty)](https://bundlephobia.com/package/@gersak/ty)
[![Clojars Project](https://img.shields.io/clojars/v/dev.gersak/ty-icons.svg)](https://clojars.org/dev.gersak/ty-icons)
[![Clojars Project](https://img.shields.io/clojars/v/dev.gersak/ty.svg)](https://clojars.org/dev.gersak/ty)

Framework churn got old. React, Vue, Angular, Svelte... every year something new, every migration a headache. So I built **ty** using Web Components instead. Turns out the browser's component system is pretty solid.

## Why "ty"?

<blockquote style="color: #414863">
<p><em>"After playing around with Replicant, I realized I could build Web Components without React — actually, even without Replicant.</em></p>

<p><em>I'm wondering if it's called 'Replicant' because of <strong>Blade Runner</strong> (I love Blade Runner).</em></p>

<p><em>Maybe I could name my own library something similar... Tyrell? No, that feels too ambitious.</em></p>

<p><em>Anyway, I don't really want to type something long like <code>tyrell-button</code>. It should be shorter — maybe <code>ty-button</code>.</em></p>

<p><strong><em>Yes! Let's call it ty."</em></strong></p>
</blockquote>

## ⚠️ Work in Progress - But Ready to Use

**ty is actively being developed.** Components work, examples run, but expect rough edges. This is a real project with a real vision - web components that work everywhere, built on standards that won't break next year.

If you're tired of framework churn and want to help build something different, you're in the right place.

### What's Working Today

✅ **Calendar** - Full calendar system with navigation, custom rendering, and form integration  
✅ **Dropdown** - Rich dropdown with HTML content support  
✅ **Modal** - Accessible modals with focus trapping  
✅ **Input** - Enhanced inputs with validation and formatting  
✅ **Button** - Semantic buttons with variants  
✅ **Multiselect** - Multi-select with tags and search  
✅ **Date Picker** - Date selection with calendar popup  
✅ **Tag** - Chip/tag component with removable option  
✅ **Tooltip** - Smart tooltips with positioning  
✅ **Textarea** - Auto-resizing textarea  
✅ **Popup** - Positioned popovers  
✅ **Icon** - 200+ Lucide icons built-in

**13 components total** - functional and used in production.

### Why Use Something That's Still in Progress?

- ✅ **It already works** - Components are functional and used in production
- ✅ **Built on web standards** - Not another abstraction layer that will break
- ✅ **Framework agnostic** - Use with React, Vue, HTMX, or vanilla HTML
- ✅ **No build step required** - CDN ready, just add script tag
- ✅ **Your input matters** - Early adopters shape the direction
- ✅ **ClojureScript powered** - Built with a language designed to last

## The Idea

Web Components work everywhere. React today, Vue tomorrow, vanilla JS next week - doesn't matter. Same components, same API. And if you're already using ClojureScript, they integrate beautifully with Google Closure compiler.

**The vision:** Components that automatically provide the best UX for each environment. Desktop gets hover states and keyboard navigation. Mobile gets touch-friendly interactions and gestures. No props, no configuration.

**See it in action:** [ty.gersak.io](https://gersak.github.io/ty)

## ⚠️ Important: CSS is Required

**Ty components require the `ty.css` stylesheet to display correctly.** The CSS file contains:

- **CSS Variables** - Design tokens for all colors, surfaces, spacing, and typography
- **Utility Classes** - Semantic classes like `.ty-bg-primary`, `.ty-text++`, `.ty-elevated`
- **Theme System** - Light/dark mode definitions that swap automatically
- **Component Styles** - Base styling that components depend on

### What Happens Without CSS?

❌ Components render but have **no styling**  
❌ Colors are **undefined** (browser defaults)  
❌ Layout may be **broken**  
❌ Theme switching **doesn't work**

### Always Include Both CSS and JavaScript

```html
<!-- 1. CSS first (required) -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css">

<!-- 2. Then JavaScript components -->
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/ty.js"></script>
```

**Why separate CSS?**
- **Global theming** - Change colors across all components by updating CSS variables
- **Performance** - CSS loads once and applies to all components instantly
- **Customization** - Override variables before components load for custom themes
- **Standards** - Follows web component best practices (styles separate from logic)

## Try It Out

### ClojureScript (best experience)

```clojure
;; deps.edn
{:deps {dev.gersak/ty {:mvn/version "0.1.0"}}}

;; Use it
[:ty-button {:flavor "primary"} "Hello ty!"]
[:ty-calendar {:value @date, :on-date-select #(reset! date %)}]
```

### React

```jsx
import { TyButton, TyCalendar } from '@gersak/ty-react'

<TyCalendar onChange={(e) => setDate(e.detail.date)} />
```

### Vanilla HTML (works everywhere)

```html
<ty-button flavor="primary">Click me</ty-button>
<ty-calendar value="2024-12-25"></ty-calendar>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css">
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/ty.js"></script>
```

## What's Cool About It

**For ClojureScript folks**: When you're already using ClojureScript, ty adds about 50KB instead of the full 80KB because you share the runtime. Plus Google Closure compiler optimizations actually work.

**For everyone else**: Components that work across frameworks. Build once, use everywhere.

**Adaptive behavior**: Dropdowns that know when you're on mobile. Calendars that handle keyboard navigation on desktop and gestures on mobile. No configuration required.

**Semantic colors**: Instead of `bg-blue-500`, you get `ty-bg-primary`. Colors that mean something and adapt to light/dark themes automatically.

## What's Available

```html
<!-- Basic stuff -->
<ty-button flavor="primary">Action</ty-button>
<ty-input placeholder="Type here..." />
<ty-tag removable>Skill</ty-tag>

<!-- Selection -->
<ty-dropdown>
  <ty-option value="cljs">ClojureScript</ty-option>
  <ty-option value="js">JavaScript</ty-option>
</ty-dropdown>

<!-- Calendar that doesn't suck -->
<ty-calendar value="2024-12-25"></ty-calendar>
<ty-date-picker name="birthday"></ty-date-picker>

<!-- Layout -->
<ty-modal>Your content here</ty-modal>
<ty-tooltip message="Helpful info">Hover target</ty-tooltip>
```

## The Bundle Situation

- **Core**: ~80KB (50KB if you're using ClojureScript)
- **Each component**: 5-15KB
- **Everything**: ~240KB total

Not tiny, but reasonable for what you get. And with ClojureScript + Closure compiler, only the parts you use get bundled.

## Framework Integration

**React**: TypeScript wrappers handle the custom events and refs. Works with React 16-19.

**HTMX**: Just works. Server-side rendering with dynamic interactions.

**Vue/Angular**: Import the components, use them. Web Components are web standards.

**ClojureScript**: Built-in router, i18n, and responsive layout system if you want them.

## When It Makes Sense

**Good fit:**
- ClojureScript projects
- Multi-framework organizations
- Teams tired of framework churn
- Projects wanting semantic design systems
- Long-term projects built on web standards

**Maybe not:**
- React-only shops (plenty of React-specific options exist)
- Need something battle-tested right now (give it a few months)
- Highly specific design system requirements

## 🤝 Join the Effort

This project grows with community input. Every issue, PR, and discussion helps shape the direction.

**Ways to contribute:**
- 🐛 [**Report Issues**](https://github.com/gersak/ty/issues) - Found a bug? Let us know
- 💬 [**Discussions**](https://github.com/gersak/ty/discussions) - Share ideas, ask questions, show what you've built
- 🌟 [**Star on GitHub**](https://github.com/gersak/ty) - Show support for the project
- 🔧 [**Pull Requests**](https://github.com/gersak/ty/pulls) - Documentation, components, examples - all contributions matter

**Especially interested in:**
- Mobile interaction improvements
- New components
- Real-world usage feedback
- Documentation improvements

## Current Development Status

**Working well:**
- ✅ All basic components (button, input, textarea, tag)
- ✅ Calendar system with navigation and custom rendering
- ✅ Form components with validation
- ✅ Modals and popups
- ✅ Semantic design system with automatic theming

**Coming soon:**
- 🚧 Tabs component
- 🚧 Better mobile adaptations
- 🚧 More layout components
- 🚧 Enhanced accessibility features

**Long-term vision:**
Components that automatically adapt to their environment without configuration. The right UX for desktop (hover states, keyboard navigation) and mobile (touch gestures, larger targets) - all handled automatically.

## Links

- 📚 **Docs & examples**: [gersak.github.io/ty](https://gersak.github.io/ty)
- 💻 **Source**: [github.com/gersak/ty](https://github.com/gersak/ty)
- 📦 **ClojureScript**: [clojars.org/dev.gersak/ty](https://clojars.org/dev.gersak/ty)
- ⚛️ **React**: [@gersak/ty-react](https://www.npmjs.com/package/@gersak/ty-react)
- 🌐 **CDN**: [jsdelivr.net/npm/@gersak/ty](https://www.jsdelivr.net/npm/@gersak/ty)

## Development

```bash
git clone https://github.com/gersak/ty.git
cd ty
npm install
npm run dev  # http://localhost:8000
```

---

Built with ClojureScript and Web Components. MIT licensed.

**Work in progress. Getting better every day.**
