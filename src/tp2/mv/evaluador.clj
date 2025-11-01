(ns tp2.mv.evaluador

  (:require [tp2.mv.estado :as estado]
            [tp2.mv.operaciones :as ops]))

(defn- obtener-rgb-desde-estado [estado-final]
  "Toma los 3 valores del tope (principio) de la pila :ds"
  (let [pila (vec (:ds estado-final))
        [b g r] (take 3 pila)]
    [(or r 0) (or g 0) (or b 0)]))

(defn evaluar-pixel [codigo x y t]

  (let [estado-final
        (loop [estado-actual estado/estado-inicial]

          (let [idx (:idx estado-actual)]

            (if (>= idx (count codigo))
              estado-actual
              (let [
                    comando (get codigo idx)
                    nuevo-estado (case comando
                                   \X (ops/op-apilar-x estado-actual x)
                                   \Y (ops/op-apilar-y estado-actual y)
                                   \T (ops/op-apilar-t estado-actual t)
                                   estado-actual
                                   )]
                (recur (update nuevo-estado :idx inc))))
            ))]

(obtener-rgb-desde-estado estado-final)))