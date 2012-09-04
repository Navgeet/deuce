(ns
 deuce.emacs.data
 (use [deuce.emacs-lisp :only (defun defvar setq)])
 (require [clojure.core :as c]
          [deuce.emacs.eval :as eval])
 (import [deuce EmacsLispError])
 (:refer-clojure
  :exclude
  [+ * - / aset set < = > max >= <= mod atom min]))

(defvar most-positive-fixnum nil
  "The largest value that is representable in a Lisp integer.")

(defvar most-negative-fixnum nil
  "The smallest value that is representable in a Lisp integer.")

(defun natnump (object)
  "Return t if OBJECT is a nonnegative integer."
  )

(defun markerp (object)
  "Return t if OBJECT is a marker (editor pointer)."
  )

(defun ash (value count)
  "Return VALUE with its bits shifted left by COUNT.
  If COUNT is negative, shifting is actually to the right.
  In this case, the sign bit is duplicated."
  )

(defun type-of (object)
  "Return a symbol representing the type of OBJECT.
  The symbol returned names the object's basic type;
  for example, (type-of 1) returns `integer'."
  )

(defun indirect-function (object &optional noerror)
  "Return the function at the end of OBJECT's function chain.
  If OBJECT is not a symbol, just return it.  Otherwise, follow all
  function indirections to find the final function binding and return it.
  If the final symbol in the chain is unbound, signal a void-function error.
  Optional arg NOERROR non-nil means to return nil instead of signaling.
  Signal a cyclic-function-indirection error if there is a loop in the
  function chain of symbols."
  )

(defun symbol-name (symbol)
  "Return SYMBOL's name, a string."
  )

(defun makunbound (symbol)
  "Make SYMBOL's value be void.
  Return SYMBOL."
  )

(defun interactive-form (cmd)
  "Return the interactive form of CMD or nil if none.
  If CMD is not a command, the return value is nil.
  Value, if non-nil, is a list (interactive SPEC)."
  )

(defun logior (&rest ints-or-markers)
  "Return bitwise-or of all the arguments.
  Arguments may be integers, or markers converted to integers."
  )

(defun sequencep (object)
  "Return t if OBJECT is a sequence (list or array)."
  )

(defun zerop (number)
  "Return t if NUMBER is zero."
  )

(defun indirect-variable (object)
  "Return the variable at the end of OBJECT's variable chain.
  If OBJECT is a symbol, follow all variable indirections and return the final
  variable.  If OBJECT is not a symbol, just return it.
  Signal a cyclic-variable-indirection error if there is a loop in the
  variable chain of symbols."
  )

(defun symbol-value (symbol)
  "Return SYMBOL's value.  Error if that is void."
  @(ns-resolve 'deuce.emacs-lisp.globals (c/symbol (name symbol))))

(defun keywordp (object)
  "Return t if OBJECT is a keyword.
  This means that it is a symbol with a print name beginning with `:'
  interned in the initial obarray."
  )

(defun (clojure.core/symbol "1+") (number)
  "Return NUMBER plus one.  NUMBER may be a number or a marker.
  Markers are converted to integers."
  )

(defun subrp (object)
  "Return t if OBJECT is a built-in function."
  )

(defun symbol-plist (symbol)
  "Return SYMBOL's property list."
  )

(defun stringp (object)
  "Return t if OBJECT is a string."
  )

(defun integerp (object)
  "Return t if OBJECT is an integer."
  )

(defun fboundp (symbol)
  "Return t if SYMBOL's function definition is not void."
  )

(defun % (x y)
  "Return remainder of X divided by Y.
  Both must be integers or markers."
  )

(defun + (&rest numbers-or-markers)
  "Return sum of any number of arguments, which are numbers or markers."
  (apply c/+ numbers-or-markers))

(defun lsh (value count)
  "Return VALUE with its bits shifted left by COUNT.
  If COUNT is negative, shifting is actually to the right.
  In this case, zeros are shifted in on the left."
  )

(defun eq (obj1 obj2)
  "Return t if the two args are the same Lisp object."
  (identical? obj1 obj2))

