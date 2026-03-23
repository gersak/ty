# Ty Component Library — AI Agent Guide

**Ty** is a framework-agnostic web component library. TypeScript core (`@gersak/ty`), React wrappers (`@gersak/ty-react`), ClojureScript infra (`dev.gersak/ty`).

---

## Rule 1: Always Use Ty Components

> **When a Ty component exists, USE IT. Do not improvise HTML.**

| Need | USE THIS | NOT THIS |
|------|----------|----------|
| Button | `<ty-button>` | `<button>`, `<div onclick>` |
| Text input | `<ty-input>` | `<input>` |
| Money/currency | `<ty-input type="currency">` | `<input type="number">` + manual formatting |
| Percentage | `<ty-input type="percent">` | `<input type="number">` + `%` suffix |
| Large numbers | `<ty-input type="compact">` | `<input type="number">` + abbreviation |
| Checkbox | `<ty-checkbox>` | `<input type="checkbox">` |
| Textarea | `<ty-textarea>` | `<textarea>` |
| Dropdown | `<ty-dropdown>` | `<select>`, custom menus |
| Multi-select | `<ty-multiselect>` | Multiple checkboxes |
| Modal | `<ty-modal>` | `<dialog>`, custom overlays |
| Tooltip | `<ty-tooltip>` | `title` attr, custom divs |
| Popup | `<ty-popup>` | Custom positioned divs |
| Tabs | `<ty-tabs>` + `<ty-tab>` | Custom tab implementations |
| Wizard | `<ty-wizard>` + `<ty-step>` | Custom step indicators |
| Date picker | `<ty-date-picker>` | `<input type="date">` |
| Calendar | `<ty-calendar>` | Custom calendar grids |
| Tags/Chips | `<ty-tag>` | `<span>` with classes |
| Icons | `<ty-icon>` | `<svg>`, `<img>`, icon fonts |
| Copy button | `<ty-copy>` | Custom copy buttons |
| Scrollable area | `<ty-scroll-container>` | `overflow-auto` + manual indicators |
| Field label | `label` attribute on component | `<label>` element |
| Error message | `error` attribute on component | `<span class="text-red-500">` |
| Debounced input | `delay` attribute | Manual `setTimeout`/debounce |

**Plain HTML is OK for:** layout (`<div>`, `<section>`), text (`<h1>`-`<h6>`, `<p>`, `<span>` with Ty classes), lists, links, images.

---

## Rule 2: Use Built-in Attributes

Form components have built-in `label`, `error`, `placeholder`, `required`, `disabled`, `flavor`, and `size`. Use them.

```html
<!-- WRONG -->
<label class="block text-sm font-medium mb-1">Email</label>
<ty-input type="email" placeholder="you@example.com"></ty-input>
<span class="text-red-500 text-sm">Invalid email</span>

<!-- CORRECT -->
<ty-input type="email" label="Email" placeholder="you@example.com" error="Invalid email"></ty-input>
```

```clojure
;; WRONG
[:div
 [:label.block.text-sm.font-medium.mb-1 "Email"]
 [:ty-input {:type "email" :placeholder "you@example.com"}]
 [:span.text-red-500.text-sm "Invalid email"]]

;; CORRECT
[:ty-input {:type "email" :label "Email" :placeholder "you@example.com" :error "Invalid email"}]
```

---

## Rule 3: Use Slots for Icons

Components with `start`/`end` slots handle spacing automatically. Do not use flex/gap wrappers.

```html
<ty-button flavor="primary">
  <ty-icon slot="start" name="save" size="sm"></ty-icon>
  Save
</ty-button>

<ty-input type="currency" currency="EUR" label="Price">
  <ty-icon slot="start" name="euro"></ty-icon>
</ty-input>
```

```clojure
[:ty-button {:flavor "primary"}
 [:ty-icon {:slot "start" :name "save" :size "sm"}]
 "Save"]
```

### All Slots

