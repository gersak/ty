(ns ty.lazy.multiselect
  "Lazy loading wrapper for ty-multiselect component"
  (:require
    [shadow.lazy :as lazy]
    [ty.lazy.util :as lazy-util]
    [ty.shim :as wcs]))

(def config (lazy/loadable ty.components.multiselect/configuration))

(wcs/define!
  "ty-multiselect"
  (merge
    {:observed [:value :placeholder :disabled :readonly :flavor :label :required :name]
     :props {:value nil}
     :form-associated true}
    (lazy-util/create-lazy-lifecycle config)))





