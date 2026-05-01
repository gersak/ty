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
   [ty.site.docs.scroll-container :as scroll-container-docs]
   [ty.site.docs.tabs :as tabs-docs]
   [ty.site.docs.tag :as tag-docs]
   [ty.site.docs.textarea :as textarea-docs]
   [ty.site.docs.tooltip :as tooltip-docs]
   [ty.site.docs.wizard :as wizard-docs]
    ;; Import component doc namespaces
   [ty.site.views.getting-started :as getting-started]
   [ty.site.views.ty-styles :as ty-styles]))

;; ============================================================================
;; Component picker previews
;; ============================================================================
;; Each preview is a plain hiccup vector rendered inside a bento cell in the
;; component picker. Live ty- elements are used wherever they render cleanly
;; at rest; styled mocks are used for components that have no at-rest form
;; (modal, popup, tooltip) or that need rich children (multiselect, tabs, wizard).
;;
;; Cells are pointer-events: none in the picker, so live components are visual.

;; --- Action ---

(def ^:private button-preview
  [:div.flex.flex-col.gap-3
   {:style {:width "260px"}}
   ;; Labeled buttons with icons
   [:div.flex.flex-wrap.items-center.gap-2
    [:ty-button {:flavor "primary"
                 :size "sm"}
     [:ty-icon {:slot "start"
                :name "save"
                :size "sm"}]
     "Save"]
    [:ty-button {:flavor "neutral"
                 :size "sm"
                 :outlined ""} "Cancel"]
    [:ty-button {:flavor "danger"
                 :size "sm"
                 :pill ""}
     [:ty-icon {:slot "start"
                :name "trash"
                :size "sm"}]
     "Delete"]]
   ;; Action buttons (icon-only, properly sized via :action)
   [:div.flex.flex-wrap.items-center.gap-2
    [:ty-button {:action true
                 :flavor "primary"
                 :size "sm"}
     [:ty-icon {:name "plus"
                :size "sm"}]]
    [:ty-button {:action true
                 :flavor "secondary"
                 :size "sm"}
     [:ty-icon {:name "edit"
                :size "sm"}]]
    [:ty-button {:action true
                 :flavor "danger"
                 :size "sm"}
     [:ty-icon {:name "trash"
                :size "sm"}]]
    [:ty-button {:action true
                 :flavor "primary"
                 :size "sm"}
     [:ty-icon {:name "loader-2"
                :size "sm"
                :spin true}]]
    [:ty-button {:action true
                 :flavor "warning"
                 :size "sm"}
     [:ty-icon {:name "bell"
                :size "sm"
                :pulse true}]]
    [:ty-button {:action true
                 :size "sm"}
     [:ty-icon {:name "more-vertical"
                :size "sm"}]]]])

(def ^:private tag-preview
  [:div.flex.flex-wrap.items-center.gap-2
   {:style {:width "260px"}}
   [:ty-tag {:flavor "primary"
             :pill ""} "react"]
   [:ty-tag {:flavor "success"
             :pill ""} "stable"]
   [:ty-tag {:flavor "warning"
             :pill ""} "beta"]
   [:ty-tag {:flavor "neutral"
             :pill ""
             :dismissible ""} "v1.0"]])

;; --- Text input ---

(def ^:private input-preview
  [:ty-input {:label "Search"
              :placeholder "Find anything…"
              :value "components"
              :style {:width "260px"}}
   [:ty-icon {:slot "start"
              :name "search"
              :size "sm"}]
   [:ty-icon {:slot "end"
              :name "x"
              :size "sm"
              :class "ty-text--"}]])

(def ^:private textarea-preview
  [:ty-textarea {:label "Description"
                 :value "Ty primitives:\n• consistent\n• embeddable\n• unstyled"
                 :style {:width "260px"
                         :min-height "92px"}}])

(def ^:private checkbox-preview
  [:div.flex.flex-col.gap-1
   [:ty-checkbox {:checked ""
                  :flavor "success"} "Done"]
   [:ty-checkbox {:flavor "primary"} "In progress"]
   [:ty-checkbox {:disabled ""} "Disabled"]])

