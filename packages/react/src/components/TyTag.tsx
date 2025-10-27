import React, { useEffect, useRef, useCallback } from 'react';

// Type definitions for Ty Tag component
export interface TyTagProps extends React.HTMLAttributes<HTMLElement> {
  flavor?: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'neutral';
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl';
  notPill?: boolean;  // Component uses not-pill attribute
  clickable?: boolean;
  dismissible?: boolean;
  disabled?: boolean;
  selected?: boolean;
  value?: string;
  onTagClick?: (event: CustomEvent) => void;
  onTagDismiss?: (event: CustomEvent) => void;
  children?: React.ReactNode;
}

// React wrapper for ty-tag web component
export const TyTag = React.forwardRef<HTMLElement, TyTagProps>(
  ({ children, onTagClick, onTagDismiss, notPill, clickable, dismissible, disabled, selected, ...props }, ref) => {
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
        ...(notPill && { 'not-pill': "" }),  // Convert camelCase to kebab-case
        ...(clickable && { clickable: "" }),
        ...(dismissible && { dismissible: "" }),
        ...(disabled && { disabled: "" }),
        ...(selected && { selected: "" }),
        ref: elementRef,
      },
      children
    );
  }
);

TyTag.displayName = 'TyTag';