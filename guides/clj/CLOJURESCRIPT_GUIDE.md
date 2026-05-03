# ClojureScript + Ty Guide

Use Ty from any ClojureScript app — Replicant, Reagent, UIx, Rum, or vanilla CLJS with no UI framework. This guide covers the substrate: distribution, shadow-cljs setup, raw interop with web components, and icon tree-shaking.

For framework-specific patterns, see [REPLICANT_TY_GUIDE.md](REPLICANT_TY_GUIDE.md). The infrastructure guides ([ROUTING_GUIDE.md](ROUTING_GUIDE.md), [I18N_GUIDE.md](I18N_GUIDE.md), [LAYOUT_GUIDE.md](LAYOUT_GUIDE.md)) are already framework-agnostic.

## What you get

Ty distributes through two ecosystems:

**NPM (`@gersak/ty`)** — the Web Components themselves. Same package JS/React/Vue/Svelte users install. Provides `<ty-button>`, `<ty-input>`, `<ty-dropdown>`, etc. (21 primitives). Loaded into the page via shadow-cljs npm interop or CDN script tag.

**Clojars (`dev.gersak/ty`, `dev.gersak/ty-icons`)** — CLJS-native infrastructure that complements the components:

| Namespace | Purpose |
|---|---|
| `ty.router` | Zipper-based client-side routing |
| `ty.i18n` (+ `.keyword`, `.string`, `.number`, `.time`) | Translations + `Intl` formatting |
| `ty.layout` | Container-aware responsive layout (`with-window`, `with-container`, breakpoints) |
| `ty.icons` | Register icons with `<ty-icon>` |
| `ty.shim` | Build your own Web Components in CLJS |
| `ty.positioning` | Smart popup/tooltip positioning helpers |
| `ty.value` | Form-value coercion utilities |

`dev.gersak/ty-icons` is a **separate** Clojars artifact containing icon namespaces (`ty.lucide`, `ty.heroicons.*`, `ty.material.*`, `ty.fontawesome.*`). Each icon is an individual CLJS `def` — shadow-cljs `:advanced` removes unused ones automatically.

## Setup

### 1. Add deps

`deps.edn` or `shadow-cljs.edn`:

```clojure
{:deps {dev.gersak/ty       {:mvn/version "1.0.0-RC4"}
        dev.gersak/ty-icons {:mvn/version "1.0.0-RC4"}}}
```

### 2. Install web components from NPM

```bash
npm install @gersak/ty
```

### 3. Load components in your app

Two paths — pick one:

#### Path A — shadow-cljs npm interop (recommended)

```clojure
(ns my-app.core
  (:require ["@gersak/ty"]))   ; registers all <ty-*> elements
```

Or for tree-shaking, register only what you use:

```clojure
(ns my-app.core
  (:require ["@gersak/ty/button"]
            ["@gersak/ty/input"]
            ["@gersak/ty/dropdown"]
            ["@gersak/ty/modal"]))
```

Subpaths match component names: `button`, `input`, `textarea`, `checkbox`, `dropdown`, `option`, `multiselect`, `tag`, `modal`, `popup`, `tooltip`, `tabs`, `tab`, `wizard`, `step`, `calendar`, `calendar-month`, `date-picker`, `icon`, `copy`, `resize-observer`, `scroll-container`.

Add the stylesheet too — typically a `<link>` in your HTML, or via shadow-cljs:

```clojure
(ns my-app.styles
  (:require ["@gersak/ty/css/ty.css"]))
```

#### Path B — CDN script tag

For server-rendered apps or when you don't want NPM in the loop:

```html
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css">
<script type="module" src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/ty.js"></script>
```

The CDN bundle registers all components. From CLJS, you don't need to require anything — just use `<ty-*>` tags in your hiccup or DOM.

### 4. Verify

```clojure
(.appendChild js/document.body
  (doto (js/document.createElement "ty-button")
    (set! -textContent "Hello")))
```

If a styled button renders, you're set.

## Using Web Components from CLJS

The same three rules apply across every CLJS framework:

1. **Event payload lives on `event.detail`.** In CLJS: `(.. e -detail -value)` or `(j/get-in e [:detail :value])`.
2. **Properties vs attributes.** Booleans, arrays, objects must be set as JS properties, not attributes. Strings work as either.
3. **Don't conflate CLJS data and the DOM.** A `ty-multiselect` `value` is a JS array, not a CLJS vector — convert with `(clj->js)` going in and `(js->clj)` coming out (or use `js/Array.from`).

### Vanilla CLJS (no UI framework)

```clojure
(ns my-app.core
  (:require ["@gersak/ty"]
            [ty.icons :as icons]
            [ty.lucide :as lucide]))

(defn render []
  (let [root (js/document.getElementById "app")
        input (doto (js/document.createElement "ty-input")
                (.setAttribute "label" "Email")
                (.setAttribute "type" "email"))
        button (doto (js/document.createElement "ty-button")
                 (.setAttribute "flavor" "primary")
                 (set! -textContent "Submit"))]
    (.addEventListener input "change"
      (fn [e]
        (js/console.log "email:" (.. e -detail -value))))
    (.addEventListener button "click"
      (fn [_]
        (js/console.log "value:" (.-value input))))
    (.appendChild root input)
    (.appendChild root button)))

(defn ^:export init []
  (icons/register! {:check lucide/check})
  (render))
```

### Hiccup (Replicant, Reagent, UIx, Rum)

Hiccup-based frameworks render `<ty-*>` tags directly. Each framework's idioms differ slightly — see [REPLICANT_TY_GUIDE.md](REPLICANT_TY_GUIDE.md) for one full example. The key concerns are framework-specific:

