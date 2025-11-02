(ns tp2.render-test
  (:require [clojure.test :refer :all]
            [tp2.runtime.render :as render])
  (:import [java.awt.image BufferedImage]))

(defn- rgb->int [r g b]
  (unchecked-int
    (-> 0xFF000000
        (bit-or (bit-shift-left r 16))
        (bit-or (bit-shift-left g 8))
        (bit-or b))))
(def color-azul-int (rgb->int 0 0 255))
(def color-rojo-int (rgb->int 255 0 0))

(deftest test-render-secuencial
  (testing "Genera un frame de color sólido (Azul)"
    (let [
          codigo "N0N0N255"
          t 0
          resultado (render/frame->imagen-interna codigo t)]

      (is (contains? resultado :ok) "El resultado debe ser :ok")

      (let [imagen (:ok resultado)]
        (is (instance? BufferedImage imagen) "El resultado debe ser un BufferedImage")
        (is (= 256 (.getWidth imagen)))
        (is (= 256 (.getHeight imagen)))
        (is (= color-azul-int (.getRGB imagen 0 0)))
        (is (= color-azul-int (.getRGB imagen 128 128)))
        (is (= color-azul-int (.getRGB imagen 255 255))))))

  (testing "Genera un frame de color sólido (Rojo)"
    (let [
          codigo "N255N0N0"
          t 0
          resultado (render/frame->imagen-interna codigo t)]
      (is (contains? resultado :ok) "El resultado debe ser :ok")
      (let [imagen (:ok resultado)]
        (is (= color-rojo-int (.getRGB imagen 100 100))))))

  (testing "Propaga el error si un píxel falla"
    (let [codigo "P"
          t 0
          resultado (render/frame->imagen-interna codigo t)]

      (is (contains? resultado :error))
      (is (= "Pila vacia" (:error resultado)))))
  )

(deftest test-render-adaptador-publico
  (testing "El adaptador devuelve la IMAGEN en caso de éxito"
    (let [codigo "N0N0N255"
          t 0
          resultado (render/generar-cuadro codigo t)]
      (is (not (map? resultado)) "No debe ser un mapa")
      (is (instance? BufferedImage resultado) "Debe ser un BufferedImage")
      (is (= color-azul-int (.getRGB resultado 0 0))))))

(testing "El adaptador LANZA UNA EXCEPCIÓN en caso de error"
  (let [codigo "P"
        t 0]
    (is (thrown? Exception (render/generar-cuadro codigo t)))
    (try
      (render/generar-cuadro codigo t)
      (catch Exception e
        (is (= "Pila vacia" (.getMessage e)))))))


