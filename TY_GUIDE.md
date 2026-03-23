# AI Agent Guide for Ty Component Library

This document provides comprehensive guidance for AI agents working with the Ty component library.

## Overview

**Ty** is a dual-language component library:
- **TypeScript Core** (`@gersak/ty`) - Web components published to NPM
- **ClojureScript Infrastructure** - Routing, i18n, published to Clojars

---

## CRITICAL: Use Ty Components - Do Not Improvise HTML!

> **When a Ty component exists for a UI pattern, YOU MUST USE IT.**
> Do not create custom HTML implementations unless explicitly requested.

### Why This Matters

Ty components provide:
- Consistent styling that respects the design system
- Built-in accessibility (ARIA, keyboard navigation)
- Form integration (works with native form submission)
- Event handling with proper detail objects
- Dark mode support automatically
- Responsive behavior

Custom HTML lacks all of these features and creates inconsistency.

### Component Selection Guide

| Need | USE THIS | NOT THIS |
|------|----------|----------|
| Button | `<ty-button>` | `<button>`, `<div onclick>` |
| Text input | `<ty-input>` | `<input>` |
| Checkbox | `<ty-checkbox>` | `<input type="checkbox">` |
| Textarea | `<ty-textarea>` | `<textarea>` |
| Dropdown/Select | `<ty-dropdown>` | `<select>`, custom div menus |
| Multi-select | `<ty-multiselect>` | Multiple checkboxes, custom lists |
| Modal/Dialog | `<ty-modal>` | `<dialog>`, custom overlay divs |
| Tooltip | `<ty-tooltip>` | `title` attribute, custom divs |
| Popup/Popover | `<ty-popup>` | Custom positioned divs |
| Tabs | `<ty-tabs>` + `<ty-tab>` | Custom tab implementations |
| Wizard/Stepper | `<ty-wizard>` + `<ty-step>` | Custom step indicators |
| Date picker | `<ty-date-picker>` | `<input type="date">`, custom calendars |
| Calendar | `<ty-calendar>` | Custom calendar grids |
| Tags/Chips | `<ty-tag>` | `<span>` with classes |
| Icons | `<ty-icon>` | `<svg>`, `<img>`, icon fonts |
| Copy to clipboard | `<ty-copy>` | Custom copy buttons |

### Examples

**WRONG - Improvised HTML:**
```html
<!-- DO NOT DO THIS when Ty components exist -->
<button class="bg-blue-500 text-white px-4 py-2 rounded">
  Click Me
</button>

<div class="relative">
  <button onclick="toggleDropdown()">Select...</button>
  <div class="absolute hidden" id="dropdown-menu">
    <div onclick="select('opt1')">Option 1</div>
    <div onclick="select('opt2')">Option 2</div>
  </div>
</div>

<input type="checkbox" id="terms">
<label for="terms">I agree</label>
```

**CORRECT - Using Ty Components:**
```html
<!-- ALWAYS use Ty components -->
<ty-button flavor="primary">Click Me</ty-button>

<ty-dropdown placeholder="Select...">
  <option value="opt1">Option 1</option>
  <option value="opt2">Option 2</option>
</ty-dropdown>

<ty-checkbox name="terms">I agree</ty-checkbox>
```

### When Custom HTML IS Acceptable

Only use plain HTML elements when:
1. **Layout containers** - `<div>`, `<section>`, `<main>`, `<article>` for structure
2. **Text content** - `<h1>`-`<h6>`, `<p>`, `<span>` for text (with Ty classes)
3. **Lists** - `<ul>`, `<ol>`, `<li>` for list content
4. **Links** - `<a>` for navigation (though consider `<ty-button>` for actions)
5. **Images** - `<img>` for images
6. **No Ty equivalent exists** - AND user explicitly requests custom implementation

### ClojureScript/Hiccup

The same rules apply in ClojureScript:

```clojure
;; WRONG
[:button.bg-blue-500.text-white.px-4.py-2 "Click"]
[:input {:type "checkbox"}]

;; CORRECT
[:ty-button {:flavor "primary"} "Click"]
[:ty-checkbox {:name "agree"} "I agree"]
```

---

## Quick Start

### Installation

**NPM (for bundlers):**
```bash
npm install @gersak/ty
```

**CDN (recommended for quick start):**
```html
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/index.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css">
```

**React:**
```bash
npm install @gersak/ty-react
# Also load web components via CDN in index.html
```

**ClojureScript (Clojars):**
```clojure
[dev.gersak/ty "0.2.0"]
```

### Basic Usage

```html
<ty-button flavor="primary" size="md">Click Me</ty-button>
<ty-input placeholder="Enter text..." name="email"></ty-input>
<ty-dropdown placeholder="Select option">
  <option value="1">Option 1</option>
  <option value="2">Option 2</option>
</ty-dropdown>
```

---

## CSS Design System

### Golden Rule

> **Use Ty for colors, Tailwind for everything else.**

```html
<!-- GOOD: Ty for colors, Tailwind for layout -->
<div class="ty-elevated p-6 rounded-lg flex items-center">
  <h2 class="ty-text++ text-xl font-bold">Title</h2>
  <p class="ty-text- text-sm">Description</p>
</div>

<!-- BAD: Don't mix color systems -->
<div class="bg-blue-500 ty-elevated">...</div>
```

### Surface System (5 Levels)

Surfaces handle background color + optional shadow:

| Class | Use Case | Description |
|-------|----------|-------------|
| `.ty-canvas` | App background | Lowest elevation |
| `.ty-content` | Main content areas | Base content |
| `.ty-elevated` | Cards, panels, sidebars | With shadow |
| `.ty-floating` | Modals, dropdowns, tooltips | Highest elevation |
| `.ty-input` | Form controls | Special for inputs |

