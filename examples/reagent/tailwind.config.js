/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.cljs", "./public/**/*.html"],
  theme: {
    extend: {
      colors: {
        // Integrate Ty semantic colors with Tailwind
        primary: {
          50: 'rgb(var(--ty-color-primary-faint) / <alpha-value>)',
          100: 'rgb(var(--ty-color-primary-soft) / <alpha-value>)',
          500: 'rgb(var(--ty-color-primary) / <alpha-value>)',
          700: 'rgb(var(--ty-color-primary-mild) / <alpha-value>)',
          900: 'rgb(var(--ty-color-primary-strong) / <alpha-value>)',
        },
        success: {
          50: 'rgb(var(--ty-color-success-faint) / <alpha-value>)',
          500: 'rgb(var(--ty-color-success) / <alpha-value>)',
          900: 'rgb(var(--ty-color-success-strong) / <alpha-value>)',
        },
        danger: {
          50: 'rgb(var(--ty-color-danger-faint) / <alpha-value>)',
          500: 'rgb(var(--ty-color-danger) / <alpha-value>)',
          900: 'rgb(var(--ty-color-danger-strong) / <alpha-value>)',
        }
      }
    },
  },
  plugins: [],
}
