(ns advent-of-code-2022.day01-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day01 :refer :all]))

(def ^:private example-input "1000
2000
3000

4000

5000
6000

7000
8000
9000

10000")

(deftest day01-1-example-test
  (testing "day01-1 example"
    (is (= [4 24000] (day01-1 example-input)))))

(deftest day01-1-test
  (testing "day01-1 example"
    (is (= [172 70509] (day01-1 (slurp "resources/day01.txt"))))))


(deftest day01-2-example-test
  (testing "day01-2 example"
    (is (= 45000 (day01-2 example-input)))))

(deftest day01-2-test
  (testing "day01-2 example"
    (is (= 208567 (day01-2 (slurp "resources/day01.txt"))))))
