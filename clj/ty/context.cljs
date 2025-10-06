(ns ty.context)


(defonce ^:dynamic *user* (atom nil))
(defonce ^:dynamic *token* nil)
(defonce ^:dynamic *roles* nil)
(defonce ^:dynamic *permissions* nil)
(defonce ^:dynamic *locale* nil)
(defonce ^:dynamic *theme* nil)

(defonce ^:dynamic *element-sizes* (atom nil))
