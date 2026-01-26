(ns log-output.consumer
  (:require
   [clojure.tools.logging :as log]
   [org.httpkit.server :as server])
  (:import
   [java.util.concurrent Executors]))

(def file-path "/usr/src/app/files/uuid.txt")

(comment
  (def file-path "/tmp/log-output/uuid.txt"))

(def opts
  {:port (Long/valueOf (or (System/getenv "PORT") 8090))
   :worker-pool (Executors/newVirtualThreadPerTaskExecutor)})

(defn read-log-output [_]
  (try
    (log/info "Loading")
    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body (slurp file-path)}
    (catch Exception e
      (let [response (str "Error: " (ex-message e))]
        (log/error response)
        {:status 500
         :headers {"Content-Type" "text/plain"}
         :body response}))))

(defn app [req]
  (read-log-output req))

(def server (server/run-server app opts))

(log/info (str "log-output consumer started on port " (:port opts)))
