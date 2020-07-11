//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.messages.internal;
//-------------------------------------------------------------------------------------
import java.io.*;

import codex.common.utils.*;

import codex.common.apps.rxflux.messages.*;

//-------------------------------------------------------------------------------------
public class RxSyncMessage extends RxMessage {

    //---statics:
    public static final int HELLO 							= 1000000;
    public static final int REQUEST_SYNC_CHANNEL 			= 1000001;
    public static final int REQUEST_DATA_CHANNEL 			= 1000002;
    public static final int ANNOUNCE_DATA_CHANNELS 			= 1000003;

//-------------------------------------------------------------------------------------
    public RxSyncMessage(int _code) {
    	this(_code, -1);
	}

//-------------------------------------------------------------------------------------
    public RxSyncMessage(int _code, long _parameter) {
    	super(_code, (Long)_parameter);
	}

//-------------------------------------------------------------------------------------
    public RxSyncMessage(int _code, Object _content) {
    	super(_code, _content);
	}

//-------------------------------------------------------------------------------------
    public String getCodeAsString(){
        switch(getCode()){
            case HELLO						: return "HELLO";
            case REQUEST_SYNC_CHANNEL		: return "REQUEST_SYNC_CHANNEL";
            case REQUEST_DATA_CHANNEL		: return "REQUEST_DATA_CHANNEL";
            case ANNOUNCE_DATA_CHANNELS		: return "ANNOUNCE_DATA_CHANNELS";
            default 						: return super.getCodeAsString();
        }
//		return "ERROR!!!";        
    }

//-------------------------------------------------------------------------------------
    public String toString() {
        return super.toString();// + "/" + getCodeAsString();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
