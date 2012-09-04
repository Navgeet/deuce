(ns deuce.main
  (require [deuce.emacs]
           [deuce.emacs.data :as data]
           [deuce.emacs.eval :as eval]
           [deuce.emacs.lread :as lread])
  (import [java.util Stack])
  (:gen-class))

(defn -main [& args]
  (when-not (some #{"-batch" "--batch"} args)
    (println "Batch mode required, run with -batch or --batch")
    (System/exit 1))

  (let [args (doto (Stack.)
               (.addAll (reverse args)))
        pop (fn [opt]
              (if (empty? args)
                (do
                  (printf ("Option `%s' requires an argument\n" opt))
                  (System/exit 1))
                (.pop args)))
        option #(hash-set (str "-" %) (str "--" %))]

    (while (seq args)
      (let [opt (.pop args)]
        (condp some [opt]
          #{"--eval" "--execute"} (eval/eval (deuce.emacs.lread/read (pop opt)))
          (option "script") (lread/load (pop opt))
          (option "version") (do (printf "GNU Emacs %s\n" (data/symbol-value 'emacs-version))
                                 (printf "%s\n" (data/symbol-value 'emacs-copyright))
                                 (printf "GNU Emacs comes with ABSOLUTELY NO WARRANTY.\n")
                                 (printf "You may redistribute copies of Emacs\n")
                                 (printf "under the terms of the GNU General Public License.\n")
                                 (printf "For more information about these matters, ")
                                 (printf "see the file named COPYING.\n")
                                 (flush)
                                 (System/exit 0))
          (option "batch") nil
          (printf "Unknown option `%s'\n" opt))))))
