(ns tp2.tabla-pruebas-test
  (:require [clojure.test :refer :all]
            [tp2.mv.evaluador :as mv]))

;; --- PRUEBAS DE LA CATEDRA ---
(deftest test-tabla-oficial-exitos
  (testing "Casos Básicos y Números"
    (let [x 1 y 2 t 3]
      (is (= {:ok [0 0 0]} (mv/evaluar-pixel "" x y t)))
      (is (= {:ok [0 0 1]} (mv/evaluar-pixel "X" x y t)))
      (is (= {:ok [0 0 2]} (mv/evaluar-pixel "Y" x y t)))
      (is (= {:ok [0 0 3]} (mv/evaluar-pixel "T" x y t)))
      (is (= {:ok [0 1 2]} (mv/evaluar-pixel "XY" x y t)))
      (is (= {:ok [1 2 3]} (mv/evaluar-pixel "XYT" x y t)))
      (is (= {:ok [3 1 2]} (mv/evaluar-pixel "XYTXYTXY" x y t)))
      (is (= {:ok [0 0 1]} (mv/evaluar-pixel "N1" x y t)))
      (is (= {:ok [0 0 2]} (mv/evaluar-pixel "N2" x y t)))
      (is (= {:ok [0 0 3]} (mv/evaluar-pixel "N3" x y t)))
      (is (= {:ok [0 0 4]} (mv/evaluar-pixel "N4" x y t)))
      (is (= {:ok [0 0 5]} (mv/evaluar-pixel "N5" x y t)))
      (is (= {:ok [0 0 6]} (mv/evaluar-pixel "N6" x y t)))
      (is (= {:ok [0 0 7]} (mv/evaluar-pixel "N7" x y t)))
      (is (= {:ok [0 0 8]} (mv/evaluar-pixel "N8" x y t)))
      (is (= {:ok [0 0 9]} (mv/evaluar-pixel "N9" x y t)))
      (is (= {:ok [0 8 9]} (mv/evaluar-pixel "N8N9" x y t)))
      (is (= {:ok [7 8 9]} (mv/evaluar-pixel "N7N8N9" x y t)))
      (is (= {:ok [5 6 7]} (mv/evaluar-pixel "N0N1N2N3N4N5N6N7" x y t)))
      ))

  (testing "Casos de Manipulación de Pila"
    (let [x 1 y 2 t 3]
      (is (= {:ok [1 2 2]} (mv/evaluar-pixel "XYD" x y t)))
      (is (= {:ok [5 6 6]} (mv/evaluar-pixel "N4N5N6D" x y t)))
      (is (= {:ok [0 0 1]} (mv/evaluar-pixel "XYP" x y t)))
      (is (= {:ok [0 2 1]} (mv/evaluar-pixel "XYS" x y t)))
      (is (= {:ok [1 3 2]} (mv/evaluar-pixel "XYTS" x y t)))
      (is (= {:ok [2 3 1]} (mv/evaluar-pixel "XYTR" x y t)))
      (is (= {:ok [2 3 1]} (mv/evaluar-pixel "N9XYTR" x y t)))
      (is (= {:ok [9 2 3]} (mv/evaluar-pixel "N9XYTRP" x y t)))))

  (testing "Casos de Aritmética"
    (is (= {:ok [0 0 3]} (mv/evaluar-pixel "XY+" 1 2 3)))
    (is (= {:ok [0 0 2]} (mv/evaluar-pixel "N1N1+" 1 2 3)))
    (is (= {:ok [0 0 6]} (mv/evaluar-pixel "XYT++" 1 2 3)))
    (is (= {:ok [0 0 2147483647]} (mv/evaluar-pixel "XY+" 2147483637 10 3)))
    (is (= {:ok [0 0 -1]} (mv/evaluar-pixel "XY-" 1 2 3)))
    (is (= {:ok [0 0 6]} (mv/evaluar-pixel "XY*" 3 2 10)))
    (is (= {:ok [0 0 -6]} (mv/evaluar-pixel "XY*" -3 2 10)))
    (is (= {:ok [0 0 -6]} (mv/evaluar-pixel "XY*" 3 -2 10)))
    (is (= {:ok [0 0 6]} (mv/evaluar-pixel "XY*" -3 -2 10)))
    (is (= {:ok [0 0 2]} (mv/evaluar-pixel "XY/" 4 2 10)))
    (is (= {:ok [0 0 1]} (mv/evaluar-pixel "XY/" 4 3 10)))
    (is (= {:ok [0 0 -1]} (mv/evaluar-pixel "XY/" -4 3 10)))
    (is (= {:ok [0 0 -1]} (mv/evaluar-pixel "XY/" 4 -3 10)))
    (is (= {:ok [0 0 0]} (mv/evaluar-pixel "XY/" 4 5 10)))
    (is (= {:ok [0 0 4]} (mv/evaluar-pixel "XY%" 4 5 10)))
    (is (= {:ok [0 0 2]} (mv/evaluar-pixel "XY%" 7 5 10)))
    (is (= {:ok [0 0 2]} (mv/evaluar-pixel "XY%" 7 -5 10)))
    (is (= {:ok [0 0 3]} (mv/evaluar-pixel "XY%" -7 5 10)))
    (is (= {:ok [0 0 3]} (mv/evaluar-pixel "XY%" -7 -5 10)))))

  (testing "Casos Lógicos y de Comparación"
    (is (= {:ok [0 0 2]} (mv/evaluar-pixel "XY^" 1 3 20)))
    (is (= {:ok [0 0 1]} (mv/evaluar-pixel "XY&" 1 3 20)))
    (is (= {:ok [0 0 3]} (mv/evaluar-pixel "XY|" 1 3 20)))
    (is (= {:ok [0 0 1]} (mv/evaluar-pixel "X!" 0 2 3)))
    (is (= {:ok [0 0 0]} (mv/evaluar-pixel "X!" 1 2 3)))
    (is (= {:ok [0 0 0]} (mv/evaluar-pixel "Y!" 1 2 3)))
    (is (= {:ok [0 0 0]} (mv/evaluar-pixel "T!" 1 2 3)))
    (is (= {:ok [0 0 0]} (mv/evaluar-pixel "X!" -1 2 3))))
    (is (= {:ok [0 0 0]} (mv/evaluar-pixel "X!" -2 2 3)))
    (is (= {:ok [0 0 1]} (mv/evaluar-pixel "X!!" 2 2 3)))
    (is (= {:ok [0 0 0]} (mv/evaluar-pixel "XY=" 1 2 3)))
    (is (= {:ok [0 0 1]} (mv/evaluar-pixel "XX=" 1 2 3)))
    (is (= {:ok [0 0 1]} (mv/evaluar-pixel "XY<" 1 2 3)))
    (is (= {:ok [0 0 0]} (mv/evaluar-pixel "XY>" 1 2 3)))

