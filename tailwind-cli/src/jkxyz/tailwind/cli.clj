(ns jkxyz.tailwind.cli
  (:require
   [clojure.string :as string]
   [clojure.java.io :as io]
   [cljs.analyzer.api :as cljs-ana]
   [jkxyz.tailwind :as tailwind]))

(defn- ns-sym->file [sym]
  (let [parts (string/split (string/replace (str sym) #"-" "_") #"\.")
        filename (str (last parts) ".cljs")
        dirs (take (dec (count parts)) parts)]
    (io/resource (string/join "/" (concat dirs [filename])))))

(defn- load-cljs-namespaces [nses]
  (run! #(cljs-ana/analyze-file (ns-sym->file %)) nses))

(defn used-classes [{:keys [clj-nses cljs-nses output-to]}]
  (binding [tailwind/*used-classes* (atom #{})]
    (when (not-empty clj-nses) (apply require clj-nses))
    (when (not-empty cljs-nses) (load-cljs-namespaces cljs-nses))
    (let [output (with-out-str (run! println @tailwind/*used-classes*))]
      (if output-to
        (spit (io/file (str output-to)) output)
        (print output)))))
