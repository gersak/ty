(ns ty.components.resize-observer
  "Self-observing resize component that makes its dimensions available 
   to Clojure code via a global registry using HTML id attribute."
  (:require
    [cljs-bean.core :refer [->js]]
    [ty.context :refer [*element-sizes*]]
    [ty.css :refer [ensure-styles!]]
    [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load resize observer styles from resize_observer.css
#_{:clj-kondo/ignore [:uninitialized-var]}
(defstyles resize-observer-styles)

;; Global registry for element sizes keyed by HTML id

(defn get-size
  "Get dimensions for element with given id.
   Returns {:width number :height number} or nil if not found."
  [id]
  (when id
    (get @*element-sizes* id)))

;; Update size in registry
(defn- update-size! [id width height]
  (when id
    (swap! *element-sizes* assoc id {:width width
                                     :height height})))

;; Remove size from registry
(defn- remove-size! [id]
  (when id
    (swap! *element-sizes* dissoc id)))

(defn resize-observer-attributes
  "Read all resize observer attributes directly from element"
  [^js el]
  {:id (wcs/attr el "id")})

(defn render! [^js el]
  (let [{:keys [id]} (resize-observer-attributes el)
        root (wcs/ensure-shadow el)]

    ;; Ensure styles are loaded
    (ensure-styles! root resize-observer-styles "ty-resize-observer")

    ;; Set up slot for content
    (when (empty? (.-innerHTML root))
      (set! (.-innerHTML root) "<slot></slot>"))

    el))

(defn- setup-resize-observer! [^js el]
  "Set up ResizeObserver to watch the element itself"
  (let [{:keys [id]} (resize-observer-attributes el)]
    (when (and id (not (.-_tyResizeObserver el)))
      (let [observer (js/ResizeObserver.
                       (fn [entries]
                         (doseq [entry entries]
                           (let [target (.-target entry)
                                 content-rect (.-contentRect entry)
                                 width (.-width content-rect)
                                 height (.-height content-rect)]
                             (update-size! id width height)))))]
        (set! (.-_tyResizeObserver el) observer)
        (set! (.-id el) id)
        (.observe observer el)))))

(defn- cleanup-resize-observer! [^js el]
  "Clean up ResizeObserver and remove from registry"
  (let [{:keys [id]} (resize-observer-attributes el)]
    (when-let [observer (.-_tyResizeObserver el)]
      (.disconnect observer)
      (set! (.-_tyResizeObserver el) nil))
    (when id
      (remove-size! id))))

(defn- handle-id-change! [^js el delta]
  "Handle ID attribute changes"
  (when-let [old-id (.-id el)]
    (remove-size! old-id))
  (when (.-_tyResizeObserver el)
    (cleanup-resize-observer! el)
    (setup-resize-observer! el)))

(def configuration
  {:observed [:id]
   :connected (fn [^js el]
                (render! el)
                (setup-resize-observer! el))
   :disconnected cleanup-resize-observer!
   :attr (fn [^js el delta]
           (if (contains? delta "id")
             (handle-id-change! el delta)
             ;; Re-render for other attributes
             (render! el)))})

;; Export functions to global scope for demo purposes
(defn init-js-api! []
  "Export resize observer functions to JavaScript global scope"
  (when-not (.-tyComponents js/window)
    (set! (.-tyComponents js/window) #js {}))
  (when-not (.-resizeObserver (.-tyComponents js/window))
    (set! (.-resizeObserver (.-tyComponents js/window))
          #js {:getSize get-size
               :elementSizes (->js *element-sizes*)})))

;; Initialize JS API when component loads
(init-js-api!)

