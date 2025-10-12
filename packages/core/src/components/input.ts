/**
 * TyInput Web Component
 * PORTED FROM: clj/ty/components/input.cljs
 * Phase D: Complete with delay/debounce feature for input and change events
 * 
 * Enhanced input component with:
 * - Label, error messages, semantic styling
 * - Icon slots (start/end)
 * - Numeric formatting with shadow values
 * - Currency, percent, compact notation
 * - Format-on-blur / raw-on-focus behavior
 * - Debounce delay (0-5000ms) for input/change events
 * - Immediate event firing on blur (cancels pending debounce)
 * 
 * NOTE: Checkbox functionality is in separate ty-checkbox component
 */

import type { Flavor, Size, InputType, TyInputElement } from '../types/common.js'
import { ensureStyles } from '../utils/styles.js'
import { inputStyles } from '../styles/input.js'
import {
  formatNumber,
  parseNumericValue,
  shouldFormat as shouldFormatType,
  type FormatConfig
} from '../utils/number-format.js'
import { getEffectiveLocale, observeLocaleChanges } from '../utils/locale.js'

/**
 * Required indicator SVG icon (from Lucide)
 */
const REQUIRED_ICON_SVG = `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-asterisk-icon lucide-asterisk"><path d="M12 6v12"/><path d="M17.196 9 6.804 15"/><path d="m6.804 9 10.392 6"/></svg>`

