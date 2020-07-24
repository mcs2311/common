//-------------------------------------------------------------------------------------
package codex.common.apps.rxbasics;
//-------------------------------------------------------------------------------------
//import java.io.*;
//import java.net.*;
import java.util.*;

//import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.*;

import codex.common.utils.*;
import codex.common.apps.uid.*;

//-------------------------------------------------------------------------------------
public class RxStatus extends RxBase {
	private int status;
	private long uid;

	private int hashCode;

	//---statics:
	public static final int STATUS_INIT 		= 0;
	public static final int STATUS_RUNNING 		= 1;
	public static final int STATUS_PAUSED 		= 2;
	public static final int STATUS_STOPPED 		= 3;

//-------------------------------------------------------------------------------------
    public RxStatus(Debug _debug){
    	this(_debug, newUid());
    }

//-------------------------------------------------------------------------------------
    public RxStatus(Debug _debug, long _uid){
    	super(_debug);
    	status = STATUS_INIT;
    	uid = _uid;
//    	debug.outln(Debug.ERROR, " RxStatus.... " + uid);
    	hashCode = createHashcode();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized int getStatus(){
    	return status;
    }

//-------------------------------------------------------------------------------------
    public synchronized String getStatusAsString(){
    	return getStatusAsString(status);
    }

//-------------------------------------------------------------------------------------
    public synchronized String getStatusAsString(int _status){
        String _string = "";//super.toString() + " : ";
        switch(_status){
            case STATUS_INIT 		: return _string + "INIT";
            case STATUS_RUNNING 	: return _string + "RUNNING";
            case STATUS_PAUSED 		: return _string + "PAUSED";
            case STATUS_STOPPED 	: return _string + "STOPPED";
            default 				: return _string + "ERROR";
        }
    }

//-------------------------------------------------------------------------------------
    public synchronized void setStatus(int _status){
    	status = _status;
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean isRunning(){
    	return (status == STATUS_RUNNING);
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean isNotRunning(){
    	return (status != STATUS_RUNNING);
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean isPaused(){
    	return (status == STATUS_PAUSED);
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean isNotStopped(){
    	return (status != STATUS_STOPPED);
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean isStopped(){
    	return (status == STATUS_STOPPED);
    }

//-------------------------------------------------------------------------------------
	public long getUid(){
		return uid;
	}

//-------------------------------------------------------------------------------------
	public void setUid(long _uid){
		uid = _uid;
	}

//-------------------------------------------------------------------------------------
	private static long newUid(){
		return Uid.next();
	}

//-------------------------------------------------------------------------------------
	private int createHashcode(){
		return (int)(uid ^ (uid >>32));
	}

//-------------------------------------------------------------------------------------
	public int hashCode(){
		return hashCode;
	}

//-------------------------------------------------------------------------------------
	public boolean equals(RxStatus _rxStatus){
		return (hashCode() == _rxStatus.hashCode());
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load(){
    	super.load();
//    	setStatus(STATUS_RUNNING);
    }

//-------------------------------------------------------------------------------------
    public void save(){
    	setStatus(STATUS_STOPPED);
    	super.save();
    }

//-------------------------------------------------------------------------------------
    public String toString(){
    	return getStatusAsString();
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
