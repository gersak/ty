/**
 * Tooltip Component
 * 
 * Shows helpful content on hover/focus with smart positioning.
 * Follows the same shadow DOM pattern as other components.
 * 
 * @example
 * <ty-tooltip placement="top" flavor="primary" delay="600">
 *   Helpful tooltip text
 * </ty-tooltip>
 */

import { findBestPosition, placementPreferences, type Placement, type CleanupFn } from '../utils/positioning.js';
import { ensureStyles } from '../utils/styles.js';
import { tooltipStyles } from '../styles/tooltip.js';

// ============================================================================
// Types
// ============================================================================

/**
 * Valid tooltip flavors (semantic colors)
 */
export type TooltipFlavor =
  | 'dark'
  | 'light'
  | 'primary'
  | 'secondary'
  | 'success'
  | 'danger'
  | 'warning'
  | 'info'
  | 'neutral';

/**
 * Tooltip attributes configuration
 */
export interface TooltipAttributes {
  placement: Placement;
  offset: number;
  delay: number;
  disabled: boolean;
  flavor: TooltipFlavor;
}

/**
 * Timeout state for show/hide delays
 */
interface TimeoutState {
  showTimeout: number | null;
  hideTimeout: number | null;
}

// ============================================================================
// WeakMaps for State Management
// ============================================================================

const autoUpdateCleanup = new WeakMap<TyTooltip, CleanupFn>();
const eventCleanup = new WeakMap<TyTooltip, CleanupFn>();
const timeoutState = new WeakMap<TyTooltip, TimeoutState>();
const popoverElements = new WeakMap<TyTooltip, HTMLElement>();

// ============================================================================
// Helper Functions
// ============================================================================

/**
 * Validate and normalize flavor attribute
 */
function validateFlavor(flavor: string | null): TooltipFlavor {
  const validFlavors: TooltipFlavor[] = [
    'dark', 'light', 'primary', 'secondary',
    'success', 'danger', 'warning', 'info', 'neutral'
  ];
  const normalized = (flavor || 'dark') as TooltipFlavor;

  if (!validFlavors.includes(normalized)) {
    console.warn(
      `[ty-tooltip] Invalid flavor '${flavor}'. Using 'dark'. ` +
      `Valid flavors: ${validFlavors.join(', ')}.`
    );
    return 'dark';
  }

  return normalized;
}

/**
 * Get timeout state for element
 */
function getTimeoutState(el: TyTooltip): TimeoutState {
  let state = timeoutState.get(el);
  if (!state) {
    state = { showTimeout: null, hideTimeout: null };
    timeoutState.set(el, state);
  }
  return state;
}

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
 * Read all tooltip attributes from element
 */
function getTooltipAttributes(el: TyTooltip): TooltipAttributes {
  return {
    placement: (el.getAttribute('placement') || 'top') as Placement,
    offset: parseIntAttr(el, 'offset', 8),
    delay: parseIntAttr(el, 'delay', 600),
    disabled: parseBoolAttr(el, 'disabled'),
    flavor: validateFlavor(el.getAttribute('flavor')),
  };
}

/**
 * Get the parent element (anchor)
 */
function getAnchorElement(el: TyTooltip): HTMLElement | null {
  return el.parentElement;
}

/**
 * Get or create popover element using Popover API
 * The popover is created in document.body for top-layer placement
 */
