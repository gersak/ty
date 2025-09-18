(ns ty.components.core
  (:require
   ;; Building block components (direct loading - lightweight, used everywhere)
   [ty.components.button :as button]
   [ty.components.icon :as icon]
   [ty.components.input :as input]
   [ty.components.modal :as modal]
   [ty.components.option :as option]
   [ty.components.popup :as popup]
   [ty.components.resize-observer :as resize-observer]
   [ty.components.tag :as tag]
   [ty.components.textarea :as textarea]
   [ty.components.tooltip :as tooltip]
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
(wcs/define! "ty-textarea" textarea/configuration)
(wcs/define! "ty-tooltip" tooltip/configuration)
