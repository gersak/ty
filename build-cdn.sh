#!/bin/bash

# Ty Components - CDN Build Script
# Reads version from package-cdn.json and builds everything correctly

set -e

# Read version and package name from package-cdn.json
if [ ! -f "package-cdn.json" ]; then
    echo "❌ Error: package-cdn.json not found"
    exit 1
fi

VERSION=$(grep '"version":' package-cdn.json | sed 's/.*"version": *"\([^"]*\)".*/\1/')
PACKAGE_NAME=$(grep '"name":' package-cdn.json | sed 's/.*"name": *"\([^"]*\)".*/\1/')
ASSET_PATH="https://cdn.jsdelivr.net/npm/${PACKAGE_NAME}@${VERSION}"

echo "🚀 Building Ty Components v${VERSION} for CDN deployment..."
echo "📦 Package name: ${PACKAGE_NAME}"

# Update shadow-cljs.edn with correct asset-path
echo "🔧 Updating shadow-cljs.edn asset-path..."
sed -i.bak "s|:asset-path \"[^\"]*\"|:asset-path \"${ASSET_PATH}\"|g" shadow-cljs.edn
rm shadow-cljs.edn.bak
echo "   ✅ Set asset-path to: ${ASSET_PATH}"

# Clean and build
echo "📁 Preparing distribution directory..."
rm -rf dist-cdn
mkdir -p dist-cdn/css

echo "🔨 Building ClojureScript modules..."
npx shadow-cljs release lib

# Copy files to package structure
echo "📋 Copying files..."
cp dist/*.js dist-cdn/
cp dist/*.d.ts dist-cdn/ 2>/dev/null || true
cp dev/css/ty.css dist-cdn/css/ 2>/dev/null || echo "⚠️ ty.css not found"
cp package-cdn.json dist-cdn/package.json
cp README-CDN.md dist-cdn/README.md

# Create LICENSE
cat > dist-cdn/LICENSE << 'EOF'
MIT License

Copyright (c) 2024 Ty Components

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
EOF

# Validate
if [ ! -f "dist-cdn/ty.js" ] || [ ! -f "dist-cdn/package.json" ]; then
    echo "❌ Build validation failed!"
    exit 1
fi

echo ""
echo "✨ Build completed successfully!"
echo "📦 Package: ${PACKAGE_NAME}@${VERSION}"
echo "🎯 JS modules: $(ls dist-cdn/*.js 2>/dev/null | wc -l)"
echo "🎨 CSS files: $(ls dist-cdn/css/*.css 2>/dev/null | wc -l || echo 0)"
echo ""
echo "🚀 To publish: cd dist-cdn && npm publish"
echo "🌐 Will be available at: ${ASSET_PATH}ty.js"
