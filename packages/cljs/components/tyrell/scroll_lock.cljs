(ns ty.scroll-lock
  "CSS-based scroll prevention using position: fixed.
   
   Prevents body/page scrolling by temporarily fixing the body position
   while preserving scroll position. Much more reliable than event prevention.
   
   Key benefits:
   - No event manipulation complexity
   - No interference with component keyboard navigation  
   - Preserves scroll position perfectly
   - Standard industry technique
   - Multiple component support with proper cleanup")

;; =====================================================
;; Global State Management
;; =====================================================

(defonce ^:private lock-state (atom {:locked? false
                                     :scroll-y 0
                                     :active-locks #{}}))

;; =====================================================
;; DOM Helper Functions
;; =====================================================

(defn- body-el
  "Get the document body element"
  []
  (.-body js/document))

(defn- doc-el
  "Get the document element (html)"
  []
  (.-documentElement js/document))

(defn- get-scroll-y
  "Get current vertical scroll position"
  []
  (or (.-scrollY js/window)
      (.-scrollTop (doc-el))
      0))

(defn- scrollbar-width
  "Calculate the width of the browser's scrollbar"
  []
  ;; window.innerWidth - documentElement.clientWidth
  (- (.-innerWidth js/window)
     (.-clientWidth (doc-el))))

;; =====================================================
;; Core Lock/Unlock Functions
;; =====================================================

(defn- lock-body-fixed!
  "Lock body scrolling using position: fixed technique"
  []
  (when-not (:locked? @lock-state)
    (let [y (get-scroll-y)
          scrollbar-w (scrollbar-width)
          body (body-el)]
      ;; Debug logging if enabled
      (when js/window.tyScrollDebug
        (js/console.log "🔒 Locking body scroll:"
                        (clj->js {:scroll-y y
                                  :scrollbar-width scrollbar-w})))

      ;; Store current state
      (swap! lock-state assoc :scroll-y y :locked? true)

      ;; Apply fixed positioning
      (set! (.. body -style -position) "fixed")
      (set! (.. body -style -top) (str "-" y "px"))
      (set! (.. body -style -left) "0")
      (set! (.. body -style -right) "0")

      ;; Prevent layout shift by adding padding to compensate for scrollbar
      (when (> scrollbar-w 0)
        (set! (.. body -style -paddingRight) (str scrollbar-w "px")))

      ;; Add CSS class for styling hooks
      (.add (.-classList body) "ty-scroll-locked"))))

(defn- unlock-body-fixed!
  "Unlock body scrolling and restore scroll position"
  []
  (when (:locked? @lock-state)
    (let [y (:scroll-y @lock-state)
          body (body-el)]
      ;; Debug logging if enabled
      (when js/window.tyScrollDebug
        (js/console.log "🔓 Unlocking body scroll:"
                        (clj->js {:restoring-scroll-y y})))

      ;; Remove CSS class
      (.remove (.-classList body) "ty-scroll-locked")

      ;; Remove fixed positioning and padding
      (set! (.. body -style -position) "")
      (set! (.. body -style -top) "")
      (set! (.. body -style -left) "")
      (set! (.. body -style -right) "")
      (set! (.. body -style -width) "")
      (set! (.. body -style -paddingRight) "")

      ;; Restore scroll position
      (.scrollTo js/window 0 y)

      ;; Update state
      (swap! lock-state assoc :locked? false))))

;; =====================================================
;; Public API
;; =====================================================

(defn lock-scroll!
  "Lock scrolling for a specific component.
   
   Args:
     component-id - Unique identifier for the component (string or keyword)
   
   Multiple components can lock scrolling simultaneously.
   Scroll remains locked until ALL components unlock."
  [component-id]
  (let [id (str component-id)]
    (when-not (contains? (:active-locks @lock-state) id)
      ;; Add this component to active locks
      (swap! lock-state update :active-locks conj id)

      ;; Lock body if this is the first lock
      (when (= 1 (count (:active-locks @lock-state)))
        (lock-body-fixed!)))))

(defn unlock-scroll!
  "Unlock scrolling for a specific component.
   
   Args:
     component-id - Unique identifier for the component (string or keyword)
   
   Scroll remains locked if other components still have locks active."
  [component-id]
  (let [id (str component-id)]
    (when (contains? (:active-locks @lock-state) id)
      ;; Remove this component from active locks
      (swap! lock-state update :active-locks disj id)

      ;; Unlock body if this was the last lock
      (when (empty? (:active-locks @lock-state))
        (unlock-body-fixed!)))))

(defn force-unlock-all!
  "Emergency unlock - removes all scroll locks immediately.
   
   Use this for cleanup or error recovery scenarios."
  []
  (swap! lock-state assoc :active-locks #{})
  (unlock-body-fixed!))

;; =====================================================
;; Debug API (exposed globally for testing)
;; =====================================================

(defn enable-debug!
  "Enable scroll lock debugging - useful for testing and development"
  []
  (js/console.log "🔍 CSS Scroll lock debugging enabled")
  (set! js/window.tyScrollDebug true))

(defn disable-debug!
  "Disable scroll lock debugging"
  []
  (js/console.log "🔍 CSS Scroll lock debugging disabled")
  (set! js/window.tyScrollDebug false))

;; =====================================================
;; State Inspection
;; =====================================================

(defn get-active-locks
  "Get the set of currently active component locks.
   
   Returns a set of component IDs that currently have scroll locked."
  []
  (:active-locks @lock-state))

(defn locked?
  "Check if scrolling is currently locked.
   
   Returns true if any component has scroll locked."
  []
  (:locked? @lock-state))

(defn locked-by?
  "Check if scrolling is locked by a specific component.
   
   Args:
     component-id - Component identifier to check
   
   Returns true if this specific component has scroll locked."
  [component-id]
  (contains? (:active-locks @lock-state) (str component-id)))

(defn get-lock-state
  "Get the complete lock state for debugging.
   
   Returns current state including scroll position and active locks."
  []
  @lock-state)
