//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.components.stack.l8;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.*;

import codex.common.utils.*;
import codex.common.apps.uid.*;
import codex.common.apps.rxbasics.*;
import codex.common.apps.rxflux.core.*;
import codex.common.apps.rxflux.messages.*;
import codex.common.apps.rxflux.messages.internal.*;
import codex.common.apps.rxflux.components.common.*;
import codex.common.apps.rxflux.components.stack.l4.*;

//-------------------------------------------------------------------------------------
public class L8_RxFlux_Flux extends RxStatus implements RxFlux {
	private RxPool rxPool;
	private String name;

	private long startTime;
	private List<L4_RxFlux_Channel> channels;

    //---rx:
	protected Flowable<RxMessage> flowable;
	protected List<FlowableEmitter<RxMessage>> emitterList;

	//---statics:

//-------------------------------------------------------------------------------------
    public L8_RxFlux_Flux(Debug _debug, RxPool _rxPool, String _name) {
    	super(_debug);
		debug = new Debug(null, toString());
    	rxPool = _rxPool;
    	name = _name;

    	startTime = 0;

    	channels  = new ArrayList<L4_RxFlux_Channel>();

    	emitterList = new ArrayList<FlowableEmitter<RxMessage>>();
    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER).share();

    	load();
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void refreshDebugHeaders(){
		debug.setPrefix(0, Debug.IMPORTANT1, toString());
	}

//-------------------------------------------------------------------------------------
    public String getName(){
		return name;
	}

//-------------------------------------------------------------------------------------
    public void setName(String _name){
		name = _name;
	}

//-------------------------------------------------------------------------------------
	public long getStartTime(){
		return startTime;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public void subscribe(FlowableEmitter<RxMessage> _emitter) {
//		emitter = _emitter;
		emitterList.add(_emitter);
    }

//-------------------------------------------------------------------------------------
	public Flowable<RxMessage> getMessages() {
		return flowable;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public RxMessage readMessage(){
//        debug.outln(Debug.ERROR, "L8_RxFlux_Flux.readMessage.0..");
/*		List<RxMessage> _rxMessages = new ArrayList<RxMessage>();
		getMessages()
    			.subscribeOn(Schedulers.computation(), false)
    			.take(1)
    			.blockingForEach(_rxFlux -> { 
        					debug.outln(Debug.ERROR, "L8_RxFlux_Flux.readMessage.xxxx..");
    						_rxMessages.add(_rxFlux);
    					});    			
		RxMessage _rxMessage = _rxMessages.get(0);*/
		RxMessage _rxMessage = flowable.blockingNext().iterator().next();		
//        debug.outln(Debug.ERROR, "L8_RxFlux_Flux.readMessage.1..");
		return _rxMessage;
	}

//-------------------------------------------------------------------------------------
	public void writeMessage(RxMessage _rxMessage){
		channels.forEach(_chanel -> _chanel.writeMessage(_rxMessage));
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public void onNext(RxMessage _rxMessage){
//    	startTime = _rxMessage.getStartTime();
	    for(FlowableEmitter<RxMessage> _emitter : emitterList){
	        _emitter.onNext(_rxMessage);
	    }
	}

//-------------------------------------------------------------------------------------
    public void connect(L4_RxFlux_Channel _rxFluxChannel){
        debug.outln("Connecting ["+this+"] to [" + _rxFluxChannel+ "]...");
        if(!_rxFluxChannel.getChannelDescriptor().getFluxName().equals(getName())){
        	debug.outln(Debug.ERROR, "Cannot connect channel [" + _rxFluxChannel + "] with flux [" + this + "]...");
        	return;
        }
//        setUid(_rxFluxChannel.getChannelDescriptor().getFluxUid());
        refreshDebugHeaders();
//		_rxFluxChannel.getChannelDescriptor().setIndex(channels.size());
		_rxFluxChannel.refreshDebugHeaders();
    	_rxFluxChannel.getMessages()
				.subscribeOn(Schedulers.computation())
				.observeOn(Schedulers.computation(), false)
				.subscribe(_rxMessage -> asyncMessageArrived(_rxMessage),
							_throwable -> onError(_throwable),
							() -> onCompleted()); 

		channels.add(_rxFluxChannel);
		RxSyncMessage _rxSyncMessage = new RxSyncMessage(RxMessage.CODE_OK);
		_rxFluxChannel.writeMessage(_rxSyncMessage);
    }

//-------------------------------------------------------------------------------------
	public void asyncMessageArrived(RxMessage _rxMessage){
		filterRxSyncMessage(_rxMessage);
	}

//-------------------------------------------------------------------------------------
	public void filterRxSyncMessage(RxMessage _rxMessage){
//        debug.outln(Debug.ERROR, "L8_RxFlux_Flux.asyncMessageArrived.0..");
        if(!(_rxMessage instanceof RxSyncMessage)){
			onNext(_rxMessage);
        }
//        debug.outln(Debug.ERROR, "L8_RxFlux_Flux.asyncMessageArrived.1..");
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public RxPool getPool(){
		return rxPool;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public String toString(){
		return "Flux: " + getName();
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
