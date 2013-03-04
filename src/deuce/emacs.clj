(ns deuce.emacs
  (:require [clojure.core :as c]
            [deuce.emacs-lisp.globals :as globals])
  (:refer-clojure :only [])
  (:use [deuce.emacs-lisp :only [and catch cond condition-case defconst defmacro defun defvar
                                 function if interactive lambda let let* or prog1 prog2 progn quote
                                 save-current-buffer save-excursion save-restriction setq setq-default
                                 unwind-protect while throw]]
        [deuce.emacs.alloc]
        [deuce.emacs.buffer]
        [deuce.emacs.bytecode]
        [deuce.emacs.callint]
        [deuce.emacs.callproc]
        [deuce.emacs.casefiddle]
        [deuce.emacs.casetab]
        [deuce.emacs.category]
        [deuce.emacs.ccl]
        [deuce.emacs.character]
        [deuce.emacs.charset]
        [deuce.emacs.chartab]
        [deuce.emacs.cmds]
        [deuce.emacs.coding]
        [deuce.emacs.composite]
        [deuce.emacs.data]
        [deuce.emacs.dired]
        [deuce.emacs.dispnew]
        [deuce.emacs.doc]
        [deuce.emacs.editfns]
        [deuce.emacs.emacs]
        [deuce.emacs.eval]
        [deuce.emacs.fileio]
        [deuce.emacs.filelock]
        [deuce.emacs.floatfns]
        [deuce.emacs.fns]
        [deuce.emacs.font]
        [deuce.emacs.frame]
        [deuce.emacs.indent]
        [deuce.emacs.insdel]
        [deuce.emacs.keyboard]
        [deuce.emacs.keymap]
        [deuce.emacs.lread]
        [deuce.emacs.macros]
        [deuce.emacs.marker]
        [deuce.emacs.menu]
        [deuce.emacs.minibuf]
        [deuce.emacs.print]
        [deuce.emacs.process]
        [deuce.emacs.search]
        [deuce.emacs.syntax]
        [deuce.emacs.term]
        [deuce.emacs.terminal]
        [deuce.emacs.textprop]
        [deuce.emacs.undo]
        [deuce.emacs.window]
        [deuce.emacs.xdisp]
        [deuce.emacs.xfaces]
        [deuce.emacs.xml]))

(setq t true)

(setq motif-version-string "")
(setq gtk-version-string "")
(setq ns-version-string "")
(setq x-toolkit-scroll-bars nil)
(setq msdos-long-file-names nil)

;; Hack for a predicate in cl.el, this is defined in emacs-lisp/bytecomp.el, which we're not using
(defun byte-compile-file-form (form))
;; AOT cl.el gets confused by this alias
(defalias 'cl-block-wrapper 'identity)

;; Hack to ensure these gets treated as a macros even before loaded
(defmacro declare (&rest _specs) nil)
(defmacro dolist (spec &rest body) nil)

;; Hacks to get a sneak peek of the welcome screen:
;; avoid loading frame.el yet
(setq frame-initial-frame nil)
(defun frame-initialize ())
(defun normal-erase-is-backspace-setup-frame ())
(defun newline (&optional arg) (c/println))
(defun make-frame (&optional parameters))
(defun display-graphic-p (&optional display))
(defun display-mouse-p (&optional display))

;; Stubs for modes defined in simple
(defun special-mode ())
(defun transient-mark-mode (arg))

;; Keymap setup
(setq global-map (make-keymap))
(setq esc-map (make-keymap))
(setq ctl-x-map (make-keymap))
;; var is definied in keyboard.clj
(setq function-key-map (make-sparse-keymap))
;; This map has a few low-level (like delete-frame) key defs in keybaoard.c
(setq special-event-map (make-sparse-keymap))

(setq minibuffer-local-map (make-sparse-keymap))
(setq minibuffer-local-ns-map (make-sparse-keymap))
; (set-keymap-parent globals/minibuffer-local-ns-map globals/minibuffer-local-map)
