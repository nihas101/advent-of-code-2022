(ns advent-of-code-2022.day06-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day06 :refer :all]))

(def ^:private example-input-1 "mjqjpqmgbljsphdztnvjfqwrcgsmlb")
(def ^:private example-input-2"bvwbjplbgvbhsrlpgdmjqwftvncz")
(def ^:private example-input-3 "nppdvjthqldpwncqszvftbrmjlhg")
(def ^:private example-input-4 "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")
(def ^:private example-input-5 "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")

(def ^:private input (slurp "resources/day06.txt"))

(deftest day06-1-example-1-test
  (testing "day06-1 example 1"
    (is (= 7 (day06-1 example-input-1)))))

(deftest day06-1-example-2-test
  (testing "day06-1 example 2"
    (is (= 5 (day06-1 example-input-2)))))

(deftest day06-1-example-3-test
  (testing "day06-1 example 3"
    (is (= 6 (day06-1 example-input-3)))))

(deftest day06-1-example-4-test
  (testing "day06-1 example 4"
    (is (= 10 (day06-1 example-input-4)))))

(deftest day06-1-example-5-test
  (testing "day06-1 example 5"
    (is (= 11 (day06-1 example-input-5)))))

(deftest day06-1-test
  (testing "day06-1 example"
    (is (= 1109 (day06-1 input)))))

(deftest day06-2-example-1-test
  (testing "day06-2 example 1"
    (is (= 19 (day06-2 example-input-1)))))

(deftest day06-2-example-2-test
  (testing "day06-2 example 2"
    (is (= 23 (day06-2 example-input-2)))))

(deftest day06-2-example-3-test
  (testing "day06-2 example 3"
    (is (= 23 (day06-2 example-input-3)))))

(deftest day06-2-example-4-test
  (testing "day06-2 example 4"
    (is (= 29 (day06-2 example-input-4)))))

(deftest day06-2-example-5-test
  (testing "day06-2 example 5"
    (is (= 26 (day06-2 example-input-5)))))

(deftest day06-2-test
  (testing "day06-2 example"
    (is (= 3965 (day06-2 input)))))
