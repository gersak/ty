(ns ty.lazy.dropdown
  "Lazy loading wrapper for ty-dropdown component"
  (:require
   [shadow.cljs.modern :refer [js-await]]
   [shadow.lazy :as lazy]
   [ty.shim :as wcs]))

(def config (lazy/loadable ty.components.dropdown/configuration))

(wcs/define!
  "ty-dropdown"
  {:observed [:value :placeholder :searchable :not-searchable :disabled :readonly :flavor :label :required :external-search :name]
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
