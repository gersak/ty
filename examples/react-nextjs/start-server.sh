#!/bin/bash

echo "Starting Next.js React example server..."
echo "==========================================="
echo ""
echo "This will start the development server on http://localhost:3000"
echo ""

cd /Users/robi/dev/gersak/ty/examples/react-nextjs

# Check if node_modules exists
if [ ! -d "node_modules" ]; then
    echo "Installing dependencies first..."
    npm install
fi

# Copy ty date-picker files if needed
echo "Copying ty component files..."
cp /Users/robi/dev/gersak/ty/dev/ty-*.js public/ 2>/dev/null || true
cp /Users/robi/dev/gersak/ty/dev/ty.css public/ 2>/dev/null || true

echo ""
echo "Starting server..."
echo "Navigate to: http://localhost:3000/examples/date-picker"
echo ""

# Start the dev server
npm run dev