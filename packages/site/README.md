# Ty Documentation Site

ClojureScript-based documentation site for Ty Web Components, built with Replicant.

## Development

```bash
# Install dependencies
npm install

# Start development server (http://localhost:8000)
npm run dev

# Build for production
npm run build

# Build for GitHub Pages
npm run build:github
```

## Project Structure

```
packages/site/
├── src/              # ClojureScript source
│   └── ty/site/
│       ├── core.cljs       # Main entry point
│       ├── docs.cljs       # Documentation routes
│       ├── state.cljs      # Application state
│       ├── icons.cljs      # Icon definitions
│       ├── docs/           # Component documentation
│       └── views/          # Site views
├── public/           # Static files & build output
│   ├── index.html
│   ├── js/           # Compiled JavaScript
│   └── css/          # Stylesheets
├── deps.edn          # Clojure dependencies
├── shadow-cljs.edn   # Build configuration
└── package.json      # NPM scripts
```

## Development with TypeScript Components

The site uses TypeScript components from `packages/core`. During development:

1. Run TypeScript component dev server: `npm run dev:ts` (in packages/core, port 3000)
2. Run site dev server: `npm run dev` (in packages/site, port 8000)

Both servers support hot module reloading.

## License

MIT
