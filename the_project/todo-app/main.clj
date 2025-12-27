(ns main
  (:require
    [babashka.deps :as deps]
    [org.httpkit.server :as server]
    [clojure.tools.logging :as log]
    [babashka.cli :as cli]))

(def opts
  {:port (Long/valueOf (or (System/getenv "PORT") 8090))})

(defn app [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World"})

(server/run-server app opts)

(log/info (str "Server started in port " (:port opts)))
