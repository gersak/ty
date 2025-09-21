(ns ty.site.docs.textarea
  "Documentation for ty-textarea component - auto-resizing textarea with form integration"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table doc-section example-section]]))

(defn basic-usage []
  [:div
   [:h2.text-2xl.font-semibold.ty-text++.mb-4 "Basic Usage"]

   [:div.space-y-4
    [:p.ty-text-
     "The ty-textarea component provides an auto-resizing text input that grows with content, eliminating scrollbars for a smooth user experience."]

    (example-section
      "Basic Auto-Resize"
      [:ty-textarea
       {:label "Tell us about yourself"
        :name "bio"
        :placeholder "Start typing... The textarea will grow automatically as you add more content."}]
      "<ty-textarea 
  label=\"Tell us about yourself\"
  name=\"bio\"
  placeholder=\"Start typing... The textarea will grow automatically as you add more content.\">
</ty-textarea>" "html")

    [:p.ty-text-
     "The textarea automatically calculates the required height based on content, providing a seamless typing experience without manual resize handles or scrollbars."]]])

(defn api-reference []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-semibold.ty-text++.mb-6 "API Reference"]

   [:div.space-y-6
    ;; Attributes section
    [:div
     [:h3.text-lg.font-medium.ty-text+.mb-3 "Attributes"]
     (attribute-table
       [{:name "name"
         :type "string"
         :default "-"
         :description "Form field name for submission and data binding"}
        {:name "value"
         :type "string"
         :default "\"\""
         :description "Current textarea value (can be set via attribute or property)"}
        {:name "placeholder"
         :type "string"
         :default "-"
         :description "Placeholder text shown when empty"}
        {:name "label"
         :type "string"
         :default "-"
         :description "Label text displayed above the textarea"}
        {:name "disabled"
         :type "boolean"
         :default "false"
         :description "Disables the textarea when true"}
        {:name "required"
         :type "boolean"
         :default "false"
         :description "Marks field as required (shows red asterisk)"}
        {:name "error"
         :type "string"
         :default "-"
         :description "Error message to display below textarea"}
        {:name "size"
         :type "string"
         :default "\"md\""
         :description "Size variant: xs, sm, md, lg, xl"}
        {:name "rows"
         :type "string"
         :default "\"3\""
         :description "Initial number of visible rows"}
        {:name "cols"
         :type "string"
         :default "-"
         :description "Number of visible columns (width in characters)"}
        {:name "resize"
         :type "string"
         :default "\"none\""
         :description "Manual resize control: none, both, vertical, horizontal"}
        {:name "min-height"
         :type "string"
         :default "-"
         :description "Minimum height constraint (e.g., \"100px\")"}
        {:name "max-height"
         :type "string"
         :default "-"
         :description "Maximum height before scrolling (e.g., \"300px\")"}])]

    ;; Events section
    [:div
     [:h3.text-lg.font-medium.ty-text+.mb-3 "Events"]
     (event-table
       [{:name "input"
         :detail "{value: string, originalEvent: Event}"
         :description "Fired on every keystroke with current value"}
        {:name "change"
         :detail "{value: string, originalEvent: Event}"
         :description "Fired when focus leaves after value changed"}
        {:name "focus"
         :detail "-"
         :description "Fired when textarea gains focus"}
        {:name "blur"
         :detail "-"
         :description "Fired when textarea loses focus"}])]

    ;; Methods section
    [:div
     [:h3.text-lg.font-medium.ty-text+.mb-3 "Properties"]
     [:div.overflow-x-auto
      [:table.w-full.text-sm
       [:thead.ty-border-b.ty-border
        [:tr
         [:th.text-left.py-2.px-4.ty-text+ "Property"]
         [:th.text-left.py-2.px-4.ty-text+ "Type"]
         [:th.text-left.py-2.px-4.ty-text+ "Description"]]]
       [:tbody.divide-y.ty-border--
        [:tr
         [:td.py-2.px-4.font-mono.text-xs "value"]
         [:td.py-2.px-4.ty-text- "string"]
         [:td.py-2.px-4.ty-text- "Get/set the current value programmatically"]]]]]]]])