```
ty-button       slot="start" | (default text) | slot="end"
ty-input        slot="start" | (input field)  | slot="end"
ty-tag          slot="start" | (tag text)     | slot="end"
ty-dropdown     slot="selected"  (custom selected display)
ty-multiselect  slot="selected"  (custom selected display)
ty-tabs         slot="label-{id}" (rich tab label)  |  slot="marker" (active indicator)
ty-wizard       slot="indicator-{id}" (custom step indicator)
```

---

## Rule 4: Use the Right Input Type

> **For money, use `type="currency"`. For percentages, use `type="percent"`. Never use `type="number"` with manual formatting.**

| Type | When | Display (on blur) | Value |
|------|------|-------------------|-------|
| `"currency"` | Money (prices, budgets, transactions) | `$1,234.56` / `€1.234,56` | `1234.56` |
| `"percent"` | Rates, discounts | `15.00%` | `15` |
| `"compact"` | Large numbers, stats | `1.2M` / `1.2K` | `1234567` |
| `"number"` | Plain numeric, no formatting | `1234.56` | `1234.56` |

**Behavior:** Raw number while editing, formatted on blur. Uses `Intl.NumberFormat`.

**Attributes:** `currency` (ISO 4217 code, default `"USD"`), `locale` (default `"en-US"`), `precision` (decimal places).

**Events:** `detail: { value, formattedValue, rawValue, originalEvent }` — `formattedValue` is the display string, `rawValue` is the number.

**FormData:** Submits raw number, not formatted string.

```html
<ty-input type="currency" currency="EUR" locale="de-DE" label="Price" placeholder="0.00">
  <ty-icon slot="start" name="euro"></ty-icon>
</ty-input>

<ty-input type="percent" label="Discount" precision="1"></ty-input>
<ty-input type="compact" label="Revenue"></ty-input>
```

---

## Rule 5: Ty for Colors, Tailwind for Layout

```html
<!-- GOOD -->
<div class="ty-elevated p-6 rounded-lg flex items-center">
  <h2 class="ty-text++ text-xl font-bold">Title</h2>
</div>

<!-- BAD — never use Tailwind for colors -->
<div class="bg-blue-500 text-white">...</div>
```

**Surfaces vs Backgrounds — know the difference:**

- **Surfaces** (`ty-canvas`, `ty-content`, `ty-elevated`, `ty-floating`, `ty-input`) are for **layout areas**: cards, panels, sidebars, modals, page background, form fields.
- **Backgrounds** (`ty-bg-primary`, `ty-bg-success-`, `ty-bg-danger`, etc.) are for **small UI elements**: buttons, tags, badges, toasts, alerts, status indicators.

```html
<!-- WRONG — using bg color for a card surface -->
<div class="ty-bg-primary- p-6 rounded-lg">Card content</div>

<!-- CORRECT — surface for cards, bg for small elements -->
<div class="ty-elevated p-6 rounded-lg">
  <ty-tag flavor="success">Active</ty-tag>
  <div class="ty-bg-danger- p-2 rounded text-sm">Error toast</div>
</div>
```

---

## Rule 6: Use Flavors Semantically

| Flavor | Intent | Examples |
|--------|--------|---------|
| `primary` | Main action | Submit, Save, Confirm |
| `secondary` | Alternative action | Export, Import |
| `success` | Positive | Income, completed, "Added!" |
| `danger` | Destructive/negative | Delete, expenses, errors |
| `warning` | Caution | Unsaved changes, limits |
| `neutral` | Default, no weight | Cancel, Close |
| `accent` | Brand/highlight | Featured items, active states |

---

## Rule 7: Correct Child Elements

| Parent | Children | Example |
|--------|----------|---------|
| `ty-dropdown` | `<option>` (simple) or `<ty-option>` (rich HTML) | `<option value="us">US</option>` |
| `ty-multiselect` | `<ty-tag>` only | `<ty-tag value="js">JavaScript</ty-tag>` |

---

## CSS Design System

### Surfaces
| Class | Use |
|-------|-----|
| `ty-canvas` | App background |
| `ty-content` | Main content area |
| `ty-elevated` | Cards, panels (with shadow) |
| `ty-floating` | Modals, dropdowns, tooltips |
| `ty-input` | Form controls |

