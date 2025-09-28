import React, { useEffect, useRef } from 'react';

// Type definitions for Ty Popup component
export interface TyPopupProps extends React.HTMLAttributes<HTMLElement> {
  /** Preferred placement of the popup relative to anchor parent: "top" | "bottom" | "left" | "right" */
  placement?: 'top' | 'bottom' | 'left' | 'right';
  
  /** Distance offset from the anchor in pixels (default: 8) */
  offset?: number;
  
  /** Disable automatic click trigger - requires manual open/close via ref methods */
  manual?: boolean;
  
  /** Disable automatic close on outside click and ESC key */
  disableClose?: boolean;
  
  /** Popup content - popup should be a child of the anchor element */
  children?: React.ReactNode;
}

// Event detail types for popup events
export interface TyPopupOpenEvent extends CustomEvent {
  type: 'ty:popup-open';
}

export interface TyPopupCloseEvent extends CustomEvent {
  type: 'ty:popup-close';
}

// Programmatic API for popup control
export interface TyPopupElement extends HTMLElement {
  openPopup(): void;
  closePopup(): void;
  togglePopup(): void;
}

// React wrapper for ty-popup web component
export const TyPopup = React.forwardRef<TyPopupElement, TyPopupProps>(
  ({ 
    placement, 
    offset, 
    manual, 
    disableClose,
    children, 
    ...props 
  }, ref) => {
    const elementRef = useRef<TyPopupElement>(null);

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

    if (manual) {
      webComponentProps.manual = '';  // Boolean attributes as empty string
    }

    if (disableClose) {
      webComponentProps['disable-close'] = '';  // Boolean attributes as empty string
    }

    return React.createElement(
      'ty-popup',
      webComponentProps,
      children
    );
  }
);

TyPopup.displayName = 'TyPopup';
