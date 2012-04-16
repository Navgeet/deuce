(ns emacs.macros (use [deuce.core]) (require [clojure.core :as core]) (:refer-clojure :only []))

(defun execute-kbd-macro (macro &optional count loopfunc)
  "Execute MACRO as string of editor command characters.
  If MACRO is a symbol, its function definition is used.
  COUNT is a repeat count, or nil for once, or 0 for infinite loop."
  )

(defun cancel-kbd-macro-events ()
  )

(defun store-kbd-macro-event (event)
  )
