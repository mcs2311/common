//-------------------------------------------------------------------------------------
package codex.common.apps.squizer;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.zip.*;
//import java.math.*;
//import java.text.*;

//-------------------------------------------------------------------------------------
public class Squizer {
	private int level;
	public static final int DEFAULT_LEVEL = Deflater.DEFAULT_COMPRESSION;

//-------------------------------------------------------------------------------------
	public Squizer() {
		this(DEFAULT_LEVEL);
	}

//-------------------------------------------------------------------------------------
	public Squizer(int _level) {
		level = _level;
	}

//-------------------------------------------------------------------------------------
	public static byte[] squize(Object _o) {
		return squize(_o, DEFAULT_LEVEL);
	}

//-------------------------------------------------------------------------------------
	public static byte[] squize(Object _o, int _level) {
		return new Squizer(_level).squizeObject(_o);
	}

//-------------------------------------------------------------------------------------
	public byte[] squizeObject(Object _o) {
		try {
			int _initialSize = estimate(_o);
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream(_initialSize);

			GZIPOutputStream zipOutputStream = new GZIPOutputStream(byteStream);
//			zipOutputStream.setLevel(level);

			ObjectOutputStream objectOutputStream = new ObjectOutputStream(zipOutputStream);
			objectOutputStream.writeObject(_o);

			//close resources
			objectOutputStream.flush();
			objectOutputStream.close();
			zipOutputStream.close();
			byteStream.close();
			return byteStream.toByteArray();
		} catch (Exception _e) {
			_e.printStackTrace();
		}
		return null;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public static Object unsquize(byte[] _bytes) {
		return unsquize(_bytes, DEFAULT_LEVEL);
	}

//-------------------------------------------------------------------------------------
	public static Object unsquize(byte[] _bytes, int _level) {
		return new Squizer(_level).unsquizeObject(_bytes);
	}

//-------------------------------------------------------------------------------------
	public Object unsquizeObject(byte[] _bytes) {
		try {
			ByteArrayInputStream byteStream = new ByteArrayInputStream(_bytes);

			GZIPInputStream zipInputStream = new GZIPInputStream(byteStream);
//			zipInputStream.setLevel(level);

			ObjectInputStream objectInputStream = new ObjectInputStream(zipInputStream);
			Object _o = objectInputStream.readObject();

			//close resources
			objectInputStream.close();
			zipInputStream.close();
			byteStream.close();
			return _o;
		} catch (Exception _e) {
			_e.printStackTrace();
		}
		return null;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	private int estimate(Object _o) {
		return 100000;
	}
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------

