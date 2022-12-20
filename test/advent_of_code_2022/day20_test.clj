(ns advent-of-code-2022.day20-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day20 :refer :all]))

(deftest day20-1-tiny-1-example-test
  (testing "day20-1 tiny example 1"
    (is (= [0 0 1 0]
           (decrypt (parse-input "0\n1\n0\n0"))))))

(deftest day20-1-tiny-2-example-test
  (testing "day20-1 tiny example 2"
    (is (= [2 0 0 0 0]
           (decrypt (parse-input "0\n0\n2\n0\n0"))))))

(deftest day20-1-tiny-3-example-test
  (testing "day20-1 tiny example 3"
    (is (= [-1 0 0 0]
           (decrypt (parse-input "0\n-1\n0\n0"))))))

(deftest day20-1-tiny-4-example-test
  (testing "day20-1 tiny example 4"
    (is (= [0 0 -2 0]
           (decrypt (parse-input "0\n-2\n0\n0"))))))

(deftest day20-1-tiny-5-example-test
  (testing "day20-1 tiny example 5"
    (is (= [0 5 0 0]
           (decrypt (parse-input "0\n0\n5\n0"))))))

(deftest day20-1-tiny-6-example-test
  (testing "day20-1 tiny example 6"
    (is (= [0 0 -18 0]
           (decrypt (parse-input "0\n0\n-18\n0"))))))

(defonce ^:private example-input (parse-input "1
2
-3
3
-2
0
4"))

(def ^:private input (parse-input (slurp "resources/day20.txt")))

(deftest day20-1-example-test
  (testing "day20-1 example"
    (is (= 3 (day20-1 example-input)))))

(deftest day20-1-test
  (testing "day20-1"
    (is (= 27726 (day20-1 input)))))

(deftest day20-2-example-test
  (testing "day20-2 example"
    (is (= 1623178306 (day20-2 example-input)))))

(deftest day20-2-test
  (testing "day20-2"
    (is (= 4275451658004 (day20-2 input)))))
