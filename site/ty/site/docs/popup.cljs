(ns ty.site.docs.popup
  "ty-popup component documentation"
  (:require [ty.site.docs.common
             :refer [code-block
                     attribute-table
                     event-table
                     doc-section
                     example-section]]))

(defn header-section []
  [:div.mb-8
   [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-popup"]
   [:p.text-lg.ty-text-.mb-4
    "Interactive popup component with built-in behaviors like click-outside-to-close and keyboard support. "
    "Uses a clean parent-child relationship and provides automatic positioning with viewport edge detection."]

   [:div.ty-bg-success-.ty-border-success.border.rounded-lg.p-4.mb-4
    [:h3.ty-text-success++.font-semibold.mb-2 "✨ Shadow Infrastructure Built-in"]
    [:p.ty-text-success.text-sm
     "ty-popup provides proper shadow space infrastructure - shadows from " [:code.ty-bg-success.ty-text-success++.px-1.rounded "ty-elevated"]
     " and custom shadow classes render without clipping, even near viewport edges."]]])

(defn api-reference-section []
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
    (code-block
      "<!-- Parent-child relationship with defaults: click to open, ESC/outside to close -->\n<ty-button>\n  Click me\n  <ty-popup>\n    <div>Popup content</div>\n  </ty-popup>\n</ty-button>")]])

(defn basic-usage-section []
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
</ty-button>")])

(defn current-best-practices-section []
  [:div
   [:h2.text-xl.font-semibold.ty-text++.mb-4.mt-8 "Current Best Practices"]

   [:div.space-y-6
    ;; ClojureScript Best Practice
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "ClojureScript/Reagent Pattern"]
     [:p.ty-text-.mb-4 "Modern approach using event handlers and .closest() to find and close popups."]

     [:div.mb-4
      [:ty-button {:flavor "primary"}
       "Delete Confirmation"
       [:ty-popup {:placement "bottom"
                   :disable-close true}
        [:div.p-6.ty-elevated.rounded-lg.shadow-lg.w-96
         [:h4.ty-text++.font-bold.mb-3 "Delete Item?"]
         [:p.ty-text-.mb-6 "This action cannot be undone. The item will be permanently deleted."]
         [:div.flex.gap-3.justify-end
          [:ty-button {:flavor "danger"
                       :size "sm"
                       :on {:click (fn [^js e]
                                     (js/alert "Item deleted!")
                                     (.closePopup (.closest (.-target e) "ty-popup")))}}
           "Delete"]
          [:ty-button {:flavor "ghost"
                       :size "sm"
                       :on {:click (fn [^js e]
                                     (.closePopup (.closest (.-target e) "ty-popup")))}}
           "Cancel"]]]]]]

     (code-block
       ";; ClojureScript - Current Best Practice\n[:ty-button {:flavor \"primary\"}\n \"Delete Item\"\n [:ty-popup {:placement \"bottom\" :disable-close true}\n  [:div.p-6.ty-elevated.rounded-lg.w-96\n   [:h4 \"Delete Item?\"]\n   [:p \"This action cannot be undone.\"]\n   [:div.flex.gap-3.justify-end\n    [:ty-button {:flavor \"danger\" :size \"sm\"\n                 :on {:click (fn [^js e]\n                               (delete-item! item-id)\n                               ;; Close using closest - CURRENT BEST PRACTICE\n                               (.closePopup (.closest (.-target e) \"ty-popup\")))}}]\n     \"Delete\"]\n    [:ty-button {:flavor \"ghost\" :size \"sm\"\n                 :on {:click (fn [^js e]\n                               (.closePopup (.closest (.-target e) \"ty-popup\")))}}]\n     \"Cancel\"]]]]]" "clojure")]

    ;; JavaScript Best Practice
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-semibold.ty-text+.mb-3 "JavaScript Pattern"]
     [:p.ty-text-.mb-4 "Clean pattern for closing popups from within their content."]

     (code-block
       "// JavaScript - Current Best Practice\n\n// Generic close function for any button inside popup\nfunction closePopup(buttonElement) {\n  const popup = buttonElement.closest('ty-popup');\n  if (popup) {\n    popup.closePopup();\n  }\n}\n\n// React example\nfunction ConfirmDialog({ onConfirm }) {\n  const handleConfirm = (e) => {\n    onConfirm();\n    // Close popup using closest - CURRENT BEST PRACTICE\n    const popup = e.target.closest('ty-popup');\n    popup?.closePopup();\n  };\n\n  return (\n    <div className=\"p-6 ty-elevated rounded-lg w-96\">\n      <h4>Confirm Action</h4>\n      <button onClick={handleConfirm}>\n        Confirm\n      </button>\n      <button onClick={(e) => {\n        const popup = e.target.closest('ty-popup');\n        popup?.closePopup();\n      }}>\n        Cancel\n      </button>\n    </div>\n  );\n}" "javascript")]]])

