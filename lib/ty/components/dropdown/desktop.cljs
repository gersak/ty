(ns ty.components.dropdown.desktop
  "Desktop implementation of dropdown using dialog element with smart positioning"
  (:require
    [ty.components.dropdown.common :as common]
    [ty.components.dropdown.global :as global]
    [ty.shim :as wcs]))

;; =====================================================
;; DESKTOP POSITIONING
;; =====================================================

(defn update-position!
  "Smart positioning using CSS classes"
  [^js el ^js shadow-root]
  (let [wrapper (.querySelector shadow-root ".dropdown-wrapper")
        dialog (.querySelector shadow-root ".dropdown-dialog")
        {:keys [open]} (common/get-component-state el)]
    (when (and open wrapper dialog)
      (let [wrapper-rect (.getBoundingClientRect wrapper)
            wrapper-bottom (.-bottom wrapper-rect)
            viewport-height (.-innerHeight js/window)
            space-below (- viewport-height wrapper-bottom)
            position-below (> space-below 300)]
        (.remove (.-classList dialog) "position-above" "position-below")
        (.add (.-classList dialog) (if position-below "position-below" "position-above"))))))

;; =====================================================
;; DESKTOP DROPDOWN CONTROL
;; =====================================================

(defn close-dropdown!
  "Close dropdown dialog"
  [^js el]
  (let [shadow-root (wcs/ensure-shadow el)]
    (when-let [dialog (.querySelector shadow-root ".dropdown-dialog")]
      (.remove (.-classList dialog) "position-above" "position-below")
      (.close dialog)))
  ;; Clear from global state if this is the current dropdown
  (global/clear-current-dropdown! el))

(defn open-dropdown!
  "Open dropdown dialog with keyboard navigation setup"
  [^js el ^js shadow-root]
  (let [{:keys [disabled readonly searchable value]} (common/dropdown-attributes el)]
    (when-not (or disabled readonly)
      ;; Close any currently open dropdown before opening this one
      (global/set-current-dropdown! el)

      ;; Initialize state for keyboard navigation
      (let [all-options (map common/get-option-data (common/get-options shadow-root))
            current-search (if searchable (:search (common/get-component-state el)) "")
            filtered (if searchable
                       (common/filter-options all-options current-search)
                       all-options)
            selected-index (when (and value (seq filtered))
                             (first (keep-indexed
                                      (fn [idx option]
                                        (when (= (:value option) value) idx))
                                      filtered)))]

        (common/set-component-state! el {:open true
                                         :filtered-options filtered
                                         :highlighted-index (or selected-index -1)}))

      (let [dialog (.querySelector shadow-root ".dropdown-dialog")
            chevron (.querySelector shadow-root ".dropdown-chevron")
            search-chevron (.querySelector shadow-root ".dropdown-search-chevron")
            input (.querySelector shadow-root ".dropdown-search-input")]

        ;; Show all options for non-searchable dropdowns
        (when-not searchable
          (let [options (map common/get-option-data (common/get-options shadow-root))]
            (common/update-option-visibility! options options)))

        (when chevron
          (.add (.-classList chevron) "open"))
        (when search-chevron
          (.add (.-classList search-chevron) "open"))

        (when dialog
          (.show dialog)
          (when (and input searchable)
            (set! (.-value input) ""))
          (update-position! el shadow-root)

          ;; Highlight selected option after brief delay
          (js/setTimeout
            (fn []
              (let [{:keys [highlighted-index filtered-options]} (common/get-component-state el)]
                (when (>= highlighted-index 0)
                  (common/highlight-option! filtered-options highlighted-index))))
            50))))))

;; =====================================================
;; EVENT HANDLERS
;; =====================================================

(defn handle-outside-click!
  "Close dropdown when clicking outside"
  [^js el ^js shadow-root ^js event]
  (let [{:keys [open]} (common/get-component-state el)]
    (when open
      (let [wrapper (.querySelector shadow-root ".dropdown-wrapper")
            target (.-target event)]
        (when (and wrapper (not (.contains wrapper target)))
          (.preventDefault event)
          (.stopPropagation event)
          (close-dropdown! el))))))

