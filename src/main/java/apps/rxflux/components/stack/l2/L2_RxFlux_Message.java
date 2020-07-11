//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.components.stack.l2;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.*;

import codex.common.utils.*;
import codex.common.apps.rxbasics.*;
import codex.common.apps.rxflux.core.*;
import codex.common.apps.rxflux.events.*;
import codex.common.apps.rxflux.messages.*;
import codex.common.apps.rxflux.components.common.*;
import codex.common.apps.rxflux.components.stack.l1.*;

//-------------------------------------------------------------------------------------
public class L2_RxFlux_Message extends L1_RxFlux_ZipObject {

    private L2_RxFlux_WriteQueue writeQueue;

//-------------------------------------------------------------------------------------
    public L2_RxFlux_Message(Debug _debug, ChannelDescriptor _channelDescriptor, Socket _socket) {
    	super(_debug, _channelDescriptor, _socket);
    	writeQueue = new L2_RxFlux_WriteQueue(_debug, this);
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------
    public RxMessage readMessage() throws Throwable {
    	RxMessage _rxMessage = (RxMessage)super.readObject();
//    	debug.outln(Debug.WARNING, "Reading... " + _rxMessage);
    	return _rxMessage;
    }

//-------------------------------------------------------------------------------------
    public void writeMessage(RxMessage _rxMessage) throws Throwable {
//    	debug.outln(Debug.WARNING, "Writing into queue... " + _rxMessage);
    	writeQueue.writeMessage(_rxMessage);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();
    	writeQueue.load();
    }

//-------------------------------------------------------------------------------------
    public void save() {
    	super.save();
    	writeQueue.save();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
