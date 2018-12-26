;; this project.clj is used only for codox API doc generation
(defproject java-wrapper "0.1.0"
  :description "Clojure wrappers for various Java classes."
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :plugins  [[lein-codox "0.10.5"]]
  :codox {:metadata    {:doc/format :markdown}
          :output-path "docs"})
