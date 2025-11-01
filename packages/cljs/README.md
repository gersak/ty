# dev.gersak/ty

**ClojureScript infrastructure for Ty web components.**

## 📦 What's in This Package

This package provides **ClojureScript-specific infrastructure** for building sophisticated web applications. All UI components are now in the TypeScript package (`@gersak/ty` on NPM).

### ClojureScript Infrastructure (Core Features)

- **Router** - Tree-based routing with dynamic vars, authorization, and query parameters
- **i18n** - Protocol-based internationalization with Intl API integration
- **Context** - Dynamic binding system for container-aware responsive layouts
- **Icon Generation** - Build-time SVG processing and optimization tooling
- **Documentation Site** - The live documentation site at [gersak.github.io/ty](https://gersak.github.io/ty)

### UI Components

**All UI components have been migrated to TypeScript** and are available via `@gersak/ty` NPM package:

```clojure
;; Use TypeScript components from NPM\/CDN
;; They work perfectly with Reagent\/UIx!
[:ty-button {:flavor "primary"} "Click Me"]
[:ty-calendar {:value 1735084800000}]  ;; 2024-12-25
[:ty-dropdown {:placeholder "Select..."} 
  [:ty-option {:value "1"} "Option 1"]]
```

---

## 🚀 Installation

### For ClojureScript Projects

```clojure
;; deps.edn
{:deps {dev.gersak/ty {:mvn/version "LATEST"}}}
```

### For TypeScript/JavaScript Projects

Use the NPM package instead:

```bash
npm install @gersak/ty
```

See [@gersak/ty on NPM](https://www.npmjs.com/package/@gersak/ty) for TypeScript documentation.

---

## 🎯 Usage

### Router

Tree-based routing with authorization and dynamic vars:

```clojure
(ns my-app.core
  (:require [ty.router :as router]))

(def routes
  [:root
   [:home]
   [:about]
   [:users
    [:user-detail {:path-params [:id]}]]
   [:admin 
    {:auth :admin}  ;; Requires authorization
    [:dashboard]]])

(router/init! routes)

;; Navigate
(router/navigate! [:user-detail {:id "123"}])

;; Get current route
@router/*route*  ;; => {:path [:user-detail] :params {:id "123"}}
```

### i18n

Protocol-based translations with Intl API integration:

```clojure
(ns my-app.i18n
  (:require [ty.i18n :as i18n]))

(defrecord Translations []
  i18n/ITranslate
  (translate [this k locale]
    (get-in translations [locale k])))

(def translations
  {:en {:welcome "Welcome"
        :goodbye "Goodbye"}
   :hr {:welcome "Dobrodošli"
        :goodbye "Doviđenja"}})

;; Use translations
(i18n/t :welcome)  ;; => "Welcome" (based on current locale)

;; Format dates, numbers, etc. with Intl API
(i18n/format-date (js/Date.) {:locale "hr"})
```

### Context

Dynamic binding system for container-aware responsive design:

```clojure
(ns my-app.layout
  (:require [ty.context :as ctx]))

;; Set context based on container size
(ctx/with-layout {:breakpoint :mobile
                  :container-width 375}
  (render-component))

;; Components can read context
(defn responsive-component []
  (let [bp @ctx/*breakpoint*]
    (if (= bp :mobile)
      [:div.mobile-view "Mobile"]
      [:div.desktop-view "Desktop"])))
```

---

## 🏗️ Project Structure

```
packages/cljs/
├── src/               # ClojureScript infrastructure source
│   ├── ty/
│   │   ├── router.cljs      # Routing system
│   │   ├── i18n.cljs        # Internationalization
│   │   ├── context.cljs     # Context management
│   │   └── date.cljs        # Date utilities
├── site/              # Documentation site source
│   └── ty/site/
├── icons/             # Icon generation scripts
├── gen/               # Generated icon definitions
├── build.clj          # Build script
├── shadow-cljs.edn    # Shadow-cljs configuration
├── deps.edn           # Clojure dependencies
└── package.json       # NPM dependencies
```

---

## 🛠️ Development

### Watch Mode (Documentation Site)

```bash
npm run dev
# Opens: http://localhost:8000
# Features: Live reload, full documentation site
```

### Build Documentation Site

```bash
cd packages/cljs
bb github-pages
# Builds to ../../docs/ for GitHub Pages
```

### Build ClojureScript Library

```bash
# Build JAR for Clojars
bb build-ty

# Install locally
bb install-ty

# Deploy to Clojars
bb deploy-ty
```

---

## 🎨 Using TypeScript Components in ClojureScript

The TypeScript components work seamlessly with Reagent and UIx:

### Reagent

```clojure
(ns my-app.core
  (:require [reagent.core :as r]))

(defn calendar-component []
  (let [selected (r/atom nil)]
    (fn []
      [:div
       [:ty-calendar 
        {:value @selected
         :on-change #(reset! selected (.-date (.-detail %)))}]
       [:p "Selected: " @selected]])))
```

### UIx

```clojure
(ns my-app.core
  (:require [uix.core :as uix]))

(defn calendar-component []
  (let [[selected set-selected] (uix/use-state nil)]
    [:div
     [:ty-calendar 
      {:value selected
       :on-change #(set-selected (.-date (.-detail %)))}]
     [:p "Selected: " selected]]))
```

**Note**: Make sure to load `@gersak/ty` CSS and JS via CDN or NPM:

```html
<!-- In your HTML -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css">
<script type="module" src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/ty.js"></script>
```

Or in your build:

```clojure
;; shadow-cljs.edn
{:builds
 {:app
  {:target :browser
   ;; ...
   :js-options {:resolve {"@gersak/ty" {:target :npm
                                        :require "@gersak/ty"}}}}}
```

---

## 📊 Bundle Size Impact

**For ClojureScript projects using Ty components:**
- TypeScript components: ~40KB (loaded separately)
- ClojureScript infrastructure: ~50KB additional
- **Total overhead**: ~90KB for full stack
- **Benefit**: Shared ClojureScript runtime means infrastructure has minimal impact

**For TypeScript/JavaScript projects:**
- Just use `@gersak/ty` NPM package (~40KB)
- No need for this package

---

## 🔄 Migration from Old ClojureScript Components

All UI components have been **fully migrated to TypeScript**. The ClojureScript component implementations are no longer maintained.

**Before (Old ClojureScript):**
```clojure
(ns my-app.core
  (:require [ty.components.button :refer [ty-button]]))

[ty-button {:flavor "primary"} "Click"]
```

**After (TypeScript Components via Web Components):**
```clojure
(ns my-app.core)

;; Just use the custom elements directly!
[:ty-button {:flavor "primary"} "Click"]
```

**What Changed:**
- ✅ UI components are now web components (TypeScript)
- ✅ Load via CDN or NPM (`@gersak/ty`)
- ✅ Use as custom elements in Reagent/UIx
- ✅ Same API, better performance, smaller bundles

**What Stayed in ClojureScript:**
- ✅ Router (tree-based routing with authorization)
- ✅ i18n (protocol-based translations)
- ✅ Context (dynamic binding system)
- ✅ Documentation site
- ✅ Icon generation tooling

---

## 🌟 Why This Architecture?

**TypeScript Components:**
- Universal compatibility (React, Vue, vanilla JS, HTMX)
- Smaller bundles (~40KB vs ~300KB CLJS)
- Better DX for most developers
- Full type definitions

**ClojureScript Infrastructure:**
- Powerful abstractions (router, i18n, context)
- Battle-tested code
- Minimal overhead for CLJS projects
- Advanced features for sophisticated applications

**Best of both worlds!**

---

## 📚 Resources

- **TypeScript Components**: [@gersak/ty on NPM](https://www.npmjs.com/package/@gersak/ty)
- **Documentation**: [gersak.github.io/ty](https://gersak.github.io/ty)
- **GitHub**: [github.com/gersak/ty](https://github.com/gersak/ty)
- **Examples**: See `/examples` directory in the repository

---

## 📝 License

MIT
