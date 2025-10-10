(ns ty.lazy.calendar-month
  "Lazy loading wrapper for ty-calendar-month component"
  (:require
   [shadow.lazy :as lazy]
   [ty.lazy.util :as lazy-util]
   [ty.shim :as wcs]))

(def config (lazy/loadable ty.components.calendar-month/configuration))

(wcs/define!
  "ty-calendar-month"
  (merge
   {:observed [:year :month :value :min-date :max-date :first-day-of-week]
    :props {:value nil}}
   (lazy-util/create-lazy-lifecycle config)))





