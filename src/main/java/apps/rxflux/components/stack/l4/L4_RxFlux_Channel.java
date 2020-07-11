//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.components.stack.l4;
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
import codex.common.apps.rxflux.components.stack.l3.*;

//-------------------------------------------------------------------------------------
public class L4_RxFlux_Channel extends L3_RxFlux_Connection implements FlowableOnSubscribe<RxMessage> {
//	private RxFlux connectedRxFlux;

    //---rx:
	protected Flowable<RxMessage> flowable;
	protected List<FlowableEmitter<RxMessage>> emitterList;

	//---statics:


//-------------------------------------------------------------------------------------
    public L4_RxFlux_Channel(Debug _debug) {
    	this(_debug, null, 0, null);
    }

//-------------------------------------------------------------------------------------
    public L4_RxFlux_Channel(Debug _debug, String _hostname, int _port) {
    	this(_debug, _hostname, _port, null);
    }

//-------------------------------------------------------------------------------------
    public L4_RxFlux_Channel(Debug _debug, Socket _socket) {
    	this(_debug, new ChannelDescriptor(_debug, -1), _socket);
    }

//-------------------------------------------------------------------------------------
    public L4_RxFlux_Channel(Debug _debug, ChannelDescriptor _channelDescriptor) {
    	this(_debug, _channelDescriptor, null);
	}

//-------------------------------------------------------------------------------------
    public L4_RxFlux_Channel(Debug _debug, String _hostname, int _port, Socket _socket) {
    	this(_debug, new ChannelDescriptor(_debug, _hostname, _port), _socket);
    }

//-------------------------------------------------------------------------------------
    public L4_RxFlux_Channel(Debug _debug, ChannelDescriptor _channelDescriptor, Socket _socket) {
    	super(_debug, _channelDescriptor, _socket);	    	
//    	connectedUid = -1;
    	emitterList = new ArrayList<FlowableEmitter<RxMessage>>();
    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER).share();

    	load();
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void announce(String _fluxName){
        List<String> _fluxNameList = new ArrayList<String>();
        _fluxNameList.add(_fluxName);
    	announce(_fluxNameList);
    }

//-------------------------------------------------------------------------------------
    public void announce(List<String> _fluxNameList){
        debug.out(Debug.ERROR, "L4_RxFlux_Channel.announce....");
        for (int i = 0; i < _fluxNameList.size() ; i++) {
        	String _fluxName = _fluxNameList.get(i);
        	debug.out(Debug.ERROR, ", " + _fluxName, false);        	
        }
        debug.outln(Debug.ERROR, "", false);
		RxSyncMessage _rxSyncMessage = new RxSyncMessage(RxSyncMessage.ANNOUNCE_DATA_CHANNELS, _fluxNameList);
		writeMessage(_rxSyncMessage);    	
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<RxMessage> _emitter) {
		emitterList.add(_emitter);
    }

//-------------------------------------------------------------------------------------
	public Flowable<RxMessage> getMessages() {
		return flowable;
    }

//-------------------------------------------------------------------------------------
	public RxMessage readMessage(){
//        debug.outln(Debug.ERROR, "L4_RxFlux_Channel.readMessage.0..");
//        onError(new Throwable());
		RxMessage _rxMessage = flowable.blockingNext().iterator().next();
//        debug.outln(Debug.ERROR, "L4_RxFlux_Channel.readMessage.1..");
		return _rxMessage;
	}

/*
//-------------------------------------------------------------------------------------
	public RxMessage readMessage(){
//        debug.outln(Debug.ERROR, "L4_RxFlux_Channel.readMessage.0..");
		List<RxMessage> _rxMessages = new ArrayList<RxMessage>();
		cachedFlowable.subscribeOn(Schedulers.computation(), false)
    			.take(1)
    			.blockingForEach(_rxMessage -> { 
//        					debug.outln(Debug.ERROR, "L4_RxFlux_Channel.readMessage.xxxx..");
    						_rxMessages.add(_rxMessage);
    					});    			
		RxMessage _rxMessage = _rxMessages.get(0);
//        debug.outln(Debug.ERROR, "L4_RxFlux_Channel.readMessage.1..");
		return _rxMessage;
	}
*/
//-------------------------------------------------------------------------------------
	public void writeMessage(RxMessage _rxMessage){
//        debug.outln(Debug.ERROR, "L4_RxFlux_Channel.writeMessage..." + _rxMessage);
		super.connection_writeMessageBlocking(_rxMessage);
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public void asyncMessageArrived(RxMessage _message){
//        debug.outln(Debug.ERROR, "L4_RxFlux_Channel.asyncMessageArrived.0..");
		onNext(_message);
//        debug.outln(Debug.ERROR, "L4_RxFlux_Channel.asyncMessageArrived.1..");
	}

//-------------------------------------------------------------------------------------
	public void onNext(RxMessage _message){
		for(FlowableEmitter<RxMessage> _emitter : emitterList){
			_emitter.onNext(_message);
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