function getOrCreatePopover(el: TyTooltip): HTMLElement {
  let popover = popoverElements.get(el);
  
  if (!popover) {
    // Create popover element
    popover = document.createElement('div');
    popover.id = `ty-tooltip-${Math.random().toString(36).substr(2, 9)}`;
    popover.setAttribute('popover', 'manual');
    popover.className = 'ty-tooltip-popover';
    
    // Get initial attributes
    const { flavor } = getTooltipAttributes(el);
    popover.setAttribute('data-flavor', flavor);
    
    // Copy content from slot
    const content = el.textContent || '';
    popover.textContent = content;
    
    // Apply inline styles (since popover is outside shadow DOM, we need inline styles)
    const styles = `
      position: fixed;
      margin: 0;
      padding: 6px 10px;
      background: var(--ty-bg-neutral-soft, #4b5563);
      color: var(--ty-color-neutral-strong, #f3f4f6);
      border: 2px solid var(--ty-border-strong, #6b7280);
      border-radius: 4px;
      font-size: var(--ty-font-sm, 14px);
      font-weight: var(--ty-font-semibold, 600);
      line-height: 1.5;
      max-width: 250px;
      box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -2px rgba(0, 0, 0, 0.1);
      user-select: none;
      pointer-events: none;
    `;
    
    popover.style.cssText = styles;
    
    // Apply flavor-specific styles
    applyFlavorStyles(popover, flavor);
    
    // Append to body
    document.body.appendChild(popover);
    
    // Store reference
    popoverElements.set(el, popover);
  }
  
  return popover;
}

/**
 * Apply flavor-specific styles to popover
 */
function applyFlavorStyles(popover: HTMLElement, flavor: TooltipFlavor): void {
  // Reset to defaults first
  popover.style.removeProperty('background');
  popover.style.removeProperty('color');
  popover.style.removeProperty('border-color');
  
  // Apply flavor-specific styles
  switch (flavor) {
    case 'primary':
      popover.style.background = 'var(--ty-bg-primary, #3b82f6)';
      popover.style.color = 'var(--ty-color-primary-strong, #eff6ff)';
      popover.style.borderColor = 'var(--ty-border-primary, #60a5fa)';
      break;
    case 'secondary':
      popover.style.background = 'var(--ty-bg-secondary, #8b5cf6)';
      popover.style.color = 'var(--ty-color-secondary-strong, #f5f3ff)';
      popover.style.borderColor = 'var(--ty-border-secondary, #a78bfa)';
      break;
    case 'success':
      popover.style.background = 'var(--ty-bg-success-mild, #10b981)';
      popover.style.color = 'var(--ty-color-success-strong, #ecfdf5)';
      popover.style.borderColor = 'var(--ty-border-success, #34d399)';
      break;
    case 'danger':
      popover.style.background = 'var(--ty-bg-danger, #ef4444)';
      popover.style.color = 'var(--ty-color-danger-strong, #fef2f2)';
      popover.style.borderColor = 'var(--ty-border-danger, #f87171)';
      break;
    case 'warning':
      popover.style.background = 'var(--ty-bg-warning, #f59e0b)';
      popover.style.color = 'var(--ty-color-warning-strong, #fffbeb)';
      popover.style.borderColor = 'var(--ty-border-warning, #fbbf24)';
      break;
    case 'info':
      popover.style.background = 'var(--ty-bg-info, #06b6d4)';
      popover.style.color = 'var(--ty-text-strong, #f0f9ff)';
      popover.style.borderColor = 'var(--ty-border-info, #22d3ee)';
      break;
    case 'light':
      popover.style.background = 'var(--ty-surface-elevated, #ffffff)';
      popover.style.color = 'var(--ty-text-strong, #111827)';
      popover.style.borderColor = 'var(--ty-border, #e5e7eb)';
      break;
    case 'neutral':
      popover.style.background = 'var(--ty-bg-neutral, #6b7280)';
      popover.style.color = 'var(--ty-color-neutral-strong, #f9fafb)';
      popover.style.borderColor = 'var(--ty-border-neutral, #9ca3af)';
      break;
    case 'dark':
    default:
      popover.style.background = 'var(--ty-bg-neutral-soft, #4b5563)';
      popover.style.color = 'var(--ty-color-neutral-strong, #f3f4f6)';
      popover.style.borderColor = 'var(--ty-border-strong, #6b7280)';
      break;
  }
}

