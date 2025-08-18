(ns ty.demo.views.popups
  "Demo view for popup components (tooltips, dropdowns, popovers)"
  (:require [ty.demo.state :as state]))

(defn demo-row [{:keys [title children]}]
  [:div.mb-6
   [:h3.text-sm.font-semibold.text-gray-700.dark:text-gray-300.mb-3 title]
   (into [:div.flex.flex-wrap.gap-4] children)])

(defn code-snippet [code]
  [:pre.bg-gray-900.text-gray-100.p-4.rounded-md.overflow-x-auto.font-mono.text-sm
   [:code code]])

(defn toggle-popup! [popup-id]
  (swap! state/state update-in [:popup-states popup-id] not))

(defn popup-open? [popup-id]
  (get-in @state/state [:popup-states popup-id] false))

(defn tooltip-demos []
  [:div
   ;; Basic positions
   (demo-row
     {:title "Basic Positions"
      :children [[:ty-button
                  "Top"
                  [:ty-tooltip {:placement "top"} "Top tooltip"]]
                 [:ty-button
                  "Bottom"
                  [:ty-tooltip {:placement "bottom"} "Bottom tooltip"]]
                 [:ty-button
                  "Left"
                  [:ty-tooltip {:placement "left"} "Left tooltip"]]
                 [:ty-button
                  "Right"
                  [:ty-tooltip {:placement "right"} "Right tooltip"]]]})

   ;; Variants
   (demo-row
     {:title "Tooltip Variants"
      :children [[:ty-button
                  "Dark (default)"
                  [:ty-tooltip "Dark tooltip"]]
                 [:ty-button
                  "Light"
                  [:ty-tooltip {:variant "light"} "Light tooltip"]]
                 [:ty-button {:flavor "positive"}
                  "Positive"
                  [:ty-tooltip {:variant "positive"} "Positive tooltip"]]
                 [:ty-button {:flavor "negative"}
                  "Negative"
                  [:ty-tooltip {:variant "negative"} "Negative tooltip"]]
                 [:ty-button {:flavor "important"}
                  "Important"
                  [:ty-tooltip {:variant "important"} "Important tooltip"]]]})

   ;; Rich content
   (demo-row
     {:title "Rich Content Tooltips"
      :children [[:ty-button
                  "Rich Text"
                  [:ty-tooltip
                   [:div
                    [:strong "Bold text"]
                    " and "
                    [:em "italic text"]]]]
                 [:ty-button
                  "Multi-line"
                  [:ty-tooltip
                   [:div
                    [:div "Line 1: Important info"]
                    [:div "Line 2: More details"]
                    [:div.text-xs.opacity-75 "Line 3: Additional note"]]]]
                 [:ty-button {:flavor "important"}
                  "With Icon"
                  [:ty-tooltip
                   [:div.flex.items-center.gap-2
                    [:ty-icon {:name "info-circle"
                               :size "sm"}]
                    [:span "Information tooltip with icon"]]]]]})

   ;; Delays
   (demo-row
     {:title "Hover Delays"
      :children [[:ty-button
                  "Quick (100ms)"
                  [:ty-tooltip {:delay "100"} "Quick tooltip"]]
                 [:ty-button
                  "Default (600ms)"
                  [:ty-tooltip "Default tooltip"]]
                 [:ty-button
                  "Slow (1000ms)"
                  [:ty-tooltip {:delay "1000"} "Slow tooltip"]]]})

   ;; With icons in buttons
   (demo-row
     {:title "Buttons with Icons and Tooltips"
      :children [[:ty-button
                  [:ty-icon {:name "save"
                             :slot "start"}]
                  "Save"
                  [:ty-tooltip "Save your changes"]]
                 [:ty-button {:flavor "negative"}
                  [:ty-icon {:name "trash-2"
                             :slot "start"}]
                  "Delete"
                  [:ty-tooltip {:variant "negative"}
                   [:div
                    [:strong "Warning!"]
                    [:br]
                    "This action cannot be undone"]]]
                 [:ty-button {:flavor "important"
                              :accent true}
                  [:ty-icon {:name "download"
                             :slot "start"}]
                  "Export"
                  [:ty-tooltip "Download as PDF"]]]})

   ;; Long content
   (demo-row
     {:title "Content Wrapping"
      :children [[:ty-button
                  "Long Tooltip"
                  [:ty-tooltip
                   "This is a very long tooltip that should wrap properly and not exceed the maximum width constraint set by the component"]]
                 [:ty-button
                  "Short"
                  [:ty-tooltip "Short"]]]})

   ;; Disabled state
   (demo-row
     {:title "Disabled State"
      :children [[:ty-button
                  "Enabled"
                  [:ty-tooltip "This tooltip works"]]
                 [:ty-button
                  "Disabled"
                  [:ty-tooltip {:disabled true} "You won't see this"]]]})])

(defn dropdown-demos []
  [:div
   [:div.text-center.text-gray-500.dark:text-gray-400.py-8
    [:ty-icon {:name "package"
               :size "xl"}]
    [:p.mt-2 "Dropdown component coming soon"]]])

