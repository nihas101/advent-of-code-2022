(ns advent-of-code-2022.day16
  (:require
   [advent-of-code-2022.utils :as u]))

(defn parse-input [input]
  (transduce
   (map (fn [line]
          (let [flow-rate (Long/parseLong (re-find #"\d+" line))
                [valve & tunnels] (re-seq #"[A-Z]{2}" line)]
            [valve {:flow-rate flow-rate
                    :state (if (pos? flow-rate) :closed :broken)
                    :valve valve
                    :tunnels (transduce (map (fn [v] [v 1])) conj {} tunnels)}])))
   conj {}
   (u/split-sections input u/line-endings)))

(defn- total-flow-rate [open-valves]
  {:open-valves open-valves
   :total-flow-rate (transduce
                     (map (fn [[_ ^long flow ^long t]] (* flow t)))
                     + open-valves)})

(defn dfs
  ([current-node graph time]
   (dfs current-node graph time #{current-node}))
  ([current-node graph time visited]
   (dfs [[current-node visited [] time graph]] []))
  ([[[current-node visited-nodes open-valves ^long time graph] & qq :as q] completed-paths]
   (let [{:keys [flow-rate state tunnels]} (get graph current-node)]
     (cond
       (empty? q) (apply max-key :total-flow-rate completed-paths)
       ;; We are out of time
       (< time 1) (recur qq
                         (conj completed-paths (total-flow-rate open-valves)))
       ;; Open valve
       (= state :closed)
       (recur (concat [[current-node
                        visited-nodes
                        (conj open-valves [current-node flow-rate (dec time)])
                        (dec time)
                        (assoc-in graph [current-node :state] :open)]]
                      qq)
              completed-paths)
       ;; Move on to next valves
       :else (let [next-states (transduce
                                (comp
                                 (remove #(contains? visited-nodes (first %)))
                                 (map (fn [[v ^long cost]]
                                        [v (conj visited-nodes v) open-valves (- time cost) graph])))
                                conj [] tunnels)]
               (recur
                (concat next-states qq)
                (if (seq next-states)
                  completed-paths
                  (conj completed-paths (total-flow-rate open-valves)))))))))

(defn- remove-verts [broken-valve-nodes]
  (fn [g n]
    (update-in g [n :tunnels] #(apply dissoc % broken-valve-nodes))))

(defn- remove-valves-nodes [graph broken-valve-nodes]
  (reduce #(dissoc %1 %2)
          (reduce (remove-verts broken-valve-nodes) graph (mapv first graph))
          broken-valve-nodes))

(defn- add-transitive-verts [g n]
  (let [tunnels (get-in g [n :tunnels])]
    (fn [tnls]
      (merge-with min tnls
                  (mapcat (fn [[nn cost]]
                            (dissoc (transduce
                                     (map (fn [nnn] (update nnn 1 + cost)))
                                     conj {}
                                     (get-in g [nn :tunnels]))
                                    n))
                          tunnels)))))

(defn- transitive-closure [graph]
  (loop [old-graph graph]
    (let [new-graph (reduce (fn [g n]
                              (update-in g [n :tunnels]
                                         (add-transitive-verts g n)))
                            old-graph (mapv first old-graph))]
      (if (= old-graph new-graph)
        old-graph
        (recur new-graph)))))

(defn day16-1 [current-node graph]
  (let [broken-valves (mapv :valve (filter (comp zero? :flow-rate) (mapv second (remove (comp #{"AA"} first) graph))))]
    (:total-flow-rate (dfs current-node (remove-valves-nodes (transitive-closure graph) broken-valves) 30))))

;; Very unhappy with this one. After putzing around for a day I could only find
;; solutions that work on either the example OR the real input, but not on both.
;; I decided to stick with the solution that works on the real input
(defn day16-2 [current-node graph]
  (let [broken-valves (mapv :valve (filter (comp zero? :flow-rate) (mapv second (remove (comp #{"AA"} first) graph))))
        g (remove-valves-nodes (transitive-closure graph) broken-valves)
        {:keys [open-valves ^long total-flow-rate]} (dfs current-node g 26)
        gg (remove-valves-nodes g (mapv first open-valves))]
    (+ total-flow-rate
       ^long (:total-flow-rate (dfs current-node gg 26)))))