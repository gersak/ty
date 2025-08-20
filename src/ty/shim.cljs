(ns ty.shim
  "Thin CLJS wrapper over wc-shim.js so you don't touch classes or js*.
   Provides helpers for attributes, props, shadow DOM, and hot reload support."
  (:require ["./shim.js" :as shim]
            [cljs-bean.core :refer [->js]]
            [cljs.reader :as edn]
            [clojure.string :as str]))

;; -----------------------------
;; Hot Reload Support
;; -----------------------------

(defonce ^:private component-registry (atom {}))
(defonce ^:private component-renderers (atom {}))

(defn- get-all-instances
  "Get all instances of a custom element in the document"
  [tag-name]
  (js/document.querySelectorAll tag-name))

(defn- refresh-instances!
  "Refresh all instances of a component by calling their render function"
  [tag-name]
  (when-let [render-fn (get @component-renderers tag-name)]
    (let [instances (get-all-instances tag-name)]
      (.forEach instances render-fn))))

;; -----------------------------
;; Attribute parsing helpers
;; -----------------------------

(defn attr
  "Get raw attribute from element."
  [^js el k]
  (.getAttribute el (name k)))

(defn set-attr!
  "Set attribute (string)."
  [^js el k v]
  (.setAttribute el (name k) (str v))
  el)

(defn rm-attr!
  [^js el k]
  (.removeAttribute el (name k))
  el)

(defn parse-int-attr [el k]
  (some-> (attr el k) js/parseInt))

(defn parse-float-attr [el k]
  (some-> (attr el k) js/parseFloat))

(defn parse-bool-attr
  "Boolean attribute semantics: presence -> true, explicit 'false' -> false."
  [el k]
  (let [v (attr el k)]
    (cond
      (nil? v) nil
      (= "false" (str/lower-case v)) false
      :else true)))

(defn parse-json-attr [el k]
  (when-let [v (attr el k)]
    (-> v js/JSON.parse (js->clj :keywordize-keys true))))

(defn parse-edn-attr [el k]
  (when-let [v (attr el k)]
    (edn/read-string v)))

;; -----------------------------
;; Shadow DOM helpers
;; -----------------------------

(defn ensure-shadow
  ([^js el] (ensure-shadow el "open"))
  ([^js el mode] (shim/ensureShadow el mode)))

(defn set-shadow-html!
  [^js el html]
  (shim/setShadowHTML el (or html "")))

;; -----------------------------
;; Props helpers (property-based API)
;; -----------------------------

(defn set-props!
  "Batch set props (EDN map). Triggers per-key prop hook."
  [^js el m]
  (let [js-props (reduce-kv
                   (fn [r k v]
                     (aset r (name k) v)
                     r)
                   #js {}
                   m)]
    (shim/setProps el js-props))
  el)

(defn get-props
  "Return current props as CLJS map."
  [^js el]
  (let [p (.-_props el)]
    (reduce
      (fn [r k]
        (assoc r (keyword k) (aget p k)))
      {}
      (js/Object.keys p))))

;; -----------------------------
;; Hooks adapter
;; -----------------------------

(defn ^:private ->hooks
  "Build hooks object for wc-shim.js from a CLJS map:
   {:observed [..attr names..]        ;; optional
    :props    {:count nil ...}        ;; optional prop accessors (keys only matter)
    :construct (fn [el] ...)
    :connected (fn [el] ...)
    :disconnected (fn [el] ...)
    :adopted (fn [el old-doc new-doc] ...)
    :attr (fn [el name old new] ...)
    :prop (fn [el k old new] ...)}"
  [{:keys [observed props construct connected disconnected adopted attr prop]}]
  #js {:observed (when observed (->js (map name observed)))
       :props (when props (->js (zipmap (map name (keys props)) (repeat true))))
       :construct (when construct (fn [el] (construct el)))
       :connected (when connected (fn [el] (connected el)))
       :disconnected (when disconnected (fn [el] (disconnected el)))
       :adopted (when adopted (fn [el od nd] (adopted el od nd)))
       :attr (when attr (fn [el n o v] (attr el (keyword n) o v)))
       :prop (when prop (fn [el k o v] (prop el (keyword k) o v)))})

;; -----------------------------
;; Public API with Hot Reload Support
;; -----------------------------

(defn define!
  "Define a Custom Element with hot reload support.
   In development: 
   - Skips redefinition if component already exists
   - Refreshes existing instances with new implementation
   In production:
   - Standard web component definition
   
   tag  - string tag name, e.g. \"x-counter\"
   opts - hooks/options map, see ->hooks above.
   
   Returns the constructor (for completeness), but you rarely need it."
  [tag opts]
  (let [already-defined? (.get js/window.customElements tag)]
    (cond
      ;; In production or first definition - define normally
      (or (not goog.DEBUG) (not already-defined?))
      (let [constructor (shim/define tag (->hooks opts))]
        ;; Store in registry for hot reload
        (swap! component-registry assoc tag opts)
        ;; Store render function if connected hook exists
        (when-let [connected (:connected opts)]
          (swap! component-renderers assoc tag connected))
        constructor)

      ;; In development and already defined - update and refresh
      :else
      (do
        ;; Update registry with new implementation
        (swap! component-registry assoc tag opts)
        ;; Update render function
        (when-let [connected (:connected opts)]
          (swap! component-renderers assoc tag connected))
        ;; Refresh all existing instances
        (js/console.log (str "[Ty] Hot reloading component: " tag))
        (refresh-instances! tag)
        ;; Return the existing constructor
        (.get js/window.customElements tag)))))

;; -----------------------------
;; Development helpers
;; -----------------------------

(defn ^:dev/after-load reload-all-components!
  "Force refresh all registered components after hot reload"
  []
  (when goog.DEBUG
    (doseq [[tag-name _] @component-registry]
      (js/console.log (str "[Ty] Refreshing: " tag-name))
      (refresh-instances! tag-name))))
