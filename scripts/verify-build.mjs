#!/usr/bin/env node
import { existsSync } from 'fs';
import { readdir } from 'fs/promises';

const requiredFiles = [
  'dist/index.js',
  'dist/index.d.ts',
  'dist/cdn/ty.js',
  'dist/css/ty.css',
  'dist/css/ty.min.css',
  'dist/components/button.js',
  'dist/components/modal.js',
  'dist/components/input.js',
  'dist/components/dropdown.js',
  'dist/components/tabs.js',
];

async function verify() {
  console.log('\n🔍 Verifying build artifacts...\n');
  
  let allGood = true;
  
  for (const file of requiredFiles) {
    const exists = existsSync(file);
    const status = exists ? '✓' : '✗';
    const icon = exists ? '✅' : '❌';
    console.log(`${status} ${file}`);
    if (!exists) allGood = false;
  }
  
  // Count component files
  try {
    const componentFiles = await readdir('dist/components');
    const jsFiles = componentFiles.filter(f => f.endsWith('.js') && !f.includes('.map'));
    console.log(`\n📦 Found ${jsFiles.length} component files`);
  } catch (err) {
    console.log('\n❌ Could not read dist/components/');
    allGood = false;
  }
  
  if (allGood) {
    console.log('\n✅ All required build artifacts present!\n');
    process.exit(0);
  } else {
    console.log('\n❌ Some required files are missing!\n');
    process.exit(1);
  }
}

verify();
