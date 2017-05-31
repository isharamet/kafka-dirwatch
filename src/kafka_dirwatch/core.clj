(ns kafka-dirwatch.core
  (:require [kafka-dirwatch.kafka-producer :as kafka]
            [clojure.edn :as edn]
            [juxt.dirwatch :as dirwatch]
            [me.raynes.fs :as fs])
  (:gen-class))

(defn walk-dir
  [f dir]
  (doall
    (fs/walk
      (fn [root dirs files]
        (let [root (str (.getPath root) (java.io.File/separator))]
          (doseq [file files]
            (f (str root file)))))
      dir)))

(defn dirwatch-fn
  [kafkap topic]
  (fn [e]
    (let [path (.getPath(e :file))]
      (if (and (= (e :action) :modify)
               (fs/exists? path))
        (do
          (println (str "Sending '" path "' content to '" topic "' Kafka topic"))
          (kafka/send kafkap topic path (slurp path)))))))

(defn -main
  [conf-file & args]
  (let [conf (edn/read-string (slurp conf-file))
        dir (conf :dir)
        kafka-conf (conf :kafka)
        kafka-topic (kafka-conf :topic)
        kafkap (kafka/producer kafka-conf)]
    (do
      (.addShutdownHook
        (Runtime/getRuntime)
        (Thread. (fn [] (.close kafkap))))
      (if (conf :initial-load)
        (walk-dir
          (fn [file]
            (kafka/send kafkap kafka-topic file (slurp file)))
          dir))
      (dirwatch/watch-dir
        (dirwatch-fn kafkap kafka-topic)
        (fs/file dir)))))