### Text Hierarchy
`ty-text++` (max) > `ty-text+` (high) > `ty-text` (normal) > `ty-text-` (reduced) > `ty-text--` (minimal)

### Semantic Colors
Available for text, backgrounds, borders: `primary`, `secondary`, `success`, `danger`, `warning`, `neutral`, `accent`

```
Text:    ty-text-{color}++  ty-text-{color}+  ty-text-{color}  ty-text-{color}-  ty-text-{color}--
Bg:      ty-bg-{color}+     ty-bg-{color}     ty-bg-{color}-
Border:  ty-border-{color}
Base:    ty-border++  ty-border+  ty-border  ty-border-  ty-border--
```

Hover/focus: `hover:ty-bg-primary`, `focus:ty-border-primary`, etc.

Dark mode: automatic via `html.dark` or `html[data-theme="dark"]`.

### Color Customization

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
```

Pattern: `--ty-color-{name}-{strong|mild|soft|faint}`, `--ty-bg-{name}-{mild|soft}`, `--ty-border-{name}`.

Dark mode overrides: scope under `html.dark, html[data-theme="dark"]`.

---

## Components Reference

### ty-button

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `flavor` | string | `'neutral'` | `primary` \| `secondary` \| `success` \| `danger` \| `warning` \| `neutral` |
| `size` | string | `'md'` | `xs` \| `sm` \| `md` \| `lg` \| `xl` |
| `type` | string | `'submit'` | `button` \| `submit` \| `reset` |
| `disabled` | boolean | `false` | |
| `pill` | boolean | `false` | Rounded shape |
| `outlined` | boolean | `false` | |
| `filled` | boolean | `false` | Solid variant |
| `plain` | boolean | `false` | Minimal variant |
| `action` | boolean | `false` | Action style |
| `accent` | boolean | `false` | Accent color |
| `wide` | boolean | `false` | Full width |

**Slots:** `start`, (default), `end` | **Events:** `click` → `{ originalEvent }`

**Mobile/responsive tips:**
- Use `wide` for primary actions on mobile — full-width buttons are easier to tap
- For responsive layouts, apply `wide` conditionally or use `class="w-full sm:w-auto"` on the host
- Use `size="lg"` on mobile for comfortable touch targets (44px+)
- Action bars on mobile: stack buttons vertically with `wide`, on desktop use inline with gap

```html
<!-- Mobile-friendly submit -->
<ty-button flavor="primary" wide>
  <ty-icon slot="start" name="check"></ty-icon>
  Save Changes
</ty-button>

