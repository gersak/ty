# Ty Icons - Pre-generated Icon Components

A collection of pre-generated ClojureScript icon components for the Ty web components library. This library provides thousands of SVG icons from popular icon sets as ready-to-use ClojureScript components.

## Icon Libraries Included

- **Lucide Icons** - Modern, consistent icon set (900+ icons)
- **Heroicons** - Tailwind UI icon library (500+ icons, solid & outline variants)
- **Material Design Icons** - Google's Material Design icons (2000+ icons)
- **Fav6** - Custom icon set

## Installation

Add to your `deps.edn`:

```clojure
{:deps {dev.gersak/ty-icons {:mvn/version "0.1.0"}}}
```

**Note**: This library provides the icon components but you still need the base icon system. Either:
1. Include `dev.gersak/ty` for the full component library, OR
2. Include the minimal `ty-icons.css` stylesheet for just icon functionality

## Usage

### With Full Ty Library

If you're already using `dev.gersak/ty`, just require the icon namespaces:

```clojure
(ns my-app.core
  (:require [ty.core :as ty]
            [ty.lucide :as lucide]
            [ty.heroicons :as heroicons]))

;; Initialize Ty (includes icon system)
(ty/init)

;; Use icons in your markup
[:div
 [lucide/calendar]
 [lucide/user]
 [heroicons/solid-home]
 [heroicons/outline-user]]
```

### Standalone Usage

If you only want icons without the full Ty library:

```html
<!-- Include minimal icon CSS -->
<link rel="stylesheet" href="path/to/ty-icons.css">
```

```clojure
(ns my-app.core
  (:require [ty.lucide :as lucide]
            [ty.heroicons :as heroicons]))

;; Icons will work with the minimal CSS
[:div
 [lucide/calendar]
 [heroicons/solid-home]]
```

### Available Icon Namespaces

```clojure
;; Lucide Icons (single namespace)
[ty.lucide/calendar]
[ty.lucide/user]  
[ty.lucide/home]
[ty.lucide/search]

;; Heroicons (organized by style)
[ty.heroicons/solid-home]
[ty.heroicons/solid-user] 
[ty.heroicons/outline-home]
[ty.heroicons/outline-user]

;; Material Design Icons (organized by category)  
[ty.material/calendar-today]
[ty.material/person]
[ty.material/home]

;; Fav6 Icons
[ty.fav6/custom-icon-1]
[ty.fav6/custom-icon-2]
```

### Icon Properties

All icon components support the same properties as the base `ty-icon` component:

```clojure
;; Size variants
[lucide/calendar {:size "lg"}]      ; xs, sm, md, lg, xl, 2xl
[lucide/calendar {:size "24"}]      ; Fixed pixel sizes: 12, 14, 16, 18, 20, 24, 32, 48

;; Animations  
[lucide/calendar {:spin true}]      ; Spinning animation
[lucide/calendar {:pulse true}]     ; Pulse animation
[lucide/calendar {:tempo "slow"}]   ; Animation tempo: slow, normal, fast

;; Interaction
[lucide/calendar {:clickable true}] ; Make icon clickable with hover effects
[lucide/calendar {:disabled true}]  ; Disabled state
```

### CSS Classes

Icons automatically get the appropriate CSS classes and can be styled:

```css
/* Style all calendar icons */
ty-icon[name="calendar"] {
  color: blue;
}

/* Style icons with CSS classes */
.my-icon {
  color: red;
  font-size: 1.5em;
}
```

```clojure
[lucide/calendar {:class "my-icon"}]
```

## Icon Counts

- **Lucide**: ~900 icons
- **Heroicons**: ~500 icons (solid + outline variants)
- **Material Design**: ~2000 icons  
- **Fav6**: Custom set

## Bundle Size

The generated icon components are optimized:
- SVG paths are embedded directly in ClojureScript
- Tree-shaking eliminates unused icons
- Minimal runtime overhead
- No external SVG file dependencies

## Browser Support

Same as Ty web components:
- Chrome 67+
- Firefox 63+
- Safari 13.1+  
- Edge 79+

## Updating Icons

This library contains pre-generated components. To update or add icons, you'll need access to the main Ty repository and its icon generation tools.

## License

MIT License - see LICENSE file for details.

Icons are subject to their respective licenses:
- Lucide Icons: ISC License
- Heroicons: MIT License
- Material Design Icons: Apache License 2.0
- Fav6: Custom license

## Related Libraries

- `dev.gersak/ty` - Full Ty web components library
