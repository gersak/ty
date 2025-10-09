/**
 * Dropdown Component Styles
 * Ported from: clj/ty/components/dropdown.css
 */

export const dropdownStyles = `
/* ===== HOST ELEMENT STYLES ===== */

:host {
  display: block;
  width: auto;
  min-width: 200px;
}

/* ===== CONTAINER STRUCTURE ===== */

.dropdown-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  box-sizing: border-box;
}

/* ===== LABEL STYLING ===== */

.dropdown-label {
  font-size: 14px;
  font-weight: 500;
  color: var(--ty-label-color);
  margin-bottom: 6px;
  line-height: 1.25;
  padding-left: 12px;
}

/* Required indicator - using SVG icon */
.required-icon {
  display: inline-flex;
  align-items: center;
  color: #ef4444;
  width: 12px;
  height: 12px;
  vertical-align: middle;
  margin-left: 4px;
}

/* ===== WRAPPER STRUCTURE ===== */

.dropdown-wrapper {
  position: relative;
  display: block;
  width: 100%;
}

/* ===== DROPDOWN STUB (CLICKABLE TRIGGER) ===== */

.dropdown-stub {
  width: 100%;
  cursor: pointer;
  box-sizing: border-box;
  position: relative;
  display: flex;
  align-items: center;

  /* Visual styling using ty tokens */
  background: var(--ty-input-bg);
  color: var(--ty-input-color);
  border: 1px solid var(--ty-input-border);
  border-radius: var(--ty-radius-md);

  /* Typography */
  font-family: var(--ty-font-sans);
  font-size: var(--ty-font-sm);
  font-weight: var(--ty-font-normal);
  line-height: var(--ty-line-height-tight);

  /* Default size (md) */
  min-height: var(--ty-size-md);
  padding: var(--ty-spacing-2) var(--ty-spacing-3);
  padding-right: calc(var(--ty-spacing-3) + 1rem + var(--ty-spacing-2));

  /* Transitions */
  transition: var(--ty-transition-all), opacity 0.2s ease;
  outline: none;
}

.dropdown-stub:hover {
  border-color: var(--ty-input-border-hover);
}

.dropdown-stub[disabled] {
  background-color: var(--ty-input-disabled-bg);
  color: var(--ty-input-disabled-color);
  cursor: not-allowed;
  opacity: 0.6;
}

.dropdown-stub:focus {
  border-color: var(--ty-input-border-hover);
}

/* Hide stub when dropdown is open */
.dropdown-wrapper:has(.dropdown-chevron.open) .dropdown-stub {
  opacity: 0;
  pointer-events: none;
}

/* ===== SIZE VARIANTS ===== */

.dropdown-stub.xs {
  min-height: var(--ty-size-xs);
  font-size: var(--ty-font-xs);
  padding: var(--ty-spacing-1) var(--ty-spacing-2);
  padding-right: calc(var(--ty-spacing-2) + 1rem + var(--ty-spacing-1));
}

.dropdown-stub.sm {
  min-height: var(--ty-size-sm);
  font-size: var(--ty-font-sm);
  padding: var(--ty-spacing-1) var(--ty-spacing-2);
  padding-right: calc(var(--ty-spacing-2) + 1rem + var(--ty-spacing-1));
}

.dropdown-stub.md {
  min-height: var(--ty-size-md);
  font-size: var(--ty-font-sm);
  padding: var(--ty-spacing-2) var(--ty-spacing-3);
  padding-right: calc(var(--ty-spacing-3) + 1rem + var(--ty-spacing-2));
}

.dropdown-stub.lg {
  min-height: var(--ty-size-lg);
  font-size: var(--ty-font-base);
  padding: var(--ty-spacing-3) var(--ty-spacing-4);
  padding-right: calc(var(--ty-spacing-4) + 1rem + var(--ty-spacing-3));
}

.dropdown-stub.xl {
  min-height: var(--ty-size-xl);
  font-size: var(--ty-font-lg);
  padding: var(--ty-spacing-4) var(--ty-spacing-5);
  padding-right: calc(var(--ty-spacing-5) + 1rem + var(--ty-spacing-4));
}

/* Placeholder styling */
.dropdown-placeholder {
  color: var(--ty-input-placeholder);
  font-weight: var(--ty-font-light);
  font-size: var(--ty-font-sm);
  font-style: italic;
  pointer-events: none;
}

.dropdown-stub.has-selection .dropdown-placeholder {
  display: none;
}

/* Selected content styling */
.dropdown-stub slot[name="selected"] {
  display: block;
}

.dropdown-stub.has-selection slot[name="selected"] {
  width: 100%;
}

.dropdown-stub slot[name="selected"] * {
  display: block;
  width: 100%;
  padding: 0;
  margin: 0;
  border: none;
  background: none;
  color: inherit;
  font: inherit;
  line-height: inherit;
  outline: none;
  appearance: none;
}

/* ===== CHEVRON INDICATOR ===== */

.dropdown-chevron {
  position: absolute;
  top: 50%;
  right: var(--ty-spacing-3);
  transform: translateY(-50%);
  width: 1rem;
  height: 1rem;
  color: var(--ty-input-placeholder);
  transition: var(--ty-transition-transform);
  pointer-events: none;
}

.dropdown-chevron.open {
  transform: translateY(-50%) rotate(180deg);
}

.dropdown-chevron svg {
  width: 100%;
  height: 100%;
}

/* Hide chevron for read-only dropdowns */
:host([readonly]) .dropdown-chevron {
  display: none;
}

:host([readonly]) .dropdown-stub {
  padding-right: var(--ty-spacing-3);
}

:host([readonly]) .dropdown-stub,
:host([readonly]) .dropdown-stub slot[name="selected"] {
  cursor: initial;
}

/* ===== DIALOG STRUCTURE ===== */

.dropdown-dialog {
  position: fixed;
  flex-direction: column;
  width: var(--dropdown-width, 200px);
  max-width: 100vw;
  margin: 0;
  padding: 0;
  border: none;
  background: transparent;
  box-sizing: border-box;
  padding: var(--dropdown-padding, 20px);

  /* Hidden by default */
  opacity: 0;
  transition: opacity 400ms ease;

  transform: translate(var(--dropdown-offset-x, 0px), var(--dropdown-offset-y, 0px));
  top: -1000px;
  left: -1000px;
}

/* Direction-based positioning */
.dropdown-dialog.position-below {
  left: var(--dropdown-x);
  top: var(--dropdown-y);
}

.dropdown-dialog.position-above {
  left: var(--dropdown-x);
  bottom: var(--dropdown-y);
  top: auto;
  flex-direction: column-reverse;
}

.dropdown-dialog.position-above .dropdown-header {
  margin-top: 4px;
}

.dropdown-dialog.position-below .dropdown-header {
  margin-bottom: 4px;
}

.dropdown-dialog.position-below .dropdown-options {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -2px rgba(0, 0, 0, 0.1), var(--ty-shadow-md);
}

.dropdown-dialog.open {
  display: flex;
  opacity: 1;
}

.dropdown-dialog.open .dropdown-options {
  opacity: 1;
  transform: translateY(0) scale(1);
}

.dropdown-dialog::backdrop {
  background: transparent;
}

/* ===== DIALOG HEADER ===== */

.dropdown-header {
  display: flex;
  align-items: center;
  gap: var(--ty-spacing-2);
  position: relative;
}

.dropdown-search-input {
  width: 100%;
  min-width: 0;
  box-sizing: border-box;
  background: var(--ty-input-bg);
  color: var(--ty-input-color);
  border: 1px solid var(--ty-input-border);
  border-radius: var(--ty-radius-md);
  font-family: var(--ty-font-sans);
  font-size: var(--ty-font-sm);
  font-weight: var(--ty-font-normal);
  line-height: var(--ty-line-height-tight);
  min-height: var(--ty-size-md);
  padding: var(--ty-spacing-2) var(--ty-spacing-3);
  padding-right: calc(var(--ty-spacing-3) + 1rem + var(--ty-spacing-2));
  transition: var(--ty-transition-all);
  outline: none;
}

.dropdown-search-input:focus {
  border-color: var(--ty-input-border-focus);
  box-shadow: 0 0 0 3px var(--ty-input-shadow-focus);
}

.dropdown-search-input:disabled {
  background-color: var(--ty-input-disabled-bg);
  color: var(--ty-input-disabled-color);
  cursor: not-allowed;
  opacity: 0.6;
}

.dropdown-search-input::placeholder {
  color: var(--ty-input-placeholder);
}

/* Search input size variants */
.dropdown-search-input.xs {
  min-height: var(--ty-size-xs);
  font-size: var(--ty-font-xs);
  padding: var(--ty-spacing-1) var(--ty-spacing-2);
  padding-right: calc(var(--ty-spacing-2) + 1rem + var(--ty-spacing-1));
}

.dropdown-search-input.sm {
  min-height: var(--ty-size-sm);
  font-size: var(--ty-font-sm);
  padding: var(--ty-spacing-1) var(--ty-spacing-2);
  padding-right: calc(var(--ty-spacing-2) + 1rem + var(--ty-spacing-1));
}

.dropdown-search-input.md {
  min-height: var(--ty-size-md);
  font-size: var(--ty-font-sm);
  padding: var(--ty-spacing-2) var(--ty-spacing-3);
  padding-right: calc(var(--ty-spacing-3) + 1rem + var(--ty-spacing-2));
}

.dropdown-search-input.lg {
  min-height: var(--ty-size-lg);
  font-size: var(--ty-font-base);
  padding: var(--ty-spacing-3) var(--ty-spacing-4);
  padding-right: calc(var(--ty-spacing-4) + 1rem + var(--ty-spacing-3));
}

.dropdown-search-input.xl {
  min-height: var(--ty-size-xl);
  font-size: var(--ty-font-lg);
  padding: var(--ty-spacing-4) var(--ty-spacing-5);
  padding-right: calc(var(--ty-spacing-5) + 1rem + var(--ty-spacing-4));
}

.dropdown-search-chevron {
  position: absolute;
  top: 50%;
  right: var(--ty-spacing-3);
  transform: translateY(-50%);
  width: 1rem;
  height: 1rem;
  color: var(--ty-input-placeholder);
  transition: var(--ty-transition-transform);
  pointer-events: none;
}

.dropdown-search-chevron.open {
  transform: translateY(-50%) rotate(180deg);
}

.dropdown-search-chevron svg {
  width: 100%;
  height: 100%;
}

/* ===== OPTIONS CONTAINER ===== */

.dropdown-options {
  opacity: 0;
  background: var(--ty-input-bg);
  border: 1px solid var(--ty-input-border);
  border-radius: var(--ty-radius-lg);
  box-shadow: var(--ty-shadow-md);
  max-height: 16rem;
  width: 100%;
  max-width: 100%;
  overflow-x: hidden;
  overflow-y: auto;
  scroll-behavior: smooth;
  box-sizing: border-box;
  box-shadow:
    0 20px 25px -5px rgba(0, 0, 0, 0.1),
    0 10px 10px -5px rgba(0, 0, 0, 0.04);

  /* Animation */
  transform: translateY(-20px) scale(0.90);
  transition:
    opacity 100ms cubic-bezier(0.16, 1, 0.3, 1),
    transform 200ms cubic-bezier(0.16, 1, 0.3, 1);
}

/* ===== OPTION ELEMENTS ===== */

/* 
 * STYLING STRATEGY:
 * - <option>: Native HTML element - needs default styling for usability
 * - <ty-option>: Custom element - NO default styling, users have full control
 * - <ty-tag>: Custom element - NO default styling, users have full control
 */

/* Default styling for native <option> elements only */
.dropdown-options ::slotted(option) {
  padding: var(--ty-spacing-2) var(--ty-spacing-3);
  color: var(--ty-input-color);
  cursor: pointer;
  transition:
    var(--ty-transition-colors),
    transform 150ms cubic-bezier(0.16, 1, 0.3, 1);
  border: none;
  background: none;
  font-size: inherit;
  font-family: inherit;
  width: 100%;
  text-align: left;
  box-sizing: border-box;
  transform: translateX(0);
}

.dropdown-options ::slotted(option:hover) {
  background-color: var(--ty-bg-neutral-soft);
}

.dropdown-options ::slotted(option[highlighted]) {
  background-color: var(--ty-bg-primary-soft);
  color: var(--ty-color-primary-mild);
}

.dropdown-options ::slotted(option[selected]) {
  background-color: var(--ty-color-primary);
  color: #ffffff;
}

/* Hidden state applies to all option types */
.dropdown-options ::slotted(option[hidden]),
.dropdown-options ::slotted(ty-option[hidden]),
.dropdown-options ::slotted(ty-tag[hidden]) {
  display: none;
}

/* ===== FLAVOR VARIANTS ===== */

:host([flavor="primary"]) .dropdown-stub,
:host([flavor="primary"]) .dropdown-search-input {
  border-color: var(--ty-color-primary);
}

:host([flavor="primary"]) .dropdown-stub:hover,
:host([flavor="primary"]) .dropdown-search-input:focus {
  border-color: var(--ty-color-primary-mild);
  box-shadow: 0 0 0 3px var(--ty-color-primary-faint);
}

:host([flavor="secondary"]) .dropdown-stub,
:host([flavor="secondary"]) .dropdown-search-input {
  border-color: var(--ty-color-secondary);
}

:host([flavor="secondary"]) .dropdown-stub:hover,
:host([flavor="secondary"]) .dropdown-search-input:focus {
  border-color: var(--ty-color-secondary-mild);
  box-shadow: 0 0 0 3px var(--ty-color-secondary-faint);
}

:host([flavor="success"]) .dropdown-stub,
:host([flavor="success"]) .dropdown-search-input {
  border-color: var(--ty-color-success);
}

:host([flavor="success"]) .dropdown-stub:hover,
:host([flavor="success"]) .dropdown-search-input:focus {
  border-color: var(--ty-color-success-mild);
  box-shadow: 0 0 0 3px var(--ty-color-success-faint);
}

:host([flavor="danger"]) .dropdown-stub,
:host([flavor="danger"]) .dropdown-search-input {
  border-color: var(--ty-color-danger);
}

:host([flavor="danger"]) .dropdown-stub:hover,
:host([flavor="danger"]) .dropdown-search-input:focus {
  border-color: var(--ty-color-danger-mild);
  box-shadow: 0 0 0 3px var(--ty-color-danger-faint);
}

:host([flavor="warning"]) .dropdown-stub,
:host([flavor="warning"]) .dropdown-search-input {
  border-color: var(--ty-color-warning);
}

:host([flavor="warning"]) .dropdown-stub:hover,
:host([flavor="warning"]) .dropdown-search-input:focus {
  border-color: var(--ty-color-warning-mild);
  box-shadow: 0 0 0 3px var(--ty-color-warning-faint);
}

/* ===== MOBILE MODE STYLES ===== */

/* Mobile modal container - full screen overlay */
.mobile-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
  display: flex;
  flex-direction: column;
  opacity: 0;
  transition: opacity 300ms ease;
  pointer-events: none;
}

.mobile-modal.open {
  opacity: 1;
  pointer-events: auto;
}

/* Mobile backdrop */
.mobile-modal-backdrop {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 1;
}

/* Mobile content container */
.mobile-modal-content {
  position: relative;
  z-index: 2;
  display: flex;
  flex-direction: column;
  height: 100%;
  background: var(--ty-surface-elevated);
  transform: translateY(100%);
  transition: transform 300ms cubic-bezier(0.16, 1, 0.3, 1);
}

.mobile-modal.open .mobile-modal-content {
  transform: translateY(0);
}

/* Mobile search header - only shown when searchable */
.mobile-search-header {
  flex-shrink: 0;
  padding: 16px;
  border-bottom: 1px solid var(--ty-border);
  background: var(--ty-surface-content);
}

.mobile-header-content {
  display: flex;
  align-items: center;
  gap: 12px;
}

/* Header for non-searchable (close button only) */
.mobile-header-nosearch {
  flex-shrink: 0;
  padding: 16px;
  border-bottom: 1px solid var(--ty-border);
  background: var(--ty-surface-content);
  display: flex;
  justify-content: flex-end;
}

/* Close button styling */
.mobile-close-button {
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  border: none;
  border-radius: var(--ty-radius-md);
  color: var(--ty-color-text);
  cursor: pointer;
  transition: var(--ty-transition-colors);
  padding: 0;
}

.mobile-close-button:hover {
  background: var(--ty-bg-neutral-soft);
}

.mobile-close-button:active {
  background: var(--ty-bg-neutral);
  transform: scale(0.95);
}

.mobile-close-button svg {
  width: 24px;
  height: 24px;
}

.mobile-search-input {
  flex: 1;
  min-width: 0;
  min-height: 48px; /* Touch-friendly */
  font-size: 16px;   /* Prevents zoom on iOS */
  box-sizing: border-box;
  background: var(--ty-input-bg);
  color: var(--ty-input-color);
  border: 1px solid var(--ty-input-border);
  border-radius: var(--ty-radius-md);
  font-family: var(--ty-font-sans);
  font-weight: var(--ty-font-normal);
  line-height: var(--ty-line-height-tight);
  padding: 12px 16px;
  transition: var(--ty-transition-all);
  outline: none;
}

.mobile-search-input:focus {
  border-color: var(--ty-input-border-focus);
  box-shadow: 0 0 0 3px var(--ty-input-shadow-focus);
}

.mobile-search-input::placeholder {
  color: var(--ty-input-placeholder);
}

/* Mobile options container */
.mobile-options-container {
  flex: 1;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
  background: var(--ty-surface-elevated);
}

/* Mobile option styling - native <option> only */
.mobile-options-container ::slotted(option) {
  display: flex;
  align-items: center;
  min-height: 48px; /* Touch-friendly */
  padding: 12px 16px;
  color: var(--ty-input-color);
  cursor: pointer;
  transition: var(--ty-transition-colors);
  border: none;
  background: none;
  font-size: 16px;
  font-family: inherit;
  width: 100%;
  text-align: left;
  box-sizing: border-box;
  border-bottom: 1px solid var(--ty-border-soft);
}

.mobile-options-container ::slotted(option:last-child) {
  border-bottom: none;
}

.mobile-options-container ::slotted(option:active) {
  background-color: var(--ty-bg-neutral-soft);
}

.mobile-options-container ::slotted(option[highlighted]) {
  background-color: var(--ty-bg-primary-soft);
  color: var(--ty-color-primary-mild);
}

.mobile-options-container ::slotted(option[selected]) {
  background-color: var(--ty-color-primary);
  color: #ffffff;
  font-weight: var(--ty-font-medium);
}

.mobile-options-container ::slotted(option[hidden]) {
  display: none;
}
`
