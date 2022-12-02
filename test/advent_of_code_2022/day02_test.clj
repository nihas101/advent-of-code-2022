(ns advent-of-code-2022.day02-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day02 :refer :all]))

(def ^:private example-input (parse-input "A Y
B X
C Z"))

(def ^:private input (parse-input (slurp "resources/day02.txt")))

(deftest day02-1-example-test
  (testing "day02-1 example"
    (is (= 15 (day02-1 example-input)))))

(deftest day02-1-test
  (testing "day01-1 example"
    (is (= 12855 (day02-1 input)))))

(deftest day02-2-example-test
  (testing "day01-2 example"
    (is (= 12 (day02-2 example-input)))))

(deftest day02-2-test
  (testing "day01-2 example"
    (is (= 13726 (day02-2 input)))))
