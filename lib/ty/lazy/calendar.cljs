(ns ty.lazy.calendar
  "Lazy loading wrapper for ty-calendar component"
  (:require
    [shadow.cljs.modern :refer [js-await]]
    [shadow.lazy :as lazy]
    [ty.shim :as wcs]))

(def nav-config (lazy/loadable ty.components.calendar-navigation/configuration))

(wcs/define!
  "ty-calendar"
  {:observed [:year :month :day :show-navigation :locale :width :min-width :max-width
              :day-content-fn :day-classes-fn :name :value]
   :props {:dayContentFn nil
           :dayClassesFn nil
           :customCSS nil
           :value nil}
   :form-associated true
   :connected (fn [^js el]
                (js-await [config (lazy/load nav-config)]
                          ((:connected config) el)))
   :disconnected (fn [^js el]
                   (js-await [config (lazy/load nav-config)]
                             (when-let [disconnected (:disconnected config)]
                               (disconnected el))))
   :attr (fn [^js el delta]
           (js-await [config (lazy/load nav-config)]
                     ((:attr config) el delta)))
   :prop (fn [^js el delta]
           (js-await [config (lazy/load nav-config)]
                     ((:prop config) el delta)))})


(def config (lazy/loadable ty.components.calendar/configuration))

(wcs/define!
  "ty-calendar"
  {:observed [:year :month :day :show-navigation :locale :width :min-width :max-width
              :day-content-fn :day-classes-fn :name :value]
   :props {:dayContentFn nil
           :dayClassesFn nil
           :customCSS nil
           :value nil}
   :form-associated true
   :connected (fn [^js el]
                (js-await [config (lazy/load config)]
                          ((:connected config) el)))
   :disconnected (fn [^js el]
                   (js-await [config (lazy/load config)]
                             (when-let [disconnected (:disconnected config)]
                               (disconnected el))))
   :attr (fn [^js el delta]
           (js-await [config (lazy/load config)]
                     ((:attr config) el delta)))
   :prop (fn [^js el delta]
           (js-await [config (lazy/load config)]
                     ((:prop config) el delta)))})
