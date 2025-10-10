# Pre-publish Checklist for @gersak/ty-react

## ✅ Completed Setup
- [x] LICENSE file created (MIT)
- [x] README.md with comprehensive documentation
- [x] package.json properly configured
- [x] TypeScript definitions for all components
- [x] Vite build configuration for library
- [x] .npmignore to include only dist/

## 🔨 Build Process
Run these commands in the `/react` directory:

```bash
# 1. Install dependencies (if not already done)
npm install

# 2. Type check (uses tsconfig.json with correct settings)
npm run type-check

# 3. Build the package
npm run build

# 4. Verify build output
ls -la dist/
```

## ✅ Expected Build Output
After running `npm run build`, you should see:
```
dist/
├── index.js         # ESM bundle
├── index.js.map     # Source map
├── index.cjs        # CommonJS bundle  
├── index.cjs.map    # Source map
├── index.d.ts       # TypeScript definitions
└── index.d.ts.map   # Declaration map
```

## 🔍 Pre-publish Verification

### 1. Test Local Import
```bash
# In the react/ directory
npm pack
# This creates gersak-ty-react-0.1.0.tgz

# Test in another project
npm install /path/to/gersak-ty-react-0.1.0.tgz
```

### 2. Test TypeScript Integration
Create a test file to verify imports work:
```tsx
import { TyButton, TyInput, type TyButtonProps } from '@gersak/ty-react';
// Should have no TypeScript errors
```

### 3. Verify Package Contents
```bash
npm publish --dry-run
# Shows what would be published without actually publishing
```

## 🚨 Important: The esModuleInterop Issue
If you see TypeScript errors about `esModuleInterop` when testing individual files, this is normal. Our `tsconfig.json` has the correct configuration (`"esModuleInterop": true`). The errors only appear when running TypeScript on individual files without the project configuration.

**✅ Correct commands to test:**
```bash
# Use npm scripts (these use tsconfig.json correctly)
npm run type-check
npm run build

# Or use tsc with project flag
npx tsc --noEmit --project tsconfig.json
```

**❌ Don't use (will show false errors):**
```bash
# This ignores tsconfig.json and shows esModuleInterop errors
npx tsc --noEmit src/components/*.tsx
```

## 📦 Publishing Commands

### Option 1: Public npm Registry
```bash
# One-time: Login to npm
npm login

# Publish (will run prepublishOnly script automatically)
npm publish --access public
```

### Option 2: GitHub Packages
```bash
# One-time: Configure registry
npm config set @gersak:registry https://npm.pkg.github.com
npm login --registry=https://npm.pkg.github.com

# Publish
npm publish
```

### Option 3: Private Registry
```bash
# Configure your private registry
npm config set registry https://your-registry.com

# Publish
npm publish
```

## 🎯 Post-publish Verification
```bash
# Test installation from npm
npm install @gersak/ty-react

# Verify in a React project
import { TyButton } from '@gersak/ty-react';
```

## 📝 Version Management
Current version: `0.1.0`

To bump version before publishing:
```bash
npm version patch   # 0.1.0 -> 0.1.1
npm version minor   # 0.1.0 -> 0.2.0  
npm version major   # 0.1.0 -> 1.0.0
```

## 🔗 Dependencies
- Peer dependency: `@gersak/ty ^0.1.0` (must be published first)
- React: `>=18.0.0`
- React DOM: `>=18.0.0`

## 🚀 Ready to Publish!
All files are prepared and the package is ready for publishing.
