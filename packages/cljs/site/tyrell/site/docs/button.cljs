(ns tyrell.site.docs.button
  "Documentation for ty-button component"
  (:require [tyrell.site.docs.common :refer [code-block attribute-table event-table docs-page]]))

(defn view []
  (docs-page
   ;; Title and Description
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-button"]
    [:p.text-lg.ty-text- "A flexible button component with multiple styles and sizes."]]

   ;; API Reference Card
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text.mb-6 "API Reference"]

    ;; Attributes
    [:div.mb-8
     [:h3.text-lg.font-medium.ty-text+.mb-4 "Attributes"]
     (attribute-table
      [{:name "flavor"
        :type "string"
        :default "\"neutral\""
        :description "Semantic color. Built-in: primary, secondary, success, danger, warning, neutral. Append '+' for a stronger shade or '-' for a softer one (e.g. \"primary+\", \"danger-\"). Any other string is also accepted — theme custom flavors via --ty-button-* CSS variables."}
       {:name "appearance"
        :type "string"
        :default "\"solid\""
        :description "Visual variant: \"solid\" (saturated brand fill, default), \"outlined\" (transparent bg with matching text + border), or \"ghost\" (text only with hover background)."}
       {:name "size"
        :type "string"
        :default "\"md\""
        :description "Size of the button (xs, sm, md, lg, xl)"}
       {:name "disabled"
        :type "boolean"
        :default "false"
        :description "Whether the button is disabled"}
       {:name "action"
        :type "boolean"
        :default "false"
        :description "Makes button circular/icon-only, typically for floating action buttons"}
       {:name "wide"
        :type "boolean"
        :default "false"
        :description "Makes button take more horizontal space"}
       {:name "type"
        :type "string"
        :default "\"button\""
        :description "HTML button type (button, submit, reset)"}
       {:name "name"
        :type "string"
        :default "-"
        :description "Form field name when used in forms"}
       {:name "value"
        :type "string"
        :default "-"
        :description "Form field value when used in forms"}])]

    ;; Events
    [:div.mb-8
     [:h3.text-lg.font-medium.ty-text+.mb-4 "Events"]
     (event-table
      [{:name "click"
        :payload "MouseEvent"
        :when-fired "When button is clicked"}
       {:name "focus"
        :payload "FocusEvent"
        :when-fired "When button receives focus"}
       {:name "blur"
        :payload "FocusEvent"
        :when-fired "When button loses focus"}])]

    ;; Slots
    [:div
     [:h3.text-lg.font-medium.ty-text+.mb-4 "Slots"]
     [:div.overflow-x-auto
      [:table.w-full
       [:thead
        [:tr.border-b.ty-border
         [:th.text-left.px-4.py-2.ty-text+ "Slot"]
         [:th.text-left.px-4.py-2.ty-text+ "Description"]]]
       [:tbody
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "start"]
         [:td.px-4.py-2.ty-text-.text-sm "Content placed before the button text (typically an icon)"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "(default)"]
         [:td.px-4.py-2.ty-text-.text-sm "Main button content"]]
        [:tr.border-b.ty-border-
         [:td.px-4.py-2.ty-text.font-mono.text-sm "end"]
         [:td.px-4.py-2.ty-text-.text-sm "Content placed after the button text (typically an icon)"]]]]]]]

