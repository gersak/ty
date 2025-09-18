(ns ty.demo.views.inputs
  "Demonstrates input components with enhanced numeric formatting"
  (:require
   [ty.i18n :as i18n]
   [ty.layout :as layout]))

(defn basic-input-demos []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "Basic Input Types"]
   [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
    [:div.space-y-4
     [:h4.font-medium "Simple Inputs"]
     [:div.space-y-3
      [:ty-input {:type "text"
                  :placeholder "Enter your name"}]
      [:ty-input {:type "email"
                  :label "Email Address"
                  :placeholder "you@example.com"}]
      [:ty-input {:type "password"
                  :label "Password"
                  :required true
                  :placeholder "Enter password"}]
      [:ty-input {:type "text"
                  :label "Disabled Field"
                  :disabled true
                  :value "Cannot edit this"}]]]

    [:div.space-y-4
     [:h4.font-medium "Input Types"]
     [:div.space-y-3
      [:ty-input {:type "text"
                  :label "Text"
                  :placeholder "Text input"}]
      [:ty-input {:type "email"
                  :label "Email"
                  :placeholder "email@example.com"}]
      [:ty-input {:type "number"
                  :label "Number"
                  :placeholder "123"}]
      [:ty-input {:type "search"
                  :label "Search"
                  :placeholder "Search..."}]]]]])

(defn numeric-formatting-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "🔢 Enhanced Numeric Formatting"]
   [:p.text-sm.ty-text- "Type numbers and blur to see automatic formatting. No parsing - uses shadow values!"]

   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-6
    [:div.space-y-4
     [:h4.font-medium "Number Formatting"]
     [:div.space-y-3
      [:ty-input {:type "number"
                  :label "Basic Number"
                  :placeholder "Enter a number"
                  :value "1234.567"
                  :precision "2"}]
      [:ty-input {:type "number"
                  :label "Croatian Locale"
                  :locale "hr"
                  :precision "2"
                  :value "9876.54"
                  :placeholder "Unesite broj"}]
      [:ty-input {:type "number"
                  :label "High Precision"
                  :precision "4"
                  :value "3.14159"
                  :placeholder "Pi value"}]]]

    [:div.space-y-4
     [:h4.font-medium "Special Numeric Types"]
     [:div.space-y-3
      [:ty-input {:type "percent"
                  :label "Percentage"
                  :precision "1"
                  :value "0.158"
                  :placeholder "Enter decimal (0.15 = 15%)"}]
      [:ty-input {:type "compact"
                  :label "Compact Notation"
                  :value "1234567"
                  :placeholder "Large numbers"}]]]]])

(defn currency-formatting-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "💰 Currency Formatting"]
   [:p.text-sm.ty-text- "Automatic currency symbols and locale-aware formatting."]

   [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-4
    [:ty-input {:type "currency"
                :currency "USD"
                :locale "en-US"
                :label "US Dollar"
                :value "2500.99"
                :placeholder "Enter price"}]
    [:ty-input {:type "currency"
                :currency "EUR"
                :locale "de"
                :label "Euro (German)"
                :value "3456.78"
                :placeholder "Preis eingeben"}]
    [:ty-input {:type "currency"
                :currency "HRK"
                :locale "hr"
                :label "Croatian Kuna"
                :value "12345.67"
                :placeholder "Unesite cijenu"}]
    [:ty-input {:type "currency"
                :currency "JPY"
                :locale "ja"
                :precision "0"
                :label "Japanese Yen"
                :value "150000"
                :placeholder "価格を入力"}]
    [:ty-input {:type "currency"
                :currency "GBP"
                :locale "en-GB"
                :label "British Pound"
                :value "789.12"
                :placeholder "Enter amount"}]
    [:ty-input {:type "currency"
                :currency "CAD"
                :locale "en-CA"
                :label "Canadian Dollar"
                :value "1999.99"
                :placeholder "Enter price"}]]])

(defn error-handling-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "⚠️ Error Handling & Validation"]
   [:p.text-sm.ty-text- "User-controlled error states with clean styling."]

   [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
    [:div.space-y-4
     [:h4.font-medium "Error States"]
     [:div.space-y-3
      [:ty-input {:type "number"
                  :label "Invalid Amount"
                  :error "Please enter a valid number"
                  :value "abc"
                  :placeholder "Enter number"}]
      [:ty-input {:type "currency"
                  :currency "USD"
                  :label "Required Field"
                  :required true
                  :error "This field is required"
                  :placeholder "Enter price"}]
      [:ty-input {:type "email"
                  :label "Invalid Email"
                  :error "Please enter a valid email address"
                  :value "invalid-email"
                  :placeholder "your@email.com"}]]]

    [:div.space-y-4
     [:h4.font-medium "Interactive Error Demo"]
     [:div.space-y-3
      [:ty-input {:id "toggle-error-demo"
                  :type "currency"
                  :currency "USD"
                  :label "Price Field"
                  :value "123.45"
                  :placeholder "Enter price"}]
      [:ty-button {:flavor "primary"
                   :on {:click #(let [input (.getElementById js/document "toggle-error-demo")
                                      has-error (.hasAttribute input "error")]
                                  (if has-error
                                    (.removeAttribute input "error")
                                    (.setAttribute input "error" "Custom validation error")))}}
       "Toggle Error State"]]]]])

