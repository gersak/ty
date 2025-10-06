# Ty TypeScript Components

**Modern, zero-dependency web components written in TypeScript**

This directory contains the TypeScript implementation of Ty components. While the original ClojureScript components remain available in the `clj/` directory, the TypeScript version offers better developer experience, smaller bundle sizes, and easier contribution.

## 🎯 Why TypeScript?

- **Type Safety**: Full TypeScript types for better IDE support and fewer runtime errors
- **Smaller Bundles**: ~5-10x smaller than ClojureScript output
- **Better DX**: Familiar syntax for most web developers
- **Easy Debugging**: Source maps work perfectly
- **Simple Contribution**: No ClojureScript knowledge required

## 📁 Structure

```
ts/
├── components/       # Web component implementations
│   └── button.ts     # TyButton component
├── styles/          # Component styles (CSS-in-JS)
│   └── button.ts    # Button styles
├── types/           # TypeScript type definitions
│   └── common.ts    # Shared types
├── utils/           # Shared utilities
│   └── styles.ts    # CSS loading helpers
└── index.ts         # Main entry point
```

## 🚀 Quick Start

### Development

```bash
# Watch mode - rebuilds on changes
npm run dev:ts

# One-time build
npm run build:ts
```

### Using the Components

```typescript
import { TyButton } from '@gersak/ty'

// TypeScript knows all the properties!
const button = document.createElement('ty-button') as TyButton
button.flavor = 'primary'
button.size = 'lg'
button.disabled = false
```

Or in HTML:

```html
<ty-button flavor="primary" size="lg">
  Click Me
</ty-button>
```

## 🎨 Components

### TyButton

Full-featured button component with semantic flavors and size variants.

**Properties:**

```typescript
interface TyButtonElement {
  flavor?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'neutral'
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl'
  disabled?: boolean
  pill?: boolean      // Fully rounded
  outlined?: boolean  // Outlined appearance
  filled?: boolean    // Filled appearance
  accent?: boolean    // Accent appearance (default)
  plain?: boolean     // Plain appearance
  type?: 'button' | 'submit' | 'reset'
}
```

**Examples:**

```html
<!-- Different flavors -->
<ty-button flavor="primary">Primary</ty-button>
<ty-button flavor="danger">Danger</ty-button>
<ty-button flavor="success">Success</ty-button>

<!-- Different sizes -->
<ty-button size="xs">Extra Small</ty-button>
<ty-button size="lg">Large</ty-button>

<!-- Different appearances -->
<ty-button flavor="primary" outlined>Outlined</ty-button>
<ty-button flavor="primary" filled>Filled</ty-button>
<ty-button flavor="primary" plain>Plain</ty-button>

<!-- With slots -->
<ty-button flavor="primary">
  <span slot="start">⭐</span>
  With Icon
</ty-button>
```

## 🛠️ Adding New Components

1. Create component file in `ts/components/`:

```typescript
// ts/components/your-component.ts
export class TyYourComponent extends HTMLElement {
  // Your implementation
}

customElements.define('ty-your-component', TyYourComponent)
```

2. Create styles in `ts/styles/`:

```typescript
// ts/styles/your-component.ts
export const yourComponentStyles = `
  /* Your CSS */
`
```

3. Add types to `ts/types/common.ts`

4. Export from `ts/index.ts`:

```typescript
export { TyYourComponent } from './components/your-component'
```

5. Update `scripts/bundle.js` to include the new component

## 📦 Build Output

```
dist-ts/
├── index.js          # Main bundle
├── index.d.ts        # Type definitions
├── components/
│   ├── button.js     # Individual component bundle
│   └── button.d.ts   # Component types
└── *.map             # Source maps
```

## 🔄 ClojureScript vs TypeScript

Both versions are maintained:

**ClojureScript** (`clj/`):
- Original implementation
- Used for documentation site
- Mature and stable

**TypeScript** (`ts/`):
- New implementation
- Preferred for library users
- Smaller bundles
- Better DX

## 🤝 Contributing

1. Make changes in `ts/` directory
2. Run `npm run build:ts` to test
3. Ensure types are correct
4. Add tests (coming soon)
5. Submit PR

## 📝 TypeScript Configuration

See `tsconfig.json` for full configuration. Key settings:

- Target: ES2020
- Module: ESNext
- Strict mode enabled
- Declaration files generated
- Source maps included

## 🎯 Bundle Size Comparison

| Version | Size (minified) |
|---------|----------------|
| ClojureScript | ~300-400 KB |
| TypeScript | ~30-50 KB |

**10x smaller!** 🎉

## 📚 Resources

- [Web Components MDN](https://developer.mozilla.org/en-US/docs/Web/Web_Components)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [Custom Elements Spec](https://html.spec.whatwg.org/multipage/custom-elements.html)

---

**Happy coding!** 🚀
