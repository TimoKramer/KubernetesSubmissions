(ns log-output.producer
  (:require
   [babashka.fs :as fs]
   [clojure.tools.logging :as log])
  (:import
   [java.nio.file FileAlreadyExistsException]))

(comment
  (def path "/tmp/log-output/"))

(def path "/usr/src/app/files/")
(def file-path (str path "uuid.txt"))
(defonce uuid (str (random-uuid)))


(do
  (try
    (fs/create-dir path)
    (fs/create-file file-path)
    (catch FileAlreadyExistsException _))
  (log/info (str (mapv fs/file-name (fs/list-dir path))))
  (spit file-path (str (java.time.LocalDateTime/now) " " uuid "\n") :append true)
  (while true
    (Thread/sleep 5000)
    (log/info (str (java.time.LocalDateTime/now) " " uuid "\n"))
    (spit file-path (str (java.time.LocalDateTime/now) " " uuid "\n") :append true)))

(log/info "log output producer service started")

(comment
  (str (java.util.Date.)))
