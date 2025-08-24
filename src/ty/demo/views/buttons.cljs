(ns ty.demo.views.buttons)

(defn demo-row [{:keys [title description children]}]
  [:div.mb-8
   (when title
     [:h3.demo-subtitle title])
   (when description
     [:p.text-gray-600.dark:text-gray-400.mb-4 description])
   (into [:div.flex.flex-wrap.gap-4.items-center] children)]) ;; <- Use 'into' to splice children

(defn code-snippet [code]
  [:div.mt-4
   [:pre.code-block.text-xs
    [:code code]]])

(defn view []
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
               :children [[:ty-button {:flavor "important"
                                       :filled true} "Important"]
                          [:ty-button {:flavor "positive"
                                       :filled true} "Positive"]
                          [:ty-button {:flavor "negative"
                                       :filled true} "Negative"]]})
    (code-snippet "<ty-button flavor=\"positive\" filled>Filled</ty-button>")

    (demo-row {:title "Outlined"
               :description "Adds a colored border"
               :children [[:ty-button {:flavor "important"
                                       :outlined true} "Important"]
                          [:ty-button {:flavor "positive"
                                       :outlined true} "Positive"]
                          [:ty-button {:flavor "negative"
                                       :outlined true} "Negative"]]})
    (code-snippet "<ty-button flavor=\"positive\" outlined>Outlined</ty-button>")

    (demo-row {:title "Accent"
               :description "Full color background (overrides other appearances)"
               :children [[:ty-button {:flavor "important"
                                       :accent true} "Important"]
                          [:ty-button {:flavor "positive"
                                       :accent true} "Positive"]
                          [:ty-button {:flavor "negative"
                                       :accent true} "Negative"]]})
    (code-snippet "<ty-button flavor=\"positive\" accent>Accent</ty-button>")

    (demo-row {:title "Combined"
               :description "Combine filled and outlined for a unique look"
               :children [[:ty-button {:flavor "important"
                                       :filled true
                                       :outlined true} "Important"]
                          [:ty-button {:flavor "positive"
                                       :filled true
                                       :outlined true} "Positive"]
                          [:ty-button {:flavor "negative"
                                       :filled true
                                       :outlined true} "Negative"]]})
    (code-snippet "<ty-button flavor=\"positive\" filled outlined>Combined</ty-button>")]

   ;; Sizes section
   [:div.demo-section
    [:h2.demo-title "Sizes"]
    (demo-row {:description "Five size variants to fit different contexts"
               :children [[:ty-button {:flavor "important"
                                       :size "xs"} "Extra Small"]
                          [:ty-button {:flavor "important"
                                       :size "sm"} "Small"]
                          [:ty-button {:flavor "important"
                                       :size "md"} "Medium"]
                          [:ty-button {:flavor "important"
                                       :size "lg"} "Large"]
                          [:ty-button {:flavor "important"
                                       :size "xl"} "Extra Large"]]})
    (code-snippet "<ty-button flavor=\"important\" size=\"lg\">Large</ty-button>")]

   ;; With icons section
   [:div.demo-section
    [:h2.demo-title "With Icons"]
    (demo-row {:title "Start slot"
               :children [[:ty-button {:flavor "positive"
                                       :filled true}
                           [:ty-icon {:name "check"
                                      :slot "start"}]
                           "Save"]
                          [:ty-button {:flavor "negative"
                                       :outlined true}
                           [:ty-icon {:name "trash-2"
                                      :slot "start"}]
                           "Delete"]
                          [:ty-button {:flavor "important"
                                       :accent true}
                           [:ty-icon {:name "download"
                                      :slot "start"}]
                           "Download"]]})
    (code-snippet "<ty-button flavor=\"positive\" filled>\n  <ty-icon name=\"check\" slot=\"start\"></ty-icon>\n  Save\n</ty-button>")

    (demo-row {:title "End slot"
               :children [[:ty-button {:flavor "neutral"}
                           "Next"
                           [:ty-icon {:name "arrow-right"
                                      :slot "end"}]]
                          [:ty-button {:flavor "important"
                                       :outlined true}
                           "Settings"
                           [:ty-icon {:name "settings"
                                      :slot "end"}]]]})

    (demo-row {:title "Icon only"
               :children [[:ty-button {:flavor "important"
                                       :size "sm"}
                           [:ty-icon {:name "heart"}]]
                          [:ty-button {:flavor "positive"
                                       :filled true}
                           [:ty-icon {:name "plus"}]]
                          [:ty-button {:flavor "negative"
                                       :outlined true
                                       :size "lg"}
                           [:ty-icon {:name "x"}]]]})]

   ;; Pill buttons section
   [:div.demo-section
    [:h2.demo-title "Pill Buttons"]
    (demo-row {:title "Basic pill buttons"
               :description "Pill buttons have fully rounded corners"
               :children [[:ty-button {:flavor "important"
                                       :pill true} "Get Started"]
                          [:ty-button {:flavor "positive"
                                       :filled true
                                       :pill true} "Success"]
                          [:ty-button {:flavor "negative"
                                       :outlined true
                                       :pill true} "Cancel"]
                          [:ty-button {:flavor "unique"
                                       :accent true
                                       :pill true} "Special"]]})
    (code-snippet "<ty-button flavor=\"important\" pill>Get Started</ty-button>")

    (demo-row {:title "Pill buttons with icons"
               :description "Pill buttons work great with icons"
               :children [[:ty-button {:flavor "positive"
                                       :pill true
                                       :filled true}
                           [:ty-icon {:name "check"
                                      :slot "start"}]
                           "Complete"]
                          [:ty-button {:flavor "important"
                                       :pill true
                                       :accent true}
                           [:ty-icon {:name "rocket"
                                      :slot "start"}]
                           "Launch"]
                          [:ty-button {:flavor "negative"
                                       :pill true
                                       :outlined true}
                           "Dismiss"
                           [:ty-icon {:name "x"
                                      :slot "end"}]]]})

    (demo-row {:title "Icon-only pill buttons (circular)"
               :description "Icon-only pill buttons become perfectly circular"
               :children [[:ty-button {:flavor "important"
                                       :pill true
                                       :size "xs"}
                           [:ty-icon {:name "plus"}]]
                          [:ty-button {:flavor "positive"
                                       :pill true
                                       :size "sm"
                                       :filled true}
                           [:ty-icon {:name "check"}]]
                          [:ty-button {:flavor "negative"
                                       :pill true
                                       :size "md"
                                       :outlined true}
                           [:ty-icon {:name "x"}]]
                          [:ty-button {:flavor "unique"
                                       :pill true
                                       :size "lg"
                                       :accent true}
                           [:ty-icon {:name "star"}]]
                          [:ty-button {:flavor "exception"
                                       :pill true
                                       :size "xl"
                                       :filled true}
                           [:ty-icon {:name "alert-circle"}]]]})
    (code-snippet "<ty-button flavor=\"important\" pill size=\"md\">\n  <ty-icon name=\"plus\"></ty-icon>\n</ty-button>")

    (demo-row {:title "Different sizes"
               :description "Pill buttons scale beautifully across all sizes"
               :children [[:ty-button {:flavor "important"
                                       :pill true
                                       :size "xs"} "XS Pill"]
                          [:ty-button {:flavor "important"
                                       :pill true
                                       :size "sm"} "SM Pill"]
                          [:ty-button {:flavor "important"
                                       :pill true
                                       :size "md"} "MD Pill"]
                          [:ty-button {:flavor "important"
                                       :pill true
                                       :size "lg"} "LG Pill"]
                          [:ty-button {:flavor "important"
                                       :pill true
                                       :size "xl"} "XL Pill"]]})]

   ;; States section

   ;; Action buttons section
   [:div.demo-section
    [:h2.demo-title "Action Buttons"]
    (demo-row {:title "Square padding buttons"
               :description "Action buttons have equal padding on all sides, perfect for icon buttons"
               :children [[:ty-button {:flavor "important"
                                       :action true}
                           [:ty-icon {:name "settings"}]]
                          [:ty-button {:flavor "positive"
                                       :filled true
                                       :action true}
                           [:ty-icon {:name "edit"}]]
                          [:ty-button {:flavor "negative"
                                       :outlined true
                                       :action true}
                           [:ty-icon {:name "trash-2"}]]
                          [:ty-button {:flavor "unique"
                                       :accent true
                                       :action true}
                           [:ty-icon {:name "star"}]]]})
    (code-snippet "<ty-button flavor=\"important\" action>\n  <ty-icon name=\"settings\"></ty-icon>\n</ty-button>")

    (demo-row {:title "Action with text"
               :description "Action modifier works with text too, creating compact square buttons"
               :children [[:ty-button {:flavor "neutral"
                                       :action true
                                       :size "sm"} "A"]
                          [:ty-button {:flavor "important"
                                       :action true} "B"]
                          [:ty-button {:flavor "positive"
                                       :action true
                                       :filled true
                                       :size "lg"} "C"]
                          [:ty-button {:flavor "negative"
                                       :action true
                                       :accent true
                                       :size "xl"} "D"]]})

    (demo-row {:title "Action + Pill combination"
               :description "Combining action and pill creates perfect circles for icon buttons"
               :children [[:ty-button {:flavor "important"
                                       :action true
                                       :pill true
                                       :size "sm"}
                           [:ty-icon {:name "plus"}]]
                          [:ty-button {:flavor "positive"
                                       :action true
                                       :pill true
                                       :filled true}
                           [:ty-icon {:name "check"}]]
                          [:ty-button {:flavor "negative"
                                       :action true
                                       :pill true
                                       :outlined true
                                       :size "lg"}
                           [:ty-icon {:name "x"}]]
                          [:ty-button {:flavor "unique"
                                       :action true
                                       :pill true
                                       :accent true
                                       :size "xl"}
                           [:ty-icon {:name "heart"}]]]})
    (code-snippet "<ty-button flavor=\"important\" action pill>\n  <ty-icon name=\"plus\"></ty-icon>\n</ty-button>")

    (demo-row {:title "Action buttons across sizes"
               :description "Action buttons maintain square padding across all sizes"
               :children [[:ty-button {:flavor "important"
                                       :action true
                                       :size "xs"}
                           [:ty-icon {:name "settings"}]]
                          [:ty-button {:flavor "important"
                                       :action true
                                       :size "sm"}
                           [:ty-icon {:name "settings"}]]
                          [:ty-button {:flavor "important"
                                       :action true
                                       :size "md"}
                           [:ty-icon {:name "settings"}]]
                          [:ty-button {:flavor "important"
                                       :action true
                                       :size "lg"}
                           [:ty-icon {:name "settings"}]]
                          [:ty-button {:flavor "important"
                                       :action true
                                       :size "xl"}
                           [:ty-icon {:name "settings"}]]]})]
   [:div.demo-section
    [:h2.demo-title "States"]
    (demo-row {:title "Disabled"
               :children [[:ty-button {:flavor "important"
                                       :disabled true} "Disabled"]
                          [:ty-button {:flavor "positive"
                                       :filled true
                                       :disabled true} "Disabled"]
                          [:ty-button {:flavor "negative"
                                       :accent true
                                       :disabled true} "Disabled"]]})
    (code-snippet "<ty-button flavor=\"important\" disabled>Disabled</ty-button>")]])