<!-- Responsive: full-width on mobile, auto on desktop -->
<ty-button flavor="primary" class="w-full sm:w-auto" size="lg">Submit</ty-button>
```

---

### ty-input

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `type` | string | `'text'` | `text` \| `email` \| `password` \| `number` \| `tel` \| `url` \| `currency` \| `percent` \| `compact` |
| `value` | string | `''` | |
| `name` | string | - | Form name |
| `placeholder` | string | - | |
| `label` | string | - | Built-in label |
| `error` | string | - | Built-in error message |
| `disabled` | boolean | `false` | |
| `required` | boolean | `false` | |
| `size` | string | `'md'` | `xs` \| `sm` \| `md` \| `lg` \| `xl` |
| `flavor` | string | `'neutral'` | |
| `currency` | string | `'USD'` | ISO 4217 code (for `type="currency"`) |
| `locale` | string | `'en-US'` | Locale for numeric formatting |
| `precision` | number | - | Decimal places |
| `delay` | number | `0` | Debounce ms (0-5000) |

**Slots:** `start`, `end` | **Events:** `input`, `change` → `{ value, formattedValue, rawValue, originalEvent }` | `focus`, `blur`

---

### ty-checkbox

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `checked` | boolean | `false` | |
| `value` | string | `'on'` | Form value when checked |
| `name` | string | - | |
| `disabled` | boolean | `false` | |
| `required` | boolean | `false` | |
| `error` | string | - | |
| `size` | string | `'md'` | |
| `flavor` | string | `'neutral'` | |

**Slots:** (default) = label | **Events:** `input`, `change` → `{ value, checked, formValue, originalEvent }`

---

### ty-textarea

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `value` | string | `''` | |
| `name` | string | - | |
| `placeholder` | string | - | |
| `label` | string | - | |
| `error` | string | - | |
| `disabled` | boolean | `false` | |
| `required` | boolean | `false` | |
| `size` | string | `'md'` | |
| `rows` | string | `'3'` | |
| `resize` | string | `'none'` | `none` \| `both` \| `horizontal` \| `vertical` |
| `min-height` | string | - | |
| `max-height` | string | - | |

**Events:** `input`, `change` → `{ value, originalEvent }`

---

### ty-copy

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `value` | string | - | Text to copy |
| `label` | string | - | |
| `format` | string | `'text'` | `text` \| `code` |
| `disabled` | boolean | `false` | |

---

### ty-dropdown

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `value` | string | `''` | |
| `name` | string | - | |
| `placeholder` | string | `'Select an option...'` | |
| `label` | string | - | |
| `disabled` | boolean | `false` | |
| `readonly` | boolean | `false` | |
| `required` | boolean | `false` | |
| `searchable` | boolean | `true` | |
| `not-searchable` | boolean | - | Disable search |
| `clearable` | boolean | `true` | |
| `not-clearable` | boolean | - | Disable clear |
| `size` | string | `'md'` | |
| `flavor` | string | `'neutral'` | |
| `delay` | number | `0` | Search debounce |

**Children:** `<option>` or `<ty-option>` (for rich HTML) | **Slots:** `selected`

**Events:** `change` → `{ value, text, option, originalEvent }` | `search` → `{ query, originalEvent }`

---

### ty-multiselect

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `value` | string | `''` | Comma-separated values |
| `name` | string | - | |
| `placeholder` | string | `'Select options...'` | |
| `label` | string | - | |
| `disabled` | boolean | `false` | |
| `readonly` | boolean | `false` | |
| `required` | boolean | `false` | |
| `searchable` | boolean | `true` | |
| `size` | string | `'md'` | |
| `flavor` | string | `'neutral'` | |
| `delay` | number | `0` | |
| `selected-label` | string | `'Selected'` | |
| `available-label` | string | `'Available'` | |

**Children:** `<ty-tag>` only | **Slots:** `selected`

**Events:** `change` → `{ values: string[], action: 'add'|'remove'|'clear'|'set', item }` | `search` → `{ query, element }`

---

### ty-option

Rich HTML option for `<ty-dropdown>`. Attrs: `value`, `selected`, `disabled`, `highlighted`, `hidden`. Default slot for content.

---

### ty-tag

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `flavor` | string | `'neutral'` | |
| `size` | string | `'md'` | |
| `value` | string | - | |
| `selected` | boolean | `false` | |
| `pill` | boolean | `true` | |
| `clickable` | boolean | `false` | |
| `dismissible` | boolean | `false` | |
| `disabled` | boolean | `false` | |

**Slots:** `start`, (default), `end` | **Events:** `click`, `dismiss`

---

### ty-tabs / ty-tab

**ty-tabs:** `width` (default `'100%'`), `height` (required), `active` (tab id), `placement` (`top` | `bottom`)

**ty-tab:** `id` (required), `label` (required)

**Slots:** `label-{id}` (rich label), `marker` (active indicator)

**Events:** `change` → `{ activeId, activeIndex, previousId, previousIndex }`

```html
<ty-tabs height="400px" active="overview">
  <span slot="label-overview" class="flex items-center gap-2">
    <ty-icon name="layout-dashboard" size="sm"></ty-icon>
    Overview
  </span>
  <ty-tab id="overview" label="Overview">Content here</ty-tab>
  <ty-tab id="details" label="Details">Details here</ty-tab>
