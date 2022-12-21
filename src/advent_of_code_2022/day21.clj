(ns advent-of-code-2022.day21
  (:require
   [advent-of-code-2022.utils :as u]))

(defn- parse-assignments [assignments]
  (reduce (fn [assgnmnts [monkey value]]
            (assoc assgnmnts monkey (Long/parseLong value)))
          {} assignments))

(defn- parse-expressions [expressions]
  (reduce (fn [exprs [monkey left op right]]
            (assoc exprs monkey {:left left
                                 :op op
                                 :right right}))
          {} expressions))

(defn parse-input [input]
  (let [{assignments 2
         expressions 4}
        (group-by count
                  (mapv #(u/split-sections % #"(:\s+)|\s+")
                        (u/split-sections input u/line-endings)))]
    {:assignments (parse-assignments assignments)
     :expressions (parse-expressions expressions)}))

(defn- initial-dependencies [{:keys [assignments expressions]}]
  (let [ass (transduce (map first) conj #{} assignments)]
    (reduce (fn [deps [monkey {:keys [left right]}]]
              (assoc deps monkey (transduce (remove ass) conj #{} [left right])))
            {} expressions)))

(defn- determine-eval-order [direct-dependencies trans-dependencies]
  (loop [eval-order []
         trans-dependencies trans-dependencies
         direct-dependencies direct-dependencies]
    (if (empty? trans-dependencies)
      eval-order
      (let [next-order (transduce
                        (filter (fn [dep]
                                  (empty? (get direct-dependencies dep))))
                        conj [] trans-dependencies)]
        (recur (into eval-order next-order)
               (apply disj trans-dependencies eval-order)
               (reduce (fn [dd m]
                         (reduce (fn [d k] (update d k disj m)) dd (keys dd)))
                       (apply dissoc direct-dependencies eval-order)
                       next-order))))))

(defonce op->func
  {"+" +
   "-" -
   "*" *
   "/" quot})

(defn- evaluate-monkey [expressions]
  (fn [ass mnky]
    (let [{:keys [left op right] :as expr} (expressions mnky)]
      (if expr
        (assoc ass mnky ((op->func op) (ass left) (ass right)))
        ass))))

(defn- evaluate [monkey {:keys [assignments expressions]} direct-dependencies trans-dependencies]
  (get
   (reduce (evaluate-monkey expressions)
           assignments
           (conj (determine-eval-order direct-dependencies trans-dependencies)
                 monkey))
   monkey))

(defn day21-1 [monkeys]
  (let [direct-dependencies (initial-dependencies monkeys)
        root-dependencies ((u/transitive-closure direct-dependencies) "root")]
    (evaluate "root" monkeys direct-dependencies root-dependencies)))

(defn- evaluable? [expressions sub-expressions]
  ((complement
    (transduce
     (comp
      (map expressions)
      (mapcat (juxt :left :right)))
     conj (set sub-expressions) sub-expressions))
   "humn"))

(defonce op->inverse-func
  {"+" -
   "-" +
   "*" quot
   "/" *})

(defn- rearrange-calc [operation c a operand-to-solve]
  (cond
    ;; c = a + b -> b = c - a
    ;; c = a * b -> b = c / a
    (#{"+" "*"} operation) ((op->inverse-func operation) c a)
    ;; c = b - a -> b = c + a
    ;; c = b / a -> b = c * a
    (and (#{"/" "-"} operation)
         (= operand-to-solve :left)) ((op->inverse-func operation) c a)
    ;; c = a - b -> b = a - c
    ;; c = a / b -> b = a / c
    (and (#{"/" "-"} operation)
         (= operand-to-solve :right)) ((op->func operation) a c)
    :else (throw (ex-info "Unknown state" {:op operation
                                           :c c
                                           :a a
                                           :to-solve operand-to-solve}))))

(defn- evaluable-subtree [{:keys [left right]}
                          expressions
                          trans-dependencies]
  (cond
    (evaluable? expressions (conj (trans-dependencies left) left)) :left
    (evaluable? expressions (conj (trans-dependencies right) right)) :right
    :else (throw (ex-info "No subtree is evaluable"
                          {:left left :right right}))))

(defn day21-2 [monkeys]
  (let [{:keys [expressions] :as monkeys} (assoc-in monkeys
                                                    [:assignments "humn"]
                                                    :humn)
        direct-dependencies (initial-dependencies monkeys)
        trans-dependencies (u/transitive-closure direct-dependencies)
        {:keys [left right] :as root} (get expressions "root")
        eval-subtree (evaluable-subtree root
                                        expressions trans-dependencies)
        [l r] (if (= eval-subtree :left) [left right] [right left])]
    (loop [res (evaluate l monkeys direct-dependencies (trans-dependencies l))
           {:keys [left op right] :as expr} (get expressions r)]
      (let [eval-subtree (evaluable-subtree expr expressions trans-dependencies)]
        (cond
          ;; We have reached the leaf with the variable
          ;; and have determined its value
          (nil? left) res
          ;; The operation in the right subtree needs to be rearranged
          (= eval-subtree :left)
          (recur (rearrange-calc op res
                                 (evaluate left monkeys
                                           direct-dependencies
                                           (trans-dependencies left))
                                 :right)
                 (get expressions right))
          ;; The operation in the left subtree needs to be rearranged
          (= eval-subtree :right)
          (recur (rearrange-calc op res
                                 (evaluate right monkeys
                                           direct-dependencies
                                           (trans-dependencies right))
                                 :left)
                 (get expressions left))
          ;; No subtree can be evaluated
          :else (throw (ex-info "Cannot evaluate any further"
                                {:left left :right right :res res})))))))