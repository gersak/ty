(ns ty.demo.views.popups
  "Demo view for popup components (tooltips, dropdowns, popovers)")

(defn demo-row [{:keys [title children]}]
  [:div.mb-6
   [:h3.text-sm.font-semibold.text-gray-700.dark:text-gray-300.mb-3 title]
   (into [:div.flex.flex-wrap.gap-4] children)])

(defn code-snippet [code]
  [:pre.bg-gray-900.text-gray-100.p-4.rounded-md.overflow-x-auto.font-mono.text-sm
   [:code code]])

(defn tooltip-demos []
  [:div
   ;; Basic positions
   (demo-row
    {:title "Basic Positions"
     :children [[:ty-button
                 "Top"
                 [:ty-tooltip {:content "Top tooltip"
                               :position "top"}]]
                [:ty-button
                 "Bottom"
                 [:ty-tooltip {:content "Bottom tooltip"
                               :position "bottom"}]]
                [:ty-button
                 "Left"
                 [:ty-tooltip {:content "Left tooltip"
                               :position "left"}]]
                [:ty-button
                 "Right"
                 [:ty-tooltip {:content "Right tooltip"
                               :position "right"}]]]})

   ;; Aligned positions
   (demo-row
    {:title "Aligned Positions"
     :children [[:ty-button
                 "Top Start"
                 [:ty-tooltip {:content "Top start tooltip"
                               :position "top-start"}]]
                [:ty-button
                 "Top End"
                 [:ty-tooltip {:content "Top end tooltip"
                               :position "top-end"}]]
                [:ty-button
                 "Bottom Start"
                 [:ty-tooltip {:content "Bottom start tooltip"
                               :position "bottom-start"}]]
                [:ty-button
                 "Bottom End"
                 [:ty-tooltip {:content "Bottom end tooltip"
                               :position "bottom-end"}]]]})

   ;; Delays
   (demo-row
    {:title "Hover Delays"
     :children [[:ty-button
                 "Quick (100ms)"
                 [:ty-tooltip {:content "Quick tooltip"
                               :delay "100"}]]
                [:ty-button
                 "Default (500ms)"
                 [:ty-tooltip {:content "Default tooltip"}]]
                [:ty-button
                 "Slow (1000ms)"
                 [:ty-tooltip {:content "Slow tooltip"
                               :delay "1000"}]]]})

   ;; With icons
   (demo-row
    {:title "With Icons"
     :children [[:ty-button
                 [:ty-icon {:name "save"
                            :slot "start"}]
                 "Save"
                 [:ty-tooltip {:content "Save your changes"}]]
                [:ty-button {:flavor "negative"}
                 [:ty-icon {:name "trash-2"
                            :slot "start"}]
                 "Delete"
                 [:ty-tooltip {:content "This action cannot be undone"}]]
                [:ty-button {:flavor "important"
                             :accent true}
                 [:ty-icon {:name "download"
                            :slot "start"}]
                 "Export"
                 [:ty-tooltip {:content "Download as PDF"}]]]})

   ;; Long content
   (demo-row
    {:title "Content Wrapping"
     :children [[:ty-button
                 "Long Tooltip"
                 [:ty-tooltip {:content "This is a very long tooltip that should wrap properly and not exceed the maximum width constraint set by the component"}]]
                [:ty-button
                 "Short"
                 [:ty-tooltip {:content "Short"}]]]})])

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
                  [:ty-tooltip {:content "Should flip to right"
                                :position "left"}]]
                 [:ty-button
                  "Right Edge"
                  [:ty-tooltip {:content "Should flip to left"
                                :position "right"}]]]]})

   (demo-row
    {:title "Scrollable Container"
     :children [[:div.h-32.overflow-auto.border.border-gray-300.dark:border-gray-600.rounded.p-4
                 [:div.h-64
                  [:ty-button
                   "Scroll me"
                   [:ty-tooltip {:content "Tooltip in scrollable container"}]]
                  [:div.mt-32
                   [:ty-button
                    "Bottom button"
                    [:ty-tooltip {:content "Another tooltip down here"}]]]]]]})])

(defn basic-popup-test []
  [:div
   (demo-row
    {:title "Basic Popup Test (Always Visible)"
     :children [[:div
                 [:p.mb-4 "Testing minimal popup - fixed position, always visible:"]
                 [:ty-popup
                  [:div
                   [:h4.font-bold.mb-2 "Hello from Popup!"]
                   [:p "This popup is always visible and centered on screen."]
                   [:p.text-sm.text-gray-600.mt-2 "Next we'll add positioning..."]]]]]})])

(defn popups-view []
  [:div
   [:h1.text-3xl.font-bold.text-gray-900.dark:text-white.mb-2 "Popup Components"]
   [:p.text-gray-600.dark:text-gray-400.mb-8
    "Floating UI components for tooltips, dropdowns, and popovers."]

   ;; Basic popup test section
   [:section.mb-12
    [:h2.text-2xl.font-semibold.text-gray-800.dark:text-gray-200.mb-6 "Basic Popup Test"]
    [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6.mb-6
     (basic-popup-test)]]

   ;; Tooltips section
   [:section.mb-12
    [:h2.text-2xl.font-semibold.text-gray-800.dark:text-gray-200.mb-6 "Tooltips"]
    [:p.text-gray-600.dark:text-gray-400.mb-6
     "Tooltips provide helpful information on hover or focus."]

    [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6.mb-6
     (tooltip-demos)]

    [:div.mb-6
     [:h3.text-lg.font-semibold.mb-2 "Usage"]
     (code-snippet
      "<button>
  Save Changes
  <ty-tooltip content=\"Save your work\"></ty-tooltip>
</button>

<!-- With ty-button -->
<ty-button>
  Delete
  <ty-tooltip content=\"This cannot be undone\"></ty-tooltip>
</ty-button>

<!-- With icons -->
<ty-button>
  <ty-icon name=\"download\" slot=\"start\"></ty-icon>
  Export
  <ty-tooltip content=\"Download as PDF\"></ty-tooltip>
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
