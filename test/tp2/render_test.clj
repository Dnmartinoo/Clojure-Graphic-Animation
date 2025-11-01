(deftest test-render-secuencial
         (testing "Genera un frame de color sólido (Azul)"
                  (let [;; ¡CORREGIDO! Este es el código para Azul (R=0, G=0, B=255)
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
                  (let [;; ¡CORREGIDO! Este es el código para Rojo (R=255, G=0, B=0)
                        codigo "N255N0N0"
                        t 0
                        resultado (render/frame->imagen-interna codigo t)]
                    (is (contains? resultado :ok) "El resultado debe ser :ok")
                    (let [imagen (:ok resultado)]
                      (is (= color-rojo-int (.getRGB imagen 100 100))))))

         (testing "Propaga el error si un píxel falla"
                  (let [codigo "P" ; Este queda igual
                        t 0
                        resultado (render/frame->imagen-interna codigo t)]

                    (is (contains? resultado :error))
                    (is (= "Pila vacia" (:error resultado)))))
         )