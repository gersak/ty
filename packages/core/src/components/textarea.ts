/**
 * ty-textarea - Enhanced textarea component with auto-resize functionality
 * 
 * Features:
 * - Auto-resize based on content
 * - Form-associated custom element
 * - Min/max height constraints
 * - Size variants (xs, sm, md, lg, xl)
 * - Validation states (error, required)
 * - Manual resize control options
 * - Accessibility support
 * 
 * @example
 * ```html
 * <!-- Basic usage -->
 * <ty-textarea label="Description" placeholder="Enter details..."></ty-textarea>
 * 
 * <!-- With validation -->
 * <ty-textarea 
 *   label="Bio" 
 *   required 
 *   error="Bio is required"
 *   size="lg">
 * </ty-textarea>
 * 
 * <!-- With height constraints -->
 * <ty-textarea 
 *   min-height="100px" 
 *   max-height="300px"
 *   placeholder="Auto-resizing textarea">
 * </ty-textarea>
 * 
 * <!-- Manual resize control -->
 * <ty-textarea resize="vertical"></ty-textarea>
 * ```
 * 
 * @fires {CustomEvent<{value: string, originalEvent: Event}>} input - Emitted on input
 * @fires {CustomEvent<{value: string, originalEvent: Event}>} change - Emitted on change
 */

import { ensureStyles } from '../utils/styles.js'
import { textareaStyles } from '../styles/textarea.js'

/**
 * Required icon SVG (matches input.ts)
 */
const REQUIRED_ICON = `<svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>`

/**
 * Component internal state
 */
interface TextareaState {
  value: string
  isFocused: boolean
  dummyStyle: CSSStyleDeclaration | null
}

/**
 * Font style properties for dummy element
 */
interface FontStyle {
  fontFamily: string
  fontSize: string
  fontWeight: string
  lineHeight: string
  letterSpacing: string
  fontStyle: string
}

/**
 * Spacing style properties for dummy element
 */
interface SpacingStyle {
  padding: string
  paddingTop: string
  paddingRight: string
  paddingBottom: string
  paddingLeft: string
  margin: string
  borderWidth: string
}

export interface TyTextareaElement extends HTMLElement {
  value?: string
  name?: string
  placeholder?: string
  label?: string
  disabled?: boolean
  required?: boolean
  error?: string
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl'
  rows?: string | number
  cols?: string | number
  resize?: 'none' | 'both' | 'horizontal' | 'vertical'
  minHeight?: string
  maxHeight?: string
}

export class TyTextarea extends HTMLElement implements TyTextareaElement {
  static formAssociated = true

  private _internals: ElementInternals
  private _state: TextareaState
  private _textareaEl: HTMLTextAreaElement | null = null
  private _dummyEl: HTMLPreElement | null = null
  private _isInitialized = false

  // Property backing fields
  private _value = ''
  private _name = ''
  private _placeholder = ''
  private _label = ''
  private _disabled = false
  private _required = false
  private _error = ''
  private _size: 'xs' | 'sm' | 'md' | 'lg' | 'xl' = 'md'
  private _rows = '3'
  private _cols = ''
  private _resize: 'none' | 'both' | 'horizontal' | 'vertical' = 'none'
  private _minHeight = ''
  private _maxHeight = ''

  constructor() {
    super()
    const shadow = this.attachShadow({ mode: 'open' })
    this._internals = this.attachInternals()
    ensureStyles(shadow, { css: textareaStyles, id: 'ty-textarea' })

    // Initialize state
    this._state = {
      value: this._value,
      isFocused: false,
      dummyStyle: null
    }
  }

  static get observedAttributes(): string[] {
    return [
      'value',
      'name',
      'placeholder',
      'label',
      'disabled',
      'required',
      'error',
      'size',
      'class',
      'rows',
      'cols',
      'resize',
      'min-height',
      'max-height'
    ]
  }

  // ===== LIFECYCLE CALLBACKS =====

  connectedCallback(): void {
    // CRITICAL: Reagent/React may set properties BEFORE the element is constructed
    // Check if value was set directly on the instance before our getter/setter was available
    const instanceValue = Object.getOwnPropertyDescriptor(this, 'value')
    if (instanceValue && instanceValue.value !== undefined) {
      this._value = instanceValue.value
      this._state.value = instanceValue.value
      // Clean up the instance property so our getter/setter works
      Reflect.deleteProperty(this, 'value')
    }
    
    // Initialize value from attribute if present
    const attrValue = this.getAttribute('value')
    if (attrValue !== null) {
      this._value = attrValue
      this._state.value = attrValue
    }

    this._isInitialized = true
    this.render()

    // Set up form value synchronization
    this._internals.setFormValue(this._value || '')
  }

