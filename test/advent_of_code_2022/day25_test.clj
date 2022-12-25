(ns advent-of-code-2022.day25-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day25 :refer :all]))

(defonce ^:private example-input
  (parse-input (str "1=-0-2\n"
                    "12111\n"
                    "2=0=\n"
                    "21\n"
                    "2=01\n"
                    "111\n"
                    "20012\n"
                    "112\n"
                    "1=-1=\n"
                    "1-12\n"
                    "12\n"
                    "1=\n"
                    "122")))

(def ^:private input (parse-input (slurp "resources/day25.txt")))

(deftest snafu->decimal-test
  (testing "snafu->decimal examples"
    (are [x y] (= x (#'advent-of-code-2022.day25/snafu->decimal
                     (first (parse-input y))))
      1             "1"
      2             "2"
      3             "1="
      4             "1-"
      5             "10"
      6             "11"
      7             "12"
      8             "2="
      9             "2-"
      10             "20"
      15            "1=0"
      20            "1-0"
      2022         "1=11-2"
      12345        "1-0---0"
      314159265  "1121-1110-1=0")))

(deftest decimal->snafu-test
  (testing "decimal->snafu examples"
    (are [x y] (= x (#'advent-of-code-2022.day25/decimal->snafu y))
      "1" 1
      "2" 2
      "1=" 3
      "1-" 4
      "10" 5
      "11" 6
      "12" 7
      "2=" 8
      "2-" 9
      "20" 10
      "1=0" 15
      "1-0" 20
      "1=11-2" 2022
      "1-0---0" 12345
      "1121-1110-1=0" 314159265)))

(deftest day25-example-test
  (testing "day25 example"
    (is (= "2=-1=0" (day25 example-input)))))

(deftest day25-test
  (testing "day25"
    (is (= "2==221=-002=0-02-000" (day25 input)))))
