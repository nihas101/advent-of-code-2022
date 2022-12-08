(ns advent-of-code-2022.day08
  (:require
   [advent-of-code-2022.utils :as u]))

(defn parse-input [input]
  (u/parse-positional-map input))

(defn- all-inner-positions [^long width ^long height]
  (mapcat (fn [x] (mapv (partial vector x) (range 1 (dec height))))
          (range 1 (dec width))))

(defn- grid-neighbours [[^long x ^long y] w h]
  (vector
   (mapv (fn [x] [x y]) (reverse (range 0 x)))
   (mapv (fn [y] [x y]) (reverse (range 0 y)))
   (mapv (fn [y] [x y]) (range (inc y) h))
   (mapv (fn [x] [x y]) (range (inc x) w))))

(defn- visible? [{:keys [height width] :as tree-map} pos]
  (let [neighbours (grid-neighbours pos height width)
        tree-height (tree-map pos)]
    (some true? (mapv (fn [dir] (every? #(< ^long % ^long tree-height) (mapv tree-map dir)))
                      neighbours))))

(defn day08-1 [{:keys [^long height ^long width] :as tree-map}]
  (+ (* 2 (dec height)) (* 2 (dec width))
     (count (filter (partial visible? tree-map)
                    (all-inner-positions width height)))))

(defn viewable-trees-count [tree-map ^long tree-height]
  (fn [dir]
    (let [[viewable [h & _]] (split-with #(< ^long % tree-height) (mapv tree-map dir))]
      (if h (inc (count viewable)) (count viewable)))))

(defn- visible-positions [{:keys [height width] :as tree-map} pos]
  (let [neighbours (grid-neighbours pos height width)
        tree-height (tree-map pos)]
    (transduce
     (map (viewable-trees-count tree-map tree-height))
     * neighbours)))

(defn day08-2 [{:keys [height width] :as tree-map}]
  (transduce
   (map (partial visible-positions tree-map))
   max 0 (all-inner-positions width height)))