(ns kafka-dirwatch.core
  (:require [clojure.edn :as edn]
            [kafka-dirwatch.kafka-producer :as kafka]
            [juxt.dirwatch :as dirwatch])
  (:gen-class))

(defn -main
  [conf-file & args]
  (let [conf (edn/read-string (slurp conf-file))
        kafka-conf (conf :kafka)
        kafkap (kafka/producer kafka-conf)]
    (do
      (.addShutdownHook (Runtime/getRuntime) (Thread. (fn [] (.close kafkap))))
      (dirwatch/watch-dir
        (fn [e]
          (kafka/send
            kafkap
            (kafka-conf :topic)
            ""
            (.getPath (e :file))))
        (clojure.java.io/file (conf :dir))))))
