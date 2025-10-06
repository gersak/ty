/**
 * TyInput Web Component
 * PORTED FROM: clj/ty/components/input.cljs
 * Phase A: Core input functionality (no checkbox, no numeric formatting)
 * 
 * Enhanced input component with label, error messages, semantic styling, and icon slots
 */

import type { Flavor, Size, InputType, TyInputElement } from '../types/common.js'
import { ensureStyles } from '../utils/styles.js'
import { inputStyles } from '../styles/input.js'

/**
 * Required indicator SVG icon (from Lucide)
 */
const REQUIRED_ICON_SVG = `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-asterisk-icon lucide-asterisk"><path d="M12 6v12"/><path d="M17.196 9 6.804 15"/><path d="m6.804 9 10.392 6"/></svg>`

/**
 * Ty Input Component (Phase A)
 * 
 * @example
 * ```html
 * <ty-input label="Email" type="email" placeholder="Enter email" required></ty-input>
 * <ty-input label="Search">
 *   <ty-icon slot="start" name="search"></ty-icon>
 * </ty-input>
 * <ty-input label="Password" type="password">
 *   <ty-icon slot="end" name="eye"></ty-icon>
 * </ty-input>
 * ```
 */
export class TyInput extends HTMLElement implements TyInputElement {
  static formAssociated = true

  private _internals: ElementInternals
  private _type: InputType = 'text'
  private _value: string = ''
  private _name: string = ''
  private _placeholder: string = ''
  private _label: string = ''
  private _disabled = false
  private _required = false
  private _error: string = ''
  private _size: Size = 'md'
  private _flavor: Flavor = 'neutral'
  private _listenersSetup = false

  constructor() {
    super()
    this._internals = this.attachInternals()

    const shadow = this.attachShadow({ mode: 'open' })
    ensureStyles(shadow, { css: inputStyles, id: 'ty-input' })

    this.render()
  }

  static get observedAttributes(): string[] {
    return [
      'type', 'value', 'name', 'placeholder', 'label',
      'disabled', 'required', 'error', 'size', 'flavor'
    ]
  }

  connectedCallback(): void {
    this.render()
    this.setupEventListeners()
  }

  disconnectedCallback(): void {
    // Cleanup handled by shadow DOM removal
  }

  attributeChangedCallback(name: string, oldValue: string | null, newValue: string | null): void {
    if (oldValue === newValue) return

    switch (name) {
      case 'type':
        this._type = (newValue as InputType) || 'text'
        break
      case 'value':
        this._value = newValue || ''
        break
      case 'name':
        this._name = newValue || ''
        break
      case 'placeholder':
        this._placeholder = newValue || ''
        break
      case 'label':
        this._label = newValue || ''
        break
      case 'disabled':
        this._disabled = newValue !== null
        break
      case 'required':
        this._required = newValue !== null
        break
      case 'error':
        this._error = newValue || ''
        // Auto-set flavor to danger when error present
        if (newValue && this._flavor === 'neutral') {
          this._flavor = 'danger'
        }
        break
      case 'size':
        this._size = (newValue as Size) || 'md'
        break
      case 'flavor':
        this._flavor = this.validateFlavor(newValue)
        break
    }

    this.render()
  }

  /**
   * Validate flavor (matches ClojureScript validation)
   */
  private validateFlavor(flavor: string | null): Flavor {
    const validFlavors: Flavor[] = ['primary', 'secondary', 'success', 'danger', 'warning', 'neutral']
    const normalized = (flavor || 'neutral') as Flavor

    if (!validFlavors.includes(normalized)) {
      console.warn(
        `[ty-input] Invalid flavor '${flavor}'. Using 'neutral'. ` +
        `Valid flavors: ${validFlavors.join(', ')}`
      )
      return 'neutral'
    }

    return normalized
  }

  // Public API - Getters/Setters
  // NOTE: All setters trigger re-render to support property changes from frameworks

  get type(): InputType {
    return this._type
  }

  set type(value: InputType) {
    if (this._type !== value) {
      this._type = value
      this.setAttribute('type', value)
      this.render()
    }
  }

  get value(): string {
    return this._value
  }

  set value(val: string) {
    if (this._value !== val) {
      this._value = val
      this.setAttribute('value', val)
      this._internals.setFormValue(val)
      this.render()
    }
  }

  get name(): string {
    return this._name
  }

  set name(val: string) {
    if (this._name !== val) {
      this._name = val
      this.setAttribute('name', val)
    }
  }

  get placeholder(): string {
    return this._placeholder
  }

