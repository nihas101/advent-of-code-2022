(ns advent-of-code-2022.day07-test
  (:require
   [clojure.test :refer :all]
   [advent-of-code-2022.day07 :refer :all]))

(def ^:private example-input (parse-input "$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k"))

(def ^:private input (parse-input (slurp "resources/day07.txt")))

(deftest day07-1-example-test
  (testing "day07-1 example"
    (is (= 95437 (day07-1 example-input)))))

(deftest day07-1-test
  (testing "day07-1 example"
    (is (= 1084134 (day07-1 input)))))

(deftest day07-2-example-test
  (testing "day07-2 example"
    (is (= 24933642 (day07-2 example-input)))))

(deftest day07-2-test
  (testing "day07-2 example"
    (is (= 6183184 (day07-2 input)))))