/**
 * Update tooltip position based on current anchor/popover state
 */
function updatePosition(el: TyTooltip): void {
  const { placement, offset } = getTooltipAttributes(el);
  const anchor = getAnchorElement(el);
  const popover = popoverElements.get(el);

  if (!anchor || !popover) {
    console.warn('[ty-tooltip] updatePosition: missing anchor or popover');
    return;
  }

  // Calculate preferences based on placement
  const preferences = placement === 'top' ? placementPreferences.tooltip :
    placement === 'bottom' ? ['bottom', 'top', 'left', 'right'] as Placement[] :
      placement === 'left' ? ['left', 'right', 'top', 'bottom'] as Placement[] :
        placement === 'right' ? ['right', 'left', 'top', 'bottom'] as Placement[] :
          placementPreferences.tooltip;

  // Use positioning engine to find best position
  const position = findBestPosition({
    targetEl: anchor,
    floatingEl: popover,
    preferences,
    offset,
  });

  // Apply position directly to popover
  popover.style.left = `${position.x}px`;
  popover.style.top = `${position.y}px`;
}

/**
 * Cleanup auto-update system
 */
function cleanupAutoUpdate(el: TyTooltip): void {
  const cleanup = autoUpdateCleanup.get(el);
  if (cleanup) {
    cleanup();
    autoUpdateCleanup.delete(el);
  }
}

/**
 * Setup auto-update system for position tracking
 * Note: This does NOT calculate initial position - call updatePosition() separately
 */
function setupAutoUpdate(el: TyTooltip): void {
  const anchor = getAnchorElement(el);
  const popover = popoverElements.get(el);

  if (!anchor || !popover) {
    console.warn('[ty-tooltip] setupAutoUpdate: missing anchor or popover');
    return;
  }

  // Debounced update function
  let timeoutId: number | null = null;
  const debouncedUpdate = () => {
    if (timeoutId !== null) {
      clearTimeout(timeoutId);
    }
    timeoutId = window.setTimeout(() => {
      timeoutId = null;
      updatePosition(el);
    }, 10);
  };

  // Setup ResizeObserver for anchor and popover
  const resizeObserver = new ResizeObserver(debouncedUpdate);
  resizeObserver.observe(anchor);
  resizeObserver.observe(popover);

  // Scroll handler with requestAnimationFrame
  let scrollRafId: number | null = null;
  const handleScroll = () => {
    if (scrollRafId === null) {
      scrollRafId = requestAnimationFrame(() => {
        scrollRafId = null;
        updatePosition(el);
      });
    }
  };

  // Listen for scroll and resize
  window.addEventListener('scroll', handleScroll, true);
  window.addEventListener('resize', debouncedUpdate);

  // Store cleanup function
  const cleanup = () => {
    resizeObserver.disconnect();
    window.removeEventListener('scroll', handleScroll, true);
    window.removeEventListener('resize', debouncedUpdate);
    if (timeoutId !== null) {
      clearTimeout(timeoutId);
    }
    if (scrollRafId !== null) {
      cancelAnimationFrame(scrollRafId);
    }
  };

  autoUpdateCleanup.set(el, cleanup);
}

/**
 * Clear all timeouts
 */
function clearTimeouts(el: TyTooltip): void {
  const state = getTimeoutState(el);
  if (state.showTimeout !== null) {
    clearTimeout(state.showTimeout);
    state.showTimeout = null;
  }
  if (state.hideTimeout !== null) {
    clearTimeout(state.hideTimeout);
    state.hideTimeout = null;
  }
}

/**
 * Show tooltip immediately using Popover API
 */
function showTooltip(el: TyTooltip): void {
  const { disabled } = getTooltipAttributes(el);
  if (disabled) return;

  // Create popover if it doesn't exist
  const popover = getOrCreatePopover(el);
  
  try {
    // Show using Popover API
    popover.showPopover();
    el._open = true;
    
    // Position and setup observers
    updatePosition(el);
    setupAutoUpdate(el);
  } catch (e) {
    console.warn('[ty-tooltip] Failed to show popover', e);
  }
}

