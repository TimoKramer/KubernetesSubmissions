(ns main
  (:require
    [clojure.tools.logging :as log]
    [org.httpkit.server :as server]))

(def opts
  {:port (Long/valueOf (or (System/getenv "PORT") 8090))})

(def counter (atom 0))

(defn app [req]
  (let [response {:status 200
                  :headers {"Content-Type" "text/plain"}
                  :body (str "pong " @counter)}]
    (swap! counter inc)
    response))

(server/run-server app opts)

(log/info (str "Server started in port " (:port opts)))
