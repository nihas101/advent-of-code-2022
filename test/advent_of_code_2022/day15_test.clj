(ns advent-of-code-2022.day15-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day15 :refer :all]))

(defonce ^:private example-input (parse-input "Sensor at x=2, y=18: closest beacon is at x=-2, y=15
Sensor at x=9, y=16: closest beacon is at x=10, y=16
Sensor at x=13, y=2: closest beacon is at x=15, y=3
Sensor at x=12, y=14: closest beacon is at x=10, y=16
Sensor at x=10, y=20: closest beacon is at x=10, y=16
Sensor at x=14, y=17: closest beacon is at x=10, y=16
Sensor at x=8, y=7: closest beacon is at x=2, y=10
Sensor at x=2, y=0: closest beacon is at x=2, y=10
Sensor at x=0, y=11: closest beacon is at x=2, y=10
Sensor at x=20, y=14: closest beacon is at x=25, y=17
Sensor at x=17, y=20: closest beacon is at x=21, y=22
Sensor at x=16, y=7: closest beacon is at x=15, y=3
Sensor at x=14, y=3: closest beacon is at x=15, y=3
Sensor at x=20, y=1: closest beacon is at x=15, y=3"))

(def ^:private input (parse-input (slurp "resources/day15.txt")))

(deftest day15-1-example-test
  (testing "day15-1 example"
    (is (= 26 (day15-1 example-input 10)))))

(deftest day15-1-test
  (testing "day15-1"
    (is (= 4725496 (day15-1 input)))))

(deftest day15-2-example-test
  (testing "day15-2 example"
    (is (= 56000011 (day15-2 example-input 0 20)))))

(deftest day15-2-test
  (testing "day15-2"
    (is (= 12051287042458 (day15-2 input)))))