(def ^:private copy-field-preview
  [:div.flex.flex-col.gap-2
   [:ty-copy {:label "Install"
              :value "npm i @gersak/ty"
              :format "code"
              :style {:width "260px"}}]
   [:ty-copy {:label "API key"
              :value "sk_live_••••••••"
              :format "code"
              :style {:width "260px"}}]])

;; --- Selection ---

(def ^:private dropdown-preview
  [:ty-dropdown {:placeholder "Choose…"
                 :value "react"
                 :label "Framework"
                 :style {:width "260px"}}
   [:ty-icon {:slot "start"
              :name "filter"
              :size "sm"}]
   [:ty-option {:value "react"} "React"]
   [:ty-option {:value "vue"} "Vue"]
   [:ty-option {:value "htmx"} "HTMX"]
   [:ty-option {:value "vanilla"} "Vanilla JS"]])

(def ^:private multiselect-preview
  [:ty-multiselect {:placeholder "Pick tags…"
                    :label "Tags"
                    :value "react,clojure"
                    :style {:width "260px"}}
   [:ty-icon {:slot "start"
              :name "tag"
              :size "sm"}]
   [:ty-option {:value "react"} "React"]
   [:ty-option {:value "vue"} "Vue"]
   [:ty-option {:value "clojure"} "Clojure"]
   [:ty-option {:value "htmx"} "HTMX"]])

;; --- Date & time ---

(def ^:private date-picker-preview
  [:ty-date-picker {:label "Pick a date"
                    :value "2026-05-15"
                    :style {:width "260px"}}
   [:ty-icon {:slot "start"
              :name "calendar"
              :size "sm"}]])

(def ^:private calendar-preview
  [:ty-calendar {:size "sm"}])

(def ^:private calendar-month-preview
  [:ty-calendar-month {:size "sm"}])

;; --- Overlays ---
;; Modal/popup/tooltip have no at-rest form on a page, so we stage stylized
;; representations that read as the actual artifact.

(def ^:private modal-preview
  [:div.ty-floating.rounded-lg.flex.flex-col.gap-3.p-4
   {:style {:width "260px"
            :box-shadow "0 12px 32px rgba(0,0,0,0.18)"}}
   [:div.flex.items-center.justify-between
    [:div.text-sm.font-semibold.ty-text "Confirm delete"]
    [:ty-icon {:name "x"
               :size "xs"
               :class "ty-text--"}]]
   [:p.text-xs.ty-text-.leading-relaxed
    "This action can't be undone. Your project will be permanently removed."]
   [:div.flex.justify-end.gap-2.pt-1
    [:div.text-xs.font-medium.px-3.py-1.5.rounded.ty-text- "Cancel"]
    [:div.text-xs.font-medium.px-3.py-1.5.rounded.ty-bg-danger
     {:class "ty-text-danger++"} "Delete"]]])

(def ^:private popup-preview
  [:div.flex.items-start.gap-3
   [:div.flex.items-center.gap-1.text-xs.font-medium.px-2.5.py-1.5.rounded-md.ty-input.border.ty-border
    "Menu"
    [:ty-icon {:name "chevron-down"
               :size "xs"
               :class "ty-text-- ml-1"}]]
   [:div.ty-floating.rounded-md.flex.flex-col.text-xs.py-1
    {:style {:min-width "140px"
             :box-shadow "0 8px 20px rgba(0,0,0,0.12)"}}
    [:div.flex.items-center.gap-2.px-3.py-1.5.ty-text
     [:ty-icon {:name "edit"
                :size "xs"
                :class "ty-text--"}]
     "Edit"]
    [:div.flex.items-center.gap-2.px-3.py-1.5.ty-text
     [:ty-icon {:name "copy"
                :size "xs"
                :class "ty-text--"}]
     "Duplicate"]
    [:div.h-px.ty-bg-neutral-.my-1]
    [:div.flex.items-center.gap-2.px-3.py-1.5.ty-text-danger
     [:ty-icon {:name "trash"
                :size "xs"}]
     "Delete"]]])

