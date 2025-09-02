(ns ty.demo.views.navigation
  (:require [ty.router :as router]))

(defn view []
  [:div.demo-section
   [:h2.demo-title "Navigation Components"]
   [:p.mb-6.ty-text-
    "Semantic navigation buttons with proper states, theming, and accessibility."]

   ;; Navigation States Demo
   [:div.mb-8
    [:h3.demo-subtitle "Navigation States"]
    [:p.mb-4.text-sm.ty-text--
     "Hover, focus, and active states with semantic styling using Ty variables."]

    [:div.ty-content.p-6.rounded-lg.border
     [:div.ty-nav-list.max-w-sm
      ;; Regular navigation item
      [:button.ty-nav-item
       [:ty-icon {:name "home" :size "sm"}]
       [:span.nav-text "Regular Item"]]

      ;; Active navigation item  
      [:button.ty-nav-item.active
       [:ty-icon {:name "settings" :size "sm"}]
       [:span.nav-text "Active Item"]]

      ;; Navigation item with badge
      [:button.ty-nav-item
       [:ty-icon {:name "inbox" :size "sm"}]
       [:span.nav-text "With Badge"]
       [:span.nav-badge "3"]]

      ;; Active item with badge
      [:button.ty-nav-item.active
       [:ty-icon {:name "bell" :size "sm"}]
       [:span.nav-text "Active with Badge"]
       [:span.nav-badge "12"]]]]]

   ;; Navigation Sizes Demo
   [:div.mb-8
    [:h3.demo-subtitle "Navigation Sizes"]
    [:p.mb-4.text-sm.ty-text--
     "Different size variants for different contexts."]

    [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
     ;; Compact size
     [:div.ty-content.p-4.rounded-lg.border
      [:h4.text-sm.font-semibold.mb-3 "Compact (.compact)"]
      [:div.ty-nav-list.max-w-xs
       [:button.ty-nav-item.compact
        [:ty-icon {:name "home" :size "xs"}]
        [:span.nav-text "Dashboard"]]
       [:button.ty-nav-item.compact.active
        [:ty-icon {:name "users" :size "xs"}]
        [:span.nav-text "Users"]]]]

     ;; Large size
     [:div.ty-content.p-4.rounded-lg.border
      [:h4.text-sm.font-semibold.mb-3 "Large (.large)"]
      [:div.ty-nav-list.max-w-sm
       [:button.ty-nav-item.large
        [:ty-icon {:name "home" :size "md"}]
        [:span.nav-text "Dashboard"]]
       [:button.ty-nav-item.large.active
        [:ty-icon {:name "users" :size "md"}]
        [:span.nav-text "Users"]]]]]]

   ;; Navigation Layout Demo
   [:div.mb-8
    [:h3.demo-subtitle "Navigation Layout"]
    [:p.mb-4.text-sm.ty-text--
     "Complete navigation layouts with sections and dividers."]

    [:div.ty-content.p-6.rounded-lg.border
     [:div.max-w-xs.mx-auto
      [:div.ty-nav-section-title "Main"]
      [:div.ty-nav-list
       [:button.ty-nav-item.active
        [:ty-icon {:name "home" :size "sm"}]
        [:span.nav-text "Dashboard"]]
       [:button.ty-nav-item
        [:ty-icon {:name "chart-bar" :size "sm"}]
        [:span.nav-text "Analytics"]
        [:span.nav-badge "new"]]]

      [:div.ty-nav-divider]

      [:div.ty-nav-section-title "Content"]
      [:div.ty-nav-list
       [:button.ty-nav-item
        [:ty-icon {:name "document-text" :size "sm"}]
        [:span.nav-text "Articles"]]
       [:button.ty-nav-item
        [:ty-icon {:name "photo" :size "sm"}]
        [:span.nav-text "Media"]]]]]]

   ;; CSS Classes Documentation  
   [:div.mb-8
    [:h3.demo-subtitle "CSS Classes"]
    [:p.mb-4.text-sm.ty-text--
     "Available classes for navigation styling."]

    [:div.ty-elevated.ty-text.p-4.rounded-lg.text-sm.font-mono.overflow-x-auto
     [:pre
      ".ty-nav-item           /* Base navigation item */
.ty-nav-item:hover     /* Hover state */
.ty-nav-item:focus     /* Focus state */ 
.ty-nav-item.active    /* Active/selected state */

/* Size variants */
.ty-nav-item.compact   /* Smaller padding */
.ty-nav-item.large     /* Larger padding */
.ty-nav-item.xl        /* Extra large */

/* Layout containers */
.ty-nav-list           /* Navigation list container */
.ty-nav-section-title  /* Section titles */
.ty-nav-divider        /* Section dividers */

/* Content elements */
.nav-text              /* Navigation text */
.nav-badge             /* Badge/counter styling */"]]]

   ;; Interactive Example
   [:div.mb-8
    [:h3.demo-subtitle "Interactive Example"]
    [:p.mb-4.text-sm.ty-text--
     "Click to navigate between different demo sections (uses actual router)."]

    [:div.ty-content.p-6.rounded-lg.border
     [:div.max-w-sm.mx-auto.ty-nav-list
      [:button.ty-nav-item
       {:class (when (router/rendered? :ty.demo.core/buttons true) "active")
        :on {:click #(router/navigate! :ty.demo.core/buttons)}}
       [:ty-icon {:name "click" :size "sm"}]
       [:span.nav-text "Buttons Demo"]]

      [:button.ty-nav-item
       {:class (when (router/rendered? :ty.demo.core/inputs true) "active")
        :on {:click #(router/navigate! :ty.demo.core/inputs)}}
       [:ty-icon {:name "type" :size "sm"}]
       [:span.nav-text "Inputs Demo"]]

      [:button.ty-nav-item
       {:class (when (router/rendered? :ty.demo.core/theme-utilities true) "active")
        :on {:click #(router/navigate! :ty.demo.core/theme-utilities)}}
       [:ty-icon {:name "palette" :size "sm"}]
       [:span.nav-text "Theme Utilities"]]]]]])
