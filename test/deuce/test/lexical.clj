(ns deuce.test.lexical
  (use [deuce.test.common]))

;; "11.9.3 Lexical Binding"[1]

;; Uses normal Clojure let, if the var isn't already globally bound.
;; As vars introduced by let in Emacs can be reassigned with setq, they're represened as atoms locally.

;; [1] http://www.gnu.org/software/emacs/manual/html_node/elisp/Lexical-Binding.html

(with-fresh-emacs)

(repl example-1

     (setq lexical-binding t)

     (let ((x 1))
       (+ x 3))      ⇒ 4

     (defun getx ()
       x)            ⇒ 'getx

     (let ((x 1))
       (getx))       ⇒ Exception)

(repl example-2

     (setq lexical-binding t)

     (defvar my-ticker nil)

     (let ((x 0))
       (setq my-ticker (lambda ()
                               (setq x (+ 1 x)))))
                         ⇒ fn?

     (funcall my-ticker) ⇒ 1

     (funcall my-ticker) ⇒ 2

     (funcall my-ticker) ⇒ 3)