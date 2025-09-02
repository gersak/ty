(ns ty.css)

;; =============================================================================
;; Style Application Utilities
;; =============================================================================

(defn ensure-styles!
  "Applies styles to a shadow root, handling both CSSStyleSheet and string styles.
   Prevents duplicate style application.
   
   Arguments:
   - shadow-root: The shadow DOM root element
   - styles: Either a CSSStyleSheet object or CSS string (from defstyles)
   - style-id: Unique identifier to prevent duplicates (optional)
   
   For CSSStyleSheet:
   - Adds to adoptedStyleSheets if not already present
   
   For string styles:
   - Creates a <style> element with the given ID
   - Only adds if no style with that ID exists"
  ([shadow-root styles]
   ;; Generate a default ID based on the style content
   (ensure-styles! shadow-root styles (str "ty-styles-" (hash styles))))
  ([shadow-root styles style-id]
   (cond
     ;; Modern path: Constructable Stylesheets
     (instance? js/CSSStyleSheet styles)
     (let [adopted (.-adoptedStyleSheets shadow-root)]
       ;; Check if this exact stylesheet is already adopted
       (when-not (some #(identical? % styles) adopted)
         (set! (.-adoptedStyleSheets shadow-root)
               (.concat adopted #js [styles]))))

     ;; Fallback path: Create style element
     (string? styles)
     (when-not (.querySelector shadow-root (str "style#" style-id))
       (let [style-el (js/document.createElement "style")]
         (set! (.-id style-el) style-id)
         (set! (.-textContent style-el) styles)
         (.appendChild shadow-root style-el)))

     :else
     (js/console.error "ensure-styles!: styles must be CSSStyleSheet or string" styles))))

