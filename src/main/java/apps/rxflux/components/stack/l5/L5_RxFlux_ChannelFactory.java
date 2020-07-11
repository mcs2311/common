//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.components.stack.l5;
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
import codex.common.apps.rxflux.components.stack.l4.*;

//-------------------------------------------------------------------------------------
public class L5_RxFlux_ChannelFactory extends RxStatus implements FlowableOnSubscribe<L4_RxFlux_Channel> {
    
    //---rx:
	private Flowable<L4_RxFlux_Channel> flowable;
	private List<FlowableEmitter<L4_RxFlux_Channel>> emitterList;

//-------------------------------------------------------------------------------------
    public L5_RxFlux_ChannelFactory(Debug _debug) {
    	super(_debug);
    	emitterList = new ArrayList<FlowableEmitter<L4_RxFlux_Channel>>();
    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER).share();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<L4_RxFlux_Channel> _emitter) throws Exception {
		emitterList.add(_emitter);
    }

//-------------------------------------------------------------------------------------
	public Flowable<L4_RxFlux_Channel> getRxFluxChannel() {
		return flowable;
    }

//-------------------------------------------------------------------------------------
    public void onNext(L4_RxFlux_Channel _rxFluxChannel){
//        debug.outln(Debug.ERROR, "L5_RxFlux_ChannelFactory.onNext...0.");
//		if(emitter != null){
	    for(FlowableEmitter<L4_RxFlux_Channel> _emitter : emitterList){
	        _emitter.onNext(_rxFluxChannel);
	    }        
//		debug.outln(Debug.ERROR, "L5_RxFlux_ChannelFactory.onNext...1.");
//			emitter.onNext(_rxFluxChannel);
//		}    		
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void run() {
		debug.outln(Debug.ERROR, "This method should never be run!!!");
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	setStatus(STATUS_RUNNING);    	
    	super.load();
    }

//-------------------------------------------------------------------------------------
    public void save() {
    	super.save();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
