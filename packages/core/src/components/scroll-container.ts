/**
 * TyScrollContainer Web Component
 *
 * A scroll container with shadow indicators and an optional custom-rendered scrollbar.
 * Uses the CustomScrollbar utility internally.
 *
 * @example Custom scrollbar
 * ```html
 * <ty-scroll-container max-height="300px" custom-scrollbar>
 *   <div>Long content...</div>
 * </ty-scroll-container>
 * ```
 */

import { ensureStyles } from '../utils/styles.js'
import { scrollContainerStyles } from '../styles/scroll-container.js'
import { CustomScrollbar } from '../utils/custom-scrollbar.js'

export class TyScrollContainer extends HTMLElement {
  private _scrollWrapper: HTMLElement | null = null
  private _shadowTop: HTMLElement | null = null
  private _shadowBottom: HTMLElement | null = null
  private _shadowLeft: HTMLElement | null = null
  private _shadowRight: HTMLElement | null = null
  private _scrollbar: CustomScrollbar | null = null
  private _resizeObserver: ResizeObserver | null = null
  private _rafId: number | null = null

  static get observedAttributes(): string[] {
    return ['shadow', 'max-height', 'hide-scrollbar', 'custom-scrollbar', 'overflow-x']
  }

  // ============ Property Accessors ============

  get shadow(): boolean {
    return this.getAttribute('shadow') !== 'false'
  }

  set shadow(value: boolean) {
    if (value) this.removeAttribute('shadow')
    else this.setAttribute('shadow', 'false')
  }

  get maxHeight(): string | null {
    return this.getAttribute('max-height')
  }

  set maxHeight(value: string | null) {
    if (value) this.setAttribute('max-height', value)
    else this.removeAttribute('max-height')
    this._updateMaxHeight()
  }

  get hideScrollbar(): boolean {
    return this.hasAttribute('hide-scrollbar')
  }

  set hideScrollbar(value: boolean) {
    if (value) this.setAttribute('hide-scrollbar', '')
    else this.removeAttribute('hide-scrollbar')
  }

  get customScrollbar(): boolean {
    return this.hasAttribute('custom-scrollbar')
  }

  set customScrollbar(value: boolean) {
    if (value) this.setAttribute('custom-scrollbar', '')
    else this.removeAttribute('custom-scrollbar')
  }

  get overflowX(): boolean {
    return this.hasAttribute('overflow-x')
  }

  set overflowX(value: boolean) {
    if (value) this.setAttribute('overflow-x', '')
    else this.removeAttribute('overflow-x')
  }

  constructor() {
    super()

    const shadow = this.attachShadow({ mode: 'open' })
    ensureStyles(shadow, { css: scrollContainerStyles, id: 'ty-scroll-container' })

    shadow.innerHTML = `
      <div class="scroll-wrapper">
        <slot></slot>
      </div>
      <div class="shadow-overlay">
        <div class="shadow-top"></div>
        <div class="shadow-bottom"></div>
        <div class="shadow-left"></div>
        <div class="shadow-right"></div>
      </div>
    `

    this._scrollWrapper = shadow.querySelector('.scroll-wrapper')
    this._shadowTop = shadow.querySelector('.shadow-top')
    this._shadowBottom = shadow.querySelector('.shadow-bottom')
    this._shadowLeft = shadow.querySelector('.shadow-left')
    this._shadowRight = shadow.querySelector('.shadow-right')
  }

  connectedCallback(): void {
    this._scrollWrapper?.addEventListener('scroll', this._onScroll, { passive: true })

    this._resizeObserver = new ResizeObserver(() => {
      this._scrollbar?.update()
      this._updateShadowState()
    })

    if (this._scrollWrapper) {
      this._resizeObserver.observe(this._scrollWrapper)

      const slot = this._scrollWrapper.querySelector('slot') as HTMLSlotElement | null
      if (slot) {
        slot.addEventListener('slotchange', this._onSlotChange)
      }
    }

    this._updateMaxHeight()
    this._updateShadowState()
    this._setupScrollbar()
  }

