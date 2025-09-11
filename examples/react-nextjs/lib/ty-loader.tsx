'use client'

import { useEffect, useState } from 'react'

/**
 * Client-side Ty Components CDN Loader
 * 
 * This component waits for Ty components to be available from the CDN
 * that's loaded in the layout.tsx <head> section.
 * 
 * It handles React Strict Mode and provides loading states.
 */

// Global flag to prevent double checks in React Strict Mode
declare global {
  interface Window {
    __TY_CDN_LOADED__?: boolean;
    ty?: {
      icons?: {
        add: (icons: Record<string, string>) => void;
        registry?: Record<string, string>;
      };
    };
  }
}

export function TyLoader({ children }: { children: React.ReactNode }) {
  const [tyLoaded, setTyLoaded] = useState(false)
  const [tyError, setTyError] = useState<string | null>(null)

  useEffect(() => {
    // Skip if already verified as loaded
    if (typeof window !== 'undefined' && window.__TY_CDN_LOADED__) {
      setTyLoaded(true)
      return
    }

    // Check for Ty components from CDN
    if (typeof window !== 'undefined') {
      let attempts = 0
      const maxAttempts = 20
      
      const checkTyLoaded = () => {
        attempts++
        
        // Check if Ty components are available
        if (window.ty && window.ty.icons && typeof window.ty.icons.add === 'function') {
          window.__TY_CDN_LOADED__ = true
          setTyLoaded(true)
          console.log('✅ Ty components loaded from CDN successfully')
          return
        }
        
        if (attempts < maxAttempts) {
          // Retry with increasing delay
          const delay = Math.min(50 + attempts * 25, 300)
          setTimeout(checkTyLoaded, delay)
        } else {
          console.error('❌ Failed to load Ty components from CDN after', maxAttempts, 'attempts')
          setTyError('Ty components failed to load from CDN. Please check your internet connection.')
        }
      }
      
      // Start checking immediately
      checkTyLoaded()
    }
  }, [])

  // Show loading state during SSR
  if (typeof window === 'undefined') {
    return (
      <div style={{ 
        padding: '20px', 
        textAlign: 'center',
        color: '#666'
      }}>
        <p>🔄 Initializing Ty components...</p>
      </div>
    )
  }

  // Show error state
  if (tyError) {
    return (
      <div style={{ 
        padding: '20px', 
        border: '2px solid #ef4444', 
        background: '#fee2e2', 
        borderRadius: '8px',
        margin: '20px',
        color: '#dc2626'
      }}>
        <h3>⚠️ Ty Components CDN Load Error</h3>
        <p>{tyError}</p>
        <details style={{ marginTop: '10px' }}>
          <summary>Troubleshooting</summary>
          <ul style={{ marginTop: '8px', paddingLeft: '20px' }}>
            <li>Check if jsDelivr CDN is accessible</li>
            <li>Look for console errors in browser dev tools</li>
            <li>Verify the CDN URLs in layout.tsx are correct</li>
            <li>Try refreshing the page</li>
          </ul>
        </details>
      </div>
    )
  }

  // Show loading state
  if (!tyLoaded) {
    return (
      <div style={{ 
        padding: '20px', 
        textAlign: 'center',
        color: '#666'
      }}>
        <p>🔄 Loading Ty components from CDN...</p>
        <small style={{ color: '#999' }}>This should only take a moment</small>
      </div>
    )
  }

  // Render children once Ty is loaded
  return <>{children}</>
}
