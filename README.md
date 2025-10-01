<blockquote style="color: #414863">
<p><em>"After playing around with Replicant, I realized I could build Web Components without React — actually, even without Replicant.</em></p>

<p><em>I'm wondering if it's called 'Replicant' because of <strong>Blade Runner</strong> (I love Blade Runner).</em></p>

<p><em>Maybe I could name my own library something similar... Tyrell? No, that feels pretentious.</em></p>

<p><em>Anyway, I don't really want to type something long like <code>tyrell-button</code>. It should be shorter — maybe <code>ty-button</code>.</em></p>

<p><strong><em>Yes! Let's call it ty."</em></strong></p>
</blockquote>

# ty - web components in ClojureScript

[![jsDelivr](https://data.jsdelivr.com/v1/package/npm/@gersak/ty/badge)](https://www.jsdelivr.com/package/npm/@gersak/ty)
[![NPM Version](https://img.shields.io/npm/v/@gersak/ty.svg)](https://www.npmjs.com/package/@gersak/ty)
[![Bundle Size](https://img.shields.io/bundlephobia/minzip/@gersak/ty)](https://bundlephobia.com/package/@gersak/ty)
[![Clojars Project](https://img.shields.io/clojars/v/dev.gersak/ty-icons.svg)](https://clojars.org/dev.gersak/ty-icons)
[![Clojars Project](https://img.shields.io/clojars/v/dev.gersak/ty.svg)](https://clojars.org/dev.gersak/ty)

Framework-agnostic web components built with ClojureScript. Standards-based. Designed to last. Works with React, Vue, HTMX—or no framework at all.

## ⚠️ Work in Progress - But Ready to Use

**ty is actively being developed.** Components work, examples run, but expect rough edges. This is a real project with a real vision - web components that work everywhere, built on standards that won't break next year.

**Working well:**
- ✅ **Button** - Semantic buttons with flavors and sizes
- ✅ **Input** - Enhanced inputs with validation and formatting
- ✅ **Textarea** - Auto-resizing textarea
- ✅ **Calendar** - Full calendar system with navigation, custom rendering, and form integration
- ✅ **Date Picker** - Date selection with calendar popup
- ✅ **Dropdown** - Rich dropdown with HTML content support
- ✅ **Multiselect** - Multi-select with tags and search
- ✅ **Modal** - Accessible modals with focus trapping
- ✅ **Popup** - Positioned popovers
- ✅ **Tooltip** - Smart tooltips with positioning
- ✅ **Tag** - Chip/tag component with removable option
- ✅ **Icon** - 8000+ icons built-in (Material, Font Awesome, Lucide, Hero)
- ✅ **Semantic design system** - Automatic theming with CSS variables

**Coming soon:**
- 🚧 Tabs component
- 🚧 Better mobile adaptations
- 🚧 More layout components
- 🚧 Enhanced accessibility features


**[See it in action →](https://gersak.github.io/ty)**

## Framework Integration

**React**: `@gersak/ty-react` typeScript wrappers handle the custom events and refs. Works with React 16-19.

**HTMX**: Just works. Server-side rendering with dynamic interactions.

**Vue/Angular**: Import the components, use them. Web Components are web standards.

**ClojureScript**: Built-in router, i18n, and responsive layout system if you want them.

## Quick Start
```html
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css">
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/ty.js"></script>

<ty-button flavor="primary">Hello World</ty-button>
<ty-calendar value="2024-12-25"></ty-calendar>
```

**[See full documentation →](https://gersak.github.io/ty/docs/getting-started)**

---


## ⚠️ Important: CSS is Required

**Ty components require the `ty.css` stylesheet to display correctly.** The CSS file contains:

- **CSS Variables** - Design tokens for all colors, surfaces, spacing, and typography
- **Utility Classes** - Semantic classes like `.ty-bg-primary`, `.ty-text++`, `.ty-elevated`
- **Theme System** - Light/dark mode definitions that swap automatically
- **Component Styles** - Base styling that components depend on

## The Bundle Size

- **Lazy**: ~89KB (50KB if you're using ClojureScript)
- **Everything**: ~134KB total (GZIP)

Comparable to other component libraries, with superior tree-shaking via Google Closure Compiler.


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
