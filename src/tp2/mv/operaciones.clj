(ns tp2.mv.operaciones)

(defn op-apilar-x [estado x]

  (assoc estado
    :ds

    (cons x (:ds estado))
    ))