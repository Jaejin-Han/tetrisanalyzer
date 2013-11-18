(ns tetrisanalyzer.core)

;; ===== Pieces =====

(def pieces [ nil
  ;; I (1)
  [[[0 0] [1 0] [2 0] [3 0]]
   [[0 0] [0 1] [0 2] [0 3]]]

  ;; Z (2)
  [[[0 0] [1 0] [1 1] [2 1]]
   [[1 0] [0 1] [1 1] [0 2]]]

  ;; S (3)
  [[[1 0] [2 0] [0 1] [1 1]]
   [[0 0] [0 1] [1 1] [1 2]]]

  ;; J (4)
  [[[0 0] [1 0] [2 0] [2 1]]
   [[0 0] [1 0] [0 1] [0 2]]
   [[0 0] [0 1] [1 1] [2 1]]
   [[1 0] [1 1] [0 2] [1 2]]]

  ;; L (5)
  [[[0 0] [1 0] [2 0] [0 1]]
   [[0 0] [0 1] [0 2] [1 2]]
   [[2 0] [0 1] [1 1] [2 1]]
   [[0 0] [1 0] [1 1] [1 2]]]

  ;; T (6)
  [[[0 0] [1 0] [2 0] [1 1]]
   [[0 0] [0 1] [1 1] [0 2]]
   [[1 0] [0 1] [1 1] [2 1]]
   [[1 0] [0 1] [1 1] [1 2]]]

  ;; O (7)
  [[[0 0] [1 0] [0 1] [1 1]]]])

(def char->piece { \- 0 \I 1 \Z 2 \S 3 \J 4 \L 5 \T 6 \O 7 \x 8 \# 9 })

(defn piece->char [piece] (nth "-IZSJLTOx#" piece))

(defn rotate-and-move-piece [piece rotation x y]
  (mapcat (fn [[px py]] [[(+ y py) (+ x px)] piece]) ((pieces piece) rotation)))

;; ===== board =====

(defn- row->str [row]
  (apply str (map (fn [[_ piece]] (piece->char piece)) row)))

(defn board->str [board width]
  (->> board
       sort
       (partition width)
       (map row->str)
       (clojure.string/join "\n")))

(defn set-piece [board piece rotation x y]
  (apply assoc board (rotate-and-move-piece piece rotation x y)))

(defn- str->row [row y]
  (map-indexed #(vector [y %1] (char->piece %2)) row))

(defn new-board
  ([] (new-board 12 21))
  ([rows] (into {} (mapcat #(str->row %1 %2) rows (range))))
  ([width height]
    (into {} (for [y (range height) x (range width)
                   :let [wall? (or (zero? x) (= x (dec width)) (= y (dec height)))]]
                   [[y x] (if wall? 9 0)]))))


