(ns advent-of-code-2022.day02
  (:require
   [advent-of-code-2022.utils :as u]))

(defonce ^:private strategy->result
  {["C" "X"] :win
   ["A" "Y"] :win
   ["B" "Z"] :win
   ["A" "X"] :draw
   ["B" "Y"] :draw
   ["C" "Z"] :draw})

(defonce ^:private shape->points
  {"X" 1
   "Y" 2
   "Z" 3
   
   "A" 1
   "B" 2
   "C" 3})

(defonce ^:private result->points
  {:win 6
   :draw 3
   nil 0

   "Z" 6
   "Y" 3
   "X" 0})

(defn parse-input [input]
  (u/split-pairs input))

(defn day02 [strategy->points]
  (fn [strategy-guide]
    (transduce strategy->points + strategy-guide)))

(defonce day02-1
  (day02 (mapcat (juxt (comp result->points strategy->result)
                       (comp shape->points second)))))

(def ^:private strategy->shape
  {["A" "X"] "C"
   ["B" "X"] "A"
   ["C" "X"] "B"

   ["A" "Y"] "A"
   ["B" "Y"] "B"
   ["C" "Y"] "C"

   ["A" "Z"] "B"
   ["B" "Z"] "C"
   ["C" "Z"] "A"})

(defonce day02-2
  (day02 (mapcat (juxt (comp shape->points strategy->shape)
                       (comp result->points second)))))