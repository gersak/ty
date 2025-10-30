/**
 * TyButton Web Component
 * PORTED FROM: clj/ty/components/button.cljs
 * Maintains full compatibility with ClojureScript version
 */

import type { Flavor, Size, TyButtonElement } from '../types/common.js'
import { ensureStyles, buildClassList } from '../utils/styles.js'
import { buttonStyles } from '../styles/button.js'

/**
 * Ty Button Component
 * 
 * @example
 * ```html
 * <ty-button flavor="primary" size="md">Click Me</ty-button>
 * <ty-button flavor="danger" outlined disabled>Disabled</ty-button>
 * <ty-button flavor="success" pill>Pill Button</ty-button>
 * ```
 */
export class TyButton extends HTMLElement implements TyButtonElement {
  static formAssociated = true

  private _internals: ElementInternals
  private _flavor: Flavor = 'neutral'
  private _size: Size = 'md'
  private _disabled = false
  private _type: 'button' | 'submit' | 'reset' = 'submit' // Default to submit like native buttons

  // Appearance modifiers (matches ClojureScript logic)
  private _pill = false
  private _outlined = false
  private _filled = false
  private _accent = false
  private _plain = false
  private _action = false
  private _wide = false

  constructor() {
    super()
    this._internals = this.attachInternals()

    const shadow = this.attachShadow({ mode: 'open' })
    ensureStyles(shadow, { css: buttonStyles, id: 'ty-button' })

    // Create button structure immediately in constructor to prevent layout shifts
    // This ensures slots exist before any slotted content (like ty-icon) tries to render
    this.initializeButtonStructure()
  }

  static get observedAttributes(): string[] {
    return [
      'flavor', 'size', 'disabled', 'type',
      'pill', 'outlined', 'filled', 'accent', 'plain', 'action', 'wide',
      'name', 'value'
    ]
  }

  attributeChangedCallback(name: string, _oldValue: string | null, newValue: string | null): void {
    switch (name) {
      case 'flavor':
        this._flavor = this.validateFlavor(newValue)
        break
      case 'size':
        this._size = (newValue as Size) || 'md'
        break
      case 'disabled':
        this._disabled = newValue !== null
        break
      case 'type':
        this._type = (newValue as 'button' | 'submit' | 'reset') || 'submit'
        break
      case 'pill':
        this._pill = newValue !== null
        break
      case 'outlined':
        this._outlined = newValue !== null
        break
      case 'filled':
        this._filled = newValue !== null
        break
      case 'accent':
        this._accent = newValue !== null
        break
      case 'plain':
        this._plain = newValue !== null
        break
      case 'action':
        this._action = newValue !== null
        break
      case 'wide':
        this._wide = newValue !== null
        break
    }
    this.render()
  }

  // Validate flavor (matches ClojureScript validation)
  private validateFlavor(flavor: string | null): Flavor {
    const validFlavors: Flavor[] = ['primary', 'secondary', 'success', 'danger', 'warning', 'neutral']
    const normalized = (flavor || 'neutral') as Flavor

    if (!validFlavors.includes(normalized)) {
      console.warn(`[ty-button] Invalid flavor '${flavor}'. Using 'neutral'. Valid flavors: ${validFlavors.join(', ')}`)
      return 'neutral'
    }

    return normalized
  }

  // Public API - Getters/Setters
  // NOTE: All setters trigger re-render to support property changes from frameworks like React
  // React sets properties (btn.flavor = 'danger') not attributes, so we must detect and render
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

  get type(): 'button' | 'submit' | 'reset' {
    return this._type
  }

