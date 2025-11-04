(ns tp2.mv.operaciones
  "Contiene la implementación pura de cada comando de la Máquina Virtual (MV).
   Cada función 'op-' toma el estado-actual y argumentos (si los necesita),
   y devuelve un mapa de resultado: {:ok nuevo-estado} o {:error ...}.
   Las operaciones que tienen éxito son responsables de actualizar el :idx."
  (:require [tp2.mv.saltos :as saltos]))


(defn- apilar [estado valor]
  "Función ayudante interna. Apila un 'valor' en la pila :ds
   siempre que la pila no esté llena (límite de 8)."
  (if (< (count (:ds estado)) 8)
    {:ok (update (assoc estado :ds (cons valor (:ds estado))) :idx inc)}
    {:error "Pila de datos llena"}))

;; --------------------COMANDOS BASICOS--------------------
(defn op-apilar-x [estado x]
  "Apila la coordenada 'x' en la pila :ds."
  (apilar estado x))

(defn op-apilar-y [estado y]
  "Apila la coordenada 'y' en la pila :ds."
  (apilar estado y))

(defn op-apilar-t [estado t]
  "Apila la coordenada 't' en la pila :ds."
  (apilar estado t))

(defn op-apilar-cero [estado]
  "Comando 'N'. Apila un 0 literal en la pila :ds."
  (apilar estado 0))


;; --------------------MANIPULACION DE PILA--------------------
(defn op-clamp [estado]
  "Comando 'C'. Limita (clampea) el valor del tope de la pila
   para que esté entre 0 y 255."
  (if-not (empty? (:ds estado))
    (let [tope (first (:ds estado))
          resto (rest (:ds estado))
          valor-clampeado (min 255 (max 0 tope))]
      {:ok (update (assoc estado :ds (cons valor-clampeado resto)) :idx inc)})
    {:error "Pila vacia"}))


(defn op-duplicar [estado]
  "Comando 'D'. Duplica el valor del tope de la pila :ds."
  (if-not (empty? (:ds estado))
    (let [tope (first (:ds estado))]
      (apilar estado tope))
    {:error "Pila vacia"}))


(defn op-pop [estado]
  "Comando 'P'. Saca (descarta) el valor del tope de la pila :ds."
  (if-not (empty? (:ds estado))
    {:ok (update (assoc estado :ds (rest (:ds estado))) :idx inc)}
    {:error "Pila vacia"}))


(defn op-swap [estado]
  "Comando 'S'. Intercambia los dos valores del tope de la pila :ds."
  (let [pila (:ds estado)]
    (if (< (count pila) 2)
      {:error "Pila con operandos insuficientes"}
      (let [tope (first pila)
            segundo (second pila)
            resto (drop 2 pila)
            nueva-pila (cons segundo (cons tope resto))]
        {:ok (update (assoc estado :ds nueva-pila) :idx inc)}))))


(defn op-rotar [estado]
  "Comando 'R'. Rota los tres valores del tope de la pila :ds."
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
  "Comandos '0-9'. Modifica el tope de la pila :ds
   calculando (tope * 10) + valor-digito."
  (if (empty? (:ds estado))
    {:error "Pila vacia para operacion de digito"}
    (let [tope-actual (first (:ds estado))
          resto-pila (rest (:ds estado))
          nuevo-valor (+ (* tope-actual 10) valor-digito)
          ]
      {:ok (update (assoc estado :ds (cons nuevo-valor resto-pila)) :idx inc)})))


;; --------------------LOGICA DE ARITMETICA--------------------
(defn- op-binaria [estado f]
  "Ayudante interna para todas las operaciones de 2 operandos (ej. +, -).
   Saca 2 valores (a, b) de la pila, aplica (f a b) y apila el resultado."
  (let [pila (:ds estado)]
    (if (< (count pila) 2)
      {:error "Pila con operandos insuficientes"}
      (let [b (first pila)
            a (second pila)
            resto (drop 2 pila)
            resultado (f a b)
            nueva-pila (cons resultado resto)
            nuevo-estado (assoc estado :ds nueva-pila)]
        {:ok (update nuevo-estado :idx inc)}))))

(defn op-suma [estado]
  "Comando '+'. Llama a op-binaria con la función de suma (+)."
  (op-binaria estado +))

