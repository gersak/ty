(ns ty.demo.state)

(defn get-initial-theme []
  (or (.getItem js/localStorage "ty-theme")
      (when (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))
        "dark")
      "light"))

(defonce state
  (atom
    (merge
      {:theme (get-initial-theme)
       :current-page :home
     ;; Popup states for demo
       :popup-states {}}
      #:ty.demo.views.icons {:show-demo true
                             :search-input ""})))
