(ns ty.components.icon
  (:require
    [clojure.set :as set]
    [clojure.string :as str]
    [ty.css :refer [ensure-styles!]]
    [ty.icons :as icons]
    [ty.icons.registry :as registry]
    [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load icon styles from icon.css
(defstyles icon-styles)

(def not-found
  "<svg xmlns=\"http://www.w3.org/2000/svg\" 
        viewBox=\"0 0 512 512\" 
        fill=\"currentColor\" 
        opacity=\"0.2\">
     <!--!Font Awesome Free v6.7.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.-->
     <path d=\"M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512z\"/>
   </svg>")

(defn icon-attributes
  [^js el]
  {:name (wcs/attr el "name")
   :size (wcs/attr el "size")
   :spin (wcs/parse-bool-attr el "spin")
   :pulse (wcs/parse-bool-attr el "pulse")
   :tempo (wcs/attr el "tempo")
   :class (wcs/attr el "class")})

(defn build-class-list
  [{:keys [size spin pulse tempo class]}]
  (let [;; Component-managed classes (internal)
        internal-classes (cond-> #{}
                           size (conj (str "icon-" size))
                           spin (conj "icon-spin")
                           pulse (conj "icon-pulse")
                           tempo (conj (str "icon-tempo-" tempo)))

        ;; User-provided classes (external)
        external-classes (when (seq class)
                           (set (str/split class #"\s+")))

        ;; Merge both sets
        all-classes (set/union internal-classes (or external-classes #{}))]

    ;; Convert to string
    (str/join " " all-classes)))

(defn render! [^js el]
  (let [name (wcs/attr el "name")
        root (wcs/ensure-shadow el)
        ;; Get icon SVG from registry or use not-found
        ;; For async results (Promises), use not-found for now (sync-only approach)
        lookup-result (icons/get name)
        icon-svg (if (instance? js/Promise lookup-result)
                   not-found ; Use fallback for async results
                   (or lookup-result not-found))
        ;; Build new class list
        new-class-list (build-class-list (icon-attributes el))
        ;; Get current class list
        current-class-list (.-className el)]

    ;; Ensure styles are loaded
    (ensure-styles! root icon-styles "ty-icon")

    ;; Only update classes if they changed
    (when (not= new-class-list current-class-list)
      (set! (.-className el) new-class-list))

    ;; Clear and set shadow DOM content
    (set! (.-innerHTML root) icon-svg)

    el))

(def configuration
  {:observed [:name :size :spin :pulse :tempo :class]
   :props {:name nil
           :size nil
           :spin nil
           :pulse nil
           :tempo nil
           :class nil}
   :construct (fn [^js el])
   :connected (fn [^js el]
                ;; Generate unique watch ID for this element
                (let [watch-id (str "ty-icon-" (random-uuid))
                      icon-name (wcs/attr el "name")]
                  ;; Store watch ID on element for cleanup
                  (set! (.-tyIconWatchId el) watch-id)

                  ;; Add watch for registry changes that affect this element
                  (when icon-name
                    (add-watch registry/registries watch-id
                               (fn [_ _ old-registries new-registries]
                                 ;; Only check if this element is still connected
                                 (when (.-isConnected el)
                                   ;; Compare lookup results for this specific icon
                                   (let [old-result (registry/lookup icon-name old-registries)
                                         new-result (registry/lookup icon-name new-registries)]
                                     ;; Only re-render if result changed and both are sync
                                     (when (and (not= old-result new-result)
                                                (not (instance? js/Promise old-result))
                                                (not (instance? js/Promise new-result)))
                                       (render! el)))))))

                  ;; Initial render
                  (render! el)))
   :disconnected (fn [^js el]
                   ;; Remove watch using stored ID
                   (when-let [watch-id (.-tyIconWatchId el)]
                     (remove-watch registry/registries watch-id)
                     (set! (.-tyIconWatchId el) nil)))
   :attr (fn [^js el attr-name old new]
           ;; If icon name changed, we need to update the watch
           (when (= attr-name "name")
             ;; Remove old watch
             (when-let [watch-id (.-tyIconWatchId el)]
               (remove-watch registry/registries watch-id))

             ;; Add new watch for new icon name  
             (let [new-watch-id (str "ty-icon-" (random-uuid))]
               (set! (.-tyIconWatchId el) new-watch-id)
               (when new
                 (add-watch registry/registries new-watch-id
                            (fn [_ _ old-registries new-registries]
                              (when (.-isConnected el)
                                (let [old-result (registry/lookup new old-registries)
                                      new-result (registry/lookup new new-registries)]
                                  (when (and (not= old-result new-result)
                                             (not (instance? js/Promise old-result))
                                             (not (instance? js/Promise new-result)))
                                    (render! el)))))))))
           ;; Re-render for any attribute change
           (render! el))
   :prop (fn [^js el _k _old _new]
           ;; Any prop change re-renders
           (render! el))})

;; -----------------------------
;; Hot Reload Hooks
;; -----------------------------

(defn ^:dev/before-load stop []
  ;; Called before code is reloaded
  (when goog.DEBUG
    (js/console.log "[ty-icon] Preparing for hot reload...")
    ;; No global watchers to clear anymore - each element manages its own watch
    ))

(defn ^:dev/after-load start []
  ;; Called after code is reloaded
  ;; The shim will automatically refresh all icon instances
  (when goog.DEBUG
    (js/console.log "[ty-icon] Hot reload complete!")))
