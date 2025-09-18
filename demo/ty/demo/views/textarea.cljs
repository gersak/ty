(ns ty.demo.views.textarea
  "Comprehensive textarea component demonstrations"
  (:require [ty.layout :as layout]))

(defn basic-demo []
  [:div
   [:h3.text-xl.font-semibold.mb-4.ty-text "📝 Basic Auto-Resize"]
   [:p.ty-text-.mb-6
    "The textarea automatically adjusts its height as you type. No scrollbars, just smooth expansion."]

   [:div.space-y-4
    [:ty-textarea
     {:label "Tell us about yourself"
      :name "bio"
      :placeholder "Start typing... The textarea will grow automatically as you add more content. Try adding multiple lines to see the magic!"
      :value "This textarea demonstrates auto-resizing functionality.\n\nTry adding more content to see it grow!\n\nIt handles line breaks naturally."}]

    [:ty-textarea
     {:label "Quick Notes"
      :name "notes"
      :placeholder "Short notes..."
      :size "sm"}]

    [:p.text-xs.ty-text--
     "💡 Tip: The textarea starts with a minimum height and grows as needed."]]])

(defn size-variants-demo []
  [:div
   [:h3.text-xl.font-semibold.mb-4.ty-text "📏 Size Variants"]
   [:p.ty-text-.mb-6
    "Different sizes for different use cases - from compact notes to detailed essays."]

   [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-6
    [:div
     [:h4.font-medium.ty-text+.mb-2 "Extra Small"]
     [:ty-textarea
      {:label "Quick note"
       :name "xs_text"
       :placeholder "Compact textarea for short notes..."
       :size "xs"}]]

    [:div
     [:h4.font-medium.ty-text+.mb-2 "Small"]
     [:ty-textarea
      {:label "Comment"
       :name "sm_text"
       :placeholder "Small textarea for comments..."
       :size "sm"}]]

    [:div
     [:h4.font-medium.ty-text+.mb-2 "Medium (Default)"]
     [:ty-textarea
      {:label "Description"
       :name "md_text"
       :placeholder "Standard textarea for descriptions..."
       :size "md"}]]

    [:div
     [:h4.font-medium.ty-text+.mb-2 "Large"]
     [:ty-textarea
      {:label "Article"
       :name "lg_text"
       :placeholder "Large textarea for articles..."
       :size "lg"}]]

    [:div
     [:h4.font-medium.ty-text+.mb-2 "Extra Large"]
     [:ty-textarea
      {:label "Essay"
       :name "xl_text"
       :placeholder "Extra large textarea for essays..."
       :size "xl"}]]]])

