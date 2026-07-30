(ns tp2.eventos.scheduler
  (:require [tp2.eventos.observer :as obs]
            [tp2.runtime.render :as render]))

(defrecord Scheduler [sujeto intervalo-ms corriendo? codigo-actual version])

(defn crear-scheduler
  "Crea un Scheduler con un intervalo (ms) entre cuadros."
  ([sujeto intervalo-ms]
   (->Scheduler sujeto intervalo-ms
                (atom false) ;
                (atom "")
                (atom 0))))

(defn renderizador
  "Renderiza los cuadros de la VM"
  [^Scheduler plan t ver codigo-snapshot]
  (when (= ver @(:version plan))
    (obs/publicar-estado! (:sujeto plan) true))
  (try
    (let [img (render/generar-cuadro codigo-snapshot t)]
      (when (= ver @(:version plan))
        (obs/publicar-frame! (:sujeto plan) t img)
        (obs/publicar-tick!  (:sujeto plan) t)))
    (catch Exception e
      (when (= ver @(:version plan))
        (obs/publicar-error! (:sujeto plan) t (.getMessage e))))
    (finally
      (when (= ver @(:version plan))
        (obs/publicar-estado! (:sujeto plan) false)))))

(defn iniciar!
  "Comienza el loop de animación."
  [^Scheduler plan]
  (when (compare-and-set! (:corriendo? plan) false true)
    (future
      (let [ver @(:version plan)]
        (loop [t 0]
          (when @(:corriendo? plan)
            (let [inicio-ns       (System/nanoTime)
                  codigo-snapshot @(:codigo-actual plan)]
              (renderizador plan t ver codigo-snapshot)
              (let [ms (/ (double (- (System/nanoTime) inicio-ns)) 1e6)
                    restante   (long (max 0 (- (:intervalo-ms plan) (Math/ceil ms))))]
                (when (pos? restante)
                  (Thread/sleep restante))))
            (recur (mod (inc t) 256))))))))

(defn detener!
  "Detiene el loop de animación."
  [^Scheduler plan]
  (reset! (:corriendo? plan) false))

(defn establecer-scheduler!
  "Actualiza el código, incrementa versión y reinicia si estaba corriendo."
  [^Scheduler plan codigo]
  (reset! (:codigo-actual plan) (or codigo ""))
  (swap!  (:version plan) inc)
  (obs/notificar! (:sujeto plan) {:type :code-changed :code @(:codigo-actual plan)})
  (when @(:corriendo? plan)
    (detener! plan)
    (iniciar!  plan)))
