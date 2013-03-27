(ns deuce.main
  (:require [clojure.string :as s]
            [clojure.java.io :as io]
            [lanterna.screen :as sc]
            [lanterna.terminal :as te]
            [deuce.emacs]
            [deuce.emacs-lisp :as el]
            [deuce.emacs-lisp.globals :as globals]
            [deuce.emacs.alloc :as alloc]
            [deuce.emacs.buffer :as buffer]
            [deuce.emacs.data :as data]
            [deuce.emacs.editfns :as editfns]
            [deuce.emacs.eval :as eval]
            [deuce.emacs.fns :as fns]
            [deuce.emacs.frame :as frame]
            [deuce.emacs.keymap :as keymap]
            [deuce.emacs.keyboard :as keyboard]
            [deuce.emacs.lread :as lread]
            [deuce.emacs.terminal :as terminal]
            [deuce.emacs.window :as window]
            [deuce.emacs.xdisp :as xdisp]
            [taoensso.timbre :as timbre]
            [dynapath.util :as dp])
  (:import [java.io FileNotFoundException InputStreamReader]
           [java.awt Toolkit]
           [java.awt.datatransfer DataFlavor StringSelection])
  (:gen-class))

;; 2013-03-27: To monitor keyboard events, call start-input-loop, then type keys in the Deuce window.
;;             They won't do anything, but will log out what it thinks it should be doing to the REPL.

;; Start Deuce like this: lein trampoline run -q --swank-clojure

;; Connect to Swank/nREPL from Emacs:

