(ns advent-of-code-2022.day05
  (:require
   [advent-of-code-2022.utils :as u]
   [clojure.string :as string]))

(defn- split-stack-lines [crates-line]
  (rest
   (string/split
    (string/replace (string/replace crates-line #"\s{4}\s?" "[]")
                    #"\s+" "")
    #"\]\[|\[|\]")))

(defn- stack-line->crate+index [crates-line]
  (map-indexed (fn [^long idx crate] [(inc idx) crate]) crates-line))

(defn- conj-to-stacks
  ([] {})
  ([crate-stacks] crate-stacks)
  ([crate-stacks crates-line]
   (reduce (fn [cs [idx c]]
             (if (seq c)
               (update cs idx (fnil conj '()) c)
               cs))
           crate-stacks crates-line)))

(defn- parse-crate-stacks [crate-stacks]
  (transduce
   (comp
    (map split-stack-lines)
    (map stack-line->crate+index))
   conj-to-stacks
   (rest (reverse (u/split-sections crate-stacks u/line-endings)))))

(defn- parse-rearrangement-procedure [rearrangement-procedure]
  (mapv (fn [operation]
          (mapv #(Long/parseLong %)
                (rest (string/split operation #"move\s|\sfrom\s|\sto\s"))))
        (string/split rearrangement-procedure u/line-endings)))

(defn parse-input [input]
  (let [[crate-stacks rearrangement-procedure] (u/split-sections input)]
    {:stacks (parse-crate-stacks crate-stacks)
     :procedure (parse-rearrangement-procedure rearrangement-procedure)}))

(defn- day05 [crate-concat]
  (fn [{:keys [stacks procedure]}]
    (transduce
     (comp (map second)
           (map first))
     str
     (->> procedure
          (reduce (fn [stacks [q s t]]
                    (-> stacks
                        (update ,,, t crate-concat (take q (get stacks s)))
                        (update ,,, s (partial drop q))))
                  stacks)
          (sort-by first)))))

(defonce day05-1 (day05 into))

(defonce day05-2 (day05 (fn [stack crates] (concat crates stack))))