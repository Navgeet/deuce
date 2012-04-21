package deuce;

import clojure.lang.Symbol;

public class EmacsLispError extends RuntimeException {
    public final Symbol symbol;
    public final Object data;

    public EmacsLispError(Object data) {
        this(null, data);
    }

    public EmacsLispError(Symbol symbol, Object data) {
        this.data = data;
        this.symbol = symbol;
    }

    public String toString() {
        return "" + data;
    }
}