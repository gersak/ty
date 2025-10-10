/**
 * Popup Component
 * 
 * Interactive popup component with dropdown-like behavior and tooltip positioning.
 * Uses parent-child relationship - popup opens on click, stays open for interaction,
 * closes on outside click or ESC key.
 * 
 * @example
 * <button>
 *   Click me
 *   <ty-popup placement="bottom" offset="8">
 *     <div class="ty-elevated p-4 rounded-lg">
 *       Popup content here
 *     </div>
 *   </ty-popup>
 * </button>
 */

import { findBestPosition, type Placement } from '../utils/positioning.js';
import { lockScroll, unlockScroll } from '../utils/scroll-lock.js';
import { ensureStyles } from '../utils/styles.js';
import { popupStyles } from '../styles/popup.js';

// ============================================================================
// Types
// ============================================================================

/**
 * Popup attributes configuration
 */
export interface PopupAttributes {
  manual: boolean;        // Override click trigger
  disableClose: boolean;  // Disable auto-close
  placement: Placement;   // Position preference
  offset: number;         // Spacing from anchor
}

// ============================================================================
// WeakMaps for State Management
// ============================================================================

const anchorClickHandlers = new WeakMap<TyPopup, (e: Event) => void>();
const outsideClickHandlers = new WeakMap<TyPopup, (e: Event) => void>();
const escapeHandlers = new WeakMap<TyPopup, (e: KeyboardEvent) => void>();
const closeRequestHandlers = new WeakMap<TyPopup, (e: CustomEvent) => void>();
const popupIds = new WeakMap<TyPopup, string>(); // Store popup ID for scroll locking

// ============================================================================
// Helper Functions
// ============================================================================

/**
 * Parse boolean attribute
 */
function parseBoolAttr(el: Element, name: string): boolean {
  return el.hasAttribute(name);
}

/**
 * Parse integer attribute
 */
function parseIntAttr(el: Element, name: string, defaultValue: number): number {
  const value = el.getAttribute(name);
  if (value === null) return defaultValue;
  const parsed = parseInt(value, 10);
  return isNaN(parsed) ? defaultValue : parsed;
}

/**
 * Read popup attributes with simplified API
 */
function getPopupAttributes(el: TyPopup): PopupAttributes {
  return {
    manual: parseBoolAttr(el, 'manual'),
    disableClose: parseBoolAttr(el, 'disable-close'),
    placement: (el.getAttribute('placement') || 'bottom') as Placement,
    offset: parseIntAttr(el, 'offset', 8),
  };
}

/**
 * Get the parent element as anchor (like tooltip)
 */
function getAnchorElement(el: TyPopup): HTMLElement | null {
  return el.parentElement;
}

/**
 * Get the popup dialog element
 */
function getPopupDialog(shadowRoot: ShadowRoot): HTMLDialogElement | null {
  return shadowRoot.querySelector<HTMLDialogElement>('.popup-dialog');
}

/**
 * Get or create a unique popup ID for scroll locking
 */
function getPopupId(el: TyPopup): string {
  let id = popupIds.get(el);
  if (!id) {
    id = `popup-${el.id || Math.random().toString(36).substr(2, 9)}`;
    popupIds.set(el, id);
  }
  return id;
}

/**
 * Calculate and update popup position based on anchor (tooltip style)
 */
function updatePosition(el: TyPopup): void {
  const { placement, offset } = getPopupAttributes(el);
  const shadowRoot = el.shadowRoot;
  const anchor = getAnchorElement(el);
  const dialog = shadowRoot ? getPopupDialog(shadowRoot) : null;

  if (!anchor || !dialog) return;

  // Calculate preferred placements based on placement attribute (like tooltip)
  const preferences: Placement[] =
    placement === 'top' ? ['top', 'bottom', 'left', 'right'] :
    placement === 'bottom' ? ['bottom', 'top', 'left', 'right'] :
    placement === 'left' ? ['left', 'right', 'top', 'bottom'] :
    placement === 'right' ? ['right', 'left', 'top', 'bottom'] :
    ['bottom', 'top', 'right', 'left']; // Default popup placement

  // Use positioning engine to find best position
  const positionData = findBestPosition({
    targetEl: anchor,
    floatingEl: dialog,
    preferences,
    offset,
    padding: 8,
    containerPadding: 16,
  });

  // Update CSS variables with compensated coordinates
  el.style.setProperty('--popup-x', `${positionData.x}px`);
  el.style.setProperty('--popup-y', `${positionData.y}px`);

  // Add position class like dropdown does (this applies the variables)
  dialog.classList.add('position-calculated');
}

