# Datastar + Ty Guide

Use Ty web components with Datastar for reactive server-driven UIs. No JavaScript framework needed — just HTML attributes, SSE, and your backend of choice.

## Setup

### HTML Head

```html
<!-- Ty CSS and Components -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/css/ty.css">
<script type="module" src="https://cdn.jsdelivr.net/npm/@gersak/ty@latest/dist/ty.js"></script>

<!-- Datastar -->
<script type="module" src="https://cdn.jsdelivr.net/gh/starfederation/datastar@v1.0.0-RC.8/bundles/datastar.js"></script>
```

Three script tags. No build tools. No bundler.

## Core Pattern: Signals + Ty Components

Datastar signals are reactive state. Ty components bind to them with `data-bind`:

```html
<div data-signals="{name: '', email: '', country: ''}">

  <ty-input data-bind="name" label="Name" placeholder="Your name"></ty-input>

  <ty-input data-bind="email" label="Email" type="email"></ty-input>

  <ty-dropdown data-bind="country" label="Country" placeholder="Select...">
    <ty-option value="us">United States</ty-option>
    <ty-option value="de">Germany</ty-option>
  </ty-dropdown>

  <ty-button flavor="primary" data-on:click="@post('/api/submit')">
    Submit
  </ty-button>

</div>
```

When the user clicks Submit, Datastar sends all signals as JSON to your backend via POST.

## Event Binding

### Standard Events

```html
<!-- Button click -->
<ty-button data-on:click="@post('/api/action')">Do it</ty-button>

<!-- Update signal on change -->
<ty-dropdown data-on:change="$currency = evt.detail.value">
  <ty-option value="USD">$ USD</ty-option>
  <ty-option value="EUR">€ EUR</ty-option>
</ty-dropdown>
```

### The Golden Rule

Always access Ty component values through `evt.detail.value`:

```html
<!-- ✅ Correct -->
<ty-dropdown data-on:change="$country = evt.detail.value">

<!-- ❌ Wrong -->
<ty-dropdown data-on:change="$country = evt.value">
```

### Two-Way Binding vs Event Binding

Use `data-bind` for simple form inputs — it handles the wiring automatically:

```html
<!-- Two-way binding (preferred for form inputs) -->
<ty-input data-bind="username"></ty-input>

<!-- Manual event binding (when you need custom logic) -->
<ty-input data-on:change="$username = evt.detail.value; $dirty = true"></ty-input>
```

## Dynamic Attributes

Bind Ty component attributes to signal values:

```html
<!-- Toggle tag flavor based on state -->
<ty-tag
  data-attr:flavor="$txType === 'expense' ? 'danger' : 'success'"
  data-on:click="$txType = 'expense'">
  Expense
</ty-tag>

<!-- Set active tab from signal -->
<ty-tabs data-attr:active="$activeTab"
         data-on:change="$activeTab = evt.detail.value">
  <ty-tab id="overview" label="Overview">...</ty-tab>
  <ty-tab id="settings" label="Settings">...</ty-tab>
</ty-tabs>

<!-- Conditional wizard step -->
<ty-wizard data-attr:active="$wizardStep"
           data-attr:completed="$wizardCompleted">
  <ty-step id="welcome" label="Welcome">...</ty-step>
  <ty-step id="budget" label="Budget">...</ty-step>
</ty-wizard>
```

## Conditional Visibility

```html
<!-- Show setup form until complete -->
<div data-show="!$setupComplete">
  <ty-input data-bind="userName" label="Your name"></ty-input>
  <ty-button data-on:click="$setupComplete = true">Continue</ty-button>
</div>

<!-- Show app after setup -->
<div data-show="$setupComplete">
  <p data-text="'Welcome, ' + $userName"></p>
</div>
```

## Server-Side: SSE Responses

Datastar communicates with your backend through Server-Sent Events. Your backend sends three types of events:

### Patch Elements (Update DOM)

```
event: datastar-patch-elements
data: selector #dashboard
data: mode innerHTML
data: elements <div id="dashboard"><h2>Updated!</h2></div>
```