;; Basic Usage
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Basic Usage"]
    (code-block "<ty-button>Click me</ty-button>")
    [:div.mt-4
     [:ty-button {:on {:click #(js/alert "It works!")}} "Try it out"]]]

   ;; Examples Section
   [:h2.text-2xl.font-semibold.ty-text.mb-6 "Examples"]

   [:div.space-y-8
    ;; Button Flavors
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Button Flavors"]
     [:div.flex.flex-wrap.gap-3.mb-4
      [:ty-button "Default"]
      [:ty-button {:flavor "primary"} "Primary"]
      [:ty-button {:flavor "secondary"} "Secondary"]
      [:ty-button {:flavor "danger"} "Danger"]
      [:ty-button {:flavor "success"} "Success"]
      [:ty-button {:flavor "warning"} "Warning"]
      [:ty-button {:flavor "neutral"} "Neutral"]]
     (code-block "<ty-button>Default</ty-button>
<ty-button flavor=\"primary\">Primary</ty-button>
<ty-button flavor=\"secondary\">Secondary</ty-button>
<ty-button flavor=\"danger\">Danger</ty-button>
<ty-button flavor=\"success\">Success</ty-button>
<ty-button flavor=\"warning\">Warning</ty-button>
<ty-button flavor=\"neutral\">Neutral</ty-button>")]

    ;; Complete Matrix: every appearance × flavor × tone
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Complete Matrix"]
     [:p.ty-text-.text-sm.mb-4
      "Every appearance × flavor × tone combination. Three appearance variants — "
      [:code.font-mono "solid"] ", " [:code.font-mono "outlined"] ", " [:code.font-mono "ghost"]
      " — each with three tones via the flavor suffix: "
      [:code.font-mono "-"] " (soft), base, "
      [:code.font-mono "+"] " (strong). Useful for inspecting contrast in light vs dark mode."]
     [:div.space-y-8
      ;; Solid
      [:div
       [:h4.text-sm.font-medium.ty-text+.mb-3 "Solid"]
       [:div.grid.gap-3.items-center
        {:style "grid-template-columns: 6rem repeat(3, max-content);"}
        [:div]
        [:div.text-xs.ty-text-.font-medium "Soft (–)"]
        [:div.text-xs.ty-text-.font-medium "Base"]
        [:div.text-xs.ty-text-.font-medium "Strong (+)"]
        (for [flavor ["primary" "secondary" "success" "danger" "warning" "neutral"]]
          (list
           [:div.text-sm.font-mono.ty-text {:key (str "solid-" flavor "-label")} flavor]
           [:ty-button {:key (str "solid-" flavor "-soft") :flavor (str flavor "-")} flavor]
           [:ty-button {:key (str "solid-" flavor "-base") :flavor flavor} flavor]
           [:ty-button {:key (str "solid-" flavor "-strong") :flavor (str flavor "+")} flavor]))]]

      ;; Outlined
      [:div
       [:h4.text-sm.font-medium.ty-text+.mb-3 "Outlined"]
       [:div.grid.gap-3.items-center
        {:style "grid-template-columns: 6rem repeat(3, max-content);"}
        [:div]
        [:div.text-xs.ty-text-.font-medium "Soft (–)"]
        [:div.text-xs.ty-text-.font-medium "Base"]
        [:div.text-xs.ty-text-.font-medium "Strong (+)"]
        (for [flavor ["primary" "secondary" "success" "danger" "warning" "neutral"]]
          (list
           [:div.text-sm.font-mono.ty-text {:key (str "outlined-" flavor "-label")} flavor]
           [:ty-button {:key (str "outlined-" flavor "-soft") :flavor (str flavor "-") :appearance "outlined"} flavor]
           [:ty-button {:key (str "outlined-" flavor "-base") :flavor flavor :appearance "outlined"} flavor]
           [:ty-button {:key (str "outlined-" flavor "-strong") :flavor (str flavor "+") :appearance "outlined"} flavor]))]]

      ;; Ghost
      [:div
       [:h4.text-sm.font-medium.ty-text+.mb-3 "Ghost"]
       [:div.grid.gap-3.items-center
        {:style "grid-template-columns: 6rem repeat(3, max-content);"}
        [:div]
        [:div.text-xs.ty-text-.font-medium "Soft (–)"]
        [:div.text-xs.ty-text-.font-medium "Base"]
        [:div.text-xs.ty-text-.font-medium "Strong (+)"]
        (for [flavor ["primary" "secondary" "success" "danger" "warning" "neutral"]]
          (list
           [:div.text-sm.font-mono.ty-text {:key (str "ghost-" flavor "-label")} flavor]
           [:ty-button {:key (str "ghost-" flavor "-soft") :flavor (str flavor "-") :appearance "ghost"} flavor]
           [:ty-button {:key (str "ghost-" flavor "-base") :flavor flavor :appearance "ghost"} flavor]
           [:ty-button {:key (str "ghost-" flavor "-strong") :flavor (str flavor "+") :appearance "ghost"} flavor]))]]]

     (code-block "<!-- Solid is the default appearance -->
<ty-button flavor=\"primary\">Primary</ty-button>
<ty-button flavor=\"primary+\">Stronger</ty-button>
<ty-button flavor=\"primary-\">Softer</ty-button>

<!-- Outlined and ghost via the appearance attribute -->
<ty-button appearance=\"outlined\" flavor=\"success+\">Success outlined</ty-button>
<ty-button appearance=\"ghost\" flavor=\"danger\">Danger ghost</ty-button>")]

    ;; Custom Colors via CSS Variables
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Custom Colors via CSS Variables"]
     [:p.ty-text-.text-sm.mb-4
      "Override colors per button by setting any of these CSS variables on the host. Useful for one-off brand colors, A/B tests, or theming a button outside the semantic palette."]
     [:ul.list-disc.list-inside.text-sm.ty-text-.mb-4.space-y-1
      [:li [:code.font-mono "--ty-button-bg"] " — background color (solid)"]
      [:li [:code.font-mono "--ty-button-bg-hover"] " — hover background"]
      [:li [:code.font-mono "--ty-button-color"] " — text color"]
      [:li [:code.font-mono "--ty-button-border"] " — border color (outlined)"]]
     [:div.flex.flex-wrap.gap-3.mb-4
      [:ty-button {:style "--ty-button-bg: #ff6600; --ty-button-color: white; --ty-button-bg-hover: #e65c00;"}
       "Brand orange"]
      [:ty-button {:flavor "primary" :appearance "outlined"
                   :style "--ty-button-color: #6366f1; --ty-button-border: #6366f1;"}
       "Indigo outlined"]
      [:ty-button {:style "--ty-button-bg: #ec4899; --ty-button-color: white; --ty-button-bg-hover: #db2777;"}
       "Pink solid"]
      [:ty-button {:style "--ty-button-bg: linear-gradient(135deg, #667eea, #764ba2);"}
       "Gradient"]]
     (code-block "<!-- One-off brand color -->
<ty-button style=\"--ty-button-bg: #ff6600;
                  --ty-button-color: white;
                  --ty-button-bg-hover: #e65c00;\">
  Brand orange
</ty-button>

<!-- Even gradients work -->
<ty-button style=\"--ty-button-bg: linear-gradient(135deg, #667eea, #764ba2);\">
  Gradient
</ty-button>")]

    ;; Custom Flavors
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Custom Flavors"]
     [:p.ty-text-.text-sm.mb-4
      "Pass any string as " [:code.font-mono "flavor"] " — even if it's not built-in. The button picks up the class so you can theme all buttons of a flavor in a single CSS rule. Combine with the CSS variables above for global brand flavors."]
     [:div.flex.flex-wrap.gap-3.mb-4
      [:style "ty-button[flavor=\"brand\"] {
   --ty-button-bg: #7c3aed;
   --ty-button-color: white;
   --ty-button-bg-hover: #6d28d9;
   --ty-button-border: #5b21b6;
 }
 ty-button[flavor=\"teal\"] {
   --ty-button-bg: #0d9488;
   --ty-button-color: white;
   --ty-button-bg-hover: #0f766e;
   --ty-button-border: #115e59;
 }
 ty-button[flavor=\"sunset\"] {
   --ty-button-bg: #f97316;
   --ty-button-color: white;
   --ty-button-bg-hover: #ea580c;
   --ty-button-border: #c2410c;
 }"]
      [:ty-button {:flavor "brand"} "Brand"]
      [:ty-button {:flavor "teal"} "Teal"]
      [:ty-button {:flavor "sunset"} "Sunset"]
      [:ty-button {:flavor "brand" :appearance "outlined"} "Brand outlined"]
      [:ty-button {:flavor "teal" :pill true} "Teal pill"]]
     (code-block "<style>
  ty-button[flavor=\"brand\"] {
    --ty-button-bg: #7c3aed;
    --ty-button-color: white;
    --ty-button-bg-hover: #6d28d9;
    --ty-button-border: #5b21b6;
  }
</style>

<ty-button flavor=\"brand\">Brand</ty-button>
<ty-button flavor=\"brand\" appearance=\"outlined\">Brand outlined</ty-button>")]

    ;; Button Sizes
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Button Sizes"]
     [:div.flex.flex-wrap.items-center.gap-3.mb-4
      [:ty-button {:size "xs"} "Extra Small"]
      [:ty-button {:size "sm"} "Small"]
      [:ty-button "Default"]
      [:ty-button {:size "lg"} "Large"]
      [:ty-button {:size "xl"} "Extra Large"]]
     (code-block "<ty-button size=\"xs\">Extra Small</ty-button>
<ty-button size=\"sm\">Small</ty-button>
<ty-button>Default</ty-button>
<ty-button size=\"lg\">Large</ty-button>
<ty-button size=\"xl\">Extra Large</ty-button>")]

    ;; Action Buttons (Icon-only)
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Action Buttons (Icon-only)"]
     [:p.ty-text-.text-sm.mb-4
      "Action buttons are circular icon-only buttons, perfect for floating action buttons or toolbar actions."]
     [:div.flex.flex-wrap.items-center.gap-3.mb-4
      [:ty-button {:action true :flavor "primary" :size "lg"}
       [:ty-icon {:name "plus" :size "md"}]]
      [:ty-button {:action true :flavor "secondary"}
       [:ty-icon {:name "edit" :size "sm"}]]
      [:ty-button {:action true :flavor "danger"}
       [:ty-icon {:name "trash" :size "sm"}]]
      [:ty-button {:action true :flavor "success"}
       [:ty-icon {:name "check" :size "sm"}]]
      [:ty-button {:action true :size "xs"}
       [:ty-icon {:name "x" :size "xs"}]]]
     (code-block "<!-- Large Primary FAB -->
<ty-button action=\"true\" flavor=\"primary\" size=\"lg\">
  <ty-icon name=\"plus\" size=\"md\"></ty-icon>
</ty-button>

<!-- Regular Action Buttons -->
<ty-button action=\"true\" flavor=\"secondary\">
  <ty-icon name=\"edit\" size=\"sm\"></ty-icon>
</ty-button>

<ty-button action=\"true\" flavor=\"danger\">
  <ty-icon name=\"trash\" size=\"sm\"></ty-icon>
</ty-button>

<!-- Small Close Button -->
<ty-button action=\"true\" size=\"xs\">
  <ty-icon name=\"x\" size=\"xs\"></ty-icon>
</ty-button>")]

    ;; Button with Icons
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Buttons with Icons"]
     [:div.flex.flex-wrap.gap-3.mb-4
      [:ty-button {:flavor "primary"}
       [:ty-icon {:slot "start" :name "save" :size "sm"}]
       "Save"]
      [:ty-button {:flavor "secondary"}
       [:ty-icon {:slot "start" :name "download" :size "sm"}]
       "Download"]
      [:ty-button {:flavor "danger"}
       [:ty-icon {:slot "start" :name "trash" :size "sm"}]
       "Delete"]
      [:ty-button
       "Next"
       [:ty-icon {:slot "end" :name "arrow-right" :size "sm"}]]]
     (code-block "<ty-button flavor=\"primary\">
  <ty-icon slot=\"start\" name=\"save\" size=\"sm\"></ty-icon>
  Save
</ty-button>

<ty-button>
  Next
  <ty-icon slot=\"end\" name=\"arrow-right\" size=\"sm\"></ty-icon>
</ty-button>")]

    ;; Button States
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Button States"]
     [:div.flex.flex-wrap.gap-3.mb-4
      [:ty-button {:disabled true} "Disabled"]
      [:ty-button {:flavor "primary"}
       [:ty-icon {:slot "start" :name "loader-2" :size "sm" :spin true}]
       "Loading..."]
      [:ty-button {:flavor "secondary"}
       [:ty-icon {:slot "start" :name "refresh-cw" :size "sm" :spin true}]
       "Processing"]
      [:ty-button {:flavor "success" :disabled true} "Success Disabled"]]
     (code-block "<ty-button disabled>Disabled</ty-button>

<!-- Loading state with spinning icon -->
<ty-button flavor=\"primary\">
  <ty-icon slot=\"start\" name=\"loader-2\" size=\"sm\" spin=\"true\"></ty-icon>
  Loading...
</ty-button>

<ty-button flavor=\"success\" disabled>Success Disabled</ty-button>")]

    ;; Wide Buttons
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Wide Buttons"]
     [:p.ty-text-.text-sm.mb-4
      "Wide buttons expand to take full available width. Perfect for mobile layouts, forms, and call-to-action sections."]

     [:div.space-y-4
      ;; Single wide button
      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Single Wide Button"]
       [:div.mb-3
        [:ty-button {:wide true :flavor "primary"}
         [:ty-icon {:slot "start" :name "log-in" :size "sm"}]
         "Sign In"]]
       (code-block "<ty-button wide=\"true\" flavor=\"primary\">
  <ty-icon slot=\"start\" name=\"log-in\" size=\"sm\"></ty-icon>
  Sign In
</ty-button>")]

      ;; Wide buttons in a grid
      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Wide Buttons in Grid"]
       [:div.grid.grid-cols-2.gap-3.mb-3
        [:ty-button {:wide true :flavor "primary"}
         [:ty-icon {:slot "start" :name "check" :size "sm"}]
         "Confirm"]
        [:ty-button {:wide true :flavor "secondary"}
         [:ty-icon {:slot "start" :name "x" :size "sm"}]
         "Cancel"]]
       (code-block "<div class=\"grid grid-cols-2 gap-3\">
  <ty-button wide=\"true\" flavor=\"primary\">
    <ty-icon slot=\"start\" name=\"check\" size=\"sm\"></ty-icon>
    Confirm
  </ty-button>
  <ty-button wide=\"true\" flavor=\"secondary\">
    <ty-icon slot=\"start\" name=\"x\" size=\"sm\"></ty-icon>
    Cancel
  </ty-button>
</div>")]

      ;; Three wide buttons in a row
      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Action Panel (Three Buttons)"]
       [:div.grid.grid-cols-3.gap-2.mb-3
        [:ty-button {:wide true :flavor "success"}
         [:ty-icon {:slot "start" :name "download" :size "sm"}]
         "Download"]
        [:ty-button {:wide true :flavor "secondary"}
         [:ty-icon {:slot "start" :name "share-2" :size "sm"}]
         "Share"]
        [:ty-button {:wide true :flavor "danger"}
         [:ty-icon {:slot "start" :name "trash" :size "sm"}]
         "Delete"]]
       (code-block "<div class=\"grid grid-cols-3 gap-2\">
  <ty-button wide=\"true\" flavor=\"success\">
    <ty-icon slot=\"start\" name=\"download\" size=\"sm\"></ty-icon>
    Download
  </ty-button>
  <ty-button wide=\"true\" flavor=\"secondary\">
    <ty-icon slot=\"start\" name=\"share-2\" size=\"sm\"></ty-icon>
    Share
  </ty-button>
  <ty-button wide=\"true\" flavor=\"danger\">
    <ty-icon slot=\"start\" name=\"trash\" size=\"sm\"></ty-icon>
    Delete
  </ty-button>
</div>")]

      ;; Mobile form example
      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Mobile Form Layout"]
       [:div.max-w-sm.space-y-2.mb-3
        [:ty-button {:wide true :flavor "primary" :size "lg"}
         [:ty-icon {:slot "start" :name "log-in" :size "sm"}]
         "Sign In with Email"]
        [:ty-button {:wide true :flavor "secondary" :size "lg"}
         [:ty-icon {:slot "start" :name "github" :size "sm"}]
         "Sign In with GitHub"]
        [:ty-button {:wide true :flavor "secondary" :size "lg"}
         [:ty-icon {:slot "start" :name "mail" :size "sm"}]
         "Sign In with Google"]]
       (code-block "<div class=\"space-y-2\">
  <ty-button wide=\"true\" flavor=\"primary\" size=\"lg\">
    <ty-icon slot=\"start\" name=\"log-in\" size=\"sm\"></ty-icon>
    Sign In with Email
  </ty-button>
  <ty-button wide=\"true\" flavor=\"secondary\" size=\"lg\">
    <ty-icon slot=\"start\" name=\"github\" size=\"sm\"></ty-icon>
    Sign In with GitHub
  </ty-button>
  <ty-button wide=\"true\" flavor=\"secondary\" size=\"lg\">
    <ty-icon slot=\"start\" name=\"mail\" size=\"sm\"></ty-icon>
    Sign In with Google
  </ty-button>
</div>")]]]

    ;; Form Integration
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Form Integration"]
     [:p.ty-text-.text-sm.mb-4
      "ty-button fully supports HTML form integration with type, name, and value attributes. Default type is 'submit' like native HTML buttons."]

     [:div.space-y-4
      ;; Basic Form
      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Basic Form with Submit"]
       [:form.ty-elevated.rounded-lg.p-4.mb-3
        {:on {:submit (fn [e]
                        (.preventDefault e)
                        (let [form-data (js/FormData. (.-target e))
                              data (js/Object.fromEntries form-data)]
                          (js/alert (str "Form submitted!\n" (js/JSON.stringify data nil 2)))))}}
        [:div.space-y-3
         [:div
          [:label.block.ty-text+.text-sm.mb-1 {:for "username"} "Username"]
          [:input#username.ty-input.ty-border.border.rounded.px-3.py-2.w-full
           {:name "username" :required true :placeholder "Enter username"}]]
         [:div
          [:label.block.ty-text+.text-sm.mb-1 {:for "email"} "Email"]
          [:input#email.ty-input.ty-border.border.rounded.px-3.py-2.w-full
           {:name "email" :type "email" :required true :placeholder "Enter email"}]]
         [:div.flex.gap-2
          [:ty-button {:type "submit" :flavor "primary"}
           [:ty-icon {:slot "start" :name "check" :size "sm"}]
           "Submit"]
          [:ty-button {:type "reset" :flavor "secondary"}
           [:ty-icon {:slot "start" :name "x" :size "sm"}]
           "Reset"]
          [:ty-button {:type "button" :flavor "secondary"}
           "Cancel"]]]]
       (code-block "<form>
  <input name=\"username\" required>
  <input name=\"email\" type=\"email\" required>
  
  <!-- type=\"submit\" (default) - submits the form -->
  <ty-button type=\"submit\" flavor=\"primary\">Submit</ty-button>
  
  <!-- type=\"reset\" - clears the form -->
  <ty-button type=\"reset\" flavor=\"secondary\">Reset</ty-button>
  
  <!-- type=\"button\" - does nothing, for custom JS -->
  <ty-button type=\"button\" flavor=\"secondary\">Cancel</ty-button>
</form>")]

      ;; Form with Named Submit Buttons
      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Multiple Submit Buttons with Name/Value"]
       [:form.ty-elevated.rounded-lg.p-4.mb-3
        {:on {:submit (fn [e]
                        (.preventDefault e)
                        (let [form-data (js/FormData. (.-target e))
                              data (js/Object.fromEntries form-data)]
                          (js/alert (str "Form submitted with action: " (or (.-action data) "none") "\n\nData:\n" (js/JSON.stringify data nil 2)))))}}
        [:div.space-y-3
         [:div
          [:label.block.ty-text+.text-sm.mb-1 {:for "comment"} "Your Comment"]
          [:textarea#comment.ty-input.ty-border.border.rounded.px-3.py-2.w-full
           {:name "comment" :rows 3 :required true :placeholder "Enter your comment"}]]
         [:div.ty-elevated.rounded.p-3.bg-opacity-50
          [:p.ty-text-.text-sm.mb-3 "Multiple submit buttons can have different name/value pairs:"]
          [:div.flex.flex-wrap.gap-2
           [:ty-button {:type "submit" :name "action" :value "save_draft" :flavor "secondary"}
            [:ty-icon {:slot "start" :name "save" :size "sm"}]
            "Save Draft"]
           [:ty-button {:type "submit" :name "action" :value "publish" :flavor "primary"}
            [:ty-icon {:slot "start" :name "send" :size "sm"}]
            "Publish"]
           [:ty-button {:type "submit" :name "action" :value "schedule" :flavor "secondary"}
            [:ty-icon {:slot "start" :name "clock" :size "sm"}]
            "Schedule"]]]]]
       (code-block "<form>
  <textarea name=\"comment\" required></textarea>
  
  <!-- Each button can have different name/value -->
  <!-- Only the clicked button's value is submitted -->
  <ty-button type=\"submit\" name=\"action\" value=\"save_draft\">
    Save Draft
  </ty-button>
  
  <ty-button type=\"submit\" name=\"action\" value=\"publish\">
    Publish
  </ty-button>
  
  <ty-button type=\"submit\" name=\"action\" value=\"schedule\">
    Schedule
  </ty-button>
</form>")]

      ;; Form Type Comparison
      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Button Types Comparison"]
       [:div.ty-elevated.rounded.p-4.mb-3
        [:table.w-full.text-sm
         [:thead
          [:tr.border-b.ty-border
           [:th.text-left.px-2.py-2.ty-text+ "Type"]
           [:th.text-left.px-2.py-2.ty-text+ "Behavior"]
           [:th.text-left.px-2.py-2.ty-text+ "Use Case"]]]
         [:tbody
          [:tr.border-b.ty-border-
           [:td.px-2.py-2.ty-text.font-mono "submit"]
           [:td.px-2.py-2.ty-text- "Submits the form"]
           [:td.px-2.py-2.ty-text- "Primary action, save data"]]
          [:tr.border-b.ty-border-
           [:td.px-2.py-2.ty-text.font-mono "reset"]
           [:td.px-2.py-2.ty-text- "Clears all fields"]
           [:td.px-2.py-2.ty-text- "Reset form to initial state"]]
          [:tr
           [:td.px-2.py-2.ty-text.font-mono "button"]
           [:td.px-2.py-2.ty-text- "Does nothing"]
           [:td.px-2.py-2.ty-text- "Custom JavaScript actions"]]]]]
       (code-block "<!-- Default behavior (submit) -->
<ty-button>Submit Form</ty-button>

<!-- Explicitly set type -->
<ty-button type=\"submit\">Submit</ty-button>
<ty-button type=\"reset\">Clear</ty-button>
<ty-button type=\"button\">Custom Action</ty-button>")]]]

    ;; Loading State Patterns
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Loading State Patterns"]
     [:p.ty-text-.text-sm.mb-4
      "Create loading states using spinning icons. Perfect for async operations."]

     [:div.space-y-4
      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Standard Loading"]
       [:div.flex.gap-3.mb-3
        [:ty-button {:flavor "primary"}
         [:ty-icon {:slot "start" :name "loader-2" :size "sm" :spin true}]
         "Saving..."]
        [:ty-button {:flavor "secondary" :disabled true}
         [:ty-icon {:slot "start" :name "refresh-cw" :size "sm" :spin true}]
         "Syncing..."]]
       (code-block "<ty-button flavor=\"primary\">
  <ty-icon slot=\"start\" name=\"loader-2\" size=\"sm\" spin=\"true\"></ty-icon>
  Saving...
</ty-button>")]

      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Action Button Loading"]
       [:div.flex.gap-3.mb-3
        [:ty-button {:action true :flavor "primary"}
         [:ty-icon {:name "loader-2" :size "sm" :spin true}]]
        [:ty-button {:action true :flavor "secondary" :disabled true}
         [:ty-icon {:name "refresh-cw" :size "sm" :spin true}]]]
       (code-block "<ty-button action=\"true\" flavor=\"primary\">
  <ty-icon name=\"loader-2\" size=\"sm\" spin=\"true\"></ty-icon>
</ty-button>")]

      [:div
       [:h4.text-sm.font-medium.ty-text.mb-2 "Available Spinning Icons"]
       [:div.flex.flex-wrap.gap-3.mb-3
        [:ty-button {:flavor "neutral"}
         [:ty-icon {:slot "start" :name "loader" :size "sm" :spin true}]
         "loader"]
        [:ty-button {:flavor "neutral"}
         [:ty-icon {:slot "start" :name "loader-2" :size "sm" :spin true}]
         "loader-2"]
        [:ty-button {:flavor "neutral"}
         [:ty-icon {:slot "start" :name "refresh-cw" :size "sm" :spin true}]
         "refresh-cw"]]]]]]

   ;; Use Cases Section
   [:h2.text-2xl.font-semibold.ty-text.mb-6.mt-12 "Common Use Cases"]

   [:div.space-y-8
    ;; Floating Action Button
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Floating Action Button (FAB)"]
     [:p.ty-text-.text-sm.mb-4
      "Use action buttons with primary flavor and large size for main floating actions."]
     [:div.relative.h-32.ty-elevated.rounded-lg.flex.items-end.justify-end.p-4.mb-4
      [:ty-button {:action true :flavor "primary" :size "lg"
                   :style "position: absolute; bottom: 1rem; right: 1rem;"}
       [:ty-icon {:name "plus" :size "md"}]]]
     (code-block "<!-- Positioned FAB -->
<div class=\"relative\">
  <!-- Your content -->
  <ty-button action=\"true\" flavor=\"primary\" size=\"lg\" 
             class=\"absolute bottom-4 right-4\">
    <ty-icon name=\"plus\" size=\"md\"></ty-icon>
  </ty-button>
</div>")]

    ;; Toolbar Actions
    [:div.ty-content.rounded-lg.p-6
     [:h3.text-lg.font-medium.ty-text.mb-4 "Toolbar Actions"]
     [:p.ty-text-.text-sm.mb-4
      "Combine text buttons with action buttons in toolbars."]
     [:div.ty-elevated.rounded.p-3.flex.items-center.justify-between.mb-4
      [:div.flex.gap-2
       [:ty-button {:size "sm"} "Edit"]
       [:ty-button {:size "sm"} "Share"]]
      [:div.flex.gap-1
       [:ty-button {:action true :size "sm"}
        [:ty-icon {:name "search" :size "xs"}]]
       [:ty-button {:action true :size "sm"}
        [:ty-icon {:name "filter" :size "xs"}]]
       [:ty-button {:action true :size "sm"}
        [:ty-icon {:name "more-vertical" :size "xs"}]]]]
     (code-block "<div class=\"flex items-center justify-between\">
  <div class=\"flex gap-2\">
    <ty-button size=\"sm\">Edit</ty-button>
    <ty-button size=\"sm\">Share</ty-button>
  </div>
  <div class=\"flex gap-1\">
    <ty-button action=\"true\" size=\"sm\">
      <ty-icon name=\"search\" size=\"xs\"></ty-icon>
    </ty-button>
    <ty-button action=\"true\" size=\"sm\">
      <ty-icon name=\"more-vertical\" size=\"xs\"></ty-icon>
    </ty-button>
  </div>
</div>")]]

   ;; Best Practices
   [:div.ty-elevated.rounded-lg.p-6.mt-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 "Best Practices"]
    [:div.space-y-3
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-1 {:name "check" :size "sm"}]
      [:p.ty-text- "Use semantic flavors (primary, danger) to convey meaning"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-1 {:name "check" :size "sm"}]
      [:p.ty-text- "Show loading states with spinning icons for async operations"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-1 {:name "check" :size "sm"}]
      [:p.ty-text- "Use action buttons for icon-only actions to save space"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-1 {:name "check" :size "sm"}]
      [:p.ty-text- "Disable buttons during loading to prevent multiple submissions"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-success.mt-1 {:name "check" :size "sm"}]
      [:p.ty-text- "Use slots for icons to maintain proper spacing"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-danger.mt-1 {:name "x" :size "sm"}]
      [:p.ty-text- "Don't use multiple buttons with the same primary action"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-danger.mt-1 {:name "x" :size "sm"}]
      [:p.ty-text- "Avoid using more than one primary button per section"]]
     [:div.flex.items-start.gap-2
      [:ty-icon.ty-text-danger.mt-1 {:name "x" :size "sm"}]
      [:p.ty-text- "Don't use action buttons for text-heavy actions"]]]]))