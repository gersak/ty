(ns ty.util.outside-click
  "Shared utilities for handling outside click detection.
   
   Provides consistent outside click behavior for components like
   dropdown, modal, popup, etc.")

;; =====================================================
;; Outside Click Detection
;; =====================================================

(defn find-custom-element
  "Find the custom element that contains the given element"
  [^js element]
  (when element
    (loop [current element]
      (cond
        (not current) nil
        (and (.-tagName current) (.includes (.-tagName current) "-")) current
        :else (recur (.-parentElement current))))))

(defn is-inside-shadow-root?
  "Check if target element is inside the shadow root of the given element"
  [^js target ^js element]
  (when (and target element)
    (when-let [shadow-root (.-shadowRoot element)]
      (.contains shadow-root target))))

(defn get-touch-target
  "Get the target element from a touch event"
  [^js e]
  (if (.-touches e)
    ;; Touch event - get target from first touch
    (when-let [first-touch (aget (.-touches e) 0)]
      (.-target first-touch))
    ;; Mouse event - get target directly
    (.-target e)))

(defn should-handle-event?
  "Determine if we should handle this outside click event"
  [^js element ^js target content-selector ignore-selectors]
  (cond
    ;; Click is inside content area - ignore
    (and content-selector
         (when-let [content (.querySelector element content-selector)]
           (.contains content target)))
    false

    ;; Click is on ignored elements - ignore
    (and ignore-selectors
         (some (fn [selector]
                 (when-let [ignored (.querySelector js/document selector)]
                   (.contains ignored target)))
               ignore-selectors))
    false

    ;; Click is on an option element (for dropdowns) - ignore
    (= (.-tagName target) "OPTION")
    false

    ;; Click is inside the shadow root of the custom element - ignore
    (when-let [custom-element (find-custom-element element)]
      (is-inside-shadow-root? target custom-element))
    false

    ;; Click is on the main element itself (backdrop) - handle
    (= target element)
    true

    ;; Click is outside the element - handle
    (not (.contains element target))
    true

    ;; Default - handle
    :else true))

(defn setup-outside-click!
  "Setup outside click detection for an element.
   Enhanced to work with both mouse and touch events.
   
   Args:
   - element: The target element (e.g., dialog, popup container) 
   - handler: Function called when outside click detected
   - options: Optional map with:
     - :content-selector - CSS selector for content area (clicks inside ignored)
     - :ignore-selectors - Vector of CSS selectors to ignore clicks on
     - :enabled? - Whether detection is enabled (default true)
     - :touch-enabled? - Enable touch event handling (default true)
     - :prevent-default? - Prevent default behavior on outside clicks (default false for mobile)
   
   Returns cleanup function to remove listeners."
  [^js element handler & [options]]
  (let [{:keys [content-selector ignore-selectors enabled? touch-enabled? prevent-default?]
         :or {enabled? true
              touch-enabled? true
              prevent-default? false}} options] ; Default false for mobile compatibility

    (when enabled?
      (let [mouse-listener (fn [e]
                             (let [target (.-target e)]
                               (when (and target (should-handle-event? element target content-selector ignore-selectors))
                                 ;; For mouse events, we can safely prevent default
                                 (when prevent-default?
                                   (.preventDefault e)
                                   (.stopPropagation e))
                                 (handler e))))

            touch-listener (fn [e]
                             (let [target (or (get-touch-target e) (.-target e))]
                               (when (and target (should-handle-event? element target content-selector ignore-selectors))
                                 ;; For touch events, be very careful about preventing default
                                 ;; Only prevent if explicitly requested and not a scroll/swipe gesture
                                 (when (and prevent-default?
                                            (not= (.-type e) "touchmove")
                                            (< (count (.-touches e)) 2)) ; Not a multi-touch gesture
                                   (.preventDefault e))
                                 ;; Don't stop propagation on touch events to maintain normal touch handling
                                 (handler e))))]

        ;; Mouse events
        (.addEventListener js/document "mousedown" mouse-listener)

        ;; Touch events (if enabled) - use passive listeners by default for better performance
        (when touch-enabled?
          (.addEventListener js/document "touchstart" touch-listener #js {:passive (not prevent-default?)})
          ;; Don't use touchend as it can interfere with scrolling and other gestures
          )

        ;; Return cleanup function
        (fn []
          (.removeEventListener js/document "mousedown" mouse-listener)
          (when touch-enabled?
            (.removeEventListener js/document "touchstart" touch-listener)))))))

(defn setup-escape-key!
  "Setup escape key detection for an element.
   
   Args:
   - element: The target element to listen for escape key
   - handler: Function called when escape key pressed
   - enabled?: Whether detection is enabled (default true)
   
   Returns cleanup function to remove listener."
  [^js element handler & [enabled?]]
  (let [enabled? (if (nil? enabled?) true enabled?)]
    (when enabled?
      (let [listener (fn [e]
                       (when (= (.-key e) "Escape")
                         (.preventDefault e)
                         (.stopPropagation e)
                         (handler e)))]

        (.addEventListener element "keydown" listener)

        ;; Return cleanup function
        (fn []
          (.removeEventListener element "keydown" listener))))))

(defn setup-mobile-outside-click!
  "Mobile-optimized version with enhanced touch handling.
   Specifically designed to not interfere with normal touch interactions."
  [^js element handler & [options]]
  (let [{:keys [content-selector ignore-selectors enabled?]
         :or {enabled? true}} options]

    (when enabled?
      (let [touch-start-time (atom nil)
            touch-start-pos (atom nil)

            touch-start-listener (fn [e]
                                   (reset! touch-start-time (js/Date.now))
                                   (when-let [touch (aget (.-touches e) 0)]
                                     (reset! touch-start-pos {:x (.-clientX touch)
                                                              :y (.-clientY touch)})))

            touch-end-listener (fn [e]
                                 (let [target (.-target e)
                                       duration (- (js/Date.now) @touch-start-time)
                                       is-tap (< duration 300)] ; Quick tap, not a long press or scroll

                                   (when (and target
                                              is-tap
                                              (should-handle-event? element target content-selector ignore-selectors))
                                     ;; This is a quick tap outside - handle it
                                     ;; Don't prevent default on touchend to maintain normal behavior
                                     (handler e))))]

        ;; Use passive listeners for better performance and compatibility
        (.addEventListener js/document "touchstart" touch-start-listener #js {:passive true})
        (.addEventListener js/document "touchend" touch-end-listener #js {:passive true})

        ;; Return cleanup function
        (fn []
          (.removeEventListener js/document "touchstart" touch-start-listener)
          (.removeEventListener js/document "touchend" touch-end-listener))))))

;; =====================================================
;; Combined Setup Utilities  
;; =====================================================

(defn setup-outside-handlers!
  "Setup both outside click and escape key handling.
   Enhanced with mobile support options.
   
   Args:
   - element: The target element
   - handler: Function called for both outside click and escape key
   - options: Optional map with:
     - :content-selector - CSS selector for content area
     - :ignore-selectors - Vector of CSS selectors to ignore
     - :outside-click? - Enable outside click detection (default true)
     - :escape-key? - Enable escape key detection (default true)
     - :mobile-optimized? - Use mobile-optimized touch handling (default false)
     - :touch-enabled? - Enable touch events (default true)
     - :prevent-default? - Prevent default on outside clicks (default false)
   
   Returns cleanup function to remove all listeners."
  [^js element handler & [options]]
  (let [{:keys [outside-click? escape-key? mobile-optimized?]
         :or {outside-click? true
              escape-key? true
              mobile-optimized? false}} options

        outside-cleanup (when outside-click?
                          (if mobile-optimized?
                            (setup-mobile-outside-click! element handler options)
                            (setup-outside-click! element handler options)))
        escape-cleanup (when escape-key?
                         (setup-escape-key! element handler escape-key?))]

    ;; Return combined cleanup function
    (fn []
      (when outside-cleanup (outside-cleanup))
      (when escape-cleanup (escape-cleanup)))))

;; =====================================================
;; Global Dropdown Management (from dropdown.cljs)
;; =====================================================

(defonce ^{:doc "Global registry for currently open dropdown/popup components.
                 Simple atom approach - only one can be open at a time."}
  current-open-component (atom nil))

(defn close-current-component!
  "Close any currently open component."
  []
  (when-let [{:keys [element close-fn]} @current-open-component]
    (when (and element close-fn)
      (close-fn element))
    (reset! current-open-component nil)))

(defn set-current-component!
  "Set the currently open component. Automatically closes any previous one.
   
   Args:
   - element: The component element
   - close-fn: Function to close this component"
  [element close-fn]
  (close-current-component!) ; Close any existing
  (reset! current-open-component {:element element
                                  :close-fn close-fn}))

(defn is-current-component?
  "Check if the given element is the currently open component."
  [element]
  (= element (:element @current-open-component)))
