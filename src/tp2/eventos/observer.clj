(ns tp2.eventos.observer)

;; Crea el hash de eventos
(defn crear-observer []
  {:observadores (atom {})})

;; Crear observador
(defn suscribir! [observer id f]
  (swap! (:observadores observer) assoc id f))

;; Quitar observador por id
(defn desuscribir! [observer id]
  (swap! (:observadores observer) dissoc id))

;; Notifica un evento a todos los observadores
(defn notificar! [observer evento]
  (doseq [[_ f] @(:observadores observer)]
    (try
      (f evento)
      (catch Exception _e
        nil))))

(defn publicar-tick!   [observer t]
  (notificar! observer {:type :tick :t t}))

(defn publicar-frame! [observer t imagen]
  (notificar! observer {:type :frame-ready :t t :image imagen}))

(defn publicar-error!  [observer t mensaje]
  (notificar! observer {:type :error :t t :message mensaje}))

(defn publicar-estado! [observer ocupado?]
  (notificar! observer {:type :status :busy? ocupado?}))