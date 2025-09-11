import './globals.css'
import type { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Ty Components + React + Next.js Example',
  description: 'Testing @gersak/ty and @gersak/ty-react packages',
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
        <script src="https://cdn.jsdelivr.net/npm/@gersak/ty@0.1.6/ty-lazy.js"></script>

        {/* Load Ty CSS styles */}
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@0.1.6/css/ty.css" />

        {/* Load icons after Ty components are available */}
        <script src="/icons.js"></script>
      </head>
      <body>
        {children}
      </body>
    </html>
  )
}
