//-------------------------------------------------------------------------------------
package codex.common.math.complex.sigmoids;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.nio.*;
import java.util.*;
import java.math.*;
import java.text.*;

//import org.apache.commons.math3.analysis.function.*;

//-------------------------------------------------------------------------------------
public class HalfSigmoidNormalized {
//    protected Sigmoid sigmoid;
	protected float acceleration;

//-------------------------------------------------------------------------------------
    public HalfSigmoidNormalized(float _acceleration) {
//    	sigmoid = new Sigmoid();
    	acceleration = _acceleration;
	} 

//-------------------------------------------------------------------------------------
	public float value(float _value){
//		return (float)sigmoid.value(_value);
		float _result = 0f;
		if(_value > 0f){
			_value = acceleration * _value;
			double _exp = Math.exp(_value) - 1.0f;
			_result = (float)(1f - (1f/(1f + _exp)));
		}
		return _result;
	}

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------

   