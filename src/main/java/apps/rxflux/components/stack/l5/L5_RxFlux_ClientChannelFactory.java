//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.components.stack.l5;
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
import codex.common.apps.rxflux.events.*;
import codex.common.apps.rxflux.messages.*;
import codex.common.apps.rxflux.messages.internal.*;
import codex.common.apps.rxflux.components.common.*;
import codex.common.apps.rxflux.components.stack.l4.*;

//-------------------------------------------------------------------------------------
public class L5_RxFlux_ClientChannelFactory extends L5_RxFlux_ChannelFactory {
	protected RxPool rxPool;

	protected L4_RxFlux_Channel syncChannel;
	protected List<L4_RxFlux_Channel> dataChannels;

//-------------------------------------------------------------------------------------
    public L5_RxFlux_ClientChannelFactory(Debug _debug, String _hostname, int _port, RxPool _rxPool) {
    	super(_debug);
    	rxPool = _rxPool;

		syncChannel = new L4_RxFlux_Channel(debug, _hostname, _port);
    	dataChannels = new ArrayList<L4_RxFlux_Channel>();
		load();
    }

//-------------------------------------------------------------------------------------
    public void run(){
//        debug.outln(Debug.ERROR, "L5_RxFlux_ClientChannelFactory.run...0.");
//		commandFlux.load();
		RxSyncMessage _rxSyncMessage = new RxSyncMessage(RxSyncMessage.REQUEST_SYNC_CHANNEL, syncChannel.getChannelDescriptor());
		syncChannel.writeMessage(_rxSyncMessage);
//        debug.outln(Debug.ERROR, "L5_RxFlux_ClientChannelFactory.run...1.");
//    	onNext(syncChannel);
    	while(isNotStopped()){
        	RxMessage _message = syncChannel.readMessage();
        	processMessage(_message);    		
    	}    	
    }

@SuppressWarnings("unchecked")
//-------------------------------------------------------------------------------------
	private void processMessage(RxMessage _message){
    	if(_message instanceof RxSyncMessage){
    		RxSyncMessage _rxSyncMessage = (RxSyncMessage)_message;
    		int _code = _rxSyncMessage.getCode();
    		switch(_code){
    			case RxSyncMessage.ANNOUNCE_DATA_CHANNELS: {
    				List<String> _fluxNameList = (List<String>)_rxSyncMessage.getContent();
    				_fluxNameList.forEach(_fluxName -> openDataChannel(_fluxName));
					return;
    			}
    		}
    	}
		debug.outln(Debug.ERROR, "Unknown message for sync channel: " + syncChannel + " : " + _message);
	}

//-------------------------------------------------------------------------------------
	private synchronized void openDataChannel(String _fluxName){
//		debug.outln("L5_RxFlux_ClientChannelFactory.openDataChannel...0... " + _fluxName);
		if(!existsChannel(_fluxName)){
//			debug.outln("L5_RxFlux_ClientChannelFactory.openDataChannel...1... ");
			ChannelDescriptor _channelDescriptor = new ChannelDescriptor(debug, syncChannel.getChannelDescriptor(), _fluxName);
			_channelDescriptor.setType(ChannelDescriptor.TYPE_CHANNEL_DATA);
			_channelDescriptor.setMode(ChannelDescriptor.MODE_CHANNEL_CLIENT);
			L4_RxFlux_Channel _dataChannel = new L4_RxFlux_Channel(debug, _channelDescriptor);
			RxSyncMessage _rxSyncMessage = new RxSyncMessage(RxSyncMessage.REQUEST_DATA_CHANNEL, _channelDescriptor);
			_dataChannel.writeMessage(_rxSyncMessage);

			RxMessage _rxMessage = _dataChannel.readMessage();
//			debug.outln("L5_RxFlux_ClientChannelFactory.openDataChannel...2... " + _rxMessage);
			if(_rxMessage.getCode() == RxMessage.CODE_OK){
//				debug.outln("Data channel["+_fluxName+"] confirmed!!!");
				dataChannels.add(_dataChannel);
				onNext(_dataChannel);
			}
		} else {
			debug.outln(Debug.ERROR, "Channel exists already in the client!!!????");
		}
	}

//-------------------------------------------------------------------------------------
	private boolean existsChannel(String _fluxName){
/*		for(int i = 0; i < dataChannels.size(); i++){
			L4_RxFlux_Channel _dataChannel = dataChannels.get(i).getFlux();
			if(_dataChannel.getUid() == _channelId){
				return true;
			}
		}
		return false;*/
		return (rxPool.getFluxNonBlocking(_fluxName) != null);
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
		syncChannel.save();
		dataChannels.forEach(_dataChannel -> _dataChannel.save());
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
