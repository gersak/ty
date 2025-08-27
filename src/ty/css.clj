(ns ty.css
  (:require [clojure.string :as str]))

;; =============================================================================
;; CSS Variable Inheritance Definitions (for compile time)
;; =============================================================================

(def ^:private ty-css-variables
  "Complete list of Ty CSS variables that need to be inherited in Shadow DOM.
   This ensures all design tokens are available to nested web components.
   
   Updated to use ONLY industry-standard semantic naming:
   Primary, Secondary, Success, Danger, Warning, Info, Neutral"
  [;; Core semantic colors (NEW INDUSTRY STANDARD)
   "--ty-semantic-primary" "--ty-semantic-secondary" "--ty-semantic-success"
   "--ty-semantic-danger" "--ty-semantic-warning" "--ty-semantic-info" "--ty-semantic-neutral"

   ;; Color variations (primary)
   "--ty-color-primary" "--ty-color-primary-p1" "--ty-color-primary-p2" "--ty-color-primary-p3"
   "--ty-color-primary-m1" "--ty-color-primary-m2" "--ty-color-primary-m3"

   ;; Color variations (secondary)
   "--ty-color-secondary" "--ty-color-secondary-p1" "--ty-color-secondary-p2" "--ty-color-secondary-p3"
   "--ty-color-secondary-m1" "--ty-color-secondary-m2" "--ty-color-secondary-m3"

   ;; Color variations (success)
   "--ty-color-success" "--ty-color-success-p1" "--ty-color-success-p2" "--ty-color-success-p3"
   "--ty-color-success-m1" "--ty-color-success-m2" "--ty-color-success-m3"

   ;; Color variations (danger)
   "--ty-color-danger" "--ty-color-danger-p1" "--ty-color-danger-p2" "--ty-color-danger-p3"
   "--ty-color-danger-m1" "--ty-color-danger-m2" "--ty-color-danger-m3"

   ;; Color variations (warning)
   "--ty-color-warning" "--ty-color-warning-p1" "--ty-color-warning-p2" "--ty-color-warning-p3"
   "--ty-color-warning-m1" "--ty-color-warning-m2" "--ty-color-warning-m3"

   ;; Color variations (info)
   "--ty-color-info" "--ty-color-info-p1" "--ty-color-info-p2" "--ty-color-info-p3"
   "--ty-color-info-m1" "--ty-color-info-m2" "--ty-color-info-m3"

   ;; Color variations (neutral)
   "--ty-color-neutral" "--ty-color-neutral-p1" "--ty-color-neutral-p2" "--ty-color-neutral-p3"
   "--ty-color-neutral-m1" "--ty-color-neutral-m2" "--ty-color-neutral-m3"

   ;; Background colors
   "--ty-bg" "--ty-bg-p1" "--ty-bg-p2" "--ty-bg-p3" "--ty-bg-m1" "--ty-bg-m2" "--ty-bg-m3"
   "--ty-bg-primary" "--ty-bg-primary-p1" "--ty-bg-primary-m1"
   "--ty-bg-secondary" "--ty-bg-secondary-p1" "--ty-bg-secondary-m1"
   "--ty-bg-success" "--ty-bg-success-p1" "--ty-bg-success-m1"
   "--ty-bg-danger" "--ty-bg-danger-p1" "--ty-bg-danger-m1"
   "--ty-bg-warning" "--ty-bg-warning-p1" "--ty-bg-warning-m1"
   "--ty-bg-info" "--ty-bg-info-p1" "--ty-bg-info-m1"
   "--ty-bg-neutral" "--ty-bg-neutral-p1" "--ty-bg-neutral-m1"

   ;; Border colors
   "--ty-border" "--ty-border-p1" "--ty-border-p2" "--ty-border-p3"
   "--ty-border-m1" "--ty-border-m2" "--ty-border-m3"
   "--ty-border-primary" "--ty-border-secondary" "--ty-border-success"
   "--ty-border-danger" "--ty-border-warning" "--ty-border-info" "--ty-border-neutral"

   ;; Modal tokens
   "--ty-modal-bg" "--ty-modal-color" "--ty-modal-border"
   "--ty-modal-backdrop" "--ty-modal-backdrop-blur" "--ty-modal-border-radius"
   "--ty-modal-shadow" "--ty-modal-duration"

   ;; Modal size variants
   "--ty-modal-sm-width" "--ty-modal-sm-min-width"
   "--ty-modal-md-width" "--ty-modal-md-min-width"
   "--ty-modal-lg-width" "--ty-modal-lg-min-width"
   "--ty-modal-xl-width" "--ty-modal-xl-min-width"
   "--ty-modal-full-width" "--ty-modal-full-height"

   ;; Input component tokens
   "--ty-input-bg" "--ty-input-color" "--ty-input-border" "--ty-input-border-hover"
   "--ty-input-border-focus" "--ty-input-shadow-focus" "--ty-input-placeholder"
   "--ty-input-disabled-bg" "--ty-input-disabled-border" "--ty-input-disabled-color"
   "--ty-label-color"

   ;; Input semantic states
   "--ty-input-success-border" "--ty-input-danger-border"
   "--ty-input-warning-border" "--ty-input-info-border"

   ;; Spacing scale
   "--ty-spacing-0" "--ty-spacing-px" "--ty-spacing-mini"
   "--ty-spacing-1" "--ty-spacing-2" "--ty-spacing-3" "--ty-spacing-4" "--ty-spacing-5"
   "--ty-spacing-6" "--ty-spacing-7" "--ty-spacing-8" "--ty-spacing-9" "--ty-spacing-10"
   "--ty-spacing-12" "--ty-spacing-16" "--ty-spacing-20" "--ty-spacing-24"

   ;; Border radius
   "--ty-radius-none" "--ty-radius-sm" "--ty-radius-base" "--ty-radius-md"
   "--ty-radius-lg" "--ty-radius-xl" "--ty-radius-2xl" "--ty-radius-3xl" "--ty-radius-full"

   ;; Shadows
   "--ty-shadow-none" "--ty-shadow-sm" "--ty-shadow-base" "--ty-shadow-md"
   "--ty-shadow-lg" "--ty-shadow-xl" "--ty-shadow-2xl"

   ;; Typography
   "--ty-font-sans" "--ty-font-mono"
   "--ty-font-xxs" "--ty-font-xs" "--ty-font-sm" "--ty-font-base" "--ty-font-lg"
   "--ty-font-xl" "--ty-font-2xl" "--ty-font-3xl" "--ty-font-4xl" "--ty-font-5xl"
   "--ty-font-thin" "--ty-font-light" "--ty-font-normal" "--ty-font-medium"
   "--ty-font-semibold" "--ty-font-bold" "--ty-font-extrabold"
   "--ty-line-height-none" "--ty-line-height-tight" "--ty-line-height-snug"
   "--ty-line-height-normal" "--ty-line-height-relaxed" "--ty-line-height-loose"

   ;; Transitions
   "--ty-transition-none" "--ty-transition-all" "--ty-transition-colors"
   "--ty-transition-shadow" "--ty-transition-transform"

   ;; Component sizes
   "--ty-size-xs" "--ty-size-sm" "--ty-size-md" "--ty-size-lg" "--ty-size-xl"

   ;; Z-index scale
   "--ty-z-auto" "--ty-z-0" "--ty-z-10" "--ty-z-20" "--ty-z-30" "--ty-z-40" "--ty-z-50"
   "--ty-z-dropdown" "--ty-z-sticky" "--ty-z-fixed" "--ty-z-modal-backdrop"
   "--ty-z-modal" "--ty-z-popover" "--ty-z-tooltip"])

