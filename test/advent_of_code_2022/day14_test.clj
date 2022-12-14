(ns advent-of-code-2022.day14-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day14 :refer :all]))

(defonce ^:private example-input (parse-input "498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9"))

(def ^:private input (parse-input (slurp "resources/day14.txt")))

(deftest day14-1-example-test
  (testing "day14-1 example"
    (is (= 24 (day14-1 example-input)))))

(deftest day14-1-test
  (testing "day14-1 example"
    (is (= 1003 (day14-1 input)))))

(deftest day14-2-example-test
  (testing "day14-2 example"
    (is (= 93 (day14-2 example-input)))))

(deftest day14-2-test
  (testing "day14-2 example"
    (is (= 25771 (day14-2 input)))))