(def ^:private tooltip-preview
  [:div.flex.items-center.gap-3
   [:ty-icon {:name "info"
              :size "lg"
              :class "ty-text-"}]
   [:div.relative
    [:div.ty-floating.rounded.px-2.5.py-1.5.text-xs.font-medium.ty-text
     {:style {:box-shadow "0 4px 12px rgba(0,0,0,0.12)"}}
     "Helpful hint"]
    [:div.absolute
     {:style {:left "-5px"
              :top "50%"
              :width 0
              :height 0
              :transform "translateY(-50%)"
              :border-top "5px solid transparent"
              :border-bottom "5px solid transparent"
              :border-right "5px solid var(--ty-surface-floating)"}}]]])

;; --- Layout primitives ---

(def ^:private tabs-preview
  [:div.flex.flex-col
   {:style {:width "260px"}}
   [:div.flex.gap-5.text-sm.border-b.ty-border-
    [:div.pb-2.font-medium.ty-text
     {:style {:border-bottom "2px solid var(--ty-color-primary)"
              :margin-bottom "-1px"}}
     "Overview"]
    [:div.pb-2.ty-text- "Activity"]
    [:div.pb-2.ty-text- "Settings"]]
   [:div.pt-3.text-xs.ty-text--.italic "Tab content here…"]])

(def ^:private wizard-preview
  [:div.flex.items-center.gap-1
   {:style {:width "260px"}}
   ;; Step 1 done
   [:div.flex.flex-col.items-center.gap-1
    [:div.flex.items-center.justify-center.rounded-full.ty-bg-success
     {:style {:width "26px"
              :height "26px"}}
     [:ty-icon {:name "check"
                :size "xs"
                :class "ty-text-success++"}]]
    [:span.text-xs.ty-text- "Plan"]]
   [:div.flex-1.h-px.ty-bg-success {:style {:margin-bottom "16px"}}]
   ;; Step 2 active
   [:div.flex.flex-col.items-center.gap-1
    [:div.flex.items-center.justify-center.rounded-full.ty-bg-primary.text-xs.font-bold
     {:class "ty-text-primary++"
      :style {:width "26px"
              :height "26px"}}
     "2"]
    [:span.text-xs.ty-text.font-medium "Build"]]
   [:div.flex-1.h-px.ty-bg-neutral- {:style {:margin-bottom "16px"}}]
   ;; Step 3 pending
   [:div.flex.flex-col.items-center.gap-1
    [:div.flex.items-center.justify-center.rounded-full.text-xs.font-medium.ty-text--
     {:style {:width "26px"
              :height "26px"
              :border "1px solid var(--ty-border)"}}
     "3"]
    [:span.text-xs.ty-text-- "Ship"]]])

(def ^:private scroll-container-preview
  [:div.relative.rounded-md.overflow-hidden.ty-content
   {:style {:width "260px"
            :height "76px"}}
   [:div.flex.gap-2.h-full.items-center.px-3
    {:style {:padding-top "12px"
             :padding-bottom "12px"}}
    (for [[i flavor] (map-indexed vector ["primary" "success" "warning" "info" "danger" "neutral"])]
      ^{:key i}
      [:div.flex-shrink-0.rounded
       {:class (str "ty-bg-" flavor "-")
        :style {:width "60px"
                :height "100%"}}])]
   ;; Edge fade indicators
   [:div.absolute.left-0.top-0.bottom-0
    {:style {:width "20px"
             :pointer-events "none"
             :background "linear-gradient(to right, var(--ty-surface-content), transparent)"}}]
   [:div.absolute.right-0.top-0.bottom-0
    {:style {:width "32px"
             :pointer-events "none"
             :background "linear-gradient(to left, var(--ty-surface-content), transparent)"}}]])

(def ^:private resize-observer-preview
  [:div.relative.flex.items-center.justify-center.rounded-md.ty-content
   {:style {:width "200px"
            :height "92px"
            :border "1px dashed var(--ty-border)"}}
   [:div.flex.flex-col.items-center.gap-1
    [:div.text-xs.font-mono.ty-text- "200 × 92"]
    [:div.text-xs.ty-text-- "size tracked"]]
   ;; Resize corner handle
   [:div.absolute
    {:style {:right "4px"
             :bottom "4px"
             :width "10px"
             :height "10px"
             :border-right "2px solid var(--ty-color-primary)"
             :border-bottom "2px solid var(--ty-color-primary)"}}]])

