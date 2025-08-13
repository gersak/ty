(ns ty.core
  (:require
    [ty.components.button]
    [ty.components.icon]
    [ty.fav6.brands :as brand]
    [ty.fav6.solid :as solid]
    [ty.icons :as icons]))


(defn init []
  (js/console.log "Ty Web Components Library Initialized")
  ;; Components are auto-registered when their namespaces are required
  (icons/set! {"add" solid/add
               "loading" solid/add
               "alert" solid/exclamation
               "star" brand/rebel}))
