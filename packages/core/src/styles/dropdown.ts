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
  width: calc(100% - 16px);
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
  transition: var(--ty-transition-all);
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

/* Size-specific clearable adjustments */
.dropdown-stub.xs.clearable.has-selection {
  padding-right: calc(var(--ty-spacing-2) + 2rem + var(--ty-spacing-2));
}

.dropdown-stub.sm.clearable.has-selection {
  padding-right: calc(var(--ty-spacing-2) + 2rem + var(--ty-spacing-2));
}

.dropdown-stub.md.clearable.has-selection {
  padding-right: calc(var(--ty-spacing-3) + 2rem + var(--ty-spacing-3));
}

.dropdown-stub.lg.clearable.has-selection {
  padding-right: calc(var(--ty-spacing-4) + 2rem + var(--ty-spacing-4));
}

.dropdown-stub.xl.clearable.has-selection {
  padding-right: calc(var(--ty-spacing-5) + 2rem + var(--ty-spacing-5));
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

/* ===== CLEAR BUTTON ===== */

.dropdown-clear-btn {
  position: absolute;
  top: 50%;
  right: calc(var(--ty-spacing-3) + 1rem + var(--ty-spacing-2)); /* Before chevron */
  transform: translateY(-50%);
  width: 1rem;
  height: 1rem;
  padding: 0;
  border: none;
  background: none;
  color: var(--ty-input-placeholder);
  cursor: pointer;
  transition: var(--ty-transition-colors);
  display: none; /* Hidden by default, shown via JS */
}

.dropdown-clear-btn:hover {
  color: var(--ty-color-danger);
}

.dropdown-clear-btn:active {
  transform: translateY(-50%) scale(0.9);
}

.dropdown-clear-btn svg {
  width: 100%;
  height: 100%;
  display: block;
}

/* Adjust stub padding when clearable AND has selection */
.dropdown-stub.clearable.has-selection {
  padding-right: calc(var(--ty-spacing-3) + 2rem + var(--ty-spacing-3)); /* Room for X + chevron */
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

/* ===== DIALOG STRUCTURE - MODAL-LIKE DESIGN ===== */

.dropdown-dialog {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  border: none;
  background: transparent;
  z-index: 9999;
  
  /* Hidden by default */
  opacity: 0;
  pointer-events: none;
  transition: opacity 200ms ease;
}

.dropdown-dialog.open {
  opacity: 1;
  pointer-events: auto;
}

/* Modal-like backdrop */
.dropdown-dialog::backdrop {
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(4px);
  -webkit-backdrop-filter: blur(4px);
}

/* ===== FLOATING CONTENT PANEL ===== */

.dropdown-panel {
  position: absolute;
  display: flex;
  flex-direction: column;
  background: var(--ty-surface-floating);
  border-radius: var(--ty-radius-lg);
  box-shadow: 
    0 20px 25px -5px rgba(0, 0, 0, 0.1),
    0 10px 10px -5px rgba(0, 0, 0, 0.04),
    var(--ty-shadow-xl);
  overflow: hidden;
  
  /* Positioning will be set dynamically via CSS variables */
  left: var(--dropdown-x);
  top: var(--dropdown-y);
  width: var(--dropdown-width);
  max-width: calc(100vw - 16px);
  max-height: min(400px, calc(100vh - var(--dropdown-y) - 16px));
  
  /* Animation */
  opacity: 0;
  transform: scale(0.95) translateY(-10px);
  transition: 
    opacity 200ms cubic-bezier(0.16, 1, 0.3, 1),
    transform 200ms cubic-bezier(0.16, 1, 0.3, 1);
}

.dropdown-dialog.open .dropdown-panel {
  opacity: 1;
  transform: scale(1) translateY(0);
}

/* Position above variant */
.dropdown-dialog.position-above .dropdown-panel {
  top: auto;
  bottom: var(--dropdown-y);
  transform: scale(0.95) translateY(10px);
}

.dropdown-dialog.position-above.open .dropdown-panel {
  transform: scale(1) translateY(0);
}

/* ===== CLOSE BUTTON (X) ===== */

.dropdown-close-btn {
  position: absolute;
  top: 12px;
  right: 12px;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  border: none;
  border-radius: 50%;
  color: var(--ty-text-);
  cursor: pointer;
  transition: var(--ty-transition-all);
  z-index: 10;
  padding: 0;
}

.dropdown-close-btn:hover {
  background: var(--ty-bg-neutral-soft);
  color: var(--ty-text);
}

.dropdown-close-btn:active {
  transform: scale(0.9);
}

.dropdown-close-btn svg {
  width: 20px;
  height: 20px;
}

/* ===== PANEL HEADER WITH SEARCH ===== */

.dropdown-header {
  padding: 16px;
  padding-right: 52px; /* Room for close button */
  border-bottom: 1px solid var(--ty-border-);
  flex-shrink: 0;
}

.dropdown-search-input {
  width: 100%;
  min-width: 0;
  box-sizing: border-box;
  background: var(--ty-surface-input);
  color: var(--ty-text);
  border: 1px solid var(--ty-border);
  border-radius: var(--ty-radius-md);
  font-family: var(--ty-font-sans);
  font-size: var(--ty-font-sm);
  font-weight: var(--ty-font-normal);
  line-height: var(--ty-line-height-tight);
  padding: var(--ty-spacing-2) var(--ty-spacing-3);
  transition: var(--ty-transition-all);
  outline: none;
}

.dropdown-search-input:focus {
  border-color: var(--ty-border-primary);
  box-shadow: 0 0 0 2px var(--ty-bg-primary-soft);
}

.dropdown-search-input:disabled {
  background-color: var(--ty-surface-elevated);
  color: var(--ty-text--);
  cursor: not-allowed;
  opacity: 0.6;
}

.dropdown-search-input::placeholder {
  color: var(--ty-text--);
}

/* Search input size variants */
.dropdown-search-input.xs {
  font-size: var(--ty-font-xs);
  padding: var(--ty-spacing-1) var(--ty-spacing-2);
}

.dropdown-search-input.sm {
  font-size: var(--ty-font-sm);
  padding: var(--ty-spacing-1) var(--ty-spacing-2);
}

.dropdown-search-input.md {
  font-size: var(--ty-font-sm);
  padding: var(--ty-spacing-2) var(--ty-spacing-3);
}

.dropdown-search-input.lg {
  font-size: var(--ty-font-base);
  padding: var(--ty-spacing-2) var(--ty-spacing-3);
}

.dropdown-search-input.xl {
  font-size: var(--ty-font-lg);
  padding: var(--ty-spacing-3) var(--ty-spacing-4);
}

/* ===== OPTIONS CONTAINER ===== */

.dropdown-options {
  flex: 1;
  overflow-x: hidden;
  overflow-y: auto;
  scroll-behavior: smooth;
  box-sizing: border-box;
  background: var(--ty-surface-floating);
  padding: 8px 0;
}

/* Custom scrollbar for options */
.dropdown-options::-webkit-scrollbar {
  width: 8px;
}

.dropdown-options::-webkit-scrollbar-track {
  background: transparent;
}

.dropdown-options::-webkit-scrollbar-thumb {
  background: var(--ty-border-);
  border-radius: 4px;
}

.dropdown-options::-webkit-scrollbar-thumb:hover {
  background: var(--ty-border);
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
  margin: 0 8px;
  color: var(--ty-text);
  cursor: pointer;
  transition: var(--ty-transition-all);
  border: none;
  background: none;
  font-size: inherit;
  font-family: inherit;
  width: calc(100% - 16px);
  text-align: left;
  box-sizing: border-box;
  border-radius: var(--ty-radius-sm);
}

.dropdown-options ::slotted(option:hover) {
  background-color: var(--ty-bg-neutral-soft);
}

.dropdown-options ::slotted(option[highlighted]) {
  background-color: var(--ty-bg-primary-soft);
  color: var(--ty-color-primary);
}

.dropdown-options ::slotted(option[selected]) {
  background-color: var(--ty-bg-primary);
  color: var(--ty-text++);
}

/* Hidden state applies to all option types */
.dropdown-options ::slotted(option[hidden]),
.dropdown-options ::slotted(ty-option[hidden]),
.dropdown-options ::slotted(ty-tag[hidden]) {
  display: none;
}

/* ===== FLAVOR VARIANTS ===== */

:host([flavor="primary"]) .dropdown-stub {
  border-color: var(--ty-border-primary);
}

:host([flavor="primary"]) .dropdown-stub:hover {
  border-color: var(--ty-color-primary);
}

:host([flavor="primary"]) .dropdown-search-input:focus {
  border-color: var(--ty-color-primary);
  box-shadow: 0 0 0 2px var(--ty-bg-primary-soft);
}

:host([flavor="secondary"]) .dropdown-stub {
  border-color: var(--ty-border-secondary);
}

:host([flavor="secondary"]) .dropdown-stub:hover {
  border-color: var(--ty-color-secondary);
}

:host([flavor="secondary"]) .dropdown-search-input:focus {
  border-color: var(--ty-color-secondary);
  box-shadow: 0 0 0 2px var(--ty-bg-secondary-soft);
}

:host([flavor="success"]) .dropdown-stub {
  border-color: var(--ty-border-success);
}

:host([flavor="success"]) .dropdown-stub:hover {
  border-color: var(--ty-color-success);
}

:host([flavor="success"]) .dropdown-search-input:focus {
  border-color: var(--ty-color-success);
  box-shadow: 0 0 0 2px var(--ty-bg-success-soft);
}

:host([flavor="danger"]) .dropdown-stub {
  border-color: var(--ty-border-danger);
}

:host([flavor="danger"]) .dropdown-stub:hover {
  border-color: var(--ty-color-danger);
}

:host([flavor="danger"]) .dropdown-search-input:focus {
  border-color: var(--ty-color-danger);
  box-shadow: 0 0 0 2px var(--ty-bg-danger-soft);
}

:host([flavor="warning"]) .dropdown-stub {
  border-color: var(--ty-border-warning);
}

:host([flavor="warning"]) .dropdown-stub:hover {
  border-color: var(--ty-color-warning);
}

:host([flavor="warning"]) .dropdown-search-input:focus {
  border-color: var(--ty-color-warning);
  box-shadow: 0 0 0 2px var(--ty-bg-warning-soft);
}

/* ===== MOBILE MODE STYLES ===== */

/* Mobile modal container - full screen overlay */
.mobile-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100vw;
  height: 100vh;
  z-index: 9999;
  display: flex;
  align-items: flex-start; /* Align to top instead of center */
  justify-content: center;
  padding-top: 10vh; /* Fixed position from top */
  opacity: 0;
  pointer-events: none;
}

.mobile-modal.open {
  opacity: 1;
  pointer-events: auto;
}

/* Mobile backdrop - full viewport */
.mobile-modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  z-index: 1;
}

