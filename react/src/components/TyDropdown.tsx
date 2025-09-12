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
  flavor?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'neutral';
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  value?: string;
  placeholder?: string;
  label?: string;
  disabled?: boolean;
  readonly?: boolean;
  required?: boolean;
  searchable?: boolean;
  externalSearch?: boolean;
  
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
  ({ options, children, onChange, onSearch, disabled, ...props }, ref) => {
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

    return React.createElement(
      'ty-dropdown',
      {
        ...props,
        ...(disabled && { disabled: "" }),
        ref: elementRef,
      },
      renderContent()
    );
  }
);

TyDropdown.displayName = 'TyDropdown';
