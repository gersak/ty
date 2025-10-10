(ns ty.css
  (:require [clojure.string :as str]))

;; =============================================================================
;; Macro for compile-time CSS loading
;; =============================================================================

(defmacro defstyles
  "Loads CSS file at compile time and returns either a CSSStyleSheet object
   (if supported) or a string fallback.
   
   Options:
     :with-vars? - If true, automatically prepends CSS variable inheritance
   
   Usage:
     (defstyles button-styles)                        ; looks for button.css in same dir
     (defstyles button-styles \"custom.css\")           ; custom path  
   
   The macro will:
   1. Load CSS file content at compile time
   2. Optionally prepend CSS variable inheritance
   3. At runtime, create CSSStyleSheet if supported
   4. Otherwise return the CSS string for fallback"
  {:clj-kondo/lint-as 'clojure.core/def
   :clj-kondo/ignore [:uninitialized-var]} ; Tell clj-kondo this defines a var
  ([name]
   ;; Infer CSS filename from the def name
   (let [css-filename (str
                        (->
                          (str *ns*)
                          (str/replace #"\." "/")
                          (str/replace #"-" "_"))
                        ".css")]
     `(defstyles ~name ~css-filename)))
  ([name path]
   ;; Get the current namespace to build full path if needed
   (let [current-ns (str *ns*)
         ;; Convert namespace to path: ty.components.button -> ty/components/button
         ns-path (str/replace current-ns #"\." "/")
         ;; If path doesn't contain /, assume it's in same directory as current namespace
         full-path (if (str/includes? path "/")
                     path
                     (str ns-path "/" path))
         ;; Read the CSS file at compile time
         css-content (try
                       (slurp (clojure.java.io/resource full-path))
                       (catch Exception _
                         (throw (ex-info (str "Cannot find CSS file: " full-path)
                                         {:path full-path
                                          :namespace current-ns}))))]
     `(defonce ~name
        ;; At runtime, create CSSStyleSheet if supported, otherwise return string
        (let [sheet# (js/CSSStyleSheet.)]
          (.replaceSync sheet# ~css-content)
          sheet#)
        #_(let [content ~css-content]
            (if (~'exists? js/CSSStyleSheet)
              (let [sheet# (js/CSSStyleSheet.)]
                (try
                  (.replaceSync sheet# content#)
                  sheet#
                  (catch js/Error e#
                  ;; If CSS parsing fails, fall back to string
                    (js/console.warn "Failed to create CSSStyleSheet:" e#)
                    content#)))
            ;; Fallback for browsers without Constructable Stylesheets
              content#))))))
