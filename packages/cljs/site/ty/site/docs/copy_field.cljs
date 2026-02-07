(ns ty.site.docs.copy-field
  "Documentation for ty-copy component (read-only copy-to-clipboard field)"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table doc-section example-section docs-page]]))

(defn api-reference []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2#attributes.text-2xl.font-bold.ty-text++.mb-4 "API Reference"]

   [:h3.text-lg.font-semibold.ty-text++.mb-2 "ty-copy Attributes"]

   (attribute-table
    [{:name "value"
      :type "string"
      :default "''"
      :description "The text value to display and copy to clipboard"}
     {:name "label"
      :type "string"
      :default "null"
      :description "Label displayed above the copy field"}
     {:name "format"
      :type "string"
      :default "'text'"
      :description "Display format: 'text' (default) or 'code' (monospace font)"}
     {:name "multiline"
      :type "boolean"
      :default "false"
      :description "Allow text to wrap across multiple lines (useful for long content like SSH keys)"}
     {:name "size"
      :type "string"
      :default "'md'"
      :description "Size variant: xs, sm, md, lg, xl"}
     {:name "flavor"
      :type "string"
      :default "'neutral'"
      :description "Semantic flavor: primary, secondary, success, danger, warning, neutral"}
     {:name "class"
      :type "string"
      :default "null"
      :description "Additional CSS classes"}])

   [:div.mt-6
    [:h3.text-lg.font-semibold.ty-text++.mb-2 "ty-copy Events"]
    (event-table
     [{:name "copy-success"
       :when-fired "Fired when text is successfully copied to clipboard"
       :payload "{value: string} - The copied text"}
      {:name "copy-error"
       :when-fired "Fired when copy operation fails"
       :payload "{error: Error} - The error that occurred"}])]

   [:div.ty-elevated.rounded.p-4.mt-4.ty-bg-info-
    [:h4.text-sm.font-semibold.ty-text++.mb-2 "💡 Copy Field Behavior"]
    [:ul.space-y-1.ty-text.text-sm
     [:li "• Read-only field with automatic copy-to-clipboard functionality"]
     [:li "• Click anywhere on the field to copy (not just the icon)"]
     [:li "• Icon animates: copy → check → copy (2 seconds)"]
     [:li "• Text selection is disabled (read-only display)"]
     [:li "• Use " [:code.ty-bg-neutral-.px-1.rounded "format=\"code\""] " for code snippets and technical content"]
     [:li "• Use " [:code.ty-bg-neutral-.px-1.rounded "multiline"] " for long text that should wrap"]
     [:li "• Perfect for API keys, tokens, URLs, and install commands"]]]])

