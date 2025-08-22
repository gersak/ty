(ns ty.components.dropdown.common
  "Shared functionality for dropdown component - used by both mobile and desktop implementations"
  (:require [ty.components.option]
            [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs]) ; Import ty-option component
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
  (when-let [slot (.querySelector shadow-root "slot:not([name])")]
    (let [assigned-elements (.assignedElements slot)]
      (->> assigned-elements
           array-seq
           (filter #(or (= (.-tagName %) "OPTION")
                        (= (.-tagName %) "TY-OPTION")))))))

(defn get-option-data
  "Extract value and text from option element (both <option> and <ty-option>)"
  [^js option]
  (let [tag-name (.-tagName option)]
    (cond
      (= tag-name "OPTION")
      {:value (or (.-value option) (.-textContent option))
       :text (.-textContent option)
       :element option}

      (= tag-name "TY-OPTION")
      {:value (or (.-value option) (.getAttribute option "value") (.-textContent option))
       :text (.-textContent option)
       :element option}

      :else
      {:value (.-textContent option)
       :text (.-textContent option)
       :element option})))

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
  "Remove selection from all options and clear selected slot"
  [^js shadow-root]
  ;; Clear selection attributes from original options
  (doseq [option (get-options shadow-root)]
    (.removeAttribute option "selected"))

  ;; Remove any clones from selected slot
  (when-let [selected-slot (.querySelector shadow-root "slot[name='selected']")]
    (doseq [clone (array-seq (.assignedElements selected-slot))]
      (.remove clone))))

(defn select-option!
  "Mark option as selected and clone to selected slot"
  [^js shadow-root option]
  (when option
    (clear-selection! shadow-root)

    ;; Clone the option for display in stub
    (let [clone (.cloneNode option true)]
      (.setAttribute clone "slot" "selected")
      ;; Add the clone to the DOM so it can render in the stub
      (.appendChild (.-parentNode option) clone))

    ;; Mark original as selected (stays in main slot for search)
    (.setAttribute option "selected" "")))

;; =====================================================
;; EVENT DISPATCHING
;; =====================================================

(defn dispatch-change-event!
  "Dispatch custom change event"
  [^js el option]
  (let [detail #js {:option option}
        event (js/CustomEvent. "change"
                               #js {:detail detail
                                    :bubbles true
                                    :cancelable true})]
    (.dispatchEvent el event)))

;; =====================================================
;; DISPLAY UPDATES
;; =====================================================

(defn update-selection-display!
  "Simple function to show/hide placeholder based on selected options"
  [^js el ^js shadow-root]
  (let [stub (.querySelector shadow-root ".dropdown-stub")
        options (get-options shadow-root)
        has-selected? (some #(.hasAttribute % "selected") options)]

    (when stub
      (if has-selected?
        (.add (.-classList stub) "has-selection")
        (.remove (.-classList stub) "has-selection")))))

;; =====================================================
;; DEVICE DETECTION
;; =====================================================

(defn is-mobile-device?
  "Detect if we're on a mobile device based on screen width and touch capability"
  []
  (let [width (.-innerWidth js/window)
        has-touch (or (exists? (.-ontouchstart js/window))
                      (> (.-maxTouchPoints js/navigator) 0))]
    (or (<= width 768)
        (and (<= width 1024) has-touch)))) ; Tablets with touch should use mobile mode

;; =====================================================
;; CSS UTILITIES
;; =====================================================

(defn ensure-dropdown-styles!
  "Ensure dropdown styles are loaded in shadow root"
  [^js shadow-root]
  (ensure-styles! shadow-root dropdown-styles "ty-dropdown"))