/* Mobile content container - clean floating design */
.mobile-modal-content {
  position: relative;
  z-index: 2;
  display: flex;
  flex-direction: column;
  width: calc(100% - 32px);
  max-width: 400px;
  min-height: 200px; /* Minimum height to prevent jumping */
  max-height: calc(90vh - 10vh); /* Account for top padding */
  /* No background - transparent */
  opacity: 0;
  transform: scale(0.95);
  transition: 
    opacity 300ms cubic-bezier(0.16, 1, 0.3, 1),
    transform 300ms cubic-bezier(0.16, 1, 0.3, 1);
}

.mobile-modal.open .mobile-modal-content {
  opacity: 1;
  transform: scale(1);
}

/* Mobile search header - floating row with search and close */
.mobile-search-header {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  padding: 0;
  background: transparent;
  position: relative;
}

.mobile-header-content {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 100%;
}

/* Header for non-searchable (close button only) */
.mobile-header-nosearch {
  flex-shrink: 0;
  display: flex;
  justify-content: flex-end;
  margin-bottom: 16px;
  padding: 0;
  background: transparent;
  position: relative;
  min-height: 40px;
}

/* Close button styling - inline with search */
.mobile-close-button {
  flex-shrink: 0;
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--ty-surface-floating);
  border: 1px solid var(--ty-border-);
  border-radius: 50%;
  color: var(--ty-text-);
  cursor: pointer;
  transition: var(--ty-transition-all);
  padding: 0;
}

