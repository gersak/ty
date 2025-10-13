#!/usr/bin/env node
import { readFile, writeFile } from 'fs/promises';
import postcss from 'postcss';
import cssnano from 'cssnano';

const inputFile = process.argv[2] || 'dist/css/ty.css';
const outputFile = process.argv[3] || 'dist/css/ty.min.css';

async function minifyCSS() {
  try {
    const css = await readFile(inputFile, 'utf8');
    
    const result = await postcss([
      cssnano({
        preset: ['default', {
          discardComments: { removeAll: true },
          normalizeWhitespace: true,
          colormin: true,
          minifyFontValues: true,
          minifyGradients: true,
          reduceIdents: false, // Keep CSS variable names
          zindex: false // Don't modify z-index values
        }]
      })
    ]).process(css, { from: inputFile, to: outputFile });
    
    await writeFile(outputFile, result.css);
    
    const originalSize = (css.length / 1024).toFixed(2);
    const minifiedSize = (result.css.length / 1024).toFixed(2);
    const savings = (((css.length - result.css.length) / css.length) * 100).toFixed(1);
    
    console.log(`✓ CSS minified: ${originalSize}KB → ${minifiedSize}KB (${savings}% reduction)`);
  } catch (error) {
    console.error('Error minifying CSS:', error);
    process.exit(1);
  }
}

minifyCSS();
