/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./templates/**/*.html",
    "./static/js/**/*.js"
  ],
  darkMode: 'class', // Matches Ty's dark theme implementation
  theme: {
    extend: {
      // Integrate with Ty's CSS variables for consistency
      colors: {
        // Map Tailwind semantic colors to Ty variables
        primary: {
          50: 'rgb(var(--ty-color-primary-faint) / <alpha-value>)',
          100: 'rgb(var(--ty-color-primary-soft) / <alpha-value>)',
          500: 'rgb(var(--ty-color-primary) / <alpha-value>)',
          600: 'rgb(var(--ty-color-primary-mild) / <alpha-value>)',
          900: 'rgb(var(--ty-color-primary-strong) / <alpha-value>)',
        },
        success: {
          50: 'rgb(var(--ty-color-success-faint) / <alpha-value>)',
          100: 'rgb(var(--ty-color-success-soft) / <alpha-value>)',
          500: 'rgb(var(--ty-color-success) / <alpha-value>)',
          600: 'rgb(var(--ty-color-success-mild) / <alpha-value>)',
          900: 'rgb(var(--ty-color-success-strong) / <alpha-value>)',
        },
        danger: {
          50: 'rgb(var(--ty-color-danger-faint) / <alpha-value>)',
          100: 'rgb(var(--ty-color-danger-soft) / <alpha-value>)',
          500: 'rgb(var(--ty-color-danger) / <alpha-value>)',
          600: 'rgb(var(--ty-color-danger-mild) / <alpha-value>)',
          900: 'rgb(var(--ty-color-danger-strong) / <alpha-value>)',
        },
        warning: {
          50: 'rgb(var(--ty-color-warning-faint) / <alpha-value>)',
          100: 'rgb(var(--ty-color-warning-soft) / <alpha-value>)',
          500: 'rgb(var(--ty-color-warning) / <alpha-value>)',
          600: 'rgb(var(--ty-color-warning-mild) / <alpha-value>)',
          900: 'rgb(var(--ty-color-warning-strong) / <alpha-value>)',
        },
        info: {
          50: 'rgb(var(--ty-color-info-faint) / <alpha-value>)',
          100: 'rgb(var(--ty-color-info-soft) / <alpha-value>)',
          500: 'rgb(var(--ty-color-info) / <alpha-value>)',
          600: 'rgb(var(--ty-color-info-mild) / <alpha-value>)',
          900: 'rgb(var(--ty-color-info-strong) / <alpha-value>)',
        },
        neutral: {
          50: 'rgb(var(--ty-color-neutral-faint) / <alpha-value>)',
          100: 'rgb(var(--ty-color-neutral-soft) / <alpha-value>)',
          500: 'rgb(var(--ty-color-neutral) / <alpha-value>)',
          600: 'rgb(var(--ty-color-neutral-mild) / <alpha-value>)',
          900: 'rgb(var(--ty-color-neutral-strong) / <alpha-value>)',
        }
      },
      // Add beautiful backdrop blur utilities
      backdropBlur: {
        xs: '2px',
      },
      // Custom animations
      animation: {
        'fade-in': 'fadeIn 0.3s ease-in-out',
        'slide-up': 'slideUp 0.3s ease-out',
        'scale-in': 'scaleIn 0.2s ease-out',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        slideUp: {
          '0%': { transform: 'translateY(10px)', opacity: '0' },
          '100%': { transform: 'translateY(0)', opacity: '1' },
        },
        scaleIn: {
          '0%': { transform: 'scale(0.9)', opacity: '0' },
          '100%': { transform: 'scale(1)', opacity: '1' },
        },
      },
      // Custom gradients
      backgroundImage: {
        'gradient-radial': 'radial-gradient(var(--tw-gradient-stops))',
        'gradient-conic': 'conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))',
        'hero-gradient': 'linear-gradient(135deg, rgb(var(--ty-color-primary-faint)) 0%, rgb(var(--ty-color-secondary-faint)) 100%)',
      },
      // Glass morphism utilities
      boxShadow: {
        'glass': '0 8px 32px 0 rgba(31, 38, 135, 0.37)',
        'glass-dark': '0 8px 32px 0 rgba(0, 0, 0, 0.5)',
      },
    },
  },
  plugins: [
    // Removed @tailwindcss/forms as it conflicts with Ty component styling
    // Custom plugin for glass morphism
    function({ addUtilities }) {
      const newUtilities = {
        '.glass': {
          background: 'rgba(255, 255, 255, 0.1)',
          'backdrop-filter': 'blur(10px)',
          '-webkit-backdrop-filter': 'blur(10px)',
          border: '1px solid rgba(255, 255, 255, 0.2)',
        },
        '.glass-dark': {
          background: 'rgba(0, 0, 0, 0.2)',
          'backdrop-filter': 'blur(10px)',
          '-webkit-backdrop-filter': 'blur(10px)',
          border: '1px solid rgba(255, 255, 255, 0.1)',
        }
      }
      addUtilities(newUtilities)
    }
  ],
}
