/**
 * TyCheckbox Web Component
 * PORTED FROM: clj/ty/components/input.cljs (checkbox type)
 * 
 * Standalone checkbox component with:
 * - Label, error messages, semantic styling
 * - Boolean checked state
 * - Form value customization
 * - Keyboard accessibility (Space/Enter)
 * - Size variants (xs, sm, md, lg, xl)
 * - Semantic flavors (primary, secondary, success, danger, warning, neutral)
 */

import type { Flavor, Size } from '../types/common.js'
import { ensureStyles } from '../utils/styles.js'
import { inputStyles } from '../styles/input.js'

/**
 * Checkbox unchecked icon (Font Awesome)
 */
const CHECKBOX_UNCHECKED_ICON = `<svg fill='currentColor' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640"><!--!Font Awesome Free v7.0.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.--><path d="M160 96L480 96C515.3 96 544 124.7 544 160L544 480C544 515.3 515.3 544 480 544L160 544C124.7 544 96 515.3 96 480L96 160C96 124.7 124.7 96 160 96z"/></svg>`

/**
 * Checkbox checked icon (Font Awesome)
 */
const CHECKBOX_CHECKED_ICON = `<svg fill='currentColor' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640"><!--!Font Awesome Free v7.0.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.--><path d="M480 96C515.3 96 544 124.7 544 160L544 480C544 515.3 515.3 544 480 544L160 544C124.7 544 96 515.3 96 480L96 160C96 124.7 124.7 96 160 96L480 96zM438 209.7C427.3 201.9 412.3 204.3 404.5 215L285.1 379.2L233 327.1C223.6 317.7 208.4 317.7 199.1 327.1C189.8 336.5 189.7 351.7 199.1 361L271.1 433C276.1 438 283 440.5 289.9 440C296.8 439.5 303.3 435.9 307.4 430.2L443.3 243.2C451.1 232.5 448.7 217.5 438 209.7z"/></svg>`

/**
 * Required indicator SVG icon (from Lucide)
 */
const REQUIRED_ICON_SVG = `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-asterisk-icon lucide-asterisk"><path d="M12 6v12"/><path d="M17.196 9 6.804 15"/><path d="m6.804 9 10.392 6"/></svg>`

/**
 * TyCheckbox Element Interface
 */
export interface TyCheckboxElement extends HTMLElement {
  checked: boolean
  value: string
  name: string
  label: string
  disabled: boolean
  required: boolean
  error: string
  size: Size
  flavor: Flavor
  form: HTMLFormElement | null
}

/**
 * Ty Checkbox Component
 * 
 * @example
 * ```html
 * <!-- Basic checkbox -->
 * <ty-checkbox label="Subscribe to newsletter"></ty-checkbox>
 * 
 * <!-- Pre-checked with custom value -->
 * <ty-checkbox 
 *   label="Accept terms" 
 *   checked 
 *   value="accepted"
 *   required>
 * </ty-checkbox>
 * 
 * <!-- With error state -->
 * <ty-checkbox 
 *   label="Agree to terms" 
 *   required 
 *   error="You must agree to continue"
 *   flavor="danger">
 * </ty-checkbox>
 * 
 * <!-- Different sizes -->
 * <ty-checkbox label="Small" size="sm"></ty-checkbox>
 * <ty-checkbox label="Large" size="lg"></ty-checkbox>
 * 
 * <!-- Semantic flavors -->
 * <ty-checkbox label="Primary" flavor="primary" checked></ty-checkbox>
 * <ty-checkbox label="Success" flavor="success" checked></ty-checkbox>
 * ```
 */
export class TyCheckbox extends HTMLElement implements TyCheckboxElement {
  static formAssociated = true

  private _internals: ElementInternals
  private _checked: boolean = false
  private _value: string = 'on'
  private _name: string = ''
  private _label: string = ''
  private _disabled: boolean = false
  private _required: boolean = false
  private _error: string = ''
  private _size: Size = 'md'
  private _flavor: Flavor = 'neutral'
  private _listenersSetup: boolean = false

  // Store references to handlers for cleanup
  private _clickHandler: ((e: Event) => void) | null = null
  private _keydownHandler: ((e: Event) => void) | null = null
  private _focusHandler: (() => void) | null = null
  private _blurHandler: (() => void) | null = null

  constructor() {
    super()
    this._internals = this.attachInternals()

    const shadow = this.attachShadow({ mode: 'open' })
    ensureStyles(shadow, { css: inputStyles, id: 'ty-checkbox' })

    this.render()
  }

  static get observedAttributes(): string[] {
    return [
      'checked', 'value', 'name', 'label',
      'disabled', 'required', 'error', 'size', 'flavor'
    ]
  }

