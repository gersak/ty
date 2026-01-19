(ns ty.site.docs.input
  "Documentation for ty-input component (text, number, currency inputs)"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table doc-section example-section]]))

(defn api-reference []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2#attributes.text-2xl.font-bold.ty-text++.mb-4 "API Reference"]

   [:h3.text-lg.font-semibold.ty-text++.mb-2 "ty-input Attributes"]

   (attribute-table
     [{:name "type"
       :type "string"
       :default "'text'"
       :description "Input type: text, number, currency, percent, compact, password, date, email"}
      {:name "value"
       :type "string | number"
       :default "null"
       :description "Initial value of the input"}
      {:name "placeholder"
       :type "string"
       :default "null"
       :description "Placeholder text for the input"}
      {:name "label"
       :type "string"
       :default "null"
       :description "Label displayed above the input"}
      {:name "name"
       :type "string"
       :default "null"
       :description "Name for form submission"}
      {:name "disabled"
       :type "boolean"
       :default "false"
       :description "Whether the input is disabled"}
      {:name "required"
       :type "boolean"
       :default "false"
       :description "Whether the input is required (shows asterisk)"}
      {:name "error"
       :type "string"
       :default "null"
       :description "Error message to display (sets danger flavor)"}
      {:name "size"
       :type "string"
       :default "'md'"
       :description "Size variant: xs, sm, md, lg, xl"}
      {:name "flavor"
       :type "string"
       :default "'neutral'"
       :description "Semantic flavor: primary, secondary, success, danger, warning, neutral"}
      {:name "currency"
       :type "string"
       :default "'USD'"
       :description "Currency code for type='currency' (e.g., USD, EUR, GBP)"}
      {:name "locale"
       :type "string"
       :default "browser locale"
       :description "Locale for number formatting (e.g., en-US, de-DE)"}
      {:name "precision"
       :type "number"
       :default "null"
       :description "Number of decimal places for numeric types"}
      {:name "delay"
       :type "number"
       :default "0"
       :description "Debounce delay in milliseconds (0-5000ms) for input/change events. Useful for search inputs and API calls."}
      {:name "class"
       :type "string"
       :default "null"
       :description "Additional CSS classes"}])

   [:div.mt-6
    [:h3.text-lg.font-semibold.ty-text++.mb-2 "ty-input Slots"]
    [:div.overflow-x-auto
     [:table.w-full
      [:thead
       [:tr.border-b.ty-border
        [:th.text-left.px-4.py-2.ty-text+ "Slot"]
        [:th.text-left.px-4.py-2.ty-text+ "Description"]]]
      [:tbody
       [:tr.border-b.ty-border-
        [:td.px-4.py-2.ty-text.font-mono.text-sm "start"]
        [:td.px-4.py-2.ty-text-.text-sm "Content placed before the input (typically an icon)"]]
       [:tr.border-b.ty-border-
        [:td.px-4.py-2.ty-text.font-mono.text-sm "end"]
        [:td.px-4.py-2.ty-text-.text-sm "Content placed after the input (typically an icon)"]]]]]]

   [:div.mt-6
    [:h3.text-lg.font-semibold.ty-text++.mb-2 "ty-input Events"]
    (event-table
      [{:name "input"
        :when-fired "Fired on each input change (respects delay attribute)"
        :payload "{value, formattedValue?, rawValue, originalEvent}"}
       {:name "change"
        :when-fired "Fired when input loses focus after change (respects delay attribute)"
        :payload "{value, formattedValue?, rawValue, originalEvent}"}
       {:name "focus"
        :when-fired "Fired when input gains focus"
        :payload "Standard FocusEvent"}
       {:name "blur"
        :when-fired "Fired when input loses focus (fires pending debounced events immediately)"
        :payload "Standard FocusEvent"}])]])

