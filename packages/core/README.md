# @gersak/ty

TypeScript web components for universal UI development.

## Installation

```bash
npm install @gersak/ty
```

## Usage

### Import All Components

```javascript
import '@gersak/ty'

// Or with React
import { TyButton, TyModal, TyInput } from '@gersak/ty'
```

### Import Individual Components (Tree-Shaking)

```javascript
import '@gersak/ty/button'
import '@gersak/ty/modal'
import '@gersak/ty/input'
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
    import '@gersak/ty'
    
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
- **ty-copy** - Copy-to-clipboard field for API keys, tokens, URLs
- **ty-calendar** - Full calendar with navigation and date selection
- **ty-calendar-month** - Month view component
- **ty-calendar-navigation** - Calendar navigation controls
- **ty-date-picker** - Date picker with calendar popup
- **ty-multiselect** - Multi-select dropdown with tags

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
import { TyButton, TyModal, type TyButtonElement } from '@gersak/ty'
import { registerIcons } from '@gersak/ty/icons/registry'

// Icons are in separate @gersak/ty-icons package (optional)
// npm install @gersak/ty-icons
// import { check, heart } from '@gersak/ty-icons/lucide'
// registerIcons({ check, heart })

// Or register your own SVG icons:
registerIcons({
  'my-icon': '<svg>...</svg>'
})

const button: TyButtonElement = document.querySelector('ty-button')!
button.flavor = 'primary'
button.addEventListener('ty-click', (e) => {
  console.log('Clicked:', e.detail)
})
```

### Icon System

The core package includes the icon registry utility but **not the icon libraries** themselves (to keep package size small).

**Option 1: Use the separate icon package** (coming soon):
```bash
npm install @gersak/ty-icons
```

```typescript
import { check, heart, star } from '@gersak/ty-icons/lucide'
import { registerIcons } from '@gersak/ty/icons/registry'

registerIcons({ check, heart, star })
```

**Option 2: Bring your own SVG icons**:
```typescript
import { registerIcons } from '@gersak/ty/icons/registry'

registerIcons({
  'check': '<svg xmlns="http://www.w3.org/2000/svg">...</svg>',
  'custom-logo': '<svg>...</svg>'
})
```

**Option 3: Use the global API**:
```html
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.js"></script>
<script>
  window.tyIcons.register({
    'check': '<svg>...</svg>',
    'heart': '<svg>...</svg>'
  })
  
  console.log(window.tyVersion)              // '0.2.0'
  console.log(window.tyIcons.has('check'))   // true
  console.log(window.tyIcons.list())         // ['check', 'heart']
</script>
```

**Why separate packages?**
- Core package stays lightweight (~2.6MB)
- Install icons only if you need them
- Or use your own custom SVG icons

## Browser Support

- All modern browsers (Chrome, Firefox, Safari, Edge)
- Web Components (Custom Elements, Shadow DOM)
- ES2020+

## Zero Dependencies

This package has zero runtime dependencies. Everything is built on native web standards.

## License

MIT