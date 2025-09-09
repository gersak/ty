(ns ty.dev
  "Development entry point - direct component loading for fast dev cycles"
  (:require
   ;; All component configurations
   [ty.components.button :as button]
   [ty.components.calendar :as calendar]
   [ty.components.calendar-month :as calendar-month]
   [ty.components.calendar-navigation :as calendar-navigation]
   [ty.components.date-picker :as date-picker]
   [ty.components.dropdown :as dropdown]
   [ty.components.icon :as icon]
   [ty.components.input :as input]
   [ty.components.modal :as modal]
   [ty.components.multiselect :as multiselect]
   [ty.components.navigation :as navigation]
   [ty.components.option :as option]
   [ty.components.popup :as popup]
   [ty.components.resize-observer :as resize-observer]
   [ty.components.tag :as tag]
   [ty.components.tooltip :as tooltip]

   ;; Web component shim
   [ty.shim :as wcs]

   ;; Include i18n namespaces
   [ty.i18n :as i18n]
   [ty.i18n.keyword]
   [ty.i18n.number]
   [ty.i18n.string]
   [ty.i18n.time]))

;; =============================================================================
;; Direct Component Registration (Development Mode)
;; =============================================================================

;; Register all components directly using their configurations
(wcs/define! "ty-button" button/configuration)
(wcs/define! "ty-calendar" calendar/configuration)
(wcs/define! "ty-calendar-month" calendar-month/configuration)
(wcs/define! "ty-calendar-navigation" calendar-navigation/configuration)
(wcs/define! "ty-date-picker" date-picker/configuration)
(wcs/define! "ty-dropdown" dropdown/configuration)
(wcs/define! "ty-icon" icon/configuration)
(wcs/define! "ty-input" input/configuration)
(wcs/define! "ty-modal" modal/configuration)
(wcs/define! "ty-multiselect" multiselect/configuration)
(wcs/define! "ty-option" option/configuration)
(wcs/define! "ty-popup" popup/configuration)
(wcs/define! "ty-resize-observer" resize-observer/configuration)
(wcs/define! "ty-tag" tag/configuration)
(wcs/define! "ty-tooltip" tooltip/configuration)

(defn init []
  (js/console.log "🛠️ Ty Web Components Library Initialized (Development Mode)")
  (js/console.log "📦 All components loaded directly - no lazy loading")

  ;; Load navigation styles (CSS only component)
  (navigation/load-navigation-styles!)

  ;; Load default datepicker translations
  (ty.i18n.string/load-datepicker-translations!)

  ;; Development mode features
  (when goog.DEBUG
    (js/console.log "🚀 Development mode: Fast reloads, direct component loading")))
