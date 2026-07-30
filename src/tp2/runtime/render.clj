(ns tp2.runtime.render
  (:require [tp2.mv.evaluador :as mv])
  (:import [java.awt.image BufferedImage]))

(defn- rgb->int [r g b]
  (unchecked-int
    (-> 0xFF000000
        (bit-or (bit-shift-left r 16))
        (bit-or (bit-shift-left g 8))
        (bit-or b))))


(defn- calcular-fila [codigo t y]
  (loop [x 0 colores-fila []]
    (if (< x 256)
      (let [resultado-pixel (mv/evaluar-pixel codigo x y t)]
        (if-let [error (:error resultado-pixel)]
          {:error error}
          (let [[r g b] (:ok resultado-pixel)
                color-int (rgb->int r g b)]
            (recur (inc x) (conj colores-fila color-int)))))
      {:ok colores-fila})))




(defn frame->imagen-interna [codigo t]
  (let [filas (range 256)
        resultados-filas (pmap #(calcular-fila codigo t %) filas)
        error-encontrado (first (filter :error resultados-filas))]
    (if error-encontrado
      error-encontrado

      (let [imagen (BufferedImage. 256 256 BufferedImage/TYPE_INT_RGB)]
        (doseq [y (range 256)]
          (let [colores-fila (:ok (nth resultados-filas y))]
            (doseq [x (range 256)]
              (.setRGB imagen x y (nth colores-fila x)))))
        {:ok imagen}))))

(defn generar-cuadro [codigo t]
  (let [resultado-interno (frame->imagen-interna codigo t)]
    (if-let [error (:error resultado-interno)]
      (throw (Exception. error))
      (:ok resultado-interno)
      )))

