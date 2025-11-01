(ns tp2.mv.operaciones)

(defn op-apilar-x [estado x]

  (assoc estado
    :ds

    (cons x (:ds estado))
    ))

(defn op-apilar-y [estado y]
  (assoc estado
    :ds
    (cons y (:ds estado))))

(defn op-apilar-t [estado t]
  (assoc estado
    :ds
    (cons t (:ds estado))))

(defn op-apilar-cero [estado]
  (assoc estado
    :ds
    (cons 0 (:ds estado))))

(defn op-digito [estado valor-digito]
  (if (empty? (:ds estado))

    estado

    (let [tope-actual (first (:ds estado))
          resto-pila (rest (:ds estado))
          nuevo-valor (+ (* tope-actual 10) valor-digito)
          ]
      (assoc estado
        :ds

        (cons nuevo-valor resto-pila)))))