```html
<div class="ty-canvas">           <!-- App background -->
  <div class="ty-content">        <!-- Main content area -->
    <div class="ty-elevated">     <!-- Card with shadow -->
      <input class="ty-input">    <!-- Form input -->
    </div>
  </div>
</div>
```

### Text Hierarchy (5 Variants)

| Class | Emphasis | Use Case |
|-------|----------|----------|
| `.ty-text++` | Maximum | Headers, primary actions |
| `.ty-text+` | High | Subheaders |
| `.ty-text` | Normal | Body text |
| `.ty-text-` | Reduced | Captions, secondary |
| `.ty-text--` | Minimal | Hints, timestamps |

### Semantic Text Colors

Each semantic color has 5 variants:

```
.ty-text-{color}++    Strong emphasis
.ty-text-{color}+     Mild emphasis
.ty-text-{color}      Base
.ty-text-{color}-     Soft
.ty-text-{color}--    Faint
```

**Available colors:** `primary`, `secondary`, `success`, `danger`, `warning`, `neutral`

**Accent colors** (3 variants only): `.ty-text-accent+`, `.ty-text-accent`, `.ty-text-accent-`

### Background Colors

```
.ty-bg-{color}+       Stronger background
.ty-bg-{color}        Base background
.ty-bg-{color}-       Softer background
```

**Available colors:** `primary`, `secondary`, `success`, `danger`, `warning`, `neutral`, `accent`

### Border Colors

**Base borders (emphasis levels):**
```
.ty-border++    Maximum
.ty-border+     Strong
.ty-border      Normal
.ty-border-     Soft
.ty-border--    Minimal
```

**Semantic borders:**
```
.ty-border-primary
.ty-border-secondary
.ty-border-success
.ty-border-danger
.ty-border-warning
.ty-border-neutral
```

**Accent borders:**
```
.ty-border-accent+
.ty-border-accent
.ty-border-accent-
```

### Interactive States

All classes support hover and focus variants:
```
.hover:ty-bg-primary
.hover:ty-text-accent
.hover:ty-border++
.focus:ty-border-primary
```

### Dark Mode

All classes automatically adapt to dark mode via:
- `html.dark` class
- `html[data-theme="dark"]` attribute

### Color Customization

Ty uses CSS custom properties (variables) for all colors, making customization straightforward.

#### Color Token Structure

Each semantic color has 5 variants:

```css
/* Color tokens (used for text) */
--ty-color-{name}-strong   /* Maximum emphasis */
--ty-color-{name}-mild     /* High emphasis */
--ty-color-{name}          /* Base */
--ty-color-{name}-soft     /* Reduced emphasis */
--ty-color-{name}-faint    /* Minimal emphasis */

/* Background tokens */
--ty-bg-{name}-mild        /* Stronger background */
--ty-bg-{name}             /* Base background */
--ty-bg-{name}-soft        /* Softer background */

/* Border tokens */
--ty-border-{name}         /* Base border */
```

**Available color names:** `primary`, `secondary`, `success`, `danger`, `warning`, `neutral`, `accent`

#### Overriding Colors

**Method 1: Override in :root (global)**
```css
:root {
  /* Override primary color family */
  --ty-color-primary-strong: #0034c7;
  --ty-color-primary-mild: #1c40a8;
  --ty-color-primary: #4367cd;
  --ty-color-primary-soft: #60a5fa;
  --ty-color-primary-faint: #93c5fd;

  /* Override primary backgrounds */
  --ty-bg-primary: #dbeafe;
  --ty-bg-primary-mild: #bfdbfe;
  --ty-bg-primary-soft: #eff6ff;
}
```

**Method 2: Override for dark mode only**
```css
html.dark, html[data-theme="dark"] {
  --ty-color-primary-strong: #93c5fd;
  --ty-color-primary-mild: #60a5fa;
  --ty-color-primary: #3b82f6;
  --ty-color-primary-soft: #2563eb;
  --ty-color-primary-faint: #1d4ed8;

  --ty-bg-primary: #1e3a5f;
  --ty-bg-primary-mild: #1e40af;
  --ty-bg-primary-soft: #172554;
}
```

**Method 3: Scoped overrides**
```css
.my-custom-theme {
  --ty-color-accent-mild: #cc6d00;
  --ty-color-accent: #e67b00;
  --ty-color-accent-soft: #f59e42;

  --ty-bg-accent: #ffedd5;
  --ty-bg-accent-mild: #fed7aa;
  --ty-bg-accent-soft: #fff7ed;
}
```

#### Complete Variable Reference

**Text Colors:**
```css
--ty-text-strong    /* Maximum emphasis text */
--ty-text-mild      /* High emphasis text */
--ty-text           /* Base text */
--ty-text-soft      /* Reduced emphasis text */
--ty-text-faint     /* Minimal emphasis text */
```

**Surface Backgrounds:**
```css
--ty-bg-strong      /* Highest surface */
--ty-bg-mild        /* Elevated surface */
--ty-bg             /* Base surface */
--ty-bg-soft        /* Softer surface */
--ty-bg-faint       /* Faintest surface */
```

**Base Borders:**
```css
--ty-border-strong  /* Maximum border */
--ty-border-mild    /* Strong border */
--ty-border         /* Normal border */
--ty-border-soft    /* Soft border */
--ty-border-faint   /* Minimal border */
```

**Input-Specific:**
```css
--ty-input-border-focus    /* Focus ring color */
--ty-input-success-border  /* Success state border */
```

#### Creating a Custom Theme

