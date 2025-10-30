/**
 * TyResizeObserver styles
 * Minimal styling - just ensures component participates in layout
 */

export const resizeObserverStyles = `
:host {
  /* Default to block display, user can override */
  display: block;
  /* Ensure the component can participate in layout */
  position: relative;
}

/* Slot content styling */
::slotted(*) {
  /* No default styling - preserve natural layout */
}
`
