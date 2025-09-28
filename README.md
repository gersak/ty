# ty - Web Components for ClojureScript

[![jsDelivr](https://data.jsdelivr.com/v1/package/npm/@gersak/ty/badge)](https://www.jsdelivr.com/package/npm/@gersak/ty)
[![NPM Version](https://img.shields.io/npm/v/@gersak/ty.svg)](https://www.npmjs.com/package/@gersak/ty)
[![Bundle Size](https://img.shields.io/bundlephobia/minzip/@gersak/ty)](https://bundlephobia.com/package/@gersak/ty)

Framework churn got old. React, Vue, Angular, Svelte... every year something new, every migration a headache. So I built **ty** using Web Components instead. Turns out the browser's component system is pretty solid.

## Why "ty"?

<blockquote>
<p><em>"After playing around with Replicant, I realized I could build Web Components without React — actually, even without Replicant.</em></p>

<p><em>I'm wondering if it's called 'Replicant' because of <strong>Blade Runner</strong> (I love Blade Runner).</em></p>

<p><em>Maybe I could name my own library something similar... Tyrell? No, that feels too ambitious.</em></p>

<p><em>I don't really want to do something long like <code>tyrell-button</code>. It should be shorter — maybe <code>ty-button</code>.</em></p>

<p><strong><em>Yes! Let's call it ty."</em></strong></p>
</blockquote>

## The Idea

Web Components work everywhere. React today, Vue tomorrow, vanilla JS next week - doesn't matter. Same components, same API. And if you're already using ClojureScript, they integrate beautifully with Google Closure compiler.

**What I'm building:**
- Components that adapt to desktop vs mobile automatically
- Semantic design system that actually makes sense
- Zero JavaScript dependencies (just ClojureScript when you want it)
- Proper calendar handling that doesn't make you cry

Still working on it, but the foundation is solid.

To se ty in action and read documentation go to [this link](https://gersak.github.io/ty).

## Try It Out

**ClojureScript** (best experience):
```clojure
;; deps.edn
{:deps {dev.gersak/ty {:mvn/version "0.1.0"}}}

;; Use it
[:ty-button {:flavor "primary"} "Hello ty!"]
[:ty-calendar {:value @date, :on-date-select #(reset! date %)}]
```

**React** (if that's your thing):
```jsx
import { TyButton, TyCalendar } from '@gersak/ty-react'

<TyCalendar onChange={(e) => setDate(e.detail.date)} />
```

**Vanilla HTML** (works everywhere):
```html
<ty-button flavor="primary">Click me</ty-button>
<ty-calendar value="2024-12-25"></ty-calendar>

<script src="https://cdn.jsdelivr.net/npm/@gersak/ty@0.1.12/ty-lazy.js"></script>
```

## What's Cool About It

**For ClojureScript folks**: When you're already using ClojureScript, ty adds about 50KB instead of the full 80KB because you share the runtime. Plus Google Closure compiler optimizations actually work.

**For everyone else**: Components that work across frameworks. Build once, use everywhere.

**Adaptive behavior**: Dropdowns that know when you're on mobile. Calendars that handle keyboard navigation on desktop and gestures on mobile. No configuration required.

**Semantic colors**: Instead of `bg-blue-500`, you get `ty-bg-primary`. Colors that mean something and adapt to light/dark themes automatically.

## What's Available

```html
<!-- Basic stuff -->
<ty-button flavor="primary">Action</ty-button>
<ty-input placeholder="Type here..." />
<ty-tag removable>Skill</ty-tag>

<!-- Selection -->
<ty-dropdown>
  <ty-option value="cljs">ClojureScript</ty-option>
  <ty-option value="js">JavaScript</ty-option>
</ty-dropdown>

<!-- Calendar that doesn't suck -->
<ty-calendar value="2024-12-25"></ty-calendar>
<ty-date-picker name="birthday"></ty-date-picker>

<!-- Layout -->
<ty-modal>Your content here</ty-modal>
<ty-tooltip message="Helpful info">Hover target</ty-tooltip>
```

**13 components total.** Calendar system is pretty solid. Forms work well. Still adding stuff.

## The Bundle Situation

- **Core**: ~80KB (50KB if you're using ClojureScript)
- **Each component**: 5-15KB
- **Everything**: ~240KB total

Not tiny, but reasonable for what you get. And with ClojureScript + Closure compiler, only the parts you use get bundled.

## Framework Integration

**React**: TypeScript wrappers handle the custom events and refs. Works with React 16-19.

**HTMX**: Just works. Server-side rendering with dynamic interactions.

**Vue/Angular**: Import the components, use them. Web Components are web standards.

**ClojureScript**: Built-in router, i18n, and responsive layout system if you want them.

## Current Status

**Works well**: All the basic components, calendar system, forms, modals.

**Still building**: 
- Tabs component (soon)
- Better mobile adaptations 
- More infrastructure pieces

**The vision**: Components that automatically provide the best UX for each environment. Desktop gets hover states and keyboard navigation. Mobile gets touch-friendly interactions and gestures. No props, no configuration.

## When It Makes Sense

**Good fit**: ClojureScript projects, multi-framework orgs, teams tired of framework churn, wanting semantic design systems.

**Maybe not**: React-only shops (plenty of React-specific options), need something battle-tested right now (give it a few months), highly specific design requirements.

## Links

- **Docs & examples**: [gersak.github.io/ty](https://gersak.github.io/ty)
- **Source**: [github.com/gersak/ty](https://github.com/gersak/ty)
- **ClojureScript**: [clojars.org/dev.gersak/ty](https://clojars.org/dev.gersak/ty)
- **React**: [@gersak/ty-react](https://www.npmjs.com/package/@gersak/ty-react)
- **CDN**: [jsdelivr.net/npm/@gersak/ty](https://www.jsdelivr.net/npm/@gersak/ty)

## Contributing

Built this for the ClojureScript community. If you're using it and want to make it better, pull requests welcome. Especially interested in mobile interaction improvements and new components.

**Development**:
```bash
git clone https://github.com/gersak/ty.git
cd ty
npm install
npm run dev  # http://localhost:8000
```

---

Built with ClojureScript and Web Components. MIT licensed.
