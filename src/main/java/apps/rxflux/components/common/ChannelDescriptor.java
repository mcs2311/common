//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.components.common;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.math.*;
import java.util.*;

import codex.common.utils.*;
import codex.common.apps.uid.*;

//-------------------------------------------------------------------------------------
public class ChannelDescriptor implements Serializable {
	private transient Debug debug;
	private int type;
	private String hostname;
	private int port;
	private String fluxName;
	private long channelUid;

	private int mode;

	//---statics:
	public static final int TYPE_CHANNEL_NONE 		= 0;
	public static final int TYPE_CHANNEL_SYNC 		= 1;
	public static final int TYPE_CHANNEL_DATA 		= 2;
	public static final int TYPE_CHANNEL_ZOMBIE 	= 1000;

	public static final int MODE_CHANNEL_UNKNOWN 	= 0;
	public static final int MODE_CHANNEL_CLIENT 	= 1;
	public static final int MODE_CHANNEL_SERVER 	= 2;

//-------------------------------------------------------------------------------------
    public ChannelDescriptor(Debug _debug) {
    	this(_debug, TYPE_CHANNEL_SYNC, null, -1, newChannelUid(), MODE_CHANNEL_SERVER, "_");
    }

//-------------------------------------------------------------------------------------
    public ChannelDescriptor(Debug _debug, int _port) {
    	this(_debug, TYPE_CHANNEL_SYNC, null, _port, newChannelUid(), MODE_CHANNEL_SERVER, "_");
    }

//-------------------------------------------------------------------------------------
    public ChannelDescriptor(Debug _debug, String _hostname, int _port) {
    	this(_debug, TYPE_CHANNEL_SYNC, _hostname, _port, newChannelUid(), MODE_CHANNEL_CLIENT, "_");
    }

//-------------------------------------------------------------------------------------
    public ChannelDescriptor(Debug _debug, ChannelDescriptor _channelDescriptor, String _fluxName) {
    	this(_debug, _channelDescriptor.getType(), _channelDescriptor.getHostname(), _channelDescriptor.getPort(), newChannelUid(), MODE_CHANNEL_CLIENT, _fluxName);
    }

//-------------------------------------------------------------------------------------
    public ChannelDescriptor(Debug _debug, int _type, String _hostname, int _port, long _channelUid, int _mode, String _fluxName) {
    	debug = _debug;
		type = _type;
    	hostname = _hostname;
    	port = _port;
    	channelUid = _channelUid;
    	mode = _mode;
    	fluxName = _fluxName;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public void setDebug(Debug _debug){
		debug = _debug;
	}

//-------------------------------------------------------------------------------------
	public int getType(){
		return type;
	}

//-------------------------------------------------------------------------------------
	public boolean isSyncType(){
		return (type == TYPE_CHANNEL_SYNC);
	}

//-------------------------------------------------------------------------------------
	public boolean isDataType(){
		return (type == TYPE_CHANNEL_DATA);
	}

//-------------------------------------------------------------------------------------
	public boolean isDataNew(){
		return isDataType() && (channelUid != -1);
	}

//-------------------------------------------------------------------------------------
	public void setType(int _type){
		type = _type;
	}

//-------------------------------------------------------------------------------------
	public String getTypeAsString(){
		switch(type){
			case TYPE_CHANNEL_NONE 		: return "CHANNEL_NONE";
			case TYPE_CHANNEL_SYNC		: return "CHANNEL_SYNC";
			case TYPE_CHANNEL_DATA 		: return "CHANNEL_DATA"; 	
			case TYPE_CHANNEL_ZOMBIE 	: return "CHANNEL_ZOMBIE"; 	
			default 					: return "ERROR";
		}
	}

//-------------------------------------------------------------------------------------
	public String getTypeAsShortString(){
		switch(type){
			case TYPE_CHANNEL_NONE 		: return "NON";
			case TYPE_CHANNEL_SYNC		: return "SYN";
			case TYPE_CHANNEL_DATA 		: return "DAT"; 	
			case TYPE_CHANNEL_ZOMBIE 	: return "ZOM"; 	
			default 					: return "ERR";
		}
	}

//-------------------------------------------------------------------------------------
	public String getHostname(){
		return hostname;
	}

//-------------------------------------------------------------------------------------
	public int getPort(){
		return port;
	}

//-------------------------------------------------------------------------------------
	public long getChannelUid(){
		return channelUid;
	}

//-------------------------------------------------------------------------------------
	public void setChannelUid(long _channelUid){
		channelUid = _channelUid;
	}

//-------------------------------------------------------------------------------------
	public String getFluxName(){
		return fluxName;
	}

//-------------------------------------------------------------------------------------
	public void setFluxName(String _fluxName){
		fluxName = _fluxName;
	}

//-------------------------------------------------------------------------------------
	private static long newChannelUid(){
		return Uid.next();
	}

//-------------------------------------------------------------------------------------
	public int getMode(){
		return mode;
	}

//-------------------------------------------------------------------------------------
	public void setMode(int _mode){
		mode = _mode;
	}

//-------------------------------------------------------------------------------------
	public String getModeAsShortString(){
		switch(mode){
			case MODE_CHANNEL_UNKNOWN 	: return "U";
			case MODE_CHANNEL_CLIENT 	: return "C";
			case MODE_CHANNEL_SERVER  	: return "S"; 	
			default 					: return "E";
		}
	}


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------
   @Override   
   public boolean equals(Object _obj) {
//		System.out.println("Key search-------");
		if (!(_obj instanceof ChannelDescriptor)){
			return false;
		}
    	ChannelDescriptor _ref = (ChannelDescriptor) _obj;
    	return (hostname.equals(_ref.hostname) && 
    		(port == _ref.port) &&
    		(channelUid == _ref.channelUid));
   }

//-------------------------------------------------------------------------------------
    @Override
    public int hashCode() {
    	int _hashcode = hostname.hashCode() ^ 
    					port ^ 
    					(int)(channelUid & 0x0000FFFF) ^
    					(int)((channelUid >> 32) & 0x00000FFF);
    	return _hashcode;
    }

//-------------------------------------------------------------------------------------
    @Override
    public String toString() {
    	String _text = "";
    	if(hostname != null){
    		_text += hostname + ".";
    	}
    	if(port != -1){
    		_text += port + ".";
    	}
    
    	_text +=  getModeAsShortString() + "." + getTypeAsShortString();

    	_text += "." + fluxName;

    	_text +=  "." + Uid.toShortHexString(getChannelUid());
    
    	return _text;
    }

//-------------------------------------------------------------------------------------
    public String toDescription() {
    	return hostname + " / " + port + " / " + getTypeAsString() + " / " + Uid.toHexString(getChannelUid());
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
