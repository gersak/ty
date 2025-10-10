/**
 * Tag Component Styles
 * PORTED FROM: clj/ty/components/tag.css
 */

export const tagStyles = `
/* Tag Component Styles using centralized ty.variables.css */

/* Host element */

/* Hidden attribute support */
:host([hidden]) {
  display: none !important;
}

/* Main container */
.tag-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--ty-spacing-2);
  /* Default pill shape - can be overridden */
  border-radius: var(--ty-radius-full);
  font-family: var(--ty-font-sans);
  font-weight: var(--ty-font-medium);
  line-height: var(--ty-line-height-none);
  text-align: center;
  white-space: nowrap;
  user-select: none;
  box-sizing: border-box;

  /* Default MD size - no need for [size="md"] */
  padding: var(--ty-spacing-mini) var(--ty-spacing-1);
  /* 8px 16px */
  font-size: var(--ty-font-sm);
  /* 14px */
  line-height: var(--ty-line-height-tight);
  min-width: var(--ty-spacing-16);
  /* 64px */

  /* Transitions using centralized values */
  transition: var(--ty-transition-all);

  /* Default styling using semantic variables */
  background: var(--ty-bg-neutral-mild);
  color: var(--ty-color-neutral-strong);
  border: 1px solid;
  border-color: var(--ty-color-neutral);
}

/* Non-pill variant - rectangular with rounded corners */
:host([pill="false"]) .tag-container,
:host([not-pill="true"]) .tag-container {
  border-radius: var(--ty-radius-md);
  /* 6px instead of full pill */
}

/* Tags with value attribute but NOT selected should be clickable */
:host([value]:not([selected])) .tag-container:not([aria-disabled="true"]) {
  cursor: pointer;
}

:host([selected]) .tag-container:not([aria-disabled="true"]) {
  cursor: initial;
}

.tag-container[tabindex]:not([aria-disabled="true"]):hover {
  background: var(--ty-bg-neutral-mild);
  transform: translateY(-1px);
  box-shadow: var(--ty-shadow-md);
}

.tag-container[tabindex]:not([aria-disabled="true"]):active {
  transform: translateY(0);
  box-shadow: var(--ty-shadow-sm);
}

.tag-container[tabindex]:focus {
  outline: none;
}

/* Disabled state */
.tag-container[aria-disabled="true"] {
  opacity: 0.6;
  cursor: not-allowed;
  pointer-events: none;
}

/* Slot containers */
.tag-start,
.tag-content,
.tag-end {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.tag-content {
  flex: 1;
  min-width: 0;
}

/* Dismiss button */
.tag-dismiss {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0;
  background: none;
  border: none;
  color: currentColor;
  cursor: pointer;
  border-radius: var(--ty-radius-full);
  transition: var(--ty-transition-all);
  opacity: 0.7;
  flex-shrink: 0;
  /* Default MD dismiss button size */
  width: var(--ty-spacing-4);
  /* 16px */
  height: var(--ty-spacing-4);
}

.tag-dismiss:hover {
  opacity: 1;
  background: rgba(0, 0, 0, 0.1);
}

.tag-dismiss svg {
  width: 100%;
  height: 100%;
}

/* Size variants - override defaults */
:host([size="xs"]) .tag-container {
  padding: var(--ty-spacing-px) var(--ty-spacing-mini);
  font-size: var(--ty-font-xs);
  line-height: var(--ty-line-height-tight);
  min-width: var(--ty-spacing-10);
}

:host([size="xs"]) .tag-dismiss {
  width: var(--ty-spacing-3);
  /* 12px */
  height: var(--ty-spacing-3);
  margin-left: var(--ty-spacing-2);
}

:host([size="sm"]) .tag-container {
  padding: var(--ty-spacing-mini) var(--ty-spacing-1);
  /* 4px 12px */
  font-size: var(--ty-font-xs);
  /* 12px */
  line-height: var(--ty-line-height-tight);
  min-width: var(--ty-spacing-12);
  /* 48px */
}

:host([size="sm"]) .tag-dismiss {
  width: var(--ty-spacing-3);
  height: var(--ty-spacing-3);
  margin-left: var(--ty-spacing-2);
}

/* MD is now the default - no need for explicit [size="md"] selector */
/* All default styles above apply to MD size */

:host([size="lg"]) .tag-container {
  padding: var(--ty-spacing-1) var(--ty-spacing-2);
  font-size: var(--ty-font-lg);
  line-height: var(--ty-line-height-tight);
  min-width: var(--ty-spacing-20);
}

:host([size="lg"]) .tag-dismiss {
  width: var(--ty-spacing-5);
  height: var(--ty-spacing-5);
  margin-left: var(--ty-spacing-4);
}

:host([size="xl"]) .tag-container {
  padding: var(--ty-spacing-2) var(--ty-spacing-2);
  font-size: var(--ty-font-2xl);
  line-height: var(--ty-line-height-tight);
  min-width: var(--ty-spacing-24);
}

:host([size="xl"]) .tag-dismiss {
  width: var(--ty-spacing-5);
  /* 20px */
  height: var(--ty-spacing-5);
  margin-left: var(--ty-spacing-4);
}

/* ===== NEW: INDUSTRY-STANDARD FLAVOR VARIANTS ===== */

/* Primary */
:host([flavor="primary"]) .tag-container {
  background: var(--ty-bg-primary);
  color: var(--ty-color-primary-strong);
  border-color: var(--ty-border-primary);
}

:host([flavor="primary"]) .tag-container[tabindex]:hover {
  background: var(--ty-bg-primary-mild);
}

:host([flavor="primary"]) .tag-container[tabindex]:focus {
  box-shadow: 0 0 0 3px var(--ty-color-primary-faint);
}

/* Secondary */
:host([flavor="secondary"]) .tag-container {
  background: var(--ty-bg-secondary);
  color: var(--ty-color-secondary-strong);
  border-color: var(--ty-border-secondary);
}

:host([flavor="secondary"]) .tag-container[tabindex]:hover {
  background: var(--ty-bg-secondary-mild);
}

:host([flavor="secondary"]) .tag-container[tabindex]:focus {
  box-shadow: 0 0 0 3px var(--ty-color-secondary-faint);
}

/* Success */
:host([flavor="success"]) .tag-container {
  background: var(--ty-bg-success);
  color: var(--ty-color-success-strong);
  border-color: var(--ty-border-success);
}

:host([flavor="success"]) .tag-container[tabindex]:hover {
  background: var(--ty-bg-success-mild);
}

:host([flavor="success"]) .tag-container[tabindex]:focus {
  box-shadow: 0 0 0 3px var(--ty-color-success-faint);
}

/* Danger */
:host([flavor="danger"]) .tag-container {
  background: var(--ty-bg-danger);
  color: var(--ty-color-danger-strong);
  border-color: var(--ty-border-danger);
}

:host([flavor="danger"]) .tag-container[tabindex]:hover {
  background: var(--ty-bg-danger-mild);
}

:host([flavor="danger"]) .tag-container[tabindex]:focus {
  box-shadow: 0 0 0 3px var(--ty-color-danger-faint);
}

/* Warning */
:host([flavor="warning"]) .tag-container {
  background: var(--ty-bg-warning);
  color: var(--ty-color-warning-strong);
  border-color: var(--ty-border-warning);
}

:host([flavor="warning"]) .tag-container[tabindex]:hover {
  background: var(--ty-bg-warning-mild);
}

:host([flavor="warning"]) .tag-container[tabindex]:focus {
  box-shadow: 0 0 0 3px var(--ty-color-warning-faint);
}

/* Info */
:host([flavor="info"]) .tag-container {
  background: var(--ty-bg-info);
  color: var(--ty-color-info-strong);
  border-color: var(--ty-border-info);
}

:host([flavor="info"]) .tag-container[tabindex]:hover {
  background: var(--ty-bg-info-mild);
}

:host([flavor="info"]) .tag-container[tabindex]:focus {
  box-shadow: 0 0 0 3px var(--ty-color-info-faint);
}

/* ===== LEGACY FLAVOR VARIANTS (Backward Compatibility) ===== */
/* 
   These legacy flavor attributes continue to work exactly as before
   because they reference the new semantic tokens via CSS variable mapping:
   
   
   The mapping is handled in ty.variables.css, so no changes needed here.
*/

/* Flavor variants using centralized semantic colors */

/* Neutral (default) */
:host([flavor="neutral"]) .tag-container {
  background: var(--ty-bg-neutral-mild);
  color: var(--ty-color-neutral-strong);
  border-color: var(--ty-border-neutral);
}

:host([flavor="neutral"]) .tag-container[tabindex]:hover {
  background: var(--ty-bg-neutral-mild);
}

:host([flavor="neutral"]) .tag-container[tabindex]:focus {
  box-shadow: 0 0 0 3px var(--ty-color-neutral-faint);
}

/* Slotted content styling */
.tag-start ::slotted(*),
.tag-content ::slotted(*),
.tag-end ::slotted(*) {
  display: flex;
  align-items: center;
}

/* Icon sizing for slotted icons */
.tag-start ::slotted(ty-icon),
.tag-end ::slotted(ty-icon) {
  width: 1em;
  height: 1em;
  flex-shrink: 0;
}

/* Count/badge styling using centralized spacing */
.tag-end ::slotted(.count) {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: var(--ty-spacing-6);
  /* 24px */
  height: var(--ty-spacing-5);
  /* 20px */
  padding: 0 var(--ty-spacing-2);
  /* 0 8px */
  background: rgba(0, 0, 0, 0.1);
  border-radius: var(--ty-radius-full);
  font-size: var(--ty-font-xs);
  font-weight: var(--ty-font-semibold);
  line-height: var(--ty-line-height-none);
  flex-shrink: 0;
}

/* Special handling for long text content */
.tag-content {
  overflow: hidden;
  text-overflow: ellipsis;
}

:host([data-type="user"]) .tag-container {
  min-width: var(--ty-spacing-20);
  /* 80px for user tags */
}

/* Dark mode is handled automatically by the centralized variables */
`
