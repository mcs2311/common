//-------------------------------------------------------------------------------------
package codex.common.utils;
//-------------------------------------------------------------------------------------
import java.util.*;

//-------------------------------------------------------------------------------------
public class LookupObject {
    private String find;
    private String replace;

//-------------------------------------------------------------------------------------
    public LookupObject(String _find, String _replace){
        find = _find;
        replace = _replace;
    }

//-------------------------------------------------------------------------------------
    public String getFind(){
        return find;
    }

//-------------------------------------------------------------------------------------
    public String getReplace(){
        return replace;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------