(defn external-value-demo []
  (letfn [(process [^js e]
            (let [log (.getElementById js/document "event-log")
                  timestamp (.toLocaleTimeString (js/Date.))
                  detail (.-detail e)
                  entry (str "[" timestamp "] CHANGE: "
                             "value=" (.-value detail)
                             " formatted=" (.-formattedValue detail))]
              (set! (.-textContent log) (str entry "\n" (.-textContent log)))
              (set! (.-scrollTop log) 0)))]
    [:div.space-y-6
     [:h3.text-lg.font-semibold "🔄 External Value Changes"]
     [:p.text-sm.ty-text- "Test shadow value synchronization with programmatic updates."]
     [:div.space-y-4
      [:ty-input
       {:id "external-demo"
        :type "currency"
        :currency "USD"
        :label "Programmatically Updated Price"
        :value "100.00"
        :placeholder "Price will be updated externally"
        :on {:change process}}]

      [:div.flex.flex-wrap.gap-2
       [:ty-button {:flavor "success"
                    :size "sm"
                    :on {:click #(.setAttribute (.getElementById js/document "external-demo") "value" "250.75")}}
        "Set $250.75"]
       [:ty-button {:flavor "primary"
                    :size "sm"
                    :on {:click #(.setAttribute (.getElementById js/document "external-demo") "value" "1000")}}
        "Set $1,000"]
       [:ty-button {:flavor "secondary"
                    :size "sm"
                    :on {:click #(.setAttribute (.getElementById js/document "external-demo") "value" "99.99")}}
        "Set $99.99"]
       [:ty-button {:flavor "neutral"
                    :size "sm"
                    :on {:click #(.setAttribute (.getElementById js/document "external-demo") "value" "")}}
        "Clear"]]

      [:div.mt-4
       [:h5.font-medium.text-sm "Event Log:"]
       [:pre#event-log.p-3.rounded.font-mono.text-xs.h-24.overflow-y-auto.transition-colors
        {:class [:ty-bg :ty-text-success]}
        "Type in the input above or click buttons to see events..."]]]]))

(defn comprehensive-form-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "💼 Professional Invoice Form"]
   [:p.text-sm.ty-text- "Real-world example showcasing various numeric input types in context."]

   [:div.ty-surface-elevated.ty-elevated.p-6.rounded-lg
    [:div.grid.grid-cols-1.lg:grid-cols-2.gap-6
     ;; Client Information
     [:div.space-y-4
      [:h4.font-medium.border-b.ty-border.pb-2 "Client Information"]
      [:ty-input {:type "text"
                  :label "Company Name"
                  :required true
                  :placeholder "Acme Corp"}]
      [:ty-input {:type "email"
                  :label "Email"
                  :required true
                  :placeholder "billing@acme.com"}]
      [:ty-input {:type "text"
                  :label "Address"
                  :placeholder "123 Business St"}]]

     ;; Invoice Details  
     [:div.space-y-4
      [:h4.font-medium.border-b.ty-border.pb-2 "Invoice Details"]
      [:ty-input {:type "text"
                  :label "Invoice Number"
                  :value "INV-001"
                  :placeholder "INV-001"}]
      [:ty-input {:type "date"
                  :label "Invoice Date"
                  :placeholder "Select date"}]
      [:ty-input {:type "percent"
                  :label "Tax Rate"
                  :value "0.08"
                  :precision "2"
                  :placeholder "0.08 = 8%"}]]]

    ;; Line Items with Numeric Formatting
    [:div.mt-6.space-y-4
     [:h4.font-medium.border-b.ty-border.pb-2 "Line Items"]
     [:div.grid.grid-cols-1.md:grid-cols-12.gap-4.items-end
      [:div.md:col-span-5
       [:ty-input {:type "text"
                   :label "Description"
                   :placeholder "Consulting services"}]]
      [:div.md:col-span-2
       [:ty-input {:type "number"
                   :label "Quantity"
                   :value "1"
                   :placeholder "1"}]]
      [:div.md:col-span-3
       [:ty-input {:type "currency"
                   :currency "USD"
                   :label "Unit Price"
                   :value "150.00"
                   :placeholder "Price per unit"}]]
      [:div.md:col-span-2
       [:ty-input {:type "currency"
                   :currency "USD"
                   :label "Total"
                   :value "150.00"
                   :disabled true}]]]

     [:div.grid.grid-cols-1.md:grid-cols-12.gap-4.items-end
      [:div.md:col-span-5
       [:ty-input {:type "text"
                   :label "Description"
                   :placeholder "Website development"}]]
      [:div.md:col-span-2
       [:ty-input {:type "number"
                   :label "Quantity"
                   :value "40"
                   :placeholder "Hours"}]]
      [:div.md:col-span-3
       [:ty-input {:type "currency"
                   :currency "USD"
                   :label "Unit Price"
                   :value "75.00"
                   :placeholder "Hourly rate"}]]
      [:div.md:col-span-2
       [:ty-input {:type "currency"
                   :currency "USD"
                   :label "Total"
                   :value "3000.00"
                   :disabled true}]]]]

    ;; Summary with formatted totals
    [:div.mt-6.pt-4.border-t.ty-border
     [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
      [:div] ;; Spacer
      [:div.space-y-3
       [:div.flex.justify-between.items-center
        [:span.font-medium "Subtotal:"]
        [:ty-input {:type "currency"
                    :currency "USD"
                    :value "3150.00"
                    :disabled true
                    :size "sm"}]]
       [:div.flex.justify-between.items-center
        [:span.font-medium "Tax (8%):"]
        [:ty-input {:type "currency"
                    :currency "USD"
                    :value "252.00"
                    :disabled true
                    :size "sm"}]]
       [:div.flex.justify-between.items-center.border-t.ty-border.pt-2
        [:span.font-bold.text-lg "Total:"]
        [:ty-input {:type "currency"
                    :currency "USD"
                    :value "3402.00"
                    :disabled true
                    :flavor "primary"}]]]]]]])

(defn size-variants-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "Size Variants"]
   [:div.space-y-3
    [:ty-input {:size "xs"
                :label "Extra Small"
                :placeholder "size=xs"}]
    [:ty-input {:size "sm"
                :label "Small"
                :placeholder "size=sm"}]
    [:ty-input {:size "md"
                :label "Medium (default)"
                :placeholder "size=md"}]
    [:ty-input {:size "lg"
                :label "Large"
                :placeholder "size=lg"}]
    [:ty-input {:size "xl"
                :label "Extra Large"
                :placeholder "size=xl"}]]])

(defn flavor-variants-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "Semantic Flavors"]
   [:div.space-y-3
    [:ty-input {:flavor "neutral"
                :label "Neutral (default)"
                :placeholder "Default styling"}]
    [:ty-input {:flavor "primary"
                :label "Primary Field"
                :placeholder "Primary field"}]
    [:ty-input {:flavor "success"
                :label "Valid Input"
                :placeholder "This looks good!"}]
    [:ty-input {:flavor "danger"
                :label "Error State"
                :placeholder "Something went wrong"}]
    [:ty-input {:flavor "warning"
                :label "Warning State"
                :placeholder "Be careful here"}]
    [:ty-input {:flavor "secondary"
                :label "Secondary Field"
                :placeholder "Secondary field"}]]])

(defn container-aware-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "Container-Aware Inputs"]
   [:p.text-sm.ty-text- "These inputs adapt to their container size using the layout context."]

   ;; Resizable container demo
   [:ty-resize-observer#input-container.ty-bg-primary-.border-2.ty-border-primary.rounded-lg.p-4.resize.overflow-auto
    {:style {:min-width "200px"
             :min-height "200px"
             :width "400px"
             :height "250px"}}
    (layout/with-resize-observer "input-container"
      [:div.space-y-4
       [:div.text-center
        [:h4.font-semibold "Resizable Container"]
        [:p.text-sm.ty-text- "Container: " (layout/container-width) "×" (layout/container-height) "px"]
        [:p.text-xs.ty-text-- "Breakpoint: " (name (layout/container-breakpoint))]]

       [:div.space-y-3
        [:ty-input {:label "Responsive Input"
                    :placeholder "I know my container size"
                    :size (cond
                            (layout/breakpoint>= :lg) "lg"
                            (layout/breakpoint>= :md) "md"
                            :else "sm")}]
        [:ty-input {:label "Dynamic Sizing"
                    :placeholder (str "Container is " (name (layout/container-breakpoint)))
                    :flavor (cond
                              (layout/breakpoint>= :lg) "success"
                              (layout/breakpoint>= :md) "primary"
                              :else "warning")}]]])]])