;; --- Visual ---

(def ^:private icon-preview
  [:div.flex.flex-wrap.items-center.gap-4
   {:style {:width "260px"}}
   [:ty-icon {:name "star"
              :size "lg"
              :class "ty-text-primary"}]
   [:ty-icon {:name "heart"
              :size "lg"
              :class "ty-text-danger"
              :pulse true}]
   [:ty-icon {:name "loader-2"
              :size "lg"
              :class "ty-text-info"
              :spin true}]
   [:ty-icon {:name "check-circle"
              :size "lg"
              :class "ty-text-success"}]
   [:ty-icon {:name "settings"
              :size "lg"
              :class "ty-text-"
              :spin true
              :tempo "slow"}]
   [:ty-icon {:name "github"
              :size "lg"
              :class "ty-text"}]
   [:ty-icon {:name "bell"
              :size "lg"
              :class "ty-text-warning"
              :pulse true
              :tempo "slow"}]])

;; ============================================================================
;; Components
;; ============================================================================

(def docs-components
  [{:id :ty.site.docs/button
    :segment "button"
    :icon "square"
    :view button-docs/view
    :name "Button"
    :description "7 semantic flavors, sizes xs–xl, start/end icon slots, and modifiers (pill, outlined, filled, plain, action, wide). Action mode for FAB-style icon buttons."
    :tags ["action" "click" "submit" "form"]
    :preview button-preview}
   {:id :ty.site.docs/calendar
    :segment "calendar"
    :icon "calendar"
    :view calendar-docs/view
    :name "Calendar"
    :description "Selectable calendar with month/year navigation. ISO date API, form integration, can be controlled externally."
    :tags ["date" "picker" "schedule" "month"]
    :span [2 2]
    :preview calendar-preview}
   {:id :ty.site.docs/calendar-month
    :segment "calendar-month"
    :icon "calendar"
    :view calendar-month-docs/view
    :name "Calendar Month"
    :description "Stateless 6-week month grid. Monday-first ordering, localized day headers, custom day rendering, sizes sm/md/lg."
    :tags ["date" "month" "grid" "schedule"]
    :span [2 2]
    :preview calendar-month-preview}
   {:id :ty.site.docs/date-picker
    :segment "date-picker"
    :icon "clock"
    :view date-picker-docs/view
    :name "Date Picker"
    :description "Date input with integrated calendar popup. ISO date string value, form participation, locale-aware."
    :tags ["date" "input" "form" "picker" "time"]
    :span [2 1]
    :preview date-picker-preview}
   {:id :ty.site.docs/dropdown
    :segment "dropdown"
    :icon "chevron-down"
    :view dropdown-docs/view
    :name "Dropdown"
    :description "Searchable single-select. Internal filter or external search via event, keyboard navigation, smart popup positioning, mobile fullscreen mode."
    :tags ["select" "options" "form" "menu" "choice"]
    :span [2 1]
    :preview dropdown-preview}
   {:id :ty.site.docs/icon
    :segment "icon"
    :icon "star"
    :view icon-docs/view
    :name "Icon"
    :description "Registry-based SVG icons. Spin and pulse animations with tempo control (slow/normal/fast), semantic colors, sizes xs–xl."
    :tags ["svg" "image" "symbol" "graphic"]
    :preview icon-preview}
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
                :description "Text, currency, percent, integer, and float types — locale-aware numeric formatting. Debounce control, start/end icon slots, label and error messages."
                :tags ["text" "form" "field" "input"]
                :span [2 1]
                :preview input-preview}
               {:id :ty.site.docs/checkbox
                :segment "checkbox"
                :icon "check-square"
                :view checkbox-docs/view
                :name "Checkbox"
                :description "Boolean toggle with semantic flavors, required indicator, error messages, and custom labels."
                :tags ["toggle" "boolean" "form" "check"]
                :preview checkbox-preview}
               {:id :ty.site.docs/copy-field
                :segment "copy-field"
                :icon "copy"
                :view copy-field-docs/view
                :name "Copy Field"
                :description "Read-only with copy-to-clipboard. Animated icon feedback (copy → check), code or text formatting modes."
                :tags ["clipboard" "copy" "readonly"]
                :span [2 1]
                :preview copy-field-preview}
               {:id :ty.site.docs/textarea
                :segment "textarea"
                :icon "file-text"
                :view textarea-docs/view
                :name "Textarea"
                :description "Auto-resizing with min/max height, custom scrollbar, resize control (none, both, horizontal, vertical), label and error messages."
                :tags ["text" "multiline" "form" "input"]
                :span [2 1]
                :preview textarea-preview}]}
   {:id :ty.site.docs/modal
    :segment "modal"
    :icon "layout"
    :view modal-docs/view
    :name "Modal"
    :description "Native `<dialog>` overlay with scroll locking, backdrop and ESC close, and protected mode for unsaved changes."
    :tags ["dialog" "overlay" "popup" "lightbox"]
    :span [2 2]
    :preview modal-preview}
   {:id :ty.site.docs/multiselect
    :segment "multiselect"
    :icon "filter"
    :view multiselect-docs/view
    :name "Multiselect"
    :description "Multi-value with tag display. Internal filter or external search via event, keyboard navigation, smart positioning, mobile fullscreen mode."
    :tags ["select" "multiple" "tags" "form" "filter"]
    :span [2 1]
    :preview multiselect-preview}
   {:id :ty.site.docs/popup
    :segment "popup"
    :icon "message-square"
    :view popup-docs/view
    :name "Popup"
    :description "Click-triggered floating content with edge-aware positioning. Scroll locking, ESC and backdrop close, manual control mode."
    :tags ["floating" "popover" "overlay" "menu"]
    :preview popup-preview}
   {:id :ty.site.docs/resize-observer
    :segment "resize-observer"
    :icon "maximize"
    :view resize-observer-docs/view
    :name "Resize Observer"
    :description "Tracks its own dimensions in a global registry with debounce support. Used for responsive layouts."
    :tags ["responsive" "size" "layout" "observer"]
    :preview resize-observer-preview}
   {:id :ty.site.docs/scroll-container
    :segment "scroll-container"
    :icon "scroll-text"
    :view scroll-container-docs/view
    :name "Scroll Container"
    :description "Scroll wrapper with edge shadow indicators, custom scrollbar styling, max-height control, and horizontal overflow."
    :tags ["scroll" "overflow" "container" "shadow"]
    :span [2 1]
    :preview scroll-container-preview}
   {:id :ty.site.docs/tabs
    :segment "tabs"
    :icon "layout"
    :view tabs-docs/view
    :name "Tabs"
    :description "Carousel-based tabs with smooth slide animations, animated active marker, and top or bottom placement."
    :tags ["navigation" "panels" "switch" "views"]
    :span [2 1]
    :preview tabs-preview}
   {:id :ty.site.docs/wizard
    :segment "wizard"
    :icon "list-ordered"
    :view wizard-docs/view
    :name "Wizard"
    :description "Multi-step stepper with progress line, completion tracking, status per step (active/completed/error/disabled), horizontal or vertical."
    :tags ["steps" "stepper" "form" "workflow" "carousel"]
    :span [2 2]
    :preview wizard-preview}
   {:id :ty.site.docs/tag
    :segment "tag"
    :icon "tag"
    :view tag-docs/view
    :name "Tag"
    :description "Pills and chips with click and dismiss handlers. Semantic flavors, sizes, selected state, keyboard accessible (Enter/Backspace)."
    :tags ["label" "badge" "chip" "category"]
    :preview tag-preview}
   {:id :ty.site.docs/tooltip
    :segment "tooltip"
    :icon "message-square"
    :view tooltip-docs/view
    :name "Tooltip"
    :description "Hover and focus tooltips via the Popover API. Smart positioning, configurable delay, dark/light/semantic flavors."
    :tags ["hint" "help" "hover" "info"]
    :preview tooltip-preview}])

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
