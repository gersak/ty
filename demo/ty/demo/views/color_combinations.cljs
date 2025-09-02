(ns ty.demo.views.color-combinations
  "Demo page for testing text color and background combinations")

(defn text-variant-grid
  "Creates a grid of text variants for a specific semantic color"
  [color-name]
  (let [variants ["strong" "mild" "" "soft" "faint"]
        bg-class (str "ty-bg-" color-name)
        bg-variants [(str bg-class "-") ; soft background
                     (str bg-class "") ; base background  
                     (str bg-class "+")]] ; mild background
    [:div.space-y-6
     [:h3.text-lg.font-semibold.ty-text (str (clojure.string/capitalize color-name) " Text on " (clojure.string/capitalize color-name) " Backgrounds")]

     ;; Background variants
     (for [bg-variant bg-variants]
       (let [bg-label (cond
                        (clojure.string/ends-with? bg-variant "-") "Soft Background"
                        (clojure.string/ends-with? bg-variant "+") "Mild Background"
                        :else "Base Background")]
         [:div {:key bg-variant}
          [:h4.text-sm.font-medium.ty-text-.mb-3 bg-label]
          [:div {:class (str bg-variant " p-6 rounded-lg border space-y-3")}
           ;; All text variants on this background
           (for [variant variants]
             (let [text-class (if (empty? variant)
                                (str "ty-text-" color-name)
                                (str "ty-text-" color-name "-" variant))
                   variant-label (if (empty? variant) "base" variant)]
               [:div {:key variant
                      :class (str text-class " flex justify-between items-center")}
                [:span (str "." text-class)]
                [:span.text-sm.font-mono variant-label]]))

           ;; Sample text content
           [:div.pt-3.border-t.border-opacity-20
            [:p {:class (str "ty-text-" color-name)}
             "Sample paragraph text to demonstrate readability and contrast on this background."]]]]))]))

(defn neutral-text-grid
  "Shows neutral text hierarchy on different neutral backgrounds"
  []
  [:div.space-y-6
   [:h3.text-lg.font-semibold.ty-text "Neutral Text Hierarchy on Neutral Backgrounds"]

   ;; Different neutral backgrounds
   (for [bg-variant ["ty-bg-neutral-" "ty-bg-neutral" "ty-bg-neutral+"]]
     (let [bg-label (cond
                      (clojure.string/ends-with? bg-variant "-") "Soft Neutral"
                      (clojure.string/ends-with? bg-variant "+") "Mild Neutral"
                      :else "Base Neutral")]
       [:div {:key bg-variant}
        [:h4.text-sm.font-medium.ty-text-.mb-3 bg-label]
        [:div {:class (str bg-variant " p-6 rounded-lg border space-y-2")}
         [:div.ty-text-neutral-strong "Strong neutral text (.ty-text-neutral-strong)"]
         [:div.ty-text-neutral-mild "Mild neutral text (.ty-text-neutral-mild)"]
         [:div.ty-text-neutral "Base neutral text (.ty-text-neutral)"]
         [:div.ty-text-neutral-soft "Soft neutral text (.ty-text-neutral-soft)"]
         [:div.ty-text-neutral-faint "Faint neutral text (.ty-text-neutral-faint)"]

         [:div.pt-3.border-t.border-opacity-20
          [:p.ty-text-neutral "This paragraph demonstrates how neutral text hierarchy works on neutral backgrounds with proper contrast ratios."]]]]))])

(defn cross-semantic-combinations
  "Shows how different semantic text colors look on various backgrounds"
  []
  [:div.space-y-6
   [:h3.text-lg.font-semibold.ty-text "Cross-Semantic Combinations"]
   [:p.text-sm.ty-text- "How different semantic text colors look on various semantic backgrounds"]

   (let [colors ["primary" "secondary" "success" "danger" "warning"]
         text-colors ["primary" "success" "danger" "warning" "neutral"]]
     [:div.grid.grid-cols-1.lg:grid-cols-2.gap-6
      (for [bg-color colors]
        [:div {:key bg-color}
         [:h4.text-sm.font-medium.ty-text-.mb-3 (str (clojure.string/capitalize bg-color) " Background")]
         [:div {:class (str "ty-bg-" bg-color "- p-4 rounded-lg border space-y-2")}
          (for [text-color text-colors]
            [:div {:key text-color
                   :class (str "ty-text-" text-color)}
             (str "." text-color " text on " bg-color " background")])]])])])

