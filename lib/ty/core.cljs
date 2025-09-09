(ns ty.core
  (:require
   ;; Building block components (direct loading - lightweight, used everywhere)
    [ty.components.button :as button]
    [ty.components.icon :as icon]
    [ty.components.input :as input]
    [ty.components.modal :as modal]
    [ty.components.navigation :as navigation]
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
    ;; Lazy component imports - FINAL PRODUCTS ONLY (heavy/complex components)
    [ty.lazy.calendar]

    [ty.lazy.calendar-month]
    [ty.lazy.date-picker]
    [ty.lazy.dropdown]
    [ty.lazy.multiselect]
    [ty.scroll-prevention]

   ;; Web component shim for direct registrations
    [ty.shim :as wcs]))


;; Register building block components immediately (lightweight, used everywhere)
(wcs/define! "ty-button" button/configuration)
(wcs/define! "ty-icon" icon/configuration)
(wcs/define! "ty-input" input/configuration)
(wcs/define! "ty-modal" modal/configuration)
(wcs/define! "ty-option" option/configuration)
(wcs/define! "ty-popup" popup/configuration)
(wcs/define! "ty-resize-observer" resize-observer/configuration)
(wcs/define! "ty-tag" tag/configuration)
(wcs/define! "ty-tooltip" tooltip/configuration)

;; TODO - remove this
(ty.i18n.string/load-datepicker-translations!)

(defn init []
  (js/console.log "🚀 Ty Web Components Library Initialized (Production Mode)")

  ;; Load navigation styles (CSS only component)
  (navigation/load-navigation-styles!)

  ;; i18n is ready to use with formatting support
  ;; Load default datepicker translations


  ;; Development mode features
  (when goog.DEBUG
    (js/console.log "🔄 Lazy loading enabled for FINAL PRODUCTS: calendar, calendar-month, date-picker, dropdown, multiselect")
    (js/console.log "📦 Direct loading for BUILDING BLOCKS: button, calendar-navigation, icon, input, modal, option, popup, resize-observer, tag, tooltip")))
