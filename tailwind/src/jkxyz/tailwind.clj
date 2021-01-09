(ns jkxyz.tailwind
  (:require
   [clojure.spec.alpha :as spec]))

(spec/def ::tw-val
  (spec/or :keyword keyword?
           :string string?
           :number number?
           :boolean boolean?))

(spec/def ::tw-map
  (spec/map-of keyword?
               (spec/or :val ::tw-val
                        :map (spec/map-of keyword? ::tw-val)
                        :vec (spec/coll-of (spec/or :keyword keyword?
                                                    :string string?
                                                    :map ::tw-map)))))

(spec/def ::tw-args
  (spec/* (spec/or :map ::tw-map
                   :keyword keyword?
                   :string string?
                   :vec (spec/coll-of (spec/or :keyword keyword? :string string?)))))

(defn- parse-map [m]
  (reduce
   (fn [classes [k v]]
     (cond
       ;; If v is another collection of classes, we prepend k as the variant
       ;; prefix for all of the classes in v, e.g.
       ;; {:lg {:px 1}} => lg:px-1
       ;; {:lg [:container {:px 1}]} => lg:container lg:px-1

       (map? v)
       (apply conj classes (map #(str (name k) ":" %) (parse-map v)))

       (coll? v)
       (apply conj
              classes
              (->> v
                   (mapcat #(if (map? %) (parse-map %) [%]))
                   (map #(str (name k) ":" (name %)))))

       ;; If v is a single value, we use k as a shorthand prefix, e.g.
       ;; {:px 1} => px-1
       (not (false? v))
       (conj classes
             ;; If the value is a negative number then prefix the class with a minus, e.g.
             ;; {:px -1} => -px-1
             (str (when (and (number? v) (neg? v)) "-")
                  (name k)
                  (cond
                    (keyword? v) (str "-" (name v))
                    ;; Use the absolute value here as we handle the minus above
                    (number? v) (str "-" (Math/abs v))
                    (boolean? v) nil
                    :else (str "-" v))))

       :else classes))
   []
   m))

(def ^:dynamic *used-classes* nil)

(defmacro tw
  "Takes a collection of class specs and returns a list of class names.

  Examples:

  (tw {:px 2 :py 1} :text-white :bg-blue-500) => px-2 py-1 text-white bg-blue-500
  (tw {:hover [:text-gray-100 {:pl 0}]}) => hover:text-gray-100 hover:pl-0
  (tw {:lg [:container {:ml -2}]}) => lg:container lg:-ml-2"
  [& classes]
  (assert (spec/valid? ::tw-args classes) (spec/explain-data ::tw-args classes))
  (let [classes (->> classes
                     (mapcat #(cond (map? %) (parse-map %) (coll? %) % :else [%]))
                     (mapv name))]
    (when *used-classes* (swap! *used-classes* into classes))
    classes))

(spec/fdef tw
  :args ::tw-args
  :ret (spec/coll-of string?))