(defn basic-usage-section []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Basic Usage"]

   [:div.mb-6
    [:h3.text-lg.font-semibold.ty-text+.mb-2 "Simple Copy Field"]
    [:div.mb-4
     [:ty-copy {:label "API Key"
                :value "sk_live_1234567890abcdef"}]]
    (code-block "<!-- Simple copy field -->
<ty-copy label=\"API Key\" value=\"sk_live_1234567890abcdef\"></ty-copy>")]

   [:div.mb-6
    [:h3.text-lg.font-semibold.ty-text+.mb-2 "With Code Format"]
    [:div.mb-4
     [:ty-copy {:label "Install Command"
                :value "npm install @gersak/ty"
                :format "code"}]]
    (code-block "<!-- Code format with monospace font -->
<ty-copy 
  label=\"Install Command\" 
  value=\"npm install @gersak/ty\"
  format=\"code\">
</ty-copy>")]

   [:div
    [:h3.text-lg.font-semibold.ty-text+.mb-2 "Multiline Content"]
    [:div.mb-4
     [:ty-copy {:label "SSH Public Key"
                :value "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC...\nLongKeyContentHere...\nMoreKeyContent..."
                :format "code"
                :multiline true}]]
    (code-block "<!-- Multiline copy field for long content -->
<ty-copy 
  label=\"SSH Public Key\"
  value=\"ssh-rsa AAAAB3NzaC1yc2EA...\\nlong key content...\"
  format=\"code\"
  multiline>
</ty-copy>")]])

(defn use-cases-section []
  [:div.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Common Use Cases"]
   [:div.ty-content.rounded-lg.p-6
    [:p.ty-text-.mb-4 "ty-copy is perfect for displaying read-only values that users need to copy."]

    [:div.grid.gap-4
     [:div
      [:ty-copy {:label "API Key"
                 :value "sk_live_1234567890abcdef"}]]

     [:div
      [:ty-copy {:label "Access Token"
                 :value "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"}]]

     [:div
      [:ty-copy {:label "Install Command"
                 :value "npm install @gersak/ty"
                 :format "code"}]]

     [:div
      [:ty-copy {:label "Website URL"
                 :value "https://ty.gersak.dev"}]]

     [:div
      [:ty-copy {:label "Git Clone URL"
                 :value "git clone https://github.com/gersak/ty.git"
                 :format "code"}]]

     [:div
      [:ty-copy {:label "Database Connection"
                 :value "postgresql://user:pass@localhost:5432/db"
                 :format "code"}]]]

    (code-block "<!-- Common use cases -->
<ty-copy label=\"API Key\" value=\"sk_live_1234567890abcdef\"></ty-copy>
<ty-copy label=\"Access Token\" value=\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9\"></ty-copy>
<ty-copy label=\"Install Command\" value=\"npm install @gersak/ty\" format=\"code\"></ty-copy>
<ty-copy label=\"Website URL\" value=\"https://ty.gersak.dev\"></ty-copy>
<ty-copy label=\"Git Clone URL\" value=\"git clone https://github.com/gersak/ty.git\" format=\"code\"></ty-copy>
<ty-copy label=\"Database Connection\" value=\"postgresql://user:pass@localhost:5432/db\" format=\"code\"></ty-copy>")]])

(defn sizes-section []
  [:div.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Sizes"]
   [:div.ty-content.rounded-lg.p-6
    [:p.ty-text-.mb-4 "Five size variants for different contexts."]
    [:div.grid.gap-4
     [:div
      [:ty-copy {:size "xs"
                 :label "Extra Small"
                 :value "xs-value-123"}]]

     [:div
      [:ty-copy {:size "sm"
                 :label "Small"
                 :value "sm-value-456"}]]

     [:div
      [:ty-copy {:size "md"
                 :label "Medium (default)"
                 :value "md-value-789"}]]

     [:div
      [:ty-copy {:size "lg"
                 :label "Large"
                 :value "lg-value-abc"}]]

     [:div
      [:ty-copy {:size "xl"
                 :label "Extra Large"
                 :value "xl-value-def"}]]]

    (code-block "<!-- Size variants -->
<ty-copy size=\"xs\" label=\"Extra Small\" value=\"xs-value-123\"></ty-copy>
<ty-copy size=\"sm\" label=\"Small\" value=\"sm-value-456\"></ty-copy>
<ty-copy size=\"md\" label=\"Medium (default)\" value=\"md-value-789\"></ty-copy>
<ty-copy size=\"lg\" label=\"Large\" value=\"lg-value-abc\"></ty-copy>
<ty-copy size=\"xl\" label=\"Extra Large\" value=\"xl-value-def\"></ty-copy>")]])

(defn flavors-section []
  [:div.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Semantic Flavors"]
   [:div.ty-content.rounded-lg.p-6
    [:p.ty-text-.mb-4 "Use semantic colors to categorize different types of copyable content."]
    [:div.grid.gap-4
     [:div
      [:ty-copy {:flavor "neutral"
                 :label "Neutral (default)"
                 :value "neutral-value-1234"}]]

     [:div
      [:ty-copy {:flavor "primary"
                 :label "Primary API Key"
                 :value "pk_live_primary_key_1234"}]]

     [:div
      [:ty-copy {:flavor "secondary"
                 :label "Secondary Token"
                 :value "sk_test_secondary_token"}]]

     [:div
      [:ty-copy {:flavor "success"
                 :label "Production URL"
                 :value "https://production.example.com"}]]

     [:div
      [:ty-copy {:flavor "warning"
                 :label "Staging Environment"
                 :value "https://staging.example.com"}]]

     [:div
      [:ty-copy {:flavor "danger"
                 :label "Delete Token (Use Carefully)"
                 :value "delete_token_dangerous_abc123"}]]]

    (code-block "<!-- Semantic flavors -->
<ty-copy flavor=\"neutral\" label=\"Neutral (default)\" value=\"neutral-value-1234\"></ty-copy>
<ty-copy flavor=\"primary\" label=\"Primary API Key\" value=\"pk_live_primary_key_1234\"></ty-copy>
<ty-copy flavor=\"secondary\" label=\"Secondary Token\" value=\"sk_test_secondary_token\"></ty-copy>
<ty-copy flavor=\"success\" label=\"Production URL\" value=\"https://production.example.com\"></ty-copy>
<ty-copy flavor=\"warning\" label=\"Staging Environment\" value=\"https://staging.example.com\"></ty-copy>
<ty-copy flavor=\"danger\" label=\"Delete Token (Use Carefully)\" value=\"delete_token_dangerous_abc123\"></ty-copy>")]])

(defn event-handling-example []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h3.text-xl.font-semibold.ty-text++.mb-4 "Event Handling"]
   [:p.ty-text-.mb-4 "Listen to copy events to provide user feedback or track copying behavior."]

   [:div.space-y-4.mb-4
    [:ty-copy {:id "event-copy"
               :label "API Key (Try copying)"
               :value "sk_live_event_example_123"}]
    [:div.ty-elevated.rounded.p-3
     [:p.text-sm.ty-text- "Last action: " [:span#copy-status.font-mono.ty-text "waiting..."]]]
    [:div.ty-elevated.rounded.p-3
     [:p.text-sm.ty-text- "Copy count: " [:span#copy-count.font-bold "0"]]]]

   [:script "
let copyCount = 0;
const copyField = document.getElementById('event-copy');
const statusEl = document.getElementById('copy-status');
const countEl = document.getElementById('copy-count');

copyField?.addEventListener('copy-success', (e) => {
  copyCount++;
  statusEl.textContent = `Copied: ${e.detail.value}`;
  countEl.textContent = copyCount;
});

copyField?.addEventListener('copy-error', (e) => {
  statusEl.textContent = `Error: ${e.detail.error.message}`;
});"]

   (code-block "<!-- Event handling example -->
<ty-copy id=\"event-copy\" label=\"API Key\" value=\"sk_live_event_example_123\"></ty-copy>
<div>Last action: <span id=\"copy-status\">waiting...</span></div>
<div>Copy count: <span id=\"copy-count\">0</span></div>

<script>
let copyCount = 0;
const copyField = document.getElementById('event-copy');

copyField.addEventListener('copy-success', (e) => {
  copyCount++;
  document.getElementById('copy-status').textContent = `Copied: ${e.detail.value}`;
  document.getElementById('copy-count').textContent = copyCount;
  
  // Show success notification, track analytics, etc.
});

copyField.addEventListener('copy-error', (e) => {
  document.getElementById('copy-status').textContent = `Error: ${e.detail.error.message}`;
  
  // Show error notification, fallback to manual copy, etc.
});
</script>" "javascript")])

(defn api-keys-example []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h3.text-xl.font-semibold.ty-text++.mb-4 "API Keys Dashboard"]
   [:p.ty-text-.mb-4 "Display multiple API keys with different access levels using semantic flavors."]

   [:div.ty-elevated.rounded-lg.p-4.space-y-4
    [:div
     [:h4.text-sm.font-semibold.ty-text+.mb-2 "Production Keys"]
     [:div.space-y-2
      [:ty-copy {:size "sm"
                 :flavor "success"
                 :label "Public Key"
                 :value "pk_live_production_public_key_abc123"}]
      [:ty-copy {:size "sm"
                 :flavor "danger"
                 :label "Secret Key (Keep Safe!)"
                 :value "sk_live_production_secret_key_xyz789"
                 :format "code"}]]]

    [:div
     [:h4.text-sm.font-semibold.ty-text+.mb-2 "Development Keys"]
     [:div.space-y-2
      [:ty-copy {:size "sm"
                 :flavor "secondary"
                 :label "Test Public Key"
                 :value "pk_test_dev_public_key_def456"}]
      [:ty-copy {:size "sm"
                 :flavor "warning"
                 :label "Test Secret Key"
                 :value "sk_test_dev_secret_key_uvw321"
                 :format "code"}]]]]

   (code-block "<!-- API Keys Dashboard -->
<div>
  <h4>Production Keys</h4>
  <ty-copy 
    size=\"sm\" 
    flavor=\"success\" 
    label=\"Public Key\" 
    value=\"pk_live_production_public_key_abc123\">
  </ty-copy>
  
  <ty-copy 
    size=\"sm\" 
    flavor=\"danger\" 
    label=\"Secret Key (Keep Safe!)\" 
    value=\"sk_live_production_secret_key_xyz789\"
    format=\"code\">
  </ty-copy>
</div>

<div>
  <h4>Development Keys</h4>
  <ty-copy 
    size=\"sm\" 
    flavor=\"secondary\" 
    label=\"Test Public Key\" 
    value=\"pk_test_dev_public_key_def456\">
  </ty-copy>
  
  <ty-copy 
    size=\"sm\" 
    flavor=\"warning\" 
    label=\"Test Secret Key\" 
    value=\"sk_test_dev_secret_key_uvw321\"
    format=\"code\">
  </ty-copy>
</div>")])

(defn advanced-examples-section []
  [:div.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Advanced Examples"]
   (event-handling-example)
   (api-keys-example)])

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
      [:li "• Use for read-only values that users need to copy"]
      [:li "• Use " [:code.ty-bg-neutral-.px-1.rounded "format=\"code\""] " for technical content"]
      [:li "• Use semantic flavors to indicate importance or environment"]
      [:li "• Provide clear labels that describe the content"]
      [:li "• Listen to " [:code.ty-bg-neutral-.px-1.rounded "copy-success"] " for user feedback"]
      [:li "• Use " [:code.ty-bg-neutral-.px-1.rounded "multiline"] " for long content like SSH keys"]
      [:li "• Group related copy fields together"]]]

    [:div
     [:h3.text-lg.font-semibold.ty-text-danger++.mb-3.flex.items-center.gap-2
      [:ty-icon {:name "x-circle"
                 :size "20"}]
      "Don'ts"]
     [:ul.space-y-2.ty-text-
      [:li "• Don't use for user-editable content (use ty-input instead)"]
      [:li "• Don't expose sensitive data without proper access control"]
      [:li "• Don't forget to handle copy errors"]
      [:li "• Don't use overly long labels"]
      [:li "• Don't rely only on color to distinguish copy fields"]
      [:li "• Don't use for actions (use ty-button instead)"]]]]])

(defn tips-section []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Tips & Tricks"]

   [:div.space-y-4
    [:div
     [:h3.font-semibold.ty-text+ "Click Anywhere to Copy"]
     [:p.ty-text- "Users can click anywhere on the field to copy, not just the icon. This makes it easier to use on mobile devices."]]

    [:div
     [:h3.font-semibold.ty-text+ "Icon Animation Feedback"]
     [:p.ty-text- "The icon animates from copy → check → copy over 2 seconds. This provides clear visual feedback that the copy was successful."]]

    [:div
     [:h3.font-semibold.ty-text+ "Code Format"]
     [:p.ty-text- "Use " [:code.ty-bg-neutral-.px-1.rounded "format=\"code\""] " for technical content. This applies monospace font and better styling for code snippets."]]

    [:div
     [:h3.font-semibold.ty-text+ "Multiline Support"]
     [:p.ty-text- "Enable " [:code.ty-bg-neutral-.px-1.rounded "multiline"] " for long content like SSH keys or certificates. Text will wrap and the field height adjusts automatically."]]

    [:div
     [:h3.font-semibold.ty-text+ "Event-Driven Feedback"]
     [:p.ty-text- "Use " [:code.ty-bg-neutral-.px-1.rounded "copy-success"] " event to show toast notifications, track analytics, or update UI state."]]

    [:div
     [:h3.font-semibold.ty-text+ "Security Consideration"]
     [:p.ty-text- "Be careful when displaying sensitive data. Consider masking part of the value or requiring additional authentication before showing full content."]]]])

(defn related-components-section []
  [:div.p-4.ty-border.border.rounded-lg
   [:h3.font-semibold.ty-text+.mb-2 "Related Components"]
   [:div.flex.gap-4.text-sm
    [:a.ty-text-primary.hover:underline {:href "/docs/input"} "ty-input →"]
    [:a.ty-text-primary.hover:underline {:href "/docs/button"} "ty-button →"]
    [:a.ty-text-primary.hover:underline {:href "/docs/modal"} "ty-modal →"]]])

(defn view []
  (docs-page
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text++.mb-2 "ty-copy"]
    [:p.text-lg.ty-text-
     "Read-only field with one-click copy-to-clipboard functionality. "
     "Perfect for API keys, tokens, URLs, install commands, and any content users need to copy. "
     "Features automatic icon animation and event-driven feedback."]]

   (api-reference)
   (basic-usage-section)
   (use-cases-section)
   (sizes-section)
   (flavors-section)
   (advanced-examples-section)
   (best-practices-section)
   (tips-section)
   (related-components-section)))
