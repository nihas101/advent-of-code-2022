(ns advent-of-code-2022.day04-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day04 :refer :all]))

(def ^:private example-input (parse-input "2-4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8"))

(def ^:private input (parse-input (slurp "resources/day04.txt")))

(deftest day04-1-example-test
  (testing "day04-1 example"
    (is (= 2 (day04-1 example-input)))))

(deftest day04-1-test
  (testing "day04-1 example"
    (is (= 567 (day04-1 input)))))

(deftest day04-2-example-test
  (testing "day04-2 example"
    (is (= 4 (day04-2 example-input)))))

(deftest day04-2-test
  (testing "day04-2 example"
    (is (= 907 (day04-2 input)))))
