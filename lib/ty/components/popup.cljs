(ns ty.components.popup
  "Interactive popup component with dropdown-like behavior and tooltip positioning.
   Uses parent-child relationship - popup opens on click, stays open for interaction,
   closes on outside click or ESC key."
  (:require [ty.css :refer [ensure-styles!]]
            [ty.positioning :as pos]
            [ty.scroll-lock :as scroll-lock]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; Load popup styles
(defstyles popup-styles)

(defn popup-attributes
  "Read popup attributes with simplified API"
  [^js el]
  {:manual (wcs/parse-bool-attr el "manual") ; Override click trigger  
   :disable-close (wcs/parse-bool-attr el "disable-close") ; Disable auto-close
   :placement (or (wcs/attr el "placement") "bottom")
   :offset (or (wcs/parse-int-attr el "offset") 8)})

(defn get-anchor-element
  "Get the parent element as anchor (like tooltip)"
  [^js el]
  (.-parentElement el))

(defn get-popup-dialog
  "Get the popup dialog element"
  [^js shadow-root]
  (.querySelector shadow-root ".popup-dialog"))

(defn update-position!
  "Calculate and update popup position based on anchor (tooltip style)"
  [^js el]
  (let [{:keys [placement offset]} (popup-attributes el)
        shadow-root (.-shadowRoot el)
        anchor (get-anchor-element el)
        dialog (get-popup-dialog shadow-root)]
    (when (and anchor dialog)
      ;; Calculate preferred placements based on placement attribute (like tooltip)
      (let [preferences (case placement
                          "top" [:top :bottom :left :right]
                          "bottom" [:bottom :top :left :right]
                          "left" [:left :right :top :bottom]
                          "right" [:right :left :top :bottom]
                          ;; Default popup placement (prefer bottom like dropdown)
                          [:bottom :top :right :left])
            ;; Use positioning engine to find best position
            position-data (pos/find-best-position
                            {:target-el anchor
                             :floating-el dialog
                             :preferences preferences
                             :offset offset
                             :padding 8})
            {:keys [x y]} position-data]
        ;; Update CSS variables (popup-specific names)
        (.setProperty (.-style el) "--popup-x" (str x "px"))
        (.setProperty (.-style el) "--popup-y" (str y "px"))

        ;; Add position class like dropdown does (this applies the variables)
        (.add (.-classList dialog) "position-calculated")))))

(defn close-popup!
  "Close popup with clean animation -> position reset -> hide sequence"
  ([^js el] (close-popup! el false))
  ([^js el force?]
   (let [{:keys [disable-close]} (popup-attributes el)]
     (when (or force? (not disable-close))
       (let [shadow-root (.-shadowRoot el)
             dialog (get-popup-dialog shadow-root)
             popup-id (str "popup-" (or (.-id el) (hash el)))]
         (when dialog
           (.remove (.-classList dialog) "open")
           (.remove (.-classList dialog) "preparing-animation")

           (js/setTimeout
             (fn []
               (.remove (.-classList dialog) "position-calculated")
               (.close dialog))
             150))

         ;; Dispatch close event
         (.dispatchEvent el (js/CustomEvent. "ty:popup-close" #js {:bubbles true})))))))

(defn open-popup!
  "Open popup with dialog open but hidden → position → animate sequence"
  [^js el]
  (let [popup-id (str "popup-" (or (.-id el) (hash el)))]
    ;; Lock scroll to prevent body scrolling
    (scroll-lock/lock-scroll! popup-id)

    (let [shadow-root (.-shadowRoot el)
          dialog (get-popup-dialog shadow-root)]
      (when dialog
        ;; Step 1: showModal() immediately - dialog gets [open] attribute and proper layout
        (.showModal dialog)
        ;; Step 2: Wait for layout to settle, then measure and position
        (.requestAnimationFrame
          js/window
          (fn []
            ;; Now dialog has [open] and accurate dimensions
            ;; Calculate and apply position while dialog is hidden by CSS
            (update-position! el)
            ;; Step 3: Add scale transform for animation prep
            (.add (.-classList dialog) "preparing-animation")

            ;; Step 4: Third RAF - THEN add open class for smooth scale animation
            (.requestAnimationFrame
              js/window
              (fn []
                ;; Now animate: scale(0.95) → scale(1) + opacity + visibility
                (.add (.-classList dialog) "open")

                ;; Dispatch open event
                (.dispatchEvent el (js/CustomEvent. "ty:popup-open" #js {:bubbles true}))))))))))

(defn toggle-popup!
  "Toggle popup visibility"
  [^js el]
  (let [shadow-root (.-shadowRoot el)
        dialog (get-popup-dialog shadow-root)]
    (if (and dialog (.contains (.-classList dialog) "open"))
      (close-popup! el)
      (open-popup! el))))

(defn handle-anchor-click
  "Handle click on anchor element"
  [^js el ^js event]
  (.preventDefault event)
  (.stopPropagation event)
  (toggle-popup! el))

(defn handle-popup-dialog-click
  "Handle clicks on the dialog element (backdrop clicks)"
  [^js el ^js event]
  (let [shadow-root (.-shadowRoot el)
        dialog (get-popup-dialog shadow-root)]
    ;; Close if clicking on dialog backdrop (not popup content)
    (when (= (.-target event) dialog)
      (.preventDefault event)
      (.stopPropagation event)
      (close-popup! el))))

(defn handle-popup-content-click
  "Handle clicks inside popup content - prevent closing"
  [^js event]
  ;; Stop the event from bubbling to document level
  (.stopPropagation event))

(defn handle-close-request
  "Handle close requests from inside popup content"
  [^js el ^js event]
  (.stopPropagation event)
  (close-popup! el true))

(defn setup-anchor-events!
  "Setup click listener on anchor element (unless manual=true)"
  [^js el]
  (let [{:keys [manual]} (popup-attributes el)
        anchor (get-anchor-element el)]
    (when (and anchor (not manual))
      ;; Remove any existing listener first
      (when (.-_popupClickHandler el)
        (.removeEventListener anchor "pointerdown" (.-_popupClickHandler el)))

      ;; Create new handler and store reference
      (let [handler (partial handle-anchor-click el)]
        (set! (.-_popupClickHandler el) handler)
        (.addEventListener anchor "pointerdown" handler)))))

(defn cleanup-anchor-events!
  "Cleanup anchor event listeners"
  [^js el]
  (let [anchor (get-anchor-element el)]
    (when (and anchor (.-_popupClickHandler el))
      (.removeEventListener anchor "pointerdown" (.-_popupClickHandler el))
      (set! (.-_popupClickHandler el) nil))))

(defn render!
  "Render the popup structure"
  [^js el]
  (let [shadow-root (wcs/ensure-shadow el)
        existing-dialog (.querySelector shadow-root ".popup-dialog")]

    ;; Ensure styles are loaded
    (ensure-styles! shadow-root popup-styles "ty-popup")

    ;; Create structure if it doesn't exist
    (when-not existing-dialog
      (let [dialog (js/document.createElement "dialog")
            content (js/document.createElement "slot")]
        (set! (.-className dialog) "popup-dialog")
        (set! (.-id content) "popup-content")
        (.appendChild shadow-root dialog)
        (.appendChild dialog content)
        ;; Initialize position
        (.setProperty (.-style el) "--popup-x" "0px")
        (.setProperty (.-style el) "--popup-y" "0px")

        (letfn [(outside-click [^js e]
                  (.stopPropagation e)
                  (let [target (.-target e)]
                    ;; Only close if click is outside the date picker element itself
                    (when (not (.contains el target))
                      (close-popup! el))))
                (on-escape [^js event]
                  (.stopPropagation event)
                  (println "PICKED UP ESCAPE")
                  (when (= (.-key event) "Escape")
                    (let [{:keys [disable-close]} (popup-attributes el)]
                      (if disable-close (.preventDefault event)
                          (close-popup! el)))))]
          (.addEventListener dialog "pointerdown" outside-click)
          (.addEventListener el "keydown" on-escape)
          (set! (.-outsideHandler el) outside-click))
        ;; Handle native dialog close event
        (.addEventListener dialog "close"
                           (fn [^js _event]
                             ;; Update component state when dialog closes natively
                             (let [popup-id (str "popup-" (or (.-id el) (hash el)))]
                               (scroll-lock/unlock-scroll! popup-id)
                               (.remove (.-classList dialog) "open")
                               (.remove (.-classList dialog) "preparing-animation"))
                             (.dispatchEvent el (js/CustomEvent. "ty:popup-close" #js {:bubbles true}))))))

    ;; Add close method to element for programmatic access
    (set! (.-closePopup el) #(close-popup! el true))
    (set! (.-openPopup el) #(open-popup! el))
    (set! (.-togglePopup el) #(toggle-popup! el))

    ;; Add event listener for close requests from content
    (.addEventListener el "ty:close-popup" (partial handle-close-request el))

    ;; Setup anchor events
    (setup-anchor-events! el)))

(defn cleanup!
  "Cleanup all resources when component disconnects"
  [^js el]
  ;; Close popup if it's currently open

  (let [outside-click (.-outsideHandler el)
        escape (.-escapeHandler el)]
    (when outside-click (.removeEventListener (get-popup-dialog el) "pointerdown" outside-click))
    (when escape (.removeEventListener el "keydown" escape)))

  ;; Cleanup anchor events
  (cleanup-anchor-events! el))

;; Global event listeners (like dropdown)
(defonce global-listeners-setup? (atom false))

(def configuration
  {:observed [:manual :disable-close :placement :offset]
   :connected (fn [^js el]
                ;; Render component
                (render! el))
   :disconnected cleanup!
   :attr (fn [^js el delta]
           ;; Re-render on attribute changes
           (render! el)

           ;; Handle specific attribute changes
           (doseq [[attr-name {:keys [new]}] delta]
             (case attr-name
               ;; Update position immediately if open and placement-related attributes change
               ("placement" "offset")
               (render! el)

               ;; Recreate anchor events if manual changes
               ("manual" "disable-close")
               (do
                 (cleanup-anchor-events! el)
                 (setup-anchor-events! el))

               ;; Default: no special handling needed
               nil)))})
