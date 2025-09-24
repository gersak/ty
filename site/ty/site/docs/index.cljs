(ns ty.site.docs.index
  "Documentation index page"
  (:require [clojure.string :as str]
            [ty.router :as router]
            [ty.site.docs.common :refer [code-block doc-section]]))

(def component-list
  ["button"
   "calendar"
   "dropdown"
   "icon"
   "input"
   "modal"
   "multiselect"
   "popup"
   "tag"
   "textarea"
   "tooltip"])

(defn view []
  [:div.max-w-6xl.mx-auto.p-6
   [:h1.text-4xl.font-bold.ty-text.mb-4 "Ty Component Documentation"]
   [:p.text-lg.ty-text-.mb-8
    "Complete documentation for the ty web components library. Select a component from the sidebar to get started."]

   ;; Quick start section
   (doc-section "Quick Start"
                [:div.ty-elevated.rounded-lg.p-6
                 [:h3.text-lg.font-medium.ty-text.mb-3 "Installation"]
                 (code-block "<!-- CDN -->
<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.css\">
<script src=\"https://cdn.jsdelivr.net/npm/@gersak/ty/dist/ty.js\"></script>

<!-- NPM -->
npm install @gersak/ty")])

   ;; Component categories overview
   (doc-section "Available Components"
                [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-3.gap-4
                 (for [component component-list]
                   [:div.ty-elevated.rounded.p-4.hover:ty-floating.transition-all.cursor-pointer
                    {:on {:click #(router/navigate! (keyword "ty.site.docs" component))}}
                    [:div.flex.items-center.gap-3
                     [:ty-icon.ty-text-primary
                      {:name (case component
                               "button" "square"
                               "calendar" "calendar"
                               "dropdown" "chevron-down"
                               "icon" "image"
                               "input" "type"
                               "modal" "square-stack"
                               "multiselect" "list"
                               "popup" "message-square"
                               "tag" "tag"
                               "textarea" "align-left"
                               "tooltip" "info"
                               "square")
                       :size "md"}]
                     [:div
                      [:h3.font-medium.ty-text (str "ty-" component)]
                      [:p.text-sm.ty-text-- (case component
                                              "button" "Interactive button component"
                                              "calendar" "Date picker and calendar display"
                                              "dropdown" "Rich dropdown selector"
                                              "icon" "SVG icon component"
                                              "input" "Enhanced text input"
                                              "modal" "Modal dialog component"
                                              "multiselect" "Multiple selection input"
                                              "popup" "Contextual popup"
                                              "tag" "Tag and chip display"
                                              "textarea" "Multi-line text input"
                                              "tooltip" "Hover tooltips"
                                              "Component description")]]]])])])
