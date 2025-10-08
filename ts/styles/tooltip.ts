/**
 * Tooltip Styles
 * 
 * CSS for ty-tooltip component with semantic flavor system
 */

import type { StyleContent } from '../types/common.js';

export const tooltipStyles: StyleContent = {
  id: 'ty-tooltip',
  css: `
/* Tooltip host element - display contents to not affect layout */
:host {
  display: contents;
}

/* Tooltip container - positioned element */
#tooltip-container {
  position: fixed;
  top: var(--y, 0px);
  left: var(--x, 0px);
  z-index: var(--ty-z-tooltip, 9999);

  /* Hidden by default */
  visibility: hidden;
  opacity: 0;
  transition: opacity 200ms, visibility 200ms;

  /* Prevent interaction */
  pointer-events: none;
  user-select: none;
  -webkit-user-select: none;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -2px rgba(0, 0, 0, 0.1), var(--ty-shadow-md);
}

/* When open */
#tooltip-container.open {
  visibility: visible;
  opacity: 1;
}

/* Default tooltip content styles */
#tooltip-container {
  /* Base styles */
  background: var(--ty-bg-neutral-soft);
  color: var(--ty-color-neutral-strong);
  padding: var(--ty-tooltip-padding, 6px 10px);
  border-radius: var(--ty-tooltip-radius, 4px);
  border: 2px solid var(--ty-border-strong);
  max-width: var(--ty-tooltip-max-width, 250px);
  font-size: var(--ty-font-sm);
  font-weight: var(--ty-font-semibold);
  line-height: 1.5;
}

/* Content slot */
#tooltip-container ::slotted(*) {
  /* Ensure slotted content inherits styles */
  color: inherit;
  font-size: inherit;
  line-height: inherit;
}

/* Flavor variants - semantic colors */
#tooltip-container[data-flavor="primary"] {
  background: var(--ty-bg-primary);
  color: var(--ty-color-primary-strong);
  border-color: var(--ty-border-primary);
}

#tooltip-container[data-flavor="secondary"] {
  background: var(--ty-bg-secondary);
  color: var(--ty-color-secondary-strong);
  border-color: var(--ty-border-secondary);
}

#tooltip-container[data-flavor="success"] {
  background: var(--ty-bg-success-mild);
  color: var(--ty-color-success-strong);
  border-color: var(--ty-border-success);
}

#tooltip-container[data-flavor="danger"] {
  background: var(--ty-bg-danger);
  color: var(--ty-color-danger-strong);
  border-color: var(--ty-border-danger);
}

#tooltip-container[data-flavor="warning"] {
  background: var(--ty-bg-warning);
  color: var(--ty-color-warning-strong);
  border-color: var(--ty-border-warning);
}

#tooltip-container[data-flavor="info"] {
  background: var(--ty-bg-info);
  color: var(--ty-text-strong);
  border-color: var(--ty-border-info);
}

#tooltip-container[data-flavor="light"] {
  background: var(--ty-surface-elevated);
  color: var(--ty-text-strong);
  border-color: var(--ty-border);
}

#tooltip-container[data-flavor="dark"] {
  background: var(--ty-bg-neutral-soft);
  color: var(--ty-color-neutral-strong);
  border-color: var(--ty-border-strong);
}

#tooltip-container[data-flavor="neutral"] {
  background: var(--ty-bg-neutral);
  color: var(--ty-color-neutral-strong);
  border-color: var(--ty-border-neutral);
}
`,
};
