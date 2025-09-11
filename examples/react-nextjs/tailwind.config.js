/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './pages/**/*.{js,ts,jsx,tsx,mdx}',
    './components/**/*.{js,ts,jsx,tsx,mdx}',
    './app/**/*.{js,ts,jsx,tsx,mdx}',
    './lib/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  darkMode: ['class'],
  theme: {
    extend: {
      // Integrate Ty CSS variables as Tailwind colors using RGB fallback approach
      colors: {
        // Primary semantic color system
        primary: {
          50: '#eff6ff',   // Approximate ty-bg-primary-soft
          100: '#dbeafe',  // Approximate ty-bg-primary
          200: '#bfdbfe',  // Approximate ty-bg-primary-mild
          300: '#93c5fd',  // Approximate ty-color-primary-faint
          400: '#60a5fa',  // Approximate ty-color-primary-soft
          500: '#4367cd',  // ty-color-primary
          600: '#1c40a8',  // ty-color-primary-mild
          700: '#0034c7',  // ty-color-primary-strong
          800: '#1e40af',
          900: '#1e3a8a',
          950: '#172554',
          
          // Ty-specific CSS variable references
          'ty-strong': 'var(--ty-color-primary-strong)',
          'ty-mild': 'var(--ty-color-primary-mild)',
          'ty-base': 'var(--ty-color-primary)',
          'ty-soft': 'var(--ty-color-primary-soft)',
          'ty-faint': 'var(--ty-color-primary-faint)',
        },
        
        // Semantic colors using CSS variables
        success: {
          'ty-strong': 'var(--ty-color-success-strong)',
          'ty-mild': 'var(--ty-color-success-mild)',
          'ty-base': 'var(--ty-color-success)',
          'ty-soft': 'var(--ty-color-success-soft)',
          'ty-faint': 'var(--ty-color-success-faint)',
        },
        
        danger: {
          'ty-strong': 'var(--ty-color-danger-strong)',
          'ty-mild': 'var(--ty-color-danger-mild)',
          'ty-base': 'var(--ty-color-danger)',
          'ty-soft': 'var(--ty-color-danger-soft)',
          'ty-faint': 'var(--ty-color-danger-faint)',
        },
        
        warning: {
          'ty-strong': 'var(--ty-color-warning-strong)',
          'ty-mild': 'var(--ty-color-warning-mild)',
          'ty-base': 'var(--ty-color-warning)',
          'ty-soft': 'var(--ty-color-warning-soft)',
          'ty-faint': 'var(--ty-color-warning-faint)',
        },
        
        info: {
          'ty-strong': 'var(--ty-color-info-strong)',
          'ty-mild': 'var(--ty-color-info-mild)',
          'ty-base': 'var(--ty-color-info)',
          'ty-soft': 'var(--ty-color-info-soft)',
          'ty-faint': 'var(--ty-color-info-faint)',
        },
        
        neutral: {
          'ty-strong': 'var(--ty-color-neutral-strong)',
          'ty-mild': 'var(--ty-color-neutral-mild)',
          'ty-base': 'var(--ty-color-neutral)',
          'ty-soft': 'var(--ty-color-neutral-soft)',
          'ty-faint': 'var(--ty-color-neutral-faint)',
        },
        
        // Surface system colors
        surface: {
          canvas: 'var(--ty-surface-canvas)',
          content: 'var(--ty-surface-content)',
          elevated: 'var(--ty-surface-elevated)',
          floating: 'var(--ty-surface-floating)',
          input: 'var(--ty-surface-input)',
        },
        
        // Border colors
        border: {
          'ty-strong': 'var(--ty-border-strong)',
          'ty-mild': 'var(--ty-border-mild)',
          'ty-base': 'var(--ty-border)',
          'ty-soft': 'var(--ty-border-soft)',
          'ty-faint': 'var(--ty-border-faint)',
        },
      },
      
      // Typography using Ty's font system
      fontFamily: {
        'ty-sans': 'var(--ty-font-sans)',
        'ty-mono': 'var(--ty-font-mono)',
      },
      
      // Spacing using Ty's spacing scale (as CSS variables)
      spacing: {
        'ty-1': 'var(--ty-spacing-1)',
        'ty-2': 'var(--ty-spacing-2)',
        'ty-3': 'var(--ty-spacing-3)',
        'ty-4': 'var(--ty-spacing-4)',
        'ty-5': 'var(--ty-spacing-5)',
        'ty-6': 'var(--ty-spacing-6)',
        'ty-8': 'var(--ty-spacing-8)',
        'ty-10': 'var(--ty-spacing-10)',
        'ty-12': 'var(--ty-spacing-12)',
        'ty-16': 'var(--ty-spacing-16)',
        'ty-20': 'var(--ty-spacing-20)',
        'ty-24': 'var(--ty-spacing-24)',
      },
      
      // Border radius using Ty's radius scale
      borderRadius: {
        'ty-sm': 'var(--ty-radius-sm)',
        'ty-base': 'var(--ty-radius-base)',
        'ty-md': 'var(--ty-radius-md)',
        'ty-lg': 'var(--ty-radius-lg)',
        'ty-xl': 'var(--ty-radius-xl)',
        'ty-2xl': 'var(--ty-radius-2xl)',
        'ty-3xl': 'var(--ty-radius-3xl)',
      },
      
      // Box shadow using Ty's shadow scale
      boxShadow: {
        'ty-sm': 'var(--ty-shadow-sm)',
        'ty-base': 'var(--ty-shadow-base)',
        'ty-md': 'var(--ty-shadow-md)',
        'ty-lg': 'var(--ty-shadow-lg)',
        'ty-xl': 'var(--ty-shadow-xl)',
        'ty-2xl': 'var(--ty-shadow-2xl)',
      },
      
      // Z-index scale using Ty values
      zIndex: {
        dropdown: '1000',    // var(--ty-z-dropdown)
        sticky: '1020',      // var(--ty-z-sticky)
        fixed: '1030',       // var(--ty-z-fixed)
        'modal-backdrop': '1040',  // var(--ty-z-modal-backdrop)
        modal: '1050',       // var(--ty-z-modal)
        popover: '1060',     // var(--ty-z-popover)
        tooltip: '1070',     // var(--ty-z-tooltip)
      },
    },
  },
  plugins: [
    // Custom plugin for Ty utility classes
    function({ addUtilities, addComponents }) {
      
      // Add semantic component classes
      const components = {
        // Surface utility classes
        '.surface-canvas': {
          backgroundColor: 'var(--ty-surface-canvas)',
        },
        '.surface-content': {
          backgroundColor: 'var(--ty-surface-content)',
        },
        '.surface-elevated': {
          backgroundColor: 'var(--ty-surface-elevated)',
          boxShadow: 'var(--ty-shadow-base)',
        },
        '.surface-floating': {
          backgroundColor: 'var(--ty-surface-floating)',
          boxShadow: 'var(--ty-shadow-md)',
        },
        '.surface-input': {
          backgroundColor: 'var(--ty-surface-input)',
        },
      }
      
      const utilities = {
        // Text semantic utilities
        '.text-semantic-primary': {
          color: 'var(--ty-color-primary)',
        },
        '.text-semantic-success': {
          color: 'var(--ty-color-success)',
        },
        '.text-semantic-danger': {
          color: 'var(--ty-color-danger)',
        },
        '.text-semantic-warning': {
          color: 'var(--ty-color-warning)',
        },
        '.text-semantic-info': {
          color: 'var(--ty-color-info)',
        },
        '.text-semantic-neutral': {
          color: 'var(--ty-color-neutral)',
        },
        
        // Background semantic utilities
        '.bg-semantic-primary': {
          backgroundColor: 'var(--ty-bg-primary-soft)',
          color: 'var(--ty-color-primary-strong)',
        },
        '.bg-semantic-success': {
          backgroundColor: 'var(--ty-bg-success-soft)',
          color: 'var(--ty-color-success-strong)',
        },
        '.bg-semantic-danger': {
          backgroundColor: 'var(--ty-bg-danger-soft)',
          color: 'var(--ty-color-danger-strong)',
        },
        '.bg-semantic-warning': {
          backgroundColor: 'var(--ty-bg-warning-soft)',
          color: 'var(--ty-color-warning-strong)',
        },
        '.bg-semantic-info': {
          backgroundColor: 'var(--ty-bg-info-soft)',
          color: 'var(--ty-color-info-strong)',
        },
        '.bg-semantic-neutral': {
          backgroundColor: 'var(--ty-bg-neutral-soft)',
          color: 'var(--ty-color-neutral-strong)',
        },
        
        // Ty spacing utilities
        '.p-ty-4': { padding: 'var(--ty-spacing-4)' },
        '.p-ty-6': { padding: 'var(--ty-spacing-6)' },
        '.m-ty-4': { margin: 'var(--ty-spacing-4)' },
        '.m-ty-6': { margin: 'var(--ty-spacing-6)' },
        
        // Ty border utilities
        '.border-ty': { borderColor: 'var(--ty-border)' },
        '.border-ty-soft': { borderColor: 'var(--ty-border-soft)' },
        
        // Ty transition utilities
        '.transition-ty': { transition: 'var(--ty-transition-all)' },
        '.transition-ty-colors': { transition: 'var(--ty-transition-colors)' },
      }
      
      addComponents(components)
      addUtilities(utilities)
    }
  ],
}
