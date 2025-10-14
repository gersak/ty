/**
 * TyOption Web Component
 * PORTED FROM: clj/ty/components/option.cljs
 * 
 * Ty Option Web Component - Rich HTML alternative to native <option>
 * Enhanced with robust property-attribute synchronization for framework compatibility.
 */

import type { TyOptionElement } from '../types/common.js'
import { ensureStyles } from '../utils/styles.js'
import { optionStyles } from '../styles/option.js'

/**
 * Ty Option Component
 * 
 * Rich HTML alternative to native <option> element.
 * Supports HTML content, styling, and framework property binding.
 * 
 * @example
 * ```html
 * <ty-option value="1">Simple Option</ty-option>
 * <ty-option value="2" selected>Selected Option</ty-option>
 * <ty-option value="3" disabled>Disabled Option</ty-option>
 * <ty-option value="4">
 *   <strong>Rich</strong> <em>HTML</em> Content
 * </ty-option>
 * ```
 */
export class TyOption extends HTMLElement implements TyOptionElement {
  private _value: string | undefined = undefined
  private _selected = false
  private _disabled = false
  private _highlighted = false
  private _hidden = false

  constructor() {
    super()

    const shadow = this.attachShadow({ mode: 'open' })
    ensureStyles(shadow, { css: optionStyles, id: 'ty-option' })

    this.render()
  }

  static get observedAttributes(): string[] {
    return ['value', 'disabled', 'selected', 'highlighted', 'hidden']
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
  }

  attributeChangedCallback(name: string, oldValue: string | null, newValue: string | null): void {
    if (oldValue === newValue) return

    switch (name) {
      case 'value':
        this._value = newValue || undefined
        break
      case 'selected':
        this._selected = newValue !== null
        break
      case 'disabled':
        this._disabled = newValue !== null
        break
      case 'highlighted':
        this._highlighted = newValue !== null
        break
      case 'hidden':
        this._hidden = newValue !== null
        break
    }

    this.render()
  }

  /**
   * Get value from either property or attribute, with textContent fallback.
   * Property takes precedence (for framework compatibility).
   */
  private getOptionValue(): string {
    const propValue = this._value
    const attrValue = this.getAttribute('value')
    const textValue = this.textContent?.trim() || ''
    const resolved = propValue || attrValue || textValue
    return resolved
  }

  // Public API - Getters/Setters
  // NOTE: All setters trigger re-render to support property changes from frameworks like React

  get value(): string | undefined {
    return this.getOptionValue()
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

  get highlighted(): boolean {
    return this._highlighted
  }

  set highlighted(value: boolean) {
    if (this._highlighted !== value) {
      this._highlighted = value
      if (value) {
        this.setAttribute('highlighted', '')
      } else {
        this.removeAttribute('highlighted')
      }
      this.render()
    }
  }

  get hidden(): boolean {
    return this._hidden
  }

  set hidden(value: boolean) {
    if (this._hidden !== value) {
      this._hidden = value
      if (value) {
        this.setAttribute('hidden', '')
      } else {
        this.removeAttribute('hidden')
      }
      this.render()
    }
  }

  /**
   * Render the option component with rich HTML content and property sync
   */
  private render(): void {
    const shadow = this.shadowRoot!
    const value = this.getOptionValue()

    // Create simple wrapper that preserves content (only once)
    if (!shadow.querySelector('.option-content')) {
      shadow.innerHTML = '<div class="option-content"><slot></slot></div>'
    }

    // Update content wrapper attributes
    const content = shadow.querySelector('.option-content')
    if (content) {
      // Disabled state
      if (this._disabled) {
        content.setAttribute('disabled', '')
      } else {
        content.removeAttribute('disabled')
      }

      // Selected state
      if (this._selected) {
        content.setAttribute('selected', '')
      } else {
        content.removeAttribute('selected')
      }

      // Highlighted state
      if (this._highlighted) {
        content.setAttribute('highlighted', '')
      } else {
        content.removeAttribute('highlighted')
      }

      // Hidden state
      if (this._hidden) {
        content.setAttribute('hidden', '')
      } else {
        content.removeAttribute('hidden')
      }
    }

    // CRITICAL: Ensure value property is set using robust resolution
    // This handles cases where frameworks set property before connection
    if (value) {
      this._value = value
      // Also ensure attribute is set for CSS selectors and debugging
      if (!this.hasAttribute('value')) {
        this.setAttribute('value', value)
      }
    }
  }
}

// Register the custom element
if (!customElements.get('ty-option')) {
  customElements.define('ty-option', TyOption)
}
