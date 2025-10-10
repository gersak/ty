/**
 * Popup Component Styles
 * 
 * Popup component with dropdown-like behavior and tooltip positioning.
 * Uses dialog element for top layer rendering like dropdown and date-picker.
 */

export const popupStyles = `
/* Popup component with dropdown-like behavior and tooltip positioning */
/* Uses dialog element for top layer rendering like dropdown and date-picker */

.popup-dialog {
  position: fixed;
  top: 0;
  left: 0;

  /* Reset dialog defaults */
  margin: 0;
  border: none;
  background: transparent;
  max-width: none;
  max-height: none;

  /* Shadow infrastructure - provide space for user shadows */
  padding: var(--popup-padding, 16px);

  /* Hidden by default - even when [open] (for positioning phase) */
  visibility: hidden;
  opacity: 0;
  /* NO TRANSFORM here - apply only when animating to avoid measurement errors! */

  /* Smooth transitions */
  transition:
    opacity 150ms ease-out,
    visibility 150ms ease-out,
    transform 150ms ease-out;

  /* Prevent interactions when hidden */
  pointer-events: none;
}

/* Apply position variables only when position is calculated (like dropdown) */
.popup-dialog.position-calculated {
  top: var(--popup-y, 0px);
  left: var(--popup-x, 0px);
}

/* Apply scale for entrance animation (right before .open) */
.popup-dialog.preparing-animation {
  transform: scale(0.95);
}

/* When open - smooth entrance animation */
.popup-dialog.open {
  visibility: visible;
  opacity: 1;
  transform: scale(1);
  pointer-events: auto;
}

/* Make dialog backdrop transparent like dropdown/date-picker */
.popup-dialog::backdrop {
  background: transparent;
}

/* Inner container - neutral structural container */
.popup-container {
  /* No default styling - just provides structure */
  /* Users control all visual aspects via slotted content */
  display: contents;
}

/* Content slot styling */
#popup-content {
  /* Allow slotted content to define its own styling */
  display: contents;
}

/* Remove default styling from slotted content - users have full control */
#popup-content ::slotted(*) {
  /* Remove default styling that conflicts with user styling */
  /* Users now have complete control over popup appearance */
  /* background: unset; */
  /* border: unset; */
  /* box-shadow: unset; */

  /* Only maintain essential positioning */
  position: relative;

  /* Prevent content from being too wide - still helpful */
  max-width: min(400px, 90vw);
  max-height: min(500px, 80vh);
  overflow: auto;
}
`;
