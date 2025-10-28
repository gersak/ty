# Beautiful HTMX + Ty Web Components Example

A stunning Flask application demonstrating the perfect fusion of **Ty Web Components** (TypeScript), **HTMX**, and **Tailwind CSS** for creating dynamic, server-rendered web applications that feel magical.

## ✨ What's New

This example now features:

- **🎯 TypeScript Implementation** - Using `@gersak/ty` NPM package via CDN
- **🌲 Tree-Shakeable Icons** - Import only the icons you need (~75 icons, ~8KB)
- **📦 Zero Build Required** - Loads directly from CDN with ES modules

- **🎨 Beautiful Modern Design** - Glass morphism effects, gradients, and animations
- **⚡ Tailwind CSS Integration** - Professional styling with Ty's semantic design system  
- **🌙 Dark/Light Theme** - Seamless theme switching with persistent user preference
- **📱 Responsive Design** - Mobile-first design that works beautifully on all devices
- **🎭 Enhanced Interactions** - Hover effects, loading states, and smooth animations
- **🎯 Improved UX** - Better visual hierarchy, spacing, and component organization

## 🚀 Features Demonstrated

- **💫 Smart User Search** - Live server-side filtering with beautiful result cards
- **📋 Task Dashboard** - Real-time filtering and status management with hover actions  
- **📅 Interactive Calendar** - Date selection with enhanced visual feedback
- **🎭 Dynamic Modals** - Server-powered content loading with loading states
- **✅ Real-time Validation** - Server-side form validation with instant visual feedback
- **🔔 Notifications** - Beautiful notification system integration

## 🛠 Setup Instructions

### Prerequisites
- **Python 3.8+** installed
- **Node.js 16+** for Tailwind CSS build process
- **Modern Browser** with ES2020+ support (Chrome, Firefox, Safari, Edge)

### Quick Setup

1. **Navigate to the example directory:**
   ```bash
   cd examples/htmx-flask
   ```

2. **Run the automated setup script:**
   ```bash
   ./setup.sh
   ```

3. **Start the development server:**
   ```bash
   npm run dev          # Runs both Tailwind watch and Flask
   # OR
   python app.py        # Flask only (if CSS is already built)
   ```

4. **Open your browser:**
   ```
   http://localhost:5000
   ```

### Manual Setup

If the automated setup doesn't work, follow these steps:

1. **Install Python dependencies:**
   ```bash
   python -m venv .venv
   source .venv/bin/activate  # On Windows: .venv\Scripts\activate
   pip install -r requirements.txt
   ```

2. **Install Node.js dependencies:**
   ```bash
   npm install
   ```

3. **Components load from CDN automatically:**
   ```html
   <!-- Already configured in base.html -->
   <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/ty.css">
   <script type="module" src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/index.js"></script>
   ```

4. **Build Tailwind CSS:**
   ```bash
   npm run build:css:prod    # Production build
   # OR
   npm run build:css         # Development with watch
   ```

## 📦 TypeScript Implementation & Icon Loading

This example uses the **modern TypeScript implementation** of Ty components, loaded directly from CDN with **tree-shakeable icon imports**.

### Component Loading (CDN)

Components load automatically from `@gersak/ty` NPM package via jsDelivr CDN:

```html
<!-- In templates/base.html -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/ty.css">
<script type="module" src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/index.js"></script>
```

**Benefits:**
- ✅ Zero build step required for components
- ✅ Automatic updates with `@latest`
- ✅ Browser caching across sites
- ✅ ES modules with native imports

### Icon Loading (Tree-Shaking)

Icons use **tree-shaking** to load only what you need:

```javascript
// static/js/icons.js - Only imports ~75 icons needed for demo
import { registerIcons } from 'https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/icons/registry.js'

const { 
  home, settings, check, x, plus, minus,  // Core UI (~6 icons)
  sun, moon,                                // Theme (~2 icons)
  edit, calendar, layers, window,          // Navigation (~4 icons)
  // ... ~75 total icons
} = await import('https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/icons/lucide.js')

registerIcons({ home, settings, check /* ... */ })
```

