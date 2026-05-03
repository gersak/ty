(ns ty.icons
  "Clean icon management API using the registry system.
   
   Core functions (re-exported from ty.icons.registry):
   - register-icons! - Register multiple icons (90% use case)
   - register-sprites! - Register sprite references 
   - configure! - Configure registry system
   - auto-register-sprites! - Auto-detect sprites from DOM
   
   Legacy compatibility functions:
   - set! - Replace all icons (uses registry)
   - add! - Add icons (uses registry) 
   - get - Lookup icon (uses registry)
   
   Advanced functions available under ty.icons.registry:
   - register-sources!, register-loaders!
   - get-registry, get-cache, clear-cache!
   - Full registry introspection and debugging"
  (:refer-clojure :exclude [get set set! set])
  (:require [cljs-bean.core :refer [->clj]]
            [ty.icons.registry :as registry]))

;; ==========================================
;; Legacy Compatibility Functions
;; ==========================================

(defn set!
  "Replace all icons with new icon map.
   Legacy compatibility - delegates to registry system."
  [icons]
  (registry/clear-registry! :icon)
  (registry/register-icons! icons))

(defn add!
  "Add icons to existing collection.
   Legacy compatibility - delegates to registry system."
  [icons]
  (registry/register-icons! icons))

(defn get
  "Lookup icon by name.
   Legacy compatibility - delegates to registry system.
   Returns SVG string or nil (sync only for backward compatibility)."
  [target]
  (let [result (registry/lookup target)]
    ;; For backward compatibility, only return sync results
    ;; Async results (Promises) are not supported in legacy API
    (if (instance? js/Promise result)
      nil ; Return nil for async results to maintain sync behavior
      result)))

;; ==========================================
;; Core Registry Functions (Re-exported)
;; ==========================================

(defn register-icons!
  "Register multiple icons to icon registry.
   This is the primary way to add icons in the new system."
  [icon-map]
  (registry/register-icons! icon-map))

(defn register-sprites!
  "Register multiple sprites to sprite registry.
   Sprites are DOM symbol references like '#icon-home'."
  [sprite-map]
  (registry/register-sprites! sprite-map))

(defn configure!
  "Configure the registry system.
   Options: {:debug true/false, :priority [...], :cache true/false, :fallback icon-name}"
  [opts]
  (registry/configure! opts))

(defn auto-register-sprites!
  "Auto-register sprites from DOM using query selector.
   Default selector finds all symbols with id attribute."
  ([] (registry/auto-register-sprites! "symbol[id]"))
  ([selector] (registry/auto-register-sprites! selector)))

;; ==========================================
;; JavaScript API (^:export for advanced compilation)
;; ==========================================

;; Legacy JS API (for backward compatibility)
(defn ^:export set
  "JS-friendly version of set!"
  [^js icons]
  (ty.icons/set! (->clj icons :keywordize-keys false)))

(defn ^:export add
  "JS-friendly version of add!"
  [^js icons]
  (add! (->clj icons :keywordize-keys false)))

(defn ^:export get-icon
  "JS-friendly version of get"
  [name]
  (get name))

;; New clean JS API (recommended)
(defn ^:export registerIcons
  "JS: Register multiple icons to icon registry"
  [^js icon-map]
  (register-icons! (->clj icon-map :keywordize-keys false)))

(defn ^:export registerSprites
  "JS: Register multiple sprites to sprite registry"
  [^js sprite-map]
  (register-sprites! (->clj sprite-map :keywordize-keys false)))

(defn ^:export configure
  "JS: Configure registry system"
  [^js opts]
  (configure! (->clj opts :keywordize-keys true)))

(defn ^:export autoRegisterSprites
  "JS: Auto-register sprites from DOM using query selector"
  ([] (auto-register-sprites!))
  ([selector] (auto-register-sprites! selector)))

(defn ^:export lookupIcon
  "JS: Lookup icon by name - supports async results"
  [name]
  (registry/lookup name))

;; Additional convenience functions for introspection
(defn get-registry
  "Get current state of specific registry"
  [registry-type]
  (registry/get-registry registry-type))

(defn get-config
  "Get current registry configuration"
  []
  (registry/get-config))
