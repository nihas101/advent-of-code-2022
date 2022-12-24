(ns advent-of-code-2022.day24-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day24 :refer :all]))

(defonce ^:private example-input
  (parse-input (str "#.######\n"
                    "#>>.<^<#\n"
                    "#.<..<<#\n"
                    "#>v.><>#\n"
                    "#<^v^^>#\n"
                    "######.#\n")))

(def ^:private input (parse-input (slurp "resources/day24.txt")))

(deftest day24-1-example-test
  (testing "day24-1 example"
    (is (= 18 (day24-1 example-input)))))

(deftest day24-1-test
  (testing "day24-1"
    (is (= 262 (day24-1 input)))))

(deftest day24-2-example-test
  (testing "day24-2 example"
    (is (= 54 (day24-2 example-input)))))

(deftest day24-2-test
  (testing "day24-2"
    (is (= 785 (day24-2 input)))))