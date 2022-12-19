(ns advent-of-code-2022.day13)

(defn parse-input [input]
  (read-string (str "[" input "]")))

(declare ordered-seq? ordered-pair?)

(defn- ordered-seq? ^long [[a & aa :as aaa] [b & bb :as bbb]]
  (cond
    (and (empty? aaa) (empty? bbb)) 0
    (empty? aaa) -1
    (empty? bbb) 1
    :else (let [dec (ordered-pair? a b)]
            (if (zero? ^long dec)
              (recur aa bb)
              dec))))

(defn- ordered-int? ^long [^long a ^long b]
  (cond
    (< a b) -1
    (> a b) 1
    :else 0))

(defn- ordered-pair? ^long [a b]
  (cond
    (and (number? a) (number? b)) (ordered-int? a b)
    (and (sequential? a) (sequential? b)) (ordered-seq? a b)
    (and (number? a) (sequential? b)) (recur [a] b)
    (and (sequential? a) (number? b)) (recur a [b])
    :else 0))

(defn day13-1 [input]
  (transduce
   (comp
    (map-indexed (fn [^long idx [a b]] [(inc idx) (ordered-pair? a b)]))
    (filter (comp neg? second))
    (map first))
   + (partition 2 input)))

(defonce ^:private divider? #{[[2]] [[6]]})

(defn day13-2 [input]
  (transduce
   (comp
    (map-indexed (fn [^long idx v] [(inc idx) v]))
    (filter (comp divider? second))
    (map first))
   * (sort-by identity ordered-pair? (into input divider?))))