```css
/* custom-theme.css */
:root {
  /* Brand colors */
  --ty-color-accent-mild: #7c3aed;
  --ty-color-accent: #8b5cf6;
  --ty-color-accent-soft: #a78bfa;

  --ty-bg-accent: #ede9fe;
  --ty-bg-accent-mild: #ddd6fe;
  --ty-bg-accent-soft: #f5f3ff;

  --ty-border-accent-mild: #7c3aed;
  --ty-border-accent: #8b5cf6;
  --ty-border-accent-soft: #a78bfa;
}

html.dark, html[data-theme="dark"] {
  --ty-color-accent-mild: #c4b5fd;
  --ty-color-accent: #a78bfa;
  --ty-color-accent-soft: #8b5cf6;

  --ty-bg-accent: #2e1065;
  --ty-bg-accent-mild: #4c1d95;
  --ty-bg-accent-soft: #1e1b4b;

  --ty-border-accent-mild: #c4b5fd;
  --ty-border-accent: #a78bfa;
  --ty-border-accent-soft: #8b5cf6;
}
```

#### JavaScript Theme Switching

```javascript
// Set dark mode
document.documentElement.classList.add('dark');
// or
document.documentElement.setAttribute('data-theme', 'dark');

// Set light mode
document.documentElement.classList.remove('dark');
// or
document.documentElement.setAttribute('data-theme', 'light');

// Toggle
document.documentElement.classList.toggle('dark');
```

---

## Components Reference

### Form Inputs

#### ty-button

Interactive button component with multiple variants.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `flavor` | string | `'neutral'` | `'primary'` \| `'secondary'` \| `'success'` \| `'danger'` \| `'warning'` \| `'neutral'` |
| `size` | string | `'md'` | `'xs'` \| `'sm'` \| `'md'` \| `'lg'` \| `'xl'` |
| `type` | string | `'submit'` | `'button'` \| `'submit'` \| `'reset'` |
| `disabled` | boolean | `false` | Disables the button |
| `pill` | boolean | `false` | Pill/rounded shape |
| `outlined` | boolean | `false` | Outlined variant |
| `filled` | boolean | `false` | Filled/solid variant |
| `accent` | boolean | `false` | Accent color variant |
| `plain` | boolean | `false` | Plain/minimal variant |
| `action` | boolean | `false` | Action button style |
| `wide` | boolean | `false` | Full-width button |
| `name` | string | - | Form field name |
| `value` | string | - | Form value |

**Slots:**
- `start` - Content before text
- (default) - Button text
- `end` - Content after text

**Events:**
- `click` - `detail: { originalEvent: Event }`

**Example:**
```html
<ty-button flavor="primary" size="lg">
  <ty-icon slot="start" name="save"></ty-icon>
  Save Changes
</ty-button>
```

---

#### ty-input

Text input with support for various formats including currency and percentage.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `type` | string | `'text'` | `'text'` \| `'email'` \| `'password'` \| `'number'` \| `'tel'` \| `'url'` \| `'currency'` \| `'percent'` \| `'compact'` |
| `value` | string | `''` | Current value |
| `name` | string | - | Form field name |
| `placeholder` | string | - | Placeholder text |
| `label` | string | - | Field label |
| `disabled` | boolean | `false` | Disables input |
| `required` | boolean | `false` | Required field |
| `error` | string | - | Error message |
| `size` | string | `'md'` | `'xs'` \| `'sm'` \| `'md'` \| `'lg'` \| `'xl'` |
| `flavor` | string | `'neutral'` | Color variant |
| `currency` | string | `'USD'` | Currency code (for currency type) |
| `locale` | string | `'en-US'` | Locale for formatting |
| `precision` | number | - | Decimal places |
| `delay` | number | `0` | Debounce delay (0-5000ms) |

**Slots:**
- `start` - Icon/content before input
- `end` - Icon/content after input

**Events:**
- `input` - `detail: { value, formattedValue, rawValue, originalEvent }`
- `change` - Same detail (fires on blur)
- `focus` - Focus event
- `blur` - Blur event

**Example:**
```html
<ty-input type="currency" currency="EUR" placeholder="Enter amount">
  <ty-icon slot="start" name="euro"></ty-icon>
</ty-input>
```

---

#### ty-checkbox

Checkbox input with label support.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `checked` | boolean | `false` | Checked state |
| `value` | string | `'on'` | Form value when checked |
| `name` | string | - | Form field name |
| `disabled` | boolean | `false` | Disables checkbox |
| `required` | boolean | `false` | Required field |
| `error` | string | - | Error message |
| `size` | string | `'md'` | Size variant |
| `flavor` | string | `'neutral'` | Color variant |

**Slots:**
- (default) - Checkbox label content

**Events:**
- `input` - `detail: { value, checked, formValue, originalEvent }`
- `change` - Same detail

**Example:**
```html
<ty-checkbox name="terms" required>
  I agree to the terms and conditions
</ty-checkbox>
```

---

#### ty-textarea

Multi-line text input with auto-resize.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `value` | string | `''` | Current value |
| `name` | string | - | Form field name |
| `placeholder` | string | - | Placeholder text |
| `label` | string | - | Field label |
| `disabled` | boolean | `false` | Disables textarea |
| `required` | boolean | `false` | Required field |
| `error` | string | - | Error message |
| `size` | string | `'md'` | Size variant |
| `rows` | string | `'3'` | Initial row count |
| `resize` | string | `'none'` | `'none'` \| `'both'` \| `'horizontal'` \| `'vertical'` |
| `min-height` | string | - | Minimum height |
| `max-height` | string | - | Maximum height |

