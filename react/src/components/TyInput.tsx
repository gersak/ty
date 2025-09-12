import React, { useEffect, useRef, useCallback } from 'react';

// Event detail structure for ty-input events
export interface TyInputEventDetail {
  value: any; // shadow value (processed/parsed)
  formattedValue: string; // user-visible formatted value
  rawValue: string; // raw input value
  originalEvent: Event; // original DOM event
}

// Type definitions for Ty Input component
export interface TyInputProps extends Omit<React.HTMLAttributes<HTMLElement>, 'onChange' | 'onInput' | 'onFocus' | 'onBlur'> {
  type?: 'text' | 'email' | 'password' | 'number' | 'tel' | 'url' | 'search';
  flavor?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'neutral';
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  value?: string;
  placeholder?: string;
  label?: string;
  error?: string;
  disabled?: boolean;
  required?: boolean;
  
  // Numeric formatting props
  currency?: string;
  locale?: string;
  precision?: string | number;
  
  // React event handlers - override with our custom types
  onInput?: (event: CustomEvent<TyInputEventDetail>) => void;
  onChange?: (event: CustomEvent<TyInputEventDetail>) => void;
  onFocus?: (event: FocusEvent) => void;
  onBlur?: (event: FocusEvent) => void;
}

// React wrapper for ty-input web component
export const TyInput = React.forwardRef<HTMLElement, TyInputProps>(
  ({ onInput, onChange, onFocus, onBlur, disabled, ...props }, ref) => {
    const elementRef = useRef<HTMLElement>(null);

    const handleInput = useCallback((event: CustomEvent<TyInputEventDetail>) => {
      if (onInput) {
        onInput(event);
      }
    }, [onInput]);

    const handleChange = useCallback((event: CustomEvent<TyInputEventDetail>) => {
      if (onChange) {
        onChange(event);
      }
    }, [onChange]);

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
      if (onInput) {
        element.addEventListener('input', handleInput as EventListener);
      }
      
      if (onChange) {
        element.addEventListener('change', handleChange as EventListener);
      }

      // Listen for standard focus/blur events
      if (onFocus) {
        element.addEventListener('focus', handleFocus as EventListener);
      }

      if (onBlur) {
        element.addEventListener('blur', handleBlur as EventListener);
      }

      return () => {
        if (onInput) {
          element.removeEventListener('input', handleInput as EventListener);
        }
        if (onChange) {
          element.removeEventListener('change', handleChange as EventListener);
        }
        if (onFocus) {
          element.removeEventListener('focus', handleFocus as EventListener);
        }
        if (onBlur) {
          element.removeEventListener('blur', handleBlur as EventListener);
        }
      };
    }, [handleInput, handleChange, handleFocus, handleBlur, onInput, onChange, onFocus, onBlur]);

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

    return React.createElement(
      'ty-input',
      {
        ...props,
        ...(disabled && { disabled: "" }),
        ref: elementRef,
      }
    );
  }
);

TyInput.displayName = 'TyInput';
