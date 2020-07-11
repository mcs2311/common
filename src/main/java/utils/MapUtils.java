//-------------------------------------------------------------------------------------
package codex.common.utils;
//-------------------------------------------------------------------------------------
import java.util.*;

//-------------------------------------------------------------------------------------
public class MapUtils {

//-------------------------------------------------------------------------------------
	public static Map<String, Object> deepCopy(Map<String, Object> original) {
	    Map<String, Object> copy = new HashMap<String, Object>(original.size());
	    for(Map.Entry<String, Object> entry : original.entrySet()) {
	        copy.put(entry.getKey(), entry.getValue());
	    }
	    return copy;
	}

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
