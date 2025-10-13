(ns ty.site.icons
  "Icon registration for the site application using window.tyIcons.register API"
  (:require [ty.fav6.brands :as fav6-brands]
            [ty.lucide :as lucide]))

(defn register-icons!
  "Register all icons used in the site with the TypeScript icon registry.
   Uses window.tyIcons.register for proper dead code elimination via Closure compiler."
  []
  (if-let [register (some->
                      js/window.tyIcons
                      (.-register))]
    (when (ifn? register)
      (register #js {;; Core navigation icons
                     "home" lucide/home
                     "user" lucide/user
                     "calendar" lucide/calendar
                     "calendar-check" lucide/calendar-check
                     "mail" lucide/mail
                     "palette" lucide/palette
                     "rocket" lucide/rocket

                    ;; UI controls
                     "menu" lucide/menu
                     "x" lucide/x
                     "moon" lucide/moon
                     "sun" lucide/sun
                     "chevron-down" lucide/chevron-down

                    ;; Component/UI icons (for docs)
                     "square" lucide/square
                     "square-stack" lucide/square-stack
                     "list" lucide/list
                     "message-square" lucide/message-square
                     "tag" lucide/tag
                     "align-left" lucide/align-left
                     "type" lucide/type
                     "image" lucide/image

                    ;; Form and interaction icons
                     "check" lucide/check
                     "plus" lucide/plus
                     "minus" lucide/minus
                     "edit" lucide/edit
                     "save" lucide/save
                     "upload" lucide/upload
                     "download" lucide/download
                     "trash" lucide/trash
                     "trash-2" lucide/trash-2
                     "filter" lucide/filter
                     "more-vertical" lucide/more-vertical

                    ;; Form field icons
                     "building" lucide/building
                     "file-text" lucide/file-text
                     "help-circle" lucide/help-circle

                    ;; Status and feedback icons
                     "check-circle" lucide/check-circle
                     "x-circle" lucide/x-circle
                     "alert-triangle" lucide/alert-triangle
                     "alert-circle" lucide/alert-circle
                     "circle" lucide/circle
                     "minus-circle" lucide/minus-circle
                     "info" lucide/info
                     "info-circle" lucide/circle-alert
                     "star" lucide/star
                     "lightbulb" lucide/lightbulb

                    ;; Additional utility icons
                     "search" lucide/search
                     "settings" lucide/settings
                     "globe" lucide/globe
                     "clock" lucide/clock
                     "arrow-right" lucide/arrow-right
                     "bell" lucide/bell
                     "wifi-off" lucide/wifi-off
                     "database" lucide/database
                     "server" lucide/server
                     "layout" lucide/layout
                     "maximize" lucide/maximize
                     "accessibility" lucide/accessibility

                    ;; Loading and refresh icons
                     "refresh-ccw" lucide/refresh-ccw
                     "refresh-cw" lucide/refresh-cw
                     "loader-2" lucide/loader-2
                     "loader" lucide/loader
                     "send" lucide/send

                    ;; Contact and business icons
                     "phone" lucide/phone
                     "map-pin" lucide/map-pin
                     "briefcase" lucide/briefcase
                     "life-buoy" lucide/life-buoy
                     "credit-card" lucide/credit-card
                     "handshake" lucide/handshake

                    ;; Development/Tech icons
                     "file-code" lucide/file-code
                     "code" lucide/code
                     "terminal" lucide/terminal
                     "package" lucide/package
                     "sparkles" lucide/sparkles
                     "layers" lucide/layers
                     "navigation" lucide/navigation
                     "form-input" lucide/form-input
                     "book-open" lucide/book-open
                     "zap" lucide/zap
                     "lambda" lucide/square-function
                     "atom" lucide/atom

                    ;; Security and visibility icons
                     "external-link" lucide/external-link
                     "eye" lucide/eye
                     "eye-off" lucide/eye-off
                     "lock" lucide/lock
                     "unlock" lucide/unlock
                     "shield" lucide/shield
                     "key" lucide/key

                    ;; Miscellaneous
                     "brush" lucide/brush
                     "copy" lucide/copy
                     "clipboard" lucide/clipboard
                     "rotate-ccw" lucide/rotate-ccw
                     "share-2" lucide/share-2
                     "box" lucide/box
                     "diamond" lucide/diamond-plus

                    ;; Framework icons from FontAwesome
                     "react" fav6-brands/react
                     "python" fav6-brands/python
                     "node-js" fav6-brands/node-js
                     "github" fav6-brands/github

                    ;; Additional icons for why page
                     "swords" lucide/swords

                    ;; Development and WIP icons
                     "hammer" lucide/hammer
                     "wrench" lucide/wrench
                     "bug" lucide/bug
                     "git-branch" lucide/git-branch
                     "users" lucide/users}))
    (do
      (.warn js/console "⚠️ window.tyIcons not available yet, retrying in 50ms...")
      (.log js/console "window.tyIcons:" js/window.tyIcons)
      (js/setTimeout register-icons! 50))))
