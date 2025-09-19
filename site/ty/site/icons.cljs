(ns ty.site.icons
  "Icon registration for the site application"
  (:require [ty.fav6.brands :as fav6-brands]
            [ty.icons :as icons]
            [ty.lucide :as lucide]))

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
             "refresh" lucide/refresh-cw
             "send" lucide/send
             "phone" lucide/phone
             "map-pin" lucide/map-pin
             "briefcase" lucide/briefcase
             "life-buoy" lucide/life-buoy
             "credit-card" lucide/credit-card
             "handshake" lucide/handshake
             "file-code" lucide/file-code

             ;; MISSING ICONS - Adding from usage analysis
             ;; Programming/Tech icons
             "code" lucide/code ; Code snippets, HTML/Backend section
             "atom" lucide/atom ; React/JS section (may need to use zap or activity if atom doesn't exist)

             ;; Development workflow icons  
             "zap" lucide/zap ; Alternative for atom if needed
             "activity" lucide/activity ; Alternative for technical sections
             "function" lucide/square-function ; Alternative for lambda if needed
             "brackets" lucide/brackets ; Code-related alternative
             "terminal" lucide/terminal ; Terminal/command line
             "box" lucide/box
             "package" lucide/package
             "sparkles" lucide/sparkles
             "layers" lucide/layers
             "book-open" lucide/book-open
             "trash" lucide/trash

             ;; Missing utility icons from user profile
             "external-link" lucide/external-link
             "eye" lucide/eye
             "eye-off" lucide/eye-off
             "lock" lucide/lock
             "unlock" lucide/unlock
             "shield" lucide/shield
             "key" lucide/key
             "brush" lucide/brush
             "map" lucide/map-pin

             ;; Copy functionality
             "copy" lucide/copy ; Copy to clipboard icon
             "arrow-right" lucide/arrow-right ; Navigation arrows used in landing page
             "lambda" lucide/square-function

             ;; Framework icons from FontAwesome
             "react" fav6-brands/react ; React brand icon
             "python" fav6-brands/python ; Python brand icon
             "node-js" fav6-brands/node-js}) ; Node.js brand icon
