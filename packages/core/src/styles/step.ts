/**
 * Step Component Styles
 *
 * Styles for the ty-step individual panel component including:
 * - Dynamic sizing based on parent wizard dimensions
 * - Independent scrolling with themed scrollbars
 * - Smooth opacity transitions
 * - Flex shrink prevention for carousel layout
 */

export const stepStyles = `
:host {
  /* Panel takes dimensions from parent ty-wizard via CSS variables */
  width: var(--wizard-width, 100%);
  height: calc(var(--wizard-height, 700px) - var(--indicators-height, 120px));
  display: block;
  box-sizing: border-box;
  flex-shrink: 0;
  /* Prevent panel compression in flex layout */

  /* Smooth opacity transition - synced with carousel animation */
  transition: opacity var(--transition-duration, 400ms) var(--transition-easing, ease-in-out);
}

/* Respect user's motion preferences */
@media (prefers-reduced-motion: reduce) {
  :host {
    transition-duration: 0ms !important;
  }
}

.step-panel {
  width: 100%;
  height: 100%;
  overflow: auto;
  /* Each panel scrolls independently */
  box-sizing: border-box;

  /* Scrollbar styling - consistent with Ty design system */
  scrollbar-width: thin;
  scrollbar-color: var(--ty-border) var(--ty-surface-elevated);
}

/* Webkit scrollbar styling */
.step-panel::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.step-panel::-webkit-scrollbar-track {
  background: var(--ty-surface-elevated);
}

.step-panel::-webkit-scrollbar-thumb {
  background: var(--ty-border);
  border-radius: 4px;
}

.step-panel::-webkit-scrollbar-thumb:hover {
  background: var(--ty-border-strong);
}

/* Inactive steps are still rendered (for carousel) but not interactable */
/* Visibility is controlled by parent transform, interaction by pointer-events and opacity */
`;
