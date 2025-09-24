(ns ty.site.docs.replicant
  "Documentation for using Ty with Replicant"
  (:require
    [ty.site.docs.common :refer [code-block doc-section example-section]]))

(defn view
  "Main view for Replicant getting started documentation"
  []
  [:div.max-w-4xl.mx-auto.p-6
   [:h1.text-4xl.font-bold.ty-text.mb-4 "Getting Started with Replicant"]
   [:p.text-xl.ty-text-.mb-6
    "Learn how to use Ty web components with Replicant for reactive ClojureScript applications."]

   ;; Dependencies
   (doc-section "Dependencies"
                [:div
                 [:p.ty-text-.mb-4
                  "Add the required dependencies to your " [:code.ty-bg-neutral-.px-2.py-1.rounded "deps.edn"] " file:"]

                 (code-block
                   "{:deps {dev.gersak/ty {:mvn/version \"LATEST\"}
        dev.gersak/ty-icons {:mvn/version \"LATEST\"}
        no.cjohansen/replicant {:mvn/version \"LATEST\"}}}"
                   "clojure")

                 [:p.ty-text-.mt-4.text-sm
                  "Replace " [:code.ty-bg-neutral-.px-1.rounded "\"LATEST\""]
                  " with the current version numbers from Clojars."]])

   ;; Component Registration  
   (doc-section "Component Registration"
                [:div
                 [:p.ty-text-.mb-4
                  "In your ClojureScript namespace, require " [:code.ty-bg-neutral-.px-2.py-1.rounded "ty.components"]
                  " to register all Ty web components:"]

                 (code-block
                   "(ns my-app.core
  (:require [replicant.dom :as d]
            [replicant.core :as r]
            [ty.components]))  ; <- This registers all Ty components"
                   "clojure")

                 [:p.ty-text-.mt-4
                  "Once registered, you can use Ty components directly in your Replicant hiccup markup."]])

   ;; Basic Usage
   (doc-section "Basic Usage"
                [:div
                 [:p.ty-text-.mb-4
                  "Here's a simple example to verify everything is working:"]

                 (code-block
                   "(defonce app-state (r/atom {:message \"Hello from Ty!\"}))

(defn hello-view [state]
  [:div.ty-elevated.p-6.rounded-lg.max-w-md.mx-auto
   [:h2.ty-text++.text-xl.mb-4 (:message @state)]
   [:ty-button {:variant \"primary\"
                :on {:click #(swap! state assoc :message \"Button clicked!\")}}
    \"Click me\"]])

(defn mount! []
  (d/render
    [hello-view app-state]
    (js/document.getElementById \"app\")))"
                   "clojure")

                 [:p.ty-text-.mt-4
                  "This creates a simple app with Ty's styling and a working button component."]])

   ;; Routing
   (doc-section "Routing with ty.router"
                [:div.p-4.ty-content.rounded-lg
                 [:p.ty-text-.mb-4
                  "Ty includes a powerful routing system that works great with Replicant. Routes can be defined in any namespace and linked together hierarchically."]

                 [:h3.text-lg.font-semibold.ty-text.mb-3 "Multi-namespace Route Example"]
                 [:p.ty-text-.mb-4
                  "Here's how to organize routes across multiple namespaces:"]

      ;; Main namespace
                 (example-section
                   "Main Namespace (my-app.main)"
                   [:div.ty-bg-info-.border.ty-border-info.rounded.p-3.mb-4
                    [:p.ty-text-info.text-sm "Main namespace that links to site-specific routes"]]
                   "(ns my-app.main
  (:require [replicant.dom :as d]
            [replicant.core :as r]
            [ty.router :as router]
            [ty.components]
            [my-app.site-a :as site-a]
            [my-app.site-b :as site-b]))

;; Define main routes and link to sub-routes
(router/link :app/main
  [{:id :app/home
    :segment \"home\"
    :view home-view}
   {:id :app/site-a
    :segment \"site-a\"}  ; <- Link to siteA routes
   {:id :app/site-b
    :segment \"site-b\"}]) ; <- Link to siteB routes

(defn navigation []
  [:nav.ty-elevated.p-4.mb-6
   [:div.flex.gap-4
    [:ty-button {:variant (if (router/rendered? :app/home) \"primary\" \"secondary\")
                 :on {:click #(router/navigate! :app/home)}}
     \"Home\"]
    [:ty-button {:variant (if (router/rendered? :app/site-a) \"primary\" \"secondary\")
                 :on {:click #(router/navigate! :app/site-a)}}
     \"Site A\"]
    [:ty-button {:variant (if (router/rendered? :app/site-b) \"primary\" \"secondary\")
                 :on {:click #(router/navigate! :app/site-b)}}
     \"Site B\"]]])

(defn main-view []
  [:div
   [navigation]
   (cond
     (router/rendered? :app/home) [home-view]
     (router/rendered? :app/site-a) [site-a/view]
     (router/rendered? :app/site-b) [site-b/view]
     :else [:div \"Not found\"])])"
                   "clojure")

      ;; Site A namespace
                 (example-section
                   "Site A Namespace (my-app.site-a)"
                   [:div.ty-bg-success-.border.ty-border-success.rounded.p-3.mb-4
                    [:p.ty-text-success.text-sm "Site A defines its own routes and sub-navigation"]]
                   "(ns my-app.site-a
  (:require [ty.router :as router]))

;; Define routes for this namespace
(def routes
  [{:id :app.site-a/dashboard
    :segment \"dashboard\"
    :view dashboard-view}
   {:id :app.site-a/users
    :segment \"users\" 
    :view users-view}
   {:id :app.site-a/settings
    :segment \"settings\"
    :view settings-view}])

;; Link the routes to make them active
;; make sure that first argument is already existing route
(router/link :app/site-a routes)

(defn site-a-nav []
  [:div.ty-elevated.p-4.mb-4
   [:h3.ty-text++.mb-2 \"Site A Navigation\"]
   [:div.flex.gap-2
    [:ty-button {:variant (if (router/rendered? :app.site-a/dashboard) \"primary\" \"outline\")
                 :size \"sm\"
                 :on {:click #(router/navigate! :app.site-a/dashboard)}}
     \"Dashboard\"]
    [:ty-button {:variant (if (router/rendered? :app.site-a/users) \"primary\" \"outline\")
                 :size \"sm\" 
                 :on {:click #(router/navigate! :app.site-a/users)}}
     \"Users\"]
    [:ty-button {:variant (if (router/rendered? :app.site-a/settings) \"primary\" \"outline\")
                 :size \"sm\"
                 :on {:click #(router/navigate! :app.site-a/settings)}}
     \"Settings\"]]])

(defn view []
  [:div
   [site-a-nav]
   (map (fn [{:keys [id view]}] 
          (when (router/rendered? id true) 
            (view))) 
        routes)])"
                   "clojure")

      ;; Site B namespace  
                 (example-section
                   "Site B Namespace (my-app.site-b)"
                   [:div.ty-bg-warning-.border.ty-border-warning.rounded.p-3.mb-4
                    [:p.ty-text-warning.text-sm "Site B has its own independent route structure"]]
                   "(ns my-app.site-b 
  (:require [ty.router :as router]))

;; Different route structure for Site B
(def routes
  [{:id :app.site-b/products
    :segment \"products\"
    :view products-view}
   {:id :app.site-b/orders
    :segment \"orders\"
    :view orders-view}])

;; Link the routes to make them active
;; make sure that first argument is already existing route
(router/link :app/site-b routes)

(defn site-b-nav []
  [:div.ty-elevated.p-4.mb-4
   [:h3.ty-text++.mb-2 \"Site B Navigation\"]  
   [:div.flex.gap-2
    [:ty-button {:variant (if (router/rendered? :app.site-b/products) \"primary\" \"outline\")
                 :size \"sm\"
                 :on {:click #(router/navigate! :app.site-b/products)}}
     \"Products\"]
    [:ty-button {:variant (if (router/rendered? :app.site-b/orders) \"primary\" \"outline\")
                 :size \"sm\"
                 :on {:click #(router/navigate! :app.site-b/orders)}}
     \"Orders\"]]])

(defn view []
  [:div
   [site-b-nav]
   (map (fn [{:keys [id view]}]
          (when (router/rendered? id true)
            (view)))
        routes)])"
                   "clojure")

                 [:h3.text-lg.font-semibold.ty-text.mb-3.mt-8 "Key Router Functions"]
                 [:div.space-y-4
                  [:div
                   [:h4.font-medium.ty-text.mb-2 [:code.ty-bg-neutral-.px-2.py-1.rounded "router/navigate!"]]
                   [:p.ty-text-.text-sm "Programmatically navigate to a route:"]]

                  (code-block
                    ";; Navigate to specific routes
(router/navigate! :app/home)
(router/navigate! :app.site-a/users)
(router/navigate! :app.site-b/products)

;; Navigate with URL query parameters
(router/navigate! :app.site-a/user {:id 123})"
                    "clojure")

                  [:div
                   [:h4.font-medium.ty-text.mb-2 [:code.ty-bg-neutral-.px-2.py-1.rounded "router/rendered?"]]
                   [:p.ty-text-.text-sm "Check if a route is currently active for conditional rendering:"]]

                  (code-block
                    ";; Check exact route
(router/rendered? :app/site-a true)

;; Check if :app/site-a is on URL path
(router/rendered? :app/site-a)

;; Check with inheritance (default behavior)
(router/rendered? :app.site-a/users)

;; Use in conditional rendering
[:ty-button {:variant (if (router/rendered? :app/home) \"primary\" \"secondary\")
             :on {:click #(router/navigate! :app/home)}}
 \"Home\"]"
                    "clojure")]])

   ;; Placeholder for more content
   [:div.ty-elevated.rounded-lg.p-6.mt-12
    [:p.ty-text- "More sections will be added here based on our discussion."]]])
