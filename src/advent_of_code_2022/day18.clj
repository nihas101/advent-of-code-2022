(ns advent-of-code-2022.day18
  (:require
   [advent-of-code-2022.utils :as u]))

(defn parse-input [input]
  (mapv (fn [pos] (mapv #(Long/parseLong %) (re-seq #"\d+" pos)))
        (u/split-sections input u/line-endings)))

(defn- neighbours [[^long x ^long y ^long z]]
  [[(dec x) y z] [(inc x) y z]
   [x (dec y) z] [x (inc y) z]
   [x y (dec z)] [x y (inc z)]])

(defn- lava-droplets-surface [lava-droplets]
  (transduce
   (comp
    (mapcat neighbours)
    (remove (set lava-droplets)))
   conj [] lava-droplets))

(defn day18-1 [lava-droplets]
  (count (lava-droplets-surface lava-droplets)))

(defonce ^:private min-max (juxt #(dec ^long (apply min %))
                                 #(inc ^long (apply max %))))

(defn- expand-water [water air lava-droplets]
  (transduce
   (comp
    (mapcat neighbours)
    (filter #(and (air %) (not (lava-droplets %)))))
   conj nil water))

(defn- fill-water [{:keys [water air]} lava-droplets]
  (let [lava-droplets (set lava-droplets)]
    (loop [water water
           air air
           total-water water]
      (if-let [exp-water (expand-water water air lava-droplets)]
        (recur exp-water
               (reduce disj air exp-water)
               (reduce conj total-water exp-water))
        total-water))))

(defn- init-water-area [[^long min-x ^long max-x]
                        [^long min-y ^long max-y]
                        [^long min-z ^long max-z]]
  (for [x (range min-x (inc max-x))
        y (range min-y (inc max-y))
        z (range min-z (inc max-z))]
    (if (or (#{min-x max-x} x)
            (#{min-y max-y} y)
            (#{min-z max-z} z))
      [:water [x y z]]
      [:air [x y z]])))

(defn- water-perimeter [x y z]
  (reduce (fn [w [st p]]
            (update w st (fnil conj #{}) p))
          {} (init-water-area x y z)))

(defn day18-2 [lava-droplets]
  (let [surface (lava-droplets-surface lava-droplets)
        x (min-max (mapv first surface))
        y (min-max (mapv second surface))
        z (min-max (mapv #(nth % 2) surface))
        water (fill-water (water-perimeter x y z) lava-droplets)]
    (count (filter (fn [pos] (some water (neighbours pos))) surface))))