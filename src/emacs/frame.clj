(ns emacs.frame (use [deuce.core]) (require [clojure.core :as core]) (:refer-clojure :only []))

(defun frame-parameters (&optional frame)
  "Return the parameters-alist of frame FRAME.
  It is a list of elements of the form (PARM . VALUE), where PARM is a symbol.
  The meaningful PARMs depend on the kind of frame."
  )

(defun frame-parameter (frame parameter)
  "Return FRAME's value for parameter PARAMETER.
  If FRAME is nil, describe the currently selected frame.make-local-variable is an interactive built-in function in `C source
  code'."
  )

(defun framep (object)
  "Return non-nil if OBJECT is a frame.
  Value is:
    t for a termcap frame (a character-only terminal),
   'x' for an Emacs frame that is really an X window,
   'w32' for an Emacs frame that is a window on MS-Windows display,
   'ns' for an Emacs frame on a GNUstep or Macintosh Cocoa display,
   'pc' for a direct-write MS-DOS frame."
  )

(defun frame-visible-p (frame)
  "Return t if FRAME is \"visible\" (actually in use for display).
  Return the symbol `icon' if FRAME is iconified or \"minimized\".
  Return nil if FRAME was made invisible, via `make-frame-invisible'.
  On graphical displays, invisible frames are not updated and are
  usually not displayed at all, even in a window system's \"taskbar\"."
  )

(defun make-terminal-frame (parms)
  "Create an additional terminal frame, possibly on another terminal.
  This function takes one argument, an alist specifying frame parameters."
  )

(defun modify-frame-parameters (frame alist)
  "Modify the parameters of frame FRAME according to ALIST.
  If FRAME is nil, it defaults to the selected frame.
  ALIST is an alist of parameters to change and their new values.
  Each element of ALIST has the form (PARM . VALUE), where PARM is a symbol.
  The meaningful PARMs depend on the kind of frame.
  Undefined PARMs are ignored, but stored in the frame's parameter list
  so that `frame-parameters' will return them."
  )

(defun frame-list ()
  )

(defun set-frame-size (frame cols rows)
  "Sets size of FRAME to COLS by ROWS, measured in characters.set-window-redisplay-end-trigger is a built-in function in `C source
  code'."
  )

(defun window-system (&optional frame)
  "The name of the window system that FRAME is displaying through.
  The value is a symbol:
   nil for a termcap frame (a character-only terminal),
   'x' for an Emacs frame that is really an X window,
   'w32' for an Emacs frame that is a window on MS-Windows display,
   'ns' for an Emacs frame on a GNUstep or Macintosh Cocoa display,
   'pc' for a direct-write MS-DOS frame."
  )

(defun previous-frame (&optional frame miniframe)
  "Return the previous frame in the frame list before FRAME.
  It considers only frames on the same terminal as FRAME.
  By default, skip minibuffer-only frames.
  If omitted, FRAME defaults to the selected frame.
  If optional argument MINIFRAME is nil, exclude minibuffer-only frames.
  If MINIFRAME is a window, include only its own frame
  and any frame now using that window as the minibuffer.
  If MINIFRAME is `visible', include all visible frames.
  If MINIFRAME is 0, include all visible and iconified frames."
  )

(defun set-mouse-pixel-position (frame x y)
  "Move the mouse pointer to pixel position (X,Y) in FRAME.
  The position is given in pixels, where (0, 0) is the upper-left corner
  of the frame, X is the horizontal offset, and Y is the vertical offset."
  )

(defun frame-pixel-height (&optional frame)
  "Return a FRAME's height in pixels.
  If FRAME is omitted, the selected frame is used.  The exact value
  of the result depends on the window-system and toolkit in use:"
  )

(defun frame-live-p (object)
  "Return non-nil if OBJECT is a frame which has not been deleted.
  Value is nil if OBJECT is not a live frame.  If object is a live
  frame, the return value indicates what sort of terminal device it is
  displayed on.  See the documentation of `framep' for possible"
  )

(defun set-frame-width (frame cols &optional pretend)
  "Specify that the frame FRAME has COLS columns.
  Optional third arg non-nil means that redisplay should use COLS columns"
  )

