/**
 * TyCopy Web Component
 * 
 * Read-only field with copy-to-clipboard functionality
 * Perfect for API keys, tokens, URLs, code snippets, etc.
 * 
 * Features:
 * - Read-only display (not an input)
 * - Copy icon on the right
 * - Icon animation on copy (copy → check → copy)
 * - Same styling as ty-input for consistency
 * - Label support
 * - Size and flavor variants
 */

import type { Flavor, Size } from '../types/common.js'
import { ensureStyles } from '../utils/styles.js'
import { inputStyles } from '../styles/input.js'
import { copyStyles } from '../styles/copy.js'

/**
 * Copy icon SVG (from Lucide)
 */
const COPY_ICON_SVG = `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="14" height="14" x="8" y="8" rx="2" ry="2"/><path d="M4 16c-1.1 0-2-.9-2-2V4c0-1.1.9-2 2-2h10c1.1 0 2 .9 2 2"/></svg>`

/**
 * Check icon SVG (from Lucide) - shown after successful copy
 */
const CHECK_ICON_SVG = `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>`

/**
 * Required indicator SVG icon (from Lucide)
 */
const REQUIRED_ICON_SVG = `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-asterisk-icon lucide-asterisk"><path d="M12 6v12"/><path d="M17.196 9 6.804 15"/><path d="m6.804 9 10.392 6"/></svg>`

/**
 * TyCopy Element Interface
 */
export interface TyCopyElement extends HTMLElement {
  value: string
  label: string
  size: Size
  flavor: Flavor
  format: 'text' | 'code'
  disabled: boolean
  required: boolean
}

/**
 * Ty Copy Field Component
 * 
 * @example
 * ```html
 * <!-- Basic copy field -->
 * <ty-copy 
 *   label="API Key" 
 *   value="sk_live_1234567890abcdef">
 * </ty-copy>
 * 
 * <!-- Code format -->
 * <ty-copy 
 *   label="Install Command" 
 *   value="npm install @gersak/ty"
 *   format="code">
 * </ty-copy>
 * 
 * <!-- Different sizes -->
 * <ty-copy size="sm" value="Small field"></ty-copy>
 * <ty-copy size="lg" value="Large field"></ty-copy>
 * 
 * <!-- Semantic flavors -->
 * <ty-copy flavor="primary" value="Primary"></ty-copy>
 * <ty-copy flavor="success" value="Success"></ty-copy>
 * ```
 */
export class TyCopy extends HTMLElement implements TyCopyElement {
  private _value: string = ''
  private _label: string = ''
  private _size: Size = 'md'
  private _flavor: Flavor = 'neutral'
  private _format: 'text' | 'code' = 'text'
  private _multiline: boolean = false
  private _disabled: boolean = false
  private _required: boolean = false
  private _copyTimeout: number | null = null

  constructor() {
    super()

    const shadow = this.attachShadow({ mode: 'open' })
    
    // Apply both input and copy-specific styles
    ensureStyles(shadow, { css: inputStyles, id: 'ty-input' })
    ensureStyles(shadow, { css: copyStyles, id: 'ty-copy' })
  }

  static get observedAttributes(): string[] {
    return ['value', 'label', 'size', 'flavor', 'format', 'multiline', 'disabled', 'required']
  }

  connectedCallback(): void {
    // CRITICAL: Reagent/React may set properties BEFORE the element is constructed
    // Check if value was set directly on the instance before our getter/setter was available
    const instanceValue = Object.getOwnPropertyDescriptor(this, 'value')
    if (instanceValue && instanceValue.value !== undefined) {
      console.log('[ty-copy] Found pre-construction value:', instanceValue.value)
      this._value = instanceValue.value
      // Clean up the instance property so our getter/setter works
      ;(this as any).value = undefined
    }
    
    // Read initial attribute values if they exist
    // This handles the case where attributes are set before the element is connected
    if (this.hasAttribute('value')) {
      this._value = this.getAttribute('value') || ''
    }
    if (this.hasAttribute('label')) {
      this._label = this.getAttribute('label') || ''
    }
    if (this.hasAttribute('size')) {
      this._size = (this.getAttribute('size') as Size) || 'md'
    }
    if (this.hasAttribute('flavor')) {
      this._flavor = this.validateFlavor(this.getAttribute('flavor'))
    }
    if (this.hasAttribute('format')) {
      const format = this.getAttribute('format')
      this._format = format === 'code' ? 'code' : 'text'
    }
    if (this.hasAttribute('multiline')) {
      this._multiline = true
    }
    if (this.hasAttribute('disabled')) {
      this._disabled = true
    }
    if (this.hasAttribute('required')) {
      this._required = true
    }

    this.render()
    this.setupEventListeners()
  }

  disconnectedCallback(): void {
    // Clear any pending timeout
    if (this._copyTimeout !== null) {
      clearTimeout(this._copyTimeout)
      this._copyTimeout = null
    }
  }

  attributeChangedCallback(name: string, oldValue: string | null, newValue: string | null): void {
    console.log('Attribute changed', name)
    if (oldValue === newValue) return

    switch (name) {
      case 'value':
        console.log('[ty-copy] ENTRY POINT 1: attributeChangedCallback - value:', newValue)
        this._value = newValue || ''
        break
      case 'label':
        this._label = newValue || ''
        break
      case 'size':
        this._size = (newValue as Size) || 'md'
        break
      case 'flavor':
        this._flavor = this.validateFlavor(newValue)
        break
      case 'format':
        this._format = (newValue === 'code' ? 'code' : 'text')
        break
      case 'multiline':
        this._multiline = newValue !== null
        break
      case 'disabled':
        this._disabled = newValue !== null
        break
      case 'required':
        this._required = newValue !== null
        break
    }

    this.render()
  }

