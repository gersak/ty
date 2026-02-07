(ns ty.site.docs.textarea
  "Documentation for ty-textarea component - auto-resizing textarea with form integration"
  (:require
   [ty.site.docs.common
    :refer [code-block
            attribute-table
            event-table
            doc-section
            example-section
            docs-page]]))

(defn view []
  (docs-page
   ;; Header
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text++.mb-2 "ty-textarea"]
    [:p.text-lg.ty-text-
     "Auto-resizing textarea that grows with your content. No scrollbars, no manual resizing - it just works."]]

   ;; API Reference
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text++.mb-6 "API Reference"]

    [:div.space-y-6
     ;; Attributes
     [:div
      [:h3.text-lg.font-medium.ty-text+.mb-3 "Attributes"]
      (attribute-table
       [{:name "name"
         :type "string"
         :default "-"
         :description "Form field name for submission"}
        {:name "value"
         :type "string"
         :default "\"\""
         :description "Current textarea value"}
        {:name "placeholder"
         :type "string"
         :default "-"
         :description "Placeholder text when empty"}
        {:name "label"
         :type "string"
         :default "-"
         :description "Label displayed above textarea"}
        {:name "disabled"
         :type "boolean"
         :default "false"
         :description "Disables the textarea"}
        {:name "required"
         :type "boolean"
         :default "false"
         :description "Marks field as required (shows asterisk)"}
        {:name "error"
         :type "string"
         :default "-"
         :description "Error message displayed below textarea"}
        {:name "size"
         :type "string"
         :default "\"md\""
         :description "Size variant: xs, sm, md, lg, xl"}
        {:name "rows"
         :type "string"
         :default "\"3\""
         :description "Initial visible rows"}
        {:name "min-height"
         :type "string"
         :default "-"
         :description "Minimum height (e.g., \"100px\")"}
        {:name "max-height"
         :type "string"
         :default "-"
         :description "Maximum height before scrolling (e.g., \"300px\")"}
        {:name "resize"
         :type "string"
         :default "\"none\""
         :description "Manual resize: none, both, vertical, horizontal"}])]

     ;; Events
     [:div
      [:h3.text-lg.font-medium.ty-text+.mb-3 "Events"]
      (event-table
       [{:name "input"
         :payload "{value: string, originalEvent: Event}"
         :when-fired "Fired on every keystroke"}
        {:name "change"
         :payload "{value: string, originalEvent: Event}"
         :when-fired "Fired when focus leaves after value changed"}])]

     ;; Properties
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
          [:td.py-2.px-4.ty-text- "Get/set the current value programmatically"]]]]]]]]

   ;; Core Features Section
   [:h2.text-2xl.font-semibold.ty-text++.mb-6 "Core Features"]

   ;; Auto-Resize Demo
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-xl.font-semibold.ty-text+.mb-4 "✨ Auto-Resize Magic"]
    [:p.ty-text-.mb-4
     "The textarea automatically adjusts its height as you type. No scrollbars, no manual resizing needed."]

    (example-section
     "Try it yourself"
     [:ty-textarea
      {:placeholder "Start typing and watch me grow...\n\nAdd new lines...\n\nI'll expand automatically!"
       :label "Auto-resizing textarea"}]
     "<ty-textarea 
  label=\"Auto-resizing textarea\"
  placeholder=\"Start typing and watch me grow...\">
</ty-textarea>")

    [:p.ty-text-.text-sm.mt-4
     "💡 The component uses a hidden measurement element to calculate the exact height needed for your content."]]

   ;; Height Constraints
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-xl.font-semibold.ty-text+.mb-4 "📏 Height Constraints"]
    [:p.ty-text-.mb-4
     "Control the minimum and maximum height while maintaining auto-resize functionality."]

    [:div.grid.grid-cols-1.md:grid-cols-3.gap-4.mb-4
     [:div
      [:p.text-sm.ty-text.font-medium.mb-2 "Min Height (100px)"]
      [:ty-textarea
       {:placeholder "I start tall..."
        :min-height "100px"}]]

     [:div
      [:p.text-sm.ty-text.font-medium.mb-2 "Max Height (120px)"]
      [:ty-textarea
       {:placeholder "I stop growing at 120px..."
        :max-height "120px"
        :value "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nScroll appears after this"}]]

     [:div
      [:p.text-sm.ty-text.font-medium.mb-2 "Both Constraints"]
      [:ty-textarea
       {:placeholder "100px to 200px range"
        :min-height "100px"
        :max-height "200px"}]]]

    (code-block
     "<ty-textarea min-height=\"100px\"></ty-textarea>
<ty-textarea max-height=\"200px\"></ty-textarea>
<ty-textarea min-height=\"100px\" max-height=\"200px\"></ty-textarea>")]

   ;; Form Integration
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-xl.font-semibold.ty-text+.mb-4 "📝 Native Form Integration"]
    [:p.ty-text-.mb-4
     "Works seamlessly with HTML forms and FormData API - no special handling required."]

    (example-section
     "Form Example"
     [:form.space-y-4
      {:on {:submit (fn [e]
                      (.preventDefault e)
                      (let [form-data (js/FormData. (.-target e))
                            data (js/Object.fromEntries form-data)]
                        (js/alert (str "Form submitted:\n" (js/JSON.stringify data nil 2)))))}}
      [:ty-textarea
       {:label "Your Message"
        :name "message"
        :placeholder "Write your message..."
        :required true}]

      [:ty-textarea
       {:label "Additional Notes (Optional)"
        :name "notes"
        :placeholder "Any additional information..."
        :rows "2"}]

      [:button.ty-bg-primary.ty-text++.px-4.py-2.rounded
       {:type "submit"}
       "Submit Form"]]

     "<form>
  <ty-textarea
    label=\"Your Message\"
    name=\"message\"
    required>
  </ty-textarea>
  
  <button type=\"submit\">Submit</button>
</form>")

    [:div.ty-bg-info-.ty-border-info.border.rounded-lg.p-4.mt-4
     [:p.ty-text.text-sm
      [:strong "💡 Tip: "]
      "The component is form-associated and participates in form validation and submission like native textarea elements."]]]

   ;; Variants Section
   [:h2.text-2xl.font-semibold.ty-text++.mb-6 "Variants & States"]

   ;; Size Variants
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-xl.font-semibold.ty-text+.mb-4 "Size Variants"]
    [:div.grid.grid-cols-2.md:grid-cols-3.gap-4
     (for [size ["xs" "sm" "md" "lg" "xl"]]
       [:div {:key size}
        [:p.text-sm.ty-text.font-medium.mb-2 (str (clojure.string/upper-case size) (when (= size "md") " (default)"))]
        [:ty-textarea
         {:placeholder (str "Size: " size)
          :size size}]])]

    (code-block "<ty-textarea size=\"lg\"></ty-textarea>")]

   ;; Validation States
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-xl.font-semibold.ty-text+.mb-4 "Validation States"]

    [:div.grid.grid-cols-1.md:grid-cols-3.gap-4.mb-4
     [:div
      [:ty-textarea
       {:label "Required Field"
        :placeholder "This field is required..."
        :required true}]]

     [:div
      [:ty-textarea
       {:label "With Error"
        :placeholder "Something went wrong..."
        :error "Please enter at least 10 characters"
        :value "Too short"}]]

     [:div
      [:ty-textarea
       {:label "Disabled"
        :disabled true
        :value "This field is disabled"}]]]

    ;; Dynamic validation example
    [:div.mt-6
     [:h4.font-medium.ty-text.mb-2 "Dynamic Validation"]
     [:ty-textarea
      {:id "validated-textarea"
       :label "Bio (10-200 characters)"
       :placeholder "Tell us about yourself..."
       :on {:change (fn [e]
                      (let [textarea (.-target e)
                            value (.. e -detail -value)
                            length (count value)]
                        (cond
                          (< length 10)
                          (.setAttribute textarea "error" (str (- 10 length) " more characters needed"))

                          (> length 200)
                          (.setAttribute textarea "error" (str "Too long by " (- length 200) " characters"))

                          :else
                          (.removeAttribute textarea "error"))))}}]

     (code-block
      "textarea.addEventListener('change', (e) => {
  const length = e.detail.value.length;
  if (length < 10) {
    textarea.setAttribute('error', `${10 - length} more characters needed`);
  } else {
    textarea.removeAttribute('error');
  }
});" "javascript")]]

   ;; Advanced Examples
   [:h2.text-2xl.font-semibold.ty-text++.mb-6 "Advanced Examples"]

   ;; Character Counter
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-xl.font-semibold.ty-text+.mb-4 "Character Counter"]

    (example-section
     "Twitter-style Character Limit"
     [:div
      [:ty-textarea
       {:id "tweet-textarea"
        :label "Compose Tweet"
        :placeholder "What's happening?"
        :max-height "150px"}]
      [:div.flex.justify-between.items-center.mt-2
       [:span.text-sm.ty-text-- {:id "tweet-count"} "0 / 280"]
       [:button.ty-bg-primary.ty-text++.px-3.py-1.rounded.text-sm
        {:id "tweet-btn"
         :disabled true}
        "Tweet"]]
      [:script
       "(function() {
  const textarea = document.getElementById('tweet-textarea');
  const counter = document.getElementById('tweet-count');
  const button = document.getElementById('tweet-btn');
  if (textarea && counter && button) {
    textarea.addEventListener('change', (e) => {
        console.log('Event', e);
      const length = e.detail.value.length;
      counter.textContent = `${length} / 280`;
      counter.className = length > 280 ? 'text-sm ty-text-danger' : 'text-sm ty-text--';
      button.disabled = length === 0 || length > 280;
    });
  }
})();"]]

     "const textarea = document.querySelector('ty-textarea');
textarea.addEventListener('change', (e) => {
  const length = e.detail.value.length;
  updateCounter(length);
  button.disabled = length === 0 || length > 280;
});" "javascript")]

   ;; Auto-save Draft
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-xl.font-semibold.ty-text+.mb-4 "Auto-save Draft"]

    (example-section
     "Automatic Draft Saving"
     [:div
      [:ty-textarea
       {:id "draft-textarea"
        :label "Article Draft"
        :placeholder "Start writing your article..."
        :rows "5"
        :min-height "120px"}]
      [:div.flex.items-center.gap-2.mt-2
       [:span.text-sm.ty-text-- {:id "save-status"} "Ready"]
       [:ty-icon.ty-text-success
        {:name "check"
         :id "save-icon"
         :style {:display "none"}}]]
      [:script
       "(function() {
  const textarea = document.getElementById('draft-textarea');
  const status = document.getElementById('save-status');
  const icon = document.getElementById('save-icon');
  if (textarea && status && icon) {
    let saveTimeout;
    
    // Load existing draft
    const saved = localStorage.getItem('draft');
    if (saved) {
      textarea.value = saved;
      status.textContent = 'Draft loaded';
    }
    
    textarea.addEventListener('input', () => {
      clearTimeout(saveTimeout);
      status.textContent = 'Typing...';
      icon.style.display = 'none';
      
      saveTimeout = setTimeout(() => {
        localStorage.setItem('draft', textarea.value);
        status.textContent = 'Saved';
        icon.style.display = 'inline-block';
      }, 1000);
    });
  }
})();"]]

     "let saveTimeout;
textarea.addEventListener('change', () => {
  clearTimeout(saveTimeout);
  showStatus('Typing...');
  
  saveTimeout = setTimeout(() => {
    localStorage.setItem('draft', textarea.value);
    showStatus('Saved');
  }, 1000);
});" "javascript")]

   ;; Programmatic Control
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-xl.font-semibold.ty-text+.mb-4 "Programmatic Control"]

    (example-section
     "JavaScript API"
     [:div.space-y-4
      [:ty-textarea
       {:id "prog-textarea"
        :label "Controlled Textarea"
        :placeholder "Control me with JavaScript..."}]

      [:div.flex.gap-2.flex-wrap
       [:button.ty-bg-primary.ty-text++.px-3.py-1.rounded.text-sm
        {:on {:click (fn [] (set! (.-value (.getElementById js/document "prog-textarea")) "Hello from JavaScript!"))}}
        "Set Value"]
       [:button.ty-bg-secondary.ty-text++.px-3.py-1.rounded.text-sm
        {:on {:click (fn []
                       (let [el (.getElementById js/document "prog-textarea")]
                         (set! (.-value el) (str (.-value el) "\nAppended text"))))}}
        "Append"]
       [:button.ty-bg-danger.ty-text++.px-3.py-1.rounded.text-sm
        {:on {:click (fn [] (set! (.-value (.getElementById js/document "prog-textarea")) ""))}}
        "Clear"]
       [:button.ty-bg-neutral.ty-text++.px-3.py-1.rounded.text-sm
        {:on {:click (fn []
                       (js/alert (str "Current: " (.-value (.getElementById js/document "prog-textarea")))))}}
        "Get Value"]]]

     "// Set value
textarea.value = 'New content';

// Get value
const content = textarea.value;

// Listen for changes
textarea.addEventListener('change', (e) => {
  console.log('Changed:', e.detail.value);
});" "javascript")]

   ;; Best Practices
   [:div.ty-elevated.rounded-lg.p-6
    [:h2.text-2xl.font-semibold.ty-text++.mb-4 "Best Practices"]

    [:div.space-y-4
     [:div.flex.gap-3
      [:ty-icon.ty-text-success.flex-shrink-0 {:name "check-circle"}]
      [:div
       [:strong.ty-text+ "Set appropriate height constraints"]
       [:p.ty-text-.text-sm "Use min-height for better UX and max-height to prevent excessive growth."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-success.flex-shrink-0 {:name "check-circle"}]
      [:div
       [:strong.ty-text+ "Always include labels"]
       [:p.ty-text-.text-sm "Use the label attribute for accessibility and better UX."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-success.flex-shrink-0 {:name "check-circle"}]
      [:div
       [:strong.ty-text+ "Use semantic sizing"]
       [:p.ty-text-.text-sm "Choose size based on expected content: xs for tweets, xl for articles."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-danger.flex-shrink-0 {:name "x-circle"}]
      [:div
       [:strong.ty-text+ "Don't disable auto-resize without reason"]
       [:p.ty-text-.text-sm "The auto-resize feature is the component's main value proposition."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-warning.flex-shrink-0 {:name "alert-triangle"}]
      [:div
       [:strong.ty-text+ "Consider performance with huge content"]
       [:p.ty-text-.text-sm "For 10,000+ character documents, set a reasonable max-height."]]]]]))