(defn size-variants []
  (doc-section
    "Size Variants"
    [:div.space-y-6
     [:p.ty-text-
      "Five size variants are available for different content needs, from quick notes to detailed essays."]

     [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
     ;; XS Example
      [:div
       [:h4.font-medium.ty-text.mb-2 "Extra Small (xs)"]
       [:ty-textarea
        {:placeholder "Quick note..."
         :size "xs"}]
       (code-block "<ty-textarea size=\"xs\" placeholder=\"Quick note...\"></ty-textarea>" "html")]

     ;; SM Example
      [:div
       [:h4.font-medium.ty-text.mb-2 "Small (sm)"]
       [:ty-textarea
        {:placeholder "Short comment..."
         :size "sm"}]
       (code-block "<ty-textarea size=\"sm\" placeholder=\"Short comment...\"></ty-textarea>" "html")]

     ;; MD Example (Default)
      [:div
       [:h4.font-medium.ty-text.mb-2 "Medium (md) - Default"]
       [:ty-textarea
        {:placeholder "Standard textarea..."
         :size "md"}]
       (code-block "<ty-textarea size=\"md\" placeholder=\"Standard textarea...\"></ty-textarea>" "html")]

     ;; LG Example
      [:div
       [:h4.font-medium.ty-text.mb-2 "Large (lg)"]
       [:ty-textarea
        {:placeholder "Detailed description..."
         :size "lg"}]
       (code-block "<ty-textarea size=\"lg\" placeholder=\"Detailed description...\"></ty-textarea>" "html")]]]))

