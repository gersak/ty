# Ty CSS System - Simple Usage Guide

## Core Rule: TY for Colors, Tailwind for Everything Else

**🎯 SIMPLE PRINCIPLE:**
- **TY classes** = Colors (backgrounds, text, borders, surfaces)
- **Tailwind classes** = Everything else (spacing, layout, typography, etc.)

## 🎨 Available TY Classes (Color Only)

### Surface Classes (Background + Shadow)
```html
<!-- App layout surfaces -->
<div class="ty-canvas">         <!-- App background -->
<div class="ty-content">        <!-- Main content area -->
<div class="ty-elevated">       <!-- Cards, panels (with shadow) -->
<div class="ty-floating">       <!-- Modals, dropdowns (with shadow) -->
<div class="ty-input">          <!-- Form controls background -->
```

### Background Colors (Semantic)
```html
<!-- Semantic backgrounds with +/- variants -->
<div class="ty-bg-primary">     <!-- Base primary background -->
<div class="ty-bg-primary+">    <!-- Stronger primary background -->
<div class="ty-bg-primary-">    <!-- Softer primary background -->

<!-- Available: primary, secondary, success, danger, warning, neutral -->
<div class="ty-bg-success">
<div class="ty-bg-danger+">
<div class="ty-bg-warning-">
```

### Text Colors (5-Variant System)
```html
<!-- Base text hierarchy -->
<p class="ty-text">             <!-- Normal text -->
<p class="ty-text+">            <!-- High emphasis -->
<p class="ty-text++">           <!-- Maximum emphasis -->
<p class="ty-text-">            <!-- Reduced emphasis -->
<p class="ty-text--">           <!-- Minimal emphasis -->

<!-- Available: primary, secondary, success, danger, warning, neutral -->
```

### Border Colors
```html
<!-- Base borders -->
<div class="ty-border">         <!-- Normal border -->
<div class="ty-border+">        <!-- Stronger border -->
<div class="ty-border++">       <!-- Maximum border -->
<div class="ty-border-">        <!-- Soft border -->
<div class="ty-border--">       <!-- Minimal border -->

<!-- Semantic borders -->
<div class="ty-border-primary">
<div class="ty-border-success">
<div class="ty-border-danger">
```

## ✅ Correct Usage Examples

### Card Component
```html
<!-- ✅ GOOD: TY for colors, Tailwind for everything else -->
<div class="ty-elevated p-6 rounded-lg">
  <h2 class="ty-text++ text-xl font-bold mb-4">Card Title</h2>
  <p class="ty-text- text-sm">Card description</p>
  <button class="ty-bg-primary ty-text++ px-4 py-2 rounded">
    Action
  </button>
</div>
```

### Alert Component
```html
<!-- ✅ GOOD: Success alert -->
<div class="ty-bg-success- ty-border-success border rounded-lg p-4">
  <h3 class="ty-text-success++ font-semibold">Success!</h3>
  <p class="ty-text-success text-sm">Operation completed successfully.</p>
</div>

<!-- ✅ GOOD: Danger alert -->
<div class="ty-bg-danger- ty-border-danger border rounded-lg p-4">
  <h3 class="ty-text-danger++ font-semibold">Error!</h3>
  <p class="ty-text-danger text-sm">Something went wrong.</p>
</div>
```

### Button with Icon
```html
<!-- ✅ GOOD: Use flexbox gap, no icon margins -->
<button class="ty-bg-primary ty-text++ px-4 py-2 rounded flex items-center gap-1">
  <ty-icon name="save"></ty-icon>
  Save Document
</button>

<!-- ✅ GOOD: Icon-only button -->
<button class="ty-bg-secondary ty-text++ p-2 rounded">
  <ty-icon name="settings"></ty-icon>
</button>
```

### Form Elements
```html
<!-- ✅ GOOD: Form styling -->
<div class="space-y-4">
  <label class="ty-text+ block text-sm font-medium">
    Email Address
  </label>
  <input class="ty-input ty-border border rounded-md px-3 py-2 w-full 
                focus:ty-border-primary focus:outline-none">
  
  <!-- Success state -->
  <input class="ty-input ty-border-success border rounded-md px-3 py-2 w-full">
  
  <!-- Error state -->
  <input class="ty-input ty-border-danger border rounded-md px-3 py-2 w-full">
</div>
```

## ❌ What NOT to Do

```html
<!-- ❌ BAD: Using Tailwind for colors -->
<div class="bg-blue-500 text++">           <!-- Use ty-bg-primary ty-text++ -->
<p class="text-red-600">Error message</p>      <!-- Use ty-text-danger -->
<div class="border-green-500">                 <!-- Use ty-border-success -->

<!-- ❌ BAD: Using TY for non-color properties -->
<div class="ty-padding-4 ty-rounded-lg">       <!-- Use p-4 rounded-lg -->
<p class="ty-font-bold ty-text-xl">            <!-- Use font-bold text-xl -->

<!-- ❌ BAD: Icon margins in buttons -->
<button class="ty-bg-primary ty-text++ px-4 py-2 rounded">
  <ty-icon name="save" class="mr-3"></ty-icon> <!-- Don't add margins to icons -->
  Save
</button>
```

## 🎯 Quick Reference

### When to Use TY Classes
- **Backgrounds:** `ty-elevated`, `ty-bg-primary`, `ty-bg-success-`
- **Text Colors:** `ty-text`, `ty-text-primary++`, `ty-text-danger`
- **Borders:** `ty-border`, `ty-border-success`, `ty-border+`
- **Surfaces:** `ty-canvas`, `ty-content`, `ty-floating`

### When to Use Tailwind Classes
- **Spacing:** `p-4`, `m-2`, `space-y-4`, `gap-2`
- **Layout:** `flex`, `grid`, `items-center`, `justify-between`
- **Typography:** `text-xl`, `font-bold`, `leading-relaxed`
- **Borders:** `border`, `rounded-lg`, `border-2`
- **Sizing:** `w-full`, `h-screen`, `max-w-md`

## 🚀 Best Practices Checklist

- [ ] Use `ty-elevated` for cards and panels
- [ ] Use semantic colors (`ty-bg-success`, `ty-text-danger`)
- [ ] Use text hierarchy (`ty-text++` for titles, `ty-text-` for captions)
- [ ] Use Tailwind for all non-color styling
- [ ] No margins on `ty-icon` elements in buttons
- [ ] Use `gap` for spacing between icons and text
- [ ] Test in both light and dark themes

## 🎨 Common Patterns

### Page Layout
```html
<div class="ty-canvas min-h-screen">
  <div class="ty-content max-w-6xl mx-auto p-6">
    <div class="ty-elevated rounded-lg p-6">
      <!-- Content -->
    </div>
  </div>
</div>
```

### Navigation
```html
<nav class="ty-elevated border-b ty-border px-6 py-4">
  <div class="flex items-center justify-between">
    <h1 class="ty-text++ text-xl font-bold">App Name</h1>
    <button class="ty-bg-primary text++ px-4 py-2 rounded">
      Sign In
    </button>
  </div>
</nav>
```

### Status Indicators
```html
<!-- Success status -->
<span class="ty-bg-success ty-text-success++ px-2 py-1 rounded text-xs font-medium">
  Active
</span>

<!-- Warning status -->
<span class="ty-bg-warning ty-text-warning++ px-2 py-1 rounded text-xs font-medium">
  Pending
</span>

<!-- Danger status -->
<span class="ty-bg-danger ty-text-danger++ px-2 py-1 rounded text-xs font-medium">
  Error
</span>
```

Remember: **TY for colors, Tailwind for everything else!**
