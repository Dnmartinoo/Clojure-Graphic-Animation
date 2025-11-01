(defproject tp2-anim "0.1.0-SNAPSHOT"
  :description "TP2 Paradigmas - Animación VM - GUI/Observer/Scheduler"
  :url "https://example.com/tp2_anim"
  :license {:name "MIT"}
  :min-lein-version "2.9.8"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [seesaw "1.5.0"]]
  :main ^:skip-aot tp2_anim.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
