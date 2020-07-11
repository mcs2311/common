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
import codex.common.apps.rxflux.components.common.*;
import codex.common.apps.rxflux.components.stack.l4.*;
import codex.common.apps.rxflux.components.stack.l7.*;

//-------------------------------------------------------------------------------------
public class L8_RxFlux_FluxMap extends RxStatus implements FlowableOnSubscribe<RxFlux> {
	private Map<String, L8_RxFlux_Flux> fluxMap;
    private L7_RxFlux_ChannelMap channelMap;
    private RxPool rxPool;

    private L8_RxFlux_FluxMapManager fluxMapManager;

    //---rx:
	protected Flowable<RxFlux> flowable;
	protected List<FlowableEmitter<RxFlux>> emitterList;

	//---statics:

//-------------------------------------------------------------------------------------
    public L8_RxFlux_FluxMap(Debug _debug, int _listeningPort, RxPool _rxPool) {
    	super(_debug);
    	rxPool = _rxPool;

    	emitterList = new ArrayList<FlowableEmitter<RxFlux>>();
    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER).share();

    	fluxMap = new HashMap<String, L8_RxFlux_Flux>();
    	channelMap = new L7_RxFlux_ChannelMap(_debug, _listeningPort, _rxPool);
    	fluxMapManager = new L8_RxFlux_FluxMapManager(_debug, this, channelMap, _rxPool);

		channelMap.getRxFluxChannel()
			.subscribeOn(Schedulers.newThread())
			.observeOn(Schedulers.computation(), false)
			.subscribe(_rxFluxChannel -> onNext(_rxFluxChannel),
						_throwable -> onError(_throwable),
						() -> onCompleted());
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<RxFlux> _emitter) {
//		emitter = _emitter;
		emitterList.add(_emitter);
    }

//-------------------------------------------------------------------------------------
	public Flowable<RxFlux> getFluxes() {
		return flowable;
    }

//-------------------------------------------------------------------------------------
	public RxFlux getFlux(String _name){
		L8_RxFlux_Flux _rxFlux = fluxMap.get(_name);
		while(_rxFlux == null){
			_rxFlux = getFluxBlocking(_name);
		}
		return _rxFlux;
	}

//-------------------------------------------------------------------------------------
	public RxFlux getFluxNonBlocking(String _name){
		L8_RxFlux_Flux _rxFlux = fluxMap.get(_name);
		return _rxFlux;
	}

//-------------------------------------------------------------------------------------
	public List<RxFlux> getActiveFluxes(){
        return new ArrayList<RxFlux>(fluxMap.values());
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void onNext(L4_RxFlux_Channel _rxFluxChannel){
//        debug.outln(Debug.ERROR, "L8_RxFlux_FluxMap.onNext...0..." + _rxFluxChannel);
        ChannelDescriptor _channelDescriptor = _rxFluxChannel.getChannelDescriptor();
        int _type = _channelDescriptor.getType();
        if(_type == ChannelDescriptor.TYPE_CHANNEL_SYNC){
        	fluxMapManager.feed(_rxFluxChannel);
        } else if(_type == ChannelDescriptor.TYPE_CHANNEL_DATA){
	    	RxFlux _rxFlux = fluxMapManager.connect(_rxFluxChannel);
			if(_rxFlux != null){
//	    		debug.outln(Debug.ERROR, "L8_RxFlux_FluxMap.onNext...1." + _rxFlux);
//				emitter.onNext(_rxFlux);				
			    for(FlowableEmitter<RxFlux> _emitter : emitterList){
			        _emitter.onNext(_rxFlux);
			    }
//	    		debug.outln(Debug.ERROR, "L8_RxFlux_FluxMap.onNext...2." + _rxFlux);
			}
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void open(String _host, int _port){
		channelMap.open(_host, _port);
    }

//-------------------------------------------------------------------------------------
    public RxFlux create(String _name){
    	return fluxMapManager.create(_name);
    }

//-------------------------------------------------------------------------------------
    public void remove(RxFlux _rxFlux){
    	fluxMapManager.remove((L8_RxFlux_Flux)_rxFlux);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public L8_RxFlux_Flux getFluxBlocking(String _name){
        debug.outln(Debug.ERROR, "L8_RxFlux_FluxMap.getFluxBlocking.0: " + _name);
//		L8_RxFlux_Flux _rxFlux = (L8_RxFlux_Flux)cachedFlowable.subscribeOn(Schedulers.computation(), false).blockingNext();    			

		L8_RxFlux_Flux _rxFlux = (L8_RxFlux_Flux)flowable.cache().blockingNext().iterator().next();

        debug.outln(Debug.ERROR, "L8_RxFlux_FluxMap.getFluxBlocking.1..");
        if(_rxFlux.getName().equals(_name)){
			return _rxFlux;        	
        } else {
        	return null;
        }
	}


/*
//-------------------------------------------------------------------------------------
	public L8_RxFlux_Flux getFluxBlocking(long _uid){
        debug.outln(Debug.ERROR, "L8_RxFlux_FluxMap.getFluxBlocking.0..");
		List<RxFlux> _rxFluxs = new ArrayList<RxFlux>();
		flowable.subscribeOn(Schedulers.computation(), false)
    			.take(1)
    			.blockingLast(_rxFlux -> { 
        					debug.outln(Debug.ERROR, "L8_RxFlux_FluxMap.getFluxBlocking.xxxx..");
    						_rxFluxs.add(_rxFlux);
    					});    			
		L8_RxFlux_Flux _rxFlux = (L8_RxFlux_Flux)_rxFluxs.get(0);
        debug.outln(Debug.ERROR, "L8_RxFlux_FluxMap.getFluxBlocking.1..");
        if(_rxFlux.getUid() == _uid){
			return _rxFlux;        	
        } else {
        	return null;
        }
	}
*/
//-------------------------------------------------------------------------------------
	public Map<String, L8_RxFlux_Flux> getMap(){
		return fluxMap;
	}

//-------------------------------------------------------------------------------------
	public L7_RxFlux_ChannelMap getChannelMap(){
		return channelMap;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();
    	fluxMap.forEach((_k, _v) -> _v.load()); 
    	fluxMapManager.load();
    	channelMap.load();
    }

//-------------------------------------------------------------------------------------
    public void save() {
    	super.save();
    	fluxMap.forEach((_k, _v) -> _v.save()); 
    	fluxMapManager.save();
    	channelMap.save();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
