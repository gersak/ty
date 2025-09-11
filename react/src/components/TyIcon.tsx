import React, { useEffect, useRef } from 'react';

// Type definitions for Ty Icon component
export interface TyIconProps extends React.HTMLAttributes<HTMLElement> {
  /** Icon name from the icon registry (e.g., 'home', 'star', 'settings') */
  name: string;
  
  /** Icon size - relative (em-based) or absolute (pixel-based) */
  size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl' | '2xl' | '16' | '20' | '24' | '32' | '48';
  
  /** Enable spinning animation */
  spin?: boolean;
  
  /** Enable pulse animation */
  pulse?: boolean;
  
  /** Animation tempo/speed */
  tempo?: 'slow' | 'fast';
  
  /** Additional CSS classes */
  className?: string;
}

// React wrapper for ty-icon web component
export const TyIcon = React.forwardRef<HTMLElement, TyIconProps>(
  ({ name, size, spin, pulse, tempo, className, ...props }, ref) => {
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
      name,
      ref: elementRef,
    };

    // Add optional attributes only if they have values
    if (size) {
      webComponentProps.size = size;
    }

    if (spin) {
      webComponentProps.spin = '';  // Boolean attributes as empty string
    }

    if (pulse) {
      webComponentProps.pulse = '';  // Boolean attributes as empty string
    }

    if (tempo) {
      webComponentProps.tempo = tempo;
    }

    if (className) {
      webComponentProps.class = className;  // HTML attribute is 'class', not 'className'
    }

    return React.createElement('ty-icon', webComponentProps);
  }
);

TyIcon.displayName = 'TyIcon';
