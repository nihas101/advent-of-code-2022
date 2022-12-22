(ns advent-of-code-2022.day22-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day22 :refer :all]))

(defonce ^:private example-input (parse-input
                                  (str "        ...#\n"
                                       "        .#..\n"
                                       "        #...\n"
                                       "        ....\n"
                                       "...#.......#\n"
                                       "........#...\n"
                                       "..#....#....\n"
                                       "..........#.\n"
                                       "        ...#....\n"
                                       "        .....#..\n"
                                       "        .#......\n"
                                       "        ......#.\n"
                                       "\n"
                                       "10R5L5R10L4R5L5")))

(def ^:private input (parse-input (slurp "resources/day22.txt")))

(deftest day22-1-example-test
    (testing "day22-1 example"
      (is (= 6032 (day22-1 example-input)))))

(deftest day22-1-test
    (testing "day22-1"
      (is (= 103224 (day22-1 input)))))

(deftest day22-2-example-test
  (testing "day22-2 example"
    (is (= 5031 (day22-2 example-input)))))

(deftest day22-2-test
  (testing "day22-2"
    (is (= 189097 (day22-2 input)))))
