(ns deuce.main
  (:require [deuce.emacs]
            [deuce.emacs-lisp :as el]
            [deuce.emacs.alloc :as alloc]
            [deuce.emacs.buffer :as buffer]
            [deuce.emacs.data :as data]
            [deuce.emacs.editfns :as editfns]
            [deuce.emacs.eval :as eval]
            [deuce.emacs.frame :as frame]
            [deuce.emacs.lread :as lread]
            [deuce.emacs.window :as window]
            [deuce.emacs.xdisp :as xdisp]
            [taoensso.timbre :as timbre])
  (:gen-class))

(defn swank [port]
  (require 'swank.swank)
  (with-out-str
    ((resolve 'swank.swank/start-repl) port))
  (println "Swank connection opened on" port))

(defn nrepl [port]
  (require 'clojure.tools.nrepl.server)
  (with-out-str
    ((resolve 'clojure.tools.nrepl.server/start-server) :port port))
  (println "nrepl server listening on" port))

(defn display-state-of-emacs []
  (doseq [frame (frame/frame-list)]
    (println "---------------" frame
             (if (= frame (frame/selected-frame)) "--- [selected frame]" "")))
  (doseq [window (window/window-list nil true)
          :let [buffer (window/window-buffer window)]]
    (println "---------------" window
             (if (= window (window/selected-window)) "--- [selected window]" ""))
    (when-not (re-find #" \*" (buffer/buffer-name buffer))
      (println (xdisp/format-mode-line (buffer/buffer-local-value 'mode-line-format buffer) nil window buffer))))
  (doseq [buffer (buffer/buffer-list)
          :let [name (buffer/buffer-name buffer)
                messages? (= name "*Messages*")]]
    (println "---------------" buffer
             (cond
              (= name (buffer/buffer-name)) "--- [current buffer]"
              messages? "--- [see stdout above]"
              :else ""))
    (when-not messages?
      (println (str (.beg (.own-text buffer)))))))

;; We want to support emacs -q initially. -q is --no-init-file
(defn -main [& args]
  (let [option #(hash-set (str "-" %) (str "--" %))
        inhibit-window-system (atom nil)
        args (map
              #(condp some [%]
                 (option "script") "-scriptload"
                 (option "version") (do (printf "GNU Emacs %s\n" (data/symbol-value 'emacs-version))
                                        (printf "%s\n" (data/symbol-value 'emacs-copyright))
                                        (printf "GNU Emacs comes with ABSOLUTELY NO WARRANTY.\n")
                                        (printf "You may redistribute copies of Emacs\n")
                                        (printf "under the terms of the GNU General Public License.\n")
                                        (printf "For more information about these matters, ")
                                        (printf "see the file named COPYING.\n")
                                        (flush)
                                        (System/exit 0))
                 (option "batch") (do (el/setq noninteractive true) nil)
                 (option "swank-clojure") (swank 4005)
                 (option "nrepl") (nrepl 7888)
                 #{"-nw" "--no-window-system,"} (do (reset! inhibit-window-system true))
                 %) args)]

    (el/setq command-line-args (alloc/cons "src/bootstrap-emacs" (apply alloc/list (remove nil? args))))

    (when-not (data/symbol-value 'noninteractive)
      ;; Emacs opens the terminal before loadup.
      ;; In temacs you get an empty frame, with the actual load echoed to the Echo Area.
      ;; The mode line is the default (just dashes), the cursor is top left, and there's no menu bar.
      ;; This means we are a bit too eager in deuce.emacs setting up the *scratch* buffer.
      ;; inhibit-window-system could maybe be used to switch between :text and :swing in lanterna.
      )

    (lread/load "deuce-loadup.el")
    ;; Dump the current buffers etc. to stdout until we have display. *Messages* is already echoed to stdout.
    (display-state-of-emacs)

    ;; Pontentially call out and init the clojure-lanterna terminal (when-not inhibit-window-system)
    ;; startup.el may take care of this indirectly and make the callback for us.
    ))
