(ns ty.scroll-prevention
  "Shared utility for preventing body scroll when modals/dialogs are open.
   Provides hybrid approach: uses native showModal() benefits while adding
   the missing body scroll prevention that browsers don't implement reliably.")

;; =====================================================
;; Body Scroll Prevention (Hybrid Approach)
;; =====================================================

(defn lock-body-scroll!
  "Prevent body scroll while keeping native showModal() benefits.
   Only handles document-level scrolling - browser handles container inertness.
   
   This is the missing piece that browsers don't implement reliably when
   using showModal(). The native dialog handles:
   - Container inertness (scrollable divs become unscrollable)
   - Focus management and trapping
   - Backdrop clicks and ESC key behavior
   - Accessibility and ARIA attributes
   
   We only add body scroll prevention since that's what's missing."
  []
  (let [body (.-body js/document)
        scroll-y (.-scrollY js/window)]
    ;; Save current scroll position for precise restoration
    (set! (.-tyScrollY body) scroll-y)
    ;; Apply minimal CSS to prevent body scroll only
    (.setProperty (.-style body) "position" "fixed")
    (.setProperty (.-style body) "top" (str "-" scroll-y "px"))
    (.setProperty (.-style body) "width" "100%")
    (.setProperty (.-style body) "overflow" "hidden")))

(defn unlock-body-scroll!
  "Restore body scroll and exact scroll position.
   
   Removes all the CSS properties we applied and restores the user
   to the exact scroll position they were at before the modal opened."
  []
  (let [body (.-body js/document)]
    ;; Remove scroll prevention styles
    (.removeProperty (.-style body) "position")
    (.removeProperty (.-style body) "top")
    (.removeProperty (.-style body) "width")
    (.removeProperty (.-style body) "overflow")
    ;; Restore exact scroll position
    (when-let [scroll-y (.-tyScrollY body)]
      (.scrollTo js/window 0 scroll-y)
      (set! (.-tyScrollY body) nil))))
