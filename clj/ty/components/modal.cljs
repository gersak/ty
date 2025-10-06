(ns ty.components.modal
  "Modal web component wrapping native dialog element"
  (:require [ty.css :refer [ensure-styles!]]
            [ty.scroll-lock :as scroll-lock]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load modal styles from modal.css
(defstyles modal-styles)

;; =====================================================
;; Modal Attributes
;; =====================================================

(defn modal-attributes
  "Extract modal configuration from element attributes.
   Only accepts new industry-standard semantic "
  [^js el]
  {:open (wcs/parse-bool-attr el "open")
   :backdrop (if (wcs/attr el "backdrop")
               (wcs/parse-bool-attr el "backdrop")
               true) ; default to true if not specified
   :close-on-outside-click (if (wcs/attr el "close-on-outside-click")
                             (wcs/parse-bool-attr el "close-on-outside-click")
                             true) ; default to true if not specified
   :close-on-escape (if (wcs/attr el "close-on-escape")
                      (wcs/parse-bool-attr el "close-on-escape")
                      true) ; default to true if not specified
   :protected (wcs/parse-bool-attr el "protected")})

;; =====================================================
;; Dialog Management
;; =====================================================

;; Your close-icon SVG (preserved!)
(def close-icon
  "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\" class=\"lucide lucide-x-icon lucide-x\"><path d=\"M18 6 6 18\"/><path d=\"m6 6 12 12\"/></svg>")

(defn ensure-internal-dialog!
  "Ensure the internal dialog element exists in shadow DOM"
  [^js root]
  (or (.querySelector root "dialog")
      (let [dialog (.createElement js/document "dialog")
            close-button (.createElement js/document "button")
            content-div (.createElement js/document "div")
            slot (.createElement js/document "slot")]
        ;; Setup dialog structure
        (.setAttribute dialog "class" "ty-modal-dialog")

        ;; Setup close button with your SVG icon
        (.setAttribute close-button "class" "close-button")
        (.setAttribute close-button "type" "button")
        (.setAttribute close-button "aria-label" "Close modal")
        (set! (.-innerHTML close-button) close-icon)

        ;; Setup content container
        (.setAttribute content-div "class" "modal-content")
        (.appendChild content-div slot)

        ;; Assemble dialog structure: dialog > [close-button, content-div]
        (.appendChild dialog close-button)
        (.appendChild dialog content-div)
        (.appendChild root dialog)
        dialog)))

(defn dispatch-modal-event!
  "Dispatch custom modal events"
  [^js el event-type detail]
  (let [event (js/CustomEvent. event-type
                               #js {:detail detail
                                    :bubbles true
                                    :cancelable true})]
    (.dispatchEvent el event)))

;; =====================================================
;; Event Handlers
;; =====================================================

(defn setup-backdrop-click!
  "Setup backdrop click handling"
  [^js el ^js dialog enabled?]
  ;; Remove any existing backdrop click listener
  (when-let [old-listener (.-tyBackdropListener dialog)]
    (.removeEventListener dialog "click" old-listener))

  ;; Add new listener if enabled
  (when enabled?
    (let [listener (fn [e]
                     (when (= (.-target e) dialog) ; Click on backdrop, not content
                       (.preventDefault e)
                       (.stopPropagation e)
                       ;; Check if protected
                       (if (wcs/parse-bool-attr el "protected")
                         (when (js/confirm "You have unsaved changes. Are you sure you want to close?")
                           (.hide el))
                         (.hide el))))]
      (.addEventListener dialog "click" listener)
      (set! (.-tyBackdropListener dialog) listener))))

(defn setup-escape-key!
  "Setup escape key handling"
  [^js el ^js dialog enabled?]
  ;; Remove any existing escape key listener  
  (when-let [old-listener (.-tyEscapeListener dialog)]
    (.removeEventListener dialog "keydown" old-listener))

  ;; Add new listener if enabled
  (when enabled?
    (let [listener (fn [e]
                     (when (= (.-key e) "Escape")
                       (.preventDefault e)
                       (.stopPropagation e)
                       ;; Check if protected
                       (if (wcs/parse-bool-attr el "protected")
                         (when (js/confirm "You have unsaved changes. Are you sure you want to close?")
                           (.hide el))
                         (.hide el))))]
      (.addEventListener dialog "keydown" listener)
      (set! (.-tyEscapeListener dialog) listener))))

(defn setup-close-button!
  "Setup close button click handling"
  [^js el ^js dialog]
  (when-let [close-button (.querySelector dialog ".close-button")]
    ;; Remove any existing close button listener
    (when-let [old-listener (.-tyCloseButtonListener close-button)]
      (.removeEventListener close-button "click" old-listener))

    ;; Add close button click listener
    (let [listener (fn [e]
                     (.preventDefault e)
                     (.stopPropagation e)
                     ;; Check if protected
                     (if (wcs/parse-bool-attr el "protected")
                       (when (js/confirm "You have unsaved changes. Are you sure you want to close?")
                         (.hide el))
                       (.hide el)))]
      (.addEventListener close-button "click" listener)
      (set! (.-tyCloseButtonListener close-button) listener))))

(defn is-mobile? []
  (or (< (.-innerWidth js/window) 768)
      (.-maxTouchPoints js/navigator)))

(defn setup-close-button-hover!
  "Setup hover listeners on modal-content to show/hide close button"
  [^js el ^js dialog]
  (when-let [modal-content (.querySelector dialog ".modal-content")]
    (when-let [close-button (.querySelector dialog ".close-button")]
      ;; Remove any existing hover listeners
      (when-let [old-enter-listener (.-tyHoverEnterListener modal-content)]
        (.removeEventListener modal-content "mouseenter" old-enter-listener))
      (when-let [old-leave-listener (.-tyHoverLeaveListener modal-content)]
        (.removeEventListener modal-content "mouseleave" old-leave-listener))

      ;; Add mouseenter listener - hide close button when cursor enters modal-content
      (when-not (is-mobile?)
        (let [enter-listener (fn [e]
                               (.classList.add close-button "hide"))
              leave-listener (fn [e]
                               (.classList.remove close-button "hide"))]

          (.addEventListener modal-content "mouseenter" enter-listener)
          (.addEventListener modal-content "mouseleave" leave-listener)

        ;; Store listeners for cleanup
          (set! (.-tyHoverEnterListener modal-content) enter-listener)
          (set! (.-tyHoverLeaveListener modal-content) leave-listener))))))

;; =====================================================
;; Core Render Function
;; =====================================================

(defn render! [^js el]
  (let [{:keys [open backdrop close-on-outside-click close-on-escape]} (modal-attributes el)
        root (wcs/ensure-shadow el)
        dialog (ensure-internal-dialog! root)
        modal-id (str "modal-" (.-id el) "-" (hash el))] ; Unique ID for this modal

    ;; Ensure styles are loaded
    (ensure-styles! root modal-styles "ty-modal")

    (let [class-list "ty-modal-dialog"]
      (set! (.-className dialog) class-list))

    ;; Apply backdrop attribute
    (if backdrop
      (.setAttribute dialog "data-backdrop" "true")
      (.removeAttribute dialog "data-backdrop"))

    ;; Setup event handlers
    (setup-backdrop-click! el dialog close-on-outside-click)
    (setup-escape-key! el dialog close-on-escape)
    (setup-close-button! el dialog)
    (setup-close-button-hover! el dialog)

    ;; Sync open state with native dialog
    (if open
      (when-not (.-open dialog)
        ;; Lock scroll using new unified system
        (scroll-lock/lock-scroll! modal-id)
        (if backdrop
          (.showModal ^js dialog)
          (.show ^js dialog))
        (dispatch-modal-event! el "ty-modal-open" #js {}))
      (when (.-open dialog)
        ;; Unlock scroll using new unified system
        (scroll-lock/unlock-scroll! modal-id)
        (.close dialog)
        (dispatch-modal-event! el "ty-modal-close" #js {:reason "programmatic"})))

    ;; Handle dialog's native close event
    (set! (.-onclose dialog)
          (fn [e]
            ;; Ensure scroll is unlocked when dialog closes
            (scroll-lock/unlock-scroll! modal-id)
            ;; Sync the open attribute when dialog closes
            (when (wcs/parse-bool-attr el "open")
              (wcs/rm-attr! el "open")
              (dispatch-modal-event! el "ty-modal-close" #js {:reason "native"
                                                              :returnValue (.-returnValue dialog)}))))

    el))

;; =====================================================
;; Component Registration
;; =====================================================

(def configuration
  {:observed [:open :backdrop :close-on-outside-click :close-on-escape :protected]
   :connected (fn [^js el]
                (render! el)
                (set! (.-show el) (fn [] (.setAttribute el "open" true)))
                (set! (.-hide el) (fn [] (.removeAttribute el "open"))))
   :attr (fn [^js el ^js delta]
           (render! el))
   :prop (fn [^js el ^js delta]
           (render! el))
   :disconnected (fn [^js el]
                   (let [modal-id (str "modal-" (.-id el) "-" (hash el))]
                     ;; Ensure scroll is unlocked if component is removed
                     (scroll-lock/unlock-scroll! modal-id))
                   ;; Cleanup event listeners
                   (when-let [root (.-shadowRoot el)]
                     (when-let [dialog (.querySelector root "dialog")]
                       (when-let [listener (.-tyBackdropListener dialog)]
                         (.removeEventListener dialog "click" listener)
                         (set! (.-tyBackdropListener dialog) nil))
                       (when-let [listener (.-tyEscapeListener dialog)]
                         (.removeEventListener dialog "keydown" listener)
                         (set! (.-tyEscapeListener dialog) nil))
                       (when-let [close-button (.querySelector dialog ".close-button")]
                         (when-let [listener (.-tyCloseButtonListener close-button)]
                           (.removeEventListener close-button "click" listener)
                           (set! (.-tyCloseButtonListener close-button) nil)))
                       (when-let [modal-content (.querySelector dialog ".modal-content")]
                         (when-let [listener (.-tyHoverEnterListener modal-content)]
                           (.removeEventListener modal-content "mouseenter" listener)
                           (set! (.-tyHoverEnterListener modal-content) nil))
                         (when-let [listener (.-tyHoverLeaveListener modal-content)]
                           (.removeEventListener modal-content "mouseleave" listener)
                           (set! (.-tyHoverLeaveListener modal-content) nil))))))})

