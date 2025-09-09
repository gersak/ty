(ns ty.lazy.calendar
  "Lazy loading wrapper for ty-calendar component"
  (:require
    [shadow.lazy :as lazy]
    [ty.lazy.util :as lazy-util]
    [ty.shim :as wcs]))

(def nav-config (lazy/loadable ty.components.calendar-navigation/configuration))
(def config (lazy/loadable ty.components.calendar/configuration))

;; Define ty-calendar-navigation as part of the calendar lazy loading
(wcs/define!
  "ty-calendar-navigation"
  (merge
    {:observed [:year :month :locale :show-today-button :show-clear-button :min-date :max-date :format-title]}
    (lazy-util/create-lazy-lifecycle nav-config)))

;; Define ty-calendar 
(wcs/define!
  "ty-calendar"
  (merge
    {:observed [:year :month :day :show-navigation :locale :width :min-width :max-width
                :day-content-fn :day-classes-fn :name :value]
     :props {:dayContentFn nil
             :dayClassesFn nil
             :customCSS nil
             :value nil}
     :form-associated true}
    (lazy-util/create-lazy-lifecycle config)))
