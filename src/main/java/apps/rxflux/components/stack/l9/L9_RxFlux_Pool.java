//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.components.stack.l9;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;

import org.apache.commons.lang3.tuple.*;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.*;

import codex.common.utils.*;
import codex.common.apps.rxbasics.*;
import codex.common.apps.rxflux.core.*;
import codex.common.apps.rxflux.events.*;
import codex.common.apps.rxflux.messages.*;
import codex.common.apps.rxflux.components.common.*;
import codex.common.apps.rxflux.components.stack.l7.*;
import codex.common.apps.rxflux.components.stack.l8.*;

//-------------------------------------------------------------------------------------
public class L9_RxFlux_Pool extends RxStatus implements RxPool {
	private L8_RxFlux_FluxMap fluxMap;

    //---rx:
	protected Flowable<RxFlux> flowable;
	protected List<FlowableEmitter<RxFlux>> emitterList;

	//---statics:

//-------------------------------------------------------------------------------------
    public L9_RxFlux_Pool(Debug _debug){
    	this(_debug, -1);
    }

//-------------------------------------------------------------------------------------
    public L9_RxFlux_Pool(Debug _debug, int _listeningPort){
    	super(_debug);

    	emitterList = new ArrayList<FlowableEmitter<RxFlux>>();
    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER).share();
    				
    	fluxMap = new L8_RxFlux_FluxMap(_debug, _listeningPort, this);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public RxPool duplicate() {
		L7_RxFlux_ChannelMap _channelMap = fluxMap.getChannelMap();

		L9_RxFlux_Pool _rxPool = new L9_RxFlux_Pool(debug, _channelMap.getListeningPort());

		List<MutablePair<String, Integer>> _clients = _channelMap.getClients();
		_clients.forEach(_client -> {
			_rxPool.open(_client.getLeft(), _client.getRight());
		});
		return _rxPool;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
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
		return fluxMap.getFlux(_name);
	}

//-------------------------------------------------------------------------------------
	public RxFlux getFluxNonBlocking(String _name){
		return fluxMap.getFluxNonBlocking(_name);
	}

//-------------------------------------------------------------------------------------
	public List<RxFlux> getActiveFluxes(){
		return fluxMap.getActiveFluxes();
	}

//-------------------------------------------------------------------------------------
	public void onNext(RxFlux _rxFlux){
//		debug.outln(Debug.ERROR, "L9_RxFlux_Pool.onNext...0." + _rxFlux);
//			emitter.onNext(_rxFlux);
	    for(FlowableEmitter<RxFlux> _emitter : emitterList){
			debug.outln(Debug.ERROR, "L9_RxFlux_Pool.onNext..." + _rxFlux);
	        _emitter.onNext(_rxFlux);
	    }
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void open(String _host, int _port){
    	fluxMap.open(_host, _port);
    }

//-------------------------------------------------------------------------------------
    public RxFlux create(String _name){
    	return fluxMap.create(_name);
    }

//-------------------------------------------------------------------------------------
    public void remove(RxFlux _rxFlux){
    	fluxMap.remove(_rxFlux);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load(){
    	super.load();
		fluxMap.getFluxes()
			.subscribeOn(Schedulers.io())
			.observeOn(Schedulers.newThread(), false)
			.subscribe(_rxFlux -> onNext(_rxFlux),
						_throwable -> onError(_throwable),
						() -> onCompleted());
    }

//-------------------------------------------------------------------------------------
    public void save(){
    	super.save();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
