(ns ty.context
  "Global context for Ty components.
   Provides dynamic vars that can be bound to customize component behavior.")

(def ^:dynamic *locale*
  "Default locale for date/time formatting.
   Components fall back to 'en-US' if nil."
  nil)
