# React + Ty Guide

Use Ty web components in React with `@gersak/ty-react` — typed wrappers with React-idiomatic event handling, ref forwarding, and controlled component patterns.

## Setup

### 1. Install packages

```bash
npm install @gersak/ty-react
```

### 2. Load Ty in your HTML

```html
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css">
<script type="module" src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/ty.js"></script>
```

### 3. Import and use

```tsx
import { TyButton, TyInput, TyDropdown } from '@gersak/ty-react'

function App() {
  const [name, setName] = useState('')

  return (
    <div className="ty-canvas p-8">
      <TyInput
        label="Name"
        value={name}
        onChange={(e) => setName(e.detail.value)}
      />
      <TyButton flavor="primary" onClick={() => alert(name)}>
        Submit
      </TyButton>
    </div>
  )
}
```

## Import Styles

Two naming conventions:

```tsx
// Ty-prefixed (explicit)
import { TyButton, TyInput, TyDropdown } from '@gersak/ty-react'

// Short names (clean)
import { Button, Input, Dropdown } from '@gersak/ty-react'
```

## Event Handling

Always access values through `event.detail.value`:

```tsx
// Correct
<TyInput onChange={(e) => setValue(e.detail.value)} />

// Wrong — event.value does not exist
<TyInput onChange={(e) => setValue(e.value)} />
```

### Input Components

- `onChange` fires on every keystroke (mapped from `input` event)
- `onChangeCommit` fires on blur (mapped from `change` event)

```tsx
<TyInput
  value={query}
  onChange={(e) => setQuery(e.detail.value)}
  onChangeCommit={(e) => search(e.detail.value)}
/>
```

Event detail structure:
```ts
interface TyInputEventDetail {
  value: any
  formattedValue: string
  rawValue: string
  originalEvent: Event
}
```

### Selection Components

```tsx
<TyDropdown onChange={(e) => setCountry(e.detail.value)}>
  <TyOption value="us">United States</TyOption>
  <TyOption value="de">Germany</TyOption>
</TyDropdown>
```

### Checkbox

```tsx
<TyCheckbox
  checked={agreed}
  onChange={(e) => setAgreed(e.detail.value)}
/>
```

### Modal Events

```tsx
<TyModal
  open={isOpen}
  onOpen={() => console.log('opened')}
  onClose={(e) => {
    setIsOpen(false)
    console.log('reason:', e.detail.reason)
  }}
>
  <div className="p-6 ty-elevated rounded-lg">
    Modal content
  </div>
</TyModal>
```

### Tag Events

```tsx
<TyTag
  value="react"
  dismissible
  onTagClick={(e) => console.log('clicked', e.detail.value)}
  onTagDismiss={(e) => removeTag(e.detail.value)}
>
  React
</TyTag>
```

## Controlled Components

```tsx
function ContactForm() {
  const [form, setForm] = useState({
    name: '',
    email: '',
    country: '',
    message: ''
  })

  const update = (field: string) => (e: any) =>
    setForm(prev => ({ ...prev, [field]: e.detail.value }))

  return (
    <form onSubmit={handleSubmit}>
      <TyInput label="Name" value={form.name} onChange={update('name')} required />
      <TyInput label="Email" type="email" value={form.email} onChange={update('email')} required />
      <TyDropdown label="Country" value={form.country} onChange={update('country')}>
        <TyOption value="us">United States</TyOption>
        <TyOption value="de">Germany</TyOption>
      </TyDropdown>
      <TyTextarea label="Message" value={form.message} onChange={update('message')} rows={4} />
      <TyButton type="submit" flavor="primary">Send</TyButton>
    </form>
  )
}
```

## Multiselect

Value is an array, passed as comma-separated string:

```tsx
const [selected, setSelected] = useState<string[]>([])

<TyMultiselect
  value={selected}
  onChange={(e) => setSelected(e.detail.value)}
  label="Tags"
  placeholder="Select tags..."
>
  <TyOption value="react">React</TyOption>
  <TyOption value="vue">Vue</TyOption>
  <TyOption value="angular">Angular</TyOption>
</TyMultiselect>
```

