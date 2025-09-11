'use client'

import { useState, useEffect } from 'react'
import { AppLayout } from '../../lib/app-layout'
import {
  TyButton,
  TyIcon,
} from '@gersak/ty-react'

export default function InstallationPage() {
  const [mounted, setMounted] = useState(false)

  useEffect(() => {
    setMounted(true)
  }, [])

  if (!mounted) return <div>Loading...</div>

  return (
    <AppLayout>
      <div className="page-header">
        <h1 className="page-title">Installation & Setup</h1>
        <p className="page-description">
          Get started with Ty Components in your React or Next.js project in minutes.
        </p>
      </div>

      <div className="component-section">
        <div className="section-header">
          <h2 className="section-title">📦 Package Installation</h2>
          <p className="section-description">
            Install the core packages to get started with Ty Components
          </p>
        </div>

        <div className="component-demo">
          <h3 style={{ marginBottom: '16px', color: 'var(--ty-color-neutral-strong)' }}>
            npm / yarn / pnpm
          </h3>
          
          <div style={{ display: 'grid', gap: '16px' }}>
            <div className="code-block">
              <code>npm install @gersak/ty</code>
            </div>

            <div className="code-block">
              <code>npm install @gersak/ty-react</code>
            </div>

            <div className="code-block">
              <code>npm install @gersak/ty @gersak/ty-react</code>
            </div>
          </div>
        </div>
      </div>

      <div className="component-section">
        <div className="section-header">
          <h2 className="section-title">🎯 Quick Start Checklist</h2>
          <p className="section-description">
            Verify your installation is working correctly
          </p>
        </div>

        <div className="component-demo">
          <div style={{ display: 'grid', gap: '12px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px', padding: '12px', backgroundColor: 'var(--ty-bg-success-soft)', borderRadius: '6px' }}>
              <TyIcon name="check-circle" size="20" style={{ color: 'var(--ty-color-success)' }} />
              <span>Install packages via npm/yarn/pnpm</span>
            </div>
            
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px', padding: '12px', backgroundColor: 'var(--ty-bg-info-soft)', borderRadius: '6px' }}>
              <TyIcon name="download" size="20" style={{ color: 'var(--ty-color-info)' }} />
              <span>Import CSS styles in your project</span>
            </div>
            
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px', padding: '12px', backgroundColor: 'var(--ty-bg-warning-soft)', borderRadius: '6px' }}>
              <TyIcon name="star" size="20" style={{ color: 'var(--ty-color-warning)' }} />
              <span>Register icons (optional but recommended)</span>
            </div>
            
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px', padding: '12px', backgroundColor: 'var(--ty-bg-primary-soft)', borderRadius: '6px' }}>
              <TyIcon name="code" size="20" style={{ color: 'var(--ty-color-primary)' }} />
              <span>Import and use React components</span>
            </div>
          </div>
        </div>
      </div>
    </AppLayout>
  )
}
