(ns advent-of-code-2022.day21-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day21 :refer :all]))

(defonce ^:private example-input (parse-input "root: pppw + sjmn
dbpl: 5
cczh: sllz + lgvd
zczc: 2
ptdq: humn - dvpt
dvpt: 3
lfqf: 4
humn: 5
ljgn: 2
sjmn: drzm * dbpl
sllz: 4
pppw: cczh / lfqf
lgvd: ljgn * ptdq
drzm: hmdt - zczc
hmdt: 32"))

(def ^:private input (parse-input (slurp "resources/day21.txt")))

(deftest day21-1-example-test
  (testing "day21-1 example"
    (is (= 152 (day21-1 example-input)))))

(deftest day21-1-test
  (testing "day21-1"
    (is (= 62386792426088 (day21-1 input)))))

(deftest day21-2-example-test
  (testing "day21-2 example"
    (is (= 301 (day21-2 example-input)))))

(deftest day21-2-test
  (testing "day21-2"
    (is (= 3876027196185 (day21-2 input)))))
