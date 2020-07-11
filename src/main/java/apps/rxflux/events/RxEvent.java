//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.events;
//-------------------------------------------------------------------------------------
import java.io.*;

import codex.common.utils.*;

//-------------------------------------------------------------------------------------
public class RxEvent {
	private int type;
	private Object content;

    //---statics:
    public static final int TYPE_NEW_CONNECTION 	= 0;
    public static final int TYPE_NEW_UNIT 			= 1;

//-------------------------------------------------------------------------------------
    public RxEvent() {
    	this(-1, null);
	}

//-------------------------------------------------------------------------------------
    public RxEvent(int _type) {
    	this(_type, null);
	}

//-------------------------------------------------------------------------------------
    public RxEvent(int _type, Object _content) {
    	type = _type;
    	content = _content;
	}

//-------------------------------------------------------------------------------------
    public int getType() {
        return type;
    }

//-------------------------------------------------------------------------------------
    public String getTypeAsString() {
        switch(type){
            case TYPE_NEW_CONNECTION	: return "TYPE_NEW_CONNECTION";
            case TYPE_NEW_UNIT			: return "TYPE_NEW_UNIT";
            default 					: return "ERROR";
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
        return "";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
