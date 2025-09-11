'use client'

import { useState, useEffect } from 'react'
import Link from 'next/link'
import { usePathname } from 'next/navigation'
import { useTheme } from './theme-provider'
import {
  TyButton,
  TyIcon,
} from '@gersak/ty-react'

interface NavigationItem {
  name: string
  href: string
  icon: string
}

interface NavigationSection {
  title: string
  items: NavigationItem[]
}

const navigation: NavigationSection[] = [
  {
    title: 'Overview',
    items: [
      { name: 'Getting Started', href: '/', icon: 'home' },
      { name: 'Installation', href: '/installation', icon: 'download' },
      { name: 'Theme System', href: '/theme', icon: 'palette' },
    ]
  },
  {
    title: 'Core Components',
    items: [
      { name: 'Buttons', href: '/components/buttons', icon: 'click' },
      { name: 'Inputs & Forms', href: '/components/inputs', icon: 'edit' },
      { name: 'Dropdowns', href: '/components/dropdowns', icon: 'chevron-down' },
      { name: 'Modals', href: '/components/modals', icon: 'square' },
    ]
  },
  {
    title: 'Advanced Components',
    items: [
      { name: 'Calendar', href: '/components/calendar', icon: 'calendar' },
      { name: 'Icons', href: '/components/icons', icon: 'star' },
      { name: 'Navigation', href: '/components/navigation', icon: 'menu' },
      { name: 'Tooltips', href: '/components/tooltips', icon: 'info' },
    ]
  },
  {
    title: 'Examples',
    items: [
      { name: 'Contact Form', href: '/examples/contact-form', icon: 'mail' },
      { name: 'Data Dashboard', href: '/examples/dashboard', icon: 'monitor' },
      { name: 'User Profile', href: '/examples/profile', icon: 'user' },
      { name: 'Settings Panel', href: '/examples/settings', icon: 'settings' },
    ]
  },
  {
    title: 'Resources',
    items: [
      { name: 'Component API', href: '/api', icon: 'file' },
      { name: 'Design System', href: '/design-system', icon: 'palette' },
      { name: 'Best Practices', href: '/best-practices', icon: 'check-circle' },
    ]
  }
]

interface AppLayoutProps {
  children: React.ReactNode
}

