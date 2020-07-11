//-------------------------------------------------------------------------------------
package codex.common.utils;
//-------------------------------------------------------------------------------------
import java.util.*;

//-------------------------------------------------------------------------------------
public class ExceptionUtils {
	private Debug debug;

//-------------------------------------------------------------------------------------
    public ExceptionUtils(Debug _debug) {
    	debug = _debug;
    }

//-------------------------------------------------------------------------------------
    public void printException(Object _source, Throwable _t) {
		debug.outln(Debug.ERROR, "onError: " + "[" + _source + "]: " + _t.getMessage());
		StackTraceElement[]	_stack = _t.getStackTrace();
		for (int i = 0; i < _stack.length; i++) {
			debug.outln(Debug.ERROR, "S[" + i + "]:" + _stack[i].toString());
		}
    }

//-------------------------------------------------------------------------------------
    public static void printException(Debug _debug, Object _source, Throwable _t) {
    	(new ExceptionUtils(_debug)).printException(_source, _t);
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------

  