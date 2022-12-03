(ns advent-of-code-2022.day03
  (:require
   [advent-of-code-2022.utils :as u]
   [clojure.set :as set]))

(defn parse-input [input]
  (u/split-sections input u/line-endings))

(defn- chars->priority-mapping [a z ^long priority-offset]
  (mapv (juxt char (fn [^long c] (- c priority-offset)))
        (range (int a) (inc (int z)))))

(defonce ^:private priority
  (into {}
        (concat (chars->priority-mapping \a \z 96)
                (chars->priority-mapping \A \Z 38))))

(defn day03 [rucksack-splitter]
  (fn [rucksacks]
    (transduce
     (comp
      rucksack-splitter
      (mapcat (partial apply set/intersection))
      (map priority))
     + rucksacks)))

(defonce day03-1
  (day03
   (map (fn [rucksack]
          (mapv (partial into #{})
                (split-at (quot (count rucksack) 2) rucksack))))))

(defonce day03-2
  (day03 (comp (map (partial into #{})) (partition-all 3))))