(defn popover-demos []
  [:div
   [:div.text-center.text-gray-500.dark:text-gray-400.py-8
    [:ty-icon {:name "message-square"
               :size "xl"}]
    [:p.mt-2 "Popover component coming soon"]]])

(defn edge-case-demos []
  [:div
   (demo-row
     {:title "Viewport Edge Detection"
      :children [[:div.flex.justify-between.w-full
                  [:ty-button
                   "Left Edge"
                   [:ty-tooltip {:placement "left"} "Should flip to right"]]
                  [:ty-button
                   "Right Edge"
                   [:ty-tooltip {:placement "right"} "Should flip to left"]]]]})

   (demo-row
     {:title "Different Element Types"
      :children [[:span.inline-flex.items-center.px-3.py-1.rounded-full.text-sm.bg-gray-100.dark:bg-gray-700
                  "Badge"
                  [:ty-tooltip "Works on any element"]]
                 [:a.text-blue-600.dark:text-blue-400.underline {:href "#"}
                  "Link with tooltip"
                  [:ty-tooltip "Links can have tooltips too"]]
                 [:div.w-12.h-12.bg-gradient-to-br.from-purple-500.to-pink-500.rounded-lg.flex.items-center.justify-center.text-white
                  [:ty-icon {:name "star"}]
                  [:ty-tooltip "Even custom elements!"]]]})

   (demo-row
     {:title "Complex Tooltip Content"
      :children [[:ty-button
                  "Tooltip with List"
                  [:ty-tooltip
                   [:div
                    [:div.font-semibold.mb-1 "Keyboard shortcuts:"]
                    [:ul.text-xs.space-y-1
                     [:li "⌘+S - Save"]
                     [:li "⌘+O - Open"]
                     [:li "⌘+N - New"]]]]]
                 [:ty-button
                  "Styled Content"
                  [:ty-tooltip
                   [:div.text-center
                    [:div.text-lg "🎉"]
                    [:div.font-bold "Success!"]
                    [:div.text-xs.opacity-75 "Your action was completed"]]]]]})])

