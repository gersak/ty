(ns ty.demo.views.icons
  (:require
    [clojure.string :as str]
    [ty.demo.state :refer [state]]
    [ty.fav6.brands :as fa-brands]
    [ty.fav6.regular :as fa-regular]
            ;; Font Awesome 6
    [ty.fav6.solid :as fa-solid]
    [ty.icons :as icons]
            ;; Material Icons
    [ty.material.filled :as mat-filled]
    [ty.material.outlined :as mat-outlined]))

(defn icon-card [{:keys [name icon-key]}]
  [:div.p-4.bg-white.dark:bg-gray-800.rounded-lg.shadow-sm.hover:shadow-md.transition-shadow.cursor-pointer.group
   {:on {:click (fn [e]
                  (js/navigator.clipboard.writeText (str "<ty-icon name=\"" icon-key "\"></ty-icon>"))
                  ;; Show a temporary tooltip
                  (let [target (.-currentTarget e)
                        tooltip (js/document.createElement "div")]
                    (set! (.-className tooltip) "absolute bg-gray-900 text-white px-2 py-1 rounded text-xs whitespace-nowrap")
                    (set! (.-textContent tooltip) "Copied!")
                    (set! (.. tooltip -style -bottom) "100%")
                    (set! (.. tooltip -style -left) "50%")
                    (set! (.. tooltip -style -transform) "translateX(-50%) translateY(-0.5rem)")
                    (.appendChild target tooltip)
                    (js/setTimeout #(.removeChild target tooltip) 1500)))}}
   [:div.flex.flex-col.items-center.gap-3
    [:ty-icon.text-gray-700.dark:text-gray-300.group-hover:text-ty-important.transition-colors
     {:name icon-key
      :size "xl"}]
    [:span.text-xs.text-gray-600.dark:text-gray-400.text-center.font-mono.break-all
     icon-key]]])

(defn search-input [{:keys [value on-change placeholder]}]
  [:div.relative
   [:input.w-full.px-4.py-2.pl-10.bg-white.dark:bg-gray-800.border.border-gray-300.dark:border-gray-600.rounded-lg.focus:outline-none.focus:ring-2.focus:ring-ty-important.focus:border-transparent
    {:type "text"
     :placeholder placeholder
     :value value
     :on {:input (fn [e] (on-change (.. e -target -value)))}}]
   [:ty-icon.absolute.text-gray-400
    {:name "search"
     :size "sm"
     :style {:top 14
             :left 14}}]])

(defn icon-section [{:keys [title icons]}]
  (when (seq icons)
    [:div.mb-8
     [:h3.text-lg.font-semibold.text-gray-900.dark:text-white.mb-4 title]
     [:div.grid.grid-cols-2.sm:grid-cols-3.md:grid-cols-4.lg:grid-cols-6.xl:grid-cols-8.gap-3
      (for [[icon-key _icon-svg] icons]
        [:div {:key icon-key}
         (icon-card {:name icon-key
                     :icon-key icon-key})])]]))

(icons/add! {"fa-solid/clock" fa-solid/clock
             "fa-solid/calendar" fa-solid/calendar
             "fa-solid/calendar-days" fa-solid/calendar-days
             "fa-solid/calendar-check" fa-solid/calendar-check
             "fa-solid/calendar-minus" fa-solid/calendar-minus
             "fa-solid/calendar-plus" fa-solid/calendar-plus
             "fa-solid/calendar-xmark" fa-solid/calendar-xmark
             "fa-solid/clock-rotate-left" fa-solid/clock-rotate-left
             "fa-solid/user-clock" fa-solid/user-clock
             "fa-solid/business-time" fa-solid/business-time
             "fa-solid/comment-dots" fa-solid/comment-dots
             "fa-solid/passport" fa-solid/passport
             "fa-solid/socks" fa-solid/socks
             "fa-solid/t-shirt" fa-solid/t-shirt
             "fa-solid/underline" fa-solid/underline

               ;; Font Awesome 6 Regular icons
             "fa-regular/clock" fa-regular/clock
             "fa-regular/calendar" fa-regular/calendar
             "fa-regular/calendar-check" fa-regular/calendar-check
             "fa-regular/comment" fa-regular/comment
             "fa-regular/comment-dots" fa-regular/comment-dots
             "fa-regular/envelope" fa-regular/envelope
             "fa-regular/heart" fa-regular/heart
             "fa-regular/star" fa-regular/star
             "fa-regular/user" fa-regular/user
             "fa-regular/hand" fa-regular/hand
             "fa-regular/file-zipper" fa-regular/file-zipper

               ;; Font Awesome 6 Brands
             "fa-brands/github" fa-brands/github
             "fa-brands/x-twitter" fa-brands/x-twitter
             "fa-brands/square-facebook" fa-brands/square-facebook
             "fa-brands/square-github" fa-brands/square-github
             "fa-brands/square-linkedin" fa-brands/square-linkedin
             "fa-brands/square-x-twitter" fa-brands/square-x-twitter
             "fa-brands/square-twitter" fa-brands/square-twitter

               ;; Material Filled with prefix
             "mat-filled/home" mat-filled/home
             "mat-filled/settings" mat-filled/settings
             "mat-filled/search" mat-filled/search
             "mat-filled/favorite" mat-filled/favorite
             "mat-filled/delete" mat-filled/delete
             "mat-filled/edit" mat-filled/edit
             "mat-filled/check" mat-filled/check
             "mat-filled/close" mat-filled/close
             "mat-filled/menu" mat-filled/menu
             "mat-filled/more-vert" mat-filled/more-vert

               ;; Material Outlined with prefix
             "mat-outlined/home" mat-outlined/home
             "mat-outlined/settings" mat-outlined/settings
             "mat-outlined/search" mat-outlined/search
             "mat-outlined/favorite" mat-outlined/favorite
             "mat-outlined/delete" mat-outlined/delete
             "mat-outlined/edit" mat-outlined/edit
             "mat-outlined/check-circle" mat-outlined/check-circle
             "mat-outlined/info" mat-outlined/info
             "mat-outlined/warning" mat-outlined/warning
             "mat-outlined/error" mat-outlined/error})

(defn filter-icons [icons search-term]
  (if (str/blank? search-term)
    icons
    (let [search (str/lower-case search-term)]
      (into {}
            (filter (fn [[k _v]]
                      (str/includes? (str/lower-case k) search))
                    icons)))))

(defn categorize-icons [all-icons]
  (let [fa-solid-icons (into {} (filter #(str/starts-with? (key %) "fa-solid/") all-icons))
        fa-regular-icons (into {} (filter #(str/starts-with? (key %) "fa-regular/") all-icons))
        fa-brands-icons (into {} (filter #(str/starts-with? (key %) "fa-brands/") all-icons))
        material-filled (into {} (filter #(str/starts-with? (key %) "mat-filled/") all-icons))
        material-outlined (into {} (filter #(str/starts-with? (key %) "mat-outlined/") all-icons))
        hero-icons (into {} (filter #(str/starts-with? (key %) "hero-") all-icons))
        lucide-icons (into {} (filter #(str/starts-with? (key %) "lucide-") all-icons))
        ui-icons (into {} (filter #(contains? #{"moon" "sun" "click" "image" "palette"
                                                "globe" "package" "sliders" "zap"
                                                "book-open" "github" "x" "plus"
                                                "heart" "trash-2" "download"} (key %))
                                  all-icons))
        other-icons (into {} (remove #(or (contains? (set (keys fa-solid-icons)) (key %))
                                          (contains? (set (keys fa-regular-icons)) (key %))
                                          (contains? (set (keys fa-brands-icons)) (key %))
                                          (contains? (set (keys material-filled)) (key %))
                                          (contains? (set (keys material-outlined)) (key %))
                                          (contains? (set (keys hero-icons)) (key %))
                                          (contains? (set (keys lucide-icons)) (key %))
                                          (contains? (set (keys ui-icons)) (key %)))
                                     all-icons))]
    {:fa-solid fa-solid-icons
     :fa-regular fa-regular-icons
     :fa-brands fa-brands-icons
     :material-filled material-filled
     :material-outlined material-outlined
     :hero hero-icons
     :lucide lucide-icons
     :ui ui-icons
     :other other-icons}))

(defn demo-section []
  [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6
   [:h2.text-2xl.font-bold.text-gray-900.dark:text-white.mb-6 "Icon Features"]

   [:div.space-y-8
    ;; Sizes
    [:div
     [:h3.text-lg.font-semibold.text-gray-900.dark:text-white.mb-4 "Sizes"]
     [:div.flex.flex-wrap.gap-4.items-end
      (for [s ["xs" "sm" "md" "lg" "xl" "2xl"]]
        [:div.text-center {:key s}
         [:ty-icon {:name "star"
                    :size s}]
         [:div.text-xs.text-gray-500.mt-1 s]])]]

    ;; Animations
    [:div
     [:h3.text-lg.font-semibold.text-gray-900.dark:text-white.mb-4 "Animations"]
     [:div.flex.gap-6
      [:div.text-center
       [:ty-icon {:name "settings"
                  :size "xl"
                  :spin true}]
       [:div.text-sm.text-gray-600.dark:text-gray-400.mt-2 "spin"]]
      [:div.text-center
       [:ty-icon {:name "heart"
                  :size "xl"
                  :pulse true}]
       [:div.text-sm.text-gray-600.dark:text-gray-400.mt-2 "pulse"]]]]

    ;; Color inheritance
    [:div
     [:h3.text-lg.font-semibold.text-gray-900.dark:text-white.mb-4 "Color Inheritance"]
     [:p.text-sm.text-gray-600.dark:text-gray-400.mb-3
      "Icons inherit color from their parent element"]
     [:div.flex.flex-wrap.gap-4
      [:div.text-ty-positive
       [:ty-icon {:name "check"
                  :size "xl"}]
       [:span.ml-2.text-sm "Success"]]
      [:div.text-ty-negative
       [:ty-icon {:name "x"
                  :size "xl"}]
       [:span.ml-2.text-sm "Error"]]
      [:div.text-ty-important
       [:ty-icon {:name "star"
                  :size "xl"}]
       [:span.ml-2.text-sm "Important"]]
      [:div.text-ty-exception
       [:ty-icon {:name "alert"
                  :size "xl"}]
       [:span.ml-2.text-sm "Warning"]]]]

    ;; Usage examples
    [:div
     [:h3.text-lg.font-semibold.text-gray-900.dark:text-white.mb-4 "Usage Examples"]
     [:div.space-y-4
      [:div.bg-gray-50.dark:bg-gray-900.p-4.rounded-md
       [:p.text-sm.font-semibold.text-gray-700.dark:text-gray-300.mb-2 "Basic usage:"]
       [:code.text-sm.text-gray-600.dark:text-gray-400.font-mono
        "<ty-icon name=\"home\"></ty-icon>"]]
      [:div.bg-gray-50.dark:bg-gray-900.p-4.rounded-md
       [:p.text-sm.font-semibold.text-gray-700.dark:text-gray-300.mb-2 "With attributes:"]
       [:code.text-sm.text-gray-600.dark:text-gray-400.font-mono
        "<ty-icon name=\"star\" size=\"xl\" spin></ty-icon>"]]
      [:div.bg-gray-50.dark:bg-gray-900.p-4.rounded-md
       [:p.text-sm.font-semibold.text-gray-700.dark:text-gray-300.mb-2 "In a button:"]
       [:code.text-sm.text-gray-600.dark:text-gray-400.font-mono
        "<ty-button><ty-icon name=\"plus\"></ty-icon> Add Item</ty-button>"]]]]]])


(defn icons-view []
  (let [{:keys [::search-term ::show-demo]} @state
        all-icons @icons/data

          ;; Filter icons based on search
        filtered-icons (filter-icons all-icons search-term)

          ;; Categorize filtered icons
        categorized (categorize-icons filtered-icons)

          ;; Count totals
        total-count (count all-icons)
        filtered-count (count filtered-icons)]
    [:div.max-w-7xl.mx-auto
       ;; Header
     [:div.mb-8
      [:h1.text-3xl.font-bold.text-gray-900.dark:text-white.mb-2
       "Icon Library"]
      [:p.text-lg.text-gray-600.dark:text-gray-400
       (str "Explore " total-count " icons from multiple libraries. Click any icon to copy its code.")]]

       ;; Controls
     [:div.bg-white.dark:bg-gray-800.rounded-lg.shadow-md.p-6.mb-8
      [:div.grid.grid-cols-1.md:grid-cols-2.gap-4
         ;; Search
       [:div
        [:label.block.text-sm.font-medium.text-gray-700.dark:text-gray-300.mb-2
         "Search Icons"]
        (search-input {:value search-term
                       :placeholder "Search by name..."
                       :on-change #(swap! state assoc ::search-term %)})]

         ;; Toggle demo
       [:div.flex.items-end.mb-1
        [:button.px-4.py-2.rounded-md.text-sm.font-medium.transition-colors
         {:class (if show-demo
                   [:bg-ty-important :text-white]
                   [:bg-gray-100 "dark:bg-gray-700" :text-gray-700 "dark:text-gray-300"])
          :on {:click #(swap! state update ::show-demo not)}}
         (if show-demo "Hide Demo" "Show Demo")]]]]

       ;; Results count
     (when-not (str/blank? search-term)
       [:div.mb-4.text-sm.text-gray-600.dark:text-gray-400
        (str "Found " filtered-count " icons matching \"" search-term "\"")])

       ;; Demo section
     (when show-demo
       [:div.mb-8
        (demo-section)])

       ;; Icon sections
     [:div
        ;; UI Icons
      (icon-section {:title "UI Icons"
                     :icons (:ui categorized)})

        ;; Font Awesome 6 Solid
      (icon-section {:title "Font Awesome 6 - Solid"
                     :icons (:fa-solid categorized)})

        ;; Font Awesome 6 Regular
      (icon-section {:title "Font Awesome 6 - Regular"
                     :icons (:fa-regular categorized)})

        ;; Font Awesome 6 Brands
      (icon-section {:title "Font Awesome 6 - Brands"
                     :icons (:fa-brands categorized)})

        ;; Material Icons Filled
      (icon-section {:title "Material Icons (Filled)"
                     :icons (:material-filled categorized)})

        ;; Material Icons Outlined
      (icon-section {:title "Material Icons (Outlined)"
                     :icons (:material-outlined categorized)})

        ;; Heroicons
      (icon-section {:title "Heroicons"
                     :icons (:hero categorized)})

        ;; Lucide Icons
      (icon-section {:title "Lucide Icons"
                     :icons (:lucide categorized)})

        ;; Other Icons
      (when (seq (:other categorized))
        (icon-section {:title "Other Icons"
                       :icons (:other categorized)}))]]))
