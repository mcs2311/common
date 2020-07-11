//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.components.stack.l1;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.*;

import codex.common.utils.*;
import codex.common.apps.rxbasics.*;
import codex.common.apps.rxflux.core.*;
import codex.common.apps.rxflux.events.*;
//import codex.common.apps.rxflux.messages.*;
import codex.common.apps.rxflux.components.common.*;
import codex.common.apps.rxflux.components.stack.l0.*;

//-------------------------------------------------------------------------------------
public class L1_RxFlux_ZipObject extends L0_RxFlux_Network {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

//-------------------------------------------------------------------------------------
    public L1_RxFlux_ZipObject(Debug _debug, ChannelDescriptor _channelDescriptor, Socket _socket) {
        super(_debug, _channelDescriptor, _socket);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    protected void createStreams(Object _object) throws Throwable {
//    	debug.outln(Debug.WARNING, "L1_RxFlux_ZipObject.createStreams..0.");
    	OutputStream _outputStream = getOutputStream();
		if(_outputStream != null){
			GZIPOutputStream _gos = new GZIPOutputStream(_outputStream, true);
			oos = new ObjectOutputStream(_gos);
			writeObject(_object);
		} else {
			throw new Exception("OutputStream is null in L1_RxFlux_ZipObject!");
		}
		
//    	debug.outln(Debug.WARNING, "L1_RxFlux_ZipObject.createStreams..1.");
		InputStream _inputStream = getInputStream();
		if(_inputStream != null){
			GZIPInputStream _gis = new GZIPInputStream(_inputStream);
			ois = new ObjectInputStream(_gis);
		} else {
			throw new Exception("InputStream is null in L1_RxFlux_ZipObject!");
		}
//    	debug.outln(Debug.WARNING, "L1_RxFlux_ZipObject.createStreams..2.");
    }

//-------------------------------------------------------------------------------------
    public Object readObject() throws Throwable {
//    	debug.outln(Debug.WARNING, "L1_RxFlux_ZipObject.readObject... ");
    	return ois.readObject();    		
    }

//-------------------------------------------------------------------------------------
    public void writeObject(Object _object) throws Throwable  {
    	oos.writeObject(_object);
    	oos.flush();
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
    	try{
            oos.flush();
        } catch (IOException _e) {
            _e.printStackTrace();
        }
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
