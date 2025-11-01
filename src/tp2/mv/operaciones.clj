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