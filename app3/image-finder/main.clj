(ns main
  (:require
   [babashka.fs :as fs]
   [babashka.http-client :as http]
   [clojure.java.io :as io]
   [clojure.tools.logging :as log]
   [org.httpkit.server :as server])
  (:import
   [java.util.concurrent Executors]))

(comment
  (def directory "/tmp/image-finder"))

(def directory "/usr/src/app/files")
(def file-path (str directory "/image.jpg"))

(def opts
  {:port (Long/valueOf (or (System/getenv "PORT") 8091))
   :worker-pool (Executors/newVirtualThreadPerTaskExecutor)})

(defn find-a-file [_]
  (try
    (log/info "Deleting")
    (fs/delete file-path)
    (catch Exception _))
  (try
    (log/info "Loading")
    (io/copy
      (:body (http/get "https://picsum.photos/200" {:as :stream}))
      (io/file file-path))
    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body "New image downloaded"}
    (catch Exception e
      (let [response (str "Error: " (ex-message e))]
        (log/error response)
        {:status 500
         :headers {"Content-Type" "text/plain"}
         :body response}))))

(defn app [req]
  (find-a-file req))

(def server (server/run-server app opts))

(log/info (str "Image-Finder started in port " (:port opts)))

(comment
  (server))
