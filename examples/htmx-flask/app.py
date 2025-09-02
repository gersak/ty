#!/usr/bin/env python3
"""
HTMX + Ty Web Components Example

A Flask application demonstrating how to use Ty web components
with HTMX for dynamic, server-rendered interactions.
"""

from flask import Flask, render_template, request, jsonify, redirect, url_for
from datetime import datetime, timedelta
import json
import random

app = Flask(__name__)
app.secret_key = "demo-key-change-in-production"

# Sample data for demonstrations
SAMPLE_USERS = [
    {"id": 1, "name": "Alice Johnson", "email": "alice@example.com", "role": "Admin"},
    {"id": 2, "name": "Bob Smith", "email": "bob@example.com", "role": "User"},
    {"id": 3, "name": "Carol Williams", "email": "carol@example.com", "role": "Editor"},
    {"id": 4, "name": "David Brown", "email": "david@example.com", "role": "User"},
    {"id": 5, "name": "Eva Davis", "email": "eva@example.com", "role": "Admin"},
]

SAMPLE_TASKS = [
    {
        "id": 1,
        "title": "Design new homepage",
        "priority": "high",
        "status": "in-progress",
    },
    {"id": 2, "title": "Fix login bug", "priority": "critical", "status": "pending"},
    {
        "id": 3,
        "title": "Update documentation",
        "priority": "low",
        "status": "completed",
    },
    {
        "id": 4,
        "title": "Optimize database queries",
        "priority": "medium",
        "status": "pending",
    },
]

# In-memory storage for demo (use a real database in production)
form_submissions = []
selected_dates = []


@app.route("/")
def index():
    """Home page showcasing various Ty components."""
    return render_template("index.html", users=SAMPLE_USERS[:3], tasks=SAMPLE_TASKS[:2])


@app.route("/forms")
def forms():
    """Form examples with Ty components."""
    return render_template("forms.html")


@app.route("/calendar")
def calendar():
    """Calendar component demonstrations."""
    return render_template("calendar.html")


@app.route("/components")
def components():
    """Individual component showcase."""
    return render_template("components.html")


# HTMX API endpoints


@app.route("/api/users/search")
def search_users():
    """Search users for dropdown/multiselect components."""
    query = request.args.get("q", "").lower()
    filtered_users = [
        user
        for user in SAMPLE_USERS
        if query in user["name"].lower() or query in user["email"].lower()
    ]
    return render_template("partials/user_search_results.html", users=filtered_users)


@app.route("/api/users/<int:user_id>")
def get_user(user_id):
    """Get user details for dynamic loading."""
    user = next((u for u in SAMPLE_USERS if u["id"] == user_id), None)
    if not user:
        return "User not found", 404
    return render_template("partials/user_card.html", user=user)


@app.route("/api/tasks/filter")
def filter_tasks():
    """Filter tasks by status or priority."""
    status = request.args.get("status")
    priority = request.args.get("priority")

    filtered_tasks = SAMPLE_TASKS
    if status:
        filtered_tasks = [t for t in filtered_tasks if t["status"] == status]
    if priority:
        filtered_tasks = [t for t in filtered_tasks if t["priority"] == priority]

    return render_template("partials/task_list.html", tasks=filtered_tasks)


@app.route("/api/tasks/<int:task_id>/toggle", methods=["POST"])
def toggle_task(task_id):
    """Toggle task completion status."""
    task = next((t for t in SAMPLE_TASKS if t["id"] == task_id), None)
    if task:
        task["status"] = "completed" if task["status"] != "completed" else "pending"
    return render_template("partials/task_item.html", task=task)


@app.route("/api/test-debug")
def test_debug():
    """Test endpoint for HTMX debugging."""
    print("=== TEST DEBUG ENDPOINT ===")
    print(f"Request method: {request.method}")
    print(f"Headers: {dict(request.headers)}")
    print("===========================")
    
    return """
    <div class="ty-bg-success-soft p-3 rounded text-sm">
        <ty-icon name="check-circle" class="inline mr-1 ty-text-success"></ty-icon>
        <strong>Debug test successful!</strong> Check console for HTMX logs.
        <br><small class="ty-text-success-mild">Timestamp: {}</small>
    </div>
    """.format(datetime.now().strftime('%H:%M:%S'))


