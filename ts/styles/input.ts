/**
 * Input Component Styles
 * PORTED FROM: clj/ty/components/input.css
 * Phase A: Regular input styles only (no checkbox, no numeric formatting)
 */

export const inputStyles = `
:host {
  display: block;
  font-family: var(--ty-font-sans);
  width: 100%;
}

.input-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  box-sizing: border-box;
}

/* ===== LABEL STYLING ===== */

.input-label {
  font-size: 14px;
  font-weight: 500;
  color: var(--ty-label-color);
  margin-bottom: 6px;
  line-height: 1.25;
  padding-left: 12px;
  display: flex;
  align-items: center;
}

/* Required indicator - using SVG icon */
.required-icon {
  display: inline-flex;
  align-items: center;
  color: var(--ty-color-danger);
  width: 12px;
  height: 12px;
  margin-left: 4px;
  vertical-align: middle;
}

.required-icon svg {
  width: 100%;
  height: 100%;
}

/* ===== INPUT WRAPPER WITH SLOTS ===== */

.input-wrapper {
  display: flex;
  align-items: center;
  gap: 0.5rem; /* 8px - normal gap for icon spacing */
  width: 100%;
  box-sizing: border-box;
  border: 1px solid var(--ty-input-border);
  border-radius: 6px;
  background: var(--ty-input-bg);
  transition: all 0.15s ease-in-out;
  
  /* Default size (md) */
  min-height: 40px;
  padding: 0 12px;
}

/* Wrapper states */
.input-wrapper:hover:not(.disabled) {
  border-color: var(--ty-input-border-hover);
}

.input-wrapper.focused {
  border-color: var(--ty-input-border-focus);
  box-shadow: 0 0 0 3px var(--ty-input-shadow-focus);
}

.input-wrapper.disabled {
  cursor: not-allowed;
  opacity: 0.5;
  background: var(--ty-input-disabled-bg);
  border-color: var(--ty-input-disabled-border);
}

/* ===== SLOT STYLING ===== */

.input-start,
.input-end {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  color: var(--ty-color-text-soft);
}

.input-start ::slotted(*),
.input-end ::slotted(*) {
  display: flex;
  align-items: center;
  justify-content: center;
}

/* Icon sizing for slotted icons */
.input-start ::slotted(ty-icon),
.input-end ::slotted(ty-icon) {
  width: 1em;
  height: 1em;
  flex-shrink: 0;
}

/* ===== ERROR MESSAGE STYLING ===== */

.error-message {
  font-size: 12px;
  color: var(--ty-color-danger);
  margin-top: 4px;
  padding-left: 12px;
  line-height: 1.4;
}

/* Error state for wrapper */
.input-wrapper.error {
  border-color: var(--ty-color-danger);
  background: var(--ty-bg-danger-soft);
}

.input-wrapper.error.focused {
  border-color: var(--ty-color-danger-mild);
  box-shadow: 0 0 0 3px var(--ty-input-shadow-focus);
}

/* ===== INPUT BASE STYLING ===== */

input {
  /* Reset and base styles */
  flex: 1;
  min-width: 0;
  box-sizing: border-box;
  border: none;
  outline: none;
  background: transparent;
  color: var(--ty-input-color);
  font-family: inherit;
  font-size: 14px;
  font-weight: 400;
  line-height: 1.5;
  padding: 0;
  margin: 0;
}

/* Remove number input spinner arrows */
input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

input[type="number"] {
  -moz-appearance: textfield;
}

/* Disabled state */
input:disabled {
  cursor: not-allowed;
  color: var(--ty-input-disabled-color);
}

/* Placeholder styling */
input::placeholder {
  color: var(--ty-input-placeholder);
  font-weight: 400;
}

/* ===== SIZE MODIFIERS ===== */

/* Extra Small */
.input-wrapper.xs {
  min-height: 32px;
  padding: 0 8px;
}

.input-wrapper.xs input {
  font-size: 12px;
}

/* Small */
.input-wrapper.sm {
  min-height: 36px;
  padding: 0 10px;
}

.input-wrapper.sm input {
  font-size: 13px;
}

/* Medium (default) */
.input-wrapper.md {
  min-height: 40px;
  padding: 0 12px;
}

.input-wrapper.md input {
  font-size: 14px;
}

/* Large */
.input-wrapper.lg {
  min-height: 44px;
  padding: 0 14px;
}

.input-wrapper.lg input {
  font-size: 16px;
}

/* Extra Large */
.input-wrapper.xl {
  min-height: 48px;
  padding: 0 16px;
}

.input-wrapper.xl input {
  font-size: 18px;
}

/* ===== SEMANTIC FLAVOR MODIFIERS ===== */

/* Primary */
.input-wrapper.primary {
  border-color: var(--ty-input-primary-border, var(--ty-color-primary));
}

.input-wrapper.primary:hover:not(.disabled) {
  border-color: var(--ty-color-primary-mild);
}

.input-wrapper.primary.focused {
  border-color: var(--ty-color-primary-mild);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

/* Secondary */
.input-wrapper.secondary {
  border-color: var(--ty-input-secondary-border, var(--ty-color-secondary));
}

.input-wrapper.secondary.focused {
  border-color: var(--ty-color-secondary-mild);
  box-shadow: 0 0 0 3px rgba(139, 92, 246, 0.1);
}

/* Success */
.input-wrapper.success {
  border-color: var(--ty-input-success-border);
}

.input-wrapper.success:hover:not(.disabled) {
  border-color: var(--ty-color-success-mild);
}

.input-wrapper.success.focused {
  border-color: var(--ty-color-success-mild);
  box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.1);
}

/* Danger */
.input-wrapper.danger {
  border-color: var(--ty-input-danger-border);
}

.input-wrapper.danger:hover:not(.disabled) {
  border-color: var(--ty-color-danger-mild);
}

.input-wrapper.danger.focused {
  border-color: var(--ty-color-danger-mild);
  box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1);
}

/* Warning */
.input-wrapper.warning {
  border-color: var(--ty-input-warning-border);
}

.input-wrapper.warning:hover:not(.disabled) {
  border-color: var(--ty-color-warning-mild);
}

.input-wrapper.warning.focused {
  border-color: var(--ty-color-warning-mild);
  box-shadow: 0 0 0 3px rgba(245, 158, 11, 0.1);
}

/* Neutral (default) */
.input-wrapper.neutral.focused {
  border-color: var(--ty-input-border-focus);
  box-shadow: 0 0 0 3px var(--ty-input-shadow-focus);
}

/* ===== ACCESSIBILITY ENHANCEMENTS ===== */

input:focus-visible {
  outline: none;
}

/* High contrast mode support */
@media (prefers-contrast: high) {
  .input-wrapper {
    border-width: 2px;
  }
}

/* Reduced motion support */
@media (prefers-reduced-motion: reduce) {
  .input-wrapper {
    transition: none;
  }
}

/* ===== RESPONSIVE BEHAVIOR ===== */

@media (max-width: 640px) {
  .input-wrapper.lg {
    min-height: 40px;
    padding: 0 12px;
  }

  .input-wrapper.lg input {
    font-size: 14px;
  }

  .input-wrapper.xl {
    min-height: 44px;
    padding: 0 14px;
  }

  .input-wrapper.xl input {
    font-size: 16px;
  }
}

@media (max-width: 480px) {
  .input-wrapper.xl {
    min-height: 40px;
    padding: 0 12px;
  }

  .input-wrapper.xl input {
    font-size: 14px;
  }
}
`
