(ns advent-of-code-2022.day10-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day10 :refer :all]))

(defonce ^:private example-input (parse-input "noop
addx 3
addx -5"))

(defonce ^:private example-input-1 (parse-input "addx 15
addx -11
addx 6
addx -3
addx 5
addx -1
addx -8
addx 13
addx 4
noop
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx -35
addx 1
addx 24
addx -19
addx 1
addx 16
addx -11
noop
noop
addx 21
addx -15
noop
noop
addx -3
addx 9
addx 1
addx -3
addx 8
addx 1
addx 5
noop
noop
noop
noop
noop
addx -36
noop
addx 1
addx 7
noop
noop
noop
addx 2
addx 6
noop
noop
noop
noop
noop
addx 1
noop
noop
addx 7
addx 1
noop
addx -13
addx 13
addx 7
noop
addx 1
addx -33
noop
noop
noop
addx 2
noop
noop
noop
addx 8
noop
addx -1
addx 2
addx 1
noop
addx 17
addx -9
addx 1
addx 1
addx -3
addx 11
noop
noop
addx 1
noop
addx 1
noop
noop
addx -13
addx -19
addx 1
addx 3
addx 26
addx -30
addx 12
addx -1
addx 3
addx 1
noop
noop
noop
addx -9
addx 18
addx 1
addx 2
noop
noop
addx 9
noop
noop
noop
addx -1
addx 2
addx -37
addx 1
addx 3
noop
addx 15
addx -21
addx 22
addx -6
addx 1
noop
addx 2
addx 1
noop
addx -10
noop
noop
addx 20
addx 1
addx 2
addx 2
addx -6
addx -11
noop
noop
noop"))

(def ^:private input (parse-input (slurp "resources/day10.txt")))

(deftest day10-1-example-test
  (testing "day10-1 example"
    (is (= 0 (day10-1 example-input)))))

(deftest day10-1-example-1-test
  (testing "day10-1 example 1"
    (is (= 13140 (day10-1 example-input-1)))))

(deftest day10-1-test
  (testing "day10-1"
    (is (= 11780 (day10-1 input)))))

(deftest day10-2-example-test
  (testing "day10-2 example"
    (is (= (str "##..##..##..##..##..##..##..##..##..##..\n"
                "###...###...###...###...###...###...###.\n"
                "####....####....####....####....####....\n"
                "#####.....#####.....#####.....#####.....\n"
                "######......######......######......####\n"
                "#######.......#######.......#######.....\n")
           (day10-2 example-input-1)))))

(deftest day10-2-test
  (testing "day10-2"
    (is (= (str "###..####.#..#.#....###...##..#..#..##..\n"
                "#..#....#.#..#.#....#..#.#..#.#..#.#..#.\n"
                "#..#...#..#..#.#....###..#..#.#..#.#..#.\n"
                "###...#...#..#.#....#..#.####.#..#.####.\n"
                "#....#....#..#.#....#..#.#..#.#..#.#..#.\n"
                "#....####..##..####.###..#..#..##..#..#.\n")
           (day10-2 input)))))