**Events:**
- `input` - `detail: { value, originalEvent }`
- `change` - Same detail

---

#### ty-copy

Read-only field with copy-to-clipboard functionality.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `value` | string | - | Text to copy |
| `label` | string | - | Field label |
| `size` | string | `'md'` | Size variant |
| `flavor` | string | `'neutral'` | Color variant |
| `format` | string | `'text'` | `'text'` \| `'code'` |
| `disabled` | boolean | `false` | Disables copy |

**Example:**
```html
<ty-copy value="npm install @gersak/ty" format="code"></ty-copy>
```

---

### Selection Components

#### ty-dropdown

Single-select dropdown with search support.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `value` | string | `''` | Selected value |
| `name` | string | - | Form field name |
| `placeholder` | string | `'Select an option...'` | Placeholder text |
| `label` | string | - | Field label |
| `disabled` | boolean | `false` | Disables dropdown |
| `readonly` | boolean | `false` | Read-only mode |
| `required` | boolean | `false` | Required field |
| `searchable` | boolean | `true` | Enable search |
| `not-searchable` | boolean | - | Alias to disable search |
| `clearable` | boolean | `true` | Show clear button |
| `not-clearable` | boolean | - | Alias to disable clear |
| `size` | string | `'md'` | `'sm'` \| `'md'` \| `'lg'` |
| `flavor` | string | `'neutral'` | Color variant |
| `delay` | number | `0` | Search debounce (0-5000ms) |

**Child Elements:** `<option>`, `<ty-option>`, or `<ty-tag>`

**Events:**
- `change` - `detail: { value, text, option, originalEvent }`
- `search` - `detail: { query, originalEvent }` (only when not-searchable)

**Example:**
```html
<ty-dropdown placeholder="Select country" searchable>
  <option value="us">United States</option>
  <option value="ca">Canada</option>
  <option value="uk">United Kingdom</option>
</ty-dropdown>
```

---

#### ty-multiselect

Multi-select component with tag-based selection.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `value` | string | `''` | Comma-separated selected values |
| `name` | string | - | Form field name |
| `placeholder` | string | `'Select options...'` | Placeholder text |
| `label` | string | - | Field label |
| `disabled` | boolean | `false` | Disables component |
| `readonly` | boolean | `false` | Read-only mode |
| `required` | boolean | `false` | Required field |
| `searchable` | boolean | `true` | Enable search |
| `size` | string | `'md'` | Size variant |
| `flavor` | string | `'neutral'` | Color variant |
| `delay` | number | `0` | Search debounce |
| `selected-label` | string | `'Selected'` | Label for selected section |
| `available-label` | string | `'Available'` | Label for available section |
| `no-selection-message` | string | `'No items selected'` | Empty selection message |
| `no-options-message` | string | `'No options available'` | No options message |

**Child Elements:** `<ty-tag>` elements only

**Events:**
- `change` - `detail: { values: string[], action: 'add'|'remove'|'clear'|'set', item: string|null }`
- `search` - `detail: { query, element }`

**Example:**
```html
<ty-multiselect placeholder="Select skills" value="js,ts">
  <ty-tag value="js">JavaScript</ty-tag>
  <ty-tag value="ts">TypeScript</ty-tag>
  <ty-tag value="py">Python</ty-tag>
</ty-multiselect>
```

---

#### ty-option

Rich HTML option for use in dropdowns.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `value` | string | - | Option value |
| `selected` | boolean | `false` | Selected state |
| `disabled` | boolean | `false` | Disabled state |
| `highlighted` | boolean | `false` | Highlighted state |
| `hidden` | boolean | `false` | Hidden state |

**Slots:**
- (default) - Rich HTML content

---

#### ty-tag

Tag/chip component for labels and selections.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `flavor` | string | `'neutral'` | Color variant |
| `size` | string | `'md'` | Size variant |
| `value` | string | - | Tag value |
| `selected` | boolean | `false` | Selected state |
| `pill` | boolean | `true` | Pill shape |
| `clickable` | boolean | `false` | Enable click events |
| `dismissible` | boolean | `false` | Show dismiss button |
| `disabled` | boolean | `false` | Disabled state |

**Events:**
- `click` - When clickable
- `dismiss` - When dismiss button clicked

---

### Navigation Components

#### ty-tabs / ty-tab

Tab navigation with carousel animation.

**ty-tabs Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `width` | string | `'100%'` | Content width |
| `height` | string | (required) | Total container height |
| `active` | string | - | Active tab ID |
| `placement` | string | `'top'` | `'top'` \| `'bottom'` |

**ty-tab Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `id` | string | (required) | Tab identifier |
| `label` | string | (required) | Tab label text |

**Slots:**
- `label-{id}` - Custom rich label for tab
- `marker` - Custom active indicator

**Events:**
- `change` - `detail: { activeId, activeIndex, previousId, previousIndex }`

**Example:**
```html
<ty-tabs height="400px" active="overview">
  <ty-tab id="overview" label="Overview">
    <h2>Overview Content</h2>
  </ty-tab>
  <ty-tab id="details" label="Details">
    <h2>Details Content</h2>
  </ty-tab>
</ty-tabs>
```

---

#### ty-wizard / ty-step

Step-by-step wizard navigation.

**ty-wizard Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `width` | string | `'100%'` | Content width |
| `height` | string | (required) | Container height |
| `active` | string | - | Active step ID |
| `completed` | string | - | Comma-separated completed step IDs |
| `orientation` | string | `'horizontal'` | Wizard orientation |

**ty-step Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `id` | string | (required) | Step identifier |
| `label` | string | (required) | Step label |

**Slots:**
- `indicator-{id}` - Custom indicator for step

