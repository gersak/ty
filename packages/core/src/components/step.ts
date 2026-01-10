/**
 * Step Component
 *
 * Individual step panel component that contains content for a single wizard step.
 * Acts as a container for panel content with proper ARIA attributes and independent scrolling.
 * The parent ty-wizard component handles orchestration and visibility control.
 *
 * Features:
 * - Simple content container with proper ARIA roles
 * - Independent scrolling within fixed viewport
 * - Scroll position resets when step becomes active
 * - Disabled state prevents step activation
 * - Smooth opacity transitions when switching steps
 *
 * @example
 * <ty-step id="account" label="Account Setup">
 *   <div class="p-6">
 *     <h3>Create Your Account</h3>
 *     <form>...</form>
 *   </div>
 * </ty-step>
 *
 * @example
 * <!-- Disabled step -->
 * <ty-step id="premium" label="Premium Features" disabled>
 *   <div class="p-6">Complete previous steps to unlock.</div>
 * </ty-step>
 */

import { ensureStyles } from '../utils/styles.js';
import { stepStyles } from '../styles/step.js';

// ============================================================================
// Types
// ============================================================================

/**
 * Step status - user can override automatic state detection
 */
export type StepStatus = 'completed' | 'active' | 'pending' | 'error';

/**
 * Step attributes configuration
 */
export interface StepAttributes {
  id: string | null;         // Required unique identifier
  label: string | null;      // Main step title
  description: string | null; // Optional subtitle/description
  disabled: boolean;         // Whether the step is disabled
  status: StepStatus | null; // User-controlled status override
}

// ============================================================================
// Helper Functions
// ============================================================================

/**
 * Extract step configuration from element attributes
 */
function getStepAttributes(el: TyStep): StepAttributes {
  const statusAttr = el.getAttribute('status');
  const validStatuses: StepStatus[] = ['completed', 'active', 'pending', 'error'];

  return {
    id: el.getAttribute('id'),
    label: el.getAttribute('label'),
    description: el.getAttribute('description'),
    disabled: el.hasAttribute('disabled'),
    status: statusAttr && validStatuses.includes(statusAttr as StepStatus)
      ? (statusAttr as StepStatus)
      : null,
  };
}

// ============================================================================
// Rendering
// ============================================================================

/**
 * Render the step - just acts as a container for panel content.
 * The label rendering is handled by the parent ty-wizard component.
 */
function render(el: TyStep): void {
  const shadowRoot = el.shadowRoot;
  if (!shadowRoot) return;

  const { id } = getStepAttributes(el);

  // Ensure styles are loaded
  ensureStyles(shadowRoot, { css: stepStyles, id: 'ty-step' });

  // Simple structure - just a scrollable container for panel content
  // The parent ty-wizard will handle positioning and sizing via CSS variables
  shadowRoot.innerHTML = `
    <div class="step-panel"
         role="tabpanel"
         ${id ? `id="panel-${id}"` : ''}
         ${id ? `aria-labelledby="step-${id}"` : ''}
    >
      <slot></slot>
    </div>
  `;
}

/**
 * Cleanup when step is disconnected
 */
function cleanup(_el: TyStep): void {
  // Nothing to cleanup for now
}

// ============================================================================
// Component Definition
// ============================================================================

/**
 * TyStep Web Component
 */
export class TyStep extends HTMLElement {
  /** Observed attributes */
  static get observedAttributes() {
    return ['id', 'label', 'description', 'disabled', 'status'];
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
if (!customElements.get('ty-step')) {
  customElements.define('ty-step', TyStep);
}
