/**
 * TyDropdown Web Component
 * PORTED FROM: clj/ty/components/dropdown.cljs
 * 
 * A semantic dropdown component with:
 * - Desktop mode with smart positioning
 * - Mobile mode with full-screen modal
 * - Search and filtering capabilities
 * - Keyboard navigation
 * - Form association for native form submission
 * - Rich option support (option, ty-option, ty-tag)
 * 
 * @example
 * ```html
 * <!-- Basic dropdown -->
 * <ty-dropdown label="Country" placeholder="Select country" required>
 *   <option value="us">United States</option>
 *   <option value="uk">United Kingdom</option>
 *   <option value="ca">Canada</option>
 * </ty-dropdown>
 * 
 * <!-- With rich options -->
 * <ty-dropdown label="User" searchable>
 *   <ty-option value="1">
 *     <div class="flex items-center gap-2">
 *       <img src="avatar1.jpg" class="w-8 h-8 rounded-full" />
 *       <span>John Doe</span>
 *     </div>
 *   </ty-option>
 * </ty-dropdown>
 * 
 * <!-- Not searchable (external search) -->
 * <ty-dropdown label="Search API" not-searchable>
 *   <option value="1">Result 1</option>
 * </ty-dropdown>
 * ```
 */

import type { Flavor, Size } from '../types/common.js'
import { ensureStyles } from '../utils/styles.js'
import { dropdownStyles } from '../styles/dropdown.js'
import { lockScroll, unlockScroll } from '../utils/scroll-lock.js'

// ============================================================================
// DEVICE DETECTION
// ============================================================================

/**
 * Detect if we're on a mobile device
 * - Screen width <= 768px (mobile phones)
 * - Screen width <= 1024px + touch capability (tablets)
 * 
 * This matches the ClojureScript is-mobile-device? logic
 */
function isMobileDevice(): boolean {
  const width = window.innerWidth
  const hasTouch = 'ontouchstart' in window || navigator.maxTouchPoints > 0
  
  return width <= 768 || (width <= 1024 && hasTouch)
}

// ============================================================================
// Element Hash Utility (equivalent to ClojureScript's hash function)
// ============================================================================

/**
 * Counter for generating unique element IDs
 */
let elementIdCounter = 0

/**
 * WeakMap to store consistent element hashes
 * Automatically garbage collects when element is destroyed
 */
const elementIds = new WeakMap<object, number>()

/**
 * Get a consistent unique ID for an element (similar to ClojureScript's hash function)
 * Returns the same ID for the same element across multiple calls
 * 
 * @param element - The element to hash
 * @returns A consistent numeric hash for the element
 */
function getElementHash(element: object): number {
  let id = elementIds.get(element)
  if (id === undefined) {
    id = ++elementIdCounter
    elementIds.set(element, id)
  }
  return id
}

// ============================================================================
// SVG Icons
// ============================================================================

/**
 * Required indicator SVG icon (from Lucide)
 */
const REQUIRED_ICON_SVG = `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-asterisk"><path d="M12 6v12"/><path d="M17.196 9 6.804 15"/><path d="m6.804 9 10.392 6"/></svg>`

/**
 * Chevron down icon SVG
 */
const CHEVRON_DOWN_SVG = `<svg viewBox="0 0 20 20" fill="currentColor">
  <path fill-rule="evenodd" d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" clip-rule="evenodd" />
</svg>`

/**
 * Option data structure
 */
interface OptionData {
  value: string
  text: string
  element: HTMLElement
}

/**
 * Component state structure
 */
interface DropdownState {
  open: boolean
  search: string
  highlightedIndex: number
  filteredOptions: OptionData[]
  currentValue: string | null
  mode: 'desktop' | 'mobile'
}

/**
 * Ty Dropdown Component
 */
export class TyDropdown extends HTMLElement {
  static formAssociated = true

  private _internals: ElementInternals
  private _value: string = ''
  private _name: string = ''
  private _placeholder: string = 'Select an option...'
  private _label: string = ''
  private _disabled = false
  private _readonly = false
  private _required = false
  private _searchable = true
  private _size: Size = 'md'
  private _flavor: Flavor = 'neutral'

  // Component state
  private _state: DropdownState = {
    open: false,
    search: '',
    highlightedIndex: -1,
    filteredOptions: [],
    currentValue: null,
    mode: isMobileDevice() ? 'mobile' : 'desktop'
  }

  // Scroll lock ID (consistent across open/close cycles)
  private _scrollLockId: string | null = null

  // Event handler references for cleanup
  private _stubClickHandler: ((e: Event) => void) | null = null
  private _outsideClickHandler: ((e: Event) => void) | null = null
  private _optionClickHandler: ((e: Event) => void) | null = null
  private _searchInputHandler: ((e: Event) => void) | null = null
  private _searchBlurHandler: ((e: Event) => void) | null = null
  private _keyboardHandler: ((e: KeyboardEvent) => void) | null = null

  // Delay/debounce properties for ty-search event
  private _delay: number = 0
  private _searchDebounceTimer: number | null = null

