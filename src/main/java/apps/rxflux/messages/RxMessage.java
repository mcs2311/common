//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.messages;
//-------------------------------------------------------------------------------------
import java.io.*;

import codex.common.utils.*;

//-------------------------------------------------------------------------------------
public class RxMessage implements Serializable {
//	private int type;
	private int code;
    protected Object content;

    //---statics:
    public static final int CODE_OK 							= 		 1;
    public static final int CODE_ERROR 							= -9999999;

/*
//-------------------------------------------------------------------------------------
    public RxMessage() {
    	this(-1, -1, null);
	}

//-------------------------------------------------------------------------------------
    public RxMessage(int _type) {
    	this(_type, -1, null);
	}

//-------------------------------------------------------------------------------------
    public RxMessage(int _type, int _code) {
    	this(_type, _code, null);
	}
*/
//-------------------------------------------------------------------------------------
    public RxMessage(int _code, Object _content) {
//    	type = _type;
    	code = _code;
    	content = _content;
	}
/*
//-------------------------------------------------------------------------------------
    public int getType() {
        return type;
    }

//-------------------------------------------------------------------------------------
    public String getTypeAsString() {
        switch(type){
            case CODE_ERROR			: return "CODE_ERROR";
            default 				: return "ERROR";
        }
    }

//-------------------------------------------------------------------------------------
    public boolean isOfType(int _type) {
    	return (type == _type);
	}
*/
//-------------------------------------------------------------------------------------
    public int getCode() {
        return code;
    }

//-------------------------------------------------------------------------------------
    public void setCode(int _code) {
        code = _code;
    }

//-------------------------------------------------------------------------------------
    public String getCodeAsString(){
        switch(code){
            case CODE_OK			: return "CODE_OK";
            case CODE_ERROR			: return "CODE_ERROR";
            default 				: return "ERROR";
        }
    }

//-------------------------------------------------------------------------------------
    public Object getContent(){
        return content;
    }

//-------------------------------------------------------------------------------------
    public void setContent(Object _content){
        content = _content;
    }

//-------------------------------------------------------------------------------------
    public String toString() {
//    	/if
        return getCodeAsString() + "/" + content;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
