/**
 * TyMultiselect Web Component
 * PORTED FROM: clj/ty/components/multiselect.cljs
 * 
 * A multiselect dropdown component using ty-tag for selections with:
 * - Tag-only options (only ty-tag elements supported)
 * - Multiple selection with visual tags
 * - Desktop mode with smart positioning
 * - Mobile mode with full-screen modal
 * - Search and filtering capabilities
 * - Keyboard navigation
 * - Form association for native form submission with multiple values
 * - Scroll locking when dropdown is open
 * - Outside click to close
 * 
 * @example
 * ```html
 * <!-- Basic multiselect -->
 * <ty-multiselect label="Skills" placeholder="Select skills" required>
 *   <ty-tag value="js">JavaScript</ty-tag>
 *   <ty-tag value="ts">TypeScript</ty-tag>
 *   <ty-tag value="py">Python</ty-tag>
 * </ty-multiselect>
 * 
 * <!-- With pre-selected values -->
 * <ty-multiselect value="js,ts">
 *   <ty-tag value="js">JavaScript</ty-tag>
 *   <ty-tag value="ts">TypeScript</ty-tag>
 *   <ty-tag value="py">Python</ty-tag>
 * </ty-multiselect>
 * 
 * <!-- With rich tag content -->
 * <ty-multiselect label="Team Members">
 *   <ty-tag value="1" flavor="primary">
 *     <div class="flex items-center gap-2">
 *       <img src="avatar1.jpg" class="w-6 h-6 rounded-full" />
 *       <span>John Doe</span>
 *     </div>
 *   </ty-tag>
 * </ty-multiselect>
 * ```
 */

import type { Flavor, Size } from '../types/common.js'
import { ensureStyles } from '../utils/styles.js'
import { multiselectStyles } from '../styles/multiselect.js'
import { lockScroll, unlockScroll } from '../utils/scroll-lock.js'
import { TyComponent } from '../base/ty-component.js'
import type { PropertyChange } from '../utils/property-manager.js'

// ============================================================================
// DEVICE DETECTION
// ============================================================================

/**
 * Detect if we're on a mobile device
 * - Screen width <= 768px (mobile phones)
 * - Screen width <= 1024px + touch capability (tablets)
 */
function isMobileDevice(): boolean {
  const width = window.innerWidth
  const hasTouch = 'ontouchstart' in window || navigator.maxTouchPoints > 0

  return width <= 768 || (width <= 1024 && hasTouch)
}

// ============================================================================
// Element Hash Utility (for consistent scroll lock IDs)
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
 * Get a consistent unique ID for an element
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
 * Tag data structure
 */
interface TagData {
  value: string
  text: string
  element: HTMLElement
}

/**
 * Component state structure
 */
interface MultiselectState {
  open: boolean
  search: string
  highlightedIndex: number
  filteredTags: TagData[]
  selectedValues: string[]
  mode: 'desktop' | 'mobile'
}

/**
 * Change event action types
 */
type ChangeAction = 'add' | 'remove' | 'clear' | 'set'

/**
 * Change event detail
 */
interface ChangeEventDetail {
  values: string[]
  action: ChangeAction
  item: string | null
}

/**
 * Ty Multiselect Component
 */
