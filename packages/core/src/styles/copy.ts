/**
 * Copy Component Styles
 * Read-only field with copy-to-clipboard functionality
 * Extends input styles with copy-specific styling
 */

export const copyStyles = `
/* Copy field value display */
.copy-field-value {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  user-select: none;
  padding: 0;
  margin: 0;
  background: transparent;
  border: none;
  color: inherit;
  font-family: inherit;
  font-size: inherit;
  line-height: inherit;
}

/* Multiline variant */
.copy-field-value.multiline {
  white-space: pre-wrap;
  word-break: break-word;
  max-height: 200px;
  overflow-y: auto;
}

/* Code format */
.copy-field-value {
  font-size: 0.8em;
}

/* Hover state - primary soft background */
.input-wrapper:not(.disabled):hover {
  background: var(--ty-bg-primary-soft);
  transition: background 0.2s ease;
}

/* Copy button */
.copy-button {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  cursor: pointer;
  border: none;
  background: transparent;
  color: var(--ty-text-);
  transition: color 0.2s ease, transform 0.2s ease;
  padding: 0;
  margin: 0;
}

.copy-button:hover:not(.disabled) {
  color: var(--ty-text);
  transform: scale(1.1);
}

.copy-button.success {
  color: var(--ty-color-success);
  animation: copy-success 0.3s ease;
}

@keyframes copy-success {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.2); }
}

.copy-button svg {
  width: 18px;
  height: 18px;
}

.input-wrapper.disabled .copy-button {
  cursor: not-allowed;
  opacity: 0.5;
}

.input-wrapper.disabled {
  cursor: not-allowed;
}
`