(defn form-layout-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "Professional Form Layouts"]
   [:p.text-sm.ty-text- "Clean, elegant forms with proper spacing and visual hierarchy."]

   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8
    ;; Contact form with refined spacing like toddler
    [:div.ty-surface-elevated.ty-elevated.p-6.rounded-lg
     [:h4.font-medium.mb-6 "Contact Information"]
     [:div.space-y-5
      [:div.grid.grid-cols-1.sm:grid-cols-2.gap-4
       [:ty-input {:type "text"
                   :label "First Name"
                   :required true
                   :placeholder "John"}]
       [:ty-input {:type "text"
                   :label "Last Name"
                   :required true
                   :placeholder "Doe"}]]
      [:ty-input {:type "text"
                  :label "Address"
                  :placeholder "123 Main Street"}]
      [:ty-input {:type "text"
                  :label "City"
                  :placeholder "New York"}]
      [:ty-input {:type "text"
                  :label "Country"
                  :placeholder "United States"}]
      [:ty-input {:type "date"
                  :label "Date of Birth"
                  :placeholder "mm/dd/yyyy"}]]]

    ;; Login form with elegant styling
    [:div.ty-surface-elevated.ty-elevated.p-6.rounded-lg
     [:h4.font-medium.mb-6 "Account Access"]
     [:div.space-y-5
      [:ty-input {:type "email"
                  :label "Email Address"
                  :required true
                  :placeholder "your@email.com"}]
      [:ty-input {:type "password"
                  :label "Password"
                  :required true
                  :placeholder "Enter password"}]
      [:ty-input {:type "password"
                  :label "Confirm Password"
                  :required true
                  :placeholder "Confirm password"}]]]]])

