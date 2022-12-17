(ns advent-of-code-2022.day17-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day17 :refer :all]))

(defonce ^:private example-input (parse-input ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"))

(def ^:private input (parse-input (slurp "resources/day17.txt")))

(deftest day17-1-example-test
  (testing "day17-1 example"
    (is (= 3068 (day17-1 example-input)))))

(deftest day17-1-test
  (testing "day17-1 example"
    (is (= 3141 (day17-1 input)))))

(deftest day17-2-example-test
  (testing "day17-2 example"
    (is (= 1514285714288 (day17-2 example-input)))))

(deftest day17-2-test
  (testing "day17-2 example"
    (is (= 1561739130391 (day17-2 input)))))
