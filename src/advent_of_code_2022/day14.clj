(ns advent-of-code-2022.day14
  (:require
   [advent-of-code-2022.utils :as u]))

(defn- expand-rock [rock-lines]
  (mapcat (fn [[[^long x1 ^long y1 :as p1] [^long x2 ^long y2 :as p2] :as rock-line]]
            (cond
              (and (not= x1 x2) (= y1 y2)) (mapv (fn [x] [x y1]) (range (min x1 x2) (inc (max x1 x2))))
              (and (= x1 x2) (not= y1 y2)) (mapv (fn [y] [x1 y]) (range (min y1 y2) (inc (max y1 y2))))
              (and (= x1 x2) (= y1 y2)) rock-line
              :else (throw (ex-info "Coordinates are not a straight line" {:p1 p1
                                                                           :p2 p2}))))
          (partition 2 1 rock-lines)))


(defn parse-input [input]
  (let [rock-points (mapv (fn [lines] (mapv (fn [xy] (u/read-longs xy #"\,"))
                                            (u/split-sections lines #"\s*->\s*")))
                          (u/split-sections input u/line-endings))]
    (transduce
     (mapcat expand-rock)
     conj #{} rock-points)))

(defn- drop-sand [{:keys [rocks sand]} ^long out-of-bounds [x y] oob-behavior]
  (loop [[^long x ^long y] [x y]]
    (let [below [x (inc y)]
          left [(dec x) (inc y)]
          right [(inc x) (inc y)]]
      (cond
        (sand [x y]) nil ;; Source is blocked
        (<= out-of-bounds y) (oob-behavior [x y])
        (and (not (rocks below)) (not (sand below))) (recur below)
        (and (not (rocks left)) (not (sand left))) (recur left)
        (and (not (rocks right)) (not (sand right))) (recur right)
        :else [x y]))))

(defn day14 [out-of-bounds oob-behavior]
  (fn [rocks]
    (let [oob (out-of-bounds rocks)]
      (loop [state {:rocks rocks
                    :sand #{}}]
        (if-let [sand-unit (drop-sand state oob [500 0] oob-behavior)]
          (recur (update state :sand conj sand-unit))
          (count (:sand state)))))))

(defonce day14-1 (day14 #(apply max (mapv second %))
                        (constantly nil)))

(defonce day14-2 (day14 #(inc ^long (apply max (mapv second %)))
                        identity))