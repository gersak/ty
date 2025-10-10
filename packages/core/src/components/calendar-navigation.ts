/**
 * TyCalendarNavigation Web Component
 * PORTED FROM: cljs/ty/components/calendar_navigation.cljs
 * 
 * A pure presentation component for calendar month/year navigation.
 * Stateless - all state comes from properties, changes emitted via events.
 * 
 * Features:
 * - Year/Month navigation (prev/next month, prev/next year)
 * - Localized month name display
 * - Custom event emission on navigation
 * - Property-driven API (no internal state)
 * - Inline SVG icons
 * - Shadow DOM encapsulation
 * 
 * @example
 * ```html
 * <!-- Basic usage -->
 * <ty-calendar-navigation 
 *   display-month="10" 
 *   display-year="2025"
 *   locale="en-US">
 * </ty-calendar-navigation>
 * 
 * <!-- Listen for changes -->
 * <script type="module">
 *   const nav = document.querySelector('ty-calendar-navigation');
 *   nav.addEventListener('change', (e) => {
 *     console.log('New month:', e.detail.month);
 *     console.log('New year:', e.detail.year);
 *   });
 * </script>
 * ```
 */

import { ensureStyles } from '../utils/styles.js';
import { calendarNavigationStyles } from '../styles/calendar-navigation.js';
import { getMonthName } from '../utils/calendar-utils.js';

// ============================================================================
// Types
// ============================================================================

/**
 * Navigation change event detail
 */
export interface NavigationChangeDetail {
  month: number;  // 1-12
  year: number;   // e.g., 2025
}

// ============================================================================
// SVG Icons (Material Design)
// ============================================================================

const CHEVRON_LEFT_SVG = `<?xml version='1.0' encoding='UTF-8'?>
<svg width='24' viewBox='0 0 24 24' height='24' xmlns='http://www.w3.org/2000/svg' stroke-width='0' stroke='currentColor' fill='currentColor'>
<path fill='none' d='M0 0h24v24H0V0z'/>
<path d='M15.41 16.59L10.83 12l4.58-4.59L14 6l-6 6 6 6 1.41-1.41z'/>
</svg>`;

const CHEVRON_RIGHT_SVG = `<?xml version='1.0' encoding='UTF-8'?>
<svg width='24' viewBox='0 0 24 24' height='24' xmlns='http://www.w3.org/2000/svg' stroke-width='0' stroke='currentColor' fill='currentColor'>
<path fill='none' d='M0 0h24v24H0V0z'/>
<path d='M8.59 16.59L13.17 12 8.59 7.41 10 6l6 6-6 6-1.41-1.41z'/>
</svg>`;

const CHEVRONS_LEFT_SVG = `<?xml version='1.0' encoding='UTF-8'?>
<svg width='24' viewBox='0 0 24 24' height='24' enable-background='new 0 0 24 24' xmlns='http://www.w3.org/2000/svg' stroke-width='0' stroke='currentColor' fill='currentColor'>
<g>
<rect width='24' height='24' fill='none'/>
</g>
<g>
<g>
<polygon points='17.59,18 19,16.59 14.42,12 19,7.41 17.59,6 11.59,12'/>
<polygon points='11,18 12.41,16.59 7.83,12 12.41,7.41 11,6 5,12'/>
</g>
</g>
</svg>`;

const CHEVRONS_RIGHT_SVG = `<?xml version='1.0' encoding='UTF-8'?>
<svg width='24' viewBox='0 0 24 24' height='24' enable-background='new 0 0 24 24' xmlns='http://www.w3.org/2000/svg' stroke-width='0' stroke='currentColor' fill='currentColor'>
<g>
<rect width='24' height='24' fill='none'/>
</g>
<g>
<g>
<polygon points='6.41,6 5,7.41 9.58,12 5,16.59 6.41,18 12.41,12'/>
<polygon points='13,6 11.59,7.41 16.17,12 11.59,16.59 13,18 19,12'/>
</g>
</g>
</svg>`;

// ============================================================================
// Component Implementation
// ============================================================================

/**
 * TyCalendarNavigation Web Component
 */
export class TyCalendarNavigation extends HTMLElement {
  // Private properties
  private _displayMonth: number;
  private _displayYear: number;
  private _locale: string = 'en-US';
  private _size: 'sm' | 'md' | 'lg' = 'md';
  private _width?: string;
  
  /**
   * Observed attributes (minimal - properties are primary API)
   */
  static get observedAttributes(): string[] {
    return ['locale'];
  }
  
  constructor() {
    super();
    
    // Initialize with current date
    const today = new Date();
    this._displayMonth = today.getMonth() + 1; // 1-based
    this._displayYear = today.getFullYear();
    
    this.attachShadow({ mode: 'open' });
  }
  
  // ==========================================================================
  // Lifecycle Methods
  // ==========================================================================
  
  connectedCallback() {
    // Set defaults if not already set
    if (!this._displayMonth) {
      this._displayMonth = new Date().getMonth() + 1;
    }
    if (!this._displayYear) {
      this._displayYear = new Date().getFullYear();
    }
    if (!this._locale) {
      this._locale = this.getAttribute('locale') || 'en-US';
    }
    
    this.render();
  }
  
  attributeChangedCallback(name: string, _oldValue: string | null, newValue: string | null) {
    if (name === 'locale') {
      this._locale = newValue || 'en-US';
      this.render();
    }
  }
  
  // ==========================================================================
  // Property Getters/Setters
  // ==========================================================================
  
  get displayMonth(): number {
    return this._displayMonth;
  }
  
  set displayMonth(value: number) {
    if (this._displayMonth !== value) {
      this._displayMonth = value;
      this.render();
    }
  }
  
