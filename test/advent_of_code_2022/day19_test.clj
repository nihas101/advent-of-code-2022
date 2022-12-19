(ns advent-of-code-2022.day19-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day19 :refer :all]))

(defonce ^:private example-input
  (parse-input (str "Blueprint 1: Each ore robot costs 4 ore. "
                    "Each clay robot costs 2 ore. "
                    "Each obsidian robot costs 3 ore and 14 clay. "
                    "Each geode robot costs 2 ore and 7 obsidian.\n"

                    "Blueprint 2: Each ore robot costs 2 ore. "
                    "Each clay robot costs 3 ore. "
                    "Each obsidian robot costs 3 ore and 8 clay. "
                    "Each geode robot costs 3 ore and 12 obsidian.")))

(def ^:private input (parse-input (slurp "resources/day19.txt")))

(deftest day19-1-example-test
  (testing "day19-1 example"
    (is (= 33 (day19-1 example-input)))))

(deftest day19-1-test
  (testing "day19-1 example"
    (is (= 1081 (day19-1 input)))))

(deftest day19-2-example-test
  (testing "day19-2 example"
    (is (= 3472 (day19-2 example-input)))))

(deftest day19-2-test
  (testing "day19-2 example"
    (is (= 2415 (day19-2 input)))))