(defn accessibility-checker
  "Shows accessibility information for color combinations"
  []
  [:div.space-y-6
   [:h3.text-lg.font-semibold.ty-text "Accessibility & Best Practices"]

   [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
    ;; Recommended combinations
    [:div.ty-elevated.p-6.rounded-lg
     [:h4.text-sm.font-semibold.ty-text-success.mb-3 "✅ Recommended Combinations"]
     [:div.space-y-3
      [:div.ty-bg-success-.p-3.rounded.border
       [:div.ty-text-success-strong "Strong success text on soft success background"]
       [:div.ty-text-success.text-xs.mt-1 "High contrast, excellent readability"]]

      [:div.ty-bg-primary-.p-3.rounded.border
       [:div.ty-text-primary-strong "Strong primary text on soft primary background"]
       [:div.ty-text-primary.text-xs.mt-1 "Perfect for important information"]]

      [:div.ty-bg-danger-.p-3.rounded.border
       [:div.ty-text-danger-strong "Strong danger text on soft danger background"]
       [:div.ty-text-danger.text-xs.mt-1 "Clear error messaging"]]]]

    ;; Caution combinations  
    [:div.ty-elevated.p-6.rounded-lg
     [:h4.text-sm.font-semibold.ty-text-warning.mb-3 "⚠️  Use With Caution"]
     [:div.space-y-3
      [:div.ty-bg-warning.p-3.rounded.border
       [:div.ty-text-warning-faint "Faint warning text on base warning background"]
       [:div.ty-text-warning-strong.text-xs.mt-1 "Low contrast - avoid for body text"]]]]]])

