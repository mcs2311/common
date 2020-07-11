//-------------------------------------------------------------------------------------
package codex.common.apps.rxbasics;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;

import io.reactivex.rxjava3.disposables.*;

import codex.common.utils.*;

//-------------------------------------------------------------------------------------
public class RxBase implements Runnable, Serializable {
	protected transient Debug debug;
	private Thread thisThread;

//-------------------------------------------------------------------------------------
    public RxBase(Debug _debug) {
    	debug = _debug;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public Thread getThisThread() {
    	return thisThread;
    }

//-------------------------------------------------------------------------------------
    public void init() {
    	thisThread = new Thread(this);
//    	return thisThread;
    }

//-------------------------------------------------------------------------------------
    public void start() {
    	if(thisThread == null){
    		init();
    	}
    	thisThread.start();
//    	return thisThread;
    }

//-------------------------------------------------------------------------------------
    public void run() {
    }

//-------------------------------------------------------------------------------------
    public void interrupt() {
    	thisThread.interrupt();
    }

//-------------------------------------------------------------------------------------
	public void onError(Throwable _t){
		ExceptionUtils.printException(debug, this, _t);
	}

//-------------------------------------------------------------------------------------
	public void onCompleted(){
		debug.outln(Debug.ERROR, "onCompleted:");
	}

//-------------------------------------------------------------------------------------
	public void onSubscribe(Disposable _disposable){
		debug.outln(Debug.ERROR, "onSubscribe:");
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    }

//-------------------------------------------------------------------------------------
    public void save() {
    	if(thisThread != null){
    		thisThread.interrupt();
    	}
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
