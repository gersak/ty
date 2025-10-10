/**
 * Calendar Utilities
 * 
 * Pure TypeScript utilities for calendar month generation and localization.
 * Ported from ClojureScript ty/date/core.cljs
 * 
 * Key Features:
 * - 42-day calendar grid generation (6 weeks × 7 days)
 * - Monday-first week ordering
 * - Rich day context with metadata
 * - Localized weekday names
 * - No external dependencies (native Date API only)
 */

// ============================================================================
// Types
// ============================================================================

/**
 * Rich context for a single calendar day
 * Contains all metadata needed for rendering and event handling
 */
export interface DayContext {
  /** Timestamp in milliseconds (UTC) */
  value: number;
  
  /** Year (e.g., 2025) */
  year: number;
  
  /** Month (1-12, 1 = January) */
  month: number;
  
  /** Day of month (1-31) */
  dayInMonth: number;
  
  /** Is this a weekend day? (Saturday or Sunday) */
  weekend: boolean;
  
  /** Is this day from a different month? */
  otherMonth: boolean;
  
  /** Is this from the previous month? */
  prevMonth?: boolean;
  
  /** Is this from the next month? */
  nextMonth?: boolean;
  
  /** Is this today? */
  today?: boolean;
  
  /** Is this a holiday? (extensible for future use) */
  holiday?: boolean;
  
  /** Is this day selected? (from calendar's internal state) */
  isSelected?: boolean;
  
  /** Calendar's selected year (if any) */
  selectedYear?: number;
  
  /** Calendar's selected month (if any) */
  selectedMonth?: number;
  
  /** Calendar's selected day (if any) */
  selectedDay?: number;
}

// ============================================================================
// Helper Functions
// ============================================================================

/**
 * Check if two dates are on the same day (ignoring time)
 */
function isSameDay(date1: Date, date2: Date): boolean {
  return date1.getFullYear() === date2.getFullYear()
    && date1.getMonth() === date2.getMonth()
    && date1.getDate() === date2.getDate();
}

/**
 * Create a DayContext from a Date object
 * 
 * @param date - The date to create context for
 * @param targetYear - The year of the calendar month being displayed
 * @param targetMonth - The month (1-12) of the calendar month being displayed
 * @param selection - Optional selection state from calendar
 * @returns Rich day context with metadata
 */
function createDayContext(
  date: Date,
  targetYear: number,
  targetMonth: number,
  selection?: { year?: number; month?: number; day?: number }
): DayContext {
  const year = date.getFullYear();
  const month = date.getMonth() + 1; // Convert to 1-based
  const dayInMonth = date.getDate();
  const dayOfWeek = date.getDay(); // 0 = Sunday, 6 = Saturday
  
  // Check if this day is from a different month
  const isOtherMonth = year !== targetYear || month !== targetMonth;
  
  // Determine if previous or next month
  let isPrevMonth = false;
  let isNextMonth = false;
  
  if (isOtherMonth) {
    if (year < targetYear || (year === targetYear && month < targetMonth)) {
      isPrevMonth = true;
    } else {
      isNextMonth = true;
    }
  }
  
  // Check if today
  const today = new Date();
  const isToday = isSameDay(date, today);
  
  // Check if this day is selected
  const isSelected = selection?.year === year 
    && selection?.month === month 
    && selection?.day === dayInMonth;
  
  return {
    value: date.getTime(),
    year,
    month,
    dayInMonth,
    weekend: dayOfWeek === 0 || dayOfWeek === 6,
    otherMonth: isOtherMonth,
    prevMonth: isPrevMonth,
    nextMonth: isNextMonth,
    today: isToday,
    isSelected,
    selectedYear: selection?.year,
    selectedMonth: selection?.month,
    selectedDay: selection?.day,
  };
}

/**
 * Get the Monday at or before a given date
 * (Monday = start of week in ISO 8601)
 * 
 * @param date - Any date
 * @returns The Monday of that week
 */
function getMondayOfWeek(date: Date): Date {
  const dayOfWeek = date.getDay(); // 0 = Sunday, 1 = Monday, etc.
  
  // Calculate days to subtract to get to Monday
  // Sunday (0) → go back 6 days
  // Monday (1) → go back 0 days
  // Tuesday (2) → go back 1 day
  // ...
  const daysToMonday = dayOfWeek === 0 ? 6 : dayOfWeek - 1;
  
  const monday = new Date(date);
  monday.setDate(date.getDate() - daysToMonday);
  
  return monday;
}

/**
 * Get the start date for the calendar grid
 * (The Monday at or before the first day of the month)
 * 
 * @param year - Year
 * @param month - Month (1-12)
 * @returns The Monday that starts the calendar grid
 */
function getMonthGridStart(year: number, month: number): Date {
  // Get first day of month (month is 1-based, Date constructor is 0-based)
  const firstDay = new Date(year, month - 1, 1);
  
  // Get the Monday of that week
  return getMondayOfWeek(firstDay);
}