</ty-tabs>
```

---

### ty-wizard / ty-step

**ty-wizard:** `width`, `height` (required), `active` (step id), `completed` (comma-separated ids), `orientation` (`horizontal` | default)

**ty-step:** `id` (required), `label` (required)

**Slots:** `indicator-{id}` (custom step indicator)

**Events:** `change` → `{ activeId, activeIndex, previousId, previousIndex, direction }`

---

### ty-calendar

Attrs: `year`, `month`, `day`, `name`, `required`. Property: `dayContentFn`.

**Events:** `change` → `{ year, month, day, action, source, dayContext }` | `navigate` → `{ month, year, action, source }`

**Form value:** ISO date `YYYY-MM-DD`

---

### ty-date-picker

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `value` | string | - | UTC ISO datetime |
| `name` | string | - | |
| `label` | string | - | |
| `placeholder` | string | - | |
| `with-time` | boolean | `false` | Include time picker |
| `disabled` | boolean | `false` | |
| `required` | boolean | `false` | |
| `locale` | string | `'en-US'` | |
| `size` | string | `'md'` | |

**Events:** `change` | **Form value:** UTC ISO `2024-09-21T08:30:00.000Z`

---

### ty-modal

Attrs: `open`, `backdrop` (default true), `close-on-outside-click` (true), `close-on-escape` (true), `protected`.

**Methods:** `show()`, `hide()` | **Events:** `close` → `{ reason, returnValue? }`

Always render modals in DOM. Control with `open` attribute or `show()`/`hide()`.

---

### ty-tooltip

Attrs: `placement` (default `'top'`), `offset` (8), `delay` (200ms), `disabled`, `flavor` (default `'dark'`).

Nest as child of target: `<ty-button>Hover<ty-tooltip>Help text</ty-tooltip></ty-button>`

---

### ty-popup

Attrs: `manual`, `disable-close`, `placement` (default `'bottom'`), `offset` (8).

**Methods:** `show()`, `hide()`

---

### ty-icon

Attrs: `name`, `size` (`xs` | `sm` | `md` | `lg` | `xl`), `spin`, `pulse`, `tempo`.

---

### ty-scroll-container

Scrollable container with shadow indicators showing there's more content above/below.

| Attribute | Type | Default | Description |
|-----------|------|---------|-------------|
| `shadow` | boolean | `true` | Show top/bottom shadow indicators |
| `max-height` | string | - | Constrain height (e.g. `"400px"`, `"50vh"`) |
| `hide-scrollbar` | boolean | `false` | Hide native scrollbar |
| `custom-scrollbar` | boolean | `false` | Render custom styled scrollbar |
| `overflow-x` | boolean | `false` | Enable horizontal scrolling |

> **Use `ty-scroll-container` instead of `overflow-auto`** for any scrollable content area.
> It gives you scroll shadow indicators for free — users immediately see there's more content.

```html
<!-- Transaction list with max height -->
<ty-scroll-container max-height="400px">
  <div>Item 1</div>
  <div>Item 2</div>
  <!-- ...many items... -->
</ty-scroll-container>

<!-- Clean scrollbar for sidebars -->
<ty-scroll-container max-height="100vh" custom-scrollbar hide-scrollbar>
  <nav>...</nav>
</ty-scroll-container>
```

```clojure
[:ty-scroll-container {:max-height "400px"}
 (for [item items]
   [:div.p-3.ty-elevated.rounded-lg (:name item)])]
```

---

### ty-resize-observer

Attrs: `id` (required), `debounce`. Query: `window.tyResizeObserver.getSize(id)`

---

## Icon System

Register icons before components render:

```javascript
// JavaScript
window.tyIcons.register({ 'heart': '<svg>...</svg>', 'star': '<svg>...</svg>' });

// ES Modules
import { registerIcons } from '@gersak/ty';
registerIcons({ 'heart': '<svg>...</svg>' });
```

```clojure
;; ClojureScript (recommended — auto-retries until ty.js loads)
(require '[ty.icons :as icons] '[ty.lucide :as lucide])
(icons/register-async! {:check lucide/check :heart lucide/heart})
```

**Icon sets (ClojureScript, tree-shakeable):** `ty.lucide`, `ty.heroicons.outline`, `ty.heroicons.solid`, `ty.material.*`, `ty.fav6.brands`, `ty.fav6.regular`, `ty.fav6.solid`

**JS libraries:** `lucide-static`, `@fortawesome/free-solid-svg-icons`, `heroicons`, `@mdi/svg`

**API:** `window.tyIcons.has(name)`, `.get(name)`, `.list()`, `.cacheInfo()`, `.clearCache()`

---

## Installation

```bash
# NPM
npm install @gersak/ty

