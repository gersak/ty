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
   [:p.text-sm.text-gray-600.dark:text-gray-400 "Type numbers and blur to see automatic formatting. No parsing - uses shadow values!"]

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
   [:p.text-sm.text-gray-600.dark:text-gray-400 "Automatic currency symbols and locale-aware formatting."]

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
   [:p.text-sm.text-gray-600.dark:text-gray-400 "User-controlled error states with clean styling."]

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
      [:button.px-4.py-2.rounded.transition-colors
       {:class [:bg-blue-500 :hover:bg-blue-600 :text-white :dark:bg-blue-600 :dark:hover:bg-blue-700]
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
     [:p.text-sm.text-gray-600.dark:text-gray-400 "Test shadow value synchronization with programmatic updates."]
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
       [:button.px-3.py-1.rounded.text-sm.transition-colors
        {:class [:bg-green-500.dark:bg-green-600 :hover:bg-green-600 :text-white :dark:bg-green-600 :dark:hover:bg-green-700]
         :on {:click #(.setAttribute (.getElementById js/document "external-demo") "value" "250.75")}}
        "Set $250.75"]
       [:button.px-3.py-1.rounded.text-sm.transition-colors
        {:class [:bg-blue-500 :hover:bg-blue-600 :text-white :dark:bg-blue-600 :dark:hover:bg-blue-700]
         :on {:click #(.setAttribute (.getElementById js/document "external-demo") "value" "1000")}}
        "Set $1,000"]
       [:button.px-3.py-1.rounded.text-sm.transition-colors
        {:class [:bg-purple-500 :hover:bg-purple-600 :text-white :dark:bg-purple-600 :dark:hover:bg-purple-700]
         :on {:click #(.setAttribute (.getElementById js/document "external-demo") "value" "99.99")}}
        "Set $99.99"]
       [:button.px-3.py-1.rounded.text-sm.transition-colors
        {:class [:bg-gray-500 :hover:bg-gray-600 :text-white :dark:bg-gray-600 :dark:hover:bg-gray-700]
         :on {:click #(.setAttribute (.getElementById js/document "external-demo") "value" "")}}
        "Clear"]]

      [:div.mt-4
       [:h5.font-medium.text-sm "Event Log:"]
       [:pre#event-log.p-3.rounded.font-mono.text-xs.h-24.overflow-y-auto.transition-colors
        {:class [:bg-gray-900 :text-green-400 :dark:bg-gray-800 :dark:text-green-300]}
        "Type in the input above or click buttons to see events..."]]]]))

(defn comprehensive-form-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "💼 Professional Invoice Form"]
   [:p.text-sm.text-gray-600.dark:text-gray-400 "Real-world example showcasing various numeric input types in context."]

   [:div.bg-white.dark:bg-gray-800.p-6.rounded-lg.shadow-md
    [:div.grid.grid-cols-1.lg:grid-cols-2.gap-6
     ;; Client Information
     [:div.space-y-4
      [:h4.font-medium.border-b.dark:border-gray-600.pb-2 "Client Information"]
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
      [:h4.font-medium.border-b.dark:border-gray-600.pb-2 "Invoice Details"]
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
     [:h4.font-medium.border-b.dark:border-gray-600.pb-2 "Line Items"]
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
    [:div.mt-6.pt-4.border-t.dark:border-gray-600
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
       [:div.flex.justify-between.items-center.border-t.dark:border-gray-600.pt-2
        [:span.font-bold.text-lg "Total:"]
        [:ty-input {:type "currency"
                    :currency "USD"
                    :value "3402.00"
                    :disabled true
                    :flavor "important"}]]]]]]])

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
    [:ty-input {:flavor "important"
                :label "Important"
                :placeholder "Important field"}]
    [:ty-input {:flavor "positive"
                :label "Valid Input"
                :placeholder "This looks good!"}]
    [:ty-input {:flavor "negative"
                :label "Error State"
                :placeholder "Something went wrong"}]
    [:ty-input {:flavor "exception"
                :label "Warning State"
                :placeholder "Be careful here"}]
    [:ty-input {:flavor "unique"
                :label "Special Field"
                :placeholder "Something unique"}]]])

(defn container-aware-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "Container-Aware Inputs"]
   [:p.text-sm.text-gray-600.dark:text-gray-400 "These inputs adapt to their container size using the layout context."]

   ;; Resizable container demo
   [:ty-resize-observer#input-container.bg-blue-50.dark:bg-blue-900.border-2.border-b.dark:border-gray-600lue-200.dark:border-blue-700.rounded-lg.p-4.resize.overflow-auto
    {:style {:min-width "200px"
             :min-height "200px"
             :width "400px"
             :height "250px"}}
    (layout/with-resize-observer "input-container"
      [:div.space-y-4
       [:div.text-center
        [:h4.font-semibold "Resizable Container"]
        [:p.text-sm.text-gray-600.dark:text-gray-400 "Container: " (layout/container-width) "×" (layout/container-height) "px"]
        [:p.text-xs.text-gray-500.dark:text-gray-500 "Breakpoint: " (name (layout/container-breakpoint))]]

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
                              (layout/breakpoint>= :lg) "positive"
                              (layout/breakpoint>= :md) "important"
                              :else "exception")}]]])]])

(defn form-layout-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "Professional Form Layouts"]
   [:p.text-sm.text-gray-600.dark:text-gray-400 "Clean, elegant forms with proper spacing and visual hierarchy."]

   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8
    ;; Contact form with refined spacing like toddler
    [:div.bg-white.dark:bg-gray-800.p-6.rounded-lg.shadow-md
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
    [:div.bg-white.dark:bg-gray-800.p-6.rounded-lg.shadow-md
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
   [:p.text-sm.text-gray-600.dark:text-gray-400 "Check the browser console to see custom events fired by inputs."]
   [:div.space-y-3
    [:ty-input {:label "Input Events"
                :placeholder "Type here and check console"
                :on {:input #(js/console.log "Input event:" (.-detail %))
                     :change #(js/console.log "Change event:" (.-detail %))}}]
    [:ty-input {:label "Form Validation Demo"
                :type "email"
                :required true
                :placeholder "Enter a valid email"
                :flavor "important"}]]])

(defn view []
  (layout/with-window
    [:div.p-8.max-w-6xl.mx-auto.space-y-8.text-gray-600.dark:text-gray-400
     [:div
      [:h1.text-3xl.font-bold.mb-4 "Enhanced Input Components"]
      [:p
       "Form inputs with sophisticated numeric formatting, shadow values, and layout integration."]
      [:p.text-sm.text-gray-500.dark:text-gray-500.mt-2
       "✨ NEW: Auto-formatting on blur, currency support, locale-aware formatting, and error handling!"]]

     ;; Highlight the new numeric formatting capabilities first
     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (numeric-formatting-demo)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (currency-formatting-demo)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (comprehensive-form-demo)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (error-handling-demo)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (external-value-demo)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (event-demo)]

     ;; Keep existing demos but lower priority
     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (basic-input-demos)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (size-variants-demo)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (flavor-variants-demo)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (container-aware-demo)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (form-layout-demo)]]))
