(ns advent-of-code-2022.day20)

(defn parse-input [input]
  (transduce
   (map-indexed vector)
   conj [] (read-string (str "[" input "]"))))

(defn- mix-index [^long val ^long idx ^long coll-count]
  (if (zero? val)
    idx
    (mod (+ val idx) (dec coll-count))))

(defn- mix [^java.util.LinkedList decrypted
           [_ val :as e] coll-count]
  (let [old-idx (.indexOf decrypted e)]
    (.remove decrypted e)
    (.add decrypted (mix-index val old-idx coll-count) e)
    decrypted))

(defn- linked-list [coll]
  (let [ll (java.util.LinkedList.)]
    (.addAll ll coll)
    ll))

(defn- decrypt
  ([encrypted]
   (mapv second (decrypt encrypted (linked-list encrypted))))
  ([encrypted decrypted]
   (reduce (fn [d e] (mix d e (count encrypted)))
           decrypted encrypted)))

(defn- decrypted-sum [^clojure.lang.PersistentVector decrypted]
  (let [start-index (.indexOf decrypted 0)]
    (transduce
     (comp
      (map #(mod (+ start-index %) (count decrypted)))
      (map decrypted))
     + [1000 2000 3000])))

(defonce day20-1 (comp decrypted-sum decrypt))

(defonce ^:private decryption-key 811589153)

(defn day20-2 [encrypted]
  (let [encrypted (mapv #(update % 1 * decryption-key) encrypted)
        decrypted (mapv second
                        (nth (iterate #(decrypt encrypted %)
                                      (linked-list encrypted))
                             10))]
    (decrypted-sum decrypted)))