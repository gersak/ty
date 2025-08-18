(ns ty.core
  (:require
    [ty.components.button]
    [ty.components.icon]
    [ty.components.popup]
    [ty.components.tooltip]))

(defn init []
  (js/console.log "Ty Web Components Library Initialized")
  ;; Components are auto-registered when their namespaces are required
  )
