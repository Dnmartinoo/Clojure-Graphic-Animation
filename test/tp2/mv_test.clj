(ns tp2.mv-test

  (:require [clojure.test :refer :all] [tp2.mv.evaluador :as mv]))


(deftest test-mv-basicos
  (testing "Prueba simple: Codigo vacio"
    (let [codigo ""
          x 1, y 2, t 3
          rgb-esperado [0 0 0]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))

  ;; --- NUEVO TEST ---
  (testing "Comando X: apila el valor de x"
    (let [codigo "X"
          x 1, y 2, t 3
          rgb-esperado [0 0 1]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))

;; --- NUEVO TEST 'Y' ---
  (testing "Comando Y: apila el valor de y"
    (let [codigo "Y"
          x 1, y 2, t 3
          rgb-esperado [0 0 2]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))

  ;; --- NUEVO TEST 'T' ---
  (testing "Comando T: apila el valor de t"
    (let [codigo "T"
          x 1, y 2, t 3
          rgb-esperado [0 0 3]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))
  ;; --- NUEVO TEST 'N' ---
  (testing "Comando N: apila un 0"
    (let [codigo "N"
          x 1, y 2, t 3
          rgb-esperado [0 0 0]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))

  ;; --- NUEVO TEST 'N' + DÍGITOS ---
  (testing "Comandos N y dígitos: N12"
    (let [codigo "N12"
          x 1, y 2, t 3
          rgb-esperado [0 0 12]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))
  (testing "Comandos N y dígitos: Múltiples números N1N2"
    (let [codigo "N1N2"
          x 1, y 2, t 3
          rgb-esperado [0 1 2]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t))))))


(deftest test-mv-manipulacion-pila

  (testing "Comando C (Clamp): valor negativo a 0"
    (let [codigo "N0N5-C"
          x 1, y 2, t 3
          rgb-esperado [0 0 0]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))


  (testing "Comando C (Clamp): valor alto a 255"
    (let [codigo "N300C"
          x 1, y 2, t 3
          rgb-esperado [0 0 255]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))

  (testing "Comando D (Duplicate): N4N5N6D"
    (let [codigo "N4N5N6D"
          x 1, y 2, t 3
          rgb-esperado [5 6 6]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))

  (testing "Comando P (Pop): XYP"
    (let [codigo "XYP"
          x 1, y 2, t 3
          rgb-esperado [0 0 1]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))

  (testing "Comando S (Swap): XYS"
    (let [codigo "XYS"
          x 1, y 2, t 3
          rgb-esperado [0 2 1]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))

  (testing "Comando R (Rotate): XYTR"
    (let [codigo "XYTR"
          x 1, y 2, t 3
          rgb-esperado [2 3 1]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))
  )



(deftest test-mv-aritmetica-logica
  (testing "Comando + (Suma): N70N50+"
    (let [codigo "N70N50+"
          x 1, y 2, t 3
          rgb-esperado [0 0 120]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))

  (testing "Comando - (Resta): N7N5-"
    (let [codigo "N7N5-"
          x 1, y 2, t 3
          rgb-esperado [0 0 2]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))

  (testing "Comando * (Mult): XY*"
    (let [codigo "XY*"
          x 3, y 2, t 0
          rgb-esperado [0 0 6]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))

  (testing "Comando ^ (XOR): XY^"
    (let [codigo "XY^"
          x 1, y 3, t 0
          rgb-esperado [0 0 2]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))

  (testing "Comando & (AND): XY&"
    (let [codigo "XY&"
          x 1, y 3, t 0
          rgb-esperado [0 0 1]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))

  (testing "Comando | (OR): XY|"
    (let [codigo "XY|"
          x 1, y 3, t 0
          rgb-esperado [0 0 3]]
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t)))))
  )