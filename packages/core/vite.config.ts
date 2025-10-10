import { defineConfig } from 'vite'
import { resolve } from 'path'
import dts from 'vite-plugin-dts'

export default defineConfig({
  plugins: [
    dts({
      include: ['src/**/*'],
      outDir: 'dist',
      rollupTypes: true,
    })
  ],
  
  build: {
    // Generate sourcemaps (disable for smallest size)
    sourcemap: true,
    
    lib: {
      entry: {
        // Main entry point
        index: resolve(__dirname, 'src/index.ts'),
        
        // Individual component entries for tree-shaking
        'components/button': resolve(__dirname, 'src/components/button.ts'),
        'components/modal': resolve(__dirname, 'src/components/modal.ts'),
        'components/input': resolve(__dirname, 'src/components/input.ts'),
        'components/dropdown': resolve(__dirname, 'src/components/dropdown.ts'),
        'components/checkbox': resolve(__dirname, 'src/components/checkbox.ts'),
        'components/textarea': resolve(__dirname, 'src/components/textarea.ts'),
        'components/popup': resolve(__dirname, 'src/components/popup.ts'),
        'components/tooltip': resolve(__dirname, 'src/components/tooltip.ts'),
        'components/tag': resolve(__dirname, 'src/components/tag.ts'),
        'components/option': resolve(__dirname, 'src/components/option.ts'),
        'components/icon': resolve(__dirname, 'src/components/icon.ts'),
        'components/tabs': resolve(__dirname, 'src/components/tabs.ts'),
        'components/tab': resolve(__dirname, 'src/components/tab.ts'),
      },
      formats: ['es'],
      fileName: (format, entryName) => `${entryName}.js`,
    },
    
    rollupOptions: {
      // Externalize dependencies
      external: [],
      
      output: {
        // Preserve module structure
        preserveModules: false,
        
        // Use named exports
        exports: 'named',
        
        // Advanced mangling for smaller output
        compact: true,
        
        // More aggressive tree-shaking
        generatedCode: {
          constBindings: true,
          objectShorthand: true,
        },
      },
      
      // Tree-shaking configuration
      treeshake: {
        moduleSideEffects: false,
        propertyReadSideEffects: false,
        tryCatchDeoptimization: false,
      },
    },
    
    // Target modern browsers (allows more optimizations)
    target: 'es2020',
    
    // Output directory
    outDir: 'dist',
    
    // Empty output dir before build
    emptyOutDir: true,
    
    // Use Terser for better compression (slower build, smaller output)
    // Options: 'esbuild' (fast), 'terser' (smaller)
    minify: 'terser',
    
    // Terser options for maximum compression
    terserOptions: {
      compress: {
        // Remove console statements in production
        drop_console: true,
        drop_debugger: true,
        
        // More aggressive optimizations
        passes: 2,
        pure_funcs: ['console.log', 'console.info', 'console.debug', 'console.trace'],
        pure_getters: true,
        unsafe: false,
        unsafe_comps: false,
        unsafe_math: false,
        unsafe_methods: false,
      },
      mangle: {
        // Mangle property names for smaller output
        properties: {
          // Don't mangle properties starting with _
          regex: /^_/,
        },
      },
      format: {
        // Remove comments
        comments: false,
        
        // Shorter output
        ecma: 2020,
      },
    },
    
    // Chunk size warnings
    chunkSizeWarningLimit: 100,
  },
  
  // Development server configuration
  server: {
    port: 3000,
    open: '/dev/index.html',
  },
  
  // Resolve configuration
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
    },
  },
})