export class TyMultiselect extends TyComponent<MultiselectState> {
  // ============================================================================
  // PROPERTY CONFIGURATION - Declarative property lifecycle
  // ============================================================================
  protected static properties = {
    value: {
      type: 'string' as const,
      visual: true,
      formValue: true,
      emitChange: false,
      default: ''
    },
    name: {
      type: 'string' as const,
      default: ''
    },
    placeholder: {
      type: 'string' as const,
      visual: true,
      default: 'Select options...'
    },
    label: {
      type: 'string' as const,
      visual: true,
      default: ''
    },
    disabled: {
      type: 'boolean' as const,
      visual: true,
      default: false
    },
    readonly: {
      type: 'boolean' as const,
      visual: true,
      default: false
    },
    required: {
      type: 'boolean' as const,
      visual: true,
      default: false
    },
    searchable: {
      type: 'boolean' as const,
      visual: true,
      default: true,
      aliases: { 'not-searchable': false }
    },
    size: {
      type: 'string' as const,
      visual: true,
      default: 'md',
      validate: (v: any) => ['sm', 'md', 'lg'].includes(v),
      coerce: (v: any) => {
        if (!['sm', 'md', 'lg'].includes(v)) {
          console.warn(`[ty-multiselect] Invalid size. Using md.`)
          return 'md'
        }
        return v
      }
    },
    flavor: {
      type: 'string' as const,
      visual: true,
      default: 'neutral',
      validate: (v: any) => ['primary', 'secondary', 'success', 'danger', 'warning', 'neutral'].includes(v),
      coerce: (v: any) => {
        const valid = ['primary', 'secondary', 'success', 'danger', 'warning', 'neutral']
        if (!valid.includes(v)) {
          console.warn(`[ty-multiselect] Invalid flavor. Using neutral.`)
          return 'neutral'
        }
        return v
      }
    },
    delay: {
      type: 'number' as const,
      default: 0,
      validate: (v: any) => v >= 0 && v <= 5000,
      coerce: (v: any) => {
        const num = Number(v)
        if (isNaN(num)) return 0
        return Math.max(0, Math.min(5000, num))
      }
    }
  }

  // ============================================================================
  // INTERNAL STATE
  // ============================================================================
  private _value: string = ''
  private _name: string = ''
  private _placeholder: string = 'Select options...'
  private _label: string = ''
  private _disabled = false
  private _readonly = false
  private _required = false
  private _searchable = true
  private _size: Size = 'md'
  private _flavor: Flavor = 'neutral'

  // Component state
  private _state: MultiselectState = {
    open: false,
    search: '',
    highlightedIndex: -1,
    filteredTags: [],
    selectedValues: [],
    mode: isMobileDevice() ? 'mobile' : 'desktop'
  }

  // Scroll lock ID (consistent across open/close cycles)
  private _scrollLockId: string | null = null

  // Event handler references for cleanup
  private _stubClickHandler: ((e: Event) => void) | null = null
  private _outsideClickHandler: ((e: Event) => void) | null = null
  private _tagClickHandler: ((e: Event) => void) | null = null
  private _tagDismissHandler: ((e: Event) => void) | null = null
  private _searchInputHandler: ((e: Event) => void) | null = null
  private _searchBlurHandler: ((e: Event) => void) | null = null
  private _keyboardHandler: ((e: KeyboardEvent) => void) | null = null

  // Delay/debounce properties for ty-search event
  private _delay: number = 0
  private _searchDebounceTimer: number | null = null

  constructor() {
    super() // TyComponent handles attachInternals() and attachShadow()

    const shadow = this.shadowRoot!
    ensureStyles(shadow, { css: multiselectStyles, id: 'ty-multiselect' })

    // Render based on device type
    if (this._state.mode === 'mobile') {
      this.renderMobile()
    } else {
      this.renderDesktop()
    }
  }

  /**
   * Called when component is connected to DOM
   * TyComponent handles property capture automatically
   */
  protected onConnect(): void {
    console.log('🔌 onConnect')
    
    // Initialize after element is connected and children are available
    requestAnimationFrame(() => {
      console.log('⏰ requestAnimationFrame callback')
      this.initializeState()
    })
  }

  /**
   * Called when component is disconnected from DOM
   * Clean up event listeners and timers
   */
  protected onDisconnect(): void {
    // Clean up document-level listeners
    const outsideClickHandler = (this as any).tyOutsideClickHandler
    const keyboardHandler = (this as any).tyKeyboardHandler

    if (outsideClickHandler) {
      document.removeEventListener('click', outsideClickHandler)
        ; (this as any).tyOutsideClickHandler = null
    }

    if (keyboardHandler) {
      document.removeEventListener('keydown', keyboardHandler)
        ; (this as any).tyKeyboardHandler = null
    }

    // Clear any pending debounce timer
    if (this._searchDebounceTimer !== null) {
      clearTimeout(this._searchDebounceTimer)
      this._searchDebounceTimer = null
    }

    // Unlock scroll if still locked
    if (this._scrollLockId) {
      unlockScroll(this._scrollLockId)
      this._scrollLockId = null
    }
  }

