import { defineConfig } from '@tailwindcss/cli'

export default defineConfig({
  content: [
    './public/**/*.html', 
    './src/**/*.cljs'
  ],
  theme: {
    extend: {}
  }
})
