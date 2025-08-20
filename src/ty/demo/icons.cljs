(ns ty.demo.icons
  "Icon registration for the demo application"
  (:require
    [ty.fav6.brands :as fa-brands]
    [ty.fav6.regular :as fa-regular]
    [ty.fav6.solid :as fa-solid]
    [ty.heroicons.mini :as hero-mini]
    [ty.heroicons.outline :as hero-outline]
    [ty.heroicons.solid :as hero-solid]
    [ty.icons :as icons]
    [ty.lucide :as lucide]
    [ty.material.filled :as mat-filled]
    [ty.material.outlined :as mat-outlined]))

(defn register-demo-icons!
  "Register all icons used in the demo application"
  []
  (icons/add! {;; Core UI icons (used throughout the demo)
               "home" mat-filled/home
               "settings" mat-filled/settings
               "search" mat-filled/search
               "check" mat-filled/check
               "close" mat-filled/close
               "menu" mat-filled/menu
               "star" fa-solid/star
               "user" fa-solid/user
               "bell" fa-regular/bell
               "alert" fa-solid/exclamation
               "x" lucide/x
               "plus" lucide/plus
               "shield" lucide/shield
               "save" lucide/save

               ;; Theme icons
               "moon" lucide/moon
               "sun" lucide/sun

               ;; Navigation icons
               "click" lucide/mouse-pointer-click
               "type" lucide/type ; Icon for Inputs
               "image" lucide/image
               "palette" lucide/palette
               "square" lucide/square
               "message-square" lucide/message-square
               "hash" lucide/hash ; Icon for Formatting
               "layout" lucide/layout ; Icon for Layout

               ;; Demo icons
               "loading" fa-solid/spinner
               "download" lucide/download
               "heart" lucide/heart
               "trash-2" lucide/trash-2

               ;; Additional icons for icon gallery
               "add" fa-solid/add
               "envelope" fa-regular/envelope
               "fa-github" fa-brands/github
               "fa-twitter" fa-brands/twitter

               ;; Material Icons examples
               "home-outlined" mat-outlined/home
               "settings-outlined" mat-outlined/settings
               "search-outlined" mat-outlined/search
               "favorite" mat-filled/favorite
               "favorite-outlined" mat-outlined/favorite
               "delete" mat-filled/delete
               "delete-outlined" mat-outlined/delete
               "edit" mat-filled/edit
               "edit-outlined" mat-outlined/edit
               "check-outlined" mat-outlined/check-circle
               "more-vert" mat-filled/more-vert
               "arrow-back" mat-filled/arrow-back
               "arrow-forward" mat-filled/arrow-forward
               "arrow-right" mat-filled/arrow-forward
               "add-circle" mat-filled/add-circle
               "add-circle-outlined" mat-outlined/add-circle
               "info" mat-filled/info
               "info-outlined" mat-outlined/info
               "warning" mat-filled/warning
               "warning-outlined" mat-outlined/warning
               "error" mat-filled/error
               "error-outlined" mat-outlined/error
               "done" mat-filled/done
               "done-outlined" mat-outlined/done

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
               "hero-plus" hero-outline/plus
               "hero-plus-solid" hero-solid/plus
               "hero-user" hero-outline/user
               "hero-user-solid" hero-solid/user
               "hero-bell" hero-outline/bell
               "hero-bell-solid" hero-solid/bell
               "hero-star" hero-outline/star
               "hero-star-solid" hero-solid/star
               "hero-heart" hero-outline/heart
               "hero-heart-solid" hero-solid/heart

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
               "lucide-more-vertical" lucide/more-vertical
               "lucide-plus" lucide/plus
               "lucide-minus" lucide/minus
               "lucide-star" lucide/star
               "lucide-user" lucide/user
               "lucide-bell" lucide/bell
               "lucide-mail" lucide/mail
               "lucide-calendar" lucide/calendar
               "lucide-clock" lucide/clock
               "lucide-download" lucide/download
               "lucide-upload" lucide/upload
               "lucide-file" lucide/file
               "lucide-folder" lucide/folder
               "lucide-save" lucide/save
               "lucide-print" lucide/printer
               "lucide-share" lucide/share-2
               "lucide-link" lucide/link
               "lucide-copy" lucide/copy
               "lucide-refresh" lucide/refresh-cw
               "globe" lucide/globe
               "package" lucide/package
               "sliders" lucide/sliders
               "zap" lucide/zap
               "book-open" lucide/book-open
               "github" lucide/github})

  ;; Additional icons for the icon gallery showcase
  (icons/add! {"fa-solid/clock" fa-solid/clock
               "fa-solid/calendar" fa-solid/calendar
               "fa-solid/calendar-days" fa-solid/calendar-days
               "fa-solid/calendar-check" fa-solid/calendar-check
               "fa-solid/calendar-minus" fa-solid/calendar-minus
               "fa-solid/calendar-plus" fa-solid/calendar-plus
               "fa-solid/calendar-xmark" fa-solid/calendar-xmark
               "fa-solid/clock-rotate-left" fa-solid/clock-rotate-left
               "fa-solid/user-clock" fa-solid/user-clock
               "fa-solid/business-time" fa-solid/business-time
               "fa-solid/comment-dots" fa-solid/comment-dots
               "fa-solid/passport" fa-solid/passport
               "fa-solid/socks" fa-solid/socks
               "fa-solid/t-shirt" fa-solid/t-shirt
               "fa-solid/underline" fa-solid/underline

               ;; Font Awesome 6 Regular icons
               "fa-regular/clock" fa-regular/clock
               "fa-regular/calendar" fa-regular/calendar
               "fa-regular/calendar-check" fa-regular/calendar-check
               "fa-regular/comment" fa-regular/comment
               "fa-regular/comment-dots" fa-regular/comment-dots
               "fa-regular/envelope" fa-regular/envelope
               "fa-regular/heart" fa-regular/heart
               "fa-regular/star" fa-regular/star
               "fa-regular/user" fa-regular/user
               "fa-regular/hand" fa-regular/hand
               "fa-regular/file-zipper" fa-regular/file-zipper

               ;; Font Awesome 6 Brands
               "fa-brands/github" fa-brands/github
               "fa-brands/x-twitter" fa-brands/x-twitter
               "fa-brands/square-facebook" fa-brands/square-facebook
               "fa-brands/square-github" fa-brands/square-github
               "fa-brands/square-linkedin" fa-brands/square-linkedin
               "fa-brands/square-x-twitter" fa-brands/square-x-twitter
               "fa-brands/square-twitter" fa-brands/square-twitter

               ;; Material Filled with prefix
               "mat-filled/home" mat-filled/home
               "mat-filled/settings" mat-filled/settings
               "mat-filled/search" mat-filled/search
               "mat-filled/favorite" mat-filled/favorite
               "mat-filled/delete" mat-filled/delete
               "mat-filled/edit" mat-filled/edit
               "mat-filled/check" mat-filled/check
               "mat-filled/close" mat-filled/close
               "mat-filled/menu" mat-filled/menu
               "mat-filled/more-vert" mat-filled/more-vert

               ;; Material Outlined with prefix
               "mat-outlined/home" mat-outlined/home
               "mat-outlined/settings" mat-outlined/settings
               "mat-outlined/search" mat-outlined/search
               "mat-outlined/favorite" mat-outlined/favorite
               "mat-outlined/delete" mat-outlined/delete
               "mat-outlined/edit" mat-outlined/edit
               "mat-outlined/check-circle" mat-outlined/check-circle
               "mat-outlined/info" mat-outlined/info
               "mat-outlined/warning" mat-outlined/warning
               "mat-outlined/error" mat-outlined/error}))
