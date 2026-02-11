(ns ty.site.docs
  "Documentation system for ty components - provides data and views"
  (:require
   [clojure.string :as str]
   [ty.router :as router]
   [ty.site.docs.button :as button-docs]
   [ty.site.docs.calendar :as calendar-docs]
   [ty.site.docs.calendar-month :as calendar-month-docs]
   [ty.site.docs.checkbox :as checkbox-docs]
   [ty.site.docs.common :as common]
   [ty.site.docs.copy-field :as copy-field-docs]
   [ty.site.docs.date-picker :as date-picker-docs]
   [ty.site.docs.dropdown :as dropdown-docs]
   [ty.site.docs.htmx :as htmx-docs]
   [ty.site.docs.icon :as icon-docs]
   [ty.site.docs.input :as input-docs]
   [ty.site.docs.js-react :as js-react-docs]
   [ty.site.docs.modal :as modal-docs]
   [ty.site.docs.multiselect :as multiselect-docs]
   [ty.site.docs.popup :as popup-docs]
   [ty.site.docs.react :as react-docs]
   [ty.site.docs.replicant :as replicant-docs]
   [ty.site.docs.resize-observer :as resize-observer-docs]
   [ty.site.docs.tabs :as tabs-docs]
   [ty.site.docs.tag :as tag-docs]
   [ty.site.docs.textarea :as textarea-docs]
   [ty.site.docs.tooltip :as tooltip-docs]
   [ty.site.docs.wizard :as wizard-docs]
    ;; Import component doc namespaces
   [ty.site.views.getting-started :as getting-started]
   [ty.site.views.ty-styles :as ty-styles]))

(def docs-components
  [{:id :ty.site.docs/button
    :segment "button"
    :icon "square"
    :view button-docs/view
    :name "Button"
    :description "Interactive button with variants and states"
    :tags ["action" "click" "submit" "form"]}
   {:id :ty.site.docs/calendar
    :segment "calendar"
    :icon "calendar"
    :view calendar-docs/view
    :name "Calendar"
    :description "Full calendar view for date selection"
    :tags ["date" "picker" "schedule" "month"]}
   {:id :ty.site.docs/calendar-month
    :segment "calendar-month"
    :icon "calendar"
    :view calendar-month-docs/view
    :name "Calendar Month"
    :description "Month grid for calendar displays"
    :tags ["date" "month" "grid" "schedule"]}
   {:id :ty.site.docs/date-picker
    :segment "date-picker"
    :icon "clock"
    :view date-picker-docs/view
    :name "Date Picker"
    :description "Date input with popup calendar"
    :tags ["date" "input" "form" "picker" "time"]}
   {:id :ty.site.docs/dropdown
    :segment "dropdown"
    :icon "chevron-down"
    :view dropdown-docs/view
    :name "Dropdown"
    :description "Select from a list of options"
    :tags ["select" "options" "form" "menu" "choice"]}
   {:id :ty.site.docs/icon
    :segment "icon"
    :icon "star"
    :view icon-docs/view
    :name "Icon"
    :description "SVG icons with size and color variants"
    :tags ["svg" "image" "symbol" "graphic"]}
   {:id :ty.site.docs/inputs
    :segment "inputs"
    :icon "inputs"
    :name "Inputs"
    :description "Form input components"
    :tags ["form" "field" "text"]
    :view (fn [] (router/navigate! :ty.site.docs/input-field) nil)
    :children [{:id :ty.site.docs/input-field
                :segment "input-field"
                :icon "edit-3"
                :view input-docs/view
                :name "Input Field"
                :description "Single-line text input"
                :tags ["text" "form" "field" "input"]}
               {:id :ty.site.docs/checkbox
                :segment "checkbox"
                :icon "check-square"
                :view checkbox-docs/view
                :name "Checkbox"
                :description "Toggle boolean values"
                :tags ["toggle" "boolean" "form" "check"]}
               {:id :ty.site.docs/copy-field
                :segment "copy-field"
                :icon "copy"
                :view copy-field-docs/view
                :name "Copy Field"
                :description "Read-only field with copy button"
                :tags ["clipboard" "copy" "readonly"]}
               {:id :ty.site.docs/textarea
                :segment "textarea"
                :icon "file-text"
                :view textarea-docs/view
                :name "Textarea"
                :description "Multi-line text input"
                :tags ["text" "multiline" "form" "input"]}]}
   {:id :ty.site.docs/modal
    :segment "modal"
    :icon "layout"
    :view modal-docs/view
    :name "Modal"
    :description "Dialog overlay for focused content"
    :tags ["dialog" "overlay" "popup" "lightbox"]}
   {:id :ty.site.docs/multiselect
    :segment "multiselect"
    :icon "filter"
    :view multiselect-docs/view
    :name "Multiselect"
    :description "Select multiple options with tags"
    :tags ["select" "multiple" "tags" "form" "filter"]}
   {:id :ty.site.docs/popup
    :segment "popup"
    :icon "message-square"
    :view popup-docs/view
    :name "Popup"
    :description "Positioned floating content"
    :tags ["floating" "popover" "overlay" "menu"]}
   {:id :ty.site.docs/resize-observer
    :segment "resize-observer"
    :icon "maximize"
    :view resize-observer-docs/view
    :name "Resize Observer"
    :description "Track element size changes"
    :tags ["responsive" "size" "layout" "observer"]}
   {:id :ty.site.docs/tabs
    :segment "tabs"
    :icon "layout"
    :view tabs-docs/view
    :name "Tabs"
    :description "Tabbed content navigation"
    :tags ["navigation" "panels" "switch" "views"]}
   {:id :ty.site.docs/wizard
    :segment "wizard"
    :icon "list-ordered"
    :view wizard-docs/view
    :name "Wizard"
    :description "Multi-step form workflow"
    :tags ["steps" "stepper" "form" "workflow" "carousel"]}
   {:id :ty.site.docs/tag
    :segment "tag"
    :icon "tag"
    :view tag-docs/view
    :name "Tag"
    :description "Labels and badges for categorization"
    :tags ["label" "badge" "chip" "category"]}
   {:id :ty.site.docs/tooltip
    :segment "tooltip"
    :icon "message-square"
    :view tooltip-docs/view
    :name "Tooltip"
    :description "Contextual information on hover"
    :tags ["hint" "help" "hover" "info"]}])

