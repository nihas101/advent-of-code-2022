(ns advent-of-code-2022.day25
  (:require
   [advent-of-code-2022.utils :as u]
   [clojure.string :as string]))

(defonce ^:private snafu->decimal-digit
  {\2 2
   \1 1
   \0 0
   \- -1
   \= -2})

(defonce ^:private decimal->snafu-digit
  {0 \0
   1 \1
   2 \2
   3 \=
   4 \-})

(defn parse-input [input]
  (mapv (fn [number] (mapv snafu->decimal-digit number))
        (u/split-sections input u/line-endings)))

(defn- snafu->decimal [snafu-number]
  (reduce + (mapv * (reverse snafu-number)
                  (iterate (partial * 5) 1))))

(defn- snafu-step [[_ ^long n]]
  (let [quot-n (quot n 5)
        mod-n (mod n 5)]
    [n
     (if (< 2 mod-n) (inc quot-n) quot-n)
     (decimal->snafu-digit mod-n)]))

(defn- decimal->snafu [snafu-number]
  (string/join
   (reverse
    (transduce
     (comp
      (take-while (comp pos? first))
      (drop 1)
      (map #(nth % 2)))
     conj [] (iterate snafu-step [snafu-number snafu-number])))))

(defn day25 [snafu-numbers]
  (decimal->snafu (transduce (map snafu->decimal) + snafu-numbers)))