(defun next-frame (&optional frame miniframe)
  "Return the next frame in the frame list after FRAME.
  It considers only frames on the same terminal as FRAME.
  By default, skip minibuffer-only frames.
  If omitted, FRAME defaults to the selected frame.
  If optional argument MINIFRAME is nil, exclude minibuffer-only frames.
  If MINIFRAME is a window, include only its own frame
  and any frame now using that window as the minibuffer.
  If MINIFRAME is `visible', include all visible frames.
  If MINIFRAME is 0, include all visible and iconified frames."
  )

(defun frame-pixel-width (&optional frame)
  "Return FRAME's width in pixels.
  For a terminal frame, the result really gives the width in characters."
  )

(defun set-frame-height (frame lines &optional pretend)
  "Specify that the frame FRAME has LINES lines.
  Optional third arg non-nil means that redisplay should use LINES lines
  but that the idea of the actual height of the frame should not be changed.move-to-column is an interactive built-in function in `C source code'."
  )

(defun frame-focus (frame)
  "Return the frame to which FRAME's keystrokes are currently being sent.
  This returns nil if FRAME's focus is not redirected."
  )

(defun frame-first-window (&optional frame)
  "Returns the topmost, leftmost window of FRAME."
  )

(defun frame-root-window (&optional frame)
  "Returns the root-window of FRAME.
  If omitted, FRAME defaults to the currently selected frame.downcase-word is an interactive built-in function in `C source code'."
  )

(defun set-frame-selected-window (frame window &optional norecord)
  "Set selected window of FRAME to WINDOW.
  If FRAME is nil, use the selected frame.  If FRAME is the
  selected frame, this makes WINDOW the selected window.
  Optional argument NORECORD non-nil means to neither change the
  order of recently selected windows nor the buffer list."
  )

(defun set-frame-position (frame xoffset yoffset)
  "Sets position of FRAME in pixels to XOFFSET by YOFFSET.
  This is actually the position of the upper left corner of the frame.
  Negative values for XOFFSET or YOFFSET are interpreted relative to"
  )

(defun frame-selected-window (&optional frame)
  "Return the selected window of FRAME."
  )

(defun frame-char-width (&optional frame)
  "Width in pixels of characters in the font in frame FRAME.
  If FRAME is omitted, the selected frame is used.
  On a graphical screen, the width is the standard width of the default font."
  )

(defun mouse-position ()
  "Return a list (FRAME X . Y) giving the current mouse frame and position.
  The position is given in character cells, where (0, 0) is the
  upper-left corner of the frame, X is the horizontal offset, and Y is
  the vertical offset.
  If Emacs is running on a mouseless terminal or hasn't been programmed
  to read the mouse position, it returns the selected frame for FRAME
  and nil for X and Y.
  If `mouse-position-function' is non-nil, `mouse-position' calls it,
  passing the normal return value to that function as an argument,"
  )

(defun set-mouse-position (frame x y)
  "Move the mouse pointer to the center of character cell (X,Y) in FRAME.
  Coordinates are relative to the frame, not a window,
  so the coordinates of the top left character in the frame
  may be nonzero due to left-hand scroll bars or the menu bar."
  )

(defun selected-frame ()
  )

(defun window-frame (window)
  )

(defun redirect-frame-focus (frame &optional focus-frame)
  "Arrange for keystrokes typed at FRAME to be sent to FOCUS-FRAME.
  In other words, switch-frame events caused by events in FRAME will
  request a switch to FOCUS-FRAME, and `last-event-frame' will be
  FOCUS-FRAME after reading an event typed at FRAME."
  )

(defun visible-frame-list ()
  "Return a list of all frames now \"visible\" (being updated).re-search-backward is an interactive built-in function in `C source
  code'."
  )

(defun frame-char-height (&optional frame)
  "Height in pixels of a line in the font in frame FRAME.
  If FRAME is omitted, the selected frame is used."
  )

(defun active-minibuffer-window ()
  "Return the currently active minibuffer window, or nil if none.forward-line is an interactive built-in function in `C source code'."
  )

(defun mouse-pixel-position ()
  "Return a list (FRAME X . Y) giving the current mouse frame and position.
  The position is given in pixel units, where (0, 0) is the
  upper-left corner of the frame, X is the horizontal offset, and Y is
  the vertical offset.
  If Emacs is running on a mouseless terminal or hasn't been programmed
  to read the mouse position, it returns the selected frame for FRAME"
  )
