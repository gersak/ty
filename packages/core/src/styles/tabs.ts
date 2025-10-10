/**
 * Tabs Component Styles
 * 
 * Styles for the ty-tabs container component including:
 * - Flexbox layout with top/bottom placement
 * - Tab buttons with hover and active states
 * - Animated marker that follows active tab
 * - Carousel viewport with transform animations
 * - Responsive design with prefers-reduced-motion support
 * - Fully customizable via CSS Parts (::part)
 */

export const tabsStyles = `
:host {
  display: block;
  width: var(--tabs-width, 100%);
  height: var(--tabs-height, 400px);
  box-sizing: border-box;
}

.tabs-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
  box-sizing: border-box;
}

/* Bottom placement reverses the order */
.tabs-container[data-placement="bottom"] {
  flex-direction: column-reverse;
}

/* ===================================== */
/* Tab Buttons Container */
/* Expose as CSS Part for full styling control */
/* ===================================== */

.tab-buttons {
  display: flex;
  gap: 0;
  flex-shrink: 0;
  position: relative;
  /* For absolute positioned marker */
  
  /* Default minimal styling - customize via ::part(buttons-container) */
  border-bottom: 1px solid var(--ty-border);
  background: transparent;
}

/* Bottom placement moves border to top */
.tabs-container[data-placement="bottom"] .tab-buttons {
  border-bottom: none;
  border-top: 1px solid var(--ty-border);
}

/* ===================================== */
/* Marker Wrapper */
/* Expose as CSS Part for custom marker styling */
/* ===================================== */

.marker-wrapper {
  position: absolute;
  z-index: 0;
  /* Behind buttons */
  pointer-events: none;
  /* Don't block clicks */
  transition: left var(--transition-duration, 300ms) var(--transition-easing, ease-in-out),
    width var(--transition-duration, 300ms) var(--transition-easing, ease-in-out),
    height var(--transition-duration, 300ms) var(--transition-easing, ease-in-out),
    top var(--transition-duration, 300ms) var(--transition-easing, ease-in-out);
}

/* Default marker - simple underline */
.default-marker {
  position: absolute;
  bottom: 0;
  left: 0;
  height: 2px;
  width: 100%;
  background: var(--ty-color-primary);
  pointer-events: none;
}

/* User's marker element fills the wrapper */
::slotted([slot="marker"]) {
  display: block;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}

/* ===================================== */
/* Tab Buttons */
/* ===================================== */

.tab-button {
  min-width: 120px;
  padding: 6px 12px;
  border: none;
  background: transparent;
  cursor: pointer;
  font: inherit;
  color: var(--ty-text-soft);
  transition: color 200ms, background-color 200ms;
  white-space: nowrap;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-weight: var(--ty-font-bold);
  font-size: var(--ty-font-sm);
  position: relative;
  /* Establish stacking context */
  z-index: 10;
  /* Above marker */
}

.tab-button[aria-selected=true] {
  color: var(--ty-text-strong);
}

.tab-button[disabled] {
  opacity: 0.5;
  cursor: not-allowed;
  pointer-events: none;
}

.tab-button:focus-visible {
  outline: 2px solid var(--ty-color-primary);
  outline-offset: -2px;
}

/* ===================================== */
/* Panels Viewport */
/* Expose as CSS Part for panels container styling */
/* ===================================== */

.panels-viewport {
  position: relative;
  flex: 1;
  overflow: hidden;
  /* Critical: hides off-screen panels */
  min-height: 0;
  /* Allows flex child to shrink */
}

/* ===================================== */
/* Panels Wrapper (slides horizontally) */
/* ===================================== */

.panels-wrapper {
  display: flex;
  height: 100%;
  transition: transform var(--transition-duration, 300ms) var(--transition-easing, ease-in-out);
}

/* Respect user's motion preferences */
@media (prefers-reduced-motion: reduce) {
  .panels-wrapper {
    transition-duration: 0ms !important;
  }

  .marker-wrapper {
    transition-duration: 0ms !important;
  }
}

/* ===================================== */
/* Slotted Tab Panels */
/* ===================================== */

::slotted(ty-tab) {
  width: var(--tabs-width, 100%);
  height: 100%;
  flex-shrink: 0;
}
`;