  disconnectedCallback(): void {
    // Cleanup event listeners if needed
    if (this._textareaEl) {
      const newTextarea = this._textareaEl.cloneNode(true)
      this._textareaEl.parentNode?.replaceChild(newTextarea, this._textareaEl)
      this._textareaEl = null
    }
  }

  attributeChangedCallback(name: string, oldValue: string | null, newValue: string | null): void {
    if (oldValue === newValue) return

    switch (name) {
      case 'value':
        this._value = newValue || ''
        this._state.value = this._value
        this._internals.setFormValue(this._value)
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
        break
      case 'size':
        this._size = (newValue as any) || 'md'
        break
      case 'rows':
        this._rows = newValue || '3'
        break
      case 'cols':
        this._cols = newValue || ''
        break
      case 'resize':
        this._resize = (newValue as any) || 'none'
        break
      case 'min-height':
        this._minHeight = newValue || ''
        break
      case 'max-height':
        this._maxHeight = newValue || ''
        break
    }

    if (this._isInitialized) {
      this.render()
    }
  }

  // ===== PUBLIC API (Properties) =====

  get value(): string { return this._value }
  set value(val: string) {
    if (this._value !== val) {
      this._value = val
      this._state.value = val
      this.setAttribute('value', val)
      this._internals.setFormValue(val)
      if (this._isInitialized) this.render()
    }
  }

  get name(): string { return this._name }
  set name(val: string) {
    if (this._name !== val) {
      this._name = val
      this.setAttribute('name', val)
    }
  }

  get placeholder(): string { return this._placeholder }
  set placeholder(val: string) {
    if (this._placeholder !== val) {
      this._placeholder = val
      this.setAttribute('placeholder', val)
    }
  }

  get label(): string { return this._label }
  set label(val: string) {
    if (this._label !== val) {
      this._label = val
      this.setAttribute('label', val)
    }
  }

  get disabled(): boolean { return this._disabled }
  set disabled(val: boolean) {
    if (this._disabled !== val) {
      this._disabled = val
      if (val) {
        this.setAttribute('disabled', '')
      } else {
        this.removeAttribute('disabled')
      }
    }
  }

  get required(): boolean { return this._required }
  set required(val: boolean) {
    if (this._required !== val) {
      this._required = val
      if (val) {
        this.setAttribute('required', '')
      } else {
        this.removeAttribute('required')
      }
    }
  }

  get error(): string { return this._error }
  set error(val: string) {
    if (this._error !== val) {
      this._error = val
      this.setAttribute('error', val)
    }
  }

  get size(): 'xs' | 'sm' | 'md' | 'lg' | 'xl' { return this._size }
  set size(val: 'xs' | 'sm' | 'md' | 'lg' | 'xl') {
    if (this._size !== val) {
      this._size = val
      this.setAttribute('size', val)
    }
  }

  get rows(): string { return this._rows }
  set rows(val: string | number) {
    const strVal = String(val)
    if (this._rows !== strVal) {
      this._rows = strVal
      this.setAttribute('rows', strVal)
    }
  }

  get cols(): string { return this._cols }
  set cols(val: string | number) {
    const strVal = String(val)
    if (this._cols !== strVal) {
      this._cols = strVal
      this.setAttribute('cols', strVal)
    }
  }

  get resize(): 'none' | 'both' | 'horizontal' | 'vertical' { return this._resize }
  set resize(val: 'none' | 'both' | 'horizontal' | 'vertical') {
    if (this._resize !== val) {
      this._resize = val
      this.setAttribute('resize', val)
    }
  }

  get minHeight(): string { return this._minHeight }
  set minHeight(val: string) {
    if (this._minHeight !== val) {
      this._minHeight = val
      this.setAttribute('min-height', val)
    }
  }

  get maxHeight(): string { return this._maxHeight }
  set maxHeight(val: string) {
    if (this._maxHeight !== val) {
      this._maxHeight = val
      this.setAttribute('max-height', val)
    }
  }

  // ===== HELPER METHODS =====

  /**
   * Extract font-related CSS properties from element
   */
  private getFontStyle(el: HTMLElement): FontStyle {
    const computedStyle = window.getComputedStyle(el)
    return {
      fontFamily: computedStyle.fontFamily,
      fontSize: computedStyle.fontSize,
      fontWeight: computedStyle.fontWeight,
      lineHeight: computedStyle.lineHeight,
      letterSpacing: computedStyle.letterSpacing,
      fontStyle: computedStyle.fontStyle
    }
  }

  /**
   * Extract spacing-related CSS properties from element
   */
  private getSpacingStyle(el: HTMLElement): SpacingStyle {
    const computedStyle = window.getComputedStyle(el)
    return {
      padding: computedStyle.padding,
      paddingTop: computedStyle.paddingTop,
      paddingRight: computedStyle.paddingRight,
      paddingBottom: computedStyle.paddingBottom,
      paddingLeft: computedStyle.paddingLeft,
      margin: computedStyle.margin,
      borderWidth: computedStyle.borderWidth
    }
  }

