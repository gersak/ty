import './globals.css'
// Bundler-only setup (TC6 sideEffects fix) — no script tag, no public/ty/ copy.
// @gersak/ty CSS imported here in the Server Component (safe on the server).
import '@gersak/ty/css/ty.css'
import type { Metadata } from 'next'
import { ThemeProvider } from '../lib/theme-provider'
import { TyRegistry } from '../components/TyRegistry'


export const metadata: Metadata = {
  title: 'Ty Components + React + Next.js Showcase',
  description: 'Comprehensive showcase of @gersak/ty and @gersak/ty-react packages with theme toggling, routing, and component examples',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body>
        <TyRegistry />
        <ThemeProvider>
          {children}
        </ThemeProvider>
      </body>
    </html>
  )
}