  disconnectedCallback(): void {
    this._scrollWrapper?.removeEventListener('scroll', this._onScroll)
    this._destroyScrollbar()

    if (this._resizeObserver) {
      this._resizeObserver.disconnect()
      this._resizeObserver = null
    }

    if (this._rafId !== null) {
      cancelAnimationFrame(this._rafId)
      this._rafId = null
    }
  }

  attributeChangedCallback(name: string, oldValue: string | null, newValue: string | null): void {
    if (oldValue === newValue) return

    if (name === 'max-height') {
      this._updateMaxHeight()
      this._updateShadowState()
      this._scrollbar?.update()
    }

    if (name === 'custom-scrollbar' || name === 'overflow-x') {
      this._destroyScrollbar()
      this._setupScrollbar()
      this._updateShadowState()
    }
  }

  // ============ Private: Scrollbar Setup ============

  private _setupScrollbar(): void {
    if (!this._scrollWrapper || !this.customScrollbar) return

    this._scrollbar = new CustomScrollbar(this._scrollWrapper, {
      vertical: true,
      horizontal: this.overflowX
    })

    // Append track elements to shadow DOM
    const shadowRoot = this.shadowRoot!
    if (this._scrollbar.trackY) shadowRoot.appendChild(this._scrollbar.trackY)
    if (this._scrollbar.trackX) shadowRoot.appendChild(this._scrollbar.trackX)
  }

  private _destroyScrollbar(): void {
    if (this._scrollbar) {
      // Remove track elements from DOM
      this._scrollbar.trackY?.remove()
      this._scrollbar.trackX?.remove()
      this._scrollbar.destroy()
      this._scrollbar = null
    }
  }

  // ============ Private: Max Height ============

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

  // ============ Private: Scroll Handling ============

  private _onScroll = (): void => {
    if (this._rafId !== null) return

    this._rafId = requestAnimationFrame(() => {
      this._rafId = null
      this._updateShadowState()
    })
  }

  private _onSlotChange = (): void => {
    this._scrollbar?.update()
    this._updateShadowState()
  }

  // ============ Private: Shadow State ============

  private _updateShadowState(): void {
    if (!this._scrollWrapper) return
    const { scrollTop, scrollLeft, scrollHeight, scrollWidth, clientHeight, clientWidth } = this._scrollWrapper
    const threshold = 2

    if (this._shadowTop) {
      this._shadowTop.classList.toggle('visible', scrollTop > threshold)
    }
    if (this._shadowBottom) {
      this._shadowBottom.classList.toggle('visible', scrollTop + clientHeight < scrollHeight - threshold)
    }

    if (this.overflowX) {
      if (this._shadowLeft) {
        this._shadowLeft.classList.toggle('visible', scrollLeft > threshold)
      }
      if (this._shadowRight) {
        this._shadowRight.classList.toggle('visible', scrollLeft + clientWidth < scrollWidth - threshold)
      }
    }
  }

  // ============ Public API ============

  updateShadows(): void {
    this._updateShadowState()
    this._scrollbar?.update()
  }

  scrollToTop(smooth = true): void {
    this._scrollWrapper?.scrollTo({ top: 0, behavior: smooth ? 'smooth' : 'auto' })
  }

  scrollToBottom(smooth = true): void {
    if (this._scrollWrapper) {
      this._scrollWrapper.scrollTo({ top: this._scrollWrapper.scrollHeight, behavior: smooth ? 'smooth' : 'auto' })
    }
  }

  scrollToLeft(smooth = true): void {
    this._scrollWrapper?.scrollTo({ left: 0, behavior: smooth ? 'smooth' : 'auto' })
  }

  scrollToRight(smooth = true): void {
    if (this._scrollWrapper) {
      this._scrollWrapper.scrollTo({ left: this._scrollWrapper.scrollWidth, behavior: smooth ? 'smooth' : 'auto' })
    }
  }

  get scrollElement(): HTMLElement | null {
    return this._scrollWrapper
  }
}

// Register custom element
customElements.define('ty-scroll-container', TyScrollContainer)
