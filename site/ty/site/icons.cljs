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
             "palette" lucide/palette
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

             ;; Form field icons
             "building" lucide/building
             "file-text" lucide/file-text

               ;; Status and feedback icons
             "check-circle" lucide/check-circle
             "alert-triangle" lucide/alert-triangle
             "alert-circle" lucide/alert-circle
             "circle" lucide/circle
             "minus-circle" lucide/minus-circle
             "info" lucide/info
             "star" lucide/star

;; Additional utility icons
             "search" lucide/search
             "settings" lucide/settings
             "globe" lucide/globe
             "clock" lucide/clock

             ;; Contact form icons
             "refresh-ccw" lucide/refresh-ccw
             "loader-2" lucide/loader-2
             "send" lucide/send
             "phone" lucide/phone
             "map-pin" lucide/map-pin
             "briefcase" lucide/briefcase
             "life-buoy" lucide/life-buoy
             "credit-card" lucide/credit-card
             "handshake" lucide/handshake})
