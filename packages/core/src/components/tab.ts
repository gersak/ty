/**
 * Tab Component
 * 
 * Individual tab panel component that contains content for a single tab.
 * Acts as a container for panel content with proper ARIA attributes and independent scrolling.
 * The parent ty-tabs component handles orchestration and visibility control.
 * 
 * Features:
 * - Simple content container with proper ARIA roles
 * - Independent scrolling within fixed viewport
 * - Scroll position resets when tab becomes active
 * - Disabled state prevents tab activation
 * - Smooth opacity transitions when switching tabs
 * 
 * @example
 * <ty-tab id="general" label="General Settings">
 *   <div class="p-6">
 *     <h3>General Settings</h3>
 *     <p>Configure general options here.</p>
 *   </div>
 * </ty-tab>
 * 
 * @example
 * <!-- Disabled tab -->
 * <ty-tab id="premium" label="Premium Features" disabled>
 *   <div class="p-6">Upgrade to access premium features.</div>
 * </ty-tab>
 */

import { ensureStyles } from '../utils/styles.js';
import { tabStyles } from '../styles/tab.js';

// ============================================================================
// Types
// ============================================================================

/**
 * Tab attributes configuration
 */
export interface TabAttributes {
  id: string | null;      // Required unique identifier
  label: string | null;   // Simple text label
  disabled: boolean;      // Whether the tab is disabled
}

// ============================================================================
// Helper Functions
// ============================================================================

/**
 * Extract tab configuration from element attributes
 */
function getTabAttributes(el: TyTab): TabAttributes {
  return {
    id: el.getAttribute('id'),
    label: el.getAttribute('label'),
    disabled: el.hasAttribute('disabled'),
  };
}

// ============================================================================
// Rendering
// ============================================================================

/**
 * Render the tab - just acts as a container for panel content.
 * The label rendering is handled by the parent ty-tabs component.
 */
function render(el: TyTab): void {
  const shadowRoot = el.shadowRoot;
  if (!shadowRoot) return;
  
  const { id } = getTabAttributes(el);
  
  // Ensure styles are loaded
  ensureStyles(shadowRoot, { css: tabStyles, id: 'ty-tab' });
  
  // Simple structure - just a scrollable container for panel content
  // The parent ty-tabs will handle positioning and sizing via CSS variables
  shadowRoot.innerHTML = `
    <div class="tab-panel"
         role="tabpanel"
         ${id ? `id="panel-${id}"` : ''}
         ${id ? `aria-labelledby="tab-${id}"` : ''}
    >
      <slot></slot>
    </div>
  `;
}

/**
 * Cleanup when tab is disconnected
 */
function cleanup(_el: TyTab): void {
  // Nothing to cleanup for now
}

// ============================================================================
// Component Definition
// ============================================================================

/**
 * TyTab Web Component
 */
export class TyTab extends HTMLElement {
  /** Observed attributes */
  static get observedAttributes() {
    return ['id', 'label', 'disabled'];
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
  
  attributeChangedCallback(_name: string, _oldValue: string | null, _newValue: string | null) {
    render(this);
  }
}

// Register the custom element
if (!customElements.get('ty-tab')) {
  customElements.define('ty-tab', TyTab);
}