  connectedCallback(): void {
    // Initialize checked state and form value
    this.initializeCheckboxState()
    this.render()
    // Event listeners are set up in render() when checkbox container exists
  }

  disconnectedCallback(): void {
    // Clean up event listeners when component is removed from DOM
    this.removeEventListeners()
  }

  attributeChangedCallback(name: string, oldValue: string | null, newValue: string | null): void {
    if (oldValue === newValue) return

    switch (name) {
      case 'checked':
        this._checked = newValue !== null
        break
      case 'value':
        this._value = newValue || 'on'
        break
      case 'name':
        this._name = newValue || ''
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
   * Initialize checkbox state from attributes
   */
  private initializeCheckboxState(): void {
    const initialChecked = this.hasAttribute('checked') || this._checked
    this._checked = initialChecked

    // Set form value: string when checked, null when unchecked
    this._internals.setFormValue(
      initialChecked ? this._value : null
    )
  }

  /**
   * Validate flavor attribute
   */
  private validateFlavor(flavor: string | null): Flavor {
    const validFlavors: Flavor[] = ['primary', 'secondary', 'success', 'danger', 'warning', 'neutral']
    const normalized = (flavor || 'neutral') as Flavor

    if (!validFlavors.includes(normalized)) {
      console.warn(
        `[ty-checkbox] Invalid flavor '${flavor}'. Using 'neutral'. ` +
        `Valid flavors: ${validFlavors.join(', ')}`
      )
      return 'neutral'
    }

    return normalized
  }

  /**
   * Build CSS class list for checkbox container
   */
  private buildClassList(): string {
    const classes: string[] = [this._size, this._flavor]

    if (this._disabled) classes.push('disabled')
    if (this._required) classes.push('required')
    if (this._error) classes.push('error')

    return classes.join(' ')
  }

  /**
   * Handle checkbox click event - standards compliant approach
   */
  private handleCheckboxClick(e: Event): void {
    if (this._disabled) return

    // Don't prevent default - let native events flow naturally for HTMX compatibility
    // Just update our internal state and emit custom events as enhancement
    const newValue = !this._checked

    this._checked = newValue

    // Update form value
    this._internals.setFormValue(
      newValue ? this._value : null
    )

    // Update checked attribute
    if (newValue) {
      this.setAttribute('checked', '')
    } else {
      this.removeAttribute('checked')
    }

    // Re-render to update icon
    this.render()

    // Emit custom events after a brief delay to allow native events to process first
    setTimeout(() => {
      const eventDetail = {
        value: newValue,
        checked: newValue,
        formValue: newValue ? this._value : null,
        originalEvent: e
      }

      this.dispatchEvent(new CustomEvent('input', {
        detail: eventDetail,
        bubbles: true,
        composed: true
      }))

      this.dispatchEvent(new CustomEvent('change', {
        detail: eventDetail,
        bubbles: true,
        composed: true
      }))
    }, 0)
  }

  /**
   * Handle checkbox keyboard events - space/enter to toggle
   */
  private handleCheckboxKeydown(e: KeyboardEvent): void {
    if (this._disabled) return

    if (['Space', ' ', 'Enter'].includes(e.key)) {
      e.preventDefault() // Prevent default for keyboard to avoid page scroll
      e.stopPropagation()
      this.handleCheckboxClick(e)
    }
  }

  /**
   * Remove event listeners for cleanup
   */
  private removeEventListeners(): void {
    if (!this._listenersSetup) return

    const shadow = this.shadowRoot
    if (!shadow) return

    const checkboxEl = shadow.querySelector('.checkbox-container')
    if (!checkboxEl) return

    // Remove all event listeners using stored handler references
    if (this._clickHandler) {
      checkboxEl.removeEventListener('click', this._clickHandler)
      this._clickHandler = null
    }

    if (this._keydownHandler) {
      checkboxEl.removeEventListener('keydown', this._keydownHandler)
      this._keydownHandler = null
    }

    if (this._focusHandler) {
      checkboxEl.removeEventListener('focus', this._focusHandler)
      this._focusHandler = null
    }

    if (this._blurHandler) {
      checkboxEl.removeEventListener('blur', this._blurHandler)
      this._blurHandler = null
    }

    this._listenersSetup = false
  }

  /**
   * Setup event listeners for checkbox functionality
   * IMPORTANT: Only called ONCE, not on every render
   */
  private setupEventListeners(): void {
    if (this._listenersSetup) return

    const shadow = this.shadowRoot!
    const checkboxEl = shadow.querySelector('.checkbox-container')

    if (!checkboxEl) return

    // Create and store handler references for cleanup
    this._clickHandler = (e: Event) => {
      this.handleCheckboxClick(e)
    }

    this._keydownHandler = (e: Event) => {
      this.handleCheckboxKeydown(e as KeyboardEvent)
    }

    this._focusHandler = () => {
      checkboxEl.classList.add('focused')
    }

    this._blurHandler = () => {
      checkboxEl.classList.remove('focused')
    }

    // Add event listeners
    checkboxEl.addEventListener('click', this._clickHandler)
    checkboxEl.addEventListener('keydown', this._keydownHandler)
    checkboxEl.addEventListener('focus', this._focusHandler)
    checkboxEl.addEventListener('blur', this._blurHandler)

    this._listenersSetup = true
  }

  /**
   * Render the checkbox component
   */
  private render(): void {
    const shadow = this.shadowRoot!
    const existingLabel = shadow.querySelector('label')
    const existingCheckbox = shadow.querySelector('.checkbox-container')
    const existingError = shadow.querySelector('.error-message')
    const classes = this.buildClassList()

    // Setup event listeners once we have the checkbox container
    // This handles both new and existing structures
    if (existingCheckbox && !this._listenersSetup) {
      this.setupEventListeners()
    }

    if (existingLabel && existingCheckbox) {
      // Update existing checkbox elements

      // Update label
      if (this._label) {
        existingLabel.innerHTML = `${this._label}${this._required ? `<span class="required-icon">${REQUIRED_ICON_SVG}</span>` : ''}`
        existingLabel.style.display = 'flex'
        existingLabel.style.alignItems = 'center'
      } else {
        existingLabel.style.display = 'none'
      }

      // Update checkbox icon
      const checkboxIcon = existingCheckbox.querySelector('.checkbox-icon')
      if (checkboxIcon) {
        checkboxIcon.innerHTML = this._checked
          ? CHECKBOX_CHECKED_ICON
          : CHECKBOX_UNCHECKED_ICON

        existingCheckbox.className = 'checkbox-container ' + classes

        // Set tabindex for keyboard accessibility
        const checkboxElement = existingCheckbox as HTMLElement
        checkboxElement.tabIndex = this._disabled ? -1 : 0
        existingCheckbox.setAttribute('role', 'checkbox')
        existingCheckbox.setAttribute('aria-checked', String(this._checked))
        existingCheckbox.setAttribute('aria-disabled', String(this._disabled))
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
      // Create new checkbox structure
      const container = document.createElement('div')
      container.className = 'input-container'

      const labelEl = document.createElement('label')
      labelEl.className = 'checkbox-label'

      const checkboxEl = document.createElement('div')
      checkboxEl.className = 'checkbox-container ' + classes

      const checkboxIcon = document.createElement('div')
      checkboxIcon.className = 'checkbox-icon'
      checkboxIcon.innerHTML = this._checked
        ? CHECKBOX_CHECKED_ICON
        : CHECKBOX_UNCHECKED_ICON

      // Accessibility attributes
      checkboxEl.tabIndex = this._disabled ? -1 : 0
      checkboxEl.setAttribute('role', 'checkbox')
      checkboxEl.setAttribute('aria-checked', String(this._checked))
      checkboxEl.setAttribute('aria-disabled', String(this._disabled))

      // Append checkbox icon to container
      checkboxEl.appendChild(checkboxIcon)

      // Build structure
      if (this._label) {
        labelEl.innerHTML = `${this._label}${this._required ? `<span class="required-icon">${REQUIRED_ICON_SVG}</span>` : ''}`
        checkboxEl.appendChild(labelEl)
      }

      container.appendChild(checkboxEl)

      // Add error message if present
      if (this._error) {
        const errorEl = document.createElement('div')
        errorEl.className = 'error-message'
        errorEl.textContent = this._error
        container.appendChild(errorEl)
      }

      // Append new structure to shadow DOM (should be empty on first render)
      shadow.appendChild(container)

      // Setup event listeners after creating structure
      this.setupEventListeners()
    }
  }

  // Public API - Getters/Setters

  get checked(): boolean {
    return this._checked
  }

  set checked(value: boolean) {
    if (this._checked !== value) {
      this._checked = value
      if (value) {
        this.setAttribute('checked', '')
      } else {
        this.removeAttribute('checked')
      }
      // Update form value
      this._internals.setFormValue(
        value ? this._value : null
      )
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
      // Update form value if checked
      if (this._checked) {
        this._internals.setFormValue(val)
      }
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
}

// Register the custom element
if (!customElements.get('ty-checkbox')) {
  customElements.define('ty-checkbox', TyCheckbox)
}
