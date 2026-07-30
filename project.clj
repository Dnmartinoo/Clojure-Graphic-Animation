(defproject clojure-graphic-animation "1.0.0"
  :description "Functional graphics engine powered by a stack-based virtual machine"
  :url "https://github.com/Dnmartinoo/Clojure-Graphic-Animation"
  :license {:name "Eclipse Public License 2.0"}

  :min-lein-version "2.9.8"

  :dependencies [[org.clojure/clojure "1.11.1"]
                 [seesaw "1.5.0"]]

  :main ^:skip-aot tp2.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
