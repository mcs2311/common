//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.components.stack.l0;
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
import codex.common.apps.rxflux.components.common.*;

//-------------------------------------------------------------------------------------
public class L0_RxFlux_Network extends RxStatus {
    protected Socket socket;
	protected ChannelDescriptor channelDescriptor;

//-------------------------------------------------------------------------------------
    public L0_RxFlux_Network(Debug _debug, ChannelDescriptor _channelDescriptor, Socket _socket) {
        super(_debug);
    	channelDescriptor = _channelDescriptor;
        socket = _socket;
		debug = new Debug(null, channelDescriptor.toString());
//        refreshDebugHeaders();
	}

//-------------------------------------------------------------------------------------
    public void refreshDebugHeaders(){
		String _prefixText = channelDescriptor.toString();
		debug.setPrefix(0, Debug.IMPORTANT1, _prefixText);
	}

//-------------------------------------------------------------------------------------
    protected OutputStream getOutputStream() throws IOException {
    	OutputStream _outputStream = socket.getOutputStream();
    	return _outputStream;
	}

//-------------------------------------------------------------------------------------
    protected InputStream getInputStream() throws IOException {
		InputStream _inputStream = socket.getInputStream();
		return _inputStream;
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
