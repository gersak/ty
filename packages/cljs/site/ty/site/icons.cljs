(ns ty.site.icons
  "Icon registration for the site application using window.tyIcons.register API"
  (:require [ty.fav6.brands :as fav6-brands]
            [ty.lucide :as lucide]))


(def clojure
  "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"800px\" height=\"800px\" viewBox=\"0 0 32 32\"><title>file_type_clojure</title><path d=\"M16,2A14,14,0,1,0,30,16,14.016,14.016,0,0,0,16,2\" style=\"fill:#fff\"/><path d=\"M15.488,16.252c-.126.273-.265.579-.408.9A22.963,22.963,0,0,0,13.8,20.605a5.181,5.181,0,0,0-.119,1.155c0,.174.009.356.024.542a6.658,6.658,0,0,0,4.413.067,3.966,3.966,0,0,1-.44-.466c-.9-1.146-1.4-2.827-2.194-5.652\" style=\"fill:#91dc47\"/><path d=\"M12.169,10.556a6.677,6.677,0,0,0-.077,10.881c.411-1.71,1.44-3.276,2.983-6.415-.092-.252-.2-.527-.313-.817a10.207,10.207,0,0,0-1.6-2.882,4.439,4.439,0,0,0-1-.767\" style=\"fill:#91dc47\"/><path d=\"M21.84,23.7a10.877,10.877,0,0,1-2.257-.471A8.036,8.036,0,0,1,10.716,9.982a5.9,5.9,0,0,0-1.4-.171c-2.358.022-4.848,1.327-5.884,4.852a6.606,6.606,0,0,0-.074,1.361,12.649,12.649,0,0,0,23,7.274,14.737,14.737,0,0,1-3.448.459A8.881,8.881,0,0,1,21.84,23.7\" style=\"fill:#63b132\"/><path d=\"M19.463,21.244a3.53,3.53,0,0,0,.5.172A6.69,6.69,0,0,0,22.7,16.023h0a6.681,6.681,0,0,0-8.79-6.348c1.358,1.548,2.011,3.761,2.643,6.181v0s.2.673.547,1.562a15.434,15.434,0,0,0,1.363,2.788,2.924,2.924,0,0,0,1,1.036\" style=\"fill:#90b4fe\"/><path d=\"M16.013,3.372A12.632,12.632,0,0,0,5.731,8.656a6.425,6.425,0,0,1,3.48-1.009,6.8,6.8,0,0,1,3.182.772c.134.077.261.16.386.246a8.038,8.038,0,0,1,11.273,7.358h0a8.013,8.013,0,0,1-2.391,5.719,9.871,9.871,0,0,0,1.143.064,6.24,6.24,0,0,0,4.051-1.263,5.348,5.348,0,0,0,1.7-2.906A12.632,12.632,0,0,0,16.013,3.372\" style=\"fill:#5881d8\"/></svg>")

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
                     "clojure" clojure

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
                     "check-square" lucide/check-square
                     "plus" lucide/plus
                     "minus" lucide/minus
                     "edit" lucide/edit
                     "edit-3" lucide/edit-3
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

                     "inputs" lucide/notebook-pen
                    ;; Development and WIP icons
                     "hammer" lucide/hammer
                     "wrench" lucide/wrench
                     "bug" lucide/bug
                     "git-branch" lucide/git-branch
                     "users" lucide/users}))
    (do
      (.warn js/console "⚠️ window.tyIcons not available yet, retrying in 50ms...")
      (js/setTimeout register-icons! 10))))


(register-icons!)
