(ns ty.demo.views.tags
  (:require [ty.demo.state :as state]))

(defn tag-event-handler [event-type]
  (fn [event]
    (let [detail (.-detail event)
          target (.-target detail)]
      (js/console.log (str "Tag " event-type ":") target)
      (when (= event-type "dismiss")
        ;; For demo purposes, add to removed tags list
        (swap! state/state update :removed-tags (fnil conj #{}) (.-textContent target))))))

(defn tags-view []
  [:div.max-w-6xl.mx-auto
   [:div.mb-8
    [:h1.text-3xl.font-bold.text-gray-900.dark:text-white.mb-2
     "Tag Component"]
    [:p.text-lg.text-gray-600.dark:text-gray-400
     "Flexible tag component with three slots and semantic flavors."]]

   ;; Basic Examples
   [:div.demo-section
    [:h2.demo-title "Basic Tags"]
    [:div.flex.flex-wrap.gap-2.mb-4
     [:ty-tag "JavaScript"]
     [:ty-tag "TypeScript"]
     [:ty-tag "React"]
     [:ty-tag "Vue.js"]
     [:ty-tag "Svelte"]
     [:ty-tag {:not-pill true} "Rectangular"]
     [:ty-tag {:not-pill true
               :flavor "positive"} "Rectangle"]]]

   ;; Test if tags work at all
   [:div.demo-section
    [:h2.demo-title "Simple Test"]
    [:div.flex.flex-wrap.gap-2.mb-4
     [:ty-tag {:flavor "positive"} "Working?"]
     [:ty-tag {:flavor "negative"} "Test"]
     [:ty-tag {:not-pill true
               :flavor "important"} "Rectangle Test"]]]

   ;; Size Variants
   [:div.demo-section
    [:h2.demo-title "Size Variants"]
    [:div.space-y-4
     [:div
      [:h3.text-sm.font-medium.mb-2 "Extra Small (xs)"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:size "xs"} "xs"]
       [:ty-tag {:size "xs"
                 :flavor "positive"} "Success"]
       [:ty-tag {:size "xs"
                 :flavor "negative"} "Error"]]]

     [:div
      [:h3.text-sm.font-medium.mb-2 "Small (sm)"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:size "sm"} "sm"]
       [:ty-tag {:size "sm"
                 :flavor "important"} "Important"]
       [:ty-tag {:size "sm"
                 :flavor "exception"} "Warning"]]]

     [:div
      [:h3.text-sm.font-medium.mb-2 "Medium (md) - Default"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag "md default"]
       [:ty-tag {:flavor "unique"} "Unique"]
       [:ty-tag {:flavor "neutral"} "Neutral"]]]

     [:div
      [:h3.text-sm.font-medium.mb-2 "Large (lg)"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:size "lg"} "lg"]
       [:ty-tag {:size "lg"
                 :flavor "positive"} "Large Success"]
       [:ty-tag {:size "lg"
                 :flavor "important"} "Large Important"]]]

     [:div
      [:h3.text-sm.font-medium.mb-2 "Extra Large (xl)"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:size "xl"} "xl"]
       [:ty-tag {:size "xl"
                 :flavor "exception"} "Extra Large"]]]]]

   ;; Shape Variants
   [:div.demo-section
    [:h2.demo-title "Shape Variants"]
    [:div.space-y-4
     [:div
      [:h3.text-sm.font-medium.mb-2 "Pill Shape (default)"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag "Default Pill"]
       [:ty-tag {:flavor "positive"} "Success"]
       [:ty-tag {:flavor "important"} "Important"]]]

     [:div
      [:h3.text-sm.font-medium.mb-2 "Rectangular Shape"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:not-pill true} "Rectangular"]
       [:ty-tag {:not-pill true
                 :flavor "positive"} "Success"]
       [:ty-tag {:not-pill true
                 :flavor "important"} "Important"]]]]]

   ;; Interactive Examples
   [:div.demo-section
    [:h2.demo-title "Interactive Examples"]
    [:div.space-y-4
     [:div
      [:h3.text-sm.font-medium.mb-2 "Clickable Tags"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:clickable true
                 :flavor "neutral"
                 :on {:ty-tag-click (tag-event-handler "click")}}
        "Click Me"]
       [:ty-tag {:not-pill true
                 :clickable true
                 :flavor "positive"
                 :on {:ty-tag-click (tag-event-handler "click")}}
        "Rectangle Click"]]]

     [:div
      [:h3.text-sm.font-medium.mb-2 "Dismissible Tags"]
      [:div.flex.flex-wrap.gap-2
       [:ty-tag {:dismissible true
                 :flavor "negative"
                 :on {:ty-tag-dismiss (tag-event-handler "dismiss")}}
        "Dismiss Me"]
       [:ty-tag {:not-pill true
                 :dismissible true
                 :flavor "exception"
                 :on {:ty-tag-dismiss (tag-event-handler "dismiss")}}
        "Rectangle Dismiss"]]]]]])
