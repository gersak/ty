(ns ty.site.docs.popup
  "ty-popup component documentation"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table doc-section example-section]]))

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Title and Description
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-popup"]
    [:p.text-lg.ty-text-
     "A low-level positioning primitive for creating floating UI elements. "
     "ty-popup provides automatic positioning, viewport edge detection, and scroll/resize handling. "
     "It's the foundation for tooltips, dropdowns, and other floating components."]]

   ;; API Reference Card
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-xl.font-semibold.ty-text++.mb-4 "API Reference"]

    ;; Attributes
    (attribute-table
      [{:name "open"
        :type "boolean"
        :default "false"
        :description "Controls the visibility of the popup"}
       {:name "placement"
        :type "string"
        :default "\"bottom\""
        :description "Preferred placement position: \"top\", \"bottom\", \"left\", \"right\""}
       {:name "offset"
        :type "number"
        :default "8"
        :description "Distance in pixels between the popup and anchor element"}
       {:name "flip"
        :type "boolean"
        :default "true"
        :description "Whether to flip to opposite side when there's not enough space"}])

    ;; Slots
    [:div.mt-6
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "Slots"]
     [:div.space-y-3
      [:div.flex.gap-3 [:code.ty-bg-neutral-.px-2.py-1.rounded.text-sm "anchor"]
       [:span.ty-text- "The element that triggers and anchors the popup"]]
      [:div.flex.gap-3
       [:code.ty-bg-neutral-.px-2.py-1.rounded.text-sm "(default)"]
       [:span.ty-text- "The popup content to display"]]]]]

   ;; Basic Usage Section
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-xl.font-semibold.ty-text++.mb-4 "Basic Usage"]
    [:p.ty-text-.mb-4
     "ty-popup uses a slot-based architecture. Place your anchor element in the "
     [:code "anchor"] " slot and your popup content as direct children."]

    [:div.mb-4
     [:ty-popup {:id "basic-example"
                 :open false}
      [:ty-button {:slot "anchor"
                   :on {:click (fn [] (.toggleAttribute (.getElementById js/document "basic-example") "open"))}}
       "Click to toggle popup"]
      [:div.p-4.ty-elevated.rounded-lg.shadow-lg
       [:h4.font-bold.mb-2 "Popup Content"]
       [:p.ty-text- "This is the popup content. Click the button again to close."]]]]

    (code-block
      "<!-- Basic popup with manual toggle -->
<ty-popup id=\"my-popup\" open=\"false\">
  <!-- Anchor element goes in the anchor slot -->
  <ty-button slot=\"anchor\" onclick=\"togglePopup()\">
    Click to toggle
  </ty-button>
  
  <!-- Popup content as direct children -->
  <div class=\"p-4 ty-elevated rounded-lg shadow-lg\">
    <h4>Popup Content</h4>
    <p>This is the popup content.</p>
  </div>
</ty-popup>

<script>
  function togglePopup() {
    const popup = document.getElementById('my-popup');
    popup.toggleAttribute('open');
  }
</script>")]

   ;; Examples Section
   [:h2.text-xl.font-semibold.ty-text++.mb-4 "Examples"]

   [:div.space-y-8
    ;; Placement Example
    (example-section
      "Placement Options"
      [:div.grid.grid-cols-2.gap-4
       [:ty-popup {:id "popup-top"
                   :placement "top"}
        [:ty-button {:slot "anchor"
                     :on {:click (fn [] (.toggleAttribute (.getElementById js/document "popup-top") "open"))}}
         "Top Popup"]
        [:div.p-3.ty-bg-primary-.rounded "Positioned above"]]

       [:ty-popup {:id "popup-bottom"
                   :placement "bottom"}
        [:ty-button {:slot "anchor"
                     :on {:click (fn [] (.toggleAttribute (.getElementById js/document "popup-bottom") "open"))}}
         "Bottom Popup"]
        [:div.p-3.ty-bg-success-.rounded "Positioned below"]]

       [:ty-popup {:id "popup-left"
                   :placement "left"}
        [:ty-button {:slot "anchor"
                     :on {:click (fn [] (.toggleAttribute (.getElementById js/document "popup-left") "open"))}}
         "Left Popup"]
        [:div.p-3.ty-bg-warning-.rounded "Positioned to the left"]]

       [:ty-popup {:id "popup-right"
                   :placement "right"}
        [:ty-button {:slot "anchor"
                     :on {:click (fn [] (.toggleAttribute (.getElementById js/document "popup-right") "open"))}}
         "Right Popup"]
        [:div.p-3.ty-bg-danger-.rounded "Positioned to the right"]]]
      "<!-- Different placement options -->
<ty-popup placement=\"top\">
  <button slot=\"anchor\">Top</button>
  <div>Content appears above</div>
</ty-popup>

<ty-popup placement=\"right\">
  <button slot=\"anchor\">Right</button>
  <div>Content appears to the right</div>
</ty-popup>")

    ;; Offset Example
    (example-section
      "Custom Offset"
      [:div.flex.gap-4
       [:ty-popup {:id "offset-0"
                   :offset "0"}
        [:ty-button {:slot "anchor"
                     :on {:click (fn [] (.toggleAttribute (.getElementById js/document "offset-0") "open"))}}
         "No offset"]
        [:div.p-3.ty-elevated.rounded.shadow-md "Touching the button"]]

       [:ty-popup {:id "offset-16"
                   :offset "16"}
        [:ty-button {:slot "anchor"
                     :on {:click (fn [] (.toggleAttribute (.getElementById js/document "offset-16") "open"))}}
         "16px offset"]
        [:div.p-3.ty-elevated.rounded.shadow-md "16px gap"]]

       [:ty-popup {:id "offset-32"
                   :offset "32"}
        [:ty-button {:slot "anchor"
                     :on {:click (fn [] (.toggleAttribute (.getElementById js/document "offset-32") "open"))}}
         "32px offset"]
        [:div.p-3.ty-elevated.rounded.shadow-md "32px gap"]]]
      "<!-- Custom offset distances -->
<ty-popup offset=\"0\">
  <button slot=\"anchor\">No Gap</button>
  <div>Popup touching anchor</div>
</ty-popup>

<ty-popup offset=\"24\">
  <button slot=\"anchor\">Large Gap</button>
  <div>24px away from anchor</div>
</ty-popup>")

    ;; Interactive Content Example
    (example-section
      "Interactive Content"
      [:ty-popup {:id "interactive-popup"
                  :placement "right"
                  :offset "16"}
       [:ty-button {:slot "anchor"
                    :flavor "primary"
                    :on {:click (fn [] (.toggleAttribute (.getElementById js/document "interactive-popup") "open"))}}
        "Open Settings"]
       [:div.p-4.ty-elevated.rounded-lg.shadow-lg.w-64
        [:h4.font-bold.mb-3 "Quick Settings"]
        [:div.space-y-3
         [:label.flex.items-center.gap-2
          [:input {:type "checkbox"}]
          [:span.ty-text "Enable notifications"]]
         [:label.flex.items-center.gap-2
          [:input {:type "checkbox"
                   :checked true}]
          [:span.ty-text "Auto-save"]]
         [:label.flex.items-center.gap-2
          [:input {:type "checkbox"}]
          [:span.ty-text "Dark mode"]]
         [:div.flex.gap-2.mt-4
          [:ty-button {:size "sm"
                       :flavor "success"
                       :on {:click (fn [] (.removeAttribute (.getElementById js/document "interactive-popup") "open"))}}
           "Apply"]
          [:ty-button {:size "sm"
                       :on {:click (fn [] (.removeAttribute (.getElementById js/document "interactive-popup") "open"))}}
           "Cancel"]]]]]
      "<!-- Interactive popup with form -->
<ty-popup id=\"settings-popup\" placement=\"right\" offset=\"16\">
  <ty-button slot=\"anchor\" onclick=\"toggleSettings()\">
    Open Settings
  </ty-button>
  
  <div class=\"p-4 ty-elevated rounded-lg shadow-lg w-64\">
    <h4 class=\"font-bold mb-3\">Quick Settings</h4>
    <form>
      <label>
        <input type=\"checkbox\"> Enable notifications
      </label>
      <label>
        <input type=\"checkbox\"> Auto-save
      </label>
      <button type=\"submit\">Apply</button>
    </form>
  </div>
</ty-popup>")]

   ;; Common Patterns Section
   [:h2.text-xl.font-semibold.ty-text++.mb-4.mt-8 "Common Patterns"]

   [:div.space-y-6.mb-8
    ;; User Profile Dropdown
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "User Profile Dropdown"]
     [:div.flex.justify-end
      [:ty-popup {:id "profile-popup"
                  :placement "bottom"
                  :offset "8"}
       [:button.flex.items-center.gap-2.py-1.5.px-3.rounded-lg.hover:ty-bg-neutral-.transition
        {:slot "anchor"
         :on {:click (fn [] (.toggleAttribute (.getElementById js/document "profile-popup") "open"))}}
        [:div.w-8.h-8.rounded-full.bg-gradient-to-br.from-blue-500.to-purple-500]
        [:span.ty-text "John Doe"]
        [:ty-icon {:name "chevron-down"
                   :size "sm"}]]
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
         "Sign out"]]]]]

    ;; Notification Bell
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "Notification Bell"]
     [:div.flex.justify-center
      [:ty-popup {:id "notif-popup"
                  :placement "bottom"
                  :offset "12"}
       [:button.relative.p-2.rounded-lg.hover:ty-bg-neutral-.transition
        {:slot "anchor"
         :on {:click (fn [] (.toggleAttribute (.getElementById js/document "notif-popup") "open"))}}
        [:ty-icon {:name "bell"}]
        [:span.absolute.top-1.right-1.w-2.h-2.bg-red-500.rounded-full]]
       [:div.ty-elevated.rounded-lg.shadow-lg.p-4.w-80
        [:h4.font-bold.mb-3.ty-text++ "Notifications"]
        [:div.space-y-3
         [:div.flex.gap-3
          [:div.w-2.h-2.bg-blue-500.rounded-full.mt-2]
          [:div.flex-1
           [:p.ty-text.text-sm "New comment on your post"]
           [:p.ty-text--.text-xs "2 minutes ago"]]]
         [:div.flex.gap-3
          [:div.w-2.h-2.bg-green-500.rounded-full.mt-2]
          [:div.flex-1
           [:p.ty-text.text-sm "Your report is ready"]
           [:p.ty-text--.text-xs "1 hour ago"]]]
         [:div.flex.gap-3
          [:div.w-2.h-2.bg-gray-400.rounded-full.mt-2]
          [:div.flex-1
           [:p.ty-text.text-sm "System update completed"]
           [:p.ty-text--.text-xs "Yesterday"]]]]
        [:button.w-full.mt-3.text-center.ty-text-primary.text-sm "View all notifications"]]]]]]

   ;; Programmatic Control Section
   [:h2.text-xl.font-semibold.ty-text++.mb-4.mt-8 "Programmatic Control"]

   [:div.ty-content.rounded-lg.p-6.mb-8
    [:p.ty-text-.mb-4 "Control popups through JavaScript for complex interactions."]

    (code-block
      "// Get popup element
const popup = document.querySelector('ty-popup');

// Open popup
popup.setAttribute('open', '');

// Close popup
popup.removeAttribute('open');

// Toggle popup
popup.toggleAttribute('open');

// Check if open
const isOpen = popup.hasAttribute('open');

// Change placement dynamically
popup.setAttribute('placement', 'top');

// Adjust offset
popup.setAttribute('offset', '20');

// Listen for attribute changes
const observer = new MutationObserver((mutations) => {
  mutations.forEach((mutation) => {
    if (mutation.attributeName === 'open') {
      const isOpen = popup.hasAttribute('open');
      console.log('Popup is now:', isOpen ? 'open' : 'closed');
    }
  });
});
observer.observe(popup, { attributes: true });

// Close all other popups when opening one
function openExclusive(popupId) {
  document.querySelectorAll('ty-popup[open]').forEach(popup => {
    if (popup.id !== popupId) {
      popup.removeAttribute('open');
    }
  });
  document.getElementById(popupId).setAttribute('open', '');
}" :lang "javascript")]

   ;; Best Practices Section
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-xl.font-semibold.ty-text++.mb-4 "Best Practices"]
    [:div.space-y-4
     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check-circle"}]
      [:div
       [:div.ty-text+.font-medium "Use semantic HTML in popup content"]
       [:div.ty-text-.text-sm "Structure your popup content with proper headings and ARIA labels for accessibility."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check-circle"}]
      [:div
       [:div.ty-text+.font-medium "Consider mobile experiences"]
       [:div.ty-text-.text-sm "Test popup positioning on smaller screens and touch devices."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-success.mt-0.5 {:name "check-circle"}]
      [:div
       [:div.ty-text+.font-medium "Implement click-outside closing"]
       [:div.ty-text-.text-sm "Users expect popups to close when clicking elsewhere on the page."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-danger.mt-0.5 {:name "x-circle"}]
      [:div
       [:div.ty-text+.font-medium "Don't nest interactive popups"]
       [:div.ty-text-.text-sm "Avoid placing popups inside other popups to prevent z-index and interaction issues."]]]

     [:div.flex.gap-3
      [:ty-icon.ty-text-danger.mt-0.5 {:name "x-circle"}]
      [:div
       [:div.ty-text+.font-medium "Don't auto-open on page load"]
       [:div.ty-text-.text-sm "Let users trigger popups intentionally to avoid disruptive experiences."]]]]]

   ;; Building Components Note
   [:div.ty-bg-info-.ty-border-info.border.rounded-lg.p-4.mb-8
    [:h3.ty-text-info++.font-semibold.mb-2 "Building Custom Components"]
    [:p.ty-text-info.text-sm.mb-3
     "ty-popup is designed as a low-level primitive for building higher-level components. "
     "Use it as a foundation for:"]
    [:ul.list-disc.list-inside.ty-text-info.text-sm.space-y-1
     [:li "Custom dropdowns with complex interactions"]
     [:li "Context menus with dynamic content"]
     [:li "Floating panels and palettes"]
     [:li "Custom tooltips with special behaviors"]
     [:li "Comboboxes and autocomplete components"]]]

   ;; Performance Note
   [:div.ty-bg-success-.ty-border-success.border.rounded-lg.p-4
    [:h3.ty-text-success++.font-semibold.mb-2 "Performance"]
    [:p.ty-text-success.text-sm
     "ty-popup uses ResizeObserver and RAF-throttled scroll listeners for optimal performance. "
     "Position updates are debounced to prevent layout thrashing. "
     "The component automatically cleans up observers when closed or disconnected."]]])