/**
 * Ty Input Component (Phase D - Complete with Delay/Debounce)
 * 
 * @example
 * ```html
 * <!-- Basic input -->
 * <ty-input label="Email" type="email" placeholder="Enter email" required></ty-input>
 * 
 * <!-- With icons -->
 * <ty-input label="Search">
 *   <ty-icon slot="start" name="search"></ty-icon>
 * </ty-input>
 * 
 * <!-- With debounce delay (500ms) -->
 * <ty-input 
 *   label="Search" 
 *   delay="500" 
 *   placeholder="Type to search...">
 * </ty-input>
 * 
 * <!-- Currency formatting -->
 * <ty-input 
 *   label="Price" 
 *   type="currency" 
 *   currency="USD"
 *   locale="en-US"
 *   value="1234.56">
 * </ty-input>
 * 
 * <!-- Percent formatting -->
 * <ty-input 
 *   label="Tax Rate" 
 *   type="percent"
 *   value="15"
 *   precision="2">
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

  // Store references to handlers for cleanup
  private _inputHandler: ((e: Event) => void) | null = null
  private _changeHandler: ((e: Event) => void) | null = null
  private _focusHandler: ((e: Event) => void) | null = null
  private _blurHandler: ((e: Event) => void) | null = null

  // Numeric formatting properties (Phase C)
  private _shadowValue: number | string | null = null
  private _isFocused = false
  private _currency: string = 'USD'
  private _locale: string = 'en-US'
  private _precision: number | undefined = undefined

  // Delay/debounce properties (Phase D)
  private _delay: number = 0
  private _inputDebounceTimer: number | null = null
  private _changeDebounceTimer: number | null = null
  private _localeObserver?: () => void // Cleanup function for locale observer

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
      'disabled', 'required', 'error', 'size', 'flavor',
      'currency', 'locale', 'precision',  // Phase C
      'delay'  // Phase D
    ]
  }

  connectedCallback(): void {
    // Initialize shadow value from initial value
    this.initializeShadowValue()
    this.render()
    this.setupEventListeners()
    
    // Setup locale observer to watch for ancestor lang changes
    this._localeObserver = observeLocaleChanges(this, () => {
      this.render()
    })
  }

  disconnectedCallback(): void {
    // Clean up event listeners
    this.removeEventListeners()
    
    // Cleanup locale observer
    if (this._localeObserver) {
      this._localeObserver()
      this._localeObserver = undefined
    }
    
    // Clear any pending debounce timers
    if (this._inputDebounceTimer !== null) {
      clearTimeout(this._inputDebounceTimer)
      this._inputDebounceTimer = null
    }
    if (this._changeDebounceTimer !== null) {
      clearTimeout(this._changeDebounceTimer)
      this._changeDebounceTimer = null
    }
  }

  attributeChangedCallback(name: string, oldValue: string | null, newValue: string | null): void {
    if (oldValue === newValue) return

    switch (name) {
      case 'type':
        this._type = (newValue as InputType) || 'text'
        break
      case 'value':
        this._value = newValue || ''
        // Re-parse shadow value when value attribute changes
        this._shadowValue = this.parseShadowValue(this._value)
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
      // Phase C attributes
      case 'currency':
        this._currency = newValue || 'USD'
        break
      case 'locale':
        this._locale = newValue || 'en-US'
        break
      case 'precision':
        this._precision = newValue ? parseInt(newValue, 10) : undefined
        break
      // Phase D attribute
      case 'delay':
        this._delay = this.parseDelay(newValue)
        break
    }

    this.render()
  }

  /**
   * Parse and validate delay value (0-5000ms)
   */
  private parseDelay(value: string | null): number {
    if (!value) return 0
    const parsed = parseInt(value, 10)
    if (isNaN(parsed)) return 0
    // Clamp between 0 and 5000ms
    return Math.max(0, Math.min(5000, parsed))
  }

  /**
 * Initialize shadow value from the initial value attribute
 */
  private initializeShadowValue(): void {
    if (this._value) {
      this._shadowValue = this.parseShadowValue(this._value)
      // Update form value
      this._internals.setFormValue(
        this._shadowValue !== null ? String(this._shadowValue) : null
      )
    }
  }

  /**
   * Parse a string value to the appropriate shadow value type
   */
  private parseShadowValue(value: string): number | string | null {
    if (!value || value.trim() === '') return null

    // For numeric types, parse to number
    if (shouldFormatType(this._type)) {
      return parseNumericValue(value)
    }

    // For other types, keep as string
    return value
  }

  /**
   * Check if current input should format numbers
   */
  private shouldFormat(): boolean {
    return shouldFormatType(this._type) &&
      !this._isFocused &&
      this._shadowValue !== null &&
      typeof this._shadowValue === 'number'
  }

  /**
   * Get the format configuration for current input
   */
  private getFormatConfig(): FormatConfig {
    return {
      type: this._type as 'number' | 'currency' | 'percent' | 'compact',
      locale: this.locale,  // Use getter which calls getEffectiveLocale correctly
      currency: this._currency,
      precision: this._precision
    }
  }

  /**
   * Get the display value (formatted or raw)
   */
  private getDisplayValue(): string {
    if (this.shouldFormat()) {
      let valueToFormat = this._shadowValue as number

      // For percent: divide by 100 (user enters 15, displays as 15%)
      // This matches ClojureScript behavior
      if (this._type === 'percent') {
        valueToFormat = valueToFormat / 100
      }

      return formatNumber(valueToFormat, this.getFormatConfig())
    }

    return this._shadowValue !== null ? String(this._shadowValue) : ''
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
      // Re-parse shadow value with new type
      this._shadowValue = this.parseShadowValue(this._value)
      this.render()
    }
  }

  get value(): string {
    // Return shadow value as string
    return this._shadowValue !== null ? String(this._shadowValue) : ''
  }

  set value(val: string) {
    if (this._value !== val) {
      this._value = val
      this._shadowValue = this.parseShadowValue(val)
      this.setAttribute('value', val)
      this._internals.setFormValue(
        this._shadowValue !== null ? String(this._shadowValue) : null
      )
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

  // Phase C getters/setters

  get currency(): string {
    return this._currency
  }

  set currency(value: string) {
    if (this._currency !== value) {
      this._currency = value
      this.setAttribute('currency', value)
      this.render()
    }
  }

  get locale(): string {
    return getEffectiveLocale(this, this.getAttribute('locale'))
  }

  set locale(value: string) {
    if (this._locale !== value) {
      this._locale = value
      this.setAttribute('locale', value)
      this.render()
    }
  }

  get precision(): number | undefined {
    return this._precision
  }

  set precision(value: number | string | undefined) {
    const numValue = typeof value === 'string' ? parseInt(value, 10) : value
    if (this._precision !== numValue) {
      this._precision = numValue
      if (numValue !== undefined) {
        this.setAttribute('precision', String(numValue))
      } else {
        this.removeAttribute('precision')
      }
      this.render()
    }
  }

  // Phase D getter/setter

  get delay(): number {
    return this._delay
  }

  set delay(value: number | string) {
    const numValue = typeof value === 'string' ? parseInt(value, 10) : value
    const clamped = this.parseDelay(String(numValue))
    if (this._delay !== clamped) {
      this._delay = clamped
      if (clamped > 0) {
        this.setAttribute('delay', String(clamped))
      } else {
        this.removeAttribute('delay')
      }
    }
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
   * Dispatch input event (helper method for debouncing)
   */
  private dispatchInputEvent(rawValue: string, originalEvent: Event): void {
    this.dispatchEvent(new CustomEvent('input', {
      detail: {
        value: this._shadowValue,
        formattedValue: this.shouldFormat() ? this.getDisplayValue() : null,
        rawValue: rawValue,
        originalEvent: originalEvent
      },
      bubbles: true,
      composed: true
    }))
  }

  /**
   * Dispatch change event (helper method for debouncing)
   */
  private dispatchChangeEvent(rawValue: string, originalEvent: Event): void {
    this.dispatchEvent(new CustomEvent('change', {
      detail: {
        value: this._shadowValue,
        formattedValue: this.shouldFormat() ? this.getDisplayValue() : null,
        rawValue: rawValue,
        originalEvent: originalEvent
      },
      bubbles: true,
      composed: true
    }))
  }

  /**
   * Remove event listeners for cleanup
   */
  private removeEventListeners(): void {
    if (!this._listenersSetup) return

    const shadow = this.shadowRoot
    if (!shadow) return

    const inputEl = shadow.querySelector('input')
    if (!inputEl) return

    // Remove all event listeners using stored handler references
    if (this._inputHandler) {
      inputEl.removeEventListener('input', this._inputHandler)
      this._inputHandler = null
    }

    if (this._changeHandler) {
      inputEl.removeEventListener('change', this._changeHandler)
      this._changeHandler = null
    }

    if (this._focusHandler) {
      inputEl.removeEventListener('focus', this._focusHandler)
      this._focusHandler = null
    }

    if (this._blurHandler) {
      inputEl.removeEventListener('blur', this._blurHandler)
      this._blurHandler = null
    }

    this._listenersSetup = false
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

    // Create and store handler references for cleanup
    
    // Input event - update shadow value and form (with debounce support)
    this._inputHandler = (e: Event) => {
      // Stop native event from propagating - only our custom event should bubble
      e.stopPropagation()
      e.stopImmediatePropagation()

      const target = e.target as HTMLInputElement
      const rawValue = target.value

      // Parse to shadow value
      this._shadowValue = this.parseShadowValue(rawValue)

      // Update form value with shadow value
      this._internals.setFormValue(
        this._shadowValue !== null ? String(this._shadowValue) : null
      )

      // Clear existing timer
      if (this._inputDebounceTimer !== null) {
        clearTimeout(this._inputDebounceTimer)
      }

      // If delay is set, debounce the event
      if (this._delay > 0) {
        this._inputDebounceTimer = window.setTimeout(() => {
          // Use current value, not the captured rawValue
          this.dispatchInputEvent(inputEl.value, e)
          this._inputDebounceTimer = null
        }, this._delay)
      } else {
        // Fire immediately if no delay
        this.dispatchInputEvent(rawValue, e)
      }
    }

    // Change event (with debounce support)
    this._changeHandler = (e: Event) => {
      // Stop native event from propagating - only our custom event should bubble
      e.stopPropagation()
      e.stopImmediatePropagation()

      const target = e.target as HTMLInputElement
      const rawValue = target.value

      // Parse to shadow value
      this._shadowValue = this.parseShadowValue(rawValue)

      // Update form value
      this._internals.setFormValue(
        this._shadowValue !== null ? String(this._shadowValue) : null
      )

      // Clear existing timer
      if (this._changeDebounceTimer !== null) {
        clearTimeout(this._changeDebounceTimer)
      }

      // If delay is set, debounce the event
      if (this._delay > 0) {
        this._changeDebounceTimer = window.setTimeout(() => {
          // Use current value, not the captured rawValue
          this.dispatchChangeEvent(inputEl.value, e)
          this._changeDebounceTimer = null
        }, this._delay)
      } else {
        // Fire immediately if no delay
        this.dispatchChangeEvent(rawValue, e)
      }
    }

    // Focus event - show raw value for numeric types
    this._focusHandler = (e: Event) => {
      this._isFocused = true
      wrapperEl.classList.add('focused')

      // For numeric types, show raw shadow value
      if (shouldFormatType(this._type) && this._shadowValue !== null) {
        inputEl.value = String(this._shadowValue)
      }

      this.dispatchEvent(new CustomEvent('focus', {
        detail: { originalEvent: e },
        bubbles: true,
        composed: true
      }))
    }

    // Blur event - fire pending debounced events immediately, then show formatted value
    this._blurHandler = (e: Event) => {
      const target = e.target as HTMLInputElement
      const rawValue = target.value

      // Fire any pending debounced input event immediately on blur
      if (this._inputDebounceTimer !== null) {
        clearTimeout(this._inputDebounceTimer)
        this._inputDebounceTimer = null
        this.dispatchInputEvent(rawValue, e)
      }

      // Fire any pending debounced change event immediately on blur
      if (this._changeDebounceTimer !== null) {
        clearTimeout(this._changeDebounceTimer)
        this._changeDebounceTimer = null
        this.dispatchChangeEvent(rawValue, e)
      }

      this._isFocused = false
      wrapperEl.classList.remove('focused')

      // For numeric types, show formatted value
      if (this.shouldFormat()) {
        inputEl.value = this.getDisplayValue()
      }

      this.dispatchEvent(new CustomEvent('blur', {
        detail: { originalEvent: e },
        bubbles: true,
        composed: true
      }))
    }

    // Add event listeners
    inputEl.addEventListener('input', this._inputHandler)
    inputEl.addEventListener('change', this._changeHandler)
    inputEl.addEventListener('focus', this._focusHandler)
    inputEl.addEventListener('blur', this._blurHandler)

    this._listenersSetup = true
  }

  /**
   * Render the input component with wrapper and slots
   * Phase C: Uses getDisplayValue() for formatted output
   */
  private render(): void {
    const shadow = this.shadowRoot!
    const existingInput = shadow.querySelector('input')
    const existingWrapper = shadow.querySelector('.input-wrapper')
    const existingLabel = shadow.querySelector('label')
    const existingError = shadow.querySelector('.error-message')
    const classes = this.buildClassList()

    // Map input type (all numeric types use 'text' in DOM)
    const inputType = ['password', 'date', 'time', 'datetime-local'].includes(this._type)
      ? this._type
      : 'text'

    // Get display value (formatted or raw based on focus)
    const displayValue = this.getDisplayValue()

    // If input exists, just update properties (like ClojureScript does)
    if (existingInput && existingWrapper) {
      existingInput.type = inputType
      existingInput.value = displayValue
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
                value="${displayValue}"
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
