(ns advent-of-code-2022.day22
  (:require
   [advent-of-code-2022.utils :as u]
   [clojure.string :as string]
   [clojure.set :as set]))

(defn- parse-instructions [instructions]
  (mapv #(let [[m r] (re-seq #"\d+|[LR]" %)]
           [(Long/parseLong m) r])
        (re-seq #"\d+[LR]?" instructions)))

(defn- initial-position [positions]
  (apply min-key first
         (transduce
          (comp
           (remove (fn [[[_ ^long y] v]] (or (< 0 y) (= v \#))))
           (map first))
          conj [] positions)))

(defn- dissoc-position
  ([board] board)
  ([board [position]]
   (update-in board [:positions] dissoc position)))

(defn- parse-board [board]
  (let [{:keys [positions] :as b}
        (u/parse-positional-map board identity u/line-endings-without-trim)]
    (transduce
     (filter (fn [[_ v]] (= v \space)))
     dissoc-position b positions)))

(defn parse-input [input]
  (let [[board instructions] (u/split-sections input)
        {:keys [positions width height]} (parse-board board)]
    {:board positions
     :instructions (parse-instructions instructions)
     :position (initial-position positions)
     :width width
     :height height
     :facing ">"}))

(defn- visualize [{:keys [board position facing ^long width height] :as state}]
  (println
   (string/join
    (for [y (range height)
          x (range width)]
      (str
       (if (= [x y] position)
         facing
         (get board [x y] \space))
       (if (= x (dec width)) \newline "")))))
  state)

(defonce ^:private rotate-right
  {">" "v"
   "v" "<"
   "<" "^"
   "^" ">"})

(defonce ^:private rotate-left (set/map-invert rotate-right))

(defn- rotation [rot]
  (cond
    (= rot "R") rotate-right
    (= rot "L") rotate-left
    :else identity))

(defonce ^:private movement
  {">" [1  0]
   "v" [0  1]
   "<" [-1 0]
   "^" [0 -1]})

(defn- apply-rotation [state rot]
  (update state :facing (rotation rot)))

(defonce ^:private empty-space? #{\.})

(defonce ^:private wall? #{\#})

(defonce ^:private void? nil?)

(defn- apply-movement [move-position]
  (fn [state _]
    (let [[np facing npv] (move-position state)]
      (cond
        ;; Movement is accepted
        (empty-space? npv) (assoc state
                                  :position np
                                  :facing facing)
        ;; We have run into a wall
        (wall? npv) (reduced state)
        :else (do
                (visualize (assoc state :position np))
                (throw (ex-info "Unknown state"
                                {:position np
                                 :facing facing
                                 :value npv})))))))

(defn- apply-instruction [move-position]
  (let [apply-move (apply-movement move-position)]
    (fn [state [move rot]]
      (apply-rotation (reduce apply-move state (range move)) rot))))

(defonce ^:private facing->score
  {">" 0
   "v" 1
   "<" 2
   "^" 3})

(defn- wrap-around-position [{:keys [board facing]} [x y]]
  (cond
    (= facing ">") (apply min-key first
                          (filterv (fn [[_ yy]] (= y yy)) (keys board)))
    (= facing "v") (apply min-key second
                          (filterv (fn [[xx _]] (= x xx)) (keys board)))
    (= facing "<") (apply max-key first 
                          (filterv (fn [[_ yy]] (= y yy)) (keys board)))
    (= facing "^") (apply max-key second
                          (filterv (fn [[xx _]] (= x xx)) (keys board)))))

(defn- move-2d-position [{:keys [board position facing] :as state}]
  (let [[^long x ^long y] position
        [^long dx ^long dy] (movement facing)
        np [(+ x dx) (+ y dy)]
        npv (get board np)]
    (if (void? npv)
      (let [wp (wrap-around-position state np)]
        [wp facing (get board wp)])
      [np facing npv])))

(defn- password [^long x ^long y facing]
  (+ (* 1000 (inc y))
     (* 4 (inc x))
     ^long (facing->score facing)))

(defn day22-1 [{:keys [instructions] :as state}]
  (let [{[x y] :position facing :facing}
        (reduce (apply-instruction move-2d-position) state instructions)]
    (password x y facing)))

(defn- left+right [[^long x ^long y]]
  [[(dec x) y] [(inc x) y]])

(defn- up+down [[^long x ^long y]]
  [[x (dec y)] [x (inc y)]])

(defn- neighbour-positions [pos]
  (concat (left+right pos) (up+down pos)))

(defn- inner-corner? [board]
  (fn [corner]
    ;; Inner corners are those where every position is still inside the map
    ;; Otherwise we have found an outer corner (or are of the map)
    (let [nps (neighbour-positions corner)]
      (every? board nps))))

(defn- inner-corner-candidates [board]
  (fn [^long y]
    (let [line (filterv (fn [[_ ^long yy]] (= y yy)) (keys board))
          [min-x max-x] (u/min+max (mapv first line))]
      [[min-x (inc y)] [min-x (dec y)]
       [max-x (inc y)] [max-x (dec y)]])))

(defn- find-inner-corners [{:keys [board height]}]
  (transduce
   (comp
    (mapcat (inner-corner-candidates board))
    (filter (inner-corner? board)))
   conj [] (range height)))

(defn- edge-position? [board]
  (fn [np] (= 3 (count (filterv board (neighbour-positions np))))))

(defn- directions [[^long x ^long y :as pos] {:keys [board]}]
  (let [npos (neighbour-positions pos)]
    (transduce
     (comp
      (filter (edge-position? board))
      (map (fn [[^long xx ^long yy :as edge-pos]]
             [edge-pos [(- xx x) (- yy y)]])))
     conj [] npos)))

(defn- corner? [pos {:keys [board]}]
  (= 2 (count (filterv board (neighbour-positions pos)))))

(defn- move-along-corner [board [^long x ^long y :as pos] [^long dx ^long dy]]
  (first (transduce
          (comp
           ;; We remove the previous position we visited
           ;; to avoid moving backwards again
           (remove #{[(- x dx) (- y dy)]})
           ;; After filtering we should only be left with the 1 position on the
           ;; other side of the corner
           (filter board)
           (take 1))
          conj [] (neighbour-positions pos))))

(defn- move-along-edge [[[^long x ^long y :as pos]
                         [^long dx ^long dy :as direction]]
                        {:keys [board] :as state}]
  (if (corner? pos state)
    (let [[^long cx ^long cy :as c-pos] (move-along-corner board pos direction)]
      ;, We round the corner and thus change direction
      [c-pos [(- cx x) (- cy y)]])
    ;; We move along the edge in the same direction
    [[(+ x dx) (+ y dy)] direction]))

(def ^:private right [1 0])
(def ^:private down [0 1])
(def ^:private left [-1 0])
(def ^:private up [0 -1])

(defonce ^:private direction->facing
  {right ">"
   down "v"
   left "<"
   up "^"})

(defn- direction->normal-facing [[^long x ^long y :as pos]
                                 direction
                                 {:keys [board]}]
  ;; We look _into_ the cube face and _out_ of it to find the normal
  (let [[^long nx ^long ny] (first (remove board (if (#{up down} direction)
                                                   (left+right pos)
                                                   (up+down pos))))]
    (direction->facing [(- nx x) (- ny y)])))

(defn- flip [facing]
  (rotate-right (rotate-right facing)))

(defn- map-inner-corner-edges [state]
  (fn [mapping pos]
    (loop [mapping mapping
           [[a a-d] [b b-d] :as edge-positions] (directions pos state)]
      (let [normal-a (direction->normal-facing a a-d state)
            normal-b (direction->normal-facing b b-d state)
            new-mapping (assoc mapping
                               [a normal-a] [b (flip normal-b)]
                               [b normal-b] [a (flip normal-a)])]
        (cond
          ;; We have reached the edges which are not continuously connected
          (and (corner? a state) (corner? b state)) new-mapping
          ;; We found a corner in one direction that is still connected with
          ;; the edge in the other direction
          (corner? a state)
          (let [[bb bb-d] (move-along-edge [b b-d] state)
                normal-bb (direction->normal-facing bb bb-d state)
                second-normal-a (rotate-right normal-a)]
            (recur (assoc new-mapping
                          [a second-normal-a] [bb (flip normal-bb)]
                          [bb normal-bb] [a (flip second-normal-a)])
                   (mapv #(move-along-edge % state) [[a a-d] [bb bb-d]])))
          ;; We found a corner in one direction that is still connected with
          ;; the edge in the other direction
          (corner? b state)
          (let [[aa aa-d] (move-along-edge [a a-d] state)
                normal-aa (direction->normal-facing aa aa-d state)
                second-normal-b (rotate-right normal-b)]
            (recur (assoc new-mapping
                          [aa normal-aa] [b (flip second-normal-b)]
                          [b second-normal-b] [aa (flip normal-aa)])
                   (mapv #(move-along-edge % state) [[aa aa-d] [b b-d]])))
          ;; We are still zipping up the same edges
          :else (recur new-mapping
                       (mapv #(move-along-edge % state) edge-positions)))))))

(defn- build-mapping [inner-corners state]
  (reduce (map-inner-corner-edges state) {} inner-corners))

(defn- move-3d-position [edge-mapping]
  (fn [{:keys [board position facing]}]
    (let [[^long x ^long y :as pos] position
          [^long dx ^long dy] (movement facing)
          np [(+ x dx) (+ y dy)]
          npv (get board np)]
      (if (void? npv)
        (let [[wp new-facing] (edge-mapping [pos facing])]
          [wp new-facing (get board wp)])
        [np facing npv]))))

;; The idea is that we first find all inner corners of the opened cube.
;; The mapping going out from those is straightforward:
;;
;; gfe
;; ..d
;; ..c
;; ..b
;; ..a
;; ...abcdefg
;;
;; We continue along these edges until we round a corner in both directions
;; at the same time. Those edges can than no longer be matched up in this way
;; and 'belong' to another inner corner. One special case here is if we
;; round the corner in one direction and not the other. Since we have to
;; exit points (up/down and left/right) we need to take special care to
;; map these correctly to the other edge. This also means that it is not enough
;; to map edge-points to other edge-points, since such corners would break the
;; bijective mapping here.
;;
;; I solved this by including the outward facing direction for that position
;; as part of the mapping. This also allowed me to simply store our new
;; facing direction when moving from one face of the cube to another, since from
;; a 2D plane view we not only move across the edge into another face,
;; but also potentially rotate.
(defn day22-2 [{:keys [instructions] :as state}]
  (let [edge-mapping (build-mapping (find-inner-corners state) state)
        {[x y] :position
         facing :facing} (reduce (apply-instruction
                                  (move-3d-position edge-mapping))
                                 state instructions)]
    (password x y facing)))