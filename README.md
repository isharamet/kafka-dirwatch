# kafka-dirwatch

A Clojure library designed to watch directory for new files and sending their contents to Kafka.

## Usage

Specify directory to watch, kafka broker URLs and topic in `conf.edn`

```clojure
{:dir  "/directory/to/watch"
 :initial-load true
 :kafka {:bootstrap.servers "host1:9092,host2:9092"
         :topic "dirwatch-topic"}}
```
and run with Leiningen

```bash
lein run ./resources/conf.edn
```
or as a standalone JAR

```bash
java -jar kafka-dirwatch.jar ./conf.edn
```
## License

Copyright © 2017 Ivan Sharamet

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