**Events:**
- `change` - `detail: { activeId, activeIndex, previousId, previousIndex, direction }`

---

### Date/Time Components

#### ty-calendar

Full calendar component.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `year` | number/string | - | Display year (YYYY) |
| `month` | number/string | - | Display month (1-12) |
| `day` | number/string | - | Selected day (1-31) |
| `name` | string | - | Form field name |
| `required` | boolean | - | Required field |

**Properties:**
- `dayContentFn` - Custom render function for day cells

**Events:**
- `change` - `detail: { year, month, day, action: 'select', source: 'day-click', dayContext }`
- `navigate` - `detail: { month, year, action: 'navigate', source: 'navigation' }`

**Form Value:** ISO date string (YYYY-MM-DD)

---

#### ty-date-picker

Date picker with optional time selection.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `value` | string | - | UTC ISO datetime string |
| `name` | string | - | Form field name |
| `label` | string | - | Field label |
| `placeholder` | string | - | Placeholder text |
| `with-time` | boolean | `false` | Include time picker |
| `disabled` | boolean | `false` | Disables picker |
| `required` | boolean | `false` | Required field |
| `locale` | string | `'en-US'` | Locale for formatting |
| `size` | string | `'md'` | Size variant |

**Events:**
- `change` - Date/time selection events

**Form Value:** UTC ISO datetime string (2024-09-21T08:30:00.000Z)

---

#### ty-calendar-month

Visual calendar grid component (used internally).

#### ty-calendar-navigation

Month/year navigation controls (used internally).

---

### Overlay Components

#### ty-modal

Modal dialog component.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `open` | boolean | `false` | Controls visibility |
| `backdrop` | boolean | `true` | Show backdrop |
| `close-on-outside-click` | boolean | `true` | Close on outside click |
| `close-on-escape` | boolean | `true` | Close on Escape key |
| `protected` | boolean | `false` | Require confirmation to close |

**Methods:**
- `show()` - Open modal
- `hide()` - Close modal

**Events:**
- `close` - `detail: { reason: 'programmatic'|'native', returnValue?: string }`

**Example:**
```html
<ty-modal id="confirm-modal">
  <h2>Confirm Action</h2>
  <p>Are you sure you want to proceed?</p>
  <ty-button onclick="document.getElementById('confirm-modal').hide()">Cancel</ty-button>
  <ty-button flavor="primary">Confirm</ty-button>
</ty-modal>
```

---

#### ty-tooltip

Hover tooltip component.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `placement` | string | `'top'` | Tooltip position |
| `offset` | number | `8` | Distance from anchor |
| `delay` | number | `200` | Show delay (ms) |
| `disabled` | boolean | `false` | Disables tooltip |
| `flavor` | string | `'dark'` | Color variant |

**Usage:** Attach as child of target element.

**Example:**
```html
<ty-button>
  Hover me
  <ty-tooltip>This is helpful information</ty-tooltip>
</ty-button>
```

---

#### ty-popup

Interactive popup with dropdown behavior.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `manual` | boolean | `false` | Manual control mode |
| `disable-close` | boolean | `false` | Prevent auto-close |
| `placement` | string | `'bottom'` | Popup position |
| `offset` | number | `8` | Distance from anchor |

**Methods:**
- `show()` - Open popup
- `hide()` - Close popup

---

### Utility Components

#### ty-icon

SVG icon component.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `name` | string | - | Icon name |
| `size` | string | - | `'xs'` \| `'sm'` \| `'md'` \| `'lg'` \| `'xl'` |
| `spin` | boolean | `false` | Spinning animation |
| `pulse` | boolean | `false` | Pulsing animation |
| `tempo` | string | - | Animation speed |

**Example:**
```html
<ty-icon name="check" size="md"></ty-icon>
<ty-icon name="loader" spin></ty-icon>
```

---

#### ty-resize-observer

Container that tracks and reports its dimensions.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `id` | string | (required) | Element identifier for registry |
| `debounce` | number | `0` | Debounce delay (ms) |

**Usage:** Query dimensions via `window.tyResizeObserver.getSize(id)`

---

#### ty-scroll-container

Scrollable container with visual indicators.

**Attributes:**
| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `shadow` | boolean | `true` | Show scroll shadows |
| `max-height` | string | - | Maximum height |
| `hide-scrollbar` | boolean | `false` | Hide native scrollbar |

---

## Icon System

### Registering Icons

**JavaScript:**
```javascript
// Register single icon
window.tyIcons.register({ 'heart': '<svg>...</svg>' });

// Register multiple icons
window.tyIcons.register({
  'heart': '<svg>...</svg>',
  'star': '<svg>...</svg>',
  'check': '<svg>...</svg>'
});
```

**ES Modules:**
```javascript
import { registerIcons } from '@gersak/ty';

registerIcons({
  'heart': '<svg>...</svg>',
  'star': '<svg>...</svg>'
});
```

**ClojureScript (Recommended):**
```clojure
(ns my.app.icons
  (:require [ty.icons :as icons]
            [ty.lucide :as lucide]))

;; Recommended: Automatic retry if ty.js hasn't loaded yet
(icons/register-async!
  {:check lucide/check
   :heart lucide/heart
   :star lucide/star})

;; With options
(icons/register-async!
  {:check lucide/check}
  {:max-retries 20
   :delay-ms 100
   :on-success #(println "Icons loaded!")})

;; Synchronous (only if ty.js is already loaded)
(icons/register! {:check lucide/check})

;; Check if icon is registered
(icons/registered? :check) ;; => true

;; Advanced: Direct JavaScript interop
(js/window.tyIcons.register
  (clj->js {:check lucide/check}))
```