/**
 * Generate N consecutive days starting from a date
 * 
 * @param startDate - Starting date
 * @param count - Number of days to generate
 * @param targetYear - Year of the calendar month
 * @param targetMonth - Month (1-12) of the calendar month
 * @param selection - Optional selection state to include in day contexts
 * @returns Array of day contexts
 */
function generateDays(
  startDate: Date,
  count: number,
  targetYear: number,
  targetMonth: number,
  selection?: { year?: number; month?: number; day?: number }
): DayContext[] {
  return Array.from({ length: count }, (_, i) => {
    const date = new Date(startDate);
    date.setDate(startDate.getDate() + i);
    return createDayContext(date, targetYear, targetMonth, selection);
  });
}

// ============================================================================
// Main Calendar Functions
// ============================================================================

/**
 * Generate a 42-day calendar grid for a given month
 * 
 * Returns 6 weeks (42 days) starting from the Monday at or before
 * the first day of the month. This ensures a complete calendar view
 * with days from the previous and next months as needed.
 * 
 * @param year - Year (e.g., 2025)
 * @param month - Month (1-12, 1 = January)
 * @param selection - Optional selection state to include in day contexts
 * @returns Array of 42 day contexts with rich metadata
 * 
 * @example
 * const days = getCalendarMonthDays(2025, 10); // October 2025
 * console.log(days.length); // 42
 * console.log(days[0].dayInMonth); // First day shown (might be from September)
 */
export function getCalendarMonthDays(
  year: number, 
  month: number,
  selection?: { year?: number; month?: number; day?: number }
): DayContext[] {
  const startDate = getMonthGridStart(year, month);
  return generateDays(startDate, 42, year, month, selection);
}

/**
 * Get localized weekday names in Monday-first order
 * 
 * Note: Intl.DateTimeFormat returns Sunday-first by default,
 * so we reorder to Monday-first: [Mon, Tue, Wed, Thu, Fri, Sat, Sun]
 * 
 * @param locale - Locale string (e.g., "en-US", "de-DE", "ja-JP")
 * @param style - Display style: "long" (Monday), "short" (Mon), "narrow" (M)
 * @returns Array of 7 weekday names starting with Monday
 * 
 * @example
 * getLocalizedWeekdays("en-US", "short")
 * // Returns: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]
 * 
 * getLocalizedWeekdays("de-DE", "long")
 * // Returns: ["Montag", "Dienstag", "Mittwoch", ...]
 */
export function getLocalizedWeekdays(
  locale: string,
  style: 'long' | 'short' | 'narrow' = 'short'
): string[] {
  const formatter = new Intl.DateTimeFormat(locale, { weekday: style });
  
  // Create a Sunday (base date)
  const sunday = new Date(2024, 0, 7); // January 7, 2024 is a Sunday
  
  // Generate Sunday-first array: [Sun, Mon, Tue, Wed, Thu, Fri, Sat]
  const sundayFirst = Array.from({ length: 7 }, (_, i) => {
    const date = new Date(sunday);
    date.setDate(sunday.getDate() + i);
    return formatter.format(date);
  });
  
  // Reorder to Monday-first: [Mon, Tue, Wed, Thu, Fri, Sat, Sun]
  const [firstDay, ...rest] = sundayFirst;
  return [...rest, firstDay];
}

// ============================================================================
// Utility Functions (for testing and debugging)
// ============================================================================

/**
 * Get month name in specified locale
 * 
 * @param month - Month (1-12)
 * @param locale - Locale string
 * @param style - Display style
 * @returns Localized month name
 */
export function getMonthName(
  month: number,
  locale: string = 'en-US',
  style: 'long' | 'short' | 'narrow' = 'long'
): string {
  const date = new Date(2024, month - 1, 1);
  const formatter = new Intl.DateTimeFormat(locale, { month: style });
  return formatter.format(date);
}

/**
 * Format a day context as an ISO date string
 * 
 * @param dayContext - Day context to format
 * @returns ISO date string (YYYY-MM-DD)
 */
export function formatDayContext(dayContext: DayContext): string {
  const { year, month, dayInMonth } = dayContext;
  const monthStr = month.toString().padStart(2, '0');
  const dayStr = dayInMonth.toString().padStart(2, '0');
  return `${year}-${monthStr}-${dayStr}`;
}

/**
 * Check if a day context matches a specific date
 * 
 * @param dayContext - Day context to check
 * @param year - Target year
 * @param month - Target month (1-12)
 * @param day - Target day
 * @returns True if the day context matches
 */
export function isDayContext(
  dayContext: DayContext,
  year: number,
  month: number,
  day: number
): boolean {
  return dayContext.year === year
    && dayContext.month === month
    && dayContext.dayInMonth === day;
}
