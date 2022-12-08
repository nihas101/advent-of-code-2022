(ns advent-of-code-2022.day08-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day08 :refer :all]))

(def ^:private example-input (parse-input "30373
25512
65332
33549
35390"))

(def ^:private input (parse-input (slurp "resources/day08.txt")))

(deftest day08-1-example-test
  (testing "day08-1 example"
    (is (= 21 (day08-1 example-input)))))

(deftest day08-1-test
  (testing "day08-1 example"
    (is (= 1792 (day08-1 input)))))

(deftest day08-2-example-test
  (testing "day08-2 example"
    (is (= 8 (day08-2 example-input)))))

(deftest day08-2-test
  (testing "day08-2 example"
    (is (= 334880 (day08-2 input)))))
