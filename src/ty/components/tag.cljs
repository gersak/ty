(ns ty.components.tag
  "Tag component with slots and semantic flavors"
  (:require [ty.css :refer [ensure-styles!]]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load tag styles
(defstyles tag-styles)

(defn get-tag-value
  "Get value from either property or attribute"
  [^js el]
  (or (.-value el)                    ; First check property
      (wcs/attr el "value")))          ; Then check attribute

(defn tag-attributes
  "Extract tag configuration from element attributes"
  [^js el]
  {:value (get-tag-value el)  ; Use helper to get value
   :size (or (wcs/attr el "size") "md") ; Still default to "md" internally for consistency
   :flavor (or (wcs/attr el "flavor") "neutral")
   :pill (let [pill? (wcs/parse-bool-attr el "pill")
               not-pill? (wcs/parse-bool-attr el "not-pill")]
           (cond
             pill? true
             not-pill? false
             :else true)) ; default to true (pill shape)
   :clickable (wcs/parse-bool-attr el "clickable")
   :dismissible (wcs/parse-bool-attr el "dismissible")
   :disabled (wcs/parse-bool-attr el "disabled")})

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
      (dispatch-tag-event! el "ty-tag-click" #js {:target el}))))

(defn handle-dismiss!
  "Handle tag dismiss events"
  [^js el ^js event]
  (let [{:keys [disabled dismissible]} (tag-attributes el)]
    (when (and dismissible (not disabled))
      (.preventDefault event)
      (.stopPropagation event)
      (dispatch-tag-event! el "ty-tag-dismiss" #js {:target el}))))

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
                  (dispatch-tag-event! el "ty-tag-click" #js {:target el}))
        ;; DELETE or BACKSPACE - trigger dismiss if dismissible
        (8 46) (when dismissible
                 (.preventDefault event)
                 (dispatch-tag-event! el "ty-tag-dismiss" #js {:target el}))
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
               "    <slot name=\"start\"></slot>"
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

(wcs/define! "ty-tag"
  {:observed [:size :flavor :pill :not-pill :clickable :dismissible :disabled :slot :value]
   :connected render!
   :disconnected cleanup!
   :attr (fn [^js el attr-name _old new]
           ;; Re-render on attribute changes
           (render! el))})