### Icon Sets

**ClojureScript (Tree-shakeable):**

Add to `deps.edn`:
```clojure
{:deps {dev.gersak/ty-icons {:mvn/version "x.y.z"}}}
```

Available icon sets (only icons you use are included in the build):
- **Lucide** - `ty.lucide`
- **Heroicons** (outline/solid) - `ty.heroicons.outline` / `ty.heroicons.solid`
- **Material Icons** - `ty.material.*`
- **FontAwesome 6** (brands/regular/solid) - `ty.fav6.brands` / `ty.fav6.regular` / `ty.fav6.solid`

```clojure
(ns my.app
  (:require [ty.lucide :as lucide]
            [ty.heroicons.outline :as hero]
            [ty.fav6.brands :as brands]))

(icons/register-async!
  {:check lucide/check
   :arrow-left hero/arrow-left
   :github brands/github})
```

**JavaScript/TypeScript:**

Compatible with popular icon libraries:
- **Lucide** - `lucide-static`
- **FontAwesome** - `@fortawesome/free-solid-svg-icons`
- **Heroicons** - `heroicons`
- **Material Icons** - `@mdi/svg`

**Example with Lucide:**
```javascript
import { icons } from 'lucide-static';
window.tyIcons.register(icons);
```

### Using Icons

```html
<ty-icon name="check" size="md"></ty-icon>
<ty-icon name="loader" spin></ty-icon>
<ty-icon name="heart" size="lg" pulse></ty-icon>
```

### Icon API

```javascript
// Check if icon exists
window.tyIcons.has('heart')  // boolean

// Get icon SVG
window.tyIcons.get('heart')  // string | undefined

// List all registered icons
window.tyIcons.list()  // string[]

// Get cache info
await window.tyIcons.cacheInfo()

// Clear cache
await window.tyIcons.clearCache()
```

---

## Utility Functions

### Resize Observer

Track element dimensions across the application.

```javascript
// Get current size
const size = window.tyResizeObserver.getSize('my-element');
// { width: 300, height: 200 }

// Subscribe to size changes
const unsubscribe = window.tyResizeObserver.onResize('my-element', ({ width, height }) => {
  console.log(`Resized to ${width}x${height}`);
});

// Get all tracked sizes
window.tyResizeObserver.sizes;
// { 'element-1': { width, height }, 'element-2': { width, height } }

// Cleanup subscription
unsubscribe();
```

### Scroll Lock

Prevent body scrolling (used by modals).

```javascript
import { lockScroll, unlockScroll, isLocked, forceUnlockAll } from '@gersak/ty';

// Lock scrolling for a component
lockScroll('modal-123');

// Check if locked
isLocked();  // true

// Unlock (ref-counted, unlocks when all locks released)
unlockScroll('modal-123');

// Force unlock all
forceUnlockAll();
```

```html
<!-- CDN -->
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/ty.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css">
```

### Positioning

Smart positioning for floating elements.

```javascript
import { findBestPosition, autoUpdate, placementPreferences } from '@gersak/ty';

// One-time positioning
const result = findBestPosition({
  targetEl: button,
  floatingEl: tooltip,
  preferences: placementPreferences.tooltip,
  offset: 8,
  padding: 8
});

tooltip.style.left = result.x + 'px';
tooltip.style.top = result.y + 'px';

// Continuous positioning (updates on scroll/resize)
const cleanup = autoUpdate(
  targetEl,
  floatingEl,
  (position) => {
    floatingEl.style.left = position.x + 'px';
    floatingEl.style.top = position.y + 'px';
  },
  { preferences: placementPreferences.dropdown }
);

// Stop auto-updating
cleanup();
```

---

## Framework Integration

### React

**Installation:**
```bash
npm install @gersak/ty-react
```

**Import Patterns:**
```typescript
// Ty-prefixed (explicit)
import { TyButton, TyInput, TyModal } from '@gersak/ty-react';

// Short names (clean)
import { Button, Input, Modal } from '@gersak/ty-react';
```

**Event Mapping:**
| React Prop | Web Component Event | When Fires |
|------------|---------------------|------------|
| `onChange` | `input` | Every keystroke |
| `onChangeCommit` | `change` | On blur |
| `onFocus` | `focus` | Focus |
| `onBlur` | `blur` | Blur |

**Event Detail Access:**
```typescript
<TyInput
  onChange={(e: CustomEvent<TyInputEventDetail>) => {
    const value = e.detail.value;
    const formatted = e.detail.formattedValue;
    const raw = e.detail.rawValue;
  }}
/>
```

**Ref Usage for Imperative Methods:**
```typescript
import { useRef } from 'react';
import { TyModal, TyModalRef, TyButton } from '@gersak/ty-react';

function App() {
  const modalRef = useRef<TyModalRef>(null);

  return (
    <>
      <TyButton onClick={() => modalRef.current?.show()}>Open</TyButton>
      <TyModal ref={modalRef}>
        <h2>Modal Content</h2>
        <TyButton onClick={() => modalRef.current?.hide()}>Close</TyButton>
      </TyModal>
    </>
  );
}
```

**TypeScript Types:**
```typescript
import type {
  TyButtonProps,
  TyInputProps,
  TyInputEventDetail,
  TyDropdownEventDetail,
  TyModalRef
} from '@gersak/ty-react';
```

---

### ClojureScript / Replicant

**Event Handling:**
```clojure
;; Use :on map for event listeners
[:ty-input {:placeholder "Enter name"
            :on {:input (fn [e]
                          (let [value (-> e .-detail .-value)]
                            (swap! state assoc :name value)))
                 :change (fn [e]
                           (println "Final value:" (-> e .-detail .-value)))}}]
```

