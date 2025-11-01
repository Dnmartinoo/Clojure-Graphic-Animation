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