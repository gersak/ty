(ns ty.site.icons
  "Icon registration for the site application"
  (:require [ty.icons :as icons]
            [ty.lucide :as lucide]
            [ty.material.filled :as mat-filled]))

(icons/add! {;; Core navigation icons
             "home" lucide/home
             "user" lucide/user
             "calendar" lucide/calendar
             "mail" lucide/mail
             "rocket" lucide/rocket

               ;; UI controls
             "menu" lucide/menu
             "x" lucide/x
             "moon" lucide/moon
             "sun" lucide/sun

               ;; Form and interaction icons
             "check" lucide/check
             "plus" lucide/plus
             "minus" lucide/minus
             "edit" lucide/edit
             "save" lucide/save
             "upload" lucide/upload
             "download" lucide/download

               ;; Status and feedback icons
             "check-circle" lucide/check-circle
             "alert-triangle" lucide/alert-triangle
             "info" lucide/info
             "star" lucide/star

;; Additional utility icons
             "search" lucide/search
             "settings" lucide/settings
             "globe" lucide/globe
             "clock" lucide/clock})
