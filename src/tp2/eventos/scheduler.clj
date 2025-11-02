(ns tp2.eventos.scheduler
  (:require [tp2.eventos.observer :as obs]
            [tp2.eventos.dibujar_render :as mock])
  (:import [java.util.concurrent Executors]
           [java.util.concurrent.atomic AtomicBoolean]))

;; Pool de hilos para renderizar sin bloquear la UI
(defonce ^:private EJECUTOR
         (Executors/newFixedThreadPool
           (.availableProcessors (Runtime/getRuntime))))

;; Estado del scheduler de animación
(defrecord Scheduler [sujeto intervalo-ms corriendo? codigo-actual])

(defn crear-scheduler
  "Crea un Scheduler con un intervalo (ms) entre cuadros. Por defecto ~10 FPS (100 ms)."
  ([sujeto] (crear-scheduler sujeto 100))
  ([sujeto intervalo-ms]
   (->Scheduler sujeto intervalo-ms (AtomicBoolean. false) (atom ""))))

(defn establecer-scheduler!
  "Actualiza el código fuente a usar por el render. Notifica el cambio."
  [^Scheduler plan codigo]
  (reset! (:codigo-actual plan) (or codigo ""))
  (obs/notificar! (:sujeto plan) {:type :code-changed :code @(:codigo-actual plan)}))

(defn- tarea-render-cuadro
  "Devuelve una Runnable que renderiza el cuadro t y publica eventos."
  [^Scheduler plan t]
  (fn []
    (try
      (obs/publicar-estado! (:sujeto plan) true)
      ;; TODO: reemplazar mock/imagen-degradado por el render real cuando esté listo
      (let [img (mock/imagen-degradado t)]
        (obs/publicar-frame! (:sujeto plan) t img)
        (obs/publicar-tick!  (:sujeto plan) t))
      (catch Exception e
        (obs/publicar-error! (:sujeto plan) t (.getMessage e)))
      (finally
        (obs/publicar-estado! (:sujeto plan) false)))))

(defn iniciar!
  "Comienza el loop de animación en background. No bloquea la UI."
  [^Scheduler plan]
  (when (.compareAndSet (:corriendo? plan) false true)
    (future
      (loop [t 0]
        (when (.get (:corriendo? plan))
          (.submit EJECUTOR ^Runnable (tarea-render-cuadro plan t))
          (Thread/sleep (:intervalo-ms plan))
          (recur (mod (inc t) 256)))))))

(defn detener!
  "Detiene el loop de animación."
  [^Scheduler plan]
  (.set (:corriendo? plan) false))
