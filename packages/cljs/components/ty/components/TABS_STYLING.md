# Tabs Styling Guide

## Philosophy

Tabs styling follows a **simple, user-controlled approach**:

- **Active tab styling** → Controlled by user via `slot="marker"`
- **Tray/container styling** → Transparent by default, customizable
- **Button states** → Hover and disabled states customizable
- **Border** → Optional, can be hidden or styled

## Available CSS Variables

### Tray (Container) Styling
```css
--ty-tabs-bg: transparent                     /* Tray background color */
--ty-tabs-border-width: 1px                   /* Tray border thickness (0 = no border) */
--ty-tabs-border-color: var(--ty-border)      /* Tray border color */
```

### Tab Button Base
```css
--ty-tabs-button-padding: 6px 12px            /* Button padding */
--ty-tabs-button-gap: 8px                     /* Gap between icon and text */
--ty-tabs-button-color: var(--ty-text-)       /* Default text color */
```

### Tab Button Hover State
```css
--ty-tabs-button-hover-bg: var(--ty-surface-elevated)   /* Hover background */
--ty-tabs-button-hover-color: var(--ty-text)            /* Hover text color */
```

### Tab Button Disabled State
```css
--ty-tabs-button-disabled-opacity: 0.5        /* Disabled tab opacity */
```

### Tab Button Focus State
```css
--ty-tabs-button-focus-color: var(--ty-color-primary)   /* Focus outline color */
```

## Usage Examples

### Example 1: Default Tabs (No Customization)
```html
<ty-tabs width="800px" height="500px">
  <ty-tab id="general" label="General">...</ty-tab>
  <ty-tab id="advanced" label="Advanced">...</ty-tab>
</ty-tabs>
```
Result: Transparent tray, subtle hover, no active indicator

### Example 2: Tabs with Underline Marker
```html
<ty-tabs width="800px" height="500px">
  <!-- Simple colored underline as marker -->
  <div slot="marker" style="
    height: 2px;
    background: var(--ty-color-primary);
    position: absolute;
    bottom: 0;
  "></div>
  
  <ty-tab id="general" label="General">...</ty-tab>
  <ty-tab id="advanced" label="Advanced">...</ty-tab>
</ty-tabs>
```
Result: Animated underline follows active tab

### Example 3: Pill-Style Tabs (Material Design)
```html
<ty-tabs width="800px" height="500px" style="
  --ty-tabs-border-width: 0;
  --ty-tabs-bg: var(--ty-surface-elevated);
  --ty-tabs-button-padding: 8px 16px;
">
  <!-- Rounded pill marker -->
  <div slot="marker" class="ty-bg-primary rounded-full shadow-sm"></div>
  
  <ty-tab id="general" label="General">...</ty-tab>
  <ty-tab id="advanced" label="Advanced">...</ty-tab>
</ty-tabs>
```
Result: Pills with animated background highlight

### Example 4: Minimal/Clean Tabs
```html
<ty-tabs width="800px" height="500px" style="
  --ty-tabs-border-width: 0;
  --ty-tabs-button-hover-bg: transparent;
  --ty-tabs-button-hover-color: var(--ty-text++);
">
  <!-- Subtle marker -->
  <div slot="marker" style="
    background: var(--ty-color-primary);
    opacity: 0.1;
    border-radius: 0.5rem;
  "></div>
  
  <ty-tab id="general" label="General">...</ty-tab>
  <ty-tab id="advanced" label="Advanced">...</ty-tab>
</ty-tabs>
```
Result: Clean, borderless tabs with subtle hover

### Example 5: Custom Tray Background
```html
<ty-tabs width="800px" height="500px" style="
  --ty-tabs-bg: var(--ty-surface-content);
  --ty-tabs-border-color: var(--ty-border++);
">
  <div slot="marker" class="ty-bg-primary- rounded-lg"></div>
  
  <ty-tab id="general" label="General">...</ty-tab>
  <ty-tab id="advanced" label="Advanced">...</ty-tab>
</ty-tabs>
```
Result: Visible tray background with stronger border

