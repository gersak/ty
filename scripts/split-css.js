#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

/**
 * Split ty.css into variables and utilities for modular CDN loading
 */
function splitTyCss() {
  const cssPath = path.join(__dirname, '../css/ty.css');
  const outputDir = path.join(__dirname, '../css');
  
  if (!fs.existsSync(cssPath)) {
    console.error('❌ ty.css not found at:', cssPath);
    console.log('💡 Run npm run css:copy first');
    process.exit(1);
  }
  
  const cssContent = fs.readFileSync(cssPath, 'utf8');
  
  // Extract CSS variables section (starts with :root or [data-theme])
  const variableRegex = /:root\s*{[^}]+}|html\[data-theme[^}]+}|\[data-theme[^}]+}/gs;
  const variableMatches = cssContent.match(variableRegex) || [];
  
  // Extract utility classes (starts with .)
  const utilityRegex = /\.ty-[^{]+\{[^}]+\}/gs;
  const utilityMatches = cssContent.match(utilityRegex) || [];
  
  // Variables CSS
  const variablesCSS = `/* Ty Components - CSS Variables */
/* Auto-generated from ty.css - Do not edit directly */

${variableMatches.join('\n\n')}
`;

  // Utilities CSS  
  const utilitiesCSS = `/* Ty Components - Utility Classes */
/* Auto-generated from ty.css - Do not edit directly */

${utilityMatches.join('\n\n')}
`;

  // Core CSS (everything else)
  let coreCSS = cssContent;
  variableMatches.forEach(match => coreCSS = coreCSS.replace(match, ''));
  utilityMatches.forEach(match => coreCSS = coreCSS.replace(match, ''));
  
  coreCSS = `/* Ty Components - Core Styles */
/* Auto-generated from ty.css - Do not edit directly */

${coreCSS.trim()}
`;

  // Write split files
  fs.writeFileSync(path.join(outputDir, 'ty.variables.css'), variablesCSS);
  fs.writeFileSync(path.join(outputDir, 'ty.utilities.css'), utilitiesCSS);
  fs.writeFileSync(path.join(outputDir, 'ty.core.css'), coreCSS);
  
  console.log('✅ CSS files split successfully:');
  console.log(`   📁 css/ty.variables.css (${variablesCSS.length} bytes)`);
  console.log(`   📁 css/ty.utilities.css (${utilitiesCSS.length} bytes)`); 
  console.log(`   📁 css/ty.core.css (${coreCSS.length} bytes)`);
  console.log(`   📁 css/ty.css (${cssContent.length} bytes - full bundle)`);
}

// Create README for CSS folder
function createCssReadme() {
  const readmeContent = `# Ty Components CSS Files

This folder contains the CSS files for ty components, optimized for CDN delivery.

## Files:

### \`ty.css\` - Complete Bundle
The complete CSS bundle containing all variables, utilities, and core styles.
```html
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tyrell/components@latest/css/ty.css">
```

### \`ty.variables.css\` - CSS Variables Only
Just the CSS custom properties (variables) for semantic colors, spacing, typography, etc.
Useful if you want to define variables but use your own styling approach.
```html  
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tyrell/components@latest/css/ty.variables.css">
```

### \`ty.utilities.css\` - Utility Classes Only
Just the utility classes like \`.ty-text-primary\`, \`.ty-bg-elevated\`, etc.
Requires ty.variables.css to work properly.
```html
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tyrell/components@latest/css/ty.variables.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tyrell/components@latest/css/ty.utilities.css">
```

### \`ty.core.css\` - Component Styles Only  
Core component styling without variables or utilities.
Requires ty.variables.css to work properly.

## Usage Patterns:

### Full Bundle (Recommended)
\`\`\`html
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tyrell/components@latest/css/ty.css">
<script src="https://cdn.jsdelivr.net/npm/@tyrell/components@latest/dist/ty.js"></script>
\`\`\`

### Modular Loading
\`\`\`html
<!-- Variables first (required) -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tyrell/components@latest/css/ty.variables.css">

<!-- Then utilities and/or core as needed -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tyrell/components@latest/css/ty.utilities.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@tyrell/components@latest/css/ty.core.css">

<!-- Load specific components -->
<script src="https://cdn.jsdelivr.net/npm/@tyrell/components@latest/dist/ty.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@tyrell/components@latest/dist/ty-calendar.js"></script>
\`\`\`
`;

  fs.writeFileSync(path.join(__dirname, '../css/README.md'), readmeContent);
  console.log('✅ CSS README created');
}

if (require.main === module) {
  console.log('🎨 Splitting ty.css for CDN optimization...');
  splitTyCss();
  createCssReadme();
}

module.exports = { splitTyCss, createCssReadme };
