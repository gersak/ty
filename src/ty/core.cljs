(ns ty.core
  (:require
    [ty.components.button]
    [ty.components.icon]
    [ty.fav6.brands :as fa-brands]
    [ty.fav6.solid :as fa-solid]
    [ty.heroicons.mini :as hero-mini]
    [ty.heroicons.outline :as hero-outline]
    [ty.heroicons.solid :as hero-solid]
    [ty.icons :as icons]
    [ty.lucide :as lucide]
    [ty.material.filled :as mat-filled]
    [ty.material.outlined :as mat-outlined]))

(defn init []
  (js/console.log "Ty Web Components Library Initialized")
  ;; Components are auto-registered when their namespaces are required
  (icons/set! {;; Font Awesome icons
               "add" fa-solid/add
               "loading" fa-solid/add
               "alert" fa-solid/exclamation
               "star" fa-brands/rebel

               ;; Material Icons examples
               "home" mat-filled/home
               "home-outlined" mat-outlined/home
               "settings" mat-filled/settings
               "settings-outlined" mat-outlined/settings
               "search" mat-filled/search
               "search-outlined" mat-outlined/search
               "favorite" mat-filled/favorite
               "favorite-outlined" mat-outlined/favorite
               "delete" mat-filled/delete
               "delete-outlined" mat-outlined/delete
               "edit" mat-filled/edit
               "edit-outlined" mat-outlined/edit
               "check" mat-filled/check
               "check-outlined" mat-outlined/check-circle
               "close" mat-filled/close
               "menu" mat-filled/menu
               "more-vert" mat-filled/more-vert
               "arrow-back" mat-filled/arrow-back
               "arrow-forward" mat-filled/arrow-forward

               ;; Heroicons examples
               "hero-home" hero-outline/home
               "hero-home-solid" hero-solid/home
               "hero-cog" hero-outline/cog
               "hero-cog-solid" hero-solid/cog
               "hero-trash" hero-outline/trash
               "hero-trash-solid" hero-solid/trash
               "hero-pencil" hero-outline/pencil
               "hero-pencil-solid" hero-solid/pencil
               "hero-check-mini" hero-mini/check
               "hero-x-mini" hero-mini/x-mark

               ;; Lucide examples
               "lucide-home" lucide/home
               "lucide-settings" lucide/settings
               "lucide-search" lucide/search
               "lucide-heart" lucide/heart
               "lucide-trash" lucide/trash-2
               "lucide-edit" lucide/edit
               "lucide-check" lucide/check
               "lucide-x" lucide/x
               "lucide-menu" lucide/menu
               "lucide-more-vertical" lucide/more-vertical}))
