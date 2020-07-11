//-------------------------------------------------------------------------------------
package codex.common.math.numbers;
//-------------------------------------------------------------------------------------
import java.nio.*;
import java.util.*;
import java.math.*;
import java.nio.charset.*;

import codex.common.utils.*;

//-------------------------------------------------------------------------------------
public class OrderedNumber {
	private Debug debug;
	private BigInteger number;

//-------------------------------------------------------------------------------------
    public OrderedNumber(Debug _debug, String _number, String _order) {
    	debug = _debug;
		debug.outln(Debug.ERROR, "OrderedNumber.0 _number ="+_number+", _order="+_order);

		ByteBuffer _buffer = ByteBuffer.wrap(StringUtils.toBytes(_number));
		if(_order.equals("little_endian")){
			_buffer = _buffer.order(ByteOrder.LITTLE_ENDIAN);
		} else if(_order.equals("big_endian")){
			_buffer = _buffer.order(ByteOrder.BIG_ENDIAN);
		} else {
			debug.outln(Debug.ERROR, "Unknwon order in StringUtils.getBit");
		}
//		String _s = new String(_buffer);
//		byte[] _bytes = _buffer.getBytes(StandardCharsets.UTF_8);
//		String _s = new String( _bytes, StandardCharsets.UTF_8 );
//		Charset _charset = Charset.forName("ISO-8859-1");
//		String _s = _charset.decode(_buffer).toString();
//			debug.outln(Debug.ERROR, ">>>_s="+_s);
		IntBuffer _intBuffer = _buffer.asIntBuffer();
		int _capacity = _intBuffer.capacity();
		if(_capacity < 1){
			if(_number.startsWith("0x")){
				_number = _number.replace("0x", "");
			}
        	number = new BigInteger(_number, 16); 
		} else{
			int[] _ints = new int[_capacity];
			_intBuffer.get(_ints);
			String _s = StringUtils.getHex(_ints);
			debug.outln(Debug.ERROR, "OrderedNumber.1 _number =" + _s);
	        number = new BigInteger(_s, 16); 
		}
	}

//-------------------------------------------------------------------------------------
    public int getBit(int _index){
//        long _n = Long.decode(_number);
//        return (int)((long)(_n >> _index) & 1);

        return number.testBit(_index) ? 1 : 0;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
