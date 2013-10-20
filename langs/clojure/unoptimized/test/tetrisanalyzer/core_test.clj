(ns tetrisanalyzer.core-test
  (:require [expectations :refer :all])
  (:require [tetrisanalyzer.core :refer :all]))

(defn write-file! [content]
  (with-open [w (clojure.java.io/writer "C:/Clojure/tetris.txt")]
    (.write w content)))

(def empty-board (new-board 8 6))

;; Convert from piece index to character.
(expect \- (piece->char 0))
(expect \I (piece->char 1))
(expect \Z (piece->char 2))
(expect \S (piece->char 3))
(expect \J (piece->char 4))
(expect \L (piece->char 5))
(expect \T (piece->char 6))
(expect \O (piece->char 7))
(expect \x (piece->char 8))
(expect \# (piece->char 9))

;; Default board size is 10x20.
(expect (str (apply str (repeat 20 "#----------#\n")) "############")
        (board->str (new-board) 12))

(expect (str "#------#\n"
             "#------#\n"
             "#------#\n"
             "#------#\n"
             "#------#\n"
             "########")
        (board->str empty-board 8))

;; move-piece returns a list of pairs: [y x] p
;; that can be used by the function assoc
;; to put a piece on a board.
;;
;;              p v x y
;;  (move-piece 6 1 4 2)
;;
;;        123456
;;    0  #------#   (6 = T)
;;    1  #------#
;;    2  #---T--#   [2 4] 6
;;    3  #---TT-#   [3 4] 6 [3 5] 6
;;    4  #---T--#   [4 4] 6
;;       ########
(expect '([2 4] 6 [3 4] 6 [3 5] 6 [4 4] 6)
        (move-piece 6 1 4 2))

(expect (str "#------#\n"
             "#---ZZ-#\n"
             "#--ZZ--#\n"
             "#------#\n"
             "#------#\n"
             "########")
        (board->str (set-piece empty-board 2 0 3 1) 8))