  /**
   * Setup the hidden dummy element for height measurement
   */
  private setupDummyElement(textareaEl: HTMLTextAreaElement): HTMLPreElement {
    const root = this.shadowRoot!
    const existingDummy = root.querySelector<HTMLPreElement>('.textarea-dummy')

    // Remove existing dummy if present
    if (existingDummy) {
      existingDummy.remove()
    }

    // Create new dummy element
    const dummyEl = document.createElement('pre')
    const fontStyle = this.getFontStyle(textareaEl)
    const spacingStyle = this.getSpacingStyle(textareaEl)

    // Set dummy element class
    dummyEl.className = 'textarea-dummy'

    // Apply styles to match textarea
    const style = dummyEl.style
    style.position = 'absolute'
    style.top = '0'
    style.left = '0'
    style.visibility = 'hidden'
    style.whiteSpace = 'pre-wrap'
    style.wordBreak = 'break-word'
    style.boxSizing = 'border-box'
    style.overflow = 'hidden'
    style.pointerEvents = 'none'

    // Apply font styles
    Object.entries(fontStyle).forEach(([prop, value]) => {
      ;(style as any)[prop] = value
    })

    // Apply spacing styles
    Object.entries(spacingStyle).forEach(([prop, value]) => {
      ;(style as any)[prop] = value
    })

    // Append to shadow root
    root.appendChild(dummyEl)

    this._dummyEl = dummyEl
    return dummyEl
  }

  /**
   * Resize textarea based on dummy element content with min/max height constraints
   */
  private resizeTextarea(textareaEl: HTMLTextAreaElement, dummyEl: HTMLPreElement): void {
    if (!textareaEl || !dummyEl) return

    const { value } = this._state
    const placeholder = this._placeholder
    const content = (value === '' && placeholder) ? placeholder : value + ' '
    const currentHeight = textareaEl.clientHeight

    // Set dummy content
    dummyEl.textContent = content

    // Set dummy width to match textarea
    dummyEl.style.width = `${textareaEl.clientWidth}px`

    // Get measured height
    const measuredHeight = dummyEl.scrollHeight

    // Parse min/max heights
    const minHeight = this._minHeight ? parseInt(this._minHeight.replace('px', ''), 10) : null
    const maxHeight = this._maxHeight ? parseInt(this._maxHeight.replace('px', ''), 10) : null

    // Apply constraints
    let constrainedHeight = measuredHeight
    if (minHeight !== null) constrainedHeight = Math.max(constrainedHeight, minHeight)
    if (maxHeight !== null) constrainedHeight = Math.min(constrainedHeight, maxHeight)

    // Only update if height changed significantly (avoid micro-adjustments)
    if (Math.abs(constrainedHeight - currentHeight) > 2) {
      textareaEl.style.height = `${constrainedHeight}px`

      // Set overflow based on whether we hit max-height
      if (maxHeight !== null && measuredHeight >= maxHeight) {
        textareaEl.style.overflowY = 'auto'
      } else {
        textareaEl.style.overflowY = 'hidden'
      }
    }
  }

  /**
   * Emit custom input and change events
   */
  private emitValueEvents(originalEvent: Event): void {
    const { value } = this._state

    // Update form internals
    this._internals.setFormValue(value || '')

    // Emit custom events
    const data = {
      bubbles: true,
      composed: true,
      detail: {
        value,
        originalEvent
      }
    }

    // Use setTimeout to ensure these fire after standard events
    setTimeout(() => {
      this.dispatchEvent(new CustomEvent('input', data))
      this.dispatchEvent(new CustomEvent('change', data))
    }, 0)
  }

  /**
   * Handle textarea input event
   */
  private handleInputEvent = (e: Event): void => {
    const target = e.target as HTMLTextAreaElement
    const newValue = target.value

    this._state.value = newValue
    this._value = newValue

    // Trigger resize after state update
    if (this._textareaEl && this._dummyEl) {
      this.resizeTextarea(this._textareaEl, this._dummyEl)
    }

    // Emit custom events
    this.emitValueEvents(e)
  }

  /**
   * Handle textarea focus event
   */
  private handleFocusEvent = (): void => {
    this._state.isFocused = true
  }

  /**
   * Handle textarea blur event
   */
  private handleBlurEvent = (): void => {
    this._state.isFocused = false
  }

  /**
   * Build class list for textarea element
   */
  private buildClassList(): string {
    const classes: string[] = [this._size]

    if (this._disabled) classes.push('disabled')
    if (this._required) classes.push('required')
    if (this._error) classes.push('error')
    if (this._resize !== 'none') classes.push(`resize-${this._resize}`)

    const customClass = this.getAttribute('class')
    if (customClass) classes.push(customClass)

    return classes.join(' ')
  }