  /**
   * Called when properties change
   * Handle state synchronization BEFORE render
   */
  protected onPropertiesChanged(changes: PropertyChange[]): void {
    for (const { name, newValue } of changes) {
      switch (name) {
        case 'value':
          this._value = newValue || ''
          const selectedValues = this.parseValue(newValue)
          this._state.selectedValues = selectedValues
          this.syncSelectedTags(selectedValues)
          this.updatePlaceholderVisibility()
          break
        case 'name':
          this._name = newValue || ''
          break
        case 'placeholder':
          this._placeholder = newValue || 'Select options...'
          this.updatePlaceholderVisibility()
          break
        case 'label':
          this._label = newValue || ''
          break
        case 'disabled':
          this._disabled = newValue
          break
        case 'readonly':
          this._readonly = newValue
          break
        case 'required':
          this._required = newValue
          break
        case 'searchable':
          this._searchable = newValue
          break
        case 'size':
          this._size = newValue
          break
        case 'flavor':
          this._flavor = newValue
          break
        case 'delay':
          this._delay = newValue
          break
      }
    }
  }

  /**
   * Get the form value for this component
   * Returns FormData with multiple entries (HTMX standard)
   */
  protected getFormValue(): FormDataEntryValue | FormData | null {
    const selectedValues = this._state.selectedValues

    if (this._name && selectedValues.length > 0) {
      const formData = new FormData()
      selectedValues.forEach(value => {
        formData.append(this._name, value)
      })
      return formData
    }
    
    return null
  }

  /**
   * Parse multiselect value (comma-separated string to array)
   */
  private parseValue(value: string | null): string[] {
    // Defensive check: ensure value is actually a string before calling .trim()
    if (!value || typeof value !== 'string' || value.trim() === '') return []
    return value.split(',').map(v => v.trim()).filter(v => v !== '')
  }

  /**
   * Initialize component state from attributes
   * Reads from both property and attribute (like ClojureScript version)
   */
  private initializeState(): void {
    console.log('🚀 initializeState')
    console.log('  value from props:', this.getProperty('value'))
    
    const initialValue = this.getProperty('value') || ''
    console.log('  initialValue:', initialValue)
    
    if (initialValue) {
      const selectedValues = this.parseValue(initialValue)
      console.log('  parsed selectedValues:', selectedValues)
      this.updateComponentValue(selectedValues, false)
    } else {
      console.log('  ⚠️  No initial value!')
    }
  }

  // ============================================================================
  // TAG MANAGEMENT METHODS (Phase 2)
  // ============================================================================

  /**
   * Get all ty-tag elements from the component (ALL slots)
   */
  private getTagElements(): HTMLElement[] {
    // Get ALL ty-tag children, regardless of slot assignment
    return Array.from(this.querySelectorAll('ty-tag')) as HTMLElement[]
  }

  /**
   * Extract value and text from a ty-tag element
   */
  private getTagData(element: HTMLElement): TagData {
    // Get value from either property or attribute
    const value = (element as any).value || element.getAttribute('value') || element.textContent || ''
    const text = element.textContent || ''

    return { value, text, element }
  }

  /**
   * Select a tag - set selected state, move to selected slot, make dismissible
   */
  private selectTag(tag: HTMLElement): void {
    // Set selected attribute
    tag.setAttribute('selected', '')

    // Move to selected slot
    tag.setAttribute('slot', 'selected')

    // Make dismissible
    tag.setAttribute('dismissible', 'true')

    // Force re-slotting by removing and re-adding
    const parent = tag.parentNode
    if (parent) {
      parent.removeChild(tag)
      parent.appendChild(tag)
    }
  }

  /**
   * Deselect a tag - remove selected state, remove from selected slot, remove dismissible
   */
  private deselectTag(tag: HTMLElement): void {
    // Remove selected attribute
    tag.removeAttribute('selected')

    // Remove from selected slot
    tag.removeAttribute('slot')

    // Remove dismissible
    tag.removeAttribute('dismissible')

    // Force re-slotting by removing and re-adding
    const parent = tag.parentNode
    if (parent) {
      parent.removeChild(tag)
      parent.appendChild(tag)
    }
  }

