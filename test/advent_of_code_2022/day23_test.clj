(ns advent-of-code-2022.day23-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day23 :refer :all]))

(defonce ^:private example-input (parse-input "....#..
..###.#
#...#.#
.#...##
#.###..
##.#.##
.#..#.."))

(def ^:private input (parse-input (slurp "resources/day23.txt")))

(deftest day23-1-example-test
  (testing "day23-1 example"
    (is (= 110 (day23-1 example-input)))))

(deftest day23-1-test
  (testing "day23-1"
    (is (= 3925 (day23-1 input)))))

(deftest day23-2-example-test
  (testing "day23-2 example"
    (is (= 20 (day23-2 example-input)))))

(deftest day23-2-test
  (testing "day23-2"
    (is (= 903 (day23-2 input)))))