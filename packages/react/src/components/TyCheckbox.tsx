import React, { useEffect, useRef } from 'react';

// Type definitions for Ty Checkbox component
export interface TyCheckboxProps extends React.HTMLAttributes<HTMLElement> {
  /** Checked state */
  checked?: boolean;
  
  /** Form field value when checked */
  value?: string;
  
  /** Form field name */
  name?: string;
  
  /** Disable the checkbox */
  disabled?: boolean;
  
  /** Required field */
  required?: boolean;
  
  /** Error message */
  error?: string;
  
  /** Checkbox size */
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  
  /** Semantic styling variant */
  flavor?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'neutral';
  
  /** Change event handler */
  onChange?: (event: CustomEvent<TyCheckboxEventDetail>) => void;
  
  /** Input event handler */
  onInput?: (event: CustomEvent<TyCheckboxEventDetail>) => void;
  
  /** Checkbox label content */
  children?: React.ReactNode;
}

export interface TyCheckboxEventDetail {
  value: boolean;
  checked: boolean;
  formValue: string | null;
  originalEvent: Event;
}

// React wrapper for ty-checkbox web component
export const TyCheckbox = React.forwardRef<HTMLElement, TyCheckboxProps>(
  ({ 
    children, 
    checked,
    value,
    name,
    disabled,
    required,
    error,
    size,
    flavor,
    onChange,
    onInput,
    ...props 
  }, ref) => {
    const elementRef = useRef<HTMLElement>(null);

    // Handle change events
    useEffect(() => {
      const element = elementRef.current;
      if (!element) return;

      const handleChange = (event: Event) => {
        if (onChange) {
          onChange(event as CustomEvent<TyCheckboxEventDetail>);
        }
      };

      const handleInput = (event: Event) => {
        if (onInput) {
          onInput(event as CustomEvent<TyCheckboxEventDetail>);
        }
      };

      element.addEventListener('change', handleChange);
      element.addEventListener('input', handleInput);

      return () => {
        element.removeEventListener('change', handleChange);
        element.removeEventListener('input', handleInput);
      };
    }, [onChange, onInput]);

    // Combine refs if needed
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

    // Add boolean attributes
    if (checked) webComponentProps.checked = '';
    if (disabled) webComponentProps.disabled = '';
    if (required) webComponentProps.required = '';
    
    // Add string attributes
    if (value) webComponentProps.value = value;
    if (name) webComponentProps.name = name;
    if (error) webComponentProps.error = error;
    if (size) webComponentProps.size = size;
    if (flavor) webComponentProps.flavor = flavor;

    return React.createElement(
      'ty-checkbox',
      webComponentProps,
      children
    );
  }
);

TyCheckbox.displayName = 'TyCheckbox';
