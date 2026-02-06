(ns main
  (:require
   [babashka.fs :as fs]
   [babashka.http-client :as http]
   [clojure.java.io :as io]
   [clojure.tools.logging :as log]
   [org.httpkit.server :as server])
  (:import
   [java.util.concurrent Executors]))

(comment)
(def directory "/tmp/image-finder")

#_(def directory "/usr/src/app/files")
(def image-file-path (str directory "/image.jpg"))
(def timestamp-file-path (str directory "/timestamp.edn"))

(defn get-timestamp []
  (try
    (-> (slurp timestamp-file-path)
        parse-long)
    (catch java.io.FileNotFoundException _
      (log/info "Creating new timestamp file")
      (fs/create-dirs directory)
      (fs/create-file timestamp-file-path)
      (let [timestamp (System/nanoTime)]
        (spit timestamp-file-path timestamp)
        timestamp))))

(defn reset-timestamp [new-timestamp]
  (spit timestamp-file-path new-timestamp))

(def opts
  {:port (Long/valueOf (or (System/getenv "PORT") 8091))
   :worker-pool (Executors/newVirtualThreadPerTaskExecutor)})

(defn reload-image? [req-time timestamp]
  (let [diff-time (and timestamp (> (- req-time timestamp) 600000000000))]
    (if diff-time
       true
       false)))

(defn find-a-file [{req-time :start-time}]
  (if (reload-image? req-time (get-timestamp))
    (try
      (log/info "Deleting")
      (fs/delete image-file-path)
      (log/info "Loading")
      (io/copy
        (:body (http/get "https://picsum.photos/1200" {:as :stream}))
        (io/file image-file-path))
      (reset-timestamp (System/nanoTime))
      {:status 200
       :headers {"Content-Type" "text/plain"}
       :body "New image downloaded"}
      (catch Exception e
        (let [response (str "Error: " (ex-message e))]
          (log/error response)
          {:status 500
           :headers {"Content-Type" "text/plain"}
           :body response})))
    {:status 200
      :headers {"Content-Type" "text/plain"}
      :body "No new image downloaded"}))

(defn app [req]
  (find-a-file req))

(def server (server/run-server app opts))

(log/info (str "Image-Finder started in port " (:port opts)))

(comment
  (server)
  (reload-image? 25844571756849 (- 25844571756849 600000000001)))
