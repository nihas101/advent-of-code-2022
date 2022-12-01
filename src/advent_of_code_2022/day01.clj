(ns advent-of-code-2022.day01
  (:require
   [advent-of-code-2022.utils :as u]))

(defn parse-input [input]
  (map-indexed
   (fn [^long i elf-cals] [(inc i) (u/read-longs elf-cals u/line-endings)])
   (u/split-sections input)))

(def ^:private sum-of-calories
  (map (fn [[i elf-cals]] [i (reduce + elf-cals)])))

(defn- reduce-indexed-calories [f init input]
  (transduce sum-of-calories f init input))

(defn day01-1 [parsed-input]
  (reduce-indexed-calories (partial max-key second) [0 0]
                           parsed-input))

(defn day01-2 [parsed-input]
  (let [sorted-elf-calories (->> parsed-input
                                 (reduce-indexed-calories conj [])
                                 (sort-by second >))]
    (transduce
     (comp (take 3) (map second))
     + sorted-elf-calories)))