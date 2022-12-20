(ns advent-of-code-2022.day19
  (:require
   [advent-of-code-2022.utils :as u]))

(defn parse-input [input]
  (mapv (fn [blueprint]
          (let [[[plan]
                 [or-robot-cost]
                 [cl-robot-cost]
                 [ob-robot-ore-cost ob-robot-clay-cost]
                 [gd-robot-ore-cost gd-robot-obsidian-cost]]
                (mapv (fn [line] (mapv #(Long/parseLong %) (re-seq #"\d+" line)))
                      (u/split-sections blueprint #"\.|:"))]
            {:plan plan
             ;; robot -> build cost
             :ore {:ore or-robot-cost
                   :clay 0
                   :obsidian 0
                   :geode 0}
             :clay {:ore cl-robot-cost
                    :clay 0
                    :obsidian 0
                    :geode 0}
             :obsidian {:ore ob-robot-ore-cost
                        :clay ob-robot-clay-cost
                        :obsidian 0
                        :geode 0}
             :geode {:ore gd-robot-ore-cost
                     :clay 0
                     :obsidian gd-robot-obsidian-cost
                     :geode 0}
             ;; Precompute the max cost of all the interesting resources
             :max-cost {:ore (max or-robot-cost cl-robot-cost ob-robot-ore-cost gd-robot-ore-cost)
                        :clay ob-robot-clay-cost
                        :obsidian gd-robot-obsidian-cost}}))
        (u/split-sections input u/line-endings)))

(defn- mine-ore [robots resources]
  (merge-with + resources robots))

(defonce ^:private build-priority [:geode :obsidian :clay :ore :none])

(defn- clamp-resource ^long [^long resource ^long resource-cost ^long production-rate ^long min-left]
  (min resource (- (* resource-cost min-left)
                   (* production-rate (dec min-left)))))

(defn- branch-score-with-weight ^long [[_ resources ^long minutes-left]]
  (+ ^long (:geode resources)
     (quot (* (quot minutes-left 2) (dec (quot minutes-left 2))) 2)))

(defn- still-worth-building? [robots output-limit robot]
  (or (#{:geode :none} robot)
      (< ^long (robot robots)
         ^long (robot output-limit))))

(defn- subtract-build-cost [factory resources robot]
  [robot (merge-with - resources (factory robot))])

(defn- build-over-budget? [[_ resources-if-built]]
  (some (comp neg? second) resources-if-built))

(defn- build+mine [robots min-left [robot resources-if-built]]
  [(if (= robot :none) robots (update robots robot inc))
   (mine-ore robots resources-if-built)
   min-left])

;; States where we have more of some resource than we could ever spend
;; should be treated the same. So we clamp the resources for the state space.
;; E.g. we create more ore per minute than we can build ore robots with already.
(defn- clamp-resources
  ([factory [rob res mnts]]
   [rob (clamp-resources factory rob res mnts) mnts])
  ([{:keys [max-cost]} robots resources ^long min-left]
   (-> resources
       (update ,,, :ore clamp-resource
                   (max-cost :ore)
                   (:ore robots) min-left)
       (update ,,, :clay clamp-resource
                   (max-cost :clay)
                   (:clay robots) min-left)
       (update ,,, :obsidian clamp-resource
                   (max-cost :obsidian)
                   (:obsidian robots) min-left))))

(defn- build-robot+mine-ore [{:keys [max-cost] :as factory}
                             [robots resources ^long minutes]
                             visited ^long best-branch-score]
  (transduce
   (comp
    (filter (partial still-worth-building? robots max-cost))
    (map (partial subtract-build-cost factory resources))
    (remove build-over-budget?)
    (map (partial build+mine robots (dec minutes)))
    (filter #(<= best-branch-score (branch-score-with-weight %)))
    (map (partial clamp-resources factory))
    (remove visited))
   conj [] build-priority))

(defonce ^:private init-robots
  {:ore 1
   :clay 0
   :obsidian 0
   :geode 0})

(defonce ^:private init-ore
  {:ore 0
   :clay 0
   :obsidian 0
   :geode 0})

(defn- max-resources ^long
  ([] 0)
  ([max-geodes [_ resources]]
   (max max-geodes ^long (:geode resources))))

(defn- continue? [[_ _ ^long min-left]]
  (pos? min-left))

(defn- branch-score ^long
  ([] 0)
  ([[_ resources]] (:geode resources)))

(defn- day19 [blueprints robots resources minutes quality-fn reducing-fn]
  (reduce reducing-fn
          (pmap
           (fn [{:keys [^long plan] :as blueprint}]
             (let [factory (dissoc blueprint :plan)
                   neighbours (fn [curr visited best-branch-score]
                                (build-robot+mine-ore factory curr visited
                                                      best-branch-score))]
               (quality-fn plan
                           (u/pruning-bfs [robots resources minutes]
                                          neighbours identity
                                          branch-score continue? max-resources))))
           blueprints)))

(defn day19-1
  ([blueprints] (day19-1 blueprints init-robots init-ore 24))
  ([blueprints robots resources minutes]
   (day19 blueprints robots resources minutes * +)))

(defn day19-2
  ([blueprints] (day19-2 (take 3 blueprints) init-robots init-ore 32))
  ([blueprints robots resources minutes]
   (day19 blueprints robots resources minutes (fn [_ geodes] geodes) *)))