  constructor() {
    super()
    this._internals = this.attachInternals()

    const shadow = this.attachShadow({ mode: 'open' })
    ensureStyles(shadow, { css: dropdownStyles, id: 'ty-dropdown' })

    // Render based on device type
    if (this._state.mode === 'mobile') {
      this.renderMobile()
    } else {
      this.renderDesktop()
    }
  }

  static get observedAttributes(): string[] {
    return [
      'value', 'name', 'placeholder', 'label',
      'disabled', 'readonly', 'required',
      'searchable', 'not-searchable',
      'size', 'flavor', 'delay'
    ]
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
    
    this.initializeState()
  }

  disconnectedCallback(): void {
    // Clean up document-level listeners using stored handlers (like ClojureScript)
    const outsideClickHandler = (this as any).tyOutsideClickHandler
    const keyboardHandler = (this as any).tyKeyboardHandler
    
    if (outsideClickHandler) {
      document.removeEventListener('click', outsideClickHandler)
      ;(this as any).tyOutsideClickHandler = null
    }
    
    if (keyboardHandler) {
      document.removeEventListener('keydown', keyboardHandler)
      ;(this as any).tyKeyboardHandler = null
    }

    // Clear any pending debounce timer
    if (this._searchDebounceTimer !== null) {
      clearTimeout(this._searchDebounceTimer)
      this._searchDebounceTimer = null
    }
  }

