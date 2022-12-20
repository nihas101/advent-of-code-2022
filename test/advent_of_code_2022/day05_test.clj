(ns advent-of-code-2022.day05-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day05 :refer :all]))

(def ^:private example-input (parse-input "    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2"))

(def ^:private input (parse-input (slurp "resources/day05.txt")))

(deftest day05-1-example-test
  (testing "day05-1 example"
    (is (= "CMZ" (day05-1 example-input)))))

(deftest day05-1-test
  (testing "day05-1"
    (is (= "TPGVQPFDH" (day05-1 input)))))

(deftest day05-2-example-test
  (testing "day05-2 example"
    (is (= "MCD" (day05-2 example-input)))))

(deftest day05-2-test
  (testing "day05-2"
    (is (= "DMRDFRHHH" (day05-2 input)))))