### Patch Signals (Update State)

```
event: datastar-patch-signals
data: signals {"txDesc":"","txAmount":"","txDate":""}
```

### Combined Response

A typical form submission response updates the UI and resets the form:

```
event: datastar-patch-elements
data: selector #transaction-list
data: mode innerHTML
data: elements <div id="transaction-list">...</div>

event: datastar-patch-signals
data: signals {"txDesc":"","txAmount":"","txCategory":"","txDate":""}
```

## Clojure Backend Example

### Parse Incoming Signals

```clojure
(defn parse-signals
  "Extract Datastar signals from request.
   GET → query param, POST → body."
  [request]
  (let [raw (or (get-in request [:params :datastar])
                (when-let [body (:body request)]
                  (if (string? body) body (slurp body))))]
    (when (and raw (not (str/blank? raw)))
      (json/read-str raw :key-fn keyword))))
```

### Send SSE Responses

```clojure
(defn patch-elements [& fragments]
  (str "event: datastar-patch-elements\n"
       "data: mode innerHTML\n"
       "data: elements " (render-html fragments) "\n\n"))

(defn patch-signals [signals]
  (str "event: datastar-patch-signals\n"
       "data: signals " (json/write-str signals) "\n\n"))

(defn sse-response [& events]
  {:status 200
   :headers {"Content-Type" "text/event-stream"
             "Cache-Control" "no-cache"}
   :body (apply str events)})
```

### Form Handler

```clojure
(defn add-transaction [request]
  (let [{:keys [txDesc txAmount txType]} (parse-signals request)]
    (if (or (empty? txDesc) (nil? txAmount))
      ;; Validation error → update result div
      (sse-response
        (patch-elements
          [:div#result.ty-bg-danger-.p-3.rounded-lg.border.ty-border-danger
           [:p.ty-text-danger "Please fill in all fields."]]))
      ;; Success → update list + reset form
      (do
        (save-transaction! {:desc txDesc :amount txAmount :type txType})
        (sse-response
          (patch-elements (transaction-list-fragment))
          (patch-elements (summary-fragment))
          (patch-signals {:txDesc "" :txAmount "" :txDate ""}))))))
```

### Middleware

Pre-read the body before Ring's `wrap-params` consumes it:

```clojure
(defn wrap-slurp-body
  "Capture POST body as string for Datastar signal parsing."
  [handler]
  (fn [request]
    (let [body (:body request)
          body-str (when body
                     (if (string? body) body
                       (let [s (slurp body)]
                         (when-not (empty? s) s))))]
      (handler (assoc request :body body-str)))))

(def app
  (-> handler
      wrap-slurp-body
      wrap-params
      wrap-keyword-params))
```

## Icon Registration

### Server-Side (Clojure)

Generate a registration script on the server:

```clojure
(ns myapp.icons
  (:require [clojure.data.json :as json]
            [ty.lucide :as lucide]))

(def app-icons
  {"wallet"    lucide/wallet
   "plus"      lucide/plus
   "settings"  lucide/settings
   "check"     lucide/check
   "calendar"  lucide/calendar})

(defn registration-script []
  [:script
   (hiccup.util/raw-string
     (str "(function() {
       var icons = " (json/write-str app-icons) ";
       function register() {
         if (!window.tyIcons || !window.tyIcons.register) return false;
         window.tyIcons.register(icons);
         return true;
       }
       if (!register()) {
         var i = setInterval(function() { if (register()) clearInterval(i); }, 100);
         setTimeout(function() { clearInterval(i); }, 10000);
       }
     })()"))])
```

### JavaScript (Build-Time Bundling)

```javascript
// icons.js — bundle with esbuild
import { wallet, plus, settings, check, calendar } from '@gersak/ty/icons/lucide'
window.tyIcons.register({ wallet, plus, settings, check, calendar })
```

```bash
esbuild icons.js --bundle --minify --format=iife --outfile=static/icons.js
```

Then load in your template:
```html
<script defer src="/static/icons.js"></script>
```

## Complete Form Example

