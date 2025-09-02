(ns ty.i18n.number
  "Number and currency formatting using native Intl.NumberFormat.
  Extends the Translator protocol for numbers."
  (:require
    [cljs-bean.core :refer [->js]]
    [ty.i18n :as i18n]))

(defn locale->string
  "Convert locale keyword to string format for Intl API.
  :en_US -> 'en-US'"
  [locale]
  (-> (name locale)
      (.replace "_" "-")))

(defn format-number
  "Format a number using Intl.NumberFormat.
  Options can include:
  - :style - 'decimal' (default), 'currency', 'percent', 'unit'
  - :currency - currency code when style is 'currency'
  - :minimumFractionDigits - minimum decimal places
  - :maximumFractionDigits - maximum decimal places
  - :useGrouping - whether to use grouping separators"
  ([number] (format-number number i18n/*locale*))
  ([number locale] (format-number number locale {}))
  ([number locale options]
   (let [formatter (js/Intl.NumberFormat.
                     (locale->string locale)
                     (->js options))]
     (.format formatter number))))

(defn format-currency
  "Format a number as currency.
  Currency should be a 3-letter ISO code like 'USD', 'EUR', 'HRK'"
  ([number currency] (format-currency number currency i18n/*locale*))
  ([number currency locale]
   (format-number number locale {:style "currency"
                                 :currency currency})))

(defn format-percent
  "Format a number as percentage"
  ([number] (format-percent number i18n/*locale*))
  ([number locale]
   (format-number number locale {:style "percent"})))

(defn format-compact
  "Format a number in compact notation (1.2K, 3.4M, etc)"
  ([number] (format-compact number i18n/*locale*))
  ([number locale]
   (format-number number locale {:notation "compact"
                                 :compactDisplay "short"})))

;; Memoized formatters for performance
(def formatters (atom {}))

(defn get-formatter
  "Get or create a memoized formatter"
  [locale options]
  (let [key [locale options]]
    (or (get @formatters key)
        (let [formatter (js/Intl.NumberFormat.
                          (locale->string locale)
                          (->js options))]
          (swap! formatters assoc key formatter)
          formatter))))

;; Extend the Translator protocol for numbers
(extend-protocol i18n/Translator
  number
  (translate
    ([this]
     ;; Default number formatting for current locale
     (format-number this i18n/*locale*))
    ([this locale-or-currency]
     ;; If string, assume it's currency code
     ;; If keyword, assume it's locale
     (cond
       (string? locale-or-currency)
       (format-currency this locale-or-currency i18n/*locale*)

       (keyword? locale-or-currency)
       (format-number this locale-or-currency)

       :else
       (str this)))
    ([this locale options]
     ;; Full control with locale and options
     (format-number this locale options))))

;; Currency detection helper
(defn currency?
  "Check if string is a valid currency code"
  [s]
  (and (string? s)
       (= 3 (count s))
       (= s (.toUpperCase s))))

;; Locale-specific number parsing (reverse of formatting)
(defn parse-number
  "Parse a localized number string back to number.
  Note: This is a simple implementation and may not handle all cases."
  ([s] (parse-number s i18n/*locale*))
  ([s locale]
   (let [formatter (js/Intl.NumberFormat. (locale->string locale))
         ;; Get the locale's decimal and group separators
         parts (.formatToParts formatter 11111.11)
         decimal-sep (some #(when (= (.-type %) "decimal") (.-value %)) parts)
         group-sep (some #(when (= (.-type %) "group") (.-value %)) parts)]
     ;; Remove group separators and normalize decimal separator
     (-> s
         (clojure.string/replace group-sep "")
         (clojure.string/replace decimal-sep ".")
         (js/parseFloat)))))
