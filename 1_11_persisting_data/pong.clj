(ns pong
  (:require
   [babashka.fs :as fs]
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [org.httpkit.server :as server]) 
  (:import
   [java.util.concurrent Executors]))

(def path "/usr/src/app/files/")
(def file "uuid.txt")
(def opts
  {:port (Long/valueOf (or (System/getenv "PORT") 8090))
   :worker-pool (Executors/newVirtualThreadPerTaskExecutor)})
(def counter (atom 0))

(comment
  (def path "/tmp/log-output/")
  (def file "uuid.txt"))

(defn app [_req]
  (let [response {:status 200
                  :headers {"Content-Type" "text/plain"}
                  :body (str "pong " @counter)}]
    (swap! counter inc)
    (Thread/startVirtualThread #(spit (str path file) @counter))
    response))

(try (fs/create-dirs path)
     (fs/create-file (str path file))
     (some->> (parse-long (first (str/split-lines (slurp (str path file)))))
              (reset! counter))
     (catch Exception e
       (log/error (ex-message e))))
(def server (server/run-server app opts))

(log/info (str "Pong server started in port " (:port opts)))

(comment
  (server))
