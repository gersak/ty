/**
 * Main Application JavaScript for Flask + HTMX + Ty Components Demo
 * 
 * This file contains application-specific JavaScript that works with
 * Ty components and HTMX interactions.
 */

// Application initialization
document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 Ty Components Flask Demo initialized');
    
    // Check if Ty components loaded successfully
    if (typeof ty === 'undefined') {
        console.error('❌ Ty components failed to load');
        showErrorMessage('Component library failed to load. Please refresh the page.');
        return;
    }
    
    console.log('✅ Ty components loaded successfully');
});

// Listen for icons ready event
window.addEventListener('ty-icons-ready', function(event) {
    console.log('🎨 Icons loaded:', event.detail.count, 'icons ready');
    
    // Update any loading states or enable icon-dependent features
    document.body.classList.add('icons-ready');
});

// Enhanced HTMX integration with comprehensive debugging
document.body.addEventListener('htmx:configRequest', function(event) {
    console.log('🚀 HTMX Request Config:', {
        url: event.detail.path,
        verb: event.detail.verb,
        headers: event.detail.headers,
        parameters: event.detail.parameters,
        element: event.detail.elt,
        triggeringEvent: event.detail.triggeringEvent
    });
});

document.body.addEventListener('htmx:beforeRequest', function(event) {
    console.log('📤 HTMX Before Request:', {
        url: event.detail.path,
        method: event.detail.xhr?.method || 'GET',
        requestConfig: event.detail.requestConfig,
        element: event.detail.elt.tagName + (event.detail.elt.id ? '#' + event.detail.elt.id : ''),
        formData: event.detail.elt.closest('form') ? new FormData(event.detail.elt.closest('form')) : null
    });
    
    // Log form data for debugging
    if (event.detail.elt.closest('form')) {
        const formData = new FormData(event.detail.elt.closest('form'));
        console.log('📋 Form Data:', Object.fromEntries(formData.entries()));
    }
});

document.body.addEventListener('htmx:afterRequest', function(event) {
    const xhr = event.detail.xhr;
    console.log('📥 HTMX After Request:', {
        url: xhr.responseURL || event.detail.pathInfo?.requestPath,
        status: xhr.status,
        statusText: xhr.statusText,
        responseText: xhr.responseText?.substring(0, 200) + (xhr.responseText?.length > 200 ? '...' : ''),
        successful: event.detail.successful,
        failed: event.detail.failed,
        headers: xhr.getAllResponseHeaders()
    });
});

document.body.addEventListener('htmx:responseError', function(event) {
    const xhr = event.detail.xhr;
    console.error('❌ HTMX Response Error:', {
        url: xhr.responseURL || event.detail.pathInfo?.requestPath,
        status: xhr.status,
        statusText: xhr.statusText,
        responseText: xhr.responseText,
        element: event.detail.elt.tagName + (event.detail.elt.id ? '#' + event.detail.elt.id : ''),
        headers: xhr.getAllResponseHeaders()
    });
    
    // Handle 400 validation errors - insert the response HTML into the target
    if (xhr.status === 400 && xhr.responseText) {
        const targetSelector = event.detail.elt.getAttribute('hx-target');
        if (targetSelector) {
            const targetElement = document.querySelector(targetSelector);
            if (targetElement) {
                targetElement.innerHTML = xhr.responseText;
                console.log('✅ Inserted validation error HTML into target:', targetSelector);
            } else {
                console.error('❌ Target element not found:', targetSelector);
            }
        }
    } else if (xhr.status !== 400) {
        showErrorMessage('Request failed. Please try again.');
    }
});

document.body.addEventListener('htmx:timeout', function(event) {
    console.warn('⏱️ HTMX Timeout:', event.detail.path);
    showErrorMessage('Request timed out. Please check your connection.');
});

// Add debugging for successful responses
document.body.addEventListener('htmx:load', function(event) {
    console.log('✅ HTMX Content Loaded:', {
        element: event.detail.elt,
        xhr: event.detail.xhr?.status
    });
});