(defn live-theme-switcher
  "Interactive theme switching to see all combinations in both themes"
  []
  [:div.space-y-6
   [:h3.text-lg.font-semibold.ty-text "Live Theme Testing"]
   [:p.text-sm.ty-text- "Toggle between light and dark themes to see how all combinations adapt"]

   [:div.flex.justify-center.gap-4.mb-6
    [:button.px-6.py-3.ty-elevated.border.ty-border.rounded-lg.hover:ty-content.ty-text.transition-colors
     {:on {:click #(.. js/document -documentElement -classList (remove "dark"))}}
     "☀️ Light Theme"]
    [:button.px-6.py-3.ty-bg-neutral.text-white.border.ty-border.rounded-lg.hover:ty-bg-neutral+.transition-colors
     {:on {:click #(.. js/document -documentElement -classList (add "dark"))}}
     "🌙 Dark Theme"]]

   ;; Quick demo of theme adaptation
   [:div.grid.grid-cols-1.md:grid-cols-3.gap-4
    [:div.ty-bg-primary-.ty-border-primary.border.rounded-lg.p-4.text-center
     [:div.ty-text-primary-strong.font-semibold.mb-2 "Primary Colors"]
     [:div.ty-text-primary "Automatically adapts"]
     [:div.ty-text-primary-faint.text-sm "Theme-aware design"]]

    [:div.ty-bg-success-.ty-border-success.border.rounded-lg.p-4.text-center
     [:div.ty-text-success-strong.font-semibold.mb-2 "Success Colors"]
     [:div.ty-text-success "Perfect contrast"]
     [:div.ty-text-success-faint.text-sm "Both light & dark"]]

    [:div.ty-bg-danger-.ty-border-danger.border.rounded-lg.p-4.text-center
     [:div.ty-text-danger-strong.font-semibold.mb-2 "Danger Colors"]
     [:div.ty-text-danger "Always readable"]
     [:div.ty-text-danger-faint.text-sm "WCAG compliant"]]]])

(defn color-system-reference
  "Quick reference showing the complete 5-variant system"
  []
  [:div.space-y-6
   [:h3.text-lg.font-semibold.ty-text "5-Variant System Reference"]
   [:p.text-sm.ty-text- "Complete overview of the text color and background system"]

   [:div.ty-elevated.p-6.rounded-lg
    [:h4.text-sm.font-semibold.ty-text.mb-4 "System Architecture"]
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
     [:div
      [:h5.text-sm.font-medium.ty-text-.mb-2 "Text Colors (--ty-color-*)"]
      [:div.text-xs.ty-text--.space-y-1
       [:div "• -strong: Maximum emphasis"]
       [:div "• -mild: High emphasis"]
       [:div "• base: Standard emphasis"]
       [:div "• -soft: Reduced emphasis"]
       [:div "• -faint: Minimal emphasis"]]]

     [:div
      [:h5.text-sm.font-medium.ty-text-.mb-2 "Backgrounds (--ty-bg-*)"]
      [:div.text-xs.ty-text--.space-y-1
       [:div "• +: Stronger/more saturated"]
       [:div "• base: Standard background"]
       [:div "• -: Softer/less saturated"]]]]]

   [:div.ty-elevated.p-6.rounded-lg
    [:h4.text-sm.font-semibold.ty-text.mb-4 "Usage Guidelines"]
    [:div.grid.grid-cols-1.md:grid-cols-2.gap-6
     [:div
      [:h5.text-sm.font-medium.ty-text-success.mb-2 "✅ Do"]
      [:div.text-xs.ty-text-.space-y-1
       [:div "• Use strong text on soft backgrounds"]
       [:div "• Match semantic meaning (success/success)"]
       [:div "• Test in both light and dark themes"]
       [:div "• Consider contrast ratios"]]]

     [:div
      [:h5.text-sm.font-medium.ty-text-danger.mb-2 "❌ Don't"]
      [:div.text-xs.ty-text-.space-y-1
       [:div "• Use faint text on saturated backgrounds"]
       [:div "• Mix competing semantic colors"]
       [:div "• Rely on color alone for meaning"]
       [:div "• Ignore accessibility guidelines"]]]]]])

(defn view []
  [:div.space-y-12

   ;; Header
   [:div.text-center.mb-8
    [:h1.text-4xl.font-bold.ty-text.mb-4 "Color Combinations Tester"]
    [:p.text-lg.ty-text- "Comprehensive testing of --ty-color-* text with --ty-bg-* backgrounds"]
    [:p.text-sm.ty-text-- "Testing the 5-variant system: strong, mild, base, soft, faint"]]

   ;; Live theme switcher at the top
   (live-theme-switcher)

   ;; Primary combinations
   [:div.demo-section
    (text-variant-grid "primary")]

   ;; Success combinations  
   [:div.demo-section
    (text-variant-grid "success")]

   ;; Danger combinations
   [:div.demo-section
    (text-variant-grid "danger")]

   ;; Warning combinations
   [:div.demo-section
    (text-variant-grid "warning")]

   ;; Secondary combinations
   [:div.demo-section
    (text-variant-grid "secondary")]

   ;; Neutral text hierarchy
   [:div.demo-section
    (neutral-text-grid)]

   ;; Cross-semantic combinations
   [:div.demo-section
    (cross-semantic-combinations)]

   ;; Accessibility checker
   [:div.demo-section
    (accessibility-checker)]

   ;; System reference
   [:div.demo-section
    (color-system-reference)]

   ;; Footer
   [:div.ty-elevated.p-6.rounded-lg.text-center
    [:h4.text-sm.font-semibold.ty-text.mb-2 "Color System Complete ✨"]
    [:p.text-sm.ty-text-
     "This demo showcases the complete ty color system with semantic text colors (--ty-color-*) and semantic backgrounds (--ty-bg-*). "
     "All combinations automatically adapt between light and dark themes while maintaining proper contrast ratios and accessibility standards."]]

   ;; Usage examples
   [:div.demo-section
    [:h3.text-lg.font-semibold.ty-text "Code Examples"]
    [:div.space-y-4
     [:div.ty-content.p-4.rounded-lg
      [:h4.text-sm.font-medium.mb-2 "Alert Component Pattern:"]
      [:pre.text-xs.ty-text-.font-mono
       "[:div.ty-bg-success-.ty-border-success.border.rounded.p-4\n"
       "  [:div.ty-text-success-strong.font-semibold \"Success!\"]\n"
       "  [:div.ty-text-success \"Operation completed successfully.\"]\n"
       "  [:div.ty-text-success-soft.text-sm \"Details...\"]]"]]

     [:div.ty-content.p-4.rounded-lg
      [:h4.text-sm.font-medium.mb-2 "Card Component Pattern:"]
      [:pre.text-xs.ty-text-.font-mono
       "[:div.ty-elevated.p-6.rounded-lg\n"
       "  [:h3.ty-text-primary-strong \"Card Title\"]\n"
       "  [:p.ty-text-neutral \"Body content with good contrast\"]\n"
       "  [:div.ty-text-neutral-soft.text-sm \"Helper text\"]]"]]

     [:div.ty-content.p-4.rounded-lg
      [:h4.text-sm.font-medium.mb-2 "Form Validation Pattern:"]
      [:pre.text-xs.ty-text-.font-mono
       "[:div.space-y-2\n"
       "  [:input.ty-input.border.ty-border-danger]\n"
       "  [:div.ty-text-danger.text-sm \"Error message\"]\n"
       "  [:div.ty-text-danger-soft.text-xs \"Validation hint\"]]"]]]]])