  set placeholder(val: string) {
    if (this._placeholder !== val) {
      this._placeholder = val
      this.setAttribute('placeholder', val)
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

  get error(): string {
    return this._error
  }

  set error(val: string) {
    if (this._error !== val) {
      this._error = val
      this.setAttribute('error', val)
      // Auto-set flavor to danger
      if (val && this._flavor === 'neutral') {
        this._flavor = 'danger'
      }
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

  get form(): HTMLFormElement | null {
    return this._internals.form
  }

  /**
   * Build CSS class list for input wrapper
   */
  private buildClassList(): string {
    const classes: string[] = [this._size, this._flavor]

    if (this._disabled) classes.push('disabled')
    if (this._required) classes.push('required')
    if (this._error) classes.push('error')

    return classes.join(' ')
  }

  /**
   * Setup event listeners for input element
   * IMPORTANT: Only called ONCE, not on every render (like ClojureScript)
   */
  private setupEventListeners(): void {
    if (this._listenersSetup) return // Already setup

    const shadow = this.shadowRoot!
    const inputEl = shadow.querySelector('input')
    const wrapperEl = shadow.querySelector('.input-wrapper')

    if (!inputEl || !wrapperEl) return

    // Input event - update value and form
    inputEl.addEventListener('input', (e) => {
      const target = e.target as HTMLInputElement
      this._value = target.value
      this._internals.setFormValue(this._value)

      // Emit custom event
      this.dispatchEvent(new CustomEvent('input', {
        detail: { value: this._value, originalEvent: e },
        bubbles: true,
        composed: true
      }))
    })

    // Change event
    inputEl.addEventListener('change', (e) => {
      const target = e.target as HTMLInputElement
      this._value = target.value
      this._internals.setFormValue(this._value)

      // Emit custom event
      this.dispatchEvent(new CustomEvent('change', {
        detail: { value: this._value, originalEvent: e },
        bubbles: true,
        composed: true
      }))
    })

    // Focus event - add focused class to wrapper
    inputEl.addEventListener('focus', (e) => {
      wrapperEl.classList.add('focused')
      this.dispatchEvent(new CustomEvent('focus', {
        detail: { originalEvent: e },
        bubbles: true,
        composed: true
      }))
    })

    // Blur event - remove focused class from wrapper
    inputEl.addEventListener('blur', (e) => {
      wrapperEl.classList.remove('focused')
      this.dispatchEvent(new CustomEvent('blur', {
        detail: { originalEvent: e },
        bubbles: true,
        composed: true
      }))
    })

    this._listenersSetup = true
  }

  /**
   * Render the input component with wrapper and slots
   * UPDATED: Input wrapper with start/end slots for icons
   */
  private render(): void {
    const shadow = this.shadowRoot!
    const existingInput = shadow.querySelector('input')
    const existingWrapper = shadow.querySelector('.input-wrapper')
    const existingLabel = shadow.querySelector('label')
    const existingError = shadow.querySelector('.error-message')
    const classes = this.buildClassList()

    // Map input type
    const inputType = ['password', 'date', 'time', 'datetime-local'].includes(this._type)
      ? this._type
      : 'text'

    // If input exists, just update properties (like ClojureScript does)
    if (existingInput && existingWrapper) {
      existingInput.type = inputType
      existingInput.value = this._value
      existingInput.placeholder = this._placeholder
      existingInput.name = this._name

      // Update wrapper classes
      existingWrapper.className = `input-wrapper ${classes}`

      // Set disabled property AND manage attribute
      existingInput.disabled = this._disabled
      if (this._disabled) {
        existingInput.setAttribute('disabled', '')
      } else {
        existingInput.removeAttribute('disabled')
      }

      // Set required property AND manage attribute
      existingInput.required = this._required
      if (this._required) {
        existingInput.setAttribute('required', '')
      } else {
        existingInput.removeAttribute('required')
      }

      // Update label
      if (existingLabel) {
        if (this._label) {
          existingLabel.innerHTML = `${this._label}${this._required ? `<span class="required-icon">${REQUIRED_ICON_SVG}</span>` : ''}`
          existingLabel.style.display = 'flex'
        } else {
          existingLabel.style.display = 'none'
        }
      }

      // Update error message
      if (this._error) {
        if (existingError) {
          existingError.textContent = this._error
        } else {
          const errorEl = document.createElement('div')
          errorEl.className = 'error-message'
          errorEl.textContent = this._error
          shadow.querySelector('.input-container')?.appendChild(errorEl)
        }
      } else if (existingError) {
        existingError.remove()
      }
    } else {
      // Create initial structure with wrapper and slots
      const labelHtml = this._label ? `
        <label class="input-label">
          ${this._label}
          ${this._required ? `<span class="required-icon">${REQUIRED_ICON_SVG}</span>` : ''}
        </label>
      ` : ''

      const errorHtml = this._error ? `
        <div class="error-message">${this._error}</div>
      ` : ''

      shadow.innerHTML = `
        <div class="input-container">
          ${labelHtml}
          <div class="input-wrapper ${classes}">
            <div class="input-start">
              <slot name="start"></slot>
            </div>
            <input
              type="${inputType}"
              value="${this._value}"
              placeholder="${this._placeholder}"
              name="${this._name}"
            />
            <div class="input-end">
              <slot name="end"></slot>
            </div>
          </div>
          ${errorHtml}
        </div>
      `

      // Set boolean properties after creating element
      const inputEl = shadow.querySelector('input')!
      inputEl.disabled = this._disabled
      inputEl.required = this._required

      // Setup event listeners ONCE
      this.setupEventListeners()
    }
  }
}

// Register the custom element
if (!customElements.get('ty-input')) {
  customElements.define('ty-input', TyInput)
}
