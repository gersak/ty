(ns ty.core
  (:require
    [ty.components.button]
    [ty.components.icon]
    [ty.components.popup]
    [ty.components.tooltip]
    ;; Include i18n namespaces
    [ty.i18n :as i18n]
    [ty.i18n.keyword]))

(defn init []
  (js/console.log "Ty Web Components Library Initialized")
  ;; Components are auto-registered when their namespaces are required
  ;; i18n is ready to use
  )
