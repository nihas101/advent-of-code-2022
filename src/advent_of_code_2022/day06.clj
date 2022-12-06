(ns advent-of-code-2022.day06)

(defn- day06 [^long marker-size]
  (fn [datastream]
    (reduce (fn [^long idx possible-marker]
              (if (apply distinct? possible-marker)
                (reduced (+ idx marker-size))
                (inc idx)))
            0 (partition-all marker-size 1 datastream))))

(defonce day06-1 (day06 4))

(defonce day06-2 (day06 14))