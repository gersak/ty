(ns ty.lazy.util
  "Shared utilities for lazy loading components"
  (:require
    [shadow.cljs.modern :refer [js-await]]
    [shadow.lazy :as lazy]))

(defn safe-lazy-load
  "Safe lazy loading with shadow.loader readiness check.
   Retries if shadow.loader is not yet initialized."
  [loadable-config callback]
  (try
    (js-await [config (lazy/load loadable-config)]
              (callback config))
    (catch js/Error _
      (.warn js/console "Trying againt to load: " loadable-config)
      (js/setTimeout
        #(safe-lazy-load loadable-config callback)
        10))))

(defn create-lazy-lifecycle
  "Create lazy component lifecycle functions with safe loading"
  [config]
  {:connected (fn [^js el]
                (safe-lazy-load config
                                (fn [config]
                                  ((:connected config) el))))
   :disconnected (fn [^js el]
                   (safe-lazy-load config
                                   (fn [config]
                                     (when-let [disconnected (:disconnected config)]
                                       (disconnected el)))))
   :attr (fn [^js el delta]
           (safe-lazy-load config
                           (fn [config]
                             ((:attr config) el delta))))
   :prop (fn [^js el delta]
           (safe-lazy-load config
                           (fn [config]
                             ((:prop config) el delta))))})



