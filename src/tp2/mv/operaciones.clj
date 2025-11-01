(ns tp2.mv.operaciones)

;; --------------------COMANDOS BASICOS--------------------
(defn op-apilar-x [estado x]
  (assoc estado :ds (cons x (:ds estado))))

(defn op-apilar-y [estado y]
  (assoc estado :ds (cons y (:ds estado))))

(defn op-apilar-t [estado t]
  (assoc estado :ds (cons t (:ds estado))))

(defn op-apilar-cero [estado]
  (assoc estado :ds (cons 0 (:ds estado))))


;; --------------------MANIPULACION DE PILA--------------------
(defn op-clamp [estado]
  (if-not (empty? (:ds estado))
    (let [tope (first (:ds estado))
          resto (rest (:ds estado))
          valor-clampeado (min 255 (max 0 tope))]
      (assoc estado :ds (cons valor-clampeado resto)))
    estado))


(defn op-duplicar [estado]
  (if-not (empty? (:ds estado))
    (let [tope (first (:ds estado))]
      (assoc estado :ds (cons tope (:ds estado))))
    estado))


(defn op-pop [estado]
  (if-not (empty? (:ds estado))
    (assoc estado :ds (rest (:ds estado)))
    estado))


(defn op-swap [estado]
  (let [pila (:ds estado)]
    (if (< (count pila) 2)
      estado
      (let [tope (first pila)
            segundo (second pila)
            resto (drop 2 pila)
            nueva-pila (cons segundo (cons tope resto))]
        (assoc estado :ds nueva-pila)))))


(defn op-rotar [estado]
  (let [pila (:ds estado)]
    (if (< (count pila) 3)
      estado
      (let [c (first pila)
            b (second pila)
            a (nth pila 2)
            resto (drop 3 pila)
            nueva-pila (cons a (cons c (cons b resto)))]
        (assoc estado :ds nueva-pila)))))

;; --------------------LOGICA DE DIGITOS--------------------
(defn op-digito [estado valor-digito]
  (if (empty? (:ds estado))
    estado
    (let [tope-actual (first (:ds estado))
          resto-pila (rest (:ds estado))
          nuevo-valor (+ (* tope-actual 10) valor-digito)
          ]
      (assoc estado :ds (cons nuevo-valor resto-pila)))))


;; --------------------LOGICA DE ARITMETICA--------------------

(defn- op-binaria [estado f]
  (let [pila (:ds estado)]
    (if (< (count pila) 2)
      estado

      (let [b (first pila)
            a (second pila)
            resto (drop 2 pila)

            resultado (f a b)

            nueva-pila (cons resultado resto)]
        (assoc estado :ds nueva-pila)))))

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