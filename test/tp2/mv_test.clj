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
      (is (= rgb-esperado (mv/evaluar-pixel codigo x y t))))))