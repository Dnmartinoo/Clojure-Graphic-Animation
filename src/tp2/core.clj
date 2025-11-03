(ns tp2.core
  (:gen-class)
  (:require [tp2.eventos.observer :as obs]
            [tp2.interfaz.ventana :as win]))

(defn -main
  "lein run             → GUI con campo vacío
   lein run \"<codigo>\" → GUI con el codigo"
  [& args]
  (let [sujeto (obs/crear-observer)
        codigo (first args)]
    (if codigo
      (win/iniciar! sujeto codigo)
      (win/iniciar! sujeto))
    nil))
