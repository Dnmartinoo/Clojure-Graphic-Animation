(ns tp2.mv.saltos)

(defn encontrar-corchete [codigo idx-inicio]

  (loop [idx (inc idx-inicio)
         balance 1]
    (cond
      (>= idx (count codigo))
      nil

      (= (get codigo idx) \])
      (let [nuevo-balance (dec balance)]
        (if (zero? nuevo-balance)
          idx
          (recur (inc idx) nuevo-balance)))
      (= (get codigo idx) \[)
      (recur (inc idx) (inc balance))

      :else
      (recur (inc idx) balance)
      )))