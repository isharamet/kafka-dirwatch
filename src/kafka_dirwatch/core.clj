(ns kafka-dirwatch.core
  (:require [clojure.edn :as edn]
            [kafka-dirwatch.kafka-producer :as kafka])
  (:gen-class))

(defn -main
  [conf-file & args]
  (let [conf (edn/read-string (slurp conf-file))
        kafkap (kafka/producer (conf :kafka))]
    (do
      (kafka/send kafkap "test" "" "Hello, World!")
      (.close kafkap))))
