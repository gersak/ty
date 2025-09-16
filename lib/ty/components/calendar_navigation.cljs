(ns ty.components.calendar-navigation
  "Pure stateless calendar navigation component - controlled through JS properties"
  (:require [ty.context :as context]
            [ty.css :refer [ensure-styles!]]
            [ty.i18n :refer [translate]]
            [ty.i18n.time :as time]
            [ty.shim :as wcs])
  (:require-macros [ty.css :refer [defstyles]]))

;; SVG icons for navigation buttons
;; (def chevron-left-svg
;;   "<svg stroke='currentColor' fill='none' stroke-linejoin='round' width='16' xmlns='http://www.w3.org/2000/svg' stroke-linecap='round' stroke-width='2' viewBox='0 0 24 24' height='16'><path d='m15 18-6-6 6-6'/></svg>")
;; 
;; (def chevron-right-svg
;;   "<svg stroke='currentColor' fill='none' stroke-linejoin='round' width='16' xmlns='http://www.w3.org/2000/svg' stroke-linecap='round' stroke-width='2' viewBox='0 0 24 24' height='16'><path d='m9 18 6-6-6-6'/></svg>")
;; 
;; (def chevrons-left-svg
;;   "<svg stroke='currentColor' fill='none' stroke-linejoin='round' width='16' xmlns='http://www.w3.org/2000/svg' stroke-linecap='round' stroke-width='2' viewBox='0 0 24 24' height='16'><path d='m11 17-5-5 5-5'/><path d='m18 17-5-5 5-5'/></svg>")
;; 
;; (def chevrons-right-svg
;;   "<svg stroke='currentColor' fill='none' stroke-linejoin='round' width='16' xmlns='http://www.w3.org/2000/svg' stroke-linecap='round' stroke-width='2' viewBox='0 0 24 24' height='16'><path d='m6 17 5-5-5-5'/><path d='m13 17 5-5-5-5'/></svg>")