### Example 6: Different Hover Style
```html
<ty-tabs width="800px" height="500px" style="
  --ty-tabs-button-hover-bg: var(--ty-bg-primary--);
  --ty-tabs-button-hover-color: var(--ty-text-primary);
">
  <div slot="marker" class="ty-bg-primary rounded"></div>
  
  <ty-tab id="general" label="General">...</ty-tab>
  <ty-tab id="advanced" label="Advanced">...</ty-tab>
</ty-tabs>
```
Result: Custom hover colors with themed feel

### Example 7: Gradient Marker
```html
<ty-tabs width="800px" height="500px">
  <div slot="marker" style="
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 0.5rem;
    box-shadow: 0 2px 8px rgba(0,0,0,0.15);
  "></div>
  
  <ty-tab id="general" label="General">...</ty-tab>
  <ty-tab id="advanced" label="Advanced">...</ty-tab>
</ty-tabs>
```
Result: Beautiful gradient background on active tab

### Example 8: No Border, Dark Background
```html
<ty-tabs width="800px" height="500px" style="
  --ty-tabs-border-width: 0;
  --ty-tabs-bg: var(--ty-surface-elevated);
  --ty-tabs-button-padding: 10px 20px;
">
  <div slot="marker" style="
    background: white;
    border-radius: 0.5rem;
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
  "></div>
  
  <ty-tab id="general" label="General">...</ty-tab>
  <ty-tab id="advanced" label="Advanced">...</ty-tab>
</ty-tabs>
```
Result: Dark tray with white marker (inverse style)

## Design Patterns

### Pattern 1: Underline Tabs (Traditional)
- **Marker**: Thin line at bottom (`height: 2px`)
- **Border**: Visible tray border
- **Background**: Transparent
- **Use case**: Documentation, settings

### Pattern 2: Pill Tabs (Modern)
- **Marker**: Rounded background fill
- **Border**: None (`--ty-tabs-border-width: 0`)
- **Background**: Subtle tray background
- **Use case**: Navigation, mobile apps

### Pattern 3: Minimal Tabs (Clean)
- **Marker**: Very subtle or text color change
- **Border**: None
- **Background**: Transparent
- **Hover**: Minimal or none
- **Use case**: Content organization, articles

### Pattern 4: Bold Tabs (Eye-catching)
- **Marker**: High contrast, shadows, gradients
- **Border**: Optional
- **Background**: Visible
- **Hover**: Noticeable
- **Use case**: Dashboards, landing pages

## Migration from Old Styling

If you were relying on the old selected tab border:

### Old Way (Automatic Border):
```html
<ty-tabs>
  <ty-tab id="general" label="General">...</ty-tab>
</ty-tabs>
```
Result: Blue bottom border on active tab

### New Way (Add Marker):
```html
<ty-tabs>
  <!-- Add this to get similar effect -->
  <div slot="marker" style="
    height: 2px;
    background: var(--ty-color-primary);
    position: absolute;
    bottom: 0;
  "></div>
  
  <ty-tab id="general" label="General">...</ty-tab>
</ty-tabs>
```

Or use Ty classes:
```html
<ty-tabs>
  <div slot="marker" class="ty-bg-primary" style="
    height: 2px;
    position: absolute;
    bottom: 0;
  "></div>
  
  <ty-tab id="general" label="General">...</ty-tab>
</ty-tabs>
```

## Tips

1. **Start simple**: No marker = clean tabs with just hover state
2. **Marker placement**: Use CSS positioning within marker element
3. **Transitions**: Marker animates automatically (300ms by default)
4. **Multiple styles**: Mix Ty semantic classes with custom CSS
5. **Responsive**: All variables work with responsive design
6. **Dark mode**: Ty semantic variables adapt automatically

## What Changed?

### Removed (Marker Now Handles These):
- ❌ Selected tab text color
- ❌ Selected tab background
- ❌ Selected tab border indicator

### Added:
- ✅ Marker slot (user controls active styling)
- ✅ CSS variables for tray, hover, disabled
- ✅ Transparent tray by default
- ✅ Cleaner, more flexible system
