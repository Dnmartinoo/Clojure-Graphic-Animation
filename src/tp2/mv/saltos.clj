(ns tp2.mv.saltos
  "Utilidad para el manejo de ciclos de la MV.
   Provee funciones para encontrar corchetes ']' correspondientes a un '['
   en el código, manejando correctamente los ciclos anidados.")

(defn encontrar-corchete [codigo idx-inicio]
  "Busca el ']' correspondiente a un '[' en el 'codigo'.
   Inicia la búsqueda en 'idx-inicio' (la posición del '[').
   Devuelve el índice del ']' si se encuentra, o nil si el código
   termina sin un par (lo que indica un error de sintaxis)."
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