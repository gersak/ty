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
   [ty.site.docs.icon :as icon-docs]
   [ty.site.docs.input-field :as input-field-docs]
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
    ;; Import component doc namespaces
   [ty.site.views.getting-started :as getting-started]
   [ty.site.views.ty-styles :as ty-styles]))

(def docs-components
  [{:id :ty.site.docs/button
    :segment "button"
    :icon "square"
    :view button-docs/view
    :name "Button"}
   {:id :ty.site.docs/calendar
    :segment "calendar"
    :icon "calendar"
    :view calendar-docs/view
    :name "Calendar"}
   {:id :ty.site.docs/calendar-month
    :segment "calendar-month"
    :icon "calendar"
    :view calendar-month-docs/view
    :name "Calendar Month"}
   {:id :ty.site.docs/date-picker
    :segment "date-picker"
    :icon "clock"
    :view date-picker-docs/view
    :name "Date Picker"}
   {:id :ty.site.docs/dropdown
    :segment "dropdown"
    :icon "chevron-down"
    :view dropdown-docs/view
    :name "Dropdown"}
   {:id :ty.site.docs/icon
    :segment "icon"
    :icon "star"
    :view icon-docs/view
    :name "Icon"}
   {:id :ty.site.docs/inputs
    :segment "inputs"
    :icon "inputs"
    :name "Inputs"
    :view (fn [] (router/navigate! :ty.site.docs/input-field) nil)
    :children [{:id :ty.site.docs/input-field
                :segment "input-field"
                :icon "edit-3"
                :view input-field-docs/view
                :name "Input Field"}
               {:id :ty.site.docs/checkbox
                :segment "checkbox"
                :icon "check-square"
                :view checkbox-docs/view
                :name "Checkbox"}
               {:id :ty.site.docs/copy-field
                :segment "copy-field"
                :icon "copy"
                :view copy-field-docs/view
                :name "Copy Field"}
               {:id :ty.site.docs/textarea
                :segment "textarea"
                :icon "file-text"
                :view textarea-docs/view
                :name "Textarea"}]}
   {:id :ty.site.docs/modal
    :segment "modal"
    :icon "layout"
    :view modal-docs/view
    :name "Modal"}
   {:id :ty.site.docs/multiselect
    :segment "multiselect"
    :icon "filter"
    :view multiselect-docs/view
    :name "Multiselect"}
   {:id :ty.site.docs/popup
    :segment "popup"
    :icon "message-square"
    :view popup-docs/view
    :name "Popup"}
   {:id :ty.site.docs/resize-observer
    :segment "resize-observer"
    :icon "maximize"
    :view resize-observer-docs/view
    :name "Resize Observer"}
   {:id :ty.site.docs/tabs
    :segment "tabs"
    :icon "layout"
    :view tabs-docs/view
    :name "Tabs"}
   {:id :ty.site.docs/tag
    :segment "tag"
    :icon "tag"
    :view tag-docs/view
    :name "Tag"}
   {:id :ty.site.docs/tooltip
    :segment "tooltip"
    :icon "message-square"
    :view tooltip-docs/view
    :name "Tooltip"}])

(def guide-components
  [{:id :ty.site.docs/getting-started
    :segment "getting-started"
    :name "Getting started"
    :icon "rocket"
    :view #(getting-started/view)}
   {:id :ty.site.docs/css
    :segment "css"
    :name "CSS System"
    :icon "palette"
    :view #(ty-styles/view)}
   {:id :ty.site.docs/replicant
    :segment "replicant"
    :name "Replicant"
    :icon "diamond"
    :view replicant-docs/view}
   {:id :ty.site.docs/clojurescript
    :segment "clojurescript"
    :name "CLJS React"
    :icon "lambda"
    :view react-docs/view}
   {:id :ty.site.docs/react
    :segment "react"
    :name "JS React"
    :icon "react"
    :view #(common/guide-placeholder-view
            "JavaScript React Integration"
            "Learn how to integrate Ty web components with JavaScript React applications.")}
   {:id :ty.site.docs/htmx
    :segment "htmx"
    :name "HTMX"
    :icon "server"
    :view #(common/guide-placeholder-view
            "HTMX Integration"
            "Discover how to use Ty components with HTMX for dynamic server-side applications.")}])

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
