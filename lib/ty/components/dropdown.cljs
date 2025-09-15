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

    ;; Ensure styles are loaded
    (common/ensure-dropdown-styles! root)

    ;; Choose rendering mode based on device detection
    (if is-mobile?
      (mobile/render! el)
      (desktop/render! el))))

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

(def configuration
  {:observed [:value :placeholder :searchable :not-searchable :disabled :readonly :flavor :label :required :external-search :name] ; ← Add :name
   :props {:value nil} ; ← Enable property watching for framework integration
   :form-associated true
   :connected (fn [^js el]
                (common/init-dropdown-state! el)
                (render! el))
   :disconnected cleanup!
   :attr (fn [^js el delta]
           (let [delta' (common/enrich-delta delta el)]
             (when (or
                     (seq (dissoc delta "value")) ; Non-value changes always render
                     (not= delta delta') ; Value parsing changed something  
                     (contains? delta' :current-value)) ; Value changed
               ;; Sync dropdown with new state
               (common/update-dropdown-state! el delta')
               (render! el))))
   ;; Same logic for property changes
   :prop (fn [^js el delta]
           (let [delta' (common/enrich-delta delta el)]
             (when (or
                     (seq (dissoc delta "value"))
                     (not= delta delta')
                     (contains? delta' :current-value))
               (common/update-dropdown-state! el delta')
               (render! el))))})

