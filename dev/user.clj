(ns user
  (:require
   [clojure.java.io :as io]
   [clojure.java.javadoc :refer [javadoc]]
   [clojure.pprint :refer [pprint]]
   [clojure.reflect :refer [reflect]]
   [clojure.repl :refer [apropos dir doc find-doc pst source]]
   [clojure.set :as set]
   [clojure.string :as str]
   [clojure.test :as test]
   [com.stuartsierra.component :as component]
   [clojure.tools.namespace.repl :refer [refresh refresh-all]]
   [catalysis.config :as config]
   [catalysis.system :as system]))

(def system nil)

(defn init
  ([config-overrides]
   (alter-var-root #'system (fn [_] (system/create-system config-overrides))))
  ([] (init {})))

(defn start []
  (alter-var-root #'system component/start))

(defn stop []
  (alter-var-root #'system
    (fn [s] (when s (component/stop s)))))

(defn run
  ([config-overrides]
   (init config-overrides)
   (start))
  ([] (run {})))

(defn reset
  ;; XXX Hmm... not sure how to get config-overrides with reset because of refresh :after needing a 0-arity fn
  []
  (stop)
  (refresh :after 'user/run))

(comment
  (try
    ;(stop)
    (run {:datomic {:seed-data "config/local/seed-data.edn"}})
    (catch Exception e (.printStackTrace e)))
  (stop)
  (reset)
  )

