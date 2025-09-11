import React, { useEffect, useRef } from 'react';

// Type definitions for Ty Option component
export interface TyOptionProps extends React.HTMLAttributes<HTMLElement> {
  value?: string;
  disabled?: boolean;
  selected?: boolean;
  hidden?: boolean;
  children?: React.ReactNode;
}

// React wrapper for ty-option web component
export const TyOption = React.forwardRef<HTMLElement, TyOptionProps>(
  ({ children, ...props }, ref) => {
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

    return React.createElement(
      'ty-option',
      {
        ...props,
        ref: elementRef,
      },
      children
    );
  }
);

TyOption.displayName = 'TyOption';