  /**
   * Get array of currently selected values from tags (ALWAYS reads from DOM)
   */
  private getSelectedValues(): string[] {
    return this.getTagElements()
      .filter(tag => tag.hasAttribute('selected'))
      .map(tag => this.getTagData(tag).value)
      .filter(value => value !== '')
  }

  /**
   * Check if all available tags are selected
   */
  private allTagsSelected(): boolean {
    const tags = this.getTagElements()
    if (tags.length === 0) return false

    const selectedCount = tags.filter(tag => tag.hasAttribute('selected')).length
    return selectedCount === tags.length
  }

  /**
   * Sync tag selection states with desired values
   */
  private syncSelectedTags(selectedValues: string[]): void {
    const selectedSet = new Set(selectedValues)
    const tags = this.getTagElements()
    
    console.log('🔄 syncSelectedTags')
    console.log('  selectedValues:', selectedValues)
    console.log('  tags found:', tags.length)

    tags.forEach(tag => {
      const tagValue = this.getTagData(tag).value
      const shouldBeSelected = selectedSet.has(tagValue)
      const isSelected = tag.hasAttribute('selected')
      
      console.log(`  tag ${tagValue}: should=${shouldBeSelected}, is=${isSelected}`)

      if (shouldBeSelected && !isSelected) {
        console.log(`    ✅ selecting ${tagValue}`)
        this.selectTag(tag)
      } else if (!shouldBeSelected && isSelected) {
        console.log(`    ❌ deselecting ${tagValue}`)
        this.deselectTag(tag)
      }
    })
  }

  /**
   * Central update function - synchronizes everything
   * Uses TyComponent's property system for proper lifecycle
   */
  private updateComponentValue(newValues: string[], dispatchChange: boolean = false, action: ChangeAction = 'set', item: string | null = null): void {
    const oldValues = this.getSelectedValues()
    const valueStr = newValues.join(',')
    
    console.log('🔄 updateComponentValue called')
    console.log('  oldValues:', oldValues)
    console.log('  newValues:', newValues)
    console.log('  dispatchChange:', dispatchChange)
    
    // Only update if changed
    if (JSON.stringify(newValues.sort()) !== JSON.stringify(oldValues.sort())) {
      console.log('  ✅ Values changed, updating...')
      
      // Use TyComponent's property system - this will trigger:
      // 1. onPropertiesChanged() → syncs tags via syncSelectedTags()
      // 2. onPropertiesChanged() → updates placeholder via updatePlaceholderVisibility()
      // 3. updateFormValue() → automatic (formValue: true in config)
      // 4. render() → automatic if visual properties changed
      this.setProperty('value', valueStr)
      
      // Dispatch custom multiselect change event (with action/item details)
      if (dispatchChange) {
        this.dispatchChangeEvent({
          values: newValues,
          action,
          item
        })
      }
    } else {
      console.log('  ⏭️  Values unchanged, skipping')
    }
  }

  // ============================================================================
  // DROPDOWN METHODS (Phase 3 & 4)
  // ============================================================================