@app.route("/api/form/validate", methods=["POST"])
def validate_form():
    """Server-side form validation with Ty components."""
    # Debug logging - print received data
    print("=== FORM VALIDATION DEBUG ===")
    print(f"Request method: {request.method}")
    print(f"Content-Type: {request.content_type}")
    print(f"Form data: {dict(request.form)}")
    print(f"Request data: {request.data}")
    print(f"Request args: {dict(request.args)}")
    print(f"Headers: {dict(request.headers)}")
    print(f"Is HTMX request: {'HX-Request' in request.headers}")
    print("===========================")
    
    errors = {}

    # Validate email
    email = request.form.get("email", "")
    if not email:
        errors["email"] = "Email is required"
    elif "@" not in email:
        errors["email"] = "Please enter a valid email address"

    # Validate name
    name = request.form.get("name", "")
    if not name or len(name.strip()) < 2:
        errors["name"] = "Name must be at least 2 characters"

    # Validate age
    age = request.form.get("age", "")
    if age and not age.isdigit():
        errors["age"] = "Age must be a number"
    elif age and (int(age) < 13 or int(age) > 120):
        errors["age"] = "Age must be between 13 and 120"

    if errors:
        print(f"Validation errors: {errors}")
        
        # For HTMX requests, return 200 with error HTML (so HTMX processes it)
        # For regular form submissions, return 400 (traditional behavior)
        if 'HX-Request' in request.headers:
            print("Returning validation errors with 200 status for HTMX")
            return render_template("partials/form_errors.html", errors=errors), 200
        else:
            print("Returning validation errors with 400 status for regular form")
            return render_template("partials/form_errors.html", errors=errors), 400

    # Success case
    print("Validation successful!")
    form_submissions.append(
        {
            "name": name,
            "email": email,
            "age": age,
            "timestamp": datetime.now().isoformat(),
        }
    )

    return render_template("partials/form_success.html", name=name)


@app.route("/api/calendar/events")
def calendar_events():
    """Get calendar events for a specific month."""
    year = int(request.args.get("year", datetime.now().year))
    month = int(request.args.get("month", datetime.now().month))

    # Generate some sample events
    events = []
    for day in range(1, 29):  # Avoid month-end complexities for demo
        if random.random() < 0.3:  # 30% chance of event
            events.append(
                {
                    "date": f"{year}-{month:02d}-{day:02d}",
                    "title": random.choice(
                        [
                            "Team Meeting",
                            "Code Review",
                            "Client Call",
                            "Project Deadline",
                            "Workshop",
                            "Planning Session",
                        ]
                    ),
                }
            )

    return jsonify(events)


@app.route("/api/date/select", methods=["POST"])
def select_date():
    """Handle date selection from calendar."""
    date_str = request.form.get("date")
    if date_str:
        selected_dates.append(
            {"date": date_str, "timestamp": datetime.now().isoformat()}
        )
        return render_template("partials/selected_date.html", date=date_str)
    return "Invalid date", 400


@app.route("/test-icons")
def test_icons():
    """Test page to verify icon registration is working."""
    return """
    <!DOCTYPE html>
    <html>
    <head>
        <title>Icon Test - Ty Components</title>
        <link rel="stylesheet" href="/static/css/ty.css">
        <link rel="stylesheet" href="/static/css/app.css">
    </head>
    <body class="ty-canvas p-8">
        <div class="max-w-4xl mx-auto">
            <h1 class="text-3xl font-bold ty-text-primary-strong mb-8">🎨 Icon Test Page</h1>
            
            <div class="ty-elevated p-6 rounded-xl mb-8">
                <h2 class="text-xl font-semibold mb-4">Icon Status</h2>
                <div id="icon-status" class="space-y-2">
                    <p class="ty-text-neutral-mild">Loading...</p>
                </div>
            </div>
            
            <div class="ty-elevated p-6 rounded-xl mb-8">
                <h2 class="text-xl font-semibold mb-4">Sample Icons</h2>
                <div class="grid grid-cols-8 gap-4" id="icon-grid">
                    <!-- Icons will be populated by JavaScript -->
                </div>
            </div>
            
            <div class="space-y-4">
                <ty-button flavor="primary" onclick="testAllIcons()">
                    <ty-icon name="search" class="mr-2"></ty-icon>
                    Test All Icons
                </ty-button>
                <ty-button flavor="secondary" onclick="window.location.href='/'">
                    <ty-icon name="home" class="mr-2"></ty-icon>
                    Back to Demo
                </ty-button>
            </div>
        </div>

        <script src="/static/js/main.js"></script>
        <script src="/static/js/icons.js"></script>
        
        <script>
        const sampleIcons = [
            'home', 'settings', 'search', 'check', 'x', 'plus', 'user', 'bell',
            'calendar', 'edit', 'heart', 'star', 'sun', 'moon', 'github', 'zap'
        ];

        function updateStatus() {
            const statusDiv = document.getElementById('icon-status');
            const tyLoaded = typeof ty !== 'undefined';
            const iconsLoaded = tyLoaded && ty.icons;
            
            statusDiv.innerHTML = `
                <div class="space-y-2">
                    <div class="flex items-center space-x-2">
                        <ty-icon name="check-circle" class="ty-text-success"></ty-icon>
                        <span>Ty Components: ${tyLoaded ? '✅ Loaded' : '❌ Failed'}</span>
                    </div>
                    <div class="flex items-center space-x-2">
                        <ty-icon name="check-circle" class="ty-text-success"></ty-icon>
                        <span>Icon System: ${iconsLoaded ? '✅ Ready' : '❌ Failed'}</span>
                    </div>
                </div>
            `;
        }

        function populateIconGrid() {
            const grid = document.getElementById('icon-grid');
            grid.innerHTML = sampleIcons.map(iconName => `
                <div class="text-center">
                    <div class="w-12 h-12 ty-bg-primary-soft rounded-lg flex items-center justify-center mx-auto mb-2">
                        <ty-icon name="${iconName}" class="ty-text-primary-strong"></ty-icon>
                    </div>
                    <p class="text-xs ty-text-neutral-mild">${iconName}</p>
                </div>
            `).join('');
        }

        function testAllIcons() {
            if (typeof listTyIcons === 'function') {
                listTyIcons();
            } else {
                console.log('Icon listing function not available');
            }
        }

        // Wait for everything to load
        window.addEventListener('ty-icons-ready', function(event) {
            console.log('Icons ready!', event.detail);
            updateStatus();
            populateIconGrid();
        });

        // Fallback for immediate update
        document.addEventListener('DOMContentLoaded', function() {
            setTimeout(() => {
                updateStatus();
                populateIconGrid();
            }, 1000);
        });
        </script>
    </body>
    </html>
    """


