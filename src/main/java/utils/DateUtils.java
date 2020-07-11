//-------------------------------------------------------------------------------------
package codex.common.utils;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.nio.*;
import java.util.*;
import java.text.*;

//-------------------------------------------------------------------------------------
public class DateUtils {


//-------------------------------------------------------------------------------------
    public static String formatTime(long _time) {
        String __time = String.format("%04d", _time/3600) + ":" +
                        String.format("%02d", (_time/60)%60) + ":" + 
                        String.format("%02d", _time%60);
        return __time;
    }

//-------------------------------------------------------------------------------------
    public static String getDateAndTime() {
        SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy_MM_dd_H_m_s");
        return _dateFormat.format(new Date());
//        return __time;
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------

   