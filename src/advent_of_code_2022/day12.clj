(ns advent-of-code-2022.day12
  (:require
   [advent-of-code-2022.utils :as u]))

(defn parse-input [input]
  (u/parse-positional-map input identity))

(defonce ^:private sym->height
  (into {} (concat
            (mapv (fn [c] [(char c) (- (long c) (long \a))])
                  (range (long \a) (inc (long \z))))
            [[\S 0] [\E 25]])))

(defn- neighbour-positions [[^long x ^long y]]
  [[x (dec y)]
   [(dec x) y] [(inc x) y]
   [x (inc y)]])

(defn- visitable? [height-sym]
  (fn [[_ h-sym]] (<= ^long (sym->height h-sym)
                      (inc ^long (sym->height height-sym)))))

(defn- visitable-neighbours [grid]
  (let [grid-lookup (juxt identity grid)]
    (fn [[pos height-sym] visited]
      (transduce
       (comp
        (map grid-lookup)
        (filter second)
        (remove visited)
        (filter (visitable? height-sym)))
       conj [] (neighbour-positions pos)))))

(defn- path-length
  ([] 0)
  ([^long path-length] path-length)
  ([^long path-length _] (inc path-length)))

(defn day12 [grid start-points]
  (let [visitable-neighbours (visitable-neighbours grid)]
    (transduce
     (comp
      (map #(u/bfs % \E visitable-neighbours path-length))
      (remove nil?))
     min Long/MAX_VALUE
     start-points)))

(defn day12-1 [grid]
  (day12 grid (take 1 (drop-while (fn [[_ sp]] (not= sp \S)) grid))))

(defn day12-2 [grid]
  (day12 grid (filter #(-> % second #{\S \a}) grid)))