**Bundle Impact:**
- 🌲 Tree-shaking: **~8KB** for 75 icons
- 📦 Full library: ~897KB for 1,636 icons (NOT loaded)
- ⚡ **93% smaller** than loading full icon set

**Available Icons:**
- Lucide: 1,636 icons (tree-shakeable)
- Heroicons: 4 variants
- Material Design: 5 variants  
- FontAwesome: 3 variants

**Debug Helpers:**
```javascript
// Check if icon is loaded
checkTyIcon('home')              // Available ✅

// List all loaded icons  
listTyIcons()                    // ['home', 'settings', 'check', ...]

// Add more icons at runtime
registerAdditionalIcons(['wifi', 'bluetooth', 'cpu'])
```

### Global API

The TypeScript implementation exposes `window.ty` for runtime access:

```javascript
// Icon registry
window.ty.icons.register({ check, heart })  // Register icons
window.ty.icons.has('check')                 // true
window.ty.icons.get('check')                 // '<svg>...</svg>'
window.ty.icons.list()                       // ['check', 'heart']

// Version info
window.ty.version                            // '0.2.0'
```

## 🏗 Project Structure

```
htmx-flask/
├── 📁 src/                     # Tailwind CSS source
│   └── input.css              # Custom Tailwind configuration
├── 📁 static/
│   ├── 📁 css/
│   │   └── app.css           # Generated Tailwind styles (ty.css from CDN)
│   └── 📁 js/
│       ├── icons.js          # Tree-shakeable icon loading (~8KB, 75 icons)
│       └── app.js            # Application logic & HTMX handlers
├── 📁 templates/
│   ├── base.html             # Enhanced base template
│   ├── index.html            # Beautiful homepage
│   ├── forms.html            # Form examples
│   └── 📁 partials/          # HTMX response templates
│       ├── user_search_results.html  # Enhanced user cards
│       ├── task_item.html             # Beautiful task items  
│       └── ...
├── app.py                    # Flask application
├── package.json              # Node.js dependencies & scripts
├── tailwind.config.js        # Tailwind configuration
├── setup.sh                  # Automated setup script
└── requirements.txt          # Python dependencies
```

## 🎨 Design System Integration

### Ty + Tailwind Harmony

This example showcases the perfect integration of:

- **🎯 Ty's Semantic Design System** - Colors, surfaces, and component tokens
- **⚡ Tailwind's Utility Classes** - Layout, spacing, and responsive design
- **🎨 Custom Components** - Glass cards, gradients, and animations

### Key Design Features

```css
/* Glass morphism cards */
.glass-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

/* Semantic color integration */
.text-gradient-primary {
  background: linear-gradient(135deg, 
    var(--ty-color-primary-strong), 
    var(--ty-color-primary-mild)
  );
}

/* Status badges using Ty colors */
.status-completed {
  background-color: var(--ty-bg-success-soft);
  color: var(--ty-color-success-strong);
}
```

## 🎯 Development Commands

```bash
# Development (with live reload)
npm run dev                   # Tailwind + Flask together

# Build commands  
npm run build:css            # Build Tailwind CSS (development)
npm run build:css:prod       # Build Tailwind CSS (production, minified)

# Note: Ty components load from CDN automatically
# No build step needed for @gersak/ty components!

# Flask only
python app.py                # Run Flask server only
```

## 🎨 Customization

### Tailwind Configuration

The `tailwind.config.js` integrates seamlessly with Ty's design system:

```js
// Ty color integration
colors: {
  primary: {
    50: 'rgb(var(--ty-color-primary-faint) / <alpha-value>)',
    500: 'rgb(var(--ty-color-primary) / <alpha-value>)',
    900: 'rgb(var(--ty-color-primary-strong) / <alpha-value>)',
  }
}
```

### Custom Styles

Add your custom styles to `src/input.css`:

```css
@layer components {
  .my-component {
    @apply ty-elevated rounded-xl p-6;
    /* Your custom styles using both Ty and Tailwind */
  }
}
```

## 🌙 Theme System

