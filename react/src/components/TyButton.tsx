import React, { useEffect, useRef, useCallback } from 'react';

// Type definitions for Ty Button component
export interface TyButtonProps extends React.HTMLAttributes<HTMLElement> {
  flavor?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'neutral';
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  type?: 'button' | 'submit' | 'reset';
  disabled?: boolean;
  children?: React.ReactNode;
}

// React wrapper for ty-button web component
export const TyButton = React.forwardRef<HTMLElement, TyButtonProps>(
  ({ children, onClick, type, disabled,  ...props }, ref) => {
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

    return React.createElement(
      'ty-button',
      {
        ...props,
        ...(disabled && { disabled: "" }),
        type, // Make sure type is passed to web component
        ref: elementRef,
      },
      children
    );
  }
);

TyButton.displayName = 'TyButton';
