(ns build
  (:require [clojure.tools.build.api :as b]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [deps-deploy.deps-deploy :as deploy]))

(def version "0.1.0-SNAPSHOT")
(def class-dir "target/classes")

;; Library configurations
(def ty-lib
  {:group-id "dev.gersak"
   :artifact-id "ty"
   :version version
   :basis-alias :ty-lib
   :src-dirs ["lib"]
   :resource-dirs ["resources/ty"]
   :jar-file (format "target/ty-%s.jar" version)})

(def ty-icons-lib
  {:group-id "dev.gersak"
   :artifact-id "ty-icons"
   :version version
   :basis-alias :ty-icons-lib
   :src-dirs ["gen"]
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
