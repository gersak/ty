/**
 * Main Application JavaScript for Flask + HTMX + Ty Components Demo
 * 
 * Clean implementation using HTMX json-enc extension for reliable form serialization.
 */

// Application initialization
// Listen for icons ready event
window.addEventListener('ty-icons-ready', function(event) {
    console.log('🎨 Icons loaded:', event.detail.count, 'icons ready');
    document.body.classList.add('icons-ready');
});

// HTMX error handling
document.body.addEventListener('htmx:responseError', function(event) {
    const xhr = event.detail.xhr;
    console.error('❌ HTMX Response Error:', {
        status: xhr.status,
        url: xhr.responseURL
    });

    // Show generic error message for actual errors (5xx, network issues, etc.)
    showErrorMessage('Request failed. Please try again.');
});

// Success response handling
document.body.addEventListener('htmx:afterRequest', function(event) {
    if (event.detail.successful && event.detail.xhr.status === 200) {
        // Reset form on successful submission
        const form = event.detail.elt.closest('form');
        if (form && form.querySelector('ty-button[type="submit"]')) {
            // Small delay to let user see the success message
            setTimeout(() => {
                form.reset();
                // Clear any remaining feedback
                form.querySelectorAll('[id$="-feedback"]').forEach(el => {
                    el.innerHTML = '';
                });
            }, 2000);
        }
    }
});

// Utility functions
function showErrorMessage(message, duration = 5000) {
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

    setTimeout(() => {
        if (notification.parentNode) {
            notification.style.animation = 'fadeOut 0.3s ease-out forwards';
            setTimeout(() => notification.remove(), 300);
        }
    }, duration);
}

function showSuccessMessage(message, duration = 3000) {
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

    setTimeout(() => {
        if (notification.parentNode) {
            notification.style.animation = 'fadeOut 0.3s ease-out forwards';
            setTimeout(() => notification.remove(), 300);
        }
    }, duration);
}

// Form enhancements
document.body.addEventListener('submit', function(event) {
    const form = event.target;
    if (!form.matches('form')) return;

    // Add loading state to submit buttons
    const submitButtons = form.querySelectorAll('ty-button[type="submit"], button[type="submit"]');
    submitButtons.forEach(button => {
        button.disabled = true;
        const originalText = button.textContent;
        button.innerHTML = '<ty-icon name="loader-2" class="animate-spin mr-2"></ty-icon>Submitting...';

        // Reset after response or timeout
        const resetButton = () => {
            button.disabled = false;
            button.textContent = originalText;
        };

        // Listen for HTMX response
        const responseHandler = () => {
            resetButton();
            document.body.removeEventListener('htmx:afterRequest', responseHandler);
            document.body.removeEventListener('htmx:responseError', responseHandler);
        };

        document.body.addEventListener('htmx:afterRequest', responseHandler);
        document.body.addEventListener('htmx:responseError', responseHandler);

        // Fallback timeout
        setTimeout(resetButton, 10000);
    });
});

// Export useful functions
window.tyApp = {
    showError: showErrorMessage,
    showSuccess: showSuccessMessage
};

// Development helpers
if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
    window.testForm = function() {
        console.log('🧪 Testing form data collection...');

        const form = document.querySelector('form[hx-ext="json-enc"]');
        if (!form) {
            console.error('❌ No JSON form found');
            return;
        }

        const formData = new FormData(form);
        const jsonData = {};

        // Collect ty-component values
        form.querySelectorAll('ty-input[name], ty-dropdown[name], ty-multiselect[name]').forEach(el => {
            const name = el.getAttribute('name');
            const value = el.value;
            if (name && value !== null && value !== undefined) {
                jsonData[name] = value;
            }
        });

        console.log('📋 Form data as JSON:', jsonData);
        console.log('📋 Native FormData:', Object.fromEntries(formData));

        return jsonData;
    };

    console.log('🛠️ Dev mode - try testForm() in console');
}
