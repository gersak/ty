#!/bin/bash

# Ty + HTMX Flask Example - Setup Script
echo "🎨 Setting up beautiful Ty Components Flask example..."

# Check if we're in the right directory
if [[ ! -f "app.py" ]]; then
    echo "❌ Please run this script from the htmx-flask directory"
    exit 1
fi

# Install Node.js dependencies
echo "📦 Installing dependencies..."
npm install

# Copy Ty CSS from main project
echo "📋 Copying Ty CSS files..."
cp ../../../dev/css/ty.css ./static/css/ 2>/dev/null || echo "⚠️  Could not copy ty.css automatically - you may need to copy it manually"

# Build Tailwind CSS
echo "🎨 Building Tailwind CSS..."
npm run build:css:prod

echo ""
echo "✅ Setup complete! 🎉"
echo ""
echo "🚀 To run the development server:"
echo "   npm run dev    # (runs both Tailwind watch and Flask)"
echo "   or"  
echo "   python app.py  # (Flask only)"
echo ""
echo "🌐 Then visit: http://localhost:5000"
echo ""
echo "💡 The app uses:"
echo "   - Ty Components for UI elements"
echo "   - HTMX for dynamic server interactions" 
echo "   - Tailwind CSS for beautiful styling"
echo ""
