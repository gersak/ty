(ns ty.demo.views.buttons)

(defn demo-row [{:keys [title description children]}]
  [:div.mb-8
   (when title
     [:h3.demo-subtitle title])
   (when description
     [:p.text-gray-600.dark:text-gray-400.mb-4 description])
   (into [:div.flex.flex-wrap.gap-4.items-center] children)])  ;; <- Use 'into' to splice children

(defn code-snippet [code]
  [:div.mt-4
   [:pre.code-block.text-xs
    [:code code]]])

(defn buttons-view []
  [:div.max-w-6xl.mx-auto
   [:div.mb-8
    [:h1.text-3xl.font-bold.text-gray-900.dark:text-white.mb-2
     "Button Component"]
    [:p.text-lg.text-gray-600.dark:text-gray-400
     "A versatile button component with semantic flavors, multiple appearances, and size variants."]]

   ;; Flavors section
   [:div.demo-section
    [:h2.demo-title "Semantic Flavors"]
    (demo-row {:description "Buttons use semantic naming to convey meaning, not just color."
               :children [[:ty-button {:flavor "important"} "Important"]
                          [:ty-button {:flavor "positive"} "Positive"]
                          [:ty-button {:flavor "negative"} "Negative"]
                          [:ty-button {:flavor "exception"} "Exception"]
                          [:ty-button {:flavor "unique"} "Unique"]
                          [:ty-button {:flavor "neutral"} "Neutral"]]})
    (code-snippet "<ty-button flavor=\"positive\">Positive</ty-button>")]

   ;; Appearances section
   [:div.demo-section
    [:h2.demo-title "Appearances"]

    (demo-row {:title "Filled"
               :description "Adds a tonal background color"
               :children [[:ty-button {:flavor "important" :filled true} "Important"]
                          [:ty-button {:flavor "positive" :filled true} "Positive"]
                          [:ty-button {:flavor "negative" :filled true} "Negative"]]})
    (code-snippet "<ty-button flavor=\"positive\" filled>Filled</ty-button>")

    (demo-row {:title "Outlined"
               :description "Adds a colored border"
               :children [[:ty-button {:flavor "important" :outlined true} "Important"]
                          [:ty-button {:flavor "positive" :outlined true} "Positive"]
                          [:ty-button {:flavor "negative" :outlined true} "Negative"]]})
    (code-snippet "<ty-button flavor=\"positive\" outlined>Outlined</ty-button>")

    (demo-row {:title "Accent"
               :description "Full color background (overrides other appearances)"
               :children [[:ty-button {:flavor "important" :accent true} "Important"]
                          [:ty-button {:flavor "positive" :accent true} "Positive"]
                          [:ty-button {:flavor "negative" :accent true} "Negative"]]})
    (code-snippet "<ty-button flavor=\"positive\" accent>Accent</ty-button>")

    (demo-row {:title "Combined"
               :description "Combine filled and outlined for a unique look"
               :children [[:ty-button {:flavor "important" :filled true :outlined true} "Important"]
                          [:ty-button {:flavor "positive" :filled true :outlined true} "Positive"]
                          [:ty-button {:flavor "negative" :filled true :outlined true} "Negative"]]})
    (code-snippet "<ty-button flavor=\"positive\" filled outlined>Combined</ty-button>")]

   ;; Sizes section
   [:div.demo-section
    [:h2.demo-title "Sizes"]
    (demo-row {:description "Five size variants to fit different contexts"
               :children [[:ty-button {:flavor "important" :size "xs"} "Extra Small"]
                          [:ty-button {:flavor "important" :size "sm"} "Small"]
                          [:ty-button {:flavor "important" :size "md"} "Medium"]
                          [:ty-button {:flavor "important" :size "lg"} "Large"]
                          [:ty-button {:flavor "important" :size "xl"} "Extra Large"]]})
    (code-snippet "<ty-button flavor=\"important\" size=\"lg\">Large</ty-button>")]

   ;; With icons section
   [:div.demo-section
    [:h2.demo-title "With Icons"]
    (demo-row {:title "Start slot"
               :children [[:ty-button {:flavor "positive" :filled true}
                           [:ty-icon {:name "check" :slot "start"}]
                           "Save"]
                          [:ty-button {:flavor "negative" :outlined true}
                           [:ty-icon {:name "trash-2" :slot "start"}]
                           "Delete"]
                          [:ty-button {:flavor "important" :accent true}
                           [:ty-icon {:name "download" :slot "start"}]
                           "Download"]]})
    (code-snippet "<ty-button flavor=\"positive\" filled>\n  <ty-icon name=\"check\" slot=\"start\"></ty-icon>\n  Save\n</ty-button>")

    (demo-row {:title "End slot"
               :children [[:ty-button {:flavor "neutral"}
                           "Next"
                           [:ty-icon {:name "arrow-right" :slot "end"}]]
                          [:ty-button {:flavor "important" :outlined true}
                           "Settings"
                           [:ty-icon {:name "settings" :slot "end"}]]]})

    (demo-row {:title "Icon only"
               :children [[:ty-button {:flavor "important" :size "sm"}
                           [:ty-icon {:name "heart"}]]
                          [:ty-button {:flavor "positive" :filled true}
                           [:ty-icon {:name "plus"}]]
                          [:ty-button {:flavor "negative" :outlined true :size "lg"}
                           [:ty-icon {:name "x"}]]]})]

   ;; States section
   [:div.demo-section
    [:h2.demo-title "States"]
    (demo-row {:title "Disabled"
               :children [[:ty-button {:flavor "important" :disabled true} "Disabled"]
                          [:ty-button {:flavor "positive" :filled true :disabled true} "Disabled"]
                          [:ty-button {:flavor "negative" :accent true :disabled true} "Disabled"]]})
    (code-snippet "<ty-button flavor=\"important\" disabled>Disabled</ty-button>")]])