(defn handle-dialog-close!
  "Handle dialog close event for cleanup"
  [^js el ^js shadow-root ^js event]
  (common/set-component-state! el {:open false
                                   :highlighted-index -1})
  (when-let [chevron (.querySelector shadow-root ".dropdown-chevron")]
    (.remove (.-classList chevron) "open"))
  (when-let [search-chevron (.querySelector shadow-root ".dropdown-search-chevron")]
    (.remove (.-classList search-chevron) "open"))
  (let [{:keys [searchable]} (common/dropdown-attributes el)]
    (when-not searchable
      (common/set-component-state! el {:search ""}))))

(defn handle-stub-click!
  "Open dropdown when clicking stub"
  [^js el ^js shadow-root ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (let [{:keys [disabled]} (common/dropdown-attributes el)]
    (when-not disabled
      (open-dropdown! el shadow-root))))

(defn handle-input-change!
  "Handle search input changes"
  [^js el ^js shadow-root ^js event]
  (let [{:keys [searchable external-search]} (common/dropdown-attributes el)]
    (when searchable
      (let [search (.-value (.-target event))]
        (common/set-component-state! el {:search search})

        (if external-search
          ;; External search: dispatch event for external handling
          (common/dispatch-search-event! el search)

          ;; Internal search: filter options locally
          (let [options (map common/get-option-data (common/get-options shadow-root))
                filtered (common/filter-options options search)
                {:keys [value]} (common/dropdown-attributes el)
                new-highlighted-index (when (and value (seq filtered))
                                        (first (keep-indexed
                                                 (fn [idx option]
                                                   (when (= (:value option) value) idx))
                                                 filtered)))]
            (common/set-component-state! el {:filtered-options filtered
                                             :highlighted-index (or new-highlighted-index -1)})
            (common/update-option-visibility! filtered options)
            (common/clear-highlights! options)
            (when (>= (or new-highlighted-index -1) 0)
              (common/highlight-option! filtered new-highlighted-index))))))))

(defn handle-search-blur!
  "Reset search when search input loses focus"
  [^js el ^js shadow-root ^js event]
  (let [{:keys [searchable]} (common/dropdown-attributes el)]
    (when searchable
      (common/set-component-state! el {:search ""})
      (let [options (map common/get-option-data (common/get-options shadow-root))]
        (when (seq options)
          (common/set-component-state! el {:filtered-options options
                                           :highlighted-index -1})
          (common/update-option-visibility! options options)
          (common/clear-highlights! options)))
      (when-let [search-input (.querySelector shadow-root ".dropdown-search-input")]
        (set! (.-value search-input) "")))))

(defn handle-option-click!
  "Handle option selection"
  [^js el _ ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (let [option-element (.-target event)]
    (common/select-option! el option-element)
    (common/update-selection-display! el)
    (common/dispatch-change-event! el option-element)
    (close-dropdown! el)))

(defn handle-keyboard!
  "Handle keyboard navigation"
  [^js el ^js shadow-root ^js event]
  (let [{:keys [open highlighted-index filtered-options]} (common/get-component-state el)
        key-code (.-keyCode event)]
    (case key-code
      27 (do ; ESCAPE
           (.preventDefault event)
           (.stopPropagation event)
           (close-dropdown! el))
      13 (do ; ENTER
           (.preventDefault event)
           (when (and open (>= highlighted-index 0) (< highlighted-index (count filtered-options)))
             (let [{:keys [element]} (nth filtered-options highlighted-index)]
               (common/select-option! shadow-root element)
               (common/dispatch-change-event! el element)
               (close-dropdown! el))))
      38 (when open ; UP ARROW
           (.preventDefault event)
           (let [new-index (if (= highlighted-index -1)
                             (dec (count filtered-options))
                             (max -1 (dec highlighted-index)))]
             (common/set-component-state! el {:highlighted-index new-index})
             (common/highlight-option! filtered-options new-index)))
      40 (when open ; DOWN ARROW
           (.preventDefault event)
           (let [new-index (if (= highlighted-index -1)
                             0
                             (min (dec (count filtered-options)) (inc highlighted-index)))]
             (common/set-component-state! el {:highlighted-index new-index})
             (common/highlight-option! filtered-options new-index)))
      nil)))

;; =====================================================
;; EVENT SETUP
;; =====================================================

(defn setup-event-listeners!
  "Setup all event listeners for desktop mode"
  [^js el]
  (let [^js shadow-root (wcs/ensure-shadow el)
        stub (.querySelector shadow-root ".dropdown-stub")
        search-input (.querySelector shadow-root ".dropdown-search-input")
        slot (.querySelector shadow-root "#options-slot")
        dialog (.querySelector shadow-root ".dropdown-dialog")
        outside-click-handler (partial handle-outside-click! el shadow-root)]

    ;; Setup event listeners
    (when stub
      (.addEventListener stub "click" (partial handle-stub-click! el shadow-root)))
    (when search-input
      (.addEventListener search-input "input" (partial handle-input-change! el shadow-root))
      (.addEventListener search-input "keydown" (partial handle-keyboard! el shadow-root))
      (.addEventListener search-input "blur" (partial handle-search-blur! el shadow-root)))
    (when slot
      (.addEventListener slot "click" (partial handle-option-click! el shadow-root)))
    (when dialog
      (.addEventListener dialog "close" (partial handle-dialog-close! el shadow-root)))

    (.addEventListener js/document "click" outside-click-handler)
    (set! (.-tyOutsideClickHandler el) outside-click-handler)

    ;; Store cleanup function
    (set! (.-tyDropdownCleanup el)
          (fn []
            (when stub
              (.removeEventListener stub "click" (partial handle-stub-click! el shadow-root)))
            (when search-input
              (.removeEventListener search-input "input" (partial handle-input-change! el shadow-root))
              (.removeEventListener search-input "keydown" (partial handle-keyboard! el shadow-root))
              (.removeEventListener search-input "blur" (partial handle-search-blur! el shadow-root)))
            (when slot
              (.removeEventListener slot "click" (partial handle-option-click! el shadow-root)))
            (when dialog
              (.removeEventListener dialog "close" (partial handle-dialog-close! el shadow-root)))
            (when-let [handler (.-tyOutsideClickHandler el)]
              (.removeEventListener js/document "click" handler)
              (set! (.-tyOutsideClickHandler el) nil))))))

;; =====================================================
;; DESKTOP RENDERING
;; =====================================================

(defn render!
  "Desktop implementation using dialog structure (self-contained)"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        {:keys [placeholder searchable disabled label required]} (common/dropdown-attributes el)]
    (when-not (.querySelector root ".dropdown-container")
      (set! (.-innerHTML root)
            (str
             ;; Container with label
              "<div class=\"dropdown-container\">"
             ;; Label (optional)
              "  <label class=\"dropdown-label\" style=\"" (if label "display: flex; align-items: center;" "display: none;") "\">"
              (or label "")
              (when (and label required) (str " <span class=\"required-icon\">" common/required-icon "</span>"))
              "  </label>"
             ;; Dropdown wrapper
              "  <div class=\"dropdown-wrapper\">"
              "    <div class=\"dropdown-stub\"" (when disabled " disabled") ">"
              "      <slot name=\"selected\"></slot>"
              "      <span class=\"dropdown-placeholder\">" placeholder "</span>"
              "    </div>"
              "    <div class=\"dropdown-chevron\">"
              "      <svg viewBox=\"0 0 20 20\" fill=\"currentColor\">"
              "        <path fill-rule=\"evenodd\" d=\"M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z\" clip-rule=\"evenodd\" />"
              "      </svg>"
              "    </div>"
              "    <dialog class=\"dropdown-dialog\">"
              "      <div class=\"dropdown-header\">"
              "        <input class=\"dropdown-search-input\" type=\"text\""
              "               placeholder=\"" (if searchable "Search..." placeholder) "\""
              (when disabled " disabled") " />"
              "        <div class=\"dropdown-search-chevron\">"
              "          <svg viewBox=\"0 0 20 20\" fill=\"currentColor\">"
              "            <path fill-rule=\"evenodd\" d=\"M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z\" clip-rule=\"evenodd\" />"
              "          </svg>"
              "        </div>"
              "      </div>"
              "      <div class=\"dropdown-options\">"
              "        <slot id=\"options-slot\"></slot>"
              "      </div>"
              "    </dialog>"
              "  </div>"
              "</div>"))
      (setup-event-listeners! el))

    ;; Ensure all options visible for non-searchable dropdowns
    (when-not searchable
      (let [options (map common/get-option-data (common/get-options root))]
        (when (seq options)
          (common/update-option-visibility! options options))))))
