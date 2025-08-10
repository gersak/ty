(ns ty.shim
  "Thin CLJS wrapper over wc-shim.js so you don’t touch classes or js*.
   Provides helpers for attributes, props, and shadow DOM."
  (:require ["./shim.js" :as shim]
            [cljs-bean.core :refer [->js]]
            [cljs.reader :as edn]
            [clojure.string :as str]))

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
      (nil? v) false
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
;; Public API
;; -----------------------------

(defn define!
  "Define a Custom Element.
   tag  - string tag name, e.g. \"x-counter\"
   opts - hooks/options map, see ->hooks above.

   Returns the constructor (for completeness), but you rarely need it."
  [tag opts]
  (shim/define tag (->hooks opts)))
