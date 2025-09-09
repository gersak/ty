(ns ty.core
  (:require
    [ty.components.core]
    [ty.lazy.calendar]
    [ty.lazy.calendar-month]
    [ty.lazy.date-picker]
    [ty.lazy.dropdown]
    [ty.lazy.multiselect]
    [ty.scroll-prevention]))

(defn init []
  (.log js/console "[TY Components] Initialized!"))

;; TODO - remove this
;; (ty.i18n.string/load-datepicker-translations!)
