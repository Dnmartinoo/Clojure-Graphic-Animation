(ns tp2_anim.core
  (:gen-class)
  (:require [tp2_anim.eventos.observer :as obs]
            [tp2_anim.interfaz.ventana :as win]
            [tp2_anim.eventos.scheduler :as sched]))

(defn -main
  "lein run             → GUI con campo vacío
   lein run \"<codigo>\" → GUI con el TextField precargado"
  [& args]
  (let [sujeto (obs/crear-sujeto)
        app    (win/iniciar! sujeto)]
    (when-let [codigo (first args)]
      (sched/establecer-scheduler! (:scheduler app) codigo))))
