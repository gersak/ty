import React, { useEffect, useRef, useImperativeHandle } from 'react';

// Type definitions for Ty ScrollContainer component
export interface TyScrollContainerProps extends React.HTMLAttributes<HTMLElement> {
  /** Maximum height of the scroll container */
  maxHeight?: string;

  /** Enable/disable scroll shadows (default: true) */
  shadow?: boolean;

  /** Hide native scrollbar */
  hideScrollbar?: boolean;

  /** Content to scroll */
  children?: React.ReactNode;
}

// Ref interface for imperative methods
export interface TyScrollContainerRef {
  /** Force update shadows (useful after dynamic content changes) */
  updateShadows: () => void;
  /** Scroll to top */
  scrollToTop: (smooth?: boolean) => void;
  /** Scroll to bottom */
  scrollToBottom: (smooth?: boolean) => void;
  /** Get the underlying scroll element */
  scrollElement: HTMLElement | null;
  /** Get the native element */
  element: HTMLElement | null;
}

// React wrapper for ty-scroll-container web component
export const TyScrollContainer = React.forwardRef<TyScrollContainerRef, TyScrollContainerProps>(
  ({
    children,
    maxHeight,
    shadow,
    hideScrollbar,
    ...props
  }, ref) => {
    const elementRef = useRef<HTMLElement>(null);

    // Expose imperative methods via ref
    useImperativeHandle(ref, () => ({
      updateShadows: () => {
        const el = elementRef.current as any;
        el?.updateShadows?.();
      },
      scrollToTop: (smooth = true) => {
        const el = elementRef.current as any;
        el?.scrollToTop?.(smooth);
      },
      scrollToBottom: (smooth = true) => {
        const el = elementRef.current as any;
        el?.scrollToBottom?.(smooth);
      },
      get scrollElement() {
        const el = elementRef.current as any;
        return el?.scrollElement ?? null;
      },
      get element() {
        return elementRef.current;
      }
    }), []);

    // Convert React props to web component attributes
    const webComponentProps: Record<string, any> = {
      ...props,
      ref: elementRef,
    };

    // Add string attributes
    if (maxHeight) webComponentProps['max-height'] = maxHeight;

    // Add boolean attributes
    if (shadow === false) webComponentProps.shadow = 'false';
    if (hideScrollbar) webComponentProps['hide-scrollbar'] = true;

    return React.createElement(
      'ty-scroll-container',
      webComponentProps,
      children
    );
  }
);

TyScrollContainer.displayName = 'TyScrollContainer';
