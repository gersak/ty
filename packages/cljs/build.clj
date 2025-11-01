(ns build
  (:require [clojure.data.json :as json]
            [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.tools.build.api :as b]
            [deps-deploy.deps-deploy :as deploy]
            [ty.template :as template]))

(def version "0.2.0-SNAPSHOT")
(def class-dir "target/classes")

;; Library configurations
(def ty-lib
  {:group-id "dev.gersak"
   :artifact-id "ty"
   :version version
   :basis-alias :ty-lib
   :src-dirs ["src"]
   :resource-dirs ["resources/ty"]
   :jar-file (format "target/ty-%s.jar" version)})

(def ty-icons-lib
  {:group-id "dev.gersak"
   :artifact-id "ty-icons"
   :version version
   :basis-alias :ty-icons-lib
   :src-dirs ["icons"]
   :resource-dirs ["resources/ty-icons"]
   :jar-file (format "target/ty-icons-%s.jar" version)})

(defn clean [_]
  (b/delete {:path "target"}))

(defn- get-pom-path [group-id artifact-id]
  (str class-dir "/META-INF/maven/" group-id "/" artifact-id "/pom.xml"))

(defn- create-pom [lib-config]
  (let [{:keys [group-id artifact-id version basis-alias src-dirs]} lib-config
        basis (b/create-basis {:aliases [basis-alias]})
        lib-symbol (symbol (str group-id "/" artifact-id))
        scm-info {:url "https://github.com/gersak/ty"
                  :connection "scm:git:git://github.com/gersak/ty.git"
                  :developer-connection "scm:git:ssh://git@github.com/gersak/ty.git"
                  :tag (str "v" version)}]
    (b/write-pom {:class-dir class-dir
                  :lib lib-symbol
                  :version version
                  :basis basis
                  :src-dirs src-dirs
                  :scm scm-info
                  :pom-data [[:description (if (= artifact-id "ty")
                                             "Modern web components library built with ClojureScript"
                                             "Pre-generated icon components for Ty web components library")]
                             [:url "https://github.com/gersak/ty"]
                             [:licenses
                              [:license
                               [:name "MIT License"]
                               [:url "https://opensource.org/licenses/MIT"]]]
                             [:developers
                              [:developer
                               [:name "Gersak"]
                               [:email "contact@gersak.dev"]]]]})))

(defn- create-jar [lib-config]
  (let [{:keys [src-dirs resource-dirs jar-file]} lib-config]
    (b/copy-dir {:src-dirs (concat src-dirs resource-dirs)
                 :target-dir class-dir})
    (b/jar {:class-dir class-dir
            :jar-file jar-file})))

;; ============================================================================
;; GitHub Pages Build Functions
;; ============================================================================

(defn read-ty-version
  "Read the current @gersak/ty version from packages/core/package.json"
  []
  (let [pkg-json-path "../core/package.json"
        pkg-json-file (io/file pkg-json-path)]
    (if (.exists pkg-json-file)
      (let [pkg-data (json/read-str (slurp pkg-json-file) :key-fn keyword)
            version (:version pkg-data)]
        (if version
          version
          (throw (ex-info "No version found in package.json"
                          {:path (.getAbsolutePath pkg-json-file)}))))
      (throw (ex-info "package.json not found"
                      {:path (.getAbsolutePath pkg-json-file)})))))

