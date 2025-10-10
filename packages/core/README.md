# @gersak/ty-core

TypeScript web components for universal UI development.

## Installation

```bash
npm install @gersak/ty-core
```

## Usage

### Import All Components

```javascript
import '@gersak/ty-core'

// Or with React
import { TyButton, TyModal, TyInput } from '@gersak/ty-core'
```

### Import Individual Components (Tree-Shaking)

```javascript
import '@gersak/ty-core/button'
import '@gersak/ty-core/modal'
import '@gersak/ty-core/input'
```

### Use in HTML

```html
<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="path/to/ty.css">
</head>
<body class="ty-canvas">
  
  <ty-button flavor="primary">Click Me</ty-button>
  
  <ty-input placeholder="Enter text..."></ty-input>
  
  <ty-modal id="my-modal">
    <div class="ty-elevated p-6 rounded-lg">
      <h2>Modal Content</h2>
    </div>
  </ty-modal>

  <script type="module">
    import '@gersak/ty-core'
    
    const modal = document.getElementById('my-modal')
    modal.show()
  </script>
</body>
</html>
```

## Available Components

- **ty-button** - Button with multiple flavors (primary, secondary, success, danger, warning)
- **ty-modal** - Modal dialog with backdrop, ESC key, focus trap
- **ty-input** - Input field with validation, number formatting
- **ty-dropdown** - Dropdown with positioning, keyboard navigation
- **ty-checkbox** - Checkbox with form integration
- **ty-textarea** - Textarea with auto-resize
- **ty-popup** - Popup positioning system
- **ty-tooltip** - Tooltip with hover/focus triggers
- **ty-tag** - Tag with removable state
- **ty-option** - Option for dropdown/multiselect
- **ty-icon** - Icon with registry system

## Development

### Setup

```bash
cd packages/core
npm install
```

### Development Server

```bash
npm run dev
```

Opens development server at `http://localhost:3000` with hot module reloading.

### Build

```bash
npm run build
```

Builds optimized production bundles with type declarations.

### Type Checking

```bash
npm run type-check
```

### Preview Production Build

```bash
npm run preview
```

## Project Structure

```
packages/core/
├── src/
│   ├── components/       # Web component implementations
│   ├── styles/          # Component styles
│   ├── utils/           # Utility functions
│   ├── types/           # TypeScript type definitions
│   └── index.ts         # Main entry point
├── dev/
│   ├── index.html       # Development test page
│   └── ty.css          # Symlink to resources/ty.css
├── dist/                # Build output
├── vite.config.ts       # Vite configuration
├── tsconfig.json        # TypeScript configuration
└── package.json
```

## TypeScript Support

Full TypeScript support with type declarations:

```typescript
import { TyButton, TyModal, type TyButtonElement } from '@gersak/ty-core'

const button: TyButtonElement = document.querySelector('ty-button')!
button.flavor = 'primary'
button.addEventListener('ty-click', (e) => {
  console.log('Clicked:', e.detail)
})
```

## Browser Support

- All modern browsers (Chrome, Firefox, Safari, Edge)
- Web Components (Custom Elements, Shadow DOM)
- ES2020+

## Zero Dependencies

This package has zero runtime dependencies. Everything is built on native web standards.

## License

MIT
