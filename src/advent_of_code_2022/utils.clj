(ns advent-of-code-2022.utils
  (:require
   [clojure.string :as string]))

;; Trees

(defn internal-node [left right]
  {:left left
   :right right})

(defn leaf-node [value]
  {:value value})

(defn tree->vec [{:keys [left right value]}]
  (or value
      [(tree->vec left) (tree->vec right)]))

;; Map

(defn remove-vals [pred m]
  (apply dissoc m (mapv first (remove pred m))))

;; Math

(defn abs ^long [^long x] (max x (- x)))

(defn extended-gcd
  "https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm"
  [^long a ^long b]
  (loop [[o-r r] [(long a) (long b)]
         [o-s s] [(long 1) (long 0)]
         [o-t t] [(long 0) (long 1)]]
    (if (zero? r)
      {:x o-s :y o-t
       :gcd o-r
       :quots [t s]}
      (let [q (quot o-r r)]
        (recur [r (- ^long o-r (* ^long q ^long r))]
               [s (- ^long o-s (* ^long q ^long s))]
               [t (- ^long o-t (* ^long q ^long t))])))))

;; The unusual definition of the lcm is so that it can be used within a transducer
(defn lcm
  ([] nil)
  ([x] x)
  ([a b]
   (if (nil? a)
     b
     (quot (* ^long a ^long b) ^long (:gcd (extended-gcd a b))))))

(defn crt
  "https://brilliant.org/wiki/chinese-remainder-theorem/"
  [as+nz]
  (let [as (mapv first as+nz)
        nz (mapv second as+nz)
        N (reduce * nz)
        ys (mapv (partial / N) nz)
        zs (mapv :x (mapv extended-gcd ys nz))]
    (mod (abs (reduce + (mapv * as ys zs))) N)))

;; Strings

(defn split-sections
  ([s]
   (string/split s #"(\r?\n){2,}"))
  ([s re]
   (string/split s re)))

(def line-endings #"\r?\n")

(defn split-pairs
  ([s]
   (split-pairs s #"\s"))
  ([s del]
   (mapv #(string/split % del) (string/split s line-endings))))

(defn read-longs [s split-on]
  (mapv #(Long/parseLong %) (string/split (string/trim s) split-on)))

;; Input parsing

(defn- vals->pos+val [height-lines]
  (mapcat (fn [hs y] (mapv (fn [h x] [[x y] (Long/parseLong (str h))])
                           hs (range)))
          height-lines (range)))

(defn parse-positional-map [height-map]
  (let [height-lines (string/split height-map line-endings)]
    (reduce conj {:width (count (first height-lines))
                  :height (count height-lines)}
            (vals->pos+val height-lines))))