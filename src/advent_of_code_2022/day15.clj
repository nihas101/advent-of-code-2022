(ns advent-of-code-2022.day15
  (:require
   [advent-of-code-2022.utils :as u]))

(defn parse-input [input]
  (mapv (fn [beacon] (let [[sx sy bx by] (mapv #(Long/parseLong %)
                                               (re-seq #"-?\d+" beacon))]
                       {:sensor [sx sy]
                        :beacon [bx by]
                        :area-covered (u/manhattan-distance [sx sy] [bx by])}))
        (u/split-sections input u/line-endings)))

(defn- covered-by-sensor? [sensors]
  (fn [pos]
    (some (fn [{:keys [^long area-covered sensor]}]
            (<= (u/manhattan-distance sensor pos) area-covered))
          sensors)))

(defn- edge-positions [^long y {[^long sx ^long sy] :sensor
                                ^long area-covered :area-covered}]
  (let [y-distance (u/abs (- sy y))
        x-distance (- area-covered y-distance)]
    (when
     (<= 0 x-distance) [[(dec (- sx x-distance)) y]
                        [(inc (+ sx x-distance)) y]])))

(defn- expand-position-range [[[^long x1 y] [^long x2 _]]]
  (mapv (fn [x] [x y]) (range (inc x1) x2)))

(defn day15-1
  ([sensors] (day15-1 sensors 2000000))
  ([sensors y]
   (count (transduce
           (comp
            (map (partial edge-positions y))
            (remove empty?)
            (mapcat expand-position-range)
            (remove (transduce (map :beacon) conj #{} sensors)))
           conj #{} sensors))))

;; Since there is only a single position the beacon can be placed, it has to be
;; at the edge of one of the sensors range, otherwise if the beacon was at
;; a position that is not at the edge of a sensors' range we would always
;; find at least one additional position the beacon could have been at.
;; This means we only need to search the positions on the edges to find the
;; beacon producing the distress signal

(defn beacon-candidates [sensors]
  (fn [y]
    (transduce
     (comp
      ;; In order for the position to be enveloped by the sensor ranges
      ;; we need at least two sensors' ranges that neighbour this position
      (filter (fn [[_ freq]] (< 1 freq)))
      (map first))
     conj [] (frequencies (mapcat (partial edge-positions y) sensors)))))

(defn day15-2
  ([sensors] (day15-2 sensors 0 4000000))
  ([sensors ^long min-pos ^long max-pos]
   (let [[^long x ^long y]
         (first
          (transduce
           (comp
            (mapcat (beacon-candidates sensors))
            (filter (fn [[x]] (< min-pos x max-pos)))
            (drop-while (covered-by-sensor? sensors))
            (take 1))
           conj []
           ;; The required constallation is less likely to happen on the border,
           ;; so we check these positions last (for the y-dimension at least)
           (interleave (range (quot max-pos 2) min-pos -1)
                       (range (inc (quot max-pos 2)) max-pos))))]
     (+ (* x 4000000) y))))