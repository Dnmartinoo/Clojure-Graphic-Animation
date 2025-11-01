(ns tp2.mv.evaluador

  (:require [tp2.mv.estado :as estado]
            [tp2.mv.operaciones :as ops]))

(defn- obtener-rgb-desde-estado [estado-final]
  "Toma los 3 valores del tope (principio) de la pila :ds"
  (let [pila (vec (:ds estado-final))
        [b g r] (take 3 pila)]
    [(or r 0) (or g 0) (or b 0)]))

(defn evaluar-pixel [codigo x y t]

  (let [resultado-final
        (loop [res {:ok estado/estado-inicial}]

          (if (or (:error res)
                  (:ok-final res)
                  (>= (:idx (:ok res)) (count codigo)))

            res
            (let [estado-actual (:ok res)
                  idx (:idx estado-actual)
                  comando (get codigo idx)
                  resultado-op (case comando
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
                                 ;; --COMANDOS DE ARITMETICA Y BITWISE--
                                 \+ (ops/op-suma estado-actual)
                                 \- (ops/op-resta estado-actual)
                                 \* (ops/op-multi estado-actual)
                                 \^ (ops/op-xor estado-actual)
                                 \& (ops/op-and estado-actual)
                                 \| (ops/op-or estado-actual)
                                 ;; --COMANDOS DE COMPARACION--
                                 \! (ops/op-not estado-actual)
                                 \= (ops/op-igual estado-actual)
                                 \< (ops/op-menor estado-actual)
                                 \> (ops/op-mayor estado-actual)
                                 ;; --COMANDOS DE MODULO--
                                 \M (ops/op-modo estado-actual)
                                 \/ (ops/op-division estado-actual)
                                 \% (ops/op-modulo estado-actual)
                                 ;; --LOGICA DE DIGITOS--
                                 (let [es-digito (Character/isDigit comando)]
                                   (if es-digito
                                     (let [valor-digito (Character/digit comando 10)]
                                       (ops/op-digito estado-actual valor-digito))
                                     {:error (str "Comando desconocido: " comando)}
                                     )))]
              (if (or (:error resultado-op) (:ok-final resultado-op))
                resultado-op
                (recur (update-in resultado-op [:ok :idx] inc))
                ))))]
    (cond
      (:error resultado-final)
      resultado-final
      (:ok-final resultado-final)
      {:ok (:ok-final resultado-final)}
      :else
      {:ok (obtener-rgb-desde-estado (:ok resultado-final))}
      )))