(ns ty.generate.fav6
  (:require
    ; [babashka.fs :as fs]
    [clojure.java.io :as io]
    [clojure.java.shell :as sh]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as str]
    [clojure.xml :as xml]
    [ty.generate.util :refer [list-files file-name ext]]))


(def root ".icons/")


(defn ensure-root
  []
  (io/make-parents ".icons/README.md"))


(defn clone-repo
  []
  (sh/sh
    "git" "clone" "https://github.com/FortAwesome/Font-Awesome.git"
    (str root "/" "fav6")))


(defn clean-repo
  []
  (sh/sh "rm" "-r" (str root "/fav6")))


(defn list-images
  [style]
  (let [target (str root "fav6/svgs/" style)]
    (map #(str target "/" %) (list-files target))))


(comment
  (def style "solid")
  (list-images "solid"))


; (defn gen-el
;   [{:keys [tag attrs content]}]
;   (let [attrs (cond->
;                 attrs
;                 (= tag :svg)
;                 (assoc
;                   :height "1em"
;                   :width "1em"
;                   :stroke "currentColor"
;                   :fill "currentColor")
;                 ;;
;                 (#{:line :path :polyline :rect :circle :polygon} tag)
;                 (as-> tag
;                       (update tag :stroke (fn [v] (if (= v "#000") "currentColor" v)))
;                   (update tag :fill (fn [v] (if (= v "#000") "currentColor" v))))
;                 ;;
;                 (some? (:style attrs))
;                 (update :style (fn [current]
;                                  (reduce
;                                    (fn [r e]
;                                      (let [[k v] (str/split e #":")]
;                                        (if (#{"stroke" "fill"} k)
;                                          (case v
;                                            "none" (assoc r (keyword k) "none")
;                                            "#000" r)
;                                          (assoc r (keyword k) v))))
;                                    nil
;                                    (when current (str/split current #";"))))))]
;     (if (empty? content)
;       `(~(symbol "helix.dom" (name tag))
;          ~(cond-> attrs
;             (= tag :svg) (assoc :& 'props)))
;       `(~(symbol "helix.dom" (name tag))
;          ~(cond-> attrs
;             (= tag :svg) (assoc :& 'props))
;          ~@(map gen-el content)))))


(defn gen-el
  [{:keys [tag attrs content]}]
  (let [attrs (cond->
                attrs
                (= tag :svg)
                (assoc
                  :height "1em"
                  :width "1em"
                  :stroke "currentColor"
                  :fill "currentColor")
                ;;
                (#{:line :path :polyline :rect :circle :polygon} tag)
                (as-> tag
                      (update tag :stroke (fn [v] (if (= v "#000") "currentColor" v)))
                  (update tag :fill (fn [v] (if (= v "#000") "currentColor" v))))
                ;;
                (some? (:style attrs))
                (update :style (fn [current]
                                 (reduce
                                   (fn [r e]
                                     (let [[k v] (str/split e #":")]
                                       (if (#{"stroke" "fill"} k)
                                         (case v
                                           "none" (assoc r (keyword k) "none")
                                           "#000" r)
                                         (assoc r (keyword k) v))))
                                   nil
                                   (when current (str/split current #";"))))))]
    (if (empty? content)
      `(~(symbol "helix.dom" (name tag))
         ~(cond-> attrs
            (= tag :svg) (assoc :& 'props)))
      `(~(symbol "helix.dom" (name tag))
         ~(cond-> attrs
            (= tag :svg) (assoc :& 'props))
         ~@(map gen-el content)))))


(defn process-svg
  [path]
  (let [xml (xml/parse (str path))
        icon (file-name path)
        icon (if (re-find #"^\d" (name icon))
               (str "_" (name icon))
               (name icon))]
    `(def ~(symbol icon) ~(with-out-str (xml/emit xml)))))


(comment
  (def path ".icons/fav6/svgs/solid/angle-up.svg")
  (def xml (xml/parse path))
  (with-out-str (xml/emit xml)))


(defn generate-fa
  [style]
  (str/join
    "\n\n"
    (map
      ; str
      #(with-out-str (pprint %))
      (reduce
        (fn [r path]
          (conj r (process-svg path)))
        [`(~'ns ~(symbol (str "ty.fav6." style))
                ~(case style
                   "brands" `(:refer-clojure
                               :exclude [~'meta])
                   "regular" `(:refer-clojure
                                :exclude [~'map ~'comment ~'clone])
                   "solid" `(:refer-clojure
                              :exclude [~'map ~'clone ~'comment ~'list
                                        ~'repeat ~'divide ~'key ~'mask
                                        ~'filter ~'shuffle ~'atom ~'cat
                                        ~'remove
                                        ~'print ~'sort])))]
        (list-images style)))))


(defn generate
  []
  (let [styles ["regular" "brands" "solid"]]
    (doseq [style styles
            :let [target (str "gen/ty/fav6/" style ".cljs")]]
      (io/make-parents target)
      (spit target (generate-fa style)))))


(comment
  (generate))
