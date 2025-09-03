(ns ty.demo.views.buttons)

(defn demo-row [{:keys [title description children]}]
  [:div.mb-8
   (when title
     [:h3.demo-subtitle title])
   (when description
     [:p.ty-text-.mb-4 description]) ; NEW: ty-text-
   (into [:div.flex.flex-wrap.gap-4.items-center] children)]) ;; <- Use 'into' to splice children

(defn code-snippet [code]
  [:div.mt-4
   [:pre.code-block.text-xs
    [:code code]]])

(defn view []
  [:div.max-w-6xl.mx-auto
   [:div.mb-8
    [:h1.text-3xl.font-bold.ty-text.mb-2 ; NEW: ty-text
     "Button Component"]
    [:p.text-lg.ty-text- ; NEW: ty-text-
     "A versatile button component with semantic flavors, multiple appearances, and size variants."]]

   ;; Flavors section
   [:div.demo-section
    [:h2.demo-title "Semantic Flavors"]
    (demo-row {:description "Buttons use semantic naming to convey meaning, not just color."
               :children [[:ty-button {:flavor "primary"} "Primary"]
                          [:ty-button {:flavor "secondary"} "Secondary"]
                          [:ty-button {:flavor "success"} "Success"]
                          [:ty-button {:flavor "danger"} "Danger"]
                          [:ty-button {:flavor "warning"} "Warning"]
                          [:ty-button {:flavor "neutral"} "Neutral"]]})
    (code-snippet "<ty-button flavor=\"success\">Success</ty-button>")]

   ;; Appearances section
   [:div.demo-section
    [:h2.demo-title "Appearances"]

    ;; Overview comparison table
    (demo-row {:title "Appearance Comparison"
               :description "Same semantic colors across different appearances"
               :children [;; Primary row
                          [:div.ty-elevated.p-4.rounded-lg.space-y-2.min-w-max
                           [:div.ty-text-.text-sm.font-medium "Primary"]
                           [:div.flex.gap-2
                            [:ty-button {:flavor "primary"} "Plain"]
                            [:ty-button {:flavor "primary" :filled true} "Filled"]
                            [:ty-button {:flavor "primary" :outlined true} "Outlined"]
                            [:ty-button {:flavor "primary" :accent true} "Accent"]]]
                          ;; Success row  
                          [:div.ty-elevated.p-4.rounded-lg.space-y-2.min-w-max
                           [:div.ty-text-.text-sm.font-medium "Success"]
                           [:div.flex.gap-2
                            [:ty-button {:flavor "success"} "Plain"]
                            [:ty-button {:flavor "success" :filled true} "Filled"]
                            [:ty-button {:flavor "success" :outlined true} "Outlined"]
                            [:ty-button {:flavor "success" :accent true} "Accent"]]]
                          ;; Danger row
                          [:div.ty-elevated.p-4.rounded-lg.space-y-2.min-w-max
                           [:div.ty-text-.text-sm.font-medium "Danger"]
                           [:div.flex.gap-2
                            [:ty-button {:flavor "danger"} "Plain"]
                            [:ty-button {:flavor "danger" :filled true} "Filled"]
                            [:ty-button {:flavor "danger" :outlined true} "Outlined"]
                            [:ty-button {:flavor "danger" :accent true} "Accent"]]]]})

    (demo-row {:title "Filled"
               :description "Adds a tonal background color"
               :children [[:ty-button {:flavor "primary"
                                       :filled true} "Primary"]
                          [:ty-button {:flavor "secondary"
                                       :filled true} "Secondary"]
                          [:ty-button {:flavor "success"
                                       :filled true} "Success"]
                          [:ty-button {:flavor "danger"
                                       :filled true} "Danger"]
                          [:ty-button {:flavor "warning"
                                       :filled true} "Warning"]
                          [:ty-button {:flavor "neutral"
                                       :filled true} "Neutral"]]})
    (code-snippet "<ty-button flavor=\"success\" filled>Filled</ty-button>")

    (demo-row {:title "Outlined"
               :description "Adds a colored border"
               :children [[:ty-button {:flavor "primary"
                                       :outlined true} "Primary"]
                          [:ty-button {:flavor "secondary"
                                       :outlined true} "Secondary"]
                          [:ty-button {:flavor "success"
                                       :outlined true} "Success"]
                          [:ty-button {:flavor "danger"
                                       :outlined true} "Danger"]
                          [:ty-button {:flavor "warning"
                                       :outlined true} "Warning"]
                          [:ty-button {:flavor "neutral"
                                       :outlined true} "Neutral"]]})
    (code-snippet "<ty-button flavor=\"success\" outlined>Outlined</ty-button>")

    (demo-row {:title "Accent"
               :description "Full color background (overrides other appearances)"
               :children [[:ty-button {:flavor "primary"
                                       :accent true} "Primary"]
                          [:ty-button {:flavor "secondary"
                                       :accent true} "Secondary"]
                          [:ty-button {:flavor "success"
                                       :accent true} "Success"]
                          [:ty-button {:flavor "danger"
                                       :accent true} "Danger"]
                          [:ty-button {:flavor "warning"
                                       :accent true} "Warning"]
                          [:ty-button {:flavor "neutral"
                                       :accent true} "Neutral"]]})
    (code-snippet "<ty-button flavor=\"success\" accent>Accent</ty-button>")

    (demo-row {:title "Combined"
               :description "Combine filled and outlined for a unique look"
               :children [[:ty-button {:flavor "primary"
                                       :filled true
                                       :outlined true} "Primary"]
                          [:ty-button {:flavor "success"
                                       :filled true
                                       :outlined true} "Success"]
                          [:ty-button {:flavor "danger"
                                       :filled true
                                       :outlined true} "Danger"]]})
    (code-snippet "<ty-button flavor=\"success\" filled outlined>Combined</ty-button>")]

   ;; Sizes section
   [:div.demo-section
    [:h2.demo-title "Sizes"]
    (demo-row {:description "Five size variants to fit different contexts"
               :children [[:ty-button {:flavor "primary"
                                       :size "xs"} "Extra Small"]
                          [:ty-button {:flavor "primary"
                                       :size "sm"} "Small"]
                          [:ty-button {:flavor "primary"
                                       :size "md"} "Medium"]
                          [:ty-button {:flavor "primary"
                                       :size "lg"} "Large"]
                          [:ty-button {:flavor "primary"
                                       :size "xl"} "Extra Large"]]})
    (code-snippet "<ty-button flavor=\"primary\" size=\"lg\">Large</ty-button>")]

   ;; With icons section
   [:div.demo-section
    [:h2.demo-title "With Icons"]
    (demo-row {:title "Start slot"
               :children [[:ty-button {:flavor "success"
                                       :filled true}
                           [:ty-icon {:name "check"
                                      :slot "start"}]
                           "Save"]
                          [:ty-button {:flavor "danger"
                                       :outlined true}
                           [:ty-icon {:name "trash-2"
                                      :slot "start"}]
                           "Delete"]
                          [:ty-button {:flavor "primary"
                                       :accent true}
                           [:ty-icon {:name "download"
                                      :slot "start"}]
                           "Download"]]})
    (code-snippet "<ty-button flavor=\"success\" filled>\n  <ty-icon name=\"check\" slot=\"start\"></ty-icon>\n  Save\n</ty-button>")

    (demo-row {:title "End slot"
               :children [[:ty-button {:flavor "neutral"}
                           "Next"
                           [:ty-icon {:name "arrow-right"
                                      :slot "end"}]]
                          [:ty-button {:flavor "primary"
                                       :outlined true}
                           "Settings"
                           [:ty-icon {:name "settings"
                                      :slot "end"}]]]})

    (demo-row {:title "Icon only"
               :children [[:ty-button {:flavor "primary"
                                       :size "sm"}
                           [:ty-icon {:name "heart"}]]
                          [:ty-button {:flavor "success"
                                       :filled true}
                           [:ty-icon {:name "plus"}]]
                          [:ty-button {:flavor "danger"
                                       :outlined true
                                       :size "lg"}
                           [:ty-icon {:name "x"}]]]})]

   ;; Pill buttons section
   [:div.demo-section
    [:h2.demo-title "Pill Buttons"]
    (demo-row {:title "Basic pill buttons"
               :description "Pill buttons have fully rounded corners"
               :children [[:ty-button {:flavor "primary"
                                       :pill true} "Get Started"]
                          [:ty-button {:flavor "success"
                                       :filled true
                                       :pill true} "Success"]
                          [:ty-button {:flavor "danger"
                                       :outlined true
                                       :pill true} "Cancel"]
                          [:ty-button {:flavor "secondary"
                                       :accent true
                                       :pill true} "Special"]]})
    (code-snippet "<ty-button flavor=\"primary\" pill>Get Started</ty-button>")

    (demo-row {:title "Pill buttons with icons"
               :description "Pill buttons work great with icons"
               :children [[:ty-button {:flavor "success"
                                       :pill true
                                       :filled true}
                           [:ty-icon {:name "check"
                                      :slot "start"}]
                           "Complete"]
                          [:ty-button {:flavor "primary"
                                       :pill true
                                       :accent true}
                           [:ty-icon {:name "rocket"
                                      :slot "start"}]
                           "Launch"]
                          [:ty-button {:flavor "danger"
                                       :pill true
                                       :outlined true}
                           "Dismiss"
                           [:ty-icon {:name "x"
                                      :slot "end"}]]]})

    (demo-row {:title "Icon-only pill buttons (circular)"
               :description "Icon-only pill buttons become perfectly circular"
               :children [[:ty-button {:flavor "primary"
                                       :pill true
                                       :size "xs"}
                           [:ty-icon {:name "plus"}]]
                          [:ty-button {:flavor "success"
                                       :pill true
                                       :size "sm"
                                       :filled true}
                           [:ty-icon {:name "check"}]]
                          [:ty-button {:flavor "danger"
                                       :pill true
                                       :size "md"
                                       :outlined true}
                           [:ty-icon {:name "x"}]]
                          [:ty-button {:flavor "secondary"
                                       :pill true
                                       :size "lg"
                                       :accent true}
                           [:ty-icon {:name "star"}]]
                          [:ty-button {:flavor "warning"
                                       :pill true
                                       :size "xl"
                                       :filled true}
                           [:ty-icon {:name "alert-circle"}]]]})
    (code-snippet "<ty-button flavor=\"primary\" pill size=\"md\">\n  <ty-icon name=\"plus\"></ty-icon>\n</ty-button>")

    (demo-row {:title "Different sizes"
               :description "Pill buttons scale beautifully across all sizes"
               :children [[:ty-button {:flavor "primary"
                                       :pill true
                                       :size "xs"} "XS Pill"]
                          [:ty-button {:flavor "primary"
                                       :pill true
                                       :size "sm"} "SM Pill"]
                          [:ty-button {:flavor "primary"
                                       :pill true
                                       :size "md"} "MD Pill"]
                          [:ty-button {:flavor "primary"
                                       :pill true
                                       :size "lg"} "LG Pill"]
                          [:ty-button {:flavor "primary"
                                       :pill true
                                       :size "xl"} "XL Pill"]]})]

   ;; States section

   ;; Action buttons section
   [:div.demo-section
    [:h2.demo-title "Action Buttons"]
    (demo-row {:title "Square padding buttons"
               :description "Action buttons have equal padding on all sides, perfect for icon buttons"
               :children [[:ty-button {:flavor "primary"
                                       :action true}
                           [:ty-icon {:name "settings"}]]
                          [:ty-button {:flavor "success"
                                       :filled true
                                       :action true}
                           [:ty-icon {:name "edit"}]]
                          [:ty-button {:flavor "danger"
                                       :outlined true
                                       :action true}
                           [:ty-icon {:name "trash-2"}]]
                          [:ty-button {:flavor "secondary"
                                       :accent true
                                       :action true}
                           [:ty-icon {:name "star"}]]]})
    (code-snippet "<ty-button flavor=\"primary\" action>\n  <ty-icon name=\"settings\"></ty-icon>\n</ty-button>")

    (demo-row {:title "Action with text"
               :description "Action modifier works with text too, creating compact square buttons"
               :children [[:ty-button {:flavor "neutral"
                                       :action true
                                       :size "sm"} "A"]
                          [:ty-button {:flavor "primary"
                                       :action true} "B"]
                          [:ty-button {:flavor "success"
                                       :action true
                                       :filled true
                                       :size "lg"} "C"]
                          [:ty-button {:flavor "danger"
                                       :action true
                                       :accent true
                                       :size "xl"} "D"]]})

    (demo-row {:title "Action + Pill combination"
               :description "Combining action and pill creates perfect circles for icon buttons"
               :children [[:ty-button {:flavor "primary"
                                       :action true
                                       :pill true
                                       :size "sm"}
                           [:ty-icon {:name "plus"}]]
                          [:ty-button {:flavor "success"
                                       :action true
                                       :pill true
                                       :filled true}
                           [:ty-icon {:name "check"}]]
                          [:ty-button {:flavor "danger"
                                       :action true
                                       :pill true
                                       :outlined true
                                       :size "lg"}
                           [:ty-icon {:name "x"}]]
                          [:ty-button {:flavor "secondary"
                                       :action true
                                       :pill true
                                       :accent true
                                       :size "xl"}
                           [:ty-icon {:name "heart"}]]]})
    (code-snippet "<ty-button flavor=\"primary\" action pill>\n  <ty-icon name=\"plus\"></ty-icon>\n</ty-button>")

    (demo-row {:title "Action buttons across sizes"
               :description "Action buttons maintain square padding across all sizes"
               :children [[:ty-button {:flavor "primary"
                                       :action true
                                       :size "xs"}
                           [:ty-icon {:name "settings"}]]
                          [:ty-button {:flavor "primary"
                                       :action true
                                       :size "sm"}
                           [:ty-icon {:name "settings"}]]
                          [:ty-button {:flavor "primary"
                                       :action true
                                       :size "md"}
                           [:ty-icon {:name "settings"}]]
                          [:ty-button {:flavor "primary"
                                       :action true
                                       :size "lg"}
                           [:ty-icon {:name "settings"}]]
                          [:ty-button {:flavor "primary"
                                       :action true
                                       :size "xl"}
                           [:ty-icon {:name "settings"}]]]})]
   [:div.demo-section
    [:h2.demo-title "Form Integration"]
    [:div.mb-4
     [:p.ty-text-.mb-2 "ty-button now supports form submission and reset functionality using the `type` attribute."]
     [:p.ty-text-.text-sm "✅ Form submission works! Check browser console to see form data."]
     [:p.ty-text-.text-xs.ty-text--
      "Note: Due to Web Component shadow DOM boundaries, individual button name/value pairs aren't included in the form data, but form submission and reset work perfectly."]]

    (demo-row {:title "Submit Button"
               :description "type=\"submit\" (default) submits the form when clicked"
               :children [[:form {:class "ty-elevated p-4 rounded-lg space-y-3"
                                  :onsubmit "event.preventDefault(); const formData = new FormData(event.target); const data = Object.fromEntries(formData); console.log('Form submitted with data:', data); alert('Form submitted successfully! Data: ' + JSON.stringify(data));"}
                           [:div.space-y-2
                            [:label.ty-text-.text-sm "Sample Input:"]
                            [:input {:type "text" :name "sampleInput" :class "px-3 py-2 border rounded" :placeholder "Enter some data..."}]
                            [:label.ty-text-.text-sm "Select Option:"]
                            [:select {:name "sampleSelect" :class "px-3 py-2 border rounded"}
                             [:option {:value "option1"} "Option 1"]
                             [:option {:value "option2"} "Option 2"]
                             [:option {:value "option3"} "Option 3"]]]
                           [:div.flex.gap-2
                            [:ty-button {:type "submit" :flavor "primary"} "Submit Form"]
                            [:ty-button {:type "submit" :flavor "success" :filled true} "Submit Alt"]]]]})

    (demo-row {:title "Reset Button"
               :description "type=\"reset\" clears all form fields"
               :children [[:form {:class "ty-elevated p-4 rounded-lg space-y-3"
                                  :onsubmit "event.preventDefault();"}
                           [:div.space-y-2
                            [:label.ty-text-.text-sm "Try typing and then reset:"]
                            [:input {:type "text" :name "resetInput" :class "px-3 py-2 border rounded" :placeholder "Type something here..."}]
                            [:textarea {:name "resetTextarea" :class "px-3 py-2 border rounded w-full h-20" :placeholder "Or here..."}]]
                           [:div.flex.gap-2
                            [:ty-button {:type "reset" :flavor "warning" :outlined true} "Reset Form"]
                            [:ty-button {:type "submit" :flavor "primary"} "Submit"]]]]})

    (demo-row {:title "Button Type"
               :description "type=\"button\" does nothing special, just dispatches click events"
               :children [[:div.ty-elevated.p-4.rounded-lg.space-y-3
                           [:p.ty-text-.text-sm "These buttons won't submit or reset forms:"]
                           [:div.flex.gap-2
                            [:ty-button {:type "button"
                                         :flavor "neutral"
                                         :onclick "alert('Just a regular button click!')"} "Regular Button"]
                            [:ty-button {:type "button"
                                         :flavor "secondary"
                                         :filled true
                                         :onclick "console.log('Button clicked but no form action')"} "Another Button"]]]]})

    (code-snippet "<form>\n  <!-- Submit button (default) -->\n  <ty-button type=\"submit\" name=\"action\" value=\"save\">Save</ty-button>\n  \n  <!-- Reset button -->\n  <ty-button type=\"reset\">Clear Form</ty-button>\n  \n  <!-- Regular button -->\n  <ty-button type=\"button\">Just a Button</ty-button>\n</form>")]

   [:div.demo-section
    [:h2.demo-title "States"]
    (demo-row {:title "Disabled"
               :children [[:ty-button {:flavor "primary"
                                       :disabled true} "Disabled"]
                          [:ty-button {:flavor "success"
                                       :filled true
                                       :disabled true} "Disabled"]
                          [:ty-button {:flavor "danger"
                                       :accent true
                                       :disabled true} "Disabled"]]})
    (code-snippet "<ty-button flavor=\"primary\" disabled>Disabled</ty-button>")]])

