(ns advent-of-code-2022.day09
  (:require
   [advent-of-code-2022.utils :as u]))

(defn parse-input [input]
  (u/split-pairs input))

(defn- move-knot [[^long hx ^long hy] [^long tx ^long ty :as tail]]
  (cond
     ;; Head and tail are touching -> No move required
    (and (<= (u/abs (- hx tx)) 1)
         (<= (u/abs (- hy ty)) 1)) tail
     ;; Head and tail are above or beside each other -> Move tail
    (or (= hx tx) (= hy ty)) [(+ tx (quot (- hx tx) 2))
                              (+ ty (quot (- hy ty) 2))]
     ;; Move tail diagonally
    :else [(+ tx (u/sign (- hx tx)))
           (+ ty (u/sign (- hy ty)))]))

(defn- move-tail
  ([rope] (move-tail rope [(first rope)]))
  ([[head tail & rope] new-rope]
   (if tail
     (let [knot (move-knot head tail)]
       (recur (conj rope knot)
              (conj new-rope knot)))
     new-rope)))

(defn move-head [d]
  (cond
    (= d "R") (fn [[^long x ^long y]] [(inc x) y])
    (= d "U") (fn [[^long x ^long y]] [x (dec y)])
    (= d "L") (fn [[^long x ^long y]] [(dec x) y])
    (= d "D") (fn [[^long x ^long y]] [x (inc y)])))

(defn execute-move [state [d n]]
  (loop [state state
         [d ^long n] [d (Long/parseLong n)]]
    (let [head [:rope 0]
          tail [:rope (-> state :rope count dec)]]
      (if (zero? n)
        (update state :visited conj (get-in state tail))
        (recur (as-> state s
                 (update-in s head (move-head d))
                 (update s :rope move-tail)
                 (update s :visited conj (get-in s tail))) [d (dec n)])))))

(defn day09 [state input]
  (-> (reduce execute-move state input) :visited count))