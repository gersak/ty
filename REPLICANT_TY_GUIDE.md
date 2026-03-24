# Replicant + Ty Components - Developer Guide

## 🎯 Overview

This guide covers best practices, patterns, and common pitfalls when developing with **Replicant** (ClojureScript DOM library) and **Ty web components**. Whether you're building new features or maintaining existing code, these patterns will help you write clean, performant, and maintainable code.

## 📚 Table of Contents

1. [Core Principles](#core-principles)
2. [Project Structure](#project-structure)
3. [Styling with Ty CSS System](#styling-with-ty-css-system)
4. [Component Development](#component-development)
5. [Routing Patterns](#routing-patterns)
6. [State Management](#state-management)
7. [Event Handling](#event-handling)
8. [Icon Usage](#icon-usage)
9. [Form Handling](#form-handling)
10. [Tabs Component](#tabs-component-ty-tabs)
11. [Common Pitfalls](#common-pitfalls)
12. [Performance Tips](#performance-tips)
13. [Testing Patterns](#testing-patterns)

---

## Core Principles

### 1. **Separation of Concerns**

```clojure
;; ✅ GOOD: Separate view, state, and logic
(ns my-app.views.user-profile
  (:require [my-app.state :as state]))

;; State in dedicated namespace
(defn validate-email [email] ...)
(defn handle-change [field] ...)

;; View function is pure hiccup
(defn view []
  [:div.ty-elevated ...])
```

```clojure
;; ❌ BAD: Everything mixed together
(defn view []
  (let [valid? (fn [x] ...)] ; validation logic in view
    (swap! some-atom assoc :x 1) ; direct state mutation
    [:div ...]))
```

### 2. **Ty for Colors, Tailwind for Everything Else**

This is the **golden rule** of styling in this project:

```clojure
;; ✅ GOOD: Clear separation
[:div.ty-elevated.p-6.rounded-lg.flex.items-center
 [:h2.ty-text++.text-xl.font-bold "Title"]
 [:p.ty-text-.text-sm "Description"]]

;; ❌ BAD: Mixing concerns
[:div.bg-blue-500.ty-elevated  ; Don't use Tailwind colors
 [:p.ty-padding-4 "Text"]]     ; Don't use ty for spacing
```

### 3. **Component State Pattern**

```clojure
;; ✅ GOOD: Centralized state with namespaced keys
(def state (atom {:user-profile {:form-data {}
                                 :validation-errors {}
                                 :touched-fields #{}}
                  :booking {:selected-date nil
                            :services #{}}}))

;; Access with path
(get-in @state [:user-profile :form-data])
(swap! state assoc-in [:user-profile :touched-fields] #{:email})

;; ❌ BAD: Multiple scattered atoms
(def form-data (atom {}))
(def errors (atom {}))
(def touched (atom #{}))
```

---

## Project Structure

### Typical View Structure

```
site/
└── ty/
    └── site/
        ├── core.cljs          # Main app, routing, layout
        ├── state.cljs         # Global state atom
        ├── icons.cljs         # Icon registration
        ├── docs.cljs          # Documentation routing
        └── views/
            ├── landing.cljs   # Landing page
            ├── user_profile.cljs
            ├── event_booking.cljs
            └── contact_form.cljs
```

### Component Structure in `lib/ty/`

```
lib/ty/
└── components/
    ├── input.cljs         # Component logic
    ├── input.css          # Component styles
    ├── button.cljs
    ├── button.css
    └── ...
```

**Key Pattern**: Each component has co-located CSS using `defstyles` macro.

---

## Styling with Ty CSS System

### Surface Classes (Backgrounds with Semantic Meaning)

```clojure
;; App layout hierarchy
[:div.ty-canvas          ; App background
 [:div.ty-content        ; Main content areas
  [:div.ty-elevated      ; Cards, panels (includes shadow)
   [:div.ty-floating]]]] ; Modals, dropdowns (includes shadow)

;; Form controls
[:input.ty-input]        ; Form input background
```

### Text Color Hierarchy (5-Variant System)

```clojure
;; Emphasis levels
[:h1.ty-text++]          ; Maximum emphasis (headers)
[:h2.ty-text+]           ; High emphasis (subheaders)
[:p.ty-text]             ; Normal text
[:span.ty-text-]         ; Reduced emphasis (captions)
[:small.ty-text--]       ; Minimal emphasis (hints)

;; Semantic colors with variants
[:p.ty-text-primary++]   ; Strong primary text
[:p.ty-text-primary]     ; Base primary text
[:p.ty-text-primary-]    ; Soft primary text

;; Same pattern for: success, danger, warning, info, neutral
[:p.ty-text-danger++]    ; Error messages
[:p.ty-text-success]     ; Success messages
```

### Background Colors (3-Variant System)

```clojure
;; Stronger → Base → Softer
[:div.ty-bg-primary+]    ; Stronger background
[:div.ty-bg-primary]     ; Base background
[:div.ty-bg-primary-]    ; Softer background

;; Available for all semantic colors
[:div.ty-bg-success-]    ; Soft success background
[:div.ty-bg-danger]      ; Base danger background
[:div.ty-bg-warning+]    ; Strong warning background
```

### Border Colors (5-Variant System)

```clojure
;; Emphasis levels
[:div.ty-border++]       ; Maximum border
[:div.ty-border+]        ; Strong border
[:div.ty-border]         ; Normal border
[:div.ty-border-]        ; Soft border
[:div.ty-border--]       ; Minimal border

;; Semantic borders
[:div.ty-border-primary.border]
[:div.ty-border-danger.border]
[:div.ty-border-success.border]
```

### Complete Card Example

```clojure
[:div.ty-elevated.p-6.rounded-lg.border.ty-border-
 [:h2.ty-text++.text-xl.font-bold.mb-4 "Card Title"]
 [:p.ty-text-.text-sm.mb-6 "Card description"]
 [:button.ty-bg-primary.ty-text++.px-4.py-2.rounded.font-medium
  "Primary Action"]]
```

### Alert Components Pattern

```clojure
;; Success alert
[:div.ty-bg-success-.ty-border-success.border.rounded-lg.p-4
 [:h3.ty-text-success++.font-semibold "Success!"]
 [:p.ty-text-success.text-sm "Operation completed."]]

;; Error alert
[:div.ty-bg-danger-.ty-border-danger.border.rounded-lg.p-4
 [:h3.ty-text-danger++.font-semibold "Error!"]
 [:p.ty-text-danger.text-sm "Something went wrong."]]

;; Warning alert
[:div.ty-bg-warning-.ty-border-warning.border.rounded-lg.p-4
 [:h3.ty-text-warning++.font-semibold "Warning!"]
 [:p.ty-text-warning.text-sm "Please review this action."]]
```

### Button Patterns with Icons

```clojure
;; ✅ GOOD: Use flexbox gap, no margin on icon
[:button.ty-bg-primary.ty-text++.px-4.py-2.rounded.flex.items-center.gap-2
 [:ty-icon {:name "save" :size "sm"}]
 "Save Document"]

;; ✅ GOOD: Icon-only button
[:button.ty-bg-secondary.ty-text++.p-2.rounded
 [:ty-icon {:name "settings" :size "sm"}]]

;; ❌ BAD: Don't add margins to icons
[:button.ty-bg-primary.ty-text++.px-4.py-2.rounded
 [:ty-icon.mr-3 {:name "save"}]  ; ❌ Don't do this
 "Save"]
```

### Form Styling

```clojure
[:div.space-y-4
 [:label.ty-text+.block.text-sm.font-medium "Email"]
 [:input.ty-input.ty-border.border.rounded-md.px-3.py-2.w-full.
  focus:ty-border-primary.focus:outline-none
  {:type "email"}]
 
 ;; Error state
 [:input.ty-input.ty-border-danger.border.rounded-md.px-3.py-2.w-full
  {:type "email"}]
 [:p.ty-text-danger.text-xs.mt-1 "Invalid email"]]
```

---

## Component Development

### Basic Component Structure

```clojure
(ns my-app.components.user-card
  (:require [ty.css :refer [ensure-styles!]])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load component styles
(defstyles user-card-styles)

(defn user-card [{:keys [name email avatar]}]
  [:div.ty-elevated.p-6.rounded-lg
   [:div.flex.items-center.gap-4
    [:img.w-12.h-12.rounded-full {:src avatar :alt name}]
    [:div
     [:h3.ty-text++.font-semibold name]
     [:p.ty-text-.text-sm email]]]])
```

### Ty Web Component Usage

```clojure
;; Input component
[:ty-input
 {:type "email"
  :label "Email Address"
  :placeholder "john@example.com"
  :required true
  :error (when (get errors :email) "Invalid email")
  :on {:ty-input-change (handle-change :email)}}]

;; Button component
[:ty-button
 {:flavor "primary"
  :size "md"
  :disabled submitting?
  :on {:click handle-submit}}
 "Submit Form"]

;; Calendar component
[:ty-calendar
 {:value selected-date
  :min (js/Date.)
  :locale "en"
  :on {:ty-calendar-change handle-date-change}}]

;; Dropdown component
[:ty-dropdown
 {:label "Select Country"
  :value selected-country
  :on {:ty-dropdown-change handle-country-change}}
 [:ty-dropdown-option {:value "us"} "United States"]
 [:ty-dropdown-option {:value "uk"} "United Kingdom"]]

;; Modal component
[:ty-modal
 {:open show-modal?
  :on {:ty-modal-close #(swap! state assoc :show-modal false)}}
 [:div.p-6.ty-floating.rounded-lg
  [:h2.ty-text++.text-xl.font-bold.mb-4 "Modal Title"]
  [:p.ty-text "Modal content"]]]
```


;; Tabs component
[:ty-tabs
 {:width "100%"
  :height "600px"
  :active "general"
  :on {:ty-tab-change handle-tab-change}}
 
 ;; Simple text labels
 [:ty-tab {:id "general" :label "General Settings"}
  [:div.p-6 "General content"]]
  
 [:ty-tab {:id "advanced" :label "Advanced Settings"}
  [:div.p-6 "Advanced content"]]]

;; Tabs with rich labels (icons, badges)
[:ty-tabs
 {:width "800px"
  :height "500px"}
 
 ;; Rich labels as direct children of ty-tabs
 [:span {:slot "label-profile"
         :class "flex items-center gap-2"}
  [:ty-icon {:name "user" :size "sm"}]
  "Profile"]
  
 [:span {:slot "label-notifications"
         :class "flex items-center gap-2"}
  [:ty-icon {:name "bell" :size "sm"}]
  "Notifications"
  [:span.ty-bg-danger.ty-text-danger++.px-2.py-0.5.rounded-full.text-xs.font-bold "5"]]
  
 ;; Tab panels
 [:ty-tab {:id "profile"}
  [:div.p-6 "Profile content"]]
  
 [:ty-tab {:id "notifications"}
  [:div.p-6 "Notifications content"]]]

;; Tabs with custom marker (active indicator)
[:ty-tabs
 {:width "100%"
  :height "400px"}
 
 ;; Custom underline marker
 [:div {:slot "marker"
        :style {:height "2px"
                :background "var(--ty-color-primary)"
                :position "absolute"
                :bottom "0"}}]
                
 [:ty-tab {:id "tab1" :label "Dashboard"}
  [:div.p-6 "Content"]]]
```

### Custom Event Handling Pattern

```clojure
;; Event details are in .-detail property
(defn handle-input-change [field-key]
  (fn [event]
    (let [value (-> event .-detail .-value)]
      (swap! state assoc-in [:form field-key] value))))

;; Calendar event has date
(defn handle-date-change [event]
  (let [date (-> event .-detail .-date)]
    (swap! state assoc :selected-date date)))

;; Dropdown event has value and label
(defn handle-dropdown-change [event]
  (let [value (-> event .-detail .-value)
        label (-> event .-detail .-label)]
    (swap! state assoc :selected {:value value :label label})))
```


;; Tabs events
(defn handle-tab-change [event]
  (let [detail (.-detail event)
        active-id (.-activeId detail)
        active-index (.-activeIndex detail)
        previous-id (.-previousId detail)
        previous-index (.-previousIndex detail)]
    (swap! state assoc :current-tab active-id)
    (println "Switched from tab" previous-id "to" active-id)))
```

---

## Routing Patterns

### Key Pattern: Self-Contained Views

The most important routing pattern in Replicant: **each view checks its own visibility**. No `cond` in parent - just list all views and each one decides if it should render.

```clojure
;; Each view owns its visibility check
(defn home-view []
  (when (router/rendered? ::home)
    [:div.ty-elevated.p-6
     [:h2 "Welcome Home"]]))

(defn about-view []
  (when (router/rendered? ::about)
    [:div.ty-elevated.p-6
     [:h2 "About Us"]]))

;; App just lists all views - NO cond needed!
(defn app []
  [:div.ty-canvas
   (nav)
   (home-view)    ; checks ::home internally
   (about-view)]) ; checks ::about internally
```

### Route Definition

```clojure
(ns my-app.core
  (:require [ty.router :as router]))

;; Link routes to router root
(router/link ::router/root
  [{:id ::home
    :segment "home"
    :landing 10}      ; highest landing = default route
   {:id ::about
    :segment "about"}
   {:id ::settings
    :segment "settings"}])

;; Child routes linked to parent
(router/link ::settings
  [{:id ::settings-general
    :segment "general"}
   {:id ::settings-account
    :segment "account"}])
```

### Router Setup

```clojure
(ns my-app.core
  (:require [replicant.dom :as dom]
            [ty.router :as router]))

;; Initialize router with optional base path
(router/init! "")  ; or "/my-base" for subdirectory

;; Watch router changes for re-renders
(add-watch router/*router* ::render
           (fn [_ _ _ _]
             (render!)))

;; Watch state for re-renders
(add-watch state ::render
           (fn [_ _ _ _]
             (render!)))

;; Render function
(defn render! []
  (dom/render (.getElementById js/document "app") (app)))
```

### Navigation

```clojure
;; Programmatic navigation
(router/navigate! ::home)
(router/navigate! ::about)

;; Navigate with query parameters
(router/navigate! ::user {:id 123})

;; Check if route is active
(router/rendered? ::home)        ; anywhere in current path
(router/rendered? ::home true)   ; exact match only
```

### Navigation Button Pattern

```clojure
(defn nav-button [route-id label icon-name]
  (let [active? (router/rendered? route-id)]
    [:button.px-4.py-2.rounded.flex.items-center.gap-2.transition-colors
     {:class (if active?
               ["ty-bg-primary" "ty-text++"]
               ["ty-bg-neutral-" "ty-text" "hover:ty-bg-neutral"])
      :on {:click #(router/navigate! route-id)}}
     [:ty-icon {:name icon-name :size "sm"}]
     label]))

(defn nav []
  [:nav.ty-elevated.p-4.rounded-lg.mb-6
   [:div.flex.gap-2
    (nav-button ::home "Home" "home")
    (nav-button ::about "About" "info")
    (nav-button ::settings "Settings" "settings")]])
```

### Complete Routing Example

```clojure
(ns my-app.core
  (:require [replicant.dom :as dom]
            [ty.router :as router]
            [ty.icons :as icons]
            [ty.lucide :as lucide]))

(defonce state (atom {:counter 0}))

;; Routes
(router/link ::router/root
  [{:id ::home :segment "home" :landing 10}
   {:id ::about :segment "about"}])

;; Views - each checks its own rendered? state
(defn home-view []
  (when (router/rendered? ::home)
    [:div.ty-elevated.p-6.rounded-lg
     [:h2.ty-text++.text-2xl.mb-4 "Home"]
     [:p.ty-text "Counter: " (:counter @state)]
     [:ty-button {:flavor "primary"
                  :on {:click #(swap! state update :counter inc)}}
      "Increment"]]))

(defn about-view []
  (when (router/rendered? ::about)
    [:div.ty-elevated.p-6.rounded-lg
     [:h2.ty-text++.text-2xl.mb-4 "About"]
     [:p.ty-text "This is the about page."]]))

;; App - lists all views, no cond
(defn app []
  [:div.ty-canvas.min-h-screen.p-6
   (nav)
   (home-view)
   (about-view)])

;; Render & Init
(defn render! []
  (dom/render (.getElementById js/document "app") (app)))

(defn init []
  (icons/register! {:home lucide/home :info lucide/info})
  (router/init! "")
  (add-watch router/*router* ::render (fn [_ _ _ _] (render!)))
  (add-watch state ::render (fn [_ _ _ _] (render!)))
  (render!))
```

---

## State Management

### State Structure

```clojure
(ns my-app.state)

(def state
  (atom {;; UI state
         :theme "light"
         :mobile-menu-open false
         
         ;; Feature states (namespaced)
         :user-profile {:form-data {:first-name ""
                                     :last-name ""
                                     :email ""}
                        :validation-errors {}
                        :touched-fields #{}
                        :is-submitting false
                        :success-modal-open false}
         
         :event-booking {:selected-date nil
                        :selected-time nil
                        :selected-services #{}
                        :booking-complete false}}))
```

### State Access Patterns

```clojure
;; ✅ GOOD: Use get-in for nested access
(get-in @state [:user-profile :form-data :email])

;; ✅ GOOD: Use swap! with assoc-in for updates
(swap! state assoc-in [:user-profile :form-data :email] "new@email.com")

;; ✅ GOOD: Use update-in for transformations
(swap! state update-in [:user-profile :touched-fields] conj :email)

;; ❌ BAD: Don't nest swap! calls
(swap! state update :user-profile
       #(swap! % assoc :email "..."))  ; ❌ Don't do this
```

### State Watchers

```clojure
;; Watch state changes and trigger renders
(add-watch state ::render
           (fn [_ _ old-val new-val]
             (when (not= old-val new-val)
               (render-app!))))

;; Watch specific paths
(add-watch state ::theme-change
           (fn [_ _ old-val new-val]
             (when (not= (:theme old-val) (:theme new-val))
               (update-theme! (:theme new-val)))))
```

---

## Event Handling

### Ty Component Events

```clojure
;; All ty components emit custom events with detail object
(defn handle-input [field]
  (fn [event]
    (let [detail (.-detail event)
          value (.-value detail)]
      (swap! state assoc-in [:form field] value))))

;; Calendar events
(defn handle-date-select [event]
  (let [detail (.-detail event)
        date (.-date detail)
        action (.-action detail)]  ; "select" or "deselect"
    (when (= action "select")
      (swap! state assoc :selected-date date))))

;; Dropdown events  
(defn handle-dropdown [event]
  (let [detail (.-detail event)
        value (.-value detail)
        label (.-label detail)]
    (swap! state assoc :selected-option {:value value :label label})))
```

### Form Submit Pattern

```clojure
(defn handle-submit [event]
  (.preventDefault event)
  (let [form-data (get-in @state [:form :data])
        errors (validate-form form-data)]
    
    ;; Mark all fields as touched
    (swap! state assoc-in [:form :touched-fields]
           #{:name :email :phone})
    
    (if (empty? errors)
      ;; Submit
      (do
        (swap! state assoc-in [:form :is-submitting] true)
        (js/setTimeout
          (fn []
            (swap! state assoc-in [:form :is-submitting] false)
            (swap! state assoc-in [:form :success] true))
          1000))
      
      ;; Show errors
      (swap! state assoc-in [:form :validation-errors] errors))))
```

### Validation Pattern

```clojure
(defn validate-field [field-key value]
  (case field-key
    :email (cond
             (empty? value) "Email is required"
             (not (re-matches #".+@.+" value)) "Invalid email"
             :else nil)
    :phone (when (< (count value) 10)
             "Phone must be at least 10 digits")
    nil))

(defn validate-form [form-data]
  (->> form-data
       (map (fn [[k v]] [k (validate-field k v)]))
       (filter (fn [[_ v]] v))
       (into {})))

;; Real-time validation on change
(defn handle-field-change [field-key]
  (fn [event]
    (let [value (-> event .-detail .-value)]
      (swap! state assoc-in [:form :data field-key] value)
      (swap! state update-in [:form :touched-fields] conj field-key)
      
      ;; Validate immediately
      (let [error (validate-field field-key value)]
        (if error
          (swap! state assoc-in [:form :errors field-key] error)
          (swap! state update-in [:form :errors] dissoc field-key))))))
```

---

## Icon Usage

### Icon Registration

```clojure
(ns my-app.icons
  (:require [ty.icons :as icons]
            [ty.lucide :as lucide]
            [ty.fav6.brands :as fav6-brands]))

;; Register icons with keyword keys (idiomatic ClojureScript)
(icons/register!
  {:home           lucide/home
   :user           lucide/user
   :calendar       lucide/calendar
   :mail           lucide/mail

   ;; UI controls
   :menu           lucide/menu
   :x              lucide/x
   :chevron-down   lucide/chevron-down

   ;; Actions
   :save           lucide/save
   :edit           lucide/edit
   :trash          lucide/trash

   ;; Status
   :check          lucide/check
   :alert-circle   lucide/alert-circle
   :info           lucide/info

   ;; Brands
   :github         fav6-brands/github
   :react          fav6-brands/react})

;; Or use register-async! if your icons.cljs loads before ty.js
(icons/register-async!
  {:check lucide/check
   :home  lucide/home}
  {:on-success #(println "Icons registered!")
   :max-retries 10
   :delay-ms 50})
```

### Icon Usage in Components

```clojure
;; Basic icon
[:ty-icon {:name "user"}]

;; Icon with size
[:ty-icon {:name "home" :size "sm"}]   ; xs, sm, md, lg, xl
[:ty-icon {:name "home" :size "md"}]

;; Icon with custom class
[:ty-icon.ty-text-primary {:name "star"}]

;; Icon in button (use gap, no margin)
[:button.ty-bg-primary.ty-text++.px-4.py-2.rounded.flex.items-center.gap-2
 [:ty-icon {:name "save" :size "sm"}]
 "Save"]

;; Icon-only button
[:button.ty-bg-secondary.p-2.rounded
 [:ty-icon {:name "settings" :size "sm"}]]
```

### Available Icon Sets

- **Lucide**: Modern, clean icons (primary choice)
- **Heroicons**: Solid and outline variants
- **Material**: Material Design icons
- **Fav6 Brands**: Brand logos (GitHub, React, etc.)

---

## Form Handling

### Complete Form Example

```clojure
(defn user-form []
  (let [form-data (get-in @state [:form :data])
        errors (get-in @state [:form :errors])
        touched (get-in @state [:form :touched-fields])
        submitting? (get-in @state [:form :is-submitting])]
    
    [:form.space-y-6
     {:on {:submit handle-submit}}
     
     ;; Name field
     [:div
      [:ty-input
       {:type "text"
        :label "Full Name"
        :placeholder "John Doe"
        :required true
        :value (:name form-data)
        :error (when (touched :name) (errors :name))
        :on {:ty-input-change (handle-field-change :name)}}]]
     
     ;; Email field
     [:div
      [:ty-input
       {:type "email"
        :label "Email Address"
        :placeholder "john@example.com"
        :required true
        :value (:email form-data)
        :error (when (touched :email) (errors :email))
        :on {:ty-input-change (handle-field-change :email)}}]]
     
     ;; Submit button
     [:div.flex.gap-3
      [:ty-button
       {:flavor "primary"
        :type "submit"
        :disabled submitting?}
       (if submitting? "Submitting..." "Submit")]
      
      [:ty-button
       {:flavor "neutral"
        :type "button"
        :on {:click handle-cancel}}
       "Cancel"]]]))
```

### Dropdown with Rich Content

```clojure
[:ty-dropdown
 {:label "Select Country"
  :placeholder "Choose a country"
  :value selected-country
  :on {:ty-dropdown-change handle-country-change}}
 
 ;; Rich option content
 [:ty-dropdown-option {:value "us"}
  [:div.flex.items-center.gap-2
   [:span.text-2xl "🇺🇸"]
   [:div
    [:div.font-medium "United States"]
    [:div.text-xs.ty-text- "North America"]]]]
 
 [:ty-dropdown-option {:value "uk"}
  [:div.flex.items-center.gap-2
   [:span.text-2xl "🇬🇧"]
   [:div
    [:div.font-medium "United Kingdom"]
    [:div.text-xs.ty-text- "Europe"]]]]]
```

### Multiselect with Tags

```clojure
[:ty-multiselect
 {:label "Select Skills"
  :placeholder "Add skills"
  :value (vec selected-skills)
  :on {:ty-multiselect-change handle-skills-change}}
 
 [:ty-multiselect-option
  {:value "clojure"
   :flavor "primary"}
  "Clojure"]
 
 [:ty-multiselect-option
  {:value "javascript"
   :flavor "warning"}
  "JavaScript"]
 
 [:ty-multiselect-option
  {:value "react"
   :flavor "info"}
  "React"]]
```

---

## Tabs Component (ty-tabs)

### Overview

The `ty-tabs` component provides a carousel-based tabbed interface with smooth animations and fixed container dimensions. It's perfect for organizing content into logical sections like settings, profiles, or multi-step forms.

### Basic Structure

```clojure
[:ty-tabs
 {:width "100%"        ; Required - content area width
  :height "600px"      ; Required - total container height
  :active "general"}   ; Optional - initially active tab ID
 
 [:ty-tab {:id "general" :label "General"}
  [:div.p-6 "General content"]]
  
 [:ty-tab {:id "advanced" :label "Advanced"}
  [:div.p-6 "Advanced content"]]]
```

### Key Features

- ✅ **Carousel Animation** - Smooth sliding transitions with automatic direction
- ✅ **Fixed Dimensions** - Prevents layout shift when switching tabs
- ✅ **Independent Scrolling** - Each panel scrolls within fixed viewport
- ✅ **Accessibility** - Built-in ARIA roles and keyboard support (v2)
- ✅ **3-7 Tabs Recommended** - Optimal UX (use alternatives for more)

### Rich Labels with Icons & Badges

Use slots for rich tab button content:

```clojure
[:ty-tabs {:width "800px" :height "500px"}
 
 ;; Rich labels as direct children of ty-tabs
 [:span {:slot "label-profile"
         :class "flex items-center gap-2"}
  [:ty-icon {:name "user" :size "sm"}]
  "Profile"]
  
 [:span {:slot "label-notifications"
         :class "flex items-center gap-2"}
  [:ty-icon {:name "bell" :size "sm"}]
  "Notifications"
  ;; Badge for notifications count
  [:span.ty-bg-danger.ty-text-danger++.px-2.py-0.5.rounded-full.text-xs.font-bold "5"]]
  
 ;; Tab panels match IDs
 [:ty-tab {:id "profile"}
  [:div.p-6 "Profile content"]]
  
 [:ty-tab {:id "notifications"}
  [:div.p-6 "Notifications content"]]]
```

### Custom Active Indicator (Marker)

Customize the active tab indicator using the `marker` slot:

```clojure
;; Underline marker (default is 2px primary underline)
[:ty-tabs {:width "100%" :height "400px"}
 [:div {:slot "marker"
        :style {:height "2px"
                :background "var(--ty-color-primary)"
                :position "absolute"
                :bottom "0"}}]
 [:ty-tab {:id "tab1" :label "Dashboard"}
  [:div.p-6 "Content"]]]

;; Pill-style marker
[:ty-tabs {:width "100%" :height "400px"}
 [:div.ty-bg-primary.rounded-full.shadow-sm {:slot "marker"}]
 [:ty-tab {:id "tab1" :label "Home"}
  [:div.p-6 "Content"]]]

;; No marker (clean look)
[:ty-tabs {:width "100%" :height "400px"}
 ;; Simply don't include a marker slot
 [:ty-tab {:id "tab1" :label "Settings"}
  [:div.p-6 "Content"]]]
```

### Event Handling

```clojure
(defn handle-tab-change [event]
  (let [detail (.-detail event)
        active-id (.-activeId detail)
        active-index (.-activeIndex detail)
        previous-id (.-previousId detail)
        previous-index (.-previousIndex detail)]
    (swap! state assoc :current-tab active-id)
    ;; Optional: save state, track analytics, etc.
    (println "Tab changed:" previous-id "->" active-id)))

[:ty-tabs
 {:width "100%"
  :height "600px"
  :on {:ty-tab-change handle-tab-change}}
 ...]
```

### Styling with CSS Variables

```clojure
[:ty-tabs
 {:width "100%"
  :height "500px"
  :style {;; Tray styling
          :--ty-tabs-bg "var(--ty-surface-elevated)"
          :--ty-tabs-border-width "0"
          
          ;; Button styling
          :--ty-tabs-button-padding "8px 16px"
          :--ty-tabs-button-gap "12px"
          
          ;; Hover state
          :--ty-tabs-button-hover-bg "var(--ty-surface-elevated)"
          
          ;; Animation
          :--transition-duration "200ms"}}
 ...]
```

### User Settings Example

Perfect for organizing settings into sections:

```clojure
(defn settings-view []
  (let [current-tab (get-in @state [:settings :current-tab] "profile")]
    [:div.ty-content.p-6
     [:h1.text-2xl.font-bold.ty-text++.mb-6 "User Settings"]
     
     [:ty-tabs
      {:width "100%"
       :height "calc(100vh - 200px)"
       :active current-tab
       :class "ty-elevated"
       :on {:ty-tab-change handle-settings-tab-change}}
      
      ;; Tab labels with icons
      [:span {:slot "label-profile"
              :class "flex items-center gap-2"}
       [:ty-icon {:name "user" :size "sm"}]
       "Profile"]
       
      [:span {:slot "label-security"
              :class "flex items-center gap-2"}
       [:ty-icon {:name "shield" :size "sm"}]
       "Security"]
       
      [:span {:slot "label-preferences"
              :class "flex items-center gap-2"}
       [:ty-icon {:name "settings" :size "sm"}]
       "Preferences"]
      
      ;; Tab panels with forms
      [:ty-tab {:id "profile"}
       [:div.p-6.space-y-4
        [:ty-input {:label "Full Name" :value "..."}]
        [:ty-input {:label "Email" :value "..."}]
        [:ty-button {:flavor "primary"} "Save Changes"]]]
        
      [:ty-tab {:id "security"}
       [:div.p-6.space-y-4
        [:ty-input {:type "password" :label "Current Password"}]
        [:ty-input {:type "password" :label "New Password"}]
        [:ty-button {:flavor "primary"} "Update Password"]]]
        
      [:ty-tab {:id "preferences"}
       [:div.p-6.space-y-4
        [:ty-dropdown {:label "Theme" :value "light"}
         [:ty-dropdown-option {:value "light"} "Light"]
         [:ty-dropdown-option {:value "dark"} "Dark"]]
        [:ty-button {:flavor "primary"} "Save Preferences"]]]]]))
```

### Best Practices

**DO:**
- ✅ Always specify both `width` and `height`
- ✅ Use semantic IDs (e.g., "settings", "profile")
- ✅ Keep labels concise (1-3 words)
- ✅ Limit to 3-7 tabs for best UX
- ✅ Use icons to improve scannability
- ✅ Add badges for notification counts

**DON'T:**
- ❌ Don't use more than 7 tabs (causes overflow)
- ❌ Don't use long, multi-word labels
- ❌ Don't nest tabs within tabs
- ❌ Avoid tabs with dramatically different content heights

### Attributes Reference

**ty-tabs:**
- `width` (required) - Content area width (px or %)
- `height` (required) - Total container height (px)
- `active` - ID of initially active tab (defaults to first)
- `placement` - Tab button position: "top" or "bottom" (default: "top")

**ty-tab:**
- `id` (required) - Unique tab identifier
- `label` - Simple text label
- `disabled` - Disable tab interaction (boolean)

**Events:**
- `ty-tab-change` - Fires when active tab changes
  - Detail: `{activeId, activeIndex, previousId, previousIndex}`

### Alternatives for More Options

If you need more than 7 options, use:
- **Sidebar Navigation** - For 10+ hierarchical items
- **Accordion Menu** - For collapsible grouped sections
- **Dropdown Select** - For choosing from many options
- **Wizard/Stepper** - For sequential multi-step flows

---

---

## Common Pitfalls

### 1. **Don't Mix Color Systems**

```clojure
;; ❌ BAD: Mixing Tailwind and Ty colors
[:div.bg-blue-500.ty-text]
[:div.ty-bg-primary.text-red-600]

;; ✅ GOOD: Use ty for all colors
[:div.ty-bg-primary.ty-text++]
```

### 2. **Don't Add Margins to Icons in Buttons**

```clojure
;; ❌ BAD: Icon has margin
[:button.flex.items-center
 [:ty-icon.mr-3 {:name "save"}]
 "Save"]

;; ✅ GOOD: Use gap on container
[:button.flex.items-center.gap-2
 [:ty-icon {:name "save"}]
 "Save"]
```

### 3. **Always Use get-in for Nested State**

```clojure
;; ❌ BAD: Fragile access
(:email (:form-data (:user-profile @state)))

;; ✅ GOOD: Safe nested access
(get-in @state [:user-profile :form-data :email])
```

### 4. **Don't Forget to Mark Fields as Touched**

```clojure
;; ❌ BAD: Show errors immediately
[:ty-input
 {:error (errors :email)}]  ; Shows before user interaction

;; ✅ GOOD: Only show after touch
[:ty-input
 {:error (when (touched :email) (errors :email))}]
```

### 5. **Handle Event Objects Correctly**

```clojure
;; ❌ BAD: Assuming structure
(defn handle-change [e]
  (let [value (.-value e)]  ; Wrong!
    ...))

;; ✅ GOOD: Access detail object
(defn handle-change [e]
  (let [value (-> e .-detail .-value)]  ; Correct!
    ...))
```

### 6. **Don't Mutate State Directly**

```clojure
;; ❌ BAD: Direct mutation
(let [data (get-in @state [:form :data])]
  (assoc data :email "new@email.com")  ; Doesn't update state!
  ...)

;; ✅ GOOD: Use swap! or reset!
(swap! state assoc-in [:form :data :email] "new@email.com")
```

### 7. **Always Prevent Default on Form Submit**

```clojure
;; ❌ BAD: Missing preventDefault
(defn handle-submit [event]
  (swap! state assoc :submitting true))  ; Page will reload!

;; ✅ GOOD: Prevent default behavior
(defn handle-submit [event]
  (.preventDefault event)
  (swap! state assoc :submitting true))
```

### 8. **Don't Use Ty Classes for Layout**

```clojure
;; ❌ BAD: Trying to use ty for everything
[:div.ty-flex.ty-gap-4]  ; These don't exist!

;; ✅ GOOD: Use Tailwind for layout
[:div.flex.gap-4.p-6.rounded-lg]
```

---

## Performance Tips

### 1. **Minimize Re-renders**

```clojure
;; ✅ GOOD: Only watch what changes
(defn user-card []
  (let [user (get-in @state [:users :current])]  ; Specific path
    [:div.ty-elevated
     [:h3 (:name user)]]))

;; ❌ BAD: Dereferencing entire state
(defn user-card []
  (let [all-state @state]  ; Triggers on ANY state change
    [:div.ty-elevated
     [:h3 (get-in all-state [:users :current :name])]]))
```

### 2. **Use Keys in Lists**

```clojure
;; ✅ GOOD: Stable keys
[:div
 (for [user users]
   ^{:key (:id user)}
   [:div.ty-elevated
    [:p (:name user)]])]

;; ❌ BAD: No keys (inefficient reconciliation)
[:div
 (for [user users]
   [:div.ty-elevated
    [:p (:name user)]])]
```

### 3. **Lazy Load Large Lists**

```clojure
;; For large datasets, consider pagination or virtual scrolling
(defn user-list []
  (let [page (get-in @state [:users :page] 0)
        users (get-in @state [:users :data])
        visible-users (take 20 (drop (* page 20) users))]
    [:div
     (for [user visible-users]
       ^{:key (:id user)}
       (user-card user))]))
```

### 4. **Debounce Search Inputs**

```clojure
(defn handle-search-input [event]
  (let [value (-> event .-detail .-value)]
    (swap! state assoc :search-query value)
    
    ;; Cancel previous timeout
    (when-let [timeout (:search-timeout @state)]
      (js/clearTimeout timeout))
    
    ;; Set new timeout
    (let [timeout (js/setTimeout
                    #(perform-search value)
                    300)]
      (swap! state assoc :search-timeout timeout))))
```

---

## Testing Patterns

### Component Testing

```clojure
(ns my-app.views.user-profile-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [my-app.views.user-profile :as profile]
            [my-app.state :as state]))

(deftest validation-test
  (testing "Email validation"
    (is (nil? (profile/validate-field :email "test@example.com")))
    (is (some? (profile/validate-field :email "invalid")))
    (is (some? (profile/validate-field :email ""))))
  
  (testing "Phone validation"
    (is (nil? (profile/validate-field :phone "+1234567890")))
    (is (some? (profile/validate-field :phone "123")))))

(deftest form-submission-test
  (testing "Form submit with valid data"
    (reset! state/state {:user-profile {:form-data {:email "test@example.com"
                                                     :name "Test User"}}})
    (let [errors (profile/validate-form
                   (get-in @state/state [:user-profile :form-data]))]
      (is (empty? errors)))))
```

### Integration Testing with Playwright

```clojure
;; In your ClojureScript test
(deftest user-flow-test
  (testing "Complete user profile flow"
    ;; Setup
    (reset! state/state {})
    
    ;; Simulate user interaction
    (profile/handle-field-change :email)
    (let [event (js/CustomEvent. "ty-input-change"
                                 #js {:detail #js {:value "test@example.com"}})]
      ((profile/handle-field-change :email) event))
    
    ;; Assert state
    (is (= "test@example.com"
           (get-in @state/state [:user-profile :form-data :email])))))
```

---

## Quick Reference Cheatsheet

### Styling
- **Colors**: Use `ty-bg-*`, `ty-text-*`, `ty-border-*`
- **Layout**: Use Tailwind (`flex`, `grid`, `p-4`, `gap-2`)
- **Surfaces**: `ty-elevated`, `ty-content`, `ty-canvas`, `ty-floating`

### State
- **Access**: `(get-in @state [:path :to :value])`
- **Update**: `(swap! state assoc-in [:path] value)`
- **Transform**: `(swap! state update-in [:path] fn)`

### Events
- **Access Detail**: `(-> event .-detail .-value)`
- **Prevent Default**: `(.preventDefault event)`
- **Stop Propagation**: `(.stopPropagation event)`

### Routing
- **Navigate**: `(router/navigate! ::route-id)`
- **Check Active**: `(router/rendered? ::route-id true)`
- **Get Path**: `(router/component-path tree ::route-id)`

### Icons
- **Register**: `(icons/register! {:name lucide/icon})`
- **Async Register**: `(icons/register-async! {:name lucide/icon})`
- **Use**: `[:ty-icon {:name "icon-name" :size "sm"}]`
- **In Buttons**: Use `gap-2` on button, not margin on icon

### Forms
- **Input**: `[:ty-input {:type "text" :value "" :on {:ty-input-change fn}}]`
- **Dropdown**: `[:ty-dropdown {:value "" :on {:ty-dropdown-change fn}}]`
- **Calendar**: `[:ty-calendar {:value date :on {:ty-calendar-change fn}}]`
- **Tabs**: `[:ty-tabs {:width "100%" :height "600px" :active "tab1"}]`

---

## Additional Resources

- **CSS Guide**: See `CSS_GUIDE.md` for complete styling reference
- **Project Summary**: See `PROJECT_SUMMARY.md` for architecture overview
- **Component Docs**: Check `site/ty/site/docs/` for component documentation
- **Examples**: Review `site/ty/site/views/` for real-world patterns

---

## 🎯 Summary

**Golden Rules:**
1. ✅ Ty for colors, Tailwind for everything else
2. ✅ Use `get-in`/`assoc-in` for nested state
3. ✅ Access event details via `.-detail` property
4. ✅ Use `gap` for spacing, not margins on icons
5. ✅ Always prevent default on form submissions
6. ✅ Mark fields as touched before showing errors
7. ✅ Register icons before using them
8. ✅ Use keys in lists for performance
9. ✅ Keep views pure, logic in separate functions
10. ✅ Test validation logic independently

This guide will evolve as new patterns emerge. When in doubt, check the existing code in `site/ty/site/views/` for proven patterns!