;; (def chevron-left-svg
;;     "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"none\" viewBox=\"0 0 24 24\" stroke-width=\"1.5\" stroke=\"currentColor\" class=\" size-6 \">
;;   <path stroke-linecap=\"round\" stroke-linejoin=\"round\" d=\"M15.75 19.5 8.25 12l7.5-7.5\" />
;; </svg>")
;; 
;; (def chevrons-left-svg
;;     "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"none\" viewBox=\"0 0 24 24\" stroke-width=\"1.5\" stroke=\"currentColor\" class=\"size-6\">
;;   <path stroke-linecap=\"round\" stroke-linejoin=\"round\" d=\"m18.75 4.5-7.5 7.5 7.5 7.5m-6-15L5.25 12l7.5 7.5\" />
;; </svg>")
;; 
;; (def chevron-right-svg
;;   "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"none\" viewBox=\"0 0 24 24\" stroke-width=\"1.5\" stroke=\"currentColor\" class=\"size-6\">
;;   <path stroke-linecap=\"round\" stroke-linejoin=\"round\" d=\"m8.25 4.5 7.5 7.5-7.5 7.5\"/>
;; </svg>")
;; 
;; (def chevrons-right-svg
;;   "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"none\" viewBox=\"0 0 24 24\" stroke-width=\"1.5\" stroke=\"currentColor\" class=\"size-6\">
;;   <path stroke-linecap=\"round\" stroke-linejoin=\"round\" d=\"m5.25 4.5 7.5 7.5-7.5 7.5m6-15 7.5 7.5-7.5 7.5\" />
;; </svg>")

(def chevron-left-svg
  "<?xml version='1.0' encoding='UTF-8'?>\n<svg width='24' viewBox='0 0 24 24' height='24' xmlns='http://www.w3.org/2000/svg' stroke-width='0' stroke='currentColor' fill='currentColor'>\n<path fill='none' d='M0 0h24v24H0V0z'/>\n<path d='M15.41 16.59L10.83 12l4.58-4.59L14 6l-6 6 6 6 1.41-1.41z'/>\n</svg>\n")

(def chevrons-left-svg
  "<?xml version='1.0' encoding='UTF-8'?>\n<svg width='24' viewBox='0 0 24 24' height='24' enable-background='new 0 0 24 24' xmlns='http://www.w3.org/2000/svg' stroke-width='0' stroke='currentColor' fill='currentColor'>\n<g>\n<rect width='24' height='24' fill='none'/>\n</g>\n<g>\n<g>\n<polygon points='17.59,18 19,16.59 14.42,12 19,7.41 17.59,6 11.59,12'/>\n<polygon points='11,18 12.41,16.59 7.83,12 12.41,7.41 11,6 5,12'/>\n</g>\n</g>\n</svg>\n")

(def chevron-right-svg
  "<?xml version='1.0' encoding='UTF-8'?>\n<svg width='24' viewBox='0 0 24 24' height='24' xmlns='http://www.w3.org/2000/svg' stroke-width='0' stroke='currentColor' fill='currentColor'>\n<path fill='none' d='M0 0h24v24H0V0z'/>\n<path d='M8.59 16.59L13.17 12 8.59 7.41 10 6l6 6-6 6-1.41-1.41z'/>\n</svg>\n")

(def chevrons-right-svg
  "<?xml version='1.0' encoding='UTF-8'?>\n<svg width='24' viewBox='0 0 24 24' height='24' enable-background='new 0 0 24 24' xmlns='http://www.w3.org/2000/svg' stroke-width='0' stroke='currentColor' fill='currentColor'>\n<g>\n<rect width='24' height='24' fill='none'/>\n</g>\n<g>\n<g>\n<polygon points='6.41,6 5,7.41 9.58,12 5,16.59 6.41,18 12.41,12'/>\n<polygon points='13,6 11.59,7.41 16.17,12 11.59,16.59 13,18 19,12'/>\n</g>\n</g>\n</svg>\n")


;; Load calendar navigation styles (reuse existing CSS)
(defstyles calendar-navigation-styles)

;; Use get-month-names from ty.i18n.time - it has proper locale normalization

(defn emit-change-event!
  "Emit change event with current month/year values"
  [^js el month year]
  (let [event-detail #js {:month month
                          :year year}
        event (js/CustomEvent. "change"
                               #js {:detail event-detail
                                    :bubbles true
                                    :cancelable true})]
    (.dispatchEvent el event)))

(defn navigate-month!
  "Navigate to relative month and emit change event"
  [^js el direction]
  (let [current-month (or (.-displayMonth el) 1)
        current-year (or (.-displayYear el) (.getFullYear (js/Date.)))
        ;; Calculate new month/year considering year boundaries
        raw-month (+ current-month direction)
        [new-month new-year] (cond
                               (< raw-month 1) [(+ raw-month 12) (dec current-year)]
                               (> raw-month 12) [(- raw-month 12) (inc current-year)]
                               :else [raw-month current-year])]
    (emit-change-event! el new-month new-year)))

(defn navigate-year!
  "Navigate to relative year and emit change event"
  [^js el direction]
  (let [current-month (or (.-displayMonth el) 1)
        current-year (or (.-displayYear el) (.getFullYear (js/Date.)))
        new-year (+ current-year direction)]
    (emit-change-event! el current-month new-year)))

(defn render!
  "Render the navigation header using JS properties"
  [^js el]
  (let [root (wcs/ensure-shadow el)
        ;; Read from JS properties (not attributes)
        now (js/Date.)
        display-month (or (.-displayMonth el) (.getMonth now))
        display-year (or (.-displayYear el) (.getFullYear now))
        locale (or (.-locale el) context/*locale* "en-US")
        width (.-width el)

        ;; Get localized month names
        month-names (time/get-month-names locale)
        current-month-name (nth month-names (dec display-month))]

    ;; Load styles
    (ensure-styles! root calendar-navigation-styles "ty-calendar-navigation")

    ;; Clear and rebuild
    (set! (.-innerHTML root) "")

    ;; Create main header
    (let [header (.createElement js/document "div")
          left-group (.createElement js/document "div")
          center-group (.createElement js/document "div")
          right-group (.createElement js/document "div")]

      ;; Set up container classes (reuse existing calendar navigation CSS)
      (set! (.-className header) "calendar-navigation-header")
      (set! (.-className left-group) "nav-group nav-group-left")
      (set! (.-className center-group) "nav-group nav-group-center")
      (set! (.-className right-group) "nav-group nav-group-right")

      ;; Set width if provided
      (when width
        (set! (.-style header) (str "width: " width ";")))

      ;; Previous year button (double chevron left)
      (let [prev-year-btn (.createElement js/document "button")]
        (set! (.-className prev-year-btn) "nav-btn nav-year-prev")
        (set! (.-title prev-year-btn) (translate "Previous year"))
        (set! (.-innerHTML prev-year-btn) chevrons-left-svg)
        (.addEventListener prev-year-btn "click" #(navigate-year! el -1))
        (.appendChild left-group prev-year-btn))

      ;; Previous month button (single chevron left)
      (let [prev-month-btn (.createElement js/document "button")]
        (set! (.-className prev-month-btn) "nav-btn nav-month-prev")
        (set! (.-title prev-month-btn) (translate "Previous month"))
        (set! (.-innerHTML prev-month-btn) chevron-left-svg)
        (.addEventListener prev-month-btn "click" #(navigate-month! el -1))
        (.appendChild left-group prev-month-btn))

      ;; Month and year display (center)
      (let [month-year-display (.createElement js/document "div")]
        (set! (.-className month-year-display) "month-year-display")
        (set! (.-textContent month-year-display)
              (str current-month-name " " display-year))
        (.appendChild center-group month-year-display))

      ;; Next month button (single chevron right)
      (let [next-month-btn (.createElement js/document "button")]
        (set! (.-className next-month-btn) "nav-btn nav-month-next")
        (set! (.-title next-month-btn) (translate "Next month"))
        (set! (.-innerHTML next-month-btn) chevron-right-svg)
        (.addEventListener next-month-btn "click" #(navigate-month! el 1))
        (.appendChild right-group next-month-btn))

      ;; Next year button (double chevron right)
      (let [next-year-btn (.createElement js/document "button")]
        (set! (.-className next-year-btn) "nav-btn nav-year-next")
        (set! (.-title next-year-btn) (translate "Next year"))
        (set! (.-innerHTML next-year-btn) chevrons-right-svg)
        (.addEventListener next-year-btn "click" #(navigate-year! el 1))
        (.appendChild right-group next-year-btn))

      ;; Assemble the header with 3 groups
      (.appendChild header left-group)
      (.appendChild header center-group)
      (.appendChild header right-group)

      (.appendChild root header))))

;; Component registration - property-controlled component
(def configuration
  {:observed [] ; No attribute watching - pure property control
   :props {:displayMonth nil ; Declare properties to install setters/getters
           :displayYear nil ; Properties automatically trigger re-render via :prop hook
           :locale nil
           :width nil}
   :connected (fn [^js el]
                ;; Set default properties if not set
                (when-not (.-displayMonth el)
                  (set! (.-displayMonth el) (inc (.getMonth (js/Date.)))))
                (when-not (.-displayYear el)
                  (set! (.-displayYear el) (.getFullYear (js/Date.))))
                (when-not (.-locale el)
                  (set! (.-locale el) "en-US"))
                (render! el))
   :disconnected (fn [^js el])
   :prop (fn [^js el prop-name old-value new-value]
           ;; Property changed - re-render the component
           (render! el))})

