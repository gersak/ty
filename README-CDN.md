# @gersak/ty

Modern Web Components Library built with ClojureScript - Zero Dependencies, Semantic Design System, Framework Agnostic.

[![npm version](https://img.shields.io/npm/v/@gersak/ty.svg)](https://www.npmjs.com/package/@gersak/ty)
[![Bundle Size](https://img.shields.io/bundlephobia/minzip/@gersak/ty)](https://bundlephobia.com/package/@gersak/ty)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 🚀 Quick Start

### CDN (Easiest - 2 minutes)

Add to your HTML `<head>`:

```html
<!-- Ty Components CSS -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css">

<!-- Option 1: All Components Bundle (Recommended for getting started) -->
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/ty.bundle.js"></script>

<!-- Option 2: Modular (Load only what you need) -->
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/ty.js"></script>
```

Then use components anywhere in your HTML:

```html
<ty-button variant="primary">Click me</ty-button>
<ty-input label="Email" type="email" required></ty-input>
<ty-dropdown label="Choose one">
  <ty-option value="1">Option 1</ty-option>
  <ty-option value="2">Option 2</ty-option>
</ty-dropdown>
```

### NPM Installation

```bash
npm install @gersak/ty
```

#### Import in your JavaScript/TypeScript:

```javascript
// Option 1: Import all components (bundle)
import '@gersak/ty/ty.bundle.js'
import '@gersak/ty/css/ty.css'

// Option 2: Import modular (core + specific components)
import '@gersak/ty/ty.js'  // Core components
import '@gersak/ty/ty-calendar.js'  // Calendar component
import '@gersak/ty/ty-dropdown.js'  // Dropdown component
import '@gersak/ty/css/ty.css'
```

## 📦 Bundle Options

### 1. All-in-One Bundle (`ty.bundle.js`)
- **Size**: ~200KB (gzipped: ~80KB)
- **Best for**: Getting started, prototypes, smaller apps
- **Includes**: All components ready to use
- **Load with**: `<script src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/ty.bundle.js"></script>`

### 2. Modular Build (`ty.js` + components)
- **Core size**: ~120KB (gzipped: ~45KB)
- **Best for**: Production apps, optimized loading
- **Load only what you need**:

```html
<!-- Core (required) -->
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/ty.js"></script>

<!-- Then add only the components you need -->
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/ty-calendar.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/ty-dropdown.js"></script>
```

#### Available Modular Components:
- `ty.js` - Core components (button, input, textarea, modal, popup, tooltip, tag, icon)
- `ty-calendar.js` - Full calendar with navigation
- `ty-calendar-month.js` - Single month calendar view
- `ty-date-picker.js` - Date picker with calendar popup
- `ty-dropdown.js` - Dropdown/select component
- `ty-multiselect.js` - Multi-select with tags

## 🎨 Included Components

### Core Components (in `ty.js`)
- **ty-button** - Semantic button with variants
- **ty-input** - Enhanced input field with validation
- **ty-textarea** - Auto-resizing textarea
- **ty-modal** - Accessible modal dialogs
- **ty-popup** - Positioned popup/popover
- **ty-tooltip** - Smart tooltips
- **ty-tag** - Tag/chip component
- **ty-icon** - 200+ Lucide icons built-in

### Advanced Components (modular)
- **ty-calendar** - Full-featured calendar
- **ty-calendar-month** - Month view calendar
- **ty-date-picker** - Date selection with popup
- **ty-dropdown** - Custom dropdown/select
- **ty-multiselect** - Multi-select with search
- **ty-option** - Option items for dropdowns

## 💅 Semantic Design System

Ty uses a semantic color system that automatically adapts to light/dark themes:

```html
<!-- Semantic colors, not hard-coded values -->
<div class="ty-bg-primary ty-text++">Primary background</div>
<div class="ty-bg-success ty-text-success++">Success state</div>
<div class="ty-border-danger ty-text-danger">Error state</div>

<!-- Surface hierarchy -->
<div class="ty-elevated">Card with shadow</div>
<div class="ty-content">Main content area</div>
<div class="ty-floating">Modal/dropdown surface</div>
```

### Available Semantic Colors:
- `primary`, `secondary`, `success`, `danger`, `warning`, `info`, `neutral`

### Text Emphasis Variants:
- `ty-text++` - Maximum emphasis
- `ty-text+` - High emphasis  
- `ty-text` - Normal emphasis
- `ty-text-` - Reduced emphasis
- `ty-text--` - Minimal emphasis

## 🔧 Framework Integration

### React
```jsx
import '@gersak/ty/ty.bundle.js'
import '@gersak/ty/css/ty.css'

function App() {
  return (
    <div>
      <ty-button variant="primary" onClick={() => alert('Clicked!')}>
        Click me
      </ty-button>
      <ty-calendar 
        onChange={(e) => console.log('Date:', e.detail.date)}
      />
    </div>
  )
}
```

### Vue
```vue
<template>
  <div>
    <ty-button variant="primary" @click="handleClick">
      Click me
    </ty-button>
    <ty-input v-model="email" label="Email" type="email" />
  </div>
</template>

<script>
import '@gersak/ty/ty.bundle.js'
import '@gersak/ty/css/ty.css'

export default {
  data() {
    return { email: '' }
  },
  methods: {
    handleClick() { alert('Clicked!') }
  }
}
</script>
```

### HTMX
```html
<ty-input 
  hx-post="/api/search" 
  hx-trigger="input changed delay:300ms"
  hx-target="#results"
  label="Search"
/>
```

## 🌐 Browser Support

- ✅ Chrome/Edge (88+)
- ✅ Firefox (78+)
- ✅ Safari (14+)
- ✅ All modern mobile browsers

Web Components are natively supported in all modern browsers. No polyfills needed!

## 🚧 Work in Progress

Ty is actively being developed. Components work and are used in production, but expect rough edges. Your feedback and contributions help shape the project!

- 🐛 [Report Issues](https://github.com/gersak/ty/issues)
- 💬 [Discussions](https://github.com/gersak/ty/discussions)
- 🌟 [Star on GitHub](https://github.com/gersak/ty)

## 📚 Documentation & Examples

- **Documentation**: [https://ty.gersak.io](https://ty.gersak.io)
- **Live Examples**: [https://ty.gersak.io/#/welcome](https://ty.gersak.io/#/welcome)
- **GitHub**: [https://github.com/gersak/ty](https://github.com/gersak/ty)

## 🤝 React Wrapper Package

For better React integration with TypeScript support:

```bash
npm install @gersak/ty-react
```

See [@gersak/ty-react](https://www.npmjs.com/package/@gersak/ty-react) for React-specific documentation.

## 📄 License

MIT © [Gersak](https://github.com/gersak)

---

**Built for developers who believe in web standards. Work in progress. Getting better every day.**