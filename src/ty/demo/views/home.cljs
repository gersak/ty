(ns ty.demo.views.home)

(defn feature-card [{:keys [title description icon]}]
  [:div.bg-white.dark:bg-gray-800.p-6.rounded-lg.shadow-md
   [:div.flex.items-center.gap-3.mb-3
    [:ty-icon {:name icon
               :size "lg"
               :class "text-ty-important"}]
    [:h3.text-lg.font-semibold.text-gray-900.dark:text-white title]]
   [:p.text-gray-600.dark:text-gray-400 description]])

(defn code-example [code]
  [:pre.code-block
   [:code code]])

(defn view []
  [:div.max-w-6xl.mx-auto
   ;; Hero section
   [:div.text-center.mb-12
    [:h1.text-4xl.font-bold.text-gray-900.dark:text-white.mb-4
     "Ty Components Library"]
    [:p.text-xl.text-gray-600.dark:text-gray-400.mb-6
     "Modern Web Components built with ClojureScript"]
    [:div.flex.gap-4.justify-center
     [:ty-button {:flavor "important"
                  :filled true
                  :size "lg"}
      [:ty-icon {:name "book-open"
                 :slot "start"}]
      "View Documentation"]
     [:ty-button {:flavor "neutral"
                  :outlined true
                  :size "lg"}
      [:ty-icon {:name "github"
                 :slot "start"}]
      "GitHub"]]]

   ;; Features grid
   [:div.mb-12
    [:h2.demo-title "Why Ty?"]
    [:div.demo-grid
     (feature-card {:title "Web Standards"
                    :description "Built on native Web Components - works everywhere"
                    :icon "globe"})
     (feature-card {:title "Zero Dependencies"
                    :description "No framework required, just vanilla Web Components"
                    :icon "package"})
     (feature-card {:title "Semantic Design"
                    :description "Uses semantic naming instead of color names"
                    :icon "palette"})
     (feature-card {:title "CSS Variables"
                    :description "Fully customizable through CSS custom properties"
                    :icon "sliders"})
     (feature-card {:title "Performance"
                    :description "Static CSS, minimal runtime, shared stylesheets"
                    :icon "zap"})
     (feature-card {:title "Dark Mode"
                    :description "Built-in dark mode support with automatic adaptation"
                    :icon "moon"})]]

   ;; Quick start section
   [:div.demo-section
    [:h2.demo-title "Quick Start"]

    [:div.mb-6
     [:h3.demo-subtitle "1. Include CSS Variables"]
     (code-example "<link rel=\"stylesheet\" href=\"css/ty.variables.css\">")]

    [:div.mb-6
     [:h3.demo-subtitle "2. Include the Library"]
     (code-example "<script src=\"js/ty.js\"></script>")]

    [:div.mb-6
     [:h3.demo-subtitle "3. Use Components"]
     (code-example "<ty-button flavor=\"positive\">\n  <ty-icon name=\"check\" slot=\"start\"></ty-icon>\n  Save Changes\n</ty-button>")]

    [:div.mt-6
     [:h3.demo-subtitle "Example Result:"]
     [:div.flex.gap-4.items-center.flex-wrap
      [:ty-button {:flavor "positive"
                   :filled true}
       [:ty-icon {:name "check"
                  :slot "start"}]
       "Save Changes"]
      [:ty-button {:flavor "negative"
                   :outlined true}
       [:ty-icon {:name "x"
                  :slot "start"}]
       "Cancel"]
      [:ty-button {:flavor "important"
                   :accent true}
       [:ty-icon {:name "star"
                  :slot "start"}]
       "Featured"]]]]])
