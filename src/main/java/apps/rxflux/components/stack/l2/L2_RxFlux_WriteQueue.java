//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.components.stack.l2;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.*;

import codex.common.utils.*;
import codex.common.apps.rxbasics.*;
import codex.common.apps.rxflux.core.*;
import codex.common.apps.rxflux.events.*;
import codex.common.apps.rxflux.messages.*;
import codex.common.apps.rxflux.components.common.*;
import codex.common.apps.rxflux.components.stack.l1.*;

//-------------------------------------------------------------------------------------
public class L2_RxFlux_WriteQueue extends RxStatus {
	private L1_RxFlux_ZipObject zipObject;
    private ArrayBlockingQueue<RxMessage> queue;

    private Throwable residualException;
    //---statics:
    public static final int CAPACITY = 100;

//-------------------------------------------------------------------------------------
    public L2_RxFlux_WriteQueue(Debug _debug, L1_RxFlux_ZipObject _zipObject) {
    	super(_debug);
    	zipObject = _zipObject;
    	queue = new ArrayBlockingQueue<RxMessage>(CAPACITY);
    	start();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void writeMessage(RxMessage _rxMessage) throws Throwable {
    	queue.put(_rxMessage);
    	if(residualException != null){
    		throw residualException;
    	}
    }

//-------------------------------------------------------------------------------------
	public void run(){
		RxMessage _rxMessage = null;
		while(isNotStopped()){
			try{
				if(zipObject.getStatus() != STATUS_RUNNING){
					Thread.sleep(100);
					continue;
				}
				_rxMessage = queue.take();
//				debug.outln(Debug.WARNING, "Writing... " + _rxMessage);
				zipObject.writeObject(_rxMessage);
			} catch(Throwable _t){
				debug.outln(Debug.WARNING, "L2_RxFlux_WriteQueue error: " + _rxMessage);
//				onError(_t);
//				System.exit(0);
				residualException = _t;
			}
		}
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();
    }

//-------------------------------------------------------------------------------------
    public void save() {
    	super.save();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
