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
    [ty.components.multiselect :as multiselect]

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
(wcs/define! "ty-calendar" calendar/configuration)
(wcs/define! "ty-calendar-month" calendar-month/configuration)
(wcs/define! "ty-calendar-navigation" calendar-navigation/configuration)
(wcs/define! "ty-date-picker" date-picker/configuration)
(wcs/define! "ty-dropdown" dropdown/configuration)
(wcs/define! "ty-multiselect" multiselect/configuration)
