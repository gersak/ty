# Changelog

All notable changes to the Ty web components library will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.2.0] - 2024-01-XX (Current Release)

### 🎉 Major Release - TypeScript Migration Complete

This is a **major milestone release** that represents a complete rewrite of the Ty component library in TypeScript while maintaining the powerful ClojureScript infrastructure for advanced features.

---

## ✨ Added

### TypeScript Components (NEW!)

- **Complete TypeScript port** of all 19 web components to `packages/core/src/components/`
  - `button.ts` - Button component with flavor and size variants
  - `calendar.ts` - Full calendar orchestration with year/month/day navigation
  - `calendar-month.ts` - Month view component with custom day rendering
  - `calendar-navigation.ts` - Calendar navigation controls
  - `checkbox.ts` - Checkbox form control
  - `copy.ts` - Copy-to-clipboard component
  - `date-picker.ts` - Date picker with calendar integration
  - `dropdown.ts` - Desktop and mobile dropdown with search
  - `icon.ts` - Icon component with registry support
  - `input.ts` - Text input with formatting and validation
  - `modal.ts` - Modal dialog with backdrop and focus trapping
  - `multiselect.ts` - Multi-selection dropdown with tags
  - `option.ts` - Option component for dropdowns
  - `popup.ts` - Popup positioning component
  - `tab.ts` - Individual tab component (NEW in this release!)
  - `tabs.ts` - Tab container component (NEW in this release!)
  - `tag.ts` - Tag/badge component for multiselect
  - `textarea.ts` - Multi-line text input
  - `tooltip.ts` - Tooltip component

### TyComponent Base Class Architecture

- **New `TyComponent` base class** (`packages/core/src/base/ty-component.ts`)
  - Unified property/attribute lifecycle management
  - Declarative property configuration
  - Automatic type coercion (string, boolean, number, object, array)
  - Property validation and custom coercion functions
  - Property aliases support (e.g., `not-searchable`, `not-clearable`)
  - Smart rendering - only triggers when visual properties change
  - Built-in ElementInternals support for form association
  - Framework compatibility (React, Vue, Reagent property capture)

- **PropertyManager** utility (`packages/core/src/utils/property-manager.ts`)
  - Centralized property storage and lifecycle
  - Type-safe property access
  - Change tracking and validation
  - Event emission for property changes

### Icon Registry System

- **Icon registry** (`packages/core/src/utils/icon-registry.ts`)
  - Centralized icon storage with Map-based architecture
  - Support for custom SVG icon registration
  - Tree-shakeable icon imports
  - Global `window.ty` API for easy integration
  - Icon watcher system for dynamic updates

- **window.ty API** for script tag usage:
  ```javascript
  window.ty.icons.register({ iconName: '<svg>...</svg>' })
  window.ty.icons.get('iconName')
  window.ty.icons.has('iconName')
  window.ty.icons.list()
  window.ty.version // '0.2.0'
  ```

### Build System & Tooling

- **Vite configuration** improvements
  - `vite.config.ts` - Main build configuration
  - `vite.config.dev.ts` - Development server (port 3000)
  - `vite.config.cdn.ts` - CDN build optimization
  - HMR (Hot Module Reload) for development
  - Source maps for debugging
  - Terser minification for production

### Documentation & Examples

- **Updated React example** (`examples/react-nextjs/`)
  - Icon registration pattern with new icon system
  - TypeScript integration examples
  - Dashboard example page

- **New icons example page** showing icon registration patterns

- **Updated Reagent example** (`examples/reagent/`)
  - Integration with TypeScript components
  - ClojureScript wrapper usage patterns

- **Updated HTMX-Flask example** with new icon loading

### ClojureScript Components Package

- **New `packages/cljs/` structure** with organized component wrappers
  - Separated ClojureScript wrappers from TypeScript implementation
  - Build configuration for ClojureScript package
  - Component-specific styling organization

---

## 🔄 Changed

### Architecture Changes

- **Migrated from ClojureScript to TypeScript** for all UI components
  - Maintained ClojureScript infrastructure for routing, i18n, and site
  - Components now in `packages/core/src/` instead of `lib/ty/components/`
  - TypeScript provides better type safety and broader ecosystem compatibility

- **Dropdown component** major improvements
  - Implemented on top of TyComponent base class
  - Better separation of desktop and mobile implementations
  - Improved search functionality
  - Enhanced keyboard navigation
  - Better styling and visual feedback

- **Multiselect component** refactored
  - Adjusted to TyComponent implementation
  - Improved tag rendering and management
  - Better mobile experience
  - Enhanced clear functionality

- **Calendar component** enhancements
  - Better state management
  - Improved locale support (130+ languages)
  - Enhanced navigation controls
  - Better date formatting

### Mobile Experience

- **Improved mobile implementations** across components
  - Better dropdown mobile menu UX
  - Modal improvements for mobile viewports
  - Touch-friendly interactions
  - Responsive layout adjustments

### Styling System

- **Dropdown styling** improvements
  - Better visual hierarchy
  - Improved focus states
  - Enhanced hover effects
  - Consistent with design system

- **Tabs component styling** (`TABS_STYLING.md` documentation)
  - Comprehensive styling guide for tabs
  - CSS customization patterns

### Documentation Site