  get displayYear(): number {
    return this._displayYear;
  }
  
  set displayYear(value: number) {
    if (this._displayYear !== value) {
      this._displayYear = value;
      this.render();
    }
  }
  
  get locale(): string {
    return this._locale;
  }
  
  set locale(value: string) {
    if (this._locale !== value) {
      this._locale = value;
      this.render();
    }
  }
  
  get size(): 'sm' | 'md' | 'lg' {
    return this._size;
  }
  
  set size(value: 'sm' | 'md' | 'lg') {
    if (this._size !== value) {
      this._size = value;
      this.applySizeAttribute();
    }
  }
  
  get width(): string | undefined {
    return this._width;
  }
  
  set width(value: string | undefined) {
    if (this._width !== value) {
      this._width = value;
      this.applyWidthProperty();
    }
  }
  
  // ==========================================================================
  // Size & Width Property System
  // ==========================================================================
  
  /**
   * Apply size as data attribute for CSS targeting
   */
  private applySizeAttribute(): void {
    this.setAttribute('data-size', this._size);
  }
  
  /**
   * Apply width property as CSS custom property
   */
  private applyWidthProperty(): void {
    if (this._width) {
      this.style.setProperty('--nav-width', this._width);
    } else {
      this.style.removeProperty('--nav-width');
    }
  }
  
  // ==========================================================================
  // Event Dispatching
  // ==========================================================================
  
  /**
   * Emit change event with new month/year
   */
  private emitChangeEvent(month: number, year: number): void {
    const detail: NavigationChangeDetail = {
      month,
      year,
    };
    
    const event = new CustomEvent('change', {
      detail,
      bubbles: true,
      cancelable: true,
    });
    
    this.dispatchEvent(event);
  }
  
  // ==========================================================================
  // Navigation Logic
  // ==========================================================================
  
  /**
   * Navigate to previous/next month
   * Handles year boundary crossing
   */
  private navigateMonth(direction: -1 | 1): void {
    const rawMonth = this._displayMonth + direction;
    
    let newMonth: number;
    let newYear: number;
    
    if (rawMonth < 1) {
      // Rolled back to previous year
      newMonth = 12;
      newYear = this._displayYear - 1;
    } else if (rawMonth > 12) {
      // Rolled forward to next year
      newMonth = 1;
      newYear = this._displayYear + 1;
    } else {
      newMonth = rawMonth;
      newYear = this._displayYear;
    }
    
    this.emitChangeEvent(newMonth, newYear);
  }
  
  /**
   * Navigate to previous/next year
   */
  private navigateYear(direction: -1 | 1): void {
    const newYear = this._displayYear + direction;
    this.emitChangeEvent(this._displayMonth, newYear);
  }
  
  // ==========================================================================
  // Rendering
  // ==========================================================================
  
  /**
   * Create navigation button
   */
  private createButton(
    className: string,
    title: string,
    svg: string,
    onClick: () => void
  ): HTMLButtonElement {
    const button = document.createElement('button');
    button.className = className;
    button.title = title;
    button.innerHTML = svg;
    button.addEventListener('click', onClick);
    return button;
  }
  
  /**
   * Main render function
   */
  private render(): void {
    const root = this.shadowRoot;
    if (!root) return;
    
    // Ensure styles are loaded
    ensureStyles(root, { css: calendarNavigationStyles, id: 'ty-calendar-navigation' });
    
    // Apply size and width properties
    this.applySizeAttribute();
    this.applyWidthProperty();
    
    // Get localized month name
    const monthName = getMonthName(this._displayMonth, this._locale, 'long');
    
    // Clear and rebuild
    root.innerHTML = '';
    
    // Create main header container
    const header = document.createElement('div');
    header.className = 'calendar-navigation-header';
    
    // Left group: [⟪ ‹]
    const leftGroup = document.createElement('div');
    leftGroup.className = 'nav-group nav-group-left';
    
    leftGroup.appendChild(
      this.createButton(
        'nav-btn nav-year-prev',
        'Previous year',
        CHEVRONS_LEFT_SVG,
        () => this.navigateYear(-1)
      )
    );
    
    leftGroup.appendChild(
      this.createButton(
        'nav-btn nav-month-prev',
        'Previous month',
        CHEVRON_LEFT_SVG,
        () => this.navigateMonth(-1)
      )
    );
    
    // Center group: [Month Year]
    const centerGroup = document.createElement('div');
    centerGroup.className = 'nav-group nav-group-center';
    
    const monthYearDisplay = document.createElement('div');
    monthYearDisplay.className = 'month-year-display';
    monthYearDisplay.textContent = `${monthName} ${this._displayYear}`;
    centerGroup.appendChild(monthYearDisplay);
    
    // Right group: [› ⟫]
    const rightGroup = document.createElement('div');
    rightGroup.className = 'nav-group nav-group-right';
    
    rightGroup.appendChild(
      this.createButton(
        'nav-btn nav-month-next',
        'Next month',
        CHEVRON_RIGHT_SVG,
        () => this.navigateMonth(1)
      )
    );
    
    rightGroup.appendChild(
      this.createButton(
        'nav-btn nav-year-next',
        'Next year',
        CHEVRONS_RIGHT_SVG,
        () => this.navigateYear(1)
      )
    );
    
    // Assemble header
    header.appendChild(leftGroup);
    header.appendChild(centerGroup);
    header.appendChild(rightGroup);
    
    root.appendChild(header);
  }
}

// Register the custom element
if (!customElements.get('ty-calendar-navigation')) {
  customElements.define('ty-calendar-navigation', TyCalendarNavigation);
}