(defn event-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "Event Handling"]
   [:p.text-sm.ty-text- "Check the browser console to see custom events fired by inputs."]
   [:div.space-y-3
    [:ty-input {:label "Input Events"
                :placeholder "Type here and check console"
                :on {:input #(js/console.log "Input event:" (.-detail %))
                     :change #(js/console.log "Change event:" (.-detail %))}}]
    [:ty-input {:label "Form Validation Demo"
                :type "email"
                :required true
                :placeholder "Enter a valid email"
                :flavor "primary"}]]])

(defn form-association-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "🔗 Form Association & HTMX Integration"]
   [:p.text-sm.ty-text- "Test form participation using ElementInternals API for HTMX compatibility."]

   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-6
    ;; Test form with FormData extraction
    [:div.space-y-4
     [:h4.font-medium "FormData Test"]
     [:p.text-xs.ty-text-- "Tests that ty-input values appear in FormData (required for HTMX)"]

     [:form#form-test.space-y-3.p-4.border.ty-border.rounded
      [:ty-input {:name "username"
                  :type "text"
                  :label "Username"
                  :value "john_doe"
                  :placeholder "Enter username"}]
      [:ty-input {:name "email"
                  :type "email"
                  :label "Email"
                  :value "john@example.com"
                  :placeholder "your@email.com"}]
      [:ty-input {:name "price"
                  :type "currency"
                  :currency "USD"
                  :label "Price"
                  :value "1234.56"
                  :placeholder "Enter price"}]
      [:ty-input {:name "discount"
                  :type "percent"
                  :label "Discount Rate"
                  :value "0.15"
                  :precision "2"
                  :placeholder "0.15 = 15%"}]
      [:ty-input {:name "quantity"
                  :type "number"
                  :label "Quantity"
                  :value "42"
                  :placeholder "Enter quantity"}]

      [:ty-button {:flavor "primary"
                   :on {:click #(let [form (.getElementById js/document "form-test")
                                      form-data (js/FormData. form)
                                      results (.getElementById js/document "formdata-results")
                                      entries ^js (js/Array.from (.entries form-data))

                                      ;; 🔍 DEBUGGING INFO with Console Logging
                                      inputs ^js (.querySelectorAll js/document "ty-input")]

                                  ;; Log everything to console first
                                  (.log js/console "=== FormData Debug ===")
                                  (.log js/console "Form found:" form)
                                  (.log js/console "FormData entries:" entries)
                                  (.log js/console "Found" (.-length inputs) "ty-input elements:")

                                  (set! (.-innerHTML results)
                                        (str "<strong>FormData contents:</strong><br>"
                                             (if (> (.-length entries) 0)
                                               (.join (.map entries
                                                            (fn [[name value]]
                                                              (str name " = " value " (" (type value) ")")))
                                                      "<br>")
                                               "❌ NO FORMDATA ENTRIES FOUND"))))}}
       "Extract FormData + Debug"]

      [:div#formdata-results.mt-4.p-3.rounded.font-mono.text-xs
       {:class [:ty-bg-neutral- :ty-text-]}
       "Click 'Extract FormData' to see form values..."]]]

    ;; Property access test  
    [:div.space-y-4
     [:h4.font-medium "Property Access Test"]
     [:p.text-xs.ty-text-- "Tests .value property access (standard DOM behavior)"]

     [:div.space-y-3.p-4.border.ty-border.rounded
      [:ty-input {:id "prop-test-input"
                  :name "test-value"
                  :type "currency"
                  :currency "USD"
                  :label "Test Input"
                  :value "999.99"
                  :placeholder "Enter amount"}]

      [:ty-button {:flavor "success"
                   :on {:click #(let [input (.getElementById js/document "prop-test-input")
                                      value (.-value input)
                                      results (.getElementById js/document "property-results")]
                                  (set! (.-innerHTML results)
                                        (str "<strong>Property Access:</strong><br>"
                                             "input.value = " value " (" (type value) ")<br>"
                                             "input.getAttribute('value') = " (.getAttribute input "value") "<br>"
                                             "input.getAttribute('name') = " (.getAttribute input "name"))))}}
       "Read .value Property"]

      [:div.flex.gap-2.flex-wrap
       [:ty-button {:flavor "warning"
                    :size "sm"
                    :on {:click #(set! (.-value (.getElementById js/document "prop-test-input")) 500.00)}}
        "Set to 500"]
       [:ty-button {:flavor "info"
                    :size "sm"
                    :on {:click #(.setAttribute (.getElementById js/document "prop-test-input") "value" "750.25")}}
        "Set Attr to 750.25"]
       [:ty-button {:flavor "neutral"
                    :size "sm"
                    :on {:click #(set! (.-value (.getElementById js/document "prop-test-input")) nil)}}
        "Clear"]]

      [:div#property-results.mt-4.p-3.rounded.font-mono.text-xs
       {:class [:ty-bg-neutral- :ty-text-]}
       "Click 'Read .value Property' to see current values..."]]]]

   ;; HTMX simulation
   [:div.mt-6.space-y-4
    [:h4.font-medium "HTMX Simulation"]
    [:p.text-xs.ty-text-- "Simulates how HTMX would serialize the form"]

    [:form#htmx-test.space-y-3.p-4.border.ty-border-primary.rounded
     {:data-testform "true"}
     [:ty-input {:name "customer"
                 :type "text"
                 :label "Customer Name"
                 :value "Acme Corp"
                 :required true}]
     [:ty-input {:name "amount"
                 :type "currency"
                 :currency "USD"
                 :label "Invoice Amount"
                 :value "1500.00"}]
     [:ty-input {:name "tax_rate"
                 :type "percent"
                 :label "Tax Rate"
                 :value "0.08"
                 :precision "3"}]

     [:ty-button {:flavor "secondary"
                  :on {:click #(let [form (.getElementById js/document "htmx-test")
                                     form-data (js/FormData. form)
                                     url-params (js/URLSearchParams. form-data)
                                     results (.getElementById js/document "htmx-results")]
                                 (set! (.-innerHTML results)
                                       (str "<strong>HTMX would send:</strong><br>"
                                            "Content-Type: application/x-www-form-urlencoded<br><br>"
                                            "<code>" (.toString url-params) "</code><br><br>"
                                            "<strong>Parsed data:</strong><br>"
                                            (let [entries (js/Array.from (.entries form-data))]
                                              (.join (.map entries
                                                           (fn [[name value]]
                                                             (str "• " name ": " value)))
                                                     "<br>")))))}}
      "Simulate HTMX POST"]

     [:div#htmx-results.mt-4.p-3.rounded.font-mono.text-xs
      {:class [:ty-bg-info- :ty-text-]}
      "Click 'Simulate HTMX POST' to see what would be sent to server..."]]]])

