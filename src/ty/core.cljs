(ns ty.core
  (:require
   [ty.components.button]
   [ty.components.icon]
   [ty.components.input]
   [ty.components.popup]
   [ty.components.resize-observer]
   [ty.components.tooltip]
     ;; Include i18n namespaces
   [ty.i18n :as i18n]
   [ty.i18n.keyword]
   [ty.i18n.number]
   [ty.i18n.time]))

(defn init []
  (js/console.log "Ty Web Components Library Initialized")
  ;; Components are auto-registered when their namespaces are required
  ;; i18n is ready to use with formatting support
  )
