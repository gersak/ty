(ns hello.state
  (:require [reagent.core :as r]))

(defn get-initial-theme []
  (or (.getItem js/localStorage "ty-theme")
      (when (.-matches (.matchMedia js/window "(prefers-color-scheme: dark)"))
        "dark")
      "light"))

(defonce app-state
  (r/atom {:theme (get-initial-theme)
           :current-route :home
           :mobile-menu-open false
           ;; Form data
           :form {:name ""
                  :email ""
                  :phone ""
                  :role ""
                  :skills []
                  :bio ""
                  :birthdate ""
                  :newsletter false}
           :form-errors {}
           :form-submitting false}))

;; Route definitions
(def routes
  {:home {:name "Home" :icon "home"}
   :forms {:name "Forms" :icon "edit"}
   :buttons {:name "Buttons" :icon "click"}
   :components {:name "Components" :icon "grid"}})

(defn navigate! [route-id]
  (swap! app-state assoc :current-route route-id)
  ;; Update URL without page reload
  (.pushState js/history nil nil (str "#" (name route-id))))

(defn toggle-theme! []
  (let [new-theme (if (= (:theme @app-state) "light") "dark" "light")]
    (swap! app-state assoc :theme new-theme)
    (.setItem js/localStorage "ty-theme" new-theme)
    ;; Apply theme to document
    (let [html (.-documentElement js/document)]
      (set! (.-className html) (if (= new-theme "dark") "dark" "")))))

(defn toggle-mobile-menu! []
  (swap! app-state update :mobile-menu-open not))