(defn form-reset-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "🔄 Form Reset & Lifecycle"]
   [:p.text-sm.ty-text- "Test form reset behavior with various ty-components. Watch browser console for lifecycle logs."]

   [:div.space-y-6
    ;; Reset test form
    [:div.space-y-4
     [:h4.font-medium "Form Reset Test"]
     [:p.text-xs.ty-text-- "Fill out the form, then test reset behavior. Components should return to initial values."]

     [:form#reset-test-form.space-y-4.p-6.border-2.ty-border-primary.rounded-lg
      {:class [:ty-bg-primary-]}

      ;; Basic inputs with different initial values
      [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
       [:ty-input {:name "name"
                   :type "text"
                   :label "Name"
                   :value "John Doe" ; Initial value
                   :placeholder "Enter your name"}]
       [:ty-input {:name "email"
                   :type "email"
                   :label "Email"
                   ; No initial value - should reset to empty
                   :placeholder "your@email.com"}]]

      ;; Numeric inputs with initial values
      [:div.grid.grid-cols-1.md:grid-cols-3.gap-4
       [:ty-input {:name "price"
                   :type "currency"
                   :currency "USD"
                   :label "Price"
                   :value "100.00" ; Should reset to this
                   :placeholder "Enter price"}]
       [:ty-input {:name "discount"
                   :type "percent"
                   :label "Discount"
                   :value "0.10" ; Should reset to 10%
                   :precision "2"
                   :placeholder "Enter discount"}]
       [:ty-input {:name "quantity"
                   :type "number"
                   :label "Quantity"
                   :value "5" ; Should reset to 5
                   :placeholder "Enter quantity"}]]

      ;; Dropdown and multiselect (if available in current form)
      [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
       [:ty-input {:name "phone"
                   :type "text"
                   :label "Phone"
                   :placeholder "+1 (555) 123-4567"}]
       [:ty-input {:name "website"
                   :type "text"
                   :label "Website"
                   :value "https://example.com" ; Should reset to this
                   :placeholder "https://yoursite.com"}]]

      ;; Disabled input (should respect initial disabled state)
      [:ty-input {:name "readonly-field"
                  :type "text"
                  :label "Read-only Field"
                  :value "Cannot be changed"
                  :disabled true}]

      ;; Form controls
      [:div.flex.gap-3.pt-4.border-t.ty-border
       [:ty-button {:type "reset"
                    :flavor "warning"
                    :on {:click #(let [form (.getElementById js/document "reset-test-form")]
                                   ;; Browser's native form reset
                                   (.log js/console "🔄 Triggering form.reset()")
                                   (.reset form))}}
        "🔄 Reset Form (Native)"]

       [:ty-button {:flavor "info"
                    :type "button"
                    :on {:click #(let [form (.getElementById js/document "reset-test-form")
                                       inputs (.querySelectorAll form "ty-input[name]")]
                                   (.log js/console "📋 Current form values:")
                                   (.forEach inputs
                                             (fn [input]
                                               (.log js/console
                                                     (str "  " (.getAttribute input "name") " = " (.-value input))))))}}
        "📋 Log Current Values"]

       [:ty-button {:flavor "success"
                    :type "button"
                    :on {:click #(let [form (.getElementById js/document "reset-test-form")
                                       inputs (.querySelectorAll form "ty-input[name]")]
                                   ;; Fill form with test data
                                   (.log js/console "📝 Filling form with test data...")
                                   (.forEach inputs
                                             (fn [input]
                                               (let [name (.getAttribute input "name")]
                                                 (case name
                                                   "name" (set! (.-value input) "Jane Smith")
                                                   "email" (set! (.-value input) "jane@example.com")
                                                   "price" (set! (.-value input) "250.99")
                                                   "discount" (set! (.-value input) "0.25")
                                                   "quantity" (set! (.-value input) "10")
                                                   "phone" (set! (.-value input) "+1 (555) 999-8888")
                                                   "website" (set! (.-value input) "https://newsite.com")
                                                   nil)))))}}
        "📝 Fill Test Data"]]]]

    ;; Reset status display
    [:div.space-y-4
     [:h4.font-medium "Reset Status Monitor"]
     [:p.text-xs.ty-text-- "Shows form reset events and component states in real-time."]

     [:div#reset-status.p-4.rounded.font-mono.text-xs.space-y-1
      {:class [:ty-bg-neutral- :ty-text-]}
      [:div "🎯 Reset Status Monitor"]
      [:div "• Fill the form above with some data"]
      [:div "• Click 'Reset Form' to test reset behavior"]
      [:div "• Check browser console for detailed logs"]
      [:div "• Watch components return to initial values"]]

     ;; Live form data display
     [:div.space-y-2
      [:h5.font-medium.text-sm "Live Form Values:"]
      [:div#live-form-data.p-3.rounded.font-mono.text-xs.max-h-40.overflow-y-auto
       {:class [:ty-bg-success- :ty-text-]}
       "Click 'Log Current Values' to see live form data..."]]]

    ;; Reset behavior explanation
    [:div.p-4.rounded-lg
     {:class [:ty-bg-info-]}
     [:h4.font-medium.ty-text-info-strong "🔍 How Form Reset Works"]
     [:div.text-sm.ty-text-info.space-y-2.mt-2
      [:p "1. Browser calls " [:code.ty-bg-info.px-1.rounded "formResetCallback()"] " on each form-associated element"]
      [:p "2. Shim restores components to their initial attribute values"]
      [:p "3. ElementInternals form values are updated"]
      [:p "4. Components re-render with initial state"]
      [:p "5. Change events are dispatched with " [:code.ty-bg-info.px-1.rounded "reason: 'form-reset'"]]]]

    ;; Individual component reset test
    [:div.space-y-4
     [:h4.font-medium "Individual Component Reset"]
     [:p.text-xs.ty-text-- "Test reset behavior on individual components outside of form context."]

     [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
      [:div.p-4.border.ty-border.rounded
       [:ty-input {:id "individual-reset-test"
                   :name "test-input"
                   :type "currency"
                   :currency "USD"
                   :label "Test Input"
                   :value "42.50" ; Initial value
                   :placeholder "Enter amount"}]

       [:div.flex.gap-2.mt-3
        [:ty-button {:size "sm"
                     :flavor "warning"
                     :on {:click #(let [input (.getElementById js/document "individual-reset-test")]
                                    ;; Manually trigger reset (no form context)
                                    (.log js/console "🔄 Manual component reset")
                                    (when (.-formResetCallback input)
                                      (.call (.-formResetCallback input) input)))}}
         "Reset Component"]
        [:ty-button {:size "sm"
                     :flavor "primary"
                     :on {:click #(set! (.-value (.getElementById js/document "individual-reset-test")) "999.99")}}
         "Change to $999.99"]]]

      [:div.p-3.rounded.text-xs
       {:class [:ty-bg-neutral- :ty-text-]}
       [:div "💡 Individual Reset Notes:"]
       [:div "• Components store initial state on creation"]
       [:div "• formResetCallback() can be called manually"]
       [:div "• Reset works even outside form context"]
       [:div "• Check console for reset lifecycle logs"]]]]]])