export function AppLayout({ children }: AppLayoutProps) {
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false)
  const [sidebarOpen, setSidebarOpen] = useState(false)
  const [mounted, setMounted] = useState(false)
  const { theme, toggleTheme } = useTheme()
  const pathname = usePathname()

  useEffect(() => {
    setMounted(true)
  }, [])

  const toggleSidebar = () => {
    setSidebarCollapsed(!sidebarCollapsed)
  }

  const toggleMobileSidebar = () => {
    setSidebarOpen(!sidebarOpen)
  }

  const closeMobileSidebar = () => {
    setSidebarOpen(false)
  }

  if (!mounted) {
    return (
      <div className="flex items-center justify-center min-h-screen surface-canvas">
        <div className="text-center">
          <div className="inline-block w-8 h-8 border-4 border-current border-t-transparent rounded-full animate-spin text-primary-500 mb-4"></div>
          <p className="text-neutral-ty-base">Loading application...</p>
        </div>
      </div>
    )
  }

  return (
    <div className={`
      min-h-screen grid transition-all duration-300
      ${sidebarCollapsed ? 'grid-cols-[var(--app-sidebar-collapsed-width)_1fr]' : 'grid-cols-[var(--app-sidebar-width)_1fr]'}
      grid-rows-[var(--app-header-height)_1fr]
      ${sidebarOpen ? 'sidebar-open' : ''}
    `}
    style={{
      gridTemplateAreas: sidebarOpen 
        ? '"sidebar header" "sidebar main"' 
        : '"sidebar header" "sidebar main"'
    }}>
      
      {/* Mobile overlay */}
      <div 
        className={`
          md:hidden fixed inset-0 bg-black z-[15] transition-opacity duration-300
          ${sidebarOpen ? 'bg-opacity-50 opacity-100 pointer-events-auto' : 'bg-opacity-0 opacity-0 pointer-events-none'}
        `}
        onClick={closeMobileSidebar} 
      />

      {/* Header */}
      <header 
        className="flex items-center justify-between px-6 z-10 surface-elevated border-b border-ty-base"
        style={{ gridArea: 'header', minHeight: 'var(--app-header-height)' }}
      >
        <div className="flex items-center gap-4">
          <TyButton 
            flavor="ghost" 
            size="sm" 
            onClick={toggleMobileSidebar}
            className="md:hidden"
          >
            <TyIcon name="menu" size="20" />
          </TyButton>

          <TyButton 
            flavor="ghost" 
            size="sm" 
            onClick={toggleSidebar}
            className="hidden md:flex"
          >
            <TyIcon name={sidebarCollapsed ? 'chevron-right' : 'chevron-left'} size="20" />
          </TyButton>

          <Link 
            href="/" 
            className="text-2xl font-semibold no-underline transition-colors duration-150 text-primary-ty-base hover:text-primary-ty-mild"
          >
            Ty + Tailwind Showcase
          </Link>
        </div>

        <div className="flex items-center gap-3">
          <TyButton 
            flavor="ghost" 
            size="sm" 
            onClick={toggleTheme}
            title={`Switch to ${theme === 'light' ? 'dark' : 'light'} theme`}
            className="relative flex items-center gap-2"
          >
            <TyIcon 
              name={theme === 'light' ? 'sun' : 'moon'} 
              size="20" 
              className={`transition-transform duration-150 ${
                theme === 'light' ? 'text-warning-ty-base' : 'text-info-ty-base'
              }`}
            />
          </TyButton>

          <div className="inline-flex items-center gap-2 px-2 py-1 rounded-md text-xs font-medium bg-semantic-success">
            <TyIcon name="check-circle" size="12" />
            Components Loaded
          </div>
        </div>
      </header>

      {/* Sidebar */}
      <aside 
        className={`
          overflow-y-auto transition-all duration-300 relative surface-content border-r border-ty-base
          md:translate-x-0
          ${sidebarOpen ? 'translate-x-0' : '-translate-x-full'}
          md:relative md:z-auto
          fixed z-20 top-[var(--app-header-height)] bottom-0 left-0
        `}
        style={{ 
          gridArea: 'sidebar',
          width: 'var(--app-sidebar-width)'
        }}
      >
        <div className="p-4 flex items-center justify-between border-b border-ty-soft" 
             style={{ minHeight: 'var(--app-header-height)' }}>
          <div className={`
            text-lg font-semibold text-neutral-ty-strong transition-opacity duration-150
            ${sidebarCollapsed ? 'opacity-0 pointer-events-none' : 'opacity-100'}
          `}>
            Navigation
          </div>
        </div>

        <nav className="py-4">
          {navigation.map((section) => (
            <div key={section.title} className="mb-6">
              <div className={`
                px-4 py-2 text-xs font-semibold uppercase tracking-wider mb-2 text-neutral-ty-soft transition-opacity duration-150
                ${sidebarCollapsed ? 'opacity-0 pointer-events-none' : 'opacity-100'}
              `}>
                {section.title}
              </div>
              {section.items.map((item) => {
                const isActive = pathname === item.href
                return (
                  <Link 
                    key={item.href}
                    href={item.href} 
                    className={`
                      flex items-center gap-3 py-3 no-underline transition-all duration-150 border-l-3
                      ${sidebarCollapsed ? 'justify-center px-3' : 'px-4'}
                      ${isActive 
                        ? 'bg-primary-100 border-l-primary-500 text-primary-ty-strong' 
                        : 'border-l-transparent text-neutral-ty-base hover:bg-neutral-50 hover:text-neutral-ty-strong'
                      }
                    `}
                    onClick={closeMobileSidebar}
                  >
                    <TyIcon name={item.icon} className="flex-shrink-0 w-5 h-5" />
                    <span className={`
                      transition-opacity duration-150
                      ${sidebarCollapsed ? 'opacity-0 pointer-events-none' : 'opacity-100'}
                    `}>
                      {item.name}
                    </span>
                  </Link>
                )
              })}
            </div>
          ))}
        </nav>
      </aside>

      {/* Main Content */}
      <main 
        className="overflow-auto surface-canvas p-6"
        style={{ gridArea: 'main' }}
      >
        <div className="max-w-6xl mx-auto">
          {children}
        </div>
      </main>
    </div>
  )
}
