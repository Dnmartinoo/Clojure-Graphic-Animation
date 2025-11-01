(ns tp2.core
  (:gen-class)
  (:require [tp2.eventos.observer :as obs]
            [tp2.interfaz.ventana :as win]
            [tp2.eventos.scheduler :as sched]))

(defn -main
  "lein run             → GUI con campo vacío
   lein run \"<codigo>\" → GUI con el TextField precargado"
  [& args]
  (let [sujeto (obs/crear-sujeto)
        app    (win/iniciar! sujeto)]
    (when-let [codigo (first args)]
      (sched/establecer-scheduler! (:scheduler app) codigo))))
