(ns tp2.eventos.dibujar_render
  (:import [java.awt Color]
           [java.awt.image BufferedImage]))

(defn imagen-degradado
  "Animacion Placeholder."
  [t]
  (let [imagen (BufferedImage. 256 256 BufferedImage/TYPE_INT_RGB)]
    (doseq [y (range 256)
            x (range 256)]
      (let [r (int (mod (+ x (* 2 t)) 256))
            g (int (mod (+ y (* 3 t)) 256))
            b (int (mod (+ (* 2 x) (* 2 y) t) 256))
            rgb (.getRGB (Color. r g b))]
        (.setRGB imagen x y rgb)))
    imagen))