(def guide-components
  [{:id :ty.site.docs/getting-started
    :segment "getting-started"
    :name "Getting started"
    :icon "rocket"
    :description "Installation and basic setup"
    :tags ["install" "setup" "npm" "quick start"]
    :view #(getting-started/view)}
   {:id :ty.site.docs/css
    :segment "css"
    :name "CSS System"
    :icon "palette"
    :description "Colors, surfaces, and design tokens"
    :tags ["theme" "colors" "dark mode" "styling" "tailwind"]
    :view #(ty-styles/view)}
   {:id :ty.site.docs/replicant
    :segment "replicant"
    :name "Replicant"
    :icon "diamond"
    :description "ClojureScript with Replicant rendering"
    :tags ["clojure" "clojurescript" "replicant" "hiccup"]
    :view replicant-docs/view}
   {:id :ty.site.docs/clojurescript
    :segment "clojurescript"
    :name "CLJS React"
    :icon "clojure"
    :description "ClojureScript with React/Reagent"
    :tags ["clojure" "clojurescript" "react" "reagent"]
    :view react-docs/view}
   {:id :ty.site.docs/react
    :segment "react"
    :name "JS React"
    :icon "react"
    :description "JavaScript React integration"
    :tags ["javascript" "react" "jsx" "typescript"]
    :view js-react-docs/view}
   {:id :ty.site.docs/htmx
    :segment "htmx"
    :name "HTMX"
    :icon "server"
    :description "Server-side rendering with HTMX"
    :tags ["htmx" "server" "html" "ssr" "backend"]
    :view htmx-docs/view}])

;; Define routes with views from separate namespaces

;; Helper to check if current route is a docs route
(defn in-docs? []
  (router/rendered? :ty.site/docs false))

;; Render function - fallback for docs routes not handled by site.core.cljs

(defn render
  "Render the appropriate docs view based on current route"
  []
  ;; No global highlighting needed - individual code blocks handle it via :replicant/on-mount
  (let [{view :view} (some #(when (router/rendered? (:id %) true) %) (concat docs-components guide-components))]
    (if view (view) (getting-started/view))))
