//-------------------------------------------------------------------------------------
package codex.common.system.audio.player;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import javax.sound.sampled.*;

import codex.common.utils.*;

//-------------------------------------------------------------------------------------
public class AudioPlayer {
    private Debug debug;
    private String name;

    private AudioInputStream audioInputStream;
    private Clip clip;

//-------------------------------------------------------------------------------------
    public AudioPlayer(Debug _debug, String _name) {
        debug = _debug;
        name = _name;
        try{
//            String _soundFileName = System.getenv("WAVES_LIBS_PATH") +"/sounds/" + name + ".wav";    
	        InputStream _src = this.getClass().getClassLoader().getResourceAsStream(_name + ".wav");
            audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(_src));
            audioInputStream.mark(Integer.MAX_VALUE);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
//			FloatControl _gainControl = 
//    				(FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//			_gainControl.setValue(-10.0f); // Reduce volume by 10 decibels.            
//            _clip.start();
        }catch(UnsupportedAudioFileException _e0){
            debug.outln(Debug.ERROR, "UnsupportedAudioFileException" + _e0.getMessage());
        }catch(IOException _e1){
            debug.outln(Debug.ERROR, "IOException" + _e1.getMessage());
        } catch(LineUnavailableException _e2){
            debug.outln(Debug.ERROR, "LineUnavailableException" + _e2.getMessage());
        }
    }

//-------------------------------------------------------------------------------------
    public synchronized void play(){
    	if(audioInputStream == null){
    		debug.outln(Debug.ERROR, "AudioPlayer: audioInputStream not ready!");
    		return;
    	}
//        try{
//            audioInputStream.reset();
            clip.setFramePosition(0);
            clip.start();
/*        }catch(IOException _e0){
            debug.outln(Debug.ERROR, "IOException"+_e0.getMessage());
        } catch(LineUnavailableException _e1){
            debug.outln(Debug.ERROR, "LineUnavailableException" + _e1.getMessage());
        }*/
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