| Framework | Set property (not attribute) | Listen to event |
|---|---|---|
| Replicant | `:replicant/key` for stable identity; properties via DOM ops or `:dom/on-mount` | `[:on/change handler]` |
| Reagent | `:value-for-typeahead` etc. work as attrs; complex → use `:ref` | `:on-change` (lowercased) |
| UIx (Helix) | `$d` for DOM elements; props are camelCased | `:on-change` |
| Rum | Refs for property setting | `:on-change` |

Across all of them, `event.detail.value` access in CLJS:

```clojure
(fn [e] (.. e -detail -value))
```

## Icon registration in CLJS

`<ty-icon name="check">` is a runtime registry lookup. The compiler can't connect the string `"check"` to an icon export — you must register explicitly.

Each icon is a CLJS `def` returning an SVG string:

```clojure
(ns my-app.icons
  (:require [ty.icons :as icons]
            [ty.lucide :as lucide]
            [ty.heroicons.outline :as ho]
            [ty.fontawesome.brands :as fa-brands]))

(defn register! []
  (icons/register!
    {:check        lucide/check
     :x            lucide/x
     :plus         lucide/plus
     :search       lucide/search
     :user         ho/user-circle
     :github       fa-brands/github
     :slack        fa-brands/slack}))
```

Call `register!` at app startup, before rendering. Use the icons anywhere:

```clojure
[:ty-icon {:name "check" :size "md"}]
[:ty-button {:flavor "primary"}
 [:ty-icon {:name "plus" :slot "start"}]
 "Add Item"]
```

### Tree-shaking

Shadow-cljs `:advanced` compilation removes unused icons automatically — you only pay for what you reference. Two rules to keep this working:

1. **Use the qualified namespace** (`lucide/check`, not aliasing). Aliases preserve tree-shaking; namespace requires that pull `:as :all` do not.
2. **Don't dynamically construct icon names.** `(symbol "lucide" name)` defeats DCE — the compiler can't see what's referenced. If you need dynamic icons, register every candidate up front.

Available icon namespaces in `dev.gersak/ty-icons`:

```
ty.lucide                   1,636 icons
ty.heroicons.outline          324 icons
ty.heroicons.solid            324 icons
ty.heroicons.mini             230 icons
ty.heroicons.micro            230 icons
ty.material.filled          2,400+ icons
ty.material.outlined        2,400+ icons
ty.material.round           2,400+ icons
ty.material.sharp           2,400+ icons
ty.material.two-tone        2,400+ icons
ty.fontawesome.solid        1,400+ icons
ty.fontawesome.regular        163 icons
ty.fontawesome.brands         500+ icons
```

### Custom SVGs

```clojure
(icons/register!
  {:company-logo "<svg viewBox=\"0 0 24 24\">...</svg>"})
```

## Shadow-cljs configuration tips

### `:js-options` for npm interop

Default config works. If you see issues with `@gersak/ty` ESM imports:

```clojure
{:builds
 {:app
  {:target :browser
   :js-options {:resolve {"@gersak/ty" {:target :npm
                                         :require "@gersak/ty"}}}}}}
```

Usually unnecessary — shadow-cljs autodetects.

### CSS handling

Shadow-cljs doesn't process CSS imports from JS — `ty.css` must be loaded via a `<link>` tag in your HTML, copied into your build output, or processed by a separate CSS pipeline. The simplest path:

```html
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css">
```

Or self-host by copying `node_modules/@gersak/ty/css/ty.css` to your `resources/public/`.

### Code splitting (lazy loading)

See [CODE_SPLITTING.md](CODE_SPLITTING.md) for shadow-cljs `:modules` setup. Pattern: defer heavy components (calendar/date-picker/dropdown) until the route that needs them loads.

## What's CLJS-specific

A few things differ from JS:

- **You import via `:require ["@gersak/ty/..."]`** — shadow-cljs's npm interop. The `sideEffects` field in `package.json` works the same way.
- **No `'use client'` boundary** — shadow-cljs targets the browser by default. SSR with `:target :node` requires extra care; see existing CLJS SSR docs (out of scope here).
- **Icons can use keyword names** — `(icons/register! {:check lucide/check})` is idiomatic. Strings also work.
- **Property setting** — CLJS hiccup renderers (Reagent, Replicant, UIx) all have framework-specific quirks for setting JS properties vs HTML attributes. The component itself doesn't care; the framework's bridge does.

## See also

- [TY_GUIDE.md](../TY_GUIDE.md) — universal component API reference
- [CSS_GUIDE.md](../CSS_GUIDE.md) — design system
- [REPLICANT_TY_GUIDE.md](REPLICANT_TY_GUIDE.md) — Replicant-specific integration patterns
- [ROUTING_GUIDE.md](ROUTING_GUIDE.md) — `ty.router` (framework-agnostic)
- [I18N_GUIDE.md](I18N_GUIDE.md) — `ty.i18n` (framework-agnostic)
- [LAYOUT_GUIDE.md](LAYOUT_GUIDE.md) — `ty.layout` (framework-agnostic)
- [COMPONENT_GUIDE.md](COMPONENT_GUIDE.md) — `ty.shim` for building Web Components in CLJS
- [CODE_SPLITTING.md](CODE_SPLITTING.md) — shadow-cljs lazy loading
- [../js/JAVASCRIPT_GUIDE.md](../js/JAVASCRIPT_GUIDE.md) — JS-side substrate (bundlers, side-effects, SSR)
