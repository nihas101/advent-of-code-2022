(ns advent-of-code-2022.day16-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day16 :refer :all]))

(defonce ^:private example-input (parse-input "Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
Valve BB has flow rate=13; tunnels lead to valves CC, AA
Valve CC has flow rate=2; tunnels lead to valves DD, BB
Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
Valve EE has flow rate=3; tunnels lead to valves FF, DD
Valve FF has flow rate=0; tunnels lead to valves EE, GG
Valve GG has flow rate=0; tunnels lead to valves FF, HH
Valve HH has flow rate=22; tunnel leads to valve GG
Valve II has flow rate=0; tunnels lead to valves AA, JJ
Valve JJ has flow rate=21; tunnel leads to valve II"))

(def ^:private input (parse-input (slurp "resources/day16.txt")))

(deftest day16-1-example-test
  (testing "day16-1 example"
    (is (= 1651 (day16-1 "AA" example-input)))))

(deftest day16-1-test
  (testing "day16-1"
    (is (= 1659 (day16-1 "AA" input)))))
#_
(deftest day16-2-example-test
  (testing "day16-2 example"
    (is (= 1707 (day16-2 "AA" example-input)))))

(deftest day16-2-test
  (testing "day16-2"
    (is (= 2382 (day16-2 "AA" input)))))
