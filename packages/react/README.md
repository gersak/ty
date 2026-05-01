# @gersak/ty-react

**React wrappers for Ty Web Components** - bringing framework-agnostic web components to React with full TypeScript support.

## 🎯 Philosophy

Ty web components are **distributed via CDN** and loaded as standard web components. These React wrappers provide:
- ✅ **React-friendly API** - Props instead of attributes
- ✅ **TypeScript types** - Full type safety
- ✅ **Event handling** - React synthetic events
- ✅ **Ref forwarding** - Direct DOM access
- ✅ **Zero bundle overhead** - Just thin wrappers (~2KB)

## 📦 Installation

### Step 1: Load Ty Web Components (CDN)

Add to your `index.html`:

```html
<!DOCTYPE html>
<html>
<head>
  <!-- Ty Web Components & Styles via CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/ty.js"></script>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css">
</head>
<body>
  <div id="root"></div>
</body>
</html>
```

### Step 2: Install React Wrappers

```bash
npm install @gersak/ty-react
```

That's it! No build complexity, no bundler configuration.

## 🚀 Quick Start

```tsx
import React, { useState } from 'react';
import { TyButton, TyInput, TyModal } from '@gersak/ty-react';

function App() {
  const [value, setValue] = useState('');

  return (
    <div>
      <TyButton flavor="primary" onClick={() => alert('Hello!')}>
        Click Me
      </TyButton>
      
      <TyInput 
        placeholder="Enter text..."
        value={value}
        onChange={(e) => setValue(e.detail.value)}  // Fires on every keystroke
      />
    </div>
  );
}
```

## 📚 Available Components

All 18 components wrapped:

| Component | Description |
|-----------|-------------|
| `TyButton` | Semantic button with multiple flavors |
| `TyInput` | Form input with validation |
| `TyTextarea` | Multi-line text input |
| `TyCheckbox` | Checkbox with custom styling |
| `TyDropdown` | Dropdown selection |
| `TyOption` | Dropdown option |
| `TyMultiselect` | Multi-value selection |
| `TyTag` | Tag/badge component |
| `TyModal` | Modal dialogs |
| `TyPopup` | Smart popup positioning |
| `TyTooltip` | Tooltip component |
| `TyIcon` | Icon component (3000+ icons) |
| `TyCalendar` | Full calendar |
| `TyCalendarMonth` | Month view |
| `TyCalendarNavigation` | Calendar navigation |
| `TyDatePicker` | Date picker with calendar |
| `TyTabs` | Tab container |
| `TyTab` | Individual tab |
| `TySurface` | Surface wrapper (`elevated` / `floating` / `canvas` / `content` / `input`) |
| `TyElevated` / `TyFloating` / `TyCanvas` / `TyContent` | Named surface aliases |

## ⚡ Event Handling (React Convention)

Ty React wrappers follow React conventions for event handling:

### Input Components (Input, Textarea, Checkbox)

```tsx
import { TyInput } from '@gersak/ty-react';

function SearchBox() {
  const [query, setQuery] = useState('');
  
  return (
    <TyInput
      value={query}
      // ✅ onChange fires on every keystroke (React convention)
      onChange={(e) => setQuery(e.detail.value)}
      
      // ✅ onChangeCommit fires on blur (optional - for validation)
      onChangeCommit={(e) => console.log('Final value:', e.detail.value)}
    />
  );
}
```

