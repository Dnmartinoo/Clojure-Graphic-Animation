(ns tp2.mv.operaciones
  (:require [tp2.mv.saltos :as saltos]))

;; --------------------COMANDOS BASICOS--------------------
(defn op-apilar-x [estado x]
  {:ok (update (assoc estado :ds (cons x (:ds estado))) :idx inc)})

(defn op-apilar-y [estado y]
  {:ok (update (assoc estado :ds (cons y (:ds estado))) :idx inc)})

(defn op-apilar-t [estado t]
  {:ok (update (assoc estado :ds (cons t (:ds estado))) :idx inc)})

(defn op-apilar-cero [estado]
  {:ok (update (assoc estado :ds (cons 0 (:ds estado))) :idx inc)})


;; --------------------MANIPULACION DE PILA--------------------
(defn op-clamp [estado]
  (if-not (empty? (:ds estado))
    (let [tope (first (:ds estado))
          resto (rest (:ds estado))
          valor-clampeado (min 255 (max 0 tope))]
      {:ok (update (assoc estado :ds (cons valor-clampeado resto)) :idx inc)})
    {:error "Pila vacia"}))


(defn op-duplicar [estado]
  (if-not (empty? (:ds estado))
    (let [tope (first (:ds estado))]
      {:ok (update (assoc estado :ds (cons tope (:ds estado))) :idx inc)})
    {:error "Pila vacia"}))


(defn op-pop [estado]
  (if-not (empty? (:ds estado))
    {:ok (update (assoc estado :ds (rest (:ds estado))) :idx inc)}
    {:error "Pila vacia"}))


(defn op-swap [estado]
  (let [pila (:ds estado)]
    (if (< (count pila) 2)
      {:error "Pila con operandos insuficientes"}
      (let [tope (first pila)
            segundo (second pila)
            resto (drop 2 pila)
            nueva-pila (cons segundo (cons tope resto))]
        {:ok (update (assoc estado :ds nueva-pila) :idx inc)}))))


(defn op-rotar [estado]
  (let [pila (:ds estado)]
    (if (< (count pila) 3)
      {:error "Pila con operandos insuficientes"}
      (let [c (first pila)
            b (second pila)
            a (nth pila 2)
            resto (drop 3 pila)
            nueva-pila (cons a (cons c (cons b resto)))]
        {:ok (update (assoc estado :ds nueva-pila) :idx inc)}))))

;; --------------------LOGICA DE DIGITOS--------------------
(defn op-digito [estado valor-digito]
  (if (empty? (:ds estado))
    {:error "Pila vacia para operacion de digito"}
    (let [tope-actual (first (:ds estado))
          resto-pila (rest (:ds estado))
          nuevo-valor (+ (* tope-actual 10) valor-digito)
          ]
      {:ok (update (assoc estado :ds (cons nuevo-valor resto-pila)) :idx inc)})))


;; --------------------LOGICA DE ARITMETICA--------------------
(defn- op-binaria [estado f]
  (let [pila (:ds estado)]
    (if (< (count pila) 2)
      {:error "Pila con operandos insuficientes"}

      (let [b (first pila)
            a (second pila)
            resto (drop 2 pila)
            resultado (f a b)
            nueva-pila (cons resultado resto)]
        {:ok (update (assoc estado :ds nueva-pila) :idx inc)})))) ; <--- CAMBIO CLAVE

(defn op-suma [estado]
  (op-binaria estado +))

(defn op-resta [estado]
  (op-binaria estado -))

(defn op-multi [estado]
  (op-binaria estado *))

;; --------------------LOGICA BITWISE--------------------

(defn op-xor [estado]
  (op-binaria estado bit-xor))

(defn op-and [estado]
  (op-binaria estado bit-and))

(defn op-or [estado]
  (op-binaria estado bit-or))

;; --------------------LOGICA DE COMPARACION--------------------

(defn op-not [estado]
  (if-not (empty? (:ds estado))
    (let [tope (first (:ds estado))
          resto (rest (:ds estado))
          resultado (if (zero? tope) 1 0)
          nueva-pila (cons resultado resto)]
      {:ok (update (assoc estado :ds nueva-pila) :idx inc)})
    {:error "Pila vacia"}))

(defn op-igual [estado]
  (op-binaria estado (fn [a b] (if (= a b) 1 0))))

(defn op-menor [estado]
  (op-binaria estado (fn [a b] (if (< a b) 1 0))))

(defn op-mayor [estado]
  (op-binaria estado (fn [a b] (if (> a b) 1 0))))

;; --------------------LOGICA DE M, DIVISION Y MODULO--------------------

(defn op-modo [estado]
  (let [nuevo-estado (update estado :m #(mod (inc %) 3))]
    {:ok (update nuevo-estado :idx inc)})) ; <--- CAMBIO

(defn- op-division-modulo [estado f-calc]
  (let [pila (:ds estado)]
    (if (< (count pila) 2)
      {:error "Pila con operandos insuficientes"}

      (let [b (first pila)
            a (second pila)
            resto (drop 2 pila)
            m (:m estado)]
        (if (zero? b)
          (case m
            0 {:error "Division por cero"}
            1 {:ok-final [0 0 0]}
            2 {:ok-final [255 0 0]}
            )
          (let [resultado (f-calc a b)
                nueva-pila (cons resultado resto)]
            {:ok (update (assoc estado :ds nueva-pila) :idx inc)}) ; <--- CAMBIO
          )))))

(defn op-division [estado]
  (op-division-modulo estado quot))


(defn op-modulo [estado]
  (op-division-modulo estado (fn [a b] (mod (rem a b) (abs b)))))

;; --------------------LOGICA DE CICLOS--------------------

(defn op-inicio-ciclo [estado codigo]
  (if (empty? (:ds estado))
    {:error "Pila vacia para '['"}
    (let [a (first (:ds estado))
          resto-ds (rest (:ds estado))
          idx-actual (:idx estado)]

      (if-let [idx-fin (saltos/encontrar-corchete codigo idx-actual)]
        (if (<= a 0)
          (let [nuevo-estado (assoc estado :ds resto-ds :idx (inc idx-fin))]
            {:ok nuevo-estado})
          (let [idx-inicio-bucle (inc idx-actual)
                nueva-ls (cons [a idx-inicio-bucle] (:ls estado))
                nuevo-estado (assoc estado :ds resto-ds :ls nueva-ls)]
            {:ok (update nuevo-estado :idx inc)}))
        {:error "No se encontro ']' correspondiente"}
        ))))

(defn op-fin-ciclo [estado]
  (if (empty? (:ls estado))
    {:error "Comando ']' sin un '[' correspondiente"}
    (let [[contador idx-inicio-bucle] (first (:ls estado))
          resto-ls (rest (:ls estado))
          contador-nuevo (dec contador)]
      (if (> contador-nuevo 0)
        (let [nueva-ls (cons [contador-nuevo idx-inicio-bucle] resto-ls)
              nuevo-estado (assoc estado :ls nueva-ls)]
          {:ok (assoc nuevo-estado :idx idx-inicio-bucle)})
        (let [nuevo-estado (assoc estado :ls resto-ls)]
          {:ok (update nuevo-estado :idx inc)})
        ))))


