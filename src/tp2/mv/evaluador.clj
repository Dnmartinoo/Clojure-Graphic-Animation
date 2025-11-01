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
              (let [comando (get codigo idx)
                    nuevo-estado (case comando
                                   ;; --COMANDOS BASICOS--
                                   \X (ops/op-apilar-x estado-actual x)
                                   \Y (ops/op-apilar-y estado-actual y)
                                   \T (ops/op-apilar-t estado-actual t)
                                   \N (ops/op-apilar-cero estado-actual)
                                   ;; --MANIPULACION DE PILA--
                                   \C (ops/op-clamp estado-actual)
                                   \D (ops/op-duplicar estado-actual)
                                   \P (ops/op-pop estado-actual)
                                   \S (ops/op-swap estado-actual)
                                   \R (ops/op-rotar estado-actual)
                                   ;; --LOGICA DE DIGITOS--
                                   (let [es-digito (Character/isDigit comando)]
                                     (if es-digito
                                       (let [valor-digito (Character/digit comando 10)]
                                         (ops/op-digito estado-actual valor-digito))
                                       estado-actual))
                                   )]
                (recur (update nuevo-estado :idx inc))))
            ))]

(obtener-rgb-desde-estado estado-final)))