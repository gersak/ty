(ns ty.icons
  (:refer-clojure :exclude [get set set!])
  (:require [cljs-bean.core :refer [->clj]]))


(def data (atom nil))


(defn set! [icons]
  (reset! data icons))

(defn add! [icons]
  (swap! data merge icons))

(defn get [target]
  (clojure.core/get @data target))

;; Export functions with ^:export metadata
(defn ^:export set
  "JS-friendly version of set!"
  [^js icons]
  (ty.icons/set! (->clj icons :keywordize-keys false)))

(defn ^:export add
  "JS-friendly version of add!"
  [^js icons]
  (ty.icons/add! (->clj icons :keywordize-keys false)))

(defn ^:export get-icon
  "JS-friendly version of get"
  [name]
  (ty.icons/get name))
