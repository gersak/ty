# @gersak/ty-react

React wrappers for [Ty Web Components](https://github.com/gersak/ty) - bringing ClojureScript web components to React with full TypeScript support.

## Features

✅ **Complete TypeScript Support** - Full type safety for all components and events  
✅ **Zero Bundle Overhead** - Only ~2KB of wrapper code  
✅ **React 18+ Compatible** - Works with Strict Mode and Concurrent Features  
✅ **Event System Integration** - Native React event handlers with typed custom events  
✅ **Ref Forwarding** - Direct access to underlying web component elements  
✅ **Imperative Methods** - useImperativeHandle for components requiring programmatic control

## Installation

### Option 1: npm + npm (Recommended)
```bash
npm install @gersak/ty @gersak/ty-react
```

### Option 2: CDN + npm (Optimal Bundle Size)
```html
<!-- Load Ty components via CDN -->
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/css/ty.css">
```

```bash
# Only install React wrappers
npm install @gersak/ty-react
```

## Quick Start

### Option 1: With npm packages
```tsx
import React from 'react';
import '@gersak/ty';  // Required: registers web components
import '@gersak/ty/css';  // Required: loads styles
import { TyButton, TyInput, TyModal } from '@gersak/ty-react';

function App() {
  const [inputValue, setInputValue] = useState('');

  return (
    <div>
      <TyButton flavor="primary" onClick={() => alert('Hello!')}>
        Click me
      </TyButton>
      
      <TyInput 
        type="email"
        placeholder="Enter email..."
        value={inputValue}
        onChange={(e) => setInputValue(e.detail.value)}
      />
    </div>
  );
}
```

### Option 2: With CDN (smaller bundle)
```tsx
import React from 'react';
// No imports needed - components loaded via CDN
import { TyButton, TyInput } from '@gersak/ty-react';

function App() {
  return (
    <TyButton flavor="success">Ready to go!</TyButton>
  );
}
```

## Available Components

| Component | Description | Key Features |
|-----------|-------------|--------------|
| `TyButton` | Button component | Multiple flavors, sizes, event handling |
| `TyInput` | Form input | Type validation, formatting, custom events |
| `TyModal` | Modal dialogs | Imperative methods, backdrop control |
| `TyDropdown` | Dropdown selection | Options or children, search support |
| `TyIcon` | Icon component | 60+ icons, animations, size variants |
| `TyTooltip` | Tooltip component | Rich content, placement options |
| `TyMultiselect` | Multi-selection | Tag-based selection, form integration |
| `TyCalendar` | Calendar component | Date selection, custom rendering |
| `TyDatePicker` | Date picker | Form integration, validation |
| `TyTag` | Tag component | Dismissible, clickable, flavors |
| `TyPopup` | Popup positioning | Smart placement, triggers |

## Usage Examples

### Form Integration
```tsx
import { TyInput, TyDropdown, TyOption, TyButton } from '@gersak/ty-react';

function ContactForm() {
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);
    console.log(Object.fromEntries(formData));
  };

  return (
    <form onSubmit={handleSubmit}>
      <TyInput 
        name="email"
        type="email" 
        required 
        placeholder="Email address"
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

### Modal with Imperative Control
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
        <div style={{ padding: '2rem' }}>
          <h2>Modal Content</h2>
          <TyButton onClick={() => modalRef.current?.hide()}>
            Close
          </TyButton>
        </div>
      </TyModal>
    </>
  );
}
```

### State Management
```tsx
import { useState } from 'react';
import { TyMultiselect, TyTag } from '@gersak/ty-react';

function StateExample() {
  const [selected, setSelected] = useState<string[]>(['react']);

  return (
    <TyMultiselect
      value={selected}
      onChange={(e) => setSelected(e.detail.values)}
      placeholder="Select technologies..."
    >
      <TyTag value="react">React</TyTag>
      <TyTag value="vue">Vue</TyTag>
      <TyTag value="angular">Angular</TyTag>
      <TyTag value="svelte">Svelte</TyTag>
    </TyMultiselect>
  );
}
```

### Custom Event Handling
```tsx
import { TyInput, type TyInputEventDetail } from '@gersak/ty-react';

function EventExample() {
  const handleInputChange = (e: CustomEvent<TyInputEventDetail>) => {
    console.log('Raw value:', e.detail.rawValue);
    console.log('Processed value:', e.detail.value);
    console.log('Formatted value:', e.detail.formattedValue);
  };

  return (
    <TyInput
      type="number"
      currency="USD"
      onInput={handleInputChange}
      onChange={handleInputChange}
      onFocus={() => console.log('Focused')}
      onBlur={() => console.log('Blurred')}
    />
  );
}
```

## TypeScript Support

All components come with complete TypeScript definitions:

```tsx
import type { 
  TyButtonProps,
  TyInputEventDetail,
  TyModalRef,
  TyDropdownEventDetail 
} from '@gersak/ty-react';

// Type-safe props
const buttonProps: TyButtonProps = {
  flavor: 'primary',  // Autocompleted!
  size: 'lg',         // Type-checked!
  disabled: false
};

// Type-safe event handlers
const handleDropdownChange = (e: CustomEvent<TyDropdownEventDetail>) => {
  const selectedOption = e.detail.option;  // HTMLElement
  const value = selectedOption.getAttribute('value');
};
```

## Tree Shaking

Import only the components you need:

```tsx
// Import specific components
import { TyButton } from '@gersak/ty-react';

// Or import all (still tree-shakeable)
import { TyButton, TyInput, TyModal } from '@gersak/ty-react';
```

## Peer Dependencies

This package requires:
- `@gersak/ty` ^0.1.0 - The core web components
- `react` >=18.0.0 - React library  
- `react-dom` >=18.0.0 - React DOM

## Browser Support

Supports all modern browsers with Web Components support:
- Chrome 67+
- Firefox 63+  
- Safari 13.1+
- Edge 79+

## Contributing

This package is part of the [Ty monorepo](https://github.com/gersak/ty). See the main repository for contribution guidelines.

## License

MIT License - see [LICENSE](./LICENSE) file for details.

## Links

- [Main Ty Repository](https://github.com/gersak/ty)
- [Ty Documentation](https://github.com/gersak/ty#readme)  
- [Demo Application](https://github.com/gersak/ty/tree/main/react/src/App.tsx)
- [npm Package](https://www.npmjs.com/package/@gersak/ty-react)