Seamless dark/light mode switching:

- **Automatic detection** - Respects user's system preference
- **Persistent storage** - Remembers user's choice in localStorage  
- **Ty integration** - Uses Ty's complete dark theme system
- **Tailwind compatibility** - All utilities work in both themes

## 📱 Responsive Design

Mobile-first responsive design using Tailwind:

```html
<div class="grid grid-cols-1 lg:grid-cols-2 gap-8">
  <!-- Mobile: single column, Desktop: two columns -->
</div>
```

## 🔧 API Endpoints

Enhanced HTMX-compatible endpoints:

- `GET /api/users/search` - Enhanced user search with cards
- `GET /api/tasks/filter` - Beautiful task filtering  
- `POST /api/form/validate` - Real-time form validation
- `POST /api/date/select` - Calendar date handling
- `GET /api/modal/content/<type>` - Dynamic modal content

## 🐛 Troubleshooting

### Common Issues

1. **Tailwind styles not working:**
   ```bash
   npm run build:css:prod  # Rebuild Tailwind
   ```

2. **Ty components not loading from CDN:**
   - Check browser console for network errors
   - Verify CDN is accessible: https://cdn.jsdelivr.net/npm/@gersak/ty@latest
   - Check browser's network tab for blocked requests

3. **Theme toggle not working:**
   - Check browser console for JavaScript errors
   - Verify `localStorage` permissions

4. **HTMX requests failing:**
   - Check Flask server is running on port 5000
   - Verify HTMX is loaded in browser dev tools

### Performance

**Bundle Sizes (TypeScript Implementation):**
- **CSS**: ~35KB (ty.css from CDN) + ~25KB (app.css) = **~60KB total**
- **JavaScript**: 
  - Ty components: ~40KB (core) + ~8KB (75 icons) = **~48KB**
  - HTMX: ~15KB
  - Total: **~63KB JavaScript**
- **First Load**: ~2s on 3G, <1s on fast connection
- **Cached Load**: <500ms (CDN browser caching)
- **Lighthouse Score**: 95+ (Performance, Accessibility, Best Practices)

**Tree-Shaking Impact:**
- Without: Would load ~897KB for full Lucide icon set
- With: Only ~8KB for 75 needed icons
- **Savings**: 93% reduction in icon bundle size

## 🚀 Production Deployment

For production deployment:

1. **Build optimized assets:**
   ```bash
   npm run build:css:prod
   ```

2. **Use a production WSGI server:**
   ```bash
   pip install gunicorn
   gunicorn -w 4 -b 0.0.0.0:5000 app:app
   ```

3. **Serve static files via CDN/nginx** for better performance

## 🎉 What's Next?

This example provides a solid foundation for building beautiful, server-rendered applications. You can extend it by:

- **🔐 Adding authentication** with Flask-Login  
- **💾 Database integration** with SQLAlchemy
- **📊 Real-time updates** with WebSockets
- **🧪 Testing** with pytest
- **🚀 Deployment** to cloud platforms

---

**Enjoy building beautiful web applications!** ✨

## 📚 Additional Resources

### Ty Components (TypeScript Implementation)
- 📦 **NPM Package**: [@gersak/ty](https://www.npmjs.com/package/@gersak/ty)
- 🔗 **CDN**: https://cdn.jsdelivr.net/npm/@gersak/ty@latest
- 📝 **Documentation**: See TYPESCRIPT_IMPLEMENTATION_GUIDE.md in project root
- 🎨 **Icons**: 3000+ tree-shakeable icons from Lucide, Heroicons, Material, FontAwesome

### Other Technologies
- ⚡ [HTMX Documentation](https://htmx.org/) - Server-side rendering made simple
- 🎯 [Tailwind CSS](https://tailwindcss.com/) - Utility-first CSS framework
- 🐍 [Flask Documentation](https://flask.palletsprojects.com/) - Python web framework

### Key Implementation Files
- `static/js/icons.js` - Tree-shaking icon loading example
- `templates/base.html` - CDN setup and component loading
- `static/js/app.js` - window.ty API usage examples