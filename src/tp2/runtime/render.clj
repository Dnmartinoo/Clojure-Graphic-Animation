(ns tp2.runtime.render
  (:require [tp2.mv.evaluador :as mv])
  (:import [java.awt.image BufferedImage]))

(defn- rgb->int [r g b]
  (unchecked-int
    (-> 0xFF000000
        (bit-or (bit-shift-left r 16))
        (bit-or (bit-shift-left g 8))
        (bit-or b))))

(defn frame->imagen-interna
  [codigo t]

  (let [imagen (BufferedImage. 256 256 BufferedImage/TYPE_INT_RGB)
        resultado-loop
        (loop [y 0]
          (if (< y 256)
            (let [resultado-fila (loop [x 0]
                                   (if (< x 256)

                                     (let [resultado-pixel (mv/evaluar-pixel codigo x y t)]
                                       (if-let [error (:error resultado-pixel)]
                                         {:error error}
                                         (let [[r g b] (:ok resultado-pixel)
                                               color-int (rgb->int r g b)]
                                           (.setRGB imagen x y color-int)
                                           (recur (inc x)))))
                                     {:ok true}))]
              (if (:error resultado-fila)
                resultado-fila
                (recur (inc y))))
            {:ok imagen}))]

    resultado-loop))