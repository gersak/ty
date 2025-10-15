/**
 * Option Component Styles
 * PORTED FROM: clj/ty/components/option.css
 */

export const optionStyles = `
/* Ty Option Component Styles */

.option-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--ty-spacing-2);
  box-sizing: border-box;
  width: 100%;

  /* Ensure content is clickable */
  cursor: pointer;
  user-select: none;

  /* Smooth transitions */
  transition: var(--ty-transition-all);

  /* Text styling */
  color: var(--ty-text);
  font-family: var(--ty-font-sans);
  font-size: var(--ty-font-sm);
  line-height: var(--ty-line-height-normal);
  padding: var(--ty-spacing-2) var(--ty-spacing-3);
}

:host([cloned]) .option-content {
  padding: 0px;
  background: transparent;
}

.option-content:hover {
  background: var(--ty-bg-primary-soft);
}

.option-content[highlighted] {
  background: var(--ty-bg-primary-soft);
  color: var(--ty-text-strong);
  font-weight: var(--ty-font-semibold);
}

.option-content[selected] {
  background: var(--ty-bg-primary-mild);
  color: var(--ty-text-strong);
  font-weight: var(--ty-font-medium);
}

.option-text {
  flex: 1;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.option-clear-btn {
  flex-shrink: 0;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.6);
  border: none;
  border-radius: 50%;
  cursor: pointer;
  padding: 0;
  transition: background 200ms ease;
}

.option-clear-btn:hover {
  background: rgba(0, 0, 0, 0.75);
}

.option-clear-btn:active {
  background: rgba(0, 0, 0, 0.9);
  transform: scale(0.95);
}

.option-clear-btn svg {
  width: 18px;
  height: 18px;
  color: #ffffff;
  stroke-width: 2.5;
}

.option-content[disabled] {
  opacity: 0.5;
  cursor: not-allowed;
}

.option-content[hidden] {}

/* Ensure nested elements inherit proper styling */
.option-content * {
  pointer-events: none;
}
`
