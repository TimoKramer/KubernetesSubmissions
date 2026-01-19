(ns main
  (:require
    [org.httpkit.server :as server]
    [clojure.tools.logging :as log]
    [hiccup2.core :as h]))

(defn generate-hash []
  (subs (str (random-uuid)) 0 6))

(defonce app-hash (generate-hash))

(def opts
  {:port (Long/valueOf (or (System/getenv "PORT") 8090))})

(defn app [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str (h/html [:h3 "Application " app-hash]
                      [:h3 "Request " (generate-hash)]))})

(server/run-server app opts)

(log/info (str "Server started in port " (:port opts)))
