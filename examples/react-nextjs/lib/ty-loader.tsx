'use client'

import { useEffect, useState } from 'react'

/**
 * Client-side Ty Components Loader
 * 
 * This component waits for Ty components to be available from the local build
 * that's loaded in the layout.tsx <head> section.
 * 
 * It handles React Strict Mode and provides loading states.
 */

// Global flag to prevent double checks in React Strict Mode
declare global {
  interface Window {
    __TY_LOADED__?: boolean;
    customElements?: CustomElementRegistry;
  }
}

export function TyLoader({ children }: { children: React.ReactNode }) {
  const [tyLoaded, setTyLoaded] = useState(false)
  const [tyError, setTyError] = useState<string | null>(null)

  useEffect(() => {
    // Skip if already verified as loaded
    if (typeof window !== 'undefined' && window.__TY_LOADED__) {
      setTyLoaded(true)
      return
    }

    // Check for Ty components
    if (typeof window !== 'undefined') {
      let attempts = 0
      const maxAttempts = 20
      
      const checkTyLoaded = () => {
        attempts++
        
        // Check if Ty components are registered
        // We check for ty-button as it's a core component that should always be available
        if (window.customElements && window.customElements.get('ty-button')) {
          window.__TY_LOADED__ = true
          setTyLoaded(true)
          console.log('✅ Ty components loaded successfully')
          return
        }
        
        if (attempts < maxAttempts) {
          // Retry with increasing delay
          const delay = Math.min(50 + attempts * 25, 300)
          setTimeout(checkTyLoaded, delay)
        } else {
          console.error('❌ Failed to load Ty components after', maxAttempts, 'attempts')
          setTyError('Ty components failed to load. Please check the console for errors.')
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
        <h3>⚠️ Ty Components Load Error</h3>
        <p>{tyError}</p>
        <details style={{ marginTop: '10px' }}>
          <summary>Troubleshooting</summary>
          <ul style={{ marginTop: '8px', paddingLeft: '20px' }}>
            <li>Check if /ty/ty.js is accessible at /public/ty/ty.js</li>
            <li>Look for console errors in browser dev tools</li>
            <li>Verify the script is loaded in layout.tsx</li>
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
        <p>🔄 Loading Ty components...</p>
        <small style={{ color: '#999' }}>This should only take a moment</small>
      </div>
    )
  }

  // Render children once Ty is loaded
  return <>{children}</>
}
