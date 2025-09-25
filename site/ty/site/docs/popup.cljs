(ns ty.site.docs.popup
  "ty-popup component documentation"
  (:require [ty.site.docs.common
             :refer [code-block
                     attribute-table
                     event-table
                     doc-section
                     example-section]]))

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Title and Description
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-popup"]
    [:p.text-lg.ty-text-
     "Interactive popup component with built-in behaviors like click-outside-to-close and keyboard support. "
     "Uses a clean parent-child relationship and provides automatic positioning with viewport edge detection."]]

   ;; API Reference Card
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-xl.font-semibold.ty-text++.mb-4 "API Reference"]

    ;; Attributes
    (attribute-table
      [{:name "manual"
        :type "boolean"
        :default "false"
        :description "Override click trigger - popup opens only via JavaScript"}
       {:name "disable-close"
        :type "boolean"
        :default "false"
        :description "Disable auto-close - popup closes only via JavaScript"}
       {:name "placement"
        :type "string"
        :default "\"bottom\""
        :description "Preferred placement position: \"top\", \"bottom\", \"left\", \"right\""}
       {:name "offset"
        :type "number"
        :default "8"
        :description "Distance in pixels between the popup and anchor element"}])

    ;; Structure
    [:div.mt-6
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "Structure"]
     [:div.ty-bg-neutral-.rounded-lg.p-4
      [:code.text-sm.ty-text
       "<!-- Parent-child relationship with defaults: click to open, ESC/outside to close -->\n"
       "<ty-button>\n"
       "  Click me\n"
       "  <ty-popup>\n"
       "    <div>Popup content</div>\n"
       "  </ty-popup>\n"
       "</ty-button>"]]]]

   ;; Basic Usage Section
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-xl.font-semibold.ty-text++.mb-4 "Basic Usage"]
    [:p.ty-text-.mb-4
     "ty-popup uses smart defaults: click the parent element to open, ESC key or click outside to close. "
     "The popup automatically detects its parent as the anchor and positions relative to it."]

    [:div.mb-4
     [:ty-button {:flavor "primary"}
      "Click to Open Popup"
      [:ty-popup {:placement "bottom"}
       [:div.p-4.ty-elevated.rounded-lg.shadow-lg
        [:h4.font-bold.mb-2 "Popup Content"]
        [:p.ty-text-.mb-3 "This popup opens on click and closes when you click outside or press ESC."]
        [:div.flex.gap-2
         [:ty-button {:flavor "success"
                      :size "sm"}
          "Action"]
         [:ty-button {:size "sm"}
          "Cancel"]]]]]]

    (code-block
      "<!-- Basic popup with smart defaults -->
<ty-button flavor=\"primary\">
  Click to Open Popup
  <ty-popup placement=\"bottom\">
    <div class=\"p-4 ty-elevated rounded-lg shadow-lg\">
      <h4>Popup Content</h4>
      <p>Opens on click, closes with ESC or click outside</p>
    </div>
  </ty-popup>
</ty-button>")]

   ;; Examples Section
   [:h2.text-xl.font-semibold.ty-text++.mb-4 "Examples"]

   [:div.space-y-8
    ;; Placement Example
    (example-section
      "Placement Options"
      [:div.grid.grid-cols-2.gap-4.items-center
       [:div
        [:ty-button {:flavor "primary"}
         "Top Popup"
         [:ty-popup {:placement "top"}
          [:div.p-3.ty-bg-primary+.rounded.ty-text++ "Appears above"]]]]

       [:div
        [:ty-button {:flavor "success"}
         "Bottom Popup"
         [:ty-popup {:placement "bottom"}
          [:div.p-3.ty-bg-success+.rounded.ty-text++ "Appears below"]]]]

       [:div
        [:ty-button {:flavor "warning"}
         "Left Popup"
         [:ty-popup {:placement "left"}
          [:div.p-3.ty-bg-warning+.rounded.ty-text++ "Appears to the left"]]]]

       [:div
        [:ty-button
         {:flavor "danger"}
         "Right Popup"
         [:ty-popup {:placement "right"}
          [:div.p-3.ty-bg-danger+.rounded.ty-text++ "Appears to the right"]]]]]
      "<!-- Different placement options -->
<ty-button flavor=\"primary\">
  Top Popup
  <ty-popup placement=\"top\">
    <div>Content appears above</div>
  </ty-popup>
</ty-button>

<ty-button flavor=\"success\">
  Right Popup  
  <ty-popup placement=\"right\">
    <div>Content appears to the right</div>
  </ty-popup>
</ty-button>")

    ;; Interactive Content Example
    (example-section
      "Interactive Content & Closing"
      [:ty-button {:flavor "primary"}
       "Contact Form"
       [:ty-popup {:placement "bottom"
                   :offset "8"}
        [:div.p-6.ty-elevated.rounded-lg.shadow-lg.w-80
         [:h4.font-bold.mb-4 "Contact Us"]
         [:div.space-y-4
          [:ty-input
           {:label "Name"
            :placeholder "Your name"
            :required true}]
          [:ty-input
           {:label "Email"
            :type "email"
            :placeholder "your@email.com"
            :required true}]
          [:ty-textarea
           {:label "Message"
            :placeholder "Your message..."
            :rows 3
            :required true}]
          [:div.flex.gap-2.pt-2
           [:ty-button {:flavor "success"
                        :size "sm"
                        :on {:click (fn [e]
                                      (js/alert "Form submitted!")
                                      ;; Close popup using custom event
                                      (.dispatchEvent
                                        (-> e .-target (.closest "ty-popup"))
                                        (js/CustomEvent. "ty:close-popup")))}}
            "Submit"]
           [:ty-button {:size "sm"
                        :on {:click (fn [^js e]
                                      ;; Close popup using direct method
                                      (let [popup (-> e .-target (.closest "ty-popup"))]
                                        (.closePopup popup)))}}
            "Cancel"]]]]]]
      "<!-- Interactive popup with form controls and close buttons -->
<ty-button flavor=\"primary\">
  Contact Form
  <ty-popup trigger=\"click\" placement=\"bottom\">
    <div class=\"p-6 ty-elevated rounded-lg shadow-lg w-80\">
      <h4 class=\"font-bold mb-4\">Contact Us</h4>
      <div class=\"space-y-4\">
        <ty-input label=\"Name\" placeholder=\"Your name\" required></ty-input>
        <ty-input label=\"Email\" type=\"email\" placeholder=\"your@email.com\" required></ty-input>
        <ty-textarea label=\"Message\" placeholder=\"Your message...\" rows=\"3\" required></ty-textarea>
        
        <div class=\"flex gap-2 pt-2\">
          <ty-button flavor=\"success\" size=\"sm\" type=\"button\" onclick=\"submitAndClose(this)\">
            Submit
          </ty-button>
          <ty-button size=\"sm\" type=\"button\" onclick=\"closePopup(this)\">
            Cancel
          </ty-button>
        </div>
      </div>
    </div>
  </ty-popup>
</ty-button>

<script>
function submitAndClose(button) {
  // Handle form submission
  alert('Form submitted!');
  
  // Close popup using custom event
  const popup = button.closest('ty-popup');
  popup.dispatchEvent(new CustomEvent('ty:close-popup'));
}

function closePopup(button) {
  // Close popup using direct method
  const popup = button.closest('ty-popup');
  popup.closePopup();
}
</script>")]

   ;; Common Patterns Section
   [:h2.text-xl.font-semibold.ty-text++.mb-4.mt-8 "Common Patterns"]

   [:div.space-y-6.mb-8
    ;; User Profile Menu
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "User Profile Menu"]
     [:div.flex.justify-end
      [:ty-button {:flavor "ghost"
                   :class "flex items-center gap-2"}
       [:div.w-8.h-8.rounded-full.bg-gradient-to-br.from-blue-500.to-purple-500]
       [:span "John Doe"]
       [:ty-icon {:name "chevron-down"
                  :size "sm"}]
       [:ty-popup {:placement "bottom"
                   :offset "8"}
        [:div.ty-elevated.rounded-lg.shadow-lg.py-2
         {:class "min-w-[200px]"}
         [:div.px-4.py-2.border-b.ty-border
          [:div.ty-text+.font-medium "John Doe"]
          [:div.ty-text--.text-xs "john@example.com"]]
         [:button.w-full.text-left.px-4.py-2.hover:ty-bg-neutral-.ty-text.flex.items-center.gap-2
          [:ty-icon {:name "user"
                     :size "sm"}]
          "Profile"]
         [:button.w-full.text-left.px-4.py-2.hover:ty-bg-neutral-.ty-text.flex.items-center.gap-2
          [:ty-icon {:name "settings"
                     :size "sm"}]
          "Settings"]
         [:hr.my-1.ty-border]
         [:button.w-full.text-left.px-4.py-2.hover:ty-bg-danger-.ty-text-danger.flex.items-center.gap-2
          [:ty-icon {:name "log-out"
                     :size "sm"}]
          "Sign out"]]]]]]

    ;; Action Menu
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "Action Menu"]
     [:div.flex.justify-center
      [:ty-button {:flavor "ghost"
                   :size "sm"
                   :class "p-2"}
       [:ty-icon {:name "more-vertical"}]
       [:ty-popup {:placement "bottom"
                   :offset "8"}
        [:div.ty-elevated.rounded-lg.shadow-lg.py-2.w-48
         [:button.w-full.text-left.px-4.py-2.hover:ty-bg-neutral-.ty-text.flex.items-center.gap-2
          [:ty-icon {:name "edit"
                     :size "sm"}]
          "Edit"]
         [:button.w-full.text-left.px-4.py-2.hover:ty-bg-neutral-.ty-text.flex.items-center.gap-2
          [:ty-icon {:name "copy"
                     :size "sm"}]
          "Duplicate"]
         [:button.w-full.text-left.px-4.py-2.hover:ty-bg-neutral-.ty-text.flex.items-center.gap-2
          [:ty-icon {:name "share"
                     :size "sm"}]
          "Share"]
         [:hr.my-1.ty-border]
         [:button.w-full.text-left.px-4.py-2.hover:ty-bg-danger-.ty-text-danger.flex.items-center.gap-2
          [:ty-icon {:name "trash"
                     :size "sm"}]
          "Delete"]]]]]]]

   ;; Programmatic Control Section
   [:h2.text-xl.font-semibold.ty-text++.mb-4.mt-8 "Programmatic Control"]

   [:div.ty-content.rounded-lg.p-6.mb-8
    [:p.ty-text-.mb-4 "Control popups programmatically for advanced interactions."]

    (code-block
      "// Get popup element
const popup = document.querySelector('ty-popup');

// Open popup
popup.openPopup();

// Close popup  
popup.closePopup();

// Toggle popup
popup.togglePopup();

// Close from inside popup content (two ways):

// 1. Using custom event
const closeButton = document.querySelector('#close-btn');
closeButton.addEventListener('click', () => {
  // Dispatch close event that popup will listen for
  popup.dispatchEvent(new CustomEvent('ty:close-popup'));
});

// 2. Direct method call
closeButton.addEventListener('click', () => {
  popup.closePopup();
});

// Listen for open/close events
popup.addEventListener('ty:popup-open', () => {
  console.log('Popup opened');
});

popup.addEventListener('ty:popup-close', () => {
  console.log('Popup closed');
});

// Change trigger behavior
popup.setAttribute('trigger', 'manual');

// Change placement
popup.setAttribute('placement', 'top');")]

   ;; Migration from v1
   [:div.ty-bg-warning-.ty-border-warning.border.rounded-lg.p-4.mb-8
    [:h3.ty-text-warning++.font-semibold.mb-2 "Migration from v1 (Slot-based)"]
    [:p.ty-text-warning.text-sm.mb-3
     "The new ty-popup uses a parent-child pattern instead of slots. Here's how to migrate:"]

    [:div.grid.grid-cols-1.md:grid-cols-2.gap-4.text-sm
     [:div
      [:div.font-medium.ty-text-warning.mb-2 "Old (v1) - Slots"]
      [:code.ty-bg-warning--.p-2.rounded.block.whitespace-pre
       "<ty-popup>\n  <ty-button slot=\"anchor\">\n    Click me\n  </ty-button>\n  <div>Content</div>\n</ty-popup>"]]
     [:div
      [:div.font-medium.ty-text-warning.mb-2 "New (v2) - Parent-Child"]
      [:code.ty-bg-warning--.p-2.rounded.block.whitespace-pre
       "<ty-button>\n  Click me\n  <ty-popup trigger=\"click\">\n    <div>Content</div>\n  </ty-popup>\n</ty-button>"]]]]

   ;; Best Practices Section
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-xl.font-semibold.ty-text++.mb-4 "Best Practices"]
    [:div.space-y-4
     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check-circle"}]
      [:div
       [:div.ty-text+.font-medium "Use ty-button for consistent styling"]
       [:div.ty-text-.text-sm "Leverage ty-button flavors and sizes for better design consistency."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check-circle"}]
      [:div
       [:div.ty-text+.font-medium "Keep popup content focused"]
       [:div.ty-text-.text-sm "Popups should contain related actions or information, not complex layouts."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check-circle"}]
      [:div
       [:div.ty-text+.font-medium "Test on mobile devices"]
       [:div.ty-text-.text-sm "Ensure popups work well with touch interactions and smaller screens."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-danger.mt-0.5 {:name "x-circle"}]
      [:div
       [:div.ty-text+.font-medium "Don't nest popups deeply"]
       [:div.ty-text-.text-sm "Avoid popups inside popups to prevent z-index and interaction conflicts."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-danger.mt-0.5 {:name "x-circle"}]
      [:div
       [:div.ty-text+.font-medium "Don't make critical content popup-only"]
       [:div.ty-text-.text-sm "Important actions should be accessible without requiring popup interactions."]]]]]

   ;; Performance Note
   [:div.ty-bg-success-.ty-border-success.border.rounded-lg.p-4
    [:h3.ty-text-success++.font-semibold.mb-2 "Performance & Accessibility"]
    [:p.ty-text-success.text-sm.mb-3
     "ty-popup automatically handles:"]
    [:ul.list-disc.list-inside.ty-text-success.text-sm.space-y-1
     [:li "Viewport edge detection and automatic repositioning"]
     [:li "Keyboard navigation (ESC key support)"]
     [:li "Click-outside-to-close behavior"]
     [:li "Body scroll locking when popup is open"]
     [:li "Automatic cleanup of event listeners"]
     [:li "Custom events for open/close state changes"]
     [:li "Smooth CSS animations with hardware acceleration"]]]])
