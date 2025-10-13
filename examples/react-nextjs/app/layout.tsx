import './globals.css'
import type { Metadata } from 'next'
import { ThemeProvider } from '../lib/theme-provider'

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
      <head>
        {/* Load Ty components and CSS from local build */}
        <link rel="stylesheet" href="/ty/ty.css" />
        <script type="module" src="/ty/index.js"></script>
      </head>
      <body>
        <ThemeProvider>
          {children}
        </ThemeProvider>
      </body>
    </html>
  )
}
