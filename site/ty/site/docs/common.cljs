(ns ty.site.docs.common
  "Common utilities for component documentation"
  (:require
   [goog.object]))

(defn code-block
  "Display a code block with syntax highlighting"
  ([code] (code-block code "html"))
  ([code lang]
   [:div.ty-bg-neutral-.rounded.p-4.overflow-x-auto
    [:pre
     [:code.text-xs
      {:class (str "language-" lang)
       :replicant/on-mount (fn [{^js el :replicant/node}]
                             (js/setTimeout
                              (fn []
                                (when (and el
                                           js/window.hljs
                                           (.-highlightElement js/window.hljs)
                                           ;; Safety check: only highlight if not already highlighted
                                           (not (.. el -dataset -highlighted)))
                                  (try
                                    (js/window.hljs.highlightElement el)
                                    (catch js/Error e
                                      (js/console.warn "Failed to highlight code block:" e)))))
                              100))}
      code]]]))

(defn attribute-table
  "Display component attributes in a table format"
  [attributes]
  [:div.overflow-x-auto
   [:table.w-full
    [:thead
     [:tr.border-b.ty-border
      [:th.text-left.px-4.py-2.ty-text+ "Name"]
      [:th.text-left.px-4.py-2.ty-text+ "Type"]
      [:th.text-left.px-4.py-2.ty-text+ "Default"]
      [:th.text-left.px-4.py-2.ty-text+ "Description"]]]
    [:tbody
     (for [{:keys [name type default description required]} attributes]
       [:tr.border-b.ty-border-
        [:td.px-4.py-2.ty-text.font-mono.text-sm name]
        [:td.px-4.py-2.ty-text-.text-sm type]
        [:td.px-4.py-2.ty-text-.text-sm.font-mono (or default "-")]
        [:td.px-4.py-2.ty-text-.text-sm
         description
         (when required
           [:span.ml-2.ty-bg-danger.ty-text-danger++.px-2.py-0.5.rounded.text-xs "required"])]])]]])

(defn event-table
  "Display component events in a table format"
  [events]
  [:div.overflow-x-auto
   [:table.w-full
    [:thead
     [:tr.border-b.ty-border
      [:th.text-left.px-4.py-2.ty-text+ "Event"]
      [:th.text-left.px-4.py-2.ty-text+ "Payload"]
      [:th.text-left.px-4.py-2.ty-text+ "When Fired"]]]
    [:tbody
     (for [{:keys [name payload when-fired]} events]
       [:tr.border-b.ty-border-
        [:td.px-4.py-2.ty-text.font-mono.text-sm name]
        [:td.px-4.py-2.ty-text-.text-xs.font-mono payload]
        [:td.px-4.py-2.ty-text-.text-xs when-fired]])]]])

(defn example-section
  "Create an example section with live demo and code"
  ([title demo code] (example-section title demo code "html"))
  ([title demo code language]
   [:div.mt-4
    [:h3.text-lg.font-medium.ty-text.mb-2 title]
    [:div.mb-4 demo]
    (code-block code language)]))

(defn doc-section
  "Create a documentation section with title and content"
  ([title content]
   [:section.mb-12
    [:h2.text-2xl.font-semibold.ty-text.mb-4 title]
    content])
  ([title id content]
   [:section.mb-12 {:id id}
    [:h2.text-2xl.font-semibold.ty-text.mb-4 title]
    content]))

(defn placeholder-view
  "Placeholder view for components not yet documented"
  [component-name]
  [:div.max-w-4xl.mx-auto.p-6
   [:h1.text-3xl.font-bold.ty-text.mb-4 (str "ty-" component-name)]
   [:p.text-lg.ty-text-.mb-8 "Documentation coming soon..."]
   [:div.ty-elevated.rounded-lg.p-6
    [:p.ty-text- "This component documentation is under construction. Check back soon for:"]
    [:ul.mt-4.space-y-2.ml-4
     [:li.ty-text- "• Complete API reference"]
     [:li.ty-text- "• Live examples"]
     [:li.ty-text- "• Best practices"]
     [:li.ty-text- "• Framework integration guides"]]]])

(defn guide-placeholder-view
  "Placeholder view for guide pages not yet documented"
  [guide-name guide-description]
  [:div.max-w-4xl.mx-auto.p-6
   [:h1.text-3xl.font-bold.ty-text.mb-4 guide-name]
   [:p.text-lg.ty-text-.mb-8 guide-description]
   [:div.ty-elevated.rounded-lg.p-6.text-center
    [:div.mb-6
     [:ty-icon.mx-auto.mb-4 {:name "clock" :class "w-12 h-12 ty-text-"}]
     [:h2.text-xl.font-semibold.ty-text.mb-2 "Coming Soon"]
     [:p.ty-text-.mb-6 "This guide is currently under development. We're working hard to bring you comprehensive documentation for integrating Ty components with this technology."]]

    [:div.ty-bg-neutral-.rounded-lg.p-4.mb-6
     [:p.ty-text-.mb-4 "In the meantime, you can:"]
     [:ul.text-left.space-y-2.ml-4
      [:li.ty-text- "• Explore the component documentation to understand available features"]
      [:li.ty-text- "• Check out the CSS System guide for styling best practices"]
      [:li.ty-text- "• Review existing examples in the repository"]
      [:li.ty-text- "• Join our community discussions for early access to guides"]]]

    [:div.flex.gap-4.justify-center
     [:button.ty-bg-primary.ty-text++.px-4.py-2.rounded.hover:opacity-90
      {:on {:click #(js/window.open "https://github.com/gersak/ty" "_blank")}}
      "View Repository"]
     [:button.ty-bg-secondary.ty-text++.px-4.py-2.rounded.hover:opacity-90
      {:on {:click #(-> js/window .-location .-href (set! "/docs/css"))}}
      "CSS System Guide"]]]])
