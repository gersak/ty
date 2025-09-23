(ns ty.site.docs.date-picker
  "Documentation for ty-date-picker component"
  (:require [ty.site.docs.common :refer [code-block attribute-table event-table doc-section example-section]]))

(defn view []
  [:div.max-w-4xl.mx-auto.p-6
   ;; Title and Description
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2 "ty-date-picker"]
    [:p.text-lg.ty-text- "A comprehensive date picker component with optional time selection, internationalization support, and a beautiful calendar dropdown."]]

   ;; API Reference Card
   [:div.ty-elevated.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text++.mb-6 "API Reference"]

    ;; Attributes Table
    [:div.mb-8
     [:h3.text-lg.font-medium.ty-text+.mb-3 "Attributes"]
     (attribute-table
      [{:name "value"
        :type "string"
        :default "null"
        :description "Initial date value (ISO format: YYYY-MM-DD or YYYY-MM-DDTHH:mm)"}
       {:name "size"
        :type "string"
        :default "\"md\""
        :description "Size variant: xs, sm, md, lg, xl"}
       {:name "flavor"
        :type "string"
        :default "null"
        :description "Color flavor: primary, secondary, success, danger, warning"}
       {:name "label"
        :type "string"
        :default "null"
        :description "Label text displayed above the input"}
       {:name "placeholder"
        :type "string"
        :default "\"Select date...\""
        :description "Placeholder text when no date is selected"}
       {:name "required"
        :type "boolean"
        :default "false"
        :description "Whether the field is required"}
       {:name "disabled"
        :type "boolean"
        :default "false"
        :description "Disable the date picker"}
       {:name "clearable"
        :type "boolean"
        :default "false"
        :description "Show clear button when value is present"}
       {:name "with-time"
        :type "boolean"
        :default "false"
        :description "Enable time selection alongside date"}
       {:name "format"
        :type "string"
        :default "\"long\""
        :description "Date format style: short, medium, long, full"}
       {:name "locale"
        :type "string"
        :default "\"en-US\""
        :description "Locale for formatting (e.g., 'de-DE', 'fr-FR')"}
       {:name "min-date"
        :type "string"
        :default "null"
        :description "Minimum selectable date (ISO format)"}
       {:name "max-date"
        :type "string"
        :default "null"
        :description "Maximum selectable date (ISO format)"}
       {:name "first-day-of-week"
        :type "number"
        :default "0"
        :description "First day of week (0=Sunday, 1=Monday, etc.)"}
       {:name "name"
        :type "string"
        :default "null"
        :description "Form field name for submission"}])]

    ;; Events Table
    [:div.mb-8
     [:h3.text-lg.font-medium.ty-text+.mb-3 "Events"]
     (event-table
      [{:name "change"
        :detail "{value: string, milliseconds: number, source: string, formatted: string}"
        :description "Fired when date/time value changes. Source can be 'selection', 'time-change', 'clear', or 'external'"}
       {:name "open"
        :detail "null"
        :description "Fired when the calendar dropdown opens"}])]

    ;; Properties Table
    [:div
     [:h3.text-lg.font-medium.ty-text+.mb-3 "Properties"]
     [:div.ty-bg-neutral-.rounded.p-4.overflow-x-auto
      [:pre.text-sm.ty-text
       "// JavaScript access
const picker = document.querySelector('ty-date-picker');
picker.value = '2024-09-21';  // Set date
picker.value = '2024-09-21T14:30';  // Set date and time
const currentValue = picker.value;  // Get current value"]]]]

   ;; Basic Usage
   [:div.ty-content.rounded-lg.p-6.mb-8
    [:h2.text-2xl.font-semibold.ty-text++.mb-6 "Basic Usage"]

    [:div.mb-6
     [:h3.text-lg.font-medium.ty-text+.mb-3 "Simple Date Picker"]
     [:div.mb-4
      [:ty-date-picker {:label "Select Date"}]]
     (code-block
      "<ty-date-picker label=\"Select Date\"></ty-date-picker>")]

    [:div.mb-6
     [:h3.text-lg.font-medium.ty-text+.mb-3 "With Initial Value"]
     [:div.mb-4
      [:ty-date-picker {:label "Event Date" :value "2024-09-21"}]]
     (code-block
      "<ty-date-picker 
  label=\"Event Date\" 
  value=\"2024-09-21\">
</ty-date-picker>")]

    [:div
     [:h3.text-lg.font-medium.ty-text+.mb-3 "With Time Selection"]
     [:div.mb-4
      [:ty-date-picker {:label "Appointment" :with-time "true" :value "2024-09-21T14:30"}]]
     (code-block
      "<ty-date-picker 
  label=\"Appointment\" 
  with-time=\"true\"
  value=\"2024-09-21T14:30\">
</ty-date-picker>")]]

   ;; Examples Section
   [:h2.text-2xl.font-semibold.ty-text++.mb-6 "Examples"]

   ;; Sizes Example
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text+.mb-4 "Sizes"]
    [:div.space-y-4.mb-4
     [:ty-date-picker {:size "xs" :placeholder "Extra Small"}]
     [:ty-date-picker {:size "sm" :placeholder "Small"}]
     [:ty-date-picker {:size "md" :placeholder "Medium (default)"}]
     [:ty-date-picker {:size "lg" :placeholder "Large"}]
     [:ty-date-picker {:size "xl" :placeholder "Extra Large"}]]
    (code-block
     "<ty-date-picker size=\"xs\" placeholder=\"Extra Small\"></ty-date-picker>
<ty-date-picker size=\"sm\" placeholder=\"Small\"></ty-date-picker>
<ty-date-picker size=\"md\" placeholder=\"Medium (default)\"></ty-date-picker>
<ty-date-picker size=\"lg\" placeholder=\"Large\"></ty-date-picker>
<ty-date-picker size=\"xl\" placeholder=\"Extra Large\"></ty-date-picker>")]

   ;; Flavors Example
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text+.mb-4 "Flavors"]
    [:div.space-y-4.mb-4
     [:ty-date-picker {:flavor "primary" :label "Primary" :value "2024-09-21"}]
     [:ty-date-picker {:flavor "secondary" :label "Secondary" :value "2024-09-21"}]
     [:ty-date-picker {:flavor "success" :label "Success" :value "2024-09-21"}]
     [:ty-date-picker {:flavor "danger" :label "Danger" :value "2024-09-21"}]
     [:ty-date-picker {:flavor "warning" :label "Warning" :value "2024-09-21"}]]
    (code-block
     "<ty-date-picker flavor=\"primary\" label=\"Primary\" value=\"2024-09-21\"></ty-date-picker>
<ty-date-picker flavor=\"secondary\" label=\"Secondary\" value=\"2024-09-21\"></ty-date-picker>
<ty-date-picker flavor=\"success\" label=\"Success\" value=\"2024-09-21\"></ty-date-picker>
<ty-date-picker flavor=\"danger\" label=\"Danger\" value=\"2024-09-21\"></ty-date-picker>
<ty-date-picker flavor=\"warning\" label=\"Warning\" value=\"2024-09-21\"></ty-date-picker>")]

   ;; Date Formats Example
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text+.mb-4 "Date Formats"]
    [:div.space-y-4.mb-4
     [:ty-date-picker {:format "short" :label "Short format" :value "2024-09-21"}]
     [:ty-date-picker {:format "medium" :label "Medium format" :value "2024-09-21"}]
     [:ty-date-picker {:format "long" :label "Long format (default)" :value "2024-09-21"}]
     [:ty-date-picker {:format "full" :label "Full format" :value "2024-09-21"}]]
    (code-block
     "<ty-date-picker format=\"short\" label=\"Short format\" value=\"2024-09-21\"></ty-date-picker>
<ty-date-picker format=\"medium\" label=\"Medium format\" value=\"2024-09-21\"></ty-date-picker>
<ty-date-picker format=\"long\" label=\"Long format (default)\" value=\"2024-09-21\"></ty-date-picker>
<ty-date-picker format=\"full\" label=\"Full format\" value=\"2024-09-21\"></ty-date-picker>")]

   ;; Localization Example
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text+.mb-4 "Internationalization"]
    [:div.space-y-4.mb-4
     [:ty-date-picker {:locale "en-US" :label "English (US)" :value "2024-09-21"}]
     [:ty-date-picker {:locale "de-DE" :label "German" :value "2024-09-21"}]
     [:ty-date-picker {:locale "fr-FR" :label "French" :value "2024-09-21"}]
     [:ty-date-picker {:locale "ja-JP" :label "Japanese" :value "2024-09-21"}]
     [:ty-date-picker {:locale "es-ES" :label "Spanish" :value "2024-09-21"}]]
    (code-block
     "<ty-date-picker locale=\"en-US\" label=\"English (US)\" value=\"2024-09-21\"></ty-date-picker>
<ty-date-picker locale=\"de-DE\" label=\"German\" value=\"2024-09-21\"></ty-date-picker>
<ty-date-picker locale=\"fr-FR\" label=\"French\" value=\"2024-09-21\"></ty-date-picker>
<ty-date-picker locale=\"ja-JP\" label=\"Japanese\" value=\"2024-09-21\"></ty-date-picker>
<ty-date-picker locale=\"es-ES\" label=\"Spanish\" value=\"2024-09-21\"></ty-date-picker>")]

   ;; Interactive Features Example
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text+.mb-4 "Interactive Features"]
    [:div.space-y-4.mb-4
     [:ty-date-picker {:label "Required field" :required "true"}]
     [:ty-date-picker {:label "Clearable" :clearable "true" :value "2024-09-21"}]
     [:ty-date-picker {:label "Disabled" :disabled "true" :value "2024-09-21"}]
     [:ty-date-picker {:label "With placeholder" :placeholder "Pick a date..."}]]
    (code-block
     "<ty-date-picker label=\"Required field\" required=\"true\"></ty-date-picker>
<ty-date-picker label=\"Clearable\" clearable=\"true\" value=\"2024-09-21\"></ty-date-picker>
<ty-date-picker label=\"Disabled\" disabled=\"true\" value=\"2024-09-21\"></ty-date-picker>
<ty-date-picker label=\"With placeholder\" placeholder=\"Pick a date...\"></ty-date-picker>")]

   ;; Time Input Example
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text+.mb-4 "Date and Time Selection"]
    [:p.ty-text-.mb-4 "When 'with-time' is enabled, users can select both date and time. The time input uses a special keyboard navigation:"]
    [:ul.list-disc.list-inside.ty-text-.mb-4
     [:li "Type digits directly to replace values"]
     [:li "Use arrow keys to navigate between hour and minute"]
     [:li "Backspace/Delete to clear individual digits"]
     [:li "Home/End to jump to start/end"]]
    [:div.space-y-4.mb-4
     [:ty-date-picker {:label "Meeting time" :with-time "true"}]
     [:ty-date-picker {:label "Appointment" :with-time "true" :value "2024-09-21T14:30"}]
     [:ty-date-picker {:label "Deadline" :with-time "true" :format "short" :value "2024-09-21T23:59"}]]
    (code-block
     "<ty-date-picker label=\"Meeting time\" with-time=\"true\"></ty-date-picker>
<ty-date-picker label=\"Appointment\" with-time=\"true\" value=\"2024-09-21T14:30\"></ty-date-picker>
<ty-date-picker label=\"Deadline\" with-time=\"true\" format=\"short\" value=\"2024-09-21T23:59\"></ty-date-picker>")]

   ;; Form Integration Example
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text+.mb-4 "Form Integration"]
    [:div.mb-4
     [:form {:id "date-form"
             :onsubmit "event.preventDefault(); 
                        const formData = new FormData(event.target);
                        document.getElementById('date-result').textContent = 
                        'Selected: ' + formData.get('event-date');
                        return false;"}
      [:div.space-y-4
       [:ty-date-picker {:label "Event Date" :name "event-date" :required "true"}]
       [:button.ty-button.primary {:type "submit"} "Submit"]]
      [:div#date-result.ty-text-.mt-4]]]
    (code-block
     "<form id=\"date-form\">
  <ty-date-picker 
    label=\"Event Date\" 
    name=\"event-date\" 
    required=\"true\">
  </ty-date-picker>
  <button type=\"submit\" class=\"ty-button primary\">Submit</button>
</form>

<script>
document.getElementById('date-form').addEventListener('submit', (e) => {
  e.preventDefault();
  const formData = new FormData(e.target);
  console.log('Date:', formData.get('event-date'));
});
</script>")]

   ;; Programmatic Control Example
   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text+.mb-4 "Programmatic Control"]
    [:div.mb-4
     [:ty-date-picker {:id "prog-picker" :label "Controlled Date"}]
     [:div.flex.gap-2.mt-4
      [:button.ty-button.secondary.sm
       {:onclick "document.getElementById('prog-picker').value = '2024-09-21'"}
       "Set Today"]
      [:button.ty-button.secondary.sm
       {:onclick "document.getElementById('prog-picker').value = '2024-12-25'"}
       "Set Christmas"]
      [:button.ty-button.secondary.sm
       {:onclick "document.getElementById('prog-picker').value = ''"}
       "Clear"]
      [:button.ty-button.secondary.sm
       {:onclick "alert('Current: ' + document.getElementById('prog-picker').value)"}
       "Get Value"]]]
    (code-block
     "<ty-date-picker id=\"date-picker\" label=\"Controlled Date\"></ty-date-picker>

<script>
const picker = document.getElementById('date-picker');

// Set value
picker.value = '2024-09-21';
picker.value = '2024-09-21T14:30'; // With time

// Get value
console.log(picker.value);

// Listen for changes
picker.addEventListener('change', (e) => {
  console.log('New date:', e.detail.value);
  console.log('Formatted:', e.detail.formatted);
  console.log('Source:', e.detail.source);
  console.log('Milliseconds:', e.detail.milliseconds);
});

// Listen for open event
picker.addEventListener('open', () => {
  console.log('Calendar opened');
});
</script>")]

   ;; Common Use Cases
   [:h2.text-2xl.font-semibold.ty-text++.mb-6 "Common Use Cases"]

   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text+.mb-4 "Booking System"]
    [:div.mb-4
     [:div.space-y-4
      [:ty-date-picker {:label "Check-in Date" :name "checkin" :min-date "2024-09-21"}]
      [:ty-date-picker {:label "Check-out Date" :name "checkout" :min-date "2024-09-22"}]
      [:ty-date-picker {:label "Preferred Appointment Time" :name "appointment" :with-time "true"}]]]
    (code-block
     "<!-- Date range selection -->
<ty-date-picker 
  label=\"Check-in Date\" 
  name=\"checkin\" 
  min-date=\"2024-09-21\">
</ty-date-picker>

<ty-date-picker 
  label=\"Check-out Date\" 
  name=\"checkout\" 
  min-date=\"2024-09-22\">
</ty-date-picker>

<!-- Appointment scheduling -->
<ty-date-picker 
  label=\"Preferred Appointment Time\" 
  name=\"appointment\" 
  with-time=\"true\">
</ty-date-picker>")]

   [:div.ty-content.rounded-lg.p-6.mb-6
    [:h3.text-lg.font-medium.ty-text+.mb-4 "Event Planning"]
    [:div.mb-4
     [:div.space-y-4
      [:ty-date-picker {:label "Event Start" :name "event-start" :with-time "true" :required "true"}]
      [:ty-date-picker {:label "Event End" :name "event-end" :with-time "true" :required "true"}]
      [:ty-date-picker {:label "Registration Deadline" :name "deadline" :clearable "true"}]]]
    (code-block
     "<!-- Event scheduling with time -->
<ty-date-picker 
  label=\"Event Start\" 
  name=\"event-start\" 
  with-time=\"true\"
  required=\"true\">
</ty-date-picker>

<ty-date-picker 
  label=\"Event End\" 
  name=\"event-end\" 
  with-time=\"true\"
  required=\"true\">
</ty-date-picker>

<!-- Optional deadline -->
<ty-date-picker 
  label=\"Registration Deadline\" 
  name=\"deadline\" 
  clearable=\"true\">
</ty-date-picker>")]

   ;; Best Practices
   [:div.ty-elevated.rounded-lg.p-6
    [:h2.text-2xl.font-semibold.ty-text++.mb-6 "Best Practices"]

    [:div.grid.gap-6.md:grid-cols-2
     ;; Do's
     [:div
      [:h3.flex.items-center.gap-2.text-lg.font-medium.ty-text-success.mb-3
       [:ty-icon {:name "check-circle" :size "20"}]
       "Do's"]
      [:ul.space-y-2.ty-text-
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-success.mt-1 {:name "check" :size "16"}]
        [:span "Use appropriate date formats for your locale"]]
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-success.mt-1 {:name "check" :size "16"}]
        [:span "Provide clear labels for date fields"]]
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-success.mt-1 {:name "check" :size "16"}]
        [:span "Use 'with-time' for appointments and deadlines"]]
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-success.mt-1 {:name "check" :size "16"}]
        [:span "Set min/max dates for valid date ranges"]]
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-success.mt-1 {:name "check" :size "16"}]
        [:span "Use 'clearable' for optional date fields"]]
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-success.mt-1 {:name "check" :size "16"}]
        [:span "Handle the change event for validation"]]]]

     ;; Don'ts
     [:div
      [:h3.flex.items-center.gap-2.text-lg.font-medium.ty-text-danger.mb-3
       [:ty-icon {:name "x-circle" :size "20"}]
       "Don'ts"]
      [:ul.space-y-2.ty-text-
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-danger.mt-1 {:name "x" :size "16"}]
        [:span "Don't use ambiguous date formats"]]
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-danger.mt-1 {:name "x" :size "16"}]
        [:span "Don't forget to set locale for non-English users"]]
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-danger.mt-1 {:name "x" :size "16"}]
        [:span "Don't disable without explaining why"]]
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-danger.mt-1 {:name "x" :size "16"}]
        [:span "Don't mix date-only and datetime values in the same form section"]]
       [:li.flex.items-start.gap-2
        [:ty-icon.ty-text-danger.mt-1 {:name "x" :size "16"}]
        [:span "Don't ignore timezone considerations for datetime values"]]]]]]

   ;; Notes
   [:div.ty-bg-warning-.ty-border-warning.border.rounded-lg.p-4.mt-8
    [:h3.flex.items-center.gap-2.ty-text-warning++.font-semibold
     [:ty-icon {:name "alert-triangle" :size "20"}]
     "Important Notes"]
    [:ul.list-disc.list-inside.ty-text-warning.mt-2.space-y-1
     [:li "Date values are always in ISO 8601 format (YYYY-MM-DD or YYYY-MM-DDTHH:mm)"]
     [:li "Time values are in 24-hour format when 'with-time' is enabled"]
     [:li "The component handles timezone conversion internally"]
     [:li "Form submission uses the 'name' attribute for field identification"]
     [:li "The calendar dropdown automatically positions itself to avoid viewport edges"]]]])