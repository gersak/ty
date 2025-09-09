(ns ty.lazy.calendar-month
  "Lazy loading wrapper for ty-calendar-month component"
  (:require
   [shadow.cljs.modern :refer [js-await]]
   [shadow.lazy :as lazy]
   [ty.shim :as wcs]))

(def config (lazy/loadable ty.components.calendar-month/configuration))

(wcs/define!
  "ty-calendar-month"
  {:observed [:year :month :value :min-date :max-date :first-day-of-week]
   :props {:value nil}
   :connected (fn [^js el]
                (js-await [config (lazy/load config)]
                          ((:connected config) el)))
   :disconnected (fn [^js el]
                   (js-await [config (lazy/load config)]
                             ((:disconnected config) el)))
   :attr (fn [^js el delta]
           (js-await [config (lazy/load config)]
                     ((:attr config) el delta)))
   :prop (fn [^js el delta]
           (js-await [config (lazy/load config)]
                     ((:prop config) el delta)))})
