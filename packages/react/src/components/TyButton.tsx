import React, { useEffect, useRef, useCallback } from 'react';

// Type definitions for Ty Button component
export interface TyButtonProps extends React.HTMLAttributes<HTMLElement> {
  /** Semantic styling variant */
  flavor?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'neutral';
  
  /** Button size */
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  
  /** Button type for form submission */
  type?: 'button' | 'submit' | 'reset';
  
  /** Disable the button */
  disabled?: boolean;
  
  /** Filled button style (solid background) */
  filled?: boolean;
  
  /** Outlined button style (border only) */
  outlined?: boolean;
  
  /** Accent styling emphasis */
  accent?: boolean;
  
  /** Pill-shaped button (rounded ends) */
  pill?: boolean;
  
  /** Action button style (prominent call-to-action) */
  action?: boolean;
  
  /** Plain button style (minimal styling) */
  plain?: boolean;
  
  /** Accessible label for screen readers */
  label?: string;
  
  /** Form field name for form submission */
  name?: string;
  
  /** Form field value for form submission */
  value?: string;
  
  /** Full-width button */
  wide?: boolean;
  
  /** Button content */
  children?: React.ReactNode;
}

// React wrapper for ty-button web component
export const TyButton = React.forwardRef<HTMLElement, TyButtonProps>(
  ({ 
    children, 
    onClick, 
    type, 
    disabled,
    filled,
    outlined,
    accent,
    pill,
    action,
    plain,
    wide,
    label,
    name,
    value,
    ...props
  }, ref) => {
    const elementRef = useRef<HTMLElement>(null);

    // Handle form submission for submit-type buttons
    const handleFormSubmission = useCallback((event: Event) => {
      const element = elementRef.current;
      if (!element || type !== 'submit') return;

      // Find the parent form
      const form = element.closest('form');
      if (!form) return;

      // Prevent the web component's native form submission
      event.preventDefault();
      event.stopPropagation();

      // Create a synthetic submit event that React can handle
      const syntheticEvent = new Event('submit', {
        bubbles: true,
        cancelable: true
      });

      // Dispatch it on the form, which should trigger React's onSubmit handler
      form.dispatchEvent(syntheticEvent);
    }, [type]);

    // Handle regular click events
    useEffect(() => {
      const element = elementRef.current;
      if (!element) return;

      const handleClick = (event: Event) => {
        // For submit buttons, handle form submission
        if (type === 'submit') {
          handleFormSubmission(event);
        }

        // Also call the onClick handler if provided
        if (onClick) {
          onClick(event as any);
        }
      };

      element.addEventListener('click', handleClick);

      return () => {
        element.removeEventListener('click', handleClick);
      };
    }, [onClick, type, handleFormSubmission]);

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

    // Add conditional attributes
    if (disabled) webComponentProps.disabled = '';
    if (filled) webComponentProps.filled = '';
    if (outlined) webComponentProps.outlined = '';
    if (accent) webComponentProps.accent = '';
    if (pill) webComponentProps.pill = '';
    if (action) webComponentProps.action = '';
    if (plain) webComponentProps.plain = '';
    if (wide) webComponentProps.wide = '';
    
    // Add string attributes
    if (type) webComponentProps.type = type;
    if (label) webComponentProps.label = label;
    if (name) webComponentProps.name = name;
    if (value) webComponentProps.value = value;

    return React.createElement(
      'ty-button',
      webComponentProps,
      children
    );
  }
);

TyButton.displayName = 'TyButton';