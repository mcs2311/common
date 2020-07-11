//-------------------------------------------------------------------------------------
package codex.common.utils;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

//-------------------------------------------------------------------------------------
public class Saver {
    private FileWriter fw;
    private String filename;

//-------------------------------------------------------------------------------------
    public Saver(String _filename){
        this(null, _filename);
    }


//-------------------------------------------------------------------------------------
    public Saver(String _directory, String _filename){
        String _path = "";
        filename = _filename;

        if(_directory != null){
            _path = _directory;
			FileUtils.ifDoesntExistCreate(_path);
        }
        try{

             fw = new FileWriter(_path + filename);
        } catch(IOException _e){
            _e.printStackTrace();
        }
    }

//-------------------------------------------------------------------------------------
    public void save(String _line) {
        try{
            fw.write(_line); 
            fw.flush();
        } catch(IOException _e){
            _e.printStackTrace();
        }
    }

//-------------------------------------------------------------------------------------
    public String getFilename() {
        return filename;
    }

//-------------------------------------------------------------------------------------
    public void close() {
        try{
            fw.flush();
        } catch(IOException _e){
            _e.printStackTrace();
        }
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