(defn op-resta [estado]
  "Comando '-'. Llama a op-binaria con la función de resta (-)."
  (op-binaria estado -))

(defn op-multi [estado]
  "Comando '*'. Llama a op-binaria con la función de multiplicación (*)."
  (op-binaria estado *))

;; --------------------LOGICA BITWISE--------------------

(defn op-xor [estado]
  "Comando '^'. Llama a op-binaria con la función bit-xor."
  (op-binaria estado bit-xor))

(defn op-and [estado]
  "Comando '&'. Llama a op-binaria con la función bit-and."
  (op-binaria estado bit-and))

(defn op-or [estado]
  "Comando '|'. Llama a op-binaria con la función bit-or."
  (op-binaria estado bit-or))

;; --------------------LOGICA DE COMPARACION--------------------

(defn op-not [estado]
  "Comando '!'. Niega el valor del tope (si es 0 pone 1, si no pone 0)."
  (if-not (empty? (:ds estado))
    (let [tope (first (:ds estado))
          resto (rest (:ds estado))
          resultado (if (zero? tope) 1 0)
          nueva-pila (cons resultado resto)]
      {:ok (update (assoc estado :ds nueva-pila) :idx inc)})
    {:error "Pila vacia"}))

(defn op-igual [estado]
  "Comando '='. Compara los 2 valores del tope, apila 1 si son iguales, 0 si no."
  (op-binaria estado (fn [a b] (if (= a b) 1 0))))

(defn op-menor [estado]
  "Comando '<'. Compara los 2 valores del tope, apila 1 si a < b, 0 si no."
  (op-binaria estado (fn [a b] (if (< a b) 1 0))))

(defn op-mayor [estado]
  "Comando '>'. Compara los 2 valores del tope, apila 1 si a > b, 0 si no."
  (op-binaria estado (fn [a b] (if (> a b) 1 0))))

;; --------------------LOGICA DE M, DIVISION Y MODULO--------------------

(defn op-modo [estado]
  "Comando 'M'. Rota el modo de error :m (0 -> 1 -> 2 -> 0)."
  (let [nuevo-estado (update estado :m #(mod (inc %) 3))]
    {:ok (update nuevo-estado :idx inc)}))

(defn- op-division-modulo [estado f-calc]
  "Ayudante interna para división y módulo. Maneja la división por cero
   basándose en el modo :m."
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
            {:ok (update (assoc estado :ds nueva-pila) :idx inc)})
          )))))

(defn op-division [estado]
  "Comando '/'. Llama a la ayudante con la función 'quot' (división entera)."
  (op-division-modulo estado quot))


(defn op-modulo [estado]
  "Comando '%'. Llama a la ayudante con la función de módulo Euclideano."
  (op-division-modulo estado (fn [a b] (mod (rem a b) (abs b)))))

;; --------------------LOGICA DE CICLOS--------------------

(defn op-inicio-ciclo [estado codigo]
  "Comando '['. Inicia un ciclo o salta al ']' correspondiente.
   Maneja el límite de la pila de ciclos :ls."
  (if (empty? (:ds estado))
    {:error "Pila vacia para '['"}
    (let [a (first (:ds estado))
          resto-ds (rest (:ds estado))
          idx-actual (:idx estado)]
      (if-let [idx-fin (saltos/encontrar-corchete codigo idx-actual)]
        (if (<= a 0)
          (let [nuevo-estado (assoc estado :ds resto-ds :idx (inc idx-fin))]
            {:ok nuevo-estado})
          (if (< (count (:ls estado)) 8)
            (let [idx-inicio-bucle (inc idx-actual)
                  nueva-ls (cons [a idx-inicio-bucle] (:ls estado))
                  nuevo-estado (assoc estado :ds resto-ds :ls nueva-ls)]
              {:ok (update nuevo-estado :idx inc)})
            {:error "Pila de ciclos llena"}))
        {:error "No se encontro ']' correspondiente"}))))

(defn op-fin-ciclo [estado]
  "Comando ']'. Finaliza una iteración del ciclo.
   Repite el ciclo (saltando :idx) o lo termina (avanzando :idx)."
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
          {:ok (update nuevo-estado :idx inc)})))))