## Dropdown with Data

```tsx
<TyDropdown
  options={[
    { value: 'us', text: 'United States' },
    { value: 'de', text: 'Germany' },
    { value: 'jp', text: 'Japan' }
  ]}
  onChange={(e) => setCountry(e.detail.value)}
/>
```

## Refs and Imperative API

### Modal

```tsx
const modalRef = useRef<TyModalRef>(null)

<TyButton onClick={() => modalRef.current?.show()}>Open Modal</TyButton>

<TyModal ref={modalRef} onClose={() => console.log('closed')}>
  <div className="p-6 ty-elevated rounded-lg">
    <p>Modal content</p>
    <TyButton onClick={() => modalRef.current?.hide()}>Close</TyButton>
  </div>
</TyModal>
```

### Popup

```tsx
const popupRef = useRef<TyPopupRef>(null)

<TyPopup ref={popupRef}>
  <TyButton onClick={() => popupRef.current?.togglePopup()}>Toggle</TyButton>
  <div className="p-4 ty-floating rounded-lg">Popup content</div>
</TyPopup>
```

### Scroll Container

```tsx
const scrollRef = useRef<TyScrollContainerRef>(null)

<TyScrollContainer ref={scrollRef} maxHeight="300px" shadow>
  {/* Long content */}
</TyScrollContainer>

<TyButton onClick={() => scrollRef.current?.scrollToTop()}>Back to top</TyButton>
```

## Calendar with Custom Rendering

```tsx
<TyCalendar
  value="2026-03-24"
  onChange={(e) => setDate(e.detail.value)}
  dayContentFn={(ctx) => {
    const el = document.createElement('div')
    el.textContent = ctx.day
    if (ctx.day === 25) {
      el.style.fontWeight = 'bold'
      el.style.color = 'var(--ty-color-primary)'
    }
    return el
  }}
  customCSS=".day-cell { border-radius: 8px; }"
/>
```

## Numeric Input Types

```tsx
// Currency
<TyInput type="currency" currency="EUR" locale="de-DE" value={price}
  onChange={(e) => setPrice(e.detail.value)} />

// Percentage
<TyInput type="percent" precision={1} value={rate}
  onChange={(e) => setRate(e.detail.value)} />

// Compact numbers (1K, 1M)
<TyInput type="compact" value={count}
  onChange={(e) => setCount(e.detail.value)} />
```

## Tabs and Wizard

```tsx
const [activeTab, setActiveTab] = useState('overview')

<TyTabs active={activeTab} onChange={(e) => setActiveTab(e.detail.value)}>
  <TyTab id="overview" label="Overview">
    <p>Overview content</p>
  </TyTab>
  <TyTab id="settings" label="Settings">
    <p>Settings content</p>
  </TyTab>
</TyTabs>
```

```tsx
<TyWizard active={step} completed={completedSteps.join(',')}>
  <TyStep id="info" label="Information">Step 1 content</TyStep>
  <TyStep id="review" label="Review">Step 2 content</TyStep>
  <TyStep id="confirm" label="Confirm">Step 3 content</TyStep>
</TyWizard>
```

## Icons

Register icons before use:

```tsx
// In index.html or a setup module
import { check, heart, star } from '@gersak/ty/icons/lucide'
window.tyIcons.register({ check, heart, star })
```

Then use in components:

```tsx
<TyIcon name="check" size="sm" />
<TyIcon name="heart" size="lg" spin />
```

## Slots

Use `slot="start"` and `slot="end"` for icons inside buttons, inputs, and tags. The component handles spacing automatically.

