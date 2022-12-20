(ns advent-of-code-2022.day11
  (:require
   [advent-of-code-2022.utils :as u]))

(defn- op [operation]
  (let [[a op b] (mapv read-string (drop 3 (u/split-sections operation #"\s+")))]
    (eval `(fn [~'old] (~op ~a ~b)))))

(defn- parse-monkey [[monkey items operation test true-case false-case]]
  {:monkey (Long/parseLong (re-find #"\d+" monkey))
   :items (mapv #(Long/parseLong %) (re-seq #"\d+" items))
   :operation (op operation)
   :test (Long/parseLong (re-find #"\d+" test))
   :true-case (Long/parseLong (re-find #"\d+" true-case))
   :false-case (Long/parseLong (re-find #"\d+" false-case))
   :inspected-items 0})

(defn parse-input [input]
  (mapv #(parse-monkey (u/split-sections % u/line-endings)) (u/split-sections input)))

(defn- monkey-target [{:keys [test true-case false-case]} new]
  (if (zero? ^long (mod new test)) true-case false-case))

(defn- inspect-item [worry-limiter {:keys [monkey operation] :as m}]
  (fn [ms item]
    (let [new (worry-limiter (operation item))
          target (monkey-target m new)]
      (-> ms
          (update-in ,,, [target :items] concat [new])
          (update-in ,,, [monkey :items] rest)
          (update-in ,,, [monkey :inspected-items] inc)))))

(defn- inspect-items [worry-limiter]
  (fn [ms monkey-idx]
    (let [{:keys [items] :as m} (get ms monkey-idx)]
      (reduce (inspect-item worry-limiter m) ms items))))

(defn- monkeying-a-round [monkeys worry-limiter]
  (reduce (inspect-items worry-limiter) monkeys (range (count monkeys))))

(defn- monkey-business [monkeys]
  (->> monkeys
       (mapv :inspected-items)
       (sort >)
       (take 2)
       (apply *)))

(defn day11-1
  ([monkeys] (day11-1 monkeys 20))
  ([monkeys ^long rounds]
   (if (zero? rounds)
     (monkey-business monkeys)
     (recur (monkeying-a-round monkeys #(quot ^long % 3))
            (dec rounds)))))

(defn- common-multiple ^long [monkeys]
  (reduce * (mapv :test monkeys)))

(defn day11-2
  ([monkeys] (day11-2 monkeys 10000))
  ([monkeys ^long rounds]
   (let [common-multiple (common-multiple monkeys)]
     (loop [monkeys monkeys
            rounds rounds]
       (if (zero? rounds)
         (monkey-business monkeys)
         (recur (monkeying-a-round monkeys #(mod ^long % common-multiple))
                (dec rounds)))))))