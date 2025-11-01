(ns tp2_anim.interfaz.ventana
  (:require [seesaw.core :as ui]
            [tp2_anim.eventos.observer :as obs]
            [tp2_anim.eventos.scheduler :as sched])
  (:import [javax.swing ImageIcon SwingUtilities]
           [java.awt Image]))

;; -------------------------
;; Helpers de UI
;; -------------------------

(defn preferido
  [c w h]
  (doto c (ui/config! :preferred-size [w :by h])))

(defn imagen->icono ^ImageIcon [^Image img]
  (ImageIcon. img))

(defn escalar-imagen
  ^Image [^Image img ^long w ^long h]
  (.getScaledInstance img w h Image/SCALE_SMOOTH))

;; Pospuesto (debounce) simple para el TextField
(def ^:private tarea-pospuesta (atom nil))

(defn- posponer!
  [ms f]
  (when-let [t @tarea-pospuesta] (future-cancel t))
  (reset! tarea-pospuesta
          (future
            (Thread/sleep ms)
            (f))))


(defn iniciar!
  [sujeto]
  (let [ui-estado (atom {:t 0 :error nil :ocupado? false :ultima-img nil})
        plan      (sched/crear-scheduler sujeto 100)

        tf-codigo (ui/text :columns 56 :text "")
        lbl-t     (ui/label :text "t = 0")
        lbl-err   (ui/label :text "" :foreground :red)
        img-lbl   (preferido (ui/label :icon nil) 512 512)
        btn-play  (ui/button :text "Pausa")

        frame     (ui/frame
                    :title "TP2 - Animación"
                    :on-close :exit
                    :resizable? false
                    :content (ui/border-panel
                               :north  (ui/vertical-panel :items [tf-codigo lbl-t lbl-err])
                               :center (ui/flow-panel :items [img-lbl])
                               :south  (ui/flow-panel  :items [btn-play]))
                    :minimum-size [560 :by 640])]

    (letfn [(al-evento [ev]
              (case (:type ev)
                :tick
                (do
                  (swap! ui-estado assoc :t (:t ev))
                  (ui/config! lbl-t :text (str "t = " (:t ev))))

                :frame-ready
                (let [base   (:image ev)
                      esc    (some-> base (escalar-imagen 512 512))
                      icono  (some-> esc imagen->icono)]
                  (swap! ui-estado assoc :ultima-img base :t (:t ev))
                  (ui/config! img-lbl :icon icono)
                  (ui/config! img-lbl :preferred-size [512 :by 512]))

                :error
                (do
                  (swap! ui-estado assoc :error (:message ev))
                  (ui/config! lbl-err :text (str "Error: " (:message ev)))
                  (when (.get (:corriendo? plan))
                    (sched/detener! plan)
                    (ui/config! btn-play :text "Play")))

                :status
                (do
                  (swap! ui-estado assoc :ocupado? (:busy? ev))
                  (ui/config! btn-play :enabled? (not (:busy? ev))))

                nil))]

      (obs/suscribir! sujeto :ui
                      (fn [ev]
                        (if (SwingUtilities/isEventDispatchThread)
                          (al-evento ev)
                          (SwingUtilities/invokeLater #(al-evento ev))))))

    (ui/listen tf-codigo :key-released
               (fn [_]
                 (posponer! 300 #(sched/establecer-scheduler! plan (ui/text tf-codigo)))))

    (ui/listen btn-play :action
               (fn [_]
                 (if (.get (:corriendo? plan))
                   (do (sched/detener! plan) (ui/config! btn-play :text "Play"))
                   (do (sched/iniciar!  plan) (ui/config! btn-play :text "Pausa")))))

    (ui/show! frame)
    (sched/iniciar! plan)
    {:frame frame :scheduler plan}))
