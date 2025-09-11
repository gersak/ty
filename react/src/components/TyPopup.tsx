import React, { useEffect, useRef } from 'react';

// Type definitions for Ty Popup component
export interface TyPopupProps extends React.HTMLAttributes<HTMLElement> {
  /** Whether the popup is open/visible */
  open?: boolean;
  
  /** Preferred placement of the popup relative to anchor: "top" | "bottom" | "left" | "right" */
  placement?: 'top' | 'bottom' | 'left' | 'right';
  
  /** Distance offset from the anchor in pixels */
  offset?: number;
  
  /** Whether to flip placement when there's not enough space */
  flip?: boolean;
  
  /** Anchor element - pass as children with slot="anchor" */
  children?: React.ReactNode;
}

// React wrapper for ty-popup web component
export const TyPopup = React.forwardRef<HTMLElement, TyPopupProps>(
  ({ 
    open,
    placement,
    offset,
    flip,
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
    if (open) {
      webComponentProps.open = '';  // Boolean attributes as empty string
    }

    if (placement) {
      webComponentProps.placement = placement;
    }

    if (offset !== undefined) {
      webComponentProps.offset = offset.toString();
    }

    if (flip) {
      webComponentProps.flip = '';  // Boolean attributes as empty string
    }

    // Process children to handle slot assignments
    const processedChildren = React.Children.map(children, (child) => {
      if (React.isValidElement(child)) {
        // If child has slot="anchor", keep it as is
        if (child.props.slot === 'anchor') {
          return child;
        }
        // Otherwise, it's popup content (default slot)
        return child;
      }
      return child;
    });

    return React.createElement('ty-popup', webComponentProps, processedChildren);
  }
);

TyPopup.displayName = 'TyPopup';
