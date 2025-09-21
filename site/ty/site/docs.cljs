(ns ty.site.docs
  "Documentation system for ty components - orchestrates all doc views"
  (:require [clojure.string :as str]
            [ty.router :as router]
            [ty.site.docs.button :as button-docs]
            [ty.site.docs.common :as common]
            [ty.site.docs.css-system :as css-system]
            ;; Import component doc namespaces
            [ty.site.docs.index :as index]
            [ty.site.docs.input :as input-docs]
            [ty.site.docs.modal :as modal-docs]))

;; Re-export component list from index
(def component-list index/component-list)

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
              [{:id :ty.site.docs/button
                :segment "button"
                :view button-docs/view
                :name "Button"}
               {:id :ty.site.docs/input
                :segment "input"
                :view input-docs/view
                :name "Input"}
               {:id :ty.site.docs/modal
                :segment "modal"
                :view modal-docs/view
                :name "Modal"}
               {:id :ty.site.docs/css-system
                :segment "css-system"
                :view css-system/view
                :name "CSS System"}]
               ;; Add placeholders for remaining components (excluding documented ones)
              (remove #(contains? #{"button" "input" "modal"} %) component-list)))

;; Helper to check if current route is a docs route
(defn in-docs? []
  (let [current (router/rendered? :ty.site/docs false)]
    (or current
        (some #(router/rendered? (keyword "ty.site.docs" %) false)
              (conj component-list "css-system")))))

;; Component sidebar for docs
(defn docs-sidebar-item [{:keys [component active?]}]
  [:button.w-full.text-left.px-4.py-2.rounded.transition-colors
   {:class (if active?
             "ty-bg-primary- ty-text-primary++"
             "hover:ty-content ty-text")
    :on {:click #(router/navigate! (keyword "ty.site.docs" component))}}
   [:div.flex.items-center.gap-2
    [:span.text-sm (str "ty-" component)]]])

(defn docs-sidebar []
  [:div.space-y-1
   [:div.px-4.py-2.ty-text+.text-sm.font-medium "Components"]
   (for [component component-list]
     [:div {:key component}
      (docs-sidebar-item
       {:component component
        :active? (router/rendered? (keyword "ty.site.docs" component) true)})])
   [:div.mt-4.pt-4.border-t.ty-border
    (docs-sidebar-item
     {:component "css-system"
      :active? (router/rendered? :ty.site.docs/css-system true)})]])

(defn highlight-all-code-blocks!
  "Highlight all code blocks on the page"
  []
  (when (and js/window.hljs (.-highlightAll js/window.hljs))
    ;; Small delay to ensure DOM is updated
    (js/setTimeout
     #(js/window.hljs.highlightAll)
     50)))

(defn render
  "Render the appropriate docs view based on current route"
  []
  ;; Trigger highlighting after render
  (js/setTimeout highlight-all-code-blocks! 100)

  (let [rendered (router/url->components)
        {view :view} (last rendered)]
    (if view (view) (index/view))))
