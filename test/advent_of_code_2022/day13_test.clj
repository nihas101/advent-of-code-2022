(ns advent-of-code-2022.day13-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day13 :refer :all]))

(defonce ^:private example-input (parse-input "[1,1,3,1,1]
[1,1,5,1,1]

[[1],[2,3,4]]
[[1],4]

[9]
[[8,7,6]]

[[4,4],4,4]
[[4,4],4,4,4]

[7,7,7,7]
[7,7,7]

[]
[3]

[[[]]]
[[]]

[1,[2,[3,[4,[5,6,7]]]],8,9]
[1,[2,[3,[4,[5,6,0]]]],8,9]"))

(defonce ^:private op? #'advent-of-code-2022.day13/ordered-pair?)

(deftest day13-1-int-pair-tests
  (testing "day13-1 int pair tests"
    (are [x y] (= x y)
      -1 (op? 1 2)
      0 (op? 2 2)
      1 (op? 2 1))))

(deftest day13-1-mixed-pair-tests
  (testing "day13-1 mixed pair tests"
    (are [x y] (= x y)
      -1 (op? [] 1)
      -1 (op? 1 [2])
      -1 (op? 1 [2 1])
      0 (op? [1] 1)
      1 (op? [1 2 3] 1)
      1 (op? 3 [1]))))

(deftest day13-1-seq-pair-tests
  (testing "day13-1 seq pair tests"
    (are [x y] (= x y)
      -1 (op? [] [1 2 3])
      -1 (op? [[]] [[1 2 3]])
      -1 (op? [1] [2])
      -1 (op? [[] [[1]]] [[] [[2]]])
      -1 (op? [1] [2 1])
      0 (op? [] [])
      0 (op? [10] [[10]])
      0 (op? [10 [10] 20] [10 10 20])
      1 (op? [1 2 3] [])
      1 (op? [3] [1]))))

(def ^:private input (parse-input (slurp "resources/day13.txt")))

(deftest day13-1-example-test
  (testing "day13-1 example"
    (is (= 13 (day13-1 example-input)))))

(deftest day13-1-test
  (testing "day13-1 example"
    (is (= 6428 (day13-1 input)))))

(deftest day13-2-example-test
  (testing "day13-2 example"
    (is (= 140 (day13-2 example-input)))))

(deftest day13-2-test
  (testing "day13-2 example"
    (is (= 22464 (day13-2 input)))))