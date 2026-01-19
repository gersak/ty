/**
 * Wizard Component Styles
 *
 * Styles for the ty-wizard container component including:
 * - Flexbox layout with step indicators and content carousel
 * - Progress line with animated completion overlay
 * - Step circles with completed/active/pending states
 * - Carousel viewport with transform animations
 * - Responsive design with prefers-reduced-motion support
 * - Fully customizable via CSS Parts (::part)
 *
 * Uses global design system tokens (no component-specific variables):
 * - Surfaces: --ty-surface-floating, --ty-surface-content, --ty-surface-elevated
 * - Colors: --ty-color-success, --ty-color-primary, --ty-color-danger
 * - Borders: --ty-border, --ty-border-soft
 * - Text: --ty-text, --ty-text-soft
 *
 * CSS Parts (for styling via ::part):
 * - indicators-wrapper: The header containing step indicators
 * - progress-line: The background progress track
 * - step-circle: Individual step circle indicators
 * - panels-container: The content viewport
 */

export const wizardStyles = `
:host {
  display: block;
  width: var(--wizard-width, 100%);
  height: var(--wizard-height, 700px);
  box-sizing: border-box;
  /* Note: --step-circle-size is NOT set here to allow inheritance from light DOM.
     Use fallbacks in var() calls instead. Set on ty-wizard element to customize. */
}

.wizard-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
  box-sizing: border-box;
  border-radius: 12px;
  border: 1px solid var(--ty-border);
  background: var(--ty-surface-floating);
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px -1px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* ===================================== */
/* Step Indicators Wrapper */
/* Expose as CSS Part for full styling control */
/* ===================================== */

.step-indicators-wrapper {
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  position: relative;
  padding: 24px 24px 16px;
  border-bottom: 1px solid var(--ty-border-soft, var(--ty-border));
  background: var(--ty-surface-content);
}

/* ===================================== */
/* Progress Line (behind step circles) */
/* Expose as CSS Part for custom styling */
/* ===================================== */

.progress-line {
  position: absolute;
  /*
   * With equal-width indicators (flex: 1), each takes 100%/N of the width.
   * Circle centers are at: 50%/N, 150%/N, 250%/N, ... from left.
   * Line spans from first center (50%/N) to last center (100% - 50%/N).
   * Inset = 50% / step-count from each side.
   */
  left: calc(50% / var(--step-count, 4));
  right: calc(50% / var(--step-count, 4));
  /* Vertically center with step circles - uses circle size variable */
  top: calc(var(--step-circle-size, 32px) / 2 - 1px);
  height: 2px;
  background: var(--ty-border);
  z-index: 0;
  pointer-events: none;
  transition: opacity var(--transition-duration, 300ms) var(--transition-easing, ease-in-out);
}

.progress-overlay {
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  background: var(--ty-color-success);
  transition: width var(--transition-duration, 500ms) var(--transition-easing, ease-in-out);
  will-change: width;
}

/* ===================================== */
/* Step Indicators Container */
/* ===================================== */

.step-indicators {
  display: flex;
  align-items: flex-start;
  position: relative;
  /* No padding - let equal-width indicators fill the space */
}

/* ===================================== */
/* Individual Step Indicator */
/* Expose as CSS Part for step styling */
/* ===================================== */

.step-indicator {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  background: transparent;
  border: none;
  padding: 0;
  font: inherit;
  transition: opacity 200ms;
  /* Equal width for all indicators - makes progress line alignment predictable */
  flex: 1;
  min-width: 0;
}

.step-indicator[aria-disabled="true"] {
  opacity: 0.4;
  cursor: not-allowed;
  pointer-events: none;
}

.step-indicator:focus-visible {
  outline: 2px solid var(--ty-color-primary);
  outline-offset: 4px;
  border-radius: 50%;
}

/* ===================================== */
/* Step Circle - Smaller, cleaner */
/* Expose as CSS Part for circle styling */
/* ===================================== */

.step-circle {
  /* Circle size - set --step-circle-size on ty-wizard element to customize */
  width: var(--step-circle-size, 32px);
  height: var(--step-circle-size, 32px);
  border-radius: 50%;
  border: 2px solid;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  z-index: 10;
  transition: all var(--transition-duration, 300ms) var(--transition-easing, ease-in-out);
  box-sizing: border-box;
  flex-shrink: 0;
}

/* Completed state - green with success glow */
.step-circle[data-state="completed"] {
  background: var(--ty-color-success);
  border-color: var(--ty-color-success-600, var(--ty-color-success));
  color: white;
  box-shadow: 0 0 0 4px rgba(34, 197, 94, 0.1);
}

/* Active state - primary with primary glow */
.step-circle[data-state="active"] {
  background: var(--ty-color-primary);
  border-color: var(--ty-color-primary-600, var(--ty-color-primary));
  color: white;
  box-shadow: 0 0 0 4px rgba(99, 102, 241, 0.1);
}

/* Pending state - neutral gray */
.step-circle[data-state="pending"] {
  background: var(--ty-surface-elevated, #f3f4f6);
  border-color: var(--ty-border, #d1d5db);
  color: var(--ty-text-soft, #6b7280);
}

/* Error state - red with danger glow */
.step-circle[data-state="error"] {
  background: var(--ty-color-danger, #ef4444);
  border-color: var(--ty-color-danger-600, #dc2626);
  color: white;
  box-shadow: 0 0 0 4px rgba(239, 68, 68, 0.1);
}

/* ===================================== */
/* Step Text Container */
/* Wraps label and description */
/* ===================================== */

.step-text {
  display: flex;
  flex-direction: column;
  gap: 2px;
  align-items: center;
}

/* ===================================== */
/* Step Label - Main title */
/* ===================================== */

.step-label {
  font-size: var(--ty-font-sm, 14px);
  font-weight: var(--ty-font-semibold, 600);
  color: var(--ty-text, inherit);
  transition: color 200ms;
}

.step-indicator[aria-selected="true"] .step-label {
  color: var(--ty-text-strong, inherit);
}

.step-indicator[aria-selected="false"] .step-label {
  color: var(--ty-text-soft, inherit);
}

/* ===================================== */
/* Step Description - Subtitle */
/* ===================================== */

.step-description {
  font-size: var(--ty-font-xs, 12px);
  font-weight: var(--ty-font-normal, 400);
  color: var(--ty-text-soft, #9ca3af);
  transition: color 200ms;
  text-align: center;
  max-width: 120px;
}

.step-indicator[aria-selected="true"] .step-description {
  color: var(--ty-text, inherit);
}

/* ===================================== */
/* Custom Indicator Content via Slots */
/* ===================================== */

::slotted([slot^="indicator-"]) {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
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
  background: var(--ty-surface-elevated);
}

/* ===================================== */
/* Panels Wrapper (slides horizontally) */
/* ===================================== */

.panels-wrapper {
  display: flex;
  height: 100%;
  transition: transform var(--transition-duration, 300ms) var(--transition-easing, ease-in-out);
  will-change: transform;
}

/* ===================================== */
/* Slotted Step Panels */
/* ===================================== */

::slotted(ty-step) {
  width: var(--wizard-width, 100%);
  height: 100%;
  flex-shrink: 0;
}

/* ===================================== */
/* Accessibility & Motion Preferences */
/* ===================================== */

/* Respect user's motion preferences */
@media (prefers-reduced-motion: reduce) {
  .panels-wrapper {
    transition-duration: 0ms !important;
  }

  .progress-overlay {
    transition-duration: 0ms !important;
  }

  .step-circle {
    transition-duration: 0ms !important;
  }

  .progress-line {
    transition-duration: 0ms !important;
  }
}

/* Screen reader only content */
.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border-width: 0;
}
`;
