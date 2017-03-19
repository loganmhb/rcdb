(ns rcdb.core
  (:require [compojure.core :refer [defroutes GET]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :refer [wrap-params]]))

(def db (atom {}))

(defroutes db-routes
  (GET "/get" request
    (let [k (get-in request [:query-params "key"])]
      (println (format "Getting key '%s'" k))
      {:status 200
       :body (get @db k)}))
  (GET "/set" request
    (doseq [[k v] (get request :query-params)]
      (println (format "Setting key '%s' to '%s'" k v))
      (swap! db assoc k v))
    {:status 200 :body "ok"}))

(defn -main []
  (run-jetty (wrap-params db-routes) {:port 4000}))

(comment
  "for repling"
  (def server
    (run-jetty (wrap-params db-routes) {:port 4004
                                        :join? false})))
