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
public class L5_RxFlux_ServerChannelFactory extends L5_RxFlux_ChannelFactory {
	private Socket socket;

	protected L4_RxFlux_Channel channel;

//-------------------------------------------------------------------------------------
    public L5_RxFlux_ServerChannelFactory(Debug _debug, Socket _socket) {
    	super(_debug);
    	socket = _socket;
	    load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void run() {
		channel = new L4_RxFlux_Channel(debug, socket);
    	while(isNotStopped()){
        	RxMessage _message = channel.readMessage();
        	processMessage(_message);    		
    	}
    }

//-------------------------------------------------------------------------------------
	private void processMessage(RxMessage _message){
		debug.outln("L5_RxFlux_ServerChannelFactory.processMessage....: " + _message);
    	if(_message instanceof RxSyncMessage){
    		RxSyncMessage _rxSyncMessage = (RxSyncMessage)_message;
    		int _code = _rxSyncMessage.getCode();
    		switch(_code){
    			case RxSyncMessage.REQUEST_SYNC_CHANNEL: {
//    				ChannelDescriptor _channelDescriptor = (ChannelDescriptor)_rxSyncMessage.getContent();
//    				_channelDescriptor.setDebug(debug);
//    				channel.getChannelDescriptor().setType(ChannelDescriptor.TYPE_CHANNEL_SYNC);

//    				channel.setChannelDescriptor(_channelDescriptor);
    				onNext(channel);
					setStatus(STATUS_STOPPED);
					return;
    			}
    			case RxSyncMessage.REQUEST_DATA_CHANNEL: {
    				ChannelDescriptor _channelDescriptor = (ChannelDescriptor)_rxSyncMessage.getContent();
//    				_channelDescriptor.setDebug(debug);
//    				channel.setChannelDescriptor(_channelDescriptor);
    				channel.getChannelDescriptor().setType(ChannelDescriptor.TYPE_CHANNEL_DATA);
    				channel.getChannelDescriptor().setFluxName(_channelDescriptor.getFluxName());
    				channel.refreshDebugHeaders();
    				onNext(channel);
					setStatus(STATUS_STOPPED);
					return;
    			}
    		}
    	}
		debug.outln(Debug.ERROR, "First message of connection should be request command: " + _message);
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
		channel.save();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
