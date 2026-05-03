(ns ty.site.docs.checkbox
  (:require [ty.site.docs.common :as common]))

(defn view []
  [:div.max-w-4xl.mx-auto.px-4.md:px-6.py-8
   [:h1.text-3xl.md:text-4xl.font-bold.ty-text++.mb-3 "Checkbox"]
   [:p.text-base.ty-text-.mb-6.leading-relaxed
    [:strong "Just the box."]
    " Renders only the checkbox visual + boolean state. Wrap in a "
    [:code.ty-bg-neutral-.px-1.rounded "<label>"]
    " for click-on-text behavior; the browser delegates clicks to the form-associated child. Required indicator and error display are the consumer's responsibility."]

   [:section.mb-10
    [:h2.text-xl.font-semibold.ty-text++.mb-3 "Basic"]
    [:div.ty-content.rounded-lg.p-6.mb-4.flex.flex-col.gap-3
     [:label.flex.items-center.gap-2.cursor-pointer
      [:ty-checkbox]
      [:span "Subscribe to newsletter"]]
     [:label.flex.items-center.gap-2.cursor-pointer
      [:ty-checkbox {:checked ""}]
      [:span "Pre-checked option"]]
     [:label.flex.items-center.gap-2.cursor-pointer.opacity-60
      [:ty-checkbox {:disabled ""}]
      [:span "Disabled"]]]
    (common/code-block
      "<label>
  <ty-checkbox></ty-checkbox>
  Subscribe to newsletter
</label>

<label>
  <ty-checkbox checked></ty-checkbox>
  Pre-checked option
</label>"
      "html")]

   [:section.mb-10
    [:h2.text-xl.font-semibold.ty-text++.mb-3 "Rich label content"]
    [:p.ty-text-.mb-3
     "Because the checkbox doesn't own the text, you can put any markup next to it — links, icons, formatting — and clicks on those interactive children "
     [:strong "do not"]
     " toggle the checkbox."]
    [:div.ty-content.rounded-lg.p-6.mb-4
     [:label.flex.items-start.gap-2.cursor-pointer
      [:ty-checkbox {:required ""
                     :flavor "primary"}]
      [:span
       "I agree to the "
       [:a.ty-text-primary.underline {:href "#"
                                      :on {:click (fn [e] (.preventDefault e))}}
        "Terms of Service"]
       " and "
       [:a.ty-text-primary.underline {:href "#"
                                      :on {:click (fn [e] (.preventDefault e))}}
        "Privacy Policy"]
       "."]]]
    (common/code-block
      "<label>
  <ty-checkbox required></ty-checkbox>
  I agree to the <a href=\"/terms\">Terms</a> and <a href=\"/privacy\">Privacy</a>.
</label>"
      "html")]

   [:section.mb-10
    [:h2.text-xl.font-semibold.ty-text++.mb-3 "Sizes"]
    [:div.ty-content.rounded-lg.p-6.mb-4.flex.flex-col.gap-3
     [:label.flex.items-center.gap-2.cursor-pointer.text-xs
      [:ty-checkbox {:size "xs" :checked ""}] [:span "Extra small"]]
     [:label.flex.items-center.gap-2.cursor-pointer.text-sm
      [:ty-checkbox {:size "sm" :checked ""}] [:span "Small"]]
     [:label.flex.items-center.gap-2.cursor-pointer
      [:ty-checkbox {:size "md" :checked ""}] [:span "Medium (default)"]]
     [:label.flex.items-center.gap-2.cursor-pointer.text-lg
      [:ty-checkbox {:size "lg" :checked ""}] [:span "Large"]]
     [:label.flex.items-center.gap-2.cursor-pointer.text-xl
      [:ty-checkbox {:size "xl" :checked ""}] [:span "Extra large"]]]]

   [:section.mb-10
    [:h2.text-xl.font-semibold.ty-text++.mb-3 "Flavors"]
    [:div.ty-content.rounded-lg.p-6.mb-4.flex.flex-col.gap-2
     (for [flavor ["primary" "secondary" "success" "danger" "warning" "neutral"]]
       ^{:key flavor}
       [:label.flex.items-center.gap-2.cursor-pointer
        [:ty-checkbox {:flavor flavor :checked ""}]
        [:span (str (clojure.string/capitalize flavor))]])]]

   [:section.mb-10
    [:h2.text-xl.font-semibold.ty-text++.mb-3 "Validation"]
    [:p.ty-text-.mb-3
     "Validation UI is the consumer's job. The "
     [:code.ty-bg-neutral-.px-1.rounded "required"]
     " attribute participates in form validation and sets "
     [:code.ty-bg-neutral-.px-1.rounded "aria-required"]
     "; render the error message and required asterisk yourself."]
    [:div.ty-content.rounded-lg.p-6.mb-4
     [:label.flex.items-center.gap-2.cursor-pointer
      [:ty-checkbox {:required ""
                     :flavor "danger"}]
      [:span
       "Accept terms"
       [:span.ty-text-danger.ml-1 "*"]]]
     [:p.text-xs.ty-text-danger.mt-2 "You must accept the terms to continue."]]
    (common/code-block
      "<label>
  <ty-checkbox required flavor=\"danger\"></ty-checkbox>
  Accept terms <span class=\"required-indicator\">*</span>
</label>
<p class=\"error\">You must accept the terms to continue.</p>"
      "html")]

   [:section.mb-10
    [:h2.text-xl.font-semibold.ty-text++.mb-3 "Attributes"]
    [:div.ty-content.rounded-lg.p-4.text-sm.ty-text-
     [:ul.list-disc.pl-6.space-y-1
      [:li [:code.ty-bg-neutral-.px-1.rounded "checked"] " — boolean state"]
      [:li [:code.ty-bg-neutral-.px-1.rounded "value"] " — form-submission value when checked (default \"on\")"]
      [:li [:code.ty-bg-neutral-.px-1.rounded "name"] " — form field name"]
      [:li [:code.ty-bg-neutral-.px-1.rounded "disabled"] " — disable interaction"]
      [:li [:code.ty-bg-neutral-.px-1.rounded "required"] " — sets aria-required and triggers form validation"]
      [:li [:code.ty-bg-neutral-.px-1.rounded "size"] " — xs/sm/md/lg/xl"]
      [:li [:code.ty-bg-neutral-.px-1.rounded "flavor"] " — primary/secondary/success/danger/warning/neutral"]]]]

   [:section
    [:h2.text-xl.font-semibold.ty-text++.mb-3 "Events"]
    [:p.ty-text-.mb-3
     "Emits both "
     [:code.ty-bg-neutral-.px-1.rounded "input"]
     " and "
     [:code.ty-bg-neutral-.px-1.rounded "change"]
     " events with detail object:"]
    (common/code-block
      "checkboxEl.addEventListener('change', (e) => {
  console.log(e.detail.value);     // true | false
  console.log(e.detail.checked);   // true | false
  console.log(e.detail.formValue); // 'on' | null
});"
      "javascript")]])
