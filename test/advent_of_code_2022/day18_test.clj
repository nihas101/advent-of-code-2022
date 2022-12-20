(ns advent-of-code-2022.day18-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day18 :refer :all]))

(defonce ^:private example-input (parse-input "2,2,2
1,2,2
3,2,2
2,1,2
2,3,2
2,2,1
2,2,3
2,2,4
2,2,6
1,2,5
3,2,5
2,1,5
2,3,5"))

(def ^:private input (parse-input (slurp "resources/day18.txt")))

(deftest day18-1-mini-example-test
  (testing "day18-1 mini example"
    (is (= 10 (day18-1 [[1 1 1] [2 1 1]])))))

(deftest day18-1-example-test
  (testing "day18-1 example"
    (is (= 64 (day18-1 example-input)))))

(deftest day18-1-test
  (testing "day18-1"
    (is (= 4636 (day18-1 input)))))

(deftest day18-2-mini-example-test
  (testing "day18-2 mini example"
    (is (= 10 (day18-2 [[1 1 1] [2 1 1]])))))

(deftest day18-2-example-test
  (testing "day18-2 example"
    (is (= 58 (day18-2 example-input)))))

(deftest day18-2-test
  (testing "day18-2"
    (is (= 2572 (day18-2 input)))))
