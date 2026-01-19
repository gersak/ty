import React, { useEffect, useRef, useCallback } from 'react';

// Event detail structure for ty-input events
export interface TyInputEventDetail {
  value: any; // shadow value (processed/parsed)
  formattedValue: string; // user-visible formatted value
  rawValue: string; // raw input value
  originalEvent: Event; // original DOM event
}

// Type definitions for Ty Input component
export interface TyInputProps extends Omit<React.HTMLAttributes<HTMLElement>, 'onChange' | 'onFocus' | 'onBlur'> {
  /** Input type */
  type?: 'text' | 'email' | 'password' | 'number' | 'tel' | 'url' | 'search'
  | 'currency' | 'percent' | 'compact';

  /** Semantic styling variant */
  flavor?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'neutral';

  /** Input size */
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';

  /** Input value */
  value?: string;

  /** Placeholder text */
  placeholder?: string;

  /** Input label */
  label?: string;

  /** Error message */
  error?: string;

  /** Disable the input */
  disabled?: boolean;

  /** Required field */
  required?: boolean;

  /** Form field name for form submission */
  name?: string;

  /** Checked state for checkbox inputs */
  checked?: boolean;

  // Numeric formatting props
  currency?: string;
  locale?: string;
  precision?: string | number;

  /** Debounce delay in milliseconds (0-5000) */
  delay?: number;

  // React event handlers - override with our custom types
  /**
   * Fires on every keystroke (React convention)
   * Maps to native 'input' event from ty-input
   */
  onChange?: (event: CustomEvent<TyInputEventDetail>) => void;

  /**
   * Fires on blur if value changed (native DOM behavior)
   * Maps to native 'change' event from ty-input
   */
  onChangeCommit?: (event: CustomEvent<TyInputEventDetail>) => void;

  /** Standard focus event */
  onFocus?: (event: FocusEvent) => void;

  /** Standard blur event */
  onBlur?: (event: FocusEvent) => void;
}

// React wrapper for ty-input web component
export const TyInput = React.forwardRef<HTMLElement, TyInputProps>(
  ({ onChange, onChangeCommit, onFocus, onBlur, disabled, name, checked, delay, ...props }, ref) => {
    const elementRef = useRef<HTMLElement>(null);

    // Map onChange to input event (React convention)
    const handleInput = useCallback((event: CustomEvent<TyInputEventDetail>) => {
      if (onChange) {
        onChange(event);
      }
    }, [onChange]);

    // Map onChangeCommit to change event (blur behavior)
    const handleChangeCommit = useCallback((event: CustomEvent<TyInputEventDetail>) => {
      if (onChangeCommit) {
        onChangeCommit(event);
      }
    }, [onChangeCommit]);

    const handleFocus = useCallback((event: FocusEvent) => {
      if (onFocus) {
        onFocus(event);
      }
    }, [onFocus]);

    const handleBlur = useCallback((event: FocusEvent) => {
      if (onBlur) {
        onBlur(event);
      }
    }, [onBlur]);

    useEffect(() => {
      const element = elementRef.current;
      if (!element) return;

      // Listen for custom input/change events from ty-input
      // Map onChange → input event (React convention)
      if (onChange) {
        element.addEventListener('input', handleInput as EventListener);
      }

      // Map onChangeCommit → change event (blur behavior)
      if (onChangeCommit) {
        element.addEventListener('change', handleChangeCommit as EventListener);
      }

      // Listen for standard focus/blur events
      if (onFocus) {
        element.addEventListener('focus', handleFocus as EventListener);
      }

      if (onBlur) {
        element.addEventListener('blur', handleBlur as EventListener);
      }

      return () => {
        if (onChange) {
          element.removeEventListener('input', handleInput as EventListener);
        }
        if (onChangeCommit) {
          element.removeEventListener('change', handleChangeCommit as EventListener);
        }
        if (onFocus) {
          element.removeEventListener('focus', handleFocus as EventListener);
        }
        if (onBlur) {
          element.removeEventListener('blur', handleBlur as EventListener);
        }
      };
    }, [handleInput, handleChangeCommit, handleFocus, handleBlur, onChange, onChangeCommit, onFocus, onBlur]);

    // Handle ref forwarding
    useEffect(() => {
      if (ref && elementRef.current) {
        if (typeof ref === 'function') {
          ref(elementRef.current);
        } else {
          ref.current = elementRef.current;
        }
      }
    }, [ref]);

    // Convert React props to web component attributes
    const webComponentProps: Record<string, any> = {
      ...props,
      ref: elementRef,
    };

    // Add conditional attributes
    if (disabled) webComponentProps.disabled = '';
    if (checked) webComponentProps.checked = '';

    // Add string attributes
    if (name) webComponentProps.name = name;

    // Add delay attribute
    if (delay !== undefined) webComponentProps.delay = delay;

    return React.createElement(
      'ty-input',
      webComponentProps
    );
  }
);

TyInput.displayName = 'TyInput';