;; user> (in-ns 'deuce.emacs)  ;; We're now in Emacs Lisp

;; (switch-to-buffer "*Messages*") ;; Shows the boot messages, Loading ...etc.
;; (switch-to-buffer "*scratch*") ;; Displays *scratch*
;; (insert "Deuce is (not yet) Emacs under Clojure") ;; Insert some text.
;; (beginning-of-line) ;; Ctrl-a
;; (kill-line) ;; Kill the line.
;; (yank) ;; Yank it back.
;; (forward-line -4) ;; C-u 4 up
;; (read-extended-command) ;; Activates Minibuffer, but it cannot do anything
;; (select-window (frame-root-window)) ;; Get out of the minibuffer.
;; (set-mark-command nil) ;; Activate the mark
;; (forward-line) ;; Select some text.
;; (pop-mark) ;; Remove mark / Deselect.
;; (find-file "~/.bashrc") ;; Open a file.
;; (switch-to-buffer "*Deuce*") ;; The debug log

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

;; The way this does this is probably utterly wrong, written by data inspection, not reading Emacs source.
;; But produces the expected result:
(defn render-menu-bar []
  (when (data/symbol-value 'menu-bar-mode)
    (let [map-for-mode #(let [map (symbol (str % "-map"))]
                          (when (data/boundp map)
                            (data/symbol-value map)))
          menu-name #(last %)
          ;; The consp check here is suspicious.
          ;; There's a "menu-bar" string in there which probably shouldn't be.
          menus-for-map #(map menu-name (filter data/consp (fns/nthcdr 2 (fns/assq 'menu-bar %))))
          menu-bar-by-name #(data/symbol-value (symbol (str "menu-bar-" %)))
          global-map (data/symbol-value 'global-map)
          major-mode-map (map-for-mode (data/symbol-value 'major-mode))
          minor-mode-maps (map map-for-mode (data/symbol-value 'minor-mode-list))
          final-items (map menu-bar-by-name (data/symbol-value 'menu-bar-final-items))
          menu-bar (concat (mapcat menus-for-map (concat [global-map major-mode-map] minor-mode-maps))
                           (map menu-name final-items))]
      (s/join " " menu-bar))))

;; Renders a single window using Lanterna. Scrolling is not properly taken care of.
;; Hard to bootstrap, requires fiddling when connected to Swank inside Deuce atm.
;; Consider moving all this into deuce.emacs.dispnew
(declare screen)

(def colors {:bg :default :fg :default})
(def reverse-video {:styles #{:reverse}})
(def region-colors {:fg :default :bg :yellow})

(defn puts
  ([x y s] (puts x y s colors))
  ([x y s opts] (sc/put-string screen x y (str s) opts)))

(defn pad [s cols]
  (format (str "%-" cols "s") s))

;; If the screen gets messed up by other output like a stack trace you need to call this.
(defn blank
  ([] (apply blank (sc/get-size screen)))
  ([width height]
     (sc/clear screen)
     (te/clear (.getTerminal screen))
     (sc/redraw screen)))

(doseq [f '[line-indexes pos-to-line point-coords]]
  (eval `(def ~f (ns-resolve 'deuce.emacs.cmds '~f))))

(defn render-live-window [window]
  (let [buffer (window/window-buffer window)
        minibuffer? (window/window-minibuffer-p window)
        [header-line mode-line] (when-not minibuffer?
                                  [(buffer/buffer-local-value 'header-line-format buffer)
                                   (buffer/buffer-local-value 'mode-line-format buffer)])
        text (binding [buffer/*current-buffer* buffer]
               (editfns/buffer-string))
        line-indexes (line-indexes text)
        pos-to-line (partial pos-to-line line-indexes)
        point-coords (partial point-coords line-indexes)
        pt (- @(.pt buffer) (or @(.begv buffer) 0))
        line (pos-to-line pt)
        total-lines (- @(.total-lines window) (or (count (remove nil? [header-line mode-line])) 0))
        scroll (max (inc (- line total-lines)) 0)
        mark-active? (buffer/buffer-local-value 'mark-active buffer)
        selected-window? (= window (window/selected-window))]
    (let [lines (s/split text #"\n")
          cols @(.total-cols window)
          top-line @(.top-line window)
          top-line (if header-line (inc top-line) top-line)
          screen-coords (fn [[x y]] [x  (+ top-line (- y scroll))])] ;; Not dealing with horizontal scroll.

      (when header-line
        (puts 0 (dec top-line) (pad (xdisp/format-mode-line header-line nil window buffer) cols) reverse-video))

      (let [[[rbx rby] [rex rey]]
            (if (and mark-active? selected-window?)
              [(screen-coords (point-coords (dec (editfns/region-beginning))))
               (screen-coords (point-coords (dec (editfns/region-end))))]
              [[-1 -1] [-1 -1]])]

        (dotimes [n total-lines]
          (let [screen-line (+ top-line n)
                text (pad (nth lines (+ scroll n) " ") cols)]
            (cond
             (= screen-line rby rey) (do
                                       (puts 0 screen-line (subs text 0 rbx))
                                       (puts rbx screen-line (subs text rbx rex) region-colors)
                                       (puts rex screen-line (subs text rex)))

             (= screen-line rby) (do
                                   (puts 0 screen-line (subs text 0 rbx))
                                   (puts rbx screen-line (subs text rbx) region-colors))

             (= screen-line rey) (do
                                   (puts 0 screen-line (subs text 0 rex) region-colors)
                                   (puts rex screen-line (subs text rex)))

             (< rby screen-line rey) (puts 0 screen-line text region-colors)

             :else (puts 0 screen-line text)))))

      (when selected-window?
        (let [[px py] (screen-coords (point-coords (dec pt)))]
          (sc/move-cursor screen px py)))

      (when mode-line
        (puts 0 (+ top-line total-lines) (pad (xdisp/format-mode-line mode-line nil window buffer) cols) {:bg :white})))))

(defn render-window [window x y width height]
  ;; We should walk the tree, splitting windows as we go.
  ;; top or left children in turn have next siblings all sharing this area.
  ;; A live window is a normal window with buffer.
  (reset! (.top-line window) y)
  (reset! (.left-col window) x)
  ;; "normal" size is a weight between 0 - 1.0, should hopfully add up.
  (reset! (.total-cols window) (long (* @(.normal-cols window) width)))
  (reset! (.total-lines window) (long (* @(.normal-lines window) height)))

  (condp some [window]
    window/window-live-p (render-live-window window)
    window/window-top-child (throw (UnsupportedOperationException.))
    window/window-left-child (throw (UnsupportedOperationException.))))

(defn display-using-lanterna []
  (let [[width height] (te/get-size (.getTerminal screen))
        mini-buffer-window (window/minibuffer-window)
        mini-buffer (- height (window/window-total-height mini-buffer-window))
        menu-bar-mode (data/symbol-value 'menu-bar-mode)
        menu-bar (if menu-bar-mode 1 0)]

    (when menu-bar-mode
      (puts 0 0 (pad (render-menu-bar) width) reverse-video))

    (render-window (window/frame-root-window) 0 menu-bar
                   width (- mini-buffer menu-bar))
    (render-window (window/minibuffer-window) 0 mini-buffer
                   width (window/window-total-height mini-buffer-window))

    (sc/redraw screen)))

(def running (atom nil))
(def in (InputStreamReader. System/in))

(defn stop-render-loop []
  (reset! running nil))

;; Not the real thing, but keeps the UI changing while using the REPL before we got a real command loop.
(defn start-render-loop []
  (reset! running true)
  (blank)
  (future
    (while @running
      (try
        (display-using-lanterna)
        (Thread/sleep 50)
        (catch Exception e
          (reset! running nil)
          (throw e))))))

(def char-buffer (atom []))
(def event-buffer (atom []))

;; We bypass Lanterna with System/in but utilize their setup of private mode, see deuce.emacs.keyboard
;; We report our TERM as "lanterna" to allow terminal-init-lanterna to be run first, then init the real one.
;; All this has only been tested on TERM=xterm
;; input-decode-map is setup in term/xterm. We should also look in local-function-key-map
;; This interfers badly with Lanterna's get-size, occasionally locks up, needs fix.
(defn read-key []
  (let [c (.read in)]
    (println c (char c))
    (swap! char-buffer conj (char c))
    (let [maybe-event (object-array @char-buffer)
          decoded (keymap/lookup-key (data/symbol-value 'input-decode-map) maybe-event)]
      (if (keymap/keymapp decoded)
        (println "potential input-decode prefix" maybe-event)
        (let [_ (reset! char-buffer [])
              event (if (data/vectorp decoded) decoded maybe-event)
              _ (swap! event-buffer (comp vec concat) event)
              def (keymap/key-binding (object-array @event-buffer))]
          (println maybe-event decoded event @char-buffer @event-buffer (if (keymap/keymapp def) "(keymap)" def))
          (if (and def (not (keymap/keymapp def)))
            (do
              (reset! char-buffer [])
              (reset! event-buffer [])
              (println "COMMAND" def))
            (when-not (keymap/keymapp def)
              (reset! char-buffer [])
              (reset! event-buffer []))))))))

(defn start-input-loop []
  (reset! running true)
  (future
    (while @running
      (try
        (read-key)
        (catch Exception e
          (reset! running nil)
          (println e)
          (throw e))))))

(timbre/set-config!
 [:appenders :deuce-buffer-appender]
 {:min-level :debug :enabled? true :async? true
  :fn (fn [{:keys [ap-config level prefix message more] :as args}]
        (binding [buffer/*current-buffer* (buffer/get-buffer-create "*Deuce*")]
          (editfns/insert (str (s/join " " (concat [prefix "-" message] more)) \newline))))})

(defn init-clipboard []
  (let [clipboard (.getSystemClipboard (Toolkit/getDefaultToolkit))]
    (el/setq interprogram-cut-function
             #(let [selection (StringSelection. %)]
                (.setContents clipboard selection selection)))

    (el/setq interprogram-paste-function
             #(.getData clipboard DataFlavor/stringFlavor))))

;; Callback run by faces/tty-run-terminal-initialization based on deuce.emacs.term/tty-type returning "lanterna"
;; Has Emacs Lisp proxy in deuce.emacs.
(defn terminal-init-lanterna []
  (try
    ((ns-resolve 'deuce.emacs.terminal 'init-initial-terminal))
    (def screen (terminal/frame-terminal))
    (init-clipboard)
    (start-render-loop)
    (catch Exception e
      (when screen
        (sc/stop screen)
        (throw e)))))

(def deuce-dot-d (str (io/file (System/getProperty "user.home") ".deuce.d")))
(def ^:dynamic *emacs-compile-path* *compile-path*)

(defn init-user-classpath []
  (.mkdirs (io/file deuce-dot-d))
  (dp/add-classpath-url (.getContextClassLoader (Thread/currentThread)) (.toURL (io/file deuce-dot-d)))
  (alter-var-root #'*emacs-compile-path* (constantly deuce-dot-d)))

(defn load-user-init-file []
  (let [init-file (io/file deuce-dot-d "init.clj")]
    (try
      (when (.exists init-file)
        (load-file (str init-file)))
      (catch Exception e
        (timbre/error e (format "An error occurred while loading `%s':" (str init-file)))))))

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

    (lread/load "deuce-loadup.el")
    (init-user-classpath)
    (when (data/symbol-value 'init-file-user)
      (load-user-init-file))

    ;; /* Enter editor command loop.  This never returns.  */
    (keyboard/recursive-edit)))
