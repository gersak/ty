(ns ty.components.dropdown.common
  "Shared functionality for dropdown component - used by both mobile and desktop implementations"
  (:require [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load dropdown styles
(defstyles dropdown-styles "ty/components/dropdown.css")

;; =====================================================
;; ATTRIBUTE PARSING
;; =====================================================

(defn dropdown-attributes
  "Read dropdown attributes directly from element"
  [^js el]
  {:value (or (wcs/attr el "value") "")
   :placeholder (or (wcs/attr el "placeholder") "Select an option...")
   :searchable (let [searchable? (wcs/parse-bool-attr el "searchable")
                     not-searchable? (wcs/parse-bool-attr el "not-searchable")]
                 (cond
                   searchable? true
                   not-searchable? false
                   :else true))
   :disabled (wcs/parse-bool-attr el "disabled")
   :readonly (wcs/parse-bool-attr el "readonly")
   :size (or (wcs/attr el "size") "md")
   :flavor (or (wcs/attr el "flavor") "neutral")})

;; =====================================================
;; COMPONENT STATE MANAGEMENT
;; =====================================================

(defn get-component-state
  "Get or initialize simplified component state"
  [^js el]
  (or (.-tyDropdownState el)
      (let [initial-state {:open false
                           :search ""
                           :highlighted-index -1
                           :filtered-options []}]
        (set! (.-tyDropdownState el) initial-state)
        initial-state)))

(defn set-component-state!
  "Update component state"
  [^js el updates]
  (let [new-state (merge (get-component-state el) updates)]
    (set! (.-tyDropdownState el) new-state)
    new-state))

;; =====================================================
;; OPTION MANAGEMENT
;; =====================================================

(defn get-options
  "Get all option elements from slot"
  [^js shadow-root]
  (when-let [slot (.querySelector shadow-root "slot")]
    (array-seq (.assignedElements slot))))

(defn get-option-data
  "Extract value and text from option element"
  [^js option]
  {:value (or (.-value option) (.-textContent option))
   :text (.-textContent option)
   :element option})

(defn filter-options
  "Filter options based on search string"
  [options search]
  (if (empty? search)
    options
    (let [search-lower (.toLowerCase search)]
      (filter (fn [{:keys [text]}]
                (.includes (.toLowerCase text) search-lower))
              options))))

(defn update-option-visibility!
  "Update visibility of options based on filtered list"
  [filtered-options all-options]
  (let [visible-values (set (map :value filtered-options))]
    (doseq [{:keys [value element]} all-options]
      (if (contains? visible-values value)
        (.removeAttribute element "hidden")
        (.setAttribute element "hidden" "")))))

(defn clear-highlights!
  "Remove highlighting from all options"
  [options]
  (doseq [{:keys [element]} options]
    (.removeAttribute element "highlighted")))

(defn highlight-option!
  "Highlight option at index"
  [options index]
  (clear-highlights! options)
  (when (and index (>= index 0) (< index (count options)))
    (let [{:keys [element]} (nth options index)]
      (.setAttribute element "highlighted" ""))))

(defn clear-selection!
  "Remove selection from all options"
  [options]
  (doseq [{:keys [element]} options]
    (.removeAttribute element "selected")))

(defn select-option!
  "Mark option as selected"
  [options value]
  (clear-selection! options)
  (when-let [option (first (filter #(= (:value %) value) options))]
    (.setAttribute (:element option) "selected" "")))

;; =====================================================
;; EVENT DISPATCHING
;; =====================================================

(defn dispatch-change-event!
  "Dispatch custom change event"
  [^js el value text]
  (let [detail (js-obj "value" value "text" text)
        event (js/CustomEvent. "change"
                               #js {:detail detail
                                    :bubbles true
                                    :cancelable true})]
    (.dispatchEvent el event)))

;; =====================================================
;; DISPLAY UPDATES
;; =====================================================

(defn update-stub-display!
  "Update stub to show selected value or placeholder"
  [^js el ^js shadow-root]
  (let [{:keys [value placeholder]} (dropdown-attributes el)
        stub-value (.querySelector shadow-root ".dropdown-value")
        options (map get-option-data (get-options shadow-root))]
    (when stub-value
      (if-let [selected-option (first (filter #(= (:value %) value) options))]
        (do
          (set! (.-textContent stub-value) (:text selected-option))
          (select-option! options value))
        (set! (.-textContent stub-value) placeholder)))))

;; =====================================================
;; DEVICE DETECTION
;; =====================================================

(defn is-mobile-device?
  "Detect if we're on a mobile device based on screen width"
  []
  (<= (.-innerWidth js/window) 768))

;; =====================================================
;; CSS UTILITIES
;; =====================================================

(defn ensure-dropdown-styles!
  "Ensure dropdown styles are loaded in shadow root"
  [^js shadow-root]
  (ensure-styles! shadow-root dropdown-styles "ty-dropdown"))
