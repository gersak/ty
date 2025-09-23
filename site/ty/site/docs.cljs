(ns ty.site.docs
  "Documentation system for ty components - orchestrates all doc views"
  (:require [clojure.string :as str]
            [ty.router :as router]
            [ty.site.docs.button :as button-docs]
            [ty.site.docs.calendar :as calendar-docs]
            [ty.site.docs.calendar-month :as calendar-month-docs]
            [ty.site.docs.common :as common]
            [ty.site.docs.css-system :as css-system]
            [ty.site.docs.dropdown :as dropdown-docs]
            ;; Import component doc namespaces
            [ty.site.docs.index :as index]
            [ty.site.docs.input :as input-docs]
            [ty.site.docs.modal :as modal-docs]
            [ty.site.docs.multiselect :as multiselect-docs]
            [ty.site.docs.popup :as popup-docs]
            [ty.site.docs.tag :as tag-docs]
            [ty.site.docs.textarea :as textarea-docs]
            [ty.site.docs.tooltip :as tooltip-docs]))


(def docs-components
  [{:id :ty.site.docs/button
    :segment "button"
    :view button-docs/view
    :name "Button"}
   {:id :ty.site.docs/calendar
    :segment "calendar"
    :view calendar-docs/view
    :name "Calendar"}
   {:id :ty.site.docs/calendar-month
    :segment "calendar-month"
    :view calendar-month-docs/view
    :name "Calendar Month"}
   {:id :ty.site.docs/dropdown
    :segment "dropdown"
    :view dropdown-docs/view
    :name "Dropdown"}
   {:id :ty.site.docs/input
    :segment "input"
    :view input-docs/view
    :name "Input"}
   {:id :ty.site.docs/modal
    :segment "modal"
    :view modal-docs/view
    :name "Modal"}
   {:id :ty.site.docs/multiselect
    :segment "multiselect"
    :view multiselect-docs/view
    :name "Multiselect"}
   {:id :ty.site.docs/popup
    :segment "popup"
    :view popup-docs/view
    :name "Popup"}
   {:id :ty.site.docs/tag
    :segment "tag"
    :view tag-docs/view
    :name "Tag"}
   {:id :ty.site.docs/textarea
    :segment "textarea"
    :view textarea-docs/view
    :name "Textarea"}
   {:id :ty.site.docs/tooltip
    :segment "tooltip"
    :view tooltip-docs/view
    :name "Tooltip"}
   {:id :ty.site.docs/css-system
    :segment "css-system"
    :view css-system/view
    :name "CSS System"}])

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
               docs-components
               ;; Add placeholders for remaining components (excluding documented ones)
               (remove #(contains? #{"button" "calendar" "calendar-month" "dropdown" "input" "modal" "multiselect" "popup" "tag" "textarea" "tooltip"} %) index/component-list)))

;; Helper to check if current route is a docs route
(defn in-docs? []
  (let [current (router/rendered? :ty.site/docs false)]
    (or current
        (some #(router/rendered? (keyword "ty.site.docs" %) false)
              (conj index/component-list "css-system")))))

;; Component sidebar for docs
(defn docs-sidebar-item [{:keys [component active?]
                          {on-click :click} :on}]
  [:button.w-full.text-left.px-4.py-2.rounded.transition-colors
   {:class (if active?
             ["ty-bg-primary-" "ty-text-primary++"]
             ["hover:ty-content" "ty-text"])
    :on {:click on-click}}
   [:div.flex.items-center.gap-2
    [:span.text-sm component]]])

(defn docs-sidebar []
  [:div.space-y-1
   [:div.px-4.py-2.ty-text+.text-sm.font-medium "Components"]
   (for [{component :name
          id :id} docs-components]
     [:div {:key component}
      (docs-sidebar-item
        {:component component
         :on {:click #(router/navigate! id)}
         :active? (router/rendered? id true)})])
   [:div.mt-4.pt-4.border-t.ty-border
    (docs-sidebar-item
      {:component "CSS System"
       :on {:click #(router/navigate! :ty.site.docs/css-system)}
       :active? (router/rendered? :ty.site.docs/css-system true)})]])

;; Removed highlight-all-code-blocks! - now using individual :replicant/on-mount in code-block

(defn render
  "Render the appropriate docs view based on current route"
  []
  ;; No global highlighting needed - individual code blocks handle it via :replicant/on-mount
  (let [{view :view} (some #(when (router/rendered? (:id %) true) %) docs-components)]
    (if view (view) (index/view))))
