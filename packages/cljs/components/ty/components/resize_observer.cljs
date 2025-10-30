(ns ty.components.resize-observer
  "DEPRECATED: This ClojureScript wrapper is obsolete.
  
  The ty-resize-observer component is now implemented in TypeScript at:
    /packages/core/src/components/resize-observer.ts
    /packages/core/src/utils/resize-observer.ts
  
  ClojureScript code should use the TypeScript implementation via the window API:
  
  Getting sizes:
    (when-let [size (and js/window (.-tyResizeObserver js/window))]
      (let [dims (.getSize size \"my-container\")]
        {:width (.-width dims)
         :height (.-height dims)}))
  
  Or use the helper functions in ty.layout:
    (require '[ty.layout :as layout])
    
    ;; Get size once
    (layout/get-observer-size \"my-container\")
    
    ;; Subscribe to changes
    (layout/observe-size-changes! \"my-container\" 
      (fn [size] (println \"Resized:\" size)))
    
    ;; Use in component with dynamic binding
    (layout/with-resize-observer \"my-container\"
      (my-component))
  
  The TypeScript implementation provides:
    - window.tyResizeObserver.getSize(id) - sync size query
    - window.tyResizeObserver.onResize(id, callback) - subscribe with unsubscribe function
    - window.tyResizeObserver.sizes - get all registered sizes
  
  This file is kept for backwards compatibility documentation only.
  DO NOT USE THE OLD CLOJURESCRIPT IMPLEMENTATION - IT IS BROKEN.
  
  Migration date: 2024-01-30
  Reason: Unified TypeScript implementation with proper registry and callback system.")

;; All functionality moved to TypeScript
;; See /packages/core/src/components/resize-observer.ts
;; See /packages/core/src/utils/resize-observer.ts
;; Use ty.layout namespace for ClojureScript helpers