(defn generate-css-variable-inheritance
  "No-op function - CSS variables should inherit naturally through Shadow DOM.
   If they don't, the issue is elsewhere (timing, load order, etc.)"
  []
  "/* CSS variables inherit naturally through Shadow DOM */\n")

(defn with-css-variable-inheritance
  "Instead of variable inheritance, we recommend removing this entirely.
   CSS variables inherit naturally through Shadow DOM boundaries."
  [css-content]
  css-content)

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
     (defstyles button-styles \"custom.css\" {:with-vars? true}) ; with CSS variable inheritance
   
   The macro will:
   1. Load CSS file content at compile time
   2. Optionally prepend CSS variable inheritance
   3. At runtime, create CSSStyleSheet if supported
   4. Otherwise return the CSS string for fallback"
  {:clj-kondo/lint-as 'clojure.core/def
   :clj-kondo/ignore [:uninitialized-var]} ; Tell clj-kondo this defines a var
  ([name]
   ;; Infer CSS filename from the def name
   (let [css-filename (str "./" (str/replace (str *ns*) #"\." "/") ".css")]
     `(defstyles ~name ~css-filename)))
  ([name path]
   `(defstyles ~name ~path {}))
  ([name path opts]
   ;; Get the current namespace to build full path if needed
   (let [current-ns (str *ns*)
         ;; Convert namespace to path: ty.components.button -> ty/components/button
         ns-path (str/replace current-ns #"\." "/")
         ;; If path doesn't contain /, assume it's in same directory as current namespace
         full-path (if (str/includes? path "/")
                     path
                     (str ns-path "/" path))
         ;; Read the CSS file at compile time
         base-css-content (try
                            (slurp (clojure.java.io/resource full-path))
                            (catch Exception e
                              (throw (ex-info (str "Cannot find CSS file: " full-path)
                                              {:path full-path
                                               :namespace current-ns}))))
         ;; Optionally add CSS variable inheritance
         css-content (if (:with-vars? opts)
                       (with-css-variable-inheritance base-css-content)
                       base-css-content)]
     `(defonce ~name
        ;; At runtime, create CSSStyleSheet if supported, otherwise return string
        (if (~'exists? js/CSSStyleSheet)
          (let [sheet# (js/CSSStyleSheet.)]
            (try
              (.replaceSync sheet# ~css-content)
              sheet#
              (catch js/Error e#
                ;; If CSS parsing fails, fall back to string
                (js/console.warn "Failed to create CSSStyleSheet:" e#)
                ~css-content)))
          ;; Fallback for browsers without Constructable Stylesheets
          ~css-content)))))
