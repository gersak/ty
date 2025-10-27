import React, { useEffect, useRef, useCallback } from 'react';

// Type definitions for Ty Multiselect component
export interface TyMultiselectEventDetail {
  /** Array of currently selected values */
  values: string[];
  /** Action that triggered the change: "add" | "remove" */
  action: 'add' | 'remove';
  /** The specific item that was added or removed */
  item: string;
}

export interface TyMultiselectProps extends Omit<React.HTMLAttributes<HTMLElement>, 'onChange'> {
  /** Current selected values as comma-separated string or array */
  value?: string | string[];
  
  /** Placeholder text when no items are selected */
  placeholder?: string;
  
  /** Disable the multiselect component */
  disabled?: boolean;
  
  /** Make the multiselect read-only */
  readonly?: boolean;
  
  /** Semantic styling variant */
  flavor?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'neutral';
  
  /** Label text for the multiselect */
  label?: string;
  
  /** Mark the field as required */
  required?: boolean;
  
  /** Enable search functionality */
  searchable?: boolean;
  
  /** Disable search (alias for searchable={false}) */
  notSearchable?: boolean;
  
  /** Debounce delay in milliseconds (0-5000) */
  delay?: number;
  
  /** Mobile section label for selected items */
  selectedLabel?: string;
  
  /** Form field name for form submission */
  name?: string;
  
  /** Callback when selection changes */
  onChange?: (event: CustomEvent<TyMultiselectEventDetail>) => void;
  
  /** Children should be TyTag components, not TyOption */
  children?: React.ReactNode;
}

// React wrapper for ty-multiselect web component
export const TyMultiselect = React.forwardRef<HTMLElement, TyMultiselectProps>(
  ({ 
    value,
    placeholder,
    disabled,
    readonly,
    flavor,
    label,
    required,
    searchable,
    notSearchable,
    delay,
    selectedLabel,
    name,
    onChange,
    children,
    ...props
  }, ref) => {
    const elementRef = useRef<HTMLElement>(null);

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

    // Handle change events
    const handleChange = useCallback((event: Event) => {
      const customEvent = event as CustomEvent<TyMultiselectEventDetail>;
      if (onChange) {
        onChange(customEvent);
      }
    }, [onChange]);

    // Set up event listeners
    useEffect(() => {
      const element = elementRef.current;
      if (element && onChange) {
        element.addEventListener('change', handleChange);
        return () => {
          element.removeEventListener('change', handleChange);
        };
      }
    }, [handleChange, onChange]);

    // Convert React props to web component attributes
    const webComponentProps: Record<string, any> = {
      ...props,
      ref: elementRef,
    };

    // Handle value conversion (array to comma-separated string)
    if (value !== undefined) {
      const valueString = Array.isArray(value) ? value.join(',') : value;
      webComponentProps.value = valueString;
    }

    // Add optional attributes only if they have values
    if (placeholder) {
      webComponentProps.placeholder = placeholder;
    }

    if (disabled) {
      webComponentProps.disabled = '';  // Boolean attributes as empty string
    }

    if (readonly) {
      webComponentProps.readonly = '';  // Boolean attributes as empty string
    }

    if (flavor) {
      webComponentProps.flavor = flavor;
    }

    if (label) {
      webComponentProps.label = label;
    }

    if (required) {
      webComponentProps.required = '';  // Boolean attributes as empty string
    }

    if (name) {
      webComponentProps.name = name;
    }
    
    // Handle searchable functionality
    if (searchable !== undefined) {
      if (searchable) {
        webComponentProps.searchable = '';
      } else {
        webComponentProps['not-searchable'] = '';
      }
    }
    
    if (notSearchable) {
      webComponentProps['not-searchable'] = '';
    }
    
    // Add delay attribute
    if (delay !== undefined) {
      webComponentProps.delay = delay;
    }
    
    // Add selectedLabel attribute
    if (selectedLabel) {
      webComponentProps['selected-label'] = selectedLabel;
    }

    return React.createElement(
      'ty-multiselect',
      webComponentProps,
      children
    );
  }
);

TyMultiselect.displayName = 'TyMultiselect';