```html
<div class="ty-elevated p-6 rounded-lg max-w-lg mx-auto"
     data-signals="{txDesc: '', txAmount: '', txType: 'expense', txCategory: '', txDate: ''}">

  <h2 class="ty-text++ text-xl mb-4">Add Transaction</h2>

  <!-- Type toggle -->
  <div class="flex gap-2 mb-4">
    <ty-tag size="md"
      data-attr:flavor="$txType === 'expense' ? 'danger' : 'neutral'"
      data-on:click="$txType = 'expense'">
      Expense
    </ty-tag>
    <ty-tag size="md"
      data-attr:flavor="$txType === 'income' ? 'success' : 'neutral'"
      data-on:click="$txType = 'income'">
      Income
    </ty-tag>
  </div>

  <!-- Form fields -->
  <ty-input data-bind="txDesc" label="Description" placeholder="What was it for?"></ty-input>

  <ty-input data-bind="txAmount" label="Amount" type="currency" currency="EUR"></ty-input>

  <ty-date-picker data-bind="txDate" label="Date" placeholder="Pick a date"></ty-date-picker>

  <ty-dropdown data-bind="txCategory" label="Category" placeholder="Select category">
    <ty-option value="food">Food</ty-option>
    <ty-option value="transport">Transport</ty-option>
    <ty-option value="utilities">Utilities</ty-option>
  </ty-dropdown>

  <!-- Submit -->
  <ty-button flavor="primary" class="w-full mt-4"
    data-on:click="@post('/api/transactions/add')">
    Add Transaction
  </ty-button>

  <!-- Server updates this -->
  <div id="result" class="mt-2"></div>
</div>
```

## Wizard Pattern

Multi-step flows with signals controlling the active step:

```html
<div data-signals="{wizardStep: 'welcome', wizardCompleted: ''}">

  <ty-wizard data-attr:active="$wizardStep"
             data-attr:completed="$wizardCompleted">

    <ty-step id="welcome" label="Welcome">
      <ty-input data-bind="userName" label="Your name"></ty-input>
      <ty-button flavor="primary"
        data-on:click="$wizardStep = 'preferences'; $wizardCompleted = 'welcome'">
        Next
      </ty-button>
    </ty-step>

    <ty-step id="preferences" label="Preferences">
      <ty-dropdown data-bind="currency" label="Currency">
        <ty-option value="EUR">€ EUR</ty-option>
        <ty-option value="USD">$ USD</ty-option>
      </ty-dropdown>
      <ty-button flavor="primary"
        data-on:click="$wizardStep = 'done'; $wizardCompleted = 'welcome,preferences'">
        Finish
      </ty-button>
    </ty-step>

    <ty-step id="done" label="Done">
      <p class="ty-text">All set, <span data-text="$userName"></span>!</p>
    </ty-step>

  </ty-wizard>
</div>
```

## Tauri / Mobile

Ty + Datastar works in Tauri apps, including Android. One caveat:

**Android WebView strips POST bodies** from `tauri.localhost` requests (a [known Android bug](https://issues.chromium.org/issues/40502017) since 2018).

**Development workaround:** Use ngrok to tunnel your local server:

```bash
# Terminal 1: Start your server
clj -M:dev

# Terminal 2: Tunnel
ngrok http 3000

# Terminal 3: Point Tauri at the tunnel
# Update src-tauri/tauri.android.conf.json devUrl to ngrok URL
cargo tauri android dev --config src-tauri/tauri.android.conf.json
```

**Production:** Point the Tauri app at your real HTTPS server. POST bodies work natively over HTTPS.

## Styling

Same rule as everywhere: **Ty for colors, Tailwind/CSS for everything else.**

```html
<!-- ✅ Good -->
<div class="ty-elevated p-6 rounded-lg">
  <h2 class="ty-text++ text-xl font-bold">Title</h2>
  <p class="ty-text-">Subtitle</p>
</div>

<!-- ❌ Bad — mixing color systems -->
<div class="bg-blue-500 ty-elevated">Don't</div>
```

See `CSS_GUIDE.md` for the complete design system reference.
