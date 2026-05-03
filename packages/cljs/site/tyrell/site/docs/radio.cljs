(ns tyrell.site.docs.radio
  (:require [tyrell.site.docs.common :as common]))

(defn view []
  [:div.max-w-4xl.mx-auto.px-4.md:px-6.py-8
   [:h1.text-3xl.md:text-4xl.font-bold.ty-text++.mb-3 "Radio Group"]
   [:p.text-base.ty-text-.mb-6.leading-relaxed
    "Exclusive single-choice selection. "
    [:strong [:code.ty-bg-neutral-.px-1.rounded "ty-radio-group"]]
    " is the form-field abstraction (label, error, value, form participation). "
    [:strong [:code.ty-bg-neutral-.px-1.rounded "ty-radio"]]
    " is just the circle — wrap each in a "
    [:code.ty-bg-neutral-.px-1.rounded "<label>"]
    " for click-on-text behavior. Arrow keys navigate AND change selection per W3C ARIA practice."]

   [:section.mb-10
    [:h2.text-xl.font-semibold.ty-text++.mb-3 "Basic"]
    [:div.ty-content.rounded-lg.p-6.mb-4
     [:ty-radio-group {:label "Plan"
                       :name "plan"
                       :value "pro"}
      [:label.inline-flex.items-center.gap-2.cursor-pointer.text-sm.font-medium
       [:ty-radio {:value "free"}] [:span.whitespace-nowrap "Free"]]
      [:label.inline-flex.items-center.gap-2.cursor-pointer.text-sm.font-medium
       [:ty-radio {:value "pro"}] [:span.whitespace-nowrap "Pro"]]
      [:label.inline-flex.items-center.gap-2.cursor-pointer.text-sm.font-medium
       [:ty-radio {:value "team"}] [:span.whitespace-nowrap "Team"]]]]
    (common/code-block
      "<ty-radio-group label=\"Plan\" name=\"plan\" value=\"pro\">
  <label><ty-radio value=\"free\"></ty-radio> Free</label>
  <label><ty-radio value=\"pro\"></ty-radio> Pro</label>
  <label><ty-radio value=\"team\"></ty-radio> Team</label>
</ty-radio-group>"
      "html")]

   [:section.mb-10
    [:h2.text-xl.font-semibold.ty-text++.mb-3 "Horizontal orientation"]
    [:div.ty-content.rounded-lg.p-6.mb-4
     [:ty-radio-group {:label "Theme"
                       :orientation "horizontal"
                       :value "auto"}
      [:label.inline-flex.items-center.gap-2.cursor-pointer.text-sm.font-medium
       [:ty-radio {:value "light"}] [:span.whitespace-nowrap "Light"]]
      [:label.inline-flex.items-center.gap-2.cursor-pointer.text-sm.font-medium
       [:ty-radio {:value "dark"}] [:span.whitespace-nowrap "Dark"]]
      [:label.inline-flex.items-center.gap-2.cursor-pointer.text-sm.font-medium
       [:ty-radio {:value "auto"}] [:span.whitespace-nowrap "Auto"]]]]]

   [:section.mb-10
    [:h2.text-xl.font-semibold.ty-text++.mb-3 "Flavors"]
    [:div.ty-content.rounded-lg.p-6.mb-4.flex.flex-col.items-start.gap-4
     [:ty-radio-group {:label "Priority" :flavor "danger" :value "high"}
      [:label.inline-flex.items-center.gap-2.cursor-pointer.text-sm.font-medium
       [:ty-radio {:value "low"}] [:span.whitespace-nowrap "Low"]]
      [:label.inline-flex.items-center.gap-2.cursor-pointer.text-sm.font-medium
       [:ty-radio {:value "med"}] [:span.whitespace-nowrap "Medium"]]
      [:label.inline-flex.items-center.gap-2.cursor-pointer.text-sm.font-medium
       [:ty-radio {:value "high"}] [:span.whitespace-nowrap "High"]]]
     [:ty-radio-group {:label "Status" :flavor "success" :value "ok"}
      [:label.inline-flex.items-center.gap-2.cursor-pointer.text-sm.font-medium
       [:ty-radio {:value "ok"}] [:span.whitespace-nowrap "OK"]]
      [:label.inline-flex.items-center.gap-2.cursor-pointer.text-sm.font-medium
       [:ty-radio {:value "warn"}] [:span.whitespace-nowrap "Warning"]]]]]

   [:section.mb-10
    [:h2.text-xl.font-semibold.ty-text++.mb-3 "Attributes"]
    [:div.ty-content.rounded-lg.p-4.text-sm.ty-text-
     [:p.font-medium.ty-text.mb-2 [:code.ty-bg-neutral-.px-1.rounded "ty-radio-group"]]
     [:ul.list-disc.pl-6.space-y-1.mb-4
      [:li [:code.ty-bg-neutral-.px-1.rounded "value"] " — currently selected radio's value"]
      [:li [:code.ty-bg-neutral-.px-1.rounded "name"] " — form field name"]
      [:li [:code.ty-bg-neutral-.px-1.rounded "label"] " — group label (rendered above radios)"]
      [:li [:code.ty-bg-neutral-.px-1.rounded "error"] " — error message (rendered below)"]
      [:li [:code.ty-bg-neutral-.px-1.rounded "orientation"] " — vertical (default) | horizontal"]
      [:li [:code.ty-bg-neutral-.px-1.rounded "size"] " / " [:code.ty-bg-neutral-.px-1.rounded "flavor"] " — applied to all child radios"]
      [:li [:code.ty-bg-neutral-.px-1.rounded "disabled / required"] " — group-level"]]
     [:p.font-medium.ty-text.mb-2 [:code.ty-bg-neutral-.px-1.rounded "ty-radio"]]
     [:ul.list-disc.pl-6.space-y-1
      [:li [:code.ty-bg-neutral-.px-1.rounded "value"] " — required, identifies this option"]
      [:li [:code.ty-bg-neutral-.px-1.rounded "disabled"] " — disable just this option"]
      [:li "Size and flavor are normally inherited from the group; can be overridden per-radio if needed."]]]]

   [:section
    [:h2.text-xl.font-semibold.ty-text++.mb-3 "Events"]
    [:p.ty-text-.mb-3
     "The group emits "
     [:code.ty-bg-neutral-.px-1.rounded "input"] " and "
     [:code.ty-bg-neutral-.px-1.rounded "change"]
     " events when the value changes:"]
    (common/code-block
      "groupEl.addEventListener('change', (e) => {
  console.log(e.detail.value);     // selected radio's value
  console.log(e.detail.formValue); // same — what gets submitted
});"
      "javascript")]])
