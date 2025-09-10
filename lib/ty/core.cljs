(ns ty.core
  (:require
   [ty.components.core]
   [ty.lazy.calendar]
   [ty.lazy.calendar-month]
   [ty.lazy.date-picker]
   [ty.lazy.dropdown]
   [ty.lazy.multiselect]
   [ty.scroll-lock]))

(defn init []
  (.log js/console "[TY Components] Initialized!")

  ;; Expose scroll lock debug functions globally for testing
  (set! js/window.tyScrollLockDebug
        #js {:enable #(ty.scroll-lock/enable-debug!)
             :disable #(ty.scroll-lock/disable-debug!)
             :status #(clj->js {:locked? (ty.scroll-lock/locked?)
                                :active-locks (ty.scroll-lock/get-active-locks)})})

  (.log js/console "[TY ScrollLock] Debug functions available: window.tyScrollLockDebug"))
