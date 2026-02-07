/**
 * TyScrollContainer Web Component
 *
 * A scroll container that displays visual shadow indicators at the top/bottom
 * edges when content is scrollable. Shadows appear based on scroll position.
 *
 * @example Basic usage
 * ```html
 * <ty-scroll-container max-height="300px">
 *   <div>Long content...</div>
 * </ty-scroll-container>
 * ```
 *
 * @example Hidden scrollbar
 * ```html
 * <ty-scroll-container max-height="400px" hide-scrollbar>
 *   <ul>...</ul>
 * </ty-scroll-container>
 * ```
 *
 * @example No shadows
 * ```html
 * <ty-scroll-container max-height="200px" shadow="false">
 *   <p>Content</p>
 * </ty-scroll-container>
 * ```
 */

import { ensureStyles } from '../utils/styles.js'
import { scrollContainerStyles } from '../styles/scroll-container.js'

export class TyScrollContainer extends HTMLElement {
  private _scrollWrapper: HTMLElement | null = null
  private _shadowTop: HTMLElement | null = null
  private _shadowBottom: HTMLElement | null = null
  private _resizeObserver: ResizeObserver | null = null
  private _rafId: number | null = null

  static get observedAttributes(): string[] {
    return ['shadow', 'max-height', 'hide-scrollbar']
  }

  /**
   * Enable/disable scroll shadows (default: true)
   */
  get shadow(): boolean {
    const attr = this.getAttribute('shadow')
    return attr !== 'false'
  }

  set shadow(value: boolean) {
    if (value) {
      this.removeAttribute('shadow')
    } else {
      this.setAttribute('shadow', 'false')
    }
  }

  /**
   * Maximum height of the scroll container
   */
  get maxHeight(): string | null {
    return this.getAttribute('max-height')
  }

  set maxHeight(value: string | null) {
    if (value) {
      this.setAttribute('max-height', value)
      this._updateMaxHeight()
    } else {
      this.removeAttribute('max-height')
      this._updateMaxHeight()
    }
  }

  /**
   * Hide native scrollbar
   */
  get hideScrollbar(): boolean {
    return this.hasAttribute('hide-scrollbar')
  }

  set hideScrollbar(value: boolean) {
    if (value) {
      this.setAttribute('hide-scrollbar', '')
    } else {
      this.removeAttribute('hide-scrollbar')
    }
  }

  constructor() {
    super()

    // Setup shadow DOM with styles
    const shadow = this.attachShadow({ mode: 'open' })
    ensureStyles(shadow, { css: scrollContainerStyles, id: 'ty-scroll-container' })

    // Create structure
    shadow.innerHTML = `
      <div class="scroll-wrapper">
        <slot></slot>
      </div>
      <div class="shadow-overlay">
        <div class="shadow-top"></div>
        <div class="shadow-bottom"></div>
      </div>
    `

    // Cache DOM references
    this._scrollWrapper = shadow.querySelector('.scroll-wrapper')
    this._shadowTop = shadow.querySelector('.shadow-top')
    this._shadowBottom = shadow.querySelector('.shadow-bottom')
  }

  connectedCallback(): void {
    // Setup scroll listener (passive for performance)
    this._scrollWrapper?.addEventListener('scroll', this._onScroll, { passive: true })

    // Setup ResizeObserver for content changes
    this._resizeObserver = new ResizeObserver(() => {
      this._updateShadowState()
    })

    // Observe the scroll wrapper for size changes
    if (this._scrollWrapper) {
      this._resizeObserver.observe(this._scrollWrapper)
    }

    // Update max-height CSS variable
    this._updateMaxHeight()

    // Initial shadow state check
    this._updateShadowState()
  }

  disconnectedCallback(): void {
    // Cleanup scroll listener
    this._scrollWrapper?.removeEventListener('scroll', this._onScroll)

    // Cleanup ResizeObserver
    if (this._resizeObserver) {
      this._resizeObserver.disconnect()
      this._resizeObserver = null
    }

    // Cancel any pending rAF
    if (this._rafId !== null) {
      cancelAnimationFrame(this._rafId)
      this._rafId = null
    }
  }

  attributeChangedCallback(name: string, oldValue: string | null, newValue: string | null): void {
    if (oldValue === newValue) return

    if (name === 'max-height') {
      this._updateMaxHeight()
      // Recheck shadows after max-height change
      this._updateShadowState()
    }
  }

  /**
   * Update the CSS variable for max-height
   */
  private _updateMaxHeight(): void {
    if (this._scrollWrapper) {
      const maxHeight = this.maxHeight
      if (maxHeight) {
        this._scrollWrapper.style.setProperty('--scroll-max-height', maxHeight)
        this._scrollWrapper.style.maxHeight = maxHeight
      } else {
        this._scrollWrapper.style.removeProperty('--scroll-max-height')
        this._scrollWrapper.style.maxHeight = ''
      }
    }
  }

  /**
   * Scroll event handler with rAF debounce
   */
  private _onScroll = (): void => {
    if (this._rafId !== null) return

    this._rafId = requestAnimationFrame(() => {
      this._rafId = null
      this._updateShadowState()
    })
  }

  /**
   * Update shadow visibility based on scroll position
   */
  private _updateShadowState(): void {
    if (!this._scrollWrapper || !this._shadowTop || !this._shadowBottom) return

    const { scrollTop, scrollHeight, clientHeight } = this._scrollWrapper
    const threshold = 2 // Small threshold to avoid floating point issues

    // Show top shadow if scrolled down
    const showTop = scrollTop > threshold
    this._shadowTop.classList.toggle('visible', showTop)

    // Show bottom shadow if more content below
    const showBottom = scrollTop + clientHeight < scrollHeight - threshold
    this._shadowBottom.classList.toggle('visible', showBottom)
  }

  // ============ Public API ============

  /**
   * Force update shadows (useful after dynamic content changes)
   */
  updateShadows(): void {
    this._updateShadowState()
  }

  /**
   * Scroll to top
   */
  scrollToTop(smooth = true): void {
    this._scrollWrapper?.scrollTo({
      top: 0,
      behavior: smooth ? 'smooth' : 'auto'
    })
  }

  /**
   * Scroll to bottom
   */
  scrollToBottom(smooth = true): void {
    if (this._scrollWrapper) {
      this._scrollWrapper.scrollTo({
        top: this._scrollWrapper.scrollHeight,
        behavior: smooth ? 'smooth' : 'auto'
      })
    }
  }

  /**
   * Get the scroll wrapper element (for advanced use cases)
   */
  get scrollElement(): HTMLElement | null {
    return this._scrollWrapper
  }
}

// Register custom element
customElements.define('ty-scroll-container', TyScrollContainer)
