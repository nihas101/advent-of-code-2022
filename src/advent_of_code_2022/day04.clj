(ns advent-of-code-2022.day04
  (:require
   [advent-of-code-2022.utils :as u]
   [clojure.string :as string]))

(defn- parse-sections [elf]
  (mapv #(Long/parseLong %) (string/split elf #"-")))

(defn parse-input [input]
  (mapv (fn [[elf-a elf-b]] [(parse-sections elf-a) (parse-sections elf-b)])
        (u/split-pairs input #",")))

(defn- section-contains? [[a-1 a-2] [b-1 b-2]]
  (<= a-1 b-1 b-2 a-2))

(defn- day04 [section-filter]
  (fn [elf-sections]
    (count
     (filterv (fn [[a b]] (or (section-filter a b)
                              (section-filter b a)))
              elf-sections))))

(defonce day04-1 (day04 section-contains?))

(defn- sections-overlap? [[a-1 a-2] [b-1 b-2]]
  (or (<= a-1 b-1 a-2)
      (<= a-1 b-2 a-2)))

(defonce day04-2 (day04 sections-overlap?))