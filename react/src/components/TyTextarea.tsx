import React, { useEffect, useRef, useCallback } from 'react';

// Event detail structure for ty-textarea events
export interface TyTextareaEventDetail {
  value: string; // textarea value
  originalEvent: Event; // original DOM event
}

// Type definitions for Ty Textarea component
export interface TyTextareaProps extends Omit<React.HTMLAttributes<HTMLElement>, 'onChange' | 'onInput' | 'onFocus' | 'onBlur'> {
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  value?: string;
  placeholder?: string;
  label?: string;
  error?: string;
  disabled?: boolean;
  required?: boolean;
  name?: string; // Important for HTMX/form compatibility
  
  // Textarea-specific props
  rows?: string | number;
  cols?: string | number;
  resize?: 'none' | 'both' | 'horizontal' | 'vertical';
  'min-height'?: string; // e.g., '100px'
  'max-height'?: string; // e.g., '500px'
  
  // React event handlers - override with our custom types
  onInput?: (event: CustomEvent<TyTextareaEventDetail>) => void;
  onChange?: (event: CustomEvent<TyTextareaEventDetail>) => void;
  onFocus?: (event: FocusEvent) => void;
  onBlur?: (event: FocusEvent) => void;
}

// React wrapper for ty-textarea web component
export const TyTextarea = React.forwardRef<HTMLElement, TyTextareaProps>(
  ({ onInput, onChange, onFocus, onBlur, disabled, required, ...props }, ref) => {
    const elementRef = useRef<HTMLElement>(null);

    const handleInput = useCallback((event: CustomEvent<TyTextareaEventDetail>) => {
      if (onInput) {
        onInput(event);
      }
    }, [onInput]);

    const handleChange = useCallback((event: CustomEvent<TyTextareaEventDetail>) => {
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

      // Listen for custom input/change events from ty-textarea
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
      'ty-textarea',
      {
        ...props,
        ...(disabled && { disabled: "" }),
        ...(required && { required: "" }),
        ref: elementRef,
      }
    );
  }
);

TyTextarea.displayName = 'TyTextarea';