@app.route("/api/calendar/date-select", methods=["POST"])
def calendar_date_select():
    """Handle calendar page date selection with server response."""
    # Get date from the custom date-select event
    date_value = request.form.get('date-value')  # timestamp
    date_str = request.form.get('date')          # ISO date string
    
    if not date_str:
        return "<p class='ty-text-danger'>❌ No date received</p>"
    
    try:
        # Parse the date
        from datetime import datetime
        date_obj = datetime.fromisoformat(date_str.replace('Z', '+00:00'))
        
        # Format for display
        formatted_date = date_obj.strftime('%A, %B %d, %Y')
        
        # Simulate some server processing
        import random
        processing_time = random.uniform(0.1, 0.5)
        import time
        time.sleep(processing_time)
        
        # Generate response
        day_name = date_obj.strftime('%A')
        is_weekend = day_name in ['Saturday', 'Sunday']
        
        response_html = f"""
        <div class="animate-fade-in space-y-3">
            <div class="flex items-center space-x-3">
                <div class="w-3 h-3 rounded-full ty-bg-success"></div>
                <span class="font-medium">Date Selected: {formatted_date}</span>
            </div>
            <div class="ty-bg-{'warning' if is_weekend else 'info'}-soft rounded-lg p-3">
                <p class="text-sm">
                    {'🎉 Weekend day! Perfect for relaxation.' if is_weekend else '💼 Weekday - great for productivity!'}
                </p>
                <p class="text-xs ty-text-neutral-mild mt-1">
                    Server processed in {processing_time:.2f}s
                </p>
            </div>
        </div>
        """
        
        return response_html
        
    except Exception as e:
        return f"<p class='ty-text-danger'>❌ Error processing date: {str(e)}</p>"


@app.route("/api/modal/content/<content_type>")
def modal_content(content_type):
    """Dynamic modal content loading."""
    if content_type == "user-profile":
        user_id = request.args.get("user_id")
        user = next((u for u in SAMPLE_USERS if u["id"] == int(user_id)), None)
        return render_template("partials/user_profile_modal.html", user=user)

    elif content_type == "task-details":
        task_id = request.args.get("task_id")
        task = next((t for t in SAMPLE_TASKS if t["id"] == int(task_id)), None)
        return render_template("partials/task_details_modal.html", task=task)

    return "Content not found", 404


@app.route("/api/notifications/demo")
def demo_notification():
    """Generate a demo notification."""
    notifications = [
        {"type": "success", "message": "Task completed successfully!"},
        {"type": "warning", "message": "Server maintenance scheduled for tonight."},
        {"type": "info", "message": "New features available in the dashboard."},
        {"type": "error", "message": "Failed to save changes. Please try again."},
    ]

    notification = random.choice(notifications)
    return render_template("partials/notification.html", **notification)


# Error handlers
@app.errorhandler(404)
def not_found(error):
    return render_template("404.html"), 404


@app.errorhandler(500)
def server_error(error):
    return render_template("500.html"), 500


# Template filters
@app.template_filter("datetime_format")
def datetime_format(value, format_str="%B %d, %Y at %I:%M %p"):
    """Format datetime strings."""
    if isinstance(value, str):
        try:
            dt = datetime.fromisoformat(value)
            return dt.strftime(format_str)
        except ValueError:
            return value
    return value


if __name__ == "__main__":
    print("🚀 Starting HTMX + Ty Components Demo")
    print("📝 Visit http://localhost:9000 to see the examples")
    print("🎨 Make sure Ty components are built and accessible")

    app.run(debug=True, host="0.0.0.0", port=9000)
