(ns kafka-dirwatch.core
  (:require [kafka-dirwatch.kafka-producer :as kafka]
            [clojure.edn :as edn]
            [juxt.dirwatch :as dirwatch]
            [me.raynes.fs :as fs])
  (:gen-class))

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
        kafka-conf (conf :kafka)
        kafkap (kafka/producer kafka-conf)]
    (do
      (.addShutdownHook (Runtime/getRuntime) (Thread. (fn [] (.close kafkap))))
      (dirwatch/watch-dir
        (dirwatch-fn kafkap (kafka-conf :topic))
        (fs/file (conf :dir))))))
