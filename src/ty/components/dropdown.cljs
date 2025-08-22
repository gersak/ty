(ns ty.components.dropdown
  "Main dropdown component with device detection and delegation to mobile/desktop implementations"
  (:require [ty.components.dropdown.common :as common]
            [ty.components.dropdown.desktop :as desktop]
            [ty.components.dropdown.mobile :as mobile]
            [ty.shim :as wcs]))

;; =====================================================
;; MAIN RENDER FUNCTION WITH DELEGATION
;; =====================================================

(defn render!
  "Main render function that delegates to mobile or desktop implementation"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        is-mobile? (common/is-mobile-device?)]

    ;; Debug logging
    (println "Window width:" (.-innerWidth js/window))
    (println "Is mobile?" is-mobile?)

    ;; Ensure styles are loaded
    (common/ensure-dropdown-styles! root)

    ;; Choose rendering mode based on device detection
    (if is-mobile?
      (mobile/render! el root)
      (desktop/render! el root))))

;; =====================================================
;; CLEANUP FUNCTION
;; =====================================================

(defn cleanup!
  "Cleanup function that delegates to implementation-specific cleanup"
  [^js el]
  (when-let [cleanup-fn (.-tyDropdownCleanup el)]
    (cleanup-fn)
    (set! (.-tyDropdownCleanup el) nil)))

;; =====================================================
;; WEB COMPONENT REGISTRATION
;; =====================================================

(wcs/define! "ty-dropdown"
  {:observed [:value :placeholder :searchable :not-searchable :disabled :readonly :size :flavor]
   :connected render!
   :disconnected cleanup!
   :attr (fn [^js el _ _old _]
           (render! el))})