(defn form-integration []
  (doc-section
    "Form Integration"
    [:div.space-y-6
     [:p.ty-text-
      "ty-textarea integrates seamlessly with native forms and HTMX, supporting standard form submission via the name attribute."]

    ;; Native form example
     (example-section
       "Native Form Submission"
       [:form.space-y-4
        {:onSubmit "event.preventDefault(); const formData = new FormData(event.target); console.log('Form Data:', Object.fromEntries(formData)); return false;"}

        [:ty-textarea
         {:label "Your Message"
          :name "message"
          :placeholder "Enter your message..."
          :required true}]

        [:ty-textarea
         {:label "Additional Notes"
          :name "notes"
          :placeholder "Optional notes..."
          :rows "2"}]

        [:button.ty-bg-primary.ty-text++.px-4.py-2.rounded
         {:type "submit"}
         "Submit Form"]]

       "<form>
  <ty-textarea
    label=\"Your Message\"
    name=\"message\"
    placeholder=\"Enter your message...\"
    required>
  </ty-textarea>
  
  <ty-textarea
    label=\"Additional Notes\"
    name=\"notes\"
    placeholder=\"Optional notes...\"
    rows=\"2\">
  </ty-textarea>
  
  <button type=\"submit\">Submit Form</button>
</form>" "html")

     [:div.ty-bg-info-.ty-border-info.border.rounded-lg.p-4
      [:p.ty-text-info.text-sm
       [:strong "FormData Support: "]
       "The component works with the native FormData API and submits values using the 'name' attribute."]]]))

(defn height-constraints []
  (doc-section
    "Height Constraints"
    [:div.space-y-6
     [:p.ty-text-
      "Control the minimum and maximum height of the textarea to maintain consistent layouts while preserving auto-resize functionality."]

     [:div.grid.grid-cols-1.md:grid-cols-3.gap-4
     ;; Min height example
      [:div
       [:h4.font-medium.ty-text.mb-2 "Minimum Height"]
       [:ty-textarea
        {:placeholder "This starts taller..."
         :min-height "100px"
         :size "sm"}]
       (code-block
         "<ty-textarea 
  min-height=\"100px\"
  size=\"sm\"
  placeholder=\"This starts taller...\">
</ty-textarea>" "html")]

     ;; Max height example
      [:div
       [:h4.font-medium.ty-text.mb-2 "Maximum Height"]
       [:ty-textarea
        {:placeholder "Type lots of text..."
         :max-height "120px"
         :value "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6"}]
       (code-block
         "<ty-textarea 
  max-height=\"120px\"
  placeholder=\"Type lots of text...\">
</ty-textarea>" "html")]

     ;; Both constraints
      [:div
       [:h4.font-medium.ty-text.mb-2 "Min & Max Range"]
       [:ty-textarea
        {:placeholder "Constrained growth..."
         :min-height "80px"
         :max-height "200px"}]
       (code-block
         "<ty-textarea 
  min-height=\"80px\"
  max-height=\"200px\"
  placeholder=\"Constrained growth...\">
</ty-textarea>" "html")]]

     [:p.ty-text-.text-sm
      "When max-height is reached, the textarea shows a scrollbar while maintaining the specified maximum height."]]))

(defn validation-states []
  (doc-section
    "Validation & States"
    [:div.space-y-6
     [:p.ty-text-
      "Handle validation with required fields, error messages, and disabled states."]

     [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
     ;; Required field
      [:div
       [:h4.font-medium.ty-text.mb-2 "Required Field"]
       [:ty-textarea
        {:label "Project Description"
         :name "description"
         :placeholder "Required field..."
         :required true}]
       (code-block
         "<ty-textarea
  label=\"Project Description\"
  name=\"description\"
  placeholder=\"Required field...\"
  required>
</ty-textarea>" "html")]

     ;; Error state
      [:div
       [:h4.font-medium.ty-text.mb-2 "Error State"]
       [:ty-textarea
        {:label "Review"
         :placeholder "Enter review..."
         :error "Review must be at least 50 characters"
         :value "Too short"}]
       (code-block
         "<ty-textarea
  label=\"Review\"
  placeholder=\"Enter review...\"
  error=\"Review must be at least 50 characters\"
  value=\"Too short\">
</ty-textarea>" "html")]

     ;; Disabled state
      [:div
       [:h4.font-medium.ty-text.mb-2 "Disabled State"]
       [:ty-textarea
        {:label "System Generated"
         :disabled true
         :value "This content is read-only"}]
       (code-block
         "<ty-textarea
  label=\"System Generated\"
  disabled
  value=\"This content is read-only\">
</ty-textarea>" "html")]

     ;; Programmatic validation
      [:div
       [:h4.font-medium.ty-text.mb-2 "Dynamic Validation"]
       [:ty-textarea
        {:id "validated-textarea"
         :label "Comment (min 10 chars)"
         :placeholder "Type at least 10 characters..."}]
       [:script
        {:dangerouslySetInnerHTML
         {:__html
          "(() => {
  const textarea = document.getElementById('validated-textarea');
  if (textarea) {
    textarea.addEventListener('input', (e) => {
      const value = e.detail.value;
      if (value.length < 10 && value.length > 0) {
        textarea.setAttribute('error', `${10 - value.length} more characters needed`);
      } else {
        textarea.removeAttribute('error');
      }
    });
  }
})();"}}]
       (code-block
         "const textarea = document.querySelector('ty-textarea');
textarea.addEventListener('input', (e) => {
  const value = e.detail.value;
  if (value.length < 10) {
    textarea.setAttribute('error', `${10 - value.length} more characters needed`);
  } else {
    textarea.removeAttribute('error');
  }
});" "javascript")]]]))

(defn resize-options []
  (doc-section
    "Resize Options"
    [:div.space-y-6
     [:p.ty-text-
      "Control whether users can manually resize the textarea in addition to auto-resize functionality."]

     [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
     ;; Auto only (default)
      [:div
       [:h4.font-medium.ty-text.mb-2 "Auto-Resize Only (Default)"]
       [:ty-textarea
        {:placeholder "Auto-resize only, no manual resize..."
         :value "This textarea automatically adjusts height based on content. Manual resize is disabled."}]
       [:p.text-sm.ty-text-- "No resize handle visible"]]

     ;; Both directions
      [:div
       [:h4.font-medium.ty-text.mb-2 "Manual Resize - Both"]
       [:ty-textarea
        {:placeholder "Drag corner to resize..."
         :resize "both"
         :value "Drag the bottom-right corner to manually resize in any direction."}]
       [:p.text-sm.ty-text-- "↘️ Resize handle in corner"]]

     ;; Vertical only
      [:div
       [:h4.font-medium.ty-text.mb-2 "Vertical Resize Only"]
       [:ty-textarea
        {:placeholder "Resize vertically..."
         :resize "vertical"
         :value "Can be resized vertically but not horizontally."}]
       [:p.text-sm.ty-text-- "↕️ Vertical resize only"]]

     ;; Horizontal only
      [:div
       [:h4.font-medium.ty-text.mb-2 "Horizontal Resize Only"]
       [:ty-textarea
        {:placeholder "Resize horizontally..."
         :resize "horizontal"
         :value "Can be resized horizontally but not vertically."}]
       [:p.text-sm.ty-text-- "↔️ Horizontal resize only"]]]

     (code-block
       "<!-- Auto-resize only (default) -->
<ty-textarea resize=\"none\"></ty-textarea>

<!-- Allow both directions -->
<ty-textarea resize=\"both\"></ty-textarea>

<!-- Vertical only -->
<ty-textarea resize=\"vertical\"></ty-textarea>

<!-- Horizontal only -->
<ty-textarea resize=\"horizontal\"></ty-textarea>" "html")]))

(defn programmatic-control []
  (doc-section
    "Programmatic Control"
    [:div.space-y-6
     [:p.ty-text-
      "Control the textarea programmatically using JavaScript for dynamic interactions."]

     (example-section
       "JavaScript Control"
       [:div.space-y-4
        [:ty-textarea
         {:id "prog-textarea"
          :label "Controlled Textarea"
          :placeholder "This textarea is controlled via JavaScript..."}]

        [:div.flex.gap-2.flex-wrap
         [:button.ty-bg-primary.ty-text++.px-3.py-1.rounded.text-sm
          {:on {:click (fn [] (set! (.-value (.getElementById js/document "prog-textarea")) "Hello from JavaScript!"))}}
          "Set Value"]
         [:button.ty-bg-secondary.ty-text++.px-3.py-1.rounded.text-sm
          {:on {:click (fn [] (set! (.-value (.getElementById js/document "prog-textarea"))
                                    (str (.-value (.getElementById js/document "prog-textarea")) "\nAppended text")))}}
          "Append Text"]
         [:button.ty-bg-danger.ty-text++.px-3.py-1.rounded.text-sm
          {:on {:click (fn [] (set! (.-value (.getElementById js/document "prog-textarea")) ""))}}
          "Clear"]
         [:button.ty-bg-warning.ty-text++.px-3.py-1.rounded.text-sm
          {:on {:click (fn [] (.setAttribute (.getElementById js/document "prog-textarea") "error" "Validation error!"))}}
          "Show Error"]
         [:button.ty-bg-success.ty-text++.px-3.py-1.rounded.text-sm
          {:on {:click (fn [] (.removeAttribute (.getElementById js/document "prog-textarea") "error"))}}
          "Clear Error"]
         [:button.ty-bg-neutral.ty-text++.px-3.py-1.rounded.text-sm
          {:on {:click (fn [] (js/alert (str "Current value: " (.-value (.getElementById js/document "prog-textarea")))))}}
          "Get Value"]]]

       "// Get textarea element
const textarea = document.querySelector('ty-textarea');

// Set value via property
textarea.value = 'New content';

// Set value via attribute
textarea.setAttribute('value', 'New content');

// Listen for changes
textarea.addEventListener('input', (e) => {
  console.log('Value changed:', e.detail.value);
});

// Show/hide error
textarea.setAttribute('error', 'Validation failed');
textarea.removeAttribute('error');

// Enable/disable
textarea.setAttribute('disabled', '');
textarea.removeAttribute('disabled');" "javascript")]))

(defn common-patterns []
  (doc-section
    "Common Use Cases"
    [:div.space-y-6
    ;; Comment form
     [:div
      [:h3.font-medium.ty-text+.mb-3 "Comment Form"]
      [:div.ty-content.rounded-lg.p-6
       [:form.space-y-4
        [:ty-textarea
         {:label "Leave a comment"
          :name "comment"
          :placeholder "Share your thoughts..."
          :required true
          :min-height "80px"
          :max-height "300px"}]
        [:div.flex.justify-between.items-center
         [:span.text-sm.ty-text-- "Markdown supported"]
         [:button.ty-bg-primary.ty-text++.px-4.py-2.rounded.text-sm
          {:type "submit"}
          "Post Comment"]]]

       (code-block
         "<form>
  <ty-textarea
    label=\"Leave a comment\"
    name=\"comment\"
    placeholder=\"Share your thoughts...\"
    required
    min-height=\"80px\"
    max-height=\"300px\">
  </ty-textarea>
  <button type=\"submit\">Post Comment</button>
</form>" "html")]]

    ;; Contact form
     [:div
      [:h3.font-medium.ty-text+.mb-3 "Contact Form Message Field"]
      [:div.ty-content.rounded-lg.p-6
       [:ty-textarea
        {:label "How can we help?"
         :name "message"
         :placeholder "Describe your inquiry in detail..."
         :required true
         :rows "5"}]

       (code-block
         "<ty-textarea
  label=\"How can we help?\"
  name=\"message\"
  placeholder=\"Describe your inquiry in detail...\"
  required
  rows=\"5\">
</ty-textarea>" "html")]]

    ;; Code editor
     [:div
      [:h3.font-medium.ty-text+.mb-3 "Code Input"]
      [:div.ty-content.rounded-lg.p-6
       [:ty-textarea
        {:label "Paste your code"
         :name "code"
         :placeholder "// Paste your code here..."
         :value "function autoResize(textarea) {\n  // Calculate required height\n  const height = textarea.scrollHeight;\n  textarea.style.height = height + 'px';\n}"
         :size "sm"}]

       (code-block
         "<ty-textarea
  label=\"Paste your code\"
  name=\"code\"
  placeholder=\"// Paste your code here...\"
  size=\"sm\">
</ty-textarea>" "html")]]]))

(defn best-practices []
  [:div.ty-elevated.rounded-lg.p-6
   [:h2.text-2xl.font-semibold.ty-text++.mb-4 "Best Practices"]

   [:div.space-y-4
    [:div.flex.gap-3
     [:ty-icon.ty-text-success.flex-shrink-0 {:name "check-circle"}]
     [:div
      [:strong.ty-text+ "Use appropriate size variants"]
      [:p.ty-text-.text-sm "Match textarea size to expected content length - xs for tweets, xl for articles."]]]

    [:div.flex.gap-3
     [:ty-icon.ty-text-success.flex-shrink-0 {:name "check-circle"}]
     [:div
      [:strong.ty-text+ "Set height constraints for layouts"]
      [:p.ty-text-.text-sm "Use min-height and max-height to maintain consistent layouts while preserving auto-resize."]]]

    [:div.flex.gap-3
     [:ty-icon.ty-text-success.flex-shrink-0 {:name "check-circle"}]
     [:div
      [:strong.ty-text+ "Always include labels for accessibility"]
      [:p.ty-text-.text-sm "Use the label attribute to provide context for screen readers and improve UX."]]]

    [:div.flex.gap-3
     [:ty-icon.ty-text-success.flex-shrink-0 {:name "check-circle"}]
     [:div
      [:strong.ty-text+ "Provide helpful placeholders"]
      [:p.ty-text-.text-sm "Use placeholders to guide users on expected content format or length."]]]

    [:div.flex.gap-3
     [:ty-icon.ty-text-danger.flex-shrink-0 {:name "x-circle"}]
     [:div
      [:strong.ty-text+ "Don't disable auto-resize without reason"]
      [:p.ty-text-.text-sm "Auto-resize improves UX by eliminating scrollbars - only disable for fixed-height requirements."]]]

    [:div.flex.gap-3
     [:ty-icon.ty-text-danger.flex-shrink-0 {:name "x-circle"}]
     [:div
      [:strong.ty-text+ "Avoid very small max-heights"]
      [:p.ty-text-.text-sm "If using max-height, ensure it's tall enough for reasonable content (minimum 100px recommended)."]]]

    [:div.flex.gap-3
     [:ty-icon.ty-text-warning.flex-shrink-0 {:name "alert-triangle"}]
     [:div
      [:strong.ty-text+ "Consider performance with large content"]
      [:p.ty-text-.text-sm "For very large documents (10,000+ characters), consider using max-height to limit DOM size."]]]]])

(defn advanced-examples []
  (doc-section
    "Advanced Examples"
    [:div.space-y-6
    ;; HTMX Integration
     [:div
      [:h3.font-medium.ty-text+.mb-3 "HTMX Live Search"]
      [:div.ty-content.rounded-lg.p-6
       [:ty-textarea
        {:name "search"
         :label "Search Content"
         :placeholder "Type to search..."
         :size "sm"
         :min-height "60px"
         :max-height "150px"
         :hx-post "/api/search"
         :hx-trigger "input changed delay:500ms"
         :hx-target "#search-results"}]
       [:div#search-results.mt-4.ty-text-.text-sm
        "Results will appear here..."]

       (code-block
         "<ty-textarea
  name=\"search\"
  label=\"Search Content\"
  placeholder=\"Type to search...\"
  size=\"sm\"
  min-height=\"60px\"
  max-height=\"150px\"
  hx-post=\"/api/search\"
  hx-trigger=\"input changed delay:500ms\"
  hx-target=\"#search-results\">
</ty-textarea>
<div id=\"search-results\">Results will appear here...</div>"
         :lang "html")]]

    ;; Character Counter
     [:div
      [:h3.font-medium.ty-text+.mb-3 "Character Counter"]
      [:div.ty-content.rounded-lg.p-6
       [:ty-textarea
        {:id "counted-textarea"
         :label "Tweet (max 280 chars)"
         :placeholder "What's happening?"
         :max-height "150px"}]
       [:div.flex.justify-between.items-center.mt-2
        [:span.text-sm.ty-text-- {:id "char-count"} "0 / 280"]
        [:button.ty-bg-primary.ty-text++.px-3.py-1.rounded.text-sm
         {:id "tweet-btn"
          :disabled true}
         "Tweet"]]
       [:script
        {:dangerouslySetInnerHTML
         {:__html
          "(() => {
  const textarea = document.getElementById('counted-textarea');
  const counter = document.getElementById('char-count');
  const button = document.getElementById('tweet-btn');
  if (textarea && counter && button) {
    textarea.addEventListener('input', (e) => {
      const length = e.detail.value.length;
      counter.textContent = `${length} / 280`;
      counter.className = length > 280 ? 'text-sm ty-text-danger' : 'text-sm ty-text--';
      button.disabled = length === 0 || length > 280;
    });
  }
})();"}}]

       (code-block
         "const textarea = document.querySelector('ty-textarea');
const counter = document.querySelector('#char-count');
const button = document.querySelector('#tweet-btn');

textarea.addEventListener('input', (e) => {
  const length = e.detail.value.length;
  counter.textContent = `${length} / 280`;
  counter.className = length > 280 ? 'text-danger' : 'text-muted';
  button.disabled = length === 0 || length > 280;
});" :lang "javascript")]]

    ;; Auto-save Draft
     [:div
      [:h3.font-medium.ty-text+.mb-3 "Auto-save Draft"]
      [:div.ty-content.rounded-lg.p-6
       [:ty-textarea
        {:id "draft-textarea"
         :label "Article Draft"
         :placeholder "Start writing your article..."
         :rows "6"
         :min-height "150px"}]
       [:div.flex.items-center.gap-2.mt-2
        [:span.text-sm.ty-text-- {:id "save-status"} "Ready"]
        [:ty-icon.ty-text-success {:name "check"
                                   :id "save-icon"
                                   :style "display: none;"}]]
       [:script
        {:dangerouslySetInnerHTML
         {:__html
          "(() => {
  const textarea = document.getElementById('draft-textarea');
  const status = document.getElementById('save-status');
  const icon = document.getElementById('save-icon');
  if (textarea && status && icon) {
    let saveTimeout;
    textarea.addEventListener('input', () => {
      clearTimeout(saveTimeout);
      status.textContent = 'Typing...';
      icon.style.display = 'none';
      
      saveTimeout = setTimeout(() => {
        // Simulate save
        localStorage.setItem('draft', textarea.value);
        status.textContent = 'Saved';
        icon.style.display = 'inline-block';
      }, 1000);
    });
    
    // Load saved draft
    const saved = localStorage.getItem('draft');
    if (saved) {
      textarea.value = saved;
      status.textContent = 'Draft loaded';
    }
  }
})();"}}]

       (code-block
         "const textarea = document.querySelector('ty-textarea');
let saveTimeout;

textarea.addEventListener('input', () => {
  clearTimeout(saveTimeout);
  showStatus('Typing...');
  
  saveTimeout = setTimeout(() => {
    // Save to localStorage or API
    localStorage.setItem('draft', textarea.value);
    showStatus('Saved', true);
  }, 1000);
});

// Load saved draft on page load
const saved = localStorage.getItem('draft');
if (saved) {
  textarea.value = saved;
}" :lang "javascript")]]]))

(defn technical-details []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-semibold.ty-text++.mb-4 "Technical Details"]

   [:div.space-y-6
    ;; Auto-resize Mechanism
    [:div
     [:h3.text-lg.font-medium.ty-text+.mb-3 "Auto-resize Mechanism"]
     [:p.ty-text-.mb-4
      "The textarea uses a sophisticated measurement system for smooth, performant auto-resizing:"]
     [:ul.space-y-2.ty-text-.list-disc.list-inside
      [:li "Hidden pre-formatted dummy element mirrors textarea's content and styling"]
      [:li "Exact font properties are copied (family, size, weight, line-height)"]
      [:li "Padding and borders are matched for accurate calculations"]
      [:li "Height is calculated from dummy's scrollHeight"]
      [:li "Constraints (min/max) are applied before updating"]
      [:li "Micro-adjustments (< 2px) are ignored to prevent jitter"]]]

    ;; Form Association
    [:div
     [:h3.text-lg.font-medium.ty-text+.mb-3 "Form Association"]
     [:p.ty-text-.mb-4
      "ty-textarea is a form-associated custom element that integrates seamlessly with native forms:"]
     [:ul.space-y-2.ty-text-.list-disc.list-inside
      [:li "Implements ElementInternals for native form participation"]
      [:li "Value is synchronized with FormData on submission"]
      [:li "Works with form.reset() and form validation API"]
      [:li "Compatible with HTMX form serialization"]
      [:li "Supports native browser autofill"]]]

    ;; Performance Considerations
    [:div
     [:h3.text-lg.font-medium.ty-text+.mb-3 "Performance Optimizations"]
     [:p.ty-text-.mb-4
      "Several optimizations ensure smooth performance even with large content:"]
     [:ul.space-y-2.ty-text-.list-disc.list-inside
      [:li "Resize calculations only trigger on actual content changes"]
      [:li "Dummy element is reused, not recreated on each update"]
      [:li "Height updates use direct style manipulation (no re-renders)"]
      [:li "Shadow DOM encapsulation prevents style recalculation"]
      [:li "Custom events are dispatched asynchronously to avoid blocking"]]]

    ;; Browser Support
    [:div
     [:h3.text-lg.font-medium.ty-text+.mb-3 "Browser Compatibility"]
     [:p.ty-text-.mb-4
      "Works in all modern browsers with Web Components support:"]
     [:ul.space-y-2.ty-text-.list-disc.list-inside
      [:li "Chrome/Edge 90+ (full support)"]
      [:li "Firefox 95+ (full support)"]
      [:li "Safari 15.4+ (full support)"]
      [:li "Mobile browsers with equivalent versions"]
      [:li "Polyfills available for older browsers"]]]]])

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Header
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text++.mb-2 "ty-textarea"]
    [:p.text-lg.ty-text-
     "Auto-resizing textarea component with smooth height adjustment, form integration, and comprehensive validation support."]]

   ;; API Reference (First - Most Important)
   (api-reference)

   ;; Technical Details
   (technical-details)

   ;; Basic Usage
   [:div.ty-content.rounded-lg.p-6.mb-8
    (basic-usage)]

   ;; Examples Section
   [:h2.text-2xl.font-semibold.ty-text++.mb-6 "Examples"]

   [:div.space-y-8
    ;; Size Variants
    [:div.ty-content.rounded-lg.p-6
     (size-variants)]

    ;; Form Integration
    [:div.ty-content.rounded-lg.p-6
     (form-integration)]

    ;; Height Constraints
    [:div.ty-content.rounded-lg.p-6
     (height-constraints)]

    ;; Validation States
    [:div.ty-content.rounded-lg.p-6
     (validation-states)]

    ;; Resize Options
    [:div.ty-content.rounded-lg.p-6
     (resize-options)]

    ;; Programmatic Control
    [:div.ty-content.rounded-lg.p-6
     (programmatic-control)]

    ;; Advanced Examples
    [:div.ty-content.rounded-lg.p-6
     (advanced-examples)]]

   ;; Common Patterns
   [:div.mb-8
    (common-patterns)]

   ;; Best Practices
   (best-practices)])
