(ns advent-of-code-2022.day07
  (:require
   [advent-of-code-2022.utils :as u]
   [clojure.string :as string]))

(defn parse-input [input]
  (mapv #(u/split-sections % #"\s") (u/split-sections input #"\r?\n")))

(defn- change-dir [state d]
  (cond
    (string/starts-with? d "/") (assoc state :cd [d])
    (= d "..") (update state :cd #(vec (butlast %)))
    :else (update state :cd conj d)))

(defn- update-dir-sizes-fn [size]
  (let [size (Long/parseLong size)]
    (fn [s d] (update-in s [:ds d] (fnil + 0) size))))

(defn- add-file [{:keys [cd visited?] :as state} [size file-name]]
  (if (visited? cd)
    state
    (let [sub-directories (take-while seq (iterate butlast cd))
          updated-state (reduce (update-dir-sizes-fn size) state sub-directories)]
      (update updated-state :visited? conj (conj cd file-name)))))

(defn- parse-terminal-line [state [a b c :as to]]
  (cond
    (= b "cd") (change-dir state c)
    (= a "$") state
    (= a "dir") state
    :else (add-file state to)))

(defn- calculate-dir-sizes [terminal-output]
  (->> terminal-output
       (reduce parse-terminal-line {:cd [] :ds {} :visited? #{}})
       :ds))

(defn day07 [dir-sizes dir-size-pred f]
  (transduce
   (comp
    (map second)
    (filter dir-size-pred))
   f dir-sizes))

(defn day07-1 [terminal-output]
  (day07 (calculate-dir-sizes terminal-output) (partial >= 100000) +))

(defonce ^:private total-disk-space 70000000)
(defonce ^:private required-disk-space 30000000)

(defn day07-2 [terminal-output]
  (let [dir-sizes (calculate-dir-sizes terminal-output)
        unused-disk-space (- ^long total-disk-space ^long (get dir-sizes ["/"]))
        required-to-free (- ^long required-disk-space unused-disk-space)]
    (day07 dir-sizes (partial <= required-to-free)
           (fn
             ([] total-disk-space)
             ([a] a)
             ([^long a ^long b] (min a b))))))