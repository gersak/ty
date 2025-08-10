(ns ty.core
  (:require [ty.components.button]
            [ty.components.button-v2]
            [ty.components.button-v3]
            [ty.components.button-v4]))

(defn init []
  (js/console.log "Ty Web Components Library Initialized")
  ;; Components are auto-registered when their namespaces are required
  )
