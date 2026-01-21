(ns log-output.main
  (:require
   [clojure.tools.logging :as log]
   [hiccup2.core :as h]
   [org.httpkit.server :as server])
  (:import
   [java.util Date]))

(defonce uuid (str (random-uuid)))

(log/info uuid)

(future
  (while true
    (do
      (Thread/sleep 5000)
      (log/info uuid))))

(def opts
  {:port (Long/valueOf (or (System/getenv "PORT") 8090))})

(defn app [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str (h/html [:h3 "Timestamp: " (str (Date.))]
                      [:h3 "UUID: " uuid]))})

(server/run-server app opts)

(log/info (str "Server started in port " (:port opts)))

(comment
  (str (java.util.Date.)))
