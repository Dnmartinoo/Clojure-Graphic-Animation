(ns tp2_anim.eventos.observer)

;; Crea el hash de eventos
(defn crear-sujeto []
  {:observadores (atom {})})

;; Crear observador
(defn suscribir! [sujeto id f]
  (swap! (:observadores sujeto) assoc id f))

;; Quitar observador por id
(defn desuscribir! [sujeto id]
  (swap! (:observadores sujeto) dissoc id))

;; Notifica un evento a todos los observadores
(defn notificar! [sujeto evento]
  (doseq [[_ f] @(:observadores sujeto)]
    (try
      (f evento)
      (catch Exception _e
        nil))))

(defn publicar-tick!   [sujeto t]
  (notificar! sujeto {:type :tick :t t}))

(defn publicar-cuadro! [sujeto t imagen]
  (notificar! sujeto {:type :frame-ready :t t :image imagen}))

(defn publicar-error!  [sujeto t mensaje]
  (notificar! sujeto {:type :error :t t :message mensaje}))

(defn publicar-estado! [sujeto ocupado?]
  (notificar! sujeto {:type :status :busy? ocupado?}))
