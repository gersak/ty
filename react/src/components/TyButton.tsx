import React, { useEffect, useRef } from 'react';

// Type definitions for Ty Button component
export interface TyButtonProps extends React.HTMLAttributes<HTMLElement> {
  flavor?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'neutral';
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  disabled?: boolean;
  children?: React.ReactNode;
}

// React wrapper for ty-button web component
export const TyButton = React.forwardRef<HTMLElement, TyButtonProps>(
  ({ children, onClick, ...props }, ref) => {
    const elementRef = useRef<HTMLElement>(null);

    useEffect(() => {
      const element = elementRef.current;
      if (!element || !onClick) return;

      const handleClick = (event: Event) => {
        onClick(event as any);
      };

      element.addEventListener('click', handleClick);
      
      return () => {
        element.removeEventListener('click', handleClick);
      };
    }, [onClick]);

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
        ref: elementRef,
      },
      children
    );
  }
);

TyButton.displayName = 'TyButton';
