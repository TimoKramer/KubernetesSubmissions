(ns main
  (:require
   [babashka.fs :as fs]
   [clojure.java.io :as io]
   [clojure.tools.logging :as log]
   [org.httpkit.server :as server])
  (:import
   [java.io IOException]
   [java.util.concurrent Executors]))

(comment
  (def directory "/tmp/image-finder"))

(def directory "/usr/src/app/files")
(def file-path (str directory "/image.jpg"))

(def opts
  {:port (Long/valueOf (or (System/getenv "PORT") 8090))
   :worker-pool (Executors/newVirtualThreadPerTaskExecutor)})

(defn serve-file [_req]
  (log/info (str (fs/list-dir directory)))
  (try
    (->> (fs/read-all-bytes file-path)
         (io/input-stream)
         (hash-map :status 200
                   :headers {"Content-Type" "image/jpeg"}
                   :body))
    (catch IOException e
      {:status 500
       :headers {"Content-Type" "text/plain"}
       :body (str "Error reading file: " (ex-message e))})))

(defn app [req]
  (serve-file req))

(def server (server/run-server app opts))

(log/info (str "Image-Response started in port " (:port opts)))

(comment
  (server))
