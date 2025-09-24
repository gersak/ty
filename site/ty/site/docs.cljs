(ns ty.site.docs
  "Documentation system for ty components - orchestrates all doc views"
  (:require
    [clojure.string :as str]
    [ty.router :as router]
    [ty.site.docs.button :as button-docs]
    [ty.site.docs.calendar :as calendar-docs]
    [ty.site.docs.calendar-month :as calendar-month-docs]
    [ty.site.docs.common :as common]
    [ty.site.docs.date-picker :as date-picker-docs]
    [ty.site.docs.dropdown :as dropdown-docs]
    [ty.site.docs.icon :as icon-docs]
            ;; Import component doc namespaces
    [ty.site.docs.index :as index]
    [ty.site.docs.input :as input-docs]
    [ty.site.docs.modal :as modal-docs]
    [ty.site.docs.multiselect :as multiselect-docs]
    [ty.site.docs.popup :as popup-docs]
    [ty.site.docs.tag :as tag-docs]
    [ty.site.docs.textarea :as textarea-docs]
    [ty.site.docs.tooltip :as tooltip-docs]
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
   {:id :ty.site.docs/input
    :segment "input"
    :icon "type"
    :view input-docs/view
    :name "Input"}
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
   {:id :ty.site.docs/tag
    :segment "tag"
    :icon "tag"
    :view tag-docs/view
    :name "Tag"}
   {:id :ty.site.docs/textarea
    :segment "textarea"
    :icon "file-text"
    :view textarea-docs/view
    :name "Textarea"}
   {:id :ty.site.docs/tooltip
    :segment "tooltip"
    :icon "message-square"
    :view tooltip-docs/view
    :name "Tooltip"}])


(def guide-components
  [{:id :ty.site.docs/css
    :segment "css"
    :component "CSS System"
    :icon "palette"
    :view #(ty-styles/view)
    :on {:click #(router/navigate! :ty.site.docs/css)}}
   {:id :ty.site.docs/replicant
    :segment "replicant"
    :component "Replicant"
    :icon "diamond"
    :on {:click #(router/navigate! :ty.site.docs/replicant)}}
   {:id :ty.site.docs/clojurescript
    :segment "clojurescript"
    :component "ClojureScript React"
    :icon "zap"
    :on {:click #(router/navigate! :ty.site.docs/clojurescript)}}
   {:id :ty.site.docs/react
    :segment "react"
    :component "JS React"
    :icon "react"
    :on {:click #(router/navigate! :ty.site.docs/react)}}
   {:id :ty.site.docs/htmx
    :segment "htmx"
    :component "HTMX"
    :icon "server"
    :on {:click #(router/navigate! :ty.site.docs/htmx)}}])

;; Define routes with views from separate namespaces
(router/link :ty.site/docs
             (reduce
               (fn [routes component]
                 (conj routes
                       {:id (keyword "ty.site.docs" component)
                        :segment component
                        :view #(common/placeholder-view component)
                        :name (str/capitalize component)}))
               ;; Start with explicitly defined component docs
               (concat
                 docs-components
                 guide-components)
               ;; Add placeholders for remaining components (excluding documented ones)
               (remove #(contains? #{"button" "calendar" "calendar-month" "date-picker" "dropdown" "input" "modal" "multiselect" "popup" "tag" "textarea" "tooltip"} %) index/component-list)))

;; Helper to check if current route is a docs route
(defn in-docs? []
  (let [current (router/rendered? :ty.site/docs false)]
    (or current
        (some #(router/rendered? (keyword "ty.site.docs" %) false)
              (conj index/component-list "css-system")))))

;; Component sidebar for docs
(defn docs-sidebar-item
  [{:keys [component icon active?]
    {on-click :click} :on}]
  [:button.w-full.text-left.px-4.py-2.rounded.transition-colors.cursor-pointer.flex.items-center
   {:class (if active?
             ["ty-bg-primary-" "ty-text-primary++"]
             ["hover:ty-bg-neutral" "ty-text"])
    :on {:click on-click}}
   (when icon [:ty-icon.mr-2 {:name icon}])
   [:div.flex.items-center.gap-2
    [:span.text-sm component]]])

(defn docs-sidebar []
  [:div.space-y-4.mt-8
   ;; Components Section
   [:div.space-y-1
    [:div.px-4.py-2.ty-text+.text-sm.font-medium "Components"]
    (for [{component :name
           icon :icon
           id :id} docs-components]
      [:div {:key component}
       (docs-sidebar-item
         {:component component
          :icon icon
          :on {:click #(router/navigate! id)}
          :active? (router/rendered? id true)})])]

   ;; Getting Started Section
   [:div.space-y-1
    [:div.px-4.py-2.ty-text+.text-sm.font-medium "Getting Started"]
    (map
      (fn [{:keys [id]
            :as component}]
        (docs-sidebar-item (assoc component :active? (router/rendered? id true))))
      guide-components)]])

;; Removed highlight-all-code-blocks! - now using individual :replicant/on-mount in code-block

(defn render
  "Render the appropriate docs view based on current route"
  []
  ;; No global highlighting needed - individual code blocks handle it via :replicant/on-mount
  (let [{view :view} (some #(when (router/rendered? (:id %) true) %) (concat docs-components guide-components))]
    (if view (view) (index/view))))
