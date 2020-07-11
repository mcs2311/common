//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.components.stack.l7;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
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
import codex.common.apps.rxflux.components.stack.l4.*;
import codex.common.apps.rxflux.components.stack.l5.*;
import codex.common.apps.rxflux.components.stack.l6.*;

//-------------------------------------------------------------------------------------
public class L7_RxFlux_ChannelMap extends RxStatus implements FlowableOnSubscribe<L4_RxFlux_Channel> {
	private int listeningPort;
	private RxPool rxPool;


	private Map<Long, L4_RxFlux_Channel> channelMap;
	private List<MutablePair<String, Integer>> clients;

    //---rx:
	protected Flowable<L4_RxFlux_Channel> flowable;
	protected List<FlowableEmitter<L4_RxFlux_Channel>> emitterList;

	//---statics:

//-------------------------------------------------------------------------------------
    public L7_RxFlux_ChannelMap(Debug _debug, int _listeningPort, RxPool _rxPool) {
    	super(_debug);
    	listeningPort = _listeningPort;
    	rxPool = _rxPool;

		emitterList = new ArrayList<FlowableEmitter<L4_RxFlux_Channel>>();
    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER).share();

    	channelMap = new HashMap<Long, L4_RxFlux_Channel>();
    	clients = new ArrayList<MutablePair<String, Integer>>();


    	if(_listeningPort > 0){
    		L6_RxFlux_ServerFactory _serverFactory = new L6_RxFlux_ServerFactory(debug, _listeningPort);

			_serverFactory
				.getRxFluxChannel()
				.subscribeOn(Schedulers.computation())
				.observeOn(Schedulers.computation(), false)
				.subscribe(_rxFlux -> onNext(_rxFlux),
							_throwable -> onError(_throwable),
							() -> onCompleted()); 
    		
    		_serverFactory.start();
    	}
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
//        debug.outln(Debug.ERROR, "L7_RxFlux_ChannelMap.onNext...0..." + _rxFluxChannel);
        long _channelUid = _rxFluxChannel.getChannelDescriptor().getChannelUid();
    	channelMap.put(_channelUid, _rxFluxChannel);
		if(_rxFluxChannel != null){
//    		L4_RxFlux_Channel _channel = (L4_RxFlux_Channel)_rxFlux;
//			if(_rxFluxChannel.getChannelDescriptor().isDataNew()){
//        		debug.outln(Debug.ERROR, "L7_RxFlux_ChannelMap.onNext...1..." + _rxFluxChannel);
//				emitter.onNext(_rxFluxChannel);
			    for(FlowableEmitter<L4_RxFlux_Channel> _emitter : emitterList){
			        _emitter.onNext(_rxFluxChannel);
			    }        		
//        		debug.outln(Debug.ERROR, "L7_RxFlux_ChannelMap.onNext...2..." + _rxFluxChannel);
//			}
		}
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void open(String _host, int _port){
		clients.add(new MutablePair(_host, _port));
		L5_RxFlux_ClientChannelFactory _clientChannelFactory = new L5_RxFlux_ClientChannelFactory(debug, _host, _port, rxPool);
		
		_clientChannelFactory.getRxFluxChannel()
			.subscribeOn(Schedulers.io())
			.observeOn(Schedulers.newThread(), false)
			.subscribe(_rxFlux -> onNext(_rxFlux),
						_throwable -> onError(_throwable),
						() -> onCompleted());
		
		_clientChannelFactory.start();
    }

//-------------------------------------------------------------------------------------
    public void announceSyncChannels(String _fluxName){
        debug.outln(Debug.ERROR, "L7_RxFlux_ChannelMap.announceSyncChannels.... for _fluxName = " + _fluxName);
    	channelMap.forEach((_k, _v) -> {
			L4_RxFlux_Channel _rxFluxChannel0 = (L4_RxFlux_Channel)_v;
			if(_rxFluxChannel0.getChannelDescriptor().isSyncType()){
				_rxFluxChannel0.announce(_fluxName);				
			}
    	});
    }


//-------------------------------------------------------------------------------------
    public int getListeningPort(){
    	return listeningPort;
    }

//-------------------------------------------------------------------------------------
    public List<MutablePair<String, Integer>> getClients(){
    	return clients;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();
    	channelMap.forEach((_k, _v) -> _v.load()); 
    }

//-------------------------------------------------------------------------------------
    public void save() {
    	super.save();
    	channelMap.forEach((_k, _v) -> _v.save()); 
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
