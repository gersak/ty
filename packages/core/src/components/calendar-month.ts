/**
 * TyCalendarMonth Web Component
 * PORTED FROM: cljs/ty/components/calendar_month.cljs
 * 
 * A stateless, property-driven calendar month renderer with customizable
 * day content and styling.
 * 
 * Features:
 * - 42-day grid (6 weeks × 7 days) with Monday-first ordering
 * - Localized weekday headers
 * - Custom day content rendering via dayContentFn
 * - Custom day classes via dayClassesFn
 * - Custom CSS injection via customCSS
 * - Responsive width system (width, minWidth, maxWidth)
 * - Rich day-click events with full context
 * 
 * @example
 * ```html
 * <!-- Basic usage -->
 * <ty-calendar-month 
 *   display-year="2025" 
 *   display-month="10"
 *   locale="en-US">
 * </ty-calendar-month>
 * 
 * <!-- With custom rendering -->
 * <ty-calendar-month id="custom"></ty-calendar-month>
 * <script type="module">
 *   const cal = document.getElementById('custom');
 *   cal.dayContentFn = (ctx) => {
 *     const el = document.createElement('div');
 *     el.textContent = ctx.dayInMonth;
 *     return el;
 *   };
 * </script>
 * ```
 */

import { ensureStyles } from '../utils/styles.js';
import { calendarMonthStyles } from '../styles/calendar-month.js';
import {
  getCalendarMonthDays,
  getLocalizedWeekdays,
  type DayContext
} from '../utils/calendar-utils.js';
import { getEffectiveLocale, observeLocaleChanges } from '../utils/locale.js';

// ============================================================================
// Types
// ============================================================================

/**
 * Calendar size variants
 */
export type CalendarSize = 'sm' | 'md' | 'lg';

/**
 * Custom day content render function
 * Must return a DOM element or string
 */
export type DayContentFn = (dayContext: DayContext) => HTMLElement | string;

/**
 * Day click event detail
 */
export interface DayClickDetail {
  dayContext: DayContext;
  value: number;
  year: number;
  month: number;
  day: number;
  isHoliday?: boolean;
  isToday?: boolean;
  isWeekend: boolean;
  isOtherMonth: boolean;
}

// ============================================================================
// Default Render Functions
// ============================================================================

/**
 * Default day content renderer - just the day number
 */
function defaultDayContent(dayContext: DayContext): string {
  return dayContext.dayInMonth.toString();
}

/**
 * Default day classes based on day context
 */
function getDefaultDayClasses(dayContext: DayContext): string[] {
  const classes = ['calendar-day'];

  if (dayContext.today) classes.push('today');
  if (dayContext.weekend) classes.push('weekend');
  if (dayContext.otherMonth) classes.push('other-month');
  if (dayContext.isSelected) classes.push('selected');

  return classes;
}

// ============================================================================
// Helper Functions
// ============================================================================

/**
 * Check if value is a DOM element
 */
function isDOMElement(value: unknown): value is HTMLElement {
  return value instanceof HTMLElement;
}

/**
 * Normalize size value - add 'px' if just a number
 */
function normalizeSizeValue(value: string | number | null | undefined): string | null {
  if (!value || value === '') return null;

  const strValue = String(value);

  // If it's just a number, add 'px'
  if (/^\d+(\.\d+)?$/.test(strValue)) {
    return `${strValue}px`;
  }

  // Otherwise use as-is (supports %, rem, vw, etc.)
  return strValue;
}

// ============================================================================
// Component Implementation
// ============================================================================

/**
 * TyCalendarMonth Web Component
 */
export class TyCalendarMonth extends HTMLElement {
  // Private properties (synced with attributes/properties)
  private _displayYear: number;
  private _displayMonth: number;
  private _locale: string = 'en-US';
  private _size: CalendarSize = 'md';
  private _width?: string;
  private _minWidth?: string;
  private _maxWidth?: string;
  private _dayContentFn?: DayContentFn;
  private _customCSS?: CSSStyleSheet;
  private _value?: number; // Selected date as timestamp (from parent calendar)
  private _localeObserver?: () => void; // Cleanup function for locale observer

  /**
   * Observed attributes (minimal - mainly for debugging)
   * Properties are the primary API
   */
  static get observedAttributes(): string[] {
    return ['locale', 'size'];
  }

