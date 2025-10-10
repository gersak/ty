# Quick Start: packages/core

## 🚀 Development

```bash
cd packages/core
npm run dev
```

Opens: http://localhost:3003/dev/index.html (or next available port)

## 📦 Build

```bash
npm run build
```

Creates optimized bundles in `dist/`

## 🧪 Type Check

```bash
npm run type-check
```

## 🔧 All Commands

| Command | What It Does |
|---------|--------------|
| `npm run dev` | Start dev server with HMR |
| `npm run build` | Production build |
| `npm run preview` | Preview production build |
| `npm run type-check` | Validate TypeScript |
| `npm run clean` | Remove dist/ |

## 📝 Files

- `src/` - TypeScript source
- `dev/index.html` - Test page
- `dev/ty.css` - Symlink to `resources/ty.css`
- `dist/` - Build output
- `vite.config.ts` - Build config
- `postcss.config.js` - CSS processing (no Tailwind)

## ⚡ Quick Tips

**Edit a component:**
1. Open `src/components/button.ts`
2. Make changes
3. Save → See instant update in browser

**Test in browser:**
- Visit `/dev/index.html` during dev
- All components are showcased
- Events logged in console

**Add new component:**
1. Create `src/components/my-component.ts`
2. Export from `src/index.ts`
3. Add to `vite.config.ts` entry points
4. Add demo to `dev/index.html`

## 🎯 Bundle Sizes

Main: 1.24 KB | Button: 17.98 KB | Modal: 8.82 KB | Dropdown: 43.99 KB

Total: ~40 KB gzipped (all components)

## 📚 Documentation

- `README.md` - Package usage
- `VITE_SETUP.md` - Detailed setup
- `SETUP_COMPLETE.md` - Configuration reference
- `ISSUE_RESOLVED.md` - PostCSS fix

Done! 🎉
