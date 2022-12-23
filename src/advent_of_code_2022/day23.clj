(ns advent-of-code-2022.day23
  (:require
   [advent-of-code-2022.utils :as u]
   [clojure.string :as string]))

(defn parse-input [input]
  (transduce
   (comp
    (filter (fn [[_ v]] (= v \#)))
    (map first))
   conj #{}
   (:positions (u/parse-positional-map input identity))))

(defn- neighbour-positions [[^long x ^long y]]
  [[(dec x) (dec y)] [x (dec y)] [(inc x) (dec y)]
   [(dec x)       y]             [(inc x)       y]
   [(dec x) (inc y)] [x (inc y)] [(inc x) (inc y)]])

(defn- north-position [[^long x ^long y]]
  [x (dec y)])

(defn- northern-positions [[^long x ^long y]]
  [[(dec x) (dec y)] [x (dec y)] [(inc x) (dec y)]])

(defn- south-position [[^long x ^long y]]
  [x (inc y)])

(defn- southern-positions [[^long x ^long y]]
  [[(dec x) (inc y)] [x (inc y)] [(inc x) (inc y)]])

(defn- west-position [[^long x ^long y]]
  [(dec x) y])

(defn- western-positions [[^long x ^long y]]
  [[(dec x) (dec y)] [(dec x) y] [(dec x) (inc y)]])

(defn- eastern-positions [[^long x ^long y]]
  [[(inc x) (dec y)] [(inc x) y] [(inc x) (inc y)]])

(defn- east-position [[^long x ^long y]]
  [(inc x) y])

(defn- first-valid-direction [pos elves move-offsets]
  (loop [[m & ms] move-offsets]
    (cond
      (nil? m) nil
      (and (= m :north)
           (not-any? elves (northern-positions pos))) [pos (north-position pos)]
      (and (= m :south)
           (not-any? elves (southern-positions pos))) [pos (south-position pos)]
      (and (= m :west)
           (not-any? elves (western-positions pos))) [pos (west-position pos)]
      (and (= m :east)
           (not-any? elves (eastern-positions pos))) [pos (east-position pos)]
      :else (recur ms))))

(defonce ^:private move-offsets
  [:north :south :west :east])

(defn- visualize [elves]
  (let [[^long min-x ^long max-x] (u/min+max (mapv first elves))
        [^long min-y ^long max-y] (u/min+max (mapv second elves))]
    (println (string/join
              (for [y (range min-y (inc max-y))
                    x (range min-x (inc max-x))]
                (str (if (elves [x y]) \# \.)
                     (if (= x max-x) \newline "")))))))

(defn- empty-tiles [elves]
  (let [[^long min-x ^long max-x] (u/min+max (mapv first elves))
        [^long min-y ^long max-y] (u/min+max (mapv second elves))]
    (- (* (- (inc max-x) min-x)
          (- (inc max-y) min-y))
       (count elves))))

(defn- elves-with-neighbours [elves]
  (fn [elf] (some elves (neighbour-positions elf))))

(defn- permitted-moves [elves move-offsets]
  (let [proposed-moves (transduce
                        (comp
                         (filter (elves-with-neighbours elves))
                         (map #(first-valid-direction % elves move-offsets))
                         (remove nil?))
                        conj [] elves)
        proposed-frequencies (frequencies (mapv second proposed-moves))]
    (filterv #(< ^long (proposed-frequencies (second %)) 2)
             proposed-moves)))

(defn- reposition-elf [elves [old-pos new-pos]]
  (-> elves
      (disj ,,, old-pos)
      (conj ,,, new-pos)))

(defn day23-1
  ([elves] (day23-1 elves move-offsets 10))
  ([elves move-offsets ^long rounds]
   (if (zero? rounds)
     (empty-tiles elves)
     (recur (reduce reposition-elf elves (permitted-moves elves move-offsets))
            (conj (subvec move-offsets 1) (first move-offsets))
            (dec rounds)))))

(defn day23-2
  ([elves] (day23-2 elves move-offsets 0))
  ([elves move-offsets ^long round]
   (if-let [perm-moves (seq (permitted-moves elves move-offsets))]
     (recur (reduce reposition-elf elves perm-moves)
            (conj (subvec move-offsets 1) (first move-offsets))
            (inc round))
     (inc round))))