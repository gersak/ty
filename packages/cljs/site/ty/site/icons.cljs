(ns ty.site.icons
  "Icon registration for the site application using window.tyIcons.register API"
  (:require [ty.fav6.brands :as fav6-brands]
            [ty.lucide :as lucide]))

(def clojure
  "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"800px\" height=\"800px\" viewBox=\"0 0 32 32\"><title>file_type_clojure</title><path d=\"M16,2A14,14,0,1,0,30,16,14.016,14.016,0,0,0,16,2\" style=\"fill:#fff\"/><path d=\"M15.488,16.252c-.126.273-.265.579-.408.9A22.963,22.963,0,0,0,13.8,20.605a5.181,5.181,0,0,0-.119,1.155c0,.174.009.356.024.542a6.658,6.658,0,0,0,4.413.067,3.966,3.966,0,0,1-.44-.466c-.9-1.146-1.4-2.827-2.194-5.652\" style=\"fill:#91dc47\"/><path d=\"M12.169,10.556a6.677,6.677,0,0,0-.077,10.881c.411-1.71,1.44-3.276,2.983-6.415-.092-.252-.2-.527-.313-.817a10.207,10.207,0,0,0-1.6-2.882,4.439,4.439,0,0,0-1-.767\" style=\"fill:#91dc47\"/><path d=\"M21.84,23.7a10.877,10.877,0,0,1-2.257-.471A8.036,8.036,0,0,1,10.716,9.982a5.9,5.9,0,0,0-1.4-.171c-2.358.022-4.848,1.327-5.884,4.852a6.606,6.606,0,0,0-.074,1.361,12.649,12.649,0,0,0,23,7.274,14.737,14.737,0,0,1-3.448.459A8.881,8.881,0,0,1,21.84,23.7\" style=\"fill:#63b132\"/><path d=\"M19.463,21.244a3.53,3.53,0,0,0,.5.172A6.69,6.69,0,0,0,22.7,16.023h0a6.681,6.681,0,0,0-8.79-6.348c1.358,1.548,2.011,3.761,2.643,6.181v0s.2.673.547,1.562a15.434,15.434,0,0,0,1.363,2.788,2.924,2.924,0,0,0,1,1.036\" style=\"fill:#90b4fe\"/><path d=\"M16.013,3.372A12.632,12.632,0,0,0,5.731,8.656a6.425,6.425,0,0,1,3.48-1.009,6.8,6.8,0,0,1,3.182.772c.134.077.261.16.386.246a8.038,8.038,0,0,1,11.273,7.358h0a8.013,8.013,0,0,1-2.391,5.719,9.871,9.871,0,0,0,1.143.064,6.24,6.24,0,0,0,4.051-1.263,5.348,5.348,0,0,0,1.7-2.906A12.632,12.632,0,0,0,16.013,3.372\" style=\"fill:#5881d8\"/></svg>")