// Utility functions
function showErrorMessage(message, duration = 5000) {
    // Create a simple error notification
    const notification = document.createElement('div');
    notification.className = 'fixed top-4 right-4 ty-bg-danger-soft ty-text-danger-strong px-6 py-4 rounded-lg shadow-lg z-50 animate-slide-up';
    notification.innerHTML = `
        <div class="flex items-center space-x-3">
            <ty-icon name="alert-circle" class="flex-shrink-0"></ty-icon>
            <span>${message}</span>
            <button onclick="this.parentElement.parentElement.remove()" class="ml-4 ty-text-danger hover:ty-text-danger-strong">
                <ty-icon name="x" class="w-4 h-4"></ty-icon>
            </button>
        </div>
    `;
    
    document.body.appendChild(notification);
    
    // Auto remove after duration
    setTimeout(() => {
        if (notification.parentNode) {
            notification.style.animation = 'fadeOut 0.3s ease-out forwards';
            setTimeout(() => notification.remove(), 300);
        }
    }, duration);
}

function showSuccessMessage(message, duration = 3000) {
    // Create a simple success notification
    const notification = document.createElement('div');
    notification.className = 'fixed top-4 right-4 ty-bg-success-soft ty-text-success-strong px-6 py-4 rounded-lg shadow-lg z-50 animate-slide-up';
    notification.innerHTML = `
        <div class="flex items-center space-x-3">
            <ty-icon name="check-circle" class="flex-shrink-0"></ty-icon>
            <span>${message}</span>
            <button onclick="this.parentElement.parentElement.remove()" class="ml-4 ty-text-success hover:ty-text-success-strong">
                <ty-icon name="x" class="w-4 h-4"></ty-icon>
            </button>
        </div>
    `;
    
    document.body.appendChild(notification);
    
    // Auto remove after duration
    setTimeout(() => {
        if (notification.parentNode) {
            notification.style.animation = 'fadeOut 0.3s ease-out forwards';
            setTimeout(() => notification.remove(), 300);
        }
    }, duration);
}

// Enhanced form handling
document.body.addEventListener('submit', function(event) {
    const form = event.target;
    if (!form.matches('form')) return;
    
    // Add loading state to submit buttons
    const submitButtons = form.querySelectorAll('ty-button[type="submit"], button[type="submit"]');
    submitButtons.forEach(button => {
        button.disabled = true;
        const originalText = button.textContent;
        button.innerHTML = '<div class="spinner mr-2"></div>Submitting...';
        
        // Reset after a delay (in case HTMX doesn't handle the response)
        setTimeout(() => {
            button.disabled = false;
            button.textContent = originalText;
        }, 10000);
    });
});

// Form validation helpers
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

function validateRequired(value) {
    return value && value.trim().length > 0;
}

// Ty component event handlers
document.body.addEventListener('input', function(event) {
    if (event.target.matches('ty-input[type="email"]')) {
        const input = event.target;
        const email = input.value;
        
        if (email && !validateEmail(email)) {
            input.classList.add('border-red-500');
            showValidationError(input, 'Please enter a valid email address');
        } else {
            input.classList.remove('border-red-500');
            clearValidationError(input);
        }
    }
});

function showValidationError(input, message) {
    // Remove existing error
    clearValidationError(input);
    
    // Add error message
    const error = document.createElement('p');
    error.className = 'validation-error text-sm ty-text-danger mt-1';
    error.textContent = message;
    
    input.parentNode.appendChild(error);
}

function clearValidationError(input) {
    const existing = input.parentNode.querySelector('.validation-error');
    if (existing) {
        existing.remove();
    }
}

// Debug helpers (only in development)
if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
    window.debugTy = function() {
        console.log('🔍 Ty Debug Info:');
        console.log('- Components loaded:', typeof ty !== 'undefined');
        console.log('- Icons available:', typeof ty !== 'undefined' && ty.icons ? 'Yes' : 'No');
        
        if (typeof ty !== 'undefined' && ty.icons) {
            console.log('- Try: listTyIcons() to see all available icons');
            console.log('- Try: checkTyIcon("home") to check a specific icon');
        }
        
        // Count registered components
        const tyComponents = document.querySelectorAll('[is^="ty-"], ty-');
        console.log(`- Found ${tyComponents.length} Ty components on page`);
        
        return {
            components: tyComponents.length,
            iconsLoaded: typeof ty !== 'undefined' && !!ty.icons
        };
    };
    
    // Add debug info to console
    setTimeout(() => {
        console.log('🛠️ Development mode - type debugTy() for debug info');
    }, 1000);
}

// Export useful functions globally
window.tyApp = {
    showError: showErrorMessage,
    showSuccess: showSuccessMessage,
    validateEmail,
    validateRequired
};
