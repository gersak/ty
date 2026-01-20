(ns ty.site.docs.common
  "Common utilities for component documentation"
  (:require
   [clojure.string :as str]
   [goog.object]))

(defn add-code-enhancements!
  "Add copy button and language label to a highlighted code element"
  [^js code-el lang]
  (when-let [container (.-parentElement code-el)]
    ;; Set container positioning
    (set! (.. container -style -position) "relative")

    ;; Add language label (if meaningful)
    (when (and lang
               (not= lang "hljs")
               (not= lang "code"))
      (let [label (.createElement js/document "div")]
        (set! (.-textContent label) lang)
        (set! (.-cssText (.-style label))
              "position: absolute; top: 0.5rem; right: 3rem; font-size: 0.75rem;
               color: var(--ty-text--); background: var(--ty-surface-content);
               padding: 0.25rem 0.5rem; border-radius: 4px;
               border: 1px solid var(--ty-border-); pointer-events: none; z-index: 10;")
        (.appendChild container label)))

    ;; Add copy button
    (let [copy-btn (.createElement js/document "button")]
      (set! (.-innerHTML copy-btn) "<ty-icon name=\"copy\" size=\"sm\"></ty-icon>")
      (set! (.-title copy-btn) "Copy to clipboard")
      (set! (.-cssText (.-style copy-btn))
            "position: absolute; top: 0.5rem; right: 0.5rem; width: 2rem; height: 2rem;
             background: var(--ty-surface-elevated); border: 1px solid var(--ty-border);
             border-radius: 4px; cursor: pointer; opacity: 0.7;
             transition: opacity 0.2s, background-color 0.2s;
             display: flex; align-items: center; justify-content: center; padding: 0; z-index: 10;")

      ;; Copy functionality
      (.addEventListener copy-btn "click"
                         (fn [e]
                           (.preventDefault e)
                           (.stopPropagation e)
                           (let [code-text (.-textContent code-el)]
                             (-> (js/navigator.clipboard.writeText code-text)
                                 (.then (fn []
                                          (set! (.-innerHTML copy-btn) "<ty-icon name=\"check\" size=\"sm\" class=\"ty-text-success\"></ty-icon>")
                                          (set! (.. copy-btn -style -backgroundColor) "var(--ty-bg-success-)")
                                          (js/setTimeout #(do (set! (.-innerHTML copy-btn) "<ty-icon name=\"copy\" size=\"sm\"></ty-icon>")
                                                              (set! (.. copy-btn -style -backgroundColor) "var(--ty-surface-elevated)"))
                                                         2000)))
                                 (.catch (fn [err]
                                           (js/console.error "Failed to copy code:" err)
                                           (set! (.-innerHTML copy-btn) "<ty-icon name=\"x\" size=\"sm\" class=\"ty-text-danger\"></ty-icon>")
                                           (set! (.. copy-btn -style -backgroundColor) "var(--ty-bg-danger-)")
                                           (js/setTimeout #(do (set! (.-innerHTML copy-btn) "<ty-icon name=\"copy\" size=\"sm\"></ty-icon>")
                                                               (set! (.. copy-btn -style -backgroundColor) "var(--ty-surface-elevated)"))
                                                          2000)))))))

      ;; Hover effects
      (.addEventListener copy-btn "mouseenter"
                         #(do (set! (.. copy-btn -style -opacity) "1")
                              (set! (.. copy-btn -style -backgroundColor) "var(--ty-surface-floating)")))

      (.addEventListener copy-btn "mouseleave"
                         #(do (set! (.. copy-btn -style -opacity) "0.7")
                              (set! (.. copy-btn -style -backgroundColor) "var(--ty-surface-elevated)")))

      (.appendChild container copy-btn))))

(defn code-block
  "Display a code block with syntax highlighting"
  ([code] (code-block code "html"))
  ([code lang]
   [:div.ty-bg-neutral-.rounded-md.p-4.overflow-x-auto.my-4
    [:pre
     [:code.text-xs
      {:replicant/on-render (fn [{^js el :replicant/node}]
                              (when (and el
                                         js/window.hljs
                                         (.-highlightElement js/window.hljs))
                                ;; Check if already highlighted by hljs
                                (let [already-highlighted? (and (.-dataset el)
                                                                (.-highlighted (.-dataset el)))]
                                  (when-not already-highlighted?
                                    ;; Set the code content
                                    (set! (.-textContent el) code)
                                    (js/setTimeout
                                     (fn []
                                       (try
                                         ;; Double-check not highlighted (race condition protection)
                                         (when-not (and (.-dataset el)
                                                        (.-highlighted (.-dataset el)))
                                           ;; Highlight the element
                                           (js/window.hljs.highlightElement el)
                                           ;; Add copy button and language label
                                           (add-code-enhancements! el lang))
                                         (catch js/Error e
                                           (js/console.warn "Failed to highlight code block:" e))))
                                     50)))))}]]]))

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

(defn slugify
  "Convert text to URL-friendly slug"
  [text]
  (-> text
      str
      str/lower-case
      (str/replace #"[^\w\s-]" "")
      (str/replace #"\s+" "-")
      (str/replace #"-+" "-")
      str/trim))

(defn doc-section
  "Create a documentation section with title and content.
   Auto-generates ID from title if not provided."
  ([title content]
   (doc-section title nil content))
  ([title id content]
   (let [section-id (or id (slugify title))]
     [:section.mb-12 {:id section-id}
      [:h2.text-2xl.font-semibold.ty-text.mb-4.scroll-mt-6 title]
      content])))

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
     [:ty-icon.mx-auto.mb-4.w-12.h-12.ty-text- {:name "clock"}]
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