(defun * (&rest numbers-or-markers)
  "Return product of any number of arguments, which are numbers or markers."
  )

(defun - (&optional number-or-marker &rest more-numbers-or-markers)
  "Negate number or subtract numbers or markers and return the result.
  With one arg, negates it.  With more than one arg,
  subtracts all but the first from the first."
  )

(defun multibyte-string-p (object)
  "Return t if OBJECT is a multibyte string."
  )

(defun logxor (&rest ints-or-markers)
  "Return bitwise-exclusive-or of all the arguments.
  Arguments may be integers, or markers converted to integers."
  )

(defun floatp (object)
  "Return t if OBJECT is a floating point number."
  )

(defun number-or-marker-p (object)
  "Return t if OBJECT is a number or a marker."
  )

(defun cdr-safe (object)
  "Return the cdr of OBJECT if it is a cons cell, or else nil."
  )

(defun / (dividend divisor &rest divisors)
  "Return first argument divided by all the remaining arguments.
  The arguments must be numbers or markers."
  (if (zero? divisor)
    (throw (EmacsLispError. 'arith-error nil))
    (c/reduce / (c/let [r (clojure.core// dividend divisor)]
                  (if (ratio? r) (long r) r))
              divisors)))

(defun byteorder ()
  "Return the byteorder for the machine.
  Returns 66 (ASCII uppercase B) for big endian machines or 108 (ASCII
  lowercase l) for small endian machines."
  )

(defun subr-name (subr)
  "Return name of subroutine SUBR.
  SUBR must be a built-in function."
  )

(defun make-local-variable (variable)
  "Make VARIABLE have a separate value in the current buffer.
  Other buffers will continue to share a common default value.
  (The buffer-local value of VARIABLE starts out as the same value
  VARIABLE previously had.  If VARIABLE was void, it remains void.)
  Return VARIABLE.

  If the variable is already arranged to become local when set,
  this function causes a local value to exist for this buffer,
  just as setting the variable would do.

  This function returns VARIABLE, and therefore
    (set (make-local-variable 'VARIABLE) VALUE-EXP)
  works.

  See also `make-variable-buffer-local'.

  Do not use `make-local-variable' to make a hook variable buffer-local.
  Instead, use `add-hook' and specify t for the LOCAL argument."
  )

(defun numberp (object)
  "Return t if OBJECT is a number (floating point or integer)."
  )

(defun logand (&rest ints-or-markers)
  "Return bitwise-and of all the arguments.
  Arguments may be integers, or markers converted to integers."
  )

(defun consp (object)
  "Return t if OBJECT is a cons cell."
  ((every-pred seq? seq) object))

(defun listp (object)
  "Return t if OBJECT is a list, that is, a cons cell or nil.
  Otherwise, return nil."
  ((some-fn list? nil?) object))

(defun aref (array idx)
  "Return the element of ARRAY at index IDX.
  ARRAY may be a vector, a string, a char-table, a bool-vector,
  or a byte-code object.  IDX starts at 0."
  )

(defun wholenump (object)
  "Return t if OBJECT is a nonnegative integer."
  )

(defun aset (array idx newelt)
  "Store into the element of ARRAY at index IDX the value NEWELT.
  Return NEWELT.  ARRAY may be a vector, a string, a char-table or a
  bool-vector.  IDX starts at 0."
  )

(defun arrayp (object)
  "Return t if OBJECT is an array (string or vector)."
  )

(defun vectorp (object)
  "Return t if OBJECT is a vector."
  )

(defun fmakunbound (symbol)
  "Make SYMBOL's function definition be void.
  Return SYMBOL."
  )

(defun lognot (number)
  "Return the bitwise complement of NUMBER.  NUMBER must be an integer."
  )

(defun setcdr (cell newcdr)
  "Set the cdr of CELL to be NEWCDR.  Returns NEWCDR."
  )

(defun set (symbol newval)
  "Set SYMBOL's value to NEWVAL, and return NEWVAL."
  (eval `(setq ~symbol '~newval)))

(defun < (num1 num2)
  "Return t if first arg is less than second arg.  Both must be numbers or markers."
  )

(defun car-safe (object)
  "Return the car of OBJECT if it is a cons cell, or else nil."
  )

(defun fset (symbol definition)
  "Set SYMBOL's function definition to DEFINITION, and return DEFINITION."
  )

(defun cdr (list)
  "Return the cdr of LIST.  If arg is nil, return nil.
  Error if arg is not nil and not a cons cell.  See also `cdr-safe'.

  See Info node `(elisp)Cons Cells' for a discussion of related basic
  Lisp concepts such as cdr, car, cons cell and list."
  (next list))

(defun = (num1 num2)
  "Return t if two args, both numbers or markers, are equal."
  )

(defun make-variable-buffer-local (variable)
  "Make VARIABLE become buffer-local whenever it is set.
  At any time, the value for the current buffer is in effect,
  unless the variable has never been set in this buffer,
  in which case the default value is in effect.
  Note that binding the variable with `let', or setting it while
  a `let'-style binding made in this buffer is in effect,
  does not make the variable buffer-local.  Return VARIABLE.

  In most cases it is better to use `make-local-variable',
  which makes a variable local in just one buffer.

  The function `default-value' gets the default value and `set-default' sets it."
  )

(defun char-or-string-p (object)
  "Return t if OBJECT is a character or a string."
  )

(defun vector-or-char-table-p (object)
  "Return t if OBJECT is a char-table or vector."
  )

(defun bufferp (object)
  "Return t if OBJECT is an editor buffer."
  )

(defun > (num1 num2)
  "Return t if first arg is greater than second arg.  Both must be numbers or markers."
  )

(defun max (number-or-marker &rest numbers-or-markers)
  "Return largest of all the arguments (which must be numbers or markers).
  The value is always a number; markers are converted to numbers."
  )

(defun local-variable-if-set-p (variable &optional buffer)
  "Non-nil if VARIABLE will be local in buffer BUFFER when set there.
  More precisely, this means that setting the variable (with `set' or`setq'),
  while it does not have a `let'-style binding that was made in BUFFER,
  will produce a buffer local binding.  See Info node
  `(elisp)Creating Buffer-Local'.
  BUFFER defaults to the current buffer."
  )

(defun default-boundp (symbol)
  "Return t if SYMBOL has a non-void default value.
  This is the value that is seen in buffers that do not have their own values
  for this variable."
  )

(defun nlistp (object)
  "Return t if OBJECT is not a list.  Lists include nil."
  )

(defun >= (num1 num2)
  "Return t if first arg is greater than or equal to second arg.
  Both must be numbers or markers."
  )

(defun boundp (symbol)
  "Return t if SYMBOL's value is not void."
  )

(defun default-value (symbol)
  "Return SYMBOL's default value.
  This is the value that is seen in buffers that do not have their own values
  for this variable.  The default value is meaningful for variables with
  local bindings in certain buffers."
  )

(defun setcar (cell newcar)
  "Set the car of CELL to be NEWCAR.  Returns NEWCAR."
  )

(defun symbolp (object)
  "Return t if OBJECT is a symbol."
  )

(defun <= (num1 num2)
  "Return t if first arg is less than or equal to second arg.
  Both must be numbers or markers."
  )

(defun local-variable-p (variable &optional buffer)
  "Non-nil if VARIABLE has a local binding in buffer BUFFER.
  BUFFER defaults to the current buffer."
  )

(defun byte-code-function-p (object)
  "Return t if OBJECT is a byte-compiled function object."
  )

(defun defalias (symbol definition &optional docstring)
  "Set SYMBOL's function definition to DEFINITION, and return DEFINITION.
  Associates the function with the current load file, if any.
  The optional third argument DOCSTRING specifies the documentation string
  for SYMBOL; if it is omitted or nil, SYMBOL uses the documentation string
  determined by DEFINITION."
  (try
    (eval/eval `(defvar ~symbol ~definition ~(or docstring "")))
    (catch Exception e
      ;(println (-> e .getCause .getMessage))
      )))

(defun setplist (symbol newplist)
  "Set SYMBOL's property list to NEWPLIST, and return NEWPLIST."
  )

(defun set-default (symbol value)
  "Set SYMBOL's default value to VALUE.  SYMBOL and VALUE are evaluated.
  The default value is seen in buffers that do not have their own values
  for this variable."
  )

(defun symbol-function (symbol)
  "Return SYMBOL's function definition.  Error if that is void."
  @(ns-resolve 'deuce.emacs (c/symbol (name symbol))))

(defun kill-local-variable (variable)
  "Make VARIABLE no longer have a separate value in the current buffer.
  From now on the default value will apply in this buffer.  Return VARIABLE."
  )

(defun car (list)
  "Return the car of LIST.  If arg is nil, return nil.
  Error if arg is not nil and not a cons cell.  See also `car-safe'.

  See Info node `(elisp)Cons Cells' for a discussion of related basic
  Lisp concepts such as car, cdr, cons cell and list."
  (c/first list))

(defun bool-vector-p (object)
  "Return t if OBJECT is a bool-vector."
  )

(defun subr-arity (subr)
  "Return minimum and maximum number of args allowed for SUBR.
  SUBR must be a built-in function.
  The returned value is a pair (MIN . MAX).  MIN is the minimum number
  of args.  MAX is the maximum number or the symbol `many', for a
  function with `&rest' args, or `unevalled' for a special form."
  )

(defun mod (x y)
  "Return X modulo Y.
  The result falls between zero (inclusive) and Y (exclusive).
  Both X and Y must be numbers or markers."
  )

(defun (clojure.core/symbol "1-") (number)
  "Return NUMBER minus one.  NUMBER may be a number or a marker.
  Markers are converted to integers."
  )

(defun atom (object)
  "Return t if OBJECT is not a cons cell.  This includes nil."
  )

(defun null (object)
  "Return t if OBJECT is nil."
  (or (nil? object) (c/= () object)))

(defun char-table-p (object)
  "Return t if OBJECT is a char-table."
  )

(defun make-variable-frame-local (variable)
  "This function is obsolete since 22.2;
  explicitly check for a frame-parameter instead.

  Enable VARIABLE to have frame-local bindings.
  This does not create any frame-local bindings for VARIABLE,
  it just makes them possible.

  A frame-local binding is actually a frame parameter value.
  If a frame F has a value for the frame parameter named VARIABLE,
  that also acts as a frame-local binding for VARIABLE in F--
  provided this function has been called to enable VARIABLE
  to have frame-local bindings at all.

  The only way to create a frame-local binding for VARIABLE in a frame
  is to set the VARIABLE frame parameter of that frame.  See
  `modify-frame-parameters' for how to set frame parameters.

  Note that since Emacs 23.1, variables cannot be both buffer-local and
  frame-local any more (buffer-local bindings used to take precedence over
  frame-local bindings)."
  )

(defun number-to-string (number)
  "Return the decimal representation of NUMBER as a string.
  Uses a minus sign if negative.
  NUMBER may be an integer or a floating point number."
  )

(defun integer-or-marker-p (object)
  "Return t if OBJECT is an integer or a marker (editor pointer)."
  )

(defun min (number-or-marker &rest numbers-or-markers)
  "Return smallest of all the arguments (which must be numbers or markers).
  The value is always a number; markers are converted to numbers."
  )

(defun string-to-number (string &optional base)
  "Parse STRING as a decimal number and return the number.
  This parses both integers and floating point numbers.
  It ignores leading spaces and tabs, and all trailing chars.

  If BASE, interpret STRING as a number in that base.  If BASE isn't
  present, base 10 is used.  BASE must be between 2 and 16 (inclusive).
  If the base used is not 10, STRING is always parsed as integer."
  (try
    (long (BigInteger. string (or base 10)))
    (catch NumberFormatException _
      (Double/parseDouble string))))

(defun variable-binding-locus (variable)
  "Return a value indicating where VARIABLE's current binding comes from.
  If the current binding is buffer-local, the value is the current buffer.
  If the current binding is frame-local, the value is the selected frame.
  If the current binding is global (the default), the value is nil."
  )