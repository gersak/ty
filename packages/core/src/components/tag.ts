/**
 * TyTag Web Component
 * PORTED FROM: clj/ty/components/tag.cljs
 * Tag component with slots and semantic flavors
 */

import type { Flavor, Size, TyTagElement, TyTagEventDetail } from '../types/common.js'
import { ensureStyles } from '../utils/styles.js'
import { tagStyles } from '../styles/tag.js'

/**
 * Ty Tag Component
 * 
 * @example
 * ```html
 * <ty-tag flavor="primary">Primary Tag</ty-tag>
 * <ty-tag flavor="success" dismissible>Success Tag</ty-tag>
 * <ty-tag flavor="danger" clickable>Clickable Tag</ty-tag>
 * <ty-tag value="tag-1" selected>Selected Tag</ty-tag>
 * ```
 */
export class TyTag extends HTMLElement implements TyTagElement {
  private _flavor: Flavor = 'neutral'
  private _size: Size = 'md'
  private _value: string | undefined = undefined
  private _selected = false
  private _pill = true // Default to pill shape
  private _clickable = false
  private _dismissible = false
  private _disabled = false

  // Event listener cleanup function
  private _cleanup: (() => void) | null = null

  constructor() {
    super()

    const shadow = this.attachShadow({ mode: 'open' })
    ensureStyles(shadow, { css: tagStyles, id: 'ty-tag' })

    this.render()
  }

  static get observedAttributes(): string[] {
    return [
      'size', 'flavor', 'pill', 'not-pill', 'clickable',
      'dismissible', 'disabled', 'value', 'selected'
    ]
  }

  connectedCallback(): void {
    // CRITICAL: Reagent/React may set properties BEFORE the element is constructed
    // Check if value was set directly on the instance before our getter/setter was available
    const instanceValue = Object.getOwnPropertyDescriptor(this, 'value')
    if (instanceValue && instanceValue.value !== undefined) {
      this._value = instanceValue.value
      // Clean up the instance property so our getter/setter works
      delete this.value
    }

    this.render()
    this.setupEventListeners()
  }

  disconnectedCallback(): void {
    this.cleanupEventListeners()
  }

  attributeChangedCallback(name: string, oldValue: string | null, newValue: string | null): void {
    if (oldValue === newValue) return

    switch (name) {
      case 'flavor':
        this._flavor = this.validateFlavor(newValue)
        break
      case 'size':
        this._size = (newValue as Size) || 'md'
        break
      case 'value':
        this._value = newValue || undefined
        break
      case 'selected':
        this._selected = newValue !== null
        break
      case 'pill':
        if (newValue !== null) {
          this._pill = newValue !== 'false'
        }
        break
      case 'not-pill':
        if (newValue !== null) {
          this._pill = false
        }
        break
      case 'clickable':
        this._clickable = newValue !== null
        break
      case 'dismissible':
        this._dismissible = newValue !== null
        break
      case 'disabled':
        this._disabled = newValue !== null
        break
    }

    this.render()
    // Re-setup event listeners after render
    if (this.isConnected) {
      this.setupEventListeners()
    }
  }

  /**
   * Validate flavor (matches ClojureScript validation)
   */
  private validateFlavor(flavor: string | null): Flavor {
    const validFlavors: Flavor[] = ['primary', 'secondary', 'success', 'danger', 'warning', 'neutral']
    const normalized = (flavor || 'neutral') as Flavor

    if (!validFlavors.includes(normalized)) {
      console.warn(
        `[ty-tag] Invalid flavor '${flavor}'. Using 'neutral'. ` +
        `Valid flavors: ${validFlavors.join(', ')}`
      )
      return 'neutral'
    }

    return normalized
  }

  // Public API - Getters/Setters
  // NOTE: All setters trigger re-render to support property changes from frameworks like React

  get flavor(): Flavor {
    return this._flavor
  }

  set flavor(value: Flavor) {
    if (this._flavor !== value) {
      this._flavor = this.validateFlavor(value)
      this.setAttribute('flavor', value)
      this.render()
    }
  }

  get size(): Size {
    return this._size
  }

  set size(value: Size) {
    if (this._size !== value) {
      this._size = value
      this.setAttribute('size', value)
      this.render()
    }
  }

  get value(): string | undefined {
    return this._value
  }

  set value(val: string | undefined) {
    if (this._value !== val) {
      this._value = val
      if (val !== undefined) {
        this.setAttribute('value', val)
      } else {
        this.removeAttribute('value')
      }
      this.render()
    }
  }

  get selected(): boolean {
    return this._selected
  }

  set selected(value: boolean) {
    if (this._selected !== value) {
      this._selected = value
      if (value) {
        this.setAttribute('selected', '')
      } else {
        this.removeAttribute('selected')
      }
      this.render()
    }
  }

  get pill(): boolean {
    return this._pill
  }

  set pill(value: boolean) {
    if (this._pill !== value) {
      this._pill = value
      if (value) {
        this.setAttribute('pill', '')
        this.removeAttribute('not-pill')
      } else {
        this.setAttribute('pill', 'false')
        this.setAttribute('not-pill', 'true')
      }
      this.render()
    }
  }