  /**
   * Validate flavor attribute
   */
  private validateFlavor(flavor: string | null): Flavor {
    const validFlavors: Flavor[] = ['primary', 'secondary', 'success', 'danger', 'warning', 'neutral']
    const normalized = (flavor || 'neutral') as Flavor

    if (!validFlavors.includes(normalized)) {
      console.warn(
        `[ty-copy] Invalid flavor '${flavor}'. Using 'neutral'. ` +
        `Valid flavors: ${validFlavors.join(', ')}`
      )
      return 'neutral'
    }

    return normalized
  }

  /**
   * Build CSS class list
   */
  private buildClassList(): string {
    const classes: string[] = [this._size, this._flavor]

    if (this._disabled) classes.push('disabled')
    if (this._required) classes.push('required')

    return classes.join(' ')
  }

  /**
   * Copy value to clipboard
   */
  private async copyToClipboard(): Promise<void> {
    if (this._disabled || !this._value) return

    try {
      await navigator.clipboard.writeText(this._value)

      // Emit success event
      this.dispatchEvent(new CustomEvent('copy-success', {
        detail: { value: this._value },
        bubbles: true,
        composed: true
      }))

      // Show success icon
      this.showCopySuccess()
    } catch (err) {
      console.error('[ty-copy] Failed to copy:', err)

      // Emit error event
      this.dispatchEvent(new CustomEvent('copy-error', {
        detail: { error: err },
        bubbles: true,
        composed: true
      }))
    }
  }

  /**
   * Show copy success animation
   * Swaps copy icon → check icon → copy icon
   */
  private showCopySuccess(): void {
    const shadow = this.shadowRoot!
    const copyButton = shadow.querySelector('.copy-button')
    if (!copyButton) return

    // Clear any existing timeout
    if (this._copyTimeout !== null) {
      clearTimeout(this._copyTimeout)
    }

    // Add success class and show check icon
    copyButton.classList.add('success')
    copyButton.innerHTML = CHECK_ICON_SVG

    // Reset after 2 seconds
    this._copyTimeout = window.setTimeout(() => {
      copyButton.classList.remove('success')
      copyButton.innerHTML = COPY_ICON_SVG
      this._copyTimeout = null
    }, 2000)
  }

  /**
   * Setup event listeners
   */
  private setupEventListeners(): void {
    const shadow = this.shadowRoot!
    const inputWrapper = shadow.querySelector('.input-wrapper')
    const copyButton = shadow.querySelector('.copy-button')

    // Make entire field clickable for copying
    if (inputWrapper && !this._disabled) {
      const wrapperEl = inputWrapper as HTMLElement
      wrapperEl.addEventListener('click', (e) => {
        // Prevent double-triggering if button was clicked
        if (e.target === copyButton || (e.target as HTMLElement).closest('.copy-button')) {
          return
        }
        this.copyToClipboard()
      })
      
      // Add visual feedback - make it look clickable
      wrapperEl.style.cursor = 'pointer'
    }

    // Button click handler (redundant but kept for explicitness)
    if (copyButton) {
      copyButton.addEventListener('click', () => {
        this.copyToClipboard()
      })
    }
  }

  /**
   * Render the component
   */
  private render(): void {
    const shadow = this.shadowRoot!
    const classes = this.buildClassList()

    // Determine display element based on format and multiline
    const multilineClass = this._multiline ? ' multiline' : ''
    const displayElement = this._format === 'code'
      ? `<code class="copy-field-value${multilineClass}">${this._value || ''}</code>`
      : `<div class="copy-field-value${multilineClass}">${this._value || ''}</div>`

    const labelHtml = this._label ? `
      <label class="input-label">
        ${this._label}
        ${this._required ? `<span class="required-icon">${REQUIRED_ICON_SVG}</span>` : ''}
      </label>
    ` : ''

    shadow.innerHTML = `
      <div class="input-container">
        ${labelHtml}
        <div class="input-wrapper ${classes}">
          ${displayElement}
          <button class="copy-button" type="button" ${this._disabled ? 'disabled' : ''}>
            ${COPY_ICON_SVG}
          </button>
        </div>
      </div>
    `

    // Re-setup event listeners after render
    this.setupEventListeners()
  }

  // Public API - Getters/Setters

  get value(): string {
    console.log('Getting value');
    return this._value
  }

  set value(val: string) {
    console.log('[ty-copy] ENTRY POINT 2: value setter - val:', val)
    if (this._value !== val) {
      this._value = val
      this.setAttribute('value', val)
      this.render()
    }
  }

  get label(): string {
    return this._label
  }

  set label(val: string) {
    if (this._label !== val) {
      this._label = val
      this.setAttribute('label', val)
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

  get format(): 'text' | 'code' {
    return this._format
  }

  set format(value: 'text' | 'code') {
    if (this._format !== value) {
      this._format = value
      this.setAttribute('format', value)
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

  get required(): boolean {
    return this._required
  }

  set required(value: boolean) {
    if (this._required !== value) {
      this._required = value
      if (value) {
        this.setAttribute('required', '')
      } else {
        this.removeAttribute('required')
      }
      this.render()
    }
  }
}

// Register the custom element
if (!customElements.get('ty-copy')) {
  customElements.define('ty-copy', TyCopy)
}