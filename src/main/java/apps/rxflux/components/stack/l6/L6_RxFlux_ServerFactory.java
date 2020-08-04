//-------------------------------------------------------------------------------------
package codex.common.apps.rxflux.components.stack.l6;
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
import codex.common.apps.rxflux.components.stack.l4.*;
import codex.common.apps.rxflux.components.stack.l5.*;

//-------------------------------------------------------------------------------------
public class L6_RxFlux_ServerFactory extends RxStatus implements FlowableOnSubscribe<L4_RxFlux_Channel> {
	private int listeningPort;
	private List<L5_RxFlux_ServerChannelFactory> serverFluxFactories;

    //---rx:
	protected Flowable<L4_RxFlux_Channel> flowable;
	protected FlowableEmitter<L4_RxFlux_Channel> emitter;

//-------------------------------------------------------------------------------------
    public L6_RxFlux_ServerFactory(Debug _debug, int _listeningPort) {
    	super(_debug);
    	listeningPort = _listeningPort;

    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER).share();

		serverFluxFactories = new ArrayList<L5_RxFlux_ServerChannelFactory>();

//    	start();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void run() {
        debug.outln(Debug.IMPORTANT3, "Starting server at port ["+listeningPort+"] ...");
        try {
            ServerSocket _listener = new ServerSocket(listeningPort);
//            debug.outln("listen...0");
            while(getStatus() != STATUS_STOPPED) {
                Socket _socket = _listener.accept();
//        		debug.outln(Debug.IMPORTANT3, "L6_RxFlux_ServerFactory _socket = ["+_socket+"] ...");
	    		L5_RxFlux_ServerChannelFactory _serverChannelFactory = new L5_RxFlux_ServerChannelFactory(debug, _socket);
	    		add(_serverChannelFactory);
	    		_serverChannelFactory.start();
            }
        } catch(IOException _e){
        	onError(_e);
        }
    }

//-------------------------------------------------------------------------------------
    private void add(L5_RxFlux_ServerChannelFactory _serverChannelFactory) {
    	_serverChannelFactory.getRxFluxChannel()
			.subscribeOn(Schedulers.computation())
			.observeOn(Schedulers.computation(), false)
			.subscribe(_rxFluxChannel -> onNext(_rxFluxChannel),
						_throwable -> onError(_throwable),
						() -> onCompleted());
						 
		serverFluxFactories.add(_serverChannelFactory);
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<L4_RxFlux_Channel> _emitter) throws Exception {
		emitter = _emitter;
    }

//-------------------------------------------------------------------------------------
	public Flowable<L4_RxFlux_Channel> getRxFluxChannel() {
		return flowable;
    }

//-------------------------------------------------------------------------------------
	public void onNext(L4_RxFlux_Channel _rxFluxChannel){
		if(emitter != null){
			emitter.onNext(_rxFluxChannel);
		}
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
		debug.outln("Shuting down...");
        try{
            new Socket("localhost", listeningPort);
        }catch(IOException _e){
        }
    	serverFluxFactories.forEach(_serverChannelFactory -> _serverChannelFactory.save());
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