(def ty-logo
  "<svg width=\"48\" height=\"24\" viewBox=\"0 0 48 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">
<path d=\"M41.9588 19.2415V15.8812C41.7226 16.652 41.1209 17.0374 40.1537 17.0374H38.956C38.1125 17.0374 37.4939 16.8628 37.1003 16.5135C36.7067 16.1522 36.5099 15.5138 36.5099 14.5985V5.8183H38.5511V15.014C38.5511 15.1826 38.6298 15.2669 38.7873 15.2669H41.9588L41.9419 5.8183H43.9831L44 18.573C44 19.5727 43.7695 20.2833 43.3084 20.7049C42.8585 21.1264 42.1275 21.3372 41.1153 21.3372H36.8473V19.4944H41.7395C41.8857 19.4944 41.9588 19.4101 41.9588 19.2415Z\" fill=\"currentColor\"/>
<path d=\"M35.4451 7.53462H33.0327V14.7972C33.0327 14.9538 33.1058 15.0321 33.252 15.0321H35.4451V16.8748H33.8762C32.864 16.8748 32.1274 16.6641 31.6662 16.2425C31.2164 15.821 30.9915 15.1164 30.9915 14.1288V7.53462H29.7768V5.81833H30.9915V3H33.0327V5.81833H35.4451V7.53462Z\" fill=\"currentColor\"/>
<path d=\"M27.8988 16.8749H25.4476L19.7178 3.5387C19.675 3.43896 19.743 3.32522 19.8455 3.32522H21.717C21.9366 3.32522 22.1361 3.46209 22.2278 3.67571L27.8988 16.8749Z\" fill=\"currentColor\"/>
<path d=\"M27.8988 16.8749H25.4476L19.7178 3.5387C19.675 3.43896 19.743 3.32522 19.8455 3.32522H21.717C21.9366 3.32522 22.1361 3.46209 22.2278 3.67571L27.8988 16.8749Z\" fill=\"currentColor\"/>
<path d=\"M4.00001 16.8749H6.45117L12.1809 3.5387C12.2238 3.43896 12.1558 3.32522 12.0532 3.32522H10.1818C9.9622 3.32522 9.76271 3.46209 9.67093 3.67571L4.00001 16.8749Z\" fill=\"currentColor\"/>
<path d=\"M4.00001 16.8749H6.45117L12.1809 3.5387C12.2238 3.43896 12.1558 3.32522 12.0532 3.32522H10.1818C9.9622 3.32522 9.76271 3.46209 9.67093 3.67571L4.00001 16.8749Z\" fill=\"currentColor\"/>
<path d=\"M24.2221 16.8749H7.67677L11.8131 7.65189C11.9068 7.44316 12.1038 7.3104 12.32 7.3104H19.5788C19.795 7.3104 19.9921 7.44316 20.0857 7.65189L24.2221 16.8749Z\" fill=\"currentColor\"/>
</svg>")


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
                     "ty-logo" ty-logo

                    ;; UI controls
                     "menu"
                     lucide/menu
                     "x"
                     lucide/x
                     "moon"
                     lucide/moon
                     "sun"
                     lucide/sun
                     "chevron-down"
                     lucide/chevron-down
                     "chevron-right"
                     lucide/chevron-right

                    ;; Component/UI icons (for docs)
                     "square"
                     lucide/square
                     "square-stack"
                     lucide/square-stack
                     "list"
                     lucide/list
                     "list-ordered"
                     lucide/list-ordered
                     "message-square"
                     lucide/message-square
                     "hand"
                     lucide/hand
                     "tag"
                     lucide/tag
                     "align-left"
                     lucide/align-left
                     "type"
                     lucide/type
                     "image"
                     lucide/image

                    ;; Form and interaction icons
                     "check"
                     lucide/check
                     "check-square"
                     lucide/check-square
                     "plus"
                     lucide/plus
                     "minus"
                     lucide/minus
                     "edit"
                     lucide/edit
                     "edit-3"
                     lucide/edit-3
                     "save"
                     lucide/save
                     "upload"
                     lucide/upload
                     "download"
                     lucide/download
                     "trash"
                     lucide/trash
                     "trash-2"
                     lucide/trash-2
                     "filter"
                     lucide/filter
                     "more-vertical"
                     lucide/more-vertical

                    ;; Form field icons
                     "building"
                     lucide/building
                     "file-text"
                     lucide/file-text
                     "help-circle"
                     lucide/help-circle

                    ;; Status and feedback icons
                     "check-circle"
                     lucide/check-circle
                     "x-circle"
                     lucide/x-circle
                     "alert-triangle"
                     lucide/alert-triangle
                     "alert-circle"
                     lucide/alert-circle
                     "circle"
                     lucide/circle
                     "minus-circle"
                     lucide/minus-circle
                     "info"
                     lucide/info
                     "info-circle"
                     lucide/circle-alert
                     "star"
                     lucide/star
                     "lightbulb"
                     lucide/lightbulb

                    ;; Additional utility icons
                     "search"
                     lucide/search
                     "settings"
                     lucide/settings
                     "trending-up"
                     lucide/trending-up
                     "globe"
                     lucide/globe
                     "clock"
                     lucide/clock
                     "arrow-left"
                     lucide/arrow-left
                     "arrow-right"
                     lucide/arrow-right
                     "bell"
                     lucide/bell
                     "wifi-off"
                     lucide/wifi-off
                     "database"
                     lucide/database
                     "server"
                     lucide/server
                     "layout"
                     lucide/layout
                     "maximize"
                     lucide/maximize
                     "accessibility"
                     lucide/accessibility

                    ;; Loading and refresh icons
                     "refresh-ccw"
                     lucide/refresh-ccw
                     "refresh-cw"
                     lucide/refresh-cw
                     "loader-2"
                     lucide/loader-2
                     "loader"
                     lucide/loader
                     "send"
                     lucide/send

                    ;; Contact and business icons
                     "phone"
                     lucide/phone
                     "map-pin"
                     lucide/map-pin
                     "briefcase"
                     lucide/briefcase
                     "life-buoy"
                     lucide/life-buoy
                     "credit-card"
                     lucide/credit-card
                     "handshake"
                     lucide/handshake

                    ;; Development/Tech icons
                     "file-code"
                     lucide/file-code
                     "code"
                     lucide/code
                     "terminal"
                     lucide/terminal
                     "package"
                     lucide/package
                     "sparkles"
                     lucide/sparkles
                     "layers"
                     lucide/layers
                     "navigation"
                     lucide/navigation
                     "form-input"
                     lucide/form-input
                     "book-open"
                     lucide/book-open
                     "zap"
                     lucide/zap
                     "lambda"
                     lucide/square-function
                     "atom"
                     lucide/atom

                    ;; Security and visibility icons
                     "external-link"
                     lucide/external-link
                     "eye"
                     lucide/eye
                     "eye-off"
                     lucide/eye-off
                     "lock"
                     lucide/lock
                     "unlock"
                     lucide/unlock
                     "shield"
                     lucide/shield
                     "key"
                     lucide/key

                    ;; Miscellaneous
                     "brush"
                     lucide/brush
                     "copy"
                     lucide/copy
                     "clipboard"
                     lucide/clipboard
                     "rotate-ccw"
                     lucide/rotate-ccw
                     "share-2"
                     lucide/share-2
                     "box"
                     lucide/box
                     "diamond"
                     lucide/diamond-plus

                    ;; Framework icons from FontAwesome
                     "react"
                     fav6-brands/react
                     "python"
                     fav6-brands/python
                     "node-js"
                     fav6-brands/node-js
                     "github"
                     fav6-brands/github

                    ;; Additional icons for why page
                     "swords"
                     lucide/swords

                     "inputs"
                     lucide/notebook-pen
                    ;; Development and WIP icons
                     "hammer"
                     lucide/hammer
                     "wrench"
                     lucide/wrench
                     "bug"
                     lucide/bug
                     "git-branch"
                     lucide/git-branch
                     "users"
                     lucide/users}))
    (do
      (.warn js/console "⚠️ window.tyIcons not available yet, retrying in 50ms...")
      (js/setTimeout register-icons! 10))))

(register-icons!)
