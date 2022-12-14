(ns advent-of-code-2022.day11-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day11 :refer :all]))

(defonce ^:private example-input (parse-input "Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1"))

(def ^:private input (parse-input (slurp "resources/day11.txt")))

(deftest day11-1-example-test
  (testing "day11-1 example"
    (is (= 10605 (day11-1 example-input)))))

(deftest day11-1-test
  (testing "day11-1 example"
    (is (= 182293 (day11-1 input)))))

(deftest day11-2-example-test
  (testing "day11-2 example"
    (is (= 2713310158 (day11-2 example-input)))))

(deftest day11-2-test
  (testing "day11-2 example"
    (is (= 54832778815 (day11-2 input)))))
