#!/usr/bin/env bb

(require '[clojure.tools.logging :as log])

(defonce uuid (random-uuid))

(log/info (str uuid))

(while true
  (do
    (Thread/sleep 5000)
    (log/info (str uuid))))
