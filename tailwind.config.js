/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: 'class',
  content: [
    "./dev/**/*.html",
    "./demo/**/*.{cljs,cljc,clj}",
  ],
  theme: {
    extend: {
      colors: {
        // Map Ty's semantic colors to Tailwind
        'ty-important': 'var(--ty-color-important)',
        'ty-positive': 'var(--ty-color-positive)',
        'ty-negative': 'var(--ty-color-negative)',
        'ty-exception': 'var(--ty-color-exception)',
        'ty-unique': 'var(--ty-color-unique)',
        'ty-neutral': 'var(--ty-color-neutral)',
      },
      backgroundColor: {
        'ty-surface': 'var(--ty-background)',
        'ty-surface-p1': 'var(--ty-background-p1)',
        'ty-surface-m1': 'var(--ty-background-m1)',
      },
      borderColor: {
        'ty-default': 'var(--ty-border)',
      },
      fontFamily: {
        'ty-sans': 'var(--ty-font-sans)',
        'ty-mono': 'var(--ty-font-mono)',
      },
    },
  },
  plugins: [],
}
