(ns advent-of-code-2022.day24
  (:require
   [advent-of-code-2022.utils :as u]
   [clojure.string :as string]))

(defn- row? [y]
  (fn wall? [[[_ yy]]] (= yy y)))

(defn- floor? [[_ v]]
  (= v \.))

(defn- hole-in-wall [y positions]
  (first
   (transduce
    (comp
     (filter (row? y))
     (filter floor?)
     (take 1)
     (map first))
    conj [] positions)))

(defn- blizzard? [[_ v]]
  (#{\> \v \< \^} v))

(defn- wall? [[_ v]]
  (= v \#))

(defn- floor [positions blizzard-positions wall-positions]
  (reduce dissoc
          (reduce (fn [ps [p]] (assoc ps p \.))
                  positions blizzard-positions)
          wall-positions))

(defn- blizzards [blizzard-positions]
  (reduce (fn [ps [p v]] (assoc ps p [v]))
          {} blizzard-positions))

(defn parse-input [input]
  (let [{:keys [^long height positions]} (u/parse-positional-map input identity)
        blizzard-positions (filterv blizzard? positions)
        wall-positions (mapv first (filterv wall? positions))]
    {:floor (floor positions blizzard-positions wall-positions)
     :blizzards (blizzards blizzard-positions)
     :start (hole-in-wall 0 positions)
     :goal (hole-in-wall (dec height) positions)}))

(defn- possible-moves [[^long x ^long y]]
  [[x (dec y)]
   [(dec x) y]
   [x y]
   [(inc x) y]
   [x (inc y)]])

(defonce ^:private blizzard-step
  {\> [1 0]
   \v [0 1]
   \< [-1 0]
   \^ [0 -1]})

(defn- row-matches [by]
  (fn row-matches [[_ y]] (= y by)))

(defn- ys [by floor]
  (transduce
   (comp
    (map first)
    (filter (row-matches by))
    (map first))
   conj [] floor))

(defn- column-matches [bx]
  (fn column-matches [[x]] (= x bx)))

(defn- xs [bx floor]
  (transduce
   (comp
    (map first)
    (filter (column-matches bx))
    (map second))
   conj [] floor))

(defn- blizzard-spawn-location [floor]
  (memoize
   (fn blizzard-spawn-location [[bx by] bliz-dir]
     (cond
       (= \> bliz-dir) [(apply min (ys by floor)) by]
       (= \< bliz-dir) [(apply max (ys by floor)) by]
       (= \v bliz-dir) [bx (apply min (xs bx floor))]
       (= \^ bliz-dir) [bx (apply max (xs bx floor))]
       :else (throw (ex-info "Unknown blizzard direction"
                             {:position [bx by]
                              :direction bliz-dir}))))))

(defn- move-blizzard [blizzard-spawn floor]
  (fn move-blizzard [[^long x ^long y :as og-pos] bliz-dir]
    (let [[^long dx ^long dy] (blizzard-step bliz-dir)
          bliz-pos [(+ x dx) (+ y dy)]]
      (if (floor bliz-pos)
        [bliz-pos bliz-dir]
        [(blizzard-spawn og-pos bliz-dir) bliz-dir]))))

(defn- move-blizzards [move-blizzard blizzards]
  (transduce
   (mapcat (fn [[p blizs]] (mapv #(move-blizzard p %) blizs)))
   (fn
     ([blizs] blizs)
     ([blizs [pos bliz]] (update blizs pos (fnil conj []) bliz)))
   {} blizzards))

(defn- next-positions [floor pos blizzards]
  (transduce
   (comp
    (filter floor)
    (remove blizzards))
   conj [] (possible-moves pos)))

(defn- visualize [floor positions blizzards]
  (let [max-y (apply max (mapv second (keys floor)))
        max-x (inc (apply max (mapv first (keys floor))))]
    (println
     (string/join
      (for [y (range (inc max-y))
            x (range (inc max-x))
            :let [pos [x y]]]
        (str
         (cond
           (blizzards pos) (count (blizzards [x y]))
           (positions pos) "X"
           (floor pos) "."
           :else "#")
         (if (= x max-x) "\n" "")))))))

(defn- shortest-path-length [positions goal floor [blizzards ^long minutes]]
  (let [blizzard-spawn (blizzard-spawn-location floor)
        move-blizzard (move-blizzard blizzard-spawn floor)]
    (loop [blizzards (move-blizzards move-blizzard blizzards)
           positions positions
           minutes (inc minutes)]
      #_(visualize floor positions blizzards)
      (let [next-positions (transduce
                            (mapcat #(next-positions floor % blizzards))
                            conj #{} positions)]
        (if (next-positions goal)
          [blizzards minutes]
          (recur (move-blizzards move-blizzard blizzards)
                 next-positions
                 (inc minutes)))))))

(defn day24-1 [{:keys [floor blizzards start goal]}]
  (-> #{start}
      (shortest-path-length ,,, goal floor [blizzards 0])
      second))

(defn day24-2 [{:keys [floor blizzards start goal]}]
  (second
   (reduce (fn next-expedition [[b m] [s g]]
             (shortest-path-length s g floor [b m]))
           [blizzards 0]
           [[#{start} goal] [#{goal} start] [#{start} goal]])))