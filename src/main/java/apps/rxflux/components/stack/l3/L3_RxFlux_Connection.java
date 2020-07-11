//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.components.stack.l3;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.*;

import codex.common.utils.*;
import codex.common.apps.rxbasics.*;
import codex.common.apps.rxflux.core.*;
import codex.common.apps.rxflux.events.*;
import codex.common.apps.rxflux.messages.*;
import codex.common.apps.rxflux.messages.internal.*;
import codex.common.apps.rxflux.components.common.*;
import codex.common.apps.rxflux.components.stack.l2.*;

//-------------------------------------------------------------------------------------
public abstract class L3_RxFlux_Connection extends L2_RxFlux_Message {
    private int retryTimeOut;

	//---statics:
	public static final int STATUS_CONNECTING = 1000;

//-------------------------------------------------------------------------------------
    public L3_RxFlux_Connection(Debug _debug, ChannelDescriptor _channelDescriptor, Socket _socket) {
    	super(_debug, _channelDescriptor, _socket);
    	retryTimeOut = 500;

    	setStatus(STATUS_CONNECTING);
//		debug.outln(Debug.ERROR, "L3_RxFlux_Connection.......");
    	start();
    }

//-------------------------------------------------------------------------------------
	public ChannelDescriptor getChannelDescriptor(){
		return channelDescriptor;
	}

//-------------------------------------------------------------------------------------
	public void setChannelDescriptor(ChannelDescriptor _channelDescriptor){
		channelDescriptor = _channelDescriptor;
	}

//-------------------------------------------------------------------------------------
    public void run() {
//		debug.outln(Debug.ERROR, "L3_RxFlux_Connection.run.0");
    	while(isNotStopped()){
    		if(getStatus() == STATUS_CONNECTING){
    			connection_connect();
//				debug.outln(Debug.ERROR, "L3_RxFlux_Connection.run.1");
    		} else {
//				debug.outln(Debug.ERROR, "L3_RxFlux_Connection.run.2");
    			RxMessage _rxMessage = connection_readMessageBlocking();
//				debug.outln(Debug.ERROR, "L3_RxFlux_Connection.run.3");
    			if(_rxMessage != null){
//					debug.outln(Debug.ERROR, "L3_RxFlux_Connection.run.4");
    				asyncMessageArrived(_rxMessage);
    			} else {
    				continue;
    			}
    		}
    	}
    }

//-------------------------------------------------------------------------------------
	public abstract void asyncMessageArrived(RxMessage _message);

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void connection_connect() {
		try{
//			debug.outln(Debug.ERROR, "Connecting....: " + channelDescriptor);
			connection_createStreams();
			if(getStatus() == STATUS_CONNECTING){
				setStatus(STATUS_RUNNING);
			}
			retryTimeOut = 200;
			return;
		}catch(Throwable _t){
//			debug.outln(Debug.ERROR, "Cannot extract streams: " + channelDescriptor);
//			onError(_t);
			if(channelDescriptor.getMode() == ChannelDescriptor.MODE_CHANNEL_SERVER){
				setStatus(STATUS_STOPPED);
			}
		}    		    	
		try{
			Thread.sleep(retryTimeOut);
			retryTimeOut = (retryTimeOut + 200) % 2000;
		}catch(InterruptedException _e){
//			debug.outln(Debug.ERROR, "Cannot extract streams...");
		}    		    	
    }

//-------------------------------------------------------------------------------------
    protected void connection_createStreams() throws Throwable {
		if(channelDescriptor.getMode() == ChannelDescriptor.MODE_CHANNEL_CLIENT){
	    	if(socket == null){
	    		socket = new Socket(channelDescriptor.getHostname(), channelDescriptor.getPort());
	    	}
		}
//    	int _type = channelDescriptor.getType();
//    	int _code = (_type == ChannelDescriptor.TYPE_CHANNEL_SYNC) ? RxSyncMessage.REQUEST_SYNC_CHANNEL : RxSyncMessage.REQUEST_DATA_CHANNEL;    	
//		super.createStreams(new RxSyncMessage(_code, channelDescriptor.getUid()));
		super.createStreams(new RxSyncMessage(RxSyncMessage.HELLO, channelDescriptor));
	}

//-------------------------------------------------------------------------------------
    private RxMessage connection_readMessageBlocking() {
    	try{
//        	debug.outln(Debug.ERROR, "connection_readMessageBlocking.0..");
    		RxMessage _rxMessage = super.readMessage();    		

//        	debug.outln(Debug.ERROR, "connection_readMessageBlocking.1..");
        	return filterHELLO(_rxMessage);
    	}catch(Throwable _t){
        	debug.outln(Debug.ERROR, "Error reading message...");
//        	onError(_t);
        	if(channelDescriptor.getMode() == ChannelDescriptor.MODE_CHANNEL_CLIENT){
        		socket = null;
				setStatus(STATUS_CONNECTING);        		
        	} else {
				setStatus(STATUS_STOPPED);        		
        	}
    	}
    	return null;
    }

//-------------------------------------------------------------------------------------
    private RxMessage filterHELLO(RxMessage _rxMessage) throws Throwable {
//		debug.outln(Debug.IMPORTANT4, "filterHELLO: " + _rxMessage);
    	if(_rxMessage instanceof RxSyncMessage){
//        		debug.outln(Debug.IMPORTANT4, "filterHELLO...1");
    		if(_rxMessage.getCode() == RxSyncMessage.HELLO){
//        		debug.outln(Debug.IMPORTANT4, "filterHELLO...2");
    			if((channelDescriptor.getType() == ChannelDescriptor.TYPE_CHANNEL_SYNC) &&
    				(channelDescriptor.getMode() == ChannelDescriptor.MODE_CHANNEL_CLIENT)){
//        			debug.outln(Debug.IMPORTANT4, "filterHELLO...3");
    				ChannelDescriptor _channelDescriptor = (ChannelDescriptor)_rxMessage.getContent();
    				channelDescriptor.setChannelUid(_channelDescriptor.getChannelUid());
    				refreshDebugHeaders();
    			}
    			return connection_readMessageBlocking();
    		}
    	}
    	return _rxMessage;
    }

//-------------------------------------------------------------------------------------
    protected void connection_writeMessageBlocking(RxMessage _rxMessage) {
    	try{
    		super.writeMessage(_rxMessage);
    	}catch(Throwable _t){
        	debug.outln(Debug.ERROR, "Error writing message..." + channelDescriptor);
//        	onError(_t);
        	if(channelDescriptor.getMode() == ChannelDescriptor.MODE_CHANNEL_CLIENT){
        		socket = null;
				setStatus(STATUS_CONNECTING);        		
				interrupt();
        	} else {
				setStatus(STATUS_STOPPED);        		
        	}
    	}
    }

//-------------------------------------------------------------------------------------
	public String toString(){
		return channelDescriptor.toString();
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
