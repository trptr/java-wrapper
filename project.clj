;; this project.clj is used only for codox API doc generation + Clojars deployment
(defproject trptr/java-wrapper "0.2.0"
  :description "Clojure wrappers for various Java classes."
  :url "https://github.com/trptr/java-wrapper"
  :license {:name "GNU General Public License v3.0"
            :url  "https://github.com/trptr/java-wrapper/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :plugins  [[lein-codox "0.10.5"]]
  :codox {:metadata    {:doc/format :markdown}
          :output-path "docs"
          :source-uri  "https://github.com/trptr/java-wrapper/blob/master/{filepath}#L{line}"})
