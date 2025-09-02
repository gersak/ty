/**
 * Ty Components + HTMX Integration Helper
 * 
 * This script improves the integration between Ty web components and HTMX
 * by handling event forwarding and providing better debugging.
 */

// Enhanced HTMX + Ty Components Integration
document.addEventListener('DOMContentLoaded', function() {
    console.log('🔗 Ty Components + HTMX integration loaded');

    // Debug: Log all custom events from Ty components
    if (window.location.hostname === 'localhost') {
        document.addEventListener('input', function(event) {
            if (event.target.matches('ty-input')) {
                console.log('🎯 Ty Input Event:', {
                    type: 'input',
                    target: event.target.getAttribute('name'),
                    value: event.detail?.value,
                    bubbles: event.bubbles,
                    composed: event.composed
                });
            }
        });

        document.addEventListener('change', function(event) {
            if (event.target.matches('ty-input')) {
                console.log('🎯 Ty Change Event:', {
                    type: 'change',
                    target: event.target.getAttribute('name'),
                    value: event.detail?.value,
                    bubbles: event.bubbles,
                    composed: event.composed
                });
            }
        });
    }

    // Helper function to manually validate a form field
    window.validateField = function(fieldName) {
        const field = document.querySelector(`ty-input[name="${fieldName}"]`);
        if (field && field.hasAttribute('hx-post')) {
            console.log(`🔍 Manual validation triggered for: ${fieldName}`);
            htmx.trigger(field, 'change');
            return true;
        }
        console.warn(`❌ Field not found or no validation: ${fieldName}`);
        return false;
    };

    // Helper function to get current form data
    window.getFormData = function(formSelector = 'form') {
        const form = document.querySelector(formSelector);
        if (!form) return {};
        
        const data = {};
        const inputs = form.querySelectorAll('ty-input');
        
        inputs.forEach(input => {
            const name = input.getAttribute('name');
            if (name) {
                // Try to get value from Ty component's internal input
                const internalInput = input.shadowRoot?.querySelector('input') || 
                                    input.querySelector('input');
                data[name] = internalInput?.value || '';
            }
        });
        
        console.log('📋 Form Data:', data);
        return data;
    };
});

// HTMX Event Listeners for better debugging
document.body.addEventListener('htmx:configRequest', function(event) {
    // Log all HTMX requests in development
    if (window.location.hostname === 'localhost') {
        console.log('📤 HTMX Request:', {
            method: event.detail.verb,
            url: event.detail.path,
            element: event.target.tagName.toLowerCase(),
            name: event.target.getAttribute('name')
        });
    }
});

document.body.addEventListener('htmx:responseError', function(event) {
    console.error('❌ HTMX Error:', {
        status: event.detail.xhr.status,
        statusText: event.detail.xhr.statusText,
        url: event.detail.xhr.responseURL
    });
});

document.body.addEventListener('htmx:afterRequest', function(event) {
    if (window.location.hostname === 'localhost' && event.detail.successful) {
        console.log('✅ HTMX Success:', {
            status: event.detail.xhr.status,
            target: event.detail.target?.id || 'unknown'
        });
    }
});

// Export debug helpers
window.tyHtmxDebug = {
    validateField: window.validateField,
    getFormData: window.getFormData,
    
    // Test if Ty components are working with HTMX
    testIntegration: function() {
        console.log('🧪 Testing Ty + HTMX Integration...');
        
        const inputs = document.querySelectorAll('ty-input[hx-post]');
        console.log(`Found ${inputs.length} Ty inputs with HTMX attributes`);
        
        inputs.forEach((input, i) => {
            const name = input.getAttribute('name');
            const trigger = input.getAttribute('hx-trigger');
            const target = input.getAttribute('hx-target');
            
            console.log(`${i + 1}. ${name}: trigger="${trigger}", target="${target}"`);
        });
        
        return inputs.length > 0;
    }
};