/**
 * Close popup with clean animation -> position reset -> hide sequence
 */
function closePopup(el: TyPopup, force = false): void {
  const { disableClose } = getPopupAttributes(el);
  if (!force && disableClose) return;

  const shadowRoot = el.shadowRoot;
  const dialog = shadowRoot ? getPopupDialog(shadowRoot) : null;
  if (!dialog) return;

  // Don't unlock scroll here - let the dialog 'close' event handler do it
  // This prevents double-unlock issues

  dialog.classList.remove('open');
  dialog.classList.remove('preparing-animation');

  setTimeout(() => {
    dialog.classList.remove('position-calculated');
    dialog.close(); // This will trigger 'close' event which unlocks scroll
  }, 150);

  // Dispatch close event
  el.dispatchEvent(new CustomEvent('ty:popup-close', { bubbles: true }));
}

/**
 * Open popup with dialog open but hidden → position → animate sequence
 */
function openPopup(el: TyPopup): void {
  // Lock scroll using the stored popup ID
  const popupId = getPopupId(el);
  lockScroll(popupId);

  const shadowRoot = el.shadowRoot;
  const dialog = shadowRoot ? getPopupDialog(shadowRoot) : null;
  if (!dialog) return;

  // Step 1: showModal() immediately - dialog gets [open] attribute and proper layout
  dialog.showModal();

  // Step 2: Wait for layout to settle, then measure and position
  requestAnimationFrame(() => {
    // Now dialog has [open] and accurate dimensions
    // Calculate and apply position while dialog is hidden by CSS
    updatePosition(el);

    // Step 3: Add scale transform for animation prep
    dialog.classList.add('preparing-animation');

    // Step 4: Third RAF - THEN add open class for smooth scale animation
    requestAnimationFrame(() => {
      // Now animate: scale(0.95) → scale(1) + opacity + visibility
      dialog.classList.add('open');

      // Dispatch open event
      el.dispatchEvent(new CustomEvent('ty:popup-open', { bubbles: true }));
    });
  });
}

/**
 * Toggle popup visibility
 */
function togglePopup(el: TyPopup): void {
  const shadowRoot = el.shadowRoot;
  const dialog = shadowRoot ? getPopupDialog(shadowRoot) : null;
  if (!dialog) return;

  if (dialog.classList.contains('open')) {
    closePopup(el);
  } else {
    openPopup(el);
  }
}

/**
 * Handle click on anchor element
 */
function handleAnchorClick(el: TyPopup, event: Event): void {
  event.preventDefault();
  event.stopPropagation();
  togglePopup(el);
}

/**
 * Handle clicks on the dialog element (backdrop clicks)
 */
function handleOutsideClick(el: TyPopup, event: Event): void {
  event.stopPropagation();
  const shadowRoot = el.shadowRoot;
  const dialog = shadowRoot ? getPopupDialog(shadowRoot) : null;
  
  // Close if clicking on dialog backdrop (not popup content)
  if (dialog && event.target === dialog) {
    event.preventDefault();
    closePopup(el);
  }
}

/**
 * Handle escape key press
 */
function handleEscape(el: TyPopup, event: KeyboardEvent): void {
  event.stopPropagation();
  if (event.key === 'Escape') {
    const { disableClose } = getPopupAttributes(el);
    if (disableClose) {
      event.preventDefault();
    } else {
      closePopup(el);
    }
  }
}

/**
 * Handle close requests from inside popup content
 */
function handleCloseRequest(el: TyPopup, event: CustomEvent): void {
  event.stopPropagation();
  closePopup(el, true);
}

/**
 * Setup click listener on anchor element (unless manual=true)
 */
function setupAnchorEvents(el: TyPopup): void {
  const { manual } = getPopupAttributes(el);
  const anchor = getAnchorElement(el);
  
  if (!anchor || manual) return;

  // Remove any existing listener first
  const existingHandler = anchorClickHandlers.get(el);
  if (existingHandler) {
    anchor.removeEventListener('pointerdown', existingHandler);
  }

  // Create new handler and store reference
  const handler = (e: Event) => handleAnchorClick(el, e);
  anchorClickHandlers.set(el, handler);
  anchor.addEventListener('pointerdown', handler);
}

/**
 * Cleanup anchor event listeners
 */
function cleanupAnchorEvents(el: TyPopup): void {
  const anchor = getAnchorElement(el);
  const handler = anchorClickHandlers.get(el);
  
  if (anchor && handler) {
    anchor.removeEventListener('pointerdown', handler);
    anchorClickHandlers.delete(el);
  }
}