  attributeChangedCallback(name: string, oldValue: string | null, newValue: string | null): void {
    if (oldValue === newValue) return

    switch (name) {
      case 'value':
        this._value = newValue || ''
        this._state.currentValue = this.parseValue(newValue)
        this.syncSelectedOption()
        this.updateFormValue()
        break
      case 'name':
        this._name = newValue || ''
        break
      case 'placeholder':
        this._placeholder = newValue || 'Select an option...'
        break
      case 'label':
        this._label = newValue || ''
        break
      case 'disabled':
        this._disabled = newValue !== null
        break
      case 'readonly':
        this._readonly = newValue !== null
        break
      case 'required':
        this._required = newValue !== null
        break
      case 'searchable':
        this._searchable = this.parseSearchable(newValue)
        break
      case 'not-searchable':
        if (newValue !== null) {
          this._searchable = false
        }
        break
      case 'size':
        this._size = (newValue as Size) || 'md'
        break
      case 'flavor':
        this._flavor = this.validateFlavor(newValue)
        break
      case 'delay':
        this._delay = this.parseDelay(newValue)
        break
    }

    this.renderDesktop()
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
   * Parse searchable attribute logic
   * - If 'searchable' is explicitly set to false, return false
   * - If 'not-searchable' attribute exists, return false
   * - Otherwise default to true
   */
  private parseSearchable(value: string | null): boolean {
    if (value === 'false') return false
    if (this.hasAttribute('not-searchable')) return false
    return true
  }

  /**
   * Parse dropdown value (single string)
   */
  private parseValue(value: string | null): string | null {
    // Defensive check: ensure value is actually a string before calling .trim()
    if (!value || typeof value !== 'string' || value.trim() === '') return null
    return value.trim()
  }

  /**
   * Validate flavor attribute
   */
  private validateFlavor(flavor: string | null): Flavor {
    const validFlavors: Flavor[] = ['primary', 'secondary', 'success', 'danger', 'warning', 'neutral']
    const normalized = (flavor || 'neutral') as Flavor

    if (!validFlavors.includes(normalized)) {
      console.warn(
        `[ty-dropdown] Invalid flavor '${flavor}'. Using 'neutral'. ` +
        `Valid flavors: ${validFlavors.join(', ')}`
      )
      return 'neutral'
    }

    return normalized
  }

  /**
   * Initialize component state from attributes
   */
  private initializeState(): void {
    if (this._value) {
      this._state.currentValue = this.parseValue(this._value)
      this.syncSelectedOption()
      this.updateFormValue()
    }
  }

  /**
   * Update form value via ElementInternals
   */
  private updateFormValue(): void {
    this._internals.setFormValue(
      this._state.currentValue || null
    )
  }

  /**
   * Get all option elements from slot
   * Supports: <option>, <ty-option>, <ty-tag>
   */
  private getOptions(): HTMLElement[] {
    const shadow = this.shadowRoot!
    const slot = shadow.querySelector('slot:not([name])') as HTMLSlotElement
    if (!slot) return []

    const assigned = (slot.assignedElements ? slot.assignedElements() : []) as HTMLElement[]
    return assigned.filter(el => {
      const tag = el.tagName
      return tag === 'OPTION' || tag === 'TY-OPTION' || tag === 'TY-TAG'
    })
  }

  /**
   * Extract value and text from option element
   */
  private getOptionData(element: HTMLElement): OptionData {
    const tag = element.tagName

    if (tag === 'OPTION') {
      const optionEl = element as HTMLOptionElement
      return {
        value: optionEl.value || optionEl.textContent || '',
        text: optionEl.textContent || '',
        element
      }
    }

    if (tag === 'TY-OPTION') {
      return {
        value: element.getAttribute('value') || element.textContent || '',
        text: element.textContent || '',
        element
      }
    }

    if (tag === 'TY-TAG') {
      return {
        value: element.getAttribute('value') || element.textContent || '',
        text: element.textContent || '',
        element
      }
    }

    return {
      value: element.textContent || '',
      text: element.textContent || '',
      element
    }
  }

  /**
   * Clear all selections
   */
  private clearSelection(): void {
    const shadow = this.shadowRoot!
    const options = this.getOptions()

    // Clear selected attribute from all options
    options.forEach(opt => opt.removeAttribute('selected'))

    // Remove clones from selected slot
    const selectedSlot = shadow.querySelector('slot[name="selected"]') as HTMLSlotElement | null
    if (selectedSlot) {
      const assigned = selectedSlot.assignedElements ? selectedSlot.assignedElements() : []
      const clones = Array.from(assigned as Element[]).filter((el: Element) => el.hasAttribute('cloned'))
      clones.forEach((clone: Element) => clone.remove())
    }
  }

  /**
   * Select an option
   */
  private selectOption(option: HTMLElement): void {
    this.clearSelection()

    // Clone option for display in stub
    const clone = option.cloneNode(true) as HTMLElement
    clone.setAttribute('slot', 'selected')
    clone.setAttribute('cloned', 'true')
    option.parentNode!.appendChild(clone)

    // Mark original as selected
    option.setAttribute('selected', '')

    // Update state and form
    const optionData = this.getOptionData(option)
    this._state.currentValue = optionData.value
    this.updateComponentValue()
    this.updateSelectionDisplay()
    this.updateFormValue()

    // Dispatch change event
    this.dispatchChangeEvent(option)
  }

  /**
   * Sync selected option based on current value
   */
  private syncSelectedOption(): void {
    const options = this.getOptions()
    const currentValue = this._state.currentValue

    if (!currentValue) {
      this.clearSelection()
      return
    }

    // Find matching option
    const matchingOption = options.find(opt => {
      const data = this.getOptionData(opt)
      return data.value === currentValue
    })

    if (matchingOption) {
      this.selectOption(matchingOption)
    }
  }

  /**
   * Update component value attribute for consistency
   */
  private updateComponentValue(): void {
    const currentValue = this._state.currentValue
    if (currentValue !== null) {
      this.setAttribute('value', currentValue)
    } else {
      this.removeAttribute('value')
    }
    // Also set property
    this._value = currentValue || ''
  }

  /**
   * Update selection display (show/hide placeholder)
   */
  private updateSelectionDisplay(): void {
    const shadow = this.shadowRoot!
    const stub = shadow.querySelector('.dropdown-stub')
    if (!stub) return

    const options = this.getOptions()
    const hasSelected = options.some(opt => opt.hasAttribute('selected'))

    if (hasSelected) {
      stub.classList.add('has-selection')
    } else {
      stub.classList.remove('has-selection')
    }
  }

  /**
   * Dispatch change event
   */
  private dispatchChangeEvent(option: HTMLElement): void {
    this.dispatchEvent(new CustomEvent('change', {
      detail: {
        value: this._state.currentValue,
        option
      },
      bubbles: true,
      composed: true
    }))
  }

  // ============================================================================
  // Phase 3: Option Visibility & Filtering Helpers
  // (Used in Phase 4: Search & Filtering and Phase 5: Keyboard Navigation)
  // ============================================================================

  /**
   * Filter options based on search query
   */
  private filterOptions(options: OptionData[], query: string): OptionData[] {
    if (!query || query.trim() === '') {
      return options
    }

    const searchLower = query.toLowerCase()
    return options.filter(({ text }) => 
      text.toLowerCase().includes(searchLower)
    )
  }

  /**
   * Update visibility of options based on filtered list
   * @internal Used in Phase 4
   */
  // @ts-ignore - Used in Phase 4
  private updateOptionVisibility(filteredOptions: OptionData[], allOptions: OptionData[]): void {
    const visibleValues = new Set(filteredOptions.map(opt => opt.value))
    
    allOptions.forEach(({ value, element }) => {
      if (visibleValues.has(value)) {
        element.removeAttribute('hidden')
      } else {
        element.setAttribute('hidden', '')
      }
    })
  }

  /**
   * Clear all option highlights
   */
  private clearHighlights(options: OptionData[]): void {
    options.forEach(({ element }) => {
      element.removeAttribute('highlighted')
    })
  }

  /**
   * Highlight option at specific index
   * @internal Used in Phase 5
   */
  // @ts-ignore - Used in Phase 5
  private highlightOption(options: OptionData[], index: number): void {
    this.clearHighlights(options)
    
    if (index >= 0 && index < options.length) {
      const { element } = options[index]
      element.setAttribute('highlighted', '')
      
      // Scroll into view
      element.scrollIntoView({
        behavior: 'smooth',
        block: 'nearest',
        inline: 'nearest'
      })
    }
  }

  /**
   * Setup event listeners
   */
  private setupEventListeners(): void {
    const shadow = this.shadowRoot!
    const stub = shadow.querySelector('.dropdown-stub')
    const optionsSlot = shadow.querySelector('#options-slot')
    const searchInput = shadow.querySelector('.dropdown-search-input')

    if (stub) {
      this._stubClickHandler = this.handleStubClick.bind(this)
      stub.addEventListener('click', this._stubClickHandler)
    }

    // Add option click handler to slot
    if (optionsSlot) {
      this._optionClickHandler = this.handleOptionClick.bind(this)
      optionsSlot.addEventListener('click', this._optionClickHandler)
    }

    // Add search input handlers
    if (searchInput) {
      this._searchInputHandler = this.handleSearchInput.bind(this)
      this._searchBlurHandler = this.handleSearchBlur.bind(this)
      
      searchInput.addEventListener('input', this._searchInputHandler)
      searchInput.addEventListener('blur', this._searchBlurHandler)
    }

    // Setup document-level event listeners immediately (like ClojureScript version)
    this._outsideClickHandler = this.handleOutsideClick.bind(this)
    this._keyboardHandler = this.handleKeyboard.bind(this)
    
    document.addEventListener('click', this._outsideClickHandler)
    document.addEventListener('keydown', this._keyboardHandler)
  }

  // removeEventListeners removed - now using ClojureScript pattern

  // ============================================================================
  // DESKTOP IMPLEMENTATION
  // ============================================================================
  // Phase 2: Dialog & Positioning
  // ============================================================================

  /**
   * Calculate and set dropdown position with smart direction detection
   */
  private calculatePosition(): void {
    const shadow = this.shadowRoot!
    const stub = shadow.querySelector('.dropdown-stub') as HTMLElement
    const dialog = shadow.querySelector('.dropdown-dialog') as HTMLDialogElement
    
    if (!stub || !dialog) return

    const stubRect = stub.getBoundingClientRect()
    const viewportHeight = window.innerHeight
    const viewportWidth = window.innerWidth

    // Get dialog dimensions (it's already shown with showModal)
    const dialogRect = dialog.getBoundingClientRect()
    const estimatedHeight = dialogRect.height || 200

    const padding = 8
    const wrapPadding = 20

    // Available space calculations
    const spaceBelow = viewportHeight - stubRect.bottom
    const spaceRight = viewportWidth - stubRect.left

    // Smart direction logic
    const positionBelow = spaceBelow >= estimatedHeight + padding
    const fitsHorizontally = spaceRight >= stubRect.width

    // Calculate position coordinates
    const x = fitsHorizontally
      ? stubRect.left - wrapPadding
      : Math.max(padding, viewportWidth - stubRect.width - padding)

    const y = positionBelow
      ? stubRect.top - wrapPadding
      : viewportHeight - stubRect.bottom - wrapPadding

    const width = stubRect.width + wrapPadding + wrapPadding

    // Set CSS variables for positioning
    this.style.setProperty('--dropdown-x', `${x}px`)
    this.style.setProperty('--dropdown-y', `${y}px`)
    this.style.setProperty('--dropdown-width', `${width}px`)
    this.style.setProperty('--dropdown-offset-x', '0px')
    this.style.setProperty('--dropdown-offset-y', '0px')
    this.style.setProperty('--dropdown-padding', `${wrapPadding}px`)

    // Set direction classes for CSS styling
    if (positionBelow) {
      dialog.classList.add('position-below')
      dialog.classList.remove('position-above')
    } else {
      dialog.classList.add('position-above')
      dialog.classList.remove('position-below')
    }

    // Optional: Store direction for debugging
    this.style.setProperty('--dropdown-direction', positionBelow ? 'below' : 'above')
  }

  /**
   * Open dropdown dialog
   */
  private openDropdown(): void {
    console.log('[Dropdown] openDropdown called')
    const shadow = this.shadowRoot!
    const dialog = shadow.querySelector('.dropdown-dialog') as HTMLDialogElement
    if (!dialog) {
      console.warn('[Dropdown] No dialog found to open')
      return
    }

    // Generate consistent unique ID for scroll locking (like ClojureScript's hash)
    const dropdownId = `dropdown-${this.id || 'anon'}-${getElementHash(this)}`
    this._scrollLockId = dropdownId
    
    // Lock scroll
    console.log('[Dropdown] Locking scroll with ID:', dropdownId)
    lockScroll(dropdownId)
    console.log('[Dropdown] Scroll locked')

    // Show modal first so browser can calculate dimensions
    dialog.showModal()
    dialog.classList.add('open')

    // Position dropdown AFTER showing modal
    this.calculatePosition()

    // Update component state
    this._state.open = true

    // Update visual states
    const chevron = shadow.querySelector('.dropdown-chevron')
    if (chevron) chevron.classList.add('open')

    const searchChevron = shadow.querySelector('.dropdown-search-chevron')
    if (searchChevron) searchChevron.classList.add('open')

    // Initialize options state and find selected option
    const options = this.getOptions().map(el => this.getOptionData(el))
    const currentValue = this._state.currentValue
    
    // Find the index of the currently selected option
    const selectedIndex = currentValue 
      ? options.findIndex(opt => opt.value === currentValue)
      : -1

    this._state.filteredOptions = options
    this._state.highlightedIndex = selectedIndex

    // Highlight and scroll to selected option if it exists
    if (selectedIndex >= 0) {
      this.highlightOption(options, selectedIndex)
    }

    // Focus search input if searchable
    if (this._searchable) {
      const searchInput = shadow.querySelector('.dropdown-search-input') as HTMLInputElement
      if (searchInput) {
        setTimeout(() => searchInput.focus(), 100)
      }
    }
  }

  /**
   * Close dropdown dialog
   */
  private closeDropdown(): void {
    console.log('[Dropdown] closeDropdown called')
    const shadow = this.shadowRoot!
    const dialog = shadow.querySelector('.dropdown-dialog') as HTMLDialogElement
    if (!dialog) {
      console.warn('[Dropdown] No dialog found to close')
      return
    }

    // Unlock scroll using stored consistent ID
    if (this._scrollLockId) {
      console.log('[Dropdown] Unlocking scroll with ID:', this._scrollLockId)
      unlockScroll(this._scrollLockId)
      this._scrollLockId = null
      console.log('[Dropdown] Scroll unlocked')
    }

    // Close dialog
    dialog.classList.remove('open')
    dialog.classList.remove('position-above')
    dialog.classList.remove('position-below')
    dialog.close()

    // Update state
    this._state.open = false
    this._state.highlightedIndex = -1

    // Update visual states
    const chevron = shadow.querySelector('.dropdown-chevron')
    if (chevron) chevron.classList.remove('open')

    const searchChevron = shadow.querySelector('.dropdown-search-chevron')
    if (searchChevron) searchChevron.classList.remove('open')

    // Reset search if not searchable
    if (!this._searchable) {
      this._state.search = ''
    }
  }

  /**
   * Handle outside click to close dropdown
   */
  private handleOutsideClick(e: Event): void {
    if (!this._state.open) return

    const target = e.target as Node
    const shadow = this.shadowRoot!
    
    // CRITICAL: Check if click is inside .dropdown-wrapper (like ClojureScript version)
    // Not the whole component - this prevents the component itself from being counted
    const wrapper = shadow.querySelector('.dropdown-wrapper')
    const clickedInside = wrapper && wrapper.contains(target)

    console.log('[Dropdown] Outside click check:', {
      target: target,
      wrapper: wrapper,
      clickedInside,
      targetNodeName: (target as Element).nodeName
    })

    if (!clickedInside) {
      console.log('[Dropdown] Closing dropdown - outside click detected')
      this.closeDropdown()
    }
  }

  // ============================================================================
  // Phase 4: Search & Filtering
  // ============================================================================

  /**
   * Handle search input changes
   */
  private handleSearchInput(e: Event): void {
    const target = e.target as HTMLInputElement
    const query = target.value
    
    // Update search state
    this._state.search = query

    if (this._searchable) {
      // Internal search: filter options locally
      const allOptions = this.getOptions().map(el => this.getOptionData(el))
      const filtered = this.filterOptions(allOptions, query)
      
      // Update state
      this._state.filteredOptions = filtered
      this._state.highlightedIndex = -1
      
      // Update visibility
      this.updateOptionVisibility(filtered, allOptions)
      
      // Clear highlights
      this.clearHighlights(allOptions)
    } else {
      // External search: dispatch event for external handling
      this.dispatchSearchEvent(query)
    }
  }

  /**
   * Handle search blur - reset search if searchable
   */
  private handleSearchBlur(_e: Event): void {
    if (!this._searchable) return

    // Reset search
    this._state.search = ''
    
    const shadow = this.shadowRoot!
    const searchInput = shadow.querySelector('.dropdown-search-input') as HTMLInputElement
    if (searchInput) {
      searchInput.value = ''
    }

    // Show all options
    const allOptions = this.getOptions().map(el => this.getOptionData(el))
    this._state.filteredOptions = allOptions
    this.updateOptionVisibility(allOptions, allOptions)
    this.clearHighlights(allOptions)
  }

  /**
   * Dispatch search event for external search handling
   * With optional delay/debounce support
   */
  private dispatchSearchEvent(query: string): void {
    // Clear existing timer
    if (this._searchDebounceTimer !== null) {
      clearTimeout(this._searchDebounceTimer)
      this._searchDebounceTimer = null
    }

    // If delay is set, debounce the event
    if (this._delay > 0) {
      this._searchDebounceTimer = window.setTimeout(() => {
        this.fireSearchEvent(query)
        this._searchDebounceTimer = null
      }, this._delay)
    } else {
      // Fire immediately if no delay
      this.fireSearchEvent(query)
    }
  }

  /**
   * Fire the actual ty-search event
   */
  private fireSearchEvent(query: string): void {
    this.dispatchEvent(new CustomEvent('ty-search', {
      detail: {
        query,
        element: this
      },
      bubbles: true,
      composed: true
    }))
  }

  // ============================================================================
  // Phase 5: Keyboard Navigation
  // ============================================================================

  /**
   * Handle keyboard navigation
   */
  private handleKeyboard(e: KeyboardEvent): void {
    if (!this._state.open) return

    const shadow = this.shadowRoot!
    const searchInput = shadow.querySelector('.dropdown-search-input') as HTMLInputElement
    const target = e.target as HTMLElement

    // Only handle navigation keys when dropdown is open and either:
    // 1. Event comes from search input, OR
    // 2. Event comes from document but search input is not focused
    const shouldHandle = target === searchInput || 
                        document.activeElement !== searchInput

    if (!shouldHandle) return

    // Get current state values
    const filteredOptions = this._state.filteredOptions
    const optionsCount = filteredOptions.length
    const currentHighlightedIndex = this._state.highlightedIndex

    console.log('[Keyboard] Key pressed:', e.key, 'Current index:', currentHighlightedIndex, 'Options count:', optionsCount)

    switch (e.key) {
      case 'Escape':
        e.preventDefault()
        e.stopPropagation()
        this.closeDropdown()
        break

      case 'Enter':
        e.preventDefault()
        e.stopPropagation()
        // Select highlighted option if any
        if (currentHighlightedIndex >= 0 && currentHighlightedIndex < optionsCount) {
          const option = filteredOptions[currentHighlightedIndex]
          this.selectOption(option.element)
          this.updateSelectionDisplay()
          this.closeDropdown()
        }
        break

      case 'ArrowUp':
        e.preventDefault()
        e.stopPropagation()
        let newIndexUp: number
        
        if (optionsCount === 0) {
          newIndexUp = -1
        } else if (currentHighlightedIndex === -1) {
          // Nothing highlighted, go to last option
          newIndexUp = optionsCount - 1
        } else if (currentHighlightedIndex === 0) {
          // At first option, wrap to last
          newIndexUp = optionsCount - 1
        } else {
          // Move up one
          newIndexUp = currentHighlightedIndex - 1
        }
        
        console.log('[Keyboard] ArrowUp: Moving from', currentHighlightedIndex, 'to', newIndexUp)
        this._state.highlightedIndex = newIndexUp
        this.highlightOption(filteredOptions, newIndexUp)
        break

      case 'ArrowDown':
        e.preventDefault()
        e.stopPropagation()
        let newIndexDown: number
        
        if (optionsCount === 0) {
          newIndexDown = -1
        } else if (currentHighlightedIndex === -1) {
          // Nothing highlighted, go to first option
          newIndexDown = 0
        } else if (currentHighlightedIndex === optionsCount - 1) {
          // At last option, wrap to first
          newIndexDown = 0
        } else {
          // Move down one
          newIndexDown = currentHighlightedIndex + 1
        }
        
        console.log('[Keyboard] ArrowDown: Moving from', currentHighlightedIndex, 'to', newIndexDown)
        this._state.highlightedIndex = newIndexDown
        this.highlightOption(filteredOptions, newIndexDown)
        break
    }
  }

  /**
   * Handle stub click - open dropdown
   */
  private handleStubClick(e: Event): void {
    e.preventDefault()
    e.stopPropagation()

    if (this._disabled || this._readonly) {
      return
    }

    this.openDropdown()
  }

  /**
   * Handle option click - select option and close
   */
  private handleOptionClick(e: Event): void {
    e.preventDefault()
    e.stopPropagation()

    const target = e.target as HTMLElement
    
    // Find the option element (might be clicking on child element)
    const option = target.closest('option, ty-option, ty-tag') as HTMLElement
    if (!option) return

    // Select the option
    this.selectOption(option)
    
    // Update display
    this.updateSelectionDisplay()

    // Close dropdown
    this.closeDropdown()
  }

  /**
   * Build CSS class list for stub
   */
  private buildStubClasses(): string {
    const classes: string[] = [this._size]
    if (this._disabled) classes.push('disabled')
    return classes.join(' ')
  }

  /**
   * Render desktop mode with dialog
   */
  private renderDesktop(): void {
    const shadow = this.shadowRoot!
    
    // Only set innerHTML and setup listeners if container doesn't exist (like ClojureScript)
    if (!shadow.querySelector('.dropdown-container')) {
      const stubClasses = this.buildStubClasses()

      const labelHtml = this._label ? `
        <label class="dropdown-label">
          ${this._label}
          ${this._required ? `<span class="required-icon">${REQUIRED_ICON_SVG}</span>` : ''}
        </label>
      ` : ''

      const searchPlaceholder = this._searchable ? 'Search...' : this._placeholder

      shadow.innerHTML = `
        <div class="dropdown-container">
          ${labelHtml}
          <div class="dropdown-wrapper">
            <div class="dropdown-stub ${stubClasses}" 
                 ${this._disabled ? 'disabled' : ''}>
              <slot name="selected"></slot>
              <span class="dropdown-placeholder">${this._placeholder}</span>
              <div class="dropdown-chevron">
                ${CHEVRON_DOWN_SVG}
              </div>
            </div>
            <dialog class="dropdown-dialog">
              <div class="dropdown-header">
                <input 
                  class="dropdown-search-input ${this._size}" 
                  type="text"
                  placeholder="${searchPlaceholder}"
                  ${this._disabled ? 'disabled' : ''}
                />
                <div class="dropdown-search-chevron">
                  ${CHEVRON_DOWN_SVG}
                </div>
              </div>
              <div class="dropdown-options">
                <slot id="options-slot"></slot>
              </div>
            </dialog>
          </div>
        </div>
      `

      // Setup event listeners ONCE (only when rendering for the first time)
      this.setupEventListeners()
    }

    // Always update selection display
    this.updateSelectionDisplay()
  }

  // ============================================================================
  // MOBILE IMPLEMENTATION
  // ============================================================================

  /**
   * Render mobile mode with full-screen modal
   */
  private renderMobile(): void {
    const shadow = this.shadowRoot!
    
    // Only set innerHTML and setup listeners if container doesn't exist
    if (!shadow.querySelector('.dropdown-container')) {
      const stubClasses = this.buildStubClasses()

      const labelHtml = this._label ? `
        <label class="dropdown-label">
          ${this._label}
          ${this._required ? `<span class="required-icon">${REQUIRED_ICON_SVG}</span>` : ''}
        </label>
      ` : ''

      // Close button SVG (X icon)
      const closeButtonSvg = `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <line x1="18" y1="6" x2="6" y2="18"></line>
        <line x1="6" y1="6" x2="18" y2="18"></line>
      </svg>`

      // Conditional search header - only show when searchable
      const searchHeaderHtml = this._searchable ? `
        <div class="mobile-search-header">
          <div class="mobile-header-content">
            <input 
              class="mobile-search-input ${this._size}" 
              type="text"
              placeholder="Search..."
              ${this._disabled ? 'disabled' : ''}
            />
            <button class="mobile-close-button" type="button" aria-label="Close">
              ${closeButtonSvg}
            </button>
          </div>
        </div>
      ` : `
        <div class="mobile-header-nosearch">
          <button class="mobile-close-button" type="button" aria-label="Close">
            ${closeButtonSvg}
          </button>
        </div>
      `

      shadow.innerHTML = `
        <div class="dropdown-container">
          ${labelHtml}
          <div class="dropdown-wrapper">
            <div class="dropdown-stub ${stubClasses}" 
                 ${this._disabled ? 'disabled' : ''}>
              <slot name="selected"></slot>
              <span class="dropdown-placeholder">${this._placeholder}</span>
              <div class="dropdown-chevron">
                ${CHEVRON_DOWN_SVG}
              </div>
            </div>
            <div class="mobile-modal" style="display: none;">
              <div class="mobile-modal-backdrop"></div>
              <div class="mobile-modal-content">
                ${searchHeaderHtml}
                <div class="mobile-options-container">
                  <slot id="options-slot"></slot>
                </div>
              </div>
            </div>
          </div>
        </div>
      `

      // Setup event listeners ONCE (only when rendering for the first time)
      this.setupMobileEventListeners()
    }

    // Always update selection display
    this.updateSelectionDisplay()
  }

  /**
   * Setup event listeners for mobile mode
   */
  private setupMobileEventListeners(): void {
    const shadow = this.shadowRoot!
    const stub = shadow.querySelector('.dropdown-stub')
    const optionsSlot = shadow.querySelector('#options-slot')
    const searchInput = shadow.querySelector('.mobile-search-input')
    const backdrop = shadow.querySelector('.mobile-modal-backdrop')
    const closeButton = shadow.querySelector('.mobile-close-button')

    if (stub) {
      stub.addEventListener('click', (e) => this.handleMobileStubClick(e))
    }

    // Add option click handler to slot
    if (optionsSlot) {
      optionsSlot.addEventListener('click', (e) => this.handleMobileOptionClick(e))
    }

    // Add search input handlers (if searchable)
    if (searchInput) {
      searchInput.addEventListener('input', (e) => this.handleSearchInput(e))
    }

    // Close button click
    if (closeButton) {
      closeButton.addEventListener('click', () => this.closeMobileModal())
    }

    // Backdrop click to close
    if (backdrop) {
      backdrop.addEventListener('click', () => this.closeMobileModal())
    }

    // Document keyboard handler for Escape key
    const keyboardHandler = (e: KeyboardEvent) => this.handleMobileKeyboard(e)
    document.addEventListener('keydown', keyboardHandler)
    ;(this as any).tyKeyboardHandler = keyboardHandler
  }

  /**
   * Handle mobile stub click - open modal
   */
  private handleMobileStubClick(e: Event): void {
    e.preventDefault()
    e.stopPropagation()

    if (this._disabled || this._readonly) {
      return
    }

    this.openMobileModal()
  }

  /**
   * Handle mobile option click - select and close
   */
  private handleMobileOptionClick(e: Event): void {
    e.preventDefault()
    e.stopPropagation()

    const target = e.target as HTMLElement
    
    // Find the option element (might be clicking on child element)
    const option = target.closest('option, ty-option, ty-tag') as HTMLElement
    if (!option) return

    // Select the option
    this.selectOption(option)
    
    // Update display
    this.updateSelectionDisplay()

    // Close modal
    this.closeMobileModal()
  }

  /**
   * Handle mobile keyboard navigation
   */
  private handleMobileKeyboard(e: KeyboardEvent): void {
    if (!this._state.open) return

    // Only handle Escape key on mobile (no arrow navigation needed for touch)
    if (e.key === 'Escape') {
      e.preventDefault()
      e.stopPropagation()
      this.closeMobileModal()
    }
  }

  /**
   * Open mobile modal
   */
  private openMobileModal(): void {
    const shadow = this.shadowRoot!
    const modal = shadow.querySelector('.mobile-modal') as HTMLElement
    if (!modal) return

    // Generate consistent unique ID for scroll locking
    const dropdownId = `dropdown-${this.id || 'anon'}-${getElementHash(this)}`
    this._scrollLockId = dropdownId
    
    // Lock scroll
    lockScroll(dropdownId)

    // Show modal with animation
    modal.style.display = 'flex'
    requestAnimationFrame(() => {
      modal.classList.add('open')
    })

    // Update component state
    this._state.open = true

    // Initialize options state
    const options = this.getOptions().map(el => this.getOptionData(el))
    this._state.filteredOptions = options

    // Focus search input if searchable
    if (this._searchable) {
      const searchInput = shadow.querySelector('.mobile-search-input') as HTMLInputElement
      if (searchInput) {
        // Small delay to ensure modal is visible and keyboard doesn't glitch
        setTimeout(() => searchInput.focus(), 300)
      }
    }
  }

  /**
   * Close mobile modal
   */
  private closeMobileModal(): void {
    const shadow = this.shadowRoot!
    const modal = shadow.querySelector('.mobile-modal') as HTMLElement
    if (!modal) return

    // Unlock scroll using stored consistent ID
    if (this._scrollLockId) {
      unlockScroll(this._scrollLockId)
      this._scrollLockId = null
    }

    // Hide modal with animation
    modal.classList.remove('open')
    setTimeout(() => {
      modal.style.display = 'none'
    }, 300) // Match CSS transition duration

    // Update state
    this._state.open = false
    this._state.highlightedIndex = -1

    // Reset search if searchable
    if (this._searchable) {
      this._state.search = ''
      const searchInput = shadow.querySelector('.mobile-search-input') as HTMLInputElement
      if (searchInput) {
        searchInput.value = ''
      }
    }
  }

  // ============================================================================
  // PUBLIC API - Getters/Setters
  // ============================================================================

  get value(): string {
    return this._state.currentValue || ''
  }

  set value(val: string) {
    if (this._value !== val) {
      this._value = val
      this._state.currentValue = this.parseValue(val)
      this.setAttribute('value', val)
      this.syncSelectedOption()
      this.updateFormValue()
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
      this.renderDesktop()
    }
  }

  get label(): string {
    return this._label
  }

  set label(val: string) {
    if (this._label !== val) {
      this._label = val
      this.setAttribute('label', val)
      this.renderDesktop()
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
      this.renderDesktop()
    }
  }

  get readonly(): boolean {
    return this._readonly
  }

  set readonly(value: boolean) {
    if (this._readonly !== value) {
      this._readonly = value
      if (value) {
        this.setAttribute('readonly', '')
      } else {
        this.removeAttribute('readonly')
      }
      this.renderDesktop()
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
      this.renderDesktop()
    }
  }

  get searchable(): boolean {
    return this._searchable
  }

  set searchable(value: boolean) {
    if (this._searchable !== value) {
      this._searchable = value
      if (value) {
        this.setAttribute('searchable', '')
        this.removeAttribute('not-searchable')
      } else {
        this.removeAttribute('searchable')
        this.setAttribute('not-searchable', '')
      }
      this.renderDesktop()
    }
  }

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

  get size(): Size {
    return this._size
  }

  set size(value: Size) {
    if (this._size !== value) {
      this._size = value
      this.setAttribute('size', value)
      this.renderDesktop()
    }
  }

  get flavor(): Flavor {
    return this._flavor
  }

  set flavor(value: Flavor) {
    if (this._flavor !== value) {
      this._flavor = this.validateFlavor(value)
      this.setAttribute('flavor', value)
      this.renderDesktop()
    }
  }

  get form(): HTMLFormElement | null {
    return this._internals.form
  }
}

// Register the custom element
if (!customElements.get('ty-dropdown')) {
  customElements.define('ty-dropdown', TyDropdown)
}
