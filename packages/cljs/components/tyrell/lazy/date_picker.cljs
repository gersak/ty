(ns ty.lazy.date-picker
  "Lazy loading wrapper for ty-date-picker component"
  (:require
   [shadow.lazy :as lazy]
   [ty.lazy.util :as lazy-util]
   [ty.shim :as wcs]))

(def config (lazy/loadable ty.components.date-picker/configuration))

(wcs/define!
  "ty-date-picker"
  (merge
   {:observed [:value :size :flavor :label :placeholder :required :disabled :name
               :clearable :format :locale :min-date :max-date :first-day-of-week :with-time]
    :props {:value nil}
    :form-associated true}
   (lazy-util/create-lazy-lifecycle config)))





