# Ty - Modern Web Components Library

Ty is a modern web components library built with ClojureScript that provides reusable UI components as native Web Components. It emphasizes performance, simplicity, and maintainability while leveraging Web Standards and CSS custom properties for theming.

## Features

- **Web Standards First** - Built on native Web Components (Custom Elements, Shadow DOM)
- **Zero Runtime Dependencies** - No React, no VDOM, just vanilla Web Components  
- **Performance Focused** - Static CSS, shared stylesheets, minimal runtime overhead
- **Semantic Design System** - Intent-based colors with 5-variant emphasis system
- **CSS Variables for Theming** - Full customization through CSS custom properties
- **Complete i18n System** - Standalone internationalization with native Intl API
- **Dynamic Layout Context** - Container-aware responsive components
- **Professional Form Components** - Elegant input styling with validation support

## Installation

Add to your `deps.edn`:

```clojure
{:deps {dev.gersak/ty {:mvn/version "0.1.0"}}}
```

## Usage

### Basic Setup

Include the CSS in your HTML:

```html
<!-- Include Ty CSS -->
<link rel="stylesheet" href="path/to/ty.css">

<!-- Or load from resources if using the JAR -->
<link rel="stylesheet" href="/ty.css">
```

In your ClojureScript code:

```clojure
(ns my-app.core
  (:require [ty.core :as ty]))

;; Initialize the components
(ty/init)
```

### Using Components

Once initialized, you can use Ty components in your HTML or generate them with ClojureScript:

```html
<!-- HTML usage -->
<ty-button variant="primary">Click Me</ty-button>
<ty-input placeholder="Enter text" type="email"></ty-input>
<ty-modal id="my-modal">
  <h2>Modal Title</h2>
  <p>Modal content...</p>
</ty-modal>
```

```clojure
;; ClojureScript usage (with Replicant)
[:div
 [:ty-button {:variant "primary"} "Click Me"]
 [:ty-input {:placeholder "Enter text" :type "email"}]
 [:ty-modal {:id "my-modal"}
  [:h2 "Modal Title"] 
  [:p "Modal content..."]]]
```

### Event Handling

Ty components dispatch standard DOM events:

```javascript
// Listen for input events
document.addEventListener('input', (event) => {
  if (event.target.tagName === 'TY-INPUT') {
    console.log('Input value:', event.detail.value);
  }
});

// Listen for button clicks  
document.addEventListener('click', (event) => {
  if (event.target.tagName === 'TY-BUTTON') {
    console.log('Button clicked');
  }
});
```

### CSS Theming

Ty uses semantic CSS variables for theming:

```css
/* Customize the color palette */
:root {
  --ty-color-primary: #your-primary-color;
  --ty-color-success: #your-success-color;
  --ty-color-danger: #your-danger-color;
}

/* Dark theme support */
html.dark {
  --ty-color-primary: #dark-theme-primary;
  /* Other dark theme colors... */
}
```

Use Ty's utility classes:

```html
<div class="ty-elevated ty-text-primary">
  <h1 class="ty-text++">Strong Emphasis Title</h1>
  <p class="ty-text-">Reduced emphasis text</p>
</div>
```

## Available Components

- **ty-button** - Buttons with variants and states
- **ty-input** - Text inputs with validation and formatting
- **ty-modal** - Modal dialogs with backdrop and animations
- **ty-dropdown** - Dropdown selection with keyboard navigation
- **ty-multiselect** - Multi-selection with tags
- **ty-calendar** - Date picker and calendar views
- **ty-tooltip** - Contextual tooltips
- **ty-icon** - Icon component with size and animation variants
- **ty-tag** - Tags and badges
- **And more...**

## CSS Classes

### Surface Classes
- `ty-canvas` - App background
- `ty-content` - Main content areas
- `ty-elevated` - Cards and panels with elevation
- `ty-floating` - Modals and dropdowns

### Text Colors (5-variant system)
- `ty-text++` - Maximum emphasis
- `ty-text+` - High emphasis  
- `ty-text` - Base emphasis
- `ty-text-` - Reduced emphasis
- `ty-text--` - Minimal emphasis

### Background Colors
- `ty-bg-primary` - Primary backgrounds
- `ty-bg-success` - Success state backgrounds
- `ty-bg-danger` - Error state backgrounds
- `ty-bg-warning` - Warning backgrounds

## Dependencies

- ClojureScript 1.11+
- Replicant (virtual DOM library)
- Timing.core (date/time utilities)
- cljs-bean (JavaScript interop)

## Browser Support

Ty supports all modern browsers that implement Web Components:

- Chrome 67+
- Firefox 63+  
- Safari 13.1+
- Edge 79+

## License

MIT License - see LICENSE file for details.

## Development

This library is part of the Ty ecosystem. For development and contributing, see the main repository.

## Related Libraries

- `dev.gersak/ty-icons` - Pre-generated icon components for Ty