(defn github-pages
  "Build and deploy the documentation site to GitHub Pages.
   Uses CDN with pinned version from @gersak/ty NPM package."
  [_]
  (let [salt (template/random-string)
        ty-version (read-ty-version)
        github-root "/ty"] ; GitHub Pages will serve from gersak.github.io/ty

    ;; 1. Clean the docs directory
    (println "\n📦 Building Ty Documentation for GitHub Pages")
    (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    (println (format "📦 Using Ty version: %s from NPM CDN" ty-version))
    (println "→ Cleaning docs directory...")
    (b/delete {:path "../../docs"})

    ;; 2. Build JavaScript with Shadow-cljs
    (println "→ Building JavaScript with shadow-cljs...")
    (let [;; Read the original site build config
          shadow-config (edn/read-string (slurp "shadow-cljs.edn"))
          site-build (get-in shadow-config [:builds :github])
          ;; Create production build config with salt
          prod-build (-> site-build
                         ;; Update module name with salt for cache-busting
                         (assoc :modules {(keyword (str "site." salt))
                                          {:init-fn 'ty.site.core/init}}))]

      (println (format "  Using salt: %s" salt))
      (println "  Starting shadow-cljs build...")
      (let [command ["npx" "shadow-cljs"
                     "-A:dev"
                     "--config-merge" (str prod-build)
                     "release" "github"]
            _ (println (format "  Command: %s" (str/join " " command)))
            {:keys [exit out err]} (b/process {:command-args command
                                               :out :capture
                                               :err :capture})]
        (when (seq out)
          (println out))
        (when-not (zero? exit)
          (println "Shadow-cljs build failed!")
          (when (seq err)
            (println "Error output:")
            (println err))
          (throw (ex-info "Shadow-cljs build failed" {:exit exit})))
        (println "  ✓ JavaScript build completed")))

    ;; 3. Process HTML templates
    (println "→ Generating HTML files...")

    ;; Main index.html
    (template/process "resources/index.html.template"
                      "../../docs/index.html"
                      {:salt salt
                       :version ty-version
                       :root github-root})

    ;; 404.html for GitHub Pages SPA support
    (template/process "resources/index.html.template"
                      "../../docs/404.html"
                      {:salt salt
                       :version ty-version
                       :root github-root})

    ;; 4. Copy static assets
    (println "→ Copying static assets...")
    ;; Copy favicon if it exists in public directory
    (when (.exists (io/file "public/favicon.ico"))
      (io/copy (io/file "public/favicon.ico")
               (io/file "../../docs/favicon.ico"))
      (println "  ✓ Copied favicon.ico"))

    ;; Copy images directory if it exists
    (when (.exists (io/file "public/images"))
      (b/copy-dir {:src-dirs ["public/images"]
                   :target-dir "../../docs/images"})
      (println "  ✓ Copied images directory"))

    ;; 5. Create .nojekyll file (tells GitHub not to process with Jekyll)
    (spit "../../docs/.nojekyll" "")
    (println "  ✓ Created .nojekyll file")

    ;; 6. Summary
    (println "\n✅ GitHub Pages build complete!")
    (println "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
    (println (format "📁 Output directory: ../../docs/"))
    (println (format "🔑 Cache-busting salt: %s" salt))
    (println (format "📦 Ty Components: @gersak/ty@%s (CDN)" ty-version))
    (println (format "🌐 Will be served at: https://gersak.github.io/ty/"))
    (println "\nGenerated files:")
    (println "  - docs/index.html (main page)")
    (println "  - docs/404.html (SPA fallback)")
    (println (format "  - docs/js/site.%s.js (ClojureScript app)" salt))
    (println "\nCDN Resources:")
    (println (format "  - https://cdn.jsdelivr.net/npm/@gersak/ty@%s/css/ty.css" ty-version))
    (println (format "  - https://cdn.jsdelivr.net/npm/@gersak/ty@%s/dist/ty.js" ty-version))
    (println "\nNext steps:")
    (println "  1. Ensure @gersak/ty is published to NPM:")
    (println (format "     npm publish (current version: %s)" ty-version))
    (println "  2. Wait ~2 minutes for CDN propagation")
    (println "  3. Review the generated files in 'docs/'")
    (println "  4. Commit and push to GitHub:")
    (println "     git add docs/")
    (println "     git commit -m 'Update GitHub Pages documentation'")
    (println "     git push origin docs")
    (println "  5. GitHub Actions will deploy automatically")
    (println "")))

(defn build-ty [_]
  (println "Building dev.gersak/ty...")
  (clean nil)
  (create-pom ty-lib)
  (create-jar ty-lib)
  (println "Built:" (:jar-file ty-lib)))

(defn build-ty-icons [_]
  (println "Building dev.gersak/ty-icons...")
  (clean nil)
  (create-pom ty-icons-lib)
  (create-jar ty-icons-lib)
  (println "Built:" (:jar-file ty-icons-lib)))

(defn build-all [_]
  (build-ty nil)
  (build-ty-icons nil))

(defn install-ty [_]
  (build-ty nil)
  (b/install {:basis (b/create-basis {:aliases [:ty-lib]})
              :lib (symbol (str (:group-id ty-lib) "/" (:artifact-id ty-lib)))
              :version (:version ty-lib)
              :jar-file (:jar-file ty-lib)
              :class-dir class-dir})
  (println "Installed dev.gersak/ty to local repo"))

(defn install-ty-icons [_]
  (build-ty-icons nil)
  (b/install {:basis (b/create-basis {:aliases [:ty-icons-lib]})
              :lib (symbol (str (:group-id ty-icons-lib) "/" (:artifact-id ty-icons-lib)))
              :version (:version ty-icons-lib)
              :jar-file (:jar-file ty-icons-lib)
              :class-dir class-dir})
  (println "Installed dev.gersak/ty-icons to local repo"))

(defn install-all [_]
  (install-ty nil)
  (install-ty-icons nil))

(defn deploy-ty [_]
  (build-ty nil)
  (let [pom-path (get-pom-path (:group-id ty-lib) (:artifact-id ty-lib))]
    (deploy/deploy {:installer :remote
                    :artifact (:jar-file ty-lib)
                    :pom-file pom-path}))
  (println "Deployed dev.gersak/ty to Clojars"))

(defn deploy-ty-icons [_]
  (build-ty-icons nil)
  (let [pom-path (get-pom-path (:group-id ty-icons-lib) (:artifact-id ty-icons-lib))]
    (deploy/deploy {:installer :remote
                    :artifact (:jar-file ty-icons-lib)
                    :pom-file pom-path}))
  (println "Deployed dev.gersak/ty-icons to Clojars"))

(defn deploy-all [_]
  (deploy-ty nil)
  (deploy-ty-icons nil))