**Accessing Event Detail:**
```clojure
(defn handle-dropdown-change [e]
  (let [detail (.-detail e)
        value (.-value detail)
        text (.-text detail)]
    (swap! state assoc :selection {:value value :text text})))

[:ty-dropdown {:on {:change handle-dropdown-change}}
 [:option {:value "opt1"} "Option 1"]
 [:option {:value "opt2"} "Option 2"]]
```

**Icon Registration:**
```clojure
(defn init-icons []
  (let [icons (clj->js {"check" "<svg>...</svg>"
                        "x" "<svg>...</svg>"})]
    (.register js/window.tyIcons icons)))
```

**Dynamic Classes:**
```clojure
;; GOOD: Vector of classes
[:div {:class ["ty-elevated" "p-4" (when active? "ty-bg-accent-")]}]

;; BAD: String concatenation
[:div {:class (str "ty-elevated p-4 " (when active? "ty-bg-accent-"))}]
```

---

## Common Patterns

### Card Component
```html
<div class="ty-elevated p-6 rounded-lg border ty-border-">
  <h2 class="ty-text++ text-xl font-bold mb-4">Card Title</h2>
  <p class="ty-text- text-sm mb-6">Description text goes here.</p>
  <ty-button flavor="primary">Action</ty-button>
</div>
```

### Alert Components
```html
<!-- Success Alert -->
<div class="ty-bg-success- ty-border-success border rounded-lg p-4">
  <h3 class="ty-text-success++ font-semibold">Success!</h3>
  <p class="ty-text-success text-sm">Operation completed successfully.</p>
</div>

<!-- Error Alert -->
<div class="ty-bg-danger- ty-border-danger border rounded-lg p-4">
  <h3 class="ty-text-danger++ font-semibold">Error!</h3>
  <p class="ty-text-danger text-sm">Something went wrong.</p>
</div>
```

### Button with Icon
```html
<!-- GOOD: Use gap, no margin on icon -->
<ty-button flavor="primary" class="flex items-center gap-2">
  <ty-icon name="save" size="sm"></ty-icon>
  Save
</ty-button>

<!-- Or use slots -->
<ty-button flavor="primary">
  <ty-icon slot="start" name="save" size="sm"></ty-icon>
  Save
</ty-button>
```

### Form Integration
```html
<form id="myForm">
  <ty-input name="email" type="email" required label="Email"></ty-input>
  <ty-input name="password" type="password" required label="Password"></ty-input>
  <ty-checkbox name="remember">Remember me</ty-checkbox>
  <ty-dropdown name="role" required>
    <option value="user">User</option>
    <option value="admin">Admin</option>
  </ty-dropdown>
  <ty-button type="submit" flavor="primary">Submit</ty-button>
</form>

<script>
document.getElementById('myForm').addEventListener('submit', (e) => {
  e.preventDefault();
  const data = new FormData(e.target);
  console.log(Object.fromEntries(data));
});
</script>
```

### Modal Pattern
```html
<!-- Always render modals, control with 'open' attribute -->
<ty-modal id="my-modal">
  <h2 class="ty-text++ text-xl mb-4">Modal Title</h2>
  <p class="ty-text mb-6">Modal content goes here.</p>
  <div class="flex gap-2 justify-end">
    <ty-button onclick="document.getElementById('my-modal').hide()">Cancel</ty-button>
    <ty-button flavor="primary" onclick="handleConfirm()">Confirm</ty-button>
  </div>
</ty-modal>

<ty-button onclick="document.getElementById('my-modal').show()">Open Modal</ty-button>
```

---

## Quick Reference

### All Components

| Component | Category | Primary Attributes |
|-----------|----------|-------------------|
| `ty-button` | Form | `flavor`, `size`, `type`, `disabled` |
| `ty-input` | Form | `type`, `value`, `placeholder`, `size` |
| `ty-checkbox` | Form | `checked`, `value`, `disabled` |
| `ty-textarea` | Form | `value`, `rows`, `resize` |
| `ty-copy` | Form | `value`, `format` |
| `ty-dropdown` | Selection | `value`, `placeholder`, `searchable` |
| `ty-multiselect` | Selection | `value`, `placeholder` |
| `ty-option` | Selection | `value`, `selected`, `disabled` |
| `ty-tag` | Selection | `flavor`, `value`, `dismissible` |
| `ty-tabs` | Navigation | `active`, `height`, `placement` |
| `ty-tab` | Navigation | `id`, `label` |
| `ty-wizard` | Navigation | `active`, `completed`, `height` |
| `ty-step` | Navigation | `id`, `label` |
| `ty-calendar` | Date/Time | `year`, `month`, `day` |
| `ty-date-picker` | Date/Time | `value`, `with-time`, `locale` |
| `ty-modal` | Overlay | `open`, `backdrop`, `close-on-escape` |
| `ty-tooltip` | Overlay | `placement`, `delay`, `flavor` |
| `ty-popup` | Overlay | `placement`, `manual` |
| `ty-icon` | Utility | `name`, `size`, `spin` |
| `ty-resize-observer` | Utility | `id`, `debounce` |
| `ty-scroll-container` | Utility | `shadow`, `max-height` |

### All Events

