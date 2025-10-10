/**
 * Calendar Month Styles
 * Improved design with better visual hierarchy and size variants
 */

export const calendarMonthStyles = `
/* ============================================================================
   Base Calendar Container
   ============================================================================ */

.calendar-flex-container {
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  gap: 0;
  padding: 0.75rem;
  border-radius: 0.5rem;
  font-family: system-ui, sans-serif;
  user-select: none;
  width: var(--calendar-width, fit-content);
  min-width: 280px;
  max-width: var(--calendar-max-width, none);
}

/* ============================================================================
   Rows (Header + 6 Day Rows)
   ============================================================================ */

.calendar-row {
  display: flex;
  flex: 1;
  min-height: 0;
}

.calendar-header-row {
  flex: 0 0 auto;
  color: var(--ty-color-neutral-strong);
}

.calendar-day-row {
  flex: 1;
  min-height: 2rem;
  margin-bottom: 0.125rem;
}

/* ============================================================================
   Base Cell Styles
   ============================================================================ */

.calendar-cell {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 0;
  position: relative;
  box-sizing: border-box;
}

/* ============================================================================
   Header Cells
   ============================================================================ */

.calendar-header-cell {
  text-align: center;
  font-weight: 600;
  text-transform: uppercase;
  padding: 0.25rem;
  letter-spacing: 0.05em;
}

/* ============================================================================
   Day Cells - Square with Better Visual Hierarchy
   ============================================================================ */

.calendar-day-cell {
  /* Square cells */
  aspect-ratio: 1;
  
  /* Layout */
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  
  /* Spacing */
  margin: 0.125rem;
  
  /* Visual */
  border-radius: 0.375rem;
  border: 1px solid var(--ty-border);
  color: var(--ty-color-neutral);
  cursor: pointer;
  transition: all 0.15s ease;
  
  /* Typography */
  font-weight: 400;
}

/* Hover State - Stronger Feedback */
.calendar-day-cell:hover {
  color: var(--ty-color-neutral-strong);
  background-color: var(--ty-bg-neutral-soft);
  border-color: var(--ty-border-mild);
}

/* ============================================================================
   Day States
   ============================================================================ */

/* Today - Strong Visual Indicator */
.calendar-day-cell.today {
  background-color: var(--ty-bg-secondary-soft);
  color: var(--ty-color-secondary-strong);
  border-color: var(--ty-color-secondary);
  font-weight: 600;
}

/* Weekend - Subtle Color Shift */
.calendar-day-cell.weekend {
  color: var(--ty-color-danger-soft);
}

/* Other Month - Muted */
.calendar-day-cell.other-month {
  color: var(--ty-color-neutral-faint);
  opacity: 0.5;
}

.calendar-day-cell.other-month:hover {
  background-color: var(--ty-bg-neutral-soft);
  opacity: 0.7;
}

/* Selected State (for custom usage) */
.calendar-day-cell.selected {
  color: var(--ty-color-neutral-strong) !important;
  border-color: var(--ty-border-mild);
  background-color: var(--ty-bg-neutral);
  font-weight: 500;
}

.calendar-day-cell.selected.other-month {
  opacity: 0.6;
}

/* ============================================================================
   Size Variants
   ============================================================================ */

/* Small - Compact (240px min-width) */
.calendar-size-sm {
  padding: 0.5rem;
  min-width: 240px;
}

.calendar-size-sm .calendar-header-cell {
  font-size: 0.625rem;
  padding: 0.125rem;
}

.calendar-size-sm .calendar-day-cell {
  font-size: 0.75rem;
  margin: 0.0625rem;
}

.calendar-size-sm .calendar-day-row {
  min-height: 1.5rem;
}

/* Medium - Default (280px min-width) */
.calendar-size-md {
  padding: 0.75rem;
  min-width: 280px;
}

.calendar-size-md .calendar-header-cell {
  font-size: 0.6875rem;
  padding: 0.25rem;
}

.calendar-size-md .calendar-day-cell {
  font-size: 0.8125rem;
  margin: 0.125rem;
}

.calendar-size-md .calendar-day-row {
  min-height: 2rem;
}

/* Large - Spacious (360px min-width) */
.calendar-size-lg {
  padding: 1rem;
  min-width: 360px;
}

.calendar-size-lg .calendar-header-cell {
  font-size: 0.75rem;
  padding: 0.375rem;
}

.calendar-size-lg .calendar-day-cell {
  font-size: 0.875rem;
  margin: 0.1875rem;
}

.calendar-size-lg .calendar-day-row {
  min-height: 2.5rem;
}

/* ============================================================================
   Backwards Compatibility
   ============================================================================ */

.calendar-day {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
}
`;
