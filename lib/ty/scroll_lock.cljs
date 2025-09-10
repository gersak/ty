(ns ty.scroll-lock
  "Selective scroll prevention system.
   
   Prevents body/page scrolling while allowing scrolling within designated containers
   like dropdown menus, modals, and other components.
   
   Key benefits:
   - Allows scrolling within dropdown options, modal content, etc.
   - Prevents body/page scrolling to avoid layout shifts
   - No viewport width changes (no scrollbar show/hide)
   - No CSS style manipulation whatsoever
   - Multiple component support with proper cleanup
   - Selective event prevention based on scroll target")

;; =====================================================
;; Global State Management
;; =====================================================

(defonce ^:private active-locks (atom #{}))
(defonce ^:private event-handlers (atom nil))

;; =====================================================
;; Event Handlers
;; =====================================================

(defn- is-body-scroll?
  "Check if the scroll event is targeting the body, html, or document specifically.
   Only block these - allow everything else including null targets."
  [^js element]
  (or (= element js/document)
      (= element (.-documentElement js/document))
      (= element (.-body js/document))))

(defn- is-ty-dropdown-scroll?
  [^js element]
  (let [tag (.-tagName element)
        tags #{"TY-DROPDOWN"
               "TY-DATE-PICKER"
               "TY-MULTISELECT"}]
    (contains? tags tag)))

(defn- prevent-scroll-event
  "Only prevent scroll events that target the body, html, or document.
   Allow all other elements to scroll freely (dropdowns, modals, etc.)"
  [^js event]
  (let [target (.-target event)
        is-body? (is-body-scroll? target)
        is-ty-dropdown? (is-ty-dropdown-scroll? target)]

    ;; Debug logging if enabled
    (when js/window.tyScrollDebug
      (js/console.log "📜 Scroll event:"
                      (clj->js {:type (.-type event)
                                :target (.-tagName target)
                                :target-id (.-id target)
                                :class (when (.-className target) (.-className target))
                                :is-body? is-body?
                                :will-prevent? is-body?})))

    ;; Only prevent if targeting body/html/document
    (when (or is-body? is-ty-dropdown?)
      (.preventDefault event)
      (.stopPropagation event))))

(defn- prevent-scroll-keys
  "Only prevent keyboard scroll events that target the body, html, or document.
   Allow keyboard scrolling within other elements (dropdowns, modals, etc.)"
  [^js event]
  (let [scroll-keys #{32 ; space
                      33 ; page up
                      34 ; page down
                      35 ; end
                      36 ; home
                      37 ; left arrow
                      38 ; up arrow
                      39 ; right arrow
                      40 ; down arrow
                      }
        target (.-target event)
        is-body? (is-body-scroll? target)
        will-prevent? (and (contains? scroll-keys (.-keyCode event))
                           is-body?)]

    ;; Debug logging if enabled
    (when (and js/window.tyScrollDebug will-prevent?)
      (js/console.log "⌨️  Scroll key event:"
                      (clj->js {:key-code (.-keyCode event)
                                :target (.-tagName target)
                                :class (when (.-className target) (.-className target))
                                :is-body? is-body?
                                :will-prevent? will-prevent?})))

    (when will-prevent?
      (.preventDefault event)
      (.stopPropagation event))))

;; =====================================================
;; Core Lock/Unlock Functions
;; =====================================================

(defn- install-event-listeners!
  "Install global event listeners for scroll prevention"
  []
  (when (nil? @event-handlers)
    (let [handlers {:wheel prevent-scroll-event
                    :touchmove prevent-scroll-event
                    :scroll prevent-scroll-event
                    :keydown prevent-scroll-keys}]

      ;; Install with capture=true and passive=false for maximum control
      (.addEventListener js/window "wheel" (:wheel handlers)
                         #js {:passive false
                              :capture true})
      (.addEventListener js/window "touchmove" (:touchmove handlers)
                         #js {:passive false
                              :capture true})
      (.addEventListener js/window "scroll" (:scroll handlers)
                         #js {:passive false
                              :capture true})
      (.addEventListener js/document "keydown" (:keydown handlers)
                         #js {:passive false
                              :capture true})

      (reset! event-handlers handlers))))

(defn- remove-event-listeners!
  "Remove global event listeners when no locks remain"
  []
  (when-let [handlers @event-handlers]
    (.removeEventListener js/window "wheel" (:wheel handlers)
                          #js {:capture true})
    (.removeEventListener js/window "touchmove" (:touchmove handlers)
                          #js {:capture true})
    (.removeEventListener js/window "scroll" (:scroll handlers)
                          #js {:capture true})
    (.removeEventListener js/document "keydown" (:keydown handlers)
                          #js {:capture true})

    (reset! event-handlers nil)))

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
    (when-not (contains? @active-locks id)
      ;; Add this component to active locks
      (swap! active-locks conj id)

      ;; Install event listeners if this is the first lock
      (when (= 1 (count @active-locks))
        (install-event-listeners!)))))

(defn unlock-scroll!
  "Unlock scrolling for a specific component.
   
   Args:
     component-id - Unique identifier for the component (string or keyword)
   
   Scroll remains locked if other components still have locks active."
  [component-id]
  (let [id (str component-id)]
    (when (contains? @active-locks id)
      ;; Remove this component from active locks
      (swap! active-locks disj id)

      ;; Remove event listeners if this was the last lock
      (when (empty? @active-locks)
        (remove-event-listeners!)))))

(defn force-unlock-all!
  "Emergency unlock - removes all scroll locks immediately.
   
   Use this for cleanup or error recovery scenarios."
  []
  (reset! active-locks #{})
  (remove-event-listeners!))

;; =====================================================
;; Debug API (exposed globally for testing)
;; =====================================================

(defn enable-debug!
  "Enable scroll event debugging - useful for testing and development"
  []
  (debug-scroll-event true))

(defn disable-debug!
  "Disable scroll event debugging"
  []
  (debug-scroll-event false))

;; =====================================================
;; Debugging & State Inspection
;; =====================================================

;; =====================================================
;; Debugging & State Inspection
;; =====================================================

(defn debug-scroll-event
  "Debug helper to understand scroll events and their targets.
   
   Call this from browser console to enable scroll event debugging:
   ty.scroll_lock.debug_scroll_event(true)"
  [enable?]
  (if enable?
    (do
      (js/console.log "🔍 Scroll lock debugging enabled")
      (set! js/window.tyScrollDebug true))
    (do
      (js/console.log "🔍 Scroll lock debugging disabled")
      (set! js/window.tyScrollDebug false))))

(defn get-active-locks
  "Get the set of currently active component locks.
   
   Returns a set of component IDs that currently have scroll locked."
  []
  @active-locks)

(defn locked?
  "Check if scrolling is currently locked.
   
   Returns true if any component has scroll locked."
  []
  (not (empty? @active-locks)))

(defn locked-by?
  "Check if scrolling is locked by a specific component.
   
   Args:
     component-id - Component identifier to check
   
   Returns true if this specific component has scroll locked."
  [component-id]
  (contains? @active-locks (str component-id)))
