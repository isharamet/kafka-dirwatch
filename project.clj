(defproject kafka-dirwatch "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [me.raynes/fs "1.4.6"]
                 [juxt/dirwatch "0.2.3"]
                 [org.apache.kafka/kafka-clients "0.10.2.0"]]
  :main kafka-dirwatch.core)