  constructor() {
    super();

    // Initialize with current date
    const today = new Date();
    this._displayYear = today.getFullYear();
    this._displayMonth = today.getMonth() + 1; // 1-based month

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
    // Read size attribute if present
    const sizeAttr = this.getAttribute('size');
    if (sizeAttr && (sizeAttr === 'sm' || sizeAttr === 'md' || sizeAttr === 'lg')) {
      this._size = sizeAttr;
    }
    
    // Setup locale observer to watch for ancestor lang changes
    this._localeObserver = observeLocaleChanges(this, () => {
      this.render();
    });

    this.render();
  }

  disconnectedCallback() {
    // Cleanup locale observer
    if (this._localeObserver) {
      this._localeObserver();
      this._localeObserver = undefined;
    }
  }

  attributeChangedCallback(name: string, _oldValue: string | null, newValue: string | null) {
    if (name === 'locale') {
      this._locale = newValue || 'en-US';
      this.render();
    } else if (name === 'size') {
      if (newValue === 'sm' || newValue === 'md' || newValue === 'lg') {
        this._size = newValue;
        this.render();
      }
    }
  }

  // ==========================================================================
  // Property Getters/Setters
  // ==========================================================================

  get displayYear(): number {
    return this._displayYear;
  }

  set displayYear(value: number) {
    if (this._displayYear !== value) {
      this._displayYear = value;
      this.render();
    }
  }

  get displayMonth(): number {
    return this._displayMonth;
  }

  set displayMonth(value: number) {
    if (this._displayMonth !== value) {
      this._displayMonth = value;
      this.render();
    }
  }

  get locale(): string {
    return getEffectiveLocale(this, this.getAttribute('locale'));
  }

  set locale(value: string) {
    if (this._locale !== value) {
      this._locale = value;
      this.render();
    }
  }

  get size(): CalendarSize {
    return this._size;
  }

  set size(value: CalendarSize) {
    if (this._size !== value) {
      this._size = value;
      this.render();
    }
  }

  get width(): string | undefined {
    return this._width;
  }

  set width(value: string | undefined) {
    if (this._width !== value) {
      this._width = value;
      this.applyWidthProperties();
    }
  }

  get minWidth(): string | undefined {
    return this._minWidth;
  }

  set minWidth(value: string | undefined) {
    if (this._minWidth !== value) {
      this._minWidth = value;
      this.applyWidthProperties();
    }
  }

  get maxWidth(): string | undefined {
    return this._maxWidth;
  }

  set maxWidth(value: string | undefined) {
    if (this._maxWidth !== value) {
      this._maxWidth = value;
      this.applyWidthProperties();
    }
  }

  get dayContentFn(): DayContentFn | undefined {
    return this._dayContentFn;
  }

  set dayContentFn(fn: DayContentFn | undefined) {
    if (this._dayContentFn !== fn) {
      this._dayContentFn = fn;
      this.render();
    }
  }

  get customCSS(): CSSStyleSheet | undefined {
    return this._customCSS;
  }

  set customCSS(sheet: CSSStyleSheet | undefined) {
    if (this._customCSS !== sheet) {
      this._customCSS = sheet;
      this.applyCustomCSS();
    }
  }

  get value(): number | undefined {
    return this._value;
  }

  set value(timestamp: number | null | undefined) {
    const newValue = timestamp ?? undefined;
    if (this._value !== newValue) {
      this._value = newValue;
      this.render();
    }
  }

  // ==========================================================================
  // Width Property System
  // ==========================================================================

  /**
   * Apply width-related properties as CSS custom properties
   */
  private applyWidthProperties(): void {
    const normalizedWidth = normalizeSizeValue(this._width);
    const normalizedMinWidth = normalizeSizeValue(this._minWidth);
    const normalizedMaxWidth = normalizeSizeValue(this._maxWidth);

    if (normalizedWidth) {
      this.style.setProperty('--calendar-width', normalizedWidth);
    } else {
      this.style.removeProperty('--calendar-width');
    }

    if (normalizedMinWidth) {
      this.style.setProperty('--calendar-min-width', normalizedMinWidth);
    } else {
      this.style.removeProperty('--calendar-min-width');
    }

    if (normalizedMaxWidth) {
      this.style.setProperty('--calendar-max-width', normalizedMaxWidth);
    } else {
      this.style.removeProperty('--calendar-max-width');
    }
  }

  /**
   * Apply custom CSS to shadow root
   */
  private applyCustomCSS(): void {
    if (!this._customCSS || !this.shadowRoot) return;

    const existing = this.shadowRoot.adoptedStyleSheets;
    if (!existing.includes(this._customCSS)) {
      this.shadowRoot.adoptedStyleSheets = [...existing, this._customCSS];
    }
  }