/**
 * Hide tooltip immediately using Popover API
 */
function hideTooltip(el: TyTooltip): void {
  const popover = popoverElements.get(el);
  if (!popover) return;
  
  try {
    popover.hidePopover();
    el._open = false;
    cleanupAutoUpdate(el);
  } catch (e) {
    // Already hidden, ignore
  }
}

/**
 * Schedule tooltip to show after delay
 */
function scheduleShow(el: TyTooltip): void {
  const state = getTimeoutState(el);
  const { delay } = getTooltipAttributes(el);

  clearTimeouts(el);
  state.showTimeout = window.setTimeout(() => showTooltip(el), delay);
}

/**
 * Schedule tooltip to hide after delay
 */
function scheduleHide(el: TyTooltip): void {
  const state = getTimeoutState(el);

  clearTimeouts(el);
  state.hideTimeout = window.setTimeout(() => hideTooltip(el), 200);
}

/**
 * Setup event listeners on anchor element
 */
function setupEvents(el: TyTooltip): void {
  const anchor = getAnchorElement(el);
  if (!anchor) return;

  const handleEnter = () => scheduleShow(el);
  const handleLeave = () => scheduleHide(el);
  const handleFocus = () => scheduleShow(el);
  const handleBlur = () => scheduleHide(el);

  // Add event listeners
  anchor.addEventListener('mouseenter', handleEnter);
  anchor.addEventListener('mouseleave', handleLeave);
  anchor.addEventListener('focusin', handleFocus);
  anchor.addEventListener('focusout', handleBlur);

  // Store cleanup function
  eventCleanup.set(el, () => {
    anchor.removeEventListener('mouseenter', handleEnter);
    anchor.removeEventListener('mouseleave', handleLeave);
    anchor.removeEventListener('focusin', handleFocus);
    anchor.removeEventListener('focusout', handleBlur);
  });
}

/**
 * Cleanup all resources including popover element
 */
function cleanup(el: TyTooltip): void {
  clearTimeouts(el);
  cleanupAutoUpdate(el);

  const cleanup = eventCleanup.get(el);
  if (cleanup) {
    cleanup();
    eventCleanup.delete(el);
  }

  // Remove popover from body
  const popover = popoverElements.get(el);
  if (popover) {
    popover.remove();
    popoverElements.delete(el);
  }

  timeoutState.delete(el);
}

// ============================================================================
// Component Definition
// ============================================================================

/**
 * TyTooltip Web Component
 */
export class TyTooltip extends HTMLElement {
  /** Internal open state */
  _open = false;

  /** Observed attributes */
  static get observedAttributes() {
    return ['placement', 'offset', 'delay', 'disabled', 'flavor'];
  }

  constructor() {
    super();
    this.attachShadow({ mode: 'open' });
  }

  connectedCallback() {
    // Initialize open state
    this._open = false;

    // Inject styles (for :host display: contents)
    ensureStyles(this.shadowRoot!, tooltipStyles);

    // Setup events on anchor
    setupEvents(this);
  }

  disconnectedCallback() {
    cleanup(this);
  }

  attributeChangedCallback(name: string, _oldValue: string | null, newValue: string | null) {
    // Update flavor in real-time if tooltip is visible
    if (name === 'flavor' && this._open) {
      const popover = popoverElements.get(this);
      if (popover) {
        const flavor = validateFlavor(newValue);
        applyFlavorStyles(popover, flavor);
      }
    }

    // Close tooltip if disabled
    if (name === 'disabled' && newValue !== null) {
      hideTooltip(this);
    }
  }
}

// Register the custom element
if (!customElements.get('ty-tooltip')) {
  customElements.define('ty-tooltip', TyTooltip);
}
