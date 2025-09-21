(ns ty.site.docs.common
  "Common utilities for component documentation")

(defn code-block
  "Display a code block with syntax highlighting"
  [code & {:keys [lang]
           :or {lang "html"}}]
  [:div.ty-bg-neutral-.rounded.p-4.overflow-x-auto
   [:pre
    [:code.text-sm
     {:class (str "language-" lang)
      :ref (fn [el]
             (when el
               ;; Highlight the code block when element is mounted
               (when (and js/window.hljs (.-highlightElement js/window.hljs))
                 (js/window.hljs.highlightElement el))))}
     code]]])

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
        [:td.px-4.py-2.ty-text-.text-sm.font-mono payload]
        [:td.px-4.py-2.ty-text-.text-sm when-fired]])]]])

(defn example-section
  "Create an example section with live demo and code"
  ([title demo code] (example-section title demo code "html"))
  ([title demo code language]
   [:div
    [:h3.text-lg.font-medium.ty-text.mb-2 title]
    [:div.mb-4 demo]
    (code-block code :lang language)]))

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
