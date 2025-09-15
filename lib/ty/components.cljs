(ns ty.components
  "Development entry point - direct component loading for fast dev cycles"
  (:require
    [ty.components.button :as button]
    [ty.components.calendar :as calendar]
    [ty.components.calendar-month :as calendar-month]
    [ty.components.calendar-navigation :as calendar-navigation]
    ;; All component configurations
    [ty.components.core]
    [ty.components.date-picker :as date-picker]
    [ty.components.dropdown :as dropdown]
    [ty.components.icon :as icon]
    [ty.components.input :as input]
    [ty.components.modal :as modal]
    [ty.components.multiselect :as multiselect]
    [ty.components.option :as option]
    [ty.components.popup :as popup]
    [ty.components.resize-observer :as resize-observer]
    [ty.components.tag :as tag]
    [ty.components.tooltip :as tooltip]

   ;; Include i18n namespaces
    [ty.i18n :as i18n]

    [ty.i18n.keyword]
    [ty.i18n.number]
    [ty.i18n.string]
    [ty.i18n.time]
   ;; Web component shim
    [ty.shim :as wcs]))

;; =============================================================================
;; Direct Component Registration (Development Mode)
;; =============================================================================

;; Register all components directly using their configurations
(wcs/define! "ty-button" button/configuration)
(wcs/define! "ty-option" option/configuration)
(wcs/define! "ty-tag" tag/configuration)
(wcs/define! "ty-tooltip" tooltip/configuration)
(wcs/define! "ty-popup" popup/configuration)
(wcs/define! "ty-icon" icon/configuration)
(wcs/define! "ty-modal" modal/configuration)
(wcs/define! "ty-input" input/configuration)
(wcs/define! "ty-calendar" calendar/configuration)
(wcs/define! "ty-calendar-month" calendar-month/configuration)
(wcs/define! "ty-calendar-navigation" calendar-navigation/configuration)
(wcs/define! "ty-date-picker" date-picker/configuration)
(wcs/define! "ty-dropdown" dropdown/configuration)
(wcs/define! "ty-multiselect" multiselect/configuration)
(wcs/define! "ty-resize-observer" resize-observer/configuration)
