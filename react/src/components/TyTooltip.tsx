import React, { useEffect, useRef } from 'react';

// Type definitions for Ty Tooltip component
export interface TyTooltipProps extends React.HTMLAttributes<HTMLElement> {
  /** Tooltip positioning relative to the parent element */
  placement?: 'top' | 'bottom' | 'left' | 'right';
  
  /** Distance in pixels from the anchor element (default: 8) */
  offset?: number;
  
  /** Delay in milliseconds before showing tooltip (default: 600) */
  delay?: number;
  
  /** Disable the tooltip */
  disabled?: boolean;
  
  /** Semantic styling variant */
  flavor?: 'dark' | 'light' | 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'neutral';
  
  /** Tooltip content */
  children?: React.ReactNode;
}

// React wrapper for ty-tooltip web component
export const TyTooltip = React.forwardRef<HTMLElement, TyTooltipProps>(
  ({ 
    placement, 
    offset, 
    delay, 
    disabled, 
    flavor, 
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

    // Convert React props to web component attributes
    const webComponentProps: Record<string, any> = {
      ...props,
      ref: elementRef,
    };

    // Add optional attributes only if they have values
    if (placement) {
      webComponentProps.placement = placement;
    }

    if (offset !== undefined) {
      webComponentProps.offset = offset.toString();
    }

    if (delay !== undefined) {
      webComponentProps.delay = delay.toString();
    }

    if (disabled) {
      webComponentProps.disabled = '';  // Boolean attributes as empty string
    }

    if (flavor) {
      webComponentProps.flavor = flavor;
    }

    return React.createElement(
      'ty-tooltip',
      webComponentProps,
      children
    );
  }
);

TyTooltip.displayName = 'TyTooltip';
