(ns advent-of-code-2022.day10
  (:require
   [advent-of-code-2022.utils :as u]))

(defonce ^:private device {:x 1
                           :cycle 0
                           :image ""
                           :signal-strengths 0})

(defn parse-input [input]
  (mapv #(u/split-sections % #"\s") (u/split-sections input u/line-endings)))

(defn- signal-strength [{:keys [^long x ^long cycle] :as state}]
  (if (zero? ^long (mod (- cycle 20) 40))
    (update state :signal-strengths + (* x cycle))
    state))

(defn- execute-instruction [run-cycle state [op v]]
  (cond
    (= op "addx") (-> state
                      run-cycle
                      run-cycle
                      (update ,,, :x + (Long/parseLong v)))
    (= op "noop") (run-cycle state)
    :else (throw (ex-info "Unknown operation" {:operation op :state state}))))

(defn- calculate-signal-strength [state]
  (-> state
      (update ,,, :cycle inc)
      signal-strength))

(defn day10 [instructions run-cycle]
  (reduce (partial execute-instruction
                   run-cycle)
          device instructions))

(defn day10-1 [instructions]
  (:signal-strengths (day10 instructions calculate-signal-strength)))

(defn- draw-pixel [{:keys [^long x cycle] :as state}]
  (let [pos (mod cycle 40)
        pixel (if (<= (dec x) pos (inc x)) "#" ".")]
    (update state :image str pixel (if (= pos 39) \newline ""))))

(defn- shoot-ray [state]
  (-> state
      draw-pixel
      (update ,,, :cycle inc)))

(defn day10-2 [instructions]
  (:image (day10 instructions shoot-ray)))