- **Site package updates** for compatibility with TypeScript components
  - Fixed integration issues
  - Updated component demos
  - Improved code examples
  - Better documentation structure

### Build & Distribution

- **NPM package structure** optimized
  - Better tree-shaking support
  - Smaller bundle sizes
  - Clearer entry points
  - Improved TypeScript definitions

---

## 🐛 Fixed

- **Highlight rendering** in dropdown components
- **Replicant integration** issues with site
- **Icon loading errors** in various contexts
- **Mobile menu** display issues in dropdown
- **Only odd tabs rendering** bug (weird edge case!)
- **Form integration** improvements for all form components
- **Keyboard navigation** edge cases in dropdowns
- **Focus management** in modal and popup components
- **Clear button** functionality in searchable dropdowns

---

## 💥 Breaking Changes

### Import Paths Changed

**Before (v0.1.x - ClojureScript)**:
```clojure
(ns my-app
  (:require [ty.components.button :as button]))
```

**After (v0.2.0 - TypeScript)**:
```typescript
import { TyButton } from '@gersak/ty'
// or
import '@gersak/ty/css/ty.css'
```

### Icon System Changed

**Before**: Icons were built-in and automatically available

**After**: Icons must be registered explicitly:
```typescript
import { check, heart } from '@gersak/ty/icons/lucide'
import { registerIcons } from '@gersak/ty/icons/registry'

registerIcons({ check, heart })
```

**OR** using the global API:
```html
<script>
  window.ty.icons.register({
    'check': '<svg>...</svg>',
    'heart': '<svg>...</svg>'
  })
</script>
```

### Component Property Changes

Some components now use **TyComponent base class** with unified property handling:
- Properties now have declarative configuration
- Validation and coercion are automatic
- Attribute aliases (e.g., `not-searchable`) are supported
- Form association is built-in for form controls

### Package Structure

- TypeScript components moved to `packages/core/`
- ClojureScript wrappers moved to `packages/cljs/`
- Root `package.json` removed (monorepo structure)

---

## 📚 Documentation Added

- **`BUILDING_WITH_TYCOMPONENT.md`** - Comprehensive guide for building components with TyComponent base class
- **`CSS_GUIDE.md`** - Ty CSS system usage guide (TY for colors, Tailwind for everything else)
- **`TYPESCRIPT_DEV_GUIDE.md`** - Development workflow for TypeScript components
- **`PROJECT_SUMMARY.md`** - Updated with TypeScript architecture details
- **`packages/cljs/README.md`** - ClojureScript package documentation
- **`packages/cljs/components/ty/components/TABS_STYLING.md`** - Tabs styling guide

---

## 🚀 Migration Guide

### For ClojureScript Projects

If you were using v0.1.x ClojureScript components:

1. **Install the new package**:
   ```clojure
   ;; deps.edn
   {:deps {dev.gersak/ty {:mvn/version "0.2.0"}}}
   ```

2. **Components still work** via ClojureScript wrappers in `packages/cljs/`
3. **Icon registration** now required (see Icon System changes above)

### For New TypeScript Projects

1. **Install via NPM**:
   ```bash
   npm install @gersak/ty
   ```

2. **Import components**:
   ```typescript
   import '@gersak/ty/css/ty.css'
   import { TyButton, TyDropdown } from '@gersak/ty'
   ```

3. **Register icons** you need:
   ```typescript
   import { check, heart } from '@gersak/ty/icons/lucide'
   import { registerIcons } from '@gersak/ty/icons/registry'
   
   registerIcons({ check, heart })
   ```

### For React Projects

See updated example in `examples/react-nextjs/`:
- Use components directly as custom elements
- Register icons at app startup
- TypeScript definitions included for full type safety

### For Vanilla JS / HTMX

Use the global `window.ty` API:
```html
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty/dist/index.js"></script>
<script>
  window.ty.icons.register({ /* your icons */ })
</script>
```

---

## 🎯 Version 0.2.0 Goals Achieved

✅ **Complete TypeScript migration** - All 19 components ported
✅ **TyComponent architecture** - Unified, maintainable base class
✅ **Icon registry system** - Flexible, tree-shakeable, easy to use
✅ **Framework compatibility** - Works with React, Vue, Reagent, vanilla JS
✅ **Production ready** - Published to NPM as `@gersak/ty`
✅ **Improved mobile UX** - Better dropdown and modal experiences
✅ **Comprehensive docs** - Guides for building, styling, and using components
✅ **Zero runtime dependencies** - Pure web standards

---

## 🔮 Future Plans (v0.3.0+)

- Additional components (data table, file upload, progress indicators)
- Enhanced a11y (accessibility) features
- More icon library integrations
- Performance optimizations
- Additional framework adapters
- Component testing utilities

---

## 📦 Package Information

- **NPM Package**: `@gersak/ty` v0.2.0
- **Clojars Package**: `dev.gersak/ty` v0.2.0
- **License**: MIT
- **TypeScript**: ✅ Full type definitions included
- **Framework Support**: React, Vue, Reagent, HTMX, Vanilla JS

---

## 🙏 Acknowledgments

This release represents a significant architectural evolution while maintaining backward compatibility through ClojureScript wrappers. Thank you to everyone who contributed feedback and tested the alpha versions!

---

**For detailed component documentation, visit the documentation site or check the README files in each package.**
