# Ty React + Next.js Example

Simple Next.js example testing @gersak/ty and @gersak/ty-react packages.

## Getting Started

### Prerequisites

Make sure both packages are installed:

```bash
# Install the packages (if not already available)
npm install @gersak/ty @gersak/ty-react
```

### Development

```bash
# Install dependencies
npm install

# Start development server
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) to view the example.

## What This Example Tests

### Package Integration
- ✅ @gersak/ty - Core web components library
- ✅ @gersak/ty-react - React wrapper components

### Component Testing
- **TyButton** - Basic button with click handlers and different flavors
- **TyInput** - Input component with value binding and change events
- **TyIcon** - Icon component with size variants (requires icon registry)
- **TyDropdown** - Dropdown with options and selection events
- **TyModal** - Modal with imperative API (show/hide methods)

### React Integration Features
- Event handling with TypeScript interfaces
- Ref forwarding for imperative methods
- Props mapping to web component attributes
- Next.js server-side rendering compatibility

### Browser Environment Tests
- Web Components registration
- Custom Elements API support
- Client-side hydration
- Icon registry initialization

## Project Structure

```
react-nextjs/
├── app/
│   ├── globals.css          # Global styles
│   ├── layout.tsx           # Root layout component
│   └── page.tsx             # Main testing page
├── lib/
│   └── icons.ts             # Icon registry initialization
├── package.json
├── tsconfig.json
├── next.config.js
└── README.md
```

## Expected Results

When everything is working correctly, you should see:

1. **Package Status**: Both packages showing "✅ Loaded"
2. **Component Tests**: All components rendering and responding to interactions
3. **Integration Status**: All green checkmarks for web components, React integration, and Next.js compatibility
4. **Debug Information**: Client environment, Custom Elements support, and mounted status

## Troubleshooting

### Icons Not Showing
- Check browser console for icon initialization messages
- Ensure @gersak/ty loads before icon initialization
- Verify icon names match the registry

### Components Not Rendering
- Check that packages are properly installed
- Verify web components are registered (check browser console)
- Ensure client-side rendering is working

### TypeScript Errors
- Make sure @gersak/ty-react types are properly imported
- Check that component props match the TypeScript interfaces

## Notes

- This example uses Next.js 14+ with the app directory structure
- All components are tested with basic functionality
- Icons require initialization after Ty components load
- Modal component uses React refs for imperative control
- **CSS Styling**: Currently using minimal styling - Ty CSS import needs to be fixed in the package exports
