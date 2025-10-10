/**
 * TyIcon Web Component
 * PORTED FROM: clj/ty/components/icon.cljs
 * SVG icon component with size variants and animations
 */

import type { IconSize, IconTempo, TyIconElement } from '../types/common.js'
import { ensureStyles } from '../utils/styles.js'
import { iconStyles } from '../styles/icon.js'
import * as IconRegistry from '../utils/icon-registry.js'

/** Fallback SVG for missing icons */
const NOT_FOUND_ICON = `<svg xmlns="http://www.w3.org/2000/svg" 
      viewBox="0 0 512 512" 
      fill="currentColor" 
      opacity="0.2">
   <path d="M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512z"/>
</svg>`

/**
 * Ty Icon Component
 * 
 * @example
 * ```html
 * <ty-icon name="check"></ty-icon>
 * <ty-icon name="spinner" spin></ty-icon>
 * <ty-icon name="heart" size="xl"></ty-icon>
 * ```
 */
export class TyIcon extends HTMLElement implements TyIconElement {
  private _name: string = ''
  private _size: IconSize | undefined = undefined
  private _spin = false
  private _pulse = false
  private _tempo: IconTempo | undefined = undefined
  private _watchId: string | null = null

  constructor() {
    super()

    const shadow = this.attachShadow({ mode: 'open' })
    ensureStyles(shadow, { css: iconStyles, id: 'ty-icon' })

    this.render()
  }

  static get observedAttributes(): string[] {
    return ['name', 'size', 'spin', 'pulse', 'tempo']
  }

  connectedCallback(): void {
    // Generate unique watch ID
    this._watchId = `ty-icon-${crypto.randomUUID()}`

    // Watch for registry changes that affect this icon
    if (this._name) {
      IconRegistry.addWatcher(this._watchId, (icons) => {
        // Re-render if our icon changed
        if (this._name && icons.has(this._name)) {
          this.render()
        }
      })
    }

    // Initial render
    this.render()
  }

  disconnectedCallback(): void {
    // Clean up watcher
    if (this._watchId) {
      IconRegistry.removeWatcher(this._watchId)
      this._watchId = null
    }
  }

  attributeChangedCallback(name: string, oldValue: string | null, newValue: string | null): void {
    if (oldValue === newValue) return

    switch (name) {
      case 'name':
        if (this._name !== (newValue || '')) {
          // Update watch when name changes
          if (this._watchId) {
            IconRegistry.removeWatcher(this._watchId)
          }
          
          this._name = newValue || ''
          
          // Re-add watcher for new name
          if (this._name && this.isConnected) {
            this._watchId = `ty-icon-${crypto.randomUUID()}`
            IconRegistry.addWatcher(this._watchId, (icons) => {
              if (this._name && icons.has(this._name)) {
                this.render()
              }
            })
          }
        }
        break
      case 'size':
        this._size = (newValue as IconSize) || null
        break
      case 'spin':
        this._spin = newValue !== null
        break
      case 'pulse':
        this._pulse = newValue !== null
        break
      case 'tempo':
        this._tempo = (newValue as IconTempo) || null
        break
    }

    this.updateClasses()
    this.render()
  }

  // Public API - Getters/Setters
  // NOTE: All setters trigger re-render to support property changes from frameworks like React
  
  get name(): string {
    return this._name
  }

  set name(value: string) {
    if (this._name !== value) {
      this._name = value
      this.setAttribute('name', value)
      this.render()
    }
  }

  get size(): IconSize | undefined {
    return this._size
  }

  set size(value: IconSize | undefined) {
    if (this._size !== value) {
      this._size = value
      if (value) {
        this.setAttribute('size', value)
      } else {
        this.removeAttribute('size')
      }
      this.updateClasses()
    }
  }

  get spin(): boolean {
    return this._spin
  }

  set spin(value: boolean) {
    if (this._spin !== value) {
      this._spin = value
      if (value) {
        this.setAttribute('spin', '')
      } else {
        this.removeAttribute('spin')
      }
      this.updateClasses()
    }
  }

  get pulse(): boolean {
    return this._pulse
  }

  set pulse(value: boolean) {
    if (this._pulse !== value) {
      this._pulse = value
      if (value) {
        this.setAttribute('pulse', '')
      } else {
        this.removeAttribute('pulse')
      }
      this.updateClasses()
    }
  }

  get tempo(): IconTempo | undefined {
    return this._tempo
  }

  set tempo(value: IconTempo | undefined) {
    if (this._tempo !== value) {
      this._tempo = value
      if (value) {
        this.setAttribute('tempo', value)
      } else {
        this.removeAttribute('tempo')
      }
      this.updateClasses()
    }
  }

  /** Build CSS classes for the host element */
  private buildClasses(): string[] {
    const classes: string[] = []

    // Size class
    if (this._size) {
      classes.push(`icon-${this._size}`)
    }

    // Animation classes
    if (this._spin) {
      classes.push('icon-spin')
    }

    if (this._pulse) {
      classes.push('icon-pulse')
    }

    // Tempo class
    if (this._tempo && (this._spin || this._pulse)) {
      classes.push(`icon-tempo-${this._tempo}`)
    }

    // Preserve user-added classes that don't conflict
    const currentClasses = this.className.split(/\s+/).filter(cls => {
      // Filter out component-managed classes
      return cls && 
        !cls.startsWith('icon-') &&
        cls !== 'icon-spin' &&
        cls !== 'icon-pulse'
    })

    return [...classes, ...currentClasses]
  }

  /** Update host element classes */
  private updateClasses(): void {
    const newClasses = this.buildClasses().join(' ')
    if (this.className !== newClasses) {
      this.className = newClasses
    }
  }

  /** Render the icon SVG */
  private render(): void {
    const shadow = this.shadowRoot!

    // Get icon SVG from registry
    const iconSvg = this._name 
      ? (IconRegistry.getIcon(this._name) || NOT_FOUND_ICON)
      : NOT_FOUND_ICON

    // Update shadow DOM content
    shadow.innerHTML = iconSvg
  }
}

// Register the custom element
if (!customElements.get('ty-icon')) {
  customElements.define('ty-icon', TyIcon)
}

// Export for use in other modules
export { IconRegistry }