**Key Points:**
- `onChange` → Fires on **every keystroke** (matches React's `<input onChange>`)
- `onChangeCommit` → Fires on **blur** if value changed (optional)
- This differs from native DOM where `onchange` fires on blur

### Selection Components (Dropdown, Calendar, etc.)

```tsx
import { TyDropdown, TyOption } from '@gersak/ty-react';

function CountrySelector() {
  return (
    <TyDropdown 
      // onChange fires when selection changes
      onChange={(e) => console.log('Selected:', e.detail.value)}
    >
      <TyOption value="us">United States</TyOption>
      <TyOption value="ca">Canada</TyOption>
    </TyDropdown>
  );
}
```

For selection components, `onChange` fires when the selection changes (as expected).

## 💡 Usage Examples

### Form with Validation

```tsx
import { TyInput, TyDropdown, TyOption, TyButton } from '@gersak/ty-react';

function ContactForm() {
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget as HTMLFormElement);
    console.log(Object.fromEntries(formData));
  };

  return (
    <form onSubmit={handleSubmit}>
      <TyInput 
        name="email"
        type="email" 
        required 
        placeholder="your@email.com"
      />
      
      <TyDropdown name="role" required>
        <TyOption value="admin">Administrator</TyOption>
        <TyOption value="user">User</TyOption>
      </TyDropdown>
      
      <TyButton type="submit" flavor="primary">
        Submit
      </TyButton>
    </form>
  );
}
```

### Modal with Imperative API

```tsx
import { useRef } from 'react';
import { TyModal, TyButton, type TyModalRef } from '@gersak/ty-react';

function ModalExample() {
  const modalRef = useRef<TyModalRef>(null);

  return (
    <>
      <TyButton onClick={() => modalRef.current?.show()}>
        Open Modal
      </TyButton>
      
      <TyModal 
        ref={modalRef}
        onClose={(e) => console.log('Closed:', e.detail.reason)}
      >
        <h2>Modal Content</h2>
        <TyButton onClick={() => modalRef.current?.hide()}>
          Close
        </TyButton>
      </TyModal>
    </>
  );
}
```

### Calendar with Custom Rendering

```tsx
import { useState } from 'react';
import { TyCalendar } from '@gersak/ty-react';

function CalendarExample() {
  const [selected, setSelected] = useState<Date | null>(null);

  return (
    <TyCalendar
      value={selected?.getTime()}
      onChange={(e) => setSelected(new Date(e.detail.date))}
      min="2024-01-01"
      max="2024-12-31"
    />
  );
}
```

## 🪟 Surfaces

The Ty design system has five surface levels (background + shadow). They ship as CSS
utility classes (`ty-elevated`, `ty-floating`, …) and as React wrappers so you don't have
to remember the class names:

```tsx
import { TyElevated, TyFloating, TySurface } from '@gersak/ty-react';

function Dashboard() {
  return (
    <TyElevated as="section" className="p-6 rounded-lg">
      <h2>Card title</h2>
      <p>Sits above the page background.</p>
    </TyElevated>
  );
}

// Equivalent — pick the API that fits your code:
<TySurface variant="floating" className="p-4">…</TySurface>
<div className="ty-floating p-4">…</div>
```

| Variant     | Use for                              |
|-------------|--------------------------------------|
| `floating`  | modals, dropdowns, tooltips          |
| `elevated`  | cards, panels, sidebars              |
| `content`   | main content areas                   |
| `canvas`    | app/page background                  |
| `input`     | form control surface                 |

**Golden rule:** use Ty surface/colour classes for colour, Tailwind (or your own utilities)
for everything else (spacing, layout, typography). Don't mix `bg-blue-500` with surfaces.

## 🎨 Icons (3000+ Available)

Register icons via CDN before use:

```html
<!-- In index.html -->
<script type="module">
  import { check, heart, star } from 'https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/icons/lucide.js';
  
  window.ty.icons.register({ check, heart, star });
</script>
```

Then use in React:

```tsx
import { TyIcon, TyButton } from '@gersak/ty-react';

function IconExample() {
  return (
    <>
      <TyIcon name="check" size="lg" />
      
      <TyButton flavor="primary">
        <TyIcon name="heart" />
        Like
      </TyButton>
    </>
  );
}
```

## 📘 TypeScript Support

Full TypeScript definitions included:

```tsx
import type { 
  TyButtonProps,
  TyInputEventDetail,
  TyModalRef,
  TyDropdownEventDetail 
} from '@gersak/ty-react';

// Type-safe props
const buttonProps: TyButtonProps = {
  flavor: 'primary',  // ✅ Autocomplete
  size: 'lg',         // ✅ Type-checked
  disabled: false
};

// Type-safe event handlers
const handleChange = (e: CustomEvent<TyInputEventDetail>) => {
  console.log(e.detail.value);      // ✅ Typed
  console.log(e.detail.rawValue);   // ✅ Typed
};
```

## 🌐 ClojureScript Support

Works perfectly with Reagent and UIx:

### Reagent

```clojure
(ns my-app.core
  (:require ["@gersak/ty-react" :as ty]))

(defn my-component []
  [:div
   [:> ty/TyButton {:flavor "primary"} "Click Me"]
   [:> ty/TyInput {:placeholder "Enter text..."}]])
```

### UIx

```clojure
(ns my-app.core
  (:require [uix.core :as uix]
            ["@gersak/ty-react" :refer [TyButton TyInput]]))

(defn my-component []
  (uix/view
    [:<>
     [TyButton {:flavor "primary"} "Click Me"]
     [TyInput {:placeholder "Enter text..."}]]))
```

## 🔧 Peer Dependencies

Only React is required:
- `react` >=18.0.0
- `react-dom` >=18.0.0

**Note:** The core `@gersak/ty` web components are loaded via CDN, not as an npm dependency!

## 🌍 Browser Support

Modern browsers with Web Components support:
- Chrome 67+
- Firefox 63+
- Safari 13.1+
- Edge 79+

## 📝 Why CDN Distribution?

**Ty web components are framework-agnostic** and designed for CDN distribution:

✅ **Zero version conflicts** - One version serves all frameworks  
✅ **Smaller bundles** - Web components load once, shared across all React components  
✅ **No build complexity** - Just script tags  
✅ **Better caching** - CDN edge network  
✅ **Framework freedom** - Same components work in React, Vue, Svelte, vanilla JS

## 🤝 Contributing

This package is part of the [Ty monorepo](https://github.com/gersak/ty).

## 📄 License

MIT License - see [LICENSE](./LICENSE)

## 🔗 Links

- [Ty Core Package](https://www.npmjs.com/package/@gersak/ty)
- [GitHub Repository](https://github.com/gersak/ty)
- [Documentation](https://ty.gersak.dev) (Coming Soon)