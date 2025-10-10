# @gersak/ty-cljs

ClojureScript components and ClojureScript-specific features for Ty.

## What's in This Package

### ClojureScript-Only Features (Permanent)
- **Router** - Tree-based routing with dynamic vars
- **i18n** - Protocol-based internationalization with Intl API integration
- **Context** - Dynamic binding system for container-aware layouts
- **Icon Generation** - Build-time SVG processing and optimization

### Components (Being Migrated to TypeScript)
- **Calendar System** - ty-calendar, ty-calendar-month, ty-date-picker
- **Multiselect** - ty-multiselect with tag management
- **Dropdown** - ty-dropdown (legacy, use TypeScript version if possible)

## Installation

```bash
npm install @gersak/ty-cljs
```

## Usage

### Calendar Components

```javascript
import '@gersak/ty-cljs/calendar';

// Now use in HTML
<ty-calendar></ty-calendar>
<ty-date-picker></ty-date-picker>
```

### In ClojureScript

```clojure
;; Router
(ns my-app.core
  (:require [ty.router :as router]))

(def routes
  [:root
   [:home]
   [:about]
   [:users
    [:user-detail {:path-params [:id]}]]])

(router/init! routes)

;; i18n
(ns my-app.i18n
  (:require [ty.i18n :as i18n]))

(defrecord Translations []
  i18n/ITranslate
  (translate [this k locale]
    (get-in translations [locale k])))

;; Context
(ns my-app.layout
  (:require [ty.context :as ctx]))

(ctx/with-layout {:breakpoint :mobile}
  (render-component))
```

## Building

```bash
# Development watch mode
npm run dev

# Production build
npm run build

# Clean build artifacts
npm run clean
```

## Project Structure

```
packages/cljs/
├── src/clj/          # ClojureScript source
├── gen/              # Icon generation scripts
├── icons/            # Generated icon definitions
├── dist/             # Build output
├── shadow-cljs.edn   # Build configuration
├── deps.edn          # Clojure dependencies
└── package.json
```

## Migration Status

Most UI components are being migrated to TypeScript for universal compatibility.
See [MIGRATION_STATUS.md](../../docs/MIGRATION_STATUS.md) for details.

**Use TypeScript versions when available:**
- Button, Modal, Input → Use `@gersak/ty-core`
- Dropdown, Checkbox, Textarea → Use `@gersak/ty-core`
- Popup, Tooltip, Tag, Icon → Use `@gersak/ty-core`

**Use ClojureScript versions for:**
- Calendar components (until migrated)
- Multiselect (until migrated)
- Router, i18n, context (staying in CLJS)

## License

MIT
