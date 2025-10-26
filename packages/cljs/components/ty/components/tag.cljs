(ns ty.components.tag
  "Tag component with slots and semantic flavors"
  (:require [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load tag styles
(defstyles tag-styles)

;; =====================================================
;; Semantic Flavor Normalization
;; =====================================================

(defn validate-flavor
  "Validate that flavor uses new industry-standard semantic naming.
   For tags, flavor indicates semantic meaning (status, category, importance)."
  [flavor]
  (let [valid-flavors #{"primary" "secondary" "success" "danger" "warning" "neutral"}
        normalized (or flavor "neutral")]
    (when (and goog.DEBUG (not (contains? valid-flavors normalized)))
      (js/console.warn (str "[ty-tag] Invalid flavor '" flavor "'. Using 'neutral'. "
                            "Valid flavors: primary, secondary, success, danger, warning, neutral.")))
    (if (contains? valid-flavors normalized)
      normalized
      "neutral")))

(defn get-tag-value
  "Get value from either property or attribute"
  [^js el]
  (or (.-value el) ; First check property
      (wcs/attr el "value"))) ; Then check attribute

;; =====================================================
;; Property-Attribute Synchronization
;; =====================================================

(defn sync-properties-to-attributes!
  "Sync JavaScript properties to HTML attributes for CSS/DevTools consistency"
  [^js el delta]
  (when (contains? delta "value")
    (let [value (get delta "value")]
      (if (some? value)
        (.setAttribute el "value" (str value))
        (.removeAttribute el "value"))))

  (when (contains? delta "selected")
    (let [selected (get delta "selected")]
      (if selected
        (.setAttribute el "selected" "")
        (.removeAttribute el "selected")))))

(defn sync-attributes-to-properties!
  "Ensure properties reflect attribute changes"
  [^js el delta]
  (when (contains? delta "value")
    (set! (.-value el) (get delta "value")))

  (when (contains? delta "selected")
    (set! (.-selected el) (boolean (get delta "selected")))))

(defn tag-attributes
  "Extract tag configuration from element attributes.
   Only accepts new industry-standard semantic flavors."
  [^js el]
  (let [raw-flavor (wcs/attr el "flavor")
        validated-flavor (validate-flavor raw-flavor)]
    {:value (get-tag-value el) ; Use helper to get value
     :size (or (wcs/attr el "size") "md") ; Still default to "md" internally for consistency
     :flavor validated-flavor
     :pill (let [pill? (wcs/parse-bool-attr el "pill")
                 not-pill? (wcs/parse-bool-attr el "not-pill")]
             (cond
               (true? pill?) true
               (false? pill?) false
               (true? not-pill?) false
               :else true)) ; default to true (pill shape)
     :clickable (wcs/parse-bool-attr el "clickable")
     :dismissible (wcs/parse-bool-attr el "dismissible")
     :disabled (wcs/parse-bool-attr el "disabled")}))

(defn dispatch-tag-event!
  "Dispatch custom tag events"
  [^js el event-type detail]
  (let [event (js/CustomEvent. event-type
                               #js {:detail detail
                                    :bubbles true
                                    :cancelable true})]
    (.dispatchEvent el event)))

(defn handle-click!
  "Handle tag click events"
  [^js el ^js event]
  (let [{:keys [disabled clickable]} (tag-attributes el)]
    (when (and clickable (not disabled))
      (.preventDefault event)
      (.stopPropagation event)
      (dispatch-tag-event! el "click" #js {:target el})
      (dispatch-tag-event! el "click" #js {:target el})
      (dispatch-tag-event! el "pointerdown" #js {:target el}))))

(defn handle-dismiss!
  "Handle tag dismiss events"
  [^js el ^js event]
  (let [{:keys [disabled dismissible]} (tag-attributes el)]
    (when (and dismissible (not disabled))
      (.preventDefault event)
      (.stopPropagation event)
      (dispatch-tag-event! el "dismiss" #js {:target el}))))

(defn handle-keydown!
  "Handle keyboard interactions"
  [^js el ^js event]
  (let [{:keys [disabled clickable dismissible]} (tag-attributes el)
        key-code (.-keyCode event)]
    (when-not disabled
      (case key-code
        ;; ENTER or SPACE - trigger click if clickable
        (13 32) (when clickable
                  (.preventDefault event)
                  (dispatch-tag-event! el "click" #js {:target el}))
        ;; DELETE or BACKSPACE - trigger dismiss if dismissible
        (8 46) (when dismissible
                 (.preventDefault event)
                 (dispatch-tag-event! el "dismiss" #js {:target el}))
        nil))))

(defn cleanup-event-listeners!
  "Clean up existing event listeners"
  [^js el]
  (when-let [cleanup-fn (.-tyTagCleanup el)]
    (cleanup-fn)
    (set! (.-tyTagCleanup el) nil)))

(defn setup-event-listeners!
  "Setup event listeners for tag interactions"
  [^js el ^js shadow-root]
  ;; Clean up any existing listeners first
  (cleanup-event-listeners! el)

  (let [container (.querySelector shadow-root ".tag-container")
        dismiss-btn (.querySelector shadow-root ".tag-dismiss")]

    ;; Click handler for main tag
    (when container
      (.addEventListener container "click" (partial handle-click! el))
      (.addEventListener container "keydown" (partial handle-keydown! el)))

    ;; Dismiss button handler
    (when dismiss-btn
      (.addEventListener dismiss-btn "click" (partial handle-dismiss! el)))

    ;; Store cleanup function
    (set! (.-tyTagCleanup el)
          (fn []
            (when container
              (.removeEventListener container "click" (partial handle-click! el))
              (.removeEventListener container "keydown" (partial handle-keydown! el)))
            (when dismiss-btn
              (.removeEventListener dismiss-btn "click" (partial handle-dismiss! el)))))))

(defn render! [^js el]
  (let [root (wcs/ensure-shadow el)
        {:keys [value size flavor clickable dismissible disabled]} (tag-attributes el)]

    ;; Ensure styles are loaded
    (ensure-styles! root tag-styles "ty-tag")

    ;; Ensure value is also set as attribute for CSS selectors and debugging
    (when value
      (when-not (.hasAttribute el "value")
        (.setAttribute el "value" value)))

    ;; Always recreate structure to handle attribute changes properly
    (set! (.-innerHTML root)
          (str "<div class=\"tag-container\""
               (when clickable " tabindex=\"0\"")
               (when disabled " aria-disabled=\"true\"")
               ;; Add data-value for CSS access if needed
               (when value (str " data-value=\"" value "\""))
               ">"
               "  <div class=\"tag-start\">"
               "    <slot name=\"start\" class=\"tag-start\"></slot>"
               "  </div>"
               "  <div class=\"tag-content\">"
               "    <slot></slot>"
               "  </div>"
               "  <div class=\"tag-end\">"
               "    <slot name=\"end\"></slot>"
               (when dismissible
                 (str "    <button class=\"tag-dismiss\" type=\"button\" aria-label=\"Remove tag\">"
                      "      <svg viewBox=\"0 0 20 20\" fill=\"currentColor\">"
                      "        <path fill-rule=\"evenodd\" d=\"M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z\" clip-rule=\"evenodd\" />"
                      "      </svg>"
                      "    </button>"))
               "  </div>"
               "</div>"))

    ;; Setup event listeners
    (setup-event-listeners! el root)))

(defn cleanup! [^js el]
  (cleanup-event-listeners! el))

(def configuration
  {:observed [:size :flavor :pill :not-pill :clickable :dismissible :disabled :slot :value :selected]
   :props {:value nil
           :selected nil} ; Enable property watching  
   :connected render!
   :disconnected cleanup!
   :attr (fn [^js el delta]
           ;; Sync attribute changes to properties
           (sync-attributes-to-properties! el delta)
           (render! el))
   :prop (fn [^js el delta]
           ;; Sync property changes to attributes  
           (sync-properties-to-attributes! el delta)
           (render! el))})

