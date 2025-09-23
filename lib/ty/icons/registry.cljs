(ns ty.icons.registry
  "Icon registry system for ty-icon component.
   
   Provides multiple registry types with configurable lookup priority:
   - icon: In-memory SVG strings (fastest)
   - sprite: DOM symbol references (fast)
   - source: URL mappings to SVG files (cached after first load)
   - loader: Dynamic/async resolution functions (slowest)
   
   Maintains full backward compatibility with existing ty.icons usage."
  (:require
    [cljs-bean.core :refer [->clj ->js]]
    [clojure.string :as str]))

;; ==========================================
;; Registry State
;; ==========================================

(def registries
  "All registry data stores"
  (atom {:icon {} ; In-memory SVG strings
         :sprite {} ; DOM symbol references  
         :source {} ; URL mappings
         :loader {}})) ; Dynamic resolution functions ; Dynamic resolution functions

(def ^:private config
  "Registry configuration"
  (atom {:priority [:icon :sprite :source :loader] ; Lookup order
         :cache true ; Cache fetched icons
         :fallback nil ; Fallback icon name
         :debug false})) ; Debug logging

(def ^:private cache
  "Cache for fetched icons (source/loader registries)"
  (atom {}))

;; ==========================================
;; Internal Utilities
;; ==========================================

(defn- log
  "Debug logging when enabled"
  [& args]
  (when (:debug @config)
    (apply js/console.log "[ty-icons-registry]" args)))

(defn- registry-exists?
  "Check if registry type is valid"
  [registry-type]
  (contains? @registries registry-type))

(defn- pattern-match?
  "Check if name matches a pattern (supports wildcards)"
  [pattern name]
  (if (str/includes? pattern "*")
    (let [regex-pattern (-> pattern
                            (str/replace "*" ".*")
                            (str "^" "$")
                            re-pattern)]
      (re-matches regex-pattern name))
    (= pattern name)))

;; ==========================================
;; Registry Management
;; ==========================================

(defn configure!
  "Configure registry system"
  [opts]
  (swap! config merge opts)
  (log "Configuration updated:" (clj->js @config)))

(defn register!
  "Register icon(s) to specific registry type"
  ([icons] (register! icons :icon))
  ([icons registry-type]
   (when-not (registry-exists? registry-type)
     (throw (js/Error. (str "Invalid registry type: " registry-type))))

   (let [icon-map (if (map? icons)
                    icons
                    ;; If it's a single icon name, we need the actual icon data
                    ;; This branch is mainly for single icon registration
                    (if (string? icons)
                      (throw (js/Error. "Single icon registration requires icon name and SVG data"))
                      icons))]
     (swap! registries update registry-type merge icon-map)
     (log "Registered" (count icon-map) "icons to" registry-type "registry")
     true)))

(defn unregister!
  "Remove icon(s) from specific registry type"
  ([icon-name] (unregister! icon-name :icon))
  ([icon-name registry-type]
   (swap! registries update registry-type dissoc icon-name)
   (log "Unregistered" icon-name "from" registry-type "registry")
   true))

(defn clear-registry!
  "Clear all icons from specific registry type"
  [registry-type]
  (swap! registries assoc registry-type {})
  (log "Cleared" registry-type "registry")
  true)

(defn clear-cache!
  "Clear icon cache"
  []
  (reset! cache {})
  (log "Cache cleared")
  true)

;; ==========================================
;; Registry Lookups
;; ==========================================

(defn- lookup-icon-registry
  "Lookup in icon registry (in-memory SVG strings)"
  [name registries-snapshot]
  (get-in registries-snapshot [:icon name]))

; Old duplicate functions removed - now using snapshot-based versions above

; Removed old lookup functions - now using snapshot-based versions above

; Old non-snapshot loader function removed - using snapshot-based version instead

(defn- lookup-sprite-registry
  "Lookup in sprite registry (DOM symbol references)"
  [name registries-snapshot]
  (get-in registries-snapshot [:sprite name]))

(defn- lookup-source-registry
  "Lookup in source registry (URL mappings) - returns Promise"
  [name registries-snapshot]
  (when-let [url (get-in registries-snapshot [:source name])]
    ;; Check cache first
    (if-let [cached (get @cache name)]
      (js/Promise.resolve cached)
      ;; Fetch from URL
      (-> (js/fetch url)
          (.then (fn [response]
                   (if (.-ok response)
                     (.text response)
                     (throw (js/Error. (str "Failed to fetch icon: " url))))))
          (.then (fn [svg-content]
                   ;; Cache the result
                   (when (:cache @config)
                     (swap! cache assoc name svg-content))
                   svg-content))
          (.catch (fn [error]
                    (log "Error fetching icon from source:" error)
                    nil))))))


(defn- lookup-loader-registry
  "Lookup in loader registry (dynamic resolution) - returns Promise"
  [name registries-snapshot]
  (let [loaders (get registries-snapshot :loader)]
    ;; Find matching loader pattern
    (when-let [[pattern loader-fn] (->> loaders
                                        (filter (fn [[pattern _]]
                                                  (pattern-match? pattern name)))
                                        first)]
      ;; Check cache first
      (if-let [cached (get @cache name)]
        (js/Promise.resolve cached)
        ;; Call loader function
        (try
          (let [result (loader-fn name)]
            ;; Handle both sync and async results
            (if (instance? js/Promise result)
              (-> result
                  (.then (fn [svg-content]
                           ;; Cache the result
                           (when (:cache @config)
                             (swap! cache assoc name svg-content))
                           svg-content))
                  (.catch (fn [error]
                            (log "Error in loader for" name ":" error)
                            nil)))
              ;; Sync result
              (do
                (when (:cache @config)
                  (swap! cache assoc name result))
                (js/Promise.resolve result))))
          (catch :default error
            (log "Error calling loader for" name ":" error)
            (js/Promise.resolve nil)))))))