  /**
   * Calculate and set dropdown position with smart direction detection
   */
  private calculatePosition(): void {
    const shadow = this.shadowRoot!
    const stub = shadow.querySelector('.multiselect-stub') as HTMLElement
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
   * Open dropdown dialog (desktop mode)
   */
  private openDropdown(): void {
    const shadow = this.shadowRoot!
    const dialog = shadow.querySelector('.dropdown-dialog') as HTMLDialogElement
    if (!dialog) return

    // Generate consistent unique ID for scroll locking
    const dropdownId = `multiselect-${this.id || 'anon'}-${getElementHash(this)}`
    this._scrollLockId = dropdownId

    // Lock scroll
    lockScroll(dropdownId)

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

    // Initialize options state
    const tags = this.getTagElements().map(el => this.getTagData(el))
    this._state.filteredTags = tags
    this._state.highlightedIndex = -1

    // Focus search input if searchable
    if (this._searchable) {
      const searchInput = shadow.querySelector('.dropdown-search-input') as HTMLInputElement
      if (searchInput) {
        setTimeout(() => searchInput.focus(), 100)
      }
    }
  }

  /**
   * Close dropdown dialog (desktop mode)
   */
  private closeDropdown(): void {
    const shadow = this.shadowRoot!
    const dialog = shadow.querySelector('.dropdown-dialog') as HTMLDialogElement
    if (!dialog) return

    // Unlock scroll using stored consistent ID
    if (this._scrollLockId) {
      unlockScroll(this._scrollLockId)
      this._scrollLockId = null
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

    // Reset search if searchable
    if (this._searchable) {
      this._state.search = ''
      const searchInput = shadow.querySelector('.dropdown-search-input') as HTMLInputElement
      if (searchInput) {
        searchInput.value = ''
      }
    }
  }

  /**
   * Open mobile modal (mobile mode)
   */
  private openMobileModal(): void {
    const shadow = this.shadowRoot!
    const modal = shadow.querySelector('.mobile-modal') as HTMLElement
    if (!modal) return

    // Generate consistent unique ID for scroll locking
    const dropdownId = `multiselect-${this.id || 'anon'}-${getElementHash(this)}`
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
    const tags = this.getTagElements().map(el => this.getTagData(el))
    this._state.filteredTags = tags

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
   * Close mobile modal (mobile mode)
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
  // EVENT HANDLERS (Phase 5 & 6)
  // ============================================================================

  private handleStubClick(e: Event): void {
    e.preventDefault()
    e.stopPropagation()

    if (this._disabled || this._readonly) {
      return
    }

    // Don't open if all tags are already selected
    if (this.allTagsSelected()) {
      return
    }

    this.openDropdown()
  }

  private handleOutsideClick(e: Event): void {
    if (!this._state.open) return

    const target = e.target as Node
    const shadow = this.shadowRoot!

    // Check if click is inside .dropdown-wrapper
    const wrapper = shadow.querySelector('.dropdown-wrapper')
    const clickedInside = wrapper && wrapper.contains(target)

    if (!clickedInside) {
      this.closeDropdown()
    }
  }

  private handleTagClick(e: Event): void {
    const target = e.target as HTMLElement

    // Find the ty-tag element
    const tag = target.tagName === 'TY-TAG'
      ? target
      : target.closest('ty-tag') as HTMLElement | null

    if (!tag || tag.hasAttribute('disabled')) return
    if (tag.hasAttribute('selected')) return // Already selected

    e.preventDefault()
    e.stopPropagation()

    const tagValue = this.getTagData(tag).value
    const currentValues = this.getSelectedValues()
    const newValues = [...currentValues, tagValue]

    // Use central update function with change event
    this.updateComponentValue(newValues, true, 'add', tagValue)

    // Auto-close if all tags selected
    if (this.allTagsSelected()) {
      if (this._state.mode === 'desktop') {
        this.closeDropdown()
      } else {
        this.closeMobileModal()
      }
    }
  }

  private handleTagDismiss(e: Event): void {
    e.preventDefault()
    e.stopPropagation()

    const customEvent = e as CustomEvent
    const tag = customEvent.detail?.target as HTMLElement
    if (!tag) return

    const tagValue = this.getTagData(tag).value
    const currentValues = this.getSelectedValues()
    const newValues = currentValues.filter(v => v !== tagValue)

    // Use central update function with change event
    this.updateComponentValue(newValues, true, 'remove', tagValue)
  }

  private handleSearchInput(e: Event): void {
    const target = e.target as HTMLInputElement
    const query = target.value

    // Update search state
    this._state.search = query

    if (this._searchable) {
      // Internal search: filter tags locally
      const allTags = this.getTagElements().map(el => this.getTagData(el))
      const filtered = this.filterTags(allTags, query)

      // Update state
      this._state.filteredTags = filtered
      this._state.highlightedIndex = -1

      // Update visibility
      this.updateTagVisibility(filtered, allTags)

      // Clear highlights
      this.clearHighlights(allTags)
    } else {
      // External search: dispatch event for external handling
      this.dispatchSearchEvent(query)
    }
  }

  private handleSearchBlur(e: Event): void {
    if (!this._searchable) return

    // Reset search
    this._state.search = ''

    const shadow = this.shadowRoot!
    const searchInput = shadow.querySelector('.dropdown-search-input') as HTMLInputElement
    if (searchInput) {
      searchInput.value = ''
    }

    // Show all tags
    const allTags = this.getTagElements().map(el => this.getTagData(el))
    this._state.filteredTags = allTags
    this.updateTagVisibility(allTags, allTags)
    this.clearHighlights(allTags)
  }

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
    const filteredTags = this._state.filteredTags
    const tagsCount = filteredTags.length
    const currentHighlightedIndex = this._state.highlightedIndex

    switch (e.key) {
      case 'Escape':
        e.preventDefault()
        e.stopPropagation()
        this.closeDropdown()
        break

      case 'Enter':
        e.preventDefault()
        e.stopPropagation()
        // Select highlighted tag if any
        if (currentHighlightedIndex >= 0 && currentHighlightedIndex < tagsCount) {
          const tag = filteredTags[currentHighlightedIndex]
          this.handleTagClick({ target: tag.element } as any)
        }
        break

      case 'ArrowUp':
        e.preventDefault()
        e.stopPropagation()
        let newIndexUp: number

        if (tagsCount === 0) {
          newIndexUp = -1
        } else if (currentHighlightedIndex === -1) {
          // Nothing highlighted, go to last tag
          newIndexUp = tagsCount - 1
        } else if (currentHighlightedIndex === 0) {
          // At first tag, wrap to last
          newIndexUp = tagsCount - 1
        } else {
          // Move up one
          newIndexUp = currentHighlightedIndex - 1
        }

        this._state.highlightedIndex = newIndexUp
        this.highlightTag(filteredTags, newIndexUp)
        break

      case 'ArrowDown':
        e.preventDefault()
        e.stopPropagation()
        let newIndexDown: number

        if (tagsCount === 0) {
          newIndexDown = -1
        } else if (currentHighlightedIndex === -1) {
          // Nothing highlighted, go to first tag
          newIndexDown = 0
        } else if (currentHighlightedIndex === tagsCount - 1) {
          // At last tag, wrap to first
          newIndexDown = 0
        } else {
          // Move down one
          newIndexDown = currentHighlightedIndex + 1
        }

        this._state.highlightedIndex = newIndexDown
        this.highlightTag(filteredTags, newIndexDown)
        break
    }
  }

  // ============================================================================
  // SEARCH & FILTERING HELPERS (Phase 6)
  // ============================================================================

  /**
   * Filter tags based on search query
   */
  private filterTags(tags: TagData[], query: string): TagData[] {
    if (!query || query.trim() === '') {
      return tags
    }

    const searchLower = query.toLowerCase()
    return tags.filter(({ text }) =>
      text.toLowerCase().includes(searchLower)
    )
  }

  /**
   * Update visibility of tags based on filtered list
   */
  private updateTagVisibility(filteredTags: TagData[], allTags: TagData[]): void {
    const visibleValues = new Set(filteredTags.map(tag => tag.value))

    allTags.forEach(({ value, element }) => {
      if (visibleValues.has(value)) {
        element.removeAttribute('hidden')
      } else {
        element.setAttribute('hidden', '')
      }
    })
  }

  /**
   * Clear all tag highlights
   */
  private clearHighlights(tags: TagData[]): void {
    tags.forEach(({ element }) => {
      element.removeAttribute('highlighted')
    })
  }

  /**
   * Highlight tag at specific index
   */
  private highlightTag(tags: TagData[], index: number): void {
    this.clearHighlights(tags)

    if (index >= 0 && index < tags.length) {
      const { element } = tags[index]
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
  // CHANGE EVENT DISPATCHING (Phase 5)
  // ============================================================================

  /**
   * Determine what type of change occurred
   */
  private determineChangeAction(oldValues: string[], newValues: string[]): { action: ChangeAction; item: string | null } {
    const oldSet = new Set(oldValues)
    const newSet = new Set(newValues)

    // Find differences
    const added = [...newSet].filter(v => !oldSet.has(v))
    const removed = [...oldSet].filter(v => !newSet.has(v))

    // Single addition
    if (added.length === 1 && removed.length === 0) {
      return { action: 'add', item: added[0] }
    }

    // Single removal
    if (removed.length === 1 && added.length === 0) {
      return { action: 'remove', item: removed[0] }
    }

    // Clear all
    if (oldValues.length > 0 && newValues.length === 0) {
      return { action: 'clear', item: null }
    }

    // Multiple changes or complete replacement
    return { action: 'set', item: null }
  }

  /**
   * Dispatch custom change event
   */
  private dispatchChangeEvent(detail: ChangeEventDetail): void {
    this.dispatchEvent(new CustomEvent('change', {
      detail,
      bubbles: true,
      cancelable: true
    }))
  }

  // ============================================================================
  // RENDERING
  // ============================================================================

  /**
   * Main render method (required by TyComponent)
   * Delegates to mode-specific renderer
   */
  protected render(): void {
    if (this._state.mode === 'mobile') {
      this.renderMobile()
    } else {
      this.renderDesktop()
    }
  }

  /**
   * Setup event listeners
   */
  private setupEventListeners(): void {
    const shadow = this.shadowRoot!
    const stub = shadow.querySelector('.multiselect-stub')
    const optionsSlot = shadow.querySelector('#options-slot')
    const searchInput = shadow.querySelector('.dropdown-search-input')

    if (stub) {
      this._stubClickHandler = this.handleStubClick.bind(this)
      stub.addEventListener('click', this._stubClickHandler)
    }

    // Add tag click handler to slot
    if (optionsSlot) {
      this._tagClickHandler = this.handleTagClick.bind(this)
      optionsSlot.addEventListener('click', this._tagClickHandler)
    }

    // Add search input handlers
    if (searchInput) {
      this._searchInputHandler = this.handleSearchInput.bind(this)
      this._searchBlurHandler = this.handleSearchBlur.bind(this)

      searchInput.addEventListener('input', this._searchInputHandler)
      searchInput.addEventListener('blur', this._searchBlurHandler)
    }

    // Setup document-level event listeners
    this._outsideClickHandler = this.handleOutsideClick.bind(this)
    this._keyboardHandler = this.handleKeyboard.bind(this)

    document.addEventListener('click', this._outsideClickHandler)
    document.addEventListener('keydown', this._keyboardHandler)

    // Listen for ty-tag-dismiss events from selected tags
    this._tagDismissHandler = this.handleTagDismiss.bind(this)
    this.addEventListener('ty-tag-dismiss', this._tagDismissHandler)
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

    // Only set innerHTML and setup listeners if container doesn't exist
    if (!shadow.querySelector('.multiselect-container')) {
      const stubClasses = this.buildStubClasses()

      const labelHtml = this._label ? `
        <label class="dropdown-label">
          ${this._label}
          ${this._required ? `<span class="required-icon">${REQUIRED_ICON_SVG}</span>` : ''}
        </label>
      ` : ''

      const searchPlaceholder = this._searchable ? 'Search...' : this._placeholder

      shadow.innerHTML = `
        <div class="multiselect-container">
          ${labelHtml}
          <div class="dropdown-wrapper">
            <div class="dropdown-stub multiselect-stub ${stubClasses}" 
                 ${this._disabled ? 'disabled' : ''}>
              <div class="multiselect-chips">
                <slot name="selected"></slot>
              </div>
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

      // Setup event listeners ONCE
      this.setupEventListeners()

      // Don't initialize here - will be done in connectedCallback
      // after properties are set and children are available
    }

    // Always update placeholder visibility on re-render
    this.updatePlaceholderVisibility()
  }

  /**
   * Render mobile mode with full-screen modal
   */
  private renderMobile(): void {
    const shadow = this.shadowRoot!

    // Only set innerHTML and setup listeners if container doesn't exist
    if (!shadow.querySelector('.multiselect-container')) {
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
        <div class="multiselect-container">
          ${labelHtml}
          <div class="dropdown-wrapper">
            <div class="dropdown-stub multiselect-stub ${stubClasses}" 
                 ${this._disabled ? 'disabled' : ''}>
              <div class="multiselect-chips">
                <slot name="selected"></slot>
              </div>
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

      // Setup event listeners ONCE
      this.setupMobileEventListeners()
    }

    // Always update placeholder visibility
    this.updatePlaceholderVisibility()
  }

  /**
   * Setup event listeners for mobile mode
   */
  private setupMobileEventListeners(): void {
    const shadow = this.shadowRoot!
    const stub = shadow.querySelector('.multiselect-stub')
    const optionsSlot = shadow.querySelector('#options-slot')
    const searchInput = shadow.querySelector('.mobile-search-input')
    const backdrop = shadow.querySelector('.mobile-modal-backdrop')
    const closeButton = shadow.querySelector('.mobile-close-button')

    if (stub) {
      stub.addEventListener('click', (e) => this.handleMobileStubClick(e))
    }

    // Add tag click handler to slot
    if (optionsSlot) {
      optionsSlot.addEventListener('click', (e) => this.handleMobileTagClick(e))
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
      ; (this as any).tyKeyboardHandler = keyboardHandler
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

    // Don't open if all tags are already selected
    if (this.allTagsSelected()) {
      return
    }

    this.openMobileModal()
  }

  /**
   * Handle mobile tag click - select and potentially close
   */
  private handleMobileTagClick(e: Event): void {
    // Use the same tag click handler as desktop
    // It already handles mobile mode for auto-close
    this.handleTagClick(e)
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
   * Update placeholder visibility based on selection
   */
  private updatePlaceholderVisibility(): void {
    const shadow = this.shadowRoot!
    const placeholder = shadow.querySelector('.dropdown-placeholder')
    const stub = shadow.querySelector('.multiselect-stub')

    if (placeholder && stub) {
      const hasSelection = this._state.selectedValues.length > 0

      if (hasSelection) {
        placeholder.classList.add('hidden')
        stub.classList.add('has-selection')
      } else {
        placeholder.classList.remove('hidden')
        stub.classList.remove('has-selection')
      }
    }
  }

  // ============================================================================
  // PUBLIC API - Getters/Setters
  // ============================================================================

  get value(): string {
    // Always read from DOM - tags with 'selected' attribute are source of truth
    return this.getSelectedValues().join(',')
  }

  set value(val: string) {
    this.setProperty('value', val)
  }

  get name(): string {
    return this.getProperty('name')
  }

  set name(val: string) {
    this.setProperty('name', val)
  }

  get placeholder(): string {
    return this.getProperty('placeholder')
  }

  set placeholder(val: string) {
    this.setProperty('placeholder', val)
  }

  get label(): string {
    return this.getProperty('label')
  }

  set label(val: string) {
    this.setProperty('label', val)
  }

  get disabled(): boolean {
    return this.getProperty('disabled')
  }

  set disabled(value: boolean) {
    this.setProperty('disabled', value)
  }

  get readonly(): boolean {
    return this.getProperty('readonly')
  }

  set readonly(value: boolean) {
    this.setProperty('readonly', value)
  }

  get required(): boolean {
    return this.getProperty('required')
  }

  set required(value: boolean) {
    this.setProperty('required', value)
  }

  get searchable(): boolean {
    return this.getProperty('searchable')
  }

  set searchable(value: boolean) {
    this.setProperty('searchable', value)
  }

  get delay(): number {
    return this.getProperty('delay')
  }

  set delay(value: number | string) {
    const numValue = typeof value === 'string' ? parseInt(value, 10) : value
    this.setProperty('delay', numValue)
  }

  get size(): Size {
    return this.getProperty('size') as Size
  }

  set size(value: Size) {
    this.setProperty('size', value)
  }

  get flavor(): Flavor {
    return this.getProperty('flavor') as Flavor
  }

  set flavor(value: Flavor) {
    this.setProperty('flavor', value)
  }

  get form(): HTMLFormElement | null {
    return this._internals.form
  }
}

// Register the custom element
if (!customElements.get('ty-multiselect')) {
  customElements.define('ty-multiselect', TyMultiselect)
}