(deftest test-tabla-oficial-ciclos
  (let [x 1 y 2 t 3]
    (is (= {:ok [0 0 1]} (mv/evaluar-pixel "XN0[N1+]" x y t)))
    (is (= {:ok [0 0 1]} (mv/evaluar-pixel "XN0N1-[N1+]" x y t)))
    (is (= {:ok [0 0 2]} (mv/evaluar-pixel "XN1[N1+]" x y t)))
    (is (= {:ok [0 0 14]} (mv/evaluar-pixel "N2N3[N4+]" x y t)))
    (is (= {:ok [0 0 19]} (mv/evaluar-pixel "XTX-[N9+]" x y t)))
    (is (= {:ok [0 19 1]} (mv/evaluar-pixel "XTX-[N9+]X" x y t)))
    (is (= {:ok [0 0 0]} (mv/evaluar-pixel "XX-[N4+]" x y t)))
    (is (= {:ok [0 0 1]} (mv/evaluar-pixel "XX-[N4+]X" x y t)))
    (is (= {:ok [0 0 6]} (mv/evaluar-pixel "N0N2[N3[N1+]]" x y t)))
    (is (= {:ok [0 0 24]} (mv/evaluar-pixel "N0N2[N3[N4[N1+]]]" x y t)))))


(deftest test-tabla-oficial-errores
  (let [x 1 y 2 t 3]
    (is (= {:error "Pila de datos llena"} (mv/evaluar-pixel "XYTXYTXYT" x y t)))
    (is (= {:error "Pila de datos llena"} (mv/evaluar-pixel "NNNNNNNNN" x y t)))
    (is (= {:error "Pila de datos llena"} (mv/evaluar-pixel "N0N1N2N3N4N5N6N7N8" x y t)))
    (is (= {:error "Pila de datos llena"} (mv/evaluar-pixel "N0N1N2N3N4N5N6N7ND" x y t)))
    (is (= {:error "Pila vacia"} (mv/evaluar-pixel "P" x y t)))
    (is (= {:error "Pila con operandos insuficientes"} (mv/evaluar-pixel "XYR" x y t)))
    (is (= {:error "Pila con operandos insuficientes"} (mv/evaluar-pixel "XS" x y t)))
    (is (= {:error "Pila con operandos insuficientes"} (mv/evaluar-pixel "X+" x y t)))
    (is (= {:error "Pila con operandos insuficientes"} (mv/evaluar-pixel "TX-[N1+]" x y t)))
    (is (= {:error "Pila de ciclos llena"} (mv/evaluar-pixel "N0N1[N1[N1[N1[N1[N1[N1[N1[N1[N1+]]]]]]]]]" x y t)))
    (is (= {:error "Pila con operandos insuficientes"} (mv/evaluar-pixel "XYT+++" x y t)))
    (is (= {:error "Division por cero"} (mv/evaluar-pixel "XY/" 1 0 3)))
    (is (= {:error "Division por cero"} (mv/evaluar-pixel "XY%" 1 0 3)))))