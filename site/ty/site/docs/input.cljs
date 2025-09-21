(ns ty.site.docs.input
  "Documentation for ty-input component"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table doc-section example-section]]))

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Title and Description
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text++.mb-2 "ty-input"]
    [:p.text-lg.ty-text-
     "A powerful, form-associated input component with built-in validation, formatting, "
     "and semantic styling. Supports text, numeric, currency, checkbox, and password types."]]

   ;; API Reference Card
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2#attributes.text-2xl.font-bold.ty-text++.mb-4 "API Reference"]

    ;; Attributes Table
    (attribute-table
      [{:name "type"
        :type "string"
        :default "'text'"
        :description "Input type: text, number, currency, percent, compact, checkbox, password, date"}
       {:name "value"
        :type "string | number | boolean"
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
       {:name "checked"
        :type "boolean"
        :default "false"
        :description "Initial checked state for checkboxes"}
       {:name "class"
        :type "string"
        :default "null"
        :description "Additional CSS classes"}])

    [:div.mt-6
     [:h3.text-lg.font-semibold.ty-text++.mb-2 "Events"]
     (event-table
       [{:name "input"
         :when-fired "Fired on each input change"
         :payload "{value, formattedValue?, rawValue, originalEvent}"}
        {:name "change"
         :when-fired "Fired when input loses focus after change"
         :payload "{value, formattedValue?, rawValue, originalEvent}"}
        {:name "focus"
         :when-fired "Fired when input gains focus"
         :payload "Standard FocusEvent"}
        {:name "blur"
         :when-fired "Fired when input loses focus"
         :payload "Standard FocusEvent"}])
     [:p.text-sm.ty-text-.mt-2
      "Note: For checkboxes, events include " [:code.ty-bg-neutral-.px-1.rounded "checked"]
      " and " [:code.ty-bg-neutral-.px-1.rounded "formValue"] " in the detail."]]]

   ;; Basic Usage
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-bold.ty-text++.mb-4 "Basic Usage"]

    [:div.mb-6
     [:h3.text-lg.font-semibold.ty-text+.mb-2 "Simple Text Input"]
     [:div.mb-4
      [:ty-input {:placeholder "Enter your name"
                  :label "Name"}]]
     (code-block "<!-- Simple text input -->
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
</ty-input>")]]

   ;; Input Types Section
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Input Types"]
   [:div.space-y-8

    ;; Text Types
    [:div.ty-content.rounded-lg.p-6
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
<ty-input type=\"date\" label=\"Date\" value=\"2024-09-15\"></ty-input>")]

    ;; Numeric Types
    [:div.ty-content.rounded-lg.p-6
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
<!-- The display value is formatted when not focused, raw value when focused -->")]

    ;; Checkbox Type
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-xl.font-semibold.ty-text++.mb-4 "Checkbox Input"]
     [:p.ty-text-.mb-4 "Checkboxes are fully form-associated and keyboard accessible."]

     [:div.grid.gap-4

      [:div
       [:ty-input {:type "checkbox"
                   :label "I agree to terms"
                   :name "terms"}]]

      [:div
       [:ty-input {:type "checkbox"
                   :label "Subscribe to newsletter"
                   :checked "true"
                   :name "subscribe"}]]

      [:div
       [:ty-input {:type "checkbox"
                   :label "Required checkbox"
                   :required "true"
                   :name "required"}]]

      [:div
       [:ty-input {:type "checkbox"
                   :label "Disabled checkbox"
                   :disabled "true"
                   :checked "true"}]]]

     (code-block "<!-- Checkbox examples -->
<ty-input type=\"checkbox\" label=\"I agree to terms\" name=\"terms\"></ty-input>
<ty-input type=\"checkbox\" label=\"Subscribe to newsletter\" checked=\"true\" name=\"subscribe\"></ty-input>
<ty-input type=\"checkbox\" label=\"Required checkbox\" required=\"true\" name=\"required\"></ty-input>
<ty-input type=\"checkbox\" label=\"Disabled checkbox\" disabled=\"true\" checked=\"true\"></ty-input>

<!-- Checkbox values: -->
<!-- - When checked: submits value attribute (default: 'on') -->
<!-- - When unchecked: doesn't submit (or null in FormData) -->")]]

   ;; Sizes Section
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
<ty-input size=\"xl\" placeholder=\"Extra large\" label=\"Size XL\"></ty-input>")]

   ;; Flavors Section
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

<!-- Note: Setting 'error' attribute automatically applies danger flavor -->")]

   ;; Form Integration Section
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Form Integration"]
   [:div.ty-content.rounded-lg.p-6
    [:h3.text-xl.font-semibold.ty-text++.mb-4 "Native Form Support"]
    [:p.ty-text-.mb-4 "ty-input is a form-associated custom element that works seamlessly with native HTML forms."]

    [:form.space-y-4 {:onsubmit "event.preventDefault(); console.log('Form submitted'); return false;"}
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
     [:ty-input {:type "checkbox"
                 :name "terms"
                 :label "I agree to the terms"
                 :required "true"}]
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
  <ty-input type=\"checkbox\" name=\"terms\" label=\"I agree to the terms\" required=\"true\"></ty-input>
  
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
</script>")]

   ;; Advanced Examples Section
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Advanced Examples"]

   ;; Live Validation Example
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h3.text-xl.font-semibold.ty-text++.mb-4 "Live Validation"]
    [:p.ty-text-.mb-4 "Use the input event to perform real-time validation."]

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
</script>" "javascript")]

   ;; Currency Converter Example
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
</script>" "javascript")]

   ;; Best Practices Section
   [:div.ty-elevated.rounded-lg.p-6
    [:h2.text-2xl.font-bold.ty-text++.mb-4 "Best Practices"]

    [:div.grid.md:grid-cols-2.gap-6
     ;; Do's
     [:div
      [:h3.text-lg.font-semibold.ty-text-success++.mb-3.flex.items-center.gap-2
       [:ty-icon {:name "check-circle"
                  :size "20"}]
       "Do's"]
      [:ul.space-y-2.ty-text-
       [:li "• Always provide labels for accessibility"]
       [:li "• Use semantic input types (email, tel, date)"]
       [:li "• Leverage native validation attributes (required, min, max)"]
       [:li "• Use appropriate flavors for visual feedback"]
       [:li "• Set proper currency and locale for international apps"]
       [:li "• Use the shadow value from events for calculations"]]]

     ;; Don'ts
     [:div
      [:h3.text-lg.font-semibold.ty-text-danger++.mb-3.flex.items-center.gap-2
       [:ty-icon {:name "x-circle"
                  :size "20"}]
       "Don'ts"]
      [:ul.space-y-2.ty-text-
       [:li "• Don't rely on formatted values for calculations"]
       [:li "• Don't mix error attribute with success/warning flavors"]
       [:li "• Don't use placeholder as a label replacement"]
       [:li "• Don't disable without clear reasoning"]
       [:li "• Don't ignore the numeric shadow values"]
       [:li "• Don't forget to handle both input and change events"]]]]]

   ;; Tips Section
   [:div.ty-content.rounded-lg.p-6.mt-8
    [:h2.text-2xl.font-bold.ty-text++.mb-4 "Tips & Tricks"]

    [:div.space-y-4
     [:div
      [:h3.font-semibold.ty-text+ "Shadow Values"]
      [:p.ty-text- "Numeric inputs maintain a 'shadow value' - the actual numeric value used for calculations. Access it via "
       [:code.ty-bg-neutral-.px-1.rounded "event.detail.value"] " in event handlers."]]

     [:div
      [:h3.font-semibold.ty-text+ "Formatting Behavior"]
      [:p.ty-text- "Numeric inputs show formatted values when blurred and raw values when focused. This provides a better UX for data entry."]]

     [:div
      [:h3.font-semibold.ty-text+ "Form Association"]
      [:p.ty-text- "ty-input is fully form-associated. It works with FormData, form validation, and form reset just like native inputs."]]

     [:div
      [:h3.font-semibold.ty-text+ "Checkbox Values"]
      [:p.ty-text- "Checkboxes submit their 'value' attribute when checked (default: 'on'), and nothing when unchecked. Use the "
       [:code.ty-bg-neutral-.px-1.rounded "checked"] " property or attribute to set initial state."]]

     [:div
      [:h3.font-semibold.ty-text+ "Internationalization"]
      [:p.ty-text- "Currency and number formatting automatically respects the locale attribute. If not specified, it uses the browser's locale."]]]]

   ;; Related Components
   [:div.mt-8.p-4.ty-border.border.rounded-lg
    [:h3.font-semibold.ty-text+.mb-2 "Related Components"]
    [:div.flex.gap-4.text-sm
     [:a.ty-text-primary.hover:underline {:href "/docs/textarea"} "ty-textarea →"]
     [:a.ty-text-primary.hover:underline {:href "/docs/dropdown"} "ty-dropdown →"]
     [:a.ty-text-primary.hover:underline {:href "/docs/multiselect"} "ty-multiselect →"]]]])
