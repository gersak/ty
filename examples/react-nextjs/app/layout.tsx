import './globals.css'
import type { Metadata } from 'next'
import { ThemeProvider } from '../lib/theme-provider'

export const metadata: Metadata = {
  title: 'Ty Components + React + Next.js Showcase',
  description: 'Comprehensive showcase of @gersak/ty and @gersak/ty-react packages with theme toggling, routing, and component examples',
}



// <script src="/ty.js"></script>
// <script src="/ty.css"></script>
//
// <script src="https://cdn.jsdelivr.net/npm/@gersak/ty@0.1.8/ty-lazy.js"></script>
// <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@0.1.8/css/ty.css" />


export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <head>
        {/* Load Ty components and CSS - use local version for consistency */}
        <link rel="stylesheet" href="/ty.css" />
        <script src="https://cdn.jsdelivr.net/npm/@gersak/ty@0.1.8/ty-lazy.js"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@0.1.8/css/ty.css" />
      </head>
      <body>
        <ThemeProvider>
          {children}
        </ThemeProvider>
      </body>
    </html>
  )
}
