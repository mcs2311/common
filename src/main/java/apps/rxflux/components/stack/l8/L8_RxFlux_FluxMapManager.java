//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.components.stack.l8;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.*;

import codex.common.utils.*;
import codex.common.apps.rxbasics.*;
import codex.common.apps.rxflux.core.*;
import codex.common.apps.rxflux.components.common.*;
import codex.common.apps.rxflux.components.stack.l4.*;
import codex.common.apps.rxflux.components.stack.l7.*;

//-------------------------------------------------------------------------------------
public class L8_RxFlux_FluxMapManager extends RxBase {
	private L8_RxFlux_FluxMap fluxMap;
	private L7_RxFlux_ChannelMap channelMap;
	private RxPool rxPool;

	//---cache:
	private Map<String, L8_RxFlux_Flux> fluxMapCache;

    
	//---statics:

//-------------------------------------------------------------------------------------
    public L8_RxFlux_FluxMapManager(Debug _debug, L8_RxFlux_FluxMap _fluxMap, L7_RxFlux_ChannelMap _channelMap, RxPool _rxPool) {
    	super(_debug);
    	rxPool = _rxPool;

    	fluxMap = _fluxMap;
    	channelMap = _channelMap;

    	fluxMapCache = fluxMap.getMap();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public L8_RxFlux_Flux create(String _fluxName){
		L8_RxFlux_Flux _flux = new L8_RxFlux_Flux(debug, rxPool, _fluxName);
		add(_flux);
		announceSyncChannels(_flux);
    	return _flux;
    }

//-------------------------------------------------------------------------------------
    public RxFlux connect(L4_RxFlux_Channel _rxFluxChannel){
//    	L4_RxFlux_Channel _channel = (L4_RxFlux_Channel)_rxFlux;
    	String _fluxName = _rxFluxChannel.getChannelDescriptor().getFluxName();
        debug.outln("Connect ["+_rxFluxChannel+"] to map ...");
//    	map.put(_uid, _flux);

//    	L8_RxFlux_Flux _rxFlux = getFlux(_uid);
    	L8_RxFlux_Flux _rxFlux = fluxMapCache.get(_fluxName);
    	if(_rxFlux == null){
			_rxFlux = new L8_RxFlux_Flux(debug, rxPool, _fluxName);
			add(_rxFlux);
    	}
        _rxFlux.connect(_rxFluxChannel);
        if(_rxFluxChannel.getChannelDescriptor().isDataNew()){
        	return _rxFlux;
        }
    	return null;
    }

//-------------------------------------------------------------------------------------
    public void feed(L4_RxFlux_Channel _rxFluxChannel){
        debug.outln(Debug.IMPORTANT3, "Feeding ["+_rxFluxChannel+"]  ...");
        List<String> _uidList = new ArrayList<String>(fluxMapCache.keySet());
    	_rxFluxChannel.announce(_uidList); 
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void add(L8_RxFlux_Flux _rxFlux){
        debug.outln(Debug.IMPORTANT3, "Adding ["+_rxFlux+"] to map ...");
    	fluxMapCache.put(_rxFlux.getName(), _rxFlux);
    }

//-------------------------------------------------------------------------------------
    public void remove(L8_RxFlux_Flux _rxFlux){
    	fluxMapCache.remove(_rxFlux);
    }

//-------------------------------------------------------------------------------------
    public void announceSyncChannels(L8_RxFlux_Flux _rxFlux){
    	channelMap.announceSyncChannels(_rxFlux.getName());
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
