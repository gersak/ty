import { defineConfig } from 'vite'
import { resolve } from 'path'

/**
 * Development Build Configuration
 * 
 * Creates UNMINIFIED bundle for development and debugging.
 * 
 * SAME OUTPUT FILENAME as production (dist/ty.js)
 * - ClojureScript site always loads the same file
 * - Switch between dev/prod by running different build command
 * - Use with --watch for continuous rebuilds
 * 
 * Strategy:
 * 1. Keep ALL console.logs (debugging!)
 * 2. NO minification - readable code
 * 3. Source maps enabled
 * 4. Fast builds
 * 5. Same filename as production build
 * 
 * Usage:
 *   npm run build:dev              # Single build
 *   npm run build:dev:watch        # Watch mode (auto-rebuild)
 * 
 * Output: dist/ty.js (UNMINIFIED)
 */

export default defineConfig({
  plugins: [],
  
  build: {
    // SOURCE MAPS for debugging
    sourcemap: true,
    
    // Single bundle library mode
    lib: {
      entry: resolve(__dirname, 'src/cdn.ts'),
      name: 'Ty',
      formats: ['umd'],
      fileName: () => 'ty.js',  // Same filename as production!
    },
    
    rollupOptions: {
      external: [],
      
      output: {
        name: 'Ty',
        exports: 'named',
        inlineDynamicImports: true,
        
        // Readable code formatting
        compact: false,
        
        generatedCode: {
          constBindings: true,
          objectShorthand: true,
          arrowFunctions: true,
        },
      },
      
      // Keep tree-shaking but don't be aggressive
      treeshake: {
        moduleSideEffects: true,  // Keep side effects for debugging
        propertyReadSideEffects: true,
      },
    },
    
    target: 'es2020',
    
    // Output to dist/ (same as production)
    outDir: resolve(__dirname, 'dist'),
    
    // Don't empty outDir - preserve ty.css
    emptyOutDir: false,
    
    // NO MINIFICATION for development
    minify: false,
    
    chunkSizeWarningLimit: 1000, // Dev bundle is bigger, that's OK
  },
  
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
    },
  },
})