(defn basic-usage-section []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Basic Usage"]

   [:div.mb-6
    [:h3.text-lg.font-semibold.ty-text+.mb-2 "Simple Text Input"]
    [:div.mb-4
     [:ty-input {:placeholder "Enter your name"
                 :label "Name"}]]
    (code-block "<!-- Simple text input with label -->
<ty-input placeholder=\"Enter your name\" label=\"Name\"></ty-input>")]

   [:div.mb-6
    [:h3.text-lg.font-semibold.ty-text+.mb-2 "With Value and Required"]
    [:div.mb-4
     [:ty-input {:value "john@example.com"
                 :label "Email"
                 :required "true"
                 :type "email"}]]
    (code-block "<!-- Required input with initial value -->
<ty-input 
  value=\"john@example.com\" 
  label=\"Email\" 
  required=\"true\"
  type=\"email\">
</ty-input>")]

   [:div
    [:h3.text-lg.font-semibold.ty-text+.mb-2 "With Error State"]
    [:div.mb-4
     [:ty-input {:label "Username"
                 :error "Username is already taken"
                 :value "admin"}]]
    (code-block "<!-- Input with error message -->
<ty-input 
  label=\"Username\" 
  error=\"Username is already taken\"
  value=\"admin\">
</ty-input>")]])

(defn icon-slots-section []
  [:div.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Icon Slots"]
   [:div.ty-content.rounded-lg.p-6
    [:h3.text-xl.font-semibold.ty-text++.mb-4 "Start and End Slots"]
    [:p.ty-text-.mb-4 "Use start and end slots to add icons or other content before/after the input field."]

    [:div.grid.gap-4
     [:div
      [:ty-input {:label "Search"
                  :placeholder "Search..."
                  :type "text"}
       [:ty-icon {:slot "start"
                  :name "search"
                  :size "sm"}]]]

     [:div
      [:ty-input {:label "Email"
                  :placeholder "email@example.com"
                  :type "email"}
       [:ty-icon {:slot "start"
                  :name "mail"
                  :size "sm"}]]]

     [:div
      [:ty-input {:label "Password"
                  :placeholder "Enter password"
                  :type "password"}
       [:ty-icon {:slot "start"
                  :name "lock"
                  :size "sm"}]
       [:ty-icon {:slot "end"
                  :name "eye"
                  :size "sm"}]]]

     [:div
      [:ty-input {:label "Website"
                  :placeholder "https://"
                  :type "text"}
       [:ty-icon {:slot "start"
                  :name "globe"
                  :size "sm"}]
       [:ty-icon {:slot "end"
                  :name "external-link"
                  :size "sm"}]]]

     [:div
      [:ty-input {:label "Amount"
                  :placeholder "0.00"
                  :type "number"}
       [:ty-icon {:slot "start"
                  :name "dollar-sign"
                  :size "sm"}]]]]

    (code-block "<!-- Inputs with icon slots -->
<ty-input label=\"Search\" placeholder=\"Search...\">
  <ty-icon slot=\"start\" name=\"search\" size=\"sm\"></ty-icon>
</ty-input>

<ty-input label=\"Email\" placeholder=\"email@example.com\" type=\"email\">
  <ty-icon slot=\"start\" name=\"mail\" size=\"sm\"></ty-icon>
</ty-input>

<ty-input label=\"Password\" placeholder=\"Enter password\" type=\"password\">
  <ty-icon slot=\"start\" name=\"lock\" size=\"sm\"></ty-icon>
  <ty-icon slot=\"end\" name=\"eye\" size=\"sm\"></ty-icon>
</ty-input>

<ty-input label=\"Website\" placeholder=\"https://\">
  <ty-icon slot=\"start\" name=\"globe\" size=\"sm\"></ty-icon>
  <ty-icon slot=\"end\" name=\"external-link\" size=\"sm\"></ty-icon>
</ty-input>")]])

(defn delay-section []
  [:div.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Debounce with Delay"]
   [:div.ty-content.rounded-lg.p-6
    [:h3.text-xl.font-semibold.ty-text++.mb-4 "Delay Attribute"]
    [:p.ty-text-.mb-4 "The delay attribute (0-5000ms) debounces input and change events. Perfect for search inputs, API calls, and expensive operations. Events fire immediately on blur."]

    [:div.grid.gap-4
     [:div
      [:ty-input {:id "search-instant"
                  :label "Instant (no delay)"
                  :placeholder "Type to search..."
                  :delay "0"}
       [:ty-icon {:slot "start"
                  :name "search"
                  :size "sm"}]]
      [:div.ty-text-.text-xs.mt-1 "Event count: " [:span#instant-count "0"]]]

     [:div
      [:ty-input {:id "search-300"
                  :label "300ms delay"
                  :placeholder "Type to search..."
                  :delay "300"}
       [:ty-icon {:slot "start"
                  :name "search"
                  :size "sm"}]]
      [:div.ty-text-.text-xs.mt-1 "Event count: " [:span#delay-300-count "0"]]]

     [:div
      [:ty-input {:id "search-1000"
                  :label "1000ms delay (1 second)"
                  :placeholder "Type to search..."
                  :delay "1000"}
       [:ty-icon {:slot "start"
                  :name "search"
                  :size "sm"}]]
      [:div.ty-text-.text-xs.mt-1 "Event count: " [:span#delay-1000-count "0"]]]]

    [:script "
let instantCount = 0;
let delay300Count = 0;
let delay1000Count = 0;

document.getElementById('search-instant')?.addEventListener('input', () => {
  instantCount++;
  document.getElementById('instant-count').textContent = instantCount;
});

document.getElementById('search-300')?.addEventListener('input', () => {
  delay300Count++;
  document.getElementById('delay-300-count').textContent = delay300Count;
});

document.getElementById('search-1000')?.addEventListener('input', () => {
  delay1000Count++;
  document.getElementById('delay-1000-count').textContent = delay1000Count;
});"]

    (code-block "<!-- Debounce examples -->
<ty-input label=\"Instant (no delay)\" delay=\"0\" placeholder=\"Type to search...\">
  <ty-icon slot=\"start\" name=\"search\" size=\"sm\"></ty-icon>
</ty-input>

<ty-input label=\"300ms delay\" delay=\"300\" placeholder=\"Type to search...\">
  <ty-icon slot=\"start\" name=\"search\" size=\"sm\"></ty-icon>
</ty-input>

<ty-input label=\"1000ms delay\" delay=\"1000\" placeholder=\"Type to search...\">
  <ty-icon slot=\"start\" name=\"search\" size=\"sm\"></ty-icon>
</ty-input>

<script>
// Listen to debounced input events
document.querySelector('ty-input').addEventListener('input', (e) => {
  console.log('Debounced input fired:', e.detail.value);
  // Make API call, update UI, etc.
});
</script>")

    [:div.ty-elevated.rounded.p-4.mt-4
     [:h4.text-sm.font-semibold.ty-text+.my-2 "💡 Delay Best Practices"]
     [:ul.space-y-1.ty-text-.text-sm
      [:li "• Use 0-100ms for real-time feedback (validation)"]
      [:li "• Use 300-500ms for search inputs and typeahead"]
      [:li "• Use 500-1000ms for expensive operations (API calls)"]
      [:li "• Use 1000-2000ms for very expensive operations"]
      [:li "• Events fire immediately on blur (cancels pending debounce)"]]]]])

(defn text-types-section []
  [:div.ty-content.rounded-lg.p-6.mb-6
   [:h3.text-xl.font-semibold.ty-text++.mb-4 "Text Input Types"]
   [:div.grid.gap-4
    [:div
     [:ty-input {:type "text"
                 :label "Text Input"
                 :placeholder "Enter text..."}]]

    [:div
     [:ty-input {:type "password"
                 :label "Password"
                 :placeholder "Enter password..."
                 :value "secret123"}]]

    [:div
     [:ty-input {:type "email"
                 :label "Email"
                 :placeholder "email@example.com"}]]

    [:div
     [:ty-input {:type "date"
                 :label "Date"
                 :value "2024-09-15"}]]]

   (code-block "<!-- Different text input types -->
<ty-input type=\"text\" label=\"Text Input\" placeholder=\"Enter text...\"></ty-input>
<ty-input type=\"password\" label=\"Password\" placeholder=\"Enter password...\"></ty-input>
<ty-input type=\"email\" label=\"Email\" placeholder=\"email@example.com\"></ty-input>
<ty-input type=\"date\" label=\"Date\" value=\"2024-09-15\"></ty-input>")])

(defn numeric-types-section []
  [:div.ty-content.rounded-lg.p-6.mb-6
   [:h3.text-xl.font-semibold.ty-text++.mb-4 "Numeric Formatting"]
   [:p.ty-text-.mb-4 "Numeric inputs automatically format values when not focused, maintaining a shadow value for calculations."]

   [:div.grid.gap-4
    [:div
     [:ty-input {:type "number"
                 :label "Number"
                 :value "1234567.89"
                 :precision "2"}]]

    [:div
     [:ty-input {:type "currency"
                 :label "Price (USD)"
                 :value "1234.56"
                 :currency "USD"}]]

    [:div
     [:ty-input {:type "currency"
                 :label "Price (EUR)"
                 :value "999.99"
                 :currency "EUR"
                 :locale "de-DE"}]]

    [:div
     [:ty-input {:type "percent"
                 :label "Percentage"
                 :value "85.5"
                 :precision "1"}]]

    [:div
     [:ty-input {:type "compact"
                 :label "Compact Number"
                 :value "1234567"}]]]

   (code-block "<!-- Numeric formatting examples -->
<ty-input type=\"number\" label=\"Number\" value=\"1234567.89\" precision=\"2\"></ty-input>
<ty-input type=\"currency\" label=\"Price (USD)\" value=\"1234.56\" currency=\"USD\"></ty-input>
<ty-input type=\"currency\" label=\"Price (EUR)\" value=\"999.99\" currency=\"EUR\" locale=\"de-DE\"></ty-input>
<ty-input type=\"percent\" label=\"Percentage\" value=\"85.5\" precision=\"1\"></ty-input>
<ty-input type=\"compact\" label=\"Compact Number\" value=\"1234567\"></ty-input>

<!-- Note: Numeric inputs maintain a 'shadow value' for accurate calculations -->
<!-- The display value is formatted when not focused, raw value when focused -->")])

(defn input-types-section []
  [:div.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Input Types"]
   (text-types-section)
   (numeric-types-section)])

(defn sizes-section []
  [:div.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Sizes"]
   [:div.ty-content.rounded-lg.p-6
    [:p.ty-text-.mb-4 "Five size variants for different contexts and layouts."]
    [:div.grid.gap-4
     [:div
      [:ty-input {:size "xs"
                  :placeholder "Extra small"
                  :label "Size XS"}]]

     [:div
      [:ty-input {:size "sm"
                  :placeholder "Small"
                  :label "Size SM"}]]

     [:div
      [:ty-input {:size "md"
                  :placeholder "Medium (default)"
                  :label "Size MD"}]]

     [:div
      [:ty-input {:size "lg"
                  :placeholder "Large"
                  :label "Size LG"}]]

     [:div
      [:ty-input {:size "xl"
                  :placeholder "Extra large"
                  :label "Size XL"}]]]

    (code-block "<!-- Size variants -->
<ty-input size=\"xs\" placeholder=\"Extra small\" label=\"Size XS\"></ty-input>
<ty-input size=\"sm\" placeholder=\"Small\" label=\"Size SM\"></ty-input>
<ty-input size=\"md\" placeholder=\"Medium (default)\" label=\"Size MD\"></ty-input>
<ty-input size=\"lg\" placeholder=\"Large\" label=\"Size LG\"></ty-input>
<ty-input size=\"xl\" placeholder=\"Extra large\" label=\"Size XL\"></ty-input>")]])

(defn flavors-section []
  [:div.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Semantic Flavors"]
   [:div.ty-content.rounded-lg.p-6
    [:p.ty-text-.mb-4 "Semantic colors for different states and purposes. The error attribute automatically sets danger flavor."]
    [:div.grid.gap-4
     [:div
      [:ty-input {:flavor "neutral"
                  :placeholder "Default neutral"
                  :label "Neutral"}]]

     [:div
      [:ty-input {:flavor "primary"
                  :placeholder "Primary action"
                  :label "Primary"}]]

     [:div
      [:ty-input {:flavor "secondary"
                  :placeholder "Secondary action"
                  :label "Secondary"}]]

     [:div
      [:ty-input {:flavor "success"
                  :value "Valid input"
                  :label "Success"}]]

     [:div
      [:ty-input {:flavor "warning"
                  :value "Check this"
                  :label "Warning"}]]

     [:div
      [:ty-input {:flavor "danger"
                  :value "Invalid data"
                  :label "Danger"}]]]

    (code-block "<!-- Semantic flavors -->
<ty-input flavor=\"neutral\" placeholder=\"Default neutral\" label=\"Neutral\"></ty-input>
<ty-input flavor=\"primary\" placeholder=\"Primary action\" label=\"Primary\"></ty-input>
<ty-input flavor=\"secondary\" placeholder=\"Secondary action\" label=\"Secondary\"></ty-input>
<ty-input flavor=\"success\" value=\"Valid input\" label=\"Success\"></ty-input>
<ty-input flavor=\"warning\" value=\"Check this\" label=\"Warning\"></ty-input>
<ty-input flavor=\"danger\" value=\"Invalid data\" label=\"Danger\"></ty-input>

<!-- Note: Setting 'error' attribute automatically applies danger flavor -->")]])

(defn form-integration-section []
  [:div.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Form Integration"]
   [:div.ty-content.rounded-lg.p-6
    [:h3.text-xl.font-semibold.ty-text++.mb-4 "Native Form Support"]
    [:p.ty-text-.mb-4 "ty-input is a form-associated custom element that works seamlessly with native HTML forms. The error attribute is for display only - implement your own validation logic."]

    [:form.space-y-4 {:on {:submit (fn [^js event]
                                     ; (.preventDefault event)
                                     (.log js/console "Form submitted!" event)
                                     false)}}
     [:ty-input {:name "fullname"
                 :label "Full Name"
                 :required "true"}]
     [:ty-input {:name "email"
                 :type "email"
                 :label "Email"
                 :required "true"}]
     [:ty-input {:name "age"
                 :type "number"
                 :label "Age"
                 :min "18"
                 :max "120"}]
     [:ty-input {:name "salary"
                 :type "currency"
                 :label "Expected Salary"
                 :currency "USD"}]
     [:div.flex.gap-2
      [:ty-button {:type "submit"
                   :flavor "primary"} "Submit"]
      [:ty-button {:type "reset"
                   :flavor "secondary"} "Reset"]]]

    (code-block "<!-- Form integration example -->
<form>
  <ty-input name=\"fullname\" label=\"Full Name\" required=\"true\"></ty-input>
  <ty-input name=\"email\" type=\"email\" label=\"Email\" required=\"true\"></ty-input>
  <ty-input name=\"age\" type=\"number\" label=\"Age\" min=\"18\" max=\"120\"></ty-input>
  <ty-input name=\"salary\" type=\"currency\" label=\"Expected Salary\" currency=\"USD\"></ty-input>
  
  <ty-button type=\"submit\" flavor=\"primary\">Submit</ty-button>
  <ty-button type=\"reset\" flavor=\"secondary\">Reset</ty-button>
</form>

<script>
// Form data is automatically collected from ty-input elements
document.querySelector('form').addEventListener('submit', (e) => {
  e.preventDefault();
  const formData = new FormData(e.target);
  console.log(Object.fromEntries(formData));
});
</script>")]])

(defn validation-example []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h3.text-xl.font-semibold.ty-text++.mb-4 "User-Controlled Validation"]
   [:p.ty-text-.mb-4 "ty-input displays error messages via the error attribute. You control validation logic and state."]

   [:div.space-y-4
    [:ty-input {:id "username-input"
                :label "Username (min 3 chars)"
                :placeholder "Enter username..."
                :error ""}]
    [:div.ty-text-.text-sm "Type to see validation in action"]]

   [:script "
document.getElementById('username-input')?.addEventListener('input', (e) => {
  const input = e.target;
  const value = e.detail.value;
  
  if (value && value.length < 3) {
    input.setAttribute('error', 'Username must be at least 3 characters');
  } else if (value && value.length > 20) {
    input.setAttribute('error', 'Username must be less than 20 characters');
  } else {
    input.removeAttribute('error');
  }
});"]

   (code-block "<!-- Live validation example -->
<ty-input id=\"username-input\" label=\"Username (min 3 chars)\" placeholder=\"Enter username...\"></ty-input>

<script>
document.getElementById('username-input').addEventListener('input', (e) => {
  const input = e.target;
  const value = e.detail.value;
  
  if (value && value.length < 3) {
    input.setAttribute('error', 'Username must be at least 3 characters');
  } else if (value && value.length > 20) {
    input.setAttribute('error', 'Username must be less than 20 characters');
  } else {
    input.removeAttribute('error');
  }
});
</script>" "javascript")])

(defn currency-converter-example []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h3.text-xl.font-semibold.ty-text++.mb-4 "Currency Converter"]
   [:p.ty-text-.mb-4 "Numeric inputs maintain shadow values for accurate calculations."]

   [:div.grid.gap-4
    [:ty-input {:id "usd-amount"
                :type "currency"
                :label "USD Amount"
                :currency "USD"
                :value "100"}]
    [:ty-input {:id "eur-amount"
                :type "currency"
                :label "EUR Amount (at 1.08 rate)"
                :currency "EUR"
                :locale "de-DE"
                :disabled "true"
                :value "92.59"}]]

   [:script {:dangerouslySetInnerHTML
             {:__html "
const usdInput = document.getElementById('usd-amount');
const eurInput = document.getElementById('eur-amount');
const rate = 0.9259; // Example rate

usdInput?.addEventListener('input', (e) => {
  const usdValue = e.detail.value; // This is the numeric shadow value
  if (usdValue) {
    eurInput.value = (usdValue * rate).toFixed(2);
  }
});"}}]

   (code-block "<!-- Currency converter with shadow values -->
<ty-input id=\"usd-amount\" type=\"currency\" label=\"USD Amount\" currency=\"USD\" value=\"100\"></ty-input>
<ty-input id=\"eur-amount\" type=\"currency\" label=\"EUR Amount\" currency=\"EUR\" locale=\"de-DE\" disabled=\"true\"></ty-input>

<script>
const usdInput = document.getElementById('usd-amount');
const eurInput = document.getElementById('eur-amount');
const rate = 0.9259; // Example exchange rate

usdInput.addEventListener('input', (e) => {
  // e.detail.value is the numeric shadow value (not formatted)
  const usdValue = e.detail.value;
  if (usdValue) {
    eurInput.value = (usdValue * rate).toFixed(2);
  }
});
</script>" "javascript")])

(defn advanced-examples-section []
  [:div.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Advanced Examples"]
   (validation-example)
   (currency-converter-example)])

(defn best-practices-section []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Best Practices"]

   [:div.grid.md:grid-cols-2.gap-6
    [:div
     [:h3.text-lg.font-semibold.ty-text-success++.mb-3.flex.items-center.gap-2
      [:ty-icon {:name "check-circle"
                 :size "20"}]
      "Do's"]
     [:ul.space-y-2.ty-text-
      [:li "• Always provide labels for accessibility"]
      [:li "• Use semantic input types (text, email, password, date, etc.)"]
      [:li "• Implement validation in your application"]
      [:li "• Use the error attribute to display validation feedback"]
      [:li "• Access numeric shadow values from event.detail.value"]
      [:li "• Set proper currency and locale for international apps"]
      [:li "• Use name attribute for form submission"]]]

    [:div
     [:h3.text-lg.font-semibold.ty-text-danger++.mb-3.flex.items-center.gap-2
      [:ty-icon {:name "x-circle"
                 :size "20"}]
      "Don'ts"]
     [:ul.space-y-2.ty-text-
      [:li "• Don't use formatted display values for calculations"]
      [:li "• Don't expect ty-input to validate data for you"]
      [:li "• Don't use placeholder as a label replacement"]
      [:li "• Don't mix error attribute with non-danger flavors"]
      [:li "• Don't ignore event.detail.value for numeric types"]
      [:li "• Don't forget validation is your responsibility - ty-input only displays errors"]]]]])

(defn tips-section []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Tips & Tricks"]

   [:div.space-y-4
    [:div
     [:h3.font-semibold.ty-text+ "State Management"]
     [:p.ty-text- "ty-input manages its own value state internally. When users type, the component updates its internal state and emits events. The error attribute is stateless - just displays what you provide."]]

    [:div
     [:h3.font-semibold.ty-text+ "Shadow Values for Numbers"]
     [:p.ty-text- "Numeric inputs maintain a 'shadow value' - the actual numeric value. Access it via "
      [:code.ty-bg-neutral-.px-1.rounded "event.detail.value"] " for calculations, not the formatted display value."]]

    [:div
     [:h3.font-semibold.ty-text+ "Formatting Behavior"]
     [:p.ty-text- "Numeric inputs show formatted values when blurred (e.g., $1,234.56) and raw values when focused (1234.56) for easier data entry."]]

    [:div
     [:h3.font-semibold.ty-text+ "Form Association"]
     [:p.ty-text- "ty-input is fully form-associated. It works with FormData, form submission, and form reset just like native inputs."]]

    [:div
     [:h3.font-semibold.ty-text+ "Error Display"]
     [:p.ty-text- "The error attribute displays a message and applies danger styling. When error is set, the flavor automatically becomes 'danger'."]]]])

(defn related-components-section []
  [:div.p-4.ty-border.border.rounded-lg
   [:h3.font-semibold.ty-text+.mb-2 "Related Components"]
   [:div.flex.gap-4.text-sm
    [:a.ty-text-primary.hover:underline {:href "/docs/input#checkbox"} "ty-checkbox →"]
    [:a.ty-text-primary.hover:underline {:href "/docs/input#copy-field"} "ty-copy →"]
    [:a.ty-text-primary.hover:underline {:href "/docs/textarea"} "ty-textarea →"]
    [:a.ty-text-primary.hover:underline {:href "/docs/dropdown"} "ty-dropdown →"]
    [:a.ty-text-primary.hover:underline {:href "/docs/multiselect"} "ty-multiselect →"]]])

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text++.mb-2 "ty-input"]
    [:p.text-lg.ty-text-
     "Powerful, form-associated input component with automatic formatting "
     "and semantic styling. Supports text, numeric, currency, and password types. "
     "Validation is controlled by the user through the error attribute."]]

   (api-reference)
   (basic-usage-section)
   (icon-slots-section)
   (delay-section)
   (input-types-section)
   (sizes-section)
   (flavors-section)
   (form-integration-section)
   (advanced-examples-section)
   (best-practices-section)
   (tips-section)
   (related-components-section)])