;; ==========================================
;; Main Lookup Function
;; ==========================================

(defn lookup
  "Lookup icon by name using configured priority order.
   Returns either SVG string (sync) or Promise<SVG string> (async)"
  ([name] (lookup name @registries))
  ([name registries-snapshot]
   (when-not (string? name)
     (throw (js/Error. "Icon name must be a string")))

   (log "Looking up icon:" name)

   (loop [remaining-registries (:priority @config)]
     (if (empty? remaining-registries)
       ;; No more registries to try
       (do
         (log "Icon not found in any registry:" name)
         (when-let [fallback (:fallback @config)]
           (when (not= name fallback) ; Prevent infinite recursion
             (lookup fallback registries-snapshot))))

       ;; Try next registry
       (let [registry-type (first remaining-registries)
             result (case registry-type
                      :icon (lookup-icon-registry name registries-snapshot)
                      :sprite (lookup-sprite-registry name registries-snapshot)
                      :source (lookup-source-registry name registries-snapshot)
                      :loader (lookup-loader-registry name registries-snapshot)
                      nil)]

         (if result
           (do
             (log "Found icon" name "in" registry-type "registry")
             result)
           (recur (rest remaining-registries))))))))

;; ==========================================
;; Convenience Functions
;; ==========================================

(defn register-icons!
  "Convenience function to register multiple icons to icon registry"
  [icon-map]
  (register! icon-map :icon))

(defn register-sprites!
  "Convenience function to register multiple sprites to sprite registry"
  [sprite-map]
  (register! sprite-map :sprite))

(defn register-sources!
  "Convenience function to register multiple sources to source registry"
  [source-map]
  (register! source-map :source))

(defn register-loaders!
  "Convenience function to register multiple loaders to loader registry"
  [loader-map]
  (register! loader-map :loader))

(defn get-registry
  "Get current state of specific registry"
  [registry-type]
  (get @registries registry-type))

(defn get-config
  "Get current configuration"
  []
  @config)

(defn get-cache
  "Get current cache state"
  []
  @cache)

;; ==========================================
;; JavaScript API (^:export for advanced compilation)
;; ==========================================

(defn ^:export configure
  "JS: Configure registry system"
  [^js opts]
  (configure! (->clj opts :keywordize-keys true)))

(defn ^:export register
  "JS: Register icon(s) to specific registry"
  ([^js icons] (register! (->clj icons :keywordize-keys false) :icon))
  ([^js icons registry-type] (register! (->clj icons :keywordize-keys false) (keyword registry-type))))

(defn ^:export registerIcons
  "JS: Register multiple icons to icon registry"
  [^js icon-map]
  (register-icons! (->clj icon-map :keywordize-keys false)))

(defn ^:export registerSprites
  "JS: Register multiple sprites to sprite registry"
  [^js sprite-map]
  (register-sprites! (->clj sprite-map :keywordize-keys false)))

(defn ^:export registerSources
  "JS: Register multiple sources to source registry"
  [^js source-map]
  (register-sources! (->clj source-map :keywordize-keys false)))

(defn ^:export registerLoaders
  "JS: Register multiple loaders to loader registry"
  [^js loader-map]
  (register-loaders! (->clj loader-map :keywordize-keys false)))

(defn ^:export lookupIcon
  "JS: Lookup icon by name"
  [name]
  (lookup name))

(defn ^:export clearCache
  "JS: Clear icon cache"
  []
  (clear-cache!))

(defn ^:export getRegistry
  "JS: Get current state of specific registry"
  [registry-type]
  (->js (get-registry (keyword registry-type))))

(defn ^:export getConfig
  "JS: Get current configuration"
  []
  (->js (get-config)))

(defn ^:export getCache
  "JS: Get current cache state"
  []
  (->js (get-cache)))

;; ==========================================
;; Auto-registration Utilities
;; ==========================================

(defn auto-register-sprites!
  "Auto-register sprites from DOM using query selector"
  ([] (auto-register-sprites! "symbol[id]"))
  ([selector]
   (let [symbols (.querySelectorAll js/document selector)
         sprite-map (reduce
                      (fn [r s]
                        (let [id (.getAttribute s "id")
                              icon-name (some-> id
                                                (str/replace #"^(icon-|sprite-)" "")
                                                (str/replace #"^(ico-|spr-)" ""))
                              svg-content (.-innerHTML s)
                              viewbox (or (.getAttribute s "viewBox") "0 0 24 24")]
                          (assoc r icon-name (str "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"" viewbox "\">"
                                                  svg-content
                                                  "</svg>"))))
                      nil
                      symbols)]
     (register-sprites! sprite-map)
     (log "Auto-registered" (.-length symbols) "sprites from DOM")
     true)))

(defn ^:export autoRegisterSprites
  "JS: Auto-register sprites from DOM using query selector"
  ([] (auto-register-sprites!))
  ([selector] (auto-register-sprites! selector)))