  /**
   * Apply min/max height constraints to textarea element
   */
  private applyHeightConstraints(textareaEl: HTMLTextAreaElement): void {
    if (this._minHeight) {
      textareaEl.style.minHeight = this._minHeight
    }
    if (this._maxHeight) {
      textareaEl.style.maxHeight = this._maxHeight
    }
  }

  /**
   * Setup event listeners for textarea
   */
  private setupTextareaEvents(textareaEl: HTMLTextAreaElement): void {
    textareaEl.addEventListener('input', this.handleInputEvent)
    textareaEl.addEventListener('change', this.handleInputEvent)
    textareaEl.addEventListener('focus', this.handleFocusEvent)
    textareaEl.addEventListener('blur', this.handleBlurEvent)
  }

  // ===== RENDER =====

  private render(): void {
    const root = this.shadowRoot!
    const existingContainer = root.querySelector<HTMLDivElement>('.textarea-container')
    const existingLabel = root.querySelector<HTMLLabelElement>('label')
    const existingTextarea = root.querySelector<HTMLTextAreaElement>('textarea')
    const existingError = root.querySelector<HTMLDivElement>('.error-message')

    if (existingContainer && existingTextarea) {
      // Update existing elements
      
      // Update label
      if (this._label) {
        if (existingLabel) {
          existingLabel.innerHTML = this._label + 
            (this._required ? ` <span class="required-icon">${REQUIRED_ICON}</span>` : '')
          existingLabel.style.display = 'block'
        }
      } else if (existingLabel) {
        existingLabel.style.display = 'none'
      }

      // Update textarea
      existingTextarea.value = this._value || ''
      existingTextarea.placeholder = this._placeholder || ''
      existingTextarea.disabled = this._disabled
      existingTextarea.required = this._required
      existingTextarea.className = this.buildClassList()

      // Set name for form association
      if (this._name) {
        existingTextarea.setAttribute('name', this._name)
      } else {
        existingTextarea.removeAttribute('name')
      }

      // Set rows/cols
      if (this._rows) existingTextarea.setAttribute('rows', this._rows)
      if (this._cols) existingTextarea.setAttribute('cols', this._cols)

      // Apply height constraints
      this.applyHeightConstraints(existingTextarea)

      // Update error message
      if (this._error) {
        if (!existingError) {
          const errorEl = document.createElement('div')
          errorEl.className = 'error-message'
          existingContainer.appendChild(errorEl)
        }
        const errorEl = root.querySelector<HTMLDivElement>('.error-message')
        if (errorEl) errorEl.textContent = this._error
      } else if (existingError) {
        existingError.remove()
      }

      // Setup dummy element and trigger resize
      const dummyEl = this.setupDummyElement(existingTextarea)
      setTimeout(() => this.resizeTextarea(existingTextarea, dummyEl), 0)

      this._textareaEl = existingTextarea

    } else {
      // Create new structure
      const container = document.createElement('div')
      container.className = 'textarea-container'

      const labelEl = document.createElement('label')
      labelEl.className = 'textarea-label'
      if (this._label) {
        labelEl.innerHTML = this._label + 
          (this._required ? ` <span class="required-icon">${REQUIRED_ICON}</span>` : '')
        labelEl.style.display = 'block'
      } else {
        labelEl.style.display = 'none'
      }

      const textareaEl = document.createElement('textarea')
      textareaEl.value = this._value || ''
      textareaEl.placeholder = this._placeholder || ''
      textareaEl.disabled = this._disabled
      textareaEl.required = this._required
      textareaEl.className = this.buildClassList()

      // Set name for form association
      if (this._name) {
        textareaEl.setAttribute('name', this._name)
      }

      // Set default or specified rows/cols
      textareaEl.setAttribute('rows', this._rows || '3')
      if (this._cols) textareaEl.setAttribute('cols', this._cols)

      // Apply height constraints
      this.applyHeightConstraints(textareaEl)

      // Setup event listeners
      this.setupTextareaEvents(textareaEl)

      // Build structure
      container.appendChild(labelEl)
      container.appendChild(textareaEl)

      // Add error message if present
      if (this._error) {
        const errorEl = document.createElement('div')
        errorEl.className = 'error-message'
        errorEl.textContent = this._error
        container.appendChild(errorEl)
      }

      root.appendChild(container)

      // Setup dummy element and trigger initial resize
      const dummyEl = this.setupDummyElement(textareaEl)
      setTimeout(() => this.resizeTextarea(textareaEl, dummyEl), 0)

      this._textareaEl = textareaEl
    }
  }
}

// Register the component
if (!customElements.get('ty-textarea')) {
  customElements.define('ty-textarea', TyTextarea)
}