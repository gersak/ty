/**
 * Styles for ty-textarea component
 * Enhanced textarea with auto-resize functionality
 */

export const textareaStyles = `
:host {
  display: block;
  font-family: var(--ty-font-sans);
  width: 100%;
}

.textarea-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  box-sizing: border-box;
  position: relative;
  /* For absolute positioned dummy element */
}

/* ===== LABEL STYLING ===== */

.textarea-label {
  font-size: 14px;
  font-weight: 500;
  color: var(--ty-label-color);
  margin-bottom: 6px;
  line-height: 1.25;
  padding-left: 12px;
}

/* Required indicator - using SVG icon instead of CSS */
.required-icon {
  display: inline-flex;
  align-items: center;
  color: var(--ty-color-danger);
  width: 12px;
  height: 12px;
  vertical-align: middle;
}

/* ===== ERROR MESSAGE STYLING ===== */

.error-message {
  font-size: 12px;
  color: var(--ty-color-danger);
  margin-top: 4px;
  padding-left: 12px;
  line-height: 1.4;
}

/* Error state for textareas */
textarea.error {
  border-color: var(--ty-color-danger);
  background: var(--ty-bg-danger-soft);
}

textarea.error:focus {
  border-color: var(--ty-color-danger-mild);
  box-shadow: 0 0 0 3px var(--ty-input-shadow-focus);
}

/* ===== TEXTAREA BASE STYLING ===== */

textarea {
  /* Base appearance - elegant and minimal like input */
  box-sizing: border-box;
  width: 100%;
  border: 1px solid var(--ty-input-border);
  border-radius: 6px;
  background: var(--ty-input-bg);
  color: var(--ty-input-color);
  font-family: inherit;
  font-size: 14px;
  font-weight: 400;
  line-height: 1.5;
  transition: all 0.15s ease-in-out;
  outline: none;

  /* Default size (md) - refined spacing */
  min-height: 80px;
  /* Larger than input for multiline */
  padding: 12px 12px;
  /* Slightly larger padding for text areas */

  /* Auto-resize specific styles */
  overflow: hidden;
  /* Hide scrollbars since we're auto-resizing */
  resize: none;
  /* Disable manual resize by default */

  /* Ensure consistent text wrapping */
  white-space: pre-wrap;
  word-wrap: break-word;
}

/* Focus state - elegant blue glow like input */
textarea:focus {
  border-color: var(--ty-input-border-focus);
  box-shadow: 0 0 0 3px var(--ty-input-shadow-focus);
  background: var(--ty-input-bg);
}

/* Hover state - subtle feedback */
textarea:hover:not(:disabled) {
  border-color: var(--ty-input-border-hover);
}

/* Disabled state - refined opacity */
textarea:disabled {
  cursor: not-allowed;
  opacity: 0.5;
  background: var(--ty-input-disabled-bg);
  border-color: var(--ty-input-disabled-border);
  color: var(--ty-input-disabled-color);
}

/* Placeholder styling - subtle and elegant */
textarea::placeholder {
  color: var(--ty-input-placeholder);
  font-weight: 400;
}

/* ===== RESIZE CONTROL OPTIONS ===== */

/* Allow manual resizing */
textarea.resize-both {
  resize: both;
}

textarea.resize-horizontal {
  resize: horizontal;
}

textarea.resize-vertical {
  resize: vertical;
}

textarea.resize-none {
  resize: none;
}

/* Auto-resize mode (default) disables manual resize */
textarea:not(.resize-both):not(.resize-horizontal):not(.resize-vertical) {
  resize: none;
}

/* ===== DUMMY ELEMENT FOR AUTO-RESIZE ===== */

.textarea-dummy {
  /* Hidden element that measures text height */
  position: absolute !important;
  top: 0 !important;
  left: 0 !important;
  visibility: hidden !important;
  white-space: pre-wrap !important;
  word-break: break-word !important;
  box-sizing: border-box !important;
  overflow: hidden !important;
  pointer-events: none !important;
  z-index: -1 !important;

  /* Ensure it has the same text behavior as textarea */
  word-wrap: break-word !important;
  overflow-wrap: break-word !important;
}

/* ===== SIZE MODIFIERS ===== */

/* Extra Small */
textarea.xs {
  min-height: 64px;
  padding: 8px 10px;
  font-size: 12px;
}

/* Small */
textarea.sm {
  min-height: 72px;
  padding: 10px 10px;
  font-size: 13px;
}

/* Medium (default) */
textarea.md {
  min-height: 80px;
  padding: 12px 12px;
  font-size: 14px;
}

/* Large */
textarea.lg {
  min-height: 96px;
  padding: 14px 14px;
  font-size: 16px;
}

/* Extra Large */
textarea.xl {
  min-height: 112px;
  padding: 16px 16px;
  font-size: 18px;
}

/* ===== ACCESSIBILITY ENHANCEMENTS ===== */

textarea:focus-visible {
  outline: none;
  /* We use box-shadow instead */
}

/* High contrast mode support */
@media (prefers-contrast: high) {
  textarea {
    border-width: 2px;
  }
}

/* Reduced motion support */
@media (prefers-reduced-motion: reduce) {
  textarea {
    transition: none;
  }

  .textarea-dummy {
    transition: none;
  }
}

/* ===== CONTAINER-AWARE RESPONSIVE BEHAVIOR ===== */

/* Scale down on smaller containers using container queries */
@container (max-width: 480px) {
  textarea.lg {
    font-size: 14px;
    padding: 12px 12px;
    min-height: 80px;
  }

  textarea.xl {
    font-size: 16px;
    padding: 14px 14px;
    min-height: 96px;
  }
}

@container (max-width: 320px) {
  textarea.xl {
    font-size: 14px;
    padding: 12px 12px;
    min-height: 80px;
  }
}

/* Fallback for browsers without container query support */
@media (max-width: 640px) {
  textarea.lg {
    font-size: 14px;
    padding: 12px 12px;
    min-height: 80px;
  }

  textarea.xl {
    font-size: 16px;
    padding: 14px 14px;
    min-height: 96px;
  }
}

@media (max-width: 480px) {
  textarea.xl {
    font-size: 14px;
    padding: 12px 12px;
    min-height: 80px;
  }
}

/* ===== ANIMATION AND TRANSITIONS ===== */

/* Smooth height transitions for auto-resize */
textarea {
  transition:
    border-color 0.15s ease-in-out,
    box-shadow 0.15s ease-in-out,
    background-color 0.15s ease-in-out,
    height 0.1s ease-out;
  /* Smooth height changes */
}

/* Disable height transition on focus to avoid jarring effect */
textarea:focus {
  transition:
    border-color 0.15s ease-in-out,
    box-shadow 0.15s ease-in-out,
    background-color 0.15s ease-in-out;
}

/* For users who prefer reduced motion, disable height transitions */
@media (prefers-reduced-motion: reduce) {
  textarea {
    transition: none;
  }
}
`
