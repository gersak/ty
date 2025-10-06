#!/usr/bin/env node

/**
 * Bundle TypeScript components using esbuild
 */

const esbuild = require('esbuild')
const fs = require('fs')
const path = require('path')

async function bundle() {
  const distDir = path.join(__dirname, '..', 'dist-ts')
  
  // Ensure dist directory exists
  if (!fs.existsSync(distDir)) {
    fs.mkdirSync(distDir, { recursive: true })
  }

  console.log('📦 Bundling TypeScript components...')

  try {
    // Bundle main entry point
    await esbuild.build({
      entryPoints: ['ts/index.ts'],
      bundle: true,
      outfile: 'dist-ts/index.js',
      format: 'esm',
      platform: 'browser',
      target: 'es2020',
      sourcemap: true,
      minify: true,
      treeShaking: true,
    })

    // Bundle individual components for tree-shaking
    await esbuild.build({
      entryPoints: ['ts/components/button.ts'],
      bundle: true,
      outfile: 'dist-ts/components/button.js',
      format: 'esm',
      platform: 'browser',
      target: 'es2020',
      sourcemap: true,
      minify: true,
      treeShaking: true,
    })

    console.log('✅ Bundling complete!')
    
    // Print bundle sizes
    const stats = fs.statSync('dist-ts/index.js')
    console.log(`📊 Main bundle: ${(stats.size / 1024).toFixed(2)} KB`)
    
    const buttonStats = fs.statSync('dist-ts/components/button.js')
    console.log(`📊 Button component: ${(buttonStats.size / 1024).toFixed(2)} KB`)
    
  } catch (error) {
    console.error('❌ Bundling failed:', error)
    process.exit(1)
  }
}

bundle()
