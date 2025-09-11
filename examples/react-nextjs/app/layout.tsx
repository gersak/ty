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
        {/* Load Ty components from jsDelivr CDN */}
        <script src="https://cdn.jsdelivr.net/npm/@gersak/ty@0.1.7/ty-lazy.js"></script>

        {/* Load Ty CSS styles */}
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@0.1.7/css/ty.css" />

        {/* Load icons after Ty components are available */}
        <script src="/icons.js"></script>
      </head>
      <body>
        <ThemeProvider>
          {children}
        </ThemeProvider>
      </body>
    </html>
  )
}