  get clickable(): boolean {
    return this._clickable
  }

  set clickable(value: boolean) {
    if (this._clickable !== value) {
      this._clickable = value
      if (value) {
        this.setAttribute('clickable', '')
      } else {
        this.removeAttribute('clickable')
      }
      this.render()
      if (this.isConnected) {
        this.setupEventListeners()
      }
    }
  }

  get dismissible(): boolean {
    return this._dismissible
  }

  set dismissible(value: boolean) {
    if (this._dismissible !== value) {
      this._dismissible = value
      if (value) {
        this.setAttribute('dismissible', '')
      } else {
        this.removeAttribute('dismissible')
      }
      this.render()
      if (this.isConnected) {
        this.setupEventListeners()
      }
    }
  }

  get disabled(): boolean {
    return this._disabled
  }

  set disabled(value: boolean) {
    if (this._disabled !== value) {
      this._disabled = value
      if (value) {
        this.setAttribute('disabled', '')
      } else {
        this.removeAttribute('disabled')
      }
      this.render()
    }
  }

  /**
   * Dispatch custom tag events
   */
  private dispatchTagEvent(eventType: string, detail: TyTagEventDetail): void {
    this.dispatchEvent(new CustomEvent(eventType, {
      detail,
      bubbles: true,
      composed: true, // Allow event to cross shadow DOM boundaries
      cancelable: true
    }))
  }

  /**
   * Handle tag click events
   */
  private handleClick = (event: Event): void => {
    if (this._clickable && !this._disabled) {
      event.preventDefault()
      event.stopPropagation()
      this.dispatchTagEvent('click', { target: this })
      this.dispatchTagEvent('pointerdown', { target: this })
    }
  }

  /**
   * Handle tag dismiss events
   */
  private handleDismiss = (event: Event): void => {
    if (this._dismissible && !this._disabled) {
      event.preventDefault()
      event.stopPropagation()
      this.dispatchTagEvent('dismiss', { target: this })
    }
  }

  /**
   * Handle keyboard interactions
   */
  private handleKeydown = (event: Event): void => {
    const keyboardEvent = event as KeyboardEvent
    if (this._disabled) return

    const keyCode = keyboardEvent.keyCode

    switch (keyCode) {
      // ENTER or SPACE - trigger click if clickable
      case 13: // Enter
      case 32: // Space
        if (this._clickable) {
          keyboardEvent.preventDefault()
          this.dispatchTagEvent('click', { target: this })
        }
        break

      // DELETE or BACKSPACE - trigger dismiss if dismissible
      case 8:  // Backspace
      case 46: // Delete
        if (this._dismissible) {
          keyboardEvent.preventDefault()
          this.dispatchTagEvent('dismiss', { target: this })
        }
        break
    }
  }

  /**
   * Clean up existing event listeners
   */
  private cleanupEventListeners(): void {
    if (this._cleanup) {
      this._cleanup()
      this._cleanup = null
    }
  }

  /**
   * Setup event listeners for tag interactions
   */
  private setupEventListeners(): void {
    // Clean up any existing listeners first
    this.cleanupEventListeners()

    const shadow = this.shadowRoot!
    const container = shadow.querySelector('.tag-container')
    const dismissBtn = shadow.querySelector('.tag-dismiss')

    if (!container) return

    // Click handler for main tag
    container.addEventListener('click', this.handleClick)
    container.addEventListener('keydown', this.handleKeydown)

    // Dismiss button handler
    if (dismissBtn) {
      dismissBtn.addEventListener('click', this.handleDismiss)
    }

    // Store cleanup function
    this._cleanup = () => {
      container.removeEventListener('click', this.handleClick)
      container.removeEventListener('keydown', this.handleKeydown)
      if (dismissBtn) {
        dismissBtn.removeEventListener('click', this.handleDismiss)
      }
    }
  }

  /**
   * Render the tag component
   */
  private render(): void {
    const shadow = this.shadowRoot!

    // Ensure value is also set as attribute for CSS selectors and debugging
    if (this._value && !this.hasAttribute('value')) {
      this.setAttribute('value', this._value)
    }

    // Build the tag container
    const tabindex = this._clickable ? ' tabindex="0"' : ''
    const ariaDisabled = this._disabled ? ' aria-disabled="true"' : ''
    const dataValue = this._value ? ` data-value="${this._value}"` : ''

    // Build dismiss button SVG
    const dismissButton = this._dismissible ? `
      <button class="tag-dismiss" type="button" aria-label="Remove tag">
        <svg viewBox="0 0 20 20" fill="currentColor">
          <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
        </svg>
      </button>
    ` : ''

    shadow.innerHTML = `
      <div class="tag-container"${tabindex}${ariaDisabled}${dataValue}>
        <div class="tag-start">
          <slot name="start" class="tag-start"></slot>
        </div>
        <div class="tag-content">
          <slot></slot>
        </div>
        <div class="tag-end">
          <slot name="end"></slot>
          ${dismissButton}
        </div>
      </div>
    `
  }
}

// Register the custom element
if (!customElements.get('ty-tag')) {
  customElements.define('ty-tag', TyTag)
}