(ns tyrell.site.docs.switch
  (:require [tyrell.site.docs.common :as common]))

(defn view []
  [:div.max-w-4xl.mx-auto.px-4.md:px-6.py-8
   [:h1.text-3xl.md:text-4xl.font-bold.ty-text++.mb-3 "Switch"]
   [:p.text-base.ty-text-.mb-6.leading-relaxed
    [:strong "Just the toggle."]
    " Renders the track + thumb visual with "
    [:code.ty-bg-neutral-.px-1.rounded "role=\"switch\""]
    " ARIA — the label and any validation UI are the consumer's responsibility. Wrap in a "
    [:code.ty-bg-neutral-.px-1.rounded "<label>"]
    " for click-on-text behavior; the browser delegates clicks to the form-associated child."]

   [:section.mb-10
    [:h2.text-xl.font-semibold.ty-text++.mb-3 "Basic"]
    [:div.ty-content.rounded-lg.p-6.mb-4.flex.flex-col.items-start.gap-3
     [:label.inline-flex.items-center.gap-3.cursor-pointer.text-sm.font-medium
      [:ty-switch {:checked ""}]
      [:span.whitespace-nowrap "Email notifications"]]
     [:label.inline-flex.items-center.gap-3.cursor-pointer.text-sm.font-medium
      [:ty-switch {:flavor "success"}]
      [:span.whitespace-nowrap "Auto-save"]]
     [:label.inline-flex.items-center.gap-3.cursor-pointer.text-sm.font-medium
      [:ty-switch {:flavor "danger" :checked ""}]
      [:span.whitespace-nowrap "Delete on inactive"]]
     [:label.inline-flex.items-center.gap-3.cursor-pointer.text-sm.font-medium.opacity-60
      [:ty-switch {:disabled ""}]
      [:span.whitespace-nowrap "Cannot toggle"]]]
    (common/code-block
      "<label>
  <ty-switch checked></ty-switch>
  Email notifications
</label>

<label>
  <ty-switch flavor=\"success\"></ty-switch>
  Auto-save
</label>"
      "html")]

   [:section.mb-10
    [:h2.text-xl.font-semibold.ty-text++.mb-3 "Sizes"]
    [:div.ty-content.rounded-lg.p-6.mb-4.flex.flex-col.items-start.gap-3
     [:label.inline-flex.items-center.gap-3.cursor-pointer.text-xs
      [:ty-switch {:size "xs" :checked ""}] [:span.whitespace-nowrap "Extra small"]]
     [:label.inline-flex.items-center.gap-3.cursor-pointer.text-sm
      [:ty-switch {:size "sm" :checked ""}] [:span.whitespace-nowrap "Small"]]
     [:label.inline-flex.items-center.gap-3.cursor-pointer
      [:ty-switch {:size "md" :checked ""}] [:span.whitespace-nowrap "Medium (default)"]]
     [:label.inline-flex.items-center.gap-3.cursor-pointer.text-lg
      [:ty-switch {:size "lg" :checked ""}] [:span.whitespace-nowrap "Large"]]
     [:label.inline-flex.items-center.gap-3.cursor-pointer.text-xl
      [:ty-switch {:size "xl" :checked ""}] [:span.whitespace-nowrap "Extra large"]]]]

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
    [:p.ty-text-.mb-3 "Emits both " [:code.ty-bg-neutral-.px-1.rounded "input"] " and " [:code.ty-bg-neutral-.px-1.rounded "change"] " events with detail:"]
    (common/code-block
      "switchEl.addEventListener('change', (e) => {
  console.log(e.detail.value);    // true | false
  console.log(e.detail.checked);  // true | false
  console.log(e.detail.formValue); // 'on' | null
});"
      "javascript")]])
