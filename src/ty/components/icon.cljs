(ns ty.components.icon
  (:require
    [clojure.set :as set]
    [clojure.string :as str]
    [ty.css :refer [ensure-styles!]]
    [ty.icons :as icons]
    [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load icon styles from icon.css
(defstyles icon-styles)

(def not-found "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 512 512\"><!--!Font Awesome Free v6.7.2 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license/free Copyright 2025 Fonticons, Inc.--><path d=\"M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512z\"/></svg>")

;; Track which components are watching which icons
(def watchers (atom {}))

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
        ;; Get icon SVG from store or use not-found
        icon-svg (or (icons/get name) not-found)
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

(defn watch-icon! [^js el icon-name]
  ;; Remove any existing watcher for this element
  (let [el-id (or (.-tyIconId el) (str (random-uuid)))]
    ;; Store the ID on the element for future reference
    (when-not (.-tyIconId el)
      (set! (.-tyIconId el) el-id))

    ;; Update watchers atom to track this element
    (swap! watchers assoc el-id {:element el
                                 :icon-name icon-name})))

(defn unwatch-icon! [^js el]
  ;; Remove watcher for this element
  (when-let [el-id (.-tyIconId el)]
    (swap! watchers dissoc el-id)))

;; Watch for changes to the icons atom
(add-watch icons/data :icon-components
           (fn [_ _ old-icons new-icons]
             ;; Find which icons changed
             (let [changed-icons (reduce-kv
                                   (fn [acc k v]
                                     (if (not= (get old-icons k) v)
                                       (conj acc k)
                                       acc))
                                   #{}
                                   new-icons)]
               ;; Re-render components watching changed icons
               (doseq [[_ {:keys [element icon-name]}] @watchers]
                 (when (contains? changed-icons icon-name)
                   ;; Check if element is still connected to DOM
                   (when (.-isConnected element)
                     (render! element)))))))

(wcs/define! "ty-icon"
  {:observed [:name :size :spin :pulse :tempo :class]
   :props {:name nil
           :size nil
           :spin nil
           :pulse nil
           :tempo nil
           :class nil}
   :construct (fn [^js el])
   :connected (fn [^js el]
                ;; Start watching when connected
                (let [name (wcs/attr el "name")]
                  (when name
                    (watch-icon! el name)))
                (render! el))
   :disconnected (fn [^js el]
                   ;; Stop watching when disconnected
                   (unwatch-icon! el))
   :attr (fn [^js el attr-name old new]
           ;; Reflect attribute changes into props
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
    ;; Clear watchers on reload to prevent memory leaks
    (reset! watchers {})))

(defn ^:dev/after-load start []
  ;; Called after code is reloaded
  ;; The shim will automatically refresh all icon instances
  (when goog.DEBUG
    (js/console.log "[ty-icon] Hot reload complete!")))
