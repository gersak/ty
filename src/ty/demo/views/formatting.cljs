(ns ty.demo.views.formatting
  "Demonstrates number and date formatting"
  (:require
   [ty.demo.state :refer [state]]
   [ty.i18n :as i18n]
   [ty.i18n.number :as number]
   [ty.i18n.time :as time]))

(defn number-formatting-examples []
  (let [locale (:locale @state i18n/*locale*)
        sample-number 1234567.89
        small-number 0.42
        currency-amount 1999.99]
    [:div.space-y-4
     [:h3.text-lg.font-semibold "Number Formatting"]

     [:div.space-y-2
      [:h4.font-medium "Basic Number"]
      [:div.grid.grid-cols-2.gap-4.text-sm
       [:div
        [:code "(i18n/t " sample-number ")"]
        [:span.ml-2 "→ " (i18n/t sample-number)]]
       [:div
        [:code "(i18n/t " sample-number " :" (name locale) ")"]
        [:span.ml-2 "→ " (i18n/t sample-number locale)]]]]

     [:div.space-y-2
      [:h4.font-medium "Currency"]
      [:div.grid.grid-cols-2.gap-4.text-sm
       [:div
        [:code "(i18n/t " currency-amount " \"USD\")"]
        [:span.ml-2 "→ " (i18n/t currency-amount "USD")]]
       [:div
        [:code "(i18n/t " currency-amount " \"EUR\")"]
        [:span.ml-2 "→ " (i18n/t currency-amount "EUR")]]
       [:div
        [:code "(i18n/t " currency-amount " \"HRK\")"]
        [:span.ml-2 "→ " (i18n/t currency-amount "HRK")]]
       [:div
        [:code "(i18n/t " currency-amount " \"JPY\")"]
        [:span.ml-2 "→ " (i18n/t currency-amount "JPY")]]]]

     [:div.space-y-2
      [:h4.font-medium "Percentages"]
      [:div.grid.grid-cols-2.gap-4.text-sm
       [:div
        [:code "(number/format-percent " small-number ")"]
        [:span.ml-2 "→ " (number/format-percent small-number)]]
       [:div
        [:code "(number/format-percent 0.1234)"]
        [:span.ml-2 "→ " (number/format-percent 0.1234)]]]]

     [:div.space-y-2
      [:h4.font-medium "Compact Notation"]
      [:div.grid.grid-cols-2.gap-4.text-sm
       [:div
        [:code "(number/format-compact 1234)"]
        [:span.ml-2 "→ " (number/format-compact 1234)]]
       [:div
        [:code "(number/format-compact 1234567)"]
        [:span.ml-2 "→ " (number/format-compact 1234567)]]
       [:div
        [:code "(number/format-compact 1234567890)"]
        [:span.ml-2 "→ " (number/format-compact 1234567890)]]]]]))

(defn date-formatting-examples []
  (let [locale (:locale @state i18n/*locale*)
        sample-date (js/Date.)]
    [:div.space-y-4
     [:h3.text-lg.font-semibold "Date/Time Formatting"]

     [:div.space-y-2
      [:h4.font-medium "Date Styles"]
      [:div.space-y-1.text-sm
       [:div
        [:code "(i18n/t sample-date)"]
        [:span.ml-2 "→ " (i18n/t sample-date)]]
       [:div
        [:code "(i18n/t sample-date \"short\")"]
        [:span.ml-2 "→ " (i18n/t sample-date "short")]]
       [:div
        [:code "(i18n/t sample-date \"medium\")"]
        [:span.ml-2 "→ " (i18n/t sample-date "medium")]]
       [:div
        [:code "(i18n/t sample-date \"long\")"]
        [:span.ml-2 "→ " (i18n/t sample-date "long")]]
       [:div
        [:code "(i18n/t sample-date \"full\")"]
        [:span.ml-2 "→ " (i18n/t sample-date "full")]]]]

     [:div.space-y-2
      [:h4.font-medium "Time Formatting"]
      [:div.space-y-1.text-sm
       [:div
        [:code "(i18n/t sample-date \"time\")"]
        [:span.ml-2 "→ " (i18n/t sample-date "time")]]
       [:div
        [:code "(i18n/t sample-date \"datetime\")"]
        [:span.ml-2 "→ " (i18n/t sample-date "datetime")]]]]

     [:div.space-y-2
      [:h4.font-medium "Custom Formatting"]
      [:div.space-y-1.text-sm
       [:div
        [:code "(time/format-date sample-date locale {:year \"numeric\" :month \"long\" :day \"numeric\"})"]
        [:div.ml-2 "→ " (time/format-date sample-date locale {:year "numeric"
                                                              :month "long"
                                                              :day "numeric"})]]
       [:div
        [:code "(time/format-date sample-date locale {:weekday \"long\" :hour \"2-digit\" :minute \"2-digit\"})"]
        [:div.ml-2 "→ " (time/format-date sample-date locale {:weekday "long"
                                                              :hour "2-digit"
                                                              :minute "2-digit"})]]]]]))

(defn relative-time-examples []
  [:div.space-y-4
   [:h3.text-lg.font-semibold "Relative Time"]
   [:div.grid.grid-cols-2.gap-4.text-sm
    [:div
     [:code "(time/format-relative -1 \"day\")"]
     [:span.ml-2 "→ " (time/format-relative -1 "day")]]
    [:div
     [:code "(time/format-relative 2 \"hour\")"]
     [:span.ml-2 "→ " (time/format-relative 2 "hour")]]
    [:div
     [:code "(time/format-relative -3 \"month\")"]
     [:span.ml-2 "→ " (time/format-relative -3 "month")]]
    [:div
     [:code "(time/format-relative 1 \"year\")"]
     [:span.ml-2 "→ " (time/format-relative 1 "year")]]]])

(defn locale-info-examples []
  (let [locale (:locale @state i18n/*locale*)]
    [:div.space-y-4
     [:h3.text-lg.font-semibold "Locale Information"]

     [:div.space-y-2
      [:h4.font-medium "Weekdays"]
      [:div.text-sm
       [:code "(i18n/locale :" (name locale) " :weekdays)"]
       [:div.flex.flex-wrap.gap-2.mt-2
        (for [day (i18n/locale locale :weekdays)]
          [:span.px-2.py-1.ty-content.rounded {:key day} day])]]]

     [:div.space-y-2
      [:h4.font-medium "Months"]
      [:div.text-sm
       [:code "(i18n/locale :" (name locale) " :months)"]
       [:div.flex.flex-wrap.gap-2.mt-2
        (for [month (i18n/locale locale :months)]
          [:span.px-2.py-1.ty-content.rounded {:key month} month])]]]]))

(defn view []
  (let [locale (:locale @state i18n/*locale*)]
    (binding [i18n/*locale* locale]
      [:div.p-8.max-w-6xl.mx-auto.space-y-8.ty-text-
       [:div
        [:h1.text-3xl.font-bold.mb-4 "Number & Date Formatting"]
        [:p
         "Format numbers, currencies, dates, and times using native Intl API."]]

       [:div.ty-elevated.rounded-lg.shadow-md.p-6.text
        [:div.flex.items-center.gap-4.mb-6
         [:span.font-medium "Current locale:"]
         [:select.px-3.py-2.border.rounded-md
          {:value (name locale)
           :on {:change (fn [e]
                          (swap! state assoc :locale (keyword (.. e -target -value))))}}
          [:option {:value "en"} "English"]
          [:option {:value "hr"} "Hrvatski"]
          [:option {:value "de"} "Deutsch"]
          [:option {:value "fr"} "Français"]
          [:option {:value "es"} "Español"]
          [:option {:value "ja"} "日本語"]]]]

       [:div.ty-elevated.rounded-lg.shadow-md.p-6
        (number-formatting-examples)]

       [:div.ty-elevated.rounded-lg.shadow-md.p-6
        (date-formatting-examples)]

       [:div.ty-elevated.rounded-lg.shadow-md.p-6
        (relative-time-examples)]

       [:div.ty-elevated.rounded-lg.shadow-md.p-6
        (locale-info-examples)]])))