| Component | Event | Detail Structure |
|-----------|-------|-----------------|
| `ty-input` | `input` | `{ value, formattedValue, rawValue, originalEvent }` |
| `ty-input` | `change` | `{ value, formattedValue, rawValue, originalEvent }` |
| `ty-checkbox` | `input` | `{ value, checked, formValue, originalEvent }` |
| `ty-dropdown` | `change` | `{ value, text, option, originalEvent }` |
| `ty-dropdown` | `search` | `{ query, originalEvent }` |
| `ty-multiselect` | `change` | `{ values, action, item }` |
| `ty-calendar` | `change` | `{ year, month, day, action, source, dayContext }` |
| `ty-calendar` | `navigate` | `{ month, year, action, source }` |
| `ty-tabs` | `change` | `{ activeId, activeIndex, previousId, previousIndex }` |
| `ty-wizard` | `change` | `{ activeId, activeIndex, previousId, previousIndex, direction }` |
| `ty-modal` | `close` | `{ reason, returnValue? }` |
| `ty-tag` | `dismiss` | - |
| `ty-button` | `click` | `{ originalEvent }` |

### CSS Classes by Category

**Surfaces:**
`ty-canvas`, `ty-content`, `ty-elevated`, `ty-floating`, `ty-input`

**Text:**
`ty-text--`, `ty-text-`, `ty-text`, `ty-text+`, `ty-text++`
`ty-text-{color}--`, `ty-text-{color}-`, `ty-text-{color}`, `ty-text-{color}+`, `ty-text-{color}++`

**Backgrounds:**
`ty-bg-{color}-`, `ty-bg-{color}`, `ty-bg-{color}+`

**Borders:**
`ty-border--`, `ty-border-`, `ty-border`, `ty-border+`, `ty-border++`
`ty-border-{color}`

**Colors:** `primary`, `secondary`, `success`, `danger`, `warning`, `neutral`, `accent`

---

## Component Slots Reference

Many Ty components support named slots for customization. Use these to add icons, rich content, or custom elements.

### Slot Quick Reference

```
<ty-button>
  ├── slot="start"     Left side (icons, badges)
  └── slot="end"       Right side (icons, arrows)

<ty-input>
  ├── slot="start"     Inside input, left (search icons, prefixes)
  └── slot="end"       Inside input, right (clear buttons, icons)

<ty-tag>
  ├── slot="start"     Before tag text (icons, avatars)
  └── slot="end"       After tag text (badges, counts)

<ty-dropdown>
  └── slot="selected"  Custom selected value display

<ty-multiselect>
  └── slot="selected"  Custom selected values display

<ty-tabs>
  ├── slot="label-{id}"   Custom label for tab (use tab's id)
  └── slot="marker"       Custom active tab indicator

<ty-wizard>
  └── slot="indicator-{id}"  Custom step indicator (use step's id)
```

### Slot Usage Examples

**Button with icons:**
```html
<ty-button flavor="primary">
  <ty-icon slot="start" name="save"></ty-icon>
  Save Changes
  <ty-icon slot="end" name="arrow-right"></ty-icon>
</ty-button>
```

**Input with search icon and clear button:**
```html
<ty-input placeholder="Search...">
  <ty-icon slot="start" name="search"></ty-icon>
  <ty-icon slot="end" name="x"></ty-icon>
</ty-input>
```

**Tag with icon and badge:**
```html
<ty-tag flavor="primary">
  <ty-icon slot="start" name="user"></ty-icon>
  John Doe
  <span slot="end" class="ty-bg-primary px-1 rounded text-xs">Admin</span>
</ty-tag>
```

**Tabs with custom labels:**
```html
<ty-tabs height="400px" active="profile">
  <!-- Custom rich labels -->
  <span slot="label-profile" class="flex items-center gap-2">
    <ty-icon name="user" size="sm"></ty-icon>
    Profile
  </span>
  <span slot="label-settings" class="flex items-center gap-2">
    <ty-icon name="settings" size="sm"></ty-icon>
    Settings
  </span>

  <!-- Optional custom marker -->
  <div slot="marker" class="h-1 bg-blue-500 rounded-full"></div>

  <!-- Tab panels -->
  <ty-tab id="profile" label="Profile">Profile content</ty-tab>
  <ty-tab id="settings" label="Settings">Settings content</ty-tab>
</ty-tabs>
```

**Wizard with custom step indicators:**
```html
<ty-wizard height="500px" active="welcome">
  <!-- Custom indicators -->
  <div slot="indicator-welcome">
    <ty-icon name="home" size="sm"></ty-icon>
  </div>
  <div slot="indicator-account">
    <ty-icon name="user" size="sm"></ty-icon>
  </div>
  <div slot="indicator-complete">
    <ty-icon name="check" size="sm"></ty-icon>
  </div>

  <!-- Step panels -->
  <ty-step id="welcome" label="Welcome">Welcome content</ty-step>
  <ty-step id="account" label="Account">Account content</ty-step>
  <ty-step id="complete" label="Complete">Complete content</ty-step>
</ty-wizard>
```

**Dropdown with custom selected display:**
```html
<ty-dropdown placeholder="Select user">
  <div slot="selected" class="flex items-center gap-2">
    <img src="avatar.jpg" class="w-6 h-6 rounded-full">
    <span>John Doe</span>
  </div>
  <ty-option value="john">John Doe</ty-option>
  <ty-option value="jane">Jane Smith</ty-option>
</ty-dropdown>
```

---

## Best Practices

1. **ALWAYS use Ty components** - Never improvise HTML when a Ty component exists (see Component Selection Guide above)
2. **Always use Ty for colors** - Never mix with Tailwind color utilities
3. **Use Tailwind for layout** - Padding, margin, flex, grid
4. **Access event.detail** - Not event.target.value
5. **Always render modals** - Control with `open` attribute
6. **Use vectors for dynamic classes** - Not string concatenation
7. **Register icons at startup** - Before components render
8. **Use gap for icon spacing** - Not margins on icons
9. **Prevent form defaults** - `e.preventDefault()` on submit
