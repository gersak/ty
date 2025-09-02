(ns ty.components.dropdown.global
  "Global dropdown management - ensures only one dropdown is open at a time")

;; =====================================================
;; GLOBAL DROPDOWN MANAGEMENT
;; =====================================================

(defonce current-open-dropdown (atom nil))

(defn close-current-dropdown!
  "Close the currently open dropdown if any"
  []
  (when-let [dropdown @current-open-dropdown]
    (let [shadow-root (.-shadowRoot dropdown)]
      (when shadow-root
        ;; Close the dropdown using appropriate method based on device
        (if-let [dialog (.querySelector shadow-root ".dropdown-dialog")]
          ;; Desktop: close dialog
          (do
            (.remove (.-classList dialog) "position-above" "position-below")
            (.close dialog))
          ;; Mobile: close modal
          (when-let [modal (.querySelector shadow-root ".mobile-dropdown-modal")]
            (.removeAttribute modal "open")))
        
        ;; Clean up visual state
        (when-let [chevron (.querySelector shadow-root ".dropdown-chevron")]
          (.remove (.-classList chevron) "open"))
        (when-let [search-chevron (.querySelector shadow-root ".dropdown-search-chevron")]
          (.remove (.-classList search-chevron) "open"))))
    (reset! current-open-dropdown nil)))

(defn set-current-dropdown!
  "Set the current open dropdown, closing any previous one"
  [dropdown]
  (close-current-dropdown!)
  (reset! current-open-dropdown dropdown))

(defn clear-current-dropdown!
  "Clear the current dropdown from global state (used when dropdown closes itself)"
  [dropdown]
  (when (= @current-open-dropdown dropdown)
    (reset! current-open-dropdown nil)))