.mobile-close-button:hover {
  background: var(--ty-bg-neutral);
  border-color: var(--ty-border);
  color: var(--ty-text);
}

.mobile-close-button:active {
  transform: scale(0.9);
}

.mobile-close-button svg {
  width: 20px;
  height: 20px;
}

.mobile-search-input {
  flex: 1;
  min-width: 0;
  box-sizing: border-box;
  background: var(--ty-surface-floating);
  color: var(--ty-text);
  border: 1px solid var(--ty-border-);
  border-radius: var(--ty-radius-md);
  font-family: var(--ty-font-sans);
  font-size: var(--ty-font-sm);
  font-weight: var(--ty-font-normal);
  line-height: var(--ty-line-height-tight);
  padding: var(--ty-spacing-2) var(--ty-spacing-3);
  height: 40px;
  transition: var(--ty-transition-all);
  outline: none;
}

.mobile-search-input:focus {
  border-color: var(--ty-border-primary);
  box-shadow: 0 0 0 2px var(--ty-bg-primary-soft);
}

.mobile-search-input::placeholder {
  color: var(--ty-text--);
}

/* Mobile options container */
.mobile-options-container {
  position: relative;
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  -webkit-overflow-scrolling: touch;
  background: var(--ty-surface-floating);
  border-radius: var(--ty-radius-lg);
  border: 1px solid var(--ty-border-);
  box-shadow: 
    0 20px 25px -5px rgba(0, 0, 0, 0.1),
    0 10px 10px -5px rgba(0, 0, 0, 0.04);
  padding: 8px 0;
}

/* Hide scrollbar on mobile but keep scroll functionality */
.mobile-options-container {
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE/Edge */
}

.mobile-options-container::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera */
}







/* Mobile option styling - native <option> only */
.mobile-options-container ::slotted(option) {
  display: block;
  padding: var(--ty-spacing-2) var(--ty-spacing-3);
  margin: 2px 8px;
  color: var(--ty-text);
  cursor: pointer;
  transition: var(--ty-transition-all);
  border: none;
  background: transparent;
  font-size: var(--ty-font-sm);
  font-family: inherit;
  width: 100%;
  text-align: left;
  box-sizing: border-box;
  border-radius: var(--ty-radius-sm);
  min-height: 36px;
}



.mobile-options-container ::slotted(option:hover),
.mobile-options-container ::slotted(option:active) {
  background-color: var(--ty-bg-neutral-soft);
}

.mobile-options-container ::slotted(option[highlighted]) {
  background-color: var(--ty-bg-primary-soft);
  color: var(--ty-color-primary);
}

.mobile-options-container ::slotted(option[selected]) {
  background-color: var(--ty-bg-primary);
  color: var(--ty-text++);
}

.mobile-options-container ::slotted(option[hidden]) {
  display: none;
}
`
