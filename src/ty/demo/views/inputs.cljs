(ns ty.demo.views.inputs
  "Demonstrates input components with layout integration"
  (:require
   [ty.layout :as layout]))

(defn basic-input-demos []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "Basic Input Types"]
   [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
    [:div.space-y-4
     [:h4.font-medium "Simple Inputs"]
     [:div.space-y-3
      [:ty-input {:type "text"
                  :placeholder "Enter your name"}]
      [:ty-input {:type "email"
                  :label "Email Address"
                  :placeholder "you@example.com"}]
      [:ty-input {:type "password"
                  :label "Password"
                  :required true
                  :placeholder "Enter password"}]
      [:ty-input {:type "text"
                  :label "Disabled Field"
                  :disabled true
                  :value "Cannot edit this"}]]]

    [:div.space-y-4
     [:h4.font-medium "Input Types"]
     [:div.space-y-3
      [:ty-input {:type "text"
                  :label "Text"
                  :placeholder "Text input"}]
      [:ty-input {:type "email"
                  :label "Email"
                  :placeholder "email@example.com"}]
      [:ty-input {:type "number"
                  :label "Number"
                  :placeholder "123"}]
      [:ty-input {:type "search"
                  :label "Search"
                  :placeholder "Search..."}]]]]])

(defn size-variants-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "Size Variants"]
   [:div.space-y-3
    [:ty-input {:size "xs"
                :label "Extra Small"
                :placeholder "size=xs"}]
    [:ty-input {:size "sm"
                :label "Small"
                :placeholder "size=sm"}]
    [:ty-input {:size "md"
                :label "Medium (default)"
                :placeholder "size=md"}]
    [:ty-input {:size "lg"
                :label "Large"
                :placeholder "size=lg"}]
    [:ty-input {:size "xl"
                :label "Extra Large"
                :placeholder "size=xl"}]]])

(defn flavor-variants-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "Semantic Flavors"]
   [:div.space-y-3
    [:ty-input {:flavor "neutral"
                :label "Neutral (default)"
                :placeholder "Default styling"}]
    [:ty-input {:flavor "important"
                :label "Important"
                :placeholder "Important field"}]
    [:ty-input {:flavor "positive"
                :label "Valid Input"
                :placeholder "This looks good!"}]
    [:ty-input {:flavor "negative"
                :label "Error State"
                :placeholder "Something went wrong"}]
    [:ty-input {:flavor "exception"
                :label "Warning State"
                :placeholder "Be careful here"}]
    [:ty-input {:flavor "unique"
                :label "Special Field"
                :placeholder "Something unique"}]]])

(defn container-aware-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "Container-Aware Inputs"]
   [:p.text-sm.text-gray-600 "These inputs adapt to their container size using the layout context."]

   ;; Resizable container demo
   [:ty-resize-observer#input-container.bg-blue-50.dark:bg-blue-900.border-2.border-blue-200.dark:border-blue-700.rounded-lg.p-4.resize.overflow-auto
    {:style {:min-width "200px"
             :min-height "200px"
             :width "400px"
             :height "250px"}}
    (layout/with-resize-observer "input-container"
      [:div.space-y-4
       [:div.text-center
        [:h4.font-semibold "Resizable Container"]
        [:p.text-sm.text-gray-600 "Container: " (layout/container-width) "×" (layout/container-height) "px"]
        [:p.text-xs.text-gray-500 "Breakpoint: " (name (layout/container-breakpoint))]]

       [:div.space-y-3
        [:ty-input {:label "Responsive Input"
                    :placeholder "I know my container size"
                    :size (cond
                            (layout/breakpoint>= :lg) "lg"
                            (layout/breakpoint>= :md) "md"
                            :else "sm")}]
        [:ty-input {:label "Dynamic Sizing"
                    :placeholder (str "Container is " (name (layout/container-breakpoint)))
                    :flavor (cond
                              (layout/breakpoint>= :lg) "positive"
                              (layout/breakpoint>= :md) "important"
                              :else "exception")}]]])]])

(defn form-layout-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "Professional Form Layouts"]
   [:p.text-sm.text-gray-600 "Clean, elegant forms with proper spacing and visual hierarchy."]

   [:div.grid.grid-cols-1.lg:grid-cols-2.gap-8
    ;; Contact form with refined spacing like toddler
    [:div.bg-white.dark:bg-gray-800.p-6.rounded-lg.shadow-md
     [:h4.font-medium.mb-6 "Contact Information"]
     [:div.space-y-5
      [:div.grid.grid-cols-1.sm:grid-cols-2.gap-4
       [:ty-input {:type "text" :label "First Name" :required true :placeholder "John"}]
       [:ty-input {:type "text" :label "Last Name" :required true :placeholder "Doe"}]]
      [:ty-input {:type "text" :label "Address" :placeholder "123 Main Street"}]
      [:ty-input {:type "text" :label "City" :placeholder "New York"}]
      [:ty-input {:type "text" :label "Country" :placeholder "United States"}]
      [:ty-input {:type "date" :label "Date of Birth" :placeholder "mm/dd/yyyy"}]]]

    ;; Login form with elegant styling
    [:div.bg-white.dark:bg-gray-800.p-6.rounded-lg.shadow-md
     [:h4.font-medium.mb-6 "Account Access"]
     [:div.space-y-5
      [:ty-input {:type "email" :label "Email Address" :required true :placeholder "your@email.com"}]
      [:ty-input {:type "password" :label "Password" :required true :placeholder "Enter password"}]
      [:ty-input {:type "password" :label "Confirm Password" :required true :placeholder "Confirm password"}]]]]])

(defn event-demo []
  [:div.space-y-6
   [:h3.text-lg.font-semibold "Event Handling"]
   [:p.text-sm.text-gray-600 "Check the browser console to see custom events fired by inputs."]
   [:div.space-y-3
    [:ty-input {:label "Input Events"
                :placeholder "Type here and check console"
                :on {:input #(js/console.log "Input event:" (.-detail %))
                     :change #(js/console.log "Change event:" (.-detail %))}}]
    [:ty-input {:label "Form Validation Demo"
                :type "email"
                :required true
                :placeholder "Enter a valid email"
                :flavor "important"}]]])

(defn inputs-view []
  (layout/with-window
    [:div.p-8.max-w-6xl.mx-auto.space-y-8
     [:div
      [:h1.text-3xl.font-bold.mb-4 "Input Components"]
      [:p.text-gray-600
       "Form input components with elegant styling inspired by toddler's refined design."]
      [:p.text-sm.text-gray-500.mt-2
       "Features subtle borders, proper spacing, and polished visual hierarchy."]]

     ;; Highlight the improved form layouts first
     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (form-layout-demo)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (basic-input-demos)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (size-variants-demo)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (flavor-variants-demo)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (container-aware-demo)]

     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
      (event-demo)]]))
