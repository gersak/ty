# Publishing @gersak/ty-react - Source-Only Package

## 🎯 Philosophy: CDN-First Architecture

**Ty web components are distributed via CDN**, not npm. This React package contains only thin wrappers that work with CDN-loaded components.

**Benefits:**
- ✅ **Zero dependency hassles** - No `@gersak/ty` npm dependency needed
- ✅ **Independent versioning** - React wrappers can update independently
- ✅ **Simpler publishing** - Just source files, no build, no coordination
- ✅ **Framework agnostic** - Same CDN serves React, Vue, Svelte, vanilla JS

## 📋 Pre-Publish Checklist

### 1. Version Check

Current version: `0.2.0`

Bump version before publishing:
```bash
cd packages/react

# Patch: 0.2.0 -> 0.2.1
npm version patch

# Minor: 0.2.0 -> 0.3.0
npm version minor

# Major: 0.2.0 -> 1.0.0
npm version major
```

### 2. Type Check (Optional but Recommended)
```bash
cd packages/react
npm run type-check
```

This runs `tsc --noEmit` to verify TypeScript types are correct.

### 3. Verify Package Contents
```bash
cd packages/react
npm publish --dry-run
```

Expected files:
```
src/
├── components/
│   ├── TyButton.tsx
│   ├── TyCalendar.tsx
│   ├── TyCheckbox.tsx
│   ├── ... (all 18 components)
│   └── index.ts
tsconfig.json
README.md
LICENSE
package.json
```

## 🚀 Publishing

### Publish to NPM
```bash
cd packages/react

# One-time: Login to npm
npm login

# Publish as public package
npm publish --access public
```

### Test Locally First (Recommended)
```bash
cd packages/react

# Create tarball
npm pack
# Creates: gersak-ty-react-0.2.0.tgz

# Test in a React project
cd /path/to/test-project
npm install /path/to/packages/react/gersak-ty-react-0.2.0.tgz
```

## 🧪 Post-Publish Verification

### Test Installation
```bash
# Create new React project
npm create vite@latest test-ty-react -- --template react-ts
cd test-ty-react

# Install dependencies
npm install
npm install @gersak/ty-react
```

### Test Usage

Update `index.html`:
```html
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Test Ty React</title>
  
  <!-- Load Ty Web Components via CDN -->
  <script src="https://cdn.jsdelivr.net/npm/@gersak/ty@0.2.0/dist/index.js"></script>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@0.2.0/css/ty.css">
</head>
<body>
  <div id="root"></div>
  <script type="module" src="/src/main.tsx"></script>
</body>
</html>
```

Update `src/App.tsx`:
```tsx
import { TyButton, TyInput } from '@gersak/ty-react'

function App() {
  return (
    <div style={{ padding: '2rem' }}>
      <TyButton flavor="primary">Click Me</TyButton>
      <TyInput placeholder="Enter text..." />
    </div>
  )
}

export default App
```

Run the dev server:
```bash
npm run dev
```

✅ Should work without any errors!

## 📦 How Consumers Use It

### Installation
```bash
npm install @gersak/ty-react
```

### Setup (Add CDN to index.html)
```html
<script src="https://cdn.jsdelivr.net/npm/@gersak/ty@0.2.0/dist/index.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@0.2.0/css/ty.css">
```

### Usage (TypeScript/TSX)
```tsx
import { TyButton, TyCalendar, type TyButtonProps } from '@gersak/ty-react'

function MyComponent() {
  return <TyButton flavor="primary">Click Me</TyButton>
}
```

### Usage (Reagent - ClojureScript)
```clojure
;; deps.edn
{:deps {dev.gersak/ty-react {:npm/version "0.2.0"}}}

;; In index.html, add CDN script tags

;; Usage
(ns my-app.core
  (:require ["@gersak/ty-react" :as ty]))

(defn my-component []
  [:> ty/TyButton {:flavor "primary"} "Click Me"])
```

### Usage (UIx - ClojureScript)
```clojure
;; deps.edn
{:deps {dev.gersak/ty-react {:npm/version "0.2.0"}}}

;; In index.html, add CDN script tags

;; Usage
(ns my-app.core
  (:require [uix.core :as uix]
            ["@gersak/ty-react" :refer [TyButton TyInput]]))

(defn my-component []
  (uix/view
    [:<>
     [TyButton {:flavor "primary"} "Click Me"]
     [TyInput {:placeholder "Enter text..."}]]))
```

## 🔍 Package Structure

### What Gets Published
- `src/**/*` - All TypeScript source files (~75KB)
- `tsconfig.json` - TypeScript configuration
- `README.md` - Documentation
- `LICENSE` - MIT License
- `package.json` - Package metadata

**Total tarball size: ~15KB gzipped**

### What Doesn't Get Published
- `node_modules/` - Dependencies
- `dist/` - Doesn't exist (source-only!)
- `.git*` - Git files

## ⚙️ How Consumer Bundlers Handle It

Modern bundlers (Vite, Next.js, Create React App) automatically:
1. Process `.tsx` files from `node_modules`
2. Compile TypeScript to JavaScript
3. Tree-shake unused exports
4. Bundle with optimal code-splitting

**Zero configuration needed by consumers!**

## 🆚 Why No @gersak/ty Dependency?

| Aspect | With npm dep | With CDN (Current) |
|--------|-------------|-------------------|
| Install | `npm install @gersak/ty @gersak/ty-react` | `npm install @gersak/ty-react` |
| Setup | Auto-imported | Add `<script>` tag once |
| Bundle | Components in every build | ❌ Not bundled |
| Caching | Per-project | ✅ Global CDN cache |
| Version conflicts | ⚠️ Possible | ✅ Impossible |
| Framework sharing | ❌ No | ✅ Yes (React + Vue) |
| Update wrappers | Need compatible core | ✅ Independent |

## 📝 Dependencies

### Peer Dependencies (Required by Consumers)
- `react` >=18.0.0
- `react-dom` >=18.0.0

### Dev Dependencies (For Type Checking)
- `typescript` ^5.7.0
- `@types/react` ^18.2.66
- `@types/react-dom` ^18.2.22

**Note:** No `@gersak/ty` dependency! Web components load via CDN.

## ✅ Ready to Publish Checklist

- [ ] Version bumped (`npm version patch/minor/major`)
- [ ] Type check passes (`npm run type-check`)
- [ ] README.md updated with correct CDN links
- [ ] LICENSE file exists
- [ ] Logged into NPM (`npm login`)
- [ ] Dry-run looks good (`npm publish --dry-run`)
- [ ] Git committed and tagged

## 🎯 Simple Publishing Workflow

```bash
# 1. Bump version
npm version patch

# 2. Optional: Type check
npm run type-check

# 3. Verify package
npm publish --dry-run

# 4. Publish!
npm publish --access public

# 5. Tag git
git push --tags
```

## 🌟 Key Advantages

**For Publishers (You):**
- ✅ No coordination with core package versions
- ✅ No build complexity
- ✅ Independent release schedule
- ✅ Simpler CI/CD

**For Consumers:**
- ✅ Smaller bundles (wrappers only)
- ✅ Better caching (CDN)
- ✅ No version conflicts
- ✅ Framework-agnostic core

**For the Ecosystem:**
- ✅ One CDN serves all frameworks
- ✅ Shared web component definitions
- ✅ Consistent UX across projects
- ✅ True zero-dependency architecture

---

That's it! No build, no dist, no core dependency. Just publish your React wrappers. 🚀
