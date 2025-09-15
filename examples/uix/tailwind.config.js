module.exports = {
  content: ['./src/**/*.cljs', './public/index.html'],
  theme: {
    extend: {
      // Let TY handle colors, Tailwind handles everything else
    }
  },
  plugins: [] // Removed @tailwindcss/forms plugin to avoid conflicts with ty components
}