  // ==========================================================================
  // Event Dispatching
  // ==========================================================================

  /**
   * Dispatch day-click custom event with day context
   */
  private dispatchDayClick(dayContext: DayContext, domEvent: Event): void {
    const detail: DayClickDetail = {
      dayContext,
      value: dayContext.value,
      year: dayContext.year,
      month: dayContext.month,
      day: dayContext.dayInMonth,
      isHoliday: dayContext.holiday,
      isToday: dayContext.today,
      isWeekend: dayContext.weekend,
      isOtherMonth: dayContext.otherMonth,
    };

    // Dispatch both 'day-click' and 'day-select' (alias)
    for (const eventType of ['day-click', 'day-select']) {
      const event = new CustomEvent(eventType, {
        detail,
        bubbles: true,
        cancelable: true,
      });
      this.dispatchEvent(event);
    }
  }

  // ==========================================================================
  // Rendering
  // ==========================================================================

  /**
   * Render a single day cell
   */
  private renderDayCell(dayContext: DayContext): HTMLElement {
    const dayElement = document.createElement('div');

    // Get content using custom or default function
    const content = this._dayContentFn
      ? this._dayContentFn(dayContext)
      : defaultDayContent(dayContext);

    // STRICT DOM-ONLY CHECK for custom functions
    if (this._dayContentFn && content && !isDOMElement(content) && typeof content !== 'string') {
      throw new Error(
        `Custom dayContentFn must return a DOM element or string, got: ${typeof content}. ` +
        `Use document.createElement() to create DOM elements.`
      );
    }

    // Apply default classes
    const classes = getDefaultDayClasses(dayContext);
    dayElement.className = classes.join(' ');

    // Set content
    if (typeof content === 'string') {
      dayElement.textContent = content;
    } else if (isDOMElement(content)) {
      dayElement.appendChild(content);
    }

    // Add click handler
    dayElement.addEventListener('pointerdown', (event: Event) => {
      event.preventDefault();
      this.dispatchDayClick(dayContext, event);
    });

    return dayElement;
  }

  /**
   * Main render function - property-based approach
   */
  private render(): void {
    const root = this.shadowRoot;
    if (!root) return;

    // Ensure styles are loaded
    ensureStyles(root, { css: calendarMonthStyles, id: 'ty-calendar-month' });

    // Apply custom CSS if provided
    this.applyCustomCSS();

    // Apply width properties
    this.applyWidthProperties();

    // Convert timestamp to selection info
    let selection: { year?: number; month?: number; day?: number } | undefined;
    if (this._value) {
      const date = new Date(this._value);
      selection = {
        year: date.getFullYear(),
        month: date.getMonth() + 1, // Convert to 1-based
        day: date.getDate()
      };
    }

    // Generate calendar days with selection info
    const days = getCalendarMonthDays(this._displayYear, this._displayMonth, selection);

    // Get localized weekday names
    const weekdays = getLocalizedWeekdays(this._locale, 'short');

    // Clear and rebuild
    root.innerHTML = '';

    // Create unified flex container
    const calendarContainer = document.createElement('div');
    calendarContainer.className = `calendar-flex-container calendar-size-${this._size}`;

    // Create header row with weekday names
    const headerRow = document.createElement('div');
    headerRow.className = 'calendar-row calendar-header-row';

    weekdays.forEach(weekday => {
      const headerCell = document.createElement('div');
      headerCell.className = 'calendar-cell calendar-header-cell';
      headerCell.textContent = weekday;
      headerRow.appendChild(headerCell);
    });

    calendarContainer.appendChild(headerRow);

    // Create day rows (6 weeks of 7 days each)
    const dayWeeks: DayContext[][] = [];
    for (let i = 0; i < 42; i += 7) {
      dayWeeks.push(days.slice(i, i + 7));
    }

    dayWeeks.forEach(week => {
      const dayRow = document.createElement('div');
      dayRow.className = 'calendar-row calendar-day-row';

      week.forEach(dayContext => {
        const dayCell = this.renderDayCell(dayContext);
        // Add unified cell classes for flex layout
        dayCell.className = `${dayCell.className} calendar-cell calendar-day-cell`;
        dayRow.appendChild(dayCell);
      });

      calendarContainer.appendChild(dayRow);
    });

    root.appendChild(calendarContainer);
  }
}

// Register the custom element
if (!customElements.get('ty-calendar-month')) {
  customElements.define('ty-calendar-month', TyCalendarMonth);
}