  set type(value: 'button' | 'submit' | 'reset') {
    if (this._type !== value) {
      this._type = value
      this.setAttribute('type', value)
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
      } else {
        this.removeAttribute('pill')
      }
      this.render()
    }
  }

  get outlined(): boolean {
    return this._outlined
  }

  set outlined(value: boolean) {
    if (this._outlined !== value) {
      this._outlined = value
      if (value) {
        this.setAttribute('outlined', '')
      } else {
        this.removeAttribute('outlined')
      }
      this.render()
    }
  }

  get filled(): boolean {
    return this._filled
  }

  set filled(value: boolean) {
    if (this._filled !== value) {
      this._filled = value
      if (value) {
        this.setAttribute('filled', '')
      } else {
        this.removeAttribute('filled')
      }
      this.render()
    }
  }

  get accent(): boolean {
    return this._accent
  }

  set accent(value: boolean) {
    if (this._accent !== value) {
      this._accent = value
      if (value) {
        this.setAttribute('accent', '')
      } else {
        this.removeAttribute('accent')
      }
      this.render()
    }
  }

  get plain(): boolean {
    return this._plain
  }

  set plain(value: boolean) {
    if (this._plain !== value) {
      this._plain = value
      if (value) {
        this.setAttribute('plain', '')
      } else {
        this.removeAttribute('plain')
      }
      this.render()
    }
  }

  get action(): boolean {
    return this._action
  }

  set action(value: boolean) {
    if (this._action !== value) {
      this._action = value
      if (value) {
        this.setAttribute('action', '')
      } else {
        this.removeAttribute('action')
      }
      this.render()
    }
  }

  get wide(): boolean {
    return this._wide
  }

  set wide(value: boolean) {
    if (this._wide !== value) {
      this._wide = value
      if (value) {
        this.setAttribute('wide', 'true')
      } else {
        this.removeAttribute('wide')
      }
      // No need to render() - wide is handled by CSS attribute selector
    }
  }

  // Form association
  get form(): HTMLFormElement | null {
    return this._internals.form
  }

  get name(): string {
    return this.getAttribute('name') || ''
  }

  get value(): string {
    return this.getAttribute('value') || ''
  }

  // Build class list (matches ClojureScript logic exactly)
  private buildClasses(): string {
    // Appearance logic (matches ClojureScript cond logic)
    let appearance: string
    if (this._plain) {
      appearance = 'plain'
    } else if (this._accent) {
      appearance = 'accent'
    } else if (this._filled && this._outlined) {
      appearance = 'filled-outlined'
    } else if (this._filled) {
      appearance = 'filled'
    } else if (this._outlined) {
      appearance = 'outlined'
    } else {
      appearance = 'accent' // Default
    }

    return buildClassList(
      this._flavor,
      this._size,
      appearance,
      this._pill && 'pill',
      this._action && 'action'
    )
  }

  // Handle form actions (matches ClojureScript logic)
  private handleFormAction(): void {
    const form = this._internals.form
    if (!form) return

    switch (this._type) {
      case 'submit':
        // Set button's name/value as form data
        if (this.name && this.value) {
          this._internals.setFormValue(this.value)
        }
        form.requestSubmit()
        break
      case 'reset':
        form.reset()
        break
      case 'button':
        // Do nothing
        break
    }
  }

  /** Initialize button structure once in constructor */
  private initializeButtonStructure(): void {
    const shadow = this.shadowRoot!
    const classes = this.buildClasses()

    // Create button structure (matches ClojureScript structure)
    const button = document.createElement('button')
    button.disabled = this._disabled
    button.className = classes

    const startSlot = document.createElement('slot')
    startSlot.name = 'start'
    startSlot.className = 'start'

    const defaultSlot = document.createElement('slot')

    const endSlot = document.createElement('slot')
    endSlot.name = 'end'
    endSlot.className = 'end'

    button.appendChild(startSlot)
    button.appendChild(defaultSlot)
    button.appendChild(endSlot)

    // Attach click handler to inner button (matches ClojureScript pattern)
    button.addEventListener('click', (e: Event) => {
      if (this._disabled) return

      // CRITICAL: Stop propagation on inner button click (matches ClojureScript)
      e.stopPropagation()

      // Handle form action (submit/reset) first
      this.handleFormAction()

      // Dispatch NEW 'click' event on host element (not 'ty-click'!)
      // This matches ClojureScript exactly
      this.dispatchEvent(new CustomEvent('click', {
        bubbles: true,
        composed: true,
        detail: { originalEvent: e }
      }))
    })

    shadow.appendChild(button)
  }

  private render(): void {
    const classes = this.buildClasses()
    const shadow = this.shadowRoot!

    // Button structure already exists from constructor, just update it
    const button = shadow.querySelector('button')
    if (button) {
      button.disabled = this._disabled
      button.className = classes
    }
  }
}

// Register the custom element
if (!customElements.get('ty-button')) {
  customElements.define('ty-button', TyButton)
}