/**
 * Render the popup structure
 */
function render(el: TyPopup): void {
  const shadowRoot = el.shadowRoot;
  if (!shadowRoot) return;

  const existingDialog = getPopupDialog(shadowRoot);

  // Ensure styles are loaded
  ensureStyles(shadowRoot, { css: popupStyles, id: 'ty-popup' });

  // Create structure if it doesn't exist
  if (!existingDialog) {
    const dialog = document.createElement('dialog');
    const container = document.createElement('div');
    const content = document.createElement('slot');

    dialog.className = 'popup-dialog';
    container.className = 'popup-container';
    content.id = 'popup-content';

    shadowRoot.appendChild(dialog);
    dialog.appendChild(container);
    container.appendChild(content);

    // Initialize position
    el.style.setProperty('--popup-x', '0px');
    el.style.setProperty('--popup-y', '0px');

    // Setup event listeners
    const outsideHandler = (e: Event) => handleOutsideClick(el, e);
    outsideClickHandlers.set(el, outsideHandler);
    dialog.addEventListener('pointerdown', outsideHandler);

    const escapeHandler = (e: KeyboardEvent) => handleEscape(el, e);
    escapeHandlers.set(el, escapeHandler);
    el.addEventListener('keydown', escapeHandler);

    // Handle native dialog close event
    dialog.addEventListener('close', () => {
      const popupId = getPopupId(el);
      unlockScroll(popupId);
      dialog.classList.remove('open');
      dialog.classList.remove('preparing-animation');
      el.dispatchEvent(new CustomEvent('ty:popup-close', { bubbles: true }));
    });
  }

  // Add close method to element for programmatic access
  el.closePopup = () => closePopup(el, true);
  el.openPopup = () => openPopup(el);
  el.togglePopup = () => togglePopup(el);

  // Add event listener for close requests from content
  const closeRequestHandler = (e: CustomEvent) => handleCloseRequest(el, e);
  closeRequestHandlers.set(el, closeRequestHandler);
  el.addEventListener('ty:close-popup', closeRequestHandler as EventListener);

  // Setup anchor events
  setupAnchorEvents(el);
}

/**
 * Cleanup all resources when component disconnects
 */
function cleanup(el: TyPopup): void {
  // Force unlock scroll if popup is being removed
  const popupId = popupIds.get(el);
  if (popupId) {
    unlockScroll(popupId);
    popupIds.delete(el);
  }

  // Cleanup anchor events
  cleanupAnchorEvents(el);

  // Cleanup dialog events
  const shadowRoot = el.shadowRoot;
  const dialog = shadowRoot ? getPopupDialog(shadowRoot) : null;
  
  if (dialog) {
    const outsideHandler = outsideClickHandlers.get(el);
    if (outsideHandler) {
      dialog.removeEventListener('pointerdown', outsideHandler);
      outsideClickHandlers.delete(el);
    }
  }

  const escapeHandler = escapeHandlers.get(el);
  if (escapeHandler) {
    el.removeEventListener('keydown', escapeHandler);
    escapeHandlers.delete(el);
  }

  const closeRequestHandler = closeRequestHandlers.get(el);
  if (closeRequestHandler) {
    el.removeEventListener('ty:close-popup', closeRequestHandler as EventListener);
    closeRequestHandlers.delete(el);
  }
}

// ============================================================================
// Component Definition
// ============================================================================

/**
 * TyPopup Web Component
 */
export class TyPopup extends HTMLElement {
  /** Programmatic API methods */
  closePopup?: () => void;
  openPopup?: () => void;
  togglePopup?: () => void;

  /** Observed attributes */
  static get observedAttributes() {
    return ['manual', 'disable-close', 'placement', 'offset'];
  }

  constructor() {
    super();
    this.attachShadow({ mode: 'open' });
  }

  connectedCallback() {
    render(this);
  }

  disconnectedCallback() {
    cleanup(this);
  }

  attributeChangedCallback(name: string, _oldValue: string | null, _newValue: string | null) {
    // Re-render on attribute changes
    render(this);

    // Handle specific attribute changes
    switch (name) {
      case 'placement':
      case 'offset':
        // Update position immediately if open and placement-related attributes change
        render(this);
        break;

      case 'manual':
      case 'disable-close':
        // Recreate anchor events if manual changes
        cleanupAnchorEvents(this);
        setupAnchorEvents(this);
        break;
    }
  }
}

// Register the custom element
if (!customElements.get('ty-popup')) {
  customElements.define('ty-popup', TyPopup);
}
