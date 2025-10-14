(ns ty.site.docs.checkbox
  "Documentation for ty-checkbox component"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table doc-section example-section]]))

(defn api-reference []
  [:div.ty-elevated.rounded-lg.p-6.mb-8
   [:h2#attributes.text-2xl.font-bold.ty-text++.mb-4 "API Reference"]

   [:h3.text-lg.font-semibold.ty-text++.mb-2 "ty-checkbox Attributes"]

   (attribute-table
    [{:name "checked"
      :type "boolean"
      :default "false"
      :description "Initial checked state"}
     {:name "value"
      :type "string"
      :default "'on'"
      :description "Form value when checked (default: 'on')"}
     {:name "name"
      :type "string"
      :default "null"
      :description "Name for form submission"}
     {:name "disabled"
      :type "boolean"
      :default "false"
      :description "Whether the checkbox is disabled"}
     {:name "required"
      :type "boolean"
      :default "false"
      :description "Whether the checkbox is required (shows asterisk)"}
     {:name "error"
      :type "string"
      :default "null"
      :description "Error message to display"}
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
    [:h3.text-lg.font-semibold.ty-text++.mb-2 "ty-checkbox Events"]
    (event-table
     [{:name "input"
       :when-fired "Fired when checkbox state changes"
       :payload "{value, checked, formValue, originalEvent}"}
      {:name "change"
       :when-fired "Fired when checkbox state changes"
       :payload "{value, checked, formValue, originalEvent}"}])]

   [:div.ty-elevated.rounded.p-4.mt-4.ty-bg-info-
    [:h4.text-sm.font-semibold.ty-text-info++.mb-2 "💡 How Checkbox Values Work"]
    [:ul.space-y-1.ty-text-info.text-sm
     [:li "• Content goes in the default slot (appears after the checkbox icon)"]
     [:li "• When " [:code.ty-bg-neutral-.px-1.rounded "checked"] ": submits the " [:code.ty-bg-neutral-.px-1.rounded "value"] " attribute"]
     [:li "• When " [:code.ty-bg-neutral-.px-1.rounded "unchecked"] ": submits nothing (null in FormData)"]
     [:li "• Default value is 'on' (standard HTML checkbox behavior)"]
     [:li "• Fully form-associated and works with native form submission"]
     [:li "• Supports keyboard navigation (Space to toggle)"]]]])

(defn basic-usage-section []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Basic Usage"]

   [:div.mb-6
    [:h3.text-lg.font-semibold.ty-text+.mb-2 "Simple Checkbox"]
    [:div.mb-4
     [:ty-checkbox {:name "terms"}
      "I agree to the terms and conditions"]]
    (code-block "<!-- Simple checkbox with label -->
<ty-checkbox name=\"terms\">
  I agree to the terms and conditions
</ty-checkbox>")]

   [:div.mb-6
    [:h3.text-lg.font-semibold.ty-text+.mb-2 "Checked by Default"]
    [:div.mb-4
     [:ty-checkbox {:checked "true"
                    :name "subscribe"}
      "Subscribe to newsletter"]]
    (code-block "<!-- Checkbox checked by default -->
<ty-checkbox checked=\"true\" name=\"subscribe\">
  Subscribe to newsletter
</ty-checkbox>")]

   [:div.mb-6
    [:h3.text-lg.font-semibold.ty-text+.mb-2 "Required Checkbox"]
    [:div.mb-4
     [:ty-checkbox {:required "true"
                    :name "required"}
      "This checkbox is required"]]
    (code-block "<!-- Required checkbox (shows asterisk) -->
<ty-checkbox required=\"true\" name=\"required\">
  This checkbox is required
</ty-checkbox>")]

   [:div
    [:h3.text-lg.font-semibold.ty-text+.mb-2 "Disabled State"]
    [:div.mb-4.space-y-2
     [:ty-checkbox {:disabled "true"}
      "Disabled unchecked"]
     [:ty-checkbox {:disabled "true"
                    :checked "true"}
      "Disabled checked"]]
    (code-block "<!-- Disabled checkboxes -->
<ty-checkbox disabled=\"true\">Disabled unchecked</ty-checkbox>
<ty-checkbox disabled=\"true\" checked=\"true\">Disabled checked</ty-checkbox>")]])

(defn sizes-section []
  [:div.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Sizes"]
   [:div.ty-content.rounded-lg.p-6
    [:p.ty-text-.mb-4 "Five size variants for different contexts."]
    [:div.space-y-3
     [:div
      [:ty-checkbox {:size "xs"
                     :checked "true"}
       "Extra small checkbox"]]

     [:div
      [:ty-checkbox {:size "sm"
                     :checked "true"}
       "Small checkbox"]]

     [:div
      [:ty-checkbox {:size "md"
                     :checked "true"}
       "Medium checkbox (default)"]]

     [:div
      [:ty-checkbox {:size "lg"
                     :checked "true"}
       "Large checkbox"]]

     [:div
      [:ty-checkbox {:size "xl"
                     :checked "true"}
       "Extra large checkbox"]]]

    (code-block "<!-- Size variants -->
<ty-checkbox size=\"xs\" checked=\"true\">Extra small checkbox</ty-checkbox>
<ty-checkbox size=\"sm\" checked=\"true\">Small checkbox</ty-checkbox>
<ty-checkbox size=\"md\" checked=\"true\">Medium checkbox (default)</ty-checkbox>
<ty-checkbox size=\"lg\" checked=\"true\">Large checkbox</ty-checkbox>
<ty-checkbox size=\"xl\" checked=\"true\">Extra large checkbox</ty-checkbox>")]])

(defn flavors-section []
  [:div.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Semantic Flavors"]
   [:div.ty-content.rounded-lg.p-6
    [:p.ty-text-.mb-4 "Semantic colors for different states and purposes."]
    [:div.space-y-3
     [:div
      [:ty-checkbox {:flavor "neutral"
                     :checked "true"}
       "Neutral (default)"]]

     [:div
      [:ty-checkbox {:flavor "primary"
                     :checked "true"}
       "Primary flavor"]]

     [:div
      [:ty-checkbox {:flavor "secondary"
                     :checked "true"}
       "Secondary flavor"]]

     [:div
      [:ty-checkbox {:flavor "success"
                     :checked "true"}
       "Success - task completed"]]

     [:div
      [:ty-checkbox {:flavor "warning"
                     :checked "true"}
       "Warning - needs attention"]]

     [:div
      [:ty-checkbox {:flavor "danger"
                     :checked "true"}
       "Danger - destructive action"]]]

    (code-block "<!-- Semantic flavors -->
<ty-checkbox flavor=\"neutral\" checked=\"true\">Neutral (default)</ty-checkbox>
<ty-checkbox flavor=\"primary\" checked=\"true\">Primary flavor</ty-checkbox>
<ty-checkbox flavor=\"secondary\" checked=\"true\">Secondary flavor</ty-checkbox>
<ty-checkbox flavor=\"success\" checked=\"true\">Success - task completed</ty-checkbox>
<ty-checkbox flavor=\"warning\" checked=\"true\">Warning - needs attention</ty-checkbox>
<ty-checkbox flavor=\"danger\" checked=\"true\">Danger - destructive action</ty-checkbox>")]])

(defn form-integration-section []
  [:div.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Form Integration"]
   [:div.ty-content.rounded-lg.p-6
    [:h3.text-xl.font-semibold.ty-text++.mb-4 "Native Form Support"]
    [:p.ty-text-.mb-4 "ty-checkbox is a form-associated custom element. When checked, it submits its value attribute. When unchecked, it doesn't submit anything."]

    [:form.space-y-4 {:on {:submit (fn [^js event]
                                     (.preventDefault event)
                                     (let [form-data (js/FormData. (.-target event))
                                           data (js/Object.fromEntries form-data)]
                                       (.log js/console "Form submitted:" data))
                                     false)}}
     [:ty-checkbox {:name "terms"
                    :value "accepted"}
      "I agree to terms and conditions"]

     [:ty-checkbox {:name "newsletter"
                    :value "yes"
                    :checked "true"}
      "Subscribe to newsletter"]

     [:ty-checkbox {:name "notifications"
                    :value "enabled"}
      "Enable email notifications"]

     [:div.flex.gap-2
      [:ty-button {:type "submit"
                   :flavor "primary"} "Submit"]
      [:ty-button {:type "reset"
                   :flavor "secondary"} "Reset"]]]

    (code-block "<!-- Form with checkboxes -->
<form>
  <ty-checkbox name=\"terms\" value=\"accepted\">
    I agree to terms and conditions
  </ty-checkbox>
  
  <ty-checkbox name=\"newsletter\" value=\"yes\" checked=\"true\">
    Subscribe to newsletter
  </ty-checkbox>
  
  <ty-checkbox name=\"notifications\" value=\"enabled\">
    Enable email notifications
  </ty-checkbox>
  
  <ty-button type=\"submit\" flavor=\"primary\">Submit</ty-button>
  <ty-button type=\"reset\" flavor=\"secondary\">Reset</ty-button>
</form>

<script>
document.querySelector('form').addEventListener('submit', (e) => {
  e.preventDefault();
  const formData = new FormData(e.target);
  console.log(Object.fromEntries(formData));
  // Checked boxes appear in form data
  // Unchecked boxes are omitted
});
</script>")]])

(defn task-list-example []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h3.text-xl.font-semibold.ty-text++.mb-4 "Interactive Task List"]
   [:p.ty-text-.mb-4 "Build a simple task list with checkboxes and event listeners."]

   [:div.space-y-2.mb-4
    [:ty-checkbox {:id "task-1"
                   :flavor "success"}
     "Complete documentation"]
    [:ty-checkbox {:id "task-2"
                   :flavor "success"}
     "Write unit tests"]
    [:ty-checkbox {:id "task-3"
                   :flavor "success"}
     "Deploy to production"]
    [:div.ty-text-.text-sm.mt-3
     "Completed: " [:span#task-count.font-bold "0"] " / 3"]]

   [:script "
const checkboxes = document.querySelectorAll('#task-1, #task-2, #task-3');
const countEl = document.getElementById('task-count');

function updateCount() {
  const checked = Array.from(checkboxes).filter(cb => cb.checked).length;
  countEl.textContent = checked;
}

checkboxes.forEach(cb => {
  cb.addEventListener('change', updateCount);
});"]

   (code-block "<!-- Task list example -->
<ty-checkbox id=\"task-1\" flavor=\"success\">Complete documentation</ty-checkbox>
<ty-checkbox id=\"task-2\" flavor=\"success\">Write unit tests</ty-checkbox>
<ty-checkbox id=\"task-3\" flavor=\"success\">Deploy to production</ty-checkbox>

<div>Completed: <span id=\"task-count\">0</span> / 3</div>

<script>
const checkboxes = document.querySelectorAll('#task-1, #task-2, #task-3');
const countEl = document.getElementById('task-count');

function updateCount() {
  const checked = Array.from(checkboxes).filter(cb => cb.checked).length;
  countEl.textContent = checked;
}

checkboxes.forEach(cb => {
  cb.addEventListener('change', updateCount);
});
</script>" "javascript")])

(defn settings-panel-example []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h3.text-xl.font-semibold.ty-text++.mb-4 "Settings Panel"]
   [:p.ty-text-.mb-4 "Use different flavors and sizes to create organized settings panels."]

   [:div.ty-elevated.rounded-lg.p-4.space-y-4
    [:div
     [:h4.text-sm.font-semibold.ty-text+.mb-2 "Privacy Settings"]
     [:div.space-y-2
      [:ty-checkbox {:size "sm"
                     :flavor "primary"
                     :checked "true"}
       "Make profile public"]
      [:ty-checkbox {:size "sm"
                     :flavor "primary"}
       "Show email address"]
      [:ty-checkbox {:size "sm"
                     :flavor "primary"
                     :checked "true"}
       "Allow search engines to index profile"]]]

    [:div
     [:h4.text-sm.font-semibold.ty-text+.mb-2 "Notification Preferences"]
     [:div.space-y-2
      [:ty-checkbox {:size "sm"
                     :flavor "secondary"
                     :checked "true"}
       "Email notifications"]
      [:ty-checkbox {:size "sm"
                     :flavor "secondary"
                     :checked "true"}
       "Push notifications"]
      [:ty-checkbox {:size "sm"
                     :flavor "secondary"}
       "SMS notifications"]]]

    [:div
     [:h4.text-sm.font-semibold.ty-text+.mb-2 "Danger Zone"]
     [:div.space-y-2
      [:ty-checkbox {:size "sm"
                     :flavor "danger"}
       "Delete my account"]
      [:ty-checkbox {:size "sm"
                     :flavor "danger"}
       "Remove all data"]]]]

   (code-block "<!-- Settings panel with grouped checkboxes -->
<div>
  <h4>Privacy Settings</h4>
  <ty-checkbox size=\"sm\" flavor=\"primary\" checked=\"true\">
    Make profile public
  </ty-checkbox>
  <ty-checkbox size=\"sm\" flavor=\"primary\">
    Show email address
  </ty-checkbox>
</div>

<div>
  <h4>Notification Preferences</h4>
  <ty-checkbox size=\"sm\" flavor=\"secondary\" checked=\"true\">
    Email notifications
  </ty-checkbox>
  <ty-checkbox size=\"sm\" flavor=\"secondary\" checked=\"true\">
    Push notifications
  </ty-checkbox>
</div>

<div>
  <h4>Danger Zone</h4>
  <ty-checkbox size=\"sm\" flavor=\"danger\">
    Delete my account
  </ty-checkbox>
</div>")])

(defn advanced-examples-section []
  [:div.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Advanced Examples"]
   (task-list-example)
   (settings-panel-example)])

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
      [:li "• Always provide clear label text in the slot"]
      [:li "• Use semantic flavors (success for completed tasks)"]
      [:li "• Set the name attribute for form submission"]
      [:li "• Use custom values to identify specific checkboxes"]
      [:li "• Group related checkboxes logically"]
      [:li "• Make labels clickable (they are by default)"]
      [:li "• Use required attribute when checkbox must be checked"]]]

    [:div
     [:h3.text-lg.font-semibold.ty-text-danger++.mb-3.flex.items-center.gap-2
      [:ty-icon {:name "x-circle"
                 :size "20"}]
      "Don'ts"]
     [:ul.space-y-2.ty-text-
      [:li "• Don't use checkboxes for mutually exclusive options (use radio buttons)"]
      [:li "• Don't nest interactive elements in checkbox labels"]
      [:li "• Don't rely only on color to convey state"]
      [:li "• Don't use tiny checkboxes for important actions"]
      [:li "• Don't forget to handle form submission"]
      [:li "• Don't use checkboxes for actions (use buttons instead)"]]]]])

(defn tips-section []
  [:div.ty-content.rounded-lg.p-6.mb-8
   [:h2.text-2xl.font-bold.ty-text++.mb-4 "Tips & Tricks"]

   [:div.space-y-4
    [:div
     [:h3.font-semibold.ty-text+ "Form Values"]
     [:p.ty-text- "When checked, ty-checkbox submits the " [:code.ty-bg-neutral-.px-1.rounded "value"] " attribute. When unchecked, nothing is submitted. Default value is 'on' (standard HTML behavior)."]]

    [:div
     [:h3.font-semibold.ty-text+ "Keyboard Navigation"]
     [:p.ty-text- "Checkboxes are fully keyboard accessible. Use Tab to focus and Space to toggle."]]

    [:div
     [:h3.font-semibold.ty-text+ "Event Handling"]
     [:p.ty-text- "Both 'input' and 'change' events fire when checkbox state changes. Event detail includes "
      [:code.ty-bg-neutral-.px-1.rounded "checked"] " (boolean) and " [:code.ty-bg-neutral-.px-1.rounded "value"] " (string)."]]

    [:div
     [:h3.font-semibold.ty-text+ "Programmatic Control"]
     [:p.ty-text- "Set the " [:code.ty-bg-neutral-.px-1.rounded "checked"] " attribute to control state: "
      [:code.ty-bg-neutral-.px-1.rounded "checkbox.checked = true"] "."]]

    [:div
     [:h3.font-semibold.ty-text+ "Styling Tips"]
     [:p.ty-text- "Use different flavors to categorize checkboxes visually. Use size variants for hierarchy (sm for sub-options, md for main options)."]]]])

(defn related-components-section []
  [:div.p-4.ty-border.border.rounded-lg
   [:h3.font-semibold.ty-text+.mb-2 "Related Components"]
   [:div.flex.gap-4.text-sm
    [:a.ty-text-primary.hover:underline {:href "/docs/input"} "ty-input →"]
    [:a.ty-text-primary.hover:underline {:href "/docs/button"} "ty-button →"]
    [:a.ty-text-primary.hover:underline {:href "/docs/multiselect"} "ty-multiselect →"]]])

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text++.mb-2 "ty-checkbox"]
    [:p.text-lg.ty-text-
     "Form-associated checkbox component with semantic styling and full keyboard support. "
     "Perfect for task lists, settings panels, and multi-select options."]]

   (api-reference)
   (basic-usage-section)
   (sizes-section)
   (flavors-section)
   (form-integration-section)
   (advanced-examples-section)
   (best-practices-section)
   (tips-section)
   (related-components-section)])
