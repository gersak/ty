import React, { useEffect, useRef, useCallback } from 'react';

// Option data structure for data-driven approach
export interface OptionData {
  value: string;
  text: string;
  disabled?: boolean;
}

// Event detail structure for dropdown events
export interface TyDropdownEventDetail {
  option: HTMLElement;
}

// Type definitions for Ty Dropdown component
export interface TyDropdownProps extends Omit<React.HTMLAttributes<HTMLElement>, 'onChange'> {
  /** Semantic styling variant */
  flavor?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'neutral';
  
  /** Dropdown size */
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  
  /** Selected value */
  value?: string;
  
  /** Placeholder text */
  placeholder?: string;
  
  /** Dropdown label */
  label?: string;
  
  /** Disable the dropdown */
  disabled?: boolean;
  
  /** Make dropdown readonly */
  readonly?: boolean;
  
  /** Required field */
  required?: boolean;
  
  /** Enable search functionality */
  searchable?: boolean;
  
  /** Disable search functionality (ClojureScript: not-searchable) */
  notSearchable?: boolean;
  
  /** @deprecated Use notSearchable instead. External search handling */
  externalSearch?: boolean;
  
  /** Form field name for form submission */
  name?: string;
  
  // Data-driven approach
  options?: OptionData[];
  
  // React event handlers
  onChange?: (event: CustomEvent<TyDropdownEventDetail>) => void;
  onSearch?: (event: CustomEvent<{ query: string; element: HTMLElement }>) => void;
  
  // Children for slotted approach
  children?: React.ReactNode;
}

// React wrapper for ty-dropdown web component
export const TyDropdown = React.forwardRef<HTMLElement, TyDropdownProps>(
  ({ 
    options, 
    children, 
    onChange, 
    onSearch, 
    disabled, 
    notSearchable,
    externalSearch,
    name,
    ...props 
  }, ref) => {
    const elementRef = useRef<HTMLElement>(null);

    const handleChange = useCallback((event: CustomEvent<TyDropdownEventDetail>) => {
      if (onChange) {
        onChange(event);
      }
    }, [onChange]);

    const handleSearch = useCallback((event: CustomEvent<{ query: string; element: HTMLElement }>) => {
      if (onSearch) {
        onSearch(event);
      }
    }, [onSearch]);

    useEffect(() => {
      const element = elementRef.current;
      if (!element) return;

      // Listen for custom events from ty-dropdown
      if (onChange) {
        element.addEventListener('change', handleChange as EventListener);
      }
      
      if (onSearch) {
        element.addEventListener('ty-search', handleSearch as EventListener);
      }

      return () => {
        if (onChange) {
          element.removeEventListener('change', handleChange as EventListener);
        }
        if (onSearch) {
          element.removeEventListener('ty-search', handleSearch as EventListener);
        }
      };
    }, [handleChange, handleSearch, onChange, onSearch]);

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

    // Render options from data if provided, otherwise use children
    const renderContent = () => {
      if (options && options.length > 0) {
        // Data-driven approach - create ty-option elements
        return options.map((option, index) => 
          React.createElement(
            'ty-option',
            {
              key: option.value || index,
              value: option.value,
              ...(option.disabled && { disabled: "" }),
            },
            option.text
          )
        );
      }
      
      // Slotted approach - use provided children
      return children;
    };

    // Convert React props to web component attributes
    const webComponentProps: Record<string, any> = {
      ...props,
      ref: elementRef,
    };

    // Add conditional attributes
    if (disabled) webComponentProps.disabled = '';
    
    // Handle search functionality (prefer not-searchable over deprecated externalSearch)
    if (notSearchable) {
      webComponentProps['not-searchable'] = '';
    } else if (externalSearch) {
      // Support deprecated externalSearch for backward compatibility
      webComponentProps['not-searchable'] = '';
    }
    
    // Add string attributes
    if (name) webComponentProps.name = name;

    return React.createElement(
      'ty-dropdown',
      webComponentProps,
      renderContent()
    );
  }
);

TyDropdown.displayName = 'TyDropdown';