(defn examples-section []
  [:div
   [:h2.text-xl.font-semibold.ty-text++.mb-4 "Examples"]

   [:div.space-y-8
    ;; Contact Form Example
    (example-section
      "Interactive Contact Form"
      [:ty-button {:flavor "primary"}
       "Contact Form"
       [:ty-popup {:placement "bottom"
                   :offset "8"}
        [:div.p-6.ty-elevated.rounded-lg.w-80.border.ty-border+
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
                        :on {:click (fn [^js e]
                                      (js/alert "Form submitted!")
                                      (.closePopup (.closest (.-target e) "ty-popup")))}}
            "Submit"]
           [:ty-button {:size "sm"
                        :on {:click (fn [^js e]
                                      (.closePopup (.closest (.-target e) "ty-popup")))}}
            "Cancel"]]]]]]
      "<!-- Interactive popup with form controls -->
<ty-button flavor=\"primary\">
  Contact Form
  <ty-popup placement=\"bottom\">
    <div class=\"p-6 ty-elevated rounded-lg shadow-lg w-80\">
      <h4 class=\"font-bold mb-4\">Contact Us</h4>
      <div class=\"space-y-4\">
        <ty-input label=\"Name\" placeholder=\"Your name\" required></ty-input>
        <ty-input label=\"Email\" type=\"email\" placeholder=\"your@email.com\" required></ty-input>
        <ty-textarea label=\"Message\" placeholder=\"Your message...\" rows=\"3\" required></ty-textarea>
        
        <div class=\"flex gap-2 pt-2\">
          <ty-button flavor=\"success\" size=\"sm\" onclick=\"submitAndClose(this)\">
            Submit
          </ty-button>
          <ty-button size=\"sm\" onclick=\"closePopup(this)\">
            Cancel
          </ty-button>
        </div>
      </div>
    </div>
  </ty-popup>
</ty-button>")

    ;; Protected Dialog Example
    (example-section
      "Protected Dialog (disable-close)"
      [:ty-button {:flavor "warning"}
       "Protected Dialog"
       [:ty-popup {:placement "bottom"
                   :disable-close true}
        [:div.p-6.ty-elevated.rounded-lg.w-80
         [:h4.ty-text++.font-bold.mb-4 "Are you sure?"]
         [:p.ty-text-.mb-6 "This popup won't close when clicking outside or pressing ESC. You must choose an action."]
         [:div.flex.gap-2.justify-end
          [:ty-button {:flavor "danger"
                       :size "sm"
                       :on {:click (fn [^js e]
                                     (js/alert "Deleted!")
                                     (.closePopup (.closest (.-target e) "ty-popup")))}}
           "Delete"]
          [:ty-button {:size "sm"
                       :on {:click (fn [^js e]
                                     (.closePopup (.closest (.-target e) "ty-popup")))}}
           "Cancel"]]]]]
      "<!-- Popup that ignores outside clicks and ESC key -->
<ty-button flavor=\"warning\">
  Protected Dialog
  <ty-popup placement=\"bottom\" disable-close=\"true\">
    <div class=\"p-6 ty-elevated rounded-lg w-80\">
      <h4>Are you sure?</h4>
      <p>This popup won't close when clicking outside or pressing ESC.</p>
      <div class=\"flex gap-2 justify-end\">
        <ty-button flavor=\"danger\" size=\"sm\" onclick=\"confirmAndClose(this)\">
          Delete
        </ty-button>
        <ty-button size=\"sm\" onclick=\"closePopup(this)\">
          Cancel
        </ty-button>
      </div>
    </div>
  </ty-popup>
</ty-button>")

    ;; Placement Options Example
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
</ty-button>")]])

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   (header-section)
   (api-reference-section)
   (basic-usage-section)
   (examples-section)
   (current-best-practices-section)])
