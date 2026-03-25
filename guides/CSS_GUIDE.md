# Ty CSS System

Ty classes handle colors. Use Tailwind for everything else (spacing, layout, typography, sizing).

## Surfaces vs Backgrounds

**Surfaces** are for layout areas — cards, panels, sidebars, modals, page background, form fields:

```html
<div class="ty-canvas">         <!-- App background -->
<div class="ty-content">        <!-- Main content area -->
<div class="ty-elevated">       <!-- Cards, panels (with shadow) -->
<div class="ty-floating">       <!-- Modals, dropdowns (with shadow) -->
<div class="ty-input">          <!-- Form controls background -->
```

**Backgrounds** are for small UI elements — buttons, tags, badges, toasts, alerts, status indicators:

```html
<div class="ty-bg-primary">     <!-- Button, badge -->
<div class="ty-bg-success-">    <!-- Success toast -->
<div class="ty-bg-danger">      <!-- Error indicator -->
```

Do not use `ty-bg-*` for cards or panels. Do not use `ty-elevated` for a badge.

## Background Colors (Semantic)

3 variants per color: stronger (+), base, softer (-).

```html
<div class="ty-bg-primary+">    <!-- Stronger -->
<div class="ty-bg-primary">     <!-- Base -->
<div class="ty-bg-primary-">    <!-- Softer -->
```

Available colors: `primary`, `secondary`, `success`, `danger`, `warning`, `neutral`, `accent`.

## Text Colors (5-Variant System)

Base text hierarchy (no color, just emphasis):

```html
<p class="ty-text++">           <!-- Maximum emphasis -->
<p class="ty-text+">            <!-- High emphasis -->
<p class="ty-text">             <!-- Normal text -->
<p class="ty-text-">            <!-- Reduced emphasis -->
<p class="ty-text--">           <!-- Minimal emphasis -->
```

Semantic text colors — same 5 variants per color:

```html
<p class="ty-text-primary++">   <!-- Maximum emphasis primary -->
<p class="ty-text-primary+">    <!-- High emphasis primary -->
<p class="ty-text-primary">     <!-- Normal primary -->
<p class="ty-text-primary-">    <!-- Reduced primary -->
<p class="ty-text-primary--">   <!-- Minimal primary -->
```

Available colors: `primary`, `secondary`, `success`, `danger`, `warning`, `neutral`, `accent`.

## Border Colors

Base borders (no color, just emphasis) — 5 variants:

```html
<div class="ty-border++">       <!-- Maximum -->
<div class="ty-border+">        <!-- Strong -->
<div class="ty-border">         <!-- Normal -->
<div class="ty-border-">        <!-- Soft -->
<div class="ty-border--">       <!-- Minimal -->
```

Semantic borders — base only (no +/- variants, except accent):

```html
<div class="ty-border-primary">
<div class="ty-border-secondary">
<div class="ty-border-success">
<div class="ty-border-danger">
<div class="ty-border-warning">
<div class="ty-border-neutral">

<!-- accent is the only semantic border with variants -->
<div class="ty-border-accent+">
<div class="ty-border-accent">
<div class="ty-border-accent-">
```

## Hover / Focus States

```html
<div class="hover:ty-bg-primary">
<input class="focus:ty-border-primary">
```

## Dark Mode

Ty uses the `dark` class on `<html>` to switch themes. All Ty color variables respond automatically.

### Requirements

Add a `dark` class to `<html>` for dark mode:

```html
<!-- Light mode (default) -->
<html>

<!-- Dark mode -->
<html class="dark">
```

### Toggling Theme (JavaScript)

```javascript
function toggleTheme() {
  const html = document.documentElement;
  const isDark = html.classList.toggle('dark');
  localStorage.setItem('theme', isDark ? 'dark' : 'light');
}
```

### Toggling Theme (ClojureScript)

```clojure
(defn toggle-theme! []
  (let [cl (.-classList js/document.documentElement)
        dark? (.toggle cl "dark")]
    (.setItem js/localStorage "theme" (if dark? "dark" "light"))))
```

### Initializing from Stored Preference

```javascript
// Run before page renders (e.g. in <head>) to prevent flash
const theme = localStorage.getItem('theme')
  || (window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
if (theme === 'dark') document.documentElement.classList.add('dark');
```

## Color Customization

Override via CSS custom properties:

```css
:root {
  --ty-color-primary-strong: #0034c7;
  --ty-color-primary-mild: #1c40a8;
  --ty-color-primary: #4367cd;
  --ty-color-primary-soft: #60a5fa;
  --ty-color-primary-faint: #93c5fd;
  --ty-bg-primary-mild: #bfdbfe;
  --ty-bg-primary: #dbeafe;
  --ty-bg-primary-soft: #eff6ff;
}

/* Dark mode overrides */
:root.dark {
  --ty-color-primary-strong: #93c5fd;
  --ty-color-primary-mild: #60a5fa;
  --ty-color-primary: #3b82f6;
  --ty-color-primary-soft: #1d4ed8;
  --ty-color-primary-faint: #1e3a5f;
  --ty-bg-primary-mild: #1e3a8a;
  --ty-bg-primary: #172554;
  --ty-bg-primary-soft: #0f172a;
}
```

Pattern: `--ty-color-{name}-{strong|mild|soft|faint}`, `--ty-bg-{name}-{mild|soft}`, `--ty-border-{name}`.