(defn basic-popup-test []
  [:div
   (demo-row
     {:title "Click to Toggle Popups"
      :children [[:div.relative
                  [:ty-popup
                   {:placement "right"
                    :open (popup-open? :demo-popup)
                    :offset "16"}
                   [:ty-button {:slot "anchor"
                                :on {:click #(toggle-popup! :demo-popup)}}
                    "Toggle Popup"]
                   [:div.p-4.bg-white.dark:bg-gray-800.rounded-lg.shadow-lg
                    [:h4.font-bold.mb-2 "Popup Content"]
                    [:p "Click the button to toggle this popup."]
                    [:ty-button.mt-2
                     {:size "sm"
                      :on {:click #(toggle-popup! :demo-popup)}}
                     "Close"]]]]]})

   (demo-row
     {:title "Popup Placement Options"
      :children [[:div.grid.grid-cols-2.gap-4.w-full
                  [:ty-popup {:open (popup-open? :popup-top)
                              :placement "top"}
                   [:ty-button {:slot "anchor"
                                :on {:click #(toggle-popup! :popup-top)}}
                    "Top"]
                   [:div.p-2.bg-blue-100.dark:bg-blue-900.rounded "Click to toggle"]]

                  [:ty-popup
                   {:open (popup-open? :popup-bottom)
                    :placement "bottom"}
                   [:ty-button {:slot "anchor"
                                :on {:click #(toggle-popup! :popup-bottom)}}
                    "Bottom"]
                   [:div.p-2.bg-green-100.dark:bg-green-900.rounded "Click to toggle"]]

                  [:ty-popup {:open (popup-open? :popup-left)
                              :placement "left"}
                   [:ty-button {:slot "anchor"
                                :on {:click #(toggle-popup! :popup-left)}}
                    "Left"]
                   [:div.p-2.bg-yellow-100.dark:bg-yellow-900.rounded "Click to toggle"]]

                  [:ty-popup
                   {:open (popup-open? :popup-right)
                    :placement "right"}
                   [:ty-button {:slot "anchor"
                                :on {:click #(toggle-popup! :popup-right)}}
                    "Right"]
                   [:div.p-2.bg-red-100.dark:bg-red-900.rounded "Click to toggle"]]]]})

   (demo-row
     {:title "Click Outside to Close"
      :children [[:div
                  [:ty-popup (when (popup-open? :click-outside-demo)
                               {:open true})
                   {:placement "bottom"
                    :offset "8"}
                   [:ty-button {:slot "anchor"
                                :on {:click #(toggle-popup! :click-outside-demo)}}
                    "Open Popup"]
                   [:div.p-4.bg-white.dark:bg-gray-800.rounded-lg.shadow-lg.border.border-gray-200.dark:border-gray-700
                    [:h4.font-bold.mb-2 "Click Outside Demo"]
                    [:p.text-sm.text-gray-600.dark:text-gray-400
                     "This popup will close when you click outside of it."]
                    [:p.text-xs.text-gray-500.dark:text-gray-500.mt-2
                     "(Feature coming with ty-dropdown component)"]]]]]})

   (demo-row
     {:title "Multiple Popups"
      :children [[:div.flex.gap-4
                  [:ty-popup
                   {:open (popup-open? :multi-1)
                    :placement "top"}
                   [:ty-button
                    {:slot "anchor"
                     :flavor "positive"
                     :on {:click #(toggle-popup! :multi-1)}}
                    [:div "Popup 1"]]
                   [:div.p-3.bg-green-100.dark:bg-green-900.rounded
                    "First popup"]]

                  [:ty-popup {:open (popup-open? :multi-2)
                              :placement "top"}
                   [:ty-button {:slot "anchor"
                                :flavor "important"
                                :on {:click #(toggle-popup! :multi-2)}}
                    "Popup 2"]
                   [:div.p-3.bg-blue-100.dark:bg-blue-900.rounded
                    "Second popup"]]

                  [:ty-popup (when (popup-open? :multi-3)
                               {:open true})
                   {:placement "top"}
                   [:ty-button {:slot "anchor"
                                :flavor "negative"
                                :on {:click #(toggle-popup! :multi-3)}}
                    "Popup 3"]
                   [:div.p-3.bg-red-100.dark:bg-red-900.rounded
                    "Third popup"]]]]})

   (demo-row
     {:title "Interactive Popup Content"
      :children [[:div
                  [:ty-popup {:open (popup-open? :interactive-demo)
                              :placement "right"
                              :offset "16"}
                   [:ty-button {:slot "anchor"
                                :on {:click #(toggle-popup! :interactive-demo)}}
                    "Open Form"]
                   [:div.p-4.bg-white.dark:bg-gray-800.rounded-lg.shadow-lg.border.border-gray-200.dark:border-gray-700.w-64
                    [:h4.font-bold.mb-3 "Quick Form"]
                    [:div.space-y-3
                     [:input.w-full.px-3.py-2.border.border-gray-300.dark:border-gray-600.rounded.bg-white.dark:bg-gray-700
                      {:type "text"
                       :placeholder "Enter name..."}]
                     [:textarea.w-full.px-3.py-2.border.border-gray-300.dark:border-gray-600.rounded.bg-white.dark:bg-gray-700
                      {:rows 3
                       :placeholder "Enter message..."}]
                     [:div.flex.gap-2
                      [:ty-button
                       {:size "sm"
                        :flavor "positive"
                        :on {:click (fn []
                                      (js/alert "Form submitted!")
                                      (toggle-popup! :interactive-demo))}}
                       "Submit"]
                      [:ty-button
                       {:size "sm"
                        :on {:click #(toggle-popup! :interactive-demo)}}
                       "Cancel"]]]]]]]})])

(defn popups-view []
  [:div
   [:h1.text-3xl.font-bold.text-gray-900.dark:text-white.mb-2 "Popup Components"]
   [:p.text-gray-600.dark:text-gray-400.mb-8
    "Floating UI components for tooltips, dropdowns, and popovers."]

   ;; Basic popup test section
   [:section.mb-12
    [:h2.text-2xl.font-semibold.text-gray-800.dark:text-gray-200.mb-6 "ty-popup Primitive"]
    [:p.text-gray-600.dark:text-gray-400.mb-6
     "The base positioning primitive that other components build upon. Click buttons to toggle popups."]
    [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6.mb-6
     (basic-popup-test)]]

   ;; Tooltips section
   [:section.mb-12
    [:h2.text-2xl.font-semibold.text-gray-800.dark:text-gray-200.mb-6 "Tooltips"]
    [:p.text-gray-600.dark:text-gray-400.mb-6
     "Tooltips provide helpful information on hover or focus. They support both simple text and rich HTML content."]

    [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6.mb-6
     (tooltip-demos)]

    [:div.mb-6
     [:h3.text-lg.font-semibold.mb-2 "Usage"]
     (code-snippet
       "<!-- Simple text tooltip -->
<button>
  Save Changes
  <ty-tooltip>Save your work</ty-tooltip>
</button>

<!-- With placement and variant -->
<ty-button>
  Delete
  <ty-tooltip placement=\"top\" variant=\"negative\">
    This cannot be undone
  </ty-tooltip>
</ty-button>

<!-- Rich content tooltip -->
<ty-button>
  Info
  <ty-tooltip>
    <strong>Important:</strong>
    <br />
    Read the documentation for details
  </ty-tooltip>
</ty-button>

<!-- With custom delay -->
<ty-button>
  Quick Info
  <ty-tooltip delay=\"100\">Shows quickly</ty-tooltip>
</ty-button>")]]

   ;; Dropdowns section
   [:section.mb-12
    [:h2.text-2xl.font-semibold.text-gray-800.dark:text-gray-200.mb-6 "Dropdowns"]
    [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
     (dropdown-demos)]]

   ;; Popovers section
   [:section.mb-12
    [:h2.text-2xl.font-semibold.text-gray-800.dark:text-gray-200.mb-6 "Popovers"]
    [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
     (popover-demos)]]

   ;; Edge cases section
   [:section
    [:h2.text-2xl.font-semibold.text-gray-800.dark:text-gray-200.mb-6 "Edge Cases & Testing"]
    [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
     (edge-case-demos)]]])
