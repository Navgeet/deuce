(ns emacs.print (use [deuce.core]) (require [clojure.core :as core]) (:refer-clojure :only []))

(defun error-message-string (obj)
  "Convert an error value (ERROR-SYMBOL . DATA) to an error message.
  See Info anchor `(elisp)Definition of signal' for some details on how this"
  )

(defun print (object &optional printcharfun)
  "Output the printed representation of OBJECT, with newlines around it.
  Quoting characters are printed when needed to make output that `read'
  can handle, whenever this is possible.  For complex objects, the behavior
  is controlled by `print-level' and `print-length', which see."
  )

(defun terpri (&optional printcharfun)
  "Output a newline to stream PRINTCHARFUN."
  )

(defun prin1-to-string (object &optional noescape)
  "Return a string containing the printed representation of OBJECT.
  OBJECT can be any Lisp object.  This function outputs quoting characters
  when necessary to make output that `read' can handle, whenever possible,
  unless the optional second argument NOESCAPE is non-nil.  For complex objects,
  the behavior is controlled by `print-level' and `print-length', which see."
  )

(defun prin1 (object &optional printcharfun)
  "Output the printed representation of OBJECT, any Lisp object.
  Quoting characters are printed when needed to make output that `read'
  can handle, whenever this is possible.  For complex objects, the behavior
  is controlled by `print-level' and `print-length', which see."
  )

(defun external-debugging-output (character)
  "Write CHARACTER to stderr.
  You can call print while debugging emacs, and pass it this function"
  )

(defun princ (object &optional printcharfun)
  "Output the printed representation of OBJECT, any Lisp object.
  No quoting characters are used; no delimiters are printed around
  the contents of strings."
  )

(defun write-char (character &optional printcharfun)
  "Output character CHARACTER to stream PRINTCHARFUN."
  )
