(ns kafka-dirwatch.kafka-producer
  (:refer-clojure :exclude [send])
  (:import (java.util Properties)
           (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)))

(def default-properties
  {:acks "all"
   :retries 0
   :batch.size 16384
   :linger.ms 1
   :buffer.memory 33554432
   :key.serializer "org.apache.kafka.common.serialization.StringSerializer"
   :value.serializer "org.apache.kafka.common.serialization.StringSerializer"})

(defn create-properties
  [conf]
  (reduce
    (fn [ps [k v]]
      (do
        (.put ps (name k) (str v))
        ps))
    (Properties.)
    (merge default-properties conf)))

(defn producer
  [conf]
  (KafkaProducer. (create-properties conf)))

(defn send
  [p t k v]
  (.send p (ProducerRecord. t k v)))