(defn form-integration-demo []
  [:div
   [:h3.text-xl.font-semibold.mb-4.ty-text "🔗 Form Integration & HTMX"]
   [:p.ty-text-.mb-6
    "Demonstrates proper form submission and HTMX compatibility with name attributes."]

   [:div.space-y-6
    ;; Standard form submission
    [:div.ty-elevated.p-6.rounded-lg
     [:h4.font-semibold.ty-text+.mb-4 "Standard Form Submission"]
     [:form.space-y-4
      {:onSubmit "event.preventDefault(); const formData = new FormData(event.target); console.log('📋 Form Data:'); for (let [key, value] of formData.entries()) { console.log(key + ':', value); }"}

      [:ty-textarea
       {:label "Message"
        :name "user_message"
        :placeholder "Enter your message..."
        :required true}]

      [:ty-textarea
       {:label "Additional Comments"
        :name "comments"
        :placeholder "Any additional thoughts? (Optional)"
        :rows "2"}]

      [:div.flex.gap-2
       [:button.ty-bg-primary.ty-text-white.px-4.py-2.rounded.font-medium
        {:type "submit"}
        "Submit Form"]
       [:button.ty-border.ty-text.px-4.py-2.rounded
        {:type "reset"}
        "Reset"]]

      [:p.text-sm.ty-text--
       "✅ Check browser console to see form data • Works with FormData API"]]]

    ;; HTMX simulation
    [:div.ty-elevated.p-6.rounded-lg
     [:h4.font-semibold.ty-text+.mb-4 "HTMX Compatibility"]
     [:form.space-y-4
      {:hx-post "/api/feedback"
       :hx-target "#htmx-result"
       :hx-swap "innerHTML"
       :onSubmit "event.preventDefault(); document.getElementById('htmx-result').innerHTML = '<div class=\"ty-bg-success-soft ty-text-success p-3 rounded\">✅ HTMX would submit this form to /api/feedback</div>'"}

      [:ty-textarea
       {:label "Feedback"
        :name "feedback"
        :placeholder "Share your feedback..."
        :required true}]

      [:button.ty-bg-secondary.ty-text-white.px-4.py-2.rounded.font-medium
       {:type "submit"}
       "Send via HTMX"]

      [:div#htmx-result.mt-4]]

     [:p.text-sm.ty-text--
      "🚀 HTMX attributes: hx-post, hx-target, hx-swap work seamlessly"]]]])

(defn validation-demo []
  [:div
   [:h3.text-xl.font-semibold.mb-4.ty-text "✅ Validation & States"]
   [:p.ty-text-.mb-6
    "Error states, required fields, and validation feedback."]

   [:div.space-y-6
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
     ;; Required field
     [:div
      [:h4.font-medium.ty-text+.mb-2 "Required Field"]
      [:ty-textarea
       {:label "Project Description"
        :name "project_desc"
        :placeholder "Describe your project..."
        :required true
        :rows "3"}]
      [:p.text-sm.ty-text--
       "📌 Required fields show red asterisk"]]

     ;; Error state
     [:div
      [:h4.font-medium.ty-text+.mb-2 "Error State"]
      [:ty-textarea
       {:label "Review Comments"
        :name "review_error"
        :placeholder "Enter your review..."
        :error "Review must be at least 10 characters long"
        :value "Too short"}]
      [:p.text-sm.ty-text--
       "❌ Error styling and message"]]]

    ;; Disabled state
    [:div
     [:h4.font-medium.ty-text+.mb-2 "Disabled State"]
     [:ty-textarea
      {:label "System Generated"
       :name "system_gen"
       :placeholder "This field is read-only"
       :disabled true
       :value "This content is generated automatically and cannot be edited by users."}]
     [:p.text-sm.ty-text--
      "🚫 Disabled textareas are not editable"]]]])

(defn content-examples-demo []
  [:div
   [:h3.text-xl.font-semibold.mb-4.ty-text "📚 Content Examples"]
   [:p.ty-text-.mb-6
    "See how the textarea handles different types of content and use cases."]

   [:div.space-y-6
    ;; Short content
    [:div
     [:h4.font-medium.ty-text+.mb-2 "Short Content"]
     [:ty-textarea
      {:label "Quick Summary"
       :name "summary"
       :placeholder "Brief summary..."
       :value "This is a short piece of content that fits on one line."}]]

    ;; Multi-paragraph content
    [:div
     [:h4.font-medium.ty-text+.mb-2 "Multi-Paragraph Text"]
     [:ty-textarea
      {:label "Article Draft"
       :name "article_draft"
       :placeholder "Write your article..."
       :value "This is the first paragraph of the article. It introduces the main topic and provides context for what follows.\n\nThe second paragraph expands on the ideas presented in the introduction. Notice how the textarea automatically adjusts its height to accommodate multiple paragraphs.\n\nThe third paragraph concludes the article with final thoughts and takeaways. The auto-resize feature ensures all content is visible without scrolling."}]]

    ;; Code/structured content
    [:div
     [:h4.font-medium.ty-text+.mb-2 "Structured Content"]
     [:ty-textarea
      {:label "Code Snippet"
       :name "code_snippet"
       :placeholder "Paste your code..."
       :value "function autoResize(textarea) {\n  const dummy = document.createElement('pre');\n  dummy.style.visibility = 'hidden';\n  dummy.style.position = 'absolute';\n  dummy.textContent = textarea.value;\n  \n  document.body.appendChild(dummy);\n  const height = dummy.scrollHeight;\n  document.body.removeChild(dummy);\n  \n  textarea.style.height = height + 'px';\n}"}]]

    ;; List content
    [:div
     [:h4.font-medium.ty-text+.mb-2 "List Content"]
     [:ty-textarea
      {:label "Project Requirements"
       :name "requirements"
       :placeholder "List your requirements..."
       :value "Project Requirements:\n\n• Auto-resizing functionality\n• Form integration support\n• HTMX compatibility\n• Multiple size variants\n• Error state handling\n• Accessibility compliance\n• Clean, minimal design\n• Performance optimized\n• Cross-browser support"}]]]])

(defn resize-options-demo []
  [:div
   [:h3.text-xl.font-semibold.mb-4.ty-text "🔄 Resize Options"]
   [:p.ty-text-.mb-6
    "Control how users can resize textareas - auto-resize vs manual resize controls."]

   [:div.space-y-6
    ;; Custom height constraints - NEW TEST SECTION
    [:div
     [:h4.font-medium.ty-text+.mb-4 "Height Constraints (Fixed!)"]
     [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-4
      [:div
       [:h5.font-medium.ty-text.mb-2 "Custom Min Height"]
       [:ty-textarea
        {:label "Tall Start"
         :name "custom_min"
         :placeholder "This starts at 120px tall..."
         :min-height "120px"
         :size "sm"}]
       [:p.text-xs.ty-text--
        "min-height=\"120px\" with size=\"sm\""]]

      [:div
       [:h5.font-medium.ty-text.mb-2 "Max Height + Scroll"]
       [:ty-textarea
        {:label "Limited Growth"
         :name "custom_max"
         :placeholder "Type a lot of content here. This textarea will grow up to 150px, then show scrollbars..."
         :max-height "150px"
         :value "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10"}]
       [:p.text-xs.ty-text--
        "max-height=\"150px\" - scrolls when full"]]

      [:div
       [:h5.font-medium.ty-text.mb-2 "Min + Max Range"]
       [:ty-textarea
        {:label "Height Range"
         :name "height_range"
         :placeholder "Starts at 80px, grows up to 200px..."
         :min-height "80px"
         :max-height "200px"}]
       [:p.text-xs.ty-text--
        "min=\"80px\" max=\"200px\""]]]]

    [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
     ;; Auto-resize (default)
     [:div
      [:h4.font-medium.ty-text+.mb-2 "Auto-Resize (Default)"]
      [:ty-textarea
       {:label "Auto-Growing"
        :name "auto_grow"
        :placeholder "Type here and watch it grow..."
        :value "This textarea automatically adjusts its height based on content. Manual resize is disabled."}]
      [:p.text-sm.ty-text--
       "📏 Automatic height adjustment only"]]

     ;; Manual resize allowed
     [:div
      [:h4.font-medium.ty-text+.mb-2 "Manual Resize Allowed"]
      [:ty-textarea
       {:label "User Resizable"
        :name "manual_resize"
        :placeholder "You can drag to resize..."
        :resize "both"
        :value "This textarea allows manual resizing. Drag the bottom-right corner to adjust size."}]
      [:p.text-sm.ty-text--
       "↗️ Drag corner to resize manually"]]

     ;; Vertical only
     [:div
      [:h4.font-medium.ty-text+.mb-2 "Vertical Resize Only"]
      [:ty-textarea
       {:label "Height Adjustable"
        :name "vertical_resize"
        :placeholder "Resize vertically..."
        :resize "vertical"
        :value "This textarea can be resized vertically but not horizontally."}]
      [:p.text-sm.ty-text--
       "↕️ Vertical resize only"]]

     ;; Horizontal only
     [:div
      [:h4.font-medium.ty-text+.mb-2 "Horizontal Resize Only"]
      [:ty-textarea
       {:label "Width Adjustable"
        :name "horizontal_resize"
        :placeholder "Resize horizontally..."
        :resize "horizontal"
        :value "This textarea can be resized horizontally but not vertically."}]
      [:p.text-sm.ty-text--
       "↔️ Horizontal resize only"]]]]])

(defn responsive-demo []
  [:div
   [:h3.text-xl.font-semibold.mb-4.ty-text "📱 Responsive Behavior"]
   [:p.ty-text-.mb-6
    "Textareas adapt to container size and work well on mobile devices."]

   [:div.space-y-6
    ;; Container-aware scaling
    [:div
     [:h4.font-medium.ty-text+.mb-4 "Container-Aware Scaling"]
     [:div.grid.grid-cols-1.md:grid-cols-3.gap-4
      [:div.ty-elevated.p-4.rounded-lg
       [:h5.font-medium.ty-text.mb-2 "Narrow Container"]
       [:ty-textarea
        {:label "Mobile Note"
         :name "mobile_note"
         :placeholder "Optimized for mobile..."
         :size "sm"}]]

      [:div.ty-elevated.p-4.rounded-lg
       [:h5.font-medium.ty-text.mb-2 "Medium Container"]
       [:ty-textarea
        {:label "Tablet Description"
         :name "tablet_desc"
         :placeholder "Good for tablets..."
         :size "md"}]]

      [:div.ty-elevated.p-4.rounded-lg
       [:h5.font-medium.ty-text.mb-2 "Wide Container"]
       [:ty-textarea
        {:label "Desktop Essay"
         :name "desktop_essay"
         :placeholder "Full desktop experience..."
         :size "lg"}]]]]

    ;; Mobile-specific considerations
    [:div.ty-elevated.p-6.rounded-lg
     [:h4.font-medium.ty-text+.mb-4 "Mobile Optimization"]
     [:div.space-y-4
      [:ty-textarea
       {:label "Mobile-Friendly Input"
        :name "mobile_input"
        :placeholder "Touch-friendly textarea with appropriate sizing..."
        :size "md"
        :value "This textarea is optimized for mobile devices:\n\n• Touch-friendly tap targets\n• Appropriate text sizing\n• Smooth auto-resize\n• No horizontal overflow"}]

      [:p.text-sm.ty-text--
       "📱 Automatically adjusts sizing on smaller screens"]]]]])

(defn performance-demo []
  [:div
   [:h3.text-xl.font-semibold.mb-4.ty-text "⚡ Performance Features"]
   [:p.ty-text-.mb-6
    "Optimized for smooth performance even with large amounts of text."]

   [:div.space-y-6
    [:div.ty-elevated.p-6.rounded-lg
     [:h4.font-medium.ty-text+.mb-4 "Large Content Handling"]
     [:ty-textarea
      {:label "Performance Test"
       :name "large_content"
       :placeholder "Paste large amounts of text to test performance..."
       :value "This textarea is optimized for performance:\n\n• Debounced resize calculations\n• Efficient DOM updates\n• Smooth height transitions\n• Memory-efficient dummy element management\n• Minimal re-renders\n\nTry pasting a large document here to see how it handles substantial content. The auto-resize feature uses a hidden dummy element to calculate the required height efficiently.\n\nThe component only triggers resize calculations when the content actually changes, preventing unnecessary work. Height transitions are smooth but not excessive, maintaining good performance.\n\nEven with thousands of lines of text, the textarea should remain responsive and smooth. The implementation is based on the proven pattern from the toddler project, ensuring reliability and performance."}]

     [:p.text-sm.ty-text--
      "🚀 Optimized resize calculations • Smooth transitions • Memory efficient"]]]])

(defn view []
  (layout/with-window
    [:div.p-8.max-w-6xl.mx-auto.space-y-12.ty-text-
     ;; Header
     [:div
      [:h1.text-4xl.font-bold.mb-4.ty-text "Textarea Components"]
      [:p.text-lg.ty-text-.mb-2
       "Auto-resizing textareas with HTMX compatibility, form integration, and smooth user experience."]
      [:p.text-sm.ty-text--.mb-6
       "Built with the proven auto-resize pattern from toddler, optimized for modern web applications."]]

     ;; Basic functionality
     [:div.ty-elevated.rounded-lg.p-8
      (basic-demo)]

     ;; Size variants
     [:div.ty-elevated.rounded-lg.p-8
      (size-variants-demo)]

     ;; Form integration
     [:div.ty-elevated.rounded-lg.p-8
      (form-integration-demo)]

     ;; Validation and states
     [:div.ty-elevated.rounded-lg.p-8
      (validation-demo)]

     ;; Content examples
     [:div.ty-elevated.rounded-lg.p-8
      (content-examples-demo)]

     ;; Resize options
     [:div.ty-elevated.rounded-lg.p-8
      (resize-options-demo)]

     ;; Responsive behavior
     [:div.ty-elevated.rounded-lg.p-8
      (responsive-demo)]

     ;; Performance
     [:div.ty-elevated.rounded-lg.p-8
      (performance-demo)]]))
