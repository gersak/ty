(ns ty.lazy.date-picker
  "Lazy loading wrapper for ty-date-picker component"
  (:require
   [shadow.cljs.modern :refer [js-await]]
   [shadow.lazy :as lazy]
   [ty.shim :as wcs]))

(def config (lazy/loadable ty.components.date-picker/configuration))

(wcs/define!
  "ty-date-picker"
  {:observed [:value :size :flavor :label :placeholder :required :disabled :name
              :clearable :format :locale :min-date :max-date :first-day-of-week :with-time]
   :props {:value nil}
   :form-associated true
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
