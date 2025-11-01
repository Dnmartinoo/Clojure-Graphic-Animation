(ns tp2.mv.operaciones)

;; --------------------COMANDOS BASICOS--------------------
(defn op-apilar-x [estado x]
  {:ok (assoc estado :ds (cons x (:ds estado)))})

(defn op-apilar-y [estado y]
  {:ok (assoc estado :ds (cons y (:ds estado)))})

(defn op-apilar-t [estado t]
  {:ok (assoc estado :ds (cons t (:ds estado)))})

(defn op-apilar-cero [estado]
  {:ok (assoc estado :ds (cons 0 (:ds estado)))})


;; --------------------MANIPULACION DE PILA--------------------
(defn op-clamp [estado]
  (if-not (empty? (:ds estado))
    (let [tope (first (:ds estado))
          resto (rest (:ds estado))
          valor-clampeado (min 255 (max 0 tope))]
      {:ok (assoc estado :ds (cons valor-clampeado resto))})
    {:error "Pila vacia"}))


(defn op-duplicar [estado]
  (if-not (empty? (:ds estado))
    (let [tope (first (:ds estado))]
      {:ok (assoc estado :ds (cons tope (:ds estado)))})
    {:error "Pila vacia"}))


(defn op-pop [estado]
  (if-not (empty? (:ds estado))
    {:ok (assoc estado :ds (rest (:ds estado)))}
    {:error "Pila vacia"}))


(defn op-swap [estado]
  (let [pila (:ds estado)]
    (if (< (count pila) 2)
      {:error "Pila con operandos insuficientes"}
      (let [tope (first pila)
            segundo (second pila)
            resto (drop 2 pila)
            nueva-pila (cons segundo (cons tope resto))]
        {:ok (assoc estado :ds nueva-pila)}))))


(defn op-rotar [estado]
  (let [pila (:ds estado)]
    (if (< (count pila) 3)
      {:error "Pila con operandos insuficientes"}
      (let [c (first pila)
            b (second pila)
            a (nth pila 2)
            resto (drop 3 pila)
            nueva-pila (cons a (cons c (cons b resto)))]
        {:ok (assoc estado :ds nueva-pila)}))))

;; --------------------LOGICA DE DIGITOS--------------------
(defn op-digito [estado valor-digito]
  (if (empty? (:ds estado))
    {:error "Pila vacia para operacion de digito"}
    (let [tope-actual (first (:ds estado))
          resto-pila (rest (:ds estado))
          nuevo-valor (+ (* tope-actual 10) valor-digito)
          ]
      {:ok (assoc estado :ds (cons nuevo-valor resto-pila))})))


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
        {:ok (assoc estado :ds nueva-pila)}))))

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
      {:ok (assoc estado :ds nueva-pila)})
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
  {:ok nuevo-estado}))

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
            {:ok (assoc estado :ds nueva-pila)})
          )))))

(defn op-division [estado]
  (op-division-modulo estado quot))


(defn op-modulo [estado]
  (op-division-modulo estado (fn [a b] (mod (rem a b) (abs b)))))