# React
npm install @gersak/ty-react
```

```html
<!-- CDN -->
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/index.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css">
```

```html
<!-- CDN -->
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/ty.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css">
```

```clojure
;; ClojureScript (Clojars)
[dev.gersak/ty "0.4.0"]
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

```typescript
import { TyButton, TyInput, TyModal } from '@gersak/ty-react';
// or short names:
import { Button, Input, Modal } from '@gersak/ty-react';
```

| React Prop | Web Component Event | When |
|------------|---------------------|------|
| `onChange` | `input` | Every keystroke |
| `onChangeCommit` | `change` | On blur |
| `onFocus` | `focus` | Focus |
| `onBlur` | `blur` | Blur |

Imperative methods via refs: `useRef<TyModalRef>()` → `.current?.show()` / `.hide()`

### ClojureScript

```clojure
;; Event handling
[:ty-input {:on {:input (fn [e] (-> e .-detail .-value))
                 :change (fn [e] (-> e .-detail .-value))}}]

;; Dynamic classes — use vectors, not string concatenation
[:div {:class ["ty-elevated" "p-4" (when active? "ty-bg-accent-")]}]
```

---

## Common Patterns

### Card
```html
<div class="ty-elevated p-6 rounded-lg border ty-border-">
  <h2 class="ty-text++ text-xl font-bold mb-4">Title</h2>
  <p class="ty-text- text-sm mb-6">Description</p>
  <ty-button flavor="primary">Action</ty-button>
</div>
```

### Alert
```html
<div class="ty-bg-success- ty-border-success border rounded-lg p-4">
  <h3 class="ty-text-success++ font-semibold">Success!</h3>
  <p class="ty-text-success text-sm">Done.</p>
</div>
```

### Form
```html
<form id="myForm">
  <ty-input name="email" type="email" required label="Email"></ty-input>
  <ty-input name="price" type="currency" currency="USD" label="Price"></ty-input>
  <ty-checkbox name="remember">Remember me</ty-checkbox>
  <ty-dropdown name="role" required label="Role">
    <option value="user">User</option>
    <option value="admin">Admin</option>
  </ty-dropdown>
  <ty-button type="submit" flavor="primary">Submit</ty-button>
</form>
```

FormData from numeric types contains the raw number, not the formatted string.

---

## Utility Functions

```javascript
// Resize observer
window.tyResizeObserver.getSize('element-id')  // { width, height }
window.tyResizeObserver.onResize('id', ({ width, height }) => { ... })

// Scroll lock (used by modals)
import { lockScroll, unlockScroll, isLocked, forceUnlockAll } from '@gersak/ty';

// Positioning for floating elements
import { findBestPosition, autoUpdate, placementPreferences } from '@gersak/ty';
```

---

## All Events Reference

| Component | Event | Detail |
|-----------|-------|--------|
| `ty-input` | `input`, `change` | `{ value, formattedValue, rawValue, originalEvent }` |
| `ty-checkbox` | `input`, `change` | `{ value, checked, formValue, originalEvent }` |
| `ty-dropdown` | `change` | `{ value, text, option, originalEvent }` |
| `ty-dropdown` | `search` | `{ query, originalEvent }` |
| `ty-multiselect` | `change` | `{ values, action, item }` |
| `ty-calendar` | `change` | `{ year, month, day, action, source, dayContext }` |
| `ty-tabs` | `change` | `{ activeId, activeIndex, previousId, previousIndex }` |
| `ty-wizard` | `change` | `{ activeId, activeIndex, previousId, previousIndex, direction }` |
| `ty-modal` | `close` | `{ reason, returnValue? }` |
| `ty-tag` | `dismiss` | — |
| `ty-button` | `click` | `{ originalEvent }` |

Always access via `event.detail.value`, never `event.target.value`.