(defn checkbox-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "☑️ User Profile Settings"]
   [:p.text-sm.ty-text- "Clean, minimal checkboxes for user preferences and account settings."]

   ;; Simple, realistic user profile form
   [:div.ty-elevated.p-6.rounded-lg
    [:form#profile-settings.space-y-6

     ;; Notifications section
     [:div
      [:h4.font-medium.mb-3 "Email Notifications"]
      [:div.space-y-3
       [:ty-input {:type "checkbox"
                   :label "Product updates and announcements"
                   :name "product_updates"
                   :value "enabled"
                   :flavor "neutral"
                   :checked true}]
       [:ty-input {:type "checkbox"
                   :label "Weekly newsletter"
                   :name "newsletter"
                   :value "enabled"
                   :flavor "neutral"}]
       [:ty-input {:type "checkbox"
                   :label "Marketing promotions"
                   :name "marketing"
                   :value "enabled"
                   :flavor "neutral"}]]]

     [:hr.ty-border]

     ;; Privacy section
     [:div
      [:h4.font-medium.mb-3 "Privacy & Security"]
      [:div.space-y-3
       [:ty-input {:type "checkbox"
                   :label "Make profile public"
                   :name "profile_public"
                   :value "enabled"
                   :flavor "neutral"}]
       [:ty-input {:type "checkbox"
                   :label "Show online status"
                   :name "show_online"
                   :value "enabled"
                   :flavor "neutral"
                   :checked true}]
       [:ty-input {:type "checkbox"
                   :label "Allow contact from other users"
                   :name "allow_contact"
                   :value "enabled"
                   :flavor "neutral"
                   :checked true}]]]

     [:hr.ty-border]

     ;; Account preferences
     [:div
      [:h4.font-medium.mb-3 "Account Preferences"]
      [:div.space-y-3
       [:ty-input {:type "checkbox"
                   :label "Auto-save drafts"
                   :name "auto_save"
                   :value "enabled"
                   :flavor "neutral"
                   :checked true}]
       [:ty-input {:type "checkbox"
                   :label "Remember login for 30 days"
                   :name "remember_login"
                   :value "enabled"
                   :flavor "neutral"}]]]

     ;; Form actions
     [:div.flex.gap-3.pt-4
      [:ty-button {:flavor "primary"
                   :on {:click #(let [form (.getElementById js/document "profile-settings")
                                      form-data (js/FormData. form)
                                      entries (js/Array.from (.entries form-data))
                                      results (.getElementById js/document "settings-results")]
                                  (set! (.-innerHTML results)
                                        (str "<strong>Saved Settings:</strong><br>"
                                             (if (> (.-length entries) 0)
                                               (.join (.map entries
                                                            (fn [[name value]]
                                                              (str "• " (.replace name "_" " ") ": " value)))
                                                      "<br>")
                                               "No preferences enabled"))))}}
       "Save Changes"]
      [:ty-button {:flavor "neutral"
                   :on {:click #(let [form (.getElementById js/document "profile-settings")]
                                  (.reset form))}}
       "Reset"]]

     ;; Results display
     [:div#settings-results.mt-4.p-4.rounded.ty-bg-neutral-.ty-text-.font-mono.text-xs
      "Click 'Save Changes' to see your preferences..."]

     ;; Event Testing
     [:div.mt-6.p-4.border.ty-border.rounded
      [:h5.font-medium.mb-2 "Event Testing"]
      [:p.text-xs.ty-text--.mb-3 "Test if checkboxes respond to changes in real-time:"]
      [:ty-input {:type "checkbox"
                  :label "Test checkbox - watch console for events"
                  :name "test_checkbox"
                  :value "test"
                  :flavor "neutral"
                  :on {:change #(js/console.log "✅ Checkbox changed!" (.-detail %))
                       :input #(js/console.log "📝 Checkbox input!" (.-detail %))}}]]]]])

(defn textarea-demo []
  [:div
   [:h3.text-xl.font-semibold.mb-4.ty-text "🗒️ Textarea Components (NEW!)"]
   [:p.ty-text-.mb-6
    "Auto-resizing textareas with HTMX compatibility and form integration. Height adjusts automatically based on content."]

   ;; Basic auto-resize demo
   [:div.space-y-6
    [:div
     [:h4.font-medium.ty-text+ "Basic Auto-Resize"]
     [:div.space-y-3
      [:ty-textarea
       {:label "Description"
        :name "description"
        :placeholder "Start typing... The textarea will grow automatically as you add more content. Try adding multiple lines!"
        :size "md"}]
      [:p.text-sm.ty-text--
       "✅ HTMX Compatible: Includes proper 'name' attribute for form submission"]]]

    ;; Size variants
    [:div
     [:h4.font-medium.ty-text+ "Size Variants"]
     [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-4
      [:ty-textarea
       {:label "Small"
        :name "small_text"
        :placeholder "Small textarea..."
        :size "sm"}]
      [:ty-textarea
       {:label "Medium (Default)"
        :name "medium_text"
        :placeholder "Medium textarea..."
        :size "md"}]
      [:ty-textarea
       {:label "Large"
        :name "large_text"
        :placeholder "Large textarea..."
        :size "lg"}]]]

    ;; Error state and validation
    [:div
     [:h4.font-medium.ty-text+ "Validation & Required Fields"]
     [:div.space-y-4
      [:ty-textarea
       {:label "Comments"
        :name "comments"
        :placeholder "Please share your feedback..."
        :required true
        :rows "3"}]
      [:ty-textarea
       {:label "Error Example"
        :name "error_example"
        :placeholder "This will show an error..."
        :error "This field is required"
        :value ""}]]]

    ;; Form integration demo
    [:div
     [:h4.font-medium.ty-text+ "Form Integration"]
     [:form.space-y-4
      {:onSubmit "event.preventDefault(); console.log('Form data:', new FormData(event.target))"}
      [:ty-textarea
       {:label "Message"
        :name "message"
        :placeholder "Enter your message..."
        :required true}]
      [:button.ty-bg-primary.ty-text-white.px-4.py-2.rounded
       {:type "submit"}
       "Submit Form"]
      [:p.text-sm.ty-text--
       "Check browser console to see form data on submit"]]]

    ;; Long content demo
    [:div
     [:h4.font-medium.ty-text+ "Auto-Resize with Long Content"]
     [:ty-textarea
      {:label "Essay"
       :name "essay"
       :placeholder "Write your essay here..."
       :value "This is a longer piece of text that demonstrates how the textarea automatically resizes to fit content.\n\nIt works with multiple paragraphs and line breaks.\n\nThe height adjusts smoothly as you type or paste content.\n\nTry adding more text to see it grow!"}]]

    ;; Disabled state
    [:div
     [:h4.font-medium.ty-text+ "Disabled State"]
     [:ty-textarea
      {:label "Disabled Textarea"
       :name "disabled_example"
       :placeholder "This is disabled"
       :disabled true
       :value "This textarea is disabled and cannot be edited."}]]

    ;; Manual resize options
    [:div
     [:h4.font-medium.ty-text+ "Manual Resize Options"]
     [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
      [:ty-textarea
       {:label "Auto-Resize (Default)"
        :name "auto_resize"
        :placeholder "Auto-resizes as you type..."
        :value "This textarea automatically adjusts height"}]
      [:ty-textarea
       {:label "Manual Resize Allowed"
        :name "manual_resize"
        :placeholder "You can manually resize this one..."
        :resize "both"
        :value "This textarea can be manually resized by dragging the corner"}]]]]])

(defn view []
  (layout/with-window
    [:div.p-8.max-w-6xl.mx-auto.space-y-8.ty-text-
     [:div
      [:h1.text-3xl.font-bold.mb-4 "Enhanced Input Components"]
      [:p
       "Form inputs with sophisticated numeric formatting, shadow values, and layout integration."]
      [:p.text-sm.ty-text--.mt-2
       "✨ NEW: Auto-formatting on blur, currency support, locale-aware formatting, checkbox support, and HTMX-compatible auto-resizing textareas!"]]

     ;; NEW: Checkbox Demo - put it early to showcase the new feature
     [:div.ty-surface-elevated.ty-elevated.rounded-lg.p-6
      (checkbox-demo)]

     ;; NEW: Textarea Demo - Auto-resizing textareas
     [:div.ty-surface-elevated.ty-elevated.rounded-lg.p-6
      (textarea-demo)]

     ;; Highlight the new numeric formatting capabilities first
     [:div.ty-surface-elevated.ty-elevated.rounded-lg.p-6
      (numeric-formatting-demo)]

     [:div.ty-surface-elevated.ty-elevated.rounded-lg.p-6
      (currency-formatting-demo)]

     [:div.ty-surface-elevated.ty-elevated.rounded-lg.p-6
      (comprehensive-form-demo)]

     [:div.ty-surface-elevated.ty-elevated.rounded-lg.p-6
      (error-handling-demo)]

     [:div.ty-surface-elevated.ty-elevated.rounded-lg.p-6
      (external-value-demo)]

     [:div.ty-surface-elevated.ty-elevated.rounded-lg.p-6
      (event-demo)]

     ;; Keep existing demos but lower priority
     [:div.ty-surface-elevated.ty-elevated.rounded-lg.p-6
      (basic-input-demos)]

     [:div.ty-surface-elevated.ty-elevated.rounded-lg.p-6
      (size-variants-demo)]

     [:div.ty-surface-elevated.ty-elevated.rounded-lg.p-6
      (flavor-variants-demo)]

     [:div.ty-surface-elevated.ty-elevated.rounded-lg.p-6
      (container-aware-demo)]

     [:div.ty-surface-elevated.ty-elevated.rounded-lg.p-6
      (form-layout-demo)]

     ;; 🔗 NEW: Form Association & HTMX Integration Testing
     [:div.ty-surface-elevated.ty-elevated.rounded-lg.p-6
      (form-association-demo)]

     ;; 🔄 NEW: Form Reset & Lifecycle Demo
     [:div.ty-surface-elevated.ty-elevated.rounded-lg.p-6
      (form-reset-demo)]]))


