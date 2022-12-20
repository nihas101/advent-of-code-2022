(ns advent-of-code-2022.day09-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day09 :refer :all]))

(defonce ^:private example-input (parse-input "R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2"))

(def ^:private state-1 {:rope [[0 0] [0 0]] :visited #{}})

(def ^:private input (parse-input (slurp "resources/day09.txt")))

(deftest day09-1-example-test
  (testing "day09-1 example"
    (is (= 13 (day09 state-1 example-input)))))

(deftest day09-1-test
  (testing "day09-1"
    (is (= 6406 (day09 state-1 input)))))

(def ^:private state-2 {:rope [[0 0]
                               [0 0] [0 0] [0 0]
                               [0 0] [0 0] [0 0]
                               [0 0] [0 0] [0 0]]
                        :visited #{}})

(deftest day09-2-example-test
  (testing "day09-2 example"
    (is (= 1 (day09 state-2 example-input)))))

(deftest day09-2-test
  (testing "day09-2"
    (is (= 2643 (day09 state-2 input)))))
