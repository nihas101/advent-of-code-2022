(ns advent-of-code-2022.day01
  (:require
   [advent-of-code-2022.utils :as u]))

(def ^:private sum-of-calories
  (map (fn [[i elf-cals]] [i (reduce + elf-cals)])))

(defn- reduce-indexed-calories [f init input]
  (transduce
   (comp
    (map-indexed
     (fn [^long i elf-cals] [(inc i) (u/read-longs elf-cals u/line-endings)]))
    sum-of-calories)
   f init input))

(defn day01-1 [input]
  (->> input
       u/split-sections
       (reduce-indexed-calories (partial max-key second) [0 0])))

(defn day01-2 [input]
  (let [sorted-elf-calories (->> input
                                 u/split-sections
                                 (reduce-indexed-calories conj [])
                                 (sort-by (partial second) >))]
    (transduce
     (comp
      (take 3)
      (map second))
     + sorted-elf-calories)))