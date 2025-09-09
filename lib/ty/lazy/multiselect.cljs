(ns ty.lazy.multiselect
  "Lazy loading wrapper for ty-multiselect component"
  (:require
   [shadow.cljs.modern :refer [js-await]]
   [shadow.lazy :as lazy]
   [ty.shim :as wcs]))

(def config (lazy/loadable ty.components.multiselect/configuration))

(wcs/define!
  "ty-multiselect"
  {:observed [:value :placeholder :disabled :readonly :flavor :label :required :name]
   :form-associated true
   :props {:value nil}
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