```tsx
{/* Button with start icon */}
<TyButton flavor="primary">
  <TyIcon slot="start" name="save" size="sm" />
  Save
</TyButton>

{/* Button with end icon */}
<TyButton flavor="neutral">
  Next
  <TyIcon slot="end" name="chevron-right" size="sm" />
</TyButton>

{/* Input with icon slots */}
<TyInput type="currency" currency="EUR" label="Price">
  <TyIcon slot="start" name="euro" />
</TyInput>

<TyInput type="email" label="Email">
  <TyIcon slot="start" name="mail" size="sm" />
  <TyIcon slot="end" name="check" size="sm" />
</TyInput>

{/* Tag with icon */}
<TyTag flavor="primary" dismissible>
  <TyIcon slot="start" name="star" size="sm" />
  Featured
</TyTag>

{/* Rich tab labels via slot="label-{id}" */}
<TyTabs height="400px">
  <span slot="label-settings" className="flex items-center gap-2">
    <TyIcon name="settings" size="sm" />
    Settings
  </span>
  <TyTab id="settings" label="Settings">Content</TyTab>
</TyTabs>
```

## Form Integration

Ty components participate in HTML forms via `ElementInternals`:

```tsx
<form onSubmit={(e) => {
  e.preventDefault()
  const data = new FormData(e.currentTarget)
  console.log(Object.fromEntries(data.entries()))
}}>
  <TyInput name="email" type="email" required label="Email" />
  <TyDropdown name="role" label="Role">
    <TyOption value="admin">Admin</TyOption>
    <TyOption value="user">User</TyOption>
  </TyDropdown>
  <TyCheckbox name="agree" required>I agree to terms</TyCheckbox>
  <TyButton type="submit" flavor="primary">Submit</TyButton>
</form>
```

## Next.js Integration

### App Router Setup

```tsx
// app/layout.tsx
export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <head>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css" />
        <script type="module" src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/ty.js" />
      </head>
      <body className="ty-canvas">{children}</body>
    </html>
  )
}
```

### Client Components

Ty components require the browser — use `'use client'` directive:

```tsx
'use client'

import { useState } from 'react'
import { TyInput, TyButton } from '@gersak/ty-react'

export default function SearchForm() {
  const [query, setQuery] = useState('')
  return (
    <div className="flex gap-2">
      <TyInput value={query} onChange={(e) => setQuery(e.detail.value)} placeholder="Search..." />
      <TyButton flavor="primary" onClick={() => search(query)}>Search</TyButton>
    </div>
  )
}
```

### Wait for Components

If you see FOUC (flash of unstyled content), gate rendering until Ty is loaded:

```tsx
'use client'
import { useState, useEffect } from 'react'

export function TyLoader({ children }: { children: React.ReactNode }) {
  const [loaded, setLoaded] = useState(false)

  useEffect(() => {
    const check = () => {
      if (window.customElements?.get('ty-button')) {
        setLoaded(true)
      } else {
        setTimeout(check, 50)
      }
    }
    check()
  }, [])

  if (!loaded) return null
  return <>{children}</>
}
```

## Component Reference

| Component | Value Prop | Event | Ref Methods |
|-----------|-----------|-------|-------------|
| `TyButton` | -- | `onClick` | -- |
| `TyInput` | `value` | `onChange` / `onChangeCommit` | -- |
| `TyTextarea` | `value` | `onChange` / `onChangeCommit` | -- |
| `TyCheckbox` | `checked` | `onChange` | -- |
| `TyDropdown` | `value` | `onChange` | -- |
| `TyMultiselect` | `value` (array) | `onChange` | -- |
| `TyDatePicker` | `value` (ISO) | `onChange` / `onOpen` / `onClose` | -- |
| `TyCalendar` | `value` (ISO) | `onChange` / `onNavigate` | -- |
| `TyTabs` | `active` | `onChange` | -- |
| `TyModal` | `open` | `onOpen` / `onClose` | `show()` / `hide()` |
| `TyPopup` | `manual` | -- | `openPopup()` / `closePopup()` / `togglePopup()` |
| `TyTag` | `value` | `onTagClick` / `onTagDismiss` | -- |
| `TyIcon` | `name` | -- | -- |
| `TyCopy` | `value` | -- | -- |
| `TyTooltip` | -- | -- | -- |
| `TyScrollContainer` | -- | -- | `scrollToTop()` / `scrollToBottom()` / `updateShadows()` |
| `TyWizard` | `active` | -- | -- |
