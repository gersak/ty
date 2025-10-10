/**
 * Calendar Orchestration Styles
 * Simple container for navigation + month display
 */

export const calendarStyles = `
/* ============================================================================
   Calendar Container
   ============================================================================ */

.calendar-container {
  display: flex;
  flex-direction: column;
  gap: 0.125rem;
  box-sizing: border-box;
  font-family: system-ui, sans-serif;
}

/* ============================================================================
   Responsive Adjustments
   ============================================================================ */

@media (max-width: 320px) {
  .calendar-container {
    gap: 0.375rem;
  }
}
`;
