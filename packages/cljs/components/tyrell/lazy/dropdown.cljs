(ns ty.lazy.dropdown
  "Lazy loading wrapper for ty-dropdown component"
  (:require
   [shadow.lazy :as lazy]
   [ty.lazy.util :as lazy-util]
   [ty.shim :as wcs]))

(def config (lazy/loadable ty.components.dropdown/configuration))

(wcs/define!
  "ty-dropdown"
  (merge
   {:observed [:value :placeholder :searchable :not-searchable :disabled :readonly :flavor :label :required :external-search :name :clearable]
    :props {:value nil}
    :form-associated true}
   (lazy-util/create-lazy-lifecycle config)))





