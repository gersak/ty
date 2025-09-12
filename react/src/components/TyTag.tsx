import React, { useEffect, useRef, useCallback } from 'react';

// Type definitions for Ty Tag component
export interface TyTagProps extends React.HTMLAttributes<HTMLElement> {
  flavor?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'neutral';
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  pill?: boolean;
  clickable?: boolean;
  dismissible?: boolean;
  disabled?: boolean;
  value?: string;
  onTagClick?: (event: CustomEvent) => void;
  onTagDismiss?: (event: CustomEvent) => void;
  children?: React.ReactNode;
}

// React wrapper for ty-tag web component
export const TyTag = React.forwardRef<HTMLElement, TyTagProps>(
  ({ children, onTagClick, onTagDismiss, pill, clickable, dismissible, disabled, ...props }, ref) => {
    const elementRef = useRef<HTMLElement>(null);

    const handleTagClick = useCallback((event: CustomEvent) => {
      if (onTagClick) {
        onTagClick(event);
      }
    }, [onTagClick]);

    const handleTagDismiss = useCallback((event: CustomEvent) => {
      if (onTagDismiss) {
        onTagDismiss(event);
      }
    }, [onTagDismiss]);

    useEffect(() => {
      const element = elementRef.current;
      if (!element) return;

      // Listen for custom events from the tag
      if (onTagClick) {
        element.addEventListener('ty-tag-click', handleTagClick as EventListener);
      }
      
      if (onTagDismiss) {
        element.addEventListener('ty-tag-dismiss', handleTagDismiss as EventListener);
      }

      return () => {
        if (onTagClick) {
          element.removeEventListener('ty-tag-click', handleTagClick as EventListener);
        }
        if (onTagDismiss) {
          element.removeEventListener('ty-tag-dismiss', handleTagDismiss as EventListener);
        }
      };
    }, [handleTagClick, handleTagDismiss, onTagClick, onTagDismiss]);

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
      'ty-tag',
      {
        ...props,
        ...(pill && { pill: "" }),
        ...(clickable && { clickable: "" }),
        ...(dismissible && { dismissible: "" }),
        ...(disabled && { disabled: "" }),
        ref: elementRef,
      },
      children
    );
  }
);

TyTag.displayName = 'TyTag';
