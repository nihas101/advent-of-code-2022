(ns advent-of-code-2022.day17
  (:require
   [advent-of-code-2022.utils :as u]
   [clojure.string :as string]))

(defn- visualize-state
  ([state rock] (visualize-state (apply conj state rock)))
  ([state]
   (println (string/join
             (for [y (range (dec ^long (apply min 0 (mapv second state))) 1)
                   x (range 0 7)]
               (if (state [x y])
                 (str "#" (if (= x 6) "\n" ""))
                 (str "." (if (= x 6) "\n" ""))))))))

(defonce ^:private floor
  #{[0 0] [1 0] [2 0] [3 0] [4 0] [5 0] [6 0]})

(defonce ^:private rocks
  (cycle [;; 0123
          [0 [[0  0] [1  0] [2  0] [3  0]]]
          ;;.3.
          ;;014
          ;;.2.
          [1 [[0 -1] [1 -1] [1 0] [1 -2] [2 -1]]]
          ;;..4
          ;;..3
          ;;012
          [2 [[0  0] [1  0] [2  0] [2 -1] [2 -2]]]
          ;;3
          ;;2
          ;;1
          ;;0
          [3 [[0  0] [0 -1] [0 -2] [0 -3]]]
          ;;12
          ;;03
          [4 [[0  0] [0 -1] [1 -1] [1  0]]]]))

(defn parse-input [jet-pattern]
  (cycle (map-indexed (fn [idx sym] [idx sym]) (filter #{\> \<} jet-pattern))))

(defn- init-rock-position [state]
  (let [min-y (transduce (map second) min 1 state)]
    [2 (- ^long min-y 4)]))

(defonce ^:private reached-floor? some)

(defn- move-rock [rock [^long dx ^long dy]]
  (mapv (fn [[^long x ^long y]] [(+ x dx) (+ y dy)]) rock))

(defn- symbol->move [symbol]
  (cond
    (= symbol \>) [1 0]
    (= symbol \<) [-1 0]
    (= symbol \v) [0 1]
    :else (throw (ex-info "Unknown symbol" {:symbol symbol}))))

(defn- push-rock [rock state symbol]
  (let [move (symbol->move symbol)
        new-rock (move-rock rock move)
        sorted-rock-x (sort (mapv first new-rock))]
    (cond
      ;; We have reached the edge and cannot be pushed further in that direction
      (not (<= 0 (first sorted-rock-x) (last sorted-rock-x) 6)) [:falling rock]
      ;; We have moved inside another rock or reached the floow
      (reached-floor? state new-rock) [(if (= symbol \v) :rest :falling) rock]
      ;; Movement was valid
      :else [:falling new-rock])))

(defn- drop-rock [rock state jet-pattern]
  (let [move (init-rock-position state)]
    (loop [[rock-state rock] [:falling (move-rock rock move)]
           [[_ j] & jp :as jet-pattern] jet-pattern]
      #_(visualize-state state)
      (if (= rock-state :rest)
        [jet-pattern (reduce conj state rock)]
        (recur (push-rock rock state j)
               (if (#{\> \<} j) (conj jp [:dummy \v]) jp))))))

(defn drop-rocks [[[_ rock] & rocks :as rs] state ^long limit jet-pattern]
  (if (zero? limit)
    [rs state jet-pattern]
    (let [[jp st] (drop-rock rock state jet-pattern)]
      (recur rocks
             st
             (dec limit)
             jp))))

(defn- extract-window [state ^long window]
  (let [max-y (transduce (map second) min 0 state)
        min-y (+ ^long max-y window)]
    [(transduce
      (comp
       (filter (fn [[_ y]] (<= max-y y min-y)))
       (map (fn [[x ^long y]] [x (- y min-y)])))
      conj #{} state)
     max-y]))

(defn- rocks-height ^long [state]
  (- ^long (transduce (map second) min 0 state)))

(defn day17
  ([state [rocks jet-pattern] limit]
   (let [[_ state] (drop-rocks rocks state limit jet-pattern)]
     (rocks-height state)))
  ([state [rocks jet-pattern] ^long limit ^long window-size]
   (loop [[[[r-idx] :as rcks]
           state
           [[i-idx] :as inpt]] (drop-rocks rocks state window-size jet-pattern)
          lmt ^long (- limit window-size)
          history {}]
     (let [[window ^long height] (extract-window state window-size)
           lookup-key [window r-idx i-idx]]
       (cond
         (zero? lmt) (rocks-height state)
         (history lookup-key)
         (let [[^long rocks-before-cycles ^long h] (history lookup-key)
               cycle-length ^long (- rocks-before-cycles lmt)
               cycle-count ^long (quot rocks-before-cycles cycle-length)
               rocks-after-cycles ^long (rem rocks-before-cycles cycle-length)
               [_ st] (drop-rocks rocks floor (+ rocks-after-cycles (- limit rocks-before-cycles)) jet-pattern)
               additional-height (rocks-height st)]
           (+ additional-height (* cycle-count (- (- height h)))))
         :else (recur (drop-rocks rcks state 1 inpt)
                      (dec lmt)
                      (assoc history lookup-key [lmt height])))))))

(defn day17-1 [jet-pattern]
  (day17 floor [rocks jet-pattern] 2022))

(defn day17-2 [jet-pattern]
  (day17 floor [rocks jet-pattern] 1000000000000 10))