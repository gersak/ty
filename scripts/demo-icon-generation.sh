#!/bin/bash

# Icon Generation Demo
# Shows how to generate and use icons

echo "🎨 Ty Icon Generation Demo"
echo "=========================="
echo ""

# Step 1: Generate Lucide icons
echo "📦 Step 1: Generating Lucide icons..."
echo "Running: npm run generate:lucide"
echo ""
npm run generate:lucide

if [ $? -eq 0 ]; then
  echo ""
  echo "✅ Icons generated successfully!"
  echo ""
  
  # Check output
  if [ -f "ts/generated/lucide-icons.ts" ]; then
    FILE_SIZE=$(du -h ts/generated/lucide-icons.ts | cut -f1)
    ICON_COUNT=$(grep -c "export const.*=.*lucideIcons\[" ts/generated/lucide-icons.ts)
    
    echo "📊 Statistics:"
    echo "   File: ts/generated/lucide-icons.ts"
    echo "   Size: $FILE_SIZE"
    echo "   Icons: ~$ICON_COUNT"
    echo ""
  fi
  
  # Step 2: Build TypeScript
  echo "🔨 Step 2: Building TypeScript..."
  echo "Running: npm run build:ts"
  echo ""
  npm run build:ts
  
  if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Build successful!"
    echo ""
    
    # Step 3: Instructions
    echo "🎉 All done! Next steps:"
    echo ""
    echo "1. Start dev server:"
    echo "   npm run test:ts"
    echo ""
    echo "2. Open browser:"
    echo "   http://localhost:8000/icon-demo.html"
    echo ""
    echo "3. Use icons in your code:"
    echo "   import { check, heart, star } from './ts/generated/lucide-icons.js'"
    echo "   import { IconRegistry } from './ts/utils/icon-registry.js'"
    echo "   IconRegistry.registerIcons({ check, heart, star })"
    echo ""
    echo "📚 See ICON_GENERATION.md for full documentation"
    echo ""
  else
    echo "❌ Build failed!"
    exit 1
  fi
else
  echo "❌ Generation failed